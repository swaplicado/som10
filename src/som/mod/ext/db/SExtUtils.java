/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.ext.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiUtils;
import som.gui.SGuiClientSessionCustom;
import som.mod.SModConsts;
import som.mod.SModSysConsts;
import som.mod.som.db.SDbExternalWarehouse;
import som.mod.som.db.SDbItem;
import som.mod.som.db.SDbProducer;
import som.mod.som.db.SRowMgmtPayment;

/**
 *
 * @author Sergio Flores
 */
public abstract class SExtUtils {

    public static void populateCataloguesUnits(SGuiClient client, JComboBox<SGuiItem> comboBox) {
        String sql = "";
        ResultSet resultSet = null;
        SGuiItem guiItem = null;
        Vector<SGuiItem> items = null;

        try {
            SGuiUtils.setCursorWait(client);

            items = new Vector<>();
            sql = "SELECT id_unit, unit FROM itmu_unit WHERE b_del = 0 ";

            resultSet = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getExtStatement().executeQuery(sql);
            items.add(new SGuiItem("- Unidad sistema externo -"));

            while (resultSet.next()) {
                guiItem = new SGuiItem(new int[] { resultSet.getInt("id_unit") }, resultSet.getString("unit"));

                items.add(guiItem);
            }

            comboBox.removeAllItems();

            for (SGuiItem item : items) {
                comboBox.addItem(item);
            }
        }
        catch (SQLException e) {
            SLibUtils.showException(null, e);
            SLibUtils.printSqlQuery(null, sql);
        }
        catch (Exception e) {
            SLibUtils.showException(null, e);
        }
        finally {
            SGuiUtils.setCursorDefault(client);
        }
    }

    public static void populateCataloguesItems(SGuiClient client, JComboBox<SGuiItem> comboBox) {
        String sql = "";
        ResultSet resultSet = null;
        SGuiItem guiItem = null;
        Vector<SGuiItem> items = null;

        try {
            SGuiUtils.setCursorWait(client);

            items = new Vector<>();
            sql = "SELECT i.id_item, i.item_key, i.item, i.fid_unit, u.symbol " +
                    "FROM itmu_item AS i " +
                    "INNER JOIN itmu_unit AS u ON i.fid_unit = u.id_unit " +
                    "WHERE i.b_del = 0 AND i.b_inv = 1 " +
                    "ORDER BY i.item, i.id_item ";

            resultSet = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getExtStatement().executeQuery(sql);
            items.add(new SGuiItem("- Ítem sistema externo -"));

            while (resultSet.next()) {
                guiItem = new SGuiItem(new int[] { resultSet.getInt("i.id_item") }, resultSet.getString("i.item"));

                guiItem.setCode(resultSet.getString("i.item_key"));
                guiItem.setComplement(resultSet.getInt("i.fid_unit"));

                items.add(guiItem);
            }

            comboBox.removeAllItems();

            for (SGuiItem item : items) {
                comboBox.addItem(item);
            }
        }
        catch (SQLException e) {
            SLibUtils.showException(null, e);
            SLibUtils.printSqlQuery(null, sql);
        }
        catch (Exception e) {
            SLibUtils.showException(null, e);
        }
        finally {
            SGuiUtils.setCursorDefault(client);
        }
    }

    public static void populateCataloguesWarehouses(SGuiClient client, JComboBox<SGuiItem> comboBox) {
        String sql = "";
        ResultSet resultSet = null;
        SGuiItem guiItem = null;
        Vector<SGuiItem> items = null;

        try {
            SGuiUtils.setCursorWait(client);

            items = new Vector<>();
            sql = "SELECT id_cob, id_ent, ent, code " +
                    "FROM cfgu_cob_ent " +
                    "WHERE b_del = 0 AND fid_ct_ent = 2 " +
                    "ORDER BY id_cob, id_ent, ent, code ";

            resultSet = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getExtStatement().executeQuery(sql);
            items.add(new SGuiItem("- Almacén sistema externo -"));

            while (resultSet.next()) {
                guiItem = new SGuiItem(new int[] { resultSet.getInt("id_cob"), resultSet.getInt("id_ent") }, resultSet.getString("ent"));
                guiItem.setCode(resultSet.getString("code"));

                items.add(guiItem);
            }

            comboBox.removeAllItems();

            for (SGuiItem item : items) {
                comboBox.addItem(item);
            }
        }
        catch (SQLException e) {
            SLibUtils.showException(null, e);
            SLibUtils.printSqlQuery(null, sql);
        }
        catch (Exception e) {
            SLibUtils.showException(null, e);
        }
        finally {
            SGuiUtils.setCursorDefault(client);
        }
    }

