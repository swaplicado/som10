/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDbOpCalendar extends SDbRegistryUser {

    protected int mnPkOpCalendarId;
    protected String msCode;
    protected String msName;
    protected String msItemsApplying;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbOpCalendarYear> maChildYears;
    
    public SDbOpCalendar() {
        super(SModConsts.SU_OP_CAL);
    }
    
    public void setPkOpCalendarId(int n) { mnPkOpCalendarId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setItemsApplying(String s) { msItemsApplying = s; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkOpCalendarId() { return mnPkOpCalendarId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getItemsApplying() { return msItemsApplying; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public ArrayList<SDbOpCalendarYear> getChildYears() { return maChildYears; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkOpCalendarId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkOpCalendarId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkOpCalendarId = 0;
        msCode = "";
        msName = "";
        msItemsApplying = "";
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildYears = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_op_cal = " + mnPkOpCalendarId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_op_cal = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkOpCalendarId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_op_cal), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkOpCalendarId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkOpCalendarId = resultSet.getInt("id_op_cal");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msItemsApplying = resultSet.getString("item_apply");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read as well children:
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                msSql = "SELECT id_year "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_OP_CAL_YEAR) + " "
                        + "WHERE id_op_cal = " + mnPkOpCalendarId + " "
                        + "ORDER BY id_year;";

                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    SDbOpCalendarYear year = new SDbOpCalendarYear();
                    year.read(session, new int[] { mnPkOpCalendarId, resultSet.getInt("id_year") });
                    maChildYears.add(year);
                }
            }
            
            // complete reading:
            
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
                    mnPkOpCalendarId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msItemsApplying + "', " + 
                    (mbUpdatable ? 1 : 0) + ", " + 
                    (mbDisableable ? 1 : 0) + ", " + 
                    (mbDeletable ? 1 : 0) + ", " + 
                    (mbDisabled ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_op_cal = " + mnPkOpCalendarId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "item_apply = '" + msItemsApplying + "', " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // save as well children:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.SU_OP_CAL_YEAR_MONTH) + " "
                + "WHERE id_op_cal = " + mnPkOpCalendarId + ";";
        session.getStatement().execute(msSql);
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.SU_OP_CAL_YEAR) + " "
                + "WHERE id_op_cal = " + mnPkOpCalendarId + ";";
        session.getStatement().execute(msSql);
        
        for (SDbOpCalendarYear year : maChildYears) {
            year.setPkOpCalendarId(mnPkOpCalendarId);
            year.setPkYearId(0);
            year.setRegistryNew(false);
            year.save(session);
        }
        
        // complete saving:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbOpCalendar clone() throws CloneNotSupportedException {
        SDbOpCalendar registry = new SDbOpCalendar();
        
        registry.setPkOpCalendarId(this.getPkOpCalendarId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setItemsApplying(this.getItemsApplying());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        // clone as well children:
        
        for (SDbOpCalendarYear year : this.getChildYears()) {
            registry.getChildYears().add(year.clone());
        }
        
        // complete clonning:

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
