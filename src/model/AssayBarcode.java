/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        this.product = Product.lookupByNameOrPartNumberOrSOP(namePrefix);
    }

    private AssayBarcode() {

    }

    public AssayBarcode(String pillarPlateBarcode) {
        Product temp = null;
        for (Product p : Product.values()) {
            if (!pillarPlateBarcode.toUpperCase().startsWith(namePrefix)) {
                continue;
            }
            temp = p;
            this.validPref = true;
        }
        if (null == temp) {
            temp = Product.TST;
            this.validPref = false;
        }
        this.product = temp;

        if (Product.TST.equals(temp)) {
            this.plateId = pillarPlateBarcode;
        } else {
            /*
            try split with 00
            production plate should all have a shape like XXX001001000ddd
            
            but how to tell 1st lot from 10th lot?
             */
            String s = pillarPlateBarcode.toUpperCase();
            s = s.replaceFirst(s, temp.prefix);
//            char[] chars = s.toCharArray();
//            int begin=0;
//            for(int i=0;i<s.length();i++){
//                String s1=new String(chars, begin, i-begin);
//            }
            int mark;
            mark = s.length() - 6;
            if (s.startsWith("8")) {
                this.lotNumber = s.substring(0, 4);
                this.batchNumber = s.substring(4, mark);
            } else {
                this.lotNumber = s.substring(0, 3);
                this.batchNumber = s.substring(3, mark);
            }
            this.plateId = s.replaceFirst(s, lotNumber).replaceFirst(s, batchNumber);    //s.substring(mark);
        }
    }

    public static AssayBarcode instanceFromBarcode(String pillarPlateBarcode) {
        if (null == pillarPlateBarcode) {
            return null;
        }
        pillarPlateBarcode=pillarPlateBarcode.trim();
        AssayBarcode ab = new AssayBarcode();
        outer:
        for (Product p : Product.values()) {
            if (!pillarPlateBarcode.toUpperCase().startsWith(p.prefix)) {
                continue;
            }

            inner:
            for (Pattern pattern : P_ILLEGAL_STRING) {
                if (pattern.matcher(pillarPlateBarcode).find()) {
                    System.out.println("found "+pattern.pattern()+" in "+pillarPlateBarcode+"...........");
                    break outer;
                }
            }
            ab.product = p;
            ab.namePrefix = p.prefix;
            ab.validPref = true;
            break;
        }
        if (null == ab.product) {
            ab.product = Product.TST;
//            ab.validPref = false;
        }
//        this.product = temp;

        if (Product.TST.equals(ab.product)) {
            ab.plateId = pillarPlateBarcode;
        } else {
            /*
            try split with 00
            production plate should all have a shape like XXX001001000ddd
            
            but how to tell 1st lot from 10th lot?
             */
            String s = pillarPlateBarcode.toUpperCase();
            s = s.replaceFirst(ab.product.prefix, "");

//            char[] chars = s.toCharArray();
//            int begin=0;
//            for(int i=0;i<s.length();i++){
//                String s1=new String(chars, begin, i-begin);
//            }
            int mark;
            mark = s.length() - VAL_PLATE_DIGITS;
            try {
                if (s.startsWith("8")) {
                    ab.lotNumber = s.substring(0, 4);
                    ab.batchNumber = s.substring(4, mark);
                } else {
                    ab.lotNumber = s.substring(0, 3);
                    ab.batchNumber = s.substring(3, mark);
                }
            } catch (java.lang.StringIndexOutOfBoundsException e) {
                System.out.println(pillarPlateBarcode + " izzue");
            }
            ab.plateId = s.substring(s.length()-6);

            ab.validateDigits();
        }
        return ab;
    }

    public String formAssayBarcode() {
        return namePrefix + lotNumber + batchNumber + plateId;
    }

    public Product getProduct() {
        Product luk = Product.lookupByNameOrPartNumberOrSOP(namePrefix);
        if(null==luk) System.out.println("can't find product for name: "+namePrefix+lotNumber+batchNumber+plateId);
        return Product.lookupByNameOrPartNumberOrSOP(namePrefix);
    }

    public boolean isValid() {
//        Product product = getProduct();
        if (null == product) {
            return false;
        }
        if (Product.TST.equals(product)) //potentially be able to check N01, be able to format TST barcode
        {
            return true;
        }
//        validateDigits();
        return this.validPref && this.validbatchNumber && this.validlotNumber && this.validplateId;
    }

    private void validateDigits() {
        // production plates
        if (VAL_LOT_DIGITS == lotNumber.length()) {
            this.validlotNumber = true;
        } else if (4 == lotNumber.length()) {
            this.validlotNumber = this.lotNumber.startsWith("8");
        }
        if (VAL_BATCH_DIGITS == batchNumber.length()) {
            this.validbatchNumber = true;
        }
        if (VAL_PLATE_DIGITS == plateId.length()) {
            this.validplateId = true;
        }
    }

    /*
    check number of digit while writing, skip checking when reading
     */
    public static boolean validate(String assayBarcode) {
        AssayBarcode ins = instanceFromBarcode(assayBarcode);
        ins.validateDigits();
        return ins.isValid();
    }
    public static final int VAL_PLATE_DIGITS = 7;
    public static final int VAL_BATCH_DIGITS = 3;
    public static final int VAL_LOT_DIGITS = 3; // with legit exception of 4 digits 8003

    private static final Pattern[] P_ILLEGAL_STRING = new Pattern[]{Pattern.compile("[\\W_]"), Pattern.compile("TST"), Pattern.compile("TEST")};

    @Override
    public String toString() {
        return "AssayBarcode{" + "namePrefix=" + namePrefix + ", lotNumber=" + lotNumber + ", batchNumber=" + batchNumber + ", plateId=" + plateId + ", product=" + product + ", validPref=" + validPref + ", validlotNumber=" + validlotNumber + ", validbatchNumber=" + validbatchNumber + ", validplateId=" + validplateId + '}';
    }
}
