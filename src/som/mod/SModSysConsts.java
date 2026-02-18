/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod;

import java.util.ArrayList;
import java.util.HashMap;
import sa.lib.gui.SGuiItem;

/**
 *
 * @author Sergio Flores, Isabel Servín, Edwin Carmona, Sergio Flores
 */
public abstract class SModSysConsts {
    
    public static final String C_PARAM_MAT_WAH_DEF = "MAT_WAH_DEF";
    public static final String C_PARAM_MAT_ITEMS = "MAT_ITEMS";
    public static final String C_PARAM_MAT_STK_DAYS_SIGNAL = "MAT_STK_DAYS_SIGNAL";
    public static final String C_PARAM_MAT_DAY_CUTOFF_HR = "MAT_DAY_CUTOFF_HR";
    public static final String C_PARAM_MAT_WEEK_START_DAY = "MAT_WEEK_START_DAY";
    public static final String C_PARAM_MAT_UNIT_DEF = "MAT_UNIT_DEF";
    public static final String C_PARAM_MAT_SCA_DEF = "MAT_SCA_DEF";

    public static final int CS_WAH_TP_WAH = 1;
    public static final int CS_WAH_TP_SIL = 2;
    public static final int CS_WAH_TP_TAN = 3;
    public static final int CS_WAH_TP_TAN_MFG = 4;
    
    public static final int CS_WAH_ORI_VER = 1;
    public static final int CS_WAH_ORI_HOR = 2;
    
    public static final int CS_WAH_CALC_TP_EMPTY_MSRE = 1;
    public static final int CS_WAH_CALC_TP_FULL_MSRE = 2;
    public static final int CS_WAH_CALC_TP_EMPTY_PERC = 3;
    public static final int CS_WAH_CALC_TP_FULL_PERC = 4;
    public static final int CS_WAH_CALC_TP_DIR_DATA = 5;

    public static final int CS_PLA_TP_PLA = 1;
    
    public static final int CS_USR_TP_USR = 1;
    public static final int CS_USR_TP_ADM = 2;
    public static final int CS_USR_TP_SUP = 3;

    public static final int CS_RIG_MAN_RM = 1; // manager raw materials
    public static final int CS_RIG_MAN_OM = 2; // manager oil and meals
    public static final int CS_RIG_WHS_RM = 3; // warehouser raw materials
    public static final int CS_RIG_WHS_OM = 4; // warehouser oil and meals
    public static final int CS_RIG_REP_RM = 5; // reports raw materials
    public static final int CS_RIG_REP_OM = 6; // reports oil and meals
    public static final int CS_RIG_LAB_SUP = 7; // laboratorist supervisor
    public static final int CS_RIG_LAB = 8; // laboratorist
    public static final int CS_RIG_SCA_SUP = 9 ; // scale supervisor
    public static final int CS_RIG_SCA = 10; // scale operator
    public static final int CS_RIG_PER_OC = 11; // periods opening and closing
    public static final int CS_RIG_DIS_RM = 12; // discharge raw materials
    public static final int CS_RIG_LOG = 13; // logistics
    public static final int CS_RIG_LAB_VAL = 14; // laboratory results validation
    public static final int CS_RIG_RM_STK_DEO = 15; // raw material existences data entry operator
    public static final int CS_RIG_RM_STK_SUP = 16; // raw material existences supervisor
    public static final int CS_RIG_RM_STK_MAN = 17; // raw material existences manager

    public static final int CS_ALT_RIG_PUR = 1; // purchase
    public static final int CS_ALT_RIG_QTY = 2; // quality
    
    public static final int CU_LAN_SPA = 1;
    public static final int CU_LAN_ENG = 2;

    public static final int CU_DIV_DEF = 1; // division
    
    public static final int CU_REP_GRP_DEF = 1; // reporting group
    
    public static final int[] CU_PLA_NA = new int[] { 1, 1, 1 }; // valor por defecto de planta NA
    public static final int[] CU_PLA_PYE = new int[] { 1, 1, 2 }; // valor por defecto de planta Prensas y Extracción
    public static final int[] CU_PLA_AGU = new int[] { 1, 1, 3 }; // valor por defecto de planta Aguacate
    public static final int[] CU_PLA_REF = new int[] { 1, 1, 4 }; // valor por defecto de planta Refinería
    
