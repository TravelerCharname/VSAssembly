/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author LM&L
 */
public class TaskSchedule {

    /**
     * @param args the command line arguments
     */
    // Time Periods
    int period;
    
    
    // containers
    static HashMap<String, List> name_container;
    /// init containers: other obj put its containers in the map, for task scheduler to maintain
    public static void registerContainers(HashMap map){
        if(null==name_container) name_container=new HashMap<>();
        name_container.putAll(map);
    }
    
    //Task 1: monitor
    /*
    data:
    lot info table
    */
    
    //Task 2: log daily
    
    
    //Task 3: alert daily
    
    
    //Task 4: show lotInfo
    
    
    //Task 5: DB backup
    
    
    //Task 6: input new batch
    
    
    //Task 7: get IQC data from MS Graph
    
    
    //Task 8: UI
    
    
    //schedule
    
    
    //main thread
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
                          
}
