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
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Isabel Servin
 */
public class SDbLaboratoryAlternative extends SDbRegistryUser {
    
    protected int mnPkAlternativeLaboratoryId;
    protected Date mtDate;
    protected double mdYieldPercentage;
    protected double mdMoisturePercentage;
    protected double mdAcidityPercentage;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbLaboratoryAlternative() {
        super(SModConsts.S_ALT_LAB);
        initRegistry();
    }
    
    public void setPkAlternativeLaboratoryId(int n) { mnPkAlternativeLaboratoryId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setYieldPercentage(double d) { mdYieldPercentage = d; }
    public void setMoisturePercentage(double d) { mdMoisturePercentage = d; }
    public void setAcidityPercentage(double d) { mdAcidityPercentage = d; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAlternativeLaboratoryId() { return mnPkAlternativeLaboratoryId; }
    public Date getDate() { return mtDate; }
    public double getYieldPercentage() { return mdYieldPercentage; }
    public double getMoisturePercentage() { return mdMoisturePercentage; }
    public double getAcidityPercentage() { return mdAcidityPercentage; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAlternativeLaboratoryId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAlternativeLaboratoryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkAlternativeLaboratoryId = 0;
        mtDate = null;
        mdYieldPercentage = 0;
        mdMoisturePercentage = 0;
        mdAcidityPercentage = 0;
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
        return "WHERE id_alt_lab = " + mnPkAlternativeLaboratoryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_alt_lab = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkAlternativeLaboratoryId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_alt_lab), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAlternativeLaboratoryId = resultSet.getInt(1);
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
            mnPkAlternativeLaboratoryId = resultSet.getInt("id_alt_lab");
            mtDate = resultSet.getDate("dt");
            mdYieldPercentage = resultSet.getDouble("yield_per");
            mdMoisturePercentage = resultSet.getDouble("moi_per");
            mdAcidityPercentage = resultSet.getDouble("aci_per");
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
                    mnPkAlternativeLaboratoryId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    mdYieldPercentage + ", " + 
                    mdMoisturePercentage + ", " + 
                    mdAcidityPercentage + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_alt_lab = " + mnPkAlternativeLaboratoryId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "yield_per = " + mdYieldPercentage + ", " +
                    "moi_per = " + mdMoisturePercentage + ", " +
                    "aci_per = " + mdAcidityPercentage + ", " +
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
    public void delete(SGuiSession session) throws Exception {
        msSql = "DELETE FROM " + getSqlTable() + " " + getSqlWhere();
        session.getStatement().execute(msSql);
    }

    @Override
    public SDbLaboratoryAlternative clone() throws CloneNotSupportedException {
        SDbLaboratoryAlternative registry = new SDbLaboratoryAlternative();
        
        registry.setPkAlternativeLaboratoryId(this.getPkAlternativeLaboratoryId());
        registry.setDate(this.getDate());
        registry.setYieldPercentage(this.getYieldPercentage());
        registry.setMoisturePercentage(this.getMoisturePercentage());
        registry.setAcidityPercentage(this.getAcidityPercentage());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(this.isRegistryNew()); 
        registry.setRegistryEdited(this.isRegistryEdited());

        return registry;
    }
}
