/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SDbGrindingResult extends SDbRegistryUser {

    protected int mnPkResultId;
    protected Date mtDateCapture;
    protected int mnOrder;
    protected double mdResult8;
    protected double mdResult10;
    protected double mdResult12;
    protected double mdResult14;
    protected double mdResult16;
    protected double mdResult18;
    protected double mdResult20;
    protected double mdResult22;
    protected double mdResult0;
    protected double mdResult2;
    protected double mdResult4;
    protected double mdResult6;
    protected String msResultText;
    protected boolean mbText;
    protected int mnFkItemId;
    protected int mnFkParameterId;
    protected int mnFkLotId;
    
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

    public SDbGrindingResult() {
        super(SModConsts.SU_LAB_GRINDING);
        initRegistry();
    }

    /*
     * Private methods:
     */
    
    
    /*
     * Public methods:
     */
    
    public void setPkResultId(int n) { mnPkResultId = n; }
    public void setDateCapture(Date t) { mtDateCapture = t; }
    public void setOrder(int n) { mnOrder = n; }
    public void setResult8(double d) { mdResult8 = d; }
    public void setResult10(double d) { mdResult10 = d; }
    public void setResult12(double d) { mdResult12 = d; }
    public void setResult14(double d) { mdResult14 = d; }
    public void setResult16(double d) { mdResult16 = d; }
    public void setResult18(double d) { mdResult18 = d; }
    public void setResult20(double d) { mdResult20 = d; }
    public void setResult22(double d) { mdResult22 = d; }
    public void setResult0(double d) { mdResult0 = d; }
    public void setResult2(double d) { mdResult2 = d; }
    public void setResult4(double d) { mdResult4 = d; }
    public void setResult6(double d) { mdResult6 = d; }
    public void setResultText(String s) { msResultText = s; }
    public void setIsText(boolean b) { mbText = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkParameterId(int n) { mnFkParameterId = n; }
    public void setFkLotId(int n) { mnFkLotId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkResultId() { return mnPkResultId; }
    public Date getDateCapture() { return mtDateCapture; }
    public int getOrder() { return mnOrder; }
    public double getResult8() { return mdResult8; }
    public double getResult10() { return mdResult10; }
    public double getResult12() { return mdResult12; }
    public double getResult14() { return mdResult14; }
    public double getResult16() { return mdResult16; }
    public double getResult18() { return mdResult18; }
    public double getResult20() { return mdResult20; }
    public double getResult22() { return mdResult22; }
    public double getResult0() { return mdResult0; }
    public double getResult2() { return mdResult2; }
    public double getResult4() { return mdResult4; }
    public double getResult6() { return mdResult6; }
    public String getResultText() { return msResultText; }
    public boolean isText() { return mbText; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkParameterId() { return mnFkParameterId; }
    public int getFkLotId() { return mnFkLotId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkResultId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkResultId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkResultId = 0;
        mtDateCapture = null;
        mnOrder = 0;
        mdResult8 = 0d;
        mdResult10 = 0d;
        mdResult12 = 0d;
        mdResult14 = 0d;
        mdResult16 = 0d;
        mdResult18 = 0d;
        mdResult20 = 0d;
        mdResult22 = 0d;
        mdResult0 = 0d;
        mdResult2 = 0d;
        mdResult4 = 0d;
        mdResult6 = 0d;
        msResultText = "";
        mbText = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkItemId = 0;
        mnFkParameterId = 0;
        mnFkLotId = 0;
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
        return "WHERE id_result = " + mnPkResultId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_result = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkResultId = 0;

        msSql = "SELECT COALESCE(MAX(id_result), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkResultId = resultSet.getInt(1);
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
            mnPkResultId = resultSet.getInt("id_result");
            mtDateCapture = resultSet.getDate("dt_capture");
            mnOrder = resultSet.getInt("capture_order");
            mdResult8 = resultSet.getDouble("result_08");
            mdResult10 = resultSet.getDouble("result_10");
            mdResult12 = resultSet.getDouble("result_12");
            mdResult14 = resultSet.getDouble("result_14");
            mdResult16 = resultSet.getDouble("result_16");
            mdResult18 = resultSet.getDouble("result_18");
            mdResult20 = resultSet.getDouble("result_20");
            mdResult22 = resultSet.getDouble("result_22");
            mdResult0 = resultSet.getDouble("result_00");
            mdResult2 = resultSet.getDouble("result_02");
            mdResult4 = resultSet.getDouble("result_04");
            mdResult6 = resultSet.getDouble("result_06");
            msResultText = resultSet.getString("result_text");
            mbText = resultSet.getBoolean("b_text");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkItemId = resultSet.getInt("fk_item_id");
            mnFkParameterId = resultSet.getInt("fk_parameter_id");
            mnFkLotId = resultSet.getInt("fk_lot_id");
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
                    mnPkResultId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateCapture) + "', " +
                    mnOrder + ", " +
                    mdResult8 + ", " +
                    mdResult10 + ", " +
                    mdResult12 + ", " +
                    mdResult14 + ", " +
                    mdResult16 + ", " +
                    mdResult18 + ", " +
                    mdResult20 + ", " +
                    mdResult22 + ", " +
                    mdResult0 + ", " +
                    mdResult2 + ", " +
                    mdResult4 + ", " +
                    mdResult6 + ", " +
                    "'" + msResultText + "', " +
                    (mbText ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkItemId + ", " +
                    mnFkParameterId + ", " +
                    mnFkLotId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_seas = " + mnPkSeasonId + ", " +
                    "dt_capture = '" + SLibUtils.DbmsDateFormatDate.format(mtDateCapture) + "', " +
                    "capture_order = " + mnOrder + ", " +
                    "result_08 = " + mdResult8 + ", " +
                    "result_10 = " + mdResult10 + ", " +
                    "result_12 = " + mdResult12 + ", " +
                    "result_14 = " + mdResult14 + ", " +
                    "result_16 = " + mdResult16 + ", " +
                    "result_18 = " + mdResult18 + ", " +
                    "result_20 = " + mdResult20 + ", " +
                    "result_22 = " + mdResult22 + ", " +
                    "result_00 = " + mdResult0 + ", " +
                    "result_02 = " + mdResult2 + ", " +
                    "result_04 = " + mdResult4 + ", " +
                    "result_06 = " + mdResult6 + ", " +
                    "result_text = '" + msResultText + "', " +
                    "b_text = " + (mbText ? 1 : 0) + ", " +
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

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbGrindingResult clone() throws CloneNotSupportedException {
        SDbGrindingResult registry = new SDbGrindingResult();

        registry.setPkResultId(this.getPkResultId());
        registry.setDateCapture(this.getDateCapture());
        registry.setOrder(this.getOrder());
        registry.setResult8(this.getResult8());
        registry.setResult10(this.getResult10());
        registry.setResult12(this.getResult12());
        registry.setResult14(this.getResult14());
        registry.setResult16(this.getResult16());
        registry.setResult18(this.getResult18());
        registry.setResult20(this.getResult20());
        registry.setResult22(this.getResult22());
        registry.setResult0(this.getResult0());
        registry.setResult2(this.getResult2());
        registry.setResult4(this.getResult4());
        registry.setResult6(this.getResult6());
        registry.setResultText(this.getResultText());
        registry.setIsText(this.isText());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkParameterId(this.getFkParameterId());
        registry.setFkLotId(this.getFkLotId());
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
        }

        return can;
    }
}
