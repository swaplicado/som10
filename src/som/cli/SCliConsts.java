/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores
 */
public abstract class SCliConsts {
    
    public static final int ID_AVO_FRUIT_CONV = 6; // aguacate, fruta convencional
    public static final int ID_AVO_FRUIT_ORG = 64; // aguacate, fruta orgánica
    public static final int ID_AVO_CHAFF = 23; // aguacate, bagazo
    public static final int ID_AVO_PELLET = 99; // aguacate, pellet
    public static final int ID_AVO_KERNEL = 100; // aguacate, hueso y cáscara (HyC)
    public static final int ID_AVO_PULP = 103; // aguacate, pulpa
    public static final int ID_MAN_KERNEL = 197; // mango, hueso y cáscara (HyC)
    
    public static final HashMap<Integer, String> ItemNames = new HashMap<>();
    
    static {
        ItemNames.put(ID_AVO_FRUIT_CONV, "Fruta aguacate");
        ItemNames.put(ID_AVO_FRUIT_ORG, "Fruta aguacate orgánico");
        ItemNames.put(ID_AVO_CHAFF, "Bagazo aguacate");
        ItemNames.put(ID_AVO_PELLET, "Pellet aguacate");
        ItemNames.put(ID_AVO_KERNEL, "Hueso y cáscara aguacate");
        ItemNames.put(ID_AVO_PULP, "Pulpa aguacate");
        ItemNames.put(ID_MAN_KERNEL, "Hueso y cáscara mango");
    }
    
    public static final String IDS_PAIR_AVO_FRUIT_CNV_ORG = ID_AVO_FRUIT_CONV + "-" + ID_AVO_FRUIT_ORG;
    
    public static final HashMap<String, String> ItemsPairsNames = new HashMap<>();
    
    static {
        ItemsPairsNames.put(IDS_PAIR_AVO_FRUIT_CNV_ORG, "Aguacate convencional y orgánico");
    }
    
    /** Mmáximo día de inicio de mes operativo. */
    public static final int MAX_MONTH_START_DAY = 27;
    
    /** Día de corte de mes operativo: 19. */
    public static final int FRUIT_MONTH_FIRST_DAY = 19;
    
    /** Primer mes operativo de temporada de fruta: julio. Aplica a la fecha de corte del primer mes operativo de la temporada. */
    public static final int FRUIT_SEASON_FIRST_MONTH = 7;
    
    /** Primer año de recepción de fruta: 2010. */
    public static final int FRUIT_FIRST_YEAR = 2010;
}
