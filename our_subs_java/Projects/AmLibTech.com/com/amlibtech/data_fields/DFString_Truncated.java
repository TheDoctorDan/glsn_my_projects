/*
 * DFString_Truncated.java
 *
 * Created on March 4, 2006, 11:19 PM
 */

package com.amlibtech.data_fields;

import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class DFString_Truncated extends DFString {
    
    
    
    /** Creates a new instance of DFString_Truncated */
   
    
    public DFString_Truncated(String field_Name, String field_Title, int max_length) throws DFException {
        super(field_Name, field_Title, max_length);
        

    }
    
    public DFString_Truncated(String field_Name, String field_Title, int min_Length, int max_Length) throws DFException {
        super(field_Name, field_Title, min_Length, max_Length);
                
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
        
        if(this.min_Length !=0)
            if(length < this.min_Length){
                throw new DFException(this.field_Title + " Minimum Length is :" + this.min_Length + ".");
            }
        
        if(this.max_Length !=0)
            if(length > this.max_Length){ 
                value = value.substring(0, max_Length -1);
            }
        this.value = value;
        this.request_Value=this.value;
        this.request_Error ="";
    }
    
   
    
    
}
