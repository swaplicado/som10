/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextField;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDate;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbBranchPlant;
import som.mod.som.data.SGrindingResultsUtils;
import som.mod.som.db.SDbGrindingEvent;
import som.mod.som.form.SFormGrindingEvent;

/**
 *
 * @author Edwin Carmona
 */
public class SViewGrindingEvents extends SGridPaneView implements ActionListener {
    
    private SGridFilterDate moFilterDate;
    private SPaneFilter moItemFilter;
    
    private int[] maPlantKey;
    
    private JTextField jtPlant;
    
    SGuiClient miClient;

    public SViewGrindingEvents(SGuiClient client, final int subType, final int[] plaKey, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_GRINDING_EVENT, subType, title);
        this.miClient = client;
        maPlantKey = plaKey;
        
        moFilterDate = new SGridFilterDate(miClient, this);
        LocalDate workingDate = miClient.getSession().getWorkingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date firstDayOfMonth = Date.from(workingDate.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, firstDayOfMonth.getTime()));
        
        moItemFilter = new SPaneFilter(this, SModConsts.SS_LINK_CFG_ITEMS);
        int item = SGrindingResultsUtils.getLastConfiguration(client, SGrindingResultsUtils.ITEM, maPlantKey);
        moItemFilter.initFilter(new int[] { item });
        
        jtPlant = new JTextField();
        jtPlant.setPreferredSize(new java.awt.Dimension(200, 23));
        jtPlant.setEditable(false);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moItemFilter);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jtPlant);
        
        try {
            SDbBranchPlant oPlant = new SDbBranchPlant();
            oPlant.read(miClient.getSession(), maPlantKey);
            
            jtPlant.setText(oPlant.getCode() + " - " + oPlant.getName());
        }
        catch (Exception ex) {
            Logger.getLogger(SViewGrindingEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(true);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") +  "v.dt_start >= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
        }
        else {
            sql += (sql.length() == 0 ? "" : "AND ")  + "v.dt_start >= '" + SLibUtils.DbmsDateFormatDate.format(miClient.getSession().getWorkingDate()) + "' ";
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_item_id" }, (int[]) filter);
        }
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ")  + " v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "v.id_event AS " + SDbConsts.FIELD_ID + "1, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt_start, "
                + "v.dt_end, "
                + "v.description,"
                + "CONCAT(i.code, '-', i.name) AS item,"
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_can_upd AS " + SDbConsts.FIELD_CAN_UPD + ", "
                + "v.b_can_dis AS " + SDbConsts.FIELD_CAN_DIS + ", "
                + "v.b_can_del AS " + SDbConsts.FIELD_CAN_DEL + ", "
                + "v.b_dis AS " + SDbConsts.FIELD_IS_DIS + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_GRINDING_EVENT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "v.fk_item_id = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "WHERE v.fk_pla_co = " + maPlantKey[0] + " AND v.fk_pla_cob = " + maPlantKey[1] + " AND v.fk_pla_pla = " + maPlantKey[2] + " "
                + "AND " + sql
                + "ORDER BY v.dt_start DESC, v.id_event ASC ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[11];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "dt_start", "Fecha Inicio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "dt_end", "Fecha Fin");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "description", "Evento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "item", "Ítem afectado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DIS, SGridConsts.COL_TITLE_IS_DIS);
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
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == null) {
                
            }
        }
    }
    
    @Override
    public void actionRowNew() {
        SDbGrindingEvent dbEvent = new SDbGrindingEvent();
        
        String title = "";
        switch(this.mnGridSubtype) {
            case SModSysConsts.CU_PLA_INT_PYE:
                title = "Evento de prensas y ext.";
                break;
                
            case SModSysConsts.CU_PLA_INT_AGU:
                title = "Evento de aguacatera.";
                break;
        }
        
        SFormGrindingEvent form = new SFormGrindingEvent(miClient, title);
        
        int item = 0;
        int[] filter = (int[]) moFiltersMap.get(SModConsts.SS_LINK_CFG_ITEMS);
        if (filter != null) {
            item = filter[0];
        }
        
        dbEvent.setFkItemId(item);
        dbEvent.setFkPlantCompanyId(maPlantKey[0]);
        dbEvent.setFkPlantBranchId(maPlantKey[1]);
        dbEvent.setFkPlantPlantId(maPlantKey[2]);

        try {
            form.setRegistry(dbEvent);
            form.setVisible(true);
            
            if (form.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
//                dbEvent = (SDbGrindingResult) form.getRegistry();
                miClient.getSession().notifySuscriptors(SModConsts.S_GRINDING_EVENT);
            }
        }
        catch (Exception ex) {
            Logger.getLogger(SViewGrindingEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
