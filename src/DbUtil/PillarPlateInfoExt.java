/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import model.PillarPlateInfo;

/**
 *
 * @author mlei
 */
public class PillarPlateInfoExt extends PillarPlateInfo{
    public String wafer_id;
    
    public PillarPlateInfoExt(String pillar_plate_id, String inventory_barcode, String plate_type, String chip_layout_type, String test_name, Date assemble_time, String status, String plate_seq_num, String TSP, String well_plate_id) {
        super(pillar_plate_id, inventory_barcode, plate_type, chip_layout_type, test_name, assemble_time, status, plate_seq_num, TSP, well_plate_id);
    }
    
    public static ArrayList<PillarPlateInfoExt> plateExtListFromDB(ResultSet rs) throws SQLException{
        if(null==rs) return null;
        ArrayList<PillarPlateInfoExt> res=new ArrayList<>();
        PillarPlateInfoExt add;
        int i=0;
        while(rs.next()){
            add=new PillarPlateInfoExt(rs.getString("pillar_plate_id"), rs.getString("inventory_barcode"), rs.getString("plate_type"), rs.getString("chip_layout_type"),rs.getString("test_name"), 
                    rs.getTimestamp("assemble_time"),rs.getString("status"), rs.getString("plate_seq_num"),rs.getString("TSP"), rs.getString("well_plate_id"));
            add.wafer_id=(rs.getString("wafer_id")==null?null:rs.getString("wafer_id").trim());
//            add.init();
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
        return "PillarPlateInfoExt{" + "wafer_id=" + wafer_id + '}';
    }
}
