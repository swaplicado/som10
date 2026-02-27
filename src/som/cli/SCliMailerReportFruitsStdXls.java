/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import erp.lib.SLibUtilities;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import som.mod.SModSysConsts;
import som.mod.som.db.SSomMailUtils;

/**
 * CLI mailer of report of monthly reception of STANDARD fruits at scale (as XLS file attached).
 * "Histórico mensual báscula fruta Excel"
 * 
 * @author Isabel García, Sergio Flores
 */
public class SCliMailerReportFruitsStdXls {
    
    /** Argument index for list of ID of items. */
    private static final int ARG_IDX_ITEM_IDS = 0;
    /** Argument index for year base. */
    private static final int ARG_IDX_YEAR_BASE = 1;
    /** Argument index for interval days for invocation of this report mailer. */
    private static final int ARG_IDX_INTVL_DAYS = 2;
    /** Argument index for list of mail-To recipients. */
    private static final int ARG_IDX_MAIL_TO = 3;
    /** Argument index for list of mail-Bcc recipients. */
    private static final int ARG_IDX_MAIL_BCC = 4;

    private static final int[] DEF_ITEM_IDS = new int[] { SCliConsts.ID_AVO_FRUIT_CONV, /*SCliConsts.ID_AVO_FRUIT_ORG,*/ SCliConsts.ID_AVO_CHAFF, SCliConsts.ID_AVO_KERNEL, SCliConsts.ID_AVO_PULP };
    private static final int DEF_YEAR_BASE = SCliConsts.FRUIT_FIRST_YEAR; // año/temporada tope hacia atrás
    //private static final int DEF_YEAR_REF = 5; // comparativa de 5 años hacia atrás, además del año/temporada actual
    private static final int DEF_INTVL_DAYS = 7; // intervalo de días entre invocaciones de este de despachador de reportes
    private static final String DEF_MAIL_TO = "sflores@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "isabel.garcia@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "isabel.garcia@swaplicado.com.mx;sflores@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "gortiz@aeth.mx";
    //private static final String DEF_MAIL_TO = "gortiz@aeth.mx;sflores@swaplicado.com.mx";
    private static final String DEF_MAIL_BCC = "sflores.swaplicado@gmail.com";
    //private static final String DEF_MAIL_BCC = "isabel.garcia@swaplicado.com.mx";
    
    public static XSSFWorkbook Workbook;
    
