/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomStock {

    protected String msItem;
    protected String msUnit;
    protected double mdStock;
    protected String msResult;

    public SSomStock(final String sItem, final String sUnit, final double dStock, final String sResult) {

        msItem = sItem;
        msUnit = sUnit;
        mdStock = dStock;
        msResult = sResult;
    }

    public void setItem(String s) { msItem = s; }
    public void setUnit(String s) { msUnit = s; }
    public void setStock(double d) { mdStock = d; }
    public void setResult(String s) { msResult = s; }

    public String getItem() { return msItem; }
    public String getUnit() { return msUnit; }
    public double getStock() { return mdStock; }
    public String getResult() { return msResult; }
}
