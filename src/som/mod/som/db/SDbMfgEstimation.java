/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbBranchWarehouse;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbMfgEstimation extends SDbRegistryUser {
    
    private static final int SQL_MODE_GLB = 1;  // global query
    private static final int SQL_MODE_DET = 2;  // detail query

    protected int mnPkMfgEstimationId;
    protected int mnVersion;
    protected Date mtDateMfgEstimation;
    protected Date mtDateStockDay;
    protected double mdQtyFinishedGoods;
    protected double mdQtySubProducts;
    protected double mdQtyWaste;
    protected boolean mbClosed;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkUnitId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected int mnFkUserClosedId;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    protected Date mtTsUserClosed;

    protected String msXtaUnitCode;
    protected String msXtaUnitName;
    protected double mdXtaQuantityDelivery;

    protected ArrayList<SSomProductionEmpty> maChildProductionEmpties;
    protected ArrayList<SSomMfgWarehouseProduct> maChildMfgWarehouseProducts;
    protected ArrayList<SRowProductionInventory> maChildProductionInventories;
    protected ArrayList<SDbIog> maChildCanSaveIogs;

    protected ArrayList<Object[]> maItemsStockDay;
    protected ArrayList<Object[]> maItemsStockSystem;
    ///* XXX Evaluating code (Sergio Flores 2015-10-13)...
    protected ArrayList<Object[]> maItemsStockSystemCurrently;
    //*/

    protected boolean mbAuxWasClosed;
    ///* XXX Evaluating code (Sergio Flores 2015-10-13)...
    protected String msAuxStockDaySkippedItem;
    protected String msAuxStockDaySkippedUnit;
    protected String msAuxStockDaySkippedCompany;
    protected String msAuxStockDaySkippedBranch;
    protected String msAuxStockDaySkippedWarehouse;
    //*/

    public SDbMfgEstimation() {
        super(SModConsts.S_MFG_EST);

        maChildProductionEmpties = new ArrayList<>();
        maChildMfgWarehouseProducts = new ArrayList<>();
        maChildProductionInventories = new ArrayList<>();
        maChildCanSaveIogs = new ArrayList<>();

        maItemsStockDay = new ArrayList<Object[]>();
        maItemsStockSystem = new ArrayList<Object[]>();
        //maItemsStockSystemCurrently = new ArrayList<Object[]>(); XXX
        
        msAuxStockDaySkippedItem = "";
        msAuxStockDaySkippedUnit = "";
        msAuxStockDaySkippedCompany = "";
        msAuxStockDaySkippedBranch = "";
        msAuxStockDaySkippedWarehouse = "";

        initRegistry();
    }

    /*
     * Private methods:
     */
    
    private boolean isBeingClosed() {
        return !mbAuxWasClosed && mbClosed;
    }
    
    private void computeQueryStockDaySkipped(SGuiSession session) throws Exception {
        String sql = "SELECT sd.* " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sd ON " +
                "sd.id_co = w.id_co AND sd.id_cob = w.id_cob AND sd.id_wah = w.id_wah AND sd.b_del = 0 AND sd.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "' " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "sd.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "sd.id_unit = u.id_unit " +
                "WHERE w.b_del = 0 AND w.b_dis = 0 AND sd.b_stk_dif_skp = 1 AND sd.id_unit = " + mnFkUnitId + " ";

        msAuxStockDaySkippedItem = "";
        msAuxStockDaySkippedUnit = "";
        msAuxStockDaySkippedCompany = "";
        msAuxStockDaySkippedBranch = "";
        msAuxStockDaySkippedWarehouse = "";
        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {

            msAuxStockDaySkippedItem += resultSet.getString("sd.id_item") + ",";
            msAuxStockDaySkippedUnit += resultSet.getString("sd.id_unit") + ",";
            msAuxStockDaySkippedCompany += resultSet.getString("sd.id_co") + ",";
            msAuxStockDaySkippedBranch += resultSet.getString("sd.id_cob") + ",";
            msAuxStockDaySkippedWarehouse += resultSet.getString("sd.id_wah") + ",";
        }

        msAuxStockDaySkippedItem = msAuxStockDaySkippedItem.isEmpty() ? "" : msAuxStockDaySkippedItem.substring(0, msAuxStockDaySkippedItem.length()-1);
        msAuxStockDaySkippedUnit = msAuxStockDaySkippedUnit.isEmpty() ? "" : msAuxStockDaySkippedUnit.substring(0, msAuxStockDaySkippedUnit.length()-1);
        msAuxStockDaySkippedCompany = msAuxStockDaySkippedCompany.isEmpty() ? "" : msAuxStockDaySkippedCompany.substring(0, msAuxStockDaySkippedCompany.length()-1);
        msAuxStockDaySkippedBranch = msAuxStockDaySkippedBranch.isEmpty() ? "" : msAuxStockDaySkippedBranch.substring(0, msAuxStockDaySkippedBranch.length()-1);
        msAuxStockDaySkippedWarehouse = msAuxStockDaySkippedWarehouse.isEmpty() ? "" : msAuxStockDaySkippedWarehouse.substring(0, msAuxStockDaySkippedWarehouse.length()-1);
    }
    
    /**
     * Composes SQL sentence for <b>daily stock</b> according to class' members current value for "daily stock date" and "estimation unit".
     * @param mode Query mode: <code>SQL_MODE_GLB</code> = global daily stock; <code>SQL_MODE_DET</code> = daily stock detail (i.e., item + unit + warehouse).
     * @return A ready-to-use SQL sentence.
     */
    private String composeSqlSentenceStockDay(final int mode) throws Exception {
        String sql = "";
        String keys = "";
        String names = "";
        String where = "";

        // Common query filter (without trailing blank space):
        
        where = "sd.b_del = 0 AND sd.b_stk_dif_skp = 0 AND " +
                "sd.id_unit = " + mnFkUnitId + " AND sd.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "'";
        
        // Compose SQL sentence (without trailing blank space):
        
        sql = "SELECT SUM(sd.stk_day) AS f_stk_day";
        
        switch (mode) {
            case SQL_MODE_GLB:
                // Get global daily stock:
                
                sql += " FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sd " +
                        "WHERE " + where + " ";
                break;
                
            case SQL_MODE_DET:
                // Get all warehouses' detail daily stock:
                
                keys = "sd.id_item, sd.id_unit, sd.id_co, sd.id_cob, sd.id_wah, i.fk_item_src_1_n, i.fk_item_src_2_n, w.fk_wah_tp, w.fk_line";
                names = "i.name, i.code, u.name, u.code, c.code, cb.code, w.code, wt.code, pl.code, pl.name";
                
                sql += ", " + keys + ", " + names;
                
                sql += " FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_PROD_LINES) + " AS pl ON " +
                        "w.fk_line = pl.id_line " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CS_WAH_TP) + " AS wt ON " +
                        "w.fk_wah_tp = wt.id_wah_tp " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS cb ON " +
                        "w.id_co = cb.id_co AND w.id_cob = cb.id_cob " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_CO) + " AS c ON " +
                        "w.id_co = c.id_co " +
                        "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sd ON " +
                        "w.id_co = sd.id_co AND w.id_cob = sd.id_cob AND w.id_wah = sd.id_wah " +
                        "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                        "sd.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +  // finished goods only
                        "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                        "sd.id_unit = u.id_unit " +
                        "WHERE " + where + " AND " +
                        "w.b_del = 0 AND w.b_dis = 0 " +
                        "GROUP BY " + keys + ", " + names + " " +
                        "ORDER BY " + names + ", " + keys + " ";
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return sql;
    }

    /**
     * Composes SQL sentence for <b>system stock</b> according to class' members current value for "daily stock date", "manufacturing estimation date" and "estimation unit".
     * @param mode Query mode: <code>SQL_MODE_GLB</code> = global system stock; <code>SQL_MODE_DET</code> = system stock detail (i.e., item + unit + warehouse).
     * @return A ready-to-use SQL sentence.
     */
    private String composeSqlSentenceStockSystem(final int mode) throws Exception {
        String sql = "";
        String keys = "";
        String names = "";
        String where = "";
        String qryExcludeWahSkipped = "";
        String qryExcludeMfgAsigned = "";
        
        boolean by_warehouse_product = false;

        // Common query filter (without trailing blank space):
        
        where = "sd.b_del = 0 AND sd.b_stk_dif_skp = 0 AND " +
                "sd.id_unit = " + mnFkUnitId + " AND sd.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "'";
        
        // Compose SQL sentence (without trailing blank space):
        
        sql = "SELECT SUM(sd.stk_day) AS f_stk_day";
        
        switch (mode) {
            case SQL_MODE_GLB:
                // Get global daily stock:
                
                sql += " FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sd " +
                        "WHERE " + where + " ";
                break;
                
            case SQL_MODE_DET:
                // Get all warehouses' detail daily stock:
                
                keys = "sd.id_item, sd.id_unit, sd.id_co, sd.id_cob, sd.id_wah, i.fk_item_src_1_n, i.fk_item_src_2_n, w.fk_wah_tp";
                names = "i.name, i.code, u.name, u.code, c.code, cb.code, w.code, wt.code";
                
                sql += ", " + keys + ", " + names;
                
                sql += " FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CS_WAH_TP) + " AS wt ON " +
                        "w.fk_wah_tp = wt.id_wah_tp " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS cb ON " +
                        "w.id_co = cb.id_co AND w.id_cob = cb.id_cob " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_CO) + " AS c ON " +
                        "w.id_co = c.id_co " +
                        "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sd ON " +
                        "w.id_co = sd.id_co AND w.id_cob = sd.id_cob AND w.id_wah = sd.id_wah " +
                        "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                        "sd.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +  // finished goods only
                        "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                        "sd.id_unit = u.id_unit " +
                        "WHERE " + where + " AND " +
                        "w.b_del = 0 AND w.b_dis = 0 " +
                        "GROUP BY " + keys + " " + names + " " +
                        "ORDER BY " + names + " " + keys + " ";
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        ////////////////////////////////////////////////////////////////////////
        
        qryExcludeWahSkipped = "SELECT DISTINCT CONCAT(id_co, id_cob, id_wah) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " " +
                "WHERE b_del = 0 AND b_stk_dif_skp = 1 AND " +
                "id_unit = " + mnFkUnitId + " AND dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "' " +
                "ORDER BY id_co, id_cob, id_wah ";
        
        qryExcludeMfgAsigned = "SELECT SUM(ss.mov_in - ss.mov_out) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS ss " +
                "WHERE ss.b_del = 0 AND ss.b_sys = 1 AND s.id_unit = " + mnFkUnitId + " AND " +
                "s.id_co = ss.id_co AND s.id_cob = ss.id_cob AND s.id_wah = ss.id_wah AND s.id_div = ss.id_div AND s.id_item = ss.id_item AND s.id_unit = ss.id_unit AND " +
                "ss.fk_iog_ct = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0] + " AND " +
                "ss.fk_iog_cl = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1] + " AND " +
                "ss.fk_iog_tp = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2] + " AND " +
                "ss.dt = DATE_SUB('" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "', INTERVAL 1 DAY";

        // Stock system global, by warehouse product or by warehouse product to currently day:

        sql = "SELECT COALESCE(SUM(s.mov_in - s.mov_out), 0) " +
                " - " +
                "COALESCE(()), 0) AS f_stock " + 
                (by_warehouse_product ? ", s.id_item, s.id_unit, s.id_co, s.id_cob, s.id_wah, i.id_item, i.name, i.code, u.code, COALESCE(i.fk_item_src_1_n, 0) AS f_fk_item_src_1_n, " +
                "COALESCE(i.fk_item_src_2_n, 0) AS f_fk_item_src_2_n, cb.code, w.code, w.cap_real_lt, w.fk_wah_tp, wt.code " : "") + ", " +
            "COALESCE((SELECT SUM(s.mov_in - s.mov_out) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s ON " +
                "g.id_iog = s.fk_iog " +
                "WHERE g.b_del = 0 AND s.b_del = 0 AND g.fk_mfg_est_n = " + mnPkMfgEstimationId + " AND " +
                "g.fk_mfg_est_ver_n = " + mnVersion + " " + (by_warehouse_product ? "AND g.fk_item = i.id_item AND g.fk_unit = u.id_unit AND g.fk_unit = " + mnFkUnitId + " " : "") + "), 0) AS f_stock_dly " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w ON " +
            "s.id_co = w.id_co AND s.id_cob = w.id_cob AND s.id_wah = w.id_wah AND " +
            "w.fk_wah_tp IN (" + SModSysConsts.CS_WAH_TP_TAN + ", " + SModSysConsts.CS_WAH_TP_TAN_MFG + ") " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS cb ON " +
            "w.id_co = cb.id_co AND w.id_cob = cb.id_cob " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CS_WAH_TP) + " AS wt ON " +
            "w.fk_wah_tp = wt.id_wah_tp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
            "s.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
            "s.id_unit = u.id_unit AND s.id_unit = " + mnFkUnitId + " " +
            "WHERE s.b_del = 0 AND s.id_year = " + SLibTimeUtils.digestYear(mtDateStockDay)[0] + " AND s.dt < '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "' " +
            (by_warehouse_product ? "GROUP BY s.id_year, s.id_item, s.id_unit, s.id_co, s.id_cob, s.id_wah " : "") + " " +
            "HAVING f_stock <> 0 " +
            (by_warehouse_product ? "ORDER BY w.cap_real_lt, i.code, i.name, i.id_item " : "") + " ";
            
        return sql;
    }
    
    private String computeQueryStockDaySystem(final boolean stk_day, final boolean by_warehouse_product, final boolean stk_sys_currently) {
        String sql = "";

        if (stk_day) {

            // Daily stock global or by warehouse product:

            sql = "SELECT COALESCE(SUM(sd.stk_day), 0) AS f_stk_day " + (by_warehouse_product ? ", " +
                    "sd.id_item, sd.id_unit, sd.id_co, sd.id_cob, sd.id_wah, i.name, i.code, u.code, COALESCE(i.fk_item_src_1_n, 0) AS f_fk_item_src_1_n, " +
                    "COALESCE(i.fk_item_src_2_n, 0) AS f_fk_item_src_2_n, cb.code, w.code, w.cap_real_lt, w.fk_wah_tp, w.fk_line, wt.code, pl.code, pl.name " : "") + " " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_PROD_LINES) + " AS pl ON " +
                "w.fk_line = pl.id_line " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS cb ON " +
                "w.id_co = cb.id_co AND w.id_cob = cb.id_cob " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CS_WAH_TP) + " AS wt ON " +
                "w.fk_wah_tp = wt.id_wah_tp " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sd ON " +
                "sd.id_co = w.id_co AND sd.id_cob = w.id_cob AND sd.id_wah = w.id_wah AND sd.b_del = 0 AND sd.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "' " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "sd.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "sd.id_unit = u.id_unit " +
                "WHERE w.b_del = 0 AND w.b_dis = 0 AND sd.b_stk_dif_skp = 0 AND sd.id_unit = " + mnFkUnitId + " " +
                (by_warehouse_product ?
                    "GROUP BY sd.id_year, sd.id_item, sd.id_unit, sd.id_co, sd.id_cob, sd.id_wah " +
                    "ORDER BY w.cap_real_lt, i.code, i.name, i.id_item " : "") + " ";
        }
        else {

            // Stock system global, by warehouse product or by warehouse product to currently day:

            sql = "SELECT " +
//XXX
                (msAuxStockDaySkippedItem.isEmpty() ? "SUM(s.mov_in - s.mov_out) " :
                    "SUM(IF((s.id_item IN (" + msAuxStockDaySkippedItem + ") AND s.id_unit IN (" + msAuxStockDaySkippedUnit + ") AND " +
                    "s.id_co IN (" + msAuxStockDaySkippedCompany + ") AND s.id_cob IN (" + msAuxStockDaySkippedBranch + ") AND s.id_wah IN (" + msAuxStockDaySkippedWarehouse + ")), 0, s.mov_in - s.mov_out))") + " - " +
                "COALESCE((SELECT SUM(ss.mov_in - ss.mov_out) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS ss " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS si ON " +
                "ss.fk_iog = si.id_iog AND si.b_sys = 1 " +
                "WHERE ss.b_del = 0 AND ss.b_sys = 1 AND s.id_co = ss.id_co AND s.id_cob = ss.id_cob AND s.id_wah = ss.id_wah AND s.id_div = ss.id_div AND s.id_item = ss.id_item AND " +
                "s.id_unit = ss.id_unit AND s.id_unit = " + mnFkUnitId + " AND " +
                "ss.fk_iog_ct = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0] + " AND ss.fk_iog_cl = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1] + " AND " +
                "ss.fk_iog_tp = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2] + " AND " +
                "ss.dt IN " +
                "(DATE_SUB('" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "', INTERVAL 1 DAY)" +
                (stk_sys_currently ? ", '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "'" : "") + ")" +
                "), 0) AS f_stock " + (by_warehouse_product ? ", " +
                "s.id_item, s.id_unit, s.id_co, s.id_cob, s.id_wah, i.id_item, i.name, i.code, u.code, COALESCE(i.fk_item_src_1_n, 0) AS f_fk_item_src_1_n, " +
                "COALESCE(i.fk_item_src_2_n, 0) AS f_fk_item_src_2_n, cb.code, w.code, w.cap_real_lt, w.fk_wah_tp, w.fk_line, wt.code, pl.code, pl.name " : "") + " " +
                ", COALESCE((SELECT SUM(s.mov_in - s.mov_out) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s ON " +
                "g.id_iog = s.fk_iog " +
                "WHERE g.b_del = 0 AND s.b_del = 0 AND g.fk_mfg_est_n = " + mnPkMfgEstimationId + " AND " +
                    "g.fk_mfg_est_ver_n = " + mnVersion + " " + (by_warehouse_product ? "AND g.fk_item = i.id_item AND g.fk_unit = u.id_unit AND g.fk_unit = " + mnFkUnitId + " " : "") + "), 0) AS f_stock_dly " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w ON " +
                "s.id_co = w.id_co AND s.id_cob = w.id_cob AND s.id_wah = w.id_wah AND " +
                "w.fk_wah_tp IN (" + SModSysConsts.CS_WAH_TP_TAN + ", " + SModSysConsts.CS_WAH_TP_TAN_MFG + ") " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_PROD_LINES) + " AS pl ON " +
                "w.fk_line = pl.id_line " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS cb ON " +
                "w.id_co = cb.id_co AND w.id_cob = cb.id_cob " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CS_WAH_TP) + " AS wt ON " +
                "w.fk_wah_tp = wt.id_wah_tp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
                "s.id_div = d.id_div " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "s.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "s.id_unit = u.id_unit AND s.id_unit = " + mnFkUnitId + " " +
                "WHERE s.b_del = 0 AND s.id_year = " + SLibTimeUtils.digestYear(mtDateStockDay)[0] + " AND s.dt < '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "' " +
                (by_warehouse_product ? "GROUP BY s.id_year, s.id_item, s.id_unit, s.id_co, s.id_cob, s.id_wah " : "") + " " +
                "HAVING f_stock <> 0 " +
                (by_warehouse_product ? "ORDER BY w.cap_real_lt, i.code, i.name, i.id_item " : "") + " ";
        }

        return sql;
    }

    private double[] productionEstimateGlobal(final SGuiSession session) {
        String sql = "";
        ResultSet resultSet = null;
        double dStockDayQuantity = 0;
        double dStockSystemQuantity = 0;
        double dStockQuantityDelivery = 0;

        try {
            /* XXX Evaluating code (Sergio Flores 2015-10-13)...
            computeQueryStockDaySkipped(session);
            */
            computeQueryStockDaySkipped(session);

            //sql = computeQueryStockDaySystem(true, false, false); (Sergio Flores 2015-10-13)
            sql = composeSqlSentenceStockDay(SQL_MODE_GLB);// stock diary
            resultSet = session.getStatement().executeQuery(sql);
            if (!resultSet.next()) {
                dStockDayQuantity = 0;
            }
            else {
                dStockDayQuantity = resultSet.getDouble(1);
            }
            
            sql = computeQueryStockDaySystem(false, false, false);
            resultSet = session.getStatement().executeQuery(sql);
            if (!resultSet.next()) {
                dStockSystemQuantity = 0;
            }
            else {
                dStockSystemQuantity = resultSet.getDouble(1);
                dStockQuantityDelivery = resultSet.getDouble(2);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        return new double[] { dStockDayQuantity, dStockSystemQuantity, dStockQuantityDelivery };
    }

    private void productionEstimateByWarehouseProduct(final SGuiSession session) {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;

        maItemsStockDay.clear();
        maItemsStockSystem.clear();
        //maItemsStockSystemCurrently.clear();  XXX

        try {
            //sql = computeQueryStockDaySystem(true, true, false);  (Sergio Flores 2015-10-13)
            sql = composeSqlSentenceStockDay(SQL_MODE_DET);
            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                maItemsStockDay.add(new Object[] {
                    resultSet.getInt("sd.id_item"), resultSet.getString("i.name"), resultSet.getString("i.code"),
                    resultSet.getDouble("f_stk_day"), resultSet.getString("u.code"), resultSet.getString("cb.code"),
                    resultSet.getString("w.code"), resultSet.getInt("sd.id_co"), resultSet.getInt("sd.id_cob"),
                    resultSet.getInt("sd.id_wah"), resultSet.getInt("w.fk_wah_tp"), resultSet.getString("wt.code"),
                    resultSet.getInt("fk_item_src_1_n"), resultSet.getInt("fk_item_src_2_n"), resultSet.getString("pl.code"), 
                    resultSet.getString("pl.name"), resultSet.getInt("w.fk_line") });
            }

            sql = computeQueryStockDaySystem(false, true, false);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                maItemsStockSystem.add(new Object[] {
                    resultSet.getInt("s.id_item"), resultSet.getString("i.name"), resultSet.getString("i.code"),
                    resultSet.getDouble("f_stock"), resultSet.getString("u.code"), resultSet.getString("f_stock_dly"),
                    resultSet.getString("cb.code"), resultSet.getString("w.code"), resultSet.getInt("s.id_co"),
                    resultSet.getInt("s.id_cob"), resultSet.getInt("s.id_wah"), resultSet.getInt("w.fk_wah_tp"),
                    resultSet.getString("wt.code"), resultSet.getInt("f_fk_item_src_1_n"), resultSet.getInt("f_fk_item_src_2_n"),
                    resultSet.getString("pl.code"), resultSet.getString("pl.name"), resultSet.getInt("w.fk_line")});
            }

            /* XXX
            sql = computeQueryStockDaySystem(false, true, true);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                maItemsStockSystemCurrently.add(new Object[] {
                    resultSet.getInt("s.id_item"), resultSet.getString("i.name"), resultSet.getString("i.code"),
                    resultSet.getDouble("f_stock"), resultSet.getString("u.code"), resultSet.getString("cb.code"),
                    resultSet.getString("w.code"), resultSet.getInt("s.id_co"), resultSet.getInt("s.id_cob"),
                    resultSet.getInt("s.id_wah"), resultSet.getInt("w.fk_wah_tp"), resultSet.getString("wt.code"),
                    resultSet.getString("f_fk_item_src_1_n"), resultSet.getString("f_fk_item_src_2_n") });
            }
            */
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    public void obtainManufacturedProducts(SGuiSession session) throws SQLException {
        boolean existStockDayItem = false;
        double quantityDelivery = 0;
        SSomMfgWarehouseProduct mfgWarehouseProduct = null;

        maChildMfgWarehouseProducts.clear();
        productionEstimateByWarehouseProduct(session);
        for (Object[] oItemStockDay : maItemsStockDay) {
            for (Object[] oItemStockSystem : maItemsStockSystem) {

                // Compare items:

                existStockDayItem = false;
                if ((Integer) oItemStockDay[7] == (Integer) oItemStockSystem[8] && // Company
                    (Integer) oItemStockDay[8] == (Integer) oItemStockSystem[9] && // Branch
                    (Integer) oItemStockDay[9] == (Integer) oItemStockSystem[10] && // Warehouse
                    (Integer) oItemStockDay[0] == (Integer) oItemStockSystem[0]) { // Item
                    // Compare quantity:

                    existStockDayItem = true;
                    if (((Double) oItemStockDay[3]).intValue() > ((Double) oItemStockSystem[3]).intValue() ||
                            (((Double) oItemStockDay[3]).intValue() < ((Double) oItemStockSystem[3]).intValue() &&
                            (Integer) oItemStockSystem[11] == SModSysConsts.CS_WAH_TP_TAN_MFG)) {

                        // Calculate production:

                        oItemStockSystem[3] = (Double) oItemStockDay[3] - (Double) oItemStockSystem[3];

                        mfgWarehouseProduct = new SSomMfgWarehouseProduct();
                        mfgWarehouseProduct.setPkItemId((Integer) oItemStockSystem[0]);
                        mfgWarehouseProduct.setPkUnitId(mnFkUnitId);
                        mfgWarehouseProduct.setPkCompanyId((Integer) oItemStockSystem[8]);
                        mfgWarehouseProduct.setPkBranchId((Integer) oItemStockSystem[9]);
                        mfgWarehouseProduct.setPkWarehouseId((Integer) oItemStockSystem[10]);
                        mfgWarehouseProduct.setItem((String) oItemStockSystem[1]);
                        mfgWarehouseProduct.setItemCode((String) oItemStockSystem[2]);
                        mfgWarehouseProduct.setUnitCode((String) oItemStockSystem[4]);
                        mfgWarehouseProduct.setQuantity((Double) oItemStockSystem[3]);
                        mfgWarehouseProduct.setBranchCode((String) oItemStockSystem[6]);
                        mfgWarehouseProduct.setWarehouseCode((String) oItemStockSystem[7]);
                        mfgWarehouseProduct.setWarehouseTypeCode((String) oItemStockSystem[12]);
                        mfgWarehouseProduct.setFkWarehouseTypeId((Integer) oItemStockSystem[11]);
                        mfgWarehouseProduct.setFkItemSource1Id_n((Integer) oItemStockSystem[13]);
                        mfgWarehouseProduct.setFkItemSource2Id_n((Integer) oItemStockSystem[14]);
                        mfgWarehouseProduct.setProductionLineCode((String) oItemStockSystem[15]);
                        mfgWarehouseProduct.setProductionLine((String) oItemStockSystem[16]);
                        mfgWarehouseProduct.setFkProductionLineId((Integer) oItemStockSystem[17]);

                        quantityDelivery = obtainQuantityDelivery(session,
                                mfgWarehouseProduct.getPkItemId(),
                                mfgWarehouseProduct.getPkUnitId(),
                                new int[] {
                                    mfgWarehouseProduct.getPkCompanyId(), mfgWarehouseProduct.getPkBranchId(), mfgWarehouseProduct.getPkWarehouseId()});
                        mfgWarehouseProduct.setQuantityDelivery(quantityDelivery);

                        if (((Integer) oItemStockSystem[11] == SModSysConsts.CS_WAH_TP_TAN_MFG &&
                                SLibUtils.round(mfgWarehouseProduct.getQuantityDelivery(), SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()) !=
                                SLibUtils.round(mfgWarehouseProduct.getQuantity(), SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits())) ||
                                SLibUtils.round(mfgWarehouseProduct.getQuantityDelivery(), SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()) <
                                SLibUtils.round(mfgWarehouseProduct.getQuantity(), SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits())) {
                            maChildMfgWarehouseProducts.add(mfgWarehouseProduct);
                        }
                    }

                    break;
                }
            }

            if (!existStockDayItem && oItemStockDay[1] != null) {
                mfgWarehouseProduct = new SSomMfgWarehouseProduct();

                mfgWarehouseProduct.setPkItemId((Integer) oItemStockDay[0]);
                mfgWarehouseProduct.setPkUnitId(mnFkUnitId);
                mfgWarehouseProduct.setPkCompanyId((Integer) oItemStockDay[7]);
                mfgWarehouseProduct.setPkBranchId((Integer) oItemStockDay[8]);
                mfgWarehouseProduct.setPkWarehouseId((Integer) oItemStockDay[9]);
                mfgWarehouseProduct.setItem((String) oItemStockDay[1]);
                mfgWarehouseProduct.setItemCode((String) oItemStockDay[2]);
                mfgWarehouseProduct.setUnitCode((String) oItemStockDay[4]);
                mfgWarehouseProduct.setQuantity((Double) oItemStockDay[3]);
                mfgWarehouseProduct.setBranchCode((String) oItemStockDay[5]);
                mfgWarehouseProduct.setWarehouseCode((String) oItemStockDay[6]);
                mfgWarehouseProduct.setQuantityDelivery(0d);
                mfgWarehouseProduct.setWarehouseTypeCode((String) oItemStockDay[11]);
                mfgWarehouseProduct.setFkWarehouseTypeId((Integer) oItemStockDay[10]);
                mfgWarehouseProduct.setFkItemSource1Id_n((Integer) oItemStockDay[12]);
                mfgWarehouseProduct.setFkItemSource2Id_n((Integer) oItemStockDay[13]);
                mfgWarehouseProduct.setProductionLineCode((String) oItemStockDay[14]);
                mfgWarehouseProduct.setProductionLine((String) oItemStockDay[15]);
                mfgWarehouseProduct.setFkProductionLineId((Integer) oItemStockDay[16]);

                if (mfgWarehouseProduct.getQuantityDelivery() < mfgWarehouseProduct.getQuantity()) {
                    maChildMfgWarehouseProducts.add(mfgWarehouseProduct);
                }
            }
        }
    }

    private double obtainQuantityDelivery(final SGuiSession session, final int itemId, final int unitId, final int[] warehouseId) throws SQLException {
        double quantityDelivery = 0;
        String sql = "";

        ResultSet resultSet = null;
        Statement statement = null;

        sql = "SELECT SUM(s.mov_in - s.mov_out) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s ON " +
            "g.id_iog = s.fk_iog " +
            "WHERE g.b_del = 0 AND s.b_del = 0 AND g.fk_mfg_est_n = " + mnPkMfgEstimationId + " AND g.fk_mfg_est_ver_n = " + mnVersion + " AND " +
            "s.id_item = " + itemId + " AND s.id_unit = " + unitId + " AND s.id_co = " + warehouseId[0] + " AND s.id_cob = " + warehouseId[1] + " AND " +
            "s.id_wah = " + warehouseId[2] + " " +
            "GROUP BY s.id_year, s.id_item, s.id_unit, s.id_co, s.id_cob, s.id_wah";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {

            quantityDelivery = resultSet.getDouble(1);
        }

        return quantityDelivery;
    }

    /**
     * XXX Evaluating code (Sergio Flores 2015-10-13)...
     */
    private void obtainProductionEstimateOpen(final SGuiSession session) throws Exception {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        
        statement = session.getDatabase().getConnection().createStatement();
        
        sql = "SELECT COALESCE(id_mfg_est, 0) AS f_mfg_est, COALESCE(MAX(ver), 0) AS f_ver " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST) + " " +
                "WHERE b_clo = 0 AND b_del = 0 AND dt_stk_day = '" +  SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "' " +
                "ORDER BY id_mfg_est ";
        resultSet = statement.executeQuery(sql);
        if (!resultSet.next() || resultSet.getInt("f_mfg_est") == 0) {

            // Create mfg estimation:

            save(session);
        }
        else {

            read(session, new int[] { resultSet.getInt("f_mfg_est") });
        }
        
        this.setRegistryNew(false);
    }

    /*
     * Public methods:
     */

    public void setPkMfgEstimationId(int n) { mnPkMfgEstimationId = n; }
    public void setVersion(int n) { mnVersion = n; }
    public void setDateMfgEstimation(Date t) { mtDateMfgEstimation = t; }
    public void setDateStockDay(Date t) { mtDateStockDay = t; }
    public void setQtyFinishedGoods(double d) { mdQtyFinishedGoods = d; }
    public void setQtySubProducts(double d) { mdQtySubProducts = d; }
    public void setQtyWaste(double d) { mdQtyWaste = d; }
    public void setClosed(boolean b) { mbClosed = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    public void setTsUserClosed(Date t) { mtTsUserClosed = t; }

    public int getPkMfgEstimationId() { return mnPkMfgEstimationId; }
    public int getVersion() { return mnVersion; }
    public Date getDateMfgEstimation() { return mtDateMfgEstimation; }
    public Date getDateStockDay() { return mtDateStockDay; }
    public double getQtyFinishedGoods() { return mdQtyFinishedGoods; }
    public double getQtySubProducts() { return mdQtySubProducts; }
    public double getQtyWaste() { return mdQtyWaste; }
    public boolean isClosed() { return mbClosed; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    public Date getTsUserClosed() { return mtTsUserClosed; }

    public void setXtaUnitCode(String s) { msXtaUnitCode = s; }
    public void setXtaUnitName(String s) { msXtaUnitName = s; }
    public void setXtaQuantityDelivery(double d) { mdXtaQuantityDelivery = d; }
    
    public String getXtaUnitCode() { return msXtaUnitCode; }
    public String getXtaUnitName() { return msXtaUnitName; }
    public double getXtaQuantityDelivery() { return mdXtaQuantityDelivery; }
    
    public ArrayList<SSomProductionEmpty> getChildProductionEmpties() { return maChildProductionEmpties; }
    public ArrayList<SSomMfgWarehouseProduct> getChildMfgWarehouseProducts() { return maChildMfgWarehouseProducts; }
    public ArrayList<SRowProductionInventory> getChildProductionInventories() { return maChildProductionInventories; }
    public ArrayList<SDbIog> getChildCanSaveIogs() { return maChildCanSaveIogs; }
    
    /* XXX Evaluating code (Sergio Flores 2015-10-13)...
    public ArrayList<Object[]> getItemsStockDay() { return maItemsStockDay; }
    public ArrayList<Object[]> getItemsStockSystem() { return maItemsStockSystem; }
    public ArrayList<Object[]> getItemsStockSystemCurrently() { return maItemsStockSystemCurrently; }
    */
    
    /**
     * XXX Evaluating code (Sergio Flores 2015-10-13)...
     */
    public boolean validateEstimateProduction(SGuiSession session) throws Exception {
        boolean res = true;
        double[] adStockDaySystem = null;

        msQueryResult = "";
        mnQueryResultId = SLibConsts.UNDEFINED;

        // Obtain production estimate open:

        obtainProductionEstimateOpen(session);  // evaluating: just 1 class call (Sergio Flores 2015-10-13)

        // Validate if production estimate global is correct:

        adStockDaySystem = productionEstimateGlobal(session);   // evaluating: 2 class calls (Sergio Flores 2015-10-13)

        // Production estimate:

        mdQtyFinishedGoods = adStockDaySystem[0] - adStockDaySystem[1];
        mdXtaQuantityDelivery = adStockDaySystem[2];

        return res;
    }

    public void validateProductionEstimateByProduct(SGuiSession session) {
        double maxStockDiffM = ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getMaximumStockDifferenceM();
        
        try {
            productionEstimateByWarehouseProduct(session);
            for (Object[] oItemStockDay : maItemsStockDay) {
                for (Object[] oItemStockSystem : maItemsStockSystem) {

                    // Compare warehouse item:

                    if ((Integer) oItemStockDay[7] == (Integer) oItemStockSystem[8] && // Company
                        (Integer) oItemStockDay[8] == (Integer) oItemStockSystem[9] && // Branch
                        (Integer) oItemStockDay[9] == (Integer) oItemStockSystem[10] && // Warehouse
                        (Integer) oItemStockDay[0] == (Integer) oItemStockSystem[0]) { // Item

                        // Compare quantity:
                        SDbBranchWarehouse whs = new SDbBranchWarehouse();
                        int[] whsPk = {(Integer) oItemStockSystem[8], (Integer) oItemStockSystem[9], (Integer) oItemStockSystem[10]};
                        whs.read(session, whsPk);
                        double maxStockDiffVolume = Math.PI * Math.pow(whs.getDimensionBase()/2, 2) * maxStockDiffM;
                        
                        SDbItem item = new SDbItem();
                        item.read(session, new int[] { (Integer) oItemStockSystem[0] });
                        double maxStockDiffKg = maxStockDiffVolume * item.getDensity();
                        
                        double stockDifference = ((Double) oItemStockSystem[3]).intValue() - ((Double) oItemStockDay[3]).intValue();
                        // la existencia del sistema es mayor a la existencia física
                        if (stockDifference > maxStockDiffKg && (Integer) oItemStockSystem[11] == SModSysConsts.CS_WAH_TP_TAN) {

                            msQueryResult = "No se puede estimar la producción debido a que la existencia de sistema (" +
                                    SLibUtils.DecimalFormatValue2D.format((Double) oItemStockSystem[3]) + " " + (String) oItemStockSystem[4] + ") del ítem: \n'" +
                                    (String) oItemStockSystem[1] + " (" + (String) oItemStockSystem[2] + ")' es mayor a la existencia física (" +
                                    SLibUtils.DecimalFormatValue2D.format((Double) oItemStockDay[3]) + " " +  (String) oItemStockSystem[4] + ") en el almacén '" + (String) oItemStockSystem[7] + "'.";
                            mnQueryResultId = SDbConsts.READ_ERROR;
                            break;
                        }
                    }
                }

                if (mnQueryResultId == SDbConsts.READ_ERROR) {
                    break;
                }
                else {
                    mnQueryResultId = SDbConsts.READ_OK;
                }
            }
        }
        catch (Exception e) {
            
        }
    }
    
    /**
     * Llena los arrayList de maChildProductionEmpties y maChildProductionInventories
     * 
     * @param session
     * @throws SQLException
     * @throws Exception 
     */
    public void computeProductionEstimate(SGuiSession session) throws SQLException, Exception {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        SDbItem itemRawMaterial = null;
        SDbItem item = null;
        SDbUnit unit = null;
        SDbStockDay stockDay = null;
        SSomProductionEmpty productionEmpty = null;
        SRowProductionInventory productionInventory = null;

        mdXtaQuantityDelivery = productionEstimateGlobal(session)[2];

        // Obtain manufactured items:

        obtainManufacturedProducts(session);

        // Daily stock by warehouse:

        sql = "SELECT w.id_co, w.id_cob, w.id_wah, sd.id_year, sd.id_item, sd.id_unit, sd.id_co, sd.id_cob, sd.id_wah, sd.b_stk_dif_skp, sd.id_day, " +
            "COALESCE(sd.emp, 0) AS f_emp, sd.b_emp, sd.stk_day, sd.cull, sd.dt, w.code, w.name, w.dim_heig, w.cap_real_lt, i.name, " +
            "i.code, i.den, u.code, i.fk_item_tp, i.fk_item_rm_n, i.fk_item_bp_n, i.fk_item_cu_n " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sd ON " +
            "sd.id_co = w.id_co AND sd.id_cob = w.id_cob AND sd.id_wah = w.id_wah AND sd.b_del = 0 AND sd.b_stk_dif_skp = 0 AND " +
            "sd.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "' " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
            "sd.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
            "sd.id_unit = u.id_unit " +
            "WHERE w.b_del = 0 AND w.b_dis = 0 AND w.fk_wah_tp IN (" + SModSysConsts.CS_WAH_TP_TAN + ", " + SModSysConsts.CS_WAH_TP_TAN_MFG + ") AND sd.id_unit = " + mnFkUnitId + " " +
            "ORDER BY sd.id_day; ";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            productionEmpty = new SSomProductionEmpty();

            productionEmpty.setPkWarehouseCompanyId(resultSet.getInt("w.id_co"));
            productionEmpty.setPkWarehouseBranchId(resultSet.getInt("w.id_cob"));
            productionEmpty.setPkWarehouseWarehouseId(resultSet.getInt("w.id_wah"));

            productionEmpty.setXtaPkItemId(resultSet.getInt("sd.id_item"));
            productionEmpty.setXtaPkUnitId(resultSet.getInt("sd.id_unit"));
            productionEmpty.setXtaEmpty(resultSet.getBoolean("sd.b_emp"));
            productionEmpty.setXtaStockDifferenceSkipped(resultSet.getBoolean("sd.b_stk_dif_skp"));
            productionEmpty.setXtaEmptiness(resultSet.getDouble("f_emp"));
            productionEmpty.setXtaEmptyKg(resultSet.getDouble("sd.stk_day"));
            productionEmpty.setXtaCull(resultSet.getDouble("sd.cull"));
            productionEmpty.setXtaWarehouse(resultSet.getString("w.name"));
            productionEmpty.setXtaWarehouseCode(resultSet.getString("w.code"));
            productionEmpty.setXtaWarehouseHeight(resultSet.getDouble("w.dim_heig"));
            productionEmpty.setXtaCapacityRealLiter(resultSet.getDouble("w.cap_real_lt"));
            productionEmpty.setXtaFGItem(resultSet.getString("i.name"));
            productionEmpty.setXtaFGItemCode(resultSet.getString("i.code"));
            productionEmpty.setXtaFGItemUnitCode(resultSet.getString("u.code"));
            productionEmpty.setXtaFGItemDensity(resultSet.getDouble("i.den"));

            // Read raw material item, read by product percentage, read waste percentage and read warehouse:

            itemRawMaterial = new SDbItem();
            unit = new SDbUnit();
            productionEmpty.setXtaFkRMItemId(resultSet.getInt("i.fk_item_rm_n"));
            if (!resultSet.wasNull()) {

                itemRawMaterial.read(session, new int[] { productionEmpty.getXtaFkRMItemId() });
                if (itemRawMaterial.getQueryResultId() == SDbConsts.READ_OK) {

                    unit.read(session, new int[] { itemRawMaterial.getFkUnitId() });
                    if (unit.getQueryResultId() == SDbConsts.READ_OK) {

                        // Validate if raw material is raw material type:

                        if (itemRawMaterial.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_RM) {
                            msQueryResult = "La materia prima '" + itemRawMaterial.getName() + " (" + itemRawMaterial.getCode() + ")' asignada al producto terminado \n" +
                                "'" + productionEmpty.getXtaFGItem() + " (" + productionEmpty.getXtaFGItemCode() + ")' no es de tipo '" +
                                session.readField(SModSysConsts.SS_ITEM_TP_RM, new int[] { itemRawMaterial.getFkInputTypeId() }, SDbRegistryUser.FIELD_NAME) + "'.";
                            mnQueryResultId = SDbConsts.READ_ERROR;
                            break;
                        }
                        else if (itemRawMaterial.getMfgFinishedGoodPercentage() <= 0) {

                            // Validate if percentage of finished good > 0:

                            msQueryResult = "El producto terminado '" + productionEmpty.getXtaFGItem() + " (" + productionEmpty.getXtaFGItemCode() + ")' de la materia prima \n" +
                                "'" + itemRawMaterial.getName() + " (" + itemRawMaterial.getCode() + ")' debe tener asignado un porcentaje mayor a 0.";
                            mnQueryResultId = SDbConsts.READ_ERROR;
                            break;
                        }

                        productionEmpty.setXtaRMItem(itemRawMaterial.getName());
                        productionEmpty.setXtaRMItemCode(itemRawMaterial.getCode());
                        productionEmpty.setXtaRMItemUnitCode(unit.getCode());
                        productionEmpty.setXtaFGPercentage(itemRawMaterial.getMfgFinishedGoodPercentage());
                        productionEmpty.setXtaBPPercentage(itemRawMaterial.getMfgByproductPercentage());
                        productionEmpty.setXtaCUPercentage(itemRawMaterial.getMfgCullPercentage());

                        productionEmpty.setXtaFkRMItemUnitId(itemRawMaterial.getFkUnitId());
                        productionEmpty.setXtaFkRMWarehouseCompanyId(itemRawMaterial.getFkWarehouseCompanyId_n());
                        productionEmpty.setXtaFkRMWarehouseBranchId(itemRawMaterial.getFkWarehouseBranchId_n());
                        productionEmpty.setXtaFkRMWarehouseWarehouseId(itemRawMaterial.getFkWarehouseWarehouseId_n());
                    }
                }
            }

            // Read subgood item and warehouse:

            item = new SDbItem();
            productionEmpty.setXtaFkBPItemId(resultSet.getInt("i.fk_item_bp_n"));
            if (!resultSet.wasNull()) {

                item.read(session, new int[] { productionEmpty.getXtaFkBPItemId() });
                if (item.getQueryResultId() == SDbConsts.READ_OK) {

                    unit.read(session, new int[] { item.getFkUnitId() });
                    if (unit.getQueryResultId() == SDbConsts.READ_OK) {

                        // Validate if by product is by product type:

                        if (item.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_BP) {
                            msQueryResult = "El subproducto '" + item.getName() + " (" + item.getCode() + ")' de la materia prima \n" +
                                "'" + productionEmpty.getXtaRMItem() + " (" + productionEmpty.getXtaRMItemCode() + ")' no es de tipo '" +
                                session.readField(SModSysConsts.SS_ITEM_TP_BP, new int[] { item.getFkInputTypeId() }, SDbRegistryUser.FIELD_NAME) + "'.";
                            mnQueryResultId = SDbConsts.READ_ERROR;
                            break;
                        }
                        else if (itemRawMaterial.getMfgByproductPercentage() <= 0) {

                            // Validate if percentage of by product > 0:

                            msQueryResult = "El subproducto '" + item.getName() + " (" + item.getCode() + ")' de la materia prima \n" +
                                "'" + itemRawMaterial.getName() + " (" + itemRawMaterial.getCode() + ")' debe tener asignado un porcentaje mayor a 0.";
                            mnQueryResultId = SDbConsts.READ_ERROR;
                            break;
                        }

                        productionEmpty.setXtaBPItem(item.getName());
                        productionEmpty.setXtaBPItemCode(item.getCode());
                        productionEmpty.setXtaBPItemUnitCode(unit.getCode());

                        productionEmpty.setXtaFkBPItemUnitId(item.getFkUnitId());
                        productionEmpty.setXtaFkBPWarehouseCompanyId(item.getFkWarehouseCompanyId_n());
                        productionEmpty.setXtaFkBPWarehouseBranchId(item.getFkWarehouseBranchId_n());
                        productionEmpty.setXtaFkBPWarehouseWarehouseId(item.getFkWarehouseWarehouseId_n());
                    }
                }
            }

            // Validate if finished good hasn't item and percentage in the raw material by product is > 0:

            if (productionEmpty.getXtaFkBPItemId() == 0 && itemRawMaterial.getMfgByproductPercentage() > 0) {

                msQueryResult = "El producto terminado '" + productionEmpty.getXtaFGItem() + " (" + productionEmpty.getXtaFGItemCode() + ")' " +
                    "debe tener asignado un subproducto.";
                mnQueryResultId = SDbConsts.READ_ERROR;
                break;
            }

            // Read waste item and warehouse:

            item = new SDbItem();
            productionEmpty.setXtaFkCUItemId(resultSet.getInt("i.fk_item_cu_n"));
            if (!resultSet.wasNull()) {

                item.read(session, new int[] { productionEmpty.getXtaFkCUItemId() });
                if (item.getQueryResultId() == SDbConsts.READ_OK) {

                    unit.read(session, new int[] { item.getFkUnitId() });
                    if (unit.getQueryResultId() == SDbConsts.READ_OK) {

                        // Validate if cull is cull type:

                        if (item.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_CU) {
                            msQueryResult = "La desecho '" + item.getName() + " (" + item.getCode() + ")' de la materia prima " +
                                "'" + productionEmpty.getXtaRMItem() + " (" + productionEmpty.getXtaRMItemCode() + ")' no es de tipo '" +
                                session.readField(SModSysConsts.SS_ITEM_TP_CU, new int[] { item.getFkInputTypeId() }, SDbRegistryUser.FIELD_NAME) + "'.";
                            mnQueryResultId = SDbConsts.READ_ERROR;
                            break;
                        }
                        else if (itemRawMaterial.getMfgCullPercentage() <= 0) {

                            // Validate if percentage of cull > 0:

                            msQueryResult = "El desecho '" + item.getName() + " (" + item.getCode() + ")' de la materia prima " +
                                "'" + itemRawMaterial.getName() + " (" + itemRawMaterial.getCode() + ")' debe tener asignado un porcentaje mayor a 0.";
                            mnQueryResultId = SDbConsts.READ_ERROR;
                            break;
                        }

                        productionEmpty.setXtaCUItem(item.getName());
                        productionEmpty.setXtaCUItemCode(item.getCode());
                        productionEmpty.setXtaCUItemUnitCode(unit.getCode());

                        productionEmpty.setXtaFkCUItemUnitId(item.getFkUnitId());
                        productionEmpty.setXtaFkCUWarehouseCompanyId(item.getFkWarehouseCompanyId_n());
                        productionEmpty.setXtaFkCUWarehouseBranchId(item.getFkWarehouseBranchId_n());
                        productionEmpty.setXtaFkCUWarehouseWarehouseId(item.getFkWarehouseWarehouseId_n());
                    }
                }
            }

            // Validate if finished good hasn't item and percentage in the raw material by product is > 0:

            if (productionEmpty.getXtaFkCUItemId() == 0 && itemRawMaterial.getMfgCullPercentage() > 0) {

                msQueryResult = "El producto terminado '" + productionEmpty.getXtaFGItem() + " (" + productionEmpty.getXtaFGItemCode() + ")' " +
                    "debe tener asignado un desecho.";
                mnQueryResultId = SDbConsts.READ_ERROR;
                break;
            }

            // Read daily stock:

            stockDay = new SDbStockDay();
            if (resultSet.getInt("sd.id_year") > 0 && resultSet.getInt("sd.id_item") > 0 && resultSet.getInt("sd.id_unit") > 0 &&
                resultSet.getInt("sd.id_co") > 0 && resultSet.getInt("sd.id_cob") > 0 && resultSet.getInt("sd.id_wah") > 0 &&
                resultSet.getInt("sd.id_day") > 0) {

                stockDay.read(session, new int[] {
                    resultSet.getInt("sd.id_year"),
                    resultSet.getInt("sd.id_item"),
                    resultSet.getInt("sd.id_unit"),
                    resultSet.getInt("sd.id_co"),
                    resultSet.getInt("sd.id_cob"),
                    resultSet.getInt("sd.id_wah"),
                    resultSet.getInt("sd.id_day") });

                if (stockDay.getQueryResultId() == SDbConsts.READ_OK) {

                }
            }

            productionEmpty.setStockDay(stockDay);
            maChildProductionEmpties.add(productionEmpty);
            mnQueryResultId = SDbConsts.READ_OK;
        }

        if (mnQueryResultId == SDbConsts.READ_OK) {

            sql = "SELECT s.id_co, s.id_cob, s.id_wah, s.id_item, s.id_unit, " +
                "SUM(s.mov_in - s.mov_out) - " +
                "COALESCE((SELECT SUM(ss.mov_in - ss.mov_out) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS ss " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS si ON " +
                "ss.fk_iog = si.id_iog AND si.b_sys = 1 " +
                "WHERE ss.b_del = 0 AND ss.b_sys = 1 AND s.id_co = ss.id_co AND s.id_cob = ss.id_cob AND s.id_wah = ss.id_wah AND s.id_div = ss.id_div AND s.id_item = ss.id_item AND " +
                "s.id_unit = ss.id_unit AND s.id_unit = " + mnFkUnitId + " AND " +
                "ss.fk_iog_ct = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0] + " AND ss.fk_iog_cl = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1] + " AND " +
                "ss.fk_iog_tp = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2] + " AND ss.dt = DATE_SUB('" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "', " +
                "INTERVAL 1 DAY)), 0) AS f_stock, w.code, w.name, w.dim_heig, w.cap_real_lt, i.name, i.code, i.den, u.code " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w ON " +
                "s.id_co = w.id_co AND s.id_cob = w.id_cob AND s.id_wah = w.id_wah AND w.fk_wah_tp = " + SModSysConsts.CS_WAH_TP_TAN + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "s.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "s.id_unit = u.id_unit AND s.id_unit = " + mnFkUnitId + " " +
                "WHERE s.b_del = 0 AND s.id_year = " + SLibTimeUtils.digestYear(mtDateStockDay)[0] + " AND s.dt < '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "' " +
                "GROUP BY s.id_co, s.id_cob, s.id_wah, s.id_item, s.id_unit " +
                "HAVING f_stock <> 0 " +
                "ORDER BY w.code, w.name, i.name, i.code, u.code ";

            maChildProductionInventories.clear();
            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                productionInventory = new SRowProductionInventory();

                productionInventory.setPkWarehouseCompanyId(resultSet.getInt("s.id_co"));
                productionInventory.setPkWarehouseBranchId(resultSet.getInt("s.id_cob"));
                productionInventory.setPkWarehouseWarehouseId(resultSet.getInt("s.id_wah"));
                productionInventory.setPkItemId(resultSet.getInt("s.id_item"));
                productionInventory.setPkUnitId(resultSet.getInt("s.id_unit"));
                productionInventory.setStock(resultSet.getDouble("f_stock"));

                productionInventory.setXtaWarehouse(resultSet.getString("w.name"));
                productionInventory.setXtaWarehouseCode(resultSet.getString("w.code"));
                productionInventory.setXtaWarehouseHeight(resultSet.getDouble("w.dim_heig"));
                productionInventory.setXtaCapacityRealLiter(resultSet.getDouble("w.cap_real_lt"));
                productionInventory.setXtaItem(resultSet.getString("i.name"));
                productionInventory.setXtaItemCode(resultSet.getString("i.code"));
                productionInventory.setXtaItemDensity(resultSet.getDouble("i.den"));
                productionInventory.setXtaUnitCode(resultSet.getString("u.code"));

                maChildProductionInventories.add(productionInventory);
            }

            mnQueryResultId = SDbConsts.READ_OK;
        }
    }

    public void obtainProductionEstimateByDate(SGuiSession session, final Date stockDay, final Date estimateDate) throws SQLException, Exception {
        ResultSet resultSet = null;
        Statement statement = null;

        msSql = "SELECT id_mfg_est FROM " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST) + " WHERE " + (
                stockDay != null ? "dt_stk_day = '" + SLibUtils.DbmsDateFormatDate.format(stockDay) :
                estimateDate != null ? "dt_mfg_est = '" + SLibUtils.DbmsDateFormatDate.format(estimateDate) : "") + "'";
        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);

        if (resultSet.next()) {
            read(session, new int[] { resultSet.getInt("id_mfg_est") });
        }
    }
    
    /*
     * Overriden methods:
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMfgEstimationId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMfgEstimationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMfgEstimationId = 0;
        mnVersion = 0;
        mtDateMfgEstimation = null;
        mtDateStockDay = null;
        mdQtyFinishedGoods = 0;
        mdQtySubProducts = 0;
        mdQtyWaste = 0;
        mbClosed = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUnitId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mnFkUserClosedId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        mtTsUserClosed = null;

        msXtaUnitCode = "";
        msXtaUnitName = "";
        mdXtaQuantityDelivery = 0;

        maChildProductionEmpties.clear();
        maChildMfgWarehouseProducts.clear();
        maChildProductionInventories.clear();
        maChildCanSaveIogs.clear();

        maItemsStockDay.clear();
        maItemsStockSystem.clear();
        //maItemsStockSystemCurrently.clear();  XXX

        /* XXX Evaluating code (Sergio Flores 2015-10-13)...
        msAuxStockDaySkippedItem = "";
        msAuxStockDaySkippedUnit = "";
        msAuxStockDaySkippedCompany = "";
        msAuxStockDaySkippedBranch = "";
        msAuxStockDaySkippedWarehouse = "";
        */
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mfg_est = " + mnPkMfgEstimationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mfg_est = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMfgEstimationId = 0;

        msSql = "SELECT COALESCE(MAX(id_mfg_est), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMfgEstimationId = resultSet.getInt(1);
            mnVersion = 1;
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkMfgEstimationId = resultSet.getInt("id_mfg_est");
            mnVersion = resultSet.getInt("ver");
            mtDateMfgEstimation = resultSet.getDate("dt_mfg_est");
            mtDateStockDay = resultSet.getDate("dt_stk_day");
            mdQtyFinishedGoods = resultSet.getDouble("mfg_fg_r");
            mdQtySubProducts = resultSet.getDouble("mfg_bp_r");
            mdQtyWaste = resultSet.getDouble("mfg_cu_r");
            mbClosed = resultSet.getBoolean("b_clo");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mnFkUserClosedId = resultSet.getInt("fk_usr_clo");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            mtTsUserClosed = resultSet.getTimestamp("ts_usr_clo");

            msXtaUnitCode = (String) session.readField(SModConsts.SU_UNIT, new int[] { mnFkUnitId }, SDbRegistry.FIELD_CODE);
            msXtaUnitName = (String) session.readField(SModConsts.SU_UNIT, new int[] { mnFkUnitId }, SDbRegistry.FIELD_NAME);
            
            mbAuxWasClosed = mbClosed;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        SDbMfgEstimationEntry entry = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            mnFkUserClosedId = !mbClosed ? SUtilConsts.USR_NA_ID : session.getUser().getPkUserId();

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkMfgEstimationId + ", " +
                mnVersion + ", " +
                "'" + SLibUtils.DbmsDateFormatDate.format(mtDateMfgEstimation) + "', " +
                "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "', " +
                mdQtyFinishedGoods + ", " +
                mdQtySubProducts + ", " +
                mdQtyWaste + ", " +
                (mbClosed ? 1 : 0) + ", " +
                (mbDeleted ? 1 : 0) + ", " +
                (mbSystem ? 1 : 0) + ", " +
                mnFkUnitId + ", " +
                mnFkUserInsertId + ", " +
                mnFkUserUpdateId + ", " +
                mnFkUserClosedId + ", " +
                "NOW()" + ", " +
                "NOW()" + ", " +
                "NOW()" + " " +
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            if (isBeingClosed()) {
                mnFkUserClosedId = session.getUser().getPkUserId();
            }

            msSql = "UPDATE " + getSqlTable() + " SET " +
                //"id_mfg_est = " + mnPkMfgEstimationId + ", " +
                "ver = " + mnVersion + ", " +
                "dt_mfg_est = '" + SLibUtils.DbmsDateFormatDate.format(mtDateMfgEstimation) + "', " +
                "dt_stk_day = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStockDay) + "', " +
                "mfg_fg_r = " + mdQtyFinishedGoods + ", " +
                "mfg_bp_r = " + mdQtySubProducts + ", " +
                "mfg_cu_r = " + mdQtyWaste + ", " +
                "b_clo = " + (mbClosed ? 1 : 0) + ", " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                "fk_unit = " + mnFkUnitId + ", " +
                //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                (!isBeingClosed() ? "" : "fk_usr_clo = " + mnFkUserClosedId + ", ") +
                //"ts_usr_ins = " + "NOW()" + ", " +
                "ts_usr_upd = " + "NOW()" + " " +
                (!isBeingClosed() ? "" : ", ts_usr_clo = " + "NOW()" + " ") +
                getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;

        // Delete entries:

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST_ETY) + " WHERE id_mfg_est = " + mnPkMfgEstimationId;
        session.getStatement().execute(msSql);
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST_RM_CON) + " WHERE id_mfg_est = " + mnPkMfgEstimationId;
        session.getStatement().execute(msSql);

        // Save entries
        
        if (! maChildMfgWarehouseProducts.isEmpty()) {
            double dSubProductTotal = 0d;
            double dCullTotal = 0d;
            double dOilTotal = 0d;
        
            Map<Integer, SDbMfgEstimationRMConsumption> rmConsumptions = new HashMap();

            for (SSomMfgWarehouseProduct estimationRow : maChildMfgWarehouseProducts) {
                entry = new SDbMfgEstimationEntry();

                SDbItem item = new SDbItem();
                item.read(session, new int[] { estimationRow.getPkItemId() });
                SDbItem itemRawMaterial = new SDbItem();
                itemRawMaterial.read(session, new int[] { item.getFkItemRowMaterialId_n() });

                double rawMaterial = estimationRow.getQuantity() / itemRawMaterial.getMfgFinishedGoodPercentage();
                double subProduct = rawMaterial * itemRawMaterial.getMfgByproductPercentage();
                double cull = rawMaterial * itemRawMaterial.getMfgCullPercentage();
                
                dSubProductTotal += subProduct;
                dCullTotal += cull;
                dOilTotal += estimationRow.getQuantity();

                entry.setPkMfgEstimationId(mnPkMfgEstimationId);
                entry.setMfgFinishedGood(estimationRow.getQuantity());
                entry.setMfgByproduct(subProduct);
                entry.setMfgCull(cull);
                entry.setConsumptionRawMaterial(rawMaterial);
                entry.setFkItemMfgFinishedGoodId(estimationRow.getPkItemId());
                entry.setFkItemMfgByproductId_n(item.getFkItemByproductId_n());
                entry.setFkItemMfgCullId_n(item.getFkItemCullId_n());
                entry.setFkItemConsumptionRawMaterialId(item.getFkItemRowMaterialId_n());
                entry.setFkWarehouseCompanyId(estimationRow.getPkCompanyId());
                entry.setFkWarehouseBranchId(estimationRow.getPkBranchId());
                entry.setFkWarehouseWarehouseId(estimationRow.getPkWarehouseId());
                entry.setFkProductionLineId(estimationRow.getFkProductionLineId());

                entry.save(session);

                if (entry.getQueryResultId() != SDbConsts.SAVE_OK) {
                    mnQueryResultId = SDbConsts.SAVE_ERROR;
                    break;
                }

                if (rmConsumptions.containsKey(itemRawMaterial.getPkItemId())) {
                    SDbMfgEstimationRMConsumption rmCons = rmConsumptions.get(itemRawMaterial.getPkItemId());

                    rmCons.setMfgFinishedGood(rmCons.getMfgFinishedGood() + estimationRow.getQuantity());
                    rmCons.setMfgByproduct(rmCons.getMfgByproduct() + subProduct);
                    rmCons.setMfgCull(rmCons.getMfgCull() + cull);
                    rmCons.setConsumptionRawMaterial(rmCons.getConsumptionRawMaterial() + rawMaterial);                
                }
                else {
                    SDbMfgEstimationRMConsumption rmCons = new SDbMfgEstimationRMConsumption();

                    rmCons.setPkMfgEstimationId(mnPkMfgEstimationId);
                    rmCons.setOilPercentage(0);
                    rmCons.setMfgFinishedGood(estimationRow.getQuantity());
                    rmCons.setMfgByproduct(subProduct);
                    rmCons.setMfgCull(cull);
                    rmCons.setConsumptionRawMaterial(rawMaterial);
                    rmCons.setFkItemConsumptionRawMaterialId(itemRawMaterial.getPkItemId());

                    rmConsumptions.put(itemRawMaterial.getPkItemId(), rmCons);
                }
            }

            // Save consumptions entries

            for (Map.Entry<Integer, SDbMfgEstimationRMConsumption> rCons : rmConsumptions.entrySet()) {
                SDbMfgEstimationRMConsumption value = rCons.getValue();
                value.save(session);

                if (value.getQueryResultId() != SDbConsts.SAVE_OK) {
                    mnQueryResultId = SDbConsts.SAVE_ERROR;
                    break;
                }
            }
            
            String msSqlAux = "UPDATE " + getSqlTable() + " SET " +
                "mfg_fg_r = " + dOilTotal + ", " +
                "mfg_bp_r = " + dSubProductTotal + ", " +
                "mfg_cu_r = " + dCullTotal + ", " +
                "b_clo = true, " +
                "fk_usr_clo = " + mnFkUserUpdateId + " " +
                getSqlWhere();
        
            session.getStatement().executeUpdate(msSqlAux);
            
            mbClosed = true;
        }

        mbRegistryNew = false;
    }
    
    @Override
    public boolean canDelete(SGuiSession session) {
        return !mbDeleted;
    }
    
    @Override
    public void delete(SGuiSession session) {
        System.out.println(this.getPrimaryKey()[0]);
        
        String delSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = true, " +
                "b_clo = false, " +
                "fk_usr_upd = " + mnFkUserUpdateId + " " +
                getSqlWhere();
        
        String iogSql = "UPDATE s_iog "
                        + "SET b_del = true "
                        + "WHERE fk_mfg_est_n = " + mnPkMfgEstimationId + ";";
        
        String stkSql = "UPDATE s_stk "
                        + "SET b_del = true "
                        + "WHERE fk_iog IN "
                                + "(SELECT id_iog FROM s_iog WHERE fk_mfg_est_n = " + mnPkMfgEstimationId + ");";
        
        try {
            
            session.getStatement().execute(delSql);
            session.getStatement().execute(iogSql);
            session.getStatement().execute(stkSql);
            
            mnQueryResultId = SDbConsts.SAVE_OK;
        }
        catch (SQLException ex) {
            Logger.getLogger(SDbMfgEstimation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public SDbMfgEstimation clone() throws CloneNotSupportedException {
        SDbMfgEstimation registry = new SDbMfgEstimation();

        registry.setPkMfgEstimationId(this.getPkMfgEstimationId());
        registry.setVersion(this.getVersion());
        registry.setDateMfgEstimation(this.getDateMfgEstimation());
        registry.setDateStockDay(this.getDateStockDay());
        registry.setQtyFinishedGoods(this.getQtyFinishedGoods());
        registry.setQtySubProducts(this.getQtySubProducts());
        registry.setQtyWaste(this.getQtyWaste());
        registry.setClosed(this.isClosed());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setFkUserClosedId(this.getFkUserClosedId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        registry.setTsUserClosed(this.getTsUserClosed());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
