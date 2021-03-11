/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanDialogReport;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.view.SPaneUserInputCategory;

/**
 *
 * @author Isabel Servín
 * 
 */
public final class SDialogRepReceivedFruitHist extends SBeanDialogReport {
    
    private final long YEAR_DAYS = 365;

    private SGuiFieldKeyGroup moFieldKeyGroup;

    /**
     * Creates new form SDialogRepReceivedFruitHist
     * @param client
     * @param formSubtype
     * @param title
     */
    public SDialogRepReceivedFruitHist(SGuiClient client, int formSubtype, String title) {
        setFormSettings(client, SModConsts.SR_ITEM_FRUIT_HIST, formSubtype, title);
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

        reportTypeGroup = new javax.swing.ButtonGroup();
        orderByGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel12 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel15 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jlProducer = new javax.swing.JLabel();
        moKeyProducer = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel8 = new javax.swing.JPanel();
        jlInputSource = new javax.swing.JLabel();
        moKeySource = new sa.lib.gui.bean.SBeanFieldKey();
        moRadProducer = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadSource = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadDetail = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadSummary = new sa.lib.gui.bean.SBeanFieldRadio();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(11, 1, 0, 7));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlDateStart);
        jPanel11.add(moDateDateStart);

        jPanel2.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDateEnd);
        jPanel12.add(moDateDateEnd);

        jPanel2.add(jPanel12);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel15.add(moKeyItem);

        jPanel2.add(jPanel15);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 0));

        jlProducer.setText("Proveedor:");
        jlProducer.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlProducer);

        moKeyProducer.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel7.add(moKeyProducer);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 0));

        jlInputSource.setText("Origen insumo:");
        jlInputSource.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlInputSource);

        moKeySource.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel8.add(moKeySource);

        jPanel2.add(jPanel8);

        orderByGroup.add(moRadProducer);
        moRadProducer.setText("Ordenar por proveedor");
        jPanel2.add(moRadProducer);

        orderByGroup.add(moRadSource);
        moRadSource.setText("Ordenar por origen");
        jPanel2.add(moRadSource);

        reportTypeGroup.add(moRadDetail);
        moRadDetail.setText("Modalidad detallada");
        jPanel2.add(moRadDetail);

        reportTypeGroup.add(moRadSummary);
        moRadSummary.setText("Modalidad resumen");
        jPanel2.add(moRadSummary);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlInputSource;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlProducer;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    private sa.lib.gui.bean.SBeanFieldKey moKeyProducer;
    private sa.lib.gui.bean.SBeanFieldKey moKeySource;
    private sa.lib.gui.bean.SBeanFieldRadio moRadDetail;
    private sa.lib.gui.bean.SBeanFieldRadio moRadProducer;
    private sa.lib.gui.bean.SBeanFieldRadio moRadSource;
    private sa.lib.gui.bean.SBeanFieldRadio moRadSummary;
    private javax.swing.ButtonGroup orderByGroup;
    private javax.swing.ButtonGroup reportTypeGroup;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);

        moFieldKeyGroup = new SGuiFieldKeyGroup(miClient);
        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), true);
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd), true);
        moKeyItem.setKeySettings(miClient, SGuiUtils.getLabelName(jlItem), false);
        moKeyProducer.setKeySettings(miClient, SGuiUtils.getLabelName(jlProducer), false);
        moKeySource.setKeySettings(miClient, SGuiUtils.getLabelName(jlInputSource), false);
        moRadProducer.setBooleanSettings(SGuiUtils.getLabelName(jlProducer), false);
        moRadSource.setBooleanSettings(SGuiUtils.getLabelName(jlInputSource), false);
        moRadDetail.setBooleanSettings(SGuiUtils.getLabelName(jlProducer), false);
        moRadSummary.setBooleanSettings(SGuiUtils.getLabelName(jlProducer), false);

        moFields.addField(moDateDateStart);
        moFields.addField(moDateDateEnd);
        moFields.addField(moKeyItem);
        moFields.addField(moKeyProducer);
        moFields.addField(moKeySource);
        moFields.addField(moRadProducer);
        moFields.addField(moRadSource);
        moFields.addField(moRadDetail);
        moFields.addField(moRadSummary);

        moFields.setFormButton(jbPrint);

        moDateDateStart.setValue(SLibTimeUtils.getBeginOfYear(miClient.getSession().getWorkingDate()));
        moDateDateEnd.setValue(SLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()));
        
        moFieldKeyGroup.initGroup();
        moFieldKeyGroup.populateCatalogues();
        moFieldKeyGroup.resetGroup();
        
        miClient.getSession().populateCatalogue(moKeyItem, SModConsts.SU_ITEM, SModSysConsts.SX_ITEM_TP_FRUIT, null);
        miClient.getSession().populateCatalogue(moKeyProducer, SModConsts.SU_PROD, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeySource, SModConsts.SU_INP_SRC, SLibConsts.UNDEFINED, null);
        
        moRadProducer.setSelected(true);
        moRadDetail.setSelected(true);
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            validation = SGuiUtils.validateDateRangeIgnoreYears(moDateDateStart, moDateDateEnd);
        }
        if (validation.isValid()) {
            long days = SLibTimeUtils.getDaysDiff(moDateDateEnd.getValue(), moDateDateStart.getValue());
            if (SLibTimeUtils.isLeapYear(moDateDateEnd.getValue()) || SLibTimeUtils.isLeapYear(moDateDateStart.getValue())){ 
                if (days > YEAR_DAYS + 1){
                    validation.setValid(false); 
                    validation.setMessage("El período del reporte no debe ser mayor a un año.");
                }
            }
            else {
                if (days > YEAR_DAYS){
                    validation.setValid(false); 
                    validation.setMessage("El período del reporte no debe ser mayor a un año.");
                }
            }
        }
        return validation;
    }

    @Override
    public void createParamsMap() {
        String sqlWhere = "";
        moParamsMap = SPrtUtils.createReportParamsMap(miClient.getSession());

        if (moKeyItem.getSelectedIndex() > 0) {
            sqlWhere += (" AND i.id_item = " + moKeyItem.getValue()[0] + " ");
        }
        
        if (moKeyProducer.getSelectedIndex() > 0) {
            moParamsMap.put("bProducerSearch", true);
            sqlWhere += (" AND p.id_prod = " + moKeyProducer.getValue()[0] + " ");
        }
        else { 
            moParamsMap.put("bProducerSearch", false);
        }
        
        if (moKeySource.getSelectedIndex() > 0) {
            moParamsMap.put("bSourceSearch", true);
            sqlWhere += (" AND s.id_inp_src = " + moKeySource.getValue()[0] + " ");
        }
        else {
            moParamsMap.put("bSourceSearch", false);
        }
        
        SPaneUserInputCategory inputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "i");
        String sqlInputCategories = inputCategory.getSqlFilter();
        if (!sqlInputCategories.isEmpty()) {
            sqlWhere += "AND " + sqlInputCategories;
        }
        
        moParamsMap.put("tActualDateStart", moDateDateStart.getValue());
        moParamsMap.put("tActualDateEnd", moDateDateEnd.getValue());
        moParamsMap.put("tLastDateStart", SLibTimeUtils.addDate(moDateDateStart.getValue(), -1, 0, 0));
        moParamsMap.put("tLastDateEnd", SLibTimeUtils.addDate(moDateDateEnd.getValue(), -1, 0, 0));
        moParamsMap.put("tAncestorDateStart", SLibTimeUtils.addDate(moDateDateStart.getValue(), -2, 0, 0));
        moParamsMap.put("tAncestorDateEnd", SLibTimeUtils.addDate(moDateDateStart.getValue(), -2, 0, 0));
        moParamsMap.put("tThreeYearsDateStart", SLibTimeUtils.addDate(moDateDateStart.getValue(), -3, 0, 0));
        moParamsMap.put("tThreeYearsDateEnd", SLibTimeUtils.addDate(moDateDateStart.getValue(), -3, 0, 0));
        moParamsMap.put("sSqlOrderBy", moRadProducer.getValue() ? "p.name, p.code " : "s.name, s.code ");
        moParamsMap.put("bShowByProd", moRadProducer.getValue());
        moParamsMap.put("bShowDetails", !moRadSummary.getValue());
        moParamsMap.put("sSqlWhere", sqlWhere);
        moParamsMap.put("sMessageFilter", inputCategory.getReportMessageFilter());
    }
}
