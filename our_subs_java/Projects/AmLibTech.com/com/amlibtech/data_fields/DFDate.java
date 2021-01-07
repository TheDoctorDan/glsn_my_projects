/*
 * DFDate.java
 *
 * Created on March 4, 2006, 11:19 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class DFDate extends DFBase {
    private static String sql_Type = "date";
    
    private Date value;
    
    
    
    /** Creates a new instance of DFDate */
   
    
    public DFDate(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title);
        this.value = new Date();
        setRequest_Value();
    }
    
    public DFDate(String field_Name, String field_Title, Date date){
        super(field_Name, field_Title);
        this.value = date;
        setRequest_Value();
    }
    
    public DFDate(String field_Name, String field_Title, int year, int month, int day){
        super(field_Name, field_Title);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name));
        calendar.set(year, month,day);
        this.value = calendar.getTime();
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
    public Date getValue() {
        return value;
    }    
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(Date value) {
        this.value = value;
        setRequest_Value();
    }
    
    public void setValue(long value) {
        this.value = new Date(value);
        setRequest_Value();
    }
   
       
    private void setRequest_Value(){
        this.request_Value=this.get_MDY();
        this.request_Error ="";
    }
   
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        
        int []     days = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

            Date d_Value;

            String parameter_Value = request.getParameter(prefix+field_Name+suffix);
            this.request_Value = parameter_Value;
            this.request_Error = "";
            
            if(parameter_Value == null){
                // no parameter passed, use default.
                d_Value = this.value;
            }else{
                parameter_Value = parameter_Value.trim();
                char    c;
                for(int i=0;i<parameter_Value.length();i++){
                    c=parameter_Value.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&  c != '/'){
                        this.request_Error = "Answer :" + parameter_Value + ": for field :" + field_Title + ": is not valid. (digits and / only)";
                        return REQUEST_ERRCODE_WRONG;
                    }
                }

                int month, day, year;

                if(parameter_Value.matches("[0-9]+/[0-9]+/[0-9]+")){
                    // m/d/y
                    String[] pieces_Of_Date = parameter_Value.split("/");
                    if(pieces_Of_Date.length!=3){
                        this.request_Error = "Answer :" + parameter_Value + ": for field :" + field_Title + ": is not valid. (Can not separate M, D, Y)";
                        return REQUEST_ERRCODE_WRONG;
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=Integer.parseInt(pieces_Of_Date[1]);
                        year=Integer.parseInt(pieces_Of_Date[2]);
                    }
                }else if(parameter_Value.matches("[0-9]+/[0-9]+")){
                    // m/y
                    String[] pieces_Of_Date = parameter_Value.split("/");
                    if(pieces_Of_Date.length!=2){
                        this.request_Error = "Answer :" + parameter_Value + ": for field :" + field_Title + ": is not valid. (Can not separate M, Y)";
                        return REQUEST_ERRCODE_WRONG;
                    }else{
                        month=Integer.parseInt(pieces_Of_Date[0]);
                        day=1;
                        year=Integer.parseInt(pieces_Of_Date[1]);
                    }
                }else {
                    // error
                    this.request_Error = "Answer :" + parameter_Value + ": for field :" + field_Title + ": is not valid. (Can not determine date pattern m/d/y or m/y)";
                    return REQUEST_ERRCODE_WRONG;
                }


                if (month < 1 || month > 12){
                    this.request_Error = "Answer :" + parameter_Value + ": for field :" +  field_Title + ": is not valid. (Month must be in 1 thru 12)";
                    return REQUEST_ERRCODE_WRONG;
                }

                if (day < 1 || day > days[month]){
                    this.request_Error = "Answer :" + parameter_Value + ": for field :" + field_Title + ": is not valid. (Day must be in 1 thru " + days[month] + ")";
                    return REQUEST_ERRCODE_WRONG;
                }

                if (year < 0 || year > 9999){
                    this.request_Error = "Answer :" + parameter_Value + ": for field :" + field_Title + ": is not valid. (Year must be in 1 thru 9999)";
                    return REQUEST_ERRCODE_WRONG;
                }

		TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);

                Calendar calendar = Calendar.getInstance(tz, Locale.US);

                calendar.set(year, month-1, day, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND,0);
                d_Value =calendar.getTime();
            }
            
        setValue(d_Value);
        return REQUEST_ERRCODE_NONE;
    }
 
    
    
       
    public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException {        
        try {
		Date date = resultSet.getDate(this.field_Name);
                if(date ==null){
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(0,Calendar.JANUARY, 0);
                    date = new Date(calendar.getTime().getTime());
                }
            this.value = Calendar_Plus.get_BOD(date);
        }
        catch(java.sql.SQLException sqle){
             throw new DFException(sqle.getMessage());
        }
        setRequest_Value();
    }
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {           
         try {
             preparedStatement.setDate(count, new java.sql.Date(this.value.getTime()));
         }
         catch(java.sql.SQLException sqle){
             throw new DFException(sqle.getMessage());
        }
         return preparedStatement;
    }
    

    
    
    public String get_MDY() {
        TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);
	Calendar calendar = Calendar.getInstance(tz, Locale.US);
	calendar.setTime(value);
	return Calendar_Plus.get_MDY(calendar);
    }



    public String toWhere_Clause(){
        return this.field_Name + " = '" + SQLUtil.encode(this.get_Y_M_D_For_Sql())+"' ";
    }
    
    public String toWhere_Clause(String operator){
        return this.field_Name + " "+operator+"  '" + SQLUtil.encode(this.get_Y_M_D_For_Sql())+"' ";
    }
    


    public String get_Y_M_D_For_Sql() {
        TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);
	Calendar calendar = Calendar.getInstance(tz, Locale.US);
	calendar.setTime(value);
	return Calendar_Plus.get_Y_M_D_For_Sql(calendar);
    }



    public String get_Month_Abbr_Year() {
        TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);
	Calendar calendar = Calendar.getInstance(tz, Locale.US);
	calendar.setTime(value);
	return Calendar_Plus.get_Month_Abbr_Year(calendar);
    }

   
    public String get_Month_Name_Year() {
        TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);
	Calendar calendar = Calendar.getInstance(tz, Locale.US);
	calendar.setTime(value);
	return Calendar_Plus.get_Month_Name_Year(calendar);
    }


    public Date get_BOD() {

	TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(value);

	// add Zone_Offset to get to GMT
	// add DST_Offset to get to GMT
	// add 12 hours to get to GMT-12

	calendar.add(Calendar.HOUR_OF_DAY, 12 + calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000) + calendar.get(Calendar.DST_OFFSET) / (60 * 60 * 1000));

	calendar.setTimeZone(tz);

	Date result_Date = calendar.getTime();
	return result_Date;
    }


    public Date get_BOM() {

	TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(value);

	calendar.set(Calendar.DAY_OF_MONTH, 1);

	Date result_Date = calendar.getTime();
	return result_Date;
    }

    public Date get_EOM() {
	TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);

	Calendar calendar = Calendar.getInstance(tz);
	calendar.setTime(value);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	calendar.add(Calendar.MONTH, 1);
	calendar.add(Calendar.DAY_OF_MONTH, -1);
	Date result_Date = calendar.getTime();
	return result_Date;
    }

    
     public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + sql_Type + " NOT NULL default '0000-00-00',\n");
    }
     
     public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + " NOT NULL default '" + default_Value + "',\n");
    }
    
     
     public String toString(){	
         return (this.get_MDY());
    }
    
       
    
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int not_Used_Size, String option, String suffix) {
        String out_Option;
        if(option==null)
            out_Option="";
        else
            out_Option=option;
        StringBuffer out = new StringBuffer();
        
        out.append("\t<input " +
        "name='" + prefix + this.field_Name + suffix + "' " +
        "id='" + prefix + this.field_Name + suffix + "' " +
        "type='text' " +
        "value='" + StringPlus.html_Encode(this.request_Value) + "' " +
        "size='10' " +
        "maxlength='10' " +
        out_Option + " >\n");
        out.append("<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n");

        out.append("\t<a href=\"javascript:date_selector('document." + form_Name + "."+ prefix + this.field_Name + suffix + "', document." + form_Name + "." + prefix + this.field_Name + suffix + ".value, '" + web_Root + "');\">\n");
        
        out.append("\t<img src=\"" + web_Root + "/scripts/date_selector/cal.gif\" width='16' height='16' border='0' alt=\"Click Here to Pick the date\"></a>\n");       
                
        return out.toString();
    }
    
    
      /**
     * Getter for property sql_Type.
     * @return Value of property sql_Type.
     */
    public java.lang.String getSql_Type() {
        return sql_Type;
    }    
    
    
    public boolean same_Field_Layout(DFBase dFBase){
        if (dFBase instanceof DFDate){
            DFDate temp = (DFDate) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            return true;
        }else{
            // wrong type
            return false;
        }
    }
    
    
    public int compareTo(DFDate dfDate){
        TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);
        
        Calendar this_Cal = Calendar.getInstance(tz);
        this_Cal.setTime(Calendar_Plus.get_BOD(this.value));
        
        Calendar passed_Cal = Calendar.getInstance(tz);
        passed_Cal.setTime(Calendar_Plus.get_BOD(dfDate.value));

        int i;
         
        i = this_Cal.get(Calendar.YEAR) - passed_Cal.get(Calendar.YEAR);
        if(i!=0) return i;
        i = this_Cal.get(Calendar.MONTH) - passed_Cal.get(Calendar.MONTH);
        if(i!=0) return i;
        i = this_Cal.get(Calendar.DAY_OF_MONTH) - passed_Cal.get(Calendar.DAY_OF_MONTH);
        if(i!=0) return i;
        
        return 0;
   
    }
     
    public int compareTo(Date date){
        TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);
        
        Calendar this_Cal = Calendar.getInstance(tz);
        this_Cal.setTime(Calendar_Plus.get_BOD(this.value));
        
        Calendar passed_Cal = Calendar.getInstance(tz);
        passed_Cal.setTime(Calendar_Plus.get_BOD(date));

        int i;
         
        i = this_Cal.get(Calendar.YEAR) - passed_Cal.get(Calendar.YEAR);
        if(i!=0) return i;
        i = this_Cal.get(Calendar.MONTH) - passed_Cal.get(Calendar.MONTH);
        if(i!=0) return i;
        i = this_Cal.get(Calendar.DAY_OF_MONTH) - passed_Cal.get(Calendar.DAY_OF_MONTH);
        if(i!=0) return i;
        
        return 0;
   
    }
    
    
    public DFDate addDays(int days) {
	TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);

	Calendar calendar = Calendar.getInstance(tz);
	calendar.setTime(value);
	calendar.add(Calendar.DAY_OF_MONTH, days);
	Date result_Date = calendar.getTime();
	return new DFDate(this.getField_Name(), this.getField_Title(), result_Date);
    }
    
    
    
    public DFDate addMonths(int months) {
	TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);

	Calendar calendar = Calendar.getInstance(tz);
	calendar.setTime(value);
	calendar.add(Calendar.MONTH, months);
	Date result_Date = calendar.getTime();
	return new DFDate(this.getField_Name(), this.getField_Title(), result_Date);
    }
  
    
    
    public DFDate addYears(int years) {
	TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);

	Calendar calendar = Calendar.getInstance(tz);
	calendar.setTime(value);
	calendar.add(Calendar.YEAR, years);
	Date result_Date = calendar.getTime();
	return new DFDate(this.getField_Name(), this.getField_Title(), result_Date);
    }

    
    
}
