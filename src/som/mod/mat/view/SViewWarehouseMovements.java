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
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.mat.db.SDbStockMovement;
import som.mod.mat.db.SMaterialUtils;
import som.mod.mat.form.SDialogNoteCardex;

/**
 *
 * @author Isabel Servín
 */
public class SViewWarehouseMovements extends SGridPaneView implements ActionListener {

    private final boolean mbIsDetail;
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    
    private JButton jbNoteCardex;
    private JButton jbMovInRep;
    private JButton jbMovInProd;
    private JButton jbMovInAdj;
    
    private SDialogNoteCardex moNoteCardex;
    
    private boolean mbUserHasAdmRig;
    
    public SViewWarehouseMovements(SGuiClient client, int subType, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.M_MVT, subType, title, params);
        setRowButtonsEnabled(false);
        mbIsDetail = getGridMode() == SModSysConsts.MX_MVT_DETAIL;
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        mbUserHasAdmRig = miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_RMES, SModSysConsts.CS_RIG_RMEA });
        
        jbMovInRep = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_adj_in.gif")), "Entrada de almacén por recepción", this);
        jbMovInProd = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_inv_in.gif")), "Entrada de almacén por producción", this);
        jbMovInAdj = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mfg_rm_asd.gif")), "Entrada de almacén por ajuste", this);
        jbNoteCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_notes.gif")), "Ver comentarios", this);
        
        if (!mbIsDetail) {
            jbRowDelete.setEnabled(mbUserHasAdmRig);
            if (mnGridSubtype == SModSysConsts.SS_IOG_CT_IN) {
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovInRep);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovInProd);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovInAdj);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
            }
        }
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbNoteCardex);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        moNoteCardex = null; 
    }
    
    private void actionNoteCardex() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                if (moNoteCardex == null) {
                    moNoteCardex = new SDialogNoteCardex(miClient, "Comentarios del movimiento de almacén");
                }
                SGuiParams params = new SGuiParams();
                SDbStockMovement stkMvt = new SDbStockMovement();
                stkMvt.read(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                params.getParamsMap().put(SModConsts.M_MVT, stkMvt.getPkStockMovementId());
                params.getParamsMap().put(SModConsts.S_TIC, stkMvt.getFkTicketId_n());
                moNoteCardex.setValue(SDialogNoteCardex.GUI_PARAMS, params);
                moNoteCardex.setVisible(true);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionMovInRep() {
        try {
            SGuiParams params = new SGuiParams();
            params.getParamsMap().put(SModConsts.MS_MVT_CL, SModSysConsts.MS_MVT_CL_IN_REC);
            miClient.getSession().getModule(SModConsts.MOD_SOM_RM).showForm(SModConsts.M_MVT, SLibConsts.UNDEFINED, params);            
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionMovInProd() {
        try {
            SGuiParams params = new SGuiParams();
            params.getParamsMap().put(SModConsts.MS_MVT_CL, SModSysConsts.MS_MVT_CL_IN_PRD);
            miClient.getSession().getModule(SModConsts.MOD_SOM_RM).showForm(SModConsts.M_MVT, SLibConsts.UNDEFINED, params);            
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionMovInAdj() {
        try {
            SGuiParams params = new SGuiParams();
            params.getParamsMap().put(SModConsts.MS_MVT_CL, SModSysConsts.MS_MVT_CL_IN_ADJ);
            miClient.getSession().getModule(SModConsts.MOD_SOM_RM).showForm(SModConsts.M_MVT, SLibConsts.UNDEFINED, params);            
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }

    @Override
    public void prepareSqlQuery() {
        String where;
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        if (mnGridSubtype == SModSysConsts.SS_IOG_CT_IN) {
            where = "fk_iog_ct = " + SModSysConsts.SS_IOG_CT_IN + " ";
        }
        else {
            where = "fk_iog_ct = " + SModSysConsts.SS_IOG_CT_OUT + " ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            where += "AND " + SGridUtils.getSqlFilterDate("m.dt", (SGuiDate) filter) + " ";
        }
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "m.b_del = 0 ";
        }
        
        msSql = "SELECT " 
                + "m.id_mvt AS " + SDbConsts.FIELD_ID + "1, " 
                + "mc.name AS " + SDbConsts.FIELD_NAME + ", " 
                + "CONCAT(m.ser, '-', LPAD(m.num, 6, '0')) AS " + SDbConsts.FIELD_CODE + ", "
                + "m.dt AS dt, "
                + "CONCAT(m.ref, ':00') AS ref, "
                + "sm.code AS sca_mov, "
                + "tm.num AS bol_mov, "
                + "pm.name AS prov_mov, "
                + "pm.name_trd AS prov_com, "
                + (mbIsDetail ? "sme.code AS sca_ety, "
                + "tme.num AS bol_ety, "
                + "pme.name AS prov_ety, "
                + "pme.name_trd AS prov_com_ety, "
                + "c.name AS mat_cond, "
                + "me.qty, "
                + "i.name AS item, "
                + "u.name AS unidad, " : "")
                + "s.name AS turno, "
                + "ewm.name AS encargado, " 
                + "ems.name AS super_pr, "
                + "m.b_del, "
                + "m.b_sys, "
                + "m.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "m.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.M_MVT) + " AS m "
                + (mbIsDetail ? "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.M_MVT_ETY) + " AS me ON "
                + "m.id_mvt = me.id_mvt " : "")
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MS_MVT_CL) + " AS mc ON "
                + "m.fk_iog_ct = mc.id_iog_ct AND m.fk_mvt_cl = mc.id_mvt_cl "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_SHIFT) + " AS s ON "
                + "m.fk_shift = s.id_shift "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EMP) + " AS ewm "
                + "ON m.fk_emp_wah_man = ewm.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EMP) + " AS ems ON "
                + "m.fk_emp_mfg_sup = ems.id_emp "
                + (mbIsDetail ? "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "me.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON "
                + "me.fk_unit = u.id_unit "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_MAT_COND) + " AS c ON "
                + "me.fk_mat_cond = c.id_mat_cond " : "")
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS tm ON "
                + "m.fk_tic_n = tm.id_tic "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sm ON "
                + "tm.fk_sca = sm.id_sca "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pm ON "
                + "tm.fk_prod = pm.id_prod "
                + (mbIsDetail ? "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS tme ON "
                + "me.fk_tic_n = tme.id_tic "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sme ON "
                + "tme.fk_sca = sme.id_sca "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pme ON "
                + "tme.fk_prod = pme.id_prod " : "")
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "m.fk_usr_ins = ui.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "m.fk_usr_upd = uu.id_usr "
                + "WHERE " + where + " "
                + "ORDER BY m.dt, mc.name, m.ser, m.num, m.ref, tm.num, pm.name"
                + (mbIsDetail ? ", mat_cond" : "");   
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns;
        if (!mbIsDetail) {
            columns = new SGridColumnView[17];
        }
        else {
            columns = new SGridColumnView[21];
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", SGridConsts.COL_TITLE_DATE + " movimiento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, SDbConsts.FIELD_NAME, "Clase movimiento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_CODE, "Folio movimiento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ref", "Referencia");
        if (!mbIsDetail) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca_mov", "Báscula");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bol_mov", "Boleto", 75);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "prov_mov", "Proveedor");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "prov_com", "Proveedor nombre comercial");
        }
        else {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca_ety", "Báscula");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bol_ety", "Boleto", 75);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "prov_ety", "Proveedor");            
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "prov_com_ety", "Proveedor nombre comercial");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "item", "Ítem");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "mat_cond", "Estado MP");            
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "qty", "Cantidad");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "unidad", "Unidad", 50);
        }
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "turno", "Turno almacén");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "encargado", "Responsable almacén");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "super_pr", "Supervisor producción");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_sys", "Sistema");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_del", "Eliminado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        
        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.M_MVT_ETY);
        moSuscriptionsSet.add(SModConsts.MS_MVT_CL);
        moSuscriptionsSet.add(SModConsts.MU_SHIFT);
        moSuscriptionsSet.add(SModConsts.MU_EMP);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.S_TIC);
    }
    
    
    @Override
    public void actionRowDelete() {
        if (jbRowDelete.isEnabled()) {
            try {
                if (jtTable.getSelectedRowCount() == 0) {
                    miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
                }
                else if (miClient.showMsgBoxConfirm(SGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                    boolean act = true;
                    boolean updates = false;
                    SGridRow gridRow = getSelectedGridRow();
                    SDbStockMovement mvt = new SDbStockMovement();
                    mvt.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                    if (SLibTimeUtils.digestYear(mvt.getDate())[0] != miClient.getSession().getSystemYear()) {
                        if (SMaterialUtils.getYearHasStkTransfer(miClient.getSession(), miClient.getSession().getSystemYear())) {
                            act = miClient.showMsgBoxConfirm("Ya hay un inventario inicial para el año " + miClient.getSession().getSystemYear() + ".\n"
                                    + "Si elimina movimientos hará que el inventario inicial de dicho año ya no coincida con el corte del " + SLibTimeUtils.digestYear(mvt.getDate())[0] + ".\n"
                                    + "¿Desea continuar?") == JOptionPane.OK_OPTION;
                        }
                    }
                    if (act) {
                        if (mvt.isSystem()) {
                            miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                        }
                        else {
                            mvt.setDeleted(true);
                            if (mvt.canDelete(miClient.getSession())) {
                                mvt.delete(miClient.getSession());
                                updates = true;
                            }
                        }

                        if (updates) {
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                }
            }
            catch(Exception e) {
                miClient.showMsgBoxError(e.getMessage());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbNoteCardex) {
                actionNoteCardex();
            }
            else if (button == jbMovInRep) {
                actionMovInRep();
            }
            else if (button == jbMovInProd) {
                actionMovInProd();
            }
            else if (button == jbMovInAdj) {
                actionMovInAdj();
            }
        }
    }
}
