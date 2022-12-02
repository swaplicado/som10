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
public class SDbProcessingRawMaterials extends SDbRegistryUser {
    
    protected int mnPkStockReportId;
    protected int mnPkProcessingRawMaterialId;
    protected String msProcessingUnitName;
    protected double mdProcessedUnits;
    protected double mdProcessedKg;
    protected int mnFkFunctionalAreaId;
    protected int mnFkItemId;
    protected int mnFkProcessingBatchId;

    public SDbProcessingRawMaterials() {
        super(SModConsts.S_PRC_RAW_MAT);
        initRegistry();
    }
    
    public void setPkStockReportId(int n) { mnPkStockReportId = n; }
    public void setPkProcessingRawMaterialId(int n) { mnPkProcessingRawMaterialId = n; }
    public void setProcessingUnitName(String s) { msProcessingUnitName = s; }
    public void setProcessedUnits(double d) { mdProcessedUnits = d; }
    public void setProcessedKg(double d) { mdProcessedKg = d; }
    public void setFkFunctionalAreaId(int n) { mnFkFunctionalAreaId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkProcessingBatchId(int n) { mnFkProcessingBatchId = n; }

    public int getPkStockReportId() { return mnPkStockReportId; }
    public int getPkProcessingRawMaterialId() { return mnPkProcessingRawMaterialId; }
    public String getProcessingUnitName() { return msProcessingUnitName; }
    public double getProcessedUnits() { return mdProcessedUnits; }
    public double getProcessedKg() { return mdProcessedKg; }
    public int getFkFunctionalAreaId() { return mnFkFunctionalAreaId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkProcessingBatchId() { return mnFkProcessingBatchId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkStockReportId = pk[0];
        mnPkProcessingRawMaterialId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockReportId, mnPkProcessingRawMaterialId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockReportId = 0;
        mnPkProcessingRawMaterialId = 0;
        msProcessingUnitName = "";
        mdProcessedUnits = 0;
        mdProcessedKg = 0;
        mnFkFunctionalAreaId = 0;
        mnFkItemId = 0;
        mnFkProcessingBatchId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_report = " + mnPkStockReportId + " " + 
                "AND id_prc_raw_mat = " + mnPkProcessingRawMaterialId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_report = " + pk[0] + " " + 
                "AND id_prc_raw_mat = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkProcessingRawMaterialId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_prc_raw_mat), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_stk_report = " + mnPkStockReportId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkProcessingRawMaterialId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT *" + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkStockReportId = resultSet.getInt("id_stk_report");
            mnPkProcessingRawMaterialId = resultSet.getInt("id_prc_raw_mat");
            msProcessingUnitName = resultSet.getString("prc_unit_name");
            mdProcessedUnits = resultSet.getDouble("prc_units");
            mdProcessedKg = resultSet.getDouble("prc_kgs");
            mnFkFunctionalAreaId = resultSet.getInt("fk_func_area");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkProcessingBatchId = resultSet.getInt("fk_prc_batch");

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
                    mnPkProcessingRawMaterialId + ", " + 
                    "'" + msProcessingUnitName + "', " + 
                    mdProcessedUnits + ", " + 
                    mdProcessedKg + ", " + 
                    mnFkFunctionalAreaId + ", " + 
                    mnFkItemId + ", " + 
                    mnFkProcessingBatchId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_stk_report = " + mnPkStockReportId + ", " +
                    //"id_prc_raw_mat = " + mnPkProcessingRawMaterialId + ", " +
                    "prc_unit_name = '" + msProcessingUnitName + "', " +
                    "prc_units = " + mdProcessedUnits + ", " +
                    "prc_kgs = " + mdProcessedKg + ", " +
                    "fk_func_area = " + mnFkFunctionalAreaId + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_prc_batch = " + mnFkProcessingBatchId + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbProcessingRawMaterials clone() throws CloneNotSupportedException {
        SDbProcessingRawMaterials registry = new SDbProcessingRawMaterials();
        
        registry.setPkStockReportId(this.getPkStockReportId());
        registry.setPkProcessingRawMaterialId(this.getPkProcessingRawMaterialId());
        registry.setProcessingUnitName(this.getProcessingUnitName());
        registry.setProcessedUnits(this.getProcessedUnits());
        registry.setProcessedKg(this.getProcessedKg());
        registry.setFkFunctionalAreaId(this.getFkFunctionalAreaId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkProcessingBatchId(this.getFkProcessingBatchId());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}
