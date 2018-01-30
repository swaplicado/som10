/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas
 */
public class SDbMgmtTicketsSupplierItem extends SDbRegistry {

    protected int mnPkSeasonId;
    protected int mnPkItemId;
    protected int mnPkProducerId;
    protected double mdPriceFreight;
    protected double mdTotalWeightSource;
    protected double mdTotalWeightDestinyNet_r;
    protected double mdTotalSystemWeightPayment;
    protected double mdUserTotalWeightPayment;
    protected double mdTotalCredit;
    protected double mdUserWeightPayment;
    protected double mdUserPricePerTon;
    protected double mdUserPayment_r;
    protected double mdUserTotal_r;
    protected boolean mbMfgOutsourcing;
    protected boolean mbLaboratory;
    protected int mnFkUnitId;

    protected int mnXtaYear;
    protected int mnXtaFkExternalProducerId;
    protected String msXtaSeason;
    protected String msXtaItem;
    protected String msXtaProducer;
    protected String msXtaProducerFiscalId;
    protected double mdXtaTotalImpurities;
    protected double mdXtaTotalMoisture;
    protected double mdXtaTotalPenalty;

    protected Vector<SDbTicket> mvChildTickets;

    public SDbMgmtTicketsSupplierItem() {
        super(SModConsts.SX_TIC_MAN_SUP);
        mvChildTickets = new Vector<>();
        initRegistry();
    }

