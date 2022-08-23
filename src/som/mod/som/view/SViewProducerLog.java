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
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import som.mod.SModConsts;
import som.mod.som.db.SDbInputSource;
import som.mod.som.db.SDbProducer;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Isabel Servín
 */
public class SViewProducerLog extends SGridPaneView implements ActionListener {
    
    private final JButton jbUpdateTicketsInputSourse;

    public SViewProducerLog(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_PROD_LOG, SLibConsts.UNDEFINED, title);
        
        jbUpdateTicketsInputSourse = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_wizard.gif")), "Actualizar origen de insumo de los boletos del proveedor", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbUpdateTicketsInputSourse);
    }
    
    private void actionUpdateTicketsInputSource() {
        SDbProducer producer;
        SDbInputSource inputSource;
        
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            try {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                producer = (SDbProducer) miClient.getSession().readRegistry(SModConsts.SU_PROD, gridRow.getRowPrimaryKey());
                inputSource = (SDbInputSource) miClient.getSession().readRegistry(SModConsts.SU_INP_SRC, new int[] { producer.getFkInputSourceId() });
                
                if (miClient.showMsgBoxConfirm("¿Está seguro(a) que desea actualizar el origen de insumo de todos los boletos de \"" + producer.getName() + "\"\n"
                        + "a \"" + inputSource.getName() + "\" ?") == JOptionPane.YES_OPTION){
                    SSomUtils.updateTicketsInputSource(miClient.getSession(), producer.getPkProducerId(), inputSource.getPkInputSourceId());                    
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(true);
        moPaneSettings.setDisableableApplying(true);
        moPaneSettings.setDeletableApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 AND v.b_log ";
        }

        msSql = "SELECT "
                + "v.id_prod AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.name_trd, "
                + "v.name_log, "
                + "v.fis_id, "
                + "v.rev_prod_id, "
                + "v.street_num, "
                + "v.neighborhood, "
                + "v.locality, "
                + "v.county, "
                + "v.state, "
                + "v.zip_code, "
                + "v.phone, "
                + "v.license_semaccdet, "
                + "v.license_sct, "
                + "v.record_env, "
                + "v.contact_name, "
                + "v.contact_charge, "
                + "v.amn_box, "
                + "v.b_amn, "
                + "v.b_fre_pay, "
                + "v.b_log, "
                + "v.b_can_upd AS " + SDbConsts.FIELD_CAN_UPD + ", "
                + "v.b_can_dis AS " + SDbConsts.FIELD_CAN_DIS + ", "
                + "v.b_can_del AS " + SDbConsts.FIELD_CAN_DEL + ", "
                + "v.b_dis AS " + SDbConsts.FIELD_IS_DIS + ", "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "r.name, "
                + "v.fk_rep_grp, "
                + "s.name, "
                + "CONCAT(vc.vehicle_type, ' ', vc.container_type) AS veh_cont_type, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_REP_GRP) + " AS r ON "
                + "v.fk_rep_grp = r.id_rep_grp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_SRC) + " AS s ON "
                + "v.fk_inp_src = s.id_inp_src "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.SU_VEH_CONT_TYPE) + " AS vc ON " 
                + "v.fk_veh_cont_type_n = vc.id_veh_cont_type "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.name, v.id_prod ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[31];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "v.name_trd", "Nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "v.name_log", "Nombre logística");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.fis_id", "RFC");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.rev_prod_id", "Clave Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.amn_box", "Correo electrónico para notificaciones automáticas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_amn", "Notificación automática");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "v.street_num", "Calle y número");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.neighborhood", "Colonia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.locality", "Ciudad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.county", "Municipio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.state", "Estado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.phone", "Télefono");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.zip_code", "Código postal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "v.contact_name", "Responsable");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "v.contact_charge", "Cargo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.license_semaccdet", "Registro SEMACCDET");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.record_env", "Registro ambiental");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.license_sct", "Retgistro S.C.T.");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "veh_cont_type", "Vehículo contenedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "r.name", "Agrupador reporte");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "s.name", "Origen insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_fre_pay", "Se paga flete");
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
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbUpdateTicketsInputSourse) {
                actionUpdateTicketsInputSource();
            }
        }
    }
}
