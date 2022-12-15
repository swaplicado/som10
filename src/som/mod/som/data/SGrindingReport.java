/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;
import som.mod.som.db.SDbGrindingReportItemGroup;
import som.mod.som.db.SDbGrindingLinkFormula;
import som.mod.som.db.SDbGrindingEvent;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbProcessingBatch;

/**
 *
 * @author Edwin Carmona
 */
public class SGrindingReport {
    
    public static final int SEND_REPORT = 1;
    public static final int SAVE_REPORT = 2;
    
    private CellStyle moStyleDecimals = null;
    private CellStyle moBackgroundBlackStyle = null;
    
    /**
     * Procesamiento del reporte
     * 
     * @param client
     * @param dtDate
     * @param idItem
     * @param idLot
     * @param actionType SGrindingReport.SEND_REPORT or SGrindingReport.SAVE_REPORT
     * @throws IOException
     * @throws Exception 
     */
    public void processReport(SGuiClient client, Date dtDate, int idItem, int idLot, int actionType) throws IOException, Exception {
        ArrayList<SDbGrindingReportItemGroup> group = SGrindingResultsUtils.getGroupOfGrindingItem(client, idItem);
        if (group.isEmpty()) {
            group = new ArrayList<>();
            SDbGrindingReportItemGroup aux = new SDbGrindingReportItemGroup();
            aux.setFkReportGroupId(0);
            aux.setFkItemId(idItem);

            group.add(aux);
        }

        String sMonth = "";
        XSSFWorkbook workbook;
        for (SDbGrindingReportItemGroup itemGroup : group) {
            workbook = new XSSFWorkbook();

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            DateTime dateStart = DateTime.parse(year + "-01-01");
            DateTime indexDate = dateStart.dayOfMonth().withMaximumValue();
            DateTime lastDate = new DateTime(dtDate);
            lastDate = lastDate.dayOfMonth().withMaximumValue();

            SCaptureConfiguration cfg = SGrindingResultsUtils.getCfgField(client, itemGroup.getFkItemId());

            if (cfg == null) {
                return;
            }

            SDbItem oItem = new SDbItem();
            oItem.read(client.getSession(), new int[]{itemGroup.getFkItemId()});

            itemGroup.setSDbItemAux(oItem);

            String sRange = "";
            ArrayList<SGrindingResumeRow> grindingRows = new ArrayList<>();
            while (indexDate.isBefore(lastDate) || indexDate.isEqual(lastDate)) {
                // 
                calendar.setTime(indexDate.toDate());
                Month month = Month.of(calendar.get(Calendar.MONTH) + 1);
                sMonth = (month.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))).toUpperCase();
                XSSFSheet sheet = workbook.createSheet(sMonth);

                int idDayLot = SGrindingResultsUtils.getLotByItemAndDate(client, itemGroup.getFkItemId(), dtDate);

                SDbProcessingBatch oLot = new SDbProcessingBatch();
                oLot.read(client.getSession(), new int[]{idDayLot});
                itemGroup.setSDbLotAux(oLot);

                grindingRows = SGrindingResume.getResumeRows(client, indexDate.toDate(), itemGroup.getFkItemId());
                double rendTeo = 0d;
                if (!grindingRows.isEmpty()) {
                    SGrindingResumeRow last = grindingRows.get(grindingRows.size() - 1);
                    rendTeo = last.getValue();
                }

                LinkedHashMap<Date, ArrayList<SGrindingResultReport>> info = this.getGrinding(client, indexDate.toDate(), itemGroup.getFkItemId(), idDayLot);

                sRange = this.generateReport(client, grindingRows, rendTeo, cfg, info, sheet, oLot, oItem, dtDate);

                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);

