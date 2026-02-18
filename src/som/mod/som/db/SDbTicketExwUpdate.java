/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDbTicketExwUpdate extends SDbRegistryUser {

    protected int mnPkTicketId;
    protected int mnPkUpdateId;
    protected String msNote;
    protected int mnFkOldTicketOriginId;
    protected int mnFkOldTicketDestinationId;
    protected int mnFkOldExwFacilityOriginId;
    protected int mnFkOldExwFacilityDestinationId;
    protected int mnFkNewTicketOriginId;
    protected int mnFkNewTicketDestinationId;
    protected int mnFkNewExwFacilityOriginId;
    protected int mnFkNewExwFacilityDestinationId;
    protected int mnFkUserId;
    protected Date mtTsUser;

    public SDbTicketExwUpdate() {
        super(SModConsts.S_TIC_EXW_UPD_LOG);
        initRegistry();
    }

    public void setPkTicketId(int n) { mnPkTicketId = n; }
    public void setPkUpdateId(int n) { mnPkUpdateId = n; }
    public void setNote(String s) { msNote = s; }
    public void setFkOldTicketOriginId(int n) { mnFkOldTicketOriginId = n; }
    public void setFkOldTicketDestinationId(int n) { mnFkOldTicketDestinationId = n; }
    public void setFkOldExwFacilityOriginId(int n) { mnFkOldExwFacilityOriginId = n; }
    public void setFkOldExwFacilityDestinationId(int n) { mnFkOldExwFacilityDestinationId = n; }
    public void setFkNewTicketOriginId(int n) { mnFkNewTicketOriginId = n; }
    public void setFkNewTicketDestinationId(int n) { mnFkNewTicketDestinationId = n; }
    public void setFkNewExwFacilityOriginId(int n) { mnFkNewExwFacilityOriginId = n; }
    public void setFkNewExwFacilityDestinationId(int n) { mnFkNewExwFacilityDestinationId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkTicketId() { return mnPkTicketId; }
    public int getPkUpdateId() { return mnPkUpdateId; }
    public String getNote() { return msNote; }
    public int getFkOldTicketOriginId() { return mnFkOldTicketOriginId; }
    public int getFkOldTicketDestinationId() { return mnFkOldTicketDestinationId; }
    public int getFkOldExwFacilityOriginId() { return mnFkOldExwFacilityOriginId; }
    public int getFkOldExwFacilityDestinationId() { return mnFkOldExwFacilityDestinationId; }
    public int getFkNewTicketOriginId() { return mnFkNewTicketOriginId; }
    public int getFkNewTicketDestinationId() { return mnFkNewTicketDestinationId; }
    public int getFkNewExwFacilityOriginId() { return mnFkNewExwFacilityOriginId; }
    public int getFkNewExwFacilityDestinationId() { return mnFkNewExwFacilityDestinationId; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTicketId = pk[0];
        mnPkUpdateId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTicketId, mnPkUpdateId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTicketId = 0;
        mnPkUpdateId = 0;
        msNote = "";
        mnFkOldTicketOriginId = 0;
        mnFkOldTicketDestinationId = 0;
        mnFkOldExwFacilityOriginId = 0;
        mnFkOldExwFacilityDestinationId = 0;
        mnFkNewTicketOriginId = 0;
        mnFkNewTicketDestinationId = 0;
        mnFkNewExwFacilityOriginId = 0;
        mnFkNewExwFacilityDestinationId = 0;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tic = " + mnPkTicketId + " AND id_upd = " + mnPkUpdateId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tic = " + pk[0] + " AND id_upd = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkUpdateId = 0;

        msSql = "SELECT COALESCE(MAX(id_upd), 0) + 1 FROM " + getSqlTable() + " WHERE id_tic = " + mnPkTicketId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkUpdateId = resultSet.getInt(1);
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
            mnPkTicketId = resultSet.getInt("id_tic");
            mnPkUpdateId = resultSet.getInt("id_upd");
            msNote = resultSet.getString("note");
            mnFkOldTicketOriginId = resultSet.getInt("fk_old_tic_orig");
            mnFkOldTicketDestinationId = resultSet.getInt("fk_old_tic_dest");
            mnFkOldExwFacilityOriginId = resultSet.getInt("fk_old_exw_fac_orig");
            mnFkOldExwFacilityDestinationId = resultSet.getInt("fk_old_exw_fac_dest");
            mnFkNewTicketOriginId = resultSet.getInt("fk_new_tic_orig");
            mnFkNewTicketDestinationId = resultSet.getInt("fk_new_tic_dest");
            mnFkNewExwFacilityOriginId = resultSet.getInt("fk_new_exw_fac_orig");
            mnFkNewExwFacilityDestinationId = resultSet.getInt("fk_new_exw_fac_dest");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

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
            mnFkUserId = session.getUser().getPkUserId();

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkTicketId + ", " + 
                    mnPkUpdateId + ", " + 
                    "'" + msNote + "', " + 
                    mnFkOldTicketOriginId + ", " + 
                    mnFkOldTicketDestinationId + ", " + 
                    mnFkOldExwFacilityOriginId + ", " + 
                    mnFkOldExwFacilityDestinationId + ", " + 
                    mnFkNewTicketOriginId + ", " + 
                    mnFkNewTicketDestinationId + ", " + 
                    mnFkNewExwFacilityOriginId + ", " + 
                    mnFkNewExwFacilityDestinationId + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }

        session.getStatement().execute(msSql);
        
        // update ticket as well:
        
        SDbTicket ticket = (SDbTicket) session.readRegistry(SModConsts.S_TIC, new int[] { mnPkTicketId });
        
        ticket.setFkTicketOriginId(mnFkNewTicketOriginId);
        ticket.setFkTicketDestinationId(mnFkNewTicketDestinationId);
        ticket.setFkExwFacilityOriginId(mnFkNewExwFacilityOriginId);
        ticket.setFkExwFacilityDestinationId(mnFkNewExwFacilityDestinationId);
        
        ticket.applyExwUpdate(session);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbTicketExwUpdate clone() throws CloneNotSupportedException {
        SDbTicketExwUpdate registry = new SDbTicketExwUpdate();

        registry.setPkTicketId(this.getPkTicketId());
        registry.setPkUpdateId(this.getPkUpdateId());
        registry.setNote(this.getNote());
        registry.setFkOldTicketOriginId(this.getFkOldTicketOriginId());
        registry.setFkOldTicketDestinationId(this.getFkOldTicketDestinationId());
        registry.setFkOldExwFacilityOriginId(this.getFkOldExwFacilityOriginId());
        registry.setFkOldExwFacilityDestinationId(this.getFkOldExwFacilityDestinationId());
        registry.setFkNewTicketOriginId(this.getFkNewTicketOriginId());
        registry.setFkNewTicketDestinationId(this.getFkNewTicketDestinationId());
        registry.setFkNewExwFacilityOriginId(this.getFkNewExwFacilityOriginId());
        registry.setFkNewExwFacilityDestinationId(this.getFkNewExwFacilityDestinationId());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
