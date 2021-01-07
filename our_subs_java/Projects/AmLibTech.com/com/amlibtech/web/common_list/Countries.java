/* Countries.java.h */

/* Country abbreviations */
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


public class Countries {

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
    
    public static final int ABBR_MAX_LEN = 2;

    private static final String[][] values = {
	{ "United States",	"US"	},
	{ "Canada",		"CA"	},

	{ "Afghanistan",	"AF"	},
	{ "Albania",		"AL"	},
	{ "Algeria",		"DZ"	},
	{ "American Samoa",	"AS"	},
	{ "Andorra ",		"AD"	},
	{ "Angola",		"AO"	},
	{ "Anguilla",		"AI"	},
	{ "Antarctica",		"AQ"	},
	{ "Antigua and Barbuda",	"AG"	},
	{ "Argentina",		"AR"	},
	{ "Armenia",		"AM"	},
	{ "Aruba",		"AW"	},
	{ "Australia",		"AU"	},
	{ "Austria",		"AT"	},
	{ "Azerbaijan",		"AZ"	},
	{ "Bahamas",		"BS"	},
	{ "Bahrain",		"BH"	},
	{ "Bangladesh",		"BD"	},
	{ "Barbados",		"BB"	},
	{ "Belarus",		"BY"	},
	{ "Belgium",		"BE"	},
	{ "Belize",		"BZ"	},
	{ "Benin",		"BJ"	},
	{ "Bermuda",		"BM"	},
	{ "Bhutan",		"BT"	},
	{ "Bolivia",		"BO"	},
	{ "Bosnia and Herzegovina",	"BA"	},
	{ "Botswana",		"BW"	},
	{ "Bouvet Island",	"BV"	},
	{ "Brazil",		"BR"	},
	{ "British Indian Ocean Territory",	"IO"	},
	{ "Brunei Darussalam",	"BN"	},
	{ "Bulgaria",		"BG"	},
	{ "Burkina Faso",	"BF"	},
	{ "Burundi",		"BI"	},
	{ "Cambodia",		"KH"	},
	{ "Cameroon",		"CM"	},
	{ "Cape Verde",		"CV"	},
	{ "Cayman Islands",	"KY"	},
	{ "Central African Republic",	"CF"	},
	{ "Chad",		"TD"	},
	{ "Chile",		"CL"	},
	{ "China",		"CN"	},
	{ "Christmas Island",	"CX"	},
	{ "Cocos (Keeling) Islands",	"CC"	},
	{ "Colombia",		"CO"	},
	{ "Comoros",		"KM"	},
	{ "Congo",		"CG"	},
	{ "Cook Islands",	"CK"	},
	{ "Costa Rica",		"CR"	},
	{ "Cote D'Ivoire (Ivory Coast)",	"CI"	},
	{ "Croatia (Hrvatska)",	"HR"	},
	{ "Cuba",		"CU"	},
	{ "Cyprus",		"CY"	},
	{ "Czech Republic",	"CZ"	},
	{ "Czechoslovakia (former)",	"CS"	},
	{ "Denmark",		"DK"	},
	{ "Djibouti",		"DJ"	},
	{ "Dominica",		"DM"	},
	{ "Dominican Republic",	"DO"	},
	{ "East Timor",		"TP"	},
	{ "Ecuador",		"EC"	},
	{ "Egypt",		"EG"	},
	{ "El Salvador",	"SV"	},
	{ "Equatorial Guinea",	"GQ"	},
	{ "Eritrea",		"ER"	},
	{ "Estonia",		"EE"	},
	{ "Ethiopia",		"ET"	},
	{ "Falkland Islands (Malvinas)",	"FK"	},
	{ "Faroe Islands",	"FO"	},
	{ "Fiji",		"FJ"	},
	{ "Finland",		"FI"	},
	{ "France - Metropolitan",	"FX"	},
	{ "France",		"FR"	},
	{ "French Guiana",	"GF"	},
	{ "French Polynesia",	"PF"	},
	{ "French Southern Territories",	"TF"	},
	{ "Gabon",		"GA"	},
	{ "Gambia",		"GM"	},
	{ "Georgia",		"GE"	},
	{ "Germany",		"DE"	},
	{ "Ghana",		"GH"	},
	{ "Gibraltar",		"GI"	},
	{ "Great Britain (UK)",	"GB"	},
	{ "Greece",		"GR"	},
	{ "Greenland",		"GL"	},
	{ "Grenada",		"GD"	},
	{ "Guadeloupe",		"GP"	},
	{ "Guam",		"GU"	},
	{ "Guatemala",		"GT"	},
	{ "Guinea",		"GN"	},
	{ "Guinea-Bissau",	"GW"	},
	{ "Guyana",		"GY"	},
	{ "Haiti",		"HT"	},
	{ "Heard and McDonald Islands",	"HM"	},
	{ "Honduras",		"HN"	},
	{ "Hong Kong",		"HK"	},
	{ "Hungary",		"HU"	},
	{ "Iceland",		"IS"	},
	{ "India",		"IN"	},
	{ "Indonesia",		"ID"	},
	{ "Iran",		"IR"	},
	{ "Iraq",		"IQ"	},
	{ "Ireland",		"IE"	},
	{ "Israel",		"IL"	},
	{ "Italy",		"IT"	},
	{ "Jamaica",		"JM"	},
	{ "Japan",		"JP"	},
	{ "Jordan",		"JO"	},
	{ "Kazakhstan",		"KZ"	},
	{ "Kenya",		"KE"	},
	{ "Kiribati",		"KI"	},
	{ "Korea (North)",	"KP"	},
	{ "Korea (South)",	"KR"	},
	{ "Kuwait",		"KW"	},
	{ "Kyrgyzstan",		"KG"	},
	{ "Laos",		"LA"	},
	{ "Latvia",		"LV"	},
	{ "Lebanon",		"LB"	},
	{ "Lesotho",		"LS"	},
	{ "Liberia",		"LR"	},
	{ "Libya",		"LY"	},
	{ "Liechtenstein",	"LI"	},
	{ "Lithuania",		"LT"	},
	{ "Luxembourg",		"LU"	},
	{ "Macau",		"MO"	},
	{ "Macedonia",		"MK"	},
	{ "Madagascar",		"MG"	},
	{ "Malawi",		"MW"	},
	{ "Malaysia",		"MY"	},
	{ "Maldives",		"MV"	},
	{ "Mali",		"ML"	},
	{ "Malta",		"MT"	},
	{ "Marshall Islands",	"MH"	},
	{ "Martinique",		"MQ"	},
	{ "Mauritania",		"MR"	},
	{ "Mauritius",		"MU"	},
	{ "Mayotte",		"YT"	},
	{ "Mexico",		"MX"	},
	{ "Micronesia",		"FM"	},
	{ "Moldova",		"MD"	},
	{ "Monaco",		"MC"	},
	{ "Mongolia",		"MN"	},
	{ "Montserrat",		"MS"	},
	{ "Morocco",		"MA"	},
	{ "Mozambique",		"MZ"	},
	{ "Myanmar",		"MM"	},
	{ "Namibia",		"NA"	},
	{ "Nauru",		"NR"	},
	{ "Nepal",		"NP"	},
	{ "Netherlands Antilles",	"AN"	},
	{ "Netherlands",	"NL"	},
	{ "Neutral Zone",	"NT"	},
	{ "New Caledonia",	"NC"	},
	{ "New Zealand (Aotearoa)",	"NZ"	},
	{ "Nicaragua",		"NI"	},
	{ "Niger",		"NE"	},
	{ "Nigeria",		"NG"	},
	{ "Niue",		"NU"	},
	{ "Norfolk Island",	"NF"	},
	{ "Northern Mariana Islands",	"MP"	},
	{ "Norway",		"NO"	},
	{ "Oman",		"OM"	},
	{ "Pakistan",		"PK"	},
	{ "Palau",		"PW"	},
	{ "Panama",		"PA"	},
	{ "Papua New Guinea",	"PG"	},
	{ "Paraguay",		"PY"	},
	{ "Peru",		"PE"	},
	{ "Philippines",	"PH"	},
	{ "Pitcairn",		"PN"	},
	{ "Poland",		"PL"	},
	{ "Portugal",		"PT"	},
	{ "Puerto Rico",	"PR"	},
	{ "Qatar",		"QA"	},
	{ "Reunion",		"RE"	},
	{ "Romania",		"RO"	},
	{ "Russian Federation",	"RU"	},
	{ "Rwanda",		"RW"	},
	{ "S. Georgia and S. Sandwich Isls.",	"GS"	},
	{ "Saint Kitts and Nevis",	"KN"	},
	{ "Saint Lucia",	"LC"	},
	{ "Saint Vincent and the Grenadines",	"VC"	},
	{ "Samoa",		"WS"	},
	{ "San Marino",		"SM"	},
	{ "Sao Tome and Principe",	"ST"	},
	{ "Saudi Arabia",	"SA"	},
	{ "Senegal",		"SN"	},
	{ "Seychelles",		"SC"	},
	{ "Sierra Leone",	"SL"	},
	{ "Singapore",		"SG"	},
	{ "Slovak Republic",	"SK"	},
	{ "Slovenia",		"SI"	},
	{ "Solomon Islands",	"Sb"	},
	{ "Somalia",		"SO"	},
	{ "South Africa",	"ZA"	},
	{ "Spain",		"ES"	},
	{ "Sri Lanka",		"LK"	},
	{ "St. Helena",		"SH"	},
	{ "St. Pierre and Miquelon",	"PM"	},
	{ "Sudan",		"SD"	},
	{ "Suriname",		"SR"	},
	{ "Svalbard and Jan Mayen Islands",	"SJ"	},
	{ "Swaziland",		"SZ"	},
	{ "Sweden",		"SE"	},
	{ "Switzerland",	"CH"	},
	{ "Syria",		"SY"	},
	{ "Taiwan",		"TW"	},
	{ "Tajikistan",		"TJ"	},
	{ "Tanzania",		"TZ"	},
	{ "Thailand",		"TH"	},
	{ "Togo",		"TG"	},
	{ "Tokelau",		"TK"	},
	{ "Tonga",		"TO"	},
	{ "Trinidad and Tobago",	"TT"	},
	{ "Tunisia",		"TN"	},
	{ "Turkey",		"TR"	},
	{ "Turkmenistan",	"TM"	},
	{ "Turks and Caicos Islands",	"TC"	},
	{ "Tuvalu",		"TV"	},
	{ "US Minor Outlying Islands",	"UM"	},
	{ "USSR (former)",	"SU"	},
	{ "Uganda",		"UG"	},
	{ "Ukraine",		"UA"	},
	{ "United Arab Emirates",	"AE"	},
	{ "United Kingdom",	"GB"	},
	{ "Uruguay",		"UY"	},
	{ "Uzbekistan",		"UZ"	},
	{ "Vanuatu",		"VU"	},
	{ "Vatican City State (Holy See)",	"VA"	},
	{ "Venezuela",		"VE"	},
	{ "Viet Nam",		"VN"	},
	{ "Virgin Islands (British)",	"VG"	},
	{ "Virgin Islands (U.S.)",	"VI"	},
	{ "Wallis and Futuna Islands",	"WF"	},
	{ "Western Sahara",	"EH"	},
	{ "Yemen",		"YE"	},
	{ "Yugoslavia",		"YU"	},
	{ "Zaire",		"ZR"	},
	{ "Zambia",		"ZM"	},
	{ "Zimbabwe",		"ZW"	},

	{ "Other",		".."		}
    };
 

