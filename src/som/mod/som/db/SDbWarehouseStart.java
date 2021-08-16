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
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 * Clase control de la tabla Almacén inicio (s_wah_start).
 * @author Isabel Servín
 */
public class SDbWarehouseStart extends SDbRegistryUser {
    
    protected int mnPkStartId;
    protected Date mtDateStart;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkWarehouseCompanyId;
    protected int mnFkWarehouseBranchId;
    protected int mnFkWarehouseWarehouseId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbWarehouseStart() {
        super(SModConsts.S_WAH_START);
        initRegistry();
    }
    
    /*
     * Private methods
     */
    
    /*
     * Public methods
     */
    
    public void setPkStartId(int n) { mnPkStartId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkWarehouseCompanyId(int n) { mnFkWarehouseCompanyId = n; }
    public void setFkWarehouseBranchId(int n) { mnFkWarehouseBranchId = n; }
    public void setFkWarehouseWarehouseId(int n) { mnFkWarehouseWarehouseId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkStartId() { return mnPkStartId; }
    public Date getDateStart() { return mtDateStart; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkWarehouseCompanyId() { return mnFkWarehouseCompanyId; }
    public int getFkWarehouseBranchId() { return mnFkWarehouseBranchId; }
    public int getFkWarehouseWarehouseId() { return mnFkWarehouseWarehouseId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkStartId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int [] { mnPkStartId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStartId = 0;
        mtDateStart = null;
        mbDeleted = false;
        mnFkWarehouseCompanyId = 0;
        mnFkWarehouseBranchId = 0;
        mnFkWarehouseWarehouseId = 0;
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
        return "WHERE id_start = " + mnPkStartId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_start = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkStartId = 0;
        
        msSql = "SELECT COALESCE (MAX(id_start), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStartId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        initRegistry();
        initQueryMembers();
        
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        ResultSet resultSet;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkStartId = resultSet.getInt("id_start");
            mtDateStart = resultSet.getDate("dt_start");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkWarehouseCompanyId = resultSet.getInt("fk_wah_co");
            mnFkWarehouseBranchId = resultSet.getInt("fk_wah_cob");
            mnFkWarehouseWarehouseId = resultSet.getInt("fk_wah_wah");
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
                    mnPkStartId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkWarehouseCompanyId + ", " + 
                    mnFkWarehouseBranchId + ", " + 
                    mnFkWarehouseWarehouseId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    "id_start = " + mnPkStartId + ", " +
                    "dt_start = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_wah_co = " + mnFkWarehouseCompanyId + ", " +
                    "fk_wah_cob = " + mnFkWarehouseBranchId + ", " +
                    "fk_wah_wah = " + mnFkWarehouseWarehouseId + ", " +
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
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbWarehouseStart registry = new SDbWarehouseStart();
        
        registry.setPkStartId(this.getPkStartId());
        registry.setDateStart(this.getDateStart());
        registry.setDeleted(this.isDeleted());
        registry.setFkWarehouseCompanyId(this.getFkWarehouseCompanyId());
        registry.setFkWarehouseBranchId(this.getFkWarehouseBranchId());
        registry.setFkWarehouseWarehouseId(this.getFkWarehouseWarehouseId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
