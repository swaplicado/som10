/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.form;

import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Isabel Servín
 * 
 */
public class SDialogWahLabTestMails extends JDialog implements ActionListener {
    
    private final SGuiClient miClient;
    
    protected int mnFormResult;
    
    public SDialogWahLabTestMails(SGuiClient client) {
        super(client.getFrame(), true);
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

        jptext = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jpArea = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaMails = new javax.swing.JTextArea();
        jptext1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Destinatarios");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jptext.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jptext.setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("El reporte se enviara a los siguientes destinatarios (separados por salto de línea):");
        jPanel1.add(jLabel1);

        jptext.add(jPanel1);

        getContentPane().add(jptext, java.awt.BorderLayout.NORTH);

        jpArea.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpArea.setLayout(new java.awt.BorderLayout());

        jtaMails.setColumns(20);
        jtaMails.setRows(5);
        jScrollPane1.setViewportView(jtaMails);

        jpArea.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpArea, java.awt.BorderLayout.CENTER);

        jptext1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jptext1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setPreferredSize(new java.awt.Dimension(77, 23));
        jPanel2.add(jbOk);

        jbClose.setText("Cerrar");
        jbClose.setPreferredSize(new java.awt.Dimension(77, 23));
        jPanel2.add(jbClose);

        jptext1.add(jPanel2);

        getContentPane().add(jptext1, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(656, 439));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        SGuiUtils.setWindowBounds(this, 640, 400);
        
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        
        setTextAreaMails();

        jbOk.addActionListener(this);
        jbClose.addActionListener(this);
        
        JEditorPane panelEditor = new JEditorPane();
        panelEditor.setContentType("text/html");
        panelEditor.setText(null);
        
    }
    
    private void setTextAreaMails() {
        try {
            String sql = "SELECT wah_lab_rep_mails FROM cu_co;";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                jtaMails.setText(resultSet.getString(1).replaceAll(";", "\n"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void saveMails() {
        try {
            String sql = "UPDATE cu_co SET wah_lab_rep_mails = '" + jtaMails.getText().replaceAll("\n", ";") + "' WHERE id_co = 1";
            miClient.getSession().getStatement().execute(sql);
        }
        catch (Exception e) {}
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbOk;
    private javax.swing.JPanel jpArea;
    private javax.swing.JPanel jptext;
    private javax.swing.JPanel jptext1;
    private javax.swing.JTextArea jtaMails;
    // End of variables declaration//GEN-END:variables

    public void actionClose() {
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        setVisible(false);
    }
    
    public void actionOk() {
        mnFormResult = SGuiConsts.FORM_RESULT_OK;
        saveMails();
        setVisible(false);
    }
    
    public int getFormResult() {
        return mnFormResult;
    }
    
    public String getMails() {
        return jtaMails.getText().replaceAll("\n", ";");
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
