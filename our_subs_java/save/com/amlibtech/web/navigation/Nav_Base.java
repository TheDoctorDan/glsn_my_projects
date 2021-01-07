/*
 * Nav_Base.java
 *
 * Created on October 23, 2004, 4:00 AM
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

package com.amlibtech.web.navigation;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Nav_Base {
    private static String base_URL;
    private static Nav_Base nav_Base_Instance = new Nav_Base();
    
    
    /** Creates a new instance of Nav_Base */
    public Nav_Base(){}
    
    public Nav_Base(String base_URL) {
        this.base_URL = base_URL;
    }
    public static Nav_Base getInstance(){
        return nav_Base_Instance;
    }
	public static void setBase_URL(String base_URL){
		Nav_Base.getInstance().base_URL = base_URL;
	}

	public static String getBase_URL(){ return base_URL; }
        
        public static void forward (HttpServletResponse response, String destination)
            throws IOException 
        {
                   
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        /* TODO output your page here */
        
        out.println("<script type=\"text/javascript\">");
	out.println("top.location.href = \"" + Nav_Base.getBase_URL() + destination + "\";");
        out.println("</script>");
        /* */
        out.close();
        
        }

        public static void new_Window (HttpServletResponse response, String destination)
            throws IOException 
        {
                   
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        /* TODO output your page here */
        
        out.println("<script type=\"text/javascript\">");
	out.println("winRef = window.open(\"" + Nav_Base.getBase_URL() + destination + "\")");
        out.println("</script>");
        /* */
        out.close();
        
        }

        public static String get_Html_Go_To(String destination){
            StringBuffer result=new StringBuffer();
            result.append("<script type=\"text/javascript\">\n");
            result.append("top.location.href = \"" + Nav_Base.getBase_URL() + destination + "\";\n");
            result.append("</script>\n");
            return result.toString();
        }
}
