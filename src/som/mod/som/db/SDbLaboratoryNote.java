/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas
 */
public class SDbLaboratoryNote extends SDbRegistry {

    protected int mnPkLaboratoryId;
    protected int mnPkNoteId;
    protected String msNote;

    public SDbLaboratoryNote() {
        super(SModConsts.S_LAB_NOTE);
        initRegistry();
    }

    public void setPkLaboratoryId(int n) { mnPkLaboratoryId = n; }
    public void setPkNoteId(int n) { mnPkNoteId = n; }
    public void setNote(String s) { msNote = s; }

    public int getPkLaboratoryId() { return mnPkLaboratoryId; }
    public int getPkNoteId() { return mnPkNoteId; }
    public String getNote() { return msNote; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLaboratoryId = pk[0];
        mnPkNoteId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLaboratoryId, mnPkNoteId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLaboratoryId = 0;
        mnPkNoteId = 0;
        msNote = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_lab = " + mnPkLaboratoryId + " AND " +
                "id_note = " + mnPkNoteId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lab = " + pk[0] + " AND " +
                "id_note = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkNoteId = 0;

        msSql = "SELECT COALESCE(MAX(id_note), 0) + 1 FROM " + getSqlTable()+ " " +
                "WHERE id_lab = " + mnPkLaboratoryId + " ";
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
            mnPkLaboratoryId = resultSet.getInt("id_lab");
            mnPkNoteId = resultSet.getInt("id_note");
            msNote = resultSet.getString("note");

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

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkLaboratoryId + ", " +
                    mnPkNoteId + ", " +
                    "'" + msNote + "' " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_lab = " + mnPkLaboratoryId + ", " +
                    //"id_note = " + mnPkNoteId + ", " +
                    "note = '" + msNote + "' " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbLaboratoryNote clone() throws CloneNotSupportedException {
        SDbLaboratoryNote registry = new SDbLaboratoryNote();

        registry.setPkLaboratoryId(this.getPkLaboratoryId());
        registry.setPkNoteId(this.getPkNoteId());
        registry.setNote(this.getNote());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
