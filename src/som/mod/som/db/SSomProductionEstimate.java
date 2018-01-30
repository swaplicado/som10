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
public class SSomProductionEstimate extends SDbRegistry {

    protected Date mtDate;
    protected double mdProductionEstimate;
    protected int mnFkMfgEstimateId_n;
    protected int mnFkMfgEstimateVersion_n;
    protected int mnFkUnitId;
    protected int mnFkUserUpdateId;

    protected Vector<SSomProductionEmpty> mvProductionEmpties;
    protected Vector<SSomMfgWarehouseProduct> mvMfgWarehouseProductsProduction;
    protected Vector<SRowProductionInventory> mvProductionInventories;
    protected Vector<SDbIog> mvCanSaveIogs;

    protected ArrayList<Object[]> maItemsStockDay;
    protected ArrayList<Object[]> maItemsStockSystem;
    protected ArrayList<Object[]> maItemsStockSystemCurrently;

    public void setDate(Date t) { mtDate = t; }
    public void setProductionEstimate(double d) { mdProductionEstimate = d; }
    public void setMfgEstimateId_n(int n) { mnFkMfgEstimateId_n = n; }
    public void setFkMfgEstimateVersion_n(int n) { mnFkMfgEstimateVersion_n = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }

    public Date getDate() { return mtDate; }
    public double getProductionEstimate() { return mdProductionEstimate; }
    public int getMfgEstimateId_n() { return mnFkMfgEstimateId_n; }
    public int getFkMfgEstimateVersion_n() { return mnFkMfgEstimateVersion_n; }
    public int setFkUnitId() { return mnFkUnitId; }
    public int setFkUserUpdateId() { return mnFkUserUpdateId; }

    public ArrayList<Object[]> getItemsStockDay() { return maItemsStockDay; }
    public ArrayList<Object[]> getItemsStockSystem() { return maItemsStockSystem; }
    public ArrayList<Object[]> getItemsStockSystemCurrently() { return maItemsStockSystemCurrently; }

    public Vector<SSomProductionEmpty> getProductionEmpties() { return mvProductionEmpties; }
    public Vector<SSomMfgWarehouseProduct> getMfgWarehouseProductsProduction() { return mvMfgWarehouseProductsProduction; }
    public Vector<SRowProductionInventory> getProductionInventories() { return mvProductionInventories; }
    public Vector<SDbIog> getCanSaveIogs() { return mvCanSaveIogs; }

    public SSomProductionEstimate() {
        super(SModConsts.SX_STK_PROD_EST);

        mvProductionEmpties = new Vector<SSomProductionEmpty>();
        mvMfgWarehouseProductsProduction = new Vector<SSomMfgWarehouseProduct>();
        mvProductionInventories = new Vector<SRowProductionInventory>();
        mvCanSaveIogs = new Vector<SDbIog>();

        maItemsStockDay = new ArrayList<Object[]>();
        maItemsStockSystem = new ArrayList<Object[]>();
        maItemsStockSystemCurrently = new ArrayList<Object[]>();

        initRegistry();
    }

    /*
     * Private methods
     * */

    private double[] productionEstimateGlobal(final SGuiSession session) {
        double dStockDayQuantity = 0;
        double dStockSystemQuantity = 0;
        String sql = "";

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            sql = computeQueryStockDaySystem(true, false, false);
            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                dStockDayQuantity = 0;
            }
            else {
                dStockDayQuantity = resultSet.getDouble(1);
            }

