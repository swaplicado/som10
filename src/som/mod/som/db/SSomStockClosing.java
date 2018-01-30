/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomStockClosing {

    protected int mnYearId;
    protected String msQueryResult;
    protected int mnQueryResultId;
    protected int mnFkUserUpdateId;

    public void setYearId(int n) { mnYearId = n; }
    public void setQueryResult(String s) { msQueryResult = s; }
    public void setQueryResultId(int n) { mnQueryResultId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }

    public int getYearId() { return mnYearId; }
    public String getQueryResult() { return msQueryResult; }
    public int getQueryResultId() { return mnQueryResultId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }

    public SSomStockClosing() {
        mnYearId = 0;
    }

    public void save(SGuiSession session) throws SQLException, Exception {
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (generateStock(session)) {
            mnQueryResultId = SDbConsts.SAVE_OK;
        }
    }

    public boolean generateStock(SGuiSession session) throws SQLException, Exception {
        boolean b = true;
        String sql = "";

        SDbIog iog = null;
        Statement statement = null;
        ResultSet resultSet = null;

        ArrayList<SDbIog> aAdjustmentsDeleted = new ArrayList<SDbIog>();
        ArrayList<SDbIog> aAdjustmentsSaved = new ArrayList<SDbIog>();

        // Delete previous generation:

        sql = "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) +
                " WHERE b_del = 0 AND b_sys = 1 AND dt = '" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.createDate(mnYearId, 1, 1)) + "' AND " +
                "fk_iog_ct = " + SModSysConsts.SS_IOG_TP_IN_EXT_ADJ[0] + " AND fk_iog_cl = " + SModSysConsts.SS_IOG_TP_IN_EXT_ADJ[1] + " AND " +
                "fk_iog_tp = " + SModSysConsts.SS_IOG_TP_IN_EXT_ADJ[2] + " ";
        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(sql);

        // Validate if is possible deleted moves:

        while (resultSet.next()) {

            iog = new SDbIog();
            iog.read(session, new int[] { resultSet.getInt("id_iog") });
            if (!iog.canDelete(session)) {
                msQueryResult = iog.getQueryResult();
                b = false;
                break;
            }

            aAdjustmentsDeleted.add(iog);
        }

        if (b) {

            // Delete moves:

            for (SDbIog oIog : aAdjustmentsDeleted) {

                oIog.delete(session);
                if (oIog.getQueryResultId() != SDbConsts.SAVE_OK) {
                    msQueryResult = oIog.getQueryResult();
                    b = false;
                    break;
                }
            }

            if (b) {

                aAdjustmentsSaved.clear();
                sql = "SELECT s.id_item, s.id_unit, s.id_co, s.id_cob, s.id_wah, s.id_div, SUM(s.mov_in - s.mov_out) AS f_stk " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                    "s.id_item = i.id_item " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                    "s.id_unit = u.id_unit " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wa ON " +
                    "s.id_co = wa.id_co AND s.id_cob = wa.id_cob AND s.id_wah = wa.id_wah " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
                    "s.id_div = d.id_div " +
                    "WHERE s.id_year = " + (mnYearId - 1) + " AND s.b_del = 0 AND s.dt <=  '" + SLibTimeUtils.createDate(mnYearId - 1, 12, 31) + "' " +
                    "GROUP BY s.id_item, s.id_unit, s.id_co, s.id_cob, s.id_wah, s.id_div " +
                    "HAVING f_stk <> 0 " +
                    "ORDER BY s.id_co, s.id_cob, s.id_wah, s.id_div, s.id_item, s.id_unit; ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {

                    iog = new SDbIog();
                    iog.setDate(SLibTimeUtils.createDate(mnYearId, 1, 1));
                    iog.setQuantity(resultSet.getDouble("f_stk"));
                    iog.setSystem(true);
                    iog.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_IN_EXT_ADJ[0]);
                    iog.setFkIogClassId(SModSysConsts.SS_IOG_TP_IN_EXT_ADJ[1]);
                    iog.setFkIogTypeId(SModSysConsts.SS_IOG_TP_IN_EXT_ADJ[2]);
                    iog.setFkIogAdjustmentTypeId(SModSysConsts.SU_IOG_ADJ_TP_NA);
                    iog.setFkItemId(resultSet.getInt("s.id_item"));
                    iog.setFkUnitId(resultSet.getInt("s.id_unit"));
                    iog.setFkWarehouseCompanyId(resultSet.getInt("s.id_co"));
                    iog.setFkWarehouseBranchId(resultSet.getInt("s.id_cob"));
                    iog.setFkWarehouseWarehouseId(resultSet.getInt("s.id_wah"));
                    iog.setFkDivisionId(resultSet.getInt("s.id_div"));
                    iog.setFkUserInsertId(session.getUser().getPkUserId());

                    if (!iog.canSave(session)) {

                        msQueryResult = iog.getQueryResult();

                        // Undeleted iogs:

                        for (SDbIog oIog : aAdjustmentsDeleted) {

                            oIog.delete(session);
                            if (oIog.getQueryResultId() != SDbConsts.SAVE_OK) {
                                msQueryResult = oIog.getQueryResult();
                                b = false;
                                break;
                            }
                        }

                        b = false;
                        break;
                    }

                    aAdjustmentsSaved.add(iog);
                }

                if (b) {
                    for (SDbIog oIog : aAdjustmentsSaved) {

                        oIog.save(session);
                        if (oIog.getQueryResultId() != SDbConsts.SAVE_OK) {
                            msQueryResult = oIog.getQueryResult();
                            b = false;
                            break;
                        }
                    }
                }
            }
        }

        return b;
    }
}
