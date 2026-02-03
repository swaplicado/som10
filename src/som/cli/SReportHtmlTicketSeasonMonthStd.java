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
import som.mod.som.db.SDbUnit;
import som.mod.som.db.SSomMailUtils;

/**
 *
 * @author Sergio Flores
 */
public class SReportHtmlTicketSeasonMonthStd {
    
    /** Report in unit of measure of item. */
    public static final int MODE_UNIT_ITEM = 1;
    /** Report in metric tons. */
    public static final int MODE_UNIT_TON = 2;
    
    private final SGuiSession moSession;
    
    public SReportHtmlTicketSeasonMonthStd(final SGuiSession session) {
        moSession = session;
    }
    
    /**
     * Generates report in HTML 5 format.
     * @param itemIds Array of ID of items to report.
     * @param yearRef Reference year. Can be one out of two elegible types of values: 1) if it is a 4-digit year and greater or equal than 2001, it is the year to start from; otherwise 2) it is the number of history years towards current year.
     * @param intvlDays Interval days for invocation of this report mailer.
     * @param seasonFirstMonth Season first month (1 = January; 0 = item's start month.)
     * @param monthFirstDay Month first day (0 | 1 = 1st. of start month; > 1 = nth of previous month.)
     * @param cutoff Cutoff date.
     * @param now The very moment of processing.
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @param mode a) Unit of measure of item; b) Metric tons.
     * @return
     * @throws Exception 
     */
    public String generateReportHtml(final int[] itemIds, final int yearRef, final int intvlDays, final int seasonFirstMonth, final int monthFirstDay, final Date cutoff, final Date now, final int ticketOrigin, final int ticketDestination, final int mode) throws Exception {
        // HTML:
        
        String html = "<html>\n";
        
        // HTML head:
        
        html += "<head>\n";
        html += "<style>\n"
                + "body {"
                + " font-size: 100%;"
                + "} "
                + "h1 {"
                + " font-size: 2.00em;"
                + " font-family: sans-serif;"
                + "} "
                + "h2 {"
                + " font-size: 1.75em;"
                + " font-family: sans-serif;"
                + "} "
                + "h3 {"
                + " font-size: 1.50em;"
                + " font-family: sans-serif;"
                + "} "
                + "h4 {"
                + " font-size: 1.25em;"
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
                + " background-color: #008080;"
                + " color: white;"
                + " word-break: keep-all;"
                + " white-space: nowrap;"
                + "} "
                + "td {"
                + " padding: 2px;"
                + " word-break: keep-all;"
                + " white-space: nowrap;"
                + "} "
                + "td.colmonth {"
                + " text-align: left;"
                + "} "
                + "td.coldata {"
                + " text-align: right;"
                + "} "
                + "td.coldatamax {"
                + " text-align: right;"
                + " background-color: Aqua;"
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
                + " background-color: Aqua;"
                + "}"
                + "td.coldatapctaccum {"
                + " text-align: center;"
                + " font-size: 0.75em;"
                + " font-family: sans-serif;"
                + " background-color: #E5E7E9;"
                + "} "
                + "td.colmonthaccum {"
                + " text-align: left;"
                + " background-color: #E5E7E9;"
                + " white-space: nowrap;"
                + "} "
                + "td.coldataaccum {"
                + " text-align: right;"
                + " background-color: #E5E7E9;"
                + "} "
                + "td.coldatatotal {"
                + " text-align: right;"
                + " background-color: #80bfbf;"
                + "} "
                + "td.coldatapcttotal {"
                + " text-align: center;"
                + " font-size: 0.75em;"
                + " font-family: sans-serif;"
                + " background-color: #80bfbf;"
                + "} "
                + "td.colmonthtotal {"
                + " text-align: left;"
                + " background-color: #80bfbf;"
                + "} "
                + "\n"
                + "</style>\n";
        
        html += "</head>\n";
        
        // HTML body:
        
        html += "<body>\n";
        
        // define time control variables for report:
        
        int[] cutoffDigestion = SLibTimeUtils.digestDate(cutoff);
        int cutoffYear = cutoffDigestion[0];
        int cutoffMonth = cutoffDigestion[1];
        boolean isCustomSeason = seasonFirstMonth > 0 && monthFirstDay > 0;
        boolean isUnitsTon = mode == MODE_UNIT_TON;
        double unitsDivisor = isUnitsTon ? 1000 : 1;
        String[] months = SLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT); // month names for first column in table of each item
        
