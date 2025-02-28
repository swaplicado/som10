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
import som.cli.SCliReportMailerSummary;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbBranchWarehouse;
import som.mod.cfg.db.SDbCompany;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public abstract class SSomUtils {
    
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
                "WHERE b_del = 0 AND b_dis = 0 AND id_seas = " + seasonId + " AND id_item = " + itemId + " AND id_prod = " + producerId + " ORDER BY b_def DESC";
        
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
     */
    public static boolean existsTicket(final SGuiSession session, int ticketNumber, int ticketId) throws Exception {
        boolean exists = false;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
                "WHERE b_del = 0 AND num = " + ticketNumber + " " +
                (ticketId == SLibConsts.UNDEFINED ? "" : " AND id_tic <> " + ticketId + " ");

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            exists = resultSet.getInt(1) > 0;
        }

        return exists;
    }

    /**
     * Validates if Region Season Configuration exists.
     * @param session Current GUI session.
     * @param configKey Region Season Configuration primary key (index: 0 = season ID; 1 = region ID; 2 = item ID).
     * @return Value of <code>true</code> if Region Season Configuration exists.
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
     * Validate stock to specific date and the end year date
     * @param session Current GUI session.
     * @param nYearId int,
     * @param nItemId int,
     * @param nUnitId int,
     * @param nItemTypeId int,
     * @param anWarehouseId int[],
     * @param date Date
     * @param dQuantity double
     * @return SSomStock
     */
    /* Check if this function is still needed (Sergio Flores 2015-10-12).
    public static SSomStock validateStock(SGuiSession session, final int nYearId, final int nItemId, final int nUnitId, final int nItemTypeId,
            final int[] anWarehouseId, final int nDivisionId, final Date date, final double dQuantity) {

        return validateStock(
                session,
                nYearId,
                nItemId,
                nUnitId,
                nItemTypeId,
                anWarehouseId,
                nDivisionId,
                null,
                date,
                dQuantity);
    }
    */
    
    /**
     * Validate stock to specific date and the end year date
     * @param session session,
     * @param nYearId int,
     * @param nItemId int,
     * @param nUnitId int,
     * @param nItemTypeId int,
     * @param anWarehouseId int[],
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
     * Validate stock to specific date and the end year date
     * @param session Current GUI session.
     * @param nYearId int,
     * @param nItemPtId int,
     * @param nItemCullTypeId int,
     * @param anWarehouseId int[],
     * @param date Date
     * @param dStockCull double
     * @return Vector<stock, item (optional), msg>
     */
    /* Check if this function is still needed (Sergio Flores 2015-10-12).
    public static double[] convertStockCulltoStockPT(SGuiSession session, final int nYearId, final int nItemPtId, final int nItemCullTypeId,
            final int[] anWarehouseId, final Date date, final double dStockCull) {
        double dCentimetersCull = 0;
        double dStock = 0;
        double dDensityCull = 0;
        double dWarehouseHeight = 0;
        double dWarehouseCapacityLiters = 0;
        double dLiterByCentimeter = 0;
        String sSql = "";
        SDbItem itemPt = null;
        ResultSet resultSet = null;

        try {
            sSql = "SELECT i.id_item, i.fk_unit, i.den, wah.dim_heig, wah.cap_real_lt " +
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
                "s.id_year = " + nYearId + " AND " +
                "i.fk_item_tp = " + nItemCullTypeId + " AND " +
                "s.id_co = " + anWarehouseId[0] + " AND " +
                "s.id_cob = " + anWarehouseId[1] + " AND " +
                "s.id_wah = " + anWarehouseId[2] + " AND " +
                "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "'; ";

            resultSet = session.getStatement().executeQuery(sSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                dDensityCull = resultSet.getDouble("i.den");
                dWarehouseHeight = resultSet.getDouble("wah.dim_heig");
                dWarehouseCapacityLiters = resultSet.getDouble("wah.cap_real_lt");
                dLiterByCentimeter = (dWarehouseHeight * 100) > 0 ? dWarehouseCapacityLiters / (dWarehouseHeight * 100) : 0;

                itemPt = new SDbItem();
                itemPt.read(session, new int[] { nItemPtId });
                if (itemPt != null) {

                    dCentimetersCull = dStockCull / (dLiterByCentimeter * dDensityCull);
                    dStock = dCentimetersCull * (dLiterByCentimeter * itemPt.getDensity());
                }
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return new double[] { dStock, dCentimetersCull };
    }
    */

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
     * Obtain conexion with system Revuelta XXI using JDBC
     * @param session Current GUI session.
     * @return Connection
     */
    /* Deprecation due to change of DBMS of Revuelta's scale software (Alfredo Pérez, 2018-02-19)
    public static Connection openConnectionRevueltaJdbc(SGuiSession session) {
        Connection connection = null;

        try {
            Class.forName("com.caigen.sql.access.AccessDriver");
            
            connection = DriverManager.getConnection("jdbc:access:/" + ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getRevueltaPath());
        }
        catch (ClassNotFoundException | SQLException e) {
            SLibUtils.printException(SSomUtils.class.getName(), e);
        }

        return connection;
    }
    */

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
     * Obtain weight on destiny from tickes on provided period.
     * @param session Current GUI session.
     * @param idItem ID of item.
     * @param dateStart Start date.
     * @param dateEnd End date.
     * @param idReportingGroup ID of reporting group, can be zero.
     * @param idProducer ID of producer, can be zero.
     * @param idTicOrig
     * @param idTicDest
     * @return double
     * @throws java.sql.SQLException
     */
    public static double obtainWeightDestinyByPeriod(final SGuiSession session, final int idItem, final Date dateStart, final Date dateEnd, 
            final int idReportingGroup, final int idProducer, final int idTicOrig, final int idTicDest) throws SQLException {
        double weight = 0;

        String sql = "SELECT SUM(t.wei_des_net_r) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON t.fk_prod = p.id_prod " +
            "WHERE NOT t.b_del AND t.b_tar AND t.fk_item = " + idItem + " AND " +
            "t.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            (idProducer == 0 ? "" : "AND p.id_prod = " + idProducer + " ") +
            (idTicOrig == 0 ? "" : "AND t.fk_tic_orig = " + idTicOrig + " ") +
            (idTicDest == 0 ? "" : "AND t.fk_tic_dest = " + idTicDest + " ") +
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
     * @param idItem ID of item.
     * @param idAltItem
     * @param dateStart Start date.
     * @param dateEnd End date.
     * @param idReportingGroup ID of reporting group, can be zero.
     * @param idProducer ID of producer, can be zero.
     * @param idTicOrig
     * @param idTicDest
     * @return double
     * @throws java.sql.SQLException
     */
    public static double[] obtainWeightDestinyByPeriodAlternative(final SGuiSession session, final int idItem, final int idAltItem, final Date dateStart, final Date dateEnd, 
            final int idReportingGroup, final int idProducer, final int idTicOrig, final int idTicDest) throws SQLException {
        double[] arr = null;

        String sql = "SELECT " +
            "SUM(IF(t.b_alt = 0, t.wei_des_net_r, 0)) AS _tic, " +
            "SUM(IF(t.b_alt = 1, t.wei_des_net_r, 0)) AS _alt, " +
            "SUM(t.wei_des_net_r) AS _tot " +
            "FROM (" + 
            "SELECT *, dt fecha FROM " +
            SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
            "WHERE NOT b_del AND NOT b_alt AND b_tar AND fk_item = " + idItem + " " +
            "UNION " +
            "SELECT a.*, t.dt fecha FROM " +
            SModConsts.TablesMap.get(SModConsts.S_ALT_TIC) + " a " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " t ON a.id_tic = t.id_tic " + 
            "WHERE NOT a.b_del AND a.b_alt AND a.fk_item = " + idAltItem + " ) AS t " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON t.fk_prod = p.id_prod " +
            "WHERE NOT t.b_del AND " +
            "t.fecha BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            (idProducer == 0 ? "" : "AND p.id_prod = " + idProducer + " ") +
            (idTicOrig == 0 ? "" : "AND t.fk_tic_orig = " + idTicOrig + " ") +
            (idTicDest == 0 ? "" : "AND t.fk_tic_dest = " + idTicDest + " ") +
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
     * @param idTicOrig
     * @param idTicDest
     * @return double
     * @throws java.sql.SQLException
     */
    public static double obtainWeightDestinyByScale(final SGuiSession session, final int idItem, final Date dateStart, final Date dateEnd, final int idScale,
            final int idTicOrig, final int idTicDest) throws SQLException {
        double weight = 0;

        String sql = "SELECT SUM(t.wei_des_net_r) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS s ON t.fk_sca = s.id_sca " +
            "WHERE NOT t.b_del AND t.b_tar AND t.fk_item = " + idItem + " " +
            (idTicOrig == 0 ? "" : "AND t.fk_tic_orig = " + idTicOrig + " ") +
            (idTicDest == 0 ? "" : "AND t.fk_tic_dest = " + idTicDest + " ") +
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
     * @param idItem int,
     * @param idAltItem
     * @param dateStart Date,
     * @param dateEnd Date,
     * @param idScale int
     * @param idTicOrig
     * @param idTicDest
     * @return double
     * @throws java.sql.SQLException
     */
    public static double[] obtainWeightDestinyByScaleAlternative(final SGuiSession session, final int idItem, final int idAltItem, final Date dateStart, final Date dateEnd, final int idScale,
            final int idTicOrig, final int idTicDest) throws SQLException {
        double[] arr = null;

        String sql = "SELECT " +
            "SUM(IF(t.b_alt = 0, t.wei_des_net_r, 0)) AS _tic, " +
            "SUM(IF(t.b_alt = 1, t.wei_des_net_r, 0)) AS _alt, " +
            "SUM(t.wei_des_net_r) AS _tot " +
            "FROM (" + 
            "SELECT *, dt fecha FROM " +
            SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
            "WHERE NOT b_del AND NOT b_alt AND b_tar AND fk_item = " + idItem + " " +
            "UNION " +
            "SELECT a.*, t.dt fecha FROM " +
            SModConsts.TablesMap.get(SModConsts.S_ALT_TIC) + " a " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " t ON a.id_tic = t.id_tic " + 
            "WHERE NOT a.b_del AND a.b_alt AND a.fk_item = " + idAltItem + " ) AS t " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS s ON t.fk_sca = s.id_sca " +
            "WHERE NOT t.b_del " +
            (idTicOrig == 0 ? "" : "AND t.fk_tic_orig = " + idTicOrig + " ") +
            (idTicDest == 0 ? "" : "AND t.fk_tic_dest = " + idTicDest + " ") +
            "AND t.fecha BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            "AND t.fk_sca = " + idScale + ";";
        ResultSet resultSet = session.getStatement().executeQuery(sql);

        if (resultSet.next()) {
            arr = new double[] { resultSet.getDouble(1), resultSet.getDouble(2), resultSet.getDouble(3) };
        }

        return arr;
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
     * @return String msg error
     */
    public static String obtainWarehouseItemsForBusinessRules(final SGuiSession session, final int[] paramPkWarehouseId, final int[] paramPkStockId, final Date paramDate,
            final int paramFkItemId, final int paramFkUnitId, final double paramQuantity, final String paramXtaUnit, final boolean paramXtaValidateWarehouseCapacity,
            final boolean paramXtaValidateExportationStock, final int paramFkMixId) throws Exception {
        String result = "";

        SDbBranchWarehouse warehouse = null;
        SDbItem item = null;
        SDbUnit unit = null;
        SSomWarehouseItem somWarehouseItem = null;
        ArrayList<SSomWarehouseItem> aWarehouseItems = new ArrayList<SSomWarehouseItem>();

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
     * Obtain stock warehouse by item
     * @param session SGuiSession,
     * @param nYearId int,
     * @param anWarehouseId int[],
     * @param anStockMoveId int[],
     * @param dateStart, Date,
     * @param dateEnd, Date,
     * @param nDivisionId int,
     * @return ArrayList<SSomWarehouseId>
     */
    public static ArrayList<SSomWarehouseItem> stockWarehouseByItem(final SGuiSession session, final int nYearId, final int[] anWarehouseId, final int[] anStockMoveId,
            final Date dateStart, final Date dateEnd, final int nDivisionId, final int nMixId) {
        String sql = "";
        ArrayList<SSomWarehouseItem> aWarehouseItem = new ArrayList<SSomWarehouseItem>();
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
    
    
    public static HashMap getOrigin(SGuiSession session, int idItem) throws SQLException {
        HashMap <Integer, String> origins = new HashMap<>();
        
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
    
    private static String composeHtmlTableKgPctMonthComparativeHeader(final String table, final String itemName, final String month, final int year, final String conceptName) {
        return "<b>Resumen y comparativa " + SLibUtils.textToHtml(table) + ": " + SLibUtils.textToHtml(itemName) + "</b><br>"
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
    
    private static String composeHtmlTableKgPctSeasonComparativeHeader(final String table, final String itemName, Date seasonStart, Date seasonEnd, final String conceptName) {
        return "<b>Resumen y comparativa " + SLibUtils.textToHtml(table) + ": " + SLibUtils.textToHtml(itemName) + "</b><br>"
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
    
    private static String composeHtmlTableKgPctComparativeRow(final String concept, final double weight, final double weightTotal, double weight1yAgo, double weight2yAgo, double weight3yAgo) {
        return "<tr>"
                + "<td>" + SLibUtils.textToHtml(concept) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(weightTotal == 0 ? 0 : weight / weightTotal) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight1yAgo) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight - weight1yAgo) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight2yAgo) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight - weight2yAgo) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight3yAgo) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight - weight3yAgo) + "</td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableKgPctComparativeFooter(final String table, final double weightTotal, final double weight1yAgoTotal, final double diference1yAgoTotal, 
            final double weight2yAgoTotal, final double diference2yAgoTotal, final double weight3yAgoTotal, final double diference3yAgoTotal) {
        return "<tr>"
                + "<td><b>Total " + SLibUtils.textToHtml(table) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatPercentage2D.format(1.0) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weight1yAgoTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(diference1yAgoTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weight2yAgoTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(diference2yAgoTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weight3yAgoTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(diference3yAgoTotal) + "</b></td>"
                + "</tr>"
                + "</table><br>";
    }
    
    private static String composeHtmlTableKgPctHeader(final String table, final String itemName, final String conceptName) {
        return "<b>Resumen " + SLibUtils.textToHtml(table) + ": " + SLibUtils.textToHtml(itemName) + "</b><br>"
                + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
                + "<tr>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(conceptName) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableKgPctHeaderAlternative(final String table, final String conceptName) {
        return "<b>Resumen " + SLibUtils.textToHtml(table) + ": </b><br>"
                + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
                + "<tr>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(conceptName) + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " conv.") + "</b></td>"
                + "<td class=Forms align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " org.") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml(SSomConsts.KG + " total") + "</b></td>"
                + "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>"
                + "</tr>";
    }

    private static String composeHtmlTableKgPctRow(final String concept, final double weight, final double weightTotal) {
        return "<tr>"
                + "<td>" + SLibUtils.textToHtml(concept) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weight) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(weightTotal == 0 ? 0 : weight / weightTotal) + "</td>"
                + "</tr>";
    }
    
    private static String composeHtmlTableKgPctRowAlternative(final String concept, final double weightTic, final double weightAlt, final double weightAll, final double weightTotal) {
        return "<tr>"
                + "<td>" + SLibUtils.textToHtml(concept) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weightTic) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weightAlt) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(weightAll) + "</td>"
                + "<td align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(weightTotal == 0 ? 0 : weightAll / weightTotal) + "</td>"
                + "</tr>";
    }

    private static String composeHtmlTableKgPctFooter(final String table, final double weightTotal) {
        return "<tr>"
                + "<td><b>Total " + SLibUtils.textToHtml(table) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatPercentage2D.format(1.0) + "</b></td>"
                + "</tr>"
                + "</table><br>";
    }
    
    private static String composeHtmlTableKgPctFooterAlternative(final String table, final double weightTotTic, final double weightTotAlt, final double weightTotal) {
        return "<tr>"
                + "<td><b>Total " + SLibUtils.textToHtml(table) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotTic) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotAlt) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(weightTotal) + "</b></td>"
                + "<td align='right'><b>" + SLibUtils.DecimalFormatPercentage2D.format(1.0) + "</b></td>"
                + "</tr>"
                + "</table><br>";
    }

    /**
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
    public static String composeHtmlSummaryItem(final SGuiSession session, final int itemId, final Date date, final int repType, final int idTicOrig, final int idTicDest) throws Exception {
        // REPORT PREPARATION:

        double weight;
        double weightTotal;
        double weight1yAgo;
        double weight1yAgoTotal;
        double weight2yAgo;
        double weight2yAgoTotal;
        double weight3yAgo;
        double weight3yAgoTotal;
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

        body += composeHtmlTableKgPctHeader(section, itemName, "Proveedor");

        // compute reporting groups:

        while (repGroupResultSet.next()) { // first reading, cursor before first row
            weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
            if (weight != 0) {
                body += composeHtmlTableKgPctRow(repGroupResultSet.getString("name"), weight, weightTotal);
            }
        }

        body += composeHtmlTableKgPctFooter(section, weightTotal);

        // SECTION 1.2. Current day summary by scale:

        int scaleRows = 0;
        boolean scaleForce = false;
        String scaleHtml = composeHtmlTableKgPctHeader(section, itemName, "Báscula");

        // compute scales:

        while (scaleResultSet.next()) { // first reading, cursor before first row
            weight = SSomUtils.obtainWeightDestinyByScale(session, itemId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            if (weight != 0) {
                scaleHtml += composeHtmlTableKgPctRow(scaleResultSet.getString("name"), weight, weightTotal);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableKgPctFooter(section, weightTotal);

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

        if (repType == SCliReportMailerSummary.REGULAR_REPORT) {
            body += composeHtmlTableKgPctHeader(section, itemName, "Proveedor");

            while (repGroupResultSet.next()) {
                weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                if (weight != 0) {
                    body += composeHtmlTableKgPctRow(repGroupResultSet.getString("name"), weight, weightTotal);
                }
            }

            body += composeHtmlTableKgPctFooter(section, weightTotal);
        }
        else {
            double diference1yAgoTotal = 0;
            double diference2yAgoTotal = 0;
            double diference3yAgoTotal = 0;
            weight1yAgoTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), 0, 0, idTicOrig, idTicDest);
            weight2yAgoTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), 0, 0, idTicOrig, idTicDest);
            weight3yAgoTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), 0, 0, idTicOrig, idTicDest);
            body += composeHtmlTableKgPctMonthComparativeHeader(section, itemName, months[curMonth - 1], curYear, "Proveedor");
            
            while (repGroupResultSet.next()) {
                weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight1yAgo = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight2yAgo = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight3yAgo = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                diference1yAgoTotal += weight - weight1yAgo;
                diference2yAgoTotal += weight - weight2yAgo;
                diference3yAgoTotal += weight - weight3yAgo;
                if (weight != 0 || weight1yAgo != 0 || weight2yAgo != 0 || weight3yAgo != 0) {
                    body += composeHtmlTableKgPctComparativeRow(repGroupResultSet.getString("name"), weight, weightTotal, weight1yAgo, weight2yAgo, weight3yAgo);
                }
            }
            
            body += composeHtmlTableKgPctComparativeFooter(section, weightTotal, weight1yAgoTotal, diference1yAgoTotal, weight2yAgoTotal, diference2yAgoTotal, weight3yAgoTotal, diference3yAgoTotal);
        }

        // SECTION 2.2: Current month summary by scale: 

        scaleRows = 0;
        scaleForce = false;
        scaleHtml = composeHtmlTableKgPctHeader(section, itemName, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weight = SSomUtils.obtainWeightDestinyByScale(session, itemId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            if (weight != 0) {
                scaleHtml += composeHtmlTableKgPctRow(scaleResultSet.getString("name"), weight, weightTotal);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableKgPctFooter(section, weightTotal);

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

        if (repType == SCliReportMailerSummary.REGULAR_REPORT) {
            body += composeHtmlTableKgPctHeader(section, itemName, "Proveedor");


            while (repGroupResultSet.next()) {
                weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                if (weight != 0) {
                    body += composeHtmlTableKgPctRow(repGroupResultSet.getString("name"), weight, weightTotal);
                }
            }

            body += composeHtmlTableKgPctFooter(section, weightTotal);
        }
        else {
            double diference1yAgoTotal = 0;
            double diference2yAgoTotal = 0;
            double diference3yAgoTotal = 0;
            weight1yAgoTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), 0, 0, idTicOrig, idTicDest);
            weight2yAgoTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), 0, 0, idTicOrig, idTicDest);
            weight3yAgoTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), 0, 0, idTicOrig, idTicDest);
            body += composeHtmlTableKgPctSeasonComparativeHeader(section, itemName, dateSeasonStart, dateSeasonEnd, "Proveedor");
            
            while (repGroupResultSet.next()) {
                weight = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight1yAgo = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 1, 0, 0), SLibTimeUtils.addDate(dateEnd, - 1, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight2yAgo = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 2, 0, 0), SLibTimeUtils.addDate(dateEnd, - 2, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                weight3yAgo = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, SLibTimeUtils.addDate(dateStart, - 3, 0, 0), SLibTimeUtils.addDate(dateEnd, - 3, 0, 0), repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
                diference1yAgoTotal += weight - weight1yAgo;
                diference2yAgoTotal += weight - weight2yAgo;
                diference3yAgoTotal += weight - weight3yAgo;
                if (weight != 0 || weight1yAgo != 0 || weight2yAgo != 0 || weight3yAgo != 0) {
                    body += composeHtmlTableKgPctComparativeRow(repGroupResultSet.getString("name"), weight, weightTotal, weight1yAgo, weight2yAgo, weight3yAgo);
                }
            }
            
            body += composeHtmlTableKgPctComparativeFooter(section, weightTotal, weight1yAgoTotal, diference1yAgoTotal, weight2yAgoTotal, diference2yAgoTotal, weight3yAgoTotal, diference3yAgoTotal);
        }
        
        // SECTION 3.2 Current season summary by scale:

        scaleRows = 0;
        scaleForce = false;
        scaleHtml = composeHtmlTableKgPctHeader(section, itemName, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weight = SSomUtils.obtainWeightDestinyByScale(session, itemId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            if (weight != 0) {
                scaleHtml += composeHtmlTableKgPctRow(scaleResultSet.getString("name"), weight, weightTotal);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableKgPctFooter(section, weightTotal);

        if (scaleForce || scaleRows > 1) {
            body += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        // SECTION 4. Season summary by month:

        dateStart = dateSeasonStart;
        dateEnd = dateSeasonEnd;
        weightTotal = SSomUtils.obtainWeightDestinyByPeriod(session, itemId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "temporada";

        body += composeHtmlTableKgPctHeader(section, itemName, "Período");

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
            body += composeHtmlTableKgPctRow("" + year + " " + months[month - 1] + ".", weight, weightTotal);
            month++;
        }

        body += composeHtmlTableKgPctFooter(section, weightTotal);

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

    /**
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
    public static String composeHtmlSummaryItemAlternative(final SGuiSession session, final int itemId, final int itemAltId, final Date date, final int idTicOrig, final int idTicDest) throws Exception {
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
        weightsTots = SSomUtils.obtainWeightDestinyByPeriodAlternative(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "día '" + SLibUtils.DateFormatDate.format(date) + "'";

        // SECTION 1.1. Current day summary by reporting group:

        body += composeHtmlTableKgPctHeaderAlternative(section, "Proveedor");

        // compute reporting groups:

        while (repGroupResultSet.next()) { // first reading, cursor before first row
            weights = SSomUtils.obtainWeightDestinyByPeriodAlternative(session, itemId, itemAltId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
            if (weights != null && weights[2] != 0.0) {
                body += composeHtmlTableKgPctRowAlternative(repGroupResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
            }
        }

        body += composeHtmlTableKgPctFooterAlternative(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        // SECTION 1.2. Current day summary by scale:

        int scaleRows = 0;
        boolean scaleForce = false;
        String scaleHtml = composeHtmlTableKgPctHeaderAlternative(section, "Báscula");

        // compute scales:

        while (scaleResultSet.next()) { // first reading, cursor before first row
            weights = SSomUtils.obtainWeightDestinyByScaleAlternative(session, itemId, itemAltId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            if (weights != null && weights[2] != 0.0) {
                scaleHtml += composeHtmlTableKgPctRowAlternative(scaleResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableKgPctFooterAlternative(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        if (scaleForce || scaleRows > 1) {
            body += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        // SECTION 2. Current month:

        dateStart = SLibTimeUtils.getBeginOfMonth(date);
        dateEnd = SLibTimeUtils.getEndOfMonth(date);
        weightsTots = SSomUtils.obtainWeightDestinyByPeriodAlternative(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "mes '" + months[curMonth - 1] + ". " + curYear + "'";

        // SECTION 2.1. Current month summary by reporting group:
        
        body += composeHtmlTableKgPctHeaderAlternative(section, "Proveedor");

        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        while (repGroupResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByPeriodAlternative(session, itemId, itemAltId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);

            if (weights != null && weights[2] != 0.0) {
                body += composeHtmlTableKgPctRowAlternative(repGroupResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
            }
        }

        body += composeHtmlTableKgPctFooterAlternative(section, weightsTots[0], weightsTots[1], weightsTots[2]);
        
        // SECTION 2.2: Current month summary by scale: 

        scaleRows = 0;
        scaleForce = false;
        scaleHtml = composeHtmlTableKgPctHeaderAlternative(section, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByScaleAlternative(session, itemId, itemAltId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            
            if (weights != null && weights[2] != 0.0) {
                scaleHtml += composeHtmlTableKgPctRowAlternative(scaleResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableKgPctFooterAlternative(section, weightsTots[0], weightsTots[1], weightsTots[2]);

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
        weightsTots = SSomUtils.obtainWeightDestinyByPeriodAlternative(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "temporada";

        // SECTION 3.1 Current season summary by reporting group:

        body += composeHtmlTableKgPctHeaderAlternative(section, "Proveedor");

        // compute reporting groups:

        if (repGroupResultSet.isAfterLast()) {
            repGroupResultSet.beforeFirst();
        }

        while (repGroupResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByPeriodAlternative(session, itemId, itemAltId, dateStart, dateEnd, repGroupResultSet.getInt("id_rep_grp"), 0, idTicOrig, idTicDest);
            if (weights != null && weights[2] != 0.0) {
                body += composeHtmlTableKgPctRowAlternative(repGroupResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
            }
        }

        body += composeHtmlTableKgPctFooterAlternative(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        // SECTION 3.2 Current season summary by scale:

        scaleRows = 0;
        scaleForce = false;
        scaleHtml = composeHtmlTableKgPctHeaderAlternative(section, "Báscula");

        // compute scales:

        if (scaleResultSet.isAfterLast()) {
            scaleResultSet.beforeFirst();
        }

        while (scaleResultSet.next()) {
            weights = SSomUtils.obtainWeightDestinyByScaleAlternative(session, itemId, itemAltId, dateStart, dateEnd, scaleResultSet.getInt("id_sca"), idTicOrig, idTicDest);
            
            if (weights != null && weights[2] != 0.0) {
                scaleHtml += composeHtmlTableKgPctRowAlternative(scaleResultSet.getString("name"), weights[0], weights[1], weights[2], weightsTots[2]);
                scaleRows++;
                if (!scaleForce && !scaleResultSet.getBoolean("b_def")) {
                    scaleForce = true;
                }
            }
        }

        scaleHtml += composeHtmlTableKgPctFooterAlternative(section, weightsTots[0], weightsTots[1], weightsTots[2]);

        if (scaleForce || scaleRows > 1) {
            body += scaleHtml; // more than one scale present or receptions done in a non-default scale
        }

        // SECTION 4. Season summary by month:

        dateStart = dateSeasonStart;
        dateEnd = dateSeasonEnd;
        weightsTots = SSomUtils.obtainWeightDestinyByPeriodAlternative(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
        section = "temporada";

        body += composeHtmlTableKgPctHeaderAlternative(section, "Período");

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

            weights = SSomUtils.obtainWeightDestinyByPeriodAlternative(session, itemId, itemAltId, dateStart, dateEnd, 0, 0, idTicOrig, idTicDest);
            body += composeHtmlTableKgPctRowAlternative("" + year + " " + months[month - 1] + ".", weights[0], weights[1], weights[2], weightsTots[2]);
            month++;
        }

        body += composeHtmlTableKgPctFooterAlternative(section, weightsTots[0], weightsTots[1], weightsTots[2]);

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
}
