/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
                    TaskSchedule.checkPlateInventory(false);

                    System.out.println("task1 invoked ! "
                            + (System.currentTimeMillis() - start));
                    Thread.sleep(3000);
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
        newScheduledThreadPool.schedule(task1, 10000, TimeUnit.MILLISECONDS);
        newScheduledThreadPool.schedule(task2, 30000, TimeUnit.MILLISECONDS);
    }

}
