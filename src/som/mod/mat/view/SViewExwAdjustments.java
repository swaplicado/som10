/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.view;

import java.util.Arrays;
import javax.swing.JOptionPane;
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
import sa.lib.gui.SGuiParams;
import som.mod.SModConsts;
import som.mod.mat.db.SDbExwAdjustment;

/**
 *
 * @author Sergio Flores
 */
public class SViewExwAdjustments extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    
    public SViewExwAdjustments(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.M_EXW_ADJ, 0, title);
        initCustomComponents();
    }
    
    private void initCustomComponents() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        setRowButtonsEnabled(false, true, false, false, true);
    }

    @Override
    public void actionRowEdit() {
        if (jbRowEdit.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                SDbExwAdjustment exwAdjustment = (SDbExwAdjustment) miClient.getSession().readRegistry(SModConsts.M_EXW_ADJ, gridRow.getRowPrimaryKey());
                
                moFormParams = new SGuiParams(exwAdjustment.getFkIogCategoryId());
                super.actionRowEdit();
            }
        }
    }

    @Override
    public void actionRowDelete() {
        if (miClient.showMsgBoxConfirm("La eliminación de los ajustes de existencias de almacenes externos no se pueden revertir.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
            super.actionRowDelete();
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String sqlWhere = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "NOT a.b_del ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("a.dt", (SGuiDate) filter);
        }

        msSql = "SELECT "
                + "a.id_exw_adj AS " + SDbConsts.FIELD_ID + "1, "
                + "CONCAT(a.ser, IF(a.ser = '', '', '-'), LPAD(a.num, " + SDbExwAdjustment.NUM_LEN + ", 0)) AS " + SDbConsts.FIELD_CODE + ", "
                + "CONCAT(a.ser, IF(a.ser = '', '', '-'), LPAD(a.num, " + SDbExwAdjustment.NUM_LEN + ", 0)) AS " + SDbConsts.FIELD_NAME + ", "
                + "a.dt, "
                + "a.ref, "
                + "a.note, "
                + "a.qty, "
                + "ict.name, "
                + "ict.code, "
                + "eat.name, "
                + "eat.code, "
                + "ef.name, "
                + "ef.code, "
                + "s.name, "
                + "s.code, "
                + "i.name, "
                + "i.code, "
                + "u.code, "
                + "a.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "a.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "a.b_aut, "
                + "a.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "a.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "a.fk_usr_aut, "
                + "a.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "a.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "a.ts_usr_aut, "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "ua.name "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.M_EXW_ADJ) + " AS a "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CT) + " AS ict ON "
                + "a.fk_iog_ct = ict.id_iog_ct "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EXW_ADJ_TP) + " AS eat ON "
                + "a.fk_exw_adj_tp = eat.id_exw_adj_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EXW_FAC) + " AS ef ON "
                + "a.fk_exw_fac = ef.id_exw_fac "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS s ON "
                + "a.fk_sca = s.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "a.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON "
                + "a.fk_item = u.id_unit "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "a.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "a.fk_usr_upd = uu.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ua ON "
                + "a.fk_usr_aut = ua.id_usr "
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + "ORDER BY s.name, s.code, s.id_sca, ict.name, ict.id_iog_ct, a.ser, a.num, a.dt, a.id_exw_adj ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[23];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "s.name", "Báscula", 125);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "s.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ict.name", "Categoría movimiento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_CODE, "Folio ajuste");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "a.dt", "Fecha ajuste");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ef.name", "Almacén externo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ef.code", "Almacén externo código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "a.qty", "Cantidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.code", "Unidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "eat.name", "Tipo ajuste");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "a.ref", "Referencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "a.note", "Notas", 150);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "a.b_aut", "Autorizado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "ua.name", "Usuario autorización");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "a.ts_usr_aut", "TS autorización");
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
        moSuscriptionsSet.add(SModConsts.MU_EXW_ADJ_TP);
        moSuscriptionsSet.add(SModConsts.MU_EXW_FAC);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
