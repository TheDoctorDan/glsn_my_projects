/*
 * Ps.java
 *
 * Created on November 13, 2007, 11:04 AM
 */

package bin_Util;

import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.data.*;
import com.amlibtech.security_Management.*;
import com.amlibtech.util.*;
import com.amlibtech.web_Processes.*;
import com.amlibtech.web.util.*;

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
public class Ps extends WPHttpServlet_Base {
    
    
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Process List";
    }
    
    
    
    
    public void do_Status_Routing(Web_Process web_Process, String action) {
        
        if(web_Process.getStatus_Node().getStatus_Text().equals("loading")){
            
            web_Process.resetStatus_Stack(new WPStatus_Node("exit"));
            
            if(action.equals("Start")){
                try {
                    Security_Module.check_Query(web_Process);
                }catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                process_Table_List_Records_Do_Display(web_Process);
                return;
                
            }else if(action.equals("Reload")){
                try {
                    Security_Module.check_Query(web_Process);
                }catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                process_Table_List_Records_Do_Display(web_Process);
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
            }catch(WPFatal_Exception wpe){
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
        return "Process Status";
    }
    
    
    
    public void process_Table_List_Records_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("Ps_process_Table_List_Records", this, "process_Table_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Ps_List";
        String focus_Button_Form="Ps_find";
        
        
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        web_Process.do_Display_HTML_Body_Begin_Plain(out);
        
        
        
        out.println("<div id='above_Header'>");
        out.println("</div>");
        
        out.println("<div id='header'>");
        out.println("<div id='header_Text'>");
        out.println(web_Process.getProgram().getProgram_Title().toHTML());
        out.println("</div>");
        out.println("</div>");
        
        
        
        
        
        out.println("<div id='main'>");
        
        
        
        
        
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        LinkedList wp_List = web_Process_Table.getLinkedList();
        
        
        // retotal session, users, processes
        int num_Sessions=0;
        int num_Users=0;
        int num_Processes= wp_List.size();
        
        LinkedList user_List = new LinkedList();
        
        LinkedList session_Id_List = new LinkedList();
        
        
        for(int i=0; i<wp_List.size(); i++){
            Web_Process wp = (Web_Process)wp_List.get(i);
            if(wp==null)
                continue;
            User wp_User = wp.getUser();
            boolean found_Session=false;
            for(int j=0; j<session_Id_List.size() && !found_Session; j++){
                String temp_Session_Id = (String) session_Id_List.get(j);
                if(temp_Session_Id==null)
                    continue;
                if(wp.getSessionId().equals(temp_Session_Id)){
                    found_Session=true;
                }
            }
            if(!found_Session){
                session_Id_List.add(wp.getSessionId());
                user_List.add(wp_User);
            }
        }
        
        
        num_Sessions=session_Id_List.size();
        num_Users=user_List.size();
        
        
        
        
        Date today_Date = new Date();
        
        today_Date.setTime(System.currentTimeMillis());
        
        
        
        Date yesterday_Date = new Date();
        
        yesterday_Date.setTime(today_Date.getTime() - 23*3600000);
        
        
        
        
        
        
        
        // get or Initialize Display List Pagination Manager
        //            Display_List_Pagination_Manager process_Table_Display_List_Pagination_Manager;
        //            try {
        //                process_Table_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("process_Table_Display_List_Pagination_Manager", null, "", 10);
        //            }catch(Database_Front_Exception dbfe){
        //                web_Process.error_End("Database Error.", dbfe);
        //                return;
        //            }
        
        
        
        //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
        
        HTML_Table_Tag totals_Table = new HTML_Table_Tag("totals_Table", out, "Record_View_Table");
        totals_Table.do_Display_HTML_Table_Begin_Std();
        totals_Table.do_Display_HTML_TBody_Begin_Std();
        totals_Table.do_Display_HTML_Row_Begin_Std();
        
        totals_Table.do_Display_HTML_TH_Element("# Sessions");
        totals_Table.do_Display_HTML_TD_Element(""+num_Sessions);
        
        totals_Table.do_Display_HTML_TH_Element("# Users");
        totals_Table.do_Display_HTML_TD_Element(""+num_Users);
        
        totals_Table.do_Display_HTML_TH_Element("# Processes");
        totals_Table.do_Display_HTML_TD_Element(""+num_Processes);
        
        totals_Table.do_Display_HTML_Row_End();
        
        totals_Table.do_Display_HTML_TBody_End();
        totals_Table.do_Display_HTML_Table_End();
        
        
        
        
        HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
        list_Table.setUse_Even_Odd_Classes(true);
        list_Table.do_Display_HTML_Table_Begin_Std();
        list_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        
        
        
        
        list_Table.do_Display_HTML_Row_Begin_Std();
        list_Table.do_Display_HTML_TH_Element("Session Id");
        list_Table.do_Display_HTML_TH_Element("UType");
        list_Table.do_Display_HTML_TH_Element("Uid");
        list_Table.do_Display_HTML_TH_Element("PID");
        list_Table.do_Display_HTML_TH_Element("PPID");
        list_Table.do_Display_HTML_TH_Element("STime");
        list_Table.do_Display_HTML_TH_Element("IP");
        list_Table.do_Display_HTML_TH_Element("ATime");
        list_Table.do_Display_HTML_TH_Element("CMD");
        list_Table.do_Display_HTML_TH_Element("Options");
        list_Table.do_Display_HTML_TH_Element("Title");
        
        
        list_Table.do_Display_HTML_Row_End();
        
        for(int row=0; row < wp_List.size(); row++){
            Web_Process wp = (Web_Process)wp_List.get(row);
            if(wp==null){
                list_Table.do_Display_HTML_Row_Begin_Std();
                list_Table.do_Display_HTML_TD_Element("wp_List:element:"+row+" has null WP");
                list_Table.do_Display_HTML_Row_End();
                
            }else{
                
                User wp_User = wp.getUser();
                
                try {
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    list_Table.do_Display_HTML_TD_Element(wp.getSessionId());
                    
                    list_Table.do_Display_HTML_TD_Element(wp_User.getLogin_Method().getValue());
                    
                    web_Process.do_Display_HTML_Text_In_Table_As_Link_With_DBR(list_Table, ""+wp_User.getFull_Name(), "Select_UID", wp_User, this, "process_Table_List_Records_Do_Action_Select_UID");
                    
//                    
//                    
//                    web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
//                    list_Table.do_Display_HTML_TD_Element(""+wp.getId());
//                    web_Process.do_Display_HTML_In_Form_Hidden_Input("value", ""+wp.getId());
//                    web_Process.do_Display_HTML_In_Form_Hidden_Input("action", "Select_PID");
//                    web_Process.curStatus_Add_Action_Node(new WPAction_Node("Select_PID", this, "process_Table_List_Records_Do_Action_Select_PID"));
//                    
//                    
//                    web_Process.do_Display_HTML_Form_End();
                    
                    
                    web_Process.do_Display_HTML_Text_In_Table_As_Link(list_Table, ""+wp.getId(), "Select_PID%amp;value="+wp.getId(), this, "process_Table_List_Records_Do_Action_Select_PID");
                    web_Process.do_Display_HTML_Text_In_Table_As_Link(list_Table, ""+wp.getParent_Web_Process_Id(), "Select_PPID%amp;value="+wp.getParent_Web_Process_Id(), this, "process_Table_List_Records_Do_Action_Select_PPID");
                    
                    if(yesterday_Date.getTime() < wp.getStart_Date().getTime()){
                        // show hms
                        list_Table.do_Display_HTML_TD_Element(Calendar_Plus.get_Miliatry_HMS(wp.getStart_Date(), User.get_Logged_In_TimeZone(web_Process.getMy_Request().getSession())));
                    }else{
                        // show mmm/dd
                        list_Table.do_Display_HTML_TD_Element(Calendar_Plus.get_MDY(wp.getStart_Date(), User.get_Logged_In_TimeZone(web_Process.getMy_Request().getSession())).substring(0,5));
                    }
                    
                    list_Table.do_Display_HTML_TD_Element(wp.getMy_Request().getRemoteAddr());
                    
                    
                    
                    if(yesterday_Date.getTime() < wp.getLast_Activity_Date().getTime()){
                        // show hms
                        list_Table.do_Display_HTML_TD_Element(Calendar_Plus.get_Miliatry_HMS(wp.getLast_Activity_Date(), User.get_Logged_In_TimeZone(web_Process.getMy_Request().getSession())));
                    }else{
                        // show mmm/dd
                        list_Table.do_Display_HTML_TD_Element(Calendar_Plus.get_MDY(wp.getLast_Activity_Date(), User.get_Logged_In_TimeZone(web_Process.getMy_Request().getSession())).substring(0,5));
                    }
                    
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, wp.getProgram().getProgram_Name());
                    list_Table.do_Display_HTML_TD_Element(wp.getLoad_Options());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, wp.getProgram().getProgram_Title());
                    
                    list_Table.do_Display_HTML_Row_End();
                    
                }catch(WPFatal_Exception wpfe){
                    web_Process.error_End(wpfe);
                    return;
                }
                
            }
        }
        
        
        list_Table.do_Display_HTML_TBody_End();
        list_Table.do_Display_HTML_Table_End();
        
        
        
        
        
        //            try {
        //                out.println(web_Process.display_List_Pagination_Manager_To_Html(process_Table_Display_List_Pagination_Manager));
        //            }catch(WPException wpe){
        //                web_Process.error_End("Failed to display pagination manager.", wpe);
        //                return;
        //            }
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        try {
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            button_Table.do_Display_HTML_TD_Begin();
            web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Ps_end", "action", "End", "End", "", this, "std_Action_End");
            button_Table.do_Display_HTML_TD_End();
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            button_Table.do_Display_HTML_TD_Begin();
            web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Ps_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
            button_Table.do_Display_HTML_TD_End();
            button_Table.do_Display_HTML_Row_End();
            
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("Failed to make buttons.", wpfe);
            return;
        }
        
        button_Table.do_Display_HTML_TBody_End();
        button_Table.do_Display_HTML_Table_End();
        
        out.println("</div>");
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        //        web_Process.setAttribute("process_Table", process_Table);
        
        out.close();
        
    }
    
    
    
    public void process_Table_List_Records_Do_Action_Select_PID(Web_Process web_Process) {
        String value = web_Process.getMy_Request().getParameter("value");
        if(value==null){
            web_Process.error("no value parameter passed.", null);
            return;
        }
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        Web_Process wp1 = null;
        try {
            wp1 = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), value);
        }catch(WPException wpe){
            web_Process.error("No such Web Process ID as :"+value, null);
            return;
        }
        try {
            wp1.reload();
        }catch(ServletException se){
            web_Process.error("Error on reload of wp :", se);
            return;
        }catch(IOException ioe){
            web_Process.error("Error on reload of wp :", ioe);
            return;
        }
        //process_Table_List_Records_Do_Action_Reload(web_Process);
        
        return;
    }
    
    public void process_Table_List_Records_Do_Action_Select_PPID(Web_Process web_Process) {
        String value = web_Process.getMy_Request().getParameter("value");
        if(value==null){
            web_Process.error("no value parameter passed.", null);
            return;
        }
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        Web_Process wp1 = null;
        try {
            wp1 = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), value);
        }catch(WPException wpe){
            web_Process.error("No such Web Process ID as :"+value, null);
            return;
        }
        process_Table_List_Records_Do_Action_Reload(web_Process);
        
        return;
    }
    
    public void process_Table_List_Records_Do_Action_Select_UID(Web_Process web_Process) {
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        process_Table_List_Records_Do_Action_Reload(web_Process);
        return;
    }
    
    
    public void process_Table_List_Records_Do_Action_Reload(Web_Process web_Process) {
        
        
        // Look for paging commands
        //            Display_List_Pagination_Manager process_Table_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("process_Table_Display_List_Pagination_Manager");
        //
        //            if(process_Table_Display_List_Pagination_Manager!=null){
        //                process_Table_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
        //                web_Process.setAttribute("process_Table_Display_List_Pagination_Manager", process_Table_Display_List_Pagination_Manager);
        //            }
        
        
        process_Table_List_Records_Do_Display(web_Process);
        return;
        
    }
    
    
    
    
    
}
