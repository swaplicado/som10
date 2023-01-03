/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

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
public class SDbGrindingItemParamHeader extends SDbRegistryUser {

    protected int mnPkHeaderId;
    protected int mnViewOrder;
    protected String msLabelText;
    protected String msParametersIds;
    protected String msUnitSymbol;

    /*
    protected boolean mbDeleted;
    */
    
    protected int mnFkItemId;
    
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbGrindingItemParamHeader() {
        super(SModConsts.SU_GRINDING_ITEM_PARAM_HEADER);
    }

    public void setPkHeaderId(int n) { mnPkHeaderId = n; }
    public void setViewOrder(int n) { mnViewOrder = n; }
    public void setLabelText(String s) { msLabelText = s; }
    public void setParametersIds(String s) { msParametersIds = s; }
    public void setUnitSymbol(String s) { msUnitSymbol = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkHeaderId() { return mnPkHeaderId; }
    public int getViewOrder() { return mnViewOrder; }
    public String getLabelText() { return msLabelText; }
    public String getParametersIds() { return msParametersIds; }
    public String getUnitSymbol() { return msUnitSymbol; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkHeaderId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkHeaderId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkHeaderId = 0;
        mnViewOrder = 0;
        msLabelText = "";
        msParametersIds = "";
        msUnitSymbol = "";
        mbDeleted = false;
        mnFkItemId = 0;
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
        return "WHERE id_header = " + mnPkHeaderId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_header = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkHeaderId = 0;

        msSql = "SELECT COALESCE(MAX(id_header), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkHeaderId = resultSet.getInt(1);
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
            mnPkHeaderId = resultSet.getInt("id_header");
            mnViewOrder = resultSet.getInt("view_order");
            msLabelText = resultSet.getString("label_text");
            msParametersIds = resultSet.getString("parameters_ids");
            msUnitSymbol = resultSet.getString("unit_symbol");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fk_item_id");
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
                    mnPkHeaderId + ", " + 
                    mnViewOrder + ", " + 
                    "'" + msLabelText + "', " + 
                    "'" + msParametersIds + "', " + 
                    "'" + msUnitSymbol + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_header = " + mnPkHeaderId + ", " +
                    "view_order = " + mnViewOrder + ", " +
                    "label_text = '" + msLabelText + "', " +
                    "parameters_ids = '" + msParametersIds + "', " +
                    "unit_symbol = '" + msUnitSymbol + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_item_id = " + mnFkItemId + ", " +
//                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + ", " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbGrindingItemParamHeader clone() throws CloneNotSupportedException {
        SDbGrindingItemParamHeader registry = new SDbGrindingItemParamHeader();

        registry.setPkHeaderId(this.getPkHeaderId());
        registry.setViewOrder(this.getViewOrder());
        registry.setLabelText(this.getLabelText());
        registry.setParametersIds(this.getParametersIds());
        registry.setUnitSymbol(this.getUnitSymbol());
        registry.setDeleted(this.isDeleted());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
