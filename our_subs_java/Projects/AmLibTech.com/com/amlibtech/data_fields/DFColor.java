/*
 * DFColor.java
 *
 * Created on March 4, 2006, 11:19 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class DFColor extends DFBase {
    private static String sql_Type = "uint";
    
    private Integer value;
    
    private static int min_Value=0;
    private static int max_Value= 0xFFFFFF;
    
    /** Creates a new instance of DFColor */
    public DFColor(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title);
        this.value=new Integer(0);
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
     * Getter for property min_Value.
     * @return Value of property min_Value.
     */
    public int getMin_Value() {
        return min_Value;
    }    
    
   
    
    /**
     * Getter for property max_Value.
     * @return Value of property max_Value.
     */
    public int getMax_Value() {
        return max_Value;
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
        
        if(value.intValue() < min_Value){
            throw new DFException(this.field_Title + " answer must be at least " + min_Value + ".");
        }

        if(value.intValue() > max_Value){
            throw new DFException(this.field_Title + " answer must not exceed " + max_Value + ".");
        }
        
        this.value = value;     
        setRequest_Value();
    }
       
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(String value) throws DFException {
        
        Integer temp_Value;
        
        try{
            temp_Value =  Integer.decode(value);
        }
        catch(NumberFormatException nfe){
            // error
            throw new DFException(this.field_Title + " answer is not valid. Number Format Error :" + nfe.getMessage() +".");
        }
        this.setValue(temp_Value);
    }
    
   
    private void setRequest_Value(){
        this.request_Value = "#" + StringPlus.left_Fill(Integer.toHexString(this.value.intValue()), 6,'0');
        this.request_Error ="";
    }
   
    
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        
        Integer  in_Value;

        String parameter_Value = request.getParameter(prefix+field_Name+suffix);
        this.request_Value=parameter_Value;
        this.request_Error ="";

        if(parameter_Value == null){
            // no parameter passed, use default.
            in_Value = value;
        }else{
            parameter_Value = parameter_Value.trim();

            if(parameter_Value.length()!=0){
                char    c;
                c=parameter_Value.charAt(0);
                if(c!= '#'){
                        this.request_Error = this.field_Title + " answer is not valid. Colors must start with #.";
                        return REQUEST_ERRCODE_WRONG;
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
                        this.request_Error = this.field_Title + " answer is not valid. Only Hex digits (0-9, A-F) are allowed.";
                        return REQUEST_ERRCODE_WRONG;
                    }
                }

                try {
                    setValue(parameter_Value);
                }
                catch(DFException dfe){
                    this.request_Error = dfe.getMessage();
                    return REQUEST_ERRCODE_WRONG;
                }
                
                return REQUEST_ERRCODE_NONE;

            }else{
                this.request_Error = this.field_Title + " answer can not be left blank.  A number is required.";
                return REQUEST_ERRCODE_WRONG;
            }
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
   
    public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + /*sql_Type*/"int" + "(" + this.length() + ") unsigned,\n");
    }
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + /*sql_Type*/"int" + "(" + this.length() + ") unsigned NOT NULL default '" + default_Value + "',\n");
    }
    
    
    
    public String toString(){
        return "#" + StringPlus.left_Fill(Integer.toHexString(this.value.intValue()), 6,'0');
    }
    
    
     
    
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int not_Used_Size, String not_Used_Option, String suffix) {
        
        
        StringBuffer out = new StringBuffer();

        out.append("\t<input " +
        "name='" + prefix + this.field_Name + suffix + "' " +
        "id='" + prefix + this.field_Name + suffix + "' " +
        "type='text' " +
        "value='" + StringPlus.html_Encode(this.request_Value) + "' " +
        "size='8' " +
        "maxlength='8' " +
        "onChange=\"oldColor=this.value;setColor(this.value);\">\n");
                
        out.append("<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n");

        out.append("\t\t<a href=\"javascript:color_selector('document." + form_Name + "." + prefix + this.field_Name + suffix + "', document." + form_Name + "." + prefix + this.field_Name + suffix + ".value, '" + web_Root + "');\">\n");
        
        out.append("\t\t<img src=\"" + web_Root + "/scripts/color_selector/color.png\" width='16' height='16' border='0' alt='Click Here to Pick the color'></a>\n");

        return out.toString();
    }
    
     /**
     * Getter for property sql_Type.
     * @return Value of property sql_Type.
     */
    public java.lang.String getSql_Type() {
        return sql_Type;
    }    
    
    
    private int length(){
        int a = MathPlus.length_Of_Integer(max_Value);
        int b = MathPlus.length_Of_Integer(min_Value);
        return Math.max(a,b)+1;
    }
    
    public boolean same_Field_Layout(DFBase dFBase){
        if (dFBase instanceof DFColor){
            DFColor temp = (DFColor) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
        
            if(this.length() != temp.length())
                return false;
            
            return true;
        }else{
            // wrong type
            return false;
        }
    }
}
