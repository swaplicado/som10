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
import java.util.Date;
import java.util.Properties;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbBranchWarehouse;
import som.mod.cfg.db.SDbCompany;

/**
 *
 * @author Juan Barajas, Sergio Flores, Alfredo Pérez
 */
public abstract class SSomUtils {
    
    /**
     * Gets proper season for ticket defined by date, item and producer.
     * @param session Current GUI session.
     * @param date Date of ticket.
     * @param itemId Item ID.
     * @param producerId Producer ID.
     * @return Proper season ID.
     */
    public static int getProperSeasonId(SGuiSession session, Date date, int itemId, int producerId) throws Exception {
        int seasonId = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT s.id_seas " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS sp ON s.id_seas = sp.id_seas AND sp.id_item = " + itemId + " AND sp.id_prod = " + producerId + " " +
                "WHERE s.b_del = 0 AND s.b_dis = 0 AND s.b_clo = 0 AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' AND dt_end >= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' ";
        
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
     */
    public static int getProperRegionId(SGuiSession session, int seasonId, int itemId, int producerId) throws Exception {
        int regionId = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_reg " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " " +
                "WHERE b_del = 0 AND b_dis = 0 AND id_seas = " + seasonId + " AND id_item = " + itemId + " AND id_prod = " + producerId + " ";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            regionId = resultSet.getInt(1);
        }

        return  regionId;
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
                    ticket.setAuxRequiredCalculation(true);
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
                ticket.setAuxRequiredCalculation(true);
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
            final int[] warehouseKey, final int divisionId, final int[] stockMoveKey, final Date date, final boolean absolute) {
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
    /* Check if this function is still needed (sflores, 2015-10-12).
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

        stock = obtainStock(session, nYearId, nItemId, nUnitId, nItemTypeId, anWarehouseId, nDivisionId, anStockMoveId, date, false);
        if (stock.getStock() < 0 || stock.getStock() < dQuantity) {
            stock.setResult("No hay existencias suficientes del producto '" + stock.getItem() + "' a la fecha '" + SLibUtils.DateFormatDate.format(date) + "'.");
        }
        else {
            stock = obtainStock(session, nYearId, nItemId, nUnitId, nItemTypeId, anWarehouseId, nDivisionId, anStockMoveId, SLibTimeUtils.getBeginOfYear(date), true);
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
    /* Check if this function is still needed (sflores, 2015-10-12).
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
            SDbCompany company = ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany();
            String url="jdbc:sybase:Tds:" + company.getRevueltaHost() + ":" + company.getRevueltaPort() + "/Revuelta";
            Properties prop = new Properties();
            prop.put("user", "usuario");
            prop.put("password", "revuelta");
            connection = DriverManager.getConnection(url, prop);
        }
        catch (ClassNotFoundException | SQLException e) {
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
     * @param idItem int,
     * @param dateStart Date,
     * @param dateEnd Date,
     * @param idReportingGroup int
     * @return double
     */
    public static double obtainWeightDestinyByPeriod(final SGuiSession session, final int idItem, final Date dateStart, final Date dateEnd, final int idReportingGroup) throws SQLException {
        double weight = 0;

        String sql = "SELECT SUM(t.wei_des_net_r) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON t.fk_prod = p.id_prod " +
            "WHERE NOT t.b_del AND t.b_tar AND t.fk_item = " + idItem + " AND " +
            "t.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            (idReportingGroup == SLibConsts.UNDEFINED ? "" : "AND p.fk_rep_grp = " + idReportingGroup + " ") + ";";
        ResultSet resultSet = session.getStatement().executeQuery(sql);

        if (resultSet.next()) {
            weight = resultSet.getDouble(1);
        }

        return weight;
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
        SDbIog iog = null;
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

                    iog = new SDbIog();
                    iog.computeIog(SLibTimeUtils.createDate(SLibTimeUtils.digestDate(pDate)[0], SLibTimeUtils.digestDate(pDate)[1], SLibTimeUtils.digestDate(pDate)[2]-1),
                        productionEstimateDistribute.getProductionAssigned(), true, SModSysConsts.SS_IOG_TP_OUT_MFG_FG_ASD, SModSysConsts.SU_IOG_ADJ_TP_NA, poMfgWarehouseProductProduction.getPkItemId(), poMfgWarehouseProductProduction.getPkUnitId(),
                        new int[] { poMfgWarehouseProductProduction.getPkCompanyId(), poMfgWarehouseProductProduction.getPkBranchId(), poMfgWarehouseProductProduction.getPkWarehouseId() }, pnDivisionId, pnProductionEstimateId,
                        pnProductionEstimateVersionId, session.getUser().getPkUserId());
                    iog.setSystem(true);
                    productionEstimateDistribute.getIogOut().add(iog);

                    // Production move in (warehouse storage):

                    iog = new SDbIog();
                    iog.computeIog(SLibTimeUtils.createDate(SLibTimeUtils.digestDate(pDate)[0], SLibTimeUtils.digestDate(pDate)[1], SLibTimeUtils.digestDate(pDate)[2]-1),
                        productionEstimateDistribute.getProductionAssigned(), true, SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD, SModSysConsts.SU_IOG_ADJ_TP_NA, mfgWarehouseProductStorage.getPkItemId(), mfgWarehouseProductStorage.getPkUnitId(),
                        new int[] { mfgWarehouseProductStorage.getPkCompanyId(), mfgWarehouseProductStorage.getPkBranchId(), mfgWarehouseProductStorage.getPkWarehouseId() }, pnDivisionId, pnProductionEstimateId,
                        pnProductionEstimateVersionId, session.getUser().getPkUserId());
                    iog.setSystem(true);
                    productionEstimateDistribute.getIogIn().add(iog);

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
}
