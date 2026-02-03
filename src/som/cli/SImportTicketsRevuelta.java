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
 *
 * @author Alfredo Pérez, Isabel Servín
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

    private static void run(int idTicketStart) throws Exception {
        Statement stmSoom = moSession.getStatement().getConnection().createStatement();
        Statement stmRev = moConnectionRev.createStatement();
        ResultSet rstSoom;

        deleteLog();
        
        rstSoom = stmSoom.executeQuery("SELECT MAX(num) FROM som_com.s_tic s WHERE dt >= '2019-01-01' AND NOT b_del");
        rstSoom.next();
        setLastImportedTicket(rstSoom.getString(1));
        String sql = "SELECT "
                + "Pes_ID, Pes_FecHorPri, Pes_ObsPri, Pes_PesoPri, Pes_Bruto, Pes_OpeNomPri, "
                + "Pes_FecHorSeg, Pes_UnidadPri, Pes_PesoSeg, Pes_ObsSeg, Pes_OpeNomSeg, "
                + "Pes_Tara, Pes_Neto, Pes_Placas, Pes_Chofer, "
                + "Pro_ID, Emp_ID, NOW() AS now "
                + "FROM dba.Pesadas "
                + "WHERE Pes_ID >= " + idTicketStart + " "
                //+ "where pes_id = 149844 "
                + "AND Usb_ID = 'ACTH' ORDER BY Pes_ID";

        ResultSet rstRev = stmRev.executeQuery(sql);

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
            rstSoom = stmSoom.executeQuery("SELECT num FROM som_com.s_tic s WHERE num = '" + id + "'");
            now = SLibUtils.DateFormatDatetime.format(rstRev.getDate("now"));
            if (!rstSoom.next()) {
                idItem = SSomUtils.mapItemSomRevuelta(moSession, rstRev.getString("Pro_ID"));
                idProducer = SSomUtils.mapProducerSomRevuelta(moSession, rstRev.getString("Emp_ID"));
                
                if (idItem != 0 && idProducer != 0) {
                    SDbItem dbItem = new SDbItem();
                    dbItem.read(moSession, new int[] { idItem });
                    
                    SDbProducer dbProducer = new SDbProducer();
                    dbProducer.read(moSession, new int[] { idProducer });
                    
                    SDbTicket registry = new SDbTicket();
                    //registry.setPkTicketId(rstSoom.getInt("newId"));
                    registry.setNumber(rstRev.getString("Pes_ID"));
                    registry.setDate(rstRev.getDate("Pes_FecHorPri"));
                    //registry.setQuantity(Quantity());
                    registry.setPlate(rstRev.getString("Pes_Placas"));
                    registry.setPlateCage(getContainerPlates(msPlateCageLabels, rstRev.getString("Pes_ObsPri")));
                    registry.setDriver(rstRev.getString("Pes_Chofer"));
                    registry.setDatetimeArrival(rstRev.getTimestamp("Pes_FecHorPri"));
                    registry.setDatetimeDeparture(rstRev.getTimestamp("Pes_FecHorSeg") == null ? rstRev.getTimestamp("Pes_FecHorPri") : rstRev.getTimestamp("Pes_FecHorSeg"));
                    //registry.setPackingFullQuantityArrival(PackingFullQuantityArrival());
                    //registry.setPackingFullQuantityDeparture(PackingFullQuantityDeparture());
                    //registry.setPackingEmptyQuantityArrival(PackingEmptyQuantityArrival());
                    //registry.setPackingEmptyQuantityDeparture(PackingEmptyQuantityDeparture());
                    //registry.setPackingWeightArrival(PackingWeightArrival());
                    //registry.setPackingWeightDeparture(PackingWeightDeparture());
                    //registry.setPackingWeightNet_r(PackingWeightNet_r());
                    //registry.setWeightSource(WeightSource());
                    registry.setWeightDestinyArrival(rstRev.getDouble("Pes_PesoPri"));
                    registry.setWeightDestinyDeparture(rstRev.getString("Pes_PesoSeg") == null ? 0 : rstRev.getDouble("Pes_PesoSeg"));
                    registry.setWeightDestinyGross_r(rstRev.getInt("Pes_PesoPri"));
                    registry.setQuantity(id);
                    //registry.setWeightDestinyNet_r(WeightDestinyNet_r());
                    registry.setScaleOperatorArrival(rstRev.getString("Pes_OpeNomPri"));
                    registry.setScaleOperatorDeparture(rstRev.getString("Pes_OpeNomSeg"));
                    registry.setScaleCommentsArrival(rstRev.getString("Pes_ObsPri"));
                    registry.setScaleCommentsDeparture(rstRev.getString("Pes_ObsSeg"));
                    //registry.setSystemPenaltyPercentage(SystemPenaltyPercentage());
                    //registry.setSystemWeightPayment(SystemWeightPayment());
                    //registry.setSystemPricePerTon(SystemPricePerTon());
                    //registry.setSystemPayment_r(SystemPayment_r());
                    //registry.setSystemFreight(SystemFreight());
                    //registry.setSystemTotal_r(SystemTotal_r());
                    //registry.setUserPenaltyPercentage(UserPenaltyPercentage());
                    //registry.setUserWeightPayment(UserWeightPayment());
                    //registry.setUserPricePerTon(UserPricePerTon());
                    //registry.setUserPayment_r(UserPayment_r());
                    //registry.setUserFreight(UserFreight());
                    //registry.setUserTotal_r(UserTotal_r());
                    //registry.setDpsSupplyDate_n(DpsSupplyDate_n()); 
                    registry.setRevueltaImport1(true);
                    //registry.setRevueltaImport2(this.isRevueltaImport2());
                    //registry.setWeightSourceAvailable(this.isWeightSourceAvailable());
                    //registry.setMfgOutsourcing(this.isMfgOutsourcing());
                    //registry.setTared(this.isTared());
                    //registry.setPayed(this.isPayed());
                    //registry.setAssorted(this.isAssorted());
                    registry.setPacking(dbItem.isPacking());
                    registry.setLaboratory(dbItem.isLaboratory());
                    //registry.setDpsSupply(this.isDpsSupply());
                    //registry.setDeleted(this.isDeleted());
                    //registry.setSystem(this.isSystem());
                    registry.setFkScaleId(1); // Revuelta XXX
                    registry.setFkTicketStatusId(SModSysConsts.SS_TIC_ST_SCA);
                    registry.setFkProducerId(idProducer);
                    registry.setFkItemId(idItem);
                    seasonId = getProperSeasonId(moSession, registry.getDate(), registry.getFkItemId(), registry.getFkProducerId());
                    registry.setFkSeasonId_n(seasonId);
                    registry.setFkRegionId_n(getProperRegionId(moSession, seasonId, registry.getFkItemId(), registry.getFkProducerId()));
                    registry.setFkUnitId(dbItem.getFkUnitId());
                    
                    HashMap<Integer, String> originsMap = SSomUtils.createOriginsMap(moSession, idItem);
                    registry.setFkInputSourceId((originsMap.containsKey(dbProducer.getFkInputSourceId()) ? dbProducer.getFkInputSourceId() : SModSysConsts.SU_INP_SRC_NA));
                    registry.setFkLaboratoryId_n(0);
                    registry.setFkTicketOriginId(SModSysConsts.SU_TIC_ORIG_NA);
                    registry.setFkTicketDestinationId(SModSysConsts.SU_TIC_DEST_NA);
                    //registry.setFkExternalDpsYearId_n(...);
                    //registry.setFkExternalDpsDocId_n(...);
                    //registry.setFkExternalDpsEntryId_n(..);
                    //registry.setFkUserInsertId(...);
                    //registry.setFkUserUpdateId(...);
                    //registry.setFkUserTaredId(...);
                    //registry.setFkUserPayedId(...);
                    //registry.setFkUserAssortedId(...);
                    //registry.setTsUserInsert(...);
                    //registry.setTsUserUpdate(...);
                    //registry.setTsUserTared(...);
                    //registry.setTsUserPayed(...);
                    //registry.setTsUserAssorted(...);
                    //registry.setXtaScaleName(...);
                    //registry.setXtaScaleCode(...);
                    //registry.setXtaSeason(...);
                    //registry.setXtaRegion(...);
                    //registry.setXtaItem(...);
                    //registry.setXtaProducer(...);
                    //registry.setXtaProviderFiscalId(...);
                    //registry.setAuxFormerTared(...);
                    //registry.setAuxFormerPayed(...);
                    //registry.setAuxFormerAssorted(...);
                    //registry.setAuxMoveNextOnSend(...);
                    //registry.setAuxRequiredCalculation(...);
                    //registry.setAuxItemSendMail(...);
                    //registry.setAuxItemRecipientsTo(...);
                    //registry.setAuxProducerSendMail(...);
                    //registry.setAuxProducerRecipientsTo(...);
                    SDbTicketNote note = new SDbTicketNote();
                    note.setNote(rstRev.getString("Pes_ObsPri"));
                    registry.getChildTicketNotes().add(note);
                    registry.save(moSession);
                    
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
