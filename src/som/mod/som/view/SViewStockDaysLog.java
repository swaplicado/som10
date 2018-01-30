/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.util.Arrays;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import som.mod.SModConsts;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SViewStockDaysLog extends SGridPaneView {

    private SGridFilterDateRange moFilterDateRange;

    public SViewStockDaysLog(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_STK_DAYS_LOG, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(SLibTimeUtils.getWholeMonth(miClient.getSession().getWorkingDate()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(7);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        /*
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        */

        sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        filter = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE);
        sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDateRange("v.dt", (Date[]) filter);

        msSql = "SELECT v.id_year AS " + SDbConsts.FIELD_ID + "1, "
            + "v.id_item AS " + SDbConsts.FIELD_ID + "2, "
            + "v.id_unit AS " + SDbConsts.FIELD_ID + "3, "
            + "v.id_co AS " + SDbConsts.FIELD_ID + "4, "
            + "v.id_cob AS " + SDbConsts.FIELD_ID + "5, "
            + "v.id_wah AS " + SDbConsts.FIELD_ID + "6, "
            + "v.id_day AS " + SDbConsts.FIELD_ID + "7, "
            + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
            + "v.emp, "
            + "v.cull, "
            + "v.stk_day, "
            + "v.stk_sys, "
            + "v.b_stk_dif_skp, "
            /*
            + "v.mfg_fg, "
            + "v.mfg_bp, "
            + "v.mfg_cu, "
            + "v.con_rm, "
            */
            + "v.b_emp, "
            + "i.name, "
            + "i.code, "
            + "i.den, "
            /*
            + "ibp.name, "
            + "ibp.code, "
            + "icu.name, "
            + "icu.code, "
            + "irm.name, "
            + "irm.code, "
            */
            + "u.code, "
            + "wah.name, "
            + "wah.code, "
            + "wah.dim_heig, "
            + "wah.cap_real_lt, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "'' AS " + SDbConsts.FIELD_NAME + ", "
            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS v "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) +" AS i ON "
            + "v.id_item = i.id_item "
            /*
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) +" AS irm ON "
            + "v.fk_item_con_rm = irm.id_item "
            */
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) +" AS u ON "
            + "v.id_unit = u.id_unit "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) +" AS wah ON "
            + "v.id_co = wah.id_co AND v.id_cob = wah.id_cob AND v.id_wah = wah.id_wah "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
            + "v.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
            + "v.fk_usr_upd = uu.id_usr "
            /*
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) +" AS ibp ON "
            + "v.fk_item_mfg_bp_n = ibp.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) +" AS icu ON "
            + "v.fk_item_mfg_cu_n = icu.id_item "
            */
            + (sql.isEmpty() ? "" : "WHERE " + sql) + " "
            + "ORDER BY v.dt, wah.code, v.emp, v.b_del DESC, i.name, i.code, v.id_item ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[18];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "wah.code", "Tanque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_emp", "Vacío");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.emp", "Vacío (" + SSomConsts.M + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cull", "Residuos (" + SSomConsts.M + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "i.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.code", "Ítem código");
        /*
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "ibp.name", "Subproducto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "ibp.code", "Subproducto código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "icu.name", "Desecho");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "icu.code", "Desecho código");
        */
        /*
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "irm.name", "Materia prima");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "irm.code", "Materia prima código");
        */
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.stk_day", "Inventario físico");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.stk_sys", "Existencias sistema");
        /*
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.mfg_fg", "Prod est PT");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.mfg_bp", "Prod est SP");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.mfg_cu", "Prod est DE");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.con_rm", "Consumo est MP");
        */
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.code", "Unidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_stk_dif_skp", "Máx dif inv físico");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "wah.dim_heig", "Altura (" + SSomConsts.M + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "wah.cap_real_lt", "Volumen (" + SSomConsts.L + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "i.den", "Densidad (" + SSomConsts.DEN + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_STK_DAY);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
