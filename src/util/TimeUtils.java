/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author aurea
 */
public class TimeUtils {

    public static long getGpsTimeMillis(Double weekNumber, Double timeOfWeek) {
        GregorianCalendar gc1 = new GregorianCalendar(1980,Calendar.JANUARY,6,0,0,0);  
        GregorianCalendar c = new GregorianCalendar();  
               
        long elaps = gc1.getTimeInMillis();  
        long time = (long) (elaps +weekNumber*7*24*3600*1000 + timeOfWeek*1000); 
        return time;
    }

    public static Date getDateFromGpsTime(Double weekNumber, Double timeOfWeek) {

        return new Date(getGpsTimeMillis(weekNumber, timeOfWeek));
    }
}
