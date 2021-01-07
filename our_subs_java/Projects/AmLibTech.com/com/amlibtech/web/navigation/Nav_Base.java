/*
 * Nav_Base.java
 *
 * Created on October 23, 2004, 4:00 AM
 */

/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 2000, 2001, 2002, 2003, 2004, 2005, 2006       |
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
    private String base_URL;

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
    
    
    /** Creates a new instance of Nav_Base */
    public Nav_Base(){
        this.base_URL=null;
    }
    
    /*
    public Nav_Base(String base_URL) {
        this.base_URL = base_URL;
    }
    
    
    
    public void setBase_URL(String base_URL){
        this.base_URL = base_URL;
    }
    */
    
    public static Nav_Base get_Session_Instance(HttpSession session){
        Nav_Base nav_Base = (Nav_Base) session.getAttribute("s_Nav_Base");
        return nav_Base;
    }


    public void set_Session_Instance(HttpSession session){
        session.setAttribute("s_Nav_Base", this);
    }

    public void remove_Session_Instance(HttpSession session){
        session.removeAttribute("s_Nav_Base");
    }
    
    public static void setBase_URL(HttpServletRequest request, String nav_Suffix) 
    throws Nav_Base_Exception {
        Nav_Base nav_Base = new Nav_Base();
        HttpSession session = request.getSession();
        StringBuffer requestURL=request.getRequestURL();
        int index= -1;
        try {
            index = requestURL.indexOf(nav_Suffix);
        }
        catch(NullPointerException npe){
            throw new Nav_Base_Exception("Nav_Base.setBase_URL() nav_Suffix is null.");
        }
        
        if(index== -1)
            throw new Nav_Base_Exception("Nav_Base.setBase_URL() nav_Suffix is not part of requestURL.");
        
        if(requestURL.length() != (index + nav_Suffix.length()))
            throw new Nav_Base_Exception("Nav_Base.setBase_URL() nav_Suffix is not at end of requestURL.");

        String base_URL = requestURL.substring(0, requestURL.length()-nav_Suffix.length());
        nav_Base.base_URL = base_URL;
        nav_Base.set_Session_Instance(session);
    }
    
    public String getBase_URL(){ return base_URL; }
    

    public static void forward(HttpServletResponse response, HttpSession session, String destination)
    throws IOException {
        Nav_Base nav_Base = get_Session_Instance(session);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        /* TODO output your page here */
        
        out.println("<script type=\"text/javascript\">");
        out.println("top.location.href = \"" + nav_Base.getBase_URL() + destination + "\";");
        out.println("</script>");
        /* */
        out.close();
        
    }

    public static void forward(PrintWriter out, HttpSession session, String destination)
    throws IOException {
        Nav_Base nav_Base = get_Session_Instance(session);

        /* TODO output your page here */
        
        out.println("<script type=\"text/javascript\">");
        out.println("top.location.href = \"" + nav_Base.getBase_URL() + destination + "\";");
        out.println("</script>");
        /* */
        out.close();
        
    }


    public static void new_Window(HttpServletResponse response, HttpSession session, String destination)
    throws IOException {

        Nav_Base nav_Base = get_Session_Instance(session);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        /* TODO output your page here */
        
        out.println("<script type=\"text/javascript\">");
        out.println("winRef = window.open(\"" + nav_Base.getBase_URL() + destination + "\");");
        out.println("</script>");
        /* */
        out.close();
        
    }
    
    public static String get_Html_Go_To(HttpSession session, String destination){

        Nav_Base nav_Base = get_Session_Instance(session);

        StringBuffer result=new StringBuffer();
        result.append("<script type=\"text/javascript\">\n");
        result.append("top.location.href = \"" + nav_Base.getBase_URL() + destination + "\";\n");
        result.append("</script>\n");
        return result.toString();
    }



}
