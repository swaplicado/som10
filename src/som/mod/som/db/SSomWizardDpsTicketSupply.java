/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomWizardDpsTicketSupply implements SGridRow {

    protected int mnPkTicketId;
    protected String msScaleCode;
    protected int mnTicket;
    protected Date mtTicketDate;
    protected String msSupplierName;
    protected String msSupplierCode;
    protected String msItem;
    protected String msItemCode;
    protected double mdWeightSource;
    protected double mdWeightDestinyArrive;
    protected double mdWeightDestinyDeparture;
    protected double mdQuantity;
    protected double mdQuantityPending;
    protected double mdQuantitySupply;
    protected Date mtArrive;
    protected Date mtDeparture;
    protected String msSeason;
    protected String msRegion;
    protected String msPlate;
    protected String msPlateCharge;
    protected String msDriver;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected String msUserInsert;
    protected Date mtUserInsertTS;
    protected String msUserUpdate;
    protected Date mtUsertUpdateTS;

    protected Vector<SSomWizardDpsTicketSupply> mvWizardDpsSupplyTicket;

    public void setPkTicketId(int n) { mnPkTicketId = n; }
    public void setScaleCode(String s) { msScaleCode = s; }
    public void setTicket(int n) { mnTicket = n; }
    public void setTicketDate(Date t) { mtTicketDate = t; }
    public void setSupplierName(String s) { msSupplierName = s; }
    public void setSupplierCode(String s) { msSupplierCode = s; }
    public void setItem(String s) { msItem = s; }
    public void setItemCode(String s) { msItemCode = s; }
    public void setWeightSource(double d) { mdWeightSource = d; }
    public void setWeightDestinyArrive(double d) { mdWeightDestinyArrive = d; }
    public void setWeightDestinyDeparture(double d) { mdWeightDestinyDeparture = d; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setQuantityPending(double d) { mdQuantityPending = d; }
    public void setQuantitySupply(double d) { mdQuantitySupply = d; }
    public void setArrive(Date t) { mtArrive = t; }
    public void setDeparture(Date t) { mtDeparture = t; }
    public void setSeason(String s) { msSeason = s; }
    public void setRegion(String s) { msRegion = s; }
    public void setPlate(String s) { msPlate = s; }
    public void setPlateCharge(String s) { msPlateCharge = s; }
    public void setDriver(String s) { msDriver = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setUserInsert(String s) { msUserInsert = s; }
    public void setUserInsertTS(Date t) { mtUserInsertTS = t; }
    public void setUserUpdate(String s) { msUserUpdate = s; }
    public void setUsertUpdateTS(Date t) { mtUsertUpdateTS = t; }

    public int getPkTicketId() { return mnPkTicketId; }
    public String getScaleCode() { return msScaleCode; }
    public int getTicket() { return mnTicket; }
    public Date getTicketDate() { return mtTicketDate; }
    public String getSupplierName() { return msSupplierName; }
    public String getSupplierCode() { return msSupplierCode; }
    public String getItem() { return msItem; }
    public String getItemCode() { return msItemCode; }
    public double getWeightSource() { return mdWeightSource; }
    public double getWeightDestinyArrive() { return mdWeightDestinyArrive; }
    public double getWeightDestinyDeparture() { return mdWeightDestinyDeparture; }
    public double getQuantity() { return mdQuantity; }
    public double getQuantityPending() { return mdQuantityPending; }
    public double getQuantitySupply() { return mdQuantitySupply; }
    public Date getArrive() { return mtArrive; }
    public Date getDeparture() { return mtDeparture; }
    public String  getSeason() { return msSeason; }
    public String  getRegion() { return msRegion; }
    public String  getPlate() { return msPlate; }
    public String  getPlateCharge() { return msPlateCharge; }
    public String  getDriver() { return msDriver; }
    public boolean getDeleted() { return mbDeleted; }
    public boolean getSystem() { return mbSystem; }
    public String  getUserInsert() { return msUserInsert; }
    public Date getUserInsertTS() { return mtUserInsertTS; }
    public String  getUserUpdate() { return msUserUpdate; }
    public Date getUsertUpdateTS() { return mtUsertUpdateTS; }

    public Vector<SSomWizardDpsTicketSupply> getWizardDpsSupplyTicket() { return mvWizardDpsSupplyTicket; }

    public SSomWizardDpsTicketSupply() {
        mvWizardDpsSupplyTicket = new Vector<SSomWizardDpsTicketSupply>();

        mnPkTicketId = 0;
        msScaleCode = "";
        mnTicket = 0;
        mtTicketDate = null;
        msSupplierName = "";
        msSupplierCode = "";
        msItem = "";
        msItemCode = "";
        mdWeightSource = 0;
        mdWeightDestinyArrive = 0;
        mdWeightDestinyDeparture = 0;
        mdQuantity = 0;
        mdQuantityPending = 0;
        mdQuantitySupply = 0;
        mtArrive = null;
        mtDeparture = null;
        msSeason = "";
        msRegion = "";
        msPlate = "";
        msPlateCharge = "";
        msDriver = "";
        mbDeleted = false;
        mbSystem = false;
        msUserInsert = "";
        mtUserInsertTS = null;
        msUserUpdate = "";
        mtUsertUpdateTS = null;

        mvWizardDpsSupplyTicket.clear();
    }

    public void read(SGuiSession session, String sqlDiogPeriod, int subtype, int nFkUnitid) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;

        SSomWizardDpsTicketSupply wizard = null;

        sql = getQuery(sqlDiogPeriod, subtype, nFkUnitid);
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            wizard = new SSomWizardDpsTicketSupply();

            wizard.setPkTicketId(resultSet.getInt("f_id_1"));
            wizard.setScaleCode(resultSet.getString("sc.code"));
            wizard.setTicket(resultSet.getInt("f_code"));
            wizard.setTicketDate(resultSet.getDate("f_date"));
            wizard.setSupplierName(resultSet.getString("pr.name"));
            wizard.setSupplierCode(resultSet.getString("pr.code"));
            wizard.setItem(resultSet.getString("it.name"));
            wizard.setItemCode(resultSet.getString("it.code"));
            wizard.setWeightSource(resultSet.getDouble("v.wei_src"));
            wizard.setWeightDestinyArrive(resultSet.getDouble("v.wei_des_arr"));
            wizard.setWeightDestinyDeparture(resultSet.getDouble("v.wei_des_dep"));
            wizard.setQuantity(resultSet.getDouble("v.qty"));
            wizard.setQuantityPending(resultSet.getDouble("v.qty") - resultSet.getDouble("f_qty"));
            wizard.setQuantitySupply(resultSet.getDouble("f_qty"));
            wizard.setArrive(resultSet.getDate("v.ts_arr"));
            wizard.setDeparture(resultSet.getDate("v.ts_dep"));
            wizard.setSeason(resultSet.getString("se.name"));
            wizard.setRegion(resultSet.getString("re.name"));
            wizard.setPlate(resultSet.getString("v.pla"));
            wizard.setPlateCharge(resultSet.getString("v.pla_cag"));
            wizard.setDriver(resultSet.getString(SDbConsts.FIELD_NAME));
            wizard.setDeleted(resultSet.getBoolean(SDbConsts.FIELD_IS_DEL));
            wizard.setSystem(resultSet.getBoolean(SDbConsts.FIELD_IS_SYS));
            wizard.setUserInsert(resultSet.getString(SDbConsts.FIELD_USER_INS_NAME));
            wizard.setUserInsertTS(resultSet.getDate(SDbConsts.FIELD_USER_INS_TS));
            wizard.setUserUpdate(resultSet.getString(SDbConsts.FIELD_USER_UPD_NAME));
            wizard.setUsertUpdateTS(resultSet.getDate(SDbConsts.FIELD_USER_UPD_TS));

            mvWizardDpsSupplyTicket.add(wizard);
        }
    }

    public String getQuery(String sqlDiogPeriod, int subtype, int nFkUnitid) {
        String sql = "";

        sql = "SELECT "
                + "v.id_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.drv AS " + SDbConsts.FIELD_NAME + ", "
                + "v.pla, "
                + "v.pla_cag, "
                + "v.qty, "
                + "sc.code, "
                + "se.name, "
                + "re.name, "
                + "it.name, "
                + "it.code, "
                + "pr.name, "
                + "pr.code, "
                + "v.ts_arr, "
                + "v.ts_dep, "
                + "v.pac_wei_arr, "
                + "v.pac_wei_dep, "
                + "v.wei_src, "
                + "v.wei_des_arr, "
                + "v.wei_des_dep, "
                + "v.wei_des_net_r, "
                + "v.sys_pen_per, "
                + "v.sys_wei_pay, "
                + "v.sys_prc_ton, "
                + "v.sys_pay_r, "
                + "v.sys_fre, "
                + "v.sys_tot_r, "
                + "v.usr_pen_per, "
                + "v.usr_wei_pay, "
                + "v.usr_prc_ton, "
                + "v.usr_pay_r, "
                + "v.usr_fre, "
                + "v.usr_tot_r, "
                + "vs.name, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.b_ass, "
                + "v.fk_item, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "v.ts_usr_ass AS f_ts_usr_ass, "
                + "it.fk_inp_tp, "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "ua.name AS f_usr_ass, "
                + "(SELECT COALESCE(SUM(ge.qty), 0) FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS ge WHERE "
                + "ge.b_del = 0 AND ge.fk_tic_n = v.id_tic AND ge.fk_item = v.fk_item AND ge.fk_unit = v.fk_unit " + sqlDiogPeriod + ") AS f_qty, "
                + "CONCAT(tp.code, '-', g.num) AS f_tp_iog "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sc ON v.fk_sca = sc.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_TIC_ST) + " AS vs ON v.fk_tic_st = vs.id_tic_st "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS pr ON v.fk_prod = pr.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS it ON v.fk_item = it.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ua ON v.fk_usr_ass = ua.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS g ON v.id_tic = g.fk_tic_n AND g.b_del = 0 "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CT) + " AS ct ON g.fk_iog_ct = ct.id_iog_ct "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_CL) + " AS cl ON g.fk_iog_ct = cl.id_iog_ct AND g.fk_iog_cl = cl.id_iog_cl "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_IOG_TP) + " AS tp ON g.fk_iog_ct = tp.id_iog_ct AND g.fk_iog_cl = tp.id_iog_cl AND g.fk_iog_tp = tp.id_iog_tp "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS se ON v.fk_seas_n = se.id_seas "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS re ON v.fk_reg_n = re.id_reg "
                + "WHERE v.fk_unit = " + nFkUnitid + " AND v.b_tar = 1 AND v.fk_tic_st = " + SModSysConsts.SS_TIC_ST_ADM  + " " + (sql.isEmpty() ? "" : sql) + " "
                + "GROUP BY v.id_tic "
                + "HAVING " + (SModConsts.SX_TIC_MAN_SUP == subtype ? "(v.b_ass = 0 AND (f_qty = 0 OR v.qty <> f_qty))" : "(v.qty = f_qty) OR v.b_ass = 1") + " "
                + "ORDER BY sc.code, v.num, v.dt, v.id_tic ";

        return sql;
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(final boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msScaleCode;
                break;
            case 1:
                value = mnTicket;
                break;
            case 2:
                value = mtTicketDate;
                break;
            case 3:
                value = msSupplierName;
                break;
            case 4:
                value = msSupplierCode;
                break;
            case 5:
                value = msItem;
                break;
            case 6:
                value = msItemCode;
                break;
            case 7:
                value = mdWeightSource;
                break;
            case 8:
                value = mdWeightDestinyArrive;
                break;
            case 9:
                value = mdWeightDestinyDeparture;
                break;
            case 10:
                value = mdQuantity;
                break;
            case 11:
                value = mdQuantityPending;
                break;
            case 12:
                value = mdQuantitySupply;
                break;
            case 13:
                value = mtArrive;
                break;
            case 14:
                value = mtDeparture;
                break;
            case 15:
                value = msSeason;
                break;
            case 16:
                value = msRegion;
                break;
            case 17:
                value = msPlate;
                break;
            case 18:
                value = msPlateCharge;
                break;
            case 19:
                value = msDriver;
                break;
            case 20:
                value = mbDeleted;
                break;
            case 21:
                value = mbSystem;
                break;
            case 22:
                value = msUserInsert;
                break;
            case 23:
                value = mtUserInsertTS;
                break;
            case 24:
                value = msUserUpdate;
                break;
            case 25:
                value = mtUsertUpdateTS;
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
            case 5:
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
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
                break;
            default:
        }
    }
}
