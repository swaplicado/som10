/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.cli;

import erp.lib.SLibUtilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import som.mod.SModSysConsts;

/**
 * CLI mailer of report of summary of reception of ORGANIC fruits at scale (in HTML format).
 * "Resumen báscula fruta"
 * 
 * @author Sergio Flores, Isabel García, Sergio Flores
 */
public class SCliMailerReportFruitsAltSummary {
    
    //public static final int REP_MODE_STANDARD = 1;
    //public static final int REP_MODE_COMPARATIVE = 2;
    
    /** Argument index for list of ID of items. */
    private static final int ARG_IDX_ITEM_IDS = 0;
    /** Argument index for report date. */
    private static final int ARG_IDX_DATE = 1;
    
    
    /** Argument index for list of mail-To recipients. */
    private static final int ARG_IDX_MAIL_TO = 2;
    /** Argument index for list of mail-Bcc recipients. */
    private static final int ARG_IDX_MAIL_BCC = 3;
    
    private static final String ARG_DATE_TODAY = "TODAY";
    private static final String ARG_DATE_YESTERDAY = "YESTERDAY";

    private static final String[] DEF_ITEM_IDS = new String[] { SCliConsts.IDS_PAIR_AVO_FRUIT_CNV_ORG };
    private static final Date DEF_DATE = SLibTimeUtils.createDate(2026, 2, 25);
    //private static final int DEF_REP_MODE = REP_MODE_COMPARATIVE;
    private static final String DEF_MAIL_TO = "sflores@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "isabel.garcia@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "isabel.garcia@swaplicado.com.mx;sflores@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "gortiz@aeth.mx";
    //private static final String DEF_MAIL_TO = "gortiz@aeth.mx;sflores@swaplicado.com.mx";
    private static final String DEF_MAIL_BCC = "sflores.swaplicado@gmail.com";
    //private static final String DEF_MAIL_BCC = "isabel.garcia@swaplicado.com.mx";

    /**
     * @param args the command line arguments.
     * Four arguments expected:
     * 1: List of ID of items to be reported, passed in pairs, in format ID_CONVENTIONAL-ID_ORGANIC, e.g., 6-64 (separated with semicolon, without blanks between them, OBVIOUSLY!)
     * 2: Date. Date for report.
     * 3: List of mail-To recipients (separated with semicolon, without blanks between them, OBVIOUSLY!).
     * 4: List of mail-Bcc recipients (separated with semicolon, without blanks between them, OBVIOUSLY!).
     */
    public static void main(String[] args) {
        try {
            // define arguments of program:
            
            String[] argItemIds = DEF_ITEM_IDS;
            Date argDate = DEF_DATE;
            //int argRepMode = DEF_REP_MODE;
            String argMailTo = DEF_MAIL_TO;
            String argMailBcc = DEF_MAIL_BCC;
            
            if (args.length >= 1) {
                argItemIds = args[ARG_IDX_ITEM_IDS].split(";");
            }
            if (args.length >= 2) {
                String dateArg = args[ARG_IDX_DATE];
                switch (dateArg) {
                    case ARG_DATE_TODAY:
                        argDate = SLibTimeUtils.convertToDateOnly(new Date());
                        break;
                    case ARG_DATE_YESTERDAY:
                        argDate = SLibTimeUtils.convertToDateOnly(SLibTimeUtils.addDate(new Date(), 0, 0, -1));
                        break;
                    default:
                        argDate = SLibUtils.DateFormatDate.parse(dateArg); // date format: yyyy-mm-dd
                }
            }
            //if (args.length >= 3) {
            //    argRepMode = Integer.parseInt(args[ARG_IDX_REP_MODE]);
            //}
            if (args.length >= 3) {
                argMailTo = args[ARG_IDX_MAIL_TO];
            }
            if (args.length >= 4) {
                argMailBcc = args[ARG_IDX_MAIL_BCC];
            }
            
            // generate mail body:

            SReportHtmlTicketSummaryAlt reportHtml = new SReportHtmlTicketSummaryAlt(SCliUtils.createSession());
            String mailBody = reportHtml.generateReportHtml(argItemIds, argDate, /*argRepMode, */SModSysConsts.SU_TIC_ORIG_SUP, 0);
            
            // send mail report:
            
            String mailSubject = SLibUtils.textToAscii("[SOM ORG.] Resumen báscula fruta " + SLibUtils.DateFormatDate.format(argDate));
            ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(argMailTo, ";")));
            ArrayList<String> recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(argMailBcc, ";")));
            
            SCliUtils.sendMailReport(mailSubject, mailBody, recipientsTo, recipientsBcc, null);
        }
        catch (Exception e) {
            SLibUtils.printException(SCliMailerReportFruitsAltSummary.class.getName(), e);
        }
    }
}
