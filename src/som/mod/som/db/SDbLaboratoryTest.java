/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas, Isabel Servín, Sergio Flores
 * 2018-12-11, Sergio Flores: Adición de parámetros de fruta.
 * 2019-01-07, Sergio Flores: Adición de ajuste de rendimiento para parámetros de fruta, y cálculo de valores '% humedad' y '% contenido aceite'.
 * 2019-01-09, Sergio Flores: Estimación de porcentaje aceite en pulpa a partir de porcentaje materia seca en fruta.
 */
public class SDbLaboratoryTest extends SDbRegistry implements SGridRow {

    public static final String RESET_FRUIT_PARAMS = "Reset Fruit Params";

    protected int mnPkLaboratoryId;
    protected int mnPkTestId;
    protected double mdDensity;
    protected double mdIodineValue;
    protected double mdRefractionIndex;
    protected double mdImpuritiesPercentage;
    protected double mdMoisturePercentage;
    protected double mdProteinPercentage;
    protected double mdOilContentPercentage;
    protected double mdOilYieldAdjustmentPercentage;
    protected double mdOleicAcidPercentage;
    protected double mdLinoleicAcidPercentage;
    protected double mdLinolenicAcidPercentage;
    protected double mdErucicAcidPercentage;
    protected double mdAcidityPercentage;
    protected double mdAcidityAveragePercentage;
    protected Date mtGrindingDate_n;
    protected String msFruitClass;
    protected String msFruitRipenessDegree;
    protected double mdFruitWeightTotal;
    protected double mdFruitWeightPeelPit;
    protected double mdFruitPulpDryMatterPercentage;
    protected double mdFruitPulpHumidityPercentage;
    protected double mdFruitPulpOilPercentage;
    protected double mdFruitYieldAdjustmentPercentage;

    public SDbLaboratoryTest() {
        super(SModConsts.S_LAB_TEST);
        initRegistry();
    }

    public void setPkLaboratoryId(int n) { mnPkLaboratoryId = n; }
    public void setPkTestId(int n) { mnPkTestId = n; }
    public void setDensity(double d) { mdDensity = d; }
    public void setIodineValue(double d) { mdIodineValue = d; }
    public void setRefractionIndex(double d) { mdRefractionIndex = d; }
    public void setImpuritiesPercentage(double d) { mdImpuritiesPercentage = d; }
    public void setMoisturePercentage(double d) { mdMoisturePercentage = d; }
    public void setProteinPercentage(double d) { mdProteinPercentage = d; }
    public void setOilContentPercentage(double d) { mdOilContentPercentage = d; }
    public void setOilYieldAdjustmentPercentage(double d) { mdOilYieldAdjustmentPercentage = d; }
    public void setOleicAcidPercentage(double d) { mdOleicAcidPercentage = d; }
    public void setLinoleicAcidPercentage(double d) { mdLinoleicAcidPercentage = d; }
    public void setLinolenicAcidPercentage(double d) { mdLinolenicAcidPercentage = d; }
    public void setErucicAcidPercentage(double d) { mdErucicAcidPercentage = d; }
    public void setAcidityPercentage(double d) { mdAcidityPercentage = d; }
    public void setAcidityAveragePercentage(double d) { mdAcidityAveragePercentage = d; }
    public void setGrindingDate_n(Date t) { mtGrindingDate_n = t; }
    public void setFruitClass(String s) { msFruitClass = s; }
    public void setFruitRipenessDegree(String s) { msFruitRipenessDegree = s; }
    public void setFruitWeightTotal(double d) { mdFruitWeightTotal = d; }
    public void setFruitWeightPeelPit(double d) { mdFruitWeightPeelPit = d; }
    public void setFruitPulpDryMatterPercentage(double d) { mdFruitPulpDryMatterPercentage = d; }
    public void setFruitPulpHumidityPercentage(double d) { mdFruitPulpHumidityPercentage = d; }
    public void setFruitPulpOilPercentage(double d) { mdFruitPulpOilPercentage = d; }
    public void setFruitYieldAdjustmentPercentage(double d) { mdFruitYieldAdjustmentPercentage = d; }