    public static void populateCataloguesProducers(SGuiClient client, JComboBox<SGuiItem> comboBox) {
        String sql = "";
        ResultSet resultSet = null;
        SGuiItem guiItem = null;
        Vector<SGuiItem> items = null;

        try {
            SGuiUtils.setCursorWait(client);

            items = new Vector<>();
            sql = "SELECT bp.id_bp, bp.bp, bp.fiscal_id, ct.bp_key " +
                   "FROM erp.bpsu_bp AS bp " +
                   "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp " +
                   "WHERE bp.b_del = 0 AND ct.b_del = 0 AND ct.id_ct_bp = 2 " +
                   "ORDER BY bp.bp, ct.bp_key, bp.id_bp ";

            resultSet = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getExtStatement().executeQuery(sql);
            items.add(new SGuiItem("- Proveedor sistema externo -"));

            while (resultSet.next()) {
                guiItem = new SGuiItem(new int[] { resultSet.getInt("bp.id_bp") }, resultSet.getString("bp.bp"));
                guiItem.setCode(resultSet.getString("ct.bp_key"));
                guiItem.setComplement(resultSet.getString("bp.fiscal_id"));

                items.add(guiItem);
            }

            comboBox.removeAllItems();

            for (SGuiItem item : items) {
                comboBox.addItem(item);
            }
        }
        catch (SQLException e) {
            SLibUtils.showException(null, e);
            SLibUtils.printSqlQuery(null, sql);
        }
        catch (Exception e) {
            SLibUtils.showException(null, e);
        }
        finally {
            SGuiUtils.setCursorDefault(client);
        }
    }