        // HTML heading 1 (main title):
        
        html += "<h2>" + SLibUtils.textToHtml("Histórico mensual de báscula: Recepción de fruta") + "</h2>\n";
        
        if (SLibTimeUtils.isSameDate(cutoff, now)) {
            html += "<p>" + SLibUtils.textToHtml("Fecha-hora de corte y emisión: " + SLibUtils.DateFormatDatetime.format(now) + ".") + "</p>\n";
        }
        else {
            html += "<p>" + SLibUtils.textToHtml("Fecha de corte: " + SLibUtils.DateFormatDate.format(cutoff) + ".") + "</p>\n";
            html += "<p>" + SLibUtils.textToHtml("Fecha-hora de emisión: " + SLibUtils.DateFormatDatetime.format(now) + ".") + "</p>\n";
        }
        
        // process list of items for report:

        int lastInputCategoryId = 0; // to control when a new input category stages, to stand it out as a new title
        
        for (int itemId : itemIds) {
            // formatters:
            DecimalFormat decimalFormatPct = isUnitsTon ? new DecimalFormat("#0%") : new DecimalFormat("#0.0%");
            DecimalFormat decimalFormatVal = isUnitsTon ? SLibUtils.DecimalFormatInteger : SLibUtils.getDecimalFormatAmount();
            
            // read requested item for report:
            SDbItem item = new SDbItem();
            item.read(moSession, new int[] { itemId }); // read this way due to session is moduleless
            SDbUnit unit = new SDbUnit();
            unit.read(moSession, new int[] { item.getFkUnitId() }); // read this way due to session is moduleless
            SDbInputCategory inputCategory = new SDbInputCategory();
            inputCategory.read(moSession, new int[] { item.getFkInputCategoryId() }); // read this way due to session is moduleless

            int reportFirstSeasonMonth = seasonFirstMonth > 0 ? seasonFirstMonth : (item.getStartingSeasonMonth() > 0 ? item.getStartingSeasonMonth() : SLibTimeConsts.MONTH_JAN);
            int reportFirstMonthDay = monthFirstDay > 0 ? monthFirstDay : 1;
            
            int yearStart = cutoffMonth < reportFirstSeasonMonth ? cutoffYear - 1 : cutoffYear;
            int yearEnd = 0;
            int years = 0;
            
            if (yearRef >= SLibTimeConsts.YEAR_MIN) {
                // year reference has the year to start from:
                
                yearEnd = yearRef;
                years = yearStart - yearEnd;
            }
            else {
                // year reference has a number of history years:
                
                years = yearRef;
                yearEnd = yearStart - years;
            }

            // validat years:
            if (yearEnd > yearStart) {
                throw new Exception("El año final, " + yearEnd + ", no puede ser mayor al año inicial, " + yearStart + ".");
            }

            // HTML heading 2 (input category subtitle, if necesary):

            if (lastInputCategoryId != inputCategory.getPkInputCategoryId()) {
                html += "<h3>" + SLibUtils.textToHtml("Categoría: " + SLibUtils.textProperCase(inputCategory.getName())) + "</h3>\n";
                
                lastInputCategoryId = inputCategory.getPkInputCategoryId();
            }
            
            // HTML heading 3 (item subtitle):
            
            String name = SCliConsts.ItemNames.get(itemId);
            html += "<h4>" + SLibUtils.textToHtml((name != null ? name : SLibUtils.textProperCase(item.getName())) + " (valores en " + (isUnitsTon ? "ton" : unit.getCode()) + ")") + "</h4>\n";

            // obtain report data:

            String sql = "SELECT SUM(wei_des_net_r) AS _tot "
                    + "FROM s_tic "
                    + "WHERE NOT b_del AND b_tar AND fk_item = " + itemId + " "
                    + (ticketOrigin == 0 ? "" : "AND fk_tic_orig = " + ticketOrigin + " ")
                    + (ticketDestination == 0 ? "" : "AND fk_tic_dest = " + ticketDestination + " ")
                    + " AND dt BETWEEN ? AND ?";
            PreparedStatement preparedStatement = moSession.getStatement().getConnection().prepareStatement(sql);
            
            // reception during last interval days:
            
            preparedStatement.setDate(1, new java.sql.Date(SLibTimeUtils.addDate(now, 0, 0, -intvlDays).getTime()));
            preparedStatement.setDate(2, new java.sql.Date(now.getTime()));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double value = resultSet.getDouble("_tot") / unitsDivisor;

                    html += "<p>" + SLibUtils.textToHtml("Recepción " + (intvlDays == 1 ? "del último día" : "de los últimos " + intvlDays + " días") + ": " + 
                            decimalFormatVal.format(value) + " " + (isUnitsTon ? "ton" : unit.getCode()) + ".") + "</p>\n";
                }
            }
            
            // monthly weights, begining from first year or season backwards:

            int year;
            int month = reportFirstSeasonMonth;
            int rowAccums = SCliUtils.getRowAccumsForMonth(cutoffMonth, reportFirstSeasonMonth);
            
            double[][] tableValues = new double[years + 1][SLibTimeConsts.MONTHS]; // total weight per year, per month
            double[] tableTotals = new double[years + 1]; // total weight per year
            double[] tableAccums = new double[years + 1]; // accumulated weight per year up to today
            
            HashMap<Integer, Integer> tableMaxYearValuesMap = new HashMap<>(); // key: year index, value: row index

            for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {
                year = yearStart;

                if (reportFirstSeasonMonth + row > SLibTimeConsts.MONTHS) {
                    year++;
                }

                for (int col = 0; col < tableTotals.length; col++) {
                    // prepare month boundaries:
                    
                    Date start;
                    Date end;
                    
                    if (reportFirstMonthDay == 1) {
                        // standard calendar month:
                        start = SLibTimeUtils.createDate(year - col, month, 1);
                        end = SLibTimeUtils.getEndOfMonth(start);
                    }
                    else {
                        // customized operational month:
                        if (month > SLibTimeConsts.MONTH_JAN) {
                            start = SLibTimeUtils.createDate(year - col, month - 1, monthFirstDay);
                        }
                        else {
                            start = SLibTimeUtils.createDate(year - col - 1, SLibTimeConsts.MONTH_DEC, monthFirstDay);
                        }
                        end = SLibTimeUtils.createDate(year - col, month, monthFirstDay - 1);
                    }
                    
                    // validate scope of report:
                    
                    double value = 0;
                    boolean executeQuery = true;
                    
                    if (SLibTimeUtils.getDaysDiff(cutoff, end) < 0) {
                        end = cutoff;
                    }
                    if (SLibTimeUtils.getDaysDiff(cutoff, start) < 0) {
                        executeQuery = false;
                    }
                    
                    // extract data:
                    
                    if (executeQuery) {
                        preparedStatement.setDate(1, new java.sql.Date(start.getTime()));
                        preparedStatement.setDate(2, new java.sql.Date(end.getTime()));

                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                value = resultSet.getDouble("_tot") / unitsDivisor;
                            }
                        }
                    }
                    
                    // process data:
                    
                    tableValues[col][row] = value;
                    tableTotals[col] += value;

                    if (row <= rowAccums) {
                        tableAccums[col] += value;
                    }

                    Integer rowOfMaxValue = tableMaxYearValuesMap.get(col);
                    if (rowOfMaxValue == null || value > tableValues[col][rowOfMaxValue]) {
                        tableMaxYearValuesMap.put(col, row);
                    }
                }

                if (++month > SLibTimeConsts.MONTHS) {
                    month = SLibTimeConsts.MONTH_JAN;
                }
            }

            // HTML table:

            html += "<table>\n";

            // table header:

            ArrayList<String> headerCols = new ArrayList<>();
            headerCols.add("Mes" + (isCustomSeason ? "*" : ""));
            for (int col = yearStart; col >= yearEnd; col--) {
                if (reportFirstSeasonMonth == SLibTimeConsts.MONTH_JAN) {
                    headerCols.add("" + col);
                }
                else {
                    headerCols.add("" + col + "-" + (col + 1));
                }
            }

            html += "<tr>";
            for (int col = 0; col < headerCols.size(); col++) {
                String spaces;
                
                if (isUnitsTon) {
                    spaces = "&nbsp;&nbsp;";
                }
                else {
                    spaces = "&nbsp;&nbsp;&nbsp;&nbsp;";
                }
                
                html += "<th" + (col == 0 ? "" : " colspan='2'") + ">" + spaces + headerCols.get(col) + spaces + "</th>";
            }
            html += "</tr>\n";

            // table body:

            month = reportFirstSeasonMonth;

            for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {
                html += "<tr>";
                html += "<td class='colmonth'>" + months[month - 1] + ".</td>";

                for (int col = 0; col < tableTotals.length; col++) {
                    boolean isMax = tableMaxYearValuesMap.get(col) == row;
                    html += "<td class='coldata" + (isMax ? "max" : "") + "'>" + decimalFormatVal.format(tableValues[col][row]) + "</td>";
                    html += "<td class='coldatapct" + (isMax ? "max" : "") + "'>" + decimalFormatPct.format(tableTotals[col] == 0 ? 0 : tableValues[col][row] / tableTotals[col]) + "</td>";
                }

                if (++month > SLibTimeConsts.MONTHS) {
                    month = SLibTimeConsts.MONTH_JAN;
                }

                html += "</tr>\n";
            }

            // table footer:

            html += "<tr>";
            html += "<td class='colmonthtotal'><b>Temporada</b></td>";
            for (int col = 0; col < tableTotals.length; col++) {
                html += "<td class='coldatatotal'><b>" + decimalFormatVal.format(tableTotals[col]) + "</b></td>";
                html += "<td class='coldatapcttotal'><b>" + decimalFormatPct.format(1) + "</b></td>";
            }
            html += "</tr>\n";
            
            html += "<tr>";
            html += "<td>&nbsp;</td>";
            html += "</tr>\n";
            
            html += "<tr>";
            html += "<td class='colmonthaccum'>Acum. a " + months[cutoffMonth - 1] + ".</td>";
            for (int col = 0; col < tableAccums.length; col++) {
                html += "<td class='coldataaccum' " + (col == 0 ? "style='text-align: center;'" : "") + ">" + (col == 0 ? "N/A" : decimalFormatVal.format(tableAccums[col])) + "</td>";
                html += "<td class='coldatapctaccum'>" + (col == 0 ? "N/A" : decimalFormatPct.format(tableTotals[col] == 0 ? 0 : tableAccums[col] / tableTotals[col])) + "</td>";
            }
            html += "</tr>\n";

            html += "</table>\n";
            
            if (isCustomSeason) {
                html += "<p>" + SLibUtils.textToHtml("* Inicio de temporada: " + SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG)[seasonFirstMonth - 1] + "; día de corte mensual: " + (monthFirstDay - 1) + ".") + "</p>\n";
            }
            
            html += "<br>\n";
        }
        
        html += SSomMailUtils.composeSomMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
        
        return html;
    }
}
