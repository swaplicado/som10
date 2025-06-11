/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Isabel Servín
 */
public class SDbStockMovementEntry extends SDbRegistryUser implements SGridRow {

    protected int mnPkStockMovenemtId;
    protected int mnPkStockMovementEntryId;
    protected double mdQuantity;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkMaterialConditionId;
    protected int mnFkTicketId_n;
    
    protected String msXtaUnit;
    protected String msXtaMatCond;
    protected String msXtaTicketNum;
    
    protected SDbStock moStock;
    protected SDbStockMovement moAuxStockMovement;
    
    public SDbStockMovementEntry() {
        super(SModConsts.M_MVT_ETY);
        initRegistry();
    }

    public void setPkStockMovenemtId(int n) { mnPkStockMovenemtId = n; }
    public void setPkStockMovementEntryId(int n) { mnPkStockMovementEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkMaterialConditionId(int n) { mnFkMaterialConditionId = n; }
    public void setFkTicketId_n(int n) { mnFkTicketId_n = n; }

    public int getPkStockMovenemtId() { return mnPkStockMovenemtId; }
    public int getPkStockMovementEntryId() { return mnPkStockMovementEntryId; }
    public double getQuantity() { return mdQuantity; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkMaterialConditionId() { return mnFkMaterialConditionId; }
    public int getFkTicketId_n() { return mnFkTicketId_n; }
    
    public void setStock(SDbStock o) { moStock = o; }
    public void setAuxStockMovement(SDbStockMovement o) { moAuxStockMovement = o; }
    
    public String getXtaMatCond() { return msXtaMatCond; }
    public String getXtaTicketNum() { return msXtaTicketNum; }
    
    public SDbStock getStock() { return moStock; }
    public SDbStockMovement getAuxStockMovement() { return moAuxStockMovement; }
    
    public void readXtaUnit(SGuiSession session) throws Exception {
        ResultSet resultSet;
        String sql = "SELECT name FROM " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " WHERE id_unit = " + mnFkUnitId;
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            msXtaUnit = resultSet.getString(1);
        }
    }
    
    public void readXtaMatCond(SGuiSession session) throws Exception {
        ResultSet resultSet;
        String sql = "SELECT name FROM " + SModConsts.TablesMap.get(SModConsts.MU_MAT_COND) + " WHERE id_mat_cond = " + mnFkMaterialConditionId;
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            msXtaMatCond = resultSet.getString(1);
        }
    }
    
