/* PayPal_Countries.java.h */

/* PayPal Country abbreviations */
/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 2002, 2003, 2004, 2005, 2006, 2007, 2008       |
|       Employees of American Liberator Technologies                           |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       Employees of American Liberator Technologies                           |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
 */

package com.amlibtech.web.common_list;

import javax.servlet.http.*;
import com.amlibtech.web.data.*;


public class PayPal_Countries {

    private static final String[] Copyright= {
    "|       Copyright (c) 1985 thru 2002, 2003, 2004, 2005, 2006, 2007, 2008       |",
    "|       Employees of American Liberator Technologies                           |",
    "|       All Rights Reserved                                                    |",
    "|                                                                              |",
    "|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |",
    "|       Employees of American Liberator Technologies                           |",
    "|       The copyright notice above does not evidence any                       |",
    "|       actual or intended publication of such source code.                    |"
    };

    private static String[][] values = {
	{ "United States",	"US" },
	{ "Canada",	"CA" },

	{ "Anguilla",	"AI" },
	{ "Argentina",	"AR" },
	{ "Australia",	"AU" },
	{ "Austria",	"AT" },
	{ "Belgium",	"BE" },
	{ "Brazil",	"BR" },
	{ "Chile",	"CL" },
	{ "China",	"CN" },
	{ "Costa Rica",	"CR" },
	{ "Denmark",	"DK" },
	{ "Dominican Republic",	"DO" },
	{ "Ecuador",	"EC" },
	{ "Finland",	"FI" },
	{ "France",	"FR" },
	{ "Germany",	"DE" },
	{ "Greece",	"GR" },
	{ "Hong Kong",	"HK" },
	{ "Iceland",	"IS" },
	{ "India",	"IN" },
	{ "Ireland",	"IE" },
	{ "Israel",	"IL" },
	{ "Italy",	"IT" },
	{ "Jamaica",	"JM" },
	{ "Japan",	"JP" },
	{ "Luxembourg",	"LU" },
	{ "Malaysia",	"MY" },
	{ "Mexico",	"MX" },
	{ "Monaco",	"MC" },
	{ "Netherlands",	"NL" },
	{ "New Zealand",	"NZ" },
	{ "Norway",	"NO" },
	{ "Portugal",	"PT" },
	{ "Singapore",	"SG" },
	{ "South Korea",	"KR" },
	{ "Spain",	"ES" },
	{ "Sweden",	"SE" },
	{ "Switzerland",	"CH" },
	{ "Thailand",	"TH" },
	{ "Taiwan",	"TW" },
	{ "Turkey",	"TR" },
	{ "United Kingdom",	"GB" },
	{ "Uruguay",	"UY" },
	{ "Venezuela",	"VE" }
    };

    public static String[] payPal_Country_Names_List = new String[PayPal_Countries.values.length];
    public static String[] payPal_Country_Abbr_List = new String[PayPal_Countries.values.length];

    private static PayPal_Countries payPal_Countries_Instance= new PayPal_Countries();

    PayPal_Countries() {
        for(int i=0; i< values.length; i++){
            payPal_Country_Names_List[i] = values[i][0];
            payPal_Country_Abbr_List[i] = values[i][1];
        }
    }

    public static String getValid_PayPal_Country_Abbr(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, String field_Value)
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

        String payPal_Country_Abbr_Value;

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
            for(int i=0; i< payPal_Country_Names_List.length; i++){
                if(parameter_Value.equalsIgnoreCase(payPal_Country_Names_List[i])){
                    payPal_Country_Abbr_Value = payPal_Country_Abbr_List[i];
                    return payPal_Country_Abbr_Value;
                }
            }
            String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not a valid PayPal Country.";
            throw new Field_Validation_Exception(message);

        }

        for(int i=0; i< payPal_Country_Abbr_List.length; i++){
            if(parameter_Value.equalsIgnoreCase(payPal_Country_Abbr_List[i])){
                payPal_Country_Abbr_Value = payPal_Country_Abbr_List[i];
                return payPal_Country_Abbr_Value;
            }
        }

        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not a valid PayPal Country.";

        throw new Field_Validation_Exception(message);


    }

    public static String get_PayPal_Country_Name(String abbr){
        for(int i=0; i< payPal_Country_Abbr_List.length; i++){
            if(abbr.equalsIgnoreCase(payPal_Country_Abbr_List[i])){
                return payPal_Country_Names_List[i];
            }
        }
        return "";
    }

    public static boolean is_Valid(String abbr){
        for(int i=0; i< payPal_Country_Abbr_List.length; i++){
            if(abbr.equalsIgnoreCase(payPal_Country_Abbr_List[i])){
                return true;
            }
        }
        return false;
    }

}
