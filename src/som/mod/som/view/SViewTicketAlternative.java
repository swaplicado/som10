/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbTicketAlternative;
import som.mod.som.db.SSomConsts;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Isabel Servín
 */
public class SViewTicketAlternative extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;
    private String msItemCodes;
    
    private SPaneFilter moPaneFilterTicketOrigin;
    private SPaneFilter moPaneFilterTicketDestination;
    
    private JButton mjbDelete;
    private JButton mjbPrint;
    
    public SViewTicketAlternative(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_ALT_TIC, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, true, false, false, false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        try {
            mjbDelete = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_delete.gif")), "Eliminar de SOM Orgánico", this);
            mjbPrint = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_print.gif")), SUtilConsts.TXT_PRINT + " boleto", this);

            msItemCodes = SSomUtils.getAlternativeItemCodes(miClient.getSession());
            
            moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "itm");
            
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
            moPaneFilterTicketOrigin = new SPaneFilter(this, SModConsts.SU_TIC_ORIG);
            moPaneFilterTicketOrigin.initFilter(null);
            moPaneFilterTicketDestination = new SPaneFilter(this, SModConsts.SU_TIC_DEST);
            moPaneFilterTicketDestination.initFilter(null);

            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketOrigin);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketDestination);
        
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbDelete);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbPrint);

            mjbDelete.setEnabled(miClient.getSession().getUser().isAdministrator());
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }

    private void actionQuit() {
        SDbTicketAlternative ticket;

        if (mjbDelete.isEnabled()) {
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

                    if (ticket.getFkLaboratoryId_n() == 0) {
                        if (miClient.showMsgBoxConfirm("¿Desea eliminar el boleto de SOM Orgánico?") == JOptionPane.YES_OPTION) {
                            deleteTicket(ticket);
                        }
                    }
                    else {
                        if (miClient.showMsgBoxConfirm("El boleto tiene resultado de laboratorio\n¿Desea eliminar el boleto de SOM Orgánico junto con el análisis?") == JOptionPane.YES_OPTION) {
                            deleteTicket(ticket);
                        }
                    }
                }
            }
        }
    }
    
    private void deleteTicket(SDbTicketAlternative ticket) {
        try {
            ticket.delete(miClient.getSession());

            miClient.getSession().notifySuscriptors(mnGridType);
            miClient.getSession().notifySuscriptors(mnGridSubtype);
            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN);
            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN_SUP);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
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
                    try {
                        SDbTicketAlternative ta = new SDbTicketAlternative();
                        HashMap<String, Object> map = SPrtUtils.createReportParamsMap(miClient.getSession());
                        
                        map.put("nTicketId", gridRow.getRowPrimaryKey()[0]);
                        map.put("sTable", ta.getTicketTable(miClient.getSession(), gridRow.getRowPrimaryKey()));
                        
                        miClient.getSession().printReport(SModConsts.SR_ALT_TIC, SLibConsts.UNDEFINED, null, map);
                
                    } 
                    catch (Exception e) {
                        miClient.showMsgBoxError(e.getMessage());
                    }
                }
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        Object filter;
        String sqlWhere = "";

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_ORIG);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_tic_orig" }, (int[]) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_DEST);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_tic_dest" }, (int[]) filter);
        }
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if(!sqlFilter.isEmpty()) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + sqlFilter;
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
                + "v.b_alt, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "sca.id_sca, "
                + "sca.code AS sca_code, "
                + "sca.name AS sca_name, "
                + "tst.id_tic_st, "
                + "tst.code AS tst_code, "
                + "tst.name AS tst_name, "
                + "itm.id_item, "
                + "itm.code AS itm_code, "
                + "itm.name AS itm_name, "
                + "prd.id_prod, "
                + "prd.code AS prd_code, "
                + "prd.name AS prd_name, "
                + "prd.name_trd, "
                + "src.id_inp_src, "
                + "src.code AS src_code, "
                + "src.name AS src_name, "
                + "sea.id_seas, "
                + "sea.name AS sea_name, "
                + "tor.name AS org_bol, "
                + "tde.name AS des_bol, "
                + "reg.id_reg, "
                + "reg.name AS reg_name, "
                + "lab.dt AS lab_dt, "
                + "w.code AS w_code, "
                + "w.name AS w_name, "
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
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_ORIG) + " AS tor ON "
                + "v.fk_tic_orig = tor.id_tic_orig "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_DEST) + " AS tde ON "
                + "v.fk_tic_dest = tde.id_tic_dest "
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
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w ON " 
                + "v.fk_wah_unld_co_n = w.id_co AND v.fk_wah_unld_cob_n = w.id_cob AND v.fk_wah_unld_wah_n = w.id_wah "                
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + "AND NOT v.b_alt AND v.b_tar AND v.fk_tic_orig = " + SModSysConsts.SU_TIC_ORIG_PRV + " "
                + "AND v.fk_item IN (" + msItemCodes + ") "
                + "UNION "
                + "SELECT "
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
                + "v.b_alt, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "sca.id_sca, "
                + "sca.code AS sca_code, "
                + "sca.name AS sca_name, "
                + "tst.id_tic_st, "
                + "tst.code AS tst_code, "
                + "tst.name AS tst_name, "
                + "itm.id_item, "
                + "itm.code AS itm_code, "
                + "itm.name AS itm_name, "
                + "prd.id_prod, "
                + "prd.code AS prd_code, "
                + "prd.name AS prd_name, "
                + "prd.name_trd, "
                + "src.id_inp_src, "
                + "src.code AS src_code, "
                + "src.name AS src_name, "
                + "sea.id_seas, "
                + "sea.name AS sea_name, "
                + "tor.name AS org_bol, "
                + "tde.name AS des_bol, "
                + "reg.id_reg, "
                + "reg.name AS reg_name, "
                + "lab.dt AS lab_dt, "
                + "w.code AS w_code, "
                + "w.name AS w_name, "
                + "COALESCE(lab.id_alt_lab, " + SGridConsts.ICON_OK + ", " + SGridConsts.ICON_NULL + ") AS _lab_done, "
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
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_ALT_TIC) + " AS v "
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
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_ORIG) + " AS tor ON "
                + "v.fk_tic_orig = tor.id_tic_orig "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_DEST) + " AS tde ON "
                + "v.fk_tic_dest = tde.id_tic_dest "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS sea ON "
                + "v.fk_seas_n = sea.id_seas "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS reg ON "
                + "v.fk_reg_n = reg.id_reg "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_ALT_LAB) + " AS lab ON "
                + "v.fk_lab_n = lab.id_alt_lab "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_REG) + " AS seareg ON "
                + "v.fk_seas_n = seareg.id_seas AND v.fk_reg_n = seareg.id_reg AND v.fk_item = seareg.id_item "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS seaprd ON "
                + "v.fk_seas_n = seaprd.id_seas AND v.fk_reg_n = seaprd.id_reg AND v.fk_item = seaprd.id_item AND v.fk_prod = seaprd.id_prod "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS w ON " 
                + "v.fk_wah_unld_co_n = w.id_co AND v.fk_wah_unld_cob_n = w.id_cob AND v.fk_wah_unld_wah_n = w.id_wah "                
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + "AND v.b_alt "
                + "AND v.fk_item IN (" + msItemCodes + ") "
                + "ORDER BY sca_code, id_sca, num, f_id_1 ";
    }

    @Override
    public void createGridColumns() {
        int cols = 46;

        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[cols];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca_code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "num", "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "prd_name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "name_trd", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "prd_code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "itm_name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "itm_code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sea_name", "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "reg_name", "Región");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "src_name", "Origen insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pla", "Placas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pla_cag", "Placas caja");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "drv", "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_arr", "TS entrada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_dep", "TS salida");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_tar", "Tarado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "org_bol", "Procedencia boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "des_bol", "Destino boleto");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "wei_src", "Peso origen (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "wei_des_arr", "Peso entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "wei_des_dep", "Peso salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "pac_qty_arr", "Cant empaq lleno entrada (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "pac_emp_qty_arr", "Cant empaq vacío entrada (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "pac_qty_dep", "Cant empaq lleno salida (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "pac_emp_qty_dep", "Cant empaq vacío salida (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_wei_arr", "Peso empaq lleno entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_emp_wei_arr", "Peso empaq vacío entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "pac_wei_arr", "Peso empaq total entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_wei_dep", "Peso empaq lleno salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_emp_wei_dep", "Peso empaq vacío salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "pac_wei_dep", "Peso empaq total salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_wei_ave", "Peso promedio (" + SSomConsts.KG + "/" + SSomConsts.PIECE + ")");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "wei_des_gro_r", "Carga destino bruto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "wei_des_net_r", "Carga destino neta (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_rev_1", "1a pesada Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_rev_2", "2a pesada Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "w_name", "Almacén descarga");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "w_code", "Almacén descarga código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_alt", "SOM Orgánico");
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
        moSuscriptionsSet.add(SModConsts.S_ALT_LAB);
        moSuscriptionsSet.add(SModConsts.SX_TIC_TARE);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbDelete) {
                actionQuit();
            }
            else if (button == mjbPrint) {
                actionPrint();
            }
        }
    }
}
