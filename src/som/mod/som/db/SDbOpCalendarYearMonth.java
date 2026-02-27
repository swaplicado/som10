/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDbOpCalendarYearMonth extends SDbRegistryUser {

    protected int mnPkOpCalendarId;
    protected int mnPkYearId;
    protected int mnPkMonthId;
    protected String msCode;
    protected String msName;
    protected Date mtMonthStart;
    protected Date mtMonthEnd;
    
    public SDbOpCalendarYearMonth() {
        super(SModConsts.SU_OP_CAL_YEAR_MONTH);
    }
    
    public void setPkOpCalendarId(int n) { mnPkOpCalendarId = n; }
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkMonthId(int n) { mnPkMonthId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setMonthStart(Date t) { mtMonthStart = t; }
    public void setMonthEnd(Date t) { mtMonthEnd = t; }

    public int getPkOpCalendarId() { return mnPkOpCalendarId; }
    public int getPkYearId() { return mnPkYearId; }
    public int getPkMonthId() { return mnPkMonthId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getMonthStart() { return mtMonthStart; }
    public Date getMonthEnd() { return mtMonthEnd; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkOpCalendarId = pk[0];
        mnPkYearId = pk[1];
        mnPkMonthId = pk[2];
    }
    
    /**
     * Get month start and end as a period ia a <code>Date[]</code> array.
     * @return 
     */
    public Date[] getPeriod() {
        return new Date[] { mtMonthStart, mtMonthEnd };
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkOpCalendarId, mnPkYearId, mnPkMonthId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkOpCalendarId = 0;
        mnPkYearId = 0;
        mnPkMonthId = 0;
        msCode = "";
        msName = "";
        mtMonthStart = null;
        mtMonthEnd = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_op_cal = " + mnPkOpCalendarId + " "
                + "AND id_year = " + mnPkYearId + " "
                + "AND id_month = " + mnPkMonthId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_op_cal = " + pk[0] + " "
                + "AND id_year = " + pk[1] + " "
                + "AND id_month = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mnPkYearId = resultSet.getInt("id_year");
            mnPkMonthId = resultSet.getInt("id_month");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mtMonthStart = resultSet.getDate("month_sta");
            mtMonthEnd = resultSet.getDate("month_end");
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        verifyRegistryNew(session);
        
        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkOpCalendarId + ", " + 
                    mnPkYearId + ", " + 
                    mnPkMonthId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtMonthStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtMonthEnd) + "' " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    /*
                    "id_op_cal = " + mnPkOpCalendarId + ", " +
                    "id_year = " + mnPkYearId + ", " +
                    "id_month = " + mnPkMonthId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "month_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtMonthStart) + "', " +
                    "month_end = '" + SLibUtils.DbmsDateFormatDate.format(mtMonthEnd) + "' " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbOpCalendarYearMonth clone() throws CloneNotSupportedException {
        SDbOpCalendarYearMonth registry = new SDbOpCalendarYearMonth();
        
        registry.setPkOpCalendarId(this.getPkOpCalendarId());
        registry.setPkYearId(this.getPkYearId());
        registry.setPkMonthId(this.getPkMonthId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setMonthStart(this.getMonthStart());
        registry.setMonthEnd(this.getMonthEnd());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
