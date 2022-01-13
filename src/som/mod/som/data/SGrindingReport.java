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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;
import som.mod.cfg.db.SDbLinkGrindingFormula;
import som.mod.som.db.SDbGrindingEvent;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbLot;

/**
 *
 * @author Edwin Carmona
 */
public class SGrindingReport {
    
    /**
     * Procesamiento del reporte
     * 
     * @param client
     * @param dtDate
     * @param idItem
     * @param idLot
     * @throws IOException
     * @throws Exception 
     */
    public void processReport(SGuiClient client, Date dtDate, int idItem, int idLot) throws IOException, Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        Calendar calendar = Calendar.getInstance();
        
        int year = calendar.get(Calendar.YEAR);
        DateTime dateStart = DateTime.parse(year + "-01-01");
        DateTime indexDate = dateStart.dayOfMonth().withMaximumValue();
        DateTime lastDate = new DateTime(dtDate);
        DateTime startMonth = null;
        lastDate = lastDate.dayOfMonth().withMaximumValue();
        
        SCaptureConfiguration cfg = SGrindingResultsUtils.getCfgFile(client, idItem);
        
        if (cfg == null) {
            return;
        }
        
        SDbLot oLot = new SDbLot();
        oLot.read(client.getSession(), new int [] { idLot });

        SDbItem oItem = new SDbItem(); 
        oItem.read(client.getSession(), new int [] { idItem });
        
