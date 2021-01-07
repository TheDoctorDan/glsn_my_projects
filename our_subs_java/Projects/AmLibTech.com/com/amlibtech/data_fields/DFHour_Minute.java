/*
 * DFHour_Minute.java
 *
 * Created on July 31, 2006, 9:18 AM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;

/**
 *
 * @author  dgleeson
 */
public class DFHour_Minute extends DFBase {
    
    private static String sql_Type = "int";
    
    private Integer value; // 24 hours represented as minutes 24*60 = 1440 is max
    private int hour_Style;
    private int minute_Increment;
    private static final int max_Length=4;
    
    private String request_Value_For_Hour;
    private String request_Value_For_Minute;
    private String request_Value_For_Meridian;
    
    /** Creates a new instance of DFHour_Minute */
    public DFHour_Minute(String field_Name, String field_Title, int hour_Style, int minute_Increment)
    throws DFException {
        super(field_Name, field_Title);
        
        if(hour_Style != 24 && hour_Style != 12){
            throw new DFException(this.field_Title + " Hour Style must be 12 or 24.");
        }
        if((60 % minute_Increment) !=0){
            throw new DFException(this.field_Title + " Minute Increment must be a factor of 60.");
        }
        this.hour_Style = hour_Style;
        this.minute_Increment = minute_Increment;
        this.value= new Integer(720);
        
        setRequest_Value();
        
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
    
    
    /**
     * Getter for property hour_Style.
     * @return Value of property hour_Style.
     */
    public int getHour_Style() {
        return hour_Style;
    }
    
    
    
    /**
     * Getter for property minute_Increment.
     * @return Value of property minute_Increment.
     */
    public int getMinute_Increment() {
        return minute_Increment;
    }
    
    
    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public Integer getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(Integer value) throws DFException {
        setValue(value.intValue());
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    
    public void setValue(int value) throws DFException {
        int hour = value/60;
        if(hour<0 || hour>23)
            throw new DFException("Hour must be between 0 and 23.");
        int minute = value%60;
        if(minute<0 || minute>59)
            throw new DFException("Minute must be between 0 and 59.");
        if(minute%this.minute_Increment !=0)
            throw new DFException("Minute must be multiple of "+ this.minute_Increment);
        
        this.value = new Integer(value);
        setRequest_Value();
    }
    
    
    private void setRequest_Value(){
        switch(this.hour_Style){
            case 12:
                int hour = this.value.intValue()/60;
                if(hour <12){
                    this.request_Value_For_Meridian="AM";
                }else{
                    this.request_Value_For_Meridian="PM";
                    hour-=12;
                }
                if(hour==0)
                    hour=12;
                this.request_Value_For_Hour=""+hour;
                break;
            case 24:
                this.request_Value_For_Hour=""+(this.value.intValue()/60);
                this.request_Value_For_Meridian="";
                break;
        }
        
        this.request_Value_For_Minute=""+(this.value.intValue()%60);
        this.request_Error ="";
    }
    
    
    
    public int validate_Request(String prefix, javax.servlet.http.HttpServletRequest request, String suffix) {
        String field_Name_For_Hour = prefix+this.field_Name +suffix+ "_Hour";
        String field_Name_For_Minute = prefix+this.field_Name +suffix+ "_Minute";
        String field_Name_For_Meridian = prefix+this.field_Name +suffix+ "_Meridian";
        
        Integer t_Value;
        
        String parameter_Value_For_Hour = request.getParameter(field_Name_For_Hour);
        String parameter_Value_For_Minute = request.getParameter(field_Name_For_Minute);
        
        String parameter_Value_For_Meridian;
        
        if(this.hour_Style==12)
            parameter_Value_For_Meridian = request.getParameter(field_Name_For_Meridian);
        else
            parameter_Value_For_Meridian="";
        
        this.request_Value_For_Hour = parameter_Value_For_Hour;
        this.request_Value_For_Minute = parameter_Value_For_Minute;
        this.request_Value_For_Meridian = parameter_Value_For_Meridian;
        this.request_Error = "";
        
        if(parameter_Value_For_Hour == null && parameter_Value_For_Minute == null && (this.hour_Style==12 && parameter_Value_For_Meridian == null)){
            // no parameter passed, use default.
            t_Value = this.value;
        }else{
            if(parameter_Value_For_Hour == null){
                this.request_Error = "Answer :null: for field :" + field_Title + ": is not valid. You can not have empty Hour and non-empty Minute.";
                return REQUEST_ERRCODE_WRONG;
            }
            parameter_Value_For_Hour = parameter_Value_For_Hour.trim();
            char    c;
            for(int i=0;i<parameter_Value_For_Hour.length();i++){
                c=parameter_Value_For_Hour.charAt(i);
                if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER){
                    this.request_Error = "Answer :" + parameter_Value_For_Hour + ": for field :" + field_Title + ": is not valid. You can have digits only.";
                    return REQUEST_ERRCODE_WRONG;
                }
            }
            int hour=Integer.parseInt(parameter_Value_For_Hour);
            
            
            if(parameter_Value_For_Minute == null){
                this.request_Error = "Answer :null: for field :" + field_Title + ": is not valid. You can not have empty Minute and non-empty Hour.";
                return REQUEST_ERRCODE_WRONG;
            }
            parameter_Value_For_Minute = parameter_Value_For_Minute.trim();
            for(int i=0;i<parameter_Value_For_Minute.length();i++){
                c=parameter_Value_For_Minute.charAt(i);
                if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER){
                    this.request_Error = "Answer :" + parameter_Value_For_Minute + ": for field :" + field_Title + ": is not valid. You can have digits only.";
                    return REQUEST_ERRCODE_WRONG;
                }
            }
            int minute=Integer.parseInt(parameter_Value_For_Minute);
            
            int hour_Of_The_Day;
            
            if(this.hour_Style==12){
                if(parameter_Value_For_Meridian == null){
                    this.request_Error = "Answer :null: for field :" + field_Title + ": is not valid. You can not have empty Meridian and non-empty Hour.";
                    return REQUEST_ERRCODE_WRONG;
                }
                parameter_Value_For_Meridian = parameter_Value_For_Meridian.trim();
                if(!parameter_Value_For_Meridian.equals("AM") && !parameter_Value_For_Meridian.equals("PM")){
                    this.request_Error = "Answer :" + parameter_Value_For_Meridian + ": for field :" + field_Title + ": is not valid. You can have AM or PM only.";
                    return REQUEST_ERRCODE_WRONG;
                }
                if(hour<1 || hour>12){
                    this.request_Error = "Answer :" + parameter_Value_For_Hour + ": for field :" + field_Title + ": is not valid. Must be between 1 and 12.";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                hour_Of_The_Day = hour;
                if(parameter_Value_For_Meridian.equals("AM") && hour==12)
                    hour_Of_The_Day = 0;
                if(parameter_Value_For_Meridian.equals("PM") && hour!=12)
                    hour_Of_The_Day += 12;
            }else{
                if(hour<0 || hour>23){
                    this.request_Error = "Answer :" + parameter_Value_For_Hour + ": for field :" + field_Title + ": is not valid. Must be between 0 and 23.";
                    return REQUEST_ERRCODE_WRONG;
                }
                hour_Of_The_Day = hour;
                
            }
            if(minute<0 || minute>59){
                this.request_Error = "Answer :" + parameter_Value_For_Minute + ": for field :" + field_Title + ": is not valid. Must be between 0 and 59.";
                return REQUEST_ERRCODE_WRONG;
            }
            if(minute%this.minute_Increment !=0){
                this.request_Error = "Answer :" + parameter_Value_For_Minute + ": for field :" + field_Title + ": is not valid. Must be a multiple of " + this.minute_Increment +".";
                return REQUEST_ERRCODE_WRONG;
            }
            
            t_Value=new Integer(hour_Of_The_Day*60+minute);
            
        }
        try {
            setValue(t_Value);
        }
        catch(DFException dfe){
            this.request_Error = dfe.getMessage();
            return REQUEST_ERRCODE_WRONG;
        }
        return REQUEST_ERRCODE_NONE;
    }
    
    
    public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException {
        try {
            this.value = new Integer(resultSet.getInt(this.field_Name));
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        setRequest_Value();
    }
    
    
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
        try {
            preparedStatement.setInt(count, this.value.intValue());
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        return preparedStatement;
    }
    
    
    public String getSQL_Declaration() {
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.max_Length + ") NOT NULL default '0',\n");
    }
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.max_Length + ") NOT NULL default '" + default_Value + "',\n");
    }
    
    
    public String toStringFormatted(){
        String out_Hour;
        String out_Minute;
        String out_Meridian;
        
        out_Minute=""+(this.value.intValue()%60);
        if(out_Minute.length()==1)
            out_Minute="0"+out_Minute;
        
        switch(this.hour_Style){
            case 12:
                int hour = this.value.intValue()/60;
                if(hour <12){
                    out_Meridian="AM";
                }else{
                    out_Meridian="PM";
                    hour-=12;
                }
                if(hour==0)
                    hour=12;
                out_Hour=""+hour;
                return out_Hour+":"+out_Minute +" " + out_Meridian;
                
            default:
                out_Hour=""+(this.value.intValue()/60);
                out_Meridian="";
                return out_Hour+":"+out_Minute;
                
        }
    }
    
    public String toHTML() {
        return (StringPlus.html_Encode(this.toStringFormatted()));
    }
    
    public String toString(){
        return(this.value.toString());
    }
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int size, String option, String suffix) {
        
        
        
        String out_Option;
        if(option==null)
            out_Option="";
        else
            out_Option=option;
        
        int hour;
        int minute;
        String meridian;
        
        minute=this.value.intValue()%60;
        
        
        
        switch(this.hour_Style){
            case 12:
                hour = this.value.intValue()/60;
                if(hour <12){
                    meridian="AM";
                }else{
                    meridian="PM";
                    hour-=12;
                }
                if(hour==0)
                    hour=12;
                
                
                break;
            default:
                hour=this.value.intValue()/60;
                meridian="";
                break;
        }
        
        
        StringBuffer out = new StringBuffer();
        
        out.append("<table><tbody><tr>\n");
        
        
        out.append("<td>\n");
        out.append("\t<select " +
        "name='" + prefix + this.field_Name + suffix + "_Hour' " +
        "id='" + prefix + this.field_Name + suffix + "_Hour' " +
        out_Option + " >\n");
        if(this.hour_Style==12){
            for(int i = 1; i<=12; i++){
                if(i==hour){
                    out.append("\t\t<option value='" + i + "' selected>" + i + "</option>\n");
                } else {
                    out.append("\t\t<option value='" + i + "'>" + i + "</option>\n");
                }
            }
        }else{
            for(int i = 0; i<=23; i++){
                if(i==hour){
                    out.append("\t\t<option value='" + i + "' selected>" + i + "</option>\n");
                } else {
                    out.append("\t\t<option value='" + i + "'>" + i + "</option>\n");
                }
            }
        }
        out.append("\t</select>\n");
        out.append("</td>");
        
        
        
        out.append("<td>\n");
        out.append("\t<select name='" + prefix + this.field_Name + suffix + "_Minute' id='" + prefix + this.field_Name + suffix + "_Minute' " + " >\n");
        for(int i = 0; i<60; i+= this.minute_Increment){
            if(i==minute){
                out.append("\t\t<option value='" + i + "' selected>" + i + "</option>\n");
            } else {
                out.append("\t\t<option value='" + i + "'>" + i + "</option>\n");
            }
        }
        out.append("\t</select>\n");
        out.append("</td>");
        
        if(this.hour_Style==12){
            
            out.append("<td>\n");
            out.append("\t<select name='" + prefix + this.field_Name + suffix + "_Meridian' id='" + prefix + this.field_Name + suffix + "_Meridian' " + " >\n");
            for(int i = 0; i<2; i++){
                String dmys;
                if(i==0)dmys="AM";
                else dmys="PM";
                
                if(dmys.equals(meridian)){
                    out.append("\t\t<option value='" + dmys + "' selected>" + dmys + "</option>\n");
                } else {
                    out.append("\t\t<option value='" + dmys + "'>" + dmys + "</option>\n");
                }
            }
            out.append("\t</select>\n");
            out.append("</td>");
        }
        
        out.append("<td><font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font></td>\n");
        
        out.append("</tr></tbody></table>\n");
        return out.toString();
        
    }
    
    public String getSql_Type() {
        return sql_Type;
    }
    
    
    public boolean same_Field_Layout(DFBase dFBase) {
        if (dFBase instanceof DFHour_Minute){
            DFHour_Minute temp = (DFHour_Minute) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            
            if(this.max_Length != temp.max_Length)
                return false;
            
            return true;
        }else if(dFBase instanceof DFInteger){
            DFInteger temp = (DFInteger) dFBase;
            if(!same_Base_Field_Layout(temp)){
                return false;
            }
            if(!this.sql_Type.equals(temp.getSql_Type())){
                return false;
            }
            
            if(this.max_Length != temp.getMax_Length())
                return false;
            return true;
        }else{
            // wrong type
            return false;
        }
    }
    
    
    
    
    
    public String toHREF_Parameter() {
        
        int hour;
        int minute;
        String meridian;
        
        minute=this.value.intValue()%60;
        
        switch(this.hour_Style){
            case 12:
                hour = this.value.intValue()/60;
                if(hour <12){
                    meridian="AM";
                }else{
                    meridian="PM";
                    hour-=12;
                }
                if(hour==0)
                    hour=12;
                
                break;
            default:
                hour=this.value.intValue()/60;
                meridian="";
                break;
        }
        
        
         if(this.hour_Style==12){
             return "&" + this.field_Name + "_Hour" + "=" + StringPlus.http_Encode(""+hour) +
                    "&" + this.field_Name + "_Minute" + "=" + StringPlus.http_Encode(""+minute) +
                    "&" + this.field_Name + "_Meridian" + "=" + StringPlus.http_Encode(meridian);
         }else{
             return "&" + this.field_Name + "_Hour" + "=" + StringPlus.http_Encode(""+hour) +
                    "&" + this.field_Name + "_Minute" + "=" + StringPlus.http_Encode(""+minute);
         }
	
    }
    
    
    
}
