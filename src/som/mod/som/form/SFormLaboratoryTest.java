/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiField;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldDecimal;
import sa.lib.gui.bean.SBeanFieldKey;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.cfg.db.SDbCompany;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbLaboratoryTest;
import som.mod.som.db.SLabConsts;
import som.mod.som.db.SLabUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores
 * 2018-12-11, Sergio Flores: Adición de parámetros de fruta.
 * 2019-01-07, Sergio Flores: Adición de ajuste de rendimiento para parámetros de fruta.
 * 2019-01-09, Sergio Flores: Estimación de porcentaje aceite en pulpa a partir de porcentaje materia seca en fruta.
 */
public class SFormLaboratoryTest extends sa.lib.gui.bean.SBeanForm implements ActionListener, ItemListener, FocusListener {

    private SDbLaboratoryTest moRegistry;
    private SDbItem moParamsItem;
    private SDbCompany moCompany;

    /**
     * Creates new form SFormLaboratoryTest
     */
    public SFormLaboratoryTest(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.S_LAB_TEST, SLibConsts.UNDEFINED, title);
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

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jlImpuritiesPercentage = new javax.swing.JLabel();
        moDecImpuritiesPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel13 = new javax.swing.JPanel();
        jlMoisturePercentage = new javax.swing.JLabel();
        moDecMoisturePercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel8 = new javax.swing.JPanel();
        jlDensity = new javax.swing.JLabel();
        moDecDensity = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel10 = new javax.swing.JPanel();
        jlRefractionIndex = new javax.swing.JLabel();
        moDecRefractionIndex = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel9 = new javax.swing.JPanel();
        jlIodineValue = new javax.swing.JLabel();
        moDecIodineValue = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jlOleicAcidPercentage = new javax.swing.JLabel();
        moDecOleicAcidPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel17 = new javax.swing.JPanel();
        jlLinoleicAcidPercentage = new javax.swing.JLabel();
        moDecLinoleicAcidPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel19 = new javax.swing.JPanel();
        jlLinolenicAcidPercentage = new javax.swing.JLabel();
        moDecLinolenicAcidPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel14 = new javax.swing.JPanel();
        jlErucicAcidPercentage = new javax.swing.JLabel();
        moDecErucicAcidPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel18 = new javax.swing.JPanel();
        jlProteinPercentage = new javax.swing.JLabel();
        moDecProteinPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel15 = new javax.swing.JPanel();
        jlOilContentPercentage = new javax.swing.JLabel();
        moDecOilContentPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel20 = new javax.swing.JPanel();
        jlAcidityPercentage = new javax.swing.JLabel();
        moDecAcidityPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jlFruitClass = new javax.swing.JLabel();
        moKeyFruitClass = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel24 = new javax.swing.JPanel();
        jlFruitRipenessDegree = new javax.swing.JLabel();
        moKeyFruitRipenessDegree = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel21 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jlFruitWeightTotal = new javax.swing.JLabel();
        moDecFruitWeightTotal = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlFruitWeightTotalUnit = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jlFruitWeightPeelPit = new javax.swing.JLabel();
        moDecFruitWeightPeelPit = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlFruitWeightPeelPitUnit = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jlFruitPulpDryMatterPercentage = new javax.swing.JLabel();
        moDecFruitPulpDryMatterPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jbComputeFruitPulpParams = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jlFruitPulpHumidityPercentage = new javax.swing.JLabel();
        moDecFruitPulpHumidityPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel29 = new javax.swing.JPanel();
        jlFruitPulpOilPercentage = new javax.swing.JLabel();
        moDecFruitPulpOilPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel30 = new javax.swing.JPanel();
        jlFruitYieldAdjustmentPercentage = new javax.swing.JLabel();
        moDecFruitYieldAdjustmentPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(6, 2, 0, 5));

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlImpuritiesPercentage.setText("Impurezas (%):");
        jlImpuritiesPercentage.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel12.add(jlImpuritiesPercentage);
        jPanel12.add(moDecImpuritiesPercentage);

        jPanel2.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMoisturePercentage.setText("Humedad (%):");
        jlMoisturePercentage.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel13.add(jlMoisturePercentage);
        jPanel13.add(moDecMoisturePercentage);

        jPanel2.add(jPanel13);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDensity.setText("Densidad:");
        jlDensity.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlDensity);
        jPanel8.add(moDecDensity);

        jPanel2.add(jPanel8);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRefractionIndex.setText("Índice refracción (IR):");
        jlRefractionIndex.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel10.add(jlRefractionIndex);
        jPanel10.add(moDecRefractionIndex);

        jPanel2.add(jPanel10);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlIodineValue.setText("Valor yodo (VI):");
        jlIodineValue.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlIodineValue);
        jPanel9.add(moDecIodineValue);

        jPanel2.add(jPanel9);

        jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel5, java.awt.BorderLayout.WEST);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOleicAcidPercentage.setText("Ácido oleico (%):");
        jlOleicAcidPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jlOleicAcidPercentage);
        jPanel16.add(moDecOleicAcidPercentage);

        jPanel4.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLinoleicAcidPercentage.setText("Ácido linoleico (%):");
        jlLinoleicAcidPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel17.add(jlLinoleicAcidPercentage);
        jPanel17.add(moDecLinoleicAcidPercentage);

        jPanel4.add(jPanel17);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLinolenicAcidPercentage.setText("Ácido linolénico (%):");
        jlLinolenicAcidPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel19.add(jlLinolenicAcidPercentage);
        jPanel19.add(moDecLinolenicAcidPercentage);

        jPanel4.add(jPanel19);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlErucicAcidPercentage.setText("Ácido erúcico (%):");
        jlErucicAcidPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlErucicAcidPercentage);
        jPanel14.add(moDecErucicAcidPercentage);

        jPanel4.add(jPanel14);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProteinPercentage.setText("Proteína (%):");
        jlProteinPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel18.add(jlProteinPercentage);
        jPanel18.add(moDecProteinPercentage);

        jPanel4.add(jPanel18);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOilContentPercentage.setText("Aceite (%):");
        jlOilContentPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlOilContentPercentage);
        jPanel15.add(moDecOilContentPercentage);

        jPanel4.add(jPanel15);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAcidityPercentage.setText("Acidez (%):");
        jlAcidityPercentage.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel20.add(jlAcidityPercentage);
        jPanel20.add(moDecAcidityPercentage);

        jPanel4.add(jPanel20);

        jPanel6.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros fruta:"));
        jPanel7.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel22.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFruitClass.setText("Clase fruta:*");
        jlFruitClass.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel23.add(jlFruitClass);

        moKeyFruitClass.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel23.add(moKeyFruitClass);

        jPanel22.add(jPanel23);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFruitRipenessDegree.setText("Grado madurez:*");
        jlFruitRipenessDegree.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlFruitRipenessDegree);

        moKeyFruitRipenessDegree.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel24.add(moKeyFruitRipenessDegree);

        jPanel22.add(jPanel24);

        jPanel11.add(jPanel22, java.awt.BorderLayout.NORTH);

        jPanel7.add(jPanel11, java.awt.BorderLayout.WEST);

        jPanel21.setLayout(new java.awt.BorderLayout());

        jPanel25.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFruitWeightTotal.setText("Peso fruta:");
        jlFruitWeightTotal.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel26.add(jlFruitWeightTotal);
        jPanel26.add(moDecFruitWeightTotal);

        jlFruitWeightTotalUnit.setText("g");
        jlFruitWeightTotalUnit.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel26.add(jlFruitWeightTotalUnit);

        jPanel25.add(jPanel26);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFruitWeightPeelPit.setText("Peso cáscara + hueso:");
        jlFruitWeightPeelPit.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel27.add(jlFruitWeightPeelPit);
        jPanel27.add(moDecFruitWeightPeelPit);

        jlFruitWeightPeelPitUnit.setText("g");
        jlFruitWeightPeelPitUnit.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel27.add(jlFruitWeightPeelPitUnit);

        jPanel25.add(jPanel27);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFruitPulpDryMatterPercentage.setText("Pulpa: materia seca (%):");
        jlFruitPulpDryMatterPercentage.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel31.add(jlFruitPulpDryMatterPercentage);
        jPanel31.add(moDecFruitPulpDryMatterPercentage);

        jbComputeFruitPulpParams.setText("Calcular");
        jbComputeFruitPulpParams.setToolTipText("Calcular % humedad y % aceite en pulpa");
        jbComputeFruitPulpParams.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel31.add(jbComputeFruitPulpParams);

        jPanel25.add(jPanel31);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFruitPulpHumidityPercentage.setText("Pulpa: humedad (%):");
        jlFruitPulpHumidityPercentage.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel28.add(jlFruitPulpHumidityPercentage);
        jPanel28.add(moDecFruitPulpHumidityPercentage);

        jPanel25.add(jPanel28);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFruitPulpOilPercentage.setText("Pulpa: aceite(%):");
        jlFruitPulpOilPercentage.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel29.add(jlFruitPulpOilPercentage);
        jPanel29.add(moDecFruitPulpOilPercentage);

        jPanel25.add(jPanel29);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFruitYieldAdjustmentPercentage.setText("Ajuste rendimiento (%):");
        jlFruitYieldAdjustmentPercentage.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel30.add(jlFruitYieldAdjustmentPercentage);
        jPanel30.add(moDecFruitYieldAdjustmentPercentage);

        jPanel25.add(jPanel30);

        jPanel21.add(jPanel25, java.awt.BorderLayout.NORTH);

        jPanel7.add(jPanel21, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel7, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbComputeFruitPulpParams;
    private javax.swing.JLabel jlAcidityPercentage;
    private javax.swing.JLabel jlDensity;
    private javax.swing.JLabel jlErucicAcidPercentage;
    private javax.swing.JLabel jlFruitClass;
    private javax.swing.JLabel jlFruitPulpDryMatterPercentage;
    private javax.swing.JLabel jlFruitPulpHumidityPercentage;
    private javax.swing.JLabel jlFruitPulpOilPercentage;
    private javax.swing.JLabel jlFruitRipenessDegree;
    private javax.swing.JLabel jlFruitWeightPeelPit;
    private javax.swing.JLabel jlFruitWeightPeelPitUnit;
    private javax.swing.JLabel jlFruitWeightTotal;
    private javax.swing.JLabel jlFruitWeightTotalUnit;
    private javax.swing.JLabel jlFruitYieldAdjustmentPercentage;
    private javax.swing.JLabel jlImpuritiesPercentage;
    private javax.swing.JLabel jlIodineValue;
    private javax.swing.JLabel jlLinoleicAcidPercentage;
    private javax.swing.JLabel jlLinolenicAcidPercentage;
    private javax.swing.JLabel jlMoisturePercentage;
    private javax.swing.JLabel jlOilContentPercentage;
    private javax.swing.JLabel jlOleicAcidPercentage;
    private javax.swing.JLabel jlProteinPercentage;
    private javax.swing.JLabel jlRefractionIndex;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecAcidityPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecDensity;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecErucicAcidPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecFruitPulpDryMatterPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecFruitPulpHumidityPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecFruitPulpOilPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecFruitWeightPeelPit;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecFruitWeightTotal;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecFruitYieldAdjustmentPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecImpuritiesPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecIodineValue;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecLinoleicAcidPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecLinolenicAcidPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecMoisturePercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecOilContentPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecOleicAcidPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecProteinPercentage;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecRefractionIndex;
    private sa.lib.gui.bean.SBeanFieldKey moKeyFruitClass;
    private sa.lib.gui.bean.SBeanFieldKey moKeyFruitRipenessDegree;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 800, 500);
        
        moCompany = ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getCompany();

        moDecImpuritiesPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlImpuritiesPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecImpuritiesPercentage.setDecimalFormat(SLibUtils.DecimalFormatPercentage2D);
        moDecMoisturePercentage.setDecimalSettings(SGuiUtils.getLabelName(jlMoisturePercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecMoisturePercentage.setDecimalFormat(SLibUtils.DecimalFormatPercentage2D);
        moDecDensity.setDecimalSettings(SGuiUtils.getLabelName(jlDensity), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDecRefractionIndex.setDecimalSettings(SGuiUtils.getLabelName(jlRefractionIndex), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDecIodineValue.setDecimalSettings(SGuiUtils.getLabelName(jlIodineValue), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDecOleicAcidPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlOleicAcidPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecLinoleicAcidPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlLinoleicAcidPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecLinolenicAcidPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlLinolenicAcidPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecErucicAcidPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlErucicAcidPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecProteinPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlProteinPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecOilContentPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlOilContentPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecAcidityPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlAcidityPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moKeyFruitClass.setKeySettings(miClient, SGuiUtils.getLabelName(jlFruitClass), true);
        moKeyFruitRipenessDegree.setKeySettings(miClient, SGuiUtils.getLabelName(jlFruitRipenessDegree), true);
        moDecFruitWeightTotal.setDecimalSettings(SGuiUtils.getLabelName(jlFruitWeightTotal), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDecFruitWeightPeelPit.setDecimalSettings(SGuiUtils.getLabelName(jlFruitWeightPeelPit), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDecFruitPulpDryMatterPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlFruitPulpDryMatterPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecFruitPulpDryMatterPercentage.setMaxDouble(1);
        moDecFruitPulpHumidityPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlFruitPulpHumidityPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecFruitPulpHumidityPercentage.setMaxDouble(1);
        moDecFruitPulpOilPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlFruitPulpOilPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);
        moDecFruitPulpOilPercentage.setMaxDouble(1);
        moDecFruitYieldAdjustmentPercentage.setDecimalSettings(SGuiUtils.getLabelName(jlFruitYieldAdjustmentPercentage), SGuiConsts.GUI_TYPE_DEC_PER_DISC, false);

        moFields.addField(moDecImpuritiesPercentage);
        moFields.addField(moDecMoisturePercentage);
        moFields.addField(moDecDensity);
        moFields.addField(moDecRefractionIndex);
        moFields.addField(moDecIodineValue);
        moFields.addField(moDecOleicAcidPercentage);
        moFields.addField(moDecLinoleicAcidPercentage);
        moFields.addField(moDecLinolenicAcidPercentage);
        moFields.addField(moDecErucicAcidPercentage);
        moFields.addField(moDecProteinPercentage);
        moFields.addField(moDecOilContentPercentage);
        moFields.addField(moDecAcidityPercentage);
        moFields.addField(moKeyFruitClass);
        moFields.addField(moKeyFruitRipenessDegree);
        moFields.addField(moDecFruitWeightTotal);
        moFields.addField(moDecFruitWeightPeelPit);
        moFields.addField(moDecFruitPulpDryMatterPercentage);
        moFields.addField(moDecFruitPulpHumidityPercentage);
        moFields.addField(moDecFruitPulpOilPercentage);
        moFields.addField(moDecFruitYieldAdjustmentPercentage);

        moFields.setFormButton(jbSave);
    }

    private void computePercentageRound(SBeanFieldDecimal fieldDecimal) {
        fieldDecimal.setValue(SLibUtils.round(fieldDecimal.getValue(), SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits()));
    }
    
    /**
     * Compute fruit related parameters: moisture pct. and oil content pct.
     * PLEASE NOTICE THAT, to prevent from duplicating calculations, 
     * a public method of class SDbLaboratoryTest is invoked on a disposable clone of the main register of this form.
     */
    private void computeFruitParams() {
        if (moParamsItem.isFruit()) {
            try {
                SDbLaboratoryTest test = moRegistry.clone();

                // load fruit class into registry for computation:
                test.setFruitClass(moKeyFruitClass.getSelectedIndex() <= 0 ? SDbLaboratoryTest.RESET_FRUIT_PARAMS : moCompany.getFruitOption(SDbCompany.FRUIT_CLASS, moKeyFruitClass.getValue()[0]));

                // load fruit params into registry for computation:
                test.setFruitWeightTotal(moDecFruitWeightTotal.getValue());
                test.setFruitWeightPeelPit(moDecFruitWeightPeelPit.getValue());
                test.setFruitPulpDryMatterPercentage(moDecFruitPulpDryMatterPercentage.getValue()); // no really required for computation
                test.setFruitPulpHumidityPercentage(moDecFruitPulpHumidityPercentage.getValue());
                test.setFruitPulpOilPercentage(moDecFruitPulpOilPercentage.getValue());
                test.computeFruitParams(); // prevents from duplicating calculations already defined in class SDbLaboratoryTest

                // retrieve computed params from registry:
                moDecMoisturePercentage.setValue(test.getMoisturePercentage());
                moDecOilContentPercentage.setValue(test.getOilContentPercentage());
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }
    
    private void setEnableRequired() {
        moDecImpuritiesPercentage.setEditable(moParamsItem.isImpuritiesPercentage());
        moDecMoisturePercentage.setEditable(moParamsItem.isMoisturePercentage() && !moParamsItem.isFruit());
        moDecDensity.setEditable(moParamsItem.isDensity());
        moDecRefractionIndex.setEditable(moParamsItem.isRefractionIndex());
        moDecIodineValue.setEditable(moParamsItem.isIodineValue());
        moDecOleicAcidPercentage.setEditable(moParamsItem.isOleicAcidPercentage());
        moDecLinoleicAcidPercentage.setEditable(moParamsItem.isLinoleicAcidPercentage());
        moDecLinolenicAcidPercentage.setEditable(moParamsItem.isLinolenicAcidPercentage());
        moDecErucicAcidPercentage.setEditable(moParamsItem.isErucicAcidPercentage());
        moDecProteinPercentage.setEditable(moParamsItem.isProteinPercentage());
        moDecOilContentPercentage.setEditable(moParamsItem.isOilContentPercentage() && !moParamsItem.isFruit());
        moDecAcidityPercentage.setEditable(moParamsItem.isAcidityPercentage());
        moKeyFruitClass.setEditable(moParamsItem.isFruit());
        moKeyFruitRipenessDegree.setEditable(moParamsItem.isFruit());
        moDecFruitWeightTotal.setEditable(moParamsItem.isFruit());
        moDecFruitWeightPeelPit.setEditable(moParamsItem.isFruit());
        moDecFruitPulpDryMatterPercentage.setEditable(moParamsItem.isFruit());
        moDecFruitPulpHumidityPercentage.setEditable(moParamsItem.isFruit());
        moDecFruitPulpOilPercentage.setEditable(moParamsItem.isFruit());
        moDecFruitYieldAdjustmentPercentage.setEditable(false); // allways is read-only
        jbComputeFruitPulpParams.setEnabled(moParamsItem.isFruit());
    }

    /**
     * Note that by now works only for avocado! No other fruit considered!
     * @param preserveOldOilPct Preserve old oil percentage when an exception occurs.
     */
    private void actionPerformedComputeFruitPulpParams() {
        double oilPct = 0;
        
        try {
            SGuiValidation validation = moDecFruitPulpDryMatterPercentage.validateField();
            
            if (SGuiUtils.computeValidation(miClient, validation)) {
                if (moDecFruitPulpDryMatterPercentage.getValue() == 0) {
                    moDecFruitPulpHumidityPercentage.setValue(1.0);
                }
                else {
                    moDecFruitPulpHumidityPercentage.setValue(1.0 - moDecFruitPulpDryMatterPercentage.getValue());
                    
                    oilPct = SLabUtils.estimateFruitOilPct(SLabConsts.FRUIT_AVOCADO, moDecFruitPulpDryMatterPercentage.getValue());
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            moDecFruitPulpOilPercentage.setValue(oilPct);
            
            computeFruitParams();
        }
    }

    @Override
    public void setValue(int type, Object value) {
        moParamsItem = new SDbItem();
        switch (type) {
            case SGuiConsts.PARAM_ITEM:
                try {
                    moParamsItem = (SDbItem) value;
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
                break;
            default:
        }
    }

    @Override
    public void addAllListeners() {
        jbComputeFruitPulpParams.addActionListener(this);
        moKeyFruitClass.addItemListener(this);
        moDecImpuritiesPercentage.addFocusListener(this);
        moDecMoisturePercentage.addFocusListener(this);
        moDecFruitWeightTotal.addFocusListener(this);
        moDecFruitWeightPeelPit.addFocusListener(this);
        moDecFruitPulpDryMatterPercentage.addFocusListener(this);
        moDecFruitPulpHumidityPercentage.addFocusListener(this);
        moDecFruitPulpOilPercentage.addFocusListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbComputeFruitPulpParams.removeActionListener(this);
        moKeyFruitClass.removeItemListener(this);
        moDecImpuritiesPercentage.removeFocusListener(this);
        moDecMoisturePercentage.removeFocusListener(this);
        moDecFruitWeightTotal.removeFocusListener(this);
        moDecFruitWeightPeelPit.removeFocusListener(this);
        moDecFruitPulpDryMatterPercentage.removeFocusListener(this);
        moDecFruitPulpHumidityPercentage.removeFocusListener(this);
        moDecFruitPulpOilPercentage.removeFocusListener(this);
    }

    @Override
    public void reloadCatalogues() {
        moCompany.populateFruitOptions(SDbCompany.FRUIT_CLASS, moKeyFruitClass);
        moCompany.populateFruitOptions(SDbCompany.FRUIT_RIPENESS_DEGREE, moKeyFruitRipenessDegree);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbLaboratoryTest) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
            
            moRegistry.setFruitYieldAdjustmentPercentage(moParamsItem.getFruitYieldAdjustmentPercentage());
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }
        
        moDecImpuritiesPercentage.setValue(moRegistry.getImpuritiesPercentage());
        moDecMoisturePercentage.setValue(moRegistry.getMoisturePercentage());
        moDecDensity.setValue(moRegistry.getDensity());
        moDecRefractionIndex.setValue(moRegistry.getRefractionIndex());
        moDecIodineValue.setValue(moRegistry.getIodineValue());
        moDecOleicAcidPercentage.setValue(moRegistry.getOleicAcidPercentage());
        moDecLinoleicAcidPercentage.setValue(moRegistry.getLinoleicAcidPercentage());
        moDecLinolenicAcidPercentage.setValue(moRegistry.getLinolenicAcidPercentage());
        moDecErucicAcidPercentage.setValue(moRegistry.getErucicAcidPercentage());
        moDecProteinPercentage.setValue(moRegistry.getProteinPercentage());
        moDecOilContentPercentage.setValue(moRegistry.getOilContentPercentage());
        moDecAcidityPercentage.setValue(moRegistry.getAcidityPercentage());
        moKeyFruitClass.setValue(new int[] { moCompany.getFruitOptionId(SDbCompany.FRUIT_CLASS, moRegistry.getFruitClass()) });
        moKeyFruitRipenessDegree.setValue(new int[] { moCompany.getFruitOptionId(SDbCompany.FRUIT_RIPENESS_DEGREE, moRegistry.getFruitRipenessDegree()) });
        moDecFruitWeightTotal.setValue(moRegistry.getFruitWeightTotal());
        moDecFruitWeightPeelPit.setValue(moRegistry.getFruitWeightPeelPit());
        moDecFruitPulpDryMatterPercentage.setValue(moRegistry.getFruitPulpDryMatterPercentage());
        moDecFruitPulpHumidityPercentage.setValue(moRegistry.getFruitPulpHumidityPercentage());
        moDecFruitPulpOilPercentage.setValue(moRegistry.getFruitPulpOilPercentage());
        moDecFruitYieldAdjustmentPercentage.setValue(moRegistry.getFruitYieldAdjustmentPercentage());

        setFormEditable(true);
        setEnableRequired();

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbLaboratoryTest registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
        }

        registry.setImpuritiesPercentage(moDecImpuritiesPercentage.getValue());
        registry.setMoisturePercentage(moDecMoisturePercentage.getValue());
        registry.setDensity(moDecDensity.getValue());
        registry.setRefractionIndex(moDecRefractionIndex.getValue());
        registry.setIodineValue(moDecIodineValue.getValue());
        registry.setOleicAcidPercentage(moDecOleicAcidPercentage.getValue());
        registry.setLinoleicAcidPercentage(moDecLinoleicAcidPercentage.getValue());
        registry.setLinolenicAcidPercentage(moDecLinolenicAcidPercentage.getValue());
        registry.setErucicAcidPercentage(moDecErucicAcidPercentage.getValue());
        registry.setProteinPercentage(moDecProteinPercentage.getValue());
        registry.setOilContentPercentage(moDecOilContentPercentage.getValue());
        registry.setAcidityPercentage(moDecAcidityPercentage.getValue());
        registry.setFruitClass(moKeyFruitClass.getSelectedIndex() <= 0 ? "" : moCompany.getFruitOption(SDbCompany.FRUIT_CLASS, moKeyFruitClass.getValue()[0]));
        registry.setFruitRipenessDegree(moKeyFruitRipenessDegree.getSelectedIndex() <= 0 ? "" : moCompany.getFruitOption(SDbCompany.FRUIT_RIPENESS_DEGREE, moKeyFruitRipenessDegree.getValue()[0]));
        registry.setFruitWeightTotal(moDecFruitWeightTotal.getValue());
        registry.setFruitWeightPeelPit(moDecFruitWeightPeelPit.getValue());
        registry.setFruitPulpDryMatterPercentage(moDecFruitPulpDryMatterPercentage.getValue());
        registry.setFruitPulpHumidityPercentage(moDecFruitPulpHumidityPercentage.getValue());
        registry.setFruitPulpOilPercentage(moDecFruitPulpOilPercentage.getValue());
        registry.setFruitYieldAdjustmentPercentage(moDecFruitYieldAdjustmentPercentage.getValue());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        boolean testsEmpty = true;
        SGuiValidation validation = moFields.validateFields();

        for (SGuiField field : moFields.getFields()) {
            if (field instanceof SBeanFieldDecimal && field.isEditable() && (Double) field.getValue() > 0) {
                testsEmpty = false;
                break;
            }
            else if (field instanceof SBeanFieldKey && field.isEnabled() && ((SBeanFieldKey) field).getSelectedIndex() > 0) {
                testsEmpty = false;
                break;
            }
        }

        if (validation.isValid()) {
            if (testsEmpty && miClient.showMsgBoxConfirm("No se ha capturado ningún resultado de laboratorio.\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                validation.setMessage("Se debe capturar al menos un resultado de laboratorio.");
            }
            else if (moParamsItem.isFruit()) {
                if (SLibUtils.round(moDecFruitPulpDryMatterPercentage.getValue() + moDecFruitPulpHumidityPercentage.getValue(), SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits()) != 1) {
                    validation.setMessage("La suma de '" + SGuiUtils.getLabelName(jlFruitPulpDryMatterPercentage) + "' y '" + SGuiUtils.getLabelName(jlFruitPulpHumidityPercentage) + "' debe ser igual a 100%.");
                    validation.setComponent(moDecFruitPulpDryMatterPercentage);
                }
                else if (moDecFruitPulpOilPercentage.getValue() > moDecFruitPulpDryMatterPercentage.getValue()) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + SGuiUtils.getLabelName(jlFruitPulpOilPercentage) + "'" + SGuiConsts.ERR_MSG_FIELD_VAL_LESS_EQUAL + "'" + SGuiUtils.getLabelName(jlFruitPulpDryMatterPercentage) + "'.");
                    validation.setComponent(moDecFruitPulpOilPercentage);
                }
            }
        }
        
        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbComputeFruitPulpParams) {
                actionPerformedComputeFruitPulpParams();
                moDecFruitPulpDryMatterPercentage.requestFocusInWindow();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof SBeanFieldKey) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SBeanFieldKey field = (SBeanFieldKey) e.getSource();
                
                if (field == moKeyFruitClass) {
                    computeFruitParams();
                }
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof javax.swing.JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == moDecImpuritiesPercentage.getComponent() || 
                    textField == moDecMoisturePercentage.getComponent()) {
                computePercentageRound((SBeanFieldDecimal) textField);
            }
            else if (textField == moDecFruitPulpDryMatterPercentage.getComponent()) {
                actionPerformedComputeFruitPulpParams(); // invokes private method computeFruitParams() as well
            }
            else if (textField == moDecFruitWeightTotal.getComponent() || 
                    textField == moDecFruitWeightPeelPit.getComponent() || 
                    textField == moDecFruitPulpHumidityPercentage.getComponent() || 
                    textField == moDecFruitPulpOilPercentage.getComponent()) {
                computeFruitParams();
            }
        }
    }
}
