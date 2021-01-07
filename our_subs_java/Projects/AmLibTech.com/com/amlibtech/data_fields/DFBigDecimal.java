/*
 * DFBigDecimal.java
 *
 * Created on March 4, 2006, 11:19 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import java.math.*;
import java.text.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class DFBigDecimal extends DFBase {
    private static String sql_Type = "double";
    
    private BigDecimal value;
    
    private int left_Of_Decimal;
    private int right_Of_Decimal;
    private int rounding_Method;
    
    private BigDecimal min_Value;
    private BigDecimal max_Value;
    
    /** Creates a new instance of DFBigDecimal */
    
    
    public DFBigDecimal(String field_Name, String field_Title, int left_Of_Decimal, int right_Of_Decimal, int rounding_Method) throws DFException {
        super(field_Name, field_Title);
        
        this.left_Of_Decimal = left_Of_Decimal;
        this.right_Of_Decimal = right_Of_Decimal;
        this.rounding_Method = rounding_Method;
        
        this.value=new BigDecimal(0).setScale(right_Of_Decimal, rounding_Method);
        
        this.max_Value=new  BigDecimal(java.lang.Math.pow(10., (double)left_Of_Decimal) +
        (1- java.lang.Math.pow(10., (double)-right_Of_Decimal))).setScale(right_Of_Decimal, rounding_Method);
        
        this.min_Value= this.max_Value.negate();
        
        setRequest_Value();
    }
    
    public DFBigDecimal(String field_Name, String field_Title, int left_Of_Decimal, int right_Of_Decimal) throws DFException {
        this(field_Name, field_Title, left_Of_Decimal, right_Of_Decimal, BigDecimal.ROUND_HALF_UP);
    }
    
    public DFBigDecimal(String field_Name, String field_Title, int left_Of_Decimal, int right_Of_Decimal, BigDecimal min_Value, BigDecimal max_Value, int rounding_Method) throws DFException {
        this(field_Name, field_Title, left_Of_Decimal, right_Of_Decimal, rounding_Method);
        
        this.min_Value = min_Value;
        this.max_Value = max_Value;
        
    }
    
    public DFBigDecimal(String field_Name, String field_Title, int left_Of_Decimal, int right_Of_Decimal, BigDecimal min_Value, BigDecimal max_Value) throws DFException {
        this(field_Name, field_Title, left_Of_Decimal, right_Of_Decimal, BigDecimal.ROUND_HALF_UP);
        
        this.value=new BigDecimal(0).setScale(right_Of_Decimal, rounding_Method);
        
        this.min_Value = min_Value;
        this.max_Value = max_Value;
        
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
    public BigDecimal getMin_Value() {
        return min_Value;
    }
    
    
    
    /**
     * Getter for property max_Value.
     * @return Value of property max_Value.
     */
    public BigDecimal getMax_Value() {
        return max_Value;
    }
    
    
    
    
    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public BigDecimal getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(BigDecimal value) throws DFException {
        
        BigDecimal temp_Value = value.setScale(this.right_Of_Decimal, this.rounding_Method);
        
        if(min_Value != null){
            if(temp_Value.compareTo(min_Value) < 0){
                throw new DFException(this.field_Title + " answer must be at least " + min_Value + ".");
            }
        }
        
        if(max_Value != null){
            if(temp_Value.compareTo(max_Value) > 0){
                throw new DFException(this.field_Title + " answer must not exceed " + max_Value + ".");
            }
        }
        
        this.value = temp_Value;
        setRequest_Value();
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(double value) throws DFException {
        setValue(new BigDecimal(value).setScale(this.right_Of_Decimal, this.rounding_Method));
    }
    
    private void setRequest_Value(){
        this.request_Value = toString();
        this.request_Error ="";
    }
    
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        
        BigDecimal  in_Value;
        
        String parameter_Value = request.getParameter(prefix+field_Name+suffix);
        this.request_Value=parameter_Value;
        this.request_Error="";
        
        if(parameter_Value == null){
            // no parameter passed, use default.
            in_Value = this.value;
        }else{
            parameter_Value = parameter_Value.trim();
            if(parameter_Value.length()!=0){
                char    c;
                for(int i=0;i<parameter_Value.length();i++){
                    c=parameter_Value.charAt(i);
                    if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER &&   c != '-' && c != '.'){
                        this.request_Error = this.field_Title + " answer is not valid. Only digits or a minus sign or a decimal point are allowed.";
                        return REQUEST_ERRCODE_WRONG;
                    }
                }
                
                
                
                if(!parameter_Value.matches("[-]*[0-9]*.*[0-9]*")){
                    // error
                    this.request_Error = this.field_Title + " answer is not valid. I can not determine a pattern that looks like an decimal number.";
                    return REQUEST_ERRCODE_WRONG;
                }
                
                
                try{
                    in_Value = new BigDecimal(parameter_Value).setScale(this.left_Of_Decimal, this.rounding_Method);
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
                // zero length parameter passed, use default.
                this.request_Error = this.field_Title + " answer can not be left blank.  A number is required.";
                return REQUEST_ERRCODE_WRONG;
            }
        }
        return REQUEST_ERRCODE_NONE;
    }
    
    
    public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException {
        try {
            this.value = resultSet.getBigDecimal(this.field_Name);
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        setRequest_Value();
    }
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
        try {
            preparedStatement.setBigDecimal(count, this.value);
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        return preparedStatement;
    }
    
    public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + (this.left_Of_Decimal+this.right_Of_Decimal) + ", " + this.right_Of_Decimal + ") NOT NULL default '0',\n");
    }
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + (this.left_Of_Decimal+this.right_Of_Decimal) + ", " + this.right_Of_Decimal + ") NOT NULL default '" + default_Value + "',\n");
    }
    
    
    public String toStringFormatted(){
        NumberFormat    nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(this.right_Of_Decimal);
        nf.setMinimumFractionDigits(this.right_Of_Decimal);
        nf.setGroupingUsed(true);
        
        return(nf.format(this.value.doubleValue()));
    }
    
    public String toHTML() {
        return (StringPlus.html_Encode(this.toStringFormatted()));
    }
    
    public String toString(){
        return(this.value.toString());
    }
    
    
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int size, String option, String suffix) {
        int len = this.left_Of_Decimal + 1 + this.right_Of_Decimal;
        if(size==0)
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
        out_Option + " ></input>" +
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
        if (dFBase instanceof DFBigDecimal){
            DFBigDecimal temp = (DFBigDecimal) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            if(this.left_Of_Decimal != temp.left_Of_Decimal)
                return false;
            if(this.right_Of_Decimal != temp.right_Of_Decimal)
                return false;
            if(this.rounding_Method != temp.rounding_Method)
                return false;
            
            return true;
            
        }else{
            // wrong type
            return false;
        }
    }
    
    
    /**
     * Getter for property rounding_Method.
     * @return Value of property rounding_Method.
     */
    public int getRounding_Method() {
        return rounding_Method;
    }
    
    
    /**
     * Getter for property left_Of_Decimal.
     * @return Value of property left_Of_Decimal.
     */
    public int getLeft_Of_Decimal() {
        return left_Of_Decimal;
    }
    
    
    
    /**
     * Getter for property right_Of_Decimal.
     * @return Value of property right_Of_Decimal.
     */
    public int getRight_Of_Decimal() {
        return right_Of_Decimal;
    }
    
   
    
    
    
    
    public DFBigDecimal addEqual(int qty){
        this.value = this.value.add(new BigDecimal((double)qty));
        return this;
    }
    
    public DFBigDecimal addEqual(double qty){
        this.value = this.value.add(new BigDecimal(qty));
        return this;
    }
    
    public DFBigDecimal addEqual(BigDecimal qty){
        this.value = this.value.add(qty);
        return this;
    }
    public DFBigDecimal addEqual(DFBigDecimal qty){
        this.value = this.value.add(qty.getValue());
        return this;
    }
    
    public DFBigDecimal addOnly(int qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.add(new BigDecimal((double)qty));
        return result;
    }
    
    public DFBigDecimal addOnly(double qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.add(new BigDecimal(qty));
        return result;
    }
    
    public DFBigDecimal addOnly(BigDecimal qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.add(qty);
        return result;
    }
    public DFBigDecimal addOnly(DFBigDecimal qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.add(qty.getValue());
        return result;
    }
    
    
    
    
    
    
    
    
    
    public DFBigDecimal subtractEqual(int qty){
        this.value = this.value.subtract(new BigDecimal((double)qty));
        return this;
    }
    
    public DFBigDecimal subtractEqual(double qty){
        this.value = this.value.subtract(new BigDecimal(qty));
        return this;
    }
    
    public DFBigDecimal subtractEqual(BigDecimal qty){
        this.value = this.value.subtract(qty);
        return this;
    }
    public DFBigDecimal subtractEqual(DFBigDecimal qty){
        this.value = this.value.subtract(qty.getValue());
        return this;
    }
     
    public DFBigDecimal subtractOnly(int qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.subtract(new BigDecimal((double)qty));
        return result;
    }
    
    public DFBigDecimal subtractOnly(double qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.subtract(new BigDecimal(qty));
        return result;
    }
    
    public DFBigDecimal subtractOnly(BigDecimal qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.subtract(qty);
        return result;
    }
    public DFBigDecimal subtractOnly(DFBigDecimal qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.subtract(qty.getValue());
        return result;
    }
    
    
    
    
    
    
    public DFBigDecimal multiplyEqual(int qty){
        this.value = this.value.multiply(new BigDecimal((double)qty));
        return this;
    }
    
    public DFBigDecimal multiplyEqual(double qty){
        this.value = this.value.multiply(new BigDecimal(qty));
        return this;
    }
    
    public DFBigDecimal multiplyEqual(BigDecimal qty){
        this.value = this.value.multiply(qty);
        return this;
    }
    public DFBigDecimal multiplyEqual(DFBigDecimal qty){
        this.value = this.value.multiply(qty.getValue());
        return this;
    }
    
    public DFBigDecimal multiplyOnly(int qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.multiply(new BigDecimal((double)qty));
        return result;
    }
    
    public DFBigDecimal multiplyOnly(double qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.multiply(new BigDecimal(qty));
        return result;
    }
    
    public DFBigDecimal multiplyOnly(BigDecimal qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.multiply(qty);
        return result;
    }
    public DFBigDecimal multiplyOnly(DFBigDecimal qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.multiply(qty.getValue());
        return result;
    }
    
    
    
    
    
    
    
    
    public DFBigDecimal divideEqual(int qty){
        this.value = this.value.divide(new BigDecimal((double)qty), this.rounding_Method);
        return this;
    }
    
    public DFBigDecimal divideEqual(double qty){
        this.value = this.value.divide(new BigDecimal(qty), this.rounding_Method);
        return this;
    }
    
    public DFBigDecimal divideEqual(BigDecimal qty){
        this.value = this.value.divide(qty, this.rounding_Method);
        return this;
    }
    public DFBigDecimal divideEqual(DFBigDecimal qty){
        this.value = this.value.divide(qty.getValue(), this.rounding_Method);
        return this;
    }
    
    public DFBigDecimal divideOnly(int qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.divide(new BigDecimal((double)qty), this.rounding_Method);
        return result;
    }
    
    public DFBigDecimal divideOnly(double qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.divide(new BigDecimal(qty), this.rounding_Method);
        return result;
    }
    
    public DFBigDecimal divideOnly(BigDecimal qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.divide(qty, this.rounding_Method);
        return result;
    }
    public DFBigDecimal divideOnly(DFBigDecimal qty) throws DFException {
        DFBigDecimal result = new DFBigDecimal(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal, this.rounding_Method);
        result.value = this.value.divide(qty.getValue(), this.rounding_Method);
        return result;
    }
    
    
    
}
