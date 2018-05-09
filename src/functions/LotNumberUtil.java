/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
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
    private static final String LOG_ROOT=FolderInitializer.LOG_ROOT;//"C:\\Users\\LM&L\\Desktop\\test\\logs\\lot info history.txt";
    public static void log(LotInfo lot) throws IOException{
        File dest=getLogFile(lot);
        log=new BufferedWriter(new FileWriter(dest));
        log.write(gs.toJson(lot, LotInfo.class));
        log.flush();
        log.close();log=null;
    }

    public static File getLogFile(LotInfo lot) {
        File dest=new File(LOG_ROOT,DocDateUtil.getfDate()+" "+lot.getProd().name()+" "+lot.getLotNumber()+" "+lot.getTotal()+".txt");
        return dest;
    }

}
