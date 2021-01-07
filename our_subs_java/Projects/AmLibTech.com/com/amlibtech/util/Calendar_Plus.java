/*
 * Calendar_Plus.java
 *
 * Created on December 9, 2004, 8:17 AM
 */

package com.amlibtech.util;

import java.util.*;

/**
 *
 * @author  dgleeson
 */
public class Calendar_Plus {

    private static final String[] Copyright= {
    "|       Copyright (c) 1985 thru 2001, 2002, 2003, 2004, 2005, 2006, 2007       |",
    "|       American Liberator Technologies                                        |",
    "|       All Rights Reserved                                                    |",
    "|                                                                              |",
    "|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |",
    "|       American Liberator Technologies                                        |",
    "|       The copyright notice above does not evidence any                       |",
    "|       actual or intended publication of such source code.                    |"
    };
    
    public static String BOD_TimeZone_Name = "Etc/GMT+12";	// results in gmt-12
    
    public static String[] month_Names = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    public static String[] month_Abbr = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    public static String[] meridian_Abbr = { "AM", "PM" };
    
    /** Creates a new instance of Calendar_Plus */
    private Calendar_Plus() {
    }
    
    
    
    
    public static String get_MDY(Date date, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(date);
        return get_MDY(calendar);
    }
    
    public static String get_MDY(java.sql.Timestamp timestamp, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(timestamp);
        return get_MDY(calendar);
    }
    
    public static String get_MDY(Calendar calendar) {
        return StringPlus.left_Fill((calendar.get(Calendar.MONTH) + 1), 2, '0') + "/" +
        StringPlus.left_Fill(calendar.get(Calendar.DAY_OF_MONTH), 2, '0') + "/" +
        calendar.get(Calendar.YEAR);
    }
    
    
    
    
    public static String get_Y_M_D_For_Sql(Date date, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(date);
        return get_Y_M_D_For_Sql(calendar);
    }
    
    public static String get_Y_M_D_For_Sql(java.sql.Timestamp timestamp, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(timestamp);
        return get_Y_M_D_For_Sql(calendar);
    }
    
    public static String get_Y_M_D_For_Sql(Calendar calendar) {
        return StringPlus.left_Fill(calendar.get(Calendar.YEAR), 4, '0') + "-" +
        StringPlus.left_Fill((calendar.get(Calendar.MONTH) + 1), 2, '0') + "-" +
        StringPlus.left_Fill(calendar.get(Calendar.DAY_OF_MONTH), 2, '0');
    }
    
    
    
    
    
    public static String get_Month_Abbr_Year(Date date, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(date);
        return get_Month_Abbr_Year(calendar);
    }
    
    public static String get_Month_Abbr_Year(java.sql.Timestamp timestamp, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(timestamp);
        return get_Month_Abbr_Year(calendar);
    }
    
    public static String get_Month_Abbr_Year(Calendar calendar) {
        return (month_Abbr[calendar.get(Calendar.MONTH)] + ". " + StringPlus.left_Fill(calendar.get(Calendar.YEAR), 4, '0'));
    }
    
    
    
    
    public static String get_Month_Name_Year(Date date, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(date);
        return get_Month_Name_Year(calendar);
    }
    
    public static String get_Month_Name_Year(java.sql.Timestamp timestamp, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(timestamp);
        return get_Month_Name_Year(calendar);
    }
    
    public static String get_Month_Name_Year(Calendar calendar) {
        return (month_Names[calendar.get(Calendar.MONTH)] + " " + StringPlus.left_Fill(calendar.get(Calendar.YEAR), 4, '0'));
    }
    
    
    
    
    public static String get_Civilian_English_Date_And_Time(Date date, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(date);
        return get_Civilian_English_Date_And_Time(calendar);
    }
    
    public static String get_Civilian_English_Date_And_Time(java.sql.Timestamp timestamp, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(timestamp);
        return get_Civilian_English_Date_And_Time(calendar);
    }
    
