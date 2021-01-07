/*
 * Subscriber_Organization.java
 *
 * Created on March 17, 2005 3:02:11 PM CST
 */

/**
 *|------------------------------------------------------------------------------|
 *|       Copyright (c) 1985 thru 1999, 2000, 2001, 2002, 2003, 2004, 2005       |
 *|       American Liberator Technologies                                        |
 *|       All Rights Reserved                                                    |
 *|                                                                              |
 *|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
 *|       American Liberator Technologies                                        |
 *|       The copyright notice above does not evidence any                       |
 *|       actual or intended publication of such source code.                    |
 *|------------------------------------------------------------------------------|
 */

package net.amlibtech.ASP.data;

import com.amlibtech.data_fields.*;
import java.util.*;
import javax.servlet.http.*;

import com.amlibtech.web.data.*;
import com.amlibtech.web.servlet_exceptions.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
import java.lang.reflect.*;



public class Subscriber_Organization extends Database_Record_Base implements Database_Record_Interface {
    
    
    // Declaration Section
    
    private	DFString_Filled	subscriber_Organization_Id;
    private	DFString	name;
    private	DFString	description;
    private	DFString	database_Server_URL;
    private	DFString	database_Name;
    private	DFString	database_Access_Login_Name;
    private	DFString	database_Access_Login_Password;
    private	DFString	destination_URL;
    
    public static final int SUBSCRIBER_ORGANIZATION_ID_LEN = 30;
    
  
    
    // Empty Constructor Section
    
