/*
 * LoginServlet.java
 *
 * Created on June 9, 2006, 2:19 PM
 */

package com.amlibtech.login;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.login.data.*;
import com.amlibtech.web_Processes.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 * @version
 */
public class LoginServlet extends HttpServlet {

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
    
    void show_Select_Organization_Page(HttpServletRequest request, HttpServletResponse response, String web_Root, String message)
    throws ServletException, IOException {
        
        
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        
        out.println("<html>\n");
        out.println("   <head>\n");


        out.println("<!--");
        out.println("");
        
        out.println("|       Copyright (c) 1985 thru 2002, 2003, 2004, 2005, 2006, 2007, 2008       |");
        out.println("|       Employees of American Liberator Technologies                           |");
        out.println("|       All Rights Reserved                                                    |");
        out.println("|                                                                              |");
        out.println("|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |");
        out.println("|       Employees of American Liberator Technologies                           |");
        out.println("|       The copyright notice above does not evidence any                       |");
        out.println("|       actual or intended publication of such source code.                    |");
        
        out.println("");
        out.println("-->");
        





        out.println("       <title>ASP  Subscriber Organization Selection Page</title>\n");
        
        out.println("       <link rel=\"stylesheet\" href=\"" + web_Root + "/IMPCMD/login.css\" type=\"text/css\">\n");
        
        out.println("   </head>\n");
        out.println("\n");
        out.println("    <body onload=\"document.login.subscriber_Organization_Id.focus();\">\n");
        //out.println("    <body>\n");
        out.println("\n");
        
//        out.println("   <table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n");
//        out.println("       <tr>\n");
//        out.println("           <td><img src=\"" + web_Root + "/images/alt_transparent_button.jpg\" WIDTH=\"120\" alt=\"\"></td>\n");
//        out.println("           <td>American Liberator Technologies</td>\n");
//        out.println("       </tr>\n");
//        out.println("   </table>\n");
        
        
        
        out.println("			\n");
        //out.println("    <center><h1>American Liberator Technologies</h1></center>\n");
        out.println("    <center><h2>Please Select</h2></center>\n");
        out.println("\n");
        
        out.println("    <form id=\"login\" action=\"" + web_Root + "/LoginServlet\" method=\"post\">\n");
        out.println("        <center>\n");
        out.println("        <table cellspacing=\"5\" border=\"0\">\n");
        out.println("            <tbody align=center >\n");
        out.println("                <tr>\n");
        out.println("                    <td align=\"right\">Subscriber Organization</td>\n");
        out.println("                    <td><input type=\"text\" name=\"subscriber_Organization_Id\"></td>\n");
        out.println("                </tr>\n");
        out.println("                <tr>\n");
        out.println("                    <td><input type=\"submit\" value=\"Select\"></td>\n");
        out.println("                </tr>\n");
        out.println("            </tbody>\n");
        out.println("        </table>\n");
        out.println("        </center>\n");
        out.println("    </form>\n");
        out.println("		\n");
        
        if(message != null && message.equals("")==false){
            out.println("    <font color=\"#FF0000\"><font size=4>\n");
            out.println("        <center><i>Error Message: " + message + "</i></center>\n");
            out.println("    </font></font>\n");
        }
        
        out.println("\n");
        out.println("    </body>\n");
        out.println("</html>\n");
        
        out.close();
        
        return;
    }
    
    
    void show_Select_Portal_Page(HttpServletRequest request, HttpServletResponse response, Subscriber_Organization subscriber_Organization, String web_Root, String message)
    throws ServletException, IOException {
        
        
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        
        
        out.println("<html>\n");
        out.println("	<head>\n");


        out.println("<!--");
        out.println("");
        
        out.println("|       Copyright (c) 1985 thru 2002, 2003, 2004, 2005, 2006, 2007, 2008       |");
        out.println("|       Employees of American Liberator Technologies                           |");
        out.println("|       All Rights Reserved                                                    |");
        out.println("|                                                                              |");
        out.println("|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |");
        out.println("|       Employees of American Liberator Technologies                           |");
        out.println("|       The copyright notice above does not evidence any                       |");
        out.println("|       actual or intended publication of such source code.                    |");
        
        out.println("");
        out.println("-->");
        




        out.println("		<title> Organization : " + subscriber_Organization.getDescription().getValue()  + "  Portal Selection Page</title>\n");
        
        out.println("		<link rel=\"stylesheet\" href=\"" + web_Root + "/Subscriber_Organization/" + subscriber_Organization.getSubscriber_Organization_Id().toHREF_Parameter() + "/login.css\" type=\"text/css\">\n");
        out.println("       <link rel=\"stylesheet\" href=\"" + web_Root + "/IMPCMD/login.css\" type=\"text/css\">\n");

        out.println("	</head>\n");
        out.println("\n");
        out.println("    <body onload=\"document.login.subscriber_Organization_Portal_Name.focus();\">\n");
        
        
//        out.println("		<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n");
//        out.println("			<tr>\n");
//        out.println("				<td><img src=\"" + web_Root + "/images/alt_transparent_button.jpg\" WIDTH=\"120\" alt=\"\"></td>\n");
//        out.println("				<td>American Liberator Technologies</td>\n");
//        out.println("			</tr>\n");
//        out.println("		</table>\n");
        
        
        out.println("		<center><h1>Organization : " + subscriber_Organization.getDescription().getValue() + "</h1></center>\n");
        out.println("		<center><h2>Please Select</h2></center>\n");
        out.println("\n");
        out.println("		<form id=\"login\" action=\"" + web_Root + "/LoginServlet\" method=\"post\">\n");
        out.println("           <input type=\"hidden\" name=\"subscriber_Organization_Id\" value=\"" + subscriber_Organization.getSubscriber_Organization_Id().getValue() + "\">\n");
        
        out.println("		<center>\n");
        out.println("			<table cellspacing=\"5\" border=\"0\">\n");
        out.println("			<tbody align=center >\n");
        out.println("                       <tr>\n");
        out.println("                           <td align=\"right\">Portal Name</td>\n");
        out.println("                           <td><input type=\"text\" name=\"subscriber_Organization_Portal_Name\"></td>\n");
        out.println("                       </tr>\n");
        out.println("                       <tr>\n");
        out.println("                           <td><input type=\"submit\" value=\"Select\"></td>\n");
        out.println("                       </tr>\n");
        out.println("                   </tbody>\n");
        
        out.println("			</table>\n");
        out.println("		</center>\n");
        out.println("		</form>\n");
        
        if(message != null && message.equals("")==false){
            out.println("    <font color=\"#FF0000\"><font size=4>\n");
            out.println("        <center><i>Error Message: " + message + "</i></center>\n");
            out.println("    </font></font>\n");
        }
        out.println("	</body>\n");
        out.println("</html>\n");
        
        
        out.close();
        
        return;
    }
    
    
    
    
    void show_Select_User_Page(HttpServletRequest request, HttpServletResponse response, Subscriber_Organization subscriber_Organization, Subscriber_Organization_Portal subscriber_Organization_Portal, String web_Root, String message)
    throws ServletException, IOException {
        
        
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        
        
        out.println("<html>\n");
        out.println("	<head>\n");



        out.println("<!--");
        out.println("");
        
        out.println("|       Copyright (c) 1985 thru 2002, 2003, 2004, 2005, 2006, 2007, 2008       |");
        out.println("|       Employees of American Liberator Technologies                           |");
        out.println("|       All Rights Reserved                                                    |");
        out.println("|                                                                              |");
        out.println("|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |");
        out.println("|       Employees of American Liberator Technologies                           |");
        out.println("|       The copyright notice above does not evidence any                       |");
        out.println("|       actual or intended publication of such source code.                    |");
        
        out.println("");
        out.println("-->");
        



        out.println("		<title> Organization : " + subscriber_Organization.getDescription().getValue() +" Portal Name : " + subscriber_Organization_Portal.getPortal_Name().getValue() + "  User Login Page</title>\n");
       
        
        String css_Root = subscriber_Organization_Portal.getCss_Root().toString();

        if(subscriber_Organization_Portal.getUse_Std_Css_Theme().getValue().booleanValue())
            out.println("   <link rel='stylesheet' type='text/css' href='" + web_Root + css_Root + "/STD.css' >");
        else
            out.println("   <link rel='stylesheet' type='text/css' href='" + web_Root + css_Root + "/" + subscriber_Organization_Portal.getCustom_Css_Theme_Name().toString() + ".css' >");
        
        out.println("       <link rel=\"stylesheet\" href=\"" + web_Root + "/IMPCMD/login.css\" type=\"text/css\">\n");

        out.println("	</head>\n");
        out.println("\n");
        out.println("    <body onload=\"document.login.user_Id.focus();\">\n");
        
        
//        out.println("		<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n");
//        out.println("			<tr>\n");
//        out.println("				<td><img src=\"" + web_Root + "/images/alt_transparent_button.jpg\" WIDTH=\"120\" alt=\"\"></td>\n");
//        out.println("				<td>American Liberator Technologies</td>\n");
//        out.println("			</tr>\n");
//        out.println("		</table>\n");
        
        
        out.println("		<center><h1>Organization : " + subscriber_Organization.getDescription().getValue() + "</h1></center>\n");
        out.println("		<center><h1>Portal Name  : " + subscriber_Organization_Portal.getPortal_Name().getValue() + "</h1></center>\n");
        out.println("		<center><h2>Please Login</h2></center>\n");
        out.println("\n");
        out.println("		<form id=\"login\" action=\"" + web_Root + "/LoginServlet\" method=\"post\">\n");
        out.println("               <input type=\"hidden\" name=\"subscriber_Organization_Id\" value=\"" + subscriber_Organization.getSubscriber_Organization_Id().getValue() + "\">\n");
        out.println("               <input type=\"hidden\" name=\"subscriber_Organization_Portal_Name\" value=\"" + subscriber_Organization_Portal.getPortal_Name().getValue() + "\">\n");
        out.println("		    <center>\n");
        out.println("			<table cellspacing=\"5\" border=\"0\">\n");
        out.println("			<tbody align=center >\n");
        
        out.println("				<tr>\n");
        out.println("					<td align=\"right\">User ID</td>\n");
        String user_Id = request.getParameter("user_Id");
        if(user_Id== null){
            out.println("					<td><input type=\"text\" name=\"user_Id\"></td>\n");
        }else{
            out.println("					<td><input type=\"text\" name=\"user_Id\" value=\"" + user_Id + "\" ></td>\n");
        }
        out.println("				</tr>\n");
        
        out.println("				<tr>\n");
        out.println("					<td align=\"right\">Password</td>\n");
        out.println("					<td><input type=\"password\" name=\"password\"></td>\n");
        out.println("				</tr>\n");
        out.println("				<tr>\n");
        out.println("					<td><input type=\"submit\" name=\"to_Do\" value=\"Login\"></td>\n");
        out.println("				</tr>\n");
        out.println("			</tbody>\n");
        out.println("			</table>\n");
        out.println("		</center>\n");
        out.println("		</form>\n");
        
        if(message != null && message.equals("")==false){
            out.println("    <font color=\"#FF0000\"><font size=4>\n");
            out.println("        <center><i>Error Message: " + message + "</i></center>\n");
            out.println("    </font></font>\n");
        }
        out.println("	</body>\n");
        out.println("</html>\n");
        
        
        out.close();
        
        return;
    }
    
    
    
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String message;
        message ="";
        session.setAttribute("s_message", message);
        
//        for (Enumeration e = request.getHeaderNames() ; e.hasMoreElements() ; ) {
//            String dmys = e.nextElement().toString();
//            System.out.println(dmys + " = " + request.getHeader(dmys));
//            for (Enumeration e2 = request.getHeaders(dmys) ; e2.hasMoreElements() ; ) {
//                String dmys2 = e2.nextElement().toString();
//                System.out.println("(2)" + dmys2 + " = " + request.getHeader(dmys2));
//
//            }
//        }
//        System.out.println("request.getContextPath() = " + request.getContextPath());
//        System.out.println("request.getPathInfo() = " + request.getPathInfo());
//        System.out.println("request.getPathTranslated() = " + request.getPathTranslated());
//        System.out.println("request.getQueryString() = " + request.getQueryString());
//        System.out.println("request.getRequestURI() = " + request.getRequestURI());
//        System.out.println("request.getRequestURL() = " + request.getRequestURL());
//        System.out.println("request.getServletPath() = " + request.getServletPath());
//        System.out.println("request.getLocalName() = " + request.getLocalName());
        
        
        