    public static final int CU_PLA_INT_NA = 1; // valor por defecto de planta NA
    public static final int CU_PLA_INT_PYE = 2; // valor por defecto de planta Prensas y Extracción
    public static final int CU_PLA_INT_AGU = 3; // valor por defecto de planta Aguacate
    public static final int CU_PLA_INT_REF = 4; // valor por defecto de planta Refinería
    
    public static final int SS_TIC_ST_SCA = 1;
    public static final int SS_TIC_ST_LAB = 2;
    public static final int SS_TIC_ST_ADM = 3;
    public static final int SS_TIC_ST_ALL_LOG = 4;
    
    public static final int SS_TIC_WAH_UNLD_N_ASIGNED = 1;
    public static final int SS_TIC_WAH_UNLD_ASIGNED = 2;

    public static final int SS_IOG_CT_IN = 1;
    public static final int SS_IOG_CT_OUT = 2;

    public static final int[] SS_IOG_CL_IN_PUR = new int[] { 1, 1 };
    public static final int[] SS_IOG_CL_IN_SAL = new int[] { 1, 2 };
    public static final int[] SS_IOG_CL_IN_EXT = new int[] { 1, 3 };
    public static final int[] SS_IOG_CL_IN_INT = new int[] { 1, 4 };
    public static final int[] SS_IOG_CL_IN_MFG = new int[] { 1, 5 };
    public static final int[] SS_IOG_CL_OUT_PUR = new int[] { 2, 1 };
    public static final int[] SS_IOG_CL_OUT_SAL = new int[] { 2, 2 };
    public static final int[] SS_IOG_CL_OUT_EXT = new int[] { 2, 3 };
    public static final int[] SS_IOG_CL_OUT_INT = new int[] { 2, 4 };
    public static final int[] SS_IOG_CL_OUT_MFG = new int[] { 2, 5 };

    public static final int[] SS_IOG_TP_IN_PUR_PUR = new int[] { 1, 1, 1 };
    public static final int[] SS_IOG_TP_IN_PUR_CHG = new int[] { 1, 1, 2 };
    public static final int[] SS_IOG_TP_IN_PUR_WAR = new int[] { 1, 1, 3 };
    public static final int[] SS_IOG_TP_IN_PUR_CSG = new int[] { 1, 1, 4 };
    public static final int[] SS_IOG_TP_IN_SAL_SAL = new int[] { 1, 2, 1 };
    public static final int[] SS_IOG_TP_IN_SAL_CHG = new int[] { 1, 2, 2 };
    public static final int[] SS_IOG_TP_IN_SAL_WAR = new int[] { 1, 2, 3 };
    public static final int[] SS_IOG_TP_IN_SAL_CSG = new int[] { 1, 2, 4 };
    public static final int[] SS_IOG_TP_IN_EXT_ADJ = new int[] { 1, 3, 1 };
    public static final int[] SS_IOG_TP_IN_EXT_INV = new int[] { 1, 3, 2 };
    public static final int[] SS_IOG_TP_IN_INT_TRA = new int[] { 1, 4, 1 };
    public static final int[] SS_IOG_TP_IN_INT_CNV = new int[] { 1, 4, 2 };
    public static final int[] SS_IOG_TP_IN_INT_MIX_PAS = new int[] { 1, 4, 3 };
    public static final int[] SS_IOG_TP_IN_INT_MIX_ACT = new int[] { 1, 4, 4 };
    public static final int[] SS_IOG_TP_IN_MFG_RM_ASD = new int[] { 1, 5, 1 };
    public static final int[] SS_IOG_TP_IN_MFG_RM_RET = new int[] { 1, 5, 2 };
    public static final int[] SS_IOG_TP_IN_MFG_WP_ASD = new int[] { 1, 5, 3 };
    public static final int[] SS_IOG_TP_IN_MFG_WP_RET = new int[] { 1, 5, 4 };
    public static final int[] SS_IOG_TP_IN_MFG_FG_ASD = new int[] { 1, 5, 5 };
    public static final int[] SS_IOG_TP_IN_MFG_FG_RET = new int[] { 1, 5, 6 };
    public static final int[] SS_IOG_TP_OUT_PUR_PUR = new int[] { 2, 1, 1 };
    public static final int[] SS_IOG_TP_OUT_PUR_CHG = new int[] { 2, 1, 2 };
    public static final int[] SS_IOG_TP_OUT_PUR_WAR = new int[] { 2, 1, 3 };
    public static final int[] SS_IOG_TP_OUT_PUR_CSG = new int[] { 2, 1, 4 };
    public static final int[] SS_IOG_TP_OUT_SAL_SAL = new int[] { 2, 2, 1 };
    public static final int[] SS_IOG_TP_OUT_SAL_CHG = new int[] { 2, 2, 2 };
    public static final int[] SS_IOG_TP_OUT_SAL_WAR = new int[] { 2, 2, 3 };
    public static final int[] SS_IOG_TP_OUT_SAL_CSG = new int[] { 2, 2, 4 };
    public static final int[] SS_IOG_TP_OUT_EXT_ADJ = new int[] { 2, 3, 1 };
    public static final int[] SS_IOG_TP_OUT_EXT_INV = new int[] { 2, 3, 2 };
    public static final int[] SS_IOG_TP_OUT_INT_TRA = new int[] { 2, 4, 1 };
    public static final int[] SS_IOG_TP_OUT_INT_CNV = new int[] { 2, 4, 2 };
    public static final int[] SS_IOG_TP_OUT_INT_MIX_PAS = new int[] { 2, 4, 3 };
    public static final int[] SS_IOG_TP_OUT_INT_MIX_ACT = new int[] { 2, 4, 4 };
    public static final int[] SS_IOG_TP_OUT_MFG_RM_ASD = new int[] { 2, 5, 1 };
    public static final int[] SS_IOG_TP_OUT_MFG_RM_RET = new int[] { 2, 5, 2 };
    public static final int[] SS_IOG_TP_OUT_MFG_WP_ASD = new int[] { 2, 5, 3 };
    public static final int[] SS_IOG_TP_OUT_MFG_WP_RET = new int[] { 2, 5, 4 };
    public static final int[] SS_IOG_TP_OUT_MFG_FG_ASD = new int[] { 2, 5, 5 };
    public static final int[] SS_IOG_TP_OUT_MFG_FG_RET = new int[] { 2, 5, 6 };

