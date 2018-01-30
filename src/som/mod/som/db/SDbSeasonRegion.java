/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbSeasonRegion extends SDbRegistryUser {

    protected int mnPkSeasonId;
    protected int mnPkRegionId;
    protected int mnPkItemId;
    protected double mdMaximumDensity;
    protected double mdMaximumIodineValue;
    protected double mdMaximumRefractionIndex;
    protected double mdMaximumImpuritiesPercentage;
    protected double mdMaximumMoisturePercentage;
    protected double mdPricePerTon;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    protected boolean mbAuxUpdatePriceTonTickets;
    protected Date mtAuxUpdateTicketDateStart;

    public SDbSeasonRegion() {
        super(SModConsts.SU_SEAS_REG);
        initRegistry();
    }

    public void setPkSeasonId(int n) { mnPkSeasonId = n; }
    public void setPkRegionId(int n) { mnPkRegionId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setMaximumDensity(double d) { mdMaximumDensity = d; }
    public void setMaximumIodineValue(double d) { mdMaximumIodineValue = d; }
    public void setMaximumRefractionIndex(double d) { mdMaximumRefractionIndex = d; }
    public void setMaximumImpuritiesPercentage(double d) { mdMaximumImpuritiesPercentage = d; }
    public void setMaximumMoisturePercentage(double d) { mdMaximumMoisturePercentage = d; }
    public void setPricePerTon(double d) { mdPricePerTon = d; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxUpdatePriceTonTickets(boolean b) { mbAuxUpdatePriceTonTickets = b; }
    public void setAuxUpdateTicketDateStart(Date t) { mtAuxUpdateTicketDateStart = t; }

    public int getPkSeasonId() { return mnPkSeasonId; }
    public int getPkRegionId() { return mnPkRegionId; }
    public int getPkItemId() { return mnPkItemId; }
    public double getMaximumDensity() { return mdMaximumDensity; }
    public double getMaximumIodineValue() { return mdMaximumIodineValue; }
    public double getMaximumRefractionIndex() { return mdMaximumRefractionIndex; }
    public double getMaximumImpuritiesPercentage() { return mdMaximumImpuritiesPercentage; }
    public double getMaximumMoisturePercentage() { return mdMaximumMoisturePercentage; }
    public double getPricePerTon() { return mdPricePerTon; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSeasonId = pk[0];
        mnPkRegionId = pk[1];
        mnPkItemId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSeasonId, mnPkRegionId, mnPkItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSeasonId = 0;
        mnPkRegionId = 0;
        mnPkItemId = 0;
        mdMaximumDensity = 0;
        mdMaximumIodineValue = 0;
        mdMaximumRefractionIndex = 0;
        mdMaximumImpuritiesPercentage = 0;
        mdMaximumMoisturePercentage = 0;
        mdPricePerTon = 0;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mbAuxUpdatePriceTonTickets = false;
        mtAuxUpdateTicketDateStart = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_seas = " + mnPkSeasonId + " AND id_reg = " + mnPkRegionId + " AND id_item = " + mnPkItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_seas = " + pk[0] + " AND id_reg = " + pk[1] + " AND id_item = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

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
            mnPkSeasonId = resultSet.getInt("id_seas");
            mnPkRegionId = resultSet.getInt("id_reg");
            mnPkItemId = resultSet.getInt("id_item");
            mdMaximumDensity = resultSet.getDouble("max_den");
            mdMaximumIodineValue = resultSet.getDouble("max_iod_val");
            mdMaximumRefractionIndex = resultSet.getDouble("max_ref_ind");
            mdMaximumImpuritiesPercentage = resultSet.getDouble("max_imp_per");
            mdMaximumMoisturePercentage = resultSet.getDouble("max_moi_per");
            mdPricePerTon = resultSet.getDouble("prc_ton");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
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
        Statement statement = null;
        ResultSet resultSet = null;
        SDbTicket ticket = null;
        
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkSeasonId + ", " +
                    mnPkRegionId + ", " +
                    mnPkItemId + ", " +
                    mdMaximumDensity + ", " +
                    mdMaximumIodineValue + ", " +
                    mdMaximumRefractionIndex + ", " +
                    mdMaximumImpuritiesPercentage + ", " +
                    mdMaximumMoisturePercentage + ", " +
                    mdPricePerTon + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
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
                    //"id_seas = " + mnPkSeasonId + ", " +
                    //"id_reg = " + mnPkRegionId + ", " +
                    //"id_item = " + mnPkItemId + ", " +
                    "max_den = " + mdMaximumDensity + ", " +
                    "max_iod_val = " + mdMaximumIodineValue + ", " +
                    "max_ref_ind = " + mdMaximumRefractionIndex + ", " +
                    "max_imp_per = " + mdMaximumImpuritiesPercentage + ", " +
                    "max_moi_per = " + mdMaximumMoisturePercentage + ", " +
                    "prc_ton = " + mdPricePerTon + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // Update value of tickets within this season and region:
        
        /*
        statement = session.getStatement().getConnection().createStatement();
        
        msSql = "SELECT id_tic " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " " +
                "WHERE b_del = 0 AND fk_seas_n = " + mnPkSeasonId + " AND fk_reg_n = " + mnPkRegionId + " " +
                "ORDER BY id_tic ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            ticket = (SDbTicket) session.readRegistry(SModConsts.S_TIC, new int[] { resultSet.getInt(1) });
            ticket.setAuxRequiredCalculation(true);
            ticket.save(session);
        }
                */
        // Finish registry updating:
        
        if (mbAuxUpdatePriceTonTickets) {
            SSomUtils.updateTicketsPriceTon(session, mtAuxUpdateTicketDateStart, new int[] { mnPkSeasonId, mnPkRegionId, mnPkItemId }, mdPricePerTon);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbSeasonRegion clone() throws CloneNotSupportedException {
        SDbSeasonRegion registry = new SDbSeasonRegion();

        registry.setPkSeasonId(this.getPkSeasonId());
        registry.setPkRegionId(this.getPkRegionId());
        registry.setPkItemId(this.getPkItemId());
        registry.setMaximumDensity(this.getMaximumDensity());
        registry.setMaximumIodineValue(this.getMaximumIodineValue());
        registry.setMaximumRefractionIndex(this.getMaximumRefractionIndex());
        registry.setMaximumImpuritiesPercentage(this.getMaximumImpuritiesPercentage());
        registry.setMaximumMoisturePercentage(this.getMaximumMoisturePercentage());
        registry.setPricePerTon(this.getPricePerTon());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);

        if (can) {
            initQueryMembers();

            if (mbRegistryNew) {
                can = !SSomUtils.existsSeasonRegion(session, new int[] { mnPkSeasonId, mnPkRegionId, mnPkItemId });

                if (!can) {
                    msQueryResult = "¡El registro ya existe!";
                }
            }
        }

        return can;
    }
}