    public static Vector<SGridRow> polulatePayments(SGuiClient client, int year, int prod) throws SQLException {
        Vector<SGridRow> gridRows = new Vector<>();
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, re.*, a.acc, cu.cur_key, mcls.cls_acc_mov, un.usr, ue.usr, ud.usr, c.cc, " +
                "bkc.code, cob.code, e.ent, " +
                "CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_per, " +
                "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, 6)) as f_num, " +
                "SUM(re.debit) AS f_debit, SUM(re.credit) AS f_credit, SUM(re.debit) - SUM(re.credit) AS f_balance " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_bkc AS bkc ON " +
                "r.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "r.fid_cob = cob.id_bpb " +
                "LEFT OUTER JOIN fin_rec_ety AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND re.b_del = 0 " +
                "LEFT OUTER JOIN fin_acc AS a ON re.fid_acc = a.id_acc " +
                "LEFT OUTER JOIN erp.cfgu_cur AS cu ON re.fid_cur = cu.id_cur " +
                "LEFT OUTER JOIN erp.fins_tp_acc_mov AS mtp ON re.fid_tp_acc_mov = mtp.id_tp_acc_mov " +
                "LEFT OUTER JOIN erp.fins_cl_acc_mov AS mcl ON re.fid_tp_acc_mov = mcl.id_tp_acc_mov AND re.fid_cl_acc_mov = mcl.id_cl_acc_mov " +
                "LEFT OUTER JOIN erp.fins_cls_acc_mov AS mcls ON re.fid_tp_acc_mov = mcls.id_tp_acc_mov AND re.fid_cl_acc_mov = mcls.id_cl_acc_mov AND re.fid_cls_acc_mov = mcls.id_cls_acc_mov " +
                "LEFT OUTER JOIN erp.usru_usr AS un ON re.fid_usr_new = un.id_usr " +
                "LEFT OUTER JOIN erp.usru_usr AS ue ON re.fid_usr_edit = ue.id_usr " +
                "LEFT OUTER JOIN erp.usru_usr AS ud ON re.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN fin_cc AS c ON re.fid_cc_n = c.id_cc " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS e ON re.fid_cob_n = e.id_cob AND re.fid_ent_n = e.id_ent " +
                "LEFT OUTER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                "LEFT OUTER JOIN erp.itmu_item AS i ON re.fid_item_n = i.id_item " +
                "LEFT OUTER JOIN erp.finu_tax AS t ON re.fid_tax_bas_n = t.id_tax_bas AND re.fid_tax_n = t.id_tax " +
                "WHERE r.b_del = 0 AND r.id_year = " + year + " AND re.fid_tp_acc_mov <> " + SModSysConsts.EXT_FINS_TP_ACC_MOV_FY_OPEN + " AND re.fid_ct_sys_mov_xxx = " + SModSysConsts.EXT_FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SModSysConsts.EXT_FINS_TP_SYS_MOV_BPS_SUP[1] + " AND re.fid_bp_nr = " + prod + " " +
                "GROUP BY r.id_year, r.id_per, bkc.code, r.id_bkc, r.id_tp_rec, r.id_num,re.id_ety " +
                "ORDER BY r.id_year, r.id_per, bkc.code, r.id_bkc, r.id_tp_rec, r.id_num, r.dt ";

        resultSet = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getExtStatementCo().executeQuery(sql);

        while (resultSet.next()) {
            SRowMgmtPayment payment = new SRowMgmtPayment();

            payment.setPeriod(resultSet.getString("f_per"));
            payment.setBkc(resultSet.getString("bkc.code"));
            payment.setBranch(resultSet.getString("cob.code"));
            payment.setRecord(resultSet.getString("f_num"));
            payment.setDate(resultSet.getDate("r.dt"));
            payment.setAccountNumber(resultSet.getString("re.fid_acc"));
            payment.setAccount(resultSet.getString("a.acc"));
            payment.setConcept(resultSet.getString("re.concept"));
            payment.setDebit(resultSet.getDouble("re.debit"));
            payment.setCredit(resultSet.getDouble("re.credit"));
            payment.setExchangeRate(resultSet.getDouble("re.exc_rate"));
            payment.setDebitCy(resultSet.getDouble("re.debit_cur"));
            payment.setCreditCy(resultSet.getDouble("re.credit_cur"));
            payment.setCurrencyKey(resultSet.getString("cu.cur_key"));
            payment.setSystem(resultSet.getBoolean("re.b_sys"));
            payment.setAccountingMoveSubclass(resultSet.getString("mcls.cls_acc_mov"));
            payment.setCostCenterId_n(resultSet.getString("re.fid_cc_n"));
            if (resultSet.wasNull()) payment.setCostCenterId_n("");
            payment.setCostCenter_n(resultSet.getString("c.cc"));
            if (resultSet.wasNull()) payment.setCostCenter_n("");
            payment.setDeleted(resultSet.getBoolean("re.b_del"));
            payment.setUserNew(resultSet.getString("un.usr"));
            payment.setTsUserNew(resultSet.getTimestamp("re.ts_new"));
            payment.setUserEdit(resultSet.getString("ue.usr"));
            payment.setTsUserEdit(resultSet.getTimestamp("re.ts_edit"));
            payment.setUserDel(resultSet.getString("ud.usr"));
            payment.setTsUserDel(resultSet.getTimestamp("re.ts_del"));

            gridRows.add(payment);
        }

        return gridRows;
    }

