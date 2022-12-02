/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;
import som.mod.som.db.SDbStockRecord;

/**
 *
 * @author Edwin Carmona
 */
public class SStockRecordUtils {

    /**
     * Guarda el registro del stock en base a las existencias consultando las tablas
     * del stock, del stock diario, y de los almacenes móviles.
     * 
     * @param client
     * @param dtEstimation fecha de la estimación.
     * 
     * @return if the process ends succesfully
     * 
     * @throws SQLException 
     */
    public static boolean saveStockRecord(SGuiClient client, Date dtEstimation) throws SQLException {

        String sqlStock = "SELECT  "
                + "    v.id_year, "
                + "    v.id_item, "
                + "    v.id_unit, "
                + "    v.id_co, "
                + "    v.id_cob, "
                + "    v.id_wah, "
                + "    vi.code, "
                + "    vi.name, "
                + "    vc.code, "
                + "    vw.name, "
                + "    vw.code, "
                + "    vw.b_mobile, "
                + "    SUM(v.mov_in) AS f_mov_i, "
                + "    SUM(v.mov_out) AS f_mov_o, "
                + "    SUM(v.mov_in - v.mov_out) AS f_stk, "
                + "    vu.code, "
                + "    wp.b_premises,"
                + "    sday.fk_oil_cl_n, "
                + "    sday.fk_oil_tp_n, "
                + "    sday.fk_oil_own_n "
                + "FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS v "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS vi ON v.id_item = vi.id_item "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS vu ON v.id_unit = vu.id_unit "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS vw ON v.id_co = vw.id_co "
                + "        AND v.id_cob = vw.id_cob "
                + "        AND v.id_wah = vw.id_wah "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " AS vc ON v.id_co = vc.id_co "
                + "        AND v.id_cob = vc.id_cob "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.S_WAH_PREMISE) + " AS wp ON v.id_co = wp.id_co "
                + "        AND v.id_cob = wp.id_cob "
                + "        AND v.id_wah = wp.id_wah "
                + "        AND wp.id_dt = '" + SLibUtils.DbmsDateFormatDate.format(dtEstimation) + "' "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sday ON v.id_year = sday.id_year "
                + "        AND v.id_item = sday.id_item "
                + "        AND v.id_unit = sday.id_unit "
                + "        AND v.id_co = sday.id_co "
                + "        AND v.id_cob = sday.id_cob "
                + "        AND v.id_wah = sday.id_wah "
                + "        AND sday.id_day = '" + SLibUtils.DbmsDateFormatDate.format(dtEstimation) + "' "
                + "WHERE "
                + "    v.b_del = 0 AND v.id_year = " + SLibTimeUtils.digestYear(dtEstimation)[0] + " "
                + "        AND v.dt <= '" + SLibUtils.DbmsDateFormatDate.format(dtEstimation) + "' AND vw.b_del = 0 "
                + "GROUP BY v.id_item , v.id_unit , v.id_cob , v.id_wah , vc.code , vw.name , vi.code , vi.name , vu.code "
                + "HAVING f_stk >= 0 "
                + "ORDER BY f_stk desc, vi.name , vi.code , v.id_item , vu.code , v.id_unit , vc.code , vw.name , v.id_cob , v.id_wah";

        ResultSet stkResult = client.getSession().getStatement().getConnection().createStatement().executeQuery(sqlStock);

        SDbStockRecord oStkRecord;
        client.getSession().getStatement().getConnection().setAutoCommit(false);
        
        try {
            String deleteSql = "DELETE FROM " + 
                                SModConsts.TablesMap.get(SModConsts.S_STK_RECORD) + 
                            " WHERE dt = '" + SLibUtils.DbmsDateFormatDate.format(dtEstimation) + "';";
            client.getSession().getStatement().getConnection().createStatement().executeUpdate(deleteSql);
            
            ArrayList<String> warehouses = new ArrayList<>();
            String whsKey = "";
            while (stkResult.next()) {
                oStkRecord = new SDbStockRecord();

                oStkRecord.setPkItemId(stkResult.getInt("id_item"));
                oStkRecord.setPkUnitId(stkResult.getInt("id_unit"));
                oStkRecord.setPkCompanyId(stkResult.getInt("id_co"));
                oStkRecord.setPkBranchId(stkResult.getInt("id_cob"));
                oStkRecord.setPkWarehouseId(stkResult.getInt("id_wah"));
                oStkRecord.setDate(dtEstimation);
                oStkRecord.setStock(stkResult.getDouble("f_stk"));
                
                whsKey = oStkRecord.getPkCompanyId() + "_" + oStkRecord.getPkBranchId() + "_" + oStkRecord.getPkWarehouseId() + "_" + SLibUtils.DbmsDateFormatDate.format(dtEstimation);
                if (oStkRecord.getStock() >= 0d && ! warehouses.contains(whsKey)) {
                    warehouses.add(whsKey);
                }
                else if (oStkRecord.getStock() == 0d && warehouses.contains(whsKey)) {
                    continue;
                }
                
                oStkRecord.setPremises(stkResult.getBoolean("b_premises"));
                oStkRecord.setDeleted(false);
                oStkRecord.setSystem(true);
                oStkRecord.setFkOilClassId_n(stkResult.getInt("fk_oil_cl_n"));
                oStkRecord.setFkOilTypeId_n(stkResult.getInt("fk_oil_tp_n"));
                oStkRecord.setFkOilOwnerId_n(stkResult.getInt("fk_oil_own_n"));
                oStkRecord.setFkOilAcidity_n(0);
                oStkRecord.setFkOilAcidityEntry_n(0);

                oStkRecord.save(client.getSession());
            }
            
            client.getSession().getStatement().getConnection().commit();
        }
        catch (Exception ex) {
            Logger.getLogger(SStockRecordUtils.class.getName()).log(Level.SEVERE, null, ex);

            if (client.getSession().getStatement().getConnection() != null) {
                System.err.print("Transaction is being rolled back in save stock record.");
                try {
                    client.getSession().getStatement().getConnection().rollback();
                }
                catch (SQLException ex1) {
                    Logger.getLogger(SStockRecordUtils.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            
            return false;
        }

        return true;
    }

}
