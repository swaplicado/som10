/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

/**
 *
 * @author Edwin Carmona
 */
public class SFormulasRow {
    FormulaColumn r00;
    FormulaColumn r02;
    FormulaColumn r04;
    FormulaColumn r06;
    FormulaColumn r08;
    FormulaColumn r10;
    FormulaColumn r12;
    FormulaColumn r14;
    FormulaColumn r16;
    FormulaColumn r18;
    FormulaColumn r20;
    FormulaColumn r22;
    FormulaColumn rExtra;


    // Getter Methods 

    public FormulaColumn getR00() {
        return r00;
    }

    public FormulaColumn getR02() {
        return r02;
    }

    public FormulaColumn getR04() {
        return r04;
    }

    public FormulaColumn getR06() {
        return r06;
    }

    public FormulaColumn getR08() {
        return r08;
    }

    public FormulaColumn getR10() {
        return r10;
    }

    public FormulaColumn getR12() {
        return r12;
    }

    public FormulaColumn getR14() {
        return r14;
    }

    public FormulaColumn getR16() {
        return r16;
    }

    public FormulaColumn getR18() {
        return r18;
    }

    public FormulaColumn getR20() {
        return r20;
    }

    public FormulaColumn getR22() {
        return r22;
    }

    public FormulaColumn getrExtra() {
        return rExtra;
    }


    // Setter Methods 

    public void setR00(FormulaColumn r00Object) {
     this.r00 = r00Object;
    }

    public void setR02(FormulaColumn r02Object) {
     this.r02 = r02Object;
    }

    public void setR04(FormulaColumn r04Object) {
     this.r04 = r04Object;
    }

    public void setR06(FormulaColumn r06Object) {
     this.r06 = r06Object;
    }

    public void setR08(FormulaColumn r08Object) {
     this.r08 = r08Object;
    }

    public void setR10(FormulaColumn r10Object) {
     this.r10 = r10Object;
    }

    public void setR12(FormulaColumn r12Object) {
     this.r12 = r12Object;
    }

    public void setR14(FormulaColumn r14Object) {
     this.r14 = r14Object;
    }

    public void setR16(FormulaColumn r16Object) {
     this.r16 = r16Object;
    }

    public void setR18(FormulaColumn r18Object) {
     this.r18 = r18Object;
    }

    public void setR20(FormulaColumn r20Object) {
     this.r20 = r20Object;
    }

    public void setR22(FormulaColumn r22Object) {
     this.r22 = r22Object;
    }

    public void setrExtra(FormulaColumn rExtra) {
        this.rExtra = rExtra;
    }
}
    
class FormulaColumn {
    private boolean isActive;
    private String formula;
    private int indexRow;
    private int colNumber;


    // Getter Methods 

    public boolean getIsActive() {
        return isActive;
    }

    public String getFormula() {
        return formula;
    }
    
    public int getIndexRow() {
        return indexRow;
    }

    public int getColNumber() {
        return colNumber;
    }
    
    

    // Setter Methods 

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setFormula(String f) {
        this.formula = f;
    }

    public void setIndexRow(int indexRow) {
        this.indexRow = indexRow;
    }

    public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }
}
