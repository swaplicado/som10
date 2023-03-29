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
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import som.mod.SModConsts;
import som.mod.som.db.SSomWahLabTestReport;
import som.mod.som.form.SDialogReportMails;

/**
 *
 * @author Isabel Servín
 */
public class SViewWahLab extends SGridPaneView implements ActionListener {
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton mjNewWithLastTest;
    private JButton mjSendReport;
    
    public SViewWahLab(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_WAH_LAB, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        mjNewWithLastTest = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_new_main.gif")), "Analisis nuevo con resultados de análisis anterior", this);
        mjSendReport = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mail.gif")), "Enviar registro de resultados", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_LEFT).add(mjNewWithLastTest, 1);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendReport);
    }
    
    private void actionNewWithLastTest() {
        miClient.getSession().getModule(SModConsts.MOD_SOM_OS).showForm(SModConsts.S_WAH_LAB, SModConsts.S_WAH_LAB_W_LAST_TEST, null);
    }
    
    
    private void actionSendReport() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            
            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                SDialogReportMails testMails = new SDialogReportMails(miClient, SModConsts.S_WAH_LAB);
                testMails.setVisible(true);
                if (testMails.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                    SSomWahLabTestReport testReport = new SSomWahLabTestReport(miClient);
                    testReport.sendReport(testMails.getMails(), gridRow.getRowPrimaryKey());
                }
            }
        }
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
                + "v.val, "
                + "v.year AS " + SDbConsts.FIELD_CODE + ", "
                + "v.week AS " + SDbConsts.FIELD_NAME + ", " 
                + "v.dt_start, "
                + "v.dt_end, "
                + "v.b_done, "
                + "v.b_val, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_done AS usr_done_id, "
                + "v.fk_usr_val AS usr_val_id, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_done AS ts_done, "
                + "v.ts_usr_val AS ts_val, "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ud.name AS usr_done, "
                + "uv.name AS usr_val, "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_WAH_LAB) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ud ON "
                + "v.fk_usr_done = ud.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uv ON "
                + "v.fk_usr_val = uv.id_usr "
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
        SGridColumnView[] columns = new SGridColumnView[17];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, SDbConsts.FIELD_CODE, "Año");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, SDbConsts.FIELD_NAME, "Semana");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_start", "Fecha inicial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_end", "Fecha final");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "val", "Validaciones");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_done", "Terminado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_val", "Validado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_done", "Usr ter");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_done", "Usr TS ter");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_val", "Usr val");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_val", "Usr TS val");
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
            else if (button == mjSendReport) {
                actionSendReport();
            }
        }
    }
}
