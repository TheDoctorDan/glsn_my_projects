/*
 * DFDay_Of_Week.java
 *
 * Created on July 31, 2006, 12:20 PM
 */

package com.amlibtech.data_fields;

import java.util.*;

/**
 *
 * @author  dgleeson
 */
public class DFDay_Of_Week_Alpha extends DFString_Enumerated {
    
    public static final int DAY_OF_WEEK_SUNDAY=0;
    public static final int DAY_OF_WEEK_MONDAY=1;
    public static final int DAY_OF_WEEK_TUESDAY=2;
    public static final int DAY_OF_WEEK_WEDNESDAY=3;
    public static final int DAY_OF_WEEK_THURSDAY=4;
    public static final int DAY_OF_WEEK_FRIDAY=5;
    public static final int DAY_OF_WEEK_SATURDAY=6;
    
    
    public static final String[] day_Values = { "Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat" };

    /** Creates a new instance of DFDay_Of_Week */
    public DFDay_Of_Week_Alpha(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title, day_Values);
    }
    
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
    
    
    public int getCalendar_Day_Week_Index_Value(){
        for(int i=0;i<7;i++){
            if(this.getValue().equals(day_Values[i]))
                switch(i){
                    case 0:
                        return Calendar.SUNDAY;
                    case 1:
                        return Calendar.MONDAY;
                    case 2:
                        return Calendar.TUESDAY;
                    case 3:
                        return Calendar.WEDNESDAY;
                    case 4:
                        return Calendar.THURSDAY;
                    case 5:
                        return Calendar.FRIDAY;
                    case 6:
                        return Calendar.SATURDAY;
                }
        }
        return -1;
    }
    
}
