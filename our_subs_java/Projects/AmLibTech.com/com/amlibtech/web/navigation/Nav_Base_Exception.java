/*
 * Nav_Base_Exception.java
 *
 * Created on February 22, 2006, 8:46 PM
 */

package com.amlibtech.web.navigation;

/**
 *
 * @author  dgleeson
 */
public class Nav_Base_Exception extends java.lang.Exception {

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
     * Creates a new instance of <code>Nav_Base_Exception</code> without detail message.
     */
    public Nav_Base_Exception() {
    }
    
    
    /**
     * Constructs an instance of <code>Nav_Base_Exception</code> with the specified detail message.
     * @param msg the detail message.
     */
    public Nav_Base_Exception(String msg) {
        super(msg);
    }
}
