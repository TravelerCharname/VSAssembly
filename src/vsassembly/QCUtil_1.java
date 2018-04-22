package vsassembly;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mlei
 */
public class QCUtil_1 {

    /**
     * @param args the command line arguments
     */
    /*
    直接改doc失败了 乱码
    改doc另存为的xml可以
     */
    public static final String CEL_TEMPLATE = "I:\\inspection docs\\四个现代化\\910019-BR-Celiact Zoomer 96 Pillar Plate_170109.doc";   //CEL_TEMPLATE = "I:\\inspection docs\\四个现代化\\910019-BR-Celiact Zoomer 96 Pillar Plate_170109.xml";

    private static final String QC_TEMPLATE = "C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\四个现代化\\codebeautify.xml";
    private static String TEMPLATE_CONTENT;
    public static final String BR = "C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\product realization\\BR\\2017-2018";
    public static final String QC = "C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\product realization\\QC\\2017-2018";
    public static final String hQCh = "-QC-Vibrant ";
    public static final String FDATE = "FDATE";
    public static final String PLATENAME = "PLATENAME";
    public static final String PLATEPARTNUMBER = "PLATEPARTNUMBER";

    private static String part_number, plate_name, date;

    public static void init_QC_TEMPLATE_CONTENT() throws RuntimeException {
        BufferedReader br = null;
        String line;
        TEMPLATE_CONTENT = "";
        try {
            br = new BufferedReader(new FileReader(QC_TEMPLATE));
            while ((line = br.readLine()) != null) {
                TEMPLATE_CONTENT = TEMPLATE_CONTENT.concat(line);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(QCUtil_1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QCUtil_1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(QCUtil_1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (TEMPLATE_CONTENT.contains(FDATE)) {
            System.out.println("found " + FDATE);
        } else {
            throw new RuntimeException("no " + FDATE);
        }
        if (TEMPLATE_CONTENT.contains(PLATENAME)) {
            System.out.println("found " + PLATENAME);
        } else {
            throw new RuntimeException("no " + PLATENAME);
        }
        if (TEMPLATE_CONTENT.contains(PLATEPARTNUMBER)) {
            System.out.println("found " + PLATEPARTNUMBER);
        } else {
            throw new RuntimeException("no " + PLATEPARTNUMBER);
        }
    }

    public static void main(String[] args) {
//        demo();
//        sopBRList();

    }

    public static void demo() {
        batchRenameGut();
//        try {
        generateQCFromBR();

//            modifyCelBR();
//        getANAFiles();
//        getQCInfoFromBR(new File("C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\product realization\\BR\\2017-2018\\ANA\\910018-BR-Vibrant ANA 96 Pillar Plate_17-07-08.doc"));
//        show();
//        } catch (IOException ex) {
//            Logger.getLogger(QCUtil_1.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    static void getQCInfoFromBR(File f) {
        init_QC_TEMPLATE_CONTENT();

        String fileName = f.getName();    //910018-BR-Vibrant ANA 96 Pillar Plate_17-07-08.doc
        if (fileName.contains("NEUR") || fileName.contains("~$")) {
            return;
        }

        part_number = fileName.substring(0, 6);

        int b, e, d;
        b = fileName.indexOf("-BR-") + 4;
        e = fileName.indexOf("_");
        d = fileName.indexOf(".");
        plate_name = fileName.substring(b, e);
        plate_name = plate_name.replaceAll("Vibrant ", "");

        date = fileName.substring(e + 1, d);
    }

    public static void show() {
        System.out.println(part_number);
        System.out.println(plate_name);
        System.out.println(date);
    }

    static void create1QCAndSaveToDest(File dest) throws FileNotFoundException, IOException {
        String content, fDate, name;
        File f;
        char[] ar = date.toCharArray();
        char[] tar = new char[9];
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                tar[i] = '-';
            } else {
//                System.out.println("index: "+(i / 3 *2+i%3-1));
                tar[i] = ar[i / 3 * 2 + i % 3 - 1];
            }
        }
        fDate = String.valueOf(tar);
        name = part_number + hQCh + plate_name + fDate + ".doc";
//        System.out.println(String.valueOf(tar));
        f = new File(dest, name);
//        if (f.exists()) {
//            System.out.println(f.getName() + " youle");
//            return;
//        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//        System.out.println(content);
        content = TEMPLATE_CONTENT.replaceAll(PLATENAME, plate_name);
        content = content.replaceAll(PLATEPARTNUMBER, part_number);
        content = content.replaceAll(FDATE, fDate);
        bw.write(content);
        bw.flush();
        bw.close();
    }

    static void generateQCFromBR() {
        File qc_folder;
        for (File br_folder : new File(BR).listFiles()) {
            qc_folder = new File(QC, br_folder.getName() + " QC");
            if (!qc_folder.exists()) {
                qc_folder.mkdirs();
                System.out.println("make folder: " + qc_folder.getAbsolutePath());
            }
            for (File br : br_folder.listFiles()) {
                getQCInfoFromBR(br);
                if ("BACT QC".equals(qc_folder.getName())) {
                    plate_name = "Gut Zoomer 96 Pillar Plate";
                }
                try {
                    //                show();
                    create1QCAndSaveToDest(qc_folder);
                } catch (IOException ex) {
                    Logger.getLogger(QCUtil_1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    static void modifyCelBR() throws IOException {

        BufferedReader br = null;
        BufferedWriter bw;
        String content, line;
        File dir = new File(CEL_TEMPLATE).getParentFile(), dest;

        for (File cel : new File("C:\\Users\\mlei\\Documents\\work\\sop\\QC Documents\\product realization\\BR\\2017-2018\\CEL").listFiles()) {
            content = "";
            try {
                br = new BufferedReader(new FileReader(cel));
                while ((line = br.readLine()) != null) {
                    content = content.concat(line);
                }
                if (content.contains("VS27")) {
                    System.out.println("found VS27");
                } else {
                    throw new RuntimeException("no VS27");
                }
                content = content.replaceAll("VS27", "VS143, VS144");

                dest = new File(dir, cel.getName());

                bw = new BufferedWriter(new FileWriter(dest));
                bw.write(content);
                bw.flush();
                bw.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(QCUtil_1.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(QCUtil_1.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (null != br) {
                        br.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(QCUtil_1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        TEMPLATE_CONTENT = "";

//        if (TEMPLATE_CONTENT.contains(PLATENAME)) {
//            System.out.println("found " + PLATENAME);
//        } else {
//            throw new RuntimeException("no " + PLATENAME);
//        }
//        if (TEMPLATE_CONTENT.contains(PLATEPARTNUMBER)) {
//            System.out.println("found " + PLATEPARTNUMBER);
//        } else {
//            throw new RuntimeException("no " + PLATEPARTNUMBER);
//        }
    }

    /*
    直接改doc失败了 乱码
     */
    static void test(File dest) throws FileNotFoundException, IOException {
        String content, fDate, name;
        File f;
        char[] ar = date.toCharArray();
        char[] tar = new char[9];
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                tar[i] = '-';
            } else {
//                System.out.println("index: "+(i / 3 *2+i%3-1));
                tar[i] = ar[i / 3 * 2 + i % 3 - 1];
            }
        }
        fDate = String.valueOf(tar);
        name = part_number + hQCh + plate_name + fDate + ".doc";
//        System.out.println(String.valueOf(tar));
        f = new File(dest, name);
//        if (f.exists()) {
//            System.out.println(f.getName() + " youle");
//            return;
//        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//        System.out.println(content);
        content = TEMPLATE_CONTENT.replaceAll(PLATENAME, plate_name);
        content = content.replaceAll(PLATEPARTNUMBER, part_number);
        content = content.replaceAll(FDATE, fDate);
        bw.write(content);
        bw.flush();
        bw.close();
    }

    static void batchRenameGut() {
        File src = new File("C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\product realization\\BR\\2017-2018\\BACT"), tar;
        for (File f : src.listFiles()) {
            tar = new File(f.getAbsolutePath().replaceAll("Wheat Zoomer", "Gut Zoomer"));
            boolean success = f.renameTo(tar);
            System.out.println(success);
        }
    }

    static void sopBRList() {
        File root = new File("C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\"), tar;
        BufferedWriter bw;
        String line;
        int count;
        for (File prod : new File(BR).listFiles()) {
            try {
                tar = new File(root, prod.getName() + ".txt");
                count = 0;
                bw = new BufferedWriter(new FileWriter(tar));
                for (File br : prod.listFiles()) {
                    line = br.getName();
                    if (line.contains(".docx")) {
                        line = line.replaceAll(".docx", ", ");
                    } else {
                        line = line.replaceAll(".doc", ", ");
                    }
                    bw.write(line);
                    count++;
                    if (0 == count % 2) {
                        bw.newLine();
                    }
                    bw.flush();
                }
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(QCUtil_1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
