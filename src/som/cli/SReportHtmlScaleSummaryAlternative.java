/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SSomMailUtils;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SReportHtmlScaleSummaryAlternative {
    
    private final SGuiSession moSession;
    
    public SReportHtmlScaleSummaryAlternative(final SGuiSession session) {
        moSession = session;
    }
    
    /**
     * Generates report in HTML 5 format.
     * @param itemIds List of ID of items.
     * @param date Date.
     * @param ticOrig
     * @param ticDest
     * @return
     * @throws Exception 
     */
    public String generateReportHtml(final String[] itemIds, final Date date, final int ticOrig, final int ticDest) throws Exception {
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
                + "td.Forms {"
                + "white-space: nowrap;"
                + "}"
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
        
        // define start and end date for report:
        
        Date now = new Date();
        
        // HTML heading 1 (main title):
        
        html += "<h2>" + SLibUtils.textToHtml("Resumen báscula " + SLibUtils.DateFormatDate.format(date)) + "</h2>\n";
        html += "<p>" + SLibUtils.textToHtml("Fecha-hora emisión: " + SLibUtils.textToHtml(SLibUtils.DateFormatDatetime.format(now))) + "</p>\n";
        
        // process list of items for report:

        for (String itemId : itemIds) {
            String[] ids = itemId.split("-");
            int convId = SLibUtils.parseInt(ids[0]);
            int altId = SLibUtils.parseInt(ids[1]);
            // read requested item for report:
            SDbItem item = new SDbItem();
            item.read(moSession, new int[] { convId });
            
            // compose summary:
            html += "<h1>" + SLibUtils.textToHtml("AGUACATE CONVENCIONAL Y ORGÁNICO") + "</h1>\n";
            html += SSomUtils.composeHtmlSummaryItemAlternative(moSession, convId, altId, date, ticOrig, ticDest);
        }
        
        html += SSomMailUtils.composeSomAlternativeMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
        
        return html;
    }
}
