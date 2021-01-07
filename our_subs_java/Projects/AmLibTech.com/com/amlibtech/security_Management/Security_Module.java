/*
 * Security_Module.java
 *
 * Created on July 16, 2006, 8:25 PM
 */

package com.amlibtech.security_Management;

import com.amlibtech.database.*;
import com.amlibtech.security_Management.data.*;
import com.amlibtech.web_Processes.*;

/**
 *
 * @author  dgleeson
 */
public class Security_Module {
    
    /** Creates a new instance of Security_Module */
    private Security_Module() {
    }

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
    
    
    public static void check_Query(Web_Process web_Process)
    throws Security_Exception {
        check_Query(web_Process, web_Process.getDatabase_Front());
    }
        
    public static void check_Query(Web_Process web_Process, Database_Front database_Front)
    throws Security_Exception {
        if(web_Process.getUser().getAsp_Admin().getValue().booleanValue())
            return;
        try {
            String security_Profile_Id =  web_Process.getUser().getSecurity_Profile_Id().getValue();
            if(security_Profile_Id.equals("")){
                throw new Security_Exception("Security_Module.check_Query(): User :" + web_Process.getUser().getName_First_Then_Last() + " has no Security Profile Id.");
            }
            Security_Profile security_Profile;
            try {
                security_Profile = (Security_Profile) database_Front.getRecord(new Security_Profile(security_Profile_Id));
            }catch(Database_Front_Exception dbfe){
                throw new Security_Exception("Security_Module.check_Query(): Database_Front_Exception : " + dbfe.getMessage());
            }catch(Database_Front_No_Results_Exception dbfe){
                throw new Security_Exception("Security_Module.check_Query(): no such Security Profile as :" + security_Profile_Id + ".");
            }
            
            String program_Name = web_Process.getProgram().getProgram_Name().getValue();
            
            Security_Profile_Program security_Profile_Program;
            try {
                security_Profile_Program = (Security_Profile_Program) database_Front.getRecord(new Security_Profile_Program(security_Profile_Id, program_Name));
                
                if(security_Profile_Program.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_QUERY]) ||
                   security_Profile_Program.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_MAINT])){
                    return;
                }
                if(security_Profile_Program.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_NONE])){
                    throw new Security_Exception("You do not have permission to query/view this information.");
                }

            }catch(Database_Front_Exception dbfe){
                throw new Security_Exception("Security_Module.check_Query(): Database_Front_Exception : " + dbfe.getMessage());
            }catch(Database_Front_No_Results_Exception dbfe){
                ;
            }
            
            
            String subsystem_Id = web_Process.getProgram().getSubsystem_Id().getValue();
            Security_Profile_Subsystem security_Profile_Subsystem;
            try {
                security_Profile_Subsystem = (Security_Profile_Subsystem) database_Front.getRecord(new Security_Profile_Subsystem(security_Profile_Id, subsystem_Id));
                
                if(security_Profile_Subsystem.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_QUERY]) ||
                   security_Profile_Subsystem.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_MAINT])){
                    return;
                }
                
                if(security_Profile_Subsystem.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_NONE])){
                    throw new Security_Exception("You do not have permission to query/view this information.");
                }

                
            }catch(Database_Front_Exception dbfe){
                throw new Security_Exception("Security_Module.check_Query(): Database_Front_Exception : " + dbfe.getMessage());
            }catch(Database_Front_No_Results_Exception dbfe){
                ;
            }
            
            throw new Security_Exception("You do not have permission to query/view this information.");
            
        }catch(Database_Record_Constructor_Exception dbrce){
            throw new Security_Exception("Security_Module.check_Query(): Database_Record_Constructor_Exception : " + dbrce.getMessage());
        }
    }
    
    
    public static void check_Transaction(Web_Process web_Process)
    throws Security_Exception {
        check_Transaction(web_Process, web_Process.getDatabase_Front());
    }
        
    public static void check_Transaction(Web_Process web_Process, Database_Front database_Front)
    throws Security_Exception {
        if(web_Process.getUser().getAsp_Admin().getValue().booleanValue())
            return;
        try {
            String security_Profile_Id =  web_Process.getUser().getSecurity_Profile_Id().getValue();
            if(security_Profile_Id.equals("")){
                throw new Security_Exception("Security_Module.check_Transaction(): User :" + web_Process.getUser().getName_First_Then_Last() + " has no Security Profile Id.");
            }
            Security_Profile security_Profile;
            try {
                security_Profile = (Security_Profile) database_Front.getRecord(new Security_Profile(security_Profile_Id));
            }catch(Database_Front_Exception dbfe){
                throw new Security_Exception("Security_Module.check_Transaction(): Database_Front_Exception : " + dbfe.getMessage());
            }catch(Database_Front_No_Results_Exception dbfe){
                throw new Security_Exception("Security_Module.check_Transaction(): no such Security Profile as :" + security_Profile_Id + ".");
            }
            
            String program_Name = web_Process.getProgram().getProgram_Name().getValue();
            
            Security_Profile_Program security_Profile_Program;
            try {
                security_Profile_Program = (Security_Profile_Program) database_Front.getRecord(new Security_Profile_Program(security_Profile_Id, program_Name));
                
                if(security_Profile_Program.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_MAINT])){
                    return;
                }
                if(security_Profile_Program.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_QUERY]) ||
                   security_Profile_Program.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_NONE])){
                    throw new Security_Exception("You do not have permission to transaction/change this information.");
                }
                
            }catch(Database_Front_Exception dbfe){
                throw new Security_Exception("Security_Module.check_Transaction(): Database_Front_Exception : " + dbfe.getMessage());
            }catch(Database_Front_No_Results_Exception dbfe){
                ;
            }
            
            
            String subsystem_Id = web_Process.getProgram().getSubsystem_Id().getValue();
            Security_Profile_Subsystem security_Profile_Subsystem;
            try {
                security_Profile_Subsystem = (Security_Profile_Subsystem) database_Front.getRecord(new Security_Profile_Subsystem(security_Profile_Id, subsystem_Id));
                
                if(security_Profile_Subsystem.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_MAINT])){
                    return;
                }
                if(security_Profile_Subsystem.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_QUERY]) ||
                   security_Profile_Subsystem.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_NONE])){
                    throw new Security_Exception("You do not have permission to transaction/change this information.");
                }
                
            }catch(Database_Front_Exception dbfe){
                throw new Security_Exception("Security_Module.check_Transaction(): Database_Front_Exception : " + dbfe.getMessage());
            }catch(Database_Front_No_Results_Exception dbfe){
                ;
            }
            
            throw new Security_Exception("You do not have permission to transaction/change this information.");
                
        }catch(Database_Record_Constructor_Exception dbrce){
            throw new Security_Exception("Security_Module.check_Transaction(): Database_Record_Constructor_Exception : " + dbrce.getMessage());
        }
    }
    
    
    
    
    
    
    
    
    
    public static boolean allow_DataGroup_Query(Web_Process web_Process, String security_DataGroup_Id){
        return(allow_DataGroup_Query(web_Process, security_DataGroup_Id, web_Process.getDatabase_Front()));
    }

    
    
    public static boolean allow_DataGroup_Query(Web_Process web_Process, String security_DataGroup_Id, Database_Front database_Front)
    {
        if(web_Process.getUser().getAsp_Admin().getValue().booleanValue())
            return true;
        try {
            String security_Profile_Id =  web_Process.getUser().getSecurity_Profile_Id().getValue();
            if(security_Profile_Id.equals("")){
                return false;
            }
            Security_Profile security_Profile;
            try {
                security_Profile = (Security_Profile) database_Front.getRecord(new Security_Profile(security_Profile_Id));
            }catch(Database_Front_Exception dbfe){
                return false;
            }catch(Database_Front_No_Results_Exception dbfe){
                return false;
            }
            
            Security_Profile_DataGroup security_Profile_DataGroup;            
            try {
                security_Profile_DataGroup = (Security_Profile_DataGroup) database_Front.getRecord(new Security_Profile_DataGroup(security_Profile_Id, security_DataGroup_Id));
                
                if(security_Profile_DataGroup.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_QUERY]) ||
                   security_Profile_DataGroup.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_MAINT])){
                    return true;
                }
                if(security_Profile_DataGroup.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_NONE])){
                return false;
                }

            }catch(Database_Front_Exception dbfe){
                return false;
            }catch(Database_Front_No_Results_Exception dbfe){
                ;
            }
            
            
                return false;
            
        }catch(Database_Record_Constructor_Exception dbrce){
                return false;
        }
    }
    
    
    public static boolean allow_DataGroup_Transaction(Web_Process web_Process, String security_DataGroup_Id){
        return(allow_DataGroup_Transaction(web_Process, security_DataGroup_Id, web_Process.getDatabase_Front()));
    }

    
    public static boolean allow_DataGroup_Transaction(Web_Process web_Process, String security_DataGroup_Id, Database_Front database_Front)
    {
       if(web_Process.getUser().getAsp_Admin().getValue().booleanValue())
            return true;
        try {
            String security_Profile_Id =  web_Process.getUser().getSecurity_Profile_Id().getValue();
            if(security_Profile_Id.equals("")){
                return false;
            }
            Security_Profile security_Profile;
            try {
                security_Profile = (Security_Profile) database_Front.getRecord(new Security_Profile(security_Profile_Id));
            }catch(Database_Front_Exception dbfe){
                return false;
            }catch(Database_Front_No_Results_Exception dbfe){
                return false;
            }
            
            Security_Profile_DataGroup security_Profile_DataGroup;            
            try {
                security_Profile_DataGroup = (Security_Profile_DataGroup) database_Front.getRecord(new Security_Profile_DataGroup(security_Profile_Id, security_DataGroup_Id));
               
                if(security_Profile_DataGroup.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_MAINT])){
                    return true;
                }
                if(security_Profile_DataGroup.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_QUERY]) ||
                   security_Profile_DataGroup.getAccess_Type().getValue().equals(Security_Profile.ACCESS_TYPES[Security_Profile.ACCESS_TYPE_NONE])){
                return false;
                }
                
            }catch(Database_Front_Exception dbfe){
                return false;
            }catch(Database_Front_No_Results_Exception dbfe){
                ;
            }
            
            
                return false;
                
        }catch(Database_Record_Constructor_Exception dbrce){
                return false;
        }
    }
    
}
