/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsassembly;

import exceptions.InvalidAssayBarcodeException;
import functions.LotNumberUtil;
import functions.PrimitiveConn;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.LotInfo;
import model.PillarPlateInfo;
import model.Product;
import static model.Product.TST;

/**
 *
 * @author mlei
 */
public class LocalTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        lotinfotableinitdemo();
    }

    public static void onePlateTest() {
        try {
            //get plate with iffy name WZMR80010007000007_0
            String sql="SELECT * FROM pillar_plate_info.pillar_plate_info where pillar_plate_id like \"%WZMR80010007000007_0%\";";
            ResultSet rs = PrimitiveConn.generateRecordThrows(PrimitiveConn.LOCAL_SCHEMA, sql, true);
            ArrayList<PillarPlateInfo> plates = PillarPlateInfo.plateListFromDB(rs);
            PillarPlateInfo demo=plates.get(0);
            //check plate type
//            System.out.println("demo type = "+demo.plate_type);
//            System.out.println("demo: "+demo);

System.out.println("barcode: "+demo.getBarcode());
//debug
        } catch (SQLException ex) {
            Logger.getLogger(LocalTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void lotinfotableinitdemo() {
        //        lotutildemo();
        try {
            for (Product p : Product.values()) {
                if(p.equals(Product.TST))continue;
                LotNumberUtil.initLotInfoDbForProduct(p, false);
//                LotNumberUtil.getLatestLofInfoCount(p, true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocalTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void lotutildemo() {
        try {
            HashMap<String, LotInfo> map = new HashMap<>();
            ArrayList<PillarPlateInfo> plates = LotNumberUtil.getAllPlatesForProduct(Product.CEL, true);
            String key;
            LotInfo l;
            for (PillarPlateInfo p : plates) {
                key = p.blindLotNumber();
                l = map.get(key);
                if (null == l) {
                    l = new LotInfo(Product.CEL, key, new ArrayList<>());
                }
                l.getPlates().add(p);
                map.put(key, l);
            }

            for (LotInfo lot : map.values()) {
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");

                lot.isConsistent();
                lot.autoCount();
                System.out.println("lot num: " + lot.getLotNumber());
                System.out.println(lot.getLotInfoLogEntry());
                lot.show();
            }
//        lotInfoDemo1();
        } catch (SQLException ex) {
            Logger.getLogger(LocalTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void lotInfoDemo1() {
        try {
            //        charTest();
            ArrayList<PillarPlateInfo> plates = LotNumberUtil.getAllPlatesForProduct(Product.ANA, true);
            LotInfo aLot = LotInfo.getLatestLotFromPlates(plates);
//LotInfo aLot=new LotInfo(Product.ANA, "8007", plates);
//aLot.show(plates);
            LotNumberUtil.log(aLot);
        } catch (SQLException ex) {
            Logger.getLogger(LocalTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAssayBarcodeException ex) {
            Logger.getLogger(LocalTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LocalTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void charTest() {
        //        try {
//            resultSetToPillarPlatesTest();
//        } catch (SQLException ex) {
//            Logger.getLogger(LocalTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
        String s = "01234567890";
        char[] chars = s.toCharArray();
        System.out.println("char len = " + chars.length);
        int b = 0;
        for (int i = 0; i < 11; i++) {
            if (0 == (i % 3)) {
                System.out.println(new String(chars, b, i - b));
                b = i;
            }
        }
        System.out.println(new String(chars, b, 11 - b));
    }
//
//    public static void resultSetToPillarPlatesTest() throws SQLException {
//        //            productionDBDemo();
//        PrimitiveConn.generateRecordThrowsLocal(PrimitiveConn.LOCAL_SCHEMA,"SELECT * FROM `"+PrimitiveConn.LOCAL_SCHEMA+"`.`pillar_plate_info` order by test_name,status,assemble_time;");
//        ResultSet newRs = PrimitiveConn.stmt.getResultSet();
//        PillarPlateInfo.plateListFromDB(newRs);
//    }

//    public static void localDBDemo() throws SQLException {
//        System.out.println("==== 2nd time ====");
//        PrimitiveConn.generateRecordThrowsLocal(PrimitiveConn.LOCAL_SCHEMA,"SELECT * FROM `"+PrimitiveConn.LOCAL_SCHEMA+"`.`pillar_plate_info` order by test_name,status,assemble_time;");
//        System.out.println("==== test separately ====");
//        while (PrimitiveConn.rs.next()) {
//            System.out.println(PrimitiveConn.rs.getString(1) + "  " + PrimitiveConn.rs.getString(2) + "  " + PrimitiveConn.rs.getString(3));
//        }
//        System.out.println("yong diao le");
//        ResultSet newRs = PrimitiveConn.stmt.getResultSet();
//        while (newRs.next()) {
//            System.out.println(newRs.getString(1) + "  " + newRs.getString(2) + "  " + newRs.getString(3));
//        }
//    }
//    public static void productionDBDemo() throws SQLException {
//        PrimitiveConn.generateRecordThrows(PrimitiveConn.VIBRANT_TEST_TRACKING,"SELECT * FROM `vibrant_test_tracking`.`pillar_plate_info` order by test_name,status,assemble_time;");
//        System.out.println("==== test separately ====");
//        String schema = PrimitiveConn.con.getSchema();  System.out.println(schema);
//        Statement text = PrimitiveConn.rs.getStatement();  System.out.println(text.isCloseOnCompletion());
//        while (PrimitiveConn.rs.next()) {
//            System.out.println(PrimitiveConn.rs.getString(1) + "  " + PrimitiveConn.rs.getString(2) + "  " + PrimitiveConn.rs.getString(3));
//        }
//        System.out.println("yong diao le");
//        ResultSet newRs = PrimitiveConn.stmt.getResultSet();
//        while (newRs.next()) {
//            System.out.println(newRs.getString(1) + "  " + newRs.getString(2) + "  " + newRs.getString(3));
//        }
//    }
    public static void dateTimeFormaterTest() {
        //        try {
//            // TODO code application logic here
////            fileSeparator("C:\\Users\\mlei\\Documents\\dumps\\ff 031418\\food_fact_db.sql", "J:\\ff\\ff 031418", 10);
//            filemove("C:\\Users\\mlei\\Documents\\dumps\\ff 031418\\food_fact_db.sql", "J:\\ff\\ff 031418\\food_fact_db.sql");
//        } catch (IOException ex) {
//            Logger.getLogger(LocalTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//int max=2*1024*1024*1024,size=0,ct=1;
//        System.out.println("max="+max);
//        System.out.println(max>1043494);
        long currentTimeMillis = System.currentTimeMillis();
        java.util.Date javaUtilDate = new java.util.Date(currentTimeMillis);
        java.sql.Date javaSqlDate = new java.sql.Date(currentTimeMillis);

        LocalDate localDate = javaSqlDate.toLocalDate();
        System.out.println("local date " + localDate);
        System.out.println(localDate.getYear());
        System.out.println(localDate.getMonthValue());
        System.out.println(localDate.getDayOfMonth());

        System.out.println("util date " + javaUtilDate);  //util date Mon Mar 19 12:12:54 PDT 2018
        System.out.println("sql date " + javaSqlDate);    //sql date 2018-03-19
        Instant instantNow = Instant.now();
        System.out.println("instant now " + instantNow);
//        System.out.print(instantNow.get(ChronoField.YEAR_OF_ERA));
//        System.out.print(instantNow.get(ChronoField.MONTH_OF_YEAR));
//        System.out.print(instantNow.get(ChronoField.DAY_OF_MONTH));

        System.out.println(javaSqlDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd")));
    }

    static void fileSeparator(String src, String dest, int nParts) throws FileNotFoundException, IOException {
        File srcf = new File(src), desf;
        long Size = srcf.getTotalSpace(), size = Size / nParts + 1;
        System.out.println("Size=" + Size + " size=" + size);
        byte[] buff = new byte[(int) size];
        FileInputStream fis = new FileInputStream(srcf);
        FileOutputStream fos;
        int ct = 0;
        while (Size > 0) {
            fis.read(buff);
            fos = new FileOutputStream(new File(dest, srcf.getName().replaceAll("\\.", ct++ + "\\.")));
            fos.write(buff);
            fos.close();

            Size -= size;
        }
    }

    synchronized static void filemove(String src, String dest) throws FileNotFoundException, IOException {
        String line;
        long max = 2 * 1024 * 1024 * 1024l, size = 0, ct = 1;
        BufferedReader br = new BufferedReader(new FileReader(src));
        BufferedWriter bw = new BufferedWriter(new FileWriter("J:\\ff\\ff 031418\\food_fact_db piece " + ct++ + ".sql"));

        synchronized (bw) {
            while ((line = br.readLine()) != null) {
                size += line.length();

                if (Long.compare(max, size) < 0) {
                    System.out.println("No " + ct);
                    System.out.println("max=" + max + " size=" + size);
                    bw = new BufferedWriter(new FileWriter("J:\\ff\\ff 031418\\food_fact_db piece " + ct++ + ".sql"));
                    size = 0;
                }
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
        }
        br.close();
        bw.close();
    }
}
