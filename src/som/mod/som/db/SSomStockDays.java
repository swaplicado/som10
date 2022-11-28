/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomStockDays extends SDbRegistry {

    protected int mnPkYearId;
    protected int mnPkMonthId;
    protected int mnPkDayId;
    protected Date mtDate;
    protected int mnXtaFkUnitId;

    protected Vector<SDbStockDay> mvStockDays;
    protected ArrayList<SDbMobileWarehousePremise> mlMobileWarehouses;

    public SSomStockDays() {
        super(SModConsts.S_STK_DAY);
        mvStockDays = new Vector<>();
        mlMobileWarehouses = new ArrayList<>();
        initRegistry();
    }

    private boolean validateOpenPeriod(SGuiSession session) throws Exception {
        boolean b = true;
        SDbMfgEstimation estimation = null;

        // Validate that day is open:

        try {
            estimation = new SDbMfgEstimation();
            estimation.obtainProductionEstimateByDate(session, mtDate, null);

            if (estimation.isClosed()) {

                msQueryResult = "El período está cerrado por estimación de la producción.";
                b = false;
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        return b;
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkMonthId(int n) { mnPkMonthId = n; }
    public void setPkDayId(int n) { mnPkDayId = n; }
    public void setDate(Date t) { mtDate = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkMonthId() { return mnPkMonthId; }
    public int getPkDayId() { return mnPkDayId; }
    public Date getDate() { return mtDate; }

    public void setStockDays(Vector<SDbStockDay> v) { mvStockDays = v; }

    public Vector<SDbStockDay> getStockDays() { return mvStockDays; }
    public ArrayList<SDbMobileWarehousePremise> getMobileWarehouses() { return mlMobileWarehouses; }
    

    public void setXtaFkUnitId(int n) { mnXtaFkUnitId = n; }

    public int getXtaFkUnitId() { return mnXtaFkUnitId; }

    public void obtainRegistries(SGuiSession session, int[] pk) throws SQLException, Exception {
        SDbStockDay stockDay;
        ResultSet resultSet;
        Statement statement;

        msSql = "SELECT s.id_year, s.id_item, s.id_unit, s.id_co, s.id_cob, s.id_wah, s.id_day " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS s " +
                getSqlWhere(pk) + " AND s.b_del = 0 ";

        mvStockDays.removeAllElements();
        statement = session.getStatement().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            stockDay = new SDbStockDay();
            stockDay.read(session, new int[] {
                resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4),
                resultSet.getInt(5), resultSet.getInt(6), resultSet.getInt(7)});
            mvStockDays.add(stockDay);
        }
        mtDate = SLibTimeUtils.createDate(pk[0], pk[1], pk[2]);
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkMonthId = pk[1];
        mnPkDayId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkMonthId, mnPkDayId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkYearId = 0;
        mnPkMonthId = 0;
        mnPkDayId = 0;
        mtDate = null;
        mnXtaFkUnitId = 0;

        mvStockDays.clear();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE s.dt = '" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.createDate(mnPkYearId, mnPkMonthId, mnPkDayId)) + "' ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE s.dt = '" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.createDate(pk[0], pk[1], pk[2])) + "' ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        SDbStockDay stockDay = null;
        Statement statement = null;
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT s.id_year, s.id_item, s.id_unit, s.id_co, s.id_cob, s.id_wah, s.id_day, s.emp, i.name, i.code, u.code, wah.name, wah.code " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS s " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) +" AS i ON " +
                    "s.id_item = i.id_item " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) +" AS u ON " +
                    "s.id_unit = u.id_unit " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) +" AS wah ON " +
                    "s.id_co = wah.id_co AND s.id_cob = wah.id_cob AND s.id_wah = wah.id_wah " +
                    getSqlWhere(pk) + " AND s.b_del = 0 " +
                    "ORDER BY wah.code, s.emp, s.b_del DESC, i.name, i.code, s.id_item ";

        statement = session.getStatement().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        mnXtaFkUnitId = 0;
        while (resultSet.next()) {
            stockDay = new SDbStockDay();
            stockDay.read(session, new int[] {
                resultSet.getInt("s.id_year"), resultSet.getInt("s.id_item"), resultSet.getInt("s.id_unit"), resultSet.getInt("s.id_co"),
                resultSet.getInt("s.id_cob"), resultSet.getInt("s.id_wah"), resultSet.getInt("s.id_day")});

            if (mnXtaFkUnitId == 0) {
                mnXtaFkUnitId = resultSet.getInt("s.id_unit");
            }
            else if (mnXtaFkUnitId != resultSet.getInt("s.id_unit")) {
                mnXtaFkUnitId = -1;
            }

            mvStockDays.add(stockDay);
        }

        mtDate = SLibTimeUtils.createDate(pk[0], pk[1], pk[2]);
        
        msSql = "SELECT "
                    + "* "
                + "FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.S_WAH_PREMISE) + " AS wahp "
                + "WHERE "
                + " NOT wahp.b_del AND wahp.id_dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' ";

        statement = session.getStatement().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        SDbMobileWarehousePremise whsMobile;
        while (resultSet.next()) {
            whsMobile = new SDbMobileWarehousePremise();
            whsMobile.read(session, new Object[] { resultSet.getInt("id_co"), resultSet.getInt("id_cob"), resultSet.getInt("id_wah"), mtDate });
            
            mlMobileWarehouses.add(whsMobile);
        }
        
        mbRegistryNew = false;

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        // Save aswell child registries:

        for (SDbStockDay stockDay : mvStockDays) {
            stockDay.setDate(mtDate);
            stockDay.save(session);
        }
        
        for (SDbMobileWarehousePremise moMobileWarehouse : mlMobileWarehouses) {
            moMobileWarehouse.setPkDate(mtDate);
            moMobileWarehouse.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public boolean canSave(SGuiSession session) throws SQLException, Exception {
        boolean b = false;
        boolean b_del = false;
        boolean b_dis = false;
        ResultSet resultSet;
        Statement statement;

        if (!validateOpenPeriod(session)) {
            b = false;
        }
        else {
            msSql = "SELECT id_co, id_cob, id_wah, code, name, b_dis, b_del " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " " +
                    "WHERE fk_wah_tp IN (" + SModSysConsts.CS_WAH_TP_TAN  + ", " + SModSysConsts.CS_WAH_TP_TAN_MFG + ") " +
                    "ORDER BY b_del DESC, b_dis DESC, id_co, id_cob, id_wah ";

            statement = session.getStatement().getConnection().createStatement();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {

                b = false;
                for (SDbStockDay stockDay : mvStockDays) {

                    if (SLibUtils.compareKeys(
                            new int[] { resultSet.getInt("id_co"), resultSet.getInt("id_cob"), resultSet.getInt("id_wah") },
                            new int[] { stockDay.getPkCompanyId(), stockDay.getPkBranchId(), stockDay.getPkWarehouseId() } ) &&
                            !stockDay.isDeleted()) {
                        b_dis = resultSet.getBoolean("b_dis");
                        b_del = resultSet.getBoolean("b_del");
                        b = true;
                        break;
                    }
                }

                if (!b && !resultSet.getBoolean("b_del") && !resultSet.getBoolean("b_dis")) {
                    msQueryResult = "No se ha capturado el vacío del tanque '" + resultSet.getString("name") + " (" + resultSet.getString("code") + ").";
                    break;
                }
                else if (b && (b_dis || b_del)) {
                    msQueryResult = "El tanque '" + resultSet.getString("name") + " (" + resultSet.getString("code") + ")' está " +
                            (b_dis ? "inhabilitado" : "inactivo") + ".";
                    b = false;
                    break;
                }
            }
        }

        return b;
    }

    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        return validateOpenPeriod(session);
    }

    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        for (SDbStockDay stockDay : mvStockDays) {
            stockDay.delete(session);
        }

        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SSomStockDays clone() throws CloneNotSupportedException {
        SSomStockDays registry = new SSomStockDays();

        registry.setDate(this.getDate());
        registry.setStockDays(mvStockDays);

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
