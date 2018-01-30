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
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbItem extends SDbRegistryUser {

    public static final int FIELD_EXTERNAL_CODE = SDbRegistry.FIELD_BASE + 1;
    public static final int FIELD_EXTERNAL_NAME = SDbRegistry.FIELD_BASE + 2;

    protected int mnPkItemId;
    protected String msCode;
    protected String msName;
    protected String msNameShort;
    protected String msExternalCode;
    protected String msExternalName;
    protected double mdDensity;
    protected double mdMfgFinishedGoodPercentage;
    protected double mdMfgByproductPercentage;
    protected double mdMfgCullPercentage;
    protected String msPackageName;
    protected double mdPackageWeight;
    protected double mdUnitaryWeight;
    protected int mnStartingSeasonMonth;
    protected String msRevueltaItemId;
    protected String msAutoMailNotificationBoxes;
    protected String msUserMailNotificationBoxes;
    protected boolean mbAutoMailNotification;
    protected boolean mbUserMailNotification;
    protected boolean mbUserMailNotificationOnlyWhenMoves;
    protected boolean mbPackage;
    protected boolean mbLaboratory;
    protected boolean mbDensity;
    protected boolean mbIodineValue;
    protected boolean mbRefractionIndex;
    protected boolean mbImpuritiesPercentage;
    protected boolean mbMoisturePercentage;
    protected boolean mbProteinPercentage;
    protected boolean mbOilContentPercentage;
    protected boolean mbOleicAcidPercentage;
    protected boolean mbLinoleicAcidPercentage;
    protected boolean mbLinolenicAcidPercentage;
    protected boolean mbErucicAcidPercentage;
    protected boolean mbAcidityPercentage;
    protected boolean mbPrintInputType;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkItemTypeId;
    protected int mnFkInputCategoryId;
    protected int mnFkInputClassId;
    protected int mnFkInputTypeId;
    protected int mnFkUnitId;
    protected int mnFkItemSource1Id_n;
    protected int mnFkItemSource2Id_n;

    protected int mnFkItemRowMaterialId_n;
    protected int mnFkItemByproductId_n;
    protected int mnFkItemCullId_n;
    protected int mnFkWarehouseCompanyId_n;
    protected int mnFkWarehouseBranchId_n;
    protected int mnFkWarehouseWarehouseId_n;
    protected int mnFkExternalWarehouseId;
    protected int mnFkExternalItemId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected int mnAuxFkExternalUnitId;

    public SDbItem() {
        super(SModConsts.SU_ITEM);
        initRegistry();
    }

    /*
     * Private methods
     */

    private boolean existsCode(final SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;
        boolean exists = false;

        msSql = "SELECT COUNT(*) FROM " + getSqlTable() + " " +
                "WHERE code = '" + msCode + "' AND id_item <> " + mnPkItemId + " ";

        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            if (resultSet.getInt(1) > 0) {
                exists = true;
            }
        }

        return exists;
    }

    private boolean existsName(final SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;
        boolean exists = false;

        msSql = "SELECT COUNT(*) FROM " + getSqlTable() + " " +
                "WHERE name = '" + msName + "' AND id_item <> " + mnPkItemId + " ";

        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            if (resultSet.getInt(1) > 0) {
                exists = true;
            }
        }

        return exists;
    }

    /*
     * Public methods
     */

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setNameShort(String s) { msNameShort = s; }
    public void setExternalCode(String s) { msExternalCode = s; }
    public void setExternalName(String s) { msExternalName = s; }
    public void setDensity(double d) { mdDensity = d; }
    public void setMfgFinishedGoodPercentage(double d) { mdMfgFinishedGoodPercentage = d; }
    public void setMfgByproductPercentage(double d) { mdMfgByproductPercentage = d; }
    public void setMfgCullPercentage(double d) { mdMfgCullPercentage = d; }
    public void setPackageName(String s) { msPackageName = s; }
    public void setPackageWeight(double d) { mdPackageWeight = d; }
    public void setUnitaryWeight(double d) { mdUnitaryWeight = d; }
    public void setStartingSeasonMonth(int n) { mnStartingSeasonMonth = n; }
    public void setRevueltaItemId(String s) { msRevueltaItemId = s; }
    public void setAutoMailNotificationBoxes(String s) { msAutoMailNotificationBoxes = s; }
    public void setUserMailNotificationBoxes(String s) { msUserMailNotificationBoxes = s; }
    public void setAutoMailNotification(boolean b) { mbAutoMailNotification = b; }
    public void setUserMailNotification(boolean b) { mbUserMailNotification = b; }
    public void setUserMailNotificationOnlyWhenMoves(boolean b) { mbUserMailNotificationOnlyWhenMoves = b; }
    public void setPackage(boolean b) { mbPackage = b; }
    public void setLaboratory(boolean b) { mbLaboratory = b; }
    public void setDensity(boolean b) { mbDensity = b; }
    public void setIodineValue(boolean b) { mbIodineValue = b; }
    public void setRefractionIndex(boolean b) { mbRefractionIndex = b; }
    public void setImpuritiesPercentage(boolean b) { mbImpuritiesPercentage = b; }
    public void setMoisturePercentage(boolean b) { mbMoisturePercentage = b; }
    public void setProteinPercentage(boolean b) { mbProteinPercentage = b; }
    public void setOilContentPercentage(boolean b) { mbOilContentPercentage = b; }
    public void setOleicAcidPercentage(boolean b) { mbOleicAcidPercentage = b; }
    public void setLinoleicAcidPercentage(boolean b) { mbLinoleicAcidPercentage = b; }
    public void setLinolenicAcidPercentage(boolean b) { mbLinolenicAcidPercentage = b; }
    public void setErucicAcidPercentage(boolean b) { mbErucicAcidPercentage = b; }
    public void setAcidityPercentage(boolean b) { mbAcidityPercentage = b; }
    public void setPrintInputType(boolean b) { mbPrintInputType = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkItemTypeId(int n) { mnFkItemTypeId = n; }
    public void setFkInputCategoryId(int n) { mnFkInputCategoryId = n; }
    public void setFkInputClassId(int n) { mnFkInputClassId = n; }
    public void setFkInputTypeId(int n) { mnFkInputTypeId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkItemSource1Id_n(int n) { mnFkItemSource1Id_n = n; }
    public void setFkItemSource2Id_n(int n) { mnFkItemSource2Id_n = n; }
    public void setFkItemRowMaterialId_n(int n) { mnFkItemRowMaterialId_n = n; }
    public void setFkItemByProductId_n(int n) { mnFkItemByproductId_n = n; }
    public void setFkItemCullId_n(int n) { mnFkItemCullId_n = n; }
    public void setFkWarehouseCompanyId_n(int n) { mnFkWarehouseCompanyId_n = n; }
    public void setFkWarehouseBranchId_n(int n) { mnFkWarehouseBranchId_n = n; }
    public void setFkWarehouseWarehouseId_n(int n) { mnFkWarehouseWarehouseId_n = n; }
    public void setFkExternalWarehouseId(int n) { mnFkExternalWarehouseId = n; }
    public void setFkExternalItemId_n(int n) { mnFkExternalItemId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemId() { return mnPkItemId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getNameShort() { return msNameShort; }
    public String getExternalCode() { return msExternalCode; }
    public String getExternalName() { return msExternalName; }
    public double getDensity() { return mdDensity; }
    public double getMfgFinishedGoodPercentage() { return mdMfgFinishedGoodPercentage; }
    public double getMfgByproductPercentage() { return mdMfgByproductPercentage; }
    public double getMfgCullPercentage() { return mdMfgCullPercentage; }
    public String getPackageName() { return msPackageName; }
    public double getPackageWeight() { return mdPackageWeight; }
    public double getUnitaryWeight() { return mdUnitaryWeight; }
    public int getStartingSeasonMonth() { return mnStartingSeasonMonth; }
    public String getRevueltaItemId() { return msRevueltaItemId; }
    public String getAutoMailNotificationBoxes() { return msAutoMailNotificationBoxes; }
    public String getUserMailNotificationBoxes() { return msUserMailNotificationBoxes; }
    public boolean isAutoMailNotification() { return mbAutoMailNotification; }
    public boolean isUserMailNotification() { return mbUserMailNotification; }
    public boolean isUserMailNotificationOnlyWhenMoves() { return mbUserMailNotificationOnlyWhenMoves; }
    public boolean isPackage() { return mbPackage; }
    public boolean isLaboratory() { return mbLaboratory; }
    public boolean isDensity() { return mbDensity; }
    public boolean isIodineValue() { return mbIodineValue; }
    public boolean isRefractionIndex() { return mbRefractionIndex; }
    public boolean isImpuritiesPercentage() { return mbImpuritiesPercentage; }
    public boolean isMoisturePercentage() { return mbMoisturePercentage; }
    public boolean isProteinPercentage() { return mbProteinPercentage; }
    public boolean isOilContentPercentage() { return mbOilContentPercentage; }
    public boolean isOleicAcidPercentage() { return mbOleicAcidPercentage; }
    public boolean isLinoleicAcidPercentage() { return mbLinoleicAcidPercentage; }
    public boolean isLinolenicAcidPercentage() { return mbLinolenicAcidPercentage; }
    public boolean isErucicAcidPercentage() { return mbErucicAcidPercentage; }
    public boolean isAcidityPercentage() { return mbAcidityPercentage; }
    public boolean isPrintInputType() { return mbPrintInputType; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkItemTypeId() { return mnFkItemTypeId; }
    public int getFkInputCategoryId() { return mnFkInputCategoryId; }
    public int getFkInputClassId() { return mnFkInputClassId; }
    public int getFkInputTypeId() { return mnFkInputTypeId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkItemSource1Id_n() { return mnFkItemSource1Id_n; }
    public int getFkItemSource2Id_n() { return mnFkItemSource2Id_n; }
    public int getFkItemRowMaterialId_n() { return mnFkItemRowMaterialId_n; }
    public int getFkItemByproductId_n() { return mnFkItemByproductId_n; }
    public int getFkItemCullId_n() { return mnFkItemCullId_n; }
    public int getFkWarehouseCompanyId_n() { return mnFkWarehouseCompanyId_n; }
    public int getFkWarehouseBranchId_n() { return mnFkWarehouseBranchId_n; }
    public int getFkWarehouseWarehouseId_n() { return mnFkWarehouseWarehouseId_n; }
    public int getFkExternalWarehouseId() { return mnFkExternalWarehouseId; }
    public int getFkExternalItemId_n() { return mnFkExternalItemId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxFkExternalUnitId(int n) { mnAuxFkExternalUnitId = n; }

    public int getAuxFkExternalUnitId() { return mnAuxFkExternalUnitId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId= pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkItemId = 0;
        msCode = "";
        msName = "";
        msNameShort = "";
        msExternalCode = "";
        msExternalName = "";
        mdDensity = 0;
        mdMfgFinishedGoodPercentage = 0;
        mdMfgByproductPercentage = 0;
        mdMfgCullPercentage = 0;
        msPackageName = "";
        mdPackageWeight = 0;
        mdUnitaryWeight = 0;
        mnStartingSeasonMonth = 0;
        msRevueltaItemId = "";
        msAutoMailNotificationBoxes = "";
        msUserMailNotificationBoxes = "";
        mbAutoMailNotification = false;
        mbUserMailNotification = false;
        mbUserMailNotificationOnlyWhenMoves = false;
        mbPackage = false;
        mbLaboratory = false;
        mbDensity = false;
        mbIodineValue = false;
        mbRefractionIndex = false;
        mbImpuritiesPercentage = false;
        mbMoisturePercentage = false;
        mbProteinPercentage = false;
        mbOilContentPercentage = false;
        mbOleicAcidPercentage = false;
        mbLinoleicAcidPercentage = false;
        mbLinolenicAcidPercentage = false;
        mbErucicAcidPercentage = false;
        mbAcidityPercentage = false;
        mbPrintInputType = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkItemTypeId = 0;
        mnFkInputCategoryId = 0;
        mnFkInputClassId = 0;
        mnFkInputTypeId = 0;
        mnFkUnitId = 0;
        mnFkItemSource1Id_n = 0;
        mnFkItemSource2Id_n = 0;
        mnFkItemRowMaterialId_n = 0;
        mnFkItemByproductId_n = 0;
        mnFkItemCullId_n = 0;
        mnFkWarehouseCompanyId_n = 0;
        mnFkWarehouseBranchId_n = 0;
        mnFkWarehouseWarehouseId_n = 0;
        mnFkExternalWarehouseId = 0;
        mnFkExternalItemId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mnAuxFkExternalUnitId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_item = " + mnPkItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_item = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkItemId = 0;

        msSql = "SELECT COALESCE(MAX(id_item), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkItemId = resultSet.getInt(1);
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
            mnPkItemId = resultSet.getInt("id_item");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msNameShort = resultSet.getString("name_sht");
            msExternalCode = resultSet.getString("ext_code");
            msExternalName = resultSet.getString("ext_name");
            mdDensity = resultSet.getDouble("den");
            mdMfgFinishedGoodPercentage = resultSet.getDouble("mfg_fg_per");
            mdMfgByproductPercentage = resultSet.getDouble("mfg_bp_per");
            mdMfgCullPercentage = resultSet.getDouble("mfg_cu_per");
            msPackageName = resultSet.getString("paq_name");
            mdPackageWeight = resultSet.getDouble("paq_wei");
            mdUnitaryWeight = resultSet.getDouble("unit_wei");
            mnStartingSeasonMonth = resultSet.getInt("sta_seas_mon");
            msRevueltaItemId = resultSet.getString("rev_item_id");
            msAutoMailNotificationBoxes = resultSet.getString("amn_box");
            msUserMailNotificationBoxes = resultSet.getString("umn_box");
            mbAutoMailNotification = resultSet.getBoolean("b_amn");
            mbUserMailNotification = resultSet.getBoolean("b_umn");
            mbUserMailNotificationOnlyWhenMoves = resultSet.getBoolean("b_umn_owm");
            mbPackage = resultSet.getBoolean("b_paq");
            mbLaboratory = resultSet.getBoolean("b_lab");
            mbDensity = resultSet.getBoolean("b_den");
            mbIodineValue = resultSet.getBoolean("b_iod_val");
            mbRefractionIndex = resultSet.getBoolean("b_ref_ind");
            mbImpuritiesPercentage = resultSet.getBoolean("b_imp_per");
            mbMoisturePercentage = resultSet.getBoolean("b_moi_per");
            mbProteinPercentage = resultSet.getBoolean("b_pro_per");
            mbOilContentPercentage = resultSet.getBoolean("b_oil_per");
            mbOleicAcidPercentage = resultSet.getBoolean("b_ole_per");
            mbLinoleicAcidPercentage = resultSet.getBoolean("b_lin_per");
            mbLinolenicAcidPercentage = resultSet.getBoolean("b_llc_per");
            mbErucicAcidPercentage = resultSet.getBoolean("b_eru_per");
            mbAcidityPercentage = resultSet.getBoolean("b_aci_per");
            mbPrintInputType = resultSet.getBoolean("b_prt_inp_tp");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkItemTypeId = resultSet.getInt("fk_item_tp");
            mnFkInputCategoryId = resultSet.getInt("fk_inp_ct");
            mnFkInputClassId = resultSet.getInt("fk_inp_cl");
            mnFkInputTypeId = resultSet.getInt("fk_inp_tp");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkItemSource1Id_n = resultSet.getInt("fk_item_src_1_n");
            mnFkItemSource2Id_n = resultSet.getInt("fk_item_src_2_n");
            mnFkItemRowMaterialId_n = resultSet.getInt("fk_item_rm_n");
            mnFkItemByproductId_n = resultSet.getInt("fk_item_bp_n");
            mnFkItemCullId_n = resultSet.getInt("fk_item_cu_n");
            mnFkWarehouseCompanyId_n = resultSet.getInt("fk_wah_co_n");
            mnFkWarehouseBranchId_n = resultSet.getInt("fk_wah_cob_n");
            mnFkWarehouseWarehouseId_n = resultSet.getInt("fk_wah_wah_n");
            mnFkExternalWarehouseId = resultSet.getInt("fk_ext_wah");
            mnFkExternalItemId_n = resultSet.getInt("fk_ext_item_n");
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
                    mnPkItemId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    "'" + msNameShort + "', " + 
                    "'" + msExternalCode + "', " +
                    "'" + msExternalName + "', " +
                    mdDensity + ", " +
                    mdMfgFinishedGoodPercentage + ", " +
                    mdMfgByproductPercentage + ", " +
                    mdMfgCullPercentage + ", " +
                    "'" + msPackageName + "', " +
                    mdPackageWeight + ", " +
                    mdUnitaryWeight + ", " +
                    mnStartingSeasonMonth + ", " +
                    "'" + msRevueltaItemId + "', " +
                    "'" + msAutoMailNotificationBoxes + "', " +
                    "'" + msUserMailNotificationBoxes + "', " +
                    (mbAutoMailNotification ? 1 : 0) + ", " +
                    (mbUserMailNotification ? 1 : 0) + ", " +
                    (mbUserMailNotificationOnlyWhenMoves ? 1 : 0) + ", " +
                    (mbPackage ? 1 : 0) + ", " +
                    (mbLaboratory ? 1 : 0) + ", " +
                    (mbDensity ? 1 : 0) + ", " +
                    (mbIodineValue ? 1 : 0) + ", " +
                    (mbRefractionIndex ? 1 : 0) + ", " +
                    (mbImpuritiesPercentage ? 1 : 0) + ", " +
                    (mbMoisturePercentage ? 1 : 0) + ", " +
                    (mbProteinPercentage ? 1 : 0) + ", " +
                    (mbOilContentPercentage ? 1 : 0) + ", " +
                    (mbOleicAcidPercentage ? 1 : 0) + ", " +
                    (mbLinoleicAcidPercentage ? 1 : 0) + ", " +
                    (mbLinolenicAcidPercentage ? 1 : 0) + ", " +
                    (mbErucicAcidPercentage ? 1 : 0) + ", " +
                    (mbAcidityPercentage ? 1 : 0) + ", " +
                    (mbPrintInputType ? 1 : 0) + ", " + 
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkItemTypeId + ", " +
                    mnFkInputCategoryId + ", " + 
                    mnFkInputClassId + ", " +
                    mnFkInputTypeId + ", " +
                    mnFkUnitId + ", " +
                    (mnFkItemSource1Id_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemSource1Id_n) + ", " +
                    (mnFkItemSource2Id_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemSource2Id_n) + ", " +
                    (mnFkItemRowMaterialId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemRowMaterialId_n) + ", " +
                    (mnFkItemByproductId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemByproductId_n) + ", " +
                    (mnFkItemCullId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemCullId_n) + ", " +
                    (mnFkWarehouseCompanyId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseCompanyId_n) + ", " +
                    (mnFkWarehouseBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseBranchId_n) + ", " +
                    (mnFkWarehouseWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseWarehouseId_n) + ", " +
                    mnFkExternalWarehouseId + ", " +
                    (mnFkExternalItemId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalItemId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_item = " + mnPkItemId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "name_sht = '" + msNameShort + "', " +
                    "ext_code = '" + msExternalCode + "', " +
                    "ext_name = '" + msExternalName + "', " +
                    "den = " + mdDensity + ", " +
                    "mfg_fg_per = " + mdMfgFinishedGoodPercentage + ", " +
                    "mfg_bp_per = " + mdMfgByproductPercentage + ", " +
                    "mfg_cu_per = " + mdMfgCullPercentage + ", " +
                    "paq_name = '" + msPackageName + "', " +
                    "paq_wei = " + mdPackageWeight + ", " +
                    "unit_wei = " + mdUnitaryWeight + ", " +
                    "sta_seas_mon = " + mnStartingSeasonMonth + ", " +
                    "rev_item_id = '" + msRevueltaItemId + "', " +
                    "amn_box = '" + msAutoMailNotificationBoxes + "', " +
                    "umn_box = '" + msUserMailNotificationBoxes + "', " +
                    "b_amn = " + (mbAutoMailNotification ? 1 : 0) + ", " +
                    "b_umn = " + (mbUserMailNotification ? 1 : 0) + ", " +
                    "b_umn_owm = " + (mbUserMailNotificationOnlyWhenMoves ? 1 : 0) + ", " +
                    "b_paq = " + (mbPackage ? 1 : 0) + ", " +
                    "b_lab = " + (mbLaboratory ? 1 : 0) + ", " +
                    "b_den = " + (mbDensity ? 1 : 0) + ", " +
                    "b_iod_val = " + (mbIodineValue ? 1 : 0) + ", " +
                    "b_ref_ind = " + (mbRefractionIndex ? 1 : 0) + ", " +
                    "b_imp_per = " + (mbImpuritiesPercentage ? 1 : 0) + ", " +
                    "b_moi_per = " + (mbMoisturePercentage ? 1 : 0) + ", " +
                    "b_pro_per = " + (mbProteinPercentage ? 1 : 0) + ", " +
                    "b_oil_per = " + (mbOilContentPercentage ? 1 : 0) + ", " +
                    "b_ole_per = " + (mbOleicAcidPercentage ? 1 : 0) + ", " +
                    "b_lin_per = " + (mbLinoleicAcidPercentage ? 1 : 0) + ", " +
                    "b_llc_per = " + (mbLinolenicAcidPercentage ? 1 : 0) + ", " +
                    "b_eru_per = " + (mbErucicAcidPercentage ? 1 : 0) + ", " +
                    "b_aci_per = " + (mbAcidityPercentage ? 1 : 0) + ", " +
                    "b_prt_inp_tp = " + (mbPrintInputType ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_item_tp = " + mnFkItemTypeId + ", " +
                    "fk_inp_ct = " + mnFkInputCategoryId + ", " +
                    "fk_inp_cl = " + mnFkInputClassId + ", " +
                    "fk_inp_tp = " + mnFkInputTypeId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_item_src_1_n = " + (mnFkItemSource1Id_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemSource1Id_n) + ", " +
                    "fk_item_src_2_n = " + (mnFkItemSource2Id_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemSource2Id_n) + ", " +
                    "fk_item_rm_n = " + (mnFkItemRowMaterialId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemRowMaterialId_n) + ", " +
                    "fk_item_bp_n = " + (mnFkItemByproductId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemByproductId_n) + ", " +
                    "fk_item_cu_n = " + (mnFkItemCullId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkItemCullId_n) + ", " +
                    "fk_wah_co_n = " + (mnFkWarehouseCompanyId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseCompanyId_n) + ", " +
                    "fk_wah_cob_n = " + (mnFkWarehouseBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseBranchId_n) + ", " +
                    "fk_wah_wah_n = " + (mnFkWarehouseWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseWarehouseId_n) + ", " +
                    "fk_ext_wah = " + mnFkExternalWarehouseId + ", " +
                    "fk_ext_item_n = " + (mnFkExternalItemId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalItemId_n) + ", " +
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
    public SDbItem clone() throws CloneNotSupportedException {
        SDbItem registry = new SDbItem();

        registry.setPkItemId(this.getPkItemId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setExternalCode(this.getExternalCode());
        registry.setExternalName(this.getExternalName());
        registry.setDensity(this.getDensity());
        registry.setMfgFinishedGoodPercentage(this.getMfgFinishedGoodPercentage());
        registry.setMfgByproductPercentage(this.getMfgByproductPercentage());
        registry.setMfgCullPercentage(this.getMfgCullPercentage());
        registry.setPackageName(this.getPackageName());
        registry.setPackageWeight(this.getPackageWeight());
        registry.setUnitaryWeight(this.getUnitaryWeight());
        registry.setStartingSeasonMonth(this.getStartingSeasonMonth());
        registry.setRevueltaItemId(this.getRevueltaItemId());
        registry.setAutoMailNotificationBoxes(this.getAutoMailNotificationBoxes());
        registry.setUserMailNotificationBoxes(this.getUserMailNotificationBoxes());
        registry.setAutoMailNotification(this.isAutoMailNotification());
        registry.setUserMailNotification(this.isUserMailNotification());
        registry.setUserMailNotificationOnlyWhenMoves(this.isUserMailNotificationOnlyWhenMoves());
        registry.setPackage(this.isPackage());
        registry.setLaboratory(this.isLaboratory());
        registry.setDensity(this.isDensity());
        registry.setIodineValue(this.isIodineValue());
        registry.setRefractionIndex(this.isRefractionIndex());
        registry.setImpuritiesPercentage(this.isImpuritiesPercentage());
        registry.setMoisturePercentage(this.isMoisturePercentage());
        registry.setProteinPercentage(this.isProteinPercentage());
        registry.setOilContentPercentage(this.isOilContentPercentage());
        registry.setOleicAcidPercentage(this.isOleicAcidPercentage());
        registry.setLinoleicAcidPercentage(this.isLinoleicAcidPercentage());
        registry.setLinolenicAcidPercentage(this.isLinolenicAcidPercentage());
        registry.setErucicAcidPercentage(this.isErucicAcidPercentage());
        registry.setAcidityPercentage(this.isAcidityPercentage());
        registry.setPrintInputType(this.isPrintInputType());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkItemTypeId(this.getFkItemTypeId());
        registry.setFkInputCategoryId(this.getFkInputCategoryId());
        registry.setFkInputClassId(this.getFkInputClassId());
        registry.setFkInputTypeId(this.getFkInputTypeId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkItemSource1Id_n(this.getFkItemSource1Id_n());
        registry.setFkItemSource2Id_n(this.getFkItemSource2Id_n());
        registry.setFkItemRowMaterialId_n(this.getFkItemRowMaterialId_n());
        registry.setFkItemByProductId_n(this.getFkItemByproductId_n());
        registry.setFkItemCullId_n(this.getFkItemCullId_n());
        registry.setFkWarehouseCompanyId_n(this.getFkWarehouseCompanyId_n());
        registry.setFkWarehouseBranchId_n(this.getFkWarehouseBranchId_n());
        registry.setFkWarehouseWarehouseId_n(this.getFkWarehouseWarehouseId_n());
        registry.setFkExternalWarehouseId(this.getFkExternalWarehouseId());
        registry.setFkExternalItemId_n(this.getFkExternalItemId_n());
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
                mnFkUnitId = SSomUtils.getUnitId(session, mnAuxFkExternalUnitId);

                if (mnFkUnitId == SLibConsts.UNDEFINED) {
                    can = false;
                    msQueryResult = "¡La unidad del ítem no existe!";
                }
            }

            if (can && existsCode(session)) {
                can = false;
                msQueryResult = "¡El código del ítem ya existe!";
            }
            else if (existsName(session)) {
                can = false;
                msQueryResult = "¡El nombre del ítem ya existe!";
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
            case FIELD_EXTERNAL_CODE:
                msSql += "ext_code = '" + value + "' ";
                break;
            case FIELD_EXTERNAL_NAME:
                msSql += "ext_name = '" + value + "' ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += "WHERE fk_ext_item_n = " + pk[0];
        statement.execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    public int validateExternalItem(SGuiSession session, int nFkExternalItemId) {
        int nPkItemId = 0;
        ResultSet resultSet = null;

        try {
            msSql = "SELECT COALESCE(MAX(id_item),0) AS f_id_item FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " WHERE fk_ext_item_n = " + nFkExternalItemId + "; ";
            resultSet = session.getStatement().executeQuery(msSql);

            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                nPkItemId = resultSet.getInt("f_id_item");
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        return nPkItemId;
    }
}
