/*
 * Enty400.java
 *
 * Created on July 9, 2007, 12:51 PM
 */

package entity_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.security_Management.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import entity_Management.datum.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 * @version
 */

public class Enty400 extends WPHttpServlet_Base {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Outside_User Maintenance";
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
                process_Load_Parameters(web_Process);
                return;
                
            }else if(action.equals("Reload")){
                try {
                    Security_Module.check_Query(web_Process);
                }catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                process_Load_Parameters(web_Process);
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
    
    
    
    
    
    
    public void process_Load_Parameters(Web_Process web_Process){
        try {
            String second_Action = web_Process.getMy_Request().getParameter("second_Action");
            if(second_Action==null){
                get_Comp_Do_Action_Start(web_Process);
            }else{
                Web_Process parent_Web_Process;
                try {
                    parent_Web_Process = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), web_Process.getParent_Web_Process_Id());
                }
                catch(WPException wpe){
                    web_Process.error_End("Unable to load parent web process.", wpe);
                    return;
                }
                
                if(second_Action.equals("add_Person") || second_Action.equals("edit_Person")){
                    Person person = (Person) parent_Web_Process.getAttribute("person");
                    if(person== null){
                        web_Process.error_End("Can't find person attribute.", null);
                        return;
                    }
                    web_Process.setAttribute("person", person);
                    
                    Comp comp;
                    try {
                        comp = (Comp) web_Process.getDatabase_Front().getRecord(new Comp(person.getComp_Id().getValue()));
                    }catch(Database_Front_Exception dbfe){
                        web_Process.error_End(dbfe);
                        return;
                    }catch(Database_Front_No_Results_Exception dbfe){
                        web_Process.error_End("No Such "+new Comp().getEntity_Name()+ " as "+ person.getComp_Id().getValue(), dbfe);
                        return;
                    }
                    web_Process.setAttribute("comp", comp);
                    
                }
                
                
                if(second_Action.equals("add_Organization") || second_Action.equals("edit_Organization")){
                    Organization organization = (Organization) parent_Web_Process.getAttribute("organization");
                    if(organization== null){
                        web_Process.error_End("Can't find organization attribute.", null);
                        return;
                    }
                    web_Process.setAttribute("organization", organization);
                    
                    Comp comp;
                    try {
                        comp = (Comp) web_Process.getDatabase_Front().getRecord(new Comp(organization.getComp_Id().getValue()));
                    }catch(Database_Front_Exception dbfe){
                        web_Process.error_End(dbfe);
                        return;
                    }catch(Database_Front_No_Results_Exception dbfe){
                        web_Process.error_End("No Such "+new Organization().getEntity_Name()+ " as "+ organization.getComp_Id().getValue(), dbfe);
                        return;
                    }
                    web_Process.setAttribute("comp", comp);
                    
                }
                
                
                if(second_Action.equals("add_Person")){
                    add_Person(web_Process);
                } else if(second_Action.equals("edit_Person")){
                    edit_Person(web_Process);
                } else if(second_Action.equals("add_Organization")){
                    add_Organization(web_Process);
                } else if(second_Action.equals("edit_Organization")){
                    edit_Organization(web_Process);
                }else {
                    web_Process.error_End("second_Action Parameter is of an unknow value :"+second_Action+":", null);
                    return;
                }
                
                
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    void add_Person(Web_Process web_Process){
        try {
            Person person = (Person) web_Process.getAttribute("person");
            
            Outside_User    outside_User = new Outside_User(person);;
            
            
            Outside_User[] outside_Users = null;
            try{
                outside_Users = (Outside_User[])web_Process.getDatabase_Front().getMany_Records(outside_User,
                "WHERE " + outside_User.getComp_Id().toWhere_Clause() +
                " AND " +  outside_User.getType_Index().toWhere_Clause() +
                " AND " +  outside_User.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return;
            }
            
            if(outside_Users != null){
                web_Process.error_End("This "+person.getEntity_Name()+" already has a login.", null);
                return;
            }else{
                web_Process.setAttribute("outside_User", outside_User);
                outside_User_New_Record_Do_Display(web_Process, outside_User, null);
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
        
    }
    
    void edit_Person(Web_Process web_Process){
        try {
            Person person = (Person) web_Process.getAttribute("person");
            
            Outside_User    outside_User = new Outside_User(person);
            
            
            Outside_User[] outside_Users = null;
            try{
                outside_Users = (Outside_User[])web_Process.getDatabase_Front().getMany_Records(outside_User,
                "WHERE " + outside_User.getComp_Id().toWhere_Clause() +
                " AND " +  outside_User.getType_Index().toWhere_Clause() +
                " AND " +  outside_User.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return;
            }
            
            if(outside_Users != null){
                web_Process.setAttribute("outside_User", outside_Users[0]);
                outside_User_Edit_Record_Do_Display(web_Process, outside_Users[0], null);
            }else{
                web_Process.error_End("This "+person.getEntity_Name()+" does not have a login.", null);
                return;
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    void add_Organization(Web_Process web_Process){
        return;
    }
    
    void edit_Organization(Web_Process web_Process){
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
                outside_User_List_Records_Do_Display(web_Process);
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
            outside_User_List_Records_Do_Display(web_Process);
            return;
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
    }
    
    
    
    
    public void outside_User_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
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
    
    
    
    void outside_User_List_Records_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("Enty400_outside_User_List_Records", this, "action_Stack_Do_Action",  "outside_User_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            String form_Name="Enty400_List";
            String focus_Button_Form="Enty400_find";
            
            Comp comp = (Comp)web_Process.getAttribute("comp");
            if(comp== null){
                web_Process.error_End("Can't find comp attribute.", null);
                return;
            }
            
            
            Outside_User temp_Outside_User= new Outside_User();
            try {
                temp_Outside_User.setComp_Id(comp.getComp_Id().getValue());
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
            web_Process.DF_Focus_Call_For_On_Event(focus_Button_Form, temp_Outside_User.getOutside_User_Id()) + "; " +
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
            
            outside_User_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            
            String  where_SQL_Clause ="WHERE " +
            temp_Outside_User.getComp_Id().toWhere_Clause();
            
            // get sort SQL clause from Display List Sort Manager
            Display_List_Sort_Manager outside_User_Display_List_Sort_Manager;
            
            outside_User_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("outside_User_Display_List_Sort_Manager");
            if(outside_User_Display_List_Sort_Manager==null){
                outside_User_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.setAttribute("outside_User_Display_List_Sort_Manager", outside_User_Display_List_Sort_Manager);
            String sort_SQL_Clause = outside_User_Display_List_Sort_Manager.getSort_SQL_Clause();
            
            // get or Initialize Display List Pagination Manager
            Display_List_Pagination_Manager outside_User_Display_List_Pagination_Manager;
            try {
                outside_User_Display_List_Pagination_Manager = web_Process.provision_Display_List_Pagination_Manager("outside_User_Display_List_Pagination_Manager", temp_Outside_User, where_SQL_Clause, 10);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error.", dbfe);
                return;
            }
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag list_Table = new HTML_Table_Tag("list_Table", out, "Records_List_Table");
            list_Table.setUse_Even_Odd_Classes(true);
            list_Table.do_Display_HTML_Table_Begin_Std();
            list_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            Outside_User[] outside_User_List;
            try {
                outside_User_List = (Outside_User[]) web_Process.getDatabase_Front().getMany_Records(temp_Outside_User, where_SQL_Clause + " " + sort_SQL_Clause, outside_User_Display_List_Pagination_Manager.getFirst_Item_Number_On_Current_Page(), outside_User_Display_List_Pagination_Manager.getItems_Per_Page());
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            Person temp_Person = new Person();
            Organization temp_Organization = new Organization();
            
            if(outside_User_List == null){
                list_Table.do_Display_HTML_Row_Begin_Std();
                list_Table.do_Display_HTML_TD_Element(" No Records Returned ");
                list_Table.do_Display_HTML_Row_End();
                
            }else{
                list_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Outside_User.getOutside_User_Id(),  outside_User_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Outside_User.getType_Index(),  outside_User_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Outside_User.getPerson_Number(),  outside_User_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, temp_Person.getName_First_Then_Last());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Outside_User.getOrganization_Number(),  outside_User_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(list_Table, temp_Organization.getName());
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Outside_User.getObsolete(),  outside_User_Display_List_Sort_Manager);
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, temp_Outside_User.getLast_Logged_In(),  outside_User_Display_List_Sort_Manager);
                
                list_Table.do_Display_HTML_Row_End();
                
                for(int row=0; row < outside_User_List.length; row++){
                    Outside_User outside_User = outside_User_List[row];
                    
                    Person person = new Person();
                    Organization organization = new Organization();
                    switch(StringPlus.getArray_Index_of_Value(Outside_User.OUTSIDE_USER_TYPES, outside_User.getType_Index().getValue())){
                        case Outside_User.OUTSIDE_USER_TYPE_INDEX_PERSON:
                            person= Person.get_Possible_Person(web_Process.getDatabase_Front(), outside_User.getComp_Id(), outside_User.getPerson_Number());
                            break;
                        case Outside_User.OUTSIDE_USER_TYPE_INDEX_ORGANIZATION:
                            organization= Organization.get_Possible_Organization(web_Process.getDatabase_Front(), outside_User.getComp_Id(), outside_User.getOrganization_Number());
                            break;
                    }
                    
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    try {
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, outside_User.getOutside_User_Id(), "Select", comp, this, "outside_User_List_Records_Do_Action_Select");
                    }catch(WPFatal_Exception wpe){
                        web_Process.error_End(wpe);
                        return;
                    }
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, outside_User.getType_Index());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, outside_User.getPerson_Number());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, person.getName_First_Then_Last());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, outside_User.getOrganization_Number());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, organization.getName());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, outside_User.getObsolete());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, outside_User.getLast_Logged_In());
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
                out.println(web_Process.display_List_Pagination_Manager_To_Html(outside_User_Display_List_Pagination_Manager));
            }catch(WPException wpe){
                web_Process.error_End("Failed to display pagination manager.", wpe);
                return;
            }
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            
            HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
            
            button_Table.do_Display_HTML_Table_Begin_Std();
            button_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            try {
                button_Table.do_Display_HTML_Row_Begin_Std();
                button_Table.do_Display_HTML_TD_Begin();
                web_Process.do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, focus_Button_Form, "action", "Go", "Go", "", temp_Outside_User.getOutside_User_Id(), 15, comp, this, "outside_User_List_Records_Do_Action_Go");
                button_Table.do_Display_HTML_TD_End();
                button_Table.do_Display_HTML_Row_End();
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                button_Table.do_Display_HTML_TD_Begin();
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty400_end", "action", "End", "End", "", this, "std_Action_End");
                button_Table.do_Display_HTML_TD_End();
                button_Table.do_Display_HTML_Row_End();
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                button_Table.do_Display_HTML_TD_Begin();
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty400_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                button_Table.do_Display_HTML_TD_End();
                button_Table.do_Display_HTML_Row_End();
                
            }catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make buttons.", wpfe);
                return;
            }
            
            button_Table.do_Display_HTML_TBody_End();
            button_Table.do_Display_HTML_Table_End();
            
            out.println("</div>");
            
            
            web_Process.do_Display_HTML_Body_End(out);
            web_Process.do_Display_HTML_Page_End(out);
            
            //        web_Process.setAttribute("outside_User", outside_User);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void outside_User_List_Records_Do_Action_Go(Web_Process web_Process) {
        try {
            Outside_User temp_Outside_User=new Outside_User();
            
            try{
                temp_Outside_User.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Outside_User.getOutside_User_Id().getField_Title()+".", dbre);
                return;
            }
            Outside_User outside_User;
            try{
                outside_User=(Outside_User)web_Process.getDatabase_Front().getRecord(temp_Outside_User);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }catch(Database_Front_No_Results_Exception dbfe){
                // no such Outside_User
                web_Process.error("No such " + temp_Outside_User.getEntity_Name()+".", dbfe);
                //outside_User_New_Record_Do_Display(web_Process, temp_Outside_User, null);
                return;
            }
            // found a Outside_User;
            outside_User_View_Record_Do_Display(web_Process, outside_User);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void outside_User_List_Records_Do_Action_Select(Web_Process web_Process) {
        try {
            Outside_User temp_Outside_User=new Outside_User();
            
            try{
                temp_Outside_User.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Outside_User.getOutside_User_Id().getField_Title()+".", dbre);
                return;
            }
            Outside_User outside_User;
            try{
                outside_User=(Outside_User)web_Process.getDatabase_Front().getRecord(temp_Outside_User);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }catch(Database_Front_No_Results_Exception dbfe){
                // no such Outside_User
                web_Process.error("No Such " + temp_Outside_User.getEntity_Name() + ".", dbfe);
                return;
            }
            // found a Outside_User;
            outside_User_View_Record_Do_Display(web_Process, outside_User);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void outside_User_List_Records_Do_Action_Reload(Web_Process web_Process) {
        try {
            Outside_User    outside_User = new Outside_User();
            
            // look for sorting commands
            Display_List_Sort_Manager outside_User_Display_List_Sort_Manager;
            
            outside_User_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("outside_User_Display_List_Sort_Manager");
            if(outside_User_Display_List_Sort_Manager==null){
                outside_User_Display_List_Sort_Manager = new Display_List_Sort_Manager();
            }
            
            web_Process.do_Process_Of_Sort_Manger_Request(outside_User_Display_List_Sort_Manager, outside_User);
            web_Process.setAttribute("outside_User_Display_List_Sort_Manager", outside_User_Display_List_Sort_Manager);
            
            // Look for paging commands
            Display_List_Pagination_Manager outside_User_Display_List_Pagination_Manager = (Display_List_Pagination_Manager)web_Process.getAttribute("outside_User_Display_List_Pagination_Manager");
            
            if(outside_User_Display_List_Pagination_Manager!=null){
                outside_User_Display_List_Pagination_Manager.servlet_Request_Proccesor(web_Process.getMy_Request());
                web_Process.setAttribute("outside_User_Display_List_Pagination_Manager", outside_User_Display_List_Pagination_Manager);
            }
            
            
            outside_User_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    void outside_User_New_Record_Do_Display(Web_Process web_Process, Outside_User outside_User, Exception ex) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty400_outside_User_New_Record", this, "action_Stack_Do_Action",  "outside_User_New_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        String form_Name="Enty400_New";
        
        
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
        
        outside_User_Edit_And_New_Shared_Main_Display(out, web_Process, outside_User, ex);
        
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "outside_User_New_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "outside_User_New_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "End Task", "", button_Table, this, "std_Action_End_Task");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "E-Mail Password", "", button_Table, this, "outside_User_New_Record_Do_Action_EMail_Password");
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
        
        web_Process.setAttribute("outside_User", outside_User);
        
        out.close();
    }
    
    
    
    public void outside_User_New_Record_Do_Action_Create(Web_Process web_Process) {
        try {
            Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
            
            try {
                outside_User.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                outside_User.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("outside_User", outside_User);
                outside_User_New_Record_Do_Display(web_Process, outside_User, dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(outside_User);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + outside_User.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void outside_User_New_Record_Do_Action_Cancel(Web_Process web_Process) {
        web_Process.removeAttribute("outside_User");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void outside_User_New_Record_Do_Action_EMail_Password(Web_Process web_Process) {
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
            
            try {
                outside_User.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                outside_User.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("outside_User", outside_User);
                outside_User_New_Record_Do_Display(web_Process, outside_User, dbre);
                return;
            }
            web_Process.setAttribute("outside_User", outside_User);
            
            try {
                
                
                Properties props = System.getProperties();
                props.put("mail.smtp.host", "localhost");
                
                Session em_Session = Session.getDefaultInstance(props, null);
                
                Message msg = new MimeMessage(em_Session);
                msg.setFrom(new InternetAddress(comp.getE_Mail().getValue()));
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(outside_User.getOutside_User_Id().getValue()));
                
                msg.setSubject(web_Process.getSubscriber_Organization().getName() +" Information");
                
                
                
                String content ="For " + comp.getName() + "  \n" +
                "Your User Id is " + outside_User.getOutside_User_Id().getValue() +"  \n"+
                "Your password is " + outside_User.getPassword().getValue()+"  \n"+
                "Our WebSite is  " + comp.getWeb_Site()+"  \n";
                
                msg.setContent(content, "text/plain");
                
                
                Transport.send(msg);
                
            } catch (MessagingException me) {
                web_Process.error(me);
                return;
            }
            outside_User_New_Record_Do_Action_Reload(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void outside_User_New_Record_Do_Action_Reload(Web_Process web_Process) {
        Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
        outside_User_New_Record_Do_Display(web_Process, outside_User, null);
        return;
    }
    
    
    
    void outside_User_View_Record_Do_Display(Web_Process web_Process, Outside_User outside_User) {
        
        try {
            web_Process.pushStatus_Wreload("Enty400_outside_User_View_Record", this, "action_Stack_Do_Action",  "outside_User_View_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        try {
            
            String form_Name="Enty400_View";
            
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
            
            outside_User_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            
            
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_View_Table");
            
            record_Table.do_Display_HTML_Table_Begin_Std();
            record_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            Person person = new Person();
            Organization organization = new Organization();
            switch(StringPlus.getArray_Index_of_Value(Outside_User.OUTSIDE_USER_TYPES, outside_User.getType_Index().getValue())){
                case Outside_User.OUTSIDE_USER_TYPE_INDEX_PERSON:
                    person= Person.get_Possible_Person(web_Process.getDatabase_Front(), outside_User.getComp_Id(), outside_User.getPerson_Number());
                    break;
                case Outside_User.OUTSIDE_USER_TYPE_INDEX_ORGANIZATION:
                    organization= Organization.get_Possible_Organization(web_Process.getDatabase_Front(), outside_User.getComp_Id(), outside_User.getOrganization_Number());
                    break;
            }
            
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getOutside_User_Id());
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getType_Index());
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getPerson_Number());
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getName_First_Then_Last());
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getOrganization_Number());
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getName());
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getLast_Logged_In());
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getObsolete());
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getPassword());
            
            
            
            
            
            record_Table.do_Display_HTML_TBody_End();
            record_Table.do_Display_HTML_Table_End();
            
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            
            
            HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
            
            button_Table.do_Display_HTML_Table_Begin_Std();
            button_Table.do_Display_HTML_TBody_Begin_Std();
            
            try {
                button_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "outside_User_View_Record_Do_Action_Edit");
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
            
            web_Process.setAttribute("outside_User", outside_User);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void outside_User_View_Record_Do_Action_Edit(Web_Process web_Process) {
        
        Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
        
        try {
            outside_User = (Outside_User) web_Process.getDatabase_Front().getLocked_Record(outside_User);
        }catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + outside_User.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + outside_User.getEntity_Name() + " record in database. ", dbfe);
            return;
        }catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + outside_User.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        outside_User_Edit_Record_Do_Display(web_Process, outside_User, null);
        return;
    }
    
    
    public void outside_User_View_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
        if(outside_User==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            outside_User_View_Record_Do_Display(web_Process, outside_User);
        }
        return;
    }
    
    
    
    void outside_User_Edit_Record_Do_Display(Web_Process web_Process, Outside_User outside_User, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty400_outside_User_Edit_Record", this, "action_Stack_Do_Action",  "outside_User_Edit_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        String form_Name="Enty400_New";
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
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
        
        outside_User_Edit_And_New_Shared_Main_Display(out, web_Process, outside_User, ex);
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "outside_User_Edit_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "outside_User_Edit_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            if(web_Process.getUser().is_A_System_User()){

                
                button_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "outside_User_Edit_Record_Do_Action_Delete");
                button_Table.do_Display_HTML_Row_End();
                
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "E-Mail Password", "", button_Table, this, "outside_User_Edit_Record_Do_Action_EMail_Password");
                button_Table.do_Display_HTML_Row_End();
                
            }
            
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
        
        web_Process.setAttribute("outside_User", outside_User);
        
        out.close();
        return;
    }
    
    
    
    public void outside_User_Edit_Record_Do_Action_Update(Web_Process web_Process) {
        
        try {
            Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
            
            try {
                outside_User.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                outside_User.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("outside_User", outside_User);
                outside_User_Edit_Record_Do_Display(web_Process, outside_User, dbre);
                return;
            }
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(outside_User);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + outside_User.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void outside_User_Edit_Record_Do_Action_EMail_Password(Web_Process web_Process) {
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
            
            try {
                outside_User.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                outside_User.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }catch(Database_Record_Exception dbre){
                web_Process.setAttribute("outside_User", outside_User);
                outside_User_Edit_Record_Do_Display(web_Process, outside_User, dbre);
                return;
            }
            web_Process.setAttribute("outside_User", outside_User);
            
            try {
                
                
                Properties props = System.getProperties();
                props.put("mail.smtp.host", "localhost");
                
                Session em_Session = Session.getDefaultInstance(props, null);
                
                Message msg = new MimeMessage(em_Session);
                msg.setFrom(new InternetAddress(comp.getE_Mail().getValue()));
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(outside_User.getOutside_User_Id().getValue()));
                
                msg.setSubject(web_Process.getSubscriber_Organization().getName() +" Information");
                
                
                
                String content ="For " + comp.getName() + "  \n" +
                "Your User Id is " + outside_User.getOutside_User_Id().getValue() +"  \n"+
                "Your password is " + outside_User.getPassword().getValue()+"  \n"+
                "Our WebSite is  " + comp.getWeb_Site()+"  \n";
                
                msg.setContent(content, "text/plain");
                
                
                Transport.send(msg);
                
            } catch (MessagingException me) {
                web_Process.error(me);
                return;
            }
            outside_User_Edit_Record_Do_Action_Reload(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void outside_User_Edit_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + outside_User.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    public void outside_User_Edit_Record_Do_Action_Delete(Web_Process web_Process) {
        try {
            Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
            try {
                outside_User.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
            }catch(Database_Record_Exception dbre){
                web_Process.error(dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().deleteRecord(outside_User);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to Delete " + outside_User.getEntity_Name() + " in database. ", dbfe);
                return;
            }
            web_Process.removeAttribute("outside_User");
            do_Pop_and_Status_Routing(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void outside_User_Edit_Record_Do_Action_End_Task(Web_Process web_Process) {
        
        Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + outside_User.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("outside_User");
        web_Process.end();
        return;
    }
    
    
    public void outside_User_Edit_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Outside_User outside_User = (Outside_User) web_Process.getAttribute("outside_User");
        outside_User_Edit_Record_Do_Display(web_Process, outside_User,null);
        return;
    }
    
    
    
    
    public void outside_User_Edit_And_New_Shared_Main_Display(PrintWriter out, Web_Process web_Process, Outside_User outside_User, Exception ex) {
        
        outside_User_Edit_And_New_Shared_Superior_Display(out, web_Process, true);
        
        try {
            //web_Process.do_Display_HTML_In_Form_Submit_Button(out, "action", "page1", "Page 1", "text");
            
            HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table", out, "Record_Edit_Table");
            
            record_Table.do_Display_HTML_Table_Begin_Std();
            record_Table.do_Display_HTML_TBody_Begin_Std();
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getOutside_User_Id());
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getType_Index());
            
            
            Person person = new Person();
            Organization organization = new Organization();
            switch(StringPlus.getArray_Index_of_Value(Outside_User.OUTSIDE_USER_TYPES, outside_User.getType_Index().getValue())){
                case Outside_User.OUTSIDE_USER_TYPE_INDEX_PERSON:
                    person= Person.get_Possible_Person(web_Process.getDatabase_Front(), outside_User.getComp_Id(), outside_User.getPerson_Number());
                    web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getPerson_Number());
                    web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getName_First_Then_Last());
                    
                    break;
                    
                case Outside_User.OUTSIDE_USER_TYPE_INDEX_ORGANIZATION:
                    organization= Organization.get_Possible_Organization(web_Process.getDatabase_Front(), outside_User.getComp_Id(), outside_User.getOrganization_Number());
                    web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getOrganization_Number());
                    web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, organization.getName());
                    
                    break;
            }
            
            
            
            
            
            
            
            web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, outside_User.getLast_Logged_In());
            
            if(web_Process.getUser().is_A_System_User()){
                web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, outside_User.getObsolete());
            }
            web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, outside_User.getPassword());
            
            
            
            
            
            
            
            
            
            record_Table.do_Display_HTML_TBody_End();
            record_Table.do_Display_HTML_Table_End();
            
            
            if(ex!=null){
                out.println("<p>Error:"+StringPlus.html_Encode(ex.getMessage()));
            }
            
        }catch(WPException wpe){
            web_Process.error_End("Programming error.", wpe);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
        
    }
    
}
