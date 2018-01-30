/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class SDbDpsAssorted extends SDbRegistryUser {

    protected int mnPkExternalDpsYearId;
    protected int mnPkExternalDpsDocId;
    protected Date mtDate;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbDpsAssorted() {
        super(SModConsts.S_DPS_ASS);
        initRegistry();
    }

    public void setPkExternalDpsYearId(int n) { mnPkExternalDpsYearId = n; }
    public void setPkExternalDpsDocId(int n) { mnPkExternalDpsDocId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkExternalDpsYearId() { return mnPkExternalDpsYearId; }
    public int getPkExternalDpsDocId() { return mnPkExternalDpsDocId; }
    public Date getDate() { return mtDate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void closeOpenIog(SGuiSession session, int[] anPrimaryKey, boolean delete) throws SQLException, Exception {
        
        ResultSet resultSet = null;
        Statement statement = null;
        
        mnPkExternalDpsYearId = anPrimaryKey[0];
        mnPkExternalDpsDocId = anPrimaryKey[1];        
        mtDate = session.getSystemDate();
        mbDeleted = delete;        

        msSql = "SELECT * FROM " + getSqlTable() + " " + getSqlWhere();
        
        statement = session.getStatement();
        resultSet = statement.executeQuery(msSql);
        
        if (resultSet.next()) {
            mbRegistryNew = false;            
        }
        
        save(session);      
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkExternalDpsYearId = pk[0];
        mnPkExternalDpsDocId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkExternalDpsYearId, mnPkExternalDpsDocId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkExternalDpsYearId = 0;
        mnPkExternalDpsDocId = 0;
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
        return "WHERE id_ext_dps_year = " + mnPkExternalDpsYearId + " AND id_ext_dps_doc = " + mnPkExternalDpsDocId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ext_dps_year = " + pk[0] + " AND id_ext_dps_doc = " + pk[1];
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
            mnPkExternalDpsYearId = resultSet.getInt("id_ext_dps_year");
            mnPkExternalDpsDocId = resultSet.getInt("id_ext_dps_doc");
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
            computePrimaryKey(session);
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkExternalDpsYearId + ", " +
                    mnPkExternalDpsDocId + ", " +
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
                    //"id_ext_year = " + mnPkExternalDpsYearId + ", " +
                    //"id_ext_doc = " + mnPkExternalDpsDocId + ", " +
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
    public SDbDpsAssorted clone() throws CloneNotSupportedException {
        SDbDpsAssorted registry = new SDbDpsAssorted();

        registry.setPkExternalDpsYearId(this.getPkExternalDpsYearId());
        registry.setPkExternalDpsDocId(this.getPkExternalDpsDocId());
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
