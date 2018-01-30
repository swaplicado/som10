/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SGuiClientUtils {

    public static boolean isPeriodOpened(final SGuiSession session, final Date date) {
        int[] period = SLibTimeUtils.digestMonth(date);
        return isPeriodOpened(session, period[0], period[1]);
    }

    public static boolean isPeriodOpened(final SGuiSession session, final int year, final int period) {
        boolean opened = false;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT b_clo FROM " + SModConsts.TablesMap.get(SModConsts.CU_YEAR_PER) + " "
                    + "WHERE id_year = " + year + " AND id_per = " + period + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                opened = !resultSet.getBoolean(1);
            }
        }
        catch (SQLException e) {
            SLibUtils.showException(SGuiClientUtils.class.getName(), e);
        }

        return opened;
    }
}
