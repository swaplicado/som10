/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiClient;
import som.mod.SModConsts;
import som.mod.som.db.SDbGrindingItemParameterHeader;
import som.mod.som.db.SDbGrindingEvent;

/**
 *
 * @author Edwin Carmona
 */
public class SGrindingResume {
    
    public static ArrayList<SGrindingResumeRow> getResumeRows(SGuiClient client, Date dtDate, final int nItemId) {
        ArrayList<SGrindingResumeRow> grindingRows = new ArrayList<>();
        
        if (! SGrindingResume.withHeaders(client, nItemId)) {
            return new ArrayList<>();
        }
        
        /**
         * Si hay configuraciones para el ítem actual determina el valor promedio y las carga
         */
        ArrayList<SDbGrindingItemParameterHeader> lHeaders = SGrindingResume.getHeaders(client, nItemId);
        if (! lHeaders.isEmpty()) {
            SGrindingResumeRow row;
            for (SDbGrindingItemParameterHeader oHeader : lHeaders) {
                row = new SGrindingResumeRow();
                row.setDataName(oHeader.getLabelText());
                
                double averageSum = 0d;
                for (int auxParameter : oHeader.getAuxParameters()) {
                    averageSum += SGrindingResultsUtils.getMonthAverage(client, dtDate, nItemId, auxParameter);
                }
                
                row.setValue(oHeader.getAuxParameters().length == 0 ? 0d : averageSum / oHeader.getAuxParameters().length);
                row.setUnit(oHeader.getUnitSimbol());
                
                grindingRows.add(row);
            }
            
            return grindingRows;
        }
        
        boolean onlyCurrentMonth = false;
        ArrayList<SGrindingData> grindingOfMonths = SGrindingResultsUtils.getGrindingByMonth(client, dtDate, nItemId, onlyCurrentMonth);
        
        double dTotalGrindingBascule = 0d;
        double dTotalGrindingOilPercent = 0d;
        for (SGrindingData grindingOfMonth : grindingOfMonths) {
            SGrindingResumeRow row = new SGrindingResumeRow();
            
            row.setDataName("Molienda " + grindingOfMonth.getMonthText());
            row.setValue(grindingOfMonth.getSumGrindingOilPercent());
            row.setUnit("kg");
            
            grindingRows.add(row);
            
            SGrindingResumeRow row1 = new SGrindingResumeRow();
            
            row1.setDataName("Molienda " + grindingOfMonth.getMonthText() + " báscula");
            row1.setValue(grindingOfMonth.getSumGrindingBascule());
            row1.setUnit("kg");
            
            grindingRows.add(row1);
            
            dTotalGrindingOilPercent += grindingOfMonth.getSumGrindingOilPercent();
            dTotalGrindingBascule += grindingOfMonth.getSumGrindingBascule();
        }
        
        String [] dataRows = {
                            "Prom. Mensual Residual 9C",
                            "Prom. Mensual Residual Prensa",
                            "Prom. Mensual Cont. Ac.Semilla",
                            "Prom. Mensual Proteína",
                            "Molienda total x dif aceite",
                            "Molienda total  Báscula",
                        };
        
        for (int i = 0; i < dataRows.length; i++) {
            String string = dataRows[i];
            
            SGrindingResumeRow row = new SGrindingResumeRow();
            row.setDataName(string);
            
            switch (i) {
                case 0:
                    row.setValue(SGrindingResultsUtils.getMonthAverage(client, dtDate, nItemId, SGrindingResultsUtils.RESID_PASTA));
                    row.setUnit("%");
                    break;
                case 1:
                    row.setValue(SGrindingResultsUtils.getMonthAverage(client, dtDate, nItemId, SGrindingResultsUtils.RESID_PRENSA));
                    row.setUnit(" ");
                    break;
                case 2:
                    row.setValue(SGrindingResultsUtils.getMonthAverage(client, dtDate, nItemId, SGrindingResultsUtils.CONT_ACEITE_SEM));
                    row.setUnit("%");
                    break;
                case 3:
                    row.setValue(SGrindingResultsUtils.getMonthAverage(client, dtDate, nItemId, SGrindingResultsUtils.PROTEINA));
                    row.setUnit("%");
                    break;
                case 4:
                    row.setValue(dTotalGrindingOilPercent);
                    row.setUnit("kg");
                    break;
                case 5:
                    row.setValue(dTotalGrindingBascule);
                    row.setUnit("kg");
                    break;
                    
                default:
                    row.setValue(0d);
                    row.setUnit("");
            }
            
            grindingRows.add(row);
        }
        
        boolean onlyCurrentDay = false;
        boolean isWeightedAverage = true;
        double dWeightedAvgContSem =  SGrindingResultsUtils.getWeightedAverage(client, dtDate, dTotalGrindingOilPercent, 
                                                        nItemId, SGrindingResultsUtils.CONT_ACEITE_SEM, onlyCurrentDay, isWeightedAverage);
        SGrindingResumeRow row1 = new SGrindingResumeRow();
            
        row1.setDataName("Ponderado Cont. Aceite Semilla");
        row1.setValue(dWeightedAvgContSem);
        row1.setUnit("%");

        grindingRows.add(row1);
        
        double dWeightedAvgResPasta =  SGrindingResultsUtils.getWeightedAverage(client, dtDate, dTotalGrindingOilPercent, 
                                                            nItemId, SGrindingResultsUtils.RESID_PASTA, onlyCurrentDay, isWeightedAverage);
        
        SGrindingResumeRow row = new SGrindingResumeRow();
            
        row.setDataName("Ponderado Residual Promedio 9C");
        row.setValue(dWeightedAvgResPasta);
        row.setUnit("%");

        grindingRows.add(row);
        
        SGrindingResumeRow rowRt = new SGrindingResumeRow();
            
        rowRt.setDataName("Ponderado rendmiento teórico");
        rowRt.setValue(1 - (1 - dWeightedAvgContSem)/(1 - dWeightedAvgResPasta));
        rowRt.setUnit("%");

        grindingRows.add(rowRt);
        
        double resPasta = SGrindingResultsUtils.getWeightedAverage(client, dtDate, 0d, 
                                                            nItemId, SGrindingResultsUtils.RESID_PASTA, true, false);
        double conAcSem = SGrindingResultsUtils.getWeightedAverage(client, dtDate, 0d, 
                                                            nItemId, SGrindingResultsUtils.CONT_ACEITE_SEM, true, false);
        
        SGrindingResumeRow rowRendt = new SGrindingResumeRow();
            
        rowRendt.setDataName("Rendimiento teórico");
        rowRendt.setValue((1 - (1 - (conAcSem / 100)) / (1 - (resPasta / 100))) * 100);
        rowRendt.setUnit("%");

        grindingRows.add(rowRendt);
        
        return grindingRows;
    }
    
