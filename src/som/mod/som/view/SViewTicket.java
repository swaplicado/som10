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
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
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
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "itm.fk_inp_ct" }, (int[]) filter);
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
                + "v.qty, " // seemingly unused
                + "v.pla, "
                + "v.pla_cag, "
                + "v.drv, "
                + "v.ts_arr, "
                + "v.ts_dep, "
                + "v.pac_qty_arr, "     // Quantity of full packing pieces at arrival
                + "v.pac_qty_dep, "     // Quantity of full packing pieces at departure
                + "v.pac_emp_qty_arr, " // Quantity of empty packing pieces at arrival
                + "v.pac_emp_qty_dep, " // Quantity of empty packing pieces at departure
                + "v.pac_wei_arr, "     // pwa: Weight of packing at arrival (full and empty?)
                + "v.pac_wei_dep, "     // pwd: Weight of packing at departure (full and empty?)
                + "v.pac_wei_net_r, "   // pwn: Weight of packing net (= pwa – pwd)
                + "v.wei_src, "         // Declared weight at origin
                + "v.wei_des_arr, "     // wda: Weigth at destiny at arrival
                + "v.wei_des_dep, "     // wdd: Weigth at destiny at departure
                + "v.wei_des_gro_r, "   // wdg: Weigth gross at destiny  (= wda – wdd)
                + "v.wei_des_net_r, "   // wdn: Weigth net at destiny (= wdg – pwn)
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
                + "v.dps_dt_n, "
                + "v.b_rev_1, "
                + "v.b_rev_2, "
                + "v.b_wei_src, "
                + "v.b_mfg_out, "
                + "v.b_tar, "
                + "v.b_pay, "
                + "v.b_ass, "
                + "v.b_paq, "
                + "v.b_lab, "
                + "v.b_dps, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "sca.id_sca, "
                + "sca.name, "
                + "sca.code, "
                + "tst.id_tic_st, "
                + "tst.name, "
                + "tst.code, "
                + "itm.id_item, "
                + "itm.name, "
                + "itm.code, "
                + "prd.id_prod, "
                + "prd.name, "
                + "prd.code, "
                + "src.id_inp_src, "
                + "src.name, "
                + "src.code, "
                + "sea.id_seas, "
                + "sea.name, "
                + "reg.id_reg, "
                + "reg.name, "
                + "lab.num, "
                + "lab.dt, "
                + "if (lab.b_done, " + SGridConsts.ICON_OK + ", " + SGridConsts.ICON_NULL + ") AS _lab_done, "
                + "COALESCE(seareg.prc_ton, 0.0) AS _prc_ton_reg, "
                + "COALESCE(seaprd.prc_ton, 0.0) AS _prc_ton_prd, "
                + "(v.pac_qty_arr * itm.paq_wei) AS _pac_wei_arr, "
                + "(v.pac_emp_qty_arr * itm.paq_wei) AS _pac_emp_wei_arr, "
                + "(v.pac_qty_dep * itm.paq_wei) AS _pac_wei_dep, "
                + "(v.pac_emp_qty_dep * itm.paq_wei) AS _pac_emp_wei_dep, "
                + "IF(v.wei_des_dep = 0, 0, IF(itm.b_paq = 0, 0, "
                + "(((v.wei_des_arr - v.wei_des_dep) - "
                + "((v.pac_qty_arr + v.pac_emp_qty_arr) * itm.paq_wei) + "
                + "((v.pac_qty_dep + v.pac_emp_qty_dep) * itm.paq_wei)) /"
                + "(v.pac_qty_arr)))) AS _wei_ave, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sca ON "
                + "v.fk_sca = sca.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_TIC_ST) + " AS tst ON "
                + "v.fk_tic_st = tst.id_tic_st "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS itm ON "
                + "v.fk_item = itm.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS prd ON "
                + "v.fk_prod = prd.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_SRC) + " AS src ON "
                + "v.fk_inp_src = src.id_inp_src "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS sea ON "
                + "v.fk_seas_n = sea.id_seas "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS reg ON "
                + "v.fk_reg_n = reg.id_reg "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_LAB) + " AS lab ON "
                + "v.fk_lab_n = lab.id_lab AND lab.b_del = 0 "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_REG) + " AS seareg ON "
                + "v.fk_seas_n = seareg.id_seas AND v.fk_reg_n = seareg.id_reg AND v.fk_item = seareg.id_item "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS seaprd ON "
                + "v.fk_seas_n = seaprd.id_seas AND v.fk_reg_n = seaprd.id_reg AND v.fk_item = seaprd.id_item AND v.fk_prod = seaprd.id_prod "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY sca.code, sca.id_sca, v.num, v.id_tic ";
    }

    @Override
    public void createGridColumns() {
        int cols = 40;

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

        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[cols];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.num", "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "prd.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "prd.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "itm.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "itm.code", "Ítem código");

        if (mnGridSubtype == SLibConsts.UNDEFINED) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tst.name", "Estatus boleto");
        }

        if (mnGridSubtype == SModSysConsts.SS_TIC_ST_LAB) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "lab.num", "Folio análisis lab");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE,  "lab.dt", SGridConsts.COL_TITLE_DATE + " análisis lab");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_lab_done", "Análisis lab terminado");
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sea.name", "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "reg.name", "Región");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "src.name", "Origen insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla", "Placas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla_cag", "Placas caja");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.drv", "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_arr", "TS entrada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_dep", "TS salida");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_tar", "Tarado");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_src", "Peso origen (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_arr", "Peso entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_dep", "Peso salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_qty_arr", "Cant empaq lleno entrada (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_emp_qty_arr", "Cant empaq vacío entrada (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_qty_dep", "Cant empaq lleno salida (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_emp_qty_dep", "Cant empaq vacío salida (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_wei_arr", "Peso empaq lleno entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_emp_wei_arr", "Peso empaq vacío entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_wei_arr", "Peso empaq total entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_wei_dep", "Peso empaq lleno salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_emp_wei_dep", "Peso empaq vacío salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_wei_dep", "Peso empaq total salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_wei_ave", "Peso promedio (" + SSomConsts.KG + "/" + SSomConsts.PIECE + ")");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_gro_r", "Carga destino bruto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_net_r", "Carga destino neto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_rev_1", "1a pesada Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_rev_2", "2a pesada Revuelta");

        if (mnGridSubtype == SModSysConsts.SS_TIC_ST_ADM) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_prc_ton_reg", "Precio ton reg $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_prc_ton_prd", "Precio ton prod $");
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
