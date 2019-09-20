/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Juan Barajas, Néstor Ávalos
 */
public class SDbMix extends SDbRegistryUser {

    protected int mnPkMixId;
    protected int mnNumber;
    protected Date mtDate;
    protected double mdQuantity;
    protected boolean mbAuthorized;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkMixTypeId;
    protected int mnFkItemSourceId;
    protected int mnFkUnitSourceId;
    protected int mnFkItemDestinyId;
    protected int mnFkUnitDestinyId;
    protected int mnFkWarehouseSourceCompanyId;
    protected int mnFkWarehouseSourceBranchId;
    protected int mnFkWarehouseSourceWarehouseId;
    protected int mnFkDivisionSourceId_n;
    protected int mnFkWarehouseDestinyCompanyId;
    protected int mnFkWarehouseDestinyBranchId;
    protected int mnFkWarehouseDestinyWarehouseId;
    protected int mnFkDivisionDestinyId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected int mnFkUserAuthorizationId;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    protected Date mtTsUserAuthorization;

    protected int[] manAuxWarehouseSourceId;
    protected int[] manAuxWarehouseDestinyId;

    protected SDbMixNote moMixNoteChild;
    protected SDbIog moAuxMixPassiveIogIn;
    protected SDbIog moAuxMixPassiveIogOut;
    protected SDbIog moAuxMixActiveIogOut;

    protected ArrayList<SDbIog> maAuxConversionIogIn;
    protected ArrayList<SDbIog> maAuxConversionIogOut;

    protected String msXtaNote;

    public SDbMix() {
        super(SModConsts.S_MIX);
        initRegistry();
    }

    /*
     * Private methods
     */

