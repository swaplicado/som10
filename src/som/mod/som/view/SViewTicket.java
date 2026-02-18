/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.som.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;
import som.gui.SGuiClientSessionCustom;
import som.gui.prt.SPrtUtils;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbTicket;
import som.mod.som.db.SDbTicketDivisionProcess;
import som.mod.som.db.SSomConsts;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Isabel Servín, Sergio Flores
 */
public class SViewTicket extends SGridPaneView implements ActionListener {

    private SGridFilterDateCutOff moFilterDateCutOff;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SPaneUserInputCategory moPaneFilterUserInputCategory;
    private SPaneFilter moPaneFilterInputCategory;
    private SPaneFilter moPaneFilterItem;
    private SPaneFilter moPaneFilterTicketOrigin;
    private SPaneFilter moPaneFilterTicketDestination;
    private SPaneFilter moPaneFilterScale;
    private JButton mjbPreviousStep;
    private JButton mjbNextStep;
    private JButton mjbLaboratoryTest;
    private JButton mjbExwUpdate;
    private JButton mjbEditByManager;
    private JButton mjbSeasonRegion;
    private JButton mjbPrint;
    private JButton mjbManifest;
    private JButton mjbDivideTicket;
    private JButton mjbDivideTicketRevert;
    
    public SViewTicket(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_TIC, gridSubtype, title);
        setRowButtonsEnabled(true, true, false, false, true);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        boolean hasRightRawMaterials = miClient.getSession().getUser().hasPrivilege(new int[] { SModSysConsts.CS_RIG_RM_STK_SUP, SModSysConsts.CS_RIG_RM_STK_MAN });
        
        mjbDivideTicket = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_doc_div.gif")), "Dividir boleto", this);
        mjbDivideTicketRevert = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_doc_div_red.gif")), "Revertir división de boleto", this);
        mjbManifest = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_print.gif")), "Manifiesto de entrega, transporte y recepción", this);
        mjbPrint = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_print.gif")), SUtilConsts.TXT_PRINT + " boleto", this);
        mjbPreviousStep = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_left.gif")), "Regresar boleto al estado anterior", this);
        mjbNextStep = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_move_right.gif")), "Enviar boleto al estado siguiente", this);
        mjbLaboratoryTest = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_lab.gif")), "Agregar análisis de laboratorio", this);
        mjbExwUpdate = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_edit_alt.jpg")), "Modificar procedencia y destino", this);
        mjbEditByManager = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_tic_edit.gif")), "Modificar boleto", this);
        mjbSeasonRegion = SGridUtils.createButton(new ImageIcon(getClass().getResource("/som/gui/img/icon_std_tic_cfg.gif")), "Cambiar temporada y región", this);

