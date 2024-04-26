/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class SDbSeasonProducer extends SDbRegistryUser {

    protected int mnPkSeasonId;
    protected int mnPkRegionId;
    protected int mnPkItemId;
    protected int mnPkProducerId;
    protected double mdPricePerTon;
    protected double mdPriceFreight;
    protected boolean mbDefault;
    protected boolean mbPricePerTon;
    protected boolean mbFreightPayment;
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
    protected boolean mbAuxUpdatePriceFreightTickets;
    protected Date mtAuxUpdateTicketDateStart;

    public SDbSeasonProducer() {
        super(SModConsts.SU_SEAS_PROD);
        initRegistry();
    }

    public void setPkSeasonId(int n) { mnPkSeasonId = n; }
    public void setPkRegionId(int n) { mnPkRegionId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkProducerId(int n) { mnPkProducerId = n; }
    public void setPricePerTon(double d) { mdPricePerTon = d; }
    public void setPriceFreight(double d) { mdPriceFreight = d; }
    public void setDefault(boolean b) { mbDefault = b; }
    public void setPricePerTon(boolean b) { mbPricePerTon = b; }
    public void setFreightPayment(boolean b) { mbFreightPayment = b; }
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
    public void setAuxUpdatePriceFreightTickets(boolean b) { mbAuxUpdatePriceFreightTickets = b; }
    public void setAuxUpdateTicketDateStart(Date t) { mtAuxUpdateTicketDateStart = t; }
    
    public int getPkSeasonId() { return mnPkSeasonId; }
    public int getPkRegionId() { return mnPkRegionId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkProducerId() { return mnPkProducerId; }
    public double getPricePerTon() { return mdPricePerTon; }
    public double getPriceFreight() { return mdPriceFreight; }
    public boolean isDefault() { return mbDefault; }
    public boolean isPricePerTon() { return mbPricePerTon; }
    public boolean isFreightPayment() { return mbFreightPayment; }
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
        mnPkProducerId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSeasonId, mnPkRegionId, mnPkItemId, mnPkProducerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSeasonId = 0;
        mnPkRegionId = 0;
        mnPkItemId = 0;
        mnPkProducerId = 0;
        mdPricePerTon = 0;
        mdPriceFreight = 0;
        mbDefault = false;
        mbPricePerTon = false;
        mbFreightPayment = false;
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
        mbAuxUpdatePriceFreightTickets = false;
        mtAuxUpdateTicketDateStart = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_seas = " + mnPkSeasonId + " AND id_reg = " + mnPkRegionId + " AND id_item = " + mnPkItemId + " AND id_prod = " + mnPkProducerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_seas = " + pk[0] + " AND id_reg = " + pk[1] + " AND id_item = " + pk[2] + " AND id_prod = " + pk[3] + " ";
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
            mnPkItemId = resultSet.getInt("id_item");
            mnPkRegionId = resultSet.getInt("id_reg");
            mnPkProducerId = resultSet.getInt("id_prod");
            mdPricePerTon = resultSet.getDouble("prc_ton");
            mdPriceFreight = resultSet.getDouble("prc_fre");
            mbDefault = resultSet.getBoolean("b_def");
            mbPricePerTon = resultSet.getBoolean("b_prc_ton");
            mbFreightPayment = resultSet.getBoolean("b_fre_pay");
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
                    mnPkProducerId + ", " +
                    mdPricePerTon + ", " +
                    mdPriceFreight + ", " +
                    (mbDefault ? 1 : 0) + ", " + 
                    (mbPricePerTon ? 1 : 0) + ", " +
                    mbFreightPayment + ", " +
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
                    //"id_prod = " + mnPkProducerId + ", " +
                    "prc_ton = " + mdPricePerTon + ", " +
                    "prc_fre = " + mdPriceFreight + ", " +
                    "b_def = " + (mbDefault ? 1 : 0) + ", " +
                    "b_prc_ton = " + (mbPricePerTon ? 1 : 0) + ", " +
                    "b_fre_pay = " + mbFreightPayment + ", " +
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
        
        if (mbAuxUpdatePriceTonTickets) {
            SSomUtils.updateTicketsPriceTon(session, mtAuxUpdateTicketDateStart, new int[] { mnPkSeasonId, mnPkRegionId, mnPkItemId, mnPkProducerId }, mdPricePerTon);
        }
        
        if (mbAuxUpdatePriceFreightTickets) {
            SSomUtils.updateTicketsPriceFreight(session, mtAuxUpdateTicketDateStart, new int[] { mnPkSeasonId, mnPkRegionId, mnPkItemId, mnPkProducerId }, mdPriceFreight);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbSeasonProducer clone() throws CloneNotSupportedException {
        SDbSeasonProducer registry = new SDbSeasonProducer();

        registry.setPkSeasonId(this.getPkSeasonId());
        registry.setPkRegionId(this.getPkRegionId());
        registry.setPkItemId(this.getPkItemId());
        registry.setPkProducerId(this.getPkProducerId());
        registry.setPricePerTon(this.getPricePerTon());
        registry.setPriceFreight(this.getPriceFreight());
        registry.setDefault(this.isDefault());
        registry.setPricePerTon(this.isPricePerTon());
        registry.setFreightPayment(this.isFreightPayment());        
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
                can = !SSomUtils.existsSeasonProducer(session, new int[] { mnPkSeasonId, mnPkRegionId, mnPkItemId, mnPkProducerId });

                if (!can) {
                    msQueryResult = "¡El registro ya existe!";
                }
            }
        }

        return can;
    }
}
