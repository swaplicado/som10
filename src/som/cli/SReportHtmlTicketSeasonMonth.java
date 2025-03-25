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
public class SReportHtmlTicketSeasonMonth {
    
    private final SGuiSession moSession;
    
    public SReportHtmlTicketSeasonMonth(final SGuiSession session) {
        moSession = session;
    }
    
    /**
     * Generates report in HTML 5 format.
     * @param itemIds List of ID of items.
     * @param yearRef Year reference. Can be one out of two elegible types of values: 1) if it is a 4-digit year and greater or equal than 2001, it is the year to start from; otherwise 2) it is the number of history years besides current year.
     * @param intvlDays Interval days for invocation of this report mailer.
     * @param today Today date.
     * @param cutOff
     * @param ticOrig
     * @param ticDest
     * @return
     * @throws Exception 
     */
    public String generateReportHtml(final int[] itemIds, final int yearRef, final int intvlDays, final Date today, final Date cutOff, final int ticOrig, final int ticDest) throws Exception {
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
        
        for (int itemId : itemIds) {
            // read requested item for report:
            SDbItem item = new SDbItem();
            item.read(moSession, new int[] { itemId }); // read this way due to session is moduleless
            SDbUnit unit = new SDbUnit();
            unit.read(moSession, new int[] { item.getFkUnitId() }); // read this way due to session is moduleless
            SDbInputCategory inputCategory = new SDbInputCategory();
            inputCategory.read(moSession, new int[] { item.getFkInputCategoryId() }); // read this way due to session is moduleless

            int yearStart;
            int yearEnd;
            int years;
            
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

            // HTML heading 2 (input category subtitle, if necesary):

            if (lastInputCategoryId != inputCategory.getPkInputCategoryId()) {
                html += "<h3>" + SLibUtils.textToHtml("Categoría " + SLibUtils.textProperCase(inputCategory.getName())) + "</h3>\n";
                
                lastInputCategoryId = inputCategory.getPkInputCategoryId();
            }
            
            // HTML heading 3 (item subtitle):
            
            String name = SCliReportMailer.ItemDescriptions.get(itemId);
            html += "<h4>" + SLibUtils.textToHtml((name != null ? name : SLibUtils.textProperCase(item.getName())) + " (acumulado en " + unit.getCode() + ")") + "</h4>\n";

            // obtain report data:

            String sql = "SELECT SUM(wei_des_net_r) "
                    + "FROM s_tic "
                    + "WHERE NOT b_del AND b_tar AND fk_item = " + itemId + " "
                    + (ticOrig == 0 ? "" : "AND fk_tic_orig = " + ticOrig + " ")
                    + (ticDest == 0 ? "" : "AND fk_tic_dest = " + ticDest + " ")
                    + " AND dt BETWEEN ? AND ?";
            PreparedStatement preparedStatement = moSession.getStatement().getConnection().prepareStatement(sql);
            
            // reception during last interval days:
            
            preparedStatement.setDate(1, new java.sql.Date(SLibTimeUtils.addDate(today, 0, 0, -intvlDays).getTime()));
            preparedStatement.setDate(2, new java.sql.Date(today.getTime()));
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                html += "<p>" + SLibUtils.textToHtml("Recepción " + (intvlDays == 1 ? "último día" : "últimos " + intvlDays + " días") + ": " + 
                        SLibUtils.getDecimalFormatAmount().format(resultSet.getDouble(1)) + " " + unit.getCode()) + "</p>\n";
            }

            // monthly weights, begining from first year or season backwards:

            int year;
            int month = item.getStartingSeasonMonth();
            int rowAccumulated = getRowEquivalenceMonth(todayMonth, item.getStartingSeasonMonth());
            double[] tableTotals = new double[years + 1]; // total weight per year
            double[] tableAccumulated = new double[years + 1]; // accumulated weight today
            double[][] tableValues = new double[years + 1][SLibTimeConsts.MONTHS]; // total weight per year
            HashMap<Integer, Integer> maxValues = new HashMap<>(); // key: year index, value: row index

            for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {
                year = yearStart;

                if (item.getStartingSeasonMonth() + row > SLibTimeConsts.MONTHS) {
                    year++;
                }

                for (int col = 0; col < tableTotals.length; col++) {
                    boolean executeQuery = true;
                    Date start = SLibTimeUtils.createDate(year - col, month, 1);
                    Date end = SLibTimeUtils.getEndOfMonth(start);

                    if (cutOff != null) {
                        if (SLibTimeUtils.getDaysDiff(cutOff, end) < 0) {
                            end = cutOff;
                        }
                        if (SLibTimeUtils.getDaysDiff(cutOff, start) < 0) {
                            executeQuery = false;
                        }
                    }
                    
                    if (executeQuery) {
                        preparedStatement.setDate(1, new java.sql.Date(start.getTime()));
                        preparedStatement.setDate(2, new java.sql.Date(end.getTime()));

                        resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            double value = resultSet.getDouble(1);
                            tableTotals[col] += value;
                            tableValues[col][row] = value;
                            
                            if (row <= rowAccumulated) {
                                tableAccumulated[col] += value;
                            }
                            
                            Integer rowOfMaxValue = maxValues.get(col);
                            if (rowOfMaxValue == null || value > tableValues[col][rowOfMaxValue]) {
                                maxValues.put(col, row);
                            }
                        }
                    }
                    else {
                        double value = 0;
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
                html += "<th" + (col == 0 ? "" : " colspan='2'") + ">&nbsp&nbsp&nbsp " + headerCols.get(col) + " &nbsp&nbsp&nbsp</th>";
            }
            html += "</tr>\n";


            // table body:

            String[] months = SLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT); // month names for first column
            DecimalFormat decimalFormatPct = new DecimalFormat("#0.0%");

            month = item.getStartingSeasonMonth();

            for (int row = 0; row < SLibTimeConsts.MONTHS; row++) {

                html += "<tr>";
                html += "<td class='colmonth'>" + months[month - 1] + ".</td>";

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
            html += "<td class='colmonthtotal'><b>Temporada</b></td>";
            for (int col = 0; col < tableTotals.length; col++) {
                html += "<td class='coldatatotal'><b>" + SLibUtils.getDecimalFormatAmount().format(tableTotals[col]) + "</b></td>";
                html += "<td class='coldatapcttotal'><b>" + decimalFormatPct.format(1) + "</b></td>";
            }
            html += "</tr>\n";
            
            html += "<tr>";
            html += "<td>&nbsp</td>";
            html += "</tr>\n";
            
            html += "<tr>";
            html += "<td class='colmonthaccum'>Acum. a " + months[todayMonth - 1] + ".</td>";
            for (int col = 0; col < tableAccumulated.length; col++) {
                html += "<td class='coldataaccum' " + (col == 0 ? "style='text-align: center;'" : "") + ">" + (col == 0 ? "N/A" : SLibUtils.getDecimalFormatAmount().format(tableAccumulated[col])) + "</td>";
                html += "<td class='coldatapctaccum'>" + (col == 0 ? "N/A" : decimalFormatPct.format(tableTotals[col] == 0 ? 0 : tableAccumulated[col] / tableTotals[col])) + "</td>";
            }
            html += "</tr>\n";

            html += "</table>\n";
            html += "<br>\n";
        }
        
        
        html += SSomMailUtils.composeSomMailWarning();
        
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
