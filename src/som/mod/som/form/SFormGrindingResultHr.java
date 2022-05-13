/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;
import som.mod.SModConsts;
import som.mod.som.data.SCaptureConfiguration;
import som.mod.som.db.SDbGrindingResult;
import som.mod.som.db.SDbItem;

/**
 *
 * @author Edwin Carmona
 */
public class SFormGrindingResultHr extends SBeanForm implements ActionListener {

    private SDbGrindingResult moRegistry;
    private SGridPaneForm moGridTableRows;

    /**
     * Creates new form SFormResult
     * @param client
     * @param title
     */
    public SFormGrindingResultHr(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.SU_GRINDING_RESULTS, SLibConsts.UNDEFINED, title);
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
        jpHeader = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moItemName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlCaptureDate = new javax.swing.JLabel();
        moCaptureDate = new sa.lib.gui.bean.SBeanFieldDate();
        jpResults = new javax.swing.JPanel();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jpHeader.setBorder(javax.swing.BorderFactory.createTitledBorder("Encabezado"));
        jpHeader.setLayout(new java.awt.GridLayout(1, 2, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel3.add(jlItem);

        moItemName.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel3.add(moItemName);

        jpHeader.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCaptureDate.setText("Fecha de captura:");
        jlCaptureDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlCaptureDate);
        jPanel4.add(moCaptureDate);

        jpHeader.add(jPanel4);

        jPanel1.add(jpHeader, java.awt.BorderLayout.NORTH);

        jpResults.setBorder(javax.swing.BorderFactory.createTitledBorder("Captura de resultados"));
        jpResults.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jpResults, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel jlCaptureDate;
    private javax.swing.JLabel jlItem;
    private javax.swing.JPanel jpHeader;
    private javax.swing.JPanel jpResults;
    private sa.lib.gui.bean.SBeanFieldDate moCaptureDate;
    private sa.lib.gui.bean.SBeanFieldText moItemName;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 640);
        
        moGridTableRows = new SGridPaneForm(miClient, SModConsts.SU_GRINDING_RESULTS, SLibConsts.UNDEFINED, "Captura de resultados") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                SGridColumnForm[] columns = new SGridColumnForm[14];
                
                /**
                 * ID, Parámetro
                 */
                columns[0] = new SGridColumnForm(SGridConsts.COL_TYPE_INT_1B, "Orden");
                columns[1] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Parámetro");
                
