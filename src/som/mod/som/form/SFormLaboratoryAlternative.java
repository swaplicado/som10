/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;
import som.gui.SGuiClientUtils;
import som.mod.SModConsts;
import som.mod.som.db.SDbLaboratoryAlternative;
import som.mod.som.db.SDbTicketAlternative;

/**
 *
 * @author Isabel Servín
 */
public class SFormLaboratoryAlternative extends SBeanForm {

    private SDbTicketAlternative moRegistry;
    private SDbLaboratoryAlternative moLabAlternative;
    
    private int mnOldItemId;
    private String msOldPlates;
    private String msOldPlatesCage;
    private int mnNewSeasonId;
    
    boolean isLabNew;
    
    /**
     * Creates new form SFormLaboratoryAlternative
     * @param client
     * @param title
     */
    public SFormLaboratoryAlternative(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.S_ALT_LAB, SLibConsts.UNDEFINED, title);
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
        jPanel7 = new javax.swing.JPanel();
        jlPlates = new javax.swing.JLabel();
        moTextPlates = new sa.lib.gui.bean.SBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlTicket = new javax.swing.JLabel();
        moTextTicket = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel14 = new javax.swing.JPanel();
        jlPlatesCage = new javax.swing.JLabel();
        moTextPlatesCage = new sa.lib.gui.bean.SBeanFieldText();
        jPanel10 = new javax.swing.JPanel();
        jlProducer = new javax.swing.JLabel();
        moTextProducer = new sa.lib.gui.bean.SBeanFieldText();
        jPanel3 = new javax.swing.JPanel();
        jlDriver = new javax.swing.JLabel();
        moTextDriver = new sa.lib.gui.bean.SBeanFieldText();
        jPanel12 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel9 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        moDateDate = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel8 = new javax.swing.JPanel();
        jlSeason = new javax.swing.JLabel();
        moTextSeason = new sa.lib.gui.bean.SBeanFieldText();
        jPanel11 = new javax.swing.JPanel();
        jlYieldPercentage = new javax.swing.JLabel();
        moDecYieldPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel15 = new javax.swing.JPanel();
        jlRegion = new javax.swing.JLabel();
        moTextRegion = new sa.lib.gui.bean.SBeanFieldText();
        jPanel16 = new javax.swing.JPanel();
        jlMoisiturePercentage = new javax.swing.JLabel();
        moDecMoisiturePercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel5 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jlAcidityPercentage = new javax.swing.JLabel();
        moDecAcidityPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del boleto y resultados:"));
        jPanel2.setLayout(new java.awt.GridLayout(8, 2, 0, 5));

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlScale.setText("Báscula:");
        jlScale.setPreferredSize(new java.awt.Dimension(75, 23));
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

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPlates.setText("Placas:");
        jlPlates.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlPlates);

        moTextPlates.setEditable(false);
        moTextPlates.setText("sBeanFieldText2");
        jPanel7.add(moTextPlates);

        jPanel2.add(jPanel7);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTicket.setText("Boleto:");
        jlTicket.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jlTicket);

        moTextTicket.setEditable(false);
        jPanel4.add(moTextTicket);

        jPanel2.add(jPanel4);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPlatesCage.setText("Placas caja:");
        jlPlatesCage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlPlatesCage);

        moTextPlatesCage.setEditable(false);
        moTextPlatesCage.setText("sBeanFieldText2");
        jPanel14.add(moTextPlatesCage);

        jPanel2.add(jPanel14);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProducer.setText("Proveedor:");
        jlProducer.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jlProducer);

        moTextProducer.setEditable(false);
        moTextProducer.setText("sBeanFieldText2");
        moTextProducer.setPreferredSize(new java.awt.Dimension(325, 23));
        jPanel10.add(moTextProducer);

        jPanel2.add(jPanel10);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDriver.setText("Chofer:");
        jlDriver.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlDriver);

        moTextDriver.setEditable(false);
        moTextDriver.setText("sBeanFieldText2");
        moTextDriver.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel3.add(moTextDriver);

        jPanel2.add(jPanel3);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(325, 23));
        jPanel12.add(moKeyItem);

        jPanel2.add(jPanel12);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha:*");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlDate);
        jPanel9.add(moDateDate);

        jPanel2.add(jPanel9);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSeason.setText("Temporada:");
        jlSeason.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel8.add(jlSeason);

        moTextSeason.setEditable(false);
        moTextSeason.setText("sBeanFieldText2");
        moTextSeason.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel8.add(moTextSeason);

        jPanel2.add(jPanel8);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYieldPercentage.setText("Rendimiento (%):");
        jlYieldPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlYieldPercentage);
        jPanel11.add(moDecYieldPercentage);

        jPanel2.add(jPanel11);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRegion.setText("Región:");
        jlRegion.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel15.add(jlRegion);

        moTextRegion.setEditable(false);
        moTextRegion.setText("sBeanFieldText2");
        moTextRegion.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel15.add(moTextRegion);

        jPanel2.add(jPanel15);

        jPanel16.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMoisiturePercentage.setText("Humedad (%):");
        jlMoisiturePercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jlMoisiturePercentage);
        jPanel16.add(moDecMoisiturePercentage);

        jPanel2.add(jPanel16);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel2.add(jPanel5);

        jPanel17.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAcidityPercentage.setText("Acidez (%):");
        jlAcidityPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel17.add(jlAcidityPercentage);
        jPanel17.add(moDecAcidityPercentage);

        jPanel2.add(jPanel17);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlAcidityPercentage;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDriver;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlMoisiturePercentage;
    private javax.swing.JLabel jlPlates;
    private javax.swing.JLabel jlPlatesCage;
    private javax.swing.JLabel jlProducer;
    private javax.swing.JLabel jlRegion;
    private javax.swing.JLabel jlScale;
    private javax.swing.JLabel jlSeason;
    private javax.swing.JLabel jlTicket;
    private javax.swing.JLabel jlYieldPercentage;
    private sa.lib.gui.bean.SBeanFieldDate moDateDate;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecAcidityPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecMoisiturePercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecYieldPercentage;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    private sa.lib.gui.bean.SBeanFieldText moTextDriver;
    private sa.lib.gui.bean.SBeanFieldText moTextPlates;
    private sa.lib.gui.bean.SBeanFieldText moTextPlatesCage;
    private sa.lib.gui.bean.SBeanFieldText moTextProducer;
    private sa.lib.gui.bean.SBeanFieldText moTextRegion;
    private sa.lib.gui.bean.SBeanFieldText moTextScaleCode;
    private sa.lib.gui.bean.SBeanFieldText moTextScaleName;
    private sa.lib.gui.bean.SBeanFieldText moTextSeason;
    private sa.lib.gui.bean.SBeanFieldInteger moTextTicket;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 880, 550);

        
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
        moDateDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlDate.getText()), true);
        moDecYieldPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlYieldPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, true);
        moDecMoisiturePercentage.setDecimalSettings(SGuiUtils.getLabelName(jlMoisiturePercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, true);
        moDecAcidityPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlAcidityPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, true);
        
        moFields.addField(moTextScaleName);
        moFields.addField(moTextScaleCode);
        moFields.addField(moTextTicket);
        moFields.addField(moTextProducer);
        moFields.addField(moKeyItem);
        moFields.addField(moTextSeason);
        moFields.addField(moTextRegion);
        moFields.addField(moTextPlates);
        moFields.addField(moTextPlatesCage);
        moFields.addField(moTextDriver);
        moFields.addField(moDateDate);
        moFields.addField(moDecYieldPercentage);
        moFields.addField(moDecMoisiturePercentage);
        moFields.addField(moDecAcidityPercentage);

        moFields.setFormButton(jbSave);
        
        isLabNew = false;
    }

    @Override
    public void addAllListeners() {
        
    }

    @Override
    public void removeAllListeners() {
        
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbTicketAlternative) registry;
        moLabAlternative = moRegistry.getDbmsLaboratoryAlt();
        
        if (moLabAlternative == null || moLabAlternative.getPkAlternativeLaboratoryId() == 0) {
            isLabNew = true;
            moDateDate.setValue(miClient.getSession().getSystemDate());
            moDecYieldPercentage.setValue(0.0);
            moDecMoisiturePercentage.setValue(0.0);
            moDecAcidityPercentage.setValue(0.0);
        }
        else {
            moDateDate.setValue(moLabAlternative.getDate());
            moDecYieldPercentage.setValue(moLabAlternative.getYieldPercentage());
            moDecMoisiturePercentage.setValue(moLabAlternative.getMoisturePercentage());
            moDecAcidityPercentage.setValue(moLabAlternative.getAcidityPercentage());
        }

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
        msOldPlates = moRegistry.getPlate();
        moTextPlates.setValue(msOldPlates);
        msOldPlatesCage = moRegistry.getPlateCage();
        moTextPlatesCage.setValue(msOldPlatesCage);
        moTextDriver.setValue(moRegistry.getDriver());
        moTextProducer.setValue(moRegistry.getXtaProducer());
        moTextSeason.setValue(moRegistry.getXtaSeason());
        moTextRegion.setValue(moRegistry.getXtaRegion());
        mnOldItemId = moRegistry.getFkItemId();
        moKeyItem.setValue(new int[] { mnOldItemId });

        setFormEditable(true);
        
        if (moRegistry.isRegistryNew()) {
            
        }

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
        
        moDecYieldPercentage.setEnabled(moRegistry.getDbmsItem().isAlternativeYieldPercentage());
        moDecMoisiturePercentage.setEnabled(moRegistry.getDbmsItem().isAlternativeMoisiturePercentage());
        moDecAcidityPercentage.setEnabled(moRegistry.getDbmsItem().isAlternativeAcidityPercentage());
        
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbTicketAlternative registry = moRegistry.clone();
        SDbLaboratoryAlternative registryLab;
        
        if (isLabNew) {
            registryLab = new SDbLaboratoryAlternative();
        }
        else {
            registryLab = moLabAlternative.clone();
            registryLab.setRegistryEdited(true);
        }

        if (registry.isRegistryNew()) { }

        registryLab.setDate(moDateDate.getValue());
        registryLab.setYieldPercentage(moDecYieldPercentage.getValue());
        registryLab.setMoisturePercentage(moDecMoisiturePercentage.getValue());
        registryLab.setAcidityPercentage(moDecAcidityPercentage.getValue());
        
        registry.setDbmsLaboratoryAlt(registryLab);

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid() && !SGuiClientUtils.isPeriodOpened(miClient.getSession(), moDateDate.getValue())) {
            validation.setMessage("El período está cerrado.");
            validation.setComponent(moDateDate.getComponent());
        }

        return validation;
    }
}
