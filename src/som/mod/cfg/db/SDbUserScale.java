/*
 * To change this template, choose Tools | Templates
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
 * @author Sergio Flores
 */
public class SDbUserScale extends SDbRegistry implements SGridRow {

    protected int mnPkUserId;
    protected int mnPkScaleId;

    protected String msXtaScale;
    protected boolean mbXtaSelected;

    public SDbUserScale() {
        super(SModConsts.CU_USR_SCA);
        initRegistry();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkScaleId(int n) { mnPkScaleId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkScaleId() { return mnPkScaleId; }

    public void setXtaScale(String s) { msXtaScale = s; }
    public void setXtaSelected(boolean b) { mbXtaSelected = b; }

    public String getXtaScale() { return msXtaScale; }
    public boolean isXtaSelected() { return mbXtaSelected; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
        mnPkScaleId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkScaleId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkUserId = 0;
        mnPkScaleId = 0;

        msXtaScale = "";
        mbXtaSelected = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_usr = " + mnPkUserId + " AND "
                + "id_sca = " + mnPkScaleId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " AND "
                + "id_sca = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession sgs) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnPkUserId = resultSet.getInt("id_usr");
            mnPkScaleId = resultSet.getInt("id_sca");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkUserId + ", " +
                    mnPkScaleId + " " +
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
    public SDbUserScale clone() throws CloneNotSupportedException {
        SDbUserScale registry = new SDbUserScale();

        registry.setPkUserId(this.getPkUserId());
        registry.setPkScaleId(this.getPkScaleId());

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
                value = msXtaScale;
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
