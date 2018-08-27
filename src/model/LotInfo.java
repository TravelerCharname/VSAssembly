/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import exceptions.InvalidAssayBarcodeException;
import java.sql.Date;
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
    
    private AssayBarcode from;
    private AssayBarcode to;
    
    private ArrayList<PillarPlateInfo> plates;
    private java.util.Date last_modified;

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
        return "('"+prod.plateName + "','" +lotNumber + "','" +from.formAssayBarcode()+ "','" +to.formAssayBarcode()+ "','" + this.total  + "','" + this.assembled  + "','" + this.approved + "','" +this.failed  + "','" + this.finished + "','" + this.test  + "','" + this.testing  + "','" + this.scanning+ "','" + this.last_modified+"')";
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
        System.out.println("last modified: " + last_modified);
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
        if(null==plates) plates=new ArrayList<>();
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

    public java.util.Date getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(java.util.Date last_modified) {
        if(null==last_modified) System.out.println("last modified set Null");
        this.last_modified = last_modified;
    }

    public String getFrom() {
        return from.formAssayBarcode();
    }

    public void setFrom(AssayBarcode from) {
        this.from = from;
    }
    public void updateFrom(AssayBarcode from) {
        if(null==from) return;
        try{
//            System.out.println("this.from = null? "+(this.from==null));
//            System.out.println("input from = null? "+(from==null));
            int old = (this.from==null?999999:Integer.parseInt(this.from.plateId));
            int nu = Integer.parseInt(from.plateId);
            if(old>=nu){
                this.from=from;
            }
        }catch(NumberFormatException e){
            System.out.println("unable to parse "+from);
        }
    }

    public String getTo() {
        return to.formAssayBarcode();
    }

    public void setTo(AssayBarcode to) {
        this.to = to;
    }
    public void updateTo(AssayBarcode to) {
        try{
//            System.out.println("this.to = null? "+(this.to==null));
//            System.out.println("input to = null? "+(to==null));
            int old = (this.to==null?-1:Integer.parseInt(this.to.plateId));
            int nu = Integer.parseInt(to.plateId);
            if(old<=nu){
                this.to=to;
            }
        }catch(NumberFormatException e){
            
        }
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
            this.last_modified=p.assemble_time;
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
