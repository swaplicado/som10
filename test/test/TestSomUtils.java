/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Date;
import sa.lib.SLibTimeUtils;
import som.cli.SCliConsts;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author SERGIO_FLORES
 */
public class TestSomUtils {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("================================================================================");
            System.out.println("*** \"Hoy\" a finales de enero ***");
            
            Date today1 = SLibTimeUtils.createDate(2026, 1, 24);

            SSomUtils.DateSettings ds1 = new SSomUtils.DateSettings(today1, 1, 1);
            System.out.println("ds1:\n" + ds1);

            SSomUtils.DateSettings ds2 = new SSomUtils.DateSettings(today1, 9, 1);
            System.out.println("ds2:\n" + ds2);

            today1 = SLibTimeUtils.createDate(2026, 10, 15);
            SSomUtils.DateSettings ds3 = new SSomUtils.DateSettings(today1, 9, 1);
            System.out.println("ds3:\n" + ds3);

            today1 = SLibTimeUtils.createDate(2026, 1, 25);
            SSomUtils.DateSettings ds4 = new SSomUtils.DateSettings(today1, 7, 26);
            System.out.println("ds4:\n" + ds4);

            today1 = SLibTimeUtils.createDate(2026, 1, 26);
            SSomUtils.DateSettings ds5 = new SSomUtils.DateSettings(today1, 7, 26);
            System.out.println("ds5:\n" + ds5);

            today1 = SLibTimeUtils.createDate(2026, 1, 27);
            SSomUtils.DateSettings ds6 = new SSomUtils.DateSettings(today1, 7, 26);
            System.out.println("ds6:\n" + ds6);
            
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Season months in ds1:\n" + ds1.getSeasonMonthsBoundariesAsString());
            System.out.println("Season months in ds2:\n" + ds2.getSeasonMonthsBoundariesAsString());
            System.out.println("Season months in ds3:\n" + ds3.getSeasonMonthsBoundariesAsString());
            System.out.println("Season months in ds4:\n" + ds4.getSeasonMonthsBoundariesAsString());
            System.out.println("Season months in ds5:\n" + ds5.getSeasonMonthsBoundariesAsString());
            System.out.println("Season months in ds6:\n" + ds6.getSeasonMonthsBoundariesAsString());
            
            System.out.println();
            System.out.println("================================================================================");
            System.out.println("*** \"Hoy\" a finales de septiembre ***");
            
            Date today2 = SLibTimeUtils.createDate(2026, 9, 30);

            SSomUtils.DateSettings ds11 = new SSomUtils.DateSettings(today2, 1, 1);
            System.out.println("ds11:\n" + ds11);

            SSomUtils.DateSettings ds12 = new SSomUtils.DateSettings(today2, 9, 1);
            System.out.println("ds12:\n" + ds12);

            SSomUtils.DateSettings ds13 = new SSomUtils.DateSettings(today2, 7, 25);
            System.out.println("ds13:\n" + ds13);

            SSomUtils.DateSettings ds14 = new SSomUtils.DateSettings(today2, 7, 26);
            System.out.println("ds14:\n" + ds14);

            SSomUtils.DateSettings ds15 = new SSomUtils.DateSettings(today2, 7, 27);
            System.out.println("ds15:\n" + ds15);
            
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Season months in ds11:\n" + ds11.getSeasonMonthsBoundariesAsString());
            System.out.println("Season months in ds12:\n" + ds12.getSeasonMonthsBoundariesAsString());
            System.out.println("Season months in ds13:\n" + ds13.getSeasonMonthsBoundariesAsString());
            System.out.println("Season months in ds14:\n" + ds14.getSeasonMonthsBoundariesAsString());
            System.out.println("Season months in ds15:\n" + ds15.getSeasonMonthsBoundariesAsString());
            
            System.out.println("================================================================================");
            System.out.println("*** Límites de histórico de temporadas para fechas con \"hoy\" a finales de enero ***");
            System.out.println("Season boundaries for ds1:\n" + SSomUtils.DateSettings.getSeasonMonthsBoundariesAsString(ds1.createSeasonsYearsBoundaries(SCliConsts.FRUIT_FIRST_YEAR)));
            System.out.println("Season boundaries for ds2:\n" + SSomUtils.DateSettings.getSeasonMonthsBoundariesAsString(ds2.createSeasonsYearsBoundaries(SCliConsts.FRUIT_FIRST_YEAR)));
            System.out.println("Season boundaries for ds3:\n" + SSomUtils.DateSettings.getSeasonMonthsBoundariesAsString(ds3.createSeasonsYearsBoundaries(SCliConsts.FRUIT_FIRST_YEAR)));
            System.out.println("Season boundaries for ds4:\n" + SSomUtils.DateSettings.getSeasonMonthsBoundariesAsString(ds4.createSeasonsYearsBoundaries(SCliConsts.FRUIT_FIRST_YEAR)));
            System.out.println("Season boundaries for ds5:\n" + SSomUtils.DateSettings.getSeasonMonthsBoundariesAsString(ds5.createSeasonsYearsBoundaries(SCliConsts.FRUIT_FIRST_YEAR)));
            System.out.println("Season boundaries for ds6:\n" + SSomUtils.DateSettings.getSeasonMonthsBoundariesAsString(ds6.createSeasonsYearsBoundaries(SCliConsts.FRUIT_FIRST_YEAR)));
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}
