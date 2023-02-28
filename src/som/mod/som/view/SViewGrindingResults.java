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
import sa.lib.gui.SGuiParams;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbBranchPlant;
import som.mod.som.db.SDbGrindingLinkItemParameter;
import som.mod.som.db.SDbGrindingResult;
import som.mod.som.db.SDbProcessingBatch;
import som.mod.som.data.SGrindingData;
import som.mod.som.data.SGrindingReport;
import som.mod.som.data.SGrindingResultsUtils;
import som.mod.som.form.SDialogEvents;
import som.mod.som.form.SDialogGrindingData;
import som.mod.som.form.SFormGrindingResultHr;
import som.mod.som.form.SFormGrindingResultNew;

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
    private JTextField jtPlant;
    private JButton jbShowEvents;
    private JButton jbShowData;
    private JButton jbFirst;
    private JButton jbPrev;
    private JButton jbNext;
    private JButton jbLast;
    private JButton jbMail;
    private JButton jbFile;
    
    SGuiClient miClient;
    private Date mtDate;
    private int mnLot;
    private double mdGrindingOil;
    private final int[] maPlantKey;

    public SViewGrindingResults(SGuiClient client, final int subtype, final int[] plaKey, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_GRINDING_RESULT, subtype, title);
        this.miClient = client;
        this.maPlantKey = plaKey;
        
        moFilterDate = new SGridFilterDate(miClient, this);
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, miClient.getSession().getWorkingDate().getTime()));
        
        SGuiParams params = new SGuiParams();
        params.getParamsMap().clear();
        params.setKey(null);
        params.getParamsMap().put(SModConsts.SX_EXT_PLA, maPlantKey);
        moItemFilter = new SPaneFilter(this, SModConsts.SS_LINK_CFG_ITEMS, params);
        int item = SGrindingResultsUtils.getLastConfiguration(client, SGrindingResultsUtils.ITEM, maPlantKey);
        moItemFilter.initFilter(new int[] { item });
        
        mnLot = SGrindingResultsUtils.getLastLotByItem(client, item, maPlantKey);
       
        jtLot = new JTextField();
        jtLot.setPreferredSize(new java.awt.Dimension(100, 23));
        jtLot.setEditable(false);
        
        jtPlant = new JTextField();
        jtPlant.setPreferredSize(new java.awt.Dimension(200, 23));
        jtPlant.setEditable(false);
        
        jbShowEvents = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_tar_not.gif")), "Ver eventos", this);
        jbShowData = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_notes.gif")), "Ver datos", this);
        jbFirst = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_up.gif")), "Ver primero", this);
        jbPrev = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_left.gif")), "Ver anterior", this);
        jbNext = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_right.gif")), "Ver siguiente", this);
        jbLast = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_down.gif")), "Ver último", this);
        jbMail = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mail.gif")), "Enviar", this);
        jbFile = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_save.gif")), "Descargar reporte", this);
        
        moDialogEvents = new SDialogEvents(miClient);
        moDialogData = new SDialogGrindingData(miClient);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moItemFilter);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jtLot);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jtPlant);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbFirst);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrev);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbNext);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLast);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbMail);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbFile);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbShowEvents);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbShowData);
    }
    
    private void actionChangeDate(final int option) {
        Date dt = SGrindingResultsUtils.getDateOfResults(miClient, option, mtDate, ((int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS)) [0], mnLot);
        
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, dt.getTime()));
        mtDate = dt;
        miClient.getSession().notifySuscriptors(SModConsts.S_GRINDING_RESULT);
    }
    
    private void actionSendMail() {
        SGrindingReport report = new SGrindingReport();
        Date filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
        int [] itemKey = (int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS);
        int idLot = SGrindingResultsUtils.getLastLotByItem(miClient, itemKey[0], maPlantKey);
        
        try {
            report.processReport(miClient, filter, itemKey[0], idLot, SGrindingReport.SEND_REPORT, maPlantKey);
        }
        catch (IOException ex) {
            Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actionSaveReport() {
        SGrindingReport report = new SGrindingReport();
        Date filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
        int [] itemKey = (int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS);
        int idLot = SGrindingResultsUtils.getLastLotByItem(miClient, itemKey[0], maPlantKey);
        
        try {
            report.processReport(miClient, filter, itemKey[0], idLot, SGrindingReport.SAVE_REPORT, maPlantKey);
        }
        catch (IOException ex) {
            Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actionShowEvents() {
        moDialogEvents.formReset();
        moDialogEvents.setFormParams(mtDate, ((int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS)) [0], maPlantKey);
        moDialogEvents.setVisible(true);
    }
    
    private void actionShowData() {
        moDialogData.formReset();
        moDialogData.setFormParams(mtDate, ((int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS)) [0], mnLot, maPlantKey);
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
        
        try {
            SDbBranchPlant oPlant = new SDbBranchPlant();
            oPlant.read(miClient.getSession(), maPlantKey);
            
            jtPlant.setText(oPlant.getCode() + " - " + oPlant.getName());
        }
        catch (Exception ex) {
            Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
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
                sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_item_id" }, (int[]) filter);
                
                mnLot = SGrindingResultsUtils.getLastLotByItem(miClient, ((int[]) filter)[0], maPlantKey);
                
                SDbProcessingBatch oLot = new SDbProcessingBatch();
                oLot.read(miClient.getSession(), new int [] { mnLot });
                
                jtLot.setText(oLot.getProcessingBatch());
                
                sql += " AND v.fk_prc_batch = " + mnLot + " ";
            }
            catch (Exception ex) {
                Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ArrayList<SGrindingData> grinding = SGrindingResultsUtils.getGrindingByMonth(miClient, mtDate, ((int[]) filter)[0], true, maPlantKey);
        if (! grinding.isEmpty()) {
            SGrindingData oGrinding = SGrindingResultsUtils.getGrindingByMonth(miClient, mtDate, ((int[]) filter)[0], true, maPlantKey).get(0);
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
                + "v.fk_param_id AS " + SDbConsts.FIELD_ID + "2, "
                + "gp.param_code AS " + SDbConsts.FIELD_CODE + ", "
                + "gp.parameter AS " + SDbConsts.FIELD_NAME + ", "
                + "COALESCE(v.dt_capture, '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "') AS " + SDbConsts.FIELD_DATE + ", "
                + "v.fk_param_id AS " + SDbConsts.FIELD_COMP + "1, "
                + "gp.b_text, "
                + "gp.def_text_value, "
                + "v.capture_order AS order_res, "
                + "v.capture_order AS order_link, "
                + "i.code AS item_code, "
                + "i.name AS item_name, "
                + "gp.details, "
                + " (@dg := (SELECT " +
                    "  grinding_oil_perc " +
                    "FROM " +
                    " " + SModConsts.TablesMap.get(SModConsts.S_GRINDING) + " " +
                    "WHERE " +
                    " fk_item_id = v.fk_item_id " +
                    " AND fk_prc_batch = v.fk_prc_batch " +
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
                + "(@prom := (IF(COALESCE(v.result_08, 0) > 0, v.result_08, 0) + " +
                    "IF(COALESCE(v.result_10, 0) > 0, v.result_10, 0) + " +
                    "IF(COALESCE(v.result_12, 0) > 0, v.result_12, 0) + " +
                    "IF(COALESCE(v.result_14, 0) > 0, v.result_14, 0) + " +
                    "IF(COALESCE(v.result_16, 0) > 0, v.result_16, 0) + " +
                    "IF(COALESCE(v.result_18, 0) > 0, v.result_18, 0) + " +
                    "IF(COALESCE(v.result_20, 0) > 0, v.result_20, 0) + " +
                    "IF(COALESCE(v.result_22, 0) > 0, v.result_22, 0) + " +
                    "IF(COALESCE(v.result_00, 0) > 0, v.result_00, 0) + " +
                    "IF(COALESCE(v.result_02, 0) > 0, v.result_02, 0) + " +
                    "IF(COALESCE(v.result_04, 0) > 0, v.result_04, 0) + " +
                    "IF(COALESCE(v.result_06, 0) > 0, v.result_06, 0)) / "
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
                + "v.fk_link_id_n AS " + SDbConsts.FIELD_USER_UPD_ID + ", "  // POR AHORA
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "'' AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "'' AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_PARAM) + " AS gp ON "
                + "gp.id_parameter = v.fk_param_id AND NOT gp.b_del "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "v.fk_item_id = i.id_item "
                + "WHERE v.fk_pla_co = " + maPlantKey[0] + " AND v.fk_pla_cob = " + maPlantKey[1] + " AND v.fk_pla_pla = " + maPlantKey[2] + " "
                + "AND " + sql
                + (hav.isEmpty() ? "" : hav)
                + "ORDER BY v.capture_order ASC";
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
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_08", "08:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_10", "10:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_12", "12:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_14", "14:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_16", "16:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_18", "18:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_20", "20:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_22", "22:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_00", "00:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_02", "02:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_04", "04:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "v.result_06", "06:00");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "promedio", "Promedio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pond", "Ponderado");

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
            
            SDbGrindingLinkItemParameter dbLink = new SDbGrindingLinkItemParameter();
            SDbGrindingResult dbResult = new SDbGrindingResult();
            try {
                
                dbLink.read(miClient.getSession(),  new int[] { gridRow.getFkUserUpdateId() }); // id de Link
                
                String title = "";
                switch (this.mnGridSubtype) {
                    case SModSysConsts.CU_PLA_INT_PYE:
                        title = "Captura resultados Molienda";
                        break;
                    case SModSysConsts.CU_PLA_INT_AGU:
                        title = "Captura resultados Aguacatera";
                        break;
                }
                
                form = new SFormGrindingResultHr(miClient, title);
                
                dbResult.setDateCapture(mtDate);
                dbResult.setFkItemId(dbLink.getFkItemId());
                dbResult.setFkParameterId(dbLink.getFkParameterId());
                dbResult.setFkPlantCompanyId(maPlantKey[0]);
                dbResult.setFkPlantBranchId(maPlantKey[1]);
                dbResult.setFkPlantPlantId(maPlantKey[2]);
                dbResult.setOrder(dbLink.getCaptureOrder());
                
                form.setRegistry(dbResult);
                form.setVisible(true);
            }
            catch (Exception ex) {
                Logger.getLogger(SViewGrindingResults.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    @Override
    public void actionRowNew() {
        String title = "";
        switch (this.mnGridSubtype) {
            case SModSysConsts.CU_PLA_INT_PYE:
                title = "Captura resultados Molienda";
                break;
            case SModSysConsts.CU_PLA_INT_AGU:
                title = "Captura resultados Aguacatera";
                break;
        }
        SFormGrindingResultNew form = new SFormGrindingResultNew(miClient, title);
        
        SDbGrindingResult dbResult = new SDbGrindingResult();
        dbResult.setDateCapture(mtDate);
        
        int item = 0;
        int[] filter = (int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS);
        if (filter != null) {
            item = filter[0];
        }
        else {
            item = SGrindingResultsUtils.getLastConfiguration(miClient, SGrindingResultsUtils.ITEM, maPlantKey);
        }
        
        dbResult.setFkItemId(item);
        dbResult.setFkPlantCompanyId(maPlantKey[0]);
        dbResult.setFkPlantBranchId(maPlantKey[1]);
        dbResult.setFkPlantPlantId(maPlantKey[2]);

        try {
            form.setRegistry(dbResult);
            form.setVisible(true);
            
            if (form.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                dbResult = (SDbGrindingResult) form.getRegistry();
                SGrindingResultsUtils.generateResults(miClient, dbResult.getFkItemId() == 0, 
                                                        dbResult.getFkItemId(), 
                                                        dbResult.getFkLotId(), 
                                                        dbResult.getDateCapture(),
                                                        maPlantKey);

                mnLot = dbResult.getFkLotId();
                miClient.getSession().notifySuscriptors(SModConsts.S_GRINDING_RESULT);
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
            else if (button == jbFile) {
                actionSaveReport();
            }
        }
    }
}
