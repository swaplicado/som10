/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

/**
 *
 * @author Isabel Servin
 */
import erp.lib.SLibUtilities;
import java.io.File;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import sa.gui.util.SUtilConfigXml;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;
import sa.lib.xml.SXmlUtils;
import som.mod.SModSysConsts;
import som.mod.som.db.SSomMailUtils;

public class SCliReportMailerXlsx {
    
    public static XSSFWorkbook workbook;
    
    public static final int ID_AVO_FRUIT = 6; // fruta
    public static final int ID_AVO_FRUIT_ORG = 64; // fruta orgánica
    public static final int ID_AVO_MARC = 23; // bagazo
    public static final int ID_AVO_KERNEL = 100; // hueso y cáscara
    public static final int ID_AVO_PULP = 103; // pulpa
    
    public static final HashMap<Integer, String> ItemDescriptions = new HashMap<>();
    
    static {
        ItemDescriptions.put(ID_AVO_FRUIT, "Fruta");
        ItemDescriptions.put(ID_AVO_FRUIT_ORG, "Fruta Orgánica");
        ItemDescriptions.put(ID_AVO_MARC, "Bagazo");
        ItemDescriptions.put(ID_AVO_KERNEL, "Hueso y Cáscara");
    }
    
    /** Argument index for list of ID of items. */
    private static final int ARG_IDX_ITEM_IDS = 0;
    /** Argument index for year reference. */
    private static final int ARG_IDX_YEAR_REF = 1;
    /** Argument index for interval days for invocation of this report mailer. */
    private static final int ARG_IDX_INTVL_DAYS = 2;
    /** Argument index for list of mail-To recipients. */
    private static final int ARG_IDX_MAIL_TO = 3;
    /** Argument index for list of mail-Bcc recipients. */
    private static final int ARG_IDX_MAIL_BCC = 4;

    private static final int[] DEF_ITEM_IDS = new int[] { ID_AVO_FRUIT, /*ID_AVO_FRUIT_ORG,*/ ID_AVO_MARC, ID_AVO_KERNEL/*, ID_AVO_PULP*/ };
    private static final int DEF_YEAR_BASE = 2010; // año/temporada tope hacia atrás
    private static final int DEF_INTVL_DAYS = 7; // intervalo de días entre invocaciones de este de despachador de reportes
    private static final String DEF_MAIL_TO = "isabel.garcia@swaplicado.com.mx;sflores@swaplicado.com.mx";
    