    public Subscriber_Organization() throws Database_Record_Exception {
        super("Subscriber Organization", "Subscriber Organizations");

        try{
            this.subscriber_Organization_Id = new DFString_Filled("subscriber_Organization_Id", "Subscriber Organization ID#", SUBSCRIBER_ORGANIZATION_ID_LEN, DFString_Filled.FILL_TYPE_LEFT, ' ');
            this.name = new DFString("name", "Name", 38);
            this.description = new DFString("description", "Description", 100);
            this.database_Server_URL = new DFString("database_Server_URL", "Database Server URL", 200);
            this.database_Name = new DFString("database_Name", "Database Name", 100);
            this.database_Access_Login_Name = new DFString("database_Access_Login_Name", "Database Access Login Name", 30);
            this.database_Access_Login_Password = new DFString("database_Access_Login_Password", "Database Access Login Password", 30);
            this.destination_URL = new DFString("destination_URL", "destination_URL", 200);
        }
        catch(DFException dfe){
            throw new Database_Record_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }
    
    
    
    // Key Fields Constructor Section 

    public Subscriber_Organization(String subscriber_Organization_Id)  throws Database_Record_Exception {
        this();
        try{
            this.subscriber_Organization_Id.setValue(subscriber_Organization_Id);
        }
        catch(DFException dfe){
            throw new Database_Record_Exception(this.getClass().getName() + ": constructor(ID) : failed because DFException : " + dfe.getMessage());
        }
    }
    
    
       
    /**
     * Getter for property subscriber_Organization_Id.
     * @return Value of property subscriber_Organization_Id.
     */
    public DFString_Filled getSubscriber_Organization_Id() {
        return subscriber_Organization_Id;
    }
    
    
    
    /**
     * Setter for property subscriber_Organization_Id.
     * @param subscriber_Organization_Id New value of property subscriber_Organization_Id.
     */
    public void setSubscriber_Organization_Id(String subscriber_Organization_Id) throws DFException {
        this.subscriber_Organization_Id.setValue(subscriber_Organization_Id);
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public DFString getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) throws DFException {
        this.name.setValue(name);
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public DFString getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) throws DFException {
        this.description.setValue(description);
    }
    
    /**
     * Getter for property database_Server_URL.
     * @return Value of property database_Server_URL.
     */
    public DFString getDatabase_Server_URL() {
        return database_Server_URL;
    }
    
    /**
     * Setter for property database_Server_URL.
     * @param database_Server_URL New value of property database_Server_URL.
     */
    public void setDatabase_Server_URL(String database_Server_URL) throws DFException {
        this.database_Server_URL.setValue(database_Server_URL);
    }
    
    /**
     * Getter for property database_Name.
     * @return Value of property database_Name.
     */
    public DFString getDatabase_Name() {
        return database_Name;
    }
    
    /**
     * Setter for property database_Name.
     * @param database_Name New value of property database_Name.
     */
    public void setDatabase_Name(String database_Name) throws DFException {
        this.database_Name.setValue(database_Name);
    }
    
    /**
     * Getter for property database_Access_Login_Name.
     * @return Value of property database_Access_Login_Name.
     */
    public DFString getDatabase_Access_Login_Name() {
        return database_Access_Login_Name;
    }
    
    /**
     * Setter for property database_Access_Login_Name.
     * @param database_Access_Login_Name New value of property database_Access_Login_Name.
     */
    public void setDatabase_Access_Login_Name(String database_Access_Login_Name) throws DFException {
        this.database_Access_Login_Name.setValue(database_Access_Login_Name);
    }
    
    /**
     * Getter for property database_Access_Login_Password.
     * @return Value of property database_Access_Login_Password.
     */
    public DFString getDatabase_Access_Login_Password() {
        return database_Access_Login_Password;
    }
    
    /**
     * Setter for property database_Access_Login_Password.
     * @param database_Access_Login_Password New value of property database_Access_Login_Password.
     */
    public void setDatabase_Access_Login_Password(String database_Access_Login_Password) throws DFException {
        this.database_Access_Login_Password.setValue(database_Access_Login_Password);
    }
    
    /**
     * Getter for property destination_URL.
     * @return Value of property destination_URL.
     */
    public DFString getDestination_URL() {
        return destination_URL;
    }
    
    /**
     * Setter for property destination_URL.
     * @param destination_URL New value of property destination_URL.
     */
    public void setDestination_URL(String destination_URL) throws DFException {
        this.destination_URL.setValue(destination_URL);
    }
    
    
 
    
    
    // Key Fields Validation Section
    
    public synchronized void validate_Key_Fields_Of_Request(HttpServletRequest request, Database_Front database_Front)
    throws  Database_Record_Exception {
        int error_Count = 0;
        
        error_Count += this.subscriber_Organization_Id.validate_Request(request);
            
        if(error_Count != 0)
            throw new Database_Record_Exception (this.getClass().getName() + "validate_Key_Fields_Of_Request(): Invalid Data Field(s).");
    }
    
    
    
    
    // Field Validation Section
    
    public synchronized void validate_Fields_Of_Request(HttpServletRequest request, Database_Front database_Front) throws Database_Record_Exception {
       
        int error_Count=0;        

        error_Count += this.name.validate_Request(request);
        error_Count += this.description.validate_Request(request);
        error_Count += this.database_Server_URL.validate_Request(request);
        error_Count += this.database_Name.validate_Request(request);
        error_Count += this.database_Access_Login_Name.validate_Request(request);
        error_Count += this.database_Access_Login_Password.validate_Request(request);
        error_Count += this.destination_URL.validate_Request(request);

        if(error_Count != 0)
            throw new Database_Record_Exception (this.getClass().getName() + "validate_Fields_Of_Request(): Invalid Data Field(s).");
       
    }
    
       
    // Process Key Fields Section
    
    public static synchronized Subscriber_Organization process_Key_Fields_Request(HttpServletRequest request, Database_Front database_Front)
    throws  Database_Record_Exception {
        HttpSession session = request.getSession();
        String message = "";
        
        Subscriber_Organization subscriber_Organization_Keys = new Subscriber_Organization();
        
        subscriber_Organization_Keys.validate_Key_Fields_Of_Request(request, database_Front);
        
        if(subscriber_Organization_Keys.getSubscriber_Organization_Id()==null ||
            subscriber_Organization_Keys.getSubscriber_Organization_Id().getValue()==null ||
            subscriber_Organization_Keys.getSubscriber_Organization_Id().getValue().trim().equals("") ) {
                throw new Database_Record_Exception("Subscriber_Organization: process_Key_Fields_Request(): No subscriber_Organization_Id passed to servlet.");
        }
        
        
        return subscriber_Organization_Keys;
    }
    
    
    
    // Is Delete Allowed Section
    
    
    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession session) {
        return;
    }
    
    
    // SQL Section 

