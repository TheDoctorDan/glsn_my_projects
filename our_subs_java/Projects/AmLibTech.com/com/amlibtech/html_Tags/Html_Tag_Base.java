/*
 * Html_Tag_Base.java
 *
 * Created on July 6, 2006, 5:18 PM
 */

package com.amlibtech.html_Tags;

/**
 *
 * @author  dgleeson
 */
public class Html_Tag_Base {
    
    String  tag_Type;
    
    /** Creates a new instance of Html_Tag_Base */
    public Html_Tag_Base(String tag_Type) {
        this.tag_Type=tag_Type;
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
     * Getter for property tag_Type.
     * @return Value of property tag_Type.
     */
    public java.lang.String getTag_Type() {
        return tag_Type;
    }
    
    /**
     * Setter for property tag_Type.
     * @param tag_Type New value of property tag_Type.
     */
    public void setTag_Type(java.lang.String tag_Type) {
        this.tag_Type = tag_Type;
    }
    
}
