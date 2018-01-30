/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.cfg.form;

import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.cfg.db.SDbCompany;
import som.mod.som.db.SSomConsts;

/**
 *
 * @author Sergio Flores
 */
public class SFormCompany extends sa.lib.gui.bean.SBeanForm {

    private SDbCompany moRegistry;

    /**
     * Creates new form SFormCompany
     */
    public SFormCompany(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.CU_CO, SLibConsts.UNDEFINED, title);
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

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel5 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel17 = new javax.swing.JPanel();
        jlCurrencyCode = new javax.swing.JLabel();
        moTextCurrencyCode = new sa.lib.gui.bean.SBeanFieldText();
        jPanel27 = new javax.swing.JPanel();
        jlMaximumStockDifferenceKg = new javax.swing.JLabel();
        moDecMaximumStockDifferenceKg = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlXtaStockUnit = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jlVersion = new javax.swing.JLabel();
        jtfVersion = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jlVersionTs = new javax.swing.JLabel();
        jtfVersionTs = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jlLanguaje = new javax.swing.JLabel();
        moKeyLanguaje = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel35 = new javax.swing.JPanel();
        jlDivisionDefault = new javax.swing.JLabel();
        moKeyDivisionDefault = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel12 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlExternalHost = new javax.swing.JLabel();
        moTextExternalHost = new sa.lib.gui.bean.SBeanFieldText();
        jPanel7 = new javax.swing.JPanel();
        jlExternalPort = new javax.swing.JLabel();
        moTextExternalPort = new sa.lib.gui.bean.SBeanFieldText();
        jPanel8 = new javax.swing.JPanel();
        jlExternalUser = new javax.swing.JLabel();
        moTextExternalUser = new sa.lib.gui.bean.SBeanFieldText();
        jPanel9 = new javax.swing.JPanel();
        jlExternalPassword = new javax.swing.JLabel();
        moPswdExternalPassword = new sa.lib.gui.bean.SBeanFieldPassword();
        jPanel10 = new javax.swing.JPanel();
        jlExternalDatabase = new javax.swing.JLabel();
        moTextExternalDatabase = new sa.lib.gui.bean.SBeanFieldText();
        jPanel14 = new javax.swing.JPanel();
        jlExternalDatabaseCo = new javax.swing.JLabel();
        moTextExternalDatabaseCo = new sa.lib.gui.bean.SBeanFieldText();
        jPanel11 = new javax.swing.JPanel();
        jlExternalCoId = new javax.swing.JLabel();
        moIntExternalCoId = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel20 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jlRevueltaId = new javax.swing.JLabel();
        moTextRevueltaId = new sa.lib.gui.bean.SBeanFieldText();
        jPanel22 = new javax.swing.JPanel();
        jlRevueltaOdbc = new javax.swing.JLabel();
        moTextRevueltaOdbc = new sa.lib.gui.bean.SBeanFieldText();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jlMailNotificationConfigProtocol = new javax.swing.JLabel();
        moTextMailNotificationConfigProtocol = new sa.lib.gui.bean.SBeanFieldText();
        jPanel30 = new javax.swing.JPanel();
        jlMailNotificationConfigHost = new javax.swing.JLabel();
        moTextMailNotificationConfigHost = new sa.lib.gui.bean.SBeanFieldText();
        jPanel31 = new javax.swing.JPanel();
        jlMailNotificationConfigPort = new javax.swing.JLabel();
        moTextMailNotificationConfigPort = new sa.lib.gui.bean.SBeanFieldText();
        jPanel32 = new javax.swing.JPanel();
        jlMailNotificationConfigUser = new javax.swing.JLabel();
        moTextMailNotificationConfigUser = new sa.lib.gui.bean.SBeanFieldText();
        jPanel33 = new javax.swing.JPanel();
        jlMailNotificationConfigPassword = new javax.swing.JLabel();
        moPswdMailNotificationConfigPassword = new sa.lib.gui.bean.SBeanFieldPassword();
        jPanel36 = new javax.swing.JPanel();
        moBoolMailNotificationConfigStartTls = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel37 = new javax.swing.JPanel();
        moBoolMailNotificationConfigAuth = new sa.lib.gui.bean.SBeanFieldBoolean();

        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel23.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jlCode);

        moTextCode.setText("sBeanFieldText1");
        jPanel4.add(moTextCode);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel5.add(jlName);

        moTextName.setText("sBeanFieldText1");
        moTextName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel5.add(moTextName);

        jPanel2.add(jPanel5);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCurrencyCode.setText("Código moneda:*");
        jlCurrencyCode.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel17.add(jlCurrencyCode);

        moTextCurrencyCode.setText("sBeanFieldText1");
        moTextCurrencyCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel17.add(moTextCurrencyCode);

        jPanel2.add(jPanel17);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMaximumStockDifferenceKg.setText("Máx. dif. inv. físico:");
        jlMaximumStockDifferenceKg.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel27.add(jlMaximumStockDifferenceKg);
        jPanel27.add(moDecMaximumStockDifferenceKg);

        jlXtaStockUnit.setText("UNIT");
        jlXtaStockUnit.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel27.add(jlXtaStockUnit);

        jPanel2.add(jPanel27);

        jPanel23.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel24.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVersion.setText("Versión base datos:");
        jlVersion.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jlVersion);

        jtfVersion.setEditable(false);
        jtfVersion.setText("jTextField1");
        jtfVersion.setFocusable(false);
        jtfVersion.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel25.add(jtfVersion);

        jPanel24.add(jPanel25);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVersionTs.setText("TS fix base datos:");
        jlVersionTs.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jlVersionTs);

        jtfVersionTs.setEditable(false);
        jtfVersionTs.setText("jTextField1");
        jtfVersionTs.setFocusable(false);
        jtfVersionTs.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel26.add(jtfVersionTs);

        jPanel24.add(jPanel26);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLanguaje.setText("Idioma local:*");
        jlLanguaje.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel34.add(jlLanguaje);

        moKeyLanguaje.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel34.add(moKeyLanguaje);

        jPanel24.add(jPanel34);

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDivisionDefault.setText("División default:*");
        jlDivisionDefault.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel35.add(jlDivisionDefault);

        moKeyDivisionDefault.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel35.add(moKeyDivisionDefault);

        jPanel24.add(jPanel35);

        jPanel23.add(jPanel24, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel23, java.awt.BorderLayout.NORTH);

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel18.setLayout(new java.awt.GridLayout(1, 2, 0, 5));

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros de conexión con sistema externo:"));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExternalHost.setForeground(new java.awt.Color(0, 102, 102));
        jlExternalHost.setText("Host:*");
        jlExternalHost.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jlExternalHost);

        moTextExternalHost.setText("sBeanFieldText1");
        moTextExternalHost.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(moTextExternalHost);

        jPanel13.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExternalPort.setForeground(new java.awt.Color(0, 102, 102));
        jlExternalPort.setText("Puerto:*");
        jlExternalPort.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jlExternalPort);

        moTextExternalPort.setText("sBeanFieldText1");
        jPanel7.add(moTextExternalPort);

        jPanel13.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExternalUser.setForeground(new java.awt.Color(0, 102, 102));
        jlExternalUser.setText("Usuario:*");
        jlExternalUser.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlExternalUser);

        moTextExternalUser.setText("sBeanFieldText1");
        moTextExternalUser.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel8.add(moTextExternalUser);

        jPanel13.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExternalPassword.setForeground(new java.awt.Color(0, 102, 102));
        jlExternalPassword.setText("Contraseña:*");
        jlExternalPassword.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlExternalPassword);

        moPswdExternalPassword.setText("sBeanFieldPassword1");
        moPswdExternalPassword.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel9.add(moPswdExternalPassword);

        jPanel13.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExternalDatabase.setForeground(new java.awt.Color(0, 102, 102));
        jlExternalDatabase.setText("BD ERP:*");
        jlExternalDatabase.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel10.add(jlExternalDatabase);

        moTextExternalDatabase.setText("sBeanFieldText1");
        moTextExternalDatabase.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(moTextExternalDatabase);

        jPanel13.add(jPanel10);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExternalDatabaseCo.setForeground(new java.awt.Color(0, 102, 102));
        jlExternalDatabaseCo.setText("BD empresa:*");
        jlExternalDatabaseCo.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel14.add(jlExternalDatabaseCo);

        moTextExternalDatabaseCo.setText("sBeanFieldText1");
        moTextExternalDatabaseCo.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel14.add(moTextExternalDatabaseCo);

        jPanel13.add(jPanel14);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExternalCoId.setText("ID empresa:*");
        jlExternalCoId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel11.add(jlExternalCoId);
        jPanel11.add(moIntExternalCoId);

        jPanel13.add(jPanel11);

        jPanel19.add(jPanel13, java.awt.BorderLayout.NORTH);

        jPanel18.add(jPanel19);

        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros de conexión con sistema Revuelta XXI:"));
        jPanel16.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRevueltaId.setForeground(new java.awt.Color(0, 102, 102));
        jlRevueltaId.setText("ID Revuelta XXI:*");
        jlRevueltaId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel21.add(jlRevueltaId);

        moTextRevueltaId.setText("sBeanFieldText1");
        moTextRevueltaId.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel21.add(moTextRevueltaId);

        jPanel16.add(jPanel21);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRevueltaOdbc.setForeground(new java.awt.Color(0, 102, 102));
        jlRevueltaOdbc.setText("ODBC data source:*");
        jlRevueltaOdbc.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel22.add(jlRevueltaOdbc);

        moTextRevueltaOdbc.setText("sBeanFieldText1");
        jPanel22.add(moTextRevueltaOdbc);

        jPanel16.add(jPanel22);

        jPanel20.add(jPanel16, java.awt.BorderLayout.NORTH);

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de mail para notificaciones:"));
        jPanel28.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMailNotificationConfigProtocol.setForeground(new java.awt.Color(0, 102, 102));
        jlMailNotificationConfigProtocol.setText("Protocolo:*");
        jlMailNotificationConfigProtocol.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jlMailNotificationConfigProtocol);

        moTextMailNotificationConfigProtocol.setText("sBeanFieldText1");
        moTextMailNotificationConfigProtocol.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel29.add(moTextMailNotificationConfigProtocol);

        jPanel28.add(jPanel29);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMailNotificationConfigHost.setForeground(new java.awt.Color(0, 102, 102));
        jlMailNotificationConfigHost.setText("Host:*");
        jlMailNotificationConfigHost.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jlMailNotificationConfigHost);

        moTextMailNotificationConfigHost.setText("sBeanFieldText1");
        moTextMailNotificationConfigHost.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel30.add(moTextMailNotificationConfigHost);

        jPanel28.add(jPanel30);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMailNotificationConfigPort.setForeground(new java.awt.Color(0, 102, 102));
        jlMailNotificationConfigPort.setText("Puerto:*");
        jlMailNotificationConfigPort.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jlMailNotificationConfigPort);

        moTextMailNotificationConfigPort.setText("sBeanFieldText1");
        moTextMailNotificationConfigPort.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel31.add(moTextMailNotificationConfigPort);

        jPanel28.add(jPanel31);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMailNotificationConfigUser.setForeground(new java.awt.Color(0, 102, 102));
        jlMailNotificationConfigUser.setText("Usuario:*");
        jlMailNotificationConfigUser.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jlMailNotificationConfigUser);

        moTextMailNotificationConfigUser.setText("sBeanFieldText1");
        moTextMailNotificationConfigUser.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel32.add(moTextMailNotificationConfigUser);

        jPanel28.add(jPanel32);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMailNotificationConfigPassword.setForeground(new java.awt.Color(0, 102, 102));
        jlMailNotificationConfigPassword.setText("Contraseña:*");
        jlMailNotificationConfigPassword.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jlMailNotificationConfigPassword);

        moPswdMailNotificationConfigPassword.setText("sBeanFieldPassword1");
        moPswdMailNotificationConfigPassword.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel33.add(moPswdMailNotificationConfigPassword);

        jPanel28.add(jPanel33);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolMailNotificationConfigStartTls.setText("Comando STARTTLS habilitado");
        moBoolMailNotificationConfigStartTls.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel36.add(moBoolMailNotificationConfigStartTls);

        jPanel28.add(jPanel36);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolMailNotificationConfigAuth.setText("Comando AUTH habilitado");
        moBoolMailNotificationConfigAuth.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel37.add(moBoolMailNotificationConfigAuth);

        jPanel28.add(jPanel37);

        jPanel20.add(jPanel28, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel20);

        jPanel12.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel12, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlCurrencyCode;
    private javax.swing.JLabel jlDivisionDefault;
    private javax.swing.JLabel jlExternalCoId;
    private javax.swing.JLabel jlExternalDatabase;
    private javax.swing.JLabel jlExternalDatabaseCo;
    private javax.swing.JLabel jlExternalHost;
    private javax.swing.JLabel jlExternalPassword;
    private javax.swing.JLabel jlExternalPort;
    private javax.swing.JLabel jlExternalUser;
    private javax.swing.JLabel jlLanguaje;
    private javax.swing.JLabel jlMailNotificationConfigHost;
    private javax.swing.JLabel jlMailNotificationConfigPassword;
    private javax.swing.JLabel jlMailNotificationConfigPort;
    private javax.swing.JLabel jlMailNotificationConfigProtocol;
    private javax.swing.JLabel jlMailNotificationConfigUser;
    private javax.swing.JLabel jlMaximumStockDifferenceKg;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlRevueltaId;
    private javax.swing.JLabel jlRevueltaOdbc;
    private javax.swing.JLabel jlVersion;
    private javax.swing.JLabel jlVersionTs;
    private javax.swing.JLabel jlXtaStockUnit;
    private javax.swing.JTextField jtfVersion;
    private javax.swing.JTextField jtfVersionTs;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolMailNotificationConfigAuth;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolMailNotificationConfigStartTls;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecMaximumStockDifferenceKg;
    private sa.lib.gui.bean.SBeanFieldInteger moIntExternalCoId;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDivisionDefault;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLanguaje;
    private sa.lib.gui.bean.SBeanFieldPassword moPswdExternalPassword;
    private sa.lib.gui.bean.SBeanFieldPassword moPswdMailNotificationConfigPassword;
    private sa.lib.gui.bean.SBeanFieldText moTextCode;
    private sa.lib.gui.bean.SBeanFieldText moTextCurrencyCode;
    private sa.lib.gui.bean.SBeanFieldText moTextExternalDatabase;
    private sa.lib.gui.bean.SBeanFieldText moTextExternalDatabaseCo;
    private sa.lib.gui.bean.SBeanFieldText moTextExternalHost;
    private sa.lib.gui.bean.SBeanFieldText moTextExternalPort;
    private sa.lib.gui.bean.SBeanFieldText moTextExternalUser;
    private sa.lib.gui.bean.SBeanFieldText moTextMailNotificationConfigHost;
    private sa.lib.gui.bean.SBeanFieldText moTextMailNotificationConfigPort;
    private sa.lib.gui.bean.SBeanFieldText moTextMailNotificationConfigProtocol;
    private sa.lib.gui.bean.SBeanFieldText moTextMailNotificationConfigUser;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    private sa.lib.gui.bean.SBeanFieldText moTextRevueltaId;
    private sa.lib.gui.bean.SBeanFieldText moTextRevueltaOdbc;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 800, 500);

        moTextCode.setTextSettings(SGuiUtils.getLabelName(jlCode), 5);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName), 50);
        moTextCurrencyCode.setTextSettings(SGuiUtils.getLabelName(jlCurrencyCode), 5);
        moDecMaximumStockDifferenceKg.setDecimalSettings(SGuiUtils.getLabelName(jlMaximumStockDifferenceKg), SGuiConsts.GUI_TYPE_DEC, false);
        moKeyLanguaje.setKeySettings(miClient, SGuiUtils.getLabelName(jlLanguaje), true);
        moKeyDivisionDefault.setKeySettings(miClient, SGuiUtils.getLabelName(jlDivisionDefault), true);
        moBoolMailNotificationConfigStartTls.setBooleanSettings(SGuiUtils.getLabelName(moBoolMailNotificationConfigStartTls.getText()), false);
        moBoolMailNotificationConfigAuth.setBooleanSettings(SGuiUtils.getLabelName(moBoolMailNotificationConfigAuth.getText()), false);
        moTextExternalHost.setTextSettings(SGuiUtils.getLabelName(jlExternalHost), 50);
        moTextExternalHost.setTextCaseType(SLibConsts.UNDEFINED);
        moTextExternalPort.setTextSettings(SGuiUtils.getLabelName(jlExternalPort), 10);
        moTextExternalPort.setTextCaseType(SLibConsts.UNDEFINED);
        moTextExternalUser.setTextSettings(SGuiUtils.getLabelName(jlExternalUser), 50);
        moTextExternalUser.setTextCaseType(SLibConsts.UNDEFINED);
        moPswdExternalPassword.setTextSettings(SGuiUtils.getLabelName(jlExternalPassword), 50);
        moTextExternalDatabase.setTextSettings(SGuiUtils.getLabelName(jlExternalDatabase), 50);
        moTextExternalDatabase.setTextCaseType(SLibConsts.UNDEFINED);
        moTextExternalDatabaseCo.setTextSettings(SGuiUtils.getLabelName(jlExternalDatabaseCo), 50);
        moTextExternalDatabaseCo.setTextCaseType(SLibConsts.UNDEFINED);
        moTextMailNotificationConfigProtocol.setTextSettings(SGuiUtils.getLabelName(jlMailNotificationConfigProtocol), 50);
        moTextMailNotificationConfigProtocol.setTextCaseType(SLibConsts.UNDEFINED);
        moTextMailNotificationConfigHost.setTextSettings(SGuiUtils.getLabelName(jlMailNotificationConfigHost), 50);
        moTextMailNotificationConfigHost.setTextCaseType(SLibConsts.UNDEFINED);
        moTextMailNotificationConfigPort.setTextSettings(SGuiUtils.getLabelName(jlMailNotificationConfigPort), 50);
        moTextMailNotificationConfigPort.setTextCaseType(SLibConsts.UNDEFINED);
        moTextMailNotificationConfigUser.setTextSettings(SGuiUtils.getLabelName(jlMailNotificationConfigUser), 50);
        moTextMailNotificationConfigUser.setTextCaseType(SLibConsts.UNDEFINED);
        moPswdMailNotificationConfigPassword.setTextSettings(SGuiUtils.getLabelName(jlMailNotificationConfigPassword), 50);
        moIntExternalCoId.setIntegerSettings(SGuiUtils.getLabelName(jlExternalCoId), SGuiConsts.GUI_TYPE_INT_RAW, true);
        moTextRevueltaId.setTextSettings(SGuiUtils.getLabelName(jlRevueltaId), 4);
        moTextRevueltaId.setTextCaseType(SLibConsts.UNDEFINED);
        moTextRevueltaOdbc.setTextSettings(SGuiUtils.getLabelName(jlRevueltaOdbc), 10);
        moTextRevueltaOdbc.setTextCaseType(SLibConsts.UNDEFINED);

        moFields.addField(moTextCode);
        moFields.addField(moTextName);
        moFields.addField(moTextCurrencyCode);
        moFields.addField(moDecMaximumStockDifferenceKg);
        moFields.addField(moKeyLanguaje);
        moFields.addField(moKeyDivisionDefault);
        moFields.addField(moTextExternalHost);
        moFields.addField(moTextExternalPort);
        moFields.addField(moTextExternalUser);
        moFields.addField(moPswdExternalPassword);
        moFields.addField(moTextExternalDatabase);
        moFields.addField(moTextExternalDatabaseCo);
        moFields.addField(moIntExternalCoId);
        moFields.addField(moTextRevueltaId);
        moFields.addField(moTextRevueltaOdbc);
        moFields.addField(moTextMailNotificationConfigProtocol);
        moFields.addField(moTextMailNotificationConfigHost);
        moFields.addField(moTextMailNotificationConfigPort);
        moFields.addField(moTextMailNotificationConfigUser);
        moFields.addField(moPswdMailNotificationConfigPassword);
        moFields.addField(moBoolMailNotificationConfigStartTls);
        moFields.addField(moBoolMailNotificationConfigAuth);

        moFields.setFormButton(jbSave);
        
        jlXtaStockUnit.setText(SSomConsts.KG);
    }

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyLanguaje, SModConsts.CU_LAN, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyDivisionDefault, SModConsts.CU_DIV, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbCompany) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            
            moRegistry.setFkLanguageId(SModSysConsts.CU_LAN_SPA);
            moRegistry.setFkDivisionDefaultId(SModSysConsts.CU_DIV_DEF);
            
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moTextName.setValue(moRegistry.getName());
        moTextCode.setValue(moRegistry.getCode());
        moTextCurrencyCode.setValue(moRegistry.getCurrencyCode());
        moDecMaximumStockDifferenceKg.setValue(moRegistry.getMaximumStockDifferenceKg());
        moKeyLanguaje.setValue(new int[] { moRegistry.getFkLanguageId() });
        moKeyDivisionDefault.setValue(new int[] { moRegistry.getFkDivisionDefaultId() });
        moBoolMailNotificationConfigStartTls.setValue(moRegistry.isMailNotificationConfigStartTls());
        moBoolMailNotificationConfigAuth.setValue(moRegistry.isMailNotificationConfigAuth());
        moTextExternalHost.setValue(moRegistry.getExternalHost());
        moTextExternalPort.setValue(moRegistry.getExternalPort());
        moTextExternalUser.setValue(moRegistry.getExternalUser());
        moPswdExternalPassword.setValue(moRegistry.getExternalPassword());
        moTextExternalDatabase.setValue(moRegistry.getExternalDatabase());
        moTextExternalDatabaseCo.setValue(moRegistry.getExternalDatabaseCo());
        moTextMailNotificationConfigProtocol.setValue(moRegistry.getMailNotificationConfigProtocol());
        moTextMailNotificationConfigHost.setValue(moRegistry.getMailNotificationConfigHost());
        moTextMailNotificationConfigPort.setValue(moRegistry.getMailNotificationConfigPort());
        moTextMailNotificationConfigUser.setValue(moRegistry.getMailNotificationConfigUser());
        moPswdMailNotificationConfigPassword.setValue(moRegistry.getMailNotificationConfigPassword());
        moIntExternalCoId.setValue(moRegistry.getFkExternalCoId_n());
        moTextRevueltaId.setValue(moRegistry.getRevueltaId());
        moTextRevueltaOdbc.setValue(moRegistry.getRevueltaOdbc());

        jtfVersion.setText("" + moRegistry.getVersion());
        jtfVersionTs.setText(moRegistry.getVersionTs() == null ? "" : SLibUtils.DateFormatDatetime.format(moRegistry.getVersionTs()));

        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbCompany registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}

        registry.setName(moTextName.getValue());
        registry.setCode(moTextCode.getValue());
        registry.setMaximumStockDifferenceKg(moDecMaximumStockDifferenceKg.getValue());
        registry.setMailNotificationConfigStartTls(moBoolMailNotificationConfigStartTls.getValue());
        registry.setMailNotificationConfigAuth(moBoolMailNotificationConfigAuth.getValue());
        registry.setExternalHost(moTextExternalHost.getValue());
        registry.setExternalPort(moTextExternalPort.getValue());
        registry.setExternalUser(moTextExternalUser.getValue());
        registry.setExternalPassword(moPswdExternalPassword.getValue());
        registry.setExternalDatabase(moTextExternalDatabase.getValue());
        registry.setExternalDatabaseCo(moTextExternalDatabaseCo.getValue());
        registry.setMailNotificationConfigProtocol(moTextMailNotificationConfigProtocol.getValue());
        registry.setMailNotificationConfigHost(moTextMailNotificationConfigHost.getValue());
        registry.setMailNotificationConfigPort(moTextMailNotificationConfigPort.getValue());
        registry.setMailNotificationConfigUser(moTextMailNotificationConfigUser.getValue());
        registry.setMailNotificationConfigPassword(moPswdMailNotificationConfigPassword.getValue());
        registry.setCurrencyCode(moTextCurrencyCode.getValue());
        registry.setRevueltaId(moTextRevueltaId.getValue());
        registry.setRevueltaOdbc(moTextRevueltaOdbc.getValue());
        //registry.setVersion(0);
        //registry.setVersionTs(null);
        registry.setFkLanguageId(moKeyLanguaje.getValue()[0]);
        registry.setFkDivisionDefaultId(moKeyDivisionDefault.getValue()[0]);
        registry.setFkExternalCoId_n(moIntExternalCoId.getValue());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }
}
