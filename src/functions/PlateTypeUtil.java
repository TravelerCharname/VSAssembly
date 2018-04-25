/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.util.HashMap;
import model.Product;

/**
 *
 * @author LM&L
 */
public class PlateTypeUtil {
    /*
    先凑合用
    有时间弄个product list版本的
    product list带cache, 维护一个hashmap, 懒加载: 
    if(hm.get(key)==null) instanciate from prop and put into table
    
    应该把product做成enum
    仿照RawMaterial
    */
    private static HashMap<Product,Integer> hm=new HashMap<>();
  
    /*
    key: product, product_user, product_db
    update cached value each db query or user input
    */
    public static int getCachedPlateType(Product p){
        Integer type=hm.get(p);
        System.out.println("type="+type);
        if(null==type){
            boolean b = p.plateName.toLowerCase().contains("food");
        b=b||p.platePartNumber.contains("910059");
        b=b||p.platePartNumber.contains("910060");
        System.out.println("plate: "+p.plateName);
        System.out.println("plate type: "+(b?24:96));
        }
        
        return type;
    }
    public static int queryDbPlateType(Product p){
        return -1;
    }
    public static int getUserInputPlateType(Product p){
        return -1;
    }
    public static int getPlateType(Product p){
        boolean b = p.plateName.toLowerCase().contains("food");
        b=b||p.platePartNumber.contains("910059");
        b=b||p.platePartNumber.contains("910060");
        System.out.println("plate: "+p.plateName);
        System.out.println("plate type: "+(b?24:96));
        return b?24:96;
    }
}
