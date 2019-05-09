/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbCompany;
import som.mod.cfg.db.SDbUser;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbTicket;
import som.mod.som.db.SDbTicketNote;
import som.mod.som.db.SSomUtils;
import static som.mod.som.db.SSomUtils.getProperRegionId;
import static som.mod.som.db.SSomUtils.getProperSeasonId;

/**
 *
 * @author Alfredo Pérez
 */
public class SImportTicketsRevuelta {

    private static Connection moConnectionRev;
    private static Connection moConnectionSom;
    private static int mnLastImportedTicket;
    private static SGuiSession session;
    private static String PlateCageLabels;

    public static int getLastImportedTicket() { return mnLastImportedTicket; }
    public static void setLastImportedTicket(int lastId) { mnLastImportedTicket = lastId; }
    
    public static void main(String[] args) {
        session = new SGuiSession(null);
        moConnectionRev = openConnectionRevuelta();
        moConnectionSom = openConnectionSom();

        SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
        database.connect("192.168.1.233", "3306", "som_com", "root", "msroot");
        session.setDatabase(database);
        try {
            SDbCompany company = (SDbCompany) session.readRegistry(SModConsts.CU_CO, new int[] { SUtilConsts.BPR_CO_ID });
            PlateCageLabels = company.getPlateCageLabels();
            
            run(session);
            database.disconnect();
            moConnectionRev.close();
            moConnectionSom.close();
        } 
        catch (Exception e) {
            SLibUtils.printException(SImportTicketsRevuelta.class.getName(), e);
        }
    }

    private static void run(SGuiSession session) throws Exception {
        Statement stmSoom = moConnectionSom.createStatement();
        Statement stmRev = moConnectionRev.createStatement();
        ResultSet rstSoom = null;

        rstSoom = stmSoom.executeQuery("SELECT MAX(num) FROM som_com.s_tic s WHERE dt >= '2019-01-01'");
        rstSoom.next();
        setLastImportedTicket(rstSoom.getInt(1));
        String sql = "SELECT "
                + "Pes_ID, Pes_FecHorPri, Pes_ObsPri, Pes_PesoPri, Pes_Bruto, Pes_FecHorSeg, Pes_UnidadPri, Pes_PesoSeg, Pes_Tara, Pes_Neto, Pes_Placas, Pes_Chofer, Pro_ID, Emp_ID "
                + "FROM dba.Pesadas "
                + "WHERE Pes_ID >= " + getLastImportedTicket() + " AND Usb_ID = 'ACTH' ORDER BY Pes_ID";

        ResultSet rstRev = stmRev.executeQuery(sql);

        int id;
        int seasonId;
        int idItem;
        int idProd;

        SDbUser user = new SDbUser();
        user.read(session, new int[] { SUtilConsts.USR_NA_ID });
        session.setUser(user);

        while (rstRev.next()) {
            id = rstRev.getInt("Pes_ID");
            rstSoom = stmSoom.executeQuery("SELECT num FROM som_com.s_tic s WHERE num = " + id);
            if (!rstSoom.next()) {
                idItem = SSomUtils.mapItemSomRevuelta(session, rstRev.getString("Pro_ID"));
                idProd = SSomUtils.mapProducerSomRevuelta(session, rstRev.getString("Emp_ID"));
                if (idItem != 0 || idProd != 0) {
                    SDbItem dbItem = new SDbItem();
                    dbItem.read(session, new int[] { idItem });
                    SDbTicket registry = new SDbTicket();
                    //registry.setPkTicketId(rstSoom.getInt("newId"));
                    registry.setNumber(rstRev.getInt("Pes_ID"));
                    registry.setDate(rstRev.getDate("Pes_FecHorPri"));
                    //registry.setQuantity(Quantity());
                    registry.setPlate(rstRev.getString("Pes_Placas"));
                    registry.setPlateCage(getContainerPlates(PlateCageLabels, rstRev.getString("Pes_ObsPri")));
                    registry.setDriver(rstRev.getString("Pes_Chofer"));
                    registry.setDatetimeArrival(rstRev.getTimestamp("Pes_FecHorPri"));
                    registry.setDatetimeDeparture(rstRev.getTimestamp("Pes_FecHorSeg") == null ? rstRev.getTimestamp("Pes_FecHorPri") : rstRev.getTimestamp("Pes_FecHorSeg"));
                    //registry.setPackageQuantityArrival(PackageQuantityArrival());
                    //registry.setPackageQuantityDeparture(PackageQuantityDeparture());
                    //registry.setPackageEmptyQuantityArrival(PackageEmptyQuantityArrival());
                    //registry.setPackageEmptyQuantityDeparture(PackageEmptyQuantityDeparture());
                    //registry.setPackageWeightArrival(PackageWeightArrival());
                    //registry.setPackageWeightDeparture(PackageWeightDeparture());
                    //registry.setPackageWeightNet_r(PackageWeightNet_r());
                    //registry.setWeightSource(WeightSource());
                    registry.setWeightDestinyArrival(rstRev.getDouble("Pes_PesoPri"));
                    registry.setWeightDestinyDeparture(rstRev.getString("Pes_PesoSeg") == null ? 0 : rstRev.getDouble("Pes_PesoSeg"));
                    registry.setWeightDestinyGross_r(rstRev.getInt("Pes_PesoPri"));
                    //registry.setWeightDestinyNet_r(WeightDestinyNet_r());
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
                    registry.setPackage(dbItem.isPackage());
                    registry.setLaboratory(dbItem.isLaboratory());
                    //registry.setDpsSupply(this.isDpsSupply());
                    //registry.setDeleted(this.isDeleted());
                    //registry.setSystem(this.isSystem());
                    registry.setFkScaleId(1);   //Revuelta
                    registry.setFkTicketStatusId(SModSysConsts.SS_TIC_ST_SCA);
                    registry.setFkProducerId(idProd);
                    registry.setFkItemId(idItem);
                    seasonId = getProperSeasonId(session, registry.getDate(), registry.getFkItemId(), registry.getFkProducerId());
                    registry.setFkSeasonId_n(seasonId);
                    registry.setFkRegionId_n(getProperRegionId(session, seasonId, registry.getFkItemId(), registry.getFkProducerId()));
                    registry.setFkUnitId(dbItem.getFkUnitId());
                    registry.setFkInputSourceId(1);
                    registry.setFkLaboratoryId_n(0);
                    //registry.setFkExternalDpsYearId_n(FkExternalDpsYearId_n());
                    //registry.setFkExternalDpsDocId_n(FkExternalDpsDocId_n());
                    //registry.setFkExternalDpsEntryId_n(FkExternalDpsEntryId_n());
                    //registry.setFkUserInsertId(FkUserInsertId());
                    //registry.setFkUserUpdateId(FkUserUpdateId());
                    //registry.setFkUserTaredId(FkUserTaredId());
                    //registry.setFkUserPayedId(FkUserPayedId());
                    //registry.setFkUserAssortedId(FkUserAssortedId());
                    //registry.setTsUserInsert(TsUserInsert());
                    //registry.setTsUserUpdate(TsUserUpdate());
                    //registry.setTsUserTared(TsUserTared());
                    //registry.setTsUserPayed(TsUserPayed());
                    //registry.setTsUserAssorted(TsUserAssorted());
                    //registry.setXtaScaleName(XtaScaleName());
                    //registry.setXtaScaleCode(XtaScaleCode());
                    //registry.setXtaSeason(XtaSeason());
                    //registry.setXtaRegion(XtaRegion());
                    //registry.setXtaItem(XtaItem());
                    //registry.setXtaProducer(XtaProducer());
                    //registry.setXtaProviderFiscalId(XtaProducerFiscalId());
                    //registry.setAuxFormerTared(this.isAuxFormerTared());
                    //registry.setAuxFormerPayed(this.isAuxFormerPayed());
                    //registry.setAuxFormerAssorted(this.isAuxFormerAssorted());
                    //registry.setAuxMoveNextOnSend(this.isAuxMoveNextOnSend());
                    //registry.setAuxRequiredCalculation(this.isAuxRequiredCalculation());
                    //registry.setAuxSendMail(this.isAuxSendMail());
                    //registry.setAuxRecipientsTo(AuxRecipientsTo());
                    SDbTicketNote note = new SDbTicketNote();
                    note.setNote(rstRev.getString("Pes_ObsPri"));
                    registry.getChildTicketNotes().add(note);
                    registry.save(session);
                    System.out.println("Boleto importado: " + rstRev.getInt("Pes_ID"));
                }
                else {
                    System.out.println("El boleto " + rstRev.getInt("Pes_ID") + " NO pudo ser importado debido al item: " + rstRev.getString("Pro_ID"));
                }
            }
        }
    }

