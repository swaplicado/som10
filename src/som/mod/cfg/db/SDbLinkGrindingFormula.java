/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.cfg.db;

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
public class SDbLinkGrindingFormula extends SDbRegistryUser {

    protected int mnPkFormulaId;
    //protected boolean mbDeleted;
    protected String msFormula;
    protected String msRowText;
    protected int mnOrder;
    protected int mnFkLinkId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbLinkGrindingFormula() {
        super(SModConsts.CU_LINK_FORMULAS);
    }

    public void setPkFormulaId(int n) { mnPkFormulaId = n; }
    public void setFormula(String s) { this.msFormula = s; }
    public void setRowText(String s) { this.msRowText = s; }
    public void setOrder(int n) { mnOrder = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkLinkId(int n) { mnFkLinkId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkFormulaId() { return mnPkFormulaId; }
    public String getFormula() { return msFormula; }
    public String getRowText() { return msRowText; }
    public int getOrder() { return mnOrder; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkLinkId() { return mnFkLinkId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkFormulaId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkFormulaId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkFormulaId = 0;
        msFormula = "";
        msRowText = "";
        mnOrder = 0;
        mbDeleted = false;
        mnFkLinkId = 0;
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
        return "WHERE id_formula = " + mnPkFormulaId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_formula = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkFormulaId = 0;

        msSql = "SELECT COALESCE(MAX(id_formula), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkFormulaId = resultSet.getInt(1);
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
            mnPkFormulaId = resultSet.getInt("id_formula");
            msFormula = resultSet.getString("formula");
            msRowText = resultSet.getString("row_text");
            mnOrder = resultSet.getInt("form_order");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkLinkId = resultSet.getInt("fk_link");
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
                    mnPkFormulaId + ", " +
                    "'" + msFormula + "', " +
                    "'" + msRowText + "', " +
                    mnOrder + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkLinkId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "formula = '" + msFormula + "', " +
                    "row_text = '" + msRowText + "', " +
                    "form_order = " + mnOrder + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_parameter_id = " + mnFkLinkId + ", " +
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
    public SDbLinkGrindingFormula clone() throws CloneNotSupportedException {
        SDbLinkGrindingFormula registry = new SDbLinkGrindingFormula();

        registry.setPkFormulaId(this.getPkFormulaId());
        registry.setFormula(this.getFormula());
        registry.setRowText(this.getRowText());
        registry.setOrder(this.getOrder());
        registry.setDeleted(this.isDeleted());
        registry.setFkLinkId(this.getFkLinkId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
