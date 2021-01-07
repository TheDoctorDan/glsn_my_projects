/*
 * DFInteger.java
 *
 * Created on March 4, 2006, 11:19 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import java.text.*;
import javax.servlet.http.*;

/**
 * DFInteger is a Data Field that contains an Integer Class as it's value.
 *
 * Data Fields contain additional information to deal with SQL databases and HTML forms.
 *
 * @author  dgleeson
 */
public class DFInteger extends DFBase {
    private static String sql_Type = "int";
    
    Integer value;
    
    private Integer min_Value;
    private Integer max_Value;
    private int max_Length;
    
    /** Creates a new instance of DFInteger.
     *
     * @param field_Name is the sql field name and the html form field name.
     * @param field_Title is the Title to be used on html forms and tables.
     * @param max_Length is the maximum length of the sql data, or text output.
     * @throws DFException if arguments are invalid or non-sense.
     */    
    public DFInteger(String field_Name, String field_Title, int max_Length) throws DFException {
        super(field_Name, field_Title);
        this.max_Length = max_Length;
        this.min_Value=null;
        this.max_Value=null;
        this.value = new Integer(0);
        setRequest_Value();
    }
     
    /**
     * Creates a new instance of DFInteger.
     * @param field_Name is the sql field name and the html form field name.
     * @param field_Title is the Title to be used on html forms and tables.
     * @param max_Length is the maximum length of the sql data, or text output.
     * @param min_Value is the smallest value allowed.
     * @param max_Value is the largest value allowed.
     * @throws DFException if arguments are invalid or non-sense.
     */
    public DFInteger(String field_Name, String field_Title, int max_Length, Integer min_Value, Integer max_Value)
    throws DFException {
        this(field_Name, field_Title, max_Length);

        setMin_Value(min_Value);
        setMax_Value(max_Value);
    }
    
