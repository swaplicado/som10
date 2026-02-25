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
 * @author Sergio Flores, Isabel Servín, Sergio Flores
 */
@Deprecated
public class SReportHtmlTicketSeasonMonthAlt_20260116 {
    
    public static final int MODE_UNIT_ITEM = 1;
    public static final int MODE_UNIT_TON = 2;
    
    private final SGuiSession moSession;
    
    public SReportHtmlTicketSeasonMonthAlt_20260116(final SGuiSession session) {
        moSession = session;
    }
    
    /**
     * Generates report in HTML 5 format.
     * @param itemIds List of ID of items.
     * @param yearRef Year reference. Can be one out of two elegible types of values: 1) if it is a 4-digit year and greater or equal than 2001, it is the year to start from; otherwise 2) it is the number of history years besides current year.
     * @param intvlDays Interval days for invocation of this report mailer.
     * @param today Today date.
     * @param ticOrig
     * @param ticDest
     * @param mode a) Unit of item; b) metric tons.
     * @return
     * @throws Exception 
     */
    public String generateReportHtml(final String[] itemIds, final int yearRef, final int intvlDays, final Date today, final int ticOrig, final int ticDest, final int mode) throws Exception {
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
                + " background-color: Aqua;"
                + "} "
                + "td.coldatapct {"
                + " text-align: center;"
                + " font-size: 0.75em;"
                + " font-family: sans-serif;"
                + "} "
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
                + "td.coldatapctmax {"
                + " text-align: center;"
                + " font-size: 0.75em;"
                + " font-family: sans-serif;"
                + " background-color: Aqua;"
                + "}\n"
                + "</style>\n";
        
        html += "</head>\n";
        
        // HTML body:
        
        html += "<body>\n";
        
        // define start and end date for report:
        
        int[] todayDigestion = SLibTimeUtils.digestDate(today);
        int todayYear = todayDigestion[0];
        int todayMonth = todayDigestion[1];
        
        // HTML heading 1 (main title):
        
        html += "<h2>" + SLibUtils.textToHtml("Histórico mensual recepción báscula") + "</h2>\n";
        html += "<p>Fecha-hora corte: " + SLibUtils.textToHtml(SLibUtils.DateFormatDatetime.format(today)) + "</p>\n";
        
        // process list of items for report:

        int lastInputCategoryId = 0; // to control when a new input category stages, to stand it out as a new title
        
        for (String itemId : itemIds) {
            // formatters:
            DecimalFormat decimalFormatPct = mode == MODE_UNIT_TON ? new DecimalFormat("#0%") : new DecimalFormat("#0.0%");
            DecimalFormat decimalFormatVal = mode == MODE_UNIT_TON ? SLibUtils.DecimalFormatInteger : SLibUtils.getDecimalFormatAmount();
            
            // read requested item for report:
            String[] ids = itemId.split("-");
            int convId = SLibUtils.parseInt(ids[0]);
            int altId = SLibUtils.parseInt(ids[1]);
            SDbItem item = new SDbItem();
            item.read(moSession, new int[] { convId }); // read this way due to session is moduleless
            SDbUnit unit = new SDbUnit();
            unit.read(moSession, new int[] { item.getFkUnitId() }); // read this way due to session is moduleless
            SDbInputCategory inputCategory = new SDbInputCategory();
            inputCategory.read(moSession, new int[] { item.getFkInputCategoryId() }); // read this way due to session is moduleless

            int yearStart;
            int yearEnd = 0;
            int years;
            
            yearStart = todayMonth < item.getStartingSeasonMonth() ? todayYear - 1 : todayYear;
            
            String sql = "SELECT MIN(YEAR(dt)) FROM s_alt_tic WHERE fk_item = " + altId + ";";
            ResultSet resultSet = moSession.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                yearEnd = resultSet.getInt(1);
            }

            if (yearEnd < yearRef) {
                yearEnd = yearRef;
                years = yearStart - yearRef;
            }
            else {
                years = yearStart - yearEnd;
            }

            if (yearEnd > yearStart) {
                throw new Exception("El año final no puede ser mayor al año inicial.");
            }

            // HTML heading 2 (input category subtitle, if necesary):

            if (lastInputCategoryId != inputCategory.getPkInputCategoryId()) {
                html += "<h3>" + SLibUtils.textToHtml("Aguacate convencional y orgánico")+ "</h3>\n";
                
                lastInputCategoryId = inputCategory.getPkInputCategoryId();
            }
            
            // HTML heading 3 (item subtitle):
            
            String name = SCliConsts.ItemNames.get(convId);
            html += "<h4>" + SLibUtils.textToHtml((name != null ? name : SLibUtils.textProperCase(item.getName())) + " (acumulado en " + (mode == MODE_UNIT_TON ? "ton" : unit.getCode()) + ")") + "</h4>\n";

            // obtain report data:


            sql = "SELECT " 
                    + "SUM(IF(b_alt = 0, wei_des_net_r, 0)) AS _tic, " 
                    + "SUM(IF(b_alt = 1, wei_des_net_r, 0)) AS _alt, " 
                    + "SUM(wei_des_net_r) _tot "
                    + "FROM ( " 
                    + "SELECT *, dt fecha FROM s_tic "
                    + "WHERE NOT b_alt AND fk_item = " + convId + " " 
                    + "UNION " 
                    + "SELECT a.*, t.dt fecha FROM s_alt_tic a "
                    + "INNER JOIN s_tic t ON t.id_tic = a.id_tic "
                    + "WHERE a.fk_item = " + altId + ") a " 
                    + "WHERE NOT b_del AND b_tar " 
                    + (ticOrig == 0 ? "" : "AND fk_tic_orig = " + ticOrig + " ")
                    + (ticDest == 0 ? "" : "AND fk_tic_dest = " + ticDest + " ")
                    + " AND fecha BETWEEN ? AND ?";
            PreparedStatement statementTotal = moSession.getStatement().getConnection().prepareStatement(sql);
            
            // reception during last interval days:
            
            statementTotal.setDate(1, new java.sql.Date(SLibTimeUtils.addDate(today, 0, 0, -intvlDays).getTime()));
            statementTotal.setDate(2, new java.sql.Date(today.getTime()));
            
            resultSet = statementTotal.executeQuery();
            if (resultSet.next()) {
                double value = resultSet.getDouble(1) / (mode == MODE_UNIT_TON ? 1000 : 1);
                
                html += "<p>" + SLibUtils.textToHtml("Recepción " + (intvlDays == 1 ? "último día" : "últimos " + intvlDays + " días") + ": " + 
                        decimalFormatVal.format(value) + " " + (mode == MODE_UNIT_TON ? "ton" : unit.getCode())) + "</p>\n";
            }

            // monthly weights, begining from first year or season backwards:

            int year;
            int month = item.getStartingSeasonMonth();
            int rowAccumulated = getRowEquivalenceMonth(todayMonth, item.getStartingSeasonMonth());
            double[][] tableValuesTic = new double[years + 1][SLibTimeConsts.MONTHS]; // total weight per year
            double[] tableTotalTic = new double[years + 1]; // total weight per year
            double[][] tableValuesAlt = new double[years + 1][SLibTimeConsts.MONTHS]; // total weight per year
            double[] tableTotalAlt = new double[years + 1]; // total weight per year
            double[] tableAccumulatedAlt = new double[years + 1]; // total weight per year
            double[][] tableValuesTotals = new double[years + 1][SLibTimeConsts.MONTHS]; // total weight per year
            double[] tableTotals = new double[years + 1]; // total weight per year
            double[] tableAccumulated = new double[years + 1]; // accumulated weight today
            HashMap<Integer, Integer> maxValuesTic = new HashMap<>(); // key: year index, value: row index
            HashMap<Integer, Integer> maxValuesAlt = new HashMap<>(); // key: year index, value: row index
            HashMap<Integer, Integer> maxValuesTot = new HashMap<>(); // key: year index, value: row index

            for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {
                year = yearStart;

                if (item.getStartingSeasonMonth() + row > SLibTimeConsts.MONTHS) {
                    year++;
                }

                for (int col = 0; col < tableTotals.length; col++) {
                    Date start = SLibTimeUtils.createDate(year - col, month, 1);
                    Date end = SLibTimeUtils.getEndOfMonth(start);

                    statementTotal.setDate(1, new java.sql.Date(start.getTime()));
                    statementTotal.setDate(2, new java.sql.Date(end.getTime()));
                    
                    resultSet = statementTotal.executeQuery();
                    if (resultSet.next()) {
                        double value = resultSet.getDouble(1) / (mode == MODE_UNIT_TON ? 1000 : 1);
                        tableTotalTic[col] += value;
                        tableValuesTic[col][row] = value;
                        
                        if (row <= rowAccumulated) {
                            tableAccumulated[col] += value;
                        }
                        
                        Integer rowOfMaxValue = maxValuesTic.get(col);
                        if (rowOfMaxValue == null || value > tableValuesTic[col][rowOfMaxValue]) {
                            maxValuesTic.put(col, row);
                        }
                        
                        value = resultSet.getDouble(2) / (mode == MODE_UNIT_TON ? 1000 : 1);
                        tableTotalAlt[col] += value;
                        tableValuesAlt[col][row] = value;
                        
                        if (row <= rowAccumulated) {
                            tableAccumulatedAlt[col] += value;
                        }
                        
                        Integer rowOfMaxValue3 = maxValuesAlt.get(col);
                        if (rowOfMaxValue3 == null || value > tableValuesAlt[col][rowOfMaxValue3]) {
                            maxValuesAlt.put(col, row);
                        }
                        
                        value = resultSet.getDouble(3) / (mode == MODE_UNIT_TON ? 1000 : 1);
                        tableTotals[col] += value;
                        tableValuesTotals[col][row] = value;

                        Integer rowOfMaxValue2 = maxValuesTot.get(col);
                        if (rowOfMaxValue2 == null || value > tableValuesTotals[col][rowOfMaxValue2]) {
                            maxValuesTot.put(col, row);
                        }
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
                String spaces;
                
                if (mode == MODE_UNIT_TON) {
                    spaces = "&nbsp;&nbsp;";
                }
                else {
                    spaces = "&nbsp;&nbsp;&nbsp;&nbsp;";
                }
                
                html += "<th" + (col == 0 ? "" : " colspan='4'") + ">" + spaces + headerCols.get(col) + spaces + "</th>";
            }
            html += "</tr>\n";
            
            html += "<tr><td></td>";
            for (int col = 0; col < headerCols.size() - 1; col++) {
                html += "<td align='center'>&nbsp;" + SLibUtils.textToHtml("Convenc.") + "&nbsp;</td>"
                        + "<td align='center'>&nbsp;" + SLibUtils.textToHtml("Orgánico") + "&nbsp;</td>"
                        + "<td align='center'>&nbsp;" + SLibUtils.textToHtml("Total") + "&nbsp;</td>"
                        + "<td align='center'>&nbsp;" + SLibUtils.textToHtml("%") + "&nbsp;</td>";
            }
            html += "</tr>\n";

            // table body:

            String[] months = SLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT); // month names for first column

            month = item.getStartingSeasonMonth();

            for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {

                html += "<tr>";
                html += "<td class='colmonth'>" + months[month - 1] + ".</td>";

                for (int col = 0; col < tableTotals.length; col++) {
                    boolean isMax = maxValuesTot.get(col) == row;
                    html += "<td class='coldata" + (isMax ? "max" : "") + "'>" + decimalFormatVal.format(tableValuesTic[col][row]) + "</td>";
                    html += "<td class='coldata" + (isMax ? "max" : "") + "'>" + decimalFormatVal.format(tableValuesAlt[col][row]) + "</td>";
                    html += "<td class='coldata" + (isMax ? "max" : "") + "'>" + decimalFormatVal.format(tableValuesTotals[col][row]) + "</td>";
                    html += "<td class='coldatapct" + (isMax ? "max" : "") + "'>" + decimalFormatPct.format(tableTotals[col] == 0 ? 0 : tableValuesTotals[col][row] / tableTotals[col]) + "</td>";
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
                html += "<td class='coldatatotal'><b>" + decimalFormatVal.format(tableTotalTic[col]) + "</b></td>";
                html += "<td class='coldatatotal'><b>" + decimalFormatVal.format(tableTotalAlt[col]) + "</b></td>";
                html += "<td class='coldatatotal'><b>" + decimalFormatVal.format(tableTotals[col]) + "</b></td>";
                html += "<td class='coldatapcttotal'><b>" + decimalFormatPct.format(1) + "</b></td>";
            }
            html += "</tr>\n";
            
            html += "<tr>";
            html += "<td>&nbsp;</td>";
            html += "</tr>\n";
            
            html += "<tr>";
            html += "<td class='colmonthaccum'>Acum. a " + months[todayMonth - 1] + ".</td>";
            for (int col = 0; col < tableAccumulated.length; col++) {
                html += "<td class='coldataaccum' " + (col == 0 ? "style='text-align: center;'" : "") + ">" + (col == 0 ? "N/A" : decimalFormatVal.format(tableAccumulated[col])) + "</td>";
                html += "<td class='coldataaccum' " + (col == 0 ? "style='text-align: center;'" : "") + ">" + (col == 0 ? "N/A" : decimalFormatVal.format(tableAccumulatedAlt[col])) + "</td>";
                html += "<td class='coldataaccum' " + (col == 0 ? "style='text-align: center;'" : "") + ">" + (col == 0 ? "N/A" : decimalFormatVal.format(tableAccumulated[col] + tableAccumulatedAlt[col])) + "</td>";
                html += "<td class='coldatapctaccum'>" + (col == 0 ? "N/A" : decimalFormatPct.format(tableTotals[col] == 0 ? 0 : (tableAccumulated[col] + tableAccumulatedAlt[col]) / tableTotals[col])) + "</td>";
            }
            html += "</tr>\n";

            html += "</table>\n";
            html += "<br>\n";
        }
        
        html += SSomMailUtils.composeSomAlternativeMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
        
        return html;
    }
    
    /*
    Devuelve el renglón equivalente para cierto mes a partir del mes de inicio de temporada
    */
    private int getRowEquivalenceMonth(int month, int startingSeasonMonth) {
        if (month >= startingSeasonMonth) {
            return month - startingSeasonMonth;
        }
        else {
            return month + SLibTimeConsts.MONTHS - startingSeasonMonth; 
        }
    }
}
