/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Isabel Servín
 */
public class SViewItemAlternative extends SGridPaneView {
    
    private String msItemCodes;

    public SViewItemAlternative(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_ITEM_ALT, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, true, false, false, false);
        
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        try {
            msItemCodes = SSomUtils.getAlternativeItemCodes(miClient.getSession());            
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(true);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        msSql = "SELECT "
                + "v.id_item AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.b_alt_yield_per, "
                + "v.b_alt_moi_per, "
                + "v.b_alt_aci_per, "
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
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "AND v.id_item IN (" + msItemCodes + ") "
                + "ORDER BY v.name, v.id_item ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns;

        columns = new SGridColumnView[12];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_alt_yield_per", "Aplica rendimiento(%)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_alt_moi_per", "Aplica humedad(%)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_alt_aci_per", "Aplica acidez(%)");
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
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
    
}