    public int getPkLaboratoryId() { return mnPkLaboratoryId; }
    public int getPkTestId() { return mnPkTestId; }
    public double getDensity() { return mdDensity; }
    public double getIodineValue() { return mdIodineValue; }
    public double getRefractionIndex() { return mdRefractionIndex; }
    public double getImpuritiesPercentage() { return mdImpuritiesPercentage; }
    public double getMoisturePercentage() { return mdMoisturePercentage; }
    public double getProteinPercentage() { return mdProteinPercentage; }
    public double getOilContentPercentage() { return mdOilContentPercentage; }
    public double getOilYieldAdjustmentPercentage() { return mdOilYieldAdjustmentPercentage; }
    public double getOleicAcidPercentage() { return mdOleicAcidPercentage; }
    public double getLinoleicAcidPercentage() { return mdLinoleicAcidPercentage; }
    public double getLinolenicAcidPercentage() { return mdLinolenicAcidPercentage; }
    public double getErucicAcidPercentage() { return mdErucicAcidPercentage; }
    public double getAcidityPercentage() { return mdAcidityPercentage; }
    public double getAcidityAveragePercentage() { return mdAcidityAveragePercentage; }
    public Date getGrindingDate_n() { return mtGrindingDate_n; }
    public String getFruitClass() { return msFruitClass; }
    public String getFruitRipenessDegree() { return msFruitRipenessDegree; }
    public double getFruitWeightTotal() { return mdFruitWeightTotal; }
    public double getFruitWeightPeelPit() { return mdFruitWeightPeelPit; }
    public double getFruitPulpDryMatterPercentage() { return mdFruitPulpDryMatterPercentage; }
    public double getFruitPulpHumidityPercentage() { return mdFruitPulpHumidityPercentage; }
    public double getFruitPulpOilPercentage() { return mdFruitPulpOilPercentage; }
    public double getFruitYieldAdjustmentPercentage() { return mdFruitYieldAdjustmentPercentage; }

