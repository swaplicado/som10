/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package som.mod.som.db;

import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.SModSysConsts;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores, Isabel Servín
 * 2019-01-07, Sergio Flores: Mostrar solo proveedores con movimientos en sección período actual, en notificación automática de recepción de boletos.
 * 2019-01-17, Sergio Flores: Mejoras reporte automático vía mail al tarar boletos:
 *   a) remoción de proveedores sin movimientos en todas las secciones. Antes aparecían todos.
 *   b) implementación de métodos privados para concentrar la generación de secciones de tablas del reporte.
 */
public class SDbTicket extends SDbRegistryUser implements SGridRow {

    public static final int FIELD_PAYED = SDbRegistry.FIELD_BASE + 1;
    public static final int FIELD_TICKET_STATUS = SDbRegistry.FIELD_BASE + 2;
    public static final int FIELD_LABORATORY = SDbRegistry.FIELD_BASE + 3;
    public static final int FIELD_TARED = SDbRegistry.FIELD_BASE + 4;
    public static final int FIELD_ASSORTED = SDbRegistry.FIELD_BASE + 5;
    public static final int FIELD_DPS = SDbRegistry.FIELD_BASE + 6;
    public static final int FIELD_DPS_NULL = SDbRegistry.FIELD_BASE + 7;
    
    private static final int MAX_LEN_PROD = 20;
    private static final int MAX_LEN_NAME = 25;
    private static final int MAX_SEARCH_TICKETS = 100;

    protected int mnPkTicketId;
    protected int mnNumber;
    protected Date mtDate;
    protected double mdQuantity;
    protected String msPlate;
    protected String msPlateCage;
    protected String msDriver;
    protected Date mtDatetimeArrival;
    protected Date mtDatetimeDeparture;
    protected double mdPackingFullQuantityArrival;
    protected double mdPackingFullQuantityDeparture;
    protected double mdPackingEmptyQuantityArrival;
    protected double mdPackingEmptyQuantityDeparture;
    protected double mdPackingWeightArrival;
    protected double mdPackingWeightDeparture;
    protected double mdPackingWeightNet_r;
    protected double mdWeightSource;
    protected double mdWeightDestinyArrival;
    protected double mdWeightDestinyDeparture;
    protected double mdWeightDestinyGross_r;
    protected double mdWeightDestinyNet_r;
    protected double mdSystemPenaltyPercentage;
    protected double mdSystemWeightPayment;
    protected double mdSystemPricePerTon;
    protected double mdSystemPayment_r;
    protected double mdSystemFreight;
    protected double mdSystemTotal_r;
    protected double mdUserPenaltyPercentage;
    protected double mdUserWeightPayment;
    protected double mdUserPricePerTon;
    protected double mdUserPayment_r;
    protected double mdUserFreight;
    protected double mdUserTotal_r;
    protected Date mtDpsSupplyDate_n;
    protected boolean mbRevueltaImport1;
    protected boolean mbRevueltaImport2;
    protected boolean mbWeightSourceAvailable;
    protected boolean mbMfgOutsourcing;
    protected boolean mbTared;
    protected boolean mbPayed;
    protected boolean mbAssorted;
    protected boolean mbPacking;
    protected boolean mbLaboratory;
    protected boolean mbWarehouseUnloadRequired;
    protected boolean mbDpsSupply;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkScaleId;
    protected int mnFkTicketStatusId;
    protected int mnFkSeasonId_n;
    protected int mnFkRegionId_n;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkProducerId;
    protected int mnFkInputSourceId;
    protected int mnFkLaboratoryId_n;
    protected int mnFkWarehouseUnloadCompanyId_n;
    protected int mnFkWarehouseUnloadBranchId_n;
    protected int mnFkWarehouseUnloadWarehouseId_n;
    protected int mnFkExternalDpsYearId_n;
    protected int mnFkExternalDpsDocId_n;
    protected int mnFkExternalDpsEntryId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected int mnFkUserTaredId;
    protected int mnFkUserPayedId;
    protected int mnFkUserAssortedId;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    protected Date mtTsUserTared;
    protected Date mtTsUserPayed;
    protected Date mtTsUserAssorted;

    protected boolean mbOldTared;
    protected boolean mbOldPayed;
    protected boolean mbOldAssorted;
    
    protected String msXtaScaleName;
    protected String msXtaScaleCode;
    protected String msXtaSeason;
    protected String msXtaRegion;
    protected String msXtaItem;
    protected int mnXtaInputCategoryId;
    protected int mnXtaInputClassId;
    protected String msXtaInputType;
    protected String msXtaInputSource;
    protected String msXtaProducer;
    protected String msXtaProducerFiscalId;

    protected boolean mbAuxMoveNextOnSave;
    protected boolean mbAuxRequirePriceComputation;
    protected boolean mbAuxItemSendMail;
    protected String msAuxItemRecipientsTo;
    protected boolean mbAuxProducerSendMail;
    protected String msAuxProducerRecipientsTo;

    protected Vector<SDbTicketNote> mvChildTicketNotes;
    protected Vector<SDbLaboratory> mvChildLaboratories;

    public SDbTicket() {
        super(SModConsts.S_TIC);
        mvChildTicketNotes = new Vector<>();
        mvChildLaboratories = new Vector<>();
        initRegistry();
    }

    /*
     * Private methods
     */

