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
import java.util.HashMap;
import java.util.TimeZone;
import sa.gui.util.SUtilConfigXml;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;
import sa.lib.xml.SXmlUtils;
import som.mod.SModSysConsts;

/**
 * Report mailer for monthly reception at scale.
 * @author Sergio Flores, Isabel Servín
 */
public class SCliReportMailerSummaryAlternative {
    
    public static final int ID_AVO_FRUIT = 6; // fruta
    public static final int ID_AVO_FRUIT_ORG = 64; // fruta orgánica
//    public static final int ID_AVO_MARC = 23; // bagazo
//    public static final int ID_AVO_KERNEL = 100; // hueso y cáscara
    
    public static final HashMap<Integer, String> ItemDescriptions = new HashMap<>();
    
    static {
        ItemDescriptions.put(ID_AVO_FRUIT, "Fruta");
        ItemDescriptions.put(ID_AVO_FRUIT_ORG, "Fruta Orgánica");
//        ItemDescriptions.put(ID_AVO_MARC, "Bagazo");
//        ItemDescriptions.put(ID_AVO_KERNEL, "Hueso y Cáscara");
    }
    
    /** Argument index for list of ID of items. */
    private static final int ARG_IDX_ITEM_IDS = 0;
    /** Argument index for year reference. */
    private static final int ARG_IDX_DATE = 1;
    /** Argument index for list of mail-To recipients. */
    private static final int ARG_IDX_MAIL_TO = 2;
    /** Argument index for list of mail-Bcc recipients. */
    private static final int ARG_IDX_MAIL_BCC = 3;
    
    private static final String ARG_DATE_TODAY = "TODAY";
    private static final String ARG_DATE_YESTERDAY = "YESTERDAY";

    private static final String[] DEF_ITEM_IDS = new String[] { ID_AVO_FRUIT + "-" + ID_AVO_FRUIT_ORG };
    private static final Date DEF_DATE = SLibTimeUtils.createDate(2024, 3, 12);
    private static final String DEF_MAIL_TO = "isabel.garcia@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "gortiz@aeth.mx";
    //private static final String DEF_MAIL_BCC;

    /**
     * @param args the command line arguments
     * Four arguments expected:
     * 1: Lista de IDs de ítems a reportar con su id convencional y su id orgánico separado por guion, para reportar otro ítem debe ir separado por punto y coma. ej: 6-64;
     * 2: Date. Date for report.
     * 3: List of mail-To recipients (separated with semicolon, without blanks between them, obviously).
     * 4: List of mail-Bcc recipients (separated with semicolon, without blanks between them, obviously).
     */
    public static void main(String[] args) {
        try {
            // define arguments of program:
            
            String[] itemIds = DEF_ITEM_IDS;
            Date date = DEF_DATE;
            String mailTo = DEF_MAIL_TO;
            String mailBcc = DEF_MAIL_TO;
            
            if (args.length >= 1) {
                itemIds = args[ARG_IDX_ITEM_IDS].split(";");
            }
            if (args.length >= 2) {
                String dateArg = args[ARG_IDX_DATE];
                switch (dateArg) {
                    case ARG_DATE_TODAY:
                        date = SLibTimeUtils.convertToDateOnly(new Date());
                        break;
                    case ARG_DATE_YESTERDAY:
                        date = SLibTimeUtils.convertToDateOnly(SLibTimeUtils.addDate(new Date(), 0, 0, -1));
                        break;
                    default:
                        date = SLibUtils.DateFormatDate.parse(dateArg); // date format: yyyy-mm-dd
                }
            }
            if (args.length >= 3) {
                mailTo = args[ARG_IDX_MAIL_TO];
            }
            if (args.length >= 4) {
                mailBcc = args[ARG_IDX_MAIL_BCC];
            }
            
            // connect database:
            
            String xml = SXmlUtils.readXml(SUtilConsts.FILE_NAME_CFG);
            SUtilConfigXml configXml = new SUtilConfigXml();
            configXml.processXml(xml);

            TimeZone zone = SLibUtils.createTimeZone(TimeZone.getDefault(), TimeZone.getTimeZone((String) configXml.getAttribute(SUtilConfigXml.ATT_TIME_ZONE).getValue()));
            SLibUtils.restoreDateFormats(zone);
            TimeZone.setDefault(zone);

            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            int result = database.connect(
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_HOST).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_PORT).getValue(),
                    "som_com",
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_NAME).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_PSWD).getValue());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            
            // create user session:
            
            SGuiSession session = new SGuiSession(null);
            session.setDatabase(database);
            
            // generate mail body:

            SReportHtmlScaleSummaryAlternative htmlScaleSummary = new SReportHtmlScaleSummaryAlternative(session);
            String mailBody = htmlScaleSummary.generateReportHtml(itemIds, date, SModSysConsts.SU_TIC_ORIG_PRV, 0);
            
            // generate mail subject:
            
            String mailSubject = "[SOM ORG.] Resumen bascula " + SLibUtils.DateFormatDate.format(date);
            
            // prepare mail recepients:
            
            ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailTo, ";")));
            ArrayList<String> recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailBcc, ";")));
            
            // send mail:
            
            SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "Aeth2021*s.", "som@aeth.mx");
            //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "sflores@swaplicado.com.mx", "Ch3c0m4n", "sflores@swaplicado.com.mx");
            
            SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(mailSubject), mailBody, recipientsTo);
            mail.getBccRecipients().addAll(recipientsBcc);
            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
            mail.send();
            
            System.out.println("Mail send!");
        }
        catch (Exception e) {
            SLibUtils.printException("main()", e);
        }
    }
}
