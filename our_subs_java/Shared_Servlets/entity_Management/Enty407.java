/*
 * Enty407.java
 *
 * Created on August 1, 2007, 6:36 AM
 */

package entity_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.security_Management.*;
import com.amlibtech.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import customer_Management.data.*;
import enroll_Now_Office.data.*;
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

public class Enty407 extends WPHttpServlet_Base {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Create and email passwords for active customers";
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
                confirm_Begin_Do_Display(web_Process);
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
            confirm_Begin_Do_Display(web_Process);
            return;
        }else if(obj instanceof String ){
            //sub program did not return anything, error out or ended
            do_Pop_and_Status_Routing(web_Process);
            return;
        }
    }
    
    
    
    public void confirm_Begin_Do_Display(Web_Process web_Process){
        
        try {
            web_Process.pushStatus_Wreload("Enty407_cust_List_Records", this, "action_Stack_Do_Action",  "confirm_Begin_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            String form_Name="Enty407_List";
            String focus_Button_Form="Enty407_find";
            
            Comp comp = (Comp)web_Process.getAttribute("comp");
            if(comp== null){
                web_Process.error_End("Can't find comp attribute.", null);
                return;
            }
            
            
            Cust temp_Cust= new Cust();
            try {
                temp_Cust.setComp_Id(comp.getComp_Id().getValue());
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
            web_Process.DF_Focus_Call_For_On_Event(focus_Button_Form, temp_Cust.getCust_Id()) + "; " +
            "\" ");
            
            
            
            out.println("<div id='above_Header'>");
            out.println("</div>");
            
            out.println("<div id='header'>");
            out.println("<div id='header_Text'>");
            out.println(web_Process.getProgram().getProgram_Title().toHTML());
            out.println("</div>");
            out.println("</div>");
            
            
            
            
            
            out.println("<div id='main'>");
            
            
            
            
            
            cust_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            out.println("<p>This program will assign passwords to non-obsolete customers with e-mail addresses and any registration orders in any period.");
            
            
            
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            
            HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
            
            button_Table.do_Display_HTML_Table_Begin_Std();
            button_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            try {
                
                
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                button_Table.do_Display_HTML_TD_Begin();
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty407_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                button_Table.do_Display_HTML_TD_End();
                button_Table.do_Display_HTML_Row_End();
                
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                button_Table.do_Display_HTML_TD_Begin();
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty407_end_task", "action", "Begin", "Begin", "", this, "confirm_Begin_Do_Action_Begin");
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
            
            //        web_Process.setAttribute("cust", cust);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    
    public void confirm_Begin_Do_Action_Begin(Web_Process web_Process) {
        
        web_Process.popStatus_Node();
        cust_Scanning_Do_Display(web_Process);
        
    }
    public void confirm_Begin_Do_Action_Reload(Web_Process web_Process) {
        
        confirm_Begin_Do_Display(web_Process);
        
    }
    
    
    
    public void cust_Edit_And_New_Shared_Superior_Display(PrintWriter out, Web_Process web_Process, boolean with_Hidden_Input) {
        
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
    
    
    
    public void cust_Scanning_Do_Display(Web_Process web_Process) {
        
        try {
            web_Process.pushStatus_Wreload("Enty407_cust_List_Records", this, "action_Stack_Do_Action",  "cust_Scanning_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        try {
            String form_Name="Enty407_Cust_Scanning";
            
            Comp comp = (Comp)web_Process.getAttribute("comp");
            if(comp== null){
                web_Process.error_End("Can't find comp attribute.", null);
                return;
            }
            
            
            
            
            
            
            web_Process.getMy_Response().setContentType("text/html");
            web_Process.getMy_Response().setHeader("Cache-Control", "no-cache");
            
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
            
            
            int count=0;
            
            
            
            cust_Edit_And_New_Shared_Superior_Display(out, web_Process, false);
            
            
            Cust temp_Cust= new Cust();
            try {
                temp_Cust.setComp_Id(comp.getComp_Id().getValue());
            }catch(DFException dfe){
                web_Process.error_End(dfe);
                return;
            }
            
            
            String  cust_Where_SQL_Clause ="WHERE " +
            temp_Cust.getComp_Id().toWhere_Clause();
            
            
            Cust[] custs;
            int cust_Count=0;
            try {
                custs = (Cust[]) web_Process.getDatabase_Front().getMany_Records(temp_Cust, cust_Where_SQL_Clause, cust_Count, 10);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Database Error", dbfe);
                return;
            }
            
            while(custs != null && count<20){
                for(int i=0;i<custs.length && count<20;i++){
                    cust_Count++;
                    Cust cust = custs[i];
                    if(cust.getKind_Of_Customer().getValue().equals(Cust.KINDS_OF_CUSTOMERS[Cust.KINDS_OF_CUSTOMERS_INDEX_PERSON])){
                        Person person = cust.get_Aka_Person(web_Process.getDatabase_Front());
                        
                        
                        Outside_User outside_User;
                        boolean valid_Email=true;
                        try {
                            outside_User = person.get_Aka_Outside_User(web_Process);
                        }catch(WPFatal_Exception wpfe){
                            // no good user id
                            valid_Email=false;
                            outside_User=null;
                        }
                        
                        if(outside_User!=null)
                            continue;
                        if(!valid_Email)
                            continue;
                        
                        Reg_Ord_Hd temp_Reg_Ord_Hd = new Reg_Ord_Hd();
                        
                        try {
                            temp_Reg_Ord_Hd.setComp_Id(cust.getComp_Id().getValue());
                            temp_Reg_Ord_Hd.setCust_Id(cust.getCust_Id().getValue());
                        }catch(DFException dfe){
                            web_Process.error_End(dfe);
                            return;
                        }
                        
                        String reg_Ord_Hd_Where_Clause = " WHERE " +
                        temp_Reg_Ord_Hd.getComp_Id().toWhere_Clause() +
                        " AND " + temp_Reg_Ord_Hd.getCust_Id().toWhere_Clause();
                        
                        Reg_Ord_Hd[] reg_Ord_Hds;
                        try {
                            reg_Ord_Hds = (Reg_Ord_Hd[]) web_Process.getDatabase_Front().getMany_Records(temp_Reg_Ord_Hd, reg_Ord_Hd_Where_Clause, 0, 1);
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End("Database Error", dbfe);
                            return;
                        }
                        
                        if(reg_Ord_Hds ==null)
                            continue;
                        out.println("<p> cust#:"+cust.getCust_Id().getValue()+"  name:"+person.getName_First_Then_Last()+"  needs password.");
                        
                        
                        
                        outside_User = new Outside_User(person);;
                        
                        
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
                        
                        if(outside_Users != null)
                            continue;
                        
                        // create password
                        try {
                            outside_User.setPassword(create_Password());
                        }catch(DFException dfe){
                            web_Process.error_End(dfe);
                            return;
                        }
                        out.println("<p>password:"+outside_User.getPassword().getValue());
                        out.flush();
                        
                        
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
                        
                        
                        
            try {
                int row_Count = web_Process.getDatabase_Front().addRecord(outside_User);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End("Failed to add " + outside_User.getEntity_Name() + " to database. ", dbfe);
                return;
            }
                        
                        try {
                            Thread.sleep(2000);
                        }catch(InterruptedException ie){
                            web_Process.error_End(ie);
                            return;
                        }
                        count++;
                        
                    }
                    
                }
                
                try {
                    custs = (Cust[]) web_Process.getDatabase_Front().getMany_Records(temp_Cust, cust_Where_SQL_Clause, cust_Count, 10);
                }catch(Database_Front_Exception dbfe){
                    web_Process.error_End("Database Error", dbfe);
                    return;
                }
            }
            
            
            
            
            
            
            out.println("</div>");
            
            out.println("<div id='sidebar'>");
            
            
            HTML_Table_Tag button_Table = new HTML_Table_Tag("button_Table", out);
            
            button_Table.do_Display_HTML_Table_Begin_Std();
            button_Table.do_Display_HTML_TBody_Begin_Std();
            
            
            try {
                
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                button_Table.do_Display_HTML_TD_Begin();
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty407_end_task", "action", "End Task", "End Task", "", this, "std_Action_End_Task");
                button_Table.do_Display_HTML_TD_End();
                button_Table.do_Display_HTML_Row_End();
                
                
                button_Table.do_Display_HTML_Row_Begin_Std();
                button_Table.do_Display_HTML_TD_Begin();
                web_Process.do_Display_HTML_Action_Button_Using_Form(out, "Enty407_continue", "action", "Continue", "Continue", "", this, "cust_Scanning_Do_Action_Continue");
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
            
            //        web_Process.setAttribute("cust", cust);
            
            out.close();
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
    }
    
    
    public void cust_Scanning_Do_Action_Continue(Web_Process web_Process) {
        
        cust_Scanning_Do_Display(web_Process);
        return;
        
    }
    
    
    
    public void cust_Scanning_Do_Action_Reload(Web_Process web_Process) {
        
        cust_Scanning_Do_Display(web_Process);
        return;
        
    }
    
    public String create_Password(){
        String char_Set ="bcdfghjkmnpqrstvwxyzBCDFGHJKMNPQRSTVWXYZ";
        String number_Set = "23456789";
        
        Random random = new Random();
        String password="";
        
        for(int l=0;l<9;l++){
            int num_Or_Char = random.nextInt(4);
            if(num_Or_Char==0){
                // use number
                int index = random.nextInt(number_Set.length());
                password += number_Set.substring(index, index+1);
            }else{
                //use char
                int index = random.nextInt(char_Set.length());
                password += char_Set.substring(index, index+1);
            }
        }
        return password;
    }
    
    
}
