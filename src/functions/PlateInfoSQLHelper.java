/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.AssayBarcode;
import model.Product;

/**
 *
 * @author mlei
 */
public class PlateInfoSQLHelper {
    // lot -> product -> resultset -> al<Barcode>
    public static ArrayList<AssayBarcode> latestLotOfPlateInfo(Product p) throws SQLException{
       ArrayList<AssayBarcode> al=new ArrayList<>();
        ResultSet rs = PrimitiveConn.lotInfoByProduct(p, true);
        while(rs.next()){
//            AssayBarcode ab=new AssayBarcode
        }
       return al;
    }
}
