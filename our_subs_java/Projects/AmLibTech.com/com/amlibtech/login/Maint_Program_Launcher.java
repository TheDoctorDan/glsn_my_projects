/*
 * Maint_Program_Launcher.java
 *
 * Created on August 2, 2006, 10:04 AM
 * revised on February 16, 2007,  5:06 PM to use program table's new field : maintenance_Of_Table_Name
 */

package com.amlibtech.login;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.login.data.*;
import com.amlibtech.web_Processes.*;
import java.io.*;
import javax.servlet.*;

/**
 *
 * @author  dgleeson
 */
public class Maint_Program_Launcher {
    

    private static final String[] Copyright= {
    "|       Copyright (c) 1985 thru 2001, 2002, 2003, 2004, 2005, 2006, 2007       |",
    "|       American Liberator Technologies                                        |",
    "|       All Rights Reserved                                                    |",
    "|                                                                              |",
    "|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |",
    "|       American Liberator Technologies                                        |",
    "|       The copyright notice above does not evidence any                       |",
    "|       actual or intended publication of such source code.                    |"
    };
   
    
    public static void process_Maint(Web_Process web_Process)
            throws ServletException, IOException {

            String maint_Table_Name = (String) web_Process.getMy_Request().getParameter("maint");
            if(maint_Table_Name==null){
                web_Process.error("Maint. button did not specify which table to maintain.", null);
                return;
            }
            
            Program program=null;
            try {
                program = Program.get_Maint_Program_Of_Table(maint_Table_Name, web_Process);
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return;
            }catch(Database_Record_Constructor_Exception dbrce){
                web_Process.error_End(dbrce);
                return;
            }catch(DFException dfe){
                web_Process.error_End(dfe);
                return;
            }
            if(program==null){
                web_Process.error("Maint. button specified table :"+maint_Table_Name+" to maintain, but I can't find an installed maintenance program for that table.", null);
                return;
            }
            String launch_Name = program.getProgram_Name().getValue();
            if(launch_Name.startsWith("/")) launch_Name = launch_Name.substring(1);
            
            web_Process.launch_Task(launch_Name, null);
            return;
            
            
            /*
            if(maint_Table_Name.equals("studio")){
                web_Process.launch_Task("Ena100",null);
                return;
                
            }else if(maint_Table_Name.equals("department")){
                web_Process.launch_Task("Ena200",null);
                return;
                
            }else if(maint_Table_Name.equals("course")){
                web_Process.launch_Task("Ena300",null);
                return;
                
            }else if(maint_Table_Name.equals("course_coreq")){
                web_Process.launch_Task("Ena301",null);
                return;
                
            }else if(maint_Table_Name.equals("course_prereq")){
                web_Process.launch_Task("Ena302",null);
                return;
                
            }else  if(maint_Table_Name.equals("registration_period")){
                web_Process.launch_Task("Ena500",null);
                return;
                 
            }else  if(maint_Table_Name.equals("registration_Period_Vacation_Day")){
                web_Process.launch_Task("Ena501",null);
                return;
                
            }else if(maint_Table_Name.equals("teacher")){
                web_Process.launch_Task("Ena600",null);
                return;
                
            }else if(maint_Table_Name.equals("teacher")){
                web_Process.launch_Task("Ena600",null);
                return;
                
            }else if(maint_Table_Name.equals("course_Offering")){
                web_Process.launch_Task("Ena700",null);
                return;
                
            }else if(maint_Table_Name.equals("comp")){
                web_Process.launch_Task("Cmp100",null);
                return;
                
            }else if(maint_Table_Name.equals("ar_Terms")){
                web_Process.launch_Task("Ar830",null);
                return;
           
                
                
                
            }else if(maint_Table_Name.equals("customer_Control")){
                web_Process.launch_Task("Cs001",null);
                return;
                   
            }else if(maint_Table_Name.equals("cust")){
                web_Process.launch_Task("Cs100",null);
                return;
                    
            }else if(maint_Table_Name.equals("cust_Totaling_Group")){
                web_Process.launch_Task("Cs830",null);
                return;
                    
            }else if(maint_Table_Name.equals("cust_Type")){
                web_Process.launch_Task("Cs840",null);
                return;
                       
            }else if(maint_Table_Name.equals("ar_Terms")){
                web_Process.launch_Task("ar830",null);
                return;
                
                        
            }else if(maint_Table_Name.equals("entity_Control")){
                web_Process.launch_Task("Enty001",null);
                return;
                           
            }else if(maint_Table_Name.equals("person_Contact_Type")){
                web_Process.launch_Task("Enty002",null);
                return;
                
           }else if(maint_Table_Name.equals("organization_Type")){
                web_Process.launch_Task("Enty010",null);
                return;
                
           }else if(maint_Table_Name.equals("organization_Contact_Type")){
                web_Process.launch_Task("Enty011",null);
                return;
                           
            }else if(maint_Table_Name.equals("person")){
                web_Process.launch_Task("Enty100",null);
                return;
                               
            }else if(maint_Table_Name.equals("family")){
                web_Process.launch_Task("Enty200",null);
                return;
                
                            
            }else if(maint_Table_Name.equals("user")){
                 web_Process.launch_Task("Sm100",null);
                return;
                               
            }else if(maint_Table_Name.equals("security_Profile")){
                 web_Process.launch_Task("Secm100",null);
                return;
                
            }else if(maint_Table_Name.equals("job_Posting")){
                web_Process.launch_Task("Jam300",null);
                return;
                
            }else {
                web_Process.error("Maint. button specified unknown file:"+maint_Table_Name+" to maint.", null);
                return;
            }
             **/
            
    }
}