    public void readXtaTicNum(SGuiSession session) throws Exception {
        if (mnFkTicketId_n != 0) {
            msSql = "SELECT num " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
                    "WHERE id_tic = " + mnFkTicketId_n;
            ResultSet resultSet = session.getStatement().executeQuery(msSql);
            if (resultSet.next()) {
                msXtaTicketNum = resultSet.getString(1);
            }
        }
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkStockMovenemtId = pk[0];
        mnPkStockMovementEntryId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockMovenemtId, mnPkStockMovementEntryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockMovenemtId = 0;
        mnPkStockMovementEntryId = 0;
        mdQuantity = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkMaterialConditionId = 0;
        mnFkTicketId_n = 0;
        
        msXtaUnit = "";
        msXtaMatCond = "";
        msXtaTicketNum = "";
        
        moStock = null;
        moAuxStockMovement = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mvt = " + mnPkStockMovenemtId + " AND id_ety = " + mnPkStockMovementEntryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mvt = " + pk[0] + " AND id_ety = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkStockMovementEntryId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + " WHERE id_mvt = " + mnPkStockMovenemtId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockMovementEntryId = resultSet.getInt(1);
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
            mnPkStockMovenemtId = resultSet.getInt("id_mvt");
            mnPkStockMovementEntryId = resultSet.getInt("id_ety");
            mdQuantity = resultSet.getDouble("qty");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkMaterialConditionId = resultSet.getInt("fk_mat_cond");
            mnFkTicketId_n = resultSet.getInt("fk_tic_n");
            
            statement = session.getDatabase().getConnection().createStatement();
            
            // Read stock
            
            msSql = "SELECT id_year, id_wah, id_item, id_unit, id_stk " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.M_STK) + " " +
                    "WHERE fk_mvt_mvt = " + mnPkStockMovenemtId + " AND fk_mvt_ety = " + mnPkStockMovementEntryId;
            resultSet = statement.executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                moStock = new SDbStock();
                moStock.read(session, new int[] { 
                    resultSet.getInt("id_year"),
                    resultSet.getInt("id_wah"),
                    resultSet.getInt("id_item"),
                    resultSet.getInt("id_unit"),
                    resultSet.getInt("id_stk")
                });
            }
            
            mbRegistryNew = false;
        }
        
        readXtaUnit(session);
        readXtaMatCond(session);
        readXtaTicNum(session);
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
                    mnPkStockMovenemtId + ", " + 
                    mnPkStockMovementEntryId + ", " + 
                    mdQuantity + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUnitId + ", " + 
                    mnFkMaterialConditionId + ", " + 
                    (mnFkTicketId_n == 0 ? "NULL " : mnFkTicketId_n + " ") + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    "id_mvt = " + mnPkStockMovenemtId + ", " +
                    "id_ety = " + mnPkStockMovementEntryId + ", " +
                    "qty = " + mdQuantity + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_mat_cond = " + mnFkMaterialConditionId + ", " +
                    "fk_tic_n = " + (mnFkTicketId_n == 0 ? "NULL " : mnFkTicketId_n + " ") + 
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        if (mbRegistryNew) {
            // Save stock
            
            moStock = new SDbStock();
            
            moStock.setPkYearId(SLibTimeUtils.digestYear(moAuxStockMovement.getDate())[0]);
            moStock.setPkWarehouseId(SMaterialUtils.getParamMaterialWarehouseDefault(session));
            moStock.setPkItemId(mnFkItemId);
            moStock.setPkUnitId(mnFkUnitId);
            moStock.setDate(moAuxStockMovement.getDate());
            moStock.setReference(moAuxStockMovement.getReference());
            moStock.setQuantityIn(SModSysConsts.SS_IOG_CT_IN == moAuxStockMovement.mnFkMovementCategoryId ? mdQuantity : 0);
            moStock.setQuantityOut(SModSysConsts.SS_IOG_CT_OUT == moAuxStockMovement.mnFkMovementCategoryId ? mdQuantity : 0);
            moStock.setDeleted(false);
            moStock.setFkMovementCategoryId(moAuxStockMovement.mnFkMovementCategoryId);
            moStock.setFkMovementClassId(moAuxStockMovement.mnFkMovementClassId);
            moStock.setFkStockMovementId(mnPkStockMovenemtId);
            moStock.setFkStockMovementEntryId(mnPkStockMovementEntryId);
            moStock.setFkMaterialConditionId(mnFkMaterialConditionId);
            moStock.setFkTicketId_n(mnFkTicketId_n);
            moStock.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbStockMovementEntry clone() throws CloneNotSupportedException {
        SDbStockMovementEntry registry = new SDbStockMovementEntry();
        
        registry.setPkStockMovenemtId(this.getPkStockMovenemtId());
        registry.setPkStockMovementEntryId(this.getPkStockMovementEntryId());
        registry.setQuantity(this.getQuantity());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkMaterialConditionId(this.getFkMaterialConditionId());
        registry.setFkTicketId_n(this.getFkTicketId_n());
        
        registry.setStock(this.getStock());
        registry.setAuxStockMovement(this.getAuxStockMovement());
        
        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
    
     @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        moStock.delete(session);
        
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = msXtaMatCond;
                break;
            case 1: 
                value = mdQuantity;
                break;
            case 2:
                value = msXtaUnit;
                break;
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        
    }
}
