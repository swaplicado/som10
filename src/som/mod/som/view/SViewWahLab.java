/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
 * @author Isabel Servín
 */
public class SViewWahLab extends SGridPaneView implements ActionListener {
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton mjNewWithLastTest;
    
    public SViewWahLab(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_WAH_LAB, SLibConsts.UNDEFINED, title);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        mjNewWithLastTest = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_lab.gif")), "Analisis nuevo con resultados de análisis anterior", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_LEFT).add(mjNewWithLastTest, 1);
    }
    
    private void actionNewWithLastTest() {
        miClient.getSession().getModule(SModConsts.MOD_SOM_OS).showForm(SModConsts.S_WAH_LAB, 1, null);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_start", (SGuiDate) filter);
        }
        
        msSql = "SELECT "
                + "v.id_wah_lab AS " + SDbConsts.FIELD_ID + "1, "
                + "v.year AS " + SDbConsts.FIELD_CODE + ", "
                + "v.week AS " + SDbConsts.FIELD_NAME + ", " 
                + "v.dt_start, "
                + "v.dt_end, "
                + "v.b_done, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_WAH_LAB) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.id_wah_lab, v.year, v.week, v.dt_start, v.dt_end ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[11];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, SDbConsts.FIELD_CODE, "Año");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, SDbConsts.FIELD_NAME, "Semana");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_start", "Fecha inicial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_end", "Fecha final");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_done", "Terminado");
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
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == mjNewWithLastTest) {
                actionNewWithLastTest();
            }
        }
    }
}
