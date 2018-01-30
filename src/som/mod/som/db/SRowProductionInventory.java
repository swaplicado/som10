/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 * @author Néstor Ávalos
 */
public class SRowProductionInventory implements SGridRow {

    protected int mnPkWarehouseCompanyId;
    protected int mnPkWarehouseBranchId;
    protected int mnPkWarehouseWarehouseId;
    protected int mnPkDivisionId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected double mdStock;
    protected boolean mbDeleted;
    protected boolean mbProcess;
    protected String msNote;

    protected boolean mbXtaReqAdjustment;
    protected boolean mbXtaEmpty;
    protected boolean mbXtaStockDifferenceSkipped;
    protected double mdXtaEmptiness;
    protected double mdXtaEmptyKg;
    protected double mdXtaCull;
    protected double mdXtaEstimateConsumptionRM;
    protected double mdXtaEstimateProductionFG;
    protected double mdXtaEstimateProductionBP;
    protected double mdXtaEstimateProductionWA;
    protected double mdXtaMaximumStockDifferenceKg;
    protected Date mtXtaDate;
    protected String msXtaWarehouse;
    protected String msXtaWarehouseCode;
    protected double mdXtaWarehouseHeight;
    protected double mdXtaCapacityRealLiter;
    protected String msXtaItem;
    protected String msXtaItemCode;
    protected double mdXtaItemDensity;
    protected String msXtaUnitCode;
    protected int mnXtaFkItemRawMaterial;
    protected int mnXtaFkUnitRawMaterial;
    protected int mnXtaFkWarehouseCompanyRawMaterial;
    protected int mnXtaFkWarehouseBranchRawMaterial;
    protected int mnXtaFkWarehouseWarehouseRawMaterial;
    protected int mnXtaFkItemByProduct;
    protected int mnXtaFkUnitByProduct;
    protected int mnXtaFkWarehouseCompanyByProduct;
    protected int mnXtaFkWarehouseBranchByProduct;
    protected int mnXtaFkWarehouseWarehouseByProduct;
    protected int mnXtaFkItemWaste;
    protected int mnXtaFkUnitWaste;
    protected int mnXtaFkWarehouseCompanyWaste;
    protected int mnXtaFkWarehouseBranchWaste;
    protected int mnXtaFkWarehouseWarehouseWaste;

    protected SDbStockDay moXtaStockDay;

    public SRowProductionInventory() {
        mnPkWarehouseCompanyId = 0;
        mnPkWarehouseBranchId = 0;
        mnPkWarehouseWarehouseId = 0;
        mnPkDivisionId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mdStock = 0;
        mbDeleted = false;
        mbProcess = false;
        msNote = "";

        mbXtaReqAdjustment = false;
        mbXtaEmpty = false;
        mbXtaStockDifferenceSkipped = false;
        mdXtaEmptiness = 0;
        mdXtaEmptyKg = 0;
        mdXtaCull = 0;
        mdXtaEstimateConsumptionRM = 0;
        mdXtaEstimateProductionFG = 0;
        mdXtaEstimateProductionBP = 0;
        mdXtaEstimateProductionWA = 0;
        mdXtaMaximumStockDifferenceKg = 0;
        mtXtaDate = null;
        msXtaWarehouse = "";
        msXtaWarehouseCode = "";
        mdXtaWarehouseHeight = 0;
        mdXtaCapacityRealLiter = 0;
        msXtaItem = "";
        msXtaItemCode = "";
        mdXtaItemDensity = 0;
        msXtaUnitCode = "";
        mnXtaFkItemRawMaterial = 0;
        mnXtaFkUnitRawMaterial = 0;
        mnXtaFkWarehouseCompanyRawMaterial = 0;
        mnXtaFkWarehouseBranchRawMaterial = 0;
        mnXtaFkWarehouseWarehouseRawMaterial = 0;
        mnXtaFkItemByProduct = 0;
        mnXtaFkUnitByProduct = 0;
        mnXtaFkWarehouseCompanyByProduct = 0;
        mnXtaFkWarehouseBranchByProduct = 0;
        mnXtaFkWarehouseWarehouseByProduct = 0;
        mnXtaFkItemWaste = 0;
        mnXtaFkUnitWaste = 0;
        mnXtaFkWarehouseCompanyWaste = 0;
        mnXtaFkWarehouseBranchWaste = 0;
        mnXtaFkWarehouseWarehouseWaste = 0;

        moXtaStockDay = null;
    }

