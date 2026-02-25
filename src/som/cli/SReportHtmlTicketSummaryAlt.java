/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.mod.som.db.SSomMailUtils;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Sergio Flores, Isabel Servín, Sergio Flores
 */
public class SReportHtmlTicketSummaryAlt {
    
    private final SGuiSession moSession;
    
    public SReportHtmlTicketSummaryAlt(final SGuiSession session) {
        moSession = session;
    }
    
    /**
     * Generates report in HTML 5 format.
     * @param itemIdsPairs Array of pairs of ID of items to report.
     * @param date Date.
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @return
     * @throws Exception 
     */
    public String generateReportHtml(final String[] itemIdsPairs, final Date date, final int ticketOrigin, final int ticketDestination) throws Exception {
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
                + " background-color: DarkSlateGray;"
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
                + "}"
                + "\n"
                + "</style>\n";
        
        html += "</head>\n";
        
        // HTML body:
        
        html += "<body>\n";
        
        // define start and end date for report:
        
        Date now = new Date();
        
        // HTML heading 1 (main title):
        
        html += "<h2>" + SLibUtils.textToHtml("Resumen de báscula: Recepción de fruta") + "</h2>\n";
        
        if (SLibTimeUtils.isSameDate(date, now)) {
            html += "<p>" + SLibUtils.textToHtml("Fecha-hora de corte y emisión: " + SLibUtils.DateFormatDatetime.format(now) + ".") + "</p>\n";
        }
        else {
            html += "<p>" + SLibUtils.textToHtml("Fecha de corte: " + SLibUtils.DateFormatDate.format(date) + ".") + "</p>\n";
            html += "<p>" + SLibUtils.textToHtml("Fecha-hora de emisión: " + SLibUtils.DateFormatDatetime.format(now) + ".") + "</p>\n";
        }
        
        // process list of items for report:

        boolean processingFirstItem = true;

        for (String itemIdsPair : itemIdsPairs) {
            if (!processingFirstItem) {
                html += "<hr>\n";
            }
            
            String[] ids = itemIdsPair.split("-");
            int itemCnvId = SLibUtils.parseInt(ids[0]);
            int itemAltId = SLibUtils.parseInt(ids[1]);
            
            // compose summary:
            html += "<h1>" + SLibUtils.textToHtml(SCliConsts.ItemsPairsNames.get(itemIdsPair).toUpperCase()) + "</h1>\n";
            html += SSomUtils.composeHtmlSummaryItemAlt(moSession, itemCnvId, itemAltId, SCliConsts.FRUIT_SEASON_FIRST_MONTH, SCliConsts.FRUIT_MONTH_FIRST_DAY, date, ticketOrigin, ticketDestination);
            
            processingFirstItem = false;
        }
        
        html += SSomMailUtils.composeSomAlternativeMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
        
        return html;
    }
}
