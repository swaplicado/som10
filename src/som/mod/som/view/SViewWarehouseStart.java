/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * Vista de las fechas de inicio de los almacenes para el cálculo del ponderado de porcentaje de aceite.
 * @author Isabel Servín
 */
public class SViewWarehouseStart extends SGridPaneView implements ActionListener {
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    
    /**
     * @param client
     * @param title
     */
    public SViewWarehouseStart(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_WAH_START, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(true, true, true, false, true);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }
    
    @Override
    public void prepareSqlQuery() {
        Object filter;
        String sql = "";

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "ws.b_del = 0 ";
        }
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("ws.dt_start", (SGuiDate) filter);
        }
        
        msSql = "SELECT " 
                + "ws.id_start AS " + SDbConsts.FIELD_ID + "1, "
                + "ws.dt_start, " 
                + "w.code AS " + SDbConsts.FIELD_CODE + ", " 
                + "w.name AS " + SDbConsts.FIELD_NAME + ", " 
                + "ws.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "  
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", " 
                + "ws.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ws.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_WAH_START) + " AS ws " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w ON " 
                + "ws.fk_wah_co = w.id_co AND ws.fk_wah_cob = w.id_cob AND ws.fk_wah_wah = w.id_wah " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON " 
                + "ws.fk_usr_ins = ui.id_usr " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON " 
                + "ws.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY w.code, w.name, ws.dt_start";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[8];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Almacén");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Almacén código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "ws.dt_start", "Inicio almacén");
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
        moSuscriptionsSet.add(SModConsts.CU_WAH);
        moSuscriptionsSet.add(SModConsts.S_WAH_START);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionMouseClicked() {
        super.actionMouseClicked();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
