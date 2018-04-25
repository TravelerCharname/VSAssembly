/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.ListIterator;
import model.Record;

/**
 *
 * @author LM&L
 * @param <K> sth like prod, raw
 * @param <V> sth like Br/Qc, order
 */


//public class Historian<K,V extends Record> {
// Bookkeeper, historian with db feature
public class Historian<K, V> {

    private HashMap<K, V> latest_hm;
    private OutputStream log;   //append; close every time appended

    /*
    by default add means add a new record
    to latest and history
     */
    public void addNew() {

    }

    public void addHist() {

    }
    /*
    add "deletion record" to history
     */
    public void deleteHistory() {

    }

    /*
    delete the latest then set the second latest as new latest
    call  deleteHistory()
     */
    public void rollbackNew() {

    }

    /*
    map.get(key)
    */
    public void searchNew() {

    }

    /*
    various searchBy()
    search from history
    return an arrayList of qualified results
    
    sorter supportive
    */
    public void searchExisting() {
        //fetch only for created
    }

    /*
    hashmap: put
    call editHistory 
     */
    public void editNew() {

    }

    /*
    history add deleted record
    add edited record
     */
    public void editHistory() {

    }

    /*
    prop to map
     */
    public void refreshNew() {

    }

    /*
    file to arrayList
     */
    public void refreshHist() {

    }

    /*
    map to prop
     */
    public void saveNew() {

    }

    /*
    log append, close
     */
    public void savelog() {

    }

    /*
    today, yesterday,   reset daily
    last week,  reset weekly
    last month, reset monthly
     */
    public void backup() {

    }
    //comment
    /*
    created, deleted, edited
     */
}