    public static final int SS_MIX_TP_NA = 1;
    public static final int SS_MIX_TP_CNV = 2;
    public static final int SS_MIX_TP_MIX_PAS = 3;
    public static final int SS_MIX_TP_MIX_ACT = 4;

    public static final int SS_ITEM_TP_RM = 1;
    public static final int SS_ITEM_TP_WP = 2;
    public static final int SS_ITEM_TP_FG = 3;
    public static final int SS_ITEM_TP_BP = 4;
    public static final int SS_ITEM_TP_CU = 5;
    
    /**
     * Warehouse attributes
     */
    
    public static final int SS_WHS_OR_VERTICAL = 1;
    public static final int SS_WHS_OR_HORIZONTAL = 2;
    
    public static final int SS_WHS_TP_CALC_EMPTY_MEASURE = 1;
    public static final int SS_WHS_TP_CALC_FULL_MEASURE = 2;
    public static final int SS_WHS_TP_CALC_EMPTY_PERCENT = 3;
    public static final int SS_WHS_TP_CALC_FULL_PERCENT = 4;
    public static final int SS_WHS_TP_CALC_DIRECT_DATA = 5;
    
    public static final int SX_ITEM_TP_FRUIT = 6;

    public static final int SU_IOG_ADJ_TP_NA = 1;

    public static final int SU_INP_CT_NA = 1;
    
    public static final int[] SU_INP_CL_NA = new int[] { 1, 1 };

    public static final int[] SU_INP_TP_NA = new int[] { 1, 1, 1 };
    
    public static final int SU_FUNC_AREA_PRE_EXT = 2;
    public static final int SU_FUNC_AREA_AVO = 3;
    public static final int SU_FUNC_AREA_REF = 4;
    
    public static final String SU_FUNC_AREA_TP_ADM = "A";
    public static final String SU_FUNC_AREA_TP_PLA = "P";
    public static final String SU_FUNC_AREA_TP_LAB = "L";
    
    public static final int SU_UNIT_NA = 1;
    public static final int SU_UNIT_MT_TON = 2;
    public static final int SU_UNIT_KG = 3;
    
    public static final int SU_ITEM_RM_AVO = 6;
    public static final int SU_ITEM_OIL_AVO = 16;
    
    public static final int SU_SCA_REV = 1; // Báscula revuelta    

    public static final int SU_INP_SRC_NA = 1;
    
    public static final int SU_BY_PRODUCT_NA = 1;
    
