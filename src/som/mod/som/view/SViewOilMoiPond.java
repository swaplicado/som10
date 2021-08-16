/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import som.mod.SModConsts;
import som.mod.som.form.SFormWarehouseCardex;

/**
 * Vista que muestra los ponderados de aceite y humedad por almacen en base a la fecha de inicio.
 * @author Isabel Servín
 */
public class SViewOilMoiPond extends SGridPaneView implements ActionListener {
    
    private JButton mjbShowDetails;
    
    /**
     * @param client
     * @param title
     */
    public SViewOilMoiPond(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_QA_OIL_MOI_POND, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        mjbShowDetails = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_look.gif")), "Ver histórico del almacén", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbShowDetails);
    }

    private void actionShowDetails() {
        if (mjbShowDetails.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    SFormWarehouseCardex form = new  SFormWarehouseCardex (miClient, (int[])gridRow.getRowPrimaryKey());
                    form.setVisible(true);
                }
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        moPaneSettings = new SGridPaneSettings(3);
        
        msSql = "SELECT " 
                + "w.id_co AS " + SDbConsts.FIELD_ID + "1, "
                + "w.id_cob AS " + SDbConsts.FIELD_ID + "2, "
                + "w.id_wah AS " + SDbConsts.FIELD_ID + "3, "
                + "w.code AS " + SDbConsts.FIELD_CODE + ", "  
                + "w.name AS " + SDbConsts.FIELD_NAME + ", "
                + "avg_tic.id_item, " 
                + "avg_tic.item, " 
                + "avg_tic.dt_start, " 
                + "SUM(avg_tic.wei_des_net_r) AS _sum_all_wei, " 
                + "SUM(IF(avg_tic.id_lab IS NULL, 0, avg_tic.wei_des_net_r)) AS _sum_lab_wei, "
                + "SUM(IF(avg_tic.id_lab IS NOT NULL, 0, avg_tic.wei_des_net_r)) AS _sum_n_lab_wei, "
                + "SUM(1) AS _sum_all_tic, "
                + "SUM(IF(avg_tic.id_lab IS NULL, 0, 1)) AS _sum_lab_tic, "
                + "SUM(IF(avg_tic.id_lab IS NOT NULL, 0, 1)) AS _sum_n_lab_tic, "
                + "SUM(avg_tic._res_wei_oil) AS _sum_wei_oil, " 
                + "SUM(avg_tic._res_wei_moi) AS _sum_wei_moi, " 
                + "SUM(avg_tic._res_wei_oil)/SUM(IF(avg_tic.id_lab IS NULL, 0, avg_tic.wei_des_net_r)) AS _pond_oil, " 
                + "SUM(avg_tic._res_wei_moi)/SUM(IF(avg_tic.id_lab IS NULL, 0, avg_tic.wei_des_net_r)) AS _pond_moi " 
                + "FROM cu_wah AS w " 
                + "INNER JOIN " 
                + "(" 
                + "SELECT wah_date.dt_start, t.fk_wah_unld_co_n, t.fk_wah_unld_cob_n, t.fk_wah_unld_wah_n, i.id_item, i.name AS item, t.num, t.wei_des_net_r, l.id_lab, avg(lt.oil_per) AS oil, avg(lt.moi_per) AS moi, " 
                + "t.wei_des_net_r * avg(lt.oil_per) AS _res_wei_oil, t.wei_des_net_r * avg(lt.moi_per) AS _res_wei_moi " 
                + "FROM s_tic AS t " 
                + "INNER JOIN " 
                + "(" 
                + "SELECT ws1.dt_start, ws1.fk_wah_co, ws1.fk_wah_cob, ws1.fk_wah_wah FROM s_wah_start AS ws1 WHERE NOT EXISTS " 
                + "(SELECT * FROM s_wah_start AS ws2 WHERE ws1.fk_wah_co = ws2.fk_wah_co AND ws1.fk_wah_cob = ws2.fk_wah_cob AND ws1.fk_wah_wah = ws2.fk_wah_wah "
                + "AND ws1.dt_start < ws2.dt_start AND NOT ws2.b_del) AND NOT ws1.b_del " 
                + ") AS wah_date ON wah_date.fk_wah_co = t.fk_wah_unld_co_n AND wah_date.fk_wah_cob = t.fk_wah_unld_cob_n AND wah_date.fk_wah_wah = t.fk_wah_unld_wah_n AND wah_date.dt_start <= t.dt "
                + "INNER JOIN su_item AS i ON t.fk_item = i.id_item " 
                + "LEFT OUTER JOIN s_lab AS l ON t.fk_lab_n = l.id_lab " 
                + "LEFT OUTER JOIN s_lab_test AS lt ON l.id_lab = lt.id_lab "  
                + "WHERE NOT t.b_del " 
                + "GROUP BY l.id_lab, t.fk_wah_unld_co_n, t.fk_wah_unld_cob_n, t.fk_wah_unld_wah_n " 
                + "ORDER BY t.fk_wah_unld_co_n, t.fk_wah_unld_cob_n, t.fk_wah_unld_wah_n " 
                + ") " 
                + "AS avg_tic ON w.id_co = avg_tic.fk_wah_unld_co_n AND w.id_cob = avg_tic.fk_wah_unld_cob_n AND w.id_wah = avg_tic.fk_wah_unld_wah_n "
                + "GROUP BY w.code, w.name, avg_tic.id_item " 
                + "ORDER BY w.code, w.name, avg_tic.dt_start;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[12];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Almacén");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Almacén código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "item", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "avg_tic.dt_start", "Inicio almacén");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "_sum_lab_tic", "Boletos c/resultados lab.");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "_sum_n_lab_tic", "Boletos s/resultados lab.");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "_sum_all_tic", "Total boletos");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_sum_lab_wei", "Peso c/resultados lab.(kg)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_sum_n_lab_wei", "Peso s/resultados lab.(kg)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_sum_all_wei", "Tolal peso almacenado(kg)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "_pond_oil", "Aceite ponderado(%)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "_pond_moi", "Humedad ponderado(%)");
        
        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_WAH);
        moSuscriptionsSet.add(SModConsts.S_WAH_START);        
        moSuscriptionsSet.add(SModConsts.S_LAB);        
        moSuscriptionsSet.add(SModConsts.S_LAB_TEST);        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == mjbShowDetails) {
                actionShowDetails();
            }
        }
    }
}
