/* Bad_Date_Exception.java */
/**
*
* Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004
* American Liberator Technologies
* All Rights Reserved
*
* THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
* American Liberator Technologies
* The copyright notice above does not evidence any
* actual or intended publication of such source code.
*
**/

package com.amlibtech.util;

public class Bad_Date_Exception extends java.lang.Exception {
	public Bad_Date_Exception() {
	}
	public Bad_Date_Exception(String mess) {
		super(mess);
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
