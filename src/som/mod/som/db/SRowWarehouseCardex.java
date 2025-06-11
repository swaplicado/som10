/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 * Renglones del grid historico de cálculos ponderados de almacenes 
 * @author Isabel Servín
 */
public class SRowWarehouseCardex implements SGridRow {
    
    private String msItem;
    private Date mtDateStart;
    private Date mtDateEnd;
    private int mnLabTic;
    private int mnNLabTic;
    private int mnAllTic;
    private double mdLabWei;
    private double mdNLabWei;
    private double mdAllWei;
    private double mdPondOil;
    private double mdPondMoi;
    
    public void setItem(String s) { msItem = s; }
    public void setDateStart(Date t)  { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setLabTic(int i) { mnLabTic = i; };
    public void setNLabTic(int i) { mnNLabTic = i; }
    public void setAllTic(int i) { mnAllTic = i; };
    public void setLabWei(double d) { mdLabWei = d; }
    public void setNLabWei(double d) { mdNLabWei = d; }
    public void setAllWei(double d) { mdAllWei = d; }
    public void setPondOil(double d) { mdPondOil = d; }
    public void setPondMoi(double d) { mdPondMoi = d; }

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
    public void setRowEdited(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch (row) {
            case 0:
                value = mtDateStart;
                break;
            case 1:
                value = mtDateEnd;
                break;
            case 2:
                value = msItem;
                break;
            case 3:
                value = mnLabTic;
                break;
            case 4:
                value = mnNLabTic;
                break;
            case 5:
                value = mnAllTic;
                break;
            case 6:
                value = mdLabWei;
                break;
            case 7:
                value = mdNLabWei;
                break;
            case 8:
                value = mdAllWei;
                break;
            case 9:
                value = mdPondOil;
                break;
            case 10:
                value = mdPondMoi;
                break;
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object o, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
