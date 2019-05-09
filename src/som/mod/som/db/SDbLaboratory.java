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
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Juan Barajas, Alfredo Pérez
 */
public class SDbLaboratory extends SDbRegistryUser {

    protected int mnPkLaboratoryId;
    protected int mnNumber;
    protected Date mtDate;
    protected boolean mbDone;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkItem;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected double mdDensityAverage;
    protected double mdIodineValueAverage;
    protected double mdRefractionIndexAverage;
    protected double mdImpuritiesPercentageAverage;
    protected double mdMoisturePercentageAverage;
    protected double mdProteinPercentageAverage;
    protected double mdOilContentPercentageAverage;
    protected double mdOleicAcidPercentageAverage;
    protected double mdLinoleicAcidPercentageAverage;
    protected double mdLinolenicAcidPercentageAverage;
    protected double mdErucicAcidPercentageAverage;

    protected Vector<SDbLaboratoryNote> mvChildNotes;
    protected Vector<SDbLaboratoryTest> mvChildTests;

    public SDbLaboratory() {
        super(SModConsts.S_LAB);
        mvChildNotes = new Vector<>();
        mvChildTests = new Vector<>();
        initRegistry();
    }

    /*
     * Private methods
     */

    private boolean existsNumber(final SGuiSession session) throws SQLException, Exception {
        boolean exists = false;
        ResultSet resultSet = null;

        msSql = "SELECT COUNT(*) FROM " + getSqlTable() + " " +
                "WHERE b_del = 0 AND num = " + mnNumber + " AND id_lab <> " + mnPkLaboratoryId;

        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            if (resultSet.getInt(1) > 0) {
                exists = true;
            }
        }

