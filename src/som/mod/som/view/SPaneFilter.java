/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SPaneFilter.java
 *
 * Created on 23/07/2012, 07:33:45 PM
 */

package som.mod.som.view;

import java.awt.Dimension;
import javax.swing.JPanel;
import sa.lib.SLibConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridFilter;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import som.mod.SModConsts;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SPaneFilter extends JPanel implements SGridFilter {

    private SGuiClient miClient;
    private SGridPaneView moPaneView;
    private int mnType;
    private int[] manSelectedKey;
    private SDialogFilter moDialogFilter;

    /** Creates new form SPaneFilter */
    public SPaneFilter(SGridPaneView paneView, int type) {
        miClient = paneView.getClient();
        moPaneView = paneView;
        mnType = type;
        initComponents();
        initComponentsCustom();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtfOption = new javax.swing.JTextField();
        jbOption = new javax.swing.JButton();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfOption.setEditable(false);
        jtfOption.setText("OPTION");
        jtfOption.setToolTipText("Opción");
        jtfOption.setFocusable(false);
        jtfOption.setPreferredSize(new java.awt.Dimension(50, 23));
        add(jtfOption);

        jbOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/som/gui/img/icon_std_filter.gif"))); // NOI18N
        jbOption.setToolTipText("Filtrar opción");
        jbOption.setPreferredSize(new java.awt.Dimension(23, 23));
        jbOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOptionActionPerformed(evt);
            }
        });
        add(jbOption);
    }// </editor-fold>//GEN-END:initComponents

    private void jbOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOptionActionPerformed
        actionOption();
    }//GEN-LAST:event_jbOptionActionPerformed

    private void initComponentsCustom() {
        String path = "";
        SGuiCatalogueSettings settings = miClient.getSession().getModuleByGuiType(mnType, SLibConsts.UNDEFINED).getCatalogueSettings(mnType, SLibConsts.UNDEFINED, null);

        manSelectedKey = null;
        moDialogFilter = new SDialogFilter(miClient, mnType);

        jtfOption.setToolTipText(settings.getCatalogueName());
        jbOption.setToolTipText(SGuiConsts.TXT_BTN_SELECT + " " + settings.getCatalogueName().toLowerCase());

        switch (mnType) {
            case SModConsts.CU_WAH:
                jtfOption.setPreferredSize(new Dimension(50, 23));
                path = "/som/gui/img/icon_std_filter_stk.gif";
                break;
            case SModConsts.CU_DIV:
                jtfOption.setPreferredSize(new Dimension(50, 23));
                path = "/som/gui/img/icon_std_filter_div.gif";
                break;
            case SModConsts.SS_IOG_TP:
                jtfOption.setPreferredSize(new Dimension(50, 23));
                path = "/som/gui/img/icon_std_filter_doc.gif";
                break;
            case SModConsts.SS_ITEM_TP:
                jtfOption.setPreferredSize(new Dimension(50, 23));
                path = "/som/gui/img/icon_std_filter_item_tp.gif";
                break;
            case SModConsts.SU_INP_CT:
            case SModConsts.SU_INP_CL:
            case SModConsts.SU_INP_TP:
                jtfOption.setPreferredSize(new Dimension(50, 23));
                path = "/som/gui/img/icon_std_filter_input.gif";
                break;
            case SModConsts.SU_ITEM:
                jtfOption.setPreferredSize(new Dimension(100, 23));
                path = "/som/gui/img/icon_std_filter_item.gif";
                break;
            case SModConsts.SU_SEAS:
                jtfOption.setPreferredSize(new Dimension(50, 23));
                path = "/som/gui/img/icon_std_filter_cal.gif";
                break;
            case SModConsts.SS_LINK_CFG_ITEMS:
                jtfOption.setPreferredSize(new Dimension(50, 23));
                path = "/som/gui/img/icon_std_filter_item.gif";
                break;
            case SModConsts.SU_LOT:
                jtfOption.setPreferredSize(new Dimension(50, 23));
                path = "/som/gui/img/icon_std_filter.gif";
                break;
            default:
        }

        jbOption.setIcon(new javax.swing.ImageIcon(getClass().getResource(path)));
        renderOption();
    }

    private void renderOption() {
        if (manSelectedKey == null || manSelectedKey[0] == SLibConsts.UNDEFINED) {
            jtfOption.setText("");
        }
        else {
            jtfOption.setText((String) miClient.getSession().readField(mnType, manSelectedKey, SDbRegistry.FIELD_CODE));
            jtfOption.setCaretPosition(0);
        }
    }

    private void actionOption() {
        moDialogFilter.resetDialog();
        moDialogFilter.setSelectedKey(manSelectedKey);
        moDialogFilter.setVisible(true);

        if (moDialogFilter.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
            manSelectedKey = moDialogFilter.getSelectedKey();
            renderOption();
            moPaneView.putFilter(mnType, manSelectedKey);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbOption;
    private javax.swing.JTextField jtfOption;
    // End of variables declaration//GEN-END:variables

    @Override
    public void initFilter(Object value) {
        manSelectedKey = (int[]) value;
        renderOption();
        moPaneView.getFiltersMap().put(mnType, manSelectedKey);
    }
}
