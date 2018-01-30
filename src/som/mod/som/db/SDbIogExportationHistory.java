/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbIogExportationHistory extends SDbRegistryUser {

    protected int mnPkIogExportationId;
    protected int mnPkExternalIogYearId;
    protected int mnPkExternalIogDocId;
    protected Date mtDate;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbIogExportationHistory() {
        super(SModConsts.S_IOG_EXP_HIS);
        initRegistry();
    }

    public void setPkIogExportationId(int n) { mnPkIogExportationId = n; }
    public void setPkExternalIogYearId(int n) { mnPkExternalIogYearId = n; }
    public void setPkExternalIogDocId(int n) { mnPkExternalIogDocId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkIogExportationId() { return mnPkIogExportationId; }
    public int getPkExternalIogYearId() { return mnPkExternalIogYearId; }
    public int getPkExternalIogDocId() { return mnPkExternalIogDocId; }
    public Date getDate() { return mtDate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkIogExportationId = pk[0];
        mnPkExternalIogYearId = pk[1];
        mnPkExternalIogDocId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkIogExportationId, mnPkExternalIogYearId, mnPkExternalIogDocId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkIogExportationId = 0;
        mnPkExternalIogYearId = 0;
        mnPkExternalIogDocId = 0;
        mtDate = null;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_iog_exp = " + mnPkIogExportationId + " AND id_ext_iog_year =  " + mnPkExternalIogYearId +
                " AND id_ext_iog_doc = " + mnPkExternalIogDocId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_iog_exp = " + pk[0] + " AND id_ext_iog_year =  " + pk[1] + " AND id_ext_iog_doc = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

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
            mnPkExternalIogYearId = resultSet.getInt("id_ext_iog_year");
            mnPkExternalIogDocId = resultSet.getInt("id_ext_iog_doc");
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
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            mbUpdatable = true;
            mbDisableable = false;
            mbDeletable = true;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkIogExportationId + ", " +
                    mnPkExternalIogYearId + ", " +
                    mnPkExternalIogDocId + ", " +
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
                    //"id_ext_iog_year = " + mnPkExternalIogYearId + ", " +
                    //"id_ext_iog_doc = " + mnPkExternalIogDocId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbIogExportationHistory clone() throws CloneNotSupportedException {
        SDbIogExportationHistory registry = new SDbIogExportationHistory();

        registry.setPkIogExportationId(this.getPkIogExportationId());
        registry.setPkExternalIogYearId(this.getPkExternalIogYearId());
        registry.setPkExternalIogDocId(this.getPkExternalIogDocId());
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
