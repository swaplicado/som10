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
 * @author Juan Barajas, Sergio Flores
 */
public class SViewTicketsSupplierItem extends SGridPaneView {
    
    private final SGridFilterDateRange moFilterDateRange;
    
    public SViewTicketsSupplierItem(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_TIC_MAN_SUP, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, true, false, false, false);
        jtbFilterDeleted.setEnabled(false);
        
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] { SLibTimeUtils.getBeginOfYear(miClient.getSession().getWorkingDate()), SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()) });
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sqlAux = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(4);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "t.b_del = 0 ";
        }

        filter = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE);
        sqlAux += (!sqlAux.isEmpty() ? "" : " AND ") + SGridUtils.getSqlFilterDateRange("t.dt", (Date[]) filter);
        
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
                + "p.name "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON t.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON t.fk_prod = p.id_prod "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS s ON t.fk_seas_n = s.id_seas "
                + "WHERE t.b_del = 0 " + sqlAux + " " 
                + "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " + SDbConsts.FIELD_ID + "3, " + SDbConsts.FIELD_ID + "4, s.code, s.name, i.code, i.name, p.code, p.name "
                + "ORDER BY s.name, s.code, " + SDbConsts.FIELD_ID + "1, i.name, i.code, " + SDbConsts.FIELD_ID + "2, p.code, p.name, " + SDbConsts.FIELD_ID + "3, " + SDbConsts.FIELD_ID + "4 ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = null;
        
        columns = new SGridColumnView[6];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, SDbConsts.FIELD_NAME, "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.name", SGridConsts.COL_TITLE_NAME + " ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.code", SGridConsts.COL_TITLE_CODE + " ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "p.name", SGridConsts.COL_TITLE_NAME + " proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "p.code", SGridConsts.COL_TITLE_CODE + " proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, SDbConsts.FIELD_ID + "4", "Es maquila");

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
