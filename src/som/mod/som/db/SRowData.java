/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import sa.lib.grid.SGridRow;

/**
 * Usada para los renglones de otros datos en los resultados de molienda
 * 
 * @author Edwin Carmona
 */
public class SRowData implements SGridRow {

    protected String msData;
    protected double mdValue;
    protected String msUnit;

    public SRowData() {
        msData = "";
        mdValue = 0d;
        msUnit = "";
    }


    public void setData(String s) { msData = s; }
    public void setValueD(double d) { mdValue = d; }
    public void setUnit(String s) { msUnit = s; }

    public String getData() { return msData; }
    public double getValueD() { return mdValue; }
    public String getUnit() { return msUnit; }

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
                value = msData;
                break;
            case 1:
                value = mdValue;
                break;
            case 2:
                value = msUnit;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object o, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
