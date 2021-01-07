/*
 * DFString_Filled.java
 *
 * Created on March 5, 2006, 11:06 AM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class DFString_Filled extends DFString {
    
    public static final int FILL_TYPE_LEFT = 1;
    public static final int FILL_TYPE_RIGHT = 2;
    public static final int FILL_TYPE_CENTER = 3;
    public static final int FILL_TYPE_TRIM = 4;
    
    
    
    private int  fill_Type; // left, right, trim
    private char  fill_Char;
    
    
    /** Creates a new instance of DFFilled_String */
    
    
    public DFString_Filled(String field_Name, String field_Title, int max_Length, int fill_Type, char fill_Char) throws DFException {
        super(field_Name, field_Title, max_Length);

        setFill_Type(fill_Type);
        setFill_Char(fill_Char);
    }
     
    public DFString_Filled(String field_Name, String field_Title, int min_Length, int max_Length, int fill_Type, char fill_Char) throws DFException {
        super(field_Name, field_Title, min_Length, max_Length);

        setFill_Type(fill_Type);
        setFill_Char(fill_Char);
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
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(String value) throws DFException {
        int length;

        length = value.length();
        
        if(this.min_Length != 0)
            if(length < this.min_Length){
                    throw new DFException(this.field_Title + " Minimum Length is :" + this.min_Length + ".");
            }
        
        if(length > this.max_Length){
            throw new DFException(this.field_Title + " Maximum Length is :" + this.max_Length + ".");
        }
        this.value = do_Filling(value);
        this.request_Value=this.value;
        this.request_Error ="";
    }

   
    
    /**
     * Getter for property fill_Type.
     * @return Value of property fill_Type.
     */
    public int getFill_Type() {
        return fill_Type;
    }
    
    /**
     * Setter for property fill_Type.
     * @param fill_Type New value of property fill_Type.
     */
    private void setFill_Type(int fill_Type) throws DFException {
        switch(fill_Type){
            case FILL_TYPE_LEFT:
            case FILL_TYPE_RIGHT:
            case FILL_TYPE_CENTER:
            case FILL_TYPE_TRIM:
                break;
            default:
                throw new DFException("Data Field :" + this.field_Title + " can not be set to unknown value :" + fill_Type + ".");
        }
        this.fill_Type = fill_Type;
    }
    
    /**
     * Getter for property fill_Char.
     * @return Value of property fill_Char.
     */
    public char getFill_Char() {
        return fill_Char;
    }
    
    /**
     * Setter for property fill_Char.
     * @param fill_Char New value of property fill_Char.
     */
    private void setFill_Char(char fill_Char) {
        this.fill_Char = fill_Char;
    }
    
    
    private String do_Filling(String passed_Value) throws DFException {
        String ret_Value=passed_Value;
        switch(this.fill_Type){
            case FILL_TYPE_LEFT:
                ret_Value = StringPlus.left_Fill(passed_Value, max_Length, fill_Char);
                break;
            case FILL_TYPE_RIGHT:
                ret_Value = StringPlus.right_Fill(passed_Value, max_Length, fill_Char);
                break;
            case FILL_TYPE_CENTER:
                ret_Value = StringPlus.center_Fill(passed_Value, max_Length, fill_Char);
                break;
            case FILL_TYPE_TRIM:
                ret_Value = passed_Value.trim();
                break;
            default:
                throw new DFException(field_Title + " has unknown Fill type :" + fill_Type + ".");
        }
        return ret_Value;
    }
      
    

}
