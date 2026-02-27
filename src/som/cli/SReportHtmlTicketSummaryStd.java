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
import som.mod.som.db.SDbItem;
import som.mod.som.db.SSomMailUtils;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Sergio Flores
 */
public class SReportHtmlTicketSummaryStd {
    
    private final SGuiSession moSession;
    
    public SReportHtmlTicketSummaryStd(final SGuiSession session) {
        moSession = session;
    }
    
    /**
     * Generates report in HTML 5 format.
     * @param itemIds Array of ID of items to report.
     * @param date Date.
     * @param reportMode Report mode (SCliMailerReportFruitsStdSummary.REP_MODE_...)
     * @param ticketOrigin Ticket origin, e.g., supplier or external warehouse. Can be zero to be discarted.
     * @param ticketDestination Ticket destination, e.g., factory or external warehouse. Can be zero to be discarted.
     * @return
     * @throws Exception 
     */
    public String generateReportHtml(final int[] itemIds, final Date date, int reportMode, final int ticketOrigin, final int ticketDestination) throws Exception {
        // HTML:
        
        String html = "<html>\n";
        
        html += SCliUtils.composeHtmlHeadForSummary();
        
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

        for (int itemId : itemIds) {
            if (!processingFirstItem) {
                html += "<hr>\n";
            }
            
            // read requested item for report:
            SDbItem item = new SDbItem();
            item.read(moSession, new int[] { itemId }); // registries must be read directly, not from available dummy session!
            
            // compose summary:
            html += "<h1>" + SLibUtils.textToHtml(item.getName()) + "</h1>\n";
            html += SSomUtils.composeHtmlSummaryItem(moSession, itemId, SCliConsts.FRUIT_SEASON_FIRST_MONTH, SCliConsts.FRUIT_MONTH_FIRST_DAY, date, reportMode, ticketOrigin, ticketDestination);
            
            processingFirstItem = false;
        }
        
        html += SSomMailUtils.composeSomMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
        
        return html;
    }
}
