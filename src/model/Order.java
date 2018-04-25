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
public class Order implements Record{

    public RawMaterial rm;
//    public String rawMaterialName;
//    public String rawMaterialPartNumber;
    public String simpleDateOrdered;
    public String simpleDateReceived;
    public String quantityOrdered;
    public String quantityReceived;
    public String supplierName;

    public String iqcIssuer;

    public String getIqcBrNumber() {
//        if(null==rm.partNumber) return null;
//        if(null==rm.name) return null;
        if (this.equals(TestOrderForVSWafer)) {
            return "VSWAFERS";
        }
        if (null == simpleDateReceived) {
            return null;
        }
        return rm.partNumber + "-IQC-" + rm.name + "_" + simpleDateReceived;
    }

    public Order(RawMaterial rm, String simpleDateOrdered, String simpleDateReceived, String quantityOrdered, String quantityReceived, String supplierName, String iqcIssuer) {
        this.rm = rm;
        this.simpleDateOrdered = simpleDateOrdered;
        this.simpleDateReceived = simpleDateReceived;
        this.quantityOrdered = quantityOrdered;
        this.quantityReceived = quantityReceived;
        this.supplierName = supplierName;
        this.iqcIssuer = iqcIssuer;
    }

    public static Order getTestOrderForVSWafer() {
        if (null == TestOrderForVSWafer) {
            TestOrderForVSWafer = new Order(RawMaterial.Wafer, "99/99/99", "99/99/99", "1", "1", "VS", "TEST");
        }
        return TestOrderForVSWafer;
    }

    private static Order TestOrderForVSWafer;
    public static final String CARRIERTAPE = "Carrier Tape";
    public static final String UVTAPE = "UV Dicing Tape";
    public static final String TAKEUPREEL = "Take-up Reel";
    public static final String COVERTAPE = "Cover Tape";
    public static final String EPOXY = "Epoxy Gels";
    public static final String SILICAGEL = "Packaging Consumables-Silica Gel";
    public static final String THERMOBAG = "Packaging Consumables-Thermo-protected Bag";

    @Override
    /*
    name -> date -> hash
    */
    public int compareTo(Record t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String stringValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
