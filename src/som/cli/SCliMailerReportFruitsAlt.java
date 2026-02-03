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
import sa.lib.SLibUtils;
import som.mod.SModSysConsts;

/**
 * CLI mailer of report of monthly reception of ORGANIC fruits at scale (in HTML format).
 * "Histórico mensual báscula fruta"
 * 
 * @author Sergio Flores, Isabel García, Sergio Flores
 */
public class SCliMailerReportFruitsAlt {
    
    /** Argument index for list of ID of items. */
    private static final int ARG_IDX_ITEM_IDS = 0;
    /** Argument index for year base. */
    private static final int ARG_IDX_YEAR_BASE = 1;
    /** Argument index for interval days for invocation of this report mailer. */
    private static final int ARG_IDX_INTVL_DAYS = 2;
    /** Argument index for list of mail-To recipients. */
    private static final int ARG_IDX_MAIL_TO = 3;
    /** Argument index for list of mail-Bcc recipients. */
    private static final int ARG_IDX_MAIL_BCC = 4;

    private static final String[] DEF_ITEM_IDS = new String[] { SCliConsts.IDS_PAIR_AVO_FRUIT_CNV_ORG };
    private static final int DEF_YEAR_BASE = 2010; // año/temporada tope hacia atrás
    //private static final int DEF_YEAR_REF = 5; // comparativa de 5 años hacia atrás, además del año/temporada actual
    private static final int DEF_INTVL_DAYS = 7; // intervalo de días entre invocaciones de este de despachador de reportes
    private static final String DEF_MAIL_TO = "sflores@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "isabel.garcia@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "isabel.garcia@swaplicado.com.mx;sflores@swaplicado.com.mx";
    //private static final String DEF_MAIL_TO = "gortiz@aeth.mx";
    //private static final String DEF_MAIL_TO = "gortiz@aeth.mx;sflores@swaplicado.com.mx";
    private static final String DEF_MAIL_BCC = "sflores.swaplicado@gmail.com";
    //private static final String DEF_MAIL_BCC = "isabel.garcia@swaplicado.com.mx";

    /**
     * @param args the command line arguments.
     * Five arguments expected:
     * 1: List of ID of items to be reported, passed in pairs, in format ID_CONVENTIONAL-ID_ORGANIC, e.g., 6-64 (separated with semicolon, without blanks between them, OBVIOUSLY!)
     * 2: Year base. Can be one out of two elegible types of values: 1) if it is a 4-digit year and greater or equal than 2001, it is the year to start from; otherwise 2) it is the number of history years besides current year.
     * 3: Interval of days for invocation of this report mailer.
     * 4: List of mail-To recipients (separated with semicolon, without blanks between them, OBVIOUSLY!).
     * 5: List of mail-Bcc recipients (separated with semicolon, without blanks between them, OBVIOUSLY!).
     */
    public static void main(String[] args) {
        try {
            // define program arguments:
            
            String[] argItemIds = DEF_ITEM_IDS;
            int argYearBase = DEF_YEAR_BASE;
            int argIntvlDays = DEF_INTVL_DAYS;
            String argMailTo = DEF_MAIL_TO;
            String argMailBcc = DEF_MAIL_BCC;
            
            if (args.length >= 1) {
                argItemIds = args[ARG_IDX_ITEM_IDS].split(";");
            }
            if (args.length >= 2) {
                argYearBase = SLibUtils.parseInt(args[ARG_IDX_YEAR_BASE]);
            }
            if (args.length >= 3) {
                argIntvlDays = SLibUtils.parseInt(args[ARG_IDX_INTVL_DAYS]);
            }
            if (args.length >= 4) {
                argMailTo = args[ARG_IDX_MAIL_TO];
            }
            if (args.length >= 5) {
                argMailBcc = args[ARG_IDX_MAIL_BCC];
            }
            
            // generate report:

            Date now = new Date();
            Date cutoff = now;
            
            SReportHtmlTicketSeasonMonthAlt reportHtml = new SReportHtmlTicketSeasonMonthAlt(SCliUtils.createSession());
            String mailBody = reportHtml.generateReportHtml(argItemIds, argYearBase, argIntvlDays, SCliConsts.FRUIT_SEASON_FIRST_MONTH, SCliConsts.FRUIT_MONTH_FIRST_DAY, cutoff, now, SModSysConsts.SU_TIC_ORIG_SUP, 0, SReportHtmlTicketSeasonMonthAlt.MODE_UNIT_TON);
            
            // send mail report:
            
            String mailSubject = SLibUtils.textToAscii("[SOM ORG.] Histórico mensual báscula fruta " + SLibUtils.DateFormatDate.format(now));
            ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(argMailTo, ";")));
            ArrayList<String> recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(argMailBcc, ";")));
            
            SCliUtils.sendMailReport(mailSubject, mailBody, recipientsTo, recipientsBcc, null);
        }
        catch (Exception e) {
            SLibUtils.printException("main()", e);
        }
    }
}
