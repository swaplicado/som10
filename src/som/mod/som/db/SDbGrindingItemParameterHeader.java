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
public class SDbGrindingItemParameterHeader extends SDbRegistryUser {

    protected int mnPkItemParameterId;
    protected int mnViewOrder;
    protected String msLabelText;
    protected String msParameters;
    protected String msUnitSimbol;
    //protected boolean mbDeleted;
    protected int mnFkItemId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected int[] maAuxParameters;

    public SDbGrindingItemParameterHeader() {
        super(SModConsts.SU_GRINDING_ITEM_PARAM_HEADER);
    }

    public void setPkItemParameterId(int n) { mnPkItemParameterId = n; }
    public void setViewOrder(int n) { mnViewOrder = n; }
    public void setLabelText(String s) { msLabelText = s; }
    public void setParameters(String s) { msParameters = s; }
    public void setUnitSimbol(String s) { msUnitSimbol = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemParameterId() { return mnPkItemParameterId; }
    public int getViewOrder() { return mnViewOrder; }
    public String getLabelText() { return msLabelText; }
    public String getParameters() { return msParameters; }
    public String getUnitSimbol() { return msUnitSimbol; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public int[] getAuxParameters() { return maAuxParameters; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemParameterId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemParameterId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkItemParameterId = 0;
        mnViewOrder = 0;
        msLabelText = "";
        msParameters = "";
        msUnitSimbol = "";
        mbDeleted = false;
        mnFkItemId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maAuxParameters = new int[] {};
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_itm_prm = " + mnPkItemParameterId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_itm_prm = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkItemParameterId = 0;

        msSql = "SELECT COALESCE(MAX(id_itm_prm), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkItemParameterId = resultSet.getInt(1);
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
            mnPkItemParameterId = resultSet.getInt("id_itm_prm");
            mnViewOrder = resultSet.getInt("view_order");
            msLabelText = resultSet.getString("label_text");
            msParameters = resultSet.getString("parameters_ids");
            msUnitSimbol = resultSet.getString("unit_simbol");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fk_item_id");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }
        
        if (! msParameters.isEmpty()) {
            String[] params = msParameters.split(",");
            maAuxParameters = new int[params.length];
            for (int i = 0; i < params.length; i++) {
                maAuxParameters[i] = Integer.parseInt(params[i]);
            }
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
                    mnPkItemParameterId + ", " +
                    mnViewOrder + ", " +
                    "'" + msLabelText + "', " +
                    "'" + msParameters + "', " +
                    "'" + msUnitSimbol + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkItemId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "view_order = " + mnViewOrder + ", " +
                    "label_text = '" + msLabelText + "', " +
                    "parameters_ids = '" + msParameters + "', " +
                    "unit_simbol = '" + msUnitSimbol + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_item_id = " + mnFkItemId + ", " +
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
    public SDbGrindingItemParameterHeader clone() throws CloneNotSupportedException {
        SDbGrindingItemParameterHeader registry = new SDbGrindingItemParameterHeader();

        registry.setPkItemParameterId(this.getPkItemParameterId());
        registry.setViewOrder(this.getViewOrder());
        registry.setLabelText(this.getLabelText());
        registry.setParameters(this.getParameters());
        registry.setUnitSimbol(this.getUnitSimbol());
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
