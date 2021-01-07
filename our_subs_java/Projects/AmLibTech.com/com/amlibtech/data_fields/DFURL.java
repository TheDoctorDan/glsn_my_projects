/*
 * DFURL.java
 *
 * Created on June 21, 2007, 8:00 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;

/**
 *
 * @author  dgleeson
 */
public class DFURL extends DFString {
    
    
    /**
     * Creates a new instance of DFURL
     * @param field_Name
     * @param field_Title
     * @throws DFException
     */
    public DFURL(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title, 0, 200);
    }
    
    /**
     *
     * @param field_Name
     * @param field_Title
     * @param min_Length
     * @throws DFException
     */
    public DFURL(String field_Name, String field_Title, int min_Length) throws DFException {
        super(field_Name, field_Title, min_Length, 200);
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
     * @param passed_Value
     * @throws DFException
     */
    public void setValue(String passed_Value) throws DFException {
        int length;
        
        length = passed_Value.length();
        
        if(this.min_Length != 0)
            if(length < this.min_Length){
                throw new DFException(this.field_Title + " Minimum Length is :" + this.min_Length + ".");
            }
        
        if(length > this.max_Length){
            throw new DFException(this.field_Title + " Maximum Length is :" + this.max_Length + ".");
        }
        
        this.value = passed_Value.trim();

        this.request_Value=this.value;
        this.request_Error ="";
    }
 
    public String toHTML() {
        if(this.toString().indexOf(":")== -1){
            // no : so add one
            return ("<a href=\"http://"+StringPlus.http_Encode(this.toString()) + "\" target='_blank' >" + StringPlus.html_Encode(this.toString()) + "</a>&nbsp;");
        }else{
            return ("<a href=\""+StringPlus.http_Encode(this.toString()) + "\" target='_blank' >" + StringPlus.html_Encode(this.toString()) + "</a>&nbsp;");
        }

    }
    
}
