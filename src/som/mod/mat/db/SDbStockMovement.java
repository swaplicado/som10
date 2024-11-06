/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.mat.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import som.gui.SGuiClientUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Isabel Servín
 */
public class SDbStockMovement extends SDbRegistryUser {
    
    protected int mnPkStockMovementId;
    protected String msSeries;
    protected int mnNumber;
    protected Date mtDate;
    protected String msReference;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkWarehouseId;
    protected int mnFkMovementCategoryId;
    protected int mnFkMovementClassId;
    protected int mnFkStockMovementId_n;
    protected int mnFkTicketId_n;
    protected int mnFkShiftId;
    protected int mnFkEmployeeWarehouseManId;
    protected int mnFkEmployeeManufacturingSupId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbNote moDbNote;
    
    protected ArrayList<SDbStockMovementEntry> maChildEntries;
    
    protected SDbStock moAuxDbStockOrig; // Utilizado para guardar los movimientos de conversión de material 
    protected SDbStockMovement moAuxDbMovOut; // Utilizado para guardar los movimientos de salida del almacén 

    public SDbStockMovement() {
        super(SModConsts.M_MVT);
        maChildEntries = new ArrayList<>();
        initRegistry();
    }
    
    public void setPkStockMovementId(int n) { mnPkStockMovementId = n; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setReference(String s) { msReference = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkWarehouseId(int n) { mnFkWarehouseId = n; }
    public void setFkMovementCategoryId(int n) { mnFkMovementCategoryId = n; }
    public void setFkMovementClassId(int n) { mnFkMovementClassId = n; }
    public void setFkStockMovementId_n(int n) { mnFkStockMovementId_n = n; }
    public void setFkTicketId_n(int n) { mnFkTicketId_n = n; }
    public void setFkShiftId(int n) { mnFkShiftId = n; }
    public void setFkEmployeeWarehouseManId(int n) { mnFkEmployeeWarehouseManId = n; }
    public void setFkEmployeeManufacturingSupId(int n) { mnFkEmployeeManufacturingSupId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkStockMovementId() { return mnPkStockMovementId; }
    public String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public String getReference() { return msReference; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkWarehouseId() { return mnFkWarehouseId; }
    public int getFkMovementCategoryId() { return mnFkMovementCategoryId; }
    public int getFkMovementClassId() { return mnFkMovementClassId; }
    public int getFkStockMovementId_n() { return mnFkStockMovementId_n; }
    public int getFkTicketId_n() { return mnFkTicketId_n; }
    public int getFkShiftId() { return mnFkShiftId; }
    public int getFkEmployeeWarehouseManId() { return mnFkEmployeeWarehouseManId; }
    public int getFkEmployeeManufacturingSupId() { return mnFkEmployeeManufacturingSupId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setDbNote(SDbNote o) { moDbNote = o; }
    public void setAuxStockOrig(SDbStock o) { moAuxDbStockOrig = o; }
    public void setAuxMovOut(SDbStockMovement o) { moAuxDbMovOut = o; }
    
    public SDbNote getDbNote() { return moDbNote; }
    public ArrayList<SDbStockMovementEntry> getChildEntries() { return maChildEntries; }
    
    private void deleteRelatedMovs(SGuiSession session) throws Exception {
        ArrayList<SDbStockMovement> arr = new ArrayList<>();
        Statement statement = session.getDatabase().getConnection().createStatement();
        String sql = "SELECT id_mvt FROM m_mvt WHERE fk_mvt_n = " +  mnPkStockMovementId + " AND NOT b_del;";
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbStockMovement mvt = new SDbStockMovement();
                mvt.read(session, new int[] { resultSet.getInt(1) });
                mvt.setDeleted(true);
                arr.add(mvt);
            }
        }
        // Primero validar si todos se pueden eliminar y después proceder 
        for (SDbStockMovement mvt : arr) {
            mvt.canDelete(session);
        }
        for (SDbStockMovement mvt : arr) {
            mvt.delete(session);
        }
    }
    
    private boolean validateMovement(final SGuiSession session) throws SQLException, Exception {
        String msg = "No se puede realizar el movimiento debido a ";
        String faltante = "";
        int yearMov = SLibTimeUtils.digestYear(mtDate)[0];
        int yearAct = SLibTimeUtils.digestYear(session.getSystemDate())[0];
        // Validar que el periodo del movimiento este abierto
        if (SGuiClientUtils.isPeriodOpened(session, mtDate)) {
            Calendar calendar = Calendar.getInstance();
            // Obener el primer día del mes
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            // Restar los meses definidos como periodo de modificación
            Date startPeriod = SLibTimeUtils.addDate(calendar.getTime(), 0, -SModSysConsts.MX_MONTHS_PERIOD, 0);
            // Validar sí la fecha del movimiento esta dentro del periodo permitido
            if (!mtDate.before(startPeriod) && !mtDate.after(SMaterialUtils.getSuggestMovementDate(session))) {
                // Validar si es inventario inicial
                if (mnFkMovementCategoryId == SModSysConsts.MS_MVT_CL_IN_INV[0] &&
                        mnFkMovementClassId == SModSysConsts.MS_MVT_CL_IN_INV[1]) {
                    validateTransferStock(session, yearAct, yearMov -1);
                }
                else {
                    // Validar si el movimiento implica una salida de inventario
                    if ((mnFkMovementCategoryId == SModSysConsts.SS_IOG_CT_IN && mbDeleted) || 
                            (mnFkMovementCategoryId == SModSysConsts.SS_IOG_CT_OUT && !mbDeleted)) {
                        boolean hasStkMov = true;
                        // Obtener stk en la fecha del movimiento
                        HashMap<Integer, Integer> stk = SMaterialUtils.getStockByTicket(session, mnFkTicketId_n, mtDate, SLibConsts.UNDEFINED);
                        // Validar si hay stk suficiente en la fecha del movimiento
                        for (SDbStockMovementEntry ety : maChildEntries) {
                            if (stk.get(ety.getFkMaterialConditionId()) == null || 
                                    stk.get(ety.getFkMaterialConditionId()) < ety.mdQuantity) {
                                faltante += "Cantidad a mover: " + ety.mdQuantity + ", cantidad en inventario: " +
                                        (stk.get(ety.getFkMaterialConditionId()) == null ? "0" :
                                        stk.get(ety.getFkMaterialConditionId())) + ", estado: " + ety.getXtaMatCond() + ", " +
                                        "boleto: " + ety.getXtaTicketNum() + ".\n";
                                hasStkMov = false;
                            }
                        }
                        if (hasStkMov) {
                            boolean hasStkMovEFY = true;
                            // Obtener stk en al final del año de la fecha del movimiento
                            HashMap<Integer, Integer> stkMovEFY = SMaterialUtils.getStockByTicket(session, mnFkTicketId_n, SLibTimeUtils.createDate(yearMov, 12, 31), SLibConsts.UNDEFINED);
                            // Validar si hay stk suficiente en la fecha del movimiento
                            for (SDbStockMovementEntry ety : maChildEntries) {
                                if (stkMovEFY.get(ety.getFkMaterialConditionId()) == null || 
                                        stkMovEFY.get(ety.getFkMaterialConditionId()) < ety.mdQuantity) {
                                    faltante += "Cantidad a mover: " + ety.mdQuantity + ", cantidad en inventario: " +
                                            (stkMovEFY.get(ety.getFkMaterialConditionId()) == null ? "0" :
                                            stkMovEFY.get(ety.getFkMaterialConditionId())) + ", estado: " + ety.getXtaMatCond() + ", " +
                                            "boleto: " + ety.getXtaTicketNum() + ".\n";
                                    hasStkMovEFY = false;
                                }
                            }
                            if (hasStkMovEFY) {
                                // Validar si el movimiento corresponde al año actual
                                if (yearMov != yearAct) {
                                    // Validar si hay traspaso para el año actual
                                    if (SMaterialUtils.getYearHasStkTransfer(session, yearAct)) {
                                        boolean hasStkMovAct = true;
                                        // Obtener stk del año actual
                                        HashMap<Integer, Integer> stkAct = SMaterialUtils.getStockByTicket(session, mnFkTicketId_n, session.getSystemDate(), SLibConsts.UNDEFINED);
                                        // Validar si se trata de un traspaso de inventarios
                                        for (SDbStockMovementEntry ety : maChildEntries) {
                                            if (stkAct.get(ety.getFkMaterialConditionId()) == null || 
                                                    stkAct.get(ety.getFkMaterialConditionId()) < ety.mdQuantity) {
                                                faltante += "Cantidad a mover: " + ety.mdQuantity + ", cantidad en inventario: " +
                                                        (stkAct.get(ety.getFkMaterialConditionId()) == null ? "0" :
                                                        stkAct.get(ety.getFkMaterialConditionId())) + ", estado: " + ety.getXtaMatCond() + ", " +
                                                        "boleto: " + ety.getXtaTicketNum() + ".\n";
                                                hasStkMovAct = false;
                                            }
                                        }
                                        if (!hasStkMovAct) {
                                            throw new Exception(msg + "que no hay inventario suficiente en el año actual (" + yearAct + ")." + "\n"
                                                    + faltante); 
                                        }
                                    }
                                }
                            }
                            else {
                                throw new Exception(msg + "que no hay inventario suficiente en el año del movimiento (" + yearMov + ")." + "\n"
                                    + faltante); 
                            }
                        }
                        else {
                            throw new Exception(msg + "que no hay inventario suficiente a la fecha de corte del movimiento (" + SLibUtils.DateFormatDate.format(mtDate) + ")." + "\n"
                                    + faltante); 
                        }

                    }
                }
            }
            else {
                throw new Exception(msg + "que la fecha del movimiento no esta dentro del periodo permitido."); 
            }
        }
        else {
            throw new Exception(msg + "que el periodo " + SLibUtils.DateFormatDateYearMonth.format(mtDate) + " esta cerrado.");
        }
        return true;
    }
    
    private void validateTransferStock(SGuiSession session, int yearAct, int yearAnt) throws Exception {
        // Validar si hay existencia en el año acual
        if (SMaterialUtils.getYearHasStkTransfer(session, yearAct)) {
            // Validar si se trata de un registro que será eliminado
            if (!isRegistryNew() && mbDeleted) {
                // Generar el nuevo inventario inicial para comparar 
                SDbStockMovement movNew = SMaterialUtils.generateTransferStockMovement(session, yearAnt);
                // Validación de inventarios
                for (SDbStockMovementEntry etyOld : maChildEntries) { // entries de la entrada inicial que será eliminada
                    // Obtener el stock del año actual
                    HashMap<Integer, Integer> stkAct = SMaterialUtils.getStockByTicket(session, etyOld.getFkTicketId_n(), SLibTimeUtils.createDate(yearAct, 12, 31), etyOld.getFkMaterialConditionId());
                    // Recorrer los movimientos del nuevo inventario inicial
                    for (SDbStockMovementEntry etyNew : movNew.getChildEntries()) {
                        // Comparar sí el movimiento a eliminar corresponde con un nuevo movimiento igualando el boleto, ítem y estado de la materia
                        if (etyOld.getFkTicketId_n() == etyNew.getFkTicketId_n() && 
                                etyOld.getFkMaterialConditionId() == etyNew.getFkMaterialConditionId() &&
                                etyOld.getFkItemId() == etyNew.getFkItemId()) {
                            // Cantidad nueva = stk actual - cantidad a eliminar + cantidad a guardar
                            double qtyNew = (stkAct.get(etyOld.getFkMaterialConditionId()) == null ? 0 : stkAct.get(etyOld.getFkMaterialConditionId())) 
                                    - etyOld.mdQuantity
                                    + etyNew.mdQuantity;
                            if (qtyNew < 0) {
                                String faltante = "Inventario despues del ajuste: " + qtyNew + ", estado: " + etyOld.getXtaMatCond() + ", " +
                                        "boleto: " + etyOld.getXtaTicketNum() + ".\n";
                                throw new Exception("No se puede realizar la entrada de inventario inicial debido a que no hay inventario suficiente en el año actual.\n" + 
                                        faltante);
                            }
                            break;
                        }
                    }
                }
            }
            // Si ya hay un inventario incial para el año, eliminarlo 
            else {
                SDbStockMovement oldTransfer = SMaterialUtils.getExistingTransferStockMovement(session, yearAct);
                oldTransfer.setDeleted(true);
                if (oldTransfer.canDelete(session)) {
                    oldTransfer.delete(session);
                }
            }                
        }
    }
    
    private void createAndProcessMovementRelated(SGuiSession session) throws Exception {
        if (moAuxDbStockOrig != null) {
            SDbStockMovement out = new SDbStockMovement();
            out.setSeries(SMaterialUtils.getTicketMovementSerie(session, SModSysConsts.MS_MVT_CL_OUT_CNV));
            out.setDate(mtDate);
            out.setReference(msReference);
            out.setSystem(true);
            out.setFkWarehouseId(mnFkWarehouseId);
            out.setFkMovementCategoryId(SModSysConsts.MS_MVT_CL_OUT_CNV[0]);
            out.setFkMovementClassId(SModSysConsts.MS_MVT_CL_OUT_CNV[1]);
            out.setFkStockMovementId_n(mnPkStockMovementId);
            out.setFkTicketId_n(mnFkTicketId_n);
            out.setFkShiftId(mnFkShiftId);
            out.setFkEmployeeWarehouseManId(mnFkEmployeeWarehouseManId);
            out.setFkEmployeeManufacturingSupId(mnFkEmployeeManufacturingSupId);

            double qtyOut = 0;
            qtyOut = maChildEntries.stream().map((ety) -> ety.getQuantity()).reduce(qtyOut, (accumulator, _item) -> accumulator + _item);
            SDbStockMovementEntry outEty = new SDbStockMovementEntry();
            outEty.setQuantity(qtyOut);
            outEty.setFkItemId(moAuxDbStockOrig.getPkItemId());
            outEty.setFkUnitId(moAuxDbStockOrig.getPkUnitId());
            outEty.setFkMaterialConditionId(moAuxDbStockOrig.getFkMaterialConditionId());
            outEty.setFkTicketId_n(mnFkTicketId_n);
            outEty.readXtaTicNum(session);
        
            out.getChildEntries().clear();
            out.getChildEntries().add(outEty);
            out.save(session);
        }
        else {
            throw new Exception("No se puede generar el movimiento de salida porque no hay objeto de referencia de origen.\n"
                    + "Favor de contactar a soporte.");
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkStockMovementId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockMovementId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockMovementId = 0;
        msSeries = "";
        mnNumber = 0;
        mtDate = null;
        msReference = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkWarehouseId = 0;
        mnFkMovementCategoryId = 0;
        mnFkMovementClassId = 0;
        mnFkStockMovementId_n = 0;
        mnFkTicketId_n = 0;
        mnFkShiftId = 0;
        mnFkEmployeeWarehouseManId = 0;
        mnFkEmployeeManufacturingSupId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moDbNote = null;
        maChildEntries.clear();
        moAuxDbStockOrig = null;
        moAuxDbMovOut = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mvt = " + mnPkStockMovementId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mvt = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkStockMovementId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_mvt), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockMovementId = resultSet.getInt(1);
        }
    }
    
    public void computeNumber(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnNumber = 0;
        
        msSql = "SELECT COALESCE(MAX(num), 0) + 1 FROM " + getSqlTable() + " WHERE ser = '" + msSeries + "'";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnNumber = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkStockMovementId = resultSet.getInt("id_mvt");
            msSeries = resultSet.getString("ser");
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            msReference = resultSet.getString("ref");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkWarehouseId = resultSet.getInt("fk_wah");
            mnFkMovementCategoryId = resultSet.getInt("fk_iog_ct");
            mnFkMovementClassId = resultSet.getInt("fk_mvt_cl");
            mnFkStockMovementId_n = resultSet.getInt("fk_mvt_n");
            mnFkTicketId_n = resultSet.getInt("fk_tic_n");
            mnFkShiftId = resultSet.getInt("fk_shift");
            mnFkEmployeeWarehouseManId = resultSet.getInt("fk_emp_wah_man");
            mnFkEmployeeManufacturingSupId = resultSet.getInt("fk_emp_mfg_sup");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            //  Read aswell child registries:
            
            statement = session.getStatement().getConnection().createStatement();
            
            msSql = "SELECT id_ety FROM " + SModConsts.TablesMap.get(SModConsts.M_MVT_ETY) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbStockMovementEntry entry = new SDbStockMovementEntry();
                entry.read(session, new int[] { mnPkStockMovementId, resultSet.getInt(1) });
                maChildEntries.add(entry);
            }
            
            // Read notes
            
            msSql = "SELECT id_note FROM " + SModConsts.TablesMap.get(SModConsts.M_NOTE) + " WHERE fk_mvt_n = " + mnPkStockMovementId;
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                moDbNote = new SDbNote();
                moDbNote.read(session, new int[] { resultSet.getInt(1) });
            }
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (canSave(session)) {
            if (mbRegistryNew) {
                computePrimaryKey(session);
                computeNumber(session);
                mbUpdatable = true;
                mbDisableable = false;
                mbDeletable = true;
                mnFkWarehouseId = SMaterialUtils.getParamMaterialWarehouseDefault(session);
                mnFkUserInsertId = session.getUser().getPkUserId();
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

                msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                        mnPkStockMovementId + ", " + 
                        "'" + msSeries + "', " + 
                        mnNumber + ", " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                        "'" + msReference + "', " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        (mbSystem ? 1 : 0) + ", " + 
                        mnFkWarehouseId + ", " + 
                        mnFkMovementCategoryId + ", " + 
                        mnFkMovementClassId + ", " + 
                        (mnFkStockMovementId_n == 0 ? "NULL, " : mnFkStockMovementId_n + ", ") + 
                        (mnFkTicketId_n == 0 ? "NULL, " : mnFkTicketId_n + ", ") + 
                        mnFkShiftId + ", " + 
                        mnFkEmployeeWarehouseManId + ", " + 
                        mnFkEmployeeManufacturingSupId + ", " + 
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " + 
                        ")";
            }
            else {
                mnFkUserUpdateId = session.getUser().getPkUserId();

                msSql = "UPDATE " + getSqlTable() + " SET " + 
                        //"id_mvt = " + mnPkStockMovementId + ", " +
                        //"ser = '" + msSeries + "', " +
                        "num = " + mnNumber + ", " +
                        "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                        "ref = '" + msReference + "', " +
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                        //"b_sys = " + (mbSystem ? 1 : 0) + ", " +
                        //"fk_wah = " + mnFkWarehouseId + ", " +
                        //"fk_iog_ct = " + mnFkMovementCategoryId + ", " +
                        //"fk_mvt_cl = " + mnFkMovementClassId + ", " +
                        "fk_mvt_n = " + (mnFkStockMovementId_n == 0 ? "NULL, " : mnFkStockMovementId_n + ", ") +
                        "fk_tic_n = " + (mnFkTicketId_n == 0 ? "NULL, " : mnFkTicketId_n + ", ") +
                        "fk_shift = " + mnFkShiftId + ", " +
                        "fk_emp_wah_man = " + mnFkEmployeeWarehouseManId + ", " +
                        "fk_emp_mfg_sup = " + mnFkEmployeeManufacturingSupId + ", " +
                        //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                        "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                        //"ts_usr_ins = " + "NOW()" + ", " +
                        "ts_usr_upd = " + "NOW()" + " " +
                        getSqlWhere();
            }

            session.getStatement().execute(msSql);

            msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.M_MVT_ETY) + " " + getSqlWhere();
            session.getStatement().execute(msSql);

            for (SDbStockMovementEntry entry : maChildEntries) {
                entry.setPkStockMovenemtId(mnPkStockMovementId);
                entry.setAuxStockMovement(this);
                entry.setRegistryNew(true);
                entry.save(session);
            }

            if (moDbNote != null) {
                moDbNote.setFkStockMovementId_n(mnPkStockMovementId);
                moDbNote.setRegistryNew(true);
                moDbNote.save(session);
            }

            if ((mnFkMovementCategoryId == SModSysConsts.MS_MVT_CL_IN_CNV[0] && mnFkMovementClassId == SModSysConsts.MS_MVT_CL_IN_CNV[1])) {
                createAndProcessMovementRelated(session);
            }
            
            if (moAuxDbMovOut != null) {
                moAuxDbMovOut.setFkStockMovementId_n(mnPkStockMovementId);
                moAuxDbMovOut.save(session);
            }
            
            mbRegistryNew = false;
            mnQueryResultId = SDbConsts.SAVE_OK;
        }
    }
    
    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        return validateMovement(session);
    }
    
    @Override
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        return validateMovement(session);
    }
    
    @Override
    public SDbStockMovement clone() throws CloneNotSupportedException {
        SDbStockMovement registry = new SDbStockMovement();
        
        registry.setPkStockMovementId(this.getPkStockMovementId());
        registry.setSeries(this.getSeries());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setReference(this.getReference());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkWarehouseId(this.getFkWarehouseId());
        registry.setFkMovementCategoryId(this.getFkMovementCategoryId());
        registry.setFkMovementClassId(this.getFkMovementClassId());
        registry.setFkStockMovementId_n(this.getFkStockMovementId_n());
        registry.setFkTicketId_n(this.getFkTicketId_n());
        registry.setFkShiftId(this.getFkShiftId());
        registry.setFkEmployeeWarehouseManId(this.getFkEmployeeWarehouseManId());
        registry.setFkEmployeeManufacturingSupId(this.getFkEmployeeManufacturingSupId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbStockMovementEntry entry : maChildEntries) {
            registry.getChildEntries().add(entry.clone());
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        deleteRelatedMovs(session);
        
        mnFkUserUpdateId = session.getUser().getPkUserId();

        mbDeleted = true;
        
        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();

        session.getStatement().execute(msSql);
        
        for (SDbStockMovementEntry ety: maChildEntries) {
            ety.delete(session);
        }
        
        if (moDbNote != null) {
            moDbNote.delete(session);
        }
        
        if (mnFkStockMovementId_n != 0) {
            SDbStockMovement mvt = new SDbStockMovement();
            mvt.read(session, new int[] { mnFkStockMovementId_n });
            if (!mvt.isDeleted()) {
                mvt.setDeleted(true);
                if (mvt.canDelete(session)) {
                    mvt.delete(session);
                }
            }
        }
        
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
