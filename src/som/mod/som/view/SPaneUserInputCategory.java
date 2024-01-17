/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SPaneUserInputCategory.java
 *
 * Created on 03/11/2020, 02:53:45 PM
 */

package som.mod.som.view;

import java.util.Vector;
import javax.swing.JPanel;
import sa.lib.gui.SGuiClient;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbUser;
import som.mod.cfg.db.SDbUserInputCategory;

/**
 *
 * @author Isabel Servín
 */
public class SPaneUserInputCategory extends JPanel {
    
    private static final String ALL = "TODAS";
    private static final String NONE = "NINGUNA";
    private static final String INPUT_CATEGORIES_MESSAGE = "Se muestran las siguientes categorías de insumo: ";

    private final SGuiClient miClient;
    private final int mnType;
    private final String msItemAlias;
    private String msCodes;
    private String msNames;
    private String msMessageNames;
    private String msSql;
    
    /** Creates new form SPaneInputCategoryFilter
     * @param client GUI client.
     * @param registryType Registry type, either SModConsts.S_TIC or SModConsts.S_LAB.
     * @param sqlItemAlias SQL alias for item table.
     */
    public SPaneUserInputCategory(SGuiClient client, int registryType, String sqlItemAlias) {
        miClient = client;
        mnType = registryType;
        msItemAlias = sqlItemAlias;
        
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
        jtfOption.setToolTipText("Categorías insumo  usuario");
        jtfOption.setFocusable(false);
        jtfOption.setPreferredSize(new java.awt.Dimension(100, 23));
        add(jtfOption);

        jbOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/som/gui/img/icon_std_look.gif"))); // NOI18N
        jbOption.setToolTipText("Ver categorías insumo usuario");
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
        msSql = "";
        msCodes = "";
        msNames = "";
        msMessageNames = "";
        boolean mbUserHasPrivilege = ((SDbUser) miClient.getSession().getUser()).hasPrivilege(SModSysConsts.CS_RIG_SUP_SCA) ||
                ((SDbUser) miClient.getSession().getUser()).hasPrivilege(SModSysConsts.CS_RIG_SUP_LAB);
        if (mbUserHasPrivilege){
            msCodes = ALL; 
            msNames = ALL; 
        }
        else {
            Vector<SDbUserInputCategory> filterInputCategories = ((SDbUser) miClient.getSession().getUser()).getChildUserInputCategories();
            for (SDbUserInputCategory filterInputCategory : filterInputCategories) {
                int inputCategoryId = filterInputCategory.getPkInputCategoryId();
                msSql += (msSql.isEmpty() ? "(" : "OR ") + msItemAlias + ".fk_inp_ct = " + inputCategoryId + " ";
                msCodes += msCodes.isEmpty() ? filterInputCategory.getXtaInputCategoryCode() : ", " + filterInputCategory.getXtaInputCategoryCode();
                msNames += filterInputCategory.getXtaInputCategoryName() + "\n";
                msMessageNames += msMessageNames.isEmpty() ? filterInputCategory.getXtaInputCategoryName() : ", " + filterInputCategory.getXtaInputCategoryName();
            }
            msCodes = msSql.isEmpty() ? NONE : msCodes;
            msNames = msSql.isEmpty() ? NONE : msNames;
            msMessageNames = msSql.isEmpty() ? NONE : msMessageNames;
            msSql += msSql.isEmpty() ? msItemAlias + ".fk_inp_ct = 0 " : ") ";
        }
        renderOption();
    }

    private void renderOption() {
        jtfOption.setText(msCodes);
        jtfOption.setCaretPosition(0);
    }

    private void actionOption() {
        miClient.showMsgBoxInformation(INPUT_CATEGORIES_MESSAGE + "\n" + msNames);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbOption;
    private javax.swing.JTextField jtfOption;
    // End of variables declaration//GEN-END:variables
    
    public String getSqlFilter() {
        return msSql;
    }
    
    public String getReportMessageFilter() {
        return INPUT_CATEGORIES_MESSAGE + (msSql.isEmpty() ? ALL : msMessageNames);
    }
}
