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
public class SGrindingResumeRow {
    
    protected String msDataName;
    protected double mdValue;
    protected String msUnit;

    public String getDataName() {
        return msDataName;
    }

    public void setDataName(String msData) {
        this.msDataName = msData;
    }

    public double getValue() {
        return mdValue;
    }

    public void setValue(double mdValue) {
        this.mdValue = mdValue;
    }

    public String getUnit() {
        return msUnit;
    }

    public void setUnit(String msUnit) {
        this.msUnit = msUnit;
    }
}
