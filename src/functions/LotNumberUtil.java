/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import com.google.gson.Gson;
import static functions.PrimitiveConn.VIBRANT_TEST_TRACKING;
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

/**
 *
 * @author mlei
 */
public class LotNumberUtil {
    private static Properties _latest;  // update when input a new batch
    private static final HashMap<Product,LotInfo> hm_latest=new HashMap<>();  //serves as a HIDDEN cache, invisible to outside classes
    private static final Gson gs = new Gson();
    // keep daily record of lot info in log
    // for history
    // date-time product lot-num quantity
    
    private static BufferedWriter log;
    private static final String LOG_ROOT=FolderInitializer.LOG_ROOT;//"C:\\Users\\LM&L\\Desktop\\test\\logs\\lot info history.txt";
    public static void log(LotInfo lot) throws IOException{
        File dest=getLogFile(lot);
        log=new BufferedWriter(new FileWriter(dest));
        log.write(gs.toJson(lot, LotInfo.class));
        log.flush();
        log.close();log=null;
    }

    public static File getLogFile(LotInfo lot) {
        File dest=new File(LOG_ROOT,DocDateUtil.getfDate()+" "+lot.getProd().name()+" "+lot.getLotNumber()+" "+lot.getTotal()+".txt");
        return dest;
    }

    /*
    create lot info table
    */
    public static void createLotInfoTable(){
        String sql="CREATE TABLE `lotinfo` (\n" +
"  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
"  `product` varchar(10) DEFAULT NULL,\n" +
"  `total` int(11) DEFAULT NULL,\n" +
"  `in_use` int(11) DEFAULT NULL,\n" +
"  `in_stock` int(11) DEFAULT NULL,\n" +
"  `assembled` int(11) DEFAULT NULL,\n" +
"  `approved` int(11) DEFAULT NULL,\n" +
"  `failed` int(11) DEFAULT NULL,\n" +
"  `test` int(11) DEFAULT NULL,\n" +
"  `testing` int(11) DEFAULT NULL,\n" +
"  `scanning` int(11) DEFAULT NULL,\n" +
"  `finished` int(11) DEFAULT NULL,\n" +
"  `time` datetime DEFAULT NULL,\n" +
"  PRIMARY KEY (`id`)\n" +
") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
    }
    
    /*
    insert on duplicate update, return the number of rows affected
    */
    public static int insertUpdateLotinfo(){
        return -1;
    }
    /*
    insert on duplicate update, return the number of rows affected
    */
    public static int batchInsertUpdateLotinfo(){
        return -1;
    }
    
    public static String[] getLotNumbers(Product p){
        return null;
    }
    
    /*
    all plates
    one plate p
        product
            key: product+lotNum, i.e.productName + 3 digits
            if non-exist
                new & put
            exists
                status
                    getLotInfo from map, lotInfo.total++; lotInfo.status++;
            lotInfo.plates.add(p);
        test
            total++;
    
    for all lot in map.values
        insertUpdate(lot)
    */
    public static void initLotInfoDbForProduct(Product p){
        
    }
    
    public static ResultSet lotInfoByProduct(Product p, boolean isLocal) {
        String schema=(isLocal?LOCAL_SCHEMA:VIBRANT_TEST_TRACKING);
        
        String sql = "SELECT * FROM "+schema+".pillar_plate_info WHERE pillar_plate_id like \"" + p.prefix + "%\" order by pillar_plate_id desc;";
        ResultSet result=PrimitiveConn.generateRecordThrows(schema, sql, isLocal);
        return result;
    }
}
