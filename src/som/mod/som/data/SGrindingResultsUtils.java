/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
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
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.cfg.db.SDbCompany;
import som.mod.som.db.SDbGrinding;
import som.mod.som.db.SDbGrindingResult;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbLot;

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
     * 
     * @return int id del último ítem o lote que se haya capturado
     */
    public static int getLastConfiguration(SGuiClient client, final int option) {
        String field = "";
        
        String sql2 = "";
        if (option == ITEM) {
            field = "fk_item_id";
            
            sql2 = "SELECT " + field + " AS l_conf FROM " + SModConsts.TablesMap.get(SModConsts.CU_LINK_ITEM_PARAM) + 
                            " WHERE b_del = 0 ORDER BY fk_usr_ins DESC;";
        }
        else {
            field = "fk_lot_id";
            
            sql2 = "SELECT id_lot AS l_conf FROM " + SModConsts.TablesMap.get(SModConsts.SU_LOT) + 
                            " WHERE b_del = 0 ORDER BY fk_usr_ins DESC;";
        }
        
        String sql = "SELECT " + field + " FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_RESULTS) + 
                        " WHERE b_del = 0 ORDER BY id_result DESC;";
        
        
        ResultSet itemIdRes;
        ResultSet lastConfRes;
        try {
            itemIdRes = client.getSession().getStatement().executeQuery(sql);
            
            while(itemIdRes.next()) {
                return itemIdRes.getInt(field);
            }
            
            lastConfRes = client.getSession().getStatement().executeQuery(sql2);
            
            while(lastConfRes.next()) {
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
     * 
     * @return int id del lote
     */
    public static int getLastLotByItem(SGuiClient client, final int item) {
        if (item == 0) {
            return 0;
        }
        
        String sql = "SELECT fk_lot_id FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_RESULTS) + 
                        " WHERE b_del = 0 AND fk_item_id = " + item + " ORDER BY id_result DESC;";
        
        String sqlLots = "SELECT id_lot FROM " + SModConsts.TablesMap.get(SModConsts.SU_LOT) + 
                        " WHERE b_del = 0 AND fk_item_id = " + item + " ORDER BY fk_usr_ins DESC;";
        
        ResultSet itemIdRes;
        ResultSet lotIdRes;
        try {
            itemIdRes = client.getSession().getStatement().executeQuery(sql);
            
            while(itemIdRes.next()) {
                return itemIdRes.getInt("fk_lot_id");
            }
            
            lotIdRes = client.getSession().getStatement().executeQuery(sqlLots);
            
            while(lotIdRes.next()) {
                return lotIdRes.getInt("id_lot");
            }
            
            return 0;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
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
     */
    public static void generateResults(SGuiClient client, final boolean bAllItems, final int nItemId, final int nLotId, final Date tCaptureDate) {
        String sql = "SELECT  " +
                    "capture_order, " +
                    "i.code AS item_code, " +
                    "i.name AS item_name, " +
                    "lip.fk_item_id, " +
                    (bAllItems ? ("COALESCE((SELECT  " +
                    "        id_lot " +
                    "     FROM " +
                    "        su_lots " +
                    "     WHERE " +
                    "        fk_item_id = lip.fk_item_id " +
                    "        AND b_del = 0 " +
                    "        ORDER BY id_lot DESC LIMIT 1), " +
                    "        0) AS lot_id, ") : "") +
                    "lip.fk_parameter_id " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.CU_LINK_ITEM_PARAM) + " lip " +
                    "INNER JOIN " +
                    "su_item i ON lip.fk_item_id = i.id_item " +
                    "WHERE " +
                    "lip.b_del = 0 " +
                    (! bAllItems ? (" AND lip.fk_item_id = ") + nItemId : "") + " " +
                    "ORDER BY lip.fk_item_id ASC, lip.fk_parameter_id ASC;";
        
        ResultSet links;
        ArrayList<SDbGrindingResult> resultsToCapture = new ArrayList<>();
        
        try {
            links = client.getSession().getStatement().executeQuery(sql);

            while(links.next()) {
                SDbGrindingResult res = new SDbGrindingResult();

                res.setFkItemId(links.getInt("fk_item_id"));
                res.setFkParameterId(links.getInt("fk_parameter_id"));
                res.setFkLotId(bAllItems ? links.getInt("lot_id") : nLotId);
                res.setOrder(links.getInt("capture_order"));
                res.setDateCapture(tCaptureDate);
                
                if (res.getFkLotId() == 0) {
                    client.showMsgBoxError("El ítem " + links.getString("item_code") + 
                                            " - " + links.getString("item_name") + 
                                            " no tiene lotes asignados.");
                    return;
                }
                
                if (SGrindingResultsUtils.resultExists(client, res.getFkItemId(), 
                        res.getFkParameterId(), res.getFkLotId(), res.getDateCapture()) > 0) {
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
                    SModConsts.TablesMap.get(SModConsts.SU_GRINDING_RESULTS) + 
                    " WHERE fk_item_id = " + nItemId +
                    " AND fk_lot_id = " + nLotId +
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
    private static int resultExists(SGuiClient client, final int itm, final int param, final int lot, final Date dtCapture) {
        String sql = "SELECT id_result FROM " + 
                        SModConsts.TablesMap.get(SModConsts.SU_GRINDING_RESULTS) + 
                    " WHERE fk_item_id = " + itm + 
                    " AND fk_parameter_id = " + param + 
                    " AND fk_lot_id = " + lot +
                    " AND dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(dtCapture) + "' "+
                    " AND b_del = 0 ORDER BY id_result DESC;";
        
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
     * 
     * @return 
     */
    public static double getMonthAverage(SGuiClient client, final Date dtDate, final int item, final int parameter) {
        
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
                    SModConsts.TablesMap.get(SModConsts.SU_GRINDING_RESULTS) + " " +
                    "WHERE " +
                    " MONTH(dt_capture) = MONTH('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                    " AND fk_parameter_id = " + parameter + " " +
                    " AND fk_item_id = " + item + " " +
                    "GROUP BY dt_capture";
        
        ResultSet resIdResult;
        
        try {
            resIdResult = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            double avgSum;
            double avg;
            
            double d8;
            double d10;
            double d12;
            double d14;
            double d16;
            double d18;
            double d20;
            double d22;
            double d0;
            double d2;
            double d4;
            double d6;
            
            double avgSumTot = 0;
            int avgNumTot = 0;
            
            while (resIdResult.next()) {
                int avgNum = 0;
                avgSum = 0d;
                avg = 0d;
                
                d8 = resIdResult.getDouble("result_08");
                d10 = resIdResult.getDouble("result_10");
                d12 = resIdResult.getDouble("result_12");
                d14 = resIdResult.getDouble("result_14");
                d16 = resIdResult.getDouble("result_16");
                d18 = resIdResult.getDouble("result_18");
                d20 = resIdResult.getDouble("result_20");
                d22 = resIdResult.getDouble("result_22");
                d0 = resIdResult.getDouble("result_00");
                d2 = resIdResult.getDouble("result_02");
                d4 = resIdResult.getDouble("result_04");
                d6 = resIdResult.getDouble("result_06");
                
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
     * 
     * @return double
     */
    public static double getWeightedAverage(SGuiClient client, final Date dtDate, 
                                    final double monthGrinding, final int nItemId, 
                                    final int parameterId, final boolean onlyCurrentDay,
                                    final boolean isWeightedAverage) {
        String sql = "SELECT @prom := COALESCE((COALESCE(result_08, 0) + COALESCE(result_10, 0) + COALESCE(result_12, 0) + " +
                    "	COALESCE(result_14, 0) + COALESCE(result_16, 0) + COALESCE(result_18, 0) + " +
                    "	COALESCE(result_20, 0) + COALESCE(result_22, 0) + COALESCE(result_00, 0) + " +
                    "	COALESCE(result_02, 0) + COALESCE(result_04, 0) + COALESCE(result_06, 0)) / " +
                    "	(IF(result_08 > 0, 1, 0) + IF(result_10 > 0, 1, 0) + IF(result_12 > 0, 1, 0) + " +
                    "	IF(result_14 > 0, 1, 0) + IF(result_16 > 0, 1, 0) + IF(result_18 > 0, 1, 0) + " +
                    "	IF(result_20 > 0, 1, 0) + IF(result_22 > 0, 1, 0) + IF(result_00 > 0, 1, 0) + " +
                    "	IF(result_02 > 0, 1, 0) + IF(result_04 > 0, 1, 0) + IF(result_06 > 0, 1, 0)), 0) AS promedio, " +
                    "	" + (!onlyCurrentDay ? "SUM(" : "") + "COALESCE(@prom / " + monthGrinding + 
                    " * COALESCE((SELECT grinding_oil_perc FROM som_com.su_grinding WHERE dt_capture = sgr.dt_capture), 0),0)" + (!onlyCurrentDay ? ")" : "") + " AS ponderado " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_RESULTS) + " sgr " +
                    "WHERE fk_parameter_id = " + parameterId + " " +
                    "AND fk_item_id = " + nItemId + " " +
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
                    SModConsts.TablesMap.get(SModConsts.SU_GRINDINGS) + 
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
     * 
     * @return ArrayList[SGrindingData] 
     */
    public static ArrayList<SGrindingData> getGrindingByMonth(SGuiClient client, Date dtDate, final int nItemId, final boolean onlyCurrentMonth) {
        ArrayList<SGrindingData> grindings = new ArrayList<>();
        
        String sql = "SELECT " +
                        "SUM(grinding_bascule) AS sum_gr_basc, " +
                        "SUM(grinding_oil_perc) AS sum_gr_op, " +
                        "MONTH(dt_capture) AS g_month " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDINGS) + " " +
                        "WHERE " +
                        "NOT b_del " +
                        "AND MONTH(dt_capture) " + (onlyCurrentMonth ? "=" : "<=" ) +  " MONTH('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                        "AND YEAR(dt_capture) = " + " YEAR('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                        "AND fk_item_id = " + nItemId + " " +
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
     * Obtiene el archivo de configuración contenido en el campo de la relación de parámetro vs ítem
     * 
     * @param client
     * @param nItemId
     * 
     * @return SCaptureConfiguration
     */
    public static SCaptureConfiguration getCfgFile(SGuiClient client, final int nItemId) {
        String query = "SELECT " +
                        "    capture_cfg " +
                        "FROM " +
                        "    " + SModConsts.TablesMap.get(SModConsts.CU_LINK_ITEM_PARAM) + " " +
                        "WHERE " +
                        "    fk_item_id = " + nItemId + " AND NOT b_del " +
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
    
    public static boolean sendReport(SGuiClient client, XSSFWorkbook workbook, SDbLot oLot, SDbItem oItem, Date cutOffDate) {
        DateFormat fileNameformatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        DateFormat subjectformatter = new SimpleDateFormat("dd-MM-yyyy");
        String sFileName = "molienda/Molienda_" + fileNameformatter.format(new Date());
        File file = new File(sFileName + ".xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(sFileName + ".xlsx")) {
            workbook.write(outputStream);
            if (!file.exists()) {
                file.createNewFile();
            }
            
            // get the content in bytes
            
            outputStream.flush();
            outputStream.close();
            
            SDbCompany company = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getCompany();
            SMailSender sender = new SMailSender(
                    company.getMailNotificationConfigHost(),
                    company.getMailNotificationConfigPort(),
                    company.getMailNotificationConfigProtocol(),
                    company.isMailNotificationConfigStartTls(),
                    company.isMailNotificationConfigAuth(),
                    company.getMailNotificationConfigUser(),
                    company.getMailNotificationConfigPassword(),
                company.getMailNotificationConfigUser());
            
//            SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "Aeth2021*s.", "som@aeth.mx");
            
            ArrayList<String> rc = new ArrayList<String>();
            String[] mails = company.getGrindingReportMails().split(";");
            for (String mail : mails) {
                rc.add(mail);
            }
            
            String subject = "Reporte molienda del " + subjectformatter.format(cutOffDate) + ". " + oItem.getNameShort() + " " + oLot.getLot();
            
            String body = "<html>" +
                            "<body>";
            
            body += "<p>" + SLibUtils.textToHtml("Última fecha de captura: ") + "&nbsp;<b>" + SLibUtils.textToHtml(subjectformatter.format(cutOffDate)) + "</b></p>" +
                            "<p>" + SLibUtils.textToHtml("Ítem: ") + "&nbsp;<b>" + SLibUtils.textToHtml(oItem.getCode() + "-" + oItem.getName()) + "</b></p>" +
                            "<p>" + SLibUtils.textToHtml("Lote: ") + "&nbsp;<b>" + SLibUtils.textToHtml(oLot.getLot()) + "</b></p>";
            
            body += "</body>" +
                    "</html>";
            
            SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(subject), body, rc);
            mail.getAttachments().add(file);
            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
            mail.send();
            
            System.out.println("Mail sent!");
            client.showMsgBoxInformation("Reporte de molienda enviado a " + company.getGrindingReportMails());
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (MessagingException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
