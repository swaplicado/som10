/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
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
import sa.lib.gui.SGuiParams;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.mat.db.SDbTicketStatus;
import som.mod.mat.db.SMaterialUtils;
import som.mod.mat.form.SDialogMovementCardex;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Isabel Servín
 */
public class SViewTicketReceptions extends SGridPaneView implements ActionListener {
    private String msMatItems;
    private int mnScaleDefault;
        
    private SGridFilterDatePeriod moFilterDatePeriod;
    
    private JButton jbCardex;
    private JButton jbOpen;
    private JButton jbClose;
    private SDialogMovementCardex moCardex;
    
    public SViewTicketReceptions(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.MX_TIC_REC, subType, title);
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        msMatItems = SMaterialUtils.getParamMaterialItemsIds(miClient.getSession());
        mnScaleDefault = SMaterialUtils.getParamScaleDefault(miClient.getSession());
        
        jbOpen = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_doc_open.gif")), "Abrir para recepción", this);
        jbClose = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_doc_close.gif")), "Cerrar para recepción", this);
        if (mnGridSubtype == SModSysConsts.MX_TIC_W_MVT_REC) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbOpen);
        }
        else {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbClose);
        }
        jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_kardex.gif")), "Ver cárdex de movimientos", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());

        if (mnGridSubtype == SModSysConsts.MX_TIC_W_MVT_REC) {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        
        moCardex = null;
    }
    
    private void actionCardex() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                if (moCardex == null) {
                    moCardex = new SDialogMovementCardex(miClient, "Cárdex de movimientos de almacén");
                }
                SGuiParams params = new SGuiParams();
                params.getParamsMap().put(SModConsts.S_TIC, getSelectedGridRow().getRowPrimaryKey()[0]);
                moCardex.setValue(SDialogMovementCardex.GUI_PARAMS, params);
                moCardex.setVisible(true);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionOpen() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                if (miClient.showMsgBoxConfirm("¿Desea abrir para recepción el boleto seleccionado?") == JOptionPane.OK_OPTION) {
                    String sql = "DELETE FROM m_tic_st WHERE id_tic = " + getSelectedGridRow().getRowPrimaryKey()[0];
                    miClient.getSession().getStatement().execute(sql);
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionClose() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                if (miClient.showMsgBoxConfirm("¿Desea cerrar para recepción el boleto seleccionado?") == JOptionPane.OK_OPTION) {
                    SDbTicketStatus status = SMaterialUtils.getTicketStatusRegistry(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                    if (status == null) {
                        status = new SDbTicketStatus();
                        status.setPkTicketId(getSelectedGridRow().getRowPrimaryKey()[0]);
                        status.setFkTicketStatusId(SModSysConsts.MS_TIC_ST_CLS_REC);
                        status.save(miClient.getSession());
                    }
                    else {
                        status.setDeleted(false);
                        status.setFkTicketStatusId(SModSysConsts.MS_TIC_ST_CLS_REC);
                        status.save(miClient.getSession());
                    }
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        if (mnGridSubtype == SModSysConsts.MX_TIC_WO_MVT_REC) {
            where = "(a.pac_qty_arr = 0 OR a.qty_rec < a.pac_qty_arr) AND a.fk_tic_st <> " + SModSysConsts.MS_TIC_ST_CLS_REC + " ";
        }
        else if (mnGridSubtype == SModSysConsts.MX_TIC_W_MVT_REC) {
            where = "((a.pac_qty_arr <> 0 AND a.qty_rec >= a.pac_qty_arr) ";
            where += "OR a.fk_tic_st = " + SModSysConsts.MS_TIC_ST_CLS_REC + ") ";
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
            if (filter != null) {
                where += "AND " + SGridUtils.getSqlFilterDate("f_date", (SGuiDate) filter) + " ";
            }
        }
        
        msSql = "SELECT * FROM ("
                + "SELECT "
                + "t.id_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "t.num AS " + SDbConsts.FIELD_CODE + ", "
                + "t.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "p.name AS " + SDbConsts.FIELD_NAME + ", "
                + "p.name_trd, "
                + "p.code AS prov_code, "
                + "p.id_prod, "
                + "s.code AS sca, "
                + "t.drv, "
                + "t.b_tar, "
                + "CONCAT(t.pla, IF(t.pla_cag = '', '', ', '), t.pla_cag) AS placas, "
                + "i.name AS item, "
                + "i.code AS item_code, "
                + "t.pac_qty_arr, "
                + "SUM(IF(m.fk_iog_ct = " + SModSysConsts.SS_IOG_CT_IN + ", COALESCE(me.qty, 0), COALESCE(-me.qty, 0))) as qty_rec, "
                + "t.pac_qty_arr - SUM(COALESCE(me.qty, 0)) as qty_res, "
                + "i.paq_name, " 
                + "COALESCE(ts.fk_tic_st, ts.fk_tic_st, 0) fk_tic_st, "
                + "IF(ts.id_tic IS NULL, 0, 1) closed, "
                + "ts.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ts.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON "
                + "t.fk_prod = p.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "t.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS s ON "
                + "t.fk_sca = s.id_sca " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.M_MVT) + " AS m ON "
                + "t.id_tic = m.fk_tic_n AND NOT m.b_del AND "
                + "m.fk_mvt_cl = " + SModSysConsts.MS_MVT_CL_IN_REC[1] + " "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.M_MVT_ETY) + " AS me ON "
                + "m.id_mvt = me.id_mvt "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.M_TIC_ST) + " AS ts ON "
                + "t.id_tic = ts.id_tic "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "ts.fk_usr_ins = ui.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "ts.fk_usr_upd = uu.id_usr "
                + "WHERE NOT t.b_del AND t.fk_item IN(" + msMatItems + ") "
                + (mnScaleDefault == 0 ? "" : "AND s.id_sca = " + mnScaleDefault + " ")
                + "GROUP BY t.num, t.id_tic, t.dt, p.name, p.id_prod, t.drv) AS a "
                + "WHERE " + where + " "
                + "ORDER BY f_code, f_id_1, f_date, f_name, id_prod, drv;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[16];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Boleto", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SDbConsts.FIELD_NAME, "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "name_trd", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "drv", "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "placas", "Placas y placas caja");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_tar", "Tarado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "item", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "pac_qty_arr", "Cant empaq lleno entrada (" + SSomConsts.PIECE + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "qty_rec", "Cant recibida (" + SSomConsts.PIECE + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "qty_res", "Cant pendiente (" + SSomConsts.PIECE + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "paq_name", "Empaque", 50);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "closed", "Cerrado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, "Usr cerrado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, "Usr TS cerrado");
        
        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_TIC);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.M_MVT);
        moSuscriptionsSet.add(SModConsts.M_MVT_ETY);
        moSuscriptionsSet.add(SModConsts.M_TIC_ST);
        moSuscriptionsSet.add(SModConsts.MX_TIC_REC);
        moSuscriptionsSet.add(SModConsts.MX_TIC_MVT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCardex) {
                actionCardex();
            }
            else if (button == jbOpen) {
                actionOpen();
            }
            else if (button == jbClose) {
                actionClose();
            }
        }
    }
    
    @Override
    public void actionMouseClicked() {
        actionCardex();
    }
}
