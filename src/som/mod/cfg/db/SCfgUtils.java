/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Isabel Servín
 */
public class SCfgUtils {
    
    /**
     * Get value of requested configuration parameter.
     * @param statement Database statement.
     * @param paramKey Requested configuration parameter. Constants defined in class som.mod.SModSysConsts (SModSysConsts.CFG_PARAM_...)
     * @return
     * @throws Exception 
     */
    public static String getParamValue(final Statement statement, final String paramKey) throws Exception {
        String paramValue = "";
        String sql = "SELECT param_value FROM c_param WHERE param_key = '" + paramKey + "';";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                paramValue = resultSet.getString(1);
            }
        }
        
        return paramValue;
    } 
}
