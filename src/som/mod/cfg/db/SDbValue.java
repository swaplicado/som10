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
 * Value of a field.
 * @author Sergio Flores
 */
public class SDbValue extends SDbRegistryUser {

    protected int mnPkValueId;
    protected String msValueText;
    protected String msValueReference;
    protected String msScopeInputCategories;
    protected String msScopeItems;
    //protected boolean mbDeleted;
    protected int mnFkFieldId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbValue() {
        super(SModConsts.C_VALUE);
        initRegistry();
    }

    public void setPkValueId(int n) { mnPkValueId = n; }
    public void setValueText(String s) { msValueText = s; }
    public void setValueReference(String s) { msValueReference = s; }
    public void setScopeInputCategories(String s) { msScopeInputCategories = s; }
    public void setScopeItems(String s) { msScopeItems = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkFieldId(int n) { mnFkFieldId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkValueId() { return mnPkValueId; }
    public String getValueText() { return msValueText; }
    public String getValueReference() { return msValueReference; }
    public String getScopeInputCategories() { return msScopeInputCategories; }
    public String getScopeItems() { return msScopeItems; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkFieldId() { return mnFkFieldId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkValueId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkValueId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkValueId = 0;
        msValueText = "";
        msValueReference = "";
        msScopeInputCategories = "";
        msScopeItems = "";
        mbDeleted = false;
        mnFkFieldId = 0;
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
        return "WHERE id_value = " + mnPkValueId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_value = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        mnPkValueId = 0;

        msSql = "SELECT COALESCE(MAX(id_value), 0) + 1 FROM " + getSqlTable();
        
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                mnPkValueId = resultSet.getInt(1);
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
                mnPkValueId = resultSet.getInt("id_value");
                msValueText = resultSet.getString("value_text");
                msValueReference = resultSet.getString("value_ref");
                msScopeInputCategories = resultSet.getString("scope_inp_ct");
                msScopeItems = resultSet.getString("scope_item");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkFieldId = resultSet.getInt("fk_field");
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkValueId + ", " + 
                    "'" + msValueText + "', " + 
                    "'" + msValueReference + "', " + 
                    "'" + msScopeInputCategories + "', " + 
                    "'" + msScopeItems + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkFieldId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_value = " + mnPkValueId + ", " +
                    "value_text = '" + msValueText + "', " +
                    "value_ref = '" + msValueReference + "', " +
                    "scope_inp_ct = '" + msScopeInputCategories + "', " +
                    "scope_item = '" + msScopeItems + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_field = " + mnFkFieldId + ", " +
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
    public SDbValue clone() throws CloneNotSupportedException {
        SDbValue registry = new SDbValue();

        registry.setPkValueId(this.getPkValueId());
        registry.setValueText(this.getValueText());
        registry.setValueReference(this.getValueReference());
        registry.setScopeInputCategories(this.getScopeInputCategories());
        registry.setScopeItems(this.getScopeItems());
        registry.setDeleted(this.isDeleted());
        registry.setFkFieldId(this.getFkFieldId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
