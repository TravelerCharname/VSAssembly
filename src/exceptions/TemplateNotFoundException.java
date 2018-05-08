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
public class TemplateNotFoundException extends Exception {

    private final String msg;

    public TemplateNotFoundException(String msg) {
        this.msg = msg;
    }

    public TemplateNotFoundException() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
