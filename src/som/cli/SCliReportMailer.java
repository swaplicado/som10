/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import erp.lib.SLibUtilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import som.mod.som.db.SDbInputCategory;
import som.mod.som.db.SDbItem;

/**
 *
 * @author Sergio Flores
 */
public class SCliReportMailer {
    
    private static final int ARG_ITEM_ID = 0;
    private static final int ARG_YEAR = 1;
    private static final int ARG_MAIL_TO = 2;

    private static final int DEF_ITEM_ID = 6; // aguacate maduro
    //private static final int DEF_ITEM_ID = 64;  // aguacate maduro orgánico
    //private static final int DEF_YEAR_REF = 5;  // comparativa de 5 hacia atrás, además del año/temporada actual
    private static final int DEF_YEAR_REF = 2010; // año/temporada tope hacia atrás
    private static final String DEF_MAIL_TO = "sflores@swaplicado.com.mx";

    /**
     * @param args the command line arguments
     * Three arguments expected:
     * 1: Item ID.
     * 2: Year reference. Can be two types of values: i.e., if >= 2001, then is the year to start from; otherwise is a number of history years besides current year.
     * 3: Mail recipients (separated with semicolon, without blanks between them, obviously).
     */
    public static void main(String[] args) {
        try {
            int itemId = DEF_ITEM_ID;
            int yearRef = DEF_YEAR_REF;
            String mailTo = DEF_MAIL_TO;
            
            if (args.length >= 1) {
                itemId = SLibUtils.parseInt(args[ARG_ITEM_ID]);
            }
            if (args.length >= 2) {
                yearRef = SLibUtils.parseInt(args[ARG_YEAR]);
            }
            if (args.length >= 3) {
                mailTo = args[ARG_MAIL_TO];
            }
            
            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            database.connect("192.168.1.233", "3306", "som_com", "root", "msroot");
            
            SGuiSession session = new SGuiSession(null);
            session.setDatabase(database);

            SReportHtmlTicketSeasonMonth reportHtmlTicketSeasonMonth = new SReportHtmlTicketSeasonMonth(session);
            String body = reportHtmlTicketSeasonMonth.generateReportHtml(itemId, yearRef);
            
            SDbItem item = new SDbItem();
            item.read(session, new int[] { itemId });
            SDbInputCategory inputCategory = new SDbInputCategory();
            inputCategory.read(session, new int[] { item.getFkInputCategoryId() });
            
            SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "AETHSOM", "som@aeth.mx");
            //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "sflores@swaplicado.com.mx", "Ch3c0m4n", "sflores@swaplicado.com.mx");
            ArrayList<String> recipients = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailTo, ";")));
            SMail mail = new SMail(sender, "[SOM] Histórico mensual " + inputCategory.getName().toLowerCase() + " " + SLibUtils.DateFormatDate.format(new Date()), body, recipients);

            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
            mail.send();
            System.out.println("Mail send!");
        }
        catch (Exception e) {
            SLibUtils.printException("main()", e);
        }
    }
}
