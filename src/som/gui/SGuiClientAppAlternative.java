/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import sa.gui.util.SUtilConfigXml;
import sa.gui.util.SUtilConsts;
import sa.gui.util.SUtilLoginDlg;
import sa.gui.util.SUtilPasswordDlg;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.db.SDbDatabaseMonitor;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDatePicker;
import sa.lib.gui.SGuiDateRangePicker;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiUserGui;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiYearMonthPicker;
import sa.lib.gui.SGuiYearPicker;
import sa.lib.gui.bean.SBeanDialogReport;
import sa.lib.gui.bean.SBeanForm;
import sa.lib.gui.bean.SBeanFormDialog;
import sa.lib.gui.bean.SBeanFormProcess;
import sa.lib.gui.bean.SBeanOptionPicker;
import sa.lib.img.SImgConsts;
import sa.lib.xml.SXmlUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.SModUtils;
import som.mod.SModuleCfg;
import som.mod.SModuleSom;
import som.mod.SModuleSomLog;
import som.mod.SModuleSomOs;
import som.mod.SModuleSomRm;
import som.mod.cfg.db.SDbCompany;
import som.mod.cfg.db.SDbUser;
import som.mod.cfg.db.SDbUserGui;
import som.mod.som.form.SDialogRepItemTickets;

/**
 *
 * @author Isabel Servín
 */
public class SGuiClientAppAlternative extends JFrame implements SGuiClient, ActionListener {

    public static final String APP_NAME = SGuiClientApp.APP_NAME;
    public static final String APP_RELEASE = SGuiClientApp.APP_RELEASE;
    public static final String APP_COPYRIGHT = SGuiClientApp.APP_COPYRIGHT;
    public static final String APP_PROVIDER = "Software Aplicado SA de CV";

    public static final String VENDOR = APP_PROVIDER;
    public static final String VENDOR_COPYRIGHT = APP_NAME + " ©" + APP_COPYRIGHT + " " + VENDOR;
    public static final String VENDOR_PHONE = "(443) 204-1032";
    public static final String VENDOR_WEBSITE = "www.swaplicado.com.mx";
    public static final String VENDOR_MAIL = "contacto@swaplicado.com.mx";

    private int mnTerminal;
    private boolean mbFirstActivation;
    private boolean mbLoggedIn;
    private SGuiSession moSession;
    private SDbDatabase moSysDatabase;
    private SDbDatabaseMonitor moSysDatabaseMonitor;
    private Statement miSysStatement;
    private String msCompany;

    private SGuiDatePicker moDatePicker;
    private SGuiDateRangePicker moDateRangePicker;
    private SGuiYearPicker moYearPicker;
    private SGuiYearMonthPicker moYearMonthPicker;
    private JFileChooser moFileChooser;
    private ImageIcon moIcon;
    private ImageIcon moIconCloseActive;
    private ImageIcon moIconCloseInactive;
    private ImageIcon moIconCloseBright;
    private ImageIcon moIconCloseDark;
    
    private SDialogRepItemTickets moDialogRepItemTickets;