    public void setPkWarehouseCompanyId(int n) { mnPkWarehouseCompanyId = n; }
    public void setPkWarehouseBranchId(int n) { mnPkWarehouseBranchId = n; }
    public void setPkWarehouseWarehouseId(int n) { mnPkWarehouseWarehouseId = n; }
    public void setPkDivisionId(int n) { mnPkDivisionId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setStock(double d) { mdStock = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setProcess(boolean b) { mbProcess = b; }
    public void setNote(String s) { msNote = s; }

    public int getPkWarehouseCompanyId() { return mnPkWarehouseCompanyId; }
    public int getPkWarehouseBranchId() { return mnPkWarehouseBranchId; }
    public int getPkWarehouseWarehouseId() { return mnPkWarehouseWarehouseId; }
    public int getPkDivisionId() { return mnPkDivisionId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public double getStock() { return mdStock; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean getProcess() { return mbProcess; }
    public String getNote() { return msNote; }

    public void setXtaReqAdjustment(boolean b) { mbXtaReqAdjustment = b; }
    public void setXtaEmpty(boolean b) { mbXtaEmpty = b; }
    public void setXtaOmit(boolean b) { mbXtaStockDifferenceSkipped = b; }
    public void setXtaEmptiness(double d) { mdXtaEmptiness = d; }
    public void setXtaEmptyKg(double d) { mdXtaEmptyKg = d; }
    public void setXtaCull(double d) { mdXtaCull = d; }
    public void setXtaEstimateConsumptionRM(double d) { mdXtaEstimateConsumptionRM = d; }
    public void setXtaEstimateProductionFG(double d) { mdXtaEstimateProductionFG = d; }
    public void setXtaEstimateProductionBP(double d) { mdXtaEstimateProductionBP = d; }
    public void setXtaEstimateProductionWA(double d) { mdXtaEstimateProductionWA = d; }
    public void setXtaMaximumStockDifferenceKg(double d) { mdXtaMaximumStockDifferenceKg = d; }
    public void setXtaDate(Date t) { mtXtaDate = t; }
    public void setXtaWarehouse(String s) { msXtaWarehouse = s; }
    public void setXtaWarehouseCode(String s) { msXtaWarehouseCode = s; }
    public void setXtaWarehouseHeight(double d) { mdXtaWarehouseHeight = d; }
    public void setXtaCapacityRealLiter(double d) { mdXtaCapacityRealLiter = d; }
    public void setXtaItem(String s) { msXtaItem = s; }
    public void setXtaItemCode(String s) { msXtaItemCode = s; }
    public void setXtaItemDensity(double d) { mdXtaItemDensity = d; }
    public void setXtaUnitCode(String s) { msXtaUnitCode = s; }
    public void setXtaFkItemRawMaterial(int n) { mnXtaFkItemRawMaterial = n; }
    public void setXtaFkUnitRawMaterial(int n) { mnXtaFkUnitRawMaterial = n; }
    public void setXtaFkWarehouseCompanyRawMaterial(int n) { mnXtaFkWarehouseCompanyRawMaterial = n; }
    public void setXtaFkWarehouseBranchRawMaterial(int n) { mnXtaFkWarehouseBranchRawMaterial = n; }
    public void setXtaFkWarehouseWarehouseRawMaterial(int n) { mnXtaFkWarehouseWarehouseRawMaterial = n; }
    public void setXtaFkItemByProduct(int n) { mnXtaFkItemByProduct = n; }
    public void setXtaFkUnitByProduct(int n) { mnXtaFkUnitByProduct = n; }
    public void setXtaFkWarehouseCompanyByProduct(int n) { mnXtaFkWarehouseCompanyByProduct = n; }
    public void setXtaFkWarehouseBranchByProduct(int n) { mnXtaFkWarehouseBranchByProduct = n; }
    public void setXtaFkWarehouseWarehouseByProduct(int n) { mnXtaFkWarehouseWarehouseByProduct = n; }
    public void setXtaFkItemCull(int n) { mnXtaFkItemWaste = n; }
    public void setXtaFkUnitCull(int n) { mnXtaFkUnitWaste = n; }
    public void setXtaFkWarehouseCompanyCull(int n) { mnXtaFkWarehouseCompanyWaste = n; }
    public void setXtaFkWarehouseBranchCull(int n) { mnXtaFkWarehouseBranchWaste = n; }
    public void setXtaFkWarehouseWarehouseCull(int n) { mnXtaFkWarehouseWarehouseWaste = n; }

    public boolean getXtaReqAdjustment() { return mbXtaReqAdjustment; }
    public boolean getXtaEmpty() { return mbXtaEmpty; }
    public boolean getXtaStockDifferenceSkipped() { return mbXtaStockDifferenceSkipped; }
    public double getXtaEmptiness() { return mdXtaEmptiness; }
    public double getXtaEmptyKg() { return mdXtaEmptyKg; }
    public double getXtaCull() { return mdXtaCull; }
    public double getXtaEstimateConsumptionRM() { return mdXtaEstimateConsumptionRM; }
    public double getXtaEstimateProductionFG() { return mdXtaEstimateProductionFG; }
    public double getXtaEstimateProductionBP() { return mdXtaEstimateProductionBP; }
    public double getXtaEstimateProductionCU() { return mdXtaEstimateProductionWA; }
    public double getXtaMaximumStockDifferenceKg() { return mdXtaMaximumStockDifferenceKg; }
    public Date getXtaDate() { return mtXtaDate; }
    public String getXtaWarehouse() { return msXtaWarehouse; }
    public String getXtaWarehouseCode() { return msXtaWarehouseCode; }
    public double getXtaWarehouseHeight() { return mdXtaWarehouseHeight; }
    public double getXtaCapacityRealLiter() { return mdXtaCapacityRealLiter; }
    public String getXtaItem() { return msXtaItem; }
    public String getXtaItemCode() { return msXtaItemCode; }
    public double getXtaItemDensity() { return mdXtaItemDensity; }
    public String getXtaUnitCode() { return msXtaUnitCode; }
    public int getXtaFkItemRawMaterial() { return mnXtaFkItemRawMaterial; }
    public int getXtaFkUnitRawMaterial() { return mnXtaFkUnitRawMaterial; }
    public int getXtaFkWarehouseCompanyRawMaterial() { return mnXtaFkWarehouseCompanyRawMaterial; }
    public int getXtaFkWarehouseBranchRawMaterial() { return mnXtaFkWarehouseBranchRawMaterial; }
    public int getXtaFkWarehouseWarehouseRawMaterial() { return mnXtaFkWarehouseWarehouseRawMaterial; }
    public int getXtaFkItemByProduct() { return mnXtaFkItemByProduct; }
    public int getXtaFkUnitByProduct() { return mnXtaFkUnitByProduct; }
    public int getXtaFkWarehouseCompanyByProduct() { return mnXtaFkWarehouseCompanyByProduct; }
    public int getXtaFkWarehouseBranchByProduct() { return mnXtaFkWarehouseBranchByProduct; }
    public int getXtaFkWarehouseWarehouseByProduct() { return mnXtaFkWarehouseWarehouseByProduct; }
    public int getXtaFkItemCull() { return mnXtaFkItemWaste; }
    public int getXtaFkUnitWaste() { return mnXtaFkUnitWaste; }
    public int getXtaFkWarehouseCompanyWaste() { return mnXtaFkWarehouseCompanyWaste; }
    public int getXtaFkWarehouseBranchWaste() { return mnXtaFkWarehouseBranchWaste; }
    public int getXtaFkWarehouseWarehouseWaste() { return mnXtaFkWarehouseWarehouseWaste; }

    public void setXtaStockDay(SDbStockDay o) { moXtaStockDay = o; }

    public SDbStockDay getXtaStockDay() { return moXtaStockDay; }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(final boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msXtaWarehouseCode;
                break;
            case 1:
                value = mbXtaEmpty;
                break;
            case 2:
                value = mdXtaEmptiness;
                break;
            case 3:
                value = mdXtaCull;
                break;
            case 4:
                value = msXtaItem;
                break;
            case 5:
                value = msXtaItemCode;
                break;
            case 6:
                value = mdXtaEmptyKg;
                break;
            case 7:
                value = mdStock;
                break;
            /*
            case 8:
                value = mdXtaEstimateProductionFG;
                break;
            case 9:
                value = mdXtaEstimateProductionBP;
                break;
            case 10:
                value = mdXtaEstimateProductionWA;
                break;
            case 11:
                value = mdXtaEstimateConsumptionRM;
                break;
            */
            case 8:
                value = msXtaUnitCode;
                break;
            case 9:
                value = mdXtaMaximumStockDifferenceKg;
                break;
            case 10:
                value = mbXtaStockDifferenceSkipped;
                break;
            case 11:
                value = msNote;
                break;
            case 12:
                value = mdXtaWarehouseHeight;
                break;
            case 13:
                value = mdXtaCapacityRealLiter;
                break;
            case 14:
                value = mdXtaItemDensity;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            /*
            case 8:
            case 9:
            case 10:
            case 11:
            */
            case 8:
            case 9:
                break;
            case 10:
                mbXtaStockDifferenceSkipped = (Boolean) value;
                break;
            case 11:
            case 12:
            case 13:
            case 14:
                break;
            default:
        }
    }
}
