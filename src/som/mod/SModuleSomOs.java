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
import som.gui.SGuiClientSessionCustom;
import som.mod.ext.db.SExtUtils;
import som.mod.som.db.SDbByProduct;
import som.mod.som.db.SDbClosingCalendar;
import som.mod.som.db.SDbIog;
import som.mod.som.db.SDbIogExportation;
import som.mod.som.db.SDbMfgEstimation;
import som.mod.som.db.SDbMix;
import som.mod.som.db.SDbOilAcidity;
import som.mod.som.db.SDbOilAcidityEntry;
import som.mod.som.db.SDbOilClass;
import som.mod.som.db.SDbOilGroupFamily;
import som.mod.som.db.SDbOilOwner;
import som.mod.som.db.SDbOilType;
import som.mod.som.db.SDbStock;
import som.mod.som.db.SDbStockDay;
import som.mod.som.db.SDbWahLab;
import som.mod.som.db.SDbWahLabTest;
import som.mod.som.db.SSomStockDays;
import som.mod.som.form.SDialogDailyStockReport;
import som.mod.som.form.SDialogRepIogList;
import som.mod.som.form.SDialogRepStock;
import som.mod.som.form.SDialogRepStockComparate;
import som.mod.som.form.SDialogRepStockDay;
import som.mod.som.form.SDialogRepStockMoves;
import som.mod.som.form.SFormByProduct;
import som.mod.som.form.SFormClosingCalendar;
import som.mod.som.form.SFormDialogIogExportation;
import som.mod.som.form.SFormDialogStockClosing;
import som.mod.som.form.SFormDialogWizardDps;
import som.mod.som.form.SFormIog;
import som.mod.som.form.SFormMix;
import som.mod.som.form.SFormOilAcidity;
import som.mod.som.form.SFormOilAcidityEntry;
import som.mod.som.form.SFormOilAcidityRow;
import som.mod.som.form.SFormOilClass;
import som.mod.som.form.SFormOilGroupFamily;
import som.mod.som.form.SFormOilOwner;
import som.mod.som.form.SFormOilType;
import som.mod.som.form.SFormProductionEstimate;
import som.mod.som.form.SFormStockDay;
import som.mod.som.form.SFormStockDays;
import som.mod.som.form.SFormWahLab;
import som.mod.som.form.SFormWahLabTest;
import som.mod.som.view.SViewByProduct;
import som.mod.som.view.SViewClosingCalendar;
import som.mod.som.view.SViewExternalDpsReturn;
import som.mod.som.view.SViewExternalDpsSupply;
import som.mod.som.view.SViewIog;
import som.mod.som.view.SViewIogExportation;
import som.mod.som.view.SViewIogProd;
import som.mod.som.view.SViewMfgEstimation;
import som.mod.som.view.SViewMfgEstimationEty;
import som.mod.som.view.SViewMfgEstimationProductionLine;
import som.mod.som.view.SViewMfgEstimationRm;
import som.mod.som.view.SViewMix;
import som.mod.som.view.SViewOilAcidity;
import som.mod.som.view.SViewOilAcidityEntry;
import som.mod.som.view.SViewOilClass;
import som.mod.som.view.SViewOilGroupFamily;
import som.mod.som.view.SViewOilOwner;
import som.mod.som.view.SViewOilType;
import som.mod.som.view.SViewStock;
import som.mod.som.view.SViewStockDays;
import som.mod.som.view.SViewStockDaysLog;
import som.mod.som.view.SViewStockMoves;
import som.mod.som.view.SViewTicketSupply;
import som.mod.som.view.SViewTicketSupplyDps;
import som.mod.som.view.SViewWahLab;

/**
 * 
 * @author Néstor Ávalos, Sergio Flores
 */
public class SModuleSomOs extends SGuiModule implements ActionListener {

    private JMenu mjCat;
    private JMenuItem mjCatProducer;
    private JMenuItem mjCatItem;
    private JMenuItem mjCatInputType;
    private JMenuItem mjCatInputClass;
    private JMenuItem mjCatInputCategory;
    private JMenuItem mjCatIodineValueRank;
    private JMenuItem mjCatAcidityRank;
    private JMenuItem mjCatExternalWarehouses;
    private JMenuItem mjCatByProduct;
    private JMenuItem mjCatOilClass; 
    private JMenuItem mjCatOilType; 
    private JMenuItem mjCatOilAci; 
    private JMenuItem mjCatOilAciEty; 
    private JMenuItem mjCatOilOwn;
    private JMenuItem mjCatOilGrpFam; 
    private JMenuItem mjCatClosingCalendar;
    private JMenuItem mjCatUpdateCatalogues;
    private JMenu mjTic;
    private JMenuItem mjTicSupply;
    private JMenuItem mjTicAssorted;
    private JMenuItem mjTicDpsSupply;
    private JMenuItem mjTicDpsAssorted;
    private JMenu mjDpsSupplyPur;
    private JMenuItem mjDpsSupplyPurPending;
    private JMenuItem mjDpsSupplyPurAssorted;
    private JMenuItem mjDpsSupplyPurDocuments;
    private JMenuItem mjAdjustmentSupplyPurchasesPending;
    private JMenuItem mjAdjustmentSupplyPurchasesAssorted;
    private JMenu mjDpsSupplySal;
    private JMenuItem mjDpsSupplySalPending;
    private JMenuItem mjDpsSupplySalAssorted;
    private JMenuItem mjDpsSupplySalDocuments;
    private JMenuItem mjAdjustmentSupplySalesPending;
    private JMenuItem mjAdjustmentSupplySalesAssorted;
    private JMenu mjDocInv;
    private JMenuItem mjDocInvDocs;
    private JMenuItem mjDocInvRmIn;
    private JMenuItem mjDocInvRmOut;
    private JMenuItem mjDocInvFgIn;
    private JMenuItem mjDocInvFgOut;
    private JMenuItem mjDocInvStkMoves;
    private JMenuItem mjDocInvStkMovesDet;
    private JMenuItem mjDocInvStkMovesEst;
    private JMenuItem mjDocInvOpenProc;
    private JMenu mjDocMix;
    private JMenuItem mjDocMixtures;
    private JMenu mjOil;
    private JMenuItem mjOilStockDays;
    private JMenuItem mjOilStockDaysLog;
    private JMenuItem mjOilWizardDps;
    private JMenuItem mjOilProdEstProc;
    private JMenuItem mjOilProdEstLog;
    private JMenuItem mjOilProdEstLogEty;
    private JMenuItem mjOilProdEstLogPL;
    private JMenuItem mjOilProdEstLogCons;
    private JMenuItem mjOilIogExpProc;
    private JMenuItem mjOilIogExpLog;
    private JMenuItem mjOilWahLab;
    private JMenu mjStk;
    private JMenuItem mjStkStock;
    private JMenuItem mjStkStockDiv;
    private JMenuItem mjStkStockWh;
    private JMenuItem mjStkStockWhDiv;
    private JMenu mjRep;
    private JMenuItem mjRepStock;
    private JMenuItem mjRepStockDiv;
    private JMenuItem mjRepStockWh;
    private JMenuItem mjRepStockWhDiv;
    private JMenuItem mjRepStockMoves;
    private JMenuItem mjRepStockMovesByIogTp;
    private JMenuItem mjRepIogList;
    private JMenuItem mjRepStockDay;
    private JMenuItem mjRepStockComp;
    private JMenuItem mjRepStockDaily;

