/*
 * Cmp100.java
 *
 * Created on May 11, 2006, 2:15 PM
 */

package company_Management;

import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.data.*;
import com.amlibtech.security_Management.*;
import com.amlibtech.site_Management.data.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;



/**
 *
 * @author  dgleeson
 * @version
 */
public class Cmp100 extends WPHttpServlet_Base {
    
    
    
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Company Maintenance";
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
                comp_List_Records_Do_Display(web_Process);
                return;
            }else if(action.equals("Reload")){
                comp_List_Records_Do_Display(web_Process);
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
        return "Company Maintenance";
    }    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    void comp_New_Record_Do_Display(Web_Process web_Process, Comp comp, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        
        try {
            web_Process.pushStatus_Wreload("Cmp100_comp_New_Record", this, "comp_New_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        
        String form_Name="Cmp100_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ comp.getName().getField_Name()+".focus();");
        
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
        
        comp_Edit_And_New_Shared_Main_Display(out, web_Process, comp, ex);
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "comp_New_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "comp_New_Record_Do_Action_Cancel");
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
        
        web_Process.setAttribute("comp", comp);
        
        out.close();
        
    }
    
    
    
    public void comp_New_Record_Do_Action_Create(Web_Process web_Process) {
        
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            
            try {
                comp.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                comp.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("comp", comp);
                comp_New_Record_Do_Display(web_Process, comp, dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(comp);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + comp.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void comp_New_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        web_Process.removeAttribute("comp");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void comp_New_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        comp_New_Record_Do_Display(web_Process, comp, null);
        return;
        
    }
    
    
    
    void comp_View_Record_Do_Display(Web_Process web_Process, Comp comp) {
        
        
        
        try {
            web_Process.pushStatus_Wreload("Cmp100_comp_View_Record", this, "comp_View_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        
        
        String form_Name="Cmp100_View";
        
        
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
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getComp_Id());
        
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getName());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getAddress_Line1());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getAddress_Line2());
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getCity());
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getState_Abbr());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getZip_Code());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getFederal_Income_Tax_Id_Num());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getPhone_Number());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getFax_Number());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getEight_Hundred_Number());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getWeb_Site());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getE_Mail());
        
        
        
        record_Table.do_Display_HTML_TBody_End();
        record_Table.do_Display_HTML_Table_End();
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "comp_View_Record_Do_Action_Edit");
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
        
        web_Process.setAttribute("comp", comp);
        
        out.close();
    }
    
    
    
    public void comp_View_Record_Do_Action_Edit(Web_Process web_Process) {
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        
        try {
            comp = (Comp) web_Process.getDatabase_Front().getLocked_Record(comp);
        }
        catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + comp.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + comp.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + comp.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        comp_Edit_Record_Do_Display(web_Process, comp, null);
        return;
    }
    
    public void comp_View_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        if(comp==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            comp_View_Record_Do_Display(web_Process, comp);
        }
        return;
        
    }
    
    
    
    
    void comp_Edit_Record_Do_Display(Web_Process web_Process, Comp comp, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        
        try {
            web_Process.pushStatus_Wreload("Cmp100_comp_Edit_Record", this, "comp_Edit_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        
        
        String form_Name="Cmp100_New";
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ comp.getName().getField_Name()+".focus();");
        
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
        
        comp_Edit_And_New_Shared_Main_Display(out, web_Process, comp, ex);
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "comp_Edit_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "comp_Edit_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "comp_Edit_Record_Do_Action_Delete");
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
        
        web_Process.setAttribute("comp", comp);
        
        out.close();
        return;
        
    }
    
    
    
    public void comp_Edit_Record_Do_Action_Update(Web_Process web_Process) {
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            
            try {
                comp.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                comp.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("comp", comp);
                comp_Edit_Record_Do_Display(web_Process, comp, dbre);
                return;
            }
            
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(comp);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + comp.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void comp_Edit_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + comp.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    
    public void comp_Edit_Record_Do_Action_Delete(Web_Process web_Process) {
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            try {
                comp.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.error(dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().deleteRecord(comp);
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to Delete " + comp.getEntity_Name() + " in database. ", dbfe);
                return;
            }
            web_Process.removeAttribute("comp");
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void comp_Edit_Record_Do_Action_End_Task(Web_Process web_Process) {
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + comp.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("comp");
        web_Process.end();
        return;
    }
    
    
    public void comp_Edit_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        comp_Edit_Record_Do_Display(web_Process, comp,null);
        return;
    }
    
    
    
    public void comp_List_Records_Do_Display(Web_Process web_Process) {
        
        
        try {
            web_Process.pushStatus_Wreload("Cmp100_comp_List_Records", this, "comp_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            
            Comp temp_Comp = new Comp();
            
            User_Master_File user_Master_File;
            user_Master_File = new User_Master_File(web_Process.getUser().getUser_Id().toString(), temp_Comp.get_Table_Name());
            try {
                if(web_Process.getDatabase_Front().isMatch(user_Master_File)){
                    web_Process.getDatabase_Front().deleteRecord(user_Master_File);
                }
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return;
            }
            
            String form_Name="Cmp100_List";
            
            String focus_Button_Form = "Cmp100_new";
            
            String  where_SQL_Clause ="";
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager comp_Display_List_Sort_Manager;
            
            comp_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("comp_Display_List_Sort_Manager");
            if(comp_Display_List_Sort_Manager==null){
                comp_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.setAttribute("comp_Display_List_Sort_Manager", comp_Display_List_Sort_Manager);
            String sort_SQL_Clause = comp_Display_List_Sort_Manager.getSort_SQL_Clause();
            
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager comp_Display_List_Pagination_Manager;
            try {
                comp_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("comp_Display_List_Pagination_Manager", new Comp(), where_SQL_Clause, 10);
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
            web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+focus_Button_Form+"."+ new Comp().getComp_Id().getField_Name()+".focus();");
            
            
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
            
            
            Comp[] comp_List;
            try {
                comp_List = (Comp[]) web_Process.getDatabase_Front().getMany_Records(new Comp(), where_SQL_Clause + " " + sort_SQL_Clause, comp_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), comp_Display_List_Pagination_Manager.getItems_Per_Page());
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            
            if(comp_List == null){
                out.println("<tr><td> No Records Returned </td></tr>");
                
            }else{
                for(int row=0; row < comp_List.length; row++){
                    Comp comp = comp_List[row];
                    
                    if(row==0){
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, comp.getComp_Id(),  comp_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, comp.getName(),  comp_Display_List_Sort_Manager);
                        list_Table.do_Display_HTML_Row_End();
                    }
                    
                    
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    try {
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link(list_Table, comp.getComp_Id(), "Select", this, "comp_List_Records_Do_Action_Select");
                    }catch(WPFatal_Exception wpe){
                        web_Process.error_End("Failed to end form.", wpe);
                        return;
                    }
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, comp.getName());
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
                out.println(web_Process.display_List_Pagination_Manager_To_Html(comp_Display_List_Pagination_Manager));
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            Comp comp;
            
            comp = new Comp();
            comp.getComp_Id().clear_Min_Length();
            
            
            
            try {
                web_Process.do_Display_HTML_DF_Button_Using_Form(out, focus_Button_Form, "action", "Go", "Go", "", comp.getComp_Id(), this, "comp_List_Records_Do_Action_Go");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Cmp100_end", "action", "End", "End", "", this, "std_Action_End");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Cmp100_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                
            }
            catch(WPFatal_Exception wpe){
                web_Process.error_End("Failed to make buttons.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            //        web_Process.setAttribute("comp", comp);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void comp_List_Records_Do_Action_Go(Web_Process web_Process) {
        
        try {
            Comp temp_Comp;
            temp_Comp=new Comp();
            
            try{
                temp_Comp.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Comp.getComp_Id().getField_Title()+".", dbre);
                return;
            }
            Comp comp;
            try{
                comp=(Comp)web_Process.getDatabase_Front().getRecord(temp_Comp);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Company
                comp_New_Record_Do_Display(web_Process, temp_Comp, null);
                return;
            }
            // found a Company;
            comp_View_Record_Do_Display(web_Process, comp);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void comp_List_Records_Do_Action_Select(Web_Process web_Process) {
        
        try {
            Comp temp_Comp;
            temp_Comp=new Comp();
            
            try{
                temp_Comp.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Comp.getComp_Id().getField_Title()+".", dbre);
                return;
            }
            Comp comp;
            try{
                comp=(Comp)web_Process.getDatabase_Front().getRecord(temp_Comp);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Company
                web_Process.error("No Such Company.", dbfe);
                return;
            }
            // found a Company;
            comp_View_Record_Do_Display(web_Process, comp);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void comp_List_Records_Do_Action_Reload(Web_Process web_Process) {
        
        try {
            Comp    comp;
            comp = new Comp();
            
            // look for sorting commands
            Display_List_Sort_Manager comp_Display_List_Sort_Manager;
            
            comp_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("comp_Display_List_Sort_Manager");
            if(comp_Display_List_Sort_Manager==null){
                comp_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.do_Process_Of_Sort_Manger_Request(comp_Display_List_Sort_Manager, comp);
            web_Process.setAttribute("comp_Display_List_Sort_Manager", comp_Display_List_Sort_Manager);
            
            // Look for paging commands
            Display_List_Pagination_Manager comp_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("comp_Display_List_Pagination_Manager");
            
            if(comp_Display_List_Pagination_Manager!=null){
                comp_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("comp_Display_List_Pagination_Manager", comp_Display_List_Pagination_Manager);
            }
            
            
            comp_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void comp_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, Comp comp, Exception ex) {
        
        
        try {
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
            
            record_Table.do_Display_HTML_Table_Begin_Std();
            record_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, comp.getComp_Id());
            
            
            
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getName());
            
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getAddress_Line1());
            
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getAddress_Line2());
            
            
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getCity());
            
            
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getState_Abbr());
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getZip_Code());
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getFederal_Income_Tax_Id_Num());
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getPhone_Number());
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getFax_Number());
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getEight_Hundred_Number());
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getWeb_Site());
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, comp.getE_Mail());
            
            
            
            record_Table.do_Display_HTML_TBody_End();
            record_Table.do_Display_HTML_Table_End();
            
            
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
