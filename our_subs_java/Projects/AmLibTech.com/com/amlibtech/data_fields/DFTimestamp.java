/*
 * DFDTimestamp.java
 *
 * Created on April 13, 2006, 4:53 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import java.util.*;
import javax.servlet.http.*;
import java.sql.Timestamp;
/**
 *
 * @author  dgleeson
 */
public class DFTimestamp extends DFBase {
    
    private static String sql_Type = "datetime";
    private GregorianCalendar value;
    
    
    private String request_Value_For_Date;
    private String request_Value_For_Time;
    
    
    /** Creates a new instance of DFTimestamp */
    
    
    public DFTimestamp(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title);
        this.value = new GregorianCalendar();
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
     * Getter for property value.
     * @return Value of property value.
     */
    public GregorianCalendar getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(GregorianCalendar value) {
        this.value = value;
        setRequest_Value();
    }
    
    
    public void setValue(long value) {
        this.value.setTimeInMillis(value);
        setRequest_Value();
    }
    
    
    
    private void setRequest_Value(){
        this.request_Value_For_Date=this.get_MDY();
        this.request_Value_For_Time=this.get_Civilian_HMS();
        this.request_Error ="";
    }
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        
        String field_Name_For_Date = prefix+this.field_Name +suffix+ "_Date";
        String field_Name_For_Time = prefix+this.field_Name +suffix+ "_Time";
        
        int []     days = {
            0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31                               };
            
            
            GregorianCalendar t_Value;
            
            String parameter_Value_For_Date = request.getParameter(field_Name_For_Date);
            String parameter_Value_For_Time = request.getParameter(field_Name_For_Time);
            
            this.request_Value_For_Date = parameter_Value_For_Date;
            this.request_Value_For_Time = parameter_Value_For_Time;
            this.request_Error = "";
            
            if(parameter_Value_For_Date == null && parameter_Value_For_Time == null){
                // no parameter passed, use default.
                t_Value = this.value;
            }else{
                
                if(parameter_Value_For_Date == null){
                    this.request_Error = "Answer :" + parameter_Value_For_Date + ": for field :" + field_Title + ": is not valid. You can not have empty Date and non-empty Time.";
                    return REQUEST_ERRCODE_WRONG;
                    
                }
                parameter_Value_For_Date = parameter_Value_For_Date.trim();
                char    c;
                for(int i=0;i<parameter_Value_For_Date.length();i++){
                    c=parameter_Value_For_Date.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&  c != '/'){
                        this.request_Error = "Answer :" + parameter_Value_For_Date + ": for field :" + field_Title + ": is not valid. You can have digits and / only.";
                        return REQUEST_ERRCODE_WRONG;
                    }
                }
                
                int month, day, year;
                
