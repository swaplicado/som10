/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import som.mod.som.data.SCaptureConfiguration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.som.data.SGrindingReport;

/**
 *
 * @author Edwin Carmona
 */
public class SDbGrindingResult extends SDbRegistryUser implements SGridRow {

    protected int mnPkResultId;
    protected Date mtDateCapture;
    protected int mnOrder;
    protected String mdResult8;
    protected String mdResult10;
    protected String mdResult12;
    protected String mdResult14;
    protected String mdResult16;
    protected String mdResult18;
    protected String mdResult20;
    protected String mdResult22;
    protected String mdResult0;
    protected String mdResult2;
    protected String mdResult4;
    protected String mdResult6;
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
    
    protected String msParameterAux;
    protected SCaptureConfiguration moConfigurationAux;

    public SDbGrindingResult() {
        super(SModConsts.SU_GRINDING_RESULTS);
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
    public void setResult8(String s) { mdResult8 = s; }
    public void setResult10(String s) { mdResult10 = s; }
    public void setResult12(String s) { mdResult12 = s; }
    public void setResult14(String s) { mdResult14 = s; }
    public void setResult16(String s) { mdResult16 = s; }
    public void setResult18(String s) { mdResult18 = s; }
    public void setResult20(String s) { mdResult20 = s; }
    public void setResult22(String s) { mdResult22 = s; }
    public void setResult0(String s) { mdResult0 = s; }
    public void setResult2(String s) { mdResult2 = s; }
    public void setResult4(String s) { mdResult4 = s; }
    public void setResult6(String s) { mdResult6 = s; }
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
    
    public void setParameterAux(String msParameterAux) { this.msParameterAux = msParameterAux; }
    public void setCaptureConfigurationAux(SCaptureConfiguration oCfgAux) { this.moConfigurationAux = oCfgAux; }

    public int getPkResultId() { return mnPkResultId; }
    public Date getDateCapture() { return mtDateCapture; }
    public int getOrder() { return mnOrder; }
    public String getResult8() { return mdResult8; }
    public String getResult10() { return mdResult10; }
    public String getResult12() { return mdResult12; }
    public String getResult14() { return mdResult14; }
    public String getResult16() { return mdResult16; }
    public String getResult18() { return mdResult18; }
    public String getResult20() { return mdResult20; }
    public String getResult22() { return mdResult22; }
    public String getResult0() { return mdResult0; }
    public String getResult2() { return mdResult2; }
    public String getResult4() { return mdResult4; }
    public String getResult6() { return mdResult6; }
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
    
    public String getParameterAux() { return msParameterAux; }
    public SCaptureConfiguration getCaptureConfigurationAux() { return moConfigurationAux; }
    
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
        mdResult8 = "";
        mdResult10 = "";
        mdResult12 = "";
        mdResult14 = "";
        mdResult16 = "";
        mdResult18 = "";
        mdResult20 = "";
        mdResult22 = "";
        mdResult0 = "";
        mdResult2 = "";
        mdResult4 = "";
        mdResult6 = "";
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
        
        msParameterAux = "";
        moConfigurationAux = null;
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
            mdResult8 = resultSet.getString("result_08");
            mdResult10 = resultSet.getString("result_10");
            mdResult12 = resultSet.getString("result_12");
            mdResult14 = resultSet.getString("result_14");
            mdResult16 = resultSet.getString("result_16");
            mdResult18 = resultSet.getString("result_18");
            mdResult20 = resultSet.getString("result_20");
            mdResult22 = resultSet.getString("result_22");
            mdResult0 = resultSet.getString("result_00");
            mdResult2 = resultSet.getString("result_02");
            mdResult4 = resultSet.getString("result_04");
            mdResult6 = resultSet.getString("result_06");
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
                    "'" + mdResult8 + "', " +
                    "'" + mdResult10 + "', " +
                    "'" + mdResult12 + "', " +
                    "'" + mdResult14 + "', " +
                    "'" + mdResult16 + "', " +
                    "'" + mdResult18 + "', " +
                    "'" + mdResult20 + "', " +
                    "'" + mdResult22 + "', " +
                    "'" + mdResult0 + "', " +
                    "'" + mdResult2 + "', " +
                    "'" + mdResult4 + "', " +
                    "'" + mdResult6 + "', " +
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
                    "result_08 = '" + mdResult8 + "', " +
                    "result_10 = '" + mdResult10 + "', " +
                    "result_12 = '" + mdResult12 + "', " +
                    "result_14 = '" + mdResult14 + "', " +
                    "result_16 = '" + mdResult16 + "', " +
                    "result_18 = '" + mdResult18 + "', " +
                    "result_20 = '" + mdResult20 + "', " +
                    "result_22 = '" + mdResult22 + "', " +
                    "result_00 = '" + mdResult0 + "', " +
                    "result_02 = '" + mdResult2 + "', " +
                    "result_04 = '" + mdResult4 + "', " +
                    "result_06 = '" + mdResult6 + "', " +
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

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
    }

    @Override
    public boolean isRowSystem() {
        return isSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return isDeletable();
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = mnPkResultId;
                break;
            case 1:
                value = msParameterAux;
                break;
            case 2:
                value = mdResult8;
                break;
            case 3:
                value = mdResult10;
                break;
            case 4:
                value = mdResult12;
                break;
            case 5:
                value = mdResult14;
                break;
            case 6:
                value = mdResult16;
                break;
            case 7:
                value = mdResult18;
                break;
            case 8:
                value = mdResult20;
                break;
            case 9:
                value = mdResult22;
                break;
            case 10:
                value = mdResult0;
                break;
            case 11:
                value = mdResult2;
                break;
            case 12:
                value = mdResult4;
                break;
            case 13:
                value = mdResult6;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        String v = (String) value;
        String absValue = SGrindingReport.isNumeric(v.replace(",", "")) ? SLibUtils.DecimalFormatValue4D.format(Double.parseDouble(v)) : v;
        switch (col) {
            case 2:
                mdResult8 = absValue;
                break;
            case 3:
                mdResult10 = absValue;
                break;
            case 4:
                mdResult12 = absValue;
                break;
            case 5:
                mdResult14 = absValue;
                break;
            case 6:
                mdResult16 = absValue;
                break;
            case 7:
                mdResult18 = absValue;
                break;
            case 8:
                mdResult20 = absValue;
                break;
            case 9:
                mdResult22 = absValue;
                break;
            case 10:
                mdResult0 = absValue;
                break;
            case 11:
                mdResult2 = absValue;
                break;
            case 12:
                mdResult4 = absValue;
                break;
            case 13:
                mdResult6 = absValue;
                break;
            
            default:
        }
    }
}