        String web_Root;
        
        web_Root = (String) session.getAttribute("s_web_Root");
        if(web_Root == null){
            session.setAttribute("s_web_Root", request.getContextPath());
            //session.setAttribute("s_web_Root", "/Web_Portal");
            web_Root = (String) session.getAttribute("s_web_Root");
        }
        
        //---------- get to_launch, argc, argvN ----------------------------------------------
        String to_Launch = request.getParameter("to_Launch");
        if(to_Launch != null){
            session.setAttribute("s_To_Launch", to_Launch);
        }
        
        String argc_Str = request.getParameter("argc");
        if(argc_Str != null){
            int argc = 0;
            try{
                argc =  new Integer(argc_Str).intValue();
            }catch(NumberFormatException nfe){
                argc=0;
            }
            if(argc>0){
                session.setAttribute("s_argc", new Integer(argc));
                for(int i=0; i<argc;i++){
                    String argvN = request.getParameter("argv"+i);
                    if(argvN != null){
                        session.setAttribute("s_argv"+i, argvN);
                    }
                }
            }
        }
        
        
        
        
        //========== get subscriber_Organization_Id ==========================================
        Subscriber_Organization i_Am_Subscriber_Organization=null;
        
        String subscriber_Organization_Id = request.getParameter("subscriber_Organization_Id");
        if(subscriber_Organization_Id== null){
            // no subscriber_Organization_Id  so ask for one
            message="";
            show_Select_Organization_Page(request, response, web_Root, message);
            return;
        }
        
        
        // validate subscriber_Organization_Id
        try {
            i_Am_Subscriber_Organization = Subscriber_Organization.get_Subscriber_Organization(subscriber_Organization_Id);
        }
        catch(Database_Front_Exception dbfe){
            log("LoginServlet: Database_Front_Exception:" + dbfe.getMessage() );
            show_Select_Organization_Page(request, response, web_Root, "Database error.");
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            log("LoginServlet: No Such Subscriber Organization Id :" + subscriber_Organization_Id + ": Database_Front_No_Results_Exception :" + dbfe.getMessage());
            show_Select_Organization_Page(request, response, web_Root, "No Such Subscriber Organization Id.");
            return;
        }
        catch(Database_Record_Constructor_Exception dbre){
            log("LoginServlet: Database_Record_Constructor_Exception:" + dbre.getMessage() );
            show_Select_Organization_Page(request, response, web_Root, "Database Record Error.");
            return;
        }
        
        
        Database_Front subscriber_Organization_Database_Front =new Database_Front(i_Am_Subscriber_Organization.get_Client_App_Constants_Constants());
        
