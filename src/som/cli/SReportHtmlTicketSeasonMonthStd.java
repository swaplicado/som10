/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.mod.mat.db.SExwUtils;
import som.mod.som.db.SDbInputCategory;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbUnit;
import static som.mod.som.db.SOpCalendarUtils.createOpCalendarsMap;
import static som.mod.som.db.SOpCalendarUtils.getOpCalendarId;
import som.mod.som.db.SSomMailUtils;
import som.mod.som.db.SSomUtils;

/**
  * Generación de las tablas comparativas históricas mensuales de recepción de fruta en base a meses con cierre de acuerdo el calendario operativo aplicable, si aplica, o a meses con cierre fijo los días 18.
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
     * @param useOpCalendars Flag to use operative calendars.
     * @param cutoff Cutoff date.
     * @param now The very moment of processing.
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @param mode a) Unit of measure of item; b) Metric tons.
     * @param addExwStock Add stock in external warehouses.
     * @return
     * @throws Exception
     */
    public String generateReportHtml(final int[] itemIds, final int yearRef, final int intvlDays, 
            final int seasonFirstMonth, final int monthFirstDay, final boolean useOpCalendars,
            final Date cutoff, final Date now, final int ticketOrigin, final int ticketDestination, final int mode, final boolean addExwStock) throws Exception {
        // HTML:
        
        String html = "<html>\n";
        
        html += SCliUtils.composeHtmlHeadForSeasonMonth();
        
        // HTML body:
        
        html += "<body>\n";
        
        // setup control variables for report:
        
        int[] cutoffDigestion = SLibTimeUtils.digestDate(cutoff);
        int cutoffYear = cutoffDigestion[0];
        int cutoffMonth = cutoffDigestion[1];
        int cutoffDay = cutoffDigestion[2];
        boolean isCustomMonth = monthFirstDay > 1;
        boolean isUnitsTon = mode == MODE_UNIT_TON;
        double unitsDivisor = isUnitsTon ? 1000 : 1;
        String[] months = SLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT); // month names for first column in table of each item
        
        if (isCustomMonth && cutoffDay >= monthFirstDay) {
            cutoffMonth++;
        }
        
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
        HashMap<Integer, ArrayList<Integer>> opCalendarsMap = null;
        
        if (useOpCalendars) {
            opCalendarsMap = createOpCalendarsMap(moSession);
        }
        
        for (int itemId : itemIds) {
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
                html += "<h3>" + SLibUtils.textToHtml("Categoría: " + SLibUtils.textProperCase(inputCategory.getName())) + "</h3>\n";
                
                lastInputCategoryId = inputCategory.getPkInputCategoryId();
            }
            
            // HTML heading 3 (item subtitle):
            
            String name = SCliConsts.ItemNames.get(itemId);
            html += "<h4>" + SLibUtils.textToHtml((name != null ? name : SLibUtils.textProperCase(item.getName())) + " (valores en " + (isUnitsTon ? SCliConsts.TON : unit.getCode()) + ")") + "</h4>\n";

            // obtain report data:
            
            String sql;
            int opCalendarId = 0;
            PreparedStatement prepStatementWeights = null;
            PreparedStatement prepStatementMonths = null;
            
            sql = "SELECT SUM(wei_des_net_r) AS _tot "
                    + "FROM s_tic "
                    + "WHERE NOT b_del AND b_tar AND fk_item = " + itemId + " "
                    + (ticketOrigin == 0 ? "" : "AND fk_tic_orig = " + ticketOrigin + " ")
                    + (ticketDestination == 0 ? "" : "AND fk_tic_dest = " + ticketDestination + " ")
                    + "AND dt BETWEEN ? AND ?;";
            prepStatementWeights = moSession.getStatement().getConnection().prepareStatement(sql);
            
            if (useOpCalendars) {
                opCalendarId = getOpCalendarId(opCalendarsMap, itemId);
                
                if (opCalendarId == 0) {
                    throw new Exception("No se encontró un calendario operativo aplicable al ítem '" + name + "' (ID " + itemId + ").");
                }
                
                sql = "SELECT month_sta, month_end "
                        + "FROM su_op_cal_year_month "
                        + "WHERE id_op_cal = " + opCalendarId + " AND id_year = ? AND id_month = ?;";
                prepStatementMonths = moSession.getStatement().getConnection().prepareStatement(sql);
            }
            
            // reception during last interval days:
            
            prepStatementWeights.setDate(1, new java.sql.Date(SLibTimeUtils.addDate(now, 0, 0, -intvlDays).getTime()));
            prepStatementWeights.setDate(2, new java.sql.Date(now.getTime()));
            
            try (ResultSet resultSet = prepStatementWeights.executeQuery()) {
                if (resultSet.next()) {
                    double value = resultSet.getDouble("_tot") / unitsDivisor;

                    html += "<p>" + SLibUtils.textToHtml("Recepción " + (intvlDays == 1 ? "del último día" : "de los últimos " + intvlDays + " días") + ": " + 
                            decimalFormatVal.format(value) + " " + (isUnitsTon ? SCliConsts.TON : unit.getCode()) + ".") + "</p>\n";
                }
            }
            
            // monthly weights, from month #1 to #12, begining from first year (or season) backwards:

            int year;
            int month = reportFirstSeasonMonth;
            int maxRowToAccum = SCliUtils.getRowIndexLimitForAccumValues(cutoffMonth, reportFirstSeasonMonth); // maximum row to accumulate values throughout all years (or seasons)
            
            double[][] tableValues = new double[years][SLibTimeConsts.MONTHS]; // total weight per year (or season), per month
            double[] tableTotals = new double[years]; // total weight per year (or season)
            double[] tableAccums = new double[years]; // accumulated weight per year (or season) up to today
            
            HashMap<Integer, Integer> tableRowOfMaxValuesMap = new HashMap<>(); // key: year index, value: row index of maximum value in year (or season)

            for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {
                year = yearStart;

                if (!useOpCalendars) {
                    // when not using operating calendars, increment year when December is overrun!
                    if (reportFirstSeasonMonth + row > SLibTimeConsts.MONTHS) {
                        year++; // season continues in next calendar year
                    }
                }

                for (int col = 0; col < tableTotals.length; col++) {
                    // prepare month boundaries:
                    
                    Date start = null;
                    Date end = null;
                    
                    if (reportFirstMonthDay == 1) {
                        // standard calendar month:
                        start = SLibTimeUtils.createDate(year - col, month, 1);
                        end = SLibTimeUtils.getEndOfMonth(start);
                    }
                    else {
                        if (useOpCalendars) {
                            // operative calendar month:
                            prepStatementMonths.setInt(1, year - col);
                            prepStatementMonths.setInt(2, row + 1); // first operating month is 1
                            
                            try (ResultSet resultSet = prepStatementMonths.executeQuery()) {
                                if (resultSet.next()) {
                                    start = resultSet.getDate("month_sta");
                                    end = resultSet.getDate("month_end");
                                }
                                else {
                                    throw new Exception("No se encontró el mes operativo " + SLibUtils.DecimalFormatCalendarMonth.format(row + 1) + ", del año " + (year - col) + ", del calendario operativo de ID " + opCalendarId + ".");
                                }
                            }
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
                    }
                    
                    // validate scope of current iteration:
                    
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
                        prepStatementWeights.setDate(1, new java.sql.Date(start.getTime()));
                        prepStatementWeights.setDate(2, new java.sql.Date(end.getTime()));

                        try (ResultSet resultSet = prepStatementWeights.executeQuery()) {
                            if (resultSet.next()) {
                                value = resultSet.getDouble("_tot") / unitsDivisor;
                            }
                        }
                    }
                    
                    // process data:
                    
                    tableValues[col][row] = value;
                    tableTotals[col] += value;

                    if (row <= maxRowToAccum) {
                        tableAccums[col] += value;
                    }

                    Integer rowOfMaxValue = tableRowOfMaxValuesMap.get(col);
                    if (rowOfMaxValue == null || value > tableValues[col][rowOfMaxValue]) {
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
            headerCols.add("Mes" + (isCustomMonth ? "*" : ""));
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
                    if (col == 0 && row > maxRowToAccum) {
                        // empty values for upcoming months in current season:
                        html += "<td class='coldata'>&nbsp;</td>";
                        html += "<td class='coldatapct'>&nbsp;</td>";
                    }
                    else {
                        // correspondig and available value:
                        boolean isMax = tableRowOfMaxValuesMap.get(col) == row;
                        html += "<td class='coldata" + (isMax ? "max" : "") + "'>" + decimalFormatVal.format(tableValues[col][row]) + "</td>";
                        html += "<td class='coldatapct" + (isMax ? "max" : "") + "'>" + decimalFormatPct.format(tableTotals[col] == 0 ? 0 : tableValues[col][row] / tableTotals[col]) + "</td>";
                    }
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
                html += "<td class='coldatapcttotal'><b>" + decimalFormatPct.format(1) + "</b></td>"; // 100%
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
            
            // custom month warning:
            
            if (isCustomMonth) {
                String monthClosing = useOpCalendars ? "según el calendario operativo aplicable (cierre mes actual: " + SLibUtils.DecimalFormatCalendarMonth.format(monthFirstDay - 1) + "/" + months[cutoffMonth - 1] + "./" + cutoffYear + ")" : "los días " + SLibUtils.DecimalFormatCalendarMonth.format(monthFirstDay - 1) + " de cada mes";
                html += "<small>" + SLibUtils.textToHtml("* Inicio de temporada: " + SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG)[seasonFirstMonth - 1] + ". Día de cierre mensual: " + monthClosing + ".") + "</small>\n";
            }
            
            // progress vs. last season:
            
            if (tableTotals.length >= 2) {
                String progress = "Recepción de la temporada actual vs. la anterior: ";
                if (tableTotals[1] == 0) {
                    progress += "N/A.";
                }
                else {
                    progress += SLibUtils.DecimalFormatPercentage1D.format(tableTotals[0] / tableTotals[1]) + ".";
                }
                html += "<p>" + SLibUtils.textToHtml(progress) + "</p>\n";
            }
            
            html += "<br>\n";
        }
        
        if (addExwStock) {
            // add HTML table with stock in external warehouses:
            html += generateExwStockHtmlTable(itemIds, cutoff, isUnitsTon);
        }
        
        html += SSomMailUtils.composeSomMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
        
        return html;
    }
    
    private String generateExwStockHtmlTable(final int[] itemIds, final Date cutoff, final boolean isUnitsTon) throws Exception {
        String html = "";
        double unitsDivisor = isUnitsTon ? 1000 : 1;
        DecimalFormat decimalFormatVal = isUnitsTon ? SLibUtils.DecimalFormatInteger : SLibUtils.getDecimalFormatAmount();
        Date exwStart = SExwUtils.getExwStart(moSession);
        
        String sql = "SELECT "
                + "ict.name, ict.id_inp_ct, "
                + "i.name, i.id_item, "
                + "u.code, u.id_unit, "
                + "exw.name, exw.id_exw_fac, "
                + "SUM(s.flow_prev) AS open_stock, "
                + "SUM(IF(flow = '" + SExwUtils.INFLOW + "', s.flow_curr, 0.0)) AS flow_in, "
                + "-SUM(IF(flow = '" + SExwUtils.OUTFLOW + "', s.flow_curr, 0.0)) AS flow_out, " // outflows is negative, so render it as positive!
                + "SUM(s.flow_prev + s.flow_curr) AS stock "
                + "FROM ("
                + SExwUtils.composeSqlExwStock(0, 0, SExwUtils.EXW_FAC_UNDEF, exwStart, cutoff)
                + ") AS s " // stock
                + ""
                + "INNER JOIN su_item AS i ON i.id_item = s.id_item "
                + "INNER JOIN su_inp_ct AS ict ON ict.id_inp_ct = i.fk_inp_ct "
                + "INNER JOIN su_unit AS u ON u.id_unit = s.id_unit "
                + "INNER JOIN mu_exw_fac AS exw ON exw.id_exw_fac = s.id_exw_fac "
                + "WHERE i.id_item IN (" + Arrays.stream(itemIds).mapToObj(String::valueOf).collect(Collectors.joining(", ")) + ") "
                + "GROUP BY "
                + "ict.name, ict.id_inp_ct, i.name, i.id_item, u.code, u.id_unit, "
                + "exw.name, exw.id_exw_fac "
                + "HAVING stock <> 0.0 "
                + "ORDER BY "
                + "ict.name, ict.id_inp_ct, i.name, i.id_item, u.code, u.id_unit, "
                + "exw.name, exw.id_exw_fac"
                + ";";
        
        html += "<hr>\n";
        html += "<h3>" + SLibUtils.textToHtml("Existencias en almacenes externos") + "</h3>\n";
        
        try (Statement statement = moSession.getStatement().getConnection().createStatement()) {
            html += "<h4>" + SLibUtils.textToHtml("Materias primas" + (isUnitsTon ? " (valores en " + SCliConsts.TON + ")" : "")) + "</h4>\n";
            
            html += "<table>\n";
            
            // table header:
            
            html += "<tr>";
            
            String[] headers = new String[] { "Categoría", "Materia prima", "Almacén externo", /*"Inv. inicial", "Entradas", "Salidas",*/ "Existencias*", "Unidad" };
            for (String header : headers) {
                html += "<th>&nbsp;" + SLibUtils.textToHtml(header) + "&nbsp;</th>";
            }
            
            html += "</tr>\n";
            
            String[] valueCols = new String[] { /*"open_stock", "flow_in", "flow_out",*/ "stock" };
            
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                html += "<tr>";
                
                html += "<td class='colmonth'>" + SLibUtils.textToHtml(SLibUtils.textProperCase(resultSet.getString("ict.name"))) + "</td>";
                html += "<td class='colmonth'>" + SLibUtils.textToHtml(SCliConsts.ItemNames.get(resultSet.getInt("i.id_item"))) + "</td>";
                html += "<td class='colmonth'>" + SLibUtils.textToHtml(SLibUtils.textProperCase(resultSet.getString("exw.name"))) + "</td>";
                
                for (String valueCol : valueCols) {
                    double value = resultSet.getDouble(valueCol) / unitsDivisor;
                    html += "<td class='coldata'" + (value < 0 ? " style='color: red;'" : "") + ">" + decimalFormatVal.format(value) + "</td>";
                }
                
                html += "<td class='colmonth'>" + SLibUtils.textToHtml(isUnitsTon ? SCliConsts.TON : resultSet.getString("u.code")) + "</td>";
                
                html += "</tr>\n";
            }
            
            html += "</table>\n";
            
            html += "<small>" + SLibUtils.textToHtml("* Día de corte de existencias: " + SSomUtils.DateFormatGui.format(cutoff) + ".") + "</small>\n";
            
            html += "<p>" + SLibUtils.textToHtml("Existencias determinadas en base a boletos de entrada y de salida de almacenes externos.") + "</p>\n";
            
            html += "<br>\n";
        }
        
        return html;
    }
}
