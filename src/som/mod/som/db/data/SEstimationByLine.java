/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db.data;

/**
 *
 * @author Edwin Carmona
 */
public class SEstimationByLine {
    protected String lineCode;
    protected String lineName;
    protected double finishedGoods;
    protected double trashProduct;
    protected double repProduct;
    protected double rawMaterial;

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public double getFinishedGoods() {
        return finishedGoods;
    }

    public void setFinishedGoods(double finishedGoods) {
        this.finishedGoods = finishedGoods;
    }

    public double getTrashProduct() {
        return trashProduct;
    }

    public void setTrashProduct(double trashProduct) {
        this.trashProduct = trashProduct;
    }

    public double getRepProduct() {
        return repProduct;
    }

    public void setRepProduct(double repProduct) {
        this.repProduct = repProduct;
    }

    public double getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(double rawMaterial) {
        this.rawMaterial = rawMaterial;
    }
    
    
}
