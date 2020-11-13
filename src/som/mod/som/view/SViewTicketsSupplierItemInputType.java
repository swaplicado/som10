/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.util.Arrays;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SViewTicketsSupplierItemInputType extends SGridPaneView {
    
    private Date[] matPeriod;
    private SGridFilterDateRange moFilterDateRange;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;
    
    public SViewTicketsSupplierItemInputType(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_TIC_MAN_SUP_INP_TP, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, true, false, false, false);
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "i");
        matPeriod = null;
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] { SLibTimeUtils.getBeginOfYear(miClient.getSession().getWorkingDate()), SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()) });
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sqlAux = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(5);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "t.b_del = 0 ";
        }

        matPeriod = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE);
        sqlAux += (!sqlAux.isEmpty() ? "" : " AND ") + SGridUtils.getSqlFilterDateRange("t.dt", matPeriod);
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if(!sqlFilter.isEmpty()) {
            sqlAux += (sqlAux.isEmpty() ? "" : "AND ") + sqlFilter;
        }
        
        msSql = "SELECT "
                + "i.fk_inp_ct AS " + SDbConsts.FIELD_ID + "1, "
                + "i.fk_inp_cl AS " + SDbConsts.FIELD_ID + "2, "
                + "i.fk_inp_tp AS " + SDbConsts.FIELD_ID + "3, "
                + "t.fk_prod AS " + SDbConsts.FIELD_ID + "4, "
                + "t.b_mfg_out AS " + SDbConsts.FIELD_ID + "5, "
                + "it.code AS " + SDbConsts.FIELD_CODE + ", "
                + "it.name AS " + SDbConsts.FIELD_NAME + ", "
                + "p.code, "
                + "p.name, "
                + "p.name_trd "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "t.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_TP) + " AS it ON "
                + "i.fk_inp_ct = it.id_inp_ct AND i.fk_inp_cl = it.id_inp_cl AND i.fk_inp_tp = it.id_inp_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON "
                + "t.fk_prod = p.id_prod "
                + "WHERE t.b_del = 0 " + sqlAux + " " 
                + "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " + SDbConsts.FIELD_ID + "3, " + SDbConsts.FIELD_ID + "4, it.code, it.name, p.code, p.name, p.name_trd "
                + "ORDER BY it.name, it.code, " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " + SDbConsts.FIELD_ID + "3, p.code, p.name, " + SDbConsts.FIELD_ID + "4, " + SDbConsts.FIELD_ID + "5 ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns;
        
        columns = new SGridColumnView[6];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_TYPE + " insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_TYPE + " insumo código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "p.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "p.name_trd", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "p.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, SDbConsts.FIELD_ID + "4", "Es maquila");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_TIC);
        moSuscriptionsSet.add(SModConsts.SU_INP_TP);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.SU_SEAS);
    }
    
    @Override
    public void actionRowEdit() {
        if (jbRowEdit.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                SGuiParams guiParams;
                
                guiParams = new SGuiParams();
                guiParams.getParamsMap().put(SGuiConsts.PARAM_DATE, matPeriod);
                guiParams.getParamsMap().put(SGuiConsts.PARAM_KEY, gridRow.getRowPrimaryKey());
                miClient.getSession().showForm(SModConsts.SX_TIC_MAN_SUP_INP_TP, SLibConsts.UNDEFINED, guiParams);
            }
        }
    }
}
