/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author mlei
 */
public class InvalidAssayBarcodeException extends Exception {
    public static final String PREFIX="namePrefix";  //4 digits;
    public static final String LOT_NUMBER="lotNumber";   //4 digits; usually 8XXX
    public static final String BATCH_NUMBER="batchNumber"; //4 digits; obsoleted; used to designate the number of assembly
    public static final String PLATE_NUMBER="plateNumber"; //6 digits;
    public InvalidAssayBarcodeException(String string) {
        super(string);
    }
    
}
