/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.mod.som.db.SDbInputCategory;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SSomMailUtils;

/**
 *
 * @author Sergio Flores
 */
public class SReportHtmlTicketSeasonMonth {
    
    private final SGuiSession moSession;
    
    public SReportHtmlTicketSeasonMonth(final SGuiSession session) {
        moSession = session;
    }
    
    /**
     * Generates report in HTML 5 format.
     * @param itemId Item ID.
     * @param yearRef Year reference. Can be two types of values: i.e., if >= 2001, then is the year to start from; otherwise is a number of history years besides current year.
     * @return
     * @throws Exception 
     */
    public String generateReportHtml(final int itemId, final int yearRef) throws Exception {
        // read requested item for report:
        SDbItem item = new SDbItem();
        item.read(moSession, new int[] { itemId });
        SDbInputCategory inputCategory = new SDbInputCategory();
        inputCategory.read(moSession, new int[] { item.getFkInputCategoryId() });
        
        // define start and end date for report:
        Date today = new Date();
        int[] todayDigestion = SLibTimeUtils.digestDate(today);
        int todayYear = todayDigestion[0];
        int todayMonth = todayDigestion[1];
        int years;
        int yearStart;
        int yearEnd;
        
        yearStart = todayMonth < item.getStartingSeasonMonth() ? todayYear - 1 : todayYear;
        
        if (yearRef >= SLibTimeConsts.YEAR_MIN) {
            // year reference contains the year to start from:
            yearEnd = yearRef;
            years = yearStart - yearEnd;
        }
        else {
            // year reference contains a number of history years:
            years = yearRef;
            yearEnd = yearStart - years;
        }
        
        if (yearEnd > yearStart) {
            throw new Exception("El año final no puede ser mayor al año inicial.");
        }
        
        // HTML:
        
        String html = "<html>\n";
        
        // HTML head:
        
        html += "<head>\n";
        html += "<style>\n"
                + "body {"
                + " font-size: 100%;"
                + "} "
                + "h1 {"
                + " font-size: 2.5em;"
                + " font-family: sans-serif;"
                + "} "
                + "h2 {"
                + " font-size: 1.5em;"
                + " font-family: sans-serif;"
                + "} "
                + "p {"
                + " font-size: 0.875em;"
                + " font-family: sans-serif;"
                + "} "
                + "table {"
                + " /*width:100%;*/" // nullified attribute
                + " font-size: 0.875em;"
                + " font-family: sans-serif;"
                + "} "
                + "table, th, td {"
                + " border: 1px solid black;"
                + " border-collapse: collapse;"
                + "} "
                + "th {"
                + " padding: 2px;"
                + " text-align: center;"
                + " background-color: DarkSlateGray;"
                + " color: white;"
                + "} "
                + "td {"
                + " padding: 2px;"
                + "} "
                + "td.colmonth {"
                + " text-align: left;"
                + "} "
                + "td.coldata {"
                + " text-align: right;"
                + "} "
                + "td.coldatamax {"
                + " text-align: right;"
                + " background-color: PaleTurquoise;"
                + "} "
                + "td.coldatapct {"
                + " text-align: center;"
                + " font-size: 0.75em;"
                + " font-family: sans-serif;"
                + "} "
                + "td.coldatapctmax {"
                + " text-align: center;"
                + " font-size: 0.75em;"
                + " font-family: sans-serif;"
                + " background-color: PaleTurquoise;"
                + "}\n"
                + "</style>\n";
        
        html += "</head>\n";
        
        // HTML body:
        
        html += "<body>\n";
        html += "<h2>" + SLibUtils.textToHtml("Comparativo histórico mensual recepción " + inputCategory.getName().toLowerCase()) + "</h2>\n";
        html += "<p><b>" + SLibUtils.textToHtml(item.getName()) + "</b> al " + SLibUtils.textToHtml(SLibUtils.DateFormatDateLong.format(today)) + " (" + SLibUtils.DateFormatTime.format(today) + ")</p>\n";
        
        // obtain report data:
        
        double[] tableTotals = new double[years + 1]; // total weight per year
        double[][] tableValues = new double[years + 1][SLibTimeConsts.MONTHS]; // total weight per year
        HashMap<Integer, Integer> maxValues = new HashMap<>(); // key: year index, value: row index
        
        String sql = "SELECT SUM(wei_des_net_r) "
                + "FROM s_tic "
                + "WHERE NOT b_del AND b_tar AND fk_item = " + itemId + " AND dt BETWEEN ? AND ?";
        PreparedStatement preparedStatement = moSession.getStatement().getConnection().prepareStatement(sql);
        
        // monthly weights, begining from first year or season backwards:
        
        int year;
        int month = item.getStartingSeasonMonth();
        
        for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {
            year = yearStart;
            
            if (item.getStartingSeasonMonth() + row > SLibTimeConsts.MONTHS) {
                year++;
            }
            
            for (int col = 0; col < tableTotals.length; col++) {
                Date start = SLibTimeUtils.createDate(year - col, month, 1);
                Date end = SLibTimeUtils.getEndOfMonth(start);
                
                preparedStatement.setDate(1, new java.sql.Date(start.getTime()));
                preparedStatement.setDate(2, new java.sql.Date(end.getTime()));
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double value = resultSet.getDouble(1);
                    tableTotals[col] += value;
                    tableValues[col][row] = value;
                    
                    Integer rowOfMaxValue = maxValues.get(col);
                    if (rowOfMaxValue == null || value > tableValues[col][rowOfMaxValue]) {
                        maxValues.put(col, row);
                    }
                }
            }
            
            if (++month > SLibTimeConsts.MONTHS) {
                month = 1;
            }
        }
        
