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
 * @author Juan Barajas, Sergio Flores
 */
public class SDbSeason extends SDbRegistryUser {

    protected int mnPkSeasonId;
    protected String msCode;
    protected String msName;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected boolean mbClosed;
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

    public SDbSeason() {
        super(SModConsts.SU_SEAS);
        initRegistry();
    }

    /*
     * Private methods:
     */
    
    /**
     * Checks if current code does not already exist.
     */
    private boolean existsCode(final SGuiSession session) throws Exception {
        ResultSet resultSet = null;
        boolean exists = false;

        msSql = "SELECT COUNT(*) FROM " + getSqlTable() + " " +
                "WHERE code = '" + msCode + "' AND id_seas <> " + mnPkSeasonId;

        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            exists = resultSet.getInt(1) > 0;
        }

        return exists;
    }

    /**
     * Checks if current name does not already exist.
     */
    private boolean existsName(final SGuiSession session) throws Exception {
        ResultSet resultSet = null;
        boolean exists = false;

        msSql = "SELECT COUNT(*) FROM " + getSqlTable() + " " +
                "WHERE name = '" + msName + "' AND id_seas <> " + mnPkSeasonId;

        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            exists = resultSet.getInt(1) > 0;
        }

        return exists;
    }

    /**
     * Checks if there is not unpaid tickets in this season.
     */
    private boolean canCloseSeason(SGuiSession session) throws Exception {
        boolean canClose = true;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
                "WHERE b_del = 0 AND b_pay = 0 AND fk_seas_n = " + mnPkSeasonId ;

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            canClose = resultSet.getInt(1) == 0;    // check if there is not unpaid tickets
        }

        return canClose;
    }
    
    /*
     * Public methods:
     */
    
    public void setPkSeasonId(int n) { mnPkSeasonId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setClosed(boolean b) { mbClosed = b; }
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

    public int getPkSeasonId() { return mnPkSeasonId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public boolean isClosed() { return mbClosed; }
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

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSeasonId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSeasonId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSeasonId = 0;
        msCode = "";
        msName = "";
        mtDateStart = null;
        mtDateEnd = null;
        mbClosed = false;
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
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_seas = " + mnPkSeasonId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_seas = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSeasonId = 0;

        msSql = "SELECT COALESCE(MAX(id_seas), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSeasonId = resultSet.getInt(1);
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
            mnPkSeasonId = resultSet.getInt("id_seas");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd = resultSet.getDate("dt_end");
            mbClosed = resultSet.getBoolean("b_clo");
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

            // Finish registry reading:

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
                    mnPkSeasonId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    (mbClosed ? 1 : 0) + ", " +
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
                    //"id_seas = " + mnPkSeasonId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "b_clo = " + (mbClosed ? 1 : 0) + ", " +
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

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbSeason clone() throws CloneNotSupportedException {
        SDbSeason registry = new SDbSeason();

        registry.setPkSeasonId(this.getPkSeasonId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setClosed(this.isClosed());
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

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);

        if (can) {
            initQueryMembers();

            if (mbClosed) {
                can = canCloseSeason(session);

                if (!can) {
                    msQueryResult = "No puede cerrar la temporada '" + msCode + "' porque hay boletos sin pagar.";
                }
            }

            if (can && existsCode(session)) {
                can = false;
                msQueryResult = "¡El código ya existe!";
            }
            else if (existsName(session)) {
                can = false;
                msQueryResult = "¡El nombre ya existe!";
            }
        }

        return can;
    }
}