    public static void updateCatalogues(SGuiClient client) {
        String sql = "";
        int[] key = null;
        Vector<Integer> vItems = null;
        Vector<Integer> vProducers = null;
        Vector<int []> vExtWarehouses = null;
        ResultSet resultSet = null;
        SDbItem item = null;
        SDbProducer producer = null;
        SDbExternalWarehouse extWarehouse = null;

        if (client.showMsgBoxConfirm("¿Está seguro(a) que desea actualizar los catálogos del sistema externo?") == JOptionPane.YES_OPTION) {
            key = new int[2];
            vItems = new Vector<>();
            vProducers = new Vector<>();
            vExtWarehouses = new Vector<>();
            item = new SDbItem();
            producer = new SDbProducer();
            extWarehouse = new SDbExternalWarehouse();

            try {
                SGuiUtils.setCursorWait(client);

                sql = "SELECT fk_ext_item_n " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.SU_ITEM) + " ";

                resultSet = client.getSession().getStatement().executeQuery(sql);
                while (resultSet.next()) {
                    vItems.add(resultSet.getInt("fk_ext_item_n"));
                }

                for (int i = 0; i < vItems.size(); i++) {
                    sql = "SELECT i.id_item, i.item_key, i.item " +
                            "FROM itmu_item AS i " +
                            "WHERE i.id_item = " + vItems.get(i) + " ";

                    resultSet = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getExtStatement().executeQuery(sql);
                    while (resultSet.next()) {
                        item.saveField(client.getSession().getStatement(), new int[] { vItems.get(i) }, SDbItem.FIELD_EXTERNAL_CODE, resultSet.getString("i.item_key"));
                        item.saveField(client.getSession().getStatement(), new int[] { vItems.get(i) }, SDbItem.FIELD_EXTERNAL_NAME, resultSet.getString("i.item"));
                    }
                }

                sql = "SELECT fk_ext_prod_n " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.SU_PROD) + " ";

                resultSet = client.getSession().getStatement().executeQuery(sql);
                while (resultSet.next()) {
                    vProducers.add(resultSet.getInt("fk_ext_prod_n"));
                }

                for (int i = 0; i < vProducers.size(); i++) {
                    sql = "SELECT bp.id_bp, bp.bp, bp.bp_comm, bp.fiscal_id, ct.bp_key " +
                            "FROM erp.bpsu_bp AS bp " +
                            "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp " +
                            "WHERE ct.id_ct_bp = 2 AND bp.id_bp = " + vProducers.get(i) + " ";

                    resultSet = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getExtStatement().executeQuery(sql);

                    while (resultSet.next()) {
                        System.out.println("bp_key: " + resultSet.getString("ct.bp_key") + "; bp: " + resultSet.getString("bp.bp") + "; bp_comm: " + resultSet.getString("bp.bp_comm") + "; fiscal_id: " + resultSet.getString("bp.fiscal_id") + ".");
                        producer.saveField(client.getSession().getStatement(), new int[] { vProducers.get(i) }, 
                                SDbProducer.FIELD_CODE, resultSet.getString("ct.bp_key"));
                        producer.saveField(client.getSession().getStatement(), new int[] { vProducers.get(i) }, 
                                SDbProducer.FIELD_NAME, resultSet.getString("bp.bp"));
                        producer.saveField(client.getSession().getStatement(), new int[] { vProducers.get(i) }, 
                                SDbProducer.FIELD_FISCAL_ID, resultSet.getString("bp.fiscal_id").length() <= SDbProducer.LEN_FISCAL_ID ? resultSet.getString("bp.fiscal_id") : resultSet.getString("bp.fiscal_id").substring(0, SDbProducer.LEN_FISCAL_ID));
                    }
                }

                sql = "SELECT fk_ext_cob_n, fk_ext_ent_n " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.SU_EXT_WAH) + " ";

                resultSet = client.getSession().getStatement().executeQuery(sql);
                while (resultSet.next()) {
                    key[0] = resultSet.getInt("fk_ext_cob_n");
                    key[1] = resultSet.getInt("fk_ext_ent_n");

                    vExtWarehouses.add(key);
                }

                for (int i = 0; i < vExtWarehouses.size(); i++) {
                    sql = "SELECT ent, code " +
                        "FROM cfgu_cob_ent " +
                        "WHERE b_del = 0 AND fid_ct_ent = 2 AND id_cob = " + vExtWarehouses.get(i)[0] + " AND id_ent = " + vExtWarehouses.get(i)[1] + " ";

                    resultSet = ((SGuiClientSessionCustom) client.getSession().getSessionCustom()).getExtStatement().executeQuery(sql);

                    while (resultSet.next()) {
                        extWarehouse.saveField(client.getSession().getStatement(), vExtWarehouses.get(i), SDbProducer.FIELD_CODE, resultSet.getString("code"));
                        extWarehouse.saveField(client.getSession().getStatement(), vExtWarehouses.get(i), SDbProducer.FIELD_NAME, resultSet.getString("ent"));
                    }
                }

                client.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED);
                client.getSession().notifySuscriptors(SModConsts.SU_ITEM);
                client.getSession().notifySuscriptors(SModConsts.SU_PROD);
                client.getSession().notifySuscriptors(SModConsts.SU_EXT_WAH);
            }
            catch (SQLException e) {
                SLibUtils.showException(null, e);
                SLibUtils.printSqlQuery(null, sql);
            }
            catch (Exception e) {
                SLibUtils.showException(null, e);
            }
            finally {
                SGuiUtils.setCursorDefault(client);
            }
        }
    }
}
