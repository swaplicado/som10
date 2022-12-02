/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import erp.lib.SLibUtilities;
import java.util.ArrayList;
import java.util.Arrays;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;

/**
 *
 * @author Isabel Servín
 */
public class SSomWahLabTestReport {
    
    private static final int FONT_SIZE_TBL = 2;
    
    private final SGuiClient miClient;
    
    public SSomWahLabTestReport(SGuiClient client) {
        miClient = client;
    }
    
    public void sendReport(String mailTo, int[] pk) {
        try {
            SDbWahLab lab = getWahLab(pk);
            if (lab.isValidation()) {
                String mailBody = generateReportHtml(lab);

                String mailSubject = "[SOM] Registro de resultados de tanques de proceso";

                ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailTo, ";")));

                SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "Aeth2021*s.", "som@aeth.mx");

                SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(mailSubject), mailBody, recipientsTo);

                mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
                mail.send();

                miClient.showMsgBoxInformation("Reporte enviado!");
                System.out.println("Mail send!");
            }
            else {
                miClient.showMsgBoxInformation("El registro seleccionado no esta validado.");
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }

    private String generateReportHtml(SDbWahLab lab) throws Exception {
        String html = "<html>" +
                "<head>" ;
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
                + " font-size: 0.875em;"
                + " font-family: sans-serif;"
                + "} "
                + "table, th, td {"
                + " border: 1px solid black;"
                + " border-collapse: collapse;"
                + "padding-left: 5px; padding-right: 5px;"
                + "} "
                + "th {"
                + " text-align: center;"
                + " background-color: DarkSlateGray;"
                + " color: white;"
                + "} "
                + "</style>\n";
        html += "</head>" +
                "<body>" +
                "<h1>REGISTRO DE RESULTADOS DE TANQUES DE PROCESO</h1>" +
                "<h2>Semana del " + SLibTimeUtils.dateFormatDatePeriodLong(lab.getDateStart(), lab.getDateEnd()) + "</h2>" +
                "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<th align='center'><b>" + SLibUtils.textToHtml("Fecha análisis") + "</b></th>" + 
                "<th align='center'><b>" + SLibUtils.textToHtml("Tanque") + "</b></th>" + 
                "<th align='center'><b>" + SLibUtils.textToHtml("Producto") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("ACIDEZ") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("I.PERÓXIDOS") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("HUMEDAD") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("SÓLIDOS") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("LINOLÉICO") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("OLÉICO") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("LINOLENICO") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("ESTEÁRICO") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("PALMÍTICO") + "</b></th>" +
                "<th width='50' align='center'><b>" + SLibUtils.textToHtml("OBSERVACIONES") + "</b></th>" +
                "</tr>";
        
        for (SDbWahLabTest test : lab.getWahLabTests()) {
            html += "<tr>" + 
                    "<td align='center' rowspan='2'>" + SLibUtils.DateFormatDate.format(test.getDate()) + "</td>" +
                    "<td align='center' rowspan='2'><b>" + SLibUtils.textToHtml(test.getDbmsBranchWarehouse().getCode()) + "</b></td>" +
                    "<td align='center'>" + SLibUtils.textToHtml(test.getDbmsItem().getName()) + "</td>" +
                    "<td " + (test.isAcidityPercentageOverange() ? "style='color:red'" : "") + "align='right'>" + (test.getAcidityPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getAcidityPercentage_n())) + "</td>" +
                    "<td " + (test.isPeroxideIndexOverange()? "style='color:red'" : "") + "align='right'>" + (test.getPeroxideIndex_n() == null ? "" : SLibUtils.textToHtml(test.getPeroxideIndex_n() + "")) + "</td>" +
                    "<td " + (test.isMoisturePercentageOverange()? "style='color:red'" : "") + "align='right'>" + (test.getMoisturePercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getMoisturePercentage_n())) + "</td>" +
                    "<td " + (test.isSolidPersentageOverange() ? "style='color:red'" : "") + "align='right'>" + (test.getSolidPersentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getSolidPersentage_n())) + "</td>" +
                    "<td " + (test.isLinoleicAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (test.getLinoleicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getLinoleicAcidPercentage_n())) + "</td>" +
                    "<td " + (test.isOleicAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (test.getOleicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getOleicAcidPercentage_n())) + "</td>" +
                    "<td " + (test.isLinolenicAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (test.getLinolenicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getLinolenicAcidPercentage_n())) + "</td>" +
                    "<td " + (test.isStearicAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (test.getStearicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getStearicAcidPercentage_n())) + "</td>" +
                    "<td " + (test.isPalmiticAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (test.getPalmiticAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getPalmiticAcidPercentage_n())) + "</td>" +
                    "<td align='left'>" + SLibUtils.textToHtml(test.getNote()) + "</td>" +
                    "</tr>";
                    if (test.getLastWahLabTest() != null) {
                        SDbWahLabTest lastTest = test.getLastWahLabTest();
                        html += "<tr>" + 
                                "<td style='font-size:80%' align='center'>" + SLibUtils.textToHtml("AGL Y PEROXIDO ANTERIOR") + "</td>" +
                                "<td " + (lastTest.isAcidityPercentageOverange() ? "style='color:red'" : "") + "align='right'>" + (lastTest.getAcidityPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(lastTest.getAcidityPercentage_n())) + "</td>" +
                                "<td " + (lastTest.isPeroxideIndexOverange()? "style='color:red'" : "") + "align='right'>" + (lastTest.getPeroxideIndex_n() == null ? "" : SLibUtils.textToHtml(lastTest.getPeroxideIndex_n() + "")) + "</td>" +
                                "<td " + (lastTest.isMoisturePercentageOverange()? "style='color:red'" : "") + "align='right'>" + (lastTest.getMoisturePercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(lastTest.getMoisturePercentage_n())) + "</td>" +
                                "<td " + (lastTest.isSolidPersentageOverange() ? "style='color:red'" : "") + "align='right'>" + (lastTest.getSolidPersentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(lastTest.getSolidPersentage_n())) + "</td>" +
                                "<td " + (lastTest.isLinoleicAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (lastTest.getLinoleicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(lastTest.getLinoleicAcidPercentage_n())) + "</td>" +
                                "<td " + (lastTest.isOleicAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (lastTest.getOleicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(lastTest.getOleicAcidPercentage_n())) + "</td>" +
                                "<td " + (lastTest.isLinolenicAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (lastTest.getLinolenicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(lastTest.getLinolenicAcidPercentage_n())) + "</td>" +
                                "<td " + (lastTest.isStearicAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (lastTest.getStearicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(lastTest.getStearicAcidPercentage_n())) + "</td>" +
                                "<td " + (lastTest.isPalmiticAcidPercentageOverange()? "style='color:red'" : "") + "align='right'>" + (lastTest.getPalmiticAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(lastTest.getPalmiticAcidPercentage_n())) + "</td>" +
                                        "<td></td>" +
                                "</tr>";
                    }
                    else {
                        html += "<tr>" + 
                                "<td style='font-size:80%' align='center'>" + SLibUtils.textToHtml("AGL Y PEROXIDO ANTERIOR") + "</td>" +
                                "<td></td>" +
                                "<td></td>" +
                                "<td></td>" +
                                "<td></td>" +
                                "<td></td>" +
                                "<td></td>" +
                                "<td></td>" +
                                "<td></td>" +
                                "<td></td>" +
                                "<td></td>" +
                                "</tr>";
                    }
        }
        html += "</table>" + 
                "<br>" +
                SSomMailUtils.composeMailWarning() +
                "</body>" +
                "</html>";
        return html;
    }
    
    private SDbWahLab getWahLab(int[] pk) throws Exception {
        SDbWahLab lab = new SDbWahLab();
        lab.read(miClient.getSession(), pk);
        if (lab.getLastWahLab() != null) {
            for (SDbWahLabTest test : lab.getWahLabTests()) {
                test.readLastWahLabTest(miClient.getSession(), lab.getLastWahLab().getPkWarehouseLaboratoryId());
            }
        }
        return lab;
    }
}