    public static final int SU_OIL_CL_AVO = 1;
    public static final int SU_OIL_CL_PRE_EXT = 2;
    
    public static final int SU_OIL_TP_CRU = 1;
    public static final int SU_OIL_TP_REF = 2;
    public static final int SU_OIL_TP_REP = 3;
    public static final int SU_OIL_TP_RES = 4;
    
    public static final HashMap<Integer, String> SU_OIL_TP_DESC;
    
    public static final int SU_OIL_GRP_FAM_SEEDS = 1;
    public static final int SU_OIL_GRP_FAM_AVO = 2; 
    public static final int SU_OIL_GRP_FAM_OTHER_SEEDS = 3;
    public static final int SU_OIL_GRP_FAM_OTHER_AVO = 4;
    
    public static final int SU_TIC_ORIG_NA = 1; // not applicable
    public static final int SU_TIC_ORIG_SUP = 11; // supplier
    public static final int SU_TIC_ORIG_EXW = 12; // external warehouse

    public static final int SU_TIC_DEST_NA = 1; // not applicable
    public static final int SU_TIC_DEST_FAC = 11; // factory
    public static final int SU_TIC_DEST_EXW = 12; // external warehouse
    
    public static final int SU_FREIGHT_ORIG_NA = 1;

    public static final int SX_DEN = 1;
    public static final int SX_IOD_VAL = 2;
    public static final int SX_REF_IND = 3;
    public static final int SX_IMP_PER = 4;
    public static final int SX_MOI_PER = 5;
    public static final int SX_PRO_PER = 6;
    public static final int SX_OIL_PER = 7;
    public static final int SX_OLE_PER = 8;
    public static final int SX_LIN_PER = 9;
    public static final int SX_LLC_PER = 10;
    public static final int SX_ERU_PER = 11;

    public static final int SX_LAB_TEST = 1;
    public static final int SX_LAB_TEST_DET = 2;
    
    public static final ArrayList<SGuiItem> SX_REQ_FRE_OPTIONS;
    public static final HashMap<Integer, String> SX_REQ_FRE_CODE;
    
    public static final int[] MS_MVT_CL_IN_REC = new int[] { 1, 1 };
    public static final int[] MS_MVT_CL_IN_PRD = new int[] { 1, 6 };
    public static final int[] MS_MVT_CL_IN_CNV = new int[] { 1, 8 };
    public static final int[] MS_MVT_CL_IN_ADJ = new int[] { 1, 9 };
    public static final int[] MS_MVT_CL_IN_INV = new int[] { 1, 10 };
    public static final int[] MS_MVT_CL_OUT_REC = new int[] { 2, 1 };
    public static final int[] MS_MVT_CL_OUT_PRD = new int[] { 2, 6 };
    public static final int[] MS_MVT_CL_OUT_CNV = new int[] { 2, 8 };
    public static final int[] MS_MVT_CL_OUT_ADJ = new int[] { 2, 9 };
    public static final int[] MS_MVT_CL_OUT_INV = new int[] { 2, 10 };
    
    public static final int MS_TIC_ST_CLS_REC = 1;
    
    public static final int MS_EMP_TP_NA = 1;
    public static final int MS_EMP_TP_WAH_MAN = 2;
    public static final int MS_EMP_TP_MFG_SUP = 3;
    
    public static final int MU_EMP_ND = 0; // Not defined
    
    public static final int MU_EXW_FAC_NA = 0; // Not applicable
    
    public static final int MX_MVT = 1; // Vista de movimientos
    public static final int MX_MVT_DETAIL = 2; // Vista de movimientos a detalle
    public static final int MX_MONTHS_PERIOD = 1; // cantidad de meses hacia atras en los que se puede capturar movimientos
    public static final int MX_LIMIT_MONTH_TRANS_STK = 1; // ¿cual es el mes limite para hacer inv inicial? 1 = enero, 2 = febrero, etc...
    public static final int MX_TIC_WO_MVT_REC = 1;
    public static final int MX_TIC_W_MVT_REC = 2;
    
    public static final String M_NOTE_TP_MVT = "MVT"; // Movimiento de almacén
    public static final String M_NOTE_TP_STK = "STK"; // Stock
    public static final String M_NOTE_TP_TIC = "TIC"; // Ticket
    
    // External system:

