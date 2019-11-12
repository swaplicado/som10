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
     * Estimate the percentage of oil given a percentage of dry matter in a fruit sample.
     * A conversion table is used in this estimation.
     * The original table is bounded to 81 rows (pair of values: pct. of dry matter to pct. of oil).
     * Due the pair of values of the table seem to be lineal, the table is extended up to 250 row of values.
     * @param fruit Fruit type.
     * @param sampleDryMatterPct Percentage of dry matter in a fruit sample.
     * @return
     * @throws Exception 
     */
    public static double estimateFruitOilPct(final int fruit, final double sampleDryMatterPct) throws Exception {
        double fruitOilPct = 0;
        
        switch (fruit) {
            case SLabConsts.FRUIT_AVOCADO:
                // values for conversion table from pct. of dry matter to pct. of oil in avocado:
                
                double stdFruitOilPctMin = 0.0400; // minimun pct. of oil in table
                double stdFruitOilPctInc = 0.0025; // observable increment between rows in table for pct. of oil

                //int rows = 81; // fixed number of rows in original table
                int rows = 250;  // fixed number of rows in "extended" table
                
                double stdDryMatterMin = 0.1586; // minimun pct. of dry matter in table
                
                double stdDryMatterInc = 0.0024333333; // observable increment between rows in table for pct. of dry matter
                
                //double stdDryMatterMax = 0.3533; // fixed maximun pct. of dry matter in table (for 81 rows)
                //double stdDryMatterMax = 0.7645; // fixed maximun pct. of dry matter in table (for 250 rows)
                double stdDryMatterMax = SLibUtils.round(stdDryMatterMin + (stdDryMatterInc * (rows - 1)), 4); // calculated maximun pct. of dry matter in table
                
                if (sampleDryMatterPct < stdDryMatterMin) {
                    int extraPairsMax = (int) (stdFruitOilPctMin / stdFruitOilPctInc);
                    int extraPairsReq = (int) ((stdDryMatterMin - sampleDryMatterPct) / stdDryMatterInc) + 1;
                    
                    if (extraPairsReq > extraPairsMax) {
                        throw new Exception ("¡ADVERTENCIA! El % de materia seca de la muestra : " + SLibUtils.DecimalFormatPercentage2D.format(sampleDryMatterPct) + " está fuera de rango:\n"
                                + "No debería ser menor que " + SLibUtils.DecimalFormatPercentage2D.format(stdDryMatterMin) + " ni mayor que " 
                                + SLibUtils.DecimalFormatPercentage2D.format(stdDryMatterMax) + " de acuerdo a la tabla de conversión de referencia.\n"
                                + "No obstante, se intentó ajustar el % mínimo de materia seca de la tabla, pero no puede ser menor a " + SLibUtils.DecimalFormatPercentage2D.format(stdDryMatterMin - (stdDryMatterInc * extraPairsMax)) + ".\n"
                                + "Para este valor corresponde un 0% de aceite.");
                    }
                    else {
                        // adjust conversion table:
                        stdFruitOilPctMin = SLibUtils.round(stdFruitOilPctMin - stdFruitOilPctInc * extraPairsReq, 4);
                        rows += extraPairsReq;
                        stdDryMatterMin = SLibUtils.round(stdDryMatterMin - stdDryMatterInc * extraPairsReq, 4);
                        stdDryMatterMax = SLibUtils.round(stdDryMatterMin + (stdDryMatterInc * (rows - 1)), 4); // max. value should remain without any change!
                    }
                }
                else if (sampleDryMatterPct > stdDryMatterMax) {
                    throw new Exception ("¡ADVERTENCIA! El % de materia seca de la muestra : " + SLibUtils.DecimalFormatPercentage2D.format(sampleDryMatterPct) + " está fuera de rango:\n"
                            + "No debería ser menor que " + SLibUtils.DecimalFormatPercentage2D.format(stdDryMatterMin) + " ni mayor que " 
                            + SLibUtils.DecimalFormatPercentage2D.format(stdDryMatterMax) + " de acuerdo a la tabla de conversión de referencia.");
                }
                
                double auxDryMatter = stdDryMatterMin;
                double auxDryMatterInf;
                double auxDryMatterSup;

                for (int row = 1; row <= rows; row++) {
                    auxDryMatterInf = auxDryMatter;
                    auxDryMatter += stdDryMatterInc;
                    auxDryMatterSup = auxDryMatter;

                    // lookup pct. of dry matter of sample in conversion table:
                    if (sampleDryMatterPct <= auxDryMatterSup) {
                        // interpolate for pct. of oil:
                        
                        /* former and inefficient calculus:
                        double dryMatterDiff = SLibUtils.round(SLibUtils.round(auxDryMatterSup, 4) - SLibUtils.round(auxDryMatterInf, 4), 4);
                        double pos = SLibUtils.round((SLibUtils.round(auxDryMatterInf, 4) - stdDryMatterMin) / stdDryMatterInc, 0);
                        double fruitOilPctInf = pos * stdFruitOilPctInc + stdFruitOilPctMin;
                        double fruitOilPctSup = fruitOilPctInf + stdFruitOilPctInc;
                        double fruitOilPctDiff = SLibUtils.round(fruitOilPctSup - fruitOilPctInf, 4);
                        double ratio = fruitOilPctDiff / dryMatterDiff;
                        fruitOilPct = SLibUtils.round((sampleDryMatterPct - SLibUtils.round(auxDryMatterInf, 4)) * ratio + fruitOilPctInf, 4);
                        */
                        
                        double calcFruitOilPctInf = stdFruitOilPctMin + ((row - 1) * stdFruitOilPctInc);
                        double calcRatio = stdFruitOilPctInc / stdDryMatterInc; // increments in table are both constants for fruit oil and dry matter
                        fruitOilPct = calcFruitOilPctInf + (sampleDryMatterPct - auxDryMatterInf) * calcRatio;
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
