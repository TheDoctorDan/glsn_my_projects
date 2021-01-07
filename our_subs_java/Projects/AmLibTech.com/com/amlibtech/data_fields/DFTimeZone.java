/*
 * DFTimeZone.java
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
public class DFTimeZone extends DFBase {
    private static String sql_Type = "varchar";

    private TimeZone value;
    String  ID_Str;
    static int max_Length=50;
    
    /** Creates a new instance of DFTimeZone */
    public DFTimeZone(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title);        
        this.value=TimeZone.getDefault();
        this.ID_Str = this.value.getID();
        this.request_Value = this.ID_Str;
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
    public TimeZone getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(TimeZone value) throws DFException {
        this.value = value;
        this.ID_Str = this.value.getID();
        this.request_Value = this.ID_Str;
        this.request_Error ="";
    }
    
    
     /**
     * Getter for property ID_Str.
     * @return Value of property ID_Str.
     */
    public String getID_Str() {
        return ID_Str;
    }    
    
    /**
     * Setter for property ID_Str.
     * @param ID_Str New value of property ID_Str.
     */
    public void setID_Str(String ID_Str) throws DFException{
        int length;
        length = ID_Str.length();
        
        boolean found=false;
        String[] availableIDs = TimeZone.getAvailableIDs();
        
        for(int i=0; i< availableIDs.length; i++){
            if(ID_Str.equals(availableIDs[i])){
                found=true;
                // end loop
                i=availableIDs.length;
            }
        }
        if(!found){
            throw new DFException(this.field_Title + ": Answer :" + ID_Str + ": is not a known TimeZone ID.");
        }
       
        if(this.max_Length !=0)
            if(length > this.max_Length){
                throw new DFException(this.field_Title + " Maximum Length is :" + this.max_Length + ".");
            }
                
        this.ID_Str = ID_Str;

        this.value = TimeZone.getTimeZone(ID_Str);
        this.request_Value = this.ID_Str;
    }
    
    
    
    
    /**
     * Getter for property max_Length.
     * @return Value of property max_Length.
     */
    public int getMax_Length() {
        return max_Length;
    }
     
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix)  {
        
        String parameter_Value = request.getParameter(prefix+this.field_Name+suffix);
        this.request_Value=parameter_Value;

        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = this.ID_Str;
        }
        if(parameter_Value==null)
            parameter_Value="";

        try {
            setID_Str(parameter_Value);
        }
        catch(DFException dfe){
            this.request_Error = dfe.getMessage();
            return REQUEST_ERRCODE_WRONG;
        }
        return REQUEST_ERRCODE_NONE;

    }
    
       
    public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException {
        try {
            this.ID_Str = resultSet.getString(this.field_Name);
        }
        catch(java.sql.SQLException sqle){
             throw new DFException(sqle.getMessage());
        }
        this.value = TimeZone.getTimeZone(this.ID_Str);
        this.request_Value = this.ID_Str;
    }
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
         try {
             preparedStatement.setString(count, this.ID_Str);
         }
         catch(java.sql.SQLException sqle){
             throw new DFException(sqle.getMessage());
        }
         return preparedStatement;
    }
    
    public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.max_Length + ") NOT NULL default '',\n");
    }
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.max_Length + ") NOT NULL default '" + default_Value + "',\n");
    }
    
    public String toString(){
        return this.ID_Str;
    }
       
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int not_Used_size, String option, String suffix) {
        StringBuffer out = new StringBuffer();
	String[] choices = TimeZone.getAvailableIDs();
    	//String[] choices = Rising_Stock.TimeZone_Name_Enum_Values;
        
        String out_Option;
        if(option==null)
            out_Option="";
        else
            out_Option=option;
        
        out.append("\t<select " +
        "name='" + prefix + this.field_Name + suffix + "' " +
        "id='" + prefix + this.field_Name + suffix + "' " +
        out_Option + " >\n");
	for(int i = 0; i<choices.length; i++){
            if(this.ID_Str.equals(choices[i])){
                out.append("\t\t<option value='" + StringPlus.html_Encode(choices[i]) + "' selected>" + StringPlus.html_Encode(choices[i]) + "</option>\n");
            } else {
                out.append("\t\t<option value='" + StringPlus.html_Encode(choices[i]) + "'>" + StringPlus.html_Encode(choices[i]) + "</option>\n");
            }
	}
        out.append("\t</select>\n");
        out.append("<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n");

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
        if (dFBase instanceof DFTimeZone){

            DFTimeZone temp = (DFTimeZone) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            if(!this.ID_Str.equals(temp.ID_Str))
                return false;
            
            if(this.max_Length != temp.max_Length)
                return false;
            return true;
        }else if(dFBase instanceof DFString){
            DFString temp = (DFString) dFBase;
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
    
    
}