        while(indexDate.isBefore(lastDate) || indexDate.isEqual(lastDate)) {
            // 
            calendar.setTime(indexDate.toDate());
            Month month = Month.of(calendar.get(Calendar.MONTH) + 1);
            XSSFSheet sheet = workbook.createSheet((month.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))).toUpperCase());

            ArrayList<SGrindingResumeRow> grindingRows = SGrindingResume.getResumeRows(client, indexDate.toDate(), idItem);
            SGrindingResumeRow last = grindingRows.remove(grindingRows.size() - 1);
            double rendTeo = last.getValue();
            
            startMonth = DateTime.parse(year + "-" + month.getValue() + "-01");
            ArrayList<SDbGrindingEvent> events = SGrindingResume.getGrindingEvents(client, startMonth.toDate(), indexDate.toDate());

            LinkedHashMap<Date, ArrayList<SGrindingResultReport>> info = this.getGrinding(client, indexDate.toDate(), idItem, idLot);
            
            this.generateReport(grindingRows, rendTeo, events, cfg, info, sheet, oLot, oItem);
            
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            
            indexDate = indexDate.plusMonths(1);
            indexDate = indexDate.dayOfMonth().withMaximumValue();
        }
        
        SGrindingResultsUtils.sendReport(client, workbook, oLot, oItem, dtDate);
    }

    /**
     * Genera la hoja del reporte mensual correspondiente al ítem en cuestión
     * 
     * @param resume
     * @param rendTeo
     * @param events
     * @param cfg
     * @param info
     * @param sheet
     * @param oLot
     * @param oItem
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void generateReport(ArrayList<SGrindingResumeRow> resume, double rendTeo,  
                            ArrayList<SDbGrindingEvent> events, SCaptureConfiguration cfg, 
                            LinkedHashMap<Date, ArrayList<SGrindingResultReport>> info, 
                            XSSFSheet sheet, SDbLot oLot, SDbItem oItem) throws FileNotFoundException, IOException {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat formatterTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        ObjectMapper mapper = new ObjectMapper();
        
        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
//        font.setBold(true);
        style.setFont(font);

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
        cellL.setCellValue(oLot.getLot() + " / " + formatter.format(oLot.getLotExpiration()));
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
         * Eventos de molienda
         */
        if (! events.isEmpty()) {

            Row eventsTitle = sheet.createRow(rowsCount++);
            Cell cellT = eventsTitle.createCell(1);
            cellT.setCellValue("Eventos de molienda:");
            for (SDbGrindingEvent event : events) {
                Row eventRow = sheet.createRow(rowsCount++);
                int columnCount = 1;
                Cell cell = eventRow.createCell(columnCount++);
                cell.setCellValue(event.getDescription());
                cell.setCellStyle(style);
                Cell cellV = eventRow.createCell(columnCount++);
                cellV.setCellValue(formatterTime.format(event.getStartDate()));
                Cell cellU = eventRow.createCell(columnCount++);
                cellU.setCellValue(formatterTime.format(event.getEndDate()));
            }
            
            sheet.createRow(rowsCount++);
        }
        
        /**
         * Tabla de resultados
         */
        // columnas
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
        
        /**
         * Tabla de resultados: datos
         */
        for (Map.Entry<Date, ArrayList<SGrindingResultReport>> entry : info.entrySet()) {
            Date keyDate = entry.getKey();
            ArrayList<SGrindingResultReport> rows = entry.getValue();
            
            sheet.createRow(rowsCount++);
            Row emtyRow = sheet.createRow(rowsCount++);
            Cell cellDateEmpty = emtyRow.createCell(1);
            cellDateEmpty.setCellValue("" + formatter.format(keyDate));
            
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
                
                //horas:
                
                if (! row.isText) {
                    if (cfg.r08.getIsActive()) {
                        Cell cell08 = infoRow.createCell(columnCount++);
                        cell08.setCellValue(row.result08);
                    }
                    if (cfg.r10.getIsActive()) {
                        Cell cell10 = infoRow.createCell(columnCount++);
                        cell10.setCellValue(row.result10);
                    }
                    if (cfg.r12.getIsActive()) {
                        Cell cell12 = infoRow.createCell(columnCount++);
                        cell12.setCellValue(row.result12);
                    }
                    if (cfg.r14.getIsActive()) {
                        Cell cell14 = infoRow.createCell(columnCount++);
                        cell14.setCellValue(row.result14);
                    }
                    if (cfg.r16.getIsActive()) {
                        Cell cell16 = infoRow.createCell(columnCount++);
                        cell16.setCellValue(row.result16);
                    }
                    if (cfg.r18.getIsActive()) {
                        Cell cell18 = infoRow.createCell(columnCount++);
                        cell18.setCellValue(row.result18);
                    }
                    if (cfg.r20.getIsActive()) {
                        Cell cell20 = infoRow.createCell(columnCount++);
                        cell20.setCellValue(row.result20);
                    }
                    if (cfg.r22.getIsActive()) {
                        Cell cell22 = infoRow.createCell(columnCount++);
                        cell22.setCellValue(row.result22);
                    }
                    if (cfg.r00.getIsActive()) {
                        Cell cell00 = infoRow.createCell(columnCount++);
                        cell00.setCellValue(row.result00);
                    }
                    if (cfg.r02.getIsActive()) {
                        Cell cell02 = infoRow.createCell(columnCount++);
                        cell02.setCellValue(row.result02);
                    }
                    if (cfg.r04.getIsActive()) {
                        Cell cell04 = infoRow.createCell(columnCount++);
                        cell04.setCellValue(row.result04);
                    }
                    if (cfg.r06.getIsActive()) {
                        Cell cell06 = infoRow.createCell(columnCount++);
                        cell06.setCellValue(row.result06);
                    }
                }
                else {
                    if (cfg.r08.getIsActive()) {
                        Cell cell08 = infoRow.createCell(columnCount++);
                        cell08.setCellValue(row.result08 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r10.getIsActive()) {
                        Cell cell10 = infoRow.createCell(columnCount++);
                        cell10.setCellValue(row.result10 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r12.getIsActive()) {
                        Cell cell12 = infoRow.createCell(columnCount++);
                        cell12.setCellValue(row.result12 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r14.getIsActive()) {
                        Cell cell14 = infoRow.createCell(columnCount++);
                        cell14.setCellValue(row.result14 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r16.getIsActive()) {
                        Cell cell16 = infoRow.createCell(columnCount++);
                        cell16.setCellValue(row.result16 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r18.getIsActive()) {
                        Cell cell18 = infoRow.createCell(columnCount++);
                        cell18.setCellValue(row.result18 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r20.getIsActive()) {
                        Cell cell20 = infoRow.createCell(columnCount++);
                        cell20.setCellValue(row.result20 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r22.getIsActive()) {
                        Cell cell22 = infoRow.createCell(columnCount++);
                        cell22.setCellValue(row.result22 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r00.getIsActive()) {
                        Cell cell00 = infoRow.createCell(columnCount++);
                        cell00.setCellValue(row.result00 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r02.getIsActive()) {
                        Cell cell02 = infoRow.createCell(columnCount++);
                        cell02.setCellValue(row.result02 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r04.getIsActive()) {
                        Cell cell04 = infoRow.createCell(columnCount++);
                        cell04.setCellValue(row.result04 < 0d ? row.defaultTextValue : "");
                    }
                    if (cfg.r06.getIsActive()) {
                        Cell cell06 = infoRow.createCell(columnCount++);
                        cell06.setCellValue(row.result06 < 0d ? row.defaultTextValue : "");
                    }
                }
                
                /**
                 * Fórmulas
                 */
                if (row.formulas.size() > 0) {
                    boolean bRowAdded = false;
                    for (SDbLinkGrindingFormula formula : row.formulas) {
                        SFormulasRow formObj = mapper.readValue(formula.getFormula(), SFormulasRow.class);
                        boolean isNewRow = (formula.getRowText() != null && formula.getRowText().length() > 0) || formObj.getR08().getIndexRow() != 0;
                        if (formObj.r08.getIsActive()) {
                            if (isNewRow) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR08().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR08 = formulaRow.createCell(formObj.getR08().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR08().getFormula(), rIndex);
                                cellR08.setCellFormula(formulaS);
                                
                                bRowAdded = true;
                                infoRow = formulaRow;
                            }
                            else {
                                Cell cellR08 = infoRow.createCell(formObj.getR08().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR08().getFormula(), rowIndex);
                                cellR08.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r10.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR10().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR10 = formulaRow.createCell(formObj.getR10().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR10().getFormula(), rIndex);
                                cellR10.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR10 = infoRow.createCell(formObj.getR10().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR10().getFormula(), rowIndex);
                                cellR10.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r12.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR12().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR12 = formulaRow.createCell(formObj.getR12().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR12().getFormula(), rIndex);
                                cellR12.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR12 = infoRow.createCell(formObj.getR12().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR12().getFormula(), rowIndex);
                                cellR12.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r14.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR14().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR14 = formulaRow.createCell(formObj.getR14().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR14().getFormula(), rIndex);
                                cellR14.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR14 = infoRow.createCell(formObj.getR14().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR14().getFormula(), rowIndex);
                                cellR14.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r16.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR16().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR16 = formulaRow.createCell(formObj.getR16().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR16().getFormula(), rIndex);
                                cellR16.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR16 = infoRow.createCell(formObj.getR16().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR16().getFormula(), rowIndex);
                                cellR16.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r18.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR18().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR18 = formulaRow.createCell(formObj.getR18().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR18().getFormula(), rIndex);
                                cellR18.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR18 = infoRow.createCell(formObj.getR18().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR18().getFormula(), rowIndex);
                                cellR18.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r20.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR20().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR20 = formulaRow.createCell(formObj.getR20().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR20().getFormula(), rIndex);
                                cellR20.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR20 = infoRow.createCell(formObj.getR20().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR20().getFormula(), rowIndex);
                                cellR20.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r22.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR22().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR22 = formulaRow.createCell(formObj.getR22().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR22().getFormula(), rIndex);
                                cellR22.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR22 = infoRow.createCell(formObj.getR22().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR22().getFormula(), rowIndex);
                                cellR22.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r00.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR00().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR00 = formulaRow.createCell(formObj.getR00().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR00().getFormula(), rIndex);
                                cellR00.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR00 = infoRow.createCell(formObj.getR00().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR00().getFormula(), rowIndex);
                                cellR00.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r02.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR02().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR02 = formulaRow.createCell(formObj.getR02().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR02().getFormula(), rIndex);
                                cellR02.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR02 = infoRow.createCell(formObj.getR02().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR02().getFormula(), rowIndex);
                                cellR02.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r04.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR04().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR04 = formulaRow.createCell(formObj.getR04().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR04().getFormula(), rIndex);
                                cellR04.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR04 = infoRow.createCell(formObj.getR04().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR04().getFormula(), rowIndex);
                                cellR04.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.r06.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getR06().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellR06 = formulaRow.createCell(formObj.getR06().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR06().getFormula(), rIndex);
                                cellR06.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellR06 = infoRow.createCell(formObj.getR06().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getR06().getFormula(), rowIndex);
                                cellR06.setCellFormula(formulaS);
                            }
                        }
                        if (formObj.rExtra.getIsActive()) {
                            if (isNewRow && !bRowAdded) {
                                int formulaRowIndex = rowsCount++;
                                int columnRowCount = 1;
                                int rIndex = formulaRowIndex + formObj.getrExtra().getIndexRow();
                                Row formulaRow = formulaRow = sheet.createRow(rIndex);
                                
                                 //Fecha
                                Cell cellFormulaDate = formulaRow.createCell(columnRowCount++);
                                cellFormulaDate.setCellValue("" + formatter.format(keyDate));

                                // Texto
                                Cell cellFormulaText = formulaRow.createCell(columnRowCount++);
                                cellFormulaText.setCellValue(formula.getRowText().toUpperCase());
                                
                                Cell cellrExtra = formulaRow.createCell(formObj.getrExtra().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getrExtra().getFormula(), rIndex);
                                cellrExtra.setCellFormula(formulaS);
                            }
                            else {
                                Cell cellrExtra = infoRow.createCell(formObj.getrExtra().getColNumber());
                                String formulaS = SGrindingResultsUtils.getFormula(formObj.getrExtra().getFormula(), rowIndex);
                                cellrExtra.setCellFormula(formulaS);
                            }
                        }
                    }
                }
            }
        }
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
                + "lip.fk_parameter_id, "
                + "gp.param_code, "
                + "gp.parameter, "
                + "COALESCE(v.dt_capture, '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') AS dt_result, "
                + "lip.fk_parameter_id, "
                + "gp.b_text, "
                + "gp.def_text_value, "
                + "v.capture_order, "
                + "lip.id_link, "
                + "lip.capture_order AS order_link, "
                + "i.code AS item_code, "
                + "i.name AS item_name, "
                + "gp.details, "
                + " (@dg := (SELECT "
                + "  grinding_oil_perc "
                + "FROM "
                + " su_grinding "
                + "WHERE "
                + " fk_item_id = lip.fk_item_id "
                + " AND fk_lot_id = v.fk_lot_id "
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
                + "(@prom := (COALESCE(v.result_08, 0) + "
                + "COALESCE(v.result_10, 0) + "
                + "COALESCE(v.result_12, 0) + "
                + "COALESCE(v.result_14, 0) + "
                + "COALESCE(v.result_16, 0) + "
                + "COALESCE(v.result_18, 0) + "
                + "COALESCE(v.result_20, 0) + "
                + "COALESCE(v.result_22, 0) + "
                + "COALESCE(v.result_00, 0) + "
                + "COALESCE(v.result_02, 0) + "
                + "COALESCE(v.result_04, 0) + "
                + "COALESCE(v.result_06, 0)) / "
                + "(IF(v.result_08 > 0, 1, 0) + "
                + "    IF(v.result_10 > 0, 1, 0) + "
                + "    IF(v.result_12 > 0, 1, 0) + "
                + "    IF(v.result_14 > 0, 1, 0) + "
                + "    IF(v.result_16 > 0, 1, 0) + "
                + "    IF(v.result_18 > 0, 1, 0) + "
                + "    IF(v.result_20 > 0, 1, 0) + "
                + "    IF(v.result_22 > 0, 1, 0) + "
                + "    IF(v.result_00 > 0, 1, 0) + "
                + "    IF(v.result_02 > 0, 1, 0) + "
                + "    IF(v.result_04 > 0, 1, 0) + "
                + "    IF(v.result_06 > 0, 1, 0))) AS promedio, "
                + "(@dg / " + 100 + " * @prom) AS pond "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_RESULTS) + " AS v "
                + "RIGHT JOIN " + SModConsts.TablesMap.get(SModConsts.CU_LINK_ITEM_PARAM) + " AS lip "
                + "ON (lip.fk_parameter_id = v.fk_parameter_id AND lip.fk_item_id = v.fk_item_id) AND NOT lip.b_del "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_PARAMS) + " AS gp ON "
                + "gp.id_parameter = lip.fk_parameter_id AND NOT gp.b_del "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "lip.fk_item_id = i.id_item "
                + "WHERE v.dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "' AND v.fk_item_id = " + idItem + " "
                + "AND fk_lot_id = " + idLot + " "
                + "ORDER BY v.capture_order ASC, lip.capture_order ASC;";
        
        ArrayList<SGrindingResultReport> results = new ArrayList<>();
        
        try {
            ResultSet result = client.getSession().getStatement().getConnection().createStatement().executeQuery(msSql);
            
            SGrindingResultReport resultRow = null;
            while (result.next()) {
                resultRow = new SGrindingResultReport();
                
                resultRow.dtCapture = result.getDate("dt_result");
                
                resultRow.parameterId = result.getInt("fk_parameter_id");
                resultRow.parameterCode = result.getString("param_code");
                resultRow.parameterName = result.getString("parameter");
                resultRow.isText = result.getBoolean("b_text");
                resultRow.defaultTextValue = result.getString("def_text_value");
                
                resultRow.itemParamLinkId = result.getInt("id_link");
                
                resultRow.itemCode = result.getString("item_code");
                resultRow.itemName = result.getString("item_name");
                
                resultRow.result08 = result.getDouble("v.result_08");
                resultRow.result10 = result.getDouble("v.result_10");
                resultRow.result12 = result.getDouble("v.result_12");
                resultRow.result14 = result.getDouble("v.result_14");
                resultRow.result16 = result.getDouble("v.result_16");
                resultRow.result18 = result.getDouble("v.result_18");
                resultRow.result20 = result.getDouble("v.result_20");
                resultRow.result22 = result.getDouble("v.result_22");
                resultRow.result00 = result.getDouble("v.result_00");
                resultRow.result02 = result.getDouble("v.result_02");
                resultRow.result04 = result.getDouble("v.result_04");
                resultRow.result06 = result.getDouble("v.result_06");
                
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
    
    private ArrayList<SDbLinkGrindingFormula> getFormulas(SGuiClient client, final int idLink) {
        String sql = "SELECT " +
                    "    f.id_formula " +
                    "FROM " +
                    "    cu_link_forms f " +
                    "        INNER JOIN " +
                    "    som_com.cu_link_itm_params l ON f.fk_link = l.id_link " +
                    "WHERE " +
                    "    NOT f.b_del AND fk_link = " + idLink + " " +
                    "ORDER BY form_order ASC;";
        
        try {
            ResultSet result = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            ArrayList<SDbLinkGrindingFormula> formulas = new ArrayList<>();
            while(result.next()) {
                SDbLinkGrindingFormula form = new SDbLinkGrindingFormula();
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
}
