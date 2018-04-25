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
public enum Users {
    BLANK(1, null, null, null,false),
    MING(2, "Ming Lei", "Assembly", "Ming Lei",false);

    private static final HashMap<String, Users> map;

    static {
        map = new HashMap<>();
        for (Users p : values()) {
            map.put(p.userName, p);
        }
    }

    private Users(int id, String userName, String userDepartment, String userSignature,boolean signatureReady) {
        this.id = id;
        this.userName = userName;
        this.userDepartment = userDepartment;
        this.userSignature = userSignature;
        this.signatureReady=signatureReady;
    }
    public int id;
    public String userName;
    public String userDepartment;
    public String userSignature;
    public boolean signatureReady;

    public static Users lookupByNameOrPartNumberOrSOP(String search) {
        String k = search.trim();
        if (map.containsKey(k)) {
            return map.get(k);
        } else {
            System.out.println("no such user like " + search);
            return null;
        }
    }
}
