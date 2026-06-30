/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.cfg.view;

import java.util.Arrays;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;
import som.mod.cfg.db.SDbField;

/**
 * Values of a specific field.
 * @author Sergio Flores
 */
public class SViewValue extends SGridPaneView {
    
    private final SDbField moField;
    
    /**
     * Creates new values view.
     * @param client GUI client.
     * @param title View title.
     * @param field Required field, either SModSysConsts.C_FIELD_TIC_PLA or SModSysConsts.C_FIELD_TIC_DRV.
     */
    public SViewValue(SGuiClient client, String title, int field) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.C_VALUE, field, title);
        moField = (SDbField) miClient.getSession().readRegistry(SModConsts.C_FIELD, new int[] { mnGridSubtype });
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        }

        msSql = "SELECT "
                + "v.id_value AS " + SDbConsts.FIELD_ID + "1, "
                + "v.value_text AS " + SDbConsts.FIELD_CODE + ", "
                + "v.value_text AS " + SDbConsts.FIELD_NAME + ", "
                + "v.value_ref, "
                + "(SELECT GROUP_CONCAT(ic.name ORDER BY ic.name, ic.id_inp_ct SEPARATOR ' + ')"
                + " FROM " + SModConsts.TablesMap.get(SModConsts.SU_INP_CT) + " AS ic "
                + " WHERE FIND_IN_SET(ic.id_inp_ct, REPLACE(v.scope_inp_ct, ' ', ''))) AS _scope_inp_ct, "
                + "(SELECT GROUP_CONCAT(i.name ORDER BY i.name, i.id_item SEPARATOR ' + ')"
                + " FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i "
                + " WHERE FIND_IN_SET(i.id_item, REPLACE(v.scope_item, ' ', ''))) AS _scope_item, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.C_VALUE) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "WHERE v.fk_field = " + mnGridSubtype + " " + (sql.isEmpty() ? "" : "AND " + sql)
                + "ORDER BY v.value_text, v.id_value;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[9];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SLibUtils.textFirstUpperCase(moField.getName()));
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.value_ref", "Referencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_scope_inp_ct", "Aplicación categorías insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "_scope_item", "Aplicación ítems");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.C_FIELD);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