    public static String[] country_Names_List = new String[Countries.values.length];
    public static String[] country_Abbr_List = new String[Countries.values.length];
    
    private static Countries countries_Instance= new Countries();
    
    Countries() {
        for(int i=0; i< values.length; i++){
            country_Names_List[i] = values[i][0];
            country_Abbr_List[i] = values[i][1];
        }
    }
    
    public static String getValid_Country_Abbr(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, String field_Value)
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
        
        String country_Abbr_Value;
        
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
            for(int i=0; i< country_Names_List.length; i++){
                if(parameter_Value.equalsIgnoreCase(country_Names_List[i])){
                    country_Abbr_Value = country_Abbr_List[i];
                    return country_Abbr_Value;
                }
            }
            String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not a valid Country.";
            throw new Field_Validation_Exception(message);
            
        }
        
        for(int i=0; i< country_Abbr_List.length; i++){
            if(parameter_Value.equalsIgnoreCase(country_Abbr_List[i])){
                country_Abbr_Value = country_Abbr_List[i];
                return country_Abbr_Value;
            }
        }
        
        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not a valid Country.";
        
        throw new Field_Validation_Exception(message);
        
        
    }
    
    public static String get_Country_Name(String abbr){
        for(int i=0; i< country_Abbr_List.length; i++){
            if(abbr.equalsIgnoreCase(country_Abbr_List[i])){
                return country_Names_List[i];
            }
        }
        return "";
    }
    
}
