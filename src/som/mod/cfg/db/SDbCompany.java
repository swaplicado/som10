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
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiConfigCompany;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDbCompany extends SDbRegistryUser implements SGuiConfigCompany {

    protected int mnPkCompanyId;
    protected String msCode;
    protected String msName;
    protected String msExternalHost;
    protected String msExternalPort;
    protected String msExternalUser;
    protected String msExternalPassword;
    protected String msExternalDatabase;
    protected String msExternalDatabaseCo;
    protected String msMailNotificationConfigProtocol;
    protected String msMailNotificationConfigHost;
    protected String msMailNotificationConfigPort;
    protected String msMailNotificationConfigUser;
    protected String msMailNotificationConfigPassword;
    protected boolean mbMailNotificationConfigStartTls;
    protected boolean mbMailNotificationConfigAuth;
    protected String msCurrencyCode;
    protected double mdMaximumStockDifferenceKg;

    protected String msRevueltaId;
    protected String msRevueltaOdbc;
    protected String msRevueltaPath;
    protected int mnVersion;
    protected Date mtVersionTs;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkLanguageId;
    protected int mnFkDivisionDefaultId;
    protected int mnFkExternalCoId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected Vector<SDbCompanyBranch> mvChildBranches;

    public SDbCompany() {
        super(SModConsts.CU_CO);
        mvChildBranches = new Vector<SDbCompanyBranch>();
        initRegistry();
    }

    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setExternalHost(String s) { msExternalHost = s; }
    public void setExternalPort(String s) { msExternalPort = s; }
    public void setExternalUser(String s) { msExternalUser = s; }
    public void setExternalPassword(String s) { msExternalPassword = s; }
    public void setExternalDatabase(String s) { msExternalDatabase = s; }
    public void setExternalDatabaseCo(String s) { msExternalDatabaseCo = s; }
    public void setMailNotificationConfigProtocol(String s) { msMailNotificationConfigProtocol = s; }
    public void setMailNotificationConfigHost(String s) { msMailNotificationConfigHost = s; }
    public void setMailNotificationConfigPort(String s) { msMailNotificationConfigPort = s; }
    public void setMailNotificationConfigUser(String s) { msMailNotificationConfigUser = s; }
    public void setMailNotificationConfigPassword(String s) { msMailNotificationConfigPassword = s; }
    public void setMailNotificationConfigStartTls(boolean b) { mbMailNotificationConfigStartTls = b; }
    public void setMailNotificationConfigAuth(boolean b) { mbMailNotificationConfigAuth = b; }
    public void setCurrencyCode(String s) { msCurrencyCode = s; }
    public void setMaximumStockDifferenceKg(double d) { mdMaximumStockDifferenceKg = d; }
    public void setRevueltaId(String s) { msRevueltaId = s; }
    public void setRevueltaOdbc(String s) { msRevueltaOdbc = s; }
    public void setRevueltaPath(String s) { msRevueltaPath = s; }
    public void setVersion(int n) { mnVersion = n; }
    public void setVersionTs(Date t) { mtVersionTs = t; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkLanguageId(int n) { mnFkLanguageId = n; }
    public void setFkDivisionDefaultId(int n) { mnFkDivisionDefaultId = n; }
    public void setFkExternalCoId_n(int n) { mnFkExternalCoId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCompanyId() { return mnPkCompanyId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getExternalHost() { return msExternalHost; }
    public String getExternalPort() { return msExternalPort; }
    public String getExternalUser() { return msExternalUser; }
    public String getExternalPassword() { return msExternalPassword; }
    public String getExternalDatabase() { return msExternalDatabase; }
    public String getExternalDatabaseCo() { return msExternalDatabaseCo; }
    public String getMailNotificationConfigProtocol() { return msMailNotificationConfigProtocol; }
    public String getMailNotificationConfigHost() { return msMailNotificationConfigHost; }
    public String getMailNotificationConfigPort() { return msMailNotificationConfigPort; }
    public String getMailNotificationConfigUser() { return msMailNotificationConfigUser; }
    public String getMailNotificationConfigPassword() { return msMailNotificationConfigPassword; }
    public boolean isMailNotificationConfigStartTls() { return mbMailNotificationConfigStartTls; }
    public boolean isMailNotificationConfigAuth() { return mbMailNotificationConfigAuth; }
    public String getCurrencyCode() { return msCurrencyCode; }
    public double getMaximumStockDifferenceKg() { return mdMaximumStockDifferenceKg; }
    public String getRevueltaId() { return msRevueltaId; }
    public String getRevueltaOdbc() { return msRevueltaOdbc; }
    public String getRevueltaPath() { return msRevueltaPath; }
    public int getVersion() { return mnVersion; }
    public Date getVersionTs() { return mtVersionTs; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkLanguageId() { return mnFkLanguageId; }
    public int getFkDivisionDefaultId() { return mnFkDivisionDefaultId; }
    public int getFkExternalCoId_n() { return mnFkExternalCoId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public Vector<SDbCompanyBranch> getChildBranches() { return mvChildBranches; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCompanyId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCompanyId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCompanyId = 0;
        msCode = "";
        msName = "";
        msExternalHost = "";
        msExternalPort = "";
        msExternalUser = "";
        msExternalPassword = "";
        msExternalDatabase = "";
        msExternalDatabaseCo = "";
        msMailNotificationConfigProtocol = "";
        msMailNotificationConfigHost = "";
        msMailNotificationConfigPort = "";
        msMailNotificationConfigUser = "";
        msMailNotificationConfigPassword = "";
        mbMailNotificationConfigStartTls = false;
        mbMailNotificationConfigAuth = false;
        msCurrencyCode = "";
        mdMaximumStockDifferenceKg = 0;
        msRevueltaId = "";
        msRevueltaOdbc = "";
        msRevueltaPath = "";
        mnVersion = 0;
        mtVersionTs = null;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkLanguageId = 0;
        mnFkDivisionDefaultId = 0;
        mnFkExternalCoId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mvChildBranches.clear();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_co = " + mnPkCompanyId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_co = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkCompanyId = 0;

        msSql = "SELECT COALESCE(MAX(id_co), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCompanyId = resultSet.getInt(1);
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
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msExternalHost = resultSet.getString("ext_host");
            msExternalPort = resultSet.getString("ext_port");
            msExternalUser = resultSet.getString("ext_user");
            msExternalPassword = resultSet.getString("ext_pswd");
            msExternalDatabase = resultSet.getString("ext_db");
            msExternalDatabaseCo = resultSet.getString("ext_db_co");
            msMailNotificationConfigProtocol = resultSet.getString("mnc_prot");
            msMailNotificationConfigHost = resultSet.getString("mnc_host");
            msMailNotificationConfigPort = resultSet.getString("mnc_port");
            msMailNotificationConfigUser = resultSet.getString("mnc_user");
            msMailNotificationConfigPassword = resultSet.getString("mnc_pswd");
            mbMailNotificationConfigStartTls = resultSet.getBoolean("b_mnc_tls");
            mbMailNotificationConfigAuth = resultSet.getBoolean("b_mnc_auth");
            msCurrencyCode = resultSet.getString("cur_code");
            mdMaximumStockDifferenceKg = resultSet.getDouble("max_stk_dif_kg");
            msRevueltaId = resultSet.getString("rev_id");
            msRevueltaOdbc = resultSet.getString("rev_odbc");
            msRevueltaPath = resultSet.getString("rev_path");
            mnVersion = resultSet.getInt("ver");
            mtVersionTs = resultSet.getTimestamp("ver_ts");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkLanguageId = resultSet.getInt("fk_lan");
            mnFkDivisionDefaultId = resultSet.getInt("fk_div_def");
            mnFkExternalCoId_n = resultSet.getInt("fk_ext_co_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_cob FROM " + SModConsts.TablesMap.get(SModConsts.CU_COB) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbCompanyBranch child = new SDbCompanyBranch();
                child.read(session, new int[] { mnPkCompanyId, resultSet.getInt(1) });
                mvChildBranches.add(child);
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
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    "'" + msExternalHost + "', " +
                    "'" + msExternalPort + "', " +
                    "'" + msExternalUser + "', " +
                    "'" + msExternalPassword + "', " +
                    "'" + msExternalDatabase + "', " +
                    "'" + msExternalDatabaseCo + "', " +
                    "'" + msMailNotificationConfigProtocol + "', " +
                    "'" + msMailNotificationConfigHost + "', " +
                    "'" + msMailNotificationConfigPort + "', " +
                    "'" + msMailNotificationConfigUser + "', " +
                    "'" + msMailNotificationConfigPassword + "', " +
                    (mbMailNotificationConfigStartTls ? 1 : 0) + ", " +
                    (mbMailNotificationConfigAuth ? 1 : 0) + ", " +
                    "'" + msCurrencyCode + "', " +
                    mdMaximumStockDifferenceKg + ", " +
                    "'" + msRevueltaId + "', " +
                    "'" + msRevueltaOdbc + "', " +
                    "'" + msRevueltaPath + "', " + 
                    mnVersion + ", " +
                    "NOW()" + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkLanguageId + ", " +
                    mnFkDivisionDefaultId + ", " + 
                    (mnFkExternalCoId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalCoId_n) + ", " +
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
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "ext_host = '" + msExternalHost + "', " +
                    "ext_port = '" + msExternalPort + "', " +
                    "ext_user = '" + msExternalUser + "', " +
                    "ext_pswd = '" + msExternalPassword + "', " +
                    "ext_db = '" + msExternalDatabase + "', " +
                    "ext_db_co = '" + msExternalDatabaseCo + "', " +
                    "mnc_prot = '" + msMailNotificationConfigProtocol + "', " +
                    "mnc_host = '" + msMailNotificationConfigHost + "', " +
                    "mnc_port = '" + msMailNotificationConfigPort + "', " +
                    "mnc_user = '" + msMailNotificationConfigUser + "', " +
                    "mnc_pswd = '" + msMailNotificationConfigPassword + "', " +
                    "b_mnc_tls = " + (mbMailNotificationConfigStartTls ? 1 : 0) + ", " +
                    "b_mnc_auth = " + (mbMailNotificationConfigAuth ? 1 : 0) + ", " +
                    "cur_code = '" + msCurrencyCode + "', " +
                    "max_stk_dif_kg = " + mdMaximumStockDifferenceKg + ", " +
                    "rev_id = '" + msRevueltaId + "', " +
                    "rev_odbc = '" + msRevueltaOdbc + "', " +
                    "rev_path = '" + msRevueltaPath + "', " +
                    //"ver = " + mnVersion + ", " +
                    //"ver_ts = " + "NOW()" + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_lan = " + mnFkLanguageId + ", " +
                    "fk_div_def = " + mnFkDivisionDefaultId + ", " +
                    "fk_ext_co_n = " + (mnFkExternalCoId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalCoId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        for (SDbCompanyBranch child : mvChildBranches) {
            if (child.isRegistryNew() || child.isRegistryEdited()) {
                child.setPkCompanyId(mnPkCompanyId);
                child.save(session);
            }
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbCompany clone() throws CloneNotSupportedException {
        SDbCompany registry = new SDbCompany();

        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setExternalHost(this.getExternalHost());
        registry.setExternalPort(this.getExternalPort());
        registry.setExternalUser(this.getExternalUser());
        registry.setExternalPassword(this.getExternalPassword());
        registry.setExternalDatabase(this.getExternalDatabase());
        registry.setExternalDatabaseCo(this.getExternalDatabaseCo());
        registry.setMailNotificationConfigProtocol(this.getMailNotificationConfigProtocol());
        registry.setMailNotificationConfigHost(this.getMailNotificationConfigHost());
        registry.setMailNotificationConfigPort(this.getMailNotificationConfigPort());
        registry.setMailNotificationConfigUser(this.getMailNotificationConfigUser());
        registry.setMailNotificationConfigPassword(this.getMailNotificationConfigPassword());
        registry.setMailNotificationConfigStartTls(this.isMailNotificationConfigStartTls());
        registry.setMailNotificationConfigAuth(this.isMailNotificationConfigAuth());
        registry.setCurrencyCode(this.getCurrencyCode());
        registry.setMaximumStockDifferenceKg(this.getMaximumStockDifferenceKg());
        registry.setRevueltaId(this.getRevueltaId());
        registry.setRevueltaOdbc(this.getRevueltaOdbc());
        registry.setRevueltaPath(this.getRevueltaPath());
        registry.setVersion(this.getVersion());
        registry.setVersionTs(this.getVersionTs());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkLanguageId(this.getFkLanguageId());
        registry.setFkDivisionDefaultId(this.getFkDivisionDefaultId());
        registry.setFkExternalCoId_n(this.getFkExternalCoId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbCompanyBranch child : mvChildBranches) {
            registry.getChildBranches().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int getCompanyId() {
        return getPkCompanyId();
    }
}
