package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.mail.MessagingException;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import som.gui.SGuiClientApp;
import som.gui.SGuiClientAppAlternative;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.cfg.db.SDbCompany;

/**
 *
 * @author Néstor Ávalos, Sergio Flores, Isabel Servín
 */
public class SSomMailUtils {
    
    private static final int FONT_SIZE_TITLE = 4;
    private static final int FONT_SIZE_INPUT = 5;
    private static final int FONT_SIZE_ITEM = 4;
    private static final int FONT_SIZE_SREG = 4;
    private static final int FONT_SIZE_REG = 3;
    private static final int FONT_SIZE_TBL = 2;
    private static final String COLOR_INPUT_HDR = "#4169E1";
    private static final String COLOR_INPUT_FTR = "#FFD700";
    
    /*
     * Private methods:
     */
    
    /**
     * Composes input type detail, if necessary, that is if total quantity for input type is different from total quantity for last item.
     * @param session Current GUI user session.
     * @param dateStart Period start.
     * @param dateEnd Period end.
     * @param year Current year.
     * @param inputTypeKey Input type PK.
     * @param notificationBoxes String,
     * @param itemUnitId  Item unit PK.
     * @param totInputYear Total quantity in current year for input type.
     * @param totLastItemYear Total quantity in current year for last item belonging to input type.
     * @param formatPercentage Decimal format for percentage values.
     */
    private static String composeInputTypeDetail(final SGuiSession session, final Date dateStart, final Date dateEnd, final int year, final int ticOrig, final int ticDest,
            final int[] inputTypeKey, final String notificationBoxes, final int itemUnitId, final double totInputYear, final double totLastItemYear, final DecimalFormat formatPercentage) throws SQLException {
        String html = "";
        
        if (SLibUtils.round(totInputYear, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()) != SLibUtils.round(totLastItemYear, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits())) {
            String sql;
            Statement statement = session.getStatement().getConnection().createStatement();
            ResultSet resultSet;
            
            sql = "SELECT " +
                    "i.name_sht, i.id_item, " +
                    "SUM(IF(t.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "', t.qty, 0)) AS f_qty_today, " +
                    "SUM(t.qty) AS f_qty_year " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                    "t.fk_item = i.id_item " +
                    "WHERE YEAR(t.dt) = " + year + " AND t.dt <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
                    (ticOrig == 0 ? "" : "AND t.fk_tic_orig = " + ticOrig + " ") +
                    (ticDest == 0 ? "" : "AND t.fk_tic_dest = " + ticDest + " ") +
                    "AND t.b_tar = 1 AND i.b_umn = 1 AND t.b_del = 0 AND " +
                    "i.fk_inp_ct = " + inputTypeKey[0] + " AND i.fk_inp_cl = " + inputTypeKey[1] + " AND i.fk_inp_tp = " + inputTypeKey[2] + " AND i.umn_box = '" + notificationBoxes + "' AND " +
                    "t.fk_unit = " + itemUnitId + " " +
                    "GROUP BY i.name_sht, i.id_item " +
                    "ORDER BY i.name_sht, i.id_item; ";
            
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                html += 
                        "<tr>" +
                        "<td align='left'>" + SLibUtils.textToHtml(resultSet.getString("i.name_sht")) + "</td>" +
                        "<td align='right'>" + SLibUtils.getDecimalFormatAmount().format(resultSet.getDouble("f_qty_today")) + "</td>" +
                        "<td align='right'>" + SLibUtils.getDecimalFormatAmount().format(resultSet.getDouble("f_qty_year")) + "</td>" +
                        "<td align='right'>" + formatPercentage.format(totInputYear == 0 ? 0 : resultSet.getDouble("f_qty_year") / totInputYear) + "</td>" +
                        "</tr>";
            }
        }
        
