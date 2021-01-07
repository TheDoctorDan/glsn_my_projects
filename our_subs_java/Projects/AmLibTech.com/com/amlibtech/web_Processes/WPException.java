/*
 * WPException.java
 *
 * Created on June 15, 2006, 1:57 PM
 */

package com.amlibtech.web_Processes;

/**
 *
 * @author  dgleeson
 */
public class WPException extends java.lang.Exception {

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
     * Creates a new instance of <code>WPException</code> without detail message.
     */
    public WPException() {
    }
    
    
    /**
     * Constructs an instance of <code>WPException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public WPException(String msg) {
        super(msg);
    }
}