    /**
     * Send mail when ticket tared.
     * @param session Current user session.
     * @param producerId Zero or ID of producer. When zero, a summary of all receptions is included. Otherwise, when non-zero, only the ticket info is rendered.
     */
    private void sendMail(final SGuiSession session, final int producerId) {
        String subject;
        String body;
        String section;

        try {
            if ((producerId == 0 && !msAuxItemRecipientsTo.isEmpty()) || (producerId != 0 && !msAuxProducerRecipientsTo.isEmpty())) {
                int[] curDate = SLibTimeUtils.digestDate(mtDate);
                int curYear = curDate[0];
                int curMonth = curDate[1];
                SDbItem item = (SDbItem) session.readRegistry(SModConsts.SU_ITEM, new int[] { mnFkItemId });
                SDbProducer prodr = (SDbProducer) session.readRegistry(SModConsts.SU_PROD, new int[] { mnFkProducerId });
                String itemName = SLibUtils.textProperCase(item.getName());

                subject = "[SOM] " + "Boleto " + mnNumber + ": " +
                        SLibUtils.textToHtml(itemName) + "; " +
                        SLibUtils.DecimalFormatValue2D.format(mdWeightDestinyNet_r) + " " + SSomConsts.KG + "; " +
                        SLibUtils.textToHtml(prodr.getNameTrade());

                // REPORT HEADER. Ticket info:

                section = SLibUtils.textToHtml("Recepción") + " " + SLibUtils.textToHtml(SLibUtils.textProperCase(msXtaScaleName));

                body = "<b>" + section + "</b><br>"
                    + "<table border='1' bordercolor='#000000' style='background-color:' width='300' cellpadding='0' cellspacing='0'>"
                    + "<tr><td>Boleto</td><td align='left'>" + mnNumber + "</td></tr>"
                    + "<tr><td>Chofer</td><td align='left'>" + SLibUtils.textToHtml(SLibUtils.textProperCase(msDriver.length() > MAX_LEN_NAME ? msDriver.substring(0, MAX_LEN_NAME) : msDriver)) + "</td></tr>"
                    + "<tr><td>Fecha</td><td align='left'>" + SLibUtils.DateFormatDatetime.format(mtDatetimeArrival) + "</td></tr>"
                    + "<tr><td>Proveedor</td><td align='left'>" + SLibUtils.textToHtml((prodr.getNameTrade().length() > MAX_LEN_PROD ? prodr.getNameTrade().substring(0, MAX_LEN_PROD) : prodr.getNameTrade())) + "</td></tr>"
                    + "<tr><td>Producto</td><td align='left'>" + SLibUtils.textToHtml(itemName.length() > MAX_LEN_NAME ? itemName.substring(0, MAX_LEN_NAME) : itemName) + "</td></tr>"
                    + "<tr><td>Peso neto</td><td align='right'>" + SLibUtils.DecimalFormatValue2D.format(mdWeightDestinyNet_r) + " " + SSomConsts.KG + "</td></tr>"
                    + "<tr><td>1er. pesada</td><td align='left'>" + (mbRevueltaImport1 ? SLibUtils.textToHtml("Automática") : "Manual") + "</td></tr>"
                    + "<tr><td>2da. pesada</td><td align='left'>" + (mbRevueltaImport2 ? SLibUtils.textToHtml("Automática") : "Manual") + "</td></tr>"
                    + "</table><br>";

                if (producerId == 0) {
                    // A summary of all receptions is about to be included:
                    body += SSomUtils.composeHtmlSummaryItem(session, mnFkItemId, mtDate);
                }
                
                // Add mail warning:

                body += SSomMailUtils.composeMailWarning();

                // Send mail to recipients:

                SMailSender sender = new SMailSender(
                    ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getMailNotificationConfigHost(),
                    ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getMailNotificationConfigPort(),
                    ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getMailNotificationConfigProtocol(),
                    ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().isMailNotificationConfigStartTls(),
                    ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().isMailNotificationConfigAuth(),
                    ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getMailNotificationConfigUser(),
                    ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getMailNotificationConfigPassword(),
                    ((SGuiClientSessionCustom) session.getSessionCustom()).getCompany().getMailNotificationConfigUser());

                ArrayList<String> recipients;
                if (producerId == 0) {
                    recipients = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(msAuxItemRecipientsTo, ";")));
                }
                else {
                    recipients = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(msAuxProducerRecipientsTo, ";")));
                }
                
                SMail mail = new SMail(sender, subject, body, recipients);
                
                mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
                mail.send();
            }
            else {
                throw new Exception ("No se pudo enviar el mail.");
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
            msQueryResult = "Error: " + e;
        }
    }

    /*
     * Public methods
     */

    public void setPkTicketId(int n) { mnPkTicketId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setPlate(String s) { msPlate = s; }
    public void setPlateCage(String s) { msPlateCage = s; }
    public void setDriver(String s) { msDriver = s; }
    public void setDatetimeArrival(Date t) { mtDatetimeArrival = t; }
    public void setDatetimeDeparture(Date t) { mtDatetimeDeparture = t; }
    public void setPackingFullQuantityArrival(double d) { mdPackingFullQuantityArrival = d; }
    public void setPackingFullQuantityDeparture(double d) { mdPackingFullQuantityDeparture = d; }
    public void setPackingEmptyQuantityArrival(double d) { mdPackingEmptyQuantityArrival = d; }
    public void setPackingEmptyQuantityDeparture(double d) { mdPackingEmptyQuantityDeparture = d; }
    public void setPackingWeightArrival(double d) { mdPackingWeightArrival = d; }
    public void setPackingWeightDeparture(double d) { mdPackingWeightDeparture = d; }
    public void setPackingWeightNet_r(double d) { mdPackingWeightNet_r = d; }
    public void setWeightSource(double d) { mdWeightSource = d; }
    public void setWeightDestinyArrival(double d) { mdWeightDestinyArrival = d; }
    public void setWeightDestinyDeparture(double d) { mdWeightDestinyDeparture = d; }
    public void setWeightDestinyGross_r(double d) { mdWeightDestinyGross_r = d; }
    public void setWeightDestinyNet_r(double d) { mdWeightDestinyNet_r = d; }
    public void setSystemPenaltyPercentage(double d) { mdSystemPenaltyPercentage = d; }
    public void setSystemWeightPayment(double d) { mdSystemWeightPayment = d; }
    public void setSystemPricePerTon(double d) { mdSystemPricePerTon = d; }
    public void setSystemPayment_r(double d) { mdSystemPayment_r = d; }
    public void setSystemFreight(double d) { mdSystemFreight = d; }
    public void setSystemTotal_r(double d) { mdSystemTotal_r = d; }
    public void setUserPenaltyPercentage(double d) { mdUserPenaltyPercentage = d; }
    public void setUserWeightPayment(double d) { mdUserWeightPayment = d; }
    public void setUserPricePerTon(double d) { mdUserPricePerTon = d; }
    public void setUserPayment_r(double d) { mdUserPayment_r = d; }
    public void setUserFreight(double d) { mdUserFreight = d; }
    public void setUserTotal_r(double d) { mdUserTotal_r = d; }
    public void setDpsSupplyDate_n(Date t) { mtDpsSupplyDate_n = t; }
    public void setRevueltaImport1(boolean b) { mbRevueltaImport1 = b; }
    public void setRevueltaImport2(boolean b) { mbRevueltaImport2 = b; }
    public void setWeightSourceAvailable(boolean b) { mbWeightSourceAvailable = b; }
    public void setMfgOutsourcing(boolean b) { mbMfgOutsourcing = b; }
    public void setTared(boolean b) { mbTared = b; }
    public void setPayed(boolean b) { mbPayed = b; }
    public void setAssorted(boolean b) { mbAssorted = b; }
    public void setPacking(boolean b) { mbPacking = b; }
    public void setLaboratory(boolean b) { mbLaboratory = b; }
    public void setWarehouseUnloadRequired(boolean b) { mbWarehouseUnloadRequired = b; }
    public void setDpsSupply(boolean b) { mbDpsSupply = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkScaleId(int n) { mnFkScaleId = n; }
    public void setFkTicketStatusId(int n) { mnFkTicketStatusId = n; }
    public void setFkSeasonId_n(int n) { mnFkSeasonId_n = n; }
    public void setFkRegionId_n(int n) { mnFkRegionId_n = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkProducerId(int n) { mnFkProducerId = n; }
    public void setFkInputSourceId(int n) { mnFkInputSourceId = n; }
    public void setFkLaboratoryId_n(int n) { mnFkLaboratoryId_n = n; }
    public void setFkWarehouseUnloadCompanyId_n(int n) { mnFkWarehouseUnloadCompanyId_n = n; }
    public void setFkWarehouseUnloadBranchId_n(int n) { mnFkWarehouseUnloadBranchId_n = n; }
    public void setFkWarehouseUnloadWarehouseId_n(int n) { mnFkWarehouseUnloadWarehouseId_n = n; }
    public void setFkExternalDpsYearId_n(int n) { mnFkExternalDpsYearId_n = n; }
    public void setFkExternalDpsDocId_n(int n) { mnFkExternalDpsDocId_n = n; }
    public void setFkExternalDpsEntryId_n(int n) { mnFkExternalDpsEntryId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setFkUserTaredId(int n) { mnFkUserTaredId = n; }
    public void setFkUserPayedId(int n) { mnFkUserPayedId = n; }
    public void setFkUserAssortedId(int n) { mnFkUserAssortedId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    public void setTsUserTared(Date t) { mtTsUserTared = t; }
    public void setTsUserPayed(Date t) { mtTsUserPayed = t; }
    public void setTsUserAssorted(Date t) { mtTsUserAssorted = t; }

    public int getPkTicketId() { return mnPkTicketId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public double getQuantity() { return mdQuantity; }
    public String getPlate() { return msPlate; }
    public String getPlateCage() { return msPlateCage; }
    public String getDriver() { return msDriver; }
    public Date getDatetimeArrival() { return mtDatetimeArrival; }
    public Date getDatetimeDeparture() { return mtDatetimeDeparture; }
    public double getPackingFullQuantityArrival() { return mdPackingFullQuantityArrival; }
    public double getPackingFullQuantityDeparture() { return mdPackingFullQuantityDeparture; }
    public double getPackingEmptyQuantityArrival() { return mdPackingEmptyQuantityArrival; }
    public double getPackingEmptyQuantityDeparture() { return mdPackingEmptyQuantityDeparture; }
    public double getPackingWeightArrival() { return mdPackingWeightArrival; }
    public double getPackingWeightDeparture() { return mdPackingWeightDeparture; }
    public double getPackingWeightNet_r() { return mdPackingWeightNet_r; }
    public double getWeightSource() { return mdWeightSource; }
    public double getWeightDestinyArrival() { return mdWeightDestinyArrival; }
    public double getWeightDestinyDeparture() { return mdWeightDestinyDeparture; }
    public double getWeightDestinyGross_r() { return mdWeightDestinyGross_r; }
    public double getWeightDestinyNet_r() { return mdWeightDestinyNet_r; }
    public double getSystemPenaltyPercentage() { return mdSystemPenaltyPercentage; }
    public double getSystemWeightPayment() { return mdSystemWeightPayment; }
    public double getSystemPricePerTon() { return mdSystemPricePerTon; }
    public double getSystemPayment_r() { return mdSystemPayment_r; }
    public double getSystemFreight() { return mdSystemFreight; }
    public double getSystemTotal_r() { return mdSystemTotal_r; }
    public double getUserPenaltyPercentage() { return mdUserPenaltyPercentage; }
    public double getUserWeightPayment() { return mdUserWeightPayment; }
    public double getUserPricePerTon() { return mdUserPricePerTon; }
    public double getUserPayment_r() { return mdUserPayment_r; }
    public double getUserFreight() { return mdUserFreight; }
    public double getUserTotal_r() { return mdUserTotal_r; }
    public Date getDpsSupplyDate_n() { return mtDpsSupplyDate_n; }
    public boolean isRevueltaImport1() { return mbRevueltaImport1; }
    public boolean isRevueltaImport2() { return mbRevueltaImport2; }
    public boolean isWeightSourceAvailable() { return mbWeightSourceAvailable; }
    public boolean isMfgOutsourcing() { return mbMfgOutsourcing; }
    public boolean isTared() { return mbTared; }
    public boolean isPayed() { return mbPayed; }
    public boolean isAssorted() { return mbAssorted; }
    public boolean isPacking() { return mbPacking; }
    public boolean isLaboratory() { return mbLaboratory; }
    public boolean isWarehouseUnloadRequired() { return mbWarehouseUnloadRequired; }
    public boolean isDpsSupply() { return mbDpsSupply; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkScaleId() { return mnFkScaleId; }
    public int getFkTicketStatusId() { return mnFkTicketStatusId; }
    public int getFkSeasonId_n() { return mnFkSeasonId_n; }
    public int getFkRegionId_n() { return mnFkRegionId_n; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkProducerId() { return mnFkProducerId; }
    public int getFkInputSourceId() { return mnFkInputSourceId; }
    public int getFkLaboratoryId_n() { return mnFkLaboratoryId_n; }
    public int getFkWarehouseUnloadCompanyId_n() { return mnFkWarehouseUnloadCompanyId_n; }
    public int getFkWarehouseUnloadBranchId_n() { return mnFkWarehouseUnloadBranchId_n; }
    public int getFkWarehouseUnloadWarehouseId_n() { return mnFkWarehouseUnloadWarehouseId_n; }
    public int getFkExternalDpsYearId_n() { return mnFkExternalDpsYearId_n; }
    public int getFkExternalDpsDocId_n() { return mnFkExternalDpsDocId_n; }
    public int getFkExternalDpsEntryId_n() { return mnFkExternalDpsEntryId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public int getFkUserTaredId() { return mnFkUserTaredId; }
    public int getFkUserPayedId() { return mnFkUserPayedId; }
    public int getFkUserAssortedId() { return mnFkUserAssortedId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    public Date getTsUserTared() { return mtTsUserTared; }
    public Date getTsUserPayed() { return mtTsUserPayed; }
    public Date getTsUserAssorted() { return mtTsUserAssorted; }

    public void setXtaScaleName(String s) { msXtaScaleName = s;  }
    public void setXtaScaleCode(String s) { msXtaScaleCode = s;  }
    public void setXtaSeason(String s) { msXtaSeason = s;  }
    public void setXtaRegion(String s) { msXtaRegion = s;  }
    public void setXtaItem(String s) { msXtaItem = s;  }
    public void setXtaInputCategoryId(int n) { mnXtaInputCategoryId = n; }
    public void setXtaInputClassId(int n) { mnXtaInputClassId = n; }
    public void setXtaInputType(String s) { msXtaInputType = s;  }
    public void setXtaInputSource(String s) { msXtaInputSource = s;  }
    public void setXtaProducer(String s) { msXtaProducer = s;  }
    public void setXtaProviderFiscalId(String s) { msXtaProducerFiscalId = s;  }

    public void setAuxMoveNextOnSend(boolean b) { mbAuxMoveNextOnSave = b; }
    public void setAuxRequirePriceComputation(boolean b) { mbAuxRequirePriceComputation = b; }
    public void setAuxItemSendMail(boolean b) { mbAuxItemSendMail = b; }
    public void setAuxItemRecipientsTo(String s) { msAuxItemRecipientsTo = s; }
    public void setAuxProducerSendMail(boolean b) { mbAuxProducerSendMail = b; }
    public void setAuxProducerRecipientsTo(String s) { msAuxProducerRecipientsTo = s; }

    public String getXtaScaleName() { return msXtaScaleName; }
    public String getXtaScaleCode() { return msXtaScaleCode; }
    public String getXtaSeason() { return msXtaSeason; }
    public String getXtaRegion() { return msXtaRegion; }
    public String getXtaItem() { return msXtaItem; }
    public int getXtaInputCategoryId() { return mnXtaInputCategoryId; }
    public int getXtaInputClassId() { return mnXtaInputClassId; }
    public String getXtaInputType() { return msXtaInputType;  }
    public String getXtaInputSource() { return msXtaInputSource;  }
    public String getXtaProducer() { return msXtaProducer; }
    public String getXtaProducerFiscalId() { return msXtaProducerFiscalId; }

    public boolean isAuxMoveNextOnSend() { return mbAuxMoveNextOnSave; }
    public boolean isAuxRequirePriceComputation() { return mbAuxRequirePriceComputation; }
    public boolean isAuxItemSendMail() { return mbAuxItemSendMail; }
    public String getAuxItemRecipientsTo() { return msAuxItemRecipientsTo; }
    public boolean isAuxProducerSendMail() { return mbAuxProducerSendMail; }
    public String getAuxProducerRecipientsTo() { return msAuxProducerRecipientsTo; }

    public Vector<SDbTicketNote> getChildTicketNotes() { return mvChildTicketNotes; }
    public Vector<SDbLaboratory> getChildLaboratories() { return mvChildLaboratories; }

    @Override
    public String getName() {
        return mnNumber + "";
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTicketId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTicketId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTicketId = 0;
        mnNumber = 0;
        mtDate = null;
        mdQuantity = 0;
        msPlate = "";
        msPlateCage = "";
        msDriver = "";
        mtDatetimeArrival = null;
        mtDatetimeDeparture = null;
        mdPackingFullQuantityArrival = 0;
        mdPackingFullQuantityDeparture = 0;
        mdPackingEmptyQuantityArrival = 0;
        mdPackingEmptyQuantityDeparture = 0;
        mdPackingWeightArrival = 0;
        mdPackingWeightDeparture = 0;
        mdPackingWeightNet_r = 0;
        mdWeightSource = 0;
        mdWeightDestinyArrival = 0;
        mdWeightDestinyDeparture = 0;
        mdWeightDestinyGross_r = 0;
        mdWeightDestinyNet_r = 0;
        mdSystemPenaltyPercentage = 0;
        mdSystemWeightPayment = 0;
        mdSystemPricePerTon = 0;
        mdSystemPayment_r = 0;
        mdSystemFreight = 0;
        mdSystemTotal_r = 0;
        mdUserPenaltyPercentage = 0;
        mdUserWeightPayment = 0;
        mdUserPricePerTon = 0;
        mdUserPayment_r = 0;
        mdUserFreight = 0;
        mdUserTotal_r = 0;
        mtDpsSupplyDate_n = null;
        mbRevueltaImport1 = false;
        mbRevueltaImport2 = false;
        mbWeightSourceAvailable = false;
        mbMfgOutsourcing = false;
        mbTared = false;
        mbPayed = false;
        mbAssorted = false;
        mbPacking = false;
        mbLaboratory = false;
        mbWarehouseUnloadRequired = false;
        mbDpsSupply = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkScaleId = 0;
        mnFkTicketStatusId = 0;
        mnFkSeasonId_n = 0;
        mnFkRegionId_n = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkProducerId = 0;
        mnFkInputSourceId = 0;
        mnFkLaboratoryId_n = 0;
        mnFkWarehouseUnloadCompanyId_n = 0;
        mnFkWarehouseUnloadBranchId_n = 0;
        mnFkWarehouseUnloadWarehouseId_n = 0;
        mnFkExternalDpsYearId_n = 0;
        mnFkExternalDpsDocId_n = 0;
        mnFkExternalDpsEntryId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mnFkUserTaredId = 0;
        mnFkUserPayedId = 0;
        mnFkUserAssortedId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        mtTsUserTared = null;
        mtTsUserPayed = null;
        mtTsUserAssorted = null;

        mbOldTared = false;
        mbOldPayed = false;
        mbOldAssorted = false;
    
        msXtaScaleName = "";
        msXtaScaleCode = "";
        msXtaSeason = "";
        msXtaRegion = "";
        msXtaItem = "";
        mnXtaInputCategoryId = 0;
        mnXtaInputClassId = 0;
        msXtaInputType = "";
        msXtaInputSource = "";
        msXtaProducer = "";
        msXtaProducerFiscalId = "";

        mbAuxMoveNextOnSave = false;
        mbAuxRequirePriceComputation = false;
        mbAuxItemSendMail = false;
        msAuxItemRecipientsTo = "";
        mbAuxProducerSendMail = false;
        msAuxProducerRecipientsTo = "";

        mvChildTicketNotes.clear();
        mvChildLaboratories.clear();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tic = " + mnPkTicketId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tic = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTicketId = 0;

        msSql = "SELECT COALESCE(MAX(id_tic), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTicketId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT t.*, sc.name, sc.code, p.name, p.fis_id, i.name, i.fk_unit, it.id_inp_ct, it.id_inp_cl, it.name, src.name, s.name, r.name " +
                "FROM " + getSqlTable() + " AS t "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SCA) + " AS sc ON "
                + "t.fk_sca = sc.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " AS p ON "
                + "t.fk_prod = p.id_prod "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " AS i ON "
                + "t.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_TP) + " AS it ON "
                + "i.fk_inp_ct = it.id_inp_ct AND i.fk_inp_cl = it.id_inp_cl AND i.fk_inp_tp = it.id_inp_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_INP_SRC) + " AS src ON "
                + "t.fk_inp_src = src.id_inp_src "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SEAS) + " AS s ON "
                + "t.fk_seas_n = s.id_seas "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_REG) + " AS r ON "
                + "t.fk_reg_n = r.id_reg "
                + getSqlWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkTicketId = resultSet.getInt("t.id_tic");
            mnNumber = resultSet.getInt("t.num");
            mtDate = resultSet.getDate("t.dt");
            mdQuantity = resultSet.getDouble("t.qty");
            msPlate = resultSet.getString("t.pla");
            msPlateCage = resultSet.getString("t.pla_cag");
            msDriver = resultSet.getString("t.drv");
            mtDatetimeArrival = resultSet.getTimestamp("t.ts_arr");
            mtDatetimeDeparture = resultSet.getTimestamp("t.ts_dep");
            mdPackingFullQuantityArrival = resultSet.getDouble("t.pac_qty_arr");
            mdPackingFullQuantityDeparture = resultSet.getDouble("t.pac_qty_dep");
            mdPackingEmptyQuantityArrival = resultSet.getDouble("t.pac_emp_qty_arr");
            mdPackingEmptyQuantityDeparture = resultSet.getDouble("t.pac_emp_qty_dep");
            mdPackingWeightArrival = resultSet.getDouble("t.pac_wei_arr");
            mdPackingWeightDeparture = resultSet.getDouble("t.pac_wei_dep");
            mdPackingWeightNet_r = resultSet.getDouble("t.pac_wei_net_r");
            mdWeightSource = resultSet.getDouble("t.wei_src");
            mdWeightDestinyArrival = resultSet.getDouble("t.wei_des_arr");
            mdWeightDestinyDeparture = resultSet.getDouble("t.wei_des_dep");
            mdWeightDestinyGross_r = resultSet.getDouble("t.wei_des_gro_r");
            mdWeightDestinyNet_r = resultSet.getDouble("t.wei_des_net_r");
            mdSystemPenaltyPercentage = resultSet.getDouble("t.sys_pen_per");
            mdSystemWeightPayment = resultSet.getDouble("t.sys_wei_pay");
            mdSystemPricePerTon = resultSet.getDouble("t.sys_prc_ton");
            mdSystemPayment_r = resultSet.getDouble("t.sys_pay_r");
            mdSystemFreight = resultSet.getDouble("t.sys_fre");
            mdSystemTotal_r = resultSet.getDouble("t.sys_tot_r");
            mdUserPenaltyPercentage = resultSet.getDouble("t.usr_pen_per");
            mdUserWeightPayment = resultSet.getDouble("t.usr_wei_pay");
            mdUserPricePerTon = resultSet.getDouble("t.usr_prc_ton");
            mdUserPayment_r = resultSet.getDouble("t.usr_pay_r");
            mdUserFreight = resultSet.getDouble("t.usr_fre");
            mdUserTotal_r = resultSet.getDouble("t.usr_tot_r");
            mtDpsSupplyDate_n = resultSet.getDate("t.dps_dt_n");
            mbRevueltaImport1 = resultSet.getBoolean("t.b_rev_1");
            mbRevueltaImport2 = resultSet.getBoolean("t.b_rev_2");
            mbWeightSourceAvailable = resultSet.getBoolean("t.b_wei_src");
            mbMfgOutsourcing = resultSet.getBoolean("t.b_mfg_out");
            mbTared = resultSet.getBoolean("t.b_tar");
            mbPayed = resultSet.getBoolean("t.b_pay");
            mbAssorted = resultSet.getBoolean("t.b_ass");
            mbPacking = resultSet.getBoolean("t.b_paq");
            mbLaboratory = resultSet.getBoolean("t.b_lab");
            mbWarehouseUnloadRequired = resultSet.getBoolean("b_wah_unld_req");
            mbDpsSupply = resultSet.getBoolean("b_dps");
            mbDeleted = resultSet.getBoolean("t.b_del");
            mbSystem = resultSet.getBoolean("t.b_sys");
            mnFkScaleId = resultSet.getInt("t.fk_sca");
            mnFkTicketStatusId = resultSet.getInt("t.fk_tic_st");
            mnFkSeasonId_n = resultSet.getInt("t.fk_seas_n");
            mnFkRegionId_n = resultSet.getInt("t.fk_reg_n");
            mnFkItemId = resultSet.getInt("t.fk_item");
            mnFkUnitId = resultSet.getInt("t.fk_unit");
            mnFkProducerId = resultSet.getInt("t.fk_prod");
            mnFkInputSourceId = resultSet.getInt("t.fk_inp_src");
            mnFkLaboratoryId_n = resultSet.getInt("t.fk_lab_n");
            mnFkWarehouseUnloadCompanyId_n = resultSet.getInt("fk_wah_unld_co_n");
            mnFkWarehouseUnloadBranchId_n = resultSet.getInt("fk_wah_unld_cob_n");
            mnFkWarehouseUnloadWarehouseId_n = resultSet.getInt("fk_wah_unld_wah_n");
            mnFkExternalDpsYearId_n = resultSet.getInt("t.fk_ext_dps_year_n");
            mnFkExternalDpsDocId_n = resultSet.getInt("t.fk_ext_dps_doc_n");
            mnFkExternalDpsEntryId_n = resultSet.getInt("t.fk_ext_dps_ety_n");
            mnFkUserInsertId = resultSet.getInt("t.fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("t.fk_usr_upd");
            mnFkUserTaredId = resultSet.getInt("t.fk_usr_tar");
            mnFkUserPayedId = resultSet.getInt("t.fk_usr_pay");
            mnFkUserAssortedId = resultSet.getInt("t.fk_usr_ass");
            mtTsUserInsert = resultSet.getTimestamp("t.ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("t.ts_usr_upd");
            mtTsUserTared = resultSet.getTimestamp("t.ts_usr_tar");
            mtTsUserPayed = resultSet.getTimestamp("t.ts_usr_pay");
            mtTsUserAssorted = resultSet.getTimestamp("t.ts_usr_ass");

            mbOldTared = mbTared;
            mbOldPayed = mbPayed;
            mbOldAssorted = mbAssorted;

            msXtaScaleName = resultSet.getString("sc.name");
            msXtaScaleCode = resultSet.getString("sc.code");
            msXtaSeason = resultSet.getString("s.name");
            if (resultSet.wasNull()) {
                msXtaSeason = SUtilConsts.NON_APPLYING;
            }
            msXtaRegion = resultSet.getString("r.name");
            if (resultSet.wasNull()) {
                msXtaRegion = SUtilConsts.NON_APPLYING;
            }
            msXtaItem = resultSet.getString("i.name");
            mnXtaInputCategoryId = resultSet.getInt("it.id_inp_ct");
            mnXtaInputClassId = resultSet.getInt("it.id_inp_cl");
            msXtaInputType = resultSet.getString("it.name");
            msXtaInputSource = resultSet.getString("src.name");
            msXtaProducer = resultSet.getString("p.name");
            msXtaProducerFiscalId = resultSet.getString("p.fis_id");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_note FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC_NOTE) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbTicketNote child = new SDbTicketNote();
                child.read(session, new int[] { mnPkTicketId, resultSet.getInt(1) });
                mvChildTicketNotes.add(child);
            }

            if (mnFkLaboratoryId_n > SLibConsts.UNDEFINED) {
                SDbLaboratory child = new SDbLaboratory();
                child.read(session, new int[] { mnFkLaboratoryId_n });
                if (!child.isDeleted()) {
                    mvChildLaboratories.add(child);
                }
                else {
                    mnFkLaboratoryId_n = SLibConsts.UNDEFINED;
                }
            }

            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        computeWeight(session, true);

        if (mbAuxRequirePriceComputation) {
            computePrice(session);
        }
        
        if (mbAuxMoveNextOnSave) {
            moveNext(session);
        }
        
        if (mbTared && !mbOldTared) {
            mnFkUserTaredId = session.getUser().getPkUserId();
        }
        else if (mnFkUserTaredId == 0) {
            mnFkUserTaredId = SUtilConsts.USR_NA_ID;
        }
        
        if (mbPayed && !mbOldPayed) {
            mnFkUserPayedId = session.getUser().getPkUserId();
        }
        else if (mnFkUserPayedId == 0) {
            mnFkUserPayedId = SUtilConsts.USR_NA_ID;
        }
        
        if (mbAssorted && !mbOldAssorted) {
            mnFkUserAssortedId = session.getUser().getPkUserId();
        }
        else if (mnFkUserAssortedId == 0) {
            mnFkUserAssortedId = SUtilConsts.USR_NA_ID;
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            SDbItem item = new SDbItem();
            item.read(session, new int [] { mnFkItemId });
            SDbInputCategory inpCat = new SDbInputCategory(); 
            inpCat.read(session, new int[] { item.getFkInputCategoryId() });
            mbWarehouseUnloadRequired = inpCat.isWareouseUnloadRequired();

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkTicketId + ", " +
                    mnNumber + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mdQuantity + ", " +
                    "'" + msPlate + "', " +
                    "'" + msPlateCage + "', " +
                    "'" + msDriver + "', " +
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeArrival) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeDeparture) + "', " +
                    mdPackingFullQuantityArrival + ", " +
                    mdPackingFullQuantityDeparture + ", " +
                    mdPackingEmptyQuantityArrival + ", " +
                    mdPackingEmptyQuantityDeparture + ", " +
                    mdPackingWeightArrival + ", " +
                    mdPackingWeightDeparture + ", " +
                    mdPackingWeightNet_r + ", " +
                    mdWeightSource + ", " +
                    mdWeightDestinyArrival + ", " +
                    mdWeightDestinyDeparture + ", " +
                    mdWeightDestinyGross_r + ", " +
                    mdWeightDestinyNet_r + ", " +
                    SLibUtils.round(mdSystemPenaltyPercentage, SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits()) + ", " +
                    mdSystemWeightPayment + ", " +
                    mdSystemPricePerTon + ", " +
                    mdSystemPayment_r + ", " +
                    mdSystemFreight + ", " +
                    mdSystemTotal_r + ", " +
                    SLibUtils.round(mdUserPenaltyPercentage, SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits()) + ", " +
                    mdUserWeightPayment + ", " +
                    mdUserPricePerTon + ", " +
                    mdUserPayment_r + ", " +
                    mdUserFreight + ", " +
                    mdUserTotal_r + ", " +
                    "" + (mtDpsSupplyDate_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtDpsSupplyDate_n) + "'") + ", " +
                    (mbRevueltaImport1 ? 1 : 0) + ", " +
                    (mbRevueltaImport2 ? 1 : 0) + ", " +
                    (mbWeightSourceAvailable ? 1 : 0) + ", " +
                    (mbMfgOutsourcing ? 1 : 0) + ", " +
                    (mbTared ? 1 : 0) + ", " +
                    (mbPayed ? 1 : 0) + ", " +
                    (mbAssorted ? 1 : 0) + ", " +
                    (mbPacking ? 1 : 0) + ", " +
                    (mbLaboratory ? 1 : 0) + ", " +
                    (mbWarehouseUnloadRequired ? 1 : 0) + ", " + 
                    (mbDpsSupply ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkScaleId + ", " +
                    mnFkTicketStatusId + ", " +
                    (mnFkSeasonId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkSeasonId_n) + ", " +
                    (mnFkRegionId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkRegionId_n) + ", " +
                    mnFkItemId + ", " +
                    mnFkUnitId + ", " +
                    mnFkProducerId + ", " +
                    mnFkInputSourceId + ", " + 
                    (mnFkLaboratoryId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkLaboratoryId_n) + ", " +
                    (mnFkWarehouseUnloadCompanyId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseUnloadBranchId_n) + ", " + 
                    (mnFkWarehouseUnloadBranchId_n == SLibConsts.UNDEFINED  ? "NULL" : mnFkWarehouseUnloadCompanyId_n) + ", " + 
                    (mnFkWarehouseUnloadWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseUnloadWarehouseId_n) + ", " + 
                    (mnFkExternalDpsYearId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalDpsYearId_n) + ", " +
                    (mnFkExternalDpsDocId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalDpsDocId_n) + ", " +
                    (mnFkExternalDpsEntryId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalDpsEntryId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    mnFkUserTaredId + ", " +
                    mnFkUserPayedId + ", " +
                    mnFkUserAssortedId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_tic = " + mnPkTicketId + ", " +
                    */
                    "num = " + mnNumber + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "qty = " + mdQuantity + ", " +
                    "pla = '" + msPlate + "', " +
                    "pla_cag = '" + msPlateCage + "', " +
                    "drv = '" + msDriver + "', " +
                    "ts_arr = '" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeArrival) + "', " +
                    "ts_dep = '" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeDeparture) + "', " +
                    "pac_qty_arr = " + mdPackingFullQuantityArrival + ", " +
                    "pac_qty_dep = " + mdPackingFullQuantityDeparture + ", " +
                    "pac_emp_qty_arr = " + mdPackingEmptyQuantityArrival + ", " +
                    "pac_emp_qty_dep = " + mdPackingEmptyQuantityDeparture + ", " +
                    "pac_wei_arr = " + mdPackingWeightArrival + ", " +
                    "pac_wei_dep = " + mdPackingWeightDeparture + ", " +
                    "pac_wei_net_r = " + mdPackingWeightNet_r + ", " +
                    "wei_src = " + mdWeightSource + ", " +
                    "wei_des_arr = " + mdWeightDestinyArrival + ", " +
                    "wei_des_dep = " + mdWeightDestinyDeparture + ", " +
                    "wei_des_gro_r = " + mdWeightDestinyGross_r + ", " +
                    "wei_des_net_r = " + mdWeightDestinyNet_r + ", " +
                    "sys_pen_per = " + SLibUtils.round(mdSystemPenaltyPercentage, SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits()) + ", " +
                    "sys_wei_pay = " + mdSystemWeightPayment + ", " +
                    "sys_prc_ton = " + mdSystemPricePerTon + ", " +
                    "sys_pay_r = " + mdSystemPayment_r + ", " +
                    "sys_fre = " + mdSystemFreight + ", " +
                    "sys_tot_r = " + mdSystemTotal_r + ", " +
                    "usr_pen_per = " + SLibUtils.round(mdUserPenaltyPercentage, SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits()) + ", " +
                    "usr_wei_pay = " + mdUserWeightPayment + ", " +
                    "usr_prc_ton = " + mdUserPricePerTon + ", " +
                    "usr_pay_r = " + mdUserPayment_r + ", " +
                    "usr_fre = " + mdUserFreight + ", " +
                    "usr_tot_r = " + mdUserTotal_r + ", " +
                    "dps_dt_n = " + (mtDpsSupplyDate_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtDpsSupplyDate_n) + "'") + ", " +
                    "b_rev_1 = " + (mbRevueltaImport1 ? 1 : 0) + ", " +
                    "b_rev_2 = " + (mbRevueltaImport2 ? 1 : 0) + ", " +
                    "b_wei_src = " + (mbWeightSourceAvailable ? 1 : 0) + ", " +
                    "b_mfg_out = " + (mbMfgOutsourcing ? 1 : 0) + ", " +
                    "b_tar = " + mbTared + ", " +
                    "b_pay = " + (mbPayed ? 1 : 0) + ", " +
                    "b_ass = " + (mbAssorted ? 1 : 0) + ", " +
                    "b_paq = " + (mbPacking ? 1 : 0) + ", " +
                    "b_lab = " + (mbLaboratory ? 1 : 0) + ", " +
                    "b_wah_unld_req = " + (mbWarehouseUnloadRequired ? 1 : 0) + ", " +
                    "b_dps = " + (mbDpsSupply ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_sca = " + mnFkScaleId + ", " +
                    "fk_tic_st = " + mnFkTicketStatusId + ", " +
                    "fk_seas_n = " + (mnFkSeasonId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkSeasonId_n) + ", " +
                    "fk_reg_n = " + (mnFkRegionId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkRegionId_n) + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_prod = " + mnFkProducerId + ", " +
                    "fk_inp_src = " + mnFkInputSourceId + ", " +
                    "fk_lab_n = " + (mnFkLaboratoryId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkLaboratoryId_n) + ", " +
                    "fk_wah_unld_co_n = " + (mnFkWarehouseUnloadCompanyId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseUnloadBranchId_n) + ", " + 
                    "fk_wah_unld_cob_n = " + (mnFkWarehouseUnloadBranchId_n == SLibConsts.UNDEFINED  ? "NULL" : mnFkWarehouseUnloadCompanyId_n) + ", " +
                    "fk_wah_unld_wah_n = " + (mnFkWarehouseUnloadWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkWarehouseUnloadWarehouseId_n) + ", " +
                    "fk_ext_dps_year_n = " + (mnFkExternalDpsYearId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalDpsYearId_n) + ", " +
                    "fk_ext_dps_doc_n = " + (mnFkExternalDpsDocId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalDpsDocId_n) + ", " +
                    "fk_ext_dps_ety_n = " + (mnFkExternalDpsEntryId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkExternalDpsEntryId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "fk_usr_tar = " + mnFkUserTaredId + ", " +
                    "fk_usr_pay = " + mnFkUserPayedId + ", " +
                    "fk_usr_ass = " + mnFkUserAssortedId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" +
                    (mbTared && !mbOldTared ? ", ts_usr_tar = NOW()" : "") +
                    (mbPayed && !mbOldPayed ? ", ts_usr_pay = NOW()" : "") +
                    (mbAssorted && !mbOldAssorted ? ", ts_usr_ass = NOW()" : "") + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC_NOTE) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (SDbTicketNote child : mvChildTicketNotes) {
            child.setRegistryNew(true);
            child.setPkTicketId(mnPkTicketId);
            child.save(session);
        }

        for (SDbLaboratory child : mvChildLaboratories) {
            if (child.isRegistryNew() || child.isRegistryEdited()) {
                child.setDeleted(mbDeleted);
                child.save(session);
                saveField(session.getStatement(), getPrimaryKey(), FIELD_LABORATORY, child.getPkLaboratoryId());
            }
        }

        if (mbAuxItemSendMail) {
            sendMail(session, 0);
        }
        
        if (mbAuxProducerSendMail) {
            sendMail(session, mnFkProducerId);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbTicket clone() throws CloneNotSupportedException {
        SDbTicket registry = new SDbTicket();

        registry.setPkTicketId(this.getPkTicketId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setQuantity(this.getQuantity());
        registry.setPlate(this.getPlate());
        registry.setPlateCage(this.getPlateCage());
        registry.setDriver(this.getDriver());
        registry.setDatetimeArrival(this.getDatetimeArrival());
        registry.setDatetimeDeparture(this.getDatetimeDeparture());
        registry.setPackingFullQuantityArrival(this.getPackingFullQuantityArrival());
        registry.setPackingFullQuantityDeparture(this.getPackingFullQuantityDeparture());
        registry.setPackingEmptyQuantityArrival(this.getPackingEmptyQuantityArrival());
        registry.setPackingEmptyQuantityDeparture(this.getPackingEmptyQuantityDeparture());
        registry.setPackingWeightArrival(this.getPackingWeightArrival());
        registry.setPackingWeightDeparture(this.getPackingWeightDeparture());
        registry.setPackingWeightNet_r(this.getPackingWeightNet_r());
        registry.setWeightSource(this.getWeightSource());
        registry.setWeightDestinyArrival(this.getWeightDestinyArrival());
        registry.setWeightDestinyDeparture(this.getWeightDestinyDeparture());
        registry.setWeightDestinyGross_r(this.getWeightDestinyGross_r());
        registry.setWeightDestinyNet_r(this.getWeightDestinyNet_r());
        registry.setSystemPenaltyPercentage(this.getSystemPenaltyPercentage());
        registry.setSystemWeightPayment(this.getSystemWeightPayment());
        registry.setSystemPricePerTon(this.getSystemPricePerTon());
        registry.setSystemPayment_r(this.getSystemPayment_r());
        registry.setSystemFreight(this.getSystemFreight());
        registry.setSystemTotal_r(this.getSystemTotal_r());
        registry.setUserPenaltyPercentage(this.getUserPenaltyPercentage());
        registry.setUserWeightPayment(this.getUserWeightPayment());
        registry.setUserPricePerTon(this.getUserPricePerTon());
        registry.setUserPayment_r(this.getUserPayment_r());
        registry.setUserFreight(this.getUserFreight());
        registry.setUserTotal_r(this.getUserTotal_r());
        registry.setDpsSupplyDate_n(this.getDpsSupplyDate_n());
        registry.setRevueltaImport1(this.isRevueltaImport1());
        registry.setRevueltaImport2(this.isRevueltaImport2());
        registry.setWeightSourceAvailable(this.isWeightSourceAvailable());
        registry.setMfgOutsourcing(this.isMfgOutsourcing());
        registry.setTared(this.isTared());
        registry.setPayed(this.isPayed());
        registry.setAssorted(this.isAssorted());
        registry.setPacking(this.isPacking());
        registry.setLaboratory(this.isLaboratory());
        registry.setWarehouseUnloadRequired(this.isWarehouseUnloadRequired());
        registry.setDpsSupply(this.isDpsSupply());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkScaleId(this.getFkScaleId());
        registry.setFkTicketStatusId(this.getFkTicketStatusId());
        registry.setFkSeasonId_n(this.getFkSeasonId_n());
        registry.setFkRegionId_n(this.getFkRegionId_n());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkProducerId(this.getFkProducerId());
        registry.setFkInputSourceId(this.getFkInputSourceId());
        registry.setFkLaboratoryId_n(this.getFkLaboratoryId_n());
        registry.setFkWarehouseUnloadCompanyId_n(this.getFkWarehouseUnloadCompanyId_n());
        registry.setFkWarehouseUnloadBranchId_n(this.getFkWarehouseUnloadBranchId_n());
        registry.setFkWarehouseUnloadWarehouseId_n(this.getFkWarehouseUnloadWarehouseId_n());
        registry.setFkExternalDpsYearId_n(this.getFkExternalDpsYearId_n());
        registry.setFkExternalDpsDocId_n(this.getFkExternalDpsDocId_n());
        registry.setFkExternalDpsEntryId_n(this.getFkExternalDpsEntryId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setFkUserTaredId(this.getFkUserTaredId());
        registry.setFkUserPayedId(this.getFkUserPayedId());
        registry.setFkUserAssortedId(this.getFkUserAssortedId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        registry.setTsUserTared(this.getTsUserTared());
        registry.setTsUserPayed(this.getTsUserPayed());
        registry.setTsUserAssorted(this.getTsUserAssorted());
        
        registry.mbOldTared = this.mbOldTared;
        registry.mbOldPayed = this.mbOldPayed;
        registry.mbOldAssorted = this.mbOldAssorted;

        registry.setXtaScaleName(this.getXtaScaleName());
        registry.setXtaScaleCode(this.getXtaScaleCode());
        registry.setXtaSeason(this.getXtaSeason());
        registry.setXtaRegion(this.getXtaRegion());
        registry.setXtaItem(this.getXtaItem());
        registry.setXtaInputCategoryId(this.getXtaInputCategoryId());
        registry.setXtaInputClassId(this.getXtaInputClassId());
        registry.setXtaInputType(this.getXtaInputType());
        registry.setXtaInputSource(this.getXtaInputSource());
        registry.setXtaProducer(this.getXtaProducer());
        registry.setXtaProviderFiscalId(this.getXtaProducerFiscalId());

        registry.setAuxMoveNextOnSend(this.isAuxMoveNextOnSend());
        registry.setAuxRequirePriceComputation(this.isAuxRequirePriceComputation());
        registry.setAuxItemSendMail(this.isAuxItemSendMail());
        registry.setAuxItemRecipientsTo(this.getAuxItemRecipientsTo());
        registry.setAuxProducerSendMail(this.isAuxProducerSendMail());
        registry.setAuxProducerRecipientsTo(this.getAuxProducerRecipientsTo());

        for (SDbTicketNote child : mvChildTicketNotes) {
            registry.getChildTicketNotes().add(child.clone());
        }

        for (SDbLaboratory child : mvChildLaboratories) {
            registry.getChildLaboratories().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);

        if (can) {
            initQueryMembers();
            can = !SSomUtils.existsTicket(session, mnNumber, mnPkTicketId);

            if (!can) {
                msQueryResult = "¡El boleto #" + mnNumber + " ya existe!";
            }
        }

        return can;
    }

    @Override
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);

        if (can) {
            can = !isLinkIog(session);

            if (!can) {
                msQueryResult = "¡El boleto está vinculado con un documento de inventarios!";
            }
            else {
                can = !SSomUtils.existsTicket(session, mnNumber, mnPkTicketId);

                if (!can) {
                    msQueryResult = "¡El boleto ya existe!";
                }
            }
        }

        return can;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_PAYED:
                msSql += "b_pay = " + value + " ";
                break;
            case FIELD_TICKET_STATUS:
                msSql += "fk_tic_st = " + value + " ";
                break;
            case FIELD_LABORATORY:
                msSql += "fk_lab_n = " + value + " ";
                break;
            case FIELD_TARED:
                msSql += "b_tar = " + value + " ";
                break;
            case FIELD_ASSORTED:
                msSql += "b_ass = " + value + ", fk_usr_ass = " + mnFkUserAssortedId + ", ts_usr_ass = NOW() ";
                break;
            case FIELD_DPS:
                msSql += "b_dps = " + value + " ";
                break;
            case FIELD_DPS_NULL:
                msSql += "fk_ext_dps_year_n = NULL, fk_ext_dps_doc_n = NULL, fk_ext_dps_ety_n = NULL ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
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
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(final boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = mnNumber;
                break;
            case 1:
                value = mtDatetimeArrival;
                break;
            case 2:
                value = msPlate;
                break;
            case 3:
                value = mdWeightSource;
                break;
            case 4:
                value = mdWeightDestinyNet_r;
                break;
            case 5:
                value = !mvChildLaboratories.isEmpty() ? mvChildLaboratories.get(0).getImpuritiesPercentageAverage() : 0;
                break;
            case 6:
                value = !mvChildLaboratories.isEmpty() ? mvChildLaboratories.get(0).getMoisturePercentageAverage() : 0;
                break;
            case 7:
                value = mdUserPenaltyPercentage;
                break;
            case 8:
                value = mdUserWeightPayment;
                break;
            case 9:
                value = mdUserPricePerTon;
                break;
            case 10:
                value = mbPayed;
                break;
            case 11:
                value = mdUserTotal_r;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                mbRegistryEdited = true;
                mbPayed = (Boolean) value;
                break;
            case 11:
                break;
            default:
        }
    }

    public boolean isLinkIog(final SGuiSession session) throws SQLException {
        boolean isLink = false;

        msSql = "SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.S_IOG) + " WHERE b_del = 0 AND fk_tic_n = " + mnPkTicketId + " ";

        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                isLink = resultSet.getInt(1) > 0;
            }
        }

        return isLink;
    }

    public void computeWeight(SGuiSession session, boolean computePrice) throws Exception {
        double oldWeightDestinyNet = mdWeightDestinyNet_r;
        
        //SDbItem item = (SDbItem) session.readRegistry(SModConsts.SU_ITEM, new int[] { mnFkItemId }); // when this method is called from CLI, session does not have modules!
        SDbItem item = new SDbItem();
        item.read(session, new int[] { mnFkItemId });

        mdPackingWeightArrival = (mdPackingFullQuantityArrival + mdPackingEmptyQuantityArrival) * item.getPackingWeight();
        mdPackingWeightDeparture = (mdPackingFullQuantityDeparture + mdPackingEmptyQuantityDeparture) * item.getPackingWeight();
        mdPackingWeightNet_r = mdPackingWeightArrival - mdPackingWeightDeparture;
        
        mdWeightDestinyGross_r = mdWeightDestinyArrival - mdWeightDestinyDeparture;
        mdWeightDestinyNet_r = mdWeightDestinyDeparture == 0 ? 0 : mdWeightDestinyGross_r - mdPackingWeightNet_r;

        if (!mbWeightSourceAvailable) {
            mdWeightSource = mdWeightDestinyGross_r;
        }

        mdQuantity = mdWeightDestinyNet_r / item.getUnitaryWeight();
        
        if (computePrice && oldWeightDestinyNet != 0 && Math.abs(oldWeightDestinyNet - mdWeightDestinyNet_r) > 0.001) {
            computePrice(session);
            mbAuxRequirePriceComputation = false;
        }
    }
    
    public void computePrice(SGuiSession session) {
        double dMaxImpuritiesPer = 0;
        double dMaxMoisturePer = 0;
        double dPricePerTon = 0;
        double dImpuritiesPer = 0;
        double dMoisturePer = 0;
        SDbSeasonRegion seasonRegion = null;
        SDbSeasonProducer seasonProducer = null;
        
        /*
         * Tickets are usually addressed to some season, but not allways to some region.
         * So this is more likely to happen that region is missed.
         */

        if (mbLaboratory && mnFkSeasonId_n != SLibConsts.UNDEFINED && mnFkRegionId_n != SLibConsts.UNDEFINED) {
            seasonRegion = (SDbSeasonRegion) session.readRegistry(SModConsts.SU_SEAS_REG, new int[] { mnFkSeasonId_n, mnFkRegionId_n, mnFkItemId });
            seasonProducer = (SDbSeasonProducer) session.readRegistry(SModConsts.SU_SEAS_PROD, new int[] { mnFkSeasonId_n, mnFkRegionId_n, mnFkItemId, mnFkProducerId });

            dMaxImpuritiesPer = seasonRegion.getMaximumImpuritiesPercentage();
            dMaxMoisturePer = seasonRegion.getMaximumMoisturePercentage();
            dPricePerTon = seasonProducer.isPricePerTon() ? seasonProducer.getPricePerTon() : seasonRegion.getPricePerTon();

            if (!mvChildLaboratories.isEmpty()) {
                mvChildLaboratories.get(0).computeTestsAverage();

                dImpuritiesPer = (mvChildLaboratories.get(0).getImpuritiesPercentageAverage() - dMaxImpuritiesPer) <= 0 ? 0 : (mvChildLaboratories.get(0).getImpuritiesPercentageAverage() - dMaxImpuritiesPer);
                dMoisturePer = (mvChildLaboratories.get(0).getMoisturePercentageAverage() - dMaxMoisturePer) <= 0 ? 0 : (mvChildLaboratories.get(0).getMoisturePercentageAverage() - dMaxMoisturePer);

                mdSystemPenaltyPercentage = SLibUtils.round((dImpuritiesPer + dMoisturePer), SLibUtils.DecimalFormatPercentage4D.getMaximumFractionDigits());

                mdUserPenaltyPercentage = mdSystemPenaltyPercentage;
            }

            // System computed values:

            mdSystemWeightPayment = SLibUtils.round(((mdWeightSource - mdWeightDestinyNet_r * mdSystemPenaltyPercentage)), SLibUtils.DecimalFormatPercentage0D.getMaximumFractionDigits());
            mdSystemPricePerTon = dPricePerTon;
            mdSystemPayment_r = (mdSystemWeightPayment * mdSystemPricePerTon) / 1000;
            mdSystemTotal_r = mdSystemPayment_r;

            // User values:

            mdUserWeightPayment = mdSystemWeightPayment;
            mdUserPricePerTon = mdSystemPricePerTon;
            mdUserPayment_r = mdSystemPayment_r;
            mdUserFreight = mdSystemFreight;
            mdUserTotal_r = mdSystemTotal_r;
        }
    }
    
    public void movePrevious(final SGuiSession session) throws SQLException, Exception {
        String msg = "No puede regresar el boleto al estado anterior porque:\n";

        if (mbDeleted) {
            throw new Exception(msg + "El boleto está eliminado.");
        }
        else if (mnFkTicketStatusId == SModSysConsts.SS_TIC_ST_LAB) {
            if (mnFkLaboratoryId_n != SLibConsts.UNDEFINED) {
                throw new Exception(msg + "El boleto tiene análisis de laboratorio.");
            }
            else {
                mnFkTicketStatusId = SModSysConsts.SS_TIC_ST_SCA;
            }
        }
        else if (mnFkTicketStatusId == SModSysConsts.SS_TIC_ST_ADM) {
            if (mbPayed) {
                throw new Exception(msg + "El boleto está pagado.");
            }
            else if (mbLaboratory) {
                mnFkTicketStatusId = SModSysConsts.SS_TIC_ST_LAB;
            }
            else {
                mnFkTicketStatusId = SModSysConsts.SS_TIC_ST_SCA;
            }
        }

        save(session);
    }

    public void moveNext(final SGuiSession session) throws SQLException, Exception {
        String msg = "No puede enviar el boleto al estado siguiente porque:\n";

        if (mbDeleted) {
            throw new Exception(msg + "El boleto está eliminado.");
        }
        else if (mnFkTicketStatusId == SModSysConsts.SS_TIC_ST_SCA) {
            if (mbLaboratory) {
                mnFkTicketStatusId = SModSysConsts.SS_TIC_ST_LAB;
            }
            else {
                mnFkTicketStatusId = SModSysConsts.SS_TIC_ST_ADM;
            }
        }
        else if (mnFkTicketStatusId == SModSysConsts.SS_TIC_ST_LAB) {
            if (mnFkLaboratoryId_n == SLibConsts.UNDEFINED && !mbAuxMoveNextOnSave) {
                throw new Exception(msg + "El boleto no tiene análisis de laboratorio.");
            }
            else if (!mvChildLaboratories.get(0).isDone() && !mbAuxMoveNextOnSave) {
                throw new Exception(msg + "El análisis de laboratorio del boleto no está terminado.");
            }
            else if (!mbTared) {
                throw new Exception(msg + "El boleto no está tarado.");
            }
            else {
                mnFkTicketStatusId = SModSysConsts.SS_TIC_ST_ADM;
                computePrice(session);
            }
        }

        if (!mbAuxMoveNextOnSave) {
            save(session);
        }

        mbAuxMoveNextOnSave = false;
    }
    
    /**
     * Get tickets by number with fuzzy search on ticket number.
     * @param session GUI session.
     * @param number Number to search.
     * @return Array of tickets.
     * @throws SQLException
     * @throws Exception 
     */
    public static ArrayList<SDbTicket> getTicketsByNumber(final SGuiSession session, final int number) throws SQLException, Exception {
        int count = 0;
        ArrayList<SDbTicket> tickets = new ArrayList<>();
        
        String sql = "SELECT id_tic "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_TIC) + " "
                + "WHERE num LIKE '%" + number + "%' "
                + "ORDER BY num, id_tic;";
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                if (++count > MAX_SEARCH_TICKETS) {
                    session.getClient().showMsgBoxWarning("La búsqueda arrojó demasiados resultados, y se acotó a " + MAX_SEARCH_TICKETS + " registros.");
                    break;
                }
                
                SDbTicket ticket = (SDbTicket) session.readRegistry(SModConsts.S_TIC, new int[] { resultSet.getInt(1) });
                tickets.add(ticket);
            }
        }
        
        return tickets;
    }
}
