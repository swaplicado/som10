/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod;

import javax.swing.JMenu;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistrySysFly;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiForm;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiReport;
import som.mod.som.db.SDbExternalWarehouse;
import som.mod.som.db.SDbGrindingLot;
import som.mod.som.db.SDbInputCategory;
import som.mod.som.db.SDbInputClass;
import som.mod.som.db.SDbInputSource;
import som.mod.som.db.SDbInputType;
import som.mod.som.db.SDbIodineValueRank;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbProducer;
import som.mod.som.db.SDbScale;
import som.mod.som.db.SDbUnit;
import som.mod.som.db.SSomConsts;
import som.mod.som.form.SFormExternalWarehouse;
import som.mod.som.form.SFormGrindingLot;
import som.mod.som.form.SFormInputCategory;
import som.mod.som.form.SFormInputClass;
import som.mod.som.form.SFormInputSource;
import som.mod.som.form.SFormInputType;
import som.mod.som.form.SFormIodineValueRank;
import som.mod.som.form.SFormItem;
import som.mod.som.form.SFormItemAlternative;
import som.mod.som.form.SFormProducer;
import som.mod.som.form.SFormScale;
import som.mod.som.view.SViewExternalWarehouse;
import som.mod.som.view.SViewGrindingLots;
import som.mod.som.view.SViewInputCategory;
import som.mod.som.view.SViewInputClass;
import som.mod.som.view.SViewInputSource;
import som.mod.som.view.SViewInputType;
import som.mod.som.view.SViewIodineValueRank;
import som.mod.som.view.SViewItem;
import som.mod.som.view.SViewItemAlternative;
import som.mod.som.view.SViewProducer;
import som.mod.som.view.SViewScale;

/**
 * 
 * @author Sergio Flores, Isabel Servín
 */
public class SModuleSom extends SGuiModule {

    private SFormExternalWarehouse moFormExternalWarehouse;
    private SFormInputCategory moFormInputCategory;
    private SFormInputClass moFormInputClass;
    private SFormInputType moFormInputType;
    private SFormInputSource moformFormInputSource;
    private SFormItem moFormItem;
    private SFormGrindingLot SFormLot;
    private SFormScale moFormScale;
    private SFormProducer moFormProducer;
    private SFormIodineValueRank moFormIodineValueRank;
    private SFormItemAlternative moFormItemAlternative;

