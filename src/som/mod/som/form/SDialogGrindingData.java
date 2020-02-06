/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.form;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import som.mod.som.db.SDbGrinding;
import som.mod.som.db.SRowData;
import som.mod.som.db.data.SGrindingData;
import som.mod.som.db.data.SGrindingResultsUtils;

/**
 *
 * @author Edwin Carmona
 * 
 */
public class SDialogGrindingData extends JDialog implements ActionListener {

    private SGuiClient miClient;
    private SGridPaneForm moGridEvents;
    private SDbGrinding moGrindingResult;

    private boolean mbFirstTime;
    private Date mtParamDate;
    private int mnItem;
    private int mnLot;
    private int mnGrindingId;

    /** Creates new form SDialogStockCardex */
    public SDialogGrindingData(SGuiClient client) {
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

        jpParams = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        moTextMonth = new sa.lib.gui.bean.SBeanFieldText();
        jPanel9 = new javax.swing.JPanel();
        jlGrinBas = new javax.swing.JLabel();
        moGrindingBasc = new sa.lib.gui.bean.SBeanFieldDecimal();
        jbSaveBasc = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jlDate2 = new javax.swing.JLabel();
        moTextDate = new sa.lib.gui.bean.SBeanFieldText();
        jPanel11 = new javax.swing.JPanel();
        jlDate3 = new javax.swing.JLabel();
        moGrindingOilPercent = new sa.lib.gui.bean.SBeanFieldDecimal();
        jbSaveOilP = new javax.swing.JButton();
        jpEvents = new javax.swing.JPanel();
        jpControls = new javax.swing.JPanel();
        jbClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Datos de molienda");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpParams.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Fecha:"));
        jPanel4.setLayout(new java.awt.GridLayout(2, 4, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Mes:");
        jlDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel8.add(jlDate);

        moTextMonth.setEditable(false);
        moTextMonth.setText("sBeanFieldText3");
        jPanel8.add(moTextMonth);

        jPanel4.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlGrinBas.setText("Molienda Báscula:");
        jlGrinBas.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlGrinBas);
        jPanel9.add(moGrindingBasc);

        jbSaveBasc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbSaveBasc.setToolTipText("Guardar molienda báscula");
        jbSaveBasc.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel9.add(jbSaveBasc);

        jPanel4.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate2.setText("Fecha:");
        jlDate2.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jlDate2);

        moTextDate.setEditable(false);
        moTextDate.setText("sBeanFieldText1");
        jPanel10.add(moTextDate);

        jPanel4.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate3.setText("Molienda Porc. Ac.");
        jlDate3.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlDate3);
        jPanel11.add(moGrindingOilPercent);

        jbSaveOilP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbSaveOilP.setToolTipText("Guardar molienda porcentaje de aceite");
        jbSaveOilP.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbSaveOilP);

        jPanel4.add(jPanel11);

        jpParams.add(jPanel4, java.awt.BorderLayout.WEST);
        jPanel4.getAccessibleContext().setAccessibleName("Fecha");

        getContentPane().add(jpParams, java.awt.BorderLayout.NORTH);

        jpEvents.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de molienda:"));
        jpEvents.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jpEvents, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbClose.setText("Cerrar");
        jbClose.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbClose);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(656, 389));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        SGuiUtils.setWindowBounds(this, 640, 400);

        moTextMonth.setTextSettings(SGuiUtils.getLabelName(jlDate.getText()), 50);

        moGridEvents = new SGridPaneForm(miClient, SModConsts.SU_GRINDING_EVENT, SLibConsts.UNDEFINED, "Eventos durante molienda") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                SGridColumnForm[] columns = new SGridColumnForm[3];

                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Dato");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Valor");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Unidad");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        jpEvents.add(moGridEvents, BorderLayout.CENTER);

        jbClose.addActionListener(this);
        jbSaveBasc.addActionListener(this);
        jbSaveOilP.addActionListener(this);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jbClose.requestFocus();
        }
    }
    
    private void actionSaveGrinding(int option) {
        if (moGrindingResult.getPkResultId() == 0) {
            moGrindingResult.setDateCapture(mtParamDate);
            moGrindingResult.setFkItemId(mnItem);
            moGrindingResult.setFkLotId(mnLot);
        }
        
        moGrindingResult.setGrindingBascule(moGrindingBasc.getValue());
        moGrindingResult.setGrindingOilPerc(moGrindingOilPercent.getValue());
        
        moGrindingResult = SGrindingResultsUtils.saveGrinding(miClient, moGrindingResult);
        this.showEvents();
    }

    @SuppressWarnings("unchecked")
    private void showEvents() {
        Vector<SGridRow> rows = new Vector<>();
        
        boolean onlyCurrentMonth = false;
        ArrayList<SGrindingData> grindingOfMonths = SGrindingResultsUtils.getGrindingByMonth(miClient, mtParamDate, mnItem, onlyCurrentMonth);
        
        double dTotalGrindingBascule = 0d;
        double dTotalGrindingOilPercent = 0d;
        for (SGrindingData grindingOfMonth : grindingOfMonths) {
            SRowData row = new SRowData();
            
            row.setData("Molienda " + grindingOfMonth.getMonthText());
            row.setValueD(grindingOfMonth.getSumGrindingOilPercent());
            row.setUnit("kg");
            
            rows.add(row);
            
            SRowData row1 = new SRowData();
            
            row1.setData("Molienda " + grindingOfMonth.getMonthText() + " báscula");
            row1.setValueD(grindingOfMonth.getSumGrindingBascule());
            row1.setUnit("kg");
            
            rows.add(row1);
            
            dTotalGrindingOilPercent += grindingOfMonth.getSumGrindingOilPercent();
            dTotalGrindingBascule += grindingOfMonth.getSumGrindingBascule();
        }
        
        String [] dataRows = {
                            "Prom. Mensual Residual 9C",
                            "Prom. Mensual Residual Prensa",
                            "Prom. Mensual Cont. Ac.Semilla",
                            "Prom. Mensual Proteína",
                            "Molienda total x dif aceite",
                            "Molienda total  Báscula",
                        };
        
        for (int i = 0; i < dataRows.length; i++) {
            String string = dataRows[i];
            
            SRowData row = new SRowData();
            row.setData(string);
            
            switch (i) {
                case 0:
                    row.setValueD(SGrindingResultsUtils.getMonthAverage(miClient, mtParamDate, mnItem, SGrindingResultsUtils.RESID_PASTA));
                    row.setUnit("%");
                    break;
                case 1:
                    row.setValueD(SGrindingResultsUtils.getMonthAverage(miClient, mtParamDate, mnItem, SGrindingResultsUtils.RESID_PRENSA));
                    row.setUnit(" ");
                    break;
                case 2:
                    row.setValueD(SGrindingResultsUtils.getMonthAverage(miClient, mtParamDate, mnItem, SGrindingResultsUtils.CONT_ACEITE_SEM));
                    row.setUnit("%");
                    break;
                case 3:
                    row.setValueD(SGrindingResultsUtils.getMonthAverage(miClient, mtParamDate, mnItem, SGrindingResultsUtils.PROTEINA));
                    row.setUnit("%");
                    break;
                case 4:
                    row.setValueD(dTotalGrindingOilPercent);
                    row.setUnit("kg");
                    break;
                case 5:
                    row.setValueD(dTotalGrindingBascule);
                    row.setUnit("kg");
                    break;
                    
                default:
                    row.setValueD(0d);
                    row.setUnit("");
            }
            
            rows.add(row);
        }
        
        boolean onlyCurrentDay = false;
        double dWeightedAvgContSem =  SGrindingResultsUtils.getWeightedAverage(miClient, mtParamDate, dTotalGrindingOilPercent, mnItem, SGrindingResultsUtils.CONT_ACEITE_SEM, onlyCurrentDay);
        SRowData row1 = new SRowData();
            
        row1.setData("Ponderado Cont. Aceite Semilla");
        row1.setValueD(dWeightedAvgContSem);
        row1.setUnit("%");

        rows.add(row1);
        
        double dWeightedAvgResPasta =  SGrindingResultsUtils.getWeightedAverage(miClient, mtParamDate, dTotalGrindingOilPercent, mnItem, SGrindingResultsUtils.RESID_PASTA, onlyCurrentDay);
        
        SRowData row = new SRowData();
            
        row.setData("Ponderado Residual Promedio 9C");
        row.setValueD(dWeightedAvgResPasta);
        row.setUnit("%");

        rows.add(row);
        
        moGridEvents.clearGridRows();
        moGridEvents.populateGrid(rows);
    }

    public void actionClose() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbSaveBasc;
    private javax.swing.JButton jbSaveOilP;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDate2;
    private javax.swing.JLabel jlDate3;
    private javax.swing.JLabel jlGrinBas;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpEvents;
    private javax.swing.JPanel jpParams;
    private sa.lib.gui.bean.SBeanFieldDecimal moGrindingBasc;
    private sa.lib.gui.bean.SBeanFieldDecimal moGrindingOilPercent;
    private sa.lib.gui.bean.SBeanFieldText moTextDate;
    private sa.lib.gui.bean.SBeanFieldText moTextMonth;
    // End of variables declaration//GEN-END:variables

    public void setFormParams(final Date dateCutOff, final int item, final int lot) {
        mtParamDate = dateCutOff;
        mnItem = item;
        mnLot = lot;
        
        LocalDate d = ((Date) mtParamDate.clone()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Month mes = d.getMonth();

        // Obtienes el nombre del mes
        String monthName = mes.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        moTextMonth.setValue(monthName.toUpperCase());
        moTextDate.setValue(SLibUtils.DateFormatDate.format(mtParamDate));
        
        moGrindingResult = SGrindingResultsUtils.getGrindingByDate(miClient, mtParamDate);
        
        moGrindingBasc.setValue(moGrindingResult.getGrindingBascule());
        moGrindingOilPercent.setValue(moGrindingResult.getGrindingOilPerc());
        
        showEvents();
    }

    public void formReset() {
        mbFirstTime = true;

        moTextMonth.setValue("");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbClose) {
                actionClose();
            }
            else if (button == jbSaveBasc) {
                this.actionSaveGrinding(SGrindingResultsUtils.GRINDING_BASCULE);
            }
            else if (button == jbSaveOilP) {
                this.actionSaveGrinding(SGrindingResultsUtils.GRINDING_BASCULE);
            }
        }
    }
}
