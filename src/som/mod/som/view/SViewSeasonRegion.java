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
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas
 */
public class SViewSeasonRegion extends SGridPaneView {
    
    public SViewSeasonRegion(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SU_SEAS_REG, SLibConsts.UNDEFINED, title);
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
                + "v.id_seas AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_reg AS " + SDbConsts.FIELD_ID + "2, "
                + "v.id_item AS " + SDbConsts.FIELD_ID + "3, "
                + "vs.name AS " + SDbConsts.FIELD_NAME + ", "
                + " '' " + SDbConsts.FIELD_CODE + ", "
                + "vi.name, "
                + "vi.code, "
                + "vr.name, "
                + "v.max_den, "
                + "v.max_iod_val, "
                + "v.max_ref_ind, "
                + "v.max_imp_per, "
                + "v.max_moi_per, "
                + "v.prc_ton, "
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
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_REG) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS vs ON "
                + "v.id_seas = vs.id_seas "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS vi ON "
                + "v.id_item = vi.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS vr ON "
                + "v.id_reg = vr.id_reg "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY vs.name, vr.name, vi.name, v.id_seas, v.id_reg, v.id_item ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = null;
        
        columns = new SGridColumnView[17];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_NAME, "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vr.name", "Región");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "vi.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "vi.code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "v.max_imp_per", "Impurezas máx");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "v.max_moi_per", "Humedad máx");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.max_den", "Densidad prom");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.max_ref_ind", "Índice refracción prom (IR)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.max_iod_val", "Valor yodo prom (VI)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.prc_ton", "Precio ton $");
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
        moSuscriptionsSet.add(SModConsts.SU_SEAS);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_REG);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
