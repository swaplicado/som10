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
import java.util.Vector;
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
 * @author Néstor Ávalos
 */
public class SDbIog extends SDbRegistryUser {

    public static final int FIELD_EXTERNAL_DPS = SDbRegistry.FIELD_BASE + 1;

    protected int mnPkIogId;
    protected int mnNumber;
    protected Date mtDate;
    protected double mdQuantity;
    protected boolean mbAuthorized;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkIogCategoryId;
    protected int mnFkIogClassId;
    protected int mnFkIogTypeId;
    protected int mnFkIogAdjustmentTypeId;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkWarehouseCompanyId;
    protected int mnFkWarehouseBranchId;
    protected int mnFkWarehouseWarehouseId;
    protected int mnFkDivisionId;
    protected int mnFkIogId_n;
    protected int mnFkMixId_n;
    protected int mnFkExternalDpsYearId_n;
    protected int mnFkExternalDpsDocId_n;
    protected int mnFkExternalDpsEntryId_n;
    protected int mnFkExternalDpsAdjustmentYearId_n;
    protected int mnFkExternalDpsAdjustmentDocId_n;
    protected int mnFkExternalDpsAdjustmentEntryId_n;
    protected int mnFkExternalIogYearId_n;
    protected int mnFkExternalIogDocId_n;
    protected int mnFkExternalIogEntryId_n;
    protected int mnFkTicketId_n;
    protected int mnFkMfgEstimationId_n;
    protected int mnFkMfgEstimationVersionId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected int mnFkUserAuthorizationId;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    protected Date mtTsUserAuthorization;

    protected boolean mbXtaValidateWarehouseProduction;
    protected boolean mbXtaValidateTankItems;
    protected boolean mbXtaValidateExportationStock;
    protected int mnXtaExternalItemId;
    protected int mnXtaExternalUnitId;
    protected String msXtaIogTypeCode;
    protected String msXtaIogTypeName;
    protected String msXtaNumber;
    protected String msXtaNote;
    protected String msXtaDpsBizPartner;
    protected String msXtaDpsBizPartnerCode;
    protected Date mtXtaDpsDate;
    protected String msXtaDpsNumberSeries;
    protected String msXtaDpsNumberReference;
    protected String msXtaDpsItem;
    protected String msXtaDpsItemCode;
    protected double mdXtaDpsQuantityOriginal;
    protected double mdXtaDpsQuantitySupDev;
    protected double mdXtaDpsQuantityPending;
    protected String msXtaDpsQuantityUnit;
    protected String msXtaItem;
    protected String msXtaUnit;
    protected int mnXtaTicketNumber;
    protected int mnXtaStkDayPkYearId;
    protected Date mtXtaStkDayDate;
    protected double mdXtaQuantitytoTransfer;

    protected SDbIog moIogRegistryB;
    protected SDbIog moIogRegistryC;
    protected SDbIog moIogRegistryD;
    protected SDbIogNote moIogNote;
    protected SDbStock moStock;

    protected Vector<SSomWizardDpsTicketSupply> mvWizardDpsSupplyTicket;
    protected ArrayList<Object[]> maTransferMoves;

    public SDbIog() {
        super(SModConsts.S_IOG);

        mvWizardDpsSupplyTicket = new Vector<SSomWizardDpsTicketSupply>();
        maTransferMoves = new ArrayList<Object[]>();

        initRegistry();
    }

    /*
    * Private methods
    */

