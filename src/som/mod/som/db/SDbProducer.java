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
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores, Isabel Servín
 */
public class SDbProducer extends SDbRegistryUser {

    public static final int FIELD_FISCAL_ID = SDbRegistry.FIELD_BASE + 1;
    public static final int FIELD_NAME_TRADE = SDbRegistry.FIELD_BASE + 2;
    public static final int LEN_FISCAL_ID = 20;
    
    protected int mnPkProducerId;
    protected String msCode;
    protected String msName;
    protected String msNameTrade;
    protected String msFiscalId;
    protected String msRevueltaProducerId;
    protected String msAutoMailNotificationBoxes;
    protected boolean mbAutoMailNotification;
    protected boolean mbFreightPayment;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkReportingGroupId;
    protected int mnFkInputSourceId;
    protected int mnFkExternalProducerId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbProducer() {
        super(SModConsts.SU_PROD);
        initRegistry();
    }
    
    /*
     * Private methods:
     */

    private boolean existsExternalProducer(SGuiSession session, int externalProducerId) throws Exception {
        boolean exists = false;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " " +
                "WHERE b_del = 0 AND b_dis = 0 AND fk_ext_prod_n = " + externalProducerId + " ";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            exists = resultSet.getInt(1) > 0;
        }

        return exists;
    }

    /*
     * Public methods:
     */

    public void setPkProducerId(int n) { mnPkProducerId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setNameTrade(String s) { msNameTrade = s; }
    public void setFiscalId(String s) { msFiscalId = s; }
    public void setRevueltaProducerId(String s) { msRevueltaProducerId = s; }
    public void setAutoMailNotificationBoxes(String s) { msAutoMailNotificationBoxes = s; }
    public void setAutoMailNotification(boolean b) { mbAutoMailNotification = b; }
    public void setFreightPayment(boolean b) { mbFreightPayment = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkReportingGroupId(int n) { mnFkReportingGroupId = n; }
    public void setFkInputSourceId(int n) { mnFkInputSourceId = n; }
    public void setFkExternalProducerId_n(int n) { mnFkExternalProducerId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkProducerId() { return mnPkProducerId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getNameTrade() { return msNameTrade; }
    public String getFiscalId() { return msFiscalId; }
    public String getRevueltaProducerId() { return msRevueltaProducerId; }
    public String getAutoMailNotificationBoxes() { return msAutoMailNotificationBoxes; }
    public boolean isAutoMailNotification() { return mbAutoMailNotification; }
    public boolean isFreightPayment() { return mbFreightPayment; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkReportingGroupId() { return mnFkReportingGroupId; }
    public int getFkInputSourceId() { return mnFkInputSourceId; }
    public int getFkExternalProducerId_n() { return mnFkExternalProducerId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkProducerId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkProducerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkProducerId = 0;
        msCode = "";
        msName = "";
        msNameTrade = "";
        msFiscalId = "";
        msRevueltaProducerId = "";
        msAutoMailNotificationBoxes = "";
        mbAutoMailNotification = false;
        mbFreightPayment = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkReportingGroupId = 0;
        mnFkInputSourceId = 0;
        mnFkExternalProducerId_n = 0;
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
        return "WHERE id_prod = " + mnPkProducerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_prod = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkProducerId = 0;

        msSql = "SELECT COALESCE(MAX(id_prod), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkProducerId = resultSet.getInt(1);
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
            mnPkProducerId = resultSet.getInt("id_prod");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msNameTrade = resultSet.getString("name_trd");
            msFiscalId = resultSet.getString("fis_id");
            msRevueltaProducerId = resultSet.getString("rev_prod_id");
            msAutoMailNotificationBoxes = resultSet.getString("amn_box");
            mbAutoMailNotification = resultSet.getBoolean("b_amn");
            mbFreightPayment = resultSet.getBoolean("b_fre_pay");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkReportingGroupId = resultSet.getInt("fk_rep_grp");
            mnFkInputSourceId = resultSet.getInt("fk_inp_src");
            mnFkExternalProducerId_n = resultSet.getInt("fk_ext_prod_n");
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
                    mnPkProducerId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    "'" + msNameTrade + "', " +
                    "'" + msFiscalId + "', " +
                    "'" + msRevueltaProducerId + "', " +
                    "'" + msAutoMailNotificationBoxes + "', " + 
                    (mbAutoMailNotification ? 1 : 0) + ", " + 
                    (mbFreightPayment ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkReportingGroupId + ", " +
                    mnFkInputSourceId + ", " + 
                    (mnFkExternalProducerId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalProducerId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_prod = " + mnPkProducerId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "name_trd = '" + msNameTrade + "', " +
                    "fis_id = '" + msFiscalId + "', " +
                    "rev_prod_id = '" + msRevueltaProducerId + "', " +
                    "amn_box = '" + msAutoMailNotificationBoxes + "', " +
                    "b_amn = " + (mbAutoMailNotification ? 1 : 0) + ", " +
                    "b_fre_pay = " + (mbFreightPayment ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_rep_grp = " + mnFkReportingGroupId + ", " +
                    "fk_inp_src = " + mnFkInputSourceId + ", " +
                    "fk_ext_prod_n = " + (mnFkExternalProducerId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalProducerId_n) + ", " +
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
    public SDbProducer clone() throws CloneNotSupportedException {
        SDbProducer registry = new SDbProducer();

        registry.setPkProducerId(this.getPkProducerId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setNameTrade(this.getNameTrade());
        registry.setFiscalId(this.getFiscalId());
        registry.setRevueltaProducerId(this.getRevueltaProducerId());
        registry.setAutoMailNotificationBoxes(this.getAutoMailNotificationBoxes());
        registry.setAutoMailNotification(this.isAutoMailNotification());
        registry.setFreightPayment(this.isFreightPayment());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkReportingGroupId(this.getFkReportingGroupId());
        registry.setFkInputSourceId(this.getFkInputSourceId());
        registry.setFkExternalProducerId_n(this.getFkExternalProducerId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);

        if (can) {
            initQueryMembers();

            if (mbRegistryNew) {
                can = !existsExternalProducer(session, mnFkExternalProducerId_n);

                if (!can) {
                    msQueryResult = "¡El proveedor externo ya existe!";
                }
            }
        }

        return can;
    }

    @Override
    public Object readField(final Statement statement, final int[] pk, final int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;
        
        if (field < SDbRegistry.FIELD_BASE) {
            value = super.readField(statement, pk, field);
        }
        else {
            initQueryMembers();
            mnQueryResultId = SDbConsts.READ_ERROR;

            msSql = "SELECT ";

            switch (field) {
                case FIELD_FISCAL_ID:
                    msSql += "fis_id ";
                    break;
                case FIELD_NAME_TRADE:
                    msSql += "name_trd ";
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            msSql += getSqlFromWhere(pk);

            resultSet = statement.executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                switch (field) {
                    case FIELD_FISCAL_ID:
                    case FIELD_NAME_TRADE:
                        value = resultSet.getString(1);
                        break;
                    default:
                }
            }

            mnQueryResultId = SDbConsts.READ_OK;
        }

        return value;
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
            case FIELD_NAME_TRADE:
                msSql += "name_trd = '" + value + "' ";
                break;
            case FIELD_FISCAL_ID:
                msSql += "fis_id = '" + value + "' ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += "WHERE fk_ext_prod_n = " + pk[0];
        statement.execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
