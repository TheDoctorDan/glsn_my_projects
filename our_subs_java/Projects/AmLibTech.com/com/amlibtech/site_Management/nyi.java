/*
 * nyi.java
 *
 * Created on July 13, 2006, 3:49 PM
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
public class nyi extends HttpServlet {

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
        
        
        
        Web_Process web_Process;
        try {
            web_Process = Web_Process.get_For_Servlet(this, request, response);
        }
        catch(WPException wpe){
            Launcher_Servlet.show_Error(this, request, response, wpe.getMessage(), wpe);
            return;
        }
        
        
        
        String action = request.getParameter("action");
        if(action==null){
            web_Process.error_End("No action has been given to servlet.", null);
            return;
        }
        web_Process.Increment_Action_Count();
        
        
        
        
        
        
        
        
        if(action.equals("Start") || action.equals("Reload")){
            web_Process.error_End("This Task has Not Yet Been Implemented!", null);
            return;
        }else{
            web_Process.error_End("Unknown action :" + action + ": given to application.", null);
            return;
        }
        
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
        return "Not Yet Implemented place holder";
    }

}
