/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.gui.SGuiSession;
import som.cli.SCliConsts;
import som.cli.SCliUtils;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Sergio Flores
 */
public class TestSomUtils {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SGuiSession session = SCliUtils.createSession();
            
            /*
            SGuiSession session = new SGuiSession(null);
            
            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);

            database.connect(
                    //"localhost",
                    "192.168.1.233",
                    "3306",
                    "som_com",
                    "root",
                    "msroot");
            
            session.setDatabase(database);
            */
            
            int targetItemId = SCliConsts.ID_AVO_FRUIT_CONV;
            
            System.out.println("================================================================================");
            System.out.println("*** \"Hoy\" a finales de enero ***");
            
            Date today1 = SLibTimeUtils.createDate(2026, 1, 24);

            SSomUtils.DateProps dp1 = new SSomUtils.DateProps(session, today1, SCliConsts.FRUIT_FIRST_YEAR, 1, 1, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp1:\n" + dp1);

            SSomUtils.DateProps dp2 = new SSomUtils.DateProps(session, today1, SCliConsts.FRUIT_FIRST_YEAR, 9, 1, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp2:\n" + dp2);

            today1 = SLibTimeUtils.createDate(2026, 10, 15);
            SSomUtils.DateProps dp3 = new SSomUtils.DateProps(session, today1, SCliConsts.FRUIT_FIRST_YEAR, 9, 1, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp3:\n" + dp3);

            today1 = SLibTimeUtils.createDate(2026, 1, 25);
            SSomUtils.DateProps dp4 = new SSomUtils.DateProps(session, today1, SCliConsts.FRUIT_FIRST_YEAR, 7, 26, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp4:\n" + dp4);

            today1 = SLibTimeUtils.createDate(2026, 1, 26);
            SSomUtils.DateProps dp5 = new SSomUtils.DateProps(session, today1, SCliConsts.FRUIT_FIRST_YEAR, 7, 26, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp5:\n" + dp5);

            today1 = SLibTimeUtils.createDate(2026, 1, 27);
            SSomUtils.DateProps dp6 = new SSomUtils.DateProps(session, today1, SCliConsts.FRUIT_FIRST_YEAR, 7, 26, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp6:\n" + dp6);
            
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Season months in dp1:\n" + dp1.composeOpSeasonMonthsBoundariesAsString());
            System.out.println("Season months in dp2:\n" + dp2.composeOpSeasonMonthsBoundariesAsString());
            System.out.println("Season months in dp3:\n" + dp3.composeOpSeasonMonthsBoundariesAsString());
            System.out.println("Season months in dp4:\n" + dp4.composeOpSeasonMonthsBoundariesAsString());
            System.out.println("Season months in dp5:\n" + dp5.composeOpSeasonMonthsBoundariesAsString());
            System.out.println("Season months in dp6:\n" + dp6.composeOpSeasonMonthsBoundariesAsString());
            
            System.out.println();
            System.out.println("================================================================================");
            System.out.println("*** \"Hoy\" a finales de septiembre ***");
            
            Date today2 = SLibTimeUtils.createDate(2026, 9, 30);

            SSomUtils.DateProps dp11 = new SSomUtils.DateProps(session, today2, SCliConsts.FRUIT_FIRST_YEAR, 1, 1, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp11:\n" + dp11);

            SSomUtils.DateProps dp12 = new SSomUtils.DateProps(session, today2, SCliConsts.FRUIT_FIRST_YEAR, 9, 1, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp12:\n" + dp12);

            SSomUtils.DateProps dp13 = new SSomUtils.DateProps(session, today2, SCliConsts.FRUIT_FIRST_YEAR, 7, 25, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp13:\n" + dp13);

            SSomUtils.DateProps dp14 = new SSomUtils.DateProps(session, today2, SCliConsts.FRUIT_FIRST_YEAR, 7, 26, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp14:\n" + dp14);

            SSomUtils.DateProps dp15 = new SSomUtils.DateProps(session, today2, SCliConsts.FRUIT_FIRST_YEAR, 7, 27, SCliConsts.FRUIT_BY_OP_CALENDARS, targetItemId);
            System.out.println("dp15:\n" + dp15);
            
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Season months boundaries for dp11:\n" + dp11.composeOpSeasonMonthsBoundariesAsString());
            System.out.println("Season months boundaries for dp12:\n" + dp12.composeOpSeasonMonthsBoundariesAsString());
            System.out.println("Season months boundaries for dp13:\n" + dp13.composeOpSeasonMonthsBoundariesAsString());
            System.out.println("Season months boundaries for dp14:\n" + dp14.composeOpSeasonMonthsBoundariesAsString());
            System.out.println("Season months boundaries for dp15:\n" + dp15.composeOpSeasonMonthsBoundariesAsString());
            
            System.out.println("================================================================================");
            System.out.println("*** Límites de histórico de temporadas para fechas con \"hoy\" a finales de enero ***");
            System.out.println("Seasons boundaries for dp1:\n" + dp1.composeOpSeasonsBoundariesAsString());
            System.out.println("Seasons boundaries for dp2:\n" + dp2.composeOpSeasonsBoundariesAsString());
            System.out.println("Seasons boundaries for dp3:\n" + dp3.composeOpSeasonsBoundariesAsString());
            System.out.println("Seasons boundaries for dp4:\n" + dp4.composeOpSeasonsBoundariesAsString());
            System.out.println("Seasons boundaries for dp5:\n" + dp5.composeOpSeasonsBoundariesAsString());
            System.out.println("Seasons boundaries for dp6:\n" + dp6.composeOpSeasonsBoundariesAsString());
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}
