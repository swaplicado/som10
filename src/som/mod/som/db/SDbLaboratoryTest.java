/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores
 * 2018-12-11, Sergio Flores: Adición de parámetros de fruta.
 */
public class SDbLaboratoryTest extends SDbRegistry implements SGridRow {

    protected int mnPkLaboratoryId;
    protected int mnPkTestId;
    protected double mdDensity;
    protected double mdIodineValue;
    protected double mdRefractionIndex;
    protected double mdImpuritiesPercentage;
    protected double mdMoisturePercentage;
    protected double mdProteinPercentage;
    protected double mdOilContentPercentage;
    protected double mdOleicAcidPercentage;
    protected double mdLinoleicAcidPercentage;
    protected double mdLinolenicAcidPercentage;
    protected double mdErucicAcidPercentage;
    protected double mdAcidityPercentage;
    protected String msFruitClass;
    protected String msFruitRipenessDegree;
    protected double mdFruitWeightTotal;
    protected double mdFruitWeightPeelPit;
    protected double mdFruitPulpHumidityPercentage;
    protected double mdFruitPulpOilPercentage;

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
    public void setOleicAcidPercentage(double d) { mdOleicAcidPercentage = d; }
    public void setLinoleicAcidPercentage(double d) { mdLinoleicAcidPercentage = d; }
    public void setLinolenicAcidPercentage(double d) { mdLinolenicAcidPercentage = d; }
    public void setErucicAcidPercentage(double d) { mdErucicAcidPercentage = d; }
    public void setAcidityPercentage(double d) { mdAcidityPercentage = d; }
    public void setFruitClass(String s) { msFruitClass = s; }
    public void setFruitRipenessDegree(String s) { msFruitRipenessDegree = s; }
    public void setFruitWeightTotal(double d) { mdFruitWeightTotal = d; }
    public void setFruitWeightPeelPit(double d) { mdFruitWeightPeelPit = d; }
    public void setFruitPulpHumidityPercentage(double d) { mdFruitPulpHumidityPercentage = d; }
    public void setFruitPulpOilPercentage(double d) { mdFruitPulpOilPercentage = d; }

