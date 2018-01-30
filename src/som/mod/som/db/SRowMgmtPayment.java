/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Juan Barajas
 */
public class SRowMgmtPayment implements SGridRow {

    protected String msPeriod;
    protected String msBkc;
    protected String msBranch;
    protected String msRecord;
    protected Date mtDate;
    protected String msAccountNumber;
    protected String msAccount;
    protected String msConcept;
    protected double mdCredit;
    protected double mdDebit;
    protected double mdExchangeRate;
    protected double mdDebitCy;
    protected double mdCreditCy;
    protected String msCurrencyKey;
    protected boolean mbSystem;
    protected String msAccountingMoveSubclass;
    protected String msCostCenterId_n;
    protected String msCostCenter_n;
    protected boolean mbDeleted;
    protected String msUserNew;
    protected Date mtTsUserNew;
    protected String msUserEdit;
    protected Date mtTsUserEdit;
    protected String msUserDel;
    protected Date mtTsUserDel;

    public SRowMgmtPayment() {
        msPeriod = "";
        msBkc = "";
        msBranch = "";
        msRecord = "";
        mtDate = null;
        msAccountNumber = "";
        msAccount = "";
        msConcept = "";
        mdCredit = 0;
        mdDebit = 0;
        mdExchangeRate = 0;
        mdDebitCy = 0;
        mdCreditCy = 0;
        msCurrencyKey = "";
        mbSystem = false;
        msAccountingMoveSubclass = "";
        msCostCenterId_n = "";
        msCostCenter_n = "";
        mbDeleted = false;
        msUserNew = "";
        mtTsUserNew = null;
        msUserEdit = "";
        mtTsUserEdit = null;
        msUserDel = "";
        mtTsUserDel = null;
    }

    public void setPeriod(String s) { msPeriod = s; }
    public void setBkc(String s) { msBkc = s; }
    public void setBranch(String s) { msBranch = s; }
    public void setRecord(String s) { msRecord = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setAccountNumber(String s) { msAccountNumber = s; }
    public void setAccount(String s) { msAccount = s; }
    public void setConcept(String s) { msConcept = s; }
    public void setCredit(double d) { mdCredit = d; }
    public void setDebit(double d) { mdDebit = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d;; }
    public void setDebitCy(double d) { mdDebitCy = d;; }
    public void setCreditCy(double d) { mdCreditCy = d;; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setAccountingMoveSubclass(java.lang.String s) { msAccountingMoveSubclass = s; }
    public void setCostCenterId_n(java.lang.String s) { msCostCenterId_n = s; }
    public void setCostCenter_n(java.lang.String s) { msCostCenter_n = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setUserNew(String s) { msUserNew = s; }
    public void setTsUserNew(Date t) { mtTsUserNew = t; }
    public void setUserEdit(String s) { msUserEdit = s; }
    public void setTsUserEdit(Date t) { mtTsUserEdit = t; }
    public void setUserDel(String s) { msUserDel = s; }
    public void setTsUserDel(Date t) { mtTsUserDel = t; }

    public String getPeriod() { return msPeriod; }
    public String getBkc() { return msBkc; }
    public String getBranch() { return msBranch; }
    public String getRecord() { return msRecord; }
    public Date getDate() { return mtDate; }
    public String getAccountNumber() { return msAccountNumber; }
    public String getAccount() { return msAccount; }
    public String getConcept() { return msConcept; }
    public double getCredit() { return mdCredit; }
    public double getDebit() { return mdDebit; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getDebitCy() { return mdDebitCy; }
    public double getCreditCy() { return mdCreditCy; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }
    public boolean isSystem() { return mbSystem; }
    public java.lang.String getAccountingMoveSubclass() { return msAccountingMoveSubclass; }
    public java.lang.String getCostCenterId_n() { return msCostCenterId_n; }
    public java.lang.String getCostCenter_n() { return msCostCenter_n; }
    public boolean isDeleted() { return mbDeleted; }
    public String getUserNew() { return msUserNew; }
    public Date getTsUserNew() { return mtTsUserNew; }
    public String getUserEdit() { return msUserEdit; }
    public Date getTsUserEdit() { return mtTsUserEdit; }
    public String getUserDel() { return msUserDel; }
    public Date getTsUserDel() { return mtTsUserDel; }

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
                value = msPeriod;
                break;
            case 1:
                value = msBkc;
                break;
            case 2:
                value = msBranch;
                break;
            case 3:
                value = msRecord;
                break;
            case 4:
                value = mtDate;
                break;
            case 5:
                value = msAccountNumber;
                break;
            case 6:
                value = msAccount;
                break;
            case 7:
                value = msConcept;
                break;
            case 8:
                value = mdDebit;
                break;
            case 9:
                value = mdCredit;
                break;
            case 10:
                value = mdExchangeRate;
                break;
            case 11:
                value = mdDebitCy;
                break;
            case 12:
                value = mdCreditCy;
                break;
            case 13:
                value = msCurrencyKey;
                break;
            case 14:
                value = mbSystem;
                break;
            case 15:
                value = msAccountingMoveSubclass;
                break;
            case 16:
                value = msCostCenterId_n;
                break;
            case 17:
                value = msCostCenter_n;
                break;
            case 18:
                value = mbDeleted;
                break;
            case 19:
                value = msUserNew;
                break;
            case 20:
                value = mtTsUserNew;
                break;
            case 21:
                value = msUserEdit;
                break;
            case 22:
                value = mtTsUserEdit;
                break;
            case 23:
                value = msUserDel;
                break;
            case 24:
                value = mtTsUserDel;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
