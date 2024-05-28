/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import erp.data.SDataConstantsSys;
import erp.lib.SLibTimeUtilities;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.SDataDiogNotes;
import erp.mtrn.data.STrnStockMove;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.gui.SGuiClientSessionCustom;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbIogExportation extends SDbRegistryUser {

    protected int mnPkIogExportationId;
    protected Date mtDate;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected String msXtaExternalDatabaseName;

    protected Vector<int[]> mvExternalDiogId;

    public SDbIogExportation() {
        super(som.mod.SModConsts.S_IOG_EXP);
        mvExternalDiogId = new Vector<int[]>();
        initRegistry();
    }

    /*
    * Private methods
    */

    private boolean validatePrimaryKeyExpHistory(SGuiSession session, int nPkIogExportationId, int nPkExternalIogYearId, int nPkExternalIogDocId) throws SQLException {
        boolean b = false;

        ResultSet resultSet = null;

        try {
            msSql = "SELECT id_iog_exp FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG_EXP_HIS) + " WHERE id_iog_exp = " + nPkIogExportationId + " AND id_ext_iog_year = " + nPkExternalIogYearId +
                " AND id_ext_iog_doc = " + nPkExternalIogDocId + "; ";
            resultSet = session.getStatement().executeQuery(msSql);

            if (resultSet.next()) {
                b = true;
            }
        }
        catch (Exception e) {
            b = true;
            SLibUtils.showException(this, e);
        }

        return b;
    }

    /*
    * Public methods
    */

    public void setPkIogExportationId(int n) { mnPkIogExportationId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkIogExportationId() { return mnPkIogExportationId; }
    public Date getDate() { return mtDate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setXtaExternalDatabaseName(String s) { msXtaExternalDatabaseName = s; }

    public String getXtaExternalDatabaseName() { return msXtaExternalDatabaseName; }

    public boolean validateUnsentDocuments(SGuiSession session) {
        boolean b = false;
        ResultSet resultSet = null;

        try {
            msSql = "SELECT io.dt " +
                "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS io " +
                "WHERE io.b_del = 0 AND io.dt < '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND ((SELECT COUNT(*) FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG_EXP) + " AS e WHERE e.dt = io.dt) = 0); ";

            resultSet = session.getStatement().executeQuery(msSql);
            if (resultSet.next()) {
                msQueryResult = "Existen documentos de inventario con fecha: '" + SLibUtils.DateFormatDate.format(resultSet.getDate("io.dt")) +
                        "' que aún no han sido exportados.";
                b = true;
            }
        }
        catch(Exception e) {
            SLibUtils.showException(this, e);
        }

        return b;
    }

    public boolean validateDpsEntries(SGuiSession session)  throws SQLException, Exception {
        boolean b = false;
        int nFkEntryId = 0;
        ResultSet resultSet = null;

        // Validate dps isn't delete AND entries:

        msSql = "SELECT CONCAT(tp.code, '-', i.num) AS f_iog_num, IF(d.num_ser <> '', CONCAT(d.num_ser, '-', d.num), d.num) AS f_num, 0 AS f_entry " +
            "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS i " +
            "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SS_IOG_CT) + " AS ct ON i.fk_iog_ct = ct.id_iog_ct " +
            "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SS_IOG_CL) + " AS cl ON i.fk_iog_ct = cl.id_iog_ct AND i.fk_iog_cl = cl.id_iog_cl " +
            "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SS_IOG_TP) + " AS tp ON i.fk_iog_ct = tp.id_iog_ct AND i.fk_iog_cl = tp.id_iog_cl AND i.fk_iog_tp = tp.id_iog_tp " +
            "INNER JOIN " + msXtaExternalDatabaseName + ".trn_dps AS d ON i.fk_ext_dps_year_n = d.id_year AND i.fk_ext_dps_doc_n = d.id_doc AND d.b_del = 1 " +
            "WHERE i.b_del = 0 AND i.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND " +
            "i.fk_iog_ct IN(" +
            som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR[0] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL[0] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR[0] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL[0] + ") AND " +
            "i.fk_iog_cl IN(" +
            som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR[1] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL[1] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR[1] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL[1] + ") AND " +
            "i.fk_iog_tp IN(" +
            som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR[2] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL[2] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR[2] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL[2] + ") " +
            "UNION " +
            "SELECT i.num, IF(d.num_ser <> '', CONCAT(d.num_ser, '-', d.num), d.num) AS f_num, de.id_ety AS f_entry " +
            "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS i " +
            "INNER JOIN " + msXtaExternalDatabaseName + ".trn_dps AS d ON i.fk_ext_dps_year_n = d.id_year AND i.fk_ext_dps_doc_n = d.id_doc " +
            "INNER JOIN " + msXtaExternalDatabaseName + ".trn_dps_ety AS de ON i.fk_ext_dps_year_n = de.id_year AND i.fk_ext_dps_doc_n = de.id_doc AND " +
            "i.fk_ext_dps_ety_n = de.id_ety AND de.b_del = 1 " +
            "WHERE i.b_del = 0 AND i.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND " +
            "i.fk_iog_ct IN(" +
            som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR[0] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL[0] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR[0] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL[0] + ") AND " +
            "i.fk_iog_cl IN(" +
            som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR[1] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL[1] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR[1] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL[1] + ") AND " +
            "i.fk_iog_tp IN(" +
            som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR[2] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL[2] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR[2] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL[2] + "); ";

        try {
            resultSet = session.getStatement().executeQuery(msSql);

            while (resultSet.next()) {
                nFkEntryId = resultSet.getInt("f_entry");

                if (nFkEntryId > 0) {
                    msQueryResult = "El docto. de inventario: '" + resultSet.getString("f_iog_num") + "', hace referencia a la partida de docto. de c/v: '" + resultSet.getString("f_num") +  "' \n "
                            + "la cual está eliminada en el sistema externo.";
                    b = true;
                }
                else {
                    msQueryResult = "El docto. de inventario: '" + resultSet.getString("f_iog_num") + "', hace referencia al docto. de c/v: '" + resultSet.getString("f_num") +  "' \n "
                            + "el cual está eliminado en el sistema externo.";
                    b = true;
                }

                break;
            }
        }
        catch(Exception e) {
            SLibUtils.showException(this, e);
        }

        return b;
    }

    public boolean validateDpsEntriesExists(SGuiSession session)  throws SQLException, Exception {
        boolean b = false;
        ResultSet resultSet = null;

        // Validate dps isn't delete AND entries:

        msSql = "SELECT CONCAT(tp.code, '-', io.num) AS f_iog_num, IF(d.num_ser <> '', CONCAT(d.num_ser, '-', d.num), d.num) AS f_num " +
            "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS io " +
            "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SS_IOG_CT) + " AS ct ON io.fk_iog_ct = ct.id_iog_ct " +
            "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SS_IOG_CL) + " AS cl ON io.fk_iog_ct = cl.id_iog_ct AND io.fk_iog_cl = cl.id_iog_cl " +
            "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SS_IOG_TP) + " AS tp ON io.fk_iog_ct = tp.id_iog_ct AND io.fk_iog_cl = tp.id_iog_cl AND io.fk_iog_tp = tp.id_iog_tp " +
            "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_ITEM) + " AS i ON io.fk_item = i.id_item " +
            "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_UNIT) + " AS u ON io.fk_unit = u.id_unit " +
            "INNER JOIN " + msXtaExternalDatabaseName + ".trn_dps AS d ON io.fk_ext_dps_year_n = d.id_year AND io.fk_ext_dps_doc_n = d.id_doc " +
            "INNER JOIN " + msXtaExternalDatabaseName + ".trn_dps_ety AS de ON io.fk_ext_dps_year_n = de.id_year AND io.fk_ext_dps_doc_n = de.id_doc AND " +
            "io.fk_ext_dps_ety_n = de.id_ety AND (i.fk_ext_item_n <> de.fid_item OR u.fk_ext_unit_n <> de.fid_unit) " +
            "WHERE io.b_del = 0 AND io.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND " +
            "io.fk_iog_ct IN(" +
            som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR[0] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL[0] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR[0] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL[0] + ") AND " +
            "io.fk_iog_cl IN(" +
            som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR[1] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL[1] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR[1] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL[1] + ") AND " +
            "io.fk_iog_tp IN(" +
            som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR[2] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL[2] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR[2] + ", " +
            som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL[2] + "); ";

        try {
            resultSet = session.getStatement().executeQuery(msSql);

            while (resultSet.next()) {

                msQueryResult = "El docto. de inventario: '" + resultSet.getString("f_iog_num") + "', hace referencia a la partida de docto. de c/v: '" + resultSet.getString("f_num") +  "' \n "
                        + "la cual tiene un ítem o unidad diferente en el sistema externo.";
                b = true;
                break;
            }
        }
        catch(Exception e) {
            SLibUtils.showException(this, e);
        }

        return b;
    }

    public boolean validateDpsAdjustments(SGuiSession session) {
        boolean b = false;
        int count = 0;
        ResultSet resultSet = null;

        try {
            msSql = "SELECT COUNT(*) = (SELECT COUNT(*) " +
                "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS g " +
                "WHERE g.b_del = 0 AND g.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND g.fk_ext_adj_year_n IS NOT NULL) AS f_res " +
                "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS g, " + msXtaExternalDatabaseName + ".trn_dps_dps_adj AS d " +
                "WHERE g.b_del = 0 AND g.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND g.fk_ext_adj_year_n IS NOT NULL AND " +
                "g.fk_ext_dps_year_n = d.id_dps_year AND g.fk_ext_dps_doc_n = d.id_dps_doc AND g.fk_ext_dps_ety_n = d.id_dps_ety AND " +
                "g.fk_ext_adj_year_n = d.id_adj_year AND g.fk_ext_adj_doc_n = d.id_adj_doc AND g.fk_ext_adj_ety_n = d.id_adj_ety; ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (resultSet.next()) {

                count = resultSet.getInt("f_res");
                if (count == 0) {
                    msQueryResult = "Doctos. de ajuste (devs.) en está fecha: '" + SLibUtils.DateFormatDate.format(mtDate) + "' ya no están relacionados a un docto. de c/v en el sistema externo.";
                    b = true;
                }
            }
        }
        catch(Exception e) {
            SLibUtils.showException(this, e);
        }

        return b;
    }

    public void deleteExternalDiogEntries(SGuiSession session) {
        ResultSet resultSet = null;

        try {
            // Validate dps isn't delete AND entries:

            msSql = "SELECT id_iog, fk_ext_iog_year_n, fk_ext_iog_doc_n, fk_ext_iog_ety_n " +
                "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS io " +
                "WHERE io.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND fk_ext_iog_year_n IS NOT NULL; ";

            mvExternalDiogId.removeAllElements();
            resultSet = session.getStatement().executeQuery(msSql);
            while (resultSet.next()) {

                mvExternalDiogId.add(new int[] { resultSet.getInt("id_iog"), resultSet.getInt("fk_ext_iog_year_n"),
                    resultSet.getInt("fk_ext_iog_doc_n"), resultSet.getInt("fk_ext_iog_ety_n"), });
            }

            // Delete Diog and Diog Entry for external system:

            for (int[] entry : mvExternalDiogId) {
                msSql = "UPDATE " + msXtaExternalDatabaseName + ".trn_diog AS d " +
                    "INNER JOIN " + msXtaExternalDatabaseName + ".trn_diog_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                    "INNER JOIN " + msXtaExternalDatabaseName + ".trn_stk AS s ON de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                    "SET d.b_del = 1, de.b_del = 1, s.b_del = 1 " +
                    "WHERE de.id_year = " + entry[1] + " AND de.id_doc = " + entry[2] + " AND de.id_ety = " + entry[3] + "; ";
                session.getStatement().execute(msSql);
            }
        }
        catch(Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    public boolean closeDps(SGuiSession session) {
        boolean b = false;

        try {
            msSql = "UPDATE " + msXtaExternalDatabaseName + ".trn_dps AS d, s_dps_ass AS ds " +
                "SET d.b_close = 1 " +
                "WHERE ds.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND d.id_year = ds.id_ext_dps_year AND d.id_doc = ds.id_ext_dps_doc; ";
            session.getStatement().execute(msSql);
        }
        catch(Exception e) {
            msQueryResult = "Error al cerrar los documentos de surtido en el sistema externo.";
        }

        return b;
    }

    public boolean createExternalDiog(SGuiSession session) throws SQLException, Exception {
        boolean b = true;
        int[] externalDiogTypeKey = null;
        Vector<int[]> vIogType = new Vector<int[]>();
        Vector<int[]> vDiogIogId = new Vector<int[]>();
        SDbIog iog = null;
        SDataDiog diog = null;
        SDataDiogEntry diogEntry = null;
        SDataDiogNotes diogNotes = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSet resultSetNote = null;
        ResultSet resultSetGroupBy = null;

        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_EXT_ADJ);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_EXT_INV);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_INT_TRA);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_INT_CNV);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_INT_MIX_PAS);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_INT_MIX_ACT);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_MFG_RM_RET);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_EXT_ADJ);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_EXT_INV);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_TRA);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_CNV);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_MIX_PAS);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_MIX_ACT);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD);
        vIogType.add(som.mod.SModSysConsts.SS_IOG_TP_OUT_MFG_FG_RET);

        for (int[] iogType : vIogType) {

            // Group by dps, adjustment, som move:

            if (SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL)) {

                msSql = "SELECT io.fk_iog_ct, io.fk_iog_cl, io.fk_iog_tp, w.fk_ext_cob_n, w.fk_ext_ent_n, io.fk_ext_dps_year_n, io.fk_ext_dps_doc_n " +
                    "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS io " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_ITEM) + " AS i ON io.fk_item = i.id_item " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_UNIT) + " AS u ON io.fk_unit = u.id_unit " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_EXT_WAH) + " AS w ON i.fk_ext_wah = w.id_ext_wah " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_IOG_ADJ_TP) + " AS adj ON io.fk_iog_adj_tp = adj.id_iog_adj_tp " +
                    "WHERE io.b_del = 0 AND io.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND io.fk_iog_ct = " + iogType[0] + " AND " +
                    "io.fk_iog_cl = " + iogType[1] + " AND io.fk_iog_tp = " + iogType[2] + " " +
                    "GROUP BY io.fk_iog_ct, io.fk_iog_cl, io.fk_iog_tp, w.fk_ext_cob_n, w.fk_ext_ent_n, io.fk_ext_dps_year_n, io.fk_ext_dps_doc_n; ";
            }
            else if (SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_EXT_ADJ) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_EXT_INV) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_EXT_ADJ) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_EXT_INV)) {

                msSql = "SELECT io.fk_iog_ct, io.fk_iog_cl, io.fk_iog_tp, w.fk_ext_cob_n, w.fk_ext_ent_n, io.fk_iog_adj_tp " +
                    "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS io " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_ITEM) + " AS i ON io.fk_item = i.id_item " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_UNIT) + " AS u ON io.fk_unit = u.id_unit " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_EXT_WAH) + " AS w ON i.fk_ext_wah = w.id_ext_wah " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_IOG_ADJ_TP) + " AS adj ON io.fk_iog_adj_tp = adj.id_iog_adj_tp " +
                        (SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_EXT_ADJ) ||
                        SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_EXT_ADJ) ?
                        "AND adj.id_iog_adj_tp != " + som.mod.SModSysConsts.SU_IOG_ADJ_TP_NA : "") + " " +
                    "WHERE io.b_del = 0 AND io.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND io.fk_iog_ct = " + iogType[0] + " AND " +
                    "io.fk_iog_cl = " + iogType[1] + " AND io.fk_iog_tp = " + iogType[2] + " " +
                    "GROUP BY io.fk_iog_ct, io.fk_iog_cl, io.fk_iog_tp, w.fk_ext_cob_n, w.fk_ext_ent_n, io.fk_iog_adj_tp; ";
            }
            else if (SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_MFG_RM_RET) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_INT_TRA) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_INT_CNV) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_INT_MIX_PAS) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_INT_MIX_ACT) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_MFG_FG_RET)||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_TRA) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_CNV) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_MIX_PAS) ||
                SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_MIX_ACT)) {

                msSql = "SELECT io.fk_iog_ct,  io.fk_iog_cl,  io.fk_iog_tp, w.fk_ext_cob_n, w.fk_ext_ent_n  " +
                    "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS io " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_ITEM) + " AS i ON io.fk_item = i.id_item " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_UNIT) + " AS u ON io.fk_unit = u.id_unit " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_EXT_WAH) + " AS w ON i.fk_ext_wah = w.id_ext_wah " +
                    "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_IOG_ADJ_TP) + " AS adj ON io.fk_iog_adj_tp = adj.id_iog_adj_tp " +
                    "WHERE io.b_del = 0 AND io.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND io.fk_iog_ct = " + iogType[0] + " AND " +
                    "io.fk_iog_cl = " + iogType[1] + " AND io.fk_iog_tp = " + iogType[2] + " " +
                    "GROUP BY io.fk_iog_ct,  io.fk_iog_cl,  io.fk_iog_tp, w.fk_ext_cob_n, w.fk_ext_ent_n; ";
            }

            statement = session.getDatabase().getConnection().createStatement();
            resultSetGroupBy = statement.executeQuery(msSql);
            while (resultSetGroupBy.next()) {

                diog = null;

                // Select by dps, adjustment, som move:

                if (SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_PUR_PUR) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_SAL_SAL) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_PUR_PUR) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_SAL_SAL)) {

                    msSql = "SELECT io.*, COALESCE(io.fk_ext_dps_year_n, 0) AS f_ext_dps_year, COALESCE(io.fk_ext_dps_doc_n, 0) AS f_ext_dps_doc, COALESCE(io.fk_ext_dps_ety_n, 0) AS f_ext_dps_ety, " +
                        "COALESCE(io.fk_ext_adj_year_n, 0) AS f_ext_adj_year, COALESCE(io.fk_ext_adj_doc_n, 0) AS f_ext_adj_doc, COALESCE(io.fk_ext_adj_ety_n, 0) AS f_ext_adj_ety, " +
                        "io.fk_iog_adj_tp, adj.fk_ext_iog_adj_tp, w.fk_ext_cob_n, w.fk_ext_ent_n, i.fk_ext_item_n, u.fk_ext_unit_n " +
                        "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS io " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_ITEM) + " AS i ON io.fk_item = i.id_item " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_UNIT) + " AS u ON io.fk_unit = u.id_unit " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_EXT_WAH) + " AS w ON i.fk_ext_wah = w.id_ext_wah " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_IOG_ADJ_TP) + " AS adj ON io.fk_iog_adj_tp = adj.id_iog_adj_tp " +
                        "WHERE io.b_del = 0 AND io.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND io.fk_iog_ct = " + iogType[0] + " AND " +
                        "io.fk_iog_cl = " + iogType[1] + " AND io.fk_iog_tp = " + iogType[2] + " AND " +
                        "w.fk_ext_cob_n = " + resultSetGroupBy.getInt("w.fk_ext_cob_n") + " AND " +
                        "w.fk_ext_ent_n = " + resultSetGroupBy.getInt("w.fk_ext_ent_n") + " AND " +
                        (resultSetGroupBy.getInt("io.fk_ext_dps_year_n") > 0 ?
                        "io.fk_ext_dps_year_n = " + resultSetGroupBy.getInt("io.fk_ext_dps_year_n") + " AND " +
                        "io.fk_ext_dps_doc_n = " + resultSetGroupBy.getInt("io.fk_ext_dps_doc_n") :
                        "io.fk_ext_dps_year_n IS NULL  AND " +
                        "io.fk_ext_dps_doc_n IS NULL ") + "; ";
                }
                else if (SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_EXT_ADJ) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_EXT_INV) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_EXT_ADJ) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_EXT_INV)) {

                    msSql = "SELECT io.*, COALESCE(io.fk_ext_dps_year_n, 0) AS f_ext_dps_year, COALESCE(io.fk_ext_dps_doc_n, 0) AS f_ext_dps_doc, COALESCE(io.fk_ext_dps_ety_n, 0) AS f_ext_dps_ety, " +
                        "COALESCE(io.fk_ext_adj_year_n, 0) AS f_ext_adj_year, COALESCE(io.fk_ext_adj_doc_n, 0) AS f_ext_adj_doc, COALESCE(io.fk_ext_adj_ety_n, 0) AS f_ext_adj_ety, " +
                        "io.fk_iog_adj_tp, adj.fk_ext_iog_adj_tp, w.fk_ext_cob_n, w.fk_ext_ent_n, i.fk_ext_item_n, u.fk_ext_unit_n " +
                        "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS io " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_ITEM) + " AS i ON io.fk_item = i.id_item " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_UNIT) + " AS u ON io.fk_unit = u.id_unit " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_EXT_WAH) + " AS w ON i.fk_ext_wah = w.id_ext_wah " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_IOG_ADJ_TP) + " AS adj ON io.fk_iog_adj_tp = adj.id_iog_adj_tp " +
                        "WHERE io.b_del = 0 AND io.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND io.fk_iog_ct = " + iogType[0] + " AND " +
                        "io.fk_iog_cl = " + iogType[1] + " AND io.fk_iog_tp = " + iogType[2] + " AND " +
                        "w.fk_ext_cob_n = " + resultSetGroupBy.getInt("w.fk_ext_cob_n") + " AND " +
                        "w.fk_ext_ent_n = " + resultSetGroupBy.getInt("w.fk_ext_ent_n") + " AND " +
                        "io.fk_iog_adj_tp = " + resultSetGroupBy.getInt("io.fk_iog_adj_tp") + "; ";
                }
                else if (SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_MFG_RM_RET) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_INT_TRA) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_INT_CNV) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_INT_MIX_PAS) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_IN_INT_MIX_ACT) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_MFG_FG_RET)||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_TRA) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_CNV) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_MIX_PAS) ||
                    SLibUtils.compareKeys(iogType, som.mod.SModSysConsts.SS_IOG_TP_OUT_INT_MIX_ACT)) {

                    msSql = "SELECT io.*, COALESCE(io.fk_ext_dps_year_n, 0) AS f_ext_dps_year, COALESCE(io.fk_ext_dps_doc_n, 0) AS f_ext_dps_doc, COALESCE(io.fk_ext_dps_ety_n, 0) AS f_ext_dps_ety, " +
                        "COALESCE(io.fk_ext_adj_year_n, 0) AS f_ext_adj_year, COALESCE(io.fk_ext_adj_doc_n, 0) AS f_ext_adj_doc, COALESCE(io.fk_ext_adj_ety_n, 0) AS f_ext_adj_ety, " +
                        "io.fk_iog_adj_tp, adj.fk_ext_iog_adj_tp, w.fk_ext_cob_n, w.fk_ext_ent_n, i.fk_ext_item_n, u.fk_ext_unit_n " +
                        "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS io " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_ITEM) + " AS i ON io.fk_item = i.id_item " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_UNIT) + " AS u ON io.fk_unit = u.id_unit " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_EXT_WAH) + " AS w ON i.fk_ext_wah = w.id_ext_wah " +
                        "INNER JOIN " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.SU_IOG_ADJ_TP) + " AS adj ON io.fk_iog_adj_tp = adj.id_iog_adj_tp " +
                        "WHERE io.b_del = 0 AND io.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND io.fk_iog_ct = " + iogType[0] + " AND " +
                        "io.fk_iog_cl = " + iogType[1] + " AND io.fk_iog_tp = " + iogType[2] + " AND " +
                        "w.fk_ext_cob_n = " + resultSetGroupBy.getInt("w.fk_ext_cob_n") + " AND " +
                        "w.fk_ext_ent_n = " + resultSetGroupBy.getInt("w.fk_ext_ent_n") + "; ";
                }

                externalDiogTypeKey = SSomUtils.getExternalDiogType(iogType);
                statement = session.getDatabase().getConnection().createStatement();
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {

                    // Create diog header:

                    if (diog == null) {
                        diog = new SDataDiog();

                        diog.setPkYearId(SLibTimeUtilities.digestYear(mtDate)[0]);
                        diog.setPkDocId(0);
                        diog.setDate(resultSet.getDate("io.dt"));
                        diog.setNumberSeries("");
                        diog.setNumber("");
                        diog.setValue_r(0);
                        diog.setCostAsigned(0);
                        diog.setCostTransferred(0);
                        diog.setIsShipmentRequired(false);
                        diog.setIsShipped(false);
                        diog.setIsAudited(false);
                        diog.setIsAuthorized(false);
                        diog.setIsRecordAutomatic(false);
                        diog.setIsSystem(true);
                        diog.setIsDeleted(false);
                        diog.setFkDiogCategoryId(externalDiogTypeKey[0]);
                        diog.setFkDiogClassId(externalDiogTypeKey[1]);
                        diog.setFkDiogTypeId(externalDiogTypeKey[2]);
                        diog.setFkDiogAdjustmentTypeId(resultSet.getInt("adj.fk_ext_iog_adj_tp"));
                        diog.setFkCompanyBranchId(resultSet.getInt("w.fk_ext_cob_n"));
                        diog.setFkWarehouseId(resultSet.getInt("w.fk_ext_ent_n"));
                        diog.setFkDpsYearId_n(resultSet.getInt("f_ext_dps_year"));
                        diog.setFkDpsDocId_n(resultSet.getInt("f_ext_dps_doc"));
                        /*
                        diog.setFkDiogYearId_n();
                        diog.setFkDiogDocId_n();
                        diog.setFkMfgYearId_n();
                        diog.setFkMfgOrderId_n();
                        diog.setFkBookkeepingYearId_n();
                        diog.setFkBookkeepingNumberId_n();
                        */
                        diog.setFkMaintMovementTypeId(SModSysConsts.TRNS_TP_MAINT_MOV_NA);
                        diog.setFkMaintUserId_n(SLibConsts.UNDEFINED);
                        diog.setFkMaintUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
                        diog.setFkMaintReturnUserId_n(SLibConsts.UNDEFINED);
                        diog.setFkMaintReturnUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
                        diog.setFkUserShippedId(SUtilConsts.USR_NA_ID);
                        diog.setFkUserAuditedId(SUtilConsts.USR_NA_ID);
                        diog.setFkUserAuthorizedId(SUtilConsts.USR_NA_ID);
                        diog.setFkUserNewId(SUtilConsts.USR_NA_ID);
                        diog.setFkUserEditId(SUtilConsts.USR_NA_ID);
                        diog.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
                        /*
                        diog.setUserAuditedTs();
                        diog.setUserAuthorizedTs();
                        diog.setUserNewTs();
                        diog.setUserEditTs();
                        diog.setUserDeleteTs();
                        */
                    }

                    // Create diog entries:

                    diogEntry = new SDataDiogEntry();

                    diogEntry.setPkYearId(diog.getPkYearId());
                    diogEntry.setPkDocId(0);
                    diogEntry.setPkEntryId(0);
                    diogEntry.setQuantity(resultSet.getDouble("io.qty"));
                    diogEntry.setValueUnitary(0);
                    diogEntry.setValue(0);
                    diogEntry.setOriginalQuantity(resultSet.getDouble("io.qty"));
                    diogEntry.setOriginalValueUnitary(0);
                    diogEntry.setSortingPosition(0);
                    diogEntry.setIsInventoriable(true);
                    diogEntry.setIsDeleted(false);
                    diogEntry.setFkItemId(resultSet.getInt("i.fk_ext_item_n"));
                    diogEntry.setFkUnitId(resultSet.getInt("u.fk_ext_unit_n"));
                    diogEntry.setFkOriginalUnitId(resultSet.getInt("u.fk_ext_unit_n"));
                    diogEntry.setFkDpsYearId_n(resultSet.getInt("f_ext_dps_year"));
                    diogEntry.setFkDpsDocId_n(resultSet.getInt("f_ext_dps_doc"));
                    diogEntry.setFkDpsEntryId_n(resultSet.getInt("f_ext_dps_ety"));
                    diogEntry.setFkDpsAdjustmentYearId_n(resultSet.getInt("f_ext_adj_year"));
                    diogEntry.setFkDpsAdjustmentDocId_n(resultSet.getInt("f_ext_adj_doc"));
                    diogEntry.setFkDpsAdjustmentEntryId_n(resultSet.getInt("f_ext_adj_ety"));
                    /*diogEntry.setFkMfgYearId_n();
                    diogEntry.setFkMfgOrderId_n();
                    diogEntry.setFkMfgChargeId_n(); */
                    diogEntry.setFkMaintAreaId(SModSysConsts.TRN_MAINT_AREA_NA);
                    diogEntry.setFkCostCenterId(SModSysConsts.FIN_CC_NA);
                    diogEntry.setFkUserNewId(SUtilConsts.USR_NA_ID);
                    diogEntry.setFkUserEditId(SUtilConsts.USR_NA_ID);
                    diogEntry.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
                    /*diogEntry.setUserNewTs();
                    diogEntry.setUserEditTs();
                    diogEntry.setUserDeleteTs();*/

                    /*if (item.getIsLotApplying()) {
                        iogEntry.getAuxStockMoves().addAll(moDialogStockLots.getStockMoves());
                    }
                    else {*/
                    diogEntry.getAuxStockMoves().add(
                            new STrnStockMove(new int[] {
                                diog.getPkYearId(),
                                diogEntry.getFkItemId(),
                                diogEntry.getFkUnitId(),
                                SDataConstantsSys.TRNX_STK_LOT_DEF_ID,
                                diog.getFkCompanyBranchId(),
                                diog.getFkWarehouseId() },
                                diogEntry.getQuantity()));
                    //}

                    diog.getDbmsEntries().add(diogEntry);

                    // Create diog note:

                    msSql = "SELECT id_iog, id_note, note FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG_NOTE) + " WHERE id_iog = " +
                            resultSet.getInt("io.id_iog");
                    statement = session.getDatabase().getConnection().createStatement();
                    resultSetNote = statement.executeQuery(msSql);

                    if (resultSetNote.next()) {

                        diogNotes = new SDataDiogNotes();

                        diogNotes.setPkYearId(diog.getPkYearId());
                        diogNotes.setPkDocId(0);
                        diogNotes.setPkNotesId(0);
                        diogNotes.setNotes(resultSetNote.getString("note").isEmpty() ? "" : ((vDiogIogId.size() + 1) + ". " + resultSetNote.getString("note")));
                        diogNotes.setFkUserNewId(SUtilConsts.USR_NA_ID);
                        diogNotes.setFkUserEditId(SUtilConsts.USR_NA_ID);
                        diogNotes.setFkUserDeleteId(SUtilConsts.USR_NA_ID);

                        diog.getDbmsNotes().add(diogNotes);
                    }

                    vDiogIogId.add(new int[] { resultSet.getInt("io.id_iog"), vDiogIogId.size() + 1 });
                }

                if (diog != null) {

                    if (diog.save(((SGuiClientSessionCustom) session.getSessionCustom()).getExtDatabaseCo().getConnection()) == som.mod.SModSysConsts.EXT_DB_ACTION_SAVE_OK) {

                        // Assign diog entry id to iog:

                        for (int[] iogId : vDiogIogId) {

                            iog = new SDbIog();

                            iog.read(session, new int[] { iogId[0] });
                            if (iog != null) {

                                iog.setFkExternalIogYearId_n(diog.getPkYearId());
                                iog.setFkExternalIogDocId_n(diog.getPkDocId());
                                iog.setFkExternalIogEntryId_n(iogId[1]);
                                iog.setXtaValidateExportationStock(true);

                                iog.save(session);
                            }
                        }
                    }
                    else {
                       msQueryResult = "Ocurrio un error al guardar los registros en el sistema externo.";
                       b = false;
                       break;
                    }

                    vDiogIogId.removeAllElements();
                }
            }
        }

        return b;
    }

    public boolean updateRelationDiog(SGuiSession session) {
        boolean b = true;
        int nFkExternalDpsYear_n = 0;
        int nFkExternalDpsDoc_n = 0;
        int nFkExternalDpsEntry_n = 0;
        int nPkDiogYearId = 0;
        int nPkDiogDocId = 0;
        int nPkDiogEntryId = 0;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Case when iog is deleted:

            msSql = "SELECT g.id_iog, de.id_year, de.id_doc, de.id_ety, g.fk_tic_n, g.fk_ext_dps_year_n, g.fk_ext_dps_doc_n, g.fk_ext_dps_ety_n, " +
                "de.fid_dps_year_n, de.fid_dps_doc_n, de.fid_dps_ety_n, g.b_del " +
                "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS g " +
                "INNER JOIN " + msXtaExternalDatabaseName + ".trn_diog_ety AS de ON g.fk_ext_iog_year_n = de.id_year AND g.fk_ext_iog_doc_n = de.id_doc AND " +
                "g.fk_ext_iog_ety_n = de.id_ety " +
                "INNER JOIN " + msXtaExternalDatabaseName + ".trn_diog AS d ON g.fk_ext_iog_year_n = d.id_year AND g.fk_ext_iog_doc_n = d.id_doc " +
                "WHERE d.b_del = 0 AND g.fk_tic_n > 0 AND g.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDate) +  "' AND " +
                "g.fk_ext_dps_year_n > 0 AND g.fk_ext_dps_doc_n > 0 AND g.fk_ext_dps_ety_n > 0 AND g.b_del = 1 ";
            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {

                nPkDiogYearId = resultSet.getInt("de.id_year");
                nPkDiogDocId = resultSet.getInt("de.id_doc");
                nPkDiogEntryId = resultSet.getInt("de.id_ety");

                // Assign null to trn_diog, trn_diog_ety, (XXX iog, ticket)

                msSql = "UPDATE " + msXtaExternalDatabaseName + ".trn_diog SET fid_dps_year_n = NULL, fid_dps_doc_n = NULL "
                        + "WHERE id_year = " + nPkDiogYearId + " AND id_doc = " + nPkDiogDocId + " ";
                session.getStatement().execute(msSql);

                msSql = "UPDATE " + msXtaExternalDatabaseName + ".trn_diog_ety SET fid_dps_year_n = NULL, fid_dps_doc_n = NULL, fid_dps_ety_n = NULL "
                        + "WHERE id_year = " + nPkDiogYearId + " AND id_doc = " + nPkDiogDocId + " AND id_ety = " + nPkDiogEntryId + " ";
                session.getStatement().execute(msSql);
            }

            // Case when iog external dps is different to diog dps or is null:

            msSql = "SELECT g.id_iog, de.id_year, de.id_doc, de.id_ety, g.fk_tic_n, g.fk_ext_dps_year_n, g.fk_ext_dps_doc_n, g.fk_ext_dps_ety_n, " +
                "de.fid_dps_year_n, de.fid_dps_doc_n, de.fid_dps_ety_n, g.b_del " +
                "FROM " + som.mod.SModConsts.TablesMap.get(som.mod.SModConsts.S_IOG) + " AS g " +
                "INNER JOIN " + msXtaExternalDatabaseName + ".trn_diog_ety AS de ON g.fk_ext_iog_year_n = de.id_year AND g.fk_ext_iog_doc_n = de.id_doc AND " +
                "g.fk_ext_iog_ety_n = de.id_ety " +
                "INNER JOIN " + msXtaExternalDatabaseName + ".trn_diog AS d ON g.fk_ext_iog_year_n = d.id_year AND g.fk_ext_iog_doc_n = d.id_doc " +
                "WHERE d.b_del = 0 AND g.fk_tic_n > 0 AND g.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDate) +  "' AND " +
                "g.fk_ext_dps_year_n > 0 AND g.fk_ext_dps_doc_n > 0 AND g.fk_ext_dps_ety_n > 0 AND " +
                "(g.fk_ext_dps_year_n <> de.fid_dps_year_n OR g.fk_ext_dps_doc_n <> de.fid_dps_doc_n OR g.fk_ext_dps_ety_n <> de.fid_dps_ety_n OR " +
                "de.fid_dps_year_n IS NULL OR de.fid_dps_doc_n IS NULL OR de.fid_dps_ety_n IS NULL) ";
            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {

                nFkExternalDpsYear_n = resultSet.getInt("g.fk_ext_dps_year_n");
                nFkExternalDpsDoc_n = resultSet.getInt("g.fk_ext_dps_doc_n");
                nFkExternalDpsEntry_n = resultSet.getInt("g.fk_ext_dps_ety_n");
                nPkDiogYearId = resultSet.getInt("de.id_year");
                nPkDiogDocId = resultSet.getInt("de.id_doc");
                nPkDiogEntryId = resultSet.getInt("de.id_ety");

                // Assign iog dps to diog dps:

                msSql = "UPDATE " + msXtaExternalDatabaseName + ".trn_diog SET fid_dps_year_n = " + nFkExternalDpsYear_n + ", fid_dps_doc_n = " +
                        nFkExternalDpsDoc_n + " "
                        + "WHERE id_year = " + nPkDiogYearId + " AND id_doc = " + nPkDiogDocId + " ";
                session.getStatement().execute(msSql);

                msSql = "UPDATE " + msXtaExternalDatabaseName + ".trn_diog_ety SET fid_dps_year_n = " + nFkExternalDpsYear_n + ", fid_dps_doc_n = " +
                        nFkExternalDpsDoc_n + ", fid_dps_ety_n = " + nFkExternalDpsEntry_n + " "
                        + "WHERE id_year = " + nPkDiogYearId + " AND id_doc = " + nPkDiogDocId + " AND id_ety = " + nPkDiogEntryId + " ";
                session.getStatement().execute(msSql);
            }

        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
            b = false;
        }

        return b;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkIogExportationId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkIogExportationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
    }

    @Override
    public String getSqlTable() {
        return som.mod.SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_iog_exp = " + mnPkIogExportationId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_iog_exp = " + pk[0];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkIogExportationId = 0;

        msSql = "SELECT COALESCE(MAX(id_iog_exp), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkIogExportationId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkIogExportationId = resultSet.getInt("id_iog_exp");
            mtDate = resultSet.getDate("dt");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        int nPkIogLastExportationId = 0;
        SDbIogExportationHistory expHistory = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        deleteExternalDiogEntries(session);
        if (!createExternalDiog(session)) {
            mnQueryResultId = SDbConsts.SAVE_ERROR;
        }
        else {
            // Validate relation between iog and diog when iog has dps external:

            if (!updateRelationDiog(session)) {
                mnQueryResultId = SDbConsts.SAVE_ERROR;
            }
            else {
                // Obtain the last exportation id of the date:

                ResultSet resultSet = null;

                msSql = "SELECT MAX(id_iog_exp) AS f_pk " +
                    "FROM " + getSqlTable() + " " +
                    "WHERE b_del = 0 AND dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' ";

                resultSet = session.getStatement().executeQuery(msSql);
                if (resultSet.next()) {
                    nPkIogLastExportationId = resultSet.getInt("f_pk");
                }

                if (mbRegistryNew) {
                    computePrimaryKey(session);
                    mbUpdatable = true;
                    mbDeletable = true;
                    mnFkUserInsertId = session.getUser().getPkUserId();
                    mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

                    msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                            mnPkIogExportationId + ", " +
                            "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                            (mbDeleted ? 1 : 0) + ", " +
                            mnFkUserInsertId + ", " +
                            mnFkUserUpdateId + ", " +
                            "NOW()" + ", " +
                            "NOW()" + " " +
                            ")";
                }
                else {
                    mnFkUserUpdateId = session.getUser().getPkUserId();

                    msSql = "UPDATE " + getSqlTable() + " SET " +
                        //"id_iog_exp = " + mnPkIogExportationId + ", " +
                        "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                        //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                        "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                        //"ts_usr_ins = " + "NOW()" + ", " +
                        "ts_usr_upd = " + "NOW()" + " " +
                        getSqlWhere();
                }
                session.getStatement().execute(msSql);

                // Delete the last exportation registry:

                if (nPkIogLastExportationId > 0) {
                    mnFkUserUpdateId = session.getUser().getPkUserId();

                    msSql = "UPDATE " + getSqlTable() + " SET " +
                        "b_del = 1, " +
                        "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                        "ts_usr_upd = " + "NOW()" + " " +
                        "WHERE id_iog_exp = " + nPkIogLastExportationId + "; ";
                    session.getStatement().execute(msSql);
                }

                // Create relation between exportation id and diog delete:

                for (int[] diog : mvExternalDiogId) {

                    expHistory = new SDbIogExportationHistory();

                    expHistory.setPkIogExportationId(mnPkIogExportationId);
                    expHistory.setPkExternalIogYearId(diog[1]);
                    expHistory.setPkExternalIogDocId(diog[2]);
                    expHistory.setDate(mtDate);

                    if (!validatePrimaryKeyExpHistory(session, expHistory.getPkIogExportationId(), expHistory.getPkExternalIogYearId(), expHistory.getPkExternalIogDocId())) {
                        expHistory.save(session);
                    }
                }

                mbRegistryNew = false;
                mnQueryResultId = SDbConsts.SAVE_OK;
            }
        }
    }

    @Override
    public boolean canSave(SGuiSession session) throws SQLException, Exception {
        boolean b = false;

        if (validateUnsentDocuments(session) ||
           validateDpsEntries(session) ||
           validateDpsEntriesExists(session) ||
           validateDpsAdjustments(session) ||
           closeDps(session)) {
            mnQueryResultId = SDbConsts.SAVE_ERROR;
        }
        else {
            b = true;
        }

        return b;

    }

    @Override
    public SDbIogExportation clone() throws CloneNotSupportedException {
        SDbIogExportation registry = new SDbIogExportation();

        registry.setPkIogExportationId(this.getPkIogExportationId());
        registry.setDate(this.getDate());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
