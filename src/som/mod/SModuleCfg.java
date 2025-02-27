/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.lib.SLibConsts;
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
import som.mod.cfg.db.SDbBranchPlant;
import som.mod.cfg.db.SDbBranchWarehouse;
import som.mod.cfg.db.SDbCompany;
import som.mod.cfg.db.SDbCompanyBranch;
import som.mod.cfg.db.SDbDivision;
import som.mod.som.db.SDbGrindingParameter;
import som.mod.som.db.SDbGrindingLinkItemParameter;
import som.mod.cfg.db.SDbProductionLine;
import som.mod.cfg.db.SDbReportingGroup;
import som.mod.cfg.db.SDbUser;
import som.mod.cfg.db.SDbUserInputCategory;
import som.mod.cfg.db.SDbUserRight;
import som.mod.cfg.db.SDbUserScale;
import som.mod.cfg.db.SDbYear;
import som.mod.cfg.db.SDbYearPeriod;
import som.mod.cfg.form.SFormBranchPlant;
import som.mod.cfg.form.SFormBranchWarehouse;
import som.mod.cfg.form.SFormCompany;
import som.mod.cfg.form.SFormCompanyBranch;
import som.mod.som.form.SFormGrindingParameters;
import som.mod.som.form.SFormGrindingLinkItemParam;
import som.mod.cfg.form.SFormProductionLine;
import som.mod.cfg.form.SFormReportingGroup;
import som.mod.cfg.form.SFormUser;
import som.mod.cfg.form.SFormUserAlternative;
import som.mod.cfg.form.SFormYear;
import som.mod.cfg.view.SViewBranchPlant;
import som.mod.cfg.view.SViewBranchWarehouse;
import som.mod.cfg.view.SViewCompany;
import som.mod.cfg.view.SViewCompanyBranch;
import som.mod.cfg.view.SViewProductionLine;
import som.mod.som.view.SViewGrindingLinkItmParam;
import som.mod.som.view.SViewGrindingParameters;
import som.mod.cfg.view.SViewReportingGroup;
import som.mod.cfg.view.SViewUser;
import som.mod.cfg.view.SViewUserAlternative;
import som.mod.cfg.view.SViewUserAlternativeRight;
import som.mod.cfg.view.SViewUserInputCategories;
import som.mod.cfg.view.SViewUserRight;
import som.mod.cfg.view.SViewUserScale;
import som.mod.cfg.view.SViewYear;
import som.mod.som.db.SDbItem;

/**
 * 
 * @author Sergio Flores, Isabel Servín
 * 2019-01-17, Sergio Flores: Cambio de ubicación del catálogo de agrupadores de reporte, del módulo configuración al módulo materias primas.
 */
public class SModuleCfg extends SGuiModule implements ActionListener {

    private JMenu mjCat;
    private JMenuItem mjCatCompany;
    private JMenuItem mjCatCompanyBranch;
    private JMenuItem mjCatProductionLine;
    private JMenuItem mjCatWarehouse;
    private JMenuItem mjCatPlant;
    private JMenuItem mjCatParameters;
    private JMenuItem mjCatLinkItmParams;
    private JMenuItem mjCatScale;
    private JMenu mjUsr;
    private JMenuItem mjUsrUser;
    private JMenuItem mjUsrUserRights;
    private JMenuItem mjUsrUserScales;
    private JMenuItem mjUsrUserInputCategories;
    private JMenuItem mjUsrUserAlt;
    private JMenuItem mjUsrUserAltRights;
    private JMenu mjCtr;
    private JMenuItem mjCtrYear;

    private SFormCompany moFormCompany;
    private SFormCompanyBranch moFormCompanyBranch;
    private SFormProductionLine moFormProductionLine;
    private SFormBranchWarehouse moFormWarehouse;
    private SFormBranchPlant moFormPlant;
    private SFormGrindingParameters moFormGrindingParameters;
    private SFormGrindingLinkItemParam moFormLinkItemParam;
    private SFormReportingGroup moFormReportingGroup;
    private SFormUser moFormUser;
    private SFormYear moFormYear;
    private SFormUserAlternative moFormUserAlternative;

    public SModuleCfg(SGuiClient client) {
        super(client, SModConsts.MOD_CFG, SLibConsts.UNDEFINED);
        initComponents();
    }

