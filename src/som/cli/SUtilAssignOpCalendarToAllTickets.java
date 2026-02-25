/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import som.mod.som.db.SOpCalendarUtils;

/**
 *
 * @author Sergio Flores
 */
public class SUtilAssignOpCalendarToAllTickets {

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
            
            SOpCalendarUtils.assignOpCalendarToAllTickets(session);
        }
        catch (Exception e) {
            SLibUtils.printException(SUtilAssignOpCalendarToAllTickets.class.getName(), e);
        }
    }
    
}
