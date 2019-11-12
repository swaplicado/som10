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
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbStockDay extends SDbRegistryUser implements SGridRow {

    protected int mnPkYearId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkCompanyId;
    protected int mnPkBranchId;
    protected int mnPkWarehouseId;
    protected int mnPkDayId;
    protected Date mtDate;
    protected double mdEmptiness;
    protected double mdCull;
    protected double mdStockDay;
    protected double mdStockSystem;
    protected String msNote;
    protected boolean mbEmpty;
    protected boolean mbStockDifferenceSkipped;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected boolean mbXtaCopy;
    protected boolean mbXtaRegistryEditing;
    protected String msXtaWarehouseCode;
    protected String msXtaItemCode;
    protected String msXtaItem;
    protected String msXtaUnit;
    protected double mdXtaStock;
    protected double mdXtaDifference;
    protected double mdXtaHeight;
    protected double mdXtaCapacityTotalKg;
    protected double mdXtaCapacityAvailable;
    protected int mnXtaItemBefore;

    public SDbStockDay() {
        super(SModConsts.S_STK_DAY);
        initRegistry();
    }

    private boolean validateOpenPeriod(SGuiSession session) throws Exception {
        boolean b = true;
        SDbMfgEstimation estimation = null;

        // Validate that day is open:

        try {
            estimation = new SDbMfgEstimation();
            estimation.obtainProductionEstimateByDate(session, mtDate, null);

            if (estimation.isClosed()) {

                msQueryResult = "El período está cerrado por estimación de la producción.";
                b = false;
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        return b;
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setPkDayId(int n) { mnPkDayId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setEmptiness(double d) { mdEmptiness = d; }
    public void setCull(double d) { mdCull = d; }
    public void setStockDay(double d) { mdStockDay = d; }
    public void setStockSystem(double d) { mdStockSystem = d; }
    public void setNote(String s) { msNote = s; }
    public void setEmpty(boolean b) { mbEmpty = b; }
    public void setStockDifferenceSkipped(boolean b) { mbStockDifferenceSkipped = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public int getPkDayId() { return mnPkDayId; }
    public Date getDate() { return mtDate; }
    public double getEmptiness() { return mdEmptiness; }
    public double getCull() { return mdCull; }
    public double getStockDay() { return mdStockDay; }
    public double getStockSystem() { return mdStockSystem; }
    public String getNote() { return msNote; }
    public boolean isEmpty() { return mbEmpty; }
    public boolean isStockDifferenceSkipped() { return mbStockDifferenceSkipped; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setXtaCopy(boolean b) { mbXtaCopy = b; }
    public void setXtaRegistryEditing(boolean b) { mbXtaRegistryEditing = b; }
    public void setXtaWarehouseCode(String s) { msXtaWarehouseCode = s; }
    public void setXtaItemCode(String s) { msXtaItemCode = s; }
    public void setXtaItem(String s) { msXtaItem = s; }
    public void setXtaUnit(String s) { msXtaUnit = s; }
    public void setXtaStock(Double d) { mdXtaStock = d; }
    public void setXtaDifference(Double d) { mdXtaDifference = d; }
    public void setXtaHeight(Double d) { mdXtaHeight = d; }
    public void setXtaCapacityTotalKg(Double d) { mdXtaCapacityTotalKg = d; }
    public void setXtaCapacityAvailable(Double d) { mdXtaCapacityAvailable = d; }
    public void setXtaItemBefore(int n) { mnXtaItemBefore = n; }

    public boolean getXtaCopy() { return mbXtaCopy; }
    public boolean getXtaRegistryEditing() { return mbXtaRegistryEditing; }
    public String getXtaWarehouseCode() { return msXtaWarehouseCode; }
    public String getXtaItemCode() { return msXtaItemCode; }
    public String getXtaItem() { return msXtaItem; }
    public String getXtaUnit() { return msXtaUnit; }
    public double getXtaStock() { return mdXtaStock; }
    public double getXtaDifference() { return mdXtaDifference; }
    public double getXtaHeight() { return mdXtaHeight; }
    public double getXtaCapacityTotalKg() { return mdXtaCapacityTotalKg; }
    public double getXtaCapacityAvailable() { return mdXtaCapacityAvailable; }
    public int getXtaItemBefore() { return mnXtaItemBefore; }

    public void obtainXtaValues(SGuiSession session, int[] key) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT COALESCE(SUM(s.mov_in - s.mov_out), 0) AS f_stk, wa.code AS f_wcode, i.code AS f_icode, i.name AS f_iname, " +
                "u.code AS f_ucode, 2 AS f_dif, wa.dim_heig AS f_heig, wa.cap_real_lt AS f_cap_real, 3 AS f_cap_avai " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
            "s.id_item = i.id_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
            "s.id_unit = u.id_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wa ON " +
            "s.id_co = wa.id_co AND s.id_cob = wa.id_cob AND s.id_wah = wa.id_wah " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
            "s.id_div = d.id_div " +
            "WHERE s.id_year = " + key[0] + " AND s.id_item = " + key[1] + " AND s.id_unit = " + key[2] + " AND s.id_co = " + key[3] +
            " AND s.id_cob = " + key[4] + " AND s.id_wah = " + key[5] + " AND s.b_del = 0 " +
            "ORDER BY wa.code, i.code, i.name ";

        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            msXtaWarehouseCode = resultSet.getString("f_wcode");
            msXtaItemCode = resultSet.getString("f_icode");
            msXtaItem = resultSet.getString("f_iname");
            msXtaUnit = resultSet.getString("f_ucode");
            mdXtaStock = resultSet.getDouble("f_stk");
            mdXtaDifference = resultSet.getDouble("f_dif");
            mdXtaHeight = resultSet.getDouble("f_heig");
            mdXtaCapacityTotalKg = resultSet.getDouble("f_cap_real");
            mdXtaCapacityAvailable = resultSet.getDouble("f_cap_avai");
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkItemId = pk[1];
        mnPkUnitId = pk[2];
        mnPkCompanyId = pk[3];
        mnPkBranchId = pk[4];
        mnPkWarehouseId = pk[5];
        mnPkDayId = pk[6];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkItemId, mnPkUnitId, mnPkCompanyId, mnPkBranchId, mnPkWarehouseId, mnPkDayId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkYearId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkCompanyId = 0;
        mnPkBranchId = 0;
        mnPkWarehouseId = 0;
        mnPkDayId = 0;
        mtDate = null;
        mdEmptiness = 0;
        mdCull = 0;
        mdStockDay = 0;
        mdStockSystem = 0;
        msNote = "";
        mbEmpty = false;
        mbStockDifferenceSkipped = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mbXtaCopy = false;
        mbXtaRegistryEditing = false;
        msXtaWarehouseCode = "";
        msXtaItemCode = "";
        msXtaItem = "";
        msXtaUnit = "";
        mdXtaStock = 0;
        mdXtaDifference = 0;
        mdXtaHeight = 0;
        mdXtaCapacityTotalKg = 0;
        mdXtaCapacityAvailable = 0;
        mnXtaItemBefore = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " AND " +
            "id_item = " + (mnXtaItemBefore != mnPkItemId ? mnXtaItemBefore : mnPkItemId) + " AND " +
            "id_unit = " + mnPkUnitId + " AND " +
            "id_co = " + mnPkCompanyId + " AND " +
            "id_cob = " + mnPkBranchId + " AND " +
            "id_wah = " + mnPkWarehouseId + " AND " +
            "id_day = " + mnPkDayId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE s.id_year = " + pk[0] + " AND " +
            "s.id_item = " + pk[1] + " AND " +
            "s.id_unit = " + pk[2] + " AND " +
            "s.id_co = " + pk[3] + " AND " +
            "s.id_cob = " + pk[4] + " AND " +
            "s.id_wah = " + pk[5] + " AND " +
            "s.id_day = " + pk[6] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkDayId = 0;

        msSql = "SELECT COALESCE(MAX(id_day), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDayId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT s.*, wa.code AS f_wcode, i.code AS f_icode, i.name AS f_iname, u.code AS f_ucode, 2 AS f_dif, " +
                "wa.dim_heig AS f_heig, wa.cap_real_lt AS f_cap_real, 3 AS f_cap_avai, " +
                "(SELECT COALESCE(SUM(s.mov_in - s.mov_out), 0) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "s.id_item = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "s.id_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wa ON " +
                "s.id_co = wa.id_co AND s.id_cob = wa.id_cob AND s.id_wah = wa.id_wah " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
                "s.id_div = d.id_div " +
                "WHERE s.id_year = " + pk[0] + " AND s.id_item = " + pk[1] + " AND s.id_unit = " + pk[2] + " AND s.id_co = " + pk[3] +
                " AND s.id_cob = " + pk[4] + " AND s.id_wah = " + pk[5] + " AND s.b_del = 0) AS f_stk " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS s " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON s.id_item = i.id_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON s.id_unit = u.id_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wa ON s.id_co = wa.id_co AND s.id_cob = wa.id_cob AND s.id_wah = wa.id_wah " +
            getSqlWhere(pk) +
            "ORDER BY wa.code, i.code, i.name ";

        //msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkYearId = resultSet.getInt("s.id_year");
            mnPkItemId = resultSet.getInt("s.id_item");
            mnPkUnitId = resultSet.getInt("s.id_unit");
            mnPkCompanyId = resultSet.getInt("s.id_co");
            mnPkBranchId = resultSet.getInt("s.id_cob");
            mnPkWarehouseId = resultSet.getInt("s.id_wah");
            mnPkDayId = resultSet.getInt("s.id_day");
            mtDate = resultSet.getDate("s.dt");
            mdEmptiness = resultSet.getDouble("s.emp");
            mdCull = resultSet.getDouble("s.cull");
            mdStockDay = resultSet.getDouble("s.stk_day");
            mdStockSystem = resultSet.getDouble("s.stk_sys");
            msNote = resultSet.getString("s.note");
            mbEmpty = resultSet.getBoolean("s.b_emp");
            mbStockDifferenceSkipped = resultSet.getBoolean("s.b_stk_dif_skp");
            mbDeleted = resultSet.getBoolean("s.b_del");
            mbSystem = resultSet.getBoolean("s.b_sys");
            mnFkUserInsertId = resultSet.getInt("s.fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("s.fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("s.ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("s.ts_usr_upd");

            msXtaWarehouseCode = resultSet.getString("f_wcode");
            msXtaItemCode = resultSet.getString("f_icode");
            msXtaItem = resultSet.getString("f_iname");
            msXtaUnit = resultSet.getString("f_ucode");
            mdXtaStock = resultSet.getDouble("f_stk");
            mdXtaDifference = resultSet.getDouble("f_dif");
            mdXtaHeight = resultSet.getDouble("f_heig");
            mdXtaCapacityTotalKg = resultSet.getDouble("f_cap_real");
            mdXtaCapacityAvailable = resultSet.getDouble("f_cap_avai");
            
            mnXtaItemBefore = resultSet.getInt("s.id_item");

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
            mnXtaItemBefore = mnPkItemId;
            
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkYearId + ", " +
                mnPkItemId + ", " +
                mnPkUnitId + ", " +
                mnPkCompanyId + ", " +
                mnPkBranchId + ", " +
                mnPkWarehouseId + ", " +
                mnPkDayId + ", " +
                "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                mdEmptiness + ", " +
                mdCull + ", " +
                mdStockDay + ", " +
                mdStockSystem + ", " +
                "'" + msNote + "', " +
                (mbEmpty ? 1 : 0) + ", " +
                (mbStockDifferenceSkipped ? 1 : 0) + ", " +
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

            msSql = "UPDATE " + getSqlTable() + " AS s SET " +
                //"s.id_year = " + mnPkYearId + ", " +
                (mnXtaItemBefore != mnPkItemId ?  ("s.id_item = " + mnPkItemId + ", ") : "") +
                //"s.id_unit = " + mnPkUnitId + ", " +
                //"s.id_co = " + mnPkCompanyId + ", " +
                //"s.id_cob = " + mnPkBranchId + ", " +
                //"s.id_wah = " + mnPkWarehouseId + ", " +
                //"s.id_day = " + mnPkDayId + ", " +
                "s.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                "s.emp = " + mdEmptiness + ", " +
                "s.cull = " + mdCull + ", " +
                "s.stk_day = " + mdStockDay + ", " +
                "s.stk_sys = " + mdStockSystem + ", " +
                "s.note = '" + msNote + "', " +
                "s.b_emp = " + (mbEmpty ? 1 : 0) + ", " +
                "s.b_stk_dif_skp = " + (mbStockDifferenceSkipped ? 1 : 0) + ", " +
                "s.b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "s.b_sys = " + (mbSystem ? 1 : 0) + ", " +
                //"s.fk_usr_ins = " + mnFkUserInsertId + ", " +
                "s.fk_usr_upd = " + mnFkUserUpdateId + ", " +
                //"s.ts_usr_ins = " + "NOW()" + ", " +
                "s.ts_usr_upd = " + "NOW()" + " " +
                getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        return validateOpenPeriod(session);
    }

    @Override
    public boolean canSave(SGuiSession session) throws SQLException, Exception {
        return validateOpenPeriod(session);
    }

    @Override
    public SDbStockDay clone() throws CloneNotSupportedException {
        SDbStockDay registry = new SDbStockDay();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setPkDayId(this.getPkDayId());
        registry.setDate(this.getDate());
        registry.setEmptiness(this.getEmptiness());
        registry.setCull(this.getCull());
        registry.setStockDay(this.getStockDay());
        registry.setStockSystem(this.getStockSystem());
        registry.setNote(this.getNote());
        registry.setEmpty(this.isEmpty());
        registry.setStockDifferenceSkipped(this.isStockDifferenceSkipped());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setXtaWarehouseCode(this.getXtaWarehouseCode());
        registry.setXtaItemCode(this.getXtaItemCode());
        registry.setXtaItem(this.getXtaItem());
        registry.setXtaUnit(this.getXtaUnit());
        registry.setXtaStock(this.getXtaStock());
        registry.setXtaDifference(this.getXtaDifference());
        registry.setXtaHeight(this.getXtaHeight());
        registry.setXtaCapacityTotalKg(this.getXtaCapacityTotalKg());
        registry.setXtaCapacityAvailable(this.getXtaCapacityAvailable());
        registry.setXtaItemBefore(this.getXtaItemBefore());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
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
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(final boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msXtaWarehouseCode;
                break;
            case 1:
                value = mbEmpty;
                break;
            case 2:
                value = mdEmptiness;
                break;
            case 3:
                value = mdCull;
                break;
            case 4:
                value = msXtaItem;
                break;
            case 5:
                value = msXtaItemCode;
                break;
            case 6:
                value = msNote;
                break;
            case 7:
                value = mdXtaHeight;
                break;
            case 8:
                value = mdXtaCapacityTotalKg;
                break;
            case 9:
                value = mbStockDifferenceSkipped;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
