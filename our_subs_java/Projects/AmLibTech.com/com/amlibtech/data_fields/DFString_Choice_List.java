/*
 * DFString_Choice_List.java
 *
 * Created on October 15, 2006, 11:32 AM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class DFString_Choice_List extends DFBase {
    private static String sql_Type = "varchar";
    
    private String value;
    private DFChoice_Node[] choice_Nodes;
    private int max_Length;
    
    
    /** Creates a new instance of DFString_Choice_List */
    public DFString_Choice_List(String field_Name, String field_Title, DFChoice_Node[] choice_Nodes) throws DFException {
        super(field_Name, field_Title);
        
        if(choice_Nodes.length <1){
            throw new DFException("DFString_Choice_List: constructor(): field_Name :" + field_Name + ": You need at least 2 Choice_Node values.");
        }
        this.choice_Nodes = choice_Nodes;
	this.max_Length=0;
        for(int i=0;i<this.choice_Nodes.length;i++){
            max_Length=this.choice_Nodes[i].getDescription().length()>max_Length?this.choice_Nodes[i].getDescription().length():max_Length;
        }
        //this.max_Length+=1;
        
        this.value=choice_Nodes[0].getDescription();
        this.request_Value=this.value;
        
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
        
        if(DFChoice_Node.indexOfDescription(choice_Nodes, value) >=0){
                this.value = value;
                this.request_Value = this.value;
                this.request_Error ="";
                return;
        }
        throw new DFException(this.field_Title + " invalid choice.");
    }
    
    
    
    
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        
        String parameter_Value = request.getParameter(prefix+this.field_Name+suffix);
        this.request_Value=parameter_Value;
        this.request_Error ="";
        
        if(parameter_Value == null){
            // no parameter passed, use default.
            parameter_Value = this.value;
        }
        if(parameter_Value==null)
            parameter_Value="";
        
        try {
            setValue(parameter_Value);
        }
        catch(DFException dfe){
            this.request_Error = dfe.getMessage();
            return REQUEST_ERRCODE_WRONG;
        }
        return REQUEST_ERRCODE_NONE;
    }
    
    public void setValue_ResultSet(java.sql.ResultSet resultSet) throws DFException {
        try {
            this.value = resultSet.getString(this.field_Name);
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        this.request_Value = this.value;
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
        return(this.value);
    }
    
    
    
    
    
    
         
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int not_Used_Size, String option, String suffix) {
        String  temp_Description;
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
        if(DFChoice_Node.indexOfDescription(choice_Nodes, this.value) >=0){
                found=true;
        }
        if(!found)
            out.append("\t\t<option value='Select Value' selected>Select Value</option>\n");
        
        for(int i = 0; i<choice_Nodes.length; i++){
            temp_Description = choice_Nodes[i].getDescription();
            if(this.value.equals(temp_Description)){
                out.append("\t\t<option value='" + StringPlus.html_Encode(temp_Description) + "' selected>" + StringPlus.html_Encode(temp_Description) + "</option>\n");
            } else {
                out.append("\t\t<option value='" + StringPlus.html_Encode(temp_Description) + "'>" + StringPlus.html_Encode(temp_Description) + "</option>\n");
            }
        }
        
        out.append("\t</select>\n");
        out.append(RED_ASTERISK_HTML);
        out.append("<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n");
        
        return out.toString();
    }
    
    
    public String[] toHTML_Input_Radio(String prefix, String web_Root, String form_Name, String suffix) {
        LinkedList list = new LinkedList();
        String  temp_Description;

        // out.append("\t<select name='" + prefix + this.field_Name + suffix + "' id='" + prefix + this.field_Name + suffix + "'>\n");
        
        for(int i = 0; i<choice_Nodes.length; i++){
            temp_Description = choice_Nodes[i].getDescription();

            if(this.value.equals(temp_Description)){
                list.add("<input type=radio name='" + prefix + this.field_Name + suffix + "' value='" + StringPlus.html_Encode(temp_Description) + "' checked >" + StringPlus.html_Encode(temp_Description) + "</input>\n");
                
            } else {
                list.add("<input type=radio name='" + prefix + this.field_Name + suffix + "' value='" + StringPlus.html_Encode(temp_Description) + "'>" + StringPlus.html_Encode(temp_Description) + "</input>\n");
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
    
    
    public int getMax_Length(){
        return max_Length;
    }
    
    public boolean same_Field_Layout(DFBase dFBase){

        if (dFBase instanceof DFString_Choice_List){

            DFString_Choice_List temp = (DFString_Choice_List) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            
            if(this.choice_Nodes.length != temp.choice_Nodes.length)
                return false;
            for(int i=0; i<this.choice_Nodes.length;i++){
                if(!this.choice_Nodes[i].getDescription().equals(temp.choice_Nodes[i].getDescription()))
                    return false;
            }
            
            return true;
            
    }else if (dFBase instanceof DFString_Enumerated){

            DFString_Enumerated temp = (DFString_Enumerated) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.getSql_Type()))
                return false;
            
            if(this.choice_Nodes.length != temp.getEnumerated_Values().length)
                return false;
            for(int i=0; i<this.choice_Nodes.length;i++){
                if(!this.choice_Nodes[i].getDescription().equals(temp.getEnumerated_Values()[i]))
                    return false;
            }
            
            return true;
            
        }else if(dFBase instanceof DFString){

            DFString temp = (DFString) dFBase;
            if(!same_Base_Field_Layout(temp)){
                return false;
            }
            
            //            if(!this.sql_Type.equals(temp.getSql_Type())){
            //                return false;
            //            }
            
            if(this.getMax_Length() != temp.getMax_Length()){
                return false;
            }
            
            return true;
        }else{
            // wrong type
            return false;
        }
    }

    /**
     * Getter for property choice_Nodes.
     * @return Value of property choice_Nodes.
     */
    public com.amlibtech.data_fields.DFChoice_Node[] getChoice_Nodes() {
        return this.choice_Nodes;
    }    
    
    /**
     * Setter for property choice_Nodes.
     * @param choice_Nodes New value of property choice_Nodes.
     */
    public void setChoice_Nodes(com.amlibtech.data_fields.DFChoice_Node[] choice_Nodes) {
        this.choice_Nodes = choice_Nodes;
    }    
    
}
