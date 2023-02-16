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
public class SDbWahLab extends SDbRegistryUser {

    protected int mnPkWarehouseLaboratoryId;
    protected int mnValidation;
    protected int mnYear;
    protected int mnWeek;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected boolean mbDone;
    protected boolean mbValidated;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkUserDoneId;
    protected int mnFkUserValidationId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserDone;
    protected Date mtTsUserValidation;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected boolean mbOldDone;
    protected boolean mbOldValidated;
    
    protected ArrayList<SDbWahLabTest> maWahLabTests;
    
    protected SDbWahLab moLastWahLab;

    public SDbWahLab() {
        super(SModConsts.S_WAH_LAB);
        maWahLabTests = new ArrayList<>();
        initRegistry();
    }
    
    public void setPkWarehouseLaboratoryId(int n) { mnPkWarehouseLaboratoryId = n; }
    public void setValidation(int n) { mnValidation = n; }
    public void setYear(int n) { mnYear = n; }
    public void setWeek(int n) { mnWeek = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setDone(boolean b) { mbDone = b; }
    public void setValidated(boolean b) { mbValidated = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserDoneId(int n) { mnFkUserDoneId = n; }
    public void setFkUserValidationId(int n) { mnFkUserValidationId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserDone(Date t) { mtTsUserDone = t; }
    public void setTsUserValidation(Date t) { mtTsUserValidation = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkWarehouseLaboratoryId() { return mnPkWarehouseLaboratoryId; }
    public int getValidation() { return mnValidation; }
    public int getYear() { return mnYear; }
    public int getWeek() { return mnWeek; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public boolean isDone() { return mbDone; }
    public boolean isValidated() { return mbValidated; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserDoneId() { return mnFkUserDoneId; }
    public int getFkUserValidationId() { return mnFkUserValidationId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserDone() { return mtTsUserDone; }
    public Date getTsUserValidation() { return mtTsUserValidation; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setOldDone(boolean b) { mbOldDone = b; }
    public void setOldValidated(boolean b) { mbOldValidated = b; }
    
    public boolean getOldDone() { return mbOldDone; }
    public boolean getOldValidated() { return mbOldValidated; }

    public ArrayList<SDbWahLabTest> getWahLabTests() { return maWahLabTests; }
    
    public SDbWahLab getLastWahLab() { return moLastWahLab; }
    
    
    /**
     * Copia del método read pero sin leer el análisis anterior
     * @param session
     * @param pk
     * @throws java.sql.SQLException
    */
    public void readXta(SGuiSession session, int[] pk) throws SQLException, Exception {
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
            mnPkWarehouseLaboratoryId = resultSet.getInt("id_wah_lab");
            mnValidation = resultSet.getInt("val");
            mnYear = resultSet.getInt("year");
            mnWeek = resultSet.getInt("week");
            mtDateStart = resultSet.getDate("dt_start");
            mtDateEnd = resultSet.getDate("dt_end");
            mbDone = resultSet.getBoolean("b_done");
            mbValidated = resultSet.getBoolean("b_val");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read aswell child registries:
            
            statement = session.getStatement().getConnection().createStatement();
            
            msSql = "SELECT id_test FROM " + SModConsts.TablesMap.get(SModConsts.S_WAH_LAB_TEST) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbWahLabTest labTest = new SDbWahLabTest();
                labTest.read(session, new int[] { mnPkWarehouseLaboratoryId, resultSet.getInt(1) });
                maWahLabTests.add(labTest);
            }
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    public void readLastWahLab(SGuiSession session, int curYear, int curWeek, boolean showMessage) throws Exception {
        Statement statement = session.getStatement().getConnection().createStatement();
        
        String sqlWhere = "WHERE year = " + (curWeek == 1 ? curYear - 1 : curYear) + " "
                + "AND week " + (curWeek == 1 ? "<= 52 " : " = " + (curWeek - 1)) + " "
                + "AND b_done AND NOT b_del";
        msSql = "SELECT id_wah_lab FROM " + getSqlTable() + " " + sqlWhere;
        ResultSet resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            moLastWahLab = new SDbWahLab();
            moLastWahLab.readXta(session, new int[] { resultSet.getInt(1) });
        }
        else {
            sqlWhere = "WHERE year = " + (curWeek == 1 ? curYear - 1 : curYear) + " "
                    + "AND week " + (curWeek == 1 ? "= 51 " : " = " + (curWeek - 2)) + " "
                    + "AND b_done AND NOT b_del";
            msSql = "SELECT id_wah_lab FROM " + getSqlTable() + " " + sqlWhere;
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                moLastWahLab = new SDbWahLab();
                moLastWahLab.readXta(session, new int[] { resultSet.getInt(1) });
            }
            else {
                if (showMessage){
                    session.getClient().showMsgBoxInformation("No se encontraron analisis de laboratorio terminados para las últimas 2 semanas.");
                }
            }
        }
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkWarehouseLaboratoryId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkWarehouseLaboratoryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkWarehouseLaboratoryId = 0;
        mnValidation = 0;
        mnYear = 0;
        mnWeek = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mbDone = false;
        mbValidated = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserDoneId = 0;
        mnFkUserValidationId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserDone = null;
        mtTsUserValidation = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mbOldDone = false;
        mbOldValidated = false;
        
        maWahLabTests.clear();
        
        moLastWahLab = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_wah_lab = " + mnPkWarehouseLaboratoryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_wah_lab = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkWarehouseLaboratoryId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_wah_lab), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkWarehouseLaboratoryId = resultSet.getInt(1);
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
            mnPkWarehouseLaboratoryId = resultSet.getInt("id_wah_lab");
            mnValidation = resultSet.getInt("val");
            mnYear = resultSet.getInt("year");
            mnWeek = resultSet.getInt("week");
            mtDateStart = resultSet.getDate("dt_start");
            mtDateEnd = resultSet.getDate("dt_end");
            mbDone = resultSet.getBoolean("b_done");
            mbValidated = resultSet.getBoolean("b_val");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserDoneId = resultSet.getInt("fk_usr_done");
            mnFkUserValidationId = resultSet.getInt("fk_usr_val");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserDone = resultSet.getTimestamp("ts_usr_done");
            mtTsUserValidation = resultSet.getTimestamp("ts_usr_val");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mbOldDone = mbDone;
            mbOldValidated = mbValidated;

            // Read aswell child registries:
            
            statement = session.getStatement().getConnection().createStatement();
            
            maWahLabTests = new ArrayList<>();
            msSql = "SELECT id_test FROM " + SModConsts.TablesMap.get(SModConsts.S_WAH_LAB_TEST) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbWahLabTest labTest = new SDbWahLabTest();
                labTest.read(session, new int[] { mnPkWarehouseLaboratoryId, resultSet.getInt(1) });
                maWahLabTests.add(labTest);
            }
            
            readLastWahLab(session, mnYear, mnWeek, false);
            
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
            mnFkUserDoneId = mbDone && mbDone != mbOldDone ? session.getUser().getPkUserId() : SUtilConsts.USR_NA_ID;
            mnFkUserValidationId = mbValidated && mbValidated != mbOldValidated ? session.getUser().getPkUserId() : SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                    mnPkWarehouseLaboratoryId + ", " + 
                    mnValidation + ", " + 
                    mnYear + ", " + 
                    mnWeek + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " + 
                    (mbDone ? 1 : 0) + ", " + 
                    (mbValidated ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkUserDoneId + ", " + 
                    mnFkUserValidationId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            mnFkUserDoneId = mbDone && mbDone != mbOldDone ? session.getUser().getPkUserId() : SUtilConsts.USR_NA_ID;
            mnFkUserValidationId = mbValidated && mbValidated != mbOldValidated ? session.getUser().getPkUserId() : SUtilConsts.USR_NA_ID;
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_wah_lab = " + mnPkWarehouseLaboratoryId + ", " +
                    "val = " + mnValidation + ", " +
                    "year = " + mnYear + ", " +
                    "week = " + mnWeek + ", " +
                    "dt_start = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "b_done = " + (mbDone ? 1 : 0) + ", " +
                    "b_val = " + (mbValidated ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_usr_done = " + mnFkUserDoneId + ", " +
                    "fk_usr_val = " + mnFkUserValidationId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_done = " + "NOW()" + ", " +
                    "ts_usr_val = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Save aswell child registries:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.S_WAH_LAB_TEST) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        
        for (SDbWahLabTest labTest : maWahLabTests) {
            labTest.setPkWarehouseLaboratoryId(mnPkWarehouseLaboratoryId);
            labTest.setRegistryNew(true);
            labTest.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbWahLab clone() throws CloneNotSupportedException {
        SDbWahLab registry = new SDbWahLab();
        
        registry.setPkWarehouseLaboratoryId(this.getPkWarehouseLaboratoryId());
        registry.setValidation(this.getValidation());
        registry.setYear(this.getYear());
        registry.setWeek(this.getWeek());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setDone(this.isDone());
        registry.setValidated(this.isValidated());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserDoneId(this.getFkUserDoneId());
        registry.setFkUserValidationId(this.getFkUserValidationId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserDone(this.getTsUserDone());
        registry.setTsUserValidation(this.getTsUserValidation());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setOldDone(this.getOldDone());
        registry.setOldValidated(this.getOldValidated());

        for (SDbWahLabTest labTest : maWahLabTests) {
            registry.getWahLabTests().add(labTest.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}
