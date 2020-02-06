/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SRowEvent implements SGridRow {

    protected Date mtStart;
    protected Date mtEnd;
    protected String msDescription;
    protected String msUserInsert;
    protected Date mtDateUserInsert;
    protected String msUserUpdate;
    protected Date mtDateUserUpdate;

    public SRowEvent() {
        mtStart = null;
        mtEnd = null;
        msDescription = "";
        msUserInsert = "";
        mtDateUserInsert = null;
        msUserUpdate = "";
        mtDateUserUpdate = null;
    }

    public void setStartDate(Date t) { mtStart = t; }
    public void setEndDate(Date t) { mtEnd = t; }
    public void setDescription(String s) { msDescription = s; }
    public void setUserInsert(String s) { msUserInsert = s; }
    public void setDateUserInsert(Date t) { mtDateUserInsert = t; }
    public void setUserUpdate(String s) { msUserUpdate = s; }
    public void setDateUserUpdate(Date t) { mtDateUserUpdate = t; }

    public Date getStartDate() { return mtStart; }
    public Date getEndDate() { return mtEnd; }
    public String getDescription() { return msDescription; }
    public String getUserInsert() { return msUserInsert; }
    public Date getDateUserInsert() { return mtDateUserInsert; }
    public String getUserUpdate() { return msUserUpdate; }
    public Date getDateUserUpdate() { return mtDateUserUpdate; }

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
                value = mtStart;
                break;
            case 1:
                value = mtEnd;
                break;
            case 2:
                value = msDescription;
                break;
            case 3:
                value = msUserInsert;
                break;
            case 4:
                value = mtDateUserInsert;
                break;
            case 5:
                value = msUserUpdate;
                break;
            case 6:
                value = mtDateUserUpdate;
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
