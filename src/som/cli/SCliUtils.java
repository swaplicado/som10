/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.TimeZone;
import javax.mail.MessagingException;
import sa.gui.util.SUtilConfigXml;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SCliUtils {
    
    /**
     * Connect to SOM database and create a new GUI session.
     * @return 
     * @throws java.lang.Exception 
     */
    public static SGuiSession createSession() throws Exception {
        // connect to database:

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

        // create new GUI session:

        SGuiSession session = new SGuiSession(null);
        session.setDatabase(database);
        
        return session;
    }
    
    /**
     * Get row index limit for accumulated values for given current month and fisrt season month.
     * @param currentMonth Month.
     * @param seasonFirstMonth Season fisrt month.
     * @return Row index limit.
     */
    public static int getRowAccumsForMonth(final int currentMonth, final int seasonFirstMonth) {
        if (currentMonth >= seasonFirstMonth) {
            return currentMonth - seasonFirstMonth;
        }
        else {
            return currentMonth + SLibTimeConsts.MONTHS - seasonFirstMonth; 
        }
    }
    
    /**
     * Send mail report.
     * @param subject Mail subject.
     * @param body Mail body.
     * @param toRecepients Mail TO recepients.
     * @param bccRecepients Mail BCC recipients.
     * @param attachment Mail flle attachment.
     * @throws MessagingException 
     */
    public static void sendMailReport(final String subject, final String body, final ArrayList<String> toRecepients, final ArrayList<String> bccRecepients, final File attachment) throws MessagingException {
        SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "Aeth2021*s.", "som@aeth.mx");

        SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(subject), body, toRecepients);
        
        if (attachment != null) {
            mail.getAttachments().add(attachment);
        }
        
        mail.getBccRecipients().addAll(bccRecepients);
        mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
        mail.send();

        System.out.println("¡El correo ha sido enviado!");
    }
}
