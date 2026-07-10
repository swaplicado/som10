/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.cfg.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.swap.utils.SJsonUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbItem;

/**
 * Field settings to validate user input.
 * @author Sergio Flores
 */
public class SDbField extends SDbRegistryUser {
    
    public static final int ADD_TO_SCOPE_POLICY_NO = 0;
    public static final int ADD_TO_SCOPE_POLICY_YES = 1;
    public static final int ADD_TO_SCOPE_POLICY_CONFIRM = 2;
    
    public static final HashMap<Integer, String> FieldNames = new HashMap<>();
    
    static {
        FieldNames.put(SModSysConsts.C_FIELD_TIC_PLA, "placas");
        FieldNames.put(SModSysConsts.C_FIELD_TIC_DRV, "chofer");
    }
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    protected int mnPkFieldId;
    protected String msCode;
    protected String msName;
    protected String msDescription;
    /** JSON field settings. */
    protected String msSettings;
    /** Individual characters to be trimmed from user input. */
    protected String msTrimCharacters;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SJsonFieldSettings moAuxJsonFieldSettings;
    protected String msAuxRegexClassValueTrimCharacters;
    
    public SDbField() {
        super(SModConsts.C_FIELD);
        initRegistry();
    }

    public void setPkFieldId(int n) { mnPkFieldId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setSettings(String s) { msSettings = s; }
    public void setTrimCharacters(String s) { msTrimCharacters = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkFieldId() { return mnPkFieldId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getDescription() { return msDescription; }
    public String getSettings() { return msSettings; }
    public String getTrimCharacters() { return msTrimCharacters; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public SJsonFieldSettings getAuxJsonFieldSettings() {
        return moAuxJsonFieldSettings;
    }
    
    public String getAuxRegexClassValueTrimCharacters() {
        return msAuxRegexClassValueTrimCharacters;
    }
    
    /*
     * Overriden methods
     */
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkFieldId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkFieldId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkFieldId = 0;
        msCode = "";
        msName = "";
        msDescription = "";
        msSettings = "";
        msTrimCharacters = "";
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moAuxJsonFieldSettings = null;
        msAuxRegexClassValueTrimCharacters = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_field = " + mnPkFieldId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_field = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        mnPkFieldId = 0;

        msSql = "SELECT COALESCE(MAX(id_field), 0) + 1 FROM " + getSqlTable();
        
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                mnPkFieldId = resultSet.getInt(1);
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
                mnPkFieldId = resultSet.getInt("id_field");
                msCode = resultSet.getString("code");
                msName = resultSet.getString("name");
                msDescription = resultSet.getString("description");
                msSettings = resultSet.getString("settings");
                msTrimCharacters = resultSet.getString("trim_chars");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
                mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
                mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
                mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
                
                // parse JSON settings to validate text values:
                
                try {
                    moAuxJsonFieldSettings = MAPPER.readValue(msSettings, SJsonFieldSettings.class);
                }
                catch (Exception e) {
                    SLibUtils.printException(this, e);
                }
                
                // create regex character class for trimming characters from text values:
                
                if (!msTrimCharacters.isEmpty()) {
                    msAuxRegexClassValueTrimCharacters = "[" + msTrimCharacters.replace("-", "\\-") + "]";
                }

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
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkFieldId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msDescription + "', " + 
                    "'" + msSettings + "', " + 
                    "'" + msTrimCharacters + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_field = " + mnPkFieldId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "description = '" + msDescription + "', " +
                    "settings = '" + msSettings + "', " +
                    "trim_chars = '" + msTrimCharacters + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
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
    public SDbField clone() throws CloneNotSupportedException {
        SDbField registry = new SDbField();

        registry.setPkFieldId(this.getPkFieldId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDescription(this.getDescription());
        registry.setSettings(this.getSettings());
        registry.setTrimCharacters(this.getTrimCharacters());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public String toString() {
        return "Campo: " + msName + " (" + msCode + ")\n"
                + "Descripción: " + msDescription + "\n"
                + "Configuración JSON:\n"
                + SJsonUtils.prettyPrint(msSettings);
    }
    
    /*
     * Public methods
     */
    
    /**
     * Sanitize text value according to field's characters to be trimmed.
     * @param valueText Text value to sanitize.
     * @return 
     */
    public String sanitizeValueText(final String valueText) {
        String textTrimmed = SLibUtils.textTrim(valueText);
        return msAuxRegexClassValueTrimCharacters.isEmpty() ? textTrimmed : textTrimmed.replaceAll(msAuxRegexClassValueTrimCharacters, "");
    }
    
    /**
     * Validate format of text value according to field's JSON settings.
     * @param client GUI client;
     * @param valueText Text value to validate.
     * @return <code>true</code> if value is valid.
     * @throws Exception 
     */
    public boolean validateValueTextFormat(final SGuiClient client, final String valueText) throws Exception {
        String message;
        String confirm = "El valor '" + valueText + "' para el campo '" + msName + "' no comple con esto:\n";
        String prefix = "El valor '" + valueText + "' para el campo '" + msName + "' no es válido:\n";
        String suffix = "\nFavor de corregir lo anterior, e intentarlo de nuevo.";
        
        if (moAuxJsonFieldSettings == null) {
            message = "la configuración JSON del campo no está disponible.";
            throw new Exception(prefix + message); // no need of suffix in this case!
        }
        
        boolean valid = false;
        String[] words = valueText.split("\\s+");
        
        if (moAuxJsonFieldSettings.getWordsMin() > 0 && words.length < moAuxJsonFieldSettings.getWordsMin()) {
            message = "el número mínimo requerido de palabaras es " + moAuxJsonFieldSettings.getWordsMin() + ".";
            
            if (moAuxJsonFieldSettings.getWordsReq() == 0) {
                message = message.replace("requerido", "sugerido");
                valid = client.showMsgBoxConfirm(confirm + message + "\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) == JOptionPane.YES_OPTION;
            }
            
            if (!valid) {
                throw new Exception(prefix + message + suffix);
            }
        }
        
        if (moAuxJsonFieldSettings.getWordsMax() > 0 && words.length > moAuxJsonFieldSettings.getWordsMax()) {
            message = "el número máximo requerido de palabaras es " + moAuxJsonFieldSettings.getWordsMax() + ".";
            
            if (moAuxJsonFieldSettings.getWordsReq() == 0) {
                message = message.replace("requerido", "sugerido");
                valid = client.showMsgBoxConfirm(confirm + message + "\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) == JOptionPane.YES_OPTION;
            }
            
            if (!valid) {
                throw new Exception(prefix + message + suffix);
            }
        }
        
        String name1 = moAuxJsonFieldSettings.hasWord2Group() ? "de la primer palabra" : "del texto";
        
        if (moAuxJsonFieldSettings.getWord1LenMin() > 0 && words[0].length() < moAuxJsonFieldSettings.getWord1LenMin()) {
            message = "el número mínimo permitido de carácteres " + name1 + " es " + moAuxJsonFieldSettings.getWord1LenMin() + ".";
            
            if (moAuxJsonFieldSettings.getWord1LenMinReq() == 0) {
                message = message.replace("permitido", "sugerido");
                valid = client.showMsgBoxConfirm(confirm + message + "\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) == JOptionPane.YES_OPTION;
            }
            
            if (!valid) {
                throw new Exception(prefix + message + suffix);
            }
        }
        
        if (moAuxJsonFieldSettings.getWord1LenMax() > 0 && words[0].length() > moAuxJsonFieldSettings.getWord1LenMax()) {
            message = "el número máximo permitido de carácteres " + name1 + " es " + moAuxJsonFieldSettings.getWord1LenMax() + ".";
            
            if (moAuxJsonFieldSettings.getWord1LenMaxReq() == 0) {
                message = message.replace("permitido", "sugerido");
                valid = client.showMsgBoxConfirm(confirm + message + "\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) == JOptionPane.YES_OPTION;
            }
            
            if (!valid) {
                throw new Exception(prefix + message + suffix);
            }
        }
        
        if (moAuxJsonFieldSettings.hasWord2Group()) {
            String name2 = "de la segunda palabra";
            
            if (moAuxJsonFieldSettings.getWord2LenMin() > 0 && words[1].length() < moAuxJsonFieldSettings.getWord2LenMin()) {
                message = "el número mínimo permitido de carácteres " + name2 + " es " + moAuxJsonFieldSettings.getWord2LenMin() + ".";
                
                if (moAuxJsonFieldSettings.getWord2LenMinReq() == 0) {
                    message = message.replace("permitido", "sugerido");
                    valid = client.showMsgBoxConfirm(confirm + message + "\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) == JOptionPane.YES_OPTION;
                }

                if (!valid) {
                    throw new Exception(prefix + message + suffix);
                }
            }
            
            if (moAuxJsonFieldSettings.getWord2LenMax() > 0 && words[1].length() > moAuxJsonFieldSettings.getWord2LenMax()) {
                message = "el número máximo permitido de carácteres " + name2 + " es " + moAuxJsonFieldSettings.getWord2LenMax() + ".";
                
                if (moAuxJsonFieldSettings.getWord2LenMaxReq() == 0) {
                    message = message.replace("permitido", "sugerido");
                    valid = client.showMsgBoxConfirm(confirm + message + "\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) == JOptionPane.YES_OPTION;
                }

                if (!valid) {
                    throw new Exception(prefix + message + suffix);
                }
            }
            
            if (words[0].equalsIgnoreCase(words[1])) {
                message = "los valores " + name1 + " y " + name2 + " no pueden ser iguales.";
                throw new Exception(prefix + message + suffix);
            }
        }
        
        return true; // value text is valid!
    }
    
    /**
     * Retrieve required value of this field and required item.
     * @param client GUI client;
     * @param valueText Text value to validate.
     * @param item Required item.
     * @param addToScopePolicy Policy of adding to scope of values: a) No, b) Yes, c) Confirm (constants ADD_TO_SCOPE_POLICY_...)
     * @param allowedScopeInputCategories List of ID's of allowed input categories in scope of values.
     * @return Found value of this field and required item.
     * NOTE: When retrived registry value was updated by adding to its scope either the input category of the item itself, its member isRegistryEdited will be set to <code>true</code>.
     * @throws java.lang.Exception 
     */
    public ValueRetrieved retrieveValue(final SGuiClient client, final String valueText, final SDbItem item, final int addToScopePolicy, final ArrayList<Integer> allowedScopeInputCategories) throws Exception {
        SDbValue value = null;
        ArrayList<String> notes = new ArrayList<>();
        
        String sql = "SELECT id_value, scope_inp_ct, scope_item, b_del "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.C_VALUE) + " "
                + "WHERE fk_field = " + mnPkFieldId + " AND value_text = '" + valueText + "' "
                + "ORDER BY id_value;";
        
        try (Statement statement = client.getSession().getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            if (resultSet.next()) {
                value = (SDbValue) client.getSession().readRegistry(SModConsts.C_VALUE, new int[] { resultSet.getInt("id_value") });
                
                if (resultSet.getBoolean("b_del")) {
                    notes.add("El valor " + msName + " '" + valueText + "' está eliminado. Es necesario reactivarlo en el catálogo para poder usarlo.");
                }
                else {
                    ArrayList<String> ids;
                    
                    ids = new ArrayList<>(Arrays.asList(resultSet.getString("scope_inp_ct").split(",")));
                    
                    if (!ids.contains("" + item.getFkInputCategoryId())) {
                        ids = new ArrayList<>(Arrays.asList(resultSet.getString("scope_item").split(",")));
                        
                        if (!ids.contains("" + item.getPkItemId())) {
                            String message = "El valor " + msName + " '" + valueText + "' no está configurado para usarse con el ítem '" + item.getName() + "'";
                            
                            if (addToScopePolicy == ADD_TO_SCOPE_POLICY_YES || addToScopePolicy == ADD_TO_SCOPE_POLICY_CONFIRM) {
                                boolean add = addToScopePolicy == ADD_TO_SCOPE_POLICY_YES;
                                
                                if (!add) {
                                    add = client.showMsgBoxConfirm(message + ", pero puede hacerse.\n"
                                            + "¿Desea configurar el valor " + msName + " '" + valueText + "' para usarse con este ítem?") == JOptionPane.YES_OPTION;
                                }
                                
                                if (add) {
                                    if (allowedScopeInputCategories.contains(item.getFkInputCategoryId())) {
                                        value.addScopeInputCategoryId(item.getFkInputCategoryId());
                                    }
                                    else {
                                        value.addScopeItem(item.getPkItemId());
                                    }
                                    
                                    value.setRegistryEdited(true);
                                }
                            }
                            else {
                                notes.add(message + ".");
                            }
                        }
                    }
                }
            }
            else {
                notes.add("El valor " + msName + " '" + valueText + "' no existe. Es necesario crearlo en el catálogo para empezar a usarlo.");
            }
        }
        
        return new ValueRetrieved(value, notes);
    }
            
    /**
     * Retrieve all non-deleted values of this field.
     * @param session GUI session.
     * @return List of all found values of this field.
     * @throws java.lang.Exception
     */
    public ArrayList<SDbValue> retriveValues(final SGuiSession session) throws Exception {
        ArrayList<SDbValue> values = new ArrayList<>();
        
        String sql = "SELECT "
                + "v.id_value , "
                + "(SELECT GROUP_CONCAT(ic.name ORDER BY ic.name, ic.id_inp_ct SEPARATOR ' + ')"
                + " FROM " + SModConsts.TablesMap.get(SModConsts.SU_INP_CT) + " AS ic "
                + " WHERE FIND_IN_SET(ic.id_inp_ct, REPLACE(v.scope_inp_ct, ' ', ''))) AS _scope_inp_ct, "
                + "(SELECT GROUP_CONCAT(i.name ORDER BY i.name, i.id_item SEPARATOR ' + ')"
                + " FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i "
                + " WHERE FIND_IN_SET(i.id_item, REPLACE(v.scope_item, ' ', ''))) AS _scope_item "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.C_VALUE) + " AS v "
                + "WHERE v.fk_field = " + mnPkFieldId + " AND NOT v.b_del "
                + "ORDER BY v.value_text, v.id_value;";

        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                SDbValue value = new SDbValue();
                value.read(session, new int[] { resultSet.getInt("v.id_value") });
                value.setAuxScopeInputCategoriesNames(resultSet.getString("_scope_inp_ct"));
                value.setAuxScopeItemsNames(resultSet.getString("_scope_item"));
                values.add(value);
            }
        }
        
        return values;
    }
    
    public static class ValueRetrieved {
        
        public SDbValue Value;
        public ArrayList<String> Notes;
        
        public ValueRetrieved(final SDbValue value, final ArrayList<String> notes) {
            Value = value;
            Notes = new ArrayList<>(notes);
        }
    }
}
