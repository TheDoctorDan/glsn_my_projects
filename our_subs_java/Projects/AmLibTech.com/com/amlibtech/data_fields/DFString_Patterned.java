/*
 * DFString_Patterned.java
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
public class DFString_Patterned extends DFString {
    
    
    
    private String fill_Pattern;

    
    /** Creates a new instance of DFFilled_String */
    
    
    public DFString_Patterned(String field_Name, String field_Title, String fill_Pattern) throws DFException {
        super(field_Name, field_Title, fill_Pattern.length());
        
        setFill_Pattern(fill_Pattern);
        this.value = "";
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
     * Getter for property fill_Pattern.
     * @return Value of property fill_Pattern.
     */
    public String getFill_Pattern() {
        return fill_Pattern;
    }
    
    /**
     * Setter for property fill_Pattern.
     * @param fill_Pattern New value of property fill_Pattern.
     */
    private void setFill_Pattern(String fill_Pattern) {
        this.fill_Pattern = fill_Pattern;
    }    
   
    
    
    private String do_Filling(String passed_Value) throws DFException {
        String ret_Value=passed_Value;
         
        try {
            ret_Value = StringPlus.key_Fill(passed_Value, this.fill_Pattern);
        }
        catch(StringPlus_Exception spe){
            throw new DFException(field_Title + " StringPlus_Exception: "  + spe.getMessage());
        }
        return ret_Value;
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
        this.value = do_Filling(value);
        this.request_Value=this.value;
        this.request_Error ="";
    }
 
   
}