    /**
     * Compute fruit related parameters: moisture pct. and oil content pct.
     */
    public void computeFruitParams() {
        if (!msFruitClass.isEmpty()) {
            if (mdFruitWeightTotal == 0 || mdFruitWeightTotal <= mdFruitWeightPeelPit || msFruitClass.equals(RESET_FRUIT_PARAMS)) {
                // no way to compute parameters related to fruit:
                mdMoisturePercentage = 0;
                mdOilContentPercentage = 0;
                
                // clear fruit class:
                msFruitClass = "";
            }
            else {
                // weight computations assumed in gr:
                double weightPulp = mdFruitWeightTotal - mdFruitWeightPeelPit;
                double weightFruitHumidity = weightPulp * mdFruitPulpHumidityPercentage;
                double weightFruitOil = weightPulp * mdFruitPulpOilPercentage;
                mdMoisturePercentage = weightFruitHumidity / mdFruitWeightTotal;
                mdOilContentPercentage = weightFruitOil / mdFruitWeightTotal;
            }
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLaboratoryId = pk[0];
        mnPkTestId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLaboratoryId, mnPkTestId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLaboratoryId = 0;
        mnPkTestId = 0;
        mdDensity = 0;
        mdIodineValue = 0;
        mdRefractionIndex = 0;
        mdImpuritiesPercentage = 0;
        mdMoisturePercentage = 0;
        mdProteinPercentage = 0;
        mdOilContentPercentage = 0;
        mdOilYieldAdjustmentPercentage = 0;
        mdOleicAcidPercentage = 0;
        mdLinoleicAcidPercentage = 0;
        mdLinolenicAcidPercentage = 0;
        mdErucicAcidPercentage = 0;
        mdAcidityPercentage = 0;
        mdAcidityAveragePercentage = 0;
        mtGrindingDate_n = null;
        msFruitClass = "";
        msFruitRipenessDegree = "";
        mdFruitWeightTotal = 0;
        mdFruitWeightPeelPit = 0;
        mdFruitPulpDryMatterPercentage = 0;
        mdFruitPulpHumidityPercentage = 0;
        mdFruitPulpOilPercentage = 0;
        mdFruitYieldAdjustmentPercentage = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_lab = " + mnPkLaboratoryId + " AND " +
                "id_test = " + mnPkTestId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lab = " + pk[0] + " AND "+
                "id_test = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTestId = 0;

        msSql = "SELECT COALESCE(MAX(id_test), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_lab = " + mnPkLaboratoryId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTestId = resultSet.getInt(1);
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
            mnPkLaboratoryId = resultSet.getInt("id_lab");
            mnPkTestId = resultSet.getInt("id_test");
            mdDensity = resultSet.getDouble("den");
            mdIodineValue = resultSet.getDouble("iod_val");
            mdRefractionIndex = resultSet.getDouble("ref_ind");
            mdImpuritiesPercentage = resultSet.getDouble("imp_per");
            mdMoisturePercentage = resultSet.getDouble("moi_per");
            mdProteinPercentage = resultSet.getDouble("pro_per");
            mdOilContentPercentage = resultSet.getDouble("oil_per");
            mdOilYieldAdjustmentPercentage = resultSet.getDouble("oil_yield_adj_per");
            mdOleicAcidPercentage = resultSet.getDouble("ole_per");
            mdLinoleicAcidPercentage = resultSet.getDouble("lin_per");
            mdLinolenicAcidPercentage = resultSet.getDouble("llc_per");
            mdErucicAcidPercentage = resultSet.getDouble("eru_per");
            mdAcidityPercentage = resultSet.getDouble("aci_per");
            mdAcidityAveragePercentage = resultSet.getDouble("aci_avg_per");
            mtGrindingDate_n = resultSet.getDate("grinding_dt_n");
            msFruitClass = resultSet.getString("fruit_class");
            msFruitRipenessDegree = resultSet.getString("fruit_ripe");
            mdFruitWeightTotal = resultSet.getDouble("fruit_wei_total");
            mdFruitWeightPeelPit = resultSet.getDouble("fruit_wei_peel_pit");
            mdFruitPulpDryMatterPercentage = resultSet.getDouble("fruit_pulp_dry_per");
            mdFruitPulpHumidityPercentage = resultSet.getDouble("fruit_pulp_hum_per");
            mdFruitPulpOilPercentage = resultSet.getDouble("fruit_pulp_oil_per");
            mdFruitYieldAdjustmentPercentage = resultSet.getDouble("fruit_yield_adj_per");

            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        computeFruitParams();

        if (mbRegistryNew) {
            computePrimaryKey(session);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkLaboratoryId + ", " +
                    mnPkTestId + ", " +
                    mdDensity + ", " +
                    mdIodineValue + ", " +
                    mdRefractionIndex + ", " +
                    mdImpuritiesPercentage + ", " +
                    mdMoisturePercentage + ", " +
                    mdProteinPercentage + ", " +
                    mdOilContentPercentage + ", " +
                    mdOilYieldAdjustmentPercentage + ", " + 
                    mdOleicAcidPercentage + ", " +
                    mdLinoleicAcidPercentage + ", " +
                    mdLinolenicAcidPercentage + ", " +
                    mdErucicAcidPercentage + ", " +
                    mdAcidityPercentage + ", " +
                    mdAcidityAveragePercentage + ", " + 
                    (mtGrindingDate_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtGrindingDate_n) + "', ") + 
                    "'" + msFruitClass + "', " + 
                    "'" + msFruitRipenessDegree + "', " + 
                    mdFruitWeightTotal + ", " + 
                    mdFruitWeightPeelPit + ", " + 
                    mdFruitPulpDryMatterPercentage + ", " + 
                    mdFruitPulpHumidityPercentage + ", " + 
                    mdFruitPulpOilPercentage + ", " + 
                    mdFruitYieldAdjustmentPercentage + " " + 
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_lab = " + mnPkLaboratoryId + ", " +
                    //"id_test = " + mnPkTestId + ", " +
                    "den = " + mdDensity + ", " +
                    "iod_val = " + mdIodineValue + ", " +
                    "ref_ind = " + mdRefractionIndex + ", " +
                    "imp_per = " + mdImpuritiesPercentage + ", " +
                    "moi_per = " + mdMoisturePercentage + ", " +
                    "pro_per = " + mdProteinPercentage + ", " +
                    "oil_per = " + mdOilContentPercentage + ", " +
                    "oil_yield_adj_per = " + mdOilYieldAdjustmentPercentage + ", " +
                    "ole_per = " + mdOleicAcidPercentage + ", " +
                    "lin_per = " + mdLinoleicAcidPercentage + ", " +
                    "llc_per = " + mdLinolenicAcidPercentage + ", " +
                    "eru_per = " + mdErucicAcidPercentage + ", " +
                    "aci_per = " + mdAcidityPercentage + ", " +
                    "aci_avg_per = " + mdAcidityAveragePercentage + ", " +
                    "grinding_dt = '" + (mtGrindingDate_n == null ? "NULL, " : SLibUtils.DbmsDateFormatDate.format(mtGrindingDate_n) + "', ") +
                    "fruit_class = '" + msFruitClass + "', " +
                    "fruit_ripe = '" + msFruitRipenessDegree + "', " +
                    "fruit_wei_total = " + mdFruitWeightTotal + ", " +
                    "fruit_wei_peel_pit = " + mdFruitWeightPeelPit + ", " +
                    "fruit_pulp_dry_per = " + mdFruitPulpDryMatterPercentage + ", " +
                    "fruit_pulp_hum_per = " + mdFruitPulpHumidityPercentage + ", " +
                    "fruit_pulp_oil_per = " + mdFruitPulpOilPercentage + ", " +
                    "fruit_yield_adj_per = " + mdFruitYieldAdjustmentPercentage + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbLaboratoryTest clone() throws CloneNotSupportedException {
        SDbLaboratoryTest registry = new SDbLaboratoryTest();

        registry.setPkLaboratoryId(this.getPkLaboratoryId());
        registry.setPkTestId(this.getPkTestId());
        registry.setDensity(this.getDensity());
        registry.setIodineValue(this.getIodineValue());
        registry.setRefractionIndex(this.getRefractionIndex());
        registry.setImpuritiesPercentage(this.getImpuritiesPercentage());
        registry.setMoisturePercentage(this.getMoisturePercentage());
        registry.setProteinPercentage(this.getProteinPercentage());
        registry.setOilContentPercentage(this.getOilContentPercentage());
        registry.setOilYieldAdjustmentPercentage(this.getOilYieldAdjustmentPercentage());
        registry.setOleicAcidPercentage(this.getOleicAcidPercentage());
        registry.setLinoleicAcidPercentage(this.getLinoleicAcidPercentage());
        registry.setLinolenicAcidPercentage(this.getLinolenicAcidPercentage());
        registry.setErucicAcidPercentage(this.getErucicAcidPercentage());
        registry.setAcidityPercentage(this.getAcidityPercentage());
        registry.setAcidityAveragePercentage(this.getAcidityAveragePercentage());
        registry.setGrindingDate_n(this.getGrindingDate_n());
        registry.setFruitClass(this.getFruitClass());
        registry.setFruitRipenessDegree(this.getFruitRipenessDegree());
        registry.setFruitWeightTotal(this.getFruitWeightTotal());
        registry.setFruitWeightPeelPit(this.getFruitWeightPeelPit());
        registry.setFruitPulpDryMatterPercentage(this.getFruitPulpDryMatterPercentage());
        registry.setFruitPulpHumidityPercentage(this.getFruitPulpHumidityPercentage());
        registry.setFruitPulpOilPercentage(this.getFruitPulpOilPercentage());
        registry.setFruitYieldAdjustmentPercentage(this.getFruitYieldAdjustmentPercentage());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public String getName() {
        return mnPkTestId + "";
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
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(final boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = mnPkTestId;
                break;
            case 1:
                value = mdImpuritiesPercentage;
                break;
            case 2:
                value = mdMoisturePercentage;
                break;
            case 3:
                value = mdDensity;
                break;
            case 4:
                value = mdRefractionIndex;
                break;
            case 5:
                value = mdIodineValue;
                break;
            case 6:
                value = mdOleicAcidPercentage;
                break;
            case 7:
                value = mdLinoleicAcidPercentage;
                break;
            case 8:
                value = mdLinolenicAcidPercentage;
                break;
            case 9:
                value = mdErucicAcidPercentage;
                break;
            case 10:
                value = mdProteinPercentage;
                break;
            case 11:
                value = mdOilContentPercentage;
                break;
            case 12:
                value = mdOilYieldAdjustmentPercentage;
                break;
            case 13:
                value = mdAcidityPercentage;
                break;
            case 14:
                value = mdAcidityAveragePercentage;
                break;
            case 15:
                value = mtGrindingDate_n;
                break;
            case 16:
                value = msFruitClass;
                break;
            case 17:
                value = msFruitRipenessDegree;
                break;
            case 18:
                value = mdFruitWeightTotal;
                break;
            case 19:
                value = mdFruitWeightPeelPit;
                break;
            case 20:
                value = mdFruitPulpDryMatterPercentage;
                break;
            case 21:
                value = mdFruitPulpHumidityPercentage;
                break;
            case 22:
                value = mdFruitPulpOilPercentage;
                break;
            case 23:
                value = mdFruitYieldAdjustmentPercentage;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        // grid is non editable
    }
}
