/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class TestAvocadoOilPercentage {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double dryMatterMin = 0.1586; // is %
        //double dryMatterMax = 0.3533; // is %, for 81 value pairs
        double dryMatterMax = 0.7645; // is %, for 250 value pairs
        double dryMatterInc = 0.0024333333;
        double oilPctMin = 0.04;
        double oilPctInc = 0.0025;
        //int valuePairs = 81;
        int valuePairs = 250;
        
        double[] dryMatterSamples = { 0.2462, 0.2474, 0.2486, 0.1829, 0.18415, 0.1854, 0.2754, 0.3419, 0.1586, 0.3533, 0.1585, 0.3534, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8 };
        double dryMatter;
        double dryMatterInf;
        double dryMatterSup;
        
        for (double dryMatterSample : dryMatterSamples) {
            System.out.println(SLibUtils.textRepeat("=", 80));
            System.out.println("% dry matter sample: " + SLibUtils.DecimalFormatPercentage2D.format(dryMatterSample));
            
            if (dryMatterSample < dryMatterMin || dryMatterSample > dryMatterMax) {
                System.out.println("WARNING!: % dry matter sample: " + SLibUtils.DecimalFormatPercentage2D.format(dryMatterSample) + " "
                        + "is out of range! (less than " + SLibUtils.DecimalFormatPercentage2D.format(dryMatterMin) + " or "
                        + "greater than " + SLibUtils.DecimalFormatPercentage2D.format(dryMatterMax) + ")");
            }
            else {
                dryMatter = dryMatterMin;
                for (int valuePair = 1; valuePair <= valuePairs; valuePair++) {
                    System.out.println("value pair " + valuePair + ": " + dryMatter + " (" + SLibUtils.DecimalFormatPercentage2D.format(dryMatter) + ")");

                    dryMatterInf = dryMatter;
                    dryMatter += dryMatterInc;
                    dryMatterSup = dryMatter;

                    if (dryMatterSample <= dryMatterSup) {
                        double dryMatterDiff = SLibUtils.round(SLibUtils.round(dryMatterSup, 4) - SLibUtils.round(dryMatterInf, 4), 4);
                        double pos = SLibUtils.round((SLibUtils.round(dryMatterInf, 4) - dryMatterMin) / dryMatterInc, 0);
                        double oilPctInf = pos * oilPctInc + oilPctMin;
                        double oilPctSup = oilPctInf + oilPctInc;
                        double oilPctDiff = SLibUtils.round(oilPctSup - oilPctInf, 4);
                        double ratio = oilPctDiff / dryMatterDiff;
                        double oilPct = SLibUtils.round((dryMatterSample - SLibUtils.round(dryMatterInf, 4)) * ratio + oilPctInf, 4);
                        System.out.println("% oil in sample: " + SLibUtils.DecimalFormatPercentage2D.format(oilPct) + " (" + oilPct + ") for "
                                + "% dry sample of: " + SLibUtils.DecimalFormatPercentage2D.format(dryMatterSample));
                        System.out.println(SLibUtils.textRepeat("-", 80));
                        System.out.println();
                        break;
                    }
                }
            }
        }
    }
}
