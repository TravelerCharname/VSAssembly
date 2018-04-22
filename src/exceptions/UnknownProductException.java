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
public class UnknownProductException extends Exception {

    public UnknownProductException() {
    }

    public UnknownProductException(String partNumberOrPlateName) {
        super("Don't Recognize Product Plate Named: "+partNumberOrPlateName);
    }
    
}
