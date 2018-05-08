/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsassembly;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import exceptions.ResourcePropertiesException;
import exceptions.TemplateNotFoundException;
import exceptions.UnknownProductException;
import functions.BrManager;
import functions.DocDateUtil;
import functions.DocTemplateManager;
import functions.IqcUtil;
import functions.PlateTypeUtil;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BatchInfo;
import model.IqcRecord;
import model.Order;
import model.Product;
import model.RawMaterial;
import ui.RecordController;

/**
 *
 * @author LM&L
 */
public class VSAssembly {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        demoLastIqcProp();
        generateBrTest();
//        System.out.println(DocDateUtil.getSignDate());
//DocDateUtil.parseLocalDate("1989-05-04");
    }

    public static void localDateTest() {
        LocalDate db = LocalDate.of(1989, 5, 4);
        LocalDate now = LocalDate.now();
        System.out.println(db);System.out.println(now);
    }

    public static void generateBrTest() {
        //        try {
//            DocTemplateManager.brTemplateFormatter();
//        } catch (IOException ex) {
//            Logger.getLogger(VSAssembly.class.getName()).log(Level.SEVERE, null, ex);
//        }
BatchInfo br = demoBatchRecordTest();
System.out.println("given from: "+br.batchNumberFrom);
System.out.println("given to: "+br.batchNumberTo);
//BrManager bc=new BrManager(br);
RecordController.initManager(br);
try {
//    bc.generateBrReport();
    RecordController.generateBrQcReport();
} catch (UnknownProductException ex) {
    Logger.getLogger(VSAssembly.class.getName()).log(Level.SEVERE, null, ex);
} catch (IOException ex) {
    Logger.getLogger(VSAssembly.class.getName()).log(Level.SEVERE, null, ex);
} catch (ResourcePropertiesException ex) {
    Logger.getLogger(VSAssembly.class.getName()).log(Level.SEVERE, null, ex);
}
//        try {
//            bc.show();
//        } catch (TemplateNotFoundException ex) {
//            Logger.getLogger(VSAssembly.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public static BatchInfo demoBatchRecordTest() {
        BatchInfo br=new BatchInfo();
//        Product p=new Product();
//        p.plateName="test plate";
//        p.platePartNumber="910059";
br.product=Product.lookupByNameOrPartNumberOrSOP("910059");
br.batchNumberFrom="TST910059N01";
br.batchNumberTo="TST910059N09";
br.dateAssembled=DocDateUtil.resetSystemCurrentDate();

System.out.println(br);
return br;
    }

    public static void propGsonTest() throws JsonSyntaxException {
        demoLastIqcProp();
        Properties p = IqcUtil.getLatestIqc();
        String s;
        IqcRecord iqc;

        Gson gson = new Gson();
        for (Object k : p.values()) {
            s = String.valueOf(k);

            iqc = gson.fromJson(s, IqcRecord.class);
            System.out.println(iqc.material);
            System.out.println(iqc.lastOrder);
        }
    }

    public static void demoLastIqcProp() {
        try {
            Properties demoProp = new Properties();
            Gson gson = new Gson();
            Order order;
            IqcRecord iqc;
            for (RawMaterial rm : RawMaterial.values()) {
                order = new Order(rm, DocDateUtil.localDateWith(2018, 2, 3).format(DateTimeFormatter.ofPattern("yyMMdd")), DocDateUtil.getyyMMdd(), "100 unit", "99 unit", "Amazon", "test user");
                iqc = new IqcRecord(rm, order);
                demoProp.setProperty(rm.partNumber, gson.toJson(iqc, IqcRecord.class));
            }
            IqcUtil.setLatest(demoProp);
            demoProp.store(new BufferedWriter(new FileWriter("src\\resources\\demo raw material latest batch.properties")), "dummy");
        } catch (IOException ex) {
            Logger.getLogger(VSAssembly.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void lastIqcBatchPropTest() {
        try {
            //        RawMatEnumTest();
            Properties prop = new Properties();
            prop.load(new FileReader("src\\resources\\raw material latest batch.properties"));
            System.out.println(prop.getProperty("920008"));
            System.out.println(prop.getProperty("920009"));
        } catch (IOException ex) {
            Logger.getLogger(VSAssembly.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void RawMatEnumTest() {
        // TODO code application logic here
        RawMaterial rm = RawMaterial.lookupByNameOrPartNumber("920007");
        System.out.println(rm);
        rm = RawMaterial.lookupByNameOrPartNumber("          UV Dicing Tape ");
        System.out.println(rm);
    }

}
