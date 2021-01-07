/*
 * Subsystem_Servlet_Base.java
 *
 * Created on June 12, 2006, 1:51 PM
 */

package com.amlibtech.web_Processes;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
/**
 *
 * @author  dgleeson
 */
public abstract class Subsystem_Servlet_Base extends HttpServlet {

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
    
    
    public abstract String[] getDatabase_Record_List();

    public abstract WPHttpServlet_Base[] getServlet_List();
    
    
}

