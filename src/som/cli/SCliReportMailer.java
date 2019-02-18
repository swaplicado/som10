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

/**
 *
 * @author Sergio Flores
 */
public class SCliReportMailer {
    
    private static final int ARG_ITEM_ID = 0;
    private static final int ARG_YEARS = 1;
    private static final int ARG_MAIL_TO = 2;

    private static final int DEF_ITEM_ID = 6;
    private static final int DEF_YEARS = 5;
    private static final String DEF_MAIL_TO = "sflores@swaplicado.com.mx";

    /**
     * @param args the command line arguments
     * Three arguments are expected:
     * 1: item ID
     * 2: history years
     * 3: mail recipients (separated with semicolon, without blanks, obviously)
     */
    public static void main(String[] args) {
        try {
            int itemId = DEF_ITEM_ID;
            int years = DEF_YEARS;
            String mailTo = DEF_MAIL_TO;
            
            if (args.length >= 1) {
                itemId = SLibUtils.parseInt(args[ARG_ITEM_ID]);
            }
            if (args.length >= 2) {
                years = SLibUtils.parseInt(args[ARG_YEARS]);
            }
            if (args.length >= 3) {
                mailTo = args[ARG_MAIL_TO];
            }
            
            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            database.connect("192.168.1.233", "3306", "som_com", "root", "msroot");
            
            SGuiSession session = new SGuiSession(null);
            session.setDatabase(database);

            SReportHtmlTicketSeasonMonth reportHtmlTicketSeasonMonth = new SReportHtmlTicketSeasonMonth(session);
            String body = reportHtmlTicketSeasonMonth.generateReportHtml(itemId, years);
            
            SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "AETHSOM", "som@aeth.mx");
            //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "sflores@swaplicado.com.mx", "Ch3c0m4n", "sflores@swaplicado.com.mx");
            ArrayList<String> recipients = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailTo, ";")));
            SMail mail = new SMail(sender, "[SOM] Comparativo histórico mensual " + SLibUtils.DateFormatDate.format(new Date()), body, recipients);

            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
            mail.send();
            System.out.println("Mail send!");
        }
        catch (Exception e) {
            SLibUtils.printException("main()", e);
        }
    }
}
