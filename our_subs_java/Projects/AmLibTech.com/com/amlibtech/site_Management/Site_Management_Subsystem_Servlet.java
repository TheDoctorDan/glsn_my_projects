/*
 * Site_Management_Subsystem_Servlet.java
 *
 * Created on June 28, 2006, 8:48 PM
 */

package com.amlibtech.site_Management;

import com.amlibtech.web_Processes.*;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 * @version
 */
public class Site_Management_Subsystem_Servlet extends Subsystem_Servlet_Base {

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
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        /* TODO output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet</title>");
        out.println("</head>");
        out.println("<body>");
         
        out.println("</body>");
        out.println("</html>");
         */
        out.close();
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Site Management Subsystem Servlet";
    }
    
    public String[] getDatabase_Record_List() {
        String[] database_Record_List ={
            "com.amlibtech.login.data.Program",
            "com.amlibtech.login.data.Subscriber_Organization_Portal",
            "com.amlibtech.login.data.Subscriber_Organization_Portal_Subsystem",
            "com.amlibtech.login.data.User",
            "com.amlibtech.site_Management.data.User_Master_File"
        };
        return database_Record_List;
    }
    
    public WPHttpServlet_Base[] getServlet_List() {
        WPHttpServlet_Base[] servlet_List ={ 
            new Run_Task(),
            new Sm100()
        };
        return servlet_List;
    }
    
}