    public DFInteger(String field_Name, String field_Title, int max_Length, int min_Value, int max_Value) throws DFException {
        this(field_Name, field_Title, max_Length);
                
        setMin_Value(new Integer(min_Value));
        setMax_Value(new Integer(max_Value));
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
    public Integer getMin_Value() {
        return min_Value;
    }    
    
    
    /**
     * Getter for property max_Value.
     * @return Value of property max_Value.
     */
    public Integer getMax_Value() {
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
        
         if(min_Value != null){
            if(value.compareTo(min_Value) < 0){
                throw new DFException(this.field_Title + " answer must be at least " + min_Value + ".");
            }
        }

        if(max_Value != null){
            if(value.compareTo(max_Value) > 0){
                throw new DFException(this.field_Title + " answer must not exceed " + max_Value + ".");
            }
        }
        
        this.value = value;     
        setRequest_Value();
    }
    
     
    private void setRequest_Value(){
        this.request_Value = toString();
        this.request_Error ="";
    }

   
    
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        
        Integer  in_Value;

        String parameter_Value = request.getParameter(prefix + field_Name + suffix);
        this.request_Value=parameter_Value;
        this.request_Error ="";

        if(parameter_Value == null){
            // no parameter passed, use default.
            in_Value = this.value;
        }else{
            parameter_Value = parameter_Value.trim();

            if(parameter_Value.length()!=0){
                char    c;
                for(int i=0;i<parameter_Value.length();i++){
                    c=parameter_Value.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&   c != '-'){
                        this.request_Error = this.field_Title + " answer is not valid. Only digits or a minus sign are allowed.";
                        return REQUEST_ERRCODE_WRONG;
                    }
                }

                if(!parameter_Value.matches("[-]*[0-9]+")){
                    // error
                    this.request_Error = this.field_Title + " answer is not valid. I can not determine a pattern that looks like an integer.";
                    return REQUEST_ERRCODE_WRONG;
                }

                try{
                    in_Value = new Integer(parameter_Value);
                }
                catch(NumberFormatException nfe){
                    // error
                    this.request_Error = this.field_Title + " answer is not valid. Number Format Error :" + nfe.getMessage() +".";
                    return REQUEST_ERRCODE_WRONG;
                }

                try {
                    setValue(in_Value);
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
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.max_Length + ") NOT NULL default '0',\n");
    }
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.max_Length + ") NOT NULL default '" + default_Value + "',\n");
    }
    
    
    public String toStringFormatted(){
        NumberFormat    nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(true);
        
        return(nf.format((double)this.value.intValue()));
    } 
    
    public String toHTML() {
        return (StringPlus.html_Encode(this.toStringFormatted()));
    }
    
    public String toString(){
        return(this.value.toString());
    }
    
    
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int size, String option, String suffix) {
        int len = this.max_Length;
        if(size<=0 || size>this.max_Length)
            size=len;
	if(size>60)size=60;

        String out_Option;
        if(option==null)
            out_Option="";
        else
            out_Option=option;
        
        return ("\t<input " +
        "name='" + prefix + this.field_Name + suffix + "' " +
        "id='" + prefix + this.field_Name + suffix + "' " +
        "type='text' " +
        "value='" + StringPlus.html_Encode(this.request_Value) + "' " +
        "size='" + size + "' " +
        "maxlength='" + len + "' " +
        out_Option + " >" +
        "<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n");
    }
    
     /**
     * Getter for property sql_Type.
     * @return Value of property sql_Type.
     */
    public java.lang.String getSql_Type() {
        return sql_Type;
    }    
    
    

    public boolean same_Field_Layout(DFBase dFBase){
        if (dFBase instanceof DFInteger){
            DFInteger temp = (DFInteger) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
        
            if(this.max_Length != temp.max_Length)
                return false;
            
            return true;
        }else{
            // wrong type
            return dFBase.same_Field_Layout(this);
        }
    }
    
    /**
     * Setter for property min_Value.
     * @param min_Value New value of property min_Value.
     * @throws DFException if min_Value to long for max_Length;
     */
    private void setMin_Value(Integer min_Value) throws DFException {
        int len = MathPlus.length_Of_Integer(min_Value);
        if(len>this.max_Length)
            throw new DFException("DFInteger.setMinValue(): failed because the length:"+len+":  of value :" + min_Value + ": is longer than max_Length :" + max_Length + ":.");
        this.min_Value = min_Value;
    }
    
    /**
     * Setter for property max_Value.
     * @param max_Value New value of property max_Value.
     * @throws DFException if max_Value to long for max_Length;
     */
    private void setMax_Value(Integer max_Value) throws DFException {
        int len = MathPlus.length_Of_Integer(max_Value);
        if(len>this.max_Length)
            throw new DFException("DFInteger.setMaxValue(): failed because the length:"+len+":  of value :" + max_Value + ": is longer than max_Length :" + max_Length + ":.");
        this.max_Value = max_Value;
    }
    
    /**
     * Getter for property max_Length.
     * @return Value of property max_Length.
     */
    public int getMax_Length() {
        return max_Length;
    }
    
    
    
    
    public DFInteger addEqual(int qty){
        this.value = new Integer(this.value.intValue() + qty);
        return this;
    }
    
    public DFInteger addEqual(Integer qty){
        this.value = new Integer(this.value.intValue() + qty.intValue());
        return this;
    }
    
    public DFInteger addEqual(DFInteger qty){
        this.value = new Integer(this.value.intValue() + qty.getValue().intValue());
        return this;
    }
    
    
    public DFInteger addOnly(int qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() + qty);
        return result;
    }
    
    public DFInteger addOnly(Integer qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() + qty.intValue());
        return result;
    }
    
    public DFInteger addOnly(DFInteger qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() + qty.getValue().intValue());
        return result;
    }
    
    
    
    
    
    public DFInteger subtractEqual(int qty){
        this.value = new Integer(this.value.intValue() - qty);
        return this;
    }
    
    public DFInteger subtractEqual(Integer qty){
        this.value = new Integer(this.value.intValue() - qty.intValue());
        return this;
    }
    
    public DFInteger subtractEqual(DFInteger qty){
        this.value = new Integer(this.value.intValue() - qty.getValue().intValue());
        return this;
    }
    
    public DFInteger subtractOnly(int qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() - qty);
        return result;
    }
    
    public DFInteger subtractOnly(Integer qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() - qty.intValue());
        return result;
    }
    
    public DFInteger subtractOnly(DFInteger qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() - qty.getValue().intValue());
        return result;
    }
    
    
    
    
    
    public DFInteger multiplyEqual(int qty){
        this.value = new Integer(this.value.intValue() * qty);
        return this;
    }
    
    public DFInteger multiplyEqual(Integer qty){
        this.value = new Integer(this.value.intValue() * qty.intValue());
        return this;
    }
    
    public DFInteger multiplyEqual(DFInteger qty){
        this.value = new Integer(this.value.intValue() * qty.getValue().intValue());
        return this;
    }
    
    public DFInteger multiplyOnly(int qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() * qty);
        return result;
    }
    
    public DFInteger multiplyOnly(Integer qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() * qty.intValue());
        return result;
    }
    
    public DFInteger multiplyOnly(DFInteger qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() * qty.getValue().intValue());
        return result;
    }
    
    
    
    
    
    
    public DFInteger divideEqual(int qty){
        this.value = new Integer(this.value.intValue() / qty);
        return this;
    }
    
    public DFInteger divideEqual(Integer qty){
        this.value = new Integer(this.value.intValue() / qty.intValue());
        return this;
    }
    
    public DFInteger divideEqual(DFInteger qty){
        this.value = new Integer(this.value.intValue() / qty.getValue().intValue());
        return this;
    }
    
    public DFInteger divideOnly(int qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() / qty);
        return result;
    }
    
    public DFInteger divideOnly(Integer qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() / qty.intValue());
        return result;
    }
    
    public DFInteger divideOnly(DFInteger qty) throws DFException {
        DFInteger result = new DFInteger(this.field_Name, this.field_Title, this.max_Length);
        result.value = new Integer(this.value.intValue() / qty.getValue().intValue());
        return result;
    }
    
    
}
