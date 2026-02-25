/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import som.mod.som.db.SOpCalendarUtils;

/**
 *
 * @author SERGIO_FLORES
 */
public class TestOpCalendarUtils {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SGuiSession session = new SGuiSession(null);
            
            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);

            database.connect(
                    "localhost",
                    "3306",
                    "som_com",
                    "root",
                    "msroot");
            
            session.setDatabase(database);

            for (int id : new int[] { 99, 103, 256 }) {
                System.out.println(SLibUtils.textRepeat("=", 80));
                System.out.println("ITEM: " + id);
                
                for (Date date : new Date[] { SLibTimeUtils.createDate(2009, 1, 1), SLibTimeUtils.createDate(2010, 6, 21), SLibTimeUtils.createDate(2011, 6, 20), SLibTimeUtils.createDate(2019, 9, 1), SLibTimeUtils.createDate(2031, 4, 2)}) {
                    int[] key = SOpCalendarUtils.getOpCalendarMonthKey(session, id, date);
                    System.out.println("ID: " + id + "; date: " + SLibUtils.DateFormatDate.format(date) + "; key: " + (key == null ? "null" : SLibUtils.textKey(key)));
                }
            }
        }
        catch (Exception e) {
            SLibUtils.printException(TestOpCalendarUtils.class.getName(), e);
        }
    }
}
