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
import som.mod.som.db.SDbWarehouseFillLevel;

/**
 *
 * @author Isabel Servín
 */
public class SFormWarehouseFillLevel extends sa.lib.gui.bean.SBeanForm {

    private SDbWarehouseFillLevel moRegistry;

    /**
     * Creates new form SFormInputClass
     * @param client
     * @param title
     */
    public SFormWarehouseFillLevel(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.SU_WAH_FILL_LEVEL, SLibConsts.UNDEFINED, title);
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
        jPanel4 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel5 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel6 = new javax.swing.JPanel();
        jlColor = new javax.swing.JLabel();
        moTextColor = new sa.lib.gui.bean.SBeanFieldText();
        jlColorNote = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jlValMin = new javax.swing.JLabel();
        moDecimalValMin = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlValMinNote = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jlValMax = new javax.swing.JLabel();
        moDecimalValMax = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlValMaxNote = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlCode);
        jPanel4.add(moTextCode);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlName);

        moTextName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel5.add(moTextName);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlColor.setText("Color:*");
        jlColor.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlColor);
        jPanel6.add(moTextColor);

        jlColorNote.setText("(Hexadecimal (#0A0A0A) o nombre HTML)");
        jlColorNote.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel6.add(jlColorNote);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlValMin.setText("Valór minimo:*");
        jlValMin.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlValMin);
        jPanel7.add(moDecimalValMin);

        jlValMinNote.setText("(A partir de)");
        jlValMinNote.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel7.add(jlValMinNote);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlValMax.setText("Valór máximo:*");
        jlValMax.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlValMax);
        jPanel8.add(moDecimalValMax);

        jlValMaxNote.setText("(Menor que)");
        jlValMaxNote.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel8.add(jlValMaxNote);

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
    private javax.swing.JLabel jlColor;
    private javax.swing.JLabel jlColorNote;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlValMax;
    private javax.swing.JLabel jlValMaxNote;
    private javax.swing.JLabel jlValMin;
    private javax.swing.JLabel jlValMinNote;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecimalValMax;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecimalValMin;
    private sa.lib.gui.bean.SBeanFieldText moTextCode;
    private sa.lib.gui.bean.SBeanFieldText moTextColor;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);

        moTextCode.setTextSettings(SGuiUtils.getLabelName(jlCode), 5);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName), 50);
        moTextColor.setTextSettings(SGuiUtils.getLabelName(jlColor), 50);
        moDecimalValMin.setDecimalSettings(SGuiUtils.getLabelName(jlValMin), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moDecimalValMin.setMinDouble(0.0);
        moDecimalValMax.setDecimalSettings(SGuiUtils.getLabelName(jlValMax), SGuiConsts.GUI_TYPE_DEC_AMT, true);

        moFields.addField(moTextCode);
        moFields.addField(moTextName);
        moFields.addField(moTextColor);
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

    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbWarehouseFillLevel) registry;

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

        moTextName.setValue(moRegistry.getName());
        moTextCode.setValue(moRegistry.getCode());
        moTextColor.setValue(moRegistry.getColor());
        moDecimalValMin.setValue(moRegistry.getValueMin());
        moDecimalValMax.setValue(moRegistry.getValueMax());
        
        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbWarehouseFillLevel registry = moRegistry.clone();

        if (registry.isRegistryNew()) { }

        registry.setName(moTextName.getValue());
        registry.setCode(moTextCode.getValue());
        registry.setColor(moTextColor.getValue());
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
