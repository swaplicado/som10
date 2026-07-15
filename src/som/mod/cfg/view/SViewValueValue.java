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
import sa.lib.gui.SGuiParams;
import som.mod.SModConsts;
import som.mod.cfg.db.SDbField;

/**
 * Values of a specific field vs. values.
 * @author Sergio Flores
 */
public class SViewValueValue extends SGridPaneView {
    
    public static final int PARENT_A = 1;
    public static final int PARENT_B = 2;
    
    private final SDbField moFieldParent;
    private final SDbField moFieldChild;
    
    /**
     * Creates new values vs. values view.
     * @param client GUI client.
     * @param title View title.
     * @param parent Indicates which is the "parent" column: A or B.
     * @param params Contains "parent" field as type and "child" field as subtype.
     */
    public SViewValueValue(SGuiClient client, String title, int parent, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.C_VALUE_VALUE, parent, title, params);
        setRowButtonsEnabled(false, false, false, false, true);
        moFieldParent = (SDbField) miClient.getSession().readRegistry(SModConsts.C_FIELD, new int[] { params.getType() });
        moFieldChild = (SDbField) miClient.getSession().readRegistry(SModConsts.C_FIELD, new int[] { params.getSubtype() });
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        String alias1 = "";
        String alias2 = "";
        
        switch (mnGridSubtype) {
            case PARENT_A:
                alias1 = "a";
                alias2 = "b";
                break;
            case PARENT_B:
                alias1 = "b";
                alias2 = "a";
                break;
            default:
                // nothing
        }
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del AND NOT v" + alias1 + ".b_del AND NOT v" + alias2 + ".b_del ";
        }
        
        msSql = "SELECT "
                + "v.id_value_a AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_value_b AS " + SDbConsts.FIELD_ID + "2, "
                + "v" + alias1 + ".value_text AS " + SDbConsts.FIELD_CODE + ", " // code of "parent" field
                + "v" + alias1 + ".value_text AS " + SDbConsts.FIELD_NAME + ", " // name of "parent" field
                + "v" + alias2 + ".value_text AS _name_child, "
                + "v" + alias1 + ".b_del AS _is_del_parent, "
                + "v" + alias2 + ".b_del AS _is_del_child, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.C_VALUE_VALUE) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.C_VALUE) + " AS va ON "
                + "va.id_value = v.id_value_a "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.C_VALUE) + " AS vb ON "
                + "vb.id_value = v.id_value_b "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "WHERE v" + alias1 + ".fk_field = " + mnGridMode + " AND v" + alias2 + ".fk_field = " + mnGridSubmode + " " + (sql.isEmpty() ? "" : "AND " + sql)
                + "ORDER BY v" + alias1 + ".value_text, v.id_value_" + alias1 + ", v" + alias2 + ".value_text, v.id_value_" + alias2 + ";";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[9];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SLibUtils.textFirstUpperCase(moFieldParent.getName()));
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "_is_del_parent", SGridConsts.COL_TITLE_IS_DEL + " " + moFieldParent.getName().toLowerCase());
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_name_child", SLibUtils.textFirstUpperCase(moFieldChild.getName()));
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "_is_del_child", SGridConsts.COL_TITLE_IS_DEL + " " + moFieldChild.getName().toLowerCase());
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
        moSuscriptionsSet.add(SModConsts.C_VALUE);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
