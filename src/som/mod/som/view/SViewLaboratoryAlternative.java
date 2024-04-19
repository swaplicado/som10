/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
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
import som.gui.SGuiClientSessionCustom;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.som.db.SDbTicketAlternative;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Isabel Servin
 */
public class SViewLaboratoryAlternative extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;
    private JButton mjbPrint;
    
    private String msItemCodes;
    
    public SViewLaboratoryAlternative(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_ALT_LAB, gridSubtype, title);
        setRowButtonsEnabled(true, true, false, false, true);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        try {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
            
            moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "itm");
            
            mjbPrint = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_print.gif")), SUtilConsts.TXT_PRINT + " boleto", this);

            if (mnGridSubtype == SModConsts.SX_ALT_W_LAB) {
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbPrint);
            }
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
            
            msItemCodes = SSomUtils.getAlternativeItemCodes(miClient.getSession());
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionPrint() {
        if (mjbPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                HashMap<String, Object> map = SPrtUtils.createReportParamsMap(miClient.getSession());
                DecimalFormat oformatDecimal = SLibUtils.RoundingDecimalFormat;

                oformatDecimal.setMaximumFractionDigits(SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits());

                map.put("nTicketId", getSelectedGridRow().getRowPrimaryKey()[0]);
                map.put("sCurrencyCode", ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getLocalCurrencyCode());
                map.put("bShowMoney", false);
                map.put("oFormatDecimal", oformatDecimal);

                miClient.getSession().printReport(SModConsts.SR_ALT_LAB, SLibConsts.UNDEFINED, null, map);
            }
        }
    }
    
    @Override
    public void actionRowDelete() {
        SDbTicketAlternative ticket;

        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    ticket = (SDbTicketAlternative) miClient.getSession().readRegistry(SModConsts.S_ALT_TIC, gridRow.getRowPrimaryKey());

                    if (miClient.showMsgBoxConfirm("¿Desea eliminar el análisis de laboratorio del boleto de SOM Aguacate?") == JOptionPane.YES_OPTION) {
                        try {
                            ticket.deleteLabTest(miClient.getSession());

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sqlWhere = "";
        String joinType = "LEFT OUTER JOIN";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setSystemApplying(false);
        
        if (mnGridSubtype == SModConsts.SX_ALT_W_LAB) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
            joinType = "INNER JOIN";
        }
        else if (mnGridSubtype == SModConsts.SX_ALT_WO_LAB) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "v.fk_lab_n IS NULL ";
        }
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if(!sqlFilter.isEmpty()) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + sqlFilter;
        }
        
        msSql = "SELECT " 
                + "v.id_tic AS " + SDbConsts.FIELD_ID + "1, " 
                + "v.num AS " + SDbConsts.FIELD_CODE + ", " 
                + "v.num AS " + SDbConsts.FIELD_NAME + ", " 
                + "vl.dt, " 
                + "vl.yield_per, "
                + "v.dt, " 
                + "sc.id_sca, " 
                + "sc.code, " 
                + "sc.name, " 
                + "it.code, " 
                + "it.name, " 
                + "pr.code, " 
                + "pr.name, " 
                + "pr.name_trd, " 
                + "vl.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " 
                + "vl.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " 
                + "vl.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " 
                + "vl.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", " 
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_ALT_TIC) + " AS v " 
                + joinType + " " + SModConsts.TablesMap.get(SModConsts.S_ALT_LAB) + " AS vl ON "
                + "v.fk_lab_n = vl.id_alt_lab " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sc ON "
                + "v.fk_sca = sc.id_sca " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS it ON "
                + "v.fk_item = it.id_item " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pr ON "
                + "v.fk_prod = pr.id_prod " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "vl.fk_usr_ins = ui.id_usr " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "vl.fk_usr_upd = uu.id_usr " 
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + "AND v.fk_item IN (" + msItemCodes + ") "
                + "ORDER BY v.id_tic;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns;
        if (mnGridSubtype == SModConsts.SX_ALT_W_LAB) {
            columns = new SGridColumnView[14];
        }
        else {
            columns = new SGridColumnView[8];
        }
        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sc.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "pr.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pr.name_trd", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "pr.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "it.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "it.code", "Ítem código");
        if (mnGridSubtype == SModConsts.SX_ALT_W_LAB) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "vl.dt", "Fecha de análisis");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "vl.yield_per", "Rendimiento (%)");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        }
    
        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_ALT_TIC);
        moSuscriptionsSet.add(SModConsts.S_ALT_LAB);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbPrint) {
                actionPrint();
            }
        }
    }
}