    private static Connection openConnectionRevuelta() {
        Connection connectionRevuelta = null;
        try {
            Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            SLibUtils.printException(SImportTicketsRevuelta.class.getName(), e);
        }
        String url = "jdbc:sybase:Tds:192.168.1.33:2638/Revuelta";  // AETH
        Properties prop = new Properties();
        prop.put("user", "usuario");
        prop.put("password", "revuelta");
        try {
            connectionRevuelta = DriverManager.getConnection(url, prop);
        } catch (Exception e) {
            SLibUtils.printException(SImportTicketsRevuelta.class.getName(), e);
        }
        return connectionRevuelta;
    }

    private static java.sql.Connection openConnectionSom() {
        String DB = "som_com";
        String url = "jdbc:mysql://192.168.1.233:3306/" + DB;
        String username = "root";
        String password = "msroot";
        java.sql.Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        return connection;
    }

    /**
     * 
     * @param aBuscar Palabras a buscar separadas por ; (punto y coma).
     * @param cadena Cadena de texto en donde se van a buscar las palabras.
     * @return Regresa la siguiente palabra encontrada despues de la palabra buscada.
     * ejemplo:
     * aBuscar = "jaula;tolva;tq";
     * cadena = "PESO TEORICO: 37990 KG JAULA: 293XS3 pedido 0068049 carga completa";
     * return = "293XS3";
     */
    private static String getContainerPlates(String aBuscar, String cadena) {
        String[] valuesSplit = aBuscar.split(";");
        String placas = "";
        for (String value : valuesSplit) {
            
            Pattern pattern = Pattern.compile(value + "([\\s:]*[\\w]*)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(cadena);
            
            if (matcher.find()) {
                placas = matcher.group(1).replaceAll("[\\s:]","");
            }
            if (!"".equals(placas)){
                return placas;
            }
        }
        return "";
    }
}
