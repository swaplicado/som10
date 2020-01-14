/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db.data;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;
import som.mod.SModSysConsts;

/**
 *
 * @author Edwin Carmona
 *
 */
public class SDailyStockUtils {
    
    public static final int AVOCADO_STOCK = 10;
    public static final int ALL_STOCK = 11;
    
    public static final int ESTIMATION = 20;
    public static final int ESTIMATION_ETY = 21;
    
    /**
     * GObtiene los datos de cada hoja por separado, para luego invocar a la función 
     * de escritura del reporte.
     * 
     * @param session
     * @param stockDate fecha de corte para stock
     * 
     * @return true or false, dependiendo si el reporte fue generado o no
     */
    public static boolean generateReport(SGuiSession session, final Date stockDate) {
        try {
            ArrayList<ArrayList> est = SDailyStockUtils.getEstimationByDate(session, stockDate, ESTIMATION);
            ArrayList<ArrayList> estEty = SDailyStockUtils.getEstimationByDate(session, stockDate, ESTIMATION_ETY);
            ArrayList<ArrayList> productionStock = SDailyStockUtils.getStock(session, stockDate, AVOCADO_STOCK);
            ArrayList<ArrayList> resumeStock = SDailyStockUtils.getStockResume(session, stockDate, AVOCADO_STOCK);
            ArrayList<ArrayList> allStock = SDailyStockUtils.getStock(session, stockDate, ALL_STOCK);
            
            File res = SDailyStockReportWriter.writer(est, estEty, productionStock, resumeStock, allStock);
            
            SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "AETHSOM", "som@aeth.mx");
            //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "sflores@swaplicado.com.mx", "Ch3c0m4n", "sflores@swaplicado.com.mx");
            ArrayList<String> mails = new ArrayList();
            mails.add("edwin.carmona@swaplicado.com.mx");
            
            SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8("Inventario diario"), SDailyStockReportWriter.createMailBody(est, estEty, productionStock, resumeStock, allStock), mails);
            mail.getAttachments().add(res);
            mail.getBccRecipients().addAll(mails);
            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
            mail.send();
        }
        catch (SQLException | IOException ex) {
            Logger.getLogger(SDailyStockUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (MessagingException ex) {
            Logger.getLogger(SDailyStockUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
    /**
     * Obtiene la estimación por línea de producción, basada en la fecha de la toma física
     * 
     * @param session
     * @param stockDate fecha de la toma física
     * @param mode ESTIMATION, ESTIMATION_ETY
     * 
     * @return una lista con los renglones que a su vez contienen una lista con las columnas
     * @throws SQLException 
     */
    private static ArrayList<ArrayList> getEstimationByDate(SGuiSession session, final Date stockDate, final int mode) throws SQLException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        String sEstimationQuery = "SELECT  " +
                "    pl.code, " +
                "    pl.name, " +
                "    wah.code AS w_code, " +
                "    wah.name AS w_name, " +
                "    SUM(mee.mfg_fg) AS tot_aceite, " +
                "    SUM(mee.mfg_bp) AS tot_repro, " +
                "    SUM(mee.mfg_cu) AS tot_deshecho, " +
                "    SUM(mee.con_rm) AS tot_mp, " +
                "    un.code AS u_code," +
                "    me.dt_mfg_est, " +
                "    me.dt_stk_day " +
                "FROM " +
                "    s_mfg_est AS me " +
                "        INNER JOIN " +
                "    s_mfg_est_ety AS mee ON me.id_mfg_est = mee.id_mfg_est " +
                "        INNER JOIN " +
                "    cu_wah AS wah ON mee.fk_wah_co = wah.id_co " +
                "        AND mee.fk_wah_cob = wah.id_cob " +
                "        AND mee.fk_wah_wah = wah.id_wah " +
                "        INNER JOIN " +
                "    cu_line AS pl ON wah.fk_line = pl.id_line " +
                "        INNER JOIN " +
                "    su_unit AS un ON me.fk_unit = un.id_unit " +
                "WHERE " +
                "    me.b_del = 0 " +
                "        AND me.dt_stk_day = '" + format.format(stockDate) + "' "  +
                (mode == ESTIMATION ? "GROUP BY pl.id_line " : "GROUP BY mee.id_mfg_est, mee.id_ety ") +
                "ORDER BY me.dt_mfg_est , me.id_mfg_est , mee.id_ety;";
        
        ArrayList<ArrayList> prodByLines = new ArrayList<>();
        
        ResultSet prodByLineRes = session.getStatement().executeQuery(sEstimationQuery);
        while(prodByLineRes.next()) {
            ArrayList<Object> row = new ArrayList<>();
            
            row.add(prodByLineRes.getString("dt_mfg_est"));
            row.add(prodByLineRes.getString("dt_stk_day"));
            if (mode == ESTIMATION_ETY) {
                row.add(prodByLineRes.getString("w_code"));
                row.add(prodByLineRes.getString("w_name"));
            }
            row.add(prodByLineRes.getString("pl.code"));
            row.add(prodByLineRes.getString("pl.name"));
            row.add(prodByLineRes.getDouble("tot_aceite"));
            row.add(prodByLineRes.getString("u_code"));
//            row.add(prodByLineRes.getDouble("tot_repro"));
//            row.add(prodByLineRes.getDouble("tot_deshecho"));
//            row.add(prodByLineRes.getDouble("tot_mp"));
            
            prodByLines.add(row);
        }
        
        return prodByLines;
    }
    
    /**
     * Obtiene el stock 
     * 
     * @param session
     * @param stockDate
     * @param stkMode
     * 
     * @return una lista con los renglones que a su vez contienen una lista con las columnas
     * @throws SQLException 
     */
    private static ArrayList<ArrayList> getStock(SGuiSession session, final Date stockDate, final int stkMode) throws SQLException {
        ResultSet stockResult = SDailyStockUtils.getStkResult(session, stockDate, stkMode);
        ArrayList<ArrayList> stock = new ArrayList<>();
        
        while(stockResult.next()) {
            ArrayList<Object> row = new ArrayList<>();

            row.add(stockResult.getString("w_code"));
//            row.add(stockResult.getString("w_name"));
            
//            row.add(stockResult.getString("i_code"));
            row.add(stockResult.getString("i_name"));
            
//            row.add(stockResult.getDouble("f_mov_i"));
//            row.add(stockResult.getDouble("f_mov_o"));
            row.add(stockResult.getDouble("f_stk"));
            
            if (stkMode == AVOCADO_STOCK) {
                row.add(stockResult.getDouble("stk_capacity"));
                row.add(stockResult.getDouble("stk_remains"));
                row.add(stockResult.getDouble("stk_available"));
            }
            
            row.add(stockResult.getString("u_code"));
            
            if (stkMode == AVOCADO_STOCK) {
                row.add(stockResult.getString("note"));
                row.add(stockResult.getDouble("acidity"));

                row.add(stockResult.getDouble("height_cm"));
            }
            
            stock.add(row);
        }
        
        return stock;
    }
    
    /**
     * Obtiene el objeto ResultSet de la query
     * 
     * @param session
     * @param stockDate
     * @param stkMode AVOCADO_STOCK, ALL_STOCK
     * 
     * @return ResultSet con el resultado de la query de stock
     * 
     * @throws SQLException 
     */
    private static ResultSet getStkResult(SGuiSession session, final Date stockDate, final int stkMode) throws SQLException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(stockDate);
        int year = calendar.get(Calendar.YEAR);
        
        String sStockQuery = "SELECT   " +
        "    vi.id_item,  " +
        "    vi.code AS i_code,  " +
        "    vi.name AS i_name,  " +
        "    vc.code,  " +
        "    vw.name AS w_name,  " +
        "    vw.code AS w_code,  " +
        "    SUM(v.mov_in) AS f_mov_i,  " +
        "    SUM(v.mov_out) AS f_mov_o,  " +
        "    SUM(v.mov_in - v.mov_out) AS f_stk,  " +
        "    vu.code AS u_code,  " +
        "    (vw.dim_heig * 100) AS height_cm,  " +
        "    vw.cap_real_lt*(vi.den) AS stk_capacity,  " +
        "    ((vw.cap_real_lt*(vi.den)) - SUM(v.mov_in - v.mov_out)) AS stk_available,  " +
        "    vw.acidity,  " +
        "    vw.note," +
        "    (SELECT  " +
                "    COALESCE(SUM(s.mov_in - s.mov_out), 0) " +
                "FROM " +
                "    s_stk AS s " +
                "        INNER JOIN " +
                "    su_item AS si ON s.id_item = si.id_item " +
                "WHERE " +
                "    s.b_del = 0 AND s.id_year = 2019 " +
                "        AND s.dt <= '2019-12-31' " +
                "        AND si.fk_item_tp = 5 " +
                "        AND s.id_co = v.id_co" +
                "        AND s.id_cob = v.id_cob" +
                "        AND s.id_wah = v.id_wah" +
                " GROUP BY s.id_item , s.id_unit, s.id_cob, s.id_wah) AS stk_remains" +
        " FROM  " +
        "    s_stk AS v  " +
        "        INNER JOIN  " +
        "    su_item AS vi ON v.id_item = vi.id_item  " +
        "        INNER JOIN  " +
        "    su_unit AS vu ON v.id_unit = vu.id_unit  " +
        "        INNER JOIN  " +
        "    cu_wah AS vw ON v.id_co = vw.id_co  " +
        "        AND v.id_cob = vw.id_cob  " +
        "        AND v.id_wah = vw.id_wah  " +
        "        INNER JOIN  " +
        "    cu_cob AS vc ON v.id_co = vc.id_co  " +
        "        AND v.id_cob = vc.id_cob  " +
        "WHERE  " +
        "    v.b_del = 0 AND v.id_year = " + year +" AND vi.fk_item_tp <> 5 " +
        "    AND v.dt <= '" + format.format(stockDate) + "' "  +
        (stkMode == AVOCADO_STOCK ? "AND ((vw.fk_wah_tp = " + SModSysConsts.CS_WAH_TP_TAN_MFG + " AND vw.fk_line = 4)" +
                "        OR (vw.fk_wah_tp = " + SModSysConsts.CS_WAH_TP_TAN + "" +
                "        AND vi.id_item IN (SELECT " +
                "            id_item" +
                "        FROM" +
                "            som_com.su_item" +
                "        WHERE" +
                "            fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG +
                "                AND name LIKE '%AGUACATE%'))) " : "") +
        "GROUP BY v.id_item , v.id_unit , v.id_cob , v.id_wah , vc.code , vw.name , vi.code , vi.name , vu.code  " +
        (stkMode == AVOCADO_STOCK ? "HAVING f_stk <> 0  " : "HAVING f_stk > 0 ") +
        "ORDER BY vi.name , vi.code , v.id_item , vu.code , v.id_unit , vc.code , vw.name , v.id_cob , v.id_wah;";
        
        return session.getStatement().executeQuery(sStockQuery);
    }
    
    /**
     * 
     * @param session
     * @param stockDate
     * @param stkMode
     * @return
     * @throws SQLException 
     */
    private static ArrayList<ArrayList> getStockResume(SGuiSession session, final Date stockDate, final int stkMode) throws SQLException {
        ResultSet stockResult = SDailyStockUtils.getStkResult(session, stockDate, stkMode);
        HashMap<Integer, ArrayList> mRows = new HashMap<>();
        ArrayList<Object> columns;
        
        int item = 0;
        while(stockResult.next()) {
            item = stockResult.getInt("id_item");
            
            if (mRows.containsKey(item)) {
                double stk = ((Double) mRows.get(item).get(1)) + stockResult.getDouble("f_stk");
                mRows.get(item).set(1, stk);
                
                double acidity = ((Double) mRows.get(item).get(2)) + (stockResult.getDouble("acidity") * stockResult.getDouble("f_stk"));
                mRows.get(item).set(2, acidity);
                
                double remains = ((Double) mRows.get(item).get(3)) + (stockResult.getDouble("stk_remains"));
                mRows.get(item).set(3, remains);
            }
            else {
                columns = new ArrayList<>();
                
                columns.add(stockResult.getString("i_name"));
                columns.add(stockResult.getDouble("f_stk"));
                columns.add(stockResult.getDouble("acidity") * stockResult.getDouble("f_stk"));
                columns.add(stockResult.getDouble("stk_remains"));
                
                mRows.put(item, columns);
            }
        }
        
        double remains = 0d;
        ArrayList<ArrayList> stock = new ArrayList<>();
        for (ArrayList value : mRows.values()) {
            value.set(2, (((Double) value.get(2)) / ((Double) value.get(1)))); // suma de acidez / existencia por ítem
            remains += (Double) value.get(3);
            value.remove(3);
            stock.add(value);
        }
        
        ArrayList<Object> blank = new ArrayList<>();
        blank.add("");
        blank.add("");
        blank.add("");
        stock.add(blank);
        
        ArrayList<Object> remainss = new ArrayList<>();
        remainss.add("BORRAS Y RESTOS (VARIOS TQ's)");
        remainss.add(remains);
        remainss.add("POR REPROCESAR");
        
        stock.add(remainss);
        
        return stock;
    }
    
    
}
