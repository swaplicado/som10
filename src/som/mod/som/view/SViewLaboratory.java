/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import som.gui.SGuiClientSessionCustom;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores, Isabel Servín, Adrián Aviés
 */
public class SViewLaboratory extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;
    private SPaneFilter moPaneFilterInputCategory;
    private SPaneFilter moPaneFilterItem;
    private JButton mjbPrint;

    public SViewLaboratory(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_LAB, gridSubtype, title);
        setRowButtonsEnabled(false, false, false, false, isSummary());
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_LAB, "it");
        moPaneFilterInputCategory = new SPaneFilter(this, SModConsts.SU_INP_CT);
        moPaneFilterItem = new SPaneFilter(this, SModConsts.SU_ITEM);
        moPaneFilterItem.initFilter(null);

        mjbPrint = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_print.gif")), SUtilConsts.TXT_PRINT + " boleto", this);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterItem);
    }

    private boolean isSummary() {
        return mnGridSubtype == SModSysConsts.SX_LAB_TEST;
    }

    private void actionPrint() {
        if (mjbPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                int[] key = SSomUtils.getTicketByLaboratory(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());

                if (key == null || key[0] == SLibConsts.UNDEFINED) {
                    miClient.showMsgBoxWarning(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\n¡No se pudo leer el boleto del análisis de laboratorio!");
                }
                else {
                    HashMap<String, Object> map = SPrtUtils.createReportParamsMap(miClient.getSession());
                    DecimalFormat oformatDecimal = SLibUtils.RoundingDecimalFormat;
                    
                    oformatDecimal.setMaximumFractionDigits(SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits());

                    map.put("nTicketId", key[0]);
                    map.put("sCurrencyCode", ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getLocalCurrencyCode());
                    map.put("bShowMoney", false);
                    map.put("oFormatDecimal", oformatDecimal);

                    miClient.getSession().printReport(SModConsts.SR_TIC, SLibConsts.UNDEFINED, null, map);
                }
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String sqlWhere = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(isSummary() ? 1 : 2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if(!sqlFilter.isEmpty()) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + sqlFilter;
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_INP_CT);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "it.fk_inp_ct" }, (int[]) filter);
        }

        filter = (int[]) moFiltersMap.get(SModConsts.SU_ITEM);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_item" }, (int[]) filter);
        }
        
        msSql = "SELECT ";

        if (isSummary()) {
            msSql += "vt.id_lab AS " + SDbConsts.FIELD_ID + "1, ";
        }
        else {
            msSql += "vt.id_lab AS " + SDbConsts.FIELD_ID + "1, "
                    + "vt.id_test AS " + SDbConsts.FIELD_ID + "2, ";
        }

        msSql += "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.num, "
                + "v.dt, "
                + "v.b_done, "
                + "if (v.b_done, " + SGridConsts.ICON_OK + ", " + SGridConsts.ICON_NULL + ") AS f_done, ";

        if (isSummary()) {
            msSql += "count(*) AS f_tests, "
                    + "SUM(vt.den) / SUM(vt.den <> 0) as f_den, "
                    + "SUM(vt.iod_val) / SUM(vt.iod_val <> 0) as f_iod_val, "
                    + "SUM(vt.ref_ind) / SUM(vt.ref_ind <> 0) as f_ref_ind, "
                    + "ROUND(SUM(vt.imp_per) / SUM(vt.imp_per <> 0), 4) as f_imp_per, "
                    + "ROUND(SUM(vt.moi_per) / SUM(vt.moi_per <> 0), 4) as f_moi_per, "
                    + "SUM(vt.pro_per) / SUM(vt.pro_per <> 0) as f_pro_per, "
                    + "SUM(vt.oil_per) / SUM(vt.oil_per <> 0) as f_oil_per, "
                    + "vt.oil_yield_adj_per, "
                    + "IF((SUM(vt.oil_per) / SUM(vt.oil_per <> 0) - vt.oil_yield_adj_per) < 0.0, 0.0, SUM(vt.oil_per) / SUM(vt.oil_per <> 0) - vt.oil_yield_adj_per) AS f_oil_per_adj, "
                    + "SUM(vt.ole_per) / SUM(vt.ole_per <> 0) as f_ole_per, "
                    + "SUM(vt.lin_per) / SUM(vt.lin_per <> 0) as f_lin_per, "
                    + "SUM(vt.llc_per) / SUM(vt.llc_per <> 0) as f_llc_per, "
                    + "SUM(vt.eru_per) / SUM(vt.eru_per <> 0) as f_eru_per, "
                    + "SUM(vt.aci_per) / SUM(vt.aci_per <> 0) as f_aci_per, "
                    + "SUM(vt.aci_avg_per) / SUM(vt.aci_avg_per <> 0) as f_aci_avg_per, ";
        }
        else {
            msSql += "vt.id_test, "
                    + "vt.den AS f_den, "
                    + "vt.iod_val AS f_iod_val, "
                    + "vt.ref_ind AS f_ref_ind, "
                    + "vt.imp_per AS f_imp_per, "
                    + "vt.moi_per AS f_moi_per, "
                    + "vt.pro_per AS f_pro_per, "
                    + "vt.oil_per AS f_oil_per, "
                    + "vt.oil_yield_adj_per, "
                    + "IF((vt.oil_per - vt.oil_yield_adj_per) < 0.0, 0.0, vt.oil_per - vt.oil_yield_adj_per) AS f_oil_per_adj, "
                    + "vt.ole_per AS f_ole_per, "
                    + "vt.lin_per AS f_lin_per, "
                    + "vt.llc_per AS f_llc_per, "
                    + "vt.eru_per AS f_eru_per, "
                    + "vt.aci_per AS f_aci_per, "
                    + "vt.aci_avg_per AS f_aci_avg_per, ";
        }

        msSql += "t.num, "
                + "t.dt, "
                + "ts.code, "
                + "ts.name, "
                + "sc.id_sca, "
                + "sc.code, "
                + "sc.name, "
                + "it.code, "
                + "it.name, "
                + "pr.code, "
                + "pr.name, "
                + "pr.name_trd, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_LAB) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_LAB_TEST) + " AS vt ON "
                + "v.id_lab = vt.id_lab "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t ON "
                + "v.id_lab = t.fk_lab_n "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sc ON "
                + "t.fk_sca = sc.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_TIC_ST) + " AS ts ON "
                + "t.fk_tic_st = ts.id_tic_st "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS it ON "
                + "t.fk_item = it.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pr ON "
                + "t.fk_prod = pr.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere);

        if (isSummary()) {
            msSql += "GROUP BY v.num, vt.id_lab ";
            msSql += "ORDER BY v.num, vt.id_lab ";
        }
        else {
            msSql += "ORDER BY v.num, vt.id_lab, vt.id_test ";
        }
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[34];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.num", "Análisis lab");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt", SGridConsts.COL_TITLE_DATE + " análisis lab");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "f_done", "Terminado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sc.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "t.num", "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "t.dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "pr.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pr.name_trd", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "pr.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "it.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "it.code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ts.name", "Estatus boleto");

        if (isSummary()) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "f_tests", "Cant pruebas lab");
        }
        else {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "vt.id_test", "Prueba lab");
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "f_imp_per", "Impurezas %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "f_moi_per", "Humedad %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_den", "Densidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_ref_ind", "Índice refracción (IR)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_iod_val", "Valor yodo (VI)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_ole_per", "Ácido oleico %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_lin_per", "Ácido linoleico %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_llc_per", "Ácido linolénico %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_eru_per", "Ácido erúcico %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_pro_per", "Proteína %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_oil_per", "Aceite %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_DISC, "vt.oil_yield_adj_per", "Ajuste rendimiento aceite %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_oil_per_adj", "Rendimiento teórico aceite %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_aci_per", "Acidez %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_aci_avg_per", "Acidez promedio proceso");
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
        moSuscriptionsSet.add(SModConsts.S_TIC);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.CU_USR);
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
