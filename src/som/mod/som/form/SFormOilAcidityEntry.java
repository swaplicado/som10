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
import som.mod.SModConsts;
import som.mod.som.db.SDbOilAcidityEntry;

/**
 *
 * @author Isabel Servín
 */
public class SFormOilAcidityEntry extends sa.lib.gui.bean.SBeanForm {

    private SDbOilAcidityEntry moRegistry;

    /**
     * Creates new form SFormInputClass
     * @param client
     * @param title
     */
    public SFormOilAcidityEntry(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.SU_OIL_ACI_ETY, SLibConsts.UNDEFINED, title);
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

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlOilAcidity = new javax.swing.JLabel();
        moKeyOilAcidity = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel4 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel5 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel7 = new javax.swing.JPanel();
        jlValMin = new javax.swing.JLabel();
        moDecimalValMin = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel8 = new javax.swing.JPanel();
        jlValMax = new javax.swing.JLabel();
        moDecimalValMax = new sa.lib.gui.bean.SBeanFieldDecimal();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOilAcidity.setForeground(java.awt.Color.blue);
        jlOilAcidity.setText("Vigencia acidez aceite:*");
        jlOilAcidity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlOilAcidity);

        moKeyOilAcidity.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(moKeyOilAcidity);

        jPanel2.add(jPanel6);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlCode);

        moTextCode.setText("sBeanFieldText1");
        jPanel4.add(moTextCode);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlName);

        moTextName.setText("sBeanFieldText1");
        moTextName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel5.add(moTextName);

        jPanel2.add(jPanel5);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlValMin.setText("Valor minimo:*");
        jlValMin.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlValMin);
        jPanel7.add(moDecimalValMin);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlValMax.setText("Valor maximo:*");
        jlValMax.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlValMax);
        jPanel8.add(moDecimalValMax);

        jPanel2.add(jPanel8);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlOilAcidity;
    private javax.swing.JLabel jlValMax;
    private javax.swing.JLabel jlValMin;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecimalValMax;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecimalValMin;
    private sa.lib.gui.bean.SBeanFieldKey moKeyOilAcidity;
    private sa.lib.gui.bean.SBeanFieldText moTextCode;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);

        moKeyOilAcidity.setKeySettings(miClient, SGuiUtils.getLabelName(jlOilAcidity), true);
        moTextCode.setTextSettings(SGuiUtils.getLabelName(jlCode.getText()), 5);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName.getText()), 50);
        moDecimalValMin.setDecimalSettings(SGuiUtils.getLabelName(jlValMin.getText()), SGuiConsts.GUI_TYPE_DEC_QTY, true);
        moDecimalValMin.setMinDouble(0.0);
        moDecimalValMax.setDecimalSettings(SGuiUtils.getLabelName(jlValMax.getText()), SGuiConsts.GUI_TYPE_DEC_QTY, true);

        moFields.addField(moKeyOilAcidity);
        moFields.addField(moTextCode);
        moFields.addField(moTextName);
        moFields.addField(moDecimalValMin);
        moFields.addField(moDecimalValMax);
        
        moFields.setFormButton(jbSave);
    }

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyOilAcidity, SModConsts.SU_OIL_ACI, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbOilAcidityEntry) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        int idClass = moRegistry.getPkOilAcidityId();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moKeyOilAcidity.setValue(new int[] { idClass });
        moTextName.setValue(moRegistry.getName());
        moTextCode.setValue(moRegistry.getCode());
        moDecimalValMin.setValue(moRegistry.getValueMin());
        moDecimalValMax.setValue(moRegistry.getValueMax());

        setFormEditable(true);

        if (moRegistry.isRegistryNew()) {

        }
        else {
            moKeyOilAcidity.setEnabled(false);
        }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbOilAcidityEntry registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.setPkOilAcidityId(moKeyOilAcidity.getValue()[0]);
        }

        registry.setName(moTextName.getValue());
        registry.setCode(moTextCode.getValue());
        registry.setValueMin(moDecimalValMin.getValue());
        registry.setValueMax(moDecimalValMax.getValue());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }
}