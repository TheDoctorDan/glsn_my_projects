/*
 * DFZip_Code.java
 *
 * Created on July 28, 2006, 1:05 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;

/**
 *
 * @author  dgleeson
 */
public class DFZip_Code extends DFString {
    
    /**
     * Creates a new instance of DFZip_Code
     * @param field_Name
     * @param field_Title
     * @throws DFException
     */
    public DFZip_Code(String field_Name, String field_Title) throws DFException {
        super(field_Name, field_Title, 0, 21);
    }
    
    /**
     *
     * @param field_Name
     * @param field_Title
     * @param min_Length
     * @throws DFException
     */    
    public DFZip_Code(String field_Name, String field_Title, int min_Length) throws DFException {
        super(field_Name, field_Title, min_Length, 21);
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
        
        passed_Value = passed_Value.trim();
        length = passed_Value.length();
        
        if(this.min_Length != 0)
            if(length < this.min_Length){
                throw new DFException(this.field_Title + " Minimum Length is :" + this.min_Length + ".  Starting with a Number or a _ for international#s.");
            }
        
        if(length > this.max_Length){
            throw new DFException(this.field_Title + " Maximum Length is :" + this.max_Length + ".");
        }
        if(length==0){
            // good! since min say they take 0 length
            this.value=passed_Value;
        }else{
            if(passed_Value.substring(0,1).equals("_")){
                // international
                this.value=passed_Value;
            }else if(passed_Value.substring(0,1).matches("[0-9]")) {
                //domestic
                if(passed_Value.matches("^[0-9][0-9][0-9][0-9][0-9]\\-[0-9][0-9][0-9][0-9]$")){
                    // good!
                    this.value=passed_Value;
                }else if(passed_Value.matches("^[0-9][0-9][0-9][0-9][0-9]$")){
                    // good!
                    this.value=passed_Value;
                }else{
                    throw new DFException(field_Title + " Should look like 12345-1234 or 12345");

                    // try to fill
//                    if(passed_Value.indexOf("-") != -1){
//                        try {
//                            this.value = StringPlus.key_Fill(passed_Value, "55555-5555");
//                        }
//                        catch(StringPlus_Exception spe){
//                            throw new DFException(field_Title + " Should look like 12345-1234 or 12345");
//                        }
//                    }else{
//                        try {
//                            this.value = StringPlus.key_Fill(passed_Value, "55555-5555");
//                        }
//                        catch(StringPlus_Exception spe){
//                            throw new DFException(field_Title + " Should look like 12345-1234 or 12345");
//                        }
//                    }
                }
            }else{
                throw new DFException(field_Title + " must start with  a Number  or  a _  for international #s.");
            }
        }
        
        this.request_Value=this.value;
        this.request_Error ="";
    }
    
}
