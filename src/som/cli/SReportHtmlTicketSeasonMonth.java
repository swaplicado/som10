/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
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
    
    public String generateReportHtml(final int itemId, final int years) throws Exception {
        // read requested item for report:
        SDbItem item = new SDbItem();
        item.read(moSession, new int[] { itemId });
        
        // define start and end date for report:
        Date dateCur = new Date();
        int[] todayDigestion = SLibTimeUtils.digestDate(dateCur);
        int curYear = todayDigestion[0];
        int curMonth = todayDigestion[1];
        int yearStart = curMonth < item.getStartingSeasonMonth() ? curYear - 1 : curYear;
        int yearEnd = yearStart - years;
        
        // define html table header:
        ArrayList<String> headerCols = new ArrayList<>();
        headerCols.add("Mes");
        for (int year = yearStart; year >= yearEnd; year--) {
            if (item.getStartingSeasonMonth() == 1) {
                headerCols.add("" + year);
            }
            else {
                headerCols.add("" + year + "-" + (year + 1));
            }
        }
        
        // HTML:
        
        String html = "<html>";
        
        // HTML head:
        
        html += "<head>";
        html += "<style>"
                + "body {"
                + " font-size: 100%;"
                + "}"
                + "h1 {"
                + " font-size: 2.5em;"
                + " font-family: sans-serif;"
                + "}"
                + "h1 {"
                + " font-size: 1.875em;"
                + " font-family: sans-serif;"
                + "}"
                + "p {"
                + " font-size: 0.875em;"
                + " font-family: sans-serif;"
                + "}"
                + "table {"
                + " width:100%;"
                + " font-size: 0.875em;"
                + " font-family: sans-serif;"
                + "}"
                + "table, th, td {"
                + " border: 1px solid black;"
                + " border-collapse: collapse;"
                + "}"
                + "th {"
                + " padding: 5px;"
                + " text-align: center;"
                + " background-color: black;"
                + " color: white;"
                + "}"
                + "td {"
                + " padding: 5px;"
                + "}"
                + "td.colmonth {"
                + " text-align: left;"
                + "}"
                + "td.coldata {"
                + " text-align: right;"
                + "}"
                + "</style>";
        
        html += "</head>";
        
        // HTML body:
        
        html += "<body>";
        
        html += "<h1>" + SLibUtils.textToHtml("Comparativo histórico mensual recepción materias primas") + "</h1>";
        html += "<p>Producto: <b>" + SLibUtils.textToHtml(item.getName()) + "</b></p>";
        html += "<p>Corte al: <b>" + SLibUtils.textToHtml(SLibUtils.DateFormatDateLong.format(dateCur)) + "</b> (" + SLibUtils.DateFormatTime.format(dateCur) + ")</p>";
        
        // HTML table:
        
        html += "<table>";
        
        // table header:
        
        html += "<tr>";
        for (int col = 0; col < headerCols.size(); col++) {
            html += "<th>" + headerCols.get(col) + "</th>";
        }
        html += "</tr>";
        
        // table body:
        
        String[] months = SLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT);
        double[] totals = new double[years + 1];
        
        String sql = "SELECT SUM(wei_des_net_r) "
                + "FROM s_tic "
                + "WHERE NOT b_del AND b_tar AND fk_item = " + itemId + " AND dt BETWEEN ? AND ?";
        PreparedStatement preparedStatement = moSession.getStatement().getConnection().prepareStatement(sql);
        
        int month = item.getStartingSeasonMonth();
        
        for (int row = 0; row < months.length; row++) {
            html += "<tr>";
            html += "<td class='colmonth'>" + months[month - 1] + "</td>";
            
            int year = yearStart;
            
            if (item.getStartingSeasonMonth() + row > SLibTimeConsts.MONTHS) {
                year++;
            }
            
            for (int col = 0; col < totals.length; col++) {
                double weight = 0;
                Date start = SLibTimeUtils.createDate(year - col, month, 1);
                Date end = SLibTimeUtils.getEndOfMonth(start);
                preparedStatement.setDate(1, new java.sql.Date(start.getTime()));
                preparedStatement.setDate(2, new java.sql.Date(end.getTime()));
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    weight = resultSet.getDouble(1);
                }
                html += "<td class='coldata'>" + SLibUtils.getDecimalFormatAmount().format(weight) + "</td>";
                totals[col] += weight;
            }
            
            if (++month > SLibTimeConsts.MONTHS) {
                month = 1;
            }
            
            html += "</tr>";
        }
        
        // table footer:
        
        html += "<tr>";
        html += "<td class='colmonth'><b>Total</b></td>";
        for (int col = 0; col < totals.length; col++) {
            html += "<td class='coldata'><b>" + SLibUtils.getDecimalFormatAmount().format(totals[col]) + "</b></td>";
        }
        html += "</tr>";
        
        html += "</table>";
        
        html += SSomMailUtils.composeMailWarning();
        
        html += "</body>";
        
        html += "</html>";
        
        return html;
    }
}
