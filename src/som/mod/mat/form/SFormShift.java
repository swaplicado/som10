/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.form;

import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;
import som.mod.SModConsts;
import som.mod.mat.db.SDbShift;

/**
 *
 * @author Isabel Servín
 */
public class SFormShift extends SBeanForm {

    private SDbShift moRegistry;

    /**
     * Creates new form SFormRegion
     * @param client
     * @param title
     */
    public SFormShift(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.MU_SHIFT, SLibConsts.UNDEFINED, title);
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
        jPanel6 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel5 = new javax.swing.JPanel();
        jlSortingPos = new javax.swing.JLabel();
        moIntSortingPos = new sa.lib.gui.bean.SBeanFieldInteger();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlCode);

        moTextCode.setText("sBeanFieldText2");
        jPanel6.add(moTextCode);

        jPanel2.add(jPanel6);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlName);

        moTextName.setText("sBeanFieldText1");
        moTextName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel4.add(moTextName);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSortingPos.setText("Orden:*");
        jlSortingPos.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlSortingPos);
        jPanel5.add(moIntSortingPos);

        jPanel2.add(jPanel5);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlSortingPos;
    private sa.lib.gui.bean.SBeanFieldInteger moIntSortingPos;
    private sa.lib.gui.bean.SBeanFieldText moTextCode;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 400, 250);

        moTextCode.setTextSettings(SGuiUtils.getLabelName(jlCode.getText()), 5);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName.getText()), 50);
        moIntSortingPos.setIntegerSettings(SGuiUtils.getLabelName(jlSortingPos.getText()), SGuiConsts.GUI_TYPE_INT, true);
        moIntSortingPos.setMinInteger(0);

        moFields.addField(moTextCode);
        moFields.addField(moTextName);
        moFields.addField(moIntSortingPos);

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
        moRegistry = (SDbShift) registry;

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
        moIntSortingPos.setValue(moRegistry.getSortingPos());

        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbShift registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}

        registry.setCode(moTextCode.getValue());
        registry.setName(moTextName.getValue());
        registry.setSortingPos(moIntSortingPos.getValue());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }
}