                indexDate = indexDate.plusMonths(1);
                indexDate = indexDate.dayOfMonth().withMaximumValue();
            }

            itemGroup.setRangeAux(sRange);
            itemGroup.setWorkbookAux(workbook);
            itemGroup.getResumeHeaderRows().clear();
            itemGroup.getResumeHeaderRows().addAll(grindingRows);
        }

        if (actionType == SGrindingReport.SEND_REPORT) {
            SGrindingResultsUtils.sendReport(client, group, dtDate, sMonth);
        }
        else if (actionType == SGrindingReport.SAVE_REPORT) {
            SGrindingResultsUtils.saveReport(client, group.get(0).getWorkbookAux(), group.get(0).getSDbLotAux(), group.get(0).getSDbItemAux(), dtDate);
        }
    }

    /**
     * Genera la hoja del reporte mensual correspondiente al ítem en cuestión
     * 
     * @param client
     * @param resume
     * @param rendTeo
     * @param cfg
     * @param info
     * @param sheet
     * @param oLot
     * @param oItem
     * @param dtDate
     * 
     * @return 
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String generateReport(SGuiClient client, ArrayList<SGrindingResumeRow> resume, double rendTeo, SCaptureConfiguration cfg, 
                            LinkedHashMap<Date, ArrayList<SGrindingResultReport>> info, 
                            XSSFSheet sheet, SDbProcessingBatch oLot, SDbItem oItem, Date dtDate) throws FileNotFoundException, IOException {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat formatterTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        ObjectMapper mapper = new ObjectMapper();

        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
//        font.setBold(true);
        style.setFont(font);

        moStyleDecimals = sheet.getWorkbook().createCellStyle();
        moStyleDecimals.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("0.0000"));

        moBackgroundBlackStyle = sheet.getWorkbook().createCellStyle();
        moBackgroundBlackStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        moBackgroundBlackStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        int rowsCount = 1;

        /**
         * Código y nombre del ítem
         */
        Row itemRow = sheet.createRow(rowsCount++);
        Cell cellLabelI = itemRow.createCell(1);
        cellLabelI.setCellValue("Ítem:");
        Cell cellI = itemRow.createCell(2);
        cellI.setCellValue(oItem.getCode() + " - " + oItem.getName());
        cellI.setCellStyle(style);

        /**
         * Lote
         */
        Row lotRow = sheet.createRow(rowsCount++);
        Cell cellLabelL = lotRow.createCell(1);
        cellLabelL.setCellValue("Lote:");
        Cell cellL = lotRow.createCell(2);
        cellL.setCellValue(oLot.getProcessingBatch() + " / " + formatter.format(oLot.getDate()));
        cellL.setCellStyle(style);

        sheet.createRow(rowsCount++);

        /**
         * Resumen
         */
        for (SGrindingResumeRow sGrindingResumeRow : resume) {
            Row resumeRow = sheet.createRow(rowsCount++);
            int columnCount = 1;
            Cell cell = resumeRow.createCell(columnCount++);
            cell.setCellValue(sGrindingResumeRow.getDataName());
            cell.setCellStyle(style);
            Cell cellV = resumeRow.createCell(columnCount++);
            cellV.setCellValue(sGrindingResumeRow.getValue());
            Cell cellU = resumeRow.createCell(columnCount++);
            cellU.setCellValue(sGrindingResumeRow.getUnit());
        }

        sheet.createRow(rowsCount++);

        /**
         * Tabla de resultados: datos
         */
        String sDate = formatter.format(dtDate);
        String columnInitialCaracter = "A";
        int rowInitialIndex = 0;
        int columnFinalIndex = 0;
        String columnFinalCaracter = "";
        int rowFinalIndex = 0;
        for (Map.Entry<Date, ArrayList<SGrindingResultReport>> entry : info.entrySet()) {
            Date keyDate = entry.getKey();
            String sKeyDate = formatter.format(keyDate);
            ArrayList<SGrindingResultReport> rows = entry.getValue();

            sheet.createRow(rowsCount++);
            Row emtyRow = sheet.createRow(rowsCount++);
            Cell cellDateEmpty = emtyRow.createCell(1);
            cellDateEmpty.setCellValue("" + formatter.format(keyDate));
            if (sDate.equals(sKeyDate)) {
                rowInitialIndex = rowsCount;
            }
            /**
             * Eventos de molienda
             */
            ArrayList<SDbGrindingEvent> events = SGrindingResume.getGrindingEvents(client, keyDate, keyDate, oItem.getPkItemId());
            if (!events.isEmpty()) {
                Row eventsTitle = sheet.createRow(rowsCount++);
                Cell cellT = eventsTitle.createCell(1);
                cellT.setCellValue("Eventos de paro de proceso:");
                for (SDbGrindingEvent event : events) {
                    Row eventRow = sheet.createRow(rowsCount++);
                    int columnEventCount = 1;
                    Cell cell = eventRow.createCell(columnEventCount++);
                    cell.setCellValue(event.getDescription());
                    cell.setCellStyle(style);
                    Cell cellV = eventRow.createCell(columnEventCount++);
                    cellV.setCellValue("De " + formatterTime.format(event.getDateStart()) + " a " + formatterTime.format(event.getDateEnd()));
                }

                sheet.createRow(rowsCount++);
            }

            /**
             * Tabla de resultados: Encabezados
             */
            // columnas
            if (!rows.isEmpty()) {
                ArrayList<String> columns = new ArrayList<>();
                columns.add("Fecha");
                columns.add("Parámetros/Hora");

                if (cfg.r08.getIsActive()) {
                    columns.add(cfg.r08.getLabel());
                }
                if (cfg.r10.getIsActive()) {
                    columns.add(cfg.r10.getLabel());
                }
                if (cfg.r12.getIsActive()) {
                    columns.add(cfg.r12.getLabel());
                }
                if (cfg.r14.getIsActive()) {
                    columns.add(cfg.r14.getLabel());
                }
                if (cfg.r16.getIsActive()) {
                    columns.add(cfg.r16.getLabel());
                }
                if (cfg.r18.getIsActive()) {
                    columns.add(cfg.r18.getLabel());
                }
                if (cfg.r20.getIsActive()) {
                    columns.add(cfg.r20.getLabel());
                }
                if (cfg.r22.getIsActive()) {
                    columns.add(cfg.r22.getLabel());
                }
                if (cfg.r00.getIsActive()) {
                    columns.add(cfg.r00.getLabel());
                }
                if (cfg.r02.getIsActive()) {
                    columns.add(cfg.r02.getLabel());
                }
                if (cfg.r04.getIsActive()) {
                    columns.add(cfg.r04.getLabel());
                }
                if (cfg.r06.getIsActive()) {
                    columns.add(cfg.r06.getLabel());
                }

                Row headerRow = sheet.createRow(rowsCount++);
                int headerColumnCount = 1;
                for (String columnName : columns) {
                    Cell cell = headerRow.createCell(headerColumnCount++);
                    cell.setCellValue(columnName);
                    cell.setCellStyle(style);
                }
            }

            for (SGrindingResultReport row : rows) {
                int rowIndex = rowsCount++;
                Row infoRow = sheet.createRow(rowIndex);
                int columnCount = 1;

                //Fecha
                Cell cellDate = infoRow.createCell(columnCount++);
                cellDate.setCellValue("" + formatter.format(keyDate));

                //Parámetro
                Cell cellParam = infoRow.createCell(columnCount++);
                cellParam.setCellValue(row.parameterName);

                // 
                if (row.parameterCode.equals("--")) {
                    cellDate.setCellStyle(moBackgroundBlackStyle);
                    cellParam.setCellStyle(moBackgroundBlackStyle);
                }

                //horas:
                if (cfg.r08.getIsActive()) {
                    Cell cell08 = this.createCell(infoRow, columnCount++, row.result08, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r10.getIsActive()) {
                    Cell cell10 = this.createCell(infoRow, columnCount++, row.result10, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r12.getIsActive()) {
                    Cell cell12 = this.createCell(infoRow, columnCount++, row.result12, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r14.getIsActive()) {
                    Cell cell14 = this.createCell(infoRow, columnCount++, row.result14, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r16.getIsActive()) {
                    Cell cell16 = this.createCell(infoRow, columnCount++, row.result16, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r18.getIsActive()) {
                    Cell cell18 = this.createCell(infoRow, columnCount++, row.result18, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r20.getIsActive()) {
                    Cell cell20 = this.createCell(infoRow, columnCount++, row.result20, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r22.getIsActive()) {
                    Cell cell22 = this.createCell(infoRow, columnCount++, row.result22, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r00.getIsActive()) {
                    Cell cell00 = this.createCell(infoRow, columnCount++, row.result00, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r02.getIsActive()) {
                    Cell cell02 = this.createCell(infoRow, columnCount++, row.result02, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r04.getIsActive()) {
                    Cell cell04 = this.createCell(infoRow, columnCount++, row.result04, row.parameterName, row.defaultTextValue);
                }
                if (cfg.r06.getIsActive()) {
                    Cell cell06 = this.createCell(infoRow, columnCount++, row.result06, row.parameterName, row.defaultTextValue);
                }
                if (sDate.equals(sKeyDate)) {
                    columnFinalIndex = columnCount;
                }

                /**
                 * Fórmulas
                 */
                if (row.formulas.size() > 0) {
                    boolean bRowAdded = false;
                    for (SDbGrindingLinkFormula formula : row.formulas) {
                        SFormulasRow formObj = mapper.readValue(formula.getFormula(), SFormulasRow.class);
                        boolean isNewRow = (formula.getRowText() != null && formula.getRowText().length() > 0) || formObj.getR08().getIndexRow() != 0;
                        if (formObj.r08.getIsActive()) {
                            if (isNewRow) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR08().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR08 = formulaRow.createCell(formObj.getR08().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR08().getFormula(), rIndex);
                                cellR08.setCellFormula(formulaS);
                                cellR08.setCellStyle(moStyleDecimals);

                                bRowAdded = true;
                                infoRow = formulaRow;
                            }
                            else {
                                Cell cellR08 = infoRow.createCell(formObj.getR08().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR08().getFormula(), rowIndex);
                                cellR08.setCellFormula(formulaS);
                                cellR08.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r10.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR10().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR10 = formulaRow.createCell(formObj.getR10().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR10().getFormula(), rIndex);
                                cellR10.setCellFormula(formulaS);
                                cellR10.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR10 = infoRow.createCell(formObj.getR10().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR10().getFormula(), rowIndex);
                                cellR10.setCellFormula(formulaS);
                                cellR10.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r12.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR12().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR12 = formulaRow.createCell(formObj.getR12().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR12().getFormula(), rIndex);
                                cellR12.setCellFormula(formulaS);
                                cellR12.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR12 = infoRow.createCell(formObj.getR12().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR12().getFormula(), rowIndex);
                                cellR12.setCellFormula(formulaS);
                                cellR12.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r14.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR14().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR14 = formulaRow.createCell(formObj.getR14().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR14().getFormula(), rIndex);
                                cellR14.setCellFormula(formulaS);
                                cellR14.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR14 = infoRow.createCell(formObj.getR14().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR14().getFormula(), rowIndex);
                                cellR14.setCellFormula(formulaS);
                                cellR14.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r16.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR16().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR16 = formulaRow.createCell(formObj.getR16().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR16().getFormula(), rIndex);
                                cellR16.setCellFormula(formulaS);
                                cellR16.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR16 = infoRow.createCell(formObj.getR16().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR16().getFormula(), rowIndex);
                                cellR16.setCellFormula(formulaS);
                                cellR16.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r18.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR18().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR18 = formulaRow.createCell(formObj.getR18().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR18().getFormula(), rIndex);
                                cellR18.setCellFormula(formulaS);
                                cellR18.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR18 = infoRow.createCell(formObj.getR18().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR18().getFormula(), rowIndex);
                                cellR18.setCellFormula(formulaS);
                                cellR18.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r20.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR20().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR20 = formulaRow.createCell(formObj.getR20().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR20().getFormula(), rIndex);
                                cellR20.setCellFormula(formulaS);
                                cellR20.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR20 = infoRow.createCell(formObj.getR20().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR20().getFormula(), rowIndex);
                                cellR20.setCellFormula(formulaS);
                                cellR20.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r22.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR22().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR22 = formulaRow.createCell(formObj.getR22().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR22().getFormula(), rIndex);
                                cellR22.setCellFormula(formulaS);
                                cellR22.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR22 = infoRow.createCell(formObj.getR22().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR22().getFormula(), rowIndex);
                                cellR22.setCellFormula(formulaS);
                                cellR22.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r00.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR00().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR00 = formulaRow.createCell(formObj.getR00().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR00().getFormula(), rIndex);
                                cellR00.setCellFormula(formulaS);
                                cellR00.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR00 = infoRow.createCell(formObj.getR00().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR00().getFormula(), rowIndex);
                                cellR00.setCellFormula(formulaS);
                                cellR00.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r02.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR02().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR02 = formulaRow.createCell(formObj.getR02().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR02().getFormula(), rIndex);
                                cellR02.setCellFormula(formulaS);
                                cellR02.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR02 = infoRow.createCell(formObj.getR02().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR02().getFormula(), rowIndex);
                                cellR02.setCellFormula(formulaS);
                                cellR02.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r04.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR04().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR04 = formulaRow.createCell(formObj.getR04().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR04().getFormula(), rIndex);
                                cellR04.setCellFormula(formulaS);
                                cellR04.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR04 = infoRow.createCell(formObj.getR04().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR04().getFormula(), rowIndex);
                                cellR04.setCellFormula(formulaS);
                                cellR04.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.r06.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR06().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellR06 = formulaRow.createCell(formObj.getR06().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR06().getFormula(), rIndex);
                                cellR06.setCellFormula(formulaS);
                                cellR06.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellR06 = infoRow.createCell(formObj.getR06().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR06().getFormula(), rowIndex);
                                cellR06.setCellFormula(formulaS);
                                cellR06.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                        if (formObj.rExtra.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getrExtra().getIndexRow();
                                Row formulaRow = sheet.createRow(rIndex);

                                //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());

                                Cell cellrExtra = formulaRow.createCell(formObj.getrExtra().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getrExtra().getFormula(), rIndex);
                                cellrExtra.setCellFormula(formulaS);
                                cellrExtra.setCellStyle(moStyleDecimals);
                            }
                            else {
                                Cell cellrExtra = infoRow.createCell(formObj.getrExtra().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getrExtra().getFormula(), rowIndex);
                                cellrExtra.setCellFormula(formulaS);
                                cellrExtra.setCellStyle(moStyleDecimals);
                            }

                            if (columnFinalIndex < formObj.getrExtra().getColNumber() && sDate.equals(sKeyDate)) {
                                columnFinalIndex = formObj.getrExtra().getColNumber();
                            }
                        }
                    }
                }
            }

            if (sDate.equals(sKeyDate)) {
                rowFinalIndex = rowsCount;
            }
        }

        columnFinalCaracter = CellReference.convertNumToColString(columnFinalIndex + 1);

        return (columnInitialCaracter + (rowInitialIndex == 0 ? 0 : rowInitialIndex - 1)
                + ":" + columnFinalCaracter + (rowFinalIndex + 1));
    }
    
    /**
     * Crea la celda en base a los parámetros recibidos
     * 
     * @param infoRow
     * @param columnCounter
     * @param rowResult
     * @return 
     */
    private Cell createCell(Row infoRow, int columnCounter, String rowResult, String parameterName, String defaultValue) {
        Cell cell = infoRow.createCell(columnCounter);
        if (SGrindingReport.isNumeric(rowResult.replace(",", ""))) {
            cell.setCellValue(Double.parseDouble(rowResult.replace(",", "")));
            cell.setCellStyle(moStyleDecimals);
        }
        else {
            if (rowResult.length() == 0 && defaultValue != null && defaultValue.length() > 0) {
                cell.setCellValue(defaultValue);
            }
            else {
                cell.setCellValue(rowResult);
            }
        }
        
        if (parameterName.equals("--")) {
            cell.setCellStyle(moBackgroundBlackStyle);
        }
        
        return cell;
    }
    
    /**
     * Obtener molienda del mes
     * 
     * @param client
     * @param dtDate
     * @param idItem
     * @param idLot
     * @return 
     */
    private LinkedHashMap<Date, ArrayList<SGrindingResultReport>> getGrinding(SGuiClient client, Date dtDate, int idItem, int idLot) {
        LocalDate localDate = dtDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        
        String sDateStart = year + "-" + month + "-01";
        DateTime dateStart = DateTime.parse(sDateStart);
        DateTime lastDate = dateStart.dayOfMonth().withMaximumValue();
        
        LinkedHashMap<Date, ArrayList<SGrindingResultReport>> grinding = new LinkedHashMap<>();
        DateTime indexDate = dateStart;
        while(indexDate.isBefore(lastDate) || indexDate.isEqual(lastDate)) {
            ArrayList<SGrindingResultReport> results = this.getGrindingDay(client, indexDate.toDate(), idItem, idLot);
            grinding.put(indexDate.toDate(), results);
            
            indexDate = indexDate.plusDays(1);
        }
        
        return grinding;
    }

    /**
     * Obtiene los resultados del día de molienda
     * 
     * @param client
     * @param dtDate
     * @param idItem
     * @param idLot
     * @return 
     */
    private ArrayList<SGrindingResultReport> getGrindingDay(SGuiClient client, Date dtDate, int idItem, int idLot) {
        String msSql = "SELECT "
                + "v.id_result, "
                + "v.fk_param_id, "
                + "gp.param_code, "
                + "gp.parameter, "
                + "COALESCE(v.dt_capture, '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') AS dt_result, "
                + "gp.b_text, "
                + "gp.def_text_value, "
                + "v.capture_order, "
                + "v.fk_link_id_n, "
                + "v.capture_order AS order_link, "
                + "i.code AS item_code, "
                + "i.name AS item_name, "
                + "gp.details, "
                + " (@dg := (SELECT "
                + "  grinding_oil_perc "
                + "FROM "
                + " " + SModConsts.TablesMap.get(SModConsts.S_GRINDING) + " "
                + "WHERE "
                + " fk_item_id = v.fk_item_id "
                + " AND fk_prc_batch = v.fk_prc_batch "
                + " AND NOT b_del "
                + " AND dt_capture = v.dt_capture)) AS day_grinding,"
                + "v.result_08, "
                + "v.result_10, "
                + "v.result_12, "
                + "v.result_14, "
                + "v.result_16, "
                + "v.result_18, "
                + "v.result_20, "
                + "v.result_22, "
                + "v.result_00, "
                + "v.result_02, "
                + "v.result_04, "
                + "v.result_06,"
                + "@prom := COALESCE("
                + "(IF(COALESCE(v.result_08, 0) > 0, v.result_08, 0) +"
                + "IF(COALESCE(v.result_10, 0) > 0, v.result_10, 0) + "
                + "IF(COALESCE(v.result_12, 0) > 0, v.result_12, 0) + "
                + "IF(COALESCE(v.result_14, 0) > 0, v.result_14, 0) + "
                + "IF(COALESCE(v.result_16, 0) > 0, v.result_16, 0) + "
                + "IF(COALESCE(v.result_18, 0) > 0, v.result_18, 0) + "
                + "IF(COALESCE(v.result_20, 0) > 0, v.result_20, 0) + "
                + "IF(COALESCE(v.result_22, 0) > 0, v.result_22, 0) + "
                + "IF(COALESCE(v.result_00, 0) > 0, v.result_00, 0) + "
                + "IF(COALESCE(v.result_02, 0) > 0, v.result_02, 0) + "
                + "IF(COALESCE(v.result_04, 0) > 0, v.result_04, 0) + "
                + "IF(COALESCE(v.result_06, 0) > 0, v.result_06, 0)) / "
                + "(IF(v.result_08 > 0, 1, 0) + "
                + "IF(v.result_10 > 0, 1, 0) + "
                + "IF(v.result_12 > 0, 1, 0) + "
                + "IF(v.result_14 > 0, 1, 0) + "
                + "IF(v.result_16 > 0, 1, 0) + "
                + "IF(v.result_18 > 0, 1, 0) + "
                + "IF(v.result_20 > 0, 1, 0) + "
                + "IF(v.result_22 > 0, 1, 0) + "
                + "IF(v.result_00 > 0, 1, 0) + "
                + "IF(v.result_02 > 0, 1, 0) + "
                + "IF(v.result_04 > 0, 1, 0) + "
                + "IF(v.result_06 > 0, 1, 0)), 0) AS promedio, "
                + "@dg / " + 100 + " * @prom AS pond "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_PARAM) + " AS gp ON "
                + "gp.id_parameter = v.fk_param_id AND NOT gp.b_del "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "v.fk_item_id = i.id_item "
                + "WHERE v.dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "' AND v.fk_item_id = " + idItem + " "
                + "AND fk_prc_batch = " + idLot + " "
                + "ORDER BY v.capture_order ASC, v.fk_link_id_n ASC;";
        
        ArrayList<SGrindingResultReport> results = new ArrayList<>();
        
        try {
            ResultSet result = client.getSession().getStatement().getConnection().createStatement().executeQuery(msSql);
            
            SGrindingResultReport resultRow = null;
            while (result.next()) {
                resultRow = new SGrindingResultReport();
                
                resultRow.dtCapture = result.getDate("dt_result");
                
                resultRow.parameterId = result.getInt("fk_param_id");
                resultRow.parameterCode = result.getString("param_code");
                resultRow.parameterName = result.getString("parameter");
                resultRow.isText = result.getBoolean("b_text");
                resultRow.defaultTextValue = result.getString("def_text_value");
                
                resultRow.itemParamLinkId = result.getInt("fk_link_id_n");
                
                resultRow.itemCode = result.getString("item_code");
                resultRow.itemName = result.getString("item_name");
                
                resultRow.result08 = result.getString("v.result_08");
                resultRow.result10 = result.getString("v.result_10");
                resultRow.result12 = result.getString("v.result_12");
                resultRow.result14 = result.getString("v.result_14");
                resultRow.result16 = result.getString("v.result_16");
                resultRow.result18 = result.getString("v.result_18");
                resultRow.result20 = result.getString("v.result_20");
                resultRow.result22 = result.getString("v.result_22");
                resultRow.result00 = result.getString("v.result_00");
                resultRow.result02 = result.getString("v.result_02");
                resultRow.result04 = result.getString("v.result_04");
                resultRow.result06 = result.getString("v.result_06");
                
                resultRow.formulas = this.getFormulas(client, resultRow.itemParamLinkId);
                
                results.add(resultRow);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingReport.class.getName()).log(Level.SEVERE, null, ex);
            
            return new ArrayList<>();
        }
        
        return results;
    }
    
    private ArrayList<SDbGrindingLinkFormula> getFormulas(SGuiClient client, final int idLink) {
        String sql = "SELECT " +
                    "    f.id_formula " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_LINK_FORMULA) + " f " +
                    "        INNER JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_LINK_ITEM_PARAM) + " l ON f.fk_link = l.id_link " +
                    "WHERE " +
                    "    NOT f.b_del AND fk_link = " + idLink + " " +
                    "ORDER BY form_order ASC;";
        
        try {
            ResultSet result = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            ArrayList<SDbGrindingLinkFormula> formulas = new ArrayList<>();
            while(result.next()) {
                SDbGrindingLinkFormula form = new SDbGrindingLinkFormula();
                form.read(client.getSession(), new int[] { result.getInt("id_formula") });
                
                formulas.add(form);
            }
            
            return formulas;
            
        } catch (SQLException ex) {
            Logger.getLogger(SGrindingReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SGrindingReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        
        try {
            double d = Double.parseDouble(strNum);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        
        return true;
    }
}
