/* LaDate.java */
/**
 *
 * Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004
 * American Liberator Technologies
 * All Rights Reserved
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
 * American Liberator Technologies
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 **/

package com.amlibtech.util;

/**
 * Long Absolute Date : using the Gregorian calendar, and assuming it has been used
 * since the' start of the common era, an LaDate.value is the number of days since
 * 01/01/0000.
 * It knows about leap years and leap centuries.
 * It dones NOT revert to the julian calandar at any time.
 * It does not handle negative dates.  I.E. no BC Dates.
 *      Days	Date
 *	0	01/01/0000
 *	9	01/10/0000
 *	99	04/09/0000
 *	999	09/26/0002
 *	9999	05/18/0027
 *	99999	10/15/0273
 *	999999	11/27/2737
 *	9999999	01/25/27379
 *
 **/

public class LaDate {
    public long	value;
    
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
    
    /**
     * Creates a Long_Absolute_Date from the month, day, century, year values
     **/
    public LaDate(int century, int year, int month, int day) {
        int[]	days = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
        
        int	i, month_days, century_leap_days;
        int	year_leap_days;
        int	month_leap_days;
        
        this.value = 0;
        
        
        if (month == 0)
            month = 1;
        
        while (month > 12) {
            month -= 12;
            year++;
        }
        
        while (year > 99) {
            year -= 99;
            century++;
        }
        
        century_leap_days=(century+3)/4;
        year_leap_days=Math.abs((year-1)/4);
        if(century %4 ==0 && year>0)
            year_leap_days++;
        
        month--;
        month_days = days[month];
        month_leap_days=0;
        
        if(year==0){
            if(century%4 ==0)
                month_leap_days=1;
            
        }else if(year %4 ==0)
            month_leap_days=1;
        
        if (month > 1)
            month_days += month_leap_days;
        
        
        this.value = century * 36524 + century_leap_days + year * 365 + year_leap_days + month_days + day - 1;
   
    }
    
    
    /**
     * Creates a Long_Absolute_Date from the Gregorian Date in a String of 10 characters.
     * i.e.  mm/dd/ccyy,   12/25/2004
     **/
    public static LaDate from_Gregorian_10mdcy(String date_str) throws Bad_Date_Exception {
        int century, year, month, day;
        if(date_str.length()!=10){
            throw new Bad_Date_Exception("Date String :" + date_str + ": Not 10 Characters long, only " + date_str.length() +".");
        }else{
            try {
                month=new Integer(date_str.substring(0,2)).intValue();
                day=new Integer(date_str.substring(3,5)).intValue();
                century=new Integer(date_str.substring(6,8)).intValue();
                year=new Integer(date_str.substring(9)).intValue();
            } catch(NumberFormatException nfe) {
                throw new Bad_Date_Exception("Date String :" + date_str + ": Not a valid format.");
            }
            
            return( new LaDate(century, year, month, day));
        }
        
    }
    
    
    /**
     * Creates a Long_Absolute_Date from the Gregorian Date in a String of 8 characters.
     * i.e.  mm/dd/yy,   12/25/04
     * the century is assumed to be 19.
     **/
    public static LaDate from_Gregorian_8mdy(String date_str) throws Bad_Date_Exception {
        int century, year, month, day;
        if(date_str.length()!=8){
            throw new Bad_Date_Exception("Date String :" + date_str + ": Not 8 Characters long, only " + date_str.length() +".");
        }else{
            month=new Integer(date_str.substring(0,2)).intValue();
            day=new Integer(date_str.substring(3,5)).intValue();
            century=19;
            year=new Integer(date_str.substring(6,8)).intValue();
            return( new LaDate(century, year, month, day));
        }
    }
    
    
    /**
     * Converts a Long_Absolute_Date to the Gregorian Date in a String of 10 characters.
     * i.e.  mm/dd/ccyy,   12/25/2004
     * see to_Gregorian_10mdcy()
     **/
    public String toString() {
        return(this.to_Gregorian_10mdcy());
    }
    
    
    
    
    /**
     * Converts a Long_Absolute_Date to the Gregorian Date in a String of 8 characters.
     * i.e.  mm/dd/yy,   12/25/04
     **/
    public String	to_Gregorian_8mdy() {
        LaDate_Record ladate_record = new LaDate_Record(this);
        return(StringPlus.left_Fill(ladate_record.getMonth_of_year()+1, 2, '0') +"/"
        + StringPlus.left_Fill(ladate_record.getDay_of_month()+1, 2, '0') +"/"
        + StringPlus.left_Fill(ladate_record.getYear(), 2, '0'));
    }
    
    /**
     * Converts a Long_Absolute_Date to the Gregorian Date in a String of 10 characters.
     * i.e.  mm/dd/ccyy,   12/25/2004
     **/
    
    public String to_Gregorian_10mdcy() {
        LaDate_Record ladate_record = new LaDate_Record(this);
        return(StringPlus.left_Fill(ladate_record.getMonth_of_year()+1, 2, '0') +"/"
        + StringPlus.left_Fill(ladate_record.getDay_of_month()+1, 2, '0') +"/"
        + StringPlus.left_Fill(ladate_record.getCentury(), 2, '0')
        + StringPlus.left_Fill(ladate_record.getYear(), 2, '0'));
    }
    
/*
 
returns January 01, 1990
char	*la2gstr(t_ladate passed_ladate)
 
returns day's name  sun,mon,tue,wed,thu,fri,sat
char	*ldaynam(t_ladate	passed_ladate)
 
return number of days since 01/01/00 of century 0
long	lg2adate(int century, char *pdate)
 
return number of days since 01/01/0000
t_ladate	lG2adate(char *pdate)
 
convert (long) absolute date to string  (for keys)
char	*ladate2str(t_ladate passed_ladate)
 
convert string  (for keys) to (long) absolute date
t_ladate	str2ladate(char *passed_string)
 
returns day's number  sun,mon,tue,wed,thu,fri,sat  0-6
int	week_daynum(t_ladate passed_ladate)
 
returns day's number  0-30
int	month_daynum(t_ladate passed_ladate)
 
returns day's number  0-365
int	year_daynum(t_ladate passed_ladate)
 
return local date  as t_ladate
t_ladate	sysladate()
 
returns first day of same month of passed ladate
t_ladate	bom_ladate(t_ladate	ladate)
 
returns first day of same week of passed ladate
t_ladate	bow_ladate(t_ladate	ladate)
 
returns first day of same year of passed ladate
t_ladate	boy_ladate(t_ladate	ladate)
 
returns last day of same month of passed ladate
t_ladate	eom_ladate(t_ladate	ladate)
 
returns last day of same week of passed ladate
t_ladate	eow_ladate(t_ladate	ladate)
 
returns last day of same year of passed ladate
t_ladate	eoy_ladate(t_ladate	ladate)
 
return start_of_four_week_period date from passed date(normally sysladate)
t_ladate	four_wk_strt(t_ladate passed_ladate)
 
 
compare 2 ladate for diff in centry, year and month
returns (yyyy*12+mm) - (yyyy*12+mm)
int	ladate_cym_cmp(t_ladate	ladate1, t_ladate	ladate2)
 
 
increment ladate by number of months
t_ladate	ladd_month(t_ladate passed_ladate, int num_months)
 
returns first day of same Quarter of passed ladate
t_ladate	boq_ladate(t_ladate	ladate)
 
returns last day of same Quarter of passed ladate
t_ladate	eoq_ladate(t_ladate	ladate)
 
return first day of quarter(1-4) of year(yyyy)
t_ladate	q2ladate(int qtr, long year)
 
 */
    
    
    
}
