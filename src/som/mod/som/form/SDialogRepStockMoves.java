/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import java.awt.event.ItemEvent;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanDialogReport;
import som.gui.SGuiClientSessionCustom;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas
 */
public class SDialogRepStockMoves extends SBeanDialogReport {

    private SGuiFieldKeyGroup moFieldKeyGroupInp;

    /**
     * Creates new form SDialogRepStockMoves
     */
    public SDialogRepStockMoves(SGuiClient client, String title, int formSubtype) {
        setFormSettings(client, SModConsts.SR_STK_MOVE, formSubtype, title);
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

        btnGpoOrderBy = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel12 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel6 = new javax.swing.JPanel();
        jlBranch = new javax.swing.JLabel();
        moKeyBranch = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel9 = new javax.swing.JPanel();
        jlWarehouse = new javax.swing.JLabel();
        moKeyWarehouse = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel30 = new javax.swing.JPanel();
        jlDivision = new javax.swing.JLabel();
        moKeyDivision = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel13 = new javax.swing.JPanel();
        jlIogType = new javax.swing.JLabel();
        moKeyIogType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel10 = new javax.swing.JPanel();
        jlItemType = new javax.swing.JLabel();
        moKeyItemType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel16 = new javax.swing.JPanel();
        jlInputCategory = new javax.swing.JLabel();
        moKeyInputCategory = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel8 = new javax.swing.JPanel();
        jlInputClass = new javax.swing.JLabel();
        moKeyInputClass = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel15 = new javax.swing.JPanel();
        jlInputType = new javax.swing.JLabel();
        moKeyInputType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel14 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(11, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlDateStart);
        jPanel11.add(moDateStart);

        jPanel2.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDateEnd);
        jPanel12.add(moDateEnd);