        try{
            subscriber_Organization_Database_Front.open();
        }
        catch(Database_Front_Exception dbfe){
            message = "Failed to open "+ i_Am_Subscriber_Organization.getDatabase_Name() +
            " (which is this Subscriber Organization's database_Front), because :" + dbfe.getMessage() + ".";
            log("LoginServlet: " + message);
            show_Select_Organization_Page(request, response, web_Root, message);
            return;
        }
        try {
            subscriber_Organization_Database_Front.close();
        }
        catch(Database_Front_Exception dbfe){
            log("LoginServlet: Database_Front_Exception on close of database :" + dbfe.getMessage());
            show_Select_Organization_Page(request, response, web_Root, "Database Close Error.");
            return;
        }
        
        i_Am_Subscriber_Organization.set_Session_Instance(session);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        //========== get subscriber_Organization_Portal_Name ==========================================
        Subscriber_Organization_Portal i_Am_Subscriber_Organization_Portal=null;
        
        String subscriber_Organization_Portal_Name = request.getParameter("subscriber_Organization_Portal_Name");
        if(subscriber_Organization_Portal_Name== null){
            // no subscriber_Organization_Portal_Name  so ask for one
            show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "");
            return;
        }
        
        
        
        
        subscriber_Organization_Database_Front =new Database_Front(i_Am_Subscriber_Organization.get_Client_App_Constants_Constants());
        
        try{
            subscriber_Organization_Database_Front.open();
        }
        catch(Database_Front_Exception dbfe){
            message = "Failed to open "+ i_Am_Subscriber_Organization.getDatabase_Name() +
            " (which is this Subscriber Organization's database_Front), because :" + dbfe.getMessage() + ".";
            log("LoginServlet: " + message);
            show_Select_Organization_Page(request, response, web_Root, message);
            return;
        }
        
        
        
        
        
        // validate subscriber_Organization_Portal_Name
        try {
            i_Am_Subscriber_Organization_Portal = Subscriber_Organization_Portal.get_Subscriber_Organization_Portal(i_Am_Subscriber_Organization, subscriber_Organization_Portal_Name, subscriber_Organization_Database_Front);
        }
        catch(Database_Front_Exception dbfe){

            log("LoginServlet: Database_Front_Exception:" + dbfe.getMessage() );
            try {
                    subscriber_Organization_Database_Front.close();
                }
                catch(Database_Front_Exception dbfe2){
                    log("LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                }
            show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "Database Record Error.");
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){

            log("LoginServlet: No such Portal Name :" + subscriber_Organization_Portal_Name + ":  Database_Front_No_Results_Exception :" + dbfe.getMessage() );
            try {
                    subscriber_Organization_Database_Front.close();
                }
                catch(Database_Front_Exception dbfe2){
                    log("LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                }
            show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "No such Portal Name.");
            return;
        }
        
        catch(Database_Record_Constructor_Exception dbre){

            log("LoginServlet: Database_Record_Constructor_Exception:" + dbre.getMessage() );
            try {
                    subscriber_Organization_Database_Front.close();
                }
                catch(Database_Front_Exception dbfe){
                    log("LoginServlet: Database_Front_Exception on close of database :" + dbfe.getMessage());
                }
            show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "Database Record Error.");
            return;
        }
        
        i_Am_Subscriber_Organization_Portal.set_Session_Instance(session);
        
        
        
        
        
        
        
        
             
        String login_Method = i_Am_Subscriber_Organization_Portal.getLogin_Method().getValue();
        
        log("LoginServlet: login_Method =" + login_Method);
        if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_SYSTEM_USER])){
            
            
            //========== get user and password ==========================================
            
            
            User i_Am_User=null;
            
            
            
            String user_Id = request.getParameter("user_Id");
            if(user_Id== null){
                try {
                    subscriber_Organization_Database_Front.close();
                }
                catch(Database_Front_Exception dbfe){
                    log("LoginServlet: Database_Front_Exception on close of database :" + dbfe.getMessage());
                }
                // No user_Id passed to servlet  so ask for one
                message="";
                show_Select_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, message);
                return;
            }
            
            String password = request.getParameter("password");
            if(password== null){
                try {
                    subscriber_Organization_Database_Front.close();
                }
                catch(Database_Front_Exception dbfe){
                    log("LoginServlet: Database_Front_Exception on close of database :" + dbfe.getMessage());
                }
                // No password passed to servlet  so ask for one
                message="";
                show_Select_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, message);
                return;
            }
            
            
            
            
            User temp_User;
            try {
                temp_User = new User(user_Id);
            }catch(Database_Record_Constructor_Exception dbre){
                log("LoginServlet: Database_Record_Constructor_Exception:" + dbre.getMessage() );
                try {
                    subscriber_Organization_Database_Front.close();
                }
                catch(Database_Front_Exception dbfe){
                    log("LoginServlet: Database_Front_Exception on close of database :" + dbfe.getMessage());
                }
                show_Select_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Database Record Error.");
                return;
            }
            
            
            try {
                i_Am_User = (User) subscriber_Organization_Database_Front.getRecord(temp_User);
            }catch(Database_Front_Exception dbfe){
                log("LoginServlet: Database_Front_Exception:" + dbfe.getMessage() );
                try {
                    subscriber_Organization_Database_Front.close();
                }
                catch(Database_Front_Exception dbfe2){
                    log("LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                }
                show_Select_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Database Record Error.");
                return;
            }catch(Database_Front_No_Results_Exception dbfe){
                log("LoginServlet: No such User :" + user_Id + ":  Database_Front_No_Results_Exception :" + dbfe.getMessage() );
                try {
                    subscriber_Organization_Database_Front.close();
                }
                catch(Database_Front_Exception dbfe2){
                    log("LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                }
                show_Select_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login Failed.");
                return;
            }
            
            
            
            if(password.equals(i_Am_User.getPassword().getValue())  == false){
                //message = "Login failed. "+ "Wrong password";
                log("LoginServlet: User :" + user_Id + ":  wrong password :" + password);
                try {
                    subscriber_Organization_Database_Front.close();
                }
                catch(Database_Front_Exception dbfe){
                    log("LoginServlet: Database_Front_Exception on close of database :" + dbfe.getMessage());
                }
                show_Select_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login failed.");
                return;
            }
            if(i_Am_User.getObsolete().getValue().booleanValue()){
                log("LoginServlet: User :" + user_Id + ":  obsolete.");
                show_Select_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login failed.");
                return;
            }
            
            
            
            
            i_Am_User.set_Session_Instance(session);
            
            log(i_Am_User.getFirst_Name() + " " + i_Am_User.getLast_Name() + " Logged in.");
            
            
            
        } else if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_CUSTOMER])){
            getServletContext().getRequestDispatcher("/Outside_User_LoginServlet").forward(request, response);
            //log("LoginServlet: portal login method :" + login_Method + " not yet implimented.");
            //show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "LoginServlet: portal login method :" + login_Method + " not yet implimented.");
            return;
        } else if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_VENDOR])){
            getServletContext().getRequestDispatcher("/Vendor_LoginServlet").forward(request, response);
            //log("LoginServlet: portal login method :" + login_Method + " not yet implimented.");
            //show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "LoginServlet: portal login method :" + login_Method + " not yet implimented.");
            return;
        } else if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_TEACHER])){
            getServletContext().getRequestDispatcher("/Outside_User_LoginServlet").forward(request, response);
            return;
        } else if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_STUDENT])){
            getServletContext().getRequestDispatcher("/Student_LoginServlet").forward(request, response);
            return;
        
        } else if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_GUEST])){
            
            
            
            User i_Am_User=null;
            
            try {
                i_Am_User=new User("Guest");
                
                i_Am_User.setDescription("Guest User");
                i_Am_User.setLast_Name("Guest");
                i_Am_User.setFirst_Name("Guest");

                i_Am_User.setSecurity_Profile_Id("Guest");
                i_Am_User.setTimeZone_Name("CST6CDT");
		i_Am_User.setLogin_Method(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_GUEST]);
		                    
                
            }catch(Exception e){
                log("LoginServlet: " + e);
                show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "LoginServlet: " + e);
                return;
            }
            
            i_Am_User.set_Session_Instance(session);
            
            log(i_Am_User.getFirst_Name() + " " + i_Am_User.getLast_Name() + " Logged in.");
            
        } else {
            log("LoginServlet: portal has unknown login method :" + login_Method + ".");
            show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "LoginServlet: portal has unknown login method :" + login_Method + ".");
            return;
        }
        
        subscriber_Organization_Database_Front.set_Session_Instance(session);
        
        
        to_Launch = (String)session.getAttribute("s_To_Launch");
        if(to_Launch != null){
            String command = "/Launcher_Servlet?action=Start&command=/" + to_Launch + "&parent_Process_Id=0";
            session.removeAttribute("s_To_Launch");
            Integer argc_Int = (Integer)session.getAttribute("s_argc");
            if(argc_Int != null){
                for(int i=0; i<argc_Int.intValue();i++){
                    String argvN = (String)session.getAttribute("s_argv"+i);
                    if(argvN != null){
                        if(i%2 ==0){
                            command=command+"&"+argvN;
                        }else{
                            command=command+"="+argvN;
                        }
                        session.removeAttribute("s_argv"+i);
                    }
                }
                session.removeAttribute("s_argc");
            }
            getServletContext().getRequestDispatcher(command).forward(request, response);
            
            
        }else{
            
            getServletContext().getRequestDispatcher(i_Am_Subscriber_Organization_Portal.getDestination_Url().getValue()).forward(request, response);
        }
        return;
        
        
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
        return "Process Subscriber Organization, Portal Name, User and Password for login.";
    }
    
}
