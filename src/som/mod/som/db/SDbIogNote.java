/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbIogNote extends SDbRegistryUser {

    protected int mnPkIogId;
    protected int mnPkNoteId;
    protected String msNote;

    public SDbIogNote() {
        super(SModConsts.S_IOG_NOTE);
        initRegistry();
    }

    public void setPkIogId(int n) { mnPkIogId = n; }
    public void setPkNoteId(int n) { mnPkNoteId = n; }
    public void setNote(String s) { msNote = s; }

    public int getPkIogId() { return mnPkIogId; }
    public int getPkNoteId() { return mnPkNoteId; }
    public String getNote() { return msNote; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkIogId = pk[0];
        mnPkNoteId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkIogId, mnPkNoteId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkIogId = 0;
        mnPkNoteId = 0;
        msNote = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_iog = " + mnPkIogId + " AND id_note =  " + mnPkNoteId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_iog = " + pk[0] + " AND id_note =  " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkNoteId = 0;

        msSql = "SELECT COALESCE(MAX(id_note), 0) + 1 FROM " + getSqlTable() + " WHERE id_iog = " + mnPkIogId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkNoteId = resultSet.getInt(1);
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
            mnPkIogId = resultSet.getInt("id_iog");
            mnPkNoteId = resultSet.getInt("id_note");
            msNote = resultSet.getString("note");

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
            mbDisableable = false;
            mbDeletable = true;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkIogId + ", " +
                    mnPkNoteId + ", " +
                    "'" + msNote + "' " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //mnPkIogId + ", " +
                    //mnPkNoteId + ", " +
                    "note = '" + msNote + "' " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbIogNote clone() throws CloneNotSupportedException {
        SDbIogNote registry = new SDbIogNote();

        registry.setPkIogId(this.getPkIogId());
        registry.setPkNoteId(this.getPkNoteId());
        registry.setNote(this.getNote());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
