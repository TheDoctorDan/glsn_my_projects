/*
 * DFState_Abbr.java
 *
 * Created on March 4, 2006, 11:19 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import com.amlibtech.web.common_list.US_States;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class DFState_Abbr extends DFBase {
    private static String sql_Type = "varchar";
    
    private String value;
    private static int max_Length= US_States.ABBR_MAX_LEN;
    
    /** Creates a new instance of DFState_Abbr */
    public DFState_Abbr(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title);
        this.value="";
        this.request_Value="";
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
    public String getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(String value) throws DFException {
        
        int length = value.length();
        
        if(length < this.max_Length){
            throw new DFException(this.field_Title + " Minimum Length is :" + this.max_Length + ".");
        }
        
        if(length > this.max_Length){
            throw new DFException(this.field_Title + " Maximum Length is :" + this.max_Length + ".");
        }
        
        for(int i=0; i< US_States.state_Abbr_List.length; i++){
            if(value.equalsIgnoreCase(US_States.state_Abbr_List[i])){
                this.value = US_States.state_Abbr_List[i];
                setRequest_Value();
                return ;
            }
        }
        
        throw new DFException(this.field_Title + " answer :" + value + ": is not a valid state.");
        
    }
    
    
    /**
     * Getter for property State Name.
     * @return Value of property State Name.
     */
    public String getName() {
        return(US_States.get_State_Name(this.value));
    }
      
    private void setRequest_Value(){
        request_Value = US_States.get_State_Name(this.value);
        this.request_Error ="";
    }

    /**
     * Setter for property Value by State Name.
     * @param name New value of property Value's State Name.
     */
    public void setName(String name) throws DFException{

        // convert name to abbr
        for(int i=0; i< US_States.state_Names_List.length; i++){
            if(name.equalsIgnoreCase(US_States.state_Names_List[i])){
                this.value = US_States.state_Abbr_List[i];
                setRequest_Value();
                return;
            }
        }
        throw new DFException("Unknown Name");
    }
    
    
    
    
    /**
     * Getter for property max_Length.
     * @return Value of property max_Length.
     */
    public int getMax_Length() {
        return max_Length;
    }
    
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix)  {
    
        
        String parameter_Value = request.getParameter(prefix + this.field_Name + suffix);
        this.request_Value=parameter_Value;

        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = this.value;
        }
        if(parameter_Value==null)
            parameter_Value="";
        
        if(parameter_Value.length() == this.max_Length){
            try {
                setValue(parameter_Value);
            }
            catch(DFException dfe){
                this.request_Error = dfe.getMessage();
                return REQUEST_ERRCODE_WRONG;
            }
            return REQUEST_ERRCODE_NONE;
        }else{
            try {
                setName(parameter_Value);
            }
            catch(DFException dfe){
                this.request_Error = dfe.getMessage();
                return REQUEST_ERRCODE_WRONG;
            }
            return REQUEST_ERRCODE_NONE;
        }
    }
    
    
    public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException {
        try {
            this.value = resultSet.getString(this.field_Name);
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        setRequest_Value();
    }
    
    
        
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
        try {
            preparedStatement.setString(count, this.value);
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
        return this.value;
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
        for(int i = 0; i<US_States.state_Names_List.length; i++){
            if(this.value.equals(US_States.state_Abbr_List[i])){
                found=true;
                break;
            }
        }
        if(!found)
            out.append("\t\t<option value='Select a State'>Select a State</option>\n");

	for(int i = 0; i<US_States.state_Names_List.length; i++){
            if(this.value.equals(US_States.state_Abbr_List[i])){
                out.append("\t\t<option value='" + StringPlus.html_Encode(US_States.state_Names_List[i]) + "' selected>" + StringPlus.html_Encode(US_States.state_Names_List[i]) + "</option>\n");
            } else {
                out.append("\t\t<option value='" + StringPlus.html_Encode(US_States.state_Names_List[i]) + "'>" + StringPlus.html_Encode(US_States.state_Names_List[i]) + "</option>\n");
            }
	}

        out.append("\t</select>\n");
        out.append(RED_ASTERISK_HTML);
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
        if (dFBase instanceof DFState_Abbr){
            DFState_Abbr temp = (DFState_Abbr) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
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