    public static ArrayList<SDbGrindingEvent> getGrindingEvents(SGuiClient client, Date start, Date end, int idItem) {
        DateFormat fileNameformatter = new SimpleDateFormat("yyy-MM-dd");
        
        String sql = "SELECT "
                + "    * "
                + "FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.S_GRINDING_EVENT) + " AS ev "
                + "WHERE "
                + " NOT ev.b_del "
                + " AND (ev.dt_start BETWEEN '" + fileNameformatter.format(start) + " 00:00:00' AND '" + fileNameformatter.format(end) + " 23:59:59' "
                + " OR ev.dt_end BETWEEN '" + fileNameformatter.format(start) + " 00:00:00' AND '" + fileNameformatter.format(end) + " 23:59:59') "
                + " AND ev.fk_item = " + idItem + " "
                + " ORDER BY ev.dt_start ASC;";
        
        ArrayList<SDbGrindingEvent> events = null;
        
        try {
            ResultSet result = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            events = new ArrayList<>();
            SDbGrindingEvent eventRow = null;
            while (result.next()) {
                eventRow = new SDbGrindingEvent();
                
                eventRow.setDateStart(result.getTimestamp("dt_start"));
                eventRow.setDateEnd(result.getTimestamp("dt_end"));
                eventRow.setDescription(result.getString("description"));
                eventRow.setPrimaryKey(new int[] { result.getInt("id_event") });
                eventRow.setRegistryNew(false);
                
                events.add(eventRow);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResume.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return events;
    }
    
    /**
     * Determina si el ítem recibido lleva o no encabezados.
     * Si un renglón con el nombre 'WITHOUT-HEADER' está asignado al ítem entonces se determina que
     * este no lleva encabezados.
     * 
     * @param client
     * @param nItemId
     * @return 
     */
    private static boolean withHeaders(SGuiClient client, final int nItemId) {
        String sql = "SELECT " +
                    " id_itm_prm " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_ITEM_PARAM_HEADER) + " " +
                    "WHERE " +
                    " NOT b_del " +
                    " AND label_text = 'WITHOUT-HEADER' " +
                    " AND fk_item_id = " + nItemId +
                    " ORDER BY view_order ASC " +
                    ";";
        
        try {
            ResultSet result = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
           return !result.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResume.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        catch (Exception ex) {
            Logger.getLogger(SGrindingResume.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Obtiene las configuraciones de encabezados para el ítem recibido
     * 
     * @param client
     * @param nItemId
     * @return 
     */
    private static ArrayList<SDbGrindingItemParameterHeader> getHeaders(SGuiClient client, final int nItemId) {
        String sql = "SELECT " +
                    " id_itm_prm " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_ITEM_PARAM_HEADER) + " " +
                    "WHERE " +
                    " NOT b_del " +
                    " AND parameters_ids <> '' " +
                    " AND label_text <> 'WITHOUT-HEADER' " +
                    " AND fk_item_id = " + nItemId +
                    " ORDER BY view_order ASC " +
                    ";";
        
        ArrayList<SDbGrindingItemParameterHeader> lHeaders = null;
        
        try {
            ResultSet result = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            lHeaders = new ArrayList<>();
            SDbGrindingItemParameterHeader headerRow = null;
            while (result.next()) {
                headerRow = new SDbGrindingItemParameterHeader();
                headerRow.read(client.getSession(), new int[] { result.getInt("id_itm_prm") });
                lHeaders.add(headerRow);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SGrindingResume.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
        catch (Exception ex) {
            Logger.getLogger(SGrindingResume.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
        
        return lHeaders;
    }
}
