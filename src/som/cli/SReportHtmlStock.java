/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.sql.ResultSet;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SReportHtmlStock {

    private static final int FONT_SIZE_TBL = 2;
    
    public String generateReportHtml(SGuiSession session, Date date) throws Exception {
        // PRODUCCIÓN
        
        String html = "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<th style='width:200px' align='center'><b>" + SLibUtils.textToHtml("ÍTEM") + "</b></th>" + 
                "<th align='center'><b>" + SLibUtils.textToHtml("ÍTEM CÓDIGO") + "</b></th>" + 
                "<th align='center'><b>" + SLibUtils.textToHtml("ALMACEN") + "</b></th>" +
                "<th align='center'><b>" + SLibUtils.textToHtml("TIPO") + "</b></th>" +
                "<th align='center'><b>" + SLibUtils.textToHtml("LÍNEA") + "</b></th>" +
                "<th align='center'><b>" + SLibUtils.textToHtml("CANTIDAD PRODUCIDA") + "</b></th>" +
                "<th align='center'><b>" + SLibUtils.textToHtml("UNIDAD") + "</b></th>" +
                "</tr>";
        
        String sql = "SELECT " +
                "pti.name AS item, " +
                "pti.code AS item_codigo, " +
                "wah.code AS almacen, " +
                "wah.name AS tipo, " +
                "pl.name AS linea, " +
                "mee.mfg_fg AS cantidad_producida, " +
                "unit.code " +
                "FROM s_mfg_est AS me " +
                "INNER JOIN s_mfg_est_ety AS mee ON me.id_mfg_est = mee.id_mfg_est " +
                "INNER JOIN cu_wah AS wah ON mee.fk_wah_co = wah.id_co AND mee.fk_wah_cob = wah.id_cob AND mee.fk_wah_wah = wah.id_wah " +
                "INNER JOIN cu_line AS pl ON wah.fk_line = pl.id_line " +
                "INNER JOIN su_item AS si ON mee.fk_item_con_rm = si.id_item " +
                "INNER JOIN su_item AS pti ON mee.fk_item_mfg_fg = pti.id_item " +
                "INNER JOIN su_unit AS unit ON me.fk_unit = id_unit " +
                "INNER JOIN cu_usr AS ui ON me.fk_usr_ins = ui.id_usr " +
                "INNER JOIN cu_usr AS uu ON me.fk_usr_upd = uu.id_usr " +
                "INNER JOIN cu_usr AS uc ON me.fk_usr_clo = uc.id_usr " +
                "WHERE me.b_del = 0 AND me.dt_mfg_est = '2022-08-14' " +
                "ORDER BY me.dt_mfg_est, me.id_mfg_est, mee.id_ety;";
        
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            html += "<tr>" + 
                    "<td style='width:200px'>" + SLibUtils.textToHtml(resultSet.getString("item")) + "</td>" +
                    "<td>" + SLibUtils.textToHtml(resultSet.getString("item_codigo")) + "</td>" +
                    "<td>" + SLibUtils.textToHtml(resultSet.getString("almacen")) + "</td>" +
                    "<td>" + SLibUtils.textToHtml(resultSet.getString("tipo")) + "</td>" +
                    "<td>" + SLibUtils.textToHtml(resultSet.getString("linea")) + "</td>" +
                    "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("cantidad_producida"))) + "</td>" +
                    "<td>" + SLibUtils.textToHtml(resultSet.getString("code")) + "</td>" +
                    "</tr>";
        }
        html += "</table>"
                + "<br>";
        
        // EXISTENCIAS TANQUES
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("TANQUE") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("ÍTEM") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("TIPO") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("ORIGEN") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("EXISTENCIAS Kg") + "</b></td>" +
                "<td colspan='2' align='center'><b>" + SLibUtils.textToHtml("CAPACIDADES") + "</b></td>" +
                "</tr>" + 
                "<tr>" + 
                "<td colspan='5'></td>" + 
                "<td align='center'>TOTAL</td>" + 
                "<td align='center'>DISP.</td>";
                
        sql = "SELECT " +
                "vw.code AS tanque, " +
                "vi.name AS item, " +
                "SUM(v.mov_in - v.mov_out) AS existencias, " +
                "vw.cap_real_lt " +
                "FROM s_stk AS v " +
                "INNER JOIN su_item AS vi ON v.id_item = vi.id_item " +
                "INNER JOIN su_unit AS vu ON v.id_unit = vu.id_unit " +
                "INNER JOIN cu_wah AS vw ON v.id_co = vw.id_co AND v.id_cob = vw.id_cob AND v.id_wah = vw.id_wah " +
                "INNER JOIN cu_cob AS vc ON v.id_co = vc.id_co AND v.id_cob = vc.id_cob " +
                "WHERE v.b_del = 0 AND v.id_year = 2022 AND  v.dt <= '2022-08-14' " +
                "GROUP BY v.id_item, v.id_unit, v.id_cob, v.id_wah, vc.code, vw.name, vi.code, vi.name, vu.code " +
                "ORDER BY vw.code, vi.name;";
        
        resultSet = session.getStatement().executeQuery(sql);
        String tanque = "";
        String capacidad = "";
        boolean imp = true;
        while (resultSet.next()) {
            if (!resultSet.getString("tanque").equals(tanque)) {
                if (!imp) {
                    // TANQUE VACIO
                    html += "<tr>" + 
                        "<td>" + SLibUtils.textToHtml(tanque) + "</td>" +
                        "<td style='width:200px'> - </td>" +
                        "<td>" + "</td>" +
                        "<td>" + "</td>" +
                        "<td align='right'> - </td>" +
                        "<td align='right'>" + SLibUtils.textToHtml(capacidad) + "</td>" +
                        "<td align='right'>" + SLibUtils.textToHtml(capacidad) + "</td>" +
                        "</tr>";
                    
                }
                tanque = resultSet.getString("tanque");
                capacidad = SLibUtils.DecimalFormatValue2D.format((resultSet.getDouble("cap_real_lt") * 0.92));
                imp = false;
            }
            if (resultSet.getDouble("existencias") != 0) {
                // TANQUE CON PRODUCTO
                html += "<tr>" + 
                        "<td>" + SLibUtils.textToHtml(resultSet.getString("tanque")) + "</td>" +
                        "<td style='width:200px'>" + SLibUtils.textToHtml(resultSet.getString("item")) + "</td>" +
                        "<td>" + "</td>" +
                        "<td>" + "</td>" +
                        "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("existencias"))) + "</td>" +
                        "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("cap_real_lt") * 0.92)) + "</td>" +
                        "<td align='right'>" + SLibUtils.textToHtml(SLibUtils.DecimalFormatValue2D.format((resultSet.getDouble("cap_real_lt") * 0.92) - resultSet.getDouble("existencias"))) + "</td>" +
                        "</tr>";
                imp = true;
            }
        }
        html += "</table>"
                + "<br>";
        
        // INVENTARIO DE ACEITE DE AGUACATE Y RESUMEN DE ACEITES
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("TANQUE") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("CAPACIDAD TANQUE") + "</b></td>" + 
                "<td style='width:200px' align='center'><b>" + SLibUtils.textToHtml("DESCRIPCIÓN DEL PRODUCTO") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("INV. DISPONIBLE") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("% OCUP.") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("ESPACIO DISPONIBLE") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("ACIDEZ") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("% ACIDEZ") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("% ACIDEZ MEZCLA") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("% HUMEDAD") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("% SOLIDOS") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("% LINO") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("% OLEICO") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("COMENTARIO") + "</b></td>" +
                "</tr>";
        
        //sql = "";
        
        //resultSet = session.getStatement().executeQuery(sql);
        //while (resultSet.next()) {
            html += "<tr>" + 
                    "<td>" + SLibUtils.textToHtml("tanque") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td style='width:200px'>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "</tr>";
        //}
        html += "</table>"
                + "<br>";
        
        // RESUMEN AGUACATE
        
        html += "Resumen aguacate <br>";
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("FAMILIA") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("INVENTARIO") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("ACIDEZ PONDERADA") + "</b></td>" +
                "</tr>";
        
        //sql = "";
        
        //resultSet = session.getStatement().executeQuery(sql);
        //while (resultSet.next()) {
            html += "<tr>" + 
                    "<td>" + SLibUtils.textToHtml("aguacate crudo") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("1,015 Tons") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("5.47%") + "</td>" +
                    "</tr>";
        //}
        html += "<tr>" + 
                "<td>" + SLibUtils.textToHtml("Total") + "</td>" +
                "<td>" + SLibUtils.textToHtml("1,015 Tons") + "</td>" +
                "<td>" + SLibUtils.textToHtml("5.47%") + "</td>" +
                "</tr>";
        html += "</table>"
                + "<br>";
        
        // RESUMEN AGUACATE
        
        html += "Resumen aguacate <br>";
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("FAMILIA") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("INVENTARIO") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("ACIDEZ PONDERADA") + "</b></td>" +
                "</tr>";
        
        //sql = "";
        
        //resultSet = session.getStatement().executeQuery(sql);
        //while (resultSet.next()) {
            html += "<tr>" + 
                    "<td>" + SLibUtils.textToHtml("aguacate crudo") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("1,015 Tons") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("5.47%") + "</td>" +
                    "</tr>";
        //}
        html += "<tr>" + 
                "<td>" + SLibUtils.textToHtml("Total") + "</td>" +
                "<td>" + SLibUtils.textToHtml("1,015 Tons") + "</td>" +
                "<td>" + SLibUtils.textToHtml("5.47%") + "</td>" +
                "</tr>";
        html += "</table>"
                + "<br>";
        
        // RESUMEN RESTO DE ACEITES
        
        html += "Resumen resto de aceites <br>";
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("ACEITE") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("INV. DISP.") + "</b></td>" + 
                
                "</tr>";
        
        //sql = "";
        
        //resultSet = session.getStatement().executeQuery(sql);
        //while (resultSet.next()) {
            html += "<tr>" + 
                    "<td>" + SLibUtils.textToHtml("aceite crudo de canola alto oleico granel") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("0 Tons") + "</td>" +
                    "</tr>";
        //}
        html += "<tr>" + 
                "<td>" + SLibUtils.textToHtml("Total") + "</td>" +
                "<td>" + SLibUtils.textToHtml("0 Tons") + "</td>" +
                "</tr>";
        html += "</table>"
                + "<br>";
        
        // Detalle refinados
        
        html += "detalle refinados <br>";
        
//        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
//                "<font size='" + FONT_SIZE_TBL + "'>" + 
//                "<tr>" +
//                "<td align='center'><b>" + SLibUtils.textToHtml("ACEITE") + "</b></td>" + 
//                "<td align='center'><b>" + SLibUtils.textToHtml("INV. DISP.") + "</b></td>" + 
//                
//                "</tr>";
        
        //sql = "";
        
        //resultSet = session.getStatement().executeQuery(sql);
        //while (resultSet.next()) {
            html += "<tr>" + 
                    "<td>" + SLibUtils.textToHtml("aceite cartamo linoleico") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("0 Tons") + "</td>" +
                    "</tr>";
        //}
        html += "<tr>" + 
                "<td>" + SLibUtils.textToHtml("Total") + "</td>" +
                "<td>" + SLibUtils.textToHtml("0 Tons") + "</td>" +
                "</tr>";
        html += "</table>"
                + "<br>";
        
        // REGISTRO RESULTADOS DE TANQUES DE PROCESO
        
        html += "<table border='1' bordercolor='#000000' cellpadding='0' cellspacing='0'>" + 
                "<font size='" + FONT_SIZE_TBL + "'>" + 
                "<tr>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("FECHA DE ANALISIS") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("# TANQUE") + "</b></td>" + 
                "<td align='center'><b>" + SLibUtils.textToHtml("PRODUCTO") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("ACIDEZ") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("I.PEROXIDOS") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("HUMEDAD") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("SOLIDOS") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("LINOLEICO") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("OLEICO") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("LINOLENICO") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("OBSERVACIONES") + "</b></td>" +
                "</tr>";
        
        //sql = "";
        
        //resultSet = session.getStatement().executeQuery(sql);
        //while (resultSet.next()) {
            html += "<tr>" + 
                    "<td>" + SLibUtils.textToHtml("tanque") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "<td>" + SLibUtils.textToHtml("") + "</td>" +
                    "</tr>";
        //}
        html += "</table>"
                + "<br>";
        
        return html;
    }
}
