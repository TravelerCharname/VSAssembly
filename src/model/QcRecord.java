/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author mlei
 */
public class QcRecord {
    //    public String plateName;
//    public String platePartNumber;
    public Product product;
    public LocalDate dateAssembled;
//    private String batchRecordNumber;
//    private String batchRecordFolderName;

    public QcRecord(Product product, LocalDate dateAssembled) {
        this.product = product;
        this.dateAssembled = dateAssembled;
        
    }
    
    public static QcRecord qcFromBr(BatchInfo br){
        QcRecord qcRecord = new QcRecord(br.product, br.dateAssembled);
//        qcRecord.batchRecordFolderName=br.getBatchRecordFolderNumber();
//        qcRecord.batchRecordNumber=br.getBatchRecordNumber();
        return qcRecord;
    }
}
