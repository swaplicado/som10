/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

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

/**
 *
 * @author Edwin Carmona
 */
public class SDbStockRecord extends SDbRegistryUser {

    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkCompanyId;
    protected int mnPkBranchId;
    protected int mnPkWarehouseId;
    protected int mnPkRecordId;
    protected Date mtDate;
    protected double mdStock;
    protected boolean mbPremises;

    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    
    protected int mnFkOilClassId_n;
    protected int mnFkOilTypeId_n;
    protected int mnFkOilOwnerId_n;
    protected int mnFkOilAcidity_n;
    protected int mnFkOilAcidityEntry_n;

    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    * */

    public SDbStockRecord() {
        super(SModConsts.S_STK_RECORD);
        initRegistry();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setPkRecordId(int n) { mnPkRecordId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setStock(double d) { mdStock = d; }
    public void setPremises(boolean b) { mbPremises = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkOilClassId_n(int n) { mnFkOilClassId_n = n; }
    public void setFkOilTypeId_n(int n) { mnFkOilTypeId_n = n; }
    public void setFkOilOwnerId_n(int n) { mnFkOilOwnerId_n = n; }
    public void setFkOilAcidity_n(int n) { mnFkOilAcidity_n = n; }
    public void setFkOilAcidityEntry_n(int n) { mnFkOilAcidityEntry_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public int getPkRecordId() { return mnPkRecordId; }
    public Date getDate() { return mtDate; }
    public double getStock() { return mdStock; }
    public boolean isPremises() { return mbPremises; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkOilClassId_n() { return mnFkOilClassId_n; }
    public int getFkOilTypeId_n() { return mnFkOilTypeId_n; }
    public int getFkOilOwnerId_n() { return mnFkOilOwnerId_n; }
    public int getFkOilAcidity_n() { return mnFkOilAcidity_n; }
    public int getFkOilAcidityEntry_n() { return mnFkOilAcidityEntry_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setPrimaryKey(Object[] pk) {
        mnPkItemId = (Integer) pk[0];
        mnPkUnitId = (Integer) pk[1];
        mnPkCompanyId = (Integer) pk[2];
        mnPkBranchId = (Integer) pk[3];
        mnPkWarehouseId = (Integer) pk[4];
        mnPkRecordId = (Integer) pk[5];
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkCompanyId = 0;
        mnPkBranchId = 0;
        mnPkWarehouseId = 0;
        mnPkRecordId = 0;
        mtDate = null;
        mdStock = 0;
        mbPremises = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkOilClassId_n = 0;
        mnFkOilTypeId_n = 0;
        mnFkOilOwnerId_n = 0;
        mnFkOilAcidity_n = 0;
        mnFkOilAcidityEntry_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId = pk[0];
        mnPkUnitId = pk[1];
        mnPkCompanyId = pk[2];
        mnPkBranchId = pk[3];
        mnPkWarehouseId = pk[5];
        mnPkRecordId = pk[6];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkUnitId, mnPkCompanyId, mnPkBranchId, mnPkWarehouseId, mnPkRecordId };
    }
    
    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRecordId = 0;

        msSql = "SELECT COALESCE(MAX(id_rec), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_item = " + mnPkItemId + " AND id_unit =  " + mnPkUnitId + 
                    " AND id_co = " + mnPkCompanyId + " AND id_cob = " + mnPkBranchId + 
                    " AND id_wah = " + mnPkWarehouseId;
        
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRecordId = resultSet.getInt(1);
        }
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_item = " + mnPkItemId + " AND id_unit =  " + mnPkUnitId + 
                    " AND id_co = " + mnPkCompanyId + " AND id_cob = " + mnPkBranchId + 
                    " AND id_wah = " + mnPkWarehouseId + " AND id_rec = " + mnPkRecordId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_item = " + pk[0] + " AND id_unit =  " + pk[1] + 
                    " AND id_co = " + pk[2] + " AND id_cob = " + pk[3] + 
                    " AND id_wah = " + pk[4] + " AND id_rec = " + pk[5] + " ";
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
            mnPkItemId = resultSet.getInt("id_item");
            mnPkUnitId = resultSet.getInt("id_unit");
            mnPkCompanyId = resultSet.getInt("id_co");
            mnPkBranchId = resultSet.getInt("id_cob");
            mnPkWarehouseId = resultSet.getInt("id_wah");
            mnPkRecordId = resultSet.getInt("id_rec");
            mtDate = resultSet.getDate("dt");
            mdStock = resultSet.getDouble("stock");
            mbPremises = resultSet.getBoolean("b_premises");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkOilClassId_n = resultSet.getInt("fk_oil_cl_n");
            mnFkOilTypeId_n = resultSet.getInt("fk_oil_tp_n");
            mnFkOilOwnerId_n = resultSet.getInt("fk_oil_own_n");
            mnFkOilAcidity_n = resultSet.getInt("fk_oil_aci_n");
            mnFkOilAcidityEntry_n = resultSet.getInt("fk_oil_aci_ety_n");
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
            mbUpdatable = true;
            mbDisableable = false;
            mbDeletable = true;
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkItemId + ", " + 
                    mnPkUnitId + ", " + 
                    mnPkCompanyId + ", " + 
                    mnPkBranchId + ", " + 
                    mnPkWarehouseId + ", " + 
                    mnPkRecordId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    mdStock + ", " + 
                    (mbPremises ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    (mnFkOilClassId_n == SLibConsts.UNDEFINED ? null : mnFkOilClassId_n) + ", " +
                    (mnFkOilTypeId_n == SLibConsts.UNDEFINED ? null : mnFkOilTypeId_n) + ", " +
                    (mnFkOilOwnerId_n == SLibConsts.UNDEFINED ? null : mnFkOilOwnerId_n) + ", " +
                    (mnFkOilAcidity_n == SLibConsts.UNDEFINED ? null : mnFkOilAcidity_n) + ", " +
                    (mnFkOilAcidityEntry_n == SLibConsts.UNDEFINED ? null : mnFkOilAcidityEntry_n) + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_item = " + mnPkItemId + ", " +
//                    "id_unit = " + mnPkUnitId + ", " +
//                    "id_co = " + mnPkCompanyId + ", " +
//                    "id_cob = " + mnPkBranchId + ", " +
//                    "id_wah = " + mnPkWarehouseId + ", " +
//                    "id_rec = " + mnPkRecordId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "stock = " + mdStock + ", " +
                    "b_premises = " + (mbPremises ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_oil_cl_n = " + (mnFkOilClassId_n == SLibConsts.UNDEFINED ? null : mnFkOilClassId_n) + ", " +
                    "fk_oil_tp_n = " + (mnFkOilTypeId_n == SLibConsts.UNDEFINED ? null : mnFkOilTypeId_n) + ", " +
                    "fk_oil_own_n = " + (mnFkOilOwnerId_n == SLibConsts.UNDEFINED ? null : mnFkOilOwnerId_n) + ", " +
                    "fk_oil_aci_n = " + (mnFkOilAcidity_n == SLibConsts.UNDEFINED ? null : mnFkOilAcidity_n) + ", " +
                    "fk_oil_aci_ety_n = " + (mnFkOilAcidityEntry_n == SLibConsts.UNDEFINED ? null : mnFkOilAcidityEntry_n) + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().getConnection().createStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbStockRecord clone() throws CloneNotSupportedException {
        SDbStockRecord registry = new SDbStockRecord();

        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setPkRecordId(this.getPkRecordId());
        registry.setDate(this.getDate());
        registry.setStock(this.getStock());
        registry.setPremises(this.isPremises());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkOilClassId_n(this.getFkOilClassId_n());
        registry.setFkOilTypeId_n(this.getFkOilTypeId_n());
        registry.setFkOilOwnerId_n(this.getFkOilOwnerId_n());
        registry.setFkOilAcidity_n(this.getFkOilAcidity_n());
        registry.setFkOilAcidityEntry_n(this.getFkOilAcidityEntry_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
