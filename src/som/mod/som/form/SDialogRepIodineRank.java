/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanDialogReport;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbIodineValueRank;
import som.mod.som.view.SPaneUserInputCategory;

/**
 *
 * @author Juan Barajas, Isabel Servín
 */
public class SDialogRepIodineRank extends SBeanDialogReport implements ItemListener {

    private SGuiFieldKeyGroup moFieldKeyGroup;

    private boolean mbRepIodine;

    /**
     * Creates new form SDialogRepIodineRank
     * @param client
     * @param type
     * @param subtype
     * @param title
     */
    public SDialogRepIodineRank(SGuiClient client, int type, int subtype, String title) {
        setFormSettings(client, type, subtype, title);
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

        bgReportType = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlReportType = new javax.swing.JLabel();
        moRadByInputCategory = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadByInputClass = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadByInputType = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadByItem = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel12 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel13 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel14 = new javax.swing.JPanel();
        jlInputCategory = new javax.swing.JLabel();
        moKeyInputCategory = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel4 = new javax.swing.JPanel();
        jlInputClass = new javax.swing.JLabel();
        moKeyInputClass = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel5 = new javax.swing.JPanel();
        jlInputType = new javax.swing.JLabel();
        moKeyInputType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel15 = new javax.swing.JPanel();
        jlIodineValueRank = new javax.swing.JLabel();
        moKeyIodineValueRank = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel8 = new javax.swing.JPanel();
        jlLimitLow = new javax.swing.JLabel();
        moDecLimitLow = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel9 = new javax.swing.JPanel();
        jlLimitTop = new javax.swing.JLabel();
        moDecLimitTop = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel16 = new javax.swing.JPanel();
        jlTicketOrigin = new javax.swing.JLabel();
        moKeyTicketOrigin = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel17 = new javax.swing.JPanel();
        jlTicketDestination = new javax.swing.JLabel();
        moKeyTicketDestination = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel18 = new javax.swing.JPanel();
        jlTicketScale = new javax.swing.JLabel();
        moKeyTicketScale = new sa.lib.gui.bean.SBeanFieldKey();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(13, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReportType.setText("Tipo reporte:");
        jlReportType.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel11.add(jlReportType);

        bgReportType.add(moRadByInputCategory);
        moRadByInputCategory.setText("Por categoría insumo");
        moRadByInputCategory.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel11.add(moRadByInputCategory);

        bgReportType.add(moRadByInputClass);
        moRadByInputClass.setText("Por clase insumo");
        moRadByInputClass.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel11.add(moRadByInputClass);

        bgReportType.add(moRadByInputType);
        moRadByInputType.setText("Por tipo insumo");
        moRadByInputType.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel11.add(moRadByInputType);

        bgReportType.add(moRadByItem);
        moRadByItem.setText("Por ítem");
        moRadByItem.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(moRadByItem);

        jPanel2.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel12.add(jlDateStart);
        jPanel12.add(moDateStart);

        jPanel2.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel13.add(jlDateEnd);
        jPanel13.add(moDateEnd);

        jPanel2.add(jPanel13);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputCategory.setText("Categoría insumo:");
        jlInputCategory.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel14.add(jlInputCategory);

        moKeyInputCategory.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel14.add(moKeyInputCategory);

        jPanel2.add(jPanel14);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputClass.setText("Clase insumo:");
        jlInputClass.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel4.add(jlInputClass);

        moKeyInputClass.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel4.add(moKeyInputClass);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputType.setText("Tipo insumo:");
        jlInputType.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel5.add(jlInputType);

        moKeyInputType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel5.add(moKeyInputType);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:*");
        jlItem.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel6.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(moKeyItem);

        jPanel2.add(jPanel6);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlIodineValueRank.setText("Rango de yodo:");
        jlIodineValueRank.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel15.add(jlIodineValueRank);

        moKeyIodineValueRank.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel15.add(moKeyIodineValueRank);

        jPanel2.add(jPanel15);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLimitLow.setText("Límite inferior:");
        jlLimitLow.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel8.add(jlLimitLow);
        jPanel8.add(moDecLimitLow);

        jPanel2.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLimitTop.setText("Límite superior:");
        jlLimitTop.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel9.add(jlLimitTop);
        jPanel9.add(moDecLimitTop);

        jPanel2.add(jPanel9);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTicketOrigin.setText("Procedencia boleto:");
        jlTicketOrigin.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel16.add(jlTicketOrigin);

        moKeyTicketOrigin.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel16.add(moKeyTicketOrigin);

        jPanel2.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTicketDestination.setText("Destino boleto:");
        jlTicketDestination.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel17.add(jlTicketDestination);

        moKeyTicketDestination.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel17.add(moKeyTicketDestination);

        jPanel2.add(jPanel17);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTicketScale.setText("Báscula:");
        jlTicketScale.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel18.add(jlTicketScale);

        moKeyTicketScale.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel18.add(moKeyTicketScale);

        jPanel2.add(jPanel18);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgReportType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlInputCategory;
    private javax.swing.JLabel jlInputClass;
    private javax.swing.JLabel jlInputType;
    private javax.swing.JLabel jlIodineValueRank;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlLimitLow;
    private javax.swing.JLabel jlLimitTop;
    private javax.swing.JLabel jlReportType;
    private javax.swing.JLabel jlTicketDestination;
    private javax.swing.JLabel jlTicketOrigin;
    private javax.swing.JLabel jlTicketScale;
    private sa.lib.gui.bean.SBeanFieldDate moDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateStart;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecLimitLow;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecLimitTop;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputCategory;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputClass;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyIodineValueRank;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTicketDestination;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTicketOrigin;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTicketScale;
    private sa.lib.gui.bean.SBeanFieldRadio moRadByInputCategory;
    private sa.lib.gui.bean.SBeanFieldRadio moRadByInputClass;
    private sa.lib.gui.bean.SBeanFieldRadio moRadByInputType;
    private sa.lib.gui.bean.SBeanFieldRadio moRadByItem;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 800, 500);

        moFieldKeyGroup = new SGuiFieldKeyGroup(miClient);
        mbRepIodine = mnFormSubtype == SModSysConsts.REP_LAB_TEST_IOD;

        moRadByInputType.setBooleanSettings(moRadByInputType.getText(), false);
        moRadByInputClass.setBooleanSettings(moRadByInputClass.getText(), false);
        moRadByInputCategory.setBooleanSettings(moRadByInputCategory.getText(), false);
        moDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart.getText()), true);
        moDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd.getText()), true);
        moKeyInputCategory.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputCategory), true);
        moKeyInputClass.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputClass), true);
        moKeyInputType.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputType), true);
        moKeyItem.setKeySettings(miClient, SGuiUtils.getLabelName(jlItem.getText()), true);
        moKeyIodineValueRank.setKeySettings(miClient, SGuiUtils.getLabelName(jlIodineValueRank.getText()), false);
        moDecLimitLow.setDecimalSettings(SGuiUtils.getLabelName(jlLimitLow.getText()), SGuiConsts.GUI_TYPE_DEC_PER, false);
        moDecLimitTop.setDecimalSettings(SGuiUtils.getLabelName(jlLimitTop.getText()), SGuiConsts.GUI_TYPE_DEC_PER, false);
        moKeyTicketOrigin.setKeySettings(miClient, SGuiUtils.getLabelName(jlTicketOrigin), false);
        moKeyTicketDestination.setKeySettings(miClient, SGuiUtils.getLabelName(jlTicketDestination), false);
        moKeyTicketScale.setKeySettings(miClient, SGuiUtils.getLabelName(jlTicketScale), false);
        
        moFields.addField(moRadByInputType);
        moFields.addField(moRadByInputClass);
        moFields.addField(moRadByInputCategory);
        moFields.addField(moDateStart);
        moFields.addField(moDateEnd);
        moFields.addField(moKeyInputCategory);
        moFields.addField(moKeyInputClass);
        moFields.addField(moKeyInputType);
        moFields.addField(moKeyItem);
        moFields.addField(moKeyIodineValueRank);
        moFields.addField(moDecLimitLow);
        moFields.addField(moDecLimitTop);
        moFields.addField(moKeyTicketOrigin);
        moFields.addField(moKeyTicketDestination);
        moFields.addField(moKeyTicketScale);

        moFields.setFormButton(jbPrint);

        moDateStart.setValue(SLibTimeUtils.getBeginOfYear(miClient.getSession().getWorkingDate()));
        moDateEnd.setValue(SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()));

        moKeyItem.addItemListener(this);
        moKeyIodineValueRank.addItemListener(this);
        moRadByInputCategory.addItemListener(this);
        moRadByInputClass.addItemListener(this);
        moRadByInputType.addItemListener(this);
        moRadByItem.addItemListener(this);

        resetForm();
        enableComboBoxes();             
    }

    /*
     * Private methods
     */

    private void resetForm() {

        reloadCatalogues();
        editableIodineValueRank();
    }

    private void editableIodineValueRank() {
        moKeyIodineValueRank.setEditable(mbRepIodine && moKeyItem.getSelectedIndex() > 0);
        jlIodineValueRank.setEnabled(mbRepIodine && moKeyItem.getSelectedIndex() > 0);
    }

    private void itemStateKeyIodineValueRank() {
        if (moKeyIodineValueRank.getSelectedIndex() <= 0) {
            moDecLimitLow.setEditable(true);
            moDecLimitTop.setEditable(true);
        }
        else {
            moDecLimitLow.setEditable(false);
            moDecLimitTop.setEditable(false);
        }
    }
    
    private void enableComboBoxes() {
        moKeyInputCategory.resetField();
        moKeyInputClass.resetField();
        moKeyInputType.resetField();
        moKeyItem.resetField();
        moKeyInputCategory.setEnabled(moRadByInputCategory.isSelected());
        moKeyInputClass.setEnabled(moRadByInputClass.isSelected());
        moKeyInputType.setEnabled(moRadByInputType.isSelected());
        moKeyItem.setEnabled(moRadByItem.isSelected());
    }

    private String getSqlHaving() {
        SDbIodineValueRank rank;
        String sqlHaving = "";

        if (moDecLimitLow.isEditable()) {
            if (moDecLimitLow.getValue() > 0) {
                sqlHaving += (sqlHaving.isEmpty() ? "HAVING " : "AND ") + "f_val >= " + moDecLimitLow.getValue();
            }

            if (moDecLimitTop.getValue() > 0) {
                sqlHaving += (sqlHaving.isEmpty() ? "HAVING " : " AND ") + "f_val <= " + moDecLimitTop.getValue();
            }
        }
        else if (moKeyIodineValueRank.getSelectedIndex() > 0) {
            rank = (SDbIodineValueRank) miClient.getSession().readRegistry(SModConsts.SU_IOD_VAL_RANK, moKeyIodineValueRank.getValue());

            if (rank.getLimitLow_n() > 0) {
                sqlHaving += (sqlHaving.isEmpty() ? "HAVING " : "AND ") + "f_val >= " + rank.getLimitLow_n();
            }

            if (rank.getLimitTop_n() > 0) {
                sqlHaving += (sqlHaving.isEmpty() ? "HAVING " : " AND ") + "f_val <= " + rank.getLimitTop_n();
            }
        }

        return sqlHaving;
    }

    /*
     * Public methods:
     */

    public void reloadCatalogues() {
        SGuiParams params = new SGuiParams(
            mbRepIodine ? SModSysConsts.SX_IOD_VAL :
            mnFormSubtype == SModSysConsts.REP_LAB_TEST_OLE ? SModSysConsts.SX_OLE_PER :
            mnFormSubtype == SModSysConsts.REP_LAB_TEST_LIN ? SModSysConsts.SX_LIN_PER :
            mnFormSubtype == SModSysConsts.REP_LAB_TEST_LLC ? SModSysConsts.SX_LLC_PER : SLibConsts.UNDEFINED);

        moFieldKeyGroup.initGroup();
        moFieldKeyGroup.addFieldKey(moKeyItem, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, params);
        moFieldKeyGroup.addFieldKey(moKeyIodineValueRank, SModConsts.SU_IOD_VAL_RANK, SLibConsts.UNDEFINED, null);
        moFieldKeyGroup.populateCatalogues();
        moFieldKeyGroup.resetGroup();
        
        miClient.getSession().populateCatalogue(moKeyInputCategory, SModConsts.SU_INP_CT, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyInputClass, SModConsts.SU_INP_CL_ALL, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyInputType, SModConsts.SU_INP_TP_ALL, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTicketOrigin, SModConsts.SU_TIC_ORIG, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTicketDestination, SModConsts.SU_TIC_DEST, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTicketScale, SModConsts.SU_SCA, SLibConsts.UNDEFINED, null);
        
        moDecLimitLow.resetField();
        moDecLimitTop.resetField();
        
        moRadByInputCategory.setSelected(true);
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (moRadByInputCategory.isSelected() && (moKeyInputCategory.getSelectedIndex() <= 0 
                    || moKeyInputCategory.getValue()[0] == SModSysConsts.SU_INP_CT_NA)) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlInputCategory) + "'.");
                validation.setComponent(moKeyInputCategory);
            }
            else if (moRadByInputClass.isSelected() && moKeyInputClass.getSelectedIndex() <= 0 
                    && moKeyInputClass.getValue()[0] == SModSysConsts.SU_INP_CL_NA[0]
                    && moKeyInputClass.getValue()[1] == SModSysConsts.SU_INP_CL_NA[1]) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlInputClass) + "'.");
                validation.setComponent(moKeyInputClass);
            }
            else if (moRadByInputType.isSelected() && moKeyInputType.getSelectedIndex() <= 0 
                    && moKeyInputType.getValue()[0] == SModSysConsts.SU_INP_TP_NA[0]
                    && moKeyInputType.getValue()[1] == SModSysConsts.SU_INP_TP_NA[1]
                    && moKeyInputType.getValue()[2] == SModSysConsts.SU_INP_TP_NA[2]) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlInputType) + "'.");
                validation.setComponent(moKeyInputType);
            }
            else if (moRadByItem.isSelected() && moKeyItem.getSelectedIndex() <= 0){
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlItem) + "'.");
                validation.setComponent(moKeyItem);
            }
        }

        if (validation.isValid()) {
            validation = SGuiUtils.validateDateRange(moDateStart, moDateEnd);

            if (moDecLimitLow.isEditable()) {
                if (validation.isValid() && moDecLimitLow.isEditable() && (moDecLimitLow.getValue() == 0 && moDecLimitTop.getValue() == 0)) {
                    validation.setMessage("Debe al menos especificar un límite.");
                    validation.setComponent(moDecLimitLow);
                }
                else if (validation.isValid() && moDecLimitTop.getValue() != 0) {
                    if (moDecLimitTop.getValue() < moDecLimitLow.getValue()) {
                        validation.setMessage("El límite inferior no puede ser mayor al límite superior.");
                        validation.setComponent(moDecLimitLow);
                    }
                }

                if (validation.isValid() && !mbRepIodine) {
                    if (moDecLimitLow.getValue() > 1) {
                        validation.setMessage("El límite inferior no puede ser mayor al 100%.");
                        validation.setComponent(moDecLimitLow);
                    }
                    else if (moDecLimitTop.getValue() > 1) {
                        validation.setMessage("El límite superior no puede ser mayor al 100%.");
                        validation.setComponent(moDecLimitTop);
                    }
                }
            }
        }

        return validation;
    }

    @Override
    public void createParamsMap() {
        String sqlWhere = "";
        String header;
        moParamsMap = SPrtUtils.createReportParamsMap(miClient.getSession());
        
        SPaneUserInputCategory inputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "it");
        String sqlInputCategories = inputCategory.getSqlFilter();
        if (!sqlInputCategories.isEmpty()) {
            sqlWhere += "AND " + sqlInputCategories;
        }
        
        if (moRadByInputCategory.isSelected()) {
            header = "CATEGORIA INSUMO: " + moKeyInputCategory.getSelectedItem().getItem();
            sqlWhere += " AND it.fk_inp_ct = " + moKeyInputCategory.getValue()[0] + " ";
        }
        else if (moRadByInputClass.isSelected()) {
            header = "CLASE INSUMO: " + moKeyInputClass.getSelectedItem().getItem();
            sqlWhere += " AND it.fk_inp_ct = " + moKeyInputClass.getValue()[0] + " " +
                    "AND it.fk_inp_cl = " + moKeyInputClass.getValue()[1] + " ";
        }
        else if (moRadByInputType.isSelected()) {
            header = "TIPO INSUMO: " + moKeyInputType.getSelectedItem().getItem();
            sqlWhere += " AND it.fk_inp_ct = " + moKeyInputType.getValue()[0] + " " +
                    "AND it.fk_inp_cl = " + moKeyInputType.getValue()[1] + " " +
                    "AND it.fk_inp_cl = " + moKeyInputType.getValue()[1] + " ";
        }
        else {
            header = "ÍTEM: " + moKeyItem.getSelectedItem().getItem();
            sqlWhere += " AND t.fk_item = " + moKeyItem.getValue()[0] + " ";
        }
        sqlWhere += (moKeyTicketOrigin.getSelectedIndex() <= 0 ? "" : "AND t.fk_tic_orig = " + moKeyTicketOrigin.getValue()[0] + " ");
        sqlWhere += (moKeyTicketDestination.getSelectedIndex() <= 0 ? "" : "AND t.fk_tic_dest = " + moKeyTicketDestination.getValue()[0] + " ");
        sqlWhere += (moKeyTicketScale.getSelectedIndex() <= 0 ? "" : "AND t.fk_sca = " + moKeyTicketScale.getValue()[0] + " ");
        
        moParamsMap.put("tDateStart", moDateStart.getValue());
        moParamsMap.put("tDateEnd", moDateEnd.getValue());
        /* Isabel Servín 18/04/2022: Estos parámetros ya no son necesarios, se mandan en el where y en el header */ 
