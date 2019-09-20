/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SRowProductionByLine implements SGridRow {


    protected String msProductionLine;
    protected double mdProduction;
    protected String msUnit;

    public SRowProductionByLine() {
        msProductionLine = "";
        mdProduction = 0;
        msUnit = "";
    }


    public void setProductionLine(String s) { msProductionLine = s; }
    public void setProduction(double d) { mdProduction = d; }
    public void setUnit(String s) { msUnit = s; }

    public String getProductionLine() { return msProductionLine; }
    public double getProduction() { return mdProduction; }
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
                value = msProductionLine;
                break;
            case 1:
                value = mdProduction;
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
