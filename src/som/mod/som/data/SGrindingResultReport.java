/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

import java.util.ArrayList;
import java.util.Date;
import som.mod.cfg.db.SDbLinkGrindingFormula;

/**
 *
 * @author Edwin Carmona
 */
public class SGrindingResultReport {

    public SGrindingResultReport() {
        formulas = new ArrayList<>();
    }
    
    String itemCode;
    String itemName;
    Date dtCapture;
    
    int parameterId;
    boolean isText;
    String defaultTextValue;
    
    int itemParamLinkId;
    
    String parameterCode;
    String parameterName;
    
    double result08;
    double result10;
    double result12;
    double result14;
    double result16;
    double result18;
    double result20;
    double result22;
    double result00;
    double result02;
    double result04;
    double result06;
    
    ArrayList<SDbLinkGrindingFormula> formulas;
}
