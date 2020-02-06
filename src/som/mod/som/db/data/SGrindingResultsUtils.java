/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;
import som.mod.som.db.SDbGrinding;
import som.mod.som.db.SDbGrindingResult;

/**
 *
 * @author Edwin Carmona
 */
public class SGrindingResultsUtils {
    
    public final static int FIRST = 1;
    public final static int PREV = 2;
    public final static int NEXT = 3;
    public final static int LAST = 4;
    
    public final static int RESID_PASTA = 8;
    public final static int CONT_ACEITE_SEM = 12;
    public final static int RESID_PRENSA = 2;
    public final static int PROTEINA = 9;
    
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
        
        if (option == ITEM) {
            field = "fk_item_id";
        }
        else {
            field = "fk_lot_id";
        }
        
        String sql = "SELECT " + field + " FROM " + SModConsts.TablesMap.get(SModConsts.SU_LAB_GRINDING) + 
                        " WHERE b_del = 0 ORDER BY id_result DESC;";
        
        ResultSet itemIdRes;
        try {
            itemIdRes = client.getSession().getStatement().executeQuery(sql);
            
            while(itemIdRes.next()) {
                return itemIdRes.getInt(field);
            }
            
            return 0;
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public static int getLastLotByItem(SGuiClient client, final int item) {
        String sql = "SELECT fk_lot_id FROM " + SModConsts.TablesMap.get(SModConsts.SU_LAB_GRINDING) + 
                        " WHERE b_del = 0 AND fk_item_id = " + item + " ORDER BY id_result DESC;";
        
        ResultSet itemIdRes;
        try {
            itemIdRes = client.getSession().getStatement().executeQuery(sql);
            
            while(itemIdRes.next()) {
                return itemIdRes.getInt("fk_lot_id");
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
                    SModConsts.TablesMap.get(SModConsts.SU_LAB_GRINDING) + 
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
                        SModConsts.TablesMap.get(SModConsts.SU_LAB_GRINDING) + 
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
                    SModConsts.TablesMap.get(SModConsts.SU_LAB_GRINDING) + " " +
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
     * 
     * @param client
     * @param dtDate
     * @param monthGrinding
     * @param nItemId
     * @param parameterId
     * @param onlyCurrentDay
     * @return 
     */
    public static double getWeightedAverage(SGuiClient client, final Date dtDate, final double monthGrinding, final int nItemId, final int parameterId, final boolean onlyCurrentDay) {
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
                    "FROM " + SModConsts.TablesMap.get(SModConsts.SU_LAB_GRINDING) + " sgr " +
                    "WHERE fk_parameter_id = " + parameterId + " " +
                    "AND fk_item_id = " + nItemId + " " +
                    "AND MONTH(dt_capture) = MONTH('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "') " +
                    (onlyCurrentDay ? "AND dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "' " : "") + 
                    ";";
        
        ResultSet resIdResult;
        
        try {
            
            resIdResult = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            if (resIdResult.next()) {
                return resIdResult.getDouble("ponderado");
            }
            
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResultsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0d;
    }
    
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
    
    public static SDbGrinding getGrindingByDate(SGuiClient client, Date dtDate) {
        SDbGrinding grindingResult = new SDbGrinding();
        
        String sql = "SELECT id_grinding FROM " + 
                    SModConsts.TablesMap.get(SModConsts.SU_GRINDINGS) + 
                    " WHERE dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "' "+
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
        
        return  new SDbGrinding();
    }
    
    public static ArrayList<SGrindingData> getGrindingByMonth(SGuiClient client, Date dtDate, final int nItemId, final boolean onlyCurrentMonth) {
        ArrayList<SGrindingData> grindings = new ArrayList<>();
        
        String sql = "SELECT " +
                        "SUM(grinding_bascule) AS sum_gr_basc, " +
                        "SUM(grinding_oil_perc) AS sum_gr_op, " +
                        "MONTH(dt_capture) AS g_month " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.SU_GRINDINGS) + " " +
                        "WHERE " +
                        "NOT b_del " +
                        "AND MONTH(dt_capture) " + (onlyCurrentMonth ? "=" : "<=" ) +  " MONTH('" + SLibUtils.DbmsDateFormatDate.format(dtDate) + "')" +
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
}
