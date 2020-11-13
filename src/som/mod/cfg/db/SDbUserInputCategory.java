/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Isabel Servín
 */
public final class SDbUserInputCategory extends SDbRegistry implements SGridRow {
    
    protected int mnPkUserId;
    protected int mnPkInputCategoryId;
    
    protected String msXtaInputCategoryName;
    protected String msXtaInputCategoryCode;
    protected boolean mbXtaSelected;

    public SDbUserInputCategory() {
        super(SModConsts.CU_USR_INP_CT);
        initRegistry();
    }
    
    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkInputCategoryId(int n) { mnPkInputCategoryId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkInputCategoryId() { return mnPkInputCategoryId; }

    public void setXtaInputCategoryName(String s) { msXtaInputCategoryName = s; }
    public void setXtaInputCategoryCode(String s) { msXtaInputCategoryCode = s; }
    public void setXtaSelected(boolean b) { mbXtaSelected = b; }
    
    public String getXtaInputCategoryName() { return msXtaInputCategoryName; }
    public String getXtaInputCategoryCode() { return msXtaInputCategoryCode; }
    public boolean isXtaSelected() { return mbXtaSelected; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
        mnPkInputCategoryId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkInputCategoryId };
    }

    @Override
    public void initRegistry() {
        mnPkUserId = 0;
        mnPkInputCategoryId = 0;
        
        msXtaInputCategoryName = "";
        mbXtaSelected = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_usr = " + mnPkUserId + " AND "
                + "id_inp_ct = " + mnPkInputCategoryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " AND "
                + "id_inp_ct = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession sgs) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;

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
            mnPkInputCategoryId = resultSet.getInt("id_inp_ct");
            
            String sqlAux = "SELECT name, code "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_INP_CT) + " "
                    + "WHERE id_inp_ct = " + mnPkInputCategoryId + ";";
            resultSet = session.getStatement().executeQuery(sqlAux);
            if (resultSet.next()) {
                msXtaInputCategoryName = resultSet.getString("name");
                msXtaInputCategoryCode = resultSet.getString("code");
            }

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        verifyRegistryNew(session);

        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkUserId + ", " +
                    mnPkInputCategoryId + " " +
                    ")";
        }
        else {
            throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbUserInputCategory clone() throws CloneNotSupportedException {
        SDbUserInputCategory registry = new SDbUserInputCategory();

        registry.setPkUserId(this.getPkUserId());
        registry.setPkInputCategoryId(this.getPkInputCategoryId());

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
        return false;
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

        switch (row) {
            case 0:
                value = msXtaInputCategoryName;
                break;
            case 1:
                value = mbXtaSelected;
                break;
            default:
        }

        return value;
    }
    
    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
                break;
            case 1:
                mbRegistryEdited = true;
                mbXtaSelected = (Boolean) value;
                break;
            default:
        }
    }
}

