/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author Edwin Carmona
 */
public class SEmbeddedImageEmailUtil {

    /**
     * Sends an HTML e-mail with inline images.
     *
     * @param host SMTP host
     * @param port SMTP port
     * @param userName e-mail address of the sender's account
     * @param password password of the sender's account
     * @param toRecsAddress e-mail address of the recipient
     * @param toCcsAddress
     * @param subject e-mail subject
     * @param htmlBody e-mail content with HTML tags
     * @param mapInlineImages key: Content-ID value: path of the image file
     * @param filePaths
     * 
     * @throws AddressException
     * @throws MessagingException
     * @throws java.io.IOException
     */
    public static void send(String host, String port,
            final String userName, final String password, 
            ArrayList<String> toRecsAddress,
            ArrayList<String> toCcsAddress, 
            String subject, String htmlBody,
            Map<String, String> mapInlineImages,
            ArrayList<String> filePaths)
            throws AddressException, MessagingException, IOException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = new InternetAddress[toRecsAddress.size()];
        for (int i = 0; i < toRecsAddress.size(); i++) {
            toAddresses[i] = new InternetAddress(toRecsAddress.get(i));
        }
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        
        if (! toCcsAddress.isEmpty()) {
            InternetAddress[] toCcsAddresses = new InternetAddress[toCcsAddress.size()];
            for (int i = 0; i < toCcsAddress.size(); i++) {
                toCcsAddresses[i] = new InternetAddress(toCcsAddress.get(i));
            }
            
            msg.setRecipients(Message.RecipientType.CC, toCcsAddresses);
        }
        
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlBody, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        
        MimeBodyPart attachmentPart;
        for (String filePath : filePaths) {
            attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(filePath));
            
            multipart.addBodyPart(attachmentPart);
        }

        // adds inline image attachments
        if (mapInlineImages != null && mapInlineImages.size() > 0) {
            Set<String> setImageID = mapInlineImages.keySet();

            for (String contentId : setImageID) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);

                String imageFilePath = mapInlineImages.get(contentId);
                try {
                    imagePart.attachFile(imageFilePath);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(imagePart);
            }
        }

        msg.setContent(multipart);
        Transport.send(msg);
    }
}