    private SFormIog moFormIog;
    private SFormStockDay moFormStockDay;
    private SFormStockDays moFormStockDays;
    private SFormProductionEstimate moFormEstimateProduction;
    private SFormMix moFormMix;
    private SFormDialogIogExportation moFormDialogIogExportation;
    private SFormDialogStockClosing moFormDialogStockClosing;
    private SFormDialogWizardDps moFormDialogWizardDps;
    private SFormByProduct moFormByProduct;
    private SFormOilType moFormOilType;
    private SFormOilClass moFormOilClass;
    private SFormOilOwner moFormOilOwner;
    private SFormOilAcidity moFormOilAcidity;
    private SFormOilAcidityEntry moFormOilAcidityEntry;
    private SFormOilGroupFamily moFormOilGroupFamily;
    private SFormClosingCalendar moFormClosingCalendar;
    private SFormWahLab moFormWahLabWithLastTest;
    private SFormWahLab moFormWahLabWithoutLastTest;
    private SFormWahLabTest moFormWahLabTest;
    
    private SFormOilAcidity SFormOilAcidity;
    private SFormOilAcidityRow SFormOilAcidityEntry;

    public SModuleSomOs(SGuiClient client) {
        super(client, SModConsts.MOD_SOM_OS, SLibConsts.UNDEFINED);
        initComponents();
    }

