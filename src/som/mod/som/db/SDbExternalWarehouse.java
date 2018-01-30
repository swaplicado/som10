/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbExternalWarehouse extends SDbRegistryUser {

    protected int mnPkExternalWarehouseId;
    protected String msCode;
    protected String msName;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkExternalCobId_n;
    protected int mnFkExternalEntId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbExternalWarehouse() {
        super(SModConsts.SU_EXT_WAH);
        initRegistry();
    }
    
    /*
     * Private methods:
     */

    private boolean existsExternalWarehouse(SGuiSession session, int[] externalWarehouseKey) throws Exception {
        boolean exists = false;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.SU_EXT_WAH) + " " +
                "WHERE b_del = 0 AND b_dis = 0 AND fk_ext_cob_n = " + externalWarehouseKey[0] + " AND fk_ext_ent_n = " + externalWarehouseKey[1] + " ";

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            exists = resultSet.getInt(1) > 0;
        }

        return exists;
    }
    
    /*
     * Public methods:
     */

    public void setPkExternalWarehouseId(int n) { mnPkExternalWarehouseId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkExternalCobId_n(int n) { mnFkExternalCobId_n = n; }
    public void setFkExternalEntId_n(int n) { mnFkExternalEntId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkExternalWarehouseId() { return mnPkExternalWarehouseId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkExternalCobId_n() { return mnFkExternalCobId_n; }
    public int getFkExternalEntId_n() { return mnFkExternalEntId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkExternalWarehouseId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkExternalWarehouseId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkExternalWarehouseId = 0;
        msCode = "";
        msName = "";
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkExternalCobId_n = 0;
        mnFkExternalEntId_n = 0;
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
        return "WHERE id_ext_wah = " + mnPkExternalWarehouseId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ext_wah = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkExternalWarehouseId = 0;

        msSql = "SELECT COALESCE(MAX(id_ext_wah), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkExternalWarehouseId = resultSet.getInt(1);
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
            mnPkExternalWarehouseId = resultSet.getInt("id_ext_wah");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkExternalCobId_n = resultSet.getInt("fk_ext_cob_n");
            mnFkExternalEntId_n = resultSet.getInt("fk_ext_ent_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Finish registry reading:

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
                    mnPkExternalWarehouseId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    (mnFkExternalCobId_n == SLibConsts.UNDEFINED ? null : mnFkExternalCobId_n) + ", " +
                    (mnFkExternalEntId_n == SLibConsts.UNDEFINED ? null : mnFkExternalEntId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_ext_wah = " + mnPkExternalWarehouseId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_ext_cob_n = " + (mnFkExternalCobId_n == SLibConsts.UNDEFINED ? null : mnFkExternalCobId_n) + ", " +
                    "fk_ext_ent_n = " + (mnFkExternalEntId_n == SLibConsts.UNDEFINED ? null : mnFkExternalEntId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbExternalWarehouse clone() throws CloneNotSupportedException {
        SDbExternalWarehouse registry = new SDbExternalWarehouse();

        registry.setPkExternalWarehouseId(this.getPkExternalWarehouseId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkExternalCobId_n(this.getFkExternalCobId_n());
        registry.setFkExternalEntId_n(this.getFkExternalEntId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);

        if (can) {
            initQueryMembers();

            if (mbRegistryNew) {
                can = !existsExternalWarehouse(session, new int[] { mnFkExternalCobId_n, mnFkExternalEntId_n });

                if (!can) {
                    msQueryResult = "¡El almacén externo ya existe!";
                }
            }
        }

        return can;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_CODE:
                msSql += "code= '" + value + "' ";
                break;
            case FIELD_NAME:
                msSql += "name = '" + value + "' ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += "WHERE fk_ext_cob_n = " + pk[0] + " AND fk_ext_ent_n = " + pk[1];
        statement.execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