    public static void main(String[] args) {
        
        try {
            int[] argItemIds = DEF_ITEM_IDS;
            int artYearBase = DEF_YEAR_BASE;
            int argIntvlDays = DEF_INTVL_DAYS;
            String argMailTo = DEF_MAIL_TO;
            String argMailBcc = DEF_MAIL_TO;
            
            if (args.length >= 1) {
                argItemIds = SLibUtils.textExplodeAsIntArray(args[ARG_IDX_ITEM_IDS], ";");
            }
            if (args.length >= 2) {
                artYearBase = SLibUtils.parseInt(args[ARG_IDX_YEAR_REF]);
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
            
            // connect database:
            
            String xml = SXmlUtils.readXml(SUtilConsts.FILE_NAME_CFG);
            SUtilConfigXml configXml = new SUtilConfigXml();
            configXml.processXml(xml);

            TimeZone zone = SLibUtils.createTimeZone(TimeZone.getDefault(), TimeZone.getTimeZone((String) configXml.getAttribute(SUtilConfigXml.ATT_TIME_ZONE).getValue()));
            SLibUtils.restoreDateFormats(zone);
            TimeZone.setDefault(zone);

            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            int result = database.connect(
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_HOST).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_PORT).getValue(),
                    "som_com",
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_NAME).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_PSWD).getValue());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            
            // create user session:
            
            SGuiSession session = new SGuiSession(null);
            session.setDatabase(database);
            
            // Obtener la fecha actual
            LocalDate todayLocalDate = LocalDate.now();
            // Ir al primer día del mes actual y restar un día para obtener el útlimo día del mes anterior
            LocalDate lastDayOfPreviousMonth = todayLocalDate.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
            // Convertir LocalDate en Date
            
            Date lastDayAsDate = Date.from(lastDayOfPreviousMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // generate html:
            SReportHtmlTicketSeasonMonth reportHtmlTicketSeasonMonth = new SReportHtmlTicketSeasonMonth(session);
            String htmlBody = reportHtmlTicketSeasonMonth.generateReportHtml(argItemIds, artYearBase, argIntvlDays, lastDayAsDate, SModSysConsts.SU_TIC_ORIG_PRV, 0);
            
            // Generar el archivo excel
            
            Document doc = Jsoup.parse(htmlBody);
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Hoja 1");
            int tableTitle = 0;
            int rowNum = 0;        
            
            byte[] headerRgb = new byte[] { (byte)47, (byte)79, (byte)79 };
            byte[] maxRgb = new byte[] { (byte)175, (byte)238, (byte)238 };
            
            XSSFColor headerColor = new XSSFColor(headerRgb);
            XSSFColor maxColor = new XSSFColor(maxRgb);
                        
            CellStyle titleStyle = getCellStyle(true, false, null, IndexedColors.BLACK.getIndex(), 16);
            
            CellStyle headerStyle = getCellStyle(true, false, headerColor, IndexedColors.WHITE.getIndex(), 11);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle metaDataStyle = getCellStyle(false, true, null, IndexedColors.BLACK.getIndex(), 10.5);
            CellStyle metaBoldStyle = getCellStyle(true, true, null, IndexedColors.BLACK.getIndex(), 10.5);
            
            CellStyle dataStyle = getCellStyle(false, true, null, IndexedColors.BLACK.getIndex(), 10.5);
            dataStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle pctStyle = getCellStyle(false, true, null, IndexedColors.BLACK.getIndex(), 8);
            pctStyle.setAlignment(HorizontalAlignment.CENTER);
            
            CellStyle dataMaxStyle = getCellStyle(false, true, maxColor, IndexedColors.BLACK.getIndex(), 10.5);
            dataMaxStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle pctMaxStyle = getCellStyle(false, true, maxColor, IndexedColors.BLACK.getIndex(), 8);
            pctMaxStyle.setAlignment(HorizontalAlignment.CENTER);
            
            CellStyle dataBoldStyle = getCellStyle(true, true, null, IndexedColors.BLACK.getIndex(), 10.5);
            dataBoldStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle pctBoldStyle = getCellStyle(true, true, null, IndexedColors.BLACK.getIndex(), 8);
            pctBoldStyle.setAlignment(HorizontalAlignment.CENTER);
            
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
                                default: 
                                    excelCell.setCellStyle(metaDataStyle);
                                    break;
                            }
                        }
                        else {
                            excelCell = excelRow.createCell(cellNum++);
                            excelCell.setCellValue(cellBold.text());
                            switch (cell.className()) {
                                case "coldata":
                                    excelCell.setCellStyle(dataBoldStyle);
                                    break;
                                case "coldatapct":
                                    excelCell.setCellStyle(pctBoldStyle);
                                    break;
                                default:
                                    excelCell.setCellStyle(metaBoldStyle);
                                    break;
                            }
                        }
                    }
                }
                tableTitle++;
                rowNum++;
            } 
            
            // Guerdar excel en disco
            
            String tempDir = System.getProperty("java.io.tmpdir") + "historico_mensual.xlsx";
            
            try (FileOutputStream outputStream = new FileOutputStream(tempDir)) {
                workbook.write(outputStream);
            }
            
            File file = new File(tempDir);
            
            // generate mail subject:
            
            String mailSubject = "[SOM] Historico mensual bascula Excel " + SLibUtils.DateFormatDate.format(lastDayAsDate);
            
            // prepare mail recepients:
            
            ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(argMailTo, ";")));
            ArrayList<String> recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(argMailBcc, ";")));
            
            // send mail:
            
            SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "Aeth2021*s.", "som@aeth.mx");
            //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "sflores@swaplicado.com.mx", "Ch3c0m4n", "sflores@swaplicado.com.mx");
            
            SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(mailSubject), getMailBody(lastDayAsDate), recipientsTo);
            mail.getAttachments().add(file);
            mail.getBccRecipients().addAll(recipientsBcc);
            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
            mail.send();
            
            file.delete();
            
            System.out.println("Correo enviado");
        }
        catch (Exception e) {
            SLibUtils.printException("main()", e);
        }
    }
    
    private static CellStyle getCellStyle (boolean bold, boolean border, XSSFColor color, short fontColor, double fontHeight){
        XSSFFont font;
        CellStyle cellStyle = workbook.createCellStyle();
        font = workbook.createFont();
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
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle.setBorderTop(BorderStyle.MEDIUM);
            cellStyle.setBorderRight(BorderStyle.MEDIUM);
            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        }
        
        return cellStyle;
    }
    
    private static String getMailBody(Date cutOff) {
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
        html += "<h2>" + SLibUtils.textToHtml("Histórico mensual recepción báscula") + "</h2>\n";
        html += "<p>Fecha de corte del archivo Excel: " + SLibUtils.textToHtml(SLibUtils.DateFormatDate.format(cutOff)) + "</p>\n";
        
        html += SSomMailUtils.composeSomMailWarning();
        
        html += "</body>\n";
        
        html += "</html>";
 
        return html;
    }
}