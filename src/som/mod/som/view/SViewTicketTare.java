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
 * @author Juan Barajas, Sergio Flores, Alfredo Pérez
 */
public class SViewTicketTare extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterDateCutOff moFilterDateCutOff;

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
        jbMailReceptions = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mail.gif")), "Enviar mail de recepciones del día", this);

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
    }

    private void actionTicketTare() {
        SDbTicket ticket = null;
        SGridRowView gridRow = null;

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

    private void actionMail() {
        int count = 0;

        if (jbMailReceptions.isEnabled()) {

            moDialogDailyMail.setFormReset();
            moDialogDailyMail.setVisible(true);
            if (moDialogDailyMail.getFormResult() == SGuiConsts.FORM_RESULT_OK) {

                try {
                    count = SSomMailUtils.computeMailReceptions(miClient.getSession(), moDialogDailyMail.getDateStart(), moDialogDailyMail.getDateEnd());
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
        String sql = "";
        Object filter = null;

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

        msSql = "SELECT "
                + "v.id_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.drv AS " + SDbConsts.FIELD_NAME + ", "
                + "v.pla, "
                + "v.pla_cag, "
                + "sc.code, "
                + "se.name, "
                + "re.name, "
                + "it.name, "
                + "it.code, "
                + "pr.name, "
                + "pr.code, "
                + "isrc.name, "
                + "isrc.code, "
                + "v.b_rev_1, "
                + "v.b_rev_2, "
                + "v.wei_src, "
                + "v.wei_des_arr, "
                + "v.wei_des_dep, "
                + "v.wei_des_gro_r, "
                + "v.wei_des_net_r, "
                + "v.ts_arr, "
                + "v.ts_dep, "
                + "v.pac_wei_arr, "
                + "v.pac_wei_dep, "
                + "vs.name, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "lb.id_lab, "
                + "lb.dt " + SDbConsts.FIELD_DATE + "1, "
                + "lb.num, "
                + "lb.b_done, "
                + "(v.pac_emp_qty_arr * it.paq_wei) AS f_pac_emp_qty_arr, "
                + "(v.pac_emp_qty_dep * it.paq_wei) AS f_pac_emp_qty_dep, "
                + "IF(v.pac_wei_dep = 0, 0, (((v.wei_src - v.wei_des_dep) - "
                + "((v.pac_qty_arr + v.pac_emp_qty_arr) * it.paq_wei) + "
                + "((v.pac_qty_dep + v.pac_emp_qty_dep) * it.paq_wei)) /"
                + "(v.pac_qty_arr))) AS f_wei_ave, "
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
                + "v.fk_lab_n = lb.id_lab "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY sc.code, v.num, v.dt, v.id_tic ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = null;

        if (mnGridSubtype == SModConsts.SX_TIC_TARE) {
            columns = new SGridColumnView[37];
        }
        else {
            columns = new SGridColumnView[34];
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sc.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "pr.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "pr.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "it.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "it.code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vs.name", "Estatus");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "se.name", "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "re.name", "Región");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "isrc.name", "Origen insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla", "Placas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla_cag", "Placas caja");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SDbConsts.FIELD_NAME, "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_arr", "TS entrada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_dep", "TS  salida");
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
        if (mnGridSubtype == SModConsts.SX_TIC_TARE) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "lb.num", "Folio análisis");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE,  SDbConsts.FIELD_DATE + "1", "Fecha análisis");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "lb.b_done", "Análisis terminado");
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
                actionMail();
            }
        }
    }
}
