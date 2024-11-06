/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiSessionCustom;
import sa.lib.gui.SGuiUser;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SDbUser extends SDbRegistryUser implements SGuiUser {

    public static final int FIELD_PASSWORD = SDbRegistry.FIELD_BASE + 1;

    protected int mnPkUserId;
    protected String msName;
    protected String msPassword;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkUserTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected Vector<SDbUserRight> mvChildUserRights;
    protected Vector<SDbUserRightAlternative> mvChildUserRightsAlternatives;
    protected Vector<SDbUserScale> mvChildUserScales;
    protected Vector<SDbUserInputCategory> mvChildUserInputCategories;

    public SDbUser() {
        super(SModConsts.CU_USR);
        mvChildUserRights = new Vector<>();
        mvChildUserRightsAlternatives = new Vector<>();
        mvChildUserScales = new Vector<>();
        mvChildUserInputCategories = new Vector<>();
        initRegistry();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setName(String s) { msName = s; }
    public void setPassword(String s) { msPassword = s; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserTypeId(int n) { mnFkUserTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkUserId() { return mnPkUserId; }
    public String getName() { return msName; }
    public String getPassword() { return msPassword; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserTypeId() { return mnFkUserTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public Vector<SDbUserRight> getChildUserRights() { return mvChildUserRights; }
    public Vector<SDbUserRightAlternative> getChildUserRightsAlternatives() { return mvChildUserRightsAlternatives; }
    public Vector<SDbUserScale> getChildUserScales() { return mvChildUserScales; }
    public Vector<SDbUserInputCategory> getChildUserInputCategories() { return mvChildUserInputCategories; }
    
    /**
     * Check if user has access to input category according to registry type.
     * @param registryType Registry type, either SModConsts.S_TIC or SModConsts.S_LAB.
     * @param inputCategoryId ID of input category.
     * @return 
     */
    public boolean hasInputCategoryAccess(final int registryType, final int inputCategoryId) {
        if (isAdministrator() || 
                (registryType == SModConsts.S_TIC && hasPrivilege(SModSysConsts.CS_RIG_SUP_SCA)) ||
                (registryType == SModConsts.S_LAB && hasPrivilege(SModSysConsts.CS_RIG_SUP_LAB))) {
            return true;
        }
        else {
            for (SDbUserInputCategory userInputCategory : mvChildUserInputCategories) {
                if (inputCategoryId == userInputCategory.getPkInputCategoryId()) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean isAdministrator() {
        return isSupervisor() || mnFkUserTypeId == SModSysConsts.CS_USR_TP_ADM;
    }

    @Override
    public boolean isSupervisor() {
        return mnFkUserTypeId == SModSysConsts.CS_USR_TP_SUP;
    }

    @Override
    public boolean hasModuleAccess(final int module) {
        boolean access = false;

        switch (module) {
            case SModConsts.MOD_CFG:
                access = isAdministrator() ||
                        hasPrivilege(SModSysConsts.CS_RIG_PER_OC);
                break;
            case SModConsts.MOD_SOM:
                access = isAdministrator() ||
                        hasPrivilege(SModSysConsts.CS_RIG_MAN_RM) || hasPrivilege(SModSysConsts.CS_RIG_WHS_RM) ||
                        hasPrivilege(SModSysConsts.CS_RIG_MAN_OM) || hasPrivilege(SModSysConsts.CS_RIG_WHS_OM);
                break;
            case SModConsts.MOD_SOM_RM:
                access = isAdministrator() ||
                        hasPrivilege(SModSysConsts.CS_RIG_MAN_RM) || hasPrivilege(SModSysConsts.CS_RIG_WHS_RM) || hasPrivilege(SModSysConsts.CS_RIG_REP_RM) ||
                        hasPrivilege(SModSysConsts.CS_RIG_SUP_SCA) || hasPrivilege(SModSysConsts.CS_RIG_SCA) ||
                        hasPrivilege(SModSysConsts.CS_RIG_SUP_LAB) || hasPrivilege(SModSysConsts.CS_RIG_LAB) ||
                        hasPrivilege(SModSysConsts.CS_RIG_RMEC) || hasPrivilege(SModSysConsts.CS_RIG_RMES) || hasPrivilege(SModSysConsts.CS_RIG_RMEA);
                break;
            case SModConsts.MOD_SOM_OS:
                access = isAdministrator() ||
                        hasPrivilege(SModSysConsts.CS_RIG_MAN_OM) || hasPrivilege(SModSysConsts.CS_RIG_WHS_OM) || hasPrivilege(SModSysConsts.CS_RIG_REP_OM);
                break;
            case SModConsts.MOD_SOM_LOG:
                access = isAdministrator() || 
                        hasPrivilege(SModSysConsts.CS_RIG_LOG);
                break;
            default:
        }

        return access;
    }

    public boolean hasPrivilegeAlternative(final int privilege) {
        boolean hasPrivilege = false;

        if (isSupervisor()) {
            hasPrivilege = true;
        }
        else if (isAdministrator()) {
            hasPrivilege = true;
        }
        else {
            for (SDbUserRightAlternative right : mvChildUserRightsAlternatives) {
                if (privilege == right.getPkRightId()) {
                    hasPrivilege = true;
                    break;
                }
            }
        }

        return hasPrivilege;
    }
    
    @Override
    public boolean hasPrivilege(final int privilege) {
        boolean hasPrivilege = false;

        if (isSupervisor()) {
            hasPrivilege = true;
        }
        else if (isAdministrator()) {
            hasPrivilege = true;
        }
        else {
            for (SDbUserRight right : mvChildUserRights) {
                if (privilege == right.getPkRightId()) {
                    hasPrivilege = true;
                    break;
                }
            }
        }

        return hasPrivilege;
    }

    @Override
    public boolean hasPrivilege(final int[] privileges) {
        boolean hasPrivilege = false;

        for (int privilege : privileges) {
            if (hasPrivilege(privilege)) {
                hasPrivilege = true;
                break;
            }
        }

        return hasPrivilege;
    }

    @Override
    public int getPrivilegeLevel(final int privilege) {
        return SUtilConsts.LEV_MANAGER;
    }

    @Override
    public HashMap<Integer, Integer> getPrivilegesMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashSet<Integer> getModulesSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computeAccess(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiSessionCustom createDefaultUserSession(final SGuiClient client) {
        return createDefaultUserSession(client, 0);
    }

    @Override
    public SGuiSessionCustom createDefaultUserSession(final SGuiClient client, final int terminal) {
        return new SGuiClientSessionCustom(client, terminal);
    }

    @Override
    public boolean showUserSessionConfigOnLogin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkUserId = 0;
        msName = "";
        msPassword = "";
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mvChildUserRights.clear();
        mvChildUserRightsAlternatives.clear();
        mvChildUserScales.clear();
        mvChildUserInputCategories.clear();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_usr = " + mnPkUserId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkUserId = 0;

        msSql = "SELECT COALESCE(MAX(id_usr), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkUserId = resultSet.getInt(1);
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
            mnPkUserId = resultSet.getInt("id_usr");
            msName = resultSet.getString("name");
            //msPassword = resultSet.getString("pswd");     // stored value is a string digestion, so it is useless
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserTypeId = resultSet.getInt("fk_usr_tp");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_rig FROM " + SModConsts.TablesMap.get(SModConsts.CU_USR_RIG) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbUserRight child = new SDbUserRight();
                child.read(session, new int[] { mnPkUserId, resultSet.getInt(1) });
                mvChildUserRights.add(child);
            }
            
            msSql = "SELECT id_rig FROM " + SModConsts.TablesMap.get(SModConsts.CU_USR_ALT_RIG) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbUserRightAlternative child = new SDbUserRightAlternative();
                child.read(session, new int[] { mnPkUserId, resultSet.getInt(1) });
                mvChildUserRightsAlternatives.add(child);
            }

            msSql = "SELECT id_sca FROM " + SModConsts.TablesMap.get(SModConsts.CU_USR_SCA) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbUserScale child = new SDbUserScale();
                child.read(session, new int[] { mnPkUserId, resultSet.getInt(1) });
                mvChildUserScales.add(child);
            }
            
            msSql = "SELECT id_inp_ct FROM " + SModConsts.TablesMap.get(SModConsts.CU_USR_INP_CT) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbUserInputCategory child = new SDbUserInputCategory();
                child.read(session, new int[] { mnPkUserId, resultSet.getInt(1) });
                mvChildUserInputCategories.add(child);
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
                    mnPkUserId + ", " +
                    "'" + msName + "', " +
                    "PASSWORD('" + msPassword + "'), " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkUserTypeId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_usr = " + mnPkUserId + ", " +
                    "name = '" + msName + "', " +
                    (msPassword.length() == 0 ? "" : "pswd = PASSWORD('" + msPassword + "'), ") +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_usr_tp = " + mnFkUserTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.CU_USR_RIG) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (SDbUserRight child : mvChildUserRights) {
            child.setPkUserId(mnPkUserId);
            child.setRegistryNew(true);
            child.save(session);
        }
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.CU_USR_ALT_RIG) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (SDbUserRightAlternative child : mvChildUserRightsAlternatives) {
            child.setPkUserId(mnPkUserId);
            child.setRegistryNew(true);
            child.save(session);
        }

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.CU_USR_SCA) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (SDbUserScale child : mvChildUserScales) {
            child.setPkUserId(mnPkUserId);
            child.setRegistryNew(true);
            child.save(session);
        }
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.CU_USR_INP_CT) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        
        for (SDbUserInputCategory child : mvChildUserInputCategories) {
            child.setPkUserId(mnPkUserId);
            child.setRegistryNew(true);
            child.save(session); 
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbUser clone() throws CloneNotSupportedException {
        SDbUser registry = new SDbUser();

        registry.setPkUserId(this.getPkUserId());
        registry.setName(this.getName());
        registry.setPassword(this.getPassword());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserTypeId(this.getFkUserTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbUserRight child : mvChildUserRights) {
            registry.getChildUserRights().add(child.clone());
        }
        
        for (SDbUserRightAlternative child : mvChildUserRightsAlternatives) {
            registry.getChildUserRightsAlternatives().add(child.clone());
        }

        for (SDbUserScale child : mvChildUserScales) {
            registry.getChildUserScales().add(child.clone());
        }
        
        for (SDbUserInputCategory child : mvChildUserInputCategories) {
            registry.getChildUserInputCategories().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_PASSWORD:
                msSql += "pswd = PASSWORD('" + value + "') ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
