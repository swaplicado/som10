/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowTicketSupplierInputType implements SGridRow {
    
    private SDbTicket moTicket;
    
    public SRowTicketSupplierInputType(SDbTicket ticket) {
        moTicket = ticket;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return moTicket.getRowPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moTicket.getRowCode();
    }

    @Override
    public String getRowName() {
        return moTicket.getRowName();
    }

    @Override
    public boolean isRowSystem() {
        return moTicket.isRowSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return moTicket.isRowDeletable();
    }

    @Override
    public boolean isRowEdited() {
        return moTicket.isRowEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        moTicket.setRowEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = moTicket.getNumber();
                break;
            case 1:
                value = moTicket.getXtaSeason();
                break;
            case 2:
                value = moTicket.getXtaRegion();
                break;
            case 3:
                value = moTicket.getXtaProducer();
                break;
            case 4:
                value = moTicket.getXtaInputType();
                break;
            case 5:
                value = moTicket.getDatetimeArrival();
                break;
            case 6:
                value = moTicket.getPlate();
                break;
            case 7:
                value = moTicket.getDriver();
                break;
            case 8:
                value = moTicket.getWeightSource();
                break;
            case 9:
                value = moTicket.getWeightDestinyNet_r();
                break;
            case 10:
                value = moTicket.getChildLaboratories().isEmpty() ? 0d : moTicket.getChildLaboratories().get(0).getImpuritiesPercentageAverage();
                break;
            case 11:
                value = moTicket.getChildLaboratories().isEmpty() ? 0d : moTicket.getChildLaboratories().get(0).getMoisturePercentageAverage();
                break;
            case 12:
                value = moTicket.getUserPenaltyPercentage();
                break;
            case 13:
                value = moTicket.getUserWeightPayment();
                break;
            case 14:
                value = moTicket.getUserPricePerTon();
                break;
            case 15:
                value = moTicket.getUserTotal_r();
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
