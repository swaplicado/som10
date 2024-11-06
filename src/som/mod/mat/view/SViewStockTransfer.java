/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.view;

import java.util.Arrays;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.mat.db.SMaterialUtils;

/**
 *
 * @author Isabel Servín
 */
public class SViewStockTransfer extends SGridPaneView {
    
    private boolean mbUserHasAdmRig;

    public SViewStockTransfer(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.MX_STK_TRANS, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(true, false, false, false, false);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        mbUserHasAdmRig = miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_RMES, SModSysConsts.CS_RIG_RMEA });
        jbRowNew.setEnabled(mbUserHasAdmRig);
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "m.b_del = 0 ";
        }
        
        msSql = "SELECT "
                + "m.id_mvt AS " + SDbConsts.FIELD_ID + "1, "
                + "mc.name AS " + SDbConsts.FIELD_NAME + ", "
                + "CONCAT(m.ser, '-', LPAD(m.num, 6, '0')) AS " + SDbConsts.FIELD_CODE + ", "
                + "YEAR(m.dt) AS year, "
                + "m.dt, "
                + "CONCAT(m.ref, ':00') AS ref, "
                + "m.b_del, " 
                + "m.b_sys, "
                + "IF((SELECT MAX(mvt.ts_usr_ins) FROM m_mvt AS mvt WHERE YEAR(mvt.dt) = YEAR(m.dt) -1) > m.ts_usr_ins, " + SGridConsts.ICON_WARN + ", " + SGridConsts.ICON_NULL + ") AS icon, "
                + "m.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "m.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.M_MVT) + " AS m "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MS_MVT_CL) + " AS mc ON " 
                + "m.fk_iog_ct = mc.id_iog_ct AND m.fk_mvt_cl = mc.id_mvt_cl " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "m.fk_usr_ins = ui.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "m.fk_usr_upd = uu.id_usr "
                + "WHERE m.fk_iog_ct = " + SModSysConsts.MS_MVT_CL_IN_INV[0] + " "
                + "AND m.fk_mvt_cl = " + SModSysConsts.MS_MVT_CL_IN_INV[1] + " "
                + (where.isEmpty() ? "" : "AND ") + where + " "
                + "ORDER BY m.dt, mc.name, m.ser, m.num, m.ref;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[12];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "year", "Año");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, SDbConsts.FIELD_NAME, "Clase movimiento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_CODE, "Folio movimiento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "ref", SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ref", "Referencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_sys", "Sistema");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_del", "Eliminado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "icon", "Cambios año procedencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        
        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.M_MVT);
        moSuscriptionsSet.add(SModConsts.M_STK);
        moSuscriptionsSet.add(SModConsts.MS_MVT_CL);
    }
    
    @Override
    public void actionRowNew() {
        if (jbRowNew.isEnabled()) {
            try {
                int lastYear = miClient.getSession().getSystemYear() -1;
                int year = miClient.getSession().getSystemYear();
                if (miClient.showMsgBoxConfirm("Está a punto de hacer una entrada de inventario inicial del año " + 
                        lastYear + " al año " + year + ".\nSi ya hay inventario inicial en el año actual esté se eliminará y se generará uno nuevo.\n" +
                        "¿Desea continuar?") == JOptionPane.OK_OPTION) {
                    SMaterialUtils.realizeTransferStock(miClient.getSession(), lastYear, year);
                    this.refreshGridWithRefresh();
                }
            }
            catch (Exception e) {
                miClient.showMsgBoxError(e.getMessage());
            }
        }
    }
}
