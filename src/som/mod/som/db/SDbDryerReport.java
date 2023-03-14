/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

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
 * @author Isabel Servín
 */
public class SDbDryerReport extends SDbRegistryUser {
    
    protected int mnPkDryerReportId;
    protected Date mtDate;
    protected double mdOperativeHours;
    protected double mdPelletGrossWeight;
    protected double mdPelletBrokenWeight;
    protected double mdPelletNetoWeight_r;
    protected double mdPelletToExtraction;
    protected double mdPelletOil;
    protected double mdPelletToWaste;
    protected double mdPelletStock;
    protected double mdSeedPeelProcessed;
    protected double mdSeedPeelToProcess;
    protected double mdSeedPeelOil;
    protected double mdSeedPeelToPlant;
    protected double mdSeedPeelToTerrain;
    protected double mdBagasseToPlant;
    protected double mdBagasseToTerrain;
    protected int mnPitMeasureEmpty;
    protected double mdPitProcessedFruit;
    protected double mdPitContentEmpty;
    protected double mdPitContentGrinding;
    protected double mdPitContentEffective;
    protected double mdPitBagasseGrindingFactor;
    protected double mdPitBagasseExternalFactor;
    protected double mdGoalMoisitureProcessed;
    protected double mdGoalPelletProcessedPerHour;
    protected double mdGoalAverageEfficiency;
    protected double mdGoalTotalProduced;
    protected double mdGoalPelletOil;
    protected double mdGoalAverageBagasseOil;
    protected Double mdLabAvocadoAcidity_n;
    protected Double mdLabAverageBagasseOil_n;
    protected Double mdLabMoisiture_n;
    protected Double mdLabPelletOil_n;
    protected Double mdLabPelletAcidity_n;
    protected double mdSmokestackEfficiency;
    protected int mnSmokestackCarbonMonoxide;
    protected boolean mbProduction;
    protected boolean mbPitContentEmpty;
    protected boolean mbPitContentGrinding;
    protected boolean mbPitContentEffective;
    
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbDryerReport() {
        super(SModConsts.S_DRYER_REP);
        initRegistry();
    }
    
