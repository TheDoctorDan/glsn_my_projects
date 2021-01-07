/*
 * DFDay_Of_Week_Numeric.java
 *
 * Created on June 18 2007, 12:41 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import java.util.*;
import javax.servlet.http.*;


/**
 *
 * @author  dgleeson
 */
public class DFDay_Of_Week_Numeric extends DFBase {
       
    
    private String value;
    private String[] enumerated_Values;
    private int max_Length;
    
    
    private static String sql_Type = "int";

  
    public static final int DAY_OF_WEEK_SUNDAY=0;
    public static final int DAY_OF_WEEK_MONDAY=1;
    public static final int DAY_OF_WEEK_TUESDAY=2;
    public static final int DAY_OF_WEEK_WEDNESDAY=3;
    public static final int DAY_OF_WEEK_THURSDAY=4;
    public static final int DAY_OF_WEEK_FRIDAY=5;
    public static final int DAY_OF_WEEK_SATURDAY=6;
    
    
    public static final String[] day_Values = { "Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat" };

    /** Creates a new instance of DFDay_Of_Week */
    public DFDay_Of_Week_Numeric(String field_Name, String field_Title) throws DFException {
 
        super(field_Name, field_Title);
        if(day_Values.length <1){
            throw new DFException("DFString_Enumerated: constructor(): field_Name :" + field_Name + ": You need at least 2 enumerated values.");
        }
        this.enumerated_Values = day_Values;
	this.max_Length=1;
	
        

        this.value=day_Values[0];
        this.request_Value=this.value;
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
    
    
    
    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(String value) throws DFException {
        
        for(int i=0; i<this.enumerated_Values.length;i++){
            if(this.enumerated_Values[i].equals(value)){
                this.value = value;
                this.request_Value = this.value;
                this.request_Error ="";
                return;
            }
        }
        throw new DFException(this.field_Title + " invalid choice.");
        
    }
    
    public void clearValue() {        
        this.value = "";
        this.request_Value = "";
        this.request_Error ="";
        return;
    }
    
    
    
    
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        
        String parameter_Value = request.getParameter(prefix+this.field_Name+suffix);
        this.request_Value=parameter_Value;
        this.request_Error ="";
        
        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = this.value;
        }
        if(parameter_Value==null)
            parameter_Value="";
        
        try {
            setValue(parameter_Value);
        }
        catch(DFException dfe){
            this.request_Error = dfe.getMessage();
            return REQUEST_ERRCODE_WRONG;
        }
        return REQUEST_ERRCODE_NONE;
    }
    
    
    
    
    public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException {
        try {
            int i = resultSet.getInt(this.field_Name)-1;
            if(i<0 || i>=day_Values.length)
                this.value=day_Values[0];
            else
                this.value = day_Values[i];
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        this.request_Value = this.value;
    }
    
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
        try {
            preparedStatement.setInt(count, getCalendar_Day_Week_Index_Value());
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        return preparedStatement;
    }
    
    
    
    public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.max_Length + ") NOT NULL default '0',\n");
    }
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.max_Length + ") NOT NULL default '" + default_Value + "',\n");
    }
    
    
    
    public String toString(){
        return(this.value);
    }
    
    /**
     * Getter for property enumerated_Values.
     * @return Value of property enumerated_Values.
     */
    public java.lang.String[] getEnumerated_Values() {
        return this.enumerated_Values;
    }
    
    /**
     * Setter for property enumerated_Values.
     * @param enumerated_Values New value of property enumerated_Values.
     */
    private void setEnumerated_Values(java.lang.String[] enumerated_Values) {
        this.enumerated_Values = enumerated_Values;
    }
    
    
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int not_Used_Size, String option, String suffix) {
        String out_Option;
        if(option==null)
            out_Option="";
        else
            out_Option=option;
        
        StringBuffer out = new StringBuffer();
        
        out.append("\t<select " +
        "name='" + prefix + this.field_Name + suffix + "' " +
        "id='" + prefix + this.field_Name + suffix + "' " +
        out_Option + " >\n");
        
        boolean found=false;
        for(int i = 0; i<enumerated_Values.length; i++){
            if(this.value.equals(enumerated_Values[i])){
                found=true;
                break;
            }
        }
        if(!found)
            out.append("\t\t<option value='Select Value' selected>Select Value</option>\n");
        
        for(int i = 0; i<enumerated_Values.length; i++){
            if(this.value.equals(enumerated_Values[i])){
                out.append("\t\t<option value='" + StringPlus.html_Encode(enumerated_Values[i]) + "' selected>" + StringPlus.html_Encode(enumerated_Values[i]) + "</option>\n");
            } else {
                out.append("\t\t<option value='" + StringPlus.html_Encode(enumerated_Values[i]) + "'>" + StringPlus.html_Encode(enumerated_Values[i]) + "</option>\n");
            }
        }
        
        out.append("\t</select>\n");
        if(!found)
            out.append(RED_ASTERISK_HTML);
        out.append("<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n");
        
        return out.toString();
    }
    
    
    public String[] toHTML_Input_Radio(String prefix, String web_Root, String form_Name, String suffix) {
        LinkedList list = new LinkedList();
        
        // out.append("\t<select name='" + prefix + this.field_Name + suffix + "' id='" + prefix + this.field_Name + suffix + "'>\n");
        
        for(int i = 0; i<enumerated_Values.length; i++){
            if(this.value.equals(enumerated_Values[i])){
                list.add("<input type=radio name='" + prefix + this.field_Name + suffix + "' value='" + StringPlus.html_Encode(enumerated_Values[i]) + "' checked >" + StringPlus.html_Encode(enumerated_Values[i]) + "</input>");
                
            } else {
                list.add("<input type=radio name='" + prefix + this.field_Name + suffix + "' value='" + StringPlus.html_Encode(enumerated_Values[i]) + "'>" + StringPlus.html_Encode(enumerated_Values[i]) + "</input>");
            }
        }
        list.add("<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n");
        
        String[] answer = new String[list.size()];
        for(int i=0;i<list.size();i++)
            answer[i] = (String)list.get(i);
        return answer;
    }
    
    
    /**
     * Getter for property sql_Type.
     * @return Value of property sql_Type.
     */
    public java.lang.String getSql_Type() {
        return sql_Type;
    }
    
    
    public int getMax_Length(){
        return max_Length;
    }
    
    public boolean same_Field_Layout(DFBase dFBase){

        if (dFBase instanceof DFDay_Of_Week_Numeric){

            DFDay_Of_Week_Numeric temp = (DFDay_Of_Week_Numeric) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            
            if(this.enumerated_Values.length != temp.enumerated_Values.length)
                return false;
            for(int i=0; i<this.enumerated_Values.length;i++){
                if(!this.enumerated_Values[i].equals(temp.enumerated_Values[i]))
                    return false;
            }
            
            return true;
        }else if(dFBase instanceof DFInteger){

            DFInteger temp = (DFInteger) dFBase;
            if(!same_Base_Field_Layout(temp)){
                return false;
            }
            
            if(!this.sql_Type.equals(temp.getSql_Type())){
                return false;
            }
            
            if(this.getMax_Length() != temp.getMax_Length()){
                return false;
            }
            
            return true;
	    
        }else{
            // wrong type
            return false;
        }
    }
    
}