            sql = computeQueryStockDaySystem(false, false, false);
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                dStockSystemQuantity = 0;
            }
            else {
                dStockSystemQuantity = resultSet.getDouble(1);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        return new double[] { dStockDayQuantity, dStockSystemQuantity };
    }

    private void productionEstimateByProduct(final SGuiSession session) {
        String sql = "";

        Statement statement = null;
        ResultSet resultSet = null;

        maItemsStockDay.clear();
        maItemsStockSystem.clear();
        maItemsStockSystemCurrently.clear();

        try {
            sql = computeQueryStockDaySystem(true, true, false);
            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                maItemsStockDay.add(new Object[] { resultSet.getInt("i.id_item"), resultSet.getString("i.name"), resultSet.getString("i.code"),
                    resultSet.getDouble("f_stk_day"), resultSet.getString("u.code") });
            }

            sql = computeQueryStockDaySystem(false, true, false);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                maItemsStockSystem.add(new Object[] { resultSet.getInt("i.id_item"), resultSet.getString("i.name"), resultSet.getString("i.code"),
                    resultSet.getDouble("f_stock"), resultSet.getString("u.code"), resultSet.getDouble("f_stock_dly") });
            }

            sql = computeQueryStockDaySystem(false, true, true);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                maItemsStockSystemCurrently.add(new Object[] { resultSet.getInt("i.id_item"), resultSet.getString("i.name"), resultSet.getString("i.code"),
                    resultSet.getDouble("f_stock"), resultSet.getString("u.code") });
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private String computeQueryStockDaySystem(final boolean stk_day, final boolean by_product, final boolean stk_sys_currently) {
        String sql = "";

        if (stk_day) {

            // Daily stock global or by product:

            sql = "SELECT SUM(sd.stk_day) AS f_stk_day " + (by_product ? ", i.id_item, i.name, i.code, u.code" : "") + " " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sd ON " +
                "sd.id_co = wah.id_co AND sd.id_cob = wah.id_cob AND sd.id_wah = wah.id_wah AND sd.b_del = 0 AND sd.b_stk_dif_skp = 0 AND " +
                "sd.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "sd.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "sd.id_unit = u.id_unit AND sd.id_unit = 3 " + // mnFkUnitId
                "WHERE wah.b_del = 0 AND wah.b_dis = 0 AND wah.fk_wah_tp = " + SModSysConsts.CS_WAH_TP_TAN + " " +
                (by_product ? "GROUP BY i.id_item" : "") + " ";
        }
        else {

            // Stock system global, by product or by product to currently day:

            sql = "SELECT SUM(s.mov_in - s.mov_out) - " +
                "COALESCE((SELECT SUM(ss.mov_in - ss.mov_out) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS ss " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS si ON " +
                "ss.fk_iog = si.id_iog AND si.b_sys = 1 " +
                "WHERE ss.b_del = 0 AND ss.b_sys = 1 AND s.id_co = ss.id_co AND s.id_cob = ss.id_cob AND s.id_wah = ss.id_wah AND s.id_item = ss.id_item AND s.id_unit = ss.id_unit AND " +
                "ss.fk_iog_ct = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0] + " AND ss.fk_iog_cl = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1] + " AND " +
                "ss.fk_iog_tp = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2] + " AND " +
                "ss.dt IN " +
                "(DATE_SUB('" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', INTERVAL 1 DAY)" +
                (stk_sys_currently ? ", '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "'" : "") + ")" +
                "), 0) AS f_stock " + (by_product ? ", i.id_item, i.name, i.code, u.code" : "") + ", " +

                "COALESCE((SELECT SUM(ss.mov_in - ss.mov_out) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS ss " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS si ON " +
                "ss.fk_iog = si.id_iog AND si.b_sys = 1 " +
                "WHERE ss.b_del = 0 AND ss.b_sys = 1 AND s.id_co = ss.id_co AND s.id_cob = ss.id_cob AND s.id_wah = ss.id_wah AND s.id_item = ss.id_item AND s.id_unit = ss.id_unit AND " +
                "si.fk_mfg_est_n = " + mnFkMfgEstimateId_n + " AND si.fk_mfg_est_ver_n = " + mnFkMfgEstimateVersion_n + "), 0) AS f_stock_dly " +

                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " +
                "s.id_co = wah.id_co AND s.id_cob = wah.id_cob AND s.id_wah = wah.id_wah AND wah.fk_wah_tp = " + SModSysConsts.CS_WAH_TP_TAN + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
                "s.id_div = d.id_div " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                "s.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                "s.id_unit = u.id_unit AND s.id_unit = 3 " + // mnFkUnitId
                "WHERE s.b_del = 0 AND s.id_year = " + SLibTimeUtils.digestYear(mtDate)[0] + " AND s.dt < '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' " +
                (by_product ? "GROUP BY i.id_item" : "") + " " +
                "HAVING f_stock <> 0 ";
        }

        return sql;
    }

    private int validateProductionEstimateByProduct() {
        int result = 0;

        for (Object[] oItemStockDay : maItemsStockDay) {
            for (Object[] oItemStockSystem : maItemsStockSystem) {

                // Compare items:

                if ((Integer) oItemStockDay[0] == (Integer) oItemStockSystem[0]) {

                    // Compare quantity:

                    if ((Integer) oItemStockDay[0] < (Integer) oItemStockSystem[0]) {

                        msQueryResult = "No se puede estimar la producción debido a que la existencia de sistema por ítem es mayor a la existencia física por ítem.";
                        result = SDbConsts.READ_ERROR;
                        break;
                    }
                }
            }

            if (result == SDbConsts.READ_ERROR) {
                break;
            }
        }

        return result;
    }

    private void obtainManufacturedProducts(SGuiSession session) {
        SSomMfgWarehouseProduct products = null;

        productionEstimateByProduct(session);
        for (Object[] oItemStockDay : maItemsStockDay) {
            for (Object[] oItemStockSystem : maItemsStockSystem) {

                // Compare items:

                if ((Integer) oItemStockDay[0] == (Integer) oItemStockSystem[0]) {

                    // Compare quantity:

                    if ((Double) oItemStockDay[3] > (Double) oItemStockSystem[3]) {

                        // Calculate production:

                        oItemStockSystem[3] = (Double) oItemStockDay[3] - (Double) oItemStockSystem[3];

                        products = new SSomMfgWarehouseProduct();
                        products.setPkItemId((Integer) oItemStockSystem[0]);
                        products.setPkUnitId(mnFkUnitId);
                        products.setItem((String) oItemStockSystem[1]);
                        products.setItemCode((String) oItemStockSystem[2]);
                        products.setUnitCode((String) oItemStockSystem[4]);
                        products.setQuantity((Double) oItemStockSystem[3]);
                        products.setQuantityDelivery((Double) oItemStockSystem[5]);
                        mvMfgWarehouseProductsProduction.add(products);
                    }
                }
            }
        }
    }

    /*
     * Public methods
     *
     * */


    @Override
    public void setPrimaryKey(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initRegistry() {
        mtDate = null;
        mdProductionEstimate = 0;
        mnFkMfgEstimateId_n = 0;
        mnFkMfgEstimateVersion_n = 0;
        mnFkUnitId = 0;
        mnFkUserUpdateId = 0;

        mvProductionEmpties.clear();
        mvProductionInventories.clear();

        maItemsStockDay.clear();
        maItemsStockSystem.clear();
        maItemsStockSystemCurrently.clear();
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean validateEstimateProduction(SGuiSession session) {
        double[] adStockDaySystem = null;

        boolean res = true;

        // Validate if production estimate global is correct:

        adStockDaySystem = productionEstimateGlobal(session);
        if (adStockDaySystem[0] < adStockDaySystem[1]) {

            msQueryResult = "No se puede estimar la producción debido a que la existencia global de sistema es mayor a la existencia global física.";
            mnQueryResultId = SDbConsts.READ_ERROR;
        }
        else if (adStockDaySystem[0] == adStockDaySystem[1]) {

            msQueryResult = "No se puede estimar la producción debido a que la existencia global de sistema es igual a la existencia global física.";
            mnQueryResultId = SDbConsts.READ_ERROR;
        }
        else {
            // Production estimate:

            mdProductionEstimate = adStockDaySystem[0] - adStockDaySystem[1];

            // Validate if production estimate by product is correct:

            productionEstimateByProduct(session);
            mnQueryResultId = validateProductionEstimateByProduct();

            if (mnQueryResultId != SDbConsts.READ_ERROR) {

            }
        }

        return res;
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {

        String sql = "";

        Statement statement = null;
        ResultSet resultSet = null;
        SDbStockDay stockDay = null;
        SSomProductionEmpty productionEmpty = null;
        SRowProductionInventory productionInventory = null;

        /*
        // Validate if production estimate global is correct:

        adStockDaySystem = productionEstimateGlobal(session);
        if (adStockDaySystem[0] < adStockDaySystem[1]) {

            msQueryResult = "No se puede estimar la producción debido a que la existencia global de sistema es mayor a la existencia global física.";
            mnQueryResultId = SDbConsts.READ_ERROR;
        }
        else if (adStockDaySystem[0] == adStockDaySystem[1]) {

            msQueryResult = "No se puede estimar la producción debido a que la existencia global de sistema es igual a la existencia global física.";
            mnQueryResultId = SDbConsts.READ_ERROR;
        }
        else {
            // Production estimate:

            mdProductionEstimate = adStockDaySystem[0] - adStockDaySystem[1];

            // Validate if production estimate by product is correct:

            productionEstimateByProduct(session);
            mnQueryResultId = validateProductionEstimateByProduct();

            if (mnQueryResultId != SDbConsts.READ_ERROR) {
        */
                // Obtain manufactured items:

                obtainManufacturedProducts(session);

                // Daily stock by warehouse:

                sql = "SELECT wah.id_co, wah.id_cob, wah.id_wah, sd.id_year, sd.id_item, sd.id_unit, sd.id_co, sd.id_cob, sd.id_wah, sd.id_day, " +
                    "COALESCE(sd.emp, 0) AS f_emp, sd.b_emp, sd.stk_day, sd.cull, sd.dt, wah.code, wah.name, wah.dim_heig, wah.cap_real_lt, i.name, " +
                    "i.code, i.den, u.code, i.fk_item_tp, i.fk_item_rm_n, i.fk_item_bp_n, i.fk_item_cu_n " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah " +
                    "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_STK_DAY) + " AS sd ON " +
                    "sd.id_co = wah.id_co AND sd.id_cob = wah.id_cob AND sd.id_wah = wah.id_wah AND sd.b_del = 0 AND sd.b_stk_dif_skp = 0 AND " +
                    "sd.dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' " +
                    "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                    "sd.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
                    "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                    "sd.id_unit = u.id_unit " +
                    "WHERE wah.b_del = 0 AND wah.b_dis = 0 AND wah.fk_wah_tp = " + SModSysConsts.CS_WAH_TP_TAN + " " +
                    "ORDER BY sd.id_day; ";

                statement = session.getDatabase().getConnection().createStatement();
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    productionEmpty = new SSomProductionEmpty();

                    productionEmpty.setPkWarehouseCompanyId(resultSet.getInt("wah.id_co"));
                    productionEmpty.setPkWarehouseBranchId(resultSet.getInt("wah.id_cob"));
                    productionEmpty.setPkWarehouseWarehouseId(resultSet.getInt("wah.id_wah"));

                    productionEmpty.setXtaPkItemId(resultSet.getInt("sd.id_item"));
                    productionEmpty.setXtaPkUnitId(resultSet.getInt("sd.id_unit"));
                    productionEmpty.setXtaEmpty(resultSet.getBoolean("sd.b_emp"));
                    productionEmpty.setXtaStockDifferenceSkipped(resultSet.getBoolean("sd.b_stk_dif_skp"));
                    productionEmpty.setXtaEmptiness(resultSet.getDouble("f_emp"));
                    productionEmpty.setXtaEmptyKg(resultSet.getDouble("sd.stk_day"));
                    productionEmpty.setXtaCull(resultSet.getDouble("sd.cull"));
                    productionEmpty.setXtaWarehouse(resultSet.getString("wah.name"));
                    productionEmpty.setXtaWarehouseCode(resultSet.getString("wah.code"));
                    productionEmpty.setXtaWarehouseHeight(resultSet.getDouble("wah.dim_heig"));
                    productionEmpty.setXtaCapacityRealLiter(resultSet.getDouble("wah.cap_real_lt"));
                    productionEmpty.setXtaFGItem(resultSet.getString("i.name"));
                    productionEmpty.setXtaFGItemCode(resultSet.getString("i.code"));
                    productionEmpty.setXtaFGItemUnitCode(resultSet.getString("u.code"));
                    productionEmpty.setXtaFGItemDensity(resultSet.getDouble("i.den"));

                    // Read raw material item, read by product percentage, read waste percentage and read warehouse:

                    /*
                    itemRawMaterial = new SDbItem();
                    unit = new SDbUnit();
                    productionEmpty.setXtaFkRMItemId(resultSet.getInt("i.fk_item_rm_n"));
                    if (!resultSet.wasNull()) {

                        itemRawMaterial.read(session, new int[] { productionEmpty.getXtaFkRMItemId() });
                        if (itemRawMaterial.getQueryResultId() == SDbConsts.READ_OK) {

                            unit.read(session, new int[] { itemRawMaterial.getFkUnitId() });
                            if (unit.getQueryResultId() == SDbConsts.READ_OK) {

                                // Validate if raw material is raw material type:

                                if (itemRawMaterial.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_RM) {
                                    msQueryResult = "La materia prima '" + itemRawMaterial.getName() + " (" + itemRawMaterial.getCode() + ")' asignada al producto terminado \n" +
                                        "'" + productionEmpty.getXtaFGItem() + " (" + productionEmpty.getXtaFGItemCode() + ")' no es de tipo '" +
                                        session.readField(SModSysConsts.SS_ITEM_TP_RM, new int[] { itemRawMaterial.getFkInputTypeId() }, SDbRegistry.FIELD_NAME) + "'.";
                                    mnQueryResultId = SDbConsts.READ_ERROR;
                                    break;
                                }
                                else if (itemRawMaterial.getMfgFinishedGoodPercentage() <= 0) {

                                    // Validate if percentage of finished good > 0:

                                    msQueryResult = "El producto terminado '" + productionEmpty.getXtaFGItem() + " (" + productionEmpty.getXtaFGItemCode() + ")' de la materia prima \n" +
                                        "'" + itemRawMaterial.getName() + " (" + itemRawMaterial.getCode() + ")' debe tener asignado un porcentaje mayor a 0.";
                                    mnQueryResultId = SDbConsts.READ_ERROR;
                                    break;
                                }

                                productionEmpty.setXtaRMItem(itemRawMaterial.getName());
                                productionEmpty.setXtaRMItemCode(itemRawMaterial.getCode());
                                productionEmpty.setXtaRMItemUnitCode(unit.getCode());
                                productionEmpty.setXtaFGPercentage(itemRawMaterial.getMfgFinishedGoodPercentage());
                                productionEmpty.setXtaBPPercentage(itemRawMaterial.getMfgByproductPercentage());
                                productionEmpty.setXtaCUPercentage(itemRawMaterial.getMfgCullPercentage());

                                productionEmpty.setXtaFkRMItemUnitId(itemRawMaterial.getFkUnitId());
                                productionEmpty.setXtaFkRMWarehouseCompanyId(itemRawMaterial.getFkWarehouseCompanyId_n());
                                productionEmpty.setXtaFkRMWarehouseBranchId(itemRawMaterial.getFkWarehouseBranchId_n());
                                productionEmpty.setXtaFkRMWarehouseWarehouseId(itemRawMaterial.getFkWarehouseWarehouseId_n());
                            }
                        }
                    }
                    * */

                    // Read subgood item and warehouse:

                    /*
                    item = new SDbItem();
                    productionEmpty.setXtaFkBPItemId(resultSet.getInt("i.fk_item_bp_n"));
                    if (!resultSet.wasNull()) {

                        item.read(session, new int[] { productionEmpty.getXtaFkBPItemId() });
                        if (item.getQueryResultId() == SDbConsts.READ_OK) {

                            unit.read(session, new int[] { item.getFkUnitId() });
                            if (unit.getQueryResultId() == SDbConsts.READ_OK) {

                                // Validate if by product is by product type:

                                if (item.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_BP) {
                                    msQueryResult = "El subproducto '" + item.getName() + " (" + item.getCode() + ")' de la materia prima \n" +
                                        "'" + productionEmpty.getXtaRMItem() + " (" + productionEmpty.getXtaRMItemCode() + ")' no es de tipo '" +
                                        session.readField(SModSysConsts.SS_ITEM_TP_BP, new int[] { item.getFkInputTypeId() }, SDbRegistry.FIELD_NAME) + "'.";
                                    mnQueryResultId = SDbConsts.READ_ERROR;
                                    break;
                                }
                                else if (itemRawMaterial.getMfgByproductPercentage() <= 0) {

                                    // Validate if percentage of by product > 0:

                                    msQueryResult = "El subproducto '" + item.getName() + " (" + item.getCode() + ")' de la materia prima \n" +
                                        "'" + itemRawMaterial.getName() + " (" + itemRawMaterial.getCode() + ")' debe tener asignado un porcentaje mayor a 0.";
                                    mnQueryResultId = SDbConsts.READ_ERROR;
                                    break;
                                }

                                productionEmpty.setXtaBPItem(item.getName());
                                productionEmpty.setXtaBPItemCode(item.getCode());
                                productionEmpty.setXtaBPItemUnitCode(unit.getCode());

                                productionEmpty.setXtaFkBPItemUnitId(item.getFkUnitId());
                                productionEmpty.setXtaFkBPWarehouseCompanyId(item.getFkWarehouseCompanyId_n());
                                productionEmpty.setXtaFkBPWarehouseBranchId(item.getFkWarehouseBranchId_n());
                                productionEmpty.setXtaFkBPWarehouseWarehouseId(item.getFkWarehouseWarehouseId_n());
                            }
                        }
                    }
                    * */

                    // Validate if finished good hasn't item and percentage in the raw material by product is > 0:

                    /*
                    if (productionEmpty.getXtaFkBPItemId() == 0 && itemRawMaterial.getMfgByproductPercentage() > 0) {

                        msQueryResult = "El producto terminado '" + productionEmpty.getXtaFGItem() + " (" + productionEmpty.getXtaFGItemCode() + ")' " +
                            "debe tener asignado un subproducto.";
                        mnQueryResultId = SDbConsts.READ_ERROR;
                        break;
                    }
                    * */

                    // Read waste item and warehouse:

                    /*
                    item = new SDbItem();
                    productionEmpty.setXtaFkCUItemId(resultSet.getInt("i.fk_item_cu_n"));
                    if (!resultSet.wasNull()) {

                        item.read(session, new int[] { productionEmpty.getXtaFkCUItemId() });
                        if (item.getQueryResultId() == SDbConsts.READ_OK) {

                            unit.read(session, new int[] { item.getFkUnitId() });
                            if (unit.getQueryResultId() == SDbConsts.READ_OK) {

                                // Validate if cull is cull type:

                                if (item.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_CU) {
                                    msQueryResult = "La desecho '" + item.getName() + " (" + item.getCode() + ")' de la materia prima " +
                                        "'" + productionEmpty.getXtaRMItem() + " (" + productionEmpty.getXtaRMItemCode() + ")' no es de tipo '" +
                                        session.readField(SModSysConsts.SS_ITEM_TP_CU, new int[] { item.getFkInputTypeId() }, SDbRegistry.FIELD_NAME) + "'.";
                                    mnQueryResultId = SDbConsts.READ_ERROR;
                                    break;
                                }
                                else if (itemRawMaterial.getMfgCullPercentage() <= 0) {

                                    // Validate if percentage of cull > 0:

                                    msQueryResult = "El desecho '" + item.getName() + " (" + item.getCode() + ")' de la materia prima " +
                                        "'" + itemRawMaterial.getName() + " (" + itemRawMaterial.getCode() + ")' debe tener asignado un porcentaje mayor a 0.";
                                    mnQueryResultId = SDbConsts.READ_ERROR;
                                    break;
                                }

                                productionEmpty.setXtaCUItem(item.getName());
                                productionEmpty.setXtaCUItemCode(item.getCode());
                                productionEmpty.setXtaCUItemUnitCode(unit.getCode());

                                productionEmpty.setXtaFkCUItemUnitId(item.getFkUnitId());
                                productionEmpty.setXtaFkCUWarehouseCompanyId(item.getFkWarehouseCompanyId_n());
                                productionEmpty.setXtaFkCUWarehouseBranchId(item.getFkWarehouseBranchId_n());
                                productionEmpty.setXtaFkCUWarehouseWarehouseId(item.getFkWarehouseWarehouseId_n());
                            }
                        }
                    }
                    * */

                    // Validate if finished good hasn't item and percentage in the raw material by product is > 0:

                    /*
                    if (productionEmpty.getXtaFkCUItemId() == 0 && itemRawMaterial.getMfgCullPercentage() > 0) {

                        msQueryResult = "El producto terminado '" + productionEmpty.getXtaFGItem() + " (" + productionEmpty.getXtaFGItemCode() + ")' " +
                            "debe tener asignado un desecho.";
                        mnQueryResultId = SDbConsts.READ_ERROR;
                        break;
                    }
                    * */

                    // Read daily stock:

                    stockDay = new SDbStockDay();
                    if (resultSet.getInt("sd.id_year") > 0 && resultSet.getInt("sd.id_item") > 0 && resultSet.getInt("sd.id_unit") > 0 &&
                        resultSet.getInt("sd.id_co") > 0 && resultSet.getInt("sd.id_cob") > 0 && resultSet.getInt("sd.id_wah") > 0 &&
                        resultSet.getInt("sd.id_day") > 0) {

                        stockDay.read(session, new int[] {
                            resultSet.getInt("sd.id_year"),
                            resultSet.getInt("sd.id_item"),
                            resultSet.getInt("sd.id_unit"),
                            resultSet.getInt("sd.id_co"),
                            resultSet.getInt("sd.id_cob"),
                            resultSet.getInt("sd.id_wah"),
                            resultSet.getInt("sd.id_day") });

                        if (stockDay.getQueryResultId() == SDbConsts.READ_OK) {

                            /*stockDay.setFkItemMfgByproductId_n(productionEmpty.getXtaFkBPItemId());
                            stockDay.setFkItemMfgCullId_n(productionEmpty.getXtaFkCUItemId());
                            stockDay.setFkItemConsumptionRawMaterial(productionEmpty.getXtaFkRMItemId());*/
                        }
                    }

                    productionEmpty.setStockDay(stockDay);
                    mvProductionEmpties.add(productionEmpty);
                    mnQueryResultId = SDbConsts.READ_OK;
                }

                if (mnQueryResultId == SDbConsts.READ_OK) {

                    sql = "SELECT s.id_co, s.id_cob, s.id_wah, s.id_item, s.id_unit, " +
                        "SUM(s.mov_in - s.mov_out) - " +
                        "COALESCE((SELECT SUM(ss.mov_in - ss.mov_out) " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS ss " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " AS si ON " +
                        "ss.fk_iog = si.id_iog AND si.b_sys = 1 " +
                        "WHERE ss.b_del = 0 AND ss.b_sys = 1 AND s.id_co = ss.id_co AND s.id_cob = ss.id_cob AND s.id_wah = ss.id_wah AND s.id_item = ss.id_item AND s.id_unit = ss.id_unit AND " +
                        "ss.fk_iog_ct = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0] + " AND ss.fk_iog_cl = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1] + " AND " +
                        "ss.fk_iog_tp = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2] + " AND ss.dt = DATE_SUB('" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                        "INTERVAL 1 DAY)), 0) AS f_stock, wah.code, wah.name, wah.dim_heig, wah.cap_real_lt, i.name, i.code, i.den, u.code " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " +
                        "s.id_co = wah.id_co AND s.id_cob = wah.id_cob AND s.id_wah = wah.id_wah AND wah.fk_wah_tp = " + SModSysConsts.CS_WAH_TP_TAN + " " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_DIV) + " AS d ON " +
                        "s.id_div = d.id_div " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON " +
                        "s.id_item = i.id_item AND i.fk_item_tp = " + SModSysConsts.SS_ITEM_TP_FG + " " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_UNIT) + " AS u ON " +
                        "s.id_unit = u.id_unit " +
                        "WHERE s.b_del = 0 AND s.id_year = " + SLibTimeUtils.digestYear(mtDate)[0] + " AND s.dt < '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' " +
                        "GROUP BY s.id_co, s.id_cob, s.id_wah, s.id_item, s.id_unit " +
                        "HAVING f_stock <> 0 " +
                        "ORDER BY wah.code, wah.name, i.name, i.code, u.code ";

                    mvProductionInventories.clear();
                    resultSet = session.getStatement().executeQuery(sql);
                    while (resultSet.next()) {
                        productionInventory = new SRowProductionInventory();

                        productionInventory.setPkWarehouseCompanyId(resultSet.getInt("s.id_co"));
                        productionInventory.setPkWarehouseBranchId(resultSet.getInt("s.id_cob"));
                        productionInventory.setPkWarehouseWarehouseId(resultSet.getInt("s.id_wah"));
                        productionInventory.setPkItemId(resultSet.getInt("s.id_item"));
                        productionInventory.setPkUnitId(resultSet.getInt("s.id_unit"));
                        productionInventory.setStock(resultSet.getDouble("f_stock"));

                        productionInventory.setXtaWarehouse(resultSet.getString("wah.name"));
                        productionInventory.setXtaWarehouseCode(resultSet.getString("wah.code"));
                        productionInventory.setXtaWarehouseHeight(resultSet.getDouble("wah.dim_heig"));
                        productionInventory.setXtaCapacityRealLiter(resultSet.getDouble("wah.cap_real_lt"));
                        productionInventory.setXtaItem(resultSet.getString("i.name"));
                        productionInventory.setXtaItemCode(resultSet.getString("i.code"));
                        productionInventory.setXtaItemDensity(resultSet.getDouble("i.den"));
                        productionInventory.setXtaUnitCode(resultSet.getString("u.code"));

                        mvProductionInventories.add(productionInventory);
                    }

                    mnQueryResultId = SDbConsts.READ_OK;
                }
            //}
        //}
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        String sql = "";
        SDbIog iog = null;
        SDbStockDay stockDay = null;

        mnQueryResultId = SDbConsts.SAVE_ERROR;

        // Delete previous registries FG:

        mnFkUserUpdateId = session.getUser().getPkUserId();
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " "
            + "SET b_del = 1, fk_usr_upd = " + mnFkUserUpdateId + ", ts_usr_upd = NOW() "
            + "WHERE b_del = 0 AND dt = DATE_SUB('" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', INTERVAL 1 DAY) AND "
            + "b_sys = 1 AND "
            + "fk_iog_ct = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0] + " AND "
            + "fk_iog_cl = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1] + " AND "
            + "fk_iog_tp = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2] + "; ";
        session.getStatement().execute(sql);

        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s, s_iog AS i "
            + "SET s.b_del = 1, s.fk_usr_upd = " + mnFkUserUpdateId + ", s.ts_usr_upd = NOW() "
            + "WHERE s.b_del = 0 AND s.fk_iog = i.id_iog AND s.dt = DATE_SUB('" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', INTERVAL 1 DAY) AND "
            + "s.b_sys = 1 AND "
            + "s.fk_iog_ct = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0] + " AND "
            + "s.fk_iog_cl = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1] + " AND "
            + "s.fk_iog_tp = " + SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2] + "; ";
        session.getStatement().execute(sql);

        // Delete previous registries RM:

        mnFkUserUpdateId = session.getUser().getPkUserId();
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " "
            + "SET b_del = 1, fk_usr_upd = " + mnFkUserUpdateId + ", ts_usr_upd = NOW() "
            + "WHERE b_del = 0 AND dt = DATE_SUB('" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', INTERVAL 1 DAY) AND "
            + "b_sys = 1 AND "
            + "fk_iog_ct = " + SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[0] + " AND "
            + "fk_iog_cl = " + SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[1] + " AND "
            + "fk_iog_tp = " + SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[2] + "; ";
        session.getStatement().execute(sql);

        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.S_STK) + " AS s, s_iog AS i "
            + "SET s.b_del = 1, s.fk_usr_upd = " + mnFkUserUpdateId + ", s.ts_usr_upd = NOW() "
            + "WHERE s.b_del = 0 AND s.fk_iog = i.id_iog AND s.dt = DATE_SUB('" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', INTERVAL 1 DAY) AND "
            + "s.b_sys = 1 AND "
            + "s.fk_iog_ct = " + SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[0] + " AND "
            + "s.fk_iog_cl = " + SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[1] + " AND "
            + "s.fk_iog_tp = " + SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[2] + "; ";
        session.getStatement().execute(sql);

        for (SRowProductionInventory productionInventory : mvProductionInventories) {

            if (!productionInventory.getXtaStockDifferenceSkipped()) {

                if (productionInventory.getXtaEstimateProductionFG() > 0) {

                    // Create move inventory for FG:

                    iog = new SDbIog();
                    iog.setDate(SLibTimeUtils.createDate(SLibTimeUtils.digestDate(mtDate)[0], SLibTimeUtils.digestDate(mtDate)[1], SLibTimeUtils.digestDate(mtDate)[2]-1)); // XXX Validate with end of month
                    iog.setQuantity(productionInventory.getXtaEstimateProductionFG());
                    iog.setSystem(true);
                    iog.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0]);
                    iog.setFkIogClassId(SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1]);
                    iog.setFkIogTypeId(SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2]);
                    iog.setFkIogAdjustmentTypeId(SModSysConsts.SU_IOG_ADJ_TP_NA);
                    iog.setFkItemId(productionInventory.getPkItemId());
                    iog.setFkUnitId(productionInventory.getPkUnitId());
                    iog.setFkWarehouseCompanyId(productionInventory.getPkWarehouseCompanyId());
                    iog.setFkWarehouseBranchId(productionInventory.getPkWarehouseBranchId());
                    iog.setFkWarehouseWarehouseId(productionInventory.getPkWarehouseWarehouseId());
                    // iog.setFkDivisionId(mnFkDivisionId); XXX 2014-10-10 navalos, pending to check.

                    iog.obtainXtaValues(session, SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD);

                    iog.save(session);
                }

                if (productionInventory.getXtaEstimateProductionBP() > 0) {

                    // Create move inventory for BP:

                    iog = new SDbIog();
                    iog.setDate(SLibTimeUtils.createDate(SLibTimeUtils.digestDate(mtDate)[0], SLibTimeUtils.digestDate(mtDate)[1], SLibTimeUtils.digestDate(mtDate)[2]-1)); // XXX Validate with end of month
                    iog.setQuantity(productionInventory.getXtaEstimateProductionBP());
                    iog.setSystem(true);
                    iog.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[0]);
                    iog.setFkIogClassId(SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[1]);
                    iog.setFkIogTypeId(SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD[2]);
                    iog.setFkIogAdjustmentTypeId(SModSysConsts.SU_IOG_ADJ_TP_NA);
                    iog.setFkItemId(productionInventory.getXtaFkItemByProduct());
                    iog.setFkUnitId(productionInventory.getXtaFkUnitByProduct());
                    iog.setFkWarehouseCompanyId(productionInventory.getXtaFkWarehouseCompanyByProduct());
                    iog.setFkWarehouseBranchId(productionInventory.getXtaFkWarehouseBranchByProduct());
                    iog.setFkWarehouseWarehouseId(productionInventory.getXtaFkWarehouseWarehouseByProduct());
                    // iog.setFkDivisionId(mnFkDivisionId); XXX 2014-10-10 navalos, pending to check.

                    iog.obtainXtaValues(session, SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD);

                    iog.save(session);
                }

                if (productionInventory.getXtaEstimateConsumptionRM() > 0) {

                    // Create move inventory for RM:

                    iog = new SDbIog();
                    iog.setDate(SLibTimeUtils.createDate(SLibTimeUtils.digestDate(mtDate)[0], SLibTimeUtils.digestDate(mtDate)[1], SLibTimeUtils.digestDate(mtDate)[2]-1)); // XXX Validate with end of month
                    iog.setQuantity(productionInventory.getXtaEstimateConsumptionRM());
                    iog.setSystem(true);
                    iog.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[0]);
                    iog.setFkIogClassId(SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[1]);
                    iog.setFkIogTypeId(SModSysConsts.SS_IOG_TP_OUT_MFG_RM_ASD[2]);
                    iog.setFkIogAdjustmentTypeId(SModSysConsts.SU_IOG_ADJ_TP_NA);
                    iog.setFkItemId(productionInventory.getXtaFkItemRawMaterial());
                    iog.setFkUnitId(productionInventory.getXtaFkUnitRawMaterial());
                    iog.setFkWarehouseCompanyId(productionInventory.getXtaFkWarehouseCompanyRawMaterial());
                    iog.setFkWarehouseBranchId(productionInventory.getXtaFkWarehouseBranchRawMaterial());
                    iog.setFkWarehouseWarehouseId(productionInventory.getXtaFkWarehouseWarehouseRawMaterial());
                    // iog.setFkDivisionId(mnFkDivisionId); XXX 2014-10-10 navalos, pending to check.

                    //iog.obtainXtaValues(session, SModSysConsts.SS_IOG_TP_OUT_MFG_FG_ASD);
                    if (iog.canSave(session)) {
                        iog.save(session);
                    }
                    else {
                        msQueryResult = iog.getQueryResult();
                        mnQueryResultId = SDbConsts.SAVE_ERROR;
                        break;
                    }
                }

                /*
                 * Probably is necessary for next version:
                 *
                if (inventory.getXtaEstimateProductionCU() > 0) {

                    // Create move inventory for WA:

                    iog = new SDbIog();
                    iog.setDate(SLibTimeUtils.createDate(SLibTimeUtils.digestDate(mtDate)[0], SLibTimeUtils.digestDate(mtDate)[1], SLibTimeUtils.digestDate(mtDate)[2]-1)); // XXX Validate with end of month
                    iog.setQuantity(inventory.getXtaEstimateProductionCU());
                    iog.setSystem(true);
                    iog.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_OUT_EXT_ADJ[0]);
                    iog.setFkIogClassId(SModSysConsts.SS_IOG_TP_OUT_EXT_ADJ[1]);
                    iog.setFkIogTypeId(SModSysConsts.SS_IOG_TP_OUT_EXT_ADJ[2]);
                    iog.setFkIogAdjustmentTypeId(SModSysConsts.SU_IOG_ADJ_TP_NA);
                    iog.setFkItemId(inventory.getXtaFkCUItemId());
                    iog.setFkUnitId(inventory.getXtaFkCUItemUnitId());
                    iog.setFkWarehouseCompanyId(inventory.getXtaFkCUWarehouseCompanyId());
                    iog.setFkWarehouseBranchId(inventory.getXtaFkCUWarehouseBranchId());
                    iog.setFkWarehouseWarehouseId(inventory.getXtaFkCUWarehouseWarehouseId());

                    iog.obtainXtaValues(session, SModSysConsts.SS_IOG_TP_IN_MFG_FG_ASD);

                    iog.save(session);
                }
                * */
            }

            stockDay = productionInventory.getXtaStockDay();

            stockDay.setStockDay(productionInventory.getXtaEmptyKg());
            stockDay.setStockSystem(productionInventory.getStock());
            /*stockDay.setStockDifferencePercentage(productionInventory.getXtaMaximumStockDifferencePercentage());
            stockDay.setMfgFinishedGood(productionInventory.getXtaEstimateProductionFG());
            stockDay.setMfgByproduct(productionInventory.getXtaEstimateProductionBP());
            stockDay.setMfgCull(productionInventory.getXtaEstimateProductionCU());
            stockDay.setConsumptionRawMaterial(productionInventory.getXtaEstimateConsumptionRM());*/
            stockDay.setStockDifferenceSkipped(productionInventory.getXtaStockDifferenceSkipped());

            stockDay.save(session);

            mnQueryResultId = SDbConsts.SAVE_OK;
        }
    }

    public boolean canSave(SGuiSession session) throws SQLException, Exception {
        boolean b = false;
        boolean b_del = false;
        boolean b_dis = false;
        String sql = "";

        Statement statement = null;
        ResultSet resultSet = null;

        sql = "SELECT id_co, id_cob, id_wah, code, name, b_dis, b_del, b_stk_dif_skp " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " " +
                "WHERE fk_wah_tp = " + SModSysConsts.CS_WAH_TP_TAN  + " " +
                "ORDER BY id_co, id_cob, id_wah ";

        statement = session.getStatement().getConnection().createStatement();
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {

            b = false;
            for (SRowProductionInventory inventory : mvProductionInventories) {

                if (SLibUtils.compareKeys(
                        new int[] { resultSet.getInt("id_co"), resultSet.getInt("id_cob"), resultSet.getInt("id_wah") },
                        new int[] { inventory.getPkWarehouseCompanyId(), inventory.getPkWarehouseCompanyId(), inventory.getPkWarehouseWarehouseId() } ) &&
                        !inventory.isDeleted()) {
                    b_dis = resultSet.getBoolean("b_dis");
                    b_del = resultSet.getBoolean("b_del");
                    b = true;
                    break;
                }
            }

            if (!b && !resultSet.getBoolean("b_del") && !resultSet.getBoolean("b_dis") && !resultSet.getBoolean("b_stk_dif_skp")) {
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

        for (SDbIog iog : mvCanSaveIogs) {
            if (!iog.canSave(session)) {
                msQueryResult = iog.getQueryResult();
                b = false;
                break;
            }
        }

        return b;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
