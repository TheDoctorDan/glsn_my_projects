/*
 * Secm100.java
 *
 * Created on November 13, 2006, 8:35 PM
 */

package com.amlibtech.security_Management;

import com.amlibtech.database.*;
import com.amlibtech.security_Management.data.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
import com.amlibtech.html_Tags.*;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import com.amlibtech.web_Processes.*;

/**
 *
 * @author  dgleeson
 * @version
 */
public class Secm100 extends WPHttpServlet_Base  {
    

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
        return "Security Profile Maintenance";
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
                security_Profile_List_Records_Do_Display(web_Process);
                return;
                
            }else if(action.equals("Reload")){
                try {
                    Security_Module.check_Query(web_Process);
                }catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                security_Profile_List_Records_Do_Display(web_Process);
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
        return "Security Profile Maintenance";
    }
    
    
    
    
    
    
    public void security_Profile_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        
        
        HTML_Table_Tag superior_Table = new HTML_Table_Tag("superior_Table", out, "Record_View_Table");
        
        superior_Table.do_Display_HTML_Table_Begin_Std();
        superior_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        //web_Process.do_Display_HTML_DF_DF_Display_With_Th_Tr(superior_Table, comp.getComp_Id(), comp.getName());
        
        
        superior_Table.do_Display_HTML_TBody_End();
        superior_Table.do_Display_HTML_Table_End();
        if(with_Hidden_Input){
//            try{
//                web_Process.do_Display_HTML_DF_As_Hidden_Input(comp.getComp_Id());
//            }catch(WPException wpe){
//                web_Process.error_End("Programming error.", wpe);
//                return;
//            }
        }
    }
    
    
    
    void security_Profile_List_Records_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("Secm100_security_Profile_List_Records", this, "security_Profile_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            String form_Name="Secm100_List";
            String focus_Button_Form="Secm100_find";
            
            
            
            
            Security_Profile temp_Security_Profile= new Security_Profile();
            
            
            String  where_SQL_Clause ="";

            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager security_Profile_Display_List_Sort_Manager;
            
            security_Profile_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("security_Profile_Display_List_Sort_Manager");
            if(security_Profile_Display_List_Sort_Manager==null){
                security_Profile_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.setAttribute("security_Profile_Display_List_Sort_Manager", security_Profile_Display_List_Sort_Manager);
            String sort_SQL_Clause = security_Profile_Display_List_Sort_Manager.getSort_SQL_Clause();
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager security_Profile_Display_List_Pagination_Manager;
            try {
                security_Profile_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("security_Profile_Display_List_Pagination_Manager", temp_Security_Profile, where_SQL_Clause, 10);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error.", dbfe);
                return;
            }
            
            
            
            
            
            
            
            
            web_Process.getMy_Response().setContentType("text/html");
            PrintWriter out = web_Process.getOut();
            
            web_Process.do_Display_HTML_Page_Begin(out);
            
            web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
            web_Process.do_Display_HTML_Head_End(out);
            
            web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+focus_Button_Form+"."+ temp_Security_Profile.getSecurity_Profile_Id().getField_Name()+".focus();");
            
            
            
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
            }catch(WPException wpe){
                web_Process.error_End("Failed to make form.", wpe);
                return;
            }
            
            security_Profile_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
            list_Table.setUse_Even_Odd_Classes(true);
            list_Table.do_Display_HTML_Table_Begin_Std();
            list_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            Security_Profile[] security_Profile_List;
            try {
                security_Profile_List = (Security_Profile[]) web_Process.getDatabase_Front().getMany_Records(temp_Security_Profile, where_SQL_Clause + " " + sort_SQL_Clause, security_Profile_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), security_Profile_Display_List_Pagination_Manager.getItems_Per_Page());
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            
            if(security_Profile_List == null){
                list_Table.do_Display_HTML_Row_Begin_Std();
                list_Table.do_Display_HTML_TD_Element(" No Records Returned ");
                list_Table.do_Display_HTML_Row_End();
                
            }else{
                list_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Security_Profile.getSecurity_Profile_Id(),  security_Profile_Display_List_Sort_Manager);
                list_Table.do_Display_HTML_Row_End();
                
                for(int row=0; row < security_Profile_List.length; row++){
                    Security_Profile security_Profile = security_Profile_List[row];
                    
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    try {
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link(list_Table, security_Profile.getSecurity_Profile_Id(), "Select", this, "security_Profile_List_Records_Do_Action_Select");
                    }catch(WPFatal_Exception wpe){
                        web_Process.error_End(wpe);
                        return;
                    }
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
            
            
            try {
                out.println(web_Process.display_List_Pagination_Manager_To_Html(security_Profile_Display_List_Pagination_Manager));
            }catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            
            
            
            try {
                web_Process.do_Display_HTML_DF_Button_Using_Form(out, focus_Button_Form, "action", "Go", "Go", "", temp_Security_Profile.getSecurity_Profile_Id(), 15, this, "security_Profile_List_Records_Do_Action_Go");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Secm100_end", "action", "End", "End", "", this, "std_Action_End");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Secm100_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                
            }catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make buttons.", wpfe);
                return;
            }
            
            
            out.println("</div>");
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            //        web_Process.setAttribute("security_Profile", security_Profile);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void security_Profile_List_Records_Do_Action_Go(Web_Process web_Process) {
        try {
            Security_Profile temp_Security_Profile=new Security_Profile();
            
            try{
                temp_Security_Profile.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Security_Profile.getSecurity_Profile_Id().getField_Title()+".", dbre);
                return;
            }
            Security_Profile security_Profile;
            try{
                security_Profile=(Security_Profile)web_Process.getDatabase_Front().getRecord(temp_Security_Profile);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }catch(Database_Front_No_Results_Exception dbfe){
                // no such Security_Profile
                security_Profile_New_Record_Do_Display(web_Process, temp_Security_Profile, null);
                return;
            }
            // found a Security_Profile;
            security_Profile_View_Record_Do_Display(web_Process, security_Profile);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void security_Profile_List_Records_Do_Action_Select(Web_Process web_Process) {
        try {
            Security_Profile temp_Security_Profile=new Security_Profile();
            
            try{
                temp_Security_Profile.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Security_Profile.getSecurity_Profile_Id().getField_Title()+".", dbre);
                return;
            }
            Security_Profile security_Profile;
            try{
                security_Profile=(Security_Profile)web_Process.getDatabase_Front().getRecord(temp_Security_Profile);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }catch(Database_Front_No_Results_Exception dbfe){
                // no such Security_Profile
                web_Process.error("No Such Security_Profile.", dbfe);
                return;
            }
            // found a Security_Profile;
            security_Profile_View_Record_Do_Display(web_Process, security_Profile);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void security_Profile_List_Records_Do_Action_Reload(Web_Process web_Process) {
        try {
            Security_Profile    security_Profile = new Security_Profile();
            
            // look for sorting commands
            Display_List_Sort_Manager security_Profile_Display_List_Sort_Manager;
            
            security_Profile_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("security_Profile_Display_List_Sort_Manager");
            if(security_Profile_Display_List_Sort_Manager==null){
                security_Profile_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.do_Process_Of_Sort_Manger_Request(security_Profile_Display_List_Sort_Manager, security_Profile);
            web_Process.setAttribute("security_Profile_Display_List_Sort_Manager", security_Profile_Display_List_Sort_Manager);
            
            // Look for paging commands
            Display_List_Pagination_Manager security_Profile_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("security_Profile_Display_List_Pagination_Manager");
            
            if(security_Profile_Display_List_Pagination_Manager!=null){
                security_Profile_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("security_Profile_Display_List_Pagination_Manager", security_Profile_Display_List_Pagination_Manager);
            }
            
            
            security_Profile_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    void security_Profile_New_Record_Do_Display(Web_Process web_Process, Security_Profile security_Profile, Exception ex) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Secm100_security_Profile_New_Record", this, "security_Profile_New_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        String form_Name="Secm100_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        web_Process.do_Display_HTML_Body_Begin_Plain(out);
        //web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ security_Profile.getDescription().getField_Name()+".focus();");
        
        
        out.println("<div id='above_Header'>");
        out.println("</div>");
        
        out.println("<div id='header'>");
        out.println("<div id='header_Text'>");
        out.println(web_Process.getProgram().getProgram_Title().toHTML());
        out.println("</div>");
        out.println("</div>");
        
        
        try {
            web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
        }catch(WPException wpe){
            web_Process.error_End("Failed to make form.", wpe);
            return;
        }
        
        
        out.println("<div id='main'>");
        
        security_Profile_Edit_And_New_Shared_Main_Display(out, web_Process, security_Profile, ex);
        
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "security_Profile_New_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "security_Profile_New_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "End Task", "", button_Table, this, "std_Action_End_Task");
            button_Table.do_Display_HTML_Row_End();
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("Failed to make buttons.", wpfe);
            return;
        }
        
        button_Table.do_Display_HTML_TBody_End();
        button_Table.do_Display_HTML_Table_End();
        
        
        try {
            web_Process.do_Display_HTML_Form_End();
        }catch(WPException wpe){
            web_Process.error_End("Failed to end form.", wpe);
            return;
        }
        
        out.println("</div>");
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("security_Profile", security_Profile);
        
        out.close();
    }
    
    
    
    public void security_Profile_New_Record_Do_Action_Create(Web_Process web_Process) {
        try {
            Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
            
            try {
                security_Profile.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                security_Profile.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("security_Profile", security_Profile);
                security_Profile_New_Record_Do_Display(web_Process, security_Profile, dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(security_Profile);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + security_Profile.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            web_Process.popStatus_Node();
            security_Profile_View_Record_Do_Action_Edit(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void security_Profile_New_Record_Do_Action_Cancel(Web_Process web_Process) {
        web_Process.removeAttribute("security_Profile");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void security_Profile_New_Record_Do_Action_Reload(Web_Process web_Process) {
        Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
        security_Profile_New_Record_Do_Display(web_Process, security_Profile, null);
        return;
    }
    
    
    
    
    
    void security_Profile_Edit_Record_Do_Display(Web_Process web_Process, Security_Profile security_Profile, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Secm100_security_Profile_Edit_Record", this, "security_Profile_Edit_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        String form_Name="Secm100_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        web_Process.do_Display_HTML_Body_Begin_Plain(out);
        //web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ security_Profile.getDescription().getField_Name()+".focus();");
        
        
        out.println("<div id='above_Header'>");
        out.println("</div>");
        
        out.println("<div id='header'>");
        out.println("<div id='header_Text'>");
        out.println(web_Process.getProgram().getProgram_Title().toHTML());
        out.println("</div>");
        out.println("</div>");
        
        try {
            web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
        }catch(WPException wpe){
            web_Process.error_End("Failed to make form.", wpe);
            return;
        }
        
        
        
        out.println("<div id='main'>");
        
        security_Profile_Edit_And_New_Shared_Main_Display(out, web_Process, security_Profile, ex);
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "security_Profile_Edit_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "security_Profile_Edit_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "security_Profile_Edit_Record_Do_Action_Delete");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Subsystems", "", button_Table, this, "security_Profile_Edit_Record_Do_Action_Subsystems");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Data Groups", "", button_Table, this, "security_Profile_Edit_Record_Do_Action_DataGroups");
            button_Table.do_Display_HTML_Row_End();
            
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("Failed to make buttons.", wpfe);
            return;
        }
        button_Table.do_Display_HTML_TBody_End();
        button_Table.do_Display_HTML_Table_End();
        
        
        try {
            web_Process.do_Display_HTML_Form_End();
        }catch(WPException wpe){
            web_Process.error_End("Failed to end form.", wpe);
            return;
        }
        
        out.println("</div>");
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("security_Profile", security_Profile);
        
        out.close();
        return;
    }
    
    
    
    public void security_Profile_Edit_Record_Do_Action_Update(Web_Process web_Process) {
        
        try {
            Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
            
            try {
                security_Profile.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                security_Profile.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("security_Profile", security_Profile);
                security_Profile_Edit_Record_Do_Display(web_Process, security_Profile, dbre);
                return;
            }
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(security_Profile);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + security_Profile.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void security_Profile_Edit_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + security_Profile.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    public void security_Profile_Edit_Record_Do_Action_Delete(Web_Process web_Process) {
        try {
            Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
            try {
                security_Profile.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
            }catch(Database_Record_Exception dbre){
                web_Process.error(dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().deleteRecord(security_Profile);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to Delete " + security_Profile.getEntity_Name() + " in database. ", dbfe);
                return;
            }
            web_Process.removeAttribute("security_Profile");
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void security_Profile_Edit_Record_Do_Action_End_Task(Web_Process web_Process) {
        
        Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + security_Profile.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("security_Profile");
        web_Process.end();
        return;
    }
    
    
    public void security_Profile_Edit_Record_Do_Action_Subsystems(Web_Process web_Process) {
        
        Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
        Secm100_Subsystems secm100_Subsystems_Servlet = new Secm100_Subsystems();
        secm100_Subsystems_Servlet.subsystem_List_Records_Do_Display(web_Process);
        return;
    }
    
    
    
    public void security_Profile_Edit_Record_Do_Action_DataGroups(Web_Process web_Process) {
       
        Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
        Secm100_Security_DataGroups secm100_Security_DataGroups_Servlet = new Secm100_Security_DataGroups();
        secm100_Security_DataGroups_Servlet.security_DataGroup_List_Records_Do_Display(web_Process);
        return;
    }
    
    public void security_Profile_Edit_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
        security_Profile_Edit_Record_Do_Display(web_Process, security_Profile,null);
        return;
    }
    
    
    
    
    public void security_Profile_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, Security_Profile security_Profile, Exception ex) {
        
        security_Profile_Edit_And_New_Shared_Superior_Display(out, web_Process, true);
        
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
            
            record_Table.do_Display_HTML_Table_Begin_Std();
            record_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, security_Profile.getSecurity_Profile_Id());
            
            
            
            
            record_Table.do_Display_HTML_TBody_End();
            record_Table.do_Display_HTML_Table_End();
            
            
            if(ex!=null){
                out.println("<p>Error:"+StringPlus.html_Encode(ex.getMessage()));
            }
            
        
        
    }
    
    
    
    void security_Profile_View_Record_Do_Display(Web_Process web_Process, Security_Profile security_Profile) {
        
        try {
            web_Process.pushStatus_Wreload("Secm100_security_Profile_View_Record", this, "security_Profile_View_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        String form_Name="Secm100_View";
        
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
            
        }catch(WPException wpe){
            web_Process.error_End("Failed to make form.", wpe);
            return;
        }
        
        
        out.println("<div id='main'>");
        
        security_Profile_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
        
        
        
        
        //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
        
        HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_View_Table");
        
        record_Table.do_Display_HTML_Table_Begin_Std();
        record_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, security_Profile.getSecurity_Profile_Id());
        
        
        
        
        record_Table.do_Display_HTML_TBody_End();
        record_Table.do_Display_HTML_Table_End();
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "security_Profile_View_Record_Do_Action_Edit");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "End", "", button_Table, this, "std_Action_End");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "End Task", "", button_Table, this, "std_Action_End_Task");
            button_Table.do_Display_HTML_Row_End();
            
            
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("Failed to make buttons.", wpfe);
            return;
        }
        
        button_Table.do_Display_HTML_TBody_End();
        button_Table.do_Display_HTML_Table_End();
        
        
        
        try {
            web_Process.do_Display_HTML_Form_End();
        }catch(WPException wpe){
            web_Process.error_End("Failed to end form.", wpe);
            return;
        }
        
        
        out.println("</div>");
        
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("security_Profile", security_Profile);
        
        out.close();
    }
    
    
    public void security_Profile_View_Record_Do_Action_Edit(Web_Process web_Process) {
        
        Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
        
        try {
            security_Profile = (Security_Profile) web_Process.getDatabase_Front().getLocked_Record(security_Profile);
        }catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + security_Profile.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + security_Profile.getEntity_Name() + " record in database. ", dbfe);
            return;
        }catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + security_Profile.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        security_Profile_Edit_Record_Do_Display(web_Process, security_Profile, null);
        return;
    }
    
    
    public void security_Profile_View_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
        if(security_Profile==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            security_Profile_View_Record_Do_Display(web_Process, security_Profile);
        }
        return;
    }
    
}
