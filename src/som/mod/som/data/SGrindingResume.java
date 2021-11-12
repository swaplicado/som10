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
import som.mod.som.db.SDbGrindingEvent;

/**
 *
 * @author Edwin Carmona
 */
public class SGrindingResume {
    
    public static ArrayList<SGrindingResumeRow> getResumeRows(SGuiClient client, Date dtDate, final int nItemId) {
        ArrayList<SGrindingResumeRow> grindingRows = new ArrayList<>();
        
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
    
    public static ArrayList<SDbGrindingEvent> getGrindingEvents(SGuiClient client, Date start, Date end) {
        DateFormat fileNameformatter = new SimpleDateFormat("yyy-MM-dd");
        
        String sql = "SELECT " +
                    "    * " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.SU_GRINDING_EVENT) + " " +
                    "WHERE " +
                    "    NOT b_del " +
                    "    AND dt_start BETWEEN '" + fileNameformatter.format(start) + " 00:00:00' AND '" + fileNameformatter.format(end) + " 23:59:59'\n" +
                    "    OR dt_end BETWEEN '" + fileNameformatter.format(start) + " 00:00:00' AND '" + fileNameformatter.format(end) + " 23:59:59';";
        
        ArrayList<SDbGrindingEvent> events = null;
        
        try {
            ResultSet result = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            events = new ArrayList<>();
            SDbGrindingEvent eventRow = null;
            while (result.next()) {
                eventRow = new SDbGrindingEvent();
                
                eventRow.setStartDate(result.getTimestamp("dt_start"));
                eventRow.setEndDate(result.getTimestamp("dt_end"));
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
}
