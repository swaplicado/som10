/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.mat.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDbExwAdjustment extends SDbRegistryUser {
    
    public static final int NUM_LEN = 6;
    public static final String SERIES_IN = "E";
    public static final String SERIES_OUT = "S";

    protected int mnPkExwAdjustmentId;
    protected String msSeries;
    protected int mnNumber;
    protected Date mtDate;
    protected String msReference;
    protected String msNote;
    protected double mdQuantity;
    protected boolean mbAuthorized;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkIogCategoryId;
    protected int mnFkExwAdjustmentTypeId;
    protected int mnFkExwFacilityId;
    protected int mnFkScaleId;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected int mnFkUserAuthorizationId;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    protected Date mtTsUserAuthorization;
    
    protected Date mtOldDate;
    protected int mnOldFkUserAuthorizationId;

    public SDbExwAdjustment() {
        super(SModConsts.M_EXW_ADJ);
        initRegistry();
    }

    public void setPkExwAdjustmentId(int n) { mnPkExwAdjustmentId = n; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setReference(String s) { msReference = s; }
    public void setNote(String s) { msNote = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setAuthorized(boolean b) { mbAuthorized = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkIogCategoryId(int n) { mnFkIogCategoryId = n; }
    public void setFkExwAdjustmentTypeId(int n) { mnFkExwAdjustmentTypeId = n; }
    public void setFkExwFacilityId(int n) { mnFkExwFacilityId = n; }
    public void setFkScaleId(int n) { mnFkScaleId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setFkUserAuthorizationId(int n) { mnFkUserAuthorizationId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    public void setTsUserAuthorization(Date t) { mtTsUserAuthorization = t; }

    public int getPkExwAdjustmentId() { return mnPkExwAdjustmentId; }
    public String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public String getReference() { return msReference; }
    public String getNote() { return msNote; }
    public double getQuantity() { return mdQuantity; }
    public boolean isAuthorized() { return mbAuthorized; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkIogCategoryId() { return mnFkIogCategoryId; }
    public int getFkExwAdjustmentTypeId() { return mnFkExwAdjustmentTypeId; }
    public int getFkExwFacilityId() { return mnFkExwFacilityId; }
    public int getFkScaleId() { return mnFkScaleId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public int getFkUserAuthorizationId() { return mnFkUserAuthorizationId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    public Date getTsUserAuthorization() { return mtTsUserAuthorization; }

    public void setOldDate(Date t) { mtOldDate = t; }
    public void setOldFkUserAuthorizationId(int n) { mnOldFkUserAuthorizationId = n; }
    
    public Date getOldDate() { return mtOldDate; }
    public int getOldFkUserAuthorizationId() { return mnOldFkUserAuthorizationId; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkExwAdjustmentId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkExwAdjustmentId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkExwAdjustmentId = 0;
        msSeries = "";
        mnNumber = 0;
        mtDate = null;
        msReference = "";
        msNote = "";
        mdQuantity = 0;
        mbAuthorized = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkIogCategoryId = 0;
        mnFkExwAdjustmentTypeId = 0;
        mnFkExwFacilityId = 0;
        mnFkScaleId = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mnFkUserAuthorizationId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        mtTsUserAuthorization = null;
        
        mtOldDate = null;
        mnOldFkUserAuthorizationId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_exw_adj = " + mnPkExwAdjustmentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_exw_adj = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkExwAdjustmentId = 0;

        msSql = "SELECT COALESCE(MAX(id_exw_adj), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkExwAdjustmentId = resultSet.getInt(1);
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
            mnPkExwAdjustmentId = resultSet.getInt("id_exw_adj");
            msSeries = resultSet.getString("ser");
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            msReference = resultSet.getString("ref");
            msNote = resultSet.getString("note");
            mdQuantity = resultSet.getDouble("qty");
            mbAuthorized = resultSet.getBoolean("b_aut");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkIogCategoryId = resultSet.getInt("fk_iog_ct");
            mnFkExwAdjustmentTypeId = resultSet.getInt("fk_exw_adj_tp");
            mnFkExwFacilityId = resultSet.getInt("fk_exw_fac");
            mnFkScaleId = resultSet.getInt("fk_sca");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mnFkUserAuthorizationId = resultSet.getInt("fk_usr_aut");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            mtTsUserAuthorization = resultSet.getTimestamp("ts_usr_aut");
            
            // Preserve original values:
            
            mtOldDate = mtDate;
            mnOldFkUserAuthorizationId = mnFkUserAuthorizationId;

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
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            mnFkUserAuthorizationId = mbAuthorized ? session.getUser().getPkUserId() : SUtilConsts.USR_NA_ID;
            
            switch (mnFkIogCategoryId) {
                case SModSysConsts.SS_IOG_CT_IN:
                    msSeries = SERIES_IN;
                    break;
                case SModSysConsts.SS_IOG_CT_OUT:
                    msSeries = SERIES_OUT;
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            mnNumber = getNextNumber(session, msSeries);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkExwAdjustmentId + ", " + 
                    "'" + msSeries + "', " + 
                    mnNumber + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    "'" + msReference + "', " + 
                    "'" + msNote + "', " + 
                    mdQuantity + ", " + 
                    (mbAuthorized ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkIogCategoryId + ", " + 
                    mnFkExwAdjustmentTypeId + ", " + 
                    mnFkExwFacilityId + ", " + 
                    mnFkScaleId + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUnitId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    mnFkUserAuthorizationId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            boolean isAuthInProgress = mnFkUserAuthorizationId != mnOldFkUserAuthorizationId;
            
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_exw_adj = " + mnPkExwAdjustmentId + ", " +
                    "ser = '" + msSeries + "', " +
                    "num = " + mnNumber + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "ref = '" + msReference + "', " +
                    "note = '" + msNote + "', " +
                    "qty = " + mdQuantity + ", " +
                    "b_aut = " + (mbAuthorized ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_iog_ct = " + mnFkIogCategoryId + ", " +
                    "fk_exw_adj_tp = " + mnFkExwAdjustmentTypeId + ", " +
                    "fk_exw_fac = " + mnFkExwFacilityId + ", " +
                    "fk_sca = " + mnFkScaleId + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    (isAuthInProgress ? "fk_usr_aut = " + mnFkUserAuthorizationId + ", " : "") +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    (isAuthInProgress ? ", ts_usr_aut = " + "NOW()" + " " : "") +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        return !mbSystem && (mbRegistryNew || (!mbRegistryNew && !mbDeleted)); // prevent deleted registries to be recovered!
    }

    @Override
    public SDbExwAdjustment clone() throws CloneNotSupportedException {
        SDbExwAdjustment registry = new SDbExwAdjustment();

        registry.setPkExwAdjustmentId(this.getPkExwAdjustmentId());
        registry.setSeries(this.getSeries());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setReference(this.getReference());
        registry.setNote(this.getNote());
        registry.setQuantity(this.getQuantity());
        registry.setAuthorized(this.isAuthorized());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkIogCategoryId(this.getFkIogCategoryId());
        registry.setFkExwAdjustmentTypeId(this.getFkExwAdjustmentTypeId());
        registry.setFkExwFacilityId(this.getFkExwFacilityId());
        registry.setFkScaleId(this.getFkScaleId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setFkUserAuthorizationId(this.getFkUserAuthorizationId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        registry.setTsUserAuthorization(this.getTsUserAuthorization());
        
        registry.setOldDate(this.getOldDate());
        registry.setOldFkUserAuthorizationId(this.getOldFkUserAuthorizationId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public String getFolio() {
        return (msSeries.isEmpty() ? "" : msSeries + "-") + SLibUtils.DecimalNumberFormat.format(mnNumber);
    }
    
    public static int getNextNumber(final SGuiSession session, final String series) throws SQLException, Exception {
        int nextNumber = 0;

        String sql = "SELECT COALESCE(MAX(num), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.M_EXW_ADJ) + " "
                + "WHERE NOT b_del AND ser = '" + series + "';";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                nextNumber = resultSet.getInt(1);
            }
        }
        
        return nextNumber;
    }
}
