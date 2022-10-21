/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SMobileWarehouseRow implements SGridRow {
    
    protected int[] whsPk;
    protected String whsCode;
    protected String whsName;
    protected boolean inPlant;

    public int[] getWhsPk() {
        return whsPk;
    }

    public void setWhsPk(int[] whsPk) {
        this.whsPk = whsPk;
    }

    public String getWhsCode() {
        return whsCode;
    }

    public void setWhsCode(String whsCode) {
        this.whsCode = whsCode;
    }

    public String getWhsName() {
        return whsName;
    }

    public void setWhsName(String whsName) {
        this.whsName = whsName;
    }

    public boolean isInPlant() {
        return inPlant;
    }

    public void setInPlant(boolean inPlant) {
        this.inPlant = inPlant;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return this.whsPk;
    }

    @Override
    public String getRowCode() {
        return this.whsCode;
    }

    @Override
    public String getRowName() {
        return this.whsName;
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
        return true;
    }

    @Override
    public void setRowEdited(boolean bln) {
        
    }

    @Override
    public Object getRowValueAt(int i) {
        Object value = null;

        switch(i) {
            case 0:
                value = whsCode;
                break;
            case 1:
                value = whsName;
                break;
            case 2:
                value = inPlant;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 2:
                inPlant = (Boolean) value;
                break;
            
            default:
        }
    }
    
}
