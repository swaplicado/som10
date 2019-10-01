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
public class SViewMfgEstimationProductionLine extends SGridPaneView {
    
    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewMfgEstimationProductionLine(SGuiClient client, String title) {
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
//        moPaneSettings.setSystemApplying(true);
//        moPaneSettings.setUserInsertApplying(true);
//        moPaneSettings.setUserUpdateApplying(true);

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
            + "pl.code, "
            + "pl.name, "
            + "SUM(mee.mfg_fg) AS tot_aceite, "
            + "SUM(mee.mfg_bp) AS tot_repro, "
            + "SUM(mee.mfg_cu) AS tot_deshecho, "
            + "SUM(mee.con_rm) AS tot_mp, "
            + "me.dt_mfg_est, "
            + "me.dt_stk_day "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST) + " AS me "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST_ETY) + " AS mee ON me.id_mfg_est = mee.id_mfg_est "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON "
            + "mee.fk_wah_co = wah.id_co AND mee.fk_wah_cob = wah.id_cob AND mee.fk_wah_wah = wah.id_wah "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_PROD_LINES) + " AS pl ON "
            + "wah.fk_line = pl.id_line "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "GROUP BY pl.id_line "
            + "ORDER BY me.dt_mfg_est, me.id_mfg_est, mee.id_ety ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[8];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "me.dt_mfg_est", "Fecha estimación");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "me.dt_stk_day", "Fecha toma física");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "pl.code", "Cod.");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pl.name", "Línea prod.");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "tot_aceite", "Producto terminado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "tot_repro", "Sub producto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "tot_deshecho", "Desecho");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "tot_mp", "Materia prima");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
