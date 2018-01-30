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
 * @author Néstor Ávalos
 */
public class SViewStockDays extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewStockDays(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_STK_DAYS, SLibConsts.UNDEFINED, title);
        initComponetsCustom();
    }

    /*
    * Private methods
    */

    private void initComponetsCustom() {

        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(true);
        jtbFilterDeleted.setEnabled(false);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    /*
    * Public methods
    */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);

        msSql = "SELECT " +
            "YEAR(v.dt) AS " + SDbConsts.FIELD_ID + "1, " +
            "MONTH(v.dt) AS " + SDbConsts.FIELD_ID + "2, " +
            "DAY(v.dt) AS " + SDbConsts.FIELD_ID + "3, " +
            "v.dt AS " + SDbConsts.FIELD_DATE + ", " +
            "'' AS " + SDbConsts.FIELD_CODE + ", " +
            "'Inventario diario ' AS " + SDbConsts.FIELD_NAME + ", " +
            "co.code AS f_co_code, " +
            "co.name AS f_co_name, " +
            "cob.code AS f_cob_code, " +
            "cob.name AS f_cob_name, v.id_co, v.id_cob " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS v " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_CO) +  " AS co ON " +
            "v.id_co = co.id_co " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) +  " AS cob ON " +
            "v.id_cob = cob.id_co AND v.id_cob = cob.id_cob " +
            (sql.isEmpty() ? "" : "WHERE " + sql) +
            "GROUP BY v.id_co, v.id_cob, v.dt " +
            "ORDER BY co.code, co.name, cob.code, cob.name, v.dt ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[6];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_co_name", "Empresa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "f_co_code", "Empresa código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_cob_name", "Sucursal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "f_cob_code", "Sucursal código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE);

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_CO);
        moSuscriptionsSet.add(SModConsts.CU_COB);
        moSuscriptionsSet.add(SModConsts.S_STK_DAY);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