//        moParamsMap.put("nItemId", moKeyItem.getValue()[0]);
//        moParamsMap.put("sItem", moKeyItem.getSelectedItem());
        moParamsMap.put("sHeader", header);
        moParamsMap.put("sRank", moKeyIodineValueRank.getSelectedIndex() <= 0 ? ((
                (!mbRepIodine ? SLibUtils.DecimalFormatPercentage4D.format(moDecLimitLow.getValue()) : SLibUtils.DecimalFormatValue4D.format(moDecLimitLow.getValue())) + " - " +
                (!mbRepIodine ? SLibUtils.DecimalFormatPercentage4D.format(moDecLimitTop.getValue()) : SLibUtils.DecimalFormatValue4D.format(moDecLimitTop.getValue())))) :
                moKeyIodineValueRank.getSelectedItem());
        moParamsMap.put("sSqlWhere", sqlWhere);
        moParamsMap.put("sSqlHaving", getSqlHaving());
        moParamsMap.put("sMessageFilter", inputCategory.getReportMessageFilter());
        moParamsMap.put("sTicOrig", moKeyTicketOrigin.getSelectedIndex() > 0 ? moKeyTicketOrigin.getSelectedItem() : "TODOS");
        moParamsMap.put("sTicDest", moKeyTicketDestination.getSelectedIndex() > 0 ? moKeyTicketDestination.getSelectedItem() : "TODOS");
        moParamsMap.put("sTicSca", moKeyTicketScale.getSelectedIndex() > 0 ? moKeyTicketScale.getSelectedItem() : "TODAS");
        
        if (mnFormSubtype != SLibConsts.UNDEFINED) {
            moParamsMap.put("bRepIodine", mnFormSubtype == SModSysConsts.REP_LAB_TEST_IOD);
            moParamsMap.put("nTicketStatusId", SModSysConsts.SS_TIC_ST_ADM);
            moParamsMap.put("sSqlTest",
                mnFormSubtype == SModSysConsts.REP_LAB_TEST_IOD ? "lt.iod_val" :
                mnFormSubtype == SModSysConsts.REP_LAB_TEST_OLE ? "lt.ole_per" :
                mnFormSubtype == SModSysConsts.REP_LAB_TEST_LIN ? "lt.lin_per" : "lt.llc_per");
            moParamsMap.put("sReportName",
                mnFormSubtype == SModSysConsts.REP_LAB_TEST_IOD ? "VALOR DE YODO" :
                mnFormSubtype == SModSysConsts.REP_LAB_TEST_OLE ? "PORCENTAJE DE ÁCIDO OLEICO" :
                mnFormSubtype == SModSysConsts.REP_LAB_TEST_LIN ? "PORCENTAJE DE ÁCIDO LINOLEICO" : "PORCENTAJE DE ÁCIDO LINOLÉNICO");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();

            if (comboBox == moKeyItem) {
                editableIodineValueRank();
            }
            else if (comboBox == moKeyIodineValueRank) {
                itemStateKeyIodineValueRank();
            }
        }
        else if (e.getSource() instanceof JRadioButton && e.getStateChange() == ItemEvent.SELECTED) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            
            if (radioButton == moRadByInputCategory) {
                enableComboBoxes();
            }
            else if (radioButton == moRadByInputClass) {
                enableComboBoxes();
            }
            else if (radioButton == moRadByInputType) {
                enableComboBoxes();
            }
            else if (radioButton == moRadByItem) {
                enableComboBoxes();
            }
        }
    }
}