    public static String get_Civilian_English_Date_And_Time(Calendar calendar) {
    	String answer= month_Names[calendar.get(Calendar.MONTH)] + " " + 
        calendar.get(Calendar.DAY_OF_MONTH) + ", " +
        calendar.get(Calendar.YEAR) + " ";
	
	if(calendar.get(Calendar.HOUR)==0)
		answer=answer+"12";
	else{
		if(calendar.get(Calendar.HOUR)<10)
			answer=answer+" "+calendar.get(Calendar.HOUR);
		else
			answer=answer+calendar.get(Calendar.HOUR);
	}
	
	answer=answer+ ":" +
        StringPlus.left_Fill(calendar.get(Calendar.MINUTE), 2, '0') + ":" +
        StringPlus.left_Fill(calendar.get(Calendar.SECOND), 2, '0') + " " +
        meridian_Abbr[(calendar.get(Calendar.AM_PM) == Calendar.AM ? 0 : 1)] + " " + 
        calendar.getTimeZone().getDisplayName(calendar.getTimeZone().inDaylightTime(calendar.getTime()), TimeZone.SHORT, Locale.US);
        //+ calendar.getTimeZone().getDisplayName(Locale.US);

	return answer;
        
    }
    
    
    
    
    
    
    public static String get_Civilian_HMS(Date date, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(date);
        return get_Civilian_HMS(calendar);
    }
    
    public static String get_Civilian_HMS(java.sql.Timestamp timestamp, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(timestamp);
        return get_Civilian_HMS(calendar);
    }
    
    public static String get_Civilian_HMS(Calendar calendar) {
        boolean is_AM = calendar.get(Calendar.AM_PM) == Calendar.AM;
        int hour = calendar.get(Calendar.HOUR);
        if (hour == 0)
            hour = 12;
        
        return StringPlus.left_Fill(hour, 2, '0') + ":" + 
        StringPlus.left_Fill(calendar.get(Calendar.MINUTE), 2, '0') + ":" + 
        StringPlus.left_Fill(calendar.get(Calendar.SECOND), 2, '0') + " " +
        meridian_Abbr[(calendar.get(Calendar.AM_PM) == Calendar.AM ? 0 : 1)];
    }
    
    
    
    
    
    
    public static String get_Miliatry_HMS(Date date, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(date);
        return get_Military_HMS(calendar);
    }
    
    public static String get_Military_HMS(java.sql.Timestamp timestamp, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(timestamp);
        return get_Military_HMS(calendar);
    }
    
    public static String get_Military_HMS(Calendar calendar) {
        return StringPlus.left_Fill(calendar.get(Calendar.HOUR_OF_DAY), 2, '0') + ":" + 
        StringPlus.left_Fill(calendar.get(Calendar.MINUTE), 2, '0') + ":" +
        StringPlus.left_Fill(calendar.get(Calendar.SECOND), 2, '0');
    }
    
    
    
    
    public static String get_Y_M_D__HMS_For_Sql(Date date, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(date);
        return get_Y_M_D_For_Sql(calendar) + " " + get_Military_HMS(calendar);
    }
    
    public static String get_Y_M_D__HMS_For_Sql(java.sql.Timestamp timestamp, TimeZone tz) {
        Calendar calendar = Calendar.getInstance(tz, Locale.US);
        calendar.setTime(timestamp);
        return get_Y_M_D_For_Sql(calendar) + " " + get_Military_HMS(calendar);
    }
    
    public static String get_Y_M_D__HMS_For_Sql(Calendar calendar) {
        return get_Y_M_D_For_Sql(calendar) + " " + get_Military_HMS(calendar);
    }
    
    public static TimeZone getBOD_TimeZone(){
        TimeZone tz = TimeZone.getTimeZone(BOD_TimeZone_Name);
        return tz;
    }
    
    public static Date get_BOD(Date passed_Date) {
        
        TimeZone tz = TimeZone.getTimeZone(BOD_TimeZone_Name);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(passed_Date);
        
        // add Zone_Offset to get to GMT
        // add DST_Offset to get to GMT
        // add 12 hours to get to GMT-12
        
        calendar.add(Calendar.HOUR_OF_DAY, 12 + calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000) + calendar.get(Calendar.DST_OFFSET) / (60 * 60 * 1000));
        
        calendar.setTimeZone(tz);
        
        Date result_Date = calendar.getTime();
        return result_Date;
    }
    
    
    public static Date get_BOM(Date passed_Date) {
        
        TimeZone tz = TimeZone.getTimeZone(BOD_TimeZone_Name);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(passed_Date);
        
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        
        Date result_Date = calendar.getTime();
        return result_Date;
    }
    
    public static Date get_EOM(Date passed_Date) {
        TimeZone tz = TimeZone.getTimeZone(BOD_TimeZone_Name);
        
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTime(passed_Date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date result_Date = calendar.getTime();
        return result_Date;
    }
    
}
