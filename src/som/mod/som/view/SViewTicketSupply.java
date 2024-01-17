/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import sa.lib.SLibConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
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
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbTicket;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SViewTicketSupply extends SGridPaneView implements java.awt.event.ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterDateCutOff moFilterDateCutOff;

    private SPaneFilter moPaneFilter;
    private SPaneFilter moPaneFilterInputCategory;
    private SPaneFilter moPaneFilterTicketOrigin;
    private SPaneFilter moPaneFilterTicketDestination;
    private SPaneFilter moPaneFilterScale;
    
    private javax.swing.JButton jbTicketSupply;
    private javax.swing.JButton jbTicketClose;
    private javax.swing.JButton jbTicketOpen;

    public SViewTicketSupply(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_TIC_SUP_RM, gridSubtype, title);
        setRowButtonsEnabled(false, false, false, false, false);
        initComponetsCustom();
    }

    /*
    * Private methods
    */

    private void initComponetsCustom() {
        moPaneFilter = new SPaneFilter(this, SModConsts.SU_ITEM);
        moPaneFilter.initFilter(null);
        moPaneFilterInputCategory = new SPaneFilter(this, SModConsts.SU_INP_CT);
        moPaneFilterInputCategory.initFilter(null);
        moPaneFilterTicketOrigin = new SPaneFilter(this, SModConsts.SU_TIC_ORIG);
        moPaneFilterTicketOrigin.initFilter(null);
        moPaneFilterTicketDestination = new SPaneFilter(this, SModConsts.SU_TIC_DEST);
        moPaneFilterTicketDestination.initFilter(null);
        moPaneFilterScale = new SPaneFilter(this, SModConsts.SU_SCA);
        moPaneFilterScale.initFilter(null);

        if (mnGridSubtype == SModConsts.SX_TIC_MAN_SUP) {
            moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
            moFilterDateCutOff.initFilter((Date) null);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
        }
        else {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }

        jbTicketSupply = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mfg_fg_asd.gif")), "Surtir boleto", this);
        jbTicketClose = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_doc_close.gif")), "Cerrar para surtido", this);
        jbTicketOpen = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_doc_open.gif")), "Abrir para surtido", this);

        switch (mnGridSubtype) {
            case SModConsts.SX_TIC_MAN_SUP:
                jbTicketSupply.setEnabled(true);
                jbTicketClose.setEnabled(true);
                jbTicketOpen.setEnabled(false);
                break;
            case SModConsts.SX_TIC_ASSO:
                jbTicketSupply.setEnabled(false);
                jbTicketClose.setEnabled(false);
                jbTicketOpen.setEnabled(true);
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbTicketSupply);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbTicketClose);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbTicketOpen);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilter);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketOrigin);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketDestination);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterScale);
    }

    private void actionTicketSupply() {
        SGuiParams params = null;

        if (jbTicketSupply.isEnabled()) {
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
                    params = new SGuiParams();

                    params.getParamsMap().clear();
                    params.getParamsMap().put(SModConsts.SX_TIC_SUP_RM, true);
                    params.getParamsMap().put(SModConsts.SS_IOG_TP, SModSysConsts.SS_IOG_TP_IN_PUR_PUR);
                    params.getParamsMap().put(SModConsts.S_TIC, gridRow.getRowPrimaryKey());
                    miClient.getSession().getModule(SModConsts.MOD_SOM_OS, SLibConsts.UNDEFINED).showForm(SModConsts.S_IOG, SLibConsts.UNDEFINED, params);
                    notifySuscriptors();
                }
            }
        }
    }

    private void actionTicketCloseOpen(boolean bClose) {
        SDbTicket ticket = null;

        if (jbTicketClose.isEnabled() || jbTicketOpen.isEnabled()) {
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
                    try {
                        ticket = new SDbTicket();
                        ticket.read(miClient.getSession(), gridRow.getRowPrimaryKey());

                        if (mnGridSubtype == SModConsts.SX_TIC_ASSO && !ticket.isAssorted()) {
                            miClient.showMsgBoxWarning("El boleto '"+ ticket.getNumber() +"' no ha sido cerrado de forma manual, para abrirlo para surtido elimine los doctos. de inventarios.");
                        }
                        else if (miClient.showMsgBoxConfirm("Se " + (mnGridSubtype == SModConsts.SX_TIC_MAN_SUP ? "cerrará" : "abrirá") + " para surtido el boleto '" + ticket.getNumber() + "'. \n" +
                            SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                            ticket.setFkUserAssortedId(miClient.getSession().getUser().getPkUserId());
                            ticket.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbTicket.FIELD_ASSORTED, bClose);
                            notifySuscriptors();
                        }
                    }
                    catch (SQLException e) {
                        SLibUtils.showException(this, e);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void notifySuscriptors() {
        miClient.getSession().notifySuscriptors(mnGridType);
        miClient.getSession().notifySuscriptors(mnGridSubtype);
        miClient.getSession().notifySuscriptors(SModConsts.S_IOG);
        miClient.getSession().notifySuscriptors(SModConsts.S_STK);
        miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN);
        miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN_SUP);
    }

    /*
    * Public methods
    */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sqlDiogPeriod = "";
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
            sql += "AND v.b_del = 0 ";
        }

        if (mnGridSubtype == SModConsts.SX_TIC_MAN_SUP) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
            if (filter != null) {
                sqlDiogPeriod += "AND " + SGridUtils.getSqlFilterDate("ge.dt", (SGuiDate) filter);
            }
        }
        else {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
            if (filter != null) {
                sqlDiogPeriod += "AND " + SGridUtils.getSqlFilterDate("ge.dt", (SGuiDate) filter);
            }
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            sql += "AND " + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
        }

        filter = (int[]) moFiltersMap.get(SModConsts.SU_INP_CT);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "it.fk_inp_ct" }, (int[]) filter);
        }

        filter = (int[]) moFiltersMap.get(SModConsts.SU_ITEM);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_item" }, (int[]) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_ORIG);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_tic_orig" }, (int[]) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_DEST);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_tic_dest" }, (int[]) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_SCA);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_sca" }, (int[]) filter);
        }

        msSql = "SELECT "
                + "v.id_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.drv AS " + SDbConsts.FIELD_NAME + ", "
                + "v.pla, "
                + "v.pla_cag, "
                + "v.qty, "
                + "sc.code, "
                + "se.name, "
                + "re.name, "
                + "it.name, "
                + "it.code, "
                + "pr.name, "
                + "pr.code, "
                + "v.ts_arr, "
                + "v.ts_dep, "
                + "v.pac_wei_arr, "
                + "v.pac_wei_dep, "
                + "v.wei_src, "
                + "v.wei_des_arr, "
                + "v.wei_des_dep, "
                + "v.wei_des_net_r, "
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
                + "vs.name, "
                + "tor.code, "
                + "tde.code, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.b_ass, "
                + "v.fk_item, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "v.ts_usr_ass AS f_ts_usr_ass, "
                + "it.fk_inp_tp, "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "ua.name AS f_usr_ass, "
                + "(SELECT COALESCE(SUM(ge.qty), 0) FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS ge WHERE "
                + "ge.b_del = 0 AND ge.fk_tic_n = v.id_tic AND ge.fk_item = v.fk_item AND ge.fk_unit = v.fk_unit " + sqlDiogPeriod + ") AS f_qty, "
                + "CONCAT(tp.code, '-', g.num) AS f_tp_iog "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sc ON v.fk_sca = sc.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_TIC_ST) + " AS vs ON v.fk_tic_st = vs.id_tic_st "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pr ON v.fk_prod = pr.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS it ON v.fk_item = it.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ua ON v.fk_usr_ass = ua.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_ORIG) + " AS tor ON v.fk_tic_orig = tor.id_tic_orig "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_DEST) + " AS tde ON v.fk_tic_dest = tde.id_tic_dest "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g ON v.id_tic = g.fk_tic_n AND g.b_del = 0 "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CT) + " AS ct ON g.fk_iog_ct = ct.id_iog_ct "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CL) + " AS cl ON g.fk_iog_ct = cl.id_iog_ct AND g.fk_iog_cl = cl.id_iog_cl "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_TP) + " AS tp ON g.fk_iog_ct = tp.id_iog_ct AND g.fk_iog_cl = tp.id_iog_cl AND g.fk_iog_tp = tp.id_iog_tp "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS se ON v.fk_seas_n = se.id_seas "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS re ON v.fk_reg_n = re.id_reg "
                + "WHERE v.b_tar = 1 AND v.fk_tic_st = " + SModSysConsts.SS_TIC_ST_ADM  + " " + (sql.isEmpty() ? "" : sql) + " "
                + "GROUP BY v.id_tic "
                + "HAVING " + (SModConsts.SX_TIC_MAN_SUP == mnGridSubtype ? "(v.b_ass = 0 AND (f_qty = 0 OR v.qty <> f_qty))" : "(v.qty = f_qty) OR v.b_ass = 1") + " "
                + "ORDER BY sc.code, v.num, v.dt, v.id_tic ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = null;

        columns = new SGridColumnView[mnGridSubtype == SModConsts.SX_TIC_ASSO ? 32 : 30];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sc.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "pr.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "pr.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "it.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "it.code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.wei_src", "Carga origen (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.wei_des_arr", "Peso entrada (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.wei_des_dep", "Peso salida (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.qty", "Peso carga destino neto (" + SSomConsts.KG + ")");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "", "Cant pendiente");
        columns[col].getRpnArguments().add(new SLibRpnArgument("v.qty", SLibRpnArgumentType.OPERAND));
        columns[col].getRpnArguments().add(new SLibRpnArgument("f_qty", SLibRpnArgumentType.OPERAND));
        columns[col++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_qty", "Cant surtida");
        if (mnGridSubtype == SModConsts.SX_TIC_ASSO) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_tp_iog", "Folio docto inv");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_ass", "Cerrado manual");
        }
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_arr", "TS Entrada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_dep", "TS Salida");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "se.name", "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "re.name", "Región");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla", "Placas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla_cag", "Placas caja");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SDbConsts.FIELD_NAME, "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tor.code", "Procedencia boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tde.code", "Destino boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "f_usr_ass", "Usr surt");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "f_ts_usr_ass", "Usr TS surt");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.S_TIC);
        moSuscriptionsSet.add(SModConsts.S_LAB);
        moSuscriptionsSet.add(SModConsts.S_IOG);
        moSuscriptionsSet.add(SModConsts.SX_TIC_SUP_RM);
        moSuscriptionsSet.add(SModConsts.SX_TIC_TARE);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionMouseClicked() {
        switch (mnGridSubtype) {
            case SModConsts.SX_TIC_MAN_SUP:

                break;
            case SModConsts.SX_TIC_ASSO:

                break;
            default:
                super.actionMouseClicked();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbTicketSupply) {
                actionTicketSupply();
            }
            else if (button == jbTicketClose) {
                actionTicketCloseOpen(true);
            }
            else if (button == jbTicketOpen) {
                actionTicketCloseOpen(false);
            }
        }
    }
}
