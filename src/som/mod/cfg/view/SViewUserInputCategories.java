/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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

/**
 *
 * @author Isabel Servín
 */
public class SViewUserInputCategories extends SGridPaneView {

    public SViewUserInputCategories(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, som.mod.SModConsts.CU_USR_INP_CT, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(2);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        msSql = "SELECT "
                + "uic.id_usr AS " + SDbConsts.FIELD_ID + "1, "
                + "uic.id_inp_ct AS " + SDbConsts.FIELD_ID + "2, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "vt.id_usr_tp AS " + SDbConsts.FIELD_TYPE_ID + "1, "
                + "vt.name AS " + SDbConsts.FIELD_TYPE + ", "
                + "ic.id_inp_ct, "
                + "ic.name, "
                + "v.b_dis AS " + SDbConsts.FIELD_IS_DIS + ", "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + " " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS v " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CS_USR_TP) + " AS vt "
                + "ON v.fk_usr_tp = vt.id_usr_tp " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR_INP_CT) + " AS uic "
                + "ON v.id_usr = uic.id_usr " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_CT) + " AS ic "
                + "ON uic.id_inp_ct = ic.id_inp_ct " 
                + (sql.isEmpty() ? "" : "WHERE " + sql) 
                + "ORDER BY v.name, v.id_usr, ic.name, ic.id_inp_ct;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[6];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_TYPE, SGridConsts.COL_TITLE_TYPE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "ic.name", "Categoría de insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DIS, SGridConsts.COL_TITLE_IS_DIS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS);

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.SU_INP_CT);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
    
}
