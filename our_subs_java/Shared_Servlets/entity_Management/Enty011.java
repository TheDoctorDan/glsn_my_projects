/*
 * Enty011.java
 *
 * Created on August 20, 2006, 5:21 PM
 */

package entity_Management;

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
public class Enty011 extends WPHttpServlet_Base {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Organization_Contact_Type Maintenance";
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
                get_Organization_Type_Do_Action_Start(web_Process);
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
            get_Organization_Type_Do_Action_Start(web_Process);
            return;
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
    }
    
    
    
    
    
    public void get_Organization_Type_Do_Display(Web_Process web_Process) {
        
        
        try {
            web_Process.pushStatus_Wreload("get_Organization_Type", this, "action_Stack_Do_Action",  "get_Organization_Type_Do_Action_Reload");
            web_Process.curStatus_Add_Action_Node(new WPAction_Node("Start", this, "get_Organization_Type_Do_Action_Start"));
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        
        web_Process.setAttribute("gotten_Organization_Type","");
        try {
            web_Process.launch_Task("Get_Organization_Type", "&return_As=gotten_Organization_Type" +
            "&loading_Comp_Id="+ StringPlus.http_Encode(comp.getComp_Id().getValue()) +
            "&title="+StringPlus.http_Encode(web_Process.getProgram().getProgram_Title().toString()));
        }catch(ServletException se){
            web_Process.error_End(se);
            return;
        }catch(IOException ioe){
            web_Process.error_End(ioe);
            return;
        }
    }
    
    
    public void get_Organization_Type_Do_Action_Start(Web_Process web_Process) {
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            
            String organization_Type_Id_Str = web_Process.getMy_Request().getParameter("loading_Organization_Type_Id");
            
            if(organization_Type_Id_Str!=null){
                Organization_Type organization_Type;
                try {
                    organization_Type = (Organization_Type) web_Process.getDatabase_Front().getRecord(new Organization_Type(comp.getComp_Id().getValue(), organization_Type_Id_Str));
                }
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error.", dbfe);
                    return;
                }
                catch(Database_Front_No_Results_Exception dbfe){
                    web_Process.error_End("Passed Organization Type :" + organization_Type_Id_Str + ": not found.", dbfe);
                    return;
                }
                
                web_Process.setAttribute("organization_Type", organization_Type);
                String organization_Contact_Type_Id = web_Process.getMy_Request().getParameter("loading_Organization_Contact_Type_Id");
                if(organization_Contact_Type_Id ==null){
                    organization_Contact_Type_List_Records_Do_Display(web_Process);
                    return;
                }else{
                    Organization_Contact_Type temp_Organization_Contact_Type =new Organization_Contact_Type(
                    organization_Type.getComp_Id().getValue(),
                    organization_Type.getOrganization_Type_Id().getValue(), organization_Contact_Type_Id);
                    
                    
                    Organization_Contact_Type organization_Contact_Type;
                    try{
                        organization_Contact_Type=(Organization_Contact_Type)web_Process.getDatabase_Front().getRecord(temp_Organization_Contact_Type);
                    }
                    catch(Database_Front_Exception dbfe){
                        web_Process.error_End("Database Failure.", dbfe);
                        return;
                    }
                    catch(Database_Front_No_Results_Exception dbfe){
                        // no such Organization_Contact_Type
                        organization_Contact_Type_New_Record_Do_Display(web_Process, temp_Organization_Contact_Type, null);
                        return;
                    }
                    // found a Organization_Contact_Type;
                    organization_Contact_Type_View_Record_Do_Display(web_Process, organization_Contact_Type);
                    return;
                    
                }
            }else{
                get_Organization_Type_Do_Display(web_Process);
                return;
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void get_Organization_Type_Do_Action_Reload(Web_Process web_Process) {
        
        Object obj = web_Process.getAttribute("gotten_Organization_Type");
        if(obj ==null){
            // back from upper level in this program
            // special case for get_Organization_Type as it uses user_master_files
            do_Pop_and_Status_Routing(web_Process);
            //get_Organization_Type_Do_Display(web_Process);
            return;
        }else  if(obj instanceof Organization_Type){
            // sub program returned a value
            Organization_Type gotten_Organization_Type = (Organization_Type)obj;
            web_Process.removeAttribute("gotten_Organization_Type");
            web_Process.setAttribute("organization_Type", gotten_Organization_Type);
            
            organization_Contact_Type_List_Records_Do_Display(web_Process);
            return;
            
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
        
    }
    
    
    
    
    public void organization_Contact_Type_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        if(comp== null){
            web_Process.error_End("Can't find comp attribute.", null);
            return;
        }
        
        Organization_Type organization_Type = (Organization_Type)web_Process.getAttribute("organization_Type");
        if(organization_Type== null){
            web_Process.error_End("Can't find organization_Type attribute.", null);
            return;
        }
        
        
        HTML_Table_Tag superior_Table = new HTML_Table_Tag("superior_Table", out, "Record_View_Table");
        
        superior_Table.do_Display_HTML_Table_Begin_Std();
        superior_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        web_Process.do_Display_HTML_DF_DF_Display_With_Th_Tr(superior_Table, comp.getComp_Id(), comp.getName());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(superior_Table, organization_Type.getOrganization_Type_Id());
        
        
        superior_Table.do_Display_HTML_TBody_End();
        superior_Table.do_Display_HTML_Table_End();
        if(with_Hidden_Input){
            try{
                web_Process.do_Display_HTML_DF_As_Hidden_Input(comp.getComp_Id());
                web_Process.do_Display_HTML_DF_As_Hidden_Input(organization_Type.getOrganization_Type_Id());
            }
            catch(WPException wpe){
                web_Process.error_End("Programming error.", wpe);
                return;
            }
        }
    }
    
    
    
    
    
    public void organization_Contact_Type_List_Records_Do_Display(Web_Process web_Process) {
        
        
        try {
            web_Process.pushStatus_Wreload("Enty011_organization_Contact_Type_List_Records", this, "action_Stack_Do_Action",  "organization_Contact_Type_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        try {
            
            String form_Name="Enty011_List";
            String focus_Button_Form="Enty011_find";
            
            Comp comp = (Comp)web_Process.getAttribute("comp");
            if(comp== null){
                web_Process.error_End("Can't find comp attribute.", null);
                return;
            }
            
            Organization_Type organization_Type = (Organization_Type)web_Process.getAttribute("organization_Type");
            if(organization_Type== null){
                web_Process.error_End("Can't find organization_Type attribute.", null);
                return;
            }
            
            
            String  where_SQL_Clause ="WHERE " +
            organization_Type.getComp_Id().toWhere_Clause() + " AND " +
            organization_Type.getOrganization_Type_Id().toWhere_Clause();
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager organization_Contact_Type_Display_List_Sort_Manager;
            
            organization_Contact_Type_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("organization_Contact_Type_Display_List_Sort_Manager");
            if(organization_Contact_Type_Display_List_Sort_Manager==null){
                organization_Contact_Type_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.setAttribute("organization_Contact_Type_Display_List_Sort_Manager", organization_Contact_Type_Display_List_Sort_Manager);
            String sort_SQL_Clause = organization_Contact_Type_Display_List_Sort_Manager.getSort_SQL_Clause();
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager organization_Contact_Type_Display_List_Pagination_Manager;
            try {
                organization_Contact_Type_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("organization_Contact_Type_Display_List_Pagination_Manager", new Organization_Contact_Type(), where_SQL_Clause, 10);
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
            
            web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+focus_Button_Form+"."+ new Organization_Contact_Type().getOrganization_Contact_Type_Id().getField_Name()+".focus();");
            
            
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
            
            organization_Contact_Type_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
            list_Table.setUse_Even_Odd_Classes(true);
            list_Table.do_Display_HTML_Table_Begin_Std();
            list_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            Organization_Contact_Type[] organization_Contact_Type_List;
            try {
                organization_Contact_Type_List = (Organization_Contact_Type[]) web_Process.getDatabase_Front().getMany_Records(new Organization_Contact_Type(), where_SQL_Clause + " " + sort_SQL_Clause, organization_Contact_Type_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), organization_Contact_Type_Display_List_Pagination_Manager.getItems_Per_Page());
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            
            if(organization_Contact_Type_List == null){
                out.println("<tr><td> No Records Returned </td></tr>");
                
            }else{
                for(int row=0; row < organization_Contact_Type_List.length; row++){
                    Organization_Contact_Type organization_Contact_Type = organization_Contact_Type_List[row];
                    
                    if(row==0){
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, organization_Contact_Type.getOrganization_Contact_Type_Id(),  organization_Contact_Type_Display_List_Sort_Manager);
                        list_Table.do_Display_HTML_Row_End();
                    }
                    
                    try {
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, organization_Contact_Type.getOrganization_Contact_Type_Id(), "Select", organization_Type, this, "organization_Contact_Type_List_Records_Do_Action_Select");
                        list_Table.do_Display_HTML_Row_End();
                    }catch(WPFatal_Exception wpfe){
                        web_Process.error_End(wpfe);
                        return;
                    }
                    
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
                out.println(web_Process.display_List_Pagination_Manager_To_Html(organization_Contact_Type_Display_List_Pagination_Manager));
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            Organization_Contact_Type organization_Contact_Type = new Organization_Contact_Type();
            
            try {
                web_Process.do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, focus_Button_Form, "action", "Go", "Go", "", organization_Contact_Type.getOrganization_Contact_Type_Id(), 15, organization_Type, this, "organization_Contact_Type_List_Records_Do_Action_Go");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty011_end", "action", "End", "End", "", this, "std_Action_End");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty011_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                
            }
            catch(WPFatal_Exception wpe){
                web_Process.error_End("Failed to make buttons.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            //        web_Process.setAttribute("organization_Contact_Type", organization_Contact_Type);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_Contact_Type_List_Records_Do_Action_Go(Web_Process web_Process) {
        
        try {
            Organization_Contact_Type temp_Organization_Contact_Type =new Organization_Contact_Type();
            
            try{
                temp_Organization_Contact_Type.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Organization_Contact_Type.getOrganization_Contact_Type_Id().getField_Title()+".", dbre);
                return;
            }
            Organization_Contact_Type organization_Contact_Type;
            try{
                organization_Contact_Type=(Organization_Contact_Type)web_Process.getDatabase_Front().getRecord(temp_Organization_Contact_Type);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Organization_Contact_Type
                organization_Contact_Type_New_Record_Do_Display(web_Process, temp_Organization_Contact_Type, null);
                return;
            }
            // found a Organization_Contact_Type;
            organization_Contact_Type_View_Record_Do_Display(web_Process, organization_Contact_Type);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_Contact_Type_List_Records_Do_Action_Select(Web_Process web_Process) {
        try {
            Organization_Contact_Type temp_Organization_Contact_Type =new Organization_Contact_Type();
            
            try{
                temp_Organization_Contact_Type.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Organization_Contact_Type.getOrganization_Contact_Type_Id().getField_Title()+".", dbre);
                return;
            }
            Organization_Contact_Type organization_Contact_Type;
            try{
                organization_Contact_Type=(Organization_Contact_Type)web_Process.getDatabase_Front().getRecord(temp_Organization_Contact_Type);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Organization_Contact_Type
                web_Process.error("No Such Organization_Contact_Type.", dbfe);
                return;
            }
            // found a Organization_Contact_Type;
            organization_Contact_Type_View_Record_Do_Display(web_Process, organization_Contact_Type);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void organization_Contact_Type_List_Records_Do_Action_Reload(Web_Process web_Process) {
        try {
            Organization_Contact_Type    organization_Contact_Type = new Organization_Contact_Type();
            
            // look for sorting commands
            Display_List_Sort_Manager organization_Contact_Type_Display_List_Sort_Manager;
            
            organization_Contact_Type_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("organization_Contact_Type_Display_List_Sort_Manager");
            if(organization_Contact_Type_Display_List_Sort_Manager==null){
                organization_Contact_Type_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.do_Process_Of_Sort_Manger_Request(organization_Contact_Type_Display_List_Sort_Manager, organization_Contact_Type);
            web_Process.setAttribute("organization_Contact_Type_Display_List_Sort_Manager", organization_Contact_Type_Display_List_Sort_Manager);
            
            // Look for paging commands
            Display_List_Pagination_Manager organization_Contact_Type_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("organization_Contact_Type_Display_List_Pagination_Manager");
            
            if(organization_Contact_Type_Display_List_Pagination_Manager!=null){
                organization_Contact_Type_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("organization_Contact_Type_Display_List_Pagination_Manager", organization_Contact_Type_Display_List_Pagination_Manager);
            }
            
            
            organization_Contact_Type_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void organization_Contact_Type_New_Record_Do_Display(Web_Process web_Process, Organization_Contact_Type organization_Contact_Type, Exception ex) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty011_organization_Contact_Type_New_Record", this, "action_Stack_Do_Action",  "organization_Contact_Type_New_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        
        String form_Name="Enty011_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        web_Process.do_Display_HTML_Body_Begin_Plain(out);
        //web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ organization_Contact_Type.getDescription().getField_Name()+".focus();");
        
        
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
        
        organization_Contact_Type_Edit_And_New_Shared_Main_Display(out, web_Process, organization_Contact_Type, ex);
        
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "organization_Contact_Type_New_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "organization_Contact_Type_New_Record_Do_Action_Cancel");
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
        
        //web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty011_end",  "action", "End Task", "End Task", "");
        
        out.println("</div>");
        
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("organization_Contact_Type", organization_Contact_Type);
        
        out.close();
        
    }
    
    
    public void organization_Contact_Type_New_Record_Do_Action_Create(Web_Process web_Process) {
        try {
            Organization_Contact_Type organization_Contact_Type = (Organization_Contact_Type) web_Process.getAttribute("organization_Contact_Type");
            
            try {
                organization_Contact_Type.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                organization_Contact_Type.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("organization_Contact_Type", organization_Contact_Type);
                organization_Contact_Type_New_Record_Do_Display(web_Process, organization_Contact_Type, dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(organization_Contact_Type);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + organization_Contact_Type.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_Contact_Type_New_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        web_Process.removeAttribute("organization_Contact_Type");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void organization_Contact_Type_New_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Organization_Contact_Type organization_Contact_Type = (Organization_Contact_Type) web_Process.getAttribute("organization_Contact_Type");
        organization_Contact_Type_New_Record_Do_Display(web_Process, organization_Contact_Type, null);
        return;
    }
    
    
    
    public void organization_Contact_Type_View_Record_Do_Display(Web_Process web_Process, Organization_Contact_Type organization_Contact_Type) {
        
        
        try {
            web_Process.pushStatus_Wreload("Enty011_organization_Contact_Type_View_Record", this, "action_Stack_Do_Action",  "organization_Contact_Type_View_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty011_View";
        
        
        
        
        
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
        
        organization_Contact_Type_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
        
        
        
        
        //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
        
        HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_View_Table");
        
        record_Table.do_Display_HTML_Table_Begin_Std();
        record_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization_Contact_Type.getOrganization_Contact_Type_Id());
        
        
        record_Table.do_Display_HTML_TBody_End();
        record_Table.do_Display_HTML_Table_End();
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "organization_Contact_Type_View_Record_Do_Action_Edit");
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
        
        web_Process.setAttribute("organization_Contact_Type", organization_Contact_Type);
        
        out.close();
    }
    
    
    
    public void organization_Contact_Type_View_Record_Do_Action_Edit(Web_Process web_Process) {
        
        Organization_Contact_Type organization_Contact_Type = (Organization_Contact_Type) web_Process.getAttribute("organization_Contact_Type");
        
        try {
            organization_Contact_Type = (Organization_Contact_Type) web_Process.getDatabase_Front().getLocked_Record(organization_Contact_Type);
        }
        catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + organization_Contact_Type.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + organization_Contact_Type.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + organization_Contact_Type.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        organization_Contact_Type_Edit_Record_Do_Display(web_Process, organization_Contact_Type, null);
        return;
    }
    
    
    
    public void organization_Contact_Type_View_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Organization_Contact_Type organization_Contact_Type = (Organization_Contact_Type) web_Process.getAttribute("organization_Contact_Type");
        if(organization_Contact_Type==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            organization_Contact_Type_View_Record_Do_Display(web_Process, organization_Contact_Type);
        }
        return;
        
    }
    
    
    void organization_Contact_Type_Edit_Record_Do_Display(Web_Process web_Process, Organization_Contact_Type organization_Contact_Type, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        
        try {
            web_Process.pushStatus_Wreload("Enty011_organization_Contact_Type_Edit_Record", this, "action_Stack_Do_Action",  "organization_Contact_Type_Edit_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        
        String form_Name="Enty011_New";
        
        
        
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        web_Process.do_Display_HTML_Body_Begin_Plain(out);
        //web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+form_Name+"."+ organization_Contact_Type.getDescription().getField_Name()+".focus();");
        
        
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
        
        organization_Contact_Type_Edit_And_New_Shared_Main_Display(out, web_Process, organization_Contact_Type, ex);
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "organization_Contact_Type_Edit_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "organization_Contact_Type_Edit_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "organization_Contact_Type_Edit_Record_Do_Action_Delete");
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
        
        web_Process.setAttribute("organization_Contact_Type", organization_Contact_Type);
        
        out.close();
        return;
        
    }
    
    
    public void organization_Contact_Type_Edit_Record_Do_Action_Update(Web_Process web_Process) {
        try {
            Organization_Contact_Type organization_Contact_Type = (Organization_Contact_Type) web_Process.getAttribute("organization_Contact_Type");
            
            try {
                organization_Contact_Type.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                organization_Contact_Type.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("organization_Contact_Type", organization_Contact_Type);
                organization_Contact_Type_Edit_Record_Do_Display(web_Process, organization_Contact_Type, dbre);
                return;
            }
            
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(organization_Contact_Type);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + organization_Contact_Type.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_Contact_Type_Edit_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        Organization_Contact_Type organization_Contact_Type = (Organization_Contact_Type) web_Process.getAttribute("organization_Contact_Type");
        
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + organization_Contact_Type.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void organization_Contact_Type_Edit_Record_Do_Action_Delete(Web_Process web_Process) {
        try {
            Organization_Contact_Type organization_Contact_Type = (Organization_Contact_Type) web_Process.getAttribute("organization_Contact_Type");
            try {
                organization_Contact_Type.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.error(dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().deleteRecord(organization_Contact_Type);
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to Delete " + organization_Contact_Type.getEntity_Name() + " in database. ", dbfe);
                return;
            }
            web_Process.removeAttribute("organization_Contact_Type");
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_Contact_Type_Edit_Record_Do_Action_End_Task(Web_Process web_Process) {
        
        Organization_Contact_Type organization_Contact_Type = (Organization_Contact_Type) web_Process.getAttribute("organization_Contact_Type");
        
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + organization_Contact_Type.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("organization_Contact_Type");
        web_Process.end();
        return;
    }
    
    
    public void organization_Contact_Type_Edit_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Organization_Contact_Type organization_Contact_Type = (Organization_Contact_Type) web_Process.getAttribute("organization_Contact_Type");
        organization_Contact_Type_Edit_Record_Do_Display(web_Process, organization_Contact_Type,null);
        return;
    }
    
    
    
    
    
    public void organization_Contact_Type_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, Organization_Contact_Type organization_Contact_Type, Exception ex) {
        
        organization_Contact_Type_Edit_And_New_Shared_Superior_Display(out, web_Process, true);
        
        
        //try {
        
        
        
        //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
        
        HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
        
        record_Table.do_Display_HTML_Table_Begin_Std();
        record_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization_Contact_Type.getOrganization_Contact_Type_Id());
        
        
        record_Table.do_Display_HTML_TBody_End();
        record_Table.do_Display_HTML_Table_End();
        
        
        if(ex!=null){
            out.println("<p>Error:"+StringPlus.html_Encode(ex.getMessage()));
        }
        
        
        
        //        }
        //        catch(WPException wpe){
        //            web_Process.error_End("Programming error.", wpe);
        //            return;
        //        }
        
    }
    
    
    
}
