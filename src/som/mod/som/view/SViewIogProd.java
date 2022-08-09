/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SViewIogProd extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SPaneFilter moPaneFilter;

    public SViewIogProd(SGuiClient client, String title, int subtype) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_IOG_PROD, subtype, title);
        setRowButtonsEnabled(false, true, false, false, true);
        initComponetsCustom();
    }

    /*
    * Private methods
    */

    private void initComponetsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        moPaneFilter = new SPaneFilter(this, SModConsts.SS_IOG_TP);
        moPaneFilter.initFilter(null);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilter);
    }

    /*
    * Public methods
    */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        switch(mnGridSubtype) {
            case SModConsts.SX_INV_IN_FG:
                sql += "v.fk_iog_ct IN(" + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0] + ", " + SModSysConsts.SS_IOG_TP_OUT_MFG_FG_ASD[0] + ") AND " +
                       "v.fk_iog_cl IN(" + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1] + ", " + SModSysConsts.SS_IOG_TP_OUT_MFG_FG_ASD[1] + ") AND " +
                       "v.fk_iog_tp IN(" + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2] + ", " + SModSysConsts.SS_IOG_TP_OUT_MFG_FG_ASD[2] + ") ";
                break;
                
            default:
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);

        filter = (int[]) moFiltersMap.get(SModConsts.SS_IOG_TP);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_iog_ct", "v.fk_iog_cl", "v.fk_iog_tp", }, (int[]) filter);
        }

        msSql = "SELECT v.id_iog AS " + SDbConsts.FIELD_ID + "1, " +
            "v.num AS number, " +
            "v.dt AS " + SDbConsts.FIELD_DATE + ", " +
            "v.ref, " +
            "v.qty, " +
            "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " +
            "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", " +
            "v.fk_iog_ct, " +
            "v.fk_iog_cl, " +
            "v.fk_iog_tp, " +
            "v.fk_iog_adj_tp, " +
            "v.fk_item, " +
            "v.fk_unit, " +
            "v.fk_wah_co, " +
            "v.fk_wah_cob, " +
            "v.fk_wah_wah, " +
            "v.fk_div, " +
            "v.fk_ext_dps_year_n, " +
            "v.fk_ext_dps_doc_n, " +
            "v.fk_ext_dps_ety_n, " +
            "v.fk_ext_iog_year_n, " +
            "v.fk_ext_iog_doc_n, " +
            "v.fk_ext_iog_ety_n, " +
            "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " +
            "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " +
            "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " +
            "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " +
            "ct.code AS ct_code, " +
            "ct.name AS ct_name, " +
            "cl.code AS cl_code, " +
            "cl.name AS cl_name, " +
            "tp.code AS tp_code, " +
            "tp.name AS tp_name, " +
            "adj.code AS adj_code, " +
            "adj.name AS adj_name, " +
            "byp.code AS by_prod_code, " +
            "co.code AS co_code, " +
            "co.name AS co_name, " +
            "cob.code AS cob_code, " +
            "cob.name AS cob_name, " +
            "IF (v.fk_iog_ct = 1, wah.code, (SELECT code "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) +
                    " WHERE id_wah = (SELECT fk_wah_wah FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + 
                    " WHERE fk_iog_n = v.id_iog))) AS wah_destiny, " +
            "IF (v.fk_iog_ct = 2, wah.code, wah2.code) AS wah_source, " +
            "wah.name AS wah_name, " +
            "wah2.code AS wah_code2, " +
            "wah2.name AS wah_name2, " +
            "d.code AS div_code, " +
            "d.name AS div_name, " +
            "i.code AS i_code, " +
            "i.name AS i_name, " +
            "u.code AS u_code, " +
            "u.name AS u_name, " +
            "CONCAT(tp.code, '-', v.num) AS f_num, " +
            "CONCAT(CONCAT(ir.num_ser, (IF(ir.num_ser <> '', '-', '')), ir.num), '; ', ir.dt, '; ', ir.name) AS f_ref, " +
            "note.note, " +
            "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", " +
            "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", " +
            "tp.code AS " + SDbConsts.FIELD_CODE + ", " +
            "CONCAT(tp.code, '-', v.num) AS " + SDbConsts.FIELD_NAME + " " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS v " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CT) + " AS ct ON " +
            "v.fk_iog_ct = ct.id_iog_ct " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CL) + " AS cl ON " +
            "v.fk_iog_ct = cl.id_iog_ct AND v.fk_iog_cl = cl.id_iog_cl " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_TP) + " AS tp ON " +
            "v.fk_iog_ct = tp.id_iog_ct AND v.fk_iog_cl = tp.id_iog_cl AND v.fk_iog_tp = tp.id_iog_tp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_IOG_ADJ_TP) + " AS adj ON " +
            "v.fk_iog_adj_tp = adj.id_iog_adj_tp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_BY_PRODUCT) + " AS byp ON " + 
            "v.fk_by_product = byp.id_by_product " + 
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_CO) + " AS co ON " +
            "v.fk_wah_co = co.id_co " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS cob ON " +
            "v.fk_wah_co = cob.id_co AND v.fk_wah_cob = cob.id_cob " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " +
            "v.fk_wah_co = wah.id_co AND v.fk_wah_cob = wah.id_cob AND v.fk_wah_wah = wah.id_wah " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
            "v.fk_div = d.id_div " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
            "v.fk_item = i.id_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
            "v.fk_unit = u.id_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON " +
            "v.fk_usr_ins = ui.id_usr " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON " +
            "v.fk_usr_upd = uu.id_usr " +
            "LEFT OUTER JOIN "+ SModConsts.TablesMap.get(SModConsts.S_IOG_NOTE) + " AS note ON " +
            "v.id_iog = note.id_iog AND note.id_note = 1 " +
            "LEFT OUTER JOIN "+ SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS v2 ON " +
            "v.fk_iog_n = v2.id_iog " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah2 ON " +
            "v2.fk_wah_co = wah2.id_co AND v2.fk_wah_cob = wah2.id_cob AND v2.fk_wah_wah = wah2.id_wah " +
            "LEFT OUTER JOIN "+ SModConsts.TablesMap.get(SModConsts.S_IOG_REF) + " AS ir ON " +
            "v.id_iog = ir.id_iog " +
            (sql.isEmpty() ? "" : "WHERE " + sql) +
            "ORDER BY v.num, co.name, co.code, cob.name, cob.code, wah.name, wah.code, d.code ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[22];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "cob_code", "Sucursal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "wah_source", "Origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "wah_destiny", "Destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "div_code", "División");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tp_name", "Tipo movimiento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "adj_code", "Tipo ajuste");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "by_prod_code", "Proceso");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "i_name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i_code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.qty", "Cantidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u_code", "Unidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.ref", "Referencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_ref", "Referencia E/S");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "note.note", "Observaciones");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_STK);
        moSuscriptionsSet.add(SModConsts.S_STK_DAY);
        moSuscriptionsSet.add(SModConsts.S_IOG_EXP);
        moSuscriptionsSet.add(SModConsts.S_IOG_EXP_HIS);
        moSuscriptionsSet.add(SModConsts.S_MIX);
        moSuscriptionsSet.add(SModConsts.CU_CO);
        moSuscriptionsSet.add(SModConsts.CU_COB);
        moSuscriptionsSet.add(SModConsts.CU_WAH);
        moSuscriptionsSet.add(SModConsts.SU_IOG_ADJ_TP);
        moSuscriptionsSet.add(SModConsts.SU_BY_PRODUCT);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
        moSuscriptionsSet.add(SModConsts.CU_USR);
        moSuscriptionsSet.add(SModConsts.SX_IOG_SUP_PUR);
        moSuscriptionsSet.add(SModConsts.SX_IOG_SUP_SAL);
        moSuscriptionsSet.add(SModConsts.SX_TIC_MAN_SUP);
        moSuscriptionsSet.add(SModConsts.SX_TIC_SUP_RM);
        moSuscriptionsSet.add(SModConsts.SX_TIC_DPS);
        moSuscriptionsSet.add(SModConsts.SX_TIC_DPS_ASSO);
        moSuscriptionsSet.add(SModConsts.SX_TIC_DPS_SUP);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
