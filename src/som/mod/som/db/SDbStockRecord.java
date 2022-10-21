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
 * @author Edwin Carmona
 */
public class SDbStockRecord extends SDbRegistryUser {

    protected int mnPkCompanyId;
    protected int mnPkBranchId;
    protected int mnPkWarehouseId;
    protected Date mtPkDate;
    protected double mdStock;
    protected double mdAcidityPercentage;
    protected boolean mbPremises;

    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    
    protected int mnFkItemId;
    protected int mnFkUnit;
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
        super(SModConsts.S_STK);
        initRegistry();
    }

    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setPkDate(Date t) { mtPkDate = t; }
    public void setStock(double d) { mdStock = d; }
    public void setAcidityPercentage(double d) { mdAcidityPercentage = d; }
    public void setPremises(boolean b) { mbPremises = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnit(int n) { mnFkUnit = n; }
    public void setFkOilClassId_n(int n) { mnFkOilClassId_n = n; }
    public void setFkOilTypeId_n(int n) { mnFkOilTypeId_n = n; }
    public void setFkOilOwnerId_n(int n) { mnFkOilOwnerId_n = n; }
    public void setFkOilAcidity_n(int n) { mnFkOilAcidity_n = n; }
    public void setFkOilAcidityEntry_n(int n) { mnFkOilAcidityEntry_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public Date getPkDate() { return mtPkDate; }
    public double getStock() { return mdStock; }
    public double getAcidityPercentage() { return mdAcidityPercentage; }
    public boolean isPremises() { return mbPremises; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnit() { return mnFkUnit; }
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
        mnPkCompanyId = (Integer) pk[0];
        mnPkBranchId =  (Integer)  pk[1];
        mnPkWarehouseId = (Integer) pk[2];
        mtPkDate = (Date) pk[3];
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCompanyId = 0;
        mnPkBranchId = 0;
        mnPkWarehouseId = 0;
        mtPkDate = null;
        mdStock = 0;
        mdAcidityPercentage = 0;
        mbPremises = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkItemId = 0;
        mnFkUnit = 0;
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
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_co = " + mnPkCompanyId + " AND id_cob =  " + mnPkBranchId + " AND id_wah = " + mnPkWarehouseId + " AND id_dt = '" + SLibUtils.DbmsDateFormatDate.format(mtPkDate) + "' ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_co = " + pk[0] + " AND id_cob =  " + pk[1] + " AND id_wah = " + pk[2] + " AND id_dt = '" + SLibUtils.DbmsDateFormatDate.format(pk[3]) + "' ";
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
            mtPkDate = resultSet.getDate("id_dt");
            mdStock = resultSet.getDouble("stock");
            mdAcidityPercentage = resultSet.getDouble("aci_per");
            mbPremises = resultSet.getBoolean("b_premises");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnit = resultSet.getInt("fk_unit");
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
            computePrimaryKey(session);
            mbUpdatable = true;
            mbDisableable = false;
            mbDeletable = true;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkCompanyId + ", " + 
                    mnPkBranchId + ", " + 
                    mnPkWarehouseId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtPkDate) + "', " + 
                    mdStock + ", " + 
                    mdAcidityPercentage + ", " + 
                    (mbPremises ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUnit + ", " + 
                    mnFkOilClassId_n + ", " + 
                    mnFkOilTypeId_n + ", " + 
                    mnFkOilOwnerId_n + ", " + 
                    mnFkOilAcidity_n + ", " + 
                    mnFkOilAcidityEntry_n + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_co = " + mnPkCompanyId + ", " +
//                    "id_cob = " + mnPkBranchId + ", " +
//                    "id_wah = " + mnPkWarehouseId + ", " +
//                    "id_dt = '" + SLibUtils.DbmsDateFormatDate.format(mtPkDate) + "', " +
                    "stock = " + mdStock + ", " +
                    "aci_per = " + mdAcidityPercentage + ", " +
                    "b_premises = " + (mbPremises ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnit + ", " +
                    "fk_oil_cl_n = " + mnFkOilClassId_n + ", " +
                    "fk_oil_tp_n = " + mnFkOilTypeId_n + ", " +
                    "fk_oil_own_n = " + mnFkOilOwnerId_n + ", " +
                    "fk_oil_aci_n = " + mnFkOilAcidity_n + ", " +
                    "fk_oil_aci_ety_n = " + mnFkOilAcidityEntry_n + ", " +
//                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbStockRecord clone() throws CloneNotSupportedException {
        SDbStockRecord registry = new SDbStockRecord();

        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setPkDate(this.getPkDate());
        registry.setStock(this.getStock());
        registry.setAcidityPercentage(this.getAcidityPercentage());
        registry.setPremises(this.isPremises());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnit(this.getFkUnit());
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

    @Override
    public void setPrimaryKey(int[] ints) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void computePrimaryKey(SGuiSession sgs) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