    private String validateMixInOutMoves(final SGuiSession session, final boolean actionDelete) throws SQLException, Exception {
        String result = "";

        SDbMfgEstimation estimation = null;

        // Validate that day is open:

        try {
            estimation = new SDbMfgEstimation();
            estimation.obtainProductionEstimateByDate(session, null, mtDate);

            if (estimation.isClosed()) {

                result = "El período está cerrado por estimación de la producción.";
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        if (result.isEmpty()) {

            // Validate stock for warehouse source and destiny:

            result = validateWarehousesStock(session, actionDelete, mtDate, mnFkItemSourceId, mnFkItemDestinyId, mnFkUnitSourceId, mnFkUnitDestinyId, manAuxWarehouseSourceId,
                    manAuxWarehouseDestinyId, mnFkDivisionSourceId_n, mnFkDivisionDestinyId_n, mdQuantity, mnFkMixTypeId, mbRegistryNew);
            if (result.isEmpty()) {

                // Business rules for warehouse source and destiny:

                result = validateBusinessRulesWarehouses(session, actionDelete);
            }

            /*

            warehouse = new SDbBranchWarehouse();
            warehouse.read(session, new int[] { mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId });
            if (warehouse.getQueryResultId() != SDbConsts.READ_OK) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }

            item = new SDbItem();
            item.read(session, new int[] { mnFkItemId });
            if (item.getQueryResultId() != SDbConsts.READ_OK) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }

            if ((!actionDelete && mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_IN) ||
                    (actionDelete && mbDeleted && mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_IN) ||
                    (actionDelete && !mbDeleted && mnFkIogCategoryId == SModSysConsts.SS_IOG_CT_OUT)) {

                // Validate warehouse business rules:

                msQueryResult = SSomUtils.obtainWarehouseItemsForBusinessRules(
                        session,
                        actionDelete,
                        warehouse.getPrimaryKey(),
                        moStock != null ? moStock.getPrimaryKey() : null,
                        mtDate,
                        mnFkItemId,
                        mnFkUnitId,
                        mdQuantity,
                        msXtaUnit,
                        mbXtaValidateTankCap,
                        mbXtaValidateExportationStock);

                if (!msQueryResult.isEmpty()) {
                    b = false;
                }
                */

                /* XXX Delete before to updateCode
                 *
                // Obtain items by warehouse:

                aWarehouseItems = stockWarehouseByItem(session, SLibTimeUtils.digestYear(mtDate)[0], new int[] { mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId },
                        (moStock != null ? moStock.getPrimaryKey() : null), (!mbXtaValidateExportationStock ? SLibTimeUtils.getEndOfYear(mtDate) : mtDate), SLibConsts.UNDEFINED);
                dWarehouseCapacity = 0;

                for (Object oItem : itemsStock) {
                    nItemId = (Integer)((Object[]) oItem)[0];
                    nItemUnitId = (Integer)((Object[]) oItem)[1];
                    nItemTypeId = (Integer)((Object[]) oItem)[2];
                    sItem = (String)((Object[]) oItem)[3];
                    sItemUnit = (String)((Object[]) oItem)[4];
                    dItemStock = (Double)((Object[]) oItem)[5];
                    dItemDensity = (Double)((Object[]) oItem)[6];

                    //if (!mbXtaValidateTankItems) {
                        if (warehouse.getFkWarehouseTypeId() != SModSysConsts.CS_WAH_TP_WAH) {

                            // Validate only if item is not a cull:

                            if ((mnFkItemId != nItemId || mnFkUnitId != nItemUnitId) &&
                                item.getFkItemTypeId() != SModSysConsts.SS_ITEM_TP_CU &&
                                nItemTypeId != SModSysConsts.SS_ITEM_TP_CU) {

                                msQueryResult = "El almacén '" + warehouse.getCode() + "' ya contiene un ítem con existencias: '" + sItem + "'.";
                                b = false;
                                break;
                            }
                        }
                    //}

                    // Validate total capacity in warehouse:

                    if (!mbXtaValidateTankCap) {
                        dWarehouseCapacity += (dItemDensity > 0 ? (dItemStock / dItemDensity) : (dItemStock / item.getDensity()));
                        if ((dWarehouseCapacity + ((actionDelete ? 0 : mdQuantity) / item.getDensity())) > warehouse.getCapacityRealLiter() &&
                                warehouse.getFkWarehouseTypeId() == SModSysConsts.CS_WAH_TP_TAN) {
                            msQueryResult = "La cantidad capturada de: '" + SLibUtils.DecimalFormatValue2D.format(mdQuantity) + " " + msXtaUnit +
                                    "' hace que el tanque: '" + warehouse.getCode() + "' sobrepase su capacidad.";
                            b = false;
                            break;
                        }
                    }

                    // Validate units:

                    if (mnFkUnitId != nItemUnitId &&
                        nItemTypeId != SModSysConsts.SS_ITEM_TP_CU) {

                        msQueryResult = "El ítem '" + msXtaItem + " " + msXtaUnit + "' tiene una unidad ('" + sItemUnit + "') diferente a la del producto : '" + warehouse.getCode() + "'.";
                        b = false;
                        break;
                    }
                } */
        }

        return result;
    }

    private static String validateWarehousesStock(final SGuiSession session, final boolean actionDelete, final Date ptDate, final int pnFkItemSourceId, final int pnFkItemDestinyId,
            final int pnFkUnitSourceId, final int pnFkUnitDestinyId, final int[] panAuxWarehouseSourceId, final int[] panAuxWarehouseDestinyId, final int pnFkDivisionSourceId_n,
            final int pnFkDivisionDestinyId_n, final double pdQuantity, final int pnFkMixTypeId, final boolean pbRegistryNew) {
        String result = "";
        SSomStock stock = null;

        // Validate stock depending of actionDelete:

        stock = SSomUtils.validateStock(
            session,
            SLibTimeUtils.digestYear(ptDate)[0],
            !actionDelete ? pnFkItemSourceId : pnFkItemDestinyId,
            !actionDelete ? pnFkUnitSourceId : pnFkUnitDestinyId,
            SLibConsts.UNDEFINED,
            !actionDelete ? panAuxWarehouseSourceId : panAuxWarehouseDestinyId,
            !actionDelete ? pnFkDivisionSourceId_n : pnFkDivisionDestinyId_n,
            null,
            ptDate,
            pdQuantity);

        if (!stock.getResult().isEmpty()) {
            result = stock.getResult();
        }
        else {
            // Validate stock destiny, only apply for mix passive and active:

            if (pnFkMixTypeId != SModSysConsts.SS_MIX_TP_CNV) {

                stock = SSomUtils.obtainStock(
                    session,
                    SLibTimeUtils.digestYear(ptDate)[0],
                    !actionDelete ?
                        (pnFkMixTypeId == SModSysConsts.SS_MIX_TP_MIX_ACT ? pnFkItemSourceId : pnFkItemDestinyId) :
                        (pnFkMixTypeId == SModSysConsts.SS_MIX_TP_MIX_ACT ? pnFkItemDestinyId : pnFkItemSourceId),
                    !actionDelete ?
                        (pnFkMixTypeId == SModSysConsts.SS_MIX_TP_MIX_ACT ? pnFkUnitSourceId : pnFkUnitDestinyId) :
                        (pnFkMixTypeId == SModSysConsts.SS_MIX_TP_MIX_ACT ? pnFkUnitDestinyId : pnFkUnitSourceId),
                    SLibConsts.UNDEFINED,
                    !actionDelete ?
                        (pnFkMixTypeId == SModSysConsts.SS_MIX_TP_MIX_ACT ? panAuxWarehouseSourceId : panAuxWarehouseDestinyId) :
                        (pnFkMixTypeId == SModSysConsts.SS_MIX_TP_MIX_ACT ? panAuxWarehouseDestinyId : panAuxWarehouseSourceId),
                    !actionDelete ?
                        (pnFkMixTypeId == SModSysConsts.SS_MIX_TP_MIX_ACT ? pnFkDivisionSourceId_n : pnFkDivisionDestinyId_n) :
                        (pnFkMixTypeId == SModSysConsts.SS_MIX_TP_MIX_ACT ? pnFkDivisionDestinyId_n : pnFkDivisionSourceId_n),
                    null,
                    ptDate,
                    false,
                    false);

                if (stock.getStock() <= 0) {

                    if (pnFkMixTypeId == SModSysConsts.SS_MIX_TP_MIX_ACT && !pbRegistryNew) {
                        result = "";
                    }
                    else {
                        result = "El almacén "+ (!actionDelete ? "destino" : "origen") + " debe tener existencias.";
                    }
                }
            }
        }

        return result;
    }

    private String validateBusinessRulesWarehouses(final SGuiSession session, final boolean actionDelete) throws Exception {
        String result = "";
        boolean validateSourceWarehouseCapacity = false;
        boolean validateDestinyWarehouseCapacity = false;

        // Validate capacity of warehouses:

        if (!actionDelete) {
            validateSourceWarehouseCapacity = false;
            validateDestinyWarehouseCapacity = true;
        }
        else {
            validateSourceWarehouseCapacity = true;
            validateDestinyWarehouseCapacity = false;
        }

        // Warehouse source:

        result = SSomUtils.obtainWarehouseItemsForBusinessRules(
            session,
            !actionDelete ? manAuxWarehouseSourceId : manAuxWarehouseDestinyId,
            null,
            mtDate,
            !actionDelete ? mnFkItemSourceId : mnFkItemDestinyId,
            !actionDelete ? mnFkUnitSourceId : mnFkUnitDestinyId,
            !actionDelete ? mdQuantity : mdQuantity,
            "", // XXX PENDING TO OBTAIN unid of TANQ
            !actionDelete ? validateSourceWarehouseCapacity : validateDestinyWarehouseCapacity,
            false,
            !actionDelete ? mnPkMixId : SLibConsts.UNDEFINED);

        if (result.isEmpty() &&
                mnFkMixTypeId != SModSysConsts.SS_MIX_TP_CNV) {

            // Warehouse destiny:

            result = SSomUtils.obtainWarehouseItemsForBusinessRules(
                session,
                !actionDelete ? manAuxWarehouseDestinyId : manAuxWarehouseSourceId,
                null,
                mtDate,
                !actionDelete ? mnFkItemDestinyId : mnFkItemSourceId,
                !actionDelete ? mnFkUnitDestinyId : mnFkUnitSourceId,
                !actionDelete ? mdQuantity : mdQuantity,
                "", // XXX PENDING TO OBTAIN unid of TANQ
                !actionDelete ? validateDestinyWarehouseCapacity : validateSourceWarehouseCapacity,
                false,
                !actionDelete ? mnPkMixId : SLibConsts.UNDEFINED);
        }

        return result;
    }

    private String validateInputFields() {
        String result = "";

        if ((SModSysConsts.SS_MIX_TP_MIX_PAS == mnFkMixTypeId ||
            SModSysConsts.SS_MIX_TP_MIX_ACT == mnFkMixTypeId) &&
            (mnFkDivisionSourceId_n == 0 || mnFkDivisionDestinyId_n == 0)) {

            result = "La división " + (mnFkDivisionSourceId_n == 0 ? "origen" : "destino") + " es obligatoria.";
        }
        else if (mnFkDivisionSourceId_n != mnFkDivisionDestinyId_n) {

            result = "La división origen y destino deben ser iguales.";
        }
        else if (mnFkItemSourceId == mnFkItemDestinyId) {

            result = "El ítem origen y destino deben ser diferentes.";
        }
        else if (mnFkUnitSourceId != mnFkUnitDestinyId) {

            result = "Las unidades del ítem origen y destino deben ser iguales.";
        }
        else if (SModSysConsts.SS_MIX_TP_MIX_PAS == mnFkMixTypeId ||
            SModSysConsts.SS_MIX_TP_MIX_ACT == mnFkMixTypeId) {

            if (SLibUtils.compareKeys(manAuxWarehouseSourceId, manAuxWarehouseDestinyId)) {

                result = "El almacén origen y destino deben ser diferentes.";
            }
        }
        else if (SModSysConsts.SS_MIX_TP_CNV == mnFkMixTypeId) {

            if (!SLibUtils.compareKeys(manAuxWarehouseSourceId, manAuxWarehouseDestinyId)) {

                result = "El almacén origen y destino deben ser iguales";
            }
        }

        return result;
    }

    private void computeNumber(final SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnNumber = 0;
        msSql = "SELECT COALESCE(MAX(CONVERT(num, UNSIGNED INTEGER)), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_MIX) + " "
                + "WHERE fk_mix_tp = " + mnFkMixTypeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnNumber = resultSet.getInt(1);
        }
    }

    private String mixPassiveSave(final SGuiSession session) throws Exception {
        String result = "";

        // Create or save iog out/in:

        if (mbRegistryNew) {
            moAuxMixPassiveIogOut = new SDbIog();

            moAuxMixPassiveIogOut.setDate(mtDate);
            moAuxMixPassiveIogOut.setQuantity(mdQuantity);
            moAuxMixPassiveIogOut.setSystem(true);
            moAuxMixPassiveIogOut.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_OUT_INT_MIX_PAS[0]);
            moAuxMixPassiveIogOut.setFkIogClassId(SModSysConsts.SS_IOG_TP_OUT_INT_MIX_PAS[1]);
            moAuxMixPassiveIogOut.setFkIogTypeId(SModSysConsts.SS_IOG_TP_OUT_INT_MIX_PAS[2]);
            moAuxMixPassiveIogOut.setFkIogAdjustmentTypeId(SModSysConsts.SU_IOG_ADJ_TP_NA);
            moAuxMixPassiveIogOut.setFkItemId(mnFkItemSourceId);
            moAuxMixPassiveIogOut.setFkUnitId(mnFkUnitSourceId);
            moAuxMixPassiveIogOut.setFkWarehouseCompanyId(mnFkWarehouseSourceCompanyId);
            moAuxMixPassiveIogOut.setFkWarehouseBranchId(mnFkWarehouseSourceBranchId);
            moAuxMixPassiveIogOut.setFkWarehouseWarehouseId(mnFkWarehouseSourceWarehouseId);
            moAuxMixPassiveIogOut.setFkDivisionId(mnFkDivisionSourceId_n);
            moAuxMixPassiveIogOut.setFkIogId_n(SLibConsts.UNDEFINED);
            moAuxMixPassiveIogOut.setFkMixId_n(mnPkMixId);
        }
        else {

            moAuxMixPassiveIogOut.setDate(mtDate);
            moAuxMixPassiveIogOut.setQuantity(mdQuantity);
        }

        if (moAuxMixPassiveIogOut.canSave(session)) {

            if (mbRegistryNew) {
                moAuxMixPassiveIogIn = new SDbIog();

                moAuxMixPassiveIogIn.setDate(mtDate);
                moAuxMixPassiveIogIn.setQuantity(mdQuantity);
                moAuxMixPassiveIogIn.setSystem(true);
                moAuxMixPassiveIogIn.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_IN_INT_MIX_PAS[0]);
                moAuxMixPassiveIogIn.setFkIogClassId(SModSysConsts.SS_IOG_TP_IN_INT_MIX_PAS[1]);
                moAuxMixPassiveIogIn.setFkIogTypeId(SModSysConsts.SS_IOG_TP_IN_INT_MIX_PAS[2]);
                moAuxMixPassiveIogIn.setFkIogAdjustmentTypeId(SModSysConsts.SU_IOG_ADJ_TP_NA);
                moAuxMixPassiveIogIn.setFkItemId(mnFkItemDestinyId);
                moAuxMixPassiveIogIn.setFkUnitId(mnFkUnitDestinyId);
                moAuxMixPassiveIogIn.setFkWarehouseCompanyId(mnFkWarehouseDestinyCompanyId);
                moAuxMixPassiveIogIn.setFkWarehouseBranchId(mnFkWarehouseDestinyBranchId);
                moAuxMixPassiveIogIn.setFkWarehouseWarehouseId(mnFkWarehouseDestinyWarehouseId);
                moAuxMixPassiveIogIn.setFkDivisionId(mnFkDivisionDestinyId_n);
                moAuxMixPassiveIogIn.setFkIogId_n(SLibConsts.UNDEFINED);
                moAuxMixPassiveIogIn.setFkMixId_n(mnPkMixId);
            }
            else {

                moAuxMixPassiveIogIn.setDate(mtDate);
                moAuxMixPassiveIogIn.setQuantity(mdQuantity);
            }

            if (moAuxMixPassiveIogIn.canSave(session)) {

                moAuxMixPassiveIogOut.save(session);
                moAuxMixPassiveIogIn.save(session);
            }
            else {

                msQueryResult = moAuxMixPassiveIogIn.getQueryResult();
            }

            validateMixInOutMoves(session, false);
        }
        else {

            result = moAuxMixPassiveIogOut.getQueryResult();
        }