    public static final int EXT_BPSS_CT_BP_SUP = 2;
    public static final int EXT_BPSS_CT_BP_CUS = 3;
    public static final int EXT_TRNS_TP_DPS_ADJ_RET = 2;
    public static final int EXT_TRNS_ST_DPS_EMITED = 2;

    public static final int EXT_FINS_TP_ACC_MOV_FY_OPEN = 2;
    public static final int EXT_FINS_TP_ACC_SYS_SUP = 4;

    public static final int[] EXT_FINS_TP_SYS_MOV_BPS_SUP = new int[] { 4, 2 };
    public static final int[] EXT_FINS_CL_ACC_ASSET = new int[] { 2, 1 };

    public static final int[] EXT_TRNS_CL_DPS_PUR_DOC = new int[] { 1, 3 };
    public static final int[] EXT_TRNS_CL_IOM_IN_INT = new int[] { 1, 5 };
    public static final int[] EXT_TRNS_CL_DPS_SAL_DOC = new int[] { 2, 3 };
    public static final int[] EXT_TRNS_CL_IOM_OUT_INT = new int[] { 2, 5 };

    public static final int[] EXT_TRNS_TP_IOG_IN_PUR_PUR = new int[] { 1, 1, 1 };
    public static final int[] EXT_TRNS_TP_IOG_IN_SAL_SAL = new int[] { 1, 2, 1 };
    public static final int[] EXT_TRNS_TP_IOG_IN_ADJ_ADJ = new int[] { 1, 3, 2 };
    public static final int[] EXT_TRNS_TP_IOG_IN_ADJ_INV = new int[] { 1, 3, 1 };
    public static final int[] EXT_TRNS_TP_IOG_IN_MFG_RM_RET = new int[] { 1, 6, 2 };
    public static final int[] EXT_TRNS_TP_IOG_IN_MFG_FG_ASD = new int[] { 1, 6, 5 };
    public static final int[] EXT_TRNS_TP_IOG_OUT_PUR_PUR = new int[] { 2, 1, 1 };
    public static final int[] EXT_TRNS_TP_IOG_OUT_SAL_SAL = new int[] { 2, 2, 1 };
    public static final int[] EXT_TRNS_TP_IOG_OUT_ADJ_ADJ = new int[] { 2, 3, 2 };
    public static final int[] EXT_TRNS_TP_IOG_OUT_ADJ_INV = new int[] { 2, 3, 1 };
    public static final int[] EXT_TRNS_TP_IOG_OUT_MFG_RM_ASD = new int[] { 2, 6, 1 };
    public static final int[] EXT_TRNS_TP_IOG_OUT_MFG_FG_RET = new int[] { 2, 6, 6 };
    public static final int[] EXT_TRNS_TP_IOG_IN_INT_TRA = new int[] { 1, 5, 1 };
    public static final int[] EXT_TRNS_TP_IOG_IN_INT_CNV = new int[] { 1, 5, 2 };
    public static final int[] EXT_TRNS_TP_IOG_OUT_INT_TRA = new int[] { 2, 5, 1 };
    public static final int[] EXT_TRNS_TP_IOG_OUT_INT_CNV = new int[] { 2, 5, 2 };

    public static final int EXT_DB_ACTION_SAVE_OK = 1201;

    public static final int REP_LAB_TEST_IOD = 1;
    public static final int REP_LAB_TEST_OLE = 2;
    public static final int REP_LAB_TEST_LIN = 3;
    public static final int REP_LAB_TEST_LLC = 4;
    
    static {
        SU_OIL_TP_DESC = new HashMap<>();
        SU_OIL_TP_DESC.put(1, "CRUDO");
        SU_OIL_TP_DESC.put(2, "REFINADO");
        SU_OIL_TP_DESC.put(3, "REPROCESO");
        SU_OIL_TP_DESC.put(4, "RESIDUO");
        
        SX_REQ_FRE_OPTIONS = new ArrayList<>();
        SX_REQ_FRE_OPTIONS.add(new SGuiItem(new int[] { 0 }, "- Control de fletes -"));
        SX_REQ_FRE_OPTIONS.add(new SGuiItem(new int[] { 1 }, "No"));
        SX_REQ_FRE_OPTIONS.add(new SGuiItem(new int[] { 2 }, "Sí"));
        
        SX_REQ_FRE_CODE = new HashMap<>();
        SX_REQ_FRE_CODE.put(1, "N");
        SX_REQ_FRE_CODE.put(2, "Y");
    }
}