        return exists;
    }

    /*
     * Public methods
     */

    public void setPkLaboratoryId(int n) { mnPkLaboratoryId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDone(boolean b) { mbDone = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkItem(int n) { mnFkItem = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setDensityAverage(double d) { mdDensityAverage = d; }
    public void setIodineValueAverage(double d) { mdIodineValueAverage = d; }
    public void setRefractionIndexAverage(double d) { mdRefractionIndexAverage = d; }
    public void setImpuritiesPercentageAverage(double d) { mdImpuritiesPercentageAverage = d; }
    public void setMoisturePercentageAverage(double d) { mdMoisturePercentageAverage = d; }
    public void setProteinPercentageAverage(double d) { mdProteinPercentageAverage = d; }
    public void setOilContentPercentageAverage(double d) { mdOilContentPercentageAverage = d; }
    public void setOleicAcidPercentageAverage(double d) { mdOleicAcidPercentageAverage = d; }
    public void setLinoleicAcidPercentageAverage(double d) { mdLinoleicAcidPercentageAverage = d; }
    public void setLinolenicAcidPercentageAverage(double d) { mdLinolenicAcidPercentageAverage = d; }
    public void setErucicAcidPercentageAverage(double d) { mdErucicAcidPercentageAverage = d; }

    public int getPkLaboratoryId() { return mnPkLaboratoryId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public boolean isDone() { return mbDone; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkItem() { return mnFkItem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public double getDensityAverage() { return mdDensityAverage; }
    public double getIodineValueAverage() { return mdIodineValueAverage; }
    public double getRefractionIndexAverage() { return mdRefractionIndexAverage; }
    public double getImpuritiesPercentageAverage() { return mdImpuritiesPercentageAverage; }
    public double getMoisturePercentageAverage() { return mdMoisturePercentageAverage; }
    public double getProteinPercentageAverage() { return mdProteinPercentageAverage; }
    public double getOilContentPercentageAverage() { return mdOilContentPercentageAverage; }
    public double getOleicAcidPercentageAverage() { return mdOleicAcidPercentageAverage; }
    public double getLinoleicAcidPercentageAverage() { return mdLinoleicAcidPercentageAverage; }
    public double getLinolenicAcidPercentageAverage() { return mdLinolenicAcidPercentageAverage; }
    public double getErucicAcidPercentageAverage() { return mdErucicAcidPercentageAverage; }

    public Vector<SDbLaboratoryNote> getChildNotes() { return mvChildNotes; }
    public Vector<SDbLaboratoryTest> getChildTests() { return mvChildTests; }

    public static int getNumberNext(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;
        String sql = "";
        int numberNext = 0;

        sql = "SELECT COALESCE(MAX(num), 0) + 1 FROM " + SModConsts.TablesMap.get(SModConsts.S_LAB) + " WHERE b_del = 0 ";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            numberNext = resultSet.getInt(1);
        }

        return numberNext;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLaboratoryId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLaboratoryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLaboratoryId = 0;
        mnNumber = 0;
        mtDate = null;
        mbDone = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkItem = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mdDensityAverage = 0;
        mdIodineValueAverage = 0;
        mdRefractionIndexAverage = 0;
        mdImpuritiesPercentageAverage = 0;
        mdMoisturePercentageAverage = 0;
        mdProteinPercentageAverage = 0;
        mdOilContentPercentageAverage = 0;
        mdOleicAcidPercentageAverage = 0;
        mdLinoleicAcidPercentageAverage = 0;
        mdLinolenicAcidPercentageAverage = 0;
        mdErucicAcidPercentageAverage = 0;

        mvChildNotes.clear();
        mvChildTests.clear();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_lab = " + mnPkLaboratoryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lab = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLaboratoryId = 0;

        msSql = "SELECT COALESCE(MAX(id_lab), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLaboratoryId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            mbDone = resultSet.getBoolean("b_done");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkItem = resultSet.getInt("fk_item");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_note FROM " + SModConsts.TablesMap.get(SModConsts.S_LAB_NOTE) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbLaboratoryNote child = new SDbLaboratoryNote();
                child.read(session, new int[] { mnPkLaboratoryId, resultSet.getInt(1) });
                mvChildNotes.add(child);
            }

            msSql = "SELECT id_test FROM " + SModConsts.TablesMap.get(SModConsts.S_LAB_TEST) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbLaboratoryTest child = new SDbLaboratoryTest();
                child.read(session, new int[] { mnPkLaboratoryId, resultSet.getInt(1) });
                mvChildTests.add(child);
            }

            if (mvChildTests.size() > SLibConsts.UNDEFINED) {
                computeTestsAverage();
            }

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
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkLaboratoryId + ", " +
                    mnNumber + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    (mbDone ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkItem + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_lab = " + mnPkLaboratoryId + ", " +
                    "num = " + mnNumber + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "b_done = " + (mbDone ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_item = " + mnFkItem + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.S_LAB_NOTE) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (SDbLaboratoryNote child : mvChildNotes) {
            child.setPkLaboratoryId(mnPkLaboratoryId);
            child.setRegistryNew(true);
            child.save(session);
        }

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.S_LAB_TEST) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (SDbLaboratoryTest child : mvChildTests) {
            child.setPkLaboratoryId(mnPkLaboratoryId);
            child.setRegistryNew(true);
            child.save(session);
        }
        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbLaboratory clone() throws CloneNotSupportedException {
        SDbLaboratory registry = new SDbLaboratory();

        registry.setPkLaboratoryId(this.getPkLaboratoryId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setDone(this.isDone());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkItem(this.getFkItem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setDensityAverage(this.getDensityAverage());
        registry.setIodineValueAverage(this.getIodineValueAverage());
        registry.setRefractionIndexAverage(this.getRefractionIndexAverage());
        registry.setImpuritiesPercentageAverage(this.getImpuritiesPercentageAverage());
        registry.setMoisturePercentageAverage(this.getMoisturePercentageAverage());
        registry.setProteinPercentageAverage(this.getProteinPercentageAverage());
        registry.setOilContentPercentageAverage(this.getOilContentPercentageAverage());
        registry.setOleicAcidPercentageAverage(this.getOleicAcidPercentageAverage());
        registry.setLinoleicAcidPercentageAverage(this.getLinoleicAcidPercentageAverage());
        registry.setLinolenicAcidPercentageAverage(this.getLinolenicAcidPercentageAverage());
        registry.setErucicAcidPercentageAverage(this.getErucicAcidPercentageAverage());

        for (SDbLaboratoryNote  child : mvChildNotes) {
            registry.getChildNotes().add(child.clone());
        }

        for (SDbLaboratoryTest child : mvChildTests) {
            registry.getChildTests().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);

        if (can) {
            initQueryMembers();
            can = !existsNumber(session);

            if (!can) {
                msQueryResult = "¡El análisis ya existe!";
            }
        }

        return can;
    }

    @Override
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;
        boolean can = super.canDelete(session);

        if (can) {
            initQueryMembers();
            msSql = "SELECT fk_tic_st FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " "
                    + "WHERE fk_lab_n = " + mnPkLaboratoryId + " AND b_del = 0 ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                msQueryResult = SDbConsts.ERR_MSG_REG_NOT_FOUND + "\n¡No se encontró el boleto asociado al análisis de laboratorio!";
            }
            else {
                can = resultSet.getInt(1) == SModSysConsts.SS_TIC_ST_LAB;
            }

            if (!can) {
                msQueryResult = "¡No puede eliminar análisis de laboratorio '" + mnNumber + "' porque el boleto no tiene el estatus "
                        + "'" + session.readField(SModConsts.SS_TIC_ST, new int[] { SModSysConsts.SS_TIC_ST_LAB }, SDbRegistry.FIELD_NAME) + "'.!";
            }
        }

        return can;
    }

    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        mbDeleted = !mbDeleted;
        mnFkUserUpdateId = session.getUser().getPkUserId();

        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();

        session.getStatement().execute(msSql);

        msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " SET " +
                "sys_pen_per = 0, " +
                "usr_pen_per = 0, " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                "WHERE fk_lab_n = " + mnPkLaboratoryId + " AND b_del = 0 ";

        session.getStatement().execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    public void computeTestsAverage() {
        int nNumImp = 0;
        int nNumMoi = 0;
        int nNumDen = 0;
        int nNumIodVal = 0;
        int nNumRefInd = 0;
        int nNumPro = 0;
        int nNumOil = 0;
        int nNumOleAcid = 0;
        int nNumLinAcid = 0;
        int nNumLlcAcid = 0;
        int nNumEruAcid = 0;

        mdImpuritiesPercentageAverage = 0;
        mdMoisturePercentageAverage = 0;
        mdDensityAverage = 0;
        mdIodineValueAverage = 0;
        mdRefractionIndexAverage = 0;
        mdProteinPercentageAverage = 0;
        mdOilContentPercentageAverage = 0;
        mdOleicAcidPercentageAverage = 0;
        mdLinoleicAcidPercentageAverage = 0;
        mdLinolenicAcidPercentageAverage = 0;
        mdErucicAcidPercentageAverage = 0;

        for (SDbLaboratoryTest child : mvChildTests) {
            mdDensityAverage += child.getDensity();
            mdIodineValueAverage += child.getIodineValue();
            mdRefractionIndexAverage += child.getRefractionIndex();
            mdImpuritiesPercentageAverage += child.getImpuritiesPercentage();
            mdMoisturePercentageAverage += child.getMoisturePercentage();
            mdProteinPercentageAverage += child.getProteinPercentage();
            mdOilContentPercentageAverage += child.getOilContentPercentage();
            mdOleicAcidPercentageAverage += child.getOleicAcidPercentage();
            mdLinoleicAcidPercentageAverage += child.getLinoleicAcidPercentage();
            mdLinolenicAcidPercentageAverage += child.getLinolenicAcidPercentage();
            mdErucicAcidPercentageAverage += child.getErucicAcidPercentage();

            nNumImp += (child.getImpuritiesPercentage() > 0 ? 1 : 0);
            nNumMoi += (child.getMoisturePercentage() > 0 ? 1 : 0);
            nNumDen += (child.getDensity() > 0 ? 1 : 0);
            nNumIodVal += (child.getIodineValue() > 0 ? 1 : 0);
            nNumRefInd += (child.getRefractionIndex() > 0 ? 1 : 0);
            nNumPro += (child.getProteinPercentage() > 0 ? 1 : 0);
            nNumOil += (child.getOilContentPercentage() > 0 ? 1 : 0);
            nNumOleAcid += (child.getOleicAcidPercentage() > 0 ? 1 : 0);
            nNumLinAcid += (child.getLinoleicAcidPercentage() > 0 ? 1 : 0);
            nNumLlcAcid += (child.getLinolenicAcidPercentage() > 0 ? 1 : 0);
            nNumEruAcid += (child.getErucicAcidPercentage() > 0 ? 1 : 0);
        }

        mdDensityAverage = (nNumDen > 0 ? mdDensityAverage / nNumDen : 0);
        mdIodineValueAverage = (nNumIodVal > 0 ? mdIodineValueAverage / nNumIodVal : 0);
        mdRefractionIndexAverage = (nNumRefInd > 0 ? mdRefractionIndexAverage / nNumRefInd : 0);
        mdImpuritiesPercentageAverage = (nNumImp > 0 ? SLibUtils.round(mdImpuritiesPercentageAverage / nNumImp, SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits()) : 0);
        mdMoisturePercentageAverage = (nNumMoi > 0 ? SLibUtils.round(mdMoisturePercentageAverage / nNumMoi, SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits()) : 0);
        mdProteinPercentageAverage = (nNumPro > 0 ? mdProteinPercentageAverage / nNumPro : 0);
        mdOilContentPercentageAverage = (nNumOil > 0 ? mdOilContentPercentageAverage / nNumOil : 0);
        mdOleicAcidPercentageAverage = (nNumOleAcid > 0 ? mdOleicAcidPercentageAverage / nNumOleAcid : 0);
        mdLinoleicAcidPercentageAverage = (nNumLinAcid > 0 ? mdLinoleicAcidPercentageAverage / nNumLinAcid : 0);
        mdLinolenicAcidPercentageAverage = (nNumLlcAcid > 0 ? mdLinolenicAcidPercentageAverage / nNumLlcAcid : 0);
        mdErucicAcidPercentageAverage = (nNumEruAcid > 0 ? mdErucicAcidPercentageAverage / nNumEruAcid : 0);
    }
}