        // HTML table:
        
        html += "<table>\n";
        
        // table header:
        
        ArrayList<String> headerCols = new ArrayList<>();
        headerCols.add("Mes");
        for (int col = yearStart; col >= yearEnd; col--) {
            if (item.getStartingSeasonMonth() == 1) {
                headerCols.add("" + col);
            }
            else {
                headerCols.add("" + col + "-" + (col + 1));
            }
        }
        
        html += "<tr>";
        for (int col = 0; col < headerCols.size(); col++) {
            html += "<th" + (col == 0 ? "" : " colspan='2'") + ">" + headerCols.get(col) + "</th>";
        }
        html += "</tr>\n";
        
        
        // table body:
        
        String[] months = SLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT); // month names for first column
        DecimalFormat decimalFormatPct = new DecimalFormat("#0.0%");
        
        month = item.getStartingSeasonMonth();
        
        for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {
            
            html += "<tr>";
            html += "<td class='colmonth'>" + months[month - 1] + "</td>";
            
            for (int col = 0; col < tableTotals.length; col++) {
                boolean isMax = maxValues.get(col) == row;
                html += "<td class='coldata" + (isMax ? "max" : "") + "'>" + SLibUtils.getDecimalFormatAmount().format(tableValues[col][row]) + "</td>";
                html += "<td class='coldatapct" + (isMax ? "max" : "") + "'>" + decimalFormatPct.format(tableTotals[col] == 0 ? 0 : tableValues[col][row] / tableTotals[col]) + "</td>";
            }
            
            if (++month > SLibTimeConsts.MONTHS) {
                month = 1;
            }
            
            html += "</tr>\n";
        }
        
        // table footer:
        
        html += "<tr>";
        html += "<td class='colmonth'><b>Total</b></td>";
        for (int col = 0; col < tableTotals.length; col++) {
            html += "<td class='coldata'><b>" + SLibUtils.getDecimalFormatAmount().format(tableTotals[col]) + "</b></td>";
            html += "<td class='coldatapct'><b>" + decimalFormatPct.format(1) + "</b></td>";
        }
        html += "</tr>\n";
        
        html += "</table>\n";
        
        html += SSomMailUtils.composeMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
        
        return html;
    }
}