        return result;
    }

    private static String conversionSave(final SGuiSession session,
            final int[] panAuxWarehouseSourceId, final int pnFkDivisionSourceId_n, final int pnFkItemSourceId, final int pnFkUnitSourceId,
            final int[] panAuxWarehouseDestinyId, final int pnFkDivisionDestinyId_n, final int pnFkItemDestinyId, final int pnFkUnitDestinyId,
            final double pdQuantity, final Date ptDate, final int pnPkMixId, final int pnFkMixTypeId, final boolean pbRegistryNew, final boolean pbDeleted,
            final ArrayList<SDbIog> paAuxConversionIogOut, final ArrayList<SDbIog> paAuxConversionIogIn) throws Exception {
        String result = "";

        SSomStock stock = null;

        ArrayList<SDbIog> aAuxConversionIogOut = new ArrayList<SDbIog>();
        ArrayList<SDbIog> aAuxConversionIogIn = new ArrayList<SDbIog>();

        // Generate iog's (out) list:

        if (pbRegistryNew) {

            aAuxConversionIogOut = conversionGenerateIogOutList(session, ptDate, pnFkItemSourceId, pnFkUnitSourceId, panAuxWarehouseSourceId, pnPkMixId);

            // Validate business rules:

            result = conversionValidateBusinessRulesByMove(session, aAuxConversionIogIn, ptDate, pnPkMixId);
            if (result.isEmpty()) {

                // Process iog's (out)  list:

                result = conversionSaveIogsOut(session, aAuxConversionIogOut);
                if (result.isEmpty()) {

                    /*
                    // Validate business rules:

                    result = conversionValidateBusinessRules(session);
                    if (result.isEmpty()) {
                    */
                        // Validate stock in warehouse (empty warehouse):

                        stock = conversionValidateWarehouseEmpty(session, ptDate, pnFkItemSourceId, pnFkUnitSourceId, panAuxWarehouseSourceId, SLibConsts.UNDEFINED);
                        if (stock.getStock() != 0) {
                            result = "No se puede realizar la conversión porque el almacén origen no queda vacío durante la conversión.";
                        }

                        if (result.isEmpty()) {

                            // Generate or convert iog's (in) list:

                            aAuxConversionIogIn = conversionConvertIogsOutToIn(aAuxConversionIogOut, pnFkItemDestinyId, pnFkUnitDestinyId, panAuxWarehouseDestinyId);

                            // Process iog's (in)  list:

                            result = conversionSaveIogsIn(session, aAuxConversionIogIn);
                        }
                    //}
                }
            }
        }
        else {

            // Save iogs moves in-out:

            for (SDbIog iog : paAuxConversionIogIn) {
                iog.save(session);

                if (!iog.getQueryResult().isEmpty()) {

                    result = iog.getQueryResult();
                    break;
                }
            }

            for (SDbIog iog : paAuxConversionIogOut) {
                iog.save(session);

                if (!iog.getQueryResult().isEmpty()) {

                    result = iog.getQueryResult();
                    break;
                }
            }
        }

        if (result.isEmpty()) {

            // Validate business rules:

            // XXX result = conversionValidateBusinessRules(session);
            result = validateWarehousesStock(session, !pbDeleted, ptDate, pnFkItemSourceId, pnFkItemDestinyId, pnFkUnitSourceId, pnFkUnitDestinyId, panAuxWarehouseSourceId,
                    panAuxWarehouseDestinyId, pnFkDivisionSourceId_n, pnFkDivisionDestinyId_n, pdQuantity, pnFkMixTypeId, pbRegistryNew);
        }

        return result;
    }

    private static String conversionDeleteIogsIn(final SGuiSession session, final ArrayList<SDbIog> maAuxConversionIogIn) throws Exception {
        String result = "";

        for (SDbIog iog : maAuxConversionIogIn) {

            if (iog.canDelete(session)) {

                iog.delete(session);
            }
            else {

                result = iog.getQueryResult();
                break;
            }
        }

        return result;
    }

    private static String conversionDeleteIogsOut(final SGuiSession session, final ArrayList<SDbIog> maAuxConversionIogOut) throws Exception {
        String result = "";

        for (SDbIog iog : maAuxConversionIogOut) {

            if (iog.canDelete(session)) {

                iog.delete(session);
            }
            else {

                result = iog.getQueryResult();
                break;
            }
        }

        return result;
    }

    private static ArrayList<SDbIog> conversionGenerateIogOutList(final SGuiSession session, final Date ptDate, final int pnFkItemSourceId, final int pnFkUnitSourceId,
            final int[] pnFkWarehouseSourceCompanyId, final int pnPkMixId) throws Exception {
        String sSql = "";
        ArrayList<SDbIog> aAuxConversionIogOut = new ArrayList<SDbIog>();

        Statement statement = null;
        ResultSet resultSet = null;

        SDbIog iog = null;

        sSql = "SELECT SUM(mov_in - mov_out) AS f_stk, id_div " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_STK) + " " +
                "WHERE b_del = 0 AND id_year = " + SLibTimeUtils.digestYear(ptDate)[0] + " AND id_item = " + pnFkItemSourceId + " AND id_unit = " + pnFkUnitSourceId + " AND " +
                "id_co = " + pnFkWarehouseSourceCompanyId[0] + " AND id_cob = " + pnFkWarehouseSourceCompanyId[1] + " AND id_wah = " + pnFkWarehouseSourceCompanyId[2] + " AND " +
                "dt <= '" + SLibUtils.DbmsDateFormatDate.format(ptDate) + "' " +
                "GROUP BY id_item, id_unit, id_co, id_cob, id_wah, id_div " +
                "HAVING f_stk > 0 " +
                "ORDER BY dt DESC, id_item, id_unit, id_co, id_cob, id_wah";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(sSql);
        while (resultSet.next()) {

            iog = new SDbIog();
            iog.setDate(ptDate);
            iog.setQuantity(resultSet.getDouble("f_stk"));
            iog.setSystem(true);
            iog.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_OUT_INT_CNV[0]);
            iog.setFkIogClassId(SModSysConsts.SS_IOG_TP_OUT_INT_CNV[1]);
            iog.setFkIogTypeId(SModSysConsts.SS_IOG_TP_OUT_INT_CNV[2]);
            iog.setFkIogAdjustmentTypeId(SModSysConsts.SU_IOG_ADJ_TP_NA);
            iog.setFkItemId(pnFkItemSourceId);
            iog.setFkUnitId(pnFkUnitSourceId);
            iog.setFkWarehouseCompanyId(pnFkWarehouseSourceCompanyId[0]);
            iog.setFkWarehouseBranchId(pnFkWarehouseSourceCompanyId[1]);
            iog.setFkWarehouseWarehouseId(pnFkWarehouseSourceCompanyId[2]);
            iog.setFkDivisionId(resultSet.getInt("id_div"));
            iog.setFkIogId_n(SLibConsts.UNDEFINED);
            iog.setFkMixId_n(pnPkMixId);

            aAuxConversionIogOut.add(iog);
        }

        return aAuxConversionIogOut;
    }

    private static String conversionValidateBusinessRulesByMove(final SGuiSession session, final ArrayList<SDbIog> paAuxConversionIogIn, final Date ptDate, final int pnPkMixId) throws Exception {
        String result = "";
        SSomStock stock = null;

  /*
        for (SDbIog iog : maAuxConversionIogOut) {

            stock = SSomUtils.validateStock(
                session,
                SLibTimeUtils.digestYear(mtDate)[0],
                iog.getFkItemId(),
                iog.getFkUnitId(),
                SLibConsts.UNDEFINED,
                new int[] { iog.getFkWarehouseCompanyId(), iog.getFkWarehouseBranchId(), iog.getFkWarehouseWarehouseId() },
                iog.getFkDivisionId(),
                null,
                iog.getDate(),
                iog.getQtyFinishedGoods());
            result = stock.getResult();
*/

            /*
            result = SSomUtils.obtainWarehouseItemsForBusinessRules(
                    session,
                    false,
                    new int[] { iog.getFkWarehouseCompanyId(), iog.getFkWarehouseBranchId(), iog.getFkWarehouseWarehouseId() },
                    iog.getStock() != null ? iog.getStock().getPrimaryKey() : null,
                    mtDate,
                    iog.getFkItemId(),
                    iog.getFkUnitId(),
                    iog.getQtyFinishedGoods(),
                    "",
                    false,
                    false);
            */
/*
            if (!result.isEmpty()) {
                break;
            }
        }
        */

        if (result.isEmpty() &&
                !paAuxConversionIogIn.isEmpty()) {

            for (SDbIog iog : paAuxConversionIogIn) {
                result = SSomUtils.obtainWarehouseItemsForBusinessRules(
                        session,
                        new int[] { iog.getFkWarehouseCompanyId(), iog.getFkWarehouseBranchId(), iog.getFkWarehouseWarehouseId() },
                        iog.getStock() != null ? iog.getStock().getPrimaryKey() : null,
                        ptDate,
                        iog.getFkItemId(),
                        iog.getFkUnitId(),
                        iog.getQuantity(),
                        "",
                        true,
                        false,
                        pnPkMixId);

                if (!result.isEmpty()) {
                    break;
                }
            }
        }

        return result;
    }

    private static String conversionSaveIogsOut(final SGuiSession session, final ArrayList<SDbIog> paAuxConversionIogOut) throws Exception {
        String result = "";

        for (SDbIog iog : paAuxConversionIogOut) {
            if (iog.canSave(session)) {

                iog.save(session);
                if (iog.getQueryResultId() != SDbConsts.SAVE_OK) {

                    result = iog.getQueryResult();
                    break;
                }
            }
            else {
                result = iog.getQueryResult();
                break;
            }
        }

        return result;
    }

    private static String conversionSaveIogsIn(final SGuiSession session, final ArrayList<SDbIog> maAuxConversionIogIn) throws Exception {
        String result = "";

        for (SDbIog iog : maAuxConversionIogIn) {
            // if (iog.canSave(session)) {

                iog.save(session);
                if (iog.getQueryResultId() != SDbConsts.SAVE_OK) {

                    result = iog.getQueryResult();
                    break;
                }
            //}
            /*else {
                result = iog.getQueryResult();
                break;
            }*/
        }

        return result;
    }

    private static SSomStock conversionValidateWarehouseEmpty(final SGuiSession session, final Date date, final int nItemId, final int nUnitId, final int[] naWarehouse, final int nDivisionId) {
        SSomStock stock = null;

        stock = SSomUtils.obtainStock(
                session,
                SLibTimeUtils.digestYear(date)[0],
                nItemId,
                nUnitId,
                SModSysConsts.SS_ITEM_TP_FG,
                naWarehouse,
                nDivisionId,
                null,
                date,
                false,
                false);

         return stock;
    }

    private static ArrayList<SDbIog> conversionConvertIogsOutToIn(final ArrayList<SDbIog> paAuxConversionIogOut, final int pnFkItemDestinyId, final int pnFkUnitDestinyId,
            final int[] panFkWarehouseDestinyCompanyId) throws CloneNotSupportedException {
        SDbIog iogIn = null;

        ArrayList<SDbIog> aAuxConversionIogIn = new ArrayList<SDbIog>();

        aAuxConversionIogIn.clear();
        for (SDbIog iogOut : paAuxConversionIogOut) {

            iogIn = iogOut.clone();
            iogIn.setRegistryNew(true);
            iogIn.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_IN_INT_CNV[0]);
            iogIn.setFkIogClassId(SModSysConsts.SS_IOG_TP_IN_INT_CNV[1]);
            iogIn.setFkIogTypeId(SModSysConsts.SS_IOG_TP_IN_INT_CNV[2]);
            iogIn.setFkItemId(pnFkItemDestinyId);
            iogIn.setFkUnitId(pnFkUnitDestinyId);
            iogIn.setFkWarehouseCompanyId(panFkWarehouseDestinyCompanyId[0]);
            iogIn.setFkWarehouseBranchId(panFkWarehouseDestinyCompanyId[1]);
            iogIn.setFkWarehouseWarehouseId(panFkWarehouseDestinyCompanyId[2]);

            aAuxConversionIogIn.add(iogIn);
        }

        return aAuxConversionIogIn;
    }

    private static String conversionDelete(final SGuiSession session,
            final int[] panAuxWarehouseSourceId, final int pnFkItemSourceId, final int pnFkUnitSourceId,
            final int[] panAuxWarehouseDestinyId, final int pnFkItemDestinyId, final int pnFkUnitDestinyId,
            final Date ptDate, final boolean pbDeleted,
            final ArrayList<SDbIog> paAuxConversionIogOut, final ArrayList<SDbIog> paAuxConversionIogIn) throws Exception {
        String result = "";

        // Validate if iogs canDelete:

        if (!pbDeleted) {

            result = conversionDeleteIogsOut(session, paAuxConversionIogOut);
        }
        else {

            result = conversionDeleteIogsIn(session, paAuxConversionIogIn);
        }

        if (result.isEmpty()) {
            SSomStock stock = null;

            // Validate if source/destiny warehouse is empty:

            stock = conversionValidateWarehouseEmpty(session, ptDate,
                    !pbDeleted ? pnFkItemSourceId : pnFkItemDestinyId,
                    !pbDeleted ? pnFkUnitSourceId : pnFkUnitDestinyId,
                    !pbDeleted ? panAuxWarehouseSourceId : panAuxWarehouseDestinyId, SLibConsts.UNDEFINED);
            if (stock.getStock() != 0) {

                result = "No se puede eliminar el registro porque el almacén destino no queda vacío durante la eliminación.\n" +
                        "Por diferencia de '" + stock.getItem() + "': " + stock.getStock() + " " + stock.getUnit();
                throw new Exception(result);
            }

            if (result.isEmpty()) {

                // Validate if iogs canDelete:

                if (!pbDeleted) {

                    result = conversionDeleteIogsIn(session, paAuxConversionIogIn);
                }
                else {

                    result = conversionDeleteIogsOut(session, paAuxConversionIogOut);
                }
            }
        }

        // XXX msQueryResult = validateWarehousesStock(session, true, false);

        return result;
    }

    private String mixActiveSave(final SGuiSession session) throws Exception {
        String result = "";

        if (moAuxMixActiveIogOut.canSave(session)) {

            moAuxMixActiveIogOut.save(session);
            if (moAuxMixActiveIogOut.getQueryResultId() != SDbConsts.SAVE_OK) {

                result = moAuxMixActiveIogOut.getQueryResult();
            }
        }
        else {
            result = moAuxMixActiveIogOut.getQueryResult();
        }

        return result;
    }

    private String mixActiveCompute(final SGuiSession session) throws CloneNotSupportedException, Exception {
        String result = "";
        SDbIog iogTransfer = null;

        if (mbRegistryNew) {

            moAuxMixActiveIogOut = new SDbIog();
            moAuxMixActiveIogOut.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_OUT_INT_TRA[0]);
            moAuxMixActiveIogOut.setFkIogClassId(SModSysConsts.SS_IOG_TP_OUT_INT_TRA[1]);
            moAuxMixActiveIogOut.setFkIogTypeId(SModSysConsts.SS_IOG_TP_OUT_INT_TRA[2]);
            moAuxMixActiveIogOut.setFkWarehouseCompanyId(mnFkWarehouseSourceCompanyId);
            moAuxMixActiveIogOut.setFkWarehouseBranchId(mnFkWarehouseSourceBranchId);
            moAuxMixActiveIogOut.setFkWarehouseWarehouseId(mnFkWarehouseSourceWarehouseId);
            moAuxMixActiveIogOut.setFkDivisionId(mnFkDivisionSourceId_n);
            moAuxMixActiveIogOut.setFkIogAdjustmentTypeId(SModSysConsts.SU_IOG_ADJ_TP_NA);
            moAuxMixActiveIogOut.setFkItemId(mnFkItemSourceId);
            moAuxMixActiveIogOut.setFkUnitId(mnFkUnitSourceId);
            moAuxMixActiveIogOut.setFkMixId_n(mnPkMixId);
            moAuxMixActiveIogOut.setDate(mtDate);
            moAuxMixActiveIogOut.setQuantity(mdQuantity);
            moAuxMixActiveIogOut.setXtaNote(msXtaNote);
            moAuxMixActiveIogOut.setSystem(true);

            iogTransfer = moAuxMixActiveIogOut.clone();
            iogTransfer.setFkIogCategoryId(SModSysConsts.SS_IOG_TP_IN_INT_TRA[0]);
            iogTransfer.setFkIogClassId(SModSysConsts.SS_IOG_TP_IN_INT_TRA[1]);
            iogTransfer.setFkIogTypeId(SModSysConsts.SS_IOG_TP_IN_INT_TRA[2]);
            iogTransfer.setFkWarehouseCompanyId(mnFkWarehouseDestinyCompanyId);
            iogTransfer.setFkWarehouseBranchId(mnFkWarehouseDestinyBranchId);
            iogTransfer.setFkWarehouseWarehouseId(mnFkWarehouseDestinyWarehouseId);
            iogTransfer.setFkDivisionId(mnFkDivisionDestinyId_n);

            moAuxMixActiveIogOut.setIogRegistryB(iogTransfer);
            result = conversionSave(session,
                    manAuxWarehouseDestinyId, mnFkDivisionDestinyId_n, mnFkItemDestinyId, mnFkUnitDestinyId,
                    manAuxWarehouseDestinyId, mnFkDivisionSourceId_n, mnFkItemSourceId, mnFkUnitSourceId,
                    ((SSomStock) SSomUtils.obtainStock(session, SLibTimeUtils.digestYear(mtDate)[0], mnFkItemDestinyId, mnFkUnitDestinyId, SLibConsts.UNDEFINED, manAuxWarehouseDestinyId,
                    mnFkDivisionDestinyId_n, null, mtDate, false, false)).getStock(),
                    mtDate, mnPkMixId, mnFkMixTypeId, mbRegistryNew, mbDeleted,
                    maAuxConversionIogOut, maAuxConversionIogIn);
            if (result.isEmpty()) {

                result = mixActiveSave(session);
            }
        }
        else {

            result = conversionSave(session,
                    manAuxWarehouseDestinyId, mnFkDivisionDestinyId_n, mnFkItemDestinyId, mnFkUnitDestinyId,
                    manAuxWarehouseDestinyId, mnFkDivisionSourceId_n, mnFkItemSourceId, mnFkUnitSourceId,
                    ((SSomStock) SSomUtils.obtainStock(session, SLibTimeUtils.digestYear(mtDate)[0], mnFkItemDestinyId, mnFkUnitDestinyId, SLibConsts.UNDEFINED, manAuxWarehouseDestinyId,
                    mnFkDivisionDestinyId_n, null, mtDate, false, false)).getStock(),
                    mtDate, mnPkMixId, mnFkMixTypeId, mbRegistryNew, mbDeleted,
                    maAuxConversionIogOut, maAuxConversionIogIn);
            if (result.isEmpty()) {

                result = mixActiveSave(session);
            }
        }

        return result;
    }

    public String mixActiveDelete(final SGuiSession session) throws Exception {
        String result = "";

        // Delete tranfer for validate conversion:

        /*
        // Reactive transfer after conversionDelete:

        moAuxMixActiveIogOut.delete(session);
        result = moAuxMixActiveIogOut.getQueryResult();
        if (result.isEmpty()) {
        */

        if (mbDeleted) {
            if (moAuxMixActiveIogOut.canDelete(session)) {

                moAuxMixActiveIogOut.delete(session);
            }
            result = moAuxMixActiveIogOut.getQueryResult();
        }

        if (result.isEmpty()) {

            result = conversionDelete(session,
                manAuxWarehouseDestinyId, mnFkItemDestinyId, mnFkUnitDestinyId,
                manAuxWarehouseDestinyId, mnFkItemSourceId, mnFkUnitSourceId,
                mtDate, mbDeleted, maAuxConversionIogOut, maAuxConversionIogIn);
            if (result.isEmpty()) {

                if (!mbDeleted) {
                    if (moAuxMixActiveIogOut.canDelete(session)) {

                        moAuxMixActiveIogOut.delete(session);
                    }
                    result = moAuxMixActiveIogOut.getQueryResult();
                }
            }
        }

        //}

        // moAuxMixActiveIogOut.delete(session);
        // result = moAuxMixActiveIogOut.getQueryResult();



        return result;
    }

    /*
     * Public methods
     */

    public void setPkMixId(int n) { mnPkMixId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setAuthorized(boolean b) { mbAuthorized = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkMixTypeId(int n) { mnFkMixTypeId = n; }
    public void setFkItemSourceId(int n) { mnFkItemSourceId = n; }
    public void setFkUnitSourceId(int n) { mnFkUnitSourceId = n; }
    public void setFkItemDestinyId(int n) { mnFkItemDestinyId = n; }
    public void setFkUnitDestinyId(int n) { mnFkUnitDestinyId = n; }
    public void setFkWarehouseSourceCompanyId(int n) { mnFkWarehouseSourceCompanyId = n; }
    public void setFkWarehouseSourceBranchId(int n) { mnFkWarehouseSourceBranchId = n; }
    public void setFkWarehouseSourceWarehouseId(int n) { mnFkWarehouseSourceWarehouseId = n; }
    public void setFkDivisionSourceId_n(int n) { mnFkDivisionSourceId_n = n; }
    public void setFkWarehouseDestinyCompanyId(int n) { mnFkWarehouseDestinyCompanyId = n; }
    public void setFkWarehouseDestinyBranchId(int n) { mnFkWarehouseDestinyBranchId = n; }
    public void setFkWarehouseDestinyWarehouseId(int n) { mnFkWarehouseDestinyWarehouseId = n; }
    public void setFkDivisionDestinyId_n(int n) { mnFkDivisionDestinyId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setFkUserAuthorizationId(int n) { mnFkUserAuthorizationId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    public void setTsUserAuthorization(Date t) { mtTsUserAuthorization = t; }

    public int getPkMixId() { return mnPkMixId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public double getQuantity() { return mdQuantity; }
    public boolean isAuthorized() { return mbAuthorized; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkMixTypeId() { return mnFkMixTypeId; }
    public int getFkItemSourceId() { return mnFkItemSourceId; }
    public int getFkUnitSourceId() { return mnFkUnitSourceId; }
    public int getFkItemDestinyId() { return mnFkItemDestinyId; }
    public int getFkUnitDestinyId() { return mnFkUnitDestinyId; }
    public int getFkWarehouseSourceCompanyId() { return mnFkWarehouseSourceCompanyId; }
    public int getFkWarehouseSourceBranchId() { return mnFkWarehouseSourceBranchId; }
    public int getFkWarehouseSourceWarehouseId() { return mnFkWarehouseSourceWarehouseId; }
    public int getFkDivisionSourceId_n() { return mnFkDivisionSourceId_n; }
    public int getFkWarehouseDestinyCompanyId() { return mnFkWarehouseDestinyCompanyId; }
    public int getFkWarehouseDestinyBranchId() { return mnFkWarehouseDestinyBranchId; }
    public int getFkWarehouseDestinyWarehouseId() { return mnFkWarehouseDestinyWarehouseId; }
    public int getFkDivisionDestinyId_n() { return mnFkDivisionDestinyId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public int getFkUserAuthorizationId() { return mnFkUserAuthorizationId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    public Date getTsUserAuthorization() { return mtTsUserAuthorization; }

    public void setAuxWarehouseSourceId(int[] n) { manAuxWarehouseSourceId = n; }
    public void setAuxWarehouseDestinyId(int[] n) { manAuxWarehouseDestinyId = n; }
    public void setAuxMixPassiveIogIn(SDbIog o) { moAuxMixPassiveIogIn = o; }
    public void setAuxMixPassiveIogOut(SDbIog o) { moAuxMixPassiveIogOut = o; }
    public void setAuxMixActiveIogOut(SDbIog o) { moAuxMixActiveIogOut = o; }
    public void setMixNoteChild(SDbMixNote o) { moMixNoteChild = o; }
    public void setAuxNote(String s) { msXtaNote = s; }

    public int[] getAuxWarehouseSourceId() { return manAuxWarehouseSourceId; }
    public int[] getAuxWarehouseDestinyId() { return manAuxWarehouseDestinyId; }
    public SDbIog getAuxMixPassiveIogIn() { return moAuxMixPassiveIogIn; }
    public SDbIog getAuxMixPassiveIogOut() { return moAuxMixPassiveIogOut; }
    public SDbIog getAuxMixActiveIogOut() { return moAuxMixActiveIogOut; }
    public ArrayList<SDbIog> getAuxConvertionIogIn() { return maAuxConversionIogIn; }
    public ArrayList<SDbIog> getAuxConvertionIogOut() { return maAuxConversionIogOut; }
    public SDbMixNote getMixNoteChild() { return moMixNoteChild; }
    public String getAuxNote() { return msXtaNote; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMixId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMixId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMixId = 0;
        mnNumber = 0;
        mtDate = null;
        mdQuantity = 0;
        mbAuthorized = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkMixTypeId = 0;
        mnFkItemSourceId = 0;
        mnFkUnitSourceId = 0;
        mnFkItemDestinyId = 0;
        mnFkUnitDestinyId = 0;
        mnFkWarehouseSourceCompanyId = 0;
        mnFkWarehouseSourceBranchId = 0;
        mnFkWarehouseSourceWarehouseId = 0;
        mnFkDivisionSourceId_n = 0;
        mnFkWarehouseDestinyCompanyId = 0;
        mnFkWarehouseDestinyBranchId = 0;
        mnFkWarehouseDestinyWarehouseId = 0;
        mnFkDivisionDestinyId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mnFkUserAuthorizationId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        mtTsUserAuthorization = null;

        moMixNoteChild = null;

        manAuxWarehouseSourceId = null;
        manAuxWarehouseDestinyId = null;

        moAuxMixPassiveIogIn = null;
        moAuxMixPassiveIogOut = null;

        moAuxMixActiveIogOut = null;

        maAuxConversionIogIn = new ArrayList<SDbIog>();
        maAuxConversionIogOut = new ArrayList<SDbIog>();

        msXtaNote = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mix = " + mnPkMixId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mix = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMixId = 0;

        msSql = "SELECT COALESCE(MAX(id_mix), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMixId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        ResultSet resultSet = null;
        Statement statement = null;

        SDbIog iog = null;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkMixId = resultSet.getInt("id_mix");
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            mdQuantity = resultSet.getDouble("qty");
            mbAuthorized = resultSet.getBoolean("b_aut");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkMixTypeId = resultSet.getInt("fk_mix_tp");
            mnFkItemSourceId = resultSet.getInt("fk_item_src");
            mnFkUnitSourceId = resultSet.getInt("fk_unit_src");
            mnFkItemDestinyId = resultSet.getInt("fk_item_des");
            mnFkUnitDestinyId = resultSet.getInt("fk_unit_des");
            mnFkWarehouseSourceCompanyId = resultSet.getInt("fk_wah_src_co");
            mnFkWarehouseSourceBranchId = resultSet.getInt("fk_wah_src_cob");
            mnFkWarehouseSourceWarehouseId = resultSet.getInt("fk_wah_src_wah");
            mnFkDivisionSourceId_n = resultSet.getInt("fk_div_src_n");
            mnFkWarehouseDestinyCompanyId = resultSet.getInt("fk_wah_des_co");
            mnFkWarehouseDestinyBranchId = resultSet.getInt("fk_wah_des_cob");
            mnFkWarehouseDestinyWarehouseId = resultSet.getInt("fk_wah_des_wah");
            mnFkDivisionDestinyId_n = resultSet.getInt("fk_div_des_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mnFkUserAuthorizationId = resultSet.getInt("fk_usr_aut");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            mtTsUserAuthorization = resultSet.getTimestamp("ts_usr_aut");

            manAuxWarehouseSourceId = new int[] { mnFkWarehouseSourceCompanyId, mnFkWarehouseSourceBranchId, mnFkWarehouseSourceWarehouseId };
            manAuxWarehouseDestinyId = new int[] { mnFkWarehouseDestinyCompanyId, mnFkWarehouseDestinyBranchId, mnFkWarehouseDestinyWarehouseId };

            // Read note:

            msSql = "SELECT id_mix, id_note, note FROM " + SModConsts.TablesMap.get(SModConsts.S_MIX_NOTE) + " WHERE id_mix = " + mnPkMixId + " ";
            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(msSql);
            if (!resultSet.next()) { }
            else {
                moMixNoteChild = new SDbMixNote();
                moMixNoteChild.read(session, new int[] { resultSet.getInt("id_mix"), resultSet.getInt("id_note") });
                msXtaNote = moMixNoteChild.getNote();
            }

            // Read mix registries:

            msSql = "SELECT id_iog, fk_iog_ct, fk_iog_cl, fk_iog_tp " +
                "FROM " +  SModConsts.TablesMap.get(SModConsts.S_IOG) + " " +
                "WHERE fk_mix_n = " + mnPkMixId + " ";
            resultSet = statement.executeQuery(msSql);
            switch (mnFkMixTypeId) {
                case SModSysConsts.SS_MIX_TP_MIX_PAS:

                    while (resultSet.next()) {
                        if (resultSet.getInt("fk_iog_ct") == SModSysConsts.SS_IOG_CT_IN) {
                            moAuxMixPassiveIogIn = new SDbIog();
                            moAuxMixPassiveIogIn.read(session, new int[] { resultSet.getInt("id_iog") });
                        }
                        else if (resultSet.getInt("fk_iog_ct") == SModSysConsts.SS_IOG_CT_OUT) {
                            moAuxMixPassiveIogOut = new SDbIog();
                            moAuxMixPassiveIogOut.read(session, new int[] { resultSet.getInt("id_iog") });
                        }
                    }

                    if (moAuxMixPassiveIogIn == null || moAuxMixPassiveIogOut == null) {
                        msQueryResult = "Error al leer los movimientos de inventarios de la mezcla pasiva.";
                    }

                    break;

                case SModSysConsts.SS_MIX_TP_MIX_ACT:

                    maAuxConversionIogIn.clear();
                    maAuxConversionIogOut.clear();
                    moAuxMixActiveIogOut = null;
                    while (resultSet.next()) {

                        iog = new SDbIog();
                        iog.read(session, new int[] { resultSet.getInt("id_iog") });
                        if ((!SLibUtils.compareKeys(new int[] { resultSet.getInt("fk_iog_ct"), resultSet.getInt("fk_iog_cl"), resultSet.getInt("fk_iog_tp") }, SModSysConsts.SS_IOG_TP_OUT_INT_TRA) &&
                                !SLibUtils.compareKeys(new int[] { resultSet.getInt("fk_iog_ct"), resultSet.getInt("fk_iog_cl"), resultSet.getInt("fk_iog_tp") }, SModSysConsts.SS_IOG_TP_IN_INT_TRA)) &&
                                resultSet.getInt("fk_iog_ct") == SModSysConsts.SS_IOG_CT_IN) {

                            maAuxConversionIogIn.add(iog);
                        }
                        else if ((!SLibUtils.compareKeys(new int[] { resultSet.getInt("fk_iog_ct"), resultSet.getInt("fk_iog_cl"), resultSet.getInt("fk_iog_tp") }, SModSysConsts.SS_IOG_TP_OUT_INT_TRA) &&
                                !SLibUtils.compareKeys(new int[] { resultSet.getInt("fk_iog_ct"), resultSet.getInt("fk_iog_cl"), resultSet.getInt("fk_iog_tp") }, SModSysConsts.SS_IOG_TP_IN_INT_TRA)) &&
                                resultSet.getInt("fk_iog_ct") == SModSysConsts.SS_IOG_CT_OUT) {

                            maAuxConversionIogOut.add(iog);
                        }
                        else if (SLibUtils.compareKeys(new int[] { resultSet.getInt("fk_iog_ct"), resultSet.getInt("fk_iog_cl"), resultSet.getInt("fk_iog_tp") }, SModSysConsts.SS_IOG_TP_OUT_INT_TRA)) {

                            moAuxMixActiveIogOut = new SDbIog();
                            moAuxMixActiveIogOut.read(session, new int[] { resultSet.getInt("id_iog") });
                        }
                    }

                    if (maAuxConversionIogIn.size() != maAuxConversionIogOut.size() ||
                            moAuxMixActiveIogOut == null) {

                        msQueryResult = "Error al leer los movimientos de inventarios de la conversión.";
                    }

                    break;

                case SModSysConsts.SS_MIX_TP_CNV:

                    maAuxConversionIogIn.clear();
                    maAuxConversionIogOut.clear();
                    while (resultSet.next()) {

                        iog = new SDbIog();
                        iog.read(session, new int[] { resultSet.getInt("id_iog") });
                        if (resultSet.getInt("fk_iog_ct") == SModSysConsts.SS_IOG_CT_IN) {

                            maAuxConversionIogIn.add(iog);
                        }
                        else if (resultSet.getInt("fk_iog_ct") == SModSysConsts.SS_IOG_CT_OUT) {

                            maAuxConversionIogOut.add(iog);
                        }
                    }

                    if (maAuxConversionIogIn.size() != maAuxConversionIogOut.size()) {
                        msQueryResult = "Error al leer los movimientos de inventarios de la conversión.";
                    }

                    break;
            }

        }

        if (msQueryResult.isEmpty()) {
            mbRegistryNew = false;
            mnQueryResultId = SDbConsts.READ_OK;
        }
        else {
            mbRegistryNew = true;
            mnQueryResultId = SDbConsts.READ_ERROR;
        }
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            computeNumber(session);

            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            mnFkUserAuthorizationId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkMixId + ", " +
                    mnNumber + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mdQuantity + ", " +
                    (mbAuthorized ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkMixTypeId + ", " +
                    mnFkItemSourceId + ", " +
                    mnFkUnitSourceId + ", " +
                    mnFkItemDestinyId + ", " +
                    mnFkUnitDestinyId + ", " +
                    mnFkWarehouseSourceCompanyId + ", " +
                    mnFkWarehouseSourceBranchId + ", " +
                    mnFkWarehouseSourceWarehouseId + ", " +
                    (mnFkDivisionSourceId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkDivisionSourceId_n) + ", " +
                    mnFkWarehouseDestinyCompanyId + ", " +
                    mnFkWarehouseDestinyBranchId + ", " +
                    mnFkWarehouseDestinyWarehouseId + ", " +
                    (mnFkDivisionDestinyId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkDivisionDestinyId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    mnFkUserAuthorizationId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_mix = " + mnPkMixId + ", " +
                    */
                    "num = " + mnNumber + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "qty = " + mdQuantity + ", " +
                    "b_aut = " + (mbAuthorized ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_mix_tp = " + mnFkMixTypeId + ", " +
                    "fk_item_src = " + mnFkItemSourceId + ", " +
                    "fk_unit_src = " + mnFkUnitSourceId + ", " +
                    "fk_item_des = " + mnFkItemDestinyId + ", " +
                    "fk_unit_des = " + mnFkUnitDestinyId + ", " +
                    "fk_wah_src_co = " + mnFkWarehouseSourceCompanyId + ", " +
                    "fk_wah_src_cob = " + mnFkWarehouseSourceBranchId + ", " +
                    "fk_wah_src_wah = " + mnFkWarehouseSourceWarehouseId + ", " +
                    "fk_div_src_n = " + (mnFkDivisionSourceId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkDivisionSourceId_n) + ", " +
                    "fk_wah_des_co = " + mnFkWarehouseDestinyCompanyId + ", " +
                    "fk_wah_des_cob = " + mnFkWarehouseDestinyBranchId + ", " +
                    "fk_wah_des_wah = " + mnFkWarehouseDestinyWarehouseId + ", " +
                    "fk_div_des_n = " + (mnFkDivisionDestinyId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkDivisionDestinyId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"fk_usr_aut = " + mnFkUserAuthorizationId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    //"ts_usr_aut = " + "NOW()" + ", " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save note:

        if (mbRegistryNew) {

            moMixNoteChild = new SDbMixNote();
            moMixNoteChild.setPkMixId(mnPkMixId);
            moMixNoteChild.setNote(msXtaNote);
            moMixNoteChild.save(session);
        }
        else {

            moMixNoteChild.setNote(msXtaNote);
            moMixNoteChild.save(session);
        }

        // Save registries for mix:

        switch (mnFkMixTypeId) {
            case SModSysConsts.SS_MIX_TP_MIX_PAS:

                msQueryResult = mixPassiveSave(session);
                break;

            case SModSysConsts.SS_MIX_TP_MIX_ACT:

                msQueryResult = mixActiveCompute(session);
                break;

            case SModSysConsts.SS_MIX_TP_CNV:

                msQueryResult = conversionSave(session,
                        manAuxWarehouseSourceId, mnFkDivisionSourceId_n, mnFkItemSourceId, mnFkUnitSourceId,
                        manAuxWarehouseDestinyId, mnFkDivisionDestinyId_n, mnFkItemDestinyId, mnFkUnitDestinyId,
                        mdQuantity, mtDate, mnPkMixId, mnFkMixTypeId, mbRegistryNew, mbDeleted,
                        maAuxConversionIogOut, maAuxConversionIogIn);
                break;
        }

        if (msQueryResult.isEmpty()) {

            mbRegistryNew = false;
            mnQueryResultId = SDbConsts.SAVE_OK;
        }
        else {
            throw new Exception(msQueryResult);
        }
    }

    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        mbDeleted = !mbDeleted;
        mnFkUserUpdateId = session.getUser().getPkUserId();

        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();
        session.getStatement().execute(msSql);

        switch (mnFkMixTypeId) {
            case SModSysConsts.SS_MIX_TP_MIX_PAS:

                moAuxMixPassiveIogIn.delete(session);
                moAuxMixPassiveIogOut.delete(session);
                msQueryResult = validateMixInOutMoves(session, !mbDeleted);
                break;

            case SModSysConsts.SS_MIX_TP_MIX_ACT:

                msQueryResult = mixActiveDelete(session);
                break;

            case SModSysConsts.SS_MIX_TP_CNV:

                msQueryResult = conversionDelete(session, manAuxWarehouseSourceId, mnFkItemSourceId, mnFkUnitSourceId, manAuxWarehouseDestinyId, mnFkItemDestinyId,
                        mnFkUnitDestinyId, mtDate, mbDeleted, maAuxConversionIogOut, maAuxConversionIogIn);
                break;

            default:
        }

        if (!msQueryResult.isEmpty()) {
            throw new Exception(msQueryResult);
        }

        mnQueryResultId = msQueryResult.isEmpty() ? SDbConsts.SAVE_OK : SDbConsts.SAVE_ERROR;
    }

    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {

        switch (mnFkMixTypeId) {
            case SModSysConsts.SS_MIX_TP_MIX_PAS:

                msQueryResult = validateMixInOutMoves(session, !mbDeleted);

                if (msQueryResult.isEmpty() && !moAuxMixPassiveIogIn.canDelete(session)) {

                    msQueryResult = moAuxMixPassiveIogIn.getQueryResult();
                }

                if (msQueryResult.isEmpty() && !moAuxMixPassiveIogOut.canDelete(session)) {

                    msQueryResult = moAuxMixPassiveIogOut.getQueryResult();
                }
                break;

            case SModSysConsts.SS_MIX_TP_MIX_ACT:

                /*
                *  Validation is in function mixActiveDelete
                */
                break;

            case SModSysConsts.SS_MIX_TP_CNV:

                msQueryResult = validateMixInOutMoves(session, !mbDeleted);
                break;

            default:
        }

        return msQueryResult.isEmpty() ? true : false;
    }

    @Override
    public boolean canSave(SGuiSession session) throws SQLException, Exception {

        msQueryResult = validateInputFields();
        if (msQueryResult.isEmpty()) {
            switch (mnFkMixTypeId) {
                case SModSysConsts.SS_MIX_TP_MIX_PAS:

                    msQueryResult = validateMixInOutMoves(session, false);
                    break;

                case SModSysConsts.SS_MIX_TP_MIX_ACT:

                    msQueryResult = validateMixInOutMoves(session, false);
                    break;

                case SModSysConsts.SS_MIX_TP_CNV:

                    msQueryResult = validateWarehousesStock(session, false, mtDate, mnFkItemSourceId, mnFkItemDestinyId, mnFkUnitSourceId, mnFkUnitDestinyId, manAuxWarehouseSourceId,
                        manAuxWarehouseDestinyId, mnFkDivisionSourceId_n, mnFkDivisionDestinyId_n, mdQuantity, mnFkMixTypeId, mbRegistryNew);
                    break;

                default:
            }
        }

        return msQueryResult.isEmpty() ? true : false;
    }

    @Override
    public SDbMix clone() throws CloneNotSupportedException {
        SDbMix registry = new SDbMix();

        registry.setPkMixId(this.getPkMixId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setQuantity(this.getQuantity());
        registry.setAuthorized(this.isAuthorized());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkMixTypeId(this.getFkMixTypeId());
        registry.setFkItemSourceId(this.getFkItemSourceId());
        registry.setFkUnitSourceId(this.getFkUnitSourceId());
        registry.setFkItemDestinyId(this.getFkItemDestinyId());
        registry.setFkUnitDestinyId(this.getFkUnitDestinyId());
        registry.setFkWarehouseSourceCompanyId(this.getFkWarehouseSourceCompanyId());
        registry.setFkWarehouseSourceBranchId(this.getFkWarehouseSourceBranchId());
        registry.setFkWarehouseSourceWarehouseId(this.getFkWarehouseSourceWarehouseId());
        registry.setFkDivisionSourceId_n(this.getFkDivisionSourceId_n());
        registry.setFkWarehouseDestinyCompanyId(this.getFkWarehouseDestinyCompanyId());
        registry.setFkWarehouseDestinyBranchId(this.getFkWarehouseDestinyBranchId());
        registry.setFkWarehouseDestinyWarehouseId(this.getFkWarehouseDestinyWarehouseId());
        registry.setFkDivisionDestinyId_n(this.getFkDivisionDestinyId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setFkUserAuthorizationId(this.getFkUserAuthorizationId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        registry.setTsUserAuthorization(this.getTsUserAuthorization());
        registry.setRegistryNew(this.isRegistryNew());

        registry.setAuxWarehouseSourceId(this.getAuxWarehouseSourceId() != null ? this.getAuxWarehouseSourceId() : null);
        registry.setAuxWarehouseDestinyId(this.getAuxWarehouseDestinyId() != null ? this.getAuxWarehouseDestinyId() : null);
        registry.setAuxMixPassiveIogIn(this.getAuxMixPassiveIogIn() != null ? this.getAuxMixPassiveIogIn().clone() : null);
        registry.setAuxMixPassiveIogOut(this.getAuxMixPassiveIogOut() != null ? this.getAuxMixPassiveIogOut().clone() : null);
        registry.setAuxMixActiveIogOut(this.getAuxMixActiveIogOut() != null ? this.getAuxMixActiveIogOut().clone() : null);
        registry.setMixNoteChild(this.getMixNoteChild() != null ? this.getMixNoteChild().clone() : null);
        registry.setAuxNote(this.getAuxNote());

        for (SDbIog iog : maAuxConversionIogOut) {
            registry.getAuxConvertionIogOut().add(iog.clone());
        }

        for (SDbIog iog : maAuxConversionIogIn) {
            registry.getAuxConvertionIogIn().add(iog.clone());
        }

        return registry;
    }
}
