/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.util.ArrayList;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomProductionEstimateDistributeWarehouses extends SSomProductionEstimateToDistribute {

    protected ArrayList<SSomMfgWarehouseProduct> maMfgWarehouseProductProduction = new ArrayList<SSomMfgWarehouseProduct>();
    protected ArrayList<SSomMfgWarehouseProduct> maMfgWarehouseProductStorage = new ArrayList<SSomMfgWarehouseProduct>();

    protected ArrayList<SDbIog> maIogOut = new ArrayList<SDbIog>();
    protected ArrayList<SDbIog> maIogIn = new ArrayList<SDbIog>();

    public void SSomProductionEstimateDistributeWarehouses () {
        super.SSomProductionEstimateToDistribute();
        
        maMfgWarehouseProductProduction = new ArrayList<SSomMfgWarehouseProduct>();
        maMfgWarehouseProductStorage = new ArrayList<SSomMfgWarehouseProduct>();

        maIogOut = new ArrayList<SDbIog>();
        maIogIn = new ArrayList<SDbIog>();
    }

    public ArrayList<SSomMfgWarehouseProduct> getMfgWarehouseProductProduction() { return maMfgWarehouseProductProduction; }
    public ArrayList<SSomMfgWarehouseProduct> getMfgWarehouseProductStorage() { return maMfgWarehouseProductStorage; }

    public ArrayList<SDbIog> getIogOut() { return maIogOut; }
    public ArrayList<SDbIog> getIogIn() { return maIogIn; }
}