    public void setPkDryerReportId(int n) { mnPkDryerReportId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setOperativeHours(double d) { mdOperativeHours = d; }
    public void setPelletGrossWeight(double d) { mdPelletGrossWeight = d; }
    public void setPelletBrokenWeight(double d) { mdPelletBrokenWeight = d; }
    public void setPelletNetoWeight_r(double d) { mdPelletNetoWeight_r = d; }
    public void setPelletToExtraction(double d) { mdPelletToExtraction = d; }
    public void setPelletOil(double d) { mdPelletOil = d; }
    public void setPelletToWaste(double d) { mdPelletToWaste = d; }
    public void setPelletStock(double d) { mdPelletStock = d; }
    public void setSeedPeelProcessed(double d) { mdSeedPeelProcessed = d; }
    public void setSeedPeelToProcess(double d) { mdSeedPeelToProcess = d; }
    public void setSeedPeelOil(double d) { mdSeedPeelOil = d; }
    public void setSeedPeelToPlant(double d) { mdSeedPeelToPlant = d; }
    public void setSeedPeelToTerrain(double d) { mdSeedPeelToTerrain = d; }
    public void setBagasseToPlant(double d) { mdBagasseToPlant = d; }
    public void setBagasseToTerrain(double d) { mdBagasseToTerrain = d; }
    public void setPitMeasureEmpty(int n) { mnPitMeasureEmpty = n; }
    public void setPitProcessedFruit(double d) { mdPitProcessedFruit = d; }
    public void setPitContentEmpty(double d) { mdPitContentEmpty = d; }
    public void setPitContentGrinding(double d) { mdPitContentGrinding = d; }
    public void setPitContentEffective(double d) { mdPitContentEffective = d; }
    public void setPitBagasseGrindingFactor(double d) { mdPitBagasseGrindingFactor = d; }
    public void setPitBagasseExternalFactor(double d) { mdPitBagasseExternalFactor = d; }
    public void setGoalMoisitureProcessed(double d) { mdGoalMoisitureProcessed = d; }
    public void setGoalPelletProcessedPerHour(double d) { mdGoalPelletProcessedPerHour = d; }
    public void setGoalAverageEfficiency(double d) { mdGoalAverageEfficiency = d; }
    public void setGoalTotalProduced(double d) { mdGoalTotalProduced = d; }
    public void setGoalPelletOil(double d) { mdGoalPelletOil = d; }
    public void setGoalAverageBagasseOil(double d) { mdGoalAverageBagasseOil = d; }
    public void setLabAvocadoAcidity_n(Double d) { mdLabAvocadoAcidity_n = d; }
    public void setLabAverageBagasseOil_n(Double d) { mdLabAverageBagasseOil_n = d; }
    public void setLabMoisiture_n(Double d) { mdLabMoisiture_n = d; }
    public void setLabPelletOil_n(Double d) { mdLabPelletOil_n = d; }
    public void setLabPelletAcidity_n(Double d) { mdLabPelletAcidity_n = d; }
    public void setSmokestackEfficiency(double d) { mdSmokestackEfficiency = d; }
    public void setSmokestackCarbonMonoxide(int n) { mnSmokestackCarbonMonoxide = n; }
    public void setProduction(boolean b) { mbProduction = b; }
    public void setPitContentEmpty(boolean b) { mbPitContentEmpty = b; }
    public void setPitContentGrinding(boolean b) { mbPitContentGrinding = b; }
    public void setPitContentEffective(boolean b) { mbPitContentEffective = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkDryerReportId() { return mnPkDryerReportId; }
    public Date getDate() { return mtDate; }
    public double getOperativeHours() { return mdOperativeHours; }
    public double getPelletGrossWeight() { return mdPelletGrossWeight; }
    public double getPelletBrokenWeight() { return mdPelletBrokenWeight; }
    public double getPelletNetoWeight_r() { return mdPelletNetoWeight_r; }
    public double getPelletToExtraction() { return mdPelletToExtraction; }
    public double getPelletOil() { return mdPelletOil; }
    public double getPelletToWaste() { return mdPelletToWaste; }
    public double getPelletStock() { return mdPelletStock; }
    public double getSeedPeelProcessed() { return mdSeedPeelProcessed; }
    public double getSeedPeelToProcess() { return mdSeedPeelToProcess; }
    public double getSeedPeelOil() { return mdSeedPeelOil; }
    public double getSeedPeelToPlant() { return mdSeedPeelToPlant; }
    public double getSeedPeelToTerrain() { return mdSeedPeelToTerrain; }
    public double getBagasseToPlant() { return mdBagasseToPlant; }
    public double getBagasseToTerrain() { return mdBagasseToTerrain; }
    public int getPitMeasureEmpty() { return mnPitMeasureEmpty; }
    public double getPitProcessedFruit() { return mdPitProcessedFruit; }
    public double getPitContentEmpty() { return mdPitContentEmpty; }
    public double getPitContentGrinding() { return mdPitContentGrinding; }
    public double getPitContentEffective() { return mdPitContentEffective; }
    public double getPitBagasseGrindingFactor() { return mdPitBagasseGrindingFactor; }
    public double getPitBagasseExternalFactor() { return mdPitBagasseExternalFactor; }
    public double getGoalMoisitureProcessed() { return mdGoalMoisitureProcessed; }
    public double getGoalPelletProcessedPerHour() { return mdGoalPelletProcessedPerHour; }
    public double getGoalAverageEfficiency() { return mdGoalAverageEfficiency; }
    public double getGoalTotalProduced() { return mdGoalTotalProduced; }
    public double getGoalPelletOil() { return mdGoalPelletOil; }
    public double getGoalAverageBagasseOil() { return mdGoalAverageBagasseOil; }
    public Double getLabAvocadoAcidity_n() { return mdLabAvocadoAcidity_n; }
    public Double getLabAverageBagasseOil_n() { return mdLabAverageBagasseOil_n; }
    public Double getLabMoisiture_n() { return mdLabMoisiture_n; }
    public Double getLabPelletOil_n() { return mdLabPelletOil_n; }
    public Double getLabPelletAcidity_n() { return mdLabPelletAcidity_n; }
    public double getSmokestackEfficiency() { return mdSmokestackEfficiency; }
    public int getSmokestackCarbonMonoxide() { return mnSmokestackCarbonMonoxide; }
    public boolean isProduction() { return mbProduction; }
    public boolean isPitContentEmpty() { return mbPitContentEmpty; }
    public boolean isPitContentGrinding() { return mbPitContentGrinding; }
    public boolean isPitContentEffective() { return mbPitContentEffective; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }


    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDryerReportId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDryerReportId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkDryerReportId = 0;
        mtDate = null;
        mdOperativeHours = 0;
        mdPelletGrossWeight = 0;
        mdPelletBrokenWeight = 0;
        mdPelletNetoWeight_r = 0;
        mdPelletToExtraction = 0;
        mdPelletOil = 0;
        mdPelletToWaste = 0;
        mdPelletStock = 0;
        mdSeedPeelProcessed = 0;
        mdSeedPeelToProcess = 0;
        mdSeedPeelOil = 0;
        mdSeedPeelToPlant = 0;
        mdSeedPeelToTerrain = 0;
        mdBagasseToPlant = 0;
        mdBagasseToTerrain = 0;
        mnPitMeasureEmpty = 0;
        mdPitProcessedFruit = 0;
        mdPitContentEmpty = 0;
        mdPitContentGrinding = 0;
        mdPitContentEffective = 0;
        mdPitBagasseGrindingFactor = 0;
        mdPitBagasseExternalFactor = 0;
        mdGoalMoisitureProcessed = 0;
        mdGoalPelletProcessedPerHour = 0;
        mdGoalAverageEfficiency = 0;
        mdGoalTotalProduced = 0;
        mdGoalPelletOil = 0;
        mdGoalAverageBagasseOil = 0;
        mdLabAvocadoAcidity_n = null;
        mdLabAverageBagasseOil_n = null;
        mdLabMoisiture_n = null;
        mdLabPelletOil_n = null;
        mdLabPelletAcidity_n = null;
        mdSmokestackEfficiency = 0;
        mnSmokestackCarbonMonoxide = 0;
        mbProduction = false;
        mbPitContentEmpty = false;
        mbPitContentGrinding = false;
        mbPitContentEffective = false;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dryer_rep = " + mnPkDryerReportId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dryer_rep = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkDryerReportId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_dryer_rep), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDryerReportId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkDryerReportId = resultSet.getInt("id_dryer_rep");
            mtDate = resultSet.getDate("dt");
            mdOperativeHours = resultSet.getDouble("operative_hrs");
            mdPelletGrossWeight = resultSet.getDouble("pellet_gross_wei");
            mdPelletBrokenWeight = resultSet.getDouble("pellet_broken_wei");
            mdPelletNetoWeight_r = resultSet.getDouble("pellet_net_wei_r");
            mdPelletToExtraction = resultSet.getDouble("pellet_to_ext");
            mdPelletOil = resultSet.getDouble("pellet_oil");
            mdPelletToWaste = resultSet.getDouble("pellet_to_waste");
            mdPelletStock = resultSet.getDouble("pellet_stk");
            mdSeedPeelProcessed = resultSet.getDouble("seed_peel_proc");
            mdSeedPeelToProcess = resultSet.getDouble("seed_peel_to_proc");
            mdSeedPeelOil = resultSet.getDouble("seed_peel_oil");
            mdSeedPeelToPlant = resultSet.getDouble("seed_peel_to_plant");
            mdSeedPeelToTerrain = resultSet.getDouble("seed_peel_to_terrain");
            mdBagasseToPlant = resultSet.getDouble("bagasse_to_plant");
            mdBagasseToTerrain = resultSet.getDouble("bagasse_to_terrain");
            mnPitMeasureEmpty = resultSet.getInt("pit_measure_emp");
            mdPitProcessedFruit = resultSet.getDouble("pit_proc_fruit");
            mdPitContentEmpty = resultSet.getDouble("pit_cont_emp");
            mdPitContentGrinding = resultSet.getDouble("pit_cont_grin");
            mdPitContentEffective = resultSet.getDouble("pit_cont_eff");
            mdPitBagasseGrindingFactor = resultSet.getDouble("pit_bag_grin_fact");
            mdPitBagasseExternalFactor = resultSet.getDouble("pit_bag_ext_fact");
            mdGoalMoisitureProcessed = resultSet.getDouble("goal_moi_proc");
            mdGoalPelletProcessedPerHour = resultSet.getDouble("goal_pellet_proc_per_hr");
            mdGoalAverageEfficiency = resultSet.getDouble("goal_avg_effcy");
            mdGoalTotalProduced = resultSet.getDouble("goal_tot_prod");
            mdGoalPelletOil = resultSet.getDouble("goal_pellet_oil");
            mdGoalAverageBagasseOil = resultSet.getDouble("goal_avg_bag_oil");
            mdLabAvocadoAcidity_n = resultSet.getString("lab_avo_aci_n") == null ? null : resultSet.getDouble("lab_avo_aci_n");
            mdLabAverageBagasseOil_n = resultSet.getString("lab_avg_bag_oil_n") == null ? null : resultSet.getDouble("lab_avg_bag_oil_n");
            mdLabMoisiture_n = resultSet.getString("lab_moi_n") == null ? null : resultSet.getDouble("lab_moi_n");
            mdLabPelletOil_n = resultSet.getString("lab_pellet_oil_n") == null ? null : resultSet.getDouble("lab_pellet_oil_n");
            mdLabPelletAcidity_n = resultSet.getString("lab_pellet_aci_n") == null ? null : resultSet.getDouble("lab_pellet_aci_n");
            mdSmokestackEfficiency = resultSet.getDouble("ss_effcy");
            mnSmokestackCarbonMonoxide = resultSet.getInt("ss_c_monoxide");
            mbProduction = resultSet.getBoolean("b_production");
            mbPitContentEmpty = resultSet.getBoolean("b_pit_cont_emp");
            mbPitContentGrinding = resultSet.getBoolean("b_pit_cont_grin");
            mbPitContentEffective = resultSet.getBoolean("b_pit_cont_eff");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

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
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                    mnPkDryerReportId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    mdOperativeHours + ", " + 
                    mdPelletGrossWeight + ", " + 
                    mdPelletBrokenWeight + ", " + 
                    mdPelletNetoWeight_r + ", " + 
                    mdPelletToExtraction + ", " + 
                    mdPelletOil + ", " + 
                    mdPelletToWaste + ", " + 
                    mdPelletStock + ", " + 
                    mdSeedPeelProcessed + ", " + 
                    mdSeedPeelToProcess + ", " + 
                    mdSeedPeelOil + ", " + 
                    mdSeedPeelToPlant + ", " + 
                    mdSeedPeelToTerrain + ", " + 
                    mdBagasseToPlant + ", " + 
                    mdBagasseToTerrain + ", " + 
                    mnPitMeasureEmpty + ", " + 
                    mdPitProcessedFruit + ", " + 
                    mdPitContentEmpty + ", " + 
                    mdPitContentGrinding + ", " + 
                    mdPitContentEffective + ", " + 
                    mdPitBagasseGrindingFactor + ", " + 
                    mdPitBagasseExternalFactor + ", " + 
                    mdGoalMoisitureProcessed + ", " + 
                    mdGoalPelletProcessedPerHour + ", " + 
                    mdGoalAverageEfficiency + ", " + 
                    mdGoalTotalProduced + ", " + 
                    mdGoalPelletOil + ", " + 
                    mdGoalAverageBagasseOil + ", " + 
                    mdLabAvocadoAcidity_n + ", " + 
                    mdLabAverageBagasseOil_n + ", " + 
                    mdLabMoisiture_n + ", " + 
                    mdLabPelletOil_n + ", " + 
                    mdLabPelletAcidity_n + ", " + 
                    mdSmokestackEfficiency + ", " + 
                    mnSmokestackCarbonMonoxide + ", " + 
                    (mbProduction ? 1 : 0) + ", " + 
                    (mbPitContentEmpty ? 1 : 0) + ", " + 
                    (mbPitContentGrinding ? 1 : 0) + ", " + 
                    (mbPitContentEffective ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_dryer_rep = " + mnPkDryerReportId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "operative_hrs = " + mdOperativeHours + ", " +
                    "pellet_gross_wei = " + mdPelletGrossWeight + ", " +
                    "pellet_broken_wei = " + mdPelletBrokenWeight + ", " +
                    "pellet_net_wei_r = " + mdPelletNetoWeight_r + ", " +
                    "pellet_to_ext = " + mdPelletToExtraction + ", " +
                    "pellet_oil = " + mdPelletOil + ", " +
                    "pellet_to_waste = " + mdPelletToWaste + ", " +
                    "pellet_stk = " + mdPelletStock + ", " +
                    "seed_peel_proc = " + mdSeedPeelProcessed + ", " +
                    "seed_peel_to_proc = " + mdSeedPeelToProcess + ", " +
                    "seed_peel_oil = " + mdSeedPeelOil + ", " +
                    "seed_peel_to_plant = " + mdSeedPeelToPlant + ", " +
                    "seed_peel_to_terrain = " + mdSeedPeelToTerrain + ", " +
                    "bagasse_to_plant = " + mdBagasseToPlant + ", " +
                    "bagasse_to_terrain = " + mdBagasseToTerrain + ", " +
                    "pit_measure_emp = " + mnPitMeasureEmpty + ", " +
                    "pit_proc_fruit = " + mdPitProcessedFruit + ", " +
                    "pit_cont_emp = " + mdPitContentEmpty + ", " +
                    "pit_cont_grin = " + mdPitContentGrinding + ", " +
                    "pit_cont_eff = " + mdPitContentEffective + ", " +
                    "pit_bag_grin_fact = " + mdPitBagasseGrindingFactor + ", " +
                    "pit_bag_ext_fact = " + mdPitBagasseExternalFactor + ", " +
                    "goal_moi_proc = " + mdGoalMoisitureProcessed + ", " +
                    "goal_pellet_proc_per_hr = " + mdGoalPelletProcessedPerHour + ", " +
                    "goal_avg_effcy = " + mdGoalAverageEfficiency + ", " +
                    "goal_tot_prod = " + mdGoalTotalProduced + ", " +
                    "goal_pellet_oil = " + mdGoalPelletOil + ", " +
                    "goal_avg_bag_oil = " + mdGoalAverageBagasseOil + ", " +
                    "lab_avo_aci_n = " + mdLabAvocadoAcidity_n + ", " +
                    "lab_avg_bag_oil_n = " + mdLabAverageBagasseOil_n + ", " +
                    "lab_moi_n = " + mdLabMoisiture_n + ", " +
                    "lab_pellet_oil_n = " + mdLabPelletOil_n + ", " +
                    "lab_pellet_aci_n = " + mdLabPelletAcidity_n + ", " +
                    "ss_effcy = " + mdSmokestackEfficiency + ", " +
                    "ss_c_monoxide = " + mnSmokestackCarbonMonoxide + ", " +
                    "b_production = " + (mbProduction ? 1 : 0) + ", " +
                    "b_pit_cont_emp = " + (mbPitContentEmpty ? 1 : 0) + ", " +
                    "b_pit_cont_grin = " + (mbPitContentGrinding ? 1 : 0) + ", " +
                    "b_pit_cont_eff = " + (mbPitContentEffective ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbDryerReport clone() throws CloneNotSupportedException {
        SDbDryerReport registry = new SDbDryerReport();
        
        registry.setPkDryerReportId(this.getPkDryerReportId());
        registry.setDate(this.getDate());
        registry.setOperativeHours(this.getOperativeHours());
        registry.setPelletGrossWeight(this.getPelletGrossWeight());
        registry.setPelletBrokenWeight(this.getPelletBrokenWeight());
        registry.setPelletNetoWeight_r(this.getPelletNetoWeight_r());
        registry.setPelletToExtraction(this.getPelletToExtraction());
        registry.setPelletOil(this.getPelletOil());
        registry.setPelletToWaste(this.getPelletToWaste());
        registry.setPelletStock(this.getPelletStock());
        registry.setSeedPeelProcessed(this.getSeedPeelProcessed());
        registry.setSeedPeelToProcess(this.getSeedPeelToProcess());
        registry.setSeedPeelOil(this.getSeedPeelOil());
        registry.setSeedPeelToPlant(this.getSeedPeelToPlant());
        registry.setSeedPeelToTerrain(this.getSeedPeelToTerrain());
        registry.setBagasseToPlant(this.getBagasseToPlant());
        registry.setBagasseToTerrain(this.getBagasseToTerrain());
        registry.setPitMeasureEmpty(this.getPitMeasureEmpty());
        registry.setPitProcessedFruit(this.getPitProcessedFruit());
        registry.setPitContentEmpty(this.getPitContentEmpty());
        registry.setPitContentGrinding(this.getPitContentGrinding());
        registry.setPitContentEffective(this.getPitContentEffective());
        registry.setGoalMoisitureProcessed(this.getGoalMoisitureProcessed());
        registry.setGoalPelletProcessedPerHour(this.getGoalPelletProcessedPerHour());
        registry.setGoalAverageEfficiency(this.getGoalAverageEfficiency());
        registry.setPitBagasseGrindingFactor(this.getPitBagasseGrindingFactor());
        registry.setPitBagasseExternalFactor(this.getPitBagasseExternalFactor());
        registry.setGoalTotalProduced(this.getGoalTotalProduced());
        registry.setGoalPelletOil(this.getGoalPelletOil());
        registry.setGoalAverageBagasseOil(this.getGoalAverageBagasseOil());
        registry.setLabAvocadoAcidity_n(this.getLabAvocadoAcidity_n());
        registry.setLabAverageBagasseOil_n(this.getLabAverageBagasseOil_n());
        registry.setLabMoisiture_n(this.getLabMoisiture_n());
        registry.setLabPelletOil_n(this.getLabPelletOil_n());
        registry.setLabPelletAcidity_n(this.getLabPelletAcidity_n());
        registry.setSmokestackEfficiency(this.getSmokestackEfficiency());
        registry.setSmokestackCarbonMonoxide(this.getSmokestackCarbonMonoxide());
        registry.setProduction(this.isProduction());
        registry.setPitContentEmpty(this.isPitContentEmpty());
        registry.setPitContentGrinding(this.isPitContentGrinding());
        registry.setPitContentEffective(this.isPitContentEffective());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}
