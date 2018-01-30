/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.gui;

import java.sql.SQLException;
import java.sql.Statement;
import sa.lib.SLibUtils;
import sa.lib.db.SDbDatabase;
import sa.lib.db.SDbDatabaseMonitor;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSessionCustom;
import som.mod.cfg.db.SDbCompany;
import som.mod.cfg.db.SDbCompanyBranch;

/**
 *
 * @author Sergio Flores
 */
public final class SGuiClientSessionCustom implements SGuiSessionCustom {

    private SGuiClient miClient;

    private int[] manLocalCountryKey;
    private int[] manLocalCurrencyKey;
    private String msLocalCountry;
    private String msLocalCountryCode;
    private String msLocalCurrency;
    private String msLocalCurrencyCode;
    private String msLocalLanguage;

    private int mnTerminal;

    private SDbCompany moCompany;

    private SDbDatabase moExtDatabase;
    private SDbDatabaseMonitor moExtDatabaseMonitor;
    private Statement miExtStatement;

    private SDbDatabase moExtDatabaseCo;
    private SDbDatabaseMonitor moExtDatabaseCoMonitor;
    private Statement miExtStatementCo;

    public SGuiClientSessionCustom(SGuiClient client, int terminal) {
        miClient = client;

        manLocalCountryKey = null;
        manLocalCurrencyKey = null;
        msLocalCountry = "";
        msLocalCountryCode = "";
        msLocalCurrency = "";
        msLocalCurrencyCode = "";
        msLocalLanguage = "";

        mnTerminal = terminal;

        moCompany = null;

        miExtStatement = null;
        moExtDatabase = null;
        moExtDatabaseMonitor = null;

        miExtStatementCo = null;
        moExtDatabaseCo = null;
        moExtDatabaseCoMonitor = null;
    }

    /*
     * Private methods
     */

    /*
     * Public methods
     */

    public void setLocalCurrencyCode(String s) { msLocalCurrencyCode = s; }

    public int[] getLocalCountryKey() { return manLocalCountryKey; }
    public String getLocalCountry() { return msLocalCountry; }
    public String getLocalCountryCode() { return msLocalCountryCode; }
    public boolean isLocalCountry(final int[] key) { return SLibUtils.compareKeys(key, manLocalCountryKey); }
    public int[] getLocalCurrencyKey() { return manLocalCurrencyKey; }
    public String getLocalCurrency() { return msLocalCurrency; }
    public String getLocalCurrencyCode() { return msLocalCurrencyCode; }
    public boolean isLocalCurrency(final int[] key) { return SLibUtils.compareKeys(key, manLocalCurrencyKey); }
    public String getLocalLanguage() { return msLocalLanguage; }

    public String getCountry(final int[] key) { throw new UnsupportedOperationException("Not supported yet."); }
    public String getCountryCode(final int[] key) { throw new UnsupportedOperationException("Not supported yet."); }
    public String getCurrency(final int[] key) { throw new UnsupportedOperationException("Not supported yet."); }
    public String getCurrencyCode(final int[] key) { throw new UnsupportedOperationException("Not supported yet."); }
    public String getLanguage(final int[] key) { throw new UnsupportedOperationException("Not supported yet."); }

    public int getTerminal() { return mnTerminal; }

    public void setCompany(SDbCompany o) { moCompany = o; }

    public SDbCompany getCompany() { return moCompany; }
    public SDbCompanyBranch getCompanyBranch() { return moCompany.getChildBranches().get(0); }

    public void setExtDatabase(final SDbDatabase database) throws SQLException, Exception {
        moExtDatabase = database;
        moExtDatabaseMonitor = new SDbDatabaseMonitor(moExtDatabase);
        moExtDatabaseMonitor.startThread();

        miExtStatement = moExtDatabase.getConnection().createStatement();
    }

    public SDbDatabase getExtDatabase() { return moExtDatabase; }
    public Statement getExtStatement() { return miExtStatement; }

    public void setExtDatabaseCo(final SDbDatabase database) throws SQLException, Exception {
        moExtDatabaseCo = database;
        moExtDatabaseCoMonitor = new SDbDatabaseMonitor(moExtDatabaseCo);
        moExtDatabaseCoMonitor.startThread();

        miExtStatementCo = moExtDatabaseCo.getConnection().createStatement();
    }

    public SDbDatabase getExtDatabaseCo() { return moExtDatabaseCo; }
    public Statement getExtStatementCo() { return miExtStatementCo; }

    @Override
    public void finalize() throws Throwable {
        super.finalize();

        if (moExtDatabaseMonitor != null && moExtDatabaseMonitor.isAlive()) {
            moExtDatabaseMonitor.stopThread();
        }

        System.out.println(this.getClass().getName() + ".finalize() called!");
    }
}
