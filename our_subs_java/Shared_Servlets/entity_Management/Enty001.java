/*
 * Enty001.java
 *
 * Created on August 10, 2006, 7:15 PM
 */

package entity_Management;

import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.data.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import entity_Management.datum.*;
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
public class Enty001 extends WPHttpServlet_Base {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Entity Control Maintenance";
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
                get_Comp_Do_Action_Start(web_Process);
                return;
                
            }else if(action.equals("Reload")){
                try {
                    Security_Module.check_Query(web_Process);
                }
                catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                get_Comp_Do_Action_Start(web_Process);
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
                web_Process.getStatus_Node().invoke(web_Process, action);
            }catch(WPException wpe){
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
    
    
    
    
    
    public void action_Stack_Do_Action(Web_Process web_Process, String action) {
        try {
            web_Process.curStatus_Find_And_Invoke_Action(web_Process, action);
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
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
    
    
    
    
    
    
    
    
    void get_Comp_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("get_Comp", this, "action_Stack_Do_Action",  "get_Comp_Do_Action_Reload");
            web_Process.curStatus_Add_Action_Node(new WPAction_Node("Start", this, "get_Comp_Do_Action_Start"));
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        web_Process.setAttribute("gotten_Comp","");
        try {
            web_Process.launch_Task("Get_Comp", "&return_As=gotten_Comp&title="+StringPlus.http_Encode(web_Process.getProgram().getProgram_Title().toString()));
        }catch(ServletException se){
            web_Process.error_End(se);
            return;
        }catch(IOException ioe){
            web_Process.error_End(ioe);
            return;
        }
    }
    
    
    
    public void get_Comp_Do_Action_Start(Web_Process web_Process) {
        try {
            String comp_Id = web_Process.getMy_Request().getParameter("loading_Comp_Id");
            if(comp_Id!=null){
                Comp comp;
                try {
                    comp = (Comp) web_Process.getDatabase_Front().getRecord(new Comp(comp_Id));
                }catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error.", dbfe);
                    return;
                }catch(Database_Front_No_Results_Exception dbfe){
                    web_Process.error_End("Passed Comp Id :" + comp_Id + ": not found.", dbfe);
                    return;
                }
                
                web_Process.setAttribute("comp", comp);
                entity_Control_Enter_Key_Do_Action_Go(web_Process);
                return;
            }else{
                get_Comp_Do_Display(web_Process);
                return;
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void get_Comp_Do_Action_Reload(Web_Process web_Process) {
        
        Object obj = web_Process.getAttribute("gotten_Comp");
        if(obj ==null){
            // back from upper level in this program
            // special case for get_Comp as it uses user_master_files
            do_Pop_and_Status_Routing(web_Process);
            //get_Comp_Do_Display(web_Process);
            return;
        }else  if(obj instanceof Comp){
            // sub program returned a value
            Comp gotten_Comp = (Comp)obj;
            web_Process.removeAttribute("gotten_Comp");
            web_Process.setAttribute("comp", gotten_Comp);
            entity_Control_Enter_Key_Do_Action_Go(web_Process);
            return;
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
    }
    
    
    
    
    
    
    
    public void entity_Control_Enter_Key_Do_Action_Go(Web_Process web_Process) {
        
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            
            Entity_Control temp_Entity_Control =new Entity_Control(comp.getComp_Id().getValue());
            
            
            Entity_Control entity_Control;
            try{
                entity_Control=(Entity_Control)web_Process.getDatabase_Front().getRecord(temp_Entity_Control);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Entity_Control
                entity_Control_New_Record_Do_Display(web_Process, temp_Entity_Control, null);
                return;
            }
            // found a Entity_Control;
            entity_Control_View_Record_Do_Display(web_Process, entity_Control);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void entity_Control_Enter_Key_Do_Action_Reload(Web_Process web_Process) {
        
        entity_Control_Enter_Key_Do_Action_Go(web_Process);
        return;
        
        
    }
    
    
    
    public void entity_Control_New_Record_Do_Display(Web_Process web_Process, Entity_Control entity_Control, Exception ex) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        
        try {
            web_Process.pushStatus_Wreload("Enty001_entity_Control_New_Record", this, "action_Stack_Do_Action",  "entity_Control_New_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty001_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ entity_Control.getLast_Used_Organization_Number().getField_Name()+".focus();");
        
        
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
        
        entity_Control_Edit_And_New_Shared_Main_Display(out, web_Process, entity_Control, ex);
        
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "entity_Control_New_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "entity_Control_New_Record_Do_Action_Cancel");
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
        
        //web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty001_end",  "action", "End Task", "End Task", "");
        
        out.println("</div>");
        
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("entity_Control", entity_Control);
        
        out.close();
        
    }
    
    
    public void entity_Control_New_Record_Do_Action_Create(Web_Process web_Process){
        
        try {
            Entity_Control entity_Control = (Entity_Control) web_Process.getAttribute("entity_Control");
            
            try {
                entity_Control.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                entity_Control.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("entity_Control", entity_Control);
                entity_Control_New_Record_Do_Display(web_Process, entity_Control, dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(entity_Control);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + entity_Control.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void entity_Control_New_Record_Do_Action_Cancel(Web_Process web_Process){
        
        web_Process.removeAttribute("entity_Control");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void entity_Control_New_Record_Do_Action_Reload(Web_Process web_Process){
        
        Entity_Control entity_Control = (Entity_Control) web_Process.getAttribute("entity_Control");
        entity_Control_New_Record_Do_Display(web_Process, entity_Control, null);
        return;
        
    }
    
    
    
    void entity_Control_View_Record_Do_Display(Web_Process web_Process, Entity_Control entity_Control) {
        
        
        try {
            web_Process.pushStatus_Wreload("Enty001_entity_Control_View_Record", this, "action_Stack_Do_Action",  "entity_Control_View_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty001_View";
        
        
        
        
        
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
        
        entity_Control_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
        
        
        
        
        //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
        
        HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_View_Table");
        
        record_Table.do_Display_HTML_Table_Begin_Std();
        record_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, entity_Control.getLast_Used_Organization_Number());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, entity_Control.getLast_Used_Person_Number());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, entity_Control.getLast_Used_Family_Number());
        
        
        
        record_Table.do_Display_HTML_TBody_End();
        record_Table.do_Display_HTML_Table_End();
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "entity_Control_View_Record_Do_Action_Edit");
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
        
        web_Process.setAttribute("entity_Control", entity_Control);
        
        out.close();
    }
    
    
    
    public void entity_Control_View_Record_Do_Action_Edit(Web_Process web_Process) {
        
        Entity_Control entity_Control = (Entity_Control) web_Process.getAttribute("entity_Control");
        
        try {
            entity_Control = (Entity_Control) web_Process.getDatabase_Front().getLocked_Record(entity_Control);
        }
        catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + entity_Control.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + entity_Control.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + entity_Control.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        entity_Control_Edit_Record_Do_Display(web_Process, entity_Control, null);
        return;
    }
    
    
    
    
    public void entity_Control_View_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Entity_Control entity_Control = (Entity_Control) web_Process.getAttribute("entity_Control");
        if(entity_Control==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            entity_Control_View_Record_Do_Display(web_Process, entity_Control);
        }
        return;
        
    }
    
    
    public void entity_Control_Edit_Record_Do_Display(Web_Process web_Process, Entity_Control entity_Control, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty001_entity_Control_Edit_Record", this, "action_Stack_Do_Action",  "entity_Control_Edit_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty001_New";
        
        
        
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ entity_Control.getLast_Used_Organization_Number().getField_Name()+".focus();");
        
        
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
        
        entity_Control_Edit_And_New_Shared_Main_Display(out, web_Process, entity_Control, ex);
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "entity_Control_Edit_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "entity_Control_Edit_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "entity_Control_Edit_Record_Do_Action_Delete");
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
        
        web_Process.setAttribute("entity_Control", entity_Control);
        
        out.close();
        return;
        
    }
    
    
    
    public void entity_Control_Edit_Record_Do_Action_Update(Web_Process web_Process) {
        
        try {
            Entity_Control entity_Control = (Entity_Control) web_Process.getAttribute("entity_Control");
            
            try {
                entity_Control.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                entity_Control.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("entity_Control", entity_Control);
                entity_Control_Edit_Record_Do_Display(web_Process, entity_Control, dbre);
                return;
            }
            
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(entity_Control);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + entity_Control.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void entity_Control_Edit_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        Entity_Control entity_Control = (Entity_Control) web_Process.getAttribute("entity_Control");
        
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + entity_Control.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void entity_Control_Edit_Record_Do_Action_Delete(Web_Process web_Process) {
        
        try {
            Entity_Control entity_Control = (Entity_Control) web_Process.getAttribute("entity_Control");
            try {
                entity_Control.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.error(dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().deleteRecord(entity_Control);
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to Delete " + entity_Control.getEntity_Name() + " in database. ", dbfe);
                return;
            }
            web_Process.removeAttribute("entity_Control");
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void entity_Control_Edit_Record_Do_Action_End_Task(Web_Process web_Process) {
        Entity_Control entity_Control = (Entity_Control) web_Process.getAttribute("entity_Control");
        
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + entity_Control.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("entity_Control");
        web_Process.end();
        return;
    }
    
    
    public void entity_Control_Edit_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Entity_Control entity_Control = (Entity_Control) web_Process.getAttribute("entity_Control");
        entity_Control_Edit_Record_Do_Display(web_Process, entity_Control,null);
        return;
        
    }
    
    
    
    public void entity_Control_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        if(comp== null){
            web_Process.error_End("Can't find comp attribute.", null);
            return;
        }
        
        
        HTML_Table_Tag superior_Table = new HTML_Table_Tag("superior_Table", out, "Record_View_Table");
        
        superior_Table.do_Display_HTML_Table_Begin_Std();
        superior_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        web_Process.do_Display_HTML_DF_DF_Display_With_Th_Tr(superior_Table, comp.getComp_Id(), comp.getName());
        
        
        superior_Table.do_Display_HTML_TBody_End();
        superior_Table.do_Display_HTML_Table_End();
        if(with_Hidden_Input){
            try{
                web_Process.do_Display_HTML_DF_As_Hidden_Input(comp.getComp_Id());
            }
            catch(WPException wpe){
                web_Process.error_End("Programming error.", wpe);
                return;
            }
        }
    }
    
    public void entity_Control_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, Entity_Control entity_Control, Exception ex) {
        
        entity_Control_Edit_And_New_Shared_Superior_Display(out, web_Process, true);
        
        
        try {
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
            
            record_Table.do_Display_HTML_Table_Begin_Std();
            record_Table.do_Display_HTML_TBody_Begin_Std();
            
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, entity_Control.getLast_Used_Organization_Number());
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, entity_Control.getLast_Used_Person_Number());
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, entity_Control.getLast_Used_Family_Number());
            
            
            
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
