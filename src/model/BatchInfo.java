/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import functions.DocDateUtil;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author mlei
 */
public class BatchInfo implements Record {
//    public String plateName;
//    public String platePartNumber;

    public Product product;
    public LocalDate dateAssembled;
    public String batchNumberFrom;
    public String batchNumberTo;
    private int batchSize;
    private String batchRecordNumber;
    private String batchRecordFolderName;
    /*
    24 or 96
     */
    public int plateType;
    public String VSWafers;
    public String plateBr;
    public String silicaGelBr;
    public String thermoBagBr;
    
    private ArrayList<AssayBarcode> plates;

    public String getBatchRecordNumber() {
        if (null == batchRecordNumber || batchRecordNumber.isEmpty()) {
            batchRecordNumber = getBatchRecordFolderNumber() + "_" + DocDateUtil.localDate2yyMMdd(dateAssembled);
            System.out.println(batchRecordNumber);
        }
        return batchRecordNumber;
    }

    public String getBatchRecordFolderNumber() {
        if (null == batchRecordFolderName || batchRecordFolderName.isEmpty()) {
            batchRecordFolderName = product.platePartNumber + "-BR-Vibrant " + product.plateName;
            System.out.println(batchRecordFolderName);
        }
        return batchRecordFolderName;
    }

    public int getBatchSize() {
        if (batchSize <= 0) {

        }
        return batchSize;
    }

    @Override
    public String stringValue() {
        return "BatchRecord{" + "product=" + product + ", dateAssembled=" + dateAssembled + ", batchNumberFrom=" + batchNumberFrom + ", batchNumberTo=" + batchNumberTo + ", plateType=" + plateType + ", VSWafers=" + VSWafers + ", plateBr=" + plateBr + ", silicaGelBr=" + silicaGelBr + ", thermoBagBr=" + thermoBagBr + '}';
    }

    @Override
    public int compareTo(Record t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<AssayBarcode> getPlates() {
        if(null==plates){
            plates=new ArrayList<>();
            
        }
        return plates;
    }

    private String getLotNumber() {
        String lf = this.batchNumberFrom.substring(4, 8);
        String lt = this.batchNumberTo.substring(4, 8);
        if (!lf.equals(lt)) {
            System.out.println("Different lot in this batch!");
        }
        return lf;
    }
}
