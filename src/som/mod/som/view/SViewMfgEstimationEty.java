/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.util.Arrays;
import sa.lib.SLibConsts;
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

/**
 *
 * @author Edwin Carmona
 */
public class SViewMfgEstimationEty extends SGridPaneView {
    
    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewMfgEstimationEty(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_MFG_EST_ETY, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;
        
        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowDisable.setEnabled(false);
        
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "me.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("me.dt_mfg_est", (SGuiDate) filter);

        msSql = "SELECT "
            + "mee.id_mfg_est AS " + SDbConsts.FIELD_ID + "1, "
            + "mee.id_ety AS " + SDbConsts.FIELD_ID + "2, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "'' AS " + SDbConsts.FIELD_NAME + ", "
            + "wah.code, "
            + "wah.name, "
            + "pl.name, "
            + "me.dt_mfg_est, "
            + "me.dt_stk_day, "
            + "me.mfg_fg_r, "
            + "me.mfg_bp_r, "
            + "me.mfg_cu_r, "
            + "mee.mfg_fg,"
            + "mee.mfg_bp,"
            + "mee.mfg_cu,"
            + "si.name,"
            + "pti.name,"
            + "sbi.name,"
            + "cui.name,"
            + "mee.con_rm,"
            + "me.b_clo, "
            + "me.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
            + "me.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
            + "me.fk_unit, "
            + "me.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "me.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "me.fk_usr_clo, "
            + "me.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "me.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "me.ts_usr_clo, "
            + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "    
            + "uc.name AS f_usr_clo "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST) + " AS me "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST_ETY) + " AS mee ON me.id_mfg_est = mee.id_mfg_est "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON "
            + "mee.fk_wah_co = wah.id_co AND mee.fk_wah_cob = wah.id_cob AND mee.fk_wah_wah = wah.id_wah "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_PROD_LINES) + " AS pl ON "
            + "wah.fk_line = pl.id_line "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS si ON mee.fk_item_con_rm = si.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS pti ON mee.fk_item_mfg_fg = pti.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS sbi ON mee.fk_item_mfg_fg = sbi.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS cui ON mee.fk_item_mfg_fg = cui.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON me.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON me.fk_usr_upd = uu.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uc ON me.fk_usr_clo = uc.id_usr "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "ORDER BY me.dt_mfg_est, me.id_mfg_est, mee.id_ety ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[25];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "me.dt_mfg_est", "Fecha estimación");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "me.dt_stk_day", "Fecha toma física");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "wah.code", "Cod. Alm.");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "wah.name", "Almacén");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pl.name", "Línea prod.");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "pti.name", "Ítem Prod. terminado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "mee.mfg_fg", "Producto terminado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "sbi.name", "Ítem sub producto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "mee.mfg_bp", "Sub producto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "cui.name", "Ítem desecho");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "mee.mfg_cu", "Desecho");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "si.name", "Ítem materia prima");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "mee.con_rm", "Materia prima");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "me.mfg_fg_r", "Total PT");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "me.mfg_bp_r", "Total SP");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "me.mfg_cu_r", "Total PD");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "me.b_clo", "Cerrado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "f_usr_clo", SGridConsts.COL_TITLE_USER_USR_NAME + " cerrado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "me.ts_usr_clo", SGridConsts.COL_TITLE_USER_USR_TS + " cerrado");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
