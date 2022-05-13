/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import java.util.Date;
import org.joda.time.LocalDate;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;
import som.mod.SModConsts;
import som.mod.som.db.SDbGrindingEvent;

/**
 *
 * @author Edwin Carmona
 */
public class SFormGrindingEvent extends SBeanForm {

    private SDbGrindingEvent moRegistry;

    /**
     * Creates new form SFormSeason
     */
    public SFormGrindingEvent(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.SU_GRINDING_EVENT, SLibConsts.UNDEFINED, title);
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
        jPanel4 = new javax.swing.JPanel();
        jlDescription = new javax.swing.JLabel();
        moDescription = new sa.lib.gui.bean.SBeanFieldText();
        jPanel13 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel11 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDatetime();
        jPanel12 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateDateEnd = new sa.lib.gui.bean.SBeanFieldDatetime();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDescription.setText("Evento:*");
        jlDescription.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDescription);

        moDescription.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel4.add(moDescription);

        jPanel2.add(jPanel4);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem afectado:*");
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel13.add(moKeyItem);

        jPanel2.add(jPanel13);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlDateStart);
        jPanel11.add(moDateDateStart);

        jPanel2.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDateEnd);
        jPanel12.add(moDateDateEnd);

        jPanel2.add(jPanel12);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDescription;
    private javax.swing.JLabel jlItem;
    private sa.lib.gui.bean.SBeanFieldDatetime moDateDateEnd;
    private sa.lib.gui.bean.SBeanFieldDatetime moDateDateStart;
    private sa.lib.gui.bean.SBeanFieldText moDescription;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);

        moDescription.setTextSettings(SGuiUtils.getLabelName(jlDescription.getText()), 50);
        moKeyItem.setKeySettings(miClient, SGuiUtils.getLabelName(jlItem.getText()), true);
        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart.getText()), true);
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd.getText()), true);

        moFields.addField(moDescription);
        moFields.addField(moKeyItem);
        moFields.addField(moDateDateStart);
        moFields.addField(moDateDateEnd);
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
        miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SS_LINK_CFG_ITEMS, 0, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbGrindingEvent) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();
        
        Date init = LocalDate.fromDateFields(miClient.getSession().getWorkingDate()).toDate();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            moRegistry.setStartDate(init);
            moRegistry.setEndDate(init);
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moDescription.setValue(moRegistry.getDescription());
        moKeyItem.setValue(new int[] {moRegistry.getFkItemId()});
        moDateDateStart.setValue(moRegistry.getStartDate());
        moDateDateEnd.setValue(moRegistry.getEndDate());

        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbGrindingEvent registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}

        registry.setDescription(moDescription.getValue());
        registry.setFkItemId(moKeyItem.getValue()[0]);
        registry.setStartDate(moDateDateStart.getValue());
        registry.setEndDate(moDateDateEnd.getValue());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            validation = SGuiUtils.validateDateRange(moDateDateStart, moDateDateEnd);
        }

        return validation;
    }
}
