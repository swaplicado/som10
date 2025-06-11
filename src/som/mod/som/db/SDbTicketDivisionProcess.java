/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Isabel Servín
 */
public class SDbTicketDivisionProcess extends SDbRegistryUser {

    protected int mnPkTicketId;
    
    protected SDbTicket moOrigTicket;
    protected ArrayList<SDbTicket> maNewTickets;
    protected ArrayList<SDbTicketDivision> maTicketsDivision;
    
    public SDbTicketDivisionProcess() {
        super(SModConsts.SX_TIC_DIV_PROC);
        initRegistry();
    }
    
    public void setPkTicketId(int n) { mnPkTicketId = n; }
    
    public void setOrigTicket(SDbTicket o) { moOrigTicket = o; } 
    
    public int getPkTicketId() { return mnPkTicketId; }
    
    public SDbTicket getOrigTicket() { return moOrigTicket; }
    public ArrayList<SDbTicket> getNewTickets() { return maNewTickets; }
    public ArrayList<SDbTicketDivision> getTicketsDivision() { return maTicketsDivision; }
    
    public void readByTicketNew(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        int ticDiv = 0;
        
        msSql = "SELECT id_tic_div FROM s_tic_div WHERE id_tic_new = " + pk[0];
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            ticDiv = resultSet.getInt(1);
        }
        
        read(session, new int[] { ticDiv });
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTicketId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTicketId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkTicketId = 0;
        
        moOrigTicket = null;
        maNewTickets = new ArrayList<>();
        maTicketsDivision = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tic_div = " + mnPkTicketId  + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tic_div = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
    
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        moOrigTicket = new SDbTicket();
        moOrigTicket.read(session, pk);
        
        mnPkTicketId = moOrigTicket.getPkTicketId();
        
        statement = session.getDatabase().getConnection().createStatement();
        
        msSql = "SELECT id_tic_div, id_tic_new FROM s_tic_div " + getSqlWhere();
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbTicketDivision ticketDiv = new SDbTicketDivision();
            ticketDiv.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            maTicketsDivision.add(ticketDiv);
        }
        
        for (SDbTicketDivision ticDiv : maTicketsDivision) {
            SDbTicket tic = new SDbTicket();
            tic.read(session, new int[] { ticDiv.getPkTicketNewId() });
            maNewTickets.add(tic);
        }
        
        mbRegistryNew = false;
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        maTicketsDivision.clear();
        for (SDbTicket newTic : maNewTickets) {
            newTic.save(session);
            SDbTicketDivision ticDiv = new SDbTicketDivision();
            ticDiv.setPkTicketDividedId(moOrigTicket.getPkTicketId());
            ticDiv.setPkTicketNewId(newTic.getPkTicketId());
            maTicketsDivision.add(ticDiv);
        }
        
        moOrigTicket.setDeleted(true);
        moOrigTicket.save(session);
        
        for(SDbTicketDivision ticDiv : maTicketsDivision) {
            ticDiv.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbTicketDivisionProcess clone() throws CloneNotSupportedException {
        SDbTicketDivisionProcess registry = new SDbTicketDivisionProcess();
        
        registry.setPkTicketId(this.getPkTicketId());
        registry.setOrigTicket(this.getOrigTicket());
        
        for (SDbTicket newTic : this.getNewTickets()) {
            registry.getNewTickets().add(newTic);
        }
        
        for (SDbTicketDivision ticDiv : this.getTicketsDivision()) {
            registry.getTicketsDivision().add(ticDiv);
        }
        
        return registry;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        for(SDbTicketDivision ticDiv : maTicketsDivision) {
            ticDiv.delete(session);
        }
        
        for (SDbTicket newTic : maNewTickets) {
            newTic.delete(session);
        }
        
        moOrigTicket.setDeleted(false);
        moOrigTicket.save(session);
    }
}