/*
 * Web_Process.java
 *
 * Created on April 30, 2006
 */

package com.amlibtech.web_Processes;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.data.*;
import com.amlibtech.util.*;
import com.amlibtech.web.navigation.*;
import com.amlibtech.web.util.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Web_Process {
    private static long id_Counter = 1L;
    
    
    private long id;
    
    private long parent_Web_Process_Id;
    private Date    start_Date;
    private Date    last_Activity_Date;
    private int action_Count;
    
    private PrintWriter out;
    
    
    private String sessionId;
    
    private Subscriber_Organization subscriber_Organization;
    private Subscriber_Organization_Portal subscriber_Organization_Portal;
    private User user;
    
    private Database_Front database_Front;
    
    
    private String servlet_Path_Name;
    private String load_Options;
    
    private String reload_Options;
    
    private Subsystem subsystem;
    private Program program;
    
    
    private boolean failed;
    private String error_Message;
    private Exception error_Exception;
    
    private boolean waiting_For_Child;
    
    private LinkedList used_Attributes;
    //private LinkedList status_Stack;
    private WPStatus_Stack wpStatus_Stack;
    
    private HTML_Form_Tag html_Form_Tag;
    
    // set by get_For_Servlet() only
    private HttpServlet my_Servlet;
    private HttpServletRequest my_Request;
    private HttpServletResponse my_Response;
    
    private static final String[] Copyright = {
        "|       Copyright (c) 1985 thru 2001, 2002, 2003, 2004, 2005, 2006, 2007       |",
        "|       American Liberator Technologies                                        |",
        "|       All Rights Reserved                                                    |",
        "|                                                                              |",
        "|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |",
        "|       American Liberator Technologies                                        |",
        "|       The copyright notice above does not evidence any                       |",
        "|       actual or intended publication of such source code.                    |"
    };
    
    /** Creates a new instance of Web_Process */
    
    public Web_Process(long parent_Web_Process_Id, HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, String servlet_Path_Name, String load_Options)
    throws WPException {
        
        sync_Constructor(parent_Web_Process_Id, servlet, request, response, servlet_Path_Name, load_Options);
    }
    
    private synchronized void sync_Constructor(long parent_Web_Process_Id, HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, String servlet_Path_Name, String load_Options)
    throws WPException {
        
        
        Web_Process_Table.clean();
        
        this.id = Web_Process.next_Id_Counter();
        
        while(Web_Process_Table.id_In_Use(id))
            this.id = Web_Process.next_Id_Counter();
        
        
        
        Web_Process_Table.add(this);
        
        
        this.start_Date = Calendar.getInstance().getTime();
        this.last_Activity_Date = this.start_Date;
        this.action_Count=0;
        //        this.status_Stack = new LinkedList();
        //        this.status_Stack.add("constructed");
        
        this.wpStatus_Stack = new WPStatus_Stack();
        this.wpStatus_Stack.resetStatus_Stack(new WPStatus_Node("constructed"));
        
        this.parent_Web_Process_Id = parent_Web_Process_Id;
        
        HttpSession session = request.getSession();
        
        try {
            this.out = response.getWriter();
        }catch(IOException ioe){
            throw new WPException("IOException from response.getWriter().  " + ioe.getMessage());
        }
        
        servlet.log("Stating WP servlet :" + servlet_Path_Name + ":  id :" + this.id + ": session :" + session.getId() + ":  parent :" + this.parent_Web_Process_Id + ":");
        
        // get subscriber_Organization for this session
        try{
            subscriber_Organization = Subscriber_Organization.get_Session_Instance(session);
        }catch(Database_Record_Exception dbre){
            throw new WPException("No active Organization for this session.");
        }
        
        // get subscriber_Organization_Portal for this session
        try{
            subscriber_Organization_Portal = Subscriber_Organization_Portal.get_Session_Instance(session);
        }catch(Database_Record_Exception dbre){
            throw new WPException("No active portal for this session.");
        }
        
        // get user for this session
        try{
            user = User.get_Session_Instance(session);
        }catch(Database_Record_Exception dbre){
            throw new WPException("No active User for this session.");
        }
        
        // get database_Front for this session
        try {
            database_Front = Database_Front.get_Session_Instance(session);
        }catch(Database_Front_Exception dbfe){
            throw new WPException("No active database_Front for this session.");
        }
        
        
        this.servlet_Path_Name = servlet_Path_Name;
        this.load_Options = load_Options;
        this.reload_Options = "";
        
        
        try {
            this.program = Program.get_Program(servlet_Path_Name, database_Front);
        }catch(Database_Front_Exception dbfe){
            throw new WPException("Database Error. " + dbfe.getMessage());
        }catch(Database_Front_No_Results_Exception dbfe){
            throw new WPException("No such Program :" + servlet_Path_Name + ": found in database.");
        }catch(Database_Record_Constructor_Exception dbre){
            throw new WPException("Database Record error. " + dbre.getMessage());
        }
        
        
        try {
            this.subsystem = Subsystem.get_Subsystem(program.getSubsystem_Id().toString());
        }catch(Database_Front_Exception dbfe){
            throw new WPException("Database Error. " + dbfe.getMessage());
        }catch(Database_Front_No_Results_Exception dbfe){
            throw new WPException("No such Subsytem :" + program.getSubsystem_Id().toString() + ": found in database.");
        }catch(Database_Record_Constructor_Exception dbre){
            throw new WPException("Database Record error. " + dbre.getMessage());
        }
        
        subsystem_Installed_Test(program.getSubsystem_Id().toString());
        
        
        this.failed = false;
        this.error_Message = "";
        this.error_Exception = null;
        
        this.waiting_For_Child = false;
        
        this.used_Attributes = new LinkedList();
        
        this.html_Form_Tag = null;
        
        this.my_Servlet = servlet;
        this.my_Request = request;
        this.my_Response = response;
        this.sessionId = session.getId();
        
        
        
        
    }
    
    
    
    private static synchronized long next_Id_Counter(){
        id_Counter++;
        return id_Counter;
    }
    
    
    // loads and reload
    
    public void load_Menu()
    throws ServletException, IOException {
        my_Servlet.getServletContext().getRequestDispatcher(this.subscriber_Organization_Portal.getDestination_Url().getValue()).forward(my_Request, my_Response);
    }
    
    public void load()
    throws ServletException, IOException {
        my_Servlet.getServletContext().getRequestDispatcher(this.get_Html_Of_Load_Path() + "&action=Start").forward(my_Request, my_Response);
    }
    
    public void reload()
    throws ServletException, IOException {
        if(this.waiting_For_Child){
            // if has child process reload that
            Web_Process child_Web_Process = Web_Process_Table.find_A_Child_Process(this.id);
            if(child_Web_Process != null){
                child_Web_Process.reload();
                return;
            }else{
                // else clear waiting flag
                this.waiting_For_Child = false;
            }
        }
        my_Servlet.getServletContext().getRequestDispatcher(this.get_HTML_Of_Reload_Path() + "&action=Reload").forward(my_Request, my_Response);
        
    }
    
    
    
    public String get_HTML_Of_Href_Call_Parameters(){
        return "?ps_Id=" + this.id + "&ps_Step=" + this.action_Count;
    }
    
    public String get_Html_Of_Load_Path(){
        if(this.load_Options.length() != 0){
            return this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&" + this.load_Options;
        } else {
            return this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters();
        }
    }
    
    
    
    public String get_HTML_Of_Reload_Path(){
        if(this.reload_Options.length() != 0){
            return this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&" + this.reload_Options;
        } else {
            return this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters();
        }
    }
    
    
    
    
    
    
    
    public String getBase_URL_With_Context(){
        String base_URL_With_Context = this.my_Request.getRequestURL().toString().replaceFirst(this.my_Request.getServletPath() + "$", "");
        return base_URL_With_Context;
    }
    
    
    public String getBase_URL(){
        String base_URL_With_Context = getBase_URL_With_Context();
        String base_URL = base_URL_With_Context.replaceFirst(this.my_Request.getContextPath() + "$", "");
        return base_URL;
    }
    
    
    
    
    
    
    public synchronized void error_End(String message, Exception ex) {
        this.error_Message = message;
        this.error_Exception = ex;
        
        log("Fatal Error in " + servlet_Path_Name + ": " + message, ex);
        
        
        my_Response.setContentType("text/html");
        PrintWriter out;
        try {
            out = my_Response.getWriter();
        }catch(IOException ioe){
            log("web_Process.error_End(ex) IOException ", ioe);
            return;
        }
        
        
        if(this.subscriber_Organization_Portal.getKiosk_Mode().getValue().booleanValue()){
            
            
            
            out.println("<html>");
            
            out.println("<script type=\"text/JavaScript\">");
            
            
            out.println("location.replace(\"" + this.subscriber_Organization_Portal.getWeb_Root() +
            this.subscriber_Organization_Portal.getDestination_Url().getValue() +
            "\");");
            
            
            out.println("</script>");
            
            out.println("</html>");
            
        }else{
            
            this.getMy_Response().setContentType("text/html");
            
            this.do_Display_HTML_Page_Begin(out);
            
            this.do_Display_HTML_Head_Begin_Std(out, "ERROR END");
            this.do_Display_HTML_Head_End(out);
            
            this.do_Display_HTML_Body_Begin_Plain(out);
            
            
            out.println("<div id='main'>");
            
            
            out.println("       <p>Fatal Error in "+servlet_Path_Name+". <br>");
            out.println("       <p>"+StringPlus.html_Encode(message)+"<br>");
            out.println("       <p>Detail of Error:<br>");
            //        out.println("       <p>Status:"+(String)status_Stack.getLast()+":");
            out.println("       <p>Status:"+ this.wpStatus_Stack.getStatus_Node().getStatus_Text()+":");
            if(ex != null){
                out.println("       <p>"+StringPlus.html_Encode(ex.getMessage())+"<br>");
                out.println("       <p>Stack Trace:<br>");
                out.println("       <p>");
                StackTraceElement[] stackTraceElement = ex.getStackTrace();
                for(int i=0;i<stackTraceElement.length;i++){
                    out.println(StringPlus.html_Encode(stackTraceElement[i].toString()) + "<br>");
                }
                out.println("<br>");
            }else{
                out.println("<p> No Detail Available.<br>");
            }
            
            
            out.println("       <form id='error_end' method='post' action='" + this.getSubscriber_Organization_Portal().getWeb_Root()+
            "/Launcher_Servlet'>");
            out.println("           <input type=hidden name='action' value='End Task'>");
            out.println("           <input type=hidden name='process_Id' value='" + this.id + "'>");
            out.println("           <input type=hidden name='ps_Step' value='" + this.action_Count + "'>");
            
            out.println("           <input type=submit value='Press Here to Return to Session'>");
            out.println("       </form>");
            
            out.println("</div>");
            
            
            this.do_Display_HTML_Body_End(out);
            this.do_Display_HTML_Page_End(out);
            
            
            out.close();
            
            
            
        }
        
        return;
    }
    
    
    public synchronized void error_End(Exception ex) {
        this.error_End(ex.getMessage(), null);
    }
    
    
    public synchronized void error(String message, Exception ex) {
        this.error_Message = message;
        this.error_Exception = ex;
        
        log("Error in " + servlet_Path_Name + ": " + message, ex);
        
        
        my_Response.setContentType("text/html");
        PrintWriter out;
        try {
            out = my_Response.getWriter();
        }catch(IOException ioe){
            log("web_Process.error_End(ex) IOException ", ioe);
            return;
        }
        
        if(this.subscriber_Organization_Portal.getKiosk_Mode().getValue().booleanValue()){
            
            
            out.println("<html>");
            
            out.println("<script type=\"text/JavaScript\">");
            
            out.println("location.replace(\"" + this.subscriber_Organization_Portal.getWeb_Root() +
            this.subscriber_Organization_Portal.getDestination_Url().getValue() +
            "\");");
            
            
            out.println("</script>");
            
            out.println("</html>");
            
            
            
        } else {
            
            
            this.getMy_Response().setContentType("text/html");
            
            this.do_Display_HTML_Page_Begin(out);
            
            this.do_Display_HTML_Head_Begin_Std(out, "ERROR");
            
            out.println("<script type=\"text/JavaScript\" >");
            out.println("<!--");
            
            out.println("function init() { ");
            out.println("   MM_showHideLayers('error_detail', '', 'hide'); ");
            out.println("}");
            
            out.println("// -->");
            out.println(" </script>");
            
            this.do_Display_HTML_Head_End(out);
            
            this.do_Display_HTML_Body_Begin(out, "onload=init();");
            
            
            out.println("<div id='main'>");
            
            out.println("       <H1>Error in " + servlet_Path_Name + ".</H1><br>");
            out.println("       <H2>"+StringPlus.html_Encode(message)+"</H2><br>");
            
            
            out.println("       <form id='error' method='post' action='" + this.getSubscriber_Organization_Portal().getWeb_Root()+
            "/Launcher_Servlet'>");
            out.println("           <input type=hidden name='action' value='Reload'>");
            out.println("           <input type=hidden name='process_Id' value='" + this.id + "'>");
            out.println("           <input type=hidden name='ps_Step' value='" + this.action_Count + "'>");
            
            out.println("           <input type=submit value='Press Here to Return to Program'>");
            out.println("       </form><br>");
            
            
            out.println("<div id='error_no_detail'>");
            out.println("<input type=button value='Show Detail'  onclick=\"MM_showHideLayers('error_detail', '', 'show', 'error_no_detail', '', 'hide'); \" >");
            out.println("</div>");
            
            
            out.println("<div id='error_detail'>");
            
            out.println("<input type=button value='Hide Detail'  onclick=\"MM_showHideLayers('error_detail', '', 'hide', 'error_no_detail', '', 'show'); \" >");
            
            
            
            
            out.println("       <p>Detail of Error:<br>");
            if(ex != null){
                out.println("       <p>"+StringPlus.html_Encode(ex.getMessage())+"<br>");
                out.println("       <p>Stack Trace:<br>");
                out.println("       <p>");
                StackTraceElement[] stackTraceElement = ex.getStackTrace();
                for(int i=0;i<stackTraceElement.length;i++){
                    out.println(StringPlus.html_Encode(stackTraceElement[i].toString()) + "<br>");
                }
                out.println("<br>");
            }else{
                out.println("<p> No Detail Available.<br>");
            }
            out.println("</div>");
            
            out.println("</div>");
            
            
            this.do_Display_HTML_Body_End(out);
            this.do_Display_HTML_Page_End(out);
            
            
            out.close();
            
            
            
        }
        return;
    }
    
    
    
    public synchronized void error(Exception ex) {
        this.error(ex.getMessage(), ex);
    }
    
    
    
    public synchronized void notice(String message) {
        
        
        my_Response.setContentType("text/html");
        PrintWriter out;
        try {
            out = my_Response.getWriter();
        }catch(IOException ioe){
            log("web_Process.error_End(ex) IOException ", ioe);
            return;
        }
        
        this.getMy_Response().setContentType("text/html");
        
        this.do_Display_HTML_Page_Begin(out);
        
        this.do_Display_HTML_Head_Begin_Std(out, "Notice");
        this.do_Display_HTML_Head_End(out);
        
        this.do_Display_HTML_Body_Begin_Plain(out);
        
        
        out.println("<div id='main'>");
        
        out.println("       <H1>Notice from " + servlet_Path_Name + ".</H1><br>");
        out.println("       <H2>"+StringPlus.html_Encode(message)+"</H2><br>");
        
        
        out.println("       <form id='error' method='post' action='" + this.getSubscriber_Organization_Portal().getWeb_Root()+
        "/Launcher_Servlet'>");
        out.println("           <input type=hidden name='action' value='Reload'>");
        out.println("           <input type=hidden name='process_Id' value='" + this.id + "'>");
        out.println("           <input type=hidden name='ps_Step' value='" + this.action_Count + "'>");
        
        out.println("           <input type=submit value='Press Here to Return to Program'>");
        out.println("       </form>");
        
        out.println("</div>");
        
        
        this.do_Display_HTML_Body_End(out);
        this.do_Display_HTML_Page_End(out);
        
        
        out.close();
        
        return;
    }
    
    
    
    
    
    
    public synchronized void end() {
        
        log(":  call of :" + this.servlet_Path_Name + ": is ending.");
        
        
        for(int i = this.used_Attributes.size() - 1; i >= 0; i--){
            String name = (String)this.used_Attributes.get(i);
            removeAttribute(name);
        }
        
        Web_Process_Table.remove_Node(this);
        
        try {
            
            if(this.parent_Web_Process_Id != 0L){
                Web_Process parent_Web_Process;
                try {
                    parent_Web_Process = Web_Process.get_Instance_From_Table(my_Servlet, my_Request, my_Response, this.parent_Web_Process_Id);
                    parent_Web_Process.waiting_For_Child = false;
                    
                    parent_Web_Process.reload();
                }catch(WPException wpe) {
                    log("Web_Process.End():Can't find session instance of parent process ID#:" + this.parent_Web_Process_Id);
                }
            } else {
                load_Menu();
            }
        }catch(ServletException se){
            error_End("web_Process.end(): failed.", se);
            return;
        }catch(IOException ioe){
            error_End("web_Process.end(): failed.", ioe);
            return;
        }
        return;
    }
    
    
    public synchronized  void end_Whole_Stack_Load_Menu(){
        
        
        log(":  call of :" + this.servlet_Path_Name + ": is end_Whole_Stack.");
        
        Web_Process temp_Web_Process = this;
        
        
        boolean loop=true;
        while(loop){
            
            
            for(int i = temp_Web_Process.used_Attributes.size() - 1; i >= 0; i--){
                String name = (String)temp_Web_Process.used_Attributes.get(i);
                removeAttribute(name);
            }
            
            Web_Process_Table.remove_Node(temp_Web_Process);
            
            
            if(temp_Web_Process.parent_Web_Process_Id != 0L){
                Web_Process parent_Web_Process;
                try {
                    parent_Web_Process = Web_Process.get_Instance_From_Table(my_Servlet, my_Request, my_Response, temp_Web_Process.parent_Web_Process_Id);
                    parent_Web_Process.waiting_For_Child = false;
                    
                    temp_Web_Process=parent_Web_Process;
                }catch(WPException wpe) {
                    log("Web_Process.End():Can't find session instance of parent process ID#:" + temp_Web_Process.parent_Web_Process_Id);
                }
            } else {
                loop=false;
            }
        }
        
        try {
            load_Menu();
        }catch(ServletException se){
            error_End("web_Process.end(): failed.", se);
            return;
        }catch(IOException ioe){
            error_End("web_Process.end(): failed.", ioe);
            return;
        }
        
        
        return;
    }
    
    
    public synchronized void end_And_Launch_Task(String task, String options) {
        
        log(":end_And_Launch_Task():  call for :" + this.servlet_Path_Name + ".");
        
        String command = "";
        
        try {
            
            command = "/Launcher_Servlet?action=Start&command=/" + task + "&parent_Process_Id=0";
            if(options != null && !options.equals(""))
                command = command + options;
            log("command:" + command);
            this.my_Servlet.getServletContext().getRequestDispatcher(command).forward(this.my_Request, this.my_Response);
            
        }catch(ServletException se){
            error_End("web_Process.end_And_Launch_Task(): failed. Command :" + command + ":.", se);
            return;
        }catch(IOException ioe){
            error_End("web_Process.end_And_Launch_Task(): failed. Command :" + command + ":.", ioe);
            return;
        }
        
        
        log(":  call of :" + this.servlet_Path_Name + ": is abruptly ending via end_And_Launch_Task.");
        
        for(int i = this.used_Attributes.size() - 1; i >= 0; i--){
            String name = (String)this.used_Attributes.get(i);
            removeAttribute(name);
        }
        
        Web_Process_Table.remove_Node(this);
        
        return;
    }
    
    
    
    public synchronized void end_And_Link_Page(String link, String options) {
        
        log(":end_And_Link_Page():  call for :" + this.servlet_Path_Name + ".");
        
        log(":  call of :" + this.servlet_Path_Name + ": is abruptly ending via end_And_Link_Page.");
        
        
        for(int i = this.used_Attributes.size() - 1; i >= 0; i--) {
            String name = (String)this.used_Attributes.get(i);
            removeAttribute(name);
        }
        
        Web_Process_Table.remove_Node(this);
        
        String command = "";
        
        try {
            
            command = link;
            if(options != null && !options.equals(""))
                command = command + options;
            log("command:" + command);
            this.my_Servlet.getServletContext().getRequestDispatcher(command).forward(this.my_Request, this.my_Response);
            
            
        }catch(ServletException se){
            error_End("web_Process.end_And_Link_Page(): failed. Command :" + command + ":.", se);
            return;
        }catch(IOException ioe){
            error_End("web_Process.end_And_Link_Page(): failed. Command :" + command + ":.", ioe);
            return;
        }
        return;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String get_Process_Table_HTML_Table_Row(TimeZone tz) {
        StringBuffer result = new StringBuffer();
        result.append("<td>" + getSessionId() + "</td>");
        result.append("<td>" + this.subscriber_Organization.getName() + "</td>");
        result.append("<td>" + this.subscriber_Organization_Portal.getPortal_Name() + "</td>");
        result.append("<td>" + this.user.getFull_Name() + "</td>");
        result.append("<td>" + this.parent_Web_Process_Id + "</td>");
        result.append("<td>" + this.id + "</td>");
        result.append("<td>" + Calendar_Plus.get_Miliatry_HMS(this.start_Date, tz) + "</td>");
        result.append("<td>" + this.servlet_Path_Name + "</td>");
        result.append("<td>" + this.load_Options + "</td>");
        result.append("<td>" + Calendar_Plus.get_Miliatry_HMS(this.last_Activity_Date, tz) + "</td>");
        
        return result.toString();
    }
    
    public static String get_Process_Table_HTML_Table_Heading() {
        StringBuffer result = new StringBuffer();
        result.append("<th>SID</th>");
        result.append("<th>OrgID</th>");
        result.append("<th>PrtlID</th>");
        result.append("<th>User</th>");
        result.append("<th>PPID</th>");
        result.append("<th>PID</th>");
        result.append("<th>Start Time</th>");
        result.append("<th>Path</th>");
        result.append("<th>Options</th>");
        result.append("<th>Last Activity Time</th>");
        return result.toString();
    }
    
    
    
    
    
    public boolean isValid() {
        //    isRequestedSessionIdValid does not like something
        //    return my_Request.isRequestedSessionIdValid();
        // find a way to FIX this.
        return true;
    }
    
    
    public synchronized void remove() {
        log(":  call of :" + this.servlet_Path_Name + ": has expired.");
        
        for(int i = this.used_Attributes.size() - 1; i >= 0; i--) {
            String name = (String)this.used_Attributes.get(i);
            removeAttribute(name);
        }
        
        Web_Process_Table.remove_Node(this);
    }
    
    
    
    public synchronized void log(String message) {
        if(this.user == null) {
            this.my_Servlet.log("Process id :" + getId() + ": servlet_Path_Name :" + servlet_Path_Name + ": User :null"  + " : " + message);
        } else {
            this.my_Servlet.log("Process id :" + getId() + ": servlet_Path_Name :" + servlet_Path_Name + ": User :"  + this.user.getFull_Name() + " : " + message);
        }
    }
    
    
    public synchronized void log(String message, Exception ex) {
        if(ex == null) {
            log(message);
        } else {
            if(this.user == null) {
                this.my_Servlet.log("Process id :" + getId() + ": servlet_Path_Name :" + servlet_Path_Name + ": User :null"  + " : " + message, ex);
            } else {
                this.my_Servlet.log("Process id :" + getId() + ": servlet_Path_Name :" + servlet_Path_Name + ": User :"  + this.user.getFull_Name() + " : " + message, ex);
            }
        }
    }
    
    
    //WIERD
    void clean_If_Invalid(){
        if(!this.isValid()){
            this.remove();
        }
    }
    
    
    
    
    /**
     * Reads User from session attribute "Web_Process + ID#".
     * @return A Web_Process instance for selected ID#.
     * @throws WPException if the session in invalid or
     * if no such ID#.
     */
    public static synchronized Web_Process get_Instance_From_Table(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, long ps_Id)
    throws WPException {
        
        Web_Process temp_Web_Process;
        try {
            temp_Web_Process = Web_Process_Table.getWeb_Process_By_Id(ps_Id);
        }catch(WPException wpe) {
            throw wpe;
        }
        
        temp_Web_Process.my_Servlet = servlet;
        temp_Web_Process.my_Request = request;
        temp_Web_Process.my_Response = response;
        
        if(temp_Web_Process == null)
            throw new WPException("Null Web Process, ID#:" + ps_Id);
        
        if(!temp_Web_Process.isValid()){
            temp_Web_Process.remove();
            throw new WPException("This session is nolonger valid.");
        }
        
        try {
            temp_Web_Process.getDatabase_Front().reValidate_Connection();
        }catch(Database_Front_Exception dbfe) {
            throw new WPException("This session database connection has timed-out.");
        }
        
        
        return temp_Web_Process;
    }
    
    
    
    public static synchronized Web_Process  get_Instance_From_Table(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, String ps_Id)
    throws WPException {
        return get_Instance_From_Table(servlet, request, response, Long.decode(ps_Id).longValue());
    }
    
    
    
    
    
    
    
    
    public static synchronized Web_Process get_For_Servlet(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
    throws WPException {
        String message;
        
        String ps_Id = request.getParameter("ps_Id");
        if(ps_Id == null) {
            message = "No Process Id has been given to servlet.";
            throw new WPException(message);
        }
        
        String ps_Step = request.getParameter("ps_Step");
        if(ps_Step == null) {
            message = "No Process Step has been given to servlet.";
            throw new WPException(message);
        }
        
        Web_Process web_Process = Web_Process.get_Instance_From_Table(servlet, request, response, ps_Id);
        
        
        try {
            web_Process.out = web_Process.getMy_Response().getWriter();
        }catch(IOException ioe) {
            throw new WPException("IOException from response.getWriter().  " + ioe.getMessage());
        }
        
        
        
        if((new Integer(ps_Step)).intValue() != web_Process.getAction_Count()) {
            message = "Passed process step is out of sync with task.  This may be due to using the back button.";
            web_Process.error(message, null);
            if(!web_Process.getSubscriber_Organization_Portal().getKiosk_Mode().getValue().booleanValue()) {
                web_Process.log("Web_Process.get_For_Servlet():" + message);
                throw new WPException(message);
            }
        }
        return web_Process;
    }
    
    
    
    
    
    
    
    
    
    public void setAttribute(String name, Object obj) {
        this.used_Attributes.add(name);
        my_Request.getSession().setAttribute(this.id + "_" + name, obj);
    }
    
    
    public Object getAttribute(String name) {
        return my_Request.getSession().getAttribute(this.id + "_" + name);
    }
    
    public void removeAttribute(String name) {
        this.used_Attributes.remove(name);
        my_Request.getSession().removeAttribute(this.id + "_" + name);
    }
    
    
    
    
    
    
    
    
    
    
    
    public void subsystem_Installed_Test(String subsystem_Id)
    throws WPException {
        Subscriber_Organization_Portal_Subsystem temp_Subscriber_Organization_Portal_Subsystem = null;
        try {
            temp_Subscriber_Organization_Portal_Subsystem = new Subscriber_Organization_Portal_Subsystem(
            subscriber_Organization_Portal.getSubscriber_Organization_Id().getValue(),
            subscriber_Organization_Portal.getPortal_Name().getValue(),
            subsystem_Id);
        }catch(Database_Record_Constructor_Exception dbre) {
            throw new WPException("Database_Record Error :" + dbre.getMessage());
        }
        
        Subscriber_Organization_Portal_Subsystem subscriber_Organization_Portal_Subsystem;
        try {
            subscriber_Organization_Portal_Subsystem = (Subscriber_Organization_Portal_Subsystem)database_Front.getRecord(temp_Subscriber_Organization_Portal_Subsystem);
        }catch(Database_Front_Exception dbfe) {
            throw new WPException("Database_Front Error :" + dbfe.getMessage());
        }catch(Database_Front_No_Results_Exception dbfe) {
            throw new WPException(" Subscriber_Organization_Id :" + temp_Subscriber_Organization_Portal_Subsystem.getSubscriber_Organization_Id().toString() +
            ":  Portal_Name :" + temp_Subscriber_Organization_Portal_Subsystem.getPortal_Name().toString() +
            ":  Subsystem :" + temp_Subscriber_Organization_Portal_Subsystem.getSubsystem_Id().toString() +
            ":  is not installed.  Because:" + dbfe.getMessage());
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void do_Display_HTML_Page_Begin(PrintWriter out) {
        
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"");
        out.println(" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
        out.println("");
        out.println("<!-- " + servlet_Path_Name + " -->");
        out.println("");
    }
    
    public void do_Display_HTML_Page_End(PrintWriter out) {
        out.println("</html>");
    }
    
    
    public void do_Display_HTML_Head_Begin_Std(PrintWriter out, String title) {
        
        
        String css_Root = this.subscriber_Organization_Portal.getCss_Root().toString();
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        
        out.println("<head>");
        
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
        
        out.println("<title>" + title + "</title>");
        //out.println("   <link rel='stylesheet' type='text/css' href='" + web_Root + "/include/jsdomenu/themes/classic/classic.css' title='Classic' />");
        //out.println("   <link rel='alternate stylesheet' type='text/css' href='" + web_Root + "/include/jsdomenu/themes/office_xp/office_xp.css' title='Office XP' />");
        
        if(this.subscriber_Organization_Portal.getUse_Std_Css_Theme().getValue().booleanValue())
            out.println("   <link rel='stylesheet' type='text/css' href='" + web_Root + css_Root + "/STD.css' >");
        else
            out.println("   <link rel='stylesheet' type='text/css' href='" + web_Root + css_Root + "/" + this.subscriber_Organization_Portal.getCustom_Css_Theme_Name().toString() + ".css' >");
        
        
//        out.println("<script type='text/javascript' src='" + web_Root + "/include/jsdomenu/jsdomenu.js' ></script>");
//        out.println("<script type='text/javascript' src='" + web_Root + "/include/jsdomenu/jsdomenubar.js' ></script>");
//        out.println("<script type='text/javascript' src='" + web_Root + "/include/jsdomenu/jsdomenu.inc.js' ></script>");
//        out.println("<script type='text/javascript' src='" + web_Root + "/include/subsystem_Menus.js' ></script>");
//        out.println("<script type='text/JavaScript' src='" + web_Root + subscriber_Organization_Portal.getMenu_Js_Path() + "'></script>");
//        
//        out.println("<script type='text/javascript' src='" + web_Root + "/include/AutoComplete.js' ></script>");
//        out.println("<script type='text/javascript' src='" + web_Root + "/scripts/date_selector/date_selector.js'></script>");
//        out.println("<script type='text/javascript' src='" + web_Root + "/scripts/time_selector/time_selector.js'></script>");
//        out.println("<script type='text/JavaScript' src='" + web_Root + "/scripts/MM_findObj.js'></script>");
//        out.println("<script type='text/JavaScript' src='" + web_Root + "/scripts/MM_showHideLayers.js'></script>");
//        out.println("<script type='text/JavaScript' src='" + web_Root + "/scripts/My_Escape.js'></script>");
        
        //================================================================
        
        String impcmd_Js_Dir = web_Root+"/IMPCMD/Scripts";
        
        out.println("<script type='text/javascript' src='" + impcmd_Js_Dir + "/jsdomenu/jsdomenu.js' ></script>");
        out.println("<script type='text/javascript' src='" + impcmd_Js_Dir + "/jsdomenu/jsdomenubar.js' ></script>");
        out.println("<script type='text/javascript' src='" + impcmd_Js_Dir + "/jsdomenu/jsdomenu.inc.js' ></script>");
        
        out.println("<script type='text/javascript' src='" + impcmd_Js_Dir + "/AutoComplete.js' ></script>");
        out.println("<script type='text/JavaScript' src='" + impcmd_Js_Dir + "/date_selector/date_selector.js'></script>");
        out.println("<script type='text/JavaScript' src='" + impcmd_Js_Dir + "/time_selector/time_selector.js'></script>");
        out.println("<script type='text/JavaScript' src='" + impcmd_Js_Dir + "/MM_findObj.js'></script>");
        out.println("<script type='text/JavaScript' src='" + impcmd_Js_Dir + "/MM_showHideLayers.js'></script>");
        out.println("<script type='text/JavaScript' src='" + impcmd_Js_Dir + "/My_Escape.js'></script>");
                
        out.println("<script type='text/javascript' src='" + web_Root + "/include/subsystem_Menus.js' ></script>");
	
        // old software compatablity
        if(subscriber_Organization_Portal.getMenu_Js_Path().getValue().length()!=1){
		out.println("<script type='text/JavaScript' src='" + web_Root + subscriber_Organization_Portal.getMenu_Js_Path() + "'></script>");
	}

    }
    
    
    public void do_Display_HTML_Head_End(PrintWriter out){
        out.println("</head>");
    }
    
    
    public void do_Display_HTML_Body_Begin_With_Menu(PrintWriter out){
        out.println("<body onload='initjsDOMenu()'  >");
        
    }
    
    public void do_Display_HTML_Body_Begin_Plain(PrintWriter out){
        out.println("<body>");
    }
    public void do_Display_HTML_Body_Begin(PrintWriter out, String option){
        out.println("<body " + option + " >");
    }
    
    public void do_Display_HTML_Body_End(PrintWriter out){
        out.println("</body>");
        
    }
    
    public void do_Display_HTML_Ps_Id_As_Hidden_Input(PrintWriter out){
        out.println("<input type=hidden name='ps_Id'  value='" + this.id + "'></input>");
        out.println("<input type=hidden name='ps_Step' value='" + this.action_Count + "'>");
    }
    
    public void do_Display_HTML_Form_Begin_Std(PrintWriter out, String form_Name) throws WPException {
        if(this.html_Form_Tag != null)
            throw new WPException("Error! Trying to begin form_Name:" + form_Name + ":  While still in form_Name:" + this.html_Form_Tag.getForm_Name() + ":  Please correct program.");
        this.html_Form_Tag=new HTML_Form_Tag(form_Name, this.servlet_Path_Name.substring(1), out, false);
        this.html_Form_Tag.do_Display_HTML_Form_Begin_Std();
        do_Display_HTML_Ps_Id_As_Hidden_Input(out);
        
    }
    
    public void do_Display_HTML_Form_Begin_Multi_Part(PrintWriter out, String form_Name) throws WPException {
        if(this.html_Form_Tag != null)
            throw new WPException("Error! Trying to begin form_Name:" + form_Name + ":  While still in form_Name:" + this.html_Form_Tag.getForm_Name() + ":  Please correct program.");
        this.html_Form_Tag=new HTML_Form_Tag(form_Name, this.servlet_Path_Name.substring(1), out, true);
        this.html_Form_Tag.do_Display_HTML_Form_Begin_Std();
        do_Display_HTML_Ps_Id_As_Hidden_Input(out);
        
    }
    
    public void do_Display_HTML_Form_Begin_Href_Blank(PrintWriter out, String form_Name, String href) throws WPException {
        if(this.html_Form_Tag != null)
            throw new WPException("Error! Trying to begin form_Name:" + form_Name + ":  While still in form_Name:" + this.html_Form_Tag.getForm_Name() + ":  Please correct program.");
        this.html_Form_Tag=new HTML_Form_Tag(form_Name, href, out, false);
        this.html_Form_Tag.do_Display_HTML_Form_Begin_Href_Blank();
        do_Display_HTML_Ps_Id_As_Hidden_Input(out);
        
    }
    
    public void do_Display_HTML_Form_End() throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to end form, but no form exist.  Please correct program.");
        this.html_Form_Tag.do_Display_HTML_Form_End();
        this.html_Form_Tag=null;
        
    }
    
    public void do_Display_HTML_In_Form_Hidden_Input(String param_Name, String param_Value) throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add hidden input to form, but no form exist. param_Name :" + param_Name + ":   Value :" + param_Value + ":  Please correct program.");
        this.html_Form_Tag.do_Display_HTML_Hidden_Input(param_Name, param_Value);
    }
    
    
    
    
    public void do_Display_HTML_DF_Input_In_Form_With_Th_Tr(HTML_Table_Tag table_Tag, DFBase dfbase) throws WPException {
        do_Display_HTML_DF_Input_In_Form_With_Th_Tr("", table_Tag, dfbase, null);
    }
    
    public void do_Display_HTML_DF_Input_In_Form_With_Th_Tr(String prefix, HTML_Table_Tag table_Tag, DFBase dfbase) throws WPException {
        do_Display_HTML_DF_Input_In_Form_With_Th_Tr(prefix, table_Tag, dfbase, null);
    }
    
    public void do_Display_HTML_DF_Input_In_Form_With_Th_Tr(HTML_Table_Tag table_Tag, DFBase dfbase, String option) throws WPException {
        do_Display_HTML_DF_Input_In_Form_With_Th_Tr("", table_Tag, dfbase, option);
    }
    
    
    public void do_Display_HTML_DF_Input_In_Form_With_Th_Tr(String prefix, HTML_Table_Tag table_Tag, DFBase dfbase, String option) throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add input to form, but no form exist. Field:" + dfbase.getField_Name() + ":  Please correct program.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        
        table_Tag.do_Display_HTML_Row_Begin_Std();
        
        table_Tag.do_Display_HTML_TH_Element(dfbase.getField_Title());
        
        table_Tag.do_Display_HTML_TD_Element(dfbase.toHTML_Input(prefix, web_Root, this.html_Form_Tag.getForm_Name(), 0, option, ""));
        
        table_Tag.do_Display_HTML_Row_End();
    }
    
    
    
    public void do_Display_HTML_DF_Input_In_Form_With_Th_Tr(HTML_Table_Tag table_Tag, DFBase dfbase, int size) throws WPException {
        do_Display_HTML_DF_Input_In_Form_With_Th_Tr("", table_Tag, dfbase, size, null);
    }
    
    public void do_Display_HTML_DF_Input_In_Form_With_Th_Tr(String prefix, HTML_Table_Tag table_Tag, DFBase dfbase, int size) throws WPException {
        do_Display_HTML_DF_Input_In_Form_With_Th_Tr(prefix, table_Tag, dfbase, size, null);
    }
    
    public void do_Display_HTML_DF_Input_In_Form_With_Th_Tr(HTML_Table_Tag table_Tag, DFBase dfbase, int size, String option) throws WPException {
        do_Display_HTML_DF_Input_In_Form_With_Th_Tr("", table_Tag, dfbase, size, option);
    }
    
    public void do_Display_HTML_DF_Input_In_Form_With_Th_Tr(String prefix, HTML_Table_Tag table_Tag, DFBase dfbase, int size, String option) throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add input to form, but no form exist. Field:" + dfbase.getField_Name() + ":  Please correct program.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        
        table_Tag.do_Display_HTML_Row_Begin_Std();
        
        table_Tag.do_Display_HTML_TH_Element(dfbase.getField_Title());
        
        table_Tag.do_Display_HTML_TD_Element(dfbase.toHTML_Input(prefix, web_Root, this.html_Form_Tag.getForm_Name(), size, option, ""));
        
        table_Tag.do_Display_HTML_Row_End();
    }
    
    
    
    
    
    public void do_Display_HTML_DF_Input_In_Form_With_TD(HTML_Table_Tag table_Tag, DFBase dfbase) throws WPException {
        do_Display_HTML_DF_Input_In_Form_With_TD("", table_Tag, dfbase, "");
    }
    
    public void do_Display_HTML_DF_Input_In_Form_With_TD(HTML_Table_Tag table_Tag, DFBase dfbase, int size) throws WPException {
        do_Display_HTML_DF_Input_In_Form_With_TD("", table_Tag, dfbase, size, "");
    }
    
    public void do_Display_HTML_DF_Input_In_Form_With_TD(String prefix, HTML_Table_Tag table_Tag, DFBase dfbase, String option) throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add input to form, but no form exist. Field:" + dfbase.getField_Name() + ":  Please correct program.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        
        table_Tag.do_Display_HTML_TD_Element(dfbase.toHTML_Input(prefix, web_Root, this.html_Form_Tag.getForm_Name(), 0, option, ""));
    }
    
    public void do_Display_HTML_DF_Input_In_Form_With_TD(String prefix, HTML_Table_Tag table_Tag, DFBase dfbase, int size, String option) throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add input to form, but no form exist. Field:" + dfbase.getField_Name() + ":  Please correct program.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        
        table_Tag.do_Display_HTML_TD_Element(dfbase.toHTML_Input(prefix, web_Root, this.html_Form_Tag.getForm_Name(), size, option, ""));
    }
    
    
    
    
    public void do_Display_HTML_DF_Input_In_Form(DFBase dfbase) throws WPException {
        do_Display_HTML_DF_Input_In_Form("", dfbase, "");
    }
    
    public void do_Display_HTML_DF_Input_In_Form(DFBase dfbase, int size) throws WPException {
        do_Display_HTML_DF_Input_In_Form("", dfbase, size, "");
    }
    
    public void do_Display_HTML_DF_Input_In_Form(String prefix, DFBase dfbase, String option) throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add input to form, but no form exist. Field:" + dfbase.getField_Name() + ":  Please correct program.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        
        out.println(dfbase.toHTML_Input(prefix, web_Root, this.html_Form_Tag.getForm_Name(), 0, option, ""));
    }
    
    public void do_Display_HTML_DF_Input_In_Form(String prefix, DFBase dfbase, int size, String option) throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add input to form, but no form exist. Field:" + dfbase.getField_Name() + ":  Please correct program.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        
        out.println(dfbase.toHTML_Input(prefix, web_Root, this.html_Form_Tag.getForm_Name(), size, option, ""));
    }
    
    
    
    
    
    
    public void do_Display_HTML_DF_Display_With_Th(HTML_Table_Tag table_Tag, DFBase dfbase){
        
        table_Tag.do_Display_HTML_TH_Element(dfbase.getField_Title());
        table_Tag.do_Display_HTML_TD_Element(dfbase.toHTML());
        
    }
    
    
    
    
    public void do_Display_HTML_DF_Display_With_Th_Tr(HTML_Table_Tag table_Tag, DFBase dfbase){
        table_Tag.do_Display_HTML_Row_Begin_Std();
        
        table_Tag.do_Display_HTML_TH_Element(dfbase.getField_Title());
        table_Tag.do_Display_HTML_TD_Element(dfbase.toHTML());
        
        table_Tag.do_Display_HTML_Row_End();
    }
    
    
    public void do_Display_HTML_DF_Display_With_Th_Tr_Col_Span(HTML_Table_Tag table_Tag, DFBase dfbase, int col_Span){
        table_Tag.do_Display_HTML_Row_Begin_Std();
        
        table_Tag.do_Display_HTML_TH_Element(dfbase.getField_Title());
        table_Tag.do_Display_HTML_TD_Element_Col_Span(dfbase.toHTML(), col_Span);
        
        table_Tag.do_Display_HTML_Row_End();
    }
    
    public void do_Display_HTML_DF_DF_Display_With_Th_Tr(HTML_Table_Tag table_Tag, DFBase dfbase, DFBase dfbase2){
        table_Tag.do_Display_HTML_Row_Begin_Std();
        
        table_Tag.do_Display_HTML_TH_Element(dfbase.getField_Title());
        table_Tag.do_Display_HTML_TD_Element(dfbase.toHTML());
        
        table_Tag.do_Display_HTML_TH_Element(dfbase2.getField_Title());
        table_Tag.do_Display_HTML_TD_Element(dfbase2.toHTML());
        
        table_Tag.do_Display_HTML_Row_End();
    }
    
    public void do_Display_HTML_DF_DF_DF_Display_With_Th_Tr(HTML_Table_Tag table_Tag, DFBase dfbase, DFBase dfbase2, DFBase dfbase3){
        table_Tag.do_Display_HTML_Row_Begin_Std();
        
        table_Tag.do_Display_HTML_TH_Element(dfbase.getField_Title());
        table_Tag.do_Display_HTML_TD_Element(dfbase.toHTML());
        
        table_Tag.do_Display_HTML_TH_Element(dfbase2.getField_Title());
        table_Tag.do_Display_HTML_TD_Element(dfbase2.toHTML());
        
        table_Tag.do_Display_HTML_TH_Element(dfbase3.getField_Title());
        table_Tag.do_Display_HTML_TD_Element(dfbase3.toHTML());
        
        table_Tag.do_Display_HTML_Row_End();
    }
    
    
    
    
    public void do_Display_HTML_DF_Display_In_Table_As_Th(String prefix, HTML_Table_Tag table_Tag, DFBase dfbase){
        table_Tag.do_Display_HTML_TH_Element(prefix + dfbase.getField_Title());
    }
    
    public void do_Display_HTML_DF_Display_In_Table_As_Th_Width(String prefix, HTML_Table_Tag table_Tag, DFBase dfbase, int width){
        table_Tag.do_Display_HTML_TH_Element_Width(prefix + dfbase.getField_Title(), width);
    }
    
    
    public void do_Display_HTML_DF_Display_In_Table_As_Th(HTML_Table_Tag table_Tag, DFBase dfbase){
        table_Tag.do_Display_HTML_TH_Element(dfbase.getField_Title());
    }
    
    public void do_Display_HTML_DF_Display_In_Table_As_Th_Width(HTML_Table_Tag table_Tag, DFBase dfbase, int width){
        table_Tag.do_Display_HTML_TH_Element_Width(dfbase.getField_Title(), width);
    }
    
    public void do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(HTML_Table_Tag table_Tag, DFBase dfbase, Display_List_Sort_Manager display_List_Sort_Manager){
        table_Tag.do_Display_HTML_TH_Element(dfbase.getField_Title() +
        this.get_HTML_Of_Heading_Sort_Href(display_List_Sort_Manager, dfbase));
    }
    public void do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(String prefix, HTML_Table_Tag table_Tag, DFBase dfbase, Display_List_Sort_Manager display_List_Sort_Manager){
        table_Tag.do_Display_HTML_TH_Element(prefix + dfbase.getField_Title() +
        this.get_HTML_Of_Heading_Sort_Href(display_List_Sort_Manager, dfbase));
    }
    
    public void do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort_Width(HTML_Table_Tag table_Tag, DFBase dfbase, Display_List_Sort_Manager display_List_Sort_Manager, int width){
        table_Tag.do_Display_HTML_TH_Element_Width(dfbase.getField_Title() +
        this.get_HTML_Of_Heading_Sort_Href(display_List_Sort_Manager, dfbase), width);
    }
    
    
    
    
    
    
    public void do_Display_HTML_DF_Display_In_Table(HTML_Table_Tag table_Tag, DFBase dfbase){
        table_Tag.do_Display_HTML_TD_Element(dfbase.toHTML());
    }
    
    
    public void do_Display_HTML_DF_As_Hidden_Input(DFBase dfbase) throws WPException {
        String field_Name=dfbase.getField_Name();
        String param_Value = StringPlus.html_Encode(dfbase.toString());
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add DF as hidden input to form, but no form exist. Field Name :" + field_Name + ":  Please correct program.");
        this.html_Form_Tag.do_Display_HTML_Hidden_Input(field_Name, param_Value);
    }
    
    public void do_Display_HTML_DF_As_Hidden_Input(String prefix, DFBase dfbase) throws WPException {
        String field_Name=dfbase.getField_Name();
        String param_Value = StringPlus.html_Encode(dfbase.toString());
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add DF as hidden input to form, but no form exist. Field Name :" + field_Name + ":  Please correct program.");
        this.html_Form_Tag.do_Display_HTML_Hidden_Input(prefix + field_Name, param_Value);
    }
    
    
    
    
    
    
    
    
    
    
    public String DF_JS_Value_For_On_Event(String form_Name, DFBase dfbase) {
        return "document." + form_Name + "." + dfbase.getField_Name() + ".value)";
    }
    
    public String DF_JS_Value_For_On_Event(DFBase dfbase)
    throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add DF_Focus_Call_For_On_Event in a form. dfbase field Name :" + dfbase.getField_Name() + ":   Please correct program.");
        return "document." + this.html_Form_Tag.getForm_Name() + "." + dfbase.getField_Name() + ".value";
    }
    
    
    
    public String DF_Focus_Call_For_On_Event(String form_Name, DFBase dfbase) {
        return "document." + form_Name + "." + dfbase.getField_Name() + ".focus()";
    }
    
    public String DF_Focus_Call_For_On_Event(DFBase dfbase)
    throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add DF_Focus_Call_For_On_Event in a form. dfbase field Name :" + dfbase.getField_Name() + ":   Please correct program.");
        return "document." + this.html_Form_Tag.getForm_Name() + "." + dfbase.getField_Name() + ".focus()";
    }
    
    
    
    public String focus_Call_For_On_Event(String form_Name, String field_Name) {
        return "document." + form_Name + "." + field_Name + ".focus()";
    }
    
    public String focus_Call_For_On_Event(String field_Name)
    throws WPException {
        if(this.html_Form_Tag == null)
            throw new WPException("Error! Trying to add focus_Call_For_On_Event in a form.  Please correct program.");
        return "document." + this.html_Form_Tag.getForm_Name() + "." +  field_Name + ".focus()";
    }
    
    
    
    
    
    public String page_Show_Call_For_On_Event(String[] page_Names, int page_Number) {
        
        String answer="";
        
        int num_Pages=page_Names.length;
        int page=page_Number;
        
        answer=answer + "MM_showHideLayers(";
        for(int i = 0; i < num_Pages; i++){
            if(i!=0)
                answer=answer + ", ";
            answer=answer + "'page" + i + "','',";
            if(page == i){
                answer=answer + "'show'";
            }else
                answer=answer + "'hide'";
            
        }
        answer=answer + ");";
        
        return answer;
        
    }
    
    public String div_Show_Call_For_On_Event(String div_Name) {
        String answer="MM_showHideLayers('" + div_Name + "','','show');";
        return answer;
    }
    
    public String div_Hide_Call_For_On_Event(String div_Name) {
        String answer="MM_showHideLayers('" + div_Name + "','','hide');";
        return answer;
    }
    
    
    public void page_Button_Links_Do_Display(String[] page_Names, PrintWriter out) {
        
        out.println("<div id='page_buttons'><b>Pages: </b>");
        int num_Pages=page_Names.length;
        for(int page = 0; page < num_Pages; page++){
            
            out.println("<input type=button value='" + page_Names[page] + "'  onclick=\"MM_showHideLayers(");
            for(int i = 0; i < num_Pages; i++){
                if(i!=0)
                    out.print(", ");
                out.print("'page" + i + "','',");
                if(page == i){
                    out.print("'show'");
                }else
                    out.print("'hide'");
            }
            out.println(")\">");
            
        }
        out.println("</div>");
    }
    
    
    
    
    
    public synchronized void logout()
    throws ServletException, IOException {
        
        this.waiting_For_Child=true;
        
        String command= "/LogoutServlet";
        log("command:" + command);
        this.my_Servlet.getServletContext().getRequestDispatcher(command).forward(this.my_Request, this.my_Response);
        return;
    }
    
    
    
    public synchronized void launch_Task(String task, String options)
    throws ServletException, IOException {
        
        this.waiting_For_Child=true;
        
        String command= "/Launcher_Servlet?action=Start&command=/" + task + "&parent_Process_Id=" + this.id;
        if(options!=null && !options.equals(""))
            command=command + options;
        log("command:" + command);
        this.my_Servlet.getServletContext().getRequestDispatcher(command).forward(this.my_Request, this.my_Response);
        return;
    }
    
    
    public synchronized void link_Page(String link, String options)
    throws ServletException, IOException {
        
        this.waiting_For_Child=true;
        
        String command= link;
        if(options!=null && !options.equals(""))
            command=command + options;
        log("Link_Page:" + command);
	System.out.println("Link_Page:"+command+":");
        this.my_Servlet.getServletContext().getRequestDispatcher(link).forward(this.my_Request, this.my_Response);
        return;
    }
    
    
    //    public void do_Display_HTML_Launch_Task_Link(PrintWriter out, String task, String options, String text) {
    //        StringBuffer href= new StringBuffer();
    //
    //        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
    //        href.append("<a href='"  + web_Root + "/Launcher_Servlet?action=Start&command=/" + task +
    //        "&parent_Process_Id=" + this.id + options + "'>");
    //        href.append(text + "</a>");
    //        out.println(href.toString());
    //    }
    //
    //
    //
    //
    //    public void do_Display_HTML_Button_Launch_Task_Using_Form(PrintWriter out, String form_Name, String task, String options, String param_Value, String text)
    //    throws WPException {
    //        if(this.html_Form_Tag != null)
    //            throw new WPException("Error! Trying to add Button_Launch_Task_Using_Form in a form. task:" + task + ":  Please correct program.");
    //
    //        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
    //
    //        this.html_Form_Tag=new HTML_Form_Tag(form_Name, web_Root + "/Launcher_Servlet", out);
    //
    //        this.html_Form_Tag.do_Display_HTML_Form_Begin_Std();
    //
    //        this.html_Form_Tag.do_Display_HTML_Hidden_Input("action", "Start");
    //        this.html_Form_Tag.do_Display_HTML_Hidden_Input("command", "/"+task);
    //        this.html_Form_Tag.do_Display_HTML_Hidden_Input("parent_Process_Id", ""+this.id);
    //        this.html_Form_Tag.do_Display_HTML_Hidden_Input("options", options);
    //
    //
    //        this.html_Form_Tag.do_Display_HTML_Submit_Button(param_Value, text);
    //        do_Display_HTML_Form_End();
    //
    //    }
    
    
    
    
    
    
    
    
    
    public String get_HTML_Of_Heading_Sort_Href(Display_List_Sort_Manager display_List_Sort_Manager, DFBase dfBase){
        StringBuffer href= new StringBuffer();
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        String img_Dir = "IMPCMD/Images";
        String prefix = display_List_Sort_Manager.getPrefix();
        
        href.append("<a href='" + web_Root + "/" + servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=Reload&sort_Field_Name=" + prefix + dfBase.getField_Name() + "'>\n");
        if(display_List_Sort_Manager != null)
            href.append("   <img src='" + web_Root + "/" + img_Dir + display_List_Sort_Manager.getField_Direction_Image(dfBase.getField_Name()) + "' alt='Srt' border='0' align='right'>\n");
        
        href.append("   </a>");
        
        return(href.toString());
        
    }
    
    public void do_Process_Of_Sort_Manger_Request(Display_List_Sort_Manager display_List_Sort_Manager, Database_Record_Base database_Record_Base){
        
        String sort_Field_Name = this.getMy_Request().getParameter("sort_Field_Name");
        if(sort_Field_Name !=null){
            String field_Name = display_List_Sort_Manager.remove_Prefix_From_Field_Name(sort_Field_Name);
            if(database_Record_Base.is_Valid_Field_Name(field_Name)){
                display_List_Sort_Manager.sort_Field_CUD(sort_Field_Name);
            }
        }
    }
    
    
    
    
    
    
    
    
    
    public String display_List_Pagination_Manager_To_Html(Display_List_Pagination_Manager display_List_Pagination_Manager)
    throws WPException {
        if(this.html_Form_Tag != null)
            throw new WPException("Error! Trying to call display_List_Pagination_Manager_To_Html  While still in form_Name:" + this.html_Form_Tag.getForm_Name() + ":  Please correct program.");
        
        StringBuffer result = new StringBuffer();
        String prefix = display_List_Pagination_Manager.getPrefix();
        
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        String img_Dir = "IMPCMD/Images/buttons";

        
        result.append("<table class='Pagination_Buttons_Table' border='1' cellpadding='0' cellspacing='0' ><tbody><tr>\n");
        
        
        result.append("<td nowrap >&nbsp;<a  href='" + web_Root);
        
        result.append(this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=Reload&page=" + prefix + "first'> <img src='" + web_Root + "/" + img_Dir + "/page_first_pointer.png' border='0' alt='first page'><a/></td>\n");
        
        
        
        result.append("<td nowrap ><a  href='" + web_Root);
        
        result.append(this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=Reload&page=" + prefix + "prev'> <img src='" + web_Root + "/" + img_Dir + "/page_prev_pointer.png' border='0' alt='previous page'><a/></td>\n");
        
        
        
        result.append("<td nowrap ><form method='post' action='" + web_Root);
        
        result.append(this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "'>\n");
        
        result.append("<input type='hidden' name='action' value='Reload'></input>Page \n");
        
        
        result.append("<input type='text' name='" + prefix + "page' size=2 maxlength=4 value='" + display_List_Pagination_Manager.getCurrent_Page() + "'></input>&nbsp; of " + display_List_Pagination_Manager.getLast_Page_Number() + "\n");
        
        result.append("</form></td>\n");
        
        
        
        
        
        
        result.append("<td nowrap ><a  href='" + web_Root);
        
        result.append(this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=Reload&page=" + prefix + "next'> <img src='" + web_Root + "/" + img_Dir + "/page_next_pointer.png' border='0' alt='next page'><a/></td>\n");
        
        
        
        
        result.append("<td nowrap ><a  href='" + web_Root);
        
        result.append(this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=Reload&page=" + prefix + "last'> <img src='" + web_Root + "/" + img_Dir + "/page_last_pointer.png' border='0' alt='last page'><a/></td>\n");
        
        
        if(display_List_Pagination_Manager.getUser_Interface_Can_Change_Items_Per_Page()){
            
            
            result.append("<td nowrap ><form method='post'  action='" + web_Root);
            
            result.append(this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "'>\n");
            
            
            result.append("<input type='hidden' name='action' value='Reload'></input>\n");
            
            
            result.append("&nbsp; @\n");
            
            
            
            Integer[] per_Page_Values = { new Integer(10), new Integer(20), new Integer(30), new Integer(40), new Integer(100) };
            
            DFInteger_Enumerated per_Page=null;
            try{
                per_Page = new DFInteger_Enumerated(prefix + "per_page",  "Per Page",  per_Page_Values);
                per_Page.setValue(new Integer(display_List_Pagination_Manager.getItems_Per_Page()));
                result.append(per_Page.toHTML_Input(web_Root, ""));
            }
            catch(DFException dfe){
                result.append(dfe.getMessage());
                per_Page=null;
            }
            
            //result.append("<input type='text' name='per_page' size=2 maxlength=4 value='" + display_List_Pagination_Manager.getItems_Per_Page() + "'> per page.</input>\n");
            result.append("per Page<input type=submit value='Go'></input>");
            result.append("</form></td>\n");
            
        }
        
        result.append("</tr></tbody></table>\n");
        
        result.append("<p><br>");
        
        
        return result.toString();
        
    }
    
    
    
    
    
    public Display_List_Pagination_Manager provision_Display_List_Pagination_Manager(String attr_Name, Database_Record_Base dbrb, String where_SQL_Clause, int initial_Per_Page_Size)
    throws Database_Front_Exception {
        return provision_Display_List_Pagination_Manager("", attr_Name, dbrb, where_SQL_Clause, initial_Per_Page_Size);
    }
    
    public Display_List_Pagination_Manager provision_Display_List_Pagination_Manager(String prefix, String attr_Name, Database_Record_Base dbrb, String where_SQL_Clause, int initial_Per_Page_Size)
    throws Database_Front_Exception {
        
        Display_List_Pagination_Manager display_List_Pagination_Manager = (Display_List_Pagination_Manager)this.getAttribute(attr_Name);
        if(display_List_Pagination_Manager==null){
            int row_Count = 0;
            row_Count = this.getDatabase_Front().getCount_Of_Records(dbrb, where_SQL_Clause);
            display_List_Pagination_Manager = new Display_List_Pagination_Manager(prefix, initial_Per_Page_Size, row_Count);
            this.setAttribute(attr_Name, display_List_Pagination_Manager);
        }else{
            int row_Count = 0;
            row_Count = this.getDatabase_Front().getCount_Of_Records(dbrb, where_SQL_Clause);
            display_List_Pagination_Manager.setItems_In_Result_Set(row_Count);
            this.setAttribute(attr_Name, display_List_Pagination_Manager);
        }
        return display_List_Pagination_Manager;
    }
    
    
    
    
    
    
    
    
    /**
     * Getter for property id.
     * @return Value of property id.
     */
    public long getId() {
        return id;
    }
    
    /**
     * Setter for property id.
     * @param id New value of property id.
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Getter for property parent_Web_Process_Id.
     * @return Value of property parent_Web_Process_Id.
     */
    public long getParent_Web_Process_Id() {
        return parent_Web_Process_Id;
    }
    
    /**
     * Setter for property parent_Web_Process_Id.
     * @param parent_Web_Process_Id New value of property parent_Web_Process_Id.
     */
    public void setParent_Web_Process_Id(long parent_Web_Process_Id) {
        this.parent_Web_Process_Id = parent_Web_Process_Id;
    }
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Getter for property sessionId.
     * @return Value of property sessionId.
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }
    
    /**
     * Setter for property sessionId.
     * @param sessionId New value of property sessionId.
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }
    
    
    /**
     * Getter for property subscriber_Organization.
     * @return Value of property subscriber_Organization.
     */
    public com.amlibtech.login.data.Subscriber_Organization getSubscriber_Organization() {
        return subscriber_Organization;
    }
    
    /**
     * Setter for property subscriber_Organization.
     * @param subscriber_Organization New value of property subscriber_Organization.
     */
    public void setSubscriber_Organization(com.amlibtech.login.data.Subscriber_Organization subscriber_Organization) {
        this.subscriber_Organization = subscriber_Organization;
    }
    
    /**
     * Getter for property subscriber_Organization_Portal.
     * @return Value of property subscriber_Organization_Portal.
     */
    public com.amlibtech.login.data.Subscriber_Organization_Portal getSubscriber_Organization_Portal() {
        return subscriber_Organization_Portal;
    }
    
    /**
     * Setter for property subscriber_Organization_Portal.
     * @param subscriber_Organization_Portal New value of property subscriber_Organization_Portal.
     */
    public void setSubscriber_Organization_Portal(com.amlibtech.login.data.Subscriber_Organization_Portal subscriber_Organization_Portal) {
        this.subscriber_Organization_Portal = subscriber_Organization_Portal;
    }
    
    
    /**
     * Getter for property user.
     * @return Value of property user.
     */
    public com.amlibtech.login.data.User getUser() {
        return user;
    }
    
    /**
     * Setter for property user.
     * @param user New value of property user.
     */
    public void setUser(com.amlibtech.login.data.User user) {
        this.user = user;
    }
    
    
    
    
    
    
    
    /**
     * Getter for property servlet_Path_Name.
     * @return Value of property servlet_Path_Name.
     */
    public java.lang.String getServlet_Path_Name() {
        return servlet_Path_Name;
    }
    
    /**
     * Setter for property servlet_Path_Name.
     * @param servlet_Path_Name New value of property servlet_Path_Name.
     */
    public void setServlet_Path_Name(java.lang.String servlet_Path_Name) {
        this.servlet_Path_Name = servlet_Path_Name;
    }
    
    
    /**
     * Getter for property load_Options.
     * @return Value of property load_Options.
     */
    public java.lang.String getLoad_Options() {
        return load_Options;
    }
    
    /**
     * Setter for property load_Options.
     * @param load_Options New value of property load_Options.
     */
    public void setLoad_Options(java.lang.String load_Options) {
        this.load_Options = load_Options;
    }
    
    /**
     * Getter for property reload_Options.
     * @return Value of property reload_Options.
     */
    public java.lang.String getReload_Options() {
        return reload_Options;
    }
    
    /**
     * Setter for property reload_Options.
     * @param reload_Options New value of property reload_Options.
     */
    public void setReload_Options(java.lang.String reload_Options) {
        this.reload_Options = reload_Options;
    }
    
    
    /**
     * Getter for property failed.
     * @return Value of property failed.
     */
    public boolean isFailed() {
        return failed;
    }
    
    /**
     * Setter for property failed.
     * @param failed New value of property failed.
     */
    public void setFailed(boolean failed) {
        this.failed = failed;
    }
    
    /**
     * Getter for property error_Message.
     * @return Value of property error_Message.
     */
    public java.lang.String getError_Message() {
        return error_Message;
    }
    
    /**
     * Setter for property error_Message.
     * @param error_Message New value of property error_Message.
     */
    public void setError_Message(java.lang.String error_Message) {
        this.error_Message = error_Message;
    }
    
    
    /**
     * Getter for property waiting_For_Child.
     * @return Value of property waiting_For_Child.
     */
    public boolean isWaiting_For_Child() {
        return waiting_For_Child;
    }
    
    /**
     * Setter for property waiting_For_Child.
     * @param waiting_For_Child New value of property waiting_For_Child.
     */
    public void setWaiting_For_Child(boolean waiting_For_Child) {
        this.waiting_For_Child = waiting_For_Child;
    }
    
    /**
     * Getter for property database_Front.
     * @return Value of property database_Front.
     */
    public com.amlibtech.database.Database_Front getDatabase_Front() {
        return database_Front;
    }
    
    /**
     * Setter for property database_Front.
     * @param database_Front New value of property database_Front.
     */
    public void setDatabase_Front(com.amlibtech.database.Database_Front database_Front) {
        this.database_Front = database_Front;
    }
    
    /**
     * Getter for property error_Exception.
     * @return Value of property error_Exception.
     */
    public java.lang.Exception getError_Exception() {
        return error_Exception;
    }
    
    /**
     * Setter for property error_Exception.
     * @param error_Exception New value of property error_Exception.
     */
    public void setError_Exception(java.lang.Exception error_Exception) {
        this.error_Exception = error_Exception;
    }
    
    /**
     * Getter for property start_Date.
     * @return Value of property start_Date.
     */
    public java.util.Date getStart_Date() {
        return start_Date;
    }
    
    /**
     * Setter for property start_Date.
     * @param start_Date New value of property start_Date.
     */
    public void setStart_Date(java.util.Date start_Date) {
        this.start_Date = start_Date;
    }
    
    /**
     * Getter for property last_Activity_Date.
     * @return Value of property last_Activity_Date.
     */
    public java.util.Date getLast_Activity_Date() {
        return last_Activity_Date;
    }
    
    /**
     * Set property last_Activity_Daten to now
     */
    public void setLast_Activity_Date(java.util.Date last_Activity_Date) {
        this.last_Activity_Date = last_Activity_Date;
    }
    
    /**
     * Setter for property last_Activity_Date.
     * @param last_Activity_Date New value of property last_Activity_Date.
     */
    public void touch_Last_Activity_Date() {
        this.last_Activity_Date = Calendar.getInstance().getTime();
        
    }
    
    /**
     * Getter for property action_Count.
     * @return Value of property action_Count.
     */
    public int getAction_Count() {
        return action_Count;
    }
    
    /**
     * Setter for property action_Count.
     * @param action_Count New value of property action_Count.
     */
    public void setAction_Count(int action_Count) {
        this.action_Count = action_Count;
    }
    
    /**
     * Increment property action_Count.
     * @param action_Count New value of property action_Count.
     */
    public void Increment_Action_Count() {
        this.action_Count++;
        touch_Last_Activity_Date();
        this.error_Message="";
        this.error_Exception=null;
    }
    
    
    
    
    
    
    
    
    
    /**
     * Getter for property my_Servlet.
     * @return Value of property my_Servlet.
     */
    public javax.servlet.http.HttpServlet getMy_Servlet() {
        return my_Servlet;
    }
    
    
    
    /**
     * Getter for property my_Request.
     * @return Value of property my_Request.
     */
    public javax.servlet.http.HttpServletRequest getMy_Request() {
        return my_Request;
    }
    
    
    
    /**
     * Getter for property my_Response.
     * @return Value of property my_Response.
     */
    public javax.servlet.http.HttpServletResponse getMy_Response() {
        return my_Response;
    }
    
    
    
    
    
    
    /**
     * Getter for property program.
     * @return Value of property program.
     */
    public com.amlibtech.login.data.Program getProgram() {
        return program;
    }
    
    /**
     * Setter for property program.
     * @param program New value of property program.
     */
    public void setProgram(com.amlibtech.login.data.Program program) {
        this.program = program;
    }
    
    /**
     * Getter for property html_Form_Tag.
     * @return Value of property html_Form_Tag.
     */
    public HTML_Form_Tag getHtml_Form_Tag() {
        return html_Form_Tag;
    }
    
    
    /**
     * Getter for property out.
     * @return Value of property out.
     */
    public java.io.PrintWriter getOut() {
        return out;
    }
    
    /**
     * Setter for property out.
     * @param out New value of property out.
     */
    public void setOut(java.io.PrintWriter out) {
        this.out = out;
    }
    
    
    
    
    
    public void curStatus_Clear_Action_Stack(){
        this.getStatus_Node().clear_Action_Stack();
    }
    
    public void curStatus_Add_Action_Node(WPAction_Node wpAction_Node)
    throws WPFatal_Exception {
        //try {
            this.getStatus_Node().add_Action_Node(wpAction_Node);
        //}catch(WPFatal_Exception wpe){
            //this.error_End("Failed to add Action Node.", wpe);
            //throw new WPFatal_Exception("Failed to add Action Node.");
        //}
    }
    
    public void curStatus_Find_And_Invoke_Action(Web_Process web_Process, String action_Text)
    throws WPFatal_Exception {
        System.err.println("in Web_process.curStatus_Find_And_Invoke_Action() action_text="+action_Text);

        try {
            this.getStatus_Node().find_And_Invoke_Action(web_Process, action_Text);
        }catch(WPException wpe){
            this.error_End("Failed to invoke Action.", wpe);
            throw new WPFatal_Exception("Failed to invoke Action.");
        }
    }
    
    
    
    
    
    /* ==Begin NEW Submit button section ========================================================================================================================== */
    
    public void do_Display_HTML_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_Button_Using_Form(out, form_Name, param_Name, param_Value, button_Label, text, dfbase, 0, class_Obj, action_Method);
    }
    
    public void do_Display_HTML_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_Button_Using_Form(out, form_Name, param_Name, param_Value, button_Label, text, dfbase, 0, httpServlet, action_Method);
    }
    
    
    public void do_Display_HTML_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, int size, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_Button_Using_Form(out, form_Name, param_Name, param_Value, button_Label, text, dfbase, size, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, int size, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag != null)
            throw new WPFatal_Exception("Error! Trying to add DF_Button_Using_Form in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        
        try {
            do_Display_HTML_Form_Begin_Std(out, form_Name);
            
            
            out.println("<p>" + dfbase.getField_Title() + ":<br>");
            out.println(dfbase.toHTML_Input(web_Root, form_Name, size));
            this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
            do_Display_HTML_Form_End();
            
        }catch(WPException wpe){
            throw new WPFatal_Exception(wpe.getMessage());
        }
        this.curStatus_Add_Action_Node(new WPAction_Node(button_Label, class_Obj, action_Method));
        
    }
    
    
    
    public void do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, int size, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, form_Name, param_Name, param_Value, button_Label, text, dfbase, size, dbrb, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, int size, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag != null)
            throw new WPFatal_Exception("Error! Trying to add DF_Button_Using_Form_With_Hidden_DF in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        
        try {
            do_Display_HTML_Form_Begin_Std(out, form_Name);
            
            DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
            for(int i=0;i<dfbase_List.length;i++){
                do_Display_HTML_DF_As_Hidden_Input(dfbase_List[i]);
            }
            
            out.println("<p>" + dfbase.getField_Title() +":");
            out.println(dfbase.toHTML_Input(web_Root, form_Name, size));
            this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
            do_Display_HTML_Form_End();
        }catch(WPException wpe){
            throw new WPFatal_Exception(wpe.getMessage());
        }
        this.curStatus_Add_Action_Node(new WPAction_Node(button_Label, class_Obj, action_Method));
        
    }
    
    
    
    public void do_Display_HTML_DF_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, DFBase dfbase2, int size, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_DF_Button_Using_Form(out, form_Name, param_Name, param_Value, button_Label, text, dfbase, dfbase2, size, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_DF_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, DFBase dfbase2, int size, Class class_Obj, String action_Method)
    throws  WPFatal_Exception {
        if(this.html_Form_Tag != null)
            throw new WPFatal_Exception("Error! Trying to add DF_Button_Using_Form_With_Hidden_DF in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        try {
            do_Display_HTML_Form_Begin_Std(out, form_Name);
            
            
            
            out.println("<p>" + dfbase.getField_Title() + ":");
            out.println(dfbase.toHTML_Input(web_Root, form_Name, size));
            
            out.println("<p>" + dfbase2.getField_Title() + ":");
            out.println(dfbase2.toHTML_Input(web_Root, form_Name, size));
            
            this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
            do_Display_HTML_Form_End();
        }catch(WPException wpe){
            throw new WPFatal_Exception(wpe.getMessage());
        }
        this.curStatus_Add_Action_Node(new WPAction_Node(button_Label, class_Obj, action_Method));
        
    }
    
    
    
    public void do_Display_HTML_DF_DF_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, DFBase dfbase2, int size, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_DF_Button_Using_Form_With_Hidden_DBR(out, form_Name, param_Name, param_Value, button_Label, text, dfbase, dfbase2, size, dbrb, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_DF_DF_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, DFBase dfbase2, int size, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws  WPFatal_Exception {
        if(this.html_Form_Tag != null)
            throw new WPFatal_Exception("Error! Trying to add DF_Button_Using_Form_With_Hidden_DF in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        try {
            do_Display_HTML_Form_Begin_Std(out, form_Name);
            
            DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
            for(int i=0;i<dfbase_List.length;i++){
                do_Display_HTML_DF_As_Hidden_Input(dfbase_List[i]);
            }
            
            out.println("<p>" + dfbase.getField_Title() + ":");
            out.println(dfbase.toHTML_Input(web_Root, form_Name, size));
            
            out.println("<p>" + dfbase2.getField_Title() + ":");
            out.println(dfbase2.toHTML_Input(web_Root, form_Name, size));
            
            this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
            do_Display_HTML_Form_End();
        }catch(WPException wpe){
            throw new WPFatal_Exception(wpe.getMessage());
        }
        this.curStatus_Add_Action_Node(new WPAction_Node(button_Label, class_Obj, action_Method));
        
    }
    
    
    
    public void do_Display_HTML_DF_DF_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, DFBase dfbase2, DFBase dfbase3, int size, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_DF_DF_Button_Using_Form(out, form_Name, param_Name, param_Value, button_Label, text, dfbase, dfbase2, dfbase3, size, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_DF_DF_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, DFBase dfbase2, DFBase dfbase3, int size, Class class_Obj, String action_Method)
    throws  WPFatal_Exception {
        if(this.html_Form_Tag != null)
            throw new WPFatal_Exception("Error! Trying to add DF_Button_Using_Form_With_Hidden_DF in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        try {
            do_Display_HTML_Form_Begin_Std(out, form_Name);
            
            
            out.println("<br>" + dfbase.getField_Title() + ":");
            out.println(dfbase.toHTML_Input(web_Root, form_Name, size));
            
            out.println("<br>" + dfbase2.getField_Title() + ":");
            out.println(dfbase2.toHTML_Input(web_Root, form_Name, size));
            
            out.println("<br>" + dfbase3.getField_Title() + ":");
            out.println(dfbase3.toHTML_Input(web_Root, form_Name, size));
            
            out.println("<br>");
            
            this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
            do_Display_HTML_Form_End();
        }catch(WPException wpe){
            throw new WPFatal_Exception(wpe.getMessage());
        }
        this.curStatus_Add_Action_Node(new WPAction_Node(button_Label, class_Obj, action_Method));
        
    }
    
    
    
    public void do_Display_HTML_DF_DF_DF_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, DFBase dfbase2, DFBase dfbase3, int size, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_DF_DF_Button_Using_Form_With_Hidden_DBR(out, form_Name, param_Name, param_Value, button_Label, text, dfbase, dfbase2, dfbase3, size, dbrb, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_DF_DF_DF_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, DFBase dfbase2, DFBase dfbase3, int size, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws  WPFatal_Exception {
        if(this.html_Form_Tag != null)
            throw new WPFatal_Exception("Error! Trying to add DF_Button_Using_Form_With_Hidden_DF in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
        try {
            do_Display_HTML_Form_Begin_Std(out, form_Name);
            
            DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
            for(int i=0;i<dfbase_List.length;i++){
                do_Display_HTML_DF_As_Hidden_Input(dfbase_List[i]);
            }
            
            out.println("<br>" + dfbase.getField_Title() + ":");
            out.println(dfbase.toHTML_Input(web_Root, form_Name, size));
            
            out.println("<br>" + dfbase2.getField_Title() + ":");
            out.println(dfbase2.toHTML_Input(web_Root, form_Name, size));
            
            out.println("<br>" + dfbase3.getField_Title() + ":");
            out.println(dfbase3.toHTML_Input(web_Root, form_Name, size));
            
            out.println("<br>");
            
            this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
            do_Display_HTML_Form_End();
        }catch(WPException wpe){
            throw new WPFatal_Exception(wpe.getMessage());
        }
        this.curStatus_Add_Action_Node(new WPAction_Node(button_Label, class_Obj, action_Method));
        
    }
    
    
    
    
    
    public void do_Display_HTML_In_Form_Submit_Button(String param_Name, String param_Value, String text, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_In_Form_Submit_Button(param_Name, param_Value, text, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_In_Form_Submit_Button(String param_Name, String param_Value, String text, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag == null)
            throw new WPFatal_Exception("Error! Trying to add submit button to form, but no form exist. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program.");
        this.html_Form_Tag.do_Display_HTML_Submit_Button(param_Name, param_Value, text);
        this.curStatus_Add_Action_Node(new WPAction_Node(param_Value, class_Obj, action_Method));
    }
    
    
    
    
    
    
    
    
    
    
    
    public void do_Display_HTML_In_Form_Submit_Image(String param_Name, String param_Value, String image_FileName, int width, int height, String background_Color, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_In_Form_Submit_Image(param_Name, param_Value,  image_FileName, width, height, background_Color, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_In_Form_Submit_Image(String param_Name, String param_Value, String image_FileName, int width, int height, String background_Color, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag == null)
            throw new WPFatal_Exception("Error! Trying to add submit image to form, but no form exist. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program.");
        this.html_Form_Tag.do_Display_HTML_Submit_Image(param_Name, param_Value, image_FileName, width, height, background_Color);
        this.curStatus_Add_Action_Node(new WPAction_Node(param_Value, class_Obj, action_Method));
    }
    
    
    
    public void do_Display_HTML_In_Form_Submit_Image_In_Table(String param_Name, String param_Value, HTML_Table_Tag table_Tag, String image_FileName, int width, int height, String background_Color, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_In_Form_Submit_Image_In_Table(param_Name, param_Value,  table_Tag, image_FileName, width, height, background_Color, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_In_Form_Submit_Image_In_Table(String param_Name, String param_Value, HTML_Table_Tag table_Tag, String image_FileName, int width, int height, String background_Color, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag == null)
            throw new WPFatal_Exception("Error! Trying to add submit image to form, but no form exist. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program.");
        this.html_Form_Tag.do_Display_HTML_Submit_Image_In_Table(param_Name, param_Value, table_Tag, image_FileName, width, height, background_Color);
        this.curStatus_Add_Action_Node(new WPAction_Node(param_Value, class_Obj, action_Method));
    }
    
    
    
    
    
    
    public void do_Display_HTML_In_Form_Confirm_Submit_Button_In_Table(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag,  HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_In_Form_Confirm_Submit_Button_In_Table(param_Name, param_Value, text, table_Tag, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_In_Form_Confirm_Submit_Button_In_Table(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag == null)
            throw new WPFatal_Exception("Error! Trying to add Confirm submit button to form, but no form exist. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program.");
        this.html_Form_Tag.do_Display_HTML_In_Form_Confirm_Submit_Button_In_Table(param_Name, param_Value, text, table_Tag);
        this.curStatus_Add_Action_Node(new WPAction_Node(param_Value, class_Obj, action_Method));
    }
    
    
    
    
    
    
    public void do_Display_HTML_In_Form_Submit_Button_In_Table(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_In_Form_Submit_Button_In_Table(param_Name, param_Value, text, table_Tag, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_In_Form_Submit_Button_In_Table(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag == null)
            throw new WPFatal_Exception("Error! Trying to add submit button to form, but no form exist. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program.");
        this.html_Form_Tag.do_Display_HTML_Submit_Button_In_Table(param_Name, param_Value, text, table_Tag);
        this.curStatus_Add_Action_Node(new WPAction_Node(param_Value, class_Obj, action_Method));
    }
    
    
    
    public void do_Display_HTML_In_Form_Submit_Button_In_Table_With_Hidden_Parameter(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag, String param_Name2, String param_Value2, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_In_Form_Submit_Button_In_Table_With_Hidden_Parameter(param_Name, param_Value, text, table_Tag, param_Name2, param_Value2, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_In_Form_Submit_Button_In_Table_With_Hidden_Parameter(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag, String param_Name2, String param_Value2, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag == null)
            throw new WPFatal_Exception("Error! Trying to add submit button to form, but no form exist. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program.");
        this.html_Form_Tag.do_Display_HTML_Submit_Button_In_Table_With_Hidden_Parameter(param_Name, param_Value, text, table_Tag, param_Name2, param_Value2);
        if(param_Name2.equals("action"))
            this.curStatus_Add_Action_Node(new WPAction_Node(param_Value2, class_Obj, action_Method));
        else
            this.curStatus_Add_Action_Node(new WPAction_Node(param_Value, class_Obj, action_Method));
    }
    
    
    
    
    
    
    
    public void do_Display_HTML_Action_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_Action_Button_Using_Form(out, form_Name, param_Name, param_Value, button_Label, text, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_Action_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag != null)
            throw new WPFatal_Exception("Error! Trying to add Action_Button_Using_Form in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
        try {
            do_Display_HTML_Form_Begin_Std(out, form_Name);
            
            this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
            do_Display_HTML_Form_End();
        }catch(WPException wpe){
            throw new WPFatal_Exception(wpe.getMessage());
        }
        this.curStatus_Add_Action_Node(new WPAction_Node(button_Label, class_Obj, action_Method));
    }
    
    
    
    
    
    public void do_Display_HTML_Action_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_Action_Button_Using_Form_With_Hidden_DBR(out, form_Name, param_Name, param_Value, button_Label, text, dbrb, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_Action_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        if(this.html_Form_Tag != null)
            throw new WPFatal_Exception("Error! Trying to add Action_Button_Using_Form in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
        try {
            do_Display_HTML_Form_Begin_Std(out, form_Name);
            
            DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
            for(int i=0;i<dfbase_List.length;i++){
                do_Display_HTML_DF_As_Hidden_Input(dfbase_List[i]);
            }
            
            this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
            do_Display_HTML_Form_End();
        }catch(WPException wpe){
            throw new WPFatal_Exception(wpe.getMessage());
        }
        this.curStatus_Add_Action_Node(new WPAction_Node(button_Label, class_Obj, action_Method));
    }
    
    
    public void do_Display_HTML_Href_Action_Button_Using_Form(PrintWriter out, String form_Name, String button_Label, String href)
    throws WPFatal_Exception {
        if(this.html_Form_Tag != null)
            throw new WPFatal_Exception("Error! Trying to add HREF Action_Button_Using_Form in a form. button_Label:" + button_Label +  ":   Please correct program  Try a outside a form.");
        try {
            do_Display_HTML_Form_Begin_Href_Blank(out, form_Name, href);
            
            this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label("", "", button_Label, "");
            do_Display_HTML_Form_End();
        }catch(WPException wpe){
            throw new WPFatal_Exception(wpe.getMessage());
        }
    }
    
    
    
    
    
    
    public void resetStatus_Stack(WPStatus_Node wPStatus_Node){
        this.wpStatus_Stack.resetStatus_Stack(wPStatus_Node);
        touch_Last_Activity_Date();
    }
    
    public WPStatus_Node getStatus_Node() {
        return this.wpStatus_Stack.getStatus_Node();
    }
    
    
    
    public void pushStatus_Node(WPStatus_Node wPStatus_Node) {
        this.wpStatus_Stack.pushStatus_Node(wPStatus_Node);
        touch_Last_Activity_Date();
    }
    
    
//    public void pushStatus_Wreload(String status_Text, HttpServlet httpServlet, String action_Method, String reload_Method)
//    throws WPFatal_Exception {
//        this.wpStatus_Stack.pushStatus_Node(new WPStatus_Node(status_Text /*, httpServlet, action_Method */));
//        this.curStatus_Add_Action_Node(new WPAction_Node("Reload", httpServlet, reload_Method));
//        touch_Last_Activity_Date();
//    }
    
    
    public void pushStatus_Wreload(String status_Text, HttpServlet httpServlet, String reload_Method)
    throws WPFatal_Exception {
        this.wpStatus_Stack.pushStatus_Node(new WPStatus_Node(status_Text));
        this.curStatus_Add_Action_Node(new WPAction_Node("Reload", httpServlet, reload_Method));
        touch_Last_Activity_Date();
    }
    
    
    public WPStatus_Node popStatus_Node(){
        touch_Last_Activity_Date();
        return this.wpStatus_Stack.popStatus_Node();
    }
    
    
    
    public void curStatus_SetBreadCrumb_Text(String breadCrumb_Text) {
        WPStatus_Node result= getStatus_Node();
        result.setBreadCrumb_Text(breadCrumb_Text);
    }
    
    
    public String[] getBreadCrumbs_Text(){
        return this.wpStatus_Stack.getBreadCrumbs_Text();
    }
    
    public String[] getStatuses_Text(){
        return this.wpStatus_Stack.getStatuses_Text();
    }
    
    
    
    
    
    
    
    
    public void do_Display_HTML_Img_Display_As_Link(HTML_Img_Tag img_Tag, String action, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_Img_Display_As_Link(img_Tag, action, httpServlet.getClass(),  action_Method);
    }
    
    public void do_Display_HTML_Img_Display_As_Link(HTML_Img_Tag img_Tag, String action, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        
        String href;
        
        href = "<a href='"
        + this.getSubscriber_Organization_Portal().getWeb_Root()
        + this.getServlet_Path_Name()
        + this.get_HTML_Of_Href_Call_Parameters()
        + "&action=" + StringPlus.http_Encode(action)
        + "'>";
        out.println(href);
        img_Tag.do_Display_HTML(this);
        out.println("</a>");
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    
    
    public void do_Display_HTML_Img_Display_As_Link_With_DBR(HTML_Img_Tag img_Tag, String action, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_Img_Display_As_Link_With_DBR(img_Tag, action, dbrb, httpServlet.getClass(), action_Method);
    }
    
    
    public void do_Display_HTML_Img_Display_As_Link_With_DBR(HTML_Img_Tag img_Tag, String action, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        
        
        String href;
        
        href = "<a href='"
        + this.getSubscriber_Organization_Portal().getWeb_Root()
        + this.getServlet_Path_Name()
        + this.get_HTML_Of_Href_Call_Parameters()
        + "&action=" + StringPlus.http_Encode(action);
        
        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
        for(int i=0;i<dfbase_List.length;i++){
            DFBase dmy_DFBase = dfbase_List[i];
            href = href + dfbase_List[i].toHREF_Parameter();
        }
        
        
        href = href + "'>";
        out.println(href);
        img_Tag.do_Display_HTML(this);
        out.println("</a>");
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    
    
    
    
    
    
    
    
    
    
    public void do_Display_HTML_Text_As_Link(String text, String action, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_Text_As_Link(text, action, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_Text_As_Link(String text, String action, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        String href;
        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + StringPlus.http_Encode(action);
        
        href = href + "'>" + text +"</a>";
        out.println(href);
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    
    public void do_Display_HTML_Text_As_Link_With_DBR(String text, String action, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_Text_As_Link_With_DBR(text, action, dbrb, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_Text_As_Link_With_DBR(String text, String action, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        String href;
        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + StringPlus.http_Encode(action);
        
        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
        for(int i=0;i<dfbase_List.length;i++){
            DFBase dmy_DFBase = dfbase_List[i];
            href = href + dfbase_List[i].toHREF_Parameter();
        }
        
        href = href + "'>" + text +"</a>";
        out.println(href);
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    
    
    
    public void do_Display_HTML_DF_Display_In_Table_As_Link(HTML_Table_Tag table_Tag, DFBase dfbase, String action, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_Display_In_Table_As_Link(table_Tag, dfbase, action, httpServlet.getClass(), action_Method);
    }
    public void do_Display_HTML_DF_Display_In_Table_As_Link(HTML_Table_Tag table_Tag, DFBase dfbase, String action, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        table_Tag.do_Display_HTML_TD_Element("<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + StringPlus.http_Encode(action) +
        dfbase.toHREF_Parameter() +
        "'>" + dfbase.toHTML() + "</a>");
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    
    
    
    public void do_Display_HTML_Text_In_Table_As_Link(HTML_Table_Tag table_Tag, String text, String action, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_Text_In_Table_As_Link(table_Tag, text, action, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_Text_In_Table_As_Link(HTML_Table_Tag table_Tag, String text, String action, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        String href;
        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + StringPlus.http_Encode(action);
        
        href = href + "'>" + text + "</a>";
        table_Tag.do_Display_HTML_TD_Element(href);
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    
    public void do_Display_HTML_Text_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, String text, String action, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_Text_In_Table_As_Link_With_DBR(table_Tag, text, action, dbrb, httpServlet.getClass(), action_Method);
    }
    
    public void do_Display_HTML_Text_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, String text, String action, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        String href;
        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + StringPlus.http_Encode(action);
        
        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
        for(int i=0;i<dfbase_List.length;i++){
            DFBase dmy_DFBase = dfbase_List[i];
            href = href + dfbase_List[i].toHREF_Parameter();
        }
        
        href = href + "'>" + text +"</a>";
        table_Tag.do_Display_HTML_TD_Element(href);
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    
    public void do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, DFBase dfbase, String action, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(table_Tag, dfbase, action, dbrb, httpServlet.getClass(), action_Method);
    }
    public void do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, DFBase dfbase, String action, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        String href;
        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + StringPlus.http_Encode(action);
        href = href + dfbase.toHREF_Parameter();
        
        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
        for(int i=0;i<dfbase_List.length;i++){
            DFBase dmy_DFBase = dfbase_List[i];
            href = href + dfbase_List[i].toHREF_Parameter();
        }
        
        href = href + "'>" + dfbase.toHTML() + "</a>";
        table_Tag.do_Display_HTML_TD_Element(href);
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    public void do_Display_HTML_DF_DF_Display_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, DFBase dfbase, DFBase dfbase2, String action, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_DF_Display_In_Table_As_Link_With_DBR(table_Tag, dfbase, dfbase2, action, dbrb, httpServlet.getClass(), action_Method);
    }
    public void do_Display_HTML_DF_DF_Display_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, DFBase dfbase, DFBase dfbase2, String action, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        String href;
        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + StringPlus.http_Encode(action);
        href = href + dfbase.toHREF_Parameter();
        href = href + dfbase2.toHREF_Parameter();
        
        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
        for(int i=0;i<dfbase_List.length;i++){
            DFBase dmy_DFBase = dfbase_List[i];
            href = href + dfbase_List[i].toHREF_Parameter();
        }
        
        href = href + "'>" + dfbase.toHTML() + "</td><td>" + dfbase2.toHTML() + "</a>";
        table_Tag.do_Display_HTML_TD_Element(href);
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    public void do_Display_HTML_DF_Hide_DF_Display_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, DFBase dfbase, DFBase dfbase2, String action, Database_Record_Base dbrb, HttpServlet httpServlet, String action_Method)
    throws WPFatal_Exception {
        do_Display_HTML_DF_Hide_DF_Display_In_Table_As_Link_With_DBR(table_Tag, dfbase, dfbase2, action, dbrb, httpServlet.getClass(), action_Method);
    }
    public void do_Display_HTML_DF_Hide_DF_Display_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, DFBase dfbase, DFBase dfbase2, String action, Database_Record_Base dbrb, Class class_Obj, String action_Method)
    throws WPFatal_Exception {
        String href;
        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + StringPlus.http_Encode(action);
        href = href + dfbase.toHREF_Parameter();
        href = href + dfbase2.toHREF_Parameter();
        
        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
        for(int i=0;i<dfbase_List.length;i++){
            DFBase dmy_DFBase = dfbase_List[i];
            href = href + dfbase_List[i].toHREF_Parameter();
        }
        
        href = href + "'>" +dfbase.toHTML() + "</a>";
        table_Tag.do_Display_HTML_TD_Element(href);
        this.curStatus_Add_Action_Node(new WPAction_Node(action, class_Obj, action_Method));
    }
    
    
    
    
    
    
    /* ==================================================================================================================== */
    
    
    
    
    
    
    
    
    /* ==Begin OLD Submit button section ========================================================================================================================== */
    
    //    public void do_Display_HTML_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase) throws WPException {
    //        do_Display_HTML_DF_Button_Using_Form(out, form_Name, param_Name, param_Value, button_Label, text, dfbase, 0);
    //    }
    //
    //    public void do_Display_HTML_DF_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, int size) throws WPException {
    //        if(this.html_Form_Tag != null)
    //            throw new WPException("Error! Trying to add DF_Button_Using_Form in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
    //        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
    //
    //        do_Display_HTML_Form_Begin_Std(out, form_Name);
    //
    //
    //        out.println("<p>" + dfbase.getField_Title() + ":");
    //        out.println(dfbase.toHTML_Input(web_Root, form_Name, size));
    //        this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
    //        do_Display_HTML_Form_End();
    //
    //    }
    //
    //
    //
    //
    //
    //    public void do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, int size, Database_Record_Base dbrb) throws WPException {
    //        if(this.html_Form_Tag != null)
    //            throw new WPException("Error! Trying to add DF_Button_Using_Form_With_Hidden_DF in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
    //        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
    //
    //        do_Display_HTML_Form_Begin_Std(out, form_Name);
    //
    //        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
    //        for(int i=0;i<dfbase_List.length;i++){
    //            do_Display_HTML_DF_As_Hidden_Input(dfbase_List[i]);
    //        }
    //
    //        out.println("<p>" + dfbase.getField_Title() + ":");
    //        out.println(dfbase.toHTML_Input(web_Root, form_Name, size));
    //        this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
    //        do_Display_HTML_Form_End();
    //
    //    }
    //
    //    public void do_Display_HTML_DF_DF_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, DFBase dfbase, DFBase dfbase2, int size, Database_Record_Base dbrb) throws WPException {
    //        if(this.html_Form_Tag != null)
    //            throw new WPException("Error! Trying to add DF_Button_Using_Form_With_Hidden_DF in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
    //        String web_Root = this.subscriber_Organization_Portal.getWeb_Root().toString();
    //
    //        do_Display_HTML_Form_Begin_Std(out, form_Name);
    //
    //        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
    //        for(int i=0;i<dfbase_List.length;i++){
    //            do_Display_HTML_DF_As_Hidden_Input(dfbase_List[i]);
    //        }
    //        out.println("<p>" + dfbase.getField_Title() + ":");
    //        out.println(dfbase.toHTML_Input(web_Root, form_Name, size));
    //
    //        out.println("<p>" + dfbase2.getField_Title() + ":");
    //        out.println(dfbase2.toHTML_Input(web_Root, form_Name, size));
    //        this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
    //        do_Display_HTML_Form_End();
    //
    //    }
    //
    //
    //
    //
    //
    //
    //    public void do_Display_HTML_In_Form_Submit_Button(String param_Name, String param_Value, String text) throws WPException {
    //        if(this.html_Form_Tag == null)
    //            throw new WPException("Error! Trying to add submit button to form, but no form exist. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program.");
    //        this.html_Form_Tag.do_Display_HTML_Submit_Button(param_Name, param_Value, text);
    //    }
    //
    //
    //
    //
    //
    //
    //
    //    public void do_Display_HTML_In_Form_Submit_Button_In_Table(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag) throws WPException {
    //        if(this.html_Form_Tag == null)
    //            throw new WPException("Error! Trying to add submit button to form, but no form exist. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program.");
    //        this.html_Form_Tag.do_Display_HTML_Submit_Button_In_Table(param_Name, param_Value, text, table_Tag);
    //    }
    //
    //
    //
    //
    //
    //
    //
    //    public void do_Display_HTML_Action_Button_Using_Form(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text) throws WPException {
    //        if(this.html_Form_Tag != null)
    //            throw new WPException("Error! Trying to add Action_Button_Using_Form in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
    //
    //        do_Display_HTML_Form_Begin_Std(out, form_Name);
    //
    //        this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
    //        do_Display_HTML_Form_End();
    //    }
    //
    //
    //
    //
    //
    //
    //
    //    public void do_Display_HTML_Action_Button_Using_Form_With_Hidden_DBR(PrintWriter out, String form_Name, String param_Name, String param_Value, String button_Label, String text, Database_Record_Base dbrb) throws WPException {
    //        if(this.html_Form_Tag != null)
    //            throw new WPException("Error! Trying to add Action_Button_Using_Form in a form. param_Name:" + param_Name + ":  param_Value:" + param_Value + ":   Please correct program  Try a In_Form_Submit_Button.");
    //
    //        do_Display_HTML_Form_Begin_Std(out, form_Name);
    //
    //        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
    //        for(int i=0;i<dfbase_List.length;i++){
    //            do_Display_HTML_DF_As_Hidden_Input(dfbase_List[i]);
    //        }
    //
    //        this.html_Form_Tag.do_Display_HTML_Submit_Button_With_Label(param_Name, param_Value, button_Label, text);
    //        do_Display_HTML_Form_End();
    //    }
    //
    //
    //    public java.lang.String getStatus() {
    //        return (String) status_Stack.getLast();
    //    }
    //
    //
    //    public void pushStatus(java.lang.String status) {
    //        String current_Status = (String)this.status_Stack.getLast();
    //        if(!status.equals(current_Status))
    //            this.status_Stack.add(status);
    //        touch_Last_Activity_Date();
    //
    //    }
    //
    //    public String popStatus(){
    //        String result= (String)this.status_Stack.getLast();
    //        this.status_Stack.removeLast();
    //        return result;
    //    }
    //
    //
    //
    //
    //
    //    public void do_Display_HTML_DF_Display_In_Table_As_Link(HTML_Table_Tag table_Tag, DFBase dfbase, String action){
    //        table_Tag.do_Display_HTML_TD_Element("<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() +
    //        this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + action +
    //        dfbase.toHREF_Parameter() +
    //        "'>" + dfbase.toHTML() + "</a>");
    //    }
    //
    //
    //    public void do_Display_HTML_Text_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, String text, String action, Database_Record_Base dbrb){
    //        String href;
    //        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + action;
    //        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
    //        for(int i=0;i<dfbase_List.length;i++){
    //            DFBase dmy_DFBase = dfbase_List[i];
    //            href = href + dfbase_List[i].toHREF_Parameter();
    //        }
    //        href = href + "'>" + text + "</a>";
    //        table_Tag.do_Display_HTML_TD_Element(href);
    //
    //    }
    //
    //    public void do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, DFBase dfbase, String action, Database_Record_Base dbrb){
    //        String href;
    //        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + action;
    //        href = href + dfbase.toHREF_Parameter();
    //        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
    //        for(int i=0;i<dfbase_List.length;i++){
    //            DFBase dmy_DFBase = dfbase_List[i];
    //            href = href + dfbase_List[i].toHREF_Parameter();
    //        }
    //        href = href + "'>" + dfbase.toHTML() + "</a>";
    //        table_Tag.do_Display_HTML_TD_Element(href);
    //
    //    }
    //
    //    public void do_Display_HTML_DF_DF_Display_In_Table_As_Link_With_DBR(HTML_Table_Tag table_Tag, DFBase dfbase, DFBase dfbase2, String action, Database_Record_Base dbrb){
    //        String href;
    //        href ="<a href='" +  this.subscriber_Organization_Portal.getWeb_Root() + this.servlet_Path_Name + this.get_HTML_Of_Href_Call_Parameters() + "&action=" + action;
    //        href = href + dfbase.toHREF_Parameter();
    //        href = href + dfbase2.toHREF_Parameter();
    //        DFBase[] dfbase_List = dbrb.getPrimary_Index_DFields();
    //        for(int i=0;i<dfbase_List.length;i++){
    //            DFBase dmy_DFBase = dfbase_List[i];
    //            href = href + dfbase_List[i].toHREF_Parameter();
    //        }
    //        href = href + "'>" + dfbase.toHTML()+"</td><td>"+dfbase2.toHTML()+"</a>";
    //        table_Tag.do_Display_HTML_TD_Element(href);
    //
    //    }
    //
    
    
    //    public void resetStatus(String status){
    //        this.status_Stack = new LinkedList();
    //        this.status_Stack.add(status);
    //    }
    
    
    
    /* ==================================================================================================================== */
    
    
    
}
