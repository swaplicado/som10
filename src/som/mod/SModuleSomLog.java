/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiForm;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiReport;
import som.mod.som.db.SDbProducer;
import som.mod.som.db.SDbVehicleContainerType;
import som.mod.som.form.SFormProducerLog;
import som.mod.som.form.SFormVehicleContainerType;
import som.mod.som.view.SViewProducerLog;
import som.mod.som.view.SViewTicket;
import som.mod.som.view.SViewVehicleContainerType;

/**
 *
 * @author Isabel Servín
 */
public class SModuleSomLog extends SGuiModule implements ActionListener {

    private JMenu mjCat;   // Catalogues
    private JMenuItem mjCatProducerLog;
    private JMenuItem mjCatVehicleContainerType;
    private JMenu mjTic;   // Tickets
    private JMenuItem mjTicTicketAllLog;
    
    private SFormProducerLog moFormProducerLog;
    private SFormVehicleContainerType moFormVehicleContainerType;
    
    public SModuleSomLog(SGuiClient client) {
        super(client, SModConsts.MOD_SOM_LOG, SLibConsts.UNDEFINED);
        initComponents();
    }
    
    /*
    * Private methods
    */

    private void initComponents() {
        mjCat = new JMenu("Catálogos");
        mjCatProducerLog = new JMenuItem("Proveedores logística");
        mjCatVehicleContainerType = new JMenuItem("Tipo de vehículo y contenedor");
        
        mjCat.add(mjCatProducerLog);
        mjCat.add(mjCatVehicleContainerType);
        
        mjCatProducerLog.addActionListener(this);
        mjCatVehicleContainerType.addActionListener(this);
        
        mjTic = new JMenu("Boletos báscula");
        mjTicTicketAllLog = new JMenuItem("Boletos (todos logística)");
        
        mjTic.add(mjTicTicketAllLog);
        
        mjTicTicketAllLog.addActionListener(this);
    }
    
    /* 
    * Public methods
    */
    
    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjTic };
    }

    @Override
    public SDbRegistry getRegistry(int type, SGuiParams params) {
        SDbRegistry registry = null;
        
        switch (type) {
            case SModConsts.SX_PROD_LOG:
                registry = new SDbProducer();
                break;
            case SModConsts.SU_VEH_CONT_TYPE:
                registry = new SDbVehicleContainerType();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(int type, int subtype, SGuiParams params) {
        String sql = "";
        SGuiCatalogueSettings settings = null;
        
        switch (type) {
            case SModConsts.SU_VEH_CONT_TYPE:
                settings = new SGuiCatalogueSettings("Tipo de vehiculo contenedor", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_veh_cont_type AS " + SDbConsts.FIELD_ID + "1, CONCAT(vehicle_type, ' - ', container_type) AS " + SDbConsts.FIELD_ITEM + ", code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND b_dis = 0 ORDER BY vehicle_type, container_type, id_veh_cont_type ";
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        if (settings != null) {
            settings.setSql(sql);

            if (params != null && params.getParamsMap().get(SGuiConsts.PARAM_ITEM) != null) {
                settings.setSelectionItemApplying((Boolean) params.getParamsMap().get(SGuiConsts.PARAM_ITEM));
            }
        }
        
        return settings;
    }

    @Override
    public SGridPaneView getView(int type, int subtype, SGuiParams params) {
        SGridPaneView view = null;
        
        switch (type) {
            case SModConsts.SX_PROD_LOG:
                view = new SViewProducerLog(miClient, "Proveedores logística");
                break;
            case SModConsts.SU_VEH_CONT_TYPE:
                view = new SViewVehicleContainerType(miClient, "Tipo vehículo contenedor");
                break;
            case SModConsts.S_TIC:
                switch (subtype) {
                    case SModSysConsts.SS_TIC_ST_ALL_LOG:
                        view = new SViewTicket(miClient, subtype, "Boletos (todos logística)");
                        break;
                    case SLibConsts.UNDEFINED:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(int i, int i1, SGuiParams sgp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiForm getForm(int type, int subtype, SGuiParams params) {
        SGuiForm form = null;
        
        switch (type) {
            case SModConsts.SX_PROD_LOG:
                if (moFormProducerLog == null) moFormProducerLog = new SFormProducerLog(miClient, "Proveedor");
                form = moFormProducerLog;
                break;
            case SModConsts.SU_VEH_CONT_TYPE:
                if (moFormVehicleContainerType == null) moFormVehicleContainerType = new SFormVehicleContainerType(miClient, "Tipo de vehículo contenedor");
                form = moFormVehicleContainerType;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return form;
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        SGuiReport guiReport = null;
        
        switch (type) {
            case SModConsts.SR_TIC_METRRME:
                guiReport = new SGuiReport("reps/s_tic_metrrme.jasper", "Manifiesto de entrega, transporte y recepcion de residuos de manejo especial");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return guiReport;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();
            
            if (menuItem == mjCatProducerLog) {
                miClient.getSession().showView(SModConsts.SX_PROD_LOG, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatVehicleContainerType) {
                miClient.getSession().showView(SModConsts.SU_VEH_CONT_TYPE, SLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjTicTicketAllLog) {
                showView(SModConsts.S_TIC, SModSysConsts.SS_TIC_ST_ALL_LOG, null);
            }
        }
    }
}
