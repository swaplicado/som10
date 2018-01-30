/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.util.Date;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomProductionEstimateMovesVersion implements SGridRow {

    protected int mnPkIogId;
    protected int mnNumber;
    protected Date mtDate;
    protected double mdQuantity;
    protected String msIogType;
    protected String msItem;
    protected String msItemCode;
    protected String msUnitCode;
    protected String msWarehouse;
    protected int mnFkMfgEstimationId_n;
    protected int mnFkMfgEstimationVersionId_n;
    protected boolean mbDeleted;
    protected boolean mbSystem;

    public void setPkIogId(int n) { mnPkIogId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setIogType(String s) { msIogType = s; }
    public void setItem(String s) { msItem = s; }
    public void setItemCode(String s) { msItemCode = s; }
    public void setUnitCode(String s) { msUnitCode = s; }
    public void setWarehouse(String s) { msWarehouse = s; }
    public void setFkMfgEstimationId_n(int n) { mnFkMfgEstimationId_n = n; }
    public void setFkMfgEstimationVersionId_n(int n) { mnFkMfgEstimationVersionId_n = n; }

    public int getPkIogId() { return mnPkIogId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public double getQuantity() { return mdQuantity; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public String getIogType() { return msIogType; }
    public String getItem() { return msItem; }
    public String getItemCode() { return msItemCode; }
    public String getUnitCode() { return msUnitCode; }
    public String getWarehouse() { return msWarehouse; }
    public int getFkMfgEstimationId_n() { return mnFkMfgEstimationId_n; }
    public int getFkMfgEstimationVersionId_n() { return mnFkMfgEstimationVersionId_n; }

    public SSomProductionEstimateMovesVersion() {
        mnPkIogId = 0;
        mnNumber = 0;
        mtDate = null;
        mdQuantity = 0;
        msIogType = "";
        msItem = "";
        msItemCode = "";
        msUnitCode = "";
        msWarehouse = "";
        mnFkMfgEstimationId_n = 0;
        mnFkMfgEstimationVersionId_n = 0;
        mbDeleted = false;
        mbSystem = false;
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
                value = mnFkMfgEstimationId_n;
                break;
            case 1:
                value = mnFkMfgEstimationVersionId_n;
                break;
            case 2:
                value = mnNumber;
                break;
            case 3:
                value = mtDate;
                break;
            case 4:
                value = msIogType;
                break;
            case 5:
                value = msWarehouse;
                break;
            case 6:
                value = msItem;
                break;
            case 7:
                value = msItemCode;
                break;
            case 8:
                value = mdQuantity;
                break;
            case 9:
                value = msUnitCode;
                break;
            case 10:
                value = mbDeleted;
                break;
            case 11:
                value = mbSystem;
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
                break;
            default:
        }
    }
}
