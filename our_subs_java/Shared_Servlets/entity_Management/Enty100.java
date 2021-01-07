/*
 * Enty100.java
 *
 * Created on August 01, 2006, 10:31 AM
 */

package entity_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.*;
import com.amlibtech.login.data.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import customer_Management.data.*;
import enroll_Now_Office.data.*;
import entity_Management.datum.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.amlibtech.security_Management.*;
import enroll_Now_Admin.data.*;




/**
 *
 * @author  dgleeson
 * @version
 */
public class Enty100 extends WPHttpServlet_Base {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Person Maintenance";
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
            person_List_Records_Do_Display(web_Process);
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
            
            String person_Number = web_Process.getMy_Request().getParameter("loading_Person_Number");
            if(person_Number!=null){
                Person person;
                try {
                    person = (Person)web_Process.getDatabase_Front().getRecord(new Person(comp.getComp_Id().getValue(), new Integer(person_Number)));
                }catch(Database_Front_Exception dbfe){
                    web_Process.error_End(dbfe);
                    return;
                }catch(Database_Front_No_Results_Exception dbfe){
                    web_Process.error_End("No Such Person Found", dbfe);
                    return;
                }
                web_Process.setAttribute("person", person);
                
                
                String loading_Action = web_Process.getMy_Request().getParameter("loading_Action");
                if(loading_Action!=null){
                    if(loading_Action.equals("Add Address")){
                        try {
                            web_Process.launch_Task("Enty101", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
                            "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()) +
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
                            web_Process.launch_Task("Enty103", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
                            "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString())+
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
                            web_Process.launch_Task("Enty102", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
                            "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString())+
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
                    String person_Additional_Address_Number = web_Process.getMy_Request().getParameter("loading_Person_Additional_Address_Number");
                    if(person_Additional_Address_Number!=null){
                        try {
                            web_Process.launch_Task("Enty101", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
                            "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()) +
                            "&loading_Person_Additional_Address_Number"+StringPlus.http_Encode(person_Additional_Address_Number)
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
                                web_Process.launch_Task("Enty102", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
                                "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()) +
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
                            String person_Additional_E_Mail_Number = web_Process.getMy_Request().getParameter("loading_Person_Additional_E_Mail_Number");
                            if(person_Additional_E_Mail_Number!=null){
                                try {
                                    web_Process.launch_Task("Enty102", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
                                    "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()) +
                                    "&loading_Person_Additional_E_Mail_Number"+StringPlus.http_Encode(person_Additional_E_Mail_Number)
                                    );
                                }catch(ServletException se){
                                    web_Process.error_End(se);
                                    return;
                                }catch(IOException ioe){
                                    web_Process.error_End(ioe);
                                    return;
                                }
                                
                            }else{
                                person_View_Record_Do_Display(web_Process, person);
                            }
                        }
                    }
                }
                return;
            }else{
                String loading_Action = web_Process.getMy_Request().getParameter("loading_Action");
                if(loading_Action != null){
                    if(loading_Action.equals("Create_For_A_Family")){
                        String loading_Family_Number = web_Process.getMy_Request().getParameter("loading_Family_Number");
                        if(loading_Family_Number == null){
                            web_Process.error_End("For me to Create a Person for a Family you must also provide the Family #.", null);
                        }else{
                            
                            Family family = new Family(comp.getComp_Id().getValue(), new Integer(loading_Family_Number));
                            try{
                                family=(Family)web_Process.getDatabase_Front().getRecord(family);
                            }
                            catch(Database_Front_Exception dbfe){
                                web_Process.error_End("Database Failure.", dbfe);
                                return;
                            }
                            catch(Database_Front_No_Results_Exception dbfe){
                                // no such cust
                                web_Process.error("No Such Family # :"+ loading_Family_Number + ".", null);
                                return;
                            }
                            
                            // found a cust
                            web_Process.setAttribute("family", family);
                            new_Record_For_Family(web_Process, family);
                        }
                        
                    }else{
                        web_Process.error_End("Unknown loading_Action :"+loading_Action+": for Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
                    }
                    
                }else{
                    person_List_Records_Do_Display(web_Process);
                }
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    void new_Record_For_Family(Web_Process web_Process, Family family) {
        
        try {
            Comp comp =(Comp) web_Process.getAttribute("comp");
            
            Person family_Person = family.get_Aka_Person(web_Process.getDatabase_Front());
            
            if(family_Person.getObsolete().getValue().booleanValue()){
                web_Process.error("Can not create a Student for a currently Obsolete Family.", null);
                return;
            }
            
            
            
            Person temp_Person =new Person();
            try {
                temp_Person.setComp_Id(comp.getComp_Id().getValue());
                temp_Person.setMember_of_Family_Number(family.getFamily_Number().getValue());
            }catch(DFException dfe){
                web_Process.error_End(dfe);
                return;
            }
            
            web_Process.setAttribute("create_New_Person", new Boolean(true));
            person_New_Record_Do_Display(web_Process, temp_Person, null);
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    
    
    
    public void person_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        
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
    
    
    
    void person_List_Records_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("Enty100_Person_List_Records", this, "action_Stack_Do_Action",  "person_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        try {
            
            String form_Name="Enty100_List";
            String focus_Button_Form="Enty100_find";
            
            Comp comp = (Comp)web_Process.getAttribute("comp");
            if(comp== null){
                web_Process.error_End("Can't find comp attribute.", null);
                return;
            }
            
            
            
            String  person_Where_SQL_Clause ="WHERE " +
            comp.getComp_Id().toWhere_Clause();
            
            Person search_Person;
            search_Person = (Person)web_Process.getAttribute("search_Person");
            if(search_Person==null){
                search_Person = new Person();
            }
            web_Process.setAttribute("search_Person", search_Person);
            
            if(!search_Person.getLast_Name().getValue().equals("")  &&  search_Person.getLast_Name().getRequest_Error().equals("")){
                person_Where_SQL_Clause = person_Where_SQL_Clause + " AND " +
                search_Person.getLast_Name().toWhere_Range_Clause();
                
            }
            
            if(!search_Person.getFirst_Name().getValue().equals("")  &&  search_Person.getFirst_Name().getRequest_Error().equals("")){
                person_Where_SQL_Clause = person_Where_SQL_Clause + " AND " +
                search_Person.getFirst_Name().toWhere_Range_Clause();
            }
            
            if(!search_Person.getCity().getValue().equals("")  &&  search_Person.getCity().getRequest_Error().equals("")){
                person_Where_SQL_Clause = person_Where_SQL_Clause + " AND " +
                search_Person.getCity().toWhere_Range_Clause();
            }
            
            if(!search_Person.getPhone().getValue().equals("")  &&  search_Person.getPhone().getRequest_Error().equals("")){
                person_Where_SQL_Clause = person_Where_SQL_Clause + " AND " +
                search_Person.getPhone().toWhere_Clause();
            }
            if(!search_Person.getE_Mail().getValue().equals("")  &&  search_Person.getE_Mail().getRequest_Error().equals("")){
                person_Where_SQL_Clause = person_Where_SQL_Clause + " AND " +
                search_Person.getE_Mail().toWhere_Clause();
            }
            
            
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager person_Display_List_Sort_Manager;
            
            person_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("person_Display_List_Sort_Manager");
            if(person_Display_List_Sort_Manager==null){
                person_Display_List_Sort_Manager = new Display_List_Sort_Manager("People.");
            }
            
            web_Process.setAttribute("person_Display_List_Sort_Manager", person_Display_List_Sort_Manager);
            String person_Sort_SQL_Clause = person_Display_List_Sort_Manager.getSort_SQL_Clause();
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager person_Display_List_Pagination_Manager;
            try {
                person_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("People", "person_Display_List_Pagination_Manager", new Person(), person_Where_SQL_Clause, 10);
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
            
            web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+focus_Button_Form+"."+ new Person().getPerson_Number().getField_Name()+".focus();");
            
            
            out.println("<div id='above_Header'>");
            out.println("</div>");
            
            out.println("<div id='header'>");
            out.println("<div id='header_Text'>");
            out.println(web_Process.getProgram().getProgram_Title().toHTML());
            out.println("</div>");
            out.println("</div>");
            
            
            
            
            
            out.println("<div id='main'>");
            
            
            
            
            person_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag person_List_Table = new HTML_Table_Tag("person_List_Table", out, "Records_List_Table");
            person_List_Table.setUse_Even_Odd_Classes(true);
            person_List_Table.do_Display_HTML_Table_Begin_Std();
            person_List_Table.do_Display_HTML_TBody_Begin_Std();
            
            Person temp_Person;
            temp_Person = new Person();
            
            Person[] person_List;
            try {
                person_List = (Person[]) web_Process.getDatabase_Front().getMany_Records(temp_Person, person_Where_SQL_Clause + " " + person_Sort_SQL_Clause, person_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), person_Display_List_Pagination_Manager.getItems_Per_Page());
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            person_List_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(person_List_Table, temp_Person.getPerson_Number(),  person_Display_List_Sort_Manager);
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(person_List_Table, temp_Person.getFirst_Name(),  person_Display_List_Sort_Manager);
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(person_List_Table, temp_Person.getLast_Name(),  person_Display_List_Sort_Manager);
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(person_List_Table, temp_Person.getStreet_Address(),  person_Display_List_Sort_Manager);
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(person_List_Table, temp_Person.getCity(),  person_Display_List_Sort_Manager);
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(person_List_Table, temp_Person.getPhone(),  person_Display_List_Sort_Manager);
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(person_List_Table, temp_Person.getE_Mail(),  person_Display_List_Sort_Manager);
            person_List_Table.do_Display_HTML_Row_End();
            
            if(person_List == null){
                out.println("<tr><td> No Records Returned </td></tr>");
                
            }else{
                for(int row=0; row < person_List.length; row++){
                    Person person = person_List[row];
                    
                    
                    person_List_Table.do_Display_HTML_Row_Begin_Std();
                    try {
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(person_List_Table, person.getPerson_Number(), "Select", comp, this, "person_List_Records_Do_Action_Select");
                    }catch(WPFatal_Exception wpfe){
                        web_Process.error_End(wpfe);
                        return;
                    }
                    web_Process.do_Display_HTML_DF_Display_In_Table(person_List_Table, person.getFirst_Name());
                    web_Process.do_Display_HTML_DF_Display_In_Table(person_List_Table, person.getLast_Name());
                    web_Process.do_Display_HTML_DF_Display_In_Table(person_List_Table, person.getStreet_Address());
                    web_Process.do_Display_HTML_DF_Display_In_Table(person_List_Table, person.getCity());
                    web_Process.do_Display_HTML_DF_Display_In_Table(person_List_Table, person.getPhone());
                    web_Process.do_Display_HTML_DF_Display_In_Table(person_List_Table, person.getE_Mail());
                    
                    
                    
                    person_List_Table.do_Display_HTML_Row_End();
                    
                    //                    person.use_Family_Update(web_Process.getDatabase_Front());
                    //                    try {
                    //                        web_Process.getDatabase_Front().updateRecord(person);
                    //                    }catch(Database_Front_Exception dbfe){
                    //                        web_Process.error_End(dbfe);
                    //                        return;
                    //                    }
                    
                }
            }
            
            
            person_List_Table.do_Display_HTML_TBody_End();
            person_List_Table.do_Display_HTML_Table_End();
            
            
            try {
                out.println(web_Process.display_List_Pagination_Manager_To_Html(person_Display_List_Pagination_Manager));
            }
            catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            
            
            
            
            
            
            
            if(!search_Person.getCity().getValue().equals("")  &&  search_Person.getCity().getRequest_Error().equals("")){
                
                Person_Additional_Address temp_Person_Additional_Address;
                temp_Person_Additional_Address = new Person_Additional_Address();
                
                
                String address_Where_SQL_Clause = "JOIN "+ temp_Person.get_Table_Name() + " USING("+
                temp_Person.getComp_Id().getField_Name()+", "+ temp_Person.getPerson_Number().getField_Name()+") " +
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
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(address_List_Table, temp_Person_Additional_Address.getPerson_Additional_Address_Number(),  address_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person.getPerson_Number());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(address_List_Table, temp_Person.getFirst_Name(),  address_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(address_List_Table, temp_Person.getLast_Name(),  address_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person_Additional_Address.getStreet_Address());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person_Additional_Address.getCity());
                address_List_Table.do_Display_HTML_Row_End();
                
                if(person_Additional_Address_List == null){
                    out.println("<tr><td> No Records Returned </td></tr>");
                    
                }else{
                    for(int row=0; row < person_Additional_Address_List.length; row++){
                        Person_Additional_Address person_Additional_Address = person_Additional_Address_List[row];
                        Person person=null;
                        try{
                            person = (Person)web_Process.getDatabase_Front().getRecord(new Person(person_Additional_Address.getComp_Id().getValue(), person_Additional_Address.getPerson_Number().getValue()));
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }catch(Database_Front_No_Results_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }
                        
                        address_List_Table.do_Display_HTML_Row_Begin_Std();
                        try {
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(address_List_Table, person_Additional_Address.getPerson_Additional_Address_Number(), "Address_Select", person, person_Additional_Address.getClass(), "person_Additional_AddressP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(address_List_Table, person_Additional_Address.getPerson_Number(), "Select", comp, this, "person_List_Records_Do_Action_Select");
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person.getFirst_Name());
                        web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person.getLast_Name());
                        web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person_Additional_Address.getStreet_Address());
                        web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person_Additional_Address.getCity());
                        
                        
                        
                        address_List_Table.do_Display_HTML_Row_End();
                        
                        
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
                
                
                String phone_Where_SQL_Clause = "JOIN "+ temp_Person.get_Table_Name() + " USING("+
                temp_Person.getComp_Id().getField_Name()+", "+ temp_Person.getPerson_Number().getField_Name()+") " +
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
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(phone_List_Table, temp_Person_Additional_Phone.getPhone(),  phone_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(phone_List_Table, temp_Person_Additional_Phone.getPhone_Type());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(phone_List_Table, temp_Person.getPerson_Number());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(phone_List_Table, temp_Person.getFirst_Name(),  phone_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(phone_List_Table, temp_Person.getLast_Name(),  phone_Display_List_Sort_Manager);
                phone_List_Table.do_Display_HTML_Row_End();
                
                if(person_Additional_Phone_List == null){
                    out.println("<tr><td> No Records Returned </td></tr>");
                    
                }else{
                    for(int row=0; row < person_Additional_Phone_List.length; row++){
                        Person_Additional_Phone person_Additional_Phone = person_Additional_Phone_List[row];
                        Person person=null;
                        try{
                            person = (Person)web_Process.getDatabase_Front().getRecord(new Person(person_Additional_Phone.getComp_Id().getValue(), person_Additional_Phone.getPerson_Number().getValue()));
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }catch(Database_Front_No_Results_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }
                        
                        phone_List_Table.do_Display_HTML_Row_Begin_Std();
                        try {
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(phone_List_Table, person_Additional_Phone.getPhone(), "Phone_Select", person, person_Additional_Phone.getClass(), "person_Additional_PhoneP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table(phone_List_Table, person_Additional_Phone.getPhone_Type());
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(phone_List_Table, person_Additional_Phone.getPerson_Number(), "Select", comp, this, "person_List_Records_Do_Action_Select");
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        web_Process.do_Display_HTML_DF_Display_In_Table(phone_List_Table, person.getFirst_Name());
                        web_Process.do_Display_HTML_DF_Display_In_Table(phone_List_Table, person.getLast_Name());
                        
                        phone_List_Table.do_Display_HTML_Row_End();
                        
                        
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
                
                Person_Additional_E_Mail temp_Person_Additional_E_Mail;
                temp_Person_Additional_E_Mail = new Person_Additional_E_Mail();
                
                
                String e_Mail_Where_SQL_Clause = "JOIN "+ temp_Person.get_Table_Name() + " USING("+
                temp_Person.getComp_Id().getField_Name()+", "+ temp_Person.getPerson_Number().getField_Name()+") " +
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
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(e_Mail_List_Table, temp_Person_Additional_E_Mail.getPerson_Additional_E_Mail_Number(),  e_Mail_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(e_Mail_List_Table, temp_Person.getPerson_Number());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(e_Mail_List_Table, temp_Person.getFirst_Name(),  e_Mail_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(e_Mail_List_Table, temp_Person.getLast_Name(),  e_Mail_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(e_Mail_List_Table, temp_Person_Additional_E_Mail.getE_Mail());
                e_Mail_List_Table.do_Display_HTML_Row_End();
                
                if(person_Additional_E_Mail_List == null){
                    out.println("<tr><td> No Records Returned </td></tr>");
                    
                }else{
                    for(int row=0; row < person_Additional_E_Mail_List.length; row++){
                        Person_Additional_E_Mail person_Additional_E_Mail = person_Additional_E_Mail_List[row];
                        Person person=null;
                        try{
                            person = (Person)web_Process.getDatabase_Front().getRecord(new Person(person_Additional_E_Mail.getComp_Id().getValue(), person_Additional_E_Mail.getPerson_Number().getValue()));
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }catch(Database_Front_No_Results_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return;
                        }
                        
                        e_Mail_List_Table.do_Display_HTML_Row_Begin_Std();
                        try {
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(e_Mail_List_Table, person_Additional_E_Mail.getPerson_Additional_E_Mail_Number(), "E_Mail_Select", person, person_Additional_E_Mail.getClass(), "person_Additional_E_MailP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(e_Mail_List_Table, person_Additional_E_Mail.getPerson_Number(), "Select", comp, this, "person_List_Records_Do_Action_Select");
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        
                        web_Process.do_Display_HTML_DF_Display_In_Table(e_Mail_List_Table, person.getFirst_Name());
                        web_Process.do_Display_HTML_DF_Display_In_Table(e_Mail_List_Table, person.getLast_Name());
                        web_Process.do_Display_HTML_DF_Display_In_Table(e_Mail_List_Table, person_Additional_E_Mail.getE_Mail());
                        
                        
                        
                        e_Mail_List_Table.do_Display_HTML_Row_End();
                        
                        
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
            
            Person person;
            person = new Person();
            
            
            try {
                web_Process.do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, focus_Button_Form, "action", "Go", "Go", "", person.getPerson_Number(), 15, comp, this, "person_List_Records_Do_Action_Go");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form_With_Hidden_DBR(out, "Enty100_new", "action", "New", "New", "", comp, this, "person_List_Records_Do_Action_New");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty100_end", "action", "End", "End", "", this, "std_Action_End");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty100_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                
                
                
                
                web_Process.do_Display_HTML_Form_Begin_Std(out, "Enty100_Search");
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
                
                
                web_Process.do_Display_HTML_In_Form_Submit_Button("action", "Search", "", this, "person_List_Records_Do_Action_Search");
                
                
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
            
            //        web_Process.setAttribute("person", person);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void person_List_Records_Do_Action_Go(Web_Process web_Process) {
        
        try {
            Person temp_Person;
            temp_Person=new Person();
            
            try{
                temp_Person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Person.getPerson_Number().getField_Title()+".", dbre);
                return;
            }
            Person person;
            try{
                person=(Person)web_Process.getDatabase_Front().getRecord(temp_Person);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Person
                //person_New_Record_Do_Display(web_Process, temp_Person, null);
                web_Process.error("No Such "+ temp_Person.getEntity_Name()+".",null);
                return;
            }
            // found a Person;
            person_View_Record_Do_Display(web_Process, person);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void person_List_Records_Do_Action_Select(Web_Process web_Process) {
        
        try {
            Person temp_Person;
            temp_Person=new Person();
            
            try{
                temp_Person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Person.getPerson_Number().getField_Title()+".", dbre);
                return;
            }
            Person person;
            try{
                person=(Person)web_Process.getDatabase_Front().getRecord(temp_Person);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Person
                web_Process.error("No Such "+ temp_Person.getEntity_Name()+".",null);
                return;
            }
            // found a Person;
            person_View_Record_Do_Display(web_Process, person);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    
    public void person_List_Records_Do_Action_New(Web_Process web_Process) {
        
        try {
            Person temp_Person;
            temp_Person=new Person();
            
            try{
                temp_Person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Person.getPerson_Number().getField_Title()+".", dbre);
                return;
            }
            
            person_New_Record_Do_Display(web_Process, temp_Person, null);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void person_List_Records_Do_Action_Search(Web_Process web_Process) {
        
        Person search_Person = (Person)web_Process.getAttribute("search_Person");
        
        
        int ier;
        ier = search_Person.getLast_Name().validate_Request(web_Process.getMy_Request());
        if(ier!=0)search_Person.getLast_Name().setRequest_Error(" ");
        ier = search_Person.getFirst_Name().validate_Request(web_Process.getMy_Request());
        if(ier!=0)search_Person.getFirst_Name().setRequest_Error(" ");
        ier = search_Person.getCity().validate_Request(web_Process.getMy_Request());
        ier = search_Person.getPhone().validate_Request(web_Process.getMy_Request());
        ier = search_Person.getE_Mail().validate_Request(web_Process.getMy_Request());
        
        web_Process.setAttribute("search_Person", search_Person);
        person_List_Records_Do_Display(web_Process);
        return;
    }
    
    
    
    
    
    public void person_List_Records_Do_Action_Reload(Web_Process web_Process) {
        
        try {
            Person    person;
            person = new Person();
            
            
            // look for sorting commands
            Display_List_Sort_Manager person_Display_List_Sort_Manager;
            
            person_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("person_Display_List_Sort_Manager");
            if(person_Display_List_Sort_Manager!=null){
                web_Process.do_Process_Of_Sort_Manger_Request(person_Display_List_Sort_Manager, person);
                web_Process.setAttribute("person_Display_List_Sort_Manager", person_Display_List_Sort_Manager);
            }
            
            // Look for paging commands
            Display_List_Pagination_Manager person_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("person_Display_List_Pagination_Manager");
            
            if(person_Display_List_Pagination_Manager!=null){
                person_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("person_Display_List_Pagination_Manager", person_Display_List_Pagination_Manager);
            }
            
            
            Person_Additional_Address    person_Additional_Address;
            person_Additional_Address = new Person_Additional_Address();
            
            
            
            
            // look for sorting commands
            Display_List_Sort_Manager address_Display_List_Sort_Manager;
            
            address_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("address_Display_List_Sort_Manager");
            if(address_Display_List_Sort_Manager!=null){
                web_Process.do_Process_Of_Sort_Manger_Request(address_Display_List_Sort_Manager, person_Additional_Address);
                web_Process.setAttribute("address_Display_List_Sort_Manager", address_Display_List_Sort_Manager);
            }
            
            // Look for paging commands
            Display_List_Pagination_Manager address_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("address_Display_List_Pagination_Manager");
            
            if(address_Display_List_Pagination_Manager!=null){
                address_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("address_Display_List_Pagination_Manager", address_Display_List_Pagination_Manager);
            }
            
            
            person_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    void person_New_Record_Do_Display(Web_Process web_Process, Person person, Exception ex) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty100_Person_New_Record", this, "action_Stack_Do_Action",  "person_New_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty100_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        
        try {
            Family_AutoCompletion.getJavaScript(out, web_Process, form_Name, "member_Of_Family_Number", "member_Of_Family_name", "comp_Id", this, "person_Edit_And_New_Shared_Do_Action_Maint");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("Programming Error", wpfe);
            return;
        }
        
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out,
        "onload=\"javascript:" +
        " member_Of_Family_Number_Init(); " +
        web_Process.DF_Focus_Call_For_On_Event(form_Name, person.getFirst_Name()) + "; " +
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
        }
        catch(WPException wpe){
            web_Process.error_End("Failed to make form.", wpe);
            return;
        }
        
        
        out.println("<div id='main'>");
        
        person_Edit_And_New_Shared_Superior_Display(out, web_Process, true);
        
        person_Edit_And_New_Shared_Main_Display(out, web_Process, person, ex);
        
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "person_New_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "person_New_Record_Do_Action_Cancel");
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
        }
        catch(WPException wpe){
            web_Process.error_End("Failed to end form.", wpe);
            return;
        }
        
        //web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty100_end",  "action", "End Task", "End Task", "");
        
        out.println("</div>");
        
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("person", person);
        
        out.close();
        
    }
    
    
    public void person_New_Record_Do_Action_Create(Web_Process web_Process) {
        try {
            Person person = (Person) web_Process.getAttribute("person");
            
            try {
                person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                person.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("person", person);
                person_New_Record_Do_Display(web_Process, person, dbre);
                return;
            }
            
            Comp comp = (Comp) web_Process.getAttribute("comp");
            
            
            try {
                person.setPerson_Number(Entity_Control.get_Next_Person_Number(web_Process.getDatabase_Front(), comp.getComp_Id()));
            }catch(DFException dfe){
                web_Process.error_End("can't set Person Number", dfe);
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
                int row_Count = web_Process.getDatabase_Front().addRecord(person);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + person.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            web_Process.popStatus_Node();
            person_View_Record_Do_Display(web_Process, person);
            //do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void person_New_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        web_Process.removeAttribute("person");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void person_New_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Person person = (Person) web_Process.getAttribute("person");
        person_New_Record_Do_Display(web_Process, person, null);
        return;
    }
    
    
    
    
    void person_View_Record_Do_Display(Web_Process web_Process, Person person) {
        
        try {
            web_Process.pushStatus_Wreload("Enty100_Person_View_Record", this, "action_Stack_Do_Action",  "person_View_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty100_View";
        
        try {
            person = (Person)web_Process.getDatabase_Front().getRecord(person);
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End(dbfe);
            return;
        }catch(Database_Front_No_Results_Exception dbfe){
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
        person.use_Family_Update(web_Process.getDatabase_Front());
        
        
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
        
        person_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
        
        person_View_Record_Do_Display_Fields(out, web_Process, person);
        
        person_View_Record_Do_Display_Additional_Records(out, web_Process, person);
        
        person_View_Record_Do_Display_Identity_Records(out, web_Process, person);
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "person_View_Record_Do_Action_Edit");
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
        web_Process.setAttribute("person", person);
        
        if(!Person.personA_Output_View_Record_Buttons(web_Process, button_Table))
            return;
        
        
        
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
        
        web_Process.setAttribute("person", person);
        
        out.close();
    }
    
    
    public void person_View_Record_Do_Display_Fields(PrintWriter out, Web_Process web_Process, Person person) {
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        
        
        {
            
            
            HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_View_Table");
            
            record_Table.do_Display_HTML_Table_Begin_Std();
            record_Table.do_Display_HTML_TBody_Begin_Std();
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getPerson_Number());
            
            web_Process.do_Display_HTML_DF_DF_Display_With_Th_Tr(record_Table, person.getFirst_Name(), person.getLast_Name());
            
            Family family = Family.get_Possible_Family(web_Process.getDatabase_Front(), person.getComp_Id().getValue(), person.getMember_of_Family_Number().getValue());
            
            record_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_Table, person.getMember_of_Family_Number());
            try {
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(record_Table, person.getMember_of_Family_Number(), "Select_Member_Of_Family", family, family.getClass(), "familyP_Select_Action");
            }catch(WPFatal_Exception wpfe){
                web_Process.error_End(wpfe);
                return;
            }
            
            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_Table,  family.getName_First_Then_Last(web_Process.getDatabase_Front()));
            web_Process.do_Display_HTML_DF_Display_In_Table(record_Table,  family.getName_First_Then_Last(web_Process.getDatabase_Front()));
            record_Table.do_Display_HTML_Row_End();
            
            record_Table.do_Display_HTML_Row_Begin_Std();
            record_Table.do_Display_HTML_TH_Element_Col_Span("Address",6);
            record_Table.do_Display_HTML_Row_End();
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getAddress_Type());
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getStreet_Address());
            web_Process.do_Display_HTML_DF_DF_DF_Display_With_Th_Tr(record_Table, person.getCity(), person.getState_Abbr(), person.getZip_Code());
            
            record_Table.do_Display_HTML_Row_Begin_Std();
            record_Table.do_Display_HTML_TH_Element(" ");
            record_Table.do_Display_HTML_Row_End();
            
            web_Process.do_Display_HTML_DF_DF_Display_With_Th_Tr(record_Table, person.getPhone_Type(), person.getPhone());
            
            record_Table.do_Display_HTML_Row_Begin_Std();
            record_Table.do_Display_HTML_TH_Element(" ");
            record_Table.do_Display_HTML_Row_End();
            
            web_Process.do_Display_HTML_DF_DF_Display_With_Th_Tr(record_Table, person.getE_Mail_Type(), person.getE_Mail());
            
            
            try {
                web_Process.do_Display_HTML_DF_DF_DF_Display_With_Th_Tr(record_Table, person.getDate_Of_Birth_Status(), person.getDate_Of_Birth(), person.getAge_Text());
            }catch(DFException dfe){
                web_Process.error_End(dfe);
                return;
            }
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getGender());
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getObsolete());
            
            
            
            
            
            record_Table.do_Display_HTML_TBody_End();
            record_Table.do_Display_HTML_Table_End();
            
        }
        
        
        
        
        
    }
    
    
    public void person_View_Record_Do_Display_Additional_Records(PrintWriter out, Web_Process web_Process, Person person) {
        try {
            {
                Person_Additional_Address temp_Person_Additional_Address;
                temp_Person_Additional_Address = new Person_Additional_Address();
                
                
                String address_Where_SQL_Clause = " WHERE " +
                person.getComp_Id().toWhere_Clause() + " AND " +
                person.getPerson_Number().toWhere_Clause();
                
                
                Person_Additional_Address[] person_Additional_Address_List;
                try {
                    person_Additional_Address_List = (Person_Additional_Address[]) web_Process.getDatabase_Front().getMany_Records(temp_Person_Additional_Address, address_Where_SQL_Clause);
                }
                
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error", dbfe);
                    return;
                }
                
                if(person_Additional_Address_List != null){
                    
                    //out.println("<p>");
                    
                    HTML_Table_Tag address_List_Table = new HTML_Table_Tag("address_List_Table", out, "Records_List_Table");
                    address_List_Table.setUse_Even_Odd_Classes(true);
                    address_List_Table.do_Display_HTML_Table_Begin_Std();
                    address_List_Table.do_Display_HTML_TBody_Begin_Std();
                    
                    
                    address_List_Table.do_Display_HTML_Row_Begin_Std();
                    address_List_Table.do_Display_HTML_TH_Element_Col_Span("Additional Addresses", 6);
                    address_List_Table.do_Display_HTML_Row_End();
                    
                    address_List_Table.do_Display_HTML_Row_Begin_Std();
                    address_List_Table.do_Display_HTML_TH_Element("#");
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person_Additional_Address.getAddress_Type());
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person_Additional_Address.getStreet_Address());
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person_Additional_Address.getCity());
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person_Additional_Address.getState_Abbr());
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(address_List_Table, temp_Person_Additional_Address.getZip_Code());
                    address_List_Table.do_Display_HTML_Row_End();
                    
                    
                    for(int row=0; row < person_Additional_Address_List.length; row++){
                        Person_Additional_Address person_Additional_Address = person_Additional_Address_List[row];
                        
                        
                        address_List_Table.do_Display_HTML_Row_Begin_Std();
                        try {
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(address_List_Table, person_Additional_Address.getPerson_Additional_Address_Number(), "Address_Select", person, person_Additional_Address.getClass(), "person_Additional_AddressP_Select_Action");
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person_Additional_Address.getAddress_Type());
                        web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person_Additional_Address.getStreet_Address());
                        web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person_Additional_Address.getCity());
                        web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person_Additional_Address.getState_Abbr());
                        web_Process.do_Display_HTML_DF_Display_In_Table(address_List_Table, person_Additional_Address.getZip_Code());
                        
                        
                        
                        address_List_Table.do_Display_HTML_Row_End();
                        
                        
                    }
                    
                    address_List_Table.do_Display_HTML_TBody_End();
                    address_List_Table.do_Display_HTML_Table_End();
                }
                
            }
            
            
            
            {
                Person_Additional_Phone temp_Person_Additional_Phone;
                temp_Person_Additional_Phone = new Person_Additional_Phone();
                
                
                String phone_Where_SQL_Clause = " WHERE " +
                person.getComp_Id().toWhere_Clause() + " AND " +
                person.getPerson_Number().toWhere_Clause();
                
                
                Person_Additional_Phone[] person_Additional_Phone_List;
                try {
                    person_Additional_Phone_List = (Person_Additional_Phone[]) web_Process.getDatabase_Front().getMany_Records(temp_Person_Additional_Phone, phone_Where_SQL_Clause);
                }
                
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error", dbfe);
                    return;
                }
                
                if(person_Additional_Phone_List != null){
                    
                    out.println("<p>");
                    
                    HTML_Table_Tag phone_List_Table = new HTML_Table_Tag("phone_List_Table", out, "Records_List_Table");
                    phone_List_Table.setUse_Even_Odd_Classes(true);
                    phone_List_Table.do_Display_HTML_Table_Begin_Std();
                    phone_List_Table.do_Display_HTML_TBody_Begin_Std();
                    
                    
                    phone_List_Table.do_Display_HTML_Row_Begin_Std();
                    phone_List_Table.do_Display_HTML_TH_Element_Col_Span("Additional Phone Numbers", 2);
                    phone_List_Table.do_Display_HTML_Row_End();
                    
                    phone_List_Table.do_Display_HTML_Row_Begin_Std();
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(phone_List_Table, temp_Person_Additional_Phone.getPhone());
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(phone_List_Table, temp_Person_Additional_Phone.getPhone_Type());
                    phone_List_Table.do_Display_HTML_Row_End();
                    
                    
                    for(int row=0; row < person_Additional_Phone_List.length; row++){
                        Person_Additional_Phone person_Additional_Phone = person_Additional_Phone_List[row];
                        
                        
                        phone_List_Table.do_Display_HTML_Row_Begin_Std();
                        try {
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(phone_List_Table, person_Additional_Phone.getPhone(), "Phone_Select", person, person_Additional_Phone.getClass(), "person_Additional_PhoneP_Select_Action");
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        web_Process.do_Display_HTML_DF_Display_In_Table(phone_List_Table, person_Additional_Phone.getPhone_Type());
                        
                        phone_List_Table.do_Display_HTML_Row_End();
                        
                        
                    }
                    
                    phone_List_Table.do_Display_HTML_TBody_End();
                    phone_List_Table.do_Display_HTML_Table_End();
                    
                }
                
            }
            
            
            
            
            {
                Person_Additional_E_Mail temp_Person_Additional_E_Mail;
                temp_Person_Additional_E_Mail = new Person_Additional_E_Mail();
                
                
                String e_Mail_Where_SQL_Clause = " WHERE " +
                person.getComp_Id().toWhere_Clause() + " AND " +
                person.getPerson_Number().toWhere_Clause();
                
                
                
                
                Person_Additional_E_Mail[] person_Additional_E_Mail_List;
                try {
                    person_Additional_E_Mail_List = (Person_Additional_E_Mail[]) web_Process.getDatabase_Front().getMany_Records(temp_Person_Additional_E_Mail, e_Mail_Where_SQL_Clause);
                }
                
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error", dbfe);
                    return;
                }
                
                if(person_Additional_E_Mail_List != null){
                    
                    out.println("<p>");
                    
                    HTML_Table_Tag e_Mail_List_Table = new HTML_Table_Tag("e_Mail_List_Table", out, "Records_List_Table");
                    e_Mail_List_Table.setUse_Even_Odd_Classes(true);
                    e_Mail_List_Table.do_Display_HTML_Table_Begin_Std();
                    e_Mail_List_Table.do_Display_HTML_TBody_Begin_Std();
                    
                    e_Mail_List_Table.do_Display_HTML_Row_Begin_Std();
                    e_Mail_List_Table.do_Display_HTML_TH_Element_Col_Span("Additional E-Mail Addresses", 3);
                    e_Mail_List_Table.do_Display_HTML_Row_End();
                    
                    e_Mail_List_Table.do_Display_HTML_Row_Begin_Std();
                    e_Mail_List_Table.do_Display_HTML_TH_Element("#");
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(e_Mail_List_Table, temp_Person_Additional_E_Mail.getE_Mail_Type());
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(e_Mail_List_Table, temp_Person_Additional_E_Mail.getE_Mail());
                    e_Mail_List_Table.do_Display_HTML_Row_End();
                    
                    
                    for(int row=0; row < person_Additional_E_Mail_List.length; row++){
                        Person_Additional_E_Mail person_Additional_E_Mail = person_Additional_E_Mail_List[row];
                        
                        
                        e_Mail_List_Table.do_Display_HTML_Row_Begin_Std();
                        try {
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(e_Mail_List_Table, person_Additional_E_Mail.getPerson_Additional_E_Mail_Number(), "E_Mail_Select", person, person_Additional_E_Mail.getClass(), "person_Additional_E_MailP_Select_Action");
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        web_Process.do_Display_HTML_DF_Display_In_Table(e_Mail_List_Table, person_Additional_E_Mail.getE_Mail_Type());
                        web_Process.do_Display_HTML_DF_Display_In_Table(e_Mail_List_Table, person_Additional_E_Mail.getE_Mail());
                        
                        e_Mail_List_Table.do_Display_HTML_Row_End();
                        
                        
                    }
                    
                    e_Mail_List_Table.do_Display_HTML_TBody_End();
                    e_Mail_List_Table.do_Display_HTML_Table_End();
                    
                }
                
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void person_View_Record_Do_Display_Identity_Records(PrintWriter out, Web_Process web_Process, Person person) {
        try {
            Comp comp = (Comp)web_Process.getAttribute("comp");
            
            
                
                out.println("<div style='position: absolute; top: 50px;left: 575px; right: 0px' id='identity_Table' >");
                
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
                            cust = person.get_Aka_Cust(web_Process.getDatabase_Front());
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
                            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Become a Customer", "", identity_Table, person.getClass(), "personA_Become_A_Customer_Action");
                        }
                        identity_Table.do_Display_HTML_Row_End();
                        
                    }
                    
                    
                    {
                        Family    family;
                        try {
                            family = person.get_Aka_Family(web_Process.getDatabase_Front());
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        
                        identity_Table.do_Display_HTML_Row_Begin_Std();
                        if(family != null){
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, family.getFamily_Number());
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(identity_Table, family.getFamily_Number(), "Select_Family", comp, family.getClass(), "familyP_Select_Action");
                        }else{
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, new Family().getFamily_Number());
                            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Become a Family", "", identity_Table, person.getClass(), "personA_Become_A_Family_Action");
                        }
                        identity_Table.do_Display_HTML_Row_End();
                        
                    }
                    
                    {
                        Student    student;
                        try {
                            student = person.get_Aka_Student(web_Process.getDatabase_Front());
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End(wpfe);
                            return;
                        }
                        
                        identity_Table.do_Display_HTML_Row_Begin_Std();
                        if(student != null){
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, student.getStudent_Number());
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(identity_Table, student.getStudent_Number(), "Select_Student", comp, student.getClass(), "studentP_Select_Action");
                        }else{
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, new Student().getStudent_Number());
                            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Become a Student", "", identity_Table, person.getClass(), "personA_Become_A_Student_Action");
                        }
                        identity_Table.do_Display_HTML_Row_End();
                        
                    }
                    
                    {
                        if(Subsystem.is_Subsystem_Installed(web_Process.getMy_Request().getSession(), "Enroll_Now_Admin")){
                            Teacher    teacher;
                            try {
                                teacher = person.get_Aka_Teacher(web_Process);
                            }catch(WPFatal_Exception wpfe){
                                web_Process.error_End(wpfe);
                                return;
                            }
                            
                            identity_Table.do_Display_HTML_Row_Begin_Std();
                            if(teacher != null){
                                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, teacher.getTeacher_Id());
                                web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(identity_Table, teacher.getTeacher_Id(), "Select_Teacher", comp, teacher.getClass(), "teacherP_Select_Action");
                            }else{
                                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(identity_Table, new Teacher().getTeacher_Id());
                                web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Become a Teacher", "", identity_Table, person.getClass(), "personA_Become_A_Teacher_Action");
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
    
    
    public void person_View_Record_Do_Action_Edit(Web_Process web_Process) {
        
        Person person = (Person) web_Process.getAttribute("person");
        
        try {
            person = (Person) web_Process.getDatabase_Front().getLocked_Record(person);
        }
        catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + person.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + person.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + person.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        person_Edit_Record_Do_Display(web_Process, person, null);
        return;
    }
    
    
    
    public void person_View_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Person person = (Person) web_Process.getAttribute("person");
        if(person==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            person_View_Record_Do_Display(web_Process, person);
        }
        return;
        
    }
    
    
    
    
    
    
    
    
    
    void person_Edit_Record_Do_Display(Web_Process web_Process, Person person, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty100_Person_Edit_Record", this, "action_Stack_Do_Action",  "person_Edit_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        
        String form_Name="Enty100_Edit";
        
        
        
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        
        try {
            Family_AutoCompletion.getJavaScript(out, web_Process, form_Name, "member_Of_Family_Number", "member_Of_Family_name", "comp_Id", this, "person_Edit_And_New_Shared_Do_Action_Maint");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("Programming Error", wpfe);
            return;
        }
        
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out,
        "onload=\"javascript:" +
        " member_Of_Family_Number_Init(); " +
        web_Process.DF_Focus_Call_For_On_Event(form_Name, person.getFirst_Name()) + "; " +
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
        }
        catch(WPException wpe){
            web_Process.error_End("Failed to make form.", wpe);
            return;
        }
        
        
        
        out.println("<div id='main'>");
        
        person_Edit_And_New_Shared_Superior_Display(out, web_Process, true);
        
        person_Edit_And_New_Shared_Main_Display(out, web_Process, person, ex);
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "person_Edit_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "person_Edit_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "person_Edit_Record_Do_Action_Delete");
            button_Table.do_Display_HTML_Row_End();
            
        }catch(WPFatal_Exception wpfe){
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
        
        web_Process.setAttribute("person", person);
        
        out.close();
        return;
        
    }
    
    
    public void person_Edit_Record_Do_Action_Update(Web_Process web_Process) {
        
        try {
            Person person = (Person) web_Process.getAttribute("person");
            
            try {
                person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                person.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.setAttribute("person", person);
                person_Edit_Record_Do_Display(web_Process, person, dbre);
                return;
            }
            
            web_Process.setAttribute("person", person);
            
            if(!person.update_Outside_User(web_Process))
                return;
            
            
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(person);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + person.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            
            do_Pop_and_Status_Routing(web_Process);
            person_Edit_Record_Do_Update_Family_Info(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void person_Edit_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        Person person = (Person) web_Process.getAttribute("person");
        
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + person.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void person_Edit_Record_Do_Action_Delete(Web_Process web_Process) {
        
        try {
            Person person = (Person) web_Process.getAttribute("person");
            try {
                person.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.error(dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().deleteRecord(person);
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to Delete " + person.getEntity_Name() + " in database. ", dbfe);
                return;
            }
            web_Process.removeAttribute("person");
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void person_Edit_Record_Do_Action_End_Task(Web_Process web_Process) {
        
        Person person = (Person) web_Process.getAttribute("person");
        
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + person.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("person");
        web_Process.end();
        return;
    }
    
    
    public void person_Edit_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Person person = (Person) web_Process.getAttribute("person");
        person_Edit_Record_Do_Display(web_Process, person,null);
        return;
    }
    
    
    
    
    
    
    public void person_Edit_Record_Do_Update_Family_Info(Web_Process web_Process){
        
        try {
            web_Process.pushStatus_Wreload("Enty100_person_Edit_Record_Do_Update_Family_Info", this, "action_Stack_Do_Action",  "person_Edit_Record_Do_Update_Family_Info");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        Person person = (Person) web_Process.getAttribute("person");
        if(person== null){
            web_Process.error_End("Can't find person attribute.", null);
            return;
        }
        
        try {
            person.update_Family_Member_Info(web_Process.getDatabase_Front());
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End(wpfe);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }catch(DFException dfe){
            web_Process.error_End(dfe);
            return;
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End(dbfe);
            return;
        }catch(Database_Front_Deadlock_Exception dbfe){
            web_Process.error(dbfe);
            return;
        }
        
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    
    
    
    
    
    public void person_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, Person person, Exception ex) {
        person_Edit_And_New_Shared_Main_Display(out, web_Process, person, ex, "");
        
    }
    
    public void person_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, Person person, Exception ex, String last_Field_event) {
        
        Family family = Family.get_Possible_Family(web_Process.getDatabase_Front(), person.getComp_Id().getValue(), person.getMember_of_Family_Number().getValue());
        
        try {
            
            
            
            {
                HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
                
                record_Table.do_Display_HTML_Table_Begin_Std();
                record_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getPerson_Number());
                
                
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, person.getFirst_Name());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, person.getLast_Name());
                
                
                record_Table.do_Display_HTML_TBody_End();
                record_Table.do_Display_HTML_Table_End();
            }
            
            if(web_Process.getUser().is_A_System_User()){

                
                {
                    HTML_Table_Tag record_TableA = new HTML_Table_Tag("record_TableA", out, "Record_Edit_Table");
                    
                    record_TableA.do_Display_HTML_Table_Begin_Std();
                    record_TableA.do_Display_HTML_TBody_Begin_Std();
                    
                    
                    
                    record_TableA.do_Display_HTML_Row_Begin_Std();
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_TableA, person.getMember_of_Family_Number());
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_TD("", record_TableA, person.getMember_of_Family_Number(),
                    "onkeyup='member_Of_Family_Number_DoFind()'");
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_TableA, family.getName_Last_Then_First(web_Process.getDatabase_Front()));
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_TD("member_Of_Family_",record_TableA, family.getName_Last_Then_First(web_Process.getDatabase_Front()),
                    "onkeyup='member_Of_Family_Number_DoCompletion()'");
                    record_TableA.do_Display_HTML_Row_End();
                    
                    
                    
                    record_TableA.do_Display_HTML_TBody_End();
                    record_TableA.do_Display_HTML_Table_End();
                }
                
                Family_AutoCompletion.getDivTable_Static(out, "member_Of_Family_Number");
            }else{
                
                if(person.getMember_of_Family_Number().getValue().intValue()!=0){
                    HTML_Table_Tag record_TableA = new HTML_Table_Tag("record_TableA", out, "Record_Edit_Table");
                    
                    record_TableA.do_Display_HTML_Table_Begin_Std();
                    record_TableA.do_Display_HTML_TBody_Begin_Std();
                    
                    record_TableA.do_Display_HTML_Row_Begin_Std();
                    
                    record_TableA.do_Display_HTML_TH_Element("Member of The Family of ");
                    web_Process.do_Display_HTML_DF_Display_In_Table(record_TableA, family.getName_Last_Then_First(web_Process.getDatabase_Front()));
                    record_TableA.do_Display_HTML_Row_End();
                    
                    record_TableA.do_Display_HTML_TBody_End();
                    record_TableA.do_Display_HTML_Table_End();
                }
            }
            
            
            
            
            
            {
                HTML_Table_Tag record_TableB = new HTML_Table_Tag("record_TableB", out, "Record_Edit_Table");
                
                record_TableB.do_Display_HTML_Table_Begin_Std();
                record_TableB.do_Display_HTML_TBody_Begin_Std();
                
                record_TableB.do_Display_HTML_Row_Begin_Std();
                record_TableB.do_Display_HTML_TH_Element_Col_Span("Address",2);
                record_TableB.do_Display_HTML_Row_End();
                
                
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, person.getAddress_Type());
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, person.getStreet_Address(),
                "onfocus=\"javascript: if ( " +
                web_Process.DF_JS_Value_For_On_Event(person.getAddress_Type()) + " =='" + Person.USE_FAMILY_TEXT +"' ) { " +
                web_Process.DF_Focus_Call_For_On_Event(person.getCity()) + " } " +
                "\" ");
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, person.getCity(),
                "onfocus=\"javascript: if ( " +
                web_Process.DF_JS_Value_For_On_Event(person.getAddress_Type()) + " =='" + Person.USE_FAMILY_TEXT +"' ) { " +
                web_Process.DF_Focus_Call_For_On_Event(person.getState_Abbr()) + " } " +
                "\" ");
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, person.getState_Abbr(),
                "onfocus=\"javascript: if ( " +
                web_Process.DF_JS_Value_For_On_Event(person.getAddress_Type()) + " =='" + Person.USE_FAMILY_TEXT +"' ) { " +
                web_Process.DF_Focus_Call_For_On_Event(person.getZip_Code()) + " } " +
                "\" ");
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableB, person.getZip_Code(),
                "onfocus=\"javascript: if ( " +
                web_Process.DF_JS_Value_For_On_Event(person.getAddress_Type()) + " =='" + Person.USE_FAMILY_TEXT +"' ) { " +
                web_Process.DF_Focus_Call_For_On_Event(person.getPhone_Type()) + " } " +
                "\" ");
                
                
                record_TableB.do_Display_HTML_TBody_End();
                record_TableB.do_Display_HTML_Table_End();
            }
            
            
            {
                HTML_Table_Tag record_TableC = new HTML_Table_Tag("record_TableC", out, "Record_Edit_Table");
                
                record_TableC.do_Display_HTML_Table_Begin_Std();
                record_TableC.do_Display_HTML_TBody_Begin_Std();
                
                
                record_TableC.do_Display_HTML_Row_Begin_Std();
                record_TableC.do_Display_HTML_TH_Element(" ");
                record_TableC.do_Display_HTML_Row_End();
                
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableC, person.getPhone_Type());
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableC, person.getPhone(),
                "onfocus=\"javascript: if ( " +
                web_Process.DF_JS_Value_For_On_Event(person.getPhone_Type()) + " =='" + Person.USE_FAMILY_TEXT +"' ) { " +
                web_Process.DF_Focus_Call_For_On_Event(person.getE_Mail_Type()) + " } " +
                "\" ");
                
                record_TableC.do_Display_HTML_TBody_End();
                record_TableC.do_Display_HTML_Table_End();
            }
            
            
            
            {
                HTML_Table_Tag record_TableD = new HTML_Table_Tag("record_TableD", out, "Record_Edit_Table");
                
                record_TableD.do_Display_HTML_Table_Begin_Std();
                record_TableD.do_Display_HTML_TBody_Begin_Std();
                
                
                record_TableD.do_Display_HTML_Row_Begin_Std();
                record_TableD.do_Display_HTML_TH_Element(" ");
                record_TableD.do_Display_HTML_Row_End();
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableD, person.getE_Mail_Type());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableD, person.getE_Mail(),
                "onfocus=\"javascript: if ( " +
                web_Process.DF_JS_Value_For_On_Event(person.getE_Mail_Type()) + " =='" + Person.USE_FAMILY_TEXT +"' ) { " +
                web_Process.DF_Focus_Call_For_On_Event(person.getDate_Of_Birth_Status()) + " } " +
                "\" ");
                
                
                record_TableD.do_Display_HTML_TBody_End();
                record_TableD.do_Display_HTML_Table_End();
            }
            
            
            
            {
                HTML_Table_Tag record_TableE = new HTML_Table_Tag("record_TableE", out, "Record_Edit_Table");
                
                record_TableE.do_Display_HTML_Table_Begin_Std();
                record_TableE.do_Display_HTML_TBody_Begin_Std();
                
                
                
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableE, person.getDate_Of_Birth_Status());
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableE, person.getDate_Of_Birth(),
                "onfocus=\"javascript: if ( " +
                web_Process.DF_JS_Value_For_On_Event(person.getDate_Of_Birth_Status()) + " !='Known' ) { " +
                web_Process.DF_Focus_Call_For_On_Event(person.getGender()) + " } " +
                "\" ");
                
                
                if(web_Process.getUser().is_A_System_User()){
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableE, person.getGender());
                    
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableE, person.getObsolete(), last_Field_event);
                    
                }else{
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_TableE, person.getGender(), last_Field_event);
                    
                }
                
                
                
                record_TableE.do_Display_HTML_TBody_End();
                record_TableE.do_Display_HTML_Table_End();
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
    
    
    
    
    
    public void subordinate_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        
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
                web_Process.do_Display_HTML_DF_As_Hidden_Input(comp.getComp_Id());
                web_Process.do_Display_HTML_DF_As_Hidden_Input(person.getPerson_Number());
            }
            catch(WPException wpe){
                web_Process.error_End("Programming error.", wpe);
                return;
            }
        }
    }
    
    
    
    public void person_Edit_And_New_Shared_Do_Action_Maint(Web_Process web_Process) {
        
        try {
            Person person = (Person) web_Process.getAttribute("person");
            
            
            try {
                person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                person.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                ;
            }
            web_Process.setAttribute("person", person);
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

