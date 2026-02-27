/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.cli.SCliConsts;
import som.cli.SCliMailerReportFruitsStdSummary;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbBranchWarehouse;
import som.mod.cfg.db.SDbCompany;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores, Isabel Servín, Sergio Flores
 */
public abstract class SSomUtils {
    
    private static final int IDX_CONV = 0;
    private static final int IDX_ALT = 1;
    private static final int IDX_TOT = 2;
    private static final double LAGGING_PCT = 0.15;
    private static final DecimalFormat DecimalFormatNegativeValue2D = new DecimalFormat("#,##0.00;(#,##0.00)");
    private static final SimpleDateFormat DateFormatGui = new SimpleDateFormat("dd/MMM./yyyy");
    
    /**
     * Gets proper season for ticket defined by date, item and producer.
     * @param session Current GUI session.
     * @param date Date of ticket.
     * @param itemId Item ID.
     * @param producerId Producer ID.
     * @return Proper season ID.
     * @throws java.lang.Exception
     */
    public static int getProperSeasonId(SGuiSession session, Date date, int itemId, int producerId) throws Exception {
        int seasonId = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT s.id_seas " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS sp ON s.id_seas = sp.id_seas AND sp.id_item = " + itemId + " AND sp.id_prod = " + producerId + " " +
                "WHERE s.b_del = 0 AND s.b_dis = 0 AND s.b_clo = 0 " +
                "AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' AND dt_end >= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' ORDER BY s.id_seas";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            seasonId = resultSet.getInt(1);
        }

