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
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import som.mod.SModConsts;
import som.mod.som.db.SDbTicket;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewTicketExwUpdateLog extends SGridPaneView {
    
    /** Para filtrar por fecha de la modificación. */
    public static final int BY_UPD_DATE = 1;
    
    /** Para filtrar por fecha del boleto de báscula. */
    public static final int BY_TIC_DATE = 2;

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;
    private SPaneFilter moPaneFilterInputCategory;
    private SPaneFilter moPaneFilterItem;
    private SPaneFilter moPaneFilterTicketOrigin;
    private SPaneFilter moPaneFilterTicketDestination;
    private SPaneFilter moPaneFilterScale;
    
    /**
     * Ticket external warehouse updates log.
     * @param client GUI client.
     * @param gridSubtype Filter mode: BY_UPD_DATE or BY_TIC_DATE.
     * @param title View title.
     */
    public SViewTicketExwUpdateLog(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_TIC_EXW_UPD_LOG, gridSubtype, title);
        setRowButtonsEnabled(false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        
        moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "itm");
        moPaneFilterInputCategory = new SPaneFilter(this, SModConsts.SU_INP_CT);
        moPaneFilterItem = new SPaneFilter(this, SModConsts.SU_ITEM);
        moPaneFilterItem.initFilter(null);
        moPaneFilterTicketOrigin = new SPaneFilter(this, SModConsts.SU_TIC_ORIG);
        moPaneFilterTicketOrigin.initFilter(null);
        moPaneFilterTicketDestination = new SPaneFilter(this, SModConsts.SU_TIC_DEST);
        moPaneFilterTicketDestination.initFilter(null);
        moPaneFilterScale = new SPaneFilter(this, SModConsts.SU_SCA);
        moPaneFilterScale.initFilter(null);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterItem);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketOrigin);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketDestination);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterScale);
    }

    @Override
    public void prepareSqlQuery() {
        Object filter;
        String sqlWhere = "";

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "t.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            switch (mnGridSubtype) {
                case BY_UPD_DATE:
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.ts_usr", (SGuiDate) filter);
                    break;
                case BY_TIC_DATE:
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("t.dt", (SGuiDate) filter);
                    break;
                default:
                    miClient.showMsgBoxWarning(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_INP_CT);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "itm.fk_inp_ct = " + ((int[]) filter)[0] + " ";
        }

        filter = (int[]) moFiltersMap.get(SModConsts.SU_ITEM);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "t.fk_item = " + ((int[]) filter)[0] + " ";
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_ORIG);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "(v.fk_old_tic_orig = " + ((int[]) filter)[0] + " OR v.fk_new_tic_orig = " + ((int[]) filter)[0] + ") ";
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_DEST);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "(v.fk_old_tic_dest = " + ((int[]) filter)[0] + " OR v.fk_new_tic_dest = " + ((int[]) filter)[0] + ") ";
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_SCA);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "t.fk_sca = " + ((int[]) filter)[0] + " ";
        }
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if(!sqlFilter.isEmpty()) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + sqlFilter;
        }
        
        msSql = "SELECT "
                + "v.id_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_upd AS " + SDbConsts.FIELD_ID + "2, "
                + "t.num AS " + SDbConsts.FIELD_CODE + ", "
                + "t.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.id_upd, "
                + "v.note, "
                + "v.ts_usr, "
                + "t.num, "
                + "t.dt, "
                + "t.pla, "
                + "t.pla_cag, "
                + "t.drv, "
                + "t.ts_arr, "
                + "t.ts_dep, "
                + "t.wei_src, "         // Declared weight at origin
                + "t.wei_des_arr, "     // wda: Weigth at destiny at arrival
                + "t.wei_des_dep, "     // wdd: Weigth at destiny at departure
                + "t.wei_des_gro_r, "   // wdg: Weigth gross at destiny (= wda – wdd)
                + "t.wei_des_net_r, "   // wdn: Weigth net at destiny (= wdg – pwn)
                + "CASE WHEN t.freight_tic_tp = '" + SDbTicket.FRT_TIC_TP_FRT + "' THEN '" + SDbTicket.FRT_TIC_TP_FRT_DESC + "' WHEN t.freight_tic_tp = '" + SDbTicket.FRT_TIC_TP_DEP + "' THEN '" + SDbTicket.FRT_TIC_TP_DEP_DESC + "' ELSE '' END AS _frt_tic_tp, "
                + "t.b_rev_1, "
                + "t.b_rev_2, "
                + "t.b_tar, "
                + "t.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "sca.id_sca, "
                + "sca.code, "
                + "sca.name, "
                + "tst.id_tic_st, "
                + "tst.code, "
                + "tst.name, "
                + "itm.id_item, "
                + "itm.code, "
                + "itm.name, "
                + "prd.id_prod, "
                + "prd.code, "
                + "prd.name, "
                + "prd.name_trd, "
                + "src.id_inp_src, "
                + "src.code, "
                + "src.name, "
                + "otor.code, "
                + "otde.code, "
                + "oefor.code, "
                + "oefde.code, "
                + "ntor.code, "
                + "ntde.code, "
                + "nefor.code, "
                + "nefde.code, "
                + "wah.id_co, "
                + "wah.id_cob, "
                + "wah.id_wah, "
                + "wah.code, "
                + "wah.name, "
                + "fror.name, "
                + "frtic.num, "
                + "v.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.ts_usr AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "u.name AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC_EXW_UPD_LOG) + " AS v "
                + "INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t ON "
                + "v.id_tic = t.id_tic "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sca ON "
                + "t.fk_sca = sca.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_TIC_ST) + " AS tst ON "
                + "t.fk_tic_st = tst.id_tic_st "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS itm ON "
                + "t.fk_item = itm.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS prd ON "
                + "t.fk_prod = prd.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_SRC) + " AS src ON "
                + "t.fk_inp_src = src.id_inp_src "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_ORIG) + " AS otor ON "
                + "v.fk_old_tic_orig = otor.id_tic_orig "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_DEST) + " AS otde ON "
                + "v.fk_old_tic_dest = otde.id_tic_dest "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EXW_FAC) + " AS oefor ON "
                + "v.fk_old_exw_fac_orig = oefor.id_exw_fac "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EXW_FAC) + " AS oefde ON "
                + "v.fk_old_exw_fac_dest = oefde.id_exw_fac "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_ORIG) + " AS ntor ON "
                + "v.fk_new_tic_orig = ntor.id_tic_orig "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_DEST) + " AS ntde ON "
                + "v.fk_new_tic_dest = ntde.id_tic_dest "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EXW_FAC) + " AS nefor ON "
                + "v.fk_new_exw_fac_orig = nefor.id_exw_fac "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EXW_FAC) + " AS nefde ON "
                + "v.fk_new_exw_fac_dest = nefde.id_exw_fac "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS u ON "
                + "v.fk_usr = u.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " 
                + "t.fk_wah_unld_co_n = wah.id_co AND t.fk_wah_unld_cob_n = wah.id_cob AND t.fk_wah_unld_wah_n = wah.id_wah "                
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_FREIGHT_ORIG) + " AS fror ON " 
                + "t.fk_freight_orig_n = fror.id_freight_orig "                
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS frtic ON " 
                + "t.fk_freight_tic_n = frtic.id_tic "                
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + "ORDER BY sca.code, sca.id_sca, t.num, v.id_tic, v.id_upd ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        
        SGridColumnView[] columns = new SGridColumnView[41];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "t.num", "Boleto", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "t.dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "prd.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "prd.name_trd", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "itm.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "itm.code", "Ítem código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tst.name", "Estatus boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.id_upd", "No. modificación");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_usr", "Modificación");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.note", "Comentarios modificación", 150);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "otor.code", "Anterior procedencia boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "oefor.code", "Anterior almacén procedencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "otde.code", "Anterior destino boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "oefde.code", "Anterior almacén destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ntor.code", "Nueva procedencia boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "nefor.code", "Nuevo almacén procedencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ntde.code", "Nuevo destino boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "nefde.code", "Nuevo almacén destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "src.name", "Origen insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "t.pla", "Placas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "t.pla_cag", "Placas caja");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "t.drv", "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "t.ts_arr", "TS entrada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "t.ts_dep", "TS salida");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "t.b_tar", "Tarado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "t.wei_src", "Peso origen (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "t.wei_des_arr", "Peso entrada (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "t.wei_des_dep", "Peso salida (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "t.wei_des_gro_r", "Carga destino bruto (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "t.wei_des_net_r", "Carga destino neta (" + SSomConsts.KG + ")");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "t.b_rev_1", "1a pesada Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "t.b_rev_2", "2a pesada Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "wah.name", "Almacén descarga");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "wah.code", "Almacén descarga código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_frt_tic_tp", "Control fletes");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fror.name", "Origen flete");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "frtic.num", "Boleto flete", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_TIC);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.SU_INP_SRC);
        moSuscriptionsSet.add(SModConsts.SX_TIC_TARE);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
