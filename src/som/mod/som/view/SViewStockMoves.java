/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JButton;
import sa.lib.SLibTimeUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewStockMoves extends SGridPaneView implements ActionListener {

    private SGridFilterDateRange moFilterDateRange;
    private SPaneFilter moPaneFilter;

    public SViewStockMoves(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_STK_MOVE, gridSubtype, title);
        setRowButtonsEnabled(false);
        initComponetsCustom();
    }

    /*
    * Private methods
    */

    private void initComponetsCustom() {
        jtbFilterDeleted.setEnabled(false);

        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] { SLibTimeUtils.getBeginOfMonth(miClient.getSession().getWorkingDate()), SLibTimeUtils.getEndOfMonth(miClient.getSession().getWorkingDate()) });
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);

        if (mnGridSubtype == SModConsts.SX_STK_MOVE_DET) {
            moPaneFilter = new SPaneFilter(this, SModConsts.SS_IOG_TP);
            moPaneFilter.initFilter(null);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilter);
        }
    }

    /*
    * Public methods
    */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;
        String sDatabaseErpExtName = ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getExtDatabase().getDbName();
        String sDatabaseCoExtName = ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getExtDatabaseCo().getDbName();

        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);

        filter = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE);
        if (filter != null) {
            sql += "AND " + SGridUtils.getSqlFilterDateRange("v.dt", (Date[]) filter);
        }

        filter = (int[]) moFiltersMap.get(SModConsts.SS_IOG_TP);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_iog_ct", "v.fk_iog_cl", "v.fk_iog_tp", }, (int[]) filter);
        }

        msSql = "SELECT "
            + "v.id_year AS " + SDbConsts.FIELD_ID + "1, "
            + "v.id_item AS " + SDbConsts.FIELD_ID + "2, "
            + "v.id_unit AS " + SDbConsts.FIELD_ID + "3, "
            + "vi.code AS " + SDbConsts.FIELD_CODE + ", "
            + "vi.name AS " + SDbConsts.FIELD_NAME + ", "
            + (mnGridSubtype == SModConsts.SX_STK_MOVE_DET ?
              "CONCAT(t.code, '-', g.num) AS f_num, "
            + "g.dt AS f_date, "
            + "v.mov_in AS f_mov_i, "
            + "v.mov_out AS f_mov_o, "
            : "SUM(v.mov_in) AS f_mov_i, "
            + "SUM(v.mov_out) AS f_mov_o, "
            + "SUM(v.mov_in - v.mov_out) AS f_stk, ")
            + "vu.code, "
            + "t.name AS f_type, "
            + "t.code AS f_type_code, "
            + "ad.name AS f_adj, "
            + "ad.code AS f_adj_code, "
            + "cob.name AS f_cob, "
            + "cob.code AS f_cob_code, "
            + "wa.name AS f_wa, "
            + "wa.code AS f_wa_code, "
            + "d.code AS f_di_code "
            + (mnGridSubtype == SModConsts.SX_STK_MOVE_DET ?
              ", COALESCE(CONCAT(d.num_ser, IF(length(d.num_ser) > 0, '-', ''), d.num), '') AS f_dps, "
            + "d.dt, "
            + "dpst.code, "
            + "COALESCE(CONCAT(da.num_ser, IF(length(da.num_ser) > 0, '-', ''), da.num), '') AS f_dps_adj, "
            + "da.dt, "
            + "adjt.code, "
            + "COALESCE(CONCAT(dg.num_ser, IF(length(dg.num_ser) > 0, '-', ''), dg.num), '') AS f_diog, "
            + "dg.dt, "
            + "diogt.code "
            : "")
            + "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS v "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS cob ON v.id_co = cob.id_co AND v.id_cob = cob.id_cob "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wa ON v.id_co = wa.id_co AND v.id_cob = wa.id_cob AND v.id_wah = wa.id_wah "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON v.id_div = d.id_div "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_TP) + " AS t ON v.fk_iog_ct = t.id_iog_ct AND v.fk_iog_cl = t.id_iog_cl AND v.fk_iog_tp = t.id_iog_tp "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g ON v.fk_iog = g.id_iog "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_IOG_ADJ_TP) + " AS ad ON g.fk_iog_adj_tp = ad.id_iog_adj_tp "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS vi ON v.id_item = vi.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS vu ON v.id_unit = vu.id_unit "
            + (mnGridSubtype == SModConsts.SX_STK_MOVE_DET ?
              "LEFT OUTER JOIN " + sDatabaseCoExtName + ".trn_dps_ety AS de ON g.fk_ext_dps_year_n = de.id_year AND g.fk_ext_dps_doc_n = de.id_doc AND "
            + "g.fk_ext_dps_ety_n = de.id_ety "
            + "LEFT OUTER JOIN " + sDatabaseCoExtName + ".trn_dps AS d ON de.id_year = d.id_year AND de.id_doc = d.id_doc "
            + "LEFT OUTER JOIN " + sDatabaseErpExtName + ".trnu_tp_dps AS dpst ON d.fid_ct_dps = dpst.id_ct_dps AND d.fid_cl_dps = dpst.id_cl_dps AND d.fid_tp_dps = dpst.id_tp_dps "
            + "LEFT OUTER JOIN " + sDatabaseCoExtName + ".trn_dps_ety AS dea ON g.fk_ext_adj_year_n = dea.id_year AND g.fk_ext_adj_doc_n = dea.id_doc AND "
            + "g.fk_ext_adj_ety_n = dea.id_ety "
            + "LEFT OUTER JOIN " + sDatabaseCoExtName + ".trn_dps AS da ON dea.id_year = da.id_year AND dea.id_doc = da.id_doc "
            + "LEFT OUTER JOIN " + sDatabaseErpExtName + ".trnu_tp_dps AS adjt ON da.fid_ct_dps = adjt.id_ct_dps AND da.fid_cl_dps = adjt.id_cl_dps AND da.fid_tp_dps = adjt.id_tp_dps "
            + "LEFT OUTER JOIN " + sDatabaseCoExtName + ".trn_diog_ety AS deg ON g.fk_ext_iog_year_n = deg.id_year AND g.fk_ext_iog_doc_n = deg.id_doc AND "
            + "g.fk_ext_iog_ety_n = deg.id_ety "
            + "LEFT OUTER JOIN " + sDatabaseCoExtName + ".trn_diog AS dg ON deg.id_year = dg.id_year AND deg.id_doc = dg.id_doc "
            + "LEFT OUTER JOIN " + sDatabaseErpExtName + ".trns_tp_iog AS diogt ON dg.fid_ct_iog = diogt.id_ct_iog AND dg.fid_cl_iog = diogt.id_cl_iog AND dg.fid_tp_iog = diogt.id_tp_iog "
            : "")
            + "WHERE v.b_del = 0 AND g.b_del = 0 " + sql
            + (mnGridSubtype == SModConsts.SX_STK_MOVE_DET ? "" :
              "GROUP BY t.id_iog_ct, t.id_iog_cl, t.id_iog_tp, ad.id_iog_adj_tp, v.id_item, v.id_unit "
            + "HAVING f_stk <> 0 ")
            + "ORDER BY vi.name, vi.code, vu.code, v.id_unit ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = null;

        columns = new SGridColumnView[mnGridSubtype == SModConsts.SX_STK_MOVE_DET ? 21 : 10];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_mov_i", "Entradas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_mov_o", "Salidas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "vu.code", "Unidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "f_cob_code", "Sucursal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "f_wa_code", "Almacén");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "f_di_code", "División");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_type", "Tipo movimiento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_adj_code", "Tipo ajuste");

        if (mnGridSubtype == SModConsts.SX_STK_MOVE_DET) {
            //columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_type_code", "Tipo docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_date", SGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dpst.code", "Tipo docto CV");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_dps", "Folio docto CV");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", SGridConsts.COL_TITLE_DATE + " docto CV");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "adjt.code", "Tipo docto CV ajuste");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_dps_adj", "Folio docto CV ajuste");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "da.dt", SGridConsts.COL_TITLE_DATE + " docto CV ajuste");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "diogt.code", "Tipo docto ext inv");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_diog", "Folio docto ext inv");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dg.dt", SGridConsts.COL_TITLE_DATE + " docto ext inv");
        }

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_COB);
        moSuscriptionsSet.add(SModConsts.CU_WAH);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
        moSuscriptionsSet.add(SModConsts.S_IOG);
        moSuscriptionsSet.add(SModConsts.S_IOG_EXP);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

        }
    }
}
