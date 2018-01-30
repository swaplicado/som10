/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomProductionEstimateToDistribute {

    protected double mdProductionAvailable;
    protected double mdProductionAssigned;
    protected String msItem;
    protected String msUnitCode;
    protected int mnFkItemId;
    protected int mnFkUnitId;

    public void SSomProductionEstimateToDistribute () {
        mdProductionAvailable = 0;
        mdProductionAssigned = 0;
        msItem = "";
        msUnitCode = "";
        mnFkItemId = 0;
        mnFkUnitId = 0;
    }

    public void setProductionAvailable(double d) { mdProductionAvailable = d; }
    public void setProductionAssigned(double d) { mdProductionAssigned = d; }
    public void setItem(String s) { msItem = s; }
    public void setUnitCode(String s) { msUnitCode = s; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }

    public double getProductionAvailable() { return mdProductionAvailable; }
    public double getProductionAssigned() { return mdProductionAssigned; }
    public String getItem() { return msItem; }
    public String getUnitCode() { return msUnitCode; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }

    public double getProductionBalance() { return mdProductionAvailable - mdProductionAssigned; }
}
