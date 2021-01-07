/*
 * WPFatal_Exception.java
 *
 * Created on August 8, 2006, 9:03 AM
 */

package com.amlibtech.web_Processes;

/**
 *
 * @author  dgleeson
 */
public class WPFatal_Exception extends java.lang.Exception {

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
     * Creates a new instance of <code>WPFatal_Exception</code> without detail message.
     */
    public WPFatal_Exception() {
    }
    
    
    /**
     * Constructs an instance of <code>WPFatal_Exception</code> with the specified detail message.
     * @param msg the detail message.
     */
    public WPFatal_Exception(String msg) {
        super(msg);
    }
}
