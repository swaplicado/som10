/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.util.Arrays;
import java.util.Date;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
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
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores, Isabel Servín
 */
public class SViewTicketsSupplierItem extends SGridPaneView {
    
    private SGridFilterDateRange moFilterDateRange;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;
    private SPaneFilter moPaneFilterTicketOrigin;
    private SPaneFilter moPaneFilterTicketDestination;
    private SPaneFilter moPaneFilterScale;
    
    public SViewTicketsSupplierItem(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_TIC_MAN_SUP, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, true, false, false, false);
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] { SLibTimeUtils.getBeginOfYear(miClient.getSession().getWorkingDate()), SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()) });
        moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "i");
        moPaneFilterTicketOrigin = new SPaneFilter(this, SModConsts.SU_TIC_ORIG);
        moPaneFilterTicketOrigin.initFilter(null);
        moPaneFilterTicketDestination = new SPaneFilter(this, SModConsts.SU_TIC_DEST);
        moPaneFilterTicketDestination.initFilter(null);
        moPaneFilterScale = new SPaneFilter(this, SModConsts.SU_SCA);
        moPaneFilterScale.initFilter(null);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketOrigin);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketDestination);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterScale);
    }
    
    @Override
    public void prepareSqlQuery() {
        Object filter;
        String sql = "";
        String sqlAux = "";

        moPaneSettings = new SGridPaneSettings(4);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "t.b_del = 0 ";
        }

        filter = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE);
        sqlAux += (!sqlAux.isEmpty() ? "" : " AND ") + SGridUtils.getSqlFilterDateRange("t.dt", (Date[]) filter);
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if(!sqlFilter.isEmpty()) {
            sqlAux += (sqlAux.isEmpty() ? "" : "AND ") + sqlFilter;
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_ORIG);
        if (filter != null) {
            sqlAux += (sqlAux.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "t.fk_tic_orig" }, (int[]) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_DEST);
        if (filter != null) {
            sqlAux += (sqlAux.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "t.fk_tic_dest" }, (int[]) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_SCA);
        if (filter != null) {
            sqlAux += (sqlAux.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "t.fk_sca" }, (int[]) filter);
        }
        
        msSql = "SELECT "
                + "t.fk_seas_n AS " + SDbConsts.FIELD_ID + "1, "
                + "t.fk_item AS " + SDbConsts.FIELD_ID + "2, "
                + "t.fk_prod AS " + SDbConsts.FIELD_ID + "3, "
                + "t.b_mfg_out AS " + SDbConsts.FIELD_ID + "4, "
                + "IF(s.code IS NULL, '" + SUtilConsts.NON_APPLYING + "', s.code) AS " + SDbConsts.FIELD_CODE + ", "
                + "IF(s.name IS NULL, '" + SUtilConsts.NON_APPLYING + "', s.name) AS " + SDbConsts.FIELD_NAME + ", "
                + "i.code, "
                + "i.name, "
                + "p.code, "
                + "p.name, "
                + "tor.code, "
                + "tde.code, "
                + "sc.code, "
                + "p.name_trd "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON t.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON t.fk_prod = p.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_ORIG) + " AS tor ON t.fk_tic_orig = tor.id_tic_orig "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_DEST) + " AS tde ON t.fk_tic_dest = tde.id_tic_dest "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sc ON t.fk_sca = sc.id_sca "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS s ON t.fk_seas_n = s.id_seas "
                + "WHERE t.b_del = 0 " + sqlAux + " " 
                + "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " + SDbConsts.FIELD_ID + "3, " + SDbConsts.FIELD_ID + "4, s.code, s.name, i.code, i.name, p.code, p.name, p.name_trd "
                + "ORDER BY s.name, s.code, " + SDbConsts.FIELD_ID + "1, i.name, i.code, " + SDbConsts.FIELD_ID + "2, p.code, p.name, " + SDbConsts.FIELD_ID + "3, " + SDbConsts.FIELD_ID + "4 ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns;
        
        columns = new SGridColumnView[10];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sc.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, SDbConsts.FIELD_NAME, "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "p.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "p.name_trd", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "p.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, SDbConsts.FIELD_ID + "4", "Es maquila");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tor.code", "Procedencia boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tde.code", "Destino boleto");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_TIC);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.SU_SEAS);
    }
    
    @Override
    public void actionRowEdit() {
        boolean edit = true;
        
        if (jbRowEdit.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                
                if (gridRow.getRowPrimaryKey()[0] == 0) {
                    edit = miClient.showMsgBoxConfirm("No tiene temporada configurada, no se mostrarán los pagos al proveedor.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                }
                
                if (edit) {
                    super.actionRowEdit();
                }
            }
        }
    }
}
