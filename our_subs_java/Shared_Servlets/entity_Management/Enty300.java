/*
 * Enty300.java
 *
 * Created on June 22, 2007, 9:23 PM
 */

package entity_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.*;
import com.amlibtech.login.data.*;
import com.amlibtech.security_Management.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import customer_Management.data.*;
import enroll_Now_Performance.data.*;
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
public class Enty300 extends WPHttpServlet_Base {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Organization Maintenance";
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
                get_Comp_Do_Action_Start(web_Process);
                return;
                
            }else if(action.equals("Reload")){
                try {
                    Security_Module.check_Query(web_Process);
                }catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                get_Comp_Do_Action_Reload(web_Process);
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
                                
                process_Loading_Parameters(web_Process);

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
            organization_List_Records_Do_Display(web_Process);
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
            
            String organization_Number = web_Process.getMy_Request().getParameter("loading_Organization_Number");
            if(organization_Number!=null){
                Organization organization;
                try {
                    organization = (Organization)web_Process.getDatabase_Front().getRecord(new Organization(comp.getComp_Id().getValue(), new Integer(organization_Number)));
                }catch(Database_Front_Exception dbfe){
                    web_Process.error_End(dbfe);
                    return;
                }catch(Database_Front_No_Results_Exception dbfe){
                    web_Process.error_End("No Such Organization Found", dbfe);
                    return;
                }
                web_Process.setAttribute("organization", organization);
                
                
                String loading_Action = web_Process.getMy_Request().getParameter("loading_Action");
                if(loading_Action!=null){
                    if(loading_Action.equals("Add Address")){
                        try {
                            web_Process.launch_Task("Enty301", "&loading_Comp_Id="+StringPlus.http_Encode(organization.getComp_Id().getValue()) +
                            "&loading_organization_Number="+StringPlus.http_Encode(organization.getOrganization_Number().toString()) +
                            "&loading_Action="+StringPlus.http_Encode("Add Address")
                            );
                        }catch(ServletException se){
                            web_Process.error_End(se);
                            return;
                        }catch(IOException ioe){
                            web_Process.error_End(ioe);
                            return;
                        }
                        return;
                        
                    } else if(loading_Action.equals("Add Phone#")){
                        try {
                            web_Process.launch_Task("Enty303", "&loading_Comp_Id="+StringPlus.http_Encode(organization.getComp_Id().getValue()) +
                            "&loading_organization_Number="+StringPlus.http_Encode(organization.getOrganization_Number().toString())+
                            "&loading_Action="+StringPlus.http_Encode("Add Phone#")
                            );
                        }catch(ServletException se){
                            web_Process.error_End(se);
                            return;
                        }catch(IOException ioe){
                            web_Process.error_End(ioe);
                            return;
                        }
                        return;
                        
                    } else if(loading_Action.equals("Add E-Mail")){
                        try {
                            web_Process.launch_Task("Enty302", "&loading_Comp_Id="+StringPlus.http_Encode(organization.getComp_Id().getValue()) +
                            "&loading_Organization_Number="+StringPlus.http_Encode(organization.getOrganization_Number().toString())+
                            "&loading_Action="+StringPlus.http_Encode("Add E-Mail")
                            );
                        }catch(ServletException se){
                            web_Process.error_End(se);
                            return;
                        }catch(IOException ioe){
                            web_Process.error_End(ioe);
                            return;
                        }
                        return;
                        
                    } else {
                        web_Process.error_End("Unknown loading_Action :"+loading_Action+": for Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
                    }
                    
                    
                } else {
                    String organization_Additional_Address_Number = web_Process.getMy_Request().getParameter("loading_Organization_Additional_Address_Number");
                    if(organization_Additional_Address_Number!=null){
                        try {
                            web_Process.launch_Task("Enty301", "&loading_Comp_Id="+StringPlus.http_Encode(organization.getComp_Id().getValue()) +
                            "&loading_Organization_Number="+StringPlus.http_Encode(organization.getOrganization_Number().toString()) +
                            "&loading_Organization_Additional_Address_Number"+StringPlus.http_Encode(organization_Additional_Address_Number)
                            );
                        }catch(ServletException se){
                            web_Process.error_End(se);
                            return;
                        }catch(IOException ioe){
                            web_Process.error_End(ioe);
                            return;
                        }
                    }else{
                        String phone = web_Process.getMy_Request().getParameter("loading_Phone");
                        if(phone!=null){
                            try {
                                web_Process.launch_Task("Enty302", "&loading_Comp_Id="+StringPlus.http_Encode(organization.getComp_Id().getValue()) +
                                "&loading_Organization_Number="+StringPlus.http_Encode(organization.getOrganization_Number().toString()) +
                                "&loading_Phone"+StringPlus.http_Encode(phone)
                                );
                            }catch(ServletException se){
                                web_Process.error_End(se);
                                return;
                            }catch(IOException ioe){
                                web_Process.error_End(ioe);
                                return;
                            }
                            
                        } else {
                            String organization_Additional_E_Mail_Number = web_Process.getMy_Request().getParameter("loading_Organization_Additional_E_Mail_Number");
                            if(organization_Additional_E_Mail_Number!=null){
                                try {
                                    web_Process.launch_Task("Enty302", "&loading_Comp_Id="+StringPlus.http_Encode(organization.getComp_Id().getValue()) +
                                    "&loading_Organization_Number="+StringPlus.http_Encode(organization.getOrganization_Number().toString()) +
                                    "&loading_Organization_Additional_E_Mail_Number"+StringPlus.http_Encode(organization_Additional_E_Mail_Number)
                                    );
                                }catch(ServletException se){
                                    web_Process.error_End(se);
                                    return;
                                }catch(IOException ioe){
                                    web_Process.error_End(ioe);
                                    return;
                                }
                                
                            }else{
                                organization_View_Record_Do_Display(web_Process, organization);
                            }
                        }
                    }
                }
                return;
            }else{
                    organization_List_Records_Do_Display(web_Process);
             
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    public void organization_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
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
            }catch(WPException wpe){
                web_Process.error_End("Programming error.", wpe);
                return;
            }
        }
    }
    
    
    
    void organization_List_Records_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("Enty300_organization_List_Records", this, "action_Stack_Do_Action",  "organization_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            String form_Name="Enty300_List";
            String focus_Button_Form="Enty300_find";
            
            Comp comp = (Comp)web_Process.getAttribute("comp");
            if(comp== null){
                web_Process.error_End("Can't find comp attribute.", null);
                return;
            }
            
            
            Organization temp_Organization= new Organization();
            try {
                temp_Organization.setComp_Id(comp.getComp_Id().getValue());
            }catch(DFException dfe){
                web_Process.error_End(dfe);
                return;
            }
            
            
            
            
            
            
            
            
            web_Process.getMy_Response().setContentType("text/html");
            PrintWriter out = web_Process.getOut();
            
            web_Process.do_Display_HTML_Page_Begin(out);
            
            web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
            web_Process.do_Display_HTML_Head_End(out);
            
            web_Process.do_Display_HTML_Body_Begin(out,
            "onload=\"javascript:" +
            web_Process.DF_Focus_Call_For_On_Event(focus_Button_Form, temp_Organization.getOrganization_Number()) + "; " +
            "\" ");
            
            
            
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
            
            organization_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            
            String  organization_Where_SQL_Clause ="WHERE " +
            temp_Organization.getComp_Id().toWhere_Clause();
            
            
            Organization search_Organization;
            search_Organization = (Organization)web_Process.getAttribute("search_Organization");
            if(search_Organization==null){
                search_Organization = new Organization();
            }
            web_Process.setAttribute("search_Organization", search_Organization);
            
            if(!search_Organization.getName().getValue().equals("")  &&  search_Organization.getName().getRequest_Error().equals("")){
                organization_Where_SQL_Clause += " AND " + search_Organization.getName().toWhere_Range_Clause();
                
            }
            
            if(!search_Organization.getAlpha_Alias().getValue().equals("")  &&  search_Organization.getAlpha_Alias().getRequest_Error().equals("")){
                organization_Where_SQL_Clause += " AND " + search_Organization.getAlpha_Alias().toWhere_Range_Clause();
            }
            
            if(!search_Organization.getCity().getValue().equals("")  &&  search_Organization.getCity().getRequest_Error().equals("")){
                organization_Where_SQL_Clause += " AND " + search_Organization.getCity().toWhere_Range_Clause();
            }
            
            if(!search_Organization.getState_Abbr().getValue().equals("")  &&  search_Organization.getState_Abbr().getRequest_Error().equals("")){
                organization_Where_SQL_Clause += " AND " + search_Organization.getState_Abbr().toWhere_Range_Clause();
            }
            
            
            if(!search_Organization.getPhone().getValue().equals("")  &&  search_Organization.getPhone().getRequest_Error().equals("")){
                organization_Where_SQL_Clause += " AND " + search_Organization.getPhone().toWhere_Range_Clause();
            }
            
            if(!search_Organization.getE_Mail().getValue().equals("")  &&  search_Organization.getE_Mail().getRequest_Error().equals("")){
                organization_Where_SQL_Clause += " AND " + search_Organization.getE_Mail().toWhere_Range_Clause();
            }
            
            
            
            
            
            
            
            
            
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager organization_Display_List_Sort_Manager;
            
            organization_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("organization_Display_List_Sort_Manager");
            if(organization_Display_List_Sort_Manager==null){
                organization_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.setAttribute("organization_Display_List_Sort_Manager", organization_Display_List_Sort_Manager);
            String sort_SQL_Clause = organization_Display_List_Sort_Manager.getSort_SQL_Clause();
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager organization_Display_List_Pagination_Manager;
            try {
                organization_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("organization_Display_List_Pagination_Manager", temp_Organization, organization_Where_SQL_Clause, 10);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error.", dbfe);
                return;
            }
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
            list_Table.setUse_Even_Odd_Classes(true);
            list_Table.do_Display_HTML_Table_Begin_Std();
            list_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            Organization[] organization_List;
            try {
                organization_List = (Organization[]) web_Process.getDatabase_Front().getMany_Records(temp_Organization, organization_Where_SQL_Clause + " " + sort_SQL_Clause, organization_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), organization_Display_List_Pagination_Manager.getItems_Per_Page());
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            
            if(organization_List == null){
                list_Table.do_Display_HTML_Row_Begin_Std();
                list_Table.do_Display_HTML_TD_Element(" No Records Returned ");
                list_Table.do_Display_HTML_Row_End();
                
            }else{
                list_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Organization.getOrganization_Number(),  organization_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Organization.getName(),  organization_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Organization.getAlpha_Alias(),  organization_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Organization.getOrganization_Type_Id(),  organization_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Organization.getStreet_Address(),  organization_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Organization.getCity(),  organization_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Organization.getPhone(),  organization_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Organization.getE_Mail(),  organization_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Organization.getWeb_Site(),  organization_Display_List_Sort_Manager);
                list_Table.do_Display_HTML_Row_End();
                
                for(int row=0; row < organization_List.length; row++){
                    Organization organization = organization_List[row];
                    
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    try {
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, organization.getOrganization_Number(), "Select", organization, this, "organization_List_Records_Do_Action_Select");
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, organization.getName(), "Select", organization, this, "organization_List_Records_Do_Action_Select");
                    }catch(WPFatal_Exception wpe){
                        web_Process.error_End(wpe);
                        return;
                    }
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, organization.getAlpha_Alias());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, organization.getOrganization_Type_Id());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, organization.getStreet_Address());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, organization.getCity());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, organization.getPhone());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, organization.getE_Mail());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, organization.getWeb_Site());
                    
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
                out.println(web_Process.display_List_Pagination_Manager_To_Html(organization_Display_List_Pagination_Manager));
            }catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            
            
            try {
                
                web_Process.do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, focus_Button_Form, "action", "Go", "Go", "", temp_Organization.getOrganization_Number(), 15, comp, this, "organization_List_Records_Do_Action_Go");
                out.println("<br>");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form_With_Hidden_DBR(out, "Enty300_new", "action", "New", "New", "", comp, this, "organization_List_Records_Do_Action_New");
                out.println("<br>");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty300_end", "action", "End", "End", "", this, "std_Action_End");
                out.println("<br>");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty300_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                out.println("<br>");
                
                
                
                
                web_Process.do_Display_HTML_Form_Begin_Std(out, "Enty300_Search");
                String web_Root = web_Process.getSubscriber_Organization_Portal().getWeb_Root().toString();
                
                
                out.println("Search For:<br>");
                out.println("<br>");
                
                out.println(search_Organization.getName().getField_Title());
                out.println(search_Organization.getName().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                out.println("<br>");
                
                out.println(search_Organization.getAlpha_Alias().getField_Title());
                out.println(search_Organization.getAlpha_Alias().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                out.println("<br>");
                
                out.println(search_Organization.getCity().getField_Title());
                out.println(search_Organization.getCity().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                
                out.println(search_Organization.getState_Abbr().getField_Title());
                out.println(search_Organization.getState_Abbr().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                out.println("<br>");
                
                out.println(search_Organization.getPhone().getField_Title());
                out.println(search_Organization.getPhone().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                
                out.println(search_Organization.getE_Mail().getField_Title());
                out.println(search_Organization.getE_Mail().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                out.println("<br>");
                out.println("<br>");
                
                web_Process.do_Display_HTML_In_Form_Submit_Button("action", "Search", "", this, "organization_List_Records_Do_Action_Search");
                
                
                web_Process.do_Display_HTML_Form_End();
                
            }catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make buttons.", wpfe);
                return;
            }catch(WPException wpe){
                web_Process.error_End("Failed to make buttons.", wpe);
                return;
            }
            
            
            
            
            out.println("</div>");
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_List_Records_Do_Action_Go(Web_Process web_Process) {
        try {
            Organization temp_Organization=new Organization();
            
            try{
                temp_Organization.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Organization.getOrganization_Number().getField_Title()+".", dbre);
                return;
            }
            Organization organization;
            try{
                organization=(Organization)web_Process.getDatabase_Front().getRecord(temp_Organization);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }catch(Database_Front_No_Results_Exception dbfe){
                // no such Organization
                //organization_New_Record_Do_Display(web_Process, temp_Organization, null);
                web_Process.error("No Such "+ temp_Organization.getEntity_Name()+".",null);
                return;
            }
            // found a Organization;
            organization_View_Record_Do_Display(web_Process, organization);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_List_Records_Do_Action_Select(Web_Process web_Process) {
        try {
            Organization temp_Organization=new Organization();
            
            try{
                temp_Organization.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Organization.getOrganization_Number().getField_Title()+".", dbre);
                return;
            }
            Organization organization;
            try{
                organization=(Organization)web_Process.getDatabase_Front().getRecord(temp_Organization);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }catch(Database_Front_No_Results_Exception dbfe){
                // no such Organization
                web_Process.error("No Such "+ temp_Organization.getEntity_Name()+".",null);
                return;
            }
            // found a Organization;
            organization_View_Record_Do_Display(web_Process, organization);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void organization_List_Records_Do_Action_New(Web_Process web_Process) {
        
        try {
            Organization temp_Organization;
            temp_Organization=new Organization();
            
            try{
                temp_Organization.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Organization.getOrganization_Number().getField_Title()+".", dbre);
                return;
            }
            
            organization_New_Record_Do_Display(web_Process, temp_Organization, null);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_List_Records_Do_Action_Search(Web_Process web_Process) {
        
        Organization search_Organization = (Organization)web_Process.getAttribute("search_Organization");
        
        
        int ier;
        ier = search_Organization.getName().validate_Request(web_Process.getMy_Request());
        if(ier!=0)search_Organization.getName().setRequest_Error(" ");
        ier = search_Organization.getAlpha_Alias().validate_Request(web_Process.getMy_Request());
        ier = search_Organization.getCity().validate_Request(web_Process.getMy_Request());
        ier = search_Organization.getState_Abbr().validate_Request(web_Process.getMy_Request());
        if(ier!=0)search_Organization.getState_Abbr().setRequest_Error(" ");
        
        ier = search_Organization.getPhone().validate_Request(web_Process.getMy_Request());
        ier = search_Organization.getE_Mail().validate_Request(web_Process.getMy_Request());
        
        web_Process.setAttribute("search_Organization", search_Organization);
        organization_List_Records_Do_Display(web_Process);
        return;
    }
    
    
    public void organization_List_Records_Do_Action_Reload(Web_Process web_Process) {
        try {
            Organization    organization = new Organization();
            
            // look for sorting commands
            Display_List_Sort_Manager organization_Display_List_Sort_Manager;
            
            organization_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("organization_Display_List_Sort_Manager");
            if(organization_Display_List_Sort_Manager==null){
                organization_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.do_Process_Of_Sort_Manger_Request(organization_Display_List_Sort_Manager, organization);
            web_Process.setAttribute("organization_Display_List_Sort_Manager", organization_Display_List_Sort_Manager);
            
            // Look for paging commands
            Display_List_Pagination_Manager organization_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("organization_Display_List_Pagination_Manager");
            
            if(organization_Display_List_Pagination_Manager!=null){
                organization_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("organization_Display_List_Pagination_Manager", organization_Display_List_Pagination_Manager);
            }
            
            
            organization_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    void organization_New_Record_Do_Display(Web_Process web_Process, Organization organization, Exception ex) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty300_organization_New_Record", this, "action_Stack_Do_Action",  "organization_New_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        String form_Name="Enty300_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        
        try {
            Organization_Type_AutoCompletion.getJavaScript(out, web_Process, form_Name, organization.getOrganization_Type_Id().getField_Name(),  organization.getComp_Id().getField_Name(), this, "organization_Edit_And_New_Shared_Do_Action_Maint");
            Organization_Contact_Type_AutoCompletion.getJavaScript(out, web_Process, form_Name, organization.getPhone_Contact_Type().getField_Name(),  organization.getComp_Id().getField_Name(), organization.getOrganization_Type_Id().getField_Name(), this, "organization_Edit_And_New_Shared_Do_Action_Maint");
            Organization_Contact_Type_AutoCompletion.getJavaScript(out, web_Process, form_Name, organization.getFax_Contact_Type().getField_Name(),  organization.getComp_Id().getField_Name(), organization.getOrganization_Type_Id().getField_Name(), this, "organization_Edit_And_New_Shared_Do_Action_Maint");
            Organization_Contact_Type_AutoCompletion.getJavaScript(out, web_Process, form_Name, organization.getE_Mail_Contact_Type().getField_Name(),  organization.getComp_Id().getField_Name(), organization.getOrganization_Type_Id().getField_Name(), this, "organization_Edit_And_New_Shared_Do_Action_Maint");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("Programming Error", wpfe);
            return;
        }
        
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out,
        "onload=\"javascript:" +
        " "+organization.getOrganization_Type_Id().getField_Name()+"_Init(); " +
        " "+organization.getPhone_Contact_Type().getField_Name()+"_Init(); " +
        " "+organization.getFax_Contact_Type().getField_Name()+"_Init(); " +
        " "+organization.getE_Mail_Contact_Type().getField_Name()+"_Init(); " +
        
        web_Process.DF_Focus_Call_For_On_Event(form_Name, organization.getDescription()) + "; " +
        "\" ");
        
        
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
        
        organization_Edit_And_New_Shared_Main_Display(out, web_Process, organization, ex, "");
        
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "organization_New_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "organization_New_Record_Do_Action_Cancel");
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
        
        web_Process.setAttribute("organization", organization);
        
        out.close();
    }
    
    
    
    public void organization_New_Record_Do_Action_Create(Web_Process web_Process) {
        try {
            Organization organization = (Organization) web_Process.getAttribute("organization");
            
            try {
                organization.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                organization.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("organization", organization);
                organization_New_Record_Do_Display(web_Process, organization, dbre);
                return;
            }
            
            Comp comp = (Comp) web_Process.getAttribute("comp");
            
            
            try {
                organization.setOrganization_Number(Entity_Control.get_Next_Organization_Number(web_Process.getDatabase_Front(), comp.getComp_Id()));
            }catch(DFException dfe){
                web_Process.error_End("can't set Organization Number", dfe);
                return;
            }catch(Database_Record_Exception dbre){
                web_Process.error_End(dbre);
                return;
            }catch(Database_Front_Exception dfe){
                web_Process.error_End(dfe);
                return;
            }catch(Database_Front_No_Results_Exception dfe){
                web_Process.error_End("No Entity Control record set up for this company.  Use Enty001.", dfe);
                return;
            }catch(Database_Front_Deadlock_Exception dfe){
                web_Process.error(dfe);
                return;
            }
            
            
            
            
            
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(organization);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + organization.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_New_Record_Do_Action_Cancel(Web_Process web_Process) {
        web_Process.removeAttribute("organization");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void organization_New_Record_Do_Action_Reload(Web_Process web_Process) {
        Organization organization = (Organization) web_Process.getAttribute("organization");
        organization_New_Record_Do_Display(web_Process, organization, null);
        return;
    }
    
    
    
    void organization_View_Record_Do_Display(Web_Process web_Process, Organization organization) {
        
        try {
            web_Process.pushStatus_Wreload("Enty300_organization_View_Record", this, "action_Stack_Do_Action",  "organization_View_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        String form_Name="Enty300_View";
        
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
        
        organization_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
        
        organization_View_Record_Do_Display_Fields(out, web_Process, organization);
        
        
        
        organization_View_Record_Do_Display_Identity_Records(out, web_Process, organization);

        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "organization_View_Record_Do_Action_Edit");
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
        
        web_Process.setAttribute("organization", organization);
        
        out.close();
    }
    
    
    public void organization_View_Record_Do_Display_Fields(PrintWriter out, Web_Process web_Process, Organization organization) {
        //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
        
        HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_View_Table");
        
        record_Table.do_Display_HTML_Table_Begin_Std();
        record_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getOrganization_Number());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getOrganization_Type_Id());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getName());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getAlpha_Alias_Different_From_Name());
        if(organization.getAlpha_Alias_Different_From_Name().getValue().booleanValue())
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getAlpha_Alias());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getStreet_Address());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getCity());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getState_Abbr());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getZip_Code());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getDescription());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getPhone_Contact_Type());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getPhone());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getFax_Contact_Type());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getFax());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getE_Mail_Contact_Type());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getE_Mail());
        
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getWeb_Site());
        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getObsolete());
        
        
        
        record_Table.do_Display_HTML_TBody_End();
        record_Table.do_Display_HTML_Table_End();
    }
    
    
    
    
    public void organization_View_Record_Do_Display_Identity_Records(PrintWriter out, Web_Process web_Process, Organization organization) {
        try {
            Comp comp = (Comp)web_Process.getAttribute("comp");
            
            
            out.println("<div style='position: absolute; top: 50px;left: 550px; right: 0px' id='identity_Table' >");
            
            try {
                
                HTML_Table_Tag identity_Table = new HTML_Table_Tag("identity_Table", out, "Record_View_Table");
                
                //HTML_Table_Tag identity_Table = new HTML_Table_Tag("identity_Table", out, "Records_List_Table");
                //identity_Table.setUse_Even_Odd_Classes(true);
                
                identity_Table.do_Display_HTML_Table_Begin_Std();
                identity_Table.do_Display_HTML_TBody_Begin_Std();
                
                identity_Table.do_Display_HTML_Row_Begin_Std();
                identity_Table.do_Display_HTML_TH_Element("Identities");
                identity_Table.do_Display_HTML_TH_Element("#/ID");
                identity_Table.do_Display_HTML_Row_End();
                
                {
                    
                    Cust    cust;
                    try {
                        cust = organization.get_Aka_Cust(web_Process.getDatabase_Front());
                    }catch(WPFatal_Exception wpfe){
                        web_Process.error_End(wpfe);
                        return;
                    }
                    identity_Table.do_Display_HTML_Row_Begin_Std();
                    
                    if(cust != null){
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, cust.getCust_Id());
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(identity_Table, cust.getCust_Id(), "Select_Cust", comp, cust.getClass(), "custP_Select_Action");
                    }else{
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, new Cust().getCust_Id());
                        web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Become a Customer", "", identity_Table, organization.getClass(), "organizationA_Become_A_Customer_Action");
                    }
                    identity_Table.do_Display_HTML_Row_End();
                    
                }
                
                
//                {
//                    Vend    vend;
//                    try {
//                        vend = organization.get_Aka_Vend(web_Process.getDatabase_Front());
//                    }catch(WPFatal_Exception wpfe){
//                        web_Process.error_End(wpfe);
//                        return;
//                    }
//                    
//                    identity_Table.do_Display_HTML_Row_Begin_Std();
//                    if(vend != null){
//                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, vend.getVend_Number());
//                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(identity_Table, vend.getVend_Number(), "Select_Vend", comp, vend.getClass(), "vendP_Select_Action");
//                    }else{
//                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, new Vend().getVend_Number());
//                        web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Become a Vend", "", identity_Table, organization.getClass(), "organizationA_Become_A_Vend_Action");
//                    }
//                    identity_Table.do_Display_HTML_Row_End();
//                    
//                }

                
                {
                if(Subsystem.is_Subsystem_Installed(web_Process.getMy_Request().getSession(), "Enroll_Now_Performance")){
                    Venue    venue;
                    try {
                        venue = organization.get_Aka_Venue(web_Process.getDatabase_Front());
                    }catch(WPFatal_Exception wpfe){
                        web_Process.error_End(wpfe);
                        return;
                    }
                    
                    identity_Table.do_Display_HTML_Row_Begin_Std();
                    if(venue != null){
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, venue.getVenue_Id());
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(identity_Table, venue.getVenue_Id(), "Select_Venue", comp, venue.getClass(), "venueP_Select_Action");
                    }else{
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, new Venue().getVenue_Id());
                        web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Become a Venue", "", identity_Table, organization.getClass(), "organizationA_Become_A_Venue_Action");
                    }
                    identity_Table.do_Display_HTML_Row_End();
                    }
                }
              
               
                identity_Table.do_Display_HTML_TBody_End();
                identity_Table.do_Display_HTML_Table_End();
                
            }catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make buttons.", wpfe);
                return;
            }
            
            
            out.println("</div>");
            
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void organization_View_Record_Do_Action_Edit(Web_Process web_Process) {
        
        Organization organization = (Organization) web_Process.getAttribute("organization");
        
        try {
            organization = (Organization) web_Process.getDatabase_Front().getLocked_Record(organization);
        }catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + organization.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + organization.getEntity_Name() + " record in database. ", dbfe);
            return;
        }catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + organization.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        organization_Edit_Record_Do_Display(web_Process, organization, null);
        return;
    }
    
    
    public void organization_View_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Organization organization = (Organization) web_Process.getAttribute("organization");
        if(organization==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            organization_View_Record_Do_Display(web_Process, organization);
        }
        return;
    }
    
    
    
    
    
    
    
    
    void organization_Edit_Record_Do_Display(Web_Process web_Process, Organization organization, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty300_organization_Edit_Record", this, "action_Stack_Do_Action",  "organization_Edit_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        String form_Name="Enty300_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        
        try {
            Organization_Type_AutoCompletion.getJavaScript(out, web_Process, form_Name, organization.getOrganization_Type_Id().getField_Name(),  organization.getComp_Id().getField_Name(), this, "organization_Edit_And_New_Shared_Do_Action_Maint");
            Organization_Contact_Type_AutoCompletion.getJavaScript(out, web_Process, form_Name, organization.getPhone_Contact_Type().getField_Name(),  organization.getComp_Id().getField_Name(), organization.getOrganization_Type_Id().getField_Name(), this, "organization_Edit_And_New_Shared_Do_Action_Maint");
            Organization_Contact_Type_AutoCompletion.getJavaScript(out, web_Process, form_Name, organization.getFax_Contact_Type().getField_Name(),  organization.getComp_Id().getField_Name(), organization.getOrganization_Type_Id().getField_Name(), this, "organization_Edit_And_New_Shared_Do_Action_Maint");
            Organization_Contact_Type_AutoCompletion.getJavaScript(out, web_Process, form_Name, organization.getE_Mail_Contact_Type().getField_Name(),  organization.getComp_Id().getField_Name(), organization.getOrganization_Type_Id().getField_Name(), this, "organization_Edit_And_New_Shared_Do_Action_Maint");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("Programming Error", wpfe);
            return;
        }
        
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out,
        "onload=\"javascript:" +
        " "+organization.getPhone_Contact_Type().getField_Name()+"_Init(); " +
        " "+organization.getFax_Contact_Type().getField_Name()+"_Init(); " +
        " "+organization.getE_Mail_Contact_Type().getField_Name()+"_Init(); " +

        web_Process.DF_Focus_Call_For_On_Event(form_Name, organization.getDescription()) + "; " +
        "\" ");
        
        
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
        
        organization_Edit_And_New_Shared_Main_Display(out, web_Process, organization, ex, "");
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "organization_Edit_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "organization_Edit_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "organization_Edit_Record_Do_Action_Delete");
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
        
        web_Process.setAttribute("organization", organization);
        
        out.close();
        return;
    }
    
    
    
    public void organization_Edit_Record_Do_Action_Update(Web_Process web_Process) {
        
        try {
            Organization organization = (Organization) web_Process.getAttribute("organization");
            
            try {
                organization.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                organization.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("organization", organization);
                organization_Edit_Record_Do_Display(web_Process, organization, dbre);
                return;
            }
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(organization);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + organization.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void organization_Edit_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        Organization organization = (Organization) web_Process.getAttribute("organization");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + organization.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    public void organization_Edit_Record_Do_Action_Delete(Web_Process web_Process) {
        try {
            Organization organization = (Organization) web_Process.getAttribute("organization");
            try {
                organization.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
            }catch(Database_Record_Exception dbre){
                web_Process.error(dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().deleteRecord(organization);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to Delete " + organization.getEntity_Name() + " in database. ", dbfe);
                return;
            }
            web_Process.removeAttribute("organization");
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void organization_Edit_Record_Do_Action_End_Task(Web_Process web_Process) {
        
        Organization organization = (Organization) web_Process.getAttribute("organization");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + organization.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("organization");
        web_Process.end();
        return;
    }
    
    
    public void organization_Edit_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Organization organization = (Organization) web_Process.getAttribute("organization");
        organization_Edit_Record_Do_Display(web_Process, organization,null);
        return;
    }
    
    
    
    
    
    
    
    
    
    
    public void organization_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, Organization organization, Exception ex) {
        organization_Edit_And_New_Shared_Main_Display(out, web_Process, organization, ex, "");
        
    }
    
    public void organization_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, Organization organization, Exception ex, String last_Field_event) {
        
        organization_Edit_And_New_Shared_Superior_Display(out, web_Process, true);
        
        try {
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            {
                HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
                
                record_Table.do_Display_HTML_Table_Begin_Std();
                record_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getOrganization_Number());
                if(organization.getOrganization_Number().getValue().intValue()!=0){
                    web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getOrganization_Type_Id());
                    web_Process.do_Display_HTML_DF_As_Hidden_Input(organization.getOrganization_Type_Id());
                }
                
                record_Table.do_Display_HTML_TBody_End();
                record_Table.do_Display_HTML_Table_End();
            }
            
            if(organization.getOrganization_Number().getValue().intValue()==0){
                
                
                {
                    HTML_Table_Tag record_TableA = new HTML_Table_Tag("record_TableA", out, "Record_Edit_Table");
                    
                    record_TableA.do_Display_HTML_Table_Begin_Std();
                    record_TableA.do_Display_HTML_TBody_Begin_Std();
                    
                    record_TableA.do_Display_HTML_Row_Begin_Std();
                    
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_TableA, organization.getOrganization_Type_Id());
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_TD("",record_TableA, organization.getOrganization_Type_Id(),
                    "onkeyup='"+organization.getOrganization_Type_Id().getField_Name()+"_DoCompletion()'");
                    record_TableA.do_Display_HTML_Row_End();
                    
                    
                    
                    record_TableA.do_Display_HTML_TBody_End();
                    record_TableA.do_Display_HTML_Table_End();
                }
                
                Organization_Type_AutoCompletion.getDivTable_Static(out, organization.getOrganization_Type_Id().getField_Name());
                
            }
            
            {
                HTML_Table_Tag record_TableB = new HTML_Table_Tag("record_TableB", out, "Record_Edit_Table");
                
                record_TableB.do_Display_HTML_Table_Begin_Std();
                record_TableB.do_Display_HTML_TBody_Begin_Std();
                
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, organization.getName());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, organization.getAlpha_Alias_Different_From_Name());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, organization.getAlpha_Alias());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, organization.getStreet_Address());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, organization.getCity());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, organization.getState_Abbr());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, organization.getZip_Code());
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, organization.getDescription());
                
                
                record_TableB.do_Display_HTML_TBody_End();
                record_TableB.do_Display_HTML_Table_End();
            }
            
            
            {
                HTML_Table_Tag record_TableC = new HTML_Table_Tag("record_TableC", out, "Record_Edit_Table");
                
                record_TableC.do_Display_HTML_Table_Begin_Std();
                record_TableC.do_Display_HTML_TBody_Begin_Std();
                
                    record_TableC.do_Display_HTML_Row_Begin_Std();
                    
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_TableC, organization.getPhone_Contact_Type());
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_TD("",record_TableC, organization.getPhone_Contact_Type(),
                    "onkeyup='"+organization.getPhone_Contact_Type().getField_Name()+"_DoCompletion()'");
                    record_TableC.do_Display_HTML_Row_End();
                    
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableC, organization.getPhone());
                
                
                record_TableC.do_Display_HTML_TBody_End();
                record_TableC.do_Display_HTML_Table_End();
            }
            Organization_Contact_Type_AutoCompletion.getDivTable_Static(out, organization.getPhone_Contact_Type().getField_Name());
            
            
            {
                HTML_Table_Tag record_TableD = new HTML_Table_Tag("record_TableD", out, "Record_Edit_Table");
                
                record_TableD.do_Display_HTML_Table_Begin_Std();
                record_TableD.do_Display_HTML_TBody_Begin_Std();
                
                
                    record_TableD.do_Display_HTML_Row_Begin_Std();
                    
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_TableD, organization.getFax_Contact_Type());
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_TD("",record_TableD, organization.getFax_Contact_Type(),
                    "onkeyup='"+organization.getFax_Contact_Type().getField_Name()+"_DoCompletion()'");
                    record_TableD.do_Display_HTML_Row_End();
                    
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableD, organization.getFax());
                
                
                record_TableD.do_Display_HTML_TBody_End();
                record_TableD.do_Display_HTML_Table_End();
            }
            Organization_Contact_Type_AutoCompletion.getDivTable_Static(out, organization.getFax_Contact_Type().getField_Name());

            
            
            
            {
                HTML_Table_Tag record_TableE = new HTML_Table_Tag("record_TableE", out, "Record_Edit_Table");
                
                record_TableE.do_Display_HTML_Table_Begin_Std();
                record_TableE.do_Display_HTML_TBody_Begin_Std();
                
                
                    record_TableE.do_Display_HTML_Row_Begin_Std();
                    
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_TableE, organization.getE_Mail_Contact_Type());
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_TD("",record_TableE, organization.getE_Mail_Contact_Type(),
                    "onkeyup='"+organization.getE_Mail_Contact_Type().getField_Name()+"_DoCompletion()'");
                    record_TableE.do_Display_HTML_Row_End();
                    
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableE, organization.getE_Mail());
                
                
                record_TableE.do_Display_HTML_TBody_End();
                record_TableE.do_Display_HTML_Table_End();
            }
            Organization_Contact_Type_AutoCompletion.getDivTable_Static(out, organization.getE_Mail_Contact_Type().getField_Name());

            
            {
                HTML_Table_Tag record_TableF = new HTML_Table_Tag("record_TableF", out, "Record_Edit_Table");
                
                record_TableF.do_Display_HTML_Table_Begin_Std();
                record_TableF.do_Display_HTML_TBody_Begin_Std();
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableF, organization.getWeb_Site());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableF, organization.getObsolete(),  last_Field_event);
                
                
                record_TableF.do_Display_HTML_TBody_End();
                record_TableF.do_Display_HTML_Table_End();
            }
            
            
            if(ex!=null){
                out.println("<p>Error:"+StringPlus.html_Encode(ex.getMessage()));
            }
            
        }catch(WPException wpe){
            web_Process.error_End("Programming error.", wpe);
            return;
        }
        
    }
    
    
    
    
    
    public void organization_Edit_And_New_Shared_Do_Action_Maint(Web_Process web_Process) {
        
        try {
            Organization organization = (Organization) web_Process.getAttribute("organization");
            
            
            try {
                organization.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                organization.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                ;
            }
            web_Process.setAttribute("organization", organization);
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
        return;
    }
    
    
}
