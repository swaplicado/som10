/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.cfg.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 * Value of a field.
 * @author Sergio Flores
 */
public class SDbValue extends SDbRegistryUser implements SGridRow {

    protected int mnPkValueId;
    protected String msValueText;
    protected String msValueReference;
    protected String msScopeInputCategories;
    protected String msScopeItems;
    //protected boolean mbDeleted;
    protected int mnFkFieldId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected HashSet<Integer> moCompScopeInputCategoriesSet;
    protected HashSet<Integer> moCompScopeItemsSet;
    
    protected String msAuxScopeInputCategoriesNames;
    protected String msAuxScopeItemsNames;
    
    public SDbValue() {
        super(SModConsts.C_VALUE);
        initRegistry();
    }

    /*
     * Overriden methods
     */
    
    private void addListIdsToSet(final String commaSeparatedListIds, final HashSet<Integer> set) {
        set.clear();
        
        if (!commaSeparatedListIds.isEmpty()) {
            set.addAll(Arrays.stream(commaSeparatedListIds.split(","))
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .collect(Collectors.toCollection(HashSet::new)));
        }
    }
    
    /*
     * Public methods
     */
    
    public void setPkValueId(int n) { mnPkValueId = n; }
    public void setValueText(String s) { msValueText = s; }
    public void setValueReference(String s) { msValueReference = s; }
    public void setScopeInputCategories(String s) { msScopeInputCategories = sanitizeDataIds(s); addListIdsToSet(msScopeInputCategories, moCompScopeInputCategoriesSet); }
    public void setScopeItems(String s) { msScopeItems = sanitizeDataIds(s); addListIdsToSet(msScopeItems, moCompScopeItemsSet); }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkFieldId(int n) { mnFkFieldId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkValueId() { return mnPkValueId; }
    public String getValueText() { return msValueText; }
    public String getValueReference() { return msValueReference; }
    public String getScopeInputCategories() { return msScopeInputCategories; }
    public String getScopeItems() { return msScopeItems; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkFieldId() { return mnFkFieldId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public HashSet<Integer> getCompScopeInputCategoriesSet() {
        return moCompScopeInputCategoriesSet;
    }
    
    public HashSet<Integer> getCompScopeItemsSet() {
        return moCompScopeItemsSet;
    }
    
    public void setAuxScopeInputCategoriesNames(String s) { msAuxScopeInputCategoriesNames = s; }
    public void setAuxScopeItemsNames(String s) { msAuxScopeItemsNames = s; }
    
    public String getAuxScopeInputCategoriesNames() { return msAuxScopeInputCategoriesNames; }
    public String getAuxScopeItemsNames() { return msAuxScopeItemsNames; }
    
    public void addScopeInputCategoryId(final int id) {
        if (msScopeInputCategories.isEmpty()) {
            msScopeInputCategories = "" + id;
            moCompScopeInputCategoriesSet.add(id);
        }
        else {
            setScopeInputCategories(msScopeInputCategories + "," + id);
        }
    }
    
    public void addScopeItem(final int id) {
        if (msScopeItems.isEmpty()) {
            msScopeItems = "" + id;
            moCompScopeItemsSet.add(id);
        }
        else {
            setScopeItems(msScopeItems + "," + id);
        }
    }
    
    public SDbValueValue checkRelatedValue(final SGuiClient client, final SDbValue related) throws Exception {
        SDbValueValue valueValue = null;
        
        String sql = "SELECT vv.id_value_a, vv.id_value_b "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.C_VALUE_VALUE) + " AS vv "
                + "WHERE vv.id_value_a = " + mnPkValueId + " AND vv.id_value_b = " + related.getPkValueId() + ";";
        
        try (Statement statement = client.getSession().getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            if (resultSet.next()) {
                valueValue = (SDbValueValue) client.getSession().readRegistry(SModConsts.C_VALUE_VALUE, new int[] { mnPkValueId, related.getPkValueId() });
                
                if (valueValue.isDeleted()) {
                    String message = "La relación entre el valor '" + msValueText + "' con '" + related.getValueText() + "' está eliminada.";
                    
                    if (client.showMsgBoxConfirm(message + "\n¿Está seguro que desea continuar, y con ello reactivar esta relación?") == JOptionPane.YES_OPTION) {
                        valueValue.setDeleted(false);
                        valueValue.setRegistryEdited(true);
                    }
                    else {
                        throw new Exception(message);
                    }
                }
            }
            else {
                String message = "La relación entre el valor '" + msValueText + "' con '" + related.getValueText() + "' no existe.";
                
                if (client.showMsgBoxConfirm(message + "\n¿Está seguro que desea continuar, y con ello crear esta relación?") == JOptionPane.YES_OPTION) {
                    valueValue = new SDbValueValue();
                    valueValue.setPkValueAId(mnPkValueId);
                    valueValue.setPkValueBId(related.getPkValueId());
                }
                else {
                    throw new Exception(message);
                }
            }
        }
        
        return valueValue;
    }
    
    /*
     * Overriden methods
     */
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkValueId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkValueId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkValueId = 0;
        msValueText = "";
        msValueReference = "";
        msScopeInputCategories = "";
        msScopeItems = "";
        mbDeleted = false;
        mnFkFieldId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        moCompScopeInputCategoriesSet = new HashSet<>();
        moCompScopeItemsSet = new HashSet<>();

        msAuxScopeInputCategoriesNames = "";
        msAuxScopeItemsNames = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_value = " + mnPkValueId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_value = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        mnPkValueId = 0;

        msSql = "SELECT COALESCE(MAX(id_value), 0) + 1 FROM " + getSqlTable();
        
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                mnPkValueId = resultSet.getInt(1);
            }
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnPkValueId = resultSet.getInt("id_value");
                msValueText = resultSet.getString("value_text");
                msValueReference = resultSet.getString("value_ref");
                msScopeInputCategories = resultSet.getString("scope_inp_ct");
                msScopeItems = resultSet.getString("scope_item");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkFieldId = resultSet.getInt("fk_field");
                mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
                mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
                mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
                mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
                
                addListIdsToSet(msScopeInputCategories, moCompScopeInputCategoriesSet);
                addListIdsToSet(msScopeItems, moCompScopeItemsSet);

                mbRegistryNew = false;
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkValueId + ", " + 
                    "'" + msValueText + "', " + 
                    "'" + msValueReference + "', " + 
                    "'" + msScopeInputCategories + "', " + 
                    "'" + msScopeItems + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkFieldId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_value = " + mnPkValueId + ", " +
                    "value_text = '" + msValueText + "', " +
                    "value_ref = '" + msValueReference + "', " +
                    "scope_inp_ct = '" + msScopeInputCategories + "', " +
                    "scope_item = '" + msScopeItems + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_field = " + mnFkFieldId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbValue clone() throws CloneNotSupportedException {
        SDbValue registry = new SDbValue();

        registry.setPkValueId(this.getPkValueId());
        registry.setValueText(this.getValueText());
        registry.setValueReference(this.getValueReference());
        registry.setScopeInputCategories(this.getScopeInputCategories());
        registry.setScopeItems(this.getScopeItems());
        registry.setDeleted(this.isDeleted());
        registry.setFkFieldId(this.getFkFieldId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        return msValueText;
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = msValueText;
                break;
            case 1:
                value = msValueReference;
                break;
            case 2:
                value = msAuxScopeInputCategoriesNames;
                break;
            case 3:
                value = msAuxScopeItemsNames;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*
     * Public static methods
     */
    
    /**
     * Count the value occurrences, to check that there are not value duplicities.
     * @param session GUI session.
     * @param fieldId Type of field of the value to be checked.
     * @param valueText Value to check.
     * @param valueId ID of the original value, to prevent it from being reported. Can be zero.
     * @return Number of occurrences found.
     * @throws Exception 
     */
    public static int countOccurrences(final SGuiSession session, final int fieldId, final String valueText, final int valueId) throws Exception {
        int count = 0;
        
        String sql = ("SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.C_VALUE) + " "
                + "WHERE value_text = '" + valueText + "' AND fk_field = " + fieldId + " "
                + (valueId != 0 ? "AND id_value <> " + valueId + " " : "")).trim() + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        
        return count;
    }
    
    /**
     * Retrieve similar values, to check that there are not value duplicities.
     * @param session GUI session.
     * @param fieldId Type of field of the value to be checked.
     * @param valueText Value to check.
     * @param valueId ID of the original value, to prevent it from being reported. Can be zero.
     * @return List of similar values found.
     * @throws Exception 
     */
    public static ArrayList<SDbValue> retrieveSimilars(final SGuiSession session, final int fieldId, final String valueText, final int valueId) throws Exception {
        ArrayList<SDbValue> similarValues = new ArrayList<>();
        
        String sql = ("SELECT id_value "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.C_VALUE) + " "
                + "WHERE SOUNDEX(value_text) = SOUNDEX('" + valueText + "') AND fk_field = " + fieldId + " "
                + (valueId != 0 ? "AND id_value <> " + valueId + " " : "")).trim() + ";";
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                similarValues.add((SDbValue) session.readRegistry(SModConsts.C_VALUE, new int[] { resultSet.getInt(1) }));
            }
        }
        
