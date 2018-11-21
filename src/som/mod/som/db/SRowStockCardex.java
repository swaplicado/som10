/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas, Sergio Flores
 * 2018-11-22, Sergio Flores:
 * 1) Adición de columna referencia en tabla de movimientos de almacén.
 * 2) Adición de referencia y observaciones de movimientos de almacén a vistas y tarjeta auxiliar.
 */
public class SRowStockCardex implements SGridRow {

    protected int mnNumber;
    protected Date mtDate;
    protected String msMoveType;
    protected String msIogAdjType;
    protected String msBranch;
    protected String msWarehouse;
    protected String msDivision;
    protected double mdIn;
    protected double mdOut;
    protected double mdStock;
    protected String msUnit;
    protected String msReference;
    protected String msNote;
    protected Date mtIogDate;
    protected String msIogType;
    protected String msIogNumber;
    protected String msIogBranch;
    protected String msIogWarehouse;
    protected String msUserInsert;
    protected Date mtDateUserInsert;
    protected String msUserUpdate;
    protected Date mtDateUserUpdate;
    protected Date mtDpsDate;
    protected String msDpsType;
    protected String msDpsNumber;
    protected String msDpsBranch;
    protected Date mtAdjDate;
    protected String msAdjType;
    protected String msAdjNumber;
    protected String msAdjBranch;

    public SRowStockCardex() {
        mnNumber = 0;
        mtDate = null;
        msMoveType = "";
        msIogAdjType = "";
        msBranch = "";
        msWarehouse = "";
        msDivision = "";
        mdIn = 0;
        mdOut = 0;
        mdStock = 0;
        msUnit = "";
        msReference = "";
        msNote = "";
        mtIogDate = null;
        msIogType = "";
        msIogNumber = "";
        msIogBranch = "";
        msIogWarehouse = "";
        msUserInsert = "";
        mtDateUserInsert = null;
        msUserUpdate = "";
        mtDateUserUpdate = null;
        mtDpsDate = null;
        msDpsType = "";
        msDpsNumber = "";
        msDpsBranch = "";
        mtAdjDate = null;
        msAdjType = "";
        msAdjNumber = "";
        msAdjBranch = "";
    }

    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setMoveType(String s) { msMoveType = s; }
    public void setIogAdjType(String s) { msIogAdjType = s; }
    public void setBranch(String s) { msBranch = s; }
    public void setDivision(String s) { msDivision = s; }
    public void setWarehouse(String s) { msWarehouse = s; }
    public void setIn(double d) { mdIn = d; }
    public void setOut(double d) { mdOut = d; }
    public void setStock(double d) { mdStock = d; }
    public void setUnit(String s) { msUnit = s; }
    public void setReference(String s) { msReference = s; }
    public void setNote(String s) { msNote = s; }
    public void setIogDate(Date t) { mtIogDate = t; }
    public void setIogType(String s) { msIogType = s; }
    public void setIogNumber(String s) { msIogNumber = s; }
    public void setIogBranch(String s) { msIogBranch = s; }
    public void setIogWarehouse(String s) { msIogWarehouse = s; }
    public void setUserInsert(String s) { msUserInsert = s; }
    public void setDateUserInsert(Date t) { mtDateUserInsert = t; }
    public void setUserUpdate(String s) { msUserUpdate = s; }
    public void setDateUserUpdate(Date t) { mtDateUserUpdate = t; }
    public void setDpsDate(Date t) { mtDpsDate = t; }
    public void setDpsType(String s) { msDpsType = s; }
    public void setDpsNumber(String s) { msDpsNumber = s; }
    public void setDpsBranch(String s) { msDpsBranch = s; }
    public void setAdjDate(Date t) { mtAdjDate = t; }
    public void setAdjType(String s) { msAdjType = s; }
    public void setAdjNumber(String s) { msAdjNumber = s; }
    public void setAdjBranch(String s) { msAdjBranch = s; }

    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public String getMoveType() { return msMoveType; }
    public String getIogAdjType() { return msIogAdjType; }
    public String getBranch() { return msBranch; }
    public String getDivision() { return msDivision; }
    public String getWarehouse() { return msWarehouse; }
    public double getIn() { return mdIn; }
    public double getOut() { return mdOut; }
    public double getStock() { return mdStock; }
    public String getUnit() { return msUnit; }
    public String getReference() { return msReference; }
    public String getNote() { return msNote; }
    public Date getIogDate() { return mtIogDate; }
    public String getIogType() { return msIogType; }
    public String getIogNumber() { return msIogNumber; }
    public String getIogBranch() { return msIogBranch; }
    public String getIogWarehouse() { return msIogWarehouse; }
    public String getUserInsert() { return msUserInsert; }
    public Date getDateUserInsert() { return mtDateUserInsert; }
    public String getUserUpdate() { return msUserUpdate; }
    public Date getDateUserUpdate() { return mtDateUserUpdate; }
    public Date getDpsDate() { return mtDpsDate; }
    public String getDpsType() { return msDpsType; }
    public String getDpsNumber() { return msDpsNumber; }
    public String getDpsBranch() { return msDpsBranch; }
    public Date getAdjDate() { return mtAdjDate; }
    public String getAdjType() { return msAdjType; }
    public String getAdjNumber() { return msAdjNumber; }
    public String getAdjBranch() { return msAdjBranch; }

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
                value = mtDate;
                break;
            case 1:
                value = msBranch;
                break;
            case 2:
                value = msWarehouse;
                break;
            case 3:
                value = msDivision;
                break;
            case 4:
                value = mnNumber;
                break;
            case 5:
                value = msMoveType;
                break;
            case 6:
                value = msIogAdjType;
                break;
            case 7:
                value = mdIn;
                break;
            case 8:
                value = mdOut;
                break;
            case 9:
                value = mdStock;
                break;
            case 10:
                value = msUnit;
                break;
            case 11:
                value = msReference;
                break;
            case 12:
                value = msNote;
                break;
            case 13:
                value = msIogNumber;
                break;
            case 14:
                value = mtIogDate;
                break;
            case 15:
                value = msDpsType;
                break;
            case 16:
                value = msDpsNumber;
                break;
            case 17:
                value = mtDpsDate;
                break;
            case 18:
                value = msAdjType;
                break;
            case 19:
                value = msAdjNumber;
                break;
            case 20:
                value = mtAdjDate;
                break;
            case 21:
                value = msUserInsert;
                break;
            case 22:
                value = mtDateUserInsert;
                break;
            case 23:
                value = msUserUpdate;
                break;
            case 24:
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
