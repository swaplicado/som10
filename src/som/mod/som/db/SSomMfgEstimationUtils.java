/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SSomMfgEstimationUtils {
    
    public static SDbMfgEstimation getLastMfgEstimation(final SGuiSession session, final Date dateStockDay) throws Exception {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        SDbMfgEstimation estimation = null;
        
        statement = session.getStatement().getConnection().createStatement();
        
        sql = "SELECT id_mfg_est, ver " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_MFG_EST) + " " +
                "WHERE b_del = 0 AND b_clo = 0 AND dt_stk_day = '" +  SLibUtils.DbmsDateFormatDate.format(dateStockDay) + "' " +
                "ORDER BY id_mfg_est ASC, ver ASC " +
                "LIMIT 1 ";
        
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            estimation = (SDbMfgEstimation) session.readRegistry(SModConsts.S_MFG_EST, new int[] { resultSet.getInt(1) });
        }
        
        return estimation;
    }
}