    private void initComponents() {
        mjCat = new JMenu("Catálogos");
        mjCatCompany = new JMenuItem("Empresas");
        mjCatCompanyBranch = new JMenuItem("Sucursales");
        mjCatProductionLine = new JMenuItem("Líneas de producción");
        mjCatWarehouse = new JMenuItem("Almacenes");
        mjCatPlant = new JMenuItem("Plantas");
        mjCatParameters = new JMenuItem("Parámetros de molienda");
        mjCatLinkItmParams = new JMenuItem("Parámetros vs ítems");
        mjCatScale = new JMenuItem("Básculas");

        mjCat.add(mjCatCompany);
        mjCat.add(mjCatCompanyBranch);
        mjCat.add(mjCatProductionLine);
        mjCat.add(mjCatWarehouse);
        mjCat.add(mjCatPlant);
        mjCat.add(mjCatParameters);
        mjCat.add(mjCatLinkItmParams);
        mjCat.addSeparator();
        mjCat.add(mjCatScale);

        mjCatCompany.addActionListener(this);
        mjCatCompanyBranch.addActionListener(this);
        mjCatProductionLine.addActionListener(this);
        mjCatWarehouse.addActionListener(this);
        mjCatPlant.addActionListener(this);
        mjCatParameters.addActionListener(this);
        mjCatLinkItmParams.addActionListener(this);
        mjCatScale.addActionListener(this);

        mjCat.setEnabled(miClient.getSession().getUser().isAdministrator());
        mjCatCompany.setEnabled(miClient.getSession().getUser().isSupervisor());

        mjUsr = new JMenu("Usuarios");
        mjUsrUser = new JMenuItem("Usuarios");
        mjUsrUserRights = new JMenuItem("Usuarios vs. derechos (Q)");
        mjUsrUserScales = new JMenuItem("Usuarios vs. básculas (Q)");
        mjUsrUserInputCategories = new JMenuItem("Usuarios vs. categorías de insumo (Q)");
        mjUsrUserAlt = new JMenuItem("Usuarios alternos");
        mjUsrUserAltRights = new JMenuItem("Usuarios alternos vs. derechos (Q)");

        mjUsr.add(mjUsrUser);
        mjUsr.addSeparator();
        mjUsr.add(mjUsrUserRights);
        mjUsr.add(mjUsrUserScales);
        mjUsr.add(mjUsrUserInputCategories);
        mjUsr.addSeparator();
        mjUsr.add(mjUsrUserAlt);
        mjUsr.addSeparator();
        mjUsr.add(mjUsrUserAltRights);

        mjUsrUser.addActionListener(this);
        mjUsrUserRights.addActionListener(this);
        mjUsrUserScales.addActionListener(this);
        mjUsrUserInputCategories.addActionListener(this);
        mjUsrUserAlt.addActionListener(this);
        mjUsrUserAltRights.addActionListener(this);

        mjUsr.setEnabled(miClient.getSession().getUser().isSupervisor());

        mjCtr = new JMenu("Control interno");
        mjCtrYear = new JMenuItem("Ejercicios y períodos");

        mjCtr.add(mjCtrYear);

        mjCtrYear.addActionListener(this);
        
        mjCtr.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_PER_OC));
    }

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjUsr, mjCtr };
    }

    @Override
    public SDbRegistry getRegistry(int type, SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.CS_WAH_TP:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_wah_tp = " + pk[0] + " "; }
                };
            case SModConsts.CS_PLA_TP:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_pla_tp = " + pk[0] + " "; }
                };
            case SModConsts.CS_USR_TP:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_usr_tp = " + pk[0] + " "; }
                };
            case SModConsts.CS_RIG:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_rig = " + pk[0] + " "; }
                };
            case SModConsts.CU_CO:
                registry = new SDbCompany();
                break;
            case SModConsts.CU_COB:
                registry = new SDbCompanyBranch();
                break;
            case SModConsts.CU_PROD_LINES:
                registry = new SDbProductionLine();
                break;
            case SModConsts.CU_WAH:
                registry = new SDbBranchWarehouse();
                break;
            case SModConsts.CU_PLA:
                registry = new SDbBranchPlant();
                break;
            case SModConsts.SU_GRINDING_PARAM:
                registry = new SDbGrindingParameter();
                break;
            case SModConsts.SU_GRINDING_LINK_ITEM_PARAM:
                registry = new SDbGrindingLinkItemParameter();
                break;
            case SModConsts.SS_LINK_CFG_ITEMS:
                registry = new SDbItem();
                break;
            case SModConsts.CU_YEAR:
                registry = new SDbYear();
                break;
            case SModConsts.CU_YEAR_PER:
                registry = new SDbYearPeriod();
                break;
            case SModConsts.CU_LAN:
                registry = null;
                break;
            case SModConsts.CU_USR:
                registry = new SDbUser();
                break;
            case SModConsts.CU_USR_RIG:
                registry = new SDbUserRight();
                break;
            case SModConsts.CU_USR_SCA:
                registry = new SDbUserScale();
                break;
            case SModConsts.CU_USR_INP_CT:
                registry = new SDbUserInputCategory();
                break;
            case SModConsts.CU_DIV:
                registry = new SDbDivision();
                break;
            case SModConsts.CU_REP_GRP:
                registry = new SDbReportingGroup();
                break;
            case SModConsts.CU_USR_ALT_RIG:
                registry = new SDbUser();
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
        Object value;
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.CS_WAH_TP:
                settings = new SGuiCatalogueSettings("Tipo de almacén", 1);
                sql = "SELECT id_wah_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.CS_WAH_ORI:
                settings = new SGuiCatalogueSettings("Orientación del almacén", 1);
                sql = "SELECT id_wah_ori AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.CS_WAH_CALC_TP:
                settings = new SGuiCatalogueSettings("Tipo de cálculo de almacén", 1);
                sql = "SELECT id_wah_calc_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.CS_PLA_TP:
                settings = new SGuiCatalogueSettings("Tipo de planta", 1);
                sql = "SELECT id_pla_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.CS_USR_TP:
                settings = new SGuiCatalogueSettings("Tipo de usuario", 1);
                sql = "SELECT id_usr_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.CS_RIG:
                settings = new SGuiCatalogueSettings("Derecho de usuario", 1);
                sql = "SELECT id_rig AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.CU_CO:
                settings = new SGuiCatalogueSettings("Empresa", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_co AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_co ";
                break;
            case SModConsts.CU_COB:
                settings = new SGuiCatalogueSettings("Sucursal", 2, 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_co AS " + SDbConsts.FIELD_ID + "1, id_cob AS " + SDbConsts.FIELD_ID + "2, name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "code AS " + SDbConsts.FIELD_CODE + ", id_co AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_co, id_cob ";
                break;
            case SModConsts.CU_WAH:
                switch (subtype) {
                    case SModSysConsts.CS_WAH_TP_TAN:
                    case SModSysConsts.CS_WAH_TP_TAN_MFG:

                        aux += "AND fk_wah_tp IN (";
                        value = params.getParamsMap().get(SModSysConsts.CS_WAH_TP_TAN);
                        if (value != null) {
                            aux += "" + value + "";
                        }

                        aux += (value != null ? ", " : "");
                        value = params.getParamsMap().get(SModSysConsts.CS_WAH_TP_TAN_MFG);
                        if (value != null) {
                            aux += "" + value + "";
                        }

                        aux += ") ";

                        settings = new SGuiCatalogueSettings("Tanque" + (subtype == SModSysConsts.CS_WAH_TP_TAN_MFG ? " producción" : ""), 3, 2, SLibConsts.DATA_TYPE_DEC);
                        settings.setCodeApplying(true);
                        sql = "SELECT id_co AS " + SDbConsts.FIELD_ID + "1, id_cob AS " + SDbConsts.FIELD_ID + "2, id_wah AS " + SDbConsts.FIELD_ID + "3, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + ", "
                                + "code AS " + SDbConsts.FIELD_CODE + ", id_co AS " + SDbConsts.FIELD_FK + "1, id_cob AS " + SDbConsts.FIELD_FK + "2, "
                                + "dim_heig AS " + SDbConsts.FIELD_COMP + " "
                                + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 " + aux + " "
                                + "ORDER BY code, name, id_co, id_cob, id_wah ";
                        break;
                    case SModConsts.SX_TIC_WAH_UNLD:
                        settings = new SGuiCatalogueSettings("Almacén", 3, 2);
                        sql = "SELECT id_co AS " + SDbConsts.FIELD_ID + "1, id_cob AS " + SDbConsts.FIELD_ID + "2, id_wah AS " + SDbConsts.FIELD_ID + "3, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + ", "
                                + "code AS " + SDbConsts.FIELD_CODE + ", id_co AS " + SDbConsts.FIELD_FK + "1, id_cob AS " + SDbConsts.FIELD_FK + "2 "
                                + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 "
                                + "AND fk_wah_tp IN (1, 2) "
                                + "ORDER BY code, name, id_co, id_cob, id_wah ";
                        break;
                    default:
                        settings = new SGuiCatalogueSettings("Almacén", 3, 2);
                        settings.setCodeApplying(true);
                        sql = "SELECT id_co AS " + SDbConsts.FIELD_ID + "1, id_cob AS " + SDbConsts.FIELD_ID + "2, id_wah AS " + SDbConsts.FIELD_ID + "3, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + ", "
                                + "code AS " + SDbConsts.FIELD_CODE + ", id_co AS " + SDbConsts.FIELD_FK + "1, id_cob AS " + SDbConsts.FIELD_FK + "2 "
                                + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 "
                                + "ORDER BY code, name, id_co, id_cob, id_wah ";
                }
                break;
            case SModConsts.CU_PLA:
                settings = new SGuiCatalogueSettings("Planta", 3, 2);
                settings.setCodeApplying(true);
                sql = "SELECT id_co AS " + SDbConsts.FIELD_ID + "1, id_cob AS " + SDbConsts.FIELD_ID + "2, id_pla AS " + SDbConsts.FIELD_ID + "3, name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "code AS " + SDbConsts.FIELD_CODE + ", id_co AS " + SDbConsts.FIELD_FK + "1, id_cob AS " + SDbConsts.FIELD_FK + "2 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 "
                        + "ORDER BY name, id_co, id_cob, id_pla ";
                break;
            case SModConsts.CU_PROD_LINES:
                settings = new SGuiCatalogueSettings("Línea de producción", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_line AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "code AS " + SDbConsts.FIELD_CODE + ", id_line AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 "
                        + "ORDER BY name, id_line, id_line";
                break;
            case SModConsts.CU_LAN:
                settings = new SGuiCatalogueSettings("Idioma", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_lan AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_lan ";
                break;
            case SModConsts.CU_USR:
                settings = new SGuiCatalogueSettings("Usuario", 1);
                sql = "SELECT id_usr AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_usr ";
                break;
            case SModConsts.CU_USR_RIG:
                settings = new SGuiCatalogueSettings("Derecho del usuario", 1);
                sql = "SELECT ur.id_rig AS " + SDbConsts.FIELD_ID + "1, r.name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS ur "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CS_RIG) + " AS r ON ur.id_rig = r.id_rig "
                        + "WHERE r.b_del = 0 AND ur.id_usr = " + params.getKey()[0] + " "
                        + "ORDER BY r.sort ";
                break;
            case SModConsts.CU_USR_SCA:
                settings = new SGuiCatalogueSettings("Báscula del usuario", 1);
                settings.setCodeApplying(true);
                sql = "SELECT us.id_sca AS " + SDbConsts.FIELD_ID + "1, s.name AS " + SDbConsts.FIELD_ITEM + ", s.code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS us "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS s ON us.id_sca = s.id_sca "
                        + "WHERE s.b_del = 0 AND s.b_dis = 0 AND us.id_usr = " + params.getKey()[0] + " "
                        + "ORDER BY s.name, s.id_sca ";
                break;
            case SModConsts.CU_DIV:
                settings = new SGuiCatalogueSettings("División", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_div AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_div ";
                break;
            case SModConsts.CU_REP_GRP:
                settings = new SGuiCatalogueSettings("Agrupador de reporte", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_rep_grp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_rep_grp ";
                break;
            case SModConsts.SU_GRINDING_PARAM:
                settings = new SGuiCatalogueSettings("Parámetros", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_parameter AS " + SDbConsts.FIELD_ID + "1, param AS " + SDbConsts.FIELD_ITEM + ", param_code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY param, id_parameter ";
                break;
            case SModConsts.SU_OIL_TP:
                settings = new SGuiCatalogueSettings("Tipo de aceite", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_oil_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name ASC ";
                break;
            case SModConsts.SU_OIL_OWN:
                settings = new SGuiCatalogueSettings("Origen de aceite", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_oil_own AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name ASC ";
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
            case SModConsts.CU_CO:
                view = new SViewCompany(miClient, "Empresas");
                break;
            case SModConsts.CU_COB:
                view = new SViewCompanyBranch(miClient, "Sucursales");
                break;
            case SModConsts.CU_PROD_LINES:
                view = new SViewProductionLine(miClient, "Líneas de Producción");
                break;
            case SModConsts.CU_WAH:
                view = new SViewBranchWarehouse(miClient, "Almacenes");
                break;
            case SModConsts.CU_PLA:
                view = new SViewBranchPlant(miClient, "Plantas");
                break;
            case SModConsts.SU_GRINDING_PARAM:
                view = new SViewGrindingParameters(miClient, "Parámetros de molienda");
                break;
            case SModConsts.SU_GRINDING_LINK_ITEM_PARAM:
                view = new SViewGrindingLinkItmParam(miClient, "Parámetros vs ítems");
                break;
            case SModConsts.CU_YEAR:
                view = new SViewYear(miClient, "Ejercicios y períodos");
                break;
            case SModConsts.CU_LAN:
                view = null;
                break;
            case SModConsts.CU_USR:
                view = new SViewUser(miClient, "Usuarios");
                break;
            case SModConsts.CU_USR_RIG:
                view = new SViewUserRight(miClient, "Usuarios vs. derechos Q");
                break;
            case SModConsts.CU_USR_SCA:
                view = new SViewUserScale(miClient, "Usuarios vs. básculas Q");
                break;
            case SModConsts.CU_USR_INP_CT:
                view = new SViewUserInputCategories(miClient, "Usuarios vs. categorías de insumo Q");
                break;
            case SModConsts.CU_DIV:
                view = null;
                break;
            case SModConsts.CU_REP_GRP:
                view = new SViewReportingGroup(miClient, "Agrupadores reporte");
                break;
            case SModConsts.CU_USR_ALT_RIG:
                switch (subtype) {
                    case SModConsts.CS_ALT_RIG: view = new SViewUserAlternativeRight(miClient, "Usuarios alt. vs. derechos (Q)"); break;
                    case SLibConsts.UNDEFINED: view = new SViewUserAlternative(miClient, "Usuarios alternos"); break;
                }
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
            case SModConsts.CU_CO:
                if (moFormCompany == null) moFormCompany = new SFormCompany(miClient, "Empresa");
                form = moFormCompany;
                break;
            case SModConsts.CU_COB:
                if (moFormCompanyBranch == null) moFormCompanyBranch = new SFormCompanyBranch(miClient, "Sucursal");
                form = moFormCompanyBranch;
                break;
            case SModConsts.CU_PROD_LINES:
                if (moFormProductionLine == null) moFormProductionLine = new SFormProductionLine(miClient, "Línea de producción");
                form = moFormProductionLine;
                break;
            case SModConsts.CU_WAH:
                if (moFormWarehouse == null) moFormWarehouse = new SFormBranchWarehouse(miClient, "Almacén");
                form = moFormWarehouse;
                break;
            case SModConsts.CU_PLA:
                if (moFormPlant == null) moFormPlant = new SFormBranchPlant(miClient, "Planta");
                form = moFormPlant;
                break;
            case SModConsts.SU_GRINDING_PARAM:
                if (moFormGrindingParameters == null) moFormGrindingParameters = new SFormGrindingParameters(miClient, "Parámetro de molienda");
                form = moFormGrindingParameters;
                break;
            case SModConsts.SU_GRINDING_LINK_ITEM_PARAM:
                if (moFormLinkItemParam == null) moFormLinkItemParam = new SFormGrindingLinkItemParam(miClient, "Parámetro vs ítem");
                form = moFormLinkItemParam;
                break;
            case SModConsts.CU_REP_GRP:
                if (moFormReportingGroup == null) moFormReportingGroup = new SFormReportingGroup(miClient, "Agrupador de reporte");
                form = moFormReportingGroup;
                break;
            case SModConsts.CU_USR:
                if (moFormUser == null) moFormUser = new SFormUser(miClient, "Usuario");
                form = moFormUser;
                break;
            case SModConsts.CU_YEAR:
                if (moFormYear == null) moFormYear = new SFormYear(miClient, "Ejercicio");
                form = moFormYear;
                break;
            case SModConsts.CU_USR_ALT_RIG:
                if (moFormUserAlternative == null) moFormUserAlternative = new SFormUserAlternative(miClient, "Usuario alterno");
                form = moFormUserAlternative;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();

            if (menuItem == mjCatCompany) {
                showView(SModConsts.CU_CO, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatCompanyBranch) {
                showView(SModConsts.CU_COB, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatProductionLine) {
                showView(SModConsts.CU_PROD_LINES, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatWarehouse) {
                showView(SModConsts.CU_WAH, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatPlant) {
                showView(SModConsts.CU_PLA, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatParameters) {
                showView(SModConsts.SU_GRINDING_PARAM, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatLinkItmParams) {
                showView(SModConsts.SU_GRINDING_LINK_ITEM_PARAM, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatScale) {
                miClient.getSession().showView(SModConsts.SU_SCA, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUser) {
                showView(SModConsts.CU_USR, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserRights) {
                showView(SModConsts.CU_USR_RIG, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserScales) {
                showView(SModConsts.CU_USR_SCA, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserInputCategories) {
                showView(SModConsts.CU_USR_INP_CT, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserAlt) {
                showView(SModConsts.CU_USR_ALT_RIG, SLibConsts.UNDEFINED, null);                
            }
            else if (menuItem == mjUsrUserAltRights) {
                showView(SModConsts.CU_USR_ALT_RIG, SModConsts.CS_ALT_RIG, null);                
            }
            else if (menuItem == mjCtrYear) {
                showView(SModConsts.CU_YEAR, SLibConsts.UNDEFINED, null);
            }
        }
    }
}