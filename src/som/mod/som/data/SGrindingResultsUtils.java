/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

import com.aspose.cells.ImageOrPrintOptions;
import com.aspose.cells.ImageType;
import com.aspose.cells.SheetRender;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JFileChooser;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.cfg.db.SDbCompany;
import som.mod.som.db.SDbGrindingReportItemGroup;
import som.mod.som.db.SDbGrinding;
import som.mod.som.db.SDbGrindingResult;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbProcessingBatch;

/**
 *
 * @author Edwin Carmona
 */
public class SGrindingResultsUtils {
    
    public final static int FIRST = 1;
    public final static int PREV = 2;
    public final static int NEXT = 3;
    public final static int LAST = 4;
    
    public final static int RESID_PASTA = 7;
    public final static int CONT_ACEITE_SEM = 11;
    public final static int RESID_PRENSA = 2;
    public final static int PROTEINA = 8;
    
    public final static int ITEM = 1;
    public final static int LOT = 2;
    
    public final static int GRINDING_BASCULE = 1;
    public final static int GRINDING_OIL_PERCENT = 2;
    
    /**
     * 
     * @param client
     * @param option puede ser:
     *                 SGrindingResultsUtils.ITEM
     *                 SGrindingResultsUtils.LOT
     * @param plantKey
     * 
     * @return int id del último ítem o lote que se haya capturado
     */
    public static int getLastConfiguration(SGuiClient client, final int option, final int[] plantKey) {
        String field = "";
        
        String sql2 = "";
        if (option == ITEM) {
            field = "fk_item_id";
            
            sql2 = "SELECT " + field + " AS l_conf FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_LINK_ITEM_PARAM) + 
                            " WHERE b_del = 0 ORDER BY fk_usr_ins DESC;";
        }
        else {
            field = "fk_prc_batch";
            
            sql2 = "SELECT id_prc_batch AS l_conf FROM " + SModConsts.TablesMap.get(SModConsts.S_PRC_BATCH) + 
                            " WHERE b_del = 0 ORDER BY fk_usr_ins DESC;";
        }
        
        String sql = "SELECT " + field + " FROM " + SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT) + 
                        " WHERE b_del = 0 ORDER BY id_result DESC;";
        
        
        ResultSet itemIdRes;
        ResultSet lastConfRes;
        try {
            itemIdRes = client.getSession().getStatement().executeQuery(sql);
            
            if (itemIdRes.next()) {
                return itemIdRes.getInt(field);
            }
            
            lastConfRes = client.getSession().getStatement().executeQuery(sql2);
            
            if (lastConfRes.next()) {
                return lastConfRes.getInt("l_conf");
            }
            
            return 0;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    /**
     * Obtiene el último id lote capturado en base al ítem recibido.
     * 
     * @param client
     * @param item
     * @param plantKey
     * 
     * @return int id del lote
     */
    public static int getLastLotByItem(SGuiClient client, final int item, final int[] plantKey) {
        if (item == 0) {
            return 0;
        }
        
        String sql = "SELECT fk_prc_batch FROM " + SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT) + 
                        " WHERE b_del = 0 AND fk_item_id = " + item + " " +
                        " AND fk_pla_co = " + plantKey[0] + " AND fk_pla_cob = " + plantKey[1] + " AND fk_pla_pla = " + plantKey[2] + " " +
                        " ORDER BY id_result DESC;";
        
        String sqlLots = "SELECT id_prc_batch FROM " + SModConsts.TablesMap.get(SModConsts.S_PRC_BATCH) + 
                        " WHERE b_del = 0 AND fk_item = " + item + " ORDER BY fk_usr_ins DESC;";
        
        ResultSet itemIdRes;
        ResultSet lotIdRes;
        try {
            itemIdRes = client.getSession().getStatement().executeQuery(sql);
            
            while(itemIdRes.next()) {
                return itemIdRes.getInt("fk_prc_batch");
            }
            
            lotIdRes = client.getSession().getStatement().executeQuery(sqlLots);
            
            while(lotIdRes.next()) {
                return lotIdRes.getInt("id_prc_batch");
            }
            
            return 0;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    /**
     * Obtiene el último id lote capturado en base al ítem y la fecha recibidas.
     * 
     * @param client
     * @param item
     * @param dtDate
     * 
     * @return int id del lote
     */
    public static int getLotByItemAndDate(SGuiClient client, final int item, final Date dtDate) {
        if (item == 0) {
            return 0;
        }

        String sql = "SELECT fk_prc_batch FROM " + SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT)
                + " WHERE b_del = 0 AND fk_item_id = " + item + ""
                + " AND dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "' ORDER BY id_result DESC;";

        String sqlLots = "SELECT id_prc_batch FROM " + SModConsts.TablesMap.get(SModConsts.S_PRC_BATCH)
                + " WHERE b_del = 0 AND fk_item = " + item + " ORDER BY fk_usr_ins DESC;";

        ResultSet itemIdRes;
        ResultSet lotIdRes;
        try {
            itemIdRes = client.getSession().getStatement().executeQuery(sql);

            while (itemIdRes.next()) {
                return itemIdRes.getInt("fk_prc_batch");
            }

            lotIdRes = client.getSession().getStatement().executeQuery(sqlLots);

            while (lotIdRes.next()) {
                return lotIdRes.getInt("id_prc_batch");
            }

            return 0;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    /**
     * 
     * 
     * @param client
     * @param item
     * @param plantKey
     * 
     * @return 
     */
    public static ArrayList<SDbGrindingReportItemGroup> getGroupOfGrindingItem(SGuiClient client, final int item, final int[] plantKey) {
        if (item == 0) {
            return new ArrayList<>();
        }

        String sql = "SELECT "
                + "fk_rep_group "
                + "FROM "
                + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_REP_ITEM_GROUP) + " "
                + "WHERE "
                + "fk_item = " + item + " "
                + " AND fk_pla_co = " + plantKey[0] + " AND fk_pla_cob = " + plantKey[1] + " AND fk_pla_pla = " + plantKey[2] + " "
                + " LIMIT 1;";

        ResultSet itemIdRes;
        ResultSet linkIdsRes;
        try {
            itemIdRes = client.getSession().getStatement().executeQuery(sql);

            int group = 0;
            if (itemIdRes.next()) {
                group = itemIdRes.getInt("fk_rep_group");
            }

            if (group == 0) {
                return new ArrayList<>();
            }

            String sqlGroup = "SELECT "
                    + "id_item_group "
                    + "FROM "
                    + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_REP_ITEM_GROUP) + " "
                    + "WHERE "
                    + "fk_rep_group = " + group + " "
                    + "ORDER BY item_sort ASC;";

            ArrayList<SDbGrindingReportItemGroup> links = new ArrayList<>();
            linkIdsRes = client.getSession().getStatement().getConnection().createStatement().executeQuery(sqlGroup);

            SDbGrindingReportItemGroup itemGrp = null;
            while (linkIdsRes.next()) {
                itemGrp = new SDbGrindingReportItemGroup();
                itemGrp.read(client.getSession(), new int[] { linkIdsRes.getInt("id_item_group") });
                links.add(itemGrp);
            }

            return links;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }
    
    /**
     * genera los resultados para el día en cuestión del ítem y lote recibidos.
     * Esto lo hace basándose en la configuración de la tabla links_item_parameter
     * 
     * @param client
     * @param bAllItems
     * @param nItemId
     * @param nLotId
     * @param tCaptureDate
     * @param maPlantPk
     */
    public static void generateResults(SGuiClient client, final boolean bAllItems, final int nItemId, final int nLotId, final Date tCaptureDate, final int[] maPlantPk) {
        String sql = "SELECT  " +
                    "id_link, " +
                    "capture_order, " +
                    "i.code AS item_code, " +
                    "i.name AS item_name, " +
                    "lip.fk_item_id, " +
                    (bAllItems ? ("COALESCE((SELECT  " +
                    "        id_prc_batch " +
                    "     FROM " +
                    "        " + SModConsts.TablesMap.get(SModConsts.S_PRC_BATCH) + " " +
                    "     WHERE " +
                    "        fk_item = lip.fk_item_id " +
                    "        AND b_del = 0 " +
                    "        ORDER BY id_prc_batch DESC LIMIT 1), " +
                    "        0) AS lot_id, ") : "") +
                    "lip.fk_param_id " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_LINK_ITEM_PARAM) + " lip " +
                    "INNER JOIN " +
                    "su_item i ON lip.fk_item_id = i.id_item " +
                    "WHERE " +
                    "lip.b_del = 0 AND lip.fk_pla_co = " + maPlantPk[0] + " AND lip.fk_pla_cob = " + maPlantPk[1] + " AND lip.fk_pla_pla = " + maPlantPk[2] + " " +
                    (! bAllItems ? (" AND lip.fk_item_id = ") + nItemId : "") + " " +
                    "ORDER BY lip.fk_item_id ASC, lip.fk_param_id ASC;";
        
        ResultSet links;
        ArrayList<SDbGrindingResult> resultsToCapture = new ArrayList<>();
        
        try {
            links = client.getSession().getStatement().executeQuery(sql);

            while(links.next()) {
                SDbGrindingResult res = new SDbGrindingResult();

                res.setFkItemId(links.getInt("fk_item_id"));
                res.setFkParameterId(links.getInt("fk_param_id"));
                res.setFkLotId(bAllItems ? links.getInt("lot_id") : nLotId);
                res.setOrder(links.getInt("capture_order"));
                res.setDateCapture(tCaptureDate);
                res.setFklinkId_n(links.getInt("id_link"));
                res.setFkPlantCompanyId(maPlantPk[0]);
                res.setFkPlantBranchId(maPlantPk[1]);
                res.setFkPlantPlantId(maPlantPk[2]);
                
                if (res.getFkLotId() == 0) {
                    client.showMsgBoxError("El ítem " + links.getString("item_code") + 
                                            " - " + links.getString("item_name") + 
                                            " no tiene lotes asignados.");
                    return;
                }
                
                if (SGrindingResultsUtils.resultExists(client, res.getFkItemId(), 
                        res.getFkParameterId(), res.getFkLotId(), res.getDateCapture(), maPlantPk) > 0) {
                    continue;
                }

                resultsToCapture.add(res);
            }

            for (SDbGrindingResult dbResult : resultsToCapture) {
                dbResult.save(client.getSession());
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param client
     * @param option
     * @param dtDate
     * @param nItemId
     * @param nLotId
     * @return 
     */
    public static Date getDateOfResults(SGuiClient client, final int option, final Date dtDate, final int nItemId, final int nLotId) {
        String sFill = "";
        
        switch (option) {
            case FIRST:
                sFill = "ORDER BY dt_capture ASC LIMIT 1;";
                break;
            case PREV:
                sFill = "AND dt_capture < '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "' " +
                        "ORDER BY dt_capture DESC LIMIT 1;";
                break;
            case NEXT:
                sFill = "AND dt_capture > '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "' " +
                        "ORDER BY dt_capture ASC LIMIT 1;";
                break;
            case LAST:
                sFill = "ORDER BY dt_capture DESC LIMIT 1;";
                break;
        }
        
        String sql = "SELECT dt_capture FROM " + 
                    SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT) + 
                    " WHERE fk_item_id = " + nItemId +
                    " AND fk_prc_batch = " + nLotId +
                    " AND MONTH(dt_capture) = MONTH('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                    " AND b_del = 0 ";
        sql += sFill;
        
        ResultSet resIdResult;
        
        try {
            
            resIdResult = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            if (resIdResult.next()) {
                return resIdResult.getDate("dt_capture");
            }
            
            return dtDate;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            return dtDate;
        }
    }
    
    /**
     * 
     * @param client
     * @param itm
     * @param param
     * @param lot
     * @param dtCapture
     * @return 
     */
    private static int resultExists(SGuiClient client, final int itm, final int param, final int lot, final Date dtCapture, final int[] maPlantPk) {
        String sql = "SELECT id_result FROM " + 
                        SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT) + 
                    " WHERE fk_item_id = " + itm + 
                    " AND fk_param_id = " + param + 
                    " AND fk_prc_batch = " + lot +
                    " AND dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(dtCapture) + "' "+
                    " AND b_del = 0 AND fk_pla_co = " + maPlantPk[0] + " " +
                    " AND fk_pla_cob = " + maPlantPk[1] + " " +
                    " AND fk_pla_pla = " + maPlantPk[2] + " ORDER BY id_result DESC;";
        
        ResultSet resIdResult;
        
        try {
            
            resIdResult = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            if (resIdResult.next()) {
                return resIdResult.getInt("id_result");
            }
            
            return 0;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    /**
     * Obtiene el promedio general de un parámetro e ítem en específico por mes, podría
     * ser considerado como un promedio de promedios.
     * No toma en cuenta los ceros.
     * 
     * @param client
     * @param dtDate
     * @param item
     * @param parameter
     * @param aPlantKey
     * 
     * @return 
     */
    public static double getMonthAverage(SGuiClient client, final Date dtDate, final int item, final int parameter, final int[] aPlantKey) {
        
        String sql = "SELECT " +
                    "result_08," +
                    "result_10," +
                    "result_12," +
                    "result_14," +
                    "result_16," +
                    "result_18," +
                    "result_20," +
                    "result_22," +
                    "result_00," +
                    "result_02," +
                    "result_04," +
                    "result_06 " +
                    "FROM " +
                    SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT) + " " +
                    "WHERE " +
                    " MONTH(dt_capture) = MONTH('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                    " AND YEAR(dt_capture) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                    " AND fk_param_id = " + parameter + " " +
                    " AND fk_item_id = " + item + " " +
                    " AND fk_pla_co = " + aPlantKey[0] + " AND fk_pla_cob = " + aPlantKey[1] + " AND fk_pla_pla = " + aPlantKey[2] + " " +
                    " AND NOT b_del " +
                    "GROUP BY dt_capture";
        
        ResultSet resIdResult;
        
        try {
            resIdResult = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            double avgSum;
            double avg;
            
            double d8;
            String s8;
            double d10;
            String s10;
            double d12;
            String s12;
            double d14;
            String s14;
            double d16;
            String s16;
            double d18;
            String s18;
            double d20;
            String s20;
            double d22;
            String s22;
            double d0;
            String s0;
            double d2;
            String s2;
            double d4;
            String s4;
            double d6;
            String s6;
            
            double avgSumTot = 0;
            int avgNumTot = 0;
            
            while (resIdResult.next()) {
                int avgNum = 0;
                avgSum = 0d;
                avg = 0d;
                
                s8 = resIdResult.getString("result_08");
                s10 = resIdResult.getString("result_10");
                s12 = resIdResult.getString("result_12");
                s14 = resIdResult.getString("result_14");
                s16 = resIdResult.getString("result_16");
                s18 = resIdResult.getString("result_18");
                s20 = resIdResult.getString("result_20");
                s22 = resIdResult.getString("result_22");
                s0 = resIdResult.getString("result_00");
                s2 = resIdResult.getString("result_02");
                s4 = resIdResult.getString("result_04");
                s6 = resIdResult.getString("result_06");
                
                d8 = SGrindingReport.isNumeric(s8.replace(",", "")) ? Double.parseDouble(s8) : 0;
                d10 = SGrindingReport.isNumeric(s10.replace(",", "")) ? Double.parseDouble(s10) : 0;
                d12 = SGrindingReport.isNumeric(s12.replace(",", "")) ? Double.parseDouble(s12) : 0;
                d14 = SGrindingReport.isNumeric(s14.replace(",", "")) ? Double.parseDouble(s14) : 0;
                d16 = SGrindingReport.isNumeric(s16.replace(",", "")) ? Double.parseDouble(s16) : 0;
                d18 = SGrindingReport.isNumeric(s18.replace(",", "")) ? Double.parseDouble(s18) : 0;
                d20 = SGrindingReport.isNumeric(s20.replace(",", "")) ? Double.parseDouble(s20) : 0;
                d22 = SGrindingReport.isNumeric(s22.replace(",", "")) ? Double.parseDouble(s22) : 0;
                d0 = SGrindingReport.isNumeric(s0.replace(",", "")) ? Double.parseDouble(s0) : 0;
                d2 = SGrindingReport.isNumeric(s2.replace(",", "")) ? Double.parseDouble(s2) : 0;
                d4 = SGrindingReport.isNumeric(s4.replace(",", "")) ? Double.parseDouble(s4) : 0;
                d6 = SGrindingReport.isNumeric(s6.replace(",", "")) ? Double.parseDouble(s6) : 0;
                
                if (d8 != 0) {
                    avgSum += d8;
                    avgNum++;
                }
                if (d10 != 0) {
                    avgSum += d10;
                    avgNum++;
                }
                if (d12 != 0) {
                    avgSum += d12;
                    avgNum++;
                }
                if (d14 != 0) {
                    avgSum += d14;
                    avgNum++;
                }
                if (d16 != 0) {
                    avgSum += d16;
                    avgNum++;
                }
                if (d18 != 0) {
                    avgSum += d18;
                    avgNum++;
                }
                if (d20 != 0) {
                    avgSum += d20;
                    avgNum++;
                }
                if (d22 != 0) {
                    avgSum += d22;
                    avgNum++;
                }
                if (d0 != 0) {
                    avgSum += d0;
                    avgNum++;
                }
                if (d2 != 0) {
                    avgSum += d2;
                    avgNum++;
                }
                if (d4 != 0) {
                    avgSum += d4;
                    avgNum++;
                }
                if (d6 != 0) {
                    avgSum += d6;
                    avgNum++;
                }
                
                avg = avgNum == 0d ? 0d : avgSum/avgNum;
                
                avgSumTot += avg;
                avgNumTot += avg == 0d ? 0 : 1;
            }
            
            return avgNumTot == 0d ? 0d : avgSumTot / avgNumTot;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            return 0d;
        }
    }
    
    /**
     * Obtiene el promedio ponderado de un día o de todo el mes dependiendo 
     * del parámetro onlyCurrentDay.
     * Funciona solamente para un parámetro de molienda y un ítem a la vez.
     * 
     * @param client
     * @param dtDate fecha de captura
     * @param monthGrinding molienda del mes (total a considerar para el promedio ponderado)
     * @param nItemId id del item
     * @param parameterId id del parámetro de molienda
     * @param onlyCurrentDay si es verdadero retorna solamente el promedio ponderado del día recibido.
     * @param isWeightedAverage verdadero si se requiere el promedio ponderado, si es falso regresa
     *                          el promedio normal
     * @param plantKey
     * 
     * @return double
     */
    public static double getWeightedAverage(SGuiClient client, final Date dtDate, 
                                    final double monthGrinding, final int nItemId, 
                                    final int parameterId, final boolean onlyCurrentDay,
                                    final boolean isWeightedAverage,
                                    final int[] plantKey) {
        String sql = "SELECT @prom := COALESCE("
                + "(IF(COALESCE(result_08, 0) > 0, result_08, 0) +"
                + "IF(COALESCE(result_10, 0) > 0, result_10, 0) + "
                + "IF(COALESCE(result_12, 0) > 0, result_12, 0) + "
                + "IF(COALESCE(result_14, 0) > 0, result_14, 0) + "
                + "IF(COALESCE(result_16, 0) > 0, result_16, 0) + "
                + "IF(COALESCE(result_18, 0) > 0, result_18, 0) + "
                + "IF(COALESCE(result_20, 0) > 0, result_20, 0) + "
                + "IF(COALESCE(result_22, 0) > 0, result_22, 0) + "
                + "IF(COALESCE(result_00, 0) > 0, result_00, 0) + "
                + "IF(COALESCE(result_02, 0) > 0, result_02, 0) + "
                + "IF(COALESCE(result_04, 0) > 0, result_04, 0) + "
                + "IF(COALESCE(result_06, 0) > 0, result_06, 0)) / "
                + "(IF(result_08 > 0, 1, 0) + "
                + "IF(result_10 > 0, 1, 0) + "
                + "IF(result_12 > 0, 1, 0) + "
                + "IF(result_14 > 0, 1, 0) + "
                + "IF(result_16 > 0, 1, 0) + "
                + "IF(result_18 > 0, 1, 0) + "
                + "IF(result_20 > 0, 1, 0) + "
                + "IF(result_22 > 0, 1, 0) + "
                + "IF(result_00 > 0, 1, 0) + "
                + "IF(result_02 > 0, 1, 0) + "
                + "IF(result_04 > 0, 1, 0) + "
                + "IF(result_06 > 0, 1, 0)), 0) AS promedio, " +
                    "	" + (!onlyCurrentDay ? "SUM(" : "") + "COALESCE(@prom / " + monthGrinding + 
                    " * COALESCE((SELECT grinding_oil_perc FROM " + SModConsts.TablesMap.get(SModConsts.S_GRINDING) + " WHERE dt_capture = sgr.dt_capture), 0),0)" + (!onlyCurrentDay ? ")" : "") + " AS ponderado " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_GRINDING_RESULT) + " sgr " +
                    "WHERE fk_param_id = " + parameterId + " " +
                    "AND fk_item_id = " + nItemId + " " +
                    "AND fk_pla_co = " + plantKey[0] + " AND fk_pla_cob = " + plantKey[1] + " AND fk_pla_pla = " + plantKey[2] + " " +
                    "AND MONTH(dt_capture) = MONTH('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                    (onlyCurrentDay ? "AND dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "' " : "") + 
                    ";";
        
        ResultSet resIdResult;
        
        try {
            
            resIdResult = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            if (resIdResult.next()) {
                return isWeightedAverage ? resIdResult.getDouble("ponderado") : resIdResult.getDouble("promedio");
            }
            
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0d;
    }
    
    /**
     * Guarda el objeto y retorna este mismo con el id correspondiente una vez guardado.
     * 
     * @param client
     * @param grindingResult SDbGrinding a guardar
     * 
     * @return SDbGrinding guardado
     */
    public static SDbGrinding saveGrinding(SGuiClient client, SDbGrinding grindingResult) {
        try {
            grindingResult.save(client.getSession());
            
            return SGrindingResultsUtils.getGrindingByDate(client, grindingResult.getDateCapture());
        }
        catch (Exception ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * obtiene un objeto SDbGrinding basado en su fecha de captura en caso de no
     * ser encontrado devuelve un objeto nuevo
     * 
     * @param client
     * @param dtDate fecha de captura del registro
     * 
     * @return SDbGrinding object
     */
    public static SDbGrinding getGrindingByDate(SGuiClient client, Date dtDate) {
        SDbGrinding grindingResult = new SDbGrinding();
        
        String sql = "SELECT id_grinding FROM " + 
                    SModConsts.TablesMap.get(SModConsts.S_GRINDING) + 
                    " WHERE dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "' " +
                    " AND NOT b_del " +
                    " ORDER BY id_grinding DESC;";
        
        ResultSet resIdResult;
        int idGrinding = 0;
        
        try {
            resIdResult = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            if (resIdResult.next()) {
                idGrinding = resIdResult.getInt("id_grinding");
                
                grindingResult.read(client.getSession(), new int[] { idGrinding });
                
                return grindingResult;
            }
            
            return new SDbGrinding();
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new SDbGrinding();
    }
    
    /**
     * Obtiene la molienda por báscula y por porcentaje de aceite por mes o de un mes
     * hacia atrás de un ítem específico, retorna un ArrayList de objetos SGrindingData
     * 
     * @param client
     * @param dtDate esta fecha solo se utiliza para obtener el mes
     * @param nItemId id del ítem del cual se quiere obtener la molienda
     * @param onlyCurrentMonth si este parámetro es verdadero devuelve solo el mes actual,
     *                      si es falso devuelve el mes actual y los anteriores (solo el presente año) 
     * @param aPlantKey llave foránea de la planta
     * 
     * @return ArrayList[SGrindingData] 
     */
    public static ArrayList<SGrindingData> getGrindingByMonth(SGuiClient client, Date dtDate, final int nItemId, final boolean onlyCurrentMonth, final int[] aPlantKey) {
        ArrayList<SGrindingData> grindings = new ArrayList<>();
        
        String sql = "SELECT " +
                        "SUM(grinding_bascule) AS sum_gr_basc, " +
                        "SUM(grinding_oil_perc) AS sum_gr_op, " +
                        "MONTH(dt_capture) AS g_month " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.S_GRINDING) + " " +
                        "WHERE " +
                        "NOT b_del " +
                        "AND MONTH(dt_capture) " + (onlyCurrentMonth ? "=" : "<=" ) +  " MONTH('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                        "AND YEAR(dt_capture) = " + " YEAR('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                        "AND fk_item_id = " + nItemId + " " +
                        "AND fk_pla_co = " + aPlantKey[0] + " AND fk_pla_cob = " + aPlantKey[1] + " AND fk_pla_pla = " + aPlantKey[2] + " " +
                        "GROUP BY g_month;";
        
        ResultSet resGrindings;
        try {
            resGrindings = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            SGrindingData oGrinding;
            while (resGrindings.next()) {
                oGrinding = new SGrindingData();
                
                oGrinding.setMonth(resGrindings.getInt("g_month"));
                oGrinding.setSumGrindingBascule(resGrindings.getDouble("sum_gr_basc"));
                oGrinding.setSumGrindingOilPercent(resGrindings.getDouble("sum_gr_op"));
                
                Month month = Month.of(oGrinding.getMonth());
                oGrinding.setMonthText((month.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))).toUpperCase());
                
                grindings.add(oGrinding);
            }
            
            return grindings;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Obtiene el campo de configuración contenido en el campo de la relación de parámetro vs ítem
     * 
     * @param client
     * @param nItemId
     * @param aPlantKey
     * 
     * @return SCaptureConfiguration
     */
    public static SCaptureConfiguration getCfgField(SGuiClient client, final int nItemId, final int[] aPlantKey) {
        String query = "SELECT " +
                        "    capture_cfg " +
                        "FROM " +
                        "    " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_LINK_ITEM_PARAM) + " " +
                        "WHERE " +
                        " fk_item_id = " + nItemId + " AND NOT b_del " +
                        " AND fk_pla_co = " + aPlantKey[0] + " AND fk_pla_cob = " + aPlantKey[1] + " AND fk_pla_pla = " + aPlantKey[2] + " " +
                        "ORDER BY capture_order ASC " +
                        "LIMIT 1;";
        
        ResultSet resCfg;
        ObjectMapper mapper = new ObjectMapper();
        try {
            resCfg = client.getSession().getStatement().getConnection().createStatement().executeQuery(query);
            
            if (resCfg.next()) {
                SCaptureConfiguration cfg = mapper.readValue(resCfg.getString("capture_cfg"), SCaptureConfiguration.class);
                
                return cfg;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (JsonProcessingException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static String getFormula(String sFormula, int row) {
        int startPos = sFormula.indexOf("[");
        int endPos = sFormula.indexOf("]");

        while (startPos != -1 && endPos != -1) {
            String pair = sFormula.substring(startPos + 1, endPos);
            String[] aPair = pair.split(",");
            String colName = aPair[0];
            int r = row + 1 + Integer.parseInt(aPair[1]);
            sFormula = sFormula.replace(("[" + pair + "]"), colName + r);

            startPos = sFormula.indexOf("[");
            endPos = sFormula.indexOf("]");
        }
        
        return sFormula;
    }
    
    public static boolean sendReport(SGuiClient client, ArrayList<SDbGrindingReportItemGroup> group, Date cutOffDate, String sMonth, int[] plantKey) {
        DateFormat fileNameformatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        DateFormat subjectformatter = new SimpleDateFormat("dd-MM-yyyy");
        ArrayList<String> files = new ArrayList<>();
        Map<String, String> inlineImages = new HashMap<>();

        SDbProcessingBatch oLot = group.get(0).getSDbLotAux();
        SDbItem oItem = group.get(0).getSDbItemAux();

        String body = "<html>"
                + "<body>";

        int counter = 0;
        for (SDbGrindingReportItemGroup oItemGroup : group) {
            String itmName = oItemGroup.getSDbItemAux().getNameShort().replace(" ", "_");
            String sFileName = "molienda/Mol_" + itmName.replace("/", "").replace(".", "") + "_" + oItemGroup.getSDbLotAux().getProcessingBatch().replace("/", "").replace(".", "") + "_" + fileNameformatter.format(new Date());
            File file = new File(sFileName + ".xlsx");
            try (FileOutputStream outputStream = new FileOutputStream(sFileName + ".xlsx")) {
                oItemGroup.getWorkbookAux().write(outputStream);
                if (!file.exists()) {
                    file.createNewFile();
                }

                // get the content in bytes
                outputStream.flush();
                outputStream.close();

                // Se comenta sección que funciona con la librería spire
                //Get the first worksheet
                //            Workbook workbook1 = new Workbook();
                //            workbook1.loadFromStream(new FileInputStream(file));
                ////            Worksheet sheet = workbook1.getWorksheets().get(0);
                //            Worksheet sheet = workbook1.getWorksheets().get(workbook1.getWorksheets().size() - 1);
                //
                //            //Save the sheet to image
                //            BufferedImage bufferedImage = sheet.toImage(68, 1, 125, 7);
                //            ImageIO.write(bufferedImage, "PNG", new File("output/ToImage.png"));
                //            
                //            //Save the sheet to image
                //            sheet.saveToImage("output/image.png");
                // Esta sección funciona con la librería ASPOSE
                // Create workbook from source file.
                Workbook workbooka = new Workbook(new FileInputStream(file));

                // Access the first worksheet
                Worksheet worksheet = workbooka.getWorksheets().get(sMonth);

                // Set the print area with your desired range
                worksheet.getPageSetup().setPrintArea(oItemGroup.getRangeAux());

                // Set all margins as 0
                worksheet.getPageSetup().setLeftMargin(0);
                worksheet.getPageSetup().setRightMargin(0);
                worksheet.getPageSetup().setTopMargin(0);
                worksheet.getPageSetup().setBottomMargin(0);

                // Set OnePagePerSheet option as true
                ImageOrPrintOptions options = new ImageOrPrintOptions();
                options.setOnePagePerSheet(true);
                options.setImageType(ImageType.JPEG);
                options.setHorizontalResolution(200);
                options.setVerticalResolution(200);

                // Take the image of your worksheet
                SheetRender sr = new SheetRender(worksheet, options);
                String img = "output/resumeMailBody" + counter + ".jpg";
                sr.toImage(0, img);

                String basePath = System.getProperty("user.dir");
                String imageFilePath = basePath + "/" + img;

                body += "<p>" + SLibUtils.textToHtml("Última fecha de captura: ") + "&nbsp;<b>" + SLibUtils.textToHtml(subjectformatter.format(cutOffDate)) + "</b></p>"
                        + "<p>" + SLibUtils.textToHtml("Ítem: ") + "&nbsp;<b>" + SLibUtils.textToHtml(oItemGroup.getSDbItemAux().getCode() + "-" + oItemGroup.getSDbItemAux().getName()) + "</b>"
                        + "<br>"
                        + "" + SLibUtils.textToHtml("Lote: ") + "&nbsp;<b>" + SLibUtils.textToHtml(oItemGroup.getSDbLotAux().getProcessingBatch()) + "</b></p>";

                if (!oItemGroup.getResumeHeaderRows().isEmpty()) {
                    body += "<p>";
                    for (SGrindingResumeRow resumeHeaderRow : oItemGroup.getResumeHeaderRows()) {
                        body += SLibUtils.textToHtml(resumeHeaderRow.getDataName()) + ": <b>"
                                + SLibUtils.textToHtml(SLibUtils.getDecimalFormatQuantity().format(resumeHeaderRow.getValue())) + " "
                                + SLibUtils.textToHtml(resumeHeaderRow.getUnit()) + "</b>";
                        body += "<br>";
                    }
                    body += "</p>";
                }

                body += "<img src=\"cid:AbcXyz123" + counter + "\" />";

                // inline images
                inlineImages.put("AbcXyz123" + counter, imageFilePath);
                files.add(sFileName + ".xlsx");
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (Exception ex) {
                Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            counter++;
        }

        body += "</body>"
                + "</html>";

        String[] tos = SGrindingResultsUtils.getGrindingRecsAndCcs(client, oItem.getPkItemId(), plantKey);

        if (tos[0].isEmpty()) {
            client.showMsgBoxError("No se encontró configuración de destinatarios de correo.");
            return false;
        }

        ArrayList<String> rc = new ArrayList<>();
        String[] mails = tos[0].split(";");
        for (String mail : mails) {
            rc.add(mail);
        }
        ArrayList<String> cc = new ArrayList<>();
        String[] mailsCC = tos[1].split(";");
        for (String mail : mailsCC) {
            cc.add(mail);
        }

        SDbCompany company = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getCompany();
        String subject = "Reporte molienda del " + subjectformatter.format(cutOffDate) + ". " + oItem.getNameShort() + " " + oLot.getProcessingBatch();

        try {
            SEmbeddedImageEmailUtil.send(company.getMailNotificationConfigHost(), company.getMailNotificationConfigPort(), company.getMailNotificationConfigUser(),
                    company.getMailNotificationConfigPassword(), rc, cc, subject, body, inlineImages, files);
        }
        catch (MessagingException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        client.showMsgBoxInformation("Reporte de molienda enviado a " + tos[0] + " cc: " + tos[1]);

        return true;
    }
    
    /**
     * 
     * 
     * @param client
     * @param workbook
     * @param oLot
     * @param oItem
     * @param cutOffDate
     * @return 
     */
    public static boolean saveReport(SGuiClient client, XSSFWorkbook workbook, SDbProcessingBatch oLot, SDbItem oItem, Date cutOffDate) {
        DateFormat fileNameformatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String itmName = oItem.getNameShort().replace(" ", "_");
        String sFileName = "Mol_" + itmName + "_" + oLot.getProcessingBatch() + "_" + fileNameformatter.format(new Date());
        sFileName = sFileName.replace("/", "_");
        File file = new File("molienda/" + sFileName + ".xlsx");
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Especifica un archivo para el guardado.");
        fileChooser.setSelectedFile(file);
        int userSelection = fileChooser.showSaveDialog(client.getFrame());
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
        
            try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
                workbook.write(outputStream);
                if (! file.exists()) {
                    file.createNewFile();
                }

                // get the content in bytes
                outputStream.flush();
                outputStream.close();
            }
            catch (IOException ex) {
                Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);

                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Obtener destinatarios y con copia de la base de datos.
     * 
     * @param client
     * @param idItem
     * 
     * @return String[0] destinatarios, String[1] Con copia
     */
    private static String[] getGrindingRecsAndCcs(SGuiClient client, final int idItem, final int[] plantKey) {
        String query = "SELECT "
                + "    recs, "
                + "    ccs "
                + "FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_REP_RECIPIENT) + " "
                + "WHERE "
                + "    fk_item = " + idItem + " AND NOT b_del "
                + " AND fk_pla_co = " + plantKey[0] + " AND fk_pla_cob = " + plantKey[1] + " AND fk_pla_pla = " + plantKey[2] + " "
                + ";";

        ResultSet resCfg;
        try {
            resCfg = client.getSession().getStatement().getConnection().createStatement().executeQuery(query);

            if (resCfg.next()) {
                return new String[]{resCfg.getString("recs"), resCfg.getString("ccs")};
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new String[]{"", ""};
    }
}
