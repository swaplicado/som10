/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;
import som.mod.SModSysConsts;
import som.mod.som.form.SDialogStockReportPreview;

/**
 *
 * @author Isabel Servín
 */
public class SSomStockReport {
    
    private static final int FONT_SIZE_TBL = 1;
    private static final int WEEKS_OF_TEST = 3; // CUANTAS SEMANAS ATRAS SE BUSCARAN ANÁLISIS DE LABORATORIO.
    private static final int[] SU_OIL_CL_IDS = { 2, 1 }; // SEMILLAS, AGUACATE; NOTA: el orden de los id indican el orden en el que aparecerán en el reporte.
    private static final int[] SU_OIL_TP_IDS = { 1, 2, 3/*, 4*/ }; // CRUDO, REFINADO, REPROCESO, RESIDUO; NOTA: se comenta el id residuo ya que no es necesario que aparezca en el reporte
    private static final String SU_FUNC_AREA_TP_PREVIEW = "V";
    
    private final SGuiClient miClient;
    
    public SSomStockReport(SGuiClient client) {
        miClient = client;
    }
    
    public void sendReports(int[] pk) {
        try {
            ArrayList<SDbFunctionalArea> funcAreas = getFunctionalAreas();
            SDbStockReport stkRp = getStockReport(pk);
            
            SDialogStockReportPreview preview = new SDialogStockReportPreview(miClient, generateReportHtml(stkRp, null, SU_FUNC_AREA_TP_PREVIEW, 2)); // v = preview
            preview.setVisible(true);
            
            if (preview.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                for (SDbFunctionalArea funcArea : funcAreas) {
                    String mailSubject = "[SOM] Existencias tanques " + SLibUtils.DateFormatDate.format(stkRp.getDate()) + " (" + funcArea.getName() + ")";

                    ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(funcArea.getStockReportMails().toLowerCase(), ";")));
                    //ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode("isabel.garcia@swaplicado.com.mx;sflores@swaplicado.com.mx", ";")));
                    //ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode("isabel.garcia@swaplicado.com.mx", ";")));
                    //ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode("estadisticas@aeth.mx", ";")));

                    SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "Aeth2021*s.", "som@aeth.mx");

                    SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(mailSubject), 
                            generateReportHtml(stkRp, funcArea, funcArea.getFunctionalAreaType(), FONT_SIZE_TBL), recipientsTo);

