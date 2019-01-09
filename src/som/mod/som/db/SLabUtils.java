/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SLabUtils {
    
    /**
     * Estimates percentage (pct.) of fruit oil from pct. of dry matter of sample.
     * A conversion table of pct. of fruit oil from pct. of dry matter of sample is used in estimation.
     * @param fruit Fruit type.
     * @param dryMatterSamplePct Percentage of dry matter in fruit sample.
     * @return
     * @throws Exception 
     */
    public static double estimateFruitOilPct(int fruit, double dryMatterSamplePct) throws Exception {
        double fruitOilPct = 0;
        
        switch (fruit) {
            case SLabConsts.FRUIT_AVOCADO:
                double dryMatterMin = 0.1586;   // minimun value (pct.) in conversion table for dry matter in avocado
                //double dryMatterMax = 0.3533; // maximun value (pct.) in conversion table for dry matter in avocado (for 81 value pairs or rows)
                double dryMatterMax = 0.7645;   // maximun value (pct.) in conversion table for dry matter in avocado (for 250 value pairs or rows)
                double dryMatterInc = 0.0024333333; // observable increment between value pairs in conversion table for pct. of dry matter
                
                if (dryMatterSamplePct < dryMatterMin || dryMatterSamplePct > dryMatterMax) {
                    throw new Exception ("¡ADVERTENCIA! El porcentaje de materia seca de la muestra : " + SLibUtils.DecimalFormatPercentage2D.format(dryMatterSamplePct) + " está fuera de rango:\n"
                            + "No puede ser menor que " + SLibUtils.DecimalFormatPercentage2D.format(dryMatterMin) + " o mayor que " + SLibUtils.DecimalFormatPercentage2D.format(dryMatterMax) + ".");
                }
                
                double fruitOilPctMin = 0.0400; // minimun value in conversion table for fruit oil in avocado (is %)
                double fruitOilPctInc = 0.0025; // observable increment between value pairs in conversion table for pct. of fruit oil
                //int valuePairs = 81;  // fixed number of value pairs (or rows) in conversion table for avocado
                int valuePairs = 250;   // fixed number of value pairs (or rows) in conversion table for avocado

                double dryMatter = dryMatterMin;
                double dryMatterInf;
                double dryMatterSup;

                for (int valuePair = 1; valuePair <= valuePairs; valuePair++) {
                    dryMatterInf = dryMatter;
                    dryMatter += dryMatterInc;
                    dryMatterSup = dryMatter;

                    // lookup pct. of dry matter of sample in conversion table:
                    if (dryMatterSamplePct <= dryMatterSup) {
                        // interpolate pct. of fruit oil:
                        double dryMatterDiff = SLibUtils.round(SLibUtils.round(dryMatterSup, 4) - SLibUtils.round(dryMatterInf, 4), 4);
                        double pos = SLibUtils.round((SLibUtils.round(dryMatterInf, 4) - dryMatterMin) / dryMatterInc, 0);
                        double fruitOilPctInf = pos * fruitOilPctInc + fruitOilPctMin;
                        double fruitOilPctSup = fruitOilPctInf + fruitOilPctInc;
                        double fruitOilPctDiff = SLibUtils.round(fruitOilPctSup - fruitOilPctInf, 4);
                        double ratio = fruitOilPctDiff / dryMatterDiff;
                        fruitOilPct = SLibUtils.round((dryMatterSamplePct - SLibUtils.round(dryMatterInf, 4)) * ratio + fruitOilPctInf, 4);
                        break;
                    }
                }
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return fruitOilPct;
    }
}
