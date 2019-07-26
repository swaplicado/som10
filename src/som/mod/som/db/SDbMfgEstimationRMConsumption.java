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
 * @author Edwin Carmona
 */
public class SDbMfgEstimationRMConsumption extends SDbRegistryUser {

    protected int mnPkMfgEstimationId;
    protected int mnPkRmConsumptionId;
    protected double mdOilPercentage;
    protected double mdMfgFinishedGood;
    protected double mdMfgByproduct;
    protected double mdMfgCull;
    protected double mdConsumptionRawMaterial;
    protected int mnFkItemConsumptionRawMaterialId;

    public SDbMfgEstimationRMConsumption() {
        super(SModConsts.S_MFG_EST_RM_CON);
        initRegistry();
    }

    public void setPkMfgEstimationId(int n) { mnPkMfgEstimationId = n; }
    public void setPkRmConsumptionId(int n) { mnPkRmConsumptionId = n; }
    public void setOilPercentage(double d) { mdOilPercentage = d; }
    public void setMfgFinishedGood(double d) { mdMfgFinishedGood = d; }
    public void setMfgByproduct(double d) { mdMfgByproduct = d; }
    public void setMfgCull(double d) { mdMfgCull = d; }
    public void setConsumptionRawMaterial(double d) { mdConsumptionRawMaterial = d; }
    public void setFkItemConsumptionRawMaterialId(int n) { mnFkItemConsumptionRawMaterialId = n; }

    public int getPkMfgEstimationId() { return mnPkMfgEstimationId; }
    public int getPkRmConsumptionId() { return mnPkRmConsumptionId; }
    public double getOilPercentage() { return mdOilPercentage; }
    public double getMfgFinishedGood() { return mdMfgFinishedGood; }
    public double getMfgByproduct() { return mdMfgByproduct; }
    public double getMfgCull() { return mdMfgCull; }
    public double getConsumptionRawMaterial() { return mdConsumptionRawMaterial; }
    public int getFkItemConsumptionRawMaterialId() { return mnFkItemConsumptionRawMaterialId; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMfgEstimationId = pk[0];
        mnPkRmConsumptionId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMfgEstimationId, mnPkRmConsumptionId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMfgEstimationId = 0;
        mnPkRmConsumptionId = 0;
        mdOilPercentage = 0;
        mdMfgFinishedGood = 0;
        mdMfgByproduct = 0;
        mdMfgCull = 0;
        mnFkItemConsumptionRawMaterialId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mfg_est = " + mnPkMfgEstimationId + " AND id_rm_con = " + mnPkRmConsumptionId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mfg_est = " + pk[0] + " AND id_rm_con = " + pk[1];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRmConsumptionId = 0;

        msSql = "SELECT COALESCE(MAX(id_rm_con), 0) + 1 FROM " + getSqlTable() + " WHERE id_mfg_est = " + mnPkMfgEstimationId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRmConsumptionId = resultSet.getInt(1);
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
            mnPkRmConsumptionId = resultSet.getInt("id_rm_con");
            mdOilPercentage = resultSet.getDouble("oil_per");
            mdMfgFinishedGood = resultSet.getDouble("mfg_fg");
            mdMfgByproduct = resultSet.getDouble("mfg_bp");
            mdMfgCull = resultSet.getDouble("mfg_cu");
            mdConsumptionRawMaterial = resultSet.getDouble("con_rm");
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
                mnPkRmConsumptionId + ", " + 
                mdOilPercentage + ", " + 
                mdMfgFinishedGood + ", " + 
                mdMfgByproduct + ", " + 
                mdMfgCull + ", " + 
                mdConsumptionRawMaterial + ", " + 
                mnFkItemConsumptionRawMaterialId + " " + 
            ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                //"id_mfg_est = " + mnPkMfgEstimationId + ", " +                 
                //"id_rm_con = " + mnPkRmConsumptionId + ", " +
                "oil_per = " + mdOilPercentage + ", " +
                "mfg_fg = " + mdMfgFinishedGood + ", " +
                "mfg_bp = " + mdMfgByproduct + ", " +
                "mfg_cu = " + mdMfgCull + ", " +
                "con_rm = " + mdConsumptionRawMaterial + ", " +
                "fk_item_con_rm = " + mnFkItemConsumptionRawMaterialId + " " +
                getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMfgEstimationRMConsumption clone() throws CloneNotSupportedException {
        SDbMfgEstimationRMConsumption registry = new SDbMfgEstimationRMConsumption();

        registry.setPkMfgEstimationId(this.getPkMfgEstimationId());
        registry.setPkRmConsumptionId(this.getPkRmConsumptionId());
        registry.setOilPercentage(this.getOilPercentage());
        registry.setMfgFinishedGood(this.getMfgFinishedGood());
        registry.setMfgByproduct(this.getMfgByproduct());
        registry.setMfgCull(this.getMfgCull());
        registry.setConsumptionRawMaterial(this.getConsumptionRawMaterial());
        registry.setFkItemConsumptionRawMaterialId(this.getFkItemConsumptionRawMaterialId());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
