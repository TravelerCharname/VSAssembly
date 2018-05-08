/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import model.LotInfo;
import model.Product;

/**
 *
 * @author mlei
 */
public class LotNumberUtil {
    private static Properties _latest;  // update when input a new batch
    private static final HashMap<Product,LotInfo> hm_latest=new HashMap<>();  //serves as a HIDDEN cache, invisible to outside classes
    private static final Gson gs = new Gson();
    // keep daily record of lot info in log
    // for history
    // date-time product lot-num quantity
    
    private static BufferedWriter log;
    private static final String LOG_FILE=FolderInitializer.LOG_FILE;//"C:\\Users\\LM&L\\Desktop\\test\\logs\\lot info history.txt";
    public static void log(LotInfo lot) throws IOException{
        if(null==log){
            resetLogger();
        }
    }

    private static void resetLogger() throws IOException {
        // nong ge dir initializer tongyi zao dir
        log=new BufferedWriter(new FileWriter(LOG_FILE));
    }
}
