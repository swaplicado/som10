/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDate;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.bean.SBeanFieldDatetime;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SCfgUtils;
import som.mod.mat.db.SDbStock;
import som.mod.mat.form.SDialogMovementCardex;
import som.mod.mat.form.SDialogNoteCardex;

/**
 *
 * @author Isabel Servín
 */
public class SViewStock extends SGridPaneView implements ActionListener {

    private JButton jbMovInRep;
    private JButton jbMovInProd;
    private JButton jbMovInAdj;
    private JButton jbMovConv;
    private JButton jbMovOutProd;
    private JButton jbMovOutRec;
    private JButton jbMovOutAdj;
    private JButton jbMvtCardex;
    private JButton jbNoteCardex;
    private JButton jbEndOfYear;
    private JButton jbPrintBallot;
    
    private SGridFilterDate moFilterDate;
    private SBeanFieldDatetime moDateTime;
    private JLabel moLabelCutoff;
    private JLabel moLabelDate;
    
    private SDialogMovementCardex moMvtCardex;
    private SDialogNoteCardex moNoteCardex;
    
    public SViewStock(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.M_STK, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).removeAll();
        jbMovInRep = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_adj_in.gif")), "Entrada de almacén por recepción", this);
        jbMovInProd = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_inv_in.gif")), "Entrada de almacén por producción", this);
        jbMovInAdj = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mfg_rm_asd.gif")), "Entrada de almacén por ajuste", this);
        jbMovOutRec = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_adj_out.gif")), "Salida de almacén por recepción", this);
        jbMovOutProd = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_inv_out.gif")), "Salida de almacén por producción", this);
        jbMovOutAdj = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mfg_rm_ret.gif")), "Salida de almacén por ajuste", this);
        jbMovConv = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_cnv.gif")), "Actualización de estado de MP", this);
        jbMvtCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_kardex.gif")), "Ver cárdex de movimientos", this);
        jbNoteCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_notes.gif")), "Ver comentarios", this);
        jbPrintBallot = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_print.gif")), "Imprimir papeleta de almacén de MP", this);
        jbEndOfYear = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_cal_date_year.gif")), "Último día del año", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovInRep);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovInProd);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovInAdj);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovOutRec);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovOutProd);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovOutAdj);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMovConv);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMvtCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbNoteCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintBallot);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        
        moLabelCutoff = new JLabel("Fecha corte:");
        moFilterDate = new SGridFilterDate(miClient, this);
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, SLibTimeUtils.getEndOfYear(miClient.getSession().getSystemDate()).getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moLabelCutoff);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbEndOfYear);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        moDateTime = new SBeanFieldDatetime();
        moDateTime.setEnabled(false);
        moLabelDate = new JLabel("Fecha-hr actual:");
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moLabelDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moDateTime);
        
        moMvtCardex = null; 
        moNoteCardex = null; 
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
    
    private void actionMovOutProd() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                Object filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
                SGuiParams params = new SGuiParams();
                SDbStock stk = new SDbStock();
                stk.read(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                params.getParamsMap().put(SModConsts.MS_MVT_CL, SModSysConsts.MS_MVT_CL_OUT_PRD);
                params.getParamsMap().put(SModConsts.M_STK, stk);
                params.getParamsMap().put(SGridConsts.FILTER_DATE, filter);
                miClient.getSession().getModule(SModConsts.MOD_SOM_RM).showForm(SModConsts.M_MVT, SLibConsts.UNDEFINED, params);            
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionMovOutRec() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                Object filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
                SGuiParams params = new SGuiParams();
                SDbStock stk = new SDbStock();
                stk.read(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                params.getParamsMap().put(SModConsts.MS_MVT_CL, SModSysConsts.MS_MVT_CL_OUT_REC);
                params.getParamsMap().put(SModConsts.M_STK, stk);
                params.getParamsMap().put(SGridConsts.FILTER_DATE, filter);
                miClient.getSession().getModule(SModConsts.MOD_SOM_RM).showForm(SModConsts.M_MVT, SLibConsts.UNDEFINED, params);        
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionMovOutAdj() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                Object filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
                SGuiParams params = new SGuiParams();
                SDbStock stk = new SDbStock();
                stk.read(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                params.getParamsMap().put(SModConsts.MS_MVT_CL, SModSysConsts.MS_MVT_CL_OUT_ADJ);
                params.getParamsMap().put(SModConsts.M_STK, stk);
                params.getParamsMap().put(SGridConsts.FILTER_DATE, filter);
                miClient.getSession().getModule(SModConsts.MOD_SOM_RM).showForm(SModConsts.M_MVT, SLibConsts.UNDEFINED, params);  
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionMovConv() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                Object filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
                SGuiParams params = new SGuiParams();
                SDbStock stk = new SDbStock();
                stk.read(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                params.getParamsMap().put(SModConsts.MS_MVT_CL, SModSysConsts.MS_MVT_CL_IN_CNV);
                params.getParamsMap().put(SModConsts.M_STK, stk);
                params.getParamsMap().put(SGridConsts.FILTER_DATE, filter);
                miClient.getSession().getModule(SModConsts.MOD_SOM_RM).showForm(SModConsts.M_MVT, SLibConsts.UNDEFINED, params);   
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionMvtCardex() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                if (moMvtCardex == null) {
                    moMvtCardex = new SDialogMovementCardex(miClient, "Cárdex de movimientos de almacén");
                }
                SGuiParams params = new SGuiParams();
                SDbStock stk = new SDbStock();
                stk.read(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                params.getParamsMap().put(SModConsts.M_STK, stk);
                params.getParamsMap().put(SModConsts.S_TIC, stk.getFkTicketId_n());
                moMvtCardex.setValue(SDialogMovementCardex.GUI_PARAMS, params);
                moMvtCardex.setVisible(true);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionNoteCardex() {
        try {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                if (moNoteCardex == null) {
                    moNoteCardex = new SDialogNoteCardex(miClient, "Comentarios del boleto");
                }
                SGuiParams params = new SGuiParams();
                SDbStock stk = new SDbStock();
                stk.read(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                params.getParamsMap().put(SModConsts.S_TIC, stk.getFkTicketId_n());
                moNoteCardex.setValue(SDialogNoteCardex.GUI_PARAMS, params);
                moNoteCardex.setVisible(true);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionPrintBallot() {
        if (jbPrintBallot.isEnabled()) {
            try {
                if (jtTable.getSelectedRowCount() != 1) {
                    miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
                }
                else {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        SDbStock stk = new SDbStock();
                        stk.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                        HashMap<String, Object> map = SPrtUtils.createReportParamsMap(miClient.getSession());
                        
                        map.put("nTicket", stk.getFkTicketId_n());
                        map.put("sReference", stk.getReference());
                        
                        miClient.getSession().printReport(SModConsts.M_STK, SLibConsts.UNDEFINED, null, map);
                    }
                }
            }
            catch (Exception e) {
                miClient.showMsgBoxError(e.getMessage());
            }
        }
    }
    
    private void actionEndOfYear() {
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, SLibTimeUtils.getEndOfYear(miClient.getSession().getSystemDate()).getTime()));
        refreshGridWithRefresh();
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        String indicator[] = new String[3];
        
        moDateTime.setValue(new Date());
        
        moPaneSettings = new SGridPaneSettings(5);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
        if (filter != null) {
            where += (where.length() == 0 ? "" : "AND ") + "a.id_year = " + SLibTimeUtils.digestYear((SGuiDate) filter)[0] + " AND "
                    + " a.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
        }
        else {
            where += (where.length() == 0 ? "" : "AND ")  + "a.id_year = " + SLibTimeUtils.digestYear(miClient.getSession().getWorkingDate())[0] + " AND "
                    + " a.dt <= '" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate())) + "' ";
        }
        
        try {
            indicator = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SModSysConsts.C_PARAM_MAT_STK_DAYS_SIGNAL).split(";");
        }
        catch (Exception e) {}
        String green = indicator[0].replaceAll("G", "dias_stk");
        String yellow = indicator[1].replaceAll("Y", "dias_stk");
        String red = indicator[2].replaceAll("R", "dias_stk");
        
        msSql = "SELECT a.*, " 
                + "id_year AS " + SDbConsts.FIELD_ID + "1, " 
                + "id_wah AS " + SDbConsts.FIELD_ID + "2, " 
                + "id_item AS " + SDbConsts.FIELD_ID + "3, " 
                + "id_unit AS " + SDbConsts.FIELD_ID + "4, " 
                + "id_stk AS " + SDbConsts.FIELD_ID + "5, " 
                + "id_stk AS " + SDbConsts.FIELD_CODE + ", " 
                + "id_stk AS " + SDbConsts.FIELD_NAME + ", " 
                + "CONCAT(dt_rec, ':00') AS dth_rec, " 
                + "IF('" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' < DATE(NOW()), NULL, "
                + "IF(" + green + ", " + SGridConsts.ICON_CIRC_GREEN + ", IF(" + yellow + " AND " + red + ", " + SGridConsts.ICON_CIRC_RED + ", " + SGridConsts.ICON_CIRC_YELLOW + "))) AS icon " 
                + "FROM ( " 
                + "SELECT " 
                + "s.*, "
                + "s.ref AS dt_rec, " 
                + "sca.code AS sca, "
                + "t.num AS boleto, " 
                + "p.name AS prov, " 
                + "p.name_trd AS prov_com, " 
                + "i.name AS item, " 
                + "mc.name AS mat_cond, " 
                + "SUM(qty_in) - SUM(qty_out) AS stk, " 
                + "u.name AS unidad, " 
                + "IF('" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' < DATE(NOW()), NULL, TIMESTAMPDIFF(MINUTE, s.ref, NOW()) / 60 / 24) AS dias_stk " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.M_STK) + " AS s " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " 
                + "s.id_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON "
                + "s.id_unit = u.id_unit " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_MAT_COND) + " AS mc ON "
                + "s.fk_mat_cond = mc.id_mat_cond " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t ON "
                + "s.fk_tic_n = t.id_tic "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sca ON "
                + "t.fk_sca = sca.id_sca "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON " 
                + "t.fk_prod = p.id_prod " 
                + "WHERE NOT s.b_del "
                + "GROUP BY s.id_year, s.id_wah, s.id_item, s.id_unit, s.ref, s.fk_mat_cond, s.fk_tic_n "
                + "HAVING (SUM(qty_in) - SUM(qty_out)) <> 0) AS a " 
                + (where.isEmpty() ? "" : "WHERE " + where) 
                + "ORDER BY dt_rec, sca, boleto, prov, item, mat_cond";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[11];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "dth_rec", "Referencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "boleto", "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "prov", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "prov_com", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "item", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "mat_cond", "Estado MP");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "stk", "Existencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "unidad", "Unidad", 50);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "dias_stk", "Días en almacén");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON_CIRC, "icon", "Indicador días en almacén", 30);
        
        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.M_MVT);
        moSuscriptionsSet.add(SModConsts.S_TIC);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
        moSuscriptionsSet.add(SModConsts.MU_MAT_COND);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbMvtCardex) {
                actionMvtCardex();
            }
            else if (button == jbNoteCardex) {
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
            else if (button == jbMovConv) {
                actionMovConv();
            }
            else if (button == jbMovOutProd) {
                actionMovOutProd();
            }
            else if (button == jbMovOutRec) {
                actionMovOutRec();
            }
            else if (button == jbMovOutAdj) {
                actionMovOutAdj();
            }
            else if (button == jbPrintBallot) {
                actionPrintBallot();
            }
            else if (button == jbEndOfYear) {
                actionEndOfYear();
            }
        }
    }
    
    @Override
    public void actionMouseClicked() {
        actionMvtCardex();
    }
}
