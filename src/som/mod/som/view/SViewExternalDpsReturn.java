/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbDpsAssorted;
import som.mod.som.db.SDbItem;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewExternalDpsReturn extends SGridPaneView implements ActionListener {

    String msSqlDiogPeriod;

    private SGridFilterDatePeriod moFilterDate;
    private SGridFilterDateCutOff moFilterDateCutOff;

    private JButton moButtonReturnIog;
    private JButton moButtonCloseIog;
    private JButton moButtonOpenIog;

    public SViewExternalDpsReturn(SGuiClient client, String title, int nType, int nSubtype) {
        super(client, SGridConsts.GRID_PANE_VIEW, nType, nSubtype, title);
        initComponetsCustom();
    }

    /*
    * Private methods
    */

    private void initComponetsCustom() {
        msSqlDiogPeriod = "";

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);

        if (mnGridSubtype == SModConsts.SX_IOG_SUP_ADJ_PEN) {
            moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
            moFilterDateCutOff.initFilter((Date) null);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
        }
        else {
            moFilterDate = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE);
            moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        }

        moButtonReturnIog = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mfg_fg_asd.gif")), "Devolver partida", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonReturnIog);

        moButtonCloseIog = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_doc_close.gif")), "Cerrar para devolución", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonCloseIog);

        moButtonOpenIog = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_doc_open.gif")), "Abrir para devolución", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonOpenIog);

        switch (mnGridSubtype) {
            case SModConsts.SX_IOG_SUP_ADJ_ASSO:
                moButtonReturnIog.setEnabled(false);
                moButtonCloseIog.setEnabled(false);
                moButtonOpenIog.setEnabled(true);
                break;

            case SModConsts.SX_IOG_SUP_ADJ_PEN:
                moButtonReturnIog.setEnabled(true);
                moButtonCloseIog.setEnabled(true);
                moButtonOpenIog.setEnabled(false);
                break;

            default:
        }
    }

    private void actionSupplyIog() {
        int nPkItem = 0;

        SGuiParams params = null;
        SDbItem item = null;

        if (moButtonReturnIog.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                item = new SDbItem();
                SGridRowView row = (SGridRowView) getSelectedGridRow();
                nPkItem = item.validateExternalItem(miClient.getSession(), row.getRowPrimaryKey()[3]);

                if (row.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (row.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + row.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (nPkItem <= 0) {
                    miClient.showMsgBoxWarning("El ítem externo no existe en la base de datos.");
                }
                else {
                    params = new SGuiParams();

                    if (mnGridType == SModConsts.SX_IOG_ADJ_PUR) {
                        params.getParamsMap().put(SModConsts.SS_IOG_TP, SModSysConsts.SS_IOG_TP_OUT_PUR_PUR);
                    }
                    else {
                        params.getParamsMap().put(SModConsts.SS_IOG_TP, SModSysConsts.SS_IOG_TP_IN_SAL_SAL);
                    }

                    params.getParamsMap().put(SModConsts.SX_EXT_DPS, row.getRowPrimaryKey());
                    miClient.getSession().getModule(SModConsts.MOD_SOM_OS, SLibConsts.UNDEFINED).showForm(SModConsts.S_IOG, SLibConsts.UNDEFINED, params);
                    miClient.getSession().notifySuscriptors(mnGridType);
                    miClient.getSession().notifySuscriptors(SModConsts.S_IOG);
                    miClient.getSession().notifySuscriptors(SModConsts.S_IOG_EXP);
                }
            }
        }
    }

    private void actionCloseIog() {
        SDbDpsAssorted oDbDpsAssorted = new SDbDpsAssorted();

        if (moButtonCloseIog.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else if (miClient.showMsgBoxConfirm("Se cerrará para devolución la partida del documento. \n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                SGridRowView row = (SGridRowView) getSelectedGridRow();

                if (row.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (row.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + row.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else {
                    try {
                        oDbDpsAssorted.closeOpenIog(miClient.getSession(), row.getRowPrimaryKey(), false);
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionOpenIog() {
        SDbDpsAssorted oDbDpsAssorted = new SDbDpsAssorted();

        if (moButtonOpenIog.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else if (miClient.showMsgBoxConfirm("Se abrirá para devolución la partida del documento. \n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                SGridRowView row = (SGridRowView) getSelectedGridRow();

                if (row.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (row.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + row.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else {
                    try {
                        oDbDpsAssorted.closeOpenIog(miClient.getSession(), row.getRowPrimaryKey(), true);
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    /*
    * Public methods
    */

    @Override
    public void prepareSqlQuery() {
        String sql = "AND ";
        String sqlBizPartner = "";
        String sqlOrderByDocEty = "";
        String sDatabaseName = miClient.getSession().getDatabase().getDbName();
        String sDatabaseErpName = ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getExtDatabase().getDbName();
        String sDatabaseCoName = ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getExtDatabaseCo().getDbName();
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(8);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += "d.b_del = 0 ";
        }

        if (mnGridSubtype == SModConsts.SX_IOG_SUP_ADJ_PEN) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
            if (filter != null) {
                sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);
                msSqlDiogPeriod += "" + SGridUtils.getSqlFilterDate("ge.dt", (SGuiDate) filter) + " AND ";
            }
        }
        else {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
            if (filter != null) {
                sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);
                msSqlDiogPeriod += "" + SGridUtils.getSqlFilterDate("ge.dt", (SGuiDate) filter) + " AND ";
            }
        }

        if (mnGridType == SModConsts.SX_IOG_ADJ_PUR) {
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

        msSql = "SELECT " +
                "v.id_year AS " + SDbConsts.FIELD_ID + "1, " +
                "v.id_doc AS " + SDbConsts.FIELD_ID + "2, "  +
                "v.id_ety AS " + SDbConsts.FIELD_ID + "3, " +
                "v.fid_item AS " + SDbConsts.FIELD_ID + "4, " +
                "v.fid_unit AS " + SDbConsts.FIELD_ID + "5, " +
                "COALESCE((SELECT id_dps_year " +
                "FROM " + sDatabaseCoName + ".TRN_DPS_DPS_ADJ AS adj " +
                "WHERE adj.id_adj_year = v.id_year AND adj.id_adj_doc = v.id_doc AND adj.id_adj_ety = v.id_ety) , 0) AS " + SDbConsts.FIELD_ID + "6, " +
                "COALESCE((SELECT id_dps_doc " +
                "FROM " + sDatabaseCoName + ".TRN_DPS_DPS_ADJ AS adj " +
                "WHERE adj.id_adj_year = v.id_year AND adj.id_adj_doc = v.id_doc AND adj.id_adj_ety = v.id_ety) , 0) AS " + SDbConsts.FIELD_ID + "7, " +
                "COALESCE((SELECT id_dps_ety " +
                "FROM " + sDatabaseCoName + ".TRN_DPS_DPS_ADJ AS adj " +
                "WHERE adj.id_adj_year = v.id_year AND adj.id_adj_doc = v.id_doc AND adj.id_adj_ety = v.id_ety) , 0) AS " + SDbConsts.FIELD_ID + "8, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "v.fid_item, v.fid_unit, v.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "v.qty AS f_qty, v.orig_qty AS f_orig_qty, " +
                "COALESCE((SELECT SUM(ge.qty) FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS ge WHERE " +
                "ge.fk_ext_adj_year_n = v.id_year AND ge.fk_ext_adj_doc_n = v.id_doc AND ge.fk_ext_adj_ety_n = v.id_ety AND " +
                msSqlDiogPeriod + " ge.b_del = 0), 0) AS f_ret_qty, " +
                "COALESCE(( " +
                "SELECT IF(d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0, 1, 0) " +
                "FROM " + sDatabaseName + ".s_dps_ass AS som WHERE d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0), 0) AS f_exist, " +
                "'' AS " + SDbConsts.FIELD_CODE + ", 'Partida del documento' AS " + SDbConsts.FIELD_NAME + " " +
                "FROM " + sDatabaseCoName + ".trn_dps AS d " +
                "INNER JOIN " + sDatabaseErpName + ".trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                "d.b_close = 0 AND d.b_del = 0 AND d.fid_st_dps = " + SModSysConsts.EXT_TRNS_ST_DPS_EMITED + " AND " +
                "d.fid_ct_dps = " + (mnGridType == SModConsts.SX_IOG_ADJ_SAL ? SModSysConsts.EXT_TRNS_CL_IOM_OUT_INT[0] : SModSysConsts.EXT_TRNS_CL_IOM_IN_INT[0]) +
                " AND d.fid_cl_dps = " + (mnGridType == SModConsts.SX_IOG_ADJ_SAL ? SModSysConsts.EXT_TRNS_CL_IOM_OUT_INT[1] : SModSysConsts.EXT_TRNS_CL_IOM_IN_INT[1]) + " " + sql + " " +
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
                "GROUP BY v.id_year, v.id_doc, v.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "v.fid_item, v.fid_unit, v.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, " +
                "v.qty, v.orig_qty ";

        if (mnGridSubtype == SModConsts.SX_IOG_SUP_ADJ_PEN) { // || mnTabType == SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY) {
            msSql += "HAVING (f_exist = 0 AND (f_orig_qty - f_ret_qty) <> 0) ";
        }
        else {
            msSql += "HAVING f_exist = 1 OR ((f_orig_qty - f_ret_qty) = 0) ";
        }

        msSql += "ORDER BY " + sqlOrderByDocEty + "; ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[19];

        if (mnGridType == SModConsts.SX_IOG_SUP_PUR) {
            // Purchases & suppliers:

            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bp_key", "Clave proveedor");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "bp", "Proveedor");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bpb", "Sucursal proveedor");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_dt_code", "Tipo docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_num", "Folio docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "num_ref", "Referencia docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "f_cb_code", "Sucursal empresa");
        }
        else {
            // Sales & customers:

            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_dt_code", "Tipo docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_num", "Folio docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "num_ref", "Referencia docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_cb_code", "Sucursal empresa");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bp_key", "Clave cliente");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "bp", "Cliente");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bpb", "Sucursal cliente");
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "tot_cur_r", "Total moneda $");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda docto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "item_key", "Clave ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "item", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_orig_qty", "Cant original");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_ret_qty", "Cant devuelta");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "", "Cant pendiente");
        columns[col].getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND));
        columns[col].getRpnArguments().add(new SLibRpnArgument("f_ret_qty", SLibRpnArgumentType.OPERAND));
        columns[col++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "f_orig_unit", "Unidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_close", "Cerrado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr", "Usr cierre");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_TIME, "ts_close", "Cierre");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_CO);
        moSuscriptionsSet.add(SModConsts.CU_COB);
        moSuscriptionsSet.add(SModConsts.CU_WAH);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
        moSuscriptionsSet.add(SModConsts.S_IOG);
        moSuscriptionsSet.add(SModConsts.S_STK);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == moButtonReturnIog) {
                actionSupplyIog();
            }
            else if (button == moButtonCloseIog) {
                actionCloseIog();
            }
            else if (button == moButtonOpenIog) {
                actionOpenIog();
            }
        }
    }
}
