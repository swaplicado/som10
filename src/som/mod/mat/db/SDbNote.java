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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Isabel Servín
 */
public class SDbNote extends SDbRegistryUser {

    protected int mnPkNoteId;
    protected String msNote;
    protected String msNoteType;
    protected String msReference;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkStockMovementId_n;
    protected int mnFkItemId_n;
    protected int mnFkUnitId_n;
    protected int mnFkMaterialConditionId_n;
    protected int mnFkTicketId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbNote() {
        super(SModConsts.M_NOTE);
    }
    
    public void setPkNoteId(int n) { mnPkNoteId = n; }
    public void setNote(String s) { msNote = s; }
    public void setNoteType(String s) { msNoteType = s; }
    public void setReference(String s) { msReference = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkStockMovementId_n(int n) { mnFkStockMovementId_n = n; }
    public void setFkItemId_n(int n) { mnFkItemId_n = n; }
    public void setFkUnitId_n(int n) { mnFkUnitId_n = n; }
    public void setFkMaterialConditionId_n(int n) { mnFkMaterialConditionId_n = n; }
    public void setFkTicketId_n(int n) { mnFkTicketId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkNoteId() { return mnPkNoteId; }
    public String getNote() { return msNote; }
    public String getNoteType() { return msNoteType; }
    public String getReference() { return msReference; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkStockMovementId_n() { return mnFkStockMovementId_n; }
    public int getFkItemId_n() { return mnFkItemId_n; }
    public int getFkUnitId_n() { return mnFkUnitId_n; }
    public int getFkMaterialConditionId_n() { return mnFkMaterialConditionId_n; }
    public int getFkTicketId_n() { return mnFkTicketId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }


    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkNoteId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkNoteId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkNoteId = 0;
        msNote = "";
        msNoteType = "";
        msReference = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkStockMovementId_n = 0;
        mnFkItemId_n = 0;
        mnFkUnitId_n = 0;
        mnFkMaterialConditionId_n = 0;
        mnFkTicketId_n = 0;
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
        return "WHERE id_note = " + mnPkNoteId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_note = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkNoteId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_note), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkNoteId = resultSet.getInt(1);
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
            mnPkNoteId = resultSet.getInt("id_note");
            msNote = resultSet.getString("note");
            msNoteType = resultSet.getString("note_tp");
            msReference = resultSet.getString("ref");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkStockMovementId_n = resultSet.getInt("fk_mvt_n");
            mnFkItemId_n = resultSet.getInt("fk_item_n");
            mnFkUnitId_n = resultSet.getInt("fk_unit_n");
            mnFkMaterialConditionId_n = resultSet.getInt("fk_mat_cond_n");
            mnFkTicketId_n = resultSet.getInt("fk_tic_n");
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
                    mnPkNoteId + ", " + 
                    "'" + msNote + "', " + 
                    "'" + msNoteType + "', " + 
                    "'" + msReference + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    (mnFkStockMovementId_n == 0 ? "NULL, " : mnFkStockMovementId_n + ", ") + 
                    (mnFkItemId_n == 0 ? "NULL, " : mnFkItemId_n + ", ") + 
                    (mnFkUnitId_n == 0 ? "NULL, " : mnFkUnitId_n + ", ") + 
                    (mnFkMaterialConditionId_n == 0 ? "NULL, " : mnFkMaterialConditionId_n + ", ") + 
                    (mnFkTicketId_n == 0 ? "NULL, " : mnFkTicketId_n + ", ") + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_note = " + mnPkNoteId + ", " +
                    "note = '" + msNote + "', " +
                    "note_tp = '" + msNoteType + "', " +
                    "ref = '" + msReference + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_mvt_n = " + (mnFkStockMovementId_n == 0 ? "NULL, " : mnFkStockMovementId_n + ", ") +
                    "fk_item_n = " + (mnFkItemId_n == 0 ? "NULL, " : mnFkItemId_n + ", ") +
                    "fk_unit_n = " + (mnFkUnitId_n == 0 ? "NULL, " : mnFkUnitId_n + ", ") +
                    "fk_mat_cond_n = " + (mnFkMaterialConditionId_n == 0 ? "NULL, " : mnFkMaterialConditionId_n + ", ") + 
                    "fk_tic_n = " + (mnFkTicketId_n == 0 ? "NULL, " : mnFkTicketId_n + ", ") + 
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
    public SDbNote clone() throws CloneNotSupportedException {
        SDbNote registry = new SDbNote();
        
        registry.setPkNoteId(this.getPkNoteId());
        registry.setNote(this.getNote());
        registry.setNoteType(this.getNoteType());
        registry.setReference(this.getReference());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkStockMovementId_n(this.getFkStockMovementId_n());
        registry.setFkItemId_n(this.getFkItemId_n());
        registry.setFkUnitId_n(this.getFkUnitId_n());
        registry.setFkMaterialConditionId_n(this.getFkMaterialConditionId_n());
        registry.setFkTicketId_n(this.getFkTicketId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
