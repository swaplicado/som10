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
import som.mod.SModConsts;
import som.mod.som.form.SDialogEstOilPercentage;

/**
 *
 * @author Edwin Carmona
 */
public class SViewMfgEstimationRm extends SGridPaneView implements ActionListener {
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton moButtonChangePercentage;

    public SViewMfgEstimationRm(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_MFG_EST_RM_CON, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        moButtonChangePercentage = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_lab.gif")), "% Aceite", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonChangePercentage);
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
        
        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "me.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("me.dt_mfg_est", (SGuiDate) filter);

        msSql = "SELECT "
            + "me.id_mfg_est AS " + SDbConsts.FIELD_ID + "1, "
            + "merc.id_rm_con AS " + SDbConsts.FIELD_ID + "2, "
            + "merc.fk_item_con_rm AS " + SDbConsts.FIELD_ID + "3, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "'' AS " + SDbConsts.FIELD_NAME + ", "
            + "me.ver, "
            + "me.dt_mfg_est, "
            + "me.dt_stk_day, "
            + "me.mfg_fg_r, "
            + "(merc.oil_per * 100) AS oil_percentage, "
            + "(si.mfg_fg_per * 100) AS fg_percentage , "
            + "si.name, "
            + "merc.con_rm, "
            + "su.code, "
            + "me.b_clo, "
            + "me.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
            + "me.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
            + "me.fk_unit, "
            + "me.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "me.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "me.fk_usr_clo, "
            + "me.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "me.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "me.ts_usr_clo, "
            + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "    
            + "uc.name AS f_usr_clo "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST) + " AS me "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST_RM_CON) + " AS merc ON me.id_mfg_est = merc.id_mfg_est "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS si ON merc.fk_item_con_rm = si.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS su ON si.fk_unit = su.id_unit "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON me.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON me.fk_usr_upd = uu.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uc ON me.fk_usr_clo = uc.id_usr "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "ORDER BY me.dt_mfg_est, me.id_mfg_est ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[17];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "me.dt_mfg_est", SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "me.dt_stk_day", SGridConsts.COL_TITLE_DATE);
        //columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.ver", SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "me.mfg_fg_r", "Cantidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "si.name", "Ítem MP");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "merc.con_rm", "Semilla");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "su.code", "Un.");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "oil_percentage", "% aceite manual");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "fg_percentage", "% aceite conf");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "me.b_clo", "Cerrado");        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "f_usr_clo", SGridConsts.COL_TITLE_USER_USR_NAME + " cerrado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "me.ts_usr_clo", SGridConsts.COL_TITLE_USER_USR_TS + " cerrado");

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

            if (button == moButtonChangePercentage && moButtonChangePercentage.isEnabled()) {
                int[] pk = super.getSelectedGridRow().getRowPrimaryKey();
                SDialogEstOilPercentage dialog = new SDialogEstOilPercentage(miClient);
                dialog.formReset();
                dialog.setFormParams(pk[0], pk[1], pk[2], (Double) super.getSelectedGridRow().getRowValueAt(6));
                dialog.setVisible(true);
                
                miClient.getSession().notifySuscriptors(mnGridType);
                miClient.getSession().notifySuscriptors(SModConsts.S_MFG_EST_ETY);
                miClient.getSession().notifySuscriptors(SModConsts.S_MFG_EST);
            }
        }
    }
}