    public void setPkSeasonId(int n) { mnPkSeasonId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkProducerId(int n) { mnPkProducerId = n; }
    public void setPriceFreight(double d) { mdPriceFreight = d; }
    public void setTotalCredit(double d) { mdTotalCredit = d; }
    public void setTotalWeightSource(double d) { mdTotalWeightSource = d; }
    public void setTotalWeightDestiny(double d) { mdTotalWeightDestinyNet_r = d; }
    public void setTotalWeightPaymentSystem(double d) { mdTotalSystemWeightPayment = d; }
    public void setTotalWeightPaymentUser(double d) { mdUserTotalWeightPayment = d; }
    public void setMfgOutsourcing(boolean b) { mbMfgOutsourcing = b; }
    public void setLaboratory(boolean b) { mbLaboratory = b; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }

    public int getPkSeasonId() { return mnPkSeasonId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkProducerId() { return mnPkProducerId; }
    public double getPriceFreight() { return mdPriceFreight; }
    public double getTotalCredit() { return mdTotalCredit; }
    public double getTotalWeightSource() { return mdTotalWeightSource; }
    public double getTotalWeightDestiny() { return mdTotalWeightDestinyNet_r; }
    public double getTotalWeightPaymentSystem() { return mdTotalSystemWeightPayment; }
    public double getTotalWeightPaymentUser() { return mdUserTotalWeightPayment; }
    public boolean isMfgOutsourcing() { return mbMfgOutsourcing; }
    public boolean isLaboratory() { return mbLaboratory; }
    public int getFkUnitId() { return mnFkUnitId; }

    public int getXtaYear() { return mnXtaYear; }
    public int getXtaFkExternalProducerId() { return mnXtaFkExternalProducerId; }
    public String getXtaSeason() { return msXtaSeason; }
    public String getXtaItem() { return msXtaItem; }
    public String getXtaProducer() { return msXtaProducer; }
    public String getXtaProducerFiscalId() { return msXtaProducerFiscalId; }
    public double getXtaTotalImpurities() { return mdXtaTotalImpurities; }
    public double getXtaTotalMoisture() { return mdXtaTotalMoisture; }
    public double getXtaTotalPenalty() { return mdXtaTotalPenalty; }

    public Vector<SDbTicket> getChildTickets() { return mvChildTickets; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSeasonId = pk[0];
        mnPkItemId = pk[1];
        mnPkProducerId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSeasonId, mnPkItemId, mnPkProducerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSeasonId = 0;
        mnPkItemId = 0;
        mnPkProducerId = 0;
        mdPriceFreight = 0;

        mnXtaYear = 0;
        mnXtaFkExternalProducerId = 0;
        msXtaSeason = "";
        msXtaItem = "";
        msXtaProducer = "";
        mnFkUnitId = 0;
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere() {
        return "WHERE t.fk_seas_n " + (mnPkSeasonId == SLibConsts.UNDEFINED ? "IS NULL " : "= " + mnPkSeasonId + " ") + "AND t.fk_item = " + mnPkItemId + " AND t.fk_prod = " + mnPkProducerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE t.fk_seas_n " + (pk[0] == SLibConsts.UNDEFINED ? "IS NULL " : "= " + pk[0] + " ") + "AND t.fk_item = " + pk[1] + " AND t.fk_prod = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Vector<Integer> tickets = new Vector<>();
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT t.id_tic, IF(se.name IS NULL, '" + SUtilConsts.NON_APPLYING + "', se.name) AS " + SDbConsts.FIELD_NAME + ", t.b_mfg_out, t.b_lab, " +
                "se.dt_sta, it.name, pr.fk_ext_prod_n, pr.name, pr.fis_id, it.fk_unit " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS it ON t.fk_item = it.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pr ON t.fk_prod = pr.id_prod " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS se ON t.fk_seas_n = se.id_seas " +
                getSqlWhere(pk) + " AND t.b_del = 0 " +
                "AND t.b_mfg_out = " + pk[3] + " " +
                "ORDER BY t.num, t.id_tic ";

        resultSet = session.getStatement().executeQuery(msSql);

        while (resultSet.next()) {
            tickets.add(resultSet.getInt("t.id_tic"));

            mbMfgOutsourcing = resultSet.getBoolean("t.b_mfg_out");
            mbLaboratory = resultSet.getBoolean("t.b_lab");
            mnFkUnitId = resultSet.getInt("it.fk_unit");

            mnXtaYear = resultSet.getDate("se.dt_sta") == null ? SLibConsts.UNDEFINED : SLibTimeUtils.digestYear(resultSet.getDate("se.dt_sta"))[0];    // XXX this is a non reliable data!!! (sflores, 2015-06-15)
            mnXtaFkExternalProducerId = resultSet.getInt("pr.fk_ext_prod_n");   // XXX this is a non reliable data!!! (sflores, 2015-06-15)
            msXtaSeason = resultSet.getString(SDbConsts.FIELD_NAME);    // XXX this is a non reliable data!!! (sflores, 2015-06-15)
            msXtaItem = resultSet.getString("it.name");                 // XXX this is a non reliable data!!! (sflores, 2015-06-15)
            msXtaProducer = resultSet.getString("pr.name");             // XXX this is a non reliable data!!! (sflores, 2015-06-15)
            msXtaProducerFiscalId = resultSet.getString("pr.fis_id");   // XXX this is a non reliable data!!! (sflores, 2015-06-15)
        }

        for (int i = 0; i < tickets.size(); i++) {
            SDbTicket ticket = new SDbTicket();
            try {
                ticket.read(session, new int[] { tickets.get(i) });
                mvChildTickets.add(ticket);
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        for (SDbTicket ticket : mvChildTickets) {
            if (ticket.isRegistryEdited()) {
                ticket.save(session);

                mbRegistryNew = false;
                mnQueryResultId = SDbConsts.SAVE_OK;
            }
        }
    }

    @Override
    public SDbMgmtTicketsSupplierItem clone() throws CloneNotSupportedException {
        SDbMgmtTicketsSupplierItem registry = new SDbMgmtTicketsSupplierItem();
        
        registry.setPkSeasonId(this.getPkSeasonId());
        registry.setPkItemId(this.getPkItemId());
        registry.setPkProducerId(this.getPkProducerId());
        registry.setPriceFreight(this.getPriceFreight());
        registry.setTotalCredit(this.getTotalCredit());
        registry.setTotalWeightSource(this.getTotalWeightSource());
        registry.setTotalWeightDestiny(this.getTotalWeightDestiny());
        registry.setTotalWeightPaymentSystem(this.getTotalWeightPaymentSystem());
        registry.setTotalWeightPaymentUser(this.getTotalWeightPaymentUser());
        registry.setMfgOutsourcing(this.isMfgOutsourcing());
        registry.setLaboratory(this.isLaboratory());
        registry.setFkUnitId(this.getFkUnitId());
        
        registry.mnXtaYear = this.getXtaYear();
        registry.mnXtaFkExternalProducerId = this.getXtaFkExternalProducerId();
        registry.msXtaSeason = this.getXtaSeason();
        registry.msXtaItem = this.getXtaItem();
        registry.msXtaProducer = this.getXtaProducer();
        registry.msXtaProducerFiscalId = this.getXtaProducerFiscalId();
        registry.mdXtaTotalImpurities = this.getXtaTotalImpurities();
        registry.mdXtaTotalMoisture = this.getXtaTotalMoisture();
        registry.mdXtaTotalPenalty = this.getXtaTotalPenalty();
        
        for (SDbTicket child : mvChildTickets) {
            registry.getChildTickets().add(child.clone());
        }
        
        return registry;
    }

    public void computeTicketValue(SGuiSession session) {
        mdTotalCredit = 0;
        mdTotalWeightSource = 0;
        mdTotalWeightDestinyNet_r = 0;
        mdTotalSystemWeightPayment = 0;
        mdUserTotalWeightPayment = 0;
        mdUserWeightPayment = 0;
        mdUserPricePerTon = 0;
        mdUserPayment_r = 0;
        mdUserTotal_r = 0;

        for (SDbTicket ticket : mvChildTickets) {

            if (ticket.isAuxRequiredCalculation()) {
                ticket.computeTicketValue(session);
            }

            // Fileds system

            mdTotalWeightSource += ticket.getWeightSource();
            mdTotalWeightDestinyNet_r += ticket.getWeightDestinyNet_r();
            mdTotalSystemWeightPayment += ticket.getSystemWeightPayment();

            // Fileds user

            mdUserWeightPayment = SLibUtils.round((ticket.getWeightSource() - ticket.getWeightDestinyNet_r() * ticket.getUserPenaltyPercentage()), SLibUtils.DecimalFormatPercentage0D.getMaximumFractionDigits());
            mdUserPricePerTon = ticket.getUserPricePerTon();
            mdUserPayment_r = (mdUserWeightPayment * mdUserPricePerTon) / 1000;
            mdUserTotalWeightPayment += mdUserWeightPayment;
            mdUserTotal_r = mdUserPayment_r;
            mdTotalCredit += mdUserTotal_r;

            if (!ticket.getChildLaboratories().isEmpty()) {
                mdXtaTotalImpurities += mdUserWeightPayment * ticket.getChildLaboratories().get(0).getImpuritiesPercentageAverage();
                mdXtaTotalMoisture += mdUserWeightPayment * ticket.getChildLaboratories().get(0).getMoisturePercentageAverage();
                mdXtaTotalPenalty += mdUserWeightPayment * ticket.getUserPenaltyPercentage();
            }
        }

        if (!mvChildTickets.isEmpty()) {
            SLibUtils.round(mdXtaTotalImpurities /= mdUserTotalWeightPayment, SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits());
            SLibUtils.round(mdXtaTotalMoisture /= mdUserTotalWeightPayment, SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits());
            SLibUtils.round(mdXtaTotalPenalty /= mdUserTotalWeightPayment, SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits());
        }
    }
}
