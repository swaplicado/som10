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
import som.mod.ext.db.SExtUtils;
import som.mod.ext.form.SDialogRepComparativeTicket;
import som.mod.som.db.SDbGrindingEvent;
import som.mod.som.db.SDbGrindingResult;
import som.mod.som.db.SDbLaboratory;
import som.mod.som.db.SDbLaboratoryTest;
import som.mod.som.db.SDbMgmtTicketsSupplierInputType;
import som.mod.som.db.SDbMgmtTicketsSupplierItem;
import som.mod.som.db.SDbRegion;
import som.mod.som.db.SDbSeason;
import som.mod.som.db.SDbSeasonProducer;
import som.mod.som.db.SDbSeasonRegion;
import som.mod.som.db.SDbSupraRegion;
import som.mod.som.db.SDbTicket;
import som.mod.som.db.SDbWarehouseStart;
import som.mod.som.form.SDialogRepFreightTime;
import som.mod.som.form.SDialogRepFruitYieldByOrigin;
import som.mod.som.form.SDialogRepIodineRank;
import som.mod.som.form.SDialogRepReceivedFruit;
import som.mod.som.form.SDialogRepReceivedFruitAcidity;
import som.mod.som.form.SDialogRepReceivedFruitHist;
import som.mod.som.form.SDialogRepReceivedSeed;
import som.mod.som.form.SDialogTicketsSearch;
import som.mod.som.form.SFormDialogAssignSeasonRegion;
import som.mod.som.form.SFormGrindingEvent;
import som.mod.som.form.SFormGrindingResultHr;
import som.mod.som.form.SFormLaboratory;
import som.mod.som.form.SFormMgmtSupplierInputType;
import som.mod.som.form.SFormMgmtSupplierItem;
import som.mod.som.form.SFormRegion;
import som.mod.som.form.SFormSeason;
import som.mod.som.form.SFormSeasonProducer;
import som.mod.som.form.SFormSeasonRegion;
import som.mod.som.form.SFormSupraRegion;
import som.mod.som.form.SFormTicket;
import som.mod.som.form.SFormTicketMgmt;
import som.mod.som.form.SFormTicketSeasonRegion;
import som.mod.som.form.SFormTicketWahUnld;
import som.mod.som.form.SFormWarehouseStart;
import som.mod.som.view.SViewGrindingEvents;
import som.mod.som.view.SViewGrindingResults;
import som.mod.som.view.SViewGrindingResume;
import som.mod.som.view.SViewLaboratory;
import som.mod.som.view.SViewOilMoiPond;
import som.mod.som.view.SViewRegion;
import som.mod.som.view.SViewSeason;
import som.mod.som.view.SViewSeasonProducer;
import som.mod.som.view.SViewSeasonRegion;
import som.mod.som.view.SViewSupraRegion;
import som.mod.som.view.SViewTicket;
import som.mod.som.view.SViewTicketTare;
import som.mod.som.view.SViewTicketWahUnld;
import som.mod.som.view.SViewTicketsLaboratoryTestFruit;
import som.mod.som.view.SViewTicketsLog;
import som.mod.som.view.SViewTicketsSupplierItem;
import som.mod.som.view.SViewTicketsSupplierItemInputType;
import som.mod.som.view.SViewWarehouseStart;

/**
 * 
 * @author Néstor Ávalos, Juan Barajas, Alfredo Pérez, Sergio Flores, Isabel Servín, Edwin Carmona
 * 2019-01-17, Sergio Flores: Cambio de ubicación del catálogo de agrupadores de reporte, del módulo configuración al módulo materias primas.
 */
public class SModuleSomRm extends SGuiModule implements ActionListener {

    private JMenu mjCat;   // Catalogues
    private JMenuItem mjCatProducer;
    private JMenuItem mjCatItem;
    private JMenuItem mjCatLot;
    private JMenuItem mjCatInputType;
    private JMenuItem mjCatInputClass;
    private JMenuItem mjCatInputCategory;
    private JMenuItem mjCatInputSource;
    private JMenuItem mjCatReportingGroup;
    private JMenuItem mjCatIodineValueRank;
    private JMenuItem mjCatExternalWarehouses;
    private JMenuItem mjCatUpdateCatalogues;
    private JMenu mjCfg;   // Configuration
    private JMenuItem mjCfgSeasons;
    private JMenuItem mjCfgSeasonRegion;
    private JMenuItem mjCfgSeasonProducer;
    private JMenuItem mjCfgRegions;
    private JMenuItem mjCfgSupraRegions;
    private JMenu mjTic;   // Tickets
    private JMenuItem mjTicTicketSca;
    private JMenuItem mjTicTicketLab;
    private JMenuItem mjTicTicketAdm;
    private JMenuItem mjTicTicketAll;
    private JMenuItem mjTicTarePend;
    private JMenuItem mjTicTare;
    private JMenuItem mjTicWahNAsigned;
    private JMenuItem mjTicWahAsigned;
    private JMenuItem mjTicManSupplierItem;
    private JMenuItem mjTicManSupplierInputType;
    private JMenuItem mjTicRank;
    private JMenuItem mjTicSearch;
    private JMenu mjGrinding;   // Molienda
    private JMenuItem mjGriEvents;
    private JMenuItem mjGriResume;
    private JMenuItem mjGriResults;
    private JMenu mjGrindingAvo;   // Aguacatera
    private JMenuItem mjGriEventsAvo;
    private JMenuItem mjGriResumeAvo;
    private JMenuItem mjGriResultsAvo;
    private JMenu mjQa;    // Quality Assurrance
    private JMenuItem mjQaLabTest;
    private JMenuItem mjQaLabTestDet;
    private JMenuItem mjQaTicketLabTestDetFruit;
    private JMenuItem mjQaWahStart;
    private JMenuItem mjQaOilMoiPond;
    private JMenu mjRep;   // Reports
    private JMenuItem mjRepSeedReceived;
    private JMenuItem mjRepReceivedFruit;
    private JMenuItem mjRepReceivedFruitHist;
    private JMenuItem mjRepReceivedFruitAcidity;
    private JMenuItem mjRepFruitYieldByOrigin;
    private JMenuItem mjRepSeedReceivedByIodVal;
    private JMenuItem mjRepSeedReceivedByPerOle;
    private JMenuItem mjRepSeedReceivedByPerLin;
    private JMenuItem mjRepSeedReceivedByPerLlc;
    private JMenuItem mjRepSeedPayed;
    private JMenuItem mjRepFreighTime;
    private JMenuItem mjRepTicLog;
    private JMenuItem mjRepCompTic;

