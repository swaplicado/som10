package som.mod.som.db.data;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sa.lib.SLibUtils;
 
/**
 * Based on SimpleExcelWriterExample
 * 
 * @author Edwin Carmona, Isabel Servin
 */
public class SDailyStockReportWriter {
 
    public static File writer(ArrayList<ArrayList> prodEstimationByLine, 
                                    ArrayList<ArrayList> prodEstimationEty, 
                                    ArrayList<ArrayList> productionStock, 
                                    ArrayList<ArrayList> resumeStock, 
                                    ArrayList<ArrayList> allStock) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheetEst = workbook.createSheet("Estimacion_de_produccion");
        
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("Fecha estimación");
        columnNames.add("Fecha toma física");
        columnNames.add("Código");
        columnNames.add("Línea de producción");
        columnNames.add("Producto terminado");
        columnNames.add("Unidad");
//        columnNames.add("Reproceso");
//        columnNames.add("Deshecho");
//        columnNames.add("Materia prima");
        
        SDailyStockReportWriter.write(sheetEst, 1, columnNames, prodEstimationByLine);
        
        columnNames.clear();
        columnNames.add("Fecha estimación");
        columnNames.add("Fecha toma física");
        columnNames.add("Código");
        columnNames.add("Almacén");
        columnNames.add("Código");
        columnNames.add("Línea de producción");
        columnNames.add("Producto terminado");
//        columnNames.add("Reproceso");
//        columnNames.add("Deshecho");
//        columnNames.add("Materia prima");
        
        SDailyStockReportWriter.write(sheetEst, prodEstimationByLine.size() + 4, columnNames, prodEstimationEty);
        
        XSSFSheet sheet = workbook.createSheet("Existencias_aguacate");
        
        columnNames.clear();
        columnNames.add("Código");
//        columnNames.add("Tanque");
//        columnNames.add("Código Ítem");
        columnNames.add("Ítem");
//        columnNames.add("Entradas");
//        columnNames.add("Salidas");
        columnNames.add("Inventario disponible");
        columnNames.add("Capacidad del tanque");
        columnNames.add("Borras y restos");
        columnNames.add("Espacio disponible");
        columnNames.add("Unidad");
        columnNames.add("Acción/Clasificación");
        columnNames.add("Acidez");
        columnNames.add("Altura tanque(cm)");
        
        SDailyStockReportWriter.write(sheet, 1, columnNames, productionStock);
        
        columnNames.clear();        
        columnNames.add("Clasificación");
        columnNames.add("Inventario disponible");
        columnNames.add("Acidez ponderada");
        
        SDailyStockReportWriter.write(sheet, productionStock.size() + 4, columnNames, resumeStock);
        
        XSSFSheet sheetStock = workbook.createSheet("Existencias");
        
