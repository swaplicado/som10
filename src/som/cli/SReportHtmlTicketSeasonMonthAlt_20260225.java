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
  * Generación de las tablas comparativas históricas mensuales de recepción de fruta en base a meses con cierre fijo los días 18.
 * @author Sergio Flores, Isabel Servín, Sergio Flores
 */
public class SReportHtmlTicketSeasonMonthAlt_20260225 {
    
    /** Report in unit of measure of item. */
    public static final int MODE_UNIT_ITEM = 1;
    /** Report in metric tons. */
    public static final int MODE_UNIT_TON = 2;
    
    private final SGuiSession moSession;
    
    public SReportHtmlTicketSeasonMonthAlt_20260225(final SGuiSession session) {
        moSession = session;
    }
    
    /**
     * Generates report in HTML 5 format.
     * @param itemIdsPairs Array of pairs of ID of items to report.
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
    public String generateReportHtml(final String[] itemIdsPairs, final int yearRef, final int intvlDays, final int seasonFirstMonth, final int monthFirstDay, final Date cutoff, final Date now, final int ticketOrigin, final int ticketDestination, final int mode) throws Exception {
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
        
        // define start and end date for report:
        
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
        DecimalFormat decimalFormatPct = isUnitsTon ? new DecimalFormat("#0%") : new DecimalFormat("#0.0%");
        DecimalFormat decimalFormatVal = isUnitsTon ? SLibUtils.DecimalFormatInteger : SLibUtils.getDecimalFormatAmount();
        
        for (String itemIdsPair : itemIdsPairs) {
            // read requested item for report:
            String[] ids = itemIdsPair.split("-");
            int itemCnvId = SLibUtils.parseInt(ids[0]); // conventional item
            int itemAltId = SLibUtils.parseInt(ids[1]); // alternative item, e.g., organic
            SDbItem item = new SDbItem();
            item.read(moSession, new int[] { itemCnvId }); // read this way due to session is moduleless
            SDbUnit unit = new SDbUnit();
            unit.read(moSession, new int[] { item.getFkUnitId() }); // read this way due to session is moduleless
            SDbInputCategory inputCategory = new SDbInputCategory();
            inputCategory.read(moSession, new int[] { item.getFkInputCategoryId() }); // read this way due to session is moduleless

            int reportFirstSeasonMonth = seasonFirstMonth > 0 ? seasonFirstMonth : (item.getStartingSeasonMonth() > 0 ? item.getStartingSeasonMonth() : SLibTimeConsts.MONTH_JAN);
            int reportFirstMonthDay = monthFirstDay > 0 ? monthFirstDay : 1;
            
            int yearStart = cutoffMonth < item.getStartingSeasonMonth() ? cutoffYear - 1 : cutoffYear;
            int yearEnd = 0;
            int years = 0;
            
            if (yearRef >= SLibTimeConsts.YEAR_MIN) {
                // year reference has the year to start from:
                
                // get first receptin year of alternative item to check if processing must start later than reference year:
                int firstYearAlt = 0;
                String sql = "SELECT MIN(YEAR(dt)) FROM s_alt_tic WHERE fk_item = " + itemAltId + ";";
                try (ResultSet resultSet = moSession.getStatement().executeQuery(sql)) {
                    if (resultSet.next()) {
                        firstYearAlt = resultSet.getInt(1);
                    }
                }
                
                if (firstYearAlt > yearRef) {
                    yearEnd = firstYearAlt;
                }
                else {
                    yearEnd = yearRef;
                }
                
                years = yearStart - yearEnd + 1; // +1 to count years properly
            }
            else {
                // year reference has a number of history years:
                
                years = yearRef + 1; // +1 to consider current year
                yearEnd = yearStart - years;
            }
            
            // validat years:
            if (yearEnd > yearStart) {
                throw new Exception("El año final, " + yearEnd + ", no puede ser mayor al año inicial, " + yearStart + ".");
            }

            // HTML heading 2 (input category subtitle, if necesary):

            if (lastInputCategoryId != inputCategory.getPkInputCategoryId()) {
                html += "<h3>" + SLibUtils.textToHtml("Categoría: " + SCliConsts.ItemsPairsNames.get(itemIdsPair))+ "</h3>\n";
                
                lastInputCategoryId = inputCategory.getPkInputCategoryId();
            }
            
            // HTML heading 3 (item subtitle):
            
            String name = SCliConsts.ItemNames.get(itemCnvId);
            html += "<h4>" + SLibUtils.textToHtml((name != null ? name : SLibUtils.textProperCase(item.getName())) + " (valores en " + (isUnitsTon ? "ton" : unit.getCode()) + ")") + "</h4>\n";

            // obtain report data:

            String sql = "SELECT " 
                    + "SUM(IF(x.b_alt = 0, x.wei_des_net_r, 0.0)) AS _cnv, " 
                    + "SUM(IF(x.b_alt = 1, x.wei_des_net_r, 0.0)) AS _alt, " 
                    + "SUM(x.wei_des_net_r) AS _tot "
                    + "FROM (" 
                    + "SELECT t_.b_alt, t_.wei_des_net_r, t_.b_del, t_.b_tar, t_.fk_tic_orig, t_.fk_tic_dest, t_.dt "
                    + "FROM s_tic AS t_ " // most tickets are conventional, some alternative
                    + "WHERE NOT t_.b_alt AND t_.fk_item = " + itemCnvId + " " 
                    + "UNION " 
                    + "SELECT at.b_alt, at.wei_des_net_r, at.b_del, at.b_tar, at.fk_tic_orig, at.fk_tic_dest, tt.dt " // date taken from its corresponding conventional ticket!
                    + "FROM s_alt_tic AS at " // all tickets are alternative (mirrored from their corresponding conventional one)
                    + "INNER JOIN s_tic AS tt ON tt.id_tic = at.id_tic "
                    + "WHERE at.fk_item = " + itemAltId + ") x "
                    + "WHERE NOT x.b_del AND x.b_tar " 
                    + (ticketOrigin == 0 ? "" : "AND x.fk_tic_orig = " + ticketOrigin + " ")
                    + (ticketDestination == 0 ? "" : "AND x.fk_tic_dest = " + ticketDestination + " ")
                    + "AND x.dt BETWEEN ? AND ?;";
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

            // monthly weights, from month #1 to #12, begining from first year (or season) backwards:

            int year;
            int month = reportFirstSeasonMonth;
            int maxRowToAccum = SCliUtils.getRowIndexLimitForAccumValues(cutoffMonth, reportFirstSeasonMonth); // maximum row to accumulate values throughout all years (or seasons)
            
            // for conventional tickets:
            double[][] tableCnvValues = new double[years][SLibTimeConsts.MONTHS]; // total weight per year (or season), per month
            double[] tableCnvTotals = new double[years]; // total weight per year (or season)
            double[] tableCnvAccums = new double[years]; // accumulated weight per year (or season) up to today
            
            // for alternative tickets:
            double[][] tableAltValues = new double[years][SLibTimeConsts.MONTHS]; // total weight per year (or season), per month
            double[] tableAltTotals = new double[years]; // total weight per year (or season)
            double[] tableAltAccums = new double[years]; // accumulated weight per year (or season) up to today
            
            // for total (all) tickets:
            double[][] tableTotValues = new double[years][SLibTimeConsts.MONTHS]; // total weight per year (or season), per month
            double[] tableTotTotals = new double[years]; // total weight per year (or season)
            double[] tableTotAccums = new double[years]; // accumulated weight per year (or season) up to today
            
            HashMap<Integer, Integer> tableRowOfMaxValuesMap = new HashMap<>(); // key: year index, value: row index of maximum value in year (or season)

            for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {
                year = yearStart;

                if (reportFirstSeasonMonth + row > SLibTimeConsts.MONTHS) {
                    year++;
                }

                for (int col = 0; col < tableTotTotals.length; col++) {
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
                    
                    double valueCnv = 0;
                    double valueAlt = 0;
                    double valueTot = 0;
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
                                valueCnv = resultSet.getDouble("_cnv") / unitsDivisor;
                                
                                valueAlt = resultSet.getDouble("_alt") / unitsDivisor;
                                
                                valueTot = resultSet.getDouble("_tot") / unitsDivisor;
                            }
                        }
                    }
                    
                    // process data:
                    
                    tableCnvValues[col][row] = valueCnv;
                    tableCnvTotals[col] += valueCnv;

                    tableAltValues[col][row] = valueAlt;
                    tableAltTotals[col] += valueAlt;

                    tableTotValues[col][row] = valueTot;
                    tableTotTotals[col] += valueTot;

                    if (row <= maxRowToAccum) {
                        tableCnvAccums[col] += valueCnv;
                        
                        tableAltAccums[col] += valueAlt;
                        
                        tableTotAccums[col] += valueTot;
                    }

                    Integer rowOfMaxTotValue = tableRowOfMaxValuesMap.get(col);
                    if (rowOfMaxTotValue == null || valueTot > tableTotValues[col][rowOfMaxTotValue]) {
                        tableRowOfMaxValuesMap.put(col, row);
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
                
                html += "<th" + (col == 0 ? "" : " colspan='4'") + ">" + spaces + headerCols.get(col) + spaces + "</th>";
            }
            html += "</tr>\n";
            
            // table sub-header:

            html += "<tr><td></td>";
            for (int col = 0; col < headerCols.size() - 1; col++) {
                html += "<td align='center'>&nbsp;" + SLibUtils.textToHtml("Convenc.") + "&nbsp;</td>"
                        + "<td align='center'>&nbsp;" + SLibUtils.textToHtml("Orgánico") + "&nbsp;</td>"
                        + "<td align='center'>&nbsp;" + SLibUtils.textToHtml("Total") + "&nbsp;</td>"
                        + "<td align='center'>&nbsp;" + SLibUtils.textToHtml("%") + "&nbsp;</td>";
            }
            html += "</tr>\n";

            // table body:

            month = reportFirstSeasonMonth;

            for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {
                html += "<tr>";
                html += "<td class='colmonth'>" + months[month - 1] + ".</td>";

                for (int col = 0; col < tableTotTotals.length; col++) {
                    boolean isMax = tableRowOfMaxValuesMap.get(col) == row;
                    html += "<td class='coldata" + (isMax ? "max" : "") + "'>" + decimalFormatVal.format(tableCnvValues[col][row]) + "</td>";
                    html += "<td class='coldata" + (isMax ? "max" : "") + "'>" + decimalFormatVal.format(tableAltValues[col][row]) + "</td>";
                    html += "<td class='coldata" + (isMax ? "max" : "") + "'>" + decimalFormatVal.format(tableTotValues[col][row]) + "</td>";
                    html += "<td class='coldatapct" + (isMax ? "max" : "") + "'>" + decimalFormatPct.format(tableTotTotals[col] == 0 ? 0 : tableTotValues[col][row] / tableTotTotals[col]) + "</td>";
                }

                if (++month > SLibTimeConsts.MONTHS) {
                    month = SLibTimeConsts.MONTH_JAN;
                }

                html += "</tr>\n";
            }

            // table footer:

            html += "<tr>";
            html += "<td class='colmonthtotal'><b>Temporada</b></td>";
            for (int col = 0; col < tableTotTotals.length; col++) {
                html += "<td class='coldatatotal'><b>" + decimalFormatVal.format(tableCnvTotals[col]) + "</b></td>";
                html += "<td class='coldatatotal'><b>" + decimalFormatVal.format(tableAltTotals[col]) + "</b></td>";
                html += "<td class='coldatatotal'><b>" + decimalFormatVal.format(tableTotTotals[col]) + "</b></td>";
                html += "<td class='coldatapcttotal'><b>" + decimalFormatPct.format(1) + "</b></td>";
            }
            html += "</tr>\n";
            
            html += "<tr>";
            html += "<td>&nbsp;</td>";
            html += "</tr>\n";
            
            html += "<tr>";
            html += "<td class='colmonthaccum'>Acum. a " + months[cutoffMonth - 1] + ".</td>";
            for (int col = 0; col < tableCnvAccums.length; col++) {
                html += "<td class='coldataaccum' " + (col == 0 ? "style='text-align: center;'" : "") + ">" + (col == 0 ? "N/A" : decimalFormatVal.format(tableCnvAccums[col])) + "</td>";
                html += "<td class='coldataaccum' " + (col == 0 ? "style='text-align: center;'" : "") + ">" + (col == 0 ? "N/A" : decimalFormatVal.format(tableAltAccums[col])) + "</td>";
                html += "<td class='coldataaccum' " + (col == 0 ? "style='text-align: center;'" : "") + ">" + (col == 0 ? "N/A" : decimalFormatVal.format(tableTotAccums[col])) + "</td>";
                html += "<td class='coldatapctaccum'>" + (col == 0 ? "N/A" : decimalFormatPct.format(tableTotTotals[col] == 0 ? 0 : tableTotAccums[col] / tableTotTotals[col])) + "</td>";
            }
            html += "</tr>\n";

            html += "</table>\n";
            
            if (isCustomSeason) {
                html += "<p>" + SLibUtils.textToHtml("* Inicio de temporada: " + SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG)[seasonFirstMonth - 1] + "; día de cierre mensual: " + (monthFirstDay - 1) + ".") + "</p>\n";
            }
            
            html += "<br>\n";
        }
        
        html += SSomMailUtils.composeSomAlternativeMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
        
        return html;
    }
}
