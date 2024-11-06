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
public class SRowNoteCardex implements SGridRow {
    
    protected Date mtDate;
    protected String msClassMov;
    protected String msNumMov;
    protected String msNote;
    
    public void setDate(Date t) { mtDate = t; }
    public void setClassMov(String s) { msClassMov = s; }
    public void setNumMov(String s) { msNumMov = s; }
    public void setNote(String s) { msNote = s; }
    
    public String getNote() { return msNote; }
    
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
            case 0: value = mtDate; break;
            case 1: value = msClassMov; break;
            case 2: value = msNumMov; break;
            case 3: value = msNote; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