	public String get_Table_Name() {
		return "subscriber_organization";
	}

    public String get_First_Field_Name() {
        return "subscriber_Organization_Id";
    }
    
    public String get_Into_Clause() {
        return "( subscriber_Organization_Id, name, description, database_Server_URL, database_Name, database_Access_Login_Name, database_Access_Login_Password, destination_URL )";
        
    }
    
    public String get_Key_Where_Clause() {
        return "subscriber_Organization_Id = ? ";
        
    }
    
    public String get_Set_Clause() {
        return "name = ?, description = ?, database_Server_URL = ?, database_Name = ?, database_Access_Login_Name = ?, database_Access_Login_Password = ?, destination_URL = ?";
        
    }
    
    public String get_Values_Clause() {
        return "( ?, ?, ?, ?, ?, ?, ?, ? )";
    }


	public synchronized java.sql.PreparedStatement preparedStatement_Encode(java.sql.PreparedStatement preparedStatement, int order) throws Database_Record_Exception {
		int count=1;
                try {
                    for(int pass=1;pass<=2;pass++){
			if((pass == 1 && order == ORDER_KEYS_FIRST) ||
			(pass == 1 && order == ORDER_KEYS_ONLY) ||
			(pass == 2 && order == ORDER_KEYS_LAST)
			){
                            preparedStatement = this.subscriber_Organization_Id.get_PreparedStatement_Value(preparedStatement, count++);
			}
			if((pass == 1 && order == ORDER_KEYS_FIRST) ||
			(pass == 1 && order == ORDER_KEYS_LAST) ||
			(pass == 1 && order == ORDER_KEYS_NEVER)
			){
                            preparedStatement = this.name.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.description.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.database_Server_URL.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.database_Name.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.database_Access_Login_Name.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.database_Access_Login_Password.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.destination_URL.get_PreparedStatement_Value(preparedStatement, count++);
			}
                    }
                }
                catch(DFException dfe){
                    throw new Database_Record_Exception(this.getClass().getName() + ": preparedStatement_Encode(): failed because DFException : " + dfe.getMessage());
                }
		return preparedStatement;
	}



	public synchronized Object resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Exception {
		Subscriber_Organization result;
               
		result = new Subscriber_Organization();
                
                try {
                    result.subscriber_Organization_Id.setValue_ResultSet(resultSet);
                    result.name.setValue_ResultSet(resultSet);
                    result.description.setValue_ResultSet(resultSet);
                    result.database_Server_URL.setValue_ResultSet(resultSet);
                    result.database_Name.setValue_ResultSet(resultSet);
                    result.database_Access_Login_Name.setValue_ResultSet(resultSet);
                    result.database_Access_Login_Password.setValue_ResultSet(resultSet);
                    result.destination_URL.setValue_ResultSet(resultSet);
                }
                catch(DFException dfe){
                    throw new Database_Record_Exception(this.getClass().getName() + ": resultSet_Row_Decode(): failed because DFException : " + dfe.getMessage());
                }
      		return result;
	}




	public Object[] get_Array_of_Records(int array_Size) {
		Subscriber_Organization[] subscriber_Organization_Records;
		subscriber_Organization_Records = new Subscriber_Organization[array_Size];
		return subscriber_Organization_Records;
	}

    
    
    

