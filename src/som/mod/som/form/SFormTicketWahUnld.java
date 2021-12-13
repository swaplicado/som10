/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;
import som.mod.SModConsts;
import som.mod.som.db.SDbTicket;
import som.mod.som.db.SSomConsts;

/**
 * Forma que ayuda a asignar el almacén de descarga.
 * @author Isabel Servín
 */
public class SFormTicketWahUnld extends SBeanForm implements ActionListener {

    private SDbTicket moRegistry;
    
    /**
     * Creates new form SFormTicketWahUnld
     * @param client
     * @param title
     */
    public SFormTicketWahUnld(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.SX_TIC_WAH_UNLD, SLibConsts.UNDEFINED, title);
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
        jPanel13 = new javax.swing.JPanel();
        jlScale = new javax.swing.JLabel();
        moTextScaleName = new sa.lib.gui.bean.SBeanFieldText();
        moTextScaleCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlTicket = new javax.swing.JLabel();
        moTextTicket = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel10 = new javax.swing.JPanel();
        jlPlates = new javax.swing.JLabel();
        moTextPlates = new sa.lib.gui.bean.SBeanFieldText();
        jPanel12 = new javax.swing.JPanel();
        jlProducer = new javax.swing.JLabel();
        moTextProducer = new sa.lib.gui.bean.SBeanFieldText();
        jPanel8 = new javax.swing.JPanel();
        jlPlatesCage = new javax.swing.JLabel();
        moTextPlatesCage = new sa.lib.gui.bean.SBeanFieldText();
        jPanel15 = new javax.swing.JPanel();
        jlDriver = new javax.swing.JLabel();
        moTextDriver = new sa.lib.gui.bean.SBeanFieldText();
        jPanel17 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel19 = new javax.swing.JPanel();
        jlSeason = new javax.swing.JLabel();
        moTextSeason = new sa.lib.gui.bean.SBeanFieldText();
        jPanel21 = new javax.swing.JPanel();
        jlRegion = new javax.swing.JLabel();
        moTextRegion = new sa.lib.gui.bean.SBeanFieldText();
        jPanel23 = new javax.swing.JPanel();
        jlWeight = new javax.swing.JLabel();
        moTextWeight = new sa.lib.gui.bean.SBeanFieldText();
        jlWeightDestinyDepartureUnit = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jlWarehouse = new javax.swing.JLabel();
        moKeyWarehouse = new sa.lib.gui.bean.SBeanFieldKey();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del boleto:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(11, 1, 0, 5));

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlScale.setText("Báscula:");
        jlScale.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel13.add(jlScale);

        moTextScaleName.setEditable(false);
        moTextScaleName.setText("sBeanFieldText2");
        moTextScaleName.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel13.add(moTextScaleName);

        moTextScaleCode.setEditable(false);
        moTextScaleCode.setText("sBeanFieldText1");
        moTextScaleCode.setPreferredSize(new java.awt.Dimension(45, 23));
        jPanel13.add(moTextScaleCode);

        jPanel2.add(jPanel13);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTicket.setText("Boleto:");
        jlTicket.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel4.add(jlTicket);

        moTextTicket.setEditable(false);
        jPanel4.add(moTextTicket);

        jPanel2.add(jPanel4);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPlates.setText("Placas:");
        jlPlates.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel10.add(jlPlates);

        moTextPlates.setEditable(false);
        moTextPlates.setText("sBeanFieldText2");
        jPanel10.add(moTextPlates);

        jPanel2.add(jPanel10);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProducer.setText("Proveedor:");
        jlProducer.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel12.add(jlProducer);

        moTextProducer.setEditable(false);
        moTextProducer.setText("sBeanFieldText2");
        moTextProducer.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel12.add(moTextProducer);

        jPanel2.add(jPanel12);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPlatesCage.setText("Placas caja:");
        jlPlatesCage.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel8.add(jlPlatesCage);

        moTextPlatesCage.setEditable(false);
        moTextPlatesCage.setText("sBeanFieldText2");
        jPanel8.add(moTextPlatesCage);

        jPanel2.add(jPanel8);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDriver.setText("Chofer:");
        jlDriver.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel15.add(jlDriver);

        moTextDriver.setEditable(false);
        moTextDriver.setText("sBeanFieldText2");
        moTextDriver.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel15.add(moTextDriver);

        jPanel2.add(jPanel15);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel17.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel17.add(moKeyItem);

        jPanel2.add(jPanel17);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSeason.setText("Temporada:");
        jlSeason.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel19.add(jlSeason);

        moTextSeason.setEditable(false);
        moTextSeason.setText("sBeanFieldText2");
        moTextSeason.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel19.add(moTextSeason);

        jPanel2.add(jPanel19);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRegion.setText("Región:");
        jlRegion.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel21.add(jlRegion);

        moTextRegion.setEditable(false);
        moTextRegion.setText("sBeanFieldText2");
        moTextRegion.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel21.add(moTextRegion);

        jPanel2.add(jPanel21);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWeight.setText("Segunda pesada:");
        jlWeight.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel23.add(jlWeight);

        moTextWeight.setEditable(false);
        moTextWeight.setText("sBeanFieldText2");
        jPanel23.add(moTextWeight);

        jlWeightDestinyDepartureUnit.setText("UNIT");
        jlWeightDestinyDepartureUnit.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel23.add(jlWeightDestinyDepartureUnit);

        jPanel2.add(jPanel23);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWarehouse.setText("Almacén descarga:*");
        jlWarehouse.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel25.add(jlWarehouse);

        moKeyWarehouse.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel25.add(moKeyWarehouse);

        jPanel2.add(jPanel25);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlDriver;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlPlates;
    private javax.swing.JLabel jlPlatesCage;
    private javax.swing.JLabel jlProducer;
    private javax.swing.JLabel jlRegion;
    private javax.swing.JLabel jlScale;
    private javax.swing.JLabel jlSeason;
    private javax.swing.JLabel jlTicket;
    private javax.swing.JLabel jlWarehouse;
    private javax.swing.JLabel jlWeight;
    private javax.swing.JLabel jlWeightDestinyDepartureUnit;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    private sa.lib.gui.bean.SBeanFieldKey moKeyWarehouse;
    private sa.lib.gui.bean.SBeanFieldText moTextDriver;
    private sa.lib.gui.bean.SBeanFieldText moTextPlates;
    private sa.lib.gui.bean.SBeanFieldText moTextPlatesCage;
    private sa.lib.gui.bean.SBeanFieldText moTextProducer;
    private sa.lib.gui.bean.SBeanFieldText moTextRegion;
    private sa.lib.gui.bean.SBeanFieldText moTextScaleCode;
    private sa.lib.gui.bean.SBeanFieldText moTextScaleName;
    private sa.lib.gui.bean.SBeanFieldText moTextSeason;
    private sa.lib.gui.bean.SBeanFieldInteger moTextTicket;
    private sa.lib.gui.bean.SBeanFieldText moTextWeight;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 720, 450);

        moTextScaleName.setTextSettings(SGuiUtils.getLabelName(jlScale.getText()), 25);
        moTextScaleCode.setTextSettings(SGuiUtils.getLabelName(jlScale.getText()), 25);
        moTextTicket.setIntegerSettings(SGuiUtils.getLabelName(jlTicket.getText()), SGuiConsts.GUI_TYPE_INT_RAW, true);
        moTextProducer.setTextSettings(SGuiUtils.getLabelName(jlProducer.getText()), 25);
        moKeyItem.setKeySettings(miClient, SGuiUtils.getLabelName(jlItem.getText()), true);
        moTextSeason.setTextSettings(SGuiUtils.getLabelName(jlSeason.getText()), 25);
        moTextRegion.setTextSettings(SGuiUtils.getLabelName(jlRegion.getText()), 25);
        moTextPlates.setTextSettings(SGuiUtils.getLabelName(jlPlates.getText()), 25);
        moTextPlatesCage.setTextSettings(SGuiUtils.getLabelName(jlPlatesCage.getText()), 25);
        moTextDriver.setTextSettings(SGuiUtils.getLabelName(jlDriver.getText()), 150);
        moTextWeight.setTextSettings(SGuiUtils.getLabelName(jlWeight.getText()), 25);
        moKeyWarehouse.setKeySettings(miClient, SGuiUtils.getLabelName(jlWarehouse.getText()), true);
        
        moFields.addField(moKeyWarehouse);
        
        moFields.setFormButton(jbSave);
        
        jlWeightDestinyDepartureUnit.setText(SSomConsts.KG);

    }

    @Override
    public void addAllListeners() {
        moKeyWarehouse.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyWarehouse.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        try {
            miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, null);
            miClient.getSession().populateCatalogue(moKeyWarehouse, SModConsts.CU_WAH, SModConsts.SX_TIC_WAH_UNLD, null);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbTicket) registry;
        
        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        
        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moTextScaleName.setValue(moRegistry.getXtaScaleName());
        moTextScaleCode.setValue(moRegistry.getXtaScaleCode());
        moTextTicket.setValue(moRegistry.getNumber());
        moTextPlates.setValue(moRegistry.getPlate());
        moTextPlatesCage.setValue(moRegistry.getPlateCage());
        moTextDriver.setValue(moRegistry.getDriver());
        moTextProducer.setValue(moRegistry.getXtaProducer());
        moTextSeason.setValue(moRegistry.getXtaSeason());
        moTextRegion.setValue(moRegistry.getXtaRegion());
        moKeyItem.setValue(new int[] {moRegistry.getFkItemId() });
        moTextWeight.setValue(moRegistry.getWeightDestinyNet_r() + "");
        moKeyWarehouse.setValue((new int [] {moRegistry.getFkWarehouseUnloadCompanyId_n(), moRegistry.getFkWarehouseUnloadBranchId_n(), moRegistry.getFkWarehouseUnloadWarehouseId_n()}));
        
        setFormEditable(true);
        
        moTextWeight.setEditable(false);
        moKeyItem.setEnabled(false);
        moTextScaleName.setEditable(false);
        moTextScaleCode.setEditable(false);
        moTextTicket.setEditable(false);
        moTextProducer.setEditable(false);
        moTextSeason.setEditable(false);
        moTextRegion.setEditable(false);
        moTextPlates.setEditable(false);
        moTextPlatesCage.setEditable(false);
        moTextDriver.setEditable(false);
        moTextWeight.setEditable(false);

        moFields.getFields();
        
        if (moRegistry.getFkWarehouseUnloadCompanyId_n() != SLibConsts.UNDEFINED && 
                moRegistry.getFkWarehouseUnloadBranchId_n() != SLibConsts.UNDEFINED && 
                moRegistry.getFkWarehouseUnloadWarehouseId_n() != SLibConsts.UNDEFINED) {
            moKeyWarehouse.setMandatory(false);
            jlWarehouse.setText("Almacén descarga:");
        }
        
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbTicket registry = moRegistry.clone();

        if (registry.isRegistryNew()) { }

        if (moKeyWarehouse.getComponent().getSelectedIndex() != 0) {
            registry.setFkWarehouseUnloadCompanyId_n(moKeyWarehouse.getValue()[0]);
            registry.setFkWarehouseUnloadBranchId_n(moKeyWarehouse.getValue()[1]);
            registry.setFkWarehouseUnloadWarehouseId_n(moKeyWarehouse.getValue()[2]); 
        }
        else {
            registry.setFkWarehouseUnloadCompanyId_n(SLibConsts.UNDEFINED);
            registry.setFkWarehouseUnloadBranchId_n(SLibConsts.UNDEFINED);
            registry.setFkWarehouseUnloadWarehouseId_n(SLibConsts.UNDEFINED); 
        }
        
        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}