        return html;
    }

    /**
     * Composes input class table, if necessary, that is if input class of current input type has more than one input type with receptions in current year up to cutoff date.
     * @param session Current GUI user session.
     * @param isByDate Is by date or by period.
     * @param dateStart Period start.
     * @param dateEnd Period end.
     * @param year Current year.
     * @param inputClassKey Input class PK.
     * @param unitId  Item unit PK.
     * @param unitCode Item unit code.
     * @param notificationBoxes Notification mail boxes.
     * @param formatPercentage Decimal format for percentage values.
     */
    private static String composeInputClassTable(final SGuiSession session, final boolean isByDate, final Date dateStart, final Date dateEnd, final int year, final int ticOrig, final int ticDest,
            final int[] inputClassKey, final String notificationBoxes, final int unitId, final String unitCode, final DecimalFormat formatPercentage) throws SQLException {
        int count = 0;
        double totalPeriod = 0;
        double totalYear = 0;
        String sql;
        String html = "";
        Statement statement = session.getStatement().getConnection().createStatement();
        ResultSet resultSet;
        
        sql = "SELECT "
                + "it.name, it.id_inp_ct, it.id_inp_cl, it.id_inp_tp, "
                + "SUM(IF(t.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "', t.qty, 0)) AS _qty_per, "
                + "SUM(t.qty) AS _qty_year "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "t.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_TP) + " AS it ON "
                + "i.fk_inp_ct = it.id_inp_ct AND i.fk_inp_cl = it.id_inp_cl AND i.fk_inp_tp = it.id_inp_tp "
                + "WHERE YEAR(t.dt) = " + year + " AND t.dt <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' "
                + (ticOrig == 0 ? "" : "AND t.fk_tic_orig = " + ticOrig + " ") 
                + (ticDest == 0 ? "" : "AND t.fk_tic_dest = " + ticDest + " ") 
                + "AND t.b_tar = 1 AND i.b_umn = 1 AND t.b_del = 0 AND "
                + "i.fk_inp_ct = " + inputClassKey[0] + " AND i.fk_inp_cl = " + inputClassKey[1] + " AND i.umn_box = '" + notificationBoxes + "' AND "
                + "t.fk_unit = " + unitId + " "
                + "GROUP BY it.name, it.id_inp_ct, it.id_inp_cl, it.id_inp_tp "
                + "ORDER BY it.name, it.id_inp_ct, it.id_inp_cl, it.id_inp_tp";

        resultSet = statement.executeQuery("SELECT COUNT(*) FROM (" + sql + ") AS t");
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        
        if (count > 1) {
            resultSet = statement.executeQuery("SELECT SUM(_qty_per), SUM(_qty_year) FROM (" + sql + ") AS t");
            if (resultSet.next()) {
                totalPeriod = resultSet.getDouble(1);
                totalYear = resultSet.getDouble(2);
            }
            
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                html += 
                        "<tr>" +
                        "<td align='left'>" + SLibUtils.textToHtml(resultSet.getString("it.name")) + "</td>" +
                        "<td align='right'>" + SLibUtils.getDecimalFormatAmount().format(resultSet.getDouble("_qty_per")) + "</td>" +
                        "<td align='right'>" + SLibUtils.getDecimalFormatAmount().format(resultSet.getDouble("_qty_year")) + "</td>" +
                        "<td align='right'>" + formatPercentage.format(totalYear == 0 ? 0 : resultSet.getDouble("_qty_year") / totalYear) + "</td>" +
                        "</tr>";
            }
            
            String inputClass = (String) session.readField(SModConsts.SU_INP_CL, inputClassKey, SDbRegistry.FIELD_NAME);
            
            html =
                    "<font size='" + (FONT_SIZE_INPUT - 1) + "' color='" + COLOR_INPUT_HDR + "'><b>" + SLibUtils.textToHtml("TOTAL CLASE: " + inputClass + " (" + unitCode + ") " + year) + "</b></font>" +
                    "<br>" +
                    "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                    "<font size='" + FONT_SIZE_TBL + "'>" +
                    "<tr>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("Concepto") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + unitCode + ")") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + unitCode + ")") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                    "</tr>" +
                    html +
                    "<tr bgcolor='" + COLOR_INPUT_FTR + "'>" +
                    "<td align='left'><b>" + SLibUtils.textToHtml("Total clase") + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totalPeriod) + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totalYear) + "</b></td>" +
                    "<td align='right'><b>" + formatPercentage.format(totalYear == 0 ? 0 : 1) + "</b></td>" +
                    "</tr>" +
                    "</font>" +
                    "</table>" +
                    "<br>";
        }
        
        return html;
    }

    /**
     * Sends receptions notifications by mail.
     * @param session SGuiSession,
     * @param mailSubject String,
     * @param mailBodyHtml String,
     * @param recipientsTo String,
     * @param isDataAvailable boolean,
     * @param isByDate Is by date or by period.
     * @param dateStart Period start.
     * @param dateEnd Period end.
     * @param year int,
     * @param formatPercentage DecimalFormat,
     * @param txtSregion String,
     * @param txtItem String,
     * @param txtItemUnit String,
     * @param itemUnitId Item PK,
     * @param txtInput String,
     * @param inputKey Input type PK.
     * @param txtInputUnit String,
     * @param totRegionCurr double,
     * @param totRegionYear double,
     * @param totSregionCurr double,
     * @param totSregionYear double,
     * @param totItemCurr double,
     * @param totItemYear double,
     * @param totInputCurr int,
     * @param totInputYear int,
     * @return boolean.
     */
    private static void sendMailReceptions(final SGuiSession session, final String mailSubject, final String mailBodyHtml, final String recipientsTo, final String recipientsBcc, 
            final boolean isDataAvailable, final boolean isByDate, final Date dateStart, final Date dateEnd, final int year, final int ticOrig, final int ticDest, final DecimalFormat formatPercentage, 
            final String txtSregion, final String txtItem, final String txtItemUnit, final int itemUnitId, final String txtInput, final int[] inputKey, final String txtInputUnit, 
            final double totRegionCurr, final double totRegionYear, final double totSregionCurr, final double totSregionYear, 
            final double totItemCurr, final double totItemYear, final double totInputCurr, final double totInputYear) throws SQLException, MessagingException {
        String html = mailBodyHtml;
        SDbCompany company = ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany();
        SMail mail;
        SMailSender sender = new SMailSender(
                company.getMailNotificationConfigHost(),
                company.getMailNotificationConfigPort(),
                company.getMailNotificationConfigProtocol(),
                company.isMailNotificationConfigStartTls(),
                company.isMailNotificationConfigAuth(),
                company.getMailNotificationConfigUser(),
                company.getMailNotificationConfigPassword(),
                company.getMailNotificationConfigUser());

        if (isDataAvailable) {
            //================================================================================
            // START OF SNIPPET HTML CODE #1 (Note: when modified, update aswell all snippets in this class!)

            // Region table footer:
            html +=
                    "<tr>" +
                    "<td align='left'><b>" + SLibUtils.textToHtml("Total región") + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionCurr) + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionYear) + "</b></td>" +
                    "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : totRegionYear / totItemYear) + "</b></td>" +
                    "</tr>" +
                    "</font>" +
                    "</table>" +
                    "<br>";

            // Supraregion summary table:
            html +=
                    "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                    "<font size='" + FONT_SIZE_TBL + "'>" +
                    "<tr>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("Total supraregión") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtItemUnit + ")") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtItemUnit + ")") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td align='left'><b>" + SLibUtils.textToHtml(txtSregion) + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totSregionCurr) + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totSregionYear) + "</b></td>" +
                    "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : totSregionYear / totItemYear) + "</b></td>" +
                    "</tr>" +
                    "</font>" +
                    "</table>" +
                    "<br>";

            // Item & Unit summary title:
            html +=
                    "<table border='0' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                    "<font size='" + FONT_SIZE_TBL + "'>" +
                    "<tr><td colspan='4'><hr></td></tr>" +
                    "<tr><td colspan='4'><b>" + SLibUtils.textToHtml(txtItem + " (" + txtItemUnit + ")") + "</b></td></tr>" +
                    "</font>" +
                    "</table>";

            // Item & Unit summary table:
            html +=
                    "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                    "<font size='" + FONT_SIZE_TBL + "'>" +
                    "<tr>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("Concepto") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtItemUnit + ")") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtItemUnit + ")") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td align='left'><b>" + SLibUtils.textToHtml("Total producto") + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totItemCurr) + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totItemYear) + "</b></td>" +
                    "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : 1) + "</b></td>" +
                    "</tr>" +
                    "</font>" +
                    "</table>" +
                    "<br>";

            // Input Type & Unit summary title:
            html +=
                    "<hr>" +
                    "<font size='" + (FONT_SIZE_INPUT - 1) + "' color='" + COLOR_INPUT_HDR + "'><b>" + SLibUtils.textToHtml("TOTAL INSUMO: " + txtInput + " (" + txtInputUnit + ") " + year) + "</b></font>" +
                    "<br>";

            // Input Type & Unit summary table:
            html +=
                    "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                    "<font size='" + FONT_SIZE_TBL + "'>" +
                    "<tr>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("Concepto") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtInputUnit + ")") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtInputUnit + ")") + "</b></td>" +
                    "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                    "</tr>" +
                    composeInputTypeDetail(session, dateStart, dateEnd, year, ticOrig, ticDest, inputKey, recipientsTo, itemUnitId, totInputYear, totItemYear, formatPercentage) +
                    "<tr bgcolor='" + COLOR_INPUT_FTR + "'>" +
                    "<td align='left'><b>" + SLibUtils.textToHtml("Total insumo") + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totInputCurr) + "</b></td>" +
                    "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totInputYear) + "</b></td>" +
                    "<td align='right'><b>" + formatPercentage.format(totInputYear == 0 ? 0 : 1) + "</b></td>" +
                    "</tr>" +
                    "</font>" +
                    "</table>" +
                    "<br>";
            
            html += composeInputClassTable(session, isByDate, dateStart, dateEnd, year, ticOrig, ticDest, inputKey, recipientsTo, itemUnitId, txtInputUnit, formatPercentage);

            // END OF SNIPPET HTML CODE #1 (Note: when modified, update aswell all snippets in this class!)
            //================================================================================
        }
        // Year summary
        
        html += yearSummary(session, year, ticOrig, ticDest);
        
        // Mail footer:
        
        html += composeSomMailWarning();

        mail = new SMail(sender, mailSubject, html, new ArrayList<String>(Arrays.asList(SLibUtils.textExplode(recipientsTo, ";"))));
        if (!recipientsBcc.isEmpty()) {
            mail.getBccRecipients().addAll(new ArrayList<String>(Arrays.asList(SLibUtils.textExplode(recipientsBcc, ";"))));
        }
        mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
        mail.send();
        System.out.println("Correo enviado!");
    }
    
    private static String yearSummary(final SGuiSession session, final int year, final int ticOrig, final int ticDest) throws SQLException {
        DecimalFormat formatPercentage = new DecimalFormat("#,##0.0%");
        
        // TOTALES DEL AÑO
        double totalYear = 0;
        double totalLastYear = 0;
        double totalAncestorYear = 0;
        double total3YearAgo = 0;
        String sql = "SELECT " +
                "SUM(IF(YEAR(t.dt) = " + year + ", t.qty, 0.0)) AS y, " +
                "SUM(IF(YEAR(t.dt) = " + (year - 1) + ", t.qty, 0.0)) AS y_1, " +
                "SUM(IF(YEAR(t.dt) = " + (year - 2) + ", t.qty, 0.0)) AS y_2, " +
                "SUM(IF(YEAR(t.dt) = " + (year - 3) + ", t.qty, 0.0)) AS y_3 " +
                "FROM s_tic AS t " +
                "INNER JOIN su_item AS i ON t.fk_item = i.id_item " +
                "INNER JOIN su_inp_tp AS tp ON i.fk_inp_ct = tp.id_inp_ct AND i.fk_inp_cl = tp.id_inp_cl AND i.fk_inp_tp = tp.id_inp_tp " +
                "WHERE YEAR(t.dt) BETWEEN " + (year - 3) + " AND " + year + " AND i.b_umn AND NOT t.b_del AND t.b_tar " +
                (ticOrig == 0 ? "" : "AND t.fk_tic_orig = " + ticOrig + " ") +
                (ticDest == 0 ? "" : "AND t.fk_tic_dest = " + ticDest + " ") +
                ";";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            totalYear = resultSet.getDouble("y");
            totalLastYear = resultSet.getDouble("y_1");
            totalAncestorYear = resultSet.getDouble("y_2");
            total3YearAgo = resultSet.getDouble("y_3");
        }
        
        // TABLA TOTALES
        
        String html = "<hr>" +
                "<font size='" + (FONT_SIZE_INPUT - 1) + "' color='" + COLOR_INPUT_HDR + "'><b>" + SLibUtils.textToHtml("RESUMEN ANUAL") + "</b></font>" +
                "<br>" +
                "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                "<font size='" + FONT_SIZE_TBL + "'>" +
                "<tr>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("Insumo") + "</b></td>" +
                "<td align='center' colspan=\"2\"><b>" + SLibUtils.textToHtml(year + " (kg)") + "</b></td>" +
                "<td align='center' colspan=\"2\"><b>" + SLibUtils.textToHtml((year - 1) + " (kg)") + "</b></td>" +
                "<td align='center' colspan=\"2\"><b>" + SLibUtils.textToHtml((year - 2) + " (kg)") + "</b></td>" +
                "<td align='center' colspan=\"2\"><b>" + SLibUtils.textToHtml((year - 3) + " (kg)") + "</b></td>" +
                "</tr>";
        
        sql = "SELECT tp.name, " +
                "SUM(IF(YEAR(t.dt) = " + year + ", t.qty, 0.0)) AS y, " +
                "SUM(IF(YEAR(t.dt) = " + (year - 1) + ", t.qty, 0.0)) AS y_1, " +
                "SUM(IF(YEAR(t.dt) = " + (year - 2) + ", t.qty, 0.0)) AS y_2, " +
                "SUM(IF(YEAR(t.dt) = " + (year - 3) + ", t.qty, 0.0)) AS y_3 " +
                "FROM s_tic AS t " +
                "INNER JOIN su_item AS i ON t.fk_item = i.id_item " +
                "INNER JOIN su_inp_tp AS tp ON i.fk_inp_ct = tp.id_inp_ct AND i.fk_inp_cl = tp.id_inp_cl AND i.fk_inp_tp = tp.id_inp_tp " +
                "WHERE year(t.dt) BETWEEN " + (year - 3) + " AND " + year + " AND i.b_umn AND NOT t.b_del AND t.b_tar " +
                (ticOrig == 0 ? "" : "AND t.fk_tic_orig = " + ticOrig + " ") +
                (ticDest == 0 ? "" : "AND t.fk_tic_dest = " + ticDest + " ") +
                "GROUP BY tp.id_inp_ct, tp.id_inp_cl, tp.id_inp_tp;";
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            html += "<tr>" +
                    "<td align='left'><b>" + SLibUtils.textToHtml(resultSet.getString(1)) + "</b></td>" +
                    "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble(2)) + "</td>" +
                    "<td align='right'><font size='0.7'>" + formatPercentage.format(resultSet.getDouble(2)/totalYear) + "</font></td>" +
                    "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble(3)) + "</td>" +
                    "<td align='right'><font size='0.7'>" + formatPercentage.format(resultSet.getDouble(3)/totalLastYear) + "</font></td>" +
                    "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble(4)) + "</td>" +
                    "<td align='right'><font size='0.7'>" + formatPercentage.format(resultSet.getDouble(4)/totalAncestorYear) + "</font></td>" +
                    "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble(5)) + "</td>" +
                    "<td align='right'><font size='0.7'>" + formatPercentage.format(resultSet.getDouble(5)/total3YearAgo) + "</font></td>" +
                    "</tr>";
        }
        html += "<tr bgcolor='" + COLOR_INPUT_FTR + "'>" +
                "<td align='left'><b>" + SLibUtils.textToHtml("TOTAL") + "</b></td>" +
                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totalYear) + "</b></td>" +
                "<td align='right'><font size='0.7'><b>" + formatPercentage.format(1) + "</b></font></td>" +
                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totalLastYear) + "</b></td>" +
                "<td align='right'><font size='0.7'><b>" + formatPercentage.format(1) + "</b></font></td>" +
                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totalAncestorYear) + "</b></td>" +
                "<td align='right'><font size='0.7'><b>" + formatPercentage.format(1) + "</b></font></td>" +
                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(total3YearAgo) + "</b></td>" +
                "<td align='right'><font size='0.7'><b>" + formatPercentage.format(1) + "</b></font></td>" +
                "</tr>" +
                "</font>" +
                "</table>" +
                "<br>";
        
        // SEGUNDA TABLA POR SUPRAREGIÓN
        
        html += //"<hr>" +
                "<font size='" + (FONT_SIZE_INPUT - 1) + "' color='" + COLOR_INPUT_HDR + "'><b>" + SLibUtils.textToHtml("RESUMEN ANUAL POR SUPRAREGIÓN") + "</b></font>" +
                "<br>" +
                "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                "<font size='" + FONT_SIZE_TBL + "'>" +
                "<tr>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("Insumo") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml("Supraregión") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml(year + " (kg)") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml((year - 1) + " (kg)") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml((year - 2) + " (kg)") + "</b></td>" +
                "<td align='center'><b>" + SLibUtils.textToHtml((year - 3) + " (kg)") + "</b></td>" +
                "</tr>";
        
        // DESGLOCE POR SUPRAREGION

        String insName = "";
        int rowspan = 0; 
        double sumTotalYearIns = 0;
        double sumTotalLastYearIns = 0;
        sql = "SELECT tp.name, IF(sreg.name IS NOT NULL, 0, 1) AS _order, IF(sreg.name IS NULL, 'N/D', sreg.name) as supra, " + 
                "SUM(IF(YEAR(t.dt) = " + year + ", t.qty, 0.0)) AS y, " + 
                "SUM(IF(YEAR(t.dt) = " + (year - 1) + ", t.qty, 0.0)) AS y_1, " +
                "SUM(IF(YEAR(t.dt) = " + (year - 2) + ", t.qty, 0.0)) AS y_2, " +
                "SUM(IF(YEAR(t.dt) = " + (year - 3) + ", t.qty, 0.0)) AS y_3 " +
                "FROM s_tic AS t " +
                "INNER JOIN su_item AS i ON t.fk_item = i.id_item " +
                "INNER JOIN su_inp_tp AS tp ON i.fk_inp_ct = tp.id_inp_ct AND i.fk_inp_cl = tp.id_inp_cl AND i.fk_inp_tp = tp.id_inp_tp " +
                "LEFT JOIN su_reg AS reg ON t.fk_reg_n = reg.id_reg " +
                "LEFT JOIN su_sup_reg AS sreg ON reg.fk_sup_reg = sreg.id_sup_reg " + 
                "WHERE year(t.dt) BETWEEN " + (year - 3) + " AND " + year + " AND i.b_umn AND NOT t.b_del AND t.b_tar " +
                (ticOrig == 0 ? "" : "AND t.fk_tic_orig = " + ticOrig + " ") +
                (ticDest == 0 ? "" : "AND t.fk_tic_dest = " + ticDest + " ") +
                "GROUP BY tp.id_inp_ct, tp.id_inp_cl, tp.id_inp_tp, sreg.name " + 
                "ORDER by tp.name, _order, sreg.sort, supra";
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            // Ítem diferente al anterior
            if (!insName.equals(resultSet.getString("name"))) {
                sumTotalYearIns = 0;
                sumTotalLastYearIns = 0;
                html = html.replaceAll("replace", "<td align='left' rowspan='" + (rowspan) + "'><b>" + SLibUtils.textToHtml(insName) + "</b></td>");
                html += "<tr>" +
//                        "<td align='left'><b>" + SLibUtils.textToHtml(resultSet.getString(1)) + "</b></td>" +
                        " replace " +
                        "<td align='left'>" + SLibUtils.textToHtml(resultSet.getString("supra")) + "</b></td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("y")) + "</td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("y_1")) + "</td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("y_2")) + "</td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("y_3")) + "</td>" +
                        "</tr>";
                sumTotalYearIns += resultSet.getDouble("y");
                sumTotalLastYearIns += resultSet.getDouble("y_1");
                rowspan = 1;
                insName = resultSet.getString("name");
            }
            else {
                sumTotalYearIns += resultSet.getDouble("y");
                sumTotalLastYearIns += resultSet.getDouble("y_1");
                html += "<tr>" +
                        "<td align='left'>" + SLibUtils.textToHtml(resultSet.getString("supra")) + "</td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("y")) + "</td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("y_1")) + "</td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("y_2")) + "</td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("y_3")) + "</td>" +
                        "</tr>";
                rowspan++;
            }
        }
        html = html.replaceAll("replace", "<td align='left' rowspan='" + (rowspan) + "'><b>" + SLibUtils.textToHtml(insName) + "</b></td>");
        html += "<tr bgcolor='" + COLOR_INPUT_FTR + "'>" +
                "<td align='left' colspan='2'><b>" + SLibUtils.textToHtml("TOTAL") + "</b></td>" +
                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totalYear) + "</b></td>" +
                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totalLastYear) + "</b></td>" +
                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totalAncestorYear) + "</b></td>" +
                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(total3YearAgo) + "</b></td>" +
                "</tr>" +
                "</font>" +
                "</table>" +
                "<br>";
        
        return html;
    }
    
    private static int noInformationFound(final SGuiSession session, final Date dateStart, final Date dateEnd, final int ticOrig, final int ticDest, final boolean isByDate, 
            final String subject, String message, final int year, final DecimalFormat formatPercentage, int count, String mailTo, String mailBcc) throws Exception {
        // If no information found:
        String html;
        String sql;
        //================================================================================
        // START OF SNIPPET HTML CODE #2 (Note: when modified, update aswell all snippets in this class!)

        // Body header:
        html =  "<head> " +
                "<style> " +
                "table, th, td {" +
                "border: 1px solid black;" +
                "border-collapse: collapse;" +
                "padding-left: 5px; padding-right: 5px;" +
                "}" +
                "</style> " +
                "</head>" +
                "<font size='" + FONT_SIZE_TITLE + "'><b>" +
                SLibUtils.textToHtml(((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getName()) + "<br>" +
                SSomConsts.SOM_MAIL_MAN + SLibUtils.DateFormatDate.format(dateStart) + (isByDate ? "" : " al " + SLibUtils.DateFormatDate.format(dateEnd)) +
                "</b></font>" +
                "<br>" +
                "<br>";

        // END OF SNIPPET HTML CODE #2 (Note: when modified, update aswell all snippets in this class!)
        //================================================================================

        html +=
                "<hr>" +
                "<font size='" + (FONT_SIZE_TITLE - 1) + "'><b>" +
                SLibUtils.textToHtml(message) +
                "</b></font>" +
                "<br>" +
                "<br>";

        if (mailTo.isEmpty()) {
            sql = "SELECT DISTINCT i.umn_box " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i " +
                    "WHERE i.b_umn = 1 AND i.b_del = 0 " +
                    "ORDER BY i.umn_box ";
            ResultSet resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                sendMailReceptions(session, subject, html, resultSet.getString("i.umn_box"), mailBcc, false,
                        isByDate, dateStart, dateEnd, year, ticOrig, ticDest, formatPercentage,
                        "", "", "", 0, "", null, "",
                        0, 0, 0, 0,
                        0, 0, 0, 0);
                count++;
            }
        }
        else {
            sendMailReceptions(session, subject, html, mailTo, mailBcc, false,
                    isByDate, dateStart, dateEnd, year, ticOrig, ticDest, formatPercentage,
                    "", "", "", 0, "", null, "",
                    0, 0, 0, 0,
                    0, 0, 0, 0);
            count++;
        }
        return count;
    }
    
    /*
     * Public methods:
     */

    /**
     * Computes receptions notifications by mail.
     * @param session SGuiSession.
     * @param dateStart Start date.
     * @param dateEnd End date.
     * @param ticOrig
     * @param ticDest
     * @param isMailer
     * @param ordinaryPeriod
     * @param noReceptionMail
     * @param daysToSendMail
     * @param mailTo
     * @param mailBcc
     * @return int Mails sent.
     */
    public static int computeMailReceptions(final SGuiSession session, final Date dateStart, final Date dateEnd, final int ticOrig, final int ticDest,
            final boolean isMailer, final boolean ordinaryPeriod, final String noReceptionMail, final int daysToSendMail, final String mailTo, final String mailBcc) {
        int count = 0;
        boolean isByDate;
        boolean sent = false;
        String sql;
        String umn_box = "";
        String subject;
        String html = "";
        String txtInput = "";
        String txtInputUnit = "";
        String txtItem = "";
        String txtItemUnit = "";
        String txtSregion = "";
        String txtRegion;
        int year;
        int[] key;
        int[] inputKey = null;
        int inputUnitId = SLibConsts.UNDEFINED;
        int itemId = SLibConsts.UNDEFINED;
        int itemUnitId = SLibConsts.UNDEFINED;
        int sregionId = SLibConsts.UNDEFINED;
        int regionId = SLibConsts.UNDEFINED;
        double totInputCurr = 0;
        double totInputYear = 0;    // comes from yearly totals
        double totItemCurr = 0;
        double totItemYear = 0;         // comes from yearly totals
        double totSregionCurr = 0;
        double totSregionYear = 0;
        double totRegionCurr = 0;
        double totRegionYear = 0;
        SSomReportProcessor processorByInput = new SSomReportProcessor();
        SSomReportProcessor processorByItem = new SSomReportProcessor();
        DecimalFormat formatPercentage = new DecimalFormat("#,##0.0%");

        try {
            Statement statement = session.getDatabase().getConnection().createStatement();
            ResultSet resultSet;

            isByDate = SLibTimeUtils.isSameDate(dateStart, dateEnd);

            // Mail's subject:

            subject = "[SOM] " + SSomConsts.SOM_MAIL_MAN + SLibUtils.DateFormatDate.format(dateStart) + (isByDate ? "" :
                " al " + SLibUtils.DateFormatDate.format(dateEnd)) + " ";
            
            year = SLibTimeUtils.digestYear(dateEnd)[0];

            // Get & preserve yearly totals by input-type & unit:

            sql = "SELECT " +
                    "i.umn_box, i.fk_inp_ct, i.fk_inp_cl, i.fk_inp_tp, t.fk_unit, " +
                    "SUM(t.qty) AS f_qty_year " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                    "t.fk_item = i.id_item " +
                    "WHERE YEAR(t.dt) = " + year + " AND t.dt <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' AND " +
                    "t.b_tar = 1 AND i.b_umn = 1 AND t.b_del = 0 " +
                    (ticOrig == 0 ? "" : "AND t.fk_tic_orig = " + ticOrig + " ") +
                    (ticDest == 0 ? "" : "AND t.fk_tic_dest = " + ticDest + " ") +
                    "GROUP BY i.umn_box, i.fk_inp_ct, i.fk_inp_cl, i.fk_inp_tp, t.fk_unit " +
                    "ORDER BY i.umn_box, i.fk_inp_ct, i.fk_inp_cl, i.fk_inp_tp, t.fk_unit; ";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                processorByInput.addEntry(
                        resultSet.getString("umn_box"),
                        new int[] { resultSet.getInt("fk_inp_ct"), resultSet.getInt("fk_inp_cl"), resultSet.getInt("fk_inp_tp") },
                        resultSet.getInt("fk_unit"),
                        resultSet.getDouble("f_qty_year"));
            }

            // Get & preserve yearly totals by item & unit:

            sql = "SELECT " +
                    "i.umn_box, t.fk_item, t.fk_unit, " +
                    "SUM(t.qty) AS f_qty_year " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                    "t.fk_item = i.id_item " +
                    "WHERE YEAR(t.dt) = " + year + " AND t.dt <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
                    (ticOrig == 0 ? "" : "AND t.fk_tic_orig = " + ticOrig + " ") +
                    (ticDest == 0 ? "" : "AND t.fk_tic_dest = " + ticDest + " ") +
                    "AND t.b_tar = 1 AND i.b_umn = 1 AND t.b_del = 0 AND " +
                    "(i.b_umn_owm = 0 OR i.id_item IN (" +
                    "SELECT DISTINCT xt.fk_item " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS xt " +
                    "WHERE xt.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
                    (ticOrig == 0 ? "" : "AND xt.fk_tic_orig = " + ticOrig + " ") +
                    (ticDest == 0 ? "" : "AND xt.fk_tic_dest = " + ticDest + " ") +
                    "AND xt.b_tar = 1 AND xt.b_del = 0 ORDER BY xt.fk_item)) " +
                    "GROUP BY i.umn_box, t.fk_item, t.fk_unit " +
                    "ORDER BY i.umn_box, t.fk_item, t.fk_unit; ";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                processorByItem.addEntry(
                        resultSet.getString("umn_box"),
                        new int[] { resultSet.getInt("fk_item") },
                        resultSet.getInt("fk_unit"),
                        resultSet.getDouble("f_qty_year"));
            }

            // Mail's body detail entries:
            
            sql = "SELECT " +
                    "i.umn_box, it.name, it.id_inp_ct, it.id_inp_cl, it.id_inp_tp, " +
                    "i.name, i.id_item, u.code, u.id_unit, " +
                    "COALESCE(sr.name, 'NO DEFINIDA') AS f_sup_name, " +
                    "COALESCE(sr.id_sup_reg, -1) AS f_id_sup, " +
                    "COALESCE(r.name, 'NO DEFINIDA') AS f_reg_name, " +
                    "COALESCE(r.id_reg, -1) AS f_id_reg, " +
                    "p.name_trd, p.id_prod, " +
                    "SUM(IF(t.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "', t.qty, 0)) AS f_qty_today, " +
                    "SUM(t.qty) AS f_qty_year " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                    "t.fk_item = i.id_item " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_TP) + " AS it ON " +
                    "i.fk_inp_ct = it.id_inp_ct AND i.fk_inp_cl = it.id_inp_cl AND i.fk_inp_tp = it.id_inp_tp " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                    "t.fk_unit = u.id_unit " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON " +
                    "t.fk_prod = p.id_prod " +
                    "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS r ON " +
                    "t.fk_reg_n = r.id_reg " +
                    "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SUP_REG) + " AS sr ON " +
                    "r.fk_sup_reg = sr.id_sup_reg " +
                    "WHERE YEAR(t.dt) = " + year + " AND t.dt <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
                    (ticOrig == 0 ? "" : "AND t.fk_tic_orig = " + ticOrig + " ") +
                    (ticDest == 0 ? "" : "AND t.fk_tic_dest = " + ticDest + " ") +
                    "AND t.b_tar = 1 AND i.b_umn = 1 AND t.b_del = 0 AND " +
                    "(i.b_umn_owm = 0 OR i.id_item IN (" +
                    "SELECT DISTINCT xt.fk_item " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS xt " +
                    "WHERE xt.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
                    (ticOrig == 0 ? "" : "AND xt.fk_tic_orig = " + ticOrig + " ") +
                    (ticDest == 0 ? "" : "AND xt.fk_tic_dest = " + ticDest + " ") +
                    "AND xt.b_tar = 1 AND xt.b_del = 0 ORDER BY xt.fk_item)) " +
                    "GROUP BY i.umn_box, it.name, it.id_inp_ct, it.id_inp_cl, it.id_inp_tp, " +
                    "i.name, i.id_item, u.code, u.id_unit, " +
                    "sr.name, sr.id_sup_reg, r.name, r.id_reg, p.name_trd, p.id_prod " +
                    "ORDER BY i.umn_box, it.name, it.id_inp_ct, it.id_inp_cl, it.id_inp_tp, " +
                    "i.name, i.id_item, u.code, u.id_unit, " +
                    "sr.name, sr.id_sup_reg, r.name, r.id_reg, p.name_trd, p.id_prod; ";
            
            umn_box = mailTo.isEmpty() ? umn_box : mailTo;
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                
                if (resultSet.getString("i.umn_box").compareTo(umn_box) != 0) {
                    // Change of User Mail Notification Boxes (recipients), i.e., a new mail is composed:
                    
                    if (!html.isEmpty() && !umn_box.isEmpty()) {
                        sendMailReceptions(session, subject, html, umn_box, mailBcc, true,
                                isByDate, dateStart, dateEnd, year, ticOrig, ticDest, formatPercentage,
                                txtSregion, txtItem, txtItemUnit, itemUnitId, txtInput, inputKey, txtInputUnit,
                                totRegionCurr, totRegionYear, totSregionCurr, totSregionYear,
                                totItemCurr, totItemYear, totInputCurr, totInputYear);
                        count++;
                    }

                    //================================================================================
                    // START OF SNIPPET HTML CODE #2 (Note: when modified, update aswell all snippets in this class!)
                    
                    // Body header:
                    html =  "<head> " +
                            "<style> " +
                            "table, th, td {" +
                            "border: 1px solid black;" +
                            "border-collapse: collapse;" +
                            "padding-left: 5px; padding-right: 5px;" +
                            "}" +
                            "</style> " +
                            "</head>" +
                            "<font size='" + FONT_SIZE_TITLE + "'><b>" +
                            SLibUtils.textToHtml(((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getName()) + "<br>" +
                            SSomConsts.SOM_MAIL_MAN + SLibUtils.DateFormatDate.format(dateStart) + (isByDate ? "" : " al " + SLibUtils.DateFormatDate.format(dateEnd)) +
                            "</b></font>" +
                            "<br>" +
                            "<br>";
                    
                    // END OF SNIPPET HTML CODE #2 (Note: when modified, update aswell all snippets in this class!)
                    //================================================================================

                    inputKey = null;
                    inputUnitId = SLibConsts.UNDEFINED;
                    txtInput = "";
                    txtInputUnit = "";
                    totInputCurr = 0;
                    totInputYear = 0;
                    
                    itemId = SLibConsts.UNDEFINED;
                    itemUnitId = SLibConsts.UNDEFINED;
                    txtItem = "";
                    txtItemUnit = "";
                    totItemCurr = 0;
                    totItemYear = 0;
                    
                    sregionId = SLibConsts.UNDEFINED;
                    txtSregion = "";
                    totSregionCurr = 0;
                    totSregionYear = 0;
                    
                    regionId = SLibConsts.UNDEFINED;
                    totRegionCurr = 0;
                    totRegionYear = 0;

                    umn_box = resultSet.getString("i.umn_box");
                }
                
                key = new int[] { resultSet.getInt("it.id_inp_ct"), resultSet.getInt("it.id_inp_cl"), resultSet.getInt("it.id_inp_tp") };

                if (!SLibUtils.compareKeys(key, inputKey) || resultSet.getInt("u.id_unit") != inputUnitId) {
                    // Change of Input Type & Unit:

                    if (inputKey != null && inputUnitId != SLibConsts.UNDEFINED) {
                        //================================================================================
                        // START OF SNIPPET HTML CODE #1 (Note: when modified, update aswell all snippets in this class!)
                        
                        // Region table footer:
                        html +=
                                "<tr>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml("Total región") + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : totRegionYear / totItemYear) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";

                        // Supraregion summary table:
                        html +=
                                "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                                "<font size='" + FONT_SIZE_TBL + "'>" +
                                "<tr>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Total supraregión") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                                "</tr>" +
                                "<tr>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml(txtSregion) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totSregionCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totSregionYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : totSregionYear / totItemYear) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";

                        // Item & Unit summary title:
                        html +=
                                "<table border='0' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                                "<font size='" + FONT_SIZE_TBL + "'>" +
                                // Para destacar los totales
                                "<tr><td colspan='4'><hr></td></tr>" +
                                "<tr><td colspan='4'><b>" + SLibUtils.textToHtml(txtItem + " (" + txtItemUnit + ")") + "</b></td></tr>" +
                                "</font>" +
                                "</table>";
                        
                        // Item & Unit summary table:
                        html +=
                                "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                                "<font size='" + FONT_SIZE_TBL + "'>" +
                                "<tr>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Concepto") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                                "</tr>" +
                                "<tr>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml("Total producto") + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totItemCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totItemYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : 1) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";

                        // Input Type & Unit summary title:
                        html +=
                                "<hr>" +
                                "<font size='" + (FONT_SIZE_INPUT - 1) + "' color='" + COLOR_INPUT_HDR + "'><b>" + SLibUtils.textToHtml("TOTAL INSUMO: " + txtInput + " (" + txtInputUnit + ") " + year) + "</b></font>" +
                                "<br>";

                        // Input Type & Unit summary table:
                        html +=
                                "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                                "<font size='" + FONT_SIZE_TBL + "'>" +
                                "<tr>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Concepto") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtInputUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtInputUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                                "</tr>" +
                                composeInputTypeDetail(session, dateStart, dateEnd, year, ticOrig, ticDest, inputKey, umn_box, itemUnitId, totInputYear, totItemYear, formatPercentage) +
                                "<tr bgcolor='" + COLOR_INPUT_FTR + "'>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml("Total insumo") + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totInputCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totInputYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totInputYear == 0 ? 0 : 1) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";
                        
                        html += composeInputClassTable(session, isByDate, dateStart, dateEnd, year, ticOrig, ticDest, inputKey, umn_box, itemUnitId, txtInputUnit, formatPercentage);
                        
                        // END OF SNIPPET HTML CODE #1 (Note: when modified, update aswell all snippets in this class!)
                        //================================================================================
                    }

                    inputKey = key;
                    inputUnitId = resultSet.getInt("u.id_unit");
                    txtInput = resultSet.getString("it.name");
                    txtInputUnit = resultSet.getString("u.code");
                    totInputCurr = 0;
                    
                    itemId = SLibConsts.UNDEFINED;
                    itemUnitId = SLibConsts.UNDEFINED;
                    txtItem = "";
                    txtItemUnit = "";
                    totItemCurr = 0;
                    totItemYear = 0;
                    
                    sregionId = SLibConsts.UNDEFINED;
                    txtSregion = "";
                    totSregionCurr = 0;
                    totSregionYear = 0;
                    
                    regionId = SLibConsts.UNDEFINED;
                    totRegionCurr = 0;
                    totRegionYear = 0;
                    
                    totInputYear = processorByInput.getEntry(umn_box, key, inputUnitId).Reception;

                    // Input Type & Unit title:
                    html += "<hr size='2'>";
                    html += "<font size='" + FONT_SIZE_INPUT + "' color='" + COLOR_INPUT_HDR + "'><b>" + SLibUtils.textToHtml("INSUMO: " + txtInput + " (" + txtInputUnit + ") " + year) + "</b></font><br>";
                }

                if (resultSet.getInt("i.id_item") != itemId || resultSet.getInt("u.id_unit") != itemUnitId) {
                    // Change of Item & Unit:

                    if (itemId != SLibConsts.UNDEFINED && itemUnitId != SLibConsts.UNDEFINED) {
                        // Region table footer:
                        html +=
                                "<tr>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml("Total región") + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : totRegionYear / totItemYear) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";

                        // Supraregion summary table:
                        html +=
                                "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                                "<font size='" + FONT_SIZE_TBL + "'>" +
                                "<tr>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Total supraregión") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                                "</tr>" +
                                "<tr>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml(txtSregion) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totSregionCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totSregionYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : totSregionYear / totItemYear) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";

                        // Item & Unit summary title:
                        html +=
                                "<table border='0' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                                "<font size='" + FONT_SIZE_TBL + "'>" +
                                // Para destacar los totales
                                "<tr><td colspan='4'><hr></td></tr>" +
                                "<tr><td colspan='4'><b>" + SLibUtils.textToHtml(txtItem + " (" + txtItemUnit + ")") + "</b></td></tr>" +
                                "</font>" +
                                "</table>";
                        
                        // Item & Unit summary table:
                        html +=
                                "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                                "<font size='" + FONT_SIZE_TBL + "'>" +
                                "<tr>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Concepto") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                                "</tr>" +
                                "<tr>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml("Total producto") + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totItemCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totItemYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : 1) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";
                    }

                    itemId = resultSet.getInt("i.id_item");
                    itemUnitId = resultSet.getInt("u.id_unit");
                    txtItem = resultSet.getString("i.name");
                    txtItemUnit = resultSet.getString("u.code");
                    totItemCurr = 0;
                    
                    sregionId = 0;
                    txtSregion = "";
                    totSregionCurr = 0;
                    totSregionYear = 0;
                    
                    regionId = 0;
                    totRegionCurr = 0;
                    totRegionYear = 0;
                    
                    totItemYear = processorByItem.getEntry(umn_box, new int[] { itemId }, itemUnitId).Reception;

                    // Item & Unit title:
                    html += "<hr>";
                    html += "<font size='" + FONT_SIZE_ITEM + "'><b>" + SLibUtils.textToHtml(txtItem + " (" + txtItemUnit + ")") + "</b></font><br>";
                }

                if (resultSet.getInt("f_id_sup") != sregionId) {
                    // Change of Supraregion:

                    if (sregionId != SLibConsts.UNDEFINED) {
                        // Region table footer:
                        html +=
                                "<tr>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml("Total región") + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : totRegionYear / totItemYear) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";

                        // Supraregion summary table:
                        html +=
                                "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                                "<font size='" + FONT_SIZE_TBL + "'>" +
                                "<tr>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Total supraregión") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtItemUnit + ")") + "</b></td>" +
                                "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                                "</tr>" +
                                "<tr>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml(txtSregion) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totSregionCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totSregionYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : totSregionYear / totItemYear) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";
                    }

                    sregionId = resultSet.getInt("f_id_sup");
                    txtSregion = SLibUtils.textProperCase(resultSet.getString("f_sup_name"));
                    totSregionCurr = 0;
                    totSregionYear = 0;
                    
                    regionId = SLibConsts.UNDEFINED;
                    totRegionCurr = 0;
                    totRegionYear = 0;

                    // Supraregion title:
                    html += "<font size='" + FONT_SIZE_SREG + "'><b>" + SLibUtils.textToHtml("Supraregión '" + txtSregion + "'") + "</b></font><br>";
                }

                if (resultSet.getInt("f_id_reg") != regionId || resultSet.wasNull()) {
                    // Change of region:

                    if (regionId != SLibConsts.UNDEFINED) {
                        // Region table footer:
                        html +=
                                "<tr>" +
                                "<td align='left'><b>" + SLibUtils.textToHtml("Total región") + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionCurr) + "</b></td>" +
                                "<td align='right'><b>" + SLibUtils.DecimalFormatValue2D.format(totRegionYear) + "</b></td>" +
                                "<td align='right'><b>" + formatPercentage.format(totItemYear == 0 ? 0 : totRegionYear / totItemYear) + "</b></td>" +
                                "</tr>" +
                                "</font>" +
                                "</table>" +
                                "<br>";
                    }

                    regionId = resultSet.getInt("f_id_reg");
                    txtRegion = SLibUtils.textProperCase(resultSet.getString("f_reg_name"));
                    totRegionCurr = 0;
                    totRegionYear = 0;

                    // Region title:
                    html +=
                            "<font size='" + FONT_SIZE_REG + "'><b>" + SLibUtils.textToHtml("Región '" + txtRegion + "'") + "</b></font>" +
                            "<br>";

                    // Region table header:
                    html +=
                            "<table border='1' bordercolor='#000000' width='400' cellpadding='0' cellspacing='0'>" +
                            "<font size='" + FONT_SIZE_TBL + "'>" +
                            "<tr>" +
                            "<td align='center'><b>" + SLibUtils.textToHtml("Proveedor") + "</b></td>" +
                            "<td align='center'><b>" + SLibUtils.textToHtml((isByDate ? "Día" : "Período") + " (" + txtItemUnit + ")") + "</b></td>" +
                            "<td align='center'><b>" + SLibUtils.textToHtml("Temporada (" + txtItemUnit + ")") + "</b></td>" +
                            "<td align='center'><b>" + SLibUtils.textToHtml("%") + "</b></td>" +
                            "</tr>";
                }

                totInputCurr += resultSet.getDouble("f_qty_today");
                //totInputTypeYear += resultSet.getDouble("f_qty_year");    // already known by yearly totals!
                totItemCurr += resultSet.getDouble("f_qty_today");
                //totItemYear += resultSet.getDouble("f_qty_year");         // already known by yearly totals!
                totSregionCurr  += resultSet.getDouble("f_qty_today");
                totSregionYear += resultSet.getDouble("f_qty_year");
                totRegionCurr  += resultSet.getDouble("f_qty_today");
                totRegionYear += resultSet.getDouble("f_qty_year");
                
                // Table detail:
                html +=
                        "<tr>" +
                        "<td align='left'>" + SLibUtils.textToHtml((resultSet.getString("p.name_trd").length() <= 12 ? resultSet.getString("p.name_trd") : resultSet.getString("p.name_trd").substring(0, 11))) + "</td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("f_qty_today")) + "</td>" +
                        "<td align='right'>" + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("f_qty_year")) + "</td>" +
                        "<td align='right'>" + formatPercentage.format(totItemYear == 0 ? 0 : resultSet.getDouble("f_qty_year") / totItemYear) + "</td>" +
                        "</tr>";
            }
            
            umn_box = mailTo.isEmpty() ? umn_box : mailTo;
            int maxId = 0;
            if (html.isEmpty()) {
                String message = "(No se encontró información para el " + (isByDate ? "día" : "período") + " solicitado.)";
                if (!isMailer) {
                    count = noInformationFound(session, dateStart, dateEnd, ticOrig, ticDest, isByDate, subject, message, year, formatPercentage, count, mailTo, mailBcc);
                }
                else {
                    if ((ordinaryPeriod && noReceptionMail.equals("1")) || noReceptionMail.equals("2")) {
                        sql = "SELECT * FROM s_cli_umn_log WHERE b_sent ORDER BY id DESC LIMIT 1;";
                        resultSet = statement.executeQuery(sql);
                        if (resultSet.next()) {
                            long daysLastMailSent = SLibTimeUtils.getDaysDiff(dateStart, resultSet.getDate("ts_exe"));
                            if (daysLastMailSent >= daysToSendMail) {
                                Statement statementAux = session.getDatabase().getConnection().createStatement();
                                ResultSet resultSetAux;
                                sql = "SELECT * FROM s_cli_umn_log WHERE b_data ORDER BY id DESC LIMIT 1;";
                                resultSetAux = statementAux.executeQuery(sql);
                                Date lastReception = null;
                                if (resultSetAux.next()) {
                                    lastReception = resultSetAux.getDate("ts_exe");
                                }
                                Calendar c = Calendar.getInstance();
                                c.setTime(lastReception);
                                String dayOfWeek = getDayOfWeek(c.get(Calendar.DAY_OF_WEEK)); 
                                long daysLastReception = SLibTimeUtils.getDaysDiff(dateStart, lastReception);
                                message = "(" + (daysLastReception == 1 ? "Va 1 día" : "Van " + daysLastReception + " días") + " sin recepciones de semillas, desde el " + dayOfWeek + " " + SLibUtils.DateFormatDate.format(lastReception) + ".)";
                                count = noInformationFound(session, dateStart, dateEnd, ticOrig, ticDest, isByDate, subject, message, year, formatPercentage, count, mailTo, mailBcc);
                                sent = true;
                            }
                        }
                        else {
                            count = noInformationFound(session, dateStart, dateEnd, ticOrig, ticDest, isByDate, subject, message, year, formatPercentage, count, mailTo, mailBcc);
                            sent = true;
                        }
                    }
                    sql = "SELECT MAX(id) FROM s_cli_umn_log";
                    try (ResultSet id = statement.executeQuery(sql)) {
                        if (id.next()) {
                            maxId = id.getInt(1);
                        }
                    }
                    
                    sql = "INSERT INTO s_cli_umn_log (id, b_data, b_sent, ts_exe) VALUES (" + (maxId + 1) + ", false, " + sent + ", NOW());";
                    statement.execute(sql);
                }
            }
            else if (!umn_box.isEmpty()) {
                sendMailReceptions(session, subject, html, umn_box, mailBcc, true,
                        isByDate, dateStart, dateEnd, year, ticOrig, ticDest, formatPercentage,
                        txtSregion, txtItem, txtItemUnit, itemUnitId, txtInput, inputKey, txtInputUnit,
                        totRegionCurr, totRegionYear, totSregionCurr, totSregionYear,
                        totItemCurr, totItemYear, totInputCurr, totInputYear);
                sent = true;
                count++;
                if (isMailer) {
                    sql = "SELECT MAX(id) FROM s_cli_umn_log";
                    try (ResultSet id = statement.executeQuery(sql)) {
                        if (id.next()) {
                            maxId = id.getInt(1);
                        }
                    }
                    sql = "INSERT INTO s_cli_umn_log (id, b_data ,b_sent, ts_exe) VALUES (" + (maxId + 1) + ", true, " + sent + ", NOW());";
                    statement.execute(sql);
                }
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SSomUtils.class.getName(), e);
        }

        return count;
    }
    
    public static String composeSomMailWarning() {
        return "<hr>" +
                "<p>" +
                "<font size='2'>" +
                SLibUtils.textToHtml("Favor de no responder este mail, fue generado de forma automática.") +
                "<br>" +
                SGuiClientApp.APP_NAME + " &copy;" + SGuiClientApp.APP_COPYRIGHT + " " + SGuiClientApp.APP_PROVIDER +
                "<br>" +
                SGuiClientApp.VENDOR_WEBSITE +
                "</font>" +
                "<br>" +
                "<font size='1'>" +
                SGuiClientApp.APP_RELEASE +
                "</font>" +
                "</p>";
    }
    
    public static String composeSomAlternativeMailWarning() {
        return "<hr>" +
                "<p>" +
                "<font size='2'>" +
                SLibUtils.textToHtml("Favor de no responder este mail, fue generado de forma automática.") +
                "<br>" +
                SGuiClientAppAlternative.APP_NAME + " &copy;" + SGuiClientAppAlternative.APP_COPYRIGHT + " " + SGuiClientAppAlternative.APP_PROVIDER +
                "<br>" +
                SGuiClientAppAlternative.VENDOR_WEBSITE +
                "</font>" +
                "<br>" +
                "<font size='1'>" +
                SGuiClientAppAlternative.APP_RELEASE +
                "</font>" +
                "</p>";
    }
    
    private static String getDayOfWeek(int i) {
        String day = "";
        switch (i) {
            case 1 : day = "domingo"; break;
            case 2 : day = "lunes"; break;
            case 3 : day = "martes"; break;
            case 4 : day = "miercoles"; break;
            case 5 : day = "jueves"; break;
            case 6 : day = "viernes"; break;
            case 7 : day = "sabado"; break;
        }
        return day;
    }
}
