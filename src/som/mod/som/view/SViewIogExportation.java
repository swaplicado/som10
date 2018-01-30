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
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import som.mod.SModConsts;
import som.mod.som.db.SDbIogExportation;
import som.mod.som.form.SFormDialogIogExportation;
import som.mod.som.form.SFormDialogWizardDps;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewIogExportation extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    
    private JButton moButtonIogExportation;
    
    public SViewIogExportation(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_IOG_EXP, SLibConsts.UNDEFINED, title);
        initComponetsCustom();
    }

    /*
    * Private methods
    */
    
    private void initComponetsCustom() {
        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
                
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        moButtonIogExportation = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_doc_xml_sign.gif")), "Exportar documentos", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonIogExportation);
    }

    private void actionIogExportation() {
        SDbIogExportation iogExportation = new SDbIogExportation();        
        SFormDialogIogExportation iogExportationForm = new SFormDialogIogExportation(miClient, "Exportación de documentos");                
        
        try {
            iogExportationForm.setRegistry((SDbRegistry) iogExportation);
            iogExportationForm.setVisible(true);
             miClient.getSession().notifySuscriptors(mnGridType);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    /*
    * Public methods
    */
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "g.b_del = 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("g.dt", (SGuiDate) filter);
        
        try {
            // Initialize variable '@res' for function 's_val_exp(date)':
            
            miClient.getSession().getStatement().executeQuery("SET @res = 0;");
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        msSql = "SELECT "
            + "v.id_iog_exp AS " + SDbConsts.FIELD_ID + "1, "
            + "v.dt, "
            + "g.dt, "
            + "v.b_del AS  " + SDbConsts.FIELD_IS_DEL + ", "

            // + "IF (v.dt IS NULL, 'Pendiente', "
            // + "IF(v.ts_usr_upd >= MAX(g.ts_usr_upd), 'Ok', 'Inconsistente')) AS f_res, "

            /*+ "IF (v.dt IS NULL, 'Pendiente', "
            + "IF(@res = null OR @res = 0 OR @res = 1, "
            + "IF (s_val_exp(g.dt) = 1, "
            + "'Ok', 'Not Ok'), "
            + "'Not OOOk')) AS f_res, "*/

            + "IF (v.dt IS NULL, 'PENDIENTE', "
            + "IF(@res = 2, 'INCONSISTENTE', "
            + "IF (s_val_exp(g.dt) = 1, "
            + "'OK', 'INCONSISTENTE'))) AS f_res, "

            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "'' AS " + SDbConsts.FIELD_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g "
            + "LEFT OUTER JOIN " +  SModConsts.TablesMap.get(SModConsts.S_IOG_EXP) + " AS v ON g.dt = v.dt AND v.b_del = 0 "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "GROUP BY g.dt "
            + "ORDER BY g.dt DESC ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[7];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "g.dt", SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_res", "Estado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_IOG);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == moButtonIogExportation) {
                actionIogExportation();
            }            
        }
    }
}
