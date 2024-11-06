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
public class SRowReferencePicker implements SGridRow {
    
    protected Date mtDate;
    protected String msMovementClass;
    protected Date mtReference;
    protected String msUsrNew;
    protected String msUsrEdit;
    protected Date mtTsUsrNew;
    protected Date mtTsUsrEdit;
    
    public void setDate(Date t) { mtDate = t; }
    public void setMovementClass(String s) { msMovementClass = s; }
    public void setReference(Date t) { mtReference = t; }
    public void setUsrNew(String s) { msUsrNew = s; }
    public void setUsrEdit(String s) { msUsrEdit = s; }
    public void setTsUsrNew(Date t) { mtTsUsrNew = t; }
    public void setTsUsrEdit(Date t) { mtTsUsrEdit = t; }
    
    public Date getReference() { return mtReference; }

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
            case 0: value = mtReference; break;
            case 1: value = msMovementClass; break;
            case 2: value = mtDate; break;
            case 3: value = msUsrNew; break;
            case 4: value = mtTsUsrNew; break;
            case 5: value = msUsrEdit; break;
            case 6: value = mtTsUsrEdit; break;
        } 
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
