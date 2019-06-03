/*
 * To change this template, choose Tools | Templates
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
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;
import som.gui.SGuiClientSessionCustom;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbTicket;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores, Alfredo Pérez
 */
public class SViewTicket extends SGridPaneView implements ActionListener {

    private SGridFilterDateCutOff moFilterDateCutOff;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SPaneFilter moPaneFilterInputCategory;
    private SPaneFilter moPaneFilterItem;
    private JButton mjbPreviousStep;
    private JButton mjbNextStep;
    private JButton mjbLaboratoryTest;
    private JButton mjbManager;
    private JButton mjbSeasonRegion;
    private JButton mjbPrint;

    public SViewTicket(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_TIC, gridSubtype, title);
        setRowButtonsEnabled(true, true, false, false, true);

        initComponetsCustom();
    }

    private void initComponetsCustom() {
        mjbPreviousStep = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_left.gif")), "Regresar boleto al estado anterior", this);
        mjbNextStep = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_right.gif")), "Enviar boleto al estado siguiente", this);
        mjbLaboratoryTest = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_lab.gif")), "Agregar análisis de laboratorio", this);
        mjbManager = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_tic_edit.gif")), "Modificar boleto", this);
        mjbSeasonRegion = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_tic_cfg.gif")), "Cambiar temporada y región", this);
        mjbPrint = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_print.gif")), SUtilConsts.TXT_PRINT + " boleto", this);

        moPaneFilterInputCategory = new SPaneFilter(this, SModConsts.SU_INP_CT);
        moPaneFilterInputCategory.initFilter(null);
        moPaneFilterItem = new SPaneFilter(this, SModConsts.SU_ITEM);
        moPaneFilterItem.initFilter(null);

        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
            case SModSysConsts.SS_TIC_ST_LAB:
                moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
                moFilterDateCutOff.initFilter(null);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
                break;
                
            case SModSysConsts.SS_TIC_ST_ADM:
            case SLibConsts.UNDEFINED:
                moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
                moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbPreviousStep);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbNextStep);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbLaboratoryTest);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbManager);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbSeasonRegion);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterItem);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbPrint);

        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
                mjbPreviousStep.setEnabled(false);
                mjbNextStep.setEnabled(true);
                mjbLaboratoryTest.setEnabled(false);
                mjbManager.setEnabled(false);
                mjbSeasonRegion.setEnabled(false);
                break;
                
            case SModSysConsts.SS_TIC_ST_LAB:
                mjbPreviousStep.setEnabled(true);
                mjbNextStep.setEnabled(true);
                mjbLaboratoryTest.setEnabled(true);
                mjbManager.setEnabled(false);
                mjbSeasonRegion.setEnabled(false);
                setRowButtonsEnabled(false);
                break;
                
            case SModSysConsts.SS_TIC_ST_ADM:
                mjbPreviousStep.setEnabled(true);
                mjbNextStep.setEnabled(false);
                mjbLaboratoryTest.setEnabled(false);
                mjbManager.setEnabled(true);
                mjbSeasonRegion.setEnabled(true);
                setRowButtonsEnabled(false);
                break;
                
            case SLibConsts.UNDEFINED:
                mjbPreviousStep.setEnabled(false);
                mjbNextStep.setEnabled(false);
                mjbLaboratoryTest.setEnabled(false);
                mjbManager.setEnabled(false);
                mjbSeasonRegion.setEnabled(false);
                setRowButtonsEnabled(false);
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    private void actionPreviousStep() {
        SDbTicket ticket = null;

        if (mjbPreviousStep.isEnabled()) {
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
                    ticket = (SDbTicket) miClient.getSession().readRegistry(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    if (miClient.showMsgBoxConfirm("¿Desea regresar el boleto al estado anterior?") == JOptionPane.YES_OPTION) {
                        try {
                            ticket.movePrevious(miClient.getSession());

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN_SUP);
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }
            }
        }
    }

    private void actionNextStep() {
        SDbTicket ticket = null;

        if (mjbNextStep.isEnabled()) {
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
                    ticket = (SDbTicket) miClient.getSession().readRegistry(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    if (miClient.showMsgBoxConfirm("¿Desea enviar el boleto al estado siguiente?") == JOptionPane.YES_OPTION) {
                        try {
                            ticket.moveNext(miClient.getSession());

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN_SUP);
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }
            }
        }
    }

    private void actionLaboratoryTest() {
        SGuiParams params = null;

        if (mjbLaboratoryTest.isEnabled()) {
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
                    params = new SGuiParams(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    miClient.getSession().getModule(SModConsts.MOD_SOM_RM, SLibConsts.UNDEFINED).showForm(SModConsts.SX_TIC_LAB, SLibConsts.UNDEFINED, params);
                }
            }
        }
    }

    private void actionManager() {
        SDbTicket ticket = null;
        SGuiParams params = null;

        if (mjbManager.isEnabled()) {
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
                    ticket = (SDbTicket) miClient.getSession().readRegistry(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    if (ticket.isLaboratory()) {
                        params = new SGuiParams(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                        miClient.getSession().getModule(SModConsts.MOD_SOM_RM, SLibConsts.UNDEFINED).showForm(SModConsts.SX_TIC_MAN, SLibConsts.UNDEFINED, params);
                    }
                    else {
                        miClient.showMsgBoxInformation("El boleto '" + ticket.getNumber() + "' no puede ser modificado por gerencia, porque no requiere análisis de laboratorio.");
                    }
                }
            }
        }
    }

    private void actionSeasonRegion() {
        SDbTicket ticket = null;
        SGuiParams params = null;

        if (mjbManager.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                ticket = (SDbTicket) miClient.getSession().readRegistry(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                if (ticket.isLaboratory()) {
                    params = new SGuiParams(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    miClient.getSession().getModule(SModConsts.MOD_SOM_RM, SLibConsts.UNDEFINED).showForm(SModConsts.SX_TIC_SEAS_REG, SLibConsts.UNDEFINED, params);
                }
                else {
                    miClient.showMsgBoxInformation("El boleto '" + ticket.getNumber() + "' no necesita configurar temporada ni región.");
                }
            }
        }
    }

    private void actionPrint() {
        if (mjbPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                
                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    HashMap<String, Object> map = SPrtUtils.createReportParamsMap(miClient.getSession());
                    DecimalFormat oformatDecimal = SLibUtils.RoundingDecimalFormat;

                    oformatDecimal.setMaximumFractionDigits(SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits());

                    map.put("nTicketId", gridRow.getRowPrimaryKey()[0]);
                    map.put("sCurrencyCode", ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getLocalCurrencyCode());
                    map.put("bShowMoney", miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_RM));
                    map.put("oFormatDecimal", oformatDecimal);

                    miClient.getSession().printReport(SModConsts.SR_TIC, SLibConsts.UNDEFINED, null, map);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

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

        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
            case SModSysConsts.SS_TIC_ST_LAB:
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
                if (filter != null) {
                    sql += (sql.length() == 0 ? "" : "AND ") + " v.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
                }
                break;
                
            case SModSysConsts.SS_TIC_ST_ADM:
            case SLibConsts.UNDEFINED:
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
                if (filter != null) {
                    sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                }
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
                
        filter = (int[]) moFiltersMap.get(SModConsts.SU_INP_CT);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "it.fk_inp_ct" }, (int[]) filter);
        }

        filter = (int[]) moFiltersMap.get(SModConsts.SU_ITEM);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_item" }, (int[]) filter);
        }

        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_tic_st = " + mnGridSubtype + " ";
                break;
            case SModSysConsts.SS_TIC_ST_LAB:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_tic_st = " + mnGridSubtype + " ";
                break;
            case SModSysConsts.SS_TIC_ST_ADM:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_tic_st = " + mnGridSubtype + " ";
                break;
            case SLibConsts.UNDEFINED:
                break;
            default:
        }

        msSql = "SELECT "
                + "v.id_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.num, "
                + "v.dt, "
                + "v.pla, "
                + "v.pla_cag, "
                + "v.drv, "
                + "v.ts_arr, "
                + "v.ts_dep, "
                + "v.pac_wei_arr, "
                + "v.pac_wei_dep, "
                + "v.wei_src, "
                + "v.wei_des_arr, "
                + "v.wei_des_dep, "
                + "v.wei_des_gro_r, "
                + "v.wei_des_net_r, "
                + "COALESCE(sr.prc_ton, 0.0) AS _prc_ton_reg, "
                + "COALESCE(sp.prc_ton, 0.0) AS _prc_ton_prod, "
                + "v.sys_pen_per, "
                + "v.sys_wei_pay, "
                + "v.sys_prc_ton, "
                + "v.sys_pay_r, "
                + "v.sys_fre, "
                + "v.sys_tot_r, "
                + "v.usr_pen_per, "
                + "v.usr_wei_pay, "
                + "v.usr_prc_ton, "
                + "v.usr_pay_r, "
                + "v.usr_fre, "
                + "v.usr_tot_r, "
                + "v.b_tar, "
                + "v.b_pay, "
                + "v.b_rev_1, "
                + "v.b_rev_2, "
                + "vs.name, "
                + "vs.code, "
                + "sc.id_sca, "
                + "sc.name, "
                + "sc.code, "
                + "it.name, "
                + "it.code, "
                + "pr.name, "
                + "pr.code, "
                + "isrc.name, "
                + "isrc.code, "
                + "se.name, "
                + "re.name, "
                + "lb.num, "
                + "lb.dt, "
                + "lb.b_done, "
                + "(v.pac_emp_qty_arr * it.paq_wei) AS f_pac_emp_qty_arr, "
                + "(v.pac_emp_qty_dep * it.paq_wei) AS f_pac_emp_qty_dep, "
                + "IF(v.wei_des_dep = 0, 0, IF(it.b_paq = 0, 0, "
                + "(((v.wei_des_arr - v.wei_des_dep) - "
                + "((v.pac_qty_arr + v.pac_emp_qty_arr) * it.paq_wei) + "
                + "((v.pac_qty_dep + v.pac_emp_qty_dep) * it.paq_wei)) /"
                + "(v.pac_qty_arr)))) AS f_wei_ave, "
                + "if (lb.b_done, " + SGridConsts.ICON_OK + ", " + SGridConsts.ICON_NULL + ") AS f_done, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sc ON "
                + "v.fk_sca = sc.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_TIC_ST) + " AS vs ON "
                + "v.fk_tic_st = vs.id_tic_st "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS it ON "
                + "v.fk_item = it.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pr ON "
                + "v.fk_prod = pr.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_SRC) + " AS isrc ON "
                + "v.fk_inp_src = isrc.id_inp_src "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS se ON "
                + "v.fk_seas_n = se.id_seas "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS re ON "
                + "v.fk_reg_n = re.id_reg "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_LAB) + " AS lb ON "
                + "v.fk_lab_n = lb.id_lab AND lb.b_del = 0 "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_REG) + " AS sr ON "
                + "v.fk_seas_n = sr.id_seas AND v.fk_reg_n = sr.id_reg AND v.fk_item = sr.id_item "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS sp ON "
                + "v.fk_seas_n = sp.id_seas AND v.fk_reg_n = sp.id_reg AND v.fk_item = sp.id_item AND v.fk_prod = sp.id_prod "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY sc.code, sc.id_sca, v.num, v.id_tic ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        int cols = 34;
        SGridColumnView[] columns = null;

        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
                break;
            case SModSysConsts.SS_TIC_ST_LAB:
                cols += 3;
                break;
            case SModSysConsts.SS_TIC_ST_ADM:
                cols += 15;
                break;
            case SLibConsts.UNDEFINED:
                cols += 1;
                break;
            default:
        }

        columns = new SGridColumnView[cols];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sc.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.num", "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "pr.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "pr.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "it.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "it.code", "Ítem código");

        if (mnGridSubtype == SLibConsts.UNDEFINED) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vs.name", "Estatus boleto");
        }

        if (mnGridSubtype == SModSysConsts.SS_TIC_ST_LAB) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "lb.num", "Análisis lab");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE,  "lb.dt", SGridConsts.COL_TITLE_DATE + " análisis lab");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "f_done", "Terminado");
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "se.name", "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "re.name", "Región");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "isrc.name", "Origen insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla", "Placas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla_cag", "Placas caja");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.drv", "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_arr", "TS entrada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_dep", "TS salida");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_tar", "Tarado");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_src", "Carga origen (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_arr", "Peso entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_dep", "Peso salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_wei_arr", "Cant empaque entrada");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_pac_emp_qty_arr", "Cant empaque vacío entrada");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_wei_dep", "Cant empaque salida");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_pac_emp_qty_dep", "Cant empaque vacío salida");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_wei_ave", "Peso promedio (" + SSomConsts.KG + "/empaque)");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_gro_r", "Carga destino bruto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_net_r", "Carga destino neto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_rev_1", "1a pesada Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_rev_2", "2a pesada Revuelta");

        if (mnGridSubtype == SModSysConsts.SS_TIC_ST_ADM) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_prc_ton_reg", "Precio ton reg $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_prc_ton_prod", "Precio ton prod $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.sys_pen_per", "Castigo sis");
            columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.sys_wei_pay", "Peso pago sis (" + SSomConsts.KG + ")");
            columns[col++].setSumApplying(true);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sys_prc_ton", "Precio sis $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sys_pay_r", "Pago sis $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sys_fre", "Flete sis $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sys_tot_r", "Total sis $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.usr_pen_per", "Castigo usr");
            columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.usr_wei_pay", "Peso pago usr (" + SSomConsts.KG + ")");
            columns[col++].setSumApplying(true);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.usr_prc_ton", "Precio usr $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.usr_pay_r", "Pago usr $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.usr_fre", "Flete usr $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.usr_tot_r", "Total usr $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_pay", "Pagado");
        }

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
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.SU_INP_SRC);
        moSuscriptionsSet.add(SModConsts.SU_SEAS);
        moSuscriptionsSet.add(SModConsts.SU_REG);
        moSuscriptionsSet.add(SModConsts.S_LAB);
        moSuscriptionsSet.add(SModConsts.SX_TIC_TARE);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionMouseClicked() {
        if (mnGridSubtype == SModSysConsts.SS_TIC_ST_LAB) {
            actionLaboratoryTest();
        }
        else if (mnGridSubtype == SModSysConsts.SS_TIC_ST_ADM) {
            actionManager();
        }
        else {
            super.actionMouseClicked();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbPreviousStep) {
                actionPreviousStep();
            }
            else if (button == mjbNextStep) {
                actionNextStep();
            }
            else if (button == mjbLaboratoryTest) {
                actionLaboratoryTest();
            }
            else if (button == mjbManager) {
                actionManager();
            }
            else if (button == mjbSeasonRegion) {
                actionSeasonRegion();
            }
            else if (button == mjbPrint) {
                actionPrint();
            }
        }
    }
}
