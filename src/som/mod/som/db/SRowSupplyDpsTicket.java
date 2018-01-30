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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SRowSupplyDpsTicket extends SDbRegistry implements SGridRow {

    protected Date mtDpsSupplyDate_n;
    protected int mnFkExternalDpsYearId_n;
    protected int mnFkExternalDpsDocId_n;
    protected int mnFkExternalDpsEntryId_n;

    protected int mnPkTicketId;
    protected int mnNumber;
    protected Date mtDate;
    protected String msPlate;
    protected String msDriver;
    protected double mdWeightSource;
    protected double mdWeightDestinyArrival;
    protected double mdWeightDestinyDeparture;
    protected double mdWeightDestinyNet_r;
    protected boolean mbDpsSupply;

    protected String msXtaScaleCode;
    protected String msXtaSeason;
    protected String msXtaRegion;
    protected String msXtaItem;
    protected String msXtaItemCode;
    protected String msXtaUnitCode;
    protected String msXtaIogType;
    protected double mdXtaIogQuantity;
    protected int mnXtaPkIogId;
    protected int mnXtaFkItem;
    protected int mnXtaFkProducer;
    protected int mnXtaFkTicketIdSelected;

    protected Vector<SRowSupplyDpsTicket> mvSupplyDpsTickets;

    public SRowSupplyDpsTicket() {
        super(SModConsts.SX_TIC_DPS);
        mvSupplyDpsTickets = new Vector<SRowSupplyDpsTicket>();
        initRegistry();
    }

    public void setDpsSupplyDate_n(Date t) { mtDpsSupplyDate_n = t; }
    public void setDpsSupply(boolean b) { mbDpsSupply = b; }
    public void setFkExternalDpsYearId_n(int n) { mnFkExternalDpsYearId_n = n; }
    public void setFkExternalDpsDocId_n(int n) { mnFkExternalDpsDocId_n = n; }
    public void setFkExternalDpsEntryId_n(int n) { mnFkExternalDpsEntryId_n = n; }

    public void setPkTicketId(int n) { mnPkTicketId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setPlate(String s) { msPlate = s; }
    public void setDriver(String s) { msDriver = s; }
    public void setWeightSource(double d) { mdWeightSource = d; }
    public void setWeightDestinyArrival(double d) { mdWeightDestinyArrival = d; }
    public void setWeightDestinyDeparture(double d) { mdWeightDestinyDeparture = d; }
    public void setWeightDestinyNet_r(double d) { mdWeightDestinyNet_r = d; }

    public Date getDpsSupplyDate_n() { return mtDpsSupplyDate_n; }
    public boolean isDpsSupply() { return mbDpsSupply; }
    public int getFkExternalDpsYearId_n() { return mnFkExternalDpsYearId_n; }
    public int getFkExternalDpsDocId_n() { return mnFkExternalDpsDocId_n; }
    public int getFkExternalDpsEntryId_n() { return mnFkExternalDpsEntryId_n; }

    public int getPkTicketId() { return mnPkTicketId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public String getPlate() { return msPlate; }
    public String getDriver() { return msDriver; }
    public double getWeightSource() { return mdWeightSource; }
    public double getWeightDestinyArrival() { return mdWeightDestinyArrival; }
    public double getWeightDestinyDeparture() { return mdWeightDestinyDeparture; }
    public double getWeightDestinyNet_r() { return mdWeightDestinyNet_r; }

    public void setXtaScaleCode(String s) { msXtaScaleCode = s;  }
    public void setXtaSeason(String s) { msXtaSeason = s;  }
    public void setXtaRegion(String s) { msXtaRegion = s;  }
    public void setXtaItem(String s) { msXtaItem = s;  }
    public void setXtaItemCode(String s) { msXtaItemCode = s;  }
    public void setXtaUnitCode(String s) { msXtaUnitCode = s;  }
    public void setXtaIogType(String s) { msXtaIogType = s; }
    public void setXtaIogQuantity(double d) { mdXtaIogQuantity = d; }
    public void setXtaPkIogId(int n) { mnXtaPkIogId = n; }
    public void setXtaFkItem(int n) { mnXtaFkItem = n; }
    public void setXtaFkTicketIdSelected(int n) { mnXtaFkTicketIdSelected = n; }
    public void setXtaFkProducer(int n) { mnXtaFkProducer = n; }

    public String getXtaScaleCode() { return msXtaScaleCode; }
    public String getXtaSeason() { return msXtaSeason; }
    public String getXtaRegion() { return msXtaRegion; }
    public String getXtaItem() { return msXtaItem; }
    public String getXtaItemCode() { return msXtaItemCode; }
    public String getXtaUnitCode() { return msXtaUnitCode; }
    public String getXtaIogType() { return msXtaIogType; }
    public double getXtaIogQuantity() { return mdXtaIogQuantity; }
    public int getXtaPkIogId() { return mnXtaPkIogId; }
    public int getXtaFkItem() { return mnXtaFkItem; }
    public int getXtaFkTicketIdSelected() { return mnXtaFkTicketIdSelected; }
    public int getXtaFkProducer() { return mnXtaFkProducer; }

    public void setSupplyDpsTickets(Vector<SRowSupplyDpsTicket> v) { mvSupplyDpsTickets = v; }

    public Vector<SRowSupplyDpsTicket> getSupplyDpsTickets() { return mvSupplyDpsTickets; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTicketId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return null;
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mtDpsSupplyDate_n = null;
        mnFkExternalDpsYearId_n = 0;
        mnFkExternalDpsDocId_n = 0;
        mnFkExternalDpsEntryId_n = 0;

        mnPkTicketId = 0;
        mnNumber = 0;
        mtDate = null;
        msPlate = "";
        msDriver = "";
        mdWeightSource = 0;
        mdWeightDestinyArrival = 0;
        mdWeightDestinyDeparture = 0;
        mdWeightDestinyNet_r = 0;
        mbDpsSupply = false;

        msXtaScaleCode = "";
        msXtaSeason = "";
        msXtaRegion = "";
        msXtaItem = "";
        msXtaItemCode = "";
        msXtaUnitCode = "";
        msXtaIogType = "";
        mdXtaIogQuantity = 0;
        mnXtaPkIogId = 0;

        mvSupplyDpsTickets.clear();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;
        SRowSupplyDpsTicket supplyDpsTicket = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT v.id_tic, v.dt, v.num, v.drv, v.pla, v.wei_src, v.wei_des_arr, v.wei_des_dep, v.wei_des_net_r, "
                + "sc.code, se.name, re.name, it.name, it.code, pr.name, v.b_dps, g.id_iog, CONCAT(tp.code, '-', g.num) AS f_tp_iog, g.qty, u.code "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sc ON v.fk_sca = sc.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_TIC_ST) + " AS vs ON v.fk_tic_st = vs.id_tic_st "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g ON v.id_tic = g.fk_tic_n "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CT) + " AS ct ON g.fk_iog_ct = ct.id_iog_ct "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CL) + " AS cl ON g.fk_iog_ct = cl.id_iog_ct AND g.fk_iog_cl = cl.id_iog_cl "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_TP) + " AS tp ON g.fk_iog_ct = tp.id_iog_ct AND g.fk_iog_cl = tp.id_iog_cl AND g.fk_iog_tp = tp.id_iog_tp "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS se ON v.fk_seas_n = se.id_seas "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS re ON v.fk_reg_n = re.id_reg "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS it ON v.fk_item = it.id_item "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON v.fk_unit = u.id_unit "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pr ON v.fk_prod = pr.id_prod "
                + "WHERE v.b_del = 0 AND g.fk_tic_n > 0 AND g.fk_ext_dps_year_n IS NULL AND g.fk_ext_dps_doc_n IS NULL AND "
                + "g.fk_ext_dps_ety_n IS NULL AND v.b_ass = 0 AND v.b_tar = 1 AND v.fk_item = " + mnXtaFkItem + " AND v.fk_prod = " + mnXtaFkProducer + " "
                + "ORDER BY sc.code, v.num, v.dt, v.id_tic ";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {

            supplyDpsTicket = new SRowSupplyDpsTicket();

            supplyDpsTicket.setPkTicketId(resultSet.getInt("v.id_tic"));
            supplyDpsTicket.setNumber(resultSet.getInt("v.num"));
            supplyDpsTicket.setDate(resultSet.getDate("v.dt"));
            supplyDpsTicket.setPlate(resultSet.getString("v.pla"));
            supplyDpsTicket.setDriver(resultSet.getString("v.drv"));
            supplyDpsTicket.setWeightSource(resultSet.getDouble("v.wei_src"));
            supplyDpsTicket.setWeightDestinyArrival(resultSet.getDouble("v.wei_des_arr"));
            supplyDpsTicket.setWeightDestinyDeparture(resultSet.getDouble("v.wei_des_dep"));
            supplyDpsTicket.setWeightDestinyNet_r(resultSet.getDouble("v.wei_des_net_r"));
            supplyDpsTicket.setXtaScaleCode(resultSet.getString("sc.code"));
            supplyDpsTicket.setXtaSeason(resultSet.getString("se.name"));
            supplyDpsTicket.setXtaRegion(resultSet.getString("re.name"));
            supplyDpsTicket.setXtaItem(resultSet.getString("it.name"));
            supplyDpsTicket.setXtaItemCode(resultSet.getString("it.code"));
            supplyDpsTicket.setXtaUnitCode(resultSet.getString("u.code"));
            supplyDpsTicket.setXtaIogType(resultSet.getString("f_tp_iog"));
            supplyDpsTicket.setXtaIogQuantity(resultSet.getDouble("g.qty"));
            supplyDpsTicket.setXtaPkIogId(resultSet.getInt("g.id_iog"));

            if (supplyDpsTicket.getPkTicketId() == mnXtaFkTicketIdSelected) {
                supplyDpsTicket.setDpsSupply(true);

                msXtaItem = resultSet.getString("it.name");
                msXtaItemCode = resultSet.getString("it.code");
            }

            mvSupplyDpsTickets.add(supplyDpsTicket);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        SDbIog iog = null;
        SDbTicket ticket = null;
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        for (SRowSupplyDpsTicket supplyDpsTicket : mvSupplyDpsTickets) {
            iog = new SDbIog();
            ticket = new SDbTicket();

            ticket.read(session, new int[] { supplyDpsTicket.getPkTicketId() });
            if (ticket != null) {
                ticket.setDpsSupplyDate_n(mtDpsSupplyDate_n);
                ticket.setFkExternalDpsYearId_n(mnFkExternalDpsYearId_n);
                ticket.setFkExternalDpsDocId_n(mnFkExternalDpsDocId_n);
                ticket.setFkExternalDpsEntryId_n(mnFkExternalDpsEntryId_n);

                ticket.save(session);

                iog.read(session, new int[] { supplyDpsTicket.getXtaPkIogId() });
                if (iog != null) {
                    iog.saveField(session.getStatement(), iog.getPrimaryKey(), SDbIog.FIELD_EXTERNAL_DPS, new int[] { mnFkExternalDpsYearId_n, mnFkExternalDpsDocId_n, mnFkExternalDpsEntryId_n } );
                }
            }
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SRowSupplyDpsTicket clone() throws CloneNotSupportedException {
        SRowSupplyDpsTicket registry = new SRowSupplyDpsTicket();

        registry.setDpsSupplyDate_n(this.getDpsSupplyDate_n());
        registry.setFkExternalDpsYearId_n(this.getFkExternalDpsYearId_n());
        registry.setFkExternalDpsDocId_n(this.getFkExternalDpsDocId_n());
        registry.setFkExternalDpsEntryId_n(this.getFkExternalDpsEntryId_n());

        registry.setSupplyDpsTickets(this.getSupplyDpsTickets());

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
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msXtaScaleCode;
                break;
            case 1:
                value = mnNumber;
                break;
            case 2:
                value = mtDate;
                break;
            case 3:
                value = msXtaItem;
                break;
            case 4:
                value = msXtaItemCode;
                break;
            case 5:
                value = mbDpsSupply;
                break;
            case 6:
                value = msXtaIogType;
                break;
            case 7:
                value = mdXtaIogQuantity;
                break;
            case 8:
                value = msXtaUnitCode;
                break;
            case 9:
                value = mdWeightSource;
                break;
            case 10:
                value = mdWeightDestinyArrival;
                break;
            case 11:
                value = mdWeightDestinyDeparture;
                break;
            case 12:
                value = mdWeightDestinyNet_r;
                break;
            case 13:
                value = msXtaSeason;
                break;
            case 14:
                value = msXtaRegion;
                break;
            case 15:
                value = msPlate;
                break;
            case 16:
                value = msDriver;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                break;
            case 5:
                mbDpsSupply = (Boolean) value;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                break;
            default:
        }
    }

    @Override
    public void setRowEdited(boolean b) {
        mbRegistryEdited = b;
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
