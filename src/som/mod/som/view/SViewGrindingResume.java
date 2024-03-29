/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextField;
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
import som.mod.SModConsts;
import som.mod.cfg.db.SDbBranchPlant;
import som.mod.som.db.SDbGrindingLinkItemParameter;
import som.mod.som.db.SDbGrindingResult;
import som.mod.som.form.SFormGrindingResultHr;

/**
 *
 * @author Edwin Carmona
 */
public class SViewGrindingResume extends SGridPaneView implements ActionListener {
   
    SGuiClient miClient;
    private SGridFilterDatePeriod moFilterDatePeriod;
    
    private int[] maPlantKey;
    
    private JTextField jtPlant;

    public SViewGrindingResume(SGuiClient client, final int subType, final int[] plaKey, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_GRINDING_RESUME, subType, title);
        this.miClient = client;
        this.maPlantKey = plaKey;
        
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        jtPlant = new JTextField();
        jtPlant.setPreferredSize(new java.awt.Dimension(200, 23));
        jtPlant.setEditable(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jtPlant);
        
        try {
            SDbBranchPlant oPlant = new SDbBranchPlant();
            oPlant.read(miClient.getSession(), maPlantKey);
            
            jtPlant.setText(oPlant.getCode() + " - " + oPlant.getName());
        }
        catch (Exception ex) {
            Logger.getLogger(SViewGrindingResume.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;
        
        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowDisable.setEnabled(false);

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_capture", (SGuiDate) filter);

        msSql = "SELECT " +
                "v.id_result AS " + SDbConsts.FIELD_ID + "1, " +
                "v.dt_capture AS " + SDbConsts.FIELD_DATE + ", " +
                "i.code AS " + SDbConsts.FIELD_CODE + ", " +
                "i.name AS " + SDbConsts.FIELD_NAME + ", " +
                "u.code, " +
                "l.prc_batch " +
                "FROM " +
                "    " + SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT) + " AS v " +
                "        INNER JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON v.fk_item_id = i.id_item " +
                "        INNER JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON i.fk_unit = u.id_unit " +
                "        INNER JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.S_PRC_BATCH) + " AS l ON v.fk_prc_batch = l.id_prc_batch " +
                "WHERE " + 
                sql +
                (sql.isEmpty() ? "" : " AND ") + "v.fk_pla_co = " + maPlantKey[0] + " AND v.fk_pla_cob = " + maPlantKey[1] + " AND v.fk_pla_pla = " + maPlantKey[2] + " " +
                "GROUP BY v.dt_capture , v.fk_item_id , v.fk_prc_batch;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[5];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha Captura");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.code", "Un.");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "l.prc_batch", "Lote");

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
                
                form = new SFormGrindingResultHr(miClient, "Captura Resultados Molienda");
                
                dbResult.setDateCapture(new Date());
                dbResult.setFkItemId(dbLink.getFkItemId());
                dbResult.setFkParameterId(dbLink.getFkParameterId());
                dbResult.setOrder(dbLink.getCaptureOrder());
                
                form.setRegistry(dbResult);
                form.setVisible(true);
                
                dbResult = (SDbGrindingResult) form.getRegistry();
                dbResult.save(miClient.getSession());
            }
            catch (Exception ex) {
                Logger.getLogger(SViewGrindingResume.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void actionRowNew() {
        
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
        }
    }
}
