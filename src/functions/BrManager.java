/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import exceptions.ResourcePropertiesException;
import model.BatchInfo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import exceptions.TemplateNotFoundException;
import exceptions.UnknownProductException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Properties;
import model.Order;
import model.QcRecord;
import model.RawMaterial;
import model.Users;

/**
 *
 * @author mlei
 */
public class BrManager {

    /*
    keep latest info
    
    ui props
     */
    private QcManager qcmngr;
    private BatchInfo br;
//    private static Properties lastIqcBatch;

    
    public void generateBrReport() throws UnknownProductException, IOException, ResourcePropertiesException {
        String template;
        try {
            template = DocTemplateManager.getTemplate(br.product);

            String content = DocTemplateManager.readDocContent(template);

            System.out.println("read content");System.out.println(content);
            System.out.println("found fDATE? ");System.out.println(content.contains(FDATE));
            content = content.replaceAll(FDATE, DocDateUtil.getfDate());
            System.out.println("found from? ");System.out.println(content.contains(brFROM));
            content = content.replaceAll(brFROM, br.batchNumberFrom);
            System.out.println("found to? ");System.out.println(content.contains(brTO));
            content = content.replaceAll(brTO, br.batchNumberTo);
            
            if(PlateTypeUtil.getPlateType(br.product)==24)
            content = content.replaceAll(iqcPLATE24, IqcUtil.getLatestIqcBatchNumber(RawMaterial.Plate24));
            else if(PlateTypeUtil.getPlateType(br.product)==96)
            content = content.replaceAll(iqcPLATE96, IqcUtil.getLatestIqcBatchNumber(RawMaterial.Plate96));
            else throw new UnknownProductException();
            System.out.println("found silica? ");System.out.println(content.contains(iqcSILICAGEL));
            content = content.replaceAll(iqcSILICAGEL, IqcUtil.getLatestIqcBatchNumber(RawMaterial.SilicaGel));
            System.out.println("found bag? ");System.out.println(content.contains(iqcTHEROMOBAG));
            content = content.replaceAll(iqcTHEROMOBAG, IqcUtil.getLatestIqcBatchNumber(RawMaterial.ThermoBag));
            String vswafer = IqcUtil.getLatestIqcBatchNumber(RawMaterial.Wafer);
            if(null==vswafer) vswafer=Order.getTestOrderForVSWafer().getIqcBrNumber();
            System.out.println("found wafer? ");System.out.println(content.contains(iqcVSWAFER));
            content = content.replaceAll(iqcVSWAFER, vswafer);
            
            DigiSigner sign=new DigiSigner(Users.MING);
            content = sign.sign(content);
//            template = template.substring(template.lastIndexOf("\\"));
//            String folder=template.replaceAll("_DATE.xml", "");
            File dest=new File(BR_ROOT, br.getBatchRecordFolderNumber());
            if(!dest.exists()) dest.mkdirs();
//            batchNumber = template.replaceAll(DATE, DocDateUtil.localDate2yyMMdd(br.dateAssembled));
            dest = new File(dest, br.getBatchRecordNumber()+sDOC);
            
//            String dest = template.replaceAll(DATE, br.dateAssembled);
//            dest = dest.replaceAll(sXML, sDOC);
            BufferedWriter bw=new BufferedWriter(new FileWriter(dest));
            bw.write(content);
            bw.flush();
            bw.close();
            
            System.out.println("BR file generated to "+dest);
        } catch (TemplateNotFoundException ex) {
            Logger.getLogger(BrManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnknownProductException();
        }

    }
    public static final String sDOC = ".doc";
    public static final String sXML = ".xml";
    public static final String hQCh = "-QC-Vibrant ";
    public static final String DATE = "DATE";
    public static final String FDATE = "fDATE";
    public static final String brFROM = "BATCHNUMBERFROM";
    public static final String brTO = "BATCHNUMBERTO";
    public static final String iqcVSWAFER = "VSWAFERS";
    public static final String iqcPLATE24 = "920010BRDATE";
    public static final String iqcPLATE96 = "920001BRDATE";
    public static final String iqcSILICAGEL = "920008BRDATE";
    public static final String iqcTHEROMOBAG = "920009BRDATE";
    public static final String PLATENAME = "PLATENAME";
    public static final String PLATEPARTNUMBER = "PLATEPARTNUMBER";

    public static final String BR_ROOT = "C:\\Users\\LM&L\\Desktop\\test\\BR\\2017-2018\\";//"C:\\Users\\mlei\\Desktop\\test\\BR\\2017-2018";
                                        //= "C:\\Users\\mlei\\Documents\\work\\sop\\QC Documents\\product realization\\BR\\2017-2018";//

    public BatchInfo getBr() {
        return br;
    }

    public void setBr(BatchInfo br) {
        this.br = br;
    }

    public BrManager(BatchInfo br) {
        this.br = br;
    }
    
    
    
//    //================= QC ==================
//    /*
//    delayed instance
//     */
//    public QcManager getQcManager() {
//        if (null == qcmngr) {
//            qcmngr=new QcManager(br);
//        }
//        return qcmngr;
//    }
//
//    /**
//     *
//     * @param qcmnger
//     */
//    public void setQcManager(QcManager qcmnger) {
//        this.qcmngr = qcmnger;
//    }
    
}
