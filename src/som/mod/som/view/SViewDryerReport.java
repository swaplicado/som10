/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import som.mod.SModConsts;
import som.mod.som.db.SSomDryerReport;
import som.mod.som.form.SDialogReportMails;

/**
 *
 * @author Isabel Servín
 */
public class SViewDryerReport extends SGridPaneView implements ActionListener {
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton mjSendReport;

    public SViewDryerReport(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_DRYER_REP, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        mjSendReport = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_mail.gif")), "Enviar reporte del secador", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendReport);
    }
    
    private void actionSendReport() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            
            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                SDialogReportMails testMails = new SDialogReportMails(miClient, SModConsts.S_DRYER_REP);
                testMails.setVisible(true); 
                if (testMails.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                    SSomDryerReport report = new SSomDryerReport(miClient);
                    report.sendReport(gridRow.getRowPrimaryKey(), testMails.getMails());
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
        }

        msSql = "SELECT "
                + "v.id_dryer_rep AS " + SDbConsts.FIELD_ID + "1, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt, "
                + "v.operative_hrs, "
                + "v.pellet_gross_wei, "
                + "v.pellet_broken_wei, "
                + "v.pellet_net_wei_r, "
                + "v.pellet_to_ext, "
                + "v.pellet_oil, "
                + "v.pellet_to_waste, "
                + "v.seed_peel_proc, "
                + "v.seed_peel_to_proc, "
                + "v.seed_peel_oil, "
                + "v.seed_peel_to_plant, "
                + "v.seed_peel_to_terrain, "
                + "v.bagasse_to_plant, "
                + "v.bagasse_to_terrain, "
                + "v.pit_measure_emp, "
                + "v.pit_proc_fruit, "
                + "v.pit_cont_emp, "
                + "v.pit_cont_grin, "
                + "v.pit_cont_eff, "
                + "v.goal_moi_proc, "
                + "v.goal_pellet_proc_per_hr, "
                + "v.goal_avg_effcy, "
                + "v.goal_tot_prod, "
                + "v.goal_pellet_oil, "
                + "v.goal_avg_bag_oil, "
                + "v.lab_avo_aci_n, "
                + "v.lab_avg_bag_oil_n, "
                + "v.lab_moi_n, "
                + "v.lab_pellet_oil_n, "
                + "v.lab_pellet_aci_n, "
                + "v.ss_effcy, "
                + "v.ss_c_monoxide, "
                + "IF(v.b_production, false, true) AS production, "
                + "v.b_pit_cont_emp, "
                + "v.b_pit_cont_grin, "
                + "v.b_pit_cont_eff, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_DRYER_REP) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.dt, v.id_dryer_rep ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[42];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "production", "Sin producción");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "operative_hrs", "Horas operativas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pellet_gross_wei", "Peso pellet bruto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pellet_broken_wei", "Peso pellet quebrado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pellet_net_wei_r", "Peso pellet neto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pellet_to_ext", "Pellet a extracción");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pellet_oil", "Aceite de pellet");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pellet_to_waste", "Pellet a desecho");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "seed_peel_proc", "HYC procesado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "seed_peel_to_proc", "HYC en planta x procesar");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "seed_peel_oil", "Aceite de HYC");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "seed_peel_to_plant", "HYC a planta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "seed_peel_to_terrain", "HYC a terreno");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "bagasse_to_plant", "Bagazo externo a planta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "bagasse_to_terrain", "Bagazo externo a terreno");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "pit_measure_emp", "Medida de vacío");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pit_proc_fruit", "Fruta procesada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_pit_cont_emp", "Contenido fosa vacío");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_pit_cont_grin", "Contenido fosa molienda");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_pit_cont_eff", "Contenido fosa efectivo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pit_cont_emp", "Contenido fosa vacío");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pit_cont_grin", "Contenido fosa molienda");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pit_cont_eff", "Contenido fosa efectivo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "goal_moi_proc", "Húmedo procesado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "goal_pellet_proc_per_hr", "Producción pellet x hr");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "goal_avg_effcy", "Eficiencia promedio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "goal_tot_prod", "Producción total");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "goal_pellet_oil", "Aceite en pellet");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "goal_avg_bag_oil", "Aceite promedio bagazo B/S");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "lab_avo_aci_n", "Acidez planta aguacate");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "lab_avg_bag_oil_n", "Aceite promedio bagazo B/S lab");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "lab_moi_n", "Humedad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "lab_pellet_oil_n", "Aceite en pellet lab");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "lab_pellet_aci_n", "Acidez en pelletizadora");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "ss_effcy", "Eficiencia chimenea");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "ss_c_monoxide", "Monoxido de carbono chimenea");
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
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == mjSendReport) {
                actionSendReport();
            }
        }
    }
}
