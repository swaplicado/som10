/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiOptionPickerSettings;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SCfgUtils;
import som.mod.mat.form.SDialogTicketPicker;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Isabel Servín
 */
public abstract class SMaterialUtils {
    
    public static String TIC_HAS_MOV = "has_mov";
    public static String TIC_IS_PEND = "is_pend";
    
    public static String getParamMaterialItemsIds(SGuiSession session) {
        String matItems = "6";
        try {
            matItems = SCfgUtils.getParamValue(session.getStatement(), SModSysConsts.C_PARAM_MAT_ITEMS).replace(";", ",");
        }
        catch (Exception e) {
            System.err.println(SMaterialUtils.class.getName() + " " + e);
        }
        return matItems;
    }
    
    public static int getParamScaleDefault(SGuiSession session) {
        int scaId = 0;
        try {
            scaId = SLibUtilities.parseInt(SCfgUtils.getParamValue(session.getStatement(), SModSysConsts.C_PARAM_MAT_SCA_DEF));
        }
        catch (Exception e) {
            System.err.println(SMaterialUtils.class.getName() + " " + e);
        }
        return scaId;
    }
    
    public static Date getSuggestMovementDate(SGuiSession session) {
        try {
            Date now = new Date();
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
            SimpleDateFormat minFormat = new SimpleDateFormat("mm");
            int curHour = SLibUtilities.parseInt(hourFormat.format(now));
            int curMin = SLibUtilities.parseInt(minFormat.format(now));
            String paramTime[] = getDayCutOff(session).split(":");
            int paramHour = SLibUtilities.parseInt(paramTime[0]);
            int paramMin = SLibUtilities.parseInt(paramTime[1]);
            
            if (curHour > paramHour || (curHour == paramHour && curMin > paramMin)) {
                return SLibTimeUtils.addDate(now, 0, 0, 1);
            }
        }
        catch (Exception e) {
            System.err.println(SMaterialUtils.class.getName() + " " + e);
        }
        return new Date();
    }
    
    public static int getParamMaterialWarehouseDefault(SGuiSession session) throws Exception {
        return SLibUtilities.parseInt(SCfgUtils.getParamValue(session.getStatement(), SModSysConsts.C_PARAM_MAT_WAH_DEF));
    }
    
    public static int getParamUnitDefault(SGuiSession session) throws Exception {
        return SLibUtilities.parseInt(SCfgUtils.getParamValue(session.getStatement(), SModSysConsts.C_PARAM_MAT_UNIT_DEF));
    }
    
    public static String getDayCutOff(SGuiSession session) throws Exception {
        return SCfgUtils.getParamValue(session.getStatement(), SModSysConsts.C_PARAM_MAT_DAY_CUTOFF_HR);
    }
    
