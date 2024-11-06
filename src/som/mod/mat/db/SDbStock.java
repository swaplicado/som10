/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

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
 * @author Isabel Servín
 */
public class SDbStock extends SDbRegistryUser {

    protected int mnPkYearId;
    protected int mnPkWarehouseId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkStockId;
    protected Date mtDate;
    protected String msReference;
    protected double mdQuantityIn;
    protected double mdQuantityOut;
    //protected boolean mbDeleted;
    protected int mnFkMovementCategoryId;
    protected int mnFkMovementClassId;
    protected int mnFkStockMovementId;
    protected int mnFkStockMovementEntryId;
    protected int mnFkMaterialConditionId;
    protected int mnFkTicketId_n;
    
    public SDbStock() {
        super(SModConsts.M_STK);
        initRegistry();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkStockId(int n) { mnPkStockId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setReference(String s) { msReference = s; }
    public void setQuantityIn(double d) { mdQuantityIn = d; }
    public void setQuantityOut(double d) { mdQuantityOut = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkMovementCategoryId(int n) { mnFkMovementCategoryId = n; }
    public void setFkMovementClassId(int n) { mnFkMovementClassId = n; }
    public void setFkStockMovementId(int n) { mnFkStockMovementId = n; }
    public void setFkStockMovementEntryId(int n) { mnFkStockMovementEntryId = n; }
    public void setFkMaterialConditionId(int n) { mnFkMaterialConditionId = n; }
    public void setFkTicketId_n(int n) { mnFkTicketId_n = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkStockId() { return mnPkStockId; }
    public Date getDate() { return mtDate; }
    public String getReference() { return msReference; }
    public double getQuantityIn() { return mdQuantityIn; }
    public double getQuantityOut() { return mdQuantityOut; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkMovementCategoryId() { return mnFkMovementCategoryId; }
    public int getFkMovementClassId() { return mnFkMovementClassId; }
    public int getFkStockMovementId() { return mnFkStockMovementId; }
    public int getFkStockMovementEntryId() { return mnFkStockMovementEntryId; }
    public int getFkMaterialConditionId() { return mnFkMaterialConditionId; }
    public int getFkTicketId_n() { return mnFkTicketId_n; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkWarehouseId = pk[1];
        mnPkItemId = pk[2];
        mnPkUnitId = pk[3];
        mnPkStockId = pk[4];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkWarehouseId, mnPkItemId, mnPkUnitId, mnPkStockId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkYearId = 0;
        mnPkWarehouseId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkStockId = 0;
        mtDate = null;
        msReference = "";
        mdQuantityIn = 0;
        mdQuantityOut = 0;
        mbDeleted = false;
        mnFkMovementCategoryId = 0;
        mnFkMovementClassId = 0;
        mnFkStockMovementId = 0;
        mnFkStockMovementEntryId = 0;
        mnFkMaterialConditionId = 0;
        mnFkTicketId_n = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " "
                + "AND id_wah = " + mnPkWarehouseId + " " 
                + "AND id_item = " + mnPkItemId + " " 
                + "AND id_unit = " + mnPkUnitId + " " 
                + "AND id_stk = " + mnPkStockId + " "; 
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " "
                + "AND id_wah = " + pk[1] + " " 
                + "AND id_item = " + pk[2] + " " 
                + "AND id_unit = " + pk[3] + " " 
                + "AND id_stk = " + pk[4] + " "; 
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkStockId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_stk), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_year = " + mnPkYearId + " "
                + "AND id_wah = " + mnPkWarehouseId + " "
                + "AND id_item = " + mnPkItemId + " "
                + "AND id_unit = " + mnPkUnitId + " ";
                
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockId = resultSet.getInt(1);
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
            mnPkYearId = resultSet.getInt("id_year");
            mnPkWarehouseId = resultSet.getInt("id_wah");
            mnPkItemId = resultSet.getInt("id_item");
            mnPkUnitId = resultSet.getInt("id_unit");
            mnPkStockId = resultSet.getInt("id_stk");
            mtDate = resultSet.getDate("dt");
            msReference = resultSet.getString("ref");
            mdQuantityIn = resultSet.getDouble("qty_in");
            mdQuantityOut = resultSet.getDouble("qty_out");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkMovementCategoryId = resultSet.getInt("fk_iog_ct");
            mnFkMovementClassId = resultSet.getInt("fk_mvt_cl");
            mnFkStockMovementId = resultSet.getInt("fk_mvt_mvt");
            mnFkStockMovementEntryId = resultSet.getInt("fk_mvt_ety");
            mnFkMaterialConditionId = resultSet.getInt("fk_mat_cond");
            mnFkTicketId_n = resultSet.getInt("fk_tic_n");
            
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
                    mnPkWarehouseId + ", " + 
                    mnPkItemId + ", " + 
                    mnPkUnitId + ", " + 
                    mnPkStockId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    "'" + msReference + "', " + 
                    mdQuantityIn + ", " + 
                    mdQuantityOut + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkMovementCategoryId + ", " + 
                    mnFkMovementClassId + ", " + 
                    mnFkStockMovementId + ", " + 
                    mnFkStockMovementEntryId + ", " + 
                    mnFkMaterialConditionId + ", " + 
                    (mnFkTicketId_n == 0 ? "NULL " : mnFkTicketId_n + " ") + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_year = " + mnPkYearId + ", " +
                    //"id_wah = " + mnPkWarehouseId + ", " +
                    //"id_item = " + mnPkItemId + ", " +
                    //"id_unit = " + mnPkUnitId + ", " +
                    //"id_stk = " + mnPkStockId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "ref = '" + msReference + "', " +
                    "qty_in = " + mdQuantityIn + ", " +
                    "qty_out = " + mdQuantityOut + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_iog_ct = " + mnFkMovementCategoryId + ", " +
                    "fk_mvt_cl = " + mnFkMovementClassId + ", " +
                    "fk_mvt_mvt = " + mnFkStockMovementId + ", " +
                    "fk_mvt_ety = " + mnFkStockMovementEntryId + ", " +
                    "fk_mat_cond = " + mnFkMaterialConditionId + ", " +
                    "fk_tic_n = " + (mnFkTicketId_n == 0 ? "NULL " : mnFkTicketId_n + " ") + 
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
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkStockId(this.getPkStockId());
        registry.setDate(this.getDate());
        registry.setReference(this.getReference());
        registry.setQuantityIn(this.getQuantityIn());
        registry.setQuantityOut(this.getQuantityOut());
        registry.setDeleted(this.isDeleted());
        registry.setFkMovementCategoryId(this.getFkMovementCategoryId());
        registry.setFkMovementClassId(this.getFkMovementClassId());
        registry.setFkStockMovementId(this.getFkStockMovementId());
        registry.setFkStockMovementEntryId(this.getFkStockMovementEntryId());
        registry.setFkMaterialConditionId(this.getFkMaterialConditionId());
        registry.setFkTicketId_n(this.getFkTicketId_n());
        
        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        mbDeleted = true;
        mnFkUserUpdateId = session.getUser().getPkUserId();

        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + " " +
                getSqlWhere();

        session.getStatement().execute(msSql);
        
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
