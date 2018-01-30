/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
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
import som.gui.SGuiClientSessionCustom;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewTicketsLog extends SGridPaneView implements ActionListener {

    private SGridFilterDateRange moFilterDateRange;
    private SPaneFilter moPaneFilterInputCategory;
    private SPaneFilter moPaneFilter;
    private JButton mjbPrint;

    public SViewTicketsLog(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_TIC_LOG, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, false, false, false, false);
        jtbFilterDeleted.setEnabled(false);

        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] { SLibTimeUtils.getBeginOfMonth(miClient.getSession().getWorkingDate()), SLibTimeUtils.getEndOfMonth(miClient.getSession().getWorkingDate()) });

        moPaneFilterInputCategory = new SPaneFilter(this, SModConsts.SU_INP_CT);
        moPaneFilterInputCategory.initFilter(null);

        moPaneFilter = new SPaneFilter(this, SModConsts.SU_ITEM);
        moPaneFilter.initFilter(null);

        mjbPrint = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_print.gif")), SUtilConsts.TXT_PRINT + " boleto", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilter);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbPrint);
    }

    private void actionPrint() {
        if (mjbPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    HashMap<String, Object> map = SPrtUtils.createReportParamsMap(miClient.getSession());
                    DecimalFormat oformatDecimal = SLibUtils.RoundingDecimalFormat;
                    
                    oformatDecimal.setMaximumFractionDigits(SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits());

                    map.put("nTicketId", gridRow.getRowPrimaryKey()[0]);
                    map.put("sCurrencyCode", ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getLocalCurrencyCode());
                    map.put("bShowMoney", miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_RM) ? true : false);
                    map.put("oFormatDecimal", oformatDecimal);

                    miClient.getSession().printReport(SModConsts.SR_TIC, SLibConsts.UNDEFINED, null, map);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE);
        sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDateRange("v.dt", (Date[]) filter);

        filter = (int[]) moFiltersMap.get(SModConsts.SU_INP_CT);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "vi.fk_inp_ct" }, (int[]) filter);
        }

        filter = (int[]) moFiltersMap.get(SModConsts.SU_ITEM);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_item" }, (int[]) filter);
        }

        msSql = "SELECT v.id_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt, "
                + "v.wei_src, "
                + "v.wei_des_gro_r, "
                + "(v.pac_qty_arr - v.pac_qty_dep) AS f_pac_qty, "
                + "v.wei_des_net_r, "
                + "v.b_tar, "
                + "vp.name, "
                + "vp.code "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS vp ON v.fk_prod = vp.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS vi ON "
                + "v.fk_item = vi.id_item "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "GROUP BY " + SDbConsts.FIELD_CODE + ", v.dt, v.id_tic "
                + "ORDER BY " + SDbConsts.FIELD_CODE + ", v.dt, v.id_tic ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = null;

        columns = new SGridColumnView[9];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "vp.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "vp.code", "Proveedor código");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_src", "Carga origen (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_gro_r", "Carga destino bruto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_pac_qty", "Cantidad empaque");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_net_r", "Carga destino neto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_tar", "Tarado");

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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbPrint) {
                actionPrint();
            }
        }
    }
}
