/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;

/**
 *
 * @author LM&L
 */
public enum RawMaterial {
    Plate96(1,"96 Pillar Plate","920001"),
    Wafer(2,"VS Wafer","920002"),
    CarrierTaple(3,"Carrier Tape","920003"),
    UVTape(4,"UV Dicing Tape","920004"),
    Reel(5,"Take-up Reel","920005"),
    CoverTape(6,"Cover Tape","920006"),
    Epoxy(7,"Epoxy Gels","920007"),
    SilicaGel(8,"Packaging Consumables-Silica Gel","920008"),
    ThermoBag(9,"Packaging Consumables-Thermo-protected Bag","920009"),
    Plate24(10,"24 Pillar Plate","920010");
    private static final HashMap<String,RawMaterial> map;
    static{
        map=new HashMap<>();
        for(RawMaterial rm:values()){
            map.put(rm.partNumber, rm);
            map.put(rm.name, rm);
        }
    }
    private RawMaterial(int id,String name,String partNumber) {
        this.id=id;
        this.name=name;
        this.partNumber=partNumber;
    }
    public int id;
    public String name;
    public String partNumber;

    public static RawMaterial lookupByNameOrPartNumber(String search){
        String k=search.trim();
        if(map.containsKey(k))
            return map.get(k);
        else{
            System.out.println("no such material like "+search);
            return null;
        }
    }

    @Override
    public String toString() {
        return "RawMaterial_Enum{" + "id=" + id + ", name=" + name + ", partNumber=" + partNumber + '}';
    }
    
    
}
