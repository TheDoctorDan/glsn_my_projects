/*
 * DFDouble.java
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
public class DFDouble extends DFBase {
    private static String sql_Type = "double";
    
    private Double value;
    
    private int left_Of_Decimal;
    private int right_Of_Decimal;
    private double rounding_factor;
    
    private Double min_Value;
    private Double max_Value;
    
    /** Creates a new instance of DFInteger */
    
    
    public DFDouble(String field_Name, String field_Title, int left_Of_Decimal, int right_Of_Decimal) throws DFException {
        super(field_Name, field_Title);
                

        this.value=new Double(0);
 
        this.max_Value=new  Double(Math.pow(10., (double)left_Of_Decimal) + 
        (1- Math.pow(10., (double)-right_Of_Decimal)));
                
        this.min_Value= new Double(-this.max_Value.doubleValue());
        setRequest_Value();
    }
    
   
    
    public DFDouble(String field_Name, String field_Title, int left_Of_Decimal, int right_Of_Decimal, Double min_Value, Double max_Value) throws DFException {
        super(field_Name, field_Title);
                        
        this.value=new Double(0);

        setMin_Value(min_Value);
        setMax_Value(max_Value);
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
    public Double getMin_Value() {
        return min_Value;
    }
    
    /**
     * Setter for property min_Value.
     * @param min_Value New value of property min_Value.
     */
    public void setMin_Value(Double min_Value) {
        this.min_Value = min_Value;
    }
    
    /**
     * Getter for property max_Value.
     * @return Value of property max_Value.
     */
    public Double getMax_Value() {
        return max_Value;
    }
    
    /**
     * Setter for property max_Value.
     * @param max_Value New value of property max_Value.
     */
    public void setMax_Value(Double max_Value) {
        this.max_Value = max_Value;
    }
    
     
    
    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public Double getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(Double value) throws DFException {
        
        value = DFDouble.nround(value, right_Of_Decimal);
        
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
    
   /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(double value) throws DFException {
        setValue(new Double(value));
    }
   
    private void setRequest_Value(){
        this.request_Value = toString();
        this.request_Error ="";
    }

    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        
        Double  in_Value;

        String parameter_Value = request.getParameter(prefix + field_Name + suffix);
        this.request_Value=parameter_Value;

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
                    in_Value = new Double(parameter_Value);
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
            this.value = new Double(resultSet.getDouble(this.field_Name));
        }
        catch(java.sql.SQLException sqle){
             throw new DFException(sqle.getMessage());
        }
        setRequest_Value();
    }
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
         try {
             preparedStatement.setDouble(count, this.value.doubleValue());
         }
         catch(java.sql.SQLException sqle){
             throw new DFException(sqle.getMessage());
         }
         return preparedStatement;
    }
    
    
    
    
    
    static double  nround(double num, int right_Of_Decimal)
    {
            double  sign;
            double  num1;
            double  factor = Math.pow(10., (double)-right_Of_Decimal);
            sign = num < 0 ? -1 : 1;
            num1 = (Math.floor((sign * num / factor) + .5) * factor) * sign;
            return(num1);
    }
    
    static Double  nround(Double num, int right_Of_Decimal)
    {
            return new Double(DFDouble.nround(num.doubleValue(), right_Of_Decimal));
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
        
        return(nf.format(this.value));
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
        if (dFBase instanceof DFDouble){
            DFDouble temp = (DFDouble) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            if(this.left_Of_Decimal != temp.left_Of_Decimal)
                return false;
            if(this.right_Of_Decimal != temp.right_Of_Decimal)
                return false;
            if(this.rounding_factor != temp.rounding_factor)
                return false;
            
            return true;
        }else{
            // wrong type
            return false;
        }
    }
    
    
    
    
    public DFDouble addEqual(int qty){
        this.value = new Double(this.value.doubleValue() + qty);
        return this;
    }
    
    public DFDouble addEqual(double qty){
        this.value = new Double(this.value.doubleValue() + qty);
        return this;
    }
   
    public DFDouble addEqual(Double qty){
        this.value = new Double(this.value.doubleValue() + qty.doubleValue());
        return this;
    }
    
    public DFDouble addEqual(DFDouble qty){
        this.value = new Double(this.value.doubleValue() + qty.getValue().doubleValue());
        return this;
    }
    
    public DFDouble addOnly(int qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() + qty);
        return result;
    }
    
    public DFDouble addOnly(double qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() + qty);
        return result;
    }
   
    public DFDouble addOnly(Double qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() + qty.doubleValue());
        return result;
    }
    
    public DFDouble addOnly(DFDouble qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() + qty.getValue().doubleValue());
        return result;
    }
    
    
    
    
    
    
    
    public DFDouble subtractEqual(int qty){
        this.value = new Double(this.value.doubleValue() - qty);
        return this;
    }
    
    public DFDouble subtractEqual(double qty){
        this.value = new Double(this.value.doubleValue() - qty);
        return this;
    }
   
    public DFDouble subtractEqual(Double qty){
        this.value = new Double(this.value.doubleValue() - qty.doubleValue());
        return this;
    }
    
    public DFDouble subtractEqual(DFDouble qty){
        this.value = new Double(this.value.doubleValue() - qty.getValue().doubleValue());
        return this;
    }
    
    public DFDouble subtractOnly(int qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() - qty);
        return result;
    }
    
    public DFDouble subtractOnly(double qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() - qty);
        return result;
    }
   
    public DFDouble subtractOnly(Double qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() - qty.doubleValue());
        return result;
    }
    
    public DFDouble subtractOnly(DFDouble qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() - qty.getValue().doubleValue());
        return result;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public DFDouble multiplyEqual(int qty){
        this.value = new Double(this.value.doubleValue() * qty);
        return this;
    }
    
    public DFDouble multiplyEqual(double qty){
        this.value = new Double(this.value.doubleValue() * qty);
        return this;
    }
   
    public DFDouble multiplyEqual(Double qty){
        this.value = new Double(this.value.doubleValue() * qty.doubleValue());
        return this;
    }
    
    public DFDouble multiplyEqual(DFDouble qty){
        this.value = new Double(this.value.doubleValue() * qty.getValue().doubleValue());
        return this;
    }
    
    public DFDouble multiplyOnly(int qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() * qty);
        return result;
    }
    
    public DFDouble multiplyOnly(double qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() * qty);
        return result;
    }
   
    public DFDouble multiplyOnly(Double qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() * qty.doubleValue());
        return result;
    }
    
    public DFDouble multiplyOnly(DFDouble qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() * qty.getValue().doubleValue());
        return result;
    }
    
    
    
    
    
    
    
    
    
    
    
    public DFDouble divideEqual(int qty){
        this.value = new Double(this.value.doubleValue() / qty);
        return this;
    }
    
    public DFDouble divideEqual(double qty){
        this.value = new Double(this.value.doubleValue() / qty);
        return this;
    }
   
    public DFDouble divideEqual(Double qty){
        this.value = new Double(this.value.doubleValue() / qty.doubleValue());
        return this;
    }
    
    public DFDouble divideEqual(DFDouble qty){
        this.value = new Double(this.value.doubleValue() / qty.getValue().doubleValue());
        return this;
    }
    
    public DFDouble divideOnly(int qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() / qty);
        return result;
    }
    
    public DFDouble divideOnly(double qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() / qty);
        return result;
    }
   
    public DFDouble divideOnly(Double qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() / qty.doubleValue());
        return result;
    }
    
    public DFDouble divideOnly(DFDouble qty) throws DFException {
        DFDouble result = new DFDouble(this.field_Name, this.field_Title, this.left_Of_Decimal, this.right_Of_Decimal);
        result.value = new Double(this.value.doubleValue() / qty.getValue().doubleValue());
        return result;
    }
    
    
    
    
    
    
    
    
    
}
