/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDbMgmtTicketsSupplierInputType extends SDbRegistry {

    protected int mnPkInputCategoryId;
    protected int mnPkInputClassId;
    protected int mnPkInputTypeId;
    protected int mnPkProducerId;
    protected boolean mbMfgOutsourcing;

    protected Date mtParamDateStart;
    protected Date mtParamDateEnd;
    
    protected String msXtaInputType;
    protected String msXtaProducer;
    protected String msXtaProducerFiscalId;

    protected Vector<SDbTicket> mvChildTickets;

    public SDbMgmtTicketsSupplierInputType() {
        super(SModConsts.SX_TIC_MAN_SUP_INP_TP);
        mvChildTickets = new Vector<>();
        initRegistry();
    }

    public void setPkInputCategoryId(int n) { mnPkInputCategoryId = n; }
    public void setPkInputClassId(int n) { mnPkInputClassId = n; }
    public void setPkInputTypeId(int n) { mnPkInputTypeId = n; }
    public void setPkProducerId(int n) { mnPkProducerId = n; }
    public void setMfgOutsourcing(boolean b) { mbMfgOutsourcing = b; }

    public int getPkInputCategoryId() { return mnPkInputCategoryId; }
    public int getPkInputClassId() { return mnPkInputClassId; }
    public int getPkInputTypeId() { return mnPkInputTypeId; }
    public int getPkProducerId() { return mnPkProducerId; }
    public boolean isMfgOutsourcing() { return mbMfgOutsourcing; }

    public void setParamDateStart(Date t) { mtParamDateStart = t; }
    public void setParamDateEnd(Date t) { mtParamDateEnd = t; }
    
    public Date getParamDateStart() { return mtParamDateStart; }
    public Date getParamDateEnd() { return mtParamDateEnd; }
    
    public String getXtaInputType() { return msXtaInputType; }
    public String getXtaProducer() { return msXtaProducer; }
    public String getXtaProducerFiscalId() { return msXtaProducerFiscalId; }

    public Vector<SDbTicket> getChildTickets() { return mvChildTickets; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkInputCategoryId = pk[0];
        mnPkInputClassId = pk[1];
        mnPkInputTypeId = pk[2];
        mnPkProducerId = pk[3];
        mbMfgOutsourcing = pk[4] == 1;
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkInputCategoryId, mnPkInputClassId, mnPkInputTypeId, mnPkProducerId, mbMfgOutsourcing ? 1 : 0 };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkInputCategoryId = 0;
        mnPkInputClassId = 0;
        mnPkInputTypeId = 0;
        mnPkProducerId = 0;
        mbMfgOutsourcing = false;

        //mtParamDateStart = null;
        //mtParamDateEnd = null;
        
        msXtaInputType = "";
        msXtaProducer = "";
        msXtaProducerFiscalId = "";
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        setPrimaryKey(pk);
        
        statement = session.getStatement().getConnection().createStatement();

        msSql = "SELECT t.id_tic, t.num " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON t.fk_item = i.id_item " +
                "WHERE t.b_del = 0 AND i.fk_inp_ct = " + mnPkInputCategoryId + " AND i.fk_inp_cl = " + mnPkInputClassId + " AND i.fk_inp_tp = " + mnPkInputTypeId + " AND " +
                "t.fk_prod = " + mnPkProducerId + " AND t.b_mfg_out = " + mbMfgOutsourcing + " " +
                (mtParamDateStart == null ? "" : "AND t.dt >= '" + SLibUtils.DbmsDateFormatDate.format(mtParamDateStart) + "' ") +
                (mtParamDateEnd == null ? "" : "AND t.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtParamDateEnd) + "' ") +
                "ORDER BY t.num, t.id_tic ";

        resultSet = statement.executeQuery(msSql);

        while (resultSet.next()) {
            mvChildTickets.add((SDbTicket) session.readRegistry(SModConsts.S_TIC, new int[] { resultSet.getInt("t.id_tic") }));
        }
        
        msXtaInputType = (String) session.readField(SModConsts.SU_INP_TP, new int[] { mnPkInputCategoryId, mnPkInputClassId, mnPkInputTypeId }, SDbRegistry.FIELD_NAME);
        msXtaProducer = (String) session.readField(SModConsts.SU_PROD, new int[] { mnPkProducerId }, SDbRegistry.FIELD_NAME);
        msXtaProducerFiscalId = (String) session.readField(SModConsts.SU_PROD, new int[] { mnPkProducerId }, SDbProducer.FIELD_FISCAL_ID);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbMgmtTicketsSupplierInputType clone() throws CloneNotSupportedException {
        SDbMgmtTicketsSupplierInputType registry = new SDbMgmtTicketsSupplierInputType();
        
        registry.setPkInputCategoryId(this.getPkInputCategoryId());
        registry.setPkInputClassId(this.getPkInputClassId());
        registry.setPkInputTypeId(this.getPkInputTypeId());
        registry.setPkProducerId(this.getPkProducerId());
        registry.setMfgOutsourcing(this.isMfgOutsourcing());

        registry.setParamDateStart(mtParamDateStart);
        registry.setParamDateEnd(mtParamDateEnd);

        registry.msXtaInputType = this.getXtaInputType();
        registry.msXtaProducer = this.getXtaProducer();
        registry.msXtaProducerFiscalId = this.getXtaProducerFiscalId();

        for (SDbTicket child : mvChildTickets) {
            registry.getChildTickets().add(child.clone());
        }
        
        return registry;
    }
}
