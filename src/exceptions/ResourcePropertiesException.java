/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author LM&L
 */
public class ResourcePropertiesException extends Exception {

    public ResourcePropertiesException(String propName) {
        super(propName);
    }
    
}
