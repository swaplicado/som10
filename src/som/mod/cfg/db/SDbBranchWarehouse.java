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
 *
 * @author Sergio Flores, Edwin Carmona
 */
public class SDbBranchWarehouse extends SDbRegistryUser {

    protected int mnPkCompanyId;
    protected int mnPkBranchId;
    protected int mnPkWarehouseId;
    protected String msCode;
    protected String msName;
    protected double mdDimensionBase;
    protected double mdDimensionHeight;
    protected double mdCapacityRealLiter;
    protected double mdVolumeAdjustLiter;
    protected double mdAcidity;
    protected String msNote;
    protected boolean mbMobile;

    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkWarehouseTypeId;
    protected int mnFkOrientationId;
    protected int mnFkVolumeCalculationId;
    protected int mnFkProductionLineId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbBranchWarehouse() {
        super(SModConsts.CU_WAH);
        initRegistry();
    }

    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDimensionBase(double d) { mdDimensionBase = d; }
    public void setDimensionHeight(double d) { mdDimensionHeight = d; }
    public void setCapacityRealLiter(double d) { mdCapacityRealLiter = d; }
    public void setVolumeAdjustLiter(double d) { mdVolumeAdjustLiter = d; }
    public void setAcidity(double d) { mdAcidity = d; }
    public void setNote(String s) { msNote = s; }
    public void setMobile(boolean b) { mbMobile = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkWarehouseTypeId(int n) { mnFkWarehouseTypeId = n; }
    public void setFkOrientationId(int n) { mnFkOrientationId = n; }
    public void setFkVolumeCalculationId(int n) { mnFkVolumeCalculationId = n; }
    public void setFkProductionLineId(int n) { mnFkProductionLineId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public double getDimensionBase() { return mdDimensionBase; }
    public double getDimensionHeight() { return mdDimensionHeight; }
    public double getCapacityRealLiter() { return mdCapacityRealLiter; }
    public double getVolumeAdjustLiter() { return mdVolumeAdjustLiter; }
    public double getAcidity() { return mdAcidity; }
    public String getNote() { return msNote; }
    public boolean isMobile() { return mbMobile; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkWarehouseTypeId() { return mnFkWarehouseTypeId; }
    public int getFkOrientationId() { return mnFkOrientationId; }
    public int getFkVolumeCalculationId() { return mnFkVolumeCalculationId; }
    public int getFkProductionLineId() { return mnFkProductionLineId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCompanyId = pk[0];
        mnPkBranchId = pk[1];
        mnPkWarehouseId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCompanyId, mnPkBranchId, mnPkWarehouseId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCompanyId = 0;
        mnPkBranchId = 0;
        mnPkWarehouseId = 0;
        msCode = "";
        msName = "";
        mdDimensionBase = 0;
        mdDimensionHeight = 0;
        mdCapacityRealLiter = 0;
        mdVolumeAdjustLiter = 0;
        mdAcidity = 0;
        msNote = "";
        mbMobile = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkWarehouseTypeId = 0;
        mnFkOrientationId = 0;
        mnFkVolumeCalculationId = 0;
        mnFkProductionLineId = 0;
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
        return "WHERE id_co = " + mnPkCompanyId + " AND " +
                "id_cob = " + mnPkBranchId + " AND " +
                "id_wah = " + mnPkWarehouseId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_co = " + pk[0] + " AND " +
                "id_cob = " + pk[1] + " AND " +
                "id_wah = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkWarehouseId = 0;

        msSql = "SELECT COALESCE(MAX(id_wah), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_co = " + mnPkCompanyId + " AND " +
                "id_cob = " + mnPkBranchId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkWarehouseId = resultSet.getInt(1);
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
            mnPkCompanyId = resultSet.getInt("id_co");
            mnPkBranchId = resultSet.getInt("id_cob");
            mnPkWarehouseId = resultSet.getInt("id_wah");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mdDimensionBase = resultSet.getDouble("dim_base");
            mdDimensionHeight = resultSet.getDouble("dim_heig");
            mdCapacityRealLiter = resultSet.getDouble("cap_real_lt");
            mdVolumeAdjustLiter = resultSet.getDouble("vol_adj_lt");
            mdAcidity = resultSet.getDouble("acidity");
            msNote = resultSet.getString("note");
            mbMobile = resultSet.getBoolean("b_mobile");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkWarehouseTypeId = resultSet.getInt("fk_wah_tp");
            mnFkOrientationId = resultSet.getInt("fk_orient");
            mnFkVolumeCalculationId = resultSet.getInt("fk_vol_cal_tp");
            mnFkProductionLineId = resultSet.getInt("fk_line");
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
                    mnPkCompanyId + ", " +
                    mnPkBranchId + ", " +
                    mnPkWarehouseId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    mdDimensionBase + ", " +
                    mdDimensionHeight + ", " +
                    mdCapacityRealLiter + ", " +
                    mdVolumeAdjustLiter + ", " +
                    mdAcidity + ", " +
                    (msNote.equals("") ? "'-'" : msNote) + ", " +
                    (mbMobile ? 1 : 0) + ", " + 
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkWarehouseTypeId + ", " +
                    mnFkOrientationId + ", " + 
                    mnFkVolumeCalculationId + ", " + 
                    mnFkProductionLineId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_co = " + mnPkCompanyId + ", " +
                    //"id_cob = " + mnPkBranchId + ", " +
                    //"id_wah = " + mnPkWarehouseId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "dim_base = " + mdDimensionBase + ", " +
                    "dim_heig = " + mdDimensionHeight + ", " +
                    "cap_real_lt = " + mdCapacityRealLiter + ", " +
                    "vol_adj_lt = " + mdVolumeAdjustLiter + ", " +
                    "acidity = " + mdAcidity + ", " +
                    "note = " + (msNote.equals("") ? "'-'" : "'" + msNote + "'") + ", " +
                    "b_mobile = " + (mbMobile ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_wah_tp = " + mnFkWarehouseTypeId + ", " +
                    "fk_orient = " + mnFkOrientationId + ", " +
                    "fk_vol_cal_tp = " + mnFkVolumeCalculationId + ", " +
                    "fk_line = " + mnFkProductionLineId + ", " +
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
    public SDbBranchWarehouse clone() throws CloneNotSupportedException {
        SDbBranchWarehouse registry = new SDbBranchWarehouse();

        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDimensionBase(this.getDimensionBase());
        registry.setDimensionHeight(this.getDimensionHeight());
        registry.setCapacityRealLiter(this.getCapacityRealLiter());
        registry.setVolumeAdjustLiter(this.getVolumeAdjustLiter());
        registry.setAcidity(this.getAcidity());
        registry.setNote(this.getNote());
        registry.setMobile(this.isMobile());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkWarehouseTypeId(this.getFkWarehouseTypeId());
        registry.setFkOrientationId(this.getFkOrientationId());
        registry.setFkVolumeCalculationId(this.getFkVolumeCalculationId());
        registry.setFkProductionLineId(this.getFkProductionLineId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
