/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbUtil;

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
    
}