        return  seasonId;
    }
    
    /**
     * Gets proper region for ticket difined by season, item and producer.
     * @param session Current GUI session.
     * @param seasonId Season ID.
     * @param itemId Item ID.
     * @param producerId Producer ID.
     * @return Proper region ID.
     * @throws java.lang.Exception
     */
    public static int getProperRegionId(SGuiSession session, int seasonId, int itemId, int producerId) throws Exception {
        int regionId = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_reg " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " " +
                "WHERE b_del = 0 AND b_dis = 0 AND id_seas = " + seasonId + " AND id_item = " + itemId + " AND id_prod = " + producerId + " ORDER BY b_def DESC, ts_usr_ins;";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            regionId = resultSet.getInt(1);
        }

        return regionId;
    }
    
    /**
     * Gets unit ID for external unit ID.
     * @param session Current GUI session.
     * @param externalUnitId External unit ID.
     * @return Unit ID.
     * @throws java.lang.Exception
     */
    public static int getUnitId(SGuiSession session, int externalUnitId) throws Exception {
        int unitId = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_unit " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " " +
                "WHERE fk_ext_unit_n = " + externalUnitId + " ";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            unitId = resultSet.getInt(1);
        }

        return unitId;
    }
    
    /**
     * Validates if ticket number already exists.
     * @param session Current GUI session.
     * @param ticketNumber Ticket number.
     * @param ticketId Ticket ID.
     * @return Value of <code>true</code> if ticket number already exists.
     * @throws java.lang.Exception
     */
    public static boolean existsTicket(final SGuiSession session, String ticketNumber, int ticketId) throws Exception {
        boolean exists = false;
        String sql;
        ResultSet resultSet;

        sql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
                "WHERE b_del = 0 AND num = '" + ticketNumber + "' " +
                (ticketId == SLibConsts.UNDEFINED ? "" : " AND id_tic <> " + ticketId + " ");

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            exists = resultSet.getInt(1) > 0;
        }

        return exists;
    }
    
    /**
     * Validar si el fue dividido en otros boletos.
     * @param session Current GUI session.
     * @param ticketId Ticket ID.
     * @return Value of <code>true</code> if is divided ticket.
     * @throws java.lang.Exception
     */
    public static boolean isDividedTicket(final SGuiSession session, int ticketId) throws Exception {
        boolean divided = false;
        String sql;
        ResultSet resultSet;

        sql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC_DIV) + " " +
                "WHERE id_tic_div = " + ticketId + " ";

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            divided = resultSet.getInt(1) > 0;
        }

        return divided;
    }
    
    /**
     * Validar si el boleto es parte del resultado de una división de otro boleto.
     * @param session Current GUI session.
     * @param ticketId Ticket ID.
     * @return Value of <code>true</code> if ticket is from divided ticket.
     * @throws java.lang.Exception
     */
    public static boolean isFromDividedTicket(final SGuiSession session, int ticketId) throws Exception {
        boolean divided = false;
        String sql;
        ResultSet resultSet;

        sql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC_DIV) + " " +
                "WHERE id_tic_new = " + ticketId + " ";

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            divided = resultSet.getInt(1) > 0;
        }

        return divided;
    }
    
    /**
     * Validar si el boleto es flete de otros boletos dependientes.
     * @param session Current GUI session.
     * @param ticketId Ticket ID.
     * @return String with dependent tickets.
     * @throws java.lang.Exception
     */
    public static String isFreightDependentTicket(final SGuiSession session, int ticketId) throws Exception {
        String tickets = "";
        String sql;
        ResultSet resultSet;

        sql = "SELECT num " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
                "WHERE fk_freight_tic_n = " + ticketId + " AND NOT b_del";

        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            tickets += (tickets.isEmpty() ?  "" : ", ") + resultSet.getString(1);
        }

        return tickets;
    }

    /**
     * Validates if Region Season Configuration exists.
     * @param session Current GUI session.
     * @param configKey Region Season Configuration primary key (index: 0 = season ID; 1 = region ID; 2 = item ID).
     * @return Value of <code>true</code> if Region Season Configuration exists.
     * @throws java.lang.Exception
     */
    public static boolean existsSeasonRegion(SGuiSession session, int[] configKey) throws Exception {
        boolean exists = false;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_REG) + " " +
                "WHERE b_del = 0 AND b_dis = 0 AND id_seas = " + configKey[0] + " AND id_reg = " + configKey[1] + " AND id_item = " + configKey[2] + " ";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            exists = resultSet.getInt(1) > 0;
        }

        return exists;
    }

    /**
     * Validates if Producer Season Configuration exists.
     * @param session Current GUI session.
     * @param configKey Region Season Configuration primary key (index: 0 = season ID; 1 = region ID; 2 = item ID, 3 = producer ID).
     * @return Value of <code>true</code> if Producer Season Configuration exists.
     * @throws java.lang.Exception
     */
    public static boolean existsSeasonProducer(SGuiSession session, int[] configKey) throws Exception {
        boolean exists = false;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " " +
                "WHERE b_del = 0 AND b_dis = 0 AND id_seas = " + configKey[0] + " AND id_reg = " + configKey[1] + " AND id_item = " + configKey[2] + " AND id_prod = " + configKey[3] + " ";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            exists = resultSet.getInt(1) > 0;
        }

        return exists;
    }

    /**
     * Completes tickets missing of season and region within provided period.
     * @param session Current GUI session.
     * @param dateStart Start date.
     * @param dateEnd End date
     * @return
     * @throws java.lang.Exception
     */
    public static String completeSeasonRegionTickets(SGuiSession session, Date dateStart, Date dateEnd) throws Exception {
        int seasonId = 0;
        int regionId = 0;
        int countTotal = 0;
        int countCompleted = 0;
        int countUncompleted = 0;
        boolean update = false;
        String msg = "";
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        SDbTicket ticket = null;
        
        statement = session.getStatement().getConnection().createStatement();
        
        // Obtain all alive tickets from provided period:
        
        sql = "SELECT id_tic, fk_seas_n, fk_reg_n " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
                "WHERE b_del = 0 AND dt BETWEEN '" +  SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" +  SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
                "ORDER BY id_tic ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            countTotal++; // counts all alive tickets
            
            // Process only tickets with missing season or region:
            
            seasonId = resultSet.getInt("fk_seas_n");
            regionId = resultSet.getInt("fk_reg_n");
            
            if (seasonId == SLibConsts.UNDEFINED || regionId == SLibConsts.UNDEFINED) {
                countUncompleted++; // counts only uncompleted tickets
                update = false;
                
                ticket = (SDbTicket) session.readRegistry(SModConsts.S_TIC, new int[] { resultSet.getInt("id_tic") });
                
                if (seasonId == SLibConsts.UNDEFINED) {
                    seasonId = getProperSeasonId(session, ticket.getDate(), ticket.getFkItemId(), ticket.getFkProducerId());

                    if (seasonId != SLibConsts.UNDEFINED) {
                        update = true;
                    }
                }

                if (seasonId != SLibConsts.UNDEFINED) {
                    if (regionId == SLibConsts.UNDEFINED) {
                        regionId = getProperRegionId(session, seasonId, ticket.getFkItemId(), ticket.getFkProducerId());

                        if (regionId != SLibConsts.UNDEFINED) {
                            update = true;
                        }
                    }
                }
                
                if (update) {
                    ticket.setFkSeasonId_n(seasonId);
                    ticket.setFkRegionId_n(regionId);
                    ticket.setAuxRequirePriceComputation(true);
                    ticket.save(session);
                    
                    if (seasonId != SLibConsts.UNDEFINED && regionId != SLibConsts.UNDEFINED) {
                        countCompleted++;
                    }
                }
            }
        }

        msg += "Boletos procesados del período del " + SLibUtils.DbmsDateFormatDate.format(dateStart) + " al " + SLibUtils.DbmsDateFormatDate.format(dateEnd) + ":\n";

        if (countTotal > 0) {
            msg += " * Total de boletos: " + SLibUtils.DecimalFormatInteger.format(countTotal) + ".\n";
            msg += " * Boletos completos: " + SLibUtils.DecimalFormatInteger.format(countTotal - countUncompleted) + ".\n";
            msg += " * Boletos incompletos: " + SLibUtils.DecimalFormatInteger.format(countUncompleted) + ".\n\n";

            msg += " * Boletos completados: " + SLibUtils.DecimalFormatInteger.format(countCompleted) + ".\n";
            msg += " * Boletos sin completar: " + SLibUtils.DecimalFormatInteger.format(countUncompleted - countCompleted) + ".\n";
        }
        else {
            msg += " * No hay boletos en este período.";
        }

        return msg;
    }
    
    /**
     * Update price per ton in tickets from date start indicated.
     * @param session Current GUI session.
     * @param dateStart Date start updated, can be null, if is case updat tickets all.
     * @param seasonProducerKey settings of season - producer Key; [0] = season, [1] = region, [2] = item, [3] = producer, the producer can not to exists.
     * @param priceTon new price per ton.
     * @return
     * @throws Exception 
     */
    public static boolean updateTicketsPriceTon(SGuiSession session, Date dateStart, int[] seasonProducerKey, final double priceTon) throws Exception {
        boolean update = false;
        boolean isByProducer = false;
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        SDbTicket ticket = null;
        
        statement = session.getStatement().getConnection().createStatement();
        
        isByProducer = seasonProducerKey.length >= 4;
        
        sql = "SELECT t.id_tic, sp.prc_ton AS _prc_ton " + 
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS sp ON t.fk_seas_n = sp.id_seas AND t.fk_reg_n = sp.id_reg AND " +
                "t.fk_item = sp.id_item AND t.fk_prod = sp.id_prod " +
                "WHERE t.b_del = 0 AND sp.b_del = 0 " +
                (dateStart == null ? "" : "AND t.dt >= '" +  SLibUtils.DbmsDateFormatDate.format(dateStart) + "' ") +
                "AND t.fk_seas_n = " + seasonProducerKey[0] + " AND t.fk_reg_n = " + seasonProducerKey[1] + " AND t.fk_item = " + seasonProducerKey[2] + 
                (isByProducer ? " AND t.fk_prod = " + seasonProducerKey[3] : "") + " ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            update = isByProducer || (!isByProducer && resultSet.getDouble("_prc_ton") == 0);
            
            if (update) {
                ticket = (SDbTicket) session.readRegistry(SModConsts.S_TIC, new int[] { resultSet.getInt("id_tic") });

                ticket.setSystemPricePerTon(priceTon);
                ticket.setAuxRequirePriceComputation(true);
                ticket.save(session);
                update = false;
            }
        }

        return true;
    }
    
    /**
     * Update tickets price freight.
     * @param session Current GUI session.
     * @param dateStart Date start updated, can be null, if is case updat tickets all.
     * @param seasonProducerKey settings of season - producer Key; [0] = season, [1] = region, [2] = item, [3] = producer, the producer can not to exists.
     * @param priceFreight new price freight.
     * @return
     * @throws Exception 
     */
    public static boolean updateTicketsPriceFreight(SGuiSession session, Date dateStart, int[] seasonProducerKey, final double priceFreight) throws Exception {
        boolean update = false;
        String sql = "";
        Statement statement = null;
        
        statement = session.getStatement().getConnection().createStatement();
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
                "SET usr_fre =  " + priceFreight + " " +
                "WHERE b_del = 0 " +
                (dateStart == null ? "" : "AND dt >= '" +  SLibUtils.DbmsDateFormatDate.format(dateStart) + "' ") +
                "AND fk_seas_n = " + seasonProducerKey[0] + " AND fk_reg_n = " + seasonProducerKey[1] + " AND fk_item = " + seasonProducerKey[2] + 
                (seasonProducerKey.length >= 4 ? " AND fk_prod = " + seasonProducerKey[3] : "") + " ";

        if (statement.execute(sql)) {
            update = true;
        }

        return update;
    }
    
    /**
     * Gets external IOG type from SOM IOG type.
     * @param somIogTypeKey SOM IOG type primary key.
     * @return External IOG type primary key.
     */
    public static int[] getExternalDiogType(int[] somIogTypeKey) {
        int[] iogTypeExtSys = new int[] {};

        if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_PUR_PUR)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_IN_PUR_PUR;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_SAL_SAL)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_IN_SAL_SAL;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_EXT_ADJ)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_IN_ADJ_ADJ;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_EXT_INV)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_IN_ADJ_INV;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_MFG_RM_RET)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_IN_MFG_RM_RET;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_IN_MFG_FG_ASD;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_PUR_PUR)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_OUT_PUR_PUR;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_SAL_SAL)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_OUT_SAL_SAL;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_EXT_ADJ)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_OUT_ADJ_ADJ;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_EXT_INV)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_OUT_ADJ_INV;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_OUT_MFG_RM_ASD;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_MFG_FG_RET)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_OUT_MFG_FG_RET;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_INT_TRA)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_IN_INT_TRA;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_INT_CNV)||
            SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_INT_MIX_PAS) ||
            SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_IN_INT_MIX_ACT)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_IN_INT_CNV;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_INT_TRA)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_OUT_INT_TRA;
        }
        else if (SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_INT_CNV) ||
            SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_INT_MIX_PAS) ||
            SLibUtils.compareKeys(somIogTypeKey, SModSysConsts.SS_IOG_TP_OUT_INT_MIX_ACT)) {
            iogTypeExtSys = SModSysConsts.EXT_TRNS_TP_IOG_OUT_INT_CNV;
        }

        return iogTypeExtSys;
    }

    /**
     * Obtain stock
     * @param session Current GUI session.
     * @param yearId int.
     * @param itemId int.
     * @param unitId int.
     * @param itemTypeId int.
     * @param warehouseKey int[].
     * @param divisionId int.
     * @param stockMoveKey int[].
     * @param date Date.
     * @param absolute boolean.
     * @param skipWaste
     * @return SSomStock
     */
    public static SSomStock obtainStock(SGuiSession session, final int yearId, final int itemId, final int unitId, final int itemTypeId,
            final int[] warehouseKey, final int divisionId, final int[] stockMoveKey, final Date date, final boolean absolute, boolean skipWaste) {
        String sql = "";
        ResultSet resultSet = null;
        SSomStock stock = new SSomStock("", "", 0d, "");

        try {
            sql = "SELECT CONCAT(i.name, ' (', i.code, ') ') AS f_item, u.code, " +
                    (absolute ?
                    "(SUM((COALESCE(s.mov_in * " +
                    "IF (g.b_del = 0 AND " +
                    "g.fk_iog_ct = " + SModSysConsts.SS_IOG_TP_IN_EXT_ADJ[0] + " AND " +
                    "g.fk_iog_cl = " + SModSysConsts.SS_IOG_TP_IN_EXT_ADJ[1] + " AND " +
                    "g.fk_iog_tp = " + SModSysConsts.SS_IOG_TP_IN_EXT_ADJ[2] + " AND " +
                    "g.b_sys = 1 AND s.id_year > " + yearId + " AND MONTH(g.dt) = 1 AND DAY(g.dt) = 1, 0, 1), 0) - COALESCE(s.mov_out, 0)))) " :
                    "COALESCE(SUM(s.mov_in - s.mov_out),0) ")+ "AS f_stock " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g ON " +
                "s.fk_iog = g.id_iog " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "s.id_item = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "s.id_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " +
                "s.id_co = wah.id_co AND s.id_cob = wah.id_cob AND s.id_wah = wah.id_wah  " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
                "s.id_div = d.id_div " +
                "WHERE s.b_del = 0 " +
                (itemId > SLibConsts.UNDEFINED ? "AND s.id_item = " + itemId : "") + " " +
                (unitId > SLibConsts.UNDEFINED ? "AND s.id_unit = " + unitId : "") + " " +
                (itemTypeId > SLibConsts.UNDEFINED ? "AND i.fk_item_tp = " + itemTypeId : "") + " AND " +
                "s.id_co = " + warehouseKey[0] + " " +
                (skipWaste ? "AND i.fk_item_tp != " + SModSysConsts.SS_ITEM_TP_CU + " " : "") +
                (warehouseKey.length >= 2 ? "AND s.id_cob = " + warehouseKey[1] : "") + " " +
                (warehouseKey.length >= 3 ? "AND s.id_wah = " + warehouseKey[2] : "") + " " +
                (divisionId > SLibConsts.UNDEFINED ? "AND s.id_div = " + divisionId : "") + " " +
                (!absolute ? "AND s.id_year = " + yearId + " AND s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " : "") + " " +
                (stockMoveKey != null ? ("AND s.id_mov <> " + stockMoveKey[7]) : "") + " ";

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                stock.setItem(itemId > SLibConsts.UNDEFINED ? resultSet.getString("f_item") : "");
                stock.setUnit(unitId > SLibConsts.UNDEFINED || itemTypeId > SLibConsts.UNDEFINED ? resultSet.getString("u.code") : "");
                stock.setStock(resultSet.getDouble("f_stock"));
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return stock;
    }
    
    /**
     * Obtain required freight by item id
     * @param session GUI session.
     * @param itemId Item ID.
     * @return
     * @throws Exception 
     */
    public static String getRequiredFreightForItem(final SGuiSession session, final int itemId) throws Exception {
        String reqFreight = "";
        String sql = "SELECT c.req_freight FROM " + SModConsts.TablesMap.get(SModConsts.SU_INP_CT) + " AS c " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON c.id_inp_ct = i.fk_inp_ct " +
                "WHERE i.id_item = " + itemId + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                reqFreight = resultSet.getString(1);
            }
        }
        
        return reqFreight;
    }
    
    /**
     * Validate stock to specific date and the end year date
     * @param session session,
     * @param nYearId int,
     * @param nItemId int,
     * @param nUnitId int,
     * @param nItemTypeId int,
     * @param anWarehouseId int[],
     * @param nDivisionId
     * @param anStockMoveId
     * @param date Date
     * @param dQuantity double
     * @return SSomStock
     */
    public static SSomStock validateStock(SGuiSession session, final int nYearId, final int nItemId, final int nUnitId, final int nItemTypeId,
            final int[] anWarehouseId, final int nDivisionId, final int[] anStockMoveId, final Date date, final double dQuantity) {
        SSomStock stock = null;

        stock = obtainStock(session, nYearId, nItemId, nUnitId, nItemTypeId, anWarehouseId, nDivisionId, anStockMoveId, date, false, false);
        if (stock.getStock() < 0 || stock.getStock() < dQuantity) {
            stock.setResult("No hay existencias suficientes del producto '" + stock.getItem() + "' a la fecha '" + SLibUtils.DateFormatDate.format(date) + "'.");
        }
        else {
            stock = obtainStock(session, nYearId, nItemId, nUnitId, nItemTypeId, anWarehouseId, nDivisionId, anStockMoveId, SLibTimeUtils.getBeginOfYear(date), true, false);
            if (stock.getStock() < 0 || stock.getStock() < dQuantity) {
                stock.setResult("No hay existencias absolutas suficientes.\n"
                        + "Existen movimientos posteriores que hacen que las existencias queden negativas.");
            }
            else {
                stock.setResult("");
            }
        }

        return stock;
    }

    /**
     * Obtain stock by volume
     * @param session Current GUI session.
     * @param nYearId int,
     * @param nItemId int,
     * @param nUnitId int,
     * @param nItemTypeId int,
     * @param anWarehouseID int[],
     * @param date Date
     * @return dVolumeTotal
     */
    public static double obtainStockByVolume(SGuiSession session, final int nYearId, final int nItemId, final int nUnitId, final int nItemTypeId,
            final int[] anWarehouseID, final Date date) {
        double dVolumeTotal = 0;
        String sSql = "";
        ResultSet resultSet = null;

        try {
            sSql = "SELECT CONCAT(i.name, ' (', i.code, ') ') AS f_item, u.code, " +
                "(SUM(s.mov_in - s.mov_out) / i.den) AS f_vol " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "s.id_item = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "s.id_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " +
                "s.id_co = wah.id_co AND s.id_cob = wah.id_cob AND s.id_wah = wah.id_wah  " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
                "s.id_div = d.id_div " +
                "WHERE s.b_del = 0 AND " +
                "s.id_year = " + nYearId + " " +
                (nItemId > SLibConsts.UNDEFINED ? "AND s.id_item = " + nItemId : "") + " " +
                (nUnitId > SLibConsts.UNDEFINED ? "AND s.id_unit = " + nUnitId : "") + " " +
                (nItemTypeId > SLibConsts.UNDEFINED ? "AND i.fk_item_tp = " + nItemTypeId : "") + " AND " +
                "s.id_co = " + anWarehouseID[0] + " " +
                (anWarehouseID.length >= 2 ? "AND s.id_cob = " + anWarehouseID[1] : "") + " " +
                (anWarehouseID.length >= 3 ? "AND s.id_wah = " + anWarehouseID[2] : "") + " " +
                "AND s.dt < '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                "GROUP BY s.id_item, s.id_unit " +
                "ORDER BY s.id_item, s.id_unit; ";

            resultSet = session.getStatement().executeQuery(sSql);
            while (resultSet.next()) {
                dVolumeTotal += resultSet.getDouble("f_vol");
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return dVolumeTotal;
    }

    /**
     * Obtain daily stock
     * @param session Current GUI session.
     * @param nYearId int,
     * @param nItemId int,
     * @param nUnitId int,
     * @param nItemTypeId int,
     * @param anWarehouseID int[],
     * @param date Date
     * @return dStockDay
     */
    public static double obtainStockDay(SGuiSession session, final int nYearId, final int nItemId, final int nUnitId, final int nItemTypeId,
            final int[] anWarehouseID, final Date date) {
        double dStockDay = 0;
        String sSql = "";
        ResultSet resultSet = null;

        try {
            sSql = "SELECT CONCAT(i.name, ' (', i.code, ') ') AS f_item, u.code, s.stk_day AS f_stk_day " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "s.id_item = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "s.id_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " +
                "s.id_co = wah.id_co AND s.id_cob = wah.id_cob AND s.id_wah = wah.id_wah  " +
                "WHERE s.b_del = 0 AND " +
                "s.id_year = " + nYearId + " " +
                (nItemId > SLibConsts.UNDEFINED ? "AND s.id_item = " + nItemId : "") + " " +
                (nUnitId > SLibConsts.UNDEFINED ? "AND s.id_unit = " + nUnitId : "") + " " +
                (nItemTypeId > SLibConsts.UNDEFINED ? "AND i.fk_item_tp = " + nItemTypeId : "") + " AND s.id_co = " + anWarehouseID[0] + " " +
                (anWarehouseID.length >= 2 ? "AND s.id_cob = " + anWarehouseID[1] : "") + " " +
                (anWarehouseID.length >= 3 ? "AND s.id_wah = " + anWarehouseID[2] : "") + " AND " +
                "s.dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                "GROUP BY s.id_item, s.id_unit " +
                "ORDER BY s.id_item, s.id_unit; ";

            resultSet = session.getStatement().executeQuery(sSql);
            if (resultSet.next()) {
                dStockDay += resultSet.getDouble("f_stk_day");
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return dStockDay;
    }

    /**
     * Obtain conexion with system Revuelta XXI using ODBC
     * @param session Current GUI session.
     * @return Connection
     */
    @Deprecated
    public static Connection openConnectionRevueltaOdbc(SGuiSession session) {
        Connection connection = null;

        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

            connection = DriverManager.getConnection("jdbc:odbc:" + ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getRevueltaOdbc());
        }
        catch (ClassNotFoundException | SQLException e) {
            SLibUtils.printException(SSomUtils.class.getName(), e);
        }

        return connection;
    }

    /**
     * Obtain conexion with system Revuelta using JDBC jconn3 Driver
     * @param session Current GUI session.
     * @return Connection
     */
    public static Connection openConnectionRevueltaJdbc(SGuiSession session) {
        Connection connection = null;

        try {
            Class.forName("com.sybase.jdbc3.jdbc.SybDriver");
            SDbCompany company = new SDbCompany();
            company.read(session, new int[] { SUtilConsts.BPR_CO_ID });
            String url = "jdbc:sybase:Tds:" + company.getRevueltaHost() + ":" + company.getRevueltaPort() + "/Revuelta"; // XXX 2020-01-14, Sergio Flores: Improve this. This paramater is fixed!
            Properties prop = new Properties();
            prop.put("user", "usuario"); // XXX 2020-01-14, Sergio Flores: Improve this. This paramater is fixed!
            prop.put("password", "revuelta"); // XXX 2020-01-14, Sergio Flores: Improve this. This paramater is fixed!
            connection = DriverManager.getConnection(url, prop);
        }
        catch (Exception e) {
            SLibUtils.printException(SSomUtils.class.getName(), e);
        }

        return connection;
    }

    /**
     * Obtain producer ID on SOM, from producer ID on Revuelta.
     * @param session Current GUI session.
     * @param revueltaProducerId Producer ID on Revuelta.
     * @return Producer ID on SOM
     */
    public static int mapProducerSomRevuelta(SGuiSession session, String revueltaProducerId) {
        String sql = "";
        int producerSomId = 0;
        ResultSet resultSet;

        try {
            sql = "SELECT id_prod FROM " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " " +
                    "WHERE b_del = 0 AND b_dis = 0 AND rev_prod_id = '" + revueltaProducerId + "' ";

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                producerSomId = resultSet.getInt(1);
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return producerSomId;
    }

    /**
     * Obtain item ID on SOM, from item ID on Revuelta.
     * @param session Current GUI session.
     * @param revueltaItemId Item ID on Revuelta.
     * @return Item ID on SOM
     */
    public static int mapItemSomRevuelta(SGuiSession session, String revueltaItemId) {
        String sql = "";
        int itemSomId = 0;
        ResultSet resultSet;

        try {
            sql = "SELECT id_item FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " " +
                    "WHERE b_del = 0 AND b_dis = 0 AND rev_item_id = '" + revueltaItemId + "' ";

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                itemSomId = resultSet.getInt(1);
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return itemSomId;
    }

    /**
     * Obtain all items mapped from Revuelta on SOM.
     * @param session Current GUI session.
     * @return String of items ID's
     */
    public static String mapItemSomRevuelta(SGuiSession session) {
        String sSql;
        String revueltaItemId = "";
        ResultSet resultSet;

        try {
            sSql = "SELECT DISTINCT rev_item_id " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " " +
                    "WHERE NOT b_del AND NOT b_dis AND rev_item_id <> '' " +
                    "ORDER BY rev_item_id ";

            resultSet = session.getStatement().executeQuery(sSql);
            while (resultSet.next()) {
                revueltaItemId += "'" + resultSet.getString(1) + "'" + (resultSet.isLast() ? "" : ",");
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return revueltaItemId;
    }

    /**
     * Gets ticket by laboratory key.
     * @param session Current GUI session.
     * @param laboratoryKey Laborator
     * @return String of items ID's
     */
    public static int[] getTicketByLaboratory(SGuiSession session, int[] laboratoryKey) {
        int[] ticketKey = null;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT id_tic FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " "
                    + "WHERE b_del = 0 AND fk_lab_n = " + laboratoryKey[0] + " "
                    + "ORDER BY id_tic ";

            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                ticketKey = new int[] { resultSet.getInt(1) };
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return ticketKey;
    }
    
    /**
     * Validate warehouse item (business rules [1. Warehouse item (only one FG), 2. Warehouse capacity, 3. Warehouse unit])
     * @param session SGuiSession,
     * @param paramPkWarehouseId int[],
     * @param paramPkStockId int,
     * @param paramDate Date,
     * @param paramFkItemId int,
     * @param paramFkUnitId int,
     * @param paramQuantity double,
     * @param paramXtaUnit String,
     * @param paramXtaValidateWarehouseCapacity boolean,
     * @param paramXtaValidateExportationStock boolean
     * @param paramFkMixId
     * @return String msg error
     * @throws java.lang.Exception
     */
    public static String obtainWarehouseItemsForBusinessRules(final SGuiSession session, final int[] paramPkWarehouseId, final int[] paramPkStockId, final Date paramDate,
            final int paramFkItemId, final int paramFkUnitId, final double paramQuantity, final String paramXtaUnit, final boolean paramXtaValidateWarehouseCapacity,
            final boolean paramXtaValidateExportationStock, final int paramFkMixId) throws Exception {
        String result = "";

        SDbBranchWarehouse warehouse = null;
        SDbItem item = null;
        SDbUnit unit = null;
        SSomWarehouseItem somWarehouseItem = null;
        ArrayList<SSomWarehouseItem> aWarehouseItems = new ArrayList<>();

        warehouse = new SDbBranchWarehouse();
        warehouse.read(session, paramPkWarehouseId);

        item = new SDbItem();
        item.read(session, new int[] { paramFkItemId });

        unit = new SDbUnit();
        unit.read(session, new int[] { paramFkUnitId });

        // Obtain warehouse by item to date:

        aWarehouseItems = stockWarehouseByItem(session, SLibTimeUtils.digestYear(paramDate)[0], paramPkWarehouseId,
                (paramPkStockId != null ? paramPkStockId : null), paramDate, null, SLibConsts.UNDEFINED, paramFkMixId);

        // Validate if there are item into warehouse

        if (aWarehouseItems.isEmpty()) {

            somWarehouseItem = new SSomWarehouseItem(paramFkItemId, paramFkUnitId, item.getFkItemTypeId(), item.getName(), unit.getCode(), 0d, item.getDensity());
            aWarehouseItems.add(somWarehouseItem);
        }

        result = validateWarehouseBusinessRules(paramDate, warehouse, item, aWarehouseItems, paramFkItemId, paramFkUnitId, paramQuantity, paramXtaUnit, paramXtaValidateWarehouseCapacity);
        if (result.isEmpty()) {
            if (!paramXtaValidateExportationStock) {

                // Obtain warehouse by item to last date of year:

                aWarehouseItems = stockWarehouseByItem(session, SLibTimeUtils.digestYear(paramDate)[0], paramPkWarehouseId,
                    (paramPkStockId != null ? paramPkStockId : null), paramDate, SLibTimeUtils.getEndOfYear(paramDate), SLibConsts.UNDEFINED, paramFkMixId);

                if (aWarehouseItems.isEmpty()) {

                    somWarehouseItem = new SSomWarehouseItem(paramFkItemId, paramFkUnitId, item.getFkItemTypeId(), item.getName(), unit.getCode(), 0d, item.getDensity());
                    aWarehouseItems.add(somWarehouseItem);
                }

                result = validateWarehouseBusinessRules(SLibTimeUtils.getEndOfYear(paramDate), warehouse, item, aWarehouseItems, paramFkItemId, paramFkUnitId, paramQuantity, paramXtaUnit, paramXtaValidateWarehouseCapacity);
            }
        }

        return result;
    }

    /**
     * Obtain stock warehouse by item
     * @param paramWarehouse SDbBranchWarehouse,
     * @param paramItem SDbItem,
     * @param aWarehouseItems ArrayList<SSomWarehouseItem>,
     * @param paramFkItemId int,
     * @param paramFkUnitId int,
     * @param paramQuantity double,
     * @param paramXtaUnit String,
     * @param paramXtaValidateWarehouseCapacity boolean,
     * @return result
     */
    private static String validateWarehouseBusinessRules(final Date paramDate, final SDbBranchWarehouse paramWarehouse, final SDbItem paramItem, final ArrayList<SSomWarehouseItem> aWarehouseItems,
            final int paramFkItemId, final int paramFkUnitId, final double paramQuantity, final String paramXtaUnit, final boolean paramXtaValidateWarehouseCapacity) {
        String result = "";
        double dWarehouseCapacity = 0;

        for (SSomWarehouseItem warehouseItem : aWarehouseItems) {

            if (paramWarehouse.getFkWarehouseTypeId() != SModSysConsts.CS_WAH_TP_WAH) {

                // Validate only if item is not a cull:

                if ((paramFkItemId != warehouseItem.getPkItemId() ||
                        paramFkUnitId != warehouseItem.getPkUnitId()) &&
                    paramItem.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_CU &&
                    warehouseItem.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_CU) {

                    result = "El almacén '" + paramWarehouse.getCode() + "' ya contiene un ítem con existencias: '" +
                            warehouseItem.getItem() + "' a la fecha: '" + SLibUtils.DateFormatDate.format(paramDate) + "'.";
                    break;
                }
            }

            // Validate total capacity in warehouse:

            if (paramXtaValidateWarehouseCapacity) {

                dWarehouseCapacity = (warehouseItem.getDensity() > 0 ? (warehouseItem.getStock() / warehouseItem.getDensity()) : (warehouseItem.getStock() / paramItem.getDensity()));
                if ((dWarehouseCapacity + (paramQuantity / paramItem.getDensity())) > paramWarehouse.getCapacityRealLiter() &&
                        paramWarehouse.getFkWarehouseTypeId() == SModSysConsts.CS_WAH_TP_TAN) {
                    result = "La cantidad capturada " + (paramQuantity > 0 ? " de: '" + SLibUtils.DecimalFormatValue2D.format(paramQuantity) + " " + paramXtaUnit + "'" : " en el movimiento ") +
                            " hace que el almacén: '" + paramWarehouse.getCode() + "' sobrepase su capacidad.";
                    break;
                }
            }

            // Validate units:

            if (paramFkUnitId != warehouseItem.getPkUnitId() &&
                warehouseItem.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_CU) {

                result = "El ítem '" + paramItem.getName() + " (" + paramXtaUnit + ")' tiene una unidad diferente a la unidad en el almacén: '" + paramWarehouse.getCode() + " unidad: " + warehouseItem.getUnit() + "'.";
                break;
            }
        }

        return result;
    }

    /**
     * Obtain stock warehouse by item.
     * @param session SGuiSession,
     * @param nYearId int,
     * @param anWarehouseId int[],
     * @param anStockMoveId int[],
     * @param dateStart, Date,
     * @param dateEnd, Date,
     * @param nDivisionId int,
     * @param nMixId
     * @return Arra of <code>SSomWarehouseId</code>
     */
    public static ArrayList<SSomWarehouseItem> stockWarehouseByItem(final SGuiSession session, final int nYearId, final int[] anWarehouseId, final int[] anStockMoveId,
            final Date dateStart, final Date dateEnd, final int nDivisionId, final int nMixId) {
        String sql = "";
        ArrayList<SSomWarehouseItem> aWarehouseItem = new ArrayList<>();
        SSomWarehouseItem warehouseItem = null;
        ResultSet resultSet = null;

        try {
            sql = "SELECT g.dt, i.id_item, u.id_unit, i.fk_item_tp, CONCAT(i.name, ' (', i.code, ') ') AS f_item, u.code, i.den, " +
                "SUM(s.mov_in - s.mov_out) AS f_stock " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g ON " +
                "s.fk_iog = g.id_iog " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "s.id_item = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "s.id_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " +
                "s.id_co = wah.id_co AND s.id_cob = wah.id_cob AND s.id_wah = wah.id_wah  " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
                "s.id_div = d.id_div " +
                "WHERE s.b_del = 0 AND " +
                "s.id_year = " + nYearId + " AND " +
                "s.id_co = " + anWarehouseId[0] + " AND " +
                "s.id_cob = " + anWarehouseId[1] + " AND " +
                "s.id_wah = " + anWarehouseId[2] + " AND " +
                (nDivisionId != SLibConsts.UNDEFINED ? ("s.id_div = " + nDivisionId + " AND ") : "") +
                (dateEnd == null ? "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' " :
                "s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd)+ "' ") + " " +
                (anStockMoveId != null ? ("AND s.id_mov <> " + anStockMoveId[7]) : "") + " " +
                (nMixId != SLibConsts.UNDEFINED ? ("AND (g.fk_mix_n IS NULL OR g.fk_mix_n <> " + nMixId + ")") : "") + " " +
                "GROUP BY " + (dateEnd == null ? "" : "g.dt, ") + "i.id_item, u.id_unit " +
                "HAVING f_stock > 0 " +
                "ORDER BY " + (dateEnd == null ? "" : "g.dt, ") + "i.id_item, u.id_unit ";

            Statement statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            aWarehouseItem.clear();
            while (resultSet.next()) {

                warehouseItem = new SSomWarehouseItem(
                    resultSet.getInt("i.id_item"),
                    resultSet.getInt("u.id_unit"),
                    resultSet.getInt("i.fk_item_tp"),
                    resultSet.getString("f_item"),
                    resultSet.getString("u.code"),
                    resultSet.getDouble("f_stock"),
                    resultSet.getDouble("i.den"));
                aWarehouseItem.add(warehouseItem);
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return aWarehouseItem;
    }

    /**
     * Distribute production estimate into warehouses by product
     * @param session SGuiSession,
     * @param pDate Date,
     * @param pnDivisionId int,
     * @param pnProductionEstimateId int,
     * @param pnProductionEstimateVersionId int,
     * @param paMfgWarehouseProductProduction ArrayList<SSomManufacturingWarehouseProduct>,
     * @param paWarehouseProductStorage ArrayList<SSomManufacturingWarehouseProduct>
     * @return SSomProductionEstimateDistributeWarehouses
     * @throws java.lang.Exception
     */
    public static SSomProductionEstimateDistributeWarehouses productionEstimateDistribute(final SGuiSession session, final Date pDate, final int pnDivisionId,
            final int pnProductionEstimateId, final int pnProductionEstimateVersionId, final ArrayList<SSomMfgWarehouseProduct> paMfgWarehouseProductProduction,
            final ArrayList<SSomMfgWarehouseProduct> paWarehouseProductStorage) throws Exception {

        SSomMfgWarehouseProduct productProduction = null;

        SSomProductionEstimateDistributeWarehouses productionEstimateDistribute = new SSomProductionEstimateDistributeWarehouses();
        SSomProductionEstimateDistributeWarehouses productionEstimateDistributeAux = new SSomProductionEstimateDistributeWarehouses();

        productionEstimateDistribute.getMfgWarehouseProductProduction().addAll(paMfgWarehouseProductProduction);
        productionEstimateDistribute.getMfgWarehouseProductStorage().addAll(paWarehouseProductStorage);
        for (int i=0; i<productionEstimateDistribute.getMfgWarehouseProductProduction().size(); i++) {

            productProduction = productionEstimateDistribute.getMfgWarehouseProductProduction().get(i);
            if (productProduction.getQuantity() < 0) {

                productionEstimateDistribute.setProductionAvailable(productProduction.getQuantity());
                for (int source=1; source<3; source++) {

                    productionEstimateDistributeAux = productionEstimateDistribution(session, pDate, pnDivisionId, pnProductionEstimateId, pnProductionEstimateVersionId,
                            productProduction, source, productionEstimateDistribute.getMfgWarehouseProductStorage());
                    productionEstimateDistribute.getIogIn().addAll(productionEstimateDistributeAux.getIogIn());
                    productionEstimateDistribute.getIogOut().addAll(productionEstimateDistributeAux.getIogOut());
                    productionEstimateDistribute.setProductionAvailable(productionEstimateDistributeAux.getProductionAvailable());
                    productProduction.setQuantity(productionEstimateDistributeAux.getProductionAvailable());
                    productionEstimateDistribute.getMfgWarehouseProductProduction().set(i, productProduction);
                    if (productionEstimateDistributeAux.getProductionAvailable() == 0) {

                        break;
                    }
                }
            }
        }

        return productionEstimateDistribute;
    }

    /**
     * Distribute production estimate into warehouses by product
     * @param session SGuiSession,
     * @param pDate Date,
     * @param pnDivisionId int,
     * @param pnProductionEstimateId int,
     * @param pnProductionEstimateVersionId int,
     * @param poMfgWarehouseProductProduction Production Product,
     * @param pnSource int,
     * @param paMfgWarehouseProductStorage Manufacturing Product Storage,
     * @return SSomProductionEstimateDistributeWarehouses
     */
    public static SSomProductionEstimateDistributeWarehouses productionEstimateDistribution(final SGuiSession session, final Date pDate, final int pnDivisionId,
            final int pnProductionEstimateId, final int pnProductionEstimateVersionId, final SSomMfgWarehouseProduct poMfgWarehouseProductProduction, int pnSource,
            final ArrayList<SSomMfgWarehouseProduct> paMfgWarehouseProductStorage) {
        SDbIog inIog = null;
        SDbIog outIog = null;
        SSomProductionEstimateDistributeWarehouses productionEstimateDistribute = new SSomProductionEstimateDistributeWarehouses();
        SSomMfgWarehouseProduct mfgWarehouseProductStorage = null;

        try {
            productionEstimateDistribute.getMfgWarehouseProductStorage().addAll(paMfgWarehouseProductStorage);
            productionEstimateDistribute.setProductionAvailable(SLibUtils.round(poMfgWarehouseProductProduction.getQuantity() * -1, SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()));

            // Find the storage warehouse containing the same item home:

            for (int j=0; j<productionEstimateDistribute.getMfgWarehouseProductStorage().size(); j++) {

                mfgWarehouseProductStorage = productionEstimateDistribute.getMfgWarehouseProductStorage().get(j);
                if (productionEstimateDistribute.getProductionAvailable() == 0) {

                    break;
                }
                else if ((pnSource == 1 && poMfgWarehouseProductProduction.getPkItemId() == mfgWarehouseProductStorage.getFkItemSource1Id_n()) ||
                        (pnSource == 2 && poMfgWarehouseProductProduction.getPkItemId() == mfgWarehouseProductStorage.getFkItemSource2Id_n())) {

                    // Quantity to assign:

                    if (productionEstimateDistribute.getProductionAvailable() <= mfgWarehouseProductStorage.getQuantity()) {

                        productionEstimateDistribute.setProductionAssigned(productionEstimateDistribute.getProductionAvailable());
                    }
                    else {

                        productionEstimateDistribute.setProductionAssigned(SLibUtils.round(mfgWarehouseProductStorage.getQuantity(), SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()));
                    }

                    // Production move out (warehouse production):

                    inIog = new SDbIog();
                    inIog.computeIog(SLibTimeUtils.createDate(SLibTimeUtils.digestDate(pDate)[0], SLibTimeUtils.digestDate(pDate)[1], SLibTimeUtils.digestDate(pDate)[2]-1),
                        productionEstimateDistribute.getProductionAssigned(), true, SModSysConsts.SS_IOG_TP_OUT_MFG_FG_ASD, SModSysConsts.SU_IOG_ADJ_TP_NA, poMfgWarehouseProductProduction.getPkItemId(), poMfgWarehouseProductProduction.getPkUnitId(),
                        new int[] { poMfgWarehouseProductProduction.getPkCompanyId(), poMfgWarehouseProductProduction.getPkBranchId(), poMfgWarehouseProductProduction.getPkWarehouseId() }, pnDivisionId, pnProductionEstimateId,
                        pnProductionEstimateVersionId, session.getUser().getPkUserId());
                    inIog.setSystem(true);
                    productionEstimateDistribute.getIogOut().add(inIog);

                    // Production move in (warehouse storage):

                    outIog = new SDbIog();
                    outIog.computeIog(SLibTimeUtils.createDate(SLibTimeUtils.digestDate(pDate)[0], SLibTimeUtils.digestDate(pDate)[1], SLibTimeUtils.digestDate(pDate)[2]-1),
                        productionEstimateDistribute.getProductionAssigned(), true, SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD, SModSysConsts.SU_IOG_ADJ_TP_NA, mfgWarehouseProductStorage.getPkItemId(), mfgWarehouseProductStorage.getPkUnitId(),
                        new int[] { mfgWarehouseProductStorage.getPkCompanyId(), mfgWarehouseProductStorage.getPkBranchId(), mfgWarehouseProductStorage.getPkWarehouseId() }, pnDivisionId, pnProductionEstimateId,
                        pnProductionEstimateVersionId, session.getUser().getPkUserId());
                    outIog.setSystem(true);
                    productionEstimateDistribute.getIogIn().add(outIog);

                    // Each one moving in/out production, decrease the amount of storage in the warehouse:

                    productionEstimateDistribute.setProductionAvailable(SLibUtils.round(productionEstimateDistribute.getProductionAvailable() -
                            productionEstimateDistribute.getProductionAssigned(), SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()));
                    mfgWarehouseProductStorage.setQuantity(SLibUtils.round(mfgWarehouseProductStorage.getQuantity() - productionEstimateDistribute.getProductionAssigned(), SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()));
                    productionEstimateDistribute.getMfgWarehouseProductStorage().set(j, mfgWarehouseProductStorage);
                }
            }

            productionEstimateDistribute.setProductionAvailable(
                    productionEstimateDistribute.getProductionAvailable() != 0 ?
                    SLibUtils.round(productionEstimateDistribute.getProductionAvailable() * -1, SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()):
                    productionEstimateDistribute.getProductionAvailable());
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return productionEstimateDistribute;
    }

    /**
     * Delete the IOG's if the production estimate processing returns an error.
     * @param session SGuiSession,
     * @param mfgEstimationId int,
     * @param mfgEstimationVersionId int
     * @throws java.sql.SQLException
     */
    public static void productionEstimateDeleteIogs(final SGuiSession session, final int mfgEstimationId, final int mfgEstimationVersionId) throws SQLException, Exception {
        String sql = "";
        SDbIog iog = null;
        Statement statement = null;
        ResultSet resultSet = null;

        statement = session.getDatabase().getConnection().createStatement();
        
        sql = "SELECT id_iog "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g "
                + "WHERE fk_mfg_est_n = " + mfgEstimationId + " AND fk_mfg_est_ver_n = " + mfgEstimationVersionId + " ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            iog = (SDbIog) session.readRegistry(SModConsts.S_IOG, new int[] { resultSet.getInt(1) });
            if (iog.canDelete(session)) {
                iog.delete(session);
            }
        }
    }
    
    /**
     * Create origins map.
     * @param session
     * @param idItem
     * @return
     * @throws SQLException 
     */
    public static HashMap<Integer, String> createOriginsMap(SGuiSession session, int idItem) throws SQLException {
        HashMap<Integer, String> origins = new HashMap<>();
        
        String sql = "SELECT sis.id_inp_src, sis.name "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_INP_SRC) +" AS sis "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS si ON sis.fk_inp_ct = si.fk_inp_ct "
                + "WHERE id_item = " + idItem;  
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            while (resultSet.next()){
                origins.put(resultSet.getInt(1), resultSet.getString(2));
            }
        }
        return origins;
    }
    
    /**
     * Obtain weight on destiny from tickes on provided period.
     * @param session Current GUI session.
     * @param idItem ID of item.
     * @param dateStart Start date.
     * @param dateEnd End date.
     * @param idReportingGroup ID of reporting group, can be zero.
     * @param idProducer ID of producer, can be zero.
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @return double
     * @throws java.sql.SQLException
     */
    private static double obtainWeightDestinyByPeriod(final SGuiSession session, final int idItem, final Date dateStart, final Date dateEnd, 
            final int idReportingGroup, final int idProducer, final int ticketOrigin, final int ticketDestination) throws SQLException {
        double weight = 0;

        String sql = "SELECT SUM(t.wei_des_net_r) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON t.fk_prod = p.id_prod " +
            "WHERE NOT t.b_del AND t.b_tar AND t.fk_item = " + idItem + " AND " +
            "t.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            (idProducer == 0 ? "" : "AND p.id_prod = " + idProducer + " ") +
            (ticketOrigin == 0 ? "" : "AND t.fk_tic_orig = " + ticketOrigin + " ") +
            (ticketDestination == 0 ? "" : "AND t.fk_tic_dest = " + ticketDestination + " ") +
            (idReportingGroup == 0 ? "" : "AND p.fk_rep_grp = " + idReportingGroup + " ") + ";";
        ResultSet resultSet = session.getStatement().executeQuery(sql);

        if (resultSet.next()) {
            weight = resultSet.getDouble(1);
        }

        return weight;
    }
    
    /**
     * Obtain weight on destiny from tickes on provided period.
     * @param session Current GUI session.
     * @param idItemCnv ID of conventional item.
     * @param idItemAlt ID of alternative item.
     * @param dateStart Start date.
     * @param dateEnd End date.
     * @param idReportingGroup ID of reporting group, can be zero.
     * @param idProducer ID of producer, can be zero.
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @return double
     * @throws java.sql.SQLException
     */
    private static double[] obtainWeightDestinyByPeriodAlt(final SGuiSession session, final int idItemCnv, final int idItemAlt, final Date dateStart, final Date dateEnd, 
            final int idReportingGroup, final int idProducer, final int ticketOrigin, final int ticketDestination) throws SQLException {
        double[] arr = null;

        String sql = "SELECT " +
            "SUM(IF(t.b_alt = 0, t.wei_des_net_r, 0)) AS _tic, " +
            "SUM(IF(t.b_alt = 1, t.wei_des_net_r, 0)) AS _alt, " +
            "SUM(t.wei_des_net_r) AS _tot " +
            "FROM (" + 
            "SELECT *, dt fecha FROM " +
            SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
            "WHERE NOT b_del AND NOT b_alt AND b_tar AND fk_item = " + idItemCnv + " " +
            "UNION " +
            "SELECT a.*, t.dt fecha FROM " +
            SModConsts.TablesMap.get(SModConsts.S_ALT_TIC) + " a " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " t ON a.id_tic = t.id_tic " + 
            "WHERE NOT a.b_del AND a.b_alt AND a.fk_item = " + idItemAlt + " ) AS t " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON t.fk_prod = p.id_prod " +
            "WHERE NOT t.b_del AND " +
            "t.fecha BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            (idProducer == 0 ? "" : "AND p.id_prod = " + idProducer + " ") +
            (ticketOrigin == 0 ? "" : "AND t.fk_tic_orig = " + ticketOrigin + " ") +
            (ticketDestination == 0 ? "" : "AND t.fk_tic_dest = " + ticketDestination + " ") +
            (idReportingGroup == 0 ? "" : "AND p.fk_rep_grp = " + idReportingGroup + " ") + ";";
        ResultSet resultSet = session.getStatement().executeQuery(sql);

        if (resultSet.next()) {
            arr = new double[] { resultSet.getDouble(1), resultSet.getDouble(2), resultSet.getDouble(3) };
        }

        return arr;
    }
    
    /**
     * Obtain weight on destiny and on a given scale from tickes on the provided period.
     * @param session Current GUI session.
     * @param idItem int,
     * @param dateStart Date,
     * @param dateEnd Date,
     * @param idScale int
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @return double
     * @throws java.sql.SQLException
     */
    private static double obtainWeightDestinyByScale(final SGuiSession session, final int idItem, final Date dateStart, final Date dateEnd, final int idScale,
            final int ticketOrigin, final int ticketDestination) throws SQLException {
        double weight = 0;

        String sql = "SELECT SUM(t.wei_des_net_r) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS s ON t.fk_sca = s.id_sca " +
            "WHERE NOT t.b_del AND t.b_tar AND t.fk_item = " + idItem + " " +
            (ticketOrigin == 0 ? "" : "AND t.fk_tic_orig = " + ticketOrigin + " ") +
            (ticketDestination == 0 ? "" : "AND t.fk_tic_dest = " + ticketDestination + " ") +
            "AND t.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            "AND t.fk_sca = " + idScale + ";";
        ResultSet resultSet = session.getStatement().executeQuery(sql);

        if (resultSet.next()) {
            weight = resultSet.getDouble(1);
        }

        return weight;
    }
    
    /**
     * Obtain weight on destiny and on a given scale from tickes on the provided period.
     * @param session Current GUI session.
     * @param idItemCnv ID of conventional item.
     * @param idItemAlt ID of alternative item.
     * @param dateStart Start date.
     * @param dateEnd End date.
     * @param idScale ID of scale.
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @return double
     * @throws java.sql.SQLException
     */
    private static double[] obtainWeightDestinyByScaleAlt(final SGuiSession session, final int idItemCnv, final int idItemAlt, final Date dateStart, final Date dateEnd, final int idScale,
            final int ticketOrigin, final int ticketDestination) throws SQLException {
        double[] arr = null;

        String sql = "SELECT " +
            "SUM(IF(t.b_alt = 0, t.wei_des_net_r, 0)) AS _tic, " +
            "SUM(IF(t.b_alt = 1, t.wei_des_net_r, 0)) AS _alt, " +
            "SUM(t.wei_des_net_r) AS _tot " +
            "FROM (" + 
            "SELECT *, dt fecha FROM " +
            SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
            "WHERE NOT b_del AND NOT b_alt AND b_tar AND fk_item = " + idItemCnv + " " +
            "UNION " +
            "SELECT a.*, t.dt fecha FROM " +
            SModConsts.TablesMap.get(SModConsts.S_ALT_TIC) + " a " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " t ON a.id_tic = t.id_tic " + 
            "WHERE NOT a.b_del AND a.b_alt AND a.fk_item = " + idItemAlt + " ) AS t " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS s ON t.fk_sca = s.id_sca " +
            "WHERE NOT t.b_del " +
            (ticketOrigin == 0 ? "" : "AND t.fk_tic_orig = " + ticketOrigin + " ") +
            (ticketDestination == 0 ? "" : "AND t.fk_tic_dest = " + ticketDestination + " ") +
            "AND t.fecha BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            "AND t.fk_sca = " + idScale + ";";
        ResultSet resultSet = session.getStatement().executeQuery(sql);

        if (resultSet.next()) {
            arr = new double[] { resultSet.getDouble(1), resultSet.getDouble(2), resultSet.getDouble(3) };
        }

        return arr;
    }

    private static String trimEnding(final String text, final String ending) {
        String textTrimmed;
        
        if (text.endsWith(ending)) {
            textTrimmed = text.substring(0, text.length() - 1);
        }
        else {
            textTrimmed = text;
        }
        
        return textTrimmed;
    }

    private static String composeHtmlTableStdHeader(final String title, final String itemName, final String conceptName) {
        return "<b>Resumen " + SLibUtils.textToHtml(title) + " (" + SLibUtils.textToHtml(itemName) + ")</b><br>"
                + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
                + "<tr>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(conceptName) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableStdHeaderSeasons(final String title, final String itemName, final String conceptName) {
        return "<b>Resumen " + SLibUtils.textToHtml(title) + " (" + SLibUtils.textToHtml(itemName) + ")</b><br>"
                + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
                + "<tr>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(conceptName) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableStdHeaderAlt(final String title, final String conceptName) {
        return "<b>Resumen " + SLibUtils.textToHtml(title) + ": </b><br>"
                + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
                + "<tr>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(conceptName) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " Convenc.") + "</b></td>"
                + "<td class='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " Orgánico") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " Total") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>"
                + "</tr>";
    }

    private static String composeHtmlTableStdHeaderSeasonsAlt(final String title, final String conceptName) {
        return "<b>Resumen " + SLibUtils.textToHtml(title) + ": </b><br>"
                + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
                + "<tr><td align='center'><b>" + SLibUtils.textToHtml(conceptName) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " Convenc.") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " Orgánico") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " Total") + "</b></td></tr>";
    }
    
    private static String composeHtmlTableStdRow(final String concept, final double weight, final double weightTotal) {
        return "<tr>"
                + "<td>" + SLibUtils.textToHtml(concept) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(weightTotal == 0 ? 0 : weight / weightTotal) + "</td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableStdRowSeasons(final String concept, final double weight) {
        return "<tr>"
                + "<td>" + SLibUtils.textToHtml(concept) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight) + "</td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableStdRowAlt(final String concept, final double weightConv, final double weightAlt, final double weightTot, final double weightTotal) {
        return "<tr>"
                + "<td>" + SLibUtils.textToHtml(concept) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weightConv) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weightAlt) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weightTot) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(weightTotal == 0 ? 0 : weightTot / weightTotal) + "</td>"
                + "</tr>";
    }

    private static String composeHtmlTableStdSeasonsAlt(final String concept, final double weightConv, final double weightAlt, final double weightTot) {
        return "<tr>"
                + "<td>" + SLibUtils.textToHtml(concept) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weightConv) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weightAlt) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weightTot) + "</td>"
                + "</tr>";
    }

    private static String composeHtmlTableStdFooter(final String title, final double weightTotal) {
        return "<tr>"
                + "<td><b>Total " + SLibUtils.textToHtml(title) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatPercentage2D.format(1.0) + "</b></td>"
                + "</tr>"
                + "</table>";
    }
    
    private static String composeHtmlTableStdFooterSeasons(final String title, final double weightTotal) {
        return "<tr>"
                + "<td><b>Total " + SLibUtils.textToHtml(title) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotal) + "</b></td>"
                + "</tr>"
                + "</table>";
    }
    
    private static String composeHtmlTableStdFooterAlt(final String title, final double weightTotalConv, final double weightTotalAlt, final double weightTotalTot) {
        return "<tr>"
                + "<td><b>Total " + SLibUtils.textToHtml(title) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotalConv) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotalAlt) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotalTot) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatPercentage2D.format(1.0) + "</b></td>"
                + "</tr>"
                + "</table>";
    }
    
    private static String composeHtmlTableStdFooterSeasonsAlt(final String title, final double weightTotalConv, final double weightTotalAlt, final double weightTotalTot) {
        return "<tr>"
                + "<td><b>Total " + SLibUtils.textToHtml(title) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotalConv) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotalAlt) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotalTot) + "</b></td>"
                + "</tr>"
                + "</table>";
    }
    
    private static String composeHtmlTableCompHeader(final String title, final String itemName, final String month, final int year, final String conceptName) {
        return "<b>Resumen y comparativa " + SLibUtils.textToHtml(title) + " (" + SLibUtils.textToHtml(itemName) + ")</b><br>"
                + "<div style='color: tomato;'><small>" + SLibUtils.textToHtml("(Se marcan en rojo los proveedores rezagados " 
                    + SLibUtils.DecimalFormatPercentage2D.format(LAGGING_PCT) + " o más respecto al mes del año anterior.)") + "</small></div>"
                + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
                + "<tr>"
                + "<td align='center' rowspan='2'><b>" + SLibUtils.textToHtml(conceptName) + "</b></td>"
                + "<td align='center' colspan='2'><b>" + SLibUtils.textToHtml(month + ". " + year) + "</b></td>"
                + "<td align='center' colspan='2'><b>" + SLibUtils.textToHtml(month + ". " + (year - 1)) + "</b></td>"
                + "<td align='center' colspan='2'><b>" + SLibUtils.textToHtml(month + ". " + (year - 2)) + "</b></td>"
                + "<td align='center' colspan='2'><b>" + SLibUtils.textToHtml(month + ". " + (year - 3)) + "</b></td>"
                + "</tr>"
                + "<tr>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " vs. mes act.") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " vs. mes act.") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " vs. mes act.") + "</b></td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableCompHeaderSeasons(final String title, final String itemName, final Date seasonStart, final Date seasonEnd, final String conceptName) {
        return "<b>Resumen y comparativa " + SLibUtils.textToHtml(title) + " (" + SLibUtils.textToHtml(itemName) + ")</b><br>"
                + "<div style='color: tomato;'><small>" + SLibUtils.textToHtml("(Se marcan en rojo los proveedores rezagados " 
                    + SLibUtils.DecimalFormatPercentage2D.format(LAGGING_PCT) + " o más respecto al mes del año anterior.)") + "</small></div>"
                + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
                + "<tr>"
                + "<td align='center' rowspan='2'><b>" + SLibUtils.textToHtml(conceptName) + "</b></td>"
                + "<td align='center' colspan='2'><b>" + SLibUtils.textToHtml(SLibTimeUtils.digestYear(seasonStart)[0] + "-" + SLibTimeUtils.digestYear(seasonEnd)[0]) + "</b></td>"
                + "<td align='center' colspan='2'><b>" + SLibUtils.textToHtml(SLibTimeUtils.digestYear(SLibTimeUtils.addDate(seasonStart, - 1, 0, 0))[0] + "-" + SLibTimeUtils.digestYear(SLibTimeUtils.addDate(seasonEnd, - 1, 0, 0))[0]) + "</b></td>"
                + "<td align='center' colspan='2'><b>" + SLibUtils.textToHtml(SLibTimeUtils.digestYear(SLibTimeUtils.addDate(seasonStart, - 2, 0, 0))[0] + "-" + SLibTimeUtils.digestYear(SLibTimeUtils.addDate(seasonEnd, - 2, 0, 0))[0]) + "</b></td>"
                + "<td align='center' colspan='2'><b>" + SLibUtils.textToHtml(SLibTimeUtils.digestYear(SLibTimeUtils.addDate(seasonStart, - 3, 0, 0))[0] + "-" + SLibTimeUtils.digestYear(SLibTimeUtils.addDate(seasonEnd, - 3, 0, 0))[0]) + "</b></td>"
                + "</tr>"
                + "<tr>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " vs. temp. act.") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " vs. temp. act.") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " vs. temp. act.") + "</b></td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableCompRow(final String concept, final double weight, final double weightTotal, double weight1Y, double weight2Y, double weight3Y) {
        return "<tr>"
                + "<td" + ((weight1Y - weight) / weight1Y >= LAGGING_PCT ? " style='background-color: tomato;'" : "") + ">" + SLibUtils.textToHtml(concept) + "</td>"
                + "<td align='right'" + ((weight1Y - weight) / weight1Y >= LAGGING_PCT ? " style='background-color: tomato;'" : "") + ">" + SLibUtils.DecimalFormatValue2D.format(weight) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(weightTotal == 0 ? 0 : weight / weightTotal) + "</td>"
                + "<td align='right'>" + DecimalFormatNegativeValue2D.format(weight1Y) + "</td>"
                + "<td align='right'>" + DecimalFormatNegativeValue2D.format(weight - weight1Y) + "</td>"
                + "<td align='right'>" + DecimalFormatNegativeValue2D.format(weight2Y) + "</td>"
                + "<td align='right'>" + DecimalFormatNegativeValue2D.format(weight - weight2Y) + "</td>"
                + "<td align='right'>" + DecimalFormatNegativeValue2D.format(weight3Y) + "</td>"
                + "<td align='right'>" + DecimalFormatNegativeValue2D.format(weight - weight3Y) + "</td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableCompFooter(final String title, final double weightTotal, final double weightTotal1Y, final double diffTotal1Y, final double weightTotal2Y, final double diffTotal2Y, final double weightTotal3Y, final double diffTotal3Y) {
        return "<tr>"
                + "<td><b>Total " + SLibUtils.textToHtml(title) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatPercentage2D.format(1.0) + "</b></td>"
                + "<td align='right'><b>" + DecimalFormatNegativeValue2D.format(weightTotal1Y) + "</b></td>"
                + "<td align='right'><b>" + DecimalFormatNegativeValue2D.format(diffTotal1Y) + "</b></td>"
                + "<td align='right'><b>" + DecimalFormatNegativeValue2D.format(weightTotal2Y) + "</b></td>"
                + "<td align='right'><b>" + DecimalFormatNegativeValue2D.format(diffTotal2Y) + "</b></td>"
                + "<td align='right'><b>" + DecimalFormatNegativeValue2D.format(weightTotal3Y) + "</b></td>"
                + "<td align='right'><b>" + DecimalFormatNegativeValue2D.format(diffTotal3Y) + "</b></td>"
                + "</tr>"
                + "</table>";
    }
    
    /**
     * Compose an HTML summary of all receptions from requested item.
     * @param session GUI session.
     * @param itemId Item ID.
     * @param seasonFirstMonth Season first month (1 = January; 0 = item's start month.)
     * @param monthFirstDay Month first day (0 | 1 = 1st. of start month; > 1 = nth of previous month.)
     * @param date Date.
     * @param reportMode Report mode (SCliMailerReportFruitsStdSummary.REP_MODE_...)
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @return HTML snippet.
     * @throws Exception 
     */
    public static String composeHtmlSummaryItem(final SGuiSession session, final int itemId, final int seasonFirstMonth, final int monthFirstDay, final Date date, final int reportMode, final int ticketOrigin, final int ticketDestination) throws Exception {
        // ---------------------------------------------------------------------
        // REPORT REPORT PREPARATION ///////////////////////////////////////////
        // ---------------------------------------------------------------------

        String sql;
        
        // retrieve item:
        
        SDbItem item = new SDbItem();
        item.read(session, new int[] { itemId }); // read this way due to session is moduleless
        String itemName = item.getName().toLowerCase();

        // retrieve list of all reporting groups:

        sql = "SELECT id_rep_grp, name, sort "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CU_REP_GRP) + " AS r "
                + "ORDER BY name, sort, id_rep_grp ";

        Statement repGroupStatement = session.getDatabase().getConnection().createStatement(); // a new statement to prevent other result sets from being closed
        ResultSet repGroupResultSet = repGroupStatement.executeQuery(sql);

        // retrieve list of all scales:

        sql = "SELECT id_sca, name, b_def "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " "
                + "ORDER BY name, id_sca ";

        Statement scaleStatement = session.getDatabase().getConnection().createStatement(); // a new statement to prevent other result sets from being closed
        ResultSet scaleResultSet = scaleStatement.executeQuery(sql);
        
        // ---------------------------------------------------------------------
        // REPORT PROCESSING ///////////////////////////////////////////////////
        // ---------------------------------------------------------------------

        Date dateStart;
        Date dateEnd;
        double weight;
        double weightTotal;
        double weight1Y;
        double weightTotal1Y;
        double weight2Y;
        double weightTotal2Y;
        double weight3Y;
        double weightTotal3Y;
        String section;
        String[] months = SLibTimeUtils.createMonthsOfYear(Locale.getDefault(), Calendar.SHORT);
        DateProps dateProps = new DateProps(session, date, SCliConsts.FRUIT_FIRST_YEAR, seasonFirstMonth, monthFirstDay, SCliConsts.FRUIT_BY_OP_CALENDARS, itemId);
        
        String html = "";
        
        // ---------------------------------------------------------------------
        // SECTION 1. Current day:
        // ---------------------------------------------------------------------

        dateStart = dateProps.Today;
        dateEnd = dateProps.Today;
        section = dateProps.composeTodayDescrip();
        weightTotal = obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, ticketOrigin, ticketDestination);

        // SECTION 1.1. Current day summary by reporting group:

        html += composeHtmlTableStdHeader(section, itemName, "Proveedor");

        // compute reporting groups:

        while (repGroupResultSet.next()) { // first reading, cursor before first row
            weight = obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
            if (weight != 0) {
                html += composeHtmlTableStdRow(repGroupResultSet.getString("name"), weight, weightTotal);
            }
        }

        html += composeHtmlTableStdFooter(section, weightTotal);

        // SECTION 1.2. Current day summary by scale:

        int scaleRows = 0;
        boolean scaleForceRendering = false;
        String scaleHtml = composeHtmlTableStdHeader(section, itemName, "Báscula");

        // compute scales:

        while (scaleResultSet.next()) { // first reading, cursor is before first row!
            weight = obtainWeightDestinyByScale(session, itemId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), ticketOrigin, ticketDestination);
            
            if (weight != 0) {
                scaleHtml += composeHtmlTableStdRow(scaleResultSet.getString("name"), weight, weightTotal);
                scaleRows++;
                if (!scaleForceRendering && !scaleResultSet.getBoolean("b_def")) {
                    scaleForceRendering = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooter(section, weightTotal);

        if (scaleForceRendering || scaleRows > 1) {
            html += "<br>";
            html += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }
        
        html += "<br>";

        // ---------------------------------------------------------------------
        // SECTION 2. Current month:
        // ---------------------------------------------------------------------

        dateStart = dateProps.OpMonthStart;
        dateEnd = dateProps.OpMonthEnd;
        section = dateProps.composeOpMonthDescrip();
        weightTotal = obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, ticketOrigin, ticketDestination);

        // SECTION 2.1. Current month summary by reporting group:

        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        if (reportMode == SCliMailerReportFruitsStdSummary.REP_MODE_STANDARD) {
            html += composeHtmlTableStdHeader(section, itemName, "Proveedor");

            while (repGroupResultSet.next()) {
                weight = obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                if (weight != 0) {
                    html += composeHtmlTableStdRow(repGroupResultSet.getString("name"), weight, weightTotal);
                }
            }

            html += composeHtmlTableStdFooter(section, weightTotal);
        }
        else {
            double diffTotal1Y = 0;
            double diffTotal2Y = 0;
            double diffTotal3y = 0;
            weightTotal1Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), 0, 0, ticketOrigin, ticketDestination);
            weightTotal2Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), 0, 0, ticketOrigin, ticketDestination);
            weightTotal3Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), 0, 0, ticketOrigin, ticketDestination);
            
            html += composeHtmlTableCompHeader(section, itemName, months[dateProps.OpMonth - 1], dateProps.OpYear, "Proveedor");
            
            while (repGroupResultSet.next()) {
                weight = obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                weight1Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                weight2Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                weight3Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                diffTotal1Y += weight - weight1Y;
                diffTotal2Y += weight - weight2Y;
                diffTotal3y += weight - weight3Y;
                if (weight != 0 || weight1Y != 0 || weight2Y != 0 || weight3Y != 0) {
                    html += composeHtmlTableCompRow(repGroupResultSet.getString("name"), weight, weightTotal, weight1Y, weight2Y, weight3Y);
                }
            }
            
            html += composeHtmlTableCompFooter(section, weightTotal, weightTotal1Y, diffTotal1Y, weightTotal2Y, diffTotal2Y, weightTotal3Y, diffTotal3y);
        }

        // SECTION 2.2: Current month summary by scale: 

        scaleRows = 0;
        scaleForceRendering = false;
        scaleHtml = composeHtmlTableStdHeader(section, itemName, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weight = obtainWeightDestinyByScale(session, itemId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), ticketOrigin, ticketDestination);
            
            if (weight != 0) {
                scaleHtml += composeHtmlTableStdRow(scaleResultSet.getString("name"), weight, weightTotal);
                scaleRows++;
                if (!scaleForceRendering && !scaleResultSet.getBoolean("b_def")) {
                    scaleForceRendering = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooter(section, weightTotal);

        if (scaleForceRendering || scaleRows > 1) {
            html += "<br>";
            html += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        if (dateProps.isOpMonthCustom()) {
            html += "<p style='font-size: 11px;'>" + SLibUtils.textToHtml("* Período " + trimEnding(section, "*") + ": " + dateProps.composeOpMonthPeriod() + ".") + "</p>";
        }
        else {
            html += "<br>";
        }
        
        // ---------------------------------------------------------------------
        // SECTION 3. Current season:
        // ---------------------------------------------------------------------

        // SECTION 3.1 Current season summary by reporting group:
        
        dateStart = dateProps.OpSeasonStart;
        dateEnd = dateProps.OpSeasonEnd;
        section = dateProps.composeOpSeasonDescrip();
        weightTotal = obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, ticketOrigin, ticketDestination);
        
        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        if (reportMode == SCliMailerReportFruitsStdSummary.REP_MODE_STANDARD) {
            html += composeHtmlTableStdHeader(section, itemName, "Proveedor");

            while (repGroupResultSet.next()) {
                weight = obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                if (weight != 0) {
                    html += composeHtmlTableStdRow(repGroupResultSet.getString("name"), weight, weightTotal);
                }
            }

            html += composeHtmlTableStdFooter(section, weightTotal);
        }
        else {
            double diffTotal1Y = 0;
            double diffTotal2Y = 0;
            double diffTotal3Y = 0;
            weightTotal1Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), 0, 0, ticketOrigin, ticketDestination);
            weightTotal2Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), 0, 0, ticketOrigin, ticketDestination);
            weightTotal3Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), 0, 0, ticketOrigin, ticketDestination);
            
            html += composeHtmlTableCompHeaderSeasons(section, itemName, dateStart, dateEnd, "Proveedor");
            
            while (repGroupResultSet.next()) {
                weight = obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                weight1Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                weight2Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                weight3Y = obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
                diffTotal1Y += weight - weight1Y;
                diffTotal2Y += weight - weight2Y;
                diffTotal3Y += weight - weight3Y;
                if (weight != 0 || weight1Y != 0 || weight2Y != 0 || weight3Y != 0) {
                    html += composeHtmlTableCompRow(repGroupResultSet.getString("name"), weight, weightTotal, weight1Y, weight2Y, weight3Y);
                }
            }
            
            html += composeHtmlTableCompFooter(section, weightTotal, weightTotal1Y, diffTotal1Y, weightTotal2Y, diffTotal2Y, weightTotal3Y, diffTotal3Y);
        }
        
        // SECTION 3.2 Current season summary by scale:

        scaleRows = 0;
        scaleForceRendering = false;
        scaleHtml = composeHtmlTableStdHeader(section, itemName, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weight = obtainWeightDestinyByScale(session, itemId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), ticketOrigin, ticketDestination);
            
            if (weight != 0) {
                scaleHtml += composeHtmlTableStdRow(scaleResultSet.getString("name"), weight, weightTotal);
                scaleRows++;
                if (!scaleForceRendering && !scaleResultSet.getBoolean("b_def")) {
                    scaleForceRendering = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooter(section, weightTotal);

        if (scaleForceRendering || scaleRows > 1) {
            html += "<br>";
            html += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        if (dateProps.isOpSeasonCustom()) {
            html += "<p style='font-size: 11px;'>" + SLibUtils.textToHtml("* Período " + trimEnding(section, "*") + ": " + dateProps.composeOpSeasonPeriod() + ".") + "</p>";
        }
        else {
            html += "<br>";
        }
        
        // ---------------------------------------------------------------------
        // SECTION 4. Season summary by month:
        // ---------------------------------------------------------------------

        dateStart = dateProps.OpSeasonStart;
        dateEnd = dateProps.OpSeasonEnd;
        section = dateProps.composeOpSeasonDescrip();
        weightTotal = obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, ticketOrigin, ticketDestination);

        html += composeHtmlTableStdHeader(section, itemName, "Mes");

        // compute session months:

        for (int i = 0; i < SLibTimeConsts.MONTHS; i++) {
            Date[] boundaries = dateProps.OpSeasonMonthsBoundaries.get(i);
            Date start = boundaries[0];
            Date end = boundaries[1];
            int[] monthDigestion = SLibTimeUtils.digestMonth(end);

            weight = obtainWeightDestinyByPeriod(session, itemId, start, end, 0, 0, ticketOrigin, ticketDestination);
            html += composeHtmlTableStdRow(monthDigestion[0] + " " + months[monthDigestion[1] - 1] + ".", weight, weightTotal);
        }

        html += composeHtmlTableStdFooter(section, weightTotal);

        if (dateProps.isOpSeasonCustom()) {
            String[] info = dateProps.composeOpSeasonHtmlInfo();
            html += "<p style='font-size: 11px;'>" + SLibUtils.textToHtml("* Info. " + trimEnding(section, "*") + ": " + info[0]) + (info.length == 1 ? "" : "<br>" + SLibUtils.textToHtml(info[1])) + "</p>";
        }
        else {
            html += "<br>";
        }
        
        // ---------------------------------------------------------------------
        // SECTION 5: History of all past seasons:
        // ---------------------------------------------------------------------
        
        weightTotal = 0;
        section = "temporadas" + (dateProps.isOpSeasonOrMonthCustom() ? "*" : "");

        html += composeHtmlTableStdHeaderSeasons(section, itemName, "Temporada");
        
        for (Date[] boundary : dateProps.OpSeasonsBoundaries) {
            Date start = boundary[0];
            Date end = boundary[1];
            String concept;
            
            if (!dateProps.isOpSeasonOrMonthCustom()) {
                concept = "" + SLibTimeUtils.digestYear(start)[0];
            }
            else {
                concept = "" + SLibTimeUtils.digestYear(start)[0] + "-" + SLibTimeUtils.digestYear(end)[0];
            }
            
            weight = obtainWeightDestinyByPeriod(session, itemId, start, end, 0, 0, ticketOrigin, ticketDestination);
            html += composeHtmlTableStdRowSeasons(concept, weight);
            
            weightTotal += weight;
        }
        
        html += composeHtmlTableStdFooterSeasons(section, weightTotal);
        
        if (dateProps.isOpSeasonOrMonthCustom()) {
            String[] info = dateProps.composeOpSeasonHtmlInfo();
            html += "<p style='font-size: 11px;'>" + SLibUtils.textToHtml("* Info. " + trimEnding(section, "*") + ": " + info[0]) + (info.length == 1 ? "" : "<br>" + SLibUtils.textToHtml(info[1])) + "</p>";
        }
        else {
            html += "<br>";
        }
        
        return html;
    }

    /**
     * Compose an HTML summary of all receptions from requested conventional and alternative items.
     * @param session GUI session.
     * @param itemConvId Conventional item ID.
     * @param itemAltId Alternative item alt ID.
     * @param seasonFirstMonth Season first month (1 = January; 0 = item's start month.)
     * @param monthFirstDay Month first day (0 | 1 = 1st. of start month; > 1 = nth of previous month.)
     * @param date Date.
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @return HTML snippet.
     * @throws Exception 
     */
    public static String composeHtmlSummaryItemAlt(final SGuiSession session, final int itemConvId, final int itemAltId, final int seasonFirstMonth, final int monthFirstDay, final Date date, final int ticketOrigin, final int ticketDestination) throws Exception {
        // ---------------------------------------------------------------------
        // REPORT REPORT PREPARATION ///////////////////////////////////////////
        // ---------------------------------------------------------------------

        String sql;
        
        // retrieve item:
        
        SDbItem item = new SDbItem();
        item.read(session, new int[] { itemConvId }); // read this way due to session is moduleless

        // retrieve list of all reporting groups:

        sql = "SELECT id_rep_grp, name, sort "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CU_REP_GRP) + " AS r "
                + "ORDER BY name, sort, id_rep_grp ";

        Statement repGroupStatement = session.getDatabase().getConnection().createStatement(); // a new statement to prevent other result sets from being closed
        ResultSet repGroupResultSet = repGroupStatement.executeQuery(sql);

        // retrieve list of all scales:

        sql = "SELECT id_sca, name, b_def "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " "
                + "ORDER BY name, id_sca ";

        Statement scaleStatement = session.getDatabase().getConnection().createStatement(); // a new statement to prevent other result sets from being closed
        ResultSet scaleResultSet = scaleStatement.executeQuery(sql);

        // ---------------------------------------------------------------------
        // REPORT PROCESSING ///////////////////////////////////////////////////
        // ---------------------------------------------------------------------

        Date dateStart;
        Date dateEnd;
        double[] weights;
        double[] weightsTotals;
        String section;
        String[] months = SLibTimeUtils.createMonthsOfYear(Locale.getDefault(), Calendar.SHORT);
        DateProps dateProps = new DateProps(session, date, SCliConsts.FRUIT_FIRST_YEAR, seasonFirstMonth, monthFirstDay, SCliConsts.FRUIT_BY_OP_CALENDARS, itemConvId);
        
        String html = "";
        
        // ---------------------------------------------------------------------
        // SECTION 1. Current day:
        // ---------------------------------------------------------------------

        dateStart = dateProps.Today;
        dateEnd = dateProps.Today;
        section = dateProps.composeTodayDescrip();
        weightsTotals = obtainWeightDestinyByPeriodAlt(session, itemConvId, itemAltId, dateStart, dateEnd, 0, 0, ticketOrigin, ticketDestination);

        // SECTION 1.1. Current day summary by reporting group:

        html += composeHtmlTableStdHeaderAlt(section, "Proveedor");

        // compute reporting groups:

        while (repGroupResultSet.next()) { // first reading, cursor before first row
            weights = obtainWeightDestinyByPeriodAlt(session, itemConvId, itemAltId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
            if (weights != null && weights[IDX_TOT] != 0.0) {
                html += composeHtmlTableStdRowAlt(repGroupResultSet.getString("name"), weights[IDX_CONV], weights[IDX_ALT], weights[IDX_TOT], weightsTotals[IDX_TOT]);
            }
        }

        html += composeHtmlTableStdFooterAlt(section, weightsTotals[IDX_CONV], weightsTotals[IDX_ALT], weightsTotals[IDX_TOT]);

        // SECTION 1.2. Current day summary by scale:

        int scaleRows = 0;
        boolean scaleForceRendering = false;
        String scaleHtml = composeHtmlTableStdHeaderAlt(section, "Báscula");

        // compute scales:

        while (scaleResultSet.next()) { // first reading, cursor before first row
            weights = obtainWeightDestinyByScaleAlt(session, itemConvId, itemAltId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), ticketOrigin, ticketDestination);
            
            if (weights != null && weights[IDX_TOT] != 0.0) {
                scaleHtml += composeHtmlTableStdRowAlt(scaleResultSet.getString("name"), weights[IDX_CONV], weights[IDX_ALT], weights[IDX_TOT], weightsTotals[IDX_TOT]);
                scaleRows++;
                if (!scaleForceRendering && !scaleResultSet.getBoolean("b_def")) {
                    scaleForceRendering = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooterAlt(section, weightsTotals[IDX_CONV], weightsTotals[IDX_ALT], weightsTotals[IDX_TOT]);

        if (scaleForceRendering || scaleRows > 1) {
            html += "<br>";
            html += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        html += "<br>";

        // ---------------------------------------------------------------------
        // SECTION 2. Current month:
        // ---------------------------------------------------------------------

        dateStart = dateProps.OpMonthStart;
        dateEnd = dateProps.OpMonthEnd;
        section = dateProps.composeOpMonthDescrip();
        weightsTotals = obtainWeightDestinyByPeriodAlt(session, itemConvId, itemAltId, dateStart, dateEnd, 0, 0, ticketOrigin, ticketDestination);

        // SECTION 2.1. Current month summary by reporting group:
        
        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        html += composeHtmlTableStdHeaderAlt(section, "Proveedor");

        while (repGroupResultSet.next()) {
            weights = obtainWeightDestinyByPeriodAlt(session, itemConvId, itemAltId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
            if (weights != null && weights[IDX_TOT] != 0.0) {
                html += composeHtmlTableStdRowAlt(repGroupResultSet.getString("name"), weights[IDX_CONV], weights[IDX_ALT], weights[IDX_TOT], weightsTotals[IDX_TOT]);
            }
        }

        html += composeHtmlTableStdFooterAlt(section, weightsTotals[IDX_CONV], weightsTotals[IDX_ALT], weightsTotals[IDX_TOT]);
        
        // SECTION 2.2: Current month summary by scale: 

        scaleRows = 0;
        scaleForceRendering = false;
        scaleHtml = composeHtmlTableStdHeaderAlt(section, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weights = obtainWeightDestinyByScaleAlt(session, itemConvId, itemAltId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), ticketOrigin, ticketDestination);
            
            if (weights != null && weights[IDX_TOT] != 0.0) {
                scaleHtml += composeHtmlTableStdRowAlt(scaleResultSet.getString("name"), weights[IDX_CONV], weights[IDX_ALT], weights[IDX_TOT], weightsTotals[IDX_TOT]);
                scaleRows++;
                if (!scaleForceRendering && !scaleResultSet.getBoolean("b_def")) {
                    scaleForceRendering = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooterAlt(section, weightsTotals[IDX_CONV], weightsTotals[IDX_ALT], weightsTotals[IDX_TOT]);

        if (scaleForceRendering || scaleRows > 1) {
            html += "<br>";
            html += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        if (dateProps.isOpMonthCustom()) {
            html += "<p style='font-size: 11px;'>" + SLibUtils.textToHtml("* Período " + trimEnding(section, "*") + ": " + dateProps.composeOpMonthPeriod() + ".") + "</p>";
        }
        else {
            html += "<br>";
        }
        
        // ---------------------------------------------------------------------
        // SECTION 3. Current season:
        // ---------------------------------------------------------------------

        // SECTION 3.1 Current season summary by reporting group:
        
        dateStart = dateProps.OpSeasonStart;
        dateEnd = dateProps.OpSeasonEnd;
        section = dateProps.composeOpSeasonDescrip();
        weightsTotals = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemConvId, itemAltId, dateStart, dateEnd, 0, 0, ticketOrigin, ticketDestination);

        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        html += composeHtmlTableStdHeaderAlt(section, "Proveedor");

        while (repGroupResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemConvId, itemAltId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, ticketOrigin, ticketDestination);
            if (weights != null && weights[IDX_TOT] != 0.0) {
                html += composeHtmlTableStdRowAlt(repGroupResultSet.getString("name"), weights[IDX_CONV], weights[IDX_ALT], weights[IDX_TOT], weightsTotals[IDX_TOT]);
            }
        }

        html += composeHtmlTableStdFooterAlt(section, weightsTotals[IDX_CONV], weightsTotals[IDX_ALT], weightsTotals[IDX_TOT]);

        // SECTION 3.2 Current season summary by scale:

        scaleRows = 0;
        scaleForceRendering = false;
        scaleHtml = composeHtmlTableStdHeaderAlt(section, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByScaleAlt(session, itemConvId, itemAltId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), ticketOrigin, ticketDestination);
            
            if (weights != null && weights[IDX_TOT] != 0.0) {
                scaleHtml += composeHtmlTableStdRowAlt(scaleResultSet.getString("name"), weights[IDX_CONV], weights[IDX_ALT], weights[IDX_TOT], weightsTotals[IDX_TOT]);
                scaleRows++;
                if (!scaleForceRendering && !scaleResultSet.getBoolean("b_def")) {
                    scaleForceRendering = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooterAlt(section, weightsTotals[IDX_CONV], weightsTotals[IDX_ALT], weightsTotals[IDX_TOT]);

        if (scaleForceRendering || scaleRows > 1) {
            html += "<br>";
            html += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        if (dateProps.isOpSeasonCustom()) {
            html += "<p style='font-size: 11px;'>" + SLibUtils.textToHtml("* Período " + trimEnding(section, "*") + ": " + dateProps.composeOpSeasonPeriod() + ".") + "</p>";
        }
        else {
            html += "<br>";
        }
        
        // ---------------------------------------------------------------------
        // SECTION 4. Season summary by month:
        // ---------------------------------------------------------------------

        dateStart = dateProps.OpSeasonStart;
        dateEnd = dateProps.OpSeasonEnd;
        section = dateProps.composeOpSeasonDescrip();
        weightsTotals = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemConvId, itemAltId, dateStart, dateEnd, 0, 0, ticketOrigin, ticketDestination);

        html += composeHtmlTableStdHeaderAlt(section, "Período");

        // compute months:

        for (int i = 0; i < SLibTimeConsts.MONTHS; i++) {
            Date[] boundaries = dateProps.OpSeasonMonthsBoundaries.get(i);
            Date start = boundaries[0];
            Date end = boundaries[1];
            int[] monthDigestion = SLibTimeUtils.digestMonth(end);

            weights = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemConvId, itemAltId, start, end, 0, 0, ticketOrigin, ticketDestination);
            html += composeHtmlTableStdRowAlt(monthDigestion[0] + " " + months[monthDigestion[1] - 1] + ".", weights[IDX_CONV], weights[IDX_ALT], weights[IDX_TOT], weightsTotals[IDX_TOT]);
        }

        html += composeHtmlTableStdFooterAlt(section, weightsTotals[IDX_CONV], weightsTotals[IDX_ALT], weightsTotals[IDX_TOT]);

        if (dateProps.isOpSeasonCustom()) {
            String[] info = dateProps.composeOpSeasonHtmlInfo();
            html += "<p style='font-size: 11px;'>" + SLibUtils.textToHtml("* Info. " + trimEnding(section, "*") + ": " + info[0]) + (info.length == 1 ? "" : "<br>" + SLibUtils.textToHtml(info[1])) + "</p>";
        }
        else {
            html += "<br>";
        }
        
        // ---------------------------------------------------------------------
        // SECTION 5: History of all past seasons:
        // ---------------------------------------------------------------------

        weightsTotals = new double[] { 0, 0, 0};
        section = "temporadas" + (dateProps.isOpSeasonOrMonthCustom() ? "*" : "");

        html += composeHtmlTableStdHeaderSeasonsAlt(section, "Temporada");
        
        for (Date[] boundary : dateProps.OpSeasonsBoundaries) {
            Date start = boundary[0];
            Date end = boundary[1];
            String concept;
            
            if (!dateProps.isOpSeasonOrMonthCustom()) {
                concept = "" + SLibTimeUtils.digestYear(start)[0];
            }
            else {
                concept = "" + SLibTimeUtils.digestYear(start)[0] + "-" + SLibTimeUtils.digestYear(end)[0];
            }
            
            weights = obtainWeightDestinyByPeriodAlt(session, itemConvId, itemAltId, start, end, 0, 0, ticketOrigin, ticketDestination);
            html += composeHtmlTableStdSeasonsAlt(concept, weights[IDX_CONV], weights[IDX_ALT], weights[IDX_TOT]);
            
            weightsTotals[IDX_CONV] += weights[IDX_CONV];
            weightsTotals[IDX_ALT] += weights[IDX_ALT];
            weightsTotals[IDX_TOT] += weights[IDX_TOT];
        }
        
        html += composeHtmlTableStdFooterSeasonsAlt(section, weightsTotals[IDX_CONV], weightsTotals[IDX_ALT], weightsTotals[IDX_TOT]);
        
        if (dateProps.isOpSeasonOrMonthCustom()) {
            String[] info = dateProps.composeOpSeasonHtmlInfo();
            html += "<p style='font-size: 11px;'>" + SLibUtils.textToHtml("* Info. " + trimEnding(section, "*") + ": " + info[0]) + (info.length == 1 ? "" : "<br>" + SLibUtils.textToHtml(info[1])) + "</p>";
        }
        else {
            html += "<br>";
        }
        
        return html;
    }
    
    /** RESPALDO REALIZADO EL 21/01/2026 DEL MÉTODO composeHtmlSummaryItem(). *
     * Compose an HTML summary of all receptions from requested item.
     * @param session GUI session.
     * @param itemId Item ID.
     * @param date Date.
     * @param repType
     * @param idTicOrig
     * @param idTicDest
     * @return HTML snippet.
     * @throws Exception 
     */
    @Deprecated
    public static String composeHtmlSummaryItem_20260116(final SGuiSession session, final int itemId, final Date date, final int repType, final int idTicOrig, final int idTicDest) throws Exception {
        // REPORT PREPARATION:

        double weight;
        double weightTotal;
        double weight1Y;
        double weightTotal1Y;
        double weight2Y;
        double weightTotal2Y;
        double weight3yAgo;
        double weightTotal3Y;
        Date dateStart;
        Date dateEnd;
        String sql;
        String section;
        String body = "";
        String[] months = SLibTimeUtils.createMonthsOfYear(Locale.getDefault(), Calendar.SHORT);
        
        SDbItem item = new SDbItem();
        item.read(session, new int[] { itemId }); // read this way due to session is moduleless
        String itemName = SLibUtils.textProperCase(item.getName());

        int[] curDate = SLibTimeUtils.digestDate(date);
        int curYear = curDate[0];
        int curMonth = curDate[1];
        
        // list of all reporting groups:

        sql = "SELECT id_rep_grp, name, sort "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CU_REP_GRP) + " AS r "
                + "ORDER BY name, sort, id_rep_grp ";

        Statement repGroupStatement = session.getDatabase().getConnection().createStatement(); // prevent other result sets from being closed
        ResultSet repGroupResultSet = repGroupStatement.executeQuery(sql);

        // list of all scales:

        sql = "SELECT id_sca, name, b_def "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " "
                + "ORDER BY name, id_sca ";

        Statement scaleStatement = session.getDatabase().getConnection().createStatement(); // prevent other result sets from being closed
        ResultSet scaleResultSet = scaleStatement.executeQuery(sql);

        // SECTION 1. Current day:

        dateStart = date;
        dateEnd = date;
        weightTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "día '" + SLibUtils.DateFormatDate.format(date) + "'";

        // SECTION 1.1. Current day summary by reporting group:

        body += composeHtmlTableStdHeader(section, itemName, "Proveedor");

        // compute reporting groups:

        while (repGroupResultSet.next()) { // first reading, cursor before first row
            weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
            if (weight != 0) {
                body += composeHtmlTableStdRow(repGroupResultSet.getString("name"), weight, weightTotal);
            }
        }

        body += composeHtmlTableStdFooter(section, weightTotal);

        // SECTION 1.2. Current day summary by scale:

        int scaleRows = 0;
        boolean scaleForce = false;
        String scaleHtml = composeHtmlTableStdHeader(section, itemName, "Báscula");

        // compute scales:

        while (scaleResultSet.next()) { // first reading, cursor before first row
            weight = SSomUtils.obtainWeightDestinyByScale(session, itemId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            if (weight != 0) {
                scaleHtml += composeHtmlTableStdRow(scaleResultSet.getString("name"), weight, weightTotal);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooter(section, weightTotal);

        if (scaleForce || scaleRows > 1) {
            body += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        // SECTION 2. Current month:

        dateStart = SLibTimeUtils.getBeginOfMonth(date);
        dateEnd = SLibTimeUtils.getEndOfMonth(date);
        weightTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "mes '" + months[curMonth - 1] + ". " + curYear + "'";

        // SECTION 2.1. Current month summary by reporting group:

        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        if (repType == SCliMailerReportFruitsStdSummary.REP_MODE_STANDARD) {
            body += composeHtmlTableStdHeader(section, itemName, "Proveedor");

            while (repGroupResultSet.next()) {
                weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                if (weight != 0) {
                    body += composeHtmlTableStdRow(repGroupResultSet.getString("name"), weight, weightTotal);
                }
            }

            body += composeHtmlTableStdFooter(section, weightTotal);
        }
        else {
            double diffTotal1Y = 0;
            double diffTotal2Y = 0;
            double diffTotal3Y = 0;
            weightTotal1Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), 0, 0, idTicOrig, idTicDest);
            weightTotal2Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), 0, 0, idTicOrig, idTicDest);
            weightTotal3Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), 0, 0, idTicOrig, idTicDest);
            
            body += composeHtmlTableCompHeader(section, itemName, months[curMonth - 1], curYear, "Proveedor");
            
            while (repGroupResultSet.next()) {
                weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight1Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight2Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight3yAgo = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                diffTotal1Y += weight - weight1Y;
                diffTotal2Y += weight - weight2Y;
                diffTotal3Y += weight - weight3yAgo;
                if (weight != 0 || weight1Y != 0 || weight2Y != 0 || weight3yAgo != 0) {
                    body += composeHtmlTableCompRow(repGroupResultSet.getString("name"), weight, weightTotal, weight1Y, weight2Y, weight3yAgo);
                }
            }
            
            body += composeHtmlTableCompFooter(section, weightTotal, weightTotal1Y, diffTotal1Y, weightTotal2Y, diffTotal2Y, weightTotal3Y, diffTotal3Y);
        }

        // SECTION 2.2: Current month summary by scale: 

        scaleRows = 0;
        scaleForce = false;
        scaleHtml = composeHtmlTableStdHeader(section, itemName, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weight = SSomUtils.obtainWeightDestinyByScale(session, itemId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            if (weight != 0) {
                scaleHtml += composeHtmlTableStdRow(scaleResultSet.getString("name"), weight, weightTotal);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooter(section, weightTotal);

        if (scaleForce || scaleRows > 1) {
            body += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        // SECTION 3. Current season:

        // Create season date range depending on item configuration:

        Date dateSeasonStart;
        Date dateSeasonEnd;

        if (curMonth >= item.getStartingSeasonMonth()) {
            dateSeasonStart = SLibTimeUtils.createDate(curYear, item.getStartingSeasonMonth(), 1);
            dateSeasonEnd = SLibTimeUtils.createDate(curYear + 1, item.getStartingSeasonMonth() - 1,
                    SLibTimeUtils.getMaxDayOfMonth(SLibTimeUtils.createDate(curYear + 1, item.getStartingSeasonMonth() - 1)));
        }
        else {
            dateSeasonStart = SLibTimeUtils.createDate(curYear - 1, item.getStartingSeasonMonth(), 1);
            dateSeasonEnd = SLibTimeUtils.createDate(curYear, item.getStartingSeasonMonth() - 1,
                    SLibTimeUtils.getMaxDayOfMonth(SLibTimeUtils.createDate(curYear, item.getStartingSeasonMonth() - 1)));
        }

        // SECTION 3.1 Current season summary by reporting group:
        
        dateStart = dateSeasonStart;
        dateEnd = dateSeasonEnd;
        weightTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "temporada";
        
        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        if (repType == SCliMailerReportFruitsStdSummary.REP_MODE_STANDARD) {
            body += composeHtmlTableStdHeader(section, itemName, "Proveedor");

            while (repGroupResultSet.next()) {
                weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                if (weight != 0) {
                    body += composeHtmlTableStdRow(repGroupResultSet.getString("name"), weight, weightTotal);
                }
            }

            body += composeHtmlTableStdFooter(section, weightTotal);
        }
        else {
            double diffTotal1Y = 0;
            double diffTotal2Y = 0;
            double diffTotal3Y = 0;
            weightTotal1Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), 0, 0, idTicOrig, idTicDest);
            weightTotal2Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), 0, 0, idTicOrig, idTicDest);
            weightTotal3Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), 0, 0, idTicOrig, idTicDest);
            
            body += composeHtmlTableCompHeaderSeasons(section, itemName, dateSeasonStart, dateSeasonEnd, "Proveedor");
            
            while (repGroupResultSet.next()) {
                weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight1Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight2Y = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight3yAgo = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                diffTotal1Y += weight - weight1Y;
                diffTotal2Y += weight - weight2Y;
                diffTotal3Y += weight - weight3yAgo;
                if (weight != 0 || weight1Y != 0 || weight2Y != 0 || weight3yAgo != 0) {
                    body += composeHtmlTableCompRow(repGroupResultSet.getString("name"), weight, weightTotal, weight1Y, weight2Y, weight3yAgo);
                }
            }
            
            body += composeHtmlTableCompFooter(section, weightTotal, weightTotal1Y, diffTotal1Y, weightTotal2Y, diffTotal2Y, weightTotal3Y, diffTotal3Y);
        }
        
        // SECTION 3.2 Current season summary by scale:

        scaleRows = 0;
        scaleForce = false;
        scaleHtml = composeHtmlTableStdHeader(section, itemName, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weight = SSomUtils.obtainWeightDestinyByScale(session, itemId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            if (weight != 0) {
                scaleHtml += composeHtmlTableStdRow(scaleResultSet.getString("name"), weight, weightTotal);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooter(section, weightTotal);

        if (scaleForce || scaleRows > 1) {
            body += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        // SECTION 4. Season summary by month:

        dateStart = dateSeasonStart;
        dateEnd = dateSeasonEnd;
        weightTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "temporada";

        body += composeHtmlTableStdHeader(section, itemName, "Período");

        // compute months:

        int year = SLibTimeUtils.digestYear(dateStart)[0];
        int month = item.getStartingSeasonMonth();

        for (int i = 0; i < SLibTimeConsts.MONTHS; i++) {
            if (month > SLibTimeConsts.MONTHS) {
                month = 1;  // January
                year++;
            }

            dateStart = SLibTimeUtils.createDate(year, month);
            dateEnd = SLibTimeUtils.getEndOfMonth(dateStart);

            weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
            body += composeHtmlTableStdRow("" + year + " " + months[month - 1] + ".", weight, weightTotal);
            month++;
        }

        body += composeHtmlTableStdFooter(section, weightTotal);

        // SECTION 5: History of all past seasons:

        section = "temporadas";

        body += "<b>Resumen " + section + ": " + itemName + "</b><br>"
            + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
            + "<tr><td align='center'><b>" + SLibUtils.textToHtml("Temporada") + "</b></td><td align='center'><b>" + SSomConsts.KG + "</b></td></tr>";

        sql = "SELECT IF(i.sta_seas_mon = 1, YEAR(t.dt), IF(MONTH(t.dt) >= i.sta_seas_mon, CONCAT(YEAR(t.dt), '-', YEAR(t.dt) + 1), CONCAT(YEAR(t.dt) - 1, '-', YEAR(t.dt)))) AS _season, "
                + "SUM(t.wei_des_net_r) AS _weight "
                + "FROM s_tic AS t "
                + "INNER JOIN su_item AS i ON t.fk_item = i.id_item "
                + "WHERE NOT t.b_del AND t.b_tar AND t.fk_item = " + itemId + " " 
                + (idTicOrig == 0 ? "" : "AND t.fk_tic_orig = " + idTicOrig + " ") 
                + (idTicDest == 0 ? "" : "AND t.fk_tic_dest = " + idTicDest + " ") 
                + "GROUP BY _season "
                + "ORDER BY _season; ";

        Statement historyStatement = session.getDatabase().getConnection().createStatement(); // prevent other result sets from being closed
        ResultSet historyResultSet = historyStatement.executeQuery(sql);
        while (historyResultSet.next()) {
            body += "<tr>"
                    + "<td>" + historyResultSet.getString("_season") + "</td>"
                    + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(historyResultSet.getDouble("_weight")) + "</td>"
                    + "</tr>";
        }

        body += "</table><br>";
        
        return body;
    }

    /** RESPALDO REALIZADO EL 21/01/2026 DEL MÉTODO composeHtmlSummaryItemAlternative(). *
     * Compose an HTML summary of all receptions from requested item.
     * @param session GUI session.
     * @param itemId Item ID.
     * @param itemAltId Item alt ID.
     * @param date Date.
     * @param idTicOrig
     * @param idTicDest
     * @return HTML snippet.
     * @throws Exception 
     */
    @Deprecated
    public static String composeHtmlSummaryItemAlternative_20260116(final SGuiSession session, final int itemId, final int itemAltId, final Date date, final int idTicOrig, final int idTicDest) throws Exception {
        // REPORT PREPARATION:

        double[] weights;
        double[] weightsTots;
        Date dateStart;
        Date dateEnd;
        String sql;
        String section;
        String body = "";
        String[] months = SLibTimeUtils.createMonthsOfYear(Locale.getDefault(), Calendar.SHORT);
        
        SDbItem item = new SDbItem();
        item.read(session, new int[] { itemId }); // read this way due to session is moduleless
        String itemName = SLibUtils.textProperCase(item.getName());

        int[] curDate = SLibTimeUtils.digestDate(date);
        int curYear = curDate[0];
        int curMonth = curDate[1];
        
        // list of all reporting groups:

        sql = "SELECT id_rep_grp, name, sort "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CU_REP_GRP) + " AS r "
                + "ORDER BY name, sort, id_rep_grp ";

        Statement repGroupStatement = session.getDatabase().getConnection().createStatement(); // prevent other result sets from being closed
        ResultSet repGroupResultSet = repGroupStatement.executeQuery(sql);

        // list of all scales:

        sql = "SELECT id_sca, name, b_def "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " "
                + "ORDER BY name, id_sca ";

        Statement scaleStatement = session.getDatabase().getConnection().createStatement(); // prevent other result sets from being closed
        ResultSet scaleResultSet = scaleStatement.executeQuery(sql);

        // SECTION 1. Current day:

        dateStart = date;
        dateEnd = date;
        weightsTots = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "día '" + SLibUtils.DateFormatDate.format(date) + "'";

        // SECTION 1.1. Current day summary by reporting group:

        body += composeHtmlTableStdHeaderAlt(section, "Proveedor");

        // compute reporting groups:

        while (repGroupResultSet.next()) { // first reading, cursor before first row
            weights = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemId, itemAltId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
            if (weights != null && weights[2] != 0.0) {
                body += composeHtmlTableStdRowAlt(repGroupResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
            }
        }

        body += composeHtmlTableStdFooterAlt(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        // SECTION 1.2. Current day summary by scale:

        int scaleRows = 0;
        boolean scaleForce = false;
        String scaleHtml = composeHtmlTableStdHeaderAlt(section, "Báscula");

        // compute scales:

        while (scaleResultSet.next()) { // first reading, cursor before first row
            weights = SSomUtils.obtainWeightDestinyByScaleAlt(session, itemId, itemAltId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            if (weights != null && weights[2] != 0.0) {
                scaleHtml += composeHtmlTableStdRowAlt(scaleResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooterAlt(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        if (scaleForce || scaleRows > 1) {
            body += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        // SECTION 2. Current month:

        dateStart = SLibTimeUtils.getBeginOfMonth(date);
        dateEnd = SLibTimeUtils.getEndOfMonth(date);
        weightsTots = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "mes '" + months[curMonth - 1] + ". " + curYear + "'";

        // SECTION 2.1. Current month summary by reporting group:
        
        body += composeHtmlTableStdHeaderAlt(section, "Proveedor");

        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        while (repGroupResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemId, itemAltId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);

            if (weights != null && weights[2] != 0.0) {
                body += composeHtmlTableStdRowAlt(repGroupResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
            }
        }

        body += composeHtmlTableStdFooterAlt(section, weightsTots[0], weightsTots[1], weightsTots[2]);
        
        // SECTION 2.2: Current month summary by scale: 

        scaleRows = 0;
        scaleForce = false;
        scaleHtml = composeHtmlTableStdHeaderAlt(section, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByScaleAlt(session, itemId, itemAltId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            
            if (weights != null && weights[2] != 0.0) {
                scaleHtml += composeHtmlTableStdRowAlt(scaleResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooterAlt(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        if (scaleForce || scaleRows > 1) {
            body += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        // SECTION 3. Current season:

        // Create season date range depending on item configuration:

        Date dateSeasonStart;
        Date dateSeasonEnd;

        if (curMonth >= item.getStartingSeasonMonth()) {
            dateSeasonStart = SLibTimeUtils.createDate(curYear, item.getStartingSeasonMonth(), 1);
            dateSeasonEnd = SLibTimeUtils.createDate(curYear + 1, item.getStartingSeasonMonth() - 1,
                    SLibTimeUtils.getMaxDayOfMonth(SLibTimeUtils.createDate(curYear + 1, item.getStartingSeasonMonth() - 1)));
        }
        else {
            dateSeasonStart = SLibTimeUtils.createDate(curYear - 1, item.getStartingSeasonMonth(), 1);
            dateSeasonEnd = SLibTimeUtils.createDate(curYear, item.getStartingSeasonMonth() - 1,
                    SLibTimeUtils.getMaxDayOfMonth(SLibTimeUtils.createDate(curYear, item.getStartingSeasonMonth() - 1)));
        }

        dateStart = dateSeasonStart;
        dateEnd = dateSeasonEnd;
        weightsTots = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "temporada";

        // SECTION 3.1 Current season summary by reporting group:

        body += composeHtmlTableStdHeaderAlt(section, "Proveedor");

        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        while (repGroupResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemId, itemAltId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
            if (weights != null && weights[2] != 0.0) {
                body += composeHtmlTableStdRowAlt(repGroupResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
            }
        }

        body += composeHtmlTableStdFooterAlt(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        // SECTION 3.2 Current season summary by scale:

        scaleRows = 0;
        scaleForce = false;
        scaleHtml = composeHtmlTableStdHeaderAlt(section, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByScaleAlt(session, itemId, itemAltId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            
            if (weights != null && weights[2] != 0.0) {
                scaleHtml += composeHtmlTableStdRowAlt(scaleResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableStdFooterAlt(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        if (scaleForce || scaleRows > 1) {
            body += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        // SECTION 4. Season summary by month:

        dateStart = dateSeasonStart;
        dateEnd = dateSeasonEnd;
        weightsTots = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "temporada";

        body += composeHtmlTableStdHeaderAlt(section, "Período");

        // compute months:

        int year = SLibTimeUtils.digestYear(dateStart)[0];
        int month = item.getStartingSeasonMonth();

        for (int i = 0; i < SLibTimeConsts.MONTHS; i++) {
            if (month > SLibTimeConsts.MONTHS) {
                month = 1;  // January
                year++;
            }

            dateStart = SLibTimeUtils.createDate(year, month);
            dateEnd = SLibTimeUtils.getEndOfMonth(dateStart);

            weights = SSomUtils.obtainWeightDestinyByPeriodAlt(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
            body += composeHtmlTableStdRowAlt("" + year + " " + months[month - 1] + ".", weights[0], weights[1], weights[2], weightsTots[2]);
            month++;
        }

        body += composeHtmlTableStdFooterAlt(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        // SECTION 5: History of all past seasons:

        section = "temporadas";

        body += "<b>Resumen " + section + ": </b><br>"
            + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
            + "<tr><td align='center'><b>" + SLibUtils.textToHtml("Temporada") + "</b></td>"
                + "<td align='center'><b>" + SSomConsts.KG + " conv.</b></td>"
                + "<td align='center'><b>" + SSomConsts.KG + " org.</b></td>"
                + "<td align='center'><b>" + SSomConsts.KG + " total</b></td></tr>";

        sql = "SELECT IF(i.sta_seas_mon = 1, YEAR(t.dt), IF(MONTH(t.dt) >= i.sta_seas_mon, CONCAT(YEAR(t.dt), '-', YEAR(t.dt) + 1), CONCAT(YEAR(t.dt) - 1, '-', YEAR(t.dt)))) AS _season, "
                + "SUM(IF(t.b_alt = 0, t.wei_des_net_r, 0)) AS _tic, "
                + "SUM(IF(t.b_alt = 1, t.wei_des_net_r, 0)) AS _alt, "
                + "SUM(t.wei_des_net_r) AS _tot "
                + "FROM ("
                + "SELECT *, dt fecha FROM "
                + SModConsts.TablesMap.get(SModConsts.S_TIC) + " " 
                + "WHERE NOT b_del AND b_tar AND NOT b_alt AND fk_item = " + itemId + " "
                + "UNION "
                + "SELECT a.*, t.dt fecha FROM "
                + SModConsts.TablesMap.get(SModConsts.S_ALT_TIC) + " a " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " t ON a.id_tic = t.id_tic " 
                + "WHERE NOT a.b_del AND a.b_alt AND a.fk_item = " + itemAltId + " " + ") AS t "
                + "INNER JOIN SU_ITEM AS i ON t.fk_item = i.id_item "
                + "WHERE NOT t.b_del " 
                + (idTicOrig == 0 ? "" : "AND t.fk_tic_orig = " + idTicOrig + " ") 
                + (idTicDest == 0 ? "" : "AND t.fk_tic_dest = " + idTicDest + " ") 
                + "AND t.fecha >= '2023-" + item.getStartingSeasonMonth() + "-01' " // año inicio som organico e inicio de temporada ítem
                + "GROUP BY _season "
                + "ORDER BY _season; ";

        Statement historyStatement = session.getDatabase().getConnection().createStatement(); // prevent other result sets from being closed
        ResultSet historyResultSet = historyStatement.executeQuery(sql);
        while (historyResultSet.next()) {
            body += "<tr>"
                    + "<td>" + historyResultSet.getString("_season") + "</td>"
                    + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(historyResultSet.getDouble("_tic")) + "</td>"
                    + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(historyResultSet.getDouble("_alt")) + "</td>"
                    + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(historyResultSet.getDouble("_tot")) + "</td>"
                    + "</tr>";
        }

        body += "</table><br>";
        
        return body;
    }
    
    /**
     * Actualiza los boletos con el origen insumo del proveedor.
     * @param session GUI session.
     * @param producerId Producer ID.
     * @param inputSourceId Input source ID.
     * @throws Exception 
     */
    public static void updateTicketsInputSource(final SGuiSession session, final int producerId, final int inputSourceId) throws Exception {
        String sql = "UPDATE s_tic SET fk_inp_src = " + inputSourceId + " WHERE fk_prod = " + producerId + ";";
        session.getStatement().execute(sql);
    }
    
    /**
     * Devuelve los codigos en formato de cadena de los ítems que seran mostrados en el sistema alterno.
     * @param session
     * @return
     * @throws Exception 
     */
    public static String getAlternativeItemCodes(final SGuiSession session) throws Exception {
        String sql = "SELECT alt_item_ids FROM " + SModConsts.TablesMap.get(SModConsts.CU_CO);
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            return resultSet.getString(1).replace(";", ",");
        }
        return "";
    }
    
    /**
     * Devuelve los codigos en formato de cadena de los ítems que se puede modificar el peso en los boletos del sistema alterno.
     * @param session
     * @return
     * @throws Exception 
     */
    public static String getAlternativeItemWeigthChangeCodes(final SGuiSession session) throws Exception {
        String sql = "SELECT alt_item_ids_wei_chg FROM " + SModConsts.TablesMap.get(SModConsts.CU_CO);
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "";
    }
    
    /**
     * Clase de utilerías para obtención de atributos relativos a una fecha, para la generación de reportes de recepción de insumos en báscula.
     */
    public static class DateProps {
        
        public Date Today;
        
        private int OldestYear;
        private int SeasonFirstMonth;
        private int MonthFirstDay;
        private boolean UseOpCalendars;
        private int TargetItemId;
        
        private String[] MonthsShort;
        private String[] MonthsLong;
        
        /** Current operating year. */
        public int OpYear;
        /** Current operating month. */
        public int OpMonth;
        
        /** Start of current operating month. */
        public Date OpMonthStart;
        /** End of current operating month. */
        public Date OpMonthEnd;
        
        /** Start of current operating season. */
        public Date OpSeasonStart;
        /** End of current operating season. */
        public Date OpSeasonEnd;
        
        /** Boundaries (starting and ending dates) of twelve months of season. */
        public ArrayList<Date[]> OpSeasonMonthsBoundaries;
        
        /** Boundaries (starting and ending dates) of seasons from oldest year up to current operating year. */
        public ArrayList<Date[]> OpSeasonsBoundaries;
        
        /**
         * Create a new instance of DatesProps.
         * @param session GUI session.
         * @param today Today.
         * @param oldestYear Oldest year for date properties.
         * @param seasonFirstMonth Season first month (1 = January; 0 = item's start month.)
         * @param monthFirstDay Month first day (0 | 1 = 1st. of start month; > 1 = nth of previous month.)
         * @param useOpCalendars Flag to use operative calendars.
         * @param targetItemId Target item ID.
         * @throws Exception 
         */
        public DateProps(final SGuiSession session, final Date today, final int oldestYear, final int seasonFirstMonth, final int monthFirstDay, final boolean useOpCalendars, final int targetItemId) throws Exception {
            // validate arguments:
            
            if (seasonFirstMonth < SLibTimeConsts.MONTH_MIN || seasonFirstMonth > SLibTimeConsts.MONTH_MAX) {
                throw new Exception("El mes de inicio de temporada, " + seasonFirstMonth + ", debe estar entre " + SLibTimeConsts.MONTH_MIN + " y " + SLibTimeConsts.MONTH_MAX + ".");
            }
            
            if (monthFirstDay < 1 || monthFirstDay > SCliConsts.MAX_MONTH_START_DAY) {
                throw new Exception("El día de inicio de mes, " + monthFirstDay + ", debe estar entre " + 1 + " y " + SCliConsts.MAX_MONTH_START_DAY + ".");
            }
            
            // setup date properties processing:
            
            Today = today;
            
            OldestYear = oldestYear;
            SeasonFirstMonth = seasonFirstMonth;
            MonthFirstDay = monthFirstDay;
            UseOpCalendars = useOpCalendars;
            TargetItemId = targetItemId;
            
            // months descriptions:
            
            MonthsShort = SLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT);
            MonthsLong = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
            
            // process date properties:
            
            if (UseOpCalendars) {
                // set properties based on matching operating calendar:
                
                SDbOpCalendarYear todayOpYear;
                SDbOpCalendarYearMonth todayOpMonth;
                int[] todayOpYearKey = SOpCalendarUtils.getOpCalendarYearKey(session, TargetItemId, Today);
                
                if (todayOpYearKey == null) {
                    throw new Exception("No se encontró el año operativo.");
                }
                
                todayOpYear = new SDbOpCalendarYear();
                todayOpYear.read(session, todayOpYearKey); // registries must be read directly, not from available dummy session!
                
                todayOpMonth = todayOpYear.getChildMonthByDate(Today);
                
                if (todayOpMonth == null) {
                    throw new Exception("No se encontró el mes operativo.");
                }
                
                int[] monthEndDigestion = SLibTimeUtils.digestMonth(todayOpMonth.getMonthEnd()); // 0 = year, 1 = month
                int monthEndYear = monthEndDigestion[0];
                int monthEndMonth = monthEndDigestion[1];

                OpYear = monthEndYear;
                OpMonth = monthEndMonth;

                OpMonthStart = todayOpMonth.getMonthStart();
                OpMonthEnd = todayOpMonth.getMonthEnd();
                
                if (OldestYear > OpYear) {
                    throw new Exception("El año más antiguo, " + OldestYear + ", no puede ser mayor al año actual, " + OpYear + ".");
                }

                OpSeasonStart = todayOpYear.getYearStart();
                OpSeasonEnd = todayOpYear.getYearEnd();

                // operating season months boundaries:

                OpSeasonMonthsBoundaries = new ArrayList<>();

                for (SDbOpCalendarYearMonth month : todayOpYear.getChildMonths()) {
                    OpSeasonMonthsBoundaries.add(month.getPeriod());
                }
                
                // operating seasons boundaries:

                OpSeasonsBoundaries = new ArrayList<>();

                for (int year = OldestYear; year < todayOpYear.getPkYearId(); year++) {
                    SDbOpCalendarYear ocy = new SDbOpCalendarYear();
                    ocy.read(session, new int[] { todayOpYear.getPkOpCalendarId(), year }); // registries must be read directly, not from available dummy session!
                    OpSeasonsBoundaries.add(ocy.getPeriod());
                }
                
                OpSeasonsBoundaries.add(todayOpYear.getPeriod());
            }
            else {
                // set properties by hand:
                
                int[] todayDigestion = SLibTimeUtils.digestDate(today); // 0 = year, 1 = month, 2 = day
                int todayYear = todayDigestion[0];
                int todayMonth = todayDigestion[1];
                int todayDay = todayDigestion[2];

                if (MonthFirstDay == 1) {
                    // month starts on 1st.

                    OpYear = todayYear;
                    OpMonth = todayMonth;

                    OpMonthStart = SLibTimeUtils.createDate(OpYear, OpMonth, 1);
                    OpMonthEnd = SLibTimeUtils.getEndOfMonth(OpMonthStart);
                }
                else {
                    // month starts after 1st.

                    if (todayDay < MonthFirstDay) {
                        // today belongs to "current" month:

                        OpYear = todayYear;
                        OpMonth = todayMonth;

                        if (OpMonth > SLibTimeConsts.MONTH_JAN) {
                            OpMonthStart = SLibTimeUtils.createDate(OpYear, OpMonth - 1, MonthFirstDay);
                        }
                        else {
                            OpMonthStart = SLibTimeUtils.createDate(OpYear - 1, SLibTimeConsts.MONTH_DEC, MonthFirstDay);
                        }
                    }
                    else {
                        // today belongs to "next" month:

                        if (todayMonth < SLibTimeConsts.MONTH_DEC) {
                            OpYear = todayYear;
                            OpMonth = todayMonth + 1;
                        }
                        else {
                            OpYear = todayYear + 1;
                            OpMonth = SLibTimeConsts.MONTH_JAN;
                        }

                        OpMonthStart = SLibTimeUtils.createDate(todayYear, todayMonth, MonthFirstDay);
                    }

                    OpMonthEnd = SLibTimeUtils.createDate(OpYear, OpMonth, MonthFirstDay - 1);
                }

                if (OldestYear > OpYear) {
                    throw new Exception("El año más antiguo, " + OldestYear + ", no puede ser mayor al año actual, " + OpYear + ".");
                }

                int firstSeasonYear = OpMonth >= SeasonFirstMonth ? OpYear : OpYear - 1;
                int firstSeasonMonth = SeasonFirstMonth - (MonthFirstDay == 1 ? 0 : 1);

                OpSeasonStart = SLibTimeUtils.createDate(firstSeasonYear, firstSeasonMonth, MonthFirstDay);
                OpSeasonEnd = SLibTimeUtils.addDate(OpSeasonStart, 1, MonthFirstDay == 1 ? 0 : 1, -1);

                // operating season months boundaries:

                OpSeasonMonthsBoundaries = new ArrayList<>();

                if (MonthFirstDay == 1) {
                    // month starts on 1st.

                    int year = firstSeasonYear;
                    int month = firstSeasonMonth;

                    for (int i = 0; i < SLibTimeConsts.MONTHS; i++, month++) {
                        if (month > SLibTimeConsts.MONTH_DEC) {
                            month = SLibTimeConsts.MONTH_JAN;
                            year++;
                        }

                        Date start = SLibTimeUtils.createDate(year, month, 1);
                        Date end = SLibTimeUtils.getEndOfMonth(start);

                        OpSeasonMonthsBoundaries.add(new Date[] { start, end });
                    }
                }
                else {
                    // month starts after 1st.

                    Date start = null;
                    Date end = null;

                    for (int i = 0; i < SLibTimeConsts.MONTHS; i++) {
                        if (start == null) {
                            start = OpSeasonStart;
                        }
                        else {
                            start = SLibTimeUtils.addDate(start, 0, 1, 0);
                        }

                        end = SLibTimeUtils.addDate(start, 0, 1, -1);

                        OpSeasonMonthsBoundaries.add(new Date[] { start, end });
                    }
                }

                // operating seasons boundaries:

                OpSeasonsBoundaries = new ArrayList<>();

                for (int year = OldestYear; isOpSeasonCustom() ? year < OpYear : year <= OpYear; year++) {
                    Date start = SLibTimeUtils.createDate(year, SeasonFirstMonth, MonthFirstDay);
                    Date end = SLibTimeUtils.addDate(start, 1, 0, -1);
                    OpSeasonsBoundaries.add(new Date[] { start, end });
                }
            }
        }

        /**
         * Check if operating season is customized, that is if it does not start in January.
         * @return <code>true</code> if season does not start in January.
         */
        public final boolean isOpSeasonCustom() {
            return SeasonFirstMonth != SLibTimeConsts.MONTH_JAN;
        }
        
        /**
         * Check if operating month is customized, that is if it does not start in the 1st.
         * @return <code>true</code> if month does not start in the 1st.
         */
        public final boolean isOpMonthCustom() {
            return MonthFirstDay != 1;
        }
        
        /**
         * Check if operating season or month are customized.
         * @return <code>true</code> if season does not start in January.
         */
        public final boolean isOpSeasonOrMonthCustom() {
            return isOpSeasonCustom() || isOpMonthCustom();
        }
        
        /**
         * Get operative season as text, either in "yyyy" or "yyyy-yyyy" format, depending of season is out of sync or not.
         * @return 
         */
        private String getOpSeasonAsText() {
            String text;
            
            if (SeasonFirstMonth == SLibTimeConsts.MONTH_JAN && MonthFirstDay == 1) {
                text = "" + SLibTimeUtils.digestYear(OpSeasonStart)[0];
            }
            else {
                text = "" + SLibTimeUtils.digestYear(OpSeasonStart)[0] + "-" + SLibTimeUtils.digestYear(OpSeasonEnd)[0];
            }
            
            return text;
        }
        
        /**
         * Compose description for today.
         * @return 
         */
        public String composeTodayDescrip() {
            return "día " + DateFormatGui.format(Today);
        }
        
        /**
         * Compose description for operating season.
         * @return 
         */
        public String composeOpSeasonDescrip() {
            return "temporada " + getOpSeasonAsText() + (isOpSeasonCustom()? "*" : "");
        }
        
        /**
         * Compose description for operating month.
         * @return 
         */
        public String composeOpMonthDescrip() {
            return "mes " + MonthsShort[OpMonth - 1] + ". " + OpYear + (isOpMonthCustom() ? "*" : "");
        }
        
        /**
         * Compose period for operating season in format "from A to B".
         * @return 
         */
        public String composeOpSeasonPeriod() {
            return "del " + DateFormatGui.format(OpSeasonStart) + " al " + DateFormatGui.format(OpSeasonEnd);
        }
        
        /**
         * Compose period for operating month in format "from A to B".
         * @return 
         */
        public String composeOpMonthPeriod() {
            return "del " + DateFormatGui.format(OpMonthStart) + " al " + DateFormatGui.format(OpMonthEnd);
        }
        
        /**
         * Compose information for operating season.
         * @return 
         */
        public String[] composeOpSeasonHtmlInfo() {
            String[] info = new String[isOpMonthCustom() ? 2 : 1];
            
            info[0] = "Inicio temporada: "
                    + "mes " + MonthsLong[SeasonFirstMonth - 1] + "; "
                    + "día " + DateFormatGui.format(OpSeasonStart) + ". ";
            
            if (isOpMonthCustom()) {
                info[1] = "Día cierre mensual: ";
                
                if (UseOpCalendars) {
                    info[1] += "según el calendario operativo aplicable (cierre mes actual: " + DateFormatGui.format(OpMonthEnd) + ").";
                }
                else {
                    info[1] += "días " + (MonthFirstDay - 1) + " de cada mes.";
                }
            }
            else {
                info[0] += "Día inicio mensual: " + MonthFirstDay + ".";
            }
            
            return info;
        }
        
        private String composeBoundariesAsString(final ArrayList<Date[]> boundaries) {
            String string = "";
            
            for (Date[] boundary : boundaries) {
                string += (string.isEmpty() ? "" : "\n") + SLibUtils.IsoFormatDate.format(boundary[0]) + " - " + SLibUtils.IsoFormatDate.format(boundary[1]);
            }
            
            return string;
        }
        
        /**
         * Compose string for operative season months boundaries.
         * @return 
         */
        public String composeOpSeasonMonthsBoundariesAsString() {
            return composeBoundariesAsString(OpSeasonMonthsBoundaries);
        }
        
        /**
         * Compose string for operative season months boundaries.
         * @return 
         */
        public String composeOpSeasonsBoundariesAsString() {
            return composeBoundariesAsString(OpSeasonsBoundaries);
        }
        
        @Override
        public String toString() {
            String string = "";
            
            string += "Today: " + SLibUtils.IsoFormatDate.format(Today) + "; ";
            string += "MonthStartDay: " + MonthFirstDay + "; ";
            string += "SeasonFirstMonth: " + SeasonFirstMonth + "; ";

            string += "CurrentYear: " + OpYear + "; ";
            string += "CurrentMonth: " + OpMonth + "; ";

            string += "MonthStartDate: " + SLibUtils.IsoFormatDate.format(OpMonthStart) + "; ";
            string += "MonthEndDate: " + SLibUtils.IsoFormatDate.format(OpMonthEnd) + "; ";
            
            string += "SeasonStartDate: " + SLibUtils.IsoFormatDate.format(OpSeasonStart) + "; ";
            string += "SeasonEndDate: " + SLibUtils.IsoFormatDate.format(OpSeasonEnd) + "; ";
            string += "SeasonMonthsBoundaries.size(): " + OpSeasonMonthsBoundaries.size() + ".";
            
            return string;
        }
    }
}
