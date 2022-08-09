/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 * Helps mail notification processing by managing region and supraregion reception totals by item, unit and notification box (mail recipients).
 * @author Sergio Flores
 */
public class SSomReportProcessor {

    private ArrayList<Entry> maEntries;

    public SSomReportProcessor() {
        maEntries = new ArrayList<>();
    }

    public void addEntry(String notificationBox, int[] item, int unit, double reception) {
        Entry entry = getEntry(notificationBox, item, unit);

        if (entry != null) {
            entry.Reception = reception;
        }
        else {
            entry = new Entry(notificationBox, item, unit, reception);
            maEntries.add(entry);
        }
    }

    public Entry getEntry(String notificationBox, int[] item, int unit) {
        Entry foundEntry = null;

        for (Entry entry : maEntries) {
            if (entry.NotificationBox.compareTo(notificationBox) == 0 && SLibUtils.compareKeys(entry.Item, item) && entry.Unit == unit) {
                foundEntry = entry;
                break;
            }
        }

        return foundEntry;
    }

    public class Entry {
        public Entry(String notificationBox, int[] item, int unit, double reception) {
            NotificationBox = notificationBox;
            Item = item;
            Unit = unit;
            Reception = reception;
        }

        String NotificationBox;
        int[] Item;
        int Unit;
        double Reception;
    }
}
