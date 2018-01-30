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
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import som.mod.SModConsts;
import som.mod.som.db.SDbInputType;

/**
 *
 * @author Sergio Flores
 */
public class SFormInputType extends sa.lib.gui.bean.SBeanForm {

    private SDbInputType moRegistry;
    private SGuiFieldKeyGroup moKeyGroupInput;

    /**
     * Creates new form SFormInputType
     */
    public SFormInputType(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.SU_INP_TP, SLibConsts.UNDEFINED, title);
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
        jPanel7 = new javax.swing.JPanel();
        jlInputCategory = new javax.swing.JLabel();
        moKeyInputCategory = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlInputClass = new javax.swing.JLabel();
        moKeyInputClass = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel4 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel5 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputCategory.setForeground(java.awt.Color.blue);
        jlInputCategory.setText("Categoría insumo:*");
        jlInputCategory.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlInputCategory);

        moKeyInputCategory.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel7.add(moKeyInputCategory);

        jPanel2.add(jPanel7);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputClass.setForeground(java.awt.Color.blue);
        jlInputClass.setText("Clase insumo:*");
        jlInputClass.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlInputClass);

        moKeyInputClass.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(moKeyInputClass);

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
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlInputCategory;
    private javax.swing.JLabel jlInputClass;
    private javax.swing.JLabel jlName;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputCategory;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputClass;
    private sa.lib.gui.bean.SBeanFieldText moTextCode;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);

        moKeyInputCategory.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputCategory), true);
        moKeyInputClass.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputClass), true);
        moTextCode.setTextSettings(SGuiUtils.getLabelName(jlCode.getText()), 5);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName.getText()), 50);

        moFields.addField(moKeyInputCategory);
        moFields.addField(moKeyInputClass);
        moFields.addField(moTextCode);
        moFields.addField(moTextName);

        moFields.setFormButton(jbSave);
        
        moKeyGroupInput = new SGuiFieldKeyGroup(miClient);
    }

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {
        moKeyGroupInput.initGroup();
        moKeyGroupInput.addFieldKey(moKeyInputCategory, SModConsts.SU_INP_CT, SLibConsts.UNDEFINED, null);
        moKeyGroupInput.addFieldKey(moKeyInputClass, SModConsts.SU_INP_CL, SLibConsts.UNDEFINED, null);
        moKeyGroupInput.populateCatalogues();
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        int idCategory = SLibConsts.UNDEFINED;
        int idClass = SLibConsts.UNDEFINED;

        moRegistry = (SDbInputType) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        idCategory = moRegistry.getPkInputCategoryId();
        idClass = moRegistry.getPkInputClassId();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moKeyInputCategory.setValue(new int[] { idCategory });
        moKeyInputClass.setValue(new int[] { idCategory, idClass });
        moTextName.setValue(moRegistry.getName());
        moTextCode.setValue(moRegistry.getCode());

        setFormEditable(true);

        if (moRegistry.isRegistryNew()) {
            if (idCategory == SLibConsts.UNDEFINED) {
                moKeyGroupInput.resetGroup();
            }
        }
        else {
            moKeyInputCategory.setEnabled(false);
            moKeyInputClass.setEnabled(false);
        }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbInputType registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.setPkInputCategoryId(moKeyInputClass.getValue()[0]);
            registry.setPkInputClassId(moKeyInputClass.getValue()[1]);
        }

        registry.setName(moTextName.getValue());
        registry.setCode(moTextCode.getValue());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }
}
