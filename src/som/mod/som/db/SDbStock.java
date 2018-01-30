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
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbStock extends SDbRegistryUser {

    protected int mnPkYearId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkCompanyId;
    protected int mnPkBranchId;
    protected int mnPkWarehouseId;
    protected int mnPkDivisionId;
    protected int mnPkMoveId;
    protected Date mtDate;
    protected double mdMoveIn;
    protected double mdMoveOut;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkIogCategoryId;
    protected int mnFkIogClassId;
    protected int mnFkIogTypeId;
    protected int mnFkIogId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    * */

    public SDbStock() {
        super(SModConsts.S_STK);
        initRegistry();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setPkDivisionId(int n) { mnPkDivisionId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setMoveIn(double d) { mdMoveIn = d; }
    public void setMoveOut(double d) { mdMoveOut = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkIogCategoryId(int n) { mnFkIogCategoryId = n; }
    public void setFkIogClassId(int n) { mnFkIogClassId = n; }
    public void setFkIogTypeId(int n) { mnFkIogTypeId = n; }
    public void setFkIogId(int n) { mnFkIogId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public int getPkDivisionId() { return mnPkDivisionId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public Date getDate() { return mtDate; }
    public double getMoveIn() { return mdMoveIn; }
    public double getMoveOut() { return mdMoveOut; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkIogCategoryId() { return mnFkIogCategoryId; }
    public int getFkIogClassId() { return mnFkIogClassId; }
    public int getFkIogTypeId() { return mnFkIogTypeId; }
    public int getFkIogId() { return mnFkIogId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkItemId = pk[1];
        mnPkUnitId = pk[2];
        mnPkCompanyId = pk[3];
        mnPkBranchId = pk[4];
        mnPkWarehouseId = pk[5];
        mnPkDivisionId = pk[6];
        mnPkMoveId = pk[7];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkItemId, mnPkUnitId, mnPkCompanyId, mnPkBranchId, mnPkWarehouseId, mnPkDivisionId, mnPkMoveId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkYearId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkCompanyId = 0;
        mnPkBranchId = 0;
        mnPkDivisionId = 0;
        mnPkWarehouseId = 0;
        mnPkMoveId = 0;
        mtDate = null;
        mdMoveIn = 0;
        mdMoveOut = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkIogCategoryId = 0;
        mnFkIogClassId = 0;
        mnFkIogTypeId = 0;
        mnFkIogId = 0;
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
        return "WHERE id_year = " + mnPkYearId + " AND id_item =  " + mnPkItemId + " AND id_unit = " + mnPkUnitId + " AND id_co = " + mnPkCompanyId +
                " AND id_cob = " + mnPkBranchId + " AND id_wah = " + mnPkWarehouseId + " AND id_div = " + mnPkDivisionId + " AND id_mov = " + mnPkMoveId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " AND id_item =  " + pk[1] + " AND id_unit = " + pk[2] + " AND id_co = " + pk[3] +
                " AND id_cob = " + pk[4] + " AND id_wah = " + pk[5] + " AND id_div = " + pk[6] + " AND id_mov = " + pk[7] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMoveId = 0;

        msSql = "SELECT COALESCE(MAX(id_mov), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMoveId = resultSet.getInt(1);
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
            mnPkYearId = resultSet.getInt("id_year");
            mnPkItemId = resultSet.getInt("id_item");
            mnPkUnitId = resultSet.getInt("id_unit");
            mnPkCompanyId = resultSet.getInt("id_co");
            mnPkBranchId = resultSet.getInt("id_cob");
            mnPkWarehouseId = resultSet.getInt("id_wah");
            mnPkDivisionId = resultSet.getInt("id_div");
            mnPkMoveId = resultSet.getInt("id_mov");
            mtDate = resultSet.getDate("dt");
            mdMoveIn = resultSet.getDouble("mov_in");
            mdMoveOut = resultSet.getDouble("mov_out");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkIogCategoryId = resultSet.getInt("fk_iog_ct");
            mnFkIogClassId = resultSet.getInt("fk_iog_cl");
            mnFkIogTypeId = resultSet.getInt("fk_iog_tp");
            mnFkIogId = resultSet.getInt("fk_iog");
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
                    mnPkYearId + ", " +
                    mnPkItemId + ", " +
                    mnPkUnitId + ", " +
                    mnPkCompanyId + ", " +
                    mnPkBranchId + ", " +
                    mnPkWarehouseId + ", " +
                    mnPkDivisionId + ", " +
                    mnPkMoveId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mdMoveIn + ", " +
                    mdMoveOut + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkIogCategoryId + ", " +
                    mnFkIogClassId + ", " +
                    mnFkIogTypeId + ", " +
                    mnFkIogId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_year = " + mnPkYearId + ", " +
                    //"id_item = " + mnPkItemId + ", " +
                    //"id_unit = " + mnPkUnitId + ", " +
                    //"id_co = " + mnPkCompanyId + ", " +
                    //"id_cob = " + mnPkBranchId + ", " +
                    //"id_wah = " + mnPkWarehouseId + ", " +
                    //"id_div = " + mnPkDivisionId + ", " +
                    //"id_mov = " + mnPkMoveId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "mov_in = " + mdMoveIn + ", " +
                    "mov_out = " + mdMoveOut + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_iog_ct = " + mnFkIogCategoryId + ", " +
                    "fk_iog_cl = " + mnFkIogClassId + ", " +
                    "fk_iog_tp = " + mnFkIogTypeId + ", " +
                    "fk_iog = " + mnFkIogId + ", " +
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
    public SDbStock clone() throws CloneNotSupportedException {
        SDbStock registry = new SDbStock();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setPkDivisionId(this.getPkDivisionId());
        registry.setPkMoveId(this.getPkMoveId());
        registry.setDate(this.getDate());
        registry.setMoveIn(this.getMoveIn());
        registry.setMoveOut(this.getMoveOut());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkIogCategoryId(this.getFkIogCategoryId());
        registry.setFkIogClassId(this.getFkIogClassId());
        registry.setFkIogTypeId(this.getFkIogTypeId());
        registry.setFkIogId(this.getFkIogId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
