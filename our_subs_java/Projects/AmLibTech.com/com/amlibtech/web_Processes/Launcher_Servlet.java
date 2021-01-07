/*
 * Launcher_Servlet.java
 *
 * Created on May 4, 2006, 12:52 PM
 */

package com.amlibtech.web_Processes;

import com.amlibtech.database.*;
import com.amlibtech.login.data.*;
import com.amlibtech.util.*;
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
public class Launcher_Servlet extends HttpServlet {
    
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
    
//    public static void show_Fatal_Error(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, String passed_Message)
//    throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        
//        String local_Message = servlet.getClass().getName() + ": " + passed_Message;
//        session.setAttribute("s_message", local_Message);
//        servlet.log(local_Message);
//        servlet.getServletContext().getRequestDispatcher("/fatal_Error.jsp").forward(request, response);
//        return;
//    }
    
    
    
    public static void show_Fatal_Error(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, String message, Exception ex)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        Subscriber_Organization_Portal subscriber_Organization_Portal;
        // get subscriber_Organization_Portal for this session
        try{
            subscriber_Organization_Portal = Subscriber_Organization_Portal.get_Session_Instance(session);
        }
        catch(Database_Record_Exception dbre){
            subscriber_Organization_Portal=null;
        }
        
        if(subscriber_Organization_Portal==null){
            out.println("<html>");
                out.println("   <title>Laucher FATAL ERROR</title>");
                out.println("   <body>");
                out.println("       <p>Laucher FATAL Error :<br>");
                out.println("       <p>"+StringPlus.html_Encode(message)+"<br>");
                out.println("       <p>Detail of FATAL Error:<br>");
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
                out.println("No subscriber_Organization_Portal!");
                out.println("</body>");
                out.println("</html>");
                session.getServletContext().log("Laucher FATAL Error :" + message, ex);
        }else{
            
            if(subscriber_Organization_Portal.getKiosk_Mode().getValue().booleanValue()){
                out.println("<html>");
                
                out.println("<script type='text/JavaScript'>");
                
                
                out.println("location.replace(\""+subscriber_Organization_Portal.getWeb_Root() +
                subscriber_Organization_Portal.getDestination_Url().getValue()+
                "\");");
                
                
                out.println("</script>");
                
                out.println("</html>");
                 
                session.getServletContext().log("Laucher FATAL Error (Kiosk Mode):" + message, ex);

                
            }else{
                out.println("<html>");
                
                out.println("<head>");
                out.println("<title>Laucher FATAL ERROR</title>");

                
                out.println("<script type='text/JavaScript'>");
                out.println("<!--");
                
                
                out.println("function MM_findObj(n, d) {");
                out.println("    var p,i,x;");
                out.println("    if(!d)");
                out.println("        d=document;");
                out.println("    if((p=n.indexOf(\"?\"))>0 && parent.frames.length) {");
                out.println("        d=parent.frames[n.substring(p+1)].document;");
                out.println("        n=n.substring(0,p);");
                out.println("    }");
                out.println("    if(!(x=d[n])&&d.all)");
                out.println("        x=d.all[n];");
                out.println("    for (i=0; !x && i<d.forms.length; i++)");
                out.println("        x=d.forms[i][n];");
                out.println("    for(i=0; !x && d.layers && i<d.layers.length; i++)");
                out.println("        x=MM_findObj(n,d.layers[i].document);");
                out.println("    if(!x && d.getElementById)");
                out.println("        x=d.getElementById(n);");
                out.println("    return x;");
                out.println("}");


                out.println("function MM_showHideLayers() { ");
                out.println("    var i,p,v,obj,args=MM_showHideLayers.arguments;");
                out.println("    for (i=0; i<(args.length-2); i+=3)");
                out.println("        if ((obj=MM_findObj(args[i]))!=null) {");
                out.println("            v=args[i+2];");
                out.println("            if (obj.style) {");
                out.println("                obj=obj.style;");
                out.println("                v=(v=='show')?'visible':(v=='hide')?'hidden':v;");
                out.println("            }");
                out.println("            obj.visibility=v;");
                out.println("        }");
                out.println("}");
                
                out.println("function init() { ");
                out.println("   MM_showHideLayers('error_detail', '', 'hide'); ");
                out.println("}");
                
                out.println("// -->");
                out.println(" </script>");

                out.println("</head>");
                
                out.println("   <body onload=init(); >");
                out.println("       <p>Launcher FATAL Error :<br>");
                out.println("       <p>"+StringPlus.html_Encode(message)+"<br>");
                
                
                
                out.println("<form id='error' method='post' action='" + subscriber_Organization_Portal.getWeb_Root() + subscriber_Organization_Portal.getDestination_Url().getValue()+ "'>");
                out.println("<input type=submit value='Press Here to Return to Menu'>");
                out.println("</form>");
                
                                
                out.println("<div id='error_no_detail'>");
                out.println("<input type=button value='Show Detail'  onclick=\"MM_showHideLayers('error_detail', '', 'show', 'error_no_detail', '', 'hide'); \" >");
                out.println("</div>");


                out.println("<div id='error_detail'>");
                                
                out.println("<input type=button value='Hide Detail'  onclick=\"MM_showHideLayers('error_detail', '', 'hide', 'error_no_detail', '', 'show'); \" >");

                out.println("<p>Detail of Error:<br>");
                if(ex != null){
                    out.println("<p>"+StringPlus.html_Encode(ex.getMessage())+"<br>");
                    out.println("<p>Stack Trace:<br>");
                    out.println("<p>");
                    StackTraceElement[] stackTraceElement = ex.getStackTrace();
                    for(int i=0;i<stackTraceElement.length;i++){
                        out.println(StringPlus.html_Encode(stackTraceElement[i].toString()) + "<br>");
                    }
                    out.println("<br>");
                }else{
                    out.println("<p> No Detail Available.<br>");
                }
                out.println("</div>");
                
                out.println("</body>");
                out.println("</html>");
                                
                session.getServletContext().log("Laucher FATAL Error :" + message, ex);

            }
        }
        return;
    }
    
    
    public static void show_Error(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, String message, Exception ex)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        Subscriber_Organization_Portal subscriber_Organization_Portal;
        // get subscriber_Organization_Portal for this session
        try{
            subscriber_Organization_Portal = Subscriber_Organization_Portal.get_Session_Instance(session);
        }
        catch(Database_Record_Exception dbre){
            subscriber_Organization_Portal=null;
        }
        
        if(subscriber_Organization_Portal==null){
            out.println("<html>");
                out.println("   <title>Laucher ERROR</title>");
                out.println("   <body>");
                out.println("       <p>Laucher Error :<br>");
                out.println("       <p>"+StringPlus.html_Encode(message)+"<br>");
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
                out.println("No subscriber_Organization_Portal!");
                out.println("</body>");
                out.println("</html>");
                session.getServletContext().log("Laucher Error :" + message, ex);
        }else{
            
            if(subscriber_Organization_Portal.getKiosk_Mode().getValue().booleanValue()){
                out.println("<html>");
                
                out.println("<script type='text/JavaScript'");
                
                
                out.println("location.replace(\""+subscriber_Organization_Portal.getWeb_Root() +
                subscriber_Organization_Portal.getDestination_Url().getValue()+
                "\");");
                
                
                out.println("</script>");
                
                out.println("</html>");
                 
                session.getServletContext().log("Laucher Error :" + message, ex);

                
            }else{
                out.println("<html>");
                
                out.println("<head>");
                out.println("<title>Laucher ERROR</title>");

                
                out.println("<script type='text/JavaScript'>");
                out.println("<!--");
                
                
                out.println("function MM_findObj(n, d) {");
                out.println("    var p,i,x;");
                out.println("    if(!d)");
                out.println("        d=document;");
                out.println("    if((p=n.indexOf(\"?\"))>0 && parent.frames.length) {");
                out.println("        d=parent.frames[n.substring(p+1)].document;");
                out.println("        n=n.substring(0,p);");
                out.println("    }");
                out.println("    if(!(x=d[n])&&d.all)");
                out.println("        x=d.all[n];");
                out.println("    for (i=0; !x && i<d.forms.length; i++)");
                out.println("        x=d.forms[i][n];");
                out.println("    for(i=0; !x && d.layers && i<d.layers.length; i++)");
                out.println("        x=MM_findObj(n,d.layers[i].document);");
                out.println("    if(!x && d.getElementById)");
                out.println("        x=d.getElementById(n);");
                out.println("    return x;");
                out.println("}");


                out.println("function MM_showHideLayers() { ");
                out.println("    var i,p,v,obj,args=MM_showHideLayers.arguments;");
                out.println("    for (i=0; i<(args.length-2); i+=3)");
                out.println("        if ((obj=MM_findObj(args[i]))!=null) {");
                out.println("            v=args[i+2];");
                out.println("            if (obj.style) {");
                out.println("                obj=obj.style;");
                out.println("                v=(v=='show')?'visible':(v=='hide')?'hidden':v;");
                out.println("            }");
                out.println("            obj.visibility=v;");
                out.println("        }");
                out.println("}");
                
                out.println("function init() { ");
                out.println("   MM_showHideLayers('error_detail', '', 'hide'); ");
                out.println("}");
                
                out.println("// -->");
                out.println(" </script>");

                out.println("</head>");
                
                out.println("   <body onload=init(); >");
                out.println("       <p>Launcher Error :<br>");
                out.println("       <p>"+StringPlus.html_Encode(message)+"<br>");
                
                
                
                out.println("<form id='error' method='post' action='" + subscriber_Organization_Portal.getWeb_Root() + subscriber_Organization_Portal.getDestination_Url().getValue()+ "'>");
                out.println("<input type=submit value='Press Here to Return to Menu'>");
                out.println("</form>");
                
                                
                out.println("<div id='error_no_detail'>");
                out.println("<input type=button value='Show Detail'  onclick=\"MM_showHideLayers('error_detail', '', 'show', 'error_no_detail', '', 'hide'); \" >");
                out.println("</div>");


                out.println("<div id='error_detail'>");
                                
                out.println("<input type=button value='Hide Detail'  onclick=\"MM_showHideLayers('error_detail', '', 'hide', 'error_no_detail', '', 'show'); \" >");

                out.println("<p>Detail of Error:<br>");
                if(ex != null){
                    out.println("<p>"+StringPlus.html_Encode(ex.getMessage())+"<br>");
                    out.println("<p>Stack Trace:<br>");
                    out.println("<p>");
                    StackTraceElement[] stackTraceElement = ex.getStackTrace();
                    for(int i=0;i<stackTraceElement.length;i++){
                        out.println(StringPlus.html_Encode(stackTraceElement[i].toString()) + "<br>");
                    }
                    out.println("<br>");
                }else{
                    out.println("<p> No Detail Available.<br>");
                }
                out.println("</div>");
                
                out.println("</body>");
                out.println("</html>");
                    
                if(ex!=null)
                    session.getServletContext().log("Laucher Error :" + message, ex);
                else
                    session.getServletContext().log("Laucher Error :" + message);

            }
        }
        return;
    }
    
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String message="";
        
        //        Enumeration en_HeaderNames = request.getHeaderNames();
        //        while(en_HeaderNames.hasMoreElements()){
        //            String headerName = (String)en_HeaderNames.nextElement();
        //            String header = request.getHeader(headerName);
        //            System.out.println(headerName + " = " + header);
        //        }
        
        
        HttpSession session = request.getSession();

        
        //        if(!request.isRequestedSessionIdValid()){
        //            message = "This session has timed-out.";
        //            log(message);
        //            show_Error(this, request, response, message, null);
        //            return;
        //        }
        
        String action = request.getParameter("action");
        if(action==null){
            message = "No action has been given to servlet.";
            show_Error(this, request, response, message, null);
            return;
        }
        
        if(action.equals("Start")){
            User i_Am_User;
            try {
                i_Am_User = User.get_Session_Instance(session);
            }
            catch(Database_Record_Exception dbre){
                show_Fatal_Error(this, request, response, dbre.getMessage(), dbre);
            }
            
            String command = request.getParameter("command");
            if(command==null){
                message = "No command has been given to servlet.";
                show_Error(this, request, response, message, null);
                return;
            }
            
            String parent_Process_Id = request.getParameter("parent_Process_Id");
            if(parent_Process_Id==null){
                message = "No parent_Process_Id has been given to servlet.";
                show_Error(this, request, response, message, null);
                return;
            }
            
            long parent_Process_Id_Num;
            try {
                parent_Process_Id_Num = Long.decode(parent_Process_Id).longValue();
            }
            catch(NumberFormatException nfe){
                message = "Invalid parent_Process_Id has been given to servlet.";
                show_Error(this, request, response, message, nfe);
                return;
            }
            
            String options = request.getParameter("options");
            if(options==null){
                options="";
            }
            
            Web_Process web_Process;
            try {
                web_Process = new Web_Process(parent_Process_Id_Num, this, request, response, command, options);
            }
            catch(WPException wpe){
                message = "Failed to construct a new Web_Process. I cannot start task:"+command+". " + wpe.getMessage();
                show_Error(this, request, response, message, wpe);
                return;
            }
            
            //            web_Process.resetStatus("loading");
            web_Process.resetStatus_Stack(new WPStatus_Node("loading"));
            web_Process.load();
            return;
            
        }else if(action.equals("Reload")){
            
            
            String ps_Id = request.getParameter("process_Id");
            if(ps_Id==null){
                message = "No process_Id has been given to servlet.";
                show_Error(this, request, response, message, null);
                return;
            }
            
            long process_Id_Num;
            try {
                process_Id_Num = Long.decode(ps_Id).longValue();
            }
            catch(NumberFormatException nfe){
                message = "Invalid process_Id has been given to servlet.";
                show_Error(this, request, response, message, nfe);
                return;
            }
            
            Web_Process web_Process;
            try {
                web_Process = Web_Process.get_Instance_From_Table(this, request, response, ps_Id);
            }
            catch(WPException wpe){
                show_Error(this, request, response, wpe.getMessage(), wpe);
                return;
            }
            web_Process.reload();
            return;
            
            
            
        }else if(action.equals("End Task")){
            
            String ps_Id = request.getParameter("process_Id");
            if(ps_Id==null){
                message = "No process_Id has been given to servlet.";
                show_Error(this, request, response, message, null);
                return;
            }
            
            long process_Id_Num;
            try {
                process_Id_Num = Long.decode(ps_Id).longValue();
            }
            catch(NumberFormatException nfe){
                message = "Invalid process_Id has been given to servlet.";
                show_Error(this, request, response, message, nfe);
                return;
            }
            
            Web_Process web_Process;
            try {
                web_Process = Web_Process.get_Instance_From_Table(this, request, response, ps_Id);
            }
            catch(WPException wpe){
                show_Error(this, request, response, wpe.getMessage(), null);
                return;
            }
            web_Process.end();
            return;
            
        }else{
            message = "The action value :" + action + ": has been given to servlet.";
            show_Error(this, request, response, message, null);
            return;
        }
        
        
        
        //response.setContentType("text/html");
        //PrintWriter out = response.getWriter();
        /* TODO output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet</title>");
        out.println("</head>");
        out.println("<body>");
         
        out.println("</body>");
        out.println("</html>");
         */
        //out.close();
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
        return "Launcher Servlet Starts/ Cancels or Returns to commands   keeping/using the Web_Process_Table";
    }
    
}
