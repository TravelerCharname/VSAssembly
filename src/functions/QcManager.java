/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import exceptions.TemplateNotFoundException;
import exceptions.UnknownProductException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BatchInfo;
import model.QcRecord;
import model.Users;

/**
 *
 * @author mlei
 */
public class QcManager {
    private QcRecord qcr;
    private String batchRecordNumber;
    
    /*
    overwrite if exists
    */

    public QcManager(QcRecord qcr) {
        this.qcr = qcr;
    }

    public QcManager(BatchInfo br) {
        if(null==br) throw new RuntimeException("Null BatchRecord. Fail to init QC Manager from BatchRecord.");
        this.qcr=QcRecord.qcFromBr(br);
        batchRecordNumber=br.getBatchRecordNumber();
    }
    
    public void generateQcReport() throws IOException{
        try {
            String template = DocTemplateManager.getTemplate(DocTemplateManager.QC_TEMPLATE);
            
            String content = DocTemplateManager.readDocContent(template);
            
//            if (content.contains(DocTemplateManager.FDATE)) {
//                System.out.println("found " + DocTemplateManager.FDATE);
//            } else {
//                throw new RuntimeException("no " + DocTemplateManager.FDATE);
//            }
            if (content.contains(DocTemplateManager.PLATENAME)) {
                System.out.println("found " + DocTemplateManager.PLATENAME);
            } else {
                throw new RuntimeException("no " + DocTemplateManager.PLATENAME);
            }
            if (content.contains(DocTemplateManager.PLATEPARTNUMBER)) {
                System.out.println("found " + DocTemplateManager.PLATEPARTNUMBER);
            } else {
                throw new RuntimeException("no " + DocTemplateManager.PLATEPARTNUMBER);
            }
            content = content.replaceAll(DocTemplateManager.PLATENAME, qcr.product.plateName);
            content = content.replaceAll(DocTemplateManager.PLATEPARTNUMBER, qcr.product.platePartNumber);
            content = content.replaceAll(DocTemplateManager.FDATE, DocDateUtil.localDate2fDate(qcr.dateAssembled));
            content = content.replaceAll(DocTemplateManager.BATCHRECORDNUMBER, batchRecordNumber);
            
            DigiSigner sign=new DigiSigner(Users.MING);
            content = sign.sign(content);
            
            File dest=getDestFile();
            
//            String dest = template.replaceAll(DATE, br.dateAssembled);
//            dest = dest.replaceAll(sXML, sDOC);
            BufferedWriter bw=new BufferedWriter(new FileWriter(dest));
            bw.write(content);
            bw.flush();
            bw.close();
            
            System.out.println("QC file generated to "+dest);
        } catch (TemplateNotFoundException ex) {
            Logger.getLogger(QcManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownProductException ex) {
            Logger.getLogger(QcManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public static final String QC_ROOT=FolderInitializer.QC_ROOT;//"C:\\Users\\LM&L\\Desktop\\test\\QC\\2017-2018\\";//"C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\product realization\\QC\\2018-2019\\";
    public static final String hQCh = "-QC-Vibrant ";
    public static final String sDOC = ".doc";
    public static final String sXML = ".xml";
    File getDestFile(){//910059-BR-Vibrant FS IgG 24 Pillar Plate
        File dest=new File(QC_ROOT);
        String folderName = qcr.product.platePartNumber+hQCh+qcr.product.plateName;
        dest=new File(dest, folderName);
        if(!dest.exists())dest.mkdirs();
        dest=new File(dest, folderName+"_"+DocDateUtil.localDate2yyMMdd(qcr.dateAssembled)+sDOC);
        return dest;
    }
}
