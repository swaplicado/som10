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
public class SGrindingData {
    protected int month;
    protected String monthText;
    protected double sumGrindingBascule;
    protected double sumGrindingOilPercent;
    protected double weightedAverage;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMonthText() {
        return monthText;
    }

    public void setMonthText(String monthText) {
        this.monthText = monthText;
    }

    public double getSumGrindingBascule() {
        return sumGrindingBascule;
    }

    public void setSumGrindingBascule(double sumGrindingBascule) {
        this.sumGrindingBascule = sumGrindingBascule;
    }

    public double getSumGrindingOilPercent() {
        return sumGrindingOilPercent;
    }

    public void setSumGrindingOilPercent(double sumGrindingOilPercent) {
        this.sumGrindingOilPercent = sumGrindingOilPercent;
    }

    public double getWeightedAverage() {
        return weightedAverage;
    }

    public void setWeightedAverage(double weightedAverage) {
        this.weightedAverage = weightedAverage;
    }
    
    
}
