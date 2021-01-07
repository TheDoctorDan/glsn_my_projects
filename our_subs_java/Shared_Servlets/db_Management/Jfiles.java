/*
 * Jfiles.java
 *
 * Created on May 14, 2006, 6:29 PM
 */

package db_Management;

import com.amlibtech.data_fields.*;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.amlibtech.database.*;
import com.amlibtech.login.data.*;
import com.amlibtech.web_Processes.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import com.amlibtech.security_Management.*;
import com.amlibtech.util.*;



/**
 *
 * @author  dgleeson
 * @version
 */
public class Jfiles extends WPHttpServlet_Base {
    
    
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Jfiles is a servlet to check on database tables used by this portal vs. database_Record classed used by this portal.";
    }
    
    
    
    
    public void do_Status_Routing(Web_Process web_Process, String action) {
        
        
        
        if(web_Process.getStatus_Node().getStatus_Text().equals("loading")){
            
            
            web_Process.resetStatus_Stack(new WPStatus_Node("exit"));
            
            if(action.equals("Start")){
                try {
                    Security_Module.check_Transaction(web_Process);
                }
                catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                table_Modification_Init(web_Process);
                return;
                
            }else if(action.equals("Reload")){
                try {
                    Security_Module.check_Transaction(web_Process);
                }
                catch(Security_Exception se){
                    web_Process.error_End(se);
                    return;
                }
                table_Modification_Init(web_Process);
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
        return "Check Database Table Versions";
    }    
    
     
    
    public void table_Modification_Init(Web_Process web_Process) {
        boolean debug=false;
        
        
        try {
            Security_Module.check_Transaction(web_Process);
        }
        catch(Security_Exception se){
            web_Process.error_End(se);
            return;
        }
        
        
        
        
        
        // get database_Front
        Database_Front database_Front = web_Process.getDatabase_Front();
        
        
        // get a list of tables in the sql database
        String[] sql_Table_List;
        try{
            sql_Table_List = database_Front.get_Table_List();
        }
        catch(Database_Front_Exception dbfe){
            web_Process.error_End(dbfe);
            return;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            sql_Table_List = new String[0];
        }
        
        if(debug){
            for(int i=0;i<sql_Table_List.length;i++){
                System.out.println(sql_Table_List[i]);
            }
        }
        
        
        LinkedList sql_Alter_List = new LinkedList();
        LinkedList warning_List = new LinkedList();
        
        
        
        
        // find all installed subsystems and process them
        String portal_Subsystem_Where ="WHERE " +
        web_Process.getSubscriber_Organization_Portal().getSubscriber_Organization_Id().toWhere_Clause() + " AND " +
        web_Process.getSubscriber_Organization_Portal().getPortal_Name().toWhere_Clause();

//        subscriber_Organization_Id ='" + web_Process.getSubscriber_Organization_Portal().getSubscriber_Organization_Id() +
//        "'  AND portal_Name = '" + web_Process.getSubscriber_Organization_Portal().getPortal_Name() + "'";
        
        Subscriber_Organization_Portal_Subsystem[] subscriber_Organization_Portal_Subsystems;
        int subscriber_Organization_Portal_Subsystem_Count=0;
        
        try{
            subscriber_Organization_Portal_Subsystems = (Subscriber_Organization_Portal_Subsystem[])database_Front.getMany_Records(new Subscriber_Organization_Portal_Subsystem(),
            portal_Subsystem_Where, subscriber_Organization_Portal_Subsystem_Count, 10);
        }
        catch(Database_Front_Exception dbfe){
            web_Process.error_End("Error on read of subscriber_Organization_Portal_Subsystems file.", dbfe);
            return;
        }
        catch(Database_Record_Constructor_Exception dbre){
            web_Process.error_End("Error on read of subscriber_Organization_Portal_Subsystems file.", dbre);
            return;
        }
        
        while(subscriber_Organization_Portal_Subsystems!=null){
            for(int i = 0; i< subscriber_Organization_Portal_Subsystems.length;i++){
                subscriber_Organization_Portal_Subsystem_Count++;
                
                
                if(!process_A_Subsystem(sql_Alter_List, warning_List,  subscriber_Organization_Portal_Subsystems[i], web_Process, sql_Table_List))
                    return;
                
            }
            
            try{
                subscriber_Organization_Portal_Subsystems = (Subscriber_Organization_Portal_Subsystem[])database_Front.getMany_Records(new Subscriber_Organization_Portal_Subsystem(),
                portal_Subsystem_Where, subscriber_Organization_Portal_Subsystem_Count, 10);
            }
            catch(Database_Front_Exception dbfe){
                web_Process.error_End("Error on read of subscriber_Organization_Portal_Subsystems file.", dbfe);
                return;
            }catch(Database_Record_Constructor_Exception dbre){
                web_Process.error_End("Error on read of subscriber_Organization_Portal_Subsystems file.", dbre);
                return;
            }
        }
        
        for(int ix=0; ix<sql_Table_List.length;ix++){
            if(!sql_Table_List[ix].equals("")){
                //extra table
                //so we need to drop it.
                Jfiles_Work jfiles_Work;
                try {
                    jfiles_Work = new Jfiles_Work(Jfiles_Work.MISC_SQL_COMMAND_TYPE, sql_Table_List[ix], "Drop Table", "Drop Table " + sql_Table_List[ix] + ";");
                    sql_Alter_List.add(jfiles_Work);
                } catch(Database_Record_Constructor_Exception dbrce){
                    web_Process.error_End("Error adding drop table misc command.", dbrce);
                    return;
                }
            }
        }
        
        web_Process.setAttribute("sql_Alter_List", sql_Alter_List);
        web_Process.setAttribute("warning_List", warning_List);
        
        table_Modifications_Do_Display(web_Process);
        
        return;
        
        
        
        
    }
    
    
    
    public  boolean ck_Begin(LinkedList sql_Alter_List, Web_Process web_Process, Database_Record_Base database_Record_Base) {
        
        Field[] my_Fields;
        
        my_Fields = database_Record_Base.getData_Field_List();
        
        // sql table has ...
        DFBase[] sql_Table_Column_List;
        try{
            sql_Table_Column_List = web_Process.getDatabase_Front().get_Table_Column_List(database_Record_Base.get_Table_Name());
        }
        catch(SQLException sqle){
            //No Such table
            //so we need to create it.
            Jfiles_Work jfiles_Work;
            try {
                jfiles_Work = new Jfiles_Work(Jfiles_Work.MISC_SQL_COMMAND_TYPE, database_Record_Base.get_Table_Name(), "Create Table", database_Record_Base.getSQL_Create_Table());
            }catch(Database_Record_Constructor_Exception dbrce){
                web_Process.error_End(dbrce);
                return false;
            }
            sql_Alter_List.add(jfiles_Work);
            
            return true;
        }
        catch(DFException dfe){
            // problem getting table column list
            web_Process.error_End("get_Table_Column_List failed.", dfe);
            return false;
        }
        catch(Database_Front_Exception dbfe){
            // problem getting table column list
            web_Process.error_End("get_Table_Column_List failed.", dbfe);
            return false;
        }
        
        
        
        
        
        // programmer has entered ...
        DFBase[] programmer_Column_List;
        programmer_Column_List = database_Record_Base.get_Field_List();
        
        
        
        
        
        // database_Record has ...
        String[] record_Column_Names = new String[my_Fields.length];
        for(int i=0;i<my_Fields.length; i++){
            record_Column_Names[i] = my_Fields[i].getName();
        }
        //record_Column_Names = StringPlus.sort_Array(record_Column_Names);
        
        
        
        
        if(record_Column_Names.length < programmer_Column_List.length){
            web_Process.error_End("Database_Record_Validator: ck_Begin() failed because : there are more fields in programmer's Field section :" + programmer_Column_List.length + ": is less than in Record's Field section :" + record_Column_Names.length + ":.  Have the programmer correct this error in " + database_Record_Base.get_Table_Name() + ".get_Column_List().", null);
            return false;
            
        }
        if(record_Column_Names.length > programmer_Column_List.length){
            web_Process.error_End("Database_Record_Validator: ck_Begin() failed because : there are less fields in programmer's Field section :" + programmer_Column_List.length + ": is more than in Record's Field section :" + record_Column_Names.length + ":.  Have the programmer correct this error in " + database_Record_Base.get_Table_Name() + ".get_Column_List().", null);
            return false;
        }
        
        
        if(!ck_sql_vs_programmer(web_Process, sql_Alter_List, database_Record_Base.get_Table_Name(), sql_Table_Column_List, programmer_Column_List))
            return false;
        
        if(!ck_Into_Clause(web_Process, sql_Alter_List, database_Record_Base.get_Table_Name(), database_Record_Base, programmer_Column_List))
            return false;
        
        if(!ck_Values_Clause(web_Process, sql_Alter_List, database_Record_Base.get_Table_Name(), database_Record_Base, programmer_Column_List))
            return false;
        
        return true;
    }
    
    
    
    boolean ck_sql_vs_programmer(Web_Process web_Process, LinkedList sql_Alter_List, String table_Name, DFBase[] sql_Table_Column_List, DFBase[] programmer_Column_List)  {
        DFBase   sql_Temp, programmer_Temp;
        boolean found;
        Jfiles_Work jfiles_Work;
        
        for(int i=0;i<sql_Table_Column_List.length;i++){
            sql_Temp = sql_Table_Column_List[i];
            found=false;
            for(int j=0; j<programmer_Column_List.length && !found; j++){
                programmer_Temp = programmer_Column_List[j];
                if(sql_Temp.getField_Name().equals(programmer_Temp.getField_Name())){
                    found=true;
                    if(!programmer_Temp.same_Field_Layout(sql_Temp)){
                        // alter sql
                        try {
                            jfiles_Work = new Jfiles_Work(Jfiles_Work.ALTER_TO_SQL_TYPE, table_Name, programmer_Temp,  sql_Temp );
                            
                            sql_Alter_List.add(jfiles_Work);
                        }catch(Database_Record_Constructor_Exception dbre){
                            web_Process.error_End("Database_Record_Validator: failed.", dbre);
                            return false;
                        }
                    }
                }
            }
            if(!found){
                // remove from sql
                try {
                    
                    jfiles_Work = new Jfiles_Work(Jfiles_Work.DROP_FROM_SQL_TYPE, table_Name,  null,  sql_Temp);
                    sql_Alter_List.add(jfiles_Work);
                }catch(Database_Record_Constructor_Exception dbre){
                    web_Process.error_End(dbre);
                    return false;
                }
            }
        }
        
        
        for(int j=0; j<programmer_Column_List.length; j++){
            programmer_Temp = programmer_Column_List[j];
            found=false;
            
            for(int i=0;i<sql_Table_Column_List.length && !found;i++){
                sql_Temp = sql_Table_Column_List[i];
                
                if(sql_Temp.getField_Name().equals(programmer_Temp.getField_Name())){
                    found=true;
                    /* already altered in forst loop
                     if(sql_Temp.compare(record_Temp)!=0){
                        // alter sql
                        sql_Alter_List.add("ALTER TABLE `" + table_Name + "` ALTER COLUMN `" + record_Temp.getColumnName() + "` " + record_Temp.get_Sql_Create_Clause());
                    }
                     */
                }
            }
            if(!found){
                // add to sql
                try{
                    jfiles_Work = new Jfiles_Work(Jfiles_Work.ADD_TO_SQL_TYPE, table_Name,  programmer_Temp,  null);
                    
                    sql_Alter_List.add(jfiles_Work);
                }
                catch(Database_Record_Constructor_Exception dbre){
                    web_Process.error_End("Database_Record_Validator: ck_sql_vs_programmer() failed to add column :" + programmer_Temp.getField_Name() + ".", dbre);
                    return false;
                }
            }
        }
        
        return true;
    }
    
    
    
    
    boolean ck_Into_Clause(Web_Process web_Process, LinkedList sql_Alter_List, String table_Name, Database_Record_Base database_Record_Base, DFBase[] programmer_Column_List)  {
        
        // "( x, comp_Id, name, address_Line1, address_Line2, city, state_Abbr, zip_Code, federal_Income_Tax_Id_Num, phone_Number, fax_Number, eight_Hundred_Number )"
        
        StringBuffer add_To_Clause = new StringBuffer();
        StringBuffer remove_From_Clause = new StringBuffer();
        StringBuffer result = new StringBuffer();
        StringBuffer corrected_Text = new StringBuffer();
        Jfiles_Work jfiles_Work;
        
        corrected_Text.append("( ");
        for(int i=0;i<programmer_Column_List.length;i++){
            if(i!=0)
                corrected_Text.append(", ");
            corrected_Text.append(programmer_Column_List[i].getField_Name());
        }
        corrected_Text.append(" )");
        
        String current_Text = database_Record_Base.get_Into_Clause();
        
        String current_Clause = current_Text.trim();
        
        
        
        
        if(current_Clause.startsWith("(")){
            current_Clause = current_Clause.substring(1).trim();
        }else{
            web_Process.error_End("Database_Record_Validator: ck_Into_Clause() failed because : clause does not start with (.  Have the programmer correct this error.", null);
            return false;
        }
        
        if(current_Clause.endsWith(")")){
            current_Clause = current_Clause.substring(0,current_Clause.length()-1).trim();
        }else{
            web_Process.error_End("Database_Record_Validator: ck_Into_Clause() failed because : clause does not end with ).  Have the programmer correct this error.", null);
        }
        
        String[] clause_Fields = current_Clause.split(",");
        boolean found;
        
        for(int i=0;i<clause_Fields.length;i++){
            found=false;
            for(int j=0; j<programmer_Column_List.length && !found; j++){
                if(clause_Fields[i].trim().equals(programmer_Column_List[j].getField_Name())){
                    found=true;
                }
            }
            if(!found){
                remove_From_Clause.append(clause_Fields[i].trim() + " ");
            }
        }
        
        for(int i=0;i<programmer_Column_List.length;i++){
            found=false;
            for(int j=0; j<clause_Fields.length && !found; j++){
                if(clause_Fields[j].trim().equals(programmer_Column_List[i].getField_Name())){
                    found=true;
                }
            }
            if(!found){
                add_To_Clause.append(programmer_Column_List[i].getField_Name() + " ");
            }
        }
        
        if(remove_From_Clause.length()+ add_To_Clause.length() !=0){
            result.append("class :" + table_Name +": get_Into_Clause() : <br>\n");
            if(remove_From_Clause.length() !=0)
                result.append("    needs these fields removed : " + remove_From_Clause.toString() + "<br>\n");
            if(add_To_Clause.length() !=0)
                result.append("    needs these fields added : " + add_To_Clause.toString() + "<br>\n");
            
            try {
                jfiles_Work = new Jfiles_Work(Jfiles_Work.CLAUSE_CORRECTION_INTO_TYPE, table_Name, result.toString(),  "\"" + current_Text + "\"",  "\"" + corrected_Text.toString() + "\"");
                sql_Alter_List.add(jfiles_Work);
            }catch(Database_Record_Constructor_Exception dbre){
                web_Process.error_End(dbre);
                return false;
            }
            
        }
        return true;
        
    }
    
    
    
    
    
    
    
    
    boolean ck_Values_Clause(Web_Process web_Process, LinkedList sql_Alter_List, String table_Name, Database_Record_Base database_Record_Base, DFBase[] programmer_Column_List) {
        
        // "( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        
        StringBuffer add_To_Clause = new StringBuffer();
        StringBuffer remove_From_Clause = new StringBuffer();
        StringBuffer result = new StringBuffer();
        StringBuffer corrected_Text = new StringBuffer();
        Jfiles_Work jfiles_Work;
        
        corrected_Text.append("( ");
        for(int i=0;i<programmer_Column_List.length;i++){
            if(i!=0)
                corrected_Text.append(", ");
            corrected_Text.append("?");
        }
        corrected_Text.append(" )");
        
        
        String current_Text = database_Record_Base.get_Values_Clause();
        
        String current_Clause = current_Text.trim();
        
        
        
        
        if(current_Clause.startsWith("(")){
            current_Clause = current_Clause.substring(1).trim();
        }else{
            web_Process.error_End("Database_Record_Validator: ck_Values_Clause() failed because : clause does not start with (.  Have the programmer correct this error.", null);
            return false;
        }
        
        
        if(current_Clause.endsWith(")")){
            current_Clause = current_Clause.substring(0,current_Clause.length()-1).trim();
        }else{
            web_Process.error_End("Database_Record_Validator: ck_Values_Clause() failed because : clause does not end with ).  Have the programmer correct this error.", null);
            return false;
        }
        
        String[] clause_Fields = current_Clause.split(",");
        boolean found_Bad_Error=false;
        for(int i=0;i<clause_Fields.length;i++){
            if(!clause_Fields[i].trim().equals("?")){
                remove_From_Clause.append(clause_Fields[i] + " ");
                found_Bad_Error=true;
            }
        }
        if(!found_Bad_Error){
            for(int i=0; i<programmer_Column_List.length-clause_Fields.length;i++){
                add_To_Clause.append("? ");
            }
            for(int i=0; i<clause_Fields.length-programmer_Column_List.length;i++){
                remove_From_Clause.append(clause_Fields[i] + " ");
            }
            
        }
        //        boolean found;
        //
        //        for(int i=0;i<clause_Fields.length;i++){
        //            found=false;
        //            for(int j=0; j<programmer_Column_List.length && !found; j++){
        //                if(clause_Fields[i].trim().equals("?")){
        //                    found=true;
        //                }
        //            }
        //            if(!found){
        //                remove_From_Clause.append(clause_Fields[i] + " ");
        //            }
        //        }
        //
        //        for(int i=0;i<programmer_Column_List.length - clause_Fields.length;i++){
        //            add_To_Clause.append("? ");
        //        }
        
        if(remove_From_Clause.length()+ add_To_Clause.length() !=0){
            result.append("class :" + table_Name +": get_Values_Clause() : <br>\n");
            if(remove_From_Clause.length() !=0)
                result.append("    needs these fields removed : " + remove_From_Clause.toString() + "<br>\n");
            if(add_To_Clause.length() !=0)
                result.append("    needs these fields added : " + add_To_Clause.toString() + "<br>\n");
            
            try {
                jfiles_Work = new Jfiles_Work(Jfiles_Work.CLAUSE_CORRECTION_INTO_TYPE, table_Name, result.toString(),  "\"" + current_Text + "\"",  "\"" + corrected_Text.toString() + "\"");
                sql_Alter_List.add(jfiles_Work);
            }catch(Database_Record_Constructor_Exception dbre){
                web_Process.error_End(dbre);
                return false;
            }
        }
        return true;
        
    }
    
    
    boolean process_A_Subsystem(LinkedList sql_Alter_List, LinkedList warning_List,  Subscriber_Organization_Portal_Subsystem subscriber_Organization_Portal_Subsystem, Web_Process web_Process, String[] sql_Table_List) {
        
        
        
        
        //get a list of all database_Record classes used by installed Subsystem
        String subsystem_Id = subscriber_Organization_Portal_Subsystem.getSubsystem_Id().toString();
        
        Subsystem subsystem;
        try {
            subsystem = Subsystem.get_Subsystem(subsystem_Id);
            
            
            String subsystem_Class_Name = subsystem.getPackage_Name() + "." + subsystem.getServlet_Name();
            
            Subsystem_Servlet_Base subsystem_Servlet;
            try {
                subsystem_Servlet = (Subsystem_Servlet_Base)Class.forName(subsystem_Class_Name).newInstance();
            }
            catch (ClassNotFoundException cnfe){
                String message ="Error loading Class for subsystem_Servlet: " + subsystem_Class_Name + ", ClassNotFoundException: " + cnfe.getMessage();
                web_Process.error_End(message, cnfe);
                return false;
            }
            catch (InstantiationException ie){
                String message ="Error loading Class for subsystem_Servlet: " + subsystem_Class_Name + ", InstantiationException: " + ie.getMessage();
                web_Process.error_End(message,ie);
                return false;
            }
            catch (IllegalAccessException iae){
                String message ="Error loading Class for subsystem_Servlet: " + subsystem_Class_Name + ", IllegalAccessException: " + iae.getMessage();
                web_Process.error_End(message,iae);
                return false;
            }
            
            String[] dbr_List = subsystem_Servlet.getDatabase_Record_List();
            if(dbr_List!=null){
                for(int j=0;j<dbr_List.length;j++){
                    String dbr_Name = dbr_List[j];
                    
                    // check database_Record classes for alterations
                    
                    Database_Record_Base db_Record;
                    try {
                        db_Record = (Database_Record_Base)Class.forName(dbr_Name).newInstance();
                        if(!ck_Begin(sql_Alter_List, web_Process, db_Record))
                            return false;
                        for(int ix=0; ix<sql_Table_List.length;ix++){
                            if(sql_Table_List[ix].equals(db_Record.get_Table_Name())){
                                sql_Table_List[ix]="";
                                break;
                            }
                        }
                    }
                    catch (ClassNotFoundException cnfe){
                        String message ="Error loading Class for database_Record: " + dbr_Name + ", ClassNotFoundException: " + cnfe.getMessage();
                        web_Process.error_End(message, cnfe);
                        return false;
                    }catch (InstantiationException ie){
                        String message ="Error loading Class for database_Record: " + dbr_Name + ", InstantiationException: " + ie.getMessage();
                        web_Process.error_End(message, ie);
                        return false;
                    }catch (IllegalAccessException iae){
                        String message ="Error loading Class for database_Record: " + dbr_Name + ", IllegalAccessException: " + iae.getMessage();
                        web_Process.error_End(message, iae);
                        return false;
                    }
                    
                }
            }
        }catch(Database_Record_Constructor_Exception dbre){
            warning_List.add(dbre.getMessage());
        }catch(Database_Front_Exception dbfe){
            warning_List.add(dbfe.getMessage());
        }catch(Database_Front_No_Results_Exception dbfe){
            warning_List.add(dbfe.getMessage());
        }
        return true;
    }
    
    
    public void table_Modifications_Do_Display(Web_Process web_Process) {
        
        
        try {
            web_Process.pushStatus_Wreload("table_Modifications_Do_Display", this, "table_Modifications_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        LinkedList sql_Alter_List = (LinkedList) web_Process.getAttribute("sql_Alter_List");
        if(sql_Alter_List==null){
            web_Process.error_End("No sql_Alter_list Attribute.", null);
            return;
        }
        
        LinkedList warning_List = (LinkedList) web_Process.getAttribute("warning_List");
        if(warning_List==null){
            web_Process.error_End("No warning_List Attribute.", null);
            return;
        }
        
        
        String web_Root = web_Process.getSubscriber_Organization_Portal().getWeb_Root().toString();
        
        String css_Root = web_Process.getSubscriber_Organization_Portal().getCss_Root().toString();
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, "Jfiles sql and class adjustments");
        web_Process.do_Display_HTML_Head_End(out);
        
        
        web_Process.do_Display_HTML_Body_Begin_Plain(out);
        
        out.println("<div id='above_Header'>");
        out.println("</div>");
        
        out.println("<div id='header'>");
        out.println("<div id='header_Text'>");
        out.println("Jfiles sql and class adjustments");
        out.println("</div>");
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        try {
            web_Process.do_Display_HTML_Action_Button_Using_Form(out, "jfiles_end",  "action", "End Task", "End Task", "", this, "std_Action_End_Task");
            web_Process.do_Display_HTML_Action_Button_Using_Form(out, "jfiles_end",  "action", "Rename Mode", "Rename Mode", "", this, "table_Modifications_Do_Action_Rename_Mode");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("failed to make button", wpfe);
            return;
        }
        
        out.println("</div>");
        
        out.println("<div id='main'>");
        
        for(int i=0; i<warning_List.size(); i++){
            out.println("<p>Warning :"+ warning_List.get(i));
        }
        if(sql_Alter_List.size()==0){
            out.println("<p> No corrections found.");
        }else{
            out.println("<p> USE " + web_Process.getSubscriber_Organization().getDatabase_Name() + ";");
            
            
            for(int i=0; i<sql_Alter_List.size();i++){
                Jfiles_Work jfiles_Work = (Jfiles_Work)sql_Alter_List.get(i);
                try {
                    if(jfiles_Work.getType() == Jfiles_Work.CLAUSE_CORRECTION_INTO_TYPE){
                        out.println("<p>item#:"+i+" code "+ jfiles_Work.get_Code_Correction());
                    }else if(jfiles_Work.getType() == Jfiles_Work.MISC_SQL_COMMAND_TYPE){
                        out.println("<p>item#:"+i+" code "+ jfiles_Work.getMisc_Sql_Command_Text().replaceAll("\n", "<br>"));
                        out.println("<br>");
                        try {
                            web_Process.do_Display_HTML_Form_Begin_Std(out,  "jfiles_"+i);
                            web_Process.do_Display_HTML_In_Form_Hidden_Input("item", ""+i);
                            //web_Process.do_Display_HTML_In_Form_Hidden_Input("action",  "execute_sql");
                            web_Process.do_Display_HTML_In_Form_Submit_Button("action",  "Do Command", "", this, "table_Modifications_Do_Action_Execute_SQL");
                            web_Process.do_Display_HTML_Form_End();
                        }catch(WPException wpe){
                            web_Process.error_End("failed to make button", wpe);
                            return;
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End("failed to make button", wpfe);
                            return;
                        }
                        
                    }else{
                        out.println("<p>item#:"+i+" sql "+ jfiles_Work.get_Sql_Command(true));
                        out.println("<br>");
                        try {
                            web_Process.do_Display_HTML_Form_Begin_Std(out,  "jfiles_"+i);
                            web_Process.do_Display_HTML_In_Form_Hidden_Input("item", ""+i);
                            // web_Process.do_Display_HTML_In_Form_Hidden_Input("action",  "execute_sql");
                            web_Process.do_Display_HTML_In_Form_Submit_Button("action",  "Do Command", "", this, "table_Modifications_Do_Action_Execute_SQL");
                            web_Process.do_Display_HTML_Form_End();
                        }catch(WPException wpe){
                            web_Process.error_End("failed to make button", wpe);
                            return;
                        }catch(WPFatal_Exception wpfe){
                            web_Process.error_End("failed to make button", wpfe);
                            return;
                        }
                    }
                }
                catch(Database_Record_Exception dbre){
                    out.println("<p>item#:"+i+" error :" +dbre.getMessage());
                }
            }
        }
        
        out.println("</div>");
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        out.close();
    }
    
    public void table_Modifications_Do_Action_Rename_Mode(Web_Process web_Process){
        web_Process.popStatus_Node();
        table_Modifications_Rename_Do_Display(web_Process);
    }
    
    public void table_Modifications_Do_Action_Reload(Web_Process web_Process){
        table_Modifications_Do_Display(web_Process);
    }
    
    
    public void table_Modifications_Do_Action_Execute_SQL(Web_Process web_Process){
        
        
        String item = (String)web_Process.getMy_Request().getParameter("item");
        if(item==null){
            web_Process.error_End("No item number for Do Command action.", null);
            return;
        }
        int index;
        try {
            index=Integer.parseInt(item);
        }
        catch(NumberFormatException nfe){
            web_Process.error_End("Item number is not an integer.", nfe);
            return;
        }
        
        LinkedList sql_Alter_List = (LinkedList) web_Process.getAttribute("sql_Alter_List");
        if(sql_Alter_List==null){
            web_Process.error_End("No sql_Alter_list Attribute.", null);
            return;
        }
        
        LinkedList warning_List = (LinkedList) web_Process.getAttribute("warning_List");
        if(warning_List==null){
            web_Process.error_End("No warning_List Attribute.", null);
            return;
        }
        
        if(index<0 || index>=sql_Alter_List.size()){
            web_Process.error_End("Item number :" + index + ": Not in range of sql_Alter_list.", null);
            return;
        }
        
        Jfiles_Work jfiles_Work = (Jfiles_Work)sql_Alter_List.get(index);
        if(jfiles_Work.getType() == Jfiles_Work.CLAUSE_CORRECTION_INTO_TYPE){
            web_Process.error_End("Item number :" + index + ": Not an sql command.", null);
            return;
        }else if(jfiles_Work.getType() == Jfiles_Work.MISC_SQL_COMMAND_TYPE){
            String sql_Command = jfiles_Work.getMisc_Sql_Command_Text();
            Statement statement;
            try{
                statement = web_Process.getDatabase_Front().get_Connection().createStatement();
                statement.execute(sql_Command);
            }
            catch(java.sql.SQLException sqle){
                web_Process.error_End("Sql command failed.", sqle);
                return;
            }
            sql_Alter_List.remove(index);
        }else{
            String sql_Command;
            try {
                sql_Command = jfiles_Work.get_Sql_Command(false);
            }
            catch(Database_Record_Exception dbre){
                web_Process.error_End("get_Sql_Command failed.", dbre);
                return;
            }
            
            Statement statement;
            try{
                statement = web_Process.getDatabase_Front().get_Connection().createStatement();
                statement.execute(sql_Command);
            }
            catch(java.sql.SQLException sqle){
                web_Process.error_End("Sql command failed.", sqle);
                return;
            }
            sql_Alter_List.remove(index);
            
        }
        
        web_Process.setAttribute("sql_Alter_List", sql_Alter_List);
        web_Process.setAttribute("warning_List", warning_List);
        
        table_Modifications_Do_Display(web_Process);
        return;
        
        
    }
    
    
    
    
    
    
    
    public void table_Modifications_Rename_Do_Display(Web_Process web_Process) {
        
        
        try {
            web_Process.pushStatus_Wreload("table_Modifications_Rename_Do_Display", this, "table_Modifications_Rename_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        
        LinkedList sql_Alter_List = (LinkedList) web_Process.getAttribute("sql_Alter_List");
        if(sql_Alter_List==null){
            web_Process.error_End("No sql_Alter_list Attribute.", null);
            return;
        }
        
        LinkedList warning_List = (LinkedList) web_Process.getAttribute("warning_List");
        if(warning_List==null){
            web_Process.error_End("No warning_List Attribute.", null);
            return;
        }
        
        
        String web_Root = web_Process.getSubscriber_Organization_Portal().getWeb_Root().toString();
        
        String css_Root = web_Process.getSubscriber_Organization_Portal().getCss_Root().toString();
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, "Jfiles sql and class adjustments");
        web_Process.do_Display_HTML_Head_End(out);
        
        
        web_Process.do_Display_HTML_Body_Begin_Plain(out);
        
        out.println("<div id='above_Header'>");
        out.println("</div>");
        
        out.println("<div id='header'>");
        out.println("<div id='header_Text'>");
        out.println("Jfiles sql and class adjustments");
        out.println("</div>");
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        try {
            web_Process.do_Display_HTML_Action_Button_Using_Form(out, "jfiles_end",  "action", "End Task", "End Task", "", this, "std_Action_End_Task");
            web_Process.do_Display_HTML_Action_Button_Using_Form(out, "jfiles_end",  "action", "Add/Drop Mode", "Add/Drop Mode", "", this, "table_Modifications_Rename_Do_Action_Add_Drop_Mode");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("failed to make button", wpfe);
            return;
        }
        
        out.println("</div>");
        
        out.println("<div id='main'>");
        
        for(int i=0; i<warning_List.size(); i++){
            out.println("<p>Warning :"+ warning_List.get(i));
        }
        if(sql_Alter_List.size()==0){
            out.println("<p> No corrections found.");
        }else{
            out.println("<p> USE " + web_Process.getSubscriber_Organization().getDatabase_Name() + ";");
            
            out.println("<p><b> Choose 1 ADD COLUMN and 1 DROP COLUMN that should combine to a RENAME COLUMN, then click submit.</b>");
            try {
                web_Process.do_Display_HTML_Form_Begin_Std(out,  "jfiles_Rename");
            }catch(WPException wpe){
                web_Process.error_End("failed to make form", wpe);
                return;
            }
            
            for(int i=0; i<sql_Alter_List.size();i++){
                Jfiles_Work jfiles_Work = (Jfiles_Work)sql_Alter_List.get(i);
                try {
                    if(jfiles_Work.getType() == Jfiles_Work.CLAUSE_CORRECTION_INTO_TYPE){
                        out.println("<p>item#:"+i+" code "+ jfiles_Work.get_Code_Correction());
                    }else if(jfiles_Work.getType() == Jfiles_Work.MISC_SQL_COMMAND_TYPE){
                        out.println("<p>");
                        //out.println("<input type=checkbox name=row_"+i+"_Select >");
                        
                        out.println(" item#:"+i+" code "+ jfiles_Work.getMisc_Sql_Command_Text().replaceAll("\n", "<br>"));
                        out.println("<br>");
                        
                        
                    }else{
                        out.println("<p>");
                        out.println("<input type=checkbox name=row_"+i+"_Select >");
                        
                        out.println(" item#:"+i+" sql "+ jfiles_Work.get_Sql_Command(true));
                        out.println("<br>");
                        
                    }
                }
                catch(Database_Record_Exception dbre){
                    out.println("<p>item#:"+i+" error :" +dbre.getMessage());
                }
            }
            out.println("<p>");
            try {
                web_Process.do_Display_HTML_In_Form_Submit_Button("action",  "submit", "", this, "table_Modifications_Rename_Do_Action_Submit");
            }catch(WPFatal_Exception wpfe){
                web_Process.error_End("failed to make button", wpfe);
                return;
            }
            
            try {
                web_Process.do_Display_HTML_Form_End();
            }catch(WPException wpe){
                web_Process.error_End("failed to make form", wpe);
                return;
            }
            
        }
        
        out.println("</div>");
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        out.close();
    }
    
    public void table_Modifications_Rename_Do_Action_Add_Drop_Mode(Web_Process web_Process){
        web_Process.popStatus_Node();
        table_Modifications_Do_Display(web_Process);
    }
    
    
    public void table_Modifications_Rename_Do_Action_Submit(Web_Process web_Process){
        
        LinkedList sql_Alter_List = (LinkedList) web_Process.getAttribute("sql_Alter_List");
        if(sql_Alter_List==null){
            web_Process.error_End("No sql_Alter_list Attribute.", null);
            return;
        }
        int count=0;
        int[] selected_Sql_Alter_List_Items = new int[2];
        
        for(int i=0; i<sql_Alter_List.size();i++){
            Jfiles_Work jfiles_Work = (Jfiles_Work)sql_Alter_List.get(i);
            
            String select = web_Process.getMy_Request().getParameter("row_"+i+"_Select");
            if(select==null){
                select="off";
            }
            if(select.equals("on")){
                if(count>1){
                    web_Process.error("Only select 2 items.", null);
                    return;
                }
                selected_Sql_Alter_List_Items[count]=i;
                count++;
            }
        }
        if(count!=2){
            web_Process.error("Select 2 items not "+count+".", null);
            return;
        }
        Jfiles_Work jfiles_Work0 = (Jfiles_Work)sql_Alter_List.get(selected_Sql_Alter_List_Items[0]);
        Jfiles_Work jfiles_Work1 = (Jfiles_Work)sql_Alter_List.get(selected_Sql_Alter_List_Items[1]);
        
        
        
        if(!jfiles_Work0.getTable_Name().equals(jfiles_Work1.getTable_Name())){
            web_Process.error("Item # "+selected_Sql_Alter_List_Items[0]+" and Item # "+selected_Sql_Alter_List_Items[1] +" must be in the same table.", null);
            return;
        }
        
        if(jfiles_Work0.getType() == Jfiles_Work.ADD_TO_SQL_TYPE){
            if(jfiles_Work1.getType() == Jfiles_Work.ADD_TO_SQL_TYPE){
                web_Process.error("Item # "+selected_Sql_Alter_List_Items[0]+" and Item # "+selected_Sql_Alter_List_Items[1] +" are both ADD COLUMN type.", null);
                return;
            }else if(jfiles_Work1.getType() == Jfiles_Work.DROP_FROM_SQL_TYPE){
                // ok
                ;
            }else{
                web_Process.error("Item # "+selected_Sql_Alter_List_Items[1]+" is neither and ADD COLUMN nor a DROP COLUMN type.", null);
                return;
            }
        }else  if(jfiles_Work0.getType() == Jfiles_Work.DROP_FROM_SQL_TYPE){
            if(jfiles_Work1.getType() == Jfiles_Work.ADD_TO_SQL_TYPE){
                // flip order
                int dmyi = selected_Sql_Alter_List_Items[0];
                selected_Sql_Alter_List_Items[0] = selected_Sql_Alter_List_Items[1];
                selected_Sql_Alter_List_Items[1] = dmyi;
                jfiles_Work0 = (Jfiles_Work)sql_Alter_List.get(selected_Sql_Alter_List_Items[0]);
                jfiles_Work1 = (Jfiles_Work)sql_Alter_List.get(selected_Sql_Alter_List_Items[1]);
            }else if(jfiles_Work1.getType() == Jfiles_Work.DROP_FROM_SQL_TYPE){
                web_Process.error("Item # "+selected_Sql_Alter_List_Items[0]+" and Item # "+selected_Sql_Alter_List_Items[1] +" are both DROP COLUMN type.", null);
                return;
            }else{
                web_Process.error("Item # "+selected_Sql_Alter_List_Items[1]+" is neither and ADD COLUMN nor a DROP COLUMN type.", null);
                return;
            }
        }else {
            web_Process.error("Item # "+selected_Sql_Alter_List_Items[0]+" is neither and ADD COLUMN nor a DROP COLUMN type.", null);
            return;
        }
        
        //0 is add
        //1 is drop
        DFBase add_DFBase = jfiles_Work0.getProgrammer_Field();
        DFBase drop_DFBase = jfiles_Work1.getSql_Field();
        
        System.out.println("add_DFBase.getSQL_Declaration():"+add_DFBase.getSQL_Declaration());
        System.out.println("drop_DFBase.getSQL_Declaration():"+drop_DFBase.getSQL_Declaration());
        
        
        String add_SQL_Declaration = add_DFBase.getSQL_Declaration();
        String drop_SQL_Declaration = drop_DFBase.getSQL_Declaration();
        
        String add_Field_Name = add_DFBase.getField_Name();
        String drop_Field_Name = drop_DFBase.getField_Name();
        
        add_SQL_Declaration = add_SQL_Declaration.substring(add_Field_Name.length()+2);
        drop_SQL_Declaration = drop_SQL_Declaration.substring(drop_Field_Name.length()+2);
        
        System.out.println("add_SQL_Declaration():"+add_SQL_Declaration);
        System.out.println("drop_SQL_Declaration():"+drop_SQL_Declaration);
        
        
        if(!add_SQL_Declaration.equals(drop_SQL_Declaration)){
            web_Process.error((
            "Column information (type, length) is/are not the same.\n" +
            " add_SQL_Declaration():" + add_DFBase.getSQL_Declaration() + ", "+
            " drop_SQL_Declaration():" + drop_DFBase.getSQL_Declaration()
            ).replaceAll("\n", "&lt;br>")
            , null);
            return;
        }
        
        Jfiles_Work rename_Jfiles_Work;
        try {
            rename_Jfiles_Work = new Jfiles_Work(Jfiles_Work.RENAME_IN_SQL_TYPE, jfiles_Work0.getTable_Name(), jfiles_Work0.getProgrammer_Field(), jfiles_Work1.getSql_Field());
        }catch(Database_Record_Constructor_Exception dbrce){
            web_Process.error_End(dbrce);
            return;
        }
        
        web_Process.setAttribute("rename_Jfiles_Work", rename_Jfiles_Work);
        web_Process.setAttribute("selected_Sql_Alter_List_Items", selected_Sql_Alter_List_Items);
        
        web_Process.popStatus_Node();
        table_Column_Rename_Do_Display(web_Process);
    }
    
    
    public void table_Modifications_Rename_Do_Action_Reload(Web_Process web_Process){
        table_Modifications_Rename_Do_Display(web_Process);
        
    }
    
    
    
    
    public void table_Column_Rename_Do_Display(Web_Process web_Process) {
        
        
        try {
            web_Process.pushStatus_Wreload("table_Column_Rename_Do_Display", this, "table_Column_Rename_Do_Action_Reload");
        }catch(WPFatal_Exception wpe){
            web_Process.error_End(wpe);
            return;
        }
        
        Jfiles_Work rename_Jfiles_Work = (Jfiles_Work) web_Process.getAttribute("rename_Jfiles_Work");
        if(rename_Jfiles_Work==null){
            web_Process.error_End("No rename_Jfiles_Work Attribute.", null);
            return;
        }
        
        
        
        String web_Root = web_Process.getSubscriber_Organization_Portal().getWeb_Root().toString();
        
        String css_Root = web_Process.getSubscriber_Organization_Portal().getCss_Root().toString();
        
        web_Process.getMy_Response().setContentType("text/html");
        PrintWriter out = web_Process.getOut();
        
        web_Process.do_Display_HTML_Page_Begin(out);
        
        web_Process.do_Display_HTML_Head_Begin_Std(out, "Column rename");
        web_Process.do_Display_HTML_Head_End(out);
        
        
        web_Process.do_Display_HTML_Body_Begin_Plain(out);
        
        out.println("<div id='above_Header'>");
        out.println("</div>");
        
        out.println("<div id='header'>");
        out.println("<div id='header_Text'>");
        out.println("Column rename");
        out.println("</div>");
        out.println("</div>");
        
        out.println("<div id='sidebar'>");
        
        
        
        try {
            web_Process.do_Display_HTML_Action_Button_Using_Form(out, "jfiles_end",  "action", "End Task", "End Task", "", this, "std_Action_End_Task");
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("failed to make button", wpfe);
            return;
        }
        
        out.println("</div>");
        
        out.println("<div id='main'>");
        
        
        
        out.println("<p> USE " + web_Process.getSubscriber_Organization().getDatabase_Name() + ";");
        
        
        try {
            out.println("<p> sql "+ rename_Jfiles_Work.get_Sql_Command(true));
        }catch(Database_Record_Exception dbre){
            web_Process.error_End(dbre);
            return;
        }
        out.println("<br>");
        try {
            web_Process.do_Display_HTML_Form_Begin_Std(out,  "jfiles_rename");
            // web_Process.do_Display_HTML_In_Form_Hidden_Input("action",  "execute_sql");
            web_Process.do_Display_HTML_In_Form_Submit_Button("action",  "Do Command", "", this, "table_Column_Rename_Do_Action_Execute_SQL");
            web_Process.do_Display_HTML_Form_End();
        }catch(WPException wpe){
            web_Process.error_End("failed to make button", wpe);
            return;
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("failed to make button", wpfe);
            return;
        }
        
        
        
        
        
        out.println("</div>");
        
        
        web_Process.do_Display_HTML_Body_End(out);
        web_Process.do_Display_HTML_Page_End(out);
        
        out.close();
    }
    
    public void table_Column_Rename_Do_Action_Execute_SQL(Web_Process web_Process){
        
        
        LinkedList sql_Alter_List = (LinkedList) web_Process.getAttribute("sql_Alter_List");
        if(sql_Alter_List==null){
            web_Process.error_End("No sql_Alter_list Attribute.", null);
            return;
        }
        
        
        Jfiles_Work rename_Jfiles_Work = (Jfiles_Work) web_Process.getAttribute("rename_Jfiles_Work");
        if(rename_Jfiles_Work==null){
            web_Process.error_End("No rename_Jfiles_Work Attribute.", null);
            return;
        }
        
        int[] selected_Sql_Alter_List_Items = (int[]) web_Process.getAttribute("selected_Sql_Alter_List_Items");
        if(selected_Sql_Alter_List_Items==null){
            web_Process.error_End("No selected_Sql_Alter_List_Items Attribute.", null);
            return;
        }
        
        
        
        
        if(rename_Jfiles_Work.getType() == Jfiles_Work.CLAUSE_CORRECTION_INTO_TYPE){
            web_Process.error_End("Not an sql command.", null);
            return;
        }else if(rename_Jfiles_Work.getType() == Jfiles_Work.MISC_SQL_COMMAND_TYPE){
            web_Process.error_End("Not a RENAME command.", null);
            return;
        }else{
            String sql_Command;
            try {
                sql_Command = rename_Jfiles_Work.get_Sql_Command(false);
            }
            catch(Database_Record_Exception dbre){
                web_Process.error_End("get_Sql_Command failed.", dbre);
                return;
            }
            
            Statement statement;
            try{
                statement = web_Process.getDatabase_Front().get_Connection().createStatement();
                statement.execute(sql_Command);
            }
            catch(java.sql.SQLException sqle){
                web_Process.error_End("Sql command failed.", sqle);
                return;
            }
            
        }
        
        if(selected_Sql_Alter_List_Items[0] < selected_Sql_Alter_List_Items[1]){
            sql_Alter_List.remove(selected_Sql_Alter_List_Items[1]);
            sql_Alter_List.remove(selected_Sql_Alter_List_Items[0]);
        }else{
            sql_Alter_List.remove(selected_Sql_Alter_List_Items[0]);
            sql_Alter_List.remove(selected_Sql_Alter_List_Items[1]);
        }
        
        web_Process.setAttribute("sql_Alter_List", sql_Alter_List);
        
        web_Process.popStatus_Node();
        table_Modifications_Rename_Do_Display(web_Process);
        return;
        
        
    }
    
    public void table_Column_Rename_Do_Action_Reload(Web_Process web_Process){
        table_Column_Rename_Do_Display(web_Process);
        
    }
    
    
    
    
    
}
