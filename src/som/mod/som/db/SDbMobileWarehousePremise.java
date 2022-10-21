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
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SDbMobileWarehousePremise extends SDbRegistryUser implements SGridRow {

    protected int mnPkCompanyId;
    protected int mnPkBranchId;
    protected int mnPkWarehouseId;
    protected Date mtPkDate;
    protected boolean mbPremises;
    
    protected String msAuxWhsCode;
    protected String msAuxWhsName;

    public SDbMobileWarehousePremise() {
        super(SModConsts.S_WAH_PREMISE);
        initRegistry();
    }

    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setPkDate(Date t) { mtPkDate = t; }
    public void setPremises(boolean b) { mbPremises = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxWarehouseCode(String s) { msAuxWhsCode = s; }
    public void setAuxWarehouseName(String s) { msAuxWhsName = s; }

    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public Date getPkDate() { return mtPkDate; }
    public boolean isPremises() { return mbPremises; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public String getAuxWarehouseCode() { return msAuxWhsCode; }
    public String getAuxWarehouseName() { return msAuxWhsName; }

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
        mbPremises = false;
        mbDeleted = false;
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
        return "WHERE wahp.id_co = " + mnPkCompanyId + " AND wahp.id_cob =  " + mnPkBranchId + " AND wahp.id_wah = " + mnPkWarehouseId + " AND wahp.id_dt = '" + SLibUtils.DbmsDateFormatDate.format(mtPkDate) + "' ";
    }

    public String getSqlWhere(Object[] pk) {
        return "WHERE wahp.id_co = " + pk[0] + " AND wahp.id_cob =  " + pk[1] + " AND wahp.id_wah = " + pk[2] + " AND wahp.id_dt = '" + SLibUtils.DbmsDateFormatDate.format(pk[3]) + "' ";
    }

    public void read(SGuiSession session, Object[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT "
                + " wahp.*, "
                + " wah.code, "
                + " wah.name "
                + "FROM "
                + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah, "
                + SModConsts.TablesMap.get(SModConsts.S_WAH_PREMISE) + " AS wahp "
                + getSqlWhere(pk) + " AND wah.id_co = wahp.id_co "
                + " AND wah.id_cob = wahp.id_cob "
                + " AND wah.id_wah = wahp.id_wah "
                + " AND NOT wah.b_del AND NOT wahp.b_del ";
        
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkCompanyId = resultSet.getInt("id_co");
            mnPkBranchId = resultSet.getInt("id_cob");
            mnPkWarehouseId = resultSet.getInt("id_wah");
            mtPkDate = resultSet.getDate("id_dt");
            mbPremises = resultSet.getBoolean("b_premises");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            msAuxWhsCode = resultSet.getString("code");
            msAuxWhsName = resultSet.getString("name");

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
//            computePrimaryKey(session);
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
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtPkDate) + "', " + 
                    (mbPremises ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " AS wahp SET " +
//                    "id_co = " + mnPkCompanyId + ", " +
//                    "id_cob = " + mnPkBranchId + ", " +
//                    "id_wah = " + mnPkWarehouseId + ", " +
//                    "id_dt = '" + SLibUtils.DbmsDateFormatDate.format(mtPkDate) + "', " +
                    "b_premises = " + (mbPremises ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
//                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMobileWarehousePremise clone() throws CloneNotSupportedException {
        SDbMobileWarehousePremise registry = new SDbMobileWarehousePremise();

        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setPkDate(this.getPkDate());
        registry.setPremises(this.isPremises());
        registry.setDeleted(this.isDeleted());
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
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkCompanyId, mnPkBranchId, mnPkWarehouseId };
    }

    @Override
    public String getRowCode() {
        return this.msAuxWhsCode;
    }

    @Override
    public String getRowName() {
        return this.msAuxWhsName;
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return true;
    }

    @Override
    public void setRowEdited(boolean bln) {
        
    }

    @Override
    public Object getRowValueAt(int i) {
        Object value = null;

        switch(i) {
            case 0:
                value = msAuxWhsCode;
                break;
            case 1:
                value = msAuxWhsName;
                break;
            case 2:
                value = mbPremises;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 2:
                mbPremises = (Boolean) value;
                break;
            
            default:
        }
    }

    @Override
    public void read(SGuiSession sgs, int[] ints) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSqlWhere(int[] ints) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