        return similarValues;
    }
    
    /**
     * Sanitize comma-separated list of ID's.
     * Empty, non-numeric and duplicated values will be removed.
     * @param commaSeparatedListIds Comma-separated list of ID's.
     * @return <code>String</code> of sanitized comma-separated list of ID's.
     */
    public static String sanitizeDataIds(final String commaSeparatedListIds) {
        String[] ids = commaSeparatedListIds.trim().replaceAll("\\s+", "").split(",");
        HashSet<Integer> idsSet = new HashSet<>();
        
        for (String id : ids) {
            if (!id.isEmpty()) {
                int n = SLibUtils.parseInt(id);
                if (n > 0) {
                    idsSet.add(n);
                }
            }
        }
        
        String sanitizedListOfIds = "";
        ArrayList<Integer> idsArray = new ArrayList<>(idsSet);
        idsArray.sort(null);
        
        for (Integer id : idsArray) {
            sanitizedListOfIds += (!sanitizedListOfIds.isEmpty() ? "," : "") + id;
        }
        
        return sanitizedListOfIds;
    }
    
    /**
     * Validate the scope of ID's of input categories.
     * @param session GUI session.
     * @param dataType Data type: SModConsts.SU_INP_CT and SModConsts.SU_ITEM;
     * @param commaSeparatedListIds Comma-separated list of ID's.
     * @return <code>true</code> if all ID's are valid.
     * @throws java.lang.Exception
     */
    public static boolean validateDataIds(final SGuiSession session, final int dataType, final String commaSeparatedListIds) throws Exception {
        String[] ids = sanitizeDataIds(commaSeparatedListIds).split(",");
        
        if (ids == null || ids.length == 0 || ids[0].isEmpty()) {
            throw new Exception("No hay ningún ID para validar.");
        }
        else {
            int issues = 0;
            String messages = "";
            String registry = "";

            String sql = "SELECT name, b_del FROM " + SModConsts.TablesMap.get(dataType) + " WHERE <id> = ?;";

            switch (dataType) {
                case SModConsts.SU_INP_CT:
                    registry = "categorías de insumo";
                    sql = sql.replace("<id>", "id_inp_ct");
                    break;
                case SModConsts.SU_ITEM:
                    registry = "ítems";
                    sql = sql.replace("<id>", "id_item");
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Data type: " + dataType + ")");
            }

            try (PreparedStatement preparedStatement = session.getStatement().getConnection().prepareStatement(sql)) {
                for (String id : ids) {
                    int n = SLibUtils.parseInt(id);
                    preparedStatement.setInt(1, n);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                    if (resultSet.next()) {
                        if (dataType == SModConsts.SU_INP_CT) {
                            if (n == SModSysConsts.SU_INP_CT_NA) {
                                messages += (!messages.isEmpty() ? "\n" : "") + ++issues + ". El registro con ID " + n + ", '" + resultSet.getString("name") + "', no está permitido.";
                                continue; // stop here and validate the next ID
                            }
                        }
                        
                        if (resultSet.getBoolean("b_del")) {
                            messages += (!messages.isEmpty() ? "\n" : "") + ++issues + ". El registro con ID " + n + ", '" + resultSet.getString("name") + "', está eliminado.";
                        }
                    }
                    else {
                        messages += (!messages.isEmpty() ? "\n" : "") + ++issues + ". El registro con ID " + n + " no existe.";
                    }
                }
            }
            
            if (!messages.isEmpty()) {
                throw new Exception((issues == 1 ? "Se detectó el siguiente problema" : "Se detectaron los siguientes " + issues + " problemas") + " con los ID de '" + registry + "':\n" + messages);
            }
        }
        
        return true; // all ID's are valid!
    }
}
