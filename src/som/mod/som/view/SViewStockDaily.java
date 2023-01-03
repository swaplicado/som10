/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import som.mod.SModConsts;

/**
 *
 * @author Isabel Servín
 */
public class SViewStockDaily extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    
    private ArrayList<Date> moDates;
    private ArrayList<int[]> moWahIds;
    
    public SViewStockDaily(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.SX_STK_DAILY, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        moDates = new ArrayList<>();
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        miClient.showMsgBoxInformation("El proceso puede demorar unos minutos.");
    }
    
    private void prepareSqlTable(SGuiDate filterDate) {
        try {
            String sql = "DROP TEMPORARY TABLE IF EXISTS existencias;";
            miClient.getSession().getStatement().execute(sql);
            
            sql = "SELECT DISTINCT r.dt FROM s_stk_record AS r " +
                    "INNER JOIN cu_wah AS w ON r.id_co = w.id_co AND r.id_cob = w.id_cob AND r.id_wah = w.id_wah " +
                    "WHERE (r.dt BETWEEN (SELECT ADDDATE(CAST(DATE_FORMAT('" + SLibUtils.DbmsDateFormatDate.format((Date) filterDate) + "' ,'%Y-%m-01') AS DATE), INTERVAL -6  DAY)) " +
                    "AND (SELECT ADDDATE(LAST_DAY('" + SLibUtils.DbmsDateFormatDate.format((Date) filterDate) + "'), INTERVAL 5 DAY))) " +
                    "ORDER BY r.dt;";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            
            sql = "CREATE TEMPORARY TABLE existencias(" + 
                    "id_co SMALLINT UNSIGNED NOT NULL, " +
                    "id_cob SMALLINT UNSIGNED NOT NULL, " + 
                    "id_wah SMALLINT UNSIGNED NOT NULL, " +
                    "id_item SMALLINT UNSIGNED NOT NULL";
    
            boolean first = true;
            moDates = new ArrayList<>();
            while (resultSet.next()){
                moDates.add(resultSet.getDate(1));
                if (!first) {
                    sql += ", `" + SLibUtils.DbmsDateFormatDate.format(resultSet.getDate(1)) + "` DECIMAL(23,8) ";
                }
                first = false;
            }
            sql += ");";
            miClient.getSession().getStatement().execute(sql);
            
            sql = "SELECT DISTINCT w.id_co, w.id_cob, w.id_wah FROM s_stk_record AS r " +
                    "INNER JOIN cu_wah AS w ON r.id_co = w.id_co AND r.id_cob = w.id_cob AND r.id_wah = w.id_wah " +
                    "WHERE (r.dt BETWEEN (SELECT ADDDATE(CAST(DATE_FORMAT('" + SLibUtils.DbmsDateFormatDate.format((Date) filterDate) + "' ,'%Y-%m-01') AS DATE), INTERVAL -6  DAY)) " +
                    "AND (SELECT ADDDATE(LAST_DAY('" + SLibUtils.DbmsDateFormatDate.format((Date) filterDate) + "'), INTERVAL 5 DAY))) " +
                    "ORDER BY w.b_mobile, w.code;";
            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            moWahIds = new ArrayList<>();
            while (resultSet.next()) {
                moWahIds.add(new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
            }
            
            for (int[] wahId : moWahIds) {
                sql = "SELECT id_item FROM s_stk_record " +
                        "WHERE id_co = " + wahId[0] + " " + 
                        "AND id_cob = " + wahId[1] + " " + 
                        "AND id_wah  = " + wahId[2] + " " +
                        "AND dt = '" + SLibUtils.DbmsDateFormatDate.format(moDates.get(moDates.size() - 1)) + "';";
                resultSet = miClient.getSession().getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    sql = "INSERT INTO existencias (id_co, id_cob, id_wah, id_item) " +
                            "VALUES (" + wahId[0] + ", " + wahId[1] + ", " + wahId[2] + ", " + resultSet.getInt(1) + ");";
                    miClient.getSession().getStatement().execute(sql);
                }
                
                for (Date date : moDates) {
                    sql = "SELECT stock FROM s_stk_record " +
                            "WHERE id_co = " + wahId[0] + " " + 
                            "AND id_cob = " + wahId[1] + " " + 
                            "AND id_wah  = " + wahId[2] + " " +
                            "AND dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "';";
                    resultSet = miClient.getSession().getStatement().executeQuery(sql);
                    Double stock = null;
                    if (resultSet.next()) {
                        stock = resultSet.getDouble(1);
                    }
                    try {
                        sql = "UPDATE existencias SET `" + SLibUtils.DbmsDateFormatDate.format(addDate(date)) + "` = " + (stock == null ? "NULL " : stock) + " " +
                                "WHERE id_co = " + wahId[0] + " " +
                                "AND id_cob = " + wahId[1] + " " + 
                                "AND id_wah  = " + wahId[2] + " ";
                        miClient.getSession().getStatement().execute(sql);
                    }
                    catch(SQLException e) {}
                }
            }
        }
        catch(Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private Date addDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    @Override
    public void prepareSqlQuery() {
        prepareSqlTable((SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD));
        super.miUserGui = null;
        super.createGridView();
        
        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);

        msSql = "SELECT " 
                + "e.id_co AS " + SDbConsts.FIELD_ID + "1, " 
                + "e.id_cob AS " + SDbConsts.FIELD_ID + "2, " 
                + "e.id_wah AS " + SDbConsts.FIELD_ID + "3, " 
                + "w.code AS " + SDbConsts.FIELD_CODE + ", " 
                + "w.name AS " + SDbConsts.FIELD_NAME + ", " 
                + "i.name AS item, " 
                + "e.* " 
                + "FROM existencias AS e " 
                + "INNER JOIN cu_wah AS w ON " 
                + "e.id_co = w.id_co AND e.id_cob = w.id_cob AND e.id_wah = w.id_wah "
                + "INNER JOIN su_item AS i ON "
                + "e.id_item = i.id_item "
                + "ORDER BY w.b_mobile, w.code";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[moDates.isEmpty() ? 2 : moDates.size() + 1];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "item", "Ítem");
        boolean first = true;
        for (Date date : moDates) {
            if (!first) {
                columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, SLibUtils.DbmsDateFormatDate.format(date), SLibUtils.DbmsDateFormatDate.format(date));
            }
            first = false;
        }
        
        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }
}
