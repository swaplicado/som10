/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import som.mod.SModConsts;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbProcessingBatch;

/**
 *
 * @author Isabel Servín
 */
public class SFormProcessingBatch extends sa.lib.gui.bean.SBeanForm implements ItemListener, ActionListener {

    private SDbProcessingBatch moRegistry;

    /**
     * Creates new form SFormProcessingBatch
     * @param client
     * @param title
     */
    public SFormProcessingBatch(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.S_PRC_BATCH, SLibConsts.UNDEFINED, title);
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
        jlDate = new javax.swing.JLabel();
        moDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel5 = new javax.swing.JPanel();
        jlBatch = new javax.swing.JLabel();
        moTextBatch = new sa.lib.gui.bean.SBeanFieldText();
        jPanel6 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jlClosingCal = new javax.swing.JLabel();
        moKeyClosingCal = new sa.lib.gui.bean.SBeanFieldKey();
        jbClosingCal = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Inicio de vigencia*:");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDate);
        jPanel4.add(moDateStart);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBatch.setText("Lote:*");
        jlBatch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlBatch);

        moTextBatch.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel5.add(moTextBatch);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:*");
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel6.add(moKeyItem);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlClosingCal.setText("Cierre de mes:");
        jlClosingCal.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlClosingCal);

        moKeyClosingCal.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel7.add(moKeyClosingCal);

        jbClosingCal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/som/gui/img/icon_std_new.gif"))); // NOI18N
        jbClosingCal.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbClosingCal);

        jPanel2.add(jPanel7);

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
    private javax.swing.JButton jbClosingCal;
    private javax.swing.JLabel jlBatch;
    private javax.swing.JLabel jlClosingCal;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlItem;
    private sa.lib.gui.bean.SBeanFieldDate moDateStart;
    private sa.lib.gui.bean.SBeanFieldKey moKeyClosingCal;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    private sa.lib.gui.bean.SBeanFieldText moTextBatch;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);

        moDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDate), true);
        moTextBatch.setTextSettings(SGuiUtils.getLabelName(jlBatch), 50);
        moKeyItem.setKeySettings(miClient, SGuiUtils.getLabelName(jlItem), true);
        moKeyClosingCal.setKeySettings(miClient, SGuiUtils.getLabelName(jlClosingCal), false);

        moFields.addField(moDateStart);
        moFields.addField(moTextBatch);
        moFields.addField(moKeyItem);
        moFields.addField(moKeyClosingCal);

        moFields.setFormButton(jbSave);
    }
    
    private void populateKeyClosingCal() {
        try {
            if (moKeyItem.getSelectedIndex() > 0) {
                SDbItem item = new SDbItem();
                item.read(miClient.getSession(), moKeyItem.getValue());

                SGuiParams params = new SGuiParams();
                params.getParamsMap().put(SModConsts.SU_INP_CT, item.getFkInputCategoryId());
                params.getParamsMap().put(SGuiConsts.PARAM_DATE, moDateStart.getValue());
                miClient.getSession().populateCatalogue(moKeyClosingCal, SModConsts.SU_CLOSING_CAL, SLibConsts.UNDEFINED, params);
            }
        }
        catch (Exception e) {}
    }
    
    private void actionClosingCalendar() {
        moKeyClosingCal.removeAllItems();
        miClient.getSession().showForm(SModConsts.SU_CLOSING_CAL, SLibConsts.UNDEFINED, null);
        populateKeyClosingCal();
    }

    @Override
    public void addAllListeners() {
        moKeyItem.addItemListener(this);
        jbClosingCal.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyItem.removeItemListener(this);
        jbClosingCal.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, null);
        moKeyClosingCal.removeAllItems();
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbProcessingBatch) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
            moDateStart.setValue(miClient.getSession().getWorkingDate());
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
            moDateStart.setValue(moRegistry.getDate());
        }

        moTextBatch.setValue(moRegistry.getProcessingBatch());
        moKeyItem.setValue(new int[] { moRegistry.getFkItemId() });
        
        populateKeyClosingCal();
        
        moKeyClosingCal.setValue(new int[] { moRegistry.getFkClosingCalendar_n() });
        
        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbProcessingBatch registry = moRegistry.clone();

        if (registry.isRegistryNew()) { }

        registry.setDate(moDateStart.getValue());
        registry.setProcessingBatch(moTextBatch.getValue());
        registry.setFkItemId(moKeyItem.getValue()[0]);
        registry.setFkClosingCalendar_n(moKeyClosingCal.getSelectedIndex() == 0 ? 0 : moKeyClosingCal.getValue()[0]);
        
        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moKeyItem.getValue()[0] == 6 && moKeyClosingCal.getSelectedIndex() <= 0) {
                validation.setMessage("Debe seleccionar cierre de mes para el ítem seleccionado.");
            }
        }
        
        return validation;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) e.getSource();
            
            if (comboBox == moKeyItem) {
                populateKeyClosingCal();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbClosingCal) {
                actionClosingCalendar();
            }
        }
    }
}
