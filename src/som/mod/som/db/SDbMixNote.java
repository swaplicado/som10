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
public class SDbMixNote extends SDbRegistryUser {

    protected int mnPkMixId;
    protected int mnPkNoteId;
    protected String msNote;

    public SDbMixNote() {
        super(SModConsts.S_MIX_NOTE);
        initRegistry();
    }

    public void setPkMixId(int n) { mnPkMixId = n; }
    public void setPkNoteId(int n) { mnPkNoteId = n; }
    public void setNote(String s) { msNote = s; }

    public int getPkMixId() { return mnPkMixId; }
    public int getPkNoteId() { return mnPkNoteId; }
    public String getNote() { return msNote; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMixId = pk[0];
        mnPkNoteId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMixId, mnPkNoteId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMixId = 0;
        mnPkNoteId = 0;
        msNote = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mix = " + mnPkMixId + " AND id_note =  " + mnPkNoteId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mix = " + pk[0] + " AND id_note =  " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkNoteId = 0;

        msSql = "SELECT COALESCE(MAX(id_note), 0) + 1 FROM " + getSqlTable() + " WHERE id_mix = " + mnPkMixId + " ";
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
            mnPkMixId = resultSet.getInt("id_mix");
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
                    mnPkMixId + ", " +
                    mnPkNoteId + ", " +
                    "'" + msNote + "' " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //mnPkMixId + ", " +
                    //mnPkNoteId + ", '" +
                    "note = '" + msNote + "' " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMixNote clone() throws CloneNotSupportedException {
        SDbMixNote registry = new SDbMixNote();

        registry.setPkMixId(this.getPkMixId());
        registry.setPkNoteId(this.getPkNoteId());
        registry.setNote(this.getNote());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
