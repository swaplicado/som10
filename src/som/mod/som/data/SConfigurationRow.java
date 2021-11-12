/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.data;

/**
 *
 * @author Edwin Carmona
 */
public class SConfigurationRow {
    private boolean isActive;
    private String label;


    // Getter Methods 

    public boolean getIsActive() {
        return isActive;
    }

    public String getLabel() {
        return label;
    }

    // Setter Methods 

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
