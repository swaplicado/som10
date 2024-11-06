/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibTimeUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import som.mod.SModConsts;
import som.mod.som.db.SSomWahLabTestReport;
import som.mod.som.form.SDialogReportMails;

/**
 *
 * @author Isabel Servín
 */
public class SViewWahLab extends SGridPaneView implements ActionListener {
    
    private final boolean mbIsViewDetail;
    
    private SGridFilterDateRange moFilterDateRange;
    private JButton mjNewWithLastTest;
    private JButton mjSendReport;
    
    public SViewWahLab(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_WAH_LAB, subType, title);
        mbIsViewDetail = mnGridSubtype == SModConsts.SX_WAH_LAB_DET; 
        if (mbIsViewDetail) {
            setRowButtonsEnabled(false);
        }
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(SLibTimeUtils.getWholeYear(miClient.getSession().getWorkingDate()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
        
        if (!mbIsViewDetail) {
            mjNewWithLastTest = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_new_main.gif")), "Analisis nuevo con resultados de análisis anterior", this);
            mjSendReport = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mail.gif")), "Enviar registro de resultados", this);

            getPanelCommandsSys(SGuiConsts.PANEL_LEFT).add(mjNewWithLastTest, 1);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendReport);
        }
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
        
        filter = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDateRange("v.dt_start", (Date[]) filter);
        }
        
        msSql = "SELECT "
                + "v.id_wah_lab AS " + SDbConsts.FIELD_ID + "1, "
                + "v.year AS " + SDbConsts.FIELD_CODE + ", "
                + "v.week AS " + SDbConsts.FIELD_NAME + ", " 
                + "v.dt_start, "
                + "v.dt_end, "
                + "v.val, "
                + "v.b_done, "
                + "v.b_val, "
                + (mbIsViewDetail ? "w.code AS wah, "
                + "w.name AS wah_name, "
                + "i.name AS item, "
                + "t.dt AS dt_test, "
                + "t.aci_per_n AS aci, "
                + "t.b_aci_per_overange AS aci_o, "
                + "t.per_ind_n AS ind, "
                + "t.b_per_ind_overange AS ind_o, "
                + "t.moi_per_n AS moi, "
                + "t.b_moi_per_overange AS moi_o, "
                + "t.sol_per_n AS sol, "
                + "t.b_sol_per_overange AS sol_o, "
                + "t.lin_per_n AS lin, "
                + "t.b_lin_per_overange AS lin_o, "
                + "t.ole_per_n AS ole, "
                + "t.b_ole_per_overange AS ole_o, "
                + "t.llc_per_n AS llc, "
                + "t.b_llc_per_overange AS llc_o, "
                + "t.ste_per_n AS ste, "
                + "t.b_ste_per_overange AS ste_o, "
                + "t.pal_per_n AS pal, "
                + "t.b_pal_per_overange AS pal_o, "
                + "t.note, " : "")
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
                + (mbIsViewDetail ? "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_WAH_LAB_TEST) + " AS t ON "
                + "v.id_wah_lab = t.id_wah_lab "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w ON "
                + "t.fk_wah_co = w.id_co AND t.fk_wah_cob = w.id_cob AND t.fk_wah_wah = w.id_wah "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "t.fk_item = i.id_item " : "")
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
        SGridColumnView[] columns;
        
        if (mbIsViewDetail) {
            columns = new SGridColumnView[40];
        }
        else {
            columns = new SGridColumnView[17];
        }
        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, SDbConsts.FIELD_CODE, "Año");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, SDbConsts.FIELD_NAME, "Semana");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_start", "Fecha inicial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_end", "Fecha final");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "val", "Validaciones");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_done", "Terminado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_val", "Validado");
        if (mbIsViewDetail) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "wah", "Almacén");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "wah_name", "Nombre almacén");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "item", "Ítem");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_test", "Fecha análisis");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "aci", "Acidez %");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "aci_o", "FR acidez");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "ind", "I. peróxidos");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "ind_o", "FR i. peróxidos");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "moi", "Humedad %");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "moi_o", "FR humedad");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "sol", "Sólidos %");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "sol_o", "FR sólidos");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "lin", "Linoleico %");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "lin_o", "FR linoleico");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "ole", "Oléico %");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "ole_o", "FR oléico");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "llc", "Linoléico %");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "llc_o", "FR linoléico");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "ste", "Esteárico %");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "ste_o", "FR esteárico");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "pal", "Palmítico %");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "pal_o", "FR palmítico");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "note", "Observaciones");
        }
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
