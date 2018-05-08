/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 *
 * @author mlei
 */
public class DocDateUtil {
    public static int getMonthValue() {
        return systemCurrentDate.getMonthValue();
    }
    private static LocalDate systemCurrentDate= LocalDate.now();
//    public static LocalDate tempDate= LocalDate.now();

    public static Month getMonthObj() {
        return systemCurrentDate.getMonth();
    }

    public static int getDayOfMonth() {
        return systemCurrentDate.getDayOfMonth();
    }

    public static int getYear() {
        return systemCurrentDate.getYear();
    }

    public static String getfDate() {
        return systemCurrentDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
    }

    public static String getyyMMdd() {
        return systemCurrentDate.format(DateTimeFormatter.ofPattern("yyMMdd"));
    }
    public static String getSignDate() {
        return systemCurrentDate.format(DateTimeFormatter.ofPattern("yy/MM/dd"));
    }

    public static LocalDate resetSystemCurrentDate(){
        systemCurrentDate= LocalDate.now();
        System.out.println("System current date is set to "+systemCurrentDate);
        return systemCurrentDate;
    }
    public static LocalDate localDateWith(int y,int M,int d){
        LocalDate tempDate = LocalDate.of(y, M, d);
        System.out.println("temp current date is set to "+tempDate);
        return tempDate;
    }
    public static String localDate2fDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
    }
    public static String localDate2yyMMdd(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyMMdd"));
    }
    /**
    * @param date must format to yyyy-MM-dd
    * @return parsed LocalDate obj
    */
    public static LocalDate parseLocalDate(String date) throws DateTimeParseException{
        LocalDate tempDate = LocalDate.parse(date);
        System.out.println("temp current date is set to "+tempDate);
        return tempDate;
    }
}
