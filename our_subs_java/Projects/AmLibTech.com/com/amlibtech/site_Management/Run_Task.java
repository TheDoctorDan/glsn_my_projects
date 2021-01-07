/*
 * Run_Task.java
 *
 * Created on August 4, 2006, 3:00 PM
 */

package com.amlibtech.site_Management;

import com.amlibtech.database.*;
import com.amlibtech.login.data.*;
import com.amlibtech.web_Processes.*;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import com.amlibtech.security_Management.*;


/**
 *
 * @author  dgleeson
 * @version
 */
public class Run_Task extends WPHttpServlet_Base {

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
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Run A Task";
    }
    
    
    
    
    
    
    
    public void do_Status_Routing(Web_Process web_Process, String action) {
        
        
        
        if(web_Process.getStatus_Node().getStatus_Text().equals("loading")){
            
            web_Process.resetStatus_Stack(new WPStatus_Node("exit"));
            
            if(action.equals("Start")){
                try {
                    Security_Module.check_Query(web_Process);
                }
                catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                enter_Task_Do_Display(web_Process);
                return;
                
            }else if(action.equals("Reload")){
                try {
                    Security_Module.check_Query(web_Process);
                }
                catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                enter_Task_Do_Display(web_Process);
                return;
                
            }else if(action.equals("End Task")){
                web_Process.end();
                return;
            }else{
                web_Process.error_End("Unknown action :"+action+": for Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
                return;
            }
            
        } else if(web_Process.getStatus_Node().getStatus_Text().equals("exit")){
            web_Process.end();
            return;
            
            
        }else if(web_Process.getStatus_Node().is_Actionable()){
            try {
                web_Process.curStatus_Find_And_Invoke_Action(web_Process, action);
                //web_Process.getStatus_Node().invoke(web_Process, action);
            }
            catch(WPFatal_Exception wpe){
                web_Process.error_End("Failed to invoke method", wpe);
                return;
            }
            return;
            
            
            
            
            
            
        } else if(web_Process.getStatus_Node().getStatus_Text().equals("waiting_for_child")){
            if(action.equals("Child Done")){
                String cps_Id = web_Process.getMy_Request().getParameter("cps_Id");
                if(cps_Id==null){
                    web_Process.error("No Child Process Id(cps_Id) has been given to servlet.", null);
                    return;
                }
            }else if(action.equals("Check Child")){
                web_Process.error("Not yet implemented.", null);
                return;
                
            }else if(action.equals("Reload")){
                web_Process.error("Not yet implemented.", null);
                return;
                
            }else if(action.equals("End Task")){
                web_Process.error("Not yet implemented.", null);
                return;
                
            }else{
                web_Process.error_End("Unknown action :"+action+": for Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
                return;
            }
            
            
        } else {
            web_Process.error_End("Unknown Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
            return;
        }
        
        
        
        web_Process.error_End("Fell thru status test.", null);
        return;
        
    }
    
    
    
    
    
    
    public void std_Action_End_Task(Web_Process web_Process) {
        web_Process.end();
        return;
    }
    
    public void std_Action_End(Web_Process web_Process) {
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void std_Action_NYI(Web_Process web_Process) {
        web_Process.error("Not Yet Implimented.",null);
        return;
    }
    
    
    public String get_Program_Title() {
        return "Run a Task";
    }    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void enter_Task_Do_Display(Web_Process web_Process) {
        
        //web_Process.pushStatus("Run_Task_Enter_Task");
        try {
            web_Process.pushStatus_Wreload("Run_Task_Enter_Task", this, "enter_Task_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        String focus_Button_Form ="Run_Task_find";
        
        try {
            
            
            Program program;
            program=new Program();
            
            web_Process.getMy_Response().setContentType("text/html");
            PrintWriter out = web_Process.getOut();
            
            web_Process.do_Display_HTML_Page_Begin(out);
            
            web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
            web_Process.do_Display_HTML_Head_End(out);
            
            
            //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
            //web_Process.do_Display_HTML_Body_Begin_Plain(out);
            web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+focus_Button_Form+"."+ program.getProgram_Name().getField_Name()+".focus();");
            
            
            out.println("<div id='above_Header'>");
            out.println("</div>");
            
            out.println("<div id='header'>");
            out.println("<div id='header_Text'>");
            out.println(web_Process.getProgram().getProgram_Title().toHTML());
            out.println("</div>");
            out.println("</div>");
            
            
            
            out.println("<div id='main'>");
            
            
            
            out.println("</div>");
            
            
            
            out.println("<div id='sidebar'>");
            
            try {
                web_Process.do_Display_HTML_DF_Button_Using_Form(out, focus_Button_Form, "action", "Run", "Run", "", program.getProgram_Name(), 15, this, "enter_Task_Do_Action_Run");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Run_Task_end", "action", "End", "End", "", this, "std_Action_End");
            }
            catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make button", wpfe);
                return;
            }
            
            out.println("</div>");
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            web_Process.setAttribute("program", program);
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
        
    }
    
    
    public void enter_Task_Do_Action_Start(Web_Process web_Process) {
        
        enter_Task_Do_Display(web_Process);
        return;
    }
    
    public void enter_Task_Do_Action_Reload(Web_Process web_Process) {
        
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    public void enter_Task_Do_Action_Run(Web_Process web_Process) {
        try {
            
            Program temp_Program;
            temp_Program=new Program();
            
            
            try{
                temp_Program.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Program.getProgram_Name().getField_Title()+".", dbre);
                return;
            }
            Program program;
            
            try {
                program = Program.get_Program("/"+temp_Program.getProgram_Name().getValue(), web_Process.getDatabase_Front());
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                web_Process.error("No such Program.", dbfe);
                return;
            }
            
            
            
            // found a Program;
            web_Process.popStatus_Node();
            try {
                web_Process.launch_Task(temp_Program.getProgram_Name().getValue(),"");
            }catch(ServletException se){
                web_Process.error_End(se);
                return;
            }catch(IOException ioe){
                web_Process.error_End(ioe);
                return;
            }
            
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
}