    public static String getTicketMovementSerie(SGuiSession session, int[] pk) {
        String serie = "";
        try {
            String sql = "SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.MS_MVT_CL) + " "
                    + "WHERE id_iog_ct = " + pk[0] + " AND id_mvt_cl = " + pk[1];
            try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
                if (resultSet.next()) {
                    serie = resultSet.getString(1);
                }
            }
        }
        catch (Exception e) {
            System.err.println(SMaterialUtils.class.getName() + " " + e);
        }
        return serie;
    }
    
    public static SDialogTicketPicker getOptionTicketPicker(SGuiClient client) {
        String matItems = getParamMaterialItemsIds(client.getSession());
        int scaId = getParamScaleDefault(client.getSession());
        
        SDialogTicketPicker picker = new SDialogTicketPicker();
        ArrayList<SGridColumnForm> gridColumns = new ArrayList<>();
        SGuiOptionPickerSettings settings;
        
        String sql = "SELECT " +
                "t.id_tic AS " + SDbConsts.FIELD_ID + "1, " +
                "sca.code AS " + SDbConsts.FIELD_PICK + "1, " +
                "t.num AS " + SDbConsts.FIELD_PICK + "2, " +
                "t.dt AS " + SDbConsts.FIELD_PICK + "3, " +
                "p.name AS " + SDbConsts.FIELD_PICK + "4, " +
                "p.name_trd AS " + SDbConsts.FIELD_PICK + "5, " +
                "t.drv AS " + SDbConsts.FIELD_PICK + "6, " +
                "CONCAT(t.pla, IF(t.pla_cag = '', '', ', '), t.pla_cag) AS " + SDbConsts.FIELD_PICK + "7, " +
                "i.name AS " + SDbConsts.FIELD_PICK + "8, " +
                "t.pac_qty_arr AS " + SDbConsts.FIELD_PICK + "9, " +
                "@recibido := (SELECT SUM(IF(m.fk_iog_ct = " + SModSysConsts.SS_IOG_CT_IN + ", qty, -qty)) FROM " + SModConsts.TablesMap.get(SModConsts.M_MVT) + " AS m " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.M_MVT_ETY) + " AS me ON m.id_mvt = me.id_mvt " +
                "WHERE m.fk_mvt_cl = " + SModSysConsts.MS_MVT_CL_IN_REC[1] + " " +
                "AND NOT m.b_del " +
                "AND m.fk_tic_n = t.id_tic " +
                "GROUP BY m.fk_tic_n) AS " + SDbConsts.FIELD_PICK + "10, " +
                "@x_recibir := (t.pac_qty_arr - @recibido) AS " + SDbConsts.FIELD_PICK + "11, " +
                "i.paq_name AS " + SDbConsts.FIELD_PICK + "12, " +
                "IF(m.id_mvt IS NOT NULL OR me.id_mvt IS NOT NULL, 1, 0) " + TIC_HAS_MOV + ", " +
                "IF(@x_recibir > 0, 1, 0) " + TIC_IS_PEND + " " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS t " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sca ON t.fk_sca = sca.id_sca " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON t.fk_prod = p.id_prod " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON t.fk_item = i.id_item " +
                "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.M_MVT) + " AS m ON t.id_tic = m.fk_tic_n AND NOT m.b_del " +
                "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.M_MVT_ETY) + " AS me ON m.id_mvt = me.id_mvt " +
                "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.M_TIC_ST) + " AS s ON t.id_tic = s.id_tic " +
                "WHERE t.fk_item IN("+ matItems +") AND NOT t.b_del " +
                (scaId == 0 ? "" : "AND sca.id_sca = " + scaId + " ") +
                "AND (s.fk_tic_st IS NULL OR s.fk_tic_st <> 1 OR s.b_del) " +
                "GROUP BY sca.code, t.num, t.dt, p.name " +
                "ORDER BY sca.code, t.num, t.dt, p.name;" ;
        
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Báscula"));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_RAW, "Boleto", 50));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha boleto"));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "Proveedor"));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Proveedor nombre comercial", 120));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Chofer"));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Placas y placas caja", 130));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Ítem", 130));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_2B, "Cant empaq lleno entrada (" + SSomConsts.PIECE + ")"));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_2B, "Cant recibida (" + SSomConsts.PIECE + ")"));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_2B, "Cant pendiente (" + SSomConsts.PIECE + ")"));
        gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Empaque", 50));
        settings = new SGuiOptionPickerSettings("Seleccionar boleto", sql, gridColumns, 1);
        
        picker.setPickerSettings(client, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, settings);
        picker.initComponentsCustom();
        
        return picker;
    }
    
    public static ArrayList<SRowReferencePicker> getReferenceTicketRows(SGuiSession session, int ticId) {
        ArrayList<SRowReferencePicker> rows = new ArrayList<>();
        try {
            String sql = "SELECT CONCAT(m.ref, ':00') ref, MIN(m.dt), c.name, ui.name, m.ts_usr_ins, uu.name, m.ts_usr_upd FROM m_mvt m " +
                "INNER JOIN ms_mvt_cl c ON m.fk_iog_ct = c.id_iog_ct AND m.fk_mvt_cl = c.id_mvt_cl " +
                "INNER JOIN cu_usr ui ON m.fk_usr_ins = ui.id_usr " +
                "INNER JOIN cu_usr uu ON m.fk_usr_upd = uu.id_usr " +
                "WHERE m.fk_iog_ct = 1 " +
                "AND m.fk_tic_n = " + ticId + " " +
                "group by m.ref " +
                "ORDER BY m.ref;";
            try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
                while (resultSet.next()) {
                    SRowReferencePicker row = new SRowReferencePicker();
                    row.setReference(resultSet.getTimestamp(1));
                    row.setMovementClass(resultSet.getString(3));
                    row.setDate(resultSet.getTimestamp(2));
                    row.setUsrNew(resultSet.getString(4));
                    row.setTsUsrNew(resultSet.getTimestamp(5));
                    row.setUsrEdit(resultSet.getString(6));
                    row.setTsUsrEdit(resultSet.getTimestamp(7));
                    rows.add(row);
                }
            }
        }
        catch (Exception e) {
            System.err.println(SMaterialUtils.class.getName() + " " + e);
        }
        return rows;
    }
    
    public static HashMap<Integer, Integer> getStockByTicket(SGuiSession session, int ticId, Date date, int matCond) {
        HashMap<Integer, Integer> stk = new HashMap<>();
        try {
            int year = SLibTimeUtils.digestYear(date)[0];
            String sql = "SELECT SUM(qty_in) - SUM(qty_out) stk, fk_mat_cond, fk_tic_n FROM m_stk " +
                    "WHERE fk_tic_n = " + ticId + " AND YEAR(dt) = " + year + " AND dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                    "AND NOT b_del " +
                    (matCond != 0 ? "AND fk_mat_cond = " + matCond + " " : "") +
                    "GROUP BY id_year, id_wah, id_item, id_unit, fk_mat_cond, fk_tic_n " +
                    "HAVING (SUM(qty_in) - SUM(qty_out)) <> 0";
            try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
                while (resultSet.next()) {
                    stk.put(resultSet.getInt("fk_mat_cond"), resultSet.getInt("stk"));
                }
            }
        }
        catch (Exception e) {
            System.err.println(SMaterialUtils.class.getName() + " " + e);
        }
        return stk;
    }
    
    public static int getQtyRecibedByTicket(SGuiSession session, int ticId) {
        int qty = 0;
        try {
            String sql = "SELECT SUM(IF(m.fk_iog_ct = " + SModSysConsts.SS_IOG_CT_IN + ", qty, -qty)) " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.M_MVT) + " AS m " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.M_MVT_ETY) + " AS me ON m.id_mvt = me.id_mvt " +
                    "WHERE m.fk_mvt_cl = " + SModSysConsts.MS_MVT_CL_IN_REC[1] + " " +
                    "AND NOT m.b_del " +
                    "AND m.fk_tic_n = " + ticId + " " +
                    "GROUP BY m.fk_tic_n;";
            try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
                if (resultSet.next()) {
                    qty = resultSet.getInt(1);
                }
            }
        }
        catch (Exception e) {
            System.err.println(SMaterialUtils.class.getName() + " " + e);
        }
        return qty;
    }
    
    public static boolean getYearHasStkTransfer(SGuiSession session, int year) {
        boolean stk = false;
        try {
            String sql = "SELECT * FROM m_mvt AS m " +
                    "WHERE fk_iog_ct = " + SModSysConsts.MS_MVT_CL_IN_INV[0] + " " +
                    "AND fk_mvt_cl = " + SModSysConsts.MS_MVT_CL_IN_INV[1] + " " +
                    "AND YEAR(m.dt) = " + year + " " +
                    "AND NOT b_del";
            try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
                if (resultSet.next()) {
                    stk = true;
                }
            }
        }
        catch (Exception e) {
            System.err.println(SMaterialUtils.class.getName() + " " + e);
        }
        return stk;
    }
    
    public static void realizeTransferStock(SGuiSession session, int lastYear, int year) throws Exception {
        if (SLibTimeUtils.digestMonth(session.getSystemDate())[1] <= SModSysConsts.MX_LIMIT_MONTH_TRANS_STK) {
            SDbStockMovement mvt = generateTransferStockMovement(session, lastYear);
            mvt.setSeries(getTicketMovementSerie(session, SModSysConsts.MS_MVT_CL_IN_INV));
            mvt.setDate(SLibTimeUtils.createDate(year, 1, 1));
            mvt.setReference(year + "-01-01 00:00");
            mvt.setSystem(true);
            mvt.setFkWarehouseId(getParamMaterialWarehouseDefault(session));
            mvt.setFkMovementCategoryId(SModSysConsts.MS_MVT_CL_IN_INV[0]);
            mvt.setFkMovementClassId(SModSysConsts.MS_MVT_CL_IN_INV[1]);
            mvt.save(session);            
        }
        else {
            throw new Exception("No se puede realizar el movimiento debido a que la fecha actual esta fuera de los meses permitidos para esta opéración.");
        }
    }
    
    public static SDbStockMovement generateTransferStockMovement(SGuiSession session, int year) throws Exception {
        SDbStockMovement mvt = new SDbStockMovement();
        Statement statement = session.getDatabase().getConnection().createStatement();
        
        String sql = "SELECT id_year, id_wah, id_item, id_unit, fk_mat_cond, fk_tic_n, SUM(qty_in) - SUM(qty_out) AS qty " +
                "FROM m_stk " +
                "WHERE id_year = " + year + " AND dt <= '" + year + "-12-31' AND NOT b_del " +
                "GROUP BY id_year, id_wah, id_item, id_unit, fk_mat_cond, fk_tic_n " +
                "HAVING (SUM(qty_in) - SUM(qty_out)) <> 0";
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbStockMovementEntry ety = new SDbStockMovementEntry();
                ety.setQuantity(resultSet.getDouble("qty"));
                ety.setFkItemId(resultSet.getInt("id_item"));
                ety.setFkUnitId(resultSet.getInt("id_unit"));
                ety.setFkMaterialConditionId(resultSet.getInt("fk_mat_cond"));
                ety.setFkTicketId_n(resultSet.getInt("fk_tic_n"));
                ety.readXtaTicNum(session);
                mvt.getChildEntries().add(ety);
            }
        }
        return mvt;
    }
    
    public static SDbStockMovement getExistingTransferStockMovement(SGuiSession session, int year) throws Exception {
        SDbStockMovement mvtAnt = new SDbStockMovement();
        String sql = "SELECT id_mvt FROM m_mvt " +
                "WHERE fk_iog_ct = " + SModSysConsts.MS_MVT_CL_IN_INV[0] + " " +
                "AND fk_mvt_cl = " + SModSysConsts.MS_MVT_CL_IN_INV[1] + " " +
                "AND YEAR(dt) = " + year + " " +
                "AND NOT b_del";
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                mvtAnt.read(session, new int[] { resultSet.getInt(1) });
            }
        }
        return mvtAnt;
    }

    public static SDbTicketStatus getTicketStatusRegistry(SGuiSession session, int[] pk) throws Exception {
        SDbTicketStatus status = null;
        String sql = "SELECT * FROM m_tic_st WHERE id_tic = " + pk[0];
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            status = new SDbTicketStatus();
            status.read(session, pk);
        }
        return status;
    }
}
