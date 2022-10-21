/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import sa.gui.util.SUtilConfigXml;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;
import sa.lib.xml.SXmlUtils;
import som.gui.SGuiClientSessionCustom;
import som.mod.cfg.db.SDbCompany;

/**
 *
 * @author Isabel Servín
 */
public class SCliReportMailerExistences {
    
    private static SGuiSession moSession;
    
    public static final int DAYS_TO_SEND_MAIL = 0;
    
    private static final int ARG_DAYS_TO_SEND_MAIL = 0;
    private static final int ARG_MAIL_TO = 1;
    private static final int ARG_MAIL_BCC = 2;
    
    public static void main(String[] args) {
        try {
            
            int daysToSendMail = DAYS_TO_SEND_MAIL;
            String mailTo = "isabel.garcia@swaplicado.com.mx";
            String mailBcc = "";
            
            if (args.length >= 1) {
                daysToSendMail = Integer.parseInt(args[ARG_DAYS_TO_SEND_MAIL]);
            }
            if (args.length >= 2) {
                mailTo = args[ARG_MAIL_TO];
            }
            if (args.length >= 3) {
                mailBcc = args[ARG_MAIL_BCC];
            }

            moSession = new SGuiSession(null);
            
            String xml;
            xml = SXmlUtils.readXml(SUtilConsts.FILE_NAME_CFG);
            SUtilConfigXml configXml = new SUtilConfigXml();
            configXml.processXml(xml);
            
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
            else {
                System.out.println("Conexión a la bd establecida");
            }
            
            moSession.setDatabase(database);
            run(daysToSendMail, mailTo, mailBcc);
        }
        catch (Exception e) {
            SLibUtils.printException(SCliReportMailerExistences.class.getName(), e);
        }
    }
    
    public static void run(int daysToSendMail, String mailTo, String mailBcc) throws Exception {
        SGuiClientSessionCustom csc = new SGuiClientSessionCustom(moSession.getClient(), 1);
        SDbCompany company = new SDbCompany();
        company.read(moSession, new int[] {1});
        
        csc.setCompany(company);
        moSession.setSessionCustom(csc);
        
        Date date = null;
        ResultSet resultSet = moSession.getStatement().executeQuery("SELECT CURDATE()");
        if (resultSet.next()) {
            date = resultSet.getDate(1);
        }
        
        SReportHtmlExistences htmlExistences = new SReportHtmlExistences();
        
        String mailBody = htmlExistences.generateReportHtml(moSession, date);

        // generate mail subject:

        String mailSubject = "[SOM] Existencias al " + SLibUtils.DateFormatDate.format(date);

        // prepare mail recepients:

        ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailTo, ";")));
        ArrayList<String> recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailBcc, ";")));

        // send mail:

        SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "Aeth2021*s.", "som@aeth.mx");
        //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "sflores@swaplicado.com.mx", "Ch3c0m4n", "sflores@swaplicado.com.mx");

        SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(mailSubject), mailBody, recipientsTo);
//        mail.getBccRecipients().addAll(recipientsBcc);
        mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
        mail.send();

        System.out.println("Mail send!");
    }
}
