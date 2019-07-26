/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.form;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiUtils;
import som.mod.SModConsts;
import som.mod.som.db.SRowProductionByLine;

/**
 *
 * @author Edwin Carmona
 */
public class SDialogProductionByLine extends JDialog implements ActionListener {

    private SGuiClient miClient;
    private SGridPaneForm moGridProductionByLine;

    private boolean mbFirstTime;

    /** Creates new form SDialogProductionByLine */
    public SDialogProductionByLine(SGuiClient client) {
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

        jpLines = new javax.swing.JPanel();
        jpControls = new javax.swing.JPanel();
        jbClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Producción por línea");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpLines.setBorder(javax.swing.BorderFactory.createTitledBorder("Producción por línea:"));
        jpLines.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jpLines, java.awt.BorderLayout.CENTER);
        jpLines.getAccessibleContext().setAccessibleName("");
        jpLines.getAccessibleContext().setAccessibleDescription("");

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbClose.setText("Cerrar");
        jbClose.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbClose);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(496, 289));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        SGuiUtils.setWindowBounds(this, 480, 250);

        moGridProductionByLine = new SGridPaneForm(miClient, SModConsts.S_MFG_EST, SLibConsts.UNDEFINED, "Producción por línea") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                SGridColumnForm[] columns = new SGridColumnForm[3];

                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Línea de producción");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Producido");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        jpLines.add(moGridProductionByLine, BorderLayout.CENTER);

        jbClose.addActionListener(this);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jbClose.requestFocus();
        }
    }

    @SuppressWarnings("unchecked")
    private void showStockMoves(Map<String, SRowProductionByLine> fatherRows) {
        Vector<SGridRow> rows = new Vector<>();
        
        try {
            for (Map.Entry<String, SRowProductionByLine> entry : fatherRows.entrySet()) {
                 rows.add(entry.getValue());
            }

            moGridProductionByLine.populateGrid(rows);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    public void actionClose() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbClose;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpLines;
    // End of variables declaration//GEN-END:variables

    public void setFormParams(Map<String, SRowProductionByLine> fatherRows) {
        showStockMoves(fatherRows);
    }

    public void formReset() {
        mbFirstTime = true;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbClose) {
                actionClose();
            }
        }
    }
}
