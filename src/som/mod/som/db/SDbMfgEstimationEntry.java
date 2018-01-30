/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbMfgEstimationEntry extends SDbRegistryUser {

    protected int mnPkMfgEstimationId;
    protected int mnPkEntryId;
    protected double mdMfgFinishedGood;
    protected double mdMfgByproduct;
    protected double mdMfgCull;
    protected double mdConsumptionRawMaterial;
    protected int mnFkItemMfgFinishedGoodId;
    protected int mnFkItemMfgByproductId_n;
    protected int mnFkItemMfgCullId_n;
    protected int mnFkItemConsumptionRawMaterialId;

    public SDbMfgEstimationEntry() {
        super(SModConsts.S_MFG_EST_ETY);
        initRegistry();
    }

    public void setPkMfgEstimationId(int n) { mnPkMfgEstimationId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setMfgFinishedGood(double d) { mdMfgFinishedGood = d; }
    public void setMfgByproduct(double d) { mdMfgByproduct = d; }
    public void setMfgCull(double d) { mdMfgCull = d; }
    public void setConsumptionRawMaterial(double d) { mdConsumptionRawMaterial = d; }
    public void setFkItemMfgFinishedGoodId(int n) { mnFkItemMfgFinishedGoodId = n; }
    public void setFkItemMfgByproductId_n(int n) { mnFkItemMfgByproductId_n = n; }
    public void setFkItemMfgCullId_n(int n) { mnFkItemMfgCullId_n = n; }
    public void setFkItemConsumptionRawMaterialId(int n) { mnFkItemConsumptionRawMaterialId = n; }

    public int getPkMfgEstimationId() { return mnPkMfgEstimationId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getMfgFinishedGood() { return mdMfgFinishedGood; }
    public double getMfgByproduct() { return mdMfgByproduct; }
    public double getMfgCull() { return mdMfgCull; }
    public double getConsumptionRawMaterial() { return mdConsumptionRawMaterial; }
    public int getFkItemMfgFinishedGoodId() { return mnFkItemMfgFinishedGoodId; }
    public int getFkItemMfgByproductId_n() { return mnFkItemMfgByproductId_n; }
    public int getFkItemMfgCullId_n() { return mnFkItemMfgCullId_n; }
    public int getFkItemConsumptionRawMaterialId() { return mnFkItemConsumptionRawMaterialId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMfgEstimationId = pk[0];
        mnPkEntryId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMfgEstimationId, mnPkEntryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMfgEstimationId = 0;
        mnPkEntryId = 0;
        mdMfgFinishedGood = 0;
        mdMfgByproduct = 0;
        mdMfgCull = 0;
        mdConsumptionRawMaterial = 0;
        mnFkItemMfgFinishedGoodId = 0;
        mnFkItemMfgByproductId_n = 0;
        mnFkItemMfgCullId_n = 0;
        mnFkItemConsumptionRawMaterialId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mfg_est = " + mnPkMfgEstimationId + " AND id_ety = " + mnPkEntryId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mfg_est = " + pk[0] + " AND id_ety = " + pk[1];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEntryId = 0;

        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + " WHERE id_mfg_est = " + mnPkMfgEstimationId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEntryId = resultSet.getInt(1);
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
            mnPkMfgEstimationId = resultSet.getInt("id_mfg_est");
            mnPkEntryId = resultSet.getInt("id_ety");
            mdMfgFinishedGood = resultSet.getDouble("mfg_fg");
            mdMfgByproduct = resultSet.getDouble("mfg_bp");
            mdMfgCull = resultSet.getDouble("mfg_cu");
            mdConsumptionRawMaterial = resultSet.getDouble("con_rm");
            mnFkItemMfgFinishedGoodId = resultSet.getInt("fk_item_mfg_fg");
            mnFkItemMfgByproductId_n = resultSet.getInt("fk_item_mfg_bp_n");
            mnFkItemMfgCullId_n = resultSet.getInt("fk_item_mfg_cu_n");
            mnFkItemConsumptionRawMaterialId = resultSet.getInt("fk_item_con_rm");

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
            mbUpdatable = true;
            mbDisableable = false;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +                    
                mnPkMfgEstimationId + ", " + 
                mnPkEntryId + ", " + 
                mdMfgFinishedGood + ", " + 
                mdMfgByproduct + ", " + 
                mdMfgCull + ", " + 
                mdConsumptionRawMaterial + ", " + 
                mnFkItemMfgFinishedGoodId + ", " + 
                (mnFkItemMfgByproductId_n > 0 ? mnFkItemMfgByproductId_n : "NULL") + ", " + 
                (mnFkItemMfgCullId_n > 0 ? mnFkItemMfgCullId_n : "NULL") + ", " + 
                mnFkItemConsumptionRawMaterialId + " " + 
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                //"id_mfg_est = " + mnPkMfgEstimationId + ", " +                 
                //"id_ety = " + mnPkEntryId + ", " +
                "mfg_fg = " + mdMfgFinishedGood + ", " +
                "mfg_bp = " + mdMfgByproduct + ", " +
                "mfg_cu = " + mdMfgCull + ", " +
                "con_rm = " + mdConsumptionRawMaterial + ", " +
                "fk_item_mfg_fg = " + mnFkItemMfgFinishedGoodId + ", " +
                "fk_item_mfg_bp_n = " + (mnFkItemMfgByproductId_n > 0 ? mnFkItemMfgByproductId_n : "NULL") + ", " +
                "fk_item_mfg_cu_n = " + (mnFkItemMfgCullId_n > 0 ? mnFkItemMfgCullId_n : "NULL") + ", " +
                "fk_item_con_rm = " + mnFkItemConsumptionRawMaterialId + " " +
                getSqlWhere();
        }

        //session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMfgEstimationEntry clone() throws CloneNotSupportedException {
        SDbMfgEstimationEntry registry = new SDbMfgEstimationEntry();

        registry.setPkMfgEstimationId(this.getPkMfgEstimationId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setMfgFinishedGood(this.getMfgFinishedGood());
        registry.setMfgByproduct(this.getMfgByproduct());
        registry.setMfgCull(this.getMfgCull());
        registry.setConsumptionRawMaterial(this.getConsumptionRawMaterial());
        registry.setFkItemMfgFinishedGoodId(this.getFkItemMfgFinishedGoodId());
        registry.setFkItemMfgByproductId_n(this.getFkItemMfgByproductId_n());
        registry.setFkItemMfgCullId_n(this.getFkItemMfgCullId_n());
        registry.setFkItemConsumptionRawMaterialId(this.getFkItemConsumptionRawMaterialId());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
