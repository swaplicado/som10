/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDate;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.som.form.SDialogStockCardex;

/**
 *
 * @author Juan Barajas
 */
public class SViewStock extends SGridPaneView implements ActionListener {

    private JButton jbCardex;

    private SGridFilterDate moFilterDate;
    private SDialogStockCardex moDialogStockCardex;
    private SPaneFilter moPaneFilter;
    private SPaneFilter moPaneFilterInputCategory;
    private SPaneFilter moPaneFilterWarehouse;
    private SPaneFilter moPaneFilterDivision;

    private Date mtDateCutOff;

    public SViewStock(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_STK, gridSubtype, title);
        setRowButtonsEnabled(false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_kardex.gif")), "Ver tarjeta auxiliar de almacén", this);

        moFilterDate = new SGridFilterDate(miClient, this);
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()).getTime()));

        moDialogStockCardex = new SDialogStockCardex(miClient);

        moPaneFilter = new SPaneFilter(this, SModConsts.SS_ITEM_TP);
        moPaneFilter.initFilter(null);
        moPaneFilterInputCategory = new SPaneFilter(this, SModConsts.SU_INP_CT);
        moPaneFilterInputCategory.initFilter(null);

        if (showWarehouses()) {
            moPaneFilterWarehouse = new SPaneFilter(this, SModConsts.CU_WAH);
            moPaneFilterWarehouse.initFilter(null);
        }

        if (showDivision()) {
            moPaneFilterDivision = new SPaneFilter(this, SModConsts.CU_DIV);
            moPaneFilterDivision.initFilter(null);
        }

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilter);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterInputCategory);

        if (showWarehouses()) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterWarehouse);
        }

        if (showDivision()) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterDivision);
        }

    }

    private void actionShowCardex() {
        if (jbCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    int[] whKey = showWarehouses() ? new int[] { key[key.length - (!showDivision() ? 3 : 4)], key[key.length - (!showDivision() ? 2 : 3)], key[key.length - (!showDivision() ? 1 : 2)] } : new int[] { ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getCompany().getPrimaryKey()[0], ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getCompany().getChildBranches().get(0).getPrimaryKey()[0], SLibConsts.UNDEFINED };
                    int itemId = key[1];
                    int unitId = key[2];
                    int divisionId = showDivision() ? (!showWarehouses() ? key[3] : key[6]) : SLibConsts.UNDEFINED;

                    moDialogStockCardex.formReset();
                    moDialogStockCardex.setFormParams(mtDateCutOff, itemId, unitId, whKey, divisionId);
                    moDialogStockCardex.setVisible(true);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sqlHaving = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings((!showWarehouses() ? !showDivision() ? 3 : 4 : !showDivision() ? 6 : 7));

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sqlHaving = "HAVING f_stk <> 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.id_year = " + SLibTimeUtils.digestYear((SGuiDate) filter)[0] + " AND "
                    + " v.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
        }
        else {
            sql += (sql.length() == 0 ? "" : "AND ")  + "v.id_year = " + SLibTimeUtils.digestYear(miClient.getSession().getWorkingDate())[0] + " AND "
                    + " v.dt <= '" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate())) + "' ";
        }
        mtDateCutOff = filter != null ? (SGuiDate) filter : miClient.getSession().getWorkingDate();

        filter = (int[]) moFiltersMap.get(SModConsts.SS_ITEM_TP);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "vi.fk_item_tp" }, (int[]) filter);
        }

        filter = (int[]) moFiltersMap.get(SModConsts.SU_INP_CT);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "vi.fk_inp_ct" }, (int[]) filter);
        }

        if (showDivision()) {
            filter = (int[]) moFiltersMap.get(SModConsts.CU_DIV);
            if (filter != null) {
                sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.id_div" }, (int[]) filter);
            }
        }

        if (showWarehouses()) {
            filter = (int[]) moFiltersMap.get(SModConsts.CU_WAH);
            if (filter != null) {
                sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.id_co", "v.id_cob", "v.id_wah" }, (int[]) filter);
            }
        }

        msSql = "SELECT "
                + "v.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_item AS " + SDbConsts.FIELD_ID + "2, "
                + "v.id_unit AS " + SDbConsts.FIELD_ID + "3, "
                + (!showWarehouses() ? "" : "v.id_co AS " + SDbConsts.FIELD_ID + "4, ")
                + (!showWarehouses() ? "" : "v.id_cob AS " + SDbConsts.FIELD_ID + "5, ")
                + (!showWarehouses() ? "" : "v.id_wah AS " + SDbConsts.FIELD_ID + "6, ")
                + (!showDivision() ? "" : "v.id_div AS " + SDbConsts.FIELD_ID + (!showWarehouses() ? "4," : "7, "))
                + "vi.code AS " + SDbConsts.FIELD_CODE + ", "
                + "vi.name AS " + SDbConsts.FIELD_NAME + ", "
                + (!showWarehouses() ? "" : "vc.code, ")
                + (!showWarehouses() ? "" : "vw.name, ")
                + (!showWarehouses() ? "" : "vw.code, ")
                + (!showDivision() ? "" : "d.code, ")
                + "SUM(v.mov_in) AS f_mov_i, "
                + "SUM(v.mov_out) AS f_mov_o, "
                + "SUM(v.mov_in - v.mov_out) AS f_stk, "
                + "vu.code "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS vi ON "
                + "v.id_item = vi.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS vu ON "
                + "v.id_unit = vu.id_unit "
                + (!showWarehouses() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS vw ON v.id_co = vw.id_co AND v.id_cob = vw.id_cob AND v.id_wah = vw.id_wah ")
                + (!showWarehouses() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS vc ON v.id_co = vc.id_co AND v.id_cob = vc.id_cob ")
                + (!showDivision() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON v.id_div = d.id_div ")
                + "WHERE v.b_del = 0 " +
                (sql.isEmpty() ? "" : "AND " + sql)
                + "GROUP BY v.id_item, v.id_unit, "
                + (!showWarehouses() ? "" : "v.id_cob, v.id_wah, vc.code, vw.name, ")
                + (!showDivision() ? "" : "v.id_div, ")
                + "vi.code, vi.name, vu.code "
                + sqlHaving
                + "ORDER BY vi.name, vi.code, v.id_item, vu.code, v.id_unit "
                + (!showWarehouses() ? "" : ", vc.code, vw.name, v.id_cob, v.id_wah ")
                + (!showDivision() ? "" : ", d.name, v.id_div ");
    }

    private boolean showWarehouses() {
        return mnGridSubtype == SModConsts.SX_STK_WAH_WAH ||
                mnGridSubtype == SModConsts.SX_STK_WAH_DIV;
    }

    private boolean showDivision() {
        return mnGridSubtype == SModConsts.SX_STK_DIV ||
                mnGridSubtype == SModConsts.SX_STK_WAH_DIV;
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = null;
        if (showWarehouses()) {
            columns = new SGridColumnView[!showDivision() ? 8 : 9];
        }
        else {
            columns = new SGridColumnView[!showDivision() ? 6 : 7];
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, "Ítem código");

        if (showWarehouses()) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "vc.code", "Sucursal");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "vw.code", "Almacén");
        }

        if (showDivision()) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "d.code", "División");
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_mov_i", "Entradas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_mov_o", "Salidas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_stk", "Existencias");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "vu.code", "Unidad");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_IOG);
        moSuscriptionsSet.add(SModConsts.S_MIX);
        moSuscriptionsSet.add(SModConsts.CU_COB);
        moSuscriptionsSet.add(SModConsts.CU_WAH);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
    }

    @Override
    public void actionMouseClicked() {
        actionShowCardex();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCardex) {
                actionShowCardex();
            }
        }
    }
}
