/*
 * Field_Validator.java
 *
 * Created on October 29, 2004, 2:24 PM
 */

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

package com.amlibtech.web.data;

import javax.servlet.http.*;
import java.util.*;
import java.text.*;
import java.math.BigDecimal;


import com.amlibtech.util.*;

/**
 *
 * @author  dgleeson
 */
public class Field_Validator {

    /** Creates a new instance of Field_Validator */
    public Field_Validator() {
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



    public static String getString(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, String field_Value)
    throws Field_Validation_Exception {
        Integer min_Length;
        Integer max_Length;
        int length;


        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);

        }

        if(field_Description_Node.getType() != "String"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not String for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }


        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = field_Value;
        }

        if(parameter_Value==null)
            parameter_Value="";


        length = parameter_Value.length();


        if((min_Length = field_Description_Node.getMin_Length()) != null){
            if(length < min_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " is required.";
                throw new Field_Validation_Exception(message);

            }
        }


        if((max_Length = field_Description_Node.getMax_Length()) != null){
            if(length > max_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " Maximum Length is :" + max_Length.toString() + ".";
                throw new Field_Validation_Exception(message);

            }
        }

        return parameter_Value;
    }


    public static String getFilled_String(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, String field_Value)
    throws Field_Validation_Exception {
        Integer min_Length;
        Integer max_Length;
        int length;


        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);
        }

        if(field_Description_Node.getType() != "Filled_String"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Filled_String for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }


        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = field_Value;
        }

        if(parameter_Value==null)
            parameter_Value="";


        length = parameter_Value.length();


        if((min_Length = field_Description_Node.getMin_Length()) != null){
            if(length < min_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " is required.";
                throw new Field_Validation_Exception(message);

            }
        }


        if((max_Length = field_Description_Node.getMax_Length()) != null){
            if(length > max_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " Maximum Length is :" + max_Length.toString() + ".";
                throw new Field_Validation_Exception(message);

            }
        }

        switch(field_Description_Node.getFill_Type()){
            case Field_Description_Node.FILL_TYPE_NONE:
                break;

            case Field_Description_Node.FILL_TYPE_LEFT:
                parameter_Value = StringPlus.left_Fill(parameter_Value, max_Length.intValue(), field_Description_Node.getFill_Char() );
                break;

            case Field_Description_Node.FILL_TYPE_RIGHT:
                parameter_Value = StringPlus.right_Fill(parameter_Value, max_Length.intValue(), field_Description_Node.getFill_Char() );

                break;
            case Field_Description_Node.FILL_TYPE_CENTER:
                parameter_Value = StringPlus.center_Fill(parameter_Value, max_Length.intValue(), field_Description_Node.getFill_Char() );
                break;
            case Field_Description_Node.FILL_TYPE_TRIM:
                parameter_Value.trim();
                break;
            default:
                String message = field_Description_Node.getField_Title() + " has unknown Fill type :" + field_Description_Node.getFill_Type() + ".";
                throw new Field_Validation_Exception(message);
        }



        return parameter_Value;
    }

    public static String getKey_String(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, String field_Value)
    throws Field_Validation_Exception {
        Integer min_Length;
        Integer max_Length;
        int length;


        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);

        }

        if(field_Description_Node.getType() != "Key_String"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Key_String for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }


        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = field_Value;
        }

        if(parameter_Value==null)
            parameter_Value="";


        length = parameter_Value.length();


        if((min_Length = field_Description_Node.getMin_Length()) != null){
            if(length < min_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " is required.";
                throw new Field_Validation_Exception(message);

            }
        }


        if((max_Length = field_Description_Node.getMax_Length()) != null){
            if(length > max_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " Maximum Length is :" + max_Length.toString() + ".";
                throw new Field_Validation_Exception(message);

            }
        }

        if(field_Description_Node.getFill_Type() != Field_Description_Node.FILL_TYPE_KFILL){
                String message = field_Description_Node.getField_Title() + " has wrong fill type.";
                throw new Field_Validation_Exception(message);
        }

        try {
            parameter_Value = StringPlus.key_Fill(parameter_Value, field_Description_Node.getFill_Pattern());
        }
        catch(StringPlus_Exception spe){
            String message = field_Description_Node.getField_Title() + " StringPlus_Exception: "  + spe.getMessage();
            throw new Field_Validation_Exception(message);
        }
        return parameter_Value;
    }




    public static String getStringTruncate(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, String field_Value)
    throws Field_Validation_Exception {
        Integer min_Length;
        Integer max_Length;
        int length;


        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);

        }

        if(field_Description_Node.getType() != "String"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not String for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }


        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = field_Value;
        }

        if(parameter_Value==null)
            parameter_Value="";


        length = parameter_Value.length();


        if((min_Length = field_Description_Node.getMin_Length()) != null){
            if(length < min_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " is required.";
                throw new Field_Validation_Exception(message);

            }
        }


        if((max_Length = field_Description_Node.getMax_Length()) != null){
            if(length > max_Length.intValue()){
                parameter_Value = parameter_Value.substring(0, max_Length.intValue() -1);
            }
        }

        return parameter_Value;
    }






    public static Boolean getBoolean(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, Boolean field_Value)
    throws Field_Validation_Exception {

        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);

        }

        if(field_Description_Node.getType() != "Boolean"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Boolean for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }

        Boolean b_Value;

        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed
            b_Value = new Boolean(false);

        }else{
            parameter_Value = parameter_Value.trim();
            if(parameter_Value.equalsIgnoreCase("true") ||
            parameter_Value.equalsIgnoreCase("yes") ||
            parameter_Value.equalsIgnoreCase("+") ||
            parameter_Value.equalsIgnoreCase("y") ||
            parameter_Value.equalsIgnoreCase("1") ){
                b_Value = new Boolean(true);
            }else if(parameter_Value.equalsIgnoreCase("false") ||
            parameter_Value.equalsIgnoreCase("no") ||
            parameter_Value.equalsIgnoreCase("-") ||
            parameter_Value.equalsIgnoreCase("n") ||
            parameter_Value.equalsIgnoreCase("0") ){
                b_Value = new Boolean(false);
            }else{
                String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not a valid boolean.";
                throw new Field_Validation_Exception(message);
            }

        }

        return b_Value;
    }

    public static java.sql.Date getSQL_Date(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, java.sql.Date field_Value)
    throws Field_Validation_Exception {

        int []     days = {
            0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31                               };


            Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
            if(field_Description_Node==null){
                String message ="No Field_Description_Node for field :" + field_Name + ":";
                throw new Field_Validation_Exception(message);

            }

            if(field_Description_Node.getType() != "Sql_Date"){
		    String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Sql_Date for field :" + field_Description_Node.getField_Title() + ":";
                throw new Field_Validation_Exception(message);
            }

            java.sql.Date d_Value;

            String parameter_Value = request.getParameter(field_Name);
            if(parameter_Value == null){
                // no parameter passed, use default.
                d_Value = field_Value;
            }else{
                parameter_Value = parameter_Value.trim();
                char    c;
                for(int i=0;i<parameter_Value.length();i++){
                    c=parameter_Value.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&  c != '/'){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (digits and / only)";
                        throw new Field_Validation_Exception(message);
                    }
                }

                int month, day, year;

                if(parameter_Value.matches("[0-9]+/[0-9]+/[0-9]+")){
                    // m/d/y
                    String[] pieces_Of_Date = parameter_Value.split("/");
                    if(pieces_Of_Date.length!=3){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not separate M, D, Y)";
                        throw new Field_Validation_Exception(message);
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=Integer.parseInt(pieces_Of_Date[1]);
                        year=Integer.parseInt(pieces_Of_Date[2]);
                    }
                }else if(parameter_Value.matches("[0-9]+/[0-9]+")){
                    // m/y
                    String[] pieces_Of_Date = parameter_Value.split("/");
                    if(pieces_Of_Date.length!=2){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not separate M, Y)";
                        throw new Field_Validation_Exception(message);
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=1;
                        year=Integer.parseInt(pieces_Of_Date[1]);
                    }
                }else {
                    // error
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not determine date pattern m/d/y or m/y)";
                    throw new Field_Validation_Exception(message);
                }


                if (month < 1 || month > 12){
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Month must be in 1 thru 12)";
                    throw new Field_Validation_Exception(message);
                }

                if (day < 1 || day > days[month]){
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Day must be in 1 thru " + days[month] + ")";
                    throw new Field_Validation_Exception(message);
                }

                if (year < 0 || year > 9999){
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Year must be in 1 thru 9999)";
                    throw new Field_Validation_Exception(message);
                }

		TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);

                Calendar calendar = Calendar.getInstance(tz, Locale.US);

                calendar.set(year, month-1, day, 0, 0, 0);

                Date date =calendar.getTime();
                d_Value =new java.sql.Date(date.getTime());

            }
            return d_Value;
    }


    public static java.util.Date getDate(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, java.util.Date field_Value)
    throws Field_Validation_Exception {

        int []     days = {
            0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31                               };

            Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
            if(field_Description_Node==null){
                String message ="No Field_Description_Node for field :" + field_Name + ":";
                throw new Field_Validation_Exception(message);

            }

            if(field_Description_Node.getType() != "Date"){
		    String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Date for field :" + field_Description_Node.getField_Title() + ":";
                throw new Field_Validation_Exception(message);
            }

            java.util.Date d_Value;

            String parameter_Value = request.getParameter(field_Name);
            if(parameter_Value == null){
                // no parameter passed, use default.
                d_Value = field_Value;
            }else{
                parameter_Value = parameter_Value.trim();
                char    c;
                for(int i=0;i<parameter_Value.length();i++){
                    c=parameter_Value.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&  c != '/'){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (digits and / only)";
                        throw new Field_Validation_Exception(message);
                    }
                }

                int month, day, year;

                if(parameter_Value.matches("[0-9]+/[0-9]+/[0-9]+")){
                    // m/d/y
                    String[] pieces_Of_Date = parameter_Value.split("/");
                    if(pieces_Of_Date.length!=3){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not separate M, D, Y)";
                        throw new Field_Validation_Exception(message);
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=Integer.parseInt(pieces_Of_Date[1]);
                        year=Integer.parseInt(pieces_Of_Date[2]);
                    }
                }else if(parameter_Value.matches("[0-9]+/[0-9]+")){
                    // m/y
                    String[] pieces_Of_Date = parameter_Value.split("/");
                    if(pieces_Of_Date.length!=2){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not separate M, Y)";
                        throw new Field_Validation_Exception(message);
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=1;
                        year=Integer.parseInt(pieces_Of_Date[1]);
                    }
                }else {
                    // error
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not determine date pattern m/d/y or m/y)";
                    throw new Field_Validation_Exception(message);
                }


                if (month < 1 || month > 12){
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Month must be in 1 thru 12)";
                    throw new Field_Validation_Exception(message);
                }

                if (day < 1 || day > days[month]){
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Day must be in 1 thru " + days[month] + ")";
                    throw new Field_Validation_Exception(message);
                }

                if (year < 0 || year > 9999){
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Year must be in 1 thru 9999)";
                    throw new Field_Validation_Exception(message);
                }

		TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);

                Calendar calendar = Calendar.getInstance(tz, Locale.US);

                calendar.set(year, month-1, day, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND,0);
                d_Value =calendar.getTime();
            }
            return d_Value;
    }


    public static Integer getInteger(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, Integer field_Value)
    throws Field_Validation_Exception {



        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);

        }

        if(field_Description_Node.getType() != "Integer"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Integer for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }

        Integer  i_Value;

        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            i_Value = field_Value;
        }else{
            parameter_Value = parameter_Value.trim();

            if(parameter_Value.length()!=0){
                char    c;
                for(int i=0;i<parameter_Value.length();i++){
                    c=parameter_Value.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&   c != '-'){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (digits and - only)";
                        throw new Field_Validation_Exception(message);
                    }
                }



                if(!parameter_Value.matches("[-]*[0-9]+")){
                    // error
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not determine integer pattern)";
                    throw new Field_Validation_Exception(message);
                }

                try{
                    i_Value = new Integer(parameter_Value);
                }
                catch(NumberFormatException nfe){
                    // error
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. " + nfe.getMessage();
                    throw new Field_Validation_Exception(message);
                }

                Integer  integer_Min_Value;

                if((integer_Min_Value = field_Description_Node.getInteger_Min_Value()) != null){
                    if(i_Value.compareTo(integer_Min_Value) < 0){
                        String message = field_Description_Node.getField_Title() + " Minumum Value is " + integer_Min_Value + ".";
                        throw new Field_Validation_Exception(message);

                    }
                }

                Integer  integer_Max_Value;


                if((integer_Max_Value = field_Description_Node.getInteger_Max_Value()) != null){
                    if(i_Value.compareTo(integer_Max_Value) > 0){
                        String message = field_Description_Node.getField_Title() + " Maximum Value is :" + integer_Max_Value + ".";
                        throw new Field_Validation_Exception(message);

                    }
                }


            }else{
                String message = field_Description_Node.getField_Title() + " Can not be left blank. A number is required.";
                throw new Field_Validation_Exception(message);
            }
        }

        return i_Value;
    }


    public static String getTimeZone_String(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, String field_Value)
    throws Field_Validation_Exception {
        Integer min_Length;
        Integer max_Length;
        int length;


        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);

        }

        if(field_Description_Node.getType() != "TimeZone_String"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not TimeZone_String for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }


        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = field_Value;
        }

        if(parameter_Value==null)
            parameter_Value="";

        parameter_Value = parameter_Value.trim();

        length = parameter_Value.length();


        if((min_Length = field_Description_Node.getMin_Length()) != null){
            if(length < min_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " is required.";
                throw new Field_Validation_Exception(message);

            }
        }


        if((max_Length = field_Description_Node.getMax_Length()) != null){
            if(length > max_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " Maximum Length is :" + max_Length.toString() + ".";
                throw new Field_Validation_Exception(message);

            }
        }

        boolean found=false;
        String[] availableIDs = TimeZone.getAvailableIDs();
        for(int i=0; i< availableIDs.length; i++){
            if(parameter_Value.equals(availableIDs[i])){
                found=true;
                i=availableIDs.length;
            }
        }
        if(!found){
            String message = field_Description_Node.getField_Title() + " is not a known TimeZone ID.";
            throw new Field_Validation_Exception(message);
        }


        return parameter_Value;
    }


    public static Integer getColor(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, Integer field_Value)
    throws Field_Validation_Exception {



        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);

        }

        if(field_Description_Node.getType() != "Color"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Color for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }

        Integer  i_Value;

        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            i_Value = field_Value;
        }else{
            parameter_Value = parameter_Value.trim();

            if(parameter_Value.length()!=0){
                char    c;
                c=parameter_Value.charAt(0);
                if(c!= '#'){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. Colors must start with #.";
                        throw new Field_Validation_Exception(message);
                }

                for(int i=1;i<parameter_Value.length();i++){
                    c=parameter_Value.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&
                        c != 'A' &&
                        c != 'a' &&
                        c != 'B' &&
                        c != 'b' &&
                        c != 'C' &&
                        c != 'c' &&
                        c != 'D' &&
                        c != 'd' &&
                        c != 'E' &&
                        c != 'e' &&
                        c != 'F' &&
                        c != 'f'

                    ){

                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Hex digits only 0-9,A-F)";
                        throw new Field_Validation_Exception(message);
                    }
                }


                try{
                    i_Value =  Integer.decode(parameter_Value);
                }
                catch(NumberFormatException nfe){
                    // error
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. " + nfe.getMessage();
                    throw new Field_Validation_Exception(message);
                }

                Integer  integer_Min_Value;

                if((integer_Min_Value = field_Description_Node.getInteger_Min_Value()) != null){
                    if(i_Value.compareTo(integer_Min_Value) < 0){
                        String message = field_Description_Node.getField_Title() + " Minumum Value is " + integer_Min_Value + ".";
                        throw new Field_Validation_Exception(message);

                    }
                }

                Integer  integer_Max_Value;


                if((integer_Max_Value = field_Description_Node.getInteger_Max_Value()) != null){
                    if(i_Value.compareTo(integer_Max_Value) > 0){
                        String message = field_Description_Node.getField_Title() + " Maximum Value is :" + integer_Max_Value + ".";
                        throw new Field_Validation_Exception(message);

                    }
                }


            }else{
                String message = field_Description_Node.getField_Title() + " Can not be left blank. A value is required.";
                throw new Field_Validation_Exception(message);
            }
        }

        return i_Value;
    }


    
    public static java.sql.Timestamp getTimestamp(HttpServletRequest request, String passed_Field_Name, Field_Description_List field_Description_List, java.sql.Timestamp field_Value, String timeZone_Name)
    throws Field_Validation_Exception {

        String field_Name_For_Date = passed_Field_Name + "_Date";
        String field_Name_For_Time = passed_Field_Name + "_Time";

        int []     days = {
            0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31                               };


            Field_Description_Node field_Description_Node = field_Description_List.getField_Node(passed_Field_Name);
            if(field_Description_Node==null){
                String message ="No Field_Description_Node for field :" + passed_Field_Name + ":";
                throw new Field_Validation_Exception(message);

            }

            if(field_Description_Node.getType() != "Timestamp"){
		    String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Timestamp for field :" + field_Description_Node.getField_Title() + ":";
                throw new Field_Validation_Exception(message);
            }





            java.sql.Timestamp t_Value;

            String parameter_Value_For_Date = request.getParameter(field_Name_For_Date);
            String parameter_Value_For_Time = request.getParameter(field_Name_For_Time);


            if(parameter_Value_For_Date == null && parameter_Value_For_Time == null){
                // no parameter passed, use default.
                t_Value = field_Value;
            }else{

                if(parameter_Value_For_Date == null){
                    String message ="Field :" + field_Description_Node.getField_Title() + ": can not have empty Date and non-empty Time";
                        throw new Field_Validation_Exception(message);
                }
                parameter_Value_For_Date = parameter_Value_For_Date.trim();
                char    c;
                for(int i=0;i<parameter_Value_For_Date.length();i++){
                    c=parameter_Value_For_Date.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&  c != '/'){
                        String message ="Answer :" + parameter_Value_For_Date + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (digits and / only)";
                        throw new Field_Validation_Exception(message);
                    }
                }

                int month, day, year;

                if(parameter_Value_For_Date.matches("[0-9]+/[0-9]+/[0-9]+")){
                    // m/d/y
                    String[] pieces_Of_Date = parameter_Value_For_Date.split("/");
                    if(pieces_Of_Date.length!=3){
                        String message ="Answer :" + parameter_Value_For_Date + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not separate M, D, Y)";
                        throw new Field_Validation_Exception(message);
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=Integer.parseInt(pieces_Of_Date[1]);
                        year=Integer.parseInt(pieces_Of_Date[2]);
                    }
                }else if(parameter_Value_For_Date.matches("[0-9]+/[0-9]+")){
                    // m/y
                    String[] pieces_Of_Date = parameter_Value_For_Date.split("/");
                    if(pieces_Of_Date.length!=2){
                        String message ="Answer :" + parameter_Value_For_Date + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not separate M, Y)";
                        throw new Field_Validation_Exception(message);
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=1;
                        year=Integer.parseInt(pieces_Of_Date[1]);
                    }
                }else {
                    // error
                    String message ="Answer :" + parameter_Value_For_Date + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not determine date pattern m/d/y or m/y)";
                    throw new Field_Validation_Exception(message);
                }


                if (month < 1 || month > 12){
                    String message ="Answer :" + parameter_Value_For_Date + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Month must be in 1 thru 12)";
                    throw new Field_Validation_Exception(message);
                }

                if (day < 1 || day > days[month]){
                    String message ="Answer :" + parameter_Value_For_Date + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Day must be in 1 thru " + days[month] + ")";
                    throw new Field_Validation_Exception(message);
                }

                if (year < 0 || year > 9999){
                    String message ="Answer :" + parameter_Value_For_Date + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Year must be in 1 thru 9999)";
                    throw new Field_Validation_Exception(message);
                }




                if(parameter_Value_For_Time == null){
                    String message ="Field :" + field_Description_Node.getField_Title() + ": can not have non-empty Date and empty Time";
                        throw new Field_Validation_Exception(message);
                }
                parameter_Value_For_Time = parameter_Value_For_Time.trim();
                for(int i=0;i<parameter_Value_For_Time.length();i++){
                    c=parameter_Value_For_Time.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&
                       c != ':' && c != 'A' && c != 'a' && c != 'P' && c != 'p' && c != 'M' && c != 'm' && c != ' '){
                        String message ="Answer :" + parameter_Value_For_Time + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (digits, :, or AM/PM only)";
                        throw new Field_Validation_Exception(message);
                    }
                }

                int hours, minutes, seconds;
                boolean ante_Meridian;

                if(parameter_Value_For_Time.matches("[0-9]+:[0-9]+:[0-9]+ [aApP][Mm]")){
                    // h:m:s a/pm
                    String[] primary_Pieces_Of_Time = parameter_Value_For_Time.split(" ");
                    if(primary_Pieces_Of_Time.length!=2){
                        String message ="Answer :" + parameter_Value_For_Time + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not separate H, M, S AM/PM). Splits into " + primary_Pieces_Of_Time.length + " pieces.";
                        throw new Field_Validation_Exception(message);
                    }else{
                        if(primary_Pieces_Of_Time[1].equalsIgnoreCase("AM")){
                            ante_Meridian=true;
                        }else if(primary_Pieces_Of_Time[1].equalsIgnoreCase("PM")){
                            ante_Meridian=false;
                        }else{
                            String message ="Answer :" + parameter_Value_For_Time + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not determine AM/PM)";
                            throw new Field_Validation_Exception(message);
                        }
		    }


                    String[] secondary_Pieces_Of_Time = primary_Pieces_Of_Time[0].split(":");
                    if(secondary_Pieces_Of_Time.length!=3){
                        String message ="Answer :" + parameter_Value_For_Time + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not separate H, M, S AM/PM). Splits into " + secondary_Pieces_Of_Time.length + " pieces.";
                        throw new Field_Validation_Exception(message);
                    }else{
                        hours=Integer.parseInt(secondary_Pieces_Of_Time[0]);
                        minutes=Integer.parseInt(secondary_Pieces_Of_Time[1]);
                        seconds=Integer.parseInt(secondary_Pieces_Of_Time[2]);
                    }

                }else {
                    // error
                    String message ="Answer :" + parameter_Value_For_Time + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not determine time pattern h:m:s AM/PM)";
                    throw new Field_Validation_Exception(message);
                }


                if (hours < 1 || hours > 12){
                    String message ="Answer :" + parameter_Value_For_Time + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Hours must be in 1 thru 12)";
                    throw new Field_Validation_Exception(message);
                }

                if (minutes < 0 || minutes > 59){
                    String message ="Answer :" + parameter_Value_For_Time + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Minutes must be in 0 thru 59)";
                    throw new Field_Validation_Exception(message);
                }

                if (seconds < 0 || seconds > 59){
                    String message ="Answer :" + parameter_Value_For_Time + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Seconds must be in 0 thru 59)";
                    throw new Field_Validation_Exception(message);
                }




                int hour_Of_The_Day = hours;
                if(ante_Meridian && hours==12)
                    hour_Of_The_Day = 0;
                if(!ante_Meridian && hours!=12)
                    hour_Of_The_Day += 12;





                TimeZone tz = TimeZone.getTimeZone(timeZone_Name);
                //int rawOffset = tz.getRawOffset();

                Calendar calendar = Calendar.getInstance(tz, Locale.US);

                calendar.clear(Calendar.MILLISECOND);

                calendar.set(year, month-1, day, hour_Of_The_Day, minutes, seconds);

                //calendar.add(Calendar.MILLISECOND, rawOffset);

                t_Value = new java.sql.Timestamp(calendar.getTime().getTime());


            }
            return t_Value;
    }


    public static BigDecimal getBigDecimal(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, BigDecimal field_Value)
    throws Field_Validation_Exception {



        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);

        }

        if(field_Description_Node.getType() != "BigDecimal"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not BigDecimal for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }

        BigDecimal  bd_Value;

        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            bd_Value = field_Value;
        }else{
            parameter_Value = parameter_Value.trim();
            if(parameter_Value.length()!=0){
                char    c;
                for(int i=0;i<parameter_Value.length();i++){
                    c=parameter_Value.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&   c != '-' && c != '.'){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (digits, . and - only)";
                        throw new Field_Validation_Exception(message);
                    }
                }



                if(!parameter_Value.matches("[-]*[0-9]*.*[0-9]*")){
                    // error
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not determine Decimal pattern)";
                    throw new Field_Validation_Exception(message);
                }

                try{
                    bd_Value = new BigDecimal(parameter_Value).setScale(field_Description_Node.getScale(), BigDecimal.ROUND_HALF_UP);
                }
                catch(NumberFormatException nfe){
                    // error
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. " + nfe.getMessage();
                    throw new Field_Validation_Exception(message);
                }

                BigDecimal  bigDecimal_Min_Value;

                if((bigDecimal_Min_Value = field_Description_Node.getBigDecimal_Min_Value()) != null){
                    if(bd_Value.compareTo(bigDecimal_Min_Value) < 0){
                        String message = field_Description_Node.getField_Title() + " Minumum Value is " + bigDecimal_Min_Value + ".";
                        throw new Field_Validation_Exception(message);

                    }
                }

                BigDecimal  bigDecimal_Max_Value;


                if((bigDecimal_Max_Value = field_Description_Node.getBigDecimal_Max_Value()) != null){
                    if(bd_Value.compareTo(bigDecimal_Max_Value) > 0){
                        String message = field_Description_Node.getField_Title() + " Maximum Value is :" + bigDecimal_Max_Value + ".";
                        throw new Field_Validation_Exception(message);

                    }
                }


            }else{
		    // zero length parameter passed, use default.
                    String message = field_Description_Node.getField_Title() + " Can not be left blank. A number is required.";
                    throw new Field_Validation_Exception(message);
            }
        }

        return bd_Value;
    }


    public static Double getDouble(HttpServletRequest request, String field_Name, Field_Description_List field_Description_List, Double field_Value)
    throws Field_Validation_Exception {



        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);

        }

        if(field_Description_Node.getType() != "Double"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Double for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }

        Double  d_Value;

        String parameter_Value = request.getParameter(field_Name);
        if(parameter_Value == null){
            // no parameter passed, use default.
            d_Value = field_Value;
        }else{
            parameter_Value = parameter_Value.trim();

            if(parameter_Value.length()!=0){
                char    c;
                for(int i=0;i<parameter_Value.length();i++){
                    c=parameter_Value.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&   c != '-' && c != '.'){
                        String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (digits, . and - only)";
                        throw new Field_Validation_Exception(message);
                    }
                }



                if(!parameter_Value.matches("[-]*[0-9]*.*[0-9]*")){
                    // error
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. (Can not determine Decimal pattern)";
                    throw new Field_Validation_Exception(message);
                }

                try{
                    d_Value = new Double(parameter_Value);
                }
                catch(NumberFormatException nfe){
                    // error
                    String message ="Answer :" + parameter_Value + ": for field :" + field_Description_Node.getField_Title() + ": is not valid. " + nfe.getMessage();
                    throw new Field_Validation_Exception(message);
                }

                Double  double_Min_Value;

                if((double_Min_Value = field_Description_Node.getDouble_Min_Value()) != null){
                    if(d_Value.compareTo(double_Min_Value) < 0){
                        String message = field_Description_Node.getField_Title() + " Minumum Value is " + double_Min_Value + ".";
                        throw new Field_Validation_Exception(message);

                    }
                }

                Double  double_Max_Value;


                if((double_Max_Value = field_Description_Node.getDouble_Max_Value()) != null){
                    if(d_Value.compareTo(double_Max_Value) > 0){
                        String message = field_Description_Node.getField_Title() + " Maximum Value is :" + double_Max_Value + ".";
                        throw new Field_Validation_Exception(message);

                    }
                }


            }else{
		    // zero length parameter passed, use default.
                    String message = field_Description_Node.getField_Title() + " Can not be left blank. A number is required.";
                    throw new Field_Validation_Exception(message);
            }
        }

        return d_Value;
    }

    
    
    public static String fill_String(String field_Name, Field_Description_List field_Description_List, String field_Value)
    throws Field_Validation_Exception {
        
       Integer min_Length;
        Integer max_Length;
        int length;

        Field_Description_Node field_Description_Node = field_Description_List.getField_Node(field_Name);
        if(field_Description_Node==null){
            String message ="No Field_Description_Node for field :" + field_Name + ":";
            throw new Field_Validation_Exception(message);
        }

        if(field_Description_Node.getType() != "Filled_String"){
            String message ="Field_Description_Node Type:" + field_Description_Node.getType() + ":  is not Filled_String for field :" + field_Description_Node.getField_Title() + ":";
            throw new Field_Validation_Exception(message);
        }
        
        field_Value = field_Value.trim();
        
        length = field_Value.length();


        if((min_Length = field_Description_Node.getMin_Length()) != null){
            if(length < min_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " is required.";
                throw new Field_Validation_Exception(message);

            }
        }


        if((max_Length = field_Description_Node.getMax_Length()) != null){
            if(length > max_Length.intValue()){
                String message = field_Description_Node.getField_Title() + " Maximum Length is :" + max_Length.toString() + ".";
                throw new Field_Validation_Exception(message);

            }
        }

        switch(field_Description_Node.getFill_Type()){
            case Field_Description_Node.FILL_TYPE_NONE:
                break;

            case Field_Description_Node.FILL_TYPE_LEFT:
                field_Value = StringPlus.left_Fill(field_Value, max_Length.intValue(), field_Description_Node.getFill_Char() );
                break;

            case Field_Description_Node.FILL_TYPE_RIGHT:
                field_Value = StringPlus.right_Fill(field_Value, max_Length.intValue(), field_Description_Node.getFill_Char() );

                break;
            case Field_Description_Node.FILL_TYPE_CENTER:
                field_Value = StringPlus.center_Fill(field_Value, max_Length.intValue(), field_Description_Node.getFill_Char() );
                break;
            case Field_Description_Node.FILL_TYPE_TRIM:
                field_Value.trim();
                break;
            default:
                String message = field_Description_Node.getField_Title() + " has unknown Fill type :" + field_Description_Node.getFill_Type() + ".";
                throw new Field_Validation_Exception(message);
        }



        return field_Value;
    }

}
