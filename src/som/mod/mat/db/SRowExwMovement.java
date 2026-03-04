/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 * External Warehouse Movement for a single item.
 * @author Sergio Flores
 */
public class SRowExwMovement implements SGridRow {
    
    protected int mnMovement;
    protected Date mtMovementDate;
    protected String msMovementType;
    protected String msMovementFolio;
    protected String msAdjustType;
    protected String msExwFacility;
    protected double mdInFlow;
    protected double mdOutFlow;
    protected double mdStock;
    protected String msUnit;
    protected String msUserInsert;
    protected String msUserUpdate;
    protected Date mtUserInsert;
    protected Date mtUserUpdate;
    
    public SRowExwMovement(final int movement, final Date movementDate, final String movementType, final String movementFolio,
            final String adjustType, final String exwFacility, final double inFlow, final double outFlow, final double stock, final String unit) {
        mnMovement = movement;
        mtMovementDate = movementDate;
        msMovementType = movementType;
        msMovementFolio = movementFolio;
        msAdjustType = adjustType;
        msExwFacility = exwFacility;
        mdInFlow = inFlow;
        mdOutFlow = outFlow;
        mdStock = stock;
        msUnit = unit;
        
        setUserInsert("", null);
        setUserUpdate("", null);
    }
    
    public final void setUserInsert(final String user, final Date ts) {
        msUserInsert = user;
        mtUserInsert = ts;
    }

    public final void setUserUpdate(final String user, final Date ts) {
        msUserUpdate = user;
        mtUserUpdate = ts;
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = mnMovement;
                break;
            case 1:
                value = mtMovementDate;
                break;
            case 2:
                value = msMovementType;
                break;
            case 3:
                value = msMovementFolio;
                break;
            case 4:
                value = msAdjustType;
                break;
            case 5:
                value = msExwFacility;
                break;
            case 6:
                value = mdInFlow;
                break;
            case 7:
                value = mdOutFlow;
                break;
            case 8:
                value = mdStock;
                break;
            case 9:
                value = msUnit;
                break;
            case 10:
                value = msUserInsert;
                break;
            case 11:
                value = mtUserInsert;
                break;
            case 12:
                value = msUserUpdate;
                break;
            case 13:
                value = mtUserUpdate;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
