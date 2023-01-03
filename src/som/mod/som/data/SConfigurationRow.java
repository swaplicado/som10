/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Edwin Carmona
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SConfigurationRow {
    private boolean isActive;
    private String label;
    private String defaultValue;

    // Getter Methods 

    public boolean getIsActive() {
        return isActive;
    }

    public String getLabel() {
        return label;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
    
    // Setter Methods 

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