    public int getPkLaboratoryId() { return mnPkLaboratoryId; }
    public int getPkTestId() { return mnPkTestId; }
    public double getDensity() { return mdDensity; }
    public double getIodineValue() { return mdIodineValue; }
    public double getRefractionIndex() { return mdRefractionIndex; }
    public double getImpuritiesPercentage() { return mdImpuritiesPercentage; }
    public double getMoisturePercentage() { return mdMoisturePercentage; }
    public double getProteinPercentage() { return mdProteinPercentage; }
    public double getOilContentPercentage() { return mdOilContentPercentage; }
    public double getOleicAcidPercentage() { return mdOleicAcidPercentage; }
    public double getLinoleicAcidPercentage() { return mdLinoleicAcidPercentage; }
    public double getLinolenicAcidPercentage() { return mdLinolenicAcidPercentage; }
    public double getErucicAcidPercentage() { return mdErucicAcidPercentage; }
    public double getAcidityPercentage() { return mdAcidityPercentage; }
    public String getFruitClass() { return msFruitClass; }
    public String getFruitRipenessDegree() { return msFruitRipenessDegree; }
    public double getFruitWeightTotal() { return mdFruitWeightTotal; }
    public double getFruitWeightPeelPit() { return mdFruitWeightPeelPit; }
    public double getFruitPulpHumidityPercentage() { return mdFruitPulpHumidityPercentage; }
    public double getFruitPulpOilPercentage() { return mdFruitPulpOilPercentage; }

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
        mdOleicAcidPercentage = 0;
        mdLinoleicAcidPercentage = 0;
        mdLinolenicAcidPercentage = 0;
        mdErucicAcidPercentage = 0;
        mdAcidityPercentage = 0;
        msFruitClass = "";
        msFruitRipenessDegree = "";
        mdFruitWeightTotal = 0;
        mdFruitWeightPeelPit = 0;
        mdFruitPulpHumidityPercentage = 0;
        mdFruitPulpOilPercentage = 0;
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
            mdOleicAcidPercentage = resultSet.getDouble("ole_per");
            mdLinoleicAcidPercentage = resultSet.getDouble("lin_per");
            mdLinolenicAcidPercentage = resultSet.getDouble("llc_per");
            mdErucicAcidPercentage = resultSet.getDouble("eru_per");
            mdAcidityPercentage = resultSet.getDouble("aci_per");
            msFruitClass = resultSet.getString("fruit_class");
            msFruitRipenessDegree = resultSet.getString("fruit_ripe");
            mdFruitWeightTotal = resultSet.getDouble("fruit_wei_total");
            mdFruitWeightPeelPit = resultSet.getDouble("fruit_wei_peel_pit");
            mdFruitPulpHumidityPercentage = resultSet.getDouble("fruit_pulp_hum_per");
            mdFruitPulpOilPercentage = resultSet.getDouble("fruit_pulp_oil_per");

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
                    mdOleicAcidPercentage + ", " +
                    mdLinoleicAcidPercentage + ", " +
                    mdLinolenicAcidPercentage + ", " +
                    mdErucicAcidPercentage + ", " +
                    mdAcidityPercentage + ", " +
                    "'" + msFruitClass + "', " + 
                    "'" + msFruitRipenessDegree + "', " + 
                    mdFruitWeightTotal + ", " + 
                    mdFruitWeightPeelPit + ", " + 
                    mdFruitPulpHumidityPercentage + ", " + 
                    mdFruitPulpOilPercentage + " " + 
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
                    "ole_per = " + mdOleicAcidPercentage + ", " +
                    "lin_per = " + mdLinoleicAcidPercentage + ", " +
                    "llc_per = " + mdLinolenicAcidPercentage + ", " +
                    "eru_per = " + mdErucicAcidPercentage + ", " +
                    "aci_per = " + mdAcidityPercentage + ", " +
                    "fruit_class = '" + msFruitClass + "', " +
                    "fruit_ripe = '" + msFruitRipenessDegree + "', " +
                    "fruit_wei_total = " + mdFruitWeightTotal + ", " +
                    "fruit_wei_peel_pit = " + mdFruitWeightPeelPit + ", " +
                    "fruit_pulp_hum_per = " + mdFruitPulpHumidityPercentage + ", " +
                    "fruit_pulp_oil_per = " + mdFruitPulpOilPercentage + " " +
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
        registry.setOleicAcidPercentage(this.getOleicAcidPercentage());
        registry.setLinoleicAcidPercentage(this.getLinoleicAcidPercentage());
        registry.setLinolenicAcidPercentage(this.getLinolenicAcidPercentage());
        registry.setErucicAcidPercentage(this.getErucicAcidPercentage());
        registry.setAcidityPercentage(this.getAcidityPercentage());
        registry.setFruitClass(this.getFruitClass());
        registry.setFruitRipenessDegree(this.getFruitRipenessDegree());
        registry.setFruitWeightTotal(this.getFruitWeightTotal());
        registry.setFruitWeightPeelPit(this.getFruitWeightPeelPit());
        registry.setFruitPulpHumidityPercentage(this.getFruitPulpHumidityPercentage());
        registry.setFruitPulpOilPercentage(this.getFruitPulpOilPercentage());

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
                value = mdAcidityPercentage;
                break;
            case 13:
                value = msFruitClass;
                break;
            case 14:
                value = msFruitRipenessDegree;
                break;
            case 15:
                value = mdFruitWeightTotal;
                break;
            case 16:
                value = mdFruitWeightPeelPit;
                break;
            case 17:
                value = mdFruitPulpHumidityPercentage;
                break;
            case 18:
                value = mdFruitPulpOilPercentage;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
                mnPkTestId = (int) value;
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:
                break;
            case 16:
                break;
            case 17:
                break;
            case 18:
                break;
            default:
        }
    }
}
