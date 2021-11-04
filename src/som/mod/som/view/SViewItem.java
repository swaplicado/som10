/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.util.Arrays;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores, Adrián Avilés
 * 2018-12-11, Sergio Flores: Adición de parámetros de fruta.
 * 2019-01-07, Sergio Flores: Adición de ajuste de rendimiento para parámetros de fruta.
 */
public class SViewItem extends SGridPaneView {

    public SViewItem(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, title);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(true);
        moPaneSettings.setDisableableApplying(true);
        moPaneSettings.setDeletableApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "v.id_item AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.name_sht, "
                + "v.ext_code, "
                + "v.ext_name, "
                + "v.unit_wei, "
                + "v.oil_yield_adj_per, "
                + "v.fruit_yield_adj_per, "
                + "v.den, "
                + "v.mfg_fg_per, "
                + "v.mfg_bp_per, "
                + "v.mfg_cu_per, "
                + "v.paq_name, "
                + "v.paq_wei, "
                + "v.rev_item_id, "
                + "v.b_paq, "
                + "v.b_lab, "
                + "un.code, "
                + "vt.code, "
                + "ict.code, "
                + "icl.code, "
                + "it.code, "
                + "v.amn_box, "
                + "v.umn_box, "
                + "v.b_amn, "
                + "v.b_umn, "
                + "v.b_umn_owm, "
                + "v.b_den, "
                + "v.b_iod_val, "
                + "v.b_ref_ind, "
                + "v.b_imp_per, "
                + "v.b_moi_per, "
                + "v.b_pro_per, "
                + "v.b_oil_per, "
                + "v.b_ole_per, "
                + "v.b_lin_per, "
                + "v.b_llc_per, "
                + "v.b_eru_per, "
                + "v.b_aci_per, "
                + "v.b_fruit, "
                + "v.b_prt_inp_tp, "
                + "v.b_can_upd AS " + SDbConsts.FIELD_CAN_UPD + ", "
                + "v.b_can_dis AS " + SDbConsts.FIELD_CAN_DIS + ", "
                + "v.b_can_del AS " + SDbConsts.FIELD_CAN_DEL + ", "
                + "v.b_dis AS " + SDbConsts.FIELD_IS_DIS + ", "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_item_src_1_n, "
                + "v.fk_item_src_2_n, "
                + "io.code AS f_item_code_1, "
                + "io.name AS f_item_name_1, "
                + "ir.code AS f_item_code_2, "
                + "ir.name AS f_item_name_2, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS un ON "
                + "v.fk_unit = un.id_unit "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_ITEM_TP) + " AS vt ON "
                + "v.fk_item_tp = vt.id_item_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_CT) + " AS ict ON "
                + "v.fk_inp_ct = ict.id_inp_ct "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_CL) + " AS icl ON "
                + "v.fk_inp_ct = icl.id_inp_ct AND v.fk_inp_cl = icl.id_inp_cl "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_TP) + " AS it ON "
                + "v.fk_inp_ct = it.id_inp_ct AND v.fk_inp_cl = it.id_inp_cl AND v.fk_inp_tp = it.id_inp_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS io ON "
                + "v.fk_item_src_1_n = io.id_item "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS ir ON "
                + "v.fk_item_src_2_n = ir.id_item "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.name, v.id_item ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = null;

        columns = new SGridColumnView[52];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.name_sht", SGridConsts.COL_TITLE_NAME + " corto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "v.ext_name", SGridConsts.COL_TITLE_NAME + " externo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "v.ext_code", SGridConsts.COL_TITLE_CODE + " externo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "v.rev_item_id", "Clave Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "vt.code", "Tipo ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ict.code", "Categoría insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "icl.code", "Clase insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "it.code", "Tipo insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "un.code", "Unidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.unit_wei", "Peso unitario (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.den", "Densidad (" + SSomConsts.DEN + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "f_item_name_1", "Ítem origen 1");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM,"f_item_code_1", "Ítem origen 1 código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "f_item_name_2", "Ítem origen 2");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM,"f_item_code_1", "Ítem origen 2 código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.mfg_fg_per", "Producto %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.mfg_bp_per", "Subproducto %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.mfg_cu_per", "Desecho %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_paq", "Aplica empaque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.paq_name", "Nombre empaque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.paq_wei", "Peso empaque (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_amn", "Envío automático mail");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.amn_box", "Destinatario(s) mail automático");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_umn", "Envío manual mail");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.umn_box", "Destinatario(s) mail manual");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_umn_owm", "Envío mail sólo si movimientos período");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_lab", "Análisis laboratorio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_imp_per", "Impurezas %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_moi_per", "Humedad %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_den", "Densidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_ref_ind", "Índice refracción (IR)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_iod_val", "Valor yodo (VI)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_ole_per", "Ácido oleico %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_lin_per", "Ácido linoleico %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_llc_per", "Ácido linolénico %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_eru_per", "Ácido erúcico %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_pro_per", "Proteína %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_oil_per", "Aceite %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_DISC, "v.oil_yield_adj_per", "Ajuste rendimiento aceite %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_aci_per", "Acidez %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_fruit", "Parámetros fruta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_DISC, "v.fruit_yield_adj_per", "Ajuste rendimiento fruta %");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_prt_inp_tp", "Tipo insumo impresión boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DIS, SGridConsts.COL_TITLE_IS_DIS);
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
        moSuscriptionsSet.add(SModConsts.SU_INP_CT);
        moSuscriptionsSet.add(SModConsts.SU_INP_CL);
        moSuscriptionsSet.add(SModConsts.SU_INP_TP);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
