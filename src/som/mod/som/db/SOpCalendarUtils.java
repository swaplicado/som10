/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 * Operating calendar utilities.
 * @author Sergio Flores
 */
public abstract class SOpCalendarUtils {
    
    /**
     * Create operating calendars map.
     * Key: ID of operating calendar.
     * Value: ArrayList of ID's of applying items.
     * @param session GUI session.
     * @return Operating calendars map
     * @throws Exception 
     */
    public static HashMap<Integer, ArrayList<Integer>> createOpCalendarsMap(final SGuiSession session) throws Exception {
        HashMap<Integer, ArrayList<Integer>> opCalendarsMap = new HashMap<>();
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT id_op_cal, item_apply "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_OP_CAL) + " "
                    + "WHERE NOT b_del "
                    + "ORDER BY id_op_cal;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String[] itemIds = resultSet.getString("item_apply").split(";");
                ArrayList<Integer> idsList = Arrays.stream(itemIds)
                        .map(Integer::parseInt)
                        .collect(Collectors.toCollection(ArrayList::new));
                opCalendarsMap.put(resultSet.getInt("id_op_cal"), idsList);
            }
        }
        
        return opCalendarsMap;
    }
    
    /**
     * Get ID of operating calendar applying to given item.
     * @param opCalendarsMap Operating calendars map.
     * @param itemId ID of item.
     * @return ID of operating calendar.
     */
    public static int getOpCalendarId(final HashMap<Integer, ArrayList<Integer>> opCalendarsMap, final int itemId) {
        int opCalendarId = 0;
        
        for (Integer id : opCalendarsMap.keySet()) {
            ArrayList<Integer> idsList = opCalendarsMap.get(id);
            if (idsList.contains(itemId)) {
                opCalendarId = id;
                break;
            }
        }
        
        return opCalendarId;
    }
    
    /**
     * Read primary key of operating calendar month for given scale ticket date.
     * @param statement DB statement.
     * @param opCalendarId ID of operating calendar.
     * @param ticketDate Scale ticket date.
     * @return Primary key of operative calendar month, if any, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static int[] readOpCalendarMonthKey(final Statement statement, final int opCalendarId, final Date ticketDate) throws Exception {
        int[] key = null;
        String sql = "SELECT id_op_cal, id_year, id_month "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_OP_CAL_YEAR_MONTH) + " "
                + "WHERE id_op_cal = " + opCalendarId + " AND '" + SLibUtils.DbmsDateFormatDate.format(ticketDate) + "' BETWEEN month_sta AND month_end "
                + "ORDER BY id_op_cal, id_year, id_month "
                + "LIMIT 1;";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                key = new int[] { resultSet.getInt("id_op_cal"), resultSet.getInt("id_year"), resultSet.getInt("id_month") };
            }
        }
        
        return key;
    }
    
    /**
     * Get primary key of operative calendar month, if any, for given item and ticket date.
     * @param session GUI session.
     * @param itemId ID of item.
     * @param ticketDate Date of scale ticket.
     * @return Primary key of operative calendar month, if any, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static int[] getOpCalendarMonthKey(final SGuiSession session, final int itemId, final Date ticketDate) throws Exception {
        int[] key = null;
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            HashMap<Integer, ArrayList<Integer>> opCalendarsMap = createOpCalendarsMap(session);
            int opCalendarId = getOpCalendarId(opCalendarsMap, itemId);
            
            if (opCalendarId != 0) {
                key = readOpCalendarMonthKey(statement, opCalendarId, ticketDate);
            }
        }
        
        return key;
    }
    
    /**
     * Massive assign of operative calendar month to all scale tickets.
     * WARNING: All massive updates to scale tickets are irreversible. Please proceed with caution!
     * @param session GUI session.
     * @throws Exception 
     */
    public static void assignOpCalendarToAllTickets(final SGuiSession session) throws Exception {
        try (Statement statement = session.getStatement().getConnection().createStatement(); Statement statementUpd = session.getStatement().getConnection().createStatement()) {
            int totalCount = 0;
            int clearedCount = 0; // tickets cleared due to non-applying item for operative calendar
            int skippedCount = 0; // tickets skipped due to unexisting operative month
            int updatedCount = 0; // tickets updated
            
            String sql = "SELECT COUNT(*) "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " "
                    + "WHERE NOT b_del AND b_tar;";
            
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
                
                if (totalCount > 0) {
                    int count = 0;
                    int currentYear = 0;
                    int currentPct = -1;
                    SDbTicket ticket = new SDbTicket();
                    HashMap<Integer, Integer> itemsOpCalendarMap = new HashMap<>(); // key: ID of item; value: ID of operating calendar
                    HashMap<Integer, ArrayList<Integer>> opCalendarsMap = createOpCalendarsMap(session); // key: ID of operating calendar; value: ID's of applying items

                    sql = "SELECT dt, id_tic, fk_item, YEAR(dt) AS _year "
                            + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " "
                            + "WHERE NOT b_del AND b_tar "
                            + "ORDER BY dt, id_tic;";

                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        int year = resultSet.getInt("_year");

                        if (year != currentYear) {
                            currentYear = year;
                            System.out.println(SLibUtils.textRepeat("=", 80));
                            System.out.println("Processing year " + currentYear + "...");
                        }
                        
                        double pct = ++count / (double) totalCount;
                        if ((int) (pct * 100) > currentPct) {
                            currentPct = (int) (pct * 100);
                            System.out.println("Progress: " + SLibUtils.DecimalFormatPercentage0D.format(currentPct / 100.0) + " (" + SLibUtils.DecimalFormatInteger.format(count) + " out of " + SLibUtils.DecimalFormatInteger.format(totalCount) + ")");
                        }

                        int itemId = resultSet.getInt("fk_item");
                        int opCalendarId = 0;
                        
                        if (itemsOpCalendarMap.containsKey(itemId)) {
                            // reuse identified operative calendar for current item:
                            opCalendarId = itemsOpCalendarMap.get(itemId);
                        }
                        else {
                            // get operative calendar for current item:
                            opCalendarId = getOpCalendarId(opCalendarsMap, itemId);
                            
                            if (opCalendarId != 0) {
                                // preserve operative calendar for current item:
                                itemsOpCalendarMap.put(itemId, opCalendarId);
                            }
                        }
                        
                        if (opCalendarId == 0) {
                            // ticket cleared due to non-applying item for operative calendar:
                            ticket.saveField(statementUpd, new int[] { resultSet.getInt("id_tic") }, SDbTicket.FIELD_OP_CALENDAR, null);
                            clearedCount++;
                        }
                        else {
                            int[] key = readOpCalendarMonthKey(statementUpd, opCalendarId, resultSet.getDate("dt"));

                            if (key == null) {
                                // ticket skipped due to unexisting operative month:
                                ticket.saveField(statementUpd, new int[] { resultSet.getInt("id_tic") }, SDbTicket.FIELD_OP_CALENDAR, null);
                                skippedCount++;
                            }
                            else {
                                // tickets updated:
                                ticket.saveField(statementUpd, new int[] { resultSet.getInt("id_tic") }, SDbTicket.FIELD_OP_CALENDAR, key);
                                updatedCount++;
                            }
                        }
                    }
                }
            }
            
            System.out.println(SLibUtils.textRepeat("=", 80));
            
            if (totalCount > 0) {
                System.out.println("Cleared tickets: " + SLibUtils.DecimalFormatInteger.format(clearedCount) + ", " + SLibUtils.DecimalFormatPercentage1D.format(clearedCount / totalCount) + ".");
                System.out.println("Skipped tickets: " + SLibUtils.DecimalFormatInteger.format(skippedCount) + ", " + SLibUtils.DecimalFormatPercentage1D.format(skippedCount / totalCount) + ".");
                System.out.println("Updated tickets: " + SLibUtils.DecimalFormatInteger.format(updatedCount) + ", " + SLibUtils.DecimalFormatPercentage1D.format(updatedCount / totalCount) + ".");
            }
            
            System.out.println("Total tickets: " + SLibUtils.DecimalFormatInteger.format(totalCount) + ", " + SLibUtils.DecimalFormatPercentage1D.format(1) + ".");
        }
    }
}
