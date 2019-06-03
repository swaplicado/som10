/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;
import som.mod.SModConsts;
import som.mod.ext.db.SExtUtils;
import som.mod.som.db.SDbProducer;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SFormProducer extends SBeanForm implements ItemListener {

    private SDbProducer moRegistry;

    /**
     * Creates new form SFormProducer
     */
    public SFormProducer(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.SU_PROD, SLibConsts.UNDEFINED, title);
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
        jPanel10 = new javax.swing.JPanel();
        jlExternalProducer = new javax.swing.JLabel();
        moKeyExternalProducer = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel8 = new javax.swing.JPanel();
        jlNameTrade = new javax.swing.JLabel();
        moTextNameTrade = new sa.lib.gui.bean.SBeanFieldText();
        jPanel5 = new javax.swing.JPanel();
        jlFiscalId = new javax.swing.JLabel();
        moTextFiscalId = new sa.lib.gui.bean.SBeanFieldText();
        jPanel7 = new javax.swing.JPanel();
        jlRevueltaProducerId = new javax.swing.JLabel();
        moTextRevueltaProducerId = new sa.lib.gui.bean.SBeanFieldText();
        jPanel9 = new javax.swing.JPanel();
        jlReportingGroup = new javax.swing.JLabel();
        moKeyReportingGroup = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel3 = new javax.swing.JPanel();
        moBoolFreight = new sa.lib.gui.bean.SBeanFieldBoolean();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExternalProducer.setForeground(new java.awt.Color(0, 0, 255));
        jlExternalProducer.setText("Proveedor sist. externo:*");
        jlExternalProducer.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel10.add(jlExternalProducer);

        moKeyExternalProducer.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel10.add(moKeyExternalProducer);

        jPanel2.add(jPanel10);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jlCode);

        moTextCode.setEditable(false);
        moTextCode.setText("sBeanFieldText2");
        jPanel6.add(moTextCode);

        jPanel2.add(jPanel6);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jlName);

        moTextName.setEditable(false);
        moTextName.setText("sBeanFieldText1");
        moTextName.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel4.add(moTextName);

        jPanel2.add(jPanel4);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNameTrade.setText("Nombre comercial:*");
        jlNameTrade.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlNameTrade);

        moTextNameTrade.setText("sBeanFieldText1");
        moTextNameTrade.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel8.add(moTextNameTrade);

        jPanel2.add(jPanel8);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalId.setText("RFC:*");
        jlFiscalId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel5.add(jlFiscalId);

        moTextFiscalId.setEditable(false);
        moTextFiscalId.setText("sBeanFieldText1");
        jPanel5.add(moTextFiscalId);

        jPanel2.add(jPanel5);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRevueltaProducerId.setText("Clave Revuelta:");
        jlRevueltaProducerId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jlRevueltaProducerId);

        moTextRevueltaProducerId.setText("sBeanFieldText1");
        jPanel7.add(moTextRevueltaProducerId);

        jPanel2.add(jPanel7);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReportingGroup.setText("Agrupador de reporte: *");
        jlReportingGroup.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlReportingGroup);

        moKeyReportingGroup.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel9.add(moKeyReportingGroup);

        jPanel2.add(jPanel9);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolFreight.setText("Se paga flete al proveedor");
        moBoolFreight.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(moBoolFreight);

        jPanel2.add(jPanel3);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlExternalProducer;
    private javax.swing.JLabel jlFiscalId;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlNameTrade;
    private javax.swing.JLabel jlReportingGroup;
    private javax.swing.JLabel jlRevueltaProducerId;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolFreight;
    private sa.lib.gui.bean.SBeanFieldKey moKeyExternalProducer;
    private sa.lib.gui.bean.SBeanFieldKey moKeyReportingGroup;
    private sa.lib.gui.bean.SBeanFieldText moTextCode;
    private sa.lib.gui.bean.SBeanFieldText moTextFiscalId;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    private sa.lib.gui.bean.SBeanFieldText moTextNameTrade;
    private sa.lib.gui.bean.SBeanFieldText moTextRevueltaProducerId;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);

        moKeyExternalProducer.setKeySettings(miClient, SGuiUtils.getLabelName(jlExternalProducer.getText()), true);
        moTextCode.setTextSettings(SGuiUtils.getLabelName(jlCode.getText()), 25);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName.getText()), 150);
        moTextNameTrade.setTextSettings(SGuiUtils.getLabelName(jlNameTrade.getText()), 15);
        moTextFiscalId.setTextSettings(SGuiUtils.getLabelName(jlFiscalId.getText()), 20);
        moTextRevueltaProducerId.setTextSettings(SGuiUtils.getLabelName(jlRevueltaProducerId.getText()), 10, 0);
        moKeyReportingGroup.setKeySettings(miClient, SGuiUtils.getLabelName(jlReportingGroup.getText()), true);
        moBoolFreight.setBooleanSettings(SGuiUtils.getLabelName(moBoolFreight.getText()), false);

        moFields.addField(moKeyExternalProducer);
        moFields.addField(moTextCode);
        moFields.addField(moTextName);
        moFields.addField(moTextNameTrade);
        moFields.addField(moTextFiscalId);
        moFields.addField(moTextRevueltaProducerId);
        moFields.addField(moKeyReportingGroup);
        moFields.addField(moBoolFreight);

        moFields.setFormButton(jbSave);
    }

    private void itemStateKeyExternalProducer() {
        int lenMax = 0;
        
        if (moKeyExternalProducer.getSelectedIndex() <= 0) {
            moTextCode.setValue("");
            moTextName.setValue("");
            moTextNameTrade.setValue("");
            moTextFiscalId.setValue("");
        }
        else {
            lenMax = moKeyExternalProducer.getSelectedItem().getItem().length() > 49 ? 49 : moKeyExternalProducer.getSelectedItem().getItem().length();
            
            moTextCode.setValue(moKeyExternalProducer.getSelectedItem().getCode());
            moTextName.setValue(moKeyExternalProducer.getSelectedItem().getItem());
            moTextNameTrade.setValue(moKeyExternalProducer.getSelectedItem().getItem().substring(0, lenMax));
            moTextFiscalId.setValue(moKeyExternalProducer.getSelectedItem().getComplement());
        }
    }

    @Override
    public void addAllListeners() {
        moKeyExternalProducer.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyExternalProducer.removeItemListener(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reloadCatalogues() {
        SExtUtils.populateCataloguesProducers(miClient, moKeyExternalProducer);
        miClient.getSession().populateCatalogue(moKeyReportingGroup, SModConsts.CU_REP_GRP, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbProducer) registry;

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

        moTextCode.setValue(moRegistry.getCode());
        moTextName.setValue(moRegistry.getName());
        moTextNameTrade.setValue(moRegistry.getNameTrade());
        moTextFiscalId.setValue(moRegistry.getFiscalId());
        moTextRevueltaProducerId.setValue(moRegistry.getRevueltaProducerId());
        moBoolFreight.setValue(moRegistry.isFreightPayment());
        moKeyReportingGroup.setValue(new int[] { moRegistry.getFkReportingGroupId() });
        moKeyExternalProducer.setValue(new int[] { moRegistry.getFkExternalProducerId_n() });

        moTextCode.setEnabled(false);
        moTextName.setEnabled(false);
        moTextFiscalId.setEnabled(false);

        setFormEditable(true);

        if (moRegistry.isRegistryNew()) {

        }
        else {
            moKeyExternalProducer.setEnabled(false);
        }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbProducer registry =  moRegistry.clone();

        if (registry.isRegistryNew()) {}

        registry.setCode(moTextCode.getValue());
        registry.setName(moTextName.getValue());
        registry.setNameTrade(moTextNameTrade.getValue());
        registry.setFiscalId(moTextFiscalId.getValue());
        registry.setRevueltaProducerId(moTextRevueltaProducerId.getValue());
        registry.setFreightPayment(moBoolFreight.getValue());
        registry.setFkReportingGroupId(moKeyReportingGroup.getValue()[0]);
        registry.setFkExternalProducerId_n(moKeyExternalProducer.getValue()[0]);

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();

            if (comboBox == moKeyExternalProducer) {
                itemStateKeyExternalProducer();
            }
        }
    }
}
