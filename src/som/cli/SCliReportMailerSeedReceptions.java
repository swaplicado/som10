/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.sql.ResultSet;
import java.util.Date;
import sa.gui.util.SUtilConfigXml;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import sa.lib.xml.SXmlUtils;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbCompany;
import som.mod.som.db.SSomMailUtils;

/**
 *
 * @author Isabel Servín
 */
public class SCliReportMailerSeedReceptions {
    
    private static SGuiSession moSession;
    
    //public static final int DAYS_TO_SEND_MAIL = 0;
    
    //private static final int ARG_DAYS_TO_SEND_MAIL = 0;
    private static final int ARG_MAIL_TO = 0;
    private static final int ARG_MAIL_BCC = 1;
    
    public static void main(String[] args) {
        try {
            
            //int daysToSendMail = DAYS_TO_SEND_MAIL;
            String mailTo = "isabel.garcia@swaplicado.com.mx;sflores@swaplicado.com.mx";
            String mailBcc = "";
            
//            if (args.length >= 1) {
//                daysToSendMail = Integer.parseInt(args[ARG_DAYS_TO_SEND_MAIL]);
//            }
            if (args.length >= 1) {
                mailTo = args[ARG_MAIL_TO];
            }
            if (args.length >= 2) {
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
            //run(daysToSendMail, mailTo, mailBcc);
            run(mailTo, mailBcc);
        }
        catch (Exception e) {
            SLibUtils.printException(SCliReportMailerSeedReceptions.class.getName(), e);
        }
    }
    
    public static void run(String mailTo, String mailBcc) throws Exception {
        SGuiClientSessionCustom csc = new SGuiClientSessionCustom(moSession.getClient(), 1);
        SDbCompany company = new SDbCompany();
        company.read(moSession, new int[] {1});
        
        csc.setCompany(company);
        moSession.setSessionCustom(csc);
        
        Date date = null;
        
        boolean ordinaryPeriod = false;
        String receptionMail = "0";
        String noReceptionMail = "0";
        int noReceptionIn = 0;
        int noReceptionOut = 0;
        
        String sql = "SELECT CURDATE(), "
                + "IF(MONTH(CURDATE()) BETWEEN rec_per_month_sta AND rec_per_month_end, TRUE, FALSE) AS b_ord, "
                + "smn_rec, smn_no_rec, smn_no_rec_int_in, smn_no_rec_int_out "
                + "FROM su_seas WHERE CURDATE() BETWEEN dt_sta AND dt_end;";
        
        ResultSet resultSet = moSession.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            date = resultSet.getDate(1);
            ordinaryPeriod = resultSet.getBoolean(2);
            receptionMail = resultSet.getString(3);
            noReceptionMail = resultSet.getString(4);
            noReceptionIn = resultSet.getInt(5);
            noReceptionOut = resultSet.getInt(6);
        }
        
        if (receptionMail.equals("1")) {
            int daysToSendMail = ordinaryPeriod ? noReceptionIn : noReceptionOut;
            SSomMailUtils.computeMailReceptions(moSession, date, date, SModSysConsts.SU_TIC_ORIG_PRV, 0, true, ordinaryPeriod, noReceptionMail, daysToSendMail, mailTo, mailBcc);
        }
        else {
            int maxId = 0;
            sql = "SELECT MAX(id) FROM s_cli_umn_log";
            try (ResultSet id = moSession.getStatement().executeQuery(sql)) {
                if (id.next()) {
                    maxId = id.getInt(1);
                }
            }
            
            sql = "INSERT INTO s_cli_umn_log (id, b_data, b_sent, ts_exe) VALUES (" + (maxId + 1) + ", false, false, NOW());";
            moSession.getStatement().execute(sql);
        }
    }
}
