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
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDbOpCalendarYear extends SDbRegistryUser {

    protected int mnPkOpCalendarId;
    protected int mnPkYearId;
    protected String msCode;
    protected String msName;
    protected Date mtYearStart;
    protected Date mtYearEnd;
    
    protected ArrayList<SDbOpCalendarYearMonth> maChildMonths;
    
    public SDbOpCalendarYear() {
        super(SModConsts.SU_OP_CAL_YEAR);
    }
    
    public void setPkOpCalendarId(int n) { mnPkOpCalendarId = n; }
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setYearStart(Date t) { mtYearStart = t; }
    public void setYearEnd(Date t) { mtYearEnd = t; }

    public int getPkOpCalendarId() { return mnPkOpCalendarId; }
    public int getPkYearId() { return mnPkYearId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getYearStart() { return mtYearStart; }
    public Date getYearEnd() { return mtYearEnd; }
    
    public ArrayList<SDbOpCalendarYearMonth> getChildMonths() { return maChildMonths; }
    
    /**
     * Get month start and end as a period ia a <code>Date[]</code> array.
     * @return 
     */
    public Date[] getPeriod() {
        return new Date[] { mtYearStart, mtYearEnd };
    }

    /**
     * Get month by given date.
     * @param date Date.
     * @return Matching month, if any, otherwise <code>null</code>.
     */
    public SDbOpCalendarYearMonth getChildMonthByDate(final Date date) {
        SDbOpCalendarYearMonth monthByDate = null;
        
        if (SLibTimeUtils.isBelongingToPeriod(date, mtYearStart, mtYearEnd)) {
            // date belongs to year...
            for (SDbOpCalendarYearMonth month : maChildMonths) {
                if (SLibTimeUtils.isBelongingToPeriod(date, month.getMonthStart(), month.getMonthEnd())) {
                    // date belongs to month...
                    monthByDate = month;
                    break;
                }
            }
        }
        
        return monthByDate;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkOpCalendarId = pk[0];
        mnPkYearId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkOpCalendarId, mnPkYearId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkOpCalendarId = 0;
        mnPkYearId = 0;
        msCode = "";
        msName = "";
        mtYearStart = null;
        mtYearEnd = null;
        
        maChildMonths = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_op_cal = " + mnPkOpCalendarId + " "
                + "AND id_year = " + mnPkYearId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_op_cal = " + pk[0] + " "
                + "AND id_year = " + pk[1] + " ";
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
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mtYearStart = resultSet.getDate("year_sta");
            mtYearEnd = resultSet.getDate("year_end");
            
            // read as well children:
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                msSql = "SELECT id_month "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_OP_CAL_YEAR_MONTH) + " "
                        + "WHERE id_op_cal = " + mnPkOpCalendarId + " AND id_year = " + mnPkYearId + " "
                        + "ORDER BY id_month;";

                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    SDbOpCalendarYearMonth month = new SDbOpCalendarYearMonth();
                    month.read(session, new int[] { mnPkOpCalendarId, mnPkYearId, resultSet.getInt("id_month") });
                    maChildMonths.add(month);
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
        
        verifyRegistryNew(session);
        
        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkOpCalendarId + ", " + 
                    mnPkYearId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtYearStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtYearEnd) + "' " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    /*
                    "id_op_cal = " + mnPkOpCalendarId + ", " +
                    "id_year = " + mnPkYearId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "year_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtYearStart) + "', " +
                    "year_end = '" + SLibUtils.DbmsDateFormatDate.format(mtYearEnd) + "' " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // save as well children:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.SU_OP_CAL_YEAR_MONTH) + " "
                + "WHERE id_op_cal = " + mnPkOpCalendarId + " AND id_year = " + mnPkYearId + ";";
        session.getStatement().execute(msSql);
        
        for (SDbOpCalendarYearMonth month : maChildMonths) {
            month.setPkOpCalendarId(mnPkOpCalendarId);
            month.setPkYearId(mnPkYearId);
            month.setPkMonthId(0);
            month.setRegistryNew(false);
            month.save(session);
        }
        
        // complete saving:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbOpCalendarYear clone() throws CloneNotSupportedException {
        SDbOpCalendarYear registry = new SDbOpCalendarYear();
        
        registry.setPkOpCalendarId(this.getPkOpCalendarId());
        registry.setPkYearId(this.getPkYearId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setYearStart(this.getYearStart());
        registry.setYearEnd(this.getYearEnd());
        
        // clone as well children:
        
        for (SDbOpCalendarYearMonth month : this.getChildMonths()) {
            registry.getChildMonths().add(month.clone());
        }
        
        // complete clonning:

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
