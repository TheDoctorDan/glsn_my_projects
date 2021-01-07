/*
 * DFBoolean.java
 *
 * Created on March 5, 2006, 10:14 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class DFBoolean extends DFBase {
    private static String sql_Type = "bool";
    
    private Boolean value;
    
    /** Creates a new instance of DFBoolean */
    public DFBoolean(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title);
        
        this.value = new Boolean(false);
        this.setRequest_Value();
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
    public Boolean getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(Boolean value) {
        this.value = value;
        this.setRequest_Value();
    }
    
    public void setValue(boolean value) {
        this.value = new Boolean(value);
        this.setRequest_Value();
    }
    
    private void setRequest_Value(){
        if(this.value.booleanValue())
            this.request_Value ="yes";
        else
            this.request_Value ="no";
        this.request_Error ="";
    }
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        Boolean b_Value;
        
        String parameter_Value = request.getParameter(prefix+field_Name+suffix);
        this.request_Value=parameter_Value;
        this.request_Error="";
        
        if(parameter_Value == null){
            // no parameter passed
            //b_Value = new Boolean(false);
            b_Value = this.value;
        }else{
            parameter_Value = parameter_Value.trim();
            if(parameter_Value.equalsIgnoreCase("true") ||
            parameter_Value.equalsIgnoreCase("yes") ||
            parameter_Value.equalsIgnoreCase("+") ||
            parameter_Value.equalsIgnoreCase("y") ||
            parameter_Value.equalsIgnoreCase("1") ){
                b_Value = new Boolean(true);
            }else if(parameter_Value.equalsIgnoreCase("false") ||
            parameter_Value.equalsIgnoreCase("no") ||
            parameter_Value.equalsIgnoreCase("-") ||
            parameter_Value.equalsIgnoreCase("n") ||
            parameter_Value.equalsIgnoreCase("0") ){
                b_Value = new Boolean(false);
            }else{
                this.request_Error = this.field_Title + " Answer :" + parameter_Value + ": is not a valid boolean.";
                return REQUEST_ERRCODE_WRONG;
            }
        }
        setValue(b_Value);
        return REQUEST_ERRCODE_NONE;
    }
    
    
    
    public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException {
        try {
            this.value = new Boolean(resultSet.getBoolean(this.field_Name));
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        this.setRequest_Value();
    }
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
        try {
            preparedStatement.setBoolean(count, this.value.booleanValue());
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }         return preparedStatement;
    }
    
    public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + sql_Type + " NOT NULL default '0',\n");
    }
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + " NOT NULL default '" + default_Value + "',\n");
    }
    
    
    
    public String toString(){
        return (this.value.toString());
    }
    
    
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int size, String option, String suffix) {
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
        if(this.value.booleanValue()){
            out.append("\t\t<option value='yes' selected>yes</option>\n");
            out.append("\t\t<option value='no'>no</option>\n");
        }else{
            out.append("\t\t<option value='yes'>yes</option>\n");
            out.append("\t\t<option value='no' selected>no</option>\n");
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
        if (dFBase instanceof DFBoolean){
            DFBoolean temp = (DFBoolean) dFBase;
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
    
    
    
    public String getSql_String(){
        if(this.value.booleanValue())
            return SQLUtil.encode("1");
        else
            return SQLUtil.encode("0");
    }
    
//    public String toWhere_Clause(){
//        if(this.value.booleanValue())
//            return this.field_Name + " = '1' ";
//        else
//            return this.field_Name + " = '0' ";
//    }
    
    

    
}
