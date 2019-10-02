/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.form;

import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;
import som.mod.som.db.SEstimationUtils;

/**
 *
 * @author Edwin Carmona
 * 
 */
public class SDialogEstOilPercentage extends SBeanFormDialog implements ActionListener {

    private SGuiClient miClient;

    private boolean mbFirstTime;

    private int mnEstimationId;
    private int mnRawMaterialId;
    private int mnConsumptionRowId;
    
    /** Creates new form SDialogStockCardex */
    public SDialogEstOilPercentage(SGuiClient client) {
        miClient = client;
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpParams = new javax.swing.JPanel();
        jpControls = new javax.swing.JPanel();
        jbChange = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlOilPercent = new javax.swing.JLabel();
        sBFOilPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tarjeta auxiliar de almacén");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpParams.setLayout(new java.awt.BorderLayout(5, 0));
        getContentPane().add(jpParams, java.awt.BorderLayout.NORTH);

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbChange.setText("Cambiar");
        jbChange.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbChange);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Almacén e ítem:"));
        jPanel4.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOilPercent.setText("% Aceite");
        jlOilPercent.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel8.add(jlOilPercent);
        jPanel8.add(sBFOilPercentage);

        jPanel4.add(jPanel8);

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);
        jPanel4.getAccessibleContext().setAccessibleName("Porcentaje de aceite");

        setSize(new java.awt.Dimension(336, 239));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        SGuiUtils.setWindowBounds(this, 320, 200);

        sBFOilPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlOilPercent.getText()), SGuiConsts.GUI_TYPE_DEC_QTY, true);
        sBFOilPercentage.setMaxDouble(100d);
        sBFOilPercentage.setMinDouble(0d);
        
        moFields.addField(sBFOilPercentage);

        jbChange.addActionListener(this);
    }

    @Override
    protected void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jbChange.requestFocus();
        }
    }

    public void actionChancePercentage() {
        boolean bRes;
        
        SGuiValidation val = validateForm();
        if (! val.isValid()) {
            miClient.showMsgBoxError(val.getMessage());
            return;
        }
        
        try {
            bRes = SEstimationUtils.changeOilPercentage(miClient, mnEstimationId, mnRawMaterialId, mnConsumptionRowId, sBFOilPercentage.getValue() / 100);
            setVisible(false);
        }
        catch (Exception ex) {
            Logger.getLogger(SDialogEstOilPercentage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        miClient.showMsgBoxInformation("La estimación se ha actualizado correctamente.");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbChange;
    private javax.swing.JLabel jlOilPercent;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpParams;
    private sa.lib.gui.bean.SBeanFieldDecimal sBFOilPercentage;
    // End of variables declaration//GEN-END:variables

    public void setFormParams(int estimationId, int rawMaterialId, int consumptionRowId, double percentage) {
        mnEstimationId = estimationId;
        mnRawMaterialId = rawMaterialId;
        mnConsumptionRowId = consumptionRowId;
        if ((percentage < 1d && percentage > 0)) {
            percentage = percentage * 100;
        }
        else if (percentage < 0) {
            percentage = 0;
        }
        
        sBFOilPercentage.setValue(percentage);
    }

    public void formReset() {
        mbFirstTime = true;
        
        sBFOilPercentage.setValue(0d);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbChange) {
                actionChancePercentage();
            }
        }
    }

    @Override
    public void addAllListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reloadCatalogues() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRegistry(SDbRegistry sdr) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (sBFOilPercentage.getValue() < 0 || sBFOilPercentage.getValue() > 100) {
                validation.setMessage("De ingresar un valor entre 0 y 100");
            }
        }
        
        return validation;
    }
}