/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public abstract class SExwUtils {
    
    /**
     * Get external warehouse stock.
     * @param scaleId
     * @param itemId
     * @param unitId
     * @param exwFacilityId
     * @param cutoff
     * @return
     * @throws Exception
     */
    public static Stock getExwStock(final int scaleId, final int itemId, final int unitId, final int exwFacilityId, final Date cutoff) throws Exception {
        Stock stock = null;
        
        return stock;
    }
    
    /**
     * Stock.
     */
    public static class Stock {
        
        public double OpeningStock;
        public double Inflows;
        public double Outflows;
        public double Stock;
        
        public Stock(final double openingStock, final double inflows, final double outflows) {
            OpeningStock = openingStock;
            Inflows = inflows;
            Outflows = outflows;
            Stock = openingStock + inflows - outflows;
        }
    }
}
