/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbIog;
import som.mod.som.db.SDbMfgEstimation;
import som.mod.som.db.SRowProductionByLine;
import som.mod.som.db.SSomMfgWarehouseProduct;
import som.mod.som.db.SSomProductionEstimateDistributeWarehouses;
import som.mod.som.db.SSomProductionEstimateToDistribute;
import som.mod.som.db.SSomStock;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDialogMfgEstimationFg extends SBeanFormDialog implements ActionListener {

    private SDbMfgEstimation moMfgEstimation;
    private SDialogMfgEstimationFgHandingOver moDlgMfgEstimationFgHandingOver;
    private SGridPaneForm moGridProducts;
    private ArrayList<SSomMfgWarehouseProduct> maProductionProducts;
    private SDialogProductionByLine moDetailDialog;

    /**
     * Creates new form SDialogProductionEstimateFg
     */
    public SDialogMfgEstimationFg(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.SX_STK_PROD_FG, SLibConsts.UNDEFINED, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlQuantity = new javax.swing.JLabel();
        moDecQuantity = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel9 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        moDateDate = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel29 = new javax.swing.JPanel();
        jlUnit = new javax.swing.JLabel();
        moKeyUnit = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel30 = new javax.swing.JPanel();
        jlDivision = new javax.swing.JLabel();
        moKeyDivision = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel31 = new javax.swing.JPanel();
        jlDivision1 = new javax.swing.JLabel();
        jbViewProductionDetail = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jpWarehouses = new javax.swing.JPanel();
        jbProductDelivery = new javax.swing.JButton();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlQuantity.setText("Cantidad estimada producida:*");
        jlQuantity.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(jlQuantity);
        jPanel3.add(moDecQuantity);

        jPanel2.add(jPanel3);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha:");
        jlDate.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel9.add(jlDate);

        moDateDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(moDateDate);

        jPanel2.add(jPanel9);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUnit.setText("Unidad:");
        jlUnit.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel29.add(jlUnit);

        moKeyUnit.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel29.add(moKeyUnit);

        jPanel2.add(jPanel29);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDivision.setText("División:*");
        jlDivision.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel30.add(jlDivision);

        moKeyDivision.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel30.add(moKeyDivision);

        jPanel2.add(jPanel30);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDivision1.setText("Detalle de producción:");
        jlDivision1.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel31.add(jlDivision1);

        jbViewProductionDetail.setText("Por línea de producción");
        jbViewProductionDetail.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel31.add(jbViewProductionDetail);

        jPanel2.add(jPanel31);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel6.setLayout(new java.awt.GridLayout(1, 2));

        jpWarehouses.setBorder(javax.swing.BorderFactory.createTitledBorder("Almacenes:"));
        jpWarehouses.setLayout(new java.awt.BorderLayout());

        jbProductDelivery.setText("Entregar producción");
        jbProductDelivery.setToolTipText("Entregar producto");
        jbProductDelivery.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbProductDelivery.setPreferredSize(new java.awt.Dimension(150, 23));
        jpWarehouses.add(jbProductDelivery, java.awt.BorderLayout.PAGE_START);

        jPanel6.add(jpWarehouses);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbProductDelivery;
    private javax.swing.JButton jbViewProductionDetail;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDivision;
    private javax.swing.JLabel jlDivision1;
    private javax.swing.JLabel jlQuantity;
    private javax.swing.JLabel jlUnit;
    private javax.swing.JPanel jpWarehouses;
    private sa.lib.gui.bean.SBeanFieldDate moDateDate;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecQuantity;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDivision;
    private sa.lib.gui.bean.SBeanFieldKey moKeyUnit;
    // End of variables declaration//GEN-END:variables

    /*
    * Private methods
    */

    private void initComponentsCustom() {
        moMfgEstimation = new SDbMfgEstimation();


        jbCancel.setText("Cerrar");
        jbSave.setEnabled(false);

        SGuiUtils.setWindowBounds(this, 800, 500);

        moDecQuantity.setDecimalSettings(SGuiUtils.getLabelName(jlQuantity.getText()), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDateDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlDate.getText()), true);
        moKeyUnit.setKeySettings(miClient, SGuiUtils.getLabelName(jlUnit), true);
        moKeyDivision.setKeySettings(miClient, SGuiUtils.getLabelName(jlDivision.getText()), true);

        moFields.addField(moDecQuantity);
        moFields.addField(moDateDate);
        moFields.addField(moKeyUnit);
        moFields.addField(moKeyDivision);

        jbProductDelivery.addActionListener(this);
        jbViewProductionDetail.addActionListener(this);

        moGridProducts = new SGridPaneForm(miClient, SModConsts.SX_STK_PROD_FG, SLibConsts.UNDEFINED, "Productos", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                SGridColumnForm[] columns = new SGridColumnForm[10];

                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Ítem");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Ítem código");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Sucursal");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Almacén");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Tipo almacén");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Línea prod");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Cantidad producida");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Cantidad entregada");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        mvFormGrids.add(moGridProducts);
        moDlgMfgEstimationFgHandingOver = new SDialogMfgEstimationFgHandingOver(miClient, "Entrega de producto");
        moGridProducts.setForm(moDlgMfgEstimationFgHandingOver);
        moGridProducts.setPaneFormOwner(null);
        moGridProducts.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbProductDelivery);
        
        moDetailDialog = new SDialogProductionByLine(miClient);

        jpWarehouses.add(moGridProducts, BorderLayout.CENTER);

        maProductionProducts = new ArrayList<>();
    
        removeAllListeners();
        reloadCatalogues();

        moDecQuantity.setEnabled(false);

        addAllListeners();

        /* XXX Cell renderers not implemented yet in XML
        mvFormGrids.add(moGridUserRights);
        mvFormGrids.add(moGridUserScales);
        */
    }

    private void populatePaneGrid() throws SQLException {
        Vector<SGridRow> rows = new Vector<>();

        for (SSomMfgWarehouseProduct row : maProductionProducts) {
            rows.add(row);
        }

        moGridProducts.populateGrid(rows);
    }

    private void actionFinishedGoodDelivery() throws SQLException, Exception {
        boolean assignQuantityProduction = false;
        boolean existProduct = false;
        boolean fillWarehouses = false;
        boolean bContinue = true;
        double quantityTotalProductStorage = 0;
        double quantityByProductStorage = 0;
        double quantityTotalProductionDistribute = 0;
        String unitProduction = "";
        String unitStorage = "";

        SDbIog iogOut = null;
        SDbIog iogIn = null;

        SSomMfgWarehouseProduct rowWarehouseProduct = null;
        SSomMfgWarehouseProduct mfgWarehouseProductAux = null;
        SSomProductionEstimateDistributeWarehouses productionEstimateDistributeWarehouses = null;
        SSomProductionEstimateToDistribute productToDistribute = null;

        ArrayList<SSomMfgWarehouseProduct> aProductsToProductionWhs = new ArrayList<SSomMfgWarehouseProduct>();
        ArrayList<SSomMfgWarehouseProduct> aProductsToStorageWhs = new ArrayList<SSomMfgWarehouseProduct>();
        ArrayList<SSomProductionEstimateToDistribute> aProductionEstimateToDistributeByProduct = new ArrayList<SSomProductionEstimateToDistribute>();

        /*
        if (moGridProducts.getTable().getSelectedRow() == -1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
        */

        // Validate if any production warehouse has negative quantity:

        for (SGridRow row : moGridProducts.getModel().getGridRows()) {

            rowWarehouseProduct = ((SSomMfgWarehouseProduct) row).clone();
            if (rowWarehouseProduct.getQuantity() < 0) {

                assignQuantityProduction = true;
                quantityTotalProductionDistribute += rowWarehouseProduct.getQuantity();
                unitProduction = rowWarehouseProduct.getUnitCode();
            }

            // Assign production warehouse and storage warehouse to arrayList:
            if (rowWarehouseProduct.getFkWarehouseTypeId() == SModSysConsts.CS_WAH_TP_TAN) {

                aProductsToStorageWhs.add(rowWarehouseProduct);
                quantityTotalProductStorage += rowWarehouseProduct.getQuantity();
                unitStorage = rowWarehouseProduct.getUnitCode();
            }
            else {
                aProductsToProductionWhs.add(rowWarehouseProduct);
            }
        }

        // Validate whether the quantity should be distributed production warehouse into storage warehouse:

        try {
            fillWarehouses = true;
            if (assignQuantityProduction) {

                // Validate if quantity production by warehouse production by division is enough:

                if ((quantityTotalProductionDistribute * -1) > quantityTotalProductStorage) {

                    miClient.showMsgBoxError("La cantidad total a distribuir de los tanques de producción '"  +
                            SLibUtils.DecimalFormatValue4D.format(quantityTotalProductionDistribute * -1) + " " + unitProduction + "' es mayor a la cantidad total que pueden recibir\n" +
                            "los tanques de almacenamiento '" + SLibUtils.DecimalFormatValue4D.format(quantityTotalProductStorage) + " " + unitStorage + "'.");
                    fillWarehouses = false;
                    bContinue = false;
                }

                // Validate if quantity production by warehouse production by division is enough:
                
                if (fillWarehouses) {
                    for (SSomMfgWarehouseProduct rowProductWhsProduction : aProductsToProductionWhs) {
                        if (rowProductWhsProduction.getQuantity() < 0) {
                            double dStock = ((SSomStock) SSomUtils.obtainStock(miClient.getSession(), 
                                                SLibTimeUtils.digestYear(moDateDate.getValue())[0],
                                                rowProductWhsProduction.getPkItemId(), 
                                                rowProductWhsProduction.getPkUnitId(), 
                                                SModSysConsts.SS_ITEM_TP_FG,
                                                new int[] { rowProductWhsProduction.getPkCompanyId(), rowProductWhsProduction.getPkBranchId(), rowProductWhsProduction.getPkWarehouseId() },
                                                moKeyDivision.getValue()[0], 
                                                null, 
                                                SLibTimeUtils.addDate(moDateDate.getValue(), 0, 0, -1), 
                                                false, 
                                                false)).getStock();

                            if (dStock < (rowProductWhsProduction.getQuantity() * -1)) {
                                miClient.showMsgBoxError("No hay existencias sufientes para la división '" +
                                        moKeyDivision.getSelectedItem().getCode() + "' en el tanque de producción '" + rowProductWhsProduction.getWarehouseCode() + "'.\n" +
                                        "Ítem '" + rowProductWhsProduction.getItem() + " (" + rowProductWhsProduction.getItemCode() + ")" + "'.");
                                fillWarehouses = false;
                                bContinue = false;
                                break;
                            }
                        }
                    }
                }

                // Validate if quantity production by warehouse production can be transfer to warehouse storage bases in source product one and two:

                if (fillWarehouses) {

                    // Obtain products of warehouses production to distribute production:

                    for (SSomMfgWarehouseProduct mfgWhsproductProduction : aProductsToProductionWhs) {
                        if (mfgWhsproductProduction.getQuantity() < 0) {

                            existProduct = false;
                            for (SSomProductionEstimateToDistribute productDistribute : aProductionEstimateToDistributeByProduct) {

                                if (mfgWhsproductProduction.getPkItemId() == productDistribute.getFkItemId() &&
                                        mfgWhsproductProduction.getPkUnitId() == productDistribute.getFkUnitId()) {

                                    productDistribute.setProductionAvailable(productDistribute.getProductionAvailable() + (mfgWhsproductProduction.getQuantity() * -1));
                                    existProduct = true;
                                    break;
                                }
                            }

                            if (!existProduct) {
                                productToDistribute = new SSomProductionEstimateToDistribute();

                                productToDistribute.setProductionAvailable(mfgWhsproductProduction.getQuantity() * -1);
                                productToDistribute.setItem(mfgWhsproductProduction.getItem());
                                productToDistribute.setUnitCode(mfgWhsproductProduction.getUnitCode());
                                productToDistribute.setFkItemId(mfgWhsproductProduction.getPkItemId());
                                productToDistribute.setFkUnitId(mfgWhsproductProduction.getPkUnitId());

                                aProductionEstimateToDistributeByProduct.add(productToDistribute);
                            }
                        }
                    }

                    // Validate if production to distribute by product can be transfer to products of warehouse storage:

                    for (SSomProductionEstimateToDistribute productDistribute : aProductionEstimateToDistributeByProduct) {

                        quantityByProductStorage = 0;
                        for (SSomMfgWarehouseProduct mfgWhsProductStorage : aProductsToStorageWhs) {

                            if ((productDistribute.getFkItemId() == mfgWhsProductStorage.getFkItemSource1Id_n()) ||
                                 (productDistribute.getFkItemId() == mfgWhsProductStorage.getFkItemSource2Id_n())) {

                                quantityByProductStorage += mfgWhsProductStorage.getQuantity();
                            }

                        }

                        if (productToDistribute.getProductionAvailable() > quantityByProductStorage) {

                            miClient.showMsgBoxError("No se puede distribuir la producción estimada del ítem: '" + productToDistribute.getItem() + "'.\n"
                                    + "Debido a que la producción a distruir es de: '" + SLibUtils.DecimalFormatValue4D.format(productToDistribute.getProductionAvailable()) + " " + unitProduction + "' \n"
                                    + "y la producción que pueden recibir los tanques de almacenamiento es de: '" + SLibUtils.DecimalFormatValue4D.format(quantityByProductStorage) + " " + unitProduction + "'");
                            fillWarehouses = false;
                            bContinue = false;
                            break;
                        }
                    }
                }

                // Storage warehouses sort ascending amount:

                if (fillWarehouses) {
                    for (int i=0; i<aProductsToStorageWhs.size()-1; i++) {
                        for (int j=i+1; j<aProductsToStorageWhs.size(); j++) {

                            rowWarehouseProduct = aProductsToStorageWhs.get(i);
                            mfgWarehouseProductAux = aProductsToStorageWhs.get(j);
                            if (rowWarehouseProduct.getQuantity() > mfgWarehouseProductAux.getQuantity()) {
                                aProductsToStorageWhs.set(i, mfgWarehouseProductAux);
                                aProductsToStorageWhs.set(j, rowWarehouseProduct);
                            }
                        }
                    }

                    // Distribute production of warehouse production to warehouse storage:

                    productionEstimateDistributeWarehouses = SSomUtils.productionEstimateDistribute(miClient.getSession(), moDateDate.getValue(), moKeyDivision.getValue()[0],
                    moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion(), aProductsToProductionWhs, aProductsToStorageWhs);

                    // Validate whether the production is fully distributed:

                    for (SSomMfgWarehouseProduct mfgWhsProductProduction : productionEstimateDistributeWarehouses.getMfgWarehouseProductProduction()) {
                        if (mfgWhsProductProduction.getQuantity() < 0) {
                            miClient.showMsgBoxError("No se pudo distribuir la producción estimada del almacén: '" + mfgWhsProductProduction.getWarehouseCode() + "'.");
                            fillWarehouses = false;
                            bContinue = false;
                        }
                    }
                }
            }

            // Fill warehouses:

            if (fillWarehouses) {

                // Create in/out iogs moves of warehouse production:

                if (assignQuantityProduction) {
                    for (int i=0; i<productionEstimateDistributeWarehouses.getIogOut().size(); i++) {
                        iogOut = productionEstimateDistributeWarehouses.getIogOut().get(i);
                        iogIn = productionEstimateDistributeWarehouses.getIogIn().get(i);

                        if (iogOut.canSave(miClient.getSession())) {

                            iogOut.save(miClient.getSession());
                            if (iogOut.getQueryResultId() == SDbConsts.SAVE_OK) {

                                iogIn.setFkIogId_n(iogOut.getPkIogId()); // Transfer movement id
                                if (iogIn.canSave(miClient.getSession())) {

                                    iogIn.save(miClient.getSession());
                                    if (iogIn.getQueryResultId() == SDbConsts.SAVE_ERROR) {

                                        SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
                                        miClient.showMsgBoxError(iogIn.getQueryResult());
                                        bContinue = false;
                                        break;
                                    }
                                }
                            }
                            else {

                                SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
                                miClient.showMsgBoxError(iogOut.getQueryResult());
                                bContinue = false;
                                break;
                            }
                        }
                        else {

                            SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
                            miClient.showMsgBoxError(iogOut.getQueryResult());
                            bContinue = false;
                            break;
                        }
                    }
                }

                // Create in moves of warehouse production:

                if (bContinue) {
                    if (productionEstimateDistributeWarehouses == null) {

                        productionEstimateDistributeWarehouses = new SSomProductionEstimateDistributeWarehouses();
                        productionEstimateDistributeWarehouses.getMfgWarehouseProductProduction().addAll(aProductsToProductionWhs);
                        productionEstimateDistributeWarehouses.getMfgWarehouseProductStorage().addAll(aProductsToStorageWhs);
                    }

                    for (SSomMfgWarehouseProduct product : productionEstimateDistributeWarehouses.getMfgWarehouseProductProduction()) {

                        bContinue = createIogInProduction(product);
                        if (!bContinue) {
                            SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
                            break;
                        }
                    }
                }

                if (bContinue) {

                    // Create in moves of warehouse storage:

                    for (SSomMfgWarehouseProduct product : productionEstimateDistributeWarehouses.getMfgWarehouseProductStorage()) {

                        bContinue = createIogInProduction(product);
                        if (!bContinue) {
                            SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
                            break;
                        }
                    }
                }
            }

            // Validate that the physical and system stock equal existence:

            if (bContinue) {
                for (SSomMfgWarehouseProduct product : productionEstimateDistributeWarehouses.getMfgWarehouseProductProduction()) {

                    bContinue = validateStockPhysicalVsSystem (product);
                    if (!bContinue) {
                        SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
                        break;
                    }
                }

                if (bContinue) {
                    for (SSomMfgWarehouseProduct product : productionEstimateDistributeWarehouses.getMfgWarehouseProductStorage()) {

                        bContinue = validateStockPhysicalVsSystem (product);
                        if (!bContinue) {
                            SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
                            break;
                        }
                    }
                }
            }

            if (bContinue) {
                moMfgEstimation.computeProductionEstimate(miClient.getSession());
                maProductionProducts = moMfgEstimation.getChildMfgWarehouseProducts();
                populatePaneGrid();
            }

                /*
                 * XXX 2014-11-12 navalos, not neccesary because production is distribute in the las process
                 *
                SGridRow gridRow = moGridProducts.getSelectedGridRow();
                SSomManufacturingWarehouseProduct productionProduct = (SSomManufacturingWarehouseProduct) gridRow;

                moFinishedGoodDelivery.setValue(SModConsts.S_MFG_EST, moMfgEstimation);
                moFinishedGoodDelivery.setValue(SGuiConsts.PARAM_DATE, moDateDate.getValue());
                moFinishedGoodDelivery.setValue(SModConsts.SX_STK_PROD_EST, productionProduct);
                moFinishedGoodDelivery.setValue(SModConsts.CU_DIV, moKeyDivision.getValue());
                moFinishedGoodDelivery.setVisible(true);

                if (moFinishedGoodDelivery.getFormResult() != SGuiConsts.FORM_RESULT_OK) {
                    try {
                        moMfgEstimation.computeProductionEstimate(miClient.getSession());
                        mvProductionProducts = moMfgEstimation.getManufacturingProducts();
                        populatePaneGrid();
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
                */
            //}
        }
        catch (Exception e) {
            SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
            SLibUtils.showException(this, e);
        }
    }

    private boolean createIogInProduction(final SSomMfgWarehouseProduct product) throws Exception {
        boolean result = true;
        SDbIog iog = null;

        if (product.getQuantity() > 0) {

            iog = new SDbIog();
            iog.computeIog(SLibTimeUtils.createDate(SLibTimeUtils.digestDate(moDateDate.getValue())[0], SLibTimeUtils.digestDate(moDateDate.getValue())[1],
                    SLibTimeUtils.digestDate(moDateDate.getValue())[2]-1), SLibUtils.round(product.getQuantity(), SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()),
                    true, SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD, SModSysConsts.SU_IOG_ADJ_TP_NA, product.getPkItemId(), product.getPkUnitId(),
                    new int[] { product.getPkCompanyId(), product.getPkBranchId(), product.getPkWarehouseId() }, moKeyDivision.getValue()[0],
                    moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion(), miClient.getSession().getUser().getPkUserId());
            iog.setSystem(true);
            if (iog.canSave(miClient.getSession())) {

                iog.save(miClient.getSession());
                if (iog.getQueryResultId() == SDbConsts.SAVE_ERROR) {

                    SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
                    miClient.showMsgBoxError(iog.getQueryResult());
                    result = false;
                }
            }
            else {

                SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
                miClient.showMsgBoxError(iog.getQueryResult());
                result = false;
            }
        }
        else if (product.getQuantity() < 0) {

            SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
            miClient.showMsgBoxError("Error al crear el movimiento de entrada de producción al almacén: '" + product.getWarehouseCode() + "'");
            result = false;
        }

        return result;
    }

    private boolean validateStockPhysicalVsSystem (final SSomMfgWarehouseProduct product) throws Exception {
        boolean result = false;

        double stockPhysical = SSomUtils.obtainStockDay(miClient.getSession(), SLibTimeUtils.digestYear(moDateDate.getValue())[0],
                product.getPkItemId(), product.getPkUnitId(), SModSysConsts.SS_ITEM_TP_FG,
                new int[] { product.getPkCompanyId(), product.getPkBranchId(), product.getPkWarehouseId() }, moDateDate.getValue());

        double stockSystem = ((SSomStock) SSomUtils.obtainStock(miClient.getSession(), SLibTimeUtils.digestYear(moDateDate.getValue())[0],
                product.getPkItemId(), product.getPkUnitId(), SModSysConsts.SS_ITEM_TP_FG,
                new int[] { product.getPkCompanyId(), product.getPkBranchId(), product.getPkWarehouseId() }, SLibConsts.UNDEFINED, null, moDateDate.getValue(), false, false)).getStock();

        result = true;
        if (SLibUtils.round(stockPhysical, SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits()) !=
                SLibUtils.round(stockSystem, SLibUtils.DecimalFormatValue4D.getMaximumFractionDigits())) {

            SSomUtils.productionEstimateDeleteIogs(miClient.getSession(), moMfgEstimation.getPkMfgEstimationId(), moMfgEstimation.getVersion());
            miClient.showMsgBoxError("Error, después de estimar y asignar la producción el almacén '" + product.getWarehouseCode() + "' tiene una existencia física de '" +
                  SLibUtils.DecimalFormatValue2D.format(stockPhysical) + "' vs. una existencia de sistema de '" +
                  SLibUtils.DecimalFormatValue2D.format(stockSystem) + "'.");
            result = false;
        }

        return result;
    }
    
    private void actionViewProductionDetail() {
        moDetailDialog.formReset();
        
        if (maProductionProducts.isEmpty()) {
            return;
        }
        
        Map<String, SRowProductionByLine> lines = new HashMap<>();
        
        for (SSomMfgWarehouseProduct maProductionProduct : maProductionProducts) {
            if (lines.containsKey(maProductionProduct.getProductionLine())) {
                SRowProductionByLine obj = lines.get(maProductionProduct.getProductionLine());
                obj.setProduction(obj.getProduction() + maProductionProduct.getQuantity());
            }
            else {
                SRowProductionByLine n = new SRowProductionByLine();
                
                n.setProductionLine(maProductionProduct.getProductionLine());
                n.setProduction(maProductionProduct.getQuantity());
                n.setUnit(maProductionProduct.getUnitCode());
                
                lines.put(maProductionProduct.getProductionLine(), n);
            }
        }
        
        moDetailDialog.setFormParams(lines);
        moDetailDialog.setVisible(true);
    }

    /*
    * Public methods
    */

    @Override
    public void addAllListeners() {
        jbProductDelivery.addActionListener(this);
        jbViewProductionDetail.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbProductDelivery.removeActionListener(this);
        jbViewProductionDetail.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        try {
            populatePaneGrid();
        }
        catch (SQLException e) {
            SLibUtils.showException(this, e);
        }

        miClient.getSession().populateCatalogue(moKeyUnit, SModConsts.SU_UNIT, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyDivision, SModConsts.CU_DIV, SLibConsts.UNDEFINED, null);

        if (moKeyDivision.getModel().getSize() == 2) {
            moKeyDivision.setSelectedIndex(1);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(int type, Object value) {
         switch (type) {
             case SModConsts.S_MFG_EST:
                 moMfgEstimation = (SDbMfgEstimation) value;
                 break;
            case SGuiConsts.PARAM_DATE:
                moDateDate.setValue(value);
                moDateDate.setEditable(false);
                break;
            case SModConsts.SU_UNIT:
                moKeyUnit.setValue(value);
                moKeyUnit.setEnabled(false);
                break;
            case SModConsts.CU_DIV:
                moKeyDivision.setValue(value);
                moKeyDivision.setEnabled(false);
                break;
            case SModConsts.SX_STK_PROD_FG:
                maProductionProducts = (ArrayList<SSomMfgWarehouseProduct>) value;

                try {
                    populatePaneGrid();
                }
                catch (SQLException e) {
                    SLibUtils.showException(this, e);
                }
                break;
            case SModConsts.SX_STK_PROD_EST:
                moDecQuantity.setValue(value);
            default:
        }
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            JButton button = (JButton) evt.getSource();

            if (button == jbProductDelivery) {
                try {
                    actionFinishedGoodDelivery();
                }
                catch (Exception ex) {
                    SLibUtils.printException(this, ex);
                }
            }
            else if (button == jbViewProductionDetail) {
                actionViewProductionDetail();
            }
        }
    }
}
