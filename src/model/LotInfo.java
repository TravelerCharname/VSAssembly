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
    private int approved;
    private int assembled;
    private int failed;
    private int finished;
//    private int inUse;
//    private int inStock;
    private int scanning;
    private int test;
    private int testing;

    private ArrayList<PillarPlateInfo> plates;

    public LotInfo() {
    }

    public LotInfo(Product prod, String lotNumber, ArrayList<PillarPlateInfo> plates) {
        this.prod = prod;
        this.lotNumber = lotNumber;
        this.plates = plates;
        if(null!=plates&&!plates.isEmpty()) autoCount();//plates
    }

    // traverse to find the latest lot number
    public static LotInfo getLatestLotFromPlates(ArrayList<PillarPlateInfo> plates) throws InvalidAssayBarcodeException {
        if (null == plates) {
            return null;
        }

        LotInfo lotInfo = new LotInfo();
        lotInfo.plates = new ArrayList<>();
        AssayBarcode bar;
        int aLotNumber = -1, temp;
//        int autoCount = 0;
        // product
        bar = plates.get(0).getBarcode();
        lotInfo.prod = bar.getProduct();
        // lot number// autoCount
        for (PillarPlateInfo p : plates) {
            bar = p.getBarcode();
            if (!bar.isValid()) {
                System.out.println("alert! invalid plate: " + bar);
            }
            try {
                temp = Integer.parseInt(bar.lotNumber);
            } catch (NumberFormatException e) {
                throw new InvalidAssayBarcodeException(InvalidAssayBarcodeException.LOT_NUMBER + "@" + bar);
            }
            if (aLotNumber == temp) {
                lotInfo.plates.add(p);
                lotInfo.count1(p);

//                autoCount++;
//                System.out.println("add "+p.pillar_plate_id);
            } else if (aLotNumber < temp) {
                lotInfo.plates.clear();
                lotInfo.clearCount();
                lotInfo.plates.add(p);
                lotInfo.count1(p);
//                autoCount = 1;
                aLotNumber = temp;

                System.out.println("============ lot number updated ============");
                System.out.println("add " + p.pillar_plate_id);
            }
        }
//        lotInfo.total = autoCount;
        lotInfo.total = lotInfo.plates.size();
        lotInfo.lotNumber = aLotNumber + "";
        // check consistancy of prod & lot num

        System.out.println("show lot info");
        lotInfo.show();  //lotInfo.show(plates);  //lotInfo.plates
        return lotInfo;
    }

    // doublecheck: lot number consistancy
    // use when you have the latest lot number at hand, and query with that info

    public String getLotInfoLogEntry() {
        return prod.name() + "\t" + this.lotNumber + "\t" + this.total+ "\t" + this.assembled + "\t" + this.approved+ "\t" + this.failed + "\t" + this.finished+ "\t" + this.test + "\t" + this.testing + "\t" + this.scanning;
    }
    public String getLotInfoDbEntry() {
        return "('"+prod.plateName + "','" +lotNumber + "','" + this.total  + "','" + this.assembled  + "','" + this.approved + "','" +this.failed  + "','" + this.finished + "','" + this.test  + "','" + this.testing  + "','" + this.scanning+"')";
    }
    
    public void show() {    //ArrayList<PillarPlateInfo> toShow
        System.out.println("product: " + prod);
        System.out.println("lot number: " + lotNumber);
        System.out.println("lot size: " + total);
        System.out.println("approved: " + approved);
        System.out.println("assembled: " + assembled);
        System.out.println("failed: " + failed);
        System.out.println("finished: " + finished);
//        System.out.println("in use: " + inUse);
//        System.out.println("in stock: " + inStock);
        System.out.println("test: " + test);
        System.out.println("testing: " + testing);
        System.out.println("scanning: " + scanning);
//        System.out.println("verify: total=inUse+inStock+finished");
        if (plates != null) {
            plates.sort(new Comparator<PillarPlateInfo>() {
                @Override
                public int compare(PillarPlateInfo t, PillarPlateInfo t1) {
                    return t.pillar_plate_id.compareToIgnoreCase(t1.pillar_plate_id);
                }
            });
            for (PillarPlateInfo p : plates) {
//                System.out.println(p);
//                System.out.println(p.pillar_plate_id);
            }
        }
    }

    public Product getProd() {
        return prod;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<PillarPlateInfo> getPlates() {
        return plates;
    }

    public int getApproved() {
        return approved;
    }

    public int getAssembled() {
        return assembled;
    }

    public int getFailed() {
        return failed;
    }

    public int getFinished() {
        return finished;
    }

    public int getScanning() {
        return scanning;
    }

    public int getTest() {
        return test;
    }

    public int getTesting() {
        return testing;
    }

    public boolean isConsistent(){
        for(PillarPlateInfo p:plates){
            if(!p.blindLotNumber().equals(lotNumber)){
                System.out.println("inconsistent plate: "+p);
                return false;
            }
        }
        return true;
    }
    public void autoCount() {   //Collection<PillarPlateInfo> plates
        for (PillarPlateInfo p : plates) {
            switch (p.status) {
                case Approve:
                    this.approved++;
                    break;
                case Fail:
                    this.failed++;
                    break;
                case finish:
                    this.finished++;
                    break;
                case assembled:
                    this.assembled++;
//                    this.inStock++;
                    break;
                case Scanning:
                    this.scanning++;
                    break;
                case Testing:
                    this.testing++;
                    break;
                case test:
                    this.test++;
                    break;
                default:
                    break;
            }
            this.total++;
        }
    }

    public void setCount(int approved,int assembled,int failed,int finished,int scanning,int test,int testing,int total){
        this.approved=approved;
        this.assembled=assembled;
        this.failed=failed;
        this.finished=finished;
        this.scanning=scanning;
        this.test=test;
        this.testing=testing;
        this.total=total;
    }
    public void count1(PillarPlateInfo p) {
        switch (p.status) {
            case Approve:
                this.approved++;
                break;
            case Fail:
                this.failed++;
                break;
            case finish:
                this.finished++;
                break;
            case assembled:
                this.assembled++;
//                    this.inStock++;
                break;
            case Scanning:
                this.scanning++;
                break;
            case Testing:
                this.testing++;
                break;
            case test:
                this.test++;
                break;
            default:
                break;
        }

    }

    public void clearCount() {
        this.approved = 0;
        this.failed = 0;
        this.finished = 0;
        this.assembled = 0;
        this.scanning = 0;
        this.testing = 0;
        this.test = 0;
//        this.inStock = 0;
//        this.inUse = 0;
        this.total = 0;
    }
}
