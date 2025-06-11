/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Isabel Servín
 */
public class SDbTicketDivision extends SDbRegistryUser {

    protected int mnPkTicketDividedId;
    protected int mnPkTicketNewId;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    public SDbTicketDivision() {
        super(SModConsts.S_TIC_DIV);
        initRegistry();
    }

    public void setPkTicketDividedId(int n) { mnPkTicketDividedId = n; }
    public void setPkTicketNewId(int n) { mnPkTicketNewId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }
    
    public int getPkTicketDividedId() { return mnPkTicketDividedId; }
    public int getPkTicketNewId() { return mnPkTicketNewId; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTicketDividedId = pk[0];
        mnPkTicketNewId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTicketDividedId, mnPkTicketNewId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkTicketDividedId = 0;
        mnPkTicketNewId = 0;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tic_div = " + mnPkTicketDividedId + " AND id_tic_new = " + mnPkTicketNewId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tic_div = " + pk[0] + " AND id_tic_new = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        
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
            mnPkTicketDividedId = resultSet.getInt("id_tic_div");
            mnPkTicketNewId = resultSet.getInt("id_tic_new");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");
            
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
            mnFkUserId = session.getUser().getPkUserId();
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkTicketDividedId + ", " + 
                    mnPkTicketNewId + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_tic_div = " + mnPkTicketDividedId + ", " +
                    //"id_tic_new = " + mnPkTicketNewId + ", " +
                    "fk_usr = " + mnFkUserId + ", " +
                    "ts_usr = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbTicketDivision clone() throws CloneNotSupportedException {
        SDbTicketDivision registry = new SDbTicketDivision();
        
        registry.setPkTicketDividedId(this.getPkTicketDividedId());
        registry.setPkTicketNewId(this.getPkTicketNewId());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public void delete(SGuiSession session) throws SQLException, Exception {
        msSql = "DELETE " + getSqlFromWhere();
        session.getStatement().execute(msSql);
    }
}
