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
import som.mod.som.db.SDbByProduct;

/**
 *
 * @author Isabel Servín
 */
public class SFormByProduct extends sa.lib.gui.bean.SBeanForm {

    private SDbByProduct moRegistry;

    /**
     * Creates new form SFormInputClass
     * @param client
     * @param title
     */
    public SFormByProduct(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.SU_BY_PRODUCT, SLibConsts.UNDEFINED, title);
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
        jPanel5 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel6 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlCode);

        moTextCode.setText("sBeanFieldText1");
        jPanel5.add(moTextCode);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlName);

        moTextName.setText("sBeanFieldText1");
        moTextName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(moTextName);

        jPanel2.add(jPanel6);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlName;
    private sa.lib.gui.bean.SBeanFieldText moTextCode;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);

        moTextCode.setTextSettings(SGuiUtils.getLabelName(jlCode), 5);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName), 50);

        moFields.addField(moTextCode);
        moFields.addField(moTextName);

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

    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbByProduct) registry;

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
        
        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbByProduct registry = moRegistry.clone();

        if (registry.isRegistryNew()) { }

        registry.setCode(moTextCode.getValue());
        registry.setName(moTextName.getValue());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }
}
