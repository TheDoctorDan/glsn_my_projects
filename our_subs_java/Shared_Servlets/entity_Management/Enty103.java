/*
 * Enty103.java
 *
 * Created on October 7, 2006, 9:19 PM
 */

package entity_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
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
public class Enty103 extends WPHttpServlet_Base {
    
    
    
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Person Additional Phone Maintenance";
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
            }
            catch(WPException wpe){
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
                }
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error.", dbfe);
                    return;
                }
                catch(Database_Front_No_Results_Exception dbfe){
                    web_Process.error_End("Passed Comp Id :" + comp_Id + ": not found.", dbfe);
                    return;
                }
                
                web_Process.setAttribute("comp", comp);
                get_Person_Do_Action_Start(web_Process);
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
            get_Person_Do_Action_Start(web_Process);
            return;
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
    }
    
    
    void get_Person_Do_Display(Web_Process web_Process) {
        try {
            web_Process.pushStatus_Wreload("get_Person", this, "action_Stack_Do_Action",  "get_Person_Do_Action_Reload");
            web_Process.curStatus_Add_Action_Node(new WPAction_Node("Start", this, "get_Person_Do_Action_Start"));
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        Comp comp = (Comp) web_Process.getAttribute("comp");
        if(comp== null){
            web_Process.error_End("Can't find comp attribute.", null);
            return;
        }
        web_Process.setAttribute("gotten_Person","");
        try {
            web_Process.launch_Task("Get_Person", "&return_As=gotten_Person&title="+
            StringPlus.http_Encode(web_Process.getProgram().getProgram_Title().toString()) +
            "&loading_Comp_Id="+StringPlus.http_Encode(comp.getComp_Id().toString()));
        }catch(ServletException se){
            web_Process.error_End(se);
            return;
        }catch(IOException ioe){
            web_Process.error_End(ioe);
            return;
        }
        
        
    }
    
    public void get_Person_Do_Action_Start(Web_Process web_Process) {
        
        
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            if(comp== null){
                web_Process.error_End("Can't find comp attribute.", null);
                return;
            }
            
            String person_Number_String = web_Process.getMy_Request().getParameter("loading_Person_Number");
            
            if(person_Number_String != null){
                
                Integer person_Number = new Integer(person_Number_String);
                
                Person person;
                try {
                    person = (Person) web_Process.getDatabase_Front().getRecord(new Person(comp.getComp_Id().getValue(), person_Number));
                }
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error.", dbfe);
                    return;
                }
                catch(Database_Front_No_Results_Exception dbfe){
                    web_Process.error_End("Passed Person # :" + person_Number +  ": not found.", dbfe);
                    return;
                }
                
                web_Process.setAttribute("person", person);
                process_Loading_Parameters(web_Process);
                
                return;
            }else{
                get_Person_Do_Display(web_Process);
                return;
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void get_Person_Do_Action_Reload(Web_Process web_Process) {
        
        Object obj = web_Process.getAttribute("gotten_Person");
        if(obj ==null){
            // back from upper level in this program
            // special case for get_Person as it uses user_master_files
            do_Pop_and_Status_Routing(web_Process);
            //get_Person_Do_Display(web_Process);
            return;
        }else  if(obj instanceof Person){
            // sub program returned a value
            Person gotten_Person = (Person)obj;
            web_Process.removeAttribute("gotten_Person");
            web_Process.setAttribute("person", gotten_Person);
            
            list_Person_Additional_Phone_Records_Do_Display(web_Process);
            return;
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
    }
    
    
    
    
    void process_Loading_Parameters(Web_Process web_Process) {
        try {
            Comp comp = (Comp)web_Process.getAttribute("comp");
            Person person = (Person) web_Process.getAttribute("person");
            
            String phone = web_Process.getMy_Request().getParameter("loading_Phone");
            if(phone!=null){
                
                Person_Additional_Phone person_Additional_Phone;
                try{
                    person_Additional_Phone=(Person_Additional_Phone)web_Process.getDatabase_Front().getRecord(new Person_Additional_Phone(comp.getComp_Id().getValue(), person.getPerson_Number().getValue(), phone));
                }
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End(dbfe);
                    return;
                }
                catch(Database_Front_No_Results_Exception dbfe){
                    // no such Person
                    web_Process.error("No Such Person_Additional_Phone.", dbfe);
                    return;
                }
                // found a Person_Additional_Phone;
                web_Process.setAttribute("person_Additional_Phone", person_Additional_Phone);
                
                view_Person_Additional_Phone_Record_Do_Action_Edit(web_Process);
                return;
            }else{
                
                String loading_Action = web_Process.getMy_Request().getParameter("loading_Action");
                if(loading_Action!=null){
                    if(loading_Action.equals("Add Phone#")){
                        Person_Additional_Phone temp_Person_Additional_Phone;
                        temp_Person_Additional_Phone=new Person_Additional_Phone();
                        
                        try {
                            temp_Person_Additional_Phone.setComp_Id(person.getComp_Id().getValue());
                            temp_Person_Additional_Phone.setPerson_Number(person.getPerson_Number().getValue());
                        }catch(DFException dfe){
                            web_Process.error(dfe);
                            return;
                        }
                        new_Person_Additional_Phone_Record_Do_Display(web_Process, temp_Person_Additional_Phone, null);
                        return;
                    } else {
                        web_Process.error_End("Unknown loading_Action :"+loading_Action+": for Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
                        return;
                    }
                }else{
                    list_Person_Additional_Phone_Records_Do_Display(web_Process);
                    return;
                }
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    void new_Person_Additional_Phone_Record_Do_Display(Web_Process web_Process, Person_Additional_Phone person_Additional_Phone, Exception ex) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty103_New_Person_Additional_Phone_Record", this, "action_Stack_Do_Action",  "new_Person_Additional_Phone_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty103_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ person_Additional_Phone.getPhone().getField_Name()+".focus();");
        
        
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
        
        edit_And_New_Shared_Person_Additional_Phone_Main_Display(out, web_Process, person_Additional_Phone, ex);
        
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "new_Person_Additional_Phone_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "new_Person_Additional_Phone_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "End Task", "", button_Table, this, "std_Action_End_Task");
            button_Table.do_Display_HTML_Row_End();
        }
        catch(WPFatal_Exception wpfe){
            web_Process.error_End("Failed to make buttons.", wpfe);
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
        
        //web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty103_end",  "action", "End Task", "End Task", "");
        
        out.println("</div>");
        
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("person_Additional_Phone", person_Additional_Phone);
        
        out.close();
        
    }
    
    public void new_Person_Additional_Phone_Record_Do_Action_Create(Web_Process web_Process) {
        
        try {
            Person_Additional_Phone person_Additional_Phone = (Person_Additional_Phone) web_Process.getAttribute("person_Additional_Phone");
            
            try {
                person_Additional_Phone.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                person_Additional_Phone.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("person_Additional_Phone", person_Additional_Phone);
                new_Person_Additional_Phone_Record_Do_Display(web_Process, person_Additional_Phone, dbre);
                return;
            }
            
            web_Process.setAttribute("person_Additional_Phone", person_Additional_Phone);
            
            
            
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(person_Additional_Phone);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + person_Additional_Phone.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void new_Person_Additional_Phone_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        web_Process.removeAttribute("person_Additional_Phone");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    
    public void new_Person_Additional_Phone_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Person_Additional_Phone person_Additional_Phone = (Person_Additional_Phone) web_Process.getAttribute("person_Additional_Phone");
        new_Person_Additional_Phone_Record_Do_Display(web_Process, person_Additional_Phone, null);
        return;
    }
    
    
    
    void view_Person_Additional_Phone_Record_Do_Display(Web_Process web_Process, Person_Additional_Phone person_Additional_Phone) {
        
        try {
            web_Process.pushStatus_Wreload("Enty103_View_Person_Additional_Phone_Record", this, "action_Stack_Do_Action",  "view_Person_Additional_Phone_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty103_View";
        
        
        
        
        
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
        
        edit_And_New_Shared_Person_Additional_Phone_Superior_Display(out, web_Process, false);
        
        
        
        
        //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
        
        HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_View_Table");
        
        record_Table.do_Display_HTML_Table_Begin_Std();
        record_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person_Additional_Phone.getPhone());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person_Additional_Phone.getPhone_Type());
        
        
        
        record_Table.do_Display_HTML_TBody_End();
        record_Table.do_Display_HTML_Table_End();
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "view_Person_Additional_Phone_Record_Do_Action_Edit");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "End", "", button_Table, this, "std_Action_End");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "End Task", "", button_Table, this, "std_Action_End_Task");
            button_Table.do_Display_HTML_Row_End();
            
            
        }
        catch(WPFatal_Exception wpfe){
            web_Process.error_End("Failed to make buttons.", wpfe);
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
        
        web_Process.setAttribute("person_Additional_Phone", person_Additional_Phone);
        
        out.close();
    }
    
    
    public void view_Person_Additional_Phone_Record_Do_Action_Edit(Web_Process web_Process) {
        
        Person_Additional_Phone person_Additional_Phone = (Person_Additional_Phone) web_Process.getAttribute("person_Additional_Phone");
        
        try {
            person_Additional_Phone = (Person_Additional_Phone) web_Process.getDatabase_Front().getLocked_Record(person_Additional_Phone);
        }
        catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + person_Additional_Phone.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + person_Additional_Phone.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + person_Additional_Phone.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        edit_Person_Additional_Phone_Record_Do_Display(web_Process, person_Additional_Phone, null);
        return;
    }
    
    
    public void view_Person_Additional_Phone_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Person_Additional_Phone person_Additional_Phone = (Person_Additional_Phone) web_Process.getAttribute("person_Additional_Phone");
        if(person_Additional_Phone==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            view_Person_Additional_Phone_Record_Do_Display(web_Process, person_Additional_Phone);
        }
        return;
        
    }
    
    
    void edit_Person_Additional_Phone_Record_Do_Display(Web_Process web_Process, Person_Additional_Phone person_Additional_Phone, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty103_Edit_Person_Additional_Phone_Record", this, "action_Stack_Do_Action",  "edit_Person_Additional_Phone_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        
        String form_Name="Enty103_New";
        
        
        
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ person_Additional_Phone.getPhone().getField_Name()+".focus();");
        
        
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
        
        edit_And_New_Shared_Person_Additional_Phone_Main_Display(out, web_Process, person_Additional_Phone, ex);
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "edit_Person_Additional_Phone_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "edit_Person_Additional_Phone_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "edit_Person_Additional_Phone_Record_Do_Action_Delete");
            button_Table.do_Display_HTML_Row_End();
            
        }
        catch(WPFatal_Exception wpfe){
            web_Process.error_End("Failed to make buttons.", wpfe);
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
        
        web_Process.setAttribute("person_Additional_Phone", person_Additional_Phone);
        
        out.close();
        return;
        
    }
    
    
    public void edit_Person_Additional_Phone_Record_Do_Action_Update(Web_Process web_Process) {
        try {
            
            Person_Additional_Phone person_Additional_Phone = (Person_Additional_Phone) web_Process.getAttribute("person_Additional_Phone");
            
            try {
                person_Additional_Phone.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                person_Additional_Phone.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("person_Additional_Phone", person_Additional_Phone);
                edit_Person_Additional_Phone_Record_Do_Display(web_Process, person_Additional_Phone, dbre);
                return;
            }
            
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(person_Additional_Phone);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + person_Additional_Phone.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void edit_Person_Additional_Phone_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        Person_Additional_Phone person_Additional_Phone = (Person_Additional_Phone) web_Process.getAttribute("person_Additional_Phone");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + person_Additional_Phone.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void edit_Person_Additional_Phone_Record_Do_Action_Delete(Web_Process web_Process) {
        
        Person_Additional_Phone person_Additional_Phone = (Person_Additional_Phone) web_Process.getAttribute("person_Additional_Phone");
        try {
            person_Additional_Phone.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
        }
        
        catch(Database_Record_Exception dbre){
            web_Process.error(dbre);
            return;
        }
        
        try {
            int row_Count = web_Process.getDatabase_Front().deleteRecord(person_Additional_Phone);
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to Delete " + person_Additional_Phone.getEntity_Name() + " in database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("person_Additional_Phone");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void edit_Person_Additional_Phone_Record_Do_Action_End_Task(Web_Process web_Process) {
        
        Person_Additional_Phone person_Additional_Phone = (Person_Additional_Phone) web_Process.getAttribute("person_Additional_Phone");
        
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + person_Additional_Phone.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("person_Additional_Phone");
        web_Process.end();
        return;
    }
    
    
    public void edit_Person_Additional_Phone_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Person_Additional_Phone person_Additional_Phone = (Person_Additional_Phone) web_Process.getAttribute("person_Additional_Phone");
        edit_Person_Additional_Phone_Record_Do_Display(web_Process, person_Additional_Phone,null);
        return;
    }
    
    
    
    void list_Person_Additional_Phone_Records_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("Enty103_List_Person_Additional_Phone_Records", this, "action_Stack_Do_Action",  "list_Person_Additional_Phone_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        try {
            
            String form_Name="Enty103_List";
            String focus_Button_Form="Enty103_find";
            
            
            
            Person person = (Person)web_Process.getAttribute("person");
            if(person== null){
                web_Process.error_End("Can't find person attribute.", null);
                return;
            }
            
            Person_Additional_Phone temp_Person_Additional_Phone = new Person_Additional_Phone();
            try {
                temp_Person_Additional_Phone.setComp_Id(person.getComp_Id().getValue());
                temp_Person_Additional_Phone.setPerson_Number(person.getPerson_Number().getValue());
            } catch(DFException dfe){
                web_Process.error_End(dfe);
                return;
            }
            
            
            String  where_SQL_Clause ="WHERE " +
            temp_Person_Additional_Phone.getComp_Id().toWhere_Clause() + " AND " +
            temp_Person_Additional_Phone.getPerson_Number().toWhere_Clause();
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager person_Additional_Phone_Display_List_Sort_Manager;
            
            person_Additional_Phone_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("person_Additional_Phone_Display_List_Sort_Manager");
            if(person_Additional_Phone_Display_List_Sort_Manager==null){
                person_Additional_Phone_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.setAttribute("person_Additional_Phone_Display_List_Sort_Manager", person_Additional_Phone_Display_List_Sort_Manager);
            String sort_SQL_Clause = person_Additional_Phone_Display_List_Sort_Manager.getSort_SQL_Clause();
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager person_Additional_Phone_Display_List_Pagination_Manager;
            try {
                person_Additional_Phone_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("person_Additional_Phone_Display_List_Pagination_Manager", temp_Person_Additional_Phone, where_SQL_Clause, 10);
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
            
            //web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+focus_Button_Form+"."+ temp_Person_Additional_Phone.getDate().getField_Name()+".focus();");
            web_Process.do_Display_HTML_Body_Begin(out,
            "onload=" +
            web_Process.DF_Focus_Call_For_On_Event(focus_Button_Form, temp_Person_Additional_Phone.getPhone()) +
            "; ");
            
            
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
            
            edit_And_New_Shared_Person_Additional_Phone_Superior_Display(out, web_Process, false);
            
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
            list_Table.setUse_Even_Odd_Classes(true);
            list_Table.do_Display_HTML_Table_Begin_Std();
            list_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            Person_Additional_Phone[] person_Additional_Phone_List;
            try {
                person_Additional_Phone_List = (Person_Additional_Phone[]) web_Process.getDatabase_Front().getMany_Records(temp_Person_Additional_Phone, where_SQL_Clause + " " + sort_SQL_Clause, person_Additional_Phone_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), person_Additional_Phone_Display_List_Pagination_Manager.getItems_Per_Page());
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            list_Table.do_Display_HTML_Row_Begin_Std();
            list_Table.do_Display_HTML_TH_Element("#");
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, temp_Person_Additional_Phone.getPhone());
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, temp_Person_Additional_Phone.getPhone_Type());
            
            list_Table.do_Display_HTML_Row_End();
            
            
            
            if(person_Additional_Phone_List == null){
                list_Table.do_Display_HTML_Row_Begin_Std();
                list_Table.do_Display_HTML_TD_Element(" No Records Returned ");
                list_Table.do_Display_HTML_Row_End();
            }else{
                
                
                for(int row=0; row < person_Additional_Phone_List.length; row++){
                    Person_Additional_Phone person_Additional_Phone = person_Additional_Phone_List[row];
                    
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    try {
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, person_Additional_Phone.getPhone(), "Select", person, this, "list_Person_Additional_Phone_Records_Do_Action_Select");
                    }catch(WPFatal_Exception wpfe){
                        web_Process.error_End("Failed to make buttons.", wpfe);
                        return;
                    }
                    
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, person_Additional_Phone.getPhone_Type());
                    
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
                out.println(web_Process.display_List_Pagination_Manager_To_Html(person_Additional_Phone_Display_List_Pagination_Manager));
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            
            
            try {
                //web_Process.do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, focus_Button_Form, "action", "Go", "Go", "", temp_Person_Additional_Phone.getPerson_Additional_Phone_Number(), 15, person, this, "list_Person_Additional_Phone_Records_Do_Action_Go");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty103_add", "action", "Add", "Add", "", this, "list_Person_Additional_Phone_Records_Do_Action_Add");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty103_end", "action", "End", "End", "", this, "std_Action_End");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty103_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                
            }
            catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make buttons.", wpfe);
                return;
            }
            
            
            out.println("</div>");
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            //        web_Process.setAttribute("person_Additional_Phone", person_Additional_Phone);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void list_Person_Additional_Phone_Records_Do_Action_Add(Web_Process web_Process) {
        
        try {
            Person_Additional_Phone temp_Person_Additional_Phone =new Person_Additional_Phone();
            
            Person person = (Person) web_Process.getAttribute("person");
            
            try {
                temp_Person_Additional_Phone.setComp_Id(person.getComp_Id().getValue());
                temp_Person_Additional_Phone.setPerson_Number(person.getPerson_Number().getValue());
            }catch(DFException dfe){
                web_Process.error(dfe);
                return;
            }
            
            new_Person_Additional_Phone_Record_Do_Display(web_Process, temp_Person_Additional_Phone, null);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void list_Person_Additional_Phone_Records_Do_Action_Select(Web_Process web_Process) {
        
        try {
            Person_Additional_Phone temp_Person_Additional_Phone=new Person_Additional_Phone();
            
            try{
                temp_Person_Additional_Phone.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Person_Additional_Phone.getPhone().getField_Title()+".", dbre);
                return;
            }
            Person_Additional_Phone person_Additional_Phone;
            try{
                person_Additional_Phone=(Person_Additional_Phone)web_Process.getDatabase_Front().getRecord(temp_Person_Additional_Phone);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Person_Additional_Phone
                web_Process.error("No Such Person_Additional_Phone.", dbfe);
                return;
            }
            // found a Person_Additional_Phone;
            view_Person_Additional_Phone_Record_Do_Display(web_Process, person_Additional_Phone);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    public void list_Person_Additional_Phone_Records_Do_Action_Reload(Web_Process web_Process) {
        
        try {
            Person_Additional_Phone    person_Additional_Phone = new Person_Additional_Phone();
            
            
            // look for sorting commands
            Display_List_Sort_Manager person_Additional_Phone_Display_List_Sort_Manager;
            
            person_Additional_Phone_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("person_Additional_Phone_Display_List_Sort_Manager");
            if(person_Additional_Phone_Display_List_Sort_Manager==null){
                person_Additional_Phone_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.do_Process_Of_Sort_Manger_Request(person_Additional_Phone_Display_List_Sort_Manager, person_Additional_Phone);
            web_Process.setAttribute("person_Additional_Phone_Display_List_Sort_Manager", person_Additional_Phone_Display_List_Sort_Manager);
            
            // Look for paging commands
            Display_List_Pagination_Manager person_Additional_Phone_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("person_Additional_Phone_Display_List_Pagination_Manager");
            
            if(person_Additional_Phone_Display_List_Pagination_Manager!=null){
                person_Additional_Phone_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("person_Additional_Phone_Display_List_Pagination_Manager", person_Additional_Phone_Display_List_Pagination_Manager);
            }
            
            
            list_Person_Additional_Phone_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    
    
    public void edit_And_New_Shared_Person_Additional_Phone_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        if(comp== null){
            web_Process.error_End("Can't find comp attribute.", null);
            return;
        }
        
        Person person = (Person) web_Process.getAttribute("person");
        if(person== null){
            web_Process.error_End("Can't find person attribute.", null);
            return;
        }
        
        
        HTML_Table_Tag superior_Table = new HTML_Table_Tag("superior_Table", out, "Record_View_Table");
        
        superior_Table.do_Display_HTML_Table_Begin_Std();
        superior_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        web_Process.do_Display_HTML_DF_DF_Display_With_Th_Tr(superior_Table, comp.getComp_Id(), comp.getName());
        web_Process.do_Display_HTML_DF_DF_Display_With_Th_Tr(superior_Table, person.getPerson_Number(), person.getName_First_Then_Last());
        
        superior_Table.do_Display_HTML_TBody_End();
        superior_Table.do_Display_HTML_Table_End();
        if(with_Hidden_Input){
            try{
                web_Process.do_Display_HTML_DF_As_Hidden_Input(person.getComp_Id());
                web_Process.do_Display_HTML_DF_As_Hidden_Input(person.getPerson_Number());
            }
            catch(WPException wpe){
                web_Process.error_End("Programming error.", wpe);
                return;
            }
        }
    }
    
    public void edit_And_New_Shared_Person_Additional_Phone_Main_Display(PrintWriter out, Web_Process web_Process, Person_Additional_Phone person_Additional_Phone, Exception ex) {
        
        edit_And_New_Shared_Person_Additional_Phone_Superior_Display(out, web_Process, true);
        
        
        try {
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
            
            record_Table.do_Display_HTML_Table_Begin_Std();
            record_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            
            
            if(web_Process.getStatus_Node().getStatus_Text().equals("Enty103_New_Person_Additional_Phone_Record")){
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, person_Additional_Phone.getPhone());
            }else{
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person_Additional_Phone.getPhone());
            }
            
            
            
            
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, person_Additional_Phone.getPhone_Type());
            
            
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
