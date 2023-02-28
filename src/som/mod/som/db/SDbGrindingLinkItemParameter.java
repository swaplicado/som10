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
public class SDbGrindingLinkItemParameter extends SDbRegistryUser {

    protected int mnPkLinkId;
    protected int mnCaptureOrder;
    protected String msCaptureConfig;
    //protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkParameterId;
    protected int mnFkPlantCompanyId;
    protected int mnFkPlantBranchId;
    protected int mnFkPlantPlantId;
    
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbGrindingLinkItemParameter() {
        super(SModConsts.SU_GRINDING_LINK_ITEM_PARAM);
    }

    public void setPkLinkId(int n) { mnPkLinkId = n; }
    public void setCaptureOrder(int n) { mnCaptureOrder = n; }
    public void setCaptureConfig(String s) { msCaptureConfig = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkParameterId(int n) { mnFkParameterId = n; }
    public void setFkPlantCompanyId(int n) { mnFkPlantCompanyId = n; }
    public void setFkPlantBranchId(int n) { mnFkPlantBranchId = n; }
    public void setFkPlantPlantId(int n) { mnFkPlantPlantId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkLinkId() { return mnPkLinkId; }
    public int getCaptureOrder() { return mnCaptureOrder; }
    public String getCaptureConfig() { return msCaptureConfig; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkParameterId() { return mnFkParameterId; }
    public int getFkPlantCompanyId() { return mnFkPlantCompanyId; }
    public int getFkPlantBranchId() { return mnFkPlantBranchId; }
    public int getFkPlantPlantId() { return mnFkPlantPlantId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLinkId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLinkId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLinkId = 0;
        mnCaptureOrder = 0;
        msCaptureConfig = "";
        mbDeleted = false;
        mnFkItemId = 0;
        mnFkParameterId = 0;
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
        return "WHERE id_link = " + mnPkLinkId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_link = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLinkId = 0;

        msSql = "SELECT COALESCE(MAX(id_link), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLinkId = resultSet.getInt(1);
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
            mnPkLinkId = resultSet.getInt("id_link");
            mnCaptureOrder = resultSet.getInt("capture_order");
            msCaptureConfig = resultSet.getString("capture_cfg");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fk_item_id");
            mnFkParameterId = resultSet.getInt("fk_param_id");
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkLinkId + ", " +
                    mnCaptureOrder + ", " +
                    "'" + msCaptureConfig + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkItemId + ", " +
                    mnFkParameterId + ", " +
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
                    "capture_order = " + mnCaptureOrder + ", " +
                    "capture_cfg = '" + msCaptureConfig + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_item_id = " + mnFkItemId + ", " +
                    "fk_param_id = " + mnFkParameterId + ", " +
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
    public SDbGrindingLinkItemParameter clone() throws CloneNotSupportedException {
        SDbGrindingLinkItemParameter registry = new SDbGrindingLinkItemParameter();

        registry.setPkLinkId(this.getPkLinkId());
        registry.setCaptureOrder(this.getCaptureOrder());
        registry.setCaptureConfig(this.getCaptureConfig());
        registry.setDeleted(this.isDeleted());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkParameterId(this.getFkParameterId());
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
