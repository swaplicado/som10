/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomWizardDpsPurchasesSalesSupply implements SGridRow {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected String msBizPartnerKey;
    protected String msBizPartner;
    protected String msBizPartnerBranch;
    protected String msDpsType;
    protected String msNumber;
    protected String msNumberReference;
    protected Date mtDate;
    protected String msCompanyBranchCode;
    protected double mdTotalCurrency;
    protected String msCurrencyKey;
    protected String msItemKey;
    protected String msItem;
    protected double mdOriginalQuantity;
    protected double mdAdjustmentQuantity;
    protected double mdQuantityNet;
    protected String msQuantityUnit;
    protected double mdQuantitySupply;
    protected double mdQuantityPending;
    protected boolean mbClose;
    protected String msUserClose;
    protected Date mtDatetimeClose;

    protected Vector<SSomWizardDpsPurchasesSalesSupply> mvWizardDpsPurchasesSalesSupply;

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setBizPartnerKey(String s) { msBizPartnerKey = s; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setBizPartnerBranch(String s) { msBizPartnerBranch = s; }
    public void setDpsType(String s) { msDpsType = s; }
    public void setNumber(String s) { msNumber = s; }
    public void setNumberReference(String s) { msNumberReference = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setCompanyBranchCode(String s) { msCompanyBranchCode = s; }
    public void setTotalCurrency(double d) { mdTotalCurrency = d; }
    public void setCurrencyKey(String s) { msCurrencyKey = s; }
    public void setItemKey(String s) { msItemKey = s; }
    public void setItem(String s) { msItem = s; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }
    public void setAdjustmentQuantity(double d) { mdAdjustmentQuantity = d; }
    public void setQuantityNet(double d) { mdQuantityNet = d; }
    public void setQuantityUnit(String s) { msQuantityUnit = s; }
    public void setQuantitySupply(double d) { mdQuantitySupply = d; }
    public void setQuantityPending(double d) { mdQuantityPending = d; }
    public void setClose(boolean b) { mbClose = b; }
    public void setUserClose(String s) { msUserClose = s; }
    public void setDatetimeClose(Date t) { mtDatetimeClose = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId ; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public String getBizPartnerKey() { return msBizPartnerKey; }
    public String getBizPartner() { return msBizPartner; }
    public String getBizPartnerBranch() { return msBizPartnerBranch; }
    public String getDpsType() { return msDpsType; }
    public String getNumber() { return msNumber; }
    public String getNumberReference() { return msNumberReference; }
    public Date getDate() { return mtDate; }
    public String getCompanyBranchCode() { return msCompanyBranchCode; }
    public double getTotalCurrency() { return mdTotalCurrency; }
    public String getCurrencyKey() { return msCurrencyKey; }
    public String getItemKey() { return msItemKey; }
    public String getItem() { return msItem; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    public double getAdjustmentQuantity() { return mdAdjustmentQuantity; }
    public double getQuantityNet() { return mdQuantityNet; }
    public String getQuantityUnit() { return msQuantityUnit; }
    public double getQuantitySupply() { return mdQuantitySupply; }
    public double getQuantityPending() { return mdQuantityPending; }
    public boolean getClose() { return mbClose; }
    public String getUserClose() { return msUserClose; }
    public Date getDatetimeClose() { return mtDatetimeClose; }

    public Vector<SSomWizardDpsPurchasesSalesSupply> getWizardDpsPurchaseSalesSupply() { return mvWizardDpsPurchasesSalesSupply; }

    public SSomWizardDpsPurchasesSalesSupply() {
        mvWizardDpsPurchasesSalesSupply = new Vector<SSomWizardDpsPurchasesSalesSupply>();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        msBizPartnerKey = "";
        msBizPartner = "";
        msBizPartnerBranch = "";
        msDpsType = "";
        msNumber = "";
        msNumberReference = "";
        mtDate = null;
        msCompanyBranchCode = "";
        mdTotalCurrency = 0;
        msCurrencyKey = "";
        msItemKey = "";
        msItem = "";
        mdOriginalQuantity = 0;
        mdAdjustmentQuantity = 0;
        mdQuantityNet = 0;
        msQuantityUnit = "";
        mdQuantitySupply = 0;
        mdQuantityPending = 0;
        mbClose = false;
        msUserClose = "";
        mtDatetimeClose = null;

        mvWizardDpsPurchasesSalesSupply.clear();
    }

    public void read(SGuiSession session, String sqlFilter, String sqlDiogPeriod, String sqlBizPartner, String sqlOrderByDocEty, String sDatabaseCoName,
            String sDatabaseName, String sDatabaseErpName, int type, int subtype, int nFkUnitid) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;
        SSomWizardDpsPurchasesSalesSupply wizard = null;

        sql = getQuery(sqlFilter, sqlDiogPeriod, sqlBizPartner, sqlOrderByDocEty, sDatabaseCoName, sDatabaseName, sDatabaseErpName, type, subtype, nFkUnitid);
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            wizard = new SSomWizardDpsPurchasesSalesSupply();

            wizard.setPkYearId(resultSet.getInt("f_id_1"));
            wizard.setPkDocId(resultSet.getInt("f_id_2"));
            wizard.setPkEntryId(resultSet.getInt("f_id_3"));
            wizard.setFkItemId(resultSet.getInt("f_id_4"));
            wizard.setFkUnitId(resultSet.getInt("f_id_5"));
            wizard.setBizPartnerKey(resultSet.getString("bp_key"));
            wizard.setBizPartner(resultSet.getString("bp"));
            wizard.setBizPartnerBranch(resultSet.getString("bpb"));
            wizard.setDpsType(resultSet.getString("f_dt_code"));
            wizard.setNumber(resultSet.getString("f_num"));
            wizard.setNumberReference(resultSet.getString("num_ref"));
            wizard.setDate(resultSet.getDate("dt"));
            wizard.setCompanyBranchCode(resultSet.getString("f_cb_code"));
            wizard.setTotalCurrency(resultSet.getDouble("tot_cur_r"));
            wizard.setCurrencyKey(resultSet.getString("cur_key"));
            wizard.setItemKey(resultSet.getString("item_key"));
            wizard.setItem(resultSet.getString("item"));
            wizard.setOriginalQuantity(resultSet.getDouble("f_orig_qty"));
            wizard.setAdjustmentQuantity(resultSet.getDouble("f_adj_orig_qty"));
            wizard.setQuantityNet(resultSet.getDouble("f_orig_qty") - resultSet.getDouble("f_adj_orig_qty"));
            wizard.setQuantityUnit(resultSet.getString("f_orig_unit"));
            wizard.setQuantitySupply(resultSet.getDouble("f_sup_qty"));
            wizard.setQuantityPending(resultSet.getDouble("f_orig_qty") - resultSet.getDouble("f_adj_orig_qty") - resultSet.getDouble("f_sup_qty"));
            wizard.setClose(resultSet.getBoolean("b_close"));
            wizard.setUserClose(resultSet.getString("usr"));
            wizard.setDatetimeClose(resultSet.getTimestamp("ts_close"));

            mvWizardDpsPurchasesSalesSupply.add(wizard);
        }
    }

    public String getQuery(String sqlFilter, String sqlDiogPeriod, String sqlBizPartner, String sqlOrderByDocEty, String sDatabaseCoName,
            String sDatabaseName, String sDatabaseErpName, int type, int subtype, int nFkUnitid) {
        String sql = "";

        sql = "SELECT " +
                "v.id_year AS " + SDbConsts.FIELD_ID + "1, " +
                "v.id_doc AS " + SDbConsts.FIELD_ID + "2, "  +
                "v.id_ety AS " + SDbConsts.FIELD_ID + "3, " +
                "v.fid_item AS " + SDbConsts.FIELD_ID + "4, " +
                "v.fid_unit AS " + SDbConsts.FIELD_ID + "5, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "v.fid_item, v.fid_unit, v.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "v.qty AS f_qty, v.orig_qty AS f_orig_qty, " +
                "COALESCE((SELECT SUM(ddd.qty) FROM " + sDatabaseCoName + ".trn_dps_dps_adj AS ddd, " + sDatabaseCoName + ".trn_dps_ety AS dae, " +
                sDatabaseCoName + ".trn_dps AS da WHERE " +
                "ddd.id_dps_year = v.id_year AND ddd.id_dps_doc = v.id_doc AND ddd.id_dps_ety = v.id_ety AND " +
                "ddd.id_adj_year = dae.id_year AND ddd.id_adj_doc = dae.id_doc AND ddd.id_adj_ety = dae.id_ety AND " +
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
                //"ge.b_del = 0), 0) AS f_sup_qty, " +
                sqlDiogPeriod + " AND ge.b_del = 0), 0) AS f_sup_qty, " +
                "COALESCE(( " +
                "SELECT IF(d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0, 1, 0) " +
                "FROM " + sDatabaseName + ".s_dps_ass AS som WHERE d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0), 0) AS f_exist, " +
                "'' AS " + SDbConsts.FIELD_CODE + ", 'Partida del documento' AS " + SDbConsts.FIELD_NAME + " " +
                "FROM " + sDatabaseCoName + ".trn_dps AS d " +
                "INNER JOIN " + sDatabaseErpName + ".trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                "d.b_close = 0 AND d.b_del = 0 AND d.fid_st_dps = " + SModSysConsts.EXT_TRNS_ST_DPS_EMITED + " AND " +
                "d.fid_ct_dps = " + (type == SModConsts.SX_IOG_SUP_SAL ? SModSysConsts.EXT_TRNS_CL_DPS_SAL_DOC[0] : SModSysConsts.EXT_TRNS_CL_DPS_PUR_DOC[0]) +
                " AND d.fid_cl_dps = " + (type == SModConsts.SX_IOG_SUP_SAL ? SModSysConsts.EXT_TRNS_CL_DPS_SAL_DOC[1] : SModSysConsts.EXT_TRNS_CL_DPS_PUR_DOC[1]) + " " + sqlFilter + " " +
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
                "GROUP BY v.id_year, v.id_doc, v.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "v.fid_item, v.fid_unit, v.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, " +
                "v.qty, v.orig_qty ";

        if (subtype == SModConsts.SX_IOG_SUP_ADJ_PEN) { // || mnTabType == SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY) {
            sql += "HAVING (f_exist = 0 AND (f_orig_qty - f_adj_orig_qty - f_sup_qty) <> 0) ";
        }
        else {
            sql += "HAVING f_exist = 1 OR ((f_orig_qty - f_adj_orig_qty - f_sup_qty) = 0) ";
        }

        sql += "ORDER BY " + sqlOrderByDocEty + "; ";

        return sql;
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(final boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msBizPartnerKey;
                break;
            case 1:
                value = msBizPartner;
                break;
            case 2:
                value = msBizPartnerBranch;
                break;
            case 3:
                value = msDpsType;
                break;
            case 4:
                value = msNumber;
                break;
            case 5:
                value = msNumberReference;
                break;
            case 6:
                value = mtDate;
                break;
            case 7:
                value = msCompanyBranchCode;
                break;
            case 8:
                value = mdTotalCurrency;
                break;
            case 9:
                value = msCurrencyKey;
                break;
            case 10:
                value = msItemKey;
                break;
            case 11:
                value = msItem;
                break;
            case 12:
                value = mdOriginalQuantity;
                break;
            case 13:
                value = mdAdjustmentQuantity;
                break;
            case 14:
                value = mdQuantityNet;
                break;
            case 15:
                value = msQuantityUnit;
                break;
            case 16:
                value = mdQuantitySupply;
                break;
            case 17:
                value = mdQuantityPending;
                break;
            case 18:
                value = msQuantityUnit;
                break;
            case 19:
                value = mbClose;
                break;
            case 20:
                value = msUserClose;
                break;
            case 21:
                value = mtDatetimeClose;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                break;
            default:
        }
    }
}
