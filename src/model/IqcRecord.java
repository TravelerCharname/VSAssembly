/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author LM&L
 */
public class IqcRecord {

    
    public RawMaterial material;
    public Order lastOrder;

    public IqcRecord(RawMaterial material, Order lastOrder) {
        this.material = material;
        this.lastOrder = lastOrder;
    }
    
}
