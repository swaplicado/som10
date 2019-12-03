/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.cfg.view;

import java.util.Arrays;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewBranchWarehouse extends SGridPaneView {

    public SViewBranchWarehouse(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.CU_WAH, SLibConsts.UNDEFINED, title);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setUpdatableApplying(true);
        moPaneSettings.setDisableableApplying(true);
        moPaneSettings.setDeletableApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "v.id_co AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_cob AS " + SDbConsts.FIELD_ID + "2, "
                + "v.id_wah AS " + SDbConsts.FIELD_ID + "3, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dim_base, "
                + "v.dim_heig, "
                + "v.cap_real_lt, "
                + "v.acidity, "
                + "co.name, "
                + "cob.name, "
                + "vt.id_wah_tp, "
                + "vt.name, "
                + "pl.name AS pl_name, "
                + "@cap := PI() * POW(dim_base / 2.0, 2.0) * dim_heig * 1000.0 AS _cap_theo, "
                + "@cap - cap_real_lt AS _cap_diff, "
                + "v.b_can_upd AS " + SDbConsts.FIELD_CAN_UPD + ", "
                + "v.b_can_dis AS " + SDbConsts.FIELD_CAN_DIS + ", "
                + "v.b_can_del AS " + SDbConsts.FIELD_CAN_DEL + ", "
                + "v.b_dis AS " + SDbConsts.FIELD_IS_DIS + ", "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_CO) + " AS co ON "
                + "v.id_co = co.id_co "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_PROD_LINES) + " AS pl ON "
                + "v.fk_line = pl.id_line "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS cob ON "
                + "v.id_co = cob.id_co AND v.id_cob = cob.id_cob "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CS_WAH_TP) + " AS vt ON "
                + "v.fk_wah_tp = vt.id_wah_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.name, v.id_co, v.id_cob, v.id_wah ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[19];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "co.name", "Empresa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cob.name", "Sucursal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vt.name", SGridConsts.COL_TITLE_TYPE + " almacén");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pl_name", "Línea de producción");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "v.dim_base", "Diámetro (" + SSomConsts.M + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "v.dim_heig", "Altura (" + SSomConsts.M + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "v.cap_real_lt", "Capacidad real (" + SSomConsts.L + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_cap_theo", "Capacidad teórica (" + SSomConsts.L + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_cap_diff", "Diferencia capacidad (" + SSomConsts.L + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "v.acidity", "Acidez (%)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DIS, SGridConsts.COL_TITLE_IS_DIS);
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
        moSuscriptionsSet.add(SModConsts.CU_CO);
        moSuscriptionsSet.add(SModConsts.CU_COB);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
