/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import sa.lib.gui.SGuiParams;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.mat.db.SExwUtils;
import som.mod.mat.form.SDialogExwStockCardex;
import som.mod.som.view.SPaneFilter;
import som.mod.som.view.SPaneUserInputCategory;

/**
 *
 * @author Sergio Flores
 */
public class SViewExwStock extends SGridPaneView implements ActionListener {

    private JButton jbShowCardex;
    private JButton jbExwAdjustmentIn;
    private JButton jbExwAdjustmentOut;
    
    private SGridFilterDate moFilterDate;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;
    private SPaneFilter moPaneFilterInputCategory;

    private SDialogExwStockCardex moDialogCardex;
    private Date mtExwStart;

    public SViewExwStock(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.MX_EXW_STOCK, gridSubtype, title);
        setRowButtonsEnabled(false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        jbShowCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_kardex.gif")), "Ver tarjeta auxiliar de almacén", this);
        jbExwAdjustmentIn = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_adj_in.gif")), "Ajuste de entradas de almacén", this);
        jbExwAdjustmentOut = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_adj_out.gif")), "Ajuste de salidas de almacén", this);

        moFilterDate = new SGridFilterDate(miClient, this);
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()).getTime()));

        moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "i");

        moPaneFilterInputCategory = new SPaneFilter(this, SModConsts.SU_INP_CT);
        moPaneFilterInputCategory.initFilter(null);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbShowCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExwAdjustmentIn);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExwAdjustmentOut);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterInputCategory);

        moDialogCardex = new SDialogExwStockCardex(miClient, mnGridSubtype, msTitle);
        
        try {
            mtExwStart = SExwUtils.getExwStart(miClient.getSession());
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private boolean isStockItemAndExw() {
        return mnGridSubtype == SExwUtils.STOCK_ITEM_EXW;
    }

    private void actionPerformedShowCardex() {
        if (jbShowCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                Date cutoff;
                int[] key = (int[]) gridRow.getRowPrimaryKey();
                
                Object filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
                if (filter != null) {
                    cutoff = (Date) filter;
                }
                else {
                    cutoff = miClient.getSession().getWorkingDate();
                }
                
                SDialogExwStockCardex.Params params = new SDialogExwStockCardex.Params(key[0], key[1], isStockItemAndExw() ? key[2] : SExwUtils.EXW_FAC_UNDEF, cutoff);

                moDialogCardex.resetForm();
                moDialogCardex.setValue(SDialogExwStockCardex.PARAMS, params);
                moDialogCardex.setVisible(true);
            }
        }
    }

    private void actionPerformedExwAdjustment(final int iogCategory) {
        miClient.getSession().showForm(SModConsts.M_EXW_ADJ, 0, new SGuiParams(iogCategory));
    }

    @Override
    public void prepareSqlQuery() {
        String sqlWhere = "";
        String sqlHaving = "";
        Date cutoff = null;
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(isStockItemAndExw() ? 3 : 2); // ID item + ID unit [+ ID external warehouse]

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sqlHaving = "stock <> 0.0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
        if (filter != null) {
            cutoff = (Date) filter;
        }
        else {
            cutoff = miClient.getSession().getWorkingDate();
        }
        
        if (cutoff.before(mtExwStart)) {
            miClient.showMsgBoxWarning("La fecha de corte, " + SLibUtils.DateFormatDate.format(cutoff) + ", no puede ser anterior a la fecha del primer movimiento en almacenes externos, " + SLibUtils.DateFormatDate.format(mtExwStart) + ".");
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_INP_CT);
        if (filter != null) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "i.fk_inp_ct" }, (int[]) filter);
        }
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if (!sqlFilter.isEmpty()) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + sqlFilter;
        }

        msSql = "SELECT "
                + "ict.name, ict.id_inp_ct, "
                + "i.name, i.code, i.id_item AS " + SDbConsts.FIELD_ID + "1, "
                + "u.code, u.id_unit AS " + SDbConsts.FIELD_ID + "2, "
                + (isStockItemAndExw() ? "exw.name, exw.code, exw.id_exw_fac AS " + SDbConsts.FIELD_ID + "3, " : "")
                + "i.code AS " + SDbConsts.FIELD_CODE + ", "
                + "i.name AS " + SDbConsts.FIELD_NAME + ", "
                + "SUM(s.flow_prev) AS open_stock, "
                + "SUM(IF(flow = '" + SExwUtils.INFLOW + "', s.flow_curr, 0.0)) AS flow_in, "
                + "-SUM(IF(flow = '" + SExwUtils.OUTFLOW + "', s.flow_curr, 0.0)) AS flow_out, " // outflows is negative, so render it as positive!
                + "SUM(s.flow_prev + s.flow_curr) AS stock "
                + "FROM ("
                + SExwUtils.composeSqlExwStock(0, 0, SExwUtils.EXW_FAC_UNDEF, mtExwStart, cutoff)
                + ") AS s " // stock
                + ""
                + "INNER JOIN su_item AS i ON i.id_item = s.id_item "
                + "INNER JOIN su_inp_ct AS ict ON ict.id_inp_ct = i.fk_inp_ct "
                + "INNER JOIN su_unit AS u ON u.id_unit = s.id_unit "
                + (isStockItemAndExw() ? "INNER JOIN mu_exw_fac AS exw ON exw.id_exw_fac = s.id_exw_fac " : "")
                + (!sqlWhere.isEmpty() ? "WHERE " + sqlWhere : "")
                + "GROUP BY "
                + "ict.name, ict.id_inp_ct, i.name, i.code, i.id_item, u.code, u.id_unit "
                + (isStockItemAndExw() ? ", exw.name, exw.code, exw.id_exw_fac " : "")
                + (!sqlHaving.isEmpty() ? "HAVING " + sqlHaving : "")
                + "ORDER BY "
                + "ict.name, ict.id_inp_ct, i.name, i.code, i.id_item, u.code, u.id_unit "
                + (isStockItemAndExw() ? ", exw.name, exw.code, exw.id_exw_fac" : "")
                + ";";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        
        SGridColumnView[] columns = new SGridColumnView[isStockItemAndExw() ? 10 : 8];
        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ict.name", "Categoría insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.code", "Ítem código");
        
        if (isStockItemAndExw()) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "exw.name", "Almacén externo");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "exw.code", "Almacén externo código");
        }
        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "open_stock", "Inventario inicial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "flow_in", "Entradas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "flow_out", "Salidas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "stock", "Existencias");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.code", "Unidad");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
        moSuscriptionsSet.add(SModConsts.MU_EXW_ADJ_TP);
        moSuscriptionsSet.add(SModConsts.M_EXW_ADJ);
        moSuscriptionsSet.add(SModConsts.S_TIC);
        moSuscriptionsSet.add(SModConsts.S_TIC_EXW_UPD_LOG);
        moSuscriptionsSet.add(SModConsts.SX_TIC_TARE);
        
        if (isStockItemAndExw()) {
            moSuscriptionsSet.add(SModConsts.MU_EXW_FAC);
        }
    }

    @Override
    public void actionMouseClicked() {
        actionPerformedShowCardex();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbShowCardex) {
                actionPerformedShowCardex();
            }
            else if (button == jbExwAdjustmentIn) {
                actionPerformedExwAdjustment(SModSysConsts.SS_IOG_CT_IN);
            }
            else if (button == jbExwAdjustmentOut) {
                actionPerformedExwAdjustment(SModSysConsts.SS_IOG_CT_OUT);
            }
        }
    }
}
