/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbUtil;

import functions.DocDateUtil;
import functions.PrimitiveConn;
import static functions.PrimitiveConn.LOCAL_SCHEMA;
import static functions.PrimitiveConn.VIBRANT_TEST_TRACKING;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
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
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    /*
    1. time line to est lot/batch
    2. correct mis-formated plate id according to time lime
    3. insert clean record to local
    */
    
    public static void establishTimeLine(){
        // lotnum from-id from-time to-id to-time
        
        // loop plate
        ///1. parse plate id into
        ///   prod lot batch plate
        
        ///2. assemble-time
        
        ///3. time-line v1: batch time line
        //  time -> prod -> same -> extend
        //               -> diff -> break/new check point
    }
    
    public static ArrayList<PillarPlateInfo> getAllPlatesForProduct(boolean isLocal) throws SQLException {
        String schema = (isLocal ? LOCAL_SCHEMA : VIBRANT_TEST_TRACKING);

        String sql = "SELECT * FROM " + schema + ".pillar_plate_info order by assemble_time,pillar_plate_id;";
        ResultSet r = PrimitiveConn.generateRecordThrows(schema, sql, isLocal);

        ArrayList<PillarPlateInfo> plates = PillarPlateInfo.plateListFromDB(r);
        ArrayList<LotInfo> al=new ArrayList<>();
        
        Date curr_lot_time=Date.from(Instant.parse("2007-01-01")),plate_time;
        LotInfo curr_lot = null;
        for(PillarPlateInfo plate:plates){
            Product product = plate.getBarcode().getProduct();
            if(null==product||Product.TST.equals(product)) continue;
            
            plate_time = plate.assemble_time;
            if(null==curr_lot){
                curr_lot=new LotInfo(product, plate.getBarcode().lotNumber, new ArrayList<PillarPlateInfo>());
                curr_lot.getPlates().add(plate);
            }
            // |time-curr_lot_time|=time-curr_lot_time>1d 
            /// => new batchinsert and reset curr_lot_time & curr_lot
            // |time-curr_lot_time|=time-curr_lot_time<1d
            /// same prod
            //// curr_lot.plates.add(plate)
            
            /// diff prod
            //// => new batchinsert and reset curr_lot_time & curr_lot
        }

        // insert lot + plate
        // prod lot from(pk) barcode prod plot pbatch pplateid passemble time
        return plates;
    }
    
    //product, lot number, from, barcode, prefix, lot, batch, plate id, assembled
    LotInfo newLotWithPlate(PillarPlateInfo plate){
        if(null==plate) return null;
        LotInfo lot=new LotInfo(plate.getBarcode().getProduct(), plate.getBarcode().lotNumber, new ArrayList<PillarPlateInfo>());
        return lot;
    }
    LotInfo newLotWithPlate(PillarPlateInfo plate,Product prod){
        if(null==plate) return null;
        LotInfo lot=new LotInfo(prod, plate.getBarcode().lotNumber, new ArrayList<PillarPlateInfo>());
        return lot;
    }
    
    
    
    class LotInfoExt extends LotInfo{
        public java.util.Date establish;
        void addPlateToLot(LotInfo lot,PillarPlateInfo plate){
//update est       
//if(null==this.establish)

//insert plate
    }
    }
}
