/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

/**
 *
 * @author Néstor Ávalos
 */
public class SSomWarehouseItem {

    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnFkItemTypeId;
    protected String msItem;
    protected String msUnit;
    protected double mdStock;
    protected double mdDensity;

    public SSomWarehouseItem(final int nPkItemId, final int nPkUnitId, final int nFkItemTypeId,
        final String sItem, final String sUnit, final double dStock, final double dDensity) {

        mnPkItemId = nPkItemId;
        mnPkUnitId = nPkUnitId;
        mnFkItemTypeId = nFkItemTypeId;
        msItem = sItem;
        msUnit = sUnit;
        mdStock = dStock;
        mdDensity = dDensity;
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setFkItemTypeId(int n) { mnFkItemTypeId = n; }
    public void setItem(String s) { msItem = s; }
    public void setUnit(String s) { msUnit = s; }
    public void setStock(double d) { mdStock = d; }
    public void setDensity(double d) { mdDensity = d; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getFkItemTypeId() { return mnFkItemTypeId; }
    public String getItem() { return msItem; }
    public String getUnit() { return msUnit; }
    public double getStock() { return mdStock; }
    public double getDensity() { return mdDensity; }
}
