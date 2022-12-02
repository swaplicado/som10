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
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Isabel Servín
 */
public class SDbStockReport extends SDbRegistryUser {
    
    protected int mnPkStockReportId;
    protected Date mtDate;
    protected double mdMixingPercentage;
    protected boolean mbDone;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkMixingWarehouseCompanyId;
    protected int mnFkMixingWarehouseBranchId;
    protected int mnFkMixingWarehouseWarehouseId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbProcessingRawMaterials> maProcessingRawMaterials;
    protected ArrayList<SDbConsumableRecord> maConsumableRecords;

    public SDbStockReport() {
        super(SModConsts.S_STK_REPORT);
        initRegistry();
    }
    
    public void setPkStockReportId(int n) { mnPkStockReportId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setMixingPercentage(double d) { mdMixingPercentage = d; }
    public void setDone(boolean b) { mbDone = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkMixingWarehouseCompanyId(int n) { mnFkMixingWarehouseCompanyId = n; }
    public void setFkMixingWarehouseBranchId(int n) { mnFkMixingWarehouseBranchId = n; }
    public void setFkMixingWarehouseWarehouseId(int n) { mnFkMixingWarehouseWarehouseId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkStockReportId() { return mnPkStockReportId; }
    public Date getDate() { return mtDate; }
    public double getMixingPercentage() { return mdMixingPercentage; }
    public boolean isDone() { return mbDone; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkMixingWarehouseCompanyId() { return mnFkMixingWarehouseCompanyId; }
    public int getFkMixingWarehouseBranchId() { return mnFkMixingWarehouseBranchId; }
    public int getFkMixingWarehouseWarehouseId() { return mnFkMixingWarehouseWarehouseId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public ArrayList<SDbProcessingRawMaterials> getProcessingRawMaterials() { return maProcessingRawMaterials; }
    public ArrayList<SDbConsumableRecord> getConsumableRecords() { return maConsumableRecords; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkStockReportId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockReportId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockReportId = 0;
        mtDate = null;
        mdMixingPercentage = 0;
        mbDone = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkMixingWarehouseCompanyId = 0;
        mnFkMixingWarehouseBranchId = 0;
        mnFkMixingWarehouseWarehouseId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maProcessingRawMaterials = new ArrayList<>();
        maConsumableRecords = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_report = " + mnPkStockReportId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_report = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkStockReportId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_stk_report), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockReportId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkStockReportId = resultSet.getInt("id_stk_report");
            mtDate = resultSet.getDate("dt");
            mdMixingPercentage = resultSet.getDouble("mix_per");
            mbDone = resultSet.getBoolean("b_done");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkMixingWarehouseCompanyId = resultSet.getInt("fk_mix_wah_co");
            mnFkMixingWarehouseBranchId = resultSet.getInt("fk_mix_wah_cob");
            mnFkMixingWarehouseWarehouseId = resultSet.getInt("fk_mix_wah_wah");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read aswell child registries:
            
            statement = session.getStatement().getConnection().createStatement();
            
            maProcessingRawMaterials = new ArrayList<>();
            msSql = "SELECT id_prc_raw_mat FROM " + SModConsts.TablesMap.get(SModConsts.S_PRC_RAW_MAT) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbProcessingRawMaterials rawMaterials = new SDbProcessingRawMaterials();
                rawMaterials.read(session, new int[] { mnPkStockReportId, resultSet.getInt(1) });
                maProcessingRawMaterials.add(rawMaterials);
            }
            
            maConsumableRecords = new ArrayList<>();
            msSql = "SELECT id_cons_record FROM " + SModConsts.TablesMap.get(SModConsts.S_CONS_RECORD) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbConsumableRecord consRec = new SDbConsumableRecord();
                consRec.read(session, new int[] { mnPkStockReportId, resultSet.getInt(1) });
                maConsumableRecords.add(consRec);
            }

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
                    mnPkStockReportId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mdMixingPercentage + ", " + 
                    (mbDone ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkMixingWarehouseCompanyId + ", " + 
                    mnFkMixingWarehouseBranchId + ", " + 
                    mnFkMixingWarehouseWarehouseId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_stk_report = " + mnPkStockReportId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "mix_per = " + mdMixingPercentage + ", " +
                    "b_done = " + (mbDone ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_mix_wah_co = " + mnFkMixingWarehouseCompanyId + ", " +
                    "fk_mix_wah_cob = " + mnFkMixingWarehouseBranchId + ", " +
                    "fk_mix_wah_wah = " + mnFkMixingWarehouseWarehouseId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Save aswell child registries:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.S_PRC_RAW_MAT) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        
        for (SDbProcessingRawMaterials rawMat : maProcessingRawMaterials) {
            rawMat.setPkStockReportId(mnPkStockReportId);
            rawMat.setRegistryNew(true);
            rawMat.save(session);
        }
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.S_CONS_RECORD) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        
        for (SDbConsumableRecord consRec : maConsumableRecords) {
            consRec.setPkStockReportId(mnPkStockReportId);
            consRec.setRegistryNew(true);
            consRec.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbStockReport clone() throws CloneNotSupportedException {
        SDbStockReport registry = new SDbStockReport();
        
        registry.setPkStockReportId(this.getPkStockReportId());
        registry.setDate(this.getDate());
        registry.setDone(this.isDone());
        registry.setMixingPercentage(this.getMixingPercentage());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkMixingWarehouseCompanyId(this.getFkMixingWarehouseCompanyId());
        registry.setFkMixingWarehouseBranchId(this.getFkMixingWarehouseBranchId());
        registry.setFkMixingWarehouseWarehouseId(this.getFkMixingWarehouseWarehouseId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbProcessingRawMaterials rawMat : this.getProcessingRawMaterials()) {
            registry.getProcessingRawMaterials().add(rawMat.clone());
        }
        
        for (SDbConsumableRecord consRec : this.getConsumableRecords()) {
            registry.getConsumableRecords().add(consRec.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }    
}
