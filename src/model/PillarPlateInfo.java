/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import functions.DocDateUtil;
//import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;

/**
 *
 * @author mlei
 */
public class PillarPlateInfo {
    public String pillar_plate_id;
    public String inventory_barcode;
    public String plate_type;
    public String chip_layout_type;
    public String test_name;
    public java.util.Date assemble_time;
    public Status status;
    
    // left NULL
    public String plate_seq_num;
    public String TSP;
    public String well_plate_id;

    private AssayBarcode barcode;
//    private Product product;
//    private String lotNumber;
//    private String batchNumber;
//    private String plateId;
    
    public PillarPlateInfo(String pillar_plate_id, String inventory_barcode, String plate_type, String chip_layout_type, String test_name, 
            java.util.Date assemble_time, String status, String plate_seq_num, String TSP, String well_plate_id) {
        this.pillar_plate_id = filter_trim(pillar_plate_id);
        this.inventory_barcode = filter_trim(inventory_barcode);
        this.plate_type = filter_trim(plate_type);
        this.chip_layout_type = filter_trim(chip_layout_type);
        this.test_name = filter_trim(test_name);
        this.assemble_time = assemble_time;
        this.status = Status.getStatusByName(filter_trim(status));
        this.plate_seq_num = filter_trim(plate_seq_num);
        this.TSP = TSP;
        this.well_plate_id = filter_trim(well_plate_id);
        
        this.init();
    }
    private String filter_trim(String fromDB){
        return (fromDB==null?null:fromDB.trim());
    }
    public static ArrayList<PillarPlateInfo> plateListFromDB(ResultSet rs) throws SQLException{
        if(null==rs) return null;
        ArrayList<PillarPlateInfo> res=new ArrayList<>();
        PillarPlateInfo add;
        int i=0;
        while(rs.next()){
            add=new PillarPlateInfo(rs.getString("pillar_plate_id"), rs.getString("inventory_barcode"), rs.getString("plate_type"), rs.getString("chip_layout_type"),rs.getString("test_name"), 
                    rs.getTimestamp("assemble_time"),rs.getString("status"), rs.getString("plate_seq_num"),rs.getString("TSP"), rs.getString("well_plate_id"));
            add.init();
//            System.out.println(add.status);
//            System.out.println(add);
            res.add(add);
            i++;
        }
//        System.out.println(i+" of pillar plate(s) was added from result set");
//        System.out.println(res.size()+" of pillar plate(s) was returned");
        return res;
    }

    @Override
    public String toString() {
        return "PillarPlateInfo{" + "pillar_plate_id=" + pillar_plate_id + ", inventory_barcode=" + inventory_barcode + ", plate_type=" + plate_type + ", chip_layout_type=" + chip_layout_type + ", test_name=" + test_name + ", assemble_time=" + assemble_time + ", status=" + status + ", plate_seq_num=" + plate_seq_num + ", TSP=" + TSP + ", well_plate_id=" + well_plate_id + ", barcode=" + barcode + '}';
    }
    

    public AssayBarcode getBarcode() {
        if(null==barcode) init();
        return barcode;
    }

    
    /*
    1. remove plate prefix
    2. return the first 3 characters as "blind lot number"
    */
//    public String blindLotNumber(){
//        String s = pillar_plate_id.toUpperCase(); if(null==s) System.out.println("s null");
//       Product product = barcode.getProduct();
//// if(null==product){
////            System.out.println("null product 4 "+s); return null;
////        }
//            s = s.replaceFirst(product.prefix,"");
//            s=s.substring(0,3);
//            System.out.println(pillar_plate_id+" => "+s);
//            return s;
//    }
    
    private boolean init(){
        this.barcode=AssayBarcode.instanceFromBarcode(pillar_plate_id);
        return this.barcode != null;
    }
}
