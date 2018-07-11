/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import static model.RawMaterial.values;

/**
 *
 * @author LM&L
 */
public enum Status {

    Approve(1),
    assembled(2),
    finish(3),
    test(4),
    Scanning(5),
    Testing(6),
    Fail(7),
    unknown(0);
    public int id;

//    public String name;
//    public String partNumber;
    private static final HashMap<String, Status> map;

    static {
        map = new HashMap<>();
        for (Status s : values()) {
            map.put(s.name().toUpperCase(), s);
        }
    }

    private Status(int id) {
        this.id = id;
    }

    public static Status getStatusByName(String name) {
        String k = name.trim().toUpperCase();
        if (map.containsKey(k)) {
            return map.get(k);
        } else {
            System.out.println("unknown status: "+name);
            return unknown;
        }
    }
}
