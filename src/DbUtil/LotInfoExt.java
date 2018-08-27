/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbUtil;

import java.util.ArrayList;
import model.LotInfo;
import model.PillarPlateInfo;
import model.Product;

/**
 *
 * @author lm
 */
public class LotInfoExt extends LotInfo {
        public java.util.Date establish;

        public LotInfoExt(Product prod, String lotNumber, ArrayList<PillarPlateInfo> plates) {
            super(prod, lotNumber, plates);
        }

        
        void addPlateToLot(PillarPlateInfo plate) {
//update est       
            if (null == this.establish) {
                establish = plate.assemble_time;
            }
//insert plate
            this.getPlates().add(plate);
        }
    }