    public SModuleSom(SGuiClient client) {
        super(client, SModConsts.MOD_SOM, SLibConsts.UNDEFINED);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry(int type, SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.SS_ITEM_TP:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_item_tp = " + pk[0] + " "; }
                };
                break;
            case SModConsts.SU_EXT_WAH:
                registry = new SDbExternalWarehouse();
                break;
            case SModConsts.SU_INP_CT:
                registry = new SDbInputCategory();
                break;
            case SModConsts.SU_INP_CL:
                registry = new SDbInputClass();
                break;
            case SModConsts.SU_INP_TP:
                registry = new SDbInputType();
                break;
            case SModConsts.SU_INP_SRC:
                registry = new SDbInputSource();
                break;
            case SModConsts.SU_UNIT:
                registry = new SDbUnit();
                break;
            case SModConsts.SU_ITEM:
            case SModConsts.SX_ITEM_ALT:
                registry = new SDbItem();
                break;
            case SModConsts.S_GRINDING_LOT:
                registry = new SDbGrindingLot();
                break;
            case SModConsts.SU_SCA:
                registry = new SDbScale();
                break;
            case SModConsts.SU_PROD:
                registry = new SDbProducer();
                break;
            case SModConsts.SU_IOD_VAL_RANK:
                registry = new SDbIodineValueRank();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(int type, int subtype, SGuiParams params) {
        String sql = "";
        String aux = "";
        Object value = null;
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.SS_ITEM_TP:
                settings = new SGuiCatalogueSettings("Tipo de ítem", 1);
                sql = "SELECT id_item_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.SU_EXT_WAH:
                settings = new SGuiCatalogueSettings("Almacén sistema externo", 1);
                sql = "SELECT id_ext_wah AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_ext_wah ";
                break;
            case SModConsts.SU_INP_CT:
                settings = new SGuiCatalogueSettings("Categoría de insumo", 1);
                sql = "SELECT id_inp_ct AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_inp_ct ";
                break;
            case SModConsts.SU_INP_CL:
                settings = new SGuiCatalogueSettings("Clase de insumo", 2, 1);
                sql = "SELECT id_inp_ct AS " + SDbConsts.FIELD_ID + "1, id_inp_cl AS " + SDbConsts.FIELD_ID + "2, name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "id_inp_ct AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_inp_ct, id_inp_cl ";
                break;
            case SModConsts.SU_INP_TP:
                settings = new SGuiCatalogueSettings("Tipo de insumo", 3, 2);
                sql = "SELECT id_inp_ct AS " + SDbConsts.FIELD_ID + "1, id_inp_cl AS " + SDbConsts.FIELD_ID + "2, id_inp_tp AS " + SDbConsts.FIELD_ID + "3, name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "id_inp_ct AS " + SDbConsts.FIELD_FK + "1, id_inp_cl AS " + SDbConsts.FIELD_FK + "2 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_inp_ct, id_inp_cl, id_inp_tp ";
                break;
            case SModConsts.SU_INP_CL_ALL:
                settings = new SGuiCatalogueSettings("Clase de insumo", 2);
                sql = "SELECT cl.id_inp_ct AS " + SDbConsts.FIELD_ID + "1, cl.id_inp_cl AS " + SDbConsts.FIELD_ID + "2, CONCAT(ct.name, ' - ', cl.name) AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM su_inp_cl AS cl " 
                        + "INNER JOIN su_inp_ct AS ct ON cl.id_inp_ct = ct.id_inp_ct;";
                break;
            case SModConsts.SU_INP_TP_ALL:
                settings = new SGuiCatalogueSettings("Tipo de insumo", 3);
                sql = "SELECT tp.id_inp_ct AS " + SDbConsts.FIELD_ID + "1, tp.id_inp_cl AS " + SDbConsts.FIELD_ID + "2, tp.id_inp_tp AS " + SDbConsts.FIELD_ID + "3, CONCAT(ct.name, ' - ', cl.name, ' - ', tp.name) AS " + SDbConsts.FIELD_ITEM + " " 
                        + "FROM su_inp_tp AS tp " 
                        + "INNER JOIN su_inp_cl AS cl ON tp.id_inp_cl = cl.id_inp_cl " 
                        + "INNER JOIN su_inp_ct AS ct ON cl.id_inp_ct = ct.id_inp_ct;";
                break;
            case SModConsts.SU_INP_SRC:
                settings = new SGuiCatalogueSettings("Origen de insumo", 1);
                sql = "SELECT id_inp_src AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 "
                        + (subtype == SSomConsts.OPC_ALL ? "" : "AND id_inp_src <> " + SModSysConsts.SU_INP_SRC_NA + " ")
                        + (params == null ? "" : "AND fk_inp_ct = " + params.getType() + " ")
                        + "ORDER BY name, id_inp_src ";
                break;
            case SModConsts.SU_UNIT:
                settings = new SGuiCatalogueSettings("Unidad", 1);
                settings.setCodeApplying(true);

                sql = "SELECT id_unit AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY sort ";
                break;
            case SModConsts.SU_ITEM:
                settings = new SGuiCatalogueSettings("Ítem", 1, 2, SLibConsts.DATA_TYPE_TEXT);
                settings.setCodeApplying(true);

                switch(subtype) {
                    case SModSysConsts.SS_ITEM_TP_RM:
                        aux += "AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_RM + " ";
                        settings.setCatalogueName("Materia prima");
                        break;
                    case SModSysConsts.SS_ITEM_TP_FG:
                        aux += "AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " ";
                        settings.setCatalogueName("Producto terminado");
                        break;
                    case SModSysConsts.SS_ITEM_TP_BP:
                        aux += "AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_BP + " ";
                        settings.setCatalogueName("Subproducto");
                        break;
                    case SModSysConsts.SS_ITEM_TP_CU:
                        aux += "AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_CU + " ";
                        settings.setCatalogueName("Desecho");
                        break;
                    case SModSysConsts.SX_ITEM_TP_FRUIT:
                        aux += "AND i.b_fruit ";
                        settings.setCatalogueName("Fruta");
                    default:
                }

                if (params != null) {
                    switch (params.getType()) {
                        case SModSysConsts.SX_IOD_VAL:
                            aux += "AND i.b_iod_val = 1 ";
                            break;
                        case SModSysConsts.SX_OLE_PER:
                            aux += "AND i.b_ole_per = 1 ";
                            break;
                        case SModSysConsts.SX_LIN_PER:
                            aux += "AND i.b_lin_per = 1 ";
                            break;
                        case SModSysConsts.SX_LLC_PER:
                            aux += "AND i.b_llc_per = 1 ";
                            break;
                        default:
                    }

                    value = params.getParamsMap().get(SModConsts.SX_EXT_ITEM);
                    if (value != null) {
                        aux += "AND i.fk_ext_item_n = " + value + " ";
                    }

                    value = params.getParamsMap().get(SModConsts.SU_INP_CT);
                    if (value != null) {
                        aux += "AND i.fk_inp_ct = " + ((int[]) value)[0] + " ";
                    }

                    value = params.getParamsMap().get(SModConsts.SU_INP_CL);
                    if (value != null) {
                        aux += "AND i.fk_inp_ct = " + ((int[]) value)[0] + " AND i.fk_inp_cl = " + ((int[]) value)[1] + " ";
                    }

                    value = params.getParamsMap().get(SModConsts.SU_INP_TP);
                    if (value != null) {
                        aux += "AND i.fk_inp_ct = " + ((int[]) value)[0] + " AND i.fk_inp_cl = " + ((int[]) value)[1] + " AND i.fk_inp_tp = " + ((int[]) value)[2] + " ";
                    }

                    value = params.getParamsMap().get(SModConsts.SU_UNIT);
                    if (value != null) {
                        aux += "AND i.fk_unit = " + ((int[]) value)[0] + " ";
                    }
                    
                    value = params.getParamsMap().get(SModConsts.SX_ITEM_ALT);
                    if (value != null) {
                        aux += "AND i.id_item IN (" + value + ") ";
                    }
                }

                sql = "SELECT i.id_item AS " + SDbConsts.FIELD_ID + "1, i.name AS " + SDbConsts.FIELD_ITEM + ", i.code AS " + SDbConsts.FIELD_CODE + ", "
                        + "i.fk_unit AS " + SDbConsts.FIELD_FK + "1, u.fk_ext_unit_n AS " + SDbConsts.FIELD_FK + "2, u.code AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS i "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON i.fk_unit = u.id_unit "
                        + "WHERE i.b_del = 0 AND i.b_dis = 0 " + aux
                        + "ORDER BY i.name, i.id_item ";
                break;
            case SModConsts.SU_SCA:
                settings = new SGuiCatalogueSettings("Báscula", 1);
                sql = "SELECT id_sca AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_sca ";
                break;
            case SModConsts.SU_PROD:
                settings = new SGuiCatalogueSettings("Proveedor", 1);
                settings.setCodeApplying(true);
                switch (subtype) {
                    case SModConsts.SU_SEAS_PROD:
                        sql = "SELECT DISTINCT p.id_prod AS " + SDbConsts.FIELD_ID + "1, p.name AS " + SDbConsts.FIELD_ITEM + ", p.code AS " + SDbConsts.FIELD_CODE + " "
                                + "FROM su_seas_prod AS sp "
                                + "INNER JOIN su_seas as s on sp.id_seas = s.id_seas "
                                + "INNER JOIN su_prod AS p ON sp.id_prod = p.id_prod "
                                + "WHERE NOT sp.b_del AND NOT s.b_del AND NOT p.b_del AND NOT p.b_dis "
                                + (params == null ? "" : "AND sp.id_item = " + params.getParamsMap().get(SModConsts.SU_ITEM) + " "
                                + "AND '" + SLibUtils.DbmsDateFormatDate.format(params.getParamsMap().get(SModConsts.SU_SEAS)) + "' BETWEEN s.dt_sta AND s.dt_end " 
                                + (params.getParamsMap().get(SModConsts.SU_REG) == null ? "" : "AND sp.id_reg = " + params.getParamsMap().get(SModConsts.SU_REG) + " "))
                                + "ORDER BY p.name, p.id_prod";
                        break;
                    case SLibConsts.UNDEFINED:
                        sql = "SELECT id_prod AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                                + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_prod ";
                        break;
                }
                break;
            case SModConsts.S_GRINDING_LOT:
                settings = new SGuiCatalogueSettings("Lotes", 1);
                settings.setCodeApplying(false);
                
                if (params != null) {
                    value = params.getParamsMap().get(SModConsts.SX_EXT_ITEM);
                    if (value != null) {
                        aux += "AND fk_item_id = " + ((int[]) value)[0] + " ";
                    }
                }
                
                sql = "SELECT id_lot AS " + SDbConsts.FIELD_ID + "1, lot AS " + SDbConsts.FIELD_ITEM + ", lot AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 " + aux + " ORDER BY id_lot DESC, lot ASC;";
                break;
            case SModConsts.SU_GRINDING_PARAM:
                settings = new SGuiCatalogueSettings("Parámetros", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_parameter AS " + SDbConsts.FIELD_ID + "1, parameter AS " + SDbConsts.FIELD_ITEM + ", param_code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY parameter, id_parameter ";
                break;
            case SModConsts.SS_LINK_CFG_ITEMS:
                settings = new SGuiCatalogueSettings("Ítems configurados", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_item AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " WHERE b_del = 0 AND id_item IN "
                        + "(SELECT DISTINCT fk_item_id FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_LINK_ITEM_PARAM) + " AS glip WHERE glip.b_del = 0 ";
                if (params != null) {
                    value = params.getParamsMap().get(SModConsts.SX_EXT_PLA);
                    if (value != null) {
                        sql += "AND glip.fk_pla_co = " + ((int[]) value)[0] + " AND glip.fk_pla_cob = " + ((int[]) value)[1] + " AND glip.fk_pla_pla = " + ((int[]) value)[2] + " ";
                    }
                }
                sql += ") ORDER BY name, id_item ";
                break;
            case SModConsts.SU_IOD_VAL_RANK:
                settings = new SGuiCatalogueSettings("Rango de yodo", 2, 1);
                sql = "SELECT s.id_item AS " + SDbConsts.FIELD_ID + "1, s.id_rank AS " + SDbConsts.FIELD_ID + "2, CONCAT(IF(s.lim_low_n IS NULL, 0, ROUND(s.lim_low_n, 4)), ' - ', IF(s.lim_top_n IS NULL, 0, ROUND(s.lim_top_n, 4))) AS " + SDbConsts.FIELD_ITEM + ", "
                        + "s.id_item AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_IOD_VAL_RANK) + " AS s "
                        + "WHERE s.b_del = 0 AND s.b_dis = 0 ORDER BY s.lim_low_n, s.lim_top_n, s.id_item, s.id_rank ";
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (settings != null) {
            settings.setSql(sql);

            if (params != null && params.getParamsMap().get(SGuiConsts.PARAM_ITEM) != null) {
                settings.setSelectionItemApplying((Boolean) params.getParamsMap().get(SGuiConsts.PARAM_ITEM));
            }
        }

        return settings;
    }

    @Override
    public SGridPaneView getView(int type, int subtype, SGuiParams params) {
        SGridPaneView view = null;

        switch (type) {
            case SModConsts.SU_EXT_WAH:
                view = new SViewExternalWarehouse(miClient, "Almacenes sist. externo");
                break;
            case SModConsts.SU_INP_CT:
                view = new SViewInputCategory(miClient, "Categorías insumo");
                break;
            case SModConsts.SU_INP_CL:
                view = new SViewInputClass(miClient, "Clases insumo");
                break;
            case SModConsts.SU_INP_TP:
                view = new SViewInputType(miClient, "Tipos insumo");
                break;
            case SModConsts.SU_INP_SRC:
                view = new SViewInputSource(miClient, "Orígenes insumos");
                break;
            case SModConsts.SU_ITEM:
                view = new SViewItem(miClient, "Ítems");
                break;
            case SModConsts.S_GRINDING_LOT:
                view = new SViewGrindingLots(miClient, "Lotes");
                break;
            case SModConsts.SU_SCA:
                view = new SViewScale(miClient, "Básculas");
                break;
            case SModConsts.SU_PROD:
                view = new SViewProducer(miClient, "Proveedores");
                break;
            case SModConsts.SU_IOD_VAL_RANK:
                view = new SViewIodineValueRank(miClient, "Rangos yodo");
                break;
            case SModConsts.SX_ITEM_ALT:
                view = new SViewItemAlternative(miClient, "Ítems alternos");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiForm getForm(int type, int subtype, SGuiParams params) {
        SGuiForm form = null;

        switch (type) {
            case SModConsts.SU_EXT_WAH:
                if (moFormExternalWarehouse == null) { moFormExternalWarehouse = new SFormExternalWarehouse(miClient, "Almacén sistema externo"); }
                form = moFormExternalWarehouse;
                break;
            case SModConsts.SU_INP_CT:
                if (moFormInputCategory == null) moFormInputCategory = new SFormInputCategory(miClient, "Categoría insumo");
                form = moFormInputCategory;
                break;
            case SModConsts.SU_INP_CL:
                if (moFormInputClass == null) moFormInputClass = new SFormInputClass(miClient, "Clase insumo");
                form = moFormInputClass;
                break;
            case SModConsts.SU_INP_TP:
                if (moFormInputType == null) moFormInputType = new SFormInputType(miClient, "Tipo insumo");
                form = moFormInputType;
                break;
            case SModConsts.SU_INP_SRC:
                if (moformFormInputSource == null) moformFormInputSource = new SFormInputSource(miClient, "Origen insumo");
                form = moformFormInputSource;
                break;
            case SModConsts.SU_ITEM:
                if (moFormItem == null) moFormItem = new SFormItem(miClient, "Ítem");
                form = moFormItem;
                break;
            case SModConsts.S_GRINDING_LOT:
                if (SFormLot == null) SFormLot = new SFormGrindingLot(miClient, "Lote");
                form = SFormLot;
                break;
            case SModConsts.SU_SCA:
                if (moFormScale == null) moFormScale = new SFormScale(miClient, "Báscula");
                form = moFormScale;
                break;
            case SModConsts.SU_PROD:
                if (moFormProducer == null) moFormProducer = new SFormProducer(miClient, "Proveedor");
                form = moFormProducer;
                break;
            case SModConsts.SU_IOD_VAL_RANK:
                if (moFormIodineValueRank == null) moFormIodineValueRank = new SFormIodineValueRank(miClient, "Rango de yodo");
                form = moFormIodineValueRank;
                break;
            case SModConsts.SX_ITEM_ALT:
                if(moFormItemAlternative == null) moFormItemAlternative = new SFormItemAlternative(miClient, "Ítem alterno");
                form = moFormItemAlternative;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