    /**
     * Creates new form SGuiClient
     */
    public SGuiClientAppAlternative() {
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtpTabbedPane = new javax.swing.JTabbedPane();
        jpStatusBar = new javax.swing.JPanel();
        jpStatusBar1 = new javax.swing.JPanel();
        jtfSystemDate = new javax.swing.JTextField();
        jtfWorkingDate = new javax.swing.JTextField();
        jbWorkingDate = new javax.swing.JButton();
        jtfSessionBranch = new javax.swing.JTextField();
        jtfUser = new javax.swing.JTextField();
        jtfUserTs = new javax.swing.JTextField();
        jpStatusBar2 = new javax.swing.JPanel();
        jlAppRelease = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jmbMenuBar = new javax.swing.JMenuBar();
        jmFile = new javax.swing.JMenu();
        jmiFileWorkingDate = new javax.swing.JMenuItem();
        jmiFileUserPassword = new javax.swing.JMenuItem();
        jsFile1 = new javax.swing.JPopupMenu.Separator();
        jmiFileCloseViewsAll = new javax.swing.JMenuItem();
        jmiFileCloseViewsOther = new javax.swing.JMenuItem();
        jsFile2 = new javax.swing.JPopupMenu.Separator();
        jmiFileCloseSession = new javax.swing.JMenuItem();
        jsFile3 = new javax.swing.JPopupMenu.Separator();
        jmiFileExit = new javax.swing.JMenuItem();
        jmView = new javax.swing.JMenu();
        jmiTicket = new javax.swing.JMenuItem();
        jmiWithoutLabTicket = new javax.swing.JMenuItem();
        jmiLabTicket = new javax.swing.JMenuItem();
        jmiTicketReport = new javax.swing.JMenuItem();
        jmHelp = new javax.swing.JMenu();
        jmiHelpHelp = new javax.swing.JMenuItem();
        jsHelp1 = new javax.swing.JPopupMenu.Separator();
        jmiHelpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().add(jtpTabbedPane, java.awt.BorderLayout.CENTER);

        jpStatusBar.setBackground(java.awt.Color.black);
        jpStatusBar.setLayout(new java.awt.BorderLayout());

        jpStatusBar1.setOpaque(false);
        jpStatusBar1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jtfSystemDate.setEditable(false);
        jtfSystemDate.setForeground(java.awt.Color.white);
        jtfSystemDate.setText("00/00/0000");
        jtfSystemDate.setToolTipText("Fecha de sistema");
        jtfSystemDate.setFocusable(false);
        jtfSystemDate.setOpaque(false);
        jtfSystemDate.setPreferredSize(new java.awt.Dimension(65, 20));
        jpStatusBar1.add(jtfSystemDate);

        jtfWorkingDate.setEditable(false);
        jtfWorkingDate.setForeground(java.awt.Color.white);
        jtfWorkingDate.setText("00/00/0000");
        jtfWorkingDate.setToolTipText("Fecha de trabajo");
        jtfWorkingDate.setFocusable(false);
        jtfWorkingDate.setOpaque(false);
        jtfWorkingDate.setPreferredSize(new java.awt.Dimension(65, 20));
        jtfWorkingDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfWorkingDateActionPerformed(evt);
            }
        });
        jpStatusBar1.add(jtfWorkingDate);

        jbWorkingDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/som/gui/img/gui_date.gif"))); // NOI18N
        jbWorkingDate.setToolTipText("Cambiar fecha de trabajo");
        jbWorkingDate.setContentAreaFilled(false);
        jbWorkingDate.setPreferredSize(new java.awt.Dimension(20, 20));
        jpStatusBar1.add(jbWorkingDate);

        jtfSessionBranch.setEditable(false);
        jtfSessionBranch.setForeground(java.awt.Color.white);
        jtfSessionBranch.setText("TEXT");
        jtfSessionBranch.setToolTipText("Sucursal actual");
        jtfSessionBranch.setFocusable(false);
        jtfSessionBranch.setOpaque(false);
        jtfSessionBranch.setPreferredSize(new java.awt.Dimension(50, 20));
        jpStatusBar1.add(jtfSessionBranch);

        jtfUser.setEditable(false);
        jtfUser.setForeground(java.awt.Color.white);
        jtfUser.setText("TEXT");
        jtfUser.setToolTipText("Usuario");
        jtfUser.setFocusable(false);
        jtfUser.setOpaque(false);
        jtfUser.setPreferredSize(new java.awt.Dimension(100, 20));
        jpStatusBar1.add(jtfUser);

        jtfUserTs.setEditable(false);
        jtfUserTs.setForeground(java.awt.Color.white);
        jtfUserTs.setText("00/00/0000 00.00.00 +0000");
        jtfUserTs.setToolTipText("Marca de tiempo de acceso");
        jtfUserTs.setFocusable(false);
        jtfUserTs.setOpaque(false);
        jtfUserTs.setPreferredSize(new java.awt.Dimension(150, 20));
        jpStatusBar1.add(jtfUserTs);

        jpStatusBar.add(jpStatusBar1, java.awt.BorderLayout.CENTER);

        jpStatusBar2.setOpaque(false);
        jpStatusBar2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jlAppRelease.setForeground(new java.awt.Color(0, 153, 153));
        jlAppRelease.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlAppRelease.setText("RELEASE");
        jlAppRelease.setPreferredSize(new java.awt.Dimension(100, 20));
        jpStatusBar2.add(jlAppRelease);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/som/gui/img/swap_logo_9.jpg"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(80, 19));
        jpStatusBar2.add(jLabel1);

        jpStatusBar.add(jpStatusBar2, java.awt.BorderLayout.EAST);

        getContentPane().add(jpStatusBar, java.awt.BorderLayout.SOUTH);

        jmFile.setText("Archivo");

        jmiFileWorkingDate.setText("Cambiar fecha de trabajo...");
        jmFile.add(jmiFileWorkingDate);

        jmiFileUserPassword.setText("Cambiar contraseña...");
        jmFile.add(jmiFileUserPassword);
        jmFile.add(jsFile1);

        jmiFileCloseViewsAll.setText("Cerrar todas las vistas");
        jmFile.add(jmiFileCloseViewsAll);

        jmiFileCloseViewsOther.setText("Cerrar las otras vistas");
        jmFile.add(jmiFileCloseViewsOther);
        jmFile.add(jsFile2);

        jmiFileCloseSession.setText("Cerrar sesión de usuario");
        jmFile.add(jmiFileCloseSession);
        jmFile.add(jsFile3);

        jmiFileExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jmiFileExit.setText("Salir");
        jmFile.add(jmiFileExit);

        jmbMenuBar.add(jmFile);

        jmView.setText("Boletos báscula");

        jmiTicket.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        jmiTicket.setText("Boletos báscula");
        jmView.add(jmiTicket);

        jmiWithoutLabTicket.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        jmiWithoutLabTicket.setText("Boletos pendientes de resultados de laboratorio");
        jmView.add(jmiWithoutLabTicket);

        jmiLabTicket.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        jmiLabTicket.setText("Boletos con resultados de laboratorio");
        jmView.add(jmiLabTicket);

        jmiTicketReport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        jmiTicketReport.setText("Reporte de lista de boletos de bascula por producto");
        jmView.add(jmiTicketReport);

        jmbMenuBar.add(jmView);

        jmHelp.setText("Ayuda");

        jmiHelpHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jmiHelpHelp.setText("Ayuda");
        jmiHelpHelp.setEnabled(false);
        jmHelp.add(jmiHelpHelp);
        jmHelp.add(jsHelp1);

        jmiHelpAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        jmiHelpAbout.setText("Acerca de...");
        jmiHelpAbout.setEnabled(false);
        jmHelp.add(jmiHelpAbout);

        jmbMenuBar.add(jmHelp);

        setJMenuBar(jmbMenuBar);

        setSize(new java.awt.Dimension(1024, 640));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        windowClosing();
    }//GEN-LAST:event_formWindowClosing

    private void jtfWorkingDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfWorkingDateActionPerformed
        actionFileWorkingDate();
    }//GEN-LAST:event_jtfWorkingDateActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            SLibUtils.showException(SGuiClient.class.getName(), e);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               new SGuiClientAppAlternative().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jbWorkingDate;
    private javax.swing.JLabel jlAppRelease;
    private javax.swing.JMenu jmFile;
    private javax.swing.JMenu jmHelp;
    private javax.swing.JMenu jmView;
    private javax.swing.JMenuBar jmbMenuBar;
    private javax.swing.JMenuItem jmiFileCloseSession;
    private javax.swing.JMenuItem jmiFileCloseViewsAll;
    private javax.swing.JMenuItem jmiFileCloseViewsOther;
    private javax.swing.JMenuItem jmiFileExit;
    private javax.swing.JMenuItem jmiFileUserPassword;
    private javax.swing.JMenuItem jmiFileWorkingDate;
    private javax.swing.JMenuItem jmiHelpAbout;
    private javax.swing.JMenuItem jmiHelpHelp;
    private javax.swing.JMenuItem jmiLabTicket;
    private javax.swing.JMenuItem jmiTicket;
    private javax.swing.JMenuItem jmiTicketReport;
    private javax.swing.JMenuItem jmiWithoutLabTicket;
    private javax.swing.JPanel jpStatusBar;
    private javax.swing.JPanel jpStatusBar1;
    private javax.swing.JPanel jpStatusBar2;
    private javax.swing.JPopupMenu.Separator jsFile1;
    private javax.swing.JPopupMenu.Separator jsFile2;
    private javax.swing.JPopupMenu.Separator jsFile3;
    private javax.swing.JPopupMenu.Separator jsHelp1;
    private javax.swing.JTextField jtfSessionBranch;
    private javax.swing.JTextField jtfSystemDate;
    private javax.swing.JTextField jtfUser;
    private javax.swing.JTextField jtfUserTs;
    private javax.swing.JTextField jtfWorkingDate;
    private javax.swing.JTabbedPane jtpTabbedPane;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        mbFirstActivation = true;

        setExtendedState(Frame.MAXIMIZED_BOTH);

        SBeanForm.OwnerFrame = this;
        SBeanFormDialog.OwnerFrame = this;
        SBeanFormProcess.OwnerFrame = this;
        SBeanOptionPicker.OwnerFrame = this;
        SBeanDialogReport.OwnerFrame = this;

        logout();

        try {
            String xml = SXmlUtils.readXml(SUtilConsts.FILE_NAME_CFG);
            SUtilConfigXml configXml = new SUtilConfigXml();
            configXml.processXml(xml);

            TimeZone zone = SLibUtils.createTimeZone(TimeZone.getDefault(), TimeZone.getTimeZone((String) configXml.getAttribute(SUtilConfigXml.ATT_TIME_ZONE).getValue()));
            SLibUtils.restoreDateFormats(zone);
            TimeZone.setDefault(zone);

            moSysDatabase = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            int result = moSysDatabase.connect(
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_HOST).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_PORT).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_NAME).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_NAME).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_PSWD).getValue());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            else {
                moSysDatabaseMonitor = new SDbDatabaseMonitor(moSysDatabase);
                moSysDatabaseMonitor.startThread();

                miSysStatement = moSysDatabase.getConnection().createStatement();
            }

            mnTerminal = SLibUtils.parseInt((String) configXml.getAttribute(SUtilConfigXml.ATT_TERMINAL).getValue());

            moDatePicker = new SGuiDatePicker(this, SGuiConsts.DATE_PICKER_DATE);
            moDateRangePicker = new SGuiDateRangePicker(this);
            moYearPicker = new SGuiYearPicker(this);
            moYearMonthPicker = new SGuiYearMonthPicker(this);
            moFileChooser = new JFileChooser();
            moIcon = new ImageIcon(getClass().getResource("/som/gui/img/swap_icon.png"));
            moIconCloseActive = new ImageIcon(getClass().getResource("/sa/lib/img/gui_close.png"));
            moIconCloseInactive = new ImageIcon(getClass().getResource("/sa/lib/img/gui_close_ina.png"));
            moIconCloseBright = new ImageIcon(getClass().getResource("/sa/lib/img/gui_close_bri.png"));
            moIconCloseDark = new ImageIcon(getClass().getResource("/sa/lib/img/gui_close_dar.png"));

            setIconImage(moIcon.getImage());
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
            actionFileExit();
        }

        jbWorkingDate.addActionListener(this);

        jmiFileWorkingDate.addActionListener(this);
        jmiFileUserPassword.addActionListener(this);
        jmiFileCloseViewsAll.addActionListener(this);
        jmiFileCloseViewsOther.addActionListener(this);
        jmiFileCloseSession.addActionListener(this);
        jmiFileExit.addActionListener(this);
        jmiTicket.addActionListener(this);
        jmiWithoutLabTicket.addActionListener(this);
        jmiLabTicket.addActionListener(this);
        jmiTicketReport.addActionListener(this);
        jmiHelpHelp.addActionListener(this);
        jmiHelpAbout.addActionListener(this);

        moDialogRepItemTickets = null;
        
        jlAppRelease.setText(APP_RELEASE);
    }

    private void windowActivated() {
        if (mbFirstActivation){
            mbFirstActivation = false;
            login();
        }
    }

    private void windowClosing() {
        if (mbLoggedIn) {
            logout();
        }
    }

    private void renderMenues(JMenu[] menues) {
        jmbMenuBar.removeAll();
        validate();

        jmbMenuBar.add(jmFile);
        jmbMenuBar.add(jmView);

        if (menues != null) {
            for (JMenu menu : menues) {
                jmbMenuBar.add(menu);
            }
        }

        jmbMenuBar.add(jmHelp);
        validate();
    }

    private void renderClientSession(SGuiClientSessionCustom clientSession) {
        // Display client session parameters to user in GUI...
    }

    private void logout() {
        renderMenues(null);
        actionFileCloseViewAll();

        mbLoggedIn = false;
        moSession = null;

        msCompany = "";
        setTitle(APP_NAME);

        jtfUserTs.setText("");
        jtfSystemDate.setText("");
        jtfSessionBranch.setText("");
        jtfUser.setText("");
        jtfWorkingDate.setText("");
        renderClientSession(null);

        jbWorkingDate.setEnabled(false);
    }

    private void login() {
        int result = SLibConsts.UNDEFINED;
        int modulesAccessed = 0;
        String sql = "";
        ResultSet resultSet;
        Date date;
        SDbUser user;
        SDbCompany company = null;
        SDbDatabase database = null;
        SDbDatabase databaseCo = null;
        SUtilLoginDlg loginDlg = new SUtilLoginDlg(this);
        JToggleButton defaultToggleButton = null;

        loginDlg.setVisible(true);

        if (loginDlg.getFormResult() != SGuiConsts.FORM_RESULT_OK) {
            actionFileExit();
        }
        else {
            try {
                SGuiUtils.setCursorWait(this);

                // Get system date:

                sql = "SELECT NOW() ";
                resultSet = miSysStatement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception (SUtilConsts.ERR_MSG_SYS_DATE);
                }
                else {
                    date = resultSet.getTimestamp(1);
                }

                mbLoggedIn = true;
                moSession = new SGuiSession(this);
                moSession.setSystemDate(date);
                moSession.setWorkingDate(date);
                moSession.setUserTs(date);
                moSession.setDatabase(loginDlg.getDatabase());

                user = new SDbUser();
                user.read(moSession, loginDlg.getUserKey());

                moSession.setConfigSystem(null);
                moSession.setConfigCompany(null);
                moSession.setConfigBranch(null);
                moSession.setUser(user);
                moSession.setModuleUtils(new SModUtils());
                moSession.getModules().add(new SModuleCfg(this));
                moSession.getModules().add(new SModuleSom(this));
                moSession.getModules().add(new SModuleSomRm(this));
                moSession.getModules().add(new SModuleSomOs(this));
                moSession.getModules().add(new SModuleSomLog(this));

                //user.computeAccess(moSession);    // not implemented yet!
                moSession.setSessionCustom(user.createDefaultUserSession(this, mnTerminal));

                company = new SDbCompany();
                company.read(moSession, new int[] { SUtilConsts.BPR_CO_ID });

                ((SGuiClientSessionCustom) moSession.getSessionCustom()).setCompany(company);
                ((SGuiClientSessionCustom) moSession.getSessionCustom()).setLocalCurrencyCode(company.getCurrencyCode());

                msCompany = loginDlg.getCompany();
                setTitle(APP_NAME + " - " + msCompany);

                database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
                result = database.connect(
                        company.getExternalHost(),
                        company.getExternalPort(),
                        company.getExternalDatabase(),
                        company.getExternalUser(),
                        company.getExternalPassword());

                if (result != SDbConsts.CONNECTION_OK) {
                    throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION + "\nLa base de datos externa 'ERP (" + company.getExternalDatabase() + ")' no está disponible o es inaccesible.");
                }
                else {
                    ((SGuiClientSessionCustom) moSession.getSessionCustom()).setExtDatabase(database);
                }

                databaseCo = new SDbDatabase(SDbConsts.DBMS_MYSQL);
                result = databaseCo.connect(
                        company.getExternalHost(),
                        company.getExternalPort(),
                        company.getExternalDatabaseCo(),
                        company.getExternalUser(),
                        company.getExternalPassword());

                if (result != SDbConsts.CONNECTION_OK) {
                    throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION + "\nLa base de datos externa 'empresa (" + company.getExternalDatabaseCo() + ")' no está disponible o es inaccesible.");
                }
                else {
                    ((SGuiClientSessionCustom) moSession.getSessionCustom()).setExtDatabaseCo(databaseCo);
                }

                jtfSystemDate.setText(SLibUtils.DateFormatDate.format(moSession.getSystemDate()));
                jtfWorkingDate.setText(SLibUtils.DateFormatDate.format(moSession.getWorkingDate()));
                jtfSessionBranch.setText(company.getChildBranches().get(0).getCode());
                jtfUser.setText(user.getName());
                jtfUserTs.setText(SLibUtils.DateFormatDatetimeTimeZone.format(moSession.getUserTs()));

                jmFile.setEnabled(true);
                jmView.setEnabled(true);
                jmHelp.setEnabled(true);

                jbWorkingDate.setEnabled(true);

                jmiFileWorkingDate.setEnabled(jbWorkingDate.isEnabled());

                renderClientSession((SGuiClientSessionCustom) moSession.getSessionCustom());
                
                jmiTicket.setEnabled(user.hasPrivilegeAlternative(SModSysConsts.CS_ALT_RIG_PUR));                
                jmiWithoutLabTicket.setEnabled(user.hasPrivilegeAlternative(SModSysConsts.CS_ALT_RIG_QTY));                
                jmiLabTicket.setEnabled(user.hasPrivilegeAlternative(SModSysConsts.CS_ALT_RIG_QTY));                
                jmiTicketReport.setEnabled(user.hasPrivilegeAlternative(SModSysConsts.CS_ALT_RIG_PUR));                

                if (defaultToggleButton != null) {
                    defaultToggleButton.requestFocus();
                }

                if (modulesAccessed == 1) {
                    defaultToggleButton.doClick();
                }

                SGuiUtils.setCursorDefault(this);
            }
            catch(SQLException e) {
                SGuiUtils.setCursorDefault(this);
                SLibUtils.showException(this, e);
                actionFileExit();
            }
            catch(Exception e) {
                SGuiUtils.setCursorDefault(this);
                SLibUtils.showException(this, e);
                actionFileExit();
            }
        }
    }

    public void actionToggleViewModule(int type, int subtype) {
        renderMenues(moSession.getModule(type, subtype).getMenus());
    }

    public void actionFileWorkingDate() {
        moDatePicker.resetPicker();
        moDatePicker.setOption(moSession.getWorkingDate());
        moDatePicker.setVisible(true);

        if (moDatePicker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            moSession.setWorkingDate(moDatePicker.getOption());
            jtfWorkingDate.setText(SLibUtils.DateFormatDate.format(moSession.getWorkingDate()));
        }
    }

    public void actionFileUserPassword() {
        new SUtilPasswordDlg(this).setVisible(true);
    }

    public void actionFileCloseViewAll() {
        try {
            SGuiUtils.setCursorWait(this);

            for (int i = 0; i < jtpTabbedPane.getTabCount(); i++) {
                ((SGridPaneView) jtpTabbedPane.getComponentAt(i)).paneViewClosed();
            }

            jtpTabbedPane.removeAll();
        }
        catch(Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            SGuiUtils.setCursorDefault(this);
        }
    }

    public void actionFileCloseViewOther() {
        int i = 0;
        int index = jtpTabbedPane.getSelectedIndex();

        try {
            SGuiUtils.setCursorWait(this);

            for (i = jtpTabbedPane.getTabCount() - 1; i > index; i--) {
                ((SGridPaneView) jtpTabbedPane.getComponentAt(i)).paneViewClosed();  // this preserves view user settings
                jtpTabbedPane.removeTabAt(i);
            }

            for (i = 0; i < index; i++) {
                ((SGridPaneView) jtpTabbedPane.getComponentAt(0)).paneViewClosed();  // this preserves view user settings
                jtpTabbedPane.removeTabAt(0);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            SGuiUtils.setCursorDefault(this);
        }
    }

    public void actionFileCloseSession() {
        logout();
        login();
    }

    private void actionFileExit() {
        logout();
        System.exit(0);
    }

    public void actionHelpHelp() {

    }

    public void actionHelpAbout() {

    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public JTabbedPane getTabbedPane() {
        return jtpTabbedPane;
    }

    @Override
    public SDbDatabase getSysDatabase() {
        return moSysDatabase;
    }

    @Override
    public Statement getSysStatement() {
        return miSysStatement;
    }

    @Override
    public SGuiSession getSession() {
        return moSession;
    }

    @Override
    public SGuiDatePicker getDatePicker() {
        return moDatePicker;
    }

    @Override
    public SGuiDateRangePicker getDateRangePicker() {
        return moDateRangePicker;
    }

    @Override
    public SGuiYearPicker getYearPicker() {
        return moYearPicker;
    }

    @Override
    public SGuiYearMonthPicker getYearMonthPicker() {
        return moYearMonthPicker;
    }

    @Override
    public JFileChooser getFileChooser() {
        return moFileChooser;
    }

    @Override
    public ImageIcon getImageIcon(int icon) {
        ImageIcon imageIcon = null;

        switch(icon) {
            case SImgConsts.ICO_GUI_CLOSE:
                imageIcon = moIconCloseActive;
                break;
            case SImgConsts.ICO_GUI_CLOSE_INA:
                imageIcon = moIconCloseInactive;
                break;
            case SImgConsts.ICO_GUI_CLOSE_BRI:
                imageIcon = moIconCloseBright;
                break;
            case SImgConsts.ICO_GUI_CLOSE_DAR:
                imageIcon = moIconCloseDark;
                break;
            default:
                showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return imageIcon;
    }

    @Override
    public SGuiUserGui readUserGui(int[] key) {
        SDbUserGui userGui = new SDbUserGui();

        try {
            userGui.read(moSession, key);
        }
        catch (SQLException e) {
            userGui = null;
            SLibUtils.printException(this, e);
        }
        catch (Exception e) {
            userGui = null;
            SLibUtils.printException(this, e);
        }

        return userGui;
    }

    @Override
    public SGuiUserGui saveUserGui(int[] key, String gui) {
        SDbUserGui userGui = (SDbUserGui) readUserGui(key);

        if (userGui == null) {
            userGui = new SDbUserGui();
            userGui.setPrimaryKey(key);
        }

        try {
            userGui.setGui(gui);
            userGui.save(moSession);
        }
        catch (SQLException e) {
            userGui = null;
            SLibUtils.printException(this, e);
        }
        catch (Exception e) {
            userGui = null;
            SLibUtils.printException(this, e);
        }

        return userGui;
    }

    @Override
    public HashMap<String, Object> createReportParams() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("sAppName", APP_NAME);
        params.put("sAppCopyright", APP_COPYRIGHT);
        params.put("sAppProvider", APP_PROVIDER);
        params.put("sVendorCopyright", VENDOR_COPYRIGHT);
        params.put("sVendorWebsite", VENDOR_WEBSITE);
        params.put("sVendorMail", VENDOR_MAIL);
        params.put("sCompany", msCompany);
        params.put("sUser", moSession.getUser().getName());
        params.put("oFormatDate", SLibUtils.DateFormatDate);
        params.put("oFormatDatetime", SLibUtils.DateFormatDatetime);

        return params;
    }

    @Override
    public String getTableCompany() {
        return SModConsts.TablesMap.get(SModConsts.SU_CO);
    }

    @Override
    public String getTableUser() {
        return SModConsts.TablesMap.get(SModConsts.CU_USR);
    }

    @Override
    public String getAppName() {
        return APP_NAME;
    }

    @Override
    public String getAppRelease() {
        return APP_RELEASE;
    }

    @Override
    public String getAppCopyright() {
        return APP_COPYRIGHT;
    }

    @Override
    public String getAppProvider() {
        return APP_PROVIDER;
    }

    @Override
    public void showMsgBoxError(String msg) {
        JOptionPane.showMessageDialog(this, msg, SGuiConsts.MSG_BOX_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showMsgBoxWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, SGuiConsts.MSG_BOX_WARNING, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void showMsgBoxInformation(String msg) {
        JOptionPane.showMessageDialog(this, msg, SGuiConsts.MSG_BOX_INFORMATION, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public int showMsgBoxConfirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, SGuiConsts.MSG_BOX_CONFIRM, JOptionPane.YES_NO_OPTION);
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();

        if (moSysDatabaseMonitor != null && moSysDatabaseMonitor.isAlive()) {
            moSysDatabaseMonitor.stopThread();
        }

        System.out.println(this.getClass().getName() + ".finalize() called!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbWorkingDate) {
                actionFileWorkingDate();
            }
        }
        else if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();

            if (menuItem == jmiFileWorkingDate) {
                actionFileWorkingDate();
            }
            else if (menuItem == jmiFileUserPassword) {
                actionFileUserPassword();
            }
            else if (menuItem == jmiFileCloseViewsAll) {
                actionFileCloseViewAll();
            }
            else if (menuItem == jmiFileCloseViewsOther) {
                actionFileCloseViewOther();
            }
            else if (menuItem == jmiFileCloseSession) {
                actionFileCloseSession();
            }
            else if (menuItem == jmiFileExit) {
                actionFileExit();
            }
            else if (menuItem == jmiTicket) {
                moSession.showView(SModConsts.S_ALT_TIC, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == jmiWithoutLabTicket) {
                moSession.showView(SModConsts.S_ALT_LAB, SModConsts.SX_ALT_WO_LAB, null);
            }
            else if (menuItem == jmiLabTicket) {
                moSession.showView(SModConsts.S_ALT_LAB, SModConsts.SX_ALT_W_LAB, null);
            }
            else if (menuItem == jmiTicketReport) {
                new SDialogRepItemTickets(this, "Reporte de lista de boletos de báscula por producto").setVisible(true);
            }
            else if (menuItem == jmiHelpHelp) {
                actionHelpHelp();
            }
            else if (menuItem == jmiHelpAbout) {
                actionHelpAbout();
            }
        }
    }
}