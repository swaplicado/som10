/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Isabel Servín
 */
public class SRowMovementCardex implements SGridRow {
    
    protected Date mtDateReference;
    protected Date mtDate;
    protected String msClassMov;
    protected String msNumMov;
    protected String msMatCond;
    protected boolean mbSystem;
    protected int mnIn;
    protected int mnOut;
    protected int mnQtyExt;
    protected String msUnit;
    
    public void setDateReference(Date t) { mtDateReference = t; }
    public void setDate(Date t) { mtDate = t; }
    public void setClassMov(String s) { msClassMov = s; }
    public void setNumMov(String s) { msNumMov = s; }
    public void setMatCond(String s) { msMatCond = s; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setIn(int n) { mnIn = n; }
    public void setOut(int n) { mnOut = n; }
    public void setQtyExt(int n) { mnQtyExt = n; }
    public void setUnit(String s) { msUnit = s; }
    
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
        return "";
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
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch(col) {
            case 0: value = mtDateReference; break;
            case 1: value = mtDate; break;
            case 2: value = msClassMov; break;
            case 3: value = msNumMov; break;
            case 4: value = msMatCond; break;
            case 5: value = mbSystem; break;
            case 6: value = mnIn; break;
            case 7: value = mnOut; break;
            case 8: value = mnQtyExt; break;
            case 9: value = msUnit; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
