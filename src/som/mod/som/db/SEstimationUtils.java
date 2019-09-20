/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;

/**
 *
 * @author Edwin Carmona
 *
 */
public class SEstimationUtils {

    /**
     * Recalcula los valores de materia prima consumida, desechos y subproducto resultante en las tablas de
     * renglones de la estimación, consumos de la estimación y en la estimación misma.
     * 
     * @param client
     * @param estimationId pk de la estimación
     * @param rawMaterialId id del item de materia prima correspondiente
     * @param rMConsumptionId pk del renglón de consumo de la estimación
     * @param oilPercentage porcentaje de aceite comprendido del  0 al 1
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public static boolean changeOilPercentage(SGuiClient client, final int estimationId, 
                                                final int rawMaterialId, final int rMConsumptionId, 
                                                final double oilPercentage) throws SQLException, Exception {
        String query = "SELECT id_ety, mfg_fg FROM som_com." + SModConsts.TablesMap.get(SModConsts.S_MFG_EST_ETY)
                + " WHERE id_mfg_est = " + estimationId + " AND fk_item_con_rm = " + rawMaterialId + ";";

        //lectura del ítem de materia prima
        SDbItem item = new SDbItem();
        item.read(client.getSession(), new int[] { rawMaterialId });
                
        ArrayList<SDbMfgEstimationEntry> etys = new ArrayList<>();
        SDbMfgEstimationEntry entry;
        double rawMaterialTotal = 0d;
        double subProductTotal = 0d;
        double cullTotal = 0d;
        double otherPercentages = item.getMfgByproductPercentage() + item.getMfgCullPercentage();
        double diffPercentages = 1d - oilPercentage;
        double subProdPercentage = (item.getMfgByproductPercentage() / otherPercentages) * diffPercentages;
        double cullPercentage = (item.getMfgCullPercentage() / otherPercentages) * diffPercentages;
        double oilQuantity;
        double rawMaterial;
        
        // lectura de los renglones de la estimación para determinar las cantidades
        // con los nuevos porcentajes
        
        ResultSet res = client.getSysStatement().executeQuery(query);
        while (res.next()) {
            entry = new SDbMfgEstimationEntry();
            
            entry.setPkEntryId(res.getInt("id_ety"));
            entry.setPkMfgEstimationId(estimationId);
            entry.setMfgFinishedGood(res.getDouble("mfg_fg"));
            
            oilQuantity = res.getDouble("mfg_fg");
            rawMaterial = oilQuantity / oilPercentage;
            
            entry.setConsumptionRawMaterial(rawMaterial);
            entry.setMfgByproduct(rawMaterial * subProdPercentage);
            entry.setMfgCull(rawMaterial * cullPercentage);
            
            rawMaterialTotal += rawMaterial;
            subProductTotal += entry.getMfgByproduct();
            cullTotal += entry.getMfgCull();
                    
            etys.add(entry);
        }
        
        // Actualizar renglones de consumos de materia prima de la estimación
        
        String sUpdConsumption = "UPDATE som_com.s_mfg_est_rm_con " +
                                    "SET " +
                                    "    oil_per = " + oilPercentage + "," +
                                    "    mfg_bp = " + subProductTotal + "," +
                                    "    mfg_cu = " + cullTotal + "," +
                                    "    con_rm = " + rawMaterialTotal + " " +
                                    "WHERE" +
                                    "    id_mfg_est = " + estimationId + " " +
                                    "AND id_rm_con = " + rMConsumptionId + ";";
        
        client.getSession().getStatement().executeUpdate(sUpdConsumption);
        
        // Actualizar renglones de la estimación
        
        updateEstimationEtys(client.getSession(), etys);
        
        // Actualizar estimación
        
        updateEstimation(client.getSession(), estimationId);
        
        return true;
    }
    
    /**
     * Cambia los valores de la cantidad de sub producto, materia prima y desecho.
     * La llave primaria de los objetos en la lista recibida debe estar completa para
     * poder ralizar la actualización.
     * 
     * Los objetos en la lista pueden pertenecer a diferentes estimaciones.
     * 
     * @param session
     * @param etys
     * @return
     * @throws SQLException 
     */
    private static boolean updateEstimationEtys(SGuiSession session, ArrayList<SDbMfgEstimationEntry> etys) throws SQLException {
        String sUpdEty;
        for (SDbMfgEstimationEntry ety : etys) {
            sUpdEty = "UPDATE som_com.s_mfg_est_ety " +
                        "SET " +
                        "    mfg_bp = " + ety.getMfgByproduct() + "," +
                        "    mfg_cu = " + ety.getMfgCull() + "," +
                        "    con_rm = " + ety.getConsumptionRawMaterial() + " " +
                        "WHERE " +
                        "    id_mfg_est = " + ety.getPkEntryId() + 
                        " AND id_ety = " + ety.getPkEntryId() + " ;";
            
            session.getStatement().executeUpdate(sUpdEty);
        }
        
        return true;
    }
    
    /**
     * Actualiza únicamente los valores de las cantidades de subproducto y desecho resultantes en la estimación
     * 
     * @param session
     * @param estimationId
     * @return
     * @throws SQLException 
     */
    private static boolean updateEstimation(SGuiSession session, final int estimationId) throws SQLException {
        String sGetConsumptions = "SELECT * FROM som_com.s_mfg_est_rm_con WHERE id_mfg_est = " + estimationId + ";";
        double subProdEstTotal = 0d;
        double cullEstTotal = 0d;
        
        ResultSet consumptions = session.getStatement().executeQuery(sGetConsumptions);
        while(consumptions.next()) {
            subProdEstTotal += consumptions.getDouble("mfg_bp");
            cullEstTotal += consumptions.getDouble("mfg_cu");
        }
        
        String sUpdEstimation = "UPDATE som_com.s_mfg_est " +
                                "SET " +
                                "    mfg_bp_r = " + subProdEstTotal + "," +
                                "    mfg_cu_r = " + cullEstTotal + " " +
                                "WHERE " +
                                "    (id_mfg_est = " + estimationId + ");";
        
        session.getStatement().executeUpdate(sUpdEstimation);
        
        return true;
    }
}