    public static Subscriber_Organization get_Subscriber_Organization(String subscriber_Organization_Id)
    throws Database_Front_Exception, Database_Record_Exception, DFException {
        Subscriber_Organization subscriber_Organization=null;
        Subscriber_Organization temp_Subscriber_Organization = new Subscriber_Organization();
        boolean debug_Mode=true;
        String message ="";
        
        Database_Front alt_Asp_Database_Front=null;
        
        Client_App_Constants alt_Asp_Client_App_Constants = new Client_App_Constants("jdbc:mysql://localhost/ALTASP", "ASP_admin", "master23er2indi");
      
        alt_Asp_Database_Front = new Database_Front(alt_Asp_Client_App_Constants);

        try{
            alt_Asp_Database_Front.open();
        }
        catch(Database_Front_Exception dbfe){
            if(debug_Mode){
                message = "Connection to ALTASP DataBase Failed. Database_Front_Exception :" + dbfe.getMessage();
                throw new Database_Front_Exception(message);
            }else{
                throw dbfe;
            }
        }
        
        
        try {
            temp_Subscriber_Organization.setSubscriber_Organization_Id(subscriber_Organization_Id);
            subscriber_Organization = (Subscriber_Organization) alt_Asp_Database_Front.getRecord(temp_Subscriber_Organization);
        }
        catch(Database_Front_Exception dbfe){
            if(debug_Mode){
                message = "Failed to find Subscriber_Organization in database. Database_Front_Exception :" + dbfe.getMessage();
                throw new Database_Front_Exception(message);
            }else{
                throw dbfe;
            }
        }
        catch(DFException dfe){
            if(debug_Mode){
                message = "Failed to find Subscriber_Organization in database. DFException :" + dfe.getMessage();
                throw new DFException(message);
            }else{
                throw dfe;
            }
        }
        
        
        
        try{
            alt_Asp_Database_Front.close();
        }
        catch(Database_Front_Exception dbfe){
            if(debug_Mode){
                message = "Close of Connection to ALTASP DataBase Failed. Database_Front_Exception :" + dbfe.getMessage();
                throw new Database_Front_Exception(message);
            }else{
                throw dbfe;
            }
        }
        
        return subscriber_Organization;
    }
    
    public Client_App_Constants get_Client_App_Constants_Constants(){
        return new Client_App_Constants(
        database_Server_URL.getValue() + "/" + database_Name.getValue(),
        database_Access_Login_Name.getValue(), database_Access_Login_Password.getValue());
    }
    
    public String getNext_Sort_Clause(int which_Index) throws Database_Front_Exception {
        switch(which_Index){
            case PRIMARY_INDEX:
                return "ORDER BY subscriber_Organization_Id ASC";
            default:
                throw new Database_Front_Exception(this.getClass().getName() + ".getNext_Sort_Clause(): unknown Index:"+which_Index);
        }
    }
    
    public String getNext_Where_Clause(int which_Index, int skip_Levels) throws Database_Front_Exception {
        switch(which_Index){
            case PRIMARY_INDEX:
                switch(skip_Levels){
                    case 0:
                        return "WHERE subscriber_Organization_Id > '"+ SQLUtil.encode(this.subscriber_Organization_Id.getValue()) + "'";
                    default:
                        return null;
                }
            default:
                throw new Database_Front_Exception(this.getClass().getName() + ".getNext_Sort_Clause(): unknown Index:"+which_Index);
        }
    }
 
    public String getSQL_Create_Table() {
            
        StringBuffer out = new StringBuffer();
        
        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        
        out.append("\t" + subscriber_Organization_Id.getSQL_Declaration());
        out.append("\t" + name.getSQL_Declaration());
        out.append("\t" + description.getSQL_Declaration());
        out.append("\t" + database_Server_URL.getSQL_Declaration());
        out.append("\t" + database_Name.getSQL_Declaration());
        out.append("\t" + database_Access_Login_Name.getSQL_Declaration());
        out.append("\t" + database_Access_Login_Password.getSQL_Declaration());
        out.append("\t" + destination_URL.getSQL_Declaration());
       
        
        
        out.append("        PRIMARY KEY  (`" + subscriber_Organization_Id.getField_Name() + "`)\n");
        out.append(") TYPE=InnoDB;\n");
        
        return (out.toString());
    }    
    
    
    
}
