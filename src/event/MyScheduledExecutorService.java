/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import functions.LotNumberUtil;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.LotInfo;
import model.Product;

/**
 *
 * @author mlei
 */
public class MyScheduledExecutorService {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        test();
    }
    static long start;

    public static void test() {
        ScheduledExecutorService newScheduledThreadPool = Executors
                .newScheduledThreadPool(2);

        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                try {
                    updateAndBackup();

                    System.out.println("task1 invoked ! "
                            + new java.util.Date(System.currentTimeMillis()));
//                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                TaskSchedule.updateLotInfoTableForAllProduct();
                System.out.println("task2 invoked ! "
                        + (System.currentTimeMillis() - start));
            }
        };
//		start = System.currentTimeMillis();
        newScheduledThreadPool.scheduleAtFixedRate(task1,0, 3600000, TimeUnit.MILLISECONDS);
//        newScheduledThreadPool.scheduleAtFixedRate(task2,0, 3600000, TimeUnit.MILLISECONDS);
    }

    public static void updateAndBackup(){
        try {
            HashMap<String, LotInfo> map = LotNumberUtil.getAllNonTestLot(false);
            int i=0;
            for(String key:map.keySet()){
                System.out.println(i+++" "+key+" : "+map.get(key).getLotInfoDbEntry());
            }
            LotNumberUtil.batchInsertUpdateLotinfo(map.values(), false);
            
            for (Product p : Product.values()) {
                if (p.equals(Product.TST)) {
                    continue;
                }
                LotNumberUtil.getLatestLofInfoCount(p, false);
            }
            LotNumberUtil.batchInsertUpdateLotinfo(map.values(), true);
        } catch (SQLException ex) {
            Logger.getLogger(MyScheduledExecutorService.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
}
