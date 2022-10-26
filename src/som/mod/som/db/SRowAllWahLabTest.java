/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Isabel Servín
 */
public class SRowAllWahLabTest implements SGridRow {

    protected SDbWahLabTest moWahLabTest;
    
    public SRowAllWahLabTest(SDbWahLabTest labTest) {
        moWahLabTest = labTest;
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return moWahLabTest.getRowPrimaryKey();
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
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRowEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRowEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch (row) {
            case 0:
                value = moWahLabTest.getDbmsBranchWarehouse().getCode();
                break;
            case 1:
                value = moWahLabTest.getDbmsBranchWarehouse().getName();
                break;
            case 2: 
                value = moWahLabTest.getDbmsItem().getName();
                break;
            case 3: 
                value = moWahLabTest.getAcidityPercentage_n();
                break;
            case 4: 
                value = moWahLabTest.isAcidityPercentageOverange();
                break;
            case 5: 
                value = moWahLabTest.getPeroxideIndex_n();
                break;
            case 6: 
                value = moWahLabTest.isPeroxideIndexOverange();
                break;
            case 7: 
                value = moWahLabTest.getMoisturePercentage_n();
                break;
            case 8: 
                value = moWahLabTest.isMoisturePercentageOverange();
                break;
            case 9: 
                value = moWahLabTest.getSolidPersentage_n();
                break;
            case 10:
                value = moWahLabTest.isSolidPersentageOverange();
                break;
            case 11:
                value = moWahLabTest.getLinoleicAcidPercentage_n();
                break;
            case 12:
                value = moWahLabTest.isLinoleicAcidPercentageOverange();
                break;
            case 13:
                value = moWahLabTest.getOleicAcidPercentage_n();
                break;
            case 14:
                value = moWahLabTest.isOleicAcidPercentageOverange();
                break;
            case 15:
                value = moWahLabTest.getLinolenicAcidPercentage_n();
                break;
            case 16:
                value = moWahLabTest.isLinolenicAcidPercentageOverange();
                break;
            case 17:
                value = moWahLabTest.getStearicAcidPercentage_n();
                break;
            case 18:
                value = moWahLabTest.isStearicAcidPercentageOverange();
                break;
            case 19:
                value = moWahLabTest.getPalmiticAcidPercentage_n();
                break;
            case 20:
                value = moWahLabTest.isPalmiticAcidPercentageOverange();
                break;
            case 21:
                value = moWahLabTest.getNote();
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
