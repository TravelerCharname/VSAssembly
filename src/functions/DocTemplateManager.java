/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import model.Product;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import exceptions.TemplateNotFoundException;
import exceptions.UnknownProductException;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author mlei
 */
public class DocTemplateManager {

    private static BufferedReader br;
    private static BufferedWriter bw;

    /*
    partNum + BR/*QC-universal/IQC to indicate which doc to fetch
    plateName => br
     */
    public static String getTemplate(Product product) throws TemplateNotFoundException {
        if ("Not Released".equals(product.sop)) {
            System.out.println("this product is not yet released. " + product.plateName);
            System.out.println("No BR template for this product at this time.");
            throw new TemplateNotFoundException(product.plateName);
        }
        switch (product) {
            case ANA:
                return "src\\resources\\910018-BR-Vibrant ANA 96 Pillar Plate_DATE.xml";
            case CEL:
                return "src\\resources\\910019-BR-Celiact Zoomer 96 Pillar Plate_DATE.xml";
            case ENA4:
                return "src\\resources\\910024-BR-Vibrant ENA 96 Pillar Plate_DATE.xml";
            case FOOA:
                return "src\\resources\\910060-BR-Vibrant FS IgA 24 Pillar Plate_DATE.xml";
            case FOOG:
                return "src\\resources\\910059-BR-Vibrant FS IgG 24 Pillar Plate_DATE.xml";
            case GUT_ZOOMER:
                return "src\\resources\\910026-BR-Vibrant Gut Zoomer 96 Pillar Plate_DATE.xml";
            case NEUG:
                return "src\\resources\\910072-BR-Vibrant NEUG 96 Pillar Plate_DATE.xml";
            case NEUM:
                return "src\\resources\\910073-BR-Vibrant NEUM 96 Pillar Plate_DATE.xml";
            case WZMR:
                return "src\\resources\\910026-BR-Vibrant Wheat Zoomer 96 Pillar Plate_DATE.xml";
            case IBSG:
                return "src\\resources\\910063-BR-Vibrant IBSG 96 Pillar Plate_DATE.xml";
            default:
                System.out.println("this product is not yet released. " + product.plateName);
                System.out.println("No BR template for this product at this time.");
                throw new TemplateNotFoundException(product.plateName);
        }
    }

    public static String getTemplate(String partNumberOrPlateNameOrQC) throws TemplateNotFoundException, UnknownProductException {
        if(QC_TEMPLATE.equalsIgnoreCase(partNumberOrPlateNameOrQC)){
            return "src\\resources\\PLATEPARTNUMBER-QC-Vibrant PLATENAME_DATE.xml";
        }
        Product p = Product.lookupByNameOrPartNumberOrSOP(partNumberOrPlateNameOrQC);
        if (null == p) {
            throw new UnknownProductException(partNumberOrPlateNameOrQC);
        }
        return getTemplate(p);
    }

    public static String readDocContent(String file) throws FileNotFoundException, IOException {
        String line, content = "";
        br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            content += line;
        }
        return content;
    }

    public static final String QC_TEMPLATE = "QC_TEMPLATE";//"I:\\inspection docs\\四个现代化\\PLATEPARTNUMBER-QC-Vibrant PLATENAME_FDATE2.xml";
    public static final String FDATE = "FDATE";
    public static final String DATE = "DATE";
    public static final String PLATENAME = "PLATENAME";
    public static final String PLATEPARTNUMBER = "PLATEPARTNUMBER";
    public static final String BATCHRECORDNUMBER = "BATCHRECORDNUMBER";
    
    /*
    xml token
    */
    public static void brTemplateFormatter() throws IOException{
        // loop, get temp
        String content;
        BufferedWriter bw;
        for(File f:new File("src\\resources\\").listFiles()){
            if(f.getName().contains("-BR-")){
                System.out.println("format "+f.getName());
                content = readDocContent(f.getAbsolutePath());
                System.out.println("found? ");System.out.println(content.contains("<w:t>BATCHNUMBER</w:t></w:r><w:r w:rsidR=\"00AD7651\" w:rsidRPr=\"009E1445\"><w:rPr><w:rFonts w:eastAsiaTheme=\"minorEastAsia\" w:cs=\"Arial\"/><w:bCs/><w:szCs w:val=\"24\"/><w:lang w:eastAsia=\"zh-CN\"/></w:rPr><w:t>FROM"));
                content.replaceAll("BATCHNUMBER</w:t></w:r><w:r w:rsidR=\"00AD7651\" w:rsidRPr=\"009E1445\"><w:rPr><w:rFonts w:eastAsiaTheme=\"minorEastAsia\" w:cs=\"Arial\"/><w:bCs/><w:szCs w:val=\"24\"/><w:lang w:eastAsia=\"zh-CN\"/></w:rPr><w:t>FROM", "BATCHNUMBERFROM");
                bw=new BufferedWriter(new FileWriter(f));
                bw.write(content);
                bw.flush();
                bw.close();
            }
        }
            
//
        // get content
        // format
        // save back
    }
}
