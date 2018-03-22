/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.ext.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas, Alfredo Pérez
 */
public class SDbTicketRevuelta extends SDbRegistryUser  {

    protected int mnPkTicketRevueltaId;
    protected String msPlate;
    protected String msDriver;
    protected String msNote;
    protected Date mtDatetimeArrival;
    protected Date mtDatetimeDeparture;
    protected String nueva;
    protected double mdWeightArrival;
    protected double mdWeightDeparture;
    protected double mdWeightNet_r;
    protected String msProducer;
    protected String msProducerId;
    protected String msItem;
    protected String msItemId;
    protected String msOperator;
    protected String msOperatorId;
    protected String msTurn;
    protected String msScaleId;
    protected boolean mbTared;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected Date mtDateStart;
    protected Date mtDateEnd;

    public SDbTicketRevuelta() {
        super(SModConsts.E_TIC_REV);
    }

    public void setPkTicketRevueltaId(int n) { mnPkTicketRevueltaId = n; }
    public void setPlate(String s) { msPlate = s; }
    public void setDriver(String s) { msDriver = s; }
    public void setNote(String s) { msNote = s; }
    public void setDatetimeArrival(Date t) { mtDatetimeArrival = t; }
    public void setDatetimeDeparture(Date t) { mtDatetimeDeparture = t; }
    public void setDatetimeDeparture2(String t) { nueva = t; }
    public void setWeightArrival(double d) { mdWeightArrival = d; }
    public void setWeightDeparture(double d) { mdWeightDeparture = d; }
    public void setWeightNet_r(double d) { mdWeightNet_r = d; }
    public void setProducer(String s) { msProducer = s; }
    public void setProducerId(String s) { msProducerId = s; }
    public void setItem(String s) { msItem = s; }
    public void setItemId(String s) { msItemId = s; }
    public void setOperator(String s) { msOperator = s; }
    public void setOperatorId(String s) { msOperatorId = s; }
    public void setTurn(String s) { msTurn = s; }
    public void setScaleId(String s) { msScaleId = s; }
    public void setTared(boolean b) { mbTared = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }

    public int getPkTicketRevueltaId() { return mnPkTicketRevueltaId; }
    public String getPlate() { return msPlate; }
    public String getDriver() { return msDriver; }
    public String getNote() { return msNote; }
    public Date getDatetimeArrival() { return mtDatetimeArrival; }
    public Date getDatetimeDeparture() { return mtDatetimeDeparture; }
    public double getWeightArrival() { return mdWeightArrival; }
    public double getWeightDeparture() { return mdWeightDeparture; }
    public double getWeightNet_r() { return mdWeightNet_r; }
    public String getProducer() { return msProducer; }
    public String getProducerId() { return msProducerId; }
    public String getItem() { return msItem; }
    public String getItemId() { return msItemId; }
    public String getOperator() { return msOperator; }
    public String getOperatorId() { return msOperatorId; }
    public String getTurn() { return msTurn; }
    public String getScaleId() { return msScaleId; }
    public boolean isTared() { return mbTared; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTicketRevueltaId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTicketRevueltaId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTicketRevueltaId = 0;
        msPlate = "";
        msDriver = "";
        msNote = "";
        mtDatetimeArrival = null;
        mtDatetimeDeparture = null;
        mdWeightArrival = 0;
        mdWeightDeparture = 0;
        mdWeightNet_r = 0;
        msProducer = "";
        msProducerId = "";
        msItem = "";
        msItemId = "";
        msOperator = "";
        msOperatorId = "";
        msTurn = "";
        msScaleId = "";
        mbTared = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mtDateStart = null;
        mtDateEnd = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tic_rev = " + mnPkTicketRevueltaId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tic_rev = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTicketRevueltaId = 0;

        msSql = "SELECT COALESCE(MAX(id_tic_rev), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTicketRevueltaId = resultSet.getInt(1);
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
            mnPkTicketRevueltaId = resultSet.getInt("id_tic_rev");
            msPlate = resultSet.getString("pla");
            msDriver = resultSet.getString("drv");
            msNote = resultSet.getString("note");
            mtDatetimeArrival = resultSet.getTimestamp("ts_arr");
            mtDatetimeDeparture = resultSet.getTimestamp("ts_dep");
            mdWeightArrival = resultSet.getDouble("wei_arr");
            mdWeightDeparture = resultSet.getDouble("wei_dep");
            mdWeightNet_r = resultSet.getDouble("wei_net_r");
            msProducer = resultSet.getString("prod");
            msProducerId = resultSet.getString("prod_id");
            msItem = resultSet.getString("item");
            msItemId = resultSet.getString("item_id");
            msOperator = resultSet.getString("oper");
            msOperatorId = resultSet.getString("oper_id");
            msTurn = resultSet.getString("turn");
            msScaleId = resultSet.getString("sca_id");
            mbTared = resultSet.getBoolean("b_tar");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if(mtDatetimeDeparture == null){
            Date a = sa.lib.SLibTimeUtils.createDate(0000-00-00); 
            mtDatetimeDeparture = a;
        }
        verifyRegistryNew(session);

        if (mbRegistryNew) {
            //computePrimaryKey(session); It's not required
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkTicketRevueltaId + ", " +
                    "'" + msPlate + "', " +
                    "'" + msDriver + "', " +
                    "'" + (msNote == null ? "" : msNote) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeArrival) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeDeparture) + "', " +
                    mdWeightArrival + ", " +
                    mdWeightDeparture + ", " +
                    mdWeightNet_r + ", " +
                    "'" + msProducer + "', " +
                    "'" + msProducerId + "', " +
                    "'" + msItem + "', " +
                    "'" + msItemId + "', " +
                    "'" + msOperator + "', " +
                    "'" + msOperatorId + "', " +
                    "'" + msTurn + "', " +
                    "'" + msScaleId + "', " +
                    (mbTared ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_tic_rev = " + mnPkTicketRevueltaId + ", " +
                    */
                    "pla = '" + msPlate + "', " +
                    "drv = '" + msDriver + "', " +
                    "note = '" + msNote + "', " +
                    "ts_arr = '" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeArrival) + "', " +
                    "ts_dep = '" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeDeparture) + "', " +
                    "wei_arr = " + mdWeightArrival + ", " +
                    "wei_dep = " + mdWeightDeparture + ", " +
                    "wei_net_r = " + mdWeightNet_r + ", " +
                    "prod = '" + msProducer + "', " +
                    "prod_id = '" + msProducerId + "', " +
                    "item = '" + msItem + "', " +
                    "item_id = '" + msItemId + "', " +
                    "oper = '" + msOperator + "', " +
                    "oper_id = '" + msOperatorId + "', " +
                    "turn = '" + msTurn + "', " +
                    "sca_id = '" + msScaleId + "', " +
                    "b_tar = " + (mbTared ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public void delete(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "DELETE FROM " + getSqlTable() + " "
                + "WHERE ts_arr BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "' ";

        session.getStatement().execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbTicketRevuelta clone() throws CloneNotSupportedException {
        SDbTicketRevuelta registry = new SDbTicketRevuelta();

        registry.setPkTicketRevueltaId(this.getPkTicketRevueltaId());
        registry.setPlate(this.getPlate());
        registry.setDriver(this.getDriver());
        registry.setNote(this.getNote());
        registry.setDatetimeArrival(this.getDatetimeArrival());
        registry.setDatetimeDeparture(this.getDatetimeDeparture());
        registry.setWeightArrival(this.getWeightArrival());
        registry.setWeightDeparture(this.getWeightDeparture());
        registry.setWeightNet_r(this.getWeightNet_r());
        registry.setProducer(this.getProducer());
        registry.setProducerId(this.getProducerId());
        registry.setItem(this.getItem());
        registry.setItemId(this.getItemId());
        registry.setOperator(this.getOperator());
        registry.setOperatorId(this.getOperatorId());
        registry.setTurn(this.getTurn());
        registry.setScaleId(this.getScaleId());
        registry.setTared(this.isTared());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
}
