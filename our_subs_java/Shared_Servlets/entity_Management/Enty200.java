/*
 * Enty200.java
 *
 * Created on August 30, 2006, 4:59 AM
 */

package entity_Management;


import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import customer_Management.*;
import customer_Management.data.*;
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
public class Enty200 extends WPHttpServlet_Base {
    
    static final String[] Enty200_Page_Names_Edit = { "Main Contact", "Extra" };
    static final String[] Enty200_Page_Names_View = { "Main Contact", "Extra", "Members", "Addresses/Phone/E-Mail" };
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Family Maintenance";
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
        try{
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
                init_Read_Of_Control_Files(web_Process);
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
            init_Read_Of_Control_Files(web_Process);
            family_List_Records_Do_Display(web_Process);
            
            return;
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
    }
    
    
    public void init_Read_Of_Control_Files(Web_Process web_Process) {
        
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            
            Entity_Control entity_Control;
            try {
                entity_Control= (Entity_Control) web_Process.getDatabase_Front().getRecord(new Entity_Control(comp.getComp_Id().getValue()));
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return;
            }catch(Database_Front_No_Results_Exception dbfe){
                web_Process.error_End("No Entity Control Record for this Company.  Use Enty001 to create one.", dbfe);
                return;
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    void process_Loading_Parameters(Web_Process web_Process) {
        
        try {
            Comp comp = (Comp)web_Process.getAttribute("comp");
            
            String family_Number = web_Process.getMy_Request().getParameter("loading_Family_Number");
            if(family_Number!=null){
                
                Family family;
                try {
                    family = (Family)web_Process.getDatabase_Front().getRecord(new Family(comp.getComp_Id().getValue(), new Integer(family_Number)));
                }catch(Database_Front_Exception dbfe){
                    web_Process.error_End(dbfe);
                    return;
                }catch(Database_Front_No_Results_Exception dbfe){
                    web_Process.error_End("No Such Family Found", dbfe);
                    return;
                }
                web_Process.setAttribute("family", family);
                
                // found a Family;
                
                Person person = new Person(family.getComp_Id().getValue(), family.getPerson_Number().getValue());
                
                try{
                    person=(Person)web_Process.getDatabase_Front().getRecord(person);
                }
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Failure.", dbfe);
                    return;
                }
                catch(Database_Front_No_Results_Exception dbfe){
                    // no such Person
                    web_Process.error("No Such Person.  Person # :"+ family.getPerson_Number().toString() + " deleted but not Family", null);
                    return;
                }
                // found a Person
                
                
                
                
                family_View_Record_Do_Display(web_Process, family, person);
                
            }else {
                String loading_Action = web_Process.getMy_Request().getParameter("loading_Action");
                if(loading_Action != null){
                    if(loading_Action.equals("Create_From_Person")){
                        String loading_Person_Number = web_Process.getMy_Request().getParameter("loading_Person_Number");
                        if(loading_Person_Number==null){
                            web_Process.error_End("For me to Create a Family from a Person you must also provide the Person Number.", null);
                        }else{
                            
                            Person person = new Person(comp.getComp_Id().getValue(), new Integer(loading_Person_Number));
                            
                            try{
                                person=(Person)web_Process.getDatabase_Front().getRecord(person);
                            }
                            catch(Database_Front_Exception dbfe){
                                web_Process.error_End("Database Failure.", dbfe);
                                return;
                            }
                            catch(Database_Front_No_Results_Exception dbfe){
                                // no such Person
                                web_Process.error("No Such Person # :"+ loading_Person_Number + ".", null);
                                return;
                            }
                            
                            // found a Person
                            web_Process.setAttribute("person", person);
                            
                            new_Record_From_Person(web_Process, person);
                        }
                        
                        
                    } else if(loading_Action.equals("Create_From_Customer")){
                        String loading_Cust_Id = web_Process.getMy_Request().getParameter("loading_Cust_Id");
                        if(loading_Cust_Id == null){
                            web_Process.error_End("For me to Create a Family from a Customer you must also provide the Customer Id.", null);
                        }else{
                            
                            Cust cust = new Cust(comp.getComp_Id().getValue(), loading_Cust_Id);
                            
                            try{
                                cust=(Cust)web_Process.getDatabase_Front().getRecord(cust);
                            }
                            catch(Database_Front_Exception dbfe){
                                web_Process.error_End("Database Failure.", dbfe);
                                return;
                            }
                            catch(Database_Front_No_Results_Exception dbfe){
                                // no such cust
                                web_Process.error("No Such Customer Id :"+ loading_Cust_Id + ".", null);
                                return;
                            }
                            
                            // found a cust
                            web_Process.setAttribute("cust", cust);
                            
                            new_Record_From_Cust(web_Process, cust);
                        }
                        
                    } else if(loading_Action.equals("Create_For_Customer")){
                        String loading_Cust_Id = web_Process.getMy_Request().getParameter("loading_Cust_Id");
                        if(loading_Cust_Id == null){
                            web_Process.error_End("For me to Create a Family for a Customer you must also provide the Customer Id.", null);
                        }else{
                            
                            Cust cust = new Cust(comp.getComp_Id().getValue(), loading_Cust_Id);
                            try{
                                cust=(Cust)web_Process.getDatabase_Front().getRecord(cust);
                            }
                            catch(Database_Front_Exception dbfe){
                                web_Process.error_End("Database Failure.", dbfe);
                                return;
                            }
                            catch(Database_Front_No_Results_Exception dbfe){
                                // no such cust
                                web_Process.error("No Such Customer Id :"+ loading_Cust_Id + ".", null);
                                return;
                            }
                            
                            // found a cust
                            web_Process.setAttribute("cust", cust);
                            new_Record_For_Cust(web_Process, cust);
                        }
                        
                    }else{
                        web_Process.error_End("Unknown loading_Action :"+loading_Action+": for Status :"+web_Process.getStatus_Node().getStatus_Text()+":.", null);
                    }
                    
                }else{
                    family_List_Records_Do_Display(web_Process);
                }
            }
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
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
            StringPlus.http_Encode(web_Process.getProgram().getProgram_Title().toString())+
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
        get_Person_Do_Display(web_Process);
        return;
    }
    
    
    public void get_Person_Do_Action_Reload(Web_Process web_Process) {
        
        Object obj = web_Process.getAttribute("gotten_Person");
        if(obj ==null){
            // back from upper level in this program
            do_Pop_and_Status_Routing(web_Process);
            return;
        }else  if(obj instanceof Person){
            // sub program returned a value
            Person gotten_Person = (Person)obj;
            web_Process.removeAttribute("gotten_Person");
            web_Process.setAttribute("person", gotten_Person);
            
            new_Record_From_Person(web_Process, gotten_Person);
            return;
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
    }
    
    
    void new_Record_From_Person(Web_Process web_Process, Person person) {
        try {
            if(person.getObsolete().getValue().booleanValue()){
                web_Process.error(person.getName_First_Then_Last().getValue() + " Can not be a Family as they are currently an Obsolete Person.", null);
                return;
            }
            
            String person_Where_Clause = " WHERE " +
            person.getComp_Id().toWhere_Clause() + " AND " +
            person.getPerson_Number().toWhere_Clause();
            
            Family[] temp_Familys;
            try {
                temp_Familys = (Family[]) web_Process.getDatabase_Front().getMany_Records(new Family(), person_Where_Clause);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return;
            }
            if(temp_Familys != null){
                web_Process.error(person.getName_First_Then_Last().getValue() + " is already a Family.", null);
                return;
            }
            Family temp_Family=new Family();
            
            family_New_Record_Do_Display(web_Process, temp_Family, person, null);
            
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
        
    }
    
    
    
    
    void new_Record_From_Cust(Web_Process web_Process, Cust cust) {
        
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            
            if(!cust.is_A_Person()){
                web_Process.error_End("This Customer is not a Person and therefore can not be made into a family.",null);
                return;
            }
            
            Person cust_Person;
            try {
                cust_Person = (Person) web_Process.getDatabase_Front().getRecord(new Person(cust.getComp_Id().getValue(), cust.getPerson_Number().getValue()));
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return;
            }catch(Database_Front_No_Results_Exception dbfe){
                web_Process.error_End("This customer can not be found in the person file.", dbfe);
                return;
            }
            
            if(cust_Person.getObsolete().getValue().booleanValue()){
                web_Process.error(cust_Person.getName_First_Then_Last().getValue() + " Can not be a Family as they are currently an Obsolete Person.", null);
                return;
            }
            
            
            if(cust.getCust_Obsolete().getValue().booleanValue()){
                web_Process.error(cust_Person.getName_First_Then_Last().getValue() + " Can not be a Family as they are currently an Obsolete Customer.", null);
                return;
            }
            
            String person_Where_Clause = " WHERE " +
            cust_Person.getComp_Id().toWhere_Clause() + " AND " +
            cust_Person.getPerson_Number().toWhere_Clause();
            
            Family[] temp_Familys;
            try {
                temp_Familys = (Family[]) web_Process.getDatabase_Front().getMany_Records(new Family(), person_Where_Clause);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return;
            }
            if(temp_Familys != null){
                web_Process.error(cust_Person.getName_First_Then_Last().getValue() + " is already a Family.", null);
                return;
            }
            Family temp_Family=new Family();
            
            try {
                temp_Family.setComp_Id(comp.getComp_Id().getValue());
                temp_Family.setCust_Id(cust.getCust_Id().getValue());
            }catch(DFException dfe){
                web_Process.error_End(dfe);
                return;
            }
            
            family_New_Record_Do_Display(web_Process, temp_Family, cust_Person, null);
            
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    void new_Record_For_Cust(Web_Process web_Process, Cust cust) {
        
        try {
            Comp comp =(Comp) web_Process.getAttribute("comp");
            
            if(cust.getCust_Obsolete().getValue().booleanValue()){
                web_Process.error("Can not create a Family for a currently Obsolete Customer.", null);
                return;
            }
            
            Family temp_Family =new Family();
            try {
                temp_Family.setComp_Id(comp.getComp_Id().getValue());
                temp_Family.setCust_Id(cust.getCust_Id().getValue());
            }catch(DFException dfe){
                web_Process.error_End(dfe);
                return;
            }
            
            Person temp_Person =new Person();
            web_Process.setAttribute("create_New_Person", new Boolean(true));
            family_New_Record_Do_Display(web_Process, temp_Family, temp_Person, null);
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    void family_New_Record_Do_Display(Web_Process web_Process, Family family, Person person, Exception ex) {
        
        try {
            
            if(person.getPerson_Number().getValue().intValue() !=0){
                try {
                    family.setPerson_Number(person.getPerson_Number().getValue());
                }catch(DFException dfe){
                    web_Process.error(dfe);
                    return;
                }
                
                // if they are a customer as well, grab it now
                Cust    cust = new Cust();
                
                Cust[] custs = null;
                try{
                    custs = (Cust[])web_Process.getDatabase_Front().getMany_Records(cust, "WHERE " +
                    person.getComp_Id().toWhere_Clause() + " AND " +
                    person.getPerson_Number().toWhere_Clause());
                }catch(Database_Front_Exception dbfe){
                    web_Process.error_End(dbfe);
                    return;
                }
                if(custs != null){
                    cust = custs[0];
                    try {
                        family.setCust_Id(cust.getCust_Id().getValue());
                    }catch(DFException dfe){
                        ;
                    }
                }
            }
            new_Record_Combo_Do_Display(web_Process, family, person, ex);
            
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    
    
    
    
    
    void new_Record_Combo_Do_Display(Web_Process web_Process, Family family, Person person, Exception ex) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty200_family_New_Record", this, "action_Stack_Do_Action",  "family_New_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty200_New";
        
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        
        boolean is_New_Family=true;
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        
        try {
            Cust_AutoCompletion.getJavaScript(out, web_Process, form_Name, "cust_Id", "cust_name", "comp_Id", this, "family_New_Record_Do_Action_Maint");
            Family_AutoCompletion.getJavaScript(out, web_Process, form_Name, "member_Of_Family_Number", "member_Of_Family_name", "comp_Id", this, "family_New_Record_Do_Action_Maint");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("programming error", wpfe);
            return;
        }
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out,
        "onload=\"" +
        web_Process.page_Show_Call_For_On_Event(Enty200_Page_Names_Edit, 0) + "; " +
        " cust_Id_Init(); member_Of_Family_Number_Init(); " +
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
        
        edit_And_New_Shared_Family_Main_Display(out, web_Process, family, person, is_New_Family, ex);
        
        
        
        out.println("</div>");
        
        
        
        out.println("<div id='sidebar'>");
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try{
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Create", "", button_Table, this, "family_New_Record_Do_Action_Create");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action",  "Cancel", "", button_Table, this, "family_New_Record_Do_Action_Cancel");
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
        
        
        out.println("</div>");
        
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        web_Process.setAttribute("family", family);
        web_Process.setAttribute("person", person);
        
        out.close();
        
    }
    
    
    public void family_New_Record_Do_Action_Create(Web_Process web_Process) {
        
        try {
            
            Family family = (Family) web_Process.getAttribute("family");
            Person person = (Person) web_Process.getAttribute("person");
            DFBoolean automatic_Family_Number  = (DFBoolean) web_Process.getAttribute("automatic_Family_Number");
            
            boolean validation_Error=false;
            
            try {
                family.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                family.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                validation_Error=true;
            }
            web_Process.setAttribute("family", family);
            
            try {
                person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                person.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                validation_Error=true;
            }
            web_Process.setAttribute("person", person);
            if(person.getAddress_Type().getValue().equals(Person.USE_FAMILY_TEXT)){
                person.getAddress_Type().setRequest_Error("Not allowed for a Family");
                validation_Error=true;
            }
            if(person.getPhone_Type().getValue().equals(Person.USE_FAMILY_TEXT)){
                person.getPhone_Type().setRequest_Error("Not allowed for a Family");
                validation_Error=true;
            }
            if(person.getE_Mail_Type().getValue().equals(Person.USE_FAMILY_TEXT)){
                person.getE_Mail_Type().setRequest_Error("Not allowed for a Family");
                validation_Error=true;
            }
            
            
            
            if(validation_Error){
                new_Record_Combo_Do_Display(web_Process, family, person, null);
                return;
            }
            
            
            if(automatic_Family_Number.validate_Request(web_Process.getMy_Request()) !=0){
                web_Process.error("Count not validate " + automatic_Family_Number.getField_Title()+".  " + automatic_Family_Number.getRequest_Error(), null);
                return;
            }
            web_Process.setAttribute("automatic_Family_Number", automatic_Family_Number);
            
            
            
            
            if(family.getPerson_Number().getValue().intValue()==0){
                try {
                    person.setPerson_Number(Entity_Control.get_Next_Person_Number(web_Process.getDatabase_Front(), family.getComp_Id()));
                }catch(DFException dfe){
                    web_Process.error_End("can't set " + person.getPerson_Number().getField_Title(), dfe);
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
                
                try {
                    family.setPerson_Number(person.getPerson_Number().getValue());
                }catch(DFException dfe){
                    web_Process.error_End("can't set Family's " + person.getPerson_Number().getField_Title(), dfe);
                    return;
                }
                web_Process.setAttribute("family", family);
            }else{
                
                if(!person.update_Outside_User(web_Process))
                    return;
                
                try {
                    int row_count = web_Process.getDatabase_Front().updateRecord(person);
                }
                catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Failed to update " + person.getEntity_Name() + " to database. ", dbfe);
                    return;
                }
                
            }
            
            
            
            
            
            
            if(automatic_Family_Number.getValue().booleanValue()){
                
                try {
                    family.setFamily_Number(Entity_Control.get_Next_Family_Number(web_Process.getDatabase_Front(), family.getComp_Id()));
                }catch(DFException dfe){
                    web_Process.error_End("can't set " + family.getFamily_Number().getField_Title(), dfe);
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
                web_Process.setAttribute("family", family);
                automatic_Family_Number.setValue(false);
                web_Process.setAttribute("automatic_Family_Number", automatic_Family_Number);
            }
            
            
            
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(family);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + family.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            Enty100 enty100 = new Enty100();
            enty100.person_Edit_Record_Do_Update_Family_Info(web_Process);
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void family_New_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        web_Process.removeAttribute("family");
        web_Process.removeAttribute("person");
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void family_New_Record_Do_Action_Maint(Web_Process web_Process) {
        
        try {
            Family family = (Family) web_Process.getAttribute("family");
            Person person = (Person) web_Process.getAttribute("person");
            DFBoolean automatic_Family_Number  = (DFBoolean) web_Process.getAttribute("automatic_Family_Number");
            
            try {
                family.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                family.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                ;
            }
            web_Process.setAttribute("family", family);
            
            try {
                person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                person.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                ;
            }
            web_Process.setAttribute("person", person);
            
            
            
            
            automatic_Family_Number.validate_Request(web_Process.getMy_Request());
            web_Process.setAttribute("automatic_Family_Number", automatic_Family_Number);
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
    
    
    
    
    public void family_New_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Family family = (Family) web_Process.getAttribute("family");
        Person person = (Person) web_Process.getAttribute("person");
        new_Record_Combo_Do_Display(web_Process, family, person, null);
        return;
        
    }
    
    
    
    void family_View_Record_Do_Display(Web_Process web_Process, Family family, Person person) {
        
        try {
            web_Process.pushStatus_Wreload("Enty200_family_View_Record", this, "action_Stack_Do_Action",  "family_View_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty200_View";
        
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        
        try {
            family = (Family)web_Process.getDatabase_Front().getRecord(family);
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End(dbfe);
            return;
        }catch(Database_Front_No_Results_Exception dbfe){
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
        
        try {
            person = (Person)web_Process.getDatabase_Front().getRecord(person);
        }catch(Database_Front_Exception dbfe){
            web_Process.error_End(dbfe);
            return;
        }catch(Database_Front_No_Results_Exception dbfe){
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
        
        
        
        
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        web_Process.do_Display_HTML_Head_End(out);
        
        
        //web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        web_Process.do_Display_HTML_Body_Begin(out,
        "onload=\"" +
        web_Process.page_Show_Call_For_On_Event(Enty200_Page_Names_View, 0) + "; "  +
        "\" ");
        
        
        out.println("<div id='above_Header'>");
        out.println("</div>");
        
        out.println("<div id='header'>");
        out.println("<div id='header_Text'>");
        out.println(web_Process.getProgram().getProgram_Title().toHTML());
        out.println("</div>");
        out.println("</div>");
        
        
        
        out.println("<div id='main'>");
        
        edit_And_New_Shared_Family_Superior_Display(out, web_Process, false);
        web_Process.page_Button_Links_Do_Display(Enty200_Page_Names_View, out);
        
        
        
        
        {
            //Main Contact
            
            out.println("<div style='position: absolute; top: 50px;left: 0px; right: 0px' id='page0' >");
            
            
            
            {
                HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table0", out, "Record_View_Table");
                
                record_Table.do_Display_HTML_Table_Begin_Std();
                record_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, family.getFamily_Number());
                
                
                record_Table.do_Display_HTML_TBody_End();
                record_Table.do_Display_HTML_Table_End();
                
            }
            
            
            {
                Enty100 enty100 = new Enty100();
                enty100.person_View_Record_Do_Display_Fields(out, web_Process, person);
            }
            
            
            
            
            out.println("</div>");
            
        }
        
        
        
        
        
        {
            //Extra
            
            out.println("<div style='position: absolute; top: 50px;left: 0px; right: 0px' id='page1' >");
            
            
            
            {
                HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table0", out, "Record_View_Table");
                
                record_Table.do_Display_HTML_Table_Begin_Std();
                record_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, family.getFamily_Number());
                
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getName_Last_Then_First());

                record_Table.do_Display_HTML_TBody_End();
                record_Table.do_Display_HTML_Table_End();
                
            }
            
            
            
            Cust cust = Cust.get_Possible_Cust(web_Process.getDatabase_Front(), family.getComp_Id().getValue(), family.getCust_Id().getValue());
            Person cust_Person = Person.get_Possible_Person(web_Process.getDatabase_Front(), cust.getComp_Id().getValue(), cust.getPerson_Number().getValue());
            
            
            {
                
                HTML_Table_Tag record_Table_B = new HTML_Table_Tag("record_Table00", out, "Record_View_Table");
                
                record_Table_B.do_Display_HTML_Table_Begin_Std();
                record_Table_B.do_Display_HTML_TBody_Begin_Std();
                
                
                record_Table_B.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_Table_B, family.getCust_Id());
                try {
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(record_Table_B, family.getCust_Id(), "Select_Cust", comp, cust.getClass(), "custP_Select_Action");
                }catch(WPFatal_Exception wpfe){
                    web_Process.error_End("programming error", wpfe);
                    return;
                }
                
                web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_Table_B, cust_Person.getName_First_Then_Last());
                web_Process.do_Display_HTML_DF_Display_In_Table(record_Table_B, cust_Person.getName_First_Then_Last());
                record_Table_B.do_Display_HTML_Row_End();
                
                
                record_Table_B.do_Display_HTML_TBody_End();
                record_Table_B.do_Display_HTML_Table_End();
                
            }
            
            {
                
                HTML_Table_Tag record_Table_C = new HTML_Table_Tag("record_TableC", out, "Record_View_Table");
                
                record_Table_C.do_Display_HTML_Table_Begin_Std();
                record_Table_C.do_Display_HTML_TBody_Begin_Std();
                
                
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table_C, family.getNotes());
                
                
                record_Table_C.do_Display_HTML_TBody_End();
                record_Table_C.do_Display_HTML_Table_End();
                
            }
            
            out.println("</div>");
            
        }
        
        
        
        {
            //Members
            
            out.println("<div style='position: absolute; top: 50px;left: 0px; right: 0px' id='page2' >");
            
            
            {
                HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table4", out, "Record_View_Table");
                
                record_Table.do_Display_HTML_Table_Begin_Std();
                record_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, family.getFamily_Number());
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getName_Last_Then_First());
                
                record_Table.do_Display_HTML_TBody_End();
                record_Table.do_Display_HTML_Table_End();
            }
            
            
            {
                            
                Cust cust = Cust.get_Possible_Cust(web_Process.getDatabase_Front(), family.getComp_Id().getValue(), family.getCust_Id().getValue());

                web_Process.setAttribute("cust", cust);
                
                Cs100 cs100 = new Cs100();
                
                cs100.cust_Family_Members_Table_Do_Display(out, web_Process, comp, cust, family, true);

                
            }
            
            
            
            out.println("</div>");
        }
        
        
        
        
        {
            //additional records
            
            out.println("<div style='position: absolute; top: 50px;left: 0px; right: 0px' id='page3' >");
            
            {
                HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table4", out, "Record_View_Table");
                
                record_Table.do_Display_HTML_Table_Begin_Std();
                record_Table.do_Display_HTML_TBody_Begin_Std();
                
                
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, family.getFamily_Number());
                web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getName_Last_Then_First());
                
                record_Table.do_Display_HTML_TBody_End();
                record_Table.do_Display_HTML_Table_End();
            }
            
            {
                Enty100 enty100 = new Enty100();
                enty100.person_View_Record_Do_Display_Additional_Records(out, web_Process, person);
            }
            
            out.println("</div>");
        }
        
        
        
        
        
        
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        try {
            web_Process.do_Display_HTML_Form_Begin_Std(out, form_Name);
            
        }
        catch(WPException wpe){
            web_Process.error_End("Failed to make form.", wpe);
            return;
        }
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit", "", button_Table, this, "family_View_Record_Do_Action_Edit");
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
        
        web_Process.setAttribute("family", family);
        web_Process.setAttribute("person", person);
        
        out.close();
    }
    
    
    
    public void family_View_Record_Do_Action_Edit(Web_Process web_Process) {
        
        Family family = (Family) web_Process.getAttribute("family");
        Person person = (Person) web_Process.getAttribute("person");
        
        try {
            family = (Family) web_Process.getDatabase_Front().getLocked_Record(family);
        }
        catch(Database_Front_Deadlock_Exception dbfdle){
            web_Process.error("Failed to lock :" + family.getEntity_Name() + " record in database. Deadlock condition would occur.  Some other user has this record lock.  Try again Later.", dbfdle);
            return;
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to lock " + family.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            web_Process.popStatus_Node();
            web_Process.error("Failed to find " + family.getEntity_Name() + " record in database. ", dbfe);
            return;
        }
        
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
        
        
        family_Edit_Record_Do_Display(web_Process, family, person, null);
        return;
    }
    
    
    
    public void family_View_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Family family = (Family) web_Process.getAttribute("family");
        Person person = (Person) web_Process.getAttribute("person");
        
        if(family==null){
            do_Pop_and_Status_Routing(web_Process);
        }else{
            family_View_Record_Do_Display(web_Process, family, person);
        }
        return;
    }
    
    
    void family_Edit_Record_Do_Display(Web_Process web_Process, Family family, Person person, Exception ex) {
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty200_family_Edit_Record", this, "action_Stack_Do_Action",  "family_Edit_Record_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        String form_Name="Enty200_Edit";
        
        
        
        Comp comp = (Comp) web_Process.getAttribute("comp");
        
        boolean is_New_Family=false;
        
        
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, web_Process.getProgram().getProgram_Title().toHTML());
        
        try {
            Cust_AutoCompletion.getJavaScript(out, web_Process, form_Name, "cust_Id", "cust_name", "comp_Id", this, "family_Edit_Record_Do_Action_Maint");
            Family_AutoCompletion.getJavaScript(out, web_Process, form_Name, "member_Of_Family_Number", "member_Of_Family_name", "comp_Id", this, "family_Edit_Record_Do_Action_Maint");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("programming error", wpfe);
            return;
        }
        web_Process.do_Display_HTML_Head_End(out);
        
        
        // web_Process.do_Display_HTML_Body_Begin_With_Menu(out);
        //web_Process.do_Display_HTML_Body_Begin_Plain(out);
        
        web_Process.do_Display_HTML_Body_Begin(out,
        "onload=\"" +
        web_Process.page_Show_Call_For_On_Event(Enty200_Page_Names_Edit, 0) + "; " +
        " cust_Id_Init(); member_Of_Family_Number_Init(); " +
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
        
        edit_And_New_Shared_Family_Main_Display(out, web_Process, family, person, is_New_Family, ex);
        
        
        
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
        
        button_Table.do_Display_HTML_Table_Begin_Std();
        button_Table.do_Display_HTML_TBody_Begin_Std();
        
        try {
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Update", "", button_Table, this, "family_Edit_Record_Do_Action_Update");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Cancel", "", button_Table, this, "family_Edit_Record_Do_Action_Cancel");
            button_Table.do_Display_HTML_Row_End();
            
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Delete", "", button_Table, this, "family_Edit_Record_Do_Action_Delete");
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
        
        
        web_Process.setAttribute("family", family);
        web_Process.setAttribute("person", person);
        
        out.close();
        return;
        
    }
    
    
    public void family_Edit_Record_Do_Action_Update(Web_Process web_Process) {
        try {
            Family family = (Family) web_Process.getAttribute("family");
            Person person = (Person) web_Process.getAttribute("person");
            
            boolean validation_Error=false;
            
            try {
                family.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                family.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            
            catch(Database_Record_Exception dbre){
                validation_Error=true;
            }
            web_Process.setAttribute("family", family);
            
            try {
                person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                person.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                validation_Error=true;
            }
            web_Process.setAttribute("person", person);
            
            if(validation_Error){
                family_Edit_Record_Do_Display(web_Process, family, person, null);
                return;
            }
            
            if(!person.update_Outside_User(web_Process))
                return;
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(person);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + person.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            
            try {
                int row_count = web_Process.getDatabase_Front().updateRecord(family);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to update " + family.getEntity_Name() + " to database. ", dbfe);
                return;
            }
            
            do_Pop_and_Status_Routing(web_Process);
            Enty100 enty100 = new Enty100();
            enty100.person_Edit_Record_Do_Update_Family_Info(web_Process);
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void family_Edit_Record_Do_Action_Cancel(Web_Process web_Process) {
        
        Family family = (Family) web_Process.getAttribute("family");
        Person person = (Person) web_Process.getAttribute("person");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + family.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        do_Pop_and_Status_Routing(web_Process);
        return;
    }
    
    
    public void family_Edit_Record_Do_Action_Delete(Web_Process web_Process) {
        
        try {
            Family family = (Family) web_Process.getAttribute("family");
            Person person = (Person) web_Process.getAttribute("person");
            try {
                family.is_Delete_Allowed(web_Process.getDatabase_Front(), web_Process.getMy_Request().getSession());
            }
            
            catch(Database_Record_Exception dbre){
                web_Process.error(dbre);
                return;
            }
            
            try {
                int row_Count = web_Process.getDatabase_Front().deleteRecord(family);
            }
            
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to Delete " + family.getEntity_Name() + " in database. ", dbfe);
                return;
            }
            web_Process.removeAttribute("family");
            do_Pop_and_Status_Routing(web_Process);
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void family_Edit_Record_Do_Action_Maint(Web_Process web_Process) {
        
        try {
            
            Family family = (Family) web_Process.getAttribute("family");
            Person person = (Person) web_Process.getAttribute("person");
            
            try {
                family.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
                family.validate_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                ;
            }
            web_Process.setAttribute("family", family);
            
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
    
    
    public void family_Edit_Record_Do_Action_End_Task(Web_Process web_Process) {
        
        Family family = (Family) web_Process.getAttribute("family");
        Person person = (Person) web_Process.getAttribute("person");
        
        try {
            web_Process.getDatabase_Front().cancel_Record_Lock();
        }
        
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Failed to unlock " + family.getEntity_Name() + " to database. ", dbfe);
            return;
        }
        web_Process.removeAttribute("family");
        web_Process.end();
        return;
    }
    
    
    public void family_Edit_Record_Do_Action_Reload(Web_Process web_Process) {
        
        Family family = (Family) web_Process.getAttribute("family");
        Person person = (Person) web_Process.getAttribute("person");
        family_Edit_Record_Do_Display(web_Process, family, person, null);
        return;
        
    }
    
    public String create_Person_Joining_Where_Clause(Comp comp, Person temp_Person){
        String where_SQL_Clause = "JOIN "+ temp_Person.get_Table_Name() + " USING("+
        temp_Person.getComp_Id().getField_Name()+", "+ temp_Person.getPerson_Number().getField_Name()+") " +
        " WHERE " +
        temp_Person.get_Table_Name() + "." + comp.getComp_Id().toWhere_Clause();
        return where_SQL_Clause;
    }
    
    
    
    
    void family_List_Records_Do_Display(Web_Process web_Process) {
        try {
            web_Process.pushStatus_Wreload("Enty200_family_List_Records", this, "action_Stack_Do_Action",  "family_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        try {
            String form_Name="Enty200_List";
            String focus_Button_Form="Enty200_find";
            
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
            Display_List_Sort_Manager family_Person_Display_List_Sort_Manager;
            
            family_Person_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("family_Person_Display_List_Sort_Manager");
            if(family_Person_Display_List_Sort_Manager==null){
                family_Person_Display_List_Sort_Manager = new Display_List_Sort_Manager("person.");
            }
            
            web_Process.setAttribute("family_Person_Display_List_Sort_Manager", family_Person_Display_List_Sort_Manager);
            String person_Sort_SQL_Clause = family_Person_Display_List_Sort_Manager.getSort_SQL_Clause(search_Person.get_Table_Name());
            
            
            
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
            
            web_Process.do_Display_HTML_Body_Begin(out, "onload=document."+focus_Button_Form+"."+ new Family().getFamily_Number().getField_Name()+".focus();");
            
            
            
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
            
            edit_And_New_Shared_Family_Superior_Display(out, web_Process, false);
            
            
            
            
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
            
            Person family_Person;
            
            if(family_List == null){
                out.println("<tr><td> No Records Returned </td></tr>");
                
            }else{
                for(int row=0; row < family_List.length; row++){
                    Family family = family_List[row];
                    family_Person = Person.get_Possible_Person(web_Process.getDatabase_Front(), family.getComp_Id().getValue(), family.getPerson_Number().getValue());
                    
                    if(first_Family_Person){
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, family.getFamily_Number(),  family_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, family.getPerson_Number(),  family_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, family_Person.getFirst_Name(),  family_Person_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, family_Person.getLast_Name(),  family_Person_Display_List_Sort_Manager);
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th_With_Sort(list_Table, family_Person.getCity(),  family_Person_Display_List_Sort_Manager);
                        list_Table.do_Display_HTML_Row_End();
                        first_Family_Person=false;
                    }
                    
                    
                    list_Table.do_Display_HTML_Row_Begin_Std();
                    try {
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, family.getFamily_Number(), "Select", comp, this, "family_List_Records_Do_Action_Select");
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(list_Table, family.getPerson_Number(), "Select_Person", comp, new Person().getClass(), "personP_Select_Action");
                    }catch(WPFatal_Exception wpfe){
                        web_Process.error_End("programming error", wpfe);
                        return;
                    }
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, family_Person.getFirst_Name());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, family_Person.getLast_Name());
                    web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, family_Person.getCity());
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
                        
                        address_List_Table.do_Display_HTML_Row_Begin_Std();
                        try {
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(address_List_Table, family.getFamily_Number(), "Select", comp, this, "family_List_Records_Do_Action_Select");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(address_List_Table, person_Additional_Address.getPerson_Number(), "Select_Person", comp, person.getClass(), "personP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(address_List_Table, person_Additional_Address.getPerson_Additional_Address_Number(), "Address_Select", person, person_Additional_Address.getClass(),"person_Additional_AddressP_Select_Action");
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End("programming error", wpfe);
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
                
                
                String phone_Where_SQL_Clause =
                " JOIN " + search_Person.get_Table_Name() + " USING("+
                search_Person.getComp_Id().getField_Name()+", "+ search_Person.getPerson_Number().getField_Name()+") " +
                " JOIN "+ search_Family.get_Table_Name() + 
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
                        
                        
                        phone_List_Table.do_Display_HTML_Row_Begin_Std();
                        try {
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(phone_List_Table, family.getFamily_Number(), "Select", comp, this, "family_List_Records_Do_Action_Select");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(phone_List_Table, person_Additional_Phone.getPhone(), "Phone_Select", person, person_Additional_Phone.getClass(), "person_Additional_PhoneP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table(phone_List_Table, person_Additional_Phone.getPhone_Type());
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(phone_List_Table, person_Additional_Phone.getPerson_Number(), "Select_Person", comp, person.getClass(), "personP_Select_Action");
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End("programming error", wpfe);
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
                        
                        
                        
                        e_Mail_List_Table.do_Display_HTML_Row_Begin_Std();
                        
                        try {
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(e_Mail_List_Table, family.getFamily_Number(), "Select", comp, this, "family_List_Records_Do_Action_Select");
                            
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(e_Mail_List_Table, person_Additional_E_Mail.getPerson_Additional_E_Mail_Number(), "E_Mail_Select", person, person_Additional_E_Mail.getClass(), "person_Additional_E_MailP_Select_Action");
                            web_Process.do_Display_HTML_DF_Display_In_Table_As_Link_With_DBR(e_Mail_List_Table, person_Additional_E_Mail.getPerson_Number(), "Select_Person", comp, person.getClass(), "personP_Select_Action");
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End("programming error", wpfe);
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
            
            Family family = new Family();
            
            
            try {
                web_Process.do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, focus_Button_Form, "action", "Find", "Find", "", family.getFamily_Number(), 15, comp, this, "family_List_Records_Do_Action_Find");
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty200_new", "action", "New", "New", "", this, "family_List_Records_Do_Action_New");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty200_end", "action", "End", "End", "", this, "std_Action_End");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty200_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                
                
                
                
                
                web_Process.do_Display_HTML_Form_Begin_Std(out, "Enty200_Search");
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
                
                
            }catch(WPException wpe){
                web_Process.error_End("Failed to make buttons.", wpe);
                return;
            }catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make buttons.", wpfe);
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
            Family temp_Family=new Family();
            
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
            
            Person person = new Person(family.getComp_Id().getValue(), family.getPerson_Number().getValue());
            
            
            try{
                person=(Person)web_Process.getDatabase_Front().getRecord(person);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Person
                web_Process.error("No Such Person.  Person # :"+ family.getPerson_Number().toString() + " deleted but not Family", null);
                return;
            }
            
            // found a Person
            
            
            
            family_View_Record_Do_Display(web_Process, family, person);
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void family_List_Records_Do_Action_Select(Web_Process web_Process) {
        
        
        try {
            Family temp_Family=new Family();
            
            
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
            
            
            Person person = new Person(family.getComp_Id().getValue(), family.getPerson_Number().getValue());
            
            
            try{
                person=(Person)web_Process.getDatabase_Front().getRecord(person);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Failure.", dbfe);
                return;
            }
            catch(Database_Front_No_Results_Exception dbfe){
                // no such Person
                web_Process.error("No Such Person.  Person # :"+ family.getPerson_Number().toString() + " deleted but not Family", null);
                return;
            }
            
            // found a Person
            
            
            
            family_View_Record_Do_Display(web_Process, family, person);
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void family_List_Records_Do_Action_New(Web_Process web_Process) {
        
        select_New_Type_Do_Display(web_Process, null);
        return;
    }
    
    
    public void family_List_Records_Do_Action_Search(Web_Process web_Process) {
        
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
            Display_List_Sort_Manager family_Person_Display_List_Sort_Manager;
            family_Person_Display_List_Sort_Manager = (Display_List_Sort_Manager) web_Process.getAttribute("family_Person_Display_List_Sort_Manager");
            if(family_Person_Display_List_Sort_Manager!= null){
                web_Process.do_Process_Of_Sort_Manger_Request(family_Person_Display_List_Sort_Manager, person);
                web_Process.setAttribute("family_Person_Display_List_Sort_Manager", family_Person_Display_List_Sort_Manager);
            }
            
            
            family_List_Records_Do_Display(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    void select_New_Type_Do_Display(Web_Process web_Process, Exception ex) {
        
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error(se);
            return;
        }
        
        try {
            web_Process.pushStatus_Wreload("Enty200_Select_New_Type", this, "action_Stack_Do_Action",  "select_New_Type_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            
            
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
            
            
            
            
            
            out.println("<div id='main'>");
            
            Person person = new Person();
            
            
            
            
            try {
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty200_new_person", "action", "Create w/ New Person", "Create w/ New Person", "", this, "select_New_Type_Do_Action_Create_With_New_Person");
                
                out.println("<p><B>OR</B>");
                
                
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty200_existing_person", "action", "Create From Existing Person, Advanced Search", "Create From Existing Person, Advanced Search", "", this, "select_New_Type_Do_Action_Create_From_Existing_Person");
                
                
            }
            catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make buttons.", wpfe);
                return;
            }
            
            
            
            if(ex!=null){
                out.println("<p>Error:"+StringPlus.html_Encode(ex.getMessage()));
            }
            
            out.println("</div>");
            
            
            
            out.println("<div id='sidebar'>");
            
            try {
                //web_Process.do_Display_HTML_DF_Button_Using_Form_With_Hidden_DBR(out, focus_Button_Form, "action", "Find", "Find", "", family.getFamily_Number(), 15, comp);
                
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty200_end", "action", "End", "End", "", this, "std_Action_End");
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty200_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                
            }
            catch(WPFatal_Exception wpfe){
                web_Process.error_End("Failed to make buttons.", wpfe);
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
    
    
    public void select_New_Type_Do_Action_Create_With_New_Person(Web_Process web_Process) {
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            Family temp_Family=new Family();
            
            Person temp_Person=new Person();
            
            web_Process.setAttribute("create_New_Person", new Boolean(true));
            family_New_Record_Do_Display(web_Process, temp_Family, temp_Person, null);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    public void select_New_Type_Do_Action_Create_From_Existing_Person(Web_Process web_Process) {
        try {
            Comp comp = (Comp) web_Process.getAttribute("comp");
            Family temp_Family=new Family();
            
            web_Process.setAttribute("create_New_Person", new Boolean(false));
            
            get_Person_Do_Action_Start(web_Process);
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    
    public void select_New_Type_Do_Action_Reload(Web_Process web_Process) {
        
        do_Pop_and_Status_Routing(web_Process);
        return;
        
    }
    
    
    
    
    
    
    public void edit_And_New_Shared_Family_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
        
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
    
    
    
    
    
    
    
    public void edit_And_New_Shared_Family_Main_Display(PrintWriter out, Web_Process web_Process, Family family, Person person, boolean is_New_Family, Exception ex) {
        
        edit_And_New_Shared_Family_Superior_Display(out, web_Process, true);
        web_Process.page_Button_Links_Do_Display(Enty200_Page_Names_Edit, out);
        
        
        {
            //Main Contact
            
            out.println("<div style='position: absolute; top: 50px;left: 0px; right: 0px' id='page0' >");
            
            
            try {
                
                {
                    HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table0", out, "Record_Edit_Table");
                    
                    record_Table.do_Display_HTML_Table_Begin_Std();
                    record_Table.do_Display_HTML_TBody_Begin_Std();
                    
                    if(is_New_Family){
                        DFBoolean automatic_Family_Number  = (DFBoolean) web_Process.getAttribute("automatic_Family_Number");
                        if(automatic_Family_Number == null){
                            try {
                                automatic_Family_Number = new DFBoolean("automatic_Family_Number", "Automatically Assign #");
                                automatic_Family_Number.setValue(true);
                            }catch(DFException dfe){
                                web_Process.error_End(dfe);
                                return;
                            }
                        }
                        web_Process.setAttribute("automatic_Family_Number", automatic_Family_Number);
                        
                        web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, family.getFamily_Number());
                        web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table, automatic_Family_Number);
                    } else
                        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, family.getFamily_Number());
                    
                    
                    record_Table.do_Display_HTML_TBody_End();
                    record_Table.do_Display_HTML_Table_End();
                    
                }
                
                
                
                {
                    Enty100 enty100 = new Enty100();
                    enty100.person_Edit_And_New_Shared_Main_Display(out, web_Process, person, null,
                    "onblur = \"javascript: " + web_Process.page_Show_Call_For_On_Event(Enty200_Page_Names_Edit, 1) +"; " + web_Process.DF_Focus_Call_For_On_Event(family.getCust_Id()) + "; \"");
                    
                }
                
                
                
                
                if(ex!=null){
                    out.println("<p>Error:"+StringPlus.html_Encode(ex.getMessage()));
                }
                
                
                
            }
            catch(WPException wpe){
                web_Process.error_End("Programming error.", wpe);
                return;
            }
            
            out.println("</div>");
            
        }
        
        
        
        {
            //Extras
            
            out.println("<div style='position: absolute; top: 50px;left: 0px; right: 0px' id='page1' >");
            
            
            try {
                
                
                
                {
                    HTML_Table_Tag record_Table = new HTML_Table_Tag("record_Table0", out, "Record_View_Table");
                    
                    record_Table.do_Display_HTML_Table_Begin_Std();
                    record_Table.do_Display_HTML_TBody_Begin_Std();
                    
                    if(is_New_Family){
                        record_Table.do_Display_HTML_Row_Begin_Std();
                        record_Table.do_Display_HTML_TH_Element(family.getFamily_Number().getField_Title());
                        record_Table.do_Display_HTML_TD_Element("New");
                        record_Table.do_Display_HTML_Row_End();
                        
                    } else
                        web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, family.getFamily_Number());
                    
                    web_Process.do_Display_HTML_DF_Display_With_Th_Tr(record_Table, person.getName_Last_Then_First());
                    
                    record_Table.do_Display_HTML_TBody_End();
                    record_Table.do_Display_HTML_Table_End();
                    
                }
                
                
                
                
                
                
                Cust cust = Cust.get_Possible_Cust(web_Process.getDatabase_Front(), family.getComp_Id().getValue(), family.getCust_Id().getValue());
                Person cust_Person = Person.get_Possible_Person(web_Process.getDatabase_Front(), cust.getComp_Id().getValue(), cust.getPerson_Number().getValue());
                
                
                {
                    
                    HTML_Table_Tag record_Table_B = new HTML_Table_Tag("record_Table00", out, "Record_Edit_Table");
                    
                    record_Table_B.do_Display_HTML_Table_Begin_Std();
                    record_Table_B.do_Display_HTML_TBody_Begin_Std();
                    
                    
                    record_Table_B.do_Display_HTML_Row_Begin_Std();
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_Table_B, family.getCust_Id());
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_TD("", record_Table_B, family.getCust_Id(), "onkeyup='cust_Id_DoFind()'");
                    web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(record_Table_B, cust_Person.getName_Last_Then_First());
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_TD("cust_",record_Table_B, cust_Person.getName_Last_Then_First(), "onkeyup='cust_Id_DoCompletion()'");
                    record_Table_B.do_Display_HTML_Row_End();
                    
                    record_Table_B.do_Display_HTML_TBody_End();
                    record_Table_B.do_Display_HTML_Table_End();
                                    
                    Cust_AutoCompletion.getDivTable_Static(out, "cust_Id");

                }

                
                {
                    
                    HTML_Table_Tag record_Table_C = new HTML_Table_Tag("record_TableC", out, "Record_Edit_Table");
                    
                    record_Table_C.do_Display_HTML_Table_Begin_Std();
                    record_Table_C.do_Display_HTML_TBody_Begin_Std();
                    
                    web_Process.do_Display_HTML_DF_Input_In_Form_With_Th_Tr(record_Table_C, family.getNotes());
                    
                    
                    
                    record_Table_C.do_Display_HTML_TBody_End();
                    record_Table_C.do_Display_HTML_Table_End();
                    
                }
                
                
                if(ex!=null){
                    out.println("<p>Error:"+StringPlus.html_Encode(ex.getMessage()));
                }
                
                
                
            }
            catch(WPException wpe){
                web_Process.error_End("Programming error.", wpe);
                return;
            }
            
            out.println("</div>");
            
        }
        
        
        
        
        
    }
    
    
    
}





