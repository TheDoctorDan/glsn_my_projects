/*
 * User.java
 *
 * Created on March 17, 2005 3:23:42 PM CST
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
import com.amlibtech.util.*;
import com.amlibtech.web.servlet_exceptions.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web.common_list.*;
import net.amlibtech.ASP.data.*;
import com.amlibtech.database.*;
import java.lang.reflect.*;


public class User extends Database_Record_Base implements Database_Record_Interface {


    // Declaration Section 

    private	DFString	user_Id;
    private	DFString	first_Name;
    private	DFString	last_Name;
    private	DFString	password;
    private	DFString	description;
    private	DFTimeZone 	timeZone_Name;
    private	DFString	reference_Type;
    private	DFString	reference_Id;


    public static final int USER_ID_LEN = 20;





    // Empty Constructor Section 

    public	User() throws Database_Record_Exception {
        super("User", "Users");

        try {
        this.user_Id = new DFString("user_Id", "User ID", 1, USER_ID_LEN);
        this.first_Name = new DFString("first_Name", "First Name", 1, 20);
        this.last_Name = new DFString("last_Name", "Last Name", 1, 20);
        this.password = new DFString("password", "Password", 1, 40);
        this.description = new DFString("description", "Description", 1, 100);
        this.timeZone_Name = new DFTimeZone("timeZone_Name", "TimeZone Name");
        this.reference_Type = new DFString("reference_Type", "Reference Type", 1, 30);
        this.reference_Id = new DFString("reference_Id", "Reference Id", 1, 30);
	}
        catch(DFException dfe){
            throw new Database_Record_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
           
    }



    // Key Fields Constructor Section 

    public User(String user_Id) throws Database_Record_Exception {
	this();
        try{
            this.setUser_Id(user_Id);
        }
        catch(DFException dfe){
            throw new Database_Record_Exception(this.getClass().getName() + ": constructor(ID) : failed because DFException : " + dfe.getMessage());
        }
    }



    // Getter and Setter Section 


    /**
     * Getter for property user_Id.
     * @return Value of property user_Id.
     */
    public DFString getUser_Id() {
        return user_Id;
    }    
   
    
    
     /**
     * Setter for property user_Id.
     * @param user_Id New value of property user_Id.
     */
    public void setUser_Id(String user_Id) throws DFException {
        this.user_Id.setValue(user_Id);
    }
    
    /**
     * Getter for property first_Name.
     * @return Value of property first_Name.
     */
    public DFString getFirst_Name() {
        return first_Name;
    }
    
    /**
     * Setter for property first_Name.
     * @param first_Name New value of property first_Name.
     */
    public void setFirst_Name(String first_Name) throws DFException {
        this.first_Name.setValue(first_Name);
    }
    
    /**
     * Getter for property last_Name.
     * @return Value of property last_Name.
     */
    public DFString getLast_Name() {
        return last_Name;
    }
    
    /**
     * Setter for property last_Name.
     * @param last_Name New value of property last_Name.
     */
    public void setLast_Name(String last_Name) throws DFException {
        this.last_Name.setValue(last_Name);
    }
    
    /**
     * Getter for property password.
     * @return Value of property password.
     */
    public DFString getPassword() {
        return password;
    }
    
    /**
     * Setter for property password.
     * @param password New value of property password.
     */
    public void setPassword(String password) throws DFException {
        this.password.setValue(password);
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
     * Getter for property timeZone_Name.
     * @return Value of property timeZone_Name.
     */
    public DFTimeZone getTimeZone_Name() {
        return timeZone_Name;
    }
    
    /**
     * Setter for property timeZone_Name.
     * @param timeZone_Name New value of property timeZone_Name.
     */
    public void setTimeZone_Name(String timeZone_Name) throws DFException {
        this.timeZone_Name.setID_Str(timeZone_Name);
    }
    
    /**
     * Getter for property reference_Type.
     * @return Value of property reference_Type.
     */
    public DFString getReference_Type() {
        return reference_Type;
    }
    
    /**
     * Setter for property reference_Type.
     * @param reference_Type New value of property reference_Type.
     */
    public void setReference_Type(String reference_Type) throws DFException {
        this.reference_Type.setValue(reference_Type);
    }
    
    /**
     * Getter for property reference_Id.
     * @return Value of property reference_Id.
     */
    public DFString getReference_Id() {
        return reference_Id;
    }
    
    /**
     * Setter for property reference_Id.
     * @param reference_Id New value of property reference_Id.
     */
    public void setReference_Id(String reference_Id) throws DFException {
        this.reference_Id.setValue(reference_Id);
    }
    
   

	// Process Key Fields Section 

    public static synchronized User process_Key_Fields_Request(HttpServletRequest request, Database_Front database_Front)
    throws  Database_Record_Exception {
        HttpSession session = request.getSession();
        String message = "";
        
        User user_Keys = new User(null);
        
        user_Keys.validate_Key_Fields_Of_Request(request, database_Front);
        
        
        if(user_Keys.getUser_Id()==null ||
        user_Keys.getUser_Id().getValue()==null ||
        user_Keys.getUser_Id().getValue().trim().equals("") ) {
            throw new Database_Record_Exception("User: process_Key_Fields_Request(): No user_Id passed to servlet.");
        }
        
        return user_Keys;
    }
    


	// Key Fields Validation Section 

	public synchronized void validate_Key_Fields_Of_Request(HttpServletRequest request, Database_Front database_Front)
	throws  Database_Record_Exception {
                int error_Count=0;

                error_Count += this.user_Id.validate_Request(request);

                if(error_Count != 0)
                    throw new Database_Record_Exception (this.getClass().getName() + "validate_Key_Fields_Of_Request(): Invalid Data Field(s).");
		
	}




    // Field Validation Section 

	public synchronized void validate_Fields_Of_Request(HttpServletRequest request, Database_Front database_Front)
	throws  Database_Record_Exception {
            int error_Count=0;        

            error_Count += this.first_Name.validate_Request(request);
            error_Count += this.last_Name.validate_Request(request);
            error_Count += this.password.validate_Request(request);
            error_Count += this.description.validate_Request(request);
            error_Count += this.timeZone_Name.validate_Request(request);
            error_Count += this.reference_Type.validate_Request(request);
            error_Count += this.reference_Id.validate_Request(request);

            if(error_Count != 0)
                throw new Database_Record_Exception (this.getClass().getName() + "validate_Fields_Of_Request(): Invalid Data Field(s).");
       
	}




    // Is Delete Allowed Section 


    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession session)
    {
    	return;
    }

    // SQL Section 

   public String get_Table_Name() {
       return "user";
   }

   public String get_First_Field_Name() {
       return "user_Id";
   }
   
   public String get_Into_Clause() {
       return "( user_Id, first_Name, last_Name, password, description, timeZone_Name, reference_Type, reference_Id )";
   }
   
   public String get_Key_Where_Clause() {
       	return "user_Id = ? ";
   }
   
   public String get_Set_Clause() {
       	return "first_Name = ?, last_Name = ?, password = ?, description = ?, timeZone_Name = ?, reference_Type = ?, reference_Id = ?";

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
                            preparedStatement = this.user_Id.get_PreparedStatement_Value(preparedStatement, count++);
			}
			if((pass == 1 && order == ORDER_KEYS_FIRST) ||
			(pass == 1 && order == ORDER_KEYS_LAST) ||
			(pass == 1 && order == ORDER_KEYS_NEVER)
			){
                                                        
                            preparedStatement = this.first_Name.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.last_Name.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.password.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.description.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.timeZone_Name.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.reference_Type.get_PreparedStatement_Value(preparedStatement, count++);
                            preparedStatement = this.reference_Id.get_PreparedStatement_Value(preparedStatement, count++);

                            
			}
                    }
		}
                catch(DFException dfe){
                    throw new Database_Record_Exception(this.getClass().getName() + ": preparedStatement_Encode(): failed because DFException : " + dfe.getMessage());
                }
		return preparedStatement;
	}



	public synchronized Object resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Exception {
		User result;
		result = new User();
                
                try {
                    result.user_Id.setValue_ResultSet(resultSet);
                    result.first_Name.setValue_ResultSet(resultSet);
                    result.last_Name.setValue_ResultSet(resultSet);
                    result.password.setValue_ResultSet(resultSet);
                    result.description.setValue_ResultSet(resultSet);
                    result.timeZone_Name.setValue_ResultSet(resultSet);
                    result.reference_Type.setValue_ResultSet(resultSet);
                    result.reference_Id.setValue_ResultSet(resultSet);

                    
                }
                catch(DFException dfe){
                    throw new Database_Record_Exception(this.getClass().getName() + ": resultSet_Row_Decode(): failed because DFException : " + dfe.getMessage());
                }
                
		return result;
	}


   
   
   
   public Object[] get_Array_of_Records(int array_Size) {
       User[] user_Records;
       user_Records = new User[array_Size];
       return user_Records;
   }
   

   public static String get_Logged_In_TimeZone_Name(HttpSession session){
        User i_Am_User= (User) session.getAttribute("s_i_Am_User");
        if(i_Am_User ==null){
            return "CST6CDT";
        }else{
            return i_Am_User.getTimeZone_Name().getID_Str();
        }
    }
   
   public static TimeZone get_Logged_In_TimeZone(HttpSession session){
        User i_Am_User= (User) session.getAttribute("s_i_Am_User");
        if(i_Am_User ==null){
            return TimeZone.getDefault();
        }else{
            return i_Am_User.getTimeZone_Name().getValue();
        }
    }
   
   
   public String getNext_Sort_Clause(int which_Index) throws Database_Front_Exception {
        switch(which_Index){
            case PRIMARY_INDEX:
                return "ORDER BY user_Id ASC";
            default:
                throw new Database_Front_Exception(this.getClass().getName() + ".getNext_Sort_Clause(): unknown Index:"+which_Index);
        }
    }
    
    public String getNext_Where_Clause(int which_Index, int skip_Levels) throws Database_Front_Exception {
        switch(which_Index){
            case PRIMARY_INDEX:
                switch(skip_Levels){
                    case 0:
                        return "WHERE user_Id > '"+ SQLUtil.encode(this.user_Id.getValue()) + "'";
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
        
        out.append("\t" + user_Id.getSQL_Declaration());
        out.append("\t" + first_Name.getSQL_Declaration());
        out.append("\t" + last_Name.getSQL_Declaration());
        out.append("\t" + password.getSQL_Declaration());
        out.append("\t" + description.getSQL_Declaration());
        out.append("\t" + timeZone_Name.getSQL_Declaration());
        out.append("\t" + reference_Type.getSQL_Declaration());
        out.append("\t" + reference_Id.getSQL_Declaration());
        
        out.append("        PRIMARY KEY  (`" + user_Id.getField_Name() + "`)\n");
        out.append(") TYPE=InnoDB;\n");
        
        return (out.toString());
    }    
    
   
}
