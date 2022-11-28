/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Isabel Servín
 */
public class SDbClosingCalendar extends SDbRegistryUser {
    
    protected int mnPkClosingCalendarId;
    protected int mnCalendarYear;
    protected int mnCalendarMonth;
    protected Date mtClosingDate;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkInputCategoryId;
    protected int mnFkInputClassId;
    protected int mnFkInputTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbClosingCalendar() {
        super(SModConsts.SU_CLOSING_CAL);
    }
    
    public void setPkClosingCalendarId(int n) { mnPkClosingCalendarId = n; }
    public void setCalendarYear(int n) { mnCalendarYear = n; }
    public void setCalendarMonth(int n) { mnCalendarMonth = n; }
    public void setClosingDate(Date t) { mtClosingDate = t; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkInputCategoryId(int n) { mnFkInputCategoryId = n; }
    public void setFkInputClassId(int n) { mnFkInputClassId = n; }
    public void setFkInputTypeId(int n) { mnFkInputTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkClosingCalendarId() { return mnPkClosingCalendarId; }
    public int getCalendarYear() { return mnCalendarYear; }
    public int getCalendarMonth() { return mnCalendarMonth; }
    public Date getClosingDate() { return mtClosingDate; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkInputCategoryId() { return mnFkInputCategoryId; }
    public int getFkInputClassId() { return mnFkInputClassId; }
    public int getFkInputTypeId() { return mnFkInputTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkClosingCalendarId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkClosingCalendarId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkClosingCalendarId = 0;
        mnCalendarYear = 0;
        mnCalendarMonth = 0;
        mtClosingDate = null;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkInputCategoryId = 0;
        mnFkInputClassId = 0;
        mnFkInputTypeId = 0;
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
        return "WHERE id_closing_cal = " + mnPkClosingCalendarId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_closing_cal = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkClosingCalendarId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_closing_cal), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkClosingCalendarId = resultSet.getInt(1);
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
            mnPkClosingCalendarId = resultSet.getInt("id_closing_cal");
            mnCalendarYear = resultSet.getInt("cal_year");
            mnCalendarMonth = resultSet.getInt("cal_month");
            mtClosingDate = resultSet.getDate("closing_dt");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkInputCategoryId = resultSet.getInt("fk_inp_ct");
            mnFkInputClassId = resultSet.getInt("fk_inp_cl");
            mnFkInputTypeId = resultSet.getInt("fk_inp_tp");
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
            mbDisableable = false;
            mbDeletable = true;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkClosingCalendarId + ", " + 
                    mnCalendarYear + ", " + 
                    mnCalendarMonth + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtClosingDate) + "', " + 
                    (mbUpdatable ? 1 : 0) + ", " + 
                    (mbDisableable ? 1 : 0) + ", " + 
                    (mbDeletable ? 1 : 0) + ", " + 
                    (mbDisabled ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkInputCategoryId + ", " + 
                    mnFkInputClassId + ", " + 
                    mnFkInputTypeId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_closing_cal = " + mnPkClosingCalendarId + ", " +
                    "cal_year = " + mnCalendarYear + ", " +
                    "cal_month = " + mnCalendarMonth + ", " +
                    "closing_dt = '" + SLibUtils.DbmsDateFormatDate.format(mtClosingDate) + "', " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_inp_ct = " + mnFkInputCategoryId + ", " +
                    "fk_inp_cl = " + mnFkInputClassId + ", " +
                    "fk_inp_tp = " + mnFkInputTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + ", " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbClosingCalendar clone() throws CloneNotSupportedException {
        SDbClosingCalendar registry = new SDbClosingCalendar();
        
        registry.setPkClosingCalendarId(this.getPkClosingCalendarId());
        registry.setCalendarYear(this.getCalendarYear());
        registry.setCalendarMonth(this.getCalendarMonth());
        registry.setClosingDate(this.getClosingDate());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkInputCategoryId(this.getFkInputCategoryId());
        registry.setFkInputClassId(this.getFkInputClassId());
        registry.setFkInputTypeId(this.getFkInputTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
