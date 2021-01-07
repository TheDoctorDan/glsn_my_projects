/*
 * DFString_Scrambled.java
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
public class DFString_Scrambled extends DFBase {
    private static String sql_Type = "bytes";

    private String value;
    
    private int min_Length;
    private int max_Length;
    private int byte_Array_Length;
    
    /** Creates a new instance of DFString_Scrambled */
    
    
    public DFString_Scrambled(String field_Name, String field_Title, int max_Length) throws DFException {
        super(field_Name, field_Title);
        
        setMin_Length(0);
        setMax_Length(max_Length);
        
        this.value="";
        this.request_Value = this.value;
    }
    
    public DFString_Scrambled(String field_Name, String field_Title, int min_Length, int max_Length) throws DFException {
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
        this.byte_Array_Length = (this.max_Length/31 +1)*31;
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
            this.value = this.scrambled_Text_Decode(resultSet.getBytes(this.field_Name));
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        this.request_Value = this.value;
    }
    
   
    
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
        try {
            preparedStatement.setBytes(count, this.scrambled_Text_Encode(this.value));
        }
        catch(java.sql.SQLException sqle){
            throw new DFException(sqle.getMessage());
        }
        return preparedStatement;
    }
    
    
    private byte[] scrambled_Text_Encode(String text) {
        int[]      reseq_Array = { 29, 4, 6, 17, 0, 26, 13, 10, 16, 12, 22, 2, 20, 3, 9, 24, 25, 21, 23, 27, 15, 7, 28, 1, 19, 14, 30, 5, 11, 8, 18 };
        int     i;
        byte[] scrambled_Text= new byte[byte_Array_Length];
        for(i=0;i<byte_Array_Length;i++){
            scrambled_Text[i]=0;
        }
        for(i=0;i<text.length();i++){
            scrambled_Text[reseq_Array[i%byte_Array_Length]+i/byte_Array_Length] = Byte.parseByte(text.substring(i,i));
        }
        return(scrambled_Text);
    }
    
    
    private String scrambled_Text_Decode(byte[] scrambled_Text) {
        int[]      reseq_Array = { 29, 4, 6, 17, 0, 26, 13, 10, 16, 12, 22, 2, 20, 3, 9, 24, 25, 21, 23, 27, 15, 7, 28, 1, 19, 14, 30, 5, 11, 8, 18 };
        int     i;
        StringBuffer text= new StringBuffer();
        byte[] unscrambled_Text = new byte[byte_Array_Length];
        
        for(i=0;i<byte_Array_Length;i++){
            unscrambled_Text[i] = scrambled_Text[reseq_Array[i%byte_Array_Length]+i/byte_Array_Length];
        }
        for(i=0;i<byte_Array_Length;i++){
            if(unscrambled_Text[i]!=0)
                text.append(new String(unscrambled_Text, i, 1));
            else
                i=byte_Array_Length;
        }
        return(text.toString());
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
        "type='text' " +
        "value='" + StringPlus.html_Encode(this.request_Value) + "' " +
        "size='" + size + "' " +
        "maxlength='" + this.max_Length +"' " +
        out_Option + " >";
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
        if (dFBase instanceof DFString_Scrambled){
            DFString_Scrambled temp = (DFString_Scrambled) dFBase;
            if(!same_Base_Field_Layout(temp))
                return false;
            if(!this.sql_Type.equals(temp.sql_Type))
                return false;
            //if(this.min_Length != temp.min_Length)
                //return false;
            if(this.max_Length != temp.max_Length)
                return false;
            return true;
        }else{
            // wrong type
            return false;
        }
    }
}
