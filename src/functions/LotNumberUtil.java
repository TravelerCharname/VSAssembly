/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import com.google.gson.Gson;
import static functions.PrimitiveConn.VIBRANT_TEST_TRACKING;
import static functions.PrimitiveConn.ASSEMBLE_SCH;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Properties;
import model.LotInfo;
import model.Product;
import static functions.PrimitiveConn.LOCAL_SCHEMA;
import java.sql.SQLException;
import java.util.ArrayList;
import model.PillarPlateInfo;

/**
 *
 * @author mlei
 */
public class LotNumberUtil {

    private static Properties _latest;  // update when input a new batch
    private static final HashMap<Product, LotInfo> hm_latest = new HashMap<>();  //serves as a HIDDEN cache, invisible to outside classes
    private static final Gson gs = new Gson();
    // keep daily record of lot info in log
    // for history
    // date-time product lot-num quantity

    private static BufferedWriter log;
    private static final String LOG_ROOT = FolderInitializer.LOG_ROOT;//"C:\\Users\\LM&L\\Desktop\\test\\logs\\lot info history.txt";

    public static void log(LotInfo lot) throws IOException {
        File dest = getLogFile(lot);
        log = new BufferedWriter(new FileWriter(dest));
        log.write(gs.toJson(lot, LotInfo.class));
        log.flush();
        log.close();
        log = null;
    }

    public static File getLogFile(LotInfo lot) {
        File dest = new File(LOG_ROOT, DocDateUtil.getfDate() + " " + lot.getProd().name() + " " + lot.getLotNumber() + " " + lot.getTotal() + ".txt");
        return dest;
    }

    public static final String INSERT_FIELDS = " (`product`,`lot_number`,`total`,`assembled`,`approved`,`failed`,`finished`,`test`,`testing`,`scanning`) ";

    /*
    create lot info table
     */
    public static void createLotInfoTable() {
        String sql = "CREATE TABLE `lotinfo` (\n"
                + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `product` varchar(10) DEFAULT NULL,\n"
                + "  `total` int(11) DEFAULT NULL,\n"
                + "  `in_use` int(11) DEFAULT NULL,\n"
                + "  `in_stock` int(11) DEFAULT NULL,\n"
                + "  `assembled` int(11) DEFAULT NULL,\n"
                + "  `approved` int(11) DEFAULT NULL,\n"
                + "  `failed` int(11) DEFAULT NULL,\n"
                + "  `test` int(11) DEFAULT NULL,\n"
                + "  `testing` int(11) DEFAULT NULL,\n"
                + "  `scanning` int(11) DEFAULT NULL,\n"
                + "  `finished` int(11) DEFAULT NULL,\n"
                + "  `time` datetime DEFAULT NULL,\n"
                + "  PRIMARY KEY (`id`)\n"
                + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
    }

    /*
    insert on duplicate update, return the number of rows affected
     */
    public static int insertUpdateLotinfo() {
        return -1;
    }

    /*
    insert on duplicate update, return the number of rows affected
     */
    public static int batchInsertUpdateLotinfo() {
        return -1;
    }

    public static ArrayList<PillarPlateInfo> getAllPlatesForProduct(Product p, boolean isLocal) throws SQLException {
        String schema = (isLocal ? LOCAL_SCHEMA : VIBRANT_TEST_TRACKING);

        String sql = "SELECT * FROM " + schema + ".pillar_plate_info WHERE pillar_plate_id like \"" + p.prefix + "%\" order by pillar_plate_id desc;";
        ResultSet r = PrimitiveConn.generateRecordThrows(schema, sql, isLocal);
        ArrayList<PillarPlateInfo> plates = PillarPlateInfo.plateListFromDB(r);
        return plates;
    }

    /**
     * all plates one plate p product key: product+lotNum, i.e.productName + 3
     * digits if non-exist new & put exists status getLotInfo from map,
     * lotInfo.total++; lotInfo.status++; lotInfo.plates.add(p); test total++;
     *
     * for all lot in map.values insertUpdate(lot)
     *
     * @param prod
     * @param isLocal
     * @throws java.sql.SQLException
     */
    public static void initLotInfoDbForProduct(Product prod, boolean isLocal) throws SQLException {
        HashMap<String, LotInfo> map = new HashMap<>();
        ArrayList<PillarPlateInfo> plates = getAllPlatesForProduct(prod, isLocal);
        String key;
        LotInfo l;
        for (PillarPlateInfo p : plates) {
            if (null == p.getBarcode() || null == p.getBarcode().getProduct() || p.getBarcode().getProduct().equals(Product.TST)) {
                continue;
            }
            key = p.getBarcode().lotNumber; //p.blindLotNumber();
//            System.out.println("lot num " + key);
            l = map.get(key);
            if (null == l) {
                l = new LotInfo(prod, key, new ArrayList<>());
            }
            l.getPlates().add(p);
            if (null == l.getLast_modified()) {
                l.setLast_modified(p.assemble_time);
//                System.out.println(l.getLotNumber()+" last mod set to "+p.assemble_time);
            } else {
                l.setLast_modified((l.getLast_modified().after(p.assemble_time) ? l.getLast_modified() : p.assemble_time));
//                System.out.println(l.getLotNumber()+" last mod set to "+(l.getLast_modified().after(p.assemble_time) ? l.getLast_modified() : p.assemble_time));
            }
            map.put(key, l);
        }
        String schema = (isLocal ? LOCAL_SCHEMA : ASSEMBLE_SCH);
        String sql, values;
        for (LotInfo lot : map.values()) {
            lot.autoCount();
            values = lot.getLotInfoDbEntry();
            sql = "INSERT INTO " + schema + ".lotinfo " + INSERT_FIELDS + " VALUES" + values + ";"; //"; + " ON DUPLICATE KEY UPDATE `name` = VALUES(name)
            int updateRecordThrows = PrimitiveConn.updateRecordThrows(schema, sql, isLocal);
//            System.out.println(updateRecordThrows + " rows affected");
        }

    }

    public static LotInfo getLatestLofInfoCount(Product prod, boolean isLocal) throws SQLException {
        System.out.println("searching latest lot for " + prod.prefix);

        String schema = (isLocal ? LOCAL_SCHEMA : ASSEMBLE_SCH);

        String sql = "SELECT * FROM " + schema + ".lotinfo WHERE product like \"" + prod.plateName + "%\" order by lot_number desc;";
        ResultSet r = PrimitiveConn.generateRecordThrows(schema, sql, isLocal);
        String lotNumber = null;
        LotInfo lotInfo = null;
        while (r.next()) {
            lotNumber = r.getString("lot_number");
            System.out.print(lotNumber + " ");
            try {
                if ((Integer.parseInt(lotNumber) >= 0 && Integer.parseInt(lotNumber) <= 1000) || (Integer.parseInt(lotNumber) >= 8000 && Integer.parseInt(lotNumber) <= 9000)) {
                    System.out.println("Bingo! " + lotNumber);
                    lotInfo = new LotInfo(prod, lotNumber, null);
                    lotInfo.setCount(r.getInt("approved"), r.getInt("assembled"), r.getInt("failed"), r.getInt("finished"), r.getInt("scanning"), r.getInt("test"), r.getInt("testing"), r.getInt("total"));

                    lotInfo.show();
                    break;
                }
            } catch (NumberFormatException e) {
            }
        }
        if (null == lotInfo) {
            System.out.println("can't find the latest lot info for " + prod.plateName);
        }
        return lotInfo;
    }
}
