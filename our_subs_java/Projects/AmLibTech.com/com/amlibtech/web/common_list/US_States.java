/* US_States.java.h */

/* State abbreviations */
/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004       |
|       American Liberator Technologies                                        |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       American Liberator Technologies                                        |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
 */

package com.amlibtech.web.common_list;

import javax.servlet.http.*;
import com.amlibtech.web.data.*;


public class US_States {

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

    public static final int ABBR_MAX_LEN = 2;
    
    public static final String[][] values = {
        {"AL",	"Alabama",		"1"},
        {"AK",	"Alaska",		"2"},
        {"AZ",	"Arizona",		"3"},
        {"AR",	"Arkansas",		"4"},
        {"CA",	"California",		"5"},
        {"CZ",	"Canal Zone",		"0"},
        {"CO",	"Colorado",		"6"},
        {"CT",	"Connecticut",		"7"},
        {"DE",	"Delaware",		"8"},
        {"DC",	"District of Columbia",	"9"},
        {"FL",	"Florida",		"10"},
        {"GA",	"Georgia",		"11"},
        {"GU",	"Guam",			"0"},
        {"HI",	"Hawaii",		"12"},
        {"ID",	"Idaho",		"13"},
        {"IL",	"Illinois",		"14"},
        {"IN",	"Indiana",		"15"},
        {"IA",	"Iowa",			"16"},
        {"KS",	"Kansas",		"17"},
        {"KY",	"Kentucky",		"18"},
        {"LA",	"Louisiana",		"19"},
        {"ME",	"Maine",		"20"},
        {"MD",	"Maryland",		"21"},
        {"MA",	"Massachusetts",	"22"},
        {"MI",	"Michigan",		"23"},
        {"MN",	"Minnesota",		"24"},
        {"MS",	"Mississippi",		"25"},
        {"MO",	"Missouri",		"26"},
        {"MT",	"Montana",		"27"},
        {"NE",	"Nebraska",		"28"},
        {"NV",	"Nevada",		"29"},
        {"NH",	"New Hampshire",	"30"},
        {"NJ",	"New Jersey",		"31"},
        {"NM",	"New Mexico",		"32"},
        {"NY",	"New York",		"33"},
        {"NC",	"North Carolina",	"34"},
        {"ND",	"North Dakota",		"35"},
        {"OH",	"Ohio",			"36"},
        {"OK",	"Oklahoma",		"37"},
        {"OR",	"Oregon",		"38"},
        {"PA",	"Pennsylvania",		"39"},
        {"PR",	"Puerto Rico",		"0"},
        {"RI",	"Rhode Island",		"40"},
        {"SC",	"South Carolina",	"41"},
        {"SD",	"South Dakota",		"42"},
        {"TN",	"Tennesse",		"43"},
        {"TX",	"Texas",		"44"},
        {"UT",	"Utah",			"45"},
        {"VT",	"Vermont",		"46"},
        {"VA",	"Virginia",		"47"},
        {"VI",	"Virgin Islands",	"0"},
        {"WA",	"Washington",		"48"},
        {"WV",	"West Virginia",	"49"},
        {"WI",	"Wisconsin",		"50"},
        {"WY",	"Wyoming",		"51"},
        {"AA",	"Armed Forces America",	"0"},
        {"AE",	"ArmedForces AfEuMECa",	"0"},
        {"AP",	"Armed Forces Pacific",	"0"},
        {"__",	"NOT USA",		"52"},
        
        {"AB",	"Alberta",		"0"},
        {"BC",	"British Columbia",	"0"},
        {"MB",	"Manitoba",		"0"},
        {"NF",	"Newfoundland",		"0"},
        {"NB",	"New Brunswick",	"0"},
        {"NT",	"Northwest Territory",	"0"},
        {"NS",	"Nova Scotia",		"0"},
        {"NU",	"Nunavut",		"0"},
        {"ON",	"Ontario",		"0"},
        {"PE",	"Prince Edward Island",	"0"},
        {"QC",	"Quebec",		"0"},
        {"SK",	"Saskatchewan",		"0"},
        {"YT",	"Yukon",		"0"}
    };
    
    public static String[] state_Names_List = new String[US_States.values.length];
    public static String[] state_Abbr_List = new String[US_States.values.length];
    
    private static US_States us_States_Instance= new US_States();
    
    US_States() {
        for(int i=0; i< values.length; i++){
            state_Abbr_List[i] = values[i][0];
            state_Names_List[i] = values[i][1];
        }
    }
    
    public static String getValid_State_Abbr(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, String field_Value)
    throws Field_Validation_Exception {
        
        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);
            
        }
        
        if(field_Description_Node.getType() != "String"){
            String message ="Field_Description_Node is not of Type String for field :" + field_Description_Node.getField_Title() + ":.";
            throw new Field_Validation_Exception(message);
        }
        
        String state_Abbr_Value;
        
        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = field_Value;
        }
        
        if(parameter_Value==null)
            parameter_Value="";
        
        
        int length = parameter_Value.length();
        
        if(length!=2){
            // convert name to abbr
            for(int i=0; i< state_Names_List.length; i++){
                if(parameter_Value.equalsIgnoreCase(state_Names_List[i])){
                    state_Abbr_Value = state_Abbr_List[i];
                    return state_Abbr_Value;
                }
            }
            String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not a valid State.";
            throw new Field_Validation_Exception(message);
            
        }
        
        for(int i=0; i< state_Abbr_List.length; i++){
            if(parameter_Value.equalsIgnoreCase(state_Abbr_List[i])){
                state_Abbr_Value = state_Abbr_List[i];
                return state_Abbr_Value;
            }
        }
        
        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not a valid State.";
        
        throw new Field_Validation_Exception(message);
        
        
    }
    
    public static String get_State_Name(String abbr){
        for(int i=0; i< state_Abbr_List.length; i++){
            if(abbr.equalsIgnoreCase(state_Abbr_List[i])){
                return state_Names_List[i];
            }
        }
        return "";
    }
    
}
