/*
 * Get_Organization_Type.java
 *
 * Created on August 20, 2006, 5:49 PM
 */

package entity_Management;


import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import entity_Management.datum.*;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 * @version
 */
public class Get_Organization_Type extends WPHttpServlet_Base {
    
    
    
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Get a Organization_Type.";
    }
    
    
    
    public void do_Status_Routing(Web_Process web_Process, String action) {
        
        try {
            
            if(web_Process.getStatus_Node().getStatus_Text().equals("loading")){
                
                web_Process.resetStatus_Stack(new WPStatus_Node("exit"));
                if(action.equals("Start") || action.equals("Reload")){
                    
                    String title = web_Process.getMy_Request().getParameter("title");
                    if(title==null){
                        title="Please Select a Organization_Type.";
                    }
                    web_Process.setAttribute("title", title);
                    
                    String return_As = web_Process.getMy_Request().getParameter("return_As");
                    if(return_As==null){
                        web_Process.error_End("Get_Organization_Type must be called with a 'return_As' parameter.", null);
                        return;
                    }
                    web_Process.setAttribute("return_As", return_As);
                    
                    String comp_Id = web_Process.getMy_Request().getParameter("loading_Comp_Id");
                    if(comp_Id==null){
                        web_Process.error_End("Get_Organization_Type must be called with a 'loading_Comp_Id' parameter.", null);
                        return;
                    }
                    Comp comp;
                    try{
                        comp= (Comp)web_Process.getDatabase_Front().getRecord(new Comp(comp_Id));
                    }catch(Database_Front_Exception dbfe){
                        web_Process.error_End("Database Failure.", dbfe);
                        return;
                    }
                    catch(Database_Front_No_Results_Exception dbfe){
                        // no such Organization_Type
                        web_Process.error_End("No such Company Id :"+ comp_Id +":.", null);
                        return;
                    }
                    web_Process.setAttribute("comp", comp);
                    
                    organization_Type_List_Records_Do_Display(web_Process);
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
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
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
    
    
    
    
    
    public void organization_Type_List_Records_Do_Display(Web_Process web_Process) {
        
        
        try {
            web_Process.pushStatus_Wreload("Get_Cust_Organization_Type_List_Records", this, "action_Stack_Do_Action",  "organization_Type_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        try {
            
            String focus_Button_Form="Get_Organization_Type_find";
            
            Comp comp = (Comp)web_Process.getAttribute("comp");
            if(comp== null){
                web_Process.error_End("Can't find comp attribute.", null);
                return;
            }
            
            
            String  where_SQL_Clause ="WHERE " +
            comp.getComp_Id().toWhere_Clause();
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager organization_Type_Display_List_Sort_Manager;
            
            organization_Type_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("organization_Type_Display_List_Sort_Manager");
            if(organization_Type_Display_List_Sort_Manager==null){
                organization_Type_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.setAttribute("organization_Type_Display_List_Sort_Manager", organization_Type_Display_List_Sort_Manager);
            String sort_SQL_Clause = organization_Type_Display_List_Sort_Manager.getSort_SQL_Clause();
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager organization_Type_Display_List_Pagination_Manager;
            try {
                organization_Type_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("organization_Type_Display_List_Pagination_Manager", new Organization_Type(), where_SQL_Clause, 10);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error.", dbfe);
                return;
            }
            
            
            
            
            
            
            
            
            web_Process.getMy_Response().setContentType("text/html");
            PrintWriter out = web_Process.getOut();
            
            web_Process.do_Display_HTML_Page_Begin(out);
            web_Process.do_Display_HTML_Body_Begin_Plain(out);
            
            
            String program_Title = (String) web_Process.getAttribute("title");
            web_Process.do_Display_HTML_Head_Begin_Std(out, program_Title);
            web_Process.do_Display_HTML_Head_End(out);
            
            
            //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
            web_Process.do_Display_HTML_Body_Begin_Plain(out);
            
            
            out.println("<div id='above_Header'>");
            out.println("</div>");
            
            out.println("<div id='header'>");
            out.println("<div id='header_Text'>");
            out.println(program_Title);
            out.println("</div>");
            out.println("</div>");
            
            
            
            
            
            out.println("<div id='main'>");
            
            
            
            
            String form_Name="Get_Organization_Type_List";
            
            try {
                web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
                
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to make form.", wpe);
                return;
            }
            
            
            edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
            list_Table.setUse_Even_Odd_Classes(true);
            list_Table.do_Display_HTML_Table_Begin_Std();
            list_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            Organization_Type[] organization_Type_List;
            try {
                organization_Type_List = (Organization_Type[]) web_Process.getDatabase_Front().getMany_Records(new Organization_Type(), where_SQL_Clause + " " + sort_SQL_Clause, organization_Type_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), organization_Type_Display_List_Pagination_Manager.getItems_Per_Page());
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            
            if(organization_Type_List == null){
                out.println("<tr><td> No Records Returned </td></tr>");
                
            }else{
                for(int row=0; row < organization_Type_List.length; row++){
                    Organization_Type organization_Type = organization_Type_List[row];
                    
                    if(row==0){
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, organization_Type.getOrganization_Type_Id(),  organization_Type_Display_List_Sort_Manager);
                        list_Table.do_Display_HTML_Row_End();
                    }
                    
                    try {
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, organization_Type.getOrganization_Type_Id(), "Select", comp, this, "organization_Type_List_Records_Do_Action_Select");
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
            }catch(WPException wpe){
                web_Process.error_End("Failed to end form.", wpe);
                return;
            }
            
            try {
                out.println(web_Process.display_List_Pagination_Manager_To_Html(organization_Type_Display_List_Pagination_Manager));
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            Organization_Type organization_Type = new Organization_Type();
            
            
            try {
                web_Process.do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, focus_Button_Form,  "action", "Go", "Go", "", organization_Type.getOrganization_Type_Id(), 15, comp, this, "organization_Type_List_Records_Do_Action_Go");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Get_Organization_Type_end", "action", "End", "End", "", this, "std_Action_End");
                
            }catch(WPFatal_Exception wpe){
                web_Process.error_End("Failed to make buttons.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            //        web_Process.setAttribute("organization_Type", organization_Type);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void organization_Type_List_Records_Do_Action_Go(Web_Process web_Process) {
        
        
        try {
            Organization_Type temp_Organization_Type =new Organization_Type();
            
            try{
                temp_Organization_Type.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Organization_Type.getOrganization_Type_Id().getField_Title()+".", dbre);
                return;
            }
            Organization_Type organization_Type;
            try{
                organization_Type=(Organization_Type)web_Process.getDatabase_Front().getRecord(temp_Organization_Type);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Organization_Type
                web_Process.error("No such Organization_Type.", null);
                return;
            }
            // found a Organization_Type;
            Web_Process parent_Web_Process;
            try {
                parent_Web_Process = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), web_Process.getParent_Web_Process_Id());
            }
            catch(WPException wpe){
                web_Process.error_End("Unable to load parent web process.", wpe);
                return;
            }
            
            String return_As = (String) web_Process.getAttribute("return_As");
            parent_Web_Process.setAttribute(return_As, organization_Type);
            web_Process.end();
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_Type_List_Records_Do_Action_Select(Web_Process web_Process) {
        
        try {
            Organization_Type temp_Organization_Type =new Organization_Type();
            
            try{
                temp_Organization_Type.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Organization_Type.getOrganization_Type_Id().getField_Title()+".", dbre);
                return;
            }
            Organization_Type organization_Type;
            try{
                organization_Type=(Organization_Type)web_Process.getDatabase_Front().getRecord(temp_Organization_Type);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Organization_Type
                web_Process.error("No Such Organization_Type.", dbfe);
                return;
            }
            // found a Organization_Type;
            Web_Process parent_Web_Process;
            try {
                parent_Web_Process = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), web_Process.getParent_Web_Process_Id());
            }
            catch(WPException wpe){
                web_Process.error_End("Unable to load parent web process.", wpe);
                return;
            }
            
            String return_As = (String) web_Process.getAttribute("return_As");
            parent_Web_Process.setAttribute(return_As, organization_Type);
            web_Process.end();
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    public void organization_Type_List_Records_Do_Action_Reload(Web_Process web_Process) {
        
        try {
            Organization_Type    organization_Type = new Organization_Type();
            
            
            // look for sorting commands
            Display_List_Sort_Manager organization_Type_Display_List_Sort_Manager;
            
            organization_Type_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("organization_Type_Display_List_Sort_Manager");
            if(organization_Type_Display_List_Sort_Manager==null){
                organization_Type_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.do_Process_Of_Sort_Manger_Request(organization_Type_Display_List_Sort_Manager, organization_Type);
            web_Process.setAttribute("organization_Type_Display_List_Sort_Manager", organization_Type_Display_List_Sort_Manager);
            
            // Look for paging commands
            Display_List_Pagination_Manager organization_Type_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("organization_Type_Display_List_Pagination_Manager");
            
            if(organization_Type_Display_List_Pagination_Manager!=null){
                organization_Type_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("organization_Type_Display_List_Pagination_Manager", organization_Type_Display_List_Pagination_Manager);
            }
            
            
            organization_Type_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
        
    }
    
    
    public void edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        
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
    
}
