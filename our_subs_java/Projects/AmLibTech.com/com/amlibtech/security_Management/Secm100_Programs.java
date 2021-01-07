/*
 * Secm100_Programs.java
 *
 * Created on December 17, 2006, 10:40 PM
 */

package com.amlibtech.security_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.login.data.*;
import com.amlibtech.security_Management.data.*;
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
public class Secm100_Programs extends WPHttpServlet_Base {

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
        return "Security Profile Item Program Maintenance";
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
                subsystem_Program_List_Records_Do_Display(web_Process);
                return;
                
            }else if(action.equals("Reload")){
                try {
                    Security_Module.check_Query(web_Process);
                }catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                subsystem_Program_List_Records_Do_Display(web_Process);
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
            return "Security Profile Maintenance - Programs";
    }    
    
    
    
    
    
    
    
    
    
    
    public void security_Profile_Program_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
        if(security_Profile== null){
            web_Process.error_End("Can't find security_Profile attribute.", null);
            return;
        }
        
        String subsystem_Id = (String)web_Process.getAttribute("subsystem_Id");
        if(subsystem_Id== null){
            web_Process.error_End("Can't find subsystem_Id attribute.", null);
            return;
        }
        
        Subsystem subsystem = null;
        try {
            subsystem = Subsystem.get_Subsystem(subsystem_Id);
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End(dbfe);
            return;
        }catch(Database_Front_No_Results_Exception dbfe){
            web_Process.error_End(dbfe);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
                    
        HTML_Table_Tag superior_Table = new HTML_Table_Tag("superior_Table", out, "Record_View_Table");
        
        superior_Table.do_Display_HTML_Table_Begin_Std();
        superior_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(superior_Table, security_Profile.getSecurity_Profile_Id());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(superior_Table, subsystem.getSubsystem_Id());
        
        
        superior_Table.do_Display_HTML_TBody_End();
        superior_Table.do_Display_HTML_Table_End();
        if(with_Hidden_Input){
            try{
                web_Process.do_Display_HTML_DF_As_Hidden_Input(security_Profile.getSecurity_Profile_Id());
            }catch(WPException wpe){
                web_Process.error_End("Programming error.", wpe);
                return;
            }
        }
    }
    
    
    
    void subsystem_Program_List_Records_Do_Display(Web_Process web_Process) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Secm100_Programs_program_List_Records", this, "subsystem_Program_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            String form_Name="Secm100_Programs_List";
            String focus_Button_Form="Secm100_Programs_find";
            
            Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
            if(security_Profile== null){
                web_Process.error_End("Can't find security_Profile attribute.", null);
                return;
            }
            

            String subsystem_Id = (String)web_Process.getAttribute("subsystem_Id");
            if(subsystem_Id== null){
                web_Process.error_End("Can't find subsystem_Id attribute.", null);
                return;
            }
            
            
            web_Process.getMy_Response().setContentType("text/html");
            PrintWriter out = web_Process.getOut();
            
            web_Process.do_Display_HTML_Page_Begin(out);
            
            web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
            web_Process.do_Display_HTML_Head_End(out);
            
            //web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+focus_Button_Form+"."+ temp_Security_Profile_Program.getSecurity_Profile_Program_Id().getField_Name()+".focus();");
            web_Process.do_Display_HTML_Body_Begin_Plain(out);
            
            
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
            
            security_Profile_Program_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
            list_Table.setUse_Even_Odd_Classes(true);
            list_Table.do_Display_HTML_Table_Begin_Std();
            list_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            Program[] programs = null;
            try {
                programs = Program.get_Subsystem_Programs(subsystem_Id, web_Process.getDatabase_Front() );
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return;
            }catch(Database_Record_Exception dbre){
                web_Process.error_End(dbre);
                return;
            }catch(DFException dfe){
                web_Process.error_End(dfe);
                return;
            }
            
            
            
            Program temp_Program = new Program();
            Security_Profile_Program temp_Security_Profile_Program = new Security_Profile_Program();
            
            if(programs == null){
                list_Table.do_Display_HTML_Row_Begin_Std();
                list_Table.do_Display_HTML_TD_Element(" No Records Returned ");
                list_Table.do_Display_HTML_Row_End();
                out.println("<input type=hidden name=rows value=0>");
                
            }else{
                list_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, temp_Program.getProgram_Name());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, temp_Program.getProgram_Title());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, temp_Security_Profile_Program.getAccess_Type());
                list_Table.do_Display_HTML_TH_Element("Delete");
                list_Table.do_Display_HTML_Row_End();
                
                out.println("<input type=hidden name=rows value=" +programs.length + ">");
                
                for(int row=0; row < programs.length; row++){
                    Program program = programs[row];
                    
                    temp_Security_Profile_Program = new Security_Profile_Program(security_Profile.getSecurity_Profile_Id().getValue(), program.getProgram_Name().getValue());
                    Security_Profile_Program security_Profile_Program = new Security_Profile_Program();
                    try {
                        security_Profile_Program = (Security_Profile_Program) web_Process.getDatabase_Front().getRecord(temp_Security_Profile_Program);
                    }catch(Database_Front_Exception dbfe){
                        web_Process.error_End(dbfe);
                        return;
                    }catch(Database_Front_No_Results_Exception dbfe){
                        security_Profile_Program = temp_Security_Profile_Program;
                        security_Profile_Program.getAccess_Type().clearValue();
                    }
                    
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, program.getProgram_Name());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, program.getProgram_Title());
                    try {
                        web_Process.do_Display_HTML_DF_As_Hidden_Input("row_"+row+"_", program.getProgram_Name());
                        web_Process.do_Display_HTML_DF_Input_In_Form_With_TD("row_"+row+"_", list_Table, security_Profile_Program.getAccess_Type(), "");
                        
                        if(security_Profile_Program.getAccess_Type().getValue().equals("")){
                            list_Table.do_Display_HTML_TD_Element("&nbsp;");
                        }else{
                            list_Table.do_Display_HTML_TD_Element("<input type=checkbox name=row_"+row+"_Delete >");
                        }

                        
                    }catch(WPException wpe){
                        web_Process.error_End(wpe);
                        return;
                    }
                    
                    list_Table.do_Display_HTML_Row_End();
                    
                    
                }
            }
            
            list_Table.do_Display_HTML_TBody_End();
            list_Table.do_Display_HTML_Table_End();
            
            
            
            
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            
            HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
            
            
            button_Table.do_Display_HTML_Table_Begin_Std();
            button_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            try{
                button_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Submit", "", button_Table, this, "subsystem_Program_List_Records_Do_Action_Submit");
                button_Table.do_Display_HTML_Row_End();
                
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "End", "", button_Table, this, "std_Action_End");
                button_Table.do_Display_HTML_Row_End();
            }catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make buttons.", wpfe);
                return;
            }
            
            
            button_Table.do_Display_HTML_TBody_End();
            button_Table.do_Display_HTML_Table_End();
            
            
            
            out.println("</div>");
            
            try {
                web_Process.do_Display_HTML_Form_End();
            }catch(WPException wpe){
                web_Process.error_End("Failed to end form.", wpe);
                return;
            }
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            //        web_Process.setAttribute("security_Profile_Program", security_Profile_Program);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }

    
    public void subsystem_Program_List_Records_Do_Action_Submit(Web_Process web_Process) {
        try {
            
            Security_Profile security_Profile = (Security_Profile) web_Process.getAttribute("security_Profile");
            if(security_Profile== null){
                web_Process.error_End("Can't find security_Profile attribute.", null);
                return;
            }
            
            String rows = web_Process.getMy_Request().getParameter("rows");
            if(rows==null){
                web_Process.error_End("Can't find rows parameter.", null);
                return;
            }
            int irows = new Integer(rows).intValue();
            
            for(int row=0; row<irows; row++){
                
                
                String delete = web_Process.getMy_Request().getParameter("row_"+row+"_Delete");
                if(delete==null){
                    delete="off";
                }
                
                Security_Profile_Program temp_Security_Profile_Program=new Security_Profile_Program();
                try {
                    temp_Security_Profile_Program.setSecurity_Profile_Id(security_Profile.getSecurity_Profile_Id().getValue());
                }catch(DFException dfe){
                    web_Process.error_End(dfe);
                    return;
                }
                try {
                    temp_Security_Profile_Program.validate_Key_Fields_Of_Request("row_"+row+"_", web_Process.getMy_Request(), web_Process.getDatabase_Front());
                }catch(Database_Record_Exception dbre){
                    continue;
                }
                
                Security_Profile_Program security_Profile_Program;
                try {
                    security_Profile_Program = (Security_Profile_Program)web_Process.getDatabase_Front().getRecord(temp_Security_Profile_Program);
                }catch(Database_Front_Exception dbfe){
                    web_Process.error_End(dbfe);
                    return;
                }catch(Database_Front_No_Results_Exception dbfe){
                    if(delete.equals("on"))
                        continue;
                    security_Profile_Program =temp_Security_Profile_Program;
                    try {
                        security_Profile_Program.validate_Fields_Of_Request("row_"+row+"_", web_Process.getMy_Request(), web_Process.getDatabase_Front());
                    }catch(Database_Record_Exception dbre){
                        continue;
                    }
                    
                    try {
                        int row_Count = web_Process.getDatabase_Front().addRecord(security_Profile_Program);
                    }catch(Database_Front_Exception dbfe2){
                        web_Process.error_End("Failed to add " + security_Profile_Program.getEntity_Name() + " to database. ", dbfe2);
                        return;
                    }
                    continue;
                    
                }
                
                if(delete.equals("on")){
                    try {
                        security_Profile_Program.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
                    }catch(Database_Record_Exception dbre){
                        continue;
                    }
                    
                    try {
                        int row_Count = web_Process.getDatabase_Front().deleteRecord(security_Profile_Program);
                    }catch(Database_Front_Exception dbfe){
                        web_Process.error_End("Failed to Delete " + security_Profile_Program.getEntity_Name() + " in database. ", dbfe);
                        return;
                    }
                }else{
                    
                    try {
                        security_Profile_Program.validate_Fields_Of_Request("row_"+row+"_", web_Process.getMy_Request(), web_Process.getDatabase_Front());
                    }catch(Database_Record_Exception dbre){
                        continue;
                    }
                    
                    try {
                        int row_count = web_Process.getDatabase_Front().updateRecord(security_Profile_Program);
                    }catch(Database_Front_Exception dbfe){
                        web_Process.error_End("Failed to update " + security_Profile_Program.getEntity_Name() + " to database. ", dbfe);
                        return;
                    }
                }
            }
            
            subsystem_Program_List_Records_Do_Display(web_Process);
            
            return;
            
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    
    public void subsystem_Program_List_Records_Do_Action_Reload(Web_Process web_Process) {
        try {
            Security_Profile_Program    security_Profile_Program = new Security_Profile_Program();
            
            
            
            subsystem_Program_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
}