                    mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
                    mail.send();
                    System.out.println(funcArea.getName() + " mail send!");
                }
                miClient.showMsgBoxInformation("Reportes enviados!");
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }

    private String generateReportHtml(SDbStockReport stkRp, SDbFunctionalArea funcArea, String funcAreaType, int fontSizeTbl) throws Exception {
        SGuiSession session = miClient.getSession();
        Statement statement = miClient.getSession().getDatabase().getConnection().createStatement();
        Date date = subtractDate(stkRp.getDate());
        SDbWahLab lastLabWah = getLastWahLab(date);
        int[] mixWahPk = { stkRp.getFkMixingWarehouseCompanyId(), stkRp.getFkMixingWarehouseBranchId(), stkRp.getFkMixingWarehouseWarehouseId() };
        int inpCt = funcArea == null ? 0 : funcArea.getFkInputCategoryId_n();
        String sql;
        ResultSet resultSet;
        
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
                + "th {"
                + " text-align: center;"
                + " background-color: DarkSlateGray;"
                + (funcAreaType.equals(SU_FUNC_AREA_TP_PREVIEW) ? " color: black;" : " color: white;")
                + "} "
                + ".sinBorde table{border: 0; border-bottom:1px solid #000}"
                + "</style> ";
        html += "</head>" +
                "<body>" +
                "<h1>Existencias tanques al " + SLibUtils.DateFormatDate.format(stkRp.getDate()) + "</h1>";
                
        // CONSUMIBLES
        
        if (funcArea != null && funcArea.getPkFunctionalAreaId() == SModSysConsts.SU_FUNC_AREA_PRE_EXT) {
            html += "<h2>" + SLibUtils.textToHtml("Consumibles") + "</h2>";
            html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                    "<font size='" + fontSizeTbl + "'>";
            html = stkRp.getConsumableRecords().stream().map((cr) -> "<tr>" +
                    "<td align='right'>" + SLibUtils.textToHtml(cr.getDbmsConsumableWarehouse().getName() + " (Litros)") + "</th>" +
                    "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(cr.getVolume()))) + "</th>" +
                    "</tr>").reduce(html, String::concat);
            html += "</table><br>";
            html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                    "<font size='" + fontSizeTbl + "'>";
            html = stkRp.getConsumableRecords().stream().map((cr) -> "<tr>" +
                    "<td align='right'>" + SLibUtils.textToHtml(cr.getDbmsConsumableWarehouse().getCode() + " (Lleno)") + "</th>" +
                    "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(cr.getMeasure()))) + "</th>" +
                    "</tr>").reduce(html, String::concat);
            html += "</table><br>";
        }
        
        // PRODUCCIÓN
        
        if (!funcAreaType.equals(SModSysConsts.SU_FUNC_AREA_TP_LAB)) {
            html += "<h2>" + SLibUtils.textToHtml("Producción " + (funcAreaType.equals(SModSysConsts.SU_FUNC_AREA_TP_PLA) && funcArea != null ? 
                    funcArea.getName().toLowerCase() : "general")) + "</h2>";
            html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                    "<font size='" + fontSizeTbl + "'>" + 
                    "<tr>" +
                    "<th align='center' style='width:300px'><b>" + SLibUtils.textToHtml("ÍTEM") + "</b></th>" + 
                    "<th align='center'><b>" + SLibUtils.textToHtml("ÍTEM CÓDIGO") + "</b></th>" + 
                    "<th align='center'><b>" + SLibUtils.textToHtml("ALMACÉN") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("TIPO") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("LÍNEA") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("CANT. PRODUCIDA") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("UNIDAD") + "</b></th>" +
                    "</tr>";

            String sqlWhere = "";
            String sqlWherePE = "";

            if (funcAreaType.equals(SModSysConsts.SU_FUNC_AREA_TP_PLA)) {
                if (funcArea != null && funcArea.getPkFunctionalAreaId() == SModSysConsts.SU_FUNC_AREA_REF) {
                    sqlWhere = "AND (pti.fk_oil_tp_n = " + SModSysConsts.SU_OIL_TP_REF + " OR pti.fk_oil_tp_n = " + SModSysConsts.SU_OIL_TP_RES + ") ";
                }
                else {
                    sqlWhere = "AND pti.fk_oil_tp_n <> " + SModSysConsts.SU_OIL_TP_REF + " AND pti.fk_oil_tp_n <> " + SModSysConsts.SU_OIL_TP_RES + " ";
                }
                if (funcArea != null && funcArea.getPkFunctionalAreaId() == SModSysConsts.SU_FUNC_AREA_PRE_EXT) {
                    sqlWherePE = "OR pti.fk_oil_cl_n = " + SModSysConsts.SU_OIL_CL_PRE_EXT + " ";
                }
                else if (funcArea != null && funcArea.getPkFunctionalAreaId() == SModSysConsts.SU_FUNC_AREA_AVO) {
                    sqlWherePE = "AND pti.fk_oil_cl_n <> " + SModSysConsts.SU_OIL_CL_PRE_EXT + " ";
                }
            }

            sql = "SELECT " +
                    "pti.name AS item, " +
                    "pti.code AS item_codigo, " +
                    "wah.code AS almacen, " +
                    "wt.code AS tipo, " +
                    "pl.code AS linea, " +
                    "mee.mfg_fg AS cantidad_producida, " +
                    "unit.code " +
                    "FROM s_mfg_est AS me " +
                    "INNER JOIN s_mfg_est_ety AS mee ON me.id_mfg_est = mee.id_mfg_est " +
                    "INNER JOIN cu_wah AS wah ON mee.fk_wah_co = wah.id_co AND mee.fk_wah_cob = wah.id_cob AND mee.fk_wah_wah = wah.id_wah " +
                    "INNER JOIN cs_wah_tp AS wt ON wah.fk_wah_tp = wt.id_wah_tp " + 
                    "INNER JOIN cu_line AS pl ON wah.fk_line = pl.id_line " +
                    "INNER JOIN su_item AS si ON mee.fk_item_con_rm = si.id_item " +
                    "INNER JOIN su_item AS pti ON mee.fk_item_mfg_fg = pti.id_item " +
                    "INNER JOIN su_unit AS unit ON me.fk_unit = id_unit " +
                    "INNER JOIN cu_usr AS ui ON me.fk_usr_ins = ui.id_usr " +
                    "INNER JOIN cu_usr AS uu ON me.fk_usr_upd = uu.id_usr " +
                    "INNER JOIN cu_usr AS uc ON me.fk_usr_clo = uc.id_usr " +
                    "WHERE me.b_del = 0 AND me.dt_mfg_est = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                    (inpCt != SLibConsts.UNDEFINED ? "AND (si.fk_inp_ct = " + inpCt + " " + sqlWherePE + ") " : "") +
                    sqlWhere + " " +
                    "ORDER BY me.dt_mfg_est, me.id_mfg_est, mee.id_ety;";

            resultSet = statement.executeQuery(sql);
            boolean prod = false;
            while (resultSet.next()) {
                prod = true;
                html += "<tr>" + 
                        "<td style='width:300px'>" + SLibUtils.textToHtml(resultSet.getString("item")) + "</td>" +
                        "<td>" + SLibUtils.textToHtml(resultSet.getString("item_codigo")) + "</td>" +
                        "<td>" + SLibUtils.textToHtml(resultSet.getString("almacen")) + "</td>" +
                        "<td>" + SLibUtils.textToHtml(resultSet.getString("tipo")) + "</td>" +
                        "<td>" + SLibUtils.textToHtml(resultSet.getString("linea")) + "</td>" +
                        "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("cantidad_producida"))) + "</td>" +
                        "<td>" + SLibUtils.textToHtml(resultSet.getString("code")) + "</td>" +
                        "</tr>";
            }
            if (!prod) {
                html += "<tr>" +
                        "<td colspan='7'>" + SLibUtils.textToHtml("SIN PRODUCCIÓN") + "</td>" +
                        "</tr>";
            }
            html += "</table>"
                    + "<br>";
        }

        // EXISTENCIAS TANQUES
        
        double sumStkWah = 0;
        html += "<h2>" + SLibUtils.textToHtml("Existencias tanques") + "</h2>";

        int limit = funcAreaType.equals(SU_FUNC_AREA_TP_PREVIEW) || funcAreaType.equals(SModSysConsts.SU_FUNC_AREA_TP_ADM) ? 2 : 1;
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                    "<font size='" + fontSizeTbl + "'>";
        for (int i = 0; i < limit ; i++) {
            html += "<tr>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml(i == 0 ? "TANQUE" : "JUMBO") + "</b></th>" + 
                    "<th align='center' style='width:300px'><b>" + SLibUtils.textToHtml("ÍTEM") + "</b></th>" + 
                    "<th align='center'><b>" + SLibUtils.textToHtml("ORIGEN") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("OLEICO") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("LINOLEICO") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("ACIDEZ") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("EXISTENCIAS Kg") + "</b></th>" +
                    "<th colspan='2' align='center'><b>" + SLibUtils.textToHtml("CAPACIDADES Kg") + "</b></th>" +
                    "</tr>" + 
                    "<tr>" + 
                    "<td colspan='6'></td>" + 
                    "<td align='center'>TOTAL Kg</td>" + 
                    "<td align='center'>DISP Kg.</td>";
            
            String sqlWhere = "";
            String sqlWherePE = "";

            if (funcAreaType.equals(SModSysConsts.SU_FUNC_AREA_TP_PLA)) {
                if (funcArea != null && funcArea.getPkFunctionalAreaId() == SModSysConsts.SU_FUNC_AREA_REF) {
                    sqlWhere = "AND (vi.fk_oil_tp_n = " + SModSysConsts.SU_OIL_TP_REF + " OR vi.fk_oil_tp_n = " + SModSysConsts.SU_OIL_TP_RES + ") ";
                }
                else {
                    sqlWhere = "AND vi.fk_oil_tp_n <> " + SModSysConsts.SU_OIL_TP_REF + " AND vi.fk_oil_tp_n <> " + SModSysConsts.SU_OIL_TP_RES;
                }
                if (funcArea != null && funcArea.getPkFunctionalAreaId() == SModSysConsts.SU_FUNC_AREA_PRE_EXT) {
                    sqlWherePE = "OR COALESCE(v.fk_oil_cl_n, vi.fk_oil_cl_n) = " + SModSysConsts.SU_OIL_CL_PRE_EXT;
                }
                else if (funcArea != null && funcArea.getPkFunctionalAreaId() == SModSysConsts.SU_FUNC_AREA_AVO) {
                    sqlWherePE = "AND COALESCE(v.fk_oil_cl_n, vi.fk_oil_cl_n) <> " + SModSysConsts.SU_OIL_CL_PRE_EXT;
                }
            }

            sql = "SELECT " +
                    "IF(vw.b_mobile, vw.name, vw.code) AS tanque, " +
                    "vi.name AS item, " +
                    "ow.name AS origen, " +
                    "v.stock AS existencias, " +
                    "vw.cap_real_lt, " +
                    "vi.den, " +
                    "v.id_item, " +
                    "v.id_co, " +
                    "v.id_cob, " +
                    "v.id_wah " +
                    "FROM s_stk_record AS v " +
                    "INNER JOIN su_item AS vi ON v.id_item = vi.id_item " +
                    "INNER JOIN su_item AS vrm ON vi.fk_item_rm_n = vrm.id_item " + 
                    "INNER JOIN su_unit AS vu ON v.id_unit = vu.id_unit " +
                    "INNER JOIN cu_wah AS vw ON v.id_co = vw.id_co AND v.id_cob = vw.id_cob AND v.id_wah = vw.id_wah " +
                    "LEFT JOIN su_oil_own AS ow ON v.fk_oil_own_n = ow.id_oil_own " +
                    "WHERE NOT v.b_del " +
                    "AND v.dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                    (inpCt != SLibConsts.UNDEFINED ? "AND (vrm.fk_inp_ct = " + inpCt + " " + sqlWherePE + ") " : "") +
                    sqlWhere + " " +
                    "AND vw.b_mobile = " + i + " " +
                    "AND IF(vw.b_mobile, b_premises, TRUE) " +
                    "ORDER BY tanque, vi.name;";

            resultSet = statement.executeQuery(sql);
            String tanque = "";
            String capacidad = "";
            boolean imp = true;
            while (resultSet.next()) {
                if (!resultSet.getString("tanque").equals(tanque)) {
                    if (!imp) {
                        // TANQUE VACIO
                        
                        if (funcAreaType.equals(SU_FUNC_AREA_TP_PREVIEW) || 
                                funcAreaType.equals(SModSysConsts.SU_FUNC_AREA_TP_ADM)) {
                            
                            html += "<tr>" + 
                                "<td>" + SLibUtils.textToHtml(tanque) + "</td>" +
                                "<td style='width:300px'> - </td>" +
                                "<td>" + "</td>" +
                                "<td align='right'> - </td>" +
                                "<td align='right'> - </td>" +
                                "<td align='right'> - </td>" +
                                "<td align='right'> - </td>" +
                                "<td align='right'>" + SLibUtils.textToHtml(capacidad) + "</td>" +
                                "<td align='right'>" + SLibUtils.textToHtml(capacidad) + "</td>" +
                                "</tr>";
                        }
                    }
                    tanque = resultSet.getString("tanque");
                    capacidad = SLibUtils.DecimalFormatValue2D.format((resultSet.getDouble("cap_real_lt") * resultSet.getDouble("den")));
                    imp = false;
                }
                if (resultSet.getDouble("existencias") != 0) {
                    // TANQUE CON PRODUCTO
                    Statement wahStatement = miClient.getSession().getDatabase().getConnection().createStatement();
                    SDbWahLabTest test = getWahLabTestByWahAndItem(wahStatement, date, resultSet.getInt("id_item"), resultSet.getInt("id_co"), resultSet.getInt("id_cob"), resultSet.getInt("id_wah"));
                    
                    html += "<tr>" + 
                            "<td>" + SLibUtils.textToHtml(resultSet.getString("tanque")) + "</td>" +
                            "<td style='width:300px'>" + SLibUtils.textToHtml(resultSet.getString("item")) + "</td>" +
                            "<td align='right'>" + SLibUtils.textToHtml(resultSet.getString("origen") == null ? "" : resultSet.getString("origen")) + "</td>" +
                            "<td align='right'>" + SLibUtils.textToHtml(test.getOleicAcidPercentage_n() == null ? " - " : SLibUtils.DecimalFormatPercentage2D.format(test.getOleicAcidPercentage_n())) + "</td>" +
                            "<td align='right'>" + SLibUtils.textToHtml(test.getLinoleicAcidPercentage_n() == null ? " - " : SLibUtils.DecimalFormatPercentage2D.format(test.getLinoleicAcidPercentage_n())) + "</td>" +
                            "<td align='right'>" + SLibUtils.textToHtml(test.getAcidityPercentage_n()== null ? " - " : SLibUtils.DecimalFormatPercentage2D.format(test.getAcidityPercentage_n())) + "</td>" +
                            "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("existencias"))) + "</td>" +
                            "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("cap_real_lt") * resultSet.getDouble("den"))) + "</td>" +
                            "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format((resultSet.getDouble("cap_real_lt") * resultSet.getDouble("den")) - resultSet.getDouble("existencias"))) + "</td>" +
                            "</tr>";
                    imp = true;
                    sumStkWah += resultSet.getDouble("existencias");
                }
            }
        }
        html += "<tr style='background-color: Silver'>" +
                "<td colspan='5' align='right'><b>TOTAL</td>" +
                "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(sumStkWah)) + "</b></td>" +
                "<td> - </td>" +
                "<td> - </td>" +
                "</tr>";
        html += "</table>"
                + "<br>";

        // INVENTARIO DE ACEITE DE AGUACATE Y RESUMEN DE ACEITES

        if (funcAreaType.equals(SU_FUNC_AREA_TP_PREVIEW) ||
                funcAreaType.equals(SModSysConsts.SU_FUNC_AREA_TP_ADM)) {
        
            html += "<h2>" + SLibUtils.textToHtml("Inventario aguacate") + "</h2>";
            if (lastLabWah != null) {
                html += "<h3>" + SLibUtils.textToHtml("Último análisis de laboratorio capturado: del " + SLibUtils.DateFormatDate.format(lastLabWah.getDateStart()) +
                    " al " + SLibUtils.DateFormatDate.format(lastLabWah.getDateEnd())) + "</h3>";
            }
            else {
                html += "<h3>" + SLibUtils.textToHtml("No hay análisis de laboratorio capturados para las ultimas " + WEEKS_OF_TEST + " semanas") + "</h3>";
            }

            html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                    "<font size='" + fontSizeTbl + "'>" + 
                    "<tr>" +
                    "<th align='center' style='width:70px'><b>" + SLibUtils.textToHtml("TANQUE") + "</b></th>" + 
                    "<th align='center'><b>" + SLibUtils.textToHtml("CAPACIDAD TANQUE") + "</b></th>" + 
                    "<th align='center' style='width:300px'><b>" + SLibUtils.textToHtml("DESCRIPCIÓN PRODUCTO") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("INV. DISPONIBLE") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("% OCUP.") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("ESPACIO DISPONIBLE") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("ACIDEZ") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("% ACIDEZ") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("% ACIDEZ MEZCLA") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("I. PEROXIDOS") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("% HUMEDAD") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("% SÓLIDOS") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("% LINOLEICO") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("% OLEICO") + "</b></th>" +
                    "<th align='center'><b>" + SLibUtils.textToHtml("% ESTEÁRICO") + "</b></th>" +
                    "<th align='center' style='width:200px'><b>" + SLibUtils.textToHtml("COMENTARIOS") + "</b></th>" +
                    "</tr>";

            // TANQUE ACIDEZ MEZCLA
            
            sql = "SELECT " +
                    "i.id_item, " +
                    "w.id_co, " +
                    "w.id_cob, " +
                    "w.id_wah, " +
                    "w.code, " +
                    "(w.cap_real_lt * i.den) AS cap_kg, " +
                    "i.name, " +
                    "s.stock AS ext_kg, " +
                    "s.stock / (w.cap_real_lt * i.den) AS per_oc, " +
                    "(w.cap_real_lt * i.den) - s.stock AS esp_disp " +
                    "FROM s_stk_record AS s " +
                    "INNER JOIN cu_wah AS w ON s.id_co = w.id_co AND s.id_cob = w.id_cob AND s.id_wah = w.id_wah " +
                    "INNER JOIN su_item AS i ON s.id_item = i.id_item " +
                    "WHERE s.dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                    "AND s.id_co = " + mixWahPk[0] + " " +
                    "AND s.id_cob = " + mixWahPk[1] + " " +
                    "AND s.id_wah = " + mixWahPk[2] + ";";
            
            double mixAci = 0;
            String htmlMixWah = "";
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                SDbWahLabTest test = getWahLabTestByWahAndItem(session.getDatabase().getConnection().createStatement(), 
                        date, resultSet.getInt("id_item"), resultSet.getInt("id_co"), resultSet.getInt("id_cob"), resultSet.getInt("id_wah"));
                mixAci = test.getAcidityPercentage_n() != null ? test.getAcidityPercentage_n() : 0;
                
                sql = "SELECT color FROM su_wah_fill_level WHERE " + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("per_oc")) + " BETWEEN val_min AND val_max";
                ResultSet resultSetColor = session.getDatabase().getConnection().createStatement().executeQuery(sql);

                sql = "SELECT oae.name " +
                        "FROM su_oil_aci AS oa " +
                        "INNER JOIN su_oil_aci_ety AS oae ON oa.id_oil_aci = oae.id_oil_aci " +
                        "WHERE oa.dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                        "AND " + SLibUtils.DecimalFormatValue2D.format(mixAci) + " BETWEEN oae.val_min AND oae.val_max;";
                ResultSet resultSetAcidity = session.getDatabase().getConnection().createStatement().executeQuery(sql);

                htmlMixWah += "<tr>" + 
                        "<td align='center' style='width:70px'>" + SLibUtils.textToHtml(resultSet.getString("code")) + "</td>" +
                        "<td align='center'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round((resultSet.getInt("cap_kg") / 1000 ))) + " Tons") + "</td>" +
                        "<td align='left' style='width:300px'>" + SLibUtils.textToHtml(resultSet.getString("name")) + "</td>" +
                        "<td align='center'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round((resultSet.getInt("ext_kg") / 1000 ))) + " Tons") + "</td>" +
                        "<td align='center' " + (resultSetColor.next() ? " style='background-color: " + resultSetColor.getString(1).toLowerCase() + "'" : " style='background-color: Red'") + ">" + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage0D.format(resultSet.getDouble("per_oc"))) + "</td>" +
                        "<td align='center'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round((resultSet.getInt("esp_disp") / 1000 ))) + " Tons") + "</td>" +
                        "<td align='center'>" + SLibUtils.textToHtml(resultSetAcidity.next() ? resultSetAcidity.getString(1) : "") + "</td>" +
                        "<td " + (test.isAcidityPercentageOverange() ? "style='color:red'" : "") + "align='center'>" + (test.getAcidityPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getAcidityPercentage_n())) + "</td>" +
                        "<td align='center'>" + SLibUtils.textToHtml("N/A") + "</td>";

                String notes = "";
                if (lastLabWah != null) {
                    notes = (test.getPkWarehouseLaboratoryId() != lastLabWah.getPkWarehouseLaboratoryId() ? test.getDate() != null ? "Fecha del último análisis " + SLibUtils.DateFormatDate.format(test.getDate()) + ". " : "No hay resultados de laboratorio. " : "");
                    notes += " (" + SLibUtils.DecimalFormatPercentage2D.format(stkRp.getMixingPercentage()) + ")";
                }
                htmlMixWah += "<td " + (test.isPeroxideIndexOverange() ? "style='color:red'" : "") + "align='center'>" + (test.getPeroxideIndex_n() == null ? "" : SLibUtils.DecimalFormatValue2D.format(test.getPeroxideIndex_n())) + "</td>" +
                        "<td " + (test.isMoisturePercentageOverange() ? "style='color:red'" : "") + "align='center'>" + (test.getMoisturePercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getMoisturePercentage_n())) + "</td>" +
                        "<td " + (test.isSolidPersentageOverange() ? "style='color:red'" : "") + "align='center'>" + (test.getSolidPersentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getSolidPersentage_n())) + "</td>" +
                        "<td " + (test.isLinoleicAcidPercentageOverange()? "style='color:red'" : "") + "align='center'>" + (test.getLinoleicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getLinoleicAcidPercentage_n())) + "</td>" +
                        "<td " + (test.isOleicAcidPercentageOverange()? "style='color:red'" : "") + "align='center'>" + (test.getOleicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getOleicAcidPercentage_n())) + "</td>" +
                        "<td " + (test.isStearicAcidPercentageOverange()? "style='color:red'" : "") + "align='center'>" + (test.getStearicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getStearicAcidPercentage_n())) + "</td>" +
                        "<td align='center' style='width:200px'>" + SLibUtils.textToHtml(notes) + "</td>" +
                        "</tr>";
            }
            
            // RESTO DE TANQUES
            
            sql = "SELECT " +
                    "i.id_item, " +
                    "w.id_co, " +
                    "w.id_cob, " +
                    "w.id_wah, " +
                    "IF(b_mobile, w.name, w.code) AS code, " +
                    "w.cap_real_lt * i.den AS cap_kg, " +
                    "i.name, " +
                    "i.id_item, " +
                    "s.stock AS ext_kg, " +
                    "s.stock / (w.cap_real_lt * i.den) AS per_oc, " +
                    "(w.cap_real_lt * i.den) - s.stock AS esp_disp " +
                    "FROM s_stk_record as s " +
                    "INNER JOIN cu_wah AS w ON s.id_co = w.id_co AND s.id_cob = w.id_cob AND s.id_wah = w.id_wah " +
                    "INNER JOIN su_item AS i ON s.id_item = i.id_item " +
                    "WHERE s.dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                    "AND i.fk_item_rm_n = " + SModSysConsts.SU_ITEM_RM_AVO + " " +
                    "AND s.stock <> 0 " +
                    "AND NOT s.b_del AND NOT w.b_del " +
                    "ORDER BY w.b_mobile, code, i.name;";

            double totLab = 0;
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDbWahLabTest test = getWahLabTestByWahAndItem(session.getDatabase().getConnection().createStatement(), 
                        date, resultSet.getInt("id_item"), resultSet.getInt("id_co"), resultSet.getInt("id_cob"), resultSet.getInt("id_wah"));
                
                sql = "SELECT color FROM su_wah_fill_level WHERE " + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("per_oc")) + " BETWEEN val_min AND val_max";
                ResultSet resultSetColor = session.getDatabase().getConnection().createStatement().executeQuery(sql);

                sql = "SELECT oae.name " +
                        "FROM su_oil_aci AS oa " +
                        "INNER JOIN su_oil_aci_ety AS oae ON oa.id_oil_aci = oae.id_oil_aci " +
                        "WHERE oa.dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                        "AND " + SLibUtils.DecimalFormatValue2D.format(test.getAcidityPercentage_n() == null ? 0 : test.getAcidityPercentage_n()) + " BETWEEN oae.val_min AND oae.val_max;";
                ResultSet resultSetAcidity = session.getDatabase().getConnection().createStatement().executeQuery(sql);

                html += "<tr>" + 
                        "<td align='center' style='width:70px'>" + SLibUtils.textToHtml(resultSet.getString("code")) + "</td>" +
                        "<td align='center'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round((resultSet.getInt("cap_kg") / 1000 ))) + " Tons") + "</td>" +
                        "<td align='left' style='width:300px'>" + SLibUtils.textToHtml(resultSet.getString("name")) + "</td>" +
                        "<td align='center'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round((resultSet.getInt("ext_kg") / 1000 ))) + " Tons") + "</td>" +
                        "<td align='center' " + (resultSetColor.next() ? " style='background-color: " + resultSetColor.getString(1).toLowerCase() + "'" : " style='background-color: Red'") + ">" + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage0D.format(resultSet.getDouble("per_oc"))) + "</td>" +
                        "<td align='center'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round((resultSet.getInt("esp_disp") / 1000 ))) + " Tons") + "</td>" +
                        "<td align='center'>" + SLibUtils.textToHtml(resultSetAcidity.next() ? resultSetAcidity.getString(1) : "") + "</td>" +
                        "<td " + (test.isAcidityPercentageOverange() ? "style='color:red'" : "") + "align='center'>" + (test.getAcidityPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getAcidityPercentage_n())) + "</td>" ;

                if (!SLibUtils.compareKeys(mixWahPk, new int[] { resultSet.getInt("id_co"), resultSet.getInt("id_cob"), resultSet.getInt("id_wah") })
                        && resultSet.getInt("id_item") == SModSysConsts.SU_ITEM_OIL_AVO) {
                    html += "<td align='center'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(
                            (((resultSet.getDouble("esp_disp") * 0.85) * (test.getAcidityPercentage_n() == null ? 0 : test.getAcidityPercentage_n())) + 
                                    ((resultSet.getDouble("esp_disp") * stkRp.getMixingPercentage()) * mixAci)) / resultSet.getDouble("esp_disp"))) + "</td>";
                }
                else {
                    html += "<td align='center'>" + SLibUtils.textToHtml("N/A") + "</td>";
                }

                String notes = "";
                if (lastLabWah != null) {
                    notes = test.getNote().isEmpty() ? "" : test.getNote() + ". ";
                    notes += test.getPkWarehouseLaboratoryId() != lastLabWah.getPkWarehouseLaboratoryId() ? test.getDate() != null ? "Fecha del último análisis " + SLibUtils.DateFormatDate.format(test.getDate()) + "." : "No hay resultados de laboratorio." : "";
                }
                html += "<td " + (test.isPeroxideIndexOverange() ? "style='color:red'" : "") + "align='center'>" + (test.getPeroxideIndex_n() == null ? "" : SLibUtils.DecimalFormatValue2D.format(test.getPeroxideIndex_n())) + "</td>" +
                        "<td " + (test.isMoisturePercentageOverange() ? "style='color:red'" : "") + "align='center'>" + (test.getMoisturePercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getMoisturePercentage_n())) + "</td>" +
                        "<td " + (test.isSolidPersentageOverange() ? "style='color:red'" : "") + "align='center'>" + (test.getSolidPersentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getSolidPersentage_n())) + "</td>" +
                        "<td " + (test.isLinoleicAcidPercentageOverange()? "style='color:red'" : "") + "align='center'>" + (test.getLinoleicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getLinoleicAcidPercentage_n())) + "</td>" +
                        "<td " + (test.isOleicAcidPercentageOverange()? "style='color:red'" : "") + "align='center'>" + (test.getOleicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getOleicAcidPercentage_n())) + "</td>" +
                        "<td " + (test.isStearicAcidPercentageOverange()? "style='color:red'" : "") + "align='center'>" + (test.getStearicAcidPercentage_n() == null ? "" : SLibUtils.DecimalFormatPercentage2D.format(test.getStearicAcidPercentage_n())) + "</td>" +
                        "<td align='center' style='width:200px'>" + SLibUtils.textToHtml(notes) + "</td>" +
                        "</tr>";
                totLab += resultSet.getDouble("ext_kg");
            }
            html += "<tr style='background-color: Silver'><b>" +
                    "<td colspan='3' align='right'>TOTAL</td>" +
                    "<td align='center'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round((totLab / 1000 ))) + " Tons") + "</td>" +
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
                    "<td></td>" +
                    "</b></tr>";
            html += htmlMixWah;
            html += "</table>"
                    + "<br>";

            // RESUMEN AGUACATE

            html += "<table class='sinBorde'>" +
                    "<tr valign='top'>" +
                    "<td>";

            html += "<h3>Resumen aguacate</h3>";

            double stkAvoOils = 0;

            for (int oilTp : SU_OIL_TP_IDS) {
                html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                        "<font size='" + fontSizeTbl + "'>" + 
                        "<tr>" +
                        "<th style='width:300px' align='right'><b>" + SLibUtils.textToHtml("FAMILIA") + "</b></th>" + 
                        "<th align='center'><b>" + SLibUtils.textToHtml("INVENTARIO") + "</b></th>" + 
                        "<th align='left'><b>" + SLibUtils.textToHtml("ACIDEZ PONDERADA") + "</b></th>" +
                        "</tr>";
                double avoTp = 0;
                ArrayList<SAvocadoOil> maAvocadoOils = new ArrayList();
                sql = "SELECT " +
                        "i.id_item, " +
                        "sr.id_co, " +
                        "sr.id_cob, " +
                        "sr.id_wah, " +
                        "i.name, " +
                        "sr.stock, " +
                        "COALESCE(sr.fk_oil_cl_n, i.fk_oil_cl_n) AS oil_cl, " +
                        "COALESCE(sr.fk_oil_tp_n, i.fk_oil_tp_n) AS oil_tp, " +
                        "i.fk_oil_grp_family_n AS oil_fam " +
                        "FROM s_stk_record AS sr " +
                        "INNER JOIN su_item AS i ON sr.id_item = i.id_item " +
                        "WHERE sr.dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                        "AND COALESCE(sr.fk_oil_cl_n, i.fk_oil_cl_n) = " + SModSysConsts.SU_OIL_CL_AVO + " " + // AGUACATERA
                        "AND COALESCE(sr.fk_oil_tp_n, i.fk_oil_tp_n) = " + oilTp + " " +
                        "AND i.fk_oil_grp_family_n = " + SModSysConsts.SU_OIL_GRP_FAM_AVO + " " +
                        "AND NOT sr.b_del " +
                        "ORDER BY i.id_item, oil_cl, oil_tp, oil_fam;";

                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDbWahLabTest test = getWahLabTestByWahAndItem(session.getDatabase().getConnection().createStatement(), 
                        date, resultSet.getInt("id_item"), resultSet.getInt("id_co"), resultSet.getInt("id_cob"), resultSet.getInt("id_wah"));
                    boolean found = false;
                    String itemName = "";
                    
                    if (oilTp == SU_OIL_TP_IDS[0] ) {
                        if (test.getAcidityPercentage_n() != null && test.getAcidityPercentage_n() != 0.0) {
                            Statement statementOilAci = miClient.getSession().getDatabase().getConnection().createStatement();
                            sql = "SELECT " +
                                    "e.name, " +
                                    "e.val_min, " +
                                    "e.val_max " +
                                    "FROM su_oil_aci_ety AS e " +
                                    "WHERE e.id_oil_aci = (SELECT o.id_oil_aci FROM su_oil_aci AS o " +
                                    "WHERE o.dt_start = (SELECT MAX(o2.dt_start) FROM su_oil_aci AS o2));";
                            ResultSet resultSetOilAci = statementOilAci.executeQuery(sql);
                            boolean foundAci = false;
                            while (resultSetOilAci.next() && !foundAci) {
                                if (SLibUtils.parseDouble(SLibUtils.DecimalFormatValue2D.format(test.getAcidityPercentage_n())) >= resultSetOilAci.getDouble("val_min")
                                        && SLibUtils.parseDouble(SLibUtils.DecimalFormatValue2D.format(test.getAcidityPercentage_n())) < resultSetOilAci.getDouble("val_max")) {
                                    itemName = "ACEITE CRUDO " + resultSetOilAci.getString("name");
                                    foundAci = true;
                                }
                            }
                        }
                    }
                    else {
                        itemName = resultSet.getString("name");
                    }
                    for (SAvocadoOil oil : maAvocadoOils) {
                        if (oil.name.equals(itemName)) {
                            oil.stock += resultSet.getDouble("stock");
                            oil.stkAcidity += test.getAcidityPercentage_n() == null ? 0 : test.getAcidityPercentage_n() * resultSet.getDouble("stock");
                            avoTp += resultSet.getDouble("stock");
                            found = true;
                            break;
                        }
                    }
                    if (!found && !itemName.isEmpty()) {
                        SAvocadoOil oil = new SAvocadoOil();
                        oil.name = itemName;
                        oil.stock = resultSet.getDouble("stock");
                        oil.stkAcidity = test.getAcidityPercentage_n() == null ? 0 : test.getAcidityPercentage_n() * resultSet.getDouble("stock");
                        avoTp += resultSet.getDouble("stock");
                        maAvocadoOils.add(oil);
                    }
                }

                html = maAvocadoOils.stream().map((oil) -> "<tr>" + 
                        "<td align='right' style='width:300px'>" + SLibUtils.textToHtml(oil.name) + "</td>" +
                        "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(oil.stock / 1000)) + " Tons") + "</td>" +
                        "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatPercentage2D.format(oil.stkAcidity / oil.stock)) + "</td>" +
                        "</tr>").reduce(html, String::concat);
                html += "<tr style='background-color: blue'><b>" + 
                        "<td align='right' style='width:300px'>" + SLibUtils.textToHtml("AGUACATE " + SModSysConsts.SU_OIL_TP_DESC.get(oilTp).toUpperCase()) + "</td>" + 
                        "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(avoTp / 1000))  + " Tons") + "</td>" +
                        "<td align='right'>" + SLibUtils.textToHtml("") + "</td>" +
                        "</b></tr>";
                html += "</table>"
                        + "<br>";
                stkAvoOils += avoTp;
            }
            html += "<table>" +
                    "<font size='" + fontSizeTbl + "'>" + 
                        "<tr style='background-color: yellow'><b>" + 
                        "<td align='right' style='width:300px'>" + SLibUtils.textToHtml("TOTAL AGUACATE") + "</td>" + 
                        "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(stkAvoOils / 1000))  + " Tons") + "</b></td>" +
                        "</tr>";
            html += "</table>";
            html += "</td>" +
                    "<td>";

            // RESUMEN RESTO DE ACEITES

            html += "<h3>Resumen resto de aceites</h3>";

            html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                    "<font size='" + fontSizeTbl + "'>" + 
                    "<tr>" +
                    "<th style='width:300px' align='right'><b>" + SLibUtils.textToHtml("ACEITE") + "</b></th>" + 
                    "<th align='center'><b>" + SLibUtils.textToHtml("INVENTARIO") + "</b></th>" + 
                    "</tr>";

            sql = "SELECT " +
                    "i.name, " +
                    "SUM(sr.stock) AS stock, " +
                    "COALESCE(sr.fk_oil_cl_n, i.fk_oil_cl_n) AS oil_cl, " +
                    "COALESCE(sr.fk_oil_tp_n, i.fk_oil_tp_n) AS oil_tp, " +
                    "i.fk_oil_grp_family_n AS oil_fam " +
                    "FROM s_stk_record AS sr " +
                    "INNER JOIN su_item AS i ON sr.id_item = i.id_item " +
                    "WHERE dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                    "AND (COALESCE(sr.fk_oil_cl_n, i.fk_oil_cl_n) = " + SModSysConsts.SU_OIL_CL_PRE_EXT + " " + // PRENSAS Y EXTRACCIÓN
                    "OR (COALESCE(sr.fk_oil_cl_n, i.fk_oil_cl_n) = " + SModSysConsts.SU_OIL_CL_AVO + " " + // AGUACATERA
                    "AND i.fk_oil_grp_family_n = " + SModSysConsts.SU_OIL_GRP_FAM_OTHER_AVO + ")) " + // OTROS ACEITES DE AGUACATE
                    "AND COALESCE(sr.fk_oil_tp_n, i.fk_oil_tp_n) <> " + SModSysConsts.SU_OIL_TP_REF + " " +
                    "AND COALESCE(sr.fk_oil_tp_n, i.fk_oil_tp_n) <> " + SModSysConsts.SU_OIL_TP_RES + " " +
                    "AND NOT sr.b_del " +
                    "GROUP BY sr.id_item, oil_cl, oil_tp " +
                    "ORDER BY oil_fam, oil_cl, oil_tp, i.name;";

            double stkOtherOils = 0; 
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                html += "<tr>" + 
                        "<td align='right' style='width:300px'>" + SLibUtils.textToHtml(resultSet.getString("name")) + "</td>" +
                        "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(resultSet.getDouble("stock") / 1000)) + " Tons") + "</td>" +
                        "</tr>";
                stkOtherOils += resultSet.getDouble("stock");
            }
            html += "<tr style='background-color: yellow'><b>" + 
                    "<td align='right' style='width:300px'>" + SLibUtils.textToHtml("TOTAL RESTO DE ACEITES") + "</td>" +
                    "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(stkOtherOils / 1000)) + " Tons") + "</b></td>" +
                    "</tr>";
            html += "</table>";
            html += "</td>" +
                    "<td>";

            // DETALLE REFINADOS

            html += "<h3>Detalle refinados</h3>";

            double stkRefOils = 0;
            double stkAvoRefOils = 0;
            for (int oilCl : SU_OIL_CL_IDS) {
                double stkRefCl = 0;
                html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                    "<font size='" + fontSizeTbl + "'>" + 
                    "<tr>" +
                    "<th style='width:300px' align='right'><b>" + SLibUtils.textToHtml("ACEITE " + (oilCl == SModSysConsts.SU_OIL_CL_AVO ? "AGUACATE" : "OTROS") ) + "</b></th>" + 
                    "<th align='center'><b>" + SLibUtils.textToHtml("INVENTARIO") + "</b></th>" + 
                    "</tr>";
                
                String sqlWhere = oilCl == SModSysConsts.SU_OIL_CL_PRE_EXT ? "OR (COALESCE(sr.fk_oil_tp_n, i.fk_oil_tp_n) = " + SModSysConsts.SU_OIL_TP_REF + " AND i.fk_oil_grp_family_n = " + SModSysConsts.SU_OIL_GRP_FAM_OTHER_AVO + " AND sr.dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "')" : 
                        "AND i.fk_oil_grp_family_n = " + SModSysConsts.SU_OIL_GRP_FAM_AVO + " AND sr.dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "'";
                
                sql = "SELECT " +
                        "i.name, " +
                        "SUM(sr.stock) AS stock, " +
                        "COALESCE(sr.fk_oil_cl_n, i.fk_oil_cl_n) AS oil_cl, " +
                        "COALESCE(sr.fk_oil_tp_n, i.fk_oil_tp_n) AS oil_tp, " +
                        "i.fk_oil_grp_family_n AS oil_fam " +
                        "FROM s_stk_record AS sr " +
                        "INNER JOIN su_item AS i ON sr.id_item = i.id_item " +
                        "WHERE sr.dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                        "AND COALESCE(sr.fk_oil_cl_n, i.fk_oil_cl_n) = " + oilCl + " " +
                        "AND COALESCE(sr.fk_oil_tp_n, i.fk_oil_tp_n) = " + SModSysConsts.SU_OIL_TP_REF + " " +
                        sqlWhere + " " +
                        "AND NOT sr.b_del " +
                        "GROUP BY sr.id_item, oil_cl, oil_tp " +
                        "ORDER BY oil_fam, oil_cl, oil_tp, i.name;";

                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    html += "<tr>" + 
                            "<td align='right' style='width:300px'>" + SLibUtils.textToHtml(resultSet.getString("name")) + "</td>" +
                            "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(resultSet.getDouble("stock") / 1000)) + " Tons") + "</td>" +
                            "</tr>";
                    stkRefCl += resultSet.getDouble("stock");
                    if (oilCl == SModSysConsts.SU_OIL_CL_AVO) {
                        stkAvoRefOils += resultSet.getDouble("stock");
                    }
                }
                html += "<tr style='background-color: blue'><b>" + 
                            "<td align='right' style='width:300px'>" + SLibUtils.textToHtml("TOTAL") + "</td>" +
                            "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(stkRefCl / 1000)) + " Tons") + "</b></td>" +
                            "</tr>" +
                            "</table>" +
                            "<br>";
                stkRefOils += stkRefCl;
            }
            html += "<table>" +
                    "<font size='" + fontSizeTbl + "'>" + 
                        "<tr style='background-color: yellow'><b>" + 
                        "<td align='right' style='width:300px'>" + SLibUtils.textToHtml("TOTAL REFINADOS") + "</td>" + 
                        "<td align='right' style='width:70px'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round(stkRefOils / 1000))  + " Tons") + "</b></td>" +
                        "</tr>";
            html += "</table>";
            html += "</td>";

            html += "</table>";
            html += "</td>" + 
                    "</tr>" +
                    "<tr>" +
                    "<td colspan='3'><b>TOTAL ACEITES*: " + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue0D.format(Math.round((stkAvoOils + stkOtherOils + stkRefOils - stkAvoRefOils) / 1000)) + " Tons") + "</b></td>" +
                    "<td colspan='3'>" + SLibUtils.textToHtml("*Total aguacate + total restos aceites + total aceite otros") + "</td>" +
                    "</tr>" +
                    "</table>";
        }
        html += "<br>" +
                SSomMailUtils.composeMailWarning() +
                "</body>" +
                "</html>";
        
        return html;
    }
        
    private Date subtractDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        return calendar.getTime();
    }
    
    private ArrayList<SDbFunctionalArea> getFunctionalAreas() throws Exception {
        ArrayList<SDbFunctionalArea> func = new ArrayList<>();
        String sql = "SELECT id_func_area FROM su_func_area WHERE NOT b_del;";
        ResultSet resultSet = miClient.getSession().getDatabase().getConnection().createStatement().executeQuery(sql);
        while (resultSet.next()) {
            SDbFunctionalArea area = new SDbFunctionalArea();
            area.read(miClient.getSession(), new int[] { resultSet.getInt(1) });
            func.add(area);
        }
        return func;
    }
    
    private SDbStockReport getStockReport(int[] pk) throws Exception {
        SDbStockReport report = new SDbStockReport();
        report.read(miClient.getSession(), pk);
        return report;
    }

    private SDbWahLab getLastWahLab(Date date) throws Exception {
        SDbWahLab test = null;
        String sql = "SELECT wl.id_wah_lab FROM s_wah_lab AS wl " +
                "WHERE wl.ts_usr_ins = ( " +
                "SELECT MAX(wl2.ts_usr_ins) " +
                "FROM s_wah_lab AS wl2 " +
                "WHERE wl2.dt_start >= ( " +
                "SELECT ADDDATE('" + SLibUtils.DbmsDateFormatDate.format(date) + "', INTERVAL -" + WEEKS_OF_TEST + " WEEK)) " +
                "AND NOT wl2.b_del);";
        ResultSet resultSet = miClient.getSession().getDatabase().getConnection().createStatement().executeQuery(sql);
        if (resultSet.next()) {
            test = new SDbWahLab();
            test.read(miClient.getSession(), new int[] { resultSet.getInt(1) } );
        }
        return test;
    }
    
    private SDbWahLabTest getWahLabTestByWahAndItem(Statement statement, Date date, int itemPk, int co, int cob, int wah) throws Exception {
        SDbItem item = new SDbItem();
        SDbWahLabTest test = new SDbWahLabTest();
        
        item.read(miClient.getSession(), new int[] { itemPk });
        
        /* OBTIENE EL PK DEL ANÁLISIS DEL LABORATORIO MAS RECIENTE PARA UN DETERMINADO TANQUE E ÍTEM SIMILAR QUE NO EXEDA DE LAS SEMANAS DEFINIDAS
        * EN CASO DE QUE HAYA 2 FECHAS REPETIDAS SE TOMA EL QUE HAYA SIDO CREADO MAS RECIENTE
        */
        String sql = "SELECT wlt.id_wah_lab, wlt.id_test FROM s_wah_lab AS wl " +
                "INNER JOIN s_wah_lab_test AS wlt ON wl.id_wah_lab = wlt.id_wah_lab " +
                "INNER JOIN su_item AS i ON wlt.fk_item = i.id_item " +
                "WHERE wl.ts_usr_ins = ( " +
                "SELECT MAX(a.ts_usr_ins) FROM ( " +
                "SELECT " +
                "wl.id_wah_lab, wl.dt_start, wl.dt_end, wl.ts_usr_ins " +
                "FROM s_wah_lab AS wl " +
                "INNER JOIN s_wah_lab_test AS wlt ON wlt.id_wah_lab = wl.id_wah_lab " +
                "INNER JOIN su_item AS i ON wlt.fk_item = i.id_item " +
                "WHERE wlt.fk_wah_co = " + co + " AND wlt.fk_wah_cob = " + cob + " AND wlt.fk_wah_wah = " + wah + " " +
                "AND wl.dt_start >= (SELECT ADDDATE('" + SLibUtils.DbmsDateFormatDate.format(date) + "', INTERVAL -" + WEEKS_OF_TEST + " WEEK)) " +
                "AND NOT wl.b_del " +
                "AND i.fk_oil_cl_n = " + item.getFkOilClassId_n() + " " +
                "AND i.fk_oil_tp_n = " + item.getFkOilTypeId_n() + " " +
                "AND i.fk_item_rm_n = " + item.getFkItemRowMaterialId_n() + ") AS a) " +
                "AND wlt.fk_wah_co = " + co + " AND wlt.fk_wah_cob = " + cob + " AND wlt.fk_wah_wah = " + wah + " " +
                "AND i.fk_oil_cl_n = " + item.getFkOilClassId_n() + " " +
                "AND i.fk_oil_tp_n = " + item.getFkOilTypeId_n() + " " +
                "AND i.fk_item_rm_n = " + item.getFkItemRowMaterialId_n() + ";";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            test.read(miClient.getSession(), new int[] { resultSet.getInt(1), resultSet.getInt(2) });
        }
        
        return test;
    }
}

class SAvocadoOil {
    
    public String name;
    public double stock;
    public double stkAcidity;
    
}
