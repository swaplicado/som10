/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
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
import som.mod.SModConsts;
import som.mod.cfg.db.SDbLinkItemParameter;
import som.mod.som.db.SDbGrindingResult;
import som.mod.som.db.SDbLot;
import som.mod.som.data.SGrindingData;
import som.mod.som.data.SGrindingReport;
import som.mod.som.data.SGrindingResultsUtils;
import som.mod.som.form.SDialogEvents;
import som.mod.som.form.SDialogGrindingData;
import som.mod.som.form.SFormGrindingResultHr;
import som.mod.som.form.SFormResultNew;

/**
 *
 * @author Edwin Carmona
 */
public class SViewGrindingResults extends SGridPaneView implements ActionListener {
    
    private SGridFilterDate moFilterDate;
    private SPaneFilter moItemFilter;
    
    private SDialogEvents moDialogEvents;
    private SDialogGrindingData moDialogData;
    private JTextField jtLot;
    private JButton jbShowEvents;
    private JButton jbShowData;
    private JButton jbFirst;
    private JButton jbPrev;
    private JButton jbNext;
    private JButton jbLast;
    private JButton jbMail;
    
    SGuiClient miClient;
    private Date mtDate;
    private int mnLot;
    private double mdGrindingOil;

    public SViewGrindingResults(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SU_GRINDING_RESULTS, SLibConsts.UNDEFINED, title);
        this.miClient = client;
        
