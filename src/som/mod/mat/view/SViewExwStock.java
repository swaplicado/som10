/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDate;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;
import som.cli.SCliConsts;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.form.SDialogStockCardex;
import som.mod.som.view.SPaneFilter;
import som.mod.som.view.SPaneUserInputCategory;

/**
 *
 * @author Sergio Flores
 */
public class SViewExwStock extends SGridPaneView implements ActionListener {
    
    public static final int BY_ITEM_EXW = 1;
    public static final int BY_ITEM = 2;

    private JButton jbExwCardex;
    private JButton jbExwAdjustmentIn;
    private JButton jbExwAdjustmentOut;
    
    private SGridFilterDate moFilterDate;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;
    private SPaneFilter moPaneFilterInputCategory;

    private SDialogStockCardex moDialogStockCardex;
    private Date mtExwStart;

    public SViewExwStock(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.MX_EXW_STOCK, gridSubtype, title);
        setRowButtonsEnabled(false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        jbExwCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_kardex.gif")), "Ver tarjeta auxiliar de almacén", this);
        jbExwCardex.setEnabled(false); // XXX provisinal hasta implementar diálogo de cárdex de movimientos de almacenes externos!
        jbExwAdjustmentIn = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_adj_in.gif")), "Ajuste de entradas de almacén", this);
        jbExwAdjustmentOut = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_adj_out.gif")), "Ajuste de salidas de almacén", this);

        moFilterDate = new SGridFilterDate(miClient, this);
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()).getTime()));

        moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "i");

        moPaneFilterInputCategory = new SPaneFilter(this, SModConsts.SU_INP_CT);
        moPaneFilterInputCategory.initFilter(null);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExwCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExwAdjustmentIn);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExwAdjustmentOut);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterInputCategory);

        moDialogStockCardex = new SDialogStockCardex(miClient);
        
        try {
            mtExwStart = getExwStart();
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private boolean isByItemExw() {
        return mnGridSubtype == BY_ITEM_EXW;
    }
    
    private Date getExwStart() throws Exception {
        Date start = null;
        
        try (Statement statement = miClient.getSession().getStatement().getConnection().createStatement()) {
            String sql = "SELECT MIN(dt) AS _start "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " "
                    + "WHERE NOT b_del AND b_tar "
                    + "AND (fk_tic_orig = " + SModSysConsts.SU_TIC_ORIG_EXW + " OR fk_tic_dest = " + SModSysConsts.SU_TIC_DEST_EXW + ");";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                start = resultSet.getDate("_start");
            }
        }
        
        return start != null ? start : SLibTimeUtils.createDate(SCliConsts.FRUIT_FIRST_YEAR, SLibTimeConsts.MONTH_JAN, 1);
    }

    private void actionPerformedExwCardex() {
        if (jbExwCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                /*
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
                */
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

        moPaneSettings = new SGridPaneSettings(isByItemExw() ? 4 : 3); // ID scale + ID item + ID unit [+ ID external warehouse]

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sqlHaving = "stock <> 0 ";
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
        
        String sqlCutoffBoy = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfYear(cutoff)) + "'";
        String sqlPeriod = "BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtExwStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(cutoff) + "' ";

        filter = (int[]) moFiltersMap.get(SModConsts.SU_INP_CT);
        if (filter != null) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "i.fk_inp_ct" }, (int[]) filter);
        }
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if (!sqlFilter.isEmpty()) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + sqlFilter;
        }

        msSql = "SELECT "
                + "s.name, s.code, s.id_sca AS " + SDbConsts.FIELD_ID + "1, ict.name, ict.id_inp_ct, i.name, i.code, i.id_item AS " + SDbConsts.FIELD_ID + "2, u.code, u.id_unit AS " + SDbConsts.FIELD_ID + "3, "
                + (isByItemExw() ? "exw.name, exw.code, exw.id_exw_fac AS " + SDbConsts.FIELD_ID + "4, " : "")
                + "i.code AS " + SDbConsts.FIELD_CODE + ", "
                + "i.name AS " + SDbConsts.FIELD_NAME + ", "
                + "SUM(f.flow_prev) AS start_stock, "
                + "SUM(IF(flow = 'IN', f.flow_year, 0.0)) AS flow_in, "
                + "-SUM(IF(flow = 'OUT', f.flow_year, 0.0)) AS flow_out, "
                + "SUM(f.flow_prev + f.flow_year) AS stock "
                + "FROM ("
                // stock inflows:
                + "SELECT "
                + "'IN' AS flow, t.fk_sca AS id_sca, t.fk_item AS id_item, t.fk_unit AS id_unit, t.fk_exw_fac_dest AS id_exw_fac, "
                + "SUM(IF(t.dt < " + sqlCutoffBoy + ", t.wei_des_net_r, 0.0)) AS flow_prev, "
                + "SUM(IF(t.dt >= " + sqlCutoffBoy + ", t.wei_des_net_r, 0.0)) AS flow_year "
                + "FROM "
                + "s_tic AS t "
                + "WHERE "
                + "NOT t.b_del AND t.b_tar AND t.dt " + sqlPeriod
                + "AND t.fk_tic_dest = " + SModSysConsts.SU_TIC_DEST_EXW + " "
                + "GROUP BY "
                + "t.fk_sca, t.fk_item, t.fk_unit, t.fk_exw_fac_dest "
                + ""
                + "UNION "
                + ""
                // stock outflows:
                + "SELECT "
                + "'OUT' AS flow, t.fk_sca AS id_sca, t.fk_item AS id_item, t.fk_unit AS id_unit, t.fk_exw_fac_orig AS id_exw_fac, "
                + "-SUM(IF(t.dt < " + sqlCutoffBoy + ", t.wei_des_net_r, 0.0)) AS flow_prev, "
                + "-SUM(IF(t.dt >= " + sqlCutoffBoy + ", t.wei_des_net_r, 0.0)) AS flow_year "
                + "FROM "
                + "s_tic AS t "
                + "WHERE "
                + "NOT t.b_del AND t.b_tar AND t.dt " + sqlPeriod
                + "AND t.fk_tic_orig = " + SModSysConsts.SU_TIC_ORIG_EXW + " "
                + "GROUP BY "
                + "t.fk_sca, t.fk_item, t.fk_unit, t.fk_exw_fac_dest "
                + ""
                + "UNION "
                + ""
                // inflow adjustments:
                + "SELECT "
                + "'IN' AS flow, a.fk_sca AS id_sca, a.fk_item AS id_item, a.fk_unit AS id_unit, a.fk_exw_fac AS id_exw_fac, "
                + "SUM(IF(a.dt < " + sqlCutoffBoy + ", a.qty, 0.0)) AS flow_prev, "
                + "SUM(IF(a.dt >= " + sqlCutoffBoy + ", a.qty, 0.0)) AS flow_year "
                + "FROM "
                + "m_exw_adj AS a "
                + "WHERE "
                + "NOT a.b_del AND a.dt " + sqlPeriod
                + "AND a.fk_iog_ct = " + SModSysConsts.SS_IOG_CT_IN + " "
                + "GROUP BY "
                + "a.fk_sca, a.fk_item, a.fk_unit, a.fk_exw_fac "
                + ""
                + "UNION "
                + ""
                // outflow adjustments:
                + "SELECT "
                + "'OUT' AS flow, a.fk_sca AS id_sca, a.fk_item AS id_item, a.fk_unit AS id_unit, a.fk_exw_fac AS id_exw_fac, "
                + "SUM(IF(a.dt < " + sqlCutoffBoy + ", a.qty, 0.0)) AS flow_prev, "
                + "SUM(IF(a.dt >= " + sqlCutoffBoy + ", a.qty, 0.0)) AS flow_year "
                + "FROM "
                + "m_exw_adj AS a "
                + "WHERE "
                + "NOT a.b_del AND a.dt " + sqlPeriod
                + "AND a.fk_iog_ct = " + SModSysConsts.SS_IOG_CT_OUT + " "
                + "GROUP BY "
                + "a.fk_sca, a.fk_item, a.fk_unit, a.fk_exw_fac "
                + ""
                + "ORDER BY "
                + "flow, id_sca, id_item, id_unit, id_exw_fac "
                + ") AS f "
                + ""
                + "INNER JOIN su_sca AS s ON s.id_sca = f.id_sca "
                + "INNER JOIN su_item AS i ON i.id_item = f.id_item "
                + "INNER JOIN su_inp_ct AS ict ON ict.id_inp_ct = i.fk_inp_ct "
                + "INNER JOIN su_unit AS u ON u.id_unit = f.id_unit "
                + (isByItemExw() ? "INNER JOIN mu_exw_fac AS exw ON exw.id_exw_fac = f.id_exw_fac " : "")
                + (!sqlWhere.isEmpty() ? "WHERE " + sqlWhere : "")
                + "GROUP BY "
                + "s.name, s.code, s.id_sca, ict.name, ict.id_inp_ct, i.name, i.code, i.id_item, u.code, u.id_unit "
                + (isByItemExw() ? ", exw.name, exw.code, exw.id_exw_fac " : "")
                + (!sqlHaving.isEmpty() ? "HAVING " + sqlHaving: "")
                + "ORDER BY "
                + "s.name, s.code, s.id_sca, ict.name, ict.id_inp_ct, i.name, i.code, i.id_item, u.code, u.id_unit "
                + (isByItemExw() ? ", exw.name, exw.code, exw.id_exw_fac " : "")
                + ";";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        
        SGridColumnView[] columns = new SGridColumnView[isByItemExw() ? 12 : 10];
        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "s.name", "Báscula", 125);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "s.code", "Báscula código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ict.name", "Categoría insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.code", "Ítem código");
        
        if (isByItemExw()) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "exw.name", "Almacén externo");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "exw.code", "Almacén externo código");
        }
        
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "start_stock", "Inventario inicial");
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
        
        if (isByItemExw()) {
            moSuscriptionsSet.add(SModConsts.MU_EXW_FAC);
        }
    }

    @Override
    public void actionMouseClicked() {
        actionPerformedExwCardex();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbExwCardex) {
                actionPerformedExwCardex();
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
