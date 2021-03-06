/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;
import som.gui.SGuiClientSessionCustom;
import som.gui.SGuiClientUtils;
import som.mod.SModConsts;
import som.mod.som.db.SDbIogExportation;

/**
 *
 * @author Néstor Ávalos
 */
public class SFormDialogIogExportation extends SBeanFormDialog implements ActionListener {

    private SDbIogExportation moRegistry;
    private SGuiClient moClient;

    /**
     * Creates new form SFormProductionEstimate
     */
    public SFormDialogIogExportation(SGuiClient client, String title) {
        moClient = client;
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.S_IOG_EXP, SLibConsts.UNDEFINED, title);
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
        jPanel3 = new javax.swing.JPanel();
        jlDateExport = new javax.swing.JLabel();
        moDateDate = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del proceso:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateExport.setText("Fecha exportación:*");
        jlDateExport.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlDateExport);
        jPanel3.add(moDateDate);

        jPanel2.add(jPanel3);
        jPanel2.add(jPanel5);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));
        jPanel2.add(jPanel4);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel jlDateExport;
    private sa.lib.gui.bean.SBeanFieldDate moDateDate;
    // End of variables declaration//GEN-END:variables

    /*
    * Private methods
    */

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 320, 200);
        jbCancel.setEnabled(true);

        jbSave.setSize(100, 25);
        jbSave.setText("Exportar");

        moDateDate.setDateSettings(miClient, "Fecha exportación", true);

        moFields.addField(moDateDate);

        moFields.setFormButton(jbSave);

        /* XXX Cell renderers not implemented yet in XML
        mvFormGrids.add(moGridUserRights);
        mvFormGrids.add(moGridUserScales);
        */
    }

    /*
    * Public methods
    */

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
        moRegistry = (SDbIogExportation) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        //moDate.setValue(moRegistry.getDate());

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moDateDate.setValue(miClient.getSession().getWorkingDate());
            moDateDate.setEnabled(true);
        }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {

        // Create registry of exportation:

        moRegistry.setDate(moDateDate.getValue());

        return moRegistry;
    }

    @Override
    public void actionSave() {
        if (jbSave.isEnabled()) {
            if (SGuiUtils.computeValidation(miClient, validateForm())) {
                SDbRegistry registry = null;

                SGuiUtils.setCursorWait(moClient);
                try {
                    registry = getRegistry();

                    if (registry != null) {
                        if (registry.canSave(miClient.getSession())) {
                            registry.save(miClient.getSession());
                            if (registry.getQueryResultId() == SDbConsts.SAVE_OK) {
                                mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
                                miClient.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED);
                                miClient.getSession().notifySuscriptors(SModConsts.S_IOG);
                                miClient.getSession().notifySuscriptors(SModConsts.S_IOG_EXP);
                                miClient.getSession().notifySuscriptors(SModConsts.S_STK);
                                dispose();
                            }
                            else {
                                miClient.showMsgBoxInformation("Error en el proceso de exportación de doctos. de inventarios.");
                            }
                        }
                        else {
                            miClient.showMsgBoxInformation(registry.getQueryResult());
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
                finally {
                    SGuiUtils.setCursorDefault(moClient);
                }
            }
        }
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (!SGuiClientUtils.isPeriodOpened(miClient.getSession(), moDateDate.getValue())) {
                validation.setMessage("El período está cerrado.");
                validation.setComponent(moDateDate.getComponent());
            }
        }

        if (validation.isValid()) {
            if (jbSave.isEnabled() && miClient.showMsgBoxConfirm("Se exportaran todos los documentos de inventarios con fecha '" +
                SLibUtils.DateFormatDate.format(moDateDate.getValue()) + "' al sistema externo. \n " + SGuiConsts.MSG_CNF_CONT + "") == JOptionPane.YES_OPTION) {
                    moRegistry.setXtaExternalDatabaseName(((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getExtDatabaseCo().getDbName());
                    moRegistry.setDate(moDateDate.getValue());
            }
            else {
                validation.setMessage("Especifique la fecha de exportación de la información.");
                validation.setComponent(moDateDate.getComponent());
            }
        }

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            JButton button = (JButton) evt.getSource();

        }
    }
}
