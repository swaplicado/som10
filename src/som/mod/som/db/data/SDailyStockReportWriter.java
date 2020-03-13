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
 
/**
 * Based on SimpleExcelWriterExample
 * 
 * @author Edwin Carmona, Isabel Servin
 */
public class SDailyStockReportWriter {
 
    public static boolean writer(ArrayList<ArrayList> prodEstimationByLine, 
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
            return false;
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
            
            return true;
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
        font.setBold(true);
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
 
}