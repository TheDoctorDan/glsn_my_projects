/*
 * DFException.java
 *
 * Created on March 4, 2006, 11:40 PM
 */

package com.amlibtech.data_fields;

/**
 *
 * @author  dgleeson
 */
public class DFException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>DFException</code> without detail message.
     */
    public DFException() {
    }
    
    
    /**
     * Constructs an instance of <code>DFException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DFException(String msg) {
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
