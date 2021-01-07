/*
 * Security_Exception.java
 *
 * Created on July 16, 2006, 8:39 PM
 */

package com.amlibtech.security_Management;

/**
 *
 * @author  dgleeson
 */
public class Security_Exception extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>Security_Exception</code> without detail message.
     */
    public Security_Exception() {
    }
    
    
    /**
     * Constructs an instance of <code>Security_Exception</code> with the specified detail message.
     * @param msg the detail message.
     */
    public Security_Exception(String msg) {
        super(msg);
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

}
