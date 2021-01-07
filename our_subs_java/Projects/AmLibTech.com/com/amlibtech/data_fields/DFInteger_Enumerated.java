/*
 * DFInteger_Enumerated.java
 *
 * Created on June 27, 2006, 8:02 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import java.text.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */

/**
 * DFInteger_Enumerated is a Data Field that contains an Integer Class as it's value.
 * Allowed values from a list
 * Data Fields contain additional information to deal with SQL databases and HTML forms.
 *
 * @author  dgleeson
 */
public class DFInteger_Enumerated extends DFBase {
    private static String sql_Type = "int";
    
    private Integer value;
    
    private Integer[]  enumerated_Values;
    private int length;
    
    /** Creates a new instance of DFInteger_Enumerated */
    
    public DFInteger_Enumerated(String field_Name, String field_Title, Integer[] enumerated_Values) throws DFException {
        super(field_Name, field_Title);
        if(enumerated_Values.length <1){
            throw new DFException("DFString_Enumerated: constructor(): field_Name :" + field_Name + ": You need at least 2 enumerated values.");
        }
        
        this.enumerated_Values = new Integer[enumerated_Values.length];
        for(int i=0; i<enumerated_Values.length; i++){
            this.enumerated_Values[i] = enumerated_Values[i];
        }
        
        Integer lowest=this.enumerated_Values[0];
        Integer highest=this.enumerated_Values[0];
        
        for(int i=1;i<this.enumerated_Values.length;i++){
            Integer n = this.enumerated_Values[i];
            lowest = lowest.compareTo(n)<0?lowest:n;
            highest = highest.compareTo(n)>0?highest:n;
        }
        int a = MathPlus.length_Of_Integer(highest);
        int b = MathPlus.length_Of_Integer(lowest);
        this.length = Math.max(a,b)+1;
        
        this.value=enumerated_Values[0];
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
     * Getter for property enumerated_Values.
     * @return Value of property enumerated_Values.
     */
    public java.lang.Integer[] getEnumerated_Values() {
        return this.enumerated_Values;
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
    public void setValue(Integer passed_Value) throws DFException {
        for(int i=0;i< this.enumerated_Values.length;i++){
            if(passed_Value.equals(this.enumerated_Values[i])){
                this.value = passed_Value;
                setRequest_Value();
                return;
            }
        }
        throw new DFException(this.field_Title + " answer must be one of the preset values.");
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
        return ("`" + this.field_Name + "` " + sql_Type + "(" + length + ") default '0',\n");
    }
    
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + length + ") NOT NULL default '" + default_Value + "',\n");
    }
    
    
    public String toStringFormatted(){
        NumberFormat    nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(true);
        
        return(nf.format((double)this.value.intValue()));
    } 
    
    public String toString(){
        return(this.value.toString());
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
                out.append("\t\t<option value='" + enumerated_Values[i] + "' selected>" + enumerated_Values[i] + "</option>\n");
            } else {
                out.append("\t\t<option value='" + enumerated_Values[i] + "'>" + enumerated_Values[i] + "</option>\n");
            }
        }
        
        out.append("\t</select>\n");
        out.append("<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n");
        
        return out.toString();
    }
    
    public String[] toHTML_Input_Radio(String prefix, String web_Root, String form_Name, String suffix) {
        LinkedList list = new LinkedList();
        
        // out.append("\t<select name='" + prefix + this.field_Name + suffix + "' id='" + prefix + this.field_Name + suffix + "'>\n");
        
        for(int i = 0; i<enumerated_Values.length; i++){
            if(this.value.equals(enumerated_Values[i])){
                list.add("<input type=radio name='" + prefix + this.field_Name + suffix + "' value='" + enumerated_Values[i] + "' checked >" + enumerated_Values[i] + "</input>");
                
            } else {
                list.add("<input type=radio name='" + prefix + this.field_Name + suffix + "' value='" + enumerated_Values[i] + "'>" + enumerated_Values[i] + "</input>");
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
    
    
    public boolean same_Field_Layout(DFBase dFBase){
        if (dFBase instanceof DFInteger_Enumerated){
            DFInteger_Enumerated temp = (DFInteger_Enumerated) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            
            if(this.length != temp.length)
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
            
            
            if(this.length != temp.getMax_Length()){
                return false;
            }
            
            return true;
        }else{
            // wrong type
            return false;
        }
    }
    
   
}