                if(parameter_Value_For_Date.matches("[0-9]+/[0-9]+/[0-9]+")){
                    // m/d/y
                    String[] pieces_Of_Date = parameter_Value_For_Date.split("/");
                    if(pieces_Of_Date.length!=3){
                        this.request_Error = "Answer :" + parameter_Value_For_Date + ": for field :" + field_Title + ": is not valid. Can not separate M, D, Y.";
                        return REQUEST_ERRCODE_WRONG;
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=Integer.parseInt(pieces_Of_Date[1]);
                        year=Integer.parseInt(pieces_Of_Date[2]);
                    }
                }else if(parameter_Value_For_Date.matches("[0-9]+/[0-9]+")){
                    // m/y
                    String[] pieces_Of_Date = parameter_Value_For_Date.split("/");
                    if(pieces_Of_Date.length!=2){
                        this.request_Error = "Answer :" + parameter_Value_For_Date + ": for field :" + field_Title + ": is not valid. Can not separate M, Y.";
                        return REQUEST_ERRCODE_WRONG;
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=1;
                        year=Integer.parseInt(pieces_Of_Date[1]);
                    }
                }else {
                    // error
                    this.request_Error = "Answer :" + parameter_Value_For_Date + ": for field :" + field_Title + ": is not valid. Can not determine date pattern m/d/y or m/y.";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                
                if (month < 1 || month > 12){
                    this.request_Error = "Answer :" + parameter_Value_For_Date + ": for field :" + field_Title + ": is not valid. Month must be in 1 thru 12.";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                if (day < 1 || day > days[month]){
                    this.request_Error = "Answer :" + parameter_Value_For_Date + ": for field :" + field_Title + ": is not valid. Day must be in 1 thru " + days[month] + ".";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                if (year < 0 || year > 9999){
                    this.request_Error = "Answer :" + parameter_Value_For_Date + ": for field :" + field_Title + ": is not valid. Year must be in 1 thru 9999.";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                
                
                
                
                
                if(parameter_Value_For_Time == null){
                    this.request_Error = "Answer :" + parameter_Value_For_Time + ": for field :" + field_Title + ": is not valid. You can not have non-empty Date and empty Time.";
                    return REQUEST_ERRCODE_WRONG;
                }
                parameter_Value_For_Time = parameter_Value_For_Time.trim();
                for(int i=0;i<parameter_Value_For_Time.length();i++){
                    c=parameter_Value_For_Time.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&
                    c != ':' && c != 'A' && c != 'a' && c != 'P' && c != 'p' && c != 'M' && c != 'm' && c != ' '){
                        this.request_Error = "Answer :" + parameter_Value_For_Time + ": for field :" + field_Title + ": is not valid. You can have digits, :, or AM/PM only.";
                        return REQUEST_ERRCODE_WRONG;
                    }
                }
                
                int hours, minutes, seconds;
                boolean ante_Meridian;
                
                if(parameter_Value_For_Time.matches("[0-9]+:[0-9]+:[0-9]+ [aApP][Mm]")){
                    // h:m:s a/pm
                    String[] primary_Pieces_Of_Time = parameter_Value_For_Time.split(" ");
                    if(primary_Pieces_Of_Time.length!=2){
                        this.request_Error = "Answer :" + parameter_Value_For_Time + ": for field :" + field_Title + ": is not valid. Can not separate H, M, S AM/PM). Splits into " + primary_Pieces_Of_Time.length + " pieces.";
                        return REQUEST_ERRCODE_WRONG;
                    }else{
                        if(primary_Pieces_Of_Time[1].equalsIgnoreCase("AM")){
                            ante_Meridian=true;
                        }else if(primary_Pieces_Of_Time[1].equalsIgnoreCase("PM")){
                            ante_Meridian=false;
                        }else{
                            this.request_Error = "Answer :" + parameter_Value_For_Time + ": for field :" + field_Title + ": is not valid. Can not determine AM/PM.";
                            return REQUEST_ERRCODE_WRONG;
                        }
                    }
                    
                    
                    String[] secondary_Pieces_Of_Time = primary_Pieces_Of_Time[0].split(":");
                    if(secondary_Pieces_Of_Time.length!=3){
                        this.request_Error = "Answer :" + parameter_Value_For_Time + ": for field :" + field_Title + ": is not valid. Can not separate H, M, S AM/PM). Splits into " + secondary_Pieces_Of_Time.length + " pieces.";
                        return REQUEST_ERRCODE_WRONG;
                    }else{
                        hours=Integer.parseInt(secondary_Pieces_Of_Time[0]);
                        minutes=Integer.parseInt(secondary_Pieces_Of_Time[1]);
                        seconds=Integer.parseInt(secondary_Pieces_Of_Time[2]);
                    }
                    
                }else {
                    // error
                    this.request_Error = "Answer :" + parameter_Value_For_Time + ": for field :" + field_Title + ": is not valid. Can not determine time pattern h:m:s AM/PM.";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                
                if (hours < 1 || hours > 12){
                    this.request_Error = "Answer :" + parameter_Value_For_Time + ": for field :" + field_Title + ": is not valid. Hours must be in 1 thru 12.";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                if (minutes < 0 || minutes > 59){
                    this.request_Error = "Answer :" + parameter_Value_For_Time + ": for field :" + field_Title + ": is not valid. Minutes must be in 0 thru 59.";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                if (seconds < 0 || seconds > 59){
                    this.request_Error = "Answer :" + parameter_Value_For_Time + ": for field :" + field_Title + ": is not valid. Seconds must be in 0 thru 59.";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                
                
                
                int hour_Of_The_Day = hours;
                if(ante_Meridian && hours==12)
                    hour_Of_The_Day = 0;
                if(!ante_Meridian && hours!=12)
                    hour_Of_The_Day += 12;
                
 
                
                t_Value = new GregorianCalendar(this.value.getTimeZone());
                t_Value.clear(Calendar.MILLISECOND);
                t_Value.set(year, month-1, day, hour_Of_The_Day, minutes, seconds);
                
                
            }
            setValue(t_Value);
            return REQUEST_ERRCODE_NONE;
    }
    
    
    
    public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException {
        //Calendar calendar = Calendar.getInstance(this.timeZone, Locale.US);
        Calendar calendar = Calendar.getInstance(Calendar_Plus.getBOD_TimeZone());
        java.sql.Timestamp temp_Timestamp;
        try {
            //temp_Timestamp = resultSet.getTimestamp(this.field_Name, calendar);
            temp_Timestamp = resultSet.getTimestamp(this.field_Name, calendar);
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        setValue(temp_Timestamp.getTime());
    }
    
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
        //Calendar calendar = Calendar.getInstance(this.timeZone, Locale.US);
        Calendar calendar = Calendar.getInstance(Calendar_Plus.getBOD_TimeZone());
        java.sql.Timestamp temp_Timestamp;
        temp_Timestamp = new java.sql.Timestamp(this.value.getTime().getTime());
        try {
            preparedStatement.setTimestamp(count++, temp_Timestamp, calendar);
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        return preparedStatement;
    }
    
    
    public String get_MDY() {
        return Calendar_Plus.get_MDY(this.value);
    }
    
    
    
    
    public String get_Y_M_D_For_Sql() {
        return Calendar_Plus.get_Y_M_D_For_Sql(this.value);
    }
    
    
    
    public String get_Month_Abbr_Year() {
        return Calendar_Plus.get_Month_Abbr_Year(this.value);
    }
    
    
    
    public String get_Month_Name_Year() {
        return Calendar_Plus.get_Month_Name_Year(this.value);
    }
    
    
    
    public String get_Civilian_English_Date_And_Time() {
        return Calendar_Plus.get_Civilian_English_Date_And_Time(this.value);
    }
    
    
    
    
    
    public String get_Civilian_HMS() {
        return Calendar_Plus.get_Civilian_HMS(this.value);
    }
    
    
    
    
    
    
    
    
    public String get_Miliatry_HMS() {
        return Calendar_Plus.get_Military_HMS(this.value);
    }
    
    
    
    
    
    
    public String get_Y_M_D__HMS_For_Sql() {
        return Calendar_Plus.get_Y_M_D_For_Sql(this.value) + " " + Calendar_Plus.get_Military_HMS(this.value);
    }
    
    
    
    
    
    public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + sql_Type + " NOT NULL default '1969-12-31 18:00:00',\n");
    }
    
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + " NOT NULL default '" + default_Value + "',\n");
    }
    
    
    public String toString(){
        return this.get_Civilian_English_Date_And_Time();
    }
    
    
    public String getSql_String(){
        return SQLUtil.encode(this.get_Y_M_D__HMS_For_Sql());
    }
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int not_Used_Size, String not_Used_Option, String suffix) {
        StringBuffer out = new StringBuffer();
        
        out.append("\t<input " +
        "name='" + prefix+this.field_Name+suffix + "_Date" + "' " +
        "id='" + prefix+this.field_Name+suffix + "_Date" + "' " +
        "type='text' " +
        "value='" + StringPlus.html_Encode(this.request_Value_For_Date) + "' " +
        "size='10' " +
        "maxlength='10' >\n");
        
        out.append("\t<a href=\"javascript:date_selector('document." + form_Name + "."+ prefix+this.field_Name+suffix + "_Date" + "', document." + form_Name + "." + prefix+this.field_Name+suffix + "_Date" + ".value, '" + web_Root + "');\">\n");
        
        out.append("\t<img src=\"" + web_Root + "/scripts/date_selector/cal.gif\" width='16' height='16' border='0' alt=\"Click Here to Pick the date\"></a>\n");
        
        
        out.append("\t<input " +
        "name='" + prefix+this.field_Name+suffix + "_Time" + "' " +
        "id='" + prefix+this.field_Name+suffix + "_Time" + "' " +
        "type='text' " +
        "value='"+ StringPlus.html_Encode(this.request_Value_For_Time) + "' " +
        "size='11' " +
        "maxlength='11' >\n");
        
        out.append("\t<a href=\"javascript:time_selector('document." + form_Name + "." + prefix+this.field_Name+suffix + "_Time" + "', document." + form_Name + "." + prefix+this.field_Name+suffix + "_Time" + ".value, '" + web_Root + "');\">\n");
        
        out.append("\t<img src=\"" + web_Root + "/scripts/time_selector/time.gif\" width='16' height='16' border='0' alt=\"Click Here to Pick the time\"></a>\n");
        out.append("<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n");
        return out.toString();
    }
    
    
    
    public String toHREF_Parameter(){
        return "&" + this.field_Name + "_Date" + "=" + StringPlus.http_Encode(this.request_Value_For_Date) +
               "&" + this.field_Name + "_Time" + "=" + StringPlus.http_Encode(this.request_Value_For_Time);
    }

    
    
     /**
     * Getter for property sql_Type.
     * @return Value of property sql_Type.
     */
    public java.lang.String getSql_Type() {
        return sql_Type;
    }    
    
    
    public boolean same_Field_Layout(DFBase dFBase){
        if (dFBase instanceof DFTimestamp){
            DFTimestamp temp = (DFTimestamp) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equalsIgnoreCase(temp.sql_Type))
                return false;
            return true;
        }else{
            // wrong type
            return false;
        }
    }
    
     
    
