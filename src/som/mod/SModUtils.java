/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod;

import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiModuleUtils;

/**
 *
 * @author Néstor Ávalos, Sergio Flores, Isabel Servín
 */
public class SModUtils implements SGuiModuleUtils {

    public SModUtils() {

    }

    private boolean belongsToSom(final int type) {
        return SLibUtils.belongsTo(type, new int[] {
            SModConsts.SS_ITEM_TP,
            SModConsts.SU_INP_CT,
            SModConsts.SU_INP_CL,
            SModConsts.SU_INP_TP,
            SModConsts.SU_INP_SRC,
            SModConsts.SU_UNIT,
            SModConsts.SU_ITEM,
            SModConsts.SU_LOT,
            SModConsts.SU_SCA,
            SModConsts.SU_PROD,
            SModConsts.SU_IOD_VAL_RANK,
            SModConsts.SU_EXT_WAH,
            SModConsts.CU_LINK_ITEMS
        });
    }

    private boolean belongsToSomRm(final int type) {
        return SLibUtils.belongsTo(type, new int[] {
            SModConsts.SS_TIC_ST,
            SModConsts.SU_SUP_REG,
            SModConsts.SU_REG,
            SModConsts.SU_SEAS,
            SModConsts.SU_SEAS_REG,
            SModConsts.SU_SEAS_PROD,
            SModConsts.SU_GRINDING_EVENT,
            SModConsts.SU_LAB_GRINDING,
            SModConsts.SU_GRINDINGS,
            SModConsts.S_LAB,
            SModConsts.S_LAB_TEST,
            SModConsts.S_TIC,
            SModConsts.S_TIC_NOTE,
            SModConsts.SX_TIC_TARE,
            SModConsts.SX_TIC_MAN_SUP,
            SModConsts.SX_TIC_MAN_SUP_INP_TP,
            SModConsts.SX_PROD_SEAS,
            SModConsts.SX_PROD_REG,
            SModConsts.SX_PROD_ITEM,
            SModConsts.SX_TIC_SEAS,
            SModConsts.SX_TIC_REG,
            SModConsts.SR_TIC,
            SModConsts.SR_TIC_COMP,
            SModConsts.SR_ITEM_REC,
            SModConsts.SR_ITEM_REC_PAY,
            SModConsts.SR_ITEM_REC_IOD_VAL,
            SModConsts.SR_FRE_TIME,
            SModConsts.SR_ITEM_FRUIT,
            SModConsts.SR_ITEM_FRUIT_HIST
        });
    }

    private boolean belongsToSomOs(final int type) {
        return SLibUtils.belongsTo(type, new int[] {
            SModConsts.SS_IOG_CT,
            SModConsts.SS_IOG_CL,
            SModConsts.SS_IOG_TP,
            SModConsts.SS_MIX_TP,
            SModConsts.SU_IOG_ADJ_TP,
            SModConsts.SU_EXT_WAH,
            SModConsts.S_IOG,
            SModConsts.S_IOG_EXP,
            SModConsts.S_DPS_ASS,
            SModConsts.S_STK_DAY,
            SModConsts.S_STK,
            SModConsts.S_MFG_EST,
            SModConsts.S_MIX,
            SModConsts.SX_STK_DAYS,
            SModConsts.SX_STK_MOVE,
            SModConsts.SX_STK_PROD_EST,
            SModConsts.SX_TIC_SUP_RM,
            SModConsts.SX_TIC_DPS,
            SModConsts.SX_EXT_DPS,
            SModConsts.SR_STK_DAY,
            SModConsts.SR_STK,
            SModConsts.SR_STK_MOVE,
            SModConsts.SR_STK_COMP,
            SModConsts.SR_IOG_LIST
        });
    }

    @Override
    public int getModuleTypeByType(final int type) {
        int module = SLibConsts.UNDEFINED;

        if (type >= SModConsts.SU_SYS && type <= SModConsts.C_USR_GUI) {
            module = SModConsts.MOD_CFG;
        }
        else if (belongsToSom(type)) {
            module = SModConsts.MOD_SOM;
        }
        else if (belongsToSomRm(type)) {
            module = SModConsts.MOD_SOM_RM;
        }
        else if (belongsToSomOs(type)) {
            module = SModConsts.MOD_SOM_OS;
        }

        return module;
    }

    @Override
    public int getModuleSubtypeBySubtype(final int type, final int subtype) {
        return SLibConsts.UNDEFINED;
    }
}