    private SFormSupraRegion moFormSupraRegion;
    private SFormRegion moFormRegion;
    private SFormSeason moFormSeason;
    private SFormGrindingEvent moFormGrindingEvent;
    private SFormGrindingResultHr moFormResult;
    private SFormLaboratory moFormLaboratory;
    private SFormTicket moFormTicket;
    private SFormTicket moFormTicketTare;
    private SFormWarehouseStart moFormWarehouseStart;
    private SFormTicketWahUnld moFormTicketWahUnld;
    private SFormTicketMgmt moFormTicketMgmt;
    private SFormTicketSeasonRegion moFormTicketSeasonRegion;
    private SFormSeasonRegion moFormSeasonRegion;
    private SFormSeasonProducer moFormSeasonProducer;
    private SFormDialogAssignSeasonRegion moFormDialogAssignSeasonRegion;
    private SFormMgmtSupplierItem moFormMgmtSupplierItem;
    private SFormMgmtSupplierInputType moFormMgmtSupplierInputType;

    public SModuleSomRm(SGuiClient client) {
       super(client, SModConsts.MOD_SOM_RM, SLibConsts.UNDEFINED);
       initComponents();
    }

    /*
     * Private methods
     */

    private void initComponents() {
        mjCat = new JMenu("Catálogos");
        mjCatProducer = new JMenuItem("Proveedores");
        mjCatItem = new JMenuItem("Ítems");
        mjCatLot = new JMenuItem("Lotes");
        mjCatInputType = new JMenuItem("Tipos de insumo");
        mjCatInputClass = new JMenuItem("Clases de insumo");
        mjCatInputCategory = new JMenuItem("Categorías de insumo");
        mjCatInputSource = new JMenuItem("Orígenes de insumos");
        mjCatReportingGroup = new JMenuItem("Agrupadores de reporte");
        mjCatIodineValueRank = new JMenuItem("Rangos de yodo");
        mjCatExternalWarehouses = new JMenuItem("Almacenes sistema externo");
        mjCatUpdateCatalogues = new JMenuItem("Actualizar catálogos sistema externo...");

        mjCat.add(mjCatProducer);
        mjCat.add(mjCatItem);
        mjCat.add(mjCatLot);
        mjCat.addSeparator();
        mjCat.add(mjCatInputType);
        mjCat.add(mjCatInputClass);
        mjCat.add(mjCatInputCategory);
        mjCat.addSeparator();
        mjCat.add(mjCatInputSource);
        mjCat.add(mjCatReportingGroup);
        mjCat.addSeparator();
        mjCat.add(mjCatIodineValueRank);
        mjCat.addSeparator();
        mjCat.add(mjCatExternalWarehouses);
        mjCat.addSeparator();
        mjCat.add(mjCatUpdateCatalogues);

        mjCatProducer.addActionListener(this);
        mjCatItem.addActionListener(this);
        mjCatLot.addActionListener(this);
        mjCatInputType.addActionListener(this);
        mjCatInputClass.addActionListener(this);
        mjCatInputCategory.addActionListener(this);
        mjCatInputSource.addActionListener(this);
        mjCatReportingGroup.addActionListener(this);
        mjCatIodineValueRank.addActionListener(this);
        mjCatExternalWarehouses.addActionListener(this);
        mjCatUpdateCatalogues.addActionListener(this);

        mjCfg = new JMenu("Configuración");
        mjCfgSeasons = new JMenuItem("Temporadas");
        mjCfgSeasonRegion = new JMenuItem("Configuración de regiones");
        mjCfgSeasonProducer = new JMenuItem("Configuración de proveedores");
        mjCfgRegions = new JMenuItem("Regiones");
        mjCfgSupraRegions = new JMenuItem("Supraregiones");

        mjCfg.add(mjCfgSeasons);
        mjCfg.add(mjCfgSeasonRegion);
        mjCfg.add(mjCfgSeasonProducer);
        mjCfg.addSeparator();
        mjCfg.add(mjCfgRegions);
        mjCfg.add(mjCfgSupraRegions);

        mjCfgSeasons.addActionListener(this);
        mjCfgSeasonRegion.addActionListener(this);
        mjCfgSeasonProducer.addActionListener(this);
        mjCfgRegions.addActionListener(this);
        mjCfgSupraRegions.addActionListener(this);

        mjTic = new JMenu("Boletos báscula");
        mjTicTicketSca = new JMenuItem("Boletos en báscula");
        mjTicTicketLab = new JMenuItem("Boletos en laboratorio");
        mjTicTicketAdm = new JMenuItem("Boletos en gerencia");
        mjTicTicketAll = new JMenuItem("Boletos (todos)");
        mjTicTarePend = new JMenuItem("Boletos por tarar");
        mjTicTare = new JMenuItem("Boletos tarados");
        mjTicWahNAsigned = new JMenuItem("Boletos por descargar");
        mjTicWahAsigned = new JMenuItem("Boletos descargados");
        mjTicManSupplierItem = new JMenuItem("Boletos por proveedor por ítem");
        mjTicManSupplierInputType = new JMenuItem("Boletos por proveedor por tipo de insumo");
        mjTicRank = new JMenuItem("Clasificar boletos sin temporada y/o región...");
        mjTicSearch = new JMenuItem("Buscar boletos...");

        mjTic.add(mjTicTicketSca);
        mjTic.add(mjTicTicketLab);
        mjTic.add(mjTicTicketAdm);
        mjTic.add(mjTicTicketAll);
        mjTic.addSeparator();
        mjTic.add(mjTicTarePend);
        mjTic.add(mjTicTare);
        mjTic.addSeparator();
        mjTic.add(mjTicWahNAsigned);
        mjTic.add(mjTicWahAsigned);
        mjTic.addSeparator();
        mjTic.add(mjTicManSupplierItem);
        mjTic.add(mjTicManSupplierInputType);
        mjTic.addSeparator();
        mjTic.add(mjTicRank);
        mjTic.addSeparator();
        mjTic.add(mjTicSearch);

        mjTicTicketSca.addActionListener(this);
        mjTicTicketLab.addActionListener(this);
        mjTicTicketAdm.addActionListener(this);
        mjTicTicketAll.addActionListener(this);
        mjTicTarePend.addActionListener(this);
        mjTicTare.addActionListener(this);
        mjTicWahNAsigned.addActionListener(this);
        mjTicWahAsigned.addActionListener(this);
        mjTicManSupplierItem.addActionListener(this);
        mjTicManSupplierInputType.addActionListener(this);
        mjTicRank.addActionListener(this);
        mjTicSearch.addActionListener(this);
        mjGrinding = new JMenu("Molienda");
        mjGriEvents = new JMenuItem("Eventos de molienda");
        mjGriResume = new JMenuItem("Resumen de molienda");
        mjGriResults = new JMenuItem("Resultados de molienda");
        mjGrindingAvo = new JMenu("Aguacatera");
        mjGriEventsAvo = new JMenuItem("Eventos de aguacatera");
        mjGriResumeAvo = new JMenuItem("Resumen de aguacatera");
        mjGriResultsAvo = new JMenuItem("Resultados de aguacatera");
        
        mjGrinding.add(mjGriEvents);
        mjGrinding.add(mjGriResume);
        mjGrinding.add(mjGriResults);
        
        mjGrindingAvo.add(mjGriEventsAvo);
        mjGrindingAvo.add(mjGriResumeAvo);
        mjGrindingAvo.add(mjGriResultsAvo);
        
        mjGriEvents.addActionListener(this);
        mjGriResume.addActionListener(this);
        mjGriResults.addActionListener(this);
        
        mjGriEventsAvo.addActionListener(this);
        mjGriResumeAvo.addActionListener(this);
        mjGriResultsAvo.addActionListener(this);

        mjQa = new JMenu("Control calidad");
        mjQaLabTest = new JMenuItem("Análisis de laboratorio");
        mjQaLabTestDet = new JMenuItem("Análisis de laboratorio a detalle");
        mjQaTicketLabTestDetFruit = new JMenuItem("Boletos fruta y análisis de laboratorio");
        mjQaWahStart = new JMenuItem("Fechas de inicio de almacenes");
        mjQaOilMoiPond = new JMenuItem("Cálculo ponderado aceite y humedad");

        mjQa.add(mjQaLabTest);
        mjQa.add(mjQaLabTestDet);
        mjQa.addSeparator();
        mjQa.add(mjQaTicketLabTestDetFruit);
        mjQa.addSeparator();
        mjQa.add(mjQaWahStart);
        mjQa.add(mjQaOilMoiPond);

        mjQaLabTest.addActionListener(this);
        mjQaLabTestDet.addActionListener(this);
        mjQaTicketLabTestDetFruit.addActionListener(this);
        mjQaWahStart.addActionListener(this);
        mjQaOilMoiPond.addActionListener(this);

        mjRep = new JMenu("Reportes");
        mjRepSeedReceived = new JMenuItem("Materia prima recibida...");
        mjRepReceivedFruit = new JMenuItem("Fruta recibida...");
        mjRepReceivedFruitHist = new JMenuItem("Comparativo histórico de fruta recibida...");
        mjRepReceivedFruitAcidity = new JMenuItem("Acidez fruta recibida...");
        mjRepFruitYieldByOrigin = new JMenuItem("Rendimiento de fruta por origen...");
        mjRepSeedReceivedByIodVal = new JMenuItem("Materia prima recibida por valor de yodo...");
        mjRepSeedReceivedByPerOle = new JMenuItem("Materia prima recibida por porcentaje de ácido oleico...");
        mjRepSeedReceivedByPerLin = new JMenuItem("Materia prima recibida por porcentaje de ácido linoleico...");
        mjRepSeedReceivedByPerLlc = new JMenuItem("Materia prima recibida por porcentaje de ácido linolénico...");
        mjRepSeedPayed = new JMenuItem("Materia prima pagada...");
        mjRepFreighTime = new JMenuItem("Duración fletes de materia prima...");
        mjRepTicLog = new JMenuItem("Bitácora de boletos");
        mjRepCompTic = new JMenuItem("Comparativo boletos SOM vs. Revuelta...");

        mjRep.add(mjRepSeedReceived);
        mjRep.addSeparator();
        mjRep.add(mjRepReceivedFruit);
        mjRep.add(mjRepReceivedFruitHist);
        mjRep.add(mjRepReceivedFruitAcidity);
        mjRep.add(mjRepFruitYieldByOrigin);
        mjRep.addSeparator();
        mjRep.add(mjRepSeedReceivedByIodVal);
        mjRep.add(mjRepSeedReceivedByPerOle);
        mjRep.add(mjRepSeedReceivedByPerLin);
        mjRep.add(mjRepSeedReceivedByPerLlc);
        mjRep.addSeparator();
        mjRep.add(mjRepSeedPayed);
        mjRep.addSeparator();
        mjRep.add(mjRepFreighTime);
        mjRep.addSeparator();
        mjRep.add(mjRepTicLog);
        mjRep.add(mjRepCompTic);

        mjRepSeedReceived.addActionListener(this);
        mjRepReceivedFruit.addActionListener(this);
        mjRepReceivedFruitHist.addActionListener(this);
        mjRepReceivedFruitAcidity.addActionListener(this);
        mjRepFruitYieldByOrigin.addActionListener(this);
        mjRepSeedReceivedByIodVal.addActionListener(this);
        mjRepSeedReceivedByPerOle.addActionListener(this);
        mjRepSeedReceivedByPerLin.addActionListener(this);
        mjRepSeedReceivedByPerLlc.addActionListener(this);
        mjRepSeedPayed.addActionListener(this);
        mjRepFreighTime.addActionListener(this);
        mjRepTicLog.addActionListener(this);
        mjRepCompTic.addActionListener(this);

        // Privileges

        mjCat.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_RM));
        
        mjCfg.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_RM));

        mjTic.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_LAB, SModSysConsts.CS_RIG_SCA, SModSysConsts.CS_RIG_SUP_SCA, SModSysConsts.CS_RIG_SUP_LAB, SModSysConsts.CS_RIG_REP_RM }));
        mjTicTicketSca.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_SCA, SModSysConsts.CS_RIG_SUP_SCA }));
        mjTicTicketLab.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_LAB, SModSysConsts.CS_RIG_SUP_LAB }));
        mjTicTicketAdm.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_RM));
        mjTicTicketAll.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM }));
        mjTicTarePend.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_SCA, SModSysConsts.CS_RIG_SUP_SCA }));
        mjTicTare.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_SCA, SModSysConsts.CS_RIG_SUP_SCA }));
        mjTicWahNAsigned.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_DIS_RM }));
        mjTicWahAsigned.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_DIS_RM }));
        mjTicManSupplierItem.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM }));
        mjTicManSupplierInputType.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM }));
        mjTicRank.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_RM));
        mjTicSearch.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_SCA, SModSysConsts.CS_RIG_LAB, SModSysConsts.CS_RIG_SUP_SCA, SModSysConsts.CS_RIG_SUP_LAB, SModSysConsts.CS_RIG_REP_RM }));
        
        mjGrinding.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM, SModSysConsts.CS_RIG_LAB, SModSysConsts.CS_RIG_SUP_LAB }));
        mjGrindingAvo.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM, SModSysConsts.CS_RIG_LAB, SModSysConsts.CS_RIG_SUP_LAB }));

        mjQa.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM, SModSysConsts.CS_RIG_LAB, SModSysConsts.CS_RIG_SUP_LAB, SModSysConsts.CS_RIG_DIS_RM }));
        mjQaWahStart.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_DIS_RM }));
        mjQaOilMoiPond.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_DIS_RM }));

        mjRep.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM }));
    }

    /*
     * Public methods
     */

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjCfg, mjTic, mjGrinding, mjGrindingAvo, mjQa, mjRep };
    }

    @Override
    public SDbRegistry getRegistry(int type, SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.SS_TIC_ST:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tic_st = " + pk[0] + " "; }
                };
                break;
            case SModConsts.SU_SUP_REG:
                registry = new SDbSupraRegion();
                break;
            case SModConsts.SU_REG:
                registry = new SDbRegion();
                break;
            case SModConsts.SU_SEAS:
                registry = new SDbSeason();
                break;
            case SModConsts.SU_SEAS_REG:
                registry = new SDbSeasonRegion();
                break;
            case SModConsts.SU_SEAS_PROD:
                registry = new SDbSeasonProducer();
                break;
            case SModConsts.SX_SEAS_REG:
                registry = new SDbSeason();
                break;
            case SModConsts.S_GRINDING_EVENT:
                registry = new SDbGrindingEvent();
                break;
            case SModConsts.S_GRINDING_RESULT:
                registry = new SDbGrindingResult();
                break;
            case SModConsts.S_LAB:
                registry = new SDbLaboratory();
                break;
            case SModConsts.S_LAB_TEST:
                registry = new SDbLaboratoryTest();
                break;
            case SModConsts.S_WAH_START:
                registry = new SDbWarehouseStart();
                break;
            case SModConsts.S_TIC:
            case SModConsts.SX_TIC_LAB:
            case SModConsts.SX_TIC_TARE:
            case SModConsts.SX_TIC_WAH_UNLD:
            case SModConsts.SX_TIC_MAN:
            case SModConsts.SX_TIC_SEAS_REG:
                registry = new SDbTicket();
                break;
            case SModConsts.SX_TIC_MAN_SUP:
                registry = new SDbMgmtTicketsSupplierItem();
                break;
            case SModConsts.SX_TIC_MAN_SUP_INP_TP:
                registry = new SDbMgmtTicketsSupplierInputType();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(int type, int subtype, SGuiParams params) {
        String sql = "";
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.SU_SUP_REG:
                settings = new SGuiCatalogueSettings("Supraregión", 1);
                sql = "SELECT id_sup_reg AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_sup_reg ";
                break;
            case SModConsts.SU_REG:
                settings = new SGuiCatalogueSettings("Región", 1);
                sql = "SELECT id_reg AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_reg ";
                break;
            case SModConsts.SU_SEAS:
                settings = new SGuiCatalogueSettings("Temporada", 1);
                sql = "SELECT id_seas AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY name, id_seas ";
                break;
            case SModConsts.SX_PROD_SEAS:
                settings = new SGuiCatalogueSettings("Temporada", 1);
                sql = "SELECT DISTINCT(s.id_seas) AS " + SDbConsts.FIELD_ID + "1, se.name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_REG) + " AS s "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS se ON s.id_seas = se.id_seas "
                        + " WHERE s.b_del = 0 AND s.b_dis = 0 ORDER BY s.id_seas ";
                break;
            case SModConsts.SX_PROD_REG:
                settings = new SGuiCatalogueSettings("Región", 2, 1);
                sql = "SELECT DISTINCT(s.id_seas) AS " + SDbConsts.FIELD_ID + "1, s.id_reg AS " + SDbConsts.FIELD_ID + "2, re.name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "s.id_seas AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_REG) + " AS s "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS re ON s.id_reg = re.id_reg "
                        + "WHERE s.b_del = 0 AND s.b_dis = 0 ORDER BY re.name, s.id_seas, s.id_reg ";
                break;
            case SModConsts.SX_PROD_ITEM:
                settings = new SGuiCatalogueSettings("Ítem", 3, 2);
                settings.setCodeApplying(true);
                sql = "SELECT s.id_seas AS " + SDbConsts.FIELD_ID + "1, s.id_reg AS " + SDbConsts.FIELD_ID + "2, s.id_item AS " + SDbConsts.FIELD_ID + "3, it.name AS " + SDbConsts.FIELD_ITEM + ", it.code AS " + SDbConsts.FIELD_CODE + ", "
                        + "s.id_seas AS " + SDbConsts.FIELD_FK + "1, " + "s.id_reg AS " + SDbConsts.FIELD_FK + "2 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_REG) + " AS s "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS it ON s.id_item = it.id_item "
                        + "WHERE s.b_del = 0 AND s.b_dis = 0 ORDER BY it.name, s.id_seas, s.id_reg, s.id_item ";
                break;
            case SModConsts.SX_TIC_SEAS:
                settings = new SGuiCatalogueSettings("Temporada", 3);
                sql = "SELECT DISTINCT(s.id_seas) AS " + SDbConsts.FIELD_ID + "1, s.id_item AS " + SDbConsts.FIELD_ID + "2, "
                        + "s.id_prod AS " + SDbConsts.FIELD_ID + "3, se.name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS s "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS se ON s.id_seas = se.id_seas AND '"
                        + SLibUtils.DbmsDateFormatDate.format(params.getParamsMap().get(SGuiConsts.PARAM_DATE)) + "' >= se.dt_sta AND '" + SLibUtils.DbmsDateFormatDate.format(params.getParamsMap().get(SGuiConsts.PARAM_DATE)) + "' <= se.dt_end "
                        + "WHERE s.b_del = 0 AND s.b_dis = 0 AND s.id_item = " + params.getParamsMap().get(SModConsts.SU_ITEM) + " AND s.id_prod = " + params.getParamsMap().get(SModConsts.SU_PROD) + "  "
                        + "ORDER BY se.name, s.id_seas, s.id_item, s.id_prod ";
                break;
            case SModConsts.SX_TIC_REG:
                settings = new SGuiCatalogueSettings("Región", 4, 3);
                sql = "SELECT DISTINCT(s.id_seas) AS " + SDbConsts.FIELD_ID + "1, s.id_reg AS " + SDbConsts.FIELD_ID + "2, s.id_item AS " + SDbConsts.FIELD_ID + "3, "
                        + "s.id_prod AS " + SDbConsts.FIELD_ID + "4, re.name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "s.id_seas AS " + SDbConsts.FIELD_FK + "1, s.id_item AS " + SDbConsts.FIELD_FK + "2, s.id_prod AS " + SDbConsts.FIELD_FK + "3 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS s "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS re ON s.id_reg = re.id_reg "
                        + "WHERE s.b_del = 0 AND s.b_dis = 0 "
                        + "ORDER BY re.name, s.id_seas, s.id_reg, s.id_item, s.id_prod ";
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
            case SModConsts.SU_SUP_REG:
                view = new SViewSupraRegion(miClient, "Supraregiones");
                break;
            case SModConsts.SU_REG:
                view = new SViewRegion(miClient, "Regiones");
                break;
            case SModConsts.SU_SEAS:
                view = new SViewSeason(miClient, "Temporadas");
                break;
            case SModConsts.SU_SEAS_REG:
                view = new SViewSeasonRegion(miClient, "Config. regiones");
                break;
            case SModConsts.SU_SEAS_PROD:
                view = new SViewSeasonProducer(miClient, "Config. proveedores");
                break;
            case SModConsts.S_GRINDING_EVENT:
                switch (subtype) {
                    case SModSysConsts.CU_PLA_INT_PYE:
                        view = new SViewGrindingEvents(miClient, subtype, params.getKey(), "Eventos de prensas y ext.");
                        break;
                    case SModSysConsts.CU_PLA_INT_AGU:
                        view = new SViewGrindingEvents(miClient, subtype, params.getKey(), "Eventos de aguacatera");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.SX_GRINDING_RESUME:
                switch (subtype) {
                    case SModSysConsts.CU_PLA_INT_PYE:
                        view = new SViewGrindingResume(miClient, subtype, params.getKey(), "Resumen de prensas y ext.");
                        break;
                    case SModSysConsts.CU_PLA_INT_AGU:
                        view = new SViewGrindingResume(miClient, subtype, params.getKey(), "Resumen de aguacatera");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.S_GRINDING_RESULT:
                switch (subtype) {
                    case SModSysConsts.CU_PLA_INT_PYE:
                        view = new SViewGrindingResults(miClient, subtype, params.getKey(), "Resultados de prensas y ext.");
                        break;
                    case SModSysConsts.CU_PLA_INT_AGU:
                        view = new SViewGrindingResults(miClient, subtype, params.getKey(), "Resultados de aguacatera");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.S_LAB:
                switch (subtype) {
                    case SModSysConsts.SX_LAB_TEST:
                        view = new SViewLaboratory(miClient, subtype, "Análisis lab");
                        break;
                    case SModSysConsts.SX_LAB_TEST_DET:
                        view = new SViewLaboratory(miClient, subtype, "Análisis lab (detalle)");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.S_WAH_START:
                view = new SViewWarehouseStart(miClient, "Fechas inicio almacenes");
                break;
            case SModConsts.SX_TIC_MAN_SUP:
                view = new SViewTicketsSupplierItem(miClient, "Boletos x proveedor x ítem");
                break;
            case SModConsts.SX_TIC_MAN_SUP_INP_TP:
                view = new SViewTicketsSupplierItemInputType(miClient, "Boletos x proveedor x tipo insumo");
                break;
            case SModConsts.S_TIC:
                switch (subtype) {
                    case SModSysConsts.SS_TIC_ST_SCA:
                        view = new SViewTicket(miClient, subtype, "Boletos báscula");
                        break;
                    case SModSysConsts.SS_TIC_ST_LAB:
                        view = new SViewTicket(miClient, subtype, "Boletos laboratorio");
                        break;
                    case SModSysConsts.SS_TIC_ST_ADM:
                        view = new SViewTicket(miClient, subtype, "Boletos gerencia");
                        break;
                    case SLibConsts.UNDEFINED:
                        view = new SViewTicket(miClient, subtype, "Boletos (todos)");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.SX_TIC_TARE:
                switch (subtype) {
                    case SModConsts.SX_TIC_TARE_PEND:
                        view = new SViewTicketTare(miClient, subtype, "Boletos x tarar");
                        break;
                    case SModConsts.SX_TIC_TARE:
                        view = new SViewTicketTare(miClient, subtype, "Boletos tarados");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.SX_TIC_WAH_UNLD:
                switch (subtype) {
                    case SModSysConsts.SS_TIC_WAH_UNLD_N_ASIGNED:
                        view = new SViewTicketWahUnld(miClient, subtype, "Boletos por descargar");
                        break;
                    case SModSysConsts.SS_TIC_WAH_UNLD_ASIGNED:
                        view = new SViewTicketWahUnld(miClient, subtype, "Boletos descargados");
                        break;
                }
                break;
            case SModConsts.SX_QA_OIL_MOI_POND:
                view = new SViewOilMoiPond(miClient, "Aceite y humedad ponderado");
                break;
            case SModConsts.SX_TIC_LOG:
                view = new SViewTicketsLog(miClient, "Bitácora boletos");
                break;
            case SModConsts.SX_TIC_LAB_TEST_FRUIT:
                view = new SViewTicketsLaboratoryTestFruit(miClient, "Boletos fruta análisis lab");
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
            case SModConsts.SU_SUP_REG:
                if (moFormSupraRegion == null) moFormSupraRegion = new SFormSupraRegion(miClient, "Supraregión");
                form = moFormSupraRegion;
                break;
            case SModConsts.SU_REG:
                if (moFormRegion == null) moFormRegion = new SFormRegion(miClient, "Región");
                form = moFormRegion;
                break;
            case SModConsts.SU_SEAS:
                if (moFormSeason == null) moFormSeason = new SFormSeason(miClient, "Temporada");
                form = moFormSeason;
                break;
            case SModConsts.SU_SEAS_REG:
                if (moFormSeasonRegion == null) moFormSeasonRegion = new SFormSeasonRegion(miClient, "Configuración de región");
                form = moFormSeasonRegion;
                break;
            case SModConsts.SU_SEAS_PROD:
                if (moFormSeasonProducer == null) moFormSeasonProducer = new SFormSeasonProducer(miClient, "Configuración de proveedor");
                form = moFormSeasonProducer;
                break;
            case SModConsts.SX_SEAS_REG:
                if (moFormDialogAssignSeasonRegion == null) moFormDialogAssignSeasonRegion = new SFormDialogAssignSeasonRegion(miClient, "Clasificar boletos sin temporada y/o región");
                form = moFormDialogAssignSeasonRegion;
                break;
            case SModConsts.S_GRINDING_EVENT:
                if (moFormGrindingEvent == null) moFormGrindingEvent = new SFormGrindingEvent(miClient, "Evento de molienda");
                form = moFormGrindingEvent;
                break;
            case SModConsts.S_GRINDING_RESULT:
                if (moFormResult == null) moFormResult = new SFormGrindingResultHr(miClient, "Resultado molienda");
                form = moFormResult;
                break;
            case SModConsts.S_TIC:
                if (moFormTicket == null) moFormTicket = new SFormTicket(miClient, "Boleto", SLibConsts.UNDEFINED);
                form = moFormTicket;
                break;
            case SModConsts.S_WAH_START:
                if (moFormWarehouseStart == null) moFormWarehouseStart = new SFormWarehouseStart(miClient, "Inicio de almacén");
                form = moFormWarehouseStart;
                break;
            case SModConsts.SX_TIC_LAB:
                if (moFormLaboratory == null) moFormLaboratory = new SFormLaboratory(miClient, "Análisis de laboratorio");
                form = moFormLaboratory;
                break;
            case SModConsts.SX_TIC_TARE:
                if (moFormTicketTare == null) moFormTicketTare = new SFormTicket(miClient, "Boleto", SModConsts.SX_TIC_TARE_PEND);
                form = moFormTicketTare;
                break;
            case SModConsts.SX_TIC_WAH_UNLD:
                if (moFormTicketWahUnld == null) moFormTicketWahUnld = new SFormTicketWahUnld(miClient, "Boleto almacén");
                form = moFormTicketWahUnld;
                break;
            case SModConsts.SX_TIC_MAN:
                if (moFormTicketMgmt == null) moFormTicketMgmt = new SFormTicketMgmt(miClient, "Boleto");
                form = moFormTicketMgmt;
                break;
            case SModConsts.SX_TIC_MAN_SUP:
                if (moFormMgmtSupplierItem == null) moFormMgmtSupplierItem = new SFormMgmtSupplierItem(miClient, "Boletos por proveedor por ítem");
                form = moFormMgmtSupplierItem;
                break;
            case SModConsts.SX_TIC_MAN_SUP_INP_TP:
                if (moFormMgmtSupplierInputType == null) moFormMgmtSupplierInputType = new SFormMgmtSupplierInputType(miClient, "Boletos por proveedor por tipo de insumo");
                form = moFormMgmtSupplierInputType;
                break;
            case SModConsts.SX_TIC_SEAS_REG:
                if (moFormTicketSeasonRegion == null) moFormTicketSeasonRegion = new SFormTicketSeasonRegion(miClient, "Configuración de temporada y región");
                form = moFormTicketSeasonRegion;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        SGuiReport guiReport = null;

        switch (type) {
            case SModConsts.SR_TIC:
                guiReport = new SGuiReport("reps/s_tic.jasper", "Reporte boleto");
                break;
            case SModConsts.SR_TIC_COMP:
                guiReport = new SGuiReport("reps/s_tic_comp.jasper", "Reporte comparativo boletos SOM vs. Revuelta");
                break;
            case SModConsts.SR_ITEM_REC:
                switch (subtype) {
                    case SModConsts.SR_ITEM_REC:
                        guiReport = new SGuiReport("reps/s_item_rec.jasper", "Reporte materia prima recibida");
                        break;
                    case SModConsts.SR_ITEM_REC_PAY:
                        guiReport = new SGuiReport("reps/s_item_rec_pay.jasper", "Reporte materia prima pagada");
                        break;
                }
                break;
            case SModConsts.SR_ITEM_FRUIT:
                guiReport = new SGuiReport("reps/s_item_rec_fruit.jasper", "Reporte de fruta recibida");
                break;
            case SModConsts.SR_ITEM_FRUIT_HIST:
                guiReport = new SGuiReport("reps/s_item_rec_fruit_hist.jasper", "Reporte comparativo histórico de fruta recibida");
                break;
            case SModConsts.SR_ITEM_FRUIT_ACI:
                guiReport = new SGuiReport("reps/s_item_rec_fruit_aci.jasper", "Reporte acidez de fruta recibida");
                break;
            case SModConsts.SR_FRUIT_YIELD_ORIG:
                guiReport = new SGuiReport("reps/s_tic_yield.jasper", "Reporte de rendimiento de fruta por origen");
                break;
            case SModConsts.SR_ITEM_REC_IOD_VAL:
                switch (subtype) {
                    case SModSysConsts.REP_LAB_TEST_IOD:
                        guiReport = new SGuiReport("reps/s_item_rec_iod_val.jasper", "Reporte materia prima por valor de yodo");
                        break;
                    default:
                        guiReport = new SGuiReport("reps/s_item_rec_test_val.jasper", "Reporte materia prima por valor de ácido " + (
                                subtype == SModSysConsts.REP_LAB_TEST_OLE ? "oleico" :
                                subtype == SModSysConsts.REP_LAB_TEST_LIN ? "linoleico" : "linolénico"));
                }
                break;
            case SModConsts.SR_FRE_TIME:
                guiReport = new SGuiReport("reps/s_fre_time.jasper", "Reporte duración fletes de materia prima");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return guiReport;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();

            if (menuItem == mjCatProducer) {
                miClient.getSession().showView(SModConsts.SU_PROD, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItem) {
                miClient.getSession().showView(SModConsts.SU_ITEM, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatLot) {
                miClient.getSession().showView(SModConsts.S_GRINDING_LOT, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatInputType) {
                miClient.getSession().showView(SModConsts.SU_INP_TP, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatInputClass) {
                miClient.getSession().showView(SModConsts.SU_INP_CL, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatInputCategory) {
                miClient.getSession().showView(SModConsts.SU_INP_CT, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatInputSource) {
                miClient.getSession().showView(SModConsts.SU_INP_SRC, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatReportingGroup) {
                miClient.getSession().getModule(SModConsts.MOD_CFG).showView(SModConsts.CU_REP_GRP, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatIodineValueRank) {
                miClient.getSession().showView(SModConsts.SU_IOD_VAL_RANK, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatExternalWarehouses) {
                miClient.getSession().showView(SModConsts.SU_EXT_WAH, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatUpdateCatalogues) {
                SExtUtils.updateCatalogues(miClient);
            }
            else if (menuItem == mjCfgSeasons) {
                showView(SModConsts.SU_SEAS, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCfgSeasonRegion) {
                showView(SModConsts.SU_SEAS_REG, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCfgSeasonProducer) {
                showView(SModConsts.SU_SEAS_PROD, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjGriEvents) {
                showView(SModConsts.S_GRINDING_EVENT, SModSysConsts.CU_PLA_INT_PYE, new SGuiParams(SModSysConsts.CU_PLA_PYE));
            }
            else if (menuItem == mjGriResume) {
                showView(SModConsts.SX_GRINDING_RESUME, SModSysConsts.CU_PLA_INT_PYE, new SGuiParams(SModSysConsts.CU_PLA_PYE));
            }
            else if (menuItem == mjGriResults) {
                showView(SModConsts.S_GRINDING_RESULT, SModSysConsts.CU_PLA_INT_PYE, new SGuiParams(SModSysConsts.CU_PLA_PYE));
            }
            else if (menuItem == mjGriEventsAvo) {
                showView(SModConsts.S_GRINDING_EVENT, SModSysConsts.CU_PLA_INT_AGU, new SGuiParams(SModSysConsts.CU_PLA_AGU));
            }
            else if (menuItem == mjGriResumeAvo) {
                showView(SModConsts.SX_GRINDING_RESUME, SModSysConsts.CU_PLA_INT_AGU, new SGuiParams(SModSysConsts.CU_PLA_AGU));
            }
            else if (menuItem == mjGriResultsAvo) {
                showView(SModConsts.S_GRINDING_RESULT, SModSysConsts.CU_PLA_INT_AGU, new SGuiParams(SModSysConsts.CU_PLA_AGU));
            }
            else if (menuItem == mjCfgRegions) {
                showView(SModConsts.SU_REG, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCfgSupraRegions) {
                showView(SModConsts.SU_SUP_REG, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjTicTicketSca) {
                showView(SModConsts.S_TIC, SModSysConsts.SS_TIC_ST_SCA, null);
            }
            else if (menuItem == mjTicTicketLab) {
                showView(SModConsts.S_TIC, SModSysConsts.SS_TIC_ST_LAB, null);
            }
            else if (menuItem == mjTicTicketAdm) {
                showView(SModConsts.S_TIC, SModSysConsts.SS_TIC_ST_ADM, null);
            }
            else if (menuItem == mjTicTicketAll) {
                showView(SModConsts.S_TIC, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjTicTarePend) {
                showView(SModConsts.SX_TIC_TARE, SModConsts.SX_TIC_TARE_PEND, null);
            }
            else if (menuItem == mjTicTare) {
                showView(SModConsts.SX_TIC_TARE, SModConsts.SX_TIC_TARE, null);
            }
            else if (menuItem == mjTicWahNAsigned) {
                showView(SModConsts.SX_TIC_WAH_UNLD, SModSysConsts.SS_TIC_WAH_UNLD_N_ASIGNED, null);
            }
            else if (menuItem == mjTicWahAsigned) {
                showView(SModConsts.SX_TIC_WAH_UNLD, SModSysConsts.SS_TIC_WAH_UNLD_ASIGNED, null);
            }
            else if (menuItem == mjQaLabTest) {
                showView(SModConsts.S_LAB, SModSysConsts.SX_LAB_TEST, null);
            }
            else if (menuItem == mjQaLabTestDet) {
                showView(SModConsts.S_LAB, SModSysConsts.SX_LAB_TEST_DET, null);
            }
            else if (menuItem == mjQaTicketLabTestDetFruit) {
                showView(SModConsts.SX_TIC_LAB_TEST_FRUIT, 0, null);
            }
            else if (menuItem == mjQaWahStart) {
                showView(SModConsts.S_WAH_START, 0, null);
            }
            else if (menuItem == mjQaOilMoiPond) {
                showView(SModConsts.SX_QA_OIL_MOI_POND, 0, null);
            }
            else if (menuItem == mjTicManSupplierItem) {
                showView(SModConsts.SX_TIC_MAN_SUP, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjTicManSupplierInputType) {
                showView(SModConsts.SX_TIC_MAN_SUP_INP_TP, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjTicRank) {
                showForm(SModConsts.SX_SEAS_REG, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjTicSearch) {
                new SDialogTicketsSearch(miClient).setVisible(true);
            }
            else if (menuItem == mjRepSeedReceived) {
                new SDialogRepReceivedSeed(miClient, SModConsts.SR_ITEM_REC, "Reporte materia prima recibida").setVisible(true);
            }
            else if (menuItem == mjRepReceivedFruit) {
                new SDialogRepReceivedFruit(miClient, SModConsts.SR_ITEM_FRUIT, "Reporte de fruta recibida").setVisible(true);
            }
            else if (menuItem == mjRepReceivedFruitHist) {
                new SDialogRepReceivedFruitHist(miClient, SModConsts.SR_ITEM_FRUIT_HIST, "Reporte comparativo histórico de fruta recibida").setVisible(true);
            }
            else if (menuItem == mjRepReceivedFruitAcidity) {
                new SDialogRepReceivedFruitAcidity(miClient, SModConsts.SR_ITEM_FRUIT_ACI, "Reporte de acidez fruta recibida").setVisible(true);
            }
            else if (menuItem == mjRepFruitYieldByOrigin) {
                new SDialogRepFruitYieldByOrigin(miClient, SModConsts.SR_FRUIT_YIELD_ORIG, "Reporte de rendimiento de fruta por origen").setVisible(true);
            }
            else if (menuItem == mjRepSeedReceivedByIodVal) {
                new SDialogRepIodineRank(miClient, SModConsts.SR_ITEM_REC_IOD_VAL, SModSysConsts.REP_LAB_TEST_IOD, "Reporte materia prima recibida por valor de yodo").setVisible(true);
            }
            else if (menuItem == mjRepSeedReceivedByPerOle) {
                new SDialogRepIodineRank(miClient, SModConsts.SR_ITEM_REC_IOD_VAL, SModSysConsts.REP_LAB_TEST_OLE, "Reporte materia prima recibida por porcentaje de ácido oleico").setVisible(true);
            }
            else if (menuItem == mjRepSeedReceivedByPerLin) {
                new SDialogRepIodineRank(miClient, SModConsts.SR_ITEM_REC_IOD_VAL, SModSysConsts.REP_LAB_TEST_LIN, "Reporte materia prima recibida por porcentaje de ácido linoleico").setVisible(true);
            }
            else if (menuItem == mjRepSeedReceivedByPerLlc) {
                new SDialogRepIodineRank(miClient, SModConsts.SR_ITEM_REC_IOD_VAL, SModSysConsts.REP_LAB_TEST_LLC, "Reporte materia prima recibida por porcentaje de ácido linolénico").setVisible(true);
            }
            else if (menuItem == mjRepSeedPayed) {
                new SDialogRepReceivedSeed(miClient, SModConsts.SR_ITEM_REC_PAY, "Reporte materia prima pagada").setVisible(true);
            }
            else if (menuItem == mjRepFreighTime) {
                new SDialogRepFreightTime(miClient, "Duración fletes de materia prima").setVisible(true);
            }
            else if (menuItem == mjRepTicLog) {
                showView(SModConsts.SX_TIC_LOG, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjRepCompTic) {
                new SDialogRepComparativeTicket(miClient, SModConsts.SR_TIC_COMP, "Reporte comparativo boletos SOM vs. Revuelta").setVisible(true);
            }
        }
    }
}