    public int compareTo(DFTimestamp dfTimestamp){
        
        int i;
         
        i = this.value.get(Calendar.YEAR) - dfTimestamp.value.get(Calendar.YEAR);
        if(i!=0) return i;
        i = this.value.get(Calendar.MONTH) - dfTimestamp.value.get(Calendar.MONTH);
        if(i!=0) return i;
        i = this.value.get(Calendar.DAY_OF_MONTH) - dfTimestamp.value.get(Calendar.DAY_OF_MONTH);
        if(i!=0) return i;
        i = this.value.get(Calendar.HOUR_OF_DAY) - dfTimestamp.value.get(Calendar.HOUR_OF_DAY);
        if(i!=0) return i;
        i = this.value.get(Calendar.MINUTE) - dfTimestamp.value.get(Calendar.MINUTE);
        if(i!=0) return i;
        i = this.value.get(Calendar.SECOND) - dfTimestamp.value.get(Calendar.SECOND);
        if(i!=0) return i;
        i = this.value.get(Calendar.MILLISECOND) - dfTimestamp.value.get(Calendar.MILLISECOND);
        if(i!=0) return i;
        
        return 0;
   
    }
     
    
    public int compareTo(Calendar cal){
        
        int i;
         
        i = this.value.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if(i!=0) return i;
        i = this.value.get(Calendar.MONTH) - cal.get(Calendar.MONTH);
        if(i!=0) return i;
        i = this.value.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH);
        if(i!=0) return i;
        i = this.value.get(Calendar.HOUR_OF_DAY) - cal.get(Calendar.HOUR_OF_DAY);
        if(i!=0) return i;
        i = this.value.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE);
        if(i!=0) return i;
        i = this.value.get(Calendar.SECOND) - cal.get(Calendar.SECOND);
        if(i!=0) return i;
        i = this.value.get(Calendar.MILLISECOND) - cal.get(Calendar.MILLISECOND);
        if(i!=0) return i;
        
        return 0;
   
    }
    
    
}
