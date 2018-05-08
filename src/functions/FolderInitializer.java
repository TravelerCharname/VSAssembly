/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author LM&L
 */
public class FolderInitializer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        System.getProperties().list(System.out);
initializeAll();
    }
    public static final String USER=System.getProperty("user.name");
//    private static final ArrayList<String> paths=new ArrayList<>();
    public static void initialize(String folderPath){
        if(null==folderPath) return;
        File folder=new File(folderPath);
        if(folder.isDirectory()&&!folder.exists()){
            boolean ok = folder.mkdirs();
            if(ok){
                System.out.println(folderPath+" was successfully created");
            }
        }
    }
    public static void initializeAll(){
        String[] paths = {BR_ROOT,LOG_FILE,QC_ROOT};
        
        for(String path:paths){
            initialize(path);
        }
    }
//    public static void add(String path){
//        if(null!=path) paths.add(path);
//    }
    
    public static final String BR_ROOT = "C:\\Users\\"+USER+"\\Desktop\\test\\BR\\2017-2018\\";
    public static final String LOG_FILE="C:\\Users\\"+USER+"\\Desktop\\test\\logs\\lot info history.txt";
    public static final String QC_ROOT="C:\\Users\\"+USER+"\\Desktop\\test\\QC\\2017-2018\\";//"C:\\Users\\LM&L\\Desktop\\微型办公室\\inspection docs\\QC Documents\\product realization\\QC\\2018-2019\\";
}
