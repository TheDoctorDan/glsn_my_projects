/*
 * DFBase.java
 *
 * Created on March 5, 2006, 10:57 AM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import javax.servlet.http.*;

/**
 * This is the Base class for all DF Datafields.
 *
 * Data Fields contain additional information to deal with SQL databases and HTML forms.
 *
 * It does NOT hold a value!
 * It should not be implimented!
 * It does contain values to be used in ALL other DF Classes.
 * @author  dgleeson
 */
abstract public class DFBase {

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

    /** An error code to be retured by validate_Request() */
    public static final int REQUEST_ERRCODE_NONE =0;
    /** An error code to be retured by validate_Request() */
    public static final int REQUEST_ERRCODE_WRONG =1;
    
    public static final String RED_ASTERISK_HTML = "<font color=\"#ff0000\" size=+2 >*</font>";
    
    String request_Value;
    String request_Error;
    
    String field_Name;
    String field_Title;
    
    private boolean part_Of_Primary_Key;
    
    private boolean foreign_Key;
    private String  foreign_Table_Name;
    
    
    /** Creates a new instance of DFBase */
    public DFBase(String field_Name, String field_Title) {
        this.request_Value="";
        this.request_Error="";
        
        this.field_Name=field_Name;
        this.field_Title=field_Title;
        
        this.part_Of_Primary_Key=false;
    }
    
    
    
    /**
     * Getter for property request_Value.
     * @return Value of property request_Value.
     */
    final public String getRequest_Value() {
        return request_Value;
    }
    
    /**
     * Setter for property request_Value.
     * @param request_Value New value of property request_Value.
     */
    final public void setRequest_Value(String request_Value) {
        this.request_Value = request_Value;
    }
    
    
    /**
     * Getter for property field_Name.
     * @return Value of property field_Name.
     */
    final public String getField_Name() {
        return field_Name;
    }
    
    /**
     * Setter for property field_Name.
     * @param field_Name New value of property field_Name.
     */
    final private void setField_Name(String field_Name) {
        this.field_Name = field_Name;
    }
    
    /**
     * Getter for property field_Title.
     * @return Value of property field_Title.
     */
    final public String getField_Title() {
        return field_Title;
    }
    
    /**
     * Setter for property field_Title.
     * @param field_Title New value of property field_Title.
     */
    final private void setField_Title(String field_Title) {
        this.field_Title = field_Title;
    }
    
    /**
     * Getter for property request_Error.
     * @return Value of property request_Error.
     */
    final public String getRequest_Error() {
        return request_Error;
    }
    
    /**
     * Setter for property request_Error.
     * @param request_Error New value of property request_Error.
     */
    final public void setRequest_Error(String request_Error) {
        this.request_Error = request_Error;
    }
    
    
    final public int validate_Request(HttpServletRequest request){
        return validate_Request("", request, "");
    }
    
    final public int validate_Request(String prefix, HttpServletRequest request){
        return validate_Request(prefix, request, "");
    }
    
    final public int validate_Request(HttpServletRequest request, String suffix){
        return validate_Request("", request, suffix);
    }
    
    
    
    abstract public int validate_Request(String prefix, HttpServletRequest request, String suffix);
    
    
    
    
    abstract public String getSql_Type();
    
    
    public String getSql_String(){
        return SQLUtil.encode(this.toString());
    }
    
    abstract public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException;
    
    public String toWhere_Clause(){
        return this.field_Name + " = '" + this.getSql_String()+"' ";
    }
    
    public String toWhere_Range_Clause(){
        return this.field_Name + " >= '" + this.getSql_String()+"' AND " +
        this.field_Name + " <= '" + this.getSql_String()+"~"+"' ";
    }
    
    public String toWhere_Range_Clause(String prefix){
        return prefix + this.field_Name + " >= '" + this.getSql_String()+"' AND " +
        prefix + this.field_Name + " <= '" + this.getSql_String()+"~"+"' ";
    }
    
    
    public String toWhere_Clause(String operator){
        return this.field_Name + " "+operator+"  '" + this.getSql_String()+"' ";
    }
    
    
    abstract public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException;
    
    abstract public String getSQL_Declaration();
    
    abstract public String getSQL_Declaration(String default_Value);

    
    final public String toHTML_Input(String web_Root, String form_Name){
        return toHTML_Input("", web_Root, form_Name, 0, null, "");
    }
    
    final public String toHTML_Input(String web_Root, String form_Name, int size){
        return toHTML_Input("", web_Root, form_Name, size, null, "");
    }
    
    final public String toHTML_Input(String web_Root, String form_Name, int size, String option){
        return toHTML_Input("", web_Root, form_Name, size, option, "");
    }
    
    final public String toHTML_Input(String prefix, String web_Root, String form_Name, int size, String suffix){
        return toHTML_Input(prefix, web_Root, form_Name, size, null, suffix);
        
    }
    
    abstract public String toHTML_Input(String prefix, String web_Root, String form_Name, int size, String option, String suffix);
    
    public String toHTML() {
        return (StringPlus.html_Encode(this.toString()));
    }
    
    
    public String toHREF_Parameter(){
        return "&" + this.field_Name + "=" + StringPlus.http_Encode(this.toString());
    }
    
    
    public boolean same_Base_Field_Layout(DFBase dFBase){
        if(!this.field_Name.equals(dFBase.field_Name)){
            return false;
        }
        return true;
    }
    
    abstract public boolean same_Field_Layout(DFBase dFBase);
    
    
    /**
     * Getter for property part_Of_Primary_Key.
     * @return Value of property part_Of_Primary_Key.
     */
    public boolean isPart_Of_Primary_Key() {
        return part_Of_Primary_Key;
    }
    
    /**
     * Setter for property part_Of_Primary_Key.
     * @param part_Of_Primary_Key New value of property part_Of_Primary_Key.
     */
    public void setPart_Of_Primary_Key(boolean part_Of_Primary_Key) {
        this.part_Of_Primary_Key = part_Of_Primary_Key;
    }
    
}
