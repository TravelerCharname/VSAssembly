/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

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
    
    public static LotInfo lotFromPlates(ArrayList<PillarPlateInfo> plates){
        if(null==plates) return null;
        // product
        // lot number
        // count
        // check consistancy of prod & lot num
        LotInfo lot=new LotInfo();
        return lot;
    }
}
