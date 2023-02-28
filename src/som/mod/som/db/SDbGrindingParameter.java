/*
 * To change this template, choose Tools | Templates
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
 * @author Edwin Carmona
 */
public class SDbGrindingParameter extends SDbRegistryUser {

    protected int mnPkParameterId;
    protected String msParameterCode;
    protected String msParameter;
    protected String msDetails;
    protected String msDefaultTextValue;
    protected boolean mbText;
    
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    
    protected int mnFkPlantCompanyId;
    protected int mnFkPlantBranchId;
    protected int mnFkPlantPlantId;
    
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbGrindingParameter() {
        super(SModConsts.SU_GRINDING_PARAM);
        initRegistry();
    }

    public void setPkParameter(int n) { mnPkParameterId = n; }
    public void setCode(String s) { msParameterCode = s; }
    public void setName(String s) { msParameter = s; }
    public void setDetails(String s) { msDetails = s; }
    public void setDefaultTextValue(String s) { msDefaultTextValue = s; }
    public void setIsText(boolean b) { mbText = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkPlantCompanyId(int n) { mnFkPlantCompanyId = n; }
    public void setFkPlantBranchId(int n) { mnFkPlantBranchId = n; }
    public void setFkPlantPlantId(int n) { mnFkPlantPlantId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkParameter() { return mnPkParameterId; }
    public String getCode() { return msParameterCode; }
    public String getName() { return msParameter; }
    public String getDetails() { return msDetails; }
    public String getDefaultTextValue() { return msDefaultTextValue; }
    public boolean isText() { return mbText; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkPlantCompanyId() { return mnFkPlantCompanyId; }
    public int getFkPlantBranchId() { return mnFkPlantBranchId; }
    public int getFkPlantPlantId() { return mnFkPlantPlantId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkParameterId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkParameterId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkParameterId = 0;
        msParameterCode = "";
        msParameter = "";
        msDetails = "";
        msDefaultTextValue = "";
        mbText = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkPlantCompanyId = 0;
        mnFkPlantBranchId = 0;
        mnFkPlantPlantId = 0;
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
        return "WHERE id_parameter = " + mnPkParameterId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_parameter = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkParameterId = 0;

        msSql = "SELECT COALESCE(MAX(id_parameter), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkParameterId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkParameterId = resultSet.getInt("id_parameter");
            msParameterCode = resultSet.getString("param_code");
            msParameter = resultSet.getString("parameter");
            msDetails = resultSet.getString("details");
            msDefaultTextValue = resultSet.getString("def_text_value");
            mbText = resultSet.getBoolean("b_text");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkPlantCompanyId = resultSet.getInt("fk_pla_co");
            mnFkPlantBranchId = resultSet.getInt("fk_pla_cob");
            mnFkPlantPlantId = resultSet.getInt("fk_pla_pla");
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
                    mnPkParameterId + ", " +
                    "'" + msParameterCode + "', " +
                    "'" + msParameter + "', " +
                    "'" + msDetails + "', " +
                    "'" + msDefaultTextValue + "', " +
                    (mbText ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkPlantCompanyId + ", " + 
                    mnFkPlantBranchId + ", " + 
                    mnFkPlantPlantId + ", " + 
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "param_code = '" + msParameterCode + "', " +
                    "parameter = '" + msParameter + "', " +
                    "details = '" + msDetails + "', " +
                    "def_text_value = '" + msDefaultTextValue + "', " +
                    "b_text = " + (mbText ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_pla_co = " + mnFkPlantCompanyId + ", " +
                    "fk_pla_cob = " + mnFkPlantBranchId + ", " +
                    "fk_pla_pla = " + mnFkPlantPlantId + ", " +
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
    public SDbGrindingParameter clone() throws CloneNotSupportedException {
        SDbGrindingParameter registry = new SDbGrindingParameter();

        registry.setPkParameter(this.getPkParameter());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDetails(this.getDetails());
        registry.setDefaultTextValue(this.getDefaultTextValue());
        registry.setIsText(this.isText());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkPlantCompanyId(this.getFkPlantCompanyId());
        registry.setFkPlantBranchId(this.getFkPlantBranchId());
        registry.setFkPlantPlantId(this.getFkPlantPlantId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
