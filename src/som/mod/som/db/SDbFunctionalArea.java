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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Isabel Servín
 */
public class SDbFunctionalArea extends SDbRegistryUser {
    
    protected int mnPkFunctionalAreaId;
    protected String msCode;
    protected String msName;
    protected String msFunctionalAreaType;
    protected String msStockReportMails;
    protected String msProcessingUnitName;
    protected double mdProcessingKgFactor;
    protected int mnPlant;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkInputCategoryId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbFunctionalArea() {
        super(SModConsts.SU_FUNC_AREA);
        initRegistry();
    }
    
    public void setPkFunctionalAreaId(int n) { mnPkFunctionalAreaId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setFunctionalAreaType(String s) { msFunctionalAreaType = s; }
    public void setStockReportMails(String s) { msStockReportMails = s; }
    public void setProcessingUnitName(String s) { msProcessingUnitName = s; }
    public void setProcessingKgFactor(double d) { mdProcessingKgFactor = d; }
    public void setPlant(int n) { mnPlant = n; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkInputCategoryId_n(int n) { mnFkInputCategoryId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkFunctionalAreaId() { return mnPkFunctionalAreaId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getFunctionalAreaType() { return msFunctionalAreaType; }
    public String getStockReportMails() { return msStockReportMails; }
    public String getProcessingUnitName() { return msProcessingUnitName; }
    public double getProcessingKgFactor() { return mdProcessingKgFactor; }
    public int getPlant() { return mnPlant; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkInputCategoryId_n() { return mnFkInputCategoryId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkFunctionalAreaId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkFunctionalAreaId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkFunctionalAreaId = 0;
        msCode = "";
        msName = "";
        msFunctionalAreaType = "";
        msStockReportMails = "";
        msProcessingUnitName = "";
        mdProcessingKgFactor = 0;
        mnPlant = 0;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkInputCategoryId_n = 0;
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
        return "WHERE id_func_area = " + mnPkFunctionalAreaId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_func_area = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkFunctionalAreaId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_func_area), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkFunctionalAreaId = resultSet.getInt(1);
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
        if(!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkFunctionalAreaId = resultSet.getInt("id_func_area");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msFunctionalAreaType = resultSet.getString("func_area_type");
            msStockReportMails = resultSet.getString("stk_report_mails");
            msProcessingUnitName = resultSet.getString("prc_unit_name");
            mdProcessingKgFactor = resultSet.getDouble("prc_kg_factor");
            mnPlant = resultSet.getInt("plant");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkInputCategoryId_n = resultSet.getInt("fk_inp_ct_n");
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
            mbDisableable = false;
            mbDeletable = true;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkFunctionalAreaId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msFunctionalAreaType + "', " + 
                    "'" + msStockReportMails + "', " + 
                    "'" + msProcessingUnitName + "', " + 
                    mdProcessingKgFactor + ", " + 
                    mnPlant + ", " + 
                    (mbUpdatable ? 1 : 0) + ", " + 
                    (mbDisableable ? 1 : 0) + ", " + 
                    (mbDeletable ? 1 : 0) + ", " + 
                    (mbDisabled ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    (mnFkInputCategoryId_n == 0 ? "NULL" : mnFkInputCategoryId_n) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_func_area = " + mnPkFunctionalAreaId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "func_area_type = '" + msFunctionalAreaType + "', " +
                    "stk_report_mails = '" + msStockReportMails + "', " +
                    "prc_unit_name = '" + msProcessingUnitName + "', " +
                    "prc_kg_factor = " + mdProcessingKgFactor + ", " +
                    "plant = " + mnPlant + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_inp_ct_n = " + (mnFkInputCategoryId_n == 0 ? "NULL" : mnFkInputCategoryId_n) + ", " +
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
    public SDbFunctionalArea clone() throws CloneNotSupportedException {
        SDbFunctionalArea registry = new SDbFunctionalArea();
        
        registry.setPkFunctionalAreaId(this.getPkFunctionalAreaId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setFunctionalAreaType(this.getFunctionalAreaType());
        registry.setStockReportMails(this.getStockReportMails());
        registry.setProcessingUnitName(this.getProcessingUnitName());
        registry.setProcessingKgFactor(this.getProcessingKgFactor());
        registry.setPlant(this.getPlant());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkInputCategoryId_n(this.getFkInputCategoryId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
}
