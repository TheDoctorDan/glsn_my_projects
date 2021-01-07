/*
 * Attribute_Validation_Exception.java
 *
 * Created on November 19, 2004, 3:31 PM
 */

/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004       |
|       American Liberator Technologies                                        |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       American Liberator Technologies                                        |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
*/

package com.amlibtech.web.servlet_exceptions;

/**
 *
 * @author  dgleeson
 */
public class Attribute_Validation_Exception extends RuntimeException{
    
    /** Creates a new instance of Attribute_Validation_Exception */
    public Attribute_Validation_Exception(String message) {
        super(message);
    }
    
}
