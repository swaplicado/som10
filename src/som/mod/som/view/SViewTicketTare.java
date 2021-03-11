/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbTicket;
import som.mod.som.db.SSomConsts;
import som.mod.som.db.SSomMailUtils;
import som.mod.som.form.SDialogMailReceptions;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public class SViewTicketTare extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterDateCutOff moFilterDateCutOff;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;

    private JButton jbTicketTare;
    private JButton jbMailReceptions;

    private SDialogMailReceptions moDialogDailyMail;

    public SViewTicketTare(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_TIC_TARE, gridSubtype, title);
        setRowButtonsEnabled(false, gridSubtype == SModConsts.SX_TIC_TARE_PEND, false, false, false);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        jbTicketTare = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_tar_not.gif")), "Destarar boleto", this);
        jbMailReceptions = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mail.gif")), "Enviar mail de recepciones del día o período", this);
        moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "itm");
        moDialogDailyMail = new SDialogMailReceptions(miClient, SModConsts.SX_DAY_MAIL, "Recepciones del día");

        switch (mnGridSubtype) {
            case SModConsts.SX_TIC_TARE:
                moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
                moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbTicketTare);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMailReceptions);
                break;
            case SModConsts.SX_TIC_TARE_PEND:
                moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
                moFilterDateCutOff.initFilter(null);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        jbTicketTare.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_RM));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
    }

    private void actionTicketTare() {
        SDbTicket ticket;
        SGridRowView gridRow;

        if (jbTicketTare.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                gridRow = (SGridRowView) getSelectedGridRow();

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

                    if (ticket.getFkTicketStatusId() < SModSysConsts.SS_TIC_ST_ADM) {
                        if (miClient.showMsgBoxConfirm("¿Está seguro(a) que desea destarar el boleto?") == JOptionPane.YES_OPTION) {
                            miClient.getSession().saveField(SModConsts.S_TIC, gridRow.getRowPrimaryKey(), SDbTicket.FIELD_TARED, false);

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
                            miClient.getSession().notifySuscriptors(SModConsts.S_TIC);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN_SUP);
                        }
                    }
                    else {
                        miClient.showMsgBoxWarning("Es necesario que el boleto esté con estatus en báscula o en laboratorio.");
                    }
                }
            }
        }
    }

    private void actionMailReceptions() {
        if (jbMailReceptions.isEnabled()) {
            moDialogDailyMail.setFormReset();
            moDialogDailyMail.setVisible(true);
            
            if (moDialogDailyMail.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                try {
                    int count = SSomMailUtils.computeMailReceptions(miClient.getSession(), moDialogDailyMail.getDateStart(), moDialogDailyMail.getDateEnd());
                    miClient.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED + "\nMails enviados: " + SLibUtils.DecimalFormatInteger.format(count) + ".");
                }
                catch (Exception e) {
                    SLibUtils.printException(this, e);
                }
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        Object filter;
        String sql = "";

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        switch (mnGridSubtype) {
            case SModConsts.SX_TIC_TARE:
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
                sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                break;
                
            case SModConsts.SX_TIC_TARE_PEND:
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
                if (filter != null) {
                    sql += (sql.length() == 0 ? "" : "AND ") + " v.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
                }
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        switch (mnGridSubtype) {
            case SModConsts.SX_TIC_TARE:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.b_tar = 1 ";
                break;
            case SModConsts.SX_TIC_TARE_PEND:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.b_tar = 0 ";
                break;
            default:
        }
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if(!sqlFilter.isEmpty()) {
            sql += (sql.isEmpty() ? "" : "AND ") + sqlFilter;
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
                + "sca.code, "
                + "sca.name, "
                + "tst.id_tic_st, "
                + "tst.code, "
                + "tst.name, "
                + "itm.id_item, "
                + "itm.code, "
                + "itm.name, "
                + "prd.id_prod, "
                + "prd.code, "
                + "prd.name, "
                + "prd.name_trd, "
                + "src.id_inp_src, "
                + "src.code, "
                + "src.name, "
                + "sea.id_seas, "
                + "sea.name, "
                + "reg.id_reg, "
                + "reg.name, "
                + "lab.num, "
                + "lab.dt, "
                + "if (lab.b_done, " + SGridConsts.ICON_OK + ", " + SGridConsts.ICON_NULL + ") AS _lab_done, "
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
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY sca.code, sca.id_sca, v.num, v.id_tic ";
    }

    @Override
    public void createGridColumns() {
        int cols = 41;

        switch (mnGridSubtype) {
            case SModConsts.SX_TIC_TARE:
                cols += 3;
                break;
            case SModConsts.SX_TIC_TARE_PEND:
                break;
            default:
        }

        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[cols];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.num", "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "prd.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "prd.name_trd", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "prd.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "itm.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "itm.code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tst.name", "Estatus boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sea.name", "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "reg.name", "Región");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "src.name", "Origen insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla", "Placas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla_cag", "Placas caja");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.drv", "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_arr", "TS entrada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_dep", "TS salida");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_src", "Peso origen (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_arr", "Peso entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_dep", "Peso salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_qty_arr", "Cant empaque lleno entrada (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_emp_qty_arr", "Cant empaque vacío entrada (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_qty_dep", "Cant empaque lleno salida (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_emp_qty_dep", "Cant empaque vacío salida (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_wei_arr", "Peso empaque lleno entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_emp_wei_arr", "Peso empaque vacío entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_wei_arr", "Peso empaque total entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_wei_dep", "Peso empaque lleno salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_emp_wei_dep", "Peso empaque vacío salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_wei_dep", "Peso empaque total salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_wei_ave", "Peso promedio (" + SSomConsts.KG + "/" + SSomConsts.PIECE + ")");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_gro_r", "Carga destino bruto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_net_r", "Carga destino neto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_rev_1", "1a pesada Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_rev_2", "2a pesada Revuelta");
        
        if (mnGridSubtype == SModConsts.SX_TIC_TARE) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "lab.num", "Folio análisis lab");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE,  "lab.dt", SGridConsts.COL_TITLE_DATE + " análisis lab");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_lab_done", "Análisis lab terminado");
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
        moSuscriptionsSet.add(mnGridSubtype);
        moSuscriptionsSet.add(SModConsts.S_TIC);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.SU_SEAS);
        moSuscriptionsSet.add(SModConsts.SU_REG);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.S_LAB);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbTicketTare) {
                actionTicketTare();
            }
            else if (button == jbMailReceptions) {
                actionMailReceptions();
            }
        }
    }
}
