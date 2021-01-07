/* * DFSchool_Grade.java
 *
 * Created on August 4, 2006, 1:09 PM
 */

package com.amlibtech.data_fields;

/**
 *
 * @author  dgleeson
 */
public class DFSchool_Grade  extends DFString_Enumerated {
    
    public static final int SCHOOL_GRADE_NONE=0;
    public static final int SCHOOL_GRADE_PRE_K=1;
    public static final int SCHOOL_GRADE_K=2;
    public static final int SCHOOL_GRADE_1ST=3;
    public static final int SCHOOL_GRADE_2ND=4;
    public static final int SCHOOL_GRADE_3RD=5;
    public static final int SCHOOL_GRADE_4TH=6;
    public static final int SCHOOL_GRADE_5TH=7;
    public static final int SCHOOL_GRADE_6TH=8;
    public static final int SCHOOL_GRADE_7TH=9;
    public static final int SCHOOL_GRADE_8TH=10;
    public static final int SCHOOL_GRADE_9TH=11;
    public static final int SCHOOL_GRADE_10TH=12;
    public static final int SCHOOL_GRADE_11TH=13;
    public static final int SCHOOL_GRADE_12TH=14;
    public static final int SCHOOL_GRADE_13THPLUS=15;

    
    public static final String[] SCHOOL_GRADE_VALUES = { "None", "Pre-K", "K", "1st", 
    "2nd", "3rd", "4th", "5th", "6th", "7th", "8th",
    "9th", "10th", "11th", "12th", "13th+" };

    
    /** Creates a new instance of DFSchool_Grade */
    public DFSchool_Grade(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title, SCHOOL_GRADE_VALUES);
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
    
    
    public int get_Int_Value(){
        int i;
        for(i=0; i<SCHOOL_GRADE_VALUES.length;i++){
            if(this.getValue().equals(SCHOOL_GRADE_VALUES[i]))
                return i;
        }
        return -1;
    }
    
    public int compare(DFSchool_Grade dfSchool_Grade){
        int i_Am = this.get_Int_Value();
        int you_Are = dfSchool_Grade.get_Int_Value();
        return i_Am - you_Are;
    }

}
