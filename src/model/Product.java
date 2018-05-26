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
public enum Product {
    ANA(1, "Vibrant ANA 96 Pillar Plate", "910018", "PROCSOP-003-002","ANAC"),
    CEL(2, "Celiac Zoomer 96 Pillar Plate", "910019", "PROCSOP-003-001","CELR8"),
    ENA4(3, "Vibrant ENA 96  Pillar Plate", "910024", "PROCSOP-003-004","ENA4R"),
    WZMR(4, "Wheat Zoomer 96 Pillar Plate", "910026", "PROCSOP-003-005","WZMR"),
    ALPG(5, "ALPS IgG + IgM 96 Pillar Plate", "910037", "PROCSOP-003-015","ALPG"),
    ALPA(6, "ALPS IgA 96 Pillar Plate", "910038", "PROCSOP-003-014","ALPA"),
    FOOG(7, "FS IgG 24 Pillar Plate", "910059", "PROCSOP-003-010","FOOG"),
    FOOA(8, "FS IgA 24 Pillar Plate", "910060", "PROCSOP-003-010","FOOA"),
    IBSG(9, "IBS IgG 96 Pillar Plate", "910063", "PROCSOP-003-008","IBSG"),
    NEUG(10, "Neural IgG 96 Pillar Plate", "910073", "PROCSOP-003-009","NEUG"),
    NEUM(11, "Neural IgM 96 Pillar Plate", "910074", "PROCSOP-003-009","NEUM"),
    Zonulin(12, "Zonulin Pillar Plate", "910107", "Not Released","ZIGR"),
    GUT_ZOOMER(13, "Gut Zoomer Pillar Plate", "910077", "PROCSOP-003-011","BACT"),
    FOOD_ALLERGY(14, "Food Allergy Pillar Plate", "910115", "Not Released","FAAE"),
    INHALANT(15, "Inhalant Allergy Pillar Plate", "910121", "PROCSOP-003-013","UPRE"),
    ENA10(16, "ENA10 IgG Pillar Plate", "910127", "PROCSOP-003-016","ENA10C"),
    TST(17, "TST Pillar Plate", "TEST", "TEST","TST"),
    GPABC(18, "GUT Panel ABC", "TEST", "TEST","GPABC");

    private static final HashMap<String, Product> map;

    static {
        map = new HashMap<>();
        for (Product p : values()) {
            map.put(p.platePartNumber, p);
            map.put(p.plateName, p);
            map.put(p.sop, p);
            map.put(p.prefix, p);
        }
    }

    private Product(int id, String plateName, String platePartNumber, String sop, String prefix) {
        this.id = id;
        this.plateName = plateName;
        this.platePartNumber = platePartNumber;
        this.sop = sop;
        this.prefix = prefix;
    }
    public int id;
    public String plateName;
    public String platePartNumber;
    public String sop;
    public String prefix;

    public static Product lookupByNameOrPartNumberOrSOP(String search) {
        String k = search.trim();
        if (map.containsKey(k)) {
            return map.get(k);
        } else {
            System.out.println("no such material like " + search);
            return null;
        }
    }

    @Override
    public String toString() {
        return "Product_Enum{" + "id=" + id + ", plateName=" + plateName + ", platePartNumber=" + platePartNumber + ", sop=" + sop + '}';
    }
    
}