    /**
     * @param args the command line arguments.
     * Five arguments expected:
     * 1: List of ID of items to be reported (separated with semicolon, without blanks between them, OBVIOUSLY!)
     * 2: Year base. Can be one out of two elegible types of values: 1) if it is a 4-digit year and greater or equal than 2001, it is the year to start from; otherwise 2) it is the number of history years besides current year.
     * 3: Interval of days for invocation of this report mailer.
     * 4: List of mail-To recipients (separated with semicolon, without blanks between them, OBVIOUSLY!).
     * 5: List of mail-Bcc recipients (separated with semicolon, without blanks between them, OBVIOUSLY!).
     */
    public static void main(String[] args) {
        try {
            // define program arguments:
            
            int[] argItemIds = DEF_ITEM_IDS;
            int argYearBase = DEF_YEAR_BASE;
            int argIntvlDays = DEF_INTVL_DAYS;
            String argMailTo = DEF_MAIL_TO;
            String argMailBcc = DEF_MAIL_BCC;
            
            if (args.length >= 1) {
                argItemIds = SLibUtils.textExplodeAsIntArray(args[ARG_IDX_ITEM_IDS], ";");
            }
            if (args.length >= 2) {
                argYearBase = SLibUtils.parseInt(args[ARG_IDX_YEAR_BASE]);
            }
            if (args.length >= 3) {
                argIntvlDays = SLibUtils.parseInt(args[ARG_IDX_INTVL_DAYS]);
            }
            if (args.length >= 4) {
                argMailTo = args[ARG_IDX_MAIL_TO];
            }
            if (args.length >= 5) {
                argMailBcc = args[ARG_IDX_MAIL_BCC];
            }
            
            // generate report:

            Date now = new Date();
            Date cutoff = null;
            int[] nowDigestion = SLibTimeUtils.digestDate(now);
            
            if (SCliConsts.FRUIT_MONTH_FIRST_DAY == 1) {
                cutoff = SLibTimeUtils.getEndOfMonth(SLibTimeUtils.addDate(now, 0, -1, 0));
            }
            else {
                if (nowDigestion[2] >= SCliConsts.FRUIT_MONTH_FIRST_DAY) {
                    // cutoff on current month:
                    cutoff = SLibTimeUtils.createDate(nowDigestion[0], nowDigestion[1], SCliConsts.FRUIT_MONTH_FIRST_DAY - 1);
                }
                else {
                    // cutoff on previous month:
                    if (nowDigestion[1] > SLibTimeConsts.MONTH_JAN) {
                        // cutfoff on current year:
                        cutoff = SLibTimeUtils.createDate(nowDigestion[0], nowDigestion[1] - 1, SCliConsts.FRUIT_MONTH_FIRST_DAY - 1);
                    }
                    else {
                        // cutfoff on previous year:
                        cutoff = SLibTimeUtils.createDate(nowDigestion[0] - 1, SLibTimeConsts.MONTH_DEC, SCliConsts.FRUIT_MONTH_FIRST_DAY - 1);
                    }
                }
            }
            
            SReportHtmlTicketSeasonMonthStd reportHtml = new SReportHtmlTicketSeasonMonthStd(SCliUtils.createSession());
            String mailBodyHtml = reportHtml.generateReportHtml(argItemIds, argYearBase, argIntvlDays,
                    SCliConsts.FRUIT_SEASON_FIRST_MONTH, SCliConsts.FRUIT_MONTH_FIRST_DAY, SCliConsts.FRUIT_BY_OP_CALENDARS,
                    cutoff, now, SModSysConsts.SU_TIC_ORIG_SUP, 0, SReportHtmlTicketSeasonMonthStd.MODE_UNIT_TON);
            
            File file = null;
            
            try {
                file = createReportFile(mailBodyHtml, cutoff, SCliConsts.FRUIT_MONTH_FIRST_DAY > 1, SCliConsts.FRUIT_BY_OP_CALENDARS);

                // send mail report:

                String mailSubject = SLibUtils.textToAscii("[SOM] Histórico mensual báscula fruta Excel " + SLibUtils.DateFormatDate.format(now));
                ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(argMailTo, ";")));
                ArrayList<String> recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(argMailBcc, ";")));

                String mailBody = generateBodyHtml(cutoff, now);
                SCliUtils.sendMailReport(mailSubject, mailBody, recipientsTo, recipientsBcc, file);
            }
            catch (Exception e) {
                throw e;
            }
            finally {
                if (file != null) {
                    file.delete();
                }
            }
        }
        catch (Exception e) {
            SLibUtils.printException(SCliMailerReportFruitsStdXls.class.getName(), e);
        }
    }
    
    private static File createReportFile(final String mailBodyHtml, final Date cutoff, final boolean isCustomMonth, final boolean useOpCalendars) throws Exception {
        Document doc = Jsoup.parse(mailBodyHtml);
        Workbook = new XSSFWorkbook();
        Sheet sheet = Workbook.createSheet("Hoja 1");
        int tableTitle = 0;
        int rowNum = 0;        

        byte[] headerRgb = new byte[] { (byte) 0, (byte) 128, (byte) 128 };
        byte[] maxRgb = new byte[] { (byte) 0, (byte) 255, (byte) 255 };
        byte[] seasonRgb = new byte[] { (byte) 128, (byte) 191, (byte) 191 };
        byte[] accumRgb = new byte[] { (byte) 229, (byte) 231, (byte) 233 };

        XSSFColor headerColor = new XSSFColor(headerRgb);
        XSSFColor maxColor = new XSSFColor(maxRgb);
        XSSFColor seasonColor = new XSSFColor(seasonRgb);
        XSSFColor accumColor = new XSSFColor(accumRgb);

        CellStyle titleStyle = createCellStyle(true, false, null, IndexedColors.BLACK.getIndex(), 16);

        CellStyle headerStyle = createCellStyle(true, false, headerColor, IndexedColors.WHITE.getIndex(), 11);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle metaDataStyle = createCellStyle(false, true, null, IndexedColors.BLACK.getIndex(), 10.5);
        CellStyle metaBoldSeasonStyle = createCellStyle(true, true, seasonColor, IndexedColors.BLACK.getIndex(), 10.5);

        CellStyle dataStyle = createCellStyle(false, true, null, IndexedColors.BLACK.getIndex(), 10.5);
        dataStyle.setAlignment(HorizontalAlignment.RIGHT);
        CellStyle pctStyle = createCellStyle(false, true, null, IndexedColors.BLACK.getIndex(), 8);
        pctStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle dataMaxStyle = createCellStyle(false, true, maxColor, IndexedColors.BLACK.getIndex(), 10.5);
        dataMaxStyle.setAlignment(HorizontalAlignment.RIGHT);
        CellStyle pctMaxStyle = createCellStyle(false, true, maxColor, IndexedColors.BLACK.getIndex(), 8);
        pctMaxStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle dataBoldSeasonStyle = createCellStyle(true, true, seasonColor, IndexedColors.BLACK.getIndex(), 10.5);
        dataBoldSeasonStyle.setAlignment(HorizontalAlignment.RIGHT);
        CellStyle pctBoldSeasonStyle = createCellStyle(true, true, seasonColor, IndexedColors.BLACK.getIndex(), 8);
        pctBoldSeasonStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle metaDataStyleAccum = createCellStyle(false, true, accumColor, IndexedColors.BLACK.getIndex(), 10.5);

        CellStyle dataStyleAccum = createCellStyle(false, true, accumColor, IndexedColors.BLACK.getIndex(), 10.5);
        dataStyleAccum.setAlignment(HorizontalAlignment.RIGHT);
        CellStyle pctStyleAccum = createCellStyle(false, true, accumColor, IndexedColors.BLACK.getIndex(), 8);
        pctStyleAccum.setAlignment(HorizontalAlignment.CENTER);

        CellStyle notesStyle = createCellStyle(false, false, null, IndexedColors.BLACK.getIndex(), 9);
        String notes = "";
        
        if (isCustomMonth) {
            int[] cutoffDigestion = SLibTimeUtils.digestMonth(cutoff);
            String monthClosing = useOpCalendars ? "según el calendario operativo aplicable (cierre mes actual: " + SLibUtils.DecimalFormatCalendarMonth.format(SCliConsts.FRUIT_MONTH_FIRST_DAY - 1) + "/" + SLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT)[cutoffDigestion[1] - 1] + "./" + cutoffDigestion[0] + ")" : "los días " + SLibUtils.DecimalFormatCalendarMonth.format(SCliConsts.FRUIT_MONTH_FIRST_DAY - 1) + " de cada mes";
            notes = "* Inicio de temporada: " + SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG)[SCliConsts.FRUIT_SEASON_FIRST_MONTH - 1] + ". Día de cierre mensual: " + monthClosing + ".";
        }

        for (Element table : doc.select("table")) {
            Element title = doc.select("h4").get(tableTitle);
            Row excelRow = sheet.createRow(rowNum++);
            Cell excelCell = excelRow.createCell(0);
            excelCell.setCellValue(title.select("h4").get(0).text());
            excelCell.setCellStyle(titleStyle);

            for (Element row : table.select("tr")) {
                excelRow = sheet.createRow(rowNum);
                Elements cells = row.select("th");
                int cellNum = 0;

                for (Element cell : cells) {
                    excelCell = excelRow.createCell(cellNum);
                    excelCell.setCellValue(cell.text());
                    if (cellNum > 0) {
                        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, cellNum, cellNum + 1));
                        sheet.setColumnWidth(cellNum, 3942); // número calculado a partir del archivo base
                        sheet.setColumnWidth(cellNum + 1, 2236); // número calculado a partir del archivo base
                        cellNum += 2;
                    }
                    else {
                        sheet.setColumnWidth(cellNum, 3143); // número calculado a partir del archivo base 1000 = 35px
                        cellNum++;
                    }
                    excelCell.setCellStyle(headerStyle);
                }

                rowNum++;

                cells = row.select("td");
                cellNum = 0;

                for (Element cell : cells) {
                    Element cellBold = cell.selectFirst("b");

                    if (cellBold == null) {
                        excelCell = excelRow.createCell(cellNum++);
                        excelCell.setCellValue(cell.text());

                        switch (cell.className()) {
                            case "coldata":
                                excelCell.setCellStyle(dataStyle);
                                break;
                            case "coldatapct":
                                excelCell.setCellStyle(pctStyle);
                                break;
                            case "coldatamax":
                                excelCell.setCellStyle(dataMaxStyle);
                                break;
                            case "coldatapctmax":
                                excelCell.setCellStyle(pctMaxStyle);
                                break;
                            case "coldataaccum":
                                excelCell.setCellStyle(dataStyleAccum);
                                break;
                            case "coldatapctaccum":
                                excelCell.setCellStyle(pctStyleAccum);
                                break;
                            case "colmonthaccum":
                                excelCell.setCellStyle(metaDataStyleAccum);
                                break;
                            default: 
                                excelCell.setCellStyle(metaDataStyle);
                                break;
                        }
                    }
                    else {
                        excelCell = excelRow.createCell(cellNum++);
                        excelCell.setCellValue(cellBold.text());

                        switch (cell.className()) {
                            case "coldatatotal":
                                excelCell.setCellStyle(dataBoldSeasonStyle);
                                break;
                            case "coldatapcttotal":
                                excelCell.setCellStyle(pctBoldSeasonStyle);
                                break;
                            default:
                                excelCell.setCellStyle(metaBoldSeasonStyle);
                                break;
                        }
                    }
                }
            }

            // notes:
            excelRow = sheet.createRow(rowNum++);
            excelCell = excelRow.createCell(0);
            excelCell.setCellValue(notes);
            excelCell.setCellStyle(notesStyle);

            // prepare for next table:
            tableTitle++;
            rowNum++;
        } 

        // save Excel file:

        String tempDir = System.getProperty("java.io.tmpdir") + "historico_mensual.xlsx";

        try (FileOutputStream outputStream = new FileOutputStream(tempDir)) {
            Workbook.write(outputStream);
        }

        return new File(tempDir);
    }
    
    private static CellStyle createCellStyle(final boolean bold, final boolean border, final XSSFColor color, final short fontColor, final double fontHeight) {
        XSSFFont font;
        CellStyle cellStyle = Workbook.createCellStyle();
        font = Workbook.createFont();
        font.setBold(bold);
        font.setFontName("Arial");
        font.setFontHeight(fontHeight);
        font.setColor(fontColor);
        cellStyle.setFont(font);
        
        if (color != null) {
            cellStyle.setFillForegroundColor(color);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        
        if (border) {
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
        }
        
        return cellStyle;
    }
    
    /**
     * Get mail body.
     * @param cutoff Cutoff date.
     * @param now Very moment of processing.
     * @return 
     */
    private static String generateBodyHtml(final Date cutoff, final Date now) {
        String html = "<html>\n";
        
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
                + "</style>\n";
        
        html += "</head>\n";
        
        html += "<body>\n";
        
        html += "<h2>" + SLibUtils.textToHtml("Histórico mensual de báscula: Recepción de fruta en Excel") + "</h2>\n";
        
        if (SLibTimeUtils.isSameDate(cutoff, now)) {
            html += "<p>" + SLibUtils.textToHtml("Fecha-hora de corte y emisión: " + SLibUtils.DateFormatDatetime.format(now) + ".") + "</p>\n";
        }
        else {
            html += "<p>" + SLibUtils.textToHtml("Fecha de corte: " + SLibUtils.DateFormatDate.format(cutoff) + ".") + "</p>\n";
            html += "<p>" + SLibUtils.textToHtml("Fecha-hora de emisión: " + SLibUtils.DateFormatDatetime.format(now) + ".") + "</p>\n";
        }
        
        html += SSomMailUtils.composeSomMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
 
        return html;
    }
}