    private void initComponents() {

        mjCat = new JMenu("Catálogos");
        mjCatProducer = new JMenuItem("Proveedores");
        mjCatItem = new JMenuItem("Ítems");
        mjCatInputType = new JMenuItem("Tipos de insumo");
        mjCatInputClass = new JMenuItem("Clases de insumo");
        mjCatInputCategory = new JMenuItem("Categorías de insumo");
        mjCatIodineValueRank = new JMenuItem("Rangos de yodo");
        mjCatAcidityRank = new JMenuItem("Rangos de acidez");
        mjCatExternalWarehouses = new JMenuItem("Almacenes sistema externo");
        mjCatByProduct = new JMenuItem("Procesos");
        mjCatOilType = new JMenuItem("Tipos de aceite");
        mjCatOilClass = new JMenuItem("Clases de aceite");
        mjCatOilAci = new JMenuItem("Vigencias de acidez aceite");
        mjCatOilAciEty = new JMenuItem("Acidez de aceite");
        mjCatOilOwn = new JMenuItem("Propietarios de aceite");
        mjCatOilGrpFam = new JMenuItem("Familias de aceite");
        mjCatClosingCalendar = new JMenuItem("Cierres de mes");
        mjCatUpdateCatalogues = new JMenuItem("Actualizar catálogos sistema externo...");

        mjCat.add(mjCatProducer);
        mjCat.add(mjCatItem);
        mjCat.addSeparator();
        mjCat.add(mjCatInputType);
        mjCat.add(mjCatInputClass);
        mjCat.add(mjCatInputCategory);
        mjCat.add(mjCatIodineValueRank);
        mjCat.add(mjCatAcidityRank);
        mjCat.add(mjCatExternalWarehouses);
        mjCat.add(mjCatByProduct);
        mjCat.addSeparator();
        mjCat.add(mjCatOilType); 
        mjCat.add(mjCatOilClass); 
        mjCat.addSeparator();
        mjCat.add(mjCatOilAci); 
        mjCat.add(mjCatOilAciEty); 
        mjCat.addSeparator();
        mjCat.add(mjCatOilOwn);
        mjCat.add(mjCatOilGrpFam); 
        mjCat.add(mjCatClosingCalendar);
        mjCat.addSeparator();
        mjCat.add(mjCatUpdateCatalogues);

        mjCatProducer.addActionListener(this);
        mjCatItem.addActionListener(this);
        mjCatInputType.addActionListener(this);
        mjCatInputClass.addActionListener(this);
        mjCatInputCategory.addActionListener(this);
        mjCatIodineValueRank.addActionListener(this);
        mjCatAcidityRank.addActionListener(this);
        mjCatExternalWarehouses.addActionListener(this);
        mjCatByProduct.addActionListener(this);
        mjCatOilType.addActionListener(this); 
        mjCatOilClass.addActionListener(this); 
        mjCatOilAci.addActionListener(this); 
        mjCatOilAciEty.addActionListener(this); 
        mjCatOilOwn.addActionListener(this);
        mjCatOilGrpFam.addActionListener(this); 
        mjCatClosingCalendar.addActionListener(this);
        mjCatUpdateCatalogues.addActionListener(this);

        mjTic = new JMenu("Boletos báscula");
        mjTicSupply = new JMenuItem("Boletos por surtir");
        mjTicAssorted = new JMenuItem("Boletos surtidos");
        mjTicDpsSupply = new JMenuItem("Boletos por facturar");
        mjTicDpsAssorted = new JMenuItem("Boletos facturados");

        mjTic.add(mjTicSupply);
        mjTic.add(mjTicAssorted);
        mjTic.addSeparator();
        mjTic.add(mjTicDpsSupply);
        mjTic.add(mjTicDpsAssorted);

        mjTicSupply.addActionListener(this);
        mjTicAssorted.addActionListener(this);
        mjTicDpsSupply.addActionListener(this);
        mjTicDpsAssorted.addActionListener(this);

        mjDpsSupplyPur = new JMenu("Movs. compras");
        mjDpsSupplyPurPending = new JMenuItem("Compras por surtir a detalle");
        mjDpsSupplyPurAssorted = new JMenuItem("Compras surtidas a detalle");
        mjAdjustmentSupplyPurchasesPending = new JMenuItem("Compras por devolver a detalle");
        mjAdjustmentSupplyPurchasesAssorted = new JMenuItem("Compras devueltas a detalle");
        mjDpsSupplyPurDocuments = new JMenuItem("Documentos de surtidos de compras");

        mjDpsSupplyPur.add(mjDpsSupplyPurPending);
        mjDpsSupplyPur.add(mjDpsSupplyPurAssorted);
        mjDpsSupplyPur.addSeparator();
        mjDpsSupplyPur.add(mjAdjustmentSupplyPurchasesPending);
        mjDpsSupplyPur.add(mjAdjustmentSupplyPurchasesAssorted);

        mjDpsSupplyPurPending.addActionListener(this);
        mjDpsSupplyPurAssorted.addActionListener(this);
        mjAdjustmentSupplyPurchasesPending.addActionListener(this);
        mjAdjustmentSupplyPurchasesAssorted.addActionListener(this);
        mjDpsSupplyPurDocuments.addActionListener(this);

        mjDpsSupplySal = new JMenu("Movs. ventas");
        mjDpsSupplySalPending = new JMenuItem("Ventas por surtir a detalle");
        mjDpsSupplySalAssorted = new JMenuItem("Ventas surtidas a detalle");
        mjAdjustmentSupplySalesPending = new JMenuItem("Ventas por devolver a detalle");
        mjAdjustmentSupplySalesAssorted = new JMenuItem("Ventas devueltas a detalle");
        mjDpsSupplySalDocuments = new JMenuItem("Documentos de surtidos de ventas");

        mjDpsSupplySal.add(mjDpsSupplySalPending);
        mjDpsSupplySal.add(mjDpsSupplySalAssorted);
        mjDpsSupplySal.addSeparator();
        mjDpsSupplySal.add(mjAdjustmentSupplySalesPending);
        mjDpsSupplySal.add(mjAdjustmentSupplySalesAssorted);

        mjDpsSupplySalPending.addActionListener(this);
        mjDpsSupplySalAssorted.addActionListener(this);
        mjAdjustmentSupplySalesPending.addActionListener(this);
        mjAdjustmentSupplySalesAssorted.addActionListener(this);
        mjDpsSupplySalDocuments.addActionListener(this);

        mjDocInv = new JMenu("Doctos. inventarios");
        mjDocInvDocs = new JMenuItem("Doctos. inventarios");
        mjDocInvRmIn = new JMenuItem("Doctos. entrega de materia prima (MP)");
        mjDocInvRmOut = new JMenuItem("Doctos. devolución de materia prima (MP)");
        mjDocInvFgIn = new JMenuItem("Doctos. entrega de producto terminado (PT)");
        mjDocInvFgOut = new JMenuItem("Doctos. devolución de producto terminado (PT)");
        mjDocInvStkMoves = new JMenuItem("Movimientos de inventarios");
        mjDocInvStkMovesDet = new JMenuItem("Movimientos de inventarios a detalle");
        mjDocInvStkMovesEst = new JMenuItem("Movimientos de inventarios de estimaciones");
        mjDocInvOpenProc = new JMenuItem("Generación de inventarios iniciales...");

        mjDocInv.add(mjDocInvDocs);
        mjDocInv.addSeparator();
        mjDocInv.add(mjDocInvRmIn);
        mjDocInv.add(mjDocInvRmOut);
        mjDocInv.addSeparator();
        mjDocInv.add(mjDocInvFgIn);
        mjDocInv.add(mjDocInvFgOut);
        mjDocInv.addSeparator();
        mjDocInv.add(mjDocInvStkMoves);
        mjDocInv.add(mjDocInvStkMovesDet);
        mjDocInv.add(mjDocInvStkMovesEst);
        mjDocInv.addSeparator();
        mjDocInv.add(mjDocInvOpenProc);

        mjDocInvDocs.addActionListener(this);
        mjDocInvRmIn.addActionListener(this);
        mjDocInvRmOut.addActionListener(this);
        mjDocInvFgIn.addActionListener(this);
        mjDocInvFgOut.addActionListener(this);
        mjDocInvStkMoves.addActionListener(this);
        mjDocInvStkMovesDet.addActionListener(this);
        mjDocInvStkMovesEst.addActionListener(this);
        mjDocInvOpenProc.addActionListener(this);

        mjDocMix = new JMenu("Doctos. mezclas");
        mjDocMixtures = new JMenuItem("Doctos. mezclas");

        mjDocMix.add(mjDocMixtures);

        mjDocMixtures.addActionListener(this);

        mjOil = new JMenu("Control aceites");
        mjOilStockDays = new JMenuItem("Inventarios físicos diarios");
        mjOilStockDaysLog = new JMenuItem("Bitácora inventarios físicos diarios");
        mjOilWizardDps = new JMenuItem("Movimientos externos...");
        mjOilProdEstProc = new JMenuItem("Estimación de la producción...");
        mjOilProdEstLog = new JMenuItem("Bitácora estimación de la producción");
        mjOilProdEstLogEty = new JMenuItem("Bitácora estimación por almacén");
        mjOilProdEstLogPL = new JMenuItem("Bitácora estimación por línea de producción");
        mjOilProdEstLogCons = new JMenuItem("Bitácora estimación de consumo por ítem");
        mjOilIogExpProc = new JMenuItem("Exportación de documentos de inventarios...");
        mjOilIogExpLog = new JMenuItem("Bitácora exportación de documentos de inventarios");
        mjOilWahLab = new JMenuItem("Análisis de laboratorio");

        mjOil.add(mjOilStockDays);
        mjOil.add(mjOilStockDaysLog);
        mjOil.addSeparator();
        /* XXX 2014-10-15 (navalos), review if is necessary wizard.
        mjOil.add(mjOilWizardDps);
        mjOil.addSeparator();
        */
        mjOil.add(mjOilProdEstProc);
        mjOil.add(mjOilProdEstLog);
        mjOil.add(mjOilProdEstLogEty);
        mjOil.add(mjOilProdEstLogPL);
        mjOil.add(mjOilProdEstLogCons);
        mjOil.addSeparator();
        mjOil.add(mjOilIogExpProc);
        mjOil.add(mjOilIogExpLog);
        mjOil.addSeparator();
        mjOil.add(mjOilWahLab);

        mjOilStockDays.addActionListener(this);
        mjOilProdEstProc.addActionListener(this);
        mjOilProdEstLog.addActionListener(this);
        mjOilProdEstLogEty.addActionListener(this);
        mjOilProdEstLogPL.addActionListener(this);
        mjOilProdEstLogCons.addActionListener(this);
        mjOilStockDaysLog.addActionListener(this);
        mjOilWizardDps.addActionListener(this);
        mjOilIogExpProc.addActionListener(this);
        mjOilIogExpLog.addActionListener(this);
        mjOilWahLab.addActionListener(this);

        mjStk = new JMenu("Existencias");
        mjStkStock = new JMenuItem("Existencias");
        mjStkStockDiv = new JMenuItem("Existencias por división");
        mjStkStockWh = new JMenuItem("Existencias por almacén");
        mjStkStockWhDiv = new JMenuItem("Existencias por almacén, división");

        mjStk.add(mjStkStock);
        mjStk.add(mjStkStockDiv);
        mjStk.add(mjStkStockWh);
        mjStk.add(mjStkStockWhDiv);

        mjStkStock.addActionListener(this);
        mjStkStockDiv.addActionListener(this);
        mjStkStockWh.addActionListener(this);
        mjStkStockWhDiv.addActionListener(this);

        mjRep = new JMenu("Reportes");
        mjRepStock = new JMenuItem("Existencias...");
        mjRepStockDiv = new JMenuItem("Existencias por división...");
        mjRepStockWh = new JMenuItem("Existencias por almacén...");
        mjRepStockWhDiv = new JMenuItem("Existencias por almacén, división...");
        mjRepStockMoves = new JMenuItem("Movimientos inventarios...");
        mjRepStockMovesByIogTp = new JMenuItem("Movimientos inventarios por tipo de movimiento...");
        mjRepIogList = new JMenuItem("Documentos inventarios...");
        mjRepStockDay = new JMenuItem("Inventario diario (toma física)...");
        mjRepStockComp = new JMenuItem("Inventario producción real vs. teórico...");
        mjRepStockDaily = new JMenuItem("Estimaciones e inventarios diarios...");

        mjRep.add(mjRepStock);
        mjRep.add(mjRepStockDiv);
        mjRep.add(mjRepStockWh);
        mjRep.add(mjRepStockWhDiv);
        mjRep.addSeparator();
        mjRep.add(mjRepStockMoves);
        mjRep.add(mjRepStockMovesByIogTp);
        mjRep.add(mjRepIogList);
        mjRep.addSeparator();
        mjRep.add(mjRepStockDay);
        mjRep.add(mjRepStockComp);
        mjRep.add(mjRepStockDaily);

        mjRepStock.addActionListener(this);
        mjRepStockDiv.addActionListener(this);
        mjRepStockWh.addActionListener(this);
        mjRepStockWhDiv.addActionListener(this);
        mjRepStockMoves.addActionListener(this);
        mjRepStockMovesByIogTp.addActionListener(this);
        mjRepIogList.addActionListener(this);
        mjRepStockDay.addActionListener(this);
        mjRepStockComp.addActionListener(this);
        mjRepStockDaily.addActionListener(this);

        // Privileges

        mjCat.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));

        mjTic.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));

        mjDpsSupplyPur.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));

        mjDpsSupplySal.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));

        mjDocInv.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM, SModSysConsts.CS_RIG_REP_OM }));
        mjDocMix.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM, SModSysConsts.CS_RIG_REP_OM }));
        mjDocInvDocs.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM}));
        mjDocInvRmIn.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));
        mjDocInvRmOut.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));
        mjDocInvFgIn.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));
        mjDocInvFgOut.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));
        mjDocInvStkMoves.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM, SModSysConsts.CS_RIG_REP_OM }));
        mjDocInvStkMovesDet.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM, SModSysConsts.CS_RIG_REP_OM }));
        mjDocInvStkMovesEst.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));
        mjDocInvOpenProc.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));

        mjOil.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));
        mjOilStockDays.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));
        mjOilProdEstProc.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));
        mjOilProdEstLog.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));
        mjOilProdEstLogEty.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));
        mjOilProdEstLogPL.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));
        mjOilProdEstLogCons.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));
        mjOilStockDaysLog.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));
        mjOilWizardDps.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM }));
        mjOilIogExpProc.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));
        mjOilIogExpLog.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));
        mjOilWahLab.setEnabled(miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_OM));

        mjStk.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM, SModSysConsts.CS_RIG_REP_OM }));

        mjRep.setEnabled(miClient.getSession().getUser().hasPrivilege(new int [] { SModSysConsts.CS_RIG_MAN_OM, SModSysConsts.CS_RIG_WHS_OM, SModSysConsts.CS_RIG_REP_OM }));
    }

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjTic, mjDpsSupplyPur, mjDpsSupplySal, mjDocInv, mjDocMix, mjOil, mjStk, mjRep };
    }

    @Override
    public SDbRegistry getRegistry(int type, SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.SS_IOG_CT:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_iog_ct = " + pk[0] + " "; }
                };
                break;
            case SModConsts.SS_IOG_CL:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_iog_ct = " + pk[0] + " AND id_iog_cl = " + pk[1] + " "; }
                };
                break;
            case SModConsts.SS_IOG_TP:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_iog_ct = " + pk[0] + " AND id_iog_cl = " + pk[1] + " AND id_iog_tp = " + pk[2] + " "; }
                };
                break;
            case SModConsts.SS_MIX_TP:
                registry = new SDbRegistrySysFly(type) {
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_mix_tp = " + pk[0] + " "; }
                };
                break;
            case SModConsts.S_STK_DAY:
                registry = new SDbStockDay();
                break;
            case SModConsts.S_STK:
                registry = new SDbStock();
                break;
            case SModConsts.S_IOG:
                registry = new SDbIog();
                break;
            case SModConsts.SX_STK_DAYS:
                registry = new SSomStockDays();
                break;
            case SModConsts.S_IOG_EXP:
                registry = new SDbIogExportation();
                break;
            case SModConsts.S_MFG_EST:
                registry = new SDbMfgEstimation();
                break;
            case SModConsts.S_MIX:
                registry = new SDbMix();
                break;
            case SModConsts.S_WAH_LAB:
                registry = new SDbWahLab();
                break;
            case SModConsts.S_WAH_LAB_TEST:
                registry = new SDbWahLabTest();
                break;
            case SModConsts.SX_EXT_DPS:
                //registry = new SRowSupplyDpsTicket();
                break;
            case SModConsts.SX_WIZ_DPS:
                break;
            case SModConsts.SU_BY_PRODUCT:
                registry = new SDbByProduct();
                break;
            case SModConsts.SU_OIL_TP:
                registry = new SDbOilType();
                break;
            case SModConsts.SU_OIL_CL:
                registry = new SDbOilClass();
                break;
            case SModConsts.SU_OIL_OWN: 
                registry = new SDbOilOwner();
                break;
            case SModConsts.SU_OIL_ACI: 
                registry = new SDbOilAcidity();
                break;
            case SModConsts.SU_OIL_ACI_ETY: 
                registry = new SDbOilAcidityEntry();
                break;
            case SModConsts.SU_OIL_GRP_FAM:
                registry = new SDbOilGroupFamily();
                break;
            case SModConsts.SU_CLOSING_CAL:
                registry = new SDbClosingCalendar();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(int type, int subtype, SGuiParams params) {
        String sql = "";
        String sDatabaseErpName = ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getExtDatabase().getDbName();
        String sDatabaseCoName = ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getExtDatabaseCo().getDbName();
        SGuiCatalogueSettings settings = null;
        Object value = null;
        String aux = "";

        switch (type) {
            case SModConsts.SS_IOG_CT:
                settings = new SGuiCatalogueSettings("Categoría mov. inv.", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_iog_ct AS " + SDbConsts.FIELD_ID + "1, "
                        + "name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.SS_IOG_CL:
                settings = new SGuiCatalogueSettings("Clase mov. inv.", 2, 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_iog_ct AS " + SDbConsts.FIELD_ID + "1, id_iog_cl AS " + SDbConsts.FIELD_ID + "2, "
                        + "name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + ", "
                        + "id_iog_ct AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.SS_IOG_TP:
                settings = new SGuiCatalogueSettings("Tipo mov. inv.", 3, 2);
                settings.setCodeApplying(true);
                sql = "SELECT id_iog_ct AS " + SDbConsts.FIELD_ID + "1, id_iog_cl AS " + SDbConsts.FIELD_ID + "2, id_iog_tp AS " + SDbConsts.FIELD_ID + "3, "
                        + "name AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + ", "
                        + "id_iog_ct AS " + SDbConsts.FIELD_FK + "1, id_iog_cl AS " + SDbConsts.FIELD_FK + "2 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.SU_IOG_ADJ_TP:
                settings = new SGuiCatalogueSettings("Tipo ajuste mov. inv.", 1);
                sql = "SELECT id_iog_adj_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY sort ";
                break;
            case SModConsts.SU_BY_PRODUCT:
                settings = new SGuiCatalogueSettings("Procesos", 1);
                sql = "SELECT id_by_product AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", " 
                        + "code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del AND NOT b_dis ORDER BY name;";
                break;
            case SModConsts.SU_OIL_CL:
                settings = new SGuiCatalogueSettings("Clases de aceite", 1);
                sql = "SELECT id_oil_cl AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", " 
                        + "code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del AND NOT b_dis ORDER BY name;";
                break;
            case SModConsts.SU_OIL_TP:
                settings = new SGuiCatalogueSettings("Tipo de aceite", 2);
                
                if (params != null) {
                    value = params.getParamsMap().get(SModConsts.SX_EXT_OIL_CL);
                    if (value != null) {
                        aux += "AND id_oil_cl = " + ((int[]) value)[0] + " ";
                    }
                }
                
                sql = "SELECT id_oil_cl AS " + SDbConsts.FIELD_ID + "1, id_oil_tp AS " + SDbConsts.FIELD_ID + "2, name AS " + SDbConsts.FIELD_ITEM + ", " 
                        + "code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del AND NOT b_dis " + aux + " ORDER BY name;";
                break;
            case SModConsts.SU_OIL_OWN:
                settings = new SGuiCatalogueSettings("Origen de aceite", 1);
                sql = "SELECT id_oil_own AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", " 
                        + "code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del AND NOT b_dis ORDER BY name;";
                break;
            case SModConsts.SX_EXT_DPS:
                settings = new SGuiCatalogueSettings("Partida documento CV", 3);
                sql = "SELECT v.id_year AS " + SDbConsts.FIELD_ID + "1, v.id_doc AS " + SDbConsts.FIELD_ID + "2, v.id_ety AS " + SDbConsts.FIELD_ID + "3, d.fid_bp_r, "
                    + "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num, '; ', d.dt, '; ', "
                    + "i.item, ' (', i.item_key, '); ', ' Cant ', v.orig_qty, '; Surt ', "
                    + "COALESCE(( SELECT SUM(ge.qty * CASE WHEN ge.fk_ext_adj_year_n IS NULL THEN 1 ELSE -1 END) "
                    + "FROM s_iog AS ge "
                    + "WHERE ge.fk_ext_dps_year_n = v.id_year AND ge.fk_ext_dps_doc_n = v.id_doc AND ge.fk_ext_dps_ety_n = v.id_ety  AND "
                    + "ge.b_del = 0), 0), ' ', uo.symbol, '; ', v.tot_cur_r, ' ', c.cur_key) AS f_item, "
                    + "v.orig_qty AS f_orig_qty, "
                    + "COALESCE(( SELECT SUM(ge.qty * CASE WHEN ge.fk_ext_adj_year_n IS NULL THEN 1 ELSE -1 END) "
                    + "FROM s_iog AS ge "
                    + "WHERE ge.fk_ext_dps_year_n = v.id_year AND ge.fk_ext_dps_doc_n = v.id_doc AND ge.fk_ext_dps_ety_n = v.id_ety  AND "
                    + "ge.b_del = 0), 0) AS f_sup_qty, "
                    + "COALESCE(( SELECT SUM(ddd.qty) "
                    + "FROM " + sDatabaseCoName + ".trn_dps_dps_adj AS ddd, " + sDatabaseCoName + ".trn_dps_ety AS dae, " + sDatabaseCoName + ".trn_dps AS da "
                    + "WHERE ddd.id_dps_year = v.id_year AND ddd.id_dps_doc = v.id_doc AND ddd.id_dps_ety = v.id_ety AND ddd.id_adj_year = dae.id_year AND "
                    + "ddd.id_adj_doc = dae.id_doc AND ddd.id_adj_ety = dae.id_ety AND dae.id_year = da.id_year AND dae.id_doc = da.id_doc AND "
                    + "dae.b_del = 0 AND dae.fid_tp_dps_adj = " + SModSysConsts.EXT_TRNS_TP_DPS_ADJ_RET + " AND da.b_del = 0 AND "
                    + "da.fid_st_dps = " + SModSysConsts.EXT_TRNS_ST_DPS_EMITED + "), 0) AS f_adj_qty, "
                    + "COALESCE(( SELECT SUM(ddd.orig_qty) "
                    + "FROM " + sDatabaseCoName + ".trn_dps_dps_adj AS ddd, " + sDatabaseCoName + ".trn_dps_ety AS dae, " + sDatabaseCoName + ".trn_dps AS da "
                    + "WHERE ddd.id_dps_year = v.id_year AND ddd.id_dps_doc = v.id_doc AND ddd.id_dps_ety = v.id_ety AND ddd.id_adj_year = dae.id_year AND "
                    + "ddd.id_adj_doc = dae.id_doc AND ddd.id_adj_ety = dae.id_ety AND dae.id_year = da.id_year AND dae.id_doc = da.id_doc AND "
                    + "dae.b_del = 0 AND dae.fid_tp_dps_adj = " + SModSysConsts.EXT_TRNS_TP_DPS_ADJ_RET + " AND da.b_del = 0 AND "
                    + "da.fid_st_dps = " + SModSysConsts.EXT_TRNS_ST_DPS_EMITED + "), 0) AS f_adj_orig_qty, "
                    + "COALESCE(( SELECT IF(d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0, 1, 0) "
                    + "FROM som_co.s_dps_ass AS som "
                    + "WHERE d.id_year = som.id_ext_dps_year AND d.id_doc = som.id_ext_dps_doc AND som.b_del = 0), 0) AS f_exist "
                    + "FROM " + sDatabaseCoName + ".trn_dps AS d "
                    + "INNER JOIN " + sDatabaseErpName + ".trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND "
                    + "d.b_del = 0 AND d.fid_st_dps = " + SModSysConsts.EXT_TRNS_ST_DPS_EMITED + " AND d.fid_ct_dps = " + SModSysConsts.EXT_TRNS_CL_DPS_PUR_DOC[0] + " AND "
                    + "d.fid_cl_dps = " + SModSysConsts.EXT_TRNS_CL_DPS_PUR_DOC[1] + " AND d.b_del = 0 "
                    + "INNER JOIN " + sDatabaseErpName + ".cfgu_cur AS c ON d.fid_cur = c.id_cur "
                    + "INNER JOIN " + sDatabaseErpName + ".bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb "
                    + "INNER JOIN " + sDatabaseErpName + ".bpsu_bp AS b ON d.fid_bp_r = b.id_bp AND d.fid_bp_r = " + params.getKey()[0] + " "
                    + "INNER JOIN " + sDatabaseErpName + ".bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " + SModSysConsts.EXT_BPSS_CT_BP_SUP + " "
                    + "INNER JOIN " + sDatabaseErpName + ".bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb "
                    + "INNER JOIN " + sDatabaseErpName + ".usru_usr AS uc ON d.fid_usr_close = uc.id_usr "
                    + "INNER JOIN " + sDatabaseCoName + ".trn_dps_ety AS v ON d.id_year = v.id_year AND d.id_doc = v.id_doc AND v.b_del = 0 AND v.b_inv = 1 AND v.qty > 0 AND "
                    + "v.orig_qty > 0 "
                    + "INNER JOIN " + sDatabaseErpName + ".itmu_item AS i ON v.fid_item = i.id_item "
                    + "INNER JOIN " + sDatabaseErpName + ".itmu_unit AS u ON v.fid_unit = u.id_unit "
                    + "INNER JOIN " + sDatabaseErpName + ".itmu_unit AS uo ON v.fid_orig_unit = uo.id_unit "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS sui ON i.id_item = sui.fk_ext_item_n AND sui.id_item = " + params.getKey()[1] + " "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS si ON i.id_item = si.fk_ext_item_n "
                    + "GROUP BY v.id_year, v.id_doc, v.id_ety, d.b_close "
                    + "HAVING (f_orig_qty - f_adj_orig_qty - f_sup_qty) <> 0 AND d.b_close = 0 AND f_exist = 0 "
                    + "ORDER BY d.num_ser, d.num, d.dt, item_key, item";
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
            case SModConsts.S_STK:
                switch (subtype) {
                    case SModConsts.SX_STK_STK:
                        view = new SViewStock(miClient, subtype, "Existencias");
                        break;
                    case SModConsts.SX_STK_DIV:
                        view = new SViewStock(miClient, subtype, "Existencias x división");
                        break;
                    case SModConsts.SX_STK_WAH_WAH:
                        view = new SViewStock(miClient, subtype, "Existencias x almacén");
                        break;
                    case SModConsts.SX_STK_WAH_DIV:
                        view = new SViewStock(miClient, subtype, "Existencias x almacén, división");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.SX_STK_MOVE:
                switch (subtype) {
                    case SModConsts.SX_STK_MOVE_DET:
                        view = new SViewStockMoves(miClient, subtype, "Movs. inventarios (detalle)");
                        break;
                    default:
                        view = new SViewStockMoves(miClient, subtype, "Movs. inventarios");
                }
                break;
            case SModConsts.S_IOG_EXP:
                view = new SViewIogExportation(miClient, "Bit. exportación doctos. inventarios");
                break;
            case SModConsts.S_IOG:
                switch (subtype) {
                    case SModConsts.SX_INV:
                        view = new SViewIog(miClient, "Doctos. inventarios", subtype);
                        break;
                    case SModConsts.SX_INV_IN_RM:
                        view = new SViewIog(miClient, "Doctos. entrega MP", subtype);
                        break;
                    case SModConsts.SX_INV_OUT_RM:
                        view = new SViewIog(miClient, "Doctos. devolución MP", subtype);
                        break;
                    case SModConsts.SX_INV_IN_FG:
                        view = new SViewIog(miClient, "Doctos. entrega PT", subtype);
                        break;
                    case SModConsts.SX_INV_OUT_FG:
                        view = new SViewIog(miClient, "Doctos. devolución PT", subtype);
                        break;
                }
                break;
            case SModConsts.S_WAH_LAB:
                view = new SViewWahLab(miClient, "Análisis laboratorio aceite");
                break;
            case SModConsts.SX_IOG_PROD:
                switch (subtype) {
                    case SModConsts.SX_INV_IN_FG:
                        view = new SViewIogProd(miClient, "Doctos. estimaciones", subtype);
                        break;
                }
                break;
            case SModConsts.S_MFG_EST:
                view = new SViewMfgEstimation(miClient, "Bit. estimación prod.");
                break;
            case SModConsts.S_MFG_EST_ETY:
                view = new SViewMfgEstimationEty(miClient, "Estimación por almacén");
                break;
            case SModConsts.S_MFG_EST_PL:
                view = new SViewMfgEstimationProductionLine(miClient, "Estimación por línea de producción");
                break;
            case SModConsts.S_MFG_EST_RM_CON:
                view = new SViewMfgEstimationRm(miClient, "Estimación de consumo por ítem");
                break;
            case SModConsts.S_MIX:
                view = new SViewMix(miClient, "Doctos. mezclas");
                break;
            case SModConsts.SX_STK_DAYS:
                view = new SViewStockDays(miClient, "Inv. físicos diarios");
                break;
            case SModConsts.SX_STK_DAYS_LOG:
                view = new SViewStockDaysLog(miClient, "Bit. inv. físicos diarios");
                break;
            case SModConsts.SX_TIC_SUP_RM:
                switch(subtype) {
                    case SModConsts.SX_TIC_MAN_SUP:
                        view = new SViewTicketSupply(miClient, subtype, "Boletos x surtir");
                        break;
                    case SModConsts.SX_TIC_ASSO:
                        view = new SViewTicketSupply(miClient, subtype, "Boletos surtidos");
                        break;
                }
                break;
            case SModConsts.SX_TIC_DPS:
                switch(subtype) {
                    case SModConsts.SX_TIC_DPS_SUP:
                        view = new SViewTicketSupplyDps(miClient, subtype, "Boletos x facturar");
                        break;
                    case SModConsts.SX_TIC_DPS_ASSO:
                        view = new SViewTicketSupplyDps(miClient, subtype, "Boletos facturados");
                        break;
                }
                break;
            case SModConsts.SX_IOG_SUP_PUR:
                switch (subtype) {
                    case SModConsts.SX_IOG_SUP_ADJ_PEN:
                        view = new SViewExternalDpsSupply(miClient, "Compras x surtir (detalle)", type, subtype);
                        break;
                    case SModConsts.SX_IOG_SUP_ADJ_ASSO:
                        view = new SViewExternalDpsSupply(miClient, "Compras surtidas (detalle)", type, subtype);
                        break;
                    case SModConsts.SX_IOG_SUP_ADJ_DOC:
                        view = new SViewIog(miClient, "Doctos. surtido compras", subtype);
                        break;
                }
                break;
            case SModConsts.SX_IOG_ADJ_PUR:
                switch (subtype) {
                    case SModConsts.SX_IOG_SUP_ADJ_PEN:
                        view = new SViewExternalDpsReturn(miClient, "Compras x devolver (detalle)", type, subtype);
                        break;
                    case SModConsts.SX_IOG_SUP_ADJ_ASSO:
                        view = new SViewExternalDpsReturn(miClient, "Compras devueltas (detalle)", type, subtype);
                        break;
                    case SModConsts.SX_IOG_SUP_ADJ_DOC:
                        view = new SViewIog(miClient, "Doctos. devolución compras", subtype);
                        break;
                }
                break;
            case SModConsts.SX_IOG_SUP_SAL:
                switch (subtype) {
                    case SModConsts.SX_IOG_SUP_ADJ_PEN:
                        view = new SViewExternalDpsSupply(miClient, "Ventas x surtir (detalle)", type, subtype);
                        break;
                    case SModConsts.SX_IOG_SUP_ADJ_ASSO:
                        view = new SViewExternalDpsSupply(miClient, "Ventas surtidas (detalle)", type, subtype);
                        break;
                    case SModConsts.SX_IOG_SUP_ADJ_DOC:
                        view = new SViewIog(miClient, "Doctos. surtido ventas", subtype);
                        break;
                }
                break;
            case SModConsts.SX_IOG_ADJ_SAL:
                switch (subtype) {
                    case SModConsts.SX_IOG_SUP_ADJ_PEN:
                        view = new SViewExternalDpsReturn(miClient, "Ventas x devolver (detalle)", type, subtype);
                        break;
                    case SModConsts.SX_IOG_SUP_ADJ_ASSO:
                        view = new SViewExternalDpsReturn(miClient, "Ventas devueltas (detalle)", type, subtype);
                        break;
                    case SModConsts.SX_IOG_SUP_ADJ_DOC:
                        view = new SViewIog(miClient, "Doctos. devolución ventas", subtype);
                        break;
                }
                break;
            case SModConsts.SU_BY_PRODUCT:
                view = new SViewByProduct(miClient, "Procesos");
                break;
            case SModConsts.SU_OIL_TP:
                view = new SViewOilType(miClient, "Tipos de aceite");
                break;
            case SModConsts.SU_OIL_CL:
                view = new SViewOilClass(miClient, "Clases de aceite");
                break;
            case SModConsts.SU_OIL_OWN: 
                view = new SViewOilOwner(miClient, "Propietarios de aceite");
                break;
            case SModConsts.SU_OIL_ACI: 
                view = new SViewOilAcidity(miClient, "Fechas de vigencia acidez de aceite");
                break;
            case SModConsts.SU_OIL_ACI_ETY: 
                view = new SViewOilAcidityEntry(miClient, "Acidez de aceite");
                break;
            case SModConsts.SU_OIL_GRP_FAM:
                view = new SViewOilGroupFamily(miClient, "Familias de aceite");
                break;
            case SModConsts.SU_CLOSING_CAL:
                view = new SViewClosingCalendar(miClient, "Cierres de mes");
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
            case SModConsts.S_STK_DAY:
                if (moFormStockDay == null) { moFormStockDay = new SFormStockDay(miClient, "Inventario físico diario"); }
                form = moFormStockDay;
                break;
            case SModConsts.SX_STK_DAYS:
                if (moFormStockDays == null) { moFormStockDays = new SFormStockDays(miClient, "Inventario físico diario"); }
                form = moFormStockDays;
                break;
            case SModConsts.S_IOG:
                if (moFormIog == null) { moFormIog = new SFormIog(miClient, "Documento inventario"); }
                form = moFormIog;
                break;
            case SModConsts.S_IOG_EXP:
                if (moFormDialogIogExportation == null) { moFormDialogIogExportation = new SFormDialogIogExportation(miClient, "Exportación de documentos de inventarios"); }
                form = moFormDialogIogExportation;
                break;
            case SModConsts.S_MFG_EST:
                if (moFormEstimateProduction == null) { moFormEstimateProduction = new SFormProductionEstimate(miClient, "Estimación de la producción"); }
                form = moFormEstimateProduction;
                break;
            case SModConsts.S_MIX:
                if (moFormMix == null) { moFormMix = new SFormMix(miClient, "Documento mezcla"); }
                form = moFormMix;
                break;
            case SModConsts.S_WAH_LAB:
                switch (subtype) {
                    case 0:
                        if (moFormWahLabWithoutLastTest == null) { moFormWahLabWithoutLastTest = new SFormWahLab(miClient, "Análisis laboratorio aceites", false); }
                        form = moFormWahLabWithoutLastTest;
                        break;
                    case 1:
                        if (moFormWahLabWithLastTest == null) { moFormWahLabWithLastTest = new SFormWahLab(miClient, "Análisis laboratorio aceites", true); }
                        form = moFormWahLabWithLastTest;
                        break;
                    default:
                }
                break;
            case SModConsts.S_WAH_LAB_TEST:
                if (moFormWahLabTest == null) { moFormWahLabTest = new SFormWahLabTest(miClient, "Almacenes e ítems"); }
                form = moFormWahLabTest;
                break;
            case SModConsts.SX_WIZ_DPS:
                if (moFormDialogWizardDps == null) { moFormDialogWizardDps = new SFormDialogWizardDps(miClient, "Movimientos externos"); }
                form = moFormDialogWizardDps;
                break;
            case SModConsts.SU_BY_PRODUCT:
                if (moFormByProduct == null) { moFormByProduct = new SFormByProduct(miClient, "Proceso"); }
                form = moFormByProduct;
                break;
            case SModConsts.SU_OIL_TP:
                if (moFormOilType == null) { moFormOilType = new SFormOilType(miClient, "Tipos de aceite"); }
                form = moFormOilType;
                break;
            case SModConsts.SU_OIL_CL:
                if (moFormOilClass == null) { moFormOilClass = new SFormOilClass(miClient, "Clases de aceite"); }
                form = moFormOilClass;
                break;
            case SModConsts.SU_OIL_OWN: 
                if (moFormOilOwner == null) { moFormOilOwner = new SFormOilOwner(miClient, "Propietarios de aceite"); }
                form = moFormOilOwner;
                break;
            case SModConsts.SU_OIL_ACI: 
                if (moFormOilAcidity == null) { moFormOilAcidity = new SFormOilAcidity(miClient, "Vigencia acidez aceite"); }
                form = moFormOilAcidity;
                break;
            case SModConsts.SU_OIL_ACI_ETY: 
                if (moFormOilAcidityEntry == null) { moFormOilAcidityEntry = new SFormOilAcidityEntry(miClient, "Acidez aceite"); }
                form = moFormOilAcidityEntry;
                break;
            case SModConsts.SU_OIL_GRP_FAM:
                if (moFormOilGroupFamily == null) { moFormOilGroupFamily = new SFormOilGroupFamily(miClient, "Familias de aceite"); }
                form = moFormOilType;
                break;
            case SModConsts.SU_CLOSING_CAL:
                if (moFormClosingCalendar == null) { moFormClosingCalendar = new SFormClosingCalendar(miClient, "Cierres de mes"); }
                form = moFormClosingCalendar;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        SGuiReport report = null;

        switch (type) {
            case SModConsts.SR_STK_DAY:
                report = new SGuiReport("reps/s_stk_day.jasper", "Reporte inventario diario (toma física)");
                break;
            case SModConsts.SR_STK:
                report = new SGuiReport("reps/s_stk.jasper", "Reporte existencias");
                break;
            case SModConsts.SR_STK_MOVE:
                report = new SGuiReport("reps/s_stk_mov.jasper", "Reporte movimientos inventarios");
                break;
            case SModConsts.SR_STK_COMP:
                report = new SGuiReport("reps/s_stk_comp.jasper", "Reporte inventario producción real vs. teórico");
                break;
            case SModConsts.SR_IOG_LIST:
                report = new SGuiReport("reps/s_iog_list.jasper", "Reporte documentos inventarios");
                break;
        }

        return report;
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
            else if (menuItem == mjCatInputType) {
                miClient.getSession().showView(SModConsts.SU_INP_TP, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatInputClass) {
                miClient.getSession().showView(SModConsts.SU_INP_CL, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatInputCategory) {
                miClient.getSession().showView(SModConsts.SU_INP_CT, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatIodineValueRank) {
                miClient.getSession().showView(SModConsts.SU_IOD_VAL_RANK, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatAcidityRank) {
                miClient.getSession().showView(SModConsts.SU_OIL_ACI, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatExternalWarehouses) {
                miClient.getSession().showView(SModConsts.SU_EXT_WAH, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatByProduct) {
                miClient.getSession().showView(SModConsts.SU_BY_PRODUCT, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatOilType) {
                miClient.getSession().showView(SModConsts.SU_OIL_TP, SLibConsts.UNDEFINED, null);
            } 
            else if (menuItem == mjCatOilClass) {
                miClient.getSession().showView(SModConsts.SU_OIL_CL, SLibConsts.UNDEFINED, null);
            } 
            else if (menuItem == mjCatOilAci) {
                miClient.getSession().showView(SModConsts.SU_OIL_ACI, SLibConsts.UNDEFINED, null);
            } 
            else if (menuItem == mjCatOilAciEty) {
                miClient.getSession().showView(SModConsts.SU_OIL_ACI_ETY, SLibConsts.UNDEFINED, null);
            } 
            else if (menuItem == mjCatOilOwn) {
                miClient.getSession().showView(SModConsts.SU_OIL_OWN, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatOilGrpFam) {
                miClient.getSession().showView(SModConsts.SU_OIL_GRP_FAM, SLibConsts.UNDEFINED, null);
            } 
            else if (menuItem == mjCatClosingCalendar) {
                miClient.getSession().showView(SModConsts.SU_CLOSING_CAL, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatUpdateCatalogues) {
                SExtUtils.updateCatalogues(miClient);
            }
            else if (menuItem == mjTicSupply) {
                showView(SModConsts.SX_TIC_SUP_RM, SModConsts.SX_TIC_MAN_SUP, null);
            }
            else if (menuItem == mjTicAssorted) {
                showView(SModConsts.SX_TIC_SUP_RM, SModConsts.SX_TIC_ASSO, null);
            }
            else if (menuItem == mjTicDpsSupply) {
                showView(SModConsts.SX_TIC_DPS, SModConsts.SX_TIC_DPS_SUP, null);
            }
            else if (menuItem == mjTicDpsAssorted) {
                showView(SModConsts.SX_TIC_DPS, SModConsts.SX_TIC_DPS_ASSO, null);
            }
            else if (menuItem == mjDpsSupplyPurPending) {
                showView(SModConsts.SX_IOG_SUP_PUR, SModConsts.SX_IOG_SUP_ADJ_PEN, null);
            }
            else if (menuItem == mjDpsSupplyPurAssorted) {
                showView(SModConsts.SX_IOG_SUP_PUR, SModConsts.SX_IOG_SUP_ADJ_ASSO, null);
            }
            else if (menuItem == mjDpsSupplyPurDocuments) {
                showView(SModConsts.SX_IOG_SUP_PUR, SModConsts.SX_IOG_SUP_ADJ_DOC, null);
            }
            else if (menuItem == mjAdjustmentSupplyPurchasesPending) {
                showView(SModConsts.SX_IOG_ADJ_PUR, SModConsts.SX_IOG_SUP_ADJ_PEN, null);
            }
            else if (menuItem == mjAdjustmentSupplyPurchasesAssorted) {
                showView(SModConsts.SX_IOG_ADJ_PUR, SModConsts.SX_IOG_SUP_ADJ_ASSO, null);
            }
            else if (menuItem == mjDpsSupplySalPending) {
                showView(SModConsts.SX_IOG_SUP_SAL, SModConsts.SX_IOG_SUP_ADJ_PEN, null);
            }
            else if (menuItem == mjDpsSupplySalAssorted) {
                showView(SModConsts.SX_IOG_SUP_SAL, SModConsts.SX_IOG_SUP_ADJ_ASSO, null);
            }
            else if (menuItem == mjDpsSupplySalDocuments) {
                showView(SModConsts.SX_IOG_SUP_SAL, SModConsts.SX_IOG_SUP_ADJ_DOC, null);
            }
            else if (menuItem == mjAdjustmentSupplySalesPending) {
                showView(SModConsts.SX_IOG_ADJ_SAL, SModConsts.SX_IOG_SUP_ADJ_PEN, null);
            }
            else if (menuItem == mjAdjustmentSupplySalesAssorted) {
                showView(SModConsts.SX_IOG_ADJ_SAL, SModConsts.SX_IOG_SUP_ADJ_ASSO, null);
            }
            else if (menuItem == mjDocInvDocs) {
                showView(SModConsts.S_IOG, SModConsts.SX_INV, null);
            }
            else if (menuItem == mjDocMixtures) {
                showView(SModConsts.S_MIX, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjDocInvRmIn) {
                showView(SModConsts.S_IOG, SModConsts.SX_INV_IN_RM, null);
            }
            else if (menuItem == mjDocInvRmOut) {
                showView(SModConsts.S_IOG, SModConsts.SX_INV_OUT_RM, null);
            }
            else if (menuItem == mjDocInvFgIn) {
                showView(SModConsts.S_IOG, SModConsts.SX_INV_IN_FG, null);
            }
            else if (menuItem == mjDocInvFgOut) {
                showView(SModConsts.S_IOG, SModConsts.SX_INV_OUT_FG, null);
            }
            else if (menuItem == mjDocInvStkMoves) {
                showView(SModConsts.SX_STK_MOVE, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjDocInvStkMovesDet) {
                showView(SModConsts.SX_STK_MOVE, SModConsts.SX_STK_MOVE_DET, null);
            }
            else if (menuItem == mjDocInvStkMovesEst) {
                showView(SModConsts.SX_IOG_PROD, SModConsts.SX_INV_IN_FG, null);
            }
            else if (menuItem == mjDocInvOpenProc) {
                new SFormDialogStockClosing(miClient, "Generación de inventarios iniciales").setVisible(true);
            }
            else if (menuItem == mjOilStockDays) {
                showView(SModConsts.SX_STK_DAYS, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilProdEstProc) {
                showForm(SModConsts.S_MFG_EST, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilProdEstLog) {
                showView(SModConsts.S_MFG_EST, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilProdEstLogEty) {
                showView(SModConsts.S_MFG_EST_ETY, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilProdEstLogPL) {
                showView(SModConsts.S_MFG_EST_PL, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilProdEstLogCons) {
                showView(SModConsts.S_MFG_EST_RM_CON, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilStockDaysLog) {
                showView(SModConsts.SX_STK_DAYS_LOG, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilWizardDps) {
                showForm(SModConsts.SX_WIZ_DPS, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilIogExpProc) {
                showForm(SModConsts.S_IOG_EXP, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilIogExpLog) {
                showView(SModConsts.S_IOG_EXP, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOilWahLab) {
                showView(SModConsts.S_WAH_LAB, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjStkStock) {
                showView(SModConsts.S_STK, SModConsts.SX_STK_STK, null);
            }
            else if (menuItem == mjStkStockDiv) {
                showView(SModConsts.S_STK, SModConsts.SX_STK_DIV, null);
            }
            else if (menuItem == mjStkStockWh) {
                showView(SModConsts.S_STK, SModConsts.SX_STK_WAH_WAH, null);
            }
            else if (menuItem == mjStkStockWhDiv) {
                showView(SModConsts.S_STK, SModConsts.SX_STK_WAH_DIV, null);
            }
            else if (menuItem == mjRepStock) {
                new SDialogRepStock(miClient, "Reporte existencias", SModConsts.SX_STK_STK).setVisible(true);
            }
            else if (menuItem == mjRepStockDiv) {
                new SDialogRepStock(miClient, "Reporte existencias por división", SModConsts.SX_STK_DIV).setVisible(true);
            }
            else if (menuItem == mjRepStockWh) {
                new SDialogRepStock(miClient, "Reporte existencias por almacén", SModConsts.SX_STK_WAH_WAH).setVisible(true);
            }
            else if (menuItem == mjRepStockWhDiv) {
                new SDialogRepStock(miClient, "Reporte existencias por almacén, división", SModConsts.SX_STK_WAH_DIV).setVisible(true);
            }
            else if (menuItem == mjRepStockMoves) {
                new SDialogRepStockMoves(miClient, "Reporte movimientos inventarios", SLibConsts.UNDEFINED).setVisible(true);
            }
            else if (menuItem == mjRepStockMovesByIogTp) {
                new SDialogRepStockMoves(miClient, "Reporte movimientos inventarios por tipo de movimiento", SModConsts.SS_IOG_TP).setVisible(true);
            }
            else if (menuItem == mjRepIogList) {
                new SDialogRepIogList(miClient, "Reporte documentos inventarios").setVisible(true);
            }
            else if (menuItem == mjRepStockDay) {
                new SDialogRepStockDay(miClient, "Reporte inventario diario (toma física)").setVisible(true);
            }
            else if (menuItem == mjRepStockComp) {
                new SDialogRepStockComparate(miClient, "Reporte inventario producción real vs. teórico").setVisible(true);
            }
            else if (menuItem == mjRepStockDaily) {
                new SDialogDailyStockReport(miClient).setVisible(true);
            }
        }
    }
}
