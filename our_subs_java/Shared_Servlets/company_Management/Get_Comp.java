/*
 * Get_Comp.java
 *
 * Created on July 23, 2006, 11:32 AM
 */

package company_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
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
public class Get_Comp extends WPHttpServlet_Base {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Get a Company";
    }
    
    
    
    public void do_Status_Routing(Web_Process web_Process, String action) {
        try {
            if(web_Process.getStatus_Node().getStatus_Text().equals("loading")){
                
                web_Process.resetStatus_Stack(new WPStatus_Node("exit"));
                if(action.equals("Start")){
                    
                    String title = web_Process.getMy_Request().getParameter("title");
                    if(title==null){
                        title="Please Select a Company.";
                    }
                    web_Process.setAttribute("title", title);
                    
                    String return_As = web_Process.getMy_Request().getParameter("return_As");
                    if(return_As==null){
                        web_Process.error_End("Get_Comp must be called with a 'return_As' parameter.", null);
                        return;
                    }
                    web_Process.setAttribute("return_As", return_As);
                    
                    String no_User_Master_File = web_Process.getMy_Request().getParameter("no_User_Master_File");
                    if(no_User_Master_File!= null){
                        web_Process.setAttribute("no_User_Master_File", Boolean.TRUE);
                        comp_List_Records_Do_Display(web_Process);
                    }else{
                        web_Process.setAttribute("no_User_Master_File", Boolean.FALSE);
                        
                        // is this a user?
                        if(web_Process.getSubscriber_Organization_Portal().getLogin_Method().toString().equals("User")){
                            
                            // is company in User_Master file
                            
                            Comp comp;
                            comp = new Comp();
                            
                            User_Master_File user_Master_File;
                            try {
                                user_Master_File =(User_Master_File)web_Process.getDatabase_Front().getRecord(new User_Master_File(web_Process.getUser().getUser_Id().toString(), comp.get_Table_Name()));
                            }
                            catch(Database_Front_Exception dbfe){
                                web_Process.error_End("Database Error.", dbfe);
                                return;
                            }catch(Database_Front_No_Results_Exception dbfe){
                                //Not found in user_Master_File
                                comp_List_Records_Do_Display(web_Process);
                                return;
                            }
                            
                            try {
                                comp = (Comp) web_Process.getDatabase_Front().getRecord(new Comp(user_Master_File.getKey_Value().toString()));
                            }catch(Database_Front_Exception dbfe){
                                web_Process.error_End("Database Error.", dbfe);
                                return;
                            }catch(Database_Front_No_Results_Exception dbfe){
                                comp_List_Records_Do_Display(web_Process);
                                return;
                            }
                            
                            // found a Company;
                            Web_Process parent_Web_Process;
                            try {
                                parent_Web_Process = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), web_Process.getParent_Web_Process_Id());
                            }
                            catch(WPException wpe){
                                web_Process.error_End("Unable to load parent web process.", wpe);
                                return;
                            }
                            
                            return_As = (String) web_Process.getAttribute("return_As");
                            parent_Web_Process.setAttribute(return_As, comp);
                            web_Process.end();
                            return;
                            
                            
                            
                        }else{
                            
                            String  comp_Id = web_Process.getSubscriber_Organization_Portal().getCustomer_Or_Vendor_Comp_Id().toString();
                            Comp comp;
                            try {
                                comp = (Comp) web_Process.getDatabase_Front().getRecord(new Comp(comp_Id));
                            }catch(Database_Front_Exception dbfe){
                                web_Process.error_End("Database Error.", dbfe);
                                return;
                            }catch(Database_Front_No_Results_Exception dbfe){
                                comp_List_Records_Do_Display(web_Process);
                                return;
                            }
                            // found a Company;
                            Web_Process parent_Web_Process;
                            try {
                                parent_Web_Process = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), web_Process.getParent_Web_Process_Id());
                            }
                            catch(WPException wpe){
                                web_Process.error_End("Unable to load parent web process.", wpe);
                                return;
                            }
                            
                            return_As = (String) web_Process.getAttribute("return_As");
                            parent_Web_Process.setAttribute(return_As, comp);
                            web_Process.end();
                            return;
                        }
                    }
                    
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
                }catch(WPFatal_Exception wpe){
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
        return "Get Company";
    }    
    
     
    
    
    
    
    public void comp_List_Records_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("comp_List_Records", this, "comp_List_Records_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        try {
            
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
            
            
            
            
            String form_Name="Get_Comp_List";
            
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
                    
                    try{
                        list_Table.do_Display_HTML_Row_Begin_Std();
                        web_Process.do_Display_HTML_DF_Display_In_Table_As_Link(list_Table, comp.getComp_Id(), "Select", this, "comp_List_Records_Do_Action_Select");
                        web_Process.do_Display_HTML_DF_Display_In_Table(list_Table, comp.getName());
                        list_Table.do_Display_HTML_Row_End();
                    }
                    catch(WPFatal_Exception wpe){
                        web_Process.error_End(wpe);
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
            
            
            try {
                
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Get_Comp_end", "action", "End", "End", "", this, "std_Action_End");
                
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
            Web_Process parent_Web_Process;
            try {
                parent_Web_Process = Web_Process.get_Instance_From_Table(this, web_Process.getMy_Request(), web_Process.getMy_Response(), web_Process.getParent_Web_Process_Id());
            }
            catch(WPException wpe){
                web_Process.error_End("Unable to load parent web process.", wpe);
                return;
            }
            
            String return_As = (String) web_Process.getAttribute("return_As");
            parent_Web_Process.setAttribute(return_As, comp);
            
            Boolean no_User_Master_File = (Boolean) web_Process.getAttribute("no_User_Master_File");
            
            if(no_User_Master_File!=null && no_User_Master_File.equals(Boolean.FALSE)){
                
                User_Master_File user_Master_File;
                user_Master_File = new User_Master_File(web_Process.getUser().getUser_Id().toString(), comp.get_Table_Name());
                
                try {
                    user_Master_File.setKey_Value(comp.getComp_Id().toString());
                }catch(DFException dfe){
                    web_Process.error_End("Unable to set User_Master_File key_Value for Comp.", dfe);
                    return;
                }
                
                try {
                    web_Process.getDatabase_Front().addRecord(user_Master_File);
                }catch(Database_Front_Exception dbfe){
                    try {
                        web_Process.getDatabase_Front().updateRecord(user_Master_File);
                    }catch(Database_Front_Exception dbfe2){
                        web_Process.error_End("Unable to add or update User_Master_File.", dbfe2);
                        return;
                    }
                }
                
            }
            
            
            
            web_Process.end();
            return;
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void comp_List_Records_Do_Action_End_Task(Web_Process web_Process) {
        web_Process.end();
        return;
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void run(Web_Process web_Process, HttpServlet httpServlet, String success_Method) {
        run(web_Process, httpServlet.getClass(), success_Method);
    }
    
    public void run(Web_Process web_Process, Class passed_Class_Obj, String success_Method) {
        
        
            try {
                web_Process.pushStatus_Wreload("get_Comp", this,  "get_Comp_Do_Action_Reload");
                web_Process.curStatus_Add_Action_Node(new WPAction_Node("Start", this, "get_Comp_Do_Action_Start"));
                web_Process.curStatus_Add_Action_Node(new WPAction_Node("Success", passed_Class_Obj, success_Method));

            }catch(WPFatal_Exception wpfe){
                web_Process.error_End(wpfe);
                return;
            }
            
            get_Comp_Do_Action_Start(web_Process);
    }
    
    
    
    
    void get_Comp_Do_Display(Web_Process web_Process) {
        
        
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
                
                try{
                    web_Process.curStatus_Find_And_Invoke_Action(web_Process, "Success");
                }catch(WPFatal_Exception wpe){
                    web_Process.error_End(wpe);
                    return;
                }
                
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

            try {
                web_Process.curStatus_Find_And_Invoke_Action(web_Process, "Success");
            }catch(WPFatal_Exception wpe){
                web_Process.error_End(wpe);
                return;
            }
            
            return;
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
    }
    
    
    
}
