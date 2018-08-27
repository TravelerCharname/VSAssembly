/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbUtil;

import functions.DocDateUtil;
import functions.PrimitiveConn;
import static functions.PrimitiveConn.ASSEMBLE_SCH;
import static functions.PrimitiveConn.LOCAL_SCHEMA;
import static functions.PrimitiveConn.VIBRANT_TEST_TRACKING;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.TreeMap;
import model.LotInfo;
import model.PillarPlateInfo;
import model.Product;

/**
 *
 * @author LM&L
 */
public class PillarPlateCleaner {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        ArrayList<LotInfo> al = getAllPlatesForProduct(true);
        show(al);
        
        batchInsertUpdateLotinfo(al, true);
    }

    /*
    1. time line to est lot/batch
    2. correct mis-formated plate id according to time lime
    3. insert clean record to local
     */
    public static void establishTimeLine() {
        // lotnum from-id from-time to-id to-time

        // loop plate
        ///1. parse plate id into
        ///   prod lot batch plate
        ///2. assemble-time
        ///3. time-line v1: batch time line
        //  time -> prod -> same -> extend
        //               -> diff -> break/new check point
    }

    public static ArrayList<LotInfo> getAllPlatesForProduct(boolean isLocal) throws SQLException {
        String schema = (isLocal ? LOCAL_SCHEMA : VIBRANT_TEST_TRACKING);

        String sql = "SELECT * FROM " + schema + ".pillar_plate_info order by assemble_time,pillar_plate_id;";
        ResultSet r = PrimitiveConn.generateRecordThrows(schema, sql, isLocal);

        ArrayList<PillarPlateInfo> plates = PillarPlateInfo.plateListFromDB(r);
        ArrayList<LotInfo> al = new ArrayList<>();

        Date curr_lot_time = Date.from(Instant.parse("2007-01-01T10:15:30Z")), plate_time;
        LotInfoExt curr_lot = null;
        for (PillarPlateInfo plate : plates) {
            Product product = plate.getBarcode().getProduct();
            if (null == product || Product.TST.equals(product)) {
                continue;
            }

            plate_time = plate.assemble_time;
            if (null == curr_lot) {
//                curr_lot = new LotInfo(product, plate.getBarcode().lotNumber, new ArrayList<PillarPlateInfo>());
//                curr_lot.getPlates().add(plate);
                curr_lot = newLotWithPlate(plate);
                curr_lot.addPlateToLot(plate);
                curr_lot_time = plate_time;

                al.add(curr_lot);
                System.out.println("add curr to list @"+plate_time+" "+product.prefix);
                continue;
            }

            // same prod
/// |time-curr_lot_time|=time-curr_lot_time<1d
//// curr_lot.plates.add(plate)
            if (curr_lot.getProd().equals(product)) {
                if (curr_lot_time.getDay() == plate_time.getDay()) {
                    curr_lot.addPlateToLot(plate);
                }
                continue;
            }
// |time-curr_lot_time|=time-curr_lot_time>1d 
            /// => new batchinsert and reset curr_lot_time & curr_lot
/// diff prod

            //// => new batchinsert and reset curr_lot_time & curr_lot
            
            curr_lot = newLotWithPlate(plate);
            curr_lot.addPlateToLot(plate);
            curr_lot_time = plate_time;
            al.add(curr_lot);

        System.out.println("add curr to list @"+curr_lot.establish+" "+curr_lot.getProd().prefix);          
        }
        
        // insert lot + plate
        // prod lot from(pk) barcode prod plot pbatch pplateid passemble time
        return al;
    }

    //product, lot number, from, barcode, prefix, lot, batch, plate id, assembled
    static LotInfoExt newLotWithPlate(PillarPlateInfo plate) {
        if (null == plate) {
            return null;
        }
        LotInfoExt lot = new LotInfoExt(plate.getBarcode().getProduct(), plate.getBarcode().lotNumber, new ArrayList<PillarPlateInfo>());
        return lot;
    }

    static LotInfoExt newLotWithPlate(PillarPlateInfo plate, Product prod) {
        if (null == plate) {
            return null;
        }
        LotInfoExt lot = new LotInfoExt(prod, plate.getBarcode().lotNumber, new ArrayList<PillarPlateInfo>());
        return lot;
    }

    static void show(ArrayList<LotInfo> al){
        if(null==al) return;
        ListIterator<LotInfo> it = al.listIterator();
        LotInfo lot; int i=0;
        while(it.hasNext()){
            lot = it.next();
            System.out.println(i+++"\t"+((LotInfoExt) lot).establish+"\t"+lot.getProd().prefix+"\t"+lot.getLotNumber());
        }
    }
    
    /*
    insert on duplicate update, return the number of rows affected
     */
    private static String INSERT_FIELDS =" (`product` , `lot number` , `from` , `barcode` , `prefix` , `lot` , `batch` , `plate id` , `assembled`) ";

    public static int batchInsertUpdateLotinfo(Collection<LotInfo> collection, boolean isLocal) {
        String schema = (isLocal ? LOCAL_SCHEMA : ASSEMBLE_SCH);
        String sql, values;
        int count = 0, updateRecordThrows;
        for (LotInfo lot : collection) {
            lot.autoCount();
            for(PillarPlateInfo plate:lot.getPlates()){
            values = getLotInfoDbEntry(lot, plate);
            sql = "INSERT INTO " + schema + ".plate_info_cleaner " + INSERT_FIELDS + " VALUES" + values
                    + "on duplicate key update "
                    + "`product`=values(`product`),"
                    + "`lot number`=values(`lot number`),"
                    + "`from`=values(`from`),"
                    + "`barcode`=values(`barcode`),"
                    + "`prefix`=values(`prefix`),"
                    + "`lot`=values(`lot`),"
                    + "`batch`=values(`batch`),"
                    + "`plate id`=values(`plate id`),"
                    + "`assembled`=values(`assembled`);"; //"; + " ON DUPLICATE KEY UPDATE `name` = VALUES(name)
//            System.out.println("updating " + lot.getProd() + " lot " + lot.getLotNumber());
            updateRecordThrows = PrimitiveConn.updateRecordThrows(schema, sql, isLocal);
            }
//            System.out.println(updateRecordThrows + " rows affected");
//            count += updateRecordThrows;
        }
        return count;
    }
    
    //date and time!
    //hash plate and set hash as pk
    public static String getLotInfoDbEntry(LotInfo aLot,PillarPlateInfo plate) {
//        " (`product` , `lot number` , `from` , `barcode` , `prefix` , `lot` , `batch` , `plate id` , `assembled`) ";
        return "('"+aLot.getProd().plateName + "','" +aLot.getLotNumber() + "','" +((LotInfoExt)aLot).establish+ "','" +aLot.getFrom()+ "','" + plate.getBarcode().getProduct().prefix+ "','" + plate.getBarcode().lotNumber+  "','" + plate.getBarcode().batchNumber+ "','"  + plate.getBarcode().plateId + "','" +plate.assemble_time+"')";
    }
}
