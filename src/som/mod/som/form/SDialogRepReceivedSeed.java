/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import java.awt.event.ItemEvent;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanDialogReport;
import som.gui.SGuiClientSessionCustom;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.som.view.SPaneUserInputCategory;

/**
 *
 * @author Juan Barajas, Sergio Flores, Isabel Servín
 * 
 * Maintenance log:
 * 2018-01-17 (Sergio Flores): development of new report: RM received by category, class or type of input.
 */
public class SDialogRepReceivedSeed extends SBeanDialogReport {

    private SGuiFieldKeyGroup moFieldKeyGroup;

    /**
     * Creates new form SDialogRepReceivedSeed
     * @param client
     * @param formSubtype
     * @param title
     */
    public SDialogRepReceivedSeed(SGuiClient client, int formSubtype, String title) {
        setFormSettings(client, SModConsts.SR_ITEM_REC, formSubtype, title);
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
        bgReportMode = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        moIntYear = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel4 = new javax.swing.JPanel();
        jlReportType = new javax.swing.JLabel();
        moRadByItem = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadByInputType = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadByInputClass = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadByInputCategory = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel11 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel12 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel13 = new javax.swing.JPanel();
        jlRegion = new javax.swing.JLabel();
        moKeyRegion = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel15 = new javax.swing.JPanel();
        jlProducer = new javax.swing.JLabel();
        moKeyProducer = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel14 = new javax.swing.JPanel();
        jlInputCategory = new javax.swing.JLabel();
        moKeyInputCategory = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel8 = new javax.swing.JPanel();
        jlInputClass = new javax.swing.JLabel();
        moKeyInputClass = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel9 = new javax.swing.JPanel();
        jlInputType = new javax.swing.JLabel();
        moKeyInputType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel10 = new javax.swing.JPanel();
        jlTicketOrigin = new javax.swing.JLabel();
        moKeyTicketOrigin = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel16 = new javax.swing.JPanel();
        jlTicketDestination = new javax.swing.JLabel();
        moKeyTicketDestination = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel5 = new javax.swing.JPanel();
        jlTicketScale = new javax.swing.JLabel();
        moKeyTicketScale = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel17 = new javax.swing.JPanel();
        moRadDetail = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadSummary = new sa.lib.gui.bean.SBeanFieldRadio();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(14, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setText("Ejercicio:*");
        jlYear.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel3.add(jlYear);

        moIntYear.setPreferredSize(new java.awt.Dimension(75, 23));
        moIntYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                moIntYearFocusLost(evt);
            }
        });
        jPanel3.add(moIntYear);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReportType.setText("Tipo reporte:");
        jlReportType.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel4.add(jlReportType);

        bgReportType.add(moRadByItem);
        moRadByItem.setText("Por ítem");
        moRadByItem.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(moRadByItem);

        bgReportType.add(moRadByInputType);
        moRadByInputType.setText("Por tipo insumo");
        moRadByInputType.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel4.add(moRadByInputType);

        bgReportType.add(moRadByInputClass);
        moRadByInputClass.setText("Por clase insumo");
        moRadByInputClass.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel4.add(moRadByInputClass);

        bgReportType.add(moRadByInputCategory);
        moRadByInputCategory.setText("Por categoría insumo");
        moRadByInputCategory.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel4.add(moRadByInputCategory);

        jPanel2.add(jPanel4);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel11.add(jlDateStart);
        jPanel11.add(moDateDateStart);

        jPanel2.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel12.add(jlDateEnd);
        jPanel12.add(moDateDateEnd);

        jPanel2.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRegion.setText("Región:");
        jlRegion.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel13.add(jlRegion);

        moKeyRegion.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel13.add(moKeyRegion);

        jPanel2.add(jPanel13);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProducer.setText("Proveedor:");
        jlProducer.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel15.add(jlProducer);

        moKeyProducer.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel15.add(moKeyProducer);

        jPanel2.add(jPanel15);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputCategory.setText("Categoría insumo:");
        jlInputCategory.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel14.add(jlInputCategory);

        moKeyInputCategory.setPreferredSize(new java.awt.Dimension(250, 23));
        moKeyInputCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                moKeyInputCategoryItemStateChanged(evt);
            }
        });
        jPanel14.add(moKeyInputCategory);

        jPanel2.add(jPanel14);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputClass.setText("Clase insumo:");
        jlInputClass.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel8.add(jlInputClass);

        moKeyInputClass.setPreferredSize(new java.awt.Dimension(250, 23));
        moKeyInputClass.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                moKeyInputClassItemStateChanged(evt);
            }
        });
        jPanel8.add(moKeyInputClass);

        jPanel2.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputType.setText("Tipo insumo:");
        jlInputType.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel9.add(jlInputType);

        moKeyInputType.setPreferredSize(new java.awt.Dimension(250, 23));
        moKeyInputType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                moKeyInputTypeItemStateChanged(evt);
            }
        });
        jPanel9.add(moKeyInputType);

        jPanel2.add(jPanel9);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel7.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel7.add(moKeyItem);

        jPanel2.add(jPanel7);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTicketOrigin.setText("Procedencia boleto:");
        jlTicketOrigin.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel10.add(jlTicketOrigin);

        moKeyTicketOrigin.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel10.add(moKeyTicketOrigin);

        jPanel2.add(jPanel10);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTicketDestination.setText("Destino boleto:");
        jlTicketDestination.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel16.add(jlTicketDestination);

        moKeyTicketDestination.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel16.add(moKeyTicketDestination);

        jPanel2.add(jPanel16);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTicketScale.setText("Báscula:");
        jlTicketScale.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel5.add(jlTicketScale);

        moKeyTicketScale.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel5.add(moKeyTicketScale);

        jPanel2.add(jPanel5);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgReportMode.add(moRadDetail);
        moRadDetail.setSelected(true);
        moRadDetail.setText("Modalidad a detalle");
        moRadDetail.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel17.add(moRadDetail);

        bgReportMode.add(moRadSummary);
        moRadSummary.setText("Modalidad resumen");
        moRadSummary.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel17.add(moRadSummary);

        jPanel2.add(jPanel17);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void moIntYearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_moIntYearFocusLost
        setDate(moIntYear.getValue());
    }//GEN-LAST:event_moIntYearFocusLost

    private void moKeyInputCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_moKeyInputCategoryItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedInputCategory();
        }
    }//GEN-LAST:event_moKeyInputCategoryItemStateChanged

    private void moKeyInputClassItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_moKeyInputClassItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedInputClass();
        }
    }//GEN-LAST:event_moKeyInputClassItemStateChanged

    private void moKeyInputTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_moKeyInputTypeItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedInputType();
        }
    }//GEN-LAST:event_moKeyInputTypeItemStateChanged

    private void itemStateChangedInputCategory() {
        SGuiParams params = null;

        if (moKeyInputCategory.getSelectedIndex() > 0) {
            params = new SGuiParams();
            params.getParamsMap().put(SModConsts.SU_INP_CT, moKeyInputCategory.getValue());
        }
        
        miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, params);
    }

    private void itemStateChangedInputClass() {
        if (moKeyInputClass.getSelectedIndex() <= 0) {
            itemStateChangedInputCategory();
        }
        else {
            SGuiParams params = new SGuiParams();
            params.getParamsMap().put(SModConsts.SU_INP_CL, moKeyInputClass.getValue());
            miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, params);
        }
    }

    private void itemStateChangedInputType() {
        if (moKeyInputType.getSelectedIndex() <= 0) {
            itemStateChangedInputClass();
        }
        else {
            SGuiParams params = new SGuiParams();
            params.getParamsMap().put(SModConsts.SU_INP_TP, moKeyInputType.getValue());
            miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, params);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgReportMode;
    private javax.swing.ButtonGroup bgReportType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlInputCategory;
    private javax.swing.JLabel jlInputClass;
    private javax.swing.JLabel jlInputType;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlProducer;
    private javax.swing.JLabel jlRegion;
    private javax.swing.JLabel jlReportType;
    private javax.swing.JLabel jlTicketDestination;
    private javax.swing.JLabel jlTicketOrigin;
    private javax.swing.JLabel jlTicketScale;
    private javax.swing.JLabel jlYear;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    private sa.lib.gui.bean.SBeanFieldInteger moIntYear;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputCategory;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputClass;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    private sa.lib.gui.bean.SBeanFieldKey moKeyProducer;
    private sa.lib.gui.bean.SBeanFieldKey moKeyRegion;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTicketDestination;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTicketOrigin;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTicketScale;
    private sa.lib.gui.bean.SBeanFieldRadio moRadByInputCategory;
    private sa.lib.gui.bean.SBeanFieldRadio moRadByInputClass;
    private sa.lib.gui.bean.SBeanFieldRadio moRadByInputType;
    private sa.lib.gui.bean.SBeanFieldRadio moRadByItem;
    private sa.lib.gui.bean.SBeanFieldRadio moRadDetail;
    private sa.lib.gui.bean.SBeanFieldRadio moRadSummary;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 800, 500);

        moFieldKeyGroup = new SGuiFieldKeyGroup(miClient);

        moIntYear.setIntegerSettings(SGuiUtils.getLabelName(jlYear), SGuiConsts.GUI_TYPE_INT_CAL_YEAR, true);
        moRadByItem.setBooleanSettings(moRadByItem.getText(), true);
        moRadByInputType.setBooleanSettings(moRadByInputType.getText(), false);
        moRadByInputClass.setBooleanSettings(moRadByInputClass.getText(), false);
        moRadByInputCategory.setBooleanSettings(moRadByInputCategory.getText(), false);
        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), true);
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd), true);
        moKeyRegion.setKeySettings(miClient, SGuiUtils.getLabelName(jlRegion), false);
        moKeyProducer.setKeySettings(miClient, SGuiUtils.getLabelName(jlProducer), false);
        moKeyInputCategory.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputCategory), false);
        moKeyInputClass.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputClass), false);
        moKeyInputType.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputType), false);
        moKeyItem.setKeySettings(miClient, SGuiUtils.getLabelName(jlItem), false);
        moKeyTicketOrigin.setKeySettings(miClient, SGuiUtils.getLabelName(jlTicketOrigin), false);
        moKeyTicketDestination.setKeySettings(miClient, SGuiUtils.getLabelName(jlTicketDestination), false);
        moKeyTicketScale.setKeySettings(miClient, SGuiUtils.getLabelName(jlTicketScale), false);
        moRadDetail.setBooleanSettings(moRadDetail.getText(), true);
        moRadSummary.setBooleanSettings(moRadSummary.getText(), false);

        moFields.addField(moIntYear);
        moFields.addField(moRadByItem);
        moFields.addField(moRadByInputType);
        moFields.addField(moRadByInputClass);
        moFields.addField(moRadByInputCategory);
        moFields.addField(moDateDateStart);
        moFields.addField(moDateDateEnd);
        moFields.addField(moKeyRegion);
        moFields.addField(moKeyProducer);
        moFields.addField(moKeyInputCategory);
        moFields.addField(moKeyInputClass);
        moFields.addField(moKeyInputType);
        moFields.addField(moKeyItem);
        moFields.addField(moKeyTicketOrigin);
        moFields.addField(moKeyTicketDestination);
        moFields.addField(moKeyTicketScale);
        moFields.addField(moRadDetail);
        moFields.addField(moRadSummary);

        moFields.setFormButton(jbPrint);

        if (mnFormSubtype == SModConsts.SR_ITEM_REC_PAY) {
            jlYear.setEnabled(true);
            moIntYear.setEnabled(true);
            
            moRadByItem.setEnabled(false);
            moRadByInputType.setEnabled(false);
            moRadByInputClass.setEnabled(false);
            moRadByInputCategory.setEnabled(false);
            
            jlDateStart.setEnabled(false);
            moDateDateStart.setEnabled(false);
            
            jlDateEnd.setEnabled(false);
            moDateDateEnd.setEnabled(false);
            
            moRadDetail.setEnabled(false);
            moRadSummary.setEnabled(false);
        }
        else {
            jlYear.setEnabled(false);
            moIntYear.setEnabled(false);
            
            moRadByItem.setEnabled(true);
            moRadByInputType.setEnabled(true);
            moRadByInputClass.setEnabled(true);
            moRadByInputCategory.setEnabled(true);
            
            jlDateStart.setEnabled(true);
            moDateDateStart.setEnabled(true);
            
            jlDateEnd.setEnabled(true);
            moDateDateEnd.setEnabled(true);
            
            moRadDetail.setEnabled(true);
            moRadSummary.setEnabled(true);
        }
        
        moIntYear.setValue(SLibTimeUtils.digestYear(miClient.getSession().getWorkingDate())[0]);
        moDateDateStart.setValue(SLibTimeUtils.getBeginOfYear(miClient.getSession().getWorkingDate()));
        moDateDateEnd.setValue(SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()));
        
        moRadByItem.setSelected(true);

        moFieldKeyGroup.initGroup();
        moFieldKeyGroup.addFieldKey(moKeyInputCategory, SModConsts.SU_INP_CT, SLibConsts.UNDEFINED, null);
        moFieldKeyGroup.addFieldKey(moKeyInputClass, SModConsts.SU_INP_CL, SLibConsts.UNDEFINED, null);
        moFieldKeyGroup.addFieldKey(moKeyInputType, SModConsts.SU_INP_TP, SLibConsts.UNDEFINED, null);
        moFieldKeyGroup.populateCatalogues();
        moFieldKeyGroup.resetGroup();

        miClient.getSession().populateCatalogue(moKeyRegion, SModConsts.SU_REG, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyProducer, SModConsts.SU_PROD, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTicketOrigin, SModConsts.SU_TIC_ORIG, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTicketDestination, SModConsts.SU_TIC_DEST, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTicketScale, SModConsts.SU_SCA, SLibConsts.UNDEFINED, null);
        
        moRadDetail.setSelected(true);
    }

    private void setDate(int year) {
        moDateDateStart.setValue(SLibTimeUtils.getBeginOfYear(SLibTimeUtils.createDate(year)));
        moDateDateEnd.setValue(SLibTimeUtils.getEndOfYear(SLibTimeUtils.createDate(year)));
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            validation = SGuiUtils.validateDateRange(moDateDateStart, moDateDateEnd);
        }

        return validation;
    }

    @Override
    public void createParamsMap() {
        String sqlWhere = "";
        String sqlOrderBy = "";
        String reportType = "";
        
        moParamsMap = SPrtUtils.createReportParamsMap(miClient.getSession());
        sqlWhere += (moKeyInputCategory.getSelectedIndex() <= 0 ? "" : " AND it.fk_inp_ct = " + moKeyInputCategory.getValue()[0] + " ");
        sqlWhere += (moKeyInputClass.getSelectedIndex() <= 0 ? "" : " AND it.fk_inp_cl = " + moKeyInputClass.getValue()[1] + " ");
        sqlWhere += (moKeyInputType.getSelectedIndex() <= 0 ? "" : " AND it.fk_inp_tp = " + moKeyInputType.getValue()[2] + " ");
        sqlWhere += (moKeyItem.getSelectedIndex() <= 0 ? "" : " AND t.fk_item = " + moKeyItem.getValue()[0] + " ");
        sqlWhere += (moKeyRegion.getSelectedIndex() <= 0 ? "" : " AND t.fk_reg_n = " + moKeyRegion.getValue()[0] + " ");
        sqlWhere += (moKeyProducer.getSelectedIndex() <= 0 ? "" : " AND t.fk_prod = " + moKeyProducer.getValue()[0] + " ");
        sqlWhere += (moKeyTicketOrigin.getSelectedIndex() <= 0 ? "" : "AND t.fk_tic_orig = " + moKeyTicketOrigin.getValue()[0] + " ");
        sqlWhere += (moKeyTicketDestination.getSelectedIndex() <= 0 ? "" : "AND t.fk_tic_dest = " + moKeyTicketDestination.getValue()[0] + " ");
        sqlWhere += (moKeyTicketScale.getSelectedIndex() <= 0 ? "" : "AND t.fk_sca = " + moKeyTicketScale.getValue()[0] + " ");
        
        SPaneUserInputCategory inputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "it");
        String sqlInputCategories = inputCategory.getSqlFilter();
        if (!sqlInputCategories.isEmpty()) {
            sqlWhere += "AND " + sqlInputCategories;
        }
        
        if (moRadByItem.isSelected()) {
            reportType = "ITEM";
            sqlOrderBy += "it.name, t.fk_item, "; //allways sort by item
        }
        else if (moRadByInputType.isSelected()) {
            reportType = "INP_TP";
            sqlOrderBy = "itp.name, it.fk_inp_ct, it.fk_inp_cl, it.fk_inp_tp, ";
        }
        else if (moRadByInputClass.isSelected()) {
            reportType = "INP_CL";
            sqlOrderBy = "icl.name, it.fk_inp_ct, it.fk_inp_cl, ";
        }
        else if (moRadByInputCategory.isSelected()) {
            reportType = "INP_CT";
            sqlOrderBy = "ict.name, it.fk_inp_ct, ";
        }
        
        String db_ext = ((SGuiClientSessionCustom)miClient.getSession().getSessionCustom()).getCompany().getExternalDatabaseCo();
        moParamsMap.put("tDateStart", moDateDateStart.getValue());
        moParamsMap.put("tDateEnd", moDateDateEnd.getValue());
        moParamsMap.put("bShowDetails", !moRadSummary.isSelected());
        moParamsMap.put("sInputCategory", moKeyInputCategory.getSelectedIndex() > 0 ? moKeyInputCategory.getSelectedItem() : "(TODAS)");
        moParamsMap.put("sInputClass", moKeyInputClass.getSelectedIndex() > 0 ? moKeyInputClass.getSelectedItem() : "(TODAS)");
        moParamsMap.put("sInputType", moKeyInputType.getSelectedIndex() > 0 ? moKeyInputType.getSelectedItem() : "(TODOS)");
        moParamsMap.put("sReportType", reportType);
        moParamsMap.put("sSqlWhere", sqlWhere);
        moParamsMap.put("sSqlOrderBy", sqlOrderBy);
        moParamsMap.put("sDatabaseCoExtName", db_ext);
        moParamsMap.put("sMessageFilter", inputCategory.getReportMessageFilter());
        moParamsMap.put("sTicOrig", moKeyTicketOrigin.getSelectedIndex() > 0 ? moKeyTicketOrigin.getSelectedItem() : "TODOS");
        moParamsMap.put("sTicDest", moKeyTicketDestination.getSelectedIndex() > 0 ? moKeyTicketDestination.getSelectedItem() : "TODOS");
        moParamsMap.put("sTicSca", moKeyTicketScale.getSelectedIndex() > 0 ? moKeyTicketScale.getSelectedItem() : "TODAS");
    }
}
