/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomProductionEmpty {

    protected int mnPkWarehouseCompanyId;
    protected int mnPkWarehouseBranchId;
    protected int mnPkWarehouseWarehouseId;

    protected int mnXtaPkItemId;
    protected int mnXtaPkUnitId;
    protected boolean mbXtaEmpty;
    protected boolean mbXtaStockDifferenceSkipped;
    protected double mdXtaEmptiness;
    protected double mdXtaEmptyKg;
    protected double mdXtaCull;
    protected Date mtXtaDate;
    protected String msXtaWarehouse;
    protected String msXtaWarehouseCode;
    protected double mdXtaWarehouseHeight;
    protected double mdXtaCapacityRealLiter;
    protected String msXtaFGItem;
    protected String msXtaFGItemCode;
    protected double mdXtaFGItemDensity;
    protected String msXtaFGItemUnitCode;
    protected String msXtaRMItem;
    protected String msXtaRMItemCode;
    protected String msXtaRMItemUnitCode;
    protected String msXtaBPItem;
    protected String msXtaBPItemCode;
    protected String msXtaBPItemUnitCode;
    protected String msXtaCUItem;
    protected String msXtaCUItemCode;
    protected String msXtaCUItemUnitCode;
    protected double mdXtaFGPercentage;
    protected double mdXtaBPPercentage;
    protected double mdXtaCUPercentage;
    protected int mnXtaFkRMItemId;
    protected int mnXtaFkRMItemUnitId;
    protected int mnXtaFkRMWarehouseCompanyId;
    protected int mnXtaFkRMWarehouseBranchId;
    protected int mnXtaFkRMWarehouseWarehouseId;
    protected int mnXtaFkBPItemId;
    protected int mnXtaFkBPItemUnitId;
    protected int mnXtaFkBPWarehouseCompanyId;
    protected int mnXtaFkBPWarehouseBranchId;
    protected int mnXtaFkBPWarehouseWarehouseId;
    protected int mnXtaFkCUItemId;
    protected int mnXtaFkCUItemUnitId;
    protected int mnXtaFkCUWarehouseCompanyId;
    protected int mnXtaFkCUWarehouseBranchId;
    protected int mnXtaFkCUWarehouseWarehouseId;

    protected SDbStockDay moStockDay;

    public SSomProductionEmpty() {
        mnPkWarehouseCompanyId = 0;
        mnPkWarehouseBranchId = 0;
        mnPkWarehouseWarehouseId = 0;

        mnXtaPkItemId = 0;
        mnXtaPkUnitId = 0;
        mbXtaEmpty = false;
        mbXtaStockDifferenceSkipped = false;
        mdXtaEmptiness = 0;
        mdXtaEmptyKg = 0;
        mdXtaCull = 0;
        mtXtaDate = null;
        msXtaWarehouse = "";
        msXtaWarehouseCode = "";
        mdXtaWarehouseHeight = 0;
        mdXtaCapacityRealLiter = 0;
        msXtaFGItem = "";
        msXtaFGItemCode = "";
        mdXtaFGItemDensity = 0;
        msXtaFGItemUnitCode = "";
        mdXtaFGPercentage = 0;
        mdXtaBPPercentage = 0;
        mdXtaCUPercentage = 0;
        mnXtaFkRMItemId = 0;
        mnXtaFkRMItemUnitId = 0;
        mnXtaFkRMWarehouseCompanyId = 0;
        mnXtaFkRMWarehouseBranchId = 0;
        mnXtaFkRMWarehouseWarehouseId = 0;
        mnXtaFkBPItemId = 0;
        mnXtaFkBPItemUnitId = 0;
        mnXtaFkBPWarehouseCompanyId = 0;
        mnXtaFkBPWarehouseBranchId = 0;
        mnXtaFkBPWarehouseWarehouseId = 0;
        mnXtaFkCUItemId = 0;
        mnXtaFkCUItemUnitId = 0;
        mnXtaFkCUWarehouseCompanyId = 0;
        mnXtaFkCUWarehouseBranchId = 0;
        mnXtaFkCUWarehouseWarehouseId = 0;

        moStockDay = null;
    }

    public void setPkWarehouseCompanyId(int n) { mnPkWarehouseCompanyId = n; }
    public void setPkWarehouseBranchId(int n) { mnPkWarehouseBranchId = n; }
    public void setPkWarehouseWarehouseId(int n) { mnPkWarehouseWarehouseId = n; }

    public int getPkWarehouseCompanyId() { return mnPkWarehouseCompanyId; }
    public int getPkWarehouseBranchId() { return mnPkWarehouseBranchId; }
    public int getPkWarehouseWarehouseId() { return mnPkWarehouseWarehouseId; }

    public void setXtaPkItemId(int n) { mnXtaPkItemId = n; }
    public void setXtaPkUnitId(int n) { mnXtaPkUnitId = n; }
    public void setXtaEmpty(boolean b) { mbXtaEmpty = b; }
    public void setXtaStockDifferenceSkipped(boolean b) { mbXtaStockDifferenceSkipped = b; }
    public void setXtaEmptiness(double d) { mdXtaEmptiness = d; }
    public void setXtaEmptyKg(double d) { mdXtaEmptyKg = d; }
    public void setXtaCull(double d) { mdXtaCull = d; }
    public void setXtaDate(Date t) { mtXtaDate = t; }
    public void setXtaWarehouse(String s) { msXtaWarehouse = s; }
    public void setXtaWarehouseCode(String s) { msXtaWarehouseCode = s; }
    public void setXtaWarehouseHeight(double d) { mdXtaWarehouseHeight = d; }
    public void setXtaCapacityRealLiter(double d) { mdXtaCapacityRealLiter = d; }
    public void setXtaFGItem(String s) { msXtaFGItem = s; }
    public void setXtaFGItemCode(String s) { msXtaFGItemCode = s; }
    public void setXtaFGItemUnitCode(String s) { msXtaFGItemUnitCode = s; }
    public void setXtaFGItemDensity(double d) { mdXtaFGItemDensity = d; }
    public void setXtaRMItem(String s) { msXtaRMItem = s; }
    public void setXtaRMItemCode(String s) { msXtaRMItemCode = s; }
    public void setXtaRMItemUnitCode(String s) { msXtaRMItemUnitCode = s; }
    public void setXtaBPItem(String s) { msXtaBPItem = s; }
    public void setXtaBPItemCode(String s) { msXtaBPItemCode = s; }
    public void setXtaBPItemUnitCode(String s) { msXtaBPItemUnitCode = s; }
    public void setXtaCUItem(String s) { msXtaCUItem = s; }
    public void setXtaCUItemCode(String s) { msXtaCUItemCode = s; }
    public void setXtaCUItemUnitCode(String s) { msXtaCUItemUnitCode = s; }
    public void setXtaFGPercentage(double d) { mdXtaFGPercentage = d; }
    public void setXtaBPPercentage(double d) { mdXtaBPPercentage = d; }
    public void setXtaCUPercentage(double d) { mdXtaCUPercentage = d; }
    public void setXtaFkRMItemId(int n) { mnXtaFkRMItemId = n; }
    public void setXtaFkRMItemUnitId(int n) { mnXtaFkRMItemUnitId = n; }
    public void setXtaFkRMWarehouseCompanyId(int n) { mnXtaFkRMWarehouseCompanyId = n; }
    public void setXtaFkRMWarehouseBranchId(int n) { mnXtaFkRMWarehouseBranchId = n; }
    public void setXtaFkRMWarehouseWarehouseId(int n) { mnXtaFkRMWarehouseWarehouseId = n; }
    public void setXtaFkBPItemId(int n) { mnXtaFkBPItemId = n; }
    public void setXtaFkBPItemUnitId(int n) { mnXtaFkBPItemUnitId = n; }
    public void setXtaFkBPWarehouseCompanyId(int n) { mnXtaFkBPWarehouseCompanyId = n; }
    public void setXtaFkBPWarehouseBranchId(int n) { mnXtaFkBPWarehouseBranchId = n; }
    public void setXtaFkBPWarehouseWarehouseId(int n) { mnXtaFkBPWarehouseWarehouseId = n; }
    public void setXtaFkCUItemId(int n) { mnXtaFkCUItemId = n; }
    public void setXtaFkCUItemUnitId(int n) { mnXtaFkCUItemUnitId = n; }
    public void setXtaFkCUWarehouseCompanyId(int n) { mnXtaFkCUWarehouseCompanyId = n; }
    public void setXtaFkCUWarehouseBranchId(int n) { mnXtaFkCUWarehouseBranchId = n; }
    public void setXtaFkCUWarehouseWarehouseId(int n) { mnXtaFkCUWarehouseWarehouseId = n; }

    public int getXtaPkItemId() { return mnXtaPkItemId; }
    public int getXtaPkUnitId() { return mnXtaPkUnitId; }
    public boolean getXtaEmpty() { return mbXtaEmpty; }
    public boolean getXtaStockDifferenceSkipped() { return mbXtaStockDifferenceSkipped; }
    public double getXtaEmptiness() { return mdXtaEmptiness; }
    public double getXtaEmptyKg() { return mdXtaEmptyKg; }
    public double getXtaCull() { return mdXtaCull; }
    public Date getXtaDate() { return mtXtaDate; }
    public String getXtaWarehouse() { return msXtaWarehouse; }
    public String getXtaWarehouseCode() { return msXtaWarehouseCode; }
    public double getXtaWarehouseHeight() { return mdXtaWarehouseHeight; }
    public double getXtaCapacityRealLiter() { return mdXtaCapacityRealLiter; }
    public String getXtaFGItem() { return msXtaFGItem; }
    public String getXtaFGItemCode() { return msXtaFGItemCode; }
    public String getXtaFGItemUnitCode() { return msXtaFGItemUnitCode; }
    public double getXtaFGItemDensity() { return mdXtaFGItemDensity; }
    public String getXtaRMItem() { return msXtaRMItem; }
    public String getXtaRMItemCode() { return msXtaRMItemCode; }
    public String getXtaRMItemUnitCode() { return msXtaRMItemUnitCode; }
    public String getXtaBPItem() { return msXtaBPItem; }
    public String getXtaBPItemCode() { return msXtaBPItemCode; }
    public String getXtaBPItemUnitCode() { return msXtaBPItemUnitCode; }
    public String getXtaCUItem() { return msXtaCUItem; }
    public String getXtaCUItemCode() { return msXtaCUItemCode; }
    public String getXtaCUItemUnitCode() { return msXtaCUItemUnitCode; }
    public double getXtaFGPercentage() { return mdXtaFGPercentage; }
    public double getXtaBPPercentage() { return mdXtaBPPercentage; }
    public double getXtaCUPercentage() { return mdXtaCUPercentage; }
    public int getXtaFkRMItemId() { return mnXtaFkRMItemId; }
    public int getXtaFkRMItemUnitId() { return mnXtaFkRMItemUnitId; }
    public int getXtaFkRMWarehouseCompanyId() { return mnXtaFkRMWarehouseCompanyId; }
    public int getXtaFkRMWarehouseBranchId() { return mnXtaFkRMWarehouseBranchId; }
    public int getXtaFkRMWarehouseWarehouseId() { return mnXtaFkRMWarehouseWarehouseId; }
    public int getXtaFkBPItemId() { return mnXtaFkBPItemId; }
    public int getXtaFkBPItemUnitId() { return mnXtaFkBPItemUnitId; }
    public int getXtaFkBPWarehouseCompanyId() { return mnXtaFkBPWarehouseCompanyId; }
    public int getXtaFkBPWarehouseBranchId() { return mnXtaFkBPWarehouseBranchId; }
    public int getXtaFkBPWarehouseWarehouseId() { return mnXtaFkBPWarehouseWarehouseId; }
    public int getXtaFkCUItemId() { return mnXtaFkCUItemId; }
    public int getXtaFkCUItemUnitId() { return mnXtaFkCUItemUnitId; }
    public int getXtaFkCUWarehouseCompanyId() { return mnXtaFkCUWarehouseCompanyId; }
    public int getXtaFkCUWarehouseBranchId() { return mnXtaFkCUWarehouseBranchId; }
    public int getXtaFkCUWarehouseWarehouseId() { return mnXtaFkCUWarehouseWarehouseId; }

    public void setStockDay(SDbStockDay o) { moStockDay = o; }

    public SDbStockDay getStockDay() { return moStockDay; }
}
