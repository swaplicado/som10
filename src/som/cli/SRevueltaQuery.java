/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.gui.util.SUtilConfigXml;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Isabel Servín
 */
public class SRevueltaQuery {
    
    private static SGuiSession moSession;
    private static Connection moConnectionRev;
    
    public static void main(String[] args) {
        try {
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
            
            moSession.setDatabase(database);
            moConnectionRev = openConnectionRevuelta();
            
            run();
            
            database.disconnect();
            moConnectionRev.close();
        }
        catch (Exception ex) {
            Logger.getLogger(SRevueltaQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    private static Connection openConnectionRevuelta() {
        ResultSet resultSet;
        String revHost = "";
        String revPort = "";
        try {
            Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance();
            resultSet = moSession.getStatement().executeQuery("SELECT rev_host, rev_port FROM cu_co;");
            if (resultSet.next()) {
                revHost = resultSet.getString(1);
                revPort = resultSet.getString(2);
            }
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            SLibUtils.printException(SImportTicketsRevuelta.class.getName(), e);
        }
        
        Connection connection = null;
        String url = "jdbc:sybase:Tds:" + revHost + ":" + revPort + "/Revuelta"; // XXX WTF!
        Properties properties = new Properties();
        properties.put("user", "usuario"); // XXX WTF!
        properties.put("password", "revuelta"); // XXX WTF!
        
        try {
            connection = DriverManager.getConnection(url, properties);
        }
        catch (Exception e) {
            SLibUtils.printException(SImportTicketsRevuelta.class.getName(), e);
        }
        
        return connection;
    }
    
    private static void run() throws Exception {
        String sql = "select * from dba.pesadas "
                + "where pes_fechorpri >= '2021-04-30' AND Usb_ID = 'ACTH' "
                + "and pes_id = 149468"
                + "order by pes_fechor;"; //pes_id, pro_id, pro_nombre, pes_fechorpri
        
        int count = 0;
        try (ResultSet resultSet = moConnectionRev.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                count ++;
                System.out.println(count + " ↓");
                ResultSetMetaData metaData = resultSet.getMetaData();
                for(int i = 0; i < metaData.getColumnCount(); i++) {
                    System.out.println(metaData.getColumnName(i + 1) + ": " + resultSet.getString(i + 1));
                }
                System.out.println(count + " ↑\n");
            }
        }
        System.out.println("Resultados totales: " + count);
        System.out.println("Fin del resultSet.");
    }
}
