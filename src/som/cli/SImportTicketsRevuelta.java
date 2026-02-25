/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sa.gui.util.SUtilConfigXml;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import sa.lib.xml.SXmlUtils;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbCompany;
import som.mod.cfg.db.SDbUser;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbProducer;
import som.mod.som.db.SDbTicket;
import som.mod.som.db.SDbTicketNote;
import som.mod.som.db.SSomUtils;
import static som.mod.som.db.SSomUtils.getProperRegionId;
import static som.mod.som.db.SSomUtils.getProperSeasonId;

/**
 * Import scale tickets from Revuelta's database (Sybase).
 * @author Alfredo Pérez, Isabel Servín, Sergio Flores
 * TODO 2019-09-20 Sergio Flores: ¡Refactorizar esta clase y convertirla al paradigma OO!
 */
public class SImportTicketsRevuelta {
    
    public static String getLastImportedTicket() { return msLastImportedTicket; }
    public static void setLastImportedTicket(String lastId) { msLastImportedTicket = lastId; }
    
    private static Connection moConnectionRev;
    private static String msLastImportedTicket;
    private static SGuiSession moSession;
    private static String msPlateCageLabels;

    private static final int ARG_IDX_TICKET_START = 0;

    public static final int DEF_TICKET_START = 190000;
    
