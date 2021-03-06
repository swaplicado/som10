/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanDialogReport;
import som.gui.SGuiClientSessionCustom;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SDialogRepStockComparate extends SBeanDialogReport {

    /**
     * Creates new form SDialogRepStockDay
     */
    public SDialogRepStockComparate(SGuiClient client, String title) {
        setFormSettings(client, SModConsts.SR_STK_COMP, SLibConsts.UNDEFINED, title);

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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel13 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateEnd = new sa.lib.gui.bean.SBeanFieldDate();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel12.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDateStart);
        jPanel12.add(moDateStart);

        jPanel2.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlDateEnd);
        jPanel13.add(moDateEnd);

        jPanel2.add(jPanel13);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private sa.lib.gui.bean.SBeanFieldDate moDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateStart;
    // End of variables declaration//GEN-END:variables

    /*
    * Private methods
    */

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 400, 250);

        moDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart.getText()), true);
        moDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd.getText()), true);

        moFields.addField(moDateStart);
        moFields.addField(moDateEnd);

        moFields.setFormButton(jbPrint);

        moDateStart.setValue(SLibTimeUtils.getBeginOfMonth(miClient.getSession().getWorkingDate()));
        moDateEnd.setValue(SLibTimeUtils.getEndOfMonth(miClient.getSession().getWorkingDate()));

        reloadCatalogues();
    }

    private void reloadCatalogues() {

    }

    /*
    * Public methods
    */

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
        moParamsMap = SPrtUtils.createReportParamsMap(miClient.getSession());

        moParamsMap.put("sCompanyBranch", ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getCompany().getChildBranches().get(0).getName());
        moParamsMap.put("nPkYearId", SLibTimeUtils.digestYear(moDateStart.getValue())[0]);
        moParamsMap.put("tDateStart", moDateStart.getValue());
        moParamsMap.put("tDateEnd", moDateEnd.getValue());
        moParamsMap.put("nSql_Out_Mfg_Rm_Asd_Ct", SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[0]); // 2
        moParamsMap.put("nSql_Out_Mfg_Rm_Asd_Cl", SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[1]); // 5
        moParamsMap.put("nSql_Out_Mfg_Rm_Asd_Tp", SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[2]); // 1
        moParamsMap.put("nSql_In_Mfg_Rm_Ret_Ct", SModSysConsts.SS_IOG_TP_IN_MFG_RM_RET[0]); // 1
        moParamsMap.put("nSql_In_Mfg_Rm_Ret_Cl", SModSysConsts.SS_IOG_TP_IN_MFG_RM_RET[1]); // 5
        moParamsMap.put("nSql_In_Mfg_Rm_Ret_Tp", SModSysConsts.SS_IOG_TP_IN_MFG_RM_RET[2]); // 2
        moParamsMap.put("nSql_In_Mfg_Fg_Asd_Ct", SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0]); // 1
        moParamsMap.put("nSql_In_Mfg_Fg_Asd_Cl", SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1]); // 5
        moParamsMap.put("nSql_In_Mfg_Fg_Asd_Tp", SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2]); // 5
        moParamsMap.put("nSql_Out_Mfg_Fg_Ret_Ct", SModSysConsts.SS_IOG_TP_OUT_MFG_FG_RET[0]); // 2
        moParamsMap.put("nSql_Out_Mfg_Fg_Ret_Cl", SModSysConsts.SS_IOG_TP_OUT_MFG_FG_RET[1]); // 5
        moParamsMap.put("nSql_Out_Mfg_Fg_Ret_Tp", SModSysConsts.SS_IOG_TP_OUT_MFG_FG_RET[2]); // 6
        moParamsMap.put("nSql_Ss_Item_Tp_Rm", SModSysConsts.SS_ITEM_TP_RM); // 1
        moParamsMap.put("nSql_Ss_Item_Tp_Fg", SModSysConsts.SS_ITEM_TP_FG); // 3
        moParamsMap.put("nSql_Ss_Item_Tp_Bp", SModSysConsts.SS_ITEM_TP_BP); // 4
    }
}
