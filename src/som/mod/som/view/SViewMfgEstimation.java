/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.util.Arrays;
import javax.swing.ImageIcon;
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
 * @author Néstor Ávalos
 */
public class SViewMfgEstimation extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    
    public SViewMfgEstimation(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_MFG_EST, SLibConsts.UNDEFINED, title);
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
        jbRowDelete.setEnabled(true);
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
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_mfg_est", (SGuiDate) filter);

        msSql = "SELECT "
            + "v.id_mfg_est AS " + SDbConsts.FIELD_ID + "1, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "'' AS " + SDbConsts.FIELD_NAME + ", "
            + "v.ver, "
            + "v.dt_mfg_est, "
            + "v.dt_stk_day, "
            + "v.mfg_fg_r, "
            + "v.b_clo, "
            + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
            + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
            + "v.fk_unit, "
            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.fk_usr_clo, "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "v.ts_usr_clo, "
            + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "    
            + "uc.name AS f_usr_clo "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST) + " AS v "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uc ON v.fk_usr_clo = uc.id_usr "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "ORDER BY v.dt_mfg_est, v.id_mfg_est ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[12];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_mfg_est", "Fecha estimación");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_stk_day", "Fecha toma física");
        //columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.ver", SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.mfg_fg_r", "Cantidad");                
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_clo", "Cerrado");        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "f_usr_clo", SGridConsts.COL_TITLE_USER_USR_NAME + " cerrado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_usr_clo", SGridConsts.COL_TITLE_USER_USR_TS + " cerrado");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
