/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public String assemble_time;
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
            String assemble_time, String status, String plate_seq_num, String TSP, String well_plate_id) {
        this.pillar_plate_id = pillar_plate_id;
        this.inventory_barcode = inventory_barcode;
        this.plate_type = plate_type;
        this.chip_layout_type = chip_layout_type;
        this.test_name = test_name;
        this.assemble_time = assemble_time;
        this.status = Status.getStatusByName(status);
        this.plate_seq_num = plate_seq_num;
        this.TSP = TSP;
        this.well_plate_id = well_plate_id;
    }
    public static ArrayList<PillarPlateInfo> plateListFromDB(ResultSet rs) throws SQLException{
        if(null==rs) return null;
        ArrayList<PillarPlateInfo> res=new ArrayList<>();
        PillarPlateInfo add;
        int i=0;
        while(rs.next()){
            add=new PillarPlateInfo(rs.getString("pillar_plate_id"), rs.getString("inventory_barcode"), rs.getString("plate_type"), rs.getString("chip_layout_type"),rs.getString("test_name"), 
                    rs.getString("assemble_time"),rs.getString("status"), rs.getString("plate_seq_num"),rs.getString("TSP"), rs.getString("well_plate_id"));
            add.init();
//            System.out.println(add.status);
//            System.out.println(add);
            res.add(add);
            i++;
        }
        System.out.println(i+" of pillar plate(s) was added from result set");
        System.out.println(res.size()+" of pillar plate(s) was returned");
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

    
    
    private boolean init(){
        this.barcode=AssayBarcode.instanceFromBarcode(pillar_plate_id);
        return this.barcode != null;
    }
}
