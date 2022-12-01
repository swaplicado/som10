/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.form;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Isabel Servín
 * 
 */
public class SDialogStockReportPreview extends JDialog implements ActionListener {
    
    private final SGuiClient miClient;
    private final String msHtml;
    
    protected int mnFormResult;
    
    public SDialogStockReportPreview(SGuiClient client, String html) {
        super(client.getFrame(), true);
        miClient = client;
        msHtml = html;
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

        jpView = new javax.swing.JPanel();
        jptext1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Reporte de existencias (vista previa)");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpView.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpView.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jpView, java.awt.BorderLayout.CENTER);

        jptext1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jptext1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Enviar");
        jbOk.setPreferredSize(new java.awt.Dimension(77, 23));
        jPanel2.add(jbOk);

        jbClose.setText("Cancelar");
        jbClose.setPreferredSize(new java.awt.Dimension(77, 23));
        jPanel2.add(jbClose);

        jptext1.add(jPanel2);

        getContentPane().add(jptext1, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(1216, 789));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        SGuiUtils.setWindowBounds(this, 1200, 700);
        
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        
        jbOk.addActionListener(this);
        jbClose.addActionListener(this);
        
        JEditorPane editor = new JEditorPane();
        JScrollPane scroll = new JScrollPane(editor);
        editor.setContentType("text/html");
        editor.setText(null);
        jpView.add(scroll);
        editor.setText(msHtml);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbOk;
    private javax.swing.JPanel jpView;
    private javax.swing.JPanel jptext1;
    // End of variables declaration//GEN-END:variables

    public void actionClose() {
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        setVisible(false);
    }
    
    public void actionOk() {
        mnFormResult = SGuiConsts.FORM_RESULT_OK;
        setVisible(false);
    }
    
    public int getFormResult() {
        return mnFormResult;
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            if (button == jbClose) {
                actionClose();
            }
            if (button == jbOk) {
                actionOk();
            }
            
        }
    }
}
