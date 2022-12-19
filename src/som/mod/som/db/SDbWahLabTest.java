package som.mod.som.db;


import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.cfg.db.SDbBranchWarehouse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Isabel Servín
 */
public class SDbWahLabTest extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkWarehouseLaboratoryId;
    protected int mnPkTestId;
    protected Date mtDate;
    protected Double mdAcidityPercentage_n;
    protected Double mdPeroxideIndex_n;
    protected Double mdMoisturePercentage_n;
    protected Double mdSolidPersentage_n;
    protected Double mdLinoleicAcidPercentage_n;
    protected Double mdOleicAcidPercentage_n;
    protected Double mdLinolenicAcidPercentage_n;
    protected Double mdStearicAcidPercentage_n;
    protected Double mdPalmiticAcidPercentage_n;
    protected String msNote;
    protected boolean mbAcidityPercentageOverange;
    protected boolean mbPeroxideIndexOverange;
    protected boolean mbMoisturePercentageOverange;
    protected boolean mbSolidPersentageOverange;
    protected boolean mbLinoleicAcidPercentageOverange;
    protected boolean mbOleicAcidPercentageOverange;
    protected boolean mbLinolenicAcidPercentageOverange;
    protected boolean mbStearicAcidPercentageOverange;
    protected boolean mbPalmiticAcidPercentageOverange;
    protected int mnFkWarehouseCompanyId;
    protected int mnFkWarehouseBranchId;
    protected int mnFkWarehouseWarehouseId;
    protected int mnFkItem;
    
    protected SDbBranchWarehouse moDbmsBranchWarehouse;
    protected SDbItem moDbmsItem;
    
    protected SDbWahLabTest moLastWahLabTest;
    
    protected boolean mbCapturedAndSaveRow;
    
    protected double mdStk;
    protected String msUnit;

    public SDbWahLabTest() {
        super(SModConsts.S_WAH_LAB_TEST);
        initRegistry();
    }
    
    public void setPkWarehouseLaboratoryId(int n) { mnPkWarehouseLaboratoryId = n; }
    public void setPkTestId(int n) { mnPkTestId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setAcidityPercentage_n(Double d) { mdAcidityPercentage_n = d; }
    public void setPeroxideIndex_n(Double d) { mdPeroxideIndex_n = d; }
    public void setMoisturePercentage_n(Double d) { mdMoisturePercentage_n = d; }
    public void setSolidPersentage_n(Double d) { mdSolidPersentage_n = d; }
    public void setLinoleicAcidPercentage_n(Double d) { mdLinoleicAcidPercentage_n = d; }
    public void setOleicAcidPercentage_n(Double d) { mdOleicAcidPercentage_n = d; }
    public void setLinolenicAcidPercentage_n(Double d) { mdLinolenicAcidPercentage_n = d; }
    public void setStearicAcidPercentage_n(Double d) { mdStearicAcidPercentage_n = d; }
    public void setPalmiticAcidPercentage_n(Double d) { mdPalmiticAcidPercentage_n = d; }
    public void setNote(String s) { msNote = s; }
    public void setAcidityPercentageOverange(boolean b) { mbAcidityPercentageOverange = b; }
    public void setPeroxideIndexOverange(boolean b) { mbPeroxideIndexOverange = b; }
    public void setMoisturePercentageOverange(boolean b) { mbMoisturePercentageOverange = b; }
    public void setSolidPersentageOverange(boolean b) { mbSolidPersentageOverange = b; }
    public void setLinoleicAcidPercentageOverange(boolean b) { mbLinoleicAcidPercentageOverange = b; }
    public void setOleicAcidPercentageOverange(boolean b) { mbOleicAcidPercentageOverange = b; }
    public void setLinolenicAcidPercentageOverange(boolean b) { mbLinolenicAcidPercentageOverange = b; }
    public void setStearicAcidPercentageOverange(boolean b) { mbStearicAcidPercentageOverange = b; }
    public void setPalmiticAcidPercentageOverange(boolean b) { mbPalmiticAcidPercentageOverange = b; }
    public void setFkWarehouseCompanyId(int n) { mnFkWarehouseCompanyId = n; }
    public void setFkWarehouseBranchId(int n) { mnFkWarehouseBranchId = n; }
    public void setFkWarehouseWarehouseId(int n) { mnFkWarehouseWarehouseId = n; }
    public void setFkItem(int n) { mnFkItem = n; }

    public int getPkWarehouseLaboratoryId() { return mnPkWarehouseLaboratoryId; }
    public int getPkTestId() { return mnPkTestId; }
    public Date getDate() { return mtDate; }
    public Double getAcidityPercentage_n() { return mdAcidityPercentage_n; }
    public Double getPeroxideIndex_n() { return mdPeroxideIndex_n; }
    public Double getMoisturePercentage_n() { return mdMoisturePercentage_n; }
    public Double getSolidPersentage_n() { return mdSolidPersentage_n; }
    public Double getLinoleicAcidPercentage_n() { return mdLinoleicAcidPercentage_n; }
    public Double getOleicAcidPercentage_n() { return mdOleicAcidPercentage_n; }
    public Double getLinolenicAcidPercentage_n() { return mdLinolenicAcidPercentage_n; }
    public Double getStearicAcidPercentage_n() { return mdStearicAcidPercentage_n; }
    public Double getPalmiticAcidPercentage_n() { return mdPalmiticAcidPercentage_n; }
    public String getNote() { return msNote; }
    public boolean isAcidityPercentageOverange() { return mbAcidityPercentageOverange; }
    public boolean isPeroxideIndexOverange() { return mbPeroxideIndexOverange; }
    public boolean isMoisturePercentageOverange() { return mbMoisturePercentageOverange; }
    public boolean isSolidPersentageOverange() { return mbSolidPersentageOverange; }
    public boolean isLinoleicAcidPercentageOverange() { return mbLinoleicAcidPercentageOverange; }
    public boolean isOleicAcidPercentageOverange() { return mbOleicAcidPercentageOverange; }
    public boolean isLinolenicAcidPercentageOverange() { return mbLinolenicAcidPercentageOverange; }
    public boolean isStearicAcidPercentageOverange() { return mbStearicAcidPercentageOverange; }
    public boolean isPalmiticAcidPercentageOverange() { return mbPalmiticAcidPercentageOverange; }
    public int getFkWarehouseCompanyId() { return mnFkWarehouseCompanyId; }
    public int getFkWarehouseBranchId() { return mnFkWarehouseBranchId; }
    public int getFkWarehouseWarehouseId() { return mnFkWarehouseWarehouseId; }
    public int getFkItem() { return mnFkItem; }
    
    public void setDbmsBranchWarehouse(SDbBranchWarehouse o) { moDbmsBranchWarehouse = o; }
    public void setDbmsItem(SDbItem o) { moDbmsItem = o; }
    
    public void setLastWahLabTest(SDbWahLabTest o) { moLastWahLabTest = o; }
    
    public void setCapturedAndSaveRow(boolean b) { mbCapturedAndSaveRow = b; }
    
    public void setStk(double d) { mdStk = d; }
    public void setUnit(String s) { msUnit = s; }
    
    public SDbBranchWarehouse getDbmsBranchWarehouse() { return moDbmsBranchWarehouse; }
    public SDbItem getDbmsItem() { return moDbmsItem; }
    
    public SDbWahLabTest getLastWahLabTest() { return moLastWahLabTest; }
    
    public boolean getCapturedAndSaveRow() { return mbCapturedAndSaveRow; }
    
    public double getStk() { return mdStk; }
    public String getUnit() { return msUnit; }
    
    public boolean readSimilarItemLastWahLabTest(SGuiSession session, int lastPkWahLabTest) throws Exception {
        boolean similar = false;
        moLastWahLabTest = null;
        msSql = "SELECT wlt.id_test FROM " + getSqlTable() + " AS wlt " +
                "INNER JOIN su_item AS i ON wlt.fk_item = i.id_item " +
                "WHERE wlt.id_wah_lab = " + lastPkWahLabTest + " " +
                "AND wlt.fk_wah_co = " + mnFkWarehouseCompanyId + " " +
                "AND wlt.fk_wah_cob = " + mnFkWarehouseBranchId + " " +
                "AND wlt.fk_wah_wah = " + mnFkWarehouseWarehouseId + " " +
                "AND i.fk_oil_cl_n = " + moDbmsItem.getFkOilClassId_n() + " " +
                "AND i.fk_oil_tp_n = " + moDbmsItem.getFkOilTypeId_n() + " " +
                "AND i.fk_item_rm_n = " + moDbmsItem.getFkItemRowMaterialId_n() + ";";
        ResultSet resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            moLastWahLabTest = new SDbWahLabTest();
            moLastWahLabTest.read(session, new int[] { lastPkWahLabTest, resultSet.getInt(1) });
            similar = true;
        }
        return similar;
    }
    
    public void readWarehouse(SGuiSession session) throws Exception {
        moDbmsBranchWarehouse = new SDbBranchWarehouse();
        moDbmsBranchWarehouse.read(session, new int[] { mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId });
    }
    
    public void readItem(SGuiSession session) throws Exception {
        moDbmsItem = new SDbItem();
        moDbmsItem.read(session, new int[] { mnFkItem });
    }
    
    public void readStk(SGuiSession session) throws Exception {
        String sql = "SELECT " +
                    "SUM(v.mov_in - v.mov_out), " +
                    "vu.code " +
                    "FROM s_stk AS v " +
                    "INNER JOIN su_item AS vi ON v.id_item = vi.id_item " +
                    "INNER JOIN su_unit AS vu ON v.id_unit = vu.id_unit " +
                    "INNER JOIN cu_wah AS vw ON v.id_co = vw.id_co AND v.id_cob = vw.id_cob AND v.id_wah = vw.id_wah " +
                    "INNER JOIN cu_cob AS vc ON v.id_co = vc.id_co AND v.id_cob = vc.id_cob " +
                    "WHERE v.b_del = 0 AND v.id_year = YEAR(" + (mtDate != null ? "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "'" : "CURDATE()") + ") " +
                    "AND v.dt <= " + (mtDate != null ? "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "'" : "CURDATE()") + " " +
                    "AND v.id_co = " + mnFkWarehouseCompanyId + " " + 
                    "AND v.id_cob = " + mnFkWarehouseBranchId + " " + 
                    "AND v.id_wah = " + mnFkWarehouseWarehouseId + " " + 
                    "AND v.id_item = " + mnFkItem + " " +
                    "GROUP BY v.id_item, v.id_unit, v.id_cob, v.id_wah, vc.code, vw.name, vi.code, vi.name, vu.code " +
                    "HAVING SUM(v.mov_in - v.mov_out) <> 0 " +
                    "ORDER BY vw.code, vi.name;" ;
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            mdStk = resultSet.getDouble(1);
            msUnit = resultSet.getString(2);
        } 
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkWarehouseLaboratoryId = pk[0];
        mnPkTestId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkWarehouseLaboratoryId, mnPkTestId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkWarehouseLaboratoryId = 0;
        mnPkTestId = 0;
        mtDate = null;
        mdAcidityPercentage_n = null;
        mdPeroxideIndex_n = null;
        mdMoisturePercentage_n = null;
        mdSolidPersentage_n = null;
        mdLinoleicAcidPercentage_n = null;
        mdOleicAcidPercentage_n = null;
        mdLinolenicAcidPercentage_n = null;
        mdStearicAcidPercentage_n = null;
        mdPalmiticAcidPercentage_n = null;
        msNote = "";
        mbAcidityPercentageOverange = false;
        mbPeroxideIndexOverange = false;
        mbMoisturePercentageOverange = false;
        mbSolidPersentageOverange = false;
        mbLinoleicAcidPercentageOverange = false;
        mbOleicAcidPercentageOverange = false;
        mbLinolenicAcidPercentageOverange = false;
        mbStearicAcidPercentageOverange = false;
        mbPalmiticAcidPercentageOverange = false;
        mnFkWarehouseCompanyId = 0;
        mnFkWarehouseBranchId = 0;
        mnFkWarehouseWarehouseId = 0;
        mnFkItem = 0;
        
        moDbmsBranchWarehouse = null;
        moDbmsItem = null;
        
        moLastWahLabTest = null;
        
        mbCapturedAndSaveRow = false;
        
        mdStk = 0;
        msUnit = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_wah_lab = " + mnPkWarehouseLaboratoryId + " AND id_test = " + mnPkTestId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_wah_lab = " + pk[0] + " AND id_test = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkTestId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_test), 0) + 1 FROM " + getSqlTable() + 
                " WHERE id_wah_lab = " + mnPkWarehouseLaboratoryId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTestId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkWarehouseLaboratoryId = resultSet.getInt("id_wah_lab");
            mnPkTestId = resultSet.getInt("id_test");
            mtDate = resultSet.getDate("dt");
            mdAcidityPercentage_n = resultSet.getString("aci_per_n") == null ? null : resultSet.getDouble("aci_per_n");
            mdPeroxideIndex_n = resultSet.getString("per_ind_n") == null ? null : resultSet.getDouble("per_ind_n");
            mdMoisturePercentage_n = resultSet.getString("moi_per_n") == null ? null : resultSet.getDouble("moi_per_n");
            mdSolidPersentage_n = resultSet.getString("sol_per_n") == null ? null : resultSet.getDouble("sol_per_n");
            mdLinoleicAcidPercentage_n = resultSet.getString("lin_per_n") == null ? null : resultSet.getDouble("lin_per_n");
            mdOleicAcidPercentage_n = resultSet.getString("ole_per_n") == null ? null : resultSet.getDouble("ole_per_n");
            mdLinolenicAcidPercentage_n = resultSet.getString("llc_per_n") == null ? null : resultSet.getDouble("llc_per_n");
            mdStearicAcidPercentage_n = resultSet.getString("ste_per_n") == null ? null : resultSet.getDouble("ste_per_n");
            mdPalmiticAcidPercentage_n = resultSet.getString("pal_per_n") == null ? null : resultSet.getDouble("pal_per_n");
            msNote = resultSet.getString("note");
            mbAcidityPercentageOverange = resultSet.getBoolean("b_aci_per_overange");
            mbPeroxideIndexOverange = resultSet.getBoolean("b_per_ind_overange");
            mbMoisturePercentageOverange = resultSet.getBoolean("b_moi_per_overange");
            mbSolidPersentageOverange = resultSet.getBoolean("b_sol_per_overange");
            mbLinoleicAcidPercentageOverange = resultSet.getBoolean("b_lin_per_overange");
            mbOleicAcidPercentageOverange = resultSet.getBoolean("b_ole_per_overange");
            mbLinolenicAcidPercentageOverange = resultSet.getBoolean("b_llc_per_overange");
            mbStearicAcidPercentageOverange = resultSet.getBoolean("b_ste_per_overange");
            mbPalmiticAcidPercentageOverange = resultSet.getBoolean("b_pal_per_overange");
            mnFkWarehouseCompanyId = resultSet.getInt("fk_wah_co");
            mnFkWarehouseBranchId = resultSet.getInt("fk_wah_cob");
            mnFkWarehouseWarehouseId = resultSet.getInt("fk_wah_wah");
            mnFkItem = resultSet.getInt("fk_item");
            
            readWarehouse(session);
            readItem(session);
            readStk(session);

            mbCapturedAndSaveRow = true;
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbUpdatable = true;
            mbDisableable = false;
            mbDeletable = true;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkWarehouseLaboratoryId + ", " + 
                    mnPkTestId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    mdAcidityPercentage_n + ", " + 
                    mdPeroxideIndex_n + ", " + 
                    mdMoisturePercentage_n + ", " + 
                    mdSolidPersentage_n + ", " + 
                    mdLinoleicAcidPercentage_n + ", " + 
                    mdOleicAcidPercentage_n + ", " + 
                    mdLinolenicAcidPercentage_n + ", " + 
                    mdStearicAcidPercentage_n + ", " + 
                    mdPalmiticAcidPercentage_n + ", " + 
                    "'" + msNote + "', " + 
                    (mbAcidityPercentageOverange ? 1 : 0) + ", " + 
                    (mbPeroxideIndexOverange ? 1 : 0) + ", " + 
                    (mbMoisturePercentageOverange ? 1 : 0) + ", " + 
                    (mbSolidPersentageOverange ? 1 : 0) + ", " + 
                    (mbLinoleicAcidPercentageOverange ? 1 : 0) + ", " + 
                    (mbOleicAcidPercentageOverange ? 1 : 0) + ", " + 
                    (mbLinolenicAcidPercentageOverange ? 1 : 0) + ", " + 
                    (mbStearicAcidPercentageOverange ? 1 : 0) + ", " + 
                    (mbPalmiticAcidPercentageOverange ? 1 : 0) + ", " + 
                    mnFkWarehouseCompanyId + ", " + 
                    mnFkWarehouseBranchId + ", " + 
                    mnFkWarehouseWarehouseId + ", " + 
                    mnFkItem + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_wah_lab = " + mnPkWarehouseLaboratoryId + ", " +
                    //"id_test = " + mnPkTestId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "aci_per_n = " + mdAcidityPercentage_n + ", " +
                    "per_ind_n = " + mdPeroxideIndex_n + ", " +
                    "moi_per_n = " + mdMoisturePercentage_n + ", " +
                    "sol_per_n = " + mdSolidPersentage_n + ", " +
                    "lin_per_n = " + mdLinoleicAcidPercentage_n + ", " +
                    "ole_per_n = " + mdOleicAcidPercentage_n + ", " +
                    "llc_per_n = " + mdLinolenicAcidPercentage_n + ", " +
                    "ste_per_n = " + mdStearicAcidPercentage_n + ", " +
                    "pal_per_n = " + mdPalmiticAcidPercentage_n + ", " +
                    "note = '" + msNote + "', " +
                    "b_aci_per_overange = " + (mbAcidityPercentageOverange ? 1 : 0) + ", " +
                    "b_per_ind_overange = " + (mbPeroxideIndexOverange ? 1 : 0) + ", " +
                    "b_moi_per_overange = " + (mbMoisturePercentageOverange ? 1 : 0) + ", " +
                    "b_sol_per_overange = " + (mbSolidPersentageOverange ? 1 : 0) + ", " +
                    "b_lin_per_overange = " + (mbLinoleicAcidPercentageOverange ? 1 : 0) + ", " +
                    "b_ole_per_overange = " + (mbOleicAcidPercentageOverange ? 1 : 0) + ", " +
                    "b_llc_per_overange = " + (mbLinolenicAcidPercentageOverange ? 1 : 0) + ", " +
                    "b_ste_per_overange = " + (mbStearicAcidPercentageOverange ? 1 : 0) + ", " +
                    "b_pal_per_overange = " + (mbPalmiticAcidPercentageOverange ? 1 : 0) + ", " +
                    "fk_wah_co = " + mnFkWarehouseCompanyId + ", " +
                    "fk_wah_cob = " + mnFkWarehouseBranchId + ", " +
                    "fk_wah_wah = " + mnFkWarehouseWarehouseId + ", " +
                    "fk_item = " + mnFkItem + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbWahLabTest clone() throws CloneNotSupportedException {
        SDbWahLabTest registry = new SDbWahLabTest();
        
        registry.setPkWarehouseLaboratoryId(this.getPkWarehouseLaboratoryId());
        registry.setPkTestId(this.getPkTestId());
        registry.setDate(this.getDate());
        registry.setAcidityPercentage_n(this.getAcidityPercentage_n());
        registry.setPeroxideIndex_n(this.getPeroxideIndex_n());
        registry.setMoisturePercentage_n(this.getMoisturePercentage_n());
        registry.setSolidPersentage_n(this.getSolidPersentage_n());
        registry.setLinoleicAcidPercentage_n(this.getLinoleicAcidPercentage_n());
        registry.setOleicAcidPercentage_n(this.getOleicAcidPercentage_n());
        registry.setLinolenicAcidPercentage_n(this.getLinolenicAcidPercentage_n());
        registry.setStearicAcidPercentage_n(this.getStearicAcidPercentage_n());
        registry.setPalmiticAcidPercentage_n(this.getPalmiticAcidPercentage_n());
        registry.setNote(this.getNote());
        registry.setAcidityPercentageOverange(this.isAcidityPercentageOverange());
        registry.setPeroxideIndexOverange(this.isPeroxideIndexOverange());
        registry.setMoisturePercentageOverange(this.isMoisturePercentageOverange());
        registry.setSolidPersentageOverange(this.isSolidPersentageOverange());
        registry.setLinoleicAcidPercentageOverange(this.isLinoleicAcidPercentageOverange());
        registry.setOleicAcidPercentageOverange(this.isOleicAcidPercentageOverange());
        registry.setLinolenicAcidPercentageOverange(this.isLinolenicAcidPercentageOverange());
        registry.setStearicAcidPercentageOverange(this.isStearicAcidPercentageOverange());
        registry.setPalmiticAcidPercentageOverange(this.isPalmiticAcidPercentageOverange());
        registry.setFkWarehouseCompanyId(this.getFkWarehouseCompanyId());
        registry.setFkWarehouseBranchId(this.getFkWarehouseBranchId());
        registry.setFkWarehouseWarehouseId(this.getFkWarehouseWarehouseId());
        registry.setFkItem(this.getFkItem());
        
        registry.setDbmsBranchWarehouse(this.getDbmsBranchWarehouse());
        registry.setDbmsItem(this.getDbmsItem());
        
        registry.setLastWahLabTest(this.getLastWahLabTest());

        registry.setRegistryNew(this.isRegistryNew());
        
        registry.setCapturedAndSaveRow(false);
        
        return registry;
    }
    
    public void setRowPrimaryKey(int[] pk) {
        mnFkWarehouseCompanyId = pk[0];
        mnFkWarehouseBranchId = pk[1];
        mnFkWarehouseWarehouseId = pk[2];
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnFkWarehouseCompanyId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId };
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRowEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRowEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch (row) {
            case 0:
                value = moDbmsBranchWarehouse.getCode();
                break;
            case 1:
                value = moDbmsBranchWarehouse.getName();
                break;
            case 2: 
                value = moDbmsItem.getName();
                break;
            case 3:
                value = mdStk;
                break;
            case 4:
                value = msUnit;
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object o, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
