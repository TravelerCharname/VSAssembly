/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import exceptions.InvalidAssayBarcodeException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author LM&L
 */
public class LotInfo {

    private Product prod;
    private String lotNumber;
    private int total;

    // traverse to find the latest lot number
    public static LotInfo lotFromPlates(ArrayList<PillarPlateInfo> plates) throws InvalidAssayBarcodeException {
        if (null == plates) {
            return null;
        }
        LotInfo lotInfo = new LotInfo();
        AssayBarcode bar;
        int aLotNumber = -1, temp, count = 0;
        // product
        bar = plates.get(0).getBarcode();
        lotInfo.prod = bar.getProduct();
        // lot number// count
        for (PillarPlateInfo p : plates) {
            bar = p.getBarcode();
            if(!bar.isValid()){
                System.out.println("alert! invalid plate: "+bar);
            }
            try {
                temp = Integer.parseInt(bar.lotNumber);
            } catch (NumberFormatException e) {
                throw new InvalidAssayBarcodeException(InvalidAssayBarcodeException.LOT_NUMBER + "@" + bar);
            }
            if (aLotNumber == temp) {
                count++;
                System.out.println("add "+p.pillar_plate_id);
            } else if (aLotNumber < temp) {
                count = 1;
                aLotNumber = temp;
                
                System.out.println("============ lot number updated ============");
                System.out.println("add "+p.pillar_plate_id);
            }
        }
        lotInfo.total = count;
        lotInfo.lotNumber = aLotNumber + "";
        // check consistancy of prod & lot num

        System.out.println("show lot info");
        lotInfo.show(null);  //lotInfo.show(plates);
        return lotInfo;
    }

    // doublecheck: lot number consistancy
    // use when you have the latest lot number at hand, and query with that info
//    public static LotInfo simpleLotFromPlates(ArrayList<PillarPlateInfo> plates) {
//        if (null == plates) {
//            return null;
//        }
//        // product
//        // lot number
//        // count
//        // check consistancy of prod & lot num
//        LotInfo lot = new LotInfo();
//        return lot;
//    }

    private void show(ArrayList<PillarPlateInfo> toShow) {
        System.out.println("product: " + prod);
        System.out.println("lot number: " + lotNumber);
        System.out.println("lot size: " + total);
        if (toShow != null) {
            toShow.sort(new Comparator<PillarPlateInfo>() {
                @Override
                public int compare(PillarPlateInfo t, PillarPlateInfo t1) {
                    return t.pillar_plate_id.compareToIgnoreCase(t1.pillar_plate_id);
                }
            });
            for (PillarPlateInfo p : toShow) {
                System.out.println(p.pillar_plate_id);
            }
        }
    }
}
