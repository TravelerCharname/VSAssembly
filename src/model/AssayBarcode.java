/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author LM&L
 */
public class AssayBarcode {
    public String namePrefix;  //4 digits;
    public String lotNumber;   //4 digits; usually 8XXX
    public String batchNumber; //4 digits; obsoleted; used to designate the number of assembly
    public String plateId; //6 digits; or entire barcode for TST plates;
    private Product product;

    private boolean validPref;
    private boolean validlotNumber;
    private boolean validbatchNumber;
    private boolean validplateId;
    
    public AssayBarcode(String namePrefix, String lotNumber, String batchNumber, String plateId) {
        this.namePrefix = namePrefix;
        this.lotNumber = lotNumber;
        this.batchNumber = batchNumber;
        this.plateId = plateId;
        this.product=Product.lookupByNameOrPartNumberOrSOP(namePrefix);
    }
    
    private AssayBarcode(){
        
    }
    public AssayBarcode(String pillarPlateBarcode) {
        Product temp = null;
        for(Product p:Product.values()){
            if(!pillarPlateBarcode.toUpperCase().startsWith(namePrefix)) continue;
            temp=p;
            this.validPref=true;
        }
        if(null==temp){
            temp=Product.TST;
            this.validPref=false;
        }
        this.product=temp;
        
        if(Product.TST.equals(temp)){
            this.plateId=pillarPlateBarcode;
        }else{
            /*
            try split with 00
            production plate should all have a shape like XXX001001000ddd
            
            but how to tell 1st lot from 10th lot?
            */
            String s=pillarPlateBarcode.toUpperCase();
            s=s.replaceFirst(temp.prefix, plateId);
            char[] chars = s.toCharArray();
            int begin=0;
            for(int i=0;i<s.length();i++){
                String s1=new String(chars, begin, i-begin);
            }
        }
    }
    
    public String formAssayBarcode(){
        return namePrefix+lotNumber+batchNumber+plateId;
    }
    
    public Product getProduct(){
        return Product.lookupByNameOrPartNumberOrSOP(namePrefix);
    }
    
    public boolean isValid(){
        Product product = getProduct();
        if(null==product)return false;
        if(Product.TST==product)
            //potentially be able to check N01, be able to format TST barcode
            return true;
        // production plates
        if(VAL_LOT_DIGITS!=lotNumber.length()) return false;
        if(VAL_BATCH_DIGITS!=batchNumber.length()) return false;
        if(VAL_PLATE_DIGITS!=plateId.length()) return false;
        return true;
    }
    
    /*
    check number of digit while writing, skip checking when reading
    */
    public static boolean validate(String assayBarcode){
        for(Product p:Product.values()){
            if(assayBarcode.startsWith(p.prefix)){
                if(Product.TST.equals(p)){
                    // verify the last 2 char are digits
                    // return y/n
                }
                // verify total lenth - len(pref) = VAL_PLATE_DIGITS + VAL_BATCH_DIGITS + VAL_LOT_DIGITS
                // all digits
            }
        }
        return false;
    }
    public static final int VAL_PLATE_DIGITS = 6;
    public static final int VAL_BATCH_DIGITS = 3;
    public static final int VAL_LOT_DIGITS = 4;
}
