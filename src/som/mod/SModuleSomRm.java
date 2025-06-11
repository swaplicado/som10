/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
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
import som.mod.mat.db.SDbEmployee;
import som.mod.mat.db.SDbMaterialCondition;
import som.mod.mat.db.SDbShift;
import som.mod.mat.db.SDbStockMovement;
import som.mod.mat.form.SFormEmployee;
import som.mod.mat.form.SFormMaterialCondition;
import som.mod.mat.form.SFormShift;
import som.mod.mat.form.SFormWarehouseMovements;
import som.mod.mat.view.SViewEmployee;
import som.mod.mat.view.SViewMaterialCondition;
import som.mod.mat.view.SViewShift;
import som.mod.mat.view.SViewStock;
import som.mod.mat.view.SViewStockTransfer;
import som.mod.mat.view.SViewTicketMovements;
import som.mod.mat.view.SViewTicketReceptions;
import som.mod.mat.view.SViewWarehouseMovements;
import som.mod.som.db.SDbFreightOrigin;
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
import som.mod.som.db.SDbTicketAlternative;
import som.mod.som.db.SDbTicketDestination;
import som.mod.som.db.SDbTicketDivisionProcess;
import som.mod.som.db.SDbTicketOrigin;
import som.mod.som.db.SDbWarehouseStart;
import som.mod.som.form.SDialogRepFreightTime;
import som.mod.som.form.SDialogRepFruitYieldByOrigin;
import som.mod.som.form.SDialogRepIodineRank;
import som.mod.som.form.SDialogRepReceivedFruit;
import som.mod.som.form.SDialogRepReceivedFruitAcidity;
import som.mod.som.form.SDialogRepReceivedFruitHist;
import som.mod.som.form.SDialogRepReceivedSeed;
import som.mod.som.form.SDialogTicketDivisionProcess;
import som.mod.som.form.SDialogTicketsSearch;
import som.mod.som.form.SFormDialogAssignSeasonRegion;
import som.mod.som.form.SFormFreightOrigin;
import som.mod.som.form.SFormGrindingEvent;
import som.mod.som.form.SFormGrindingResultHr;
import som.mod.som.form.SFormLaboratory;
import som.mod.som.form.SFormLaboratoryAlternative;
import som.mod.som.form.SFormMgmtSupplierInputType;
import som.mod.som.form.SFormMgmtSupplierItem;
import som.mod.som.form.SFormRegion;
import som.mod.som.form.SFormSeason;
import som.mod.som.form.SFormSeasonProducer;
import som.mod.som.form.SFormSeasonRegion;
import som.mod.som.form.SFormSupraRegion;
import som.mod.som.form.SFormTicket;
import som.mod.som.form.SFormTicketAlternative;
import som.mod.som.form.SFormTicketDestination;
import som.mod.som.form.SFormTicketMgmt;
import som.mod.som.form.SFormTicketOrigin;
import som.mod.som.form.SFormTicketSeasonRegion;
import som.mod.som.form.SFormTicketWahUnld;
import som.mod.som.form.SFormWarehouseStart;
import som.mod.som.view.SViewFreightOrigin;
import som.mod.som.view.SViewGrindingEvents;
import som.mod.som.view.SViewGrindingResults;
import som.mod.som.view.SViewGrindingResume;
import som.mod.som.view.SViewLaboratory;
import som.mod.som.view.SViewLaboratoryAlternative;
import som.mod.som.view.SViewOilMoiPond;
import som.mod.som.view.SViewRegion;
import som.mod.som.view.SViewSeason;
import som.mod.som.view.SViewSeasonProducer;
import som.mod.som.view.SViewSeasonRegion;
import som.mod.som.view.SViewSupraRegion;
import som.mod.som.view.SViewTicket;
import som.mod.som.view.SViewTicketAlternative;
import som.mod.som.view.SViewTicketDestination;
import som.mod.som.view.SViewTicketOrigin;
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
    private JMenuItem mjCatItemAlternative;
    private JMenuItem mjCatInputType;
    private JMenuItem mjCatInputClass;
    private JMenuItem mjCatInputCategory;
    private JMenuItem mjCatInputSource;
    private JMenuItem mjCatReportingGroup;
    private JMenuItem mjCatTicketOrigin;
    private JMenuItem mjCatTicketDestination;
    private JMenuItem mjCatFreightOrigin;
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
    private JMenu mjGrindingSeed;   // Molienda oleaginosas
    private JMenuItem mjGriSeedEvents;
    private JMenuItem mjGriSeedResume;
    private JMenuItem mjGriSeedResults;
    private JMenu mjGrindingAvoc;   // Molienda aguacate
    private JMenuItem mjGriAvocEvents;
    private JMenuItem mjGriAvocResume;
    private JMenuItem mjGriAvocResults;
    private JMenu mjQa;    // Quality Assurrance
    private JMenuItem mjQaLabTest;
    private JMenuItem mjQaLabTestDet;
    private JMenuItem mjQaTicketLabTestDetFruit;
    private JMenuItem mjQaWahStart;
    private JMenuItem mjQaOilMoiPond;
    private JMenu mjStk;    // Stock
    private JMenuItem mjStkStk;
    private JMenuItem mjStkIn;
    private JMenuItem mjStkInDetail;
    private JMenuItem mjStkOut;
    private JMenuItem mjStkOutDetail;
    private JMenuItem mjStkTicxRec;
    private JMenuItem mjStkTicRecibed;
    private JMenuItem mjStkTicWithMov;
    private JMenuItem mjStkTicWithoutMov;
    private JMenuItem mjStkMatCond;
    private JMenuItem mjStkShift;
    private JMenuItem mjStkEmployee;
    private JMenuItem mjStkStkTransfer;
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
    private SFormLaboratoryAlternative moFormLaboratoryAlternative;
    private SFormTicket moFormTicket;
    private SFormTicketAlternative moFormTicketAlternative;
    private SFormTicket moFormTicketTare;
    private SFormWarehouseStart moFormWarehouseStart;
    private SFormTicketWahUnld moFormTicketWahUnld;
    private SFormTicketMgmt moFormTicketMgmt;
    private SFormTicketSeasonRegion moFormTicketSeasonRegion;
    private SFormSeasonRegion moFormSeasonRegion;
    private SFormSeasonProducer moFormSeasonProducer;
    private SFormTicketOrigin moFormTicketOrigin;
    private SFormTicketDestination moFormTicketDestination;
    private SFormFreightOrigin moFormFreightOrigin;
    private SFormDialogAssignSeasonRegion moFormDialogAssignSeasonRegion;
    private SFormMgmtSupplierItem moFormMgmtSupplierItem;
    private SFormMgmtSupplierInputType moFormMgmtSupplierInputType;
    private SFormMaterialCondition moFormMaterialCondition;
    private SFormShift moFormShift;
    private SFormEmployee moFormEmployee;
    private SFormWarehouseMovements moFormTicketMovements;
    private SDialogTicketDivisionProcess moDialogTicketDivision;

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
        mjCatItemAlternative = new JMenuItem("Ítems alternos");
        mjCatInputType = new JMenuItem("Tipos de insumo");
        mjCatInputClass = new JMenuItem("Clases de insumo");
        mjCatInputCategory = new JMenuItem("Categorías de insumo");
        mjCatInputSource = new JMenuItem("Orígenes de insumos");
        mjCatReportingGroup = new JMenuItem("Agrupadores de reporte");
        mjCatTicketOrigin = new JMenuItem("Procedencias de boletos");
        mjCatTicketDestination = new JMenuItem("Destinos de boletos");
        mjCatFreightOrigin = new JMenuItem("Origenes de fletes");
        mjCatIodineValueRank = new JMenuItem("Rangos de yodo");
        mjCatExternalWarehouses = new JMenuItem("Almacenes sistema externo");
        mjCatUpdateCatalogues = new JMenuItem("Actualizar catálogos sistema externo...");

        mjCat.add(mjCatProducer);
        mjCat.add(mjCatItem);
        mjCat.add(mjCatLot);
        mjCat.addSeparator();
        mjCat.add(mjCatItemAlternative);
        mjCat.addSeparator();
        mjCat.add(mjCatInputType);
        mjCat.add(mjCatInputClass);
        mjCat.add(mjCatInputCategory);
        mjCat.addSeparator();
        mjCat.add(mjCatInputSource);
        mjCat.add(mjCatReportingGroup);
        mjCat.addSeparator();
        mjCat.add(mjCatTicketOrigin);
        mjCat.add(mjCatTicketDestination);
        mjCat.addSeparator();
        mjCat.add(mjCatFreightOrigin);
        mjCat.addSeparator();
        mjCat.add(mjCatIodineValueRank);
        mjCat.addSeparator();
        mjCat.add(mjCatExternalWarehouses);
        mjCat.addSeparator();
        mjCat.add(mjCatUpdateCatalogues);

        mjCatProducer.addActionListener(this);
        mjCatItem.addActionListener(this);
        mjCatLot.addActionListener(this);
        mjCatItemAlternative.addActionListener(this);
        mjCatInputType.addActionListener(this);
        mjCatInputClass.addActionListener(this);
        mjCatInputCategory.addActionListener(this);
        mjCatInputSource.addActionListener(this);
        mjCatReportingGroup.addActionListener(this);
        mjCatTicketOrigin.addActionListener(this);
        mjCatTicketDestination.addActionListener(this);
        mjCatFreightOrigin.addActionListener(this);
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
        mjGrindingSeed = new JMenu("Molienda oleaginosas");
        mjGriSeedEvents = new JMenuItem("Eventos de molienda");
        mjGriSeedResume = new JMenuItem("Resumen de molienda");
        mjGriSeedResults = new JMenuItem("Resultados de molienda");
        mjGrindingAvoc = new JMenu("Molienda aguacate");
        mjGriAvocEvents = new JMenuItem("Eventos de aguacate");
        mjGriAvocResume = new JMenuItem("Resumen de aguacate");
        mjGriAvocResults = new JMenuItem("Resultados de aguacate");
        
        mjGrindingSeed.add(mjGriSeedEvents);
        mjGrindingSeed.add(mjGriSeedResume);
        mjGrindingSeed.add(mjGriSeedResults);
        
        mjGrindingAvoc.add(mjGriAvocEvents);
        mjGrindingAvoc.add(mjGriAvocResume);
        mjGrindingAvoc.add(mjGriAvocResults);
        
        mjGriSeedEvents.addActionListener(this);
        mjGriSeedResume.addActionListener(this);
        mjGriSeedResults.addActionListener(this);
        
        mjGriAvocEvents.addActionListener(this);
        mjGriAvocResume.addActionListener(this);
        mjGriAvocResults.addActionListener(this);

        mjQa = new JMenu("Control calidad");
        mjQaLabTest = new JMenuItem("Análisis de laboratorio de MP");
        mjQaLabTestDet = new JMenuItem("Análisis de laboratorio de MP a detalle");
        mjQaTicketLabTestDetFruit = new JMenuItem("Boletos fruta y análisis de laboratorio de MP");
        mjQaWahStart = new JMenuItem("Fechas de inicio de almacenes de MP");
        mjQaOilMoiPond = new JMenuItem("Cálculo ponderado aceite y humedad de MP");

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
        
        mjStk = new JMenu("Existencias");
        mjStkStk = new JMenuItem("Existencias de MP");
        mjStkIn = new JMenuItem("Entradas de almacén de MP");
        mjStkInDetail = new JMenuItem("Entradas de almacén de MP a detalle");
        mjStkOut = new JMenuItem("Salidas de almacén de MP");
        mjStkOutDetail = new JMenuItem("Salidas de almacén de MP a detalle");
        mjStkTicxRec = new JMenuItem("Boletos por recibir de MP");
        mjStkTicRecibed = new JMenuItem("Boletos recibidos de MP");
        mjStkTicWithoutMov = new JMenuItem("Boletos sin movimientos de almacén de MP");
        mjStkTicWithMov = new JMenuItem("Boletos con movimientos de almacén de MP");
        mjStkMatCond = new JMenuItem("Estados de MP");
        mjStkShift = new JMenuItem("Turnos de almacén de MP");
        mjStkEmployee = new JMenuItem("Empleados de almacén de MP");
        mjStkStkTransfer = new JMenuItem("Inventarios iniciales de MP");
        
        mjStk.add(mjStkStk);
        mjStk.addSeparator();
        mjStk.add(mjStkIn);
        mjStk.add(mjStkInDetail);
        mjStk.add(mjStkOut);
        mjStk.add(mjStkOutDetail);
        mjStk.addSeparator();
        mjStk.add(mjStkTicxRec);
        mjStk.add(mjStkTicRecibed);
        mjStk.add(mjStkTicWithoutMov);
        mjStk.add(mjStkTicWithMov);
        mjStk.addSeparator();
        mjStk.add(mjStkMatCond);
        mjStk.add(mjStkShift);
        mjStk.add(mjStkEmployee);
        mjStk.addSeparator();
        mjStk.add(mjStkStkTransfer);
        
        mjStkStk.addActionListener(this);
        mjStkIn.addActionListener(this);
        mjStkInDetail.addActionListener(this);
        mjStkOut.addActionListener(this);
        mjStkOutDetail.addActionListener(this);
        mjStkTicxRec.addActionListener(this);
        mjStkTicRecibed.addActionListener(this);
        mjStkTicWithoutMov.addActionListener(this);
        mjStkTicWithMov.addActionListener(this);
        mjStkMatCond.addActionListener(this);
        mjStkShift.addActionListener(this);
        mjStkEmployee.addActionListener(this);
        mjStkStkTransfer.addActionListener(this);

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
        
        mjGrindingSeed.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM, SModSysConsts.CS_RIG_LAB, SModSysConsts.CS_RIG_SUP_LAB, SModSysConsts.CS_RIG_DIS_RM }));
        mjGrindingAvoc.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM, SModSysConsts.CS_RIG_LAB, SModSysConsts.CS_RIG_SUP_LAB, SModSysConsts.CS_RIG_DIS_RM }));

        mjQa.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM, SModSysConsts.CS_RIG_LAB, SModSysConsts.CS_RIG_SUP_LAB, SModSysConsts.CS_RIG_DIS_RM }));
        mjQaWahStart.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_DIS_RM }));
        mjQaOilMoiPond.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_DIS_RM }));

        mjRep.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_MAN_RM, SModSysConsts.CS_RIG_REP_RM }));
        
        mjStk.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_RMEC, SModSysConsts.CS_RIG_RMES, SModSysConsts.CS_RIG_RMEA }));
        mjStkMatCond.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_RMEA));
        mjStkShift.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_RMEA));
        mjStkEmployee.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_RMEA));
    }

    /*
     * Public methods
     */

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjCfg, mjTic, mjGrindingSeed, mjGrindingAvoc, mjQa, mjStk, mjRep };
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
            case SModConsts.SU_TIC_ORIG:
                registry = new SDbTicketOrigin();
                break;
            case SModConsts.SU_TIC_DEST:
                registry = new SDbTicketDestination();
                break;
            case SModConsts.SU_FREIGHT_ORIG:
                registry = new SDbFreightOrigin();
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
            case SModConsts.S_ALT_TIC:
            case SModConsts.S_ALT_LAB:
                registry = new SDbTicketAlternative();
                break;
            case SModConsts.SX_TIC_MAN_SUP:
                registry = new SDbMgmtTicketsSupplierItem();
                break;
            case SModConsts.SX_TIC_MAN_SUP_INP_TP:
                registry = new SDbMgmtTicketsSupplierInputType();
                break;
            case SModConsts.MU_MAT_COND:
                registry = new SDbMaterialCondition();
                break;
            case SModConsts.MU_SHIFT:
                registry = new SDbShift();
                break;
            case SModConsts.MU_EMP:
                registry = new SDbEmployee();
                break;
            case SModConsts.M_MVT:
                registry = new SDbStockMovement();
                break;
            case SModConsts.SX_TIC_DIV_PROC:
                registry = new SDbTicketDivisionProcess();
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
            case SModConsts.SU_TIC_ORIG:
                settings = new SGuiCatalogueSettings("Procedencia del boleto", 1);
                sql = "SELECT id_tic_orig AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " " 
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del AND NOT b_dis ORDER BY name, id_tic_orig ";
                break;
            case SModConsts.SU_TIC_DEST:
                settings = new SGuiCatalogueSettings("Destino del boleto", 1);
                sql = "SELECT id_tic_dest AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " " 
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del AND NOT b_dis ORDER BY name, id_tic_dest ";
                break;
            case SModConsts.SU_FREIGHT_ORIG:
                settings = new SGuiCatalogueSettings("Origen del flete", 1);
                sql = "SELECT id_freight_orig AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " " 
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del AND NOT b_dis ORDER BY name, id_freight_orig ";
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
            case SModConsts.SX_PROD_REG_ITEM_SEAS:
                settings = new SGuiCatalogueSettings("Región", 1);
                int itemId = 0;
                int prodId = 0;
                int seasId = 0;
                if (params != null) {
                    itemId = (int) params.getParamsMap().get(SModConsts.SU_ITEM);
                    prodId = (int) params.getParamsMap().get(SModConsts.SU_PROD);
                    seasId = (int) params.getParamsMap().get(SModConsts.SU_SEAS);
                }
                sql = "SELECT s.id_reg AS " + SDbConsts.FIELD_ID + "1, re.name AS " + SDbConsts.FIELD_ITEM + " " 
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS s "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS re ON s.id_reg = re.id_reg "
                        + "WHERE s.b_del = 0 AND s.b_dis = 0 AND s.id_seas = " + seasId + " AND s.id_item = " + itemId + " AND s.id_prod = " + prodId + " " 
                        + "ORDER BY re.name, s.id_seas, s.id_reg, s.id_item, s.id_prod ";
                break;
            case SModConsts.SX_TIC_FREIGHT:
                int inputCt = 0;
                Date date = new Date();
                if (params != null) {
                    inputCt = (int) params.getParamsMap().get(SModConsts.SU_INP_CT);
                    date = (Date) params.getParamsMap().get(SModConsts.SX_TIC_DATE);
                }
                date = SLibTimeUtils.addDate(date, 0, -1, 0);
                settings = new SGuiCatalogueSettings("Boleto del flete", 1);
                sql = "SELECT t.id_tic AS " + SDbConsts.FIELD_ID + "1, t.num AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM s_tic AS t "
                        + "INNER JOIN su_item AS i ON t.fk_item = i.id_item "
                        + "WHERE t.req_freight = '" + SModSysConsts.SX_REQ_FRE_YES + "' "
                        + "AND t.freight_tic_tp = '" + SModSysConsts.SX_FREIGHT_TIC_TP_FRE + "' "
                        + "AND i.fk_inp_ct = " + inputCt + " AND t.dt >= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' "
                        + "ORDER BY t.num DESC ";
                break;
            case SModConsts.MS_MVT_CL:
                settings = new SGuiCatalogueSettings("Clase de movimiento", 2);
                sql = "SELECT id_iog_ct AS " + SDbConsts.FIELD_ID + "1, id_mvt_cl AS " + SDbConsts.FIELD_ID + "2, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY sort, name, id_iog_ct, id_mvt_cl ";
                break;
            case SModConsts.MS_EMP_TP:
                settings = new SGuiCatalogueSettings("Tipo de empleado de almacén", 1);
                sql = "SELECT id_emp_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY sort, name, id_emp_tp ";
                break;
            case SModConsts.MU_MAT_COND:
                settings = new SGuiCatalogueSettings("Estado de MP", 1);
                sql = "SELECT id_mat_cond AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del AND NOT b_dis "
                        + "ORDER BY sort, name, id_mat_cond ";
                break;
            case SModConsts.MU_SHIFT:
                settings = new SGuiCatalogueSettings("Turno de almacén de MP", 1);
                sql = "SELECT id_shift AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del AND id_shift <> 0 AND NOT b_dis "
                        + "ORDER BY sort, name, id_shift ";
                break;
            case SModConsts.MU_EMP:
                settings = new SGuiCatalogueSettings("Empleado de almacén de MP", 1);
                String where = "";
                if (subtype != SLibConsts.UNDEFINED) {
                    if (subtype == SModSysConsts.MS_EMP_TP_WAH_MAN) {
                        settings = new SGuiCatalogueSettings("Encargado de almacén", 1);
                    }
                    else if (subtype == SModSysConsts.MS_EMP_TP_MFG_SUP) {
                        settings = new SGuiCatalogueSettings("Supervisor de producción", 1);
                    }
                    where = "fk_emp_tp = " + subtype;
                }
                sql = "SELECT id_emp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del AND NOT b_sys AND NOT b_dis " 
                        + (where.isEmpty() ? "" : "AND " + where + " ")
                        + "ORDER BY name, id_emp ";
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
            case SModConsts.SU_TIC_ORIG:
                view = new SViewTicketOrigin(miClient, "Procedencias boletos");
                break;
            case SModConsts.SU_TIC_DEST:
                view = new SViewTicketDestination(miClient, "Destinos boletos");
                break;
            case SModConsts.SU_FREIGHT_ORIG:
                view = new SViewFreightOrigin(miClient, "Origenes fletes");
                break;
            case SModConsts.S_GRINDING_EVENT:
                switch (subtype) {
                    case SModSysConsts.CU_PLA_INT_PYE:
                        view = new SViewGrindingEvents(miClient, subtype, params.getKey(), "Eventos oleaginosas");
                        break;
                    case SModSysConsts.CU_PLA_INT_AGU:
                        view = new SViewGrindingEvents(miClient, subtype, params.getKey(), "Eventos aguacate");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.SX_GRINDING_RESUME:
                switch (subtype) {
                    case SModSysConsts.CU_PLA_INT_PYE:
                        view = new SViewGrindingResume(miClient, subtype, params.getKey(), "Resumen oleaginosas");
                        break;
                    case SModSysConsts.CU_PLA_INT_AGU:
                        view = new SViewGrindingResume(miClient, subtype, params.getKey(), "Resumen aguacate");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.S_GRINDING_RESULT:
                switch (subtype) {
                    case SModSysConsts.CU_PLA_INT_PYE:
                        view = new SViewGrindingResults(miClient, subtype, params.getKey(), "Resultados oleaginosas");
                        break;
                    case SModSysConsts.CU_PLA_INT_AGU:
                        view = new SViewGrindingResults(miClient, subtype, params.getKey(), "Resultados aguacate");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.S_LAB:
                switch (subtype) {
                    case SModSysConsts.SX_LAB_TEST:
                        view = new SViewLaboratory(miClient, subtype, "Análisis lab MP");
                        break;
                    case SModSysConsts.SX_LAB_TEST_DET:
                        view = new SViewLaboratory(miClient, subtype, "Análisis lab MP (detalle)");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.S_ALT_LAB:
                switch (subtype) {
                    case SModConsts.SX_ALT_WO_LAB:
                        view = new SViewLaboratoryAlternative(miClient, subtype, "Bol. pendientes resultados lab MP");
                        break;
                    case SModConsts.SX_ALT_W_LAB:
                        view = new SViewLaboratoryAlternative(miClient, subtype, "Bol. con resultados lab MP");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.S_WAH_START:
                view = new SViewWarehouseStart(miClient, "Fechas inicio almacenes MP");
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
            case SModConsts.S_ALT_TIC:
                view = new SViewTicketAlternative(miClient, "Boletos báscula");
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
                view = new SViewOilMoiPond(miClient, "Ponderado aceite humedad MP");
                break;
            case SModConsts.SX_TIC_LOG:
                view = new SViewTicketsLog(miClient, "Bitácora boletos");
                break;
            case SModConsts.SX_TIC_LAB_TEST_FRUIT:
                view = new SViewTicketsLaboratoryTestFruit(miClient, "Boletos fruta análisis lab");
                break;
            case SModConsts.MU_MAT_COND:
                view = new SViewMaterialCondition(miClient, "Estados MP");
                break;
            case SModConsts.MU_SHIFT:
                view = new SViewShift(miClient, "Turnos almacén MP");
                break;
            case SModConsts.MU_EMP:
                view = new SViewEmployee(miClient, "Empleados almacén MP");
                break;
            case SModConsts.M_MVT:
                switch (subtype) {
                    case SModSysConsts.SS_IOG_CT_IN:
                        if (params.getType() == SModSysConsts.MX_MVT) {
                            view = new SViewWarehouseMovements(miClient, subtype, "Entradas almacén MP", params);
                        }
                        else {
                            view = new SViewWarehouseMovements(miClient, subtype, "Entradas almacén MP (detalle)", params);
                        }
                        break;
                    case SModSysConsts.SS_IOG_CT_OUT:
                        if (params.getType() == SModSysConsts.MX_MVT) {
                            view = new SViewWarehouseMovements(miClient, subtype, "Salidas almacén MP", params);
                        }
                        else {
                            view = new SViewWarehouseMovements(miClient, subtype, "Salidas almacén MP (detalle)", params);
                        }
                        break;
                }
                break;
            case SModConsts.M_STK:
                view = new SViewStock(miClient, "Existencias MP");
                break;
            case SModConsts.MX_TIC_MVT:
                switch (subtype) {
                    case SModSysConsts.MX_TIC_WO_MVT_REC:
                        view = new SViewTicketMovements(miClient, subtype, "Boletos s/movimientos almacén MP");
                        break;
                    case SModSysConsts.MX_TIC_W_MVT_REC:
                        view = new SViewTicketMovements(miClient, subtype, "Boletos c/movimientos almacén MP");
                        break;
                }
                break;
            case SModConsts.MX_TIC_REC:
                switch (subtype) {
                    case SModSysConsts.MX_TIC_WO_MVT_REC:
                        view = new SViewTicketReceptions(miClient, subtype, "Boletos x recibir MP");
                        break;
                    case SModSysConsts.MX_TIC_W_MVT_REC:
                        view = new SViewTicketReceptions(miClient, subtype, "Boletos recibidos MP");
                        break;
                }
                break;
            case SModConsts.MX_STK_TRANS:
                view = new SViewStockTransfer(miClient, "Inventarios iniciales MP");
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
            case SModConsts.SU_TIC_ORIG:
                if (moFormTicketOrigin == null) moFormTicketOrigin = new SFormTicketOrigin(miClient, "Procedencia del boleto");
                form = moFormTicketOrigin;
                break;
            case SModConsts.SU_TIC_DEST:
                if (moFormTicketDestination == null) moFormTicketDestination = new SFormTicketDestination(miClient, "Destino del boleto");
                form = moFormTicketDestination;
                break;
            case SModConsts.SU_FREIGHT_ORIG:
                if (moFormFreightOrigin == null) moFormFreightOrigin = new SFormFreightOrigin(miClient, "Origen del flete");
                form = moFormFreightOrigin;
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
            case SModConsts.S_ALT_TIC:
                if (moFormTicketAlternative == null) moFormTicketAlternative = new SFormTicketAlternative(miClient, "Boleto", SLibConsts.UNDEFINED);
                form = moFormTicketAlternative;
                break;
            case SModConsts.S_WAH_START:
                if (moFormWarehouseStart == null) moFormWarehouseStart = new SFormWarehouseStart(miClient, "Inicio de almacén");
                form = moFormWarehouseStart;
                break;
            case SModConsts.SX_TIC_LAB:
                if (moFormLaboratory == null) moFormLaboratory = new SFormLaboratory(miClient, "Análisis de laboratorio");
                form = moFormLaboratory;
                break;
            case SModConsts.S_ALT_LAB:
                if (moFormLaboratoryAlternative == null) moFormLaboratoryAlternative = new SFormLaboratoryAlternative(miClient, "Análisis de laboratorio");
                form = moFormLaboratoryAlternative;
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
            case SModConsts.MU_MAT_COND:
                if (moFormMaterialCondition == null) moFormMaterialCondition = new SFormMaterialCondition(miClient, "Estado de MP");
                form = moFormMaterialCondition;
                break;
            case SModConsts.MU_SHIFT:
                if (moFormShift == null) moFormShift = new SFormShift(miClient, "Turno de almacén de MP");
                form = moFormShift;
                break;
            case SModConsts.MU_EMP:
                if (moFormEmployee == null) moFormEmployee = new SFormEmployee(miClient, "Empleado de almacén de MP");
                form = moFormEmployee;
                break;
            case SModConsts.M_MVT:
                if (moFormTicketMovements == null) moFormTicketMovements = new SFormWarehouseMovements(miClient, "Movimientos de almacén de MP");
                moFormTicketMovements.setValue(SFormWarehouseMovements.GUI_PARAMS, params);
                form = moFormTicketMovements;
                break;
            case SModConsts.SX_TIC_DIV_PROC:
                if (moDialogTicketDivision == null) moDialogTicketDivision = new SDialogTicketDivisionProcess(miClient, "Division de boletos");
                form = moDialogTicketDivision;
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
            case SModConsts.SR_ALT_TIC:
                guiReport = new SGuiReport("reps/s_alt_tic.jasper", "Boleto SOM Aguacate");
                break;
            case SModConsts.SR_ALT_LAB:
                guiReport = new SGuiReport("reps/s_alt_lab.jasper", "Análisis laboratorio SOM Aguacate");
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
            case SModConsts.SR_ITM_TIC:
                guiReport = new SGuiReport("reps/s_itm_tic.jasper", "Reporte de lista de boletos de báscula por producto");
                break;
            case SModConsts.M_STK:
                guiReport = new SGuiReport("reps/m_stk_ballot.jasper", "Papeleta de almacén MP");
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
            else if (menuItem == mjCatItemAlternative) {
                miClient.getSession().showView(SModConsts.SX_ITEM_ALT, SLibConsts.UNDEFINED, null);
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
            else if (menuItem == mjCatTicketOrigin) {
                showView(SModConsts.SU_TIC_ORIG, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatTicketDestination) {
                showView(SModConsts.SU_TIC_DEST, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatFreightOrigin) {
                showView(SModConsts.SU_FREIGHT_ORIG, SLibConsts.UNDEFINED, null);
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
            else if (menuItem == mjGriSeedEvents) {
                showView(SModConsts.S_GRINDING_EVENT, SModSysConsts.CU_PLA_INT_PYE, new SGuiParams(SModSysConsts.CU_PLA_PYE));
            }
            else if (menuItem == mjGriSeedResume) {
                showView(SModConsts.SX_GRINDING_RESUME, SModSysConsts.CU_PLA_INT_PYE, new SGuiParams(SModSysConsts.CU_PLA_PYE));
            }
            else if (menuItem == mjGriSeedResults) {
                showView(SModConsts.S_GRINDING_RESULT, SModSysConsts.CU_PLA_INT_PYE, new SGuiParams(SModSysConsts.CU_PLA_PYE));
            }
            else if (menuItem == mjGriAvocEvents) {
                showView(SModConsts.S_GRINDING_EVENT, SModSysConsts.CU_PLA_INT_AGU, new SGuiParams(SModSysConsts.CU_PLA_AGU));
            }
            else if (menuItem == mjGriAvocResume) {
                showView(SModConsts.SX_GRINDING_RESUME, SModSysConsts.CU_PLA_INT_AGU, new SGuiParams(SModSysConsts.CU_PLA_AGU));
            }
            else if (menuItem == mjGriAvocResults) {
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
            else if (menuItem == mjStkStk) {
                showView(SModConsts.M_STK, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjStkIn) {
                showView(SModConsts.M_MVT, SModSysConsts.SS_IOG_CT_IN, new SGuiParams(SModSysConsts.MX_MVT));
            }
            else if (menuItem == mjStkInDetail) {
                showView(SModConsts.M_MVT, SModSysConsts.SS_IOG_CT_IN, new SGuiParams(SModSysConsts.MX_MVT_DETAIL));
            }
            else if (menuItem == mjStkOut) {
                showView(SModConsts.M_MVT, SModSysConsts.SS_IOG_CT_OUT, new SGuiParams(SModSysConsts.MX_MVT));
            }
            else if (menuItem == mjStkOutDetail) {
                showView(SModConsts.M_MVT, SModSysConsts.SS_IOG_CT_OUT, new SGuiParams(SModSysConsts.MX_MVT_DETAIL));
            }
            else if (menuItem == mjStkTicxRec) {
                showView(SModConsts.MX_TIC_REC, SModSysConsts.MX_TIC_WO_MVT_REC, null);
            }
            else if (menuItem == mjStkTicRecibed) {
                showView(SModConsts.MX_TIC_REC, SModSysConsts.MX_TIC_W_MVT_REC, null);
            }
            else if (menuItem == mjStkTicWithoutMov) {
                showView(SModConsts.MX_TIC_MVT, SModSysConsts.MX_TIC_WO_MVT_REC, null);
            }
            else if (menuItem == mjStkTicWithMov) {
                showView(SModConsts.MX_TIC_MVT, SModSysConsts.MX_TIC_W_MVT_REC, null);
            }
            else if (menuItem == mjStkMatCond) {
                showView(SModConsts.MU_MAT_COND, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjStkShift) {
                showView(SModConsts.MU_SHIFT, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjStkEmployee) {
                showView(SModConsts.MU_EMP, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjStkStkTransfer) {
                showView(SModConsts.MX_STK_TRANS, SLibConsts.UNDEFINED, null);
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