    private void computeNumber(final SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnNumber = 0;
        msSql = "SELECT COALESCE(MAX(CONVERT(num, UNSIGNED INTEGER)), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " "
                + "WHERE fk_iog_ct = " + mnFkIogCategoryId + " AND fk_iog_cl = " + mnFkIogClassId + " AND fk_iog_tp = " + mnFkIogTypeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnNumber = resultSet.getInt(1);
        }
    }

    private String obtainQueryByDps(final SGuiSession session, final int[] nIogType, final int[] nFkDpsEntryId) {
        String sql = "";
        String sqlBizPartner = "";
        String sqlOrderByDocEty = "";
        String sDatabaseName = "";
        String sDatabaseCoName = "";
        String sDatabaseErpName = "";

        if (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_OUT_PUR_PUR, nIogType) || SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_PUR_PUR, nIogType)) {

            // Purchases & suppliers:

            sqlBizPartner = "AND bc.id_ct_bp = " + SModSysConsts.EXT_BPSS_CT_BP_SUP + " ";
            sqlOrderByDocEty += "bp_key, bp, id_bp, f_dt_code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt ";
        }
        else {
            // Sales & customers:

            sqlBizPartner = "AND bc.id_ct_bp = " + SModSysConsts.EXT_BPSS_CT_BP_CUS + " ";
            sqlOrderByDocEty += "f_dt_code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, bp_key, bp, id_bp ";
        }
        sqlOrderByDocEty += ", item_key, item, fid_item, f_orig_unit, fid_orig_unit, f_orig_qty ";

        sDatabaseName = session.getDatabase().getDbName();
        sDatabaseCoName = ((SGuiClientSessionCustom) session.getSessionCustom()).getExtDatabaseCo().getDbName();
        sDatabaseErpName = ((SGuiClientSessionCustom) session.getSessionCustom()).getExtDatabase().getDbName();

        if (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_PUR_PUR, nIogType) || SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_OUT_SAL_SAL, nIogType)) {

            sql = "SELECT v.id_year, v.id_doc, v.id_ety, v.fid_item, v.fid_unit, d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, " +
                    "d.b_close, d.ts_close, uc.usr, c.cur_key, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                    "dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, v.fid_item, v.fid_unit, v.fid_orig_unit, " +
                    "i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, v.qty AS f_qty, v.orig_qty AS f_orig_qty, " +
                    "COALESCE((SELECT SUM(ddd.qty) FROM " + sDatabaseCoName + ".trn_dps_dps_adj AS ddd, " + sDatabaseCoName + ".trn_dps_ety AS dae, " +
                    sDatabaseCoName + ".trn_dps AS da WHERE ddd.id_dps_year = v.id_year AND ddd.id_dps_doc = v.id_doc AND " +
                    "ddd.id_dps_ety = v.id_ety AND ddd.id_adj_year = dae.id_year AND ddd.id_adj_doc = dae.id_doc AND ddd.id_adj_ety = dae.id_ety AND " +
                    "dae.id_year = da.id_year AND dae.id_doc = da.id_doc AND " +
                    "dae.b_del = 0 AND dae.fid_tp_dps_adj = " + SModSysConsts.EXT_TRNS_TP_DPS_ADJ_RET + " AND " +
                    "da.b_del = 0 AND da.fid_st_dps = " + SModSysConsts.EXT_TRNS_ST_DPS_EMITED + "), 0) AS f_adj_qty, " +
                    "COALESCE((SELECT SUM(ddd.orig_qty) FROM " + sDatabaseCoName + ".trn_dps_dps_adj AS ddd, " + sDatabaseCoName + ".trn_dps_ety AS dae, " +
                    sDatabaseCoName + ".trn_dps AS da WHERE " +
                    "ddd.id_dps_year = v.id_year AND ddd.id_dps_doc = v.id_doc AND ddd.id_dps_ety = v.id_ety AND " +
                    "ddd.id_adj_year = dae.id_year AND ddd.id_adj_doc = dae.id_doc AND ddd.id_adj_ety = dae.id_ety AND " +
                    "dae.id_year = da.id_year AND dae.id_doc = da.id_doc AND " +
                    "dae.b_del = 0 AND dae.fid_tp_dps_adj = " + SModSysConsts.EXT_TRNS_TP_DPS_ADJ_RET + " AND " +
                    "da.b_del = 0 AND da.fid_st_dps = " + SModSysConsts.EXT_TRNS_ST_DPS_EMITED + "), 0) AS f_adj_orig_qty, " +
                    "COALESCE((SELECT SUM(ge.qty * CASE WHEN ge.fk_ext_adj_year_n IS NULL THEN 1 ELSE -1 END) " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS ge " +
                    "WHERE ge.fk_ext_dps_year_n = v.id_year AND ge.fk_ext_dps_doc_n = v.id_doc AND ge.fk_ext_dps_ety_n = v.id_ety " +
                    " AND ge.b_del = 0), 0) AS f_sup_qty, " +
                    "COALESCE(( SELECT IF(d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0, 1, 0) " +
                    "FROM " + sDatabaseName + ".s_dps_ass AS som WHERE d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0), 0) AS f_exist " +
                    "FROM " + sDatabaseCoName + ".trn_dps AS d " +
                    "INNER JOIN " + sDatabaseErpName + ".trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                    "d.b_del = 0 AND d.fid_st_dps = " + SModSysConsts.EXT_TRNS_ST_DPS_EMITED + " AND " +
                    "d.fid_ct_dps = " + (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_OUT_SAL_SAL, nIogType) ? SModSysConsts.EXT_TRNS_CL_DPS_SAL_DOC[0] : SModSysConsts.EXT_TRNS_CL_DPS_PUR_DOC[0]) +
                    " AND d.fid_cl_dps = " + (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_OUT_SAL_SAL, nIogType) ? SModSysConsts.EXT_TRNS_CL_DPS_SAL_DOC[1] : SModSysConsts.EXT_TRNS_CL_DPS_PUR_DOC[1]) + " " +
                    "INNER JOIN " + sDatabaseErpName + ".cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                    "INNER JOIN " + sDatabaseErpName + ".bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                    "INNER JOIN " + sDatabaseErpName + ".bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                    "INNER JOIN " + sDatabaseErpName + ".bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp " + sqlBizPartner +
                    "INNER JOIN " + sDatabaseErpName + ".bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                    "INNER JOIN " + sDatabaseErpName + ".usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                    "INNER JOIN " + sDatabaseCoName + ".trn_dps_ety AS v ON d.id_year = v.id_year AND d.id_doc = v.id_doc AND " +
                    "v.b_del = 0 AND v.b_inv = 1 AND v.qty > 0 AND v.orig_qty > 0 " +
                    "INNER JOIN " + sDatabaseErpName + ".itmu_item AS i ON v.fid_item = i.id_item " +
                    "INNER JOIN " + sDatabaseErpName + ".itmu_unit AS u ON v.fid_unit = u.id_unit " +
                    "INNER JOIN " + sDatabaseErpName + ".itmu_unit AS uo ON v.fid_orig_unit = uo.id_unit " +
                    "WHERE v.id_year = " + nFkDpsEntryId[0] + " AND v.id_doc = " + nFkDpsEntryId[1] + " AND v.id_ety = " + nFkDpsEntryId[2] + " " +
                    "GROUP BY v.id_year, v.id_doc, v.id_ety, d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, " +
                    "uc.usr, c.cur_key, dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, v.fid_item, v.fid_unit, v.fid_orig_unit, i.item_key, " +
                    "i.item, u.symbol, uo.symbol, v.qty, v.orig_qty ";
        }
        else {
            sql = "SELECT v.id_year, v.id_doc, v.id_ety, v.fid_item, v.fid_unit, " +
                    "COALESCE((SELECT id_dps_year FROM " + sDatabaseCoName + ".TRN_DPS_DPS_ADJ AS adj " +
                    "WHERE adj.id_adj_year = v.id_year AND adj.id_adj_doc = v.id_doc AND adj.id_adj_ety = v.id_ety) , 0) AS f_adj_year, " +
                    "COALESCE((SELECT id_dps_doc FROM " + sDatabaseCoName + ".TRN_DPS_DPS_ADJ AS adj " +
                    "WHERE adj.id_adj_year = v.id_year AND adj.id_adj_doc = v.id_doc AND adj.id_adj_ety = v.id_ety) , 0) AS f_adj_doc, " +
                    "COALESCE((SELECT id_dps_ety FROM " + sDatabaseCoName + ".TRN_DPS_DPS_ADJ AS adj " +
                    "WHERE adj.id_adj_year = v.id_year AND adj.id_adj_doc = v.id_doc AND adj.id_adj_ety = v.id_ety) , 0) AS f_adj_ety, " +
                    "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                    "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                    "dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                    "v.fid_item, v.fid_unit, v.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                    "v.qty AS f_qty, v.orig_qty AS f_orig_qty, " +
                    "COALESCE((SELECT SUM(ge.qty) FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS ge WHERE " +
                    "ge.fk_ext_adj_year_n = v.id_year AND ge.fk_ext_adj_doc_n = v.id_doc AND ge.fk_ext_adj_ety_n = v.id_ety AND " +
                    " ge.b_del = 0), 0) AS f_ret_qty, " +
                    "COALESCE(( SELECT IF(d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0, 1, 0) " +
                    "FROM " + sDatabaseName + ".s_dps_ass AS som WHERE d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0), 0) AS f_exist " +
                    "FROM " + sDatabaseCoName + ".trn_dps AS d " +
                    "INNER JOIN " + sDatabaseErpName + ".trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                    "d.b_del = 0 AND d.fid_st_dps = " + SModSysConsts.EXT_TRNS_ST_DPS_EMITED + " AND " +
                    "d.fid_ct_dps = " + (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_SAL_SAL, nIogType) ? SModSysConsts.EXT_TRNS_CL_IOM_OUT_INT[0] : SModSysConsts.EXT_TRNS_CL_IOM_IN_INT[0]) +
                    " AND d.fid_cl_dps = " + (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_SAL_SAL, nIogType) ? SModSysConsts.EXT_TRNS_CL_IOM_OUT_INT[1] : SModSysConsts.EXT_TRNS_CL_IOM_IN_INT[1]) + " " +
                    "INNER JOIN " + sDatabaseErpName + ".cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                    "INNER JOIN " + sDatabaseErpName + ".bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                    "INNER JOIN " + sDatabaseErpName + ".bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                    "INNER JOIN " + sDatabaseErpName + ".bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp " + sqlBizPartner +
                    "INNER JOIN " + sDatabaseErpName + ".bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                    "INNER JOIN " + sDatabaseErpName + ".usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                    "INNER JOIN " + sDatabaseCoName + ".trn_dps_ety AS v ON d.id_year = v.id_year AND d.id_doc = v.id_doc AND " +
                    "v.b_del = 0 AND v.b_inv = 1 AND v.qty > 0 AND v.orig_qty > 0 AND v.fid_tp_dps_adj = " + SModSysConsts.EXT_TRNS_TP_DPS_ADJ_RET + " " +
                    "INNER JOIN " + sDatabaseErpName + ".itmu_item AS i ON v.fid_item = i.id_item " +
                    "INNER JOIN " + sDatabaseErpName + ".itmu_unit AS u ON v.fid_unit = u.id_unit " +
                    "INNER JOIN " + sDatabaseErpName + ".itmu_unit AS uo ON v.fid_orig_unit = uo.id_unit " +
                    "WHERE v.id_year = " + nFkDpsEntryId[0] + " AND v.id_doc = " + nFkDpsEntryId[1] + " AND v.id_ety = " + nFkDpsEntryId[2] + " " +
                    "GROUP BY v.id_year, v.id_doc, v.id_ety, d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, " +
                    "uc.usr, c.cur_key, dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, v.fid_item, v.fid_unit, v.fid_orig_unit, i.item_key, " +
                    "i.item, u.symbol, uo.symbol, v.qty, v.orig_qty ";
        }
        sql += "ORDER BY " + sqlOrderByDocEty + "; ";

        return sql;
    }

    private String obtainTicketQuery(final int nPkTicket) {

        return "SELECT v.id_tic, v.num, v.qty, v.fk_item, COALESCE(it.fk_wah_co_n, 0) AS f_fk_wah_co_n, COALESCE(it.fk_wah_cob_n, 0) AS f_fk_wah_cob_n, " +
            "COALESCE(it.fk_wah_wah_n, 0) AS f_fk_wah_wah_n, COALESCE(g.fk_div, 0) AS f_fk_div,  CONCAT(tp.code, '-', g.num) AS f_tp_iog, " +
            "(SELECT COALESCE(SUM(ge.qty), 0) FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS ge WHERE " +
            "ge.b_del = 0 AND ge.fk_tic_n = v.id_tic AND ge.fk_item = v.fk_item AND ge.fk_unit = v.fk_unit) AS f_qty " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS v " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sc ON " +
            "v.fk_sca = sc.id_sca " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_TIC_ST) + " AS vs ON " +
            "v.fk_tic_st = vs.id_tic_st " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pr ON " +
            "v.fk_prod = pr.id_prod " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS it ON " +
            "v.fk_item = it.id_item " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g ON " +
            "v.id_tic = g.fk_tic_n AND g.b_del = 0 " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CT) + " AS ct ON " +
            "g.fk_iog_ct = ct.id_iog_ct " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CL) + " AS cl ON " +
            "g.fk_iog_ct = cl.id_iog_ct AND g.fk_iog_cl = cl.id_iog_cl " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_TP) + " AS tp ON " +
            "g.fk_iog_ct = tp.id_iog_ct AND g.fk_iog_cl = tp.id_iog_cl AND g.fk_iog_tp = tp.id_iog_tp " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS se ON " +
            "v.fk_seas_n = se.id_seas " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS re ON " +
            "v.fk_reg_n = re.id_reg " +
            "WHERE v.id_tic = " + nPkTicket + " AND v.b_tar = 1 AND v.fk_tic_st = " + SModSysConsts.SS_TIC_ST_ADM  + " " +
            "GROUP BY v.id_tic " +
            "ORDER BY sc.code, v.num, v.dt, v.id_tic ";
    }

    private static ArrayList<SSomWarehouseItem> stockWarehouseByItem(final SGuiSession session, final int nYearId, final int[] anWarehouseId, final int[] anStockMoveId,
            final Date date, final int nDivisionId) {
        String sql = "";
        ArrayList<SSomWarehouseItem> aWarehouseItem = new ArrayList<SSomWarehouseItem>();
        SSomWarehouseItem warehouseItem = null;
        ResultSet resultSet = null;

        try {
            sql = "SELECT i.id_item, u.id_unit, i.fk_item_tp, CONCAT(i.name, ' (', i.code, ') ') AS f_item, u.code, i.den, " +
                "SUM(s.mov_in - s.mov_out) AS f_stock " +
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
                "s.id_co = " + anWarehouseId[0] + " AND " +
                "s.id_cob = " + anWarehouseId[1] + " AND " +
                "s.id_wah = " + anWarehouseId[2] + " AND " +
                (nDivisionId != SLibConsts.UNDEFINED ? ("s.id_div = " + nDivisionId + " AND ") : "") +
                "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                (anStockMoveId != null ? ("AND s.id_mov <> " + anStockMoveId[6]) : "") + " " +
                "GROUP BY i.id_item, u.id_unit " +
                "HAVING f_stock > 0 " +
                "ORDER BY i.id_item, u.id_unit ";

            resultSet = session.getStatement().executeQuery(sql);
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

    private boolean validateInOutMoves(final SGuiSession session, final boolean actionDelete) throws SQLException, Exception {
        boolean b = true;
        int nItemId = 0;
        int nItemUnitId = 0;
        int nItemTypeId = 0;
        double dItemStock = 0;
        double dItemDensity = 0;
        double dWarehouseCapacity = 0;
        String sItem = "";
        String sItemUnit = "";

        SSomStock stock = null;
        Vector<Object> itemsStock = new Vector<Object>();
        ArrayList<SSomWarehouseItem> aWarehouseItems = new ArrayList<SSomWarehouseItem>();

        SDbItem item = null;
        SDbBranchWarehouse warehouse = null;
        SDbMfgEstimation estimation = null;

        // Validate that day is open:

        try {
            estimation = new SDbMfgEstimation();
            estimation.obtainProductionEstimateByDate(session, null, mtDate);

            if (estimation.isClosed()) {

                msQueryResult = "El período está cerrado por estimación de la producción.";
                b = false;
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        if (!b) {
            mnQueryResultId = SDbConsts.SAVE_ERROR;
        }
        else {

            warehouse = new SDbBranchWarehouse();
            warehouse.read(session, new int[] { mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId });
            if (warehouse.getQueryResultId() != SDbConsts.READ_OK) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }

            item = new SDbItem();
            item.read(session, new int[] { mnFkItemId });
            if (item.getQueryResultId() != SDbConsts.READ_OK) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }

            if ((mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_IN && !mbDeleted && !actionDelete) ||
                (mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_IN && mbDeleted && actionDelete) ||
                (mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_OUT && !mbDeleted && actionDelete)) {

                // Validate warehouse business rules:

                msQueryResult = SSomUtils.obtainWarehouseItemsForBusinessRules(
                        session,
                        warehouse.getPrimaryKey(),
                        moStock != null ? moStock.getPrimaryKey() : null,
                        mtDate,
                        mnFkItemId,
                        mnFkUnitId,
                        actionDelete && !mbDeleted ? 0d : mdQuantity,
                        msXtaUnit,
                        mbXtaValidateWarehouseProduction ? false : true,
                        mbXtaValidateExportationStock,
                        SLibConsts.UNDEFINED);

                if (!msQueryResult.isEmpty()) {
                    b = false;
                }

                /* XXX Delete before to updateCode
                 *
                // Obtain items by warehouse:

                aWarehouseItems = stockWarehouseByItem(session, SLibTimeUtils.digestYear(mtDate)[0], new int[] { mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId },
                        (moStock != null ? moStock.getPrimaryKey() : null), (!mbXtaValidateExportationStock ? SLibTimeUtils.getEndOfYear(mtDate) : mtDate), SLibConsts.UNDEFINED);
                dWarehouseCapacity = 0;

                for (Object oItem : itemsStock) {
                    nItemId = (Integer)((Object[]) oItem)[0];
                    nItemUnitId = (Integer)((Object[]) oItem)[1];
                    nItemTypeId = (Integer)((Object[]) oItem)[2];
                    sItem = (String)((Object[]) oItem)[3];
                    sItemUnit = (String)((Object[]) oItem)[4];
                    dItemStock = (Double)((Object[]) oItem)[5];
                    dItemDensity = (Double)((Object[]) oItem)[6];

                    //if (!mbXtaValidateTankItems) {
                        if (warehouse.getFkWarehouseTypeId() != SModSysConsts.CS_WAH_TP_WAH) {

                            // Validate only if item is not a cull:

                            if ((mnFkItemId != nItemId || mnFkUnitId != nItemUnitId) &&
                                item.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_CU &&
                                nItemTypeId != SModSysConsts.SS_ITEM_TP_CU) {

                                msQueryResult = "El almacén '" + warehouse.getCode() + "' ya contiene un ítem con existencias: '" + sItem + "'.";
                                b = false;
                                break;
                            }
                        }
                    //}

                    // Validate total capacity in warehouse:

                    if (!mbXtaValidateWarehouseProduction) {
                        dWarehouseCapacity += (dItemDensity > 0 ? (dItemStock / dItemDensity) : (dItemStock / item.getDensity()));
                        if ((dWarehouseCapacity + ((actionDelete ? 0 : mdQuantity) / item.getDensity())) > warehouse.getCapacityRealLiter() &&
                                warehouse.getFkWarehouseTypeId() == SModSysConsts.CS_WAH_TP_TAN) {
                            msQueryResult = "La cantidad capturada de: '" + SLibUtils.DecimalFormatValue2D.format(mdQuantity) + " " + msXtaUnit +
                                    "' hace que el tanque: '" + warehouse.getCode() + "' sobrepase su capacidad.";
                            b = false;
                            break;
                        }
                    }

                    // Validate units:

                    if (mnFkUnitId != nItemUnitId &&
                        nItemTypeId != SModSysConsts.SS_ITEM_TP_CU) {

                        msQueryResult = "El ítem '" + msXtaItem + " " + msXtaUnit + "' tiene una unidad ('" + sItemUnit + "') diferente a la del producto : '" + warehouse.getCode() + "'.";
                        b = false;
                        break;
                    }
                } */
            }

            if (b &&
               (mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_IN && !mbDeleted && actionDelete) ||
               (mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_OUT && !mbDeleted && !actionDelete) ||
               (mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_OUT && mbDeleted && actionDelete)) {

                // Validate stock:

                stock = SSomUtils.validateStock(
                    session,
                    SLibTimeUtils.digestYear(mtDate)[0],
                    mnFkItemId,
                    mnFkUnitId,
                    SLibConsts.UNDEFINED,
                    new int[] { mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId },
                    mnFkDivisionId,
                    ((actionDelete && !mbDeleted && mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_IN) ? null :
                    (moStock != null ? moStock.getPrimaryKey() : null)),
                    mtDate,
                    mdQuantity);

                if (!stock.getResult().isEmpty()) {
                    msQueryResult = stock.getResult();
                    b = false;
                }
                else {

                    // Validate warehouse business rules:

                    msQueryResult = SSomUtils.obtainWarehouseItemsForBusinessRules(
                        session,
                        new int[] { mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId },
                        moStock != null ? moStock.getPrimaryKey() : null,
                        mtDate,
                        mnFkItemId,
                        mnFkUnitId,
                        0d,
                        msXtaUnit,
                        true,
                        false,
                        SLibConsts.UNDEFINED);

                    if (!msQueryResult.isEmpty()) {
                        b = false;
                    }
                }
            }
        }

        return b;
    }

    private SDbIog readRegistryDestiny(final SGuiSession session, final SDbIog iogRegistry, final int nPkIogRegistryId) throws SQLException, Exception {
        SDbIog iog = iogRegistry;

        iog = new SDbIog();
        iog.read(session, new int[] { nPkIogRegistryId });
        if (iog.getQueryResultId() == SDbConsts.READ_ERROR) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }

        return iog;
    }

    private boolean saveRegistryDestiny(final SGuiSession session, final SDbIog iogRegistry, final int nPkIogId) throws SQLException, Exception {
        boolean res = true;
        SDbIog iog = iogRegistry;

        if (iog != null) {

            iog.setXtaValidateExportationStock(mbXtaValidateExportationStock);
            if (mbRegistryNew) {
                iog.setFkIogId_n(nPkIogId);
            }

            if (iog.canSave(session)){
                iog.save(session);
            }
            else {
                mnQueryResultId = iog.getQueryResultId();
                msQueryResult = iog.getQueryResult();
                res = false;
            }
        }

        return res;
    }

    private String deleteRegistryDestiny(final SGuiSession session, final SDbIog iogRegistry, final boolean bDeleted) throws SQLException, Exception {
        SDbIog iog = iogRegistry;

        iog.setDeleted(!bDeleted);
        iog.delete(session);

        return iog.msQueryResult;
    }

    private boolean tankContentItem(final SGuiSession session, final int nYearId, final int mnPkItemId, final int mnPkUnitId, final int nItemTypeId, final int[] anWarehouseId,
            final int nDivisionId, Date date) throws SQLException {
        boolean exist = true;
        String sql = "";

        ResultSet resultSet = null;
        Statement statement = null;

        sql = "SELECT CONCAT(i.name, ' (', i.code, ') ') AS f_item, u.code, COALESCE(SUM(s.mov_in - s.mov_out),0) AS f_stock, i.id_item, i.fk_unit " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
            "s.id_item = i.id_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
            "s.id_unit = u.id_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " +
            "s.id_co = wah.id_co AND s.id_cob = wah.id_cob AND s.id_wah = wah.id_wah " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
            "s.id_div = d.id_div " +
            "WHERE s.b_del = 0 AND " +
            "s.id_year = " + nYearId + " AND " +
            "i.fk_item_tp = " + nItemTypeId + " AND " +
            "s.id_co = " + anWarehouseId[0] + " AND " +
            "s.id_cob = " + anWarehouseId[1] + " AND " +
            "s.id_wah = " + anWarehouseId[2] + " " +
            (nDivisionId == SLibConsts.UNDEFINED ? "" : "AND s.id_div = " + nDivisionId) + " AND " +
            "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "'" +
            "GROUP BY i.id_item, i.fk_unit; ";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            if (resultSet.getInt("i.id_item") != mnPkItemId && resultSet.getInt("i.fk_unit") != mnPkUnitId) {
                exist = false;
                break;
            }
        }

        return exist;
    }

    private boolean saveTransferMoves(SGuiSession session) throws SQLException, Exception {
        boolean res = true;
        int nSrcWahCompanyId = 0;
        int nSrcWahBranchId = 0;
        int nSrcWahWarehouseId = 0;
        int nSrcDivisionId = 0;
        int nDesWahCompanyId = 0;
        int nDesWahBranchId = 0;
        int nDesWahWarehouseId = 0;
        int nDesDivisionId = 0;
        int nItemId = 0;
        int nUnitId = 0;
        double dQuantity = 0;
        Date tDate = null;

        SDbIog source = null;

        if (maTransferMoves.size() > 0) {
            for (Object[] transferMove : maTransferMoves) {

                nSrcWahCompanyId = (Integer) transferMove[0];
                nSrcWahBranchId = (Integer) transferMove[1];
                nSrcWahWarehouseId = (Integer) transferMove[2];
                nSrcDivisionId = (Integer) transferMove[3];
                nDesWahCompanyId = (Integer) transferMove[4];
                nDesWahBranchId = (Integer) transferMove[5];
                nDesWahWarehouseId = (Integer) transferMove[6];
                nDesDivisionId = (Integer) transferMove[3];
                nItemId = (Integer) transferMove[7];
                nUnitId = (Integer) transferMove[8];
                dQuantity = (Double) transferMove[9];
                tDate = (Date) transferMove[10];

                source = new SDbIog();
                source.setIogRegistryB(new SDbIog());

                source.computeIog(tDate, dQuantity, true, SModSysConsts.SS_IOG_TP_OUT_INT_TRA , SModSysConsts.SU_IOG_ADJ_TP_NA,
                    nItemId, nUnitId, new int[] { nSrcWahCompanyId, nSrcWahBranchId, nSrcWahWarehouseId }, nSrcDivisionId,
                    mnFkMfgEstimationId_n, mnFkMfgEstimationVersionId_n, mnFkUserInsertId);

                source.getIogRegistryB().computeIog(tDate, dQuantity, true, SModSysConsts.SS_IOG_TP_IN_INT_TRA , SModSysConsts.SU_IOG_ADJ_TP_NA,
                    nItemId, nUnitId, new int[] { nDesWahCompanyId, nDesWahBranchId, nDesWahWarehouseId }, nDesDivisionId,
                    mnFkMfgEstimationId_n, mnFkMfgEstimationVersionId_n, mnFkUserInsertId);

                source.save(session);
                if (source.getQueryResultId() != SDbConsts.SAVE_OK) {

                    mnQueryResultId = source.getQueryResultId();
                    msQueryResult = source.getQueryResult();
                    res = false;
                }
            }
        }

        return res;
    }

    /*
    * Public methods
    */

    public void setPkIogId(int n) { mnPkIogId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setAuthorized(boolean b) { mbAuthorized = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkIogCategoryId(int n) { mnFkIogCategoryId = n; }
    public void setFkIogClassId(int n) { mnFkIogClassId = n; }
    public void setFkIogTypeId(int n) { mnFkIogTypeId = n; }
    public void setFkIogAdjustmentTypeId(int n) { mnFkIogAdjustmentTypeId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkWarehouseCompanyId(int n) { mnFkWarehouseCompanyId = n; }
    public void setFkWarehouseBranchId(int n) { mnFkWarehouseBranchId = n; }
    public void setFkWarehouseWarehouseId(int n) { mnFkWarehouseWarehouseId = n; }
    public void setFkDivisionId(int n) { mnFkDivisionId = n; }
    public void setFkIogId_n(int n) { mnFkIogId_n = n; }
    public void setFkMixId_n(int n) { mnFkMixId_n = n; }
    public void setFkExternalDpsYearId_n(int n) { mnFkExternalDpsYearId_n = n; }
    public void setFkExternalDpsDocId_n(int n) { mnFkExternalDpsDocId_n = n; }
    public void setFkExternalDpsEntryId_n(int n) { mnFkExternalDpsEntryId_n = n; }
    public void setFkExternalDpsAdjustmentYearId_n(int n) { mnFkExternalDpsAdjustmentYearId_n = n; }
    public void setFkExternalDpsAdjustmentDocId_n(int n) { mnFkExternalDpsAdjustmentDocId_n = n; }
    public void setFkExternalDpsAdjustmentEntryId_n(int n) { mnFkExternalDpsAdjustmentEntryId_n = n; }
    public void setFkExternalIogYearId_n(int n) { mnFkExternalIogYearId_n = n; }
    public void setFkExternalIogDocId_n(int n) { mnFkExternalIogDocId_n = n; }
    public void setFkExternalIogEntryId_n(int n) { mnFkExternalIogEntryId_n = n; }
    public void setFkTicketId_n(int n) { mnFkTicketId_n = n; }
    public void setFkMfgEstimationId_n(int n) { mnFkMfgEstimationId_n = n; }
    public void setFkMfgEstimationVersionId_n(int n) { mnFkMfgEstimationVersionId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setFkUserAuthorizationId(int n) { mnFkUserAuthorizationId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    public void setTsUserAuthorization(Date t) { mtTsUserAuthorization = t; }

    public int getPkIogId() { return mnPkIogId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public double getQuantity() { return mdQuantity; }
    public boolean isAuthorized() { return mbAuthorized; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkIogCategoryId() { return mnFkIogCategoryId; }
    public int getFkIogClassId() { return mnFkIogClassId; }
    public int getFkIogTypeId() { return mnFkIogTypeId; }
    public int getFkIogAdjustmentTypeId() { return mnFkIogAdjustmentTypeId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkWarehouseCompanyId() { return mnFkWarehouseCompanyId; }
    public int getFkWarehouseBranchId() { return mnFkWarehouseBranchId; }
    public int getFkWarehouseWarehouseId() { return mnFkWarehouseWarehouseId; }
    public int getFkDivisionId() { return mnFkDivisionId; }
    public int getFkIogId_n() { return mnFkIogId_n; }
    public int getFkMixId_n() { return mnFkMixId_n; }
    public int getFkExternalDpsYearId_n() { return mnFkExternalDpsYearId_n; }
    public int getFkExternalDpsDocId_n() { return mnFkExternalDpsDocId_n; }
    public int getFkExternalDpsEntryId_n() { return mnFkExternalDpsEntryId_n; }
    public int getFkExternalDpsAdjustmentYearId_n() { return mnFkExternalDpsAdjustmentYearId_n; }
    public int getFkExternalDpsAdjustmentDocId_n() { return mnFkExternalDpsAdjustmentDocId_n; }
    public int getFkExternalDpsAdjustmentEntryId_n() { return mnFkExternalDpsAdjustmentEntryId_n; }
    public int getFkExternalIogYearId_n() { return mnFkExternalIogYearId_n; }
    public int getFkExternalIogDocId_n() { return mnFkExternalIogDocId_n; }
    public int getFkExternalIogEntryId_n() { return mnFkExternalIogEntryId_n; }
    public int getFkTicketId_n() { return mnFkTicketId_n; }
    public int getFkMfgEstimationId_n() { return mnFkMfgEstimationId_n; }
    public int getFkMfgEstimationVersionId_n() { return mnFkMfgEstimationVersionId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public int getFkUserAutorizationId() { return mnFkUserAuthorizationId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    public Date getTsUserAuthorization() { return mtTsUserAuthorization; }

    public void setXtaValidateWarehouseProduction(boolean b) { mbXtaValidateWarehouseProduction = b; }
    public void setXtaValidateTankItems(boolean b) { mbXtaValidateTankItems = b; }
    public void setXtaValidateExportationStock(boolean b) { mbXtaValidateExportationStock = b; }
    public void setXtaExternalItemId(int n) { mnXtaExternalItemId = n; }
    public void setXtaExternalUnitId(int n) { mnXtaExternalUnitId = n; }
    public void setXtaIogTypeCode(String s) { msXtaIogTypeCode = s; }
    public void setXtaIogTypeName(String s) { msXtaIogTypeName = s; }
    public void setXtaNumber(String s) { msXtaNumber = s; }
    public void setXtaNote(String s) { msXtaNote = s; }
    public void setXtaDpsBizPartner(String s) { msXtaDpsBizPartner = s; }
    public void setXtaDpsBizPartnerCode(String s) { msXtaDpsBizPartnerCode = s; }
    public void setXtaDpsDate(Date t) { mtXtaDpsDate = t; }
    public void setXtaDpsNumberSeries(String s) { msXtaDpsNumberSeries = s; }
    public void setXtaDpsNumberReference(String s) { msXtaDpsNumberReference = s; }
    public void setXtaDpsItem(String s) { msXtaDpsItem = s; }
    public void setXtaDpsItemCode(String s) { msXtaDpsItemCode = s; }
    public void setXtaDpsQuantityOriginal(double d) { mdXtaDpsQuantityOriginal = d; }
    public void setXtaDpsQuantitySupDev(double d) { mdXtaDpsQuantitySupDev = d; }
    public void setXtaDpsQuantityPending(double d) { mdXtaDpsQuantityPending = d; }
    public void setXtaDpsQuantityUnit(String s) { msXtaDpsQuantityUnit = s; }
    public void setXtaItem(String s) { msXtaItem = s; }
    public void setXtaUnit(String s) { msXtaUnit = s; }
    public void setXtaTicketNumber(int n) { mnXtaTicketNumber = n; }
    public void setXtaStkDayPkYearId(int n) { mnXtaStkDayPkYearId = n; }
    public void setXtaStkDayDate(Date t) { mtXtaStkDayDate = t; }
    public void setXtaQuantitytoTransfer(double d) { mdXtaQuantitytoTransfer = 0; }

    public boolean getXtaValidateWarehouseProduction() { return mbXtaValidateWarehouseProduction; }
    public boolean getXtaValidateTankItems() { return mbXtaValidateTankItems; }
    public boolean getXtaValidateExportationStock() { return mbXtaValidateExportationStock; }
    public int getXtaExternalItemId() { return mnXtaExternalItemId; }
    public int getXtaExternalUnitId() { return mnXtaExternalUnitId; }
    public String getXtaIogTypeCode() { return msXtaIogTypeCode; }
    public String getXtaIogTypeName() { return msXtaIogTypeName; }
    public String getXtaNumber() { return msXtaNumber; }
    public String getXtaNote() { return msXtaNote; }
    public String getXtaDpsBizPartner() { return msXtaDpsBizPartner; }
    public String getXtaDpsBizPartnerCode() { return msXtaDpsBizPartnerCode; }
    public Date getXtaDpsDate() { return mtXtaDpsDate; }
    public String getXtaDpsNumberSeries() { return msXtaDpsNumberSeries; }
    public String getXtaDpsNumberReference() { return msXtaDpsNumberReference; }
    public String getXtaDpsItem() { return msXtaDpsItem; }
    public String getXtaDpsItemCode() { return msXtaDpsItemCode; }
    public double getXtaDpsQuantityOriginal() { return mdXtaDpsQuantityOriginal; }
    public double getXtaDpsQuantitySupDev() { return mdXtaDpsQuantitySupDev; }
    public double getXtaDpsQuantityPending() { return mdXtaDpsQuantityPending; }
    public String getXtaDpsQuantityUnit() { return msXtaDpsQuantityUnit; }
    public String getXtaItem() { return msXtaItem; }
    public String getXtaUnit() { return msXtaUnit; }
    public int getXtaTicketNumber() { return mnXtaTicketNumber; }
    public int getXtaStkDayPkYearId() { return mnXtaStkDayPkYearId; }
    public Date getXtaStkDayDate() { return mtXtaStkDayDate; }
    public double getXtaQuantitytoTransfer() { return mdXtaQuantitytoTransfer; }

    public void setIogRegistryB(SDbIog o) { moIogRegistryB = o; }
    public void setIogRegistryC(SDbIog o) { moIogRegistryC = o; }
    public void setIogRegistryD(SDbIog o) { moIogRegistryD = o; }

    public void setIogNote(SDbIogNote o) { moIogNote = o; }
    public void setStock(SDbStock o) { moStock = o; }

    public SDbIog getIogRegistryB() { return moIogRegistryB; }
    public SDbIog getIogRegistryC() { return moIogRegistryC; }
    public SDbIog getIogRegistryD() { return moIogRegistryD; }

    public SDbStock getStock() { return moStock; }

    public Vector<SSomWizardDpsTicketSupply> getWizardDpsSupplyTicket() { return mvWizardDpsSupplyTicket; }
    public ArrayList<Object[]> getTransferMoves() { return maTransferMoves; }

    public boolean deliveryFinishedGood(final SGuiSession session, final SDbIog paramIog) throws SQLException, Exception {
        boolean res = true;
        double dPhisicalInventory = 0;
        double dStockSystemInventory = 0;
        double dStockSystemInventoryByTank = 0;
        double dQuantitytoInMove = 0;
        double dQuantitytoTransferMove = 0;
        String sql = "";

        SSomStock stock = null;

        ResultSet resultSet = null;
        Statement statement = null;

        // canSave:

        mbXtaValidateWarehouseProduction = true;
        if (!canSave(session)) {
            res = false;
        }

        if (res) {

            // Obtain phisical inventory:

            sql = "SELECT stk_day " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " " +
                "WHERE b_del = 0 AND b_stk_dif_skp = 0 AND id_year = " + mnXtaStkDayPkYearId + " AND id_item = " + mnFkItemId + " AND " +
                    "id_unit = " + mnFkUnitId + " AND id_co = " + mnFkWarehouseCompanyId + " AND " +
                    "id_cob = " + mnFkWarehouseBranchId + " AND id_wah = " + mnFkWarehouseWarehouseId + " AND " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtXtaStkDayDate) + "' ";

            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                msQueryResult = "No se encontro el registro de la toma física.";
                res = false;
            }
            else {
                dPhisicalInventory = resultSet.getDouble("stk_day");
            }

            // Obtain system inventory:

            if (res) {

                stock = SSomUtils.obtainStock(session, mnXtaStkDayPkYearId, mnFkItemId, mnFkUnitId, 0,
                        new int[] { mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId }, SLibConsts.UNDEFINED, null,
                        SLibTimeUtils.createDate(
                            SLibTimeUtils.digestDate(mtXtaStkDayDate)[0],
                            SLibTimeUtils.digestDate(mtXtaStkDayDate)[1],
                            SLibTimeUtils.digestDate(mtXtaStkDayDate)[2]-1), false); // XXX Validate with first day of the month

                dStockSystemInventory = (stock.getStock());

                // Obtain quantity that is possible to transfer:

                dQuantitytoInMove = dPhisicalInventory - dStockSystemInventory;
                mdXtaQuantitytoTransfer = mdQuantity - dQuantitytoInMove;

                // Obtain tanks that can do the transfer of oil (phisical inventory):

                sql = "SELECT w.code, d.id_co, d.id_cob, d.id_wah, d.stk_day " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS d " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w ON " +
                    "d.id_co = w.id_co AND d.id_cob = w.id_cob AND d.id_wah = w.id_wah " +
                    "WHERE d.b_del = 0 AND d.b_stk_dif_skp = 0 AND d.b_emp = 0 AND d.id_year = " + mnXtaStkDayPkYearId + " AND d.id_item = " + mnFkItemId + " AND " +
                        "d.id_unit = " + mnFkUnitId + " AND dt = '" + SLibUtils.DbmsDateFormatDate.format(mtXtaStkDayDate) + "' AND w.fk_wah_tp = " + SModSysConsts.CS_WAH_TP_TAN + " ";

                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {

                    if (mdXtaQuantitytoTransfer > 0) {

                        // Validate that the product exist in the tank:

                        if (tankContentItem(session,
                                mnXtaStkDayPkYearId, mnFkItemId, mnFkUnitId, SModSysConsts.SS_ITEM_TP_FG,
                                new int[] {
                                resultSet.getInt("d.id_co"),
                                resultSet.getInt("d.id_cob"),
                                resultSet.getInt("d.id_wah") },
                                SLibConsts.UNDEFINED,
                                SLibTimeUtils.createDate(
                                SLibTimeUtils.digestDate(mtXtaStkDayDate)[0],
                                SLibTimeUtils.digestDate(mtXtaStkDayDate)[1],
                                SLibTimeUtils.digestDate(mtXtaStkDayDate)[2]-1))) {

                            // Obtain system inventory by tank:

                            stock = SSomUtils.obtainStock(session, mnXtaStkDayPkYearId, mnFkItemId, mnFkUnitId, SModSysConsts.SS_ITEM_TP_FG,
                                new int[] { resultSet.getInt("d.id_co"), resultSet.getInt("d.id_cob"), resultSet.getInt("d.id_wah") }, SLibConsts.UNDEFINED, null,
                                SLibTimeUtils.createDate(
                                    SLibTimeUtils.digestDate(mtXtaStkDayDate)[0],
                                    SLibTimeUtils.digestDate(mtXtaStkDayDate)[1],
                                    SLibTimeUtils.digestDate(mtXtaStkDayDate)[2]-1), false); // XXX Validate with first day of the month

                            dStockSystemInventoryByTank = stock.getStock();
                            dQuantitytoTransferMove = resultSet.getDouble("d.stk_day") - dStockSystemInventoryByTank;
                            if (dQuantitytoTransferMove <= mdXtaQuantitytoTransfer) {
                                mdXtaQuantitytoTransfer = mdXtaQuantitytoTransfer - dQuantitytoTransferMove;
                            }
                            else {
                                dQuantitytoTransferMove = mdXtaQuantitytoTransfer;
                                mdXtaQuantitytoTransfer = 0;
                            }

                            maTransferMoves.add(new Object[] {
                                mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId, mnFkDivisionId,
                                resultSet.getInt("d.id_co"), resultSet.getInt("d.id_cob"), resultSet.getInt("d.id_wah"),
                                mnFkItemId, mnFkUnitId, dQuantitytoTransferMove,
                                SLibTimeUtils.createDate(
                                    SLibTimeUtils.digestDate(mtXtaStkDayDate)[0],
                                    SLibTimeUtils.digestDate(mtXtaStkDayDate)[1],
                                    SLibTimeUtils.digestDate(mtXtaStkDayDate)[2]-1),
                                resultSet.getString("w.code")});
                        }
                    }
                }
            }
        }

        return res;
    }

    public void computeIog(final Date date, final double quantity, final boolean system, final int[] iogType, final int adjustment,
            final int item, final int unit, final int[] warehouse, final int nDivisionId, final int estimation, final int version, final int user) {

        mtDate = date;
        mdQuantity = quantity;
        mbSystem = system;
        mnFkIogCategoryId = iogType[0];
        mnFkIogClassId = iogType[1];
        mnFkIogTypeId = iogType[2];
        mnFkIogAdjustmentTypeId = adjustment;
        mnFkItemId = item;
        mnFkUnitId = unit;
        mnFkWarehouseCompanyId = warehouse[0];
        mnFkWarehouseBranchId = warehouse[1];
        mnFkWarehouseWarehouseId = warehouse[2];
        mnFkDivisionId = nDivisionId;
        mnFkMfgEstimationId_n = estimation;
        mnFkMfgEstimationVersionId_n = version;
        mnFkUserInsertId = user;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkIogId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkIogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkIogId = 0;
        mnNumber = 0;
        mtDate = null;
        mdQuantity = 0;
        mbAuthorized = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkIogCategoryId = 0;
        mnFkIogClassId = 0;
        mnFkIogTypeId = 0;
        mnFkIogAdjustmentTypeId = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkWarehouseCompanyId = 0;
        mnFkWarehouseBranchId = 0;
        mnFkWarehouseWarehouseId = 0;
        mnFkDivisionId = 0;
        mnFkIogId_n = 0;
        mnFkMixId_n = 0;
        mnFkExternalDpsYearId_n = 0;
        mnFkExternalDpsDocId_n = 0;
        mnFkExternalDpsEntryId_n = 0;
        mnFkExternalDpsAdjustmentYearId_n = 0;
        mnFkExternalDpsAdjustmentDocId_n = 0;
        mnFkExternalDpsAdjustmentEntryId_n = 0;
        mnFkExternalIogYearId_n = 0;
        mnFkExternalIogDocId_n = 0;
        mnFkExternalIogEntryId_n = 0;
        mnFkTicketId_n = 0;
        mnFkMfgEstimationId_n = 0;
        mnFkMfgEstimationVersionId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mnFkUserAuthorizationId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        mtTsUserAuthorization = null;

        mbXtaValidateWarehouseProduction = false;
        mbXtaValidateTankItems = false;
        mbXtaValidateExportationStock = false;
        mnXtaExternalItemId = 0;
        mnXtaExternalUnitId = 0;
        msXtaIogTypeCode = "";
        msXtaIogTypeName = "";
        msXtaNumber = "";
        msXtaNote = "";
        msXtaDpsBizPartner = "";
        msXtaDpsBizPartnerCode = "";
        mtXtaDpsDate = null;
        msXtaDpsNumberSeries = "";
        msXtaDpsNumberReference = "";
        msXtaDpsItem = "";
        msXtaDpsItemCode = "";
        mdXtaDpsQuantityOriginal = 0;
        mdXtaDpsQuantitySupDev = 0;
        mdXtaDpsQuantityPending = 0;
        msXtaDpsQuantityUnit = "";
        msXtaItem = "";
        msXtaUnit = "";
        mnXtaTicketNumber = 0;
        mnXtaStkDayPkYearId = 0;
        mtXtaStkDayDate = null;
        mdXtaQuantitytoTransfer = 0;

        moIogRegistryB = null;
        moIogRegistryC = null;
        moIogRegistryD = null;
        moIogNote = null;
        moStock = null;

        mvWizardDpsSupplyTicket.clear();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_iog = " + mnPkIogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_iog = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkIogId = 0;

        msSql = "SELECT COALESCE(MAX(id_iog), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkIogId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        int nPkIogRegistryId = 0;
        int[] anTypeIog = null;
        ResultSet resultSet = null;
        Statement statement = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        moIogNote = new SDbIogNote();
        moStock = new SDbStock();

        msSql = "SELECT iog.*, t.code, t.name, tk.num, CONCAT(i.name, ' (', i.code, ')') AS f_item, u.code "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS iog "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_TP) + " AS t ON "
                + "iog.fk_iog_ct = t.id_iog_ct AND iog.fk_iog_cl = t.id_iog_cl AND iog.fk_iog_tp = t.id_iog_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "iog.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON "
                + "iog.fk_unit = u.id_unit "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS tk ON "
                + "iog.fk_tic_n = tk.id_tic "
                + "WHERE iog.id_iog = " + pk[0];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkIogId = resultSet.getInt("iog.id_iog");
            mnNumber = resultSet.getInt("iog.num");
            mtDate = resultSet.getDate("iog.dt");
            mdQuantity = resultSet.getDouble("iog.qty");
            mbAuthorized = resultSet.getBoolean("iog.b_aut");
            mbDeleted = resultSet.getBoolean("iog.b_del");
            mbSystem = resultSet.getBoolean("iog.b_sys");
            mnFkIogCategoryId = resultSet.getInt("iog.fk_iog_ct");
            mnFkIogClassId = resultSet.getInt("iog.fk_iog_cl");
            mnFkIogTypeId = resultSet.getInt("iog.fk_iog_tp");
            mnFkIogAdjustmentTypeId = resultSet.getInt("iog.fk_iog_adj_tp");
            mnFkItemId = resultSet.getInt("iog.fk_item");
            mnFkUnitId = resultSet.getInt("iog.fk_unit");
            mnFkWarehouseCompanyId = resultSet.getInt("iog.fk_wah_co");
            mnFkWarehouseBranchId = resultSet.getInt("iog.fk_wah_cob");
            mnFkWarehouseWarehouseId = resultSet.getInt("iog.fk_wah_wah");
            mnFkDivisionId = resultSet.getInt("iog.fk_div");
            mnFkIogId_n = resultSet.getInt("iog.fk_iog_n");
            if (resultSet.wasNull()) { mnFkIogId_n = 0; }
            mnFkMixId_n = resultSet.getInt("iog.fk_mix_n");
            if (resultSet.wasNull()) { mnFkMixId_n = 0; }
            mnFkExternalDpsYearId_n = resultSet.getInt("iog.fk_ext_dps_year_n");
            if (resultSet.wasNull()) { mnFkExternalDpsYearId_n = 0; }
            if (resultSet.wasNull()) { mnFkExternalDpsYearId_n = 0; }
            mnFkExternalDpsDocId_n = resultSet.getInt("iog.fk_ext_dps_doc_n");
            if (resultSet.wasNull()) { mnFkExternalDpsDocId_n = 0; }
            mnFkExternalDpsEntryId_n = resultSet.getInt("iog.fk_ext_dps_ety_n");
            if (resultSet.wasNull()) { mnFkExternalDpsEntryId_n = 0; }
            mnFkExternalDpsAdjustmentYearId_n = resultSet.getInt("iog.fk_ext_adj_year_n");
            if (resultSet.wasNull()) { mnFkExternalDpsAdjustmentYearId_n = 0; }
            mnFkExternalDpsAdjustmentDocId_n = resultSet.getInt("iog.fk_ext_adj_doc_n");
            if (resultSet.wasNull()) { mnFkExternalDpsAdjustmentDocId_n = 0; }
            mnFkExternalDpsAdjustmentEntryId_n = resultSet.getInt("iog.fk_ext_adj_ety_n");
            if (resultSet.wasNull()) { mnFkExternalDpsAdjustmentEntryId_n = 0; }
            mnFkExternalIogYearId_n = resultSet.getInt("iog.fk_ext_iog_year_n");
            if (resultSet.wasNull()) { mnFkExternalIogYearId_n = 0; }
            mnFkExternalIogDocId_n = resultSet.getInt("iog.fk_ext_iog_doc_n");
            if (resultSet.wasNull()) { mnFkExternalIogDocId_n = 0; }
            mnFkExternalIogEntryId_n = resultSet.getInt("iog.fk_ext_iog_ety_n");
            if (resultSet.wasNull()) { mnFkExternalIogEntryId_n = 0; }
            mnFkTicketId_n = resultSet.getInt("iog.fk_tic_n");
            if (resultSet.wasNull()) { mnFkTicketId_n = 0; }
            mnFkMfgEstimationId_n = resultSet.getInt("iog.fk_mfg_est_n");
            if (resultSet.wasNull()) { mnFkMfgEstimationId_n = 0; }
            mnFkMfgEstimationVersionId_n = resultSet.getInt("iog.fk_mfg_est_ver_n");
            if (resultSet.wasNull()) { mnFkMfgEstimationVersionId_n = 0; }
            mnFkUserInsertId = resultSet.getInt("iog.fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("iog.fk_usr_upd");
            mnFkUserAuthorizationId = resultSet.getInt("iog.fk_usr_aut");
            mtTsUserInsert = resultSet.getTimestamp("iog.ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("iog.ts_usr_upd");
            mtTsUserAuthorization = resultSet.getTimestamp("iog.ts_usr_aut");

            msXtaIogTypeCode = resultSet.getString("t.code");
            msXtaIogTypeName = resultSet.getString("t.name");
            msXtaNumber = msXtaIogTypeCode + "-" + mnNumber;
            mnXtaTicketNumber = resultSet.getInt("tk.num");
            msXtaItem = resultSet.getString("f_item");
            msXtaUnit = resultSet.getString("u.code");

            if (resultSet.wasNull()) { mnXtaTicketNumber = 0; }

            // Read iog destiny registries:

            msSql = "SELECT iog.id_iog, iog.fk_iog_ct, iog.fk_iog_cl, iog.fk_iog_tp "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS iog "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_TP) + " AS t ON "
                + "iog.fk_iog_ct = t.id_iog_ct AND iog.fk_iog_cl = t.id_iog_cl AND iog.fk_iog_tp = t.id_iog_tp "
                + "WHERE iog.fk_iog_n = " + pk[0] + " "
                + "ORDER BY iog.id_iog ";
            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(msSql);
            while(resultSet.next()) {
                nPkIogRegistryId = resultSet.getInt("iog.id_iog");
                anTypeIog = new int[] { resultSet.getInt("iog.fk_iog_ct"), resultSet.getInt("iog.fk_iog_cl"), resultSet.getInt("iog.fk_iog_tp") };

                if (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_INT_TRA, anTypeIog) ||
                    SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_INT_CNV, anTypeIog) ||
                    SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_INT_MIX_PAS, anTypeIog) ||
                    SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_INT_MIX_ACT, anTypeIog) ) {

                    if (moIogRegistryB == null) {
                        moIogRegistryB = readRegistryDestiny(session, moIogRegistryB, nPkIogRegistryId);
                    }
                    else if (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_INT_MIX_ACT, anTypeIog)) {
                        moIogRegistryD = readRegistryDestiny(session, moIogRegistryD, nPkIogRegistryId);
                    }
                }

                if (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_OUT_INT_MIX_ACT, anTypeIog)) {
                    moIogRegistryC = readRegistryDestiny(session, moIogRegistryC, nPkIogRegistryId);
                }
            }

            // Read note:

            msSql = "SELECT id_iog, id_note FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG_NOTE) + " WHERE id_iog = " + mnPkIogId + " ";
            resultSet = statement.executeQuery(msSql);
            if (!resultSet.next()) {
                //throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                moIogNote.read(session, new int[] { resultSet.getInt("id_iog"), resultSet.getInt("id_note") });
                msXtaNote = moIogNote.getNote();
            }

            // Read stock:

            msSql = "SELECT id_year, id_item, id_unit, id_co, id_cob, id_wah, id_div, id_mov " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " WHERE fk_iog = " + mnPkIogId + " ";
            resultSet = statement.executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                moStock.read(session, new int[] {
                    resultSet.getInt("id_year"),
                    resultSet.getInt("id_item"),
                    resultSet.getInt("id_unit"),
                    resultSet.getInt("id_co"),
                    resultSet.getInt("id_cob"),
                    resultSet.getInt("id_wah"),
                    resultSet.getInt("id_div"),
                    resultSet.getInt("id_mov")
                });
            }

            // Read dps information:

            if (mnFkExternalDpsAdjustmentYearId_n > 0 && mnFkExternalDpsAdjustmentDocId_n > 0 && mnFkExternalDpsAdjustmentEntryId_n > 0) {
                obtainDps(session, new int[] { mnFkExternalDpsAdjustmentYearId_n, mnFkExternalDpsAdjustmentDocId_n, mnFkExternalDpsAdjustmentEntryId_n });
            }
            else if (mnFkExternalDpsYearId_n > 0 && mnFkExternalDpsDocId_n > 0 && mnFkExternalDpsEntryId_n > 0) {
                obtainDps(session, new int[] { mnFkExternalDpsYearId_n, mnFkExternalDpsDocId_n, mnFkExternalDpsEntryId_n });
            }

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            computeNumber(session);
            mbUpdatable = true;
            mbDisableable = false;
            mbDeletable = true;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            mnFkUserAuthorizationId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkIogId + ", " +
                    mnNumber + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mdQuantity + ", " +
                    (mbAuthorized ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkIogCategoryId + ", " +
                    mnFkIogClassId + ", " +
                    mnFkIogTypeId + ", " +
                    mnFkIogAdjustmentTypeId + ", " +
                    mnFkItemId + ", " +
                    mnFkUnitId + ", " +
                    mnFkWarehouseCompanyId + ", " +
                    mnFkWarehouseBranchId + ", " +
                    mnFkWarehouseWarehouseId + ", " +
                    mnFkDivisionId + ", " +
                    (mnFkIogId_n > 0 ? mnFkIogId_n : "NULL") + ", " +
                    (mnFkMixId_n > 0 ? mnFkMixId_n : "NULL") + ", " +
                    (mnFkExternalDpsYearId_n > 0 ? mnFkExternalDpsYearId_n : "NULL") + ", " +
                    (mnFkExternalDpsDocId_n > 0 ? mnFkExternalDpsDocId_n : "NULL") + ", " +
                    (mnFkExternalDpsEntryId_n > 0 ? mnFkExternalDpsEntryId_n : "NULL") + ", " +
                    (mnFkExternalDpsAdjustmentYearId_n > 0 ? mnFkExternalDpsAdjustmentYearId_n : "NULL") + ", " +
                    (mnFkExternalDpsAdjustmentDocId_n > 0 ? mnFkExternalDpsAdjustmentDocId_n : "NULL") + ", " +
                    (mnFkExternalDpsAdjustmentEntryId_n > 0 ? mnFkExternalDpsAdjustmentEntryId_n : "NULL") + ", " +
                    (mnFkExternalIogYearId_n > 0 ? mnFkExternalIogYearId_n : "NULL") + ", " +
                    (mnFkExternalIogDocId_n > 0 ? mnFkExternalIogDocId_n : "NULL") + ", " +
                    (mnFkExternalIogEntryId_n > 0 ? mnFkExternalIogEntryId_n : "NULL") + ", " +
                    (mnFkTicketId_n > 0 ? mnFkTicketId_n : "NULL") + ", " +
                    (mnFkMfgEstimationId_n > 0 ? mnFkMfgEstimationId_n : "NULL") + ", " +
                    (mnFkMfgEstimationVersionId_n > 0 ? mnFkMfgEstimationVersionId_n : "NULL") + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    mnFkUserAuthorizationId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_iog = " + mnPkIogId + ", " +
                    "num = " + mnNumber + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "qty = " + mdQuantity + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_iog_ct = " + mnFkIogCategoryId + ", " +
                    "fk_iog_cl = " + mnFkIogClassId + ", " +
                    "fk_iog_tp = " + mnFkIogTypeId + ", " +
                    "fk_iog_adj_tp = " + mnFkIogAdjustmentTypeId + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_wah_co = " + mnFkWarehouseCompanyId + ", " +
                    "fk_wah_cob = " + mnFkWarehouseBranchId + ", " +
                    "fk_wah_wah = " + mnFkWarehouseWarehouseId + ", " +
                    "fk_div = " + mnFkDivisionId + ", " +
                    "fk_iog_n = " + (mnFkIogId_n > 0 ? mnFkIogId_n : "NULL") + ", " +
                    "fk_mix_n = " + (mnFkMixId_n > 0 ? mnFkMixId_n : "NULL") + ", " +
                    "fk_ext_dps_year_n = " + (mnFkExternalDpsYearId_n > 0 ? mnFkExternalDpsYearId_n : "NULL") + ", " +
                    "fk_ext_dps_doc_n = " + (mnFkExternalDpsDocId_n > 0 ? mnFkExternalDpsDocId_n : "NULL") + ", " +
                    "fk_ext_dps_ety_n = " + (mnFkExternalDpsEntryId_n > 0 ? mnFkExternalDpsEntryId_n : "NULL") + ", " +
                    "fk_ext_adj_year_n = " + (mnFkExternalDpsAdjustmentYearId_n > 0 ? mnFkExternalDpsAdjustmentYearId_n : "NULL") + ", " +
                    "fk_ext_adj_doc_n = " + (mnFkExternalDpsAdjustmentDocId_n > 0 ? mnFkExternalDpsAdjustmentDocId_n : "NULL") + ", " +
                    "fk_ext_adj_ety_n = " + (mnFkExternalDpsAdjustmentEntryId_n > 0 ? mnFkExternalDpsAdjustmentEntryId_n : "NULL") + ", " +
                    "fk_ext_iog_year_n = " + (mnFkExternalIogYearId_n > 0 ? mnFkExternalIogYearId_n : "NULL") + ", " +
                    "fk_ext_iog_doc_n = " + (mnFkExternalIogDocId_n > 0 ? mnFkExternalIogDocId_n : "NULL") + ", " +
                    "fk_ext_iog_ety_n = " + (mnFkExternalIogEntryId_n > 0 ? mnFkExternalIogEntryId_n : "NULL") + ", " +
                    "fk_tic_n = " + (mnFkTicketId_n > 0 ? mnFkTicketId_n : "NULL") + ", " +
                    "fk_mfg_est_n = " + (mnFkMfgEstimationId_n > 0 ? mnFkMfgEstimationId_n : "NULL") + ", " +
                    "fk_mfg_est_ver_n = " + (mnFkMfgEstimationVersionId_n > 0 ? mnFkMfgEstimationVersionId_n : "NULL") + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "fk_usr_aut = " + mnFkUserAuthorizationId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + ", " +
                    "ts_usr_aut = " + "NOW()" + " " +
                    getSqlWhere();
        }
        session.getStatement().execute(msSql);

        if (mbRegistryNew) {

            // Save note:

            moIogNote = new SDbIogNote();
            moIogNote.setPkIogId(mnPkIogId);
            moIogNote.setNote(msXtaNote);
            moIogNote.save(session);

            // Save stock:

            moStock = new SDbStock();

            moStock.setPkYearId(SLibTimeUtils.digestYear(mtDate)[0]);
            moStock.setPkItemId(mnFkItemId);
            moStock.setPkUnitId(mnFkUnitId);
            moStock.setPkCompanyId(mnFkWarehouseCompanyId);
            moStock.setPkBranchId(mnFkWarehouseBranchId);
            moStock.setPkWarehouseId(mnFkWarehouseWarehouseId);
            moStock.setPkDivisionId(mnFkDivisionId);
            moStock.setDate(mtDate);
            moStock.setMoveIn(SModSysConsts.SS_IOG_CT_IN == mnFkIogCategoryId ? mdQuantity : 0);
            moStock.setMoveOut(SModSysConsts.SS_IOG_CT_OUT == mnFkIogCategoryId ? mdQuantity : 0);
            moStock.setDeleted(false);
            moStock.setSystem(true);
            moStock.setFkIogCategoryId(mnFkIogCategoryId);
            moStock.setFkIogClassId(mnFkIogClassId);
            moStock.setFkIogTypeId(mnFkIogTypeId);
            moStock.setFkIogId(mnPkIogId);
            moStock.save(session);
        }
        else {
            // Save note:

            moIogNote.setNote(msXtaNote);
            moIogNote.save(session);

            // Save stock:

            moStock.setDate(mtDate);
            moStock.setMoveIn(SModSysConsts.SS_IOG_CT_IN == mnFkIogCategoryId ? mdQuantity : 0);
            moStock.setMoveOut(SModSysConsts.SS_IOG_CT_OUT == mnFkIogCategoryId ? mdQuantity : 0);
            moStock.setDeleted(mbDeleted);
            moStock.setRegistryNew(false);
            moStock.save(session);
        }

        // Save iog's destiny and transfer moves if is necessary:

        if (SLibUtils.compareKeys(new int[] { mnFkIogCategoryId, mnFkIogClassId, mnFkIogTypeId }, SModSysConsts.SS_IOG_TP_OUT_INT_MIX_ACT)) {
            if (moIogRegistryD != null) {
                moIogRegistryD.setXtaValidateTankItems(true);
            }

            if (moIogRegistryB != null) {
                moIogRegistryB.setXtaValidateTankItems(true);
            }
        }

        if (saveRegistryDestiny(session, moIogRegistryC, mnPkIogId) &&
            saveRegistryDestiny(session, moIogRegistryD, mnPkIogId) &&
            saveRegistryDestiny(session, moIogRegistryB, mnPkIogId) &&
            saveTransferMoves(session)) {

                mbRegistryNew = false;
                mnQueryResultId = SDbConsts.SAVE_OK;
        }
        else {
            throw new Exception(msQueryResult);
        }
    }

    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        mbDeleted = !mbDeleted;
        mnFkUserUpdateId = session.getUser().getPkUserId();

        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();
        session.getStatement().execute(msSql);

        // Delete obtainStock move:

        moStock.setDeleted(mbDeleted ? false : true);
        moStock.delete(session);

        // Delete iog's destiny if is necessary:

        if (moIogRegistryC != null) {
            msQueryResult = deleteRegistryDestiny(session, moIogRegistryC, mbDeleted);
        }

        if (moIogRegistryD != null && msQueryResult.isEmpty()) {
            msQueryResult = deleteRegistryDestiny(session, moIogRegistryD, mbDeleted);
        }

        if (moIogRegistryB != null && msQueryResult.isEmpty()) {
            msQueryResult = deleteRegistryDestiny(session, moIogRegistryB, mbDeleted);
        }

        if (!msQueryResult.isEmpty()) {
            throw new Exception(msQueryResult);
        }

        mnQueryResultId = msQueryResult.isEmpty() ? SDbConsts.SAVE_OK : SDbConsts.SAVE_ERROR;
    }

    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        boolean result = validateInOutMoves(session, true);

        /*
        if (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_OUT_INT_CNV, new int[] { mnFkIogCategoryId, mnFkIogClassId, mnFkIogTypeId })) {
            result = validateInOutMoves(session, true);
        }
        else {
            result = validateInOutMoves(session, true);
        }

        if (result && moIogRegistryC != null) {
            result = moIogRegistryC.validateInOutMoves(session, true);
            msQueryResult = moIogRegistryC.msQueryResult;
        }

        if (result && moIogRegistryD != null) {
            result = moIogRegistryD.validateInOutMoves(session, true);
            msQueryResult = moIogRegistryD.msQueryResult;
        }
        */

        if (result && moIogRegistryB != null) {
            result = moIogRegistryB.validateInOutMoves(session, true);

            if (!result) {
                msQueryResult = moIogRegistryB.msQueryResult;
            }
        }

        return result;
    }

    @Override
    public boolean canSave(SGuiSession session) throws SQLException, Exception {
        boolean result =  validateInOutMoves(session, false);

        /* 27/10/2014 jbarajas remove all traces of control mixtures.
         *
        if (result && moIogRegistryC != null) {
            result = moIogRegistryC.validateInOutMoves(session, false);
            msQueryResult = moIogRegistryC.msQueryResult;
        }

        if (result && moIogRegistryD != null) {
            result = moIogRegistryD.validateInOutMoves(session, false);
            msQueryResult = moIogRegistryD.msQueryResult;
        }
        */

        if (result && moIogRegistryB != null) {
            /* 27/10/2014 jbarajas remove all traces of control mixtures.
             *
            if (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_OUT_INT_MIX_ACT, new int[] { mnFkIogCategoryId, mnFkIogClassId, mnFkIogTypeId })) {
                result = moIogRegistryB.validateInOutMoves(session, false);
            }
            else {
            */
            result = moIogRegistryB.validateInOutMoves(session, false);
            //}

            msQueryResult = moIogRegistryB.msQueryResult;
        }

        return result;
    }

    @Override
    public SDbIog clone() throws CloneNotSupportedException {
        SDbIog registry = new SDbIog();

        registry.setPkIogId(this.getPkIogId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setQuantity(this.getQuantity());
        registry.setAuthorized(this.isAuthorized());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkIogCategoryId(this.getFkIogCategoryId());
        registry.setFkIogClassId(this.getFkIogClassId());
        registry.setFkIogTypeId(this.getFkIogTypeId());
        registry.setFkIogAdjustmentTypeId(this.getFkIogAdjustmentTypeId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkWarehouseCompanyId(this.getFkWarehouseCompanyId());
        registry.setFkWarehouseBranchId(this.getFkWarehouseBranchId());
        registry.setFkWarehouseWarehouseId(this.getFkWarehouseWarehouseId());
        registry.setFkDivisionId(this.getFkDivisionId());
        registry.setFkIogId_n(this.getFkIogId_n());
        registry.setFkMixId_n(this.getFkMixId_n());
        registry.setFkExternalDpsYearId_n(this.getFkExternalDpsYearId_n());
        registry.setFkExternalDpsDocId_n(this.getFkExternalDpsDocId_n());
        registry.setFkExternalDpsEntryId_n(this.getFkExternalDpsEntryId_n());
        registry.setFkExternalDpsAdjustmentYearId_n(this.getFkExternalDpsAdjustmentYearId_n());
        registry.setFkExternalDpsAdjustmentDocId_n(this.getFkExternalDpsAdjustmentDocId_n());
        registry.setFkExternalDpsAdjustmentEntryId_n(this.getFkExternalDpsAdjustmentEntryId_n());
        registry.setFkExternalIogYearId_n(this.getFkExternalIogYearId_n());
        registry.setFkExternalIogDocId_n(this.getFkExternalIogDocId_n());
        registry.setFkExternalIogEntryId_n(this.getFkExternalIogEntryId_n());
        registry.setFkTicketId_n(this.getFkTicketId_n());
        registry.setFkMfgEstimationId_n(this.getFkMfgEstimationId_n());
        registry.setFkMfgEstimationVersionId_n(this.getFkMfgEstimationVersionId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setFkUserAuthorizationId(this.getFkUserAutorizationId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        registry.setTsUserAuthorization(this.getTsUserAuthorization());

        registry.setXtaValidateExportationStock(this.getXtaValidateExportationStock());
        registry.setXtaIogTypeCode(this.getXtaIogTypeCode());
        registry.setXtaIogTypeName(this.getXtaIogTypeName());

        if (this.moIogRegistryB != null) {
            registry.setIogRegistryB(this.moIogRegistryB.clone());
        }

        if (this.moIogRegistryC != null) {
            registry.setIogRegistryC(this.moIogRegistryC.clone());
        }

        if (this.moIogRegistryD != null) {
            registry.setIogRegistryD(this.moIogRegistryD.clone());
        }

        if (this.moIogNote != null) {
            registry.setIogNote(this.moIogNote.clone());
        }

        if (this.moStock != null) {
            registry.setStock(this.moStock.clone());
        }

        if (this.maTransferMoves != null) {
            registry.getTransferMoves().clear();
            for (Object[] obj : this.maTransferMoves) {
                registry.getTransferMoves().add(obj);
            }
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_EXTERNAL_DPS:
                msSql += "fk_ext_dps_year_n = " + ((int []) value)[0] + ", fk_ext_dps_doc_n = " + ((int []) value)[1] +
                        ", fk_ext_dps_ety_n = " + ((int []) value)[2] + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    public void obtainXtaValues(SGuiSession session, int[] key) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT st.code, st.name " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.SS_IOG_TP) + " AS st " +
            "WHERE st.id_iog_ct = " + key[0] + " AND st.id_iog_cl = " + key[1] + " AND st.id_iog_tp = " + key[2] + " AND st.b_del = 0 " +
            "ORDER BY st.code, st.name ";

        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            msXtaIogTypeCode = resultSet.getString("code");
            msXtaIogTypeName = resultSet.getString("name");
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    public void obtainDps(SGuiSession session, int[] nFkDpsEntryId) throws SQLException, Exception {
        ResultSet resultSet = null;
        Statement statement = null;

        msSql = obtainQueryByDps(session, new int[] { mnFkIogCategoryId, mnFkIogClassId, mnFkIogTypeId }, nFkDpsEntryId);
        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnXtaExternalItemId = resultSet.getInt("v.fid_item");
            mnXtaExternalUnitId = resultSet.getInt("v.fid_unit");
            msXtaDpsBizPartner = resultSet.getString("b.bp");
            msXtaDpsBizPartnerCode = resultSet.getString("bc.bp_key");
            mtXtaDpsDate = resultSet.getDate("d.dt");
            msXtaDpsNumberSeries = resultSet.getString("f_num");
            msXtaDpsNumberReference = resultSet.getString("d.num_ref");
            msXtaDpsItem = resultSet.getString("i.item");
            msXtaDpsItemCode = resultSet.getString("i.item_key");
            mdXtaDpsQuantityOriginal = resultSet.getDouble("f_orig_qty");
            msXtaDpsQuantityUnit = resultSet.getString("f_orig_unit");

            if (SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_IN_PUR_PUR, new int[] { mnFkIogCategoryId, mnFkIogClassId, mnFkIogTypeId }) ||
                SLibUtils.compareKeys(SModSysConsts.SS_IOG_TP_OUT_SAL_SAL, new int[] { mnFkIogCategoryId, mnFkIogClassId, mnFkIogTypeId })) {

                mnFkExternalDpsYearId_n = resultSet.getInt("v.id_year");
                mnFkExternalDpsDocId_n = resultSet.getInt("v.id_doc");
                mnFkExternalDpsEntryId_n = resultSet.getInt("v.id_ety");

                mdXtaDpsQuantitySupDev = resultSet.getDouble("f_sup_qty");
            }
            else {

                mnFkExternalDpsYearId_n = resultSet.getInt("f_adj_year");
                mnFkExternalDpsDocId_n = resultSet.getInt("f_adj_doc");
                mnFkExternalDpsEntryId_n = resultSet.getInt("f_adj_ety");

                mnFkExternalDpsAdjustmentYearId_n = resultSet.getInt("v.id_year");
                mnFkExternalDpsAdjustmentDocId_n = resultSet.getInt("v.id_doc");
                mnFkExternalDpsAdjustmentEntryId_n = resultSet.getInt("v.id_ety");

                mdXtaDpsQuantitySupDev = resultSet.getDouble("f_ret_qty");
            }

            mdXtaDpsQuantityPending = mdXtaDpsQuantityOriginal - mdXtaDpsQuantitySupDev;
            mdQuantity = mnPkIogId > 0 ? mdQuantity : mdXtaDpsQuantityPending;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    public void obtainTicket(SGuiSession session, int nPkTicketId) throws SQLException, Exception {
        ResultSet resultSet = null;
        Statement statement = null;

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(obtainTicketQuery(nPkTicketId));
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            msXtaNumber = resultSet.getString("v.num");
            mnFkItemId = resultSet.getInt("v.fk_item");
            mnFkTicketId_n = resultSet.getInt("v.id_tic");
            mnFkWarehouseCompanyId = resultSet.getInt("f_fk_wah_co_n");
            mnFkWarehouseBranchId = resultSet.getInt("f_fk_wah_cob_n");
            mnFkWarehouseWarehouseId = resultSet.getInt("f_fk_wah_wah_n");
            mnFkDivisionId = resultSet.getInt("f_fk_div");
            mdQuantity = mnPkIogId > 0 ? mdQuantity : (resultSet.getDouble("v.qty") - resultSet.getDouble("f_qty"));
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }
}
