/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.util.Arrays;
import java.util.Date;
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
import som.mod.SModConsts;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewTicketsLaboratoryTestFruit extends SGridPaneView {

    private SGridFilterDateRange moFilterDateRange;

    public SViewTicketsLaboratoryTestFruit(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_TIC_LAB_TEST_FRUIT, 0, title);
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);

        initComponetsCustom();
    }

    private void initComponetsCustom() {
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] { SLibTimeUtils.getBeginOfMonth(miClient.getSession().getWorkingDate()), SLibTimeUtils.getEndOfMonth(miClient.getSession().getWorkingDate()) });

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);

        filter = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE);
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDateRange("t.dt", (Date[]) filter);

        msSql = "SELECT "
                + "t.id_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "t.num, "
                + "t.dt, "
                + "sca.code, "
                + "sca.name, "
                + "i.code AS " + SDbConsts.FIELD_CODE + ", "
                + "i.name AS " + SDbConsts.FIELD_NAME + ", "
                + "src.code, "
                + "src.name, "
                + "l.id_lab, "
                + "l.num, "
                + "lt.id_test, "
                + "lt.fruit_class, "
                + "lt.fruit_ripe, "
                + "lt.fruit_wei_total, "
                + "lt.fruit_wei_peel_pit, "
                + "@pulp_wei := lt.fruit_wei_total - lt.fruit_wei_peel_pit AS _fruit_wei_pulp, "
                + "1 - lt.fruit_pulp_hum_per AS _pulp_dry_matter_per, "
                + "lt.fruit_pulp_hum_per AS _pulp_hum_per, "
                + "1.0 - lt.fruit_pulp_hum_per - lt.fruit_pulp_oil_per AS _pulp_sol_per, "
                + "lt.fruit_pulp_oil_per AS _pulp_oil_per, "
                + "@fruit_hum_wei := @pulp_wei * lt.fruit_pulp_hum_per AS _fruit_hum_wei, "
                + "@fruit_sol_wei := @pulp_wei * (1 - lt.fruit_pulp_hum_per - lt.fruit_pulp_oil_per) + lt.fruit_wei_peel_pit AS _fruit_sol_wei, "
                + "@fruit_oil_wei := @pulp_wei * lt.fruit_pulp_oil_per AS _fruit_oil_wei, "
                + "IF(lt.fruit_wei_total = 0, 0.0, @fruit_hum_wei / lt.fruit_wei_total) AS _fruit_hum_per, "
                + "IF(lt.fruit_wei_total = 0, 0.0, @fruit_sol_wei / lt.fruit_wei_total) AS _fruit_sol_per, "
                + "IF(lt.fruit_wei_total = 0, 0.0, @fruit_oil_wei / lt.fruit_wei_total) AS _fruit_oil_per "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sca ON T.fk_sca = sca.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON t.fk_item = i.id_item AND NOT t.b_del "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_SRC) + " AS src ON t.fk_inp_src = src.id_inp_src "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_LAB) + " AS l ON t.fk_lab_n = l.id_lab AND NOT l.b_del "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_LAB_TEST) + " AS lt ON l.id_lab = lt.id_lab "
                + "WHERE i.b_fruit " + (sql.isEmpty() ? "" : "AND " + sql)
                + "ORDER BY t.num, t.id_tic;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = null;

        columns = new SGridColumnView[23];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "t.num", "Boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "t.dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, SDbConsts.FIELD_NAME, "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "src.name", "Origen insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "l.num", "Análisis lab");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "lt.id_test", "Prueba lab");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "lt.fruit_class", "Clase fruta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "lt.fruit_ripe", "Grado madurez");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "lt.fruit_wei_total", "Peso fruta (" + SSomConsts.G + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "lt.fruit_wei_peel_pit", "Peso cáscara + hueso (" + SSomConsts.G + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_fruit_wei_pulp", "Peso pulpa (" + SSomConsts.G + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "_pulp_dry_matter_per", "Pulpa: materia seca");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "_pulp_hum_per", "Pulpa: humedad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "_pulp_sol_per", "Pulpa: sólidos");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "_pulp_oil_per", "Pulpa: aceite");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_fruit_hum_wei", "Fruta: humedad (" + SSomConsts.G + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_fruit_sol_wei", "Fruta: sólidos (" + SSomConsts.G + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_fruit_oil_wei", "Fruta: aceite (" + SSomConsts.G + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "_fruit_hum_per", "Fruta: humedad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "_fruit_sol_per", "Fruta: sólidos");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "_fruit_oil_per", "Fruta: aceite");

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.SU_INP_SRC);
        moSuscriptionsSet.add(SModConsts.SU_SEAS);
        moSuscriptionsSet.add(SModConsts.SU_REG);
        moSuscriptionsSet.add(SModConsts.S_LAB);
        moSuscriptionsSet.add(SModConsts.SX_TIC_TARE);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
