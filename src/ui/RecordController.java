/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import exceptions.ResourcePropertiesException;
import exceptions.UnknownProductException;
import functions.BrManager;
import functions.QcManager;
import java.io.IOException;
import model.BatchInfo;

/**
 *
 * @author LM&L
 */
public class RecordController {
    private static BrManager brmng;
    private static QcManager qcmng;
    /*
    overwrite if exists
     */
    public static void generateBrQcReport() throws UnknownProductException, ResourcePropertiesException, IOException{
        brmng.generateBrReport();
        qcmng.generateQcReport();
    }
    public static void initManager(BatchInfo br){
        brmng=new BrManager(br);
        qcmng=new QcManager(br);
    }
}