                /**
                 * Valores
                 */
                columns[2] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "08:00");
                columns[2].setEditable(true);
                columns[3] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "10:00");
                columns[3].setEditable(true);
                columns[4] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "12:00");
                columns[4].setEditable(true);
                columns[5] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "14:00");
                columns[5].setEditable(true);
                columns[6] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "16:00");
                columns[6].setEditable(true);
                columns[7] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "18:00");
                columns[7].setEditable(true);
                columns[8] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "20:00");
                columns[8].setEditable(true);
                columns[9] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "22:00");
                columns[9].setEditable(true);
                columns[10] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "00:00");
                columns[10].setEditable(true);
                columns[11] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "02:00");
                columns[11].setEditable(true);
                columns[12] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "04:00");
                columns[12].setEditable(true);
                columns[13] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "06:00");
                columns[13].setEditable(true);
                
                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };
        
        jpResults.add(moGridTableRows, BorderLayout.CENTER);
        
        moCaptureDate.setEditable(false);
        moItemName.setEditable(false);
        moFields.setFormButton(jbSave);
    }
    
    private ArrayList<SDbGrindingResult> createNewTableRows() {
        String sql = "SELECT  " +
                "    res.id_result, " +
                "    gp.parameter AS param_name, " +
                "    (SELECT capture_cfg FROM " + SModConsts.TablesMap.get(SModConsts.CU_LINK_ITEM_PARAM) + 
                "        AS li where res.fk_item_id = li.fk_item_id " +
                "        AND res.fk_parameter_id = li.fk_parameter_id ORDER BY ts_usr_ins ASC LIMIT 1) AS capture_cfg " +
                "FROM " +
                "    " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_RESULTS) + " AS res " +
                "        INNER JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.CU_PARAMS) + " AS gp ON res.fk_parameter_id = gp.id_parameter " +
                "WHERE " +
                "    res.b_del = FALSE " +
                "    AND gp.b_del = FALSE " +
                "    AND res.fk_item_id = " + moRegistry.getFkItemId() + " " +
                "    AND dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(moRegistry.getDateCapture()) + "';";
        
        ArrayList<SDbGrindingResult> rows = new ArrayList<SDbGrindingResult>();
        try {
            ResultSet res = miClient.getSession().getDatabase().getConnection().createStatement().executeQuery(sql);
            ObjectMapper mapper = new ObjectMapper();
            
            SDbGrindingResult row = null;
            while (res.next()) {
                row = new SDbGrindingResult();
                row.read(miClient.getSession(), new int[] { res.getInt("id_result") });
                row.setParameterAux(res.getString("param_name"));
                SCaptureConfiguration cfg = mapper.readValue(res.getString("capture_cfg"), SCaptureConfiguration.class);
                row.setCaptureConfigurationAux(cfg);
                
                rows.add(row);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SFormGrindingResultHr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SFormGrindingResultHr.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rows;
    }

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {

    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbGrindingResult) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        
        SDbItem oItem = new SDbItem(); 
        oItem.read(miClient.getSession(), new int [] { moRegistry.getFkItemId() });
        
        moItemName.setValue(oItem.getCode() + " - " + oItem.getName());
        moCaptureDate.setValue(moRegistry.getDateCapture());

        removeAllListeners();
        reloadCatalogues();
        
        Vector<SGridRow> rows = new Vector<SGridRow>();
        ArrayList<SDbGrindingResult> registryRows = this.createNewTableRows();
        
        for (SDbGrindingResult registryRow : registryRows) {
            rows.add(registryRow);
        }
        
        moGridTableRows.populateGrid(rows);
        moGridTableRows.getTable().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moGridTableRows.getTable().setColumnSelectionAllowed(false);
        moGridTableRows.getTable().getTableHeader().setReorderingAllowed(false);
        moGridTableRows.getTable().getTableHeader().setResizingAllowed(true);
        moGridTableRows.getTable().setRowSorter(new TableRowSorter<AbstractTableModel>(moGridTableRows.getModel()));
        moGridTableRows.getTable().getTableHeader().setEnabled(true);
        moGridTableRows.resetSortKeys();
        moGridTableRows.setSelectedGridRow(0);
        
        if (registryRows.size() > 0) {
            SDbGrindingResult format = registryRows.get(0);
            
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(2)).setColumnTitle(format.getCaptureConfigurationAux().getR08().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(3)).setColumnTitle(format.getCaptureConfigurationAux().getR10().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(4)).setColumnTitle(format.getCaptureConfigurationAux().getR12().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(5)).setColumnTitle(format.getCaptureConfigurationAux().getR14().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(6)).setColumnTitle(format.getCaptureConfigurationAux().getR16().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(7)).setColumnTitle(format.getCaptureConfigurationAux().getR18().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(8)).setColumnTitle(format.getCaptureConfigurationAux().getR20().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(9)).setColumnTitle(format.getCaptureConfigurationAux().getR22().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(10)).setColumnTitle(format.getCaptureConfigurationAux().getR00().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(11)).setColumnTitle(format.getCaptureConfigurationAux().getR02().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(12)).setColumnTitle(format.getCaptureConfigurationAux().getR04().getLabel());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(13)).setColumnTitle(format.getCaptureConfigurationAux().getR06().getLabel());
            
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(2)).setEditable(format.getCaptureConfigurationAux().getR08().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(3)).setEditable(format.getCaptureConfigurationAux().getR10().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(4)).setEditable(format.getCaptureConfigurationAux().getR12().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(5)).setEditable(format.getCaptureConfigurationAux().getR14().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(6)).setEditable(format.getCaptureConfigurationAux().getR16().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(7)).setEditable(format.getCaptureConfigurationAux().getR18().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(8)).setEditable(format.getCaptureConfigurationAux().getR20().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(9)).setEditable(format.getCaptureConfigurationAux().getR22().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(10)).setEditable(format.getCaptureConfigurationAux().getR00().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(11)).setEditable(format.getCaptureConfigurationAux().getR02().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(12)).setEditable(format.getCaptureConfigurationAux().getR04().getIsActive());
            ((SGridColumnForm) moGridTableRows.getModel().getGridColumns().get(13)).setEditable(format.getCaptureConfigurationAux().getR06().getIsActive());
            
            jpResults.repaint();
        }
        
        setFormEditable(true);
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbGrindingResult registry = new SDbGrindingResult();
        
        return registry;
    }
    
    @Override
    public void setValue(int type, Object value) { }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            
        }

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void actionSave() {
        SGuiValidation validation = validateForm();
        
        if (SGuiUtils.computeValidation(miClient, validation)) {
            try {
                for (int i = 0; i < moGridTableRows.getTable().getRowCount(); i++) {
                    SDbGrindingResult row = (SDbGrindingResult) moGridTableRows.getGridRow(i);
                    row.save(miClient.getSession());
                }
                
                miClient.getSession().notifySuscriptors(SModConsts.SU_GRINDING_RESULTS);
                dispose();
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }
}
