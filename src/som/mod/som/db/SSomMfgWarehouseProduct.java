/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomMfgWarehouseProduct implements SGridRow {

    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkCompanyId;
    protected int mnPkBranchId;
    protected int mnPkWarehouseId;
    protected String msItem;
    protected String msItemCode;
    protected String msUnitCode;
    protected String msBranchCode;
    protected String msWarehouseCode;
    protected String msWarehouseTypeCode;
    protected String msProductionLineCode;
    protected String msProductionLine;
    protected double mdQuantity;
    protected double mdQuantityDelivery;
    protected int mnFkWarehouseTypeId;
    protected int mnFkProductionLineId;
    protected int mnFkItemSource1Id_n;
    protected int mnFkItemSource2Id_n;

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setItem(String s) { msItem = s; }
    public void setItemCode(String s) { msItemCode = s; }
    public void setUnitCode(String s) { msUnitCode = s; }
    public void setBranchCode(String s) { msBranchCode = s; }
    public void setWarehouseCode(String s) { msWarehouseCode = s; }
    public void setWarehouseTypeCode(String s) { msWarehouseTypeCode = s; }
    public void setProductionLineCode(String s) { msProductionLineCode = s; }
    public void setProductionLine(String s) { msProductionLine = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setQuantityDelivery(double d) { mdQuantityDelivery = d; }
    public void setFkWarehouseTypeId(int n) { mnFkWarehouseTypeId = n; }
    public void setFkProductionLineId(int n) { mnFkProductionLineId = n; }
    public void setFkItemSource1Id_n(int n) { mnFkItemSource1Id_n = n; }
    public void setFkItemSource2Id_n(int n) { mnFkItemSource2Id_n = n; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public String getItem() { return msItem; }
    public String getItemCode() { return msItemCode; }
    public String getUnitCode() { return msUnitCode; }
    public String getBranchCode() { return msBranchCode; }
    public String getWarehouseCode() { return msWarehouseCode; }
    public String getWarehouseTypeCode() { return msWarehouseTypeCode; }
    public String getProductionLineCode() { return msProductionLineCode; }
    public String getProductionLine() { return msProductionLine; }
    public double getQuantity() { return mdQuantity; }
    public double getQuantityDelivery() { return mdQuantityDelivery; }
    public int getFkWarehouseTypeId() { return mnFkWarehouseTypeId; }
    public int getFkProductionLineId() { return mnFkProductionLineId; }
    public int getFkItemSource1Id_n() { return mnFkItemSource1Id_n; }
    public int getFkItemSource2Id_n() { return mnFkItemSource2Id_n; }

    public SSomMfgWarehouseProduct() {

        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkCompanyId = 0;
        mnPkBranchId = 0;
        mnPkWarehouseId = 0;
        msItem = "";
        msItemCode = "";
        msUnitCode = "";
        msBranchCode = "";
        msWarehouseCode = "";
        msWarehouseTypeCode = "";
        msProductionLineCode = "";
        msProductionLine = "";
        mdQuantity = 0;
        mdQuantityDelivery = 0;
        mnFkWarehouseTypeId = 0;
        mnFkProductionLineId = 0;
        mnFkItemSource1Id_n = 0;
        mnFkItemSource2Id_n = 0;
    }

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
                value = msItem;
                break;
            case 1:
                value = msItemCode;
                break;
            case 2:
                value = msBranchCode;
                break;
            case 3:
                value = msWarehouseCode;
                break;
            case 4:
                value = msWarehouseTypeCode;
                break;
            case 5:
                value = msProductionLineCode;
                break;
            case 6:
                value = mdQuantity;
                break;
            case 7:
                value = msUnitCode;
                break;
            case 8:
                value = mdQuantityDelivery;
                break;
            case 9:
                value = msUnitCode;
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
            case 8:
            case 9:
                break;
            default:
        }
    }

    @Override
    public SSomMfgWarehouseProduct clone() throws CloneNotSupportedException {
        SSomMfgWarehouseProduct registry = new SSomMfgWarehouseProduct();

        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setItem(this.getItem());
        registry.setItemCode(this.getItemCode());
        registry.setUnitCode(this.getUnitCode());
        registry.setBranchCode(this.getBranchCode());
        registry.setWarehouseCode(this.getWarehouseCode());
        registry.setWarehouseTypeCode(this.getWarehouseTypeCode());
        registry.setProductionLineCode(this.getProductionLineCode());
        registry.setProductionLine(this.getProductionLine());
        registry.setQuantity(this.getQuantity());
        registry.setQuantityDelivery(this.getQuantityDelivery());
        registry.setFkWarehouseTypeId(this.getFkWarehouseTypeId());
        registry.setFkProductionLineId(this.getFkProductionLineId());
        registry.setFkItemSource1Id_n(this.getFkItemSource1Id_n());
        registry.setFkItemSource2Id_n(this.getFkItemSource2Id_n());

        return registry;
    }
}