    public static void main(String[] args) {
        try {
            int idTicketStart = DEF_TICKET_START;
            
            if (args.length >= 1) {
                idTicketStart = SLibUtils.parseInt(args[ARG_IDX_TICKET_START]);
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
            
            moSession.setDatabase(database);
            
            moConnectionRev = openConnectionRevuelta();
            try {
                SDbCompany company = new SDbCompany();
                company.read(moSession, new int[] { SUtilConsts.BPR_CO_ID });
                msPlateCageLabels = company.getPlateCageLabels();
                
                run(idTicketStart);
                database.disconnect();
                moConnectionRev.close();
            }
            catch (Exception e) {
                SLibUtils.printException(SImportTicketsRevuelta.class.getName(), e);
            }
        }
        catch (Exception ex) {
            Logger.getLogger(SImportTicketsRevuelta.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    private static void run(final int idTicketStart) throws Exception {
        Statement stSom = moSession.getStatement().getConnection().createStatement();
        Statement stRevuelta = moConnectionRev.createStatement();
        ResultSet rsSom;

        deleteLog();
        
        rsSom = stSom.executeQuery("SELECT MAX(num) FROM som_com.s_tic s WHERE dt >= '2019-01-01' AND NOT b_del");
        rsSom.next();
        setLastImportedTicket(rsSom.getString(1));
        String sql = "SELECT "
                + "Pes_ID, Pes_FecHorPri, Pes_ObsPri, Pes_PesoPri, Pes_Bruto, Pes_OpeNomPri, "
                + "Pes_FecHorSeg, Pes_UnidadPri, Pes_PesoSeg, Pes_ObsSeg, Pes_OpeNomSeg, "
                + "Pes_Tara, Pes_Neto, Pes_Placas, Pes_Chofer, "
                + "Pro_ID, Emp_ID, NOW() AS now "
                + "FROM dba.Pesadas "
                + "WHERE Pes_ID >= " + idTicketStart + " "
                //+ "where pes_id = 149844 "
                + "AND Usb_ID = 'ACTH' ORDER BY Pes_ID";

        ResultSet rstRev = stRevuelta.executeQuery(sql);

        int id;
        int seasonId;
        int idItem;
        int idProducer;
        String now;

        SDbUser user = new SDbUser();
        user.read(moSession, new int[] { SUtilConsts.USR_NA_ID });
        moSession.setUser(user);

        while (rstRev.next()) {
            id = rstRev.getInt("Pes_ID");
            rsSom = stSom.executeQuery("SELECT num FROM som_com.s_tic s WHERE num = '" + id + "'");
            now = SLibUtils.DateFormatDatetime.format(rstRev.getDate("now"));
            if (!rsSom.next()) {
                idItem = SSomUtils.mapItemSomRevuelta(moSession, rstRev.getString("Pro_ID"));
                idProducer = SSomUtils.mapProducerSomRevuelta(moSession, rstRev.getString("Emp_ID"));
                
                if (idItem != 0 && idProducer != 0) {
                    SDbItem item = new SDbItem();
                    item.read(moSession, new int[] { idItem });
                    
                    SDbProducer producer = new SDbProducer();
                    producer.read(moSession, new int[] { idProducer });
                    
                    String comments;
                    String operator;
                    
                    SDbTicket ticket = new SDbTicket();
                    //ticket.setPkTicketId(rstSoom.getInt(...));
                    ticket.setNumber(rstRev.getString("Pes_ID"));
                    ticket.setDate(rstRev.getDate("Pes_FecHorPri"));
                    //ticket.setQuantity(...);
                    ticket.setPlate(rstRev.getString("Pes_Placas"));
                    comments = getContainerPlates(msPlateCageLabels, rstRev.getString("Pes_ObsPri"));
                    ticket.setPlateCage(comments == null || comments.equals("null") ? "" : comments);
                    ticket.setDriver(rstRev.getString("Pes_Chofer"));
                    ticket.setDatetimeArrival(rstRev.getTimestamp("Pes_FecHorPri"));
                    ticket.setDatetimeDeparture(rstRev.getTimestamp("Pes_FecHorSeg") == null ? rstRev.getTimestamp("Pes_FecHorPri") : rstRev.getTimestamp("Pes_FecHorSeg"));
                    //ticket.setPackingFullQuantityArrival(...);
                    //ticket.setPackingFullQuantityDeparture(...);
                    //ticket.setPackingEmptyQuantityArrival(...);
                    //ticket.setPackingEmptyQuantityDeparture(...);
                    //ticket.setPackingWeightArrival(...);
                    //ticket.setPackingWeightDeparture(...);
                    //ticket.setPackingWeightNet_r(...);
                    //ticket.setWeightSource(...);
                    ticket.setWeightDestinyArrival(rstRev.getDouble("Pes_PesoPri"));
                    ticket.setWeightDestinyDeparture(rstRev.getString("Pes_PesoSeg") == null ? 0 : rstRev.getDouble("Pes_PesoSeg"));
                    ticket.setWeightDestinyGross_r(rstRev.getInt("Pes_PesoPri"));
                    //ticket.setWeightDestinyNet_r(...);
                    operator = rstRev.getString("Pes_OpeNomPri");
                    ticket.setScaleOperatorArrival(operator == null || operator.equals("") ? "" : operator);
                    operator = rstRev.getString("Pes_OpeNomSeg");
                    ticket.setScaleOperatorDeparture(operator == null || operator.equals("") ? "" : operator);
                    comments = rstRev.getString("Pes_ObsPri");
                    ticket.setScaleCommentsArrival(comments == null || comments.equals("null") ? "" : comments);
                    comments = rstRev.getString("Pes_ObsSeg");
                    ticket.setScaleCommentsDeparture(comments == null || comments.equals("null") ? "" : comments);
                    //ticket.setSystemPenaltyPercentage(...);
                    //ticket.setSystemWeightPayment(...);
                    //ticket.setSystemPricePerTon(...);
                    //ticket.setSystemPayment_r(...);
                    //ticket.setSystemFreight(...);
                    //ticket.setSystemTotal_r(...);
                    //ticket.setUserPenaltyPercentage(...);
                    //ticket.setUserWeightPayment(...);
                    //ticket.setUserPricePerTon(...);
                    //ticket.setUserPayment_r(...);
                    //ticket.setUserFreight(...);
                    //ticket.setUserTotal_r(...);
                    //ticket.setDpsSupplyDate_n(...); 
                    ticket.setRequiredFreight(SSomUtils.getRequiredFreightForItem(moSession, idItem));
                    ticket.setFreightTicketType("");
                    ticket.setRevueltaImport1(true);
                    //ticket.setRevueltaImport2(...);
                    //ticket.setWeightSourceAvailable(...);
                    //ticket.setMfgOutsourcing(...);
                    //ticket.setTared(...);
                    //ticket.setPayed(...);
                    //ticket.setAssorted(...);
                    ticket.setPacking(item.isPacking());
                    ticket.setLaboratory(item.isLaboratory());
                    //ticket.setDpsSupply(...);
                    //ticket.setDeleted(...);
                    //ticket.setSystem(...);
                    
                    ticket.setFkScaleId(SModSysConsts.SU_SCA_REV);
                    ticket.setFkTicketStatusId(SModSysConsts.SS_TIC_ST_SCA);
                    
                    seasonId = getProperSeasonId(moSession, ticket.getDate(), ticket.getFkItemId(), ticket.getFkProducerId());
                    ticket.setFkSeasonId_n(seasonId);
                    ticket.setFkRegionId_n(getProperRegionId(moSession, seasonId, ticket.getFkItemId(), ticket.getFkProducerId()));
                    
                    ticket.setFkItemId(idItem);
                    ticket.setFkUnitId(item.getFkUnitId());
                    ticket.setFkProducerId(idProducer);
                    
                    HashMap<Integer, String> originsMap = SSomUtils.createOriginsMap(moSession, idItem);
                    ticket.setFkInputSourceId((originsMap.containsKey(producer.getFkInputSourceId()) ? producer.getFkInputSourceId() : SModSysConsts.SU_INP_SRC_NA));
                    
                    //ticket.setFkLaboratoryId_n(...);
                    //ticket.setFkWarehouseUnloadCompanyId_n(...);
                    //ticket.setFkWarehouseUnloadBranchId_n(...);
                    //ticket.setFkWarehouseUnloadWarehouseId_n(...);
                    ticket.setFkTicketOriginId(SModSysConsts.SU_TIC_ORIG_NA);
                    ticket.setFkTicketDestinationId(SModSysConsts.SU_TIC_DEST_NA);
                    ticket.setFkExwFacilityOriginId(SModSysConsts.MU_EXW_FAC_NA);
                    ticket.setFkExwFacilityDestinationId(SModSysConsts.MU_EXW_FAC_NA);
                    //ticket.setFkExternalDpsYearId_n(...);
                    //ticket.setFkExternalDpsDocId_n(...);
                    //ticket.setFkExternalDpsEntryId_n(..);
                    //ticket.setFkFreightOriginId_n(...);
                    //ticket.setFkFreightTicketId_n(...);
                    //ticket.setPkOpCalendarId_n(...);
                    //ticket.setPkOpCalendarYearId_n(...);
                    //ticket.setPkOpCalendarMonthId_n(...);
                    
                    //ticket.setFkUserInsertId(...);
                    //ticket.setFkUserUpdateId(...);
                    //ticket.setFkUserTaredId(...);
                    //ticket.setFkUserPayedId(...);
                    //ticket.setFkUserAssortedId(...);
                    //ticket.setTsUserInsert(...);
                    //ticket.setTsUserUpdate(...);
                    //ticket.setTsUserTared(...);
                    //ticket.setTsUserPayed(...);
                    //ticket.setTsUserAssorted(...);
                    
                    SDbTicketNote note = new SDbTicketNote();
                    note.setNote(rstRev.getString("Pes_ObsPri"));
                    ticket.getChildTicketNotes().add(note);
                    ticket.save(moSession);
                    
                    String entry = "Boleto no. " + rstRev.getInt("Pes_ID") + " importado!";
                    writeLog(entry);
                    //System.out.println(entry);
                }
                else {
                    String entry = "Boleto no. " + rstRev.getInt("Pes_ID") + " NO fue importado! Producto: " + rstRev.getString("Pro_ID") + " " + now + ".";
                    writeLog(entry);
                    //System.err.println(entry);
                }
            }
        }
    }

    /**
     * Con el fin de no hacer el archivo excesivamente grande, el archivo *.log se elimina de la carpera raiz de SOM.
     * @throws Exception
     */
    public static void deleteLog() throws Exception {
        File log = new File("SImportTicketsRevuelta.log");
        log.delete();
    }
    
    /**
     * Guarda la entrada en el archivo de bitacora *.log dentro de la carpeta raiz de SOM.
     * @param entry Entrada a guardar en la bitacora.
     * @throws Exception
     */
    public static void writeLog(String entry) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SImportTicketsRevuelta.log", true))) {
            writer.append(System.getProperty("line.separator"));
            writer.append(entry);
            writer.close();
        }
    }

    /**
     * Establece conexion con la base de datos (Sybase) de Revuelta.
     * @return 
     */
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
    
    /**
     * Obtiene el texto que contiene el número de placas del contenedor.
     * 
     * @param targetWords Palabras objetivo a buscar separadas por ; (punto y coma).
     * @param textToScan Texto a escanear para buscar las palabras objetivo.
     * @return Regresa la siguiente palabra encontrada después de la primer palabra objetivo encontrada.
     * Ejemplo: 
     * targetWords: "jaula;tolva;tq"; 
     * textToScan: "PESO TEÓRICO: 37990 KG JAULA: 293XS3 pedido 0068049 carga completa"; 
     * valor de retorno: "293XS3";
     */
    private static String getContainerPlates(String targetWords, String textToScan) {
        if (textToScan == null) {
            return "";
        }
        
        for (String targetWord : targetWords.split(";")) {
            String plates = "";
            Pattern pattern = Pattern.compile(targetWord + "([\\s:]*[\\w]*)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(textToScan);

            if (matcher.find()) {
                plates = matcher.group(1).replaceAll("[\\s:]", "");
            }
            if (plates != null && !plates.equals("")) {
                return plates;
            }
        }
        
        return "";
    }
}
