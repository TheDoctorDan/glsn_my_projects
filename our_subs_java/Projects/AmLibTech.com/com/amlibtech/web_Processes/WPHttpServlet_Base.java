/*
 * WPHttpServlet_Base.java
 *
 * Created on September 25, 2006, 9:13 PM
 */

package com.amlibtech.web_Processes;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public abstract class WPHttpServlet_Base extends HttpServlet {

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
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public final void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected final synchronized void processRequest(HttpServletRequest request, HttpServletResponse response)
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
        
               
        if(web_Process.getSubscriber_Organization_Portal().getKiosk_Mode().getValue().booleanValue()){
            web_Process.log(" Action: " + action);
        }
        
        web_Process.Increment_Action_Count();
        
        
        do_Status_Routing(web_Process, action);
        return;
        
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected final void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected final void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    public final synchronized void do_Pop_and_Status_Routing(Web_Process web_Process)
    {
	System.err.println("do_Pop_and_Status_Routing");
	System.err.println("do_Pop_and_Status_Routing:calling popStatus_Node()");
        web_Process.popStatus_Node();
	System.err.println("do_Pop_and_Status_Routing:back from popStatus_Node()");
	System.err.println("do_Pop_and_Status_Routing:calling do_Status_Routing() to Reload");
        do_Status_Routing(web_Process, "Reload");
	System.err.println("do_Pop_and_Status_Routing:back from do_Status_Routing() to Reload");
        return;
    }
    
    abstract public void do_Status_Routing(Web_Process web_Process, String action);
    
    //abstract public void action_Stack_Do_Action(Web_Process web_Process, String action);
    
    
        
    
    abstract public void std_Action_End_Task(Web_Process web_Process);
    
    abstract public void std_Action_End(Web_Process web_Process);
         
    abstract public void std_Action_NYI(Web_Process web_Process);
    
    abstract public String get_Program_Title();
    
    
}


//
//    public void do_Status_Routing(Web_Process web_Process, String action)
//    throws ServletException, IOException {
//        try {
//            
//            if(web_Process.getStatus_Node().getStatus_Text().equals("loading")){
//                
//                web_Process.resetStatus_Stack(new WPStatus_Node("exit"));
//                
//                if(action.equals("Start")){
//                    try {
//                        Security_Module.check_Query(web_Process);
//                    }
//                    catch(Security_Exception se){
//                        web_Process.error_End(se);
//                        return;
//                    }
//                    get_Comp_Do_Action_Start(web_Process);
//                    return;
//                    
//                }else if(action.equals("Reload")){
//                    try {
//                        Security_Module.check_Query(web_Process);
//                    }
//                    catch(Security_Exception se){
//                        web_Process.error_End(se);
//                        return;
//                    }
//                    get_Comp_Do_Action_Reload(web_Process);
//                    return;
//                    
//                }else if(action.equals("End Task")){
//                    web_Process.end();
//                    return;
//                }else{
//                    web_Process.error_End("Unknown action :"+action+": for Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
//                    return;
//                }
//                
//            } else if(web_Process.getStatus_Node().getStatus_Text().equals("exit")){
//                web_Process.end();
//                return;
//                
//                
//            }else if(web_Process.getStatus_Node().is_Actionable()){
//                try {
//                    web_Process.getStatus_Node().invoke(web_Process, action);
//                }
//                catch(WPException wpe){
//                    web_Process.error_End("Failed to invoke method", wpe);
//                    return;
//                }
//                return;
//                
//                
//            } else if(web_Process.getStatus_Node().getStatus_Text().equals("waiting_for_child")){
//                if(action.equals("Child Done")){
//                    String cps_Id = web_Process.getMy_Request().getParameter("cps_Id");
//                    if(cps_Id==null){
//                        web_Process.error("No Child Process Id(cps_Id) has been given to servlet.", null);
//                        return;
//                    }
//                }else if(action.equals("Check Child")){
//                    web_Process.error("Not yet implemented.", null);
//                    return;
//                    
//                }else if(action.equals("Reload")){
//                    web_Process.error("Not yet implemented.", null);
//                    return;
//                    
//                }else if(action.equals("End Task")){
//                    web_Process.error("Not yet implemented.", null);
//                    return;
//                    
//                }else{
//                    web_Process.error_End("Unknown action :"+action+": for Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
//                    return;
//                }
//                
//                
//            } else {
//                web_Process.error_End("Unknown Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
//                return;
//            }
//            
//            
//            
//            web_Process.error_End("Fell thru status test.", null);
//            return;
//        }catch(Database_Record_Constructor_Exception dbrce){
//            web_Process.error_End(dbrce);
//            return;
//        }
//    }
//    
//    
//    
//    
//    public void action_Stack_Do_Action(Web_Process web_Process, String action)
//    throws ServletException, IOException {
//        try {
//            web_Process.curStatus_Find_And_Invoke_Action(web_Process, action);
//        }catch(WPFatal_Exception wpe){
//            web_Process.error_End(wpe);
//            return;
//        }
//    }
//    
//    
//    
//    public void std_Action_End_Task(Web_Process web_Process)
//    throws ServletException, IOException {
//        web_Process.end();
//        return;
//    }
//    
//    public void std_Action_End(Web_Process web_Process)
//    throws ServletException, IOException {
//        do_Pop_and_Status_Routing(web_Process);
//        return;
//    }
//    
//    
//    public void std_Action_NYI(Web_Process web_Process)
//    throws ServletException, IOException {
//        web_Process.error("Not Yet Implimented.",null);
//        return;
//    }
//    
//    
    
