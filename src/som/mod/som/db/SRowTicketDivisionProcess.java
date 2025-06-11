/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.io.Serializable;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Isabel Servín
 */
public class SRowTicketDivisionProcess implements SGridRow, Serializable {

    protected String msTicNum;
    protected String msProd;
    protected double mdSource;
    protected double mdArrival;
    protected double mdDeparture;
    protected double mdNet;

    public void setTicNum(String ticNum) {
        msTicNum = ticNum;
    }

    public void setProd(String prod) {
        msProd = prod;
    }
    
    public void setSource(double source) {
        mdSource = source;
    }
    
    public void setArrival(double arrival) {
        mdArrival = arrival;
    }
    
    public void setDeparture(double departure) {
        mdDeparture = departure;
    }
    
    public void setNet(double net) {
        mdNet = net;
    }
    
    public String getTicNum() {
        return msTicNum;
    }
    
    public String getProd() {
        return msProd;
    }

    public double getSource() {
        return mdSource;
    }

    public double getArrival() {
        return mdArrival;
    }

    public double getDeparture() {
        return mdDeparture;
    }

    public double getNet() {
        return mdNet;
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] {};
    }

    @Override
    public String getRowCode() {
        return "";
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
        return false;
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
            case 0: value = msTicNum; break;
            case 1: value = msProd; break;
            case 2: value = mdSource; break;
            case 3: value = mdArrival; break;
            case 4: value = mdDeparture; break;
            case 5: value = mdNet; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
    
    }
}
