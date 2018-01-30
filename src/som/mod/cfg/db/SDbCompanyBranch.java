/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiConfigBranch;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDbCompanyBranch extends SDbRegistryUser implements SGuiConfigBranch {

    protected int mnPkCompanyId;
    protected int mnPkBranchId;
    protected String msCode;
    protected String msName;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected Vector<SDbBranchPlant> mvChildPlants;
    protected Vector<SDbBranchWarehouse> mvChildWarehouses;

    public SDbCompanyBranch() {
        super(SModConsts.CU_COB);
        mvChildPlants = new Vector<SDbBranchPlant>();
        mvChildWarehouses = new Vector<SDbBranchWarehouse>();
        initRegistry();
    }

    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public Vector<SDbBranchPlant> getChildBranchCashes() { return mvChildPlants; }
    public Vector<SDbBranchWarehouse> getChildBranchWarehouses() { return mvChildWarehouses; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCompanyId = pk[0];
        mnPkBranchId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCompanyId, mnPkBranchId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCompanyId = 0;
        mnPkBranchId = 0;
        msCode = "";
        msName = "";
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mvChildPlants.clear();
        mvChildWarehouses.clear();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_co = " + mnPkCompanyId + " AND " +
                "id_cob = " + mnPkBranchId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_co = " + pk[0] + " AND " +
                "id_cob = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkBranchId = 0;

        msSql = "SELECT COALESCE(MAX(id_cob), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_co = " + mnPkCompanyId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBranchId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            mnPkCompanyId = resultSet.getInt("id_co");
            mnPkBranchId = resultSet.getInt("id_cob");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_wah FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbBranchWarehouse child = new SDbBranchWarehouse();
                child.read(session, new int[] { mnPkCompanyId, mnPkBranchId, resultSet.getInt(1) });
                mvChildWarehouses.add(child);
            }

            msSql = "SELECT id_pla FROM " + SModConsts.TablesMap.get(SModConsts.CU_PLA) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbBranchPlant child = new SDbBranchPlant();
                child.read(session, new int[] { mnPkCompanyId, mnPkBranchId, resultSet.getInt(1) });
                mvChildPlants.add(child);
            }

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
                    mnPkCompanyId + ", " +
                    mnPkBranchId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_co = " + mnPkCompanyId + ", " +
                    //"id_cob = " + mnPkBranchId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        for (SDbBranchWarehouse child : mvChildWarehouses) {
            if (child.isRegistryNew() || child.isRegistryEdited()) {
                child.setPkCompanyId(mnPkCompanyId);
                child.setPkBranchId(mnPkBranchId);
                child.save(session);
            }
        }

        for (SDbBranchPlant child : mvChildPlants) {
            if (child.isRegistryNew() || child.isRegistryEdited()) {
                child.setPkCompanyId(mnPkCompanyId);
                child.setPkBranchId(mnPkBranchId);
                child.save(session);
            }
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbCompanyBranch clone() throws CloneNotSupportedException {
        SDbCompanyBranch registry = new SDbCompanyBranch();

        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbBranchPlant child : mvChildPlants) {
            registry.getChildBranchCashes().add(child.clone());
        }

        for (SDbBranchWarehouse child : mvChildWarehouses) {
            registry.getChildBranchWarehouses().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int getCompanyId() {
        return getPkCompanyId();
    }

    @Override
    public int getBranchId() {
        return getPkBranchId();
    }
}