        columnNames.clear();
        columnNames.add("Código");
//        columnNames.add("Tanque");
//        columnNames.add("Código Ítem");
        columnNames.add("Ítem");
//        columnNames.add("Entradas");
//        columnNames.add("Salidas");
        columnNames.add("Inventario disponible");
        columnNames.add("Unidad");
        
        
        SDailyStockReportWriter.write(sheetStock, 1, columnNames, allStock);
        
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Microsoft office Excel", "xlsx");
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new File("reporte" + (new Date()).getTime() + ".xlsx"));

        int iSelection = fileChooser.showSaveDialog(fileChooser);

        if (iSelection == JFileChooser.CANCEL_OPTION) {
            return null;
        }

        File fileName = fileChooser.getSelectedFile();

        if (fileName == null) {
            throw new IOException("Nombre de archivo inválido");
        }
        else {
            String fn = fileName.getName();
            if (! fn.substring(fn.lastIndexOf("."), fn.length()).toLowerCase().equals(".xlsx")) {
                throw new IOException("Nombre de archivo inválido");
            }
        }
        
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            
            return fileName;
        }
    }
    
    private static void write(Sheet sheet, final int startRow, ArrayList<String> columnNames, ArrayList<ArrayList> content) {
        SDailyStockReportWriter.createHeaderRow(sheet, columnNames, startRow);

        CellStyle qtyStyle = sheet.getWorkbook().createCellStyle();
        qtyStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("0.0000"));
        
        int rowCount = startRow; 
        for (ArrayList oRow : content) {
            Row row = sheet.createRow(++rowCount);
             
            int columnCount = 0;
            for (Object o : oRow) {
                Cell cell = row.createCell(++columnCount);
                if (o instanceof String) {
                    cell.setCellValue((String) o);
                }
                else if (o instanceof Double) {
                    cell.setCellStyle(qtyStyle);
                    cell.setCellValue((Double) o);
                }
            }
        }
        
        for (int i = 0; i < 10; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private static void createHeaderRow(Sheet sheet, ArrayList<String> columns, final int startRow) {
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
//        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        cellStyle.setFont(font);

        Row row = sheet.createRow(startRow);
        
        int columnIndex = 1;
        for (String colName : columns) {
            Cell cellTitle = row.createCell(columnIndex++);
            cellTitle.setCellStyle(cellStyle);
            cellTitle.setCellValue(colName);
        }
    }
 
    public static String createMailBody(ArrayList<ArrayList> prodEstimationByLine, 
                                    ArrayList<ArrayList> prodEstimationEty, 
                                    ArrayList<ArrayList> productionStock, 
                                    ArrayList<ArrayList> resumeStock, 
                                    ArrayList<ArrayList> allStock) {
        String bodyMail = "";
        
        // HTML:
        
        bodyMail = "<!DOCTYPE html>\n" +
                    "<html lang=\"es\">\n";
        
        // HTML head:
        
        bodyMail += "<head>\n" +
                        "<meta charset=\"UTF-8\">\n" +
                        "<title>Title of the document</title>\n" +
                        SDailyStockReportWriter.styleOfMail() + "\n" +
                    "</head>\n" +
                    "<body>\n";
        
        ArrayList<String> estColumnNames = new ArrayList<>();
        estColumnNames.add("Fecha estimación");
        estColumnNames.add("Fecha toma física");
        estColumnNames.add("Código");
        estColumnNames.add("Línea de producción");
        estColumnNames.add("Producto terminado");
        estColumnNames.add("Unidad");
        
        ArrayList<String> estWhsColumnNames = new ArrayList<>();
        estWhsColumnNames.add("Fecha estimación");
        estWhsColumnNames.add("Fecha toma física");
        estWhsColumnNames.add("Código");
        estWhsColumnNames.add("Almacén");
        estWhsColumnNames.add("Código");
        estWhsColumnNames.add("Línea de producción");
        estWhsColumnNames.add("Producto terminado");
        estWhsColumnNames.add("Unidad");
        
        ArrayList<String> AvocadoColNames = new ArrayList<>();
        AvocadoColNames.add("Código");
//        columnNames.add("Tanque");
//        columnNames.add("Código Ítem");
        AvocadoColNames.add("Ítem");
//        columnNames.add("Entradas");
//        columnNames.add("Salidas");
        AvocadoColNames.add("Inventario disponible");
        AvocadoColNames.add("Capacidad del tanque");
        AvocadoColNames.add("Borras y restos");
        AvocadoColNames.add("Espacio disponible");
        AvocadoColNames.add("Unidad");
        AvocadoColNames.add("Acción/Clasificación");
        AvocadoColNames.add("Acidez");
        AvocadoColNames.add("Altura tanque(cm)");
        
        ArrayList<String> resumeColNames = new ArrayList<>();
        resumeColNames.add("Clasificación");
        resumeColNames.add("Inventario disponible");
        resumeColNames.add("Acidez ponderada");
        
        ArrayList<String> stkColNames = new ArrayList<>();
        stkColNames.add("Código");
//        columnNames.add("Tanque");
//        columnNames.add("Código Ítem");
        stkColNames.add("Ítem");
//        columnNames.add("Entradas");
//        columnNames.add("Salidas");
        stkColNames.add("Inventario disponible");
        stkColNames.add("Unidad");
        
        bodyMail += SDailyStockReportWriter.getDivOfTable("Estimación por línea de producción", estColumnNames, prodEstimationByLine);
        bodyMail += "<br>\n";
        bodyMail += "<br>\n";
        bodyMail += SDailyStockReportWriter.getDivOfTable("Estimación por tanque", estWhsColumnNames, prodEstimationEty);
        bodyMail += "<br>\n";
        bodyMail += "<br>\n";
        bodyMail += SDailyStockReportWriter.getDivOfTable("Existencias Aguacate", AvocadoColNames, productionStock);
        bodyMail += "<br>\n";
        bodyMail += "<br>\n";
        bodyMail += SDailyStockReportWriter.getDivOfTable("Resumen", resumeColNames, resumeStock);
        bodyMail += "<br>\n";
        bodyMail += "<br>\n";
        bodyMail += SDailyStockReportWriter.getDivOfTable("Existencias", stkColNames, allStock);
        
        bodyMail += "</body>\n";
        bodyMail += "</html>";
        
        return bodyMail;
    }
    
    private static String getDivOfTable(String title, ArrayList<String> columnNames, ArrayList<ArrayList> content) {
        String bodyMail = "<div>\n" +
                "<h3>" + SLibUtils.textToHtml(title) + "</h3>\n" +
                "<table>\n" +
                    "<tr>\n";
        
        for (String estColumnName : columnNames) {
            bodyMail += "<th>" + SLibUtils.textToHtml(estColumnName) + "</th>\n";
        }
        
        bodyMail += "</tr>\n";
        for (ArrayList oRow : content) {
            bodyMail += "<tr>\n";
             
            for (Object o : oRow) {
                if (o instanceof String) {
                    bodyMail += "<td>" + SLibUtils.textToHtml(o.toString()) + "</td>\n";
                }
                else if (o instanceof Double) {
                    bodyMail += "<td style=\"text-align:right;\">" + SLibUtils.getDecimalFormatQuantity().format(o) + "</td>\n";
                }
            }
            
            bodyMail += "</tr>\n";
        }
        bodyMail += "</table>\n";
        bodyMail += "</div>";
        
        return bodyMail;
    }
    
    private static String styleOfMail() {
        return "<style>\n" +
                "table {\n" +
                "font-family: arial, sans-serif;\n" +
                "border-collapse: collapse;\n" +
                "font-size: 90%;\n" +
                "width: 90%;\n" +
                "    }\n" +
                "\n" +
                "    td {\n" +
                "border: 1px solid #dddddd;\n" +
                "text-align: left;\n" +
                "padding: 8px;\n" +
                "    }\n" +
                "\n" +
                "    th {\n" +
                "border: 1px solid #dddddd;\n" +
                "text-align: center;\n" +
                "padding: 8px;\n" +
                "    }\n" +
                "\n" +
                "    tr:nth-child(even) {\n" +
                "background-color: #dddddd;\n" +
                "    }\n" +
            "</style>";
    }
}