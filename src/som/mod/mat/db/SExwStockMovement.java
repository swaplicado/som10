/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

import java.util.Date;
import sa.lib.grid.SGridRow;
import som.mod.SModConsts;

/**
 *
 * @author Sergio Flores
 */
public class SExwStockMovement implements SGridRow {
    
    public static final int OPEN_STOCK = 1;
    
    /** 1) Opening stock; or 2) scale ticket; or 3) external warehouse adjustment. */
    public int MovementType;
    
    public int ScaleId;
    public String ScaleName;
    public String ScaleCode;
    
    public int ItemId;
    public String ItemCode;
    public String ItemName;
    
    public int UnitId;
    public String UnitName;
    public String UnitCode;
    
    public int ExwFacilityId;
    public String ExwFacilityName;
    public String ExwFacilityCode;
    
    public int MovementId;
    public Date MovementDate;
    
    public int IogCategoryId;
    public String IogCategoryName;
    public String IogCategoryCode;
    
    public double Quantity;
    
    public int TicketId;
    public String TicketFolio;
    
    public int ExwAdjustmentId;
    public String ExwAdjustmentFolio;
    public String ExwAdjustmentReference;
    public String ExwAdjustmentNote;
    public String ExwAdjustmentType;
    
    public int UserInsertId; // either of scale ticket or external warehouse adjustment
    public String UserInsertName;
    public Date UserInsertTs;
    
    public int UserUpdateId; // either of scale ticket or external warehouse adjustment
    public String UserUpdteName;
    public Date UserUpdteTs;

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { ScaleId, ExwFacilityId, ItemId, UnitId, MovementId };
    }

    @Override
    public String getRowCode() {
        String code = "";
        
        switch (MovementType) {
            case SModConsts.S_TIC:
                code = TicketFolio;
                break;
            case SModConsts.M_EXW_ADJ:
                code = ExwAdjustmentFolio;
                break;
            default:
        }
        
        return code;
    }

    @Override
    public String getRowName() {
        return getRowCode();
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        
    }
}
