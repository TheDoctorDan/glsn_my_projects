/*
 * Sm100.java
 *
 * Created on November 6, 2006, 2:06 PM
 */

package com.amlibtech.site_Management;

import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.*;
import com.amlibtech.login.data.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
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
public class Sm100 extends WPHttpServlet_Base {

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
        return "User Maintenance";
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
                user_List_Records_Do_Display(web_Process);
                return;
            }else if(action.equals("Reload")){
                user_List_Records_Do_Display(web_Process);
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
        return "User Maintenance";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    void user_New_Record_Do_Display(Web_Process web_Process, User user, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        
        try {
            web_Process.pushStatus_Wreload("Sm100_user_New_Record", this, "user_New_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        
        String form_Name="Sm100_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        
        
        try {
            Security_Profile_AutoCompletion.getJavaScript(out, web_Process, form_Name, "security_Profile_Id", this, "user_New_Record_Do_Action_Maint");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End(wpfe);
            return;
        }
        
        
        
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ user.getFirst_Name().getField_Name()+".focus();" +
        "security_Profile_Id_Init();");
        
        out.println("<div id='above_Header'>");
        out.println("</div>");
        
        out.println("<div id='header'>");
        out.println("<div id='header_Text'>");
        out.println(web_Process.getProgram().getProgram_Title().toHTML());
        out.println("</div>");
        out.println("</div>");
        
        
        try {
            web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
        }
        catch(WPException wpe){
            web_Process.error_End("Failed to make form.", wpe);
            return;
        }
        
        
        out.println("<div id='main'>");
        
        user_Edit_And_New_Shared_Main_Display(out, web_Process, user, ex);
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "user_New_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "user_New_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "End Task", "", button_Table, this, "std_Action_End_Task");
            button_Table.do_Display_HTML_Row_End();
        }
        catch(WPFatal_Exception wpe){
            web_Process.error_End("Failed to make buttons.", wpe);
            return;
        }
        
        button_Table.do_Display_HTML_TBody_End();
        button_Table.do_Display_HTML_Table_End();
        
        
        try {
            web_Process.do_Display_HTML_Form_End();
        }
        catch(WPException wpe){
            web_Process.error_End("Failed to end form.", wpe);
            return;
        }
        
        
        out.println("</div>");
        
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("user", user);
        
        out.close();
        
    }
    
    
    
    public void user_New_Record_Do_Action_Create(Web_Process web_Process) {
        
        try {
            User user = (User) web_Process.getAttribute("user");
            
            try {
                user.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                user.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("user", user);
                user_New_Record_Do_Display(web_Process, user, dbre);
                return;
            }
            
            
            
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(user);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + user.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    public void user_New_Record_Do_Action_Maint(Web_Process web_Process) {
        
        try {
            User user = (User) web_Process.getAttribute("user");
            
            try {
                user.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                user.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                ;
            }
            web_Process.setAttribute("user", user);
            
            
            
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
        
        try {
            Maint_Program_Launcher.process_Maint(web_Process);
        }catch(ServletException se){
            web_Process.error_End(se);
            return;
        }catch(IOException ioe){
            web_Process.error_End(ioe);
            return;
        }
    }
    
    
    public void user_New_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        web_Process.removeAttribute("user");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void user_New_Record_Do_Action_Reload(Web_Process web_Process) {
        
        User user = (User) web_Process.getAttribute("user");
        user_New_Record_Do_Display(web_Process, user, null);
        return;
        
    }
    
    
    
    void user_View_Record_Do_Display(Web_Process web_Process, User user) {
        
        
        
        try {
            web_Process.pushStatus_Wreload("Sm100_user_View_Record", this, "user_View_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
       
            
            
            String form_Name="Sm100_View";
            
            
            web_Process.getMy_Response().setContentType("text/html");
            PrintWriter out = web_Process.getOut();
            
            web_Process.do_Display_HTML_Page_Begin(out);
            
            web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
            web_Process.do_Display_HTML_Head_End(out);
            
            
            //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
            web_Process.do_Display_HTML_Body_Begin_Plain(out);
            
            out.println("<div id='above_Header'>");
            out.println("</div>");
            
            out.println("<div id='header'>");
            out.println("<div id='header_Text'>");
            out.println(web_Process.getProgram().getProgram_Title().toHTML());
            out.println("</div>");
            out.println("</div>");
            
            
            
            try {
                web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
                
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to make form.", wpe);
                return;
            }
            
            
            out.println("<div id='main'>");
            
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_View_Table");
            
            record_Table.do_Display_HTML_Table_Begin_Std();
            record_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getUser_Id());
            
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getFirst_Name());
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getLast_Name());
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getPassword());
            
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getDescription());
            
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getTimeZone_Name());
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getObsolete());
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getEmail_Address());
            
            
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getSecurity_Profile_Id());
            
            if(web_Process.getUser().getAsp_Admin().getValue().booleanValue()){
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getReference_Type());
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getReference_Id());
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getAsp_Admin());
            }
            
            
            record_Table.do_Display_HTML_TBody_End();
            record_Table.do_Display_HTML_Table_End();
            
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            
            
            HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
            
            button_Table.do_Display_HTML_Table_Begin_Std();
            button_Table.do_Display_HTML_TBody_Begin_Std();
            
            try {
                button_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "user_View_Record_Do_Action_Edit");
                button_Table.do_Display_HTML_Row_End();
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "End", "", button_Table, this, "std_Action_End");
                button_Table.do_Display_HTML_Row_End();
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "End Task", "", button_Table, this, "std_Action_End_Task");
                button_Table.do_Display_HTML_Row_End();
                
            }
            catch(WPFatal_Exception wpe){
                web_Process.error_End("Failed to make buttons.", wpe);
                return;
            }
            
            button_Table.do_Display_HTML_TBody_End();
            button_Table.do_Display_HTML_Table_End();
            
            
            
            try {
                web_Process.do_Display_HTML_Form_End();
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to end form.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            web_Process.setAttribute("user", user);
            
            out.close();
            
        
    }
    
    
    
    public void user_View_Record_Do_Action_Edit(Web_Process web_Process) {
        
        User user = (User) web_Process.getAttribute("user");
        
        try {
            user = (User) web_Process.getDatabase_Front().getLocked_Record(user);
        }
        catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + user.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + user.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + user.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        user_Edit_Record_Do_Display(web_Process, user, null);
        return;
    }
    
    public void user_View_Record_Do_Action_Reload(Web_Process web_Process) {
        
        User user = (User) web_Process.getAttribute("user");
        if(user==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            user_View_Record_Do_Display(web_Process, user);
        }
        return;
        
    }
    
    
    
    
    void user_Edit_Record_Do_Display(Web_Process web_Process, User user, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        
        try {
            web_Process.pushStatus_Wreload("Sm100_user_Edit_Record", this, "user_Edit_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        
        
        String form_Name="Sm100_New";
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        
        
        
        try {
            Security_Profile_AutoCompletion.getJavaScript(out, web_Process, form_Name, "security_Profile_Id", this, "user_Edit_Record_Do_Action_Maint");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End(wpfe);
            return;
        }
        
        
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ user.getFirst_Name().getField_Name()+".focus();" +
        "security_Profile_Id_Init();");
        
        out.println("<div id='above_Header'>");
        out.println("</div>");
        
        out.println("<div id='header'>");
        out.println("<div id='header_Text'>");
        out.println(web_Process.getProgram().getProgram_Title().toHTML());
        out.println("</div>");
        out.println("</div>");
        
        try {
            web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
        }
        catch(WPException wpe){
            web_Process.error_End("Failed to make form.", wpe);
            return;
        }
        
        
        
        out.println("<div id='main'>");
        
        user_Edit_And_New_Shared_Main_Display(out, web_Process, user, ex);
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "user_Edit_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "user_Edit_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "user_Edit_Record_Do_Action_Delete");
            button_Table.do_Display_HTML_Row_End();
            
        }
        catch(WPFatal_Exception wpe){
            web_Process.error_End("Failed to make buttons.", wpe);
            return;
        }
        button_Table.do_Display_HTML_TBody_End();
        button_Table.do_Display_HTML_Table_End();
        
        
        try {
            web_Process.do_Display_HTML_Form_End();
        }
        catch(WPException wpe){
            web_Process.error_End("Failed to end form.", wpe);
            return;
        }
        
        out.println("</div>");
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("user", user);
        
        out.close();
        return;
        
    }
    
    
    
    public void user_Edit_Record_Do_Action_Update(Web_Process web_Process) {
        try {
            User user = (User) web_Process.getAttribute("user");
            
            try {
                user.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                user.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("user", user);
                user_Edit_Record_Do_Display(web_Process, user, dbre);
                return;
            }
            
            
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(user);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + user.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void user_Edit_Record_Do_Action_Maint(Web_Process web_Process) {
        try {
            User user = (User) web_Process.getAttribute("user");
            
            try {
                user.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                user.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                ;
            }
            web_Process.setAttribute("user", user);
            
            
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
        
        try {
            Maint_Program_Launcher.process_Maint(web_Process);
        }catch(ServletException se){
            web_Process.error_End(se);
            return;
        }catch(IOException ioe){
            web_Process.error_End(ioe);
            return;
        }
    }
    
    
    public void user_Edit_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        User user = (User) web_Process.getAttribute("user");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + user.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void user_Edit_Record_Do_Action_Delete(Web_Process web_Process) {
        try {
            
            User user = (User) web_Process.getAttribute("user");
            try {
                user.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.error(dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().deleteRecord(user);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to Delete " + user.getEntity_Name() + " in database. ", dbfe);
                return;
            }
            
            
            
            web_Process.removeAttribute("user");
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void user_Edit_Record_Do_Action_End_Task(Web_Process web_Process) {
        
        User user = (User) web_Process.getAttribute("user");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + user.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("user");
        web_Process.end();
        return;
    }
    
    
    public void user_Edit_Record_Do_Action_Reload(Web_Process web_Process) {
        
        User user = (User) web_Process.getAttribute("user");
        user_Edit_Record_Do_Display(web_Process, user,null);
        return;
    }
    
    
    
    public void user_List_Records_Do_Display(Web_Process web_Process) {
        
        
        try {
            web_Process.pushStatus_Wreload("Sm100_user_List_Records", this, "user_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            
            String form_Name="Sm100_List";
            
            String focus_Button_Form = "Sm100_new";
            
            User    temp_User = new User();
            
            String  where_SQL_Clause ="";
            
            if(!web_Process.getUser().getAsp_Admin().getValue().booleanValue()){
                where_SQL_Clause = " WHERE " + temp_User.getAsp_Admin().toWhere_Clause();
            }
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager user_Display_List_Sort_Manager;
            
            user_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("user_Display_List_Sort_Manager");
            if(user_Display_List_Sort_Manager==null){
                user_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.setAttribute("user_Display_List_Sort_Manager", user_Display_List_Sort_Manager);
            String sort_SQL_Clause = user_Display_List_Sort_Manager.getSort_SQL_Clause();
            
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager user_Display_List_Pagination_Manager;
            try {
                user_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("user_Display_List_Pagination_Manager", new User(), where_SQL_Clause, 10);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error.", dbfe);
                return;
            }
            
            
            
            
            
            
            
            web_Process.getMy_Response().setContentType("text/html");
            PrintWriter out = web_Process.getOut();
            
            web_Process.do_Display_HTML_Page_Begin(out);
            
            web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
            web_Process.do_Display_HTML_Head_End(out);
            
            
            //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
            //web_Process.do_Display_HTML_Body_Begin_Plain(out);
            web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+focus_Button_Form+"."+ new User().getUser_Id().getField_Name()+".focus();");
            
            
            out.println("<div id='above_Header'>");
            out.println("</div>");
            
            out.println("<div id='header'>");
            out.println("<div id='header_Text'>");
            out.println(web_Process.getProgram().getProgram_Title().toHTML());
            out.println("</div>");
            out.println("</div>");
            
            
            
            
            
            out.println("<div id='main'>");
            
            
            
            
            
            try {
                web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
                
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to make form.", wpe);
                return;
            }
            
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
            list_Table.setUse_Even_Odd_Classes(true);
            list_Table.do_Display_HTML_Table_Begin_Std();
            list_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            User[] user_List;
            try {
                user_List = (User[]) web_Process.getDatabase_Front().getMany_Records(new User(), where_SQL_Clause + " " + sort_SQL_Clause, user_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), user_Display_List_Pagination_Manager.getItems_Per_Page());
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            
            if(user_List == null){
                out.println("<tr><td> No Records Returned </td></tr>");
                
            }else{
                for(int row=0; row < user_List.length; row++){
                    User user = user_List[row];
                    
                    if(row==0){
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, user.getUser_Id(),  user_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, user.getFirst_Name(),  user_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, user.getLast_Name(),  user_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, user.getObsolete(),  user_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, user.getDescription(),  user_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, user.getEmail_Address(),  user_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, user.getSecurity_Profile_Id(),  user_Display_List_Sort_Manager);
                        list_Table.do_Display_HTML_Row_End();
                    }
                    
                    
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    try {
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link(list_Table, user.getUser_Id(), "Select", this, "user_List_Records_Do_Action_Select");
                    }catch(WPFatal_Exception wpe){
                        web_Process.error_End("Failed to end form.", wpe);
                        return;
                    }
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, user.getFirst_Name());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, user.getLast_Name());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, user.getObsolete());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, user.getDescription());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, user.getEmail_Address());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, user.getSecurity_Profile_Id());
                    list_Table.do_Display_HTML_Row_End();
                    
                    
                    
                    
                }
            }
            
            list_Table.do_Display_HTML_TBody_End();
            list_Table.do_Display_HTML_Table_End();
            
            
            
            try {
                web_Process.do_Display_HTML_Form_End();
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to end form.", wpe);
                return;
            }
            
            
            try {
                out.println(web_Process.display_List_Pagination_Manager_To_Html(user_Display_List_Pagination_Manager));
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            User user;
            
            user = new User();
            
            
            
            
            try {
                web_Process.do_Display_HTML_DF_Button_Using_Form(out, focus_Button_Form, "action", "Go", "Go", "", user.getUser_Id(), 15, this, "user_List_Records_Do_Action_Go");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Sm100_end", "action", "End", "End", "", this, "std_Action_End");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Sm100_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                
            }
            catch(WPFatal_Exception wpe){
                web_Process.error_End("Failed to make buttons.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            //        web_Process.setAttribute("user", user);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void user_List_Records_Do_Action_Go(Web_Process web_Process) {
        
        try {
            User temp_User;
            temp_User=new User();
            
            try{
                temp_User.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_User.getUser_Id().getField_Title()+".", dbre);
                return;
            }
            User user;
            try{
                user=(User)web_Process.getDatabase_Front().getRecord(temp_User);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Userany
                user_New_Record_Do_Display(web_Process, temp_User, null);
                return;
            }
            // found a Userany;
            if(!web_Process.getUser().getAsp_Admin().getValue().booleanValue()){
                if(user.getAsp_Admin().getValue().booleanValue()){
                    web_Process.error("This is andASP Admin User.  Access not allowed.", null);
                    return;
                }
            }
            
            user_View_Record_Do_Display(web_Process, user);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void user_List_Records_Do_Action_Select(Web_Process web_Process) {
        
        try {
            User temp_User;
            temp_User=new User();
            
            try{
                temp_User.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_User.getUser_Id().getField_Title()+".", dbre);
                return;
            }
            User user;
            try{
                user=(User)web_Process.getDatabase_Front().getRecord(temp_User);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Userany
                web_Process.error("No Such Userany.", dbfe);
                return;
            }
            // found a Userany;
            user_View_Record_Do_Display(web_Process, user);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void user_List_Records_Do_Action_Reload(Web_Process web_Process) {
        
        try {
            User    user;
            user = new User();
            
            // look for sorting commands
            Display_List_Sort_Manager user_Display_List_Sort_Manager;
            
            user_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("user_Display_List_Sort_Manager");
            if(user_Display_List_Sort_Manager==null){
                user_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.do_Process_Of_Sort_Manger_Request(user_Display_List_Sort_Manager, user);
            web_Process.setAttribute("user_Display_List_Sort_Manager", user_Display_List_Sort_Manager);
            
            // Look for paging commands
            Display_List_Pagination_Manager user_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("user_Display_List_Pagination_Manager");
            
            if(user_Display_List_Pagination_Manager!=null){
                user_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("user_Display_List_Pagination_Manager", user_Display_List_Pagination_Manager);
            }
            
            
            user_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void user_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, User user, Exception ex) {
        
        
        try {
            
           
            {
                
                HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
                
                record_Table.do_Display_HTML_Table_Begin_Std();
                record_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                
                
                
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, user.getUser_Id());
                web_Process.do_Display_HTML_DF_As_Hidden_Input(user.getUser_Id());

                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getFirst_Name());
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getLast_Name());
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getPassword());
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getDescription());
                
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getTimeZone_Name());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getObsolete());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getEmail_Address());
                
                
                
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getSecurity_Profile_Id(), "onkeyup='security_Profile_Id_DoCompletion()'");
                
                record_Table.do_Display_HTML_TBody_End();
                record_Table.do_Display_HTML_Table_End();
            }
            
            
            Security_Profile_AutoCompletion.getDivTable_Static(out, "security_Profile_Id");
            
            
            {
                
                if(web_Process.getUser().getAsp_Admin().getValue().booleanValue()){
                    
                    HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
                    
                    record_Table.do_Display_HTML_Table_Begin_Std();
                    record_Table.do_Display_HTML_TBody_Begin_Std();
                    
                    
                    
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getReference_Type());
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getReference_Id());
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, user.getAsp_Admin());
                    
                    
                    record_Table.do_Display_HTML_TBody_End();
                    record_Table.do_Display_HTML_Table_End();
                    
                }
                
                
            }
            
            if(ex!=null){
                out.println("<p>Error:"+StringPlus.html_Encode(ex.getMessage()));
            }
            
            
            
        }
        catch(WPException wpe){
            web_Process.error_End("Programming error.", wpe);
            return;
        }
        
        
        
    }
    
    
    
    
}
