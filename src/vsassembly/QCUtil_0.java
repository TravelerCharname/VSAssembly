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
public class QCUtil_0 {

    /**
     * @param args the command line arguments
     */
    private static final String TEMPLATE = "C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\四个现代化\\PLATEPARTNUMBER-QC-Vibrant PLATENAME_FDATE1.xml";
    private static String TEMPLATE_CONTENT;
    public static final String BR = "C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\product realization\\BR\\2017-2018";
    public static final String QC = "C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\product realization\\QC\\2017-2018";
    public static final String hQCh = "-QC-Vibrant ";
    public static final String FDATE = "FDATE";
    public static final String PLATENAME = "PLATENAME";
    public static final String PLATEPARTNUMBER = "PLATEPARTNUMBER";

    private static String part_number, plate_name, date;

    static {
        BufferedReader br = null;
        String line;
        TEMPLATE_CONTENT = "";
        try {
            br = new BufferedReader(new FileReader(TEMPLATE));
            while ((line = br.readLine()) != null) {
                TEMPLATE_CONTENT = TEMPLATE_CONTENT.concat(line);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(QCUtil_0.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QCUtil_0.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(QCUtil_0.class.getName()).log(Level.SEVERE, null, ex);
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
        makeDirs();
//        getANAFiles();

//        getQCInfoFromBR(new File("C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\product realization\\BR\\2017-2018\\ANA\\910018-BR-Vibrant ANA 96 Pillar Plate_17-07-08.doc"));
//        show();
    }

    public static void show() {
        System.out.println(part_number);
        System.out.println(plate_name);
        System.out.println(date);

//        try {
//            // TODO code application logic here
//            test("160304");
//
//        } catch (IOException ex) {
//            Logger.getLogger(QCUtil_0.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

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
//        PLATEPARTNUMBER-QC-Vibrant PLATENAME_FDATE.xml;
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

    static void makeDirs() {
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
                    test(qc_folder);
                } catch (IOException ex) {
                    Logger.getLogger(QCUtil_0.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    static void getQCInfoFromBR(File f) {
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

//    static void getANAFiles() {
//        String root = "C:\\Users\\mlei\\Documents\\work\\sop\\QC Documents\\product realization\\2017-2018\\ANA", name;
//
//        for (File f : new File(root).listFiles()) {
//            name = f.getName();
//            System.out.println(name);
//            name = name.replaceAll("910018-BR-Vibrant ANA 96 Pillar Plate_", "");
//            name = name.replaceAll(".doc", "");
//            System.out.println(name);
//
//            try {
//                test(name);
//            } catch (IOException ex) {
//                Logger.getLogger(QCUtil_0.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
}
