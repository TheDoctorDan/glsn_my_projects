/*
 * DFString_Encrypted_Hex.java
 *
 * Created on March 22, 2006, 16:59 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class DFString_Encrypted_Hex extends DFBase {
    private static String sql_Type = "varchar";

    private String value;
    
    private int min_Length;
    private int max_Length;
    private int byte_Array_Length;
    
    /** Creates a new instance of DFString_Encrypted_Hex */
    
    
    public DFString_Encrypted_Hex(String field_Name, String field_Title, int max_Length) throws DFException {
        super(field_Name, field_Title);
        
        setMin_Length(0);
        setMax_Length(max_Length);
        
        this.value="";
        this.request_Value = this.value;
    }
    
    public DFString_Encrypted_Hex(String field_Name, String field_Title, int min_Length, int max_Length) throws DFException {
        super(field_Name, field_Title);
        
        setMin_Length(min_Length);
        setMax_Length(max_Length);
        
        this.value="";
        this.request_Value = this.value;
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
        int length;
        length = value.length();
        
        if(this.min_Length !=0)
            if(length < this.min_Length){
                throw new DFException(this.field_Title + " Minimum Length is :" + this.min_Length + ".");
            }
        
        if(this.max_Length !=0)
            if(length > this.max_Length){
                throw new DFException(this.field_Title + " Maximum Length is :" + this.max_Length + ".");
            }
        this.value = value;
        this.request_Value = this.value;
        this.request_Error ="";
    }
    
    /**
     * Getter for property min_Length.
     * @return Value of property min_Length.
     */
    public int getMin_Length() {
        return min_Length;
    }
    
    /**
     * Setter for property min_Length.
     * @param min_Length New value of property min_Length.
     */
    private void setMin_Length(int min_Length) throws DFException {
        if(min_Length<0){
            throw new DFException("Data Field :" + this.field_Title + ": minimum length can not be less than 0.");
        }
        this.min_Length = min_Length;
    }
    
    /**
     * Getter for property max_Length.
     * @return Value of property max_Length.
     */
    public int getMax_Length() {
        return max_Length;
    }
    
    /**
     * Setter for property max_Length.
     * @param max_Length New value of property max_Length.
     */
    private void setMax_Length(int max_Length) throws DFException {
        if(max_Length<0){
            throw new DFException("Data Field :" + this.field_Title + ": maximum length can not be less than 0.");
        }
        this.max_Length = max_Length;
        this.byte_Array_Length = (this.max_Length/48 +1)*48 + 16;
    }
    
    
    
    public int validate_Request(String prefix, HttpServletRequest request, String suffix) {
        
        String parameter_Value = request.getParameter(prefix + this.field_Name + suffix);
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
            String hex_String = resultSet.getString(this.field_Name);
            byte[] dmy_Bytes = StringPlus.hex_String_to_Bytes(hex_String);
            this.value = Encrypt.getDecryptedBytesToString(dmy_Bytes);
        }
        catch(java.sql.SQLException sqle){
            throw new DFException("  field: " + this.field_Name + "  Error: " + sqle.getMessage());
        }catch(StringPlus_Exception spe){
            throw new DFException("  field: " + this.field_Name + "  Error: " + spe.getMessage());
        }catch(EncryptException ee){
            throw new DFException("  field: " + this.field_Name + "  Error: " + ee.getMessage());
        }
        
        
        
//        byte[] dmy_Bytes= null;
//        try {
//            dmy_Bytes = resultSet.getBytes(this.field_Name);
//
//            this.value = Encrypt.getDecryptedBytesToString(resultSet.getBytes(this.field_Name));
//        }
//        catch(java.sql.SQLException sqle){
//            throw new DFException(sqle.getMessage());
//        }
//        catch(EncryptException ee){
//            this.value="garbled";
//            //throw new DFException("  field: " + this.field_Name + "  Error: " + ee.getMessage() + "  byte array length = " + dmy_Bytes.length+ " ");
//        }
        this.request_Value = this.value;
    }
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
        try {            
            byte[] dmy_Bytes = Encrypt.getEncryptedStringToBytes(this.value);
            String hex_String = StringPlus.bytes_To_Hex_String(dmy_Bytes);
            preparedStatement.setString(count, hex_String);
        }
        catch(java.sql.SQLException sqle){
            throw new DFException("  field: " + this.field_Name + "  Error: " + sqle.getMessage());
        }
        catch(EncryptException ee){
            throw new DFException("  field: " + this.field_Name + "  Error: " + ee.getMessage());
        }
        
        
        
        
//        try {
//            preparedStatement.setBytes(count, Encrypt.getEncryptedStringToBytes(this.value));
//        }
//        catch(java.sql.SQLException sqle){
//            throw new DFException(sqle.getMessage());
//        }
//        catch(EncryptException ee){
//            throw new DFException(ee.getMessage());
//        }
        return preparedStatement;
    }
    
    
    
    public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.byte_Array_Length*2 + ") NOT NULL default '',\n");
    }
    
    public String getSQL_Declaration(String default_Value){
        return ("`" + this.field_Name + "` " + sql_Type + "(" + this.byte_Array_Length*2 + ") NOT NULL default '" + default_Value + "',\n");
    }
    
    
    public String toString(){
        return this.value;
    }

    /**
     * Getter for property byte_Array_Length.
     * @return Value of property byte_Array_Length.
     */
    public int getByte_Array_Length() {
        return byte_Array_Length;
    }    
   
       
   

    public String toHTML_Input(String prefix, String web_Root, String form_Name, int size, String option, String suffix) {
        if(size==0)
            size=this.max_Length;
	if(size>60)size=60;

        String out_Option;
        if(option==null)
            out_Option="";
        else
            out_Option=option;
        
        String answer;
        answer = "\t<input " +
        "name='" + prefix + this.field_Name + suffix + "' " +
        "id='" + prefix + this.field_Name + suffix + "' " +
        "type='text' " +
        "value='" + StringPlus.html_Encode(this.request_Value) + "' " +
        "size='" + size + "' " +
        "maxlength='" + this.max_Length +"' " +
        out_Option + " ></input>";
        if(this.min_Length !=0)
            answer += RED_ASTERISK_HTML;
        if(this.request_Error.length() !=0)
            answer += "<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n";
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
        if (dFBase instanceof DFString_Encrypted_Hex){
            
            DFString_Encrypted_Hex temp = (DFString_Encrypted_Hex) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            
            //if(this.min_Length != temp.min_Length)
                //return false;

            if(this.byte_Array_Length != temp.byte_Array_Length)
                return false;
            return true;
            
        }else if(dFBase instanceof DFString){
            DFString temp = (DFString) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            
            if(!this.sql_Type.equals(temp.getSql_Type()))
                return false;
            
            //if(this.min_Length != temp.min_Length)
                //return false;
            
            if(this.byte_Array_Length*2 != temp.max_Length)
                return false;
            return true;
            
        }else{
            // wrong type
            return false;
        }
    }
}
