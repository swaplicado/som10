/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import erp.lib.SLibUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;
import som.mod.som.form.SDialogReportPreview;

/**
 *
 * @author Isabel Servín
 */
public class SSomDryerReport {
    
    private static final int FONT_SIZE_TBL = 1;
    private static int YEARS_TO_REPORT = 3;
    private static final int YEARS_DETAIL = 2;
    private static final int SQL_BY_MONTH = 1;
    private static final int SQL_BY_YEAR = 2;
    private static final String HTML_PREVIEW = "<h4>";
    
    private final SGuiClient miClient;
    private Connection moConnectionRevuelta;
    
    public SSomDryerReport(SGuiClient client) {
        miClient = client;
    }
    
    public void sendReport(int[] pk, String mails) {
        try {
            moConnectionRevuelta = SSomUtils.openConnectionRevueltaJdbc(miClient.getSession());
            if (moConnectionRevuelta == null) {
                miClient.showMsgBoxWarning("No se pudo establecer comunicación con el sistema Revuelta.");
            }
            
            setYearsToReport();
            
            SDbDryerReport dryRp = getDryerReportByPk(pk);

            SDialogReportPreview preview = new SDialogReportPreview(miClient, generateReportHtml(dryRp, true));
            preview.setVisible(true);

            if (preview.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                String mailSubject = "[SOM] Reporte secador " + SLibUtils.DateFormatDate.format(dryRp.getDate());

                ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mails, ";")));

                SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "Aeth2021*s.", "som@aeth.mx");
                
                String mailBody = generateReportHtml(dryRp, false);

                SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(mailSubject), mailBody, recipientsTo);

                mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
                mail.send();
                System.out.println("mail send!");
                miClient.showMsgBoxInformation("Reporte enviado!");
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private String generateReportHtml(SDbDryerReport dryRp, boolean preview) throws Exception {
        Statement statement = miClient.getSession().getStatement();
        Base64.Encoder encoder = Base64.getEncoder();
        ResultSet resultSet;
        BufferedImage image;
        byte[] imageBytes; 
        File file;
        String encoding;
        String sql;
        
        String html = "<html>" +
                "<head>" ;
        html += "<style> "
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
                + " font-size: 0.875em;"
                + " font-family: sans-serif;"
                + "} "
                + "th, td {"
                + " border: 1px solid black;"
                + " border-collapse: collapse;"
                + "padding-left: 5px; padding-right: 5px;"
                + "} "
//                + "th {"
//                + " text-align: center;"
//                + " background-color: DarkSlateGray;"
//                + " color: white;"
//                + "} "
                + ".sinBorde table{border: 0; border-bottom:1px solid #000}"
                + "</style> ";
        html += "</head>" +
                "<body>" +
                "AETH01RG001-007.1 <br>" +
                "<h1>" + SLibUtils.textToHtml("Reporte de secador y pelletizado al día " + SLibUtils.DateFormatDate.format(dryRp.getDate()) + "") + "</h1>";
        html+= "<h1>";
        
        // Inicia primera tabla sólo para formato
        
        html += "<table>" +
                "<tr>" + 
                "<td rowspan='2'>"; 
                
        // Tabla resumen por año: 
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" +
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr><b>" +
                "<th align='center' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Fecha") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo húmedo procesado (kg)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Producción promedio (kg/hr)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% eficiencia promedio") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Producción total pellet (kg)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% prom. pond. aceite pellet") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% prom. pond. aceite bagazo seco") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% prom. pond. humedad bagazo pelletizado") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% prom. pond. acidez bagazo pelletizado") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% prom. pond. acidez aguacatera") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Inventario pellet extracción (kg)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Pellet a desecho (kg)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Saldo pellet inventario (kg)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Aceite extraido pellet (kg)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Producción teórica aceite pellet (kg)") + "</b></th>" + 
                "</b></tr>";
        
        for (int i = YEARS_TO_REPORT; i > 0; i--) {
            sql = "";
            if (i <= YEARS_DETAIL) {
                sql = getSqlDryerResume(SQL_BY_MONTH, SLibUtils.DateFormatDateYear.format(SLibTimeUtils.addDate(dryRp.getDate(), -(i-1), SLibConsts.UNDEFINED, SLibConsts.UNDEFINED)));
            }
            if (!sql.isEmpty()) {
                // RESUMEN POR MES
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    html += "<tr>" + 
                            "<td align='left' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DateFormatDateMonthYearLong.format(resultSet.getDate("fecha"))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("hum_proc"), 0))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("prod_prom"), 0))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("ef_prom"))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("prod_tot"), 0))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("ace_pellet"))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("ace_bagazo"))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("humedad"))) + "</td>" + // 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("aci_pellet"))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("aci_agua"))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("pellet_ext"), 0))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("pellet_desecho"), 0))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("inventario"), 0))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("aceite_pellet"), 0))) + "</td>" + 
                            "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("prod_tot"), 0) * resultSet.getDouble("ace_pellet"))) + "</td>" + 
                            "</tr>" ; 
                }
            }
            // RESUMEN POR AÑO
            sql = getSqlDryerResume(SQL_BY_YEAR, SLibUtils.DateFormatDateYear.format(SLibTimeUtils.addDate(dryRp.getDate(), -(i-1), SLibConsts.UNDEFINED, SLibConsts.UNDEFINED)));
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                html += "<tr style='background-color: silver'><b>" + 
                        "<td align='left' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DateFormatDateYear.format(resultSet.getDate("fecha"))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("hum_proc"), 0))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("prod_prom"), 0))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("ef_prom"))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("prod_tot"), 0))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("ace_pellet"))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("ace_bagazo"))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("humedad"))) + "</td>" + // 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("aci_pellet"))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("aci_agua"))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("pellet_ext"), 0))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("pellet_desecho"), 0))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("inventario"), 0))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("aceite_pellet"), 0))) + "</td>" + 
                        "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("prod_tot"), 0) * resultSet.getDouble("ace_pellet"))) + "</td>" + 
                        "</b></tr>" ; 
            } 
        }
        html += "</font>" +
                "</table>";
        
        html += "</td>" + 
                "<td>" ;
                // tabla inventario fosa:
        
        sql = "SELECT " +
                "SUM(pellet_net_wei_r) pelletizado, " +
                "SUM(pit_proc_fruit) * " + dryRp.getPitBagasseGrindingFactor() + " c, " +
                "SUM(seed_peel_to_plant) * " + dryRp.getPitBagasseGrindingFactor() + " d, " +
                "SUM(bagasse_to_plant) * " + dryRp.getPitBagasseExternalFactor()+ " e " +
                "FROM s_dryer_rep " +
                "WHERE MONTH(ADDDATE(dt, INTERVAL -1 DAY)) = MONTH('" + SLibUtils.DbmsDateFormatDate.format(dryRp.getDate()) + "') " +
                "AND YEAR(ADDDATE(dt, INTERVAL -1 DAY)) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(dryRp.getDate()) + "');";
        resultSet = statement.executeQuery(sql);
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" +
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<th align='center' style='background-color: navy' colspan='3'><font color='yellow'><b>" + SLibUtils.textToHtml("Inventario fosa de bagazo kgs") + "</b></font></th>" + 
                "</tr>";
        if (resultSet.next()) {
            html += "<tr>" +
                    "<td align='right'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo en planta al día de hoy") + "<b> (A)+(B)</b>" + "</td>" + 
                    "<td align='center' style='background-color: navy'><font color='yellow'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("A+B") + "</font></td>" + 
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(dryRp.getSeedPeelToProcess() + dryRp.getPitContentEmpty())) + "</td>" + 
                    "</tr>" + 
                    "<tr>" +
                    "<td align='right'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo en planta x proc. (a partir de HyC)") + "<b> (A)</b>" + "</td>" + 
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(dryRp.getSeedPeelToProcess(), 0))) + "</td>" + 
                    "<td align='center' style='background-color: navy'><font color='yellow'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("T") + "</font></td>" + 
                    "</tr>" + 
                    "<tr>" +
                    "<td align='right'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo en fosa al día de hoy") + "<b> (B)</b>" + "</td>" + 
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(dryRp.getPitContentEmpty(), 0))) + "</td>" + 
                    "<td align='center' style='background-color: navy'><font color='yellow'>" + SLibUtils.textToHtml("O") + "</font></td>" + 
                    "</tr>" + 
                    "<tr>" +
                    "<td align='right'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo pelletizado total del mes") + "</td>" + 
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("pelletizado"), 0))) + "</b></td>" + 
                    "<td align='center' style='background-color: navy'><font color='yellow'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("T") + "</font></td>" + 
                    "</tr>" + 
                    "<tr>" +
                    "<td align='right'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo fruta producido mes") + "<b> (C)</b>" + "</td>" + 
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("c"), 0))) + "</b></td>" + 
                    "<td align='center' style='background-color: navy'><font color='yellow'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("A") + "</font></td>" + 
                    "</tr>" +
                    "<tr>" +
                    "<td align='right'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo generado a partir de HyC mes") + "<b> (D)</b>" + "</td>" + 
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("d"), 0))) + "</b></td>" + 
                    "<td align='center' style='background-color: navy'><font color='yellow'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("L") + "</font></td>" + 
                    "</tr>" +
                    "<tr>" +
                    "<td align='right'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo generado a partir de bagazo mes") + "<b> (E)</b>" + "</td>" + 
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("e"), 0))) + "</b></td>" + 
                    "<td align='center' style='background-color: navy'><font color='yellow'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("") + "</font></td>" + 
                    "</tr>" +
                    "<tr>" +
                    "<td align='right'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo total producido al mes") + "</td>" + 
                    "<td align='center' style='background-color: navy'><font color='yellow'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("C+D+E") + "</font></td>" + 
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("c") + resultSet.getDouble("d") + resultSet.getDouble("e"), 0))) + "</b></td>" + 
                    "</tr>"
                    ;
        }
        html += "</font>" + 
                "</table>" +
                "</td>" + 
                "</tr>" + 
                "<tr>" +
                "<td>";
                
        
        // Tabla HYC:
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" +
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<th align='center'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Fecha") + "</b></th>" +
                "<th align='center'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Aceite obtenido kg") + "</b></th>" +
                "<th align='center'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("HYC procesado kg") + "</b></th>" +
                "<th align='center'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Rendim. %") + "</b></th>" + 
                "</tr>";
        
        sql = "SELECT ADDDATE(dt, INTERVAL -1 DAY) fecha, " +
                "seed_peel_oil acei, " +
                "seed_peel_proc proc, " +
                "seed_peel_oil/seed_peel_proc rend " +
                "FROM s_dryer_rep " +
                "WHERE dt BETWEEN ADDDATE('" + SLibUtils.DbmsDateFormatDate.format(dryRp.getDate()) + "', INTERVAL -2 DAY) " +
                "AND '" + SLibUtils.DbmsDateFormatDate.format(dryRp.getDate()) + "';";
        resultSet = statement.executeQuery(sql);
        
        while (resultSet.next()) {
            html += "<tr>" + 
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DateFormatDateShortMonth.format(resultSet.getDate("fecha"))) + "</td>" +
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("acei"), 0))) + "</td>" +
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("proc"), 0))) + "</td>" +
                    "<td align='center'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("rend"))) + "</td>" +
                    "</tr>" 
                    ;
            }
        
        html += "</font>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "</table>";
        
        // Fin de la primera tabla sólo para formato
        
        // Inicia segunda tabla sólo para formato
        
        html += "<table>" +
                "<tr>" + 
                "<td>"; 
        
        // tabla ultimos 10 días:
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" +
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<th align='center' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Fecha") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Bagazo húmedo procesado (kg)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Producción promedio (kg/hr)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% eficiencia promedio") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Producción total pellet (kg)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% promedio aceite pellet") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% aceite bagazo seco") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% humedad bagazo pelletizado") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% acidez en bagazo pelletizado") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% acidez planta aguacatera") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: maroon'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Aceite estimado (kg)") + "</b></th>" + 
                "<th align='center' style='width:100px' style='background-color: navy'><font color='yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Fecha") + "</b></font></th>" + 
                "<th align='center' style='width:100px' style='background-color: navy'><font color='yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("% eficiencia") + "</b></font></th>" + 
                "<th align='center' style='width:100px' style='background-color: navy'><font color='yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml("Monóxido de carbono (ppm)") + "</b></font></th>" + 
                "</tr>"; 
        
        sql = "SELECT ADDDATE(dt, INTERVAL -1 DAY) AS fecha, " +
                "pellet_net_wei_r / 0.28 AS hum_proc, " +
                "(pellet_net_wei_r / operative_hrs) / 0.28 AS prod_prom, " +
                "if(((pellet_net_wei_r / operative_hrs) / 0.28) = 0, 0, ((pellet_net_wei_r / operative_hrs) / 0.28) / goal_pellet_proc_per_hr) AS ef_prom, " +
                "pellet_net_wei_r AS prod_tot, " +
                "lab_pellet_oil_n AS ace_pellet, " +
                "lab_avg_bag_oil_n AS ace_bagazo, " +
                "lab_moi_n AS humedad, " +
                "lab_pellet_aci_n AS aci_pellet, " +
                "lab_avo_aci_n AS aci_agua, " +
                "pellet_net_wei_r * lab_pellet_oil_n AS ace_prom_ext, " +
                "ss_effcy AS eficiencia, " +
                "ss_c_monoxide AS monoxido " +
                "FROM s_dryer_rep " +
                "WHERE dt BETWEEN ADDDATE('" + SLibUtils.DbmsDateFormatDate.format(dryRp.getDate()) + "', INTERVAL -9 DAY) " +
                "AND '" + SLibUtils.DbmsDateFormatDate.format(dryRp.getDate()) + "';";
        resultSet = statement.executeQuery(sql);
        
        while (resultSet.next()) {
            html += "<tr>" +
                    "<font size='" + FONT_SIZE_TBL + "'>" + 
                    "<td align='left' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DateFormatDateShortMonth.format(resultSet.getDate("fecha"))) + "</td>" + 
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("hum_proc"), 0))) + "</td>" + 
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("prod_prom"), 0))) + "</td>" + 
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("ef_prom"))) + "</td>" + 
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("prod_tot"), 0))) + "</td>" + 
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("ace_pellet"))) + "</td>" + 
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("ace_bagazo"))) + "</td>" + 
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("humedad"))) + "</td>" +  
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("aci_pellet"))) + "</td>" + 
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("aci_agua"))) + "</td>" + 
                    "<td align='right' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(SLibUtils.round(resultSet.getDouble("ace_prom_ext"), 0))) + "</td>" + 
                    "<td align='center' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DateFormatDateShortMonth.format(resultSet.getDate("fecha"))) + "</td>" + 
                    "<td align='center' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(resultSet.getDouble("eficiencia"))) + "</td>" + 
                    "<td align='center' style='width:100px'>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("monoxido"))) + "</td>" + 
                    "</tr>"
                    ;
        }
        // Parámetros meta :
        html += "<tr>" +
                "<td align='left' style='width:100px'><b>" + SLibUtils.textToHtml("Parámetros meta") + "</b></td>" +
                "<td align='right' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(dryRp.getGoalMoisitureProcessed())) + "</b></td>" +
                "<td align='right' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(dryRp.getGoalPelletProcessedPerHour())) + "</b></td>" +
                "<td align='right' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(dryRp.getGoalAverageEfficiency())) + "</b></td>" +
                "<td align='right' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(dryRp.getGoalTotalProduced())) + "</b></td>" +
                "<td align='right' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(dryRp.getGoalPelletOil())) + "</b></td>" +
                "<td align='right' style='width:100px' style='background-color: yellow'><b>" + (preview ? HTML_PREVIEW : "" ) + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(dryRp.getGoalAverageBagasseOil())) + "</b></td>" +
                "</tr>" ;
        html += "</font>" + 
                "</table>";
        
        html += "</td>" +
                "<td>" ;
        
        // aqui va la grafica de la chimenea
        
        image = smokestackChart(dryRp.getDate());
        file = new File("smokestack-chart.png");
        ImageIO.write(image, "png", file);
        imageBytes = Files.readAllBytes(Paths.get("smokestack-chart.png"));
        encoding = "data:image/png;base64," + encoder.encodeToString(imageBytes);
        html += "<img src='" + encoding + "'><br>";
        file.delete();
        
        html += "</td>" +
                "</tr>" +
                "</table>";
        html += "</h1><br><br>";
        // Fin de la segunda tabla sólo para formato
        
        image = pitCapacityChart(dryRp.getDate());
        file = new File("pit-chart.png");
        ImageIO.write(image, "png", file);
        imageBytes = Files.readAllBytes(Paths.get("pit-chart.png"));
        encoding = "data:image/png;base64," + encoder.encodeToString(imageBytes);
        html += "<img src='" + encoding + "'><br><br>";
        file.delete();
        
        image = pelletProductionChart(dryRp.getDate());
        file = new File("pellet-chart.png");
        ImageIO.write(image, "png", file);
        imageBytes = Files.readAllBytes(Paths.get("pellet-chart.png"));
        encoding = "data:image/png;base64," + encoder.encodeToString(imageBytes);
        file.delete();
        
        html += "<img src='" + encoding + "'><br>" +
                SSomMailUtils.composeMailWarning() +
                "</body>" +
                "</html>";
        
        return html;
    }
    
    private SDbDryerReport getDryerReportByPk(int[] pk) throws Exception {
        SDbDryerReport report = new SDbDryerReport();
        report.read(miClient.getSession(), pk);
        return report;
    }
    
    private void setYearsToReport() throws Exception {
        String sql = "SELECT dryer_years_to_report FROM cu_co";
        ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            YEARS_TO_REPORT = resultSet.getInt(1);
        }
    }
    
    private String getSqlDryerResume(int byTime, String year) {
        return "SELECT ADDDATE(dt, interval -1 day) fecha, " +
                "SUM(COALESCE(pellet_net_wei_r / 0.28, 0)) hum_proc, " +
                "(SUM(operative_hrs * COALESCE((pellet_net_wei_r / operative_hrs) / 0.28, 0))) / SUM(operative_hrs) prod_prom, " +
                "(SUM(COALESCE((pellet_net_wei_r / operative_hrs) / 0.28, 0) * COALESCE((pellet_net_wei_r / operative_hrs) / 0.28, 0) / goal_pellet_proc_per_hr)) / SUM(COALESCE((pellet_net_wei_r / operative_hrs) / 0.28, 0)) ef_prom, " +
                "SUM(pellet_net_wei_r) prod_tot, " +
                "(SUM(pellet_net_wei_r * COALESCE(lab_pellet_oil_n, 0))) / SUM(pellet_net_wei_r) ace_pellet, " +
                "(SUM(pellet_net_wei_r * COALESCE(lab_avg_bag_oil_n, 0))) / SUM(pellet_net_wei_r) ace_bagazo, " +
                "(SUM(pellet_net_wei_r * COALESCE(lab_moi_n, 0))) / SUM(pellet_net_wei_r) humedad, " +
                "(SUM(pellet_net_wei_r * COALESCE(lab_pellet_aci_n, 0))) / SUM(pellet_net_wei_r) aci_pellet, " +
                "(SUM(pellet_net_wei_r * COALESCE(lab_avo_aci_n, 0))) / SUM(pellet_net_wei_r) aci_agua, " +
                "SUM(pellet_to_ext) pellet_ext, " +
                "SUM(pellet_to_waste) pellet_desecho, " +
                "(SELECT dr2.pellet_stk FROM s_dryer_rep dr2 WHERE ADDDATE(dr2.dt, interval -1 day) = MAX(ADDDATE(dr.dt, interval -1 day))) inventario, " +
                "SUM(pellet_oil) aceite_pellet " +
                "FROM s_dryer_rep dr " +
                "WHERE YEAR(ADDDATE(dt, interval -1 day)) = " + year + " " +
                "GROUP BY " + (byTime == SQL_BY_MONTH ? "MONTH" : "YEAR") + "(ADDDATE(dt, interval -1 day));";        
    }
    
    private BufferedImage smokestackChart(Date date) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        
        String sql = "SELECT ADDDATE(dt, interval -1 day), " +
                "ss_effcy, " +
                "ss_c_monoxide " +
                "FROM s_dryer_rep " +
                "WHERE dt BETWEEN ADDDATE('" + SLibUtils.DbmsDateFormatDate.format(date) + "', interval -9 day) " +
                "AND '" + SLibUtils.DbmsDateFormatDate.format(date) + "';";
        ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            dataset.addValue(SLibUtils.round(resultSet.getDouble(2) * 100, 2), "% eficiencia", SLibUtils.DateFormatDateDay.format(resultSet.getDate(1)));
            dataset2.addValue(SLibUtils.round(resultSet.getDouble(3), 0), "Monóxido de carbono", SLibUtils.DateFormatDateDay.format(resultSet.getDate(1)));
        }
        
        BarRenderer renderer = new BarRenderer();
        Color barras = new Color(40, 88, 226); // Azul
        Color linea = new Color(255, 87, 51); // Naranja
        renderer.setSeriesPaint(0, barras);
        renderer.setSeriesStroke(0, new BasicStroke(2));
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());
        
        JFreeChart chart = ChartFactory.createBarChart("Chimenea secador", null, null, dataset2);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDataset(1,dataset);
        plot.mapDatasetToRangeAxis(1, 1);
        
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        final ValueAxis axis2 = new NumberAxis("%");
        plot.setRangeAxis(1, axis2);
        
        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        plot.setRenderer(0, renderer);
        plot.setRenderer(1, renderer2);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        renderer2.setSeriesPaint(0, linea);
        // OPTIONAL CUSTOMISATION COMPLETED.

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        return chart.createBufferedImage(400, 200);
    }
    
    private BufferedImage pitCapacityChart(Date date) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        
        String sql = "SELECT ADDDATE(dt, interval -1 day), " +
                "IF(b_pit_cont_emp, pit_cont_emp, IF(b_pit_cont_grin, pit_cont_grin, IF(b_pit_cont_eff, pit_cont_eff, 0))) " +
                "FROM s_dryer_rep " +
                "WHERE YEAR(ADDDATE(dt, interval -1 day)) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(date) + "') " +
                "AND MONTH(ADDDATE(dt, interval -1 day)) = MONTH('" + SLibUtils.DbmsDateFormatDate.format(date) + "');";
        ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
        
        double max = 0.0;
        String lastDate = "";
        while (resultSet.next()) {
            dataset.addValue(150000, "Nivel óptimo de arranque", SLibUtils.DateFormatDateDayMonth.format(resultSet.getDate(1)));
            dataset.addValue(230000, "Nivel deseado de uso en fosa", SLibUtils.DateFormatDateDayMonth.format(resultSet.getDate(1)));
            dataset2.addValue(SLibUtils.round(resultSet.getDouble(2), 0), "Existencia total en fosa", SLibUtils.DateFormatDateDayMonth.format(resultSet.getDate(1)));
            if (resultSet.getDouble(2) > max ){
                max = resultSet.getDouble(2);
            }
            lastDate = SLibUtils.DateFormatDateDayMonth.format(resultSet.getDate(1));
        }
        dataset.addValue(max, "", lastDate);
        
        AreaRenderer renderer = new AreaRenderer();
        renderer.setSeriesPaint(0, Color.GRAY);
        renderer.setSeriesStroke(0, new BasicStroke(2));
        
        JFreeChart chart = ChartFactory.createAreaChart("Nivel de la fosa", "Fecha", "kg", dataset2);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDataset(1,dataset);
        plot.mapDatasetToRangeAxis(1, 1);
        
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
        final ValueAxis axis2 = new NumberAxis("kg");
        plot.setRangeAxis(1, axis2);
        
        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        plot.setRenderer(0, renderer);
        plot.setRenderer(1, renderer2);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        renderer2.setSeriesPaint(0, Color.PINK);
        renderer2.setSeriesPaint(1, Color.RED);
        renderer2.setSeriesPaint(2, Color.WHITE);
        // OPTIONAL CUSTOMISATION COMPLETED.

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        return chart.createBufferedImage(1600, 400);
    }
    
    private BufferedImage pelletProductionChart(Date date) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        
        String sql = "SELECT ADDDATE(dt, interval -1 day), " +
                "COALESCE(lab_pellet_oil_n, 0), " +
                "pellet_net_wei_r " +                
                "FROM s_dryer_rep " +
                "WHERE YEAR(ADDDATE(dt, interval -1 day)) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(date) + "') " +
                "AND MONTH(ADDDATE(dt, interval -1 day)) = MONTH('" + SLibUtils.DbmsDateFormatDate.format(date) + "');";
        ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            dataset.addValue(SLibUtils.round(resultSet.getDouble(2) * 100, 2), "% cantidad de aceite", SLibUtils.DateFormatDateDayMonth.format(resultSet.getDate(1)));
            dataset2.addValue(SLibUtils.round(resultSet.getDouble(3), 0), "Producción pelletizado kg", SLibUtils.DateFormatDateDayMonth.format(resultSet.getDate(1)));
        }
        
        BarRenderer renderer = new BarRenderer();
        Color barras = new Color(40, 88, 226); // Azul
        Color linea = new Color(255, 87, 51); // Naranja
        renderer.setSeriesPaint(0, barras);
        renderer.setSeriesStroke(0, new BasicStroke(2));
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());
        
        JFreeChart chart = ChartFactory.createBarChart("Producción de pellet", "Fecha", "Kilos producidos", dataset2);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDataset(1,dataset);
        plot.mapDatasetToRangeAxis(1, 1);
        
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
        final ValueAxis axis2 = new NumberAxis("%");
        plot.setRangeAxis(1, axis2);
        
        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        plot.setRenderer(0, renderer);
        plot.setRenderer(1, renderer2);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        renderer2.setSeriesPaint(0, linea);
        // OPTIONAL CUSTOMISATION COMPLETED.

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        return chart.createBufferedImage(1600, 400);
    }
}
