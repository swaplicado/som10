/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public abstract class SModConsts {

    public static final int MOD_CFG = 1;
    public static final int MOD_SOM = 2;
    public static final int MOD_SOM_RM = 3;
    public static final int MOD_SOM_OS = 4;
    public static final int MOD_SOM_LOG = 5;
    
    public static final int S_WAH_LAB_WO_LAST_TEST = 0;
    public static final int S_WAH_LAB_W_LAST_TEST = 1;

    public static final int SU_SYS = 110001;
    public static final int SU_CO = 110002;

    public static final int CS_WAH_TP = 210001;
    public static final int CS_WAH_ORI = 210005;
    public static final int CS_WAH_CALC_TP = 210006;
    public static final int CS_PLA_TP = 210002;
    public static final int CS_USR_TP = 210003;
    public static final int CS_RIG = 210004;

    public static final int CU_CO = 220001;
    public static final int CU_COB = 220002;
    public static final int CU_WAH = 220003;
    public static final int CU_PLA = 220004;
    public static final int CU_YEAR = 220005;
    public static final int CU_YEAR_PER = 220006;
    public static final int CU_LAN = 220007;
    public static final int CU_USR = 220008;
    public static final int CU_USR_RIG = 220009;
    public static final int CU_USR_SCA = 220010;
    public static final int CU_USR_INP_CT = 220021;
    public static final int CU_DIV = 220011;
    public static final int CU_PROD_LINES = 220012;
    public static final int CU_REP_GRP = 220016;
    public static final int SS_LINK_CFG_ITEMS = 220020;

    public static final int C_USR_GUI = 230001;

    public static final int SS_TIC_ST = 310001;
    public static final int SS_IOG_CT = 310002;
    public static final int SS_IOG_CL = 310003;
    public static final int SS_IOG_TP = 310004;
    public static final int SS_MIX_TP = 310005;
    public static final int SS_ITEM_TP = 310006;

    public static final int SU_IOG_ADJ_TP = 320001;
    public static final int SU_EXT_WAH = 320002;
    public static final int SU_INP_CT = 320015;
    public static final int SU_INP_CL = 320003;
    public static final int SU_INP_TP = 320004;
    public static final int SU_UNIT = 320005;
    public static final int SU_ITEM = 320006;
    public static final int SU_SCA = 320007;
    public static final int SU_SUP_REG = 320008;
    public static final int SU_REG = 320009;
    public static final int SU_PROD = 320010;
    public static final int SX_PROD_LOG = 320103;
    public static final int SU_SEAS = 320011;
    public static final int SU_SEAS_REG = 320012;
    public static final int SU_SEAS_PROD = 320013;
    public static final int SU_IOD_VAL_RANK = 320014;
    public static final int SU_INP_SRC = 320021;
    public static final int SU_BY_PRODUCT = 320022;
    public static final int SU_GRINDING_PARAM = 320041;
    public static final int SU_GRINDING_LINK_ITEM_PARAM = 320042;
    public static final int SU_GRINDING_LINK_FORMULA = 320043;
    public static final int SU_GRINDING_ITEM_PARAM_HEADER = 320044;
    public static final int SU_GRINDING_REP_GROUP = 320045;
    public static final int SU_GRINDING_REP_ITEM_GROUP = 320046;
    public static final int SU_GRINDING_REP_RECIPIENT = 320047;
    public static final int SU_VEH_CONT_TYPE = 320023;
    public static final int SU_WAH_FILL_LEVEL = 320024;
    public static final int SU_OIL_CL = 320025;
    public static final int SU_OIL_TP = 320026;
    public static final int SU_OIL_OWN = 320027;
    public static final int SU_OIL_ACI = 320028;
    public static final int SU_OIL_ACI_ETY = 320029;
    public static final int SU_OIL_GRP_FAM = 320030;
    public static final int SU_CLOSING_CAL = 320031;
    public static final int SU_FUNC_AREA = 320032;
    public static final int SU_CONS_WAH = 320033;
    public static final int SU_INP_CL_ALL = 320101;
    public static final int SU_INP_TP_ALL = 320102;

    public static final int S_LAB = 330001;
    public static final int S_LAB_NOTE = 330002;
    public static final int S_LAB_TEST = 330003;
    public static final int S_TIC = 330011;
    public static final int S_TIC_NOTE = 330012;
    public static final int S_WAH_LAB = 330016;
    public static final int S_WAH_LAB_TEST = 330017;
    public static final int S_WAH_LAB_TEST_ALL = 330019;
    public static final int S_WAH_PREMISE = 330018;
    public static final int S_IOG = 330021;
    public static final int S_IOG_NOTE = 330022;
    public static final int S_IOG_REF = 330023;
    public static final int S_IOG_EXP = 330026;
    public static final int S_IOG_EXP_HIS = 330027;
    public static final int S_DPS_ASS = 330031;
    public static final int S_GRINDING_LOT = 330041;
    public static final int S_GRINDING_RESULT = 330042;
    public static final int S_GRINDING_EVENT = 330043;
    public static final int S_GRINDING = 330046;
    public static final int S_STK_DAY = 330051;
    public static final int S_STK = 330052;
    public static final int S_STK_RECORD = 330053;
    public static final int S_PRC_BATCH = 330054;
    public static final int S_STK_REPORT = 330055;
    public static final int S_PRC_RAW_MAT = 330056;
    public static final int S_CONS_RECORD = 330057;
    public static final int S_MFG_EST = 330061;
    public static final int S_MFG_EST_ETY = 330062;
    public static final int S_MFG_EST_RM_CON = 330063;
    public static final int S_MFG_EST_PL = 330064;
    public static final int S_MIX = 330071;
    public static final int S_MIX_NOTE = 330072;
    public static final int S_WAH_START = 330073;

    public static final int SX_SEAS_REG = 340001;
    public static final int SX_PROD_SEAS = 340002;
    public static final int SX_PROD_REG = 340003;
    public static final int SX_PROD_ITEM = 340011;
    public static final int SX_TIC_SEAS = 340012;
    public static final int SX_TIC_REG = 340013;
    public static final int SX_TIC_TARE = 340021;
    public static final int SX_TIC_TARE_PEND = 340022;
    public static final int SX_IOG_PROD = 340024;
    public static final int SX_TIC_WAH_UNLD = 340081;
    public static final int SX_TIC_LOG = 340023;
    public static final int SX_TIC_LAB_TEST_FRUIT = 340026;
    public static final int SX_GRINDING_RESUME = 340028;
    public static final int SX_STK_STK = 340031;
    public static final int SX_STK_DIV = 340032;
    public static final int SX_STK_MOVE = 340033;
    public static final int SX_STK_MOVE_DET = 340034;
    public static final int SX_STK_WAH_WAH = 340035;
    public static final int SX_STK_WAH_DIV = 340036;
    public static final int SX_STK_DAYS = 340037;
    public static final int SX_STK_DAYS_LOG = 340038;
    public static final int SX_IOG_SUP_PUR = 340041;
    public static final int SX_IOG_SUP_SAL = 340042;
    public static final int SX_IOG_ADJ_PUR = 340043;
    public static final int SX_IOG_ADJ_SAL = 340044;
    public static final int SX_IOG_SUP_ADJ_PEN = 340045;
    public static final int SX_IOG_SUP_ADJ_ASSO = 340046;
    public static final int SX_IOG_SUP_ADJ_DOC = 340047;
    public static final int SX_STK_PROD_EST = 340051;
    public static final int SX_STK_PROD_EMP = 340052;
    public static final int SX_STK_PROD_INV = 340053;
    public static final int SX_STK_PROD_FG = 340054;
    public static final int SX_STK_PROD_VER = 340054;
    public static final int SX_TIC_LAB = 340061;
    public static final int SX_TIC_MAN = 340062;
    public static final int SX_TIC_MAN_SUP = 340063;
    public static final int SX_TIC_MAN_SUP_INP_TP = 340064;
    public static final int SX_TIC_SEAS_REG = 340065;
    public static final int SX_TIC_ASSO = 340066;
    public static final int SX_TIC_SUP_RM = 340067;
    public static final int SX_TIC_DPS = 340068;
    public static final int SX_TIC_DPS_SUP = 340069;
    public static final int SX_TIC_DPS_ASSO = 340070;
    public static final int SX_INV = 340071;
    public static final int SX_INV_IN_RM = 340072;
    public static final int SX_INV_OUT_RM = 340073;
    public static final int SX_INV_IN_FG = 340074;
    public static final int SX_INV_OUT_FG = 340075;
    public static final int SX_STK_CLO = 340076;
    public static final int SX_EXT_ITEM = 340101;
    public static final int SX_EXT_DPS = 340102;
    public static final int SX_WIZ_DPS = 340103;
    public static final int SX_WIZ_DPS_TIC = 340104;
    public static final int SX_WIZ_DPS_PUR = 340105;
    public static final int SX_WIZ_DPS_ADJ = 340106;
    public static final int SX_TIC_DPS_PAY = 340107;
    public static final int SX_DAY_MAIL = 340108;
    public static final int SX_EXT_OIL_CL = 340108;
    public static final int SX_QA_OIL_MOI_POND = 340082;

    public static final int SR_TIC = 350001;
    public static final int SR_TIC_METRRME = 350015;
    public static final int SR_TIC_COMP = 350002;
    public static final int SR_ITEM_REC = 350003;
    public static final int SR_ITEM_REC_PAY = 350004;
    public static final int SR_ITEM_REC_IOD_VAL = 350005;
    public static final int SR_STK_DAY = 350006;
    public static final int SR_STK = 350007;
    public static final int SR_STK_MOVE = 350008;
    public static final int SR_STK_COMP = 350009;
    public static final int SR_IOG_LIST = 350010;
    public static final int SR_FRE_TIME = 350011;
    public static final int SR_ITEM_FRUIT = 350012;
    public static final int SR_ITEM_FRUIT_HIST = 350013;
    public static final int SR_FRUIT_YIELD_ORIG = 350014;
    public static final int SR_CH_OIL_PERC = 350016;
    public static final int SR_STK_DAILY = 350021;
    public static final int SR_ITEM_FRUIT_ACI = 350022;

    public static final int E_TIC_REV = 430001;

    public static final HashMap<Integer, String> TablesMap = new HashMap<>();

    static {
        TablesMap.put(SU_SYS, "su_sys");
        TablesMap.put(SU_CO, "su_co");

        TablesMap.put(CS_WAH_TP, "cs_wah_tp");
        TablesMap.put(CS_WAH_ORI, "cs_wah_ori");
        TablesMap.put(CS_WAH_CALC_TP, "cs_wah_calc_tp");
        TablesMap.put(CS_PLA_TP, "cs_pla_tp");
        TablesMap.put(CS_USR_TP, "cs_usr_tp");
        TablesMap.put(CS_RIG, "cs_rig");

        TablesMap.put(CU_CO, "cu_co");
        TablesMap.put(CU_COB, "cu_cob");
        TablesMap.put(CU_WAH, "cu_wah");
        TablesMap.put(CU_PLA, "cu_pla");
        TablesMap.put(CU_YEAR, "cu_year");
        TablesMap.put(CU_YEAR_PER, "cu_year_per");
        TablesMap.put(CU_LAN, "cu_lan");
        TablesMap.put(CU_USR, "cu_usr");
        TablesMap.put(CU_USR_RIG, "cu_usr_rig");
        TablesMap.put(CU_USR_SCA, "cu_usr_sca");
        TablesMap.put(CU_USR_INP_CT, "cu_usr_inp_ct");
        TablesMap.put(CU_DIV, "cu_div");
        TablesMap.put(CU_PROD_LINES, "cu_line");
        TablesMap.put(CU_REP_GRP, "cu_rep_grp");
        TablesMap.put(C_USR_GUI, "c_usr_gui");

        TablesMap.put(SS_TIC_ST, "ss_tic_st");
        TablesMap.put(SS_IOG_CT, "ss_iog_ct");
        TablesMap.put(SS_IOG_CL, "ss_iog_cl");
        TablesMap.put(SS_IOG_TP, "ss_iog_tp");
        TablesMap.put(SS_MIX_TP, "ss_mix_tp");
        TablesMap.put(SS_ITEM_TP, "ss_item_tp");

        TablesMap.put(SU_IOG_ADJ_TP, "su_iog_adj_tp");
        TablesMap.put(SU_EXT_WAH, "su_ext_wah");
        TablesMap.put(SU_INP_CT, "su_inp_ct");
        TablesMap.put(SU_INP_CL, "su_inp_cl");
        TablesMap.put(SU_INP_TP, "su_inp_tp");
        TablesMap.put(SU_UNIT, "su_unit");
        TablesMap.put(SU_ITEM, "su_item");
        TablesMap.put(SU_SCA, "su_sca");
        TablesMap.put(SU_SUP_REG, "su_sup_reg");
        TablesMap.put(SU_REG, "su_reg");
        TablesMap.put(SU_PROD, "su_prod");
        TablesMap.put(SU_SEAS, "su_seas");
        TablesMap.put(SU_SEAS_REG, "su_seas_reg");
        TablesMap.put(SU_SEAS_PROD, "su_seas_prod");
        TablesMap.put(SU_IOD_VAL_RANK, "su_iod_val_rank");
        TablesMap.put(SU_INP_SRC, "su_inp_src");
        TablesMap.put(SU_BY_PRODUCT, "su_by_product");
        TablesMap.put(SU_GRINDING_PARAM, "su_grin_param");
        TablesMap.put(SU_GRINDING_LINK_ITEM_PARAM, "su_grin_link_item_param");
        TablesMap.put(SU_GRINDING_LINK_FORMULA, "su_grin_link_formula");
        TablesMap.put(SU_GRINDING_ITEM_PARAM_HEADER, "su_grin_item_param_header");
        TablesMap.put(SU_GRINDING_REP_GROUP, "su_grin_group");
        TablesMap.put(SU_GRINDING_REP_ITEM_GROUP, "su_grin_rep_group");
        TablesMap.put(SU_GRINDING_REP_RECIPIENT, "su_grin_recipient");
        TablesMap.put(SU_VEH_CONT_TYPE, "su_veh_cont_type");
        TablesMap.put(SU_OIL_TP, "su_oil_tp");
        TablesMap.put(SU_WAH_FILL_LEVEL, "su_wah_fill_level");
        TablesMap.put(SU_OIL_CL, "su_oil_cl");
        TablesMap.put(SU_OIL_TP, "su_oil_tp");
        TablesMap.put(SU_OIL_OWN, "su_oil_own");
        TablesMap.put(SU_OIL_ACI, "su_oil_aci");
        TablesMap.put(SU_OIL_ACI_ETY, "su_oil_aci_ety");
        TablesMap.put(SU_OIL_GRP_FAM, "su_oil_grp_fam");
        TablesMap.put(SU_CLOSING_CAL, "su_closing_cal");
        TablesMap.put(SU_FUNC_AREA, "su_func_area");
        TablesMap.put(SU_CONS_WAH, "su_cons_wah");
        TablesMap.put(SU_GRINDING_EVENT, "su_grin_events");
        TablesMap.put(SU_GRINDING_RESULTS, "su_grin_res");
        TablesMap.put(SU_GRINDINGS, "su_grinding");

        TablesMap.put(S_LAB, "s_lab");
        TablesMap.put(S_LAB_NOTE, "s_lab_note");
        TablesMap.put(S_LAB_TEST, "s_lab_test");
        TablesMap.put(S_TIC, "s_tic");
        TablesMap.put(S_TIC_NOTE, "s_tic_note");
        TablesMap.put(S_WAH_PREMISE, "s_wah_premise");
        TablesMap.put(S_WAH_LAB, "s_wah_lab");
        TablesMap.put(S_WAH_LAB_TEST, "s_wah_lab_test");
        TablesMap.put(S_IOG, "s_iog");
        TablesMap.put(S_IOG_NOTE, "s_iog_note");
        TablesMap.put(S_IOG_REF, "s_iog_ref");
        TablesMap.put(S_IOG_EXP, "s_iog_exp");
        TablesMap.put(S_IOG_EXP_HIS, "s_iog_exp_his");
        TablesMap.put(S_DPS_ASS, "s_dps_ass");
        TablesMap.put(S_GRINDING_LOT, "s_grin_lot");
        TablesMap.put(S_GRINDING_RESULT, "s_grin_result");
        TablesMap.put(S_GRINDING_EVENT, "s_grin_event");
        TablesMap.put(S_GRINDING, "s_grinding");
        TablesMap.put(S_STK_DAY, "s_stk_day");
        TablesMap.put(S_STK, "s_stk");
        TablesMap.put(S_STK_RECORD, "s_stk_record");
        TablesMap.put(S_PRC_BATCH, "s_prc_batch");
        TablesMap.put(S_STK_REPORT, "s_stk_report");
        TablesMap.put(S_PRC_RAW_MAT, "s_prc_raw_mat");
        TablesMap.put(S_CONS_RECORD, "s_cons_record");
        TablesMap.put(S_MFG_EST, "s_mfg_est");
        TablesMap.put(S_MFG_EST_ETY, "s_mfg_est_ety");
        TablesMap.put(S_MFG_EST_RM_CON, "s_mfg_est_rm_con");
        TablesMap.put(S_MIX, "s_mix");
        TablesMap.put(S_MIX_NOTE, "s_mix_note");
        TablesMap.put(S_WAH_START, "s_wah_start");

        TablesMap.put(E_TIC_REV, "e_tic_rev");
    }
}
