/*
 * Get_Family.java
 *
 * Created on September 13, 2006, 5:15 PM
 */

package entity_Management;

import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.*;
import com.amlibtech.util.*;
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
public class Get_Family extends WPHttpServlet_Base {
    
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Get a Family.";
    }
    
    
    public void do_Status_Routing(Web_Process web_Process, String action) {
        
        try {
            
            if(web_Process.getStatus_Node().getStatus_Text().equals("loading")){
                
                web_Process.resetStatus_Stack(new WPStatus_Node("exit"));
                if(action.equals("Start") || action.equals("Reload")){
                    
                    String title = web_Process.getMy_Request().getParameter("title");
                    if(title==null){
                        title="Please Select a Family.";
                    }
                    web_Process.setAttribute("title", title);
                    
                    String return_As = web_Process.getMy_Request().getParameter("return_As");
                    if(return_As==null){
                        web_Process.error_End("Get_Family must be called with a 'return_As' parameter.", null);
                        return;
                    }
                    web_Process.setAttribute("return_As", return_As);
                    
                    String comp_Id = web_Process.getMy_Request().getParameter("loading_Comp_Id");
                    if(comp_Id==null){
                        web_Process.error_End("Get_Family must be called with a 'loading_Comp_Id' parameter.", null);
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
                        // no such Family
                        web_Process.error_End("No such Company Id :"+ comp_Id +":.", null);
                        return;
                    }
                    web_Process.setAttribute("comp", comp);
                    family_List_Records_Do_Display(web_Process);
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
    
    
    
    
    
    
    public String create_Person_Joining_Where_Clause(Comp comp, Person temp_Person){
        String where_SQL_Clause = "JOIN "+ temp_Person.get_Table_Name() + " USING("+
        temp_Person.getComp_Id().getField_Name()+", "+ temp_Person.getPerson_Number().getField_Name()+") " +
        " WHERE " +
        temp_Person.get_Table_Name() + "." + comp.getComp_Id().toWhere_Clause();
        return where_SQL_Clause;
    }
    
    
    public String create_Organization_Joining_Where_Clause(Comp comp, Organization temp_Organization){
        String where_SQL_Clause = "JOIN "+ temp_Organization.get_Table_Name() + " USING("+
        temp_Organization.getComp_Id().getField_Name()+", "+ temp_Organization.getOrganization_Number().getField_Name()+") " +
        " WHERE " +
        temp_Organization.get_Table_Name() + "." + comp.getComp_Id().toWhere_Clause();
        return where_SQL_Clause;
    }
    
    
    
    
    void family_List_Records_Do_Display(Web_Process web_Process) {
        try {
            web_Process.pushStatus_Wreload("Get_Family_family_List_Records", this, "action_Stack_Do_Action",  "family_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            
            String form_Name="Get_Family_List";
            String focus_Button_Form="Get_Family_find";
            
            Comp comp = (Comp)web_Process.getAttribute("comp");
            if(comp== null){
                web_Process.error_End("Can't find comp attribute.", null);
                return;
            }
            
            
            
            String  where_SQL_Clause ="WHERE " +
            comp.getComp_Id().toWhere_Clause();
            String where_Clause_Type="non joining";
            
            
            
            
            Family search_Family;
            search_Family = (Family)web_Process.getAttribute("search_Family");
            if(search_Family==null){
                search_Family = new Family();
            }
            web_Process.setAttribute("search_Family", search_Family);
            
            
            Person search_Person;
            search_Person = (Person)web_Process.getAttribute("search_Person");
            if(search_Person==null){
                search_Person = new Person();
            }
            web_Process.setAttribute("search_Person", search_Person);
            
            Organization search_Organization;
            search_Organization = (Organization)web_Process.getAttribute("search_Organization");
            if(search_Organization==null){
                search_Organization = new Organization();
            }
            web_Process.setAttribute("search_Organization", search_Organization);
            
            
            if(where_Clause_Type.equals("non joining") || where_Clause_Type.equals("person joining") ){
                if(!search_Person.getLast_Name().getValue().equals("")  &&  search_Person.getLast_Name().getRequest_Error().equals("")){
                    if(where_Clause_Type.equals("non joining")){
                        where_SQL_Clause = create_Person_Joining_Where_Clause(comp, search_Person);
                        where_Clause_Type="person joining";
                    }
                    where_SQL_Clause = where_SQL_Clause + " AND " + search_Person.getLast_Name().toWhere_Range_Clause(search_Person.get_Table_Name() + ".");
                }
            }
            
            if(where_Clause_Type.equals("non joining") || where_Clause_Type.equals("person joining") ){
                if(!search_Person.getFirst_Name().getValue().equals("")  &&  search_Person.getFirst_Name().getRequest_Error().equals("")){
                    if(where_Clause_Type.equals("non joining")){
                        where_SQL_Clause = create_Person_Joining_Where_Clause(comp, search_Person);
                        where_Clause_Type="person joining";
                    }
                    where_SQL_Clause = where_SQL_Clause + " AND " + search_Person.getFirst_Name().toWhere_Range_Clause(search_Person.get_Table_Name() + ".");
                }
            }
            
            if(where_Clause_Type.equals("non joining") || where_Clause_Type.equals("person joining") ){
                if(!search_Person.getCity().getValue().equals("")  &&  search_Person.getCity().getRequest_Error().equals("")){
                    if(where_Clause_Type.equals("non joining")){
                        where_SQL_Clause = create_Person_Joining_Where_Clause(comp, search_Person);
                        where_Clause_Type="person joining";
                    }
                    where_SQL_Clause = where_SQL_Clause + " AND " + search_Person.getCity().toWhere_Range_Clause(search_Person.get_Table_Name() + ".");
                }
            }
            
            if(where_Clause_Type.equals("non joining") || where_Clause_Type.equals("person joining") ){
                if(!search_Person.getPhone().getValue().equals("")  &&  search_Person.getPhone().getRequest_Error().equals("")){
                    if(where_Clause_Type.equals("non joining")){
                        where_SQL_Clause = create_Person_Joining_Where_Clause(comp, search_Person);
                        where_Clause_Type="person joining";
                    }
                    where_SQL_Clause = where_SQL_Clause + " AND " + search_Person.get_Table_Name() + "." + search_Person.getPhone().toWhere_Clause();
                }
            }
            
            if(where_Clause_Type.equals("non joining") || where_Clause_Type.equals("person joining") ){
                if(!search_Person.getE_Mail().getValue().equals("")  &&  search_Person.getE_Mail().getRequest_Error().equals("")){
                    if(where_Clause_Type.equals("non joining")){
                        where_SQL_Clause = create_Person_Joining_Where_Clause(comp, search_Person);
                        where_Clause_Type="person joining";
                    }
                    where_SQL_Clause = where_SQL_Clause + " AND " + search_Person.get_Table_Name() + "." + search_Person.getE_Mail().toWhere_Clause();
                }
            }
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager family_Display_List_Sort_Manager;
            
            family_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("family_Display_List_Sort_Manager");
            if(family_Display_List_Sort_Manager==null){
                family_Display_List_Sort_Manager = new Display_List_Sort_Manager("family.");
            }
            
            web_Process.setAttribute("family_Display_List_Sort_Manager", family_Display_List_Sort_Manager);
            String family_Sort_SQL_Clause = family_Display_List_Sort_Manager.getSort_SQL_Clause(search_Family.get_Table_Name());
            
            
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager person_Display_List_Sort_Manager;
            
            person_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("person_Display_List_Sort_Manager");
            if(person_Display_List_Sort_Manager==null){
                person_Display_List_Sort_Manager = new Display_List_Sort_Manager("person.");
            }
            
            web_Process.setAttribute("person_Display_List_Sort_Manager", person_Display_List_Sort_Manager);
            String person_Sort_SQL_Clause = person_Display_List_Sort_Manager.getSort_SQL_Clause(search_Person.get_Table_Name());
            
            
            
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager organization_Display_List_Sort_Manager;
            
            organization_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("organization_Display_List_Sort_Manager");
            if(organization_Display_List_Sort_Manager==null){
                organization_Display_List_Sort_Manager = new Display_List_Sort_Manager("organization.");
            }
            
            web_Process.setAttribute("organization_Display_List_Sort_Manager", organization_Display_List_Sort_Manager);
            String organization_Sort_SQL_Clause = organization_Display_List_Sort_Manager.getSort_SQL_Clause(search_Organization.get_Table_Name());
            
            // combine sort clauses
            String sort_SQL_Clause="";
            if(!family_Sort_SQL_Clause.equals("")){
                if(sort_SQL_Clause.equals("")) sort_SQL_Clause="ORDER BY "; else sort_SQL_Clause=sort_SQL_Clause+", ";
                // remove sort clause from family_Sort_SQL_Clause
                sort_SQL_Clause = sort_SQL_Clause + family_Sort_SQL_Clause.trim().substring("ORDER BY".length());
            }
            if(!person_Sort_SQL_Clause.equals("")){
                if(where_Clause_Type.equals("non joining")){
                    where_SQL_Clause = create_Person_Joining_Where_Clause(comp, search_Person);
                    where_Clause_Type="person joining";
                }
                if(where_Clause_Type.equals("person joining")){
                    if(sort_SQL_Clause.equals("")) sort_SQL_Clause="ORDER BY "; else sort_SQL_Clause=sort_SQL_Clause+", ";
                    // remove sort clause from person_Sort_SQL_Clause
                    sort_SQL_Clause = sort_SQL_Clause + person_Sort_SQL_Clause.trim().substring("ORDER BY".length());
                }
            }
            if(!organization_Sort_SQL_Clause.equals("")){
                if(where_Clause_Type.equals("non joining")){
                    where_SQL_Clause = create_Organization_Joining_Where_Clause(comp, search_Organization);
                    where_Clause_Type="organization joining";
                }
                if(where_Clause_Type.equals("organization joining")){
                    if(sort_SQL_Clause.equals("")) sort_SQL_Clause="ORDER BY "; else sort_SQL_Clause=sort_SQL_Clause+", ";
                    // remove sort clause from organization_Sort_SQL_Clause
                    sort_SQL_Clause = sort_SQL_Clause + organization_Sort_SQL_Clause.trim().substring("ORDER BY".length());
                }
            }
            
            
            
            
            
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager family_Display_List_Pagination_Manager;
            try {
                family_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("family_Display_List_Pagination_Manager", new Family(), where_SQL_Clause, 10);
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
            
            web_Process.do_Display_HTML_Body_Begin_Plain(out);
            
            
            
            out.println("<div id='above_Header'>");
            out.println("</div>");
            
            out.println("<div id='header'>");
            out.println("<div id='header_Text'>");
            out.println(web_Process.getProgram().getProgram_Title().toHTML());
            out.println("</div>");
            out.println("</div>");
            
            
            
            
            
            out.println("<div id='main'>");
            
            
            
            
            family_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
            list_Table.setUse_Even_Odd_Classes(true);
            list_Table.do_Display_HTML_Table_Begin_Std();
            list_Table.do_Display_HTML_TBody_Begin_Std();
            
            Family[] family_List;
            try {
                family_List = (Family[]) web_Process.getDatabase_Front().getMany_Records(new Family(), where_SQL_Clause + " " + sort_SQL_Clause, family_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), family_Display_List_Pagination_Manager.getItems_Per_Page());
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            boolean first_Family_Person=true;
            boolean first_Family_Organization=true;
            
            Person person;
            Organization organization;
            
            if(family_List == null){
                out.println("<tr><td> No Records Returned </td></tr>");
                
            }else{
                for(int row=0; row < family_List.length; row++){
                    Family family = family_List[row];
                    person = Person.get_Possible_Person(web_Process.getDatabase_Front(), family.getComp_Id().getValue(), family.getPerson_Number().getValue());
                    
                    if(first_Family_Person){
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, family.getFamily_Number(),  family_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, family.getPerson_Number(),  family_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, person.getFirst_Name(),  person_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, person.getLast_Name(),  person_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, person.getStreet_Address(),  person_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, person.getCity(),  person_Display_List_Sort_Manager);
                        list_Table.do_Display_HTML_Row_End();
                        first_Family_Person=false;
                    }
                    
                    try {
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, family.getFamily_Number(), "Select", comp, this, "family_List_Records_Do_Action_Select");
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, family.getPerson_Number(), "Select_Person", comp, person.getClass(), "personP_Select_Action");
                        web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, person.getFirst_Name());
                        web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, person.getLast_Name());
                        web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, person.getStreet_Address());
                        web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, person.getCity());
                        list_Table.do_Display_HTML_Row_End();
                    }catch(WPFatal_Exception wpfe){
                        web_Process.error_End(wpfe);
                        return;
                    }
                    
                    
                }
            }
            
            list_Table.do_Display_HTML_TBody_End();
            list_Table.do_Display_HTML_Table_End();
            
            
            //        try {
            //            web_Process.do_Display_HTML_Form_End();
            //        }
            //        catch(WPException wpe){
            //            web_Process.error_End("Failed to end form.", wpe);
            //            return;
            //        }
            
            
            try {
                out.println(web_Process.display_List_Pagination_Manager_To_Html(family_Display_List_Pagination_Manager));
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            
            
            
            
            
            
            
            if(!search_Person.getCity().getValue().equals("")  &&  search_Person.getCity().getRequest_Error().equals("")){
                
                
                Person_Additional_Address temp_Person_Additional_Address = new Person_Additional_Address();
                
                
                String address_Where_SQL_Clause = 
                " JOIN " + search_Person.get_Table_Name() + " USING("+
                search_Family.getComp_Id().getField_Name()+", "+ search_Family.getPerson_Number().getField_Name()+") " +
                " JOIN "+ search_Family.get_Table_Name() +  " USING("+
                search_Family.getComp_Id().getField_Name()+", "+ search_Family.getPerson_Number().getField_Name()+") " +

                " WHERE " +
                search_Person.getCity().toWhere_Range_Clause(temp_Person_Additional_Address.get_Table_Name() + ".");
                
                if(!search_Person.getLast_Name().getValue().equals("")  &&  search_Person.getLast_Name().getRequest_Error().equals("")){
                    address_Where_SQL_Clause = address_Where_SQL_Clause + " AND " +
                    search_Person.getLast_Name().toWhere_Range_Clause();
                }
                
                if(!search_Person.getFirst_Name().getValue().equals("")  &&  search_Person.getFirst_Name().getRequest_Error().equals("")){
                    address_Where_SQL_Clause = address_Where_SQL_Clause + " AND " +
                    search_Person.getFirst_Name().toWhere_Range_Clause();
                }
                
                
                // get sort SQL clause from Display List Sort Manager
                Display_List_Sort_Manager address_Display_List_Sort_Manager;
                
                address_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("address_Display_List_Sort_Manager");
                if(address_Display_List_Sort_Manager==null){
                    address_Display_List_Sort_Manager = new Display_List_Sort_Manager("address.");
                }
                
                web_Process.setAttribute("address_Display_List_Sort_Manager", address_Display_List_Sort_Manager);
                String address_Sort_SQL_Clause = address_Display_List_Sort_Manager.getSort_SQL_Clause();
                if(address_Sort_SQL_Clause.equals("")){
                    address_Sort_SQL_Clause=" ORDER BY ";
                }else{
                    address_Sort_SQL_Clause = address_Sort_SQL_Clause +", ";
                }
                address_Sort_SQL_Clause = address_Sort_SQL_Clause + temp_Person_Additional_Address.get_Table_Name() + "." + temp_Person_Additional_Address.getCity().getField_Name() + " ASC ";
                
                // get or Initialize Display List Pagination Manager
                Display_List_Pagination_Manager address_Display_List_Pagination_Manager;
                try {
                    address_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("address", "address_Display_List_Pagination_Manager", new Person_Additional_Address(), address_Where_SQL_Clause, 10);
                }
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error.", dbfe);
                    return;
                }
                
                
                HTML_Table_Tag address_List_Table = new HTML_Table_Tag("address_List_Table", out, "Records_List_Table");
                address_List_Table.setUse_Even_Odd_Classes(true);
                address_List_Table.do_Display_HTML_Table_Begin_Std();
                address_List_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                Person_Additional_Address[] person_Additional_Address_List;
                try {
                    person_Additional_Address_List = (Person_Additional_Address[]) web_Process.getDatabase_Front().getMany_Records(temp_Person_Additional_Address, address_Where_SQL_Clause + " " + address_Sort_SQL_Clause, address_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), address_Display_List_Pagination_Manager.getItems_Per_Page());
                }
                
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error", dbfe);
                    return;
                }
                
                address_List_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, search_Family.getFamily_Number());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, search_Family.getPerson_Number());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(address_List_Table, temp_Person_Additional_Address.getPerson_Additional_Address_Number(),  address_Display_List_Sort_Manager);
                
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(address_List_Table, search_Person.getFirst_Name(),  address_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(address_List_Table, search_Person.getLast_Name(),  address_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person_Additional_Address.getStreet_Address());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person_Additional_Address.getCity());
                address_List_Table.do_Display_HTML_Row_End();
                
                if(person_Additional_Address_List == null){
                    out.println("<tr><td> No Records Returned </td></tr>");
                    
                }else{
                    for(int row=0; row < person_Additional_Address_List.length; row++){
                        Person_Additional_Address person_Additional_Address = person_Additional_Address_List[row];
                        person=null;
                        try{
                            person = (Person)web_Process.getDatabase_Front().getRecord(new Person(person_Additional_Address.getComp_Id().getValue(), person_Additional_Address.getPerson_Number().getValue()));
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }catch(Database_Front_No_Results_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }
                        
                        Family[] familys=null;
                        try {
                            familys = (Family[]) web_Process.getDatabase_Front().getMany_Records(new Family(), " WHERE " +
                            person.getComp_Id().toWhere_Clause() + " AND " +
                            person.getPerson_Number().toWhere_Clause());
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }
                        if(familys==null)
                            continue;
                        
                        Family family = familys[0];
                        
                        try {
                            address_List_Table.do_Display_HTML_Row_Begin_Std();
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(address_List_Table, family.getFamily_Number(), "Select", comp, this, "family_List_Records_Do_Action_Select");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(address_List_Table, person_Additional_Address.getPerson_Number(), "Select_Person", comp, person.getClass(), "personP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(address_List_Table, person_Additional_Address.getPerson_Additional_Address_Number(), "Address_Select", person, person_Additional_Address.getClass(), "person_Additional_AddressP_Select_Action");
                            
                            web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person.getFirst_Name());
                            web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person.getLast_Name());
                            web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person_Additional_Address.getStreet_Address());
                            web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person_Additional_Address.getCity());
                            
                            
                            address_List_Table.do_Display_HTML_Row_End();
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        
                    }
                }
                
                
                
                address_List_Table.do_Display_HTML_TBody_End();
                address_List_Table.do_Display_HTML_Table_End();
                try {
                    out.println(web_Process.display_List_Pagination_Manager_To_Html(address_Display_List_Pagination_Manager));
                }
                catch(WPException wpe){
                    web_Process.error_End("Failed to display pagination manager.", wpe);
                    return;
                }
            }
            
            
            
            
            
            
            
            if(!search_Person.getPhone().getValue().equals("")  &&  search_Person.getPhone().getRequest_Error().equals("")){
                
                
                Person_Additional_Phone temp_Person_Additional_Phone = new Person_Additional_Phone();
                
                
                String phone_Where_SQL_Clause =
                " JOIN " + search_Person.get_Table_Name() + " USING("+
                search_Person.getComp_Id().getField_Name()+", "+ search_Person.getPerson_Number().getField_Name()+") " +
                " JOIN "+ search_Family.get_Table_Name() + " USING("+
                search_Person.getComp_Id().getField_Name()+", "+ search_Person.getPerson_Number().getField_Name()+") " +
                " WHERE " +
                temp_Person_Additional_Phone.get_Table_Name() + "." + search_Person.getPhone().toWhere_Clause();
                
                if(!search_Person.getLast_Name().getValue().equals("")  &&  search_Person.getLast_Name().getRequest_Error().equals("")){
                    phone_Where_SQL_Clause = phone_Where_SQL_Clause + " AND " +
                    search_Person.getLast_Name().toWhere_Range_Clause();
                }
                
                if(!search_Person.getFirst_Name().getValue().equals("")  &&  search_Person.getFirst_Name().getRequest_Error().equals("")){
                    phone_Where_SQL_Clause = phone_Where_SQL_Clause + " AND " +
                    search_Person.getFirst_Name().toWhere_Range_Clause();
                }
                
                
                // get sort SQL clause from Display List Sort Manager
                Display_List_Sort_Manager phone_Display_List_Sort_Manager;
                
                phone_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("phone_Display_List_Sort_Manager");
                if(phone_Display_List_Sort_Manager==null){
                    phone_Display_List_Sort_Manager = new Display_List_Sort_Manager("Phone.");
                }
                
                web_Process.setAttribute("phone_Display_List_Sort_Manager", phone_Display_List_Sort_Manager);
                String phone_Sort_SQL_Clause = phone_Display_List_Sort_Manager.getSort_SQL_Clause();
                if(phone_Sort_SQL_Clause.equals("")){
                    phone_Sort_SQL_Clause=" ORDER BY ";
                }else{
                    phone_Sort_SQL_Clause = phone_Sort_SQL_Clause +", ";
                }
                phone_Sort_SQL_Clause = phone_Sort_SQL_Clause + temp_Person_Additional_Phone.get_Table_Name() + "." + temp_Person_Additional_Phone.getPhone().getField_Name() + " ASC ";
                
                // get or Initialize Display List Pagination Manager
                Display_List_Pagination_Manager phone_Display_List_Pagination_Manager;
                try {
                    phone_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("Phone", "phone_Display_List_Pagination_Manager", new Person_Additional_Phone(), phone_Where_SQL_Clause, 10);
                }
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error.", dbfe);
                    return;
                }
                
                
                HTML_Table_Tag phone_List_Table = new HTML_Table_Tag("phone_List_Table", out, "Records_List_Table");
                phone_List_Table.setUse_Even_Odd_Classes(true);
                phone_List_Table.do_Display_HTML_Table_Begin_Std();
                phone_List_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                Person_Additional_Phone[] person_Additional_Phone_List;
                try {
                    person_Additional_Phone_List = (Person_Additional_Phone[]) web_Process.getDatabase_Front().getMany_Records(temp_Person_Additional_Phone, phone_Where_SQL_Clause + " " + phone_Sort_SQL_Clause, phone_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), phone_Display_List_Pagination_Manager.getItems_Per_Page());
                }
                
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error", dbfe);
                    return;
                }
                
                phone_List_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(phone_List_Table, search_Family.getFamily_Number());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(phone_List_Table, temp_Person_Additional_Phone.getPhone(),  phone_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(phone_List_Table, temp_Person_Additional_Phone.getPhone_Type());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(phone_List_Table, search_Person.getPerson_Number());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(phone_List_Table, search_Person.getFirst_Name(),  phone_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(phone_List_Table, search_Person.getLast_Name(),  phone_Display_List_Sort_Manager);
                phone_List_Table.do_Display_HTML_Row_End();
                
                if(person_Additional_Phone_List == null){
                    out.println("<tr><td> No Records Returned </td></tr>");
                    
                }else{
                    for(int row=0; row < person_Additional_Phone_List.length; row++){
                        Person_Additional_Phone person_Additional_Phone = person_Additional_Phone_List[row];
                        person=null;
                        try{
                            person = (Person)web_Process.getDatabase_Front().getRecord(new Person(person_Additional_Phone.getComp_Id().getValue(), person_Additional_Phone.getPerson_Number().getValue()));
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }catch(Database_Front_No_Results_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }
                        
                        Family[] familys=null;
                        try {
                            familys = (Family[]) web_Process.getDatabase_Front().getMany_Records(new Family(), " WHERE " +
                            person.getComp_Id().toWhere_Clause() + " AND " +
                            person.getPerson_Number().toWhere_Clause());
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }
                        if(familys==null)
                            continue;
                        
                        Family family = familys[0];
                        
                        try {
                            phone_List_Table.do_Display_HTML_Row_Begin_Std();
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(phone_List_Table, family.getFamily_Number(), "Select", comp, this, "family_List_Records_Do_Action_Select");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(phone_List_Table, person_Additional_Phone.getPhone(), "Phone_Select", person, person_Additional_Phone.getClass(), "person_Additional_PhoneP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table(phone_List_Table, person_Additional_Phone.getPhone_Type());
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(phone_List_Table, person_Additional_Phone.getPerson_Number(), "Select_Person", comp, person.getClass(), "personP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table(phone_List_Table, person.getFirst_Name());
                            web_Process.do_Display_HTML_DF_Display_In_Table(phone_List_Table, person.getLast_Name());
                            
                            phone_List_Table.do_Display_HTML_Row_End();
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        
                    }
                }
                
                phone_List_Table.do_Display_HTML_TBody_End();
                phone_List_Table.do_Display_HTML_Table_End();
                try {
                    out.println(web_Process.display_List_Pagination_Manager_To_Html(phone_Display_List_Pagination_Manager));
                }
                catch(WPException wpe){
                    web_Process.error_End("Failed to display pagination manager.", wpe);
                    return;
                }
            }
            
            
            
            
            
            
            
            if(!search_Person.getE_Mail().getValue().equals("")  &&  search_Person.getE_Mail().getRequest_Error().equals("")){
                
                
                Person_Additional_E_Mail temp_Person_Additional_E_Mail = new Person_Additional_E_Mail();
                
                
                String e_Mail_Where_SQL_Clause =
                " JOIN " + search_Person.get_Table_Name() + " USING("+
                search_Person.getComp_Id().getField_Name()+", "+ search_Person.getPerson_Number().getField_Name()+") " +
                " JOIN "+ search_Family.get_Table_Name() + " USING("+
                search_Person.getComp_Id().getField_Name()+", "+ search_Person.getPerson_Number().getField_Name()+") " +
                " WHERE " +
                temp_Person_Additional_E_Mail.get_Table_Name() + "." + search_Person.getE_Mail().toWhere_Clause();
                
                if(!search_Person.getLast_Name().getValue().equals("")  &&  search_Person.getLast_Name().getRequest_Error().equals("")){
                    e_Mail_Where_SQL_Clause = e_Mail_Where_SQL_Clause + " AND " +
                    search_Person.getLast_Name().toWhere_Range_Clause();
                }
                
                if(!search_Person.getFirst_Name().getValue().equals("")  &&  search_Person.getFirst_Name().getRequest_Error().equals("")){
                    e_Mail_Where_SQL_Clause = e_Mail_Where_SQL_Clause + " AND " +
                    search_Person.getFirst_Name().toWhere_Range_Clause();
                }
                
                
                // get sort SQL clause from Display List Sort Manager
                Display_List_Sort_Manager e_Mail_Display_List_Sort_Manager;
                
                e_Mail_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("e_Mail_Display_List_Sort_Manager");
                if(e_Mail_Display_List_Sort_Manager==null){
                    e_Mail_Display_List_Sort_Manager = new Display_List_Sort_Manager("E_Mail.");
                }
                
                web_Process.setAttribute("e_Mail_Display_List_Sort_Manager", e_Mail_Display_List_Sort_Manager);
                String e_Mail_Sort_SQL_Clause = e_Mail_Display_List_Sort_Manager.getSort_SQL_Clause();
                if(e_Mail_Sort_SQL_Clause.equals("")){
                    e_Mail_Sort_SQL_Clause=" ORDER BY ";
                }else{
                    e_Mail_Sort_SQL_Clause = e_Mail_Sort_SQL_Clause +", ";
                }
                e_Mail_Sort_SQL_Clause = e_Mail_Sort_SQL_Clause + temp_Person_Additional_E_Mail.get_Table_Name() + "." + temp_Person_Additional_E_Mail.getE_Mail().getField_Name() + " ASC ";
                
                // get or Initialize Display List Pagination Manager
                Display_List_Pagination_Manager e_Mail_Display_List_Pagination_Manager;
                try {
                    e_Mail_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("E_Mail", "e_Mail_Display_List_Pagination_Manager", new Person_Additional_E_Mail(), e_Mail_Where_SQL_Clause, 10);
                }
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error.", dbfe);
                    return;
                }
                
                
                HTML_Table_Tag e_Mail_List_Table = new HTML_Table_Tag("e_Mail_List_Table", out, "Records_List_Table");
                e_Mail_List_Table.setUse_Even_Odd_Classes(true);
                e_Mail_List_Table.do_Display_HTML_Table_Begin_Std();
                e_Mail_List_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                Person_Additional_E_Mail[] person_Additional_E_Mail_List;
                try {
                    person_Additional_E_Mail_List = (Person_Additional_E_Mail[]) web_Process.getDatabase_Front().getMany_Records(temp_Person_Additional_E_Mail, e_Mail_Where_SQL_Clause + " " + e_Mail_Sort_SQL_Clause, e_Mail_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), e_Mail_Display_List_Pagination_Manager.getItems_Per_Page());
                }
                
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error", dbfe);
                    return;
                }
                
                e_Mail_List_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(e_Mail_List_Table, search_Family.getFamily_Number());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(e_Mail_List_Table, temp_Person_Additional_E_Mail.getPerson_Additional_E_Mail_Number(),  e_Mail_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(e_Mail_List_Table, search_Person.getPerson_Number());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(e_Mail_List_Table, search_Person.getFirst_Name(),  e_Mail_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(e_Mail_List_Table, search_Person.getLast_Name(),  e_Mail_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(e_Mail_List_Table, temp_Person_Additional_E_Mail.getE_Mail());
                e_Mail_List_Table.do_Display_HTML_Row_End();
                
                if(person_Additional_E_Mail_List == null){
                    out.println("<tr><td> No Records Returned </td></tr>");
                    
                }else{
                    for(int row=0; row < person_Additional_E_Mail_List.length; row++){
                        Person_Additional_E_Mail person_Additional_E_Mail = person_Additional_E_Mail_List[row];
                        person=null;
                        try{
                            person = (Person)web_Process.getDatabase_Front().getRecord(new Person(person_Additional_E_Mail.getComp_Id().getValue(), person_Additional_E_Mail.getPerson_Number().getValue()));
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }catch(Database_Front_No_Results_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }
                        
                        Family[] familys=null;
                        try {
                            familys = (Family[]) web_Process.getDatabase_Front().getMany_Records(new Family(), " WHERE " +
                            person.getComp_Id().toWhere_Clause() + " AND " +
                            person.getPerson_Number().toWhere_Clause());
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }
                        if(familys==null)
                            continue;
                        
                        Family family = familys[0];
                        
                        
                        try {
                            e_Mail_List_Table.do_Display_HTML_Row_Begin_Std();
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(e_Mail_List_Table, family.getFamily_Number(), "Select", comp, this, "family_List_Records_Do_Action_Select");
                            
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(e_Mail_List_Table, person_Additional_E_Mail.getPerson_Additional_E_Mail_Number(), "E_Mail_Select", person, person_Additional_E_Mail.getClass(), "person_Additional_E_MailP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(e_Mail_List_Table, person_Additional_E_Mail.getPerson_Number(), "Select_Person", comp, person.getClass(), "personP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table(e_Mail_List_Table, person.getFirst_Name());
                            web_Process.do_Display_HTML_DF_Display_In_Table(e_Mail_List_Table, person.getLast_Name());
                            web_Process.do_Display_HTML_DF_Display_In_Table(e_Mail_List_Table, person_Additional_E_Mail.getE_Mail());
                            
                            e_Mail_List_Table.do_Display_HTML_Row_End();
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        
                    }
                }
                
                e_Mail_List_Table.do_Display_HTML_TBody_End();
                e_Mail_List_Table.do_Display_HTML_Table_End();
                try {
                    out.println(web_Process.display_List_Pagination_Manager_To_Html(e_Mail_Display_List_Pagination_Manager));
                }
                catch(WPException wpe){
                    web_Process.error_End("Failed to display pagination manager.", wpe);
                    return;
                }
            }
            
            
            
            
            
            
            
            
            
            
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            Family family = new Family();
            
            
            try {
                web_Process.do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, focus_Button_Form, "action", "Find", "Find", "", family.getFamily_Number(), 15, comp, this, "family_List_Records_Do_Action_Find");
                
                web_Process.do_Display_HTML_Form_Begin_Std(out, "Get_Family_Maint");
                web_Process.do_Display_HTML_In_Form_Hidden_Input("maint", "family");
                web_Process.do_Display_HTML_In_Form_Submit_Button("action", "Maint.", "", this, "family_List_Records_Do_Action_Maint");
                web_Process.do_Display_HTML_Form_End();
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Get_Family_end", "action", "End", "End", "", this, "std_Action_End");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Get_Family_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                
                
                
                
                
                
                web_Process.do_Display_HTML_Form_Begin_Std(out, "Get_Family_Search");
                String web_Root = web_Process.getSubscriber_Organization_Portal().getWeb_Root().toString();
                
                out.println("Search For:<br>");
                
                out.println(search_Person.getFirst_Name().getField_Title());
                out.println(search_Person.getFirst_Name().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                
                out.println(search_Person.getLast_Name().getField_Title());
                out.println(search_Person.getLast_Name().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                
                out.println(search_Person.getCity().getField_Title());
                out.println(search_Person.getCity().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                
                out.println(search_Person.getPhone().getField_Title());
                out.println(search_Person.getPhone().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                
                out.println(search_Person.getE_Mail().getField_Title());
                out.println(search_Person.getE_Mail().toHTML_Input("", web_Root, web_Process.getHtml_Form_Tag().getForm_Name(), 14, "", ""));
                
              
                web_Process.do_Display_HTML_In_Form_Submit_Button("action", "Search", "", this, "family_List_Records_Do_Action_Search");
                
                
                web_Process.do_Display_HTML_Form_End();
                
               
                
            }
            catch(WPFatal_Exception wpe){
                web_Process.error_End("Failed to make buttons.", wpe);
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
    
    
    
    public void family_List_Records_Do_Action_Find(Web_Process web_Process) {
        
        try {
            Family temp_Family =new Family();
            
            try{
                temp_Family.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Family.getFamily_Number().getField_Title()+".", dbre);
                return;
            }
            Family family;
            try{
                family=(Family)web_Process.getDatabase_Front().getRecord(temp_Family);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Family
                web_Process.error("No Such Family.", null);
                return;
            }
            // found a Family;
            
            Web_Process parent_Web_Process;
            try {
                parent_Web_Process = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), web_Process.getParent_Web_Process_Id());
            }
            catch(WPException wpe){
                web_Process.error_End("Unable to load parent web process.", wpe);
                return;
            }
            
            String return_As = (String) web_Process.getAttribute("return_As");
            parent_Web_Process.setAttribute(return_As, family);
            web_Process.end();
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void family_List_Records_Do_Action_Select(Web_Process web_Process) {
        try {
            Family temp_Family =new Family();
            
            try{
                temp_Family.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Family.getFamily_Number().getField_Title()+".", dbre);
                return;
            }
            Family family;
            try{
                family=(Family)web_Process.getDatabase_Front().getRecord(temp_Family);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Family
                web_Process.error("No Such Family.", dbfe);
                return;
            }
            // found a Family;
            
            Web_Process parent_Web_Process;
            try {
                parent_Web_Process = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), web_Process.getParent_Web_Process_Id());
            }
            catch(WPException wpe){
                web_Process.error_End("Unable to load parent web process.", wpe);
                return;
            }
            
            String return_As = (String) web_Process.getAttribute("return_As");
            parent_Web_Process.setAttribute(return_As, family);
            web_Process.end();
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void family_List_Records_Do_Action_Maint(Web_Process web_Process) {
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
    
    
    public void family_List_Records_Do_Action_Search(Web_Process web_Process) {
        
        Person search_Person = (Person)web_Process.getAttribute("search_Person");
        int ier;
        ier = search_Person.getLast_Name().validate_Request(web_Process.getMy_Request());
        if(ier!=0)search_Person.getLast_Name().setRequest_Error("");
        ier = search_Person.getFirst_Name().validate_Request(web_Process.getMy_Request());
        if(ier!=0)search_Person.getFirst_Name().setRequest_Error("");
        ier = search_Person.getCity().validate_Request(web_Process.getMy_Request());
        ier = search_Person.getPhone().validate_Request(web_Process.getMy_Request());
        ier = search_Person.getE_Mail().validate_Request(web_Process.getMy_Request());
        
        web_Process.setAttribute("search_Person", search_Person);
        family_List_Records_Do_Display(web_Process);
        return;
        
    
    }
    
    
    
    
    
    
    public void family_List_Records_Do_Action_Reload(Web_Process web_Process) {
        try {
            Family    family = new Family();
            
            
            // look for sorting commands
            Display_List_Sort_Manager family_Display_List_Sort_Manager;
            family_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("family_Display_List_Sort_Manager");
            if(family_Display_List_Sort_Manager != null){
                web_Process.do_Process_Of_Sort_Manger_Request(family_Display_List_Sort_Manager, family);
                web_Process.setAttribute("family_Display_List_Sort_Manager", family_Display_List_Sort_Manager);
            }
            
            
            
            // Look for paging commands
            Display_List_Pagination_Manager family_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("family_Display_List_Pagination_Manager");
            if(family_Display_List_Pagination_Manager!=null){
                family_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("family_Display_List_Pagination_Manager", family_Display_List_Pagination_Manager);
            }
            
            
            Person    person = new Person();
            
            
            // look for sorting commands
            Display_List_Sort_Manager person_Display_List_Sort_Manager;
            person_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("person_Display_List_Sort_Manager");
            if(person_Display_List_Sort_Manager!= null){
                web_Process.do_Process_Of_Sort_Manger_Request(person_Display_List_Sort_Manager, person);
                web_Process.setAttribute("person_Display_List_Sort_Manager", person_Display_List_Sort_Manager);
            }
            
            
            Organization    organization = new Organization();
            
            
            // look for sorting commands
            Display_List_Sort_Manager organization_Display_List_Sort_Manager;
            organization_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("organization_Display_List_Sort_Manager");
            if(organization_Display_List_Sort_Manager!= null){
                web_Process.do_Process_Of_Sort_Manger_Request(organization_Display_List_Sort_Manager, organization);
                web_Process.setAttribute("organization_Display_List_Sort_Manager", organization_Display_List_Sort_Manager);
            }
            
            
            
            
            family_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
        
    }
    
    
    
    
    
    
    public void family_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        
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
