/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import exceptions.InvalidAssayBarcodeException;
import java.util.ArrayList;

/**
 *
 * @author LM&L
 */
public class LotInfo {
    // try to commit this comment
    // and finish this
    private Product prod;
    private String lotNumber;
    private int total;
    
    
    // traverse to find the latest lot number
    public static LotInfo lotFromPlates(ArrayList<PillarPlateInfo> plates) throws InvalidAssayBarcodeException{
        if(null==plates) return null;
        LotInfo lotInfo=new LotInfo();
        AssayBarcode bar;
        int aLotNumber=-1,temp,count=0;
        // product
        bar=plates.get(0).getBarcode();
        lotInfo.prod=bar.getProduct();
        // lot number// count
        for(PillarPlateInfo p:plates){
            bar=p.getBarcode();
            try{
            temp=Integer.parseInt(bar.lotNumber);
            }catch(NumberFormatException e){
                throw new InvalidAssayBarcodeException(InvalidAssayBarcodeException.LOT_NUMBER+"@"+bar);
            }
            if(aLotNumber==temp){
                count++;
            }else if(aLotNumber<temp){
                count=1;
                aLotNumber=temp;
            }
        }
        lotInfo.total=count;
        lotInfo.lotNumber=aLotNumber+"";
        // check consistancy of prod & lot num
        
        return lotInfo;
    }
    
    // doublecheck: lot number consistancy
    // use when you have the latest lot number at hand, and query with that info
    public static LotInfo simpleLotFromPlates(ArrayList<PillarPlateInfo> plates){
        if(null==plates) return null;
        // product
        // lot number
        // count
        // check consistancy of prod & lot num
        LotInfo lot=new LotInfo();
        return lot;
    }
}
