/*
 * Who.java
 *
 * Created on August 22, 2007, 9:47 AM
 */

package bin_Util;

import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.data.*;
import com.amlibtech.security_Management.*;
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
public class Who extends WPHttpServlet_Base {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Show who is logged on";
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
                logged_In_User_List_Records_Do_Display(web_Process);
                return;
                
            }else if(action.equals("Reload")){
                try {
                    Security_Module.check_Query(web_Process);
                }catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                logged_In_User_List_Records_Do_Display(web_Process);
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
        return "Show who is logged on";
    }  
    
    
    
    
    
    
    public void logged_In_User_List_Records_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("Who_logged_In_User_List_Records", this, "logged_In_User_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Who_List";
        String focus_Button_Form="Who_find";
        
        
        
        
        
        
        
        
        
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
        
        LinkedList user_List;
        
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        LinkedList wp_List = web_Process_Table.getLinkedList();
        user_List = new LinkedList();
        
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
        
        
        
        try {
            web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
        }catch(WPException wpe){
            web_Process.error_End("Failed to make form.", wpe);
            return;
        }
        
        
        
        
        
        
        //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
        
        HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
        list_Table.setUse_Even_Odd_Classes(true);
        list_Table.do_Display_HTML_Table_Begin_Std();
        list_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        
        
        
        if(user_List.size() == 0){
            list_Table.do_Display_HTML_Row_Begin_Std();
            list_Table.do_Display_HTML_TD_Element(" No Records Returned ");
            list_Table.do_Display_HTML_Row_End();
            
        }else{
            
            
            for(int row=0; row < user_List.size(); row++){
                User logged_In_User = (User)user_List.get(row);
                String session_Id = (String)session_Id_List.get(row);
                
                if(row==0){
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    
                    list_Table.do_Display_HTML_TH_Element("Session ID");
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, logged_In_User.getLogin_Method());
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, logged_In_User.getUser_Id());
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, logged_In_User.getName_First_Then_Last());
                    list_Table.do_Display_HTML_Row_End();
                }
                
                list_Table.do_Display_HTML_Row_Begin_Std();
                list_Table.do_Display_HTML_TD_Element(session_Id);
                web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, logged_In_User.getLogin_Method());
                if(logged_In_User.is_A_System_User())
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, logged_In_User.getUser_Id());
                else
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, logged_In_User.getReference_Id());
                
                web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, logged_In_User.getName_First_Then_Last());
                list_Table.do_Display_HTML_Row_End();
                
                
            }
        }
        
        list_Table.do_Display_HTML_TBody_End();
        list_Table.do_Display_HTML_Table_End();
        
        
        
        try {
            web_Process.do_Display_HTML_Form_End();
        }catch(WPException wpe){
            web_Process.error_End("Failed to end form.", wpe);
            return;
        }
        
        
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        try {
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            button_Table.do_Display_HTML_TD_Begin();
            web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Who_end", "action", "End", "End", "", this, "std_Action_End");
            button_Table.do_Display_HTML_TD_End();
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            button_Table.do_Display_HTML_TD_Begin();
            web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Who_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
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
        
        //        web_Process.setAttribute("logged_In_User", logged_In_User);
        
        out.close();
        
    }
    
    
    
    
    
    
    public void logged_In_User_List_Records_Do_Action_Reload(Web_Process web_Process) {
        
        
        logged_In_User_List_Records_Do_Display(web_Process);
        return;
        
    }
      
    
     
    
}
