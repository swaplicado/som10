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
    
    String result08;
    String result10;
    String result12;
    String result14;
    String result16;
    String result18;
    String result20;
    String result22;
    String result00;
    String result02;
    String result04;
    String result06;
    
    ArrayList<SDbLinkGrindingFormula> formulas;
}
