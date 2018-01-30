/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Juan Barajas, Néstor Ávalos
 */
public class SViewMix extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;

    private JButton moButtonOutMixingPasive;
    private JButton moButtonOutMixingActive;
    private JButton moButtonOutConvertion;

    public SViewMix(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_MIX, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, true, true, false, true);
        initComponetsCustom();
    }

    /*
    * Private methods
    */

    private void initComponetsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        moButtonOutMixingPasive = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_mix_pas.gif")), "Salida traspaso mezcla pasiva", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonOutMixingPasive);
        moButtonOutMixingActive = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_mix_act.gif")), "Salida traspaso mezcla activa", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonOutMixingActive);
        moButtonOutConvertion = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_stk_cnv.gif")), "Salida traspaso conversión", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonOutConvertion);
    }

    private void actionMixMove(int nFkMixType) {
        SGuiParams params = null;

        params = new SGuiParams();
        params.getParamsMap().put(SModConsts.S_MIX, nFkMixType);

        miClient.getSession().getModule(SModConsts.MOD_SOM_OS, SLibConsts.UNDEFINED).showForm(SModConsts.S_MIX, SLibConsts.UNDEFINED, params);
        miClient.getSession().notifySuscriptors(mnGridType);
    }

    /*
    * Public methods
    */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);

        msSql = "SELECT v.id_mix AS " + SDbConsts.FIELD_ID + "1, " +
            "CONCAT(tp.code, '-', v.num) AS f_num, " +
            "v.dt AS " + SDbConsts.FIELD_DATE + ", " +
            "v.qty AS f_qty, " +
            "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " +
            "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", " +
            "v.fk_mix_tp, " +
            "v.fk_item_src, " +
            "v.fk_unit_src, " +
            "v.fk_item_des, " +
            "v.fk_unit_des, " +
            "v.fk_wah_src_co, " +
            "v.fk_wah_src_cob, " +
            "v.fk_wah_src_wah, " +
            "v.fk_div_src_n, " +
            "v.fk_wah_des_co, " +
            "v.fk_wah_des_cob, " +
            "v.fk_wah_des_wah, " +
            "v.fk_div_des_n, " +
            "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " +
            "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " +
            "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " +
            "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " +
            "tp.code AS tp_code, " +
            "tp.name AS tp_name, " +
            "co.code AS co_code, " +
            "co.name AS co_name, " +
            "cob.code AS cob_code, " +
            "cob.name AS cob_name, " +
            "wah_src.code AS wah_src_code, " +
            "wah_src.name AS wah_src_name, " +
            "d_src.code AS div_src_code, " +
            "d_src.name AS div_src_name, " +
            "i_src.code AS i_src_code, " +
            "i_src.name AS i_src_name, " +
            "u_src.code AS u_src_code, " +
            "u_src.name AS u_src_name, " +
            "wah_des.code AS wah_des_code, " +
            "wah_des.name AS wah_des_name, " +
            "d_des.code AS div_des_code, " +
            "d_des.name AS div_des_name, " +
            "i_des.code AS i_des_code, " +
            "i_des.name AS i_des_name, " +
            "u_des.code AS u_des_code, " +
            "u_des.name AS u_des_name, " +
            "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", " +
            "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", " +
            "tp.code AS " + SDbConsts.FIELD_CODE + ", " +
            "CONCAT(tp.code, '-', v.num) AS " + SDbConsts.FIELD_NAME + " " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_MIX) + " AS v " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_MIX_TP) + " AS tp ON " +
            "v.fk_mix_tp = tp.id_mix_tp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_CO) + " AS co ON " +
            "v.fk_wah_src_co = co.id_co " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS cob ON " +
            "v.fk_wah_src_co = cob.id_co AND v.fk_wah_src_cob = cob.id_cob " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah_src ON " +
            "v.fk_wah_src_co = wah_src.id_co AND v.fk_wah_src_cob = wah_src.id_cob AND v.fk_wah_src_wah = wah_src.id_wah " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i_src ON " +
            "v.fk_item_src = i_src.id_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u_src ON " +
            "v.fk_unit_src = u_src.id_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah_des ON " +
            "v.fk_wah_des_co = wah_des.id_co AND v.fk_wah_des_cob = wah_des.id_cob AND v.fk_wah_des_wah = wah_des.id_wah " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i_des ON " +
            "v.fk_item_des = i_des.id_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u_des ON " +
            "v.fk_unit_des = u_des.id_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON " +
            "v.fk_usr_ins = ui.id_usr " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON " +
            "v.fk_usr_upd = uu.id_usr " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d_src ON " +
            "v.fk_div_src_n = d_src.id_div " +
            "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d_des ON " +
            "v.fk_div_des_n = d_des.id_div " +
            (sql.isEmpty() ? "" : "WHERE " + sql) +
            "ORDER BY v.num, co.name, co.code, cob.name, cob.code, wah_src.name, wah_src.code, wah_src.code ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[20];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "cob_code", "Sucursal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "wah_src_code", "Almacén origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "div_src_code", "División origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "wah_des_code", "Almacén destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "div_des_code", "División destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tp_name", "Tipo mezcla");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "i_src_name", "Ítem orígen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i_src_code", "Ítem código orígen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "i_des_name", "Ítem destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i_des_code", "Ítem código destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_qty", "Cantidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u_src_code", "Unidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_IOG);
        moSuscriptionsSet.add(SModConsts.S_STK);
        moSuscriptionsSet.add(SModConsts.S_STK_DAY);
        moSuscriptionsSet.add(SModConsts.S_IOG_EXP);
        moSuscriptionsSet.add(SModConsts.S_IOG_EXP_HIS);
        moSuscriptionsSet.add(SModConsts.CU_CO);
        moSuscriptionsSet.add(SModConsts.CU_COB);
        moSuscriptionsSet.add(SModConsts.CU_WAH);
        moSuscriptionsSet.add(SModConsts.CU_DIV);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_UNIT);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == moButtonOutMixingPasive && moButtonOutMixingPasive.isEnabled()) {
                actionMixMove(SModSysConsts.SS_MIX_TP_MIX_PAS);
            }
            else if (button == moButtonOutMixingActive && moButtonOutMixingActive.isEnabled()) {
                actionMixMove(SModSysConsts.SS_MIX_TP_MIX_ACT);
            }
            else if (button == moButtonOutConvertion && moButtonOutConvertion.isEnabled()) {
                actionMixMove(SModSysConsts.SS_MIX_TP_CNV);
            }
        }
    }
}
