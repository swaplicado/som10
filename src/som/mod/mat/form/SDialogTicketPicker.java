/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDatePicker;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.bean.SBeanOptionPicker;
import som.mod.mat.db.SMaterialUtils;
import som.mod.mat.db.SRowTicketPicker;

/**
 *
 * @author Isabel Servín
 */
public class SDialogTicketPicker extends SBeanOptionPicker implements ActionListener, ItemListener {

    private Vector<SGridRow> moAllRows;
    private Vector<SGridRow> moFilterRows;
    
    /**
     * Creates new form SDialogTicketsPicker
     */
    public SDialogTicketPicker() {
        initComponents();
    }
    
    public void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 640);
        jpGrid.add(jpFilters, java.awt.BorderLayout.NORTH);
        try {
            jftDate.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter(SGuiUtils.createMaskFormatterDatetime(SLibUtils.DateFormatDate.toPattern()))));
        }
        catch (Exception e) {}
        
        jftDate.setValue(SLibUtils.DateFormatDate.format(miClient.getSession().getSystemDate()));
        
        moFilterRows = new Vector<>();
        
        jbDate.addActionListener(this);
        moRadioWithoutMovs.addItemListener(this);
        moRadioPending.addItemListener(this);
        moRadioWithMovs.addItemListener(this);
        jbDatePlus.addActionListener(this);
        jbDateMin.addActionListener(this);
        
        populateGridPicker();
        moAllRows = new Vector<>(moGridPicker.getModel().getGridRows());
        
        stateChangedWithoutMovs();
    }
    
    private Date parseDate() {
        Date date = null;
        try {
            date = SLibUtils.DateFormatDate.parse(jftDate.getText());
        }
        catch (Exception e) {}
        return date;
    }
    
    private void actionDate() {
        SGuiDatePicker picker = miClient.getDatePicker();
        picker.resetPicker();
        picker.setOption(parseDate());
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            jftDate.setValue(SLibUtils.DateFormatDate.format(picker.getOption()));
            jftDate.requestFocus();
        }
    }
    
    private void actionDatePlus() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        jftDate.setValue(SLibUtils.DateFormatDate.format(calendar.getTime()));
    }
    
    private void actionDateMin() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        jftDate.setValue(SLibUtils.DateFormatDate.format(calendar.getTime()));
    }
    
    private void stateChangedWithoutMovs() {
        jbDate.setEnabled(false);
        jbDatePlus.setEnabled(false);
        jbDateMin.setEnabled(false);
        
        moFilterRows.clear();
        for (SGridRow row : moAllRows) {
            if(!((SRowTicketPicker) row).getHasMov()) {
                moFilterRows.add(row);
            }
        }
        moGridPicker.populateGrid(moFilterRows);
    }
    
    private void stateChangedPending() {
        jbDate.setEnabled(false);
        jbDatePlus.setEnabled(false);
        jbDateMin.setEnabled(false);
        
        moFilterRows.clear();
        for (SGridRow row : moAllRows) {
            if(((SRowTicketPicker) row).getIsPend()) {
                moFilterRows.add(row);
            }
        }
        moGridPicker.populateGrid(moFilterRows);
    }
    
    private void stateChangedWithMovs() {
        jbDate.setEnabled(true);
        jbDatePlus.setEnabled(true);
        jbDateMin.setEnabled(true);
        
        moFilterRows.clear();
        for (SGridRow row : moAllRows) {
            if(SLibTimeUtils.isSameDate(((SRowTicketPicker) row).getDate(), parseDate()) && 
                    ((SRowTicketPicker) row).getHasMov()) {
                moFilterRows.add(row);
            }
        }
        moGridPicker.populateGrid(moFilterRows);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jpFilters = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        moRadioWithoutMovs = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadioPending = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadioWithMovs = new sa.lib.gui.bean.SBeanFieldRadio();
        jftDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jbDateMin = new javax.swing.JButton();
        jbDatePlus = new javax.swing.JButton();

        jpFilters.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtrar por:"));
        jpFilters.setLayout(new java.awt.GridLayout(1, 0, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        buttonGroup1.add(moRadioWithoutMovs);
        moRadioWithoutMovs.setSelected(true);
        moRadioWithoutMovs.setText("Sin movimientos almacén");
        moRadioWithoutMovs.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(moRadioWithoutMovs);

        buttonGroup1.add(moRadioPending);
        moRadioPending.setText("Con entrada parcial");
        moRadioPending.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(moRadioPending);

        buttonGroup1.add(moRadioWithMovs);
        moRadioWithMovs.setText("Con movimientos de almacén");
        moRadioWithMovs.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(moRadioWithMovs);

        jftDate.setEditable(false);
        jftDate.setEnabled(false);
        jftDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jftDate);

        jbDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/som/gui/img/gui_date.gif"))); // NOI18N
        jbDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbDate);

        jbDateMin.setText("Restar un día");
        jbDateMin.setEnabled(false);
        jbDateMin.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jbDateMin);

        jbDatePlus.setText("Sumar un día");
        jbDatePlus.setEnabled(false);
        jbDatePlus.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jbDatePlus);

        jpFilters.add(jPanel3);

        getContentPane().add(jpFilters, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbDate;
    private javax.swing.JButton jbDateMin;
    private javax.swing.JButton jbDatePlus;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JPanel jpFilters;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioPending;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioWithMovs;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioWithoutMovs;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void populateGridPicker() {
        int col;
        int cols = moSettings.getGridColumns().size();
        int[] key = null;
        Class colClass;
        ResultSet resultSet;
        SRowTicketPicker row;
        Vector<SGridRow> rows = new Vector<>();
        
        try {
            resultSet = miClient.getSession().getStatement().executeQuery(moSettings.getSql());
            while (resultSet.next()) {
                if (moSettings.getPrimaryKeyLength() > 0) {
                    key = new int[moSettings.getPrimaryKeyLength()];
                    for (col = 0; col < moSettings.getPrimaryKeyLength(); col++) {
                        key[col] = resultSet.getInt(SDbConsts.FIELD_ID + (col + 1));
                    }
                }

                row = new SRowTicketPicker(key);
                for (col = 0; col < cols; col++) {
                    colClass = SGridUtils.getDataTypeClass(moSettings.getGridColumns().get(col).getColumnType());

                    if (colClass == Long.class) {
                        row.getValues().add(resultSet.getLong(SDbConsts.FIELD_PICK + (col + 1)));
                    }
                    else if (colClass == Integer.class) {
                        row.getValues().add(resultSet.getInt(SDbConsts.FIELD_PICK + (col + 1)));
                    }
                    else if (colClass == Double.class) {
                        row.getValues().add(resultSet.getDouble(SDbConsts.FIELD_PICK + (col + 1)));
                    }
                    else if (colClass == Float.class) {
                        row.getValues().add(resultSet.getFloat(SDbConsts.FIELD_PICK + (col + 1)));
                    }
                    else if (colClass == Boolean.class) {
                        row.getValues().add(resultSet.getBoolean(SDbConsts.FIELD_PICK + (col + 1)));
                    }
                    else if (colClass == String.class) {
                        row.getValues().add(resultSet.getString(SDbConsts.FIELD_PICK + (col + 1)));
                    }
                    else if (colClass == Date.class) {
                        switch (moSettings.getGridColumns().get(col).getColumnType()) {
                            case SGridConsts.COL_TYPE_DATE:
                                row.getValues().add(resultSet.getDate(SDbConsts.FIELD_PICK + (col + 1)));
                                break;
                            case SGridConsts.COL_TYPE_DATE_DATETIME:
                                row.getValues().add(resultSet.getTimestamp(SDbConsts.FIELD_PICK + (col + 1)));
                                break;
                            case SGridConsts.COL_TYPE_DATE_TIME:
                                row.getValues().add(resultSet.getTime(SDbConsts.FIELD_PICK + (col + 1)));
                                break;
                            default:
                                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                    }
                    else {
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                }

                if (moSettings.isMainOptionApplying()) {
                    switch (moSettings.getMainOptionDataType()) {
                        case SLibConsts.DATA_TYPE_INT:
                            row.setMainOption(resultSet.getLong(SDbConsts.FIELD_VALUE));
                            break;
                        case SLibConsts.DATA_TYPE_DEC:
                            row.setMainOption(resultSet.getDouble(SDbConsts.FIELD_VALUE));
                            break;
                        case SLibConsts.DATA_TYPE_BOOL:
                            row.setMainOption(resultSet.getBoolean(SDbConsts.FIELD_VALUE));
                            break;
                        case SLibConsts.DATA_TYPE_TEXT:
                            row.setMainOption(resultSet.getString(SDbConsts.FIELD_VALUE));
                            break;
                        case SLibConsts.DATA_TYPE_DATE:
                            row.setMainOption(resultSet.getDate(SDbConsts.FIELD_VALUE));
                            break;
                        default:
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                }
                row.setDate(resultSet.getDate(SDbConsts.FIELD_PICK + "3"));
                row.setHasMov(resultSet.getBoolean(SMaterialUtils.TIC_HAS_MOV));
                row.setIsPend(resultSet.getBoolean(SMaterialUtils.TIC_IS_PEND));
                
                rows.add(row);
            }

            moGridPicker.populateGrid(rows);
        }
        catch (SQLException e) {
            SLibUtils.showException(this, e);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    @Override
    public Object getOption() {
        Object option = null;
        SRowTicketPicker row = (SRowTicketPicker) moGridPicker.getSelectedGridRow();

        if (row != null) {
            option = moSettings.isMainOptionApplying() ? row.getMainOption() : row.getRowPrimaryKey();
        }

        return option;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbDate) {
                actionDate();
            }
            else if (button == jbDatePlus) {
                actionDatePlus();
            }
            else if (button == jbDateMin) {
                actionDateMin();
            }
            stateChangedWithMovs();            
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            
            if (radioButton == moRadioWithoutMovs) {
                stateChangedWithoutMovs();
            }
            else if (radioButton == moRadioPending) {
                stateChangedPending();
            }
            else if (radioButton == moRadioWithMovs) {
                stateChangedWithMovs();
            }
        }
    }
}