        jPanel2.add(jPanel12);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBranch.setText("Sucursal: ");
        jlBranch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlBranch);

        moKeyBranch.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(moKeyBranch);

        jPanel2.add(jPanel6);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWarehouse.setText("Almacén:");
        jlWarehouse.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlWarehouse);

        moKeyWarehouse.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel9.add(moKeyWarehouse);

        jPanel2.add(jPanel9);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDivision.setText("División:");
        jlDivision.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jlDivision);

        moKeyDivision.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel30.add(moKeyDivision);

        jPanel2.add(jPanel30);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlIogType.setText("Tipo movimiento:");
        jlIogType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlIogType);

        moKeyIogType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel13.add(moKeyIogType);

        jPanel2.add(jPanel13);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItemType.setText("Tipo ítem:");
        jlItemType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlItemType);

        moKeyItemType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel10.add(moKeyItemType);

        jPanel2.add(jPanel10);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputCategory.setText("Categoría insumo:");
        jlInputCategory.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jlInputCategory);

        moKeyInputCategory.setPreferredSize(new java.awt.Dimension(250, 23));
        moKeyInputCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                moKeyInputCategoryItemStateChanged(evt);
            }
        });
        jPanel16.add(moKeyInputCategory);

        jPanel2.add(jPanel16);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputClass.setText("Clase insumo:");
        jlInputClass.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlInputClass);

        moKeyInputClass.setPreferredSize(new java.awt.Dimension(250, 23));
        moKeyInputClass.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                moKeyInputClassItemStateChanged(evt);
            }
        });
        jPanel8.add(moKeyInputClass);

        jPanel2.add(jPanel8);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInputType.setText("Tipo insumo:");
        jlInputType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlInputType);

        moKeyInputType.setPreferredSize(new java.awt.Dimension(250, 23));
        moKeyInputType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                moKeyInputTypeItemStateChanged(evt);
            }
        });
        jPanel15.add(moKeyInputType);

        jPanel2.add(jPanel15);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel14.add(moKeyItem);

        jPanel2.add(jPanel14);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.ButtonGroup btnGpoOrderBy;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlBranch;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDivision;
    private javax.swing.JLabel jlInputCategory;
    private javax.swing.JLabel jlInputClass;
    private javax.swing.JLabel jlInputType;
    private javax.swing.JLabel jlIogType;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlItemType;
    private javax.swing.JLabel jlWarehouse;
    private sa.lib.gui.bean.SBeanFieldDate moDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateStart;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBranch;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDivision;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputCategory;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputClass;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInputType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyIogType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItemType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyWarehouse;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);
        moFieldKeyGroupInp = new SGuiFieldKeyGroup(miClient);

        moDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), true);
        moDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd), true);
        moKeyBranch.setKeySettings(miClient, SGuiUtils.getLabelName(jlBranch), false);
        moKeyWarehouse.setKeySettings(miClient, SGuiUtils.getLabelName(jlWarehouse), false);
        moKeyDivision.setKeySettings(miClient, SGuiUtils.getLabelName(jlDivision), false);
        moKeyIogType.setKeySettings(miClient, SGuiUtils.getLabelName(jlIogType), false);
        moKeyItemType.setKeySettings(miClient, SGuiUtils.getLabelName(jlItemType), false);
        moKeyInputCategory.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputCategory), false);
        moKeyInputClass.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputClass), false);
        moKeyInputType.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputType), false);
        moKeyItem.setKeySettings(miClient, SGuiUtils.getLabelName(jlItem), false);

        moFields.addField(moDateStart);
        moFields.addField(moDateEnd);
        moFields.addField(moKeyBranch);
        moFields.addField(moKeyWarehouse);
        moFields.addField(moKeyDivision);
        moFields.addField(moKeyIogType);
        moFields.addField(moKeyItemType);
        moFields.addField(moKeyInputCategory);
        moFields.addField(moKeyInputClass);
        moFields.addField(moKeyInputType);
        moFields.addField(moKeyItem);

        moFields.setFormButton(jbPrint);

        moFieldKeyGroupInp.initGroup();
        moFieldKeyGroupInp.addFieldKey(moKeyInputCategory, SModConsts.SU_INP_CT, SLibConsts.UNDEFINED, null);
        moFieldKeyGroupInp.addFieldKey(moKeyInputClass, SModConsts.SU_INP_CL, SLibConsts.UNDEFINED, null);
        moFieldKeyGroupInp.addFieldKey(moKeyInputType, SModConsts.SU_INP_TP, SLibConsts.UNDEFINED, null);
        moFieldKeyGroupInp.populateCatalogues();
        moFieldKeyGroupInp.resetGroup();

        miClient.getSession().populateCatalogue(moKeyBranch, SModConsts.CU_COB, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyWarehouse, SModConsts.CU_WAH, SLibConsts.UNDEFINED, new SGuiParams(((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getCompany().getChildBranches().get(0).getPrimaryKey()));
        miClient.getSession().populateCatalogue(moKeyDivision, SModConsts.CU_DIV, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyIogType, SModConsts.SS_IOG_TP, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyItemType, SModConsts.SS_ITEM_TP, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SU_ITEM, SLibConsts.UNDEFINED, null);

        moDateStart.setValue(SLibTimeUtils.getBeginOfMonth(miClient.getSession().getWorkingDate()));
        moDateEnd.setValue(SLibTimeUtils.getEndOfMonth(miClient.getSession().getWorkingDate()));
        moKeyBranch.setValue(((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getCompany().getChildBranches().get(0).getPrimaryKey());
        
        jlBranch.setEnabled(false);
        moKeyBranch.setEnabled(false);

        jlIogType.setEnabled(mnFormSubtype == SModConsts.SS_IOG_TP);
        moKeyIogType.setEnabled(mnFormSubtype == SModConsts.SS_IOG_TP);
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            validation = SGuiUtils.validateDateRange(moDateStart, moDateEnd);
        }

        return validation;
    }

    @Override
    public void createParamsMap() {
        String sqlWhere = "";
        String sqlOrderBy;
        moParamsMap = SPrtUtils.createReportParamsMap(miClient.getSession());

        sqlWhere += " AND v.fk_wah_co = " + moKeyBranch.getValue()[0];
        sqlWhere += (moKeyBranch.getSelectedIndex() > 0 ? " AND v.fk_wah_cob = " + moKeyBranch.getValue()[1] : "");
        sqlWhere += (moKeyWarehouse.getSelectedIndex() > 0 ? " AND v.fk_wah_wah = " + moKeyWarehouse.getValue()[2] : "");
        sqlWhere += (moKeyDivision.getSelectedIndex() > 0 ? " AND v.fk_div = " + moKeyDivision.getValue()[0] : "");
        sqlWhere += (moKeyIogType.getSelectedIndex() > 0 ? " AND " + SGridUtils.getSqlFilterKey(new String[] { "v.fk_iog_ct", "v.fk_iog_cl", "v.fk_iog_tp", }, moKeyIogType.getValue()) : "");
        sqlWhere += (moKeyItemType.getSelectedIndex() > 0 ? " AND i.fk_item_tp = " + moKeyItemType.getValue()[0] : "");
        sqlWhere += (moKeyInputCategory.getSelectedIndex() > 0 ? " AND i.fk_inp_ct = " + moKeyInputCategory.getValue()[0] : "");
        sqlWhere += (moKeyInputClass.getSelectedIndex() > 0 ? " AND i.fk_inp_cl = " + moKeyInputClass.getValue()[1] : "");
        sqlWhere += (moKeyInputType.getSelectedIndex() > 0 ? " AND i.fk_inp_tp = " + moKeyInputType.getValue()[2] : "");
        sqlWhere += (moKeyItem.getSelectedIndex() > 0 ? " AND i.id_item = " + moKeyItem.getValue()[0] : "");
        sqlOrderBy = "ORDER BY v.fk_wah_cob, v.fk_wah_wah, v.fk_item, v.fk_unit "
                + (mnFormSubtype == SModConsts.SS_IOG_TP ? ", v.fk_iog_ct, v.fk_iog_cl, v.fk_iog_tp " : "") + ", v.dt"
                + (moKeyDivision.getSelectedIndex() > 0 ? ", d.code, d.name, d.id_div " : "") + ", CONCAT(tp.code, '-', v.num) ";

        moParamsMap.put("tDateStart", moDateStart.getValue());
        moParamsMap.put("tDateEnd", moDateEnd.getValue());
        moParamsMap.put("nWorkingYear", SLibTimeUtils.digestYear(moDateStart.getValue())[0]);
        moParamsMap.put("sCompanyBranch", moKeyBranch.getSelectedIndex() > 0 ? moKeyBranch.getSelectedItem() : "(TODAS)");
        moParamsMap.put("sWarehouse", moKeyWarehouse.getSelectedIndex() > 0 ? moKeyWarehouse.getSelectedItem() : "(TODOS)");
        moParamsMap.put("sDivision", moKeyDivision.getSelectedIndex() > 0 ? moKeyDivision.getSelectedItem().getItem() : "TODAS");
        moParamsMap.put("sIogType", moKeyIogType.getSelectedIndex() > 0 ? moKeyIogType.getSelectedItem() : "(TODOS)");
        moParamsMap.put("sItemType", moKeyItemType.getSelectedIndex() > 0 ? moKeyItemType.getSelectedItem() : "(TODOS)");
        moParamsMap.put("sInputCategory", moKeyInputCategory.getSelectedIndex() > 0 ? moKeyInputCategory.getSelectedItem() : "(TODAS)");
        moParamsMap.put("sInputClass", moKeyInputClass.getSelectedIndex() > 0 ? moKeyInputClass.getSelectedItem() : "(TODAS)");
        moParamsMap.put("sInputType", moKeyInputType.getSelectedIndex() > 0 ? moKeyInputType.getSelectedItem() : "(TODOS)");
        moParamsMap.put("bIsIogType", mnFormSubtype == SModConsts.SS_IOG_TP);
        moParamsMap.put("sSqlWhere", sqlWhere);
        moParamsMap.put("sSqlOrderBy", sqlOrderBy);
        moParamsMap.put("sDatabaseCoExtName", ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getExtDatabaseCo().getDbName());
    }
}
