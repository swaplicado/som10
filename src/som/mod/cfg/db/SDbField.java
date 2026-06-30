/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 * Field settings to validate user input.
 * @author Sergio Flores
 */
public class SDbField extends SDbRegistryUser {
    
    protected int mnPkFieldId;
    protected String msCode;
    protected String msName;
    protected String msDescription;
    /** JSON field settings. */
    protected String msSettings;
    /** Individual characters to be trimmed from user input. */
    protected String msTrimCharacters;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbField() {
        super(SModConsts.C_FIELD);
        initRegistry();
    }

    public void setPkFieldId(int n) { mnPkFieldId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setSettings(String s) { msSettings = s; }
    public void setTrimCharacters(String s) { msTrimCharacters = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkFieldId() { return mnPkFieldId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getDescription() { return msDescription; }
    public String getSettings() { return msSettings; }
    public String getTrimCharacters() { return msTrimCharacters; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkFieldId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkFieldId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkFieldId = 0;
        msCode = "";
        msName = "";
        msDescription = "";
        msSettings = "";
        msTrimCharacters = "";
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
        return "WHERE id_field = " + mnPkFieldId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_field = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        mnPkFieldId = 0;

        msSql = "SELECT COALESCE(MAX(id_field), 0) + 1 FROM " + getSqlTable();
        
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                mnPkFieldId = resultSet.getInt(1);
            }
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnPkFieldId = resultSet.getInt("id_field");
                msCode = resultSet.getString("code");
                msName = resultSet.getString("name");
                msDescription = resultSet.getString("description");
                msSettings = resultSet.getString("settings");
                msTrimCharacters = resultSet.getString("trim_chars");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
                mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
                mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
                mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

                mbRegistryNew = false;
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkFieldId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msDescription + "', " + 
                    "'" + msSettings + "', " + 
                    "'" + msTrimCharacters + "', " + 
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
                    //"id_field = " + mnPkFieldId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "description = '" + msDescription + "', " +
                    "settings = '" + msSettings + "', " +
                    "trim_chars = '" + msTrimCharacters + "', " +
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
    public SDbField clone() throws CloneNotSupportedException {
        SDbField registry = new SDbField();

        registry.setPkFieldId(this.getPkFieldId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDescription(this.getDescription());
        registry.setSettings(this.getSettings());
        registry.setTrimCharacters(this.getTrimCharacters());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