        moFilterDate = new SGridFilterDate(miClient, this);
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, miClient.getSession().getWorkingDate().getTime()));
        
        moItemFilter = new SPaneFilter(this, SModConsts.SS_LINK_CFG_ITEMS);
        int item = SGrindingResultsUtils.getLastConfiguration(client, SGrindingResultsUtils.ITEM);
        moItemFilter.initFilter(new int[] { item });
        
        mnLot = SGrindingResultsUtils.getLastLotByItem(client, item);
       
        jtLot = new JTextField();
        jtLot.setPreferredSize(new java.awt.Dimension(100, 23));
        jtLot.setEditable(false);
        
        jbShowEvents = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_tar_not.gif")), "Ver eventos", this);
        jbShowData = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_notes.gif")), "Ver datos", this);
        jbFirst = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_up.gif")), "Ver primero", this);
        jbPrev = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_left.gif")), "Ver anterior", this);
        jbNext = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_right.gif")), "Ver siguiente", this);
        jbLast = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_down.gif")), "Ver último", this);
        jbMail = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mail.gif")), "Enviar", this);
        
        moDialogEvents = new SDialogEvents(miClient);
        moDialogData = new SDialogGrindingData(miClient);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moItemFilter);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jtLot);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbFirst);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrev);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbNext);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLast);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMail);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbShowEvents);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbShowData);
    }
    
    private void actionChangeDate(final int option) {
        Date dt = SGrindingResultsUtils.getDateOfResults(miClient, option, mtDate, ((int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS)) [0], mnLot);
        
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, dt.getTime()));
        mtDate = dt;
        miClient.getSession().notifySuscriptors(SModConsts.SU_GRINDING_RESULTS);
    }
    
    private void actionSendMail() {
        SGrindingReport report = new SGrindingReport();
        Date filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
        int [] itemKey = (int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS);
        int idLot = SGrindingResultsUtils.getLastLotByItem(miClient, itemKey[0]);
        
        try {
            report.processReport(miClient, filter, itemKey[0], idLot);
        }
        catch (IOException ex) {
            Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actionShowEvents() {
        moDialogEvents.formReset();
        moDialogEvents.setFormParams(mtDate);
        moDialogEvents.setVisible(true);
    }
    
    private void actionShowData() {
        moDialogData.formReset();
        moDialogData.setFormParams(mtDate, ((int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS)) [0], mnLot);
        moDialogData.setVisible(true);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String hav = "";
        Object filter = null;
        
        jbRowCopy.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowDisable.setEnabled(false);

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(true);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") +  "v.dt_capture = '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
        }
        else {
            sql += (sql.length() == 0 ? "" : "AND ")  + " v.dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(miClient.getSession().getWorkingDate()) + "' ";
        }
        mtDate = filter != null ? (SGuiDate) filter : miClient.getSession().getWorkingDate();
        
        filter = (int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS);
        if (filter != null) {
            try {
                sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "lip.fk_item_id" }, (int[]) filter);
                
                mnLot = SGrindingResultsUtils.getLastLotByItem(miClient, ((int[]) filter)[0]);
                
                SDbLot oLot = new SDbLot();
                oLot.read(miClient.getSession(), new int [] { mnLot });
                
                jtLot.setText(oLot.getLot());
                
                sql += " AND v.fk_lot_id = " + mnLot + " ";
            }
            catch (Exception ex) {
                Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ArrayList<SGrindingData> grinding = SGrindingResultsUtils.getGrindingByMonth(miClient, mtDate, ((int[]) filter)[0], true);
        if (! grinding.isEmpty()) {
            SGrindingData oGrinding = SGrindingResultsUtils.getGrindingByMonth(miClient, mtDate, ((int[]) filter)[0], true).get(0);
            if (oGrinding != null) {
                mdGrindingOil = oGrinding.getSumGrindingOilPercent();
            }
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            hav += "HAVING b_deleted = 0 ";
        }

        msSql = "SELECT "
                + "v.id_result AS " + SDbConsts.FIELD_ID + "1, "
                + "lip.fk_parameter_id AS " + SDbConsts.FIELD_ID + "2, "
                + "gp.param_code AS " + SDbConsts.FIELD_CODE + ", "
                + "gp.parameter AS " + SDbConsts.FIELD_NAME + ", "
                + "COALESCE(v.dt_capture, '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "') AS " + SDbConsts.FIELD_DATE + ", "
                + "lip.fk_parameter_id AS " + SDbConsts.FIELD_COMP + "1, "
                + "gp.b_text, "
                + "gp.def_text_value, "
                + "v.capture_order AS order_res, "
                + "lip.capture_order AS order_link, "
                + "i.code AS item_code, "
                + "i.name AS item_name, "
                + "gp.details, "
                + " (@dg := (SELECT " +
                    "  grinding_oil_perc " +
                    "FROM " +
                    " su_grinding " +
                    "WHERE " +
                    " fk_item_id = lip.fk_item_id " +
                    " AND fk_lot_id = v.fk_lot_id " +
                    " AND NOT b_del " +
                    " AND dt_capture = v.dt_capture)) AS day_grinding,"
                + "v.result_08, "
                + "v.result_10, "
                + "v.result_12, "
                + "v.result_14, "
                + "v.result_16, "
                + "v.result_18, "
                + "v.result_20, "
                + "v.result_22, "
                + "v.result_00, "
                + "v.result_02, "
                + "v.result_04, "
                + "v.result_06,"
                + "(@prom := (COALESCE(v.result_08, 0) + " +
                    "COALESCE(v.result_10, 0) + " +
                    "COALESCE(v.result_12, 0) + " +
                    "COALESCE(v.result_14, 0) + " +
                    "COALESCE(v.result_16, 0) + " +
                    "COALESCE(v.result_18, 0) + " +
                    "COALESCE(v.result_20, 0) + " +
                    "COALESCE(v.result_22, 0) + " +
                    "COALESCE(v.result_00, 0) + " +
                    "COALESCE(v.result_02, 0) + " +
                    "COALESCE(v.result_04, 0) + " +
                    "COALESCE(v.result_06, 0)) / "
                + "(IF(v.result_08 > 0, 1, 0) + " +
                "    IF(v.result_10 > 0, 1, 0) + " +
                "    IF(v.result_12 > 0, 1, 0) + " +
                "    IF(v.result_14 > 0, 1, 0) + " +
                "    IF(v.result_16 > 0, 1, 0) + " +
                "    IF(v.result_18 > 0, 1, 0) + " +
                "    IF(v.result_20 > 0, 1, 0) + " +
                "    IF(v.result_22 > 0, 1, 0) + " +
                "    IF(v.result_00 > 0, 1, 0) + " +
                "    IF(v.result_02 > 0, 1, 0) + " +
                "    IF(v.result_04 > 0, 1, 0) + " +
                "    IF(v.result_06 > 0, 1, 0))) AS promedio, "
                + "(@dg / " + mdGrindingOil + " * @prom) AS pond, "
                + "COALESCE(v.b_del, 0) AS b_deleted, "
                + "COALESCE(v.b_can_upd, 1) AS " + SDbConsts.FIELD_CAN_UPD + ", "
                + "v.b_can_dis AS " + SDbConsts.FIELD_CAN_DIS + ", "
                + "v.b_can_del AS " + SDbConsts.FIELD_CAN_DEL + ", "
                + "v.b_dis AS " + SDbConsts.FIELD_IS_DIS + ", "
                + "COALESCE(v.b_del, 0) AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "lip.id_link AS " + SDbConsts.FIELD_USER_UPD_ID + ", "  // POR AHORA
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "'' AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "'' AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_RESULTS) + " AS v "
                + "RIGHT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_LINK_ITEM_PARAM) + " AS lip "
                + "ON (lip.fk_parameter_id = v.fk_parameter_id AND lip.fk_item_id = v.fk_item_id) AND NOT lip.b_del "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_PARAMS) + " AS gp ON "
                + "gp.id_parameter = lip.fk_parameter_id AND NOT gp.b_del "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "lip.fk_item_id = i.id_item "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + (hav.isEmpty() ? "" : hav)
                + "ORDER BY v.capture_order ASC, lip.capture_order ASC ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[19];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "order_link", "Orden captura");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha captura");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "item_name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Parámetro");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_08", "08:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_10", "10:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_12", "12:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_14", "14:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_16", "16:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_18", "18:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_20", "20:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_22", "22:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_00", "00:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_02", "02:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_04", "04:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.result_06", "06:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "promedio", "Promedio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "pond", "Ponderado");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }
    
    @Override
    public void actionRowEdit() {
        SFormGrindingResultHr form;
        
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            int[] pk = gridRow.getRowPrimaryKey();
            
            if (pk[0] > 0) {
                super.actionRowEdit();
                return;
            }
            
            SDbLinkItemParameter dbLink = new SDbLinkItemParameter();
            SDbGrindingResult dbResult = new SDbGrindingResult();
            try {
                
                dbLink.read(miClient.getSession(),  new int[] { gridRow.getFkUserUpdateId() }); // id de Link
                
                form = new SFormGrindingResultHr(miClient, "Captura Resultados Molienda");
                
                dbResult.setDateCapture(mtDate);
                dbResult.setFkItemId(dbLink.getFkItemId());
                dbResult.setFkParameterId(dbLink.getFkParameterId());
                dbResult.setOrder(dbLink.getOrder());
                
                form.setRegistry(dbResult);
                form.setVisible(true);
                
                dbResult = (SDbGrindingResult) form.getRegistry();
//                dbResult.save(miClient.getSession());
            }
            catch (Exception ex) {
                Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    @Override
    public void actionRowNew() {
        SFormResultNew form;
        
        SDbGrindingResult dbResult = new SDbGrindingResult();
                
        form = new SFormResultNew(miClient, "Captura Resultados Molienda");
        
        dbResult.setDateCapture(mtDate);
        
        int item = 0;
        int[] filter = (int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS);
        if (filter != null) {
            item = filter[0];
        }
        else {
            item = SGrindingResultsUtils.getLastConfiguration(miClient, SGrindingResultsUtils.ITEM);
        }
        
        dbResult.setFkItemId(item);

        try {
            form.setRegistry(dbResult);
            form.setVisible(true);
            
            if (form.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                dbResult = (SDbGrindingResult) form.getRegistry();
                SGrindingResultsUtils.generateResults(miClient, dbResult.getFkItemId() == 0, 
                                                        dbResult.getFkItemId(), 
                                                        dbResult.getFkLotId(), 
                                                        dbResult.getDateCapture());

                mnLot = dbResult.getFkLotId();
                miClient.getSession().notifySuscriptors(SModConsts.SU_GRINDING_RESULTS);
            }
            
        }
        catch (Exception ex) {
            Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbShowEvents) {
                actionShowEvents();
            }
            else if (button == jbShowData) {
                actionShowData();
            }
            else if (button == jbFirst) {
                actionChangeDate(SGrindingResultsUtils.FIRST);
            }
            else if (button == jbPrev) {
                actionChangeDate(SGrindingResultsUtils.PREV);
            }
            else if (button == jbNext) {
                actionChangeDate(SGrindingResultsUtils.NEXT);
            }
            else if (button == jbLast) {
                actionChangeDate(SGrindingResultsUtils.LAST);
            }
            else if (button == jbMail) {
                actionSendMail();
            }
        }
    }
}
