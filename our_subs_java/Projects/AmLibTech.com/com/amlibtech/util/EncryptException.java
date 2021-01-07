/*
 * EncryptException.java
 *
 * Created on July 20, 2006, 10:47 AM
 */

package com.amlibtech.util;

/**
 *
 * @author  dgleeson
 */
public class EncryptException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>EncryptException</code> without detail message.
     */
    public EncryptException() {
    }
    
    
    /**
     * Constructs an instance of <code>EncryptException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EncryptException(String msg) {
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
