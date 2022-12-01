/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Isabel Servín
 */
public class SDbConsumableRecord extends SDbRegistryUser {

    protected int mnPkStockReportId;
    protected int mnPkConsumableRecordId;
    protected double mdMeasure;
    protected double mdVolume;
    protected int mnFkConsumableWahId;
    
    protected SDbConsumableWarehouse moDbmsConsumableWarehouse;

    public SDbConsumableRecord() {
        super(SModConsts.S_CONS_RECORD);
    }
    
    public void setPkStockReportId(int n) { mnPkStockReportId = n; }
    public void setPkConsumableRecordId(int n) { mnPkConsumableRecordId = n; }
    public void setMeasure(double d) { mdMeasure = d; }
    public void setVolume(double d) { mdVolume = d; }
    public void setFkConsumableWahId(int n) { mnFkConsumableWahId = n; }

    public int getPkStockReportId() { return mnPkStockReportId; }
    public int getPkConsumableRecordId() { return mnPkConsumableRecordId; }
    public double getMeasure() { return mdMeasure; }
    public double getVolume() { return mdVolume; }
    public int getFkConsumableWahId() { return mnFkConsumableWahId; }
    
    public void setDbmsConsumableWarehouse(SDbConsumableWarehouse o) { moDbmsConsumableWarehouse = o; }

    public SDbConsumableWarehouse getDbmsConsumableWarehouse() { return moDbmsConsumableWarehouse; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkStockReportId = pk[0];
        mnPkConsumableRecordId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockReportId, mnPkConsumableRecordId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockReportId = 0;
        mnPkConsumableRecordId = 0;
        mdMeasure = 0;
        mdVolume = 0;
        mnFkConsumableWahId = 0;
        
        moDbmsConsumableWarehouse = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_report = " + mnPkStockReportId + " " +
                "AND id_cons_record = " + mnPkConsumableRecordId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_report = " + pk[0] + " " +
                "AND id_cons_record = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkConsumableRecordId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_cons_record), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_stk_report = " + mnPkStockReportId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkConsumableRecordId = resultSet.getInt(1);
        }
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
            mnPkStockReportId = resultSet.getInt("id_stk_report");
            mnPkConsumableRecordId = resultSet.getInt("id_cons_record");
            mdMeasure = resultSet.getDouble("measure");
            mdVolume = resultSet.getDouble("volume");
            mnFkConsumableWahId = resultSet.getInt("fk_cons_wah");
            
            moDbmsConsumableWarehouse = new SDbConsumableWarehouse();
            moDbmsConsumableWarehouse.read(session, new int[]{ mnFkConsumableWahId });

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
                    mnPkConsumableRecordId + ", " + 
                    mdMeasure + ", " + 
                    mdVolume + ", " + 
                    mnFkConsumableWahId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlWhere() + " SET " + 
                    //"id_stk_report = " + mnPkStockReportId + ", " +
                    "id_cons_record = " + mnPkConsumableRecordId + ", " +
                    "measure = " + mdMeasure + ", " +
                    "volume = " + mdVolume + ", " +
                    "fk_cons_wah = " + mnFkConsumableWahId + ", " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbConsumableRecord clone() throws CloneNotSupportedException {
        SDbConsumableRecord registry = new SDbConsumableRecord();
        
        registry.setPkStockReportId(this.getPkStockReportId());
        registry.setPkConsumableRecordId(this.getPkConsumableRecordId());
        registry.setMeasure(this.getMeasure());
        registry.setVolume(this.getVolume());
        registry.setFkConsumableWahId(this.getFkConsumableWahId());
        
        registry.setDbmsConsumableWarehouse(this.getDbmsConsumableWarehouse());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}