        moPaneFilterUserInputCategory = new SPaneUserInputCategory(miClient, SModConsts.S_TIC, "itm");
        moPaneFilterInputCategory = new SPaneFilter(this, SModConsts.SU_INP_CT);
        moPaneFilterItem = new SPaneFilter(this, SModConsts.SU_ITEM);
        moPaneFilterItem.initFilter(null);
        moPaneFilterTicketOrigin = new SPaneFilter(this, SModConsts.SU_TIC_ORIG);
        moPaneFilterTicketOrigin.initFilter(null);
        moPaneFilterTicketDestination = new SPaneFilter(this, SModConsts.SU_TIC_DEST);
        moPaneFilterTicketDestination.initFilter(null);
        moPaneFilterScale = new SPaneFilter(this, SModConsts.SU_SCA);
        moPaneFilterScale.initFilter(null);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbDivideTicket);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbDivideTicketRevert);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbManifest);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbPrint);
        
        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
            case SModSysConsts.SS_TIC_ST_LAB:
                moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
                moFilterDateCutOff.initFilter(null);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
                break;
                
            case SModSysConsts.SS_TIC_ST_ADM:
            case SModSysConsts.SS_TIC_ST_ALL_LOG:
            case SLibConsts.UNDEFINED:
                moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
                moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbPreviousStep);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbNextStep);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbLaboratoryTest);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbExwUpdate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbEditByManager);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbSeasonRegion);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterUserInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterInputCategory);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterItem);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketOrigin);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterTicketDestination);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneFilterScale);

        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
                mjbDivideTicket.setEnabled(true);
                mjbDivideTicketRevert.setEnabled(true);
                mjbManifest.setEnabled(false);
                mjbPrint.setEnabled(true);
                
                mjbPreviousStep.setEnabled(false);
                mjbNextStep.setEnabled(true);
                mjbLaboratoryTest.setEnabled(false);
                mjbExwUpdate.setEnabled(hasRightRawMaterials);
                mjbEditByManager.setEnabled(false);
                mjbSeasonRegion.setEnabled(false);
                break;
                
            case SModSysConsts.SS_TIC_ST_LAB:
                mjbDivideTicket.setEnabled(false);
                mjbDivideTicketRevert.setEnabled(false);
                mjbManifest.setEnabled(false);
                mjbPrint.setEnabled(true);
                
                mjbPreviousStep.setEnabled(true);
                mjbNextStep.setEnabled(true);
                mjbLaboratoryTest.setEnabled(true);
                mjbExwUpdate.setEnabled(hasRightRawMaterials);
                mjbEditByManager.setEnabled(false);
                mjbSeasonRegion.setEnabled(false);
                
                setRowButtonsEnabled(false);
                break;
                
            case SModSysConsts.SS_TIC_ST_ADM:
                mjbDivideTicket.setEnabled(false);
                mjbDivideTicketRevert.setEnabled(false);
                mjbManifest.setEnabled(false);
                mjbPrint.setEnabled(true);
                
                mjbPreviousStep.setEnabled(true);
                mjbNextStep.setEnabled(false);
                mjbLaboratoryTest.setEnabled(false);
                mjbExwUpdate.setEnabled(hasRightRawMaterials);
                mjbEditByManager.setEnabled(true);
                mjbSeasonRegion.setEnabled(true);
                
                setRowButtonsEnabled(false);
                break;
                
            case SModSysConsts.SS_TIC_ST_ALL_LOG:
                mjbDivideTicket.setEnabled(false);
                mjbDivideTicketRevert.setEnabled(false);
                mjbManifest.setEnabled(true);
                mjbPrint.setEnabled(true);
                
                mjbPreviousStep.setEnabled(false);
                mjbNextStep.setEnabled(false);
                mjbLaboratoryTest.setEnabled(false);
                mjbExwUpdate.setEnabled(hasRightRawMaterials);
                mjbEditByManager.setEnabled(false);
                mjbSeasonRegion.setEnabled(false);
                
                setRowButtonsEnabled(false);
                break;
                
            case SLibConsts.UNDEFINED:
                mjbDivideTicket.setEnabled(false);
                mjbDivideTicketRevert.setEnabled(false);
                mjbManifest.setEnabled(false);
                mjbPrint.setEnabled(true);
                
                mjbPreviousStep.setEnabled(false);
                mjbNextStep.setEnabled(false);
                mjbLaboratoryTest.setEnabled(false);
                mjbExwUpdate.setEnabled(hasRightRawMaterials);
                mjbEditByManager.setEnabled(false);
                mjbSeasonRegion.setEnabled(false);
                
                setRowButtonsEnabled(false);
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    private void actionPreviousStep() {
        if (mjbPreviousStep.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    SDbTicket ticket = (SDbTicket) miClient.getSession().readRegistry(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    if (miClient.showMsgBoxConfirm("¿Desea regresar el boleto al estado anterior?") == JOptionPane.YES_OPTION) {
                        try {
                            ticket.movePrevious(miClient.getSession());

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN_SUP);
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }
            }
        }
    }

    private void actionNextStep() {
        if (mjbNextStep.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    SDbTicket ticket = (SDbTicket) miClient.getSession().readRegistry(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    if (miClient.showMsgBoxConfirm("¿Desea enviar el boleto al estado siguiente?") == JOptionPane.YES_OPTION) {
                        try {
                            ticket.moveNext(miClient.getSession());

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN);
                            miClient.getSession().notifySuscriptors(SModConsts.SX_TIC_MAN_SUP);
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }
            }
        }
    }

    private void actionLaboratoryTest() {
        if (mjbLaboratoryTest.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    SGuiParams params = new SGuiParams(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    miClient.getSession().getModule(SModConsts.MOD_SOM_RM, SLibConsts.UNDEFINED).showForm(SModConsts.SX_TIC_LAB, SLibConsts.UNDEFINED, params);
                }
            }
        }
    }

    private void actionExwUpdate() {
        if (mjbExwUpdate.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    SGuiParams params = new SGuiParams(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    miClient.getSession().getModule(SModConsts.MOD_SOM_RM, SLibConsts.UNDEFINED).showForm(SModConsts.S_TIC_EXW_UPD_LOG, SLibConsts.UNDEFINED, params);
                }
            }
        }
    }

    private void actionEditByManager() {
        if (mjbEditByManager.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    SDbTicket ticket = (SDbTicket) miClient.getSession().readRegistry(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    if (ticket.isLaboratory()) {
                        SGuiParams params = new SGuiParams(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                        miClient.getSession().getModule(SModConsts.MOD_SOM_RM, SLibConsts.UNDEFINED).showForm(SModConsts.SX_TIC_MAN, SLibConsts.UNDEFINED, params);
                    }
                    else {
                        miClient.showMsgBoxInformation("El boleto '" + ticket.getNumber() + "' no puede ser modificado por gerencia, porque no requiere análisis de laboratorio.");
                    }
                }
            }
        }
    }

    private void actionSeasonRegion() {
        if (mjbEditByManager.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    SDbTicket ticket = (SDbTicket) miClient.getSession().readRegistry(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                    if (ticket.isLaboratory()) {
                        SGuiParams params = new SGuiParams(SModConsts.S_TIC, gridRow.getRowPrimaryKey());

                        miClient.getSession().getModule(SModConsts.MOD_SOM_RM, SLibConsts.UNDEFINED).showForm(SModConsts.SX_TIC_SEAS_REG, SLibConsts.UNDEFINED, params);
                    }
                    else {
                        miClient.showMsgBoxInformation("El boleto '" + ticket.getNumber() + "' no necesita configurar temporada ni región.");
                    }
                }
            }
        }
    }

    private void actionPrint() {
        if (mjbPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                
                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    HashMap<String, Object> map = SPrtUtils.createReportParamsMap(miClient.getSession());
                    DecimalFormat oformatDecimal = SLibUtils.RoundingDecimalFormat;

                    oformatDecimal.setMaximumFractionDigits(SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits());

                    map.put("nTicketId", gridRow.getRowPrimaryKey()[0]);
                    map.put("sCurrencyCode", ((SGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getLocalCurrencyCode());
                    map.put("bShowMoney", miClient.getSession().getUser().hasPrivilege(SModSysConsts.CS_RIG_MAN_RM));
                    map.put("oFormatDecimal", oformatDecimal);

                    miClient.getSession().printReport(SModConsts.SR_TIC, SLibConsts.UNDEFINED, null, map);
                }
            }
        }
    }
    
    private void actionManifest() {
        if (mjbManifest.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                
                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    HashMap<String, Object> map = SPrtUtils.createReportParamsMap(miClient.getSession());
                    map.put("nTicketId", gridRow.getRowPrimaryKey()[0]);

                    miClient.getSession().printReport(SModConsts.SR_TIC_METRRME, SLibConsts.UNDEFINED, null, map);
                }
            }
        }
    }
    
    @Override
    public void actionRowDelete(){
        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
            }
            else if (miClient.showMsgBoxConfirm(SGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                boolean updates = false;
                SGridRow[] gridRows = getSelectedGridRows();

                for (SGridRow gridRow : gridRows) {
                    if (((SGridRowView) gridRow).getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else if (((SGridRowView) gridRow).isRowSystem()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!((SGridRowView) gridRow).isDeletable()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_DELETABLE);
                    }
                    else {
                        try { 
                            SDbTicket ticket = new SDbTicket();
                            ticket.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                            if (!SSomUtils.isFromDividedTicket(miClient.getSession(), ticket.getPkTicketId())) {
                                if (!SSomUtils.isDividedTicket(miClient.getSession(), ticket.getPkTicketId())) {
                                    String depTic = SSomUtils.isFreightDependentTicket(miClient.getSession(), ticket.getPkTicketId());
                                    if (depTic.isEmpty()) {
                                        if (!ticket.isAlternative()) {
                                            ticket.delete(miClient.getSession());
                                            updates = true;
                                        }
                                        else {
                                            miClient.showMsgBoxInformation("El boleto no se puede eliminar debido a que ya tiene un registro en el sistema de SOM Orgánico con el folio " + ticket.getXtaNumAlternative() + ".\n"
                                            + "Es necesario eliminar el registro.");
                                        }
                                    }
                                    else {
                                        miClient.showMsgBoxInformation("El boleto no se puede eliminar debido a que los siguientes boletos son dependientes de flete:\n"
                                                + depTic + ".");
                                    }
                                }
                                else {
                                    miClient.showMsgBoxInformation("El boleto no se puede eliminar debido a que fue dividido en otros boletos.");
                                }
                            }
                            else {
                                miClient.showMsgBoxInformation("El boleto no se puede eliminar debido a que viene de otro boleto dividido.");
                            }
                        }
                        catch (Exception e) {}
                    }
                }

                if (updates) {
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
    }
    
    private void actionDivideTicket() {
        if (mjbDivideTicket.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
            }
            else {
                SGridRow gridRow = getSelectedGridRow();

                if (((SGridRowView) gridRow).getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (((SGridRowView) gridRow).isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!((SGridRowView) gridRow).isDeletable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_DELETABLE);
                }
                else {
                    try { 
                        SDbTicket tic = new SDbTicket();
                        tic.read(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                        if (!SSomUtils.isFromDividedTicket(miClient.getSession(), tic.getPkTicketId())) {
                            if (!tic.isPacking()) {
                                if (tic.isTared()) {
                                    if (!tic.isDeleted()) {
                                        if (!tic.isAlternative()) {
                                            SGuiParams params = new SGuiParams();
                                            params.setKey(getSelectedGridRow().getRowPrimaryKey());
                                            miClient.getSession().getModule(SModConsts.MOD_SOM_RM).showForm(SModConsts.SX_TIC_DIV_PROC, SLibConsts.UNDEFINED, params);
                                            miClient.getSession().notifySuscriptors(mnGridType);
                                        }
                                        else {
                                            miClient.showMsgBoxInformation("El boleto no se puede dividir debido a que ya tiene un registro en el sistema de SOM Orgánico con el folio " + tic.getXtaNumAlternative() + ".\n"
                                            + "Es necesario eliminar el registro.");
                                        }
                                    }
                                    else {
                                        miClient.showMsgBoxInformation("No se puede dividir el boleto debido está eliminado.");
                                    }
                                }
                                else {
                                    miClient.showMsgBoxInformation("No se puede dividir el boleto debido a que no está tarado.");
                                }
                            }
                            else {
                                miClient.showMsgBoxInformation("No se puede dividir el boleto debido a que requiere empaques.");
                            }
                        }
                        else {
                            miClient.showMsgBoxInformation("No se puede dividir el boleto debido a que ya viene de otro boleto dividido.");
                        }
                    }
                    catch (Exception e) {}
                }
            }
        }
    }
    
    private void actionDivideTicketRevert() {
        if (mjbDivideTicketRevert.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
            }
            else {
                SGridRow gridRow = getSelectedGridRow();

                if (((SGridRowView) gridRow).getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (((SGridRowView) gridRow).isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!((SGridRowView) gridRow).isDeletable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_DELETABLE);
                }
                else {
                    try { 
                        if (SSomUtils.isFromDividedTicket(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey()[0])) {
                            if (miClient.showMsgBoxConfirm("Se eliminarán todos los boletos resultado de la división para regresar al boleto original.\n"
                                    + "¿Desea continuar?") == JOptionPane.OK_OPTION) {
                                SDbTicketDivisionProcess ticDivProc = new SDbTicketDivisionProcess();
                                ticDivProc.readByTicketNew(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                                ticDivProc.delete(miClient.getSession());
                                miClient.getSession().notifySuscriptors(mnGridType);
                            }
                        }
                        else {
                            miClient.showMsgBoxInformation("No se puede revertir la división del registro debido a que el boleto no viene de otro boleto dividido.");
                        }
                    }
                    catch (Exception e) {}
                }
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        Object filter;
        String sqlWhere = "";

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
            case SModSysConsts.SS_TIC_ST_LAB:
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE);
                if (filter != null) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + " v.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
                }
                break;
                
            case SModSysConsts.SS_TIC_ST_ADM:
            case SModSysConsts.SS_TIC_ST_ALL_LOG:
            case SLibConsts.UNDEFINED:
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD);
                if (filter != null) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                }
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
                
        filter = (int[]) moFiltersMap.get(SModConsts.SU_INP_CT);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "itm.fk_inp_ct" }, (int[]) filter);
        }

        filter = (int[]) moFiltersMap.get(SModConsts.SU_ITEM);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_item" }, (int[]) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_ORIG);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_tic_orig" }, (int[]) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_TIC_DEST);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_tic_dest" }, (int[]) filter);
        }
        
        filter = (int[]) moFiltersMap.get(SModConsts.SU_SCA);
        if (filter != null) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SGridUtils.getSqlFilterKey(new String[] { "v.fk_sca" }, (int[]) filter);
        }
        
        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "v.fk_tic_st = " + mnGridSubtype + " ";
                break;
            case SModSysConsts.SS_TIC_ST_LAB:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "v.fk_tic_st = " + mnGridSubtype + " ";
                break;
            case SModSysConsts.SS_TIC_ST_ADM:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "v.fk_tic_st = " + mnGridSubtype + " ";
                break;
            case SModSysConsts.SS_TIC_ST_ALL_LOG:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "prd.b_log ";
                break;
            case SLibConsts.UNDEFINED:
                break;
            default:
        }
        
        String sqlFilter = moPaneFilterUserInputCategory.getSqlFilter();
        if (!sqlFilter.isEmpty()) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + sqlFilter;
        }
        
        msSql = "SELECT "
                + "v.id_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.num, "
                + "v.dt, "
                + "v.qty, " // seemingly unused
                + "v.pla, "
                + "v.pla_cag, "
                + "v.drv, "
                + "v.ts_arr, "
                + "v.ts_dep, "
                + "v.pac_qty_arr, "     // Quantity of full packing pieces at arrival
                + "v.pac_qty_dep, "     // Quantity of full packing pieces at departure
                + "v.pac_emp_qty_arr, " // Quantity of empty packing pieces at arrival
                + "v.pac_emp_qty_dep, " // Quantity of empty packing pieces at departure
                + "v.pac_wei_arr, "     // pwa: Weight of packing at arrival (full and empty?)
                + "v.pac_wei_dep, "     // pwd: Weight of packing at departure (full and empty?)
                + "v.pac_wei_net_r, "   // pwn: Weight of packing net (= pwa – pwd)
                + "v.wei_src, "         // Declared weight at origin
                + "v.wei_des_arr, "     // wda: Weigth at destiny at arrival
                + "v.wei_des_dep, "     // wdd: Weigth at destiny at departure
                + "v.wei_des_gro_r, "   // wdg: Weigth gross at destiny (= wda – wdd)
                + "v.wei_des_net_r, "   // wdn: Weigth net at destiny (= wdg – pwn)
                + "v.sys_pen_per, "
                + "v.sys_wei_pay, "
                + "v.sys_prc_ton, "
                + "v.sys_pay_r, "
                + "v.sys_fre, "
                + "v.sys_tot_r, "
                + "v.usr_pen_per, "
                + "v.usr_wei_pay, "
                + "v.usr_prc_ton, "
                + "v.usr_pay_r, "
                + "v.usr_fre, "
                + "v.usr_tot_r, "
                + "v.dps_dt_n, "
                + "CASE WHEN v.freight_tic_tp = '" + SDbTicket.FRT_TIC_TP_FRT + "' THEN '" + SDbTicket.FRT_TIC_TP_FRT_DESC + "' WHEN v.freight_tic_tp = '" + SDbTicket.FRT_TIC_TP_DEP + "' THEN '" + SDbTicket.FRT_TIC_TP_DEP_DESC + "' ELSE '' END AS _frt_tic_tp, "
                + "v.b_rev_1, "
                + "v.b_rev_2, "
                + "v.b_wei_src, "
                + "v.b_mfg_out, "
                + "v.b_tar, "
                + "v.b_pay, "
                + "v.b_ass, "
                + "v.b_paq, "
                + "v.b_lab, "
                + "v.b_dps, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "sca.id_sca, "
                + "sca.code, "
                + "sca.name, "
                + "tst.id_tic_st, "
                + "tst.code, "
                + "tst.name, "
                + "itm.id_item, "
                + "itm.code, "
                + "itm.name, "
                + "prd.id_prod, "
                + "prd.code, "
                + "prd.name, "
                + "prd.name_trd, "
                + "src.id_inp_src, "
                + "src.code, "
                + "src.name, "
                + "sea.id_seas, "
                + "sea.name, "
                + "reg.id_reg, "
                + "reg.name, "
                + "tor.code, "
                + "tde.code, "
                + "efor.code, "
                + "efde.code, "
                + "lab.num, "
                + "lab.dt, "
                + "wah.id_co, "
                + "wah.id_cob, "
                + "wah.id_wah, "
                + "wah.code, "
                + "wah.name, "
                + "fror.name, "
                + "frtic.num, "
                + "if (lab.b_done, " + SGridConsts.ICON_OK + ", " + SGridConsts.ICON_NULL + ") AS _lab_done, "
                + "COALESCE(seareg.prc_ton, 0.0) AS _prc_ton_reg, "
                + "COALESCE(seaprd.prc_ton, 0.0) AS _prc_ton_prd, "
                + "(v.pac_qty_arr * itm.paq_wei) AS _pac_wei_arr, "
                + "(v.pac_emp_qty_arr * itm.paq_wei) AS _pac_emp_wei_arr, "
                + "(v.pac_qty_dep * itm.paq_wei) AS _pac_wei_dep, "
                + "(v.pac_emp_qty_dep * itm.paq_wei) AS _pac_emp_wei_dep, "
                + "IF(v.wei_des_dep = 0, 0, IF(itm.b_paq = 0, 0, "
                + "(((v.wei_des_arr - v.wei_des_dep) - "
                + "((v.pac_qty_arr + v.pac_emp_qty_arr) * itm.paq_wei) + "
                + "((v.pac_qty_dep + v.pac_emp_qty_dep) * itm.paq_wei)) /"
                + "(v.pac_qty_arr)))) AS _wei_ave, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sca ON "
                + "v.fk_sca = sca.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_TIC_ST) + " AS tst ON "
                + "v.fk_tic_st = tst.id_tic_st "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS itm ON "
                + "v.fk_item = itm.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS prd ON "
                + "v.fk_prod = prd.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_SRC) + " AS src ON "
                + "v.fk_inp_src = src.id_inp_src "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_ORIG) + " AS tor ON "
                + "v.fk_tic_orig = tor.id_tic_orig "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_TIC_DEST) + " AS tde ON "
                + "v.fk_tic_dest = tde.id_tic_dest "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EXW_FAC) + " AS efor ON "
                + "v.fk_exw_fac_orig = efor.id_exw_fac "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MU_EXW_FAC) + " AS efde ON "
                + "v.fk_exw_fac_dest = efde.id_exw_fac "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS sea ON "
                + "v.fk_seas_n = sea.id_seas "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS reg ON "
                + "v.fk_reg_n = reg.id_reg "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_LAB) + " AS lab ON "
                + "v.fk_lab_n = lab.id_lab AND lab.b_del = 0 "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_REG) + " AS seareg ON "
                + "v.fk_seas_n = seareg.id_seas AND v.fk_reg_n = seareg.id_reg AND v.fk_item = seareg.id_item "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS_PROD) + " AS seaprd ON "
                + "v.fk_seas_n = seaprd.id_seas AND v.fk_reg_n = seaprd.id_reg AND v.fk_item = seaprd.id_item AND v.fk_prod = seaprd.id_prod "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_WAH) + " AS wah ON " 
                + "v.fk_wah_unld_co_n = wah.id_co AND v.fk_wah_unld_cob_n = wah.id_cob AND v.fk_wah_unld_wah_n = wah.id_wah "                
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_FREIGHT_ORIG) + " AS fror ON " 
                + "v.fk_freight_orig_n = fror.id_freight_orig "                
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " AS frtic ON " 
                + "v.fk_freight_tic_n = frtic.id_tic "                
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + "ORDER BY sca.code, sca.id_sca, v.num, v.id_tic ";
    }

    @Override
    public void createGridColumns() {
        int cols = 50;

        switch (mnGridSubtype) {
            case SModSysConsts.SS_TIC_ST_SCA:
                break;
            case SModSysConsts.SS_TIC_ST_LAB:
                cols += 3;
                break;
            case SModSysConsts.SS_TIC_ST_ADM:
                cols += 15;
                break;
            case SLibConsts.UNDEFINED:
                cols += 1;
                break;
            default:
        }

        int col = 0;
        SGridColumnView[] columns = new SGridColumnView[cols];
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca.code", "Báscula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.num", "Boleto", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt", SGridConsts.COL_TITLE_DATE + " boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "prd.name", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "prd.name_trd", "Proveedor nombre comercial");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "prd.code", "Proveedor código");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "itm.name", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "itm.code", "Ítem código");

        if (mnGridSubtype == SLibConsts.UNDEFINED) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tst.name", "Estatus boleto");
        }

        if (mnGridSubtype == SModSysConsts.SS_TIC_ST_LAB) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "lab.num", "Folio análisis lab");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE,  "lab.dt", SGridConsts.COL_TITLE_DATE + " análisis lab");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_lab_done", "Análisis lab terminado");
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sea.name", "Temporada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "reg.name", "Región");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tor.code", "Procedencia boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "efor.code", "Almacén procedencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tde.code", "Destino boleto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "efde.code", "Almacén destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "src.name", "Origen insumo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla", "Placas");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pla_cag", "Placas caja");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.drv", "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_arr", "TS entrada");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_dep", "TS salida");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_tar", "Tarado");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_src", "Peso origen (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_arr", "Peso entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_dep", "Peso salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_qty_arr", "Núm envases llenos entrada (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_emp_qty_arr", "Núm envases vacíos entrada (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_qty_dep", "Núm envases llenos salida (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_emp_qty_dep", "Núm envases vacíos salida (" + SSomConsts.PIECE + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_wei_arr", "Peso envases llenos entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_emp_wei_arr", "Peso envases vacíos entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_wei_arr", "Peso total envases entrada (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_wei_dep", "Peso envases llenos salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_pac_emp_wei_dep", "Peso envases vacíos salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pac_wei_dep", "Peso total envases salida (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_wei_ave", "Peso promedio (" + SSomConsts.KG + "/" + SSomConsts.PIECE + ")");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_gro_r", "Carga destino bruto (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.wei_des_net_r", "Carga destino neta (" + SSomConsts.KG + ")");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_rev_1", "1a pesada Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_rev_2", "2a pesada Revuelta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "wah.name", "Almacén descarga");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "wah.code", "Almacén descarga código");

        if (mnGridSubtype == SModSysConsts.SS_TIC_ST_ADM) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_prc_ton_reg", "Precio ton reg $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_prc_ton_prd", "Precio ton prod $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.sys_pen_per", "Castigo sis");
            columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.sys_wei_pay", "Peso pago sis (" + SSomConsts.KG + ")");
            columns[col++].setSumApplying(true);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sys_prc_ton", "Precio sis $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sys_pay_r", "Pago sis $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sys_fre", "Flete sis $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sys_tot_r", "Total sis $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.usr_pen_per", "Castigo usr");
            columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.usr_wei_pay", "Peso pago usr (" + SSomConsts.KG + ")");
            columns[col++].setSumApplying(true);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.usr_prc_ton", "Precio usr $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.usr_pay_r", "Pago usr $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.usr_fre", "Flete usr $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.usr_tot_r", "Total usr $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_pay", "Pagado");
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_frt_tic_tp", "Control fletes");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fror.name", "Origen flete");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "frtic.num", "Boleto flete", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);

        moModel.getGridColumns().addAll(Arrays.asList(columns));
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.SU_SCA);
        moSuscriptionsSet.add(SModConsts.SU_ITEM);
        moSuscriptionsSet.add(SModConsts.SU_PROD);
        moSuscriptionsSet.add(SModConsts.SU_INP_SRC);
        moSuscriptionsSet.add(SModConsts.SU_SEAS);
        moSuscriptionsSet.add(SModConsts.SU_REG);
        moSuscriptionsSet.add(SModConsts.S_LAB);
        moSuscriptionsSet.add(SModConsts.S_TIC_EXW_UPD_LOG);
        moSuscriptionsSet.add(SModConsts.SX_TIC_TARE);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }

    @Override
    public void actionMouseClicked() {
        if (mnGridSubtype == SModSysConsts.SS_TIC_ST_LAB) {
            actionLaboratoryTest();
        }
        else if (mnGridSubtype == SModSysConsts.SS_TIC_ST_ADM) {
            actionEditByManager();
        }
        else {
            super.actionMouseClicked();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbDivideTicket) {
                actionDivideTicket();
            }
            else if (button == mjbDivideTicketRevert) {
                actionDivideTicketRevert();
            }
            else if (button == mjbManifest) {
                actionManifest();
            }
            else if (button == mjbPrint) {
                actionPrint();
            }
            else if (button == mjbPreviousStep) {
                actionPreviousStep();
            }
            else if (button == mjbNextStep) {
                actionNextStep();
            }
            else if (button == mjbLaboratoryTest) {
                actionLaboratoryTest();
            }
            else if (button == mjbExwUpdate) {
                actionExwUpdate();
            }
            else if (button == mjbEditByManager) {
                actionEditByManager();
            }
            else if (button == mjbSeasonRegion) {
                actionSeasonRegion();
            }
        }
    }
}
