/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.cli.SCliConsts;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SExwUtils {
    
    public static final int STOCK_ITEM_EXW = 1;
    public static final int STOCK_ITEM = 2;
    
    public static final String INFLOW = "I";
    public static final String OUTFLOW = "O";
    
    public static final String MVMT_OPEN_STK = "OPEN_STK";
    public static final String MVMT_TICKET = "SCA_TIC";
    public static final String MVMT_ADJUST = "STK_ADJ";
    
    public static final HashMap<String, String> MovementTypes = new HashMap<>();
    
    static {
        MovementTypes.put(MVMT_OPEN_STK, "Inv. inicial");
        MovementTypes.put(MVMT_TICKET, "Boleto");
        MovementTypes.put(MVMT_ADJUST, "Ajuste");
    }
    
    /**
     * Get date of start of stock control in External Warehouses.
     * @param session GUI session.
     * @return
     * @throws Exception 
     */
    public static Date getExwStart(final SGuiSession session) throws Exception {
        Date start = null;
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT MIN(dt) AS _start "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " "
                    + "WHERE NOT b_del AND b_tar "
                    + "AND (fk_tic_orig = " + SModSysConsts.SU_TIC_ORIG_EXW + " OR fk_tic_dest = " + SModSysConsts.SU_TIC_DEST_EXW + ");";
            
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                start = resultSet.getDate("_start");
            }
        }
        
        return start != null ? start : SLibTimeUtils.createDate(SCliConsts.FRUIT_FIRST_YEAR, SLibTimeConsts.MONTH_JAN, 1);
    }

    /**
     * Compose SQL sentence for derived table of external warehouse cardex.
     * Result set columns: mvmt_type (TICKET | ADJUST), id_mvmt (ID of TICKET | ADJUST), flow (INFLOW | OUTFLOW), 
     * mvmt_date, mvmt_folio, id_adjust_type, id_exw_fac, qty, id_usr_ins, ts_ins, id_usr_upd, ts_upd.
     * @param itemId ID of required item. Mandatory.
     * @param unitId ID of required unit. Mandatory.
     * @param exwFacilityId ID of desired External Warehouse Facility. Optional. When not needed, pass -1.
     * @param cardexStart Start of cardex control. Mandatory.
     * @param cutoff Cutoff date for stock. Mandatory.
     * @return 
     * @throws java.lang.Exception 
     */
    public static String composeSqlExwCardex(final int itemId, final int unitId, final int exwFacilityId, final Date cardexStart, final Date cutoff) throws Exception {
        if (itemId == 0) {
            throw new Exception("No se proporcionó el ítem.");
        }
        
        if (unitId == 0) {
            throw new Exception("No se proporcionó la unidad.");
        }
        
        String sqlPeriod = "BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(cardexStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(cutoff) + "' ";
        
        return
                // stock inflows:
                "SELECT "
                + "'" + MVMT_TICKET + "' AS mvmt_type, t.id_tic AS id_mvmt, '" + INFLOW + "' AS flow, "
                + "t.dt AS mvmt_date, CONCAT(s.code, '-', t.num) AS mvmt_folio, 0 AS id_adjust_type, t.fk_exw_fac_dest AS id_exw_fac, t.wei_des_net_r AS qty, "
                + "t.fk_usr_ins AS id_usr_ins, t.ts_usr_ins AS ts_usr_ins, t.fk_usr_upd AS id_usr_upd, t.ts_usr_upd AS ts_usr_upd "
                + "FROM "
                + "s_tic AS t "
                + "INNER JOIN su_sca AS s ON s.id_sca = t.fk_sca "
                + "WHERE "
                + "NOT t.b_del AND t.b_tar AND t.dt " + sqlPeriod
                + "AND t.fk_tic_dest = " + SModSysConsts.SU_TIC_DEST_EXW + " "
                + (itemId == 0 ? "" : "AND t.fk_item = " + itemId + " ")
                + (unitId == 0 ? "" : "AND t.fk_unit = " + unitId + " ")
                + (exwFacilityId == -1 ? "" : "AND t.fk_exw_fac_dest = " + exwFacilityId + " ")
                + ""
                + "UNION "
                + ""
                // stock outflows:
                + "SELECT "
                + "'" + MVMT_TICKET + "' AS mvmt_type, t.id_tic AS id_mvmt, '" + OUTFLOW + "' AS flow, "
                + "t.dt AS mvmt_date, CONCAT(s.code, '-', t.num) AS mvmt_folio, 0 AS id_adjust_type, t.fk_exw_fac_orig AS id_exw_fac, t.wei_des_net_r AS qty, "
                + "t.fk_usr_ins AS id_usr_ins, t.ts_usr_ins AS ts_usr_ins, t.fk_usr_upd AS id_usr_upd, t.ts_usr_upd AS ts_usr_upd "
                + "FROM "
                + "s_tic AS t "
                + "INNER JOIN su_sca AS s ON s.id_sca = t.fk_sca "
                + "WHERE "
                + "NOT t.b_del AND t.b_tar AND t.dt " + sqlPeriod
                + "AND t.fk_tic_orig = " + SModSysConsts.SU_TIC_ORIG_EXW + " "
                + (itemId == 0 ? "" : "AND t.fk_item = " + itemId + " ")
                + (unitId == 0 ? "" : "AND t.fk_unit = " + unitId + " ")
                + (exwFacilityId == -1 ? "" : "AND t.fk_exw_fac_orig = " + exwFacilityId + " ")
                + ""
                + "UNION "
                + ""
                // inflow adjustments:
                + "SELECT "
                + "'" + MVMT_ADJUST + "' AS mvmt_type, a.id_exw_adj AS id_mvmt, '" + INFLOW + "' AS flow, "
                + "a.dt AS mvmt_date, CONCAT(a.ser, IF(a.ser = '', '', '-'), LPAD(a.num, " + SDbExwAdjustment.NUM_LEN + ", 0)) AS mvmt_folio, a.fk_exw_adj_tp AS id_adjust_type, a.fk_exw_fac AS id_exw_fac, a.qty AS qty, "
                + "a.fk_usr_ins AS id_usr_ins, a.ts_usr_ins AS ts_usr_ins, a.fk_usr_upd AS id_usr_upd, a.ts_usr_upd AS ts_usr_upd "
                + "FROM "
                + "m_exw_adj AS a "
                + "WHERE "
                + "NOT a.b_del AND a.dt " + sqlPeriod
                + "AND a.fk_iog_ct = " + SModSysConsts.SS_IOG_CT_IN + " "
                + (itemId == 0 ? "" : "AND a.fk_item = " + itemId + " ")
                + (unitId == 0 ? "" : "AND a.fk_unit = " + unitId + " ")
                + (exwFacilityId == -1 ? "" : "AND a.fk_exw_fac = " + exwFacilityId + " ")
                + ""
                + "UNION "
                + ""
                // outflow adjustments:
                + "SELECT "
                + "'" + MVMT_ADJUST + "' AS mvmt_type, a.id_exw_adj AS id_mvmt, '" + OUTFLOW + "' AS flow, "
                + "a.dt AS mvmt_date, CONCAT(a.ser, IF(a.ser = '', '', '-'), LPAD(a.num, " + SDbExwAdjustment.NUM_LEN + ", 0)) AS mvmt_folio, a.fk_exw_adj_tp AS id_adjust_type, a.fk_exw_fac AS id_exw_fac, a.qty AS qty, "
                + "a.fk_usr_ins AS id_usr_ins, a.ts_usr_ins AS ts_usr_ins, a.fk_usr_upd AS id_usr_upd, a.ts_usr_upd AS ts_usr_upd "
                + "FROM "
                + "m_exw_adj AS a "
                + "WHERE "
                + "NOT a.b_del AND a.dt " + sqlPeriod
                + "AND a.fk_iog_ct = " + SModSysConsts.SS_IOG_CT_OUT + " "
                + (itemId == 0 ? "" : "AND a.fk_item = " + itemId + " ")
                + (unitId == 0 ? "" : "AND a.fk_unit = " + unitId + " ")
                + (exwFacilityId == -1 ? "" : "AND a.fk_exw_fac = " + exwFacilityId + " ")
                + ""
                + "ORDER BY "
                + "mvmt_date, flow, mvmt_type, mvmt_folio, id_mvmt";
    }
    
    /**
     * Compose SQL sentence for derived table of external warehouse stock.
     * Result set columns: flow (INFLOW | OUTFLOW), id_item, id_unit, id_exw_fac, flow_prev, flow_curr.
     * @param itemId ID of desired item. Optional. When not needed, pass zero.
     * @param unitId ID of desired unit. Optional. When not needed, pass zero.
     * @param exwFacilityId ID of desired External Warehouse Facility. Optional. When not needed, pass -1.
     * @param exwStart Start of stock control. Optional. When unknown, pass a <code>null</code> value.
     * @param cutoff Cutoff date for stock. Required.
     * @return 
     */
    public static String composeSqlExwStock(final int itemId, final int unitId, final int exwFacilityId, final Date exwStart, final Date cutoff) {
        String sqlCutoffBoy = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfYear(cutoff)) + "'";
        String sqlPeriod = "";
        
        if (exwStart != null) {
            sqlPeriod = "BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(exwStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(cutoff) + "' ";
        }
        
        return
                // stock inflows:
                "SELECT "
                + "'" + INFLOW + "' AS flow, t.fk_item AS id_item, t.fk_unit AS id_unit, t.fk_exw_fac_dest AS id_exw_fac, "
                + "SUM(IF(t.dt < " + sqlCutoffBoy + ", t.wei_des_net_r, 0.0)) AS flow_prev, "
                + "SUM(IF(t.dt >= " + sqlCutoffBoy + ", t.wei_des_net_r, 0.0)) AS flow_curr "
                + "FROM "
                + "s_tic AS t "
                + "WHERE "
                + "NOT t.b_del AND t.b_tar " + (sqlPeriod.isEmpty() ? "" : "AND t.dt " + sqlPeriod)
                + "AND t.fk_tic_dest = " + SModSysConsts.SU_TIC_DEST_EXW + " "
                + (itemId == 0 ? "" : "AND t.fk_item = " + itemId + " ")
                + (unitId == 0 ? "" : "AND t.fk_unit = " + unitId + " ")
                + (exwFacilityId == -1 ? "" : "AND t.fk_exw_fac_dest = " + exwFacilityId + " ")
                + "GROUP BY "
                + "t.fk_item, t.fk_unit, t.fk_exw_fac_dest "
                + ""
                + "UNION "
                + ""
                // stock outflows:
                + "SELECT "
                + "'" + OUTFLOW + "' AS flow, t.fk_item AS id_item, t.fk_unit AS id_unit, t.fk_exw_fac_orig AS id_exw_fac, "
                + "-SUM(IF(t.dt < " + sqlCutoffBoy + ", t.wei_des_net_r, 0.0)) AS flow_prev, "
                + "-SUM(IF(t.dt >= " + sqlCutoffBoy + ", t.wei_des_net_r, 0.0)) AS flow_curr "
                + "FROM "
                + "s_tic AS t "
                + "WHERE "
                + "NOT t.b_del AND t.b_tar " + (sqlPeriod.isEmpty() ? "" : "AND t.dt " + sqlPeriod)
                + "AND t.fk_tic_orig = " + SModSysConsts.SU_TIC_ORIG_EXW + " "
                + (itemId == 0 ? "" : "AND t.fk_item = " + itemId + " ")
                + (unitId == 0 ? "" : "AND t.fk_unit = " + unitId + " ")
                + (exwFacilityId == -1 ? "" : "AND t.fk_exw_fac_orig = " + exwFacilityId + " ")
                + "GROUP BY "
                + "t.fk_item, t.fk_unit, t.fk_exw_fac_orig "
                + ""
                + "UNION "
                + ""
                // inflow adjustments:
                + "SELECT "
                + "'" + INFLOW + "' AS flow, a.fk_item AS id_item, a.fk_unit AS id_unit, a.fk_exw_fac AS id_exw_fac, "
                + "SUM(IF(a.dt < " + sqlCutoffBoy + ", a.qty, 0.0)) AS flow_prev, "
                + "SUM(IF(a.dt >= " + sqlCutoffBoy + ", a.qty, 0.0)) AS flow_curr "
                + "FROM "
                + "m_exw_adj AS a "
                + "WHERE "
                + "NOT a.b_del " + (sqlPeriod.isEmpty() ? "" : "AND a.dt " + sqlPeriod)
                + "AND a.fk_iog_ct = " + SModSysConsts.SS_IOG_CT_IN + " "
                + (itemId == 0 ? "" : "AND a.fk_item = " + itemId + " ")
                + (unitId == 0 ? "" : "AND a.fk_unit = " + unitId + " ")
                + (exwFacilityId == -1 ? "" : "AND a.fk_exw_fac = " + exwFacilityId + " ")
                + "GROUP BY "
                + "a.fk_item, a.fk_unit, a.fk_exw_fac "
                + ""
                + "UNION "
                + ""
                // outflow adjustments:
                + "SELECT "
                + "'" + OUTFLOW + "' AS flow, a.fk_item AS id_item, a.fk_unit AS id_unit, a.fk_exw_fac AS id_exw_fac, "
                + "SUM(IF(a.dt < " + sqlCutoffBoy + ", a.qty, 0.0)) AS flow_prev, "
                + "-SUM(IF(a.dt >= " + sqlCutoffBoy + ", a.qty, 0.0)) AS flow_curr "
                + "FROM "
                + "m_exw_adj AS a "
                + "WHERE "
                + "NOT a.b_del " + (sqlPeriod.isEmpty() ? "" : "AND a.dt " + sqlPeriod)
                + "AND a.fk_iog_ct = " + SModSysConsts.SS_IOG_CT_OUT + " "
                + (itemId == 0 ? "" : "AND a.fk_item = " + itemId + " ")
                + (unitId == 0 ? "" : "AND a.fk_unit = " + unitId + " ")
                + (exwFacilityId == -1 ? "" : "AND a.fk_exw_fac = " + exwFacilityId + " ")
                + "GROUP BY "
                + "a.fk_item, a.fk_unit, a.fk_exw_fac "
                + ""
                + "ORDER BY "
                + "flow, id_item, id_unit, id_exw_fac";
    }
    
    /**
     * Get external warehouse stock.
     * @param session GUI session.
     * @param itemId ID of required item. Mandatory.
     * @param unitId ID of required unit. Mandatory.
     * @param exwFacilityId ID of desired External Warehouse Facility. Optional. When not needed, pass -1.
     * @param exwStart Start of stock control. Optional. When unknown, pass a <code>null</code> value.
     * @param cutoff Cutoff date for stock. Mandatory.
     * @return
     * @throws Exception
     */
    public static Stock getExwStock(final SGuiSession session, final int itemId, final int unitId, final int exwFacilityId, final Date exwStart, final Date cutoff) throws Exception {
        if (itemId == 0) {
            throw new Exception("No se proporcionó el ítem.");
        }
        
        if (unitId == 0) {
            throw new Exception("No se proporcionó la unidad.");
        }
        
        Stock stock = null;
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT "
                    + "SUM(s.flow_prev) AS open_stock, "
                    + "SUM(IF(flow = '" + INFLOW + "', s.flow_curr, 0.0)) AS flow_in, "
                    + "-SUM(IF(flow = '" + OUTFLOW + "', s.flow_curr, 0.0)) AS flow_out "
                    + "FROM ("
                    + composeSqlExwStock(itemId, unitId, exwFacilityId, exwStart, cutoff)
                    + ") AS s;"; // stock

            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                stock = new Stock(resultSet.getDouble("open_stock"), resultSet.getDouble("flow_in"), resultSet.getDouble("flow_out"));
            }
        }
        
        return stock;
    }
    
    /**
     * Stock.
     */
    public static class Stock {
        
        public double OpeningStock;
        public double Inflows;
        public double Outflows;
        public double Stock;
        
        public Stock(final double openingStock, final double inflows, final double outflows) {
            OpeningStock = openingStock;
            Inflows = inflows;
            Outflows = outflows;
            Stock = openingStock + inflows - outflows;
        }
    }
}
