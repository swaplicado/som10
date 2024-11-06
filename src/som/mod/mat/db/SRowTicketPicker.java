/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

import java.util.ArrayList;
import java.util.Date;
import sa.lib.grid.SGridRowCustom;

/**
 *
 * @author Isabel Servín
 */
public class SRowTicketPicker extends SGridRowCustom {
    
    private Object moMainOption;
    private final ArrayList<Object> maValues;
    private Date mdDate;
    private boolean mbHasMov;
    private boolean mbIsPend;

    public SRowTicketPicker(int[] pk) {
        super(pk, "", "");
        moMainOption = null;
        maValues = new ArrayList<>();
    }
    
    public void setMainOption(Object o) { moMainOption = o; }
    public void setDate(Date d) { mdDate = d; }
    public void setHasMov(boolean b) { mbHasMov = b; }
    public void setIsPend(boolean b) { mbIsPend = b; }
    
    public Object getMainOption() { return moMainOption; }
    public Date getDate() { return mdDate; }
    public boolean getHasMov() { return mbHasMov; }
    public boolean getIsPend() { return mbIsPend; }
    
    public ArrayList<Object> getValues() { return maValues; }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        if (col >= 0 && col < maValues.size()) {
            value = maValues.get(col);
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
