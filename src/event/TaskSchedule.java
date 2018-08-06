/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import functions.LotNumberUtil;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.LotInfo;
import model.Product;

/**
 *
 * @author LM&L
 */
public class TaskSchedule extends Application {

    /**
     * @param args the command line arguments
     */
    // Time Periods
    int period;

    // containers
    static HashMap<String, List> name_container;

    /// init containers: other obj put its containers in the map, for task scheduler to maintain
    public static void registerContainers(HashMap map) {
        if (null == name_container) {
            name_container = new HashMap<>();
        }
        name_container.putAll(map);
    }

    // work mode
    boolean isLoacal;

    //Task 1: monitor, every hour
    /*
    data:
    lot info table
     */
    public static void updateLotInfoTableForAllProduct() {

        try {
            //        lotinfotableinitdemo();
            HashMap<String, LotInfo> map = LotNumberUtil.getAllNonTestLot(true);
//            LotNumberUtil.batchInsertUpdateLotinfo(map.values(), true);
            LotNumberUtil.batchInsertUpdateLotinfo(map.values(), true);
            for (Product p : Product.values()) {
                if (p.equals(Product.TST)) {
                    continue;
                }
                LotNumberUtil.getLatestLofInfoCount(p, true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaskSchedule.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    

    //Task 2: log daily
    //Task 3: alert daily
    public static void checkLatestLotInventory() {
        String header = "", info = "";
        try {
            for (Product p : Product.values()) {
                if (p.equals(Product.TST)) {
                    continue;
                }
                LotInfo lot = LotNumberUtil.getLatestLofInfoCount(p, true);
                if (null == lot) {
                    System.out.println("null " + p.plateName);
                    continue;
                }
                if (lot.getAssembled() < 10) {
                    if (!"".equals(header)) {
                        header += ", ";
                    }
                    header += p.prefix;
                    if (!"".equals(info)) {
                        info += "\r\n\r\n";
                    }
                    info += lot.getLotInfoLogEntry();
                } else {
                    System.out.println(lot.getLotInfoDbEntry());
                }
            }
            if (!"".equals(info)) {
                info = "prod\tlotNumber\ttotal\tassembled\tapproved\tfailed\tfinished\ttest\ttesting\tscanning\r\n\r\n" + info;
            }
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Plate inventory is low!");
            alert.setHeaderText(header);
            alert.setContentText(info);
            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(TaskSchedule.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Task 4: show lotInfo
    //Task 5: DB backup, daily
    //Task 6: input new batch
    //Task 7: get IQC data from MS Graph
    //Task 8: UI
    //schedule
    //main thread
    public static void main(String[] args) {
        // TODO code application logic here
//      
//new Alert(AlertType.ERROR, "msg", ButtonType.OK);
//Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle("title");
//        alert.setHeaderText("header msg");
//        alert.setContentText("info msg");
//        alert.showAndWait();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        updateLotInfoTableForAllProduct();
//        checkPlateInventory(false);
//        checkLatestLotInventory();
    }

    static void checkPlateInventory(boolean isLocal) {
        String header = "", info = "";
        try {
            for (Product p : Product.values()) {
                if (p.equals(Product.TST)) {
                    continue;
                }
                LotInfo lot = LotNumberUtil.getLatestLofInfoCount(p, isLocal);
                if (null == lot) {
                    System.out.println("null " + p.plateName);
                    continue;
                }
                if (lot.getAssembled() < MIN_REQUIRE_QUANT) {
                    header = p.prefix;
                    info = lot.getLotInfoLogEntry();
//                    Alert alert = new Alert(AlertType.INFORMATION);
//                    alert.setTitle("Plate inventory is low!");
//                    alert.setHeaderText(header);
//                    alert.setContentText(info);
//                    alert.showAndWait();
                } else {
                    System.out.println(lot.getLotInfoDbEntry());
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(TaskSchedule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static final int MIN_REQUIRE_QUANT = 10;
}
