/*
 * DFString_Encrypted_Password_Hex.java
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
public class DFString_Encrypted_Password_Hex extends DFString_Encrypted_Hex {
    
    private String value;
    
    private int min_Length;
    private int max_Length;
    
    /** Creates a new instance of DFString_Encrypted_Password_Hex */
    
    public DFString_Encrypted_Password_Hex(String field_Name, String field_Title, int max_Length) throws DFException {
        super(field_Name, field_Title, max_Length);
    }
    
    public DFString_Encrypted_Password_Hex(String field_Name, String field_Title, int min_Length, int max_Length) throws DFException {
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
    
   
   
    public String toHTML_Input(String web_Root, String form_Name, String field_Name_Suffix) {
        int size=this.max_Length;
	if(size>60)size=60;

        String answer;
        answer = "\t<input " +
        "name='" + this.field_Name + field_Name_Suffix + "' " +
        "id='" + this.field_Name + field_Name_Suffix + "' " +
        "type='password' " +
        "value='" + StringPlus.html_Encode(this.request_Value) + "' " +
        "size='" + size + "' " +
        "maxlength='" + this.max_Length +"' >";
        if(this.min_Length !=0)
            answer += RED_ASTERISK_HTML;
        if(this.request_Error.length() !=0)
            answer += "<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n";
        return answer;
    }
    
     
}
