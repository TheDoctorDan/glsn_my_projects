/*
 * User.java
 *
 * Created on March 17, 2005 3:23:42 PM CST
 */

/**
 *|------------------------------------------------------------------------------|
 *|       Copyright (c) 1985 thru 2000, 2001, 2002, 2003, 2004, 2005, 2006       |
 *|       American Liberator Technologies                                        |
 *|       All Rights Reserved                                                    |
 *|                                                                              |
 *|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
 *|       American Liberator Technologies                                        |
 *|       The copyright notice above does not evidence any                       |
 *|       actual or intended publication of such source code.                    |
 *|------------------------------------------------------------------------------|
 */

package com.amlibtech.login.data;

import com.amlibtech.data_fields.*;
import java.util.*;
import javax.servlet.http.*;

import com.amlibtech.web.data.*;
import com.amlibtech.util.*;
import com.amlibtech.web.servlet_exceptions.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web.common_list.*;
import com.amlibtech.database.*;
import com.amlibtech.security_Management.data.*;
import java.lang.reflect.*;


public class User extends Database_Record_Base {

    private	DFString	user_Id;
    private	DFString	first_Name;
    private	DFString	last_Name;
    private	DFString	password;
    private	DFString	description;
    private	DFTimeZone 	timeZone_Name;
    private	DFString	reference_Type;
    private	DFString	reference_Id;
    private     DFBoolean       obsolete;
    private     DFBoolean       asp_Admin;
    private     DFString        security_Profile_Id;
    private     DFE_Mail        email_Address;
    private	DFString_Enumerated login_Method;


    public static final int USER_ID_LEN = 20;



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



    //============== Begin jdbr Constructor Section ===========================


    // Empty Constructor
    /** Creates a new instance of User
     * @throws Database_Record_Constructor_Exception
     */
    public User() throws Database_Record_Constructor_Exception {
        super("User", "Users");
        try {
            this.user_Id = new DFString("user_Id", "User ID", 1, USER_ID_LEN);
            this.first_Name = new DFString("first_Name", "First Name", 1, 20);
            this.last_Name = new DFString("last_Name", "Last Name", 1, 20);
            this.password = new DFString("password", "Password", 1, 40);
            this.description = new DFString("description", "Description", 0, 100);
            this.timeZone_Name = new DFTimeZone("timeZone_Name", "TimeZone Name");
            this.reference_Type = new DFString("reference_Type", "Reference Type", 0, 30);
            this.reference_Id = new DFString("reference_Id", "Reference Id", 0, 30);

            this.obsolete =  new DFBoolean("obsolete", "Obsolete");
            this.asp_Admin =  new DFBoolean("asp_Admin", "ASP Admin");
            this.security_Profile_Id =  Security_Profile.DFInit_Security_Profile_Id();

            this.email_Address =  new DFE_Mail("email_Address", "E-Mail");

            this.login_Method = Subscriber_Organization_Portal.DFInit_Login_Method();
            this.login_Method.setValue(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_SYSTEM_USER]);

            this.setPrimary_Index_DFields(this.user_Id);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }




    // Keys Only Constructor
    /** Creates a new instance of User with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public User(String user_Id) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.user_Id.setValue(user_Id);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor(ID) : failed because DFException : " + dfe.getMessage());
        }
    }



    // NON SYSTEM_USER Constructor
    /** Creates a new instance of User with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public User(String login_Method, String reference_Id) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.login_Method.setValue(login_Method);
            this.reference_Id.setValue(reference_Id);
            this.user_Id.setValue(login_Method);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor(ID) : failed because DFException : " + dfe.getMessage());
        }
    }




    //============== End jdbr Constructor Section ===========================


    //============== Begin jdbr Generated Section ===========================



    /**
     * Returns name of Database table for these records.
     * @see Database_Front.
     * @return Table Name.
     */
    public String get_Table_Name() {
        return "user";
    }




    /** Used to check for reasons not to delete this record from table.
     * @throws Database_Record_Exception
     * @throws Database_Record_Constructor_Exception
     * @param database_Front used if routine needs to check database.
     * @param httpSession used if routine needs extra info from a session attribute.
     */
    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession httpSession) throws Database_Record_Exception, Database_Record_Constructor_Exception {
        return;
    }




    /**
     * Used by Database_Front to return many records.
     * @param array_Size is the size of the array required.
     * @return User[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        User[] user_Records;
        user_Records = new User[array_Size];
        return user_Records;
    }





    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
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
        out.append("\t" + obsolete.getSQL_Declaration());
        out.append("\t" + asp_Admin.getSQL_Declaration());
        out.append("\t" + security_Profile_Id.getSQL_Declaration());
        out.append("\t" + email_Address.getSQL_Declaration());
        out.append("\t" + login_Method.getSQL_Declaration(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_SYSTEM_USER]));


        out.append("        PRIMARY KEY  (`" + user_Id.getField_Name() + "`)\n");
        out.append(") TYPE=InnoDB;\n");

        return (out.toString());
    }




    /**
     * Used by Database_Front to sort records.
     * @param which_Index used to select which index to use.  PRIMARY_INDEX is always available.
     * @return SQL ORDER BY clause for selected index.
     * @throws Database_Front_Exception if selected index is invalid.
     */
    public String getNext_Sort_Clause(int which_Index) throws Database_Front_Exception {
        switch(which_Index){
            case PRIMARY_INDEX:
                return "ORDER BY user_Id ASC";
            default:
                throw new Database_Front_Exception(this.getClass().getName() + ".getNext_Sort_Clause(): unknown Index:"+which_Index);
        }
    }




    /**
     * Used by Database_Front to split matching records into small sets of records.
     * skip_Levels will increment as each prior level fails.
     * @param which_Index used to select which index to use.  PRIMARY_INDEX is always available.
     * @param skip_Levels used to control how many fields of the index are to be used.
     *  As last field of multi-field index maxes out, We want to allow next to last field to increase.
     * And then again on the next level.  Etc.
     * @return WHERE clause for which_Index and skip_Levels.
     * @return <code>null</code> if skip_Levels exceeds maximum number for selected index.
     * @return <code>null</code> if skip_Levels less than 0.
     * @throws Database_Front_Exception if selected index is invalid.
     */
    public String getNext_Where_Clause(int which_Index, int skip_Levels) throws Database_Front_Exception {
        switch(which_Index){
        case PRIMARY_INDEX:
            switch(skip_Levels){
            case 0:
                return "WHERE user_Id > '" + SQLUtil.encode(this.user_Id.toString()) + "' ";
            default:
                return null;
            }
        default:
            throw new Database_Front_Exception(this.getClass().getName() + ".getNext_Sort_Clause(): unknown Index:"+which_Index);
        }
    }





    /**
     * Used by Database_Front to encode fields into a SQL prepared statement.
     * The first section is for fields in Primary key.
     * See get_Key_Where_Clause() .
     * The second section is for the rest of the data fields.
     * @see get_Set_Clause().
     * @see also get_Into_Clause() and get_Values_Clause().
     * @param preparedStatement is the SQL statement with ? for values.
     * @param order indicates which groups of values in which order are in the preparedStatement.
     * order values can be:
     * <ul><li>ORDER_KEYS_FIRST used when index fields are at beginning of the preparedStatement. (addRecord(), INSERT INTO ...).
     * <li>ORDER_KEYS_LAST used when index fields are at end of the preparedStatement. (updateRecord(), UPDATE x SET ... WHERE ...).
     * <li>ORDER_KEYS_ONLY used when index fields are the only values in the preparedStatement. (is_Match(), getResultSet_Row(), Select * FROM x WHERE ..., deleteRecord(), DELETE FROM ...).
     * <li>ORDER_KEYS_NEVER used when index fields are not in the preparedStatement.
     * </ul>
     * @return preparedStatement with values inserted where ? were.
     * @throws Database_Record_Exception if any of the data_fields.DFBase Classes : get_PreparedStatement_Value() fails.
     */
    public synchronized java.sql.PreparedStatement preparedStatement_Encode(java.sql.PreparedStatement preparedStatement, int order)
    throws Database_Record_Exception {
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
                    preparedStatement = this.obsolete.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.asp_Admin.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.security_Profile_Id.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.email_Address.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.login_Method.get_PreparedStatement_Value(preparedStatement, count++);
                }
            }
        }
        catch(DFException dfe){
            throw new Database_Record_Exception(this.getClass().getName() + ": preparedStatement_Encode(): failed because DFException : " + dfe.getMessage());
        }

        return preparedStatement;
    }





    /**
     * Used by Database_Front to decode SQL resultSet into data fields of record.
     * @param resultSet is a ResultSet returned from PrepareStatement.executeUpdate().
     * @return User record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        User result;
        try {
            result = new User();

            result.user_Id.setValue_ResultSet(resultSet);
            result.first_Name.setValue_ResultSet(resultSet);
            result.last_Name.setValue_ResultSet(resultSet);
            result.password.setValue_ResultSet(resultSet);
            result.description.setValue_ResultSet(resultSet);
            result.timeZone_Name.setValue_ResultSet(resultSet);
            result.reference_Type.setValue_ResultSet(resultSet);
            result.reference_Id.setValue_ResultSet(resultSet);
            result.obsolete.setValue_ResultSet(resultSet);
            result.asp_Admin.setValue_ResultSet(resultSet);
            result.security_Profile_Id.setValue_ResultSet(resultSet);
            result.email_Address.setValue_ResultSet(resultSet);
            result.login_Method.setValue_ResultSet(resultSet);

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": resultSet_Row_Decode(): failed because DFException : " + dfe.getMessage());
        }

        return result;
    }





    /** Validate index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     */
    public synchronized void validate_Key_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix) throws Database_Record_Exception {
        int error_Count =0;

        error_Count += this.user_Id.validate_Request(prefix, httpServletRequest, suffix);

        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + ".validate_Key_Fields_Of_Request(): Invalid Key Data Field(s). " + error_Count + " errors.");

    }





    /** Validate non-index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     * @throws Database_Record_Constructor_Exception if a value could not be validated against a foriegn record.
     */
    public synchronized void validate_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix) throws Database_Record_Exception, Database_Record_Constructor_Exception {

        int error_Count =0;

        error_Count += this.first_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.last_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.password.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.description.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.timeZone_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.reference_Type.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.reference_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.obsolete.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.asp_Admin.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.security_Profile_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.email_Address.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.login_Method.validate_Request(prefix, httpServletRequest, suffix);

        if(error_Count==0)
            error_Count += validate_Record(database_Front);

        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + ".validate_Fields_Of_Request(): Invalid Data Field(s). " + error_Count + " errors.");


    }





    /** Returns a list of fields of this record. Set manually by programmer.
     * @return DFBase[]
     */
    public DFBase[] get_Field_List() {
        // programmer has entered ...
        LinkedList programmers_Field_List = new LinkedList();
        programmers_Field_List.add( user_Id );
        programmers_Field_List.add( first_Name );
        programmers_Field_List.add( last_Name );
        programmers_Field_List.add( password );
        programmers_Field_List.add( description );
        programmers_Field_List.add( timeZone_Name );
        programmers_Field_List.add( reference_Type );
        programmers_Field_List.add( reference_Id );
        programmers_Field_List.add( obsolete );
        programmers_Field_List.add( asp_Admin );
        programmers_Field_List.add( security_Profile_Id );
        programmers_Field_List.add( email_Address );
        programmers_Field_List.add( login_Method );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }



    /**
     * @return User.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue() fails.
     */
    public synchronized User copy_Of() throws Database_Record_Constructor_Exception {
        User result;
        try {
            result = new User();

            result.setUser_Id(this.user_Id.getValue());
            result.setFirst_Name(this.first_Name.getValue());
            result.setLast_Name(this.last_Name.getValue());
            result.setPassword(this.password.getValue());
            result.setDescription(this.description.getValue());
            result.setTimeZone_Name(this.timeZone_Name.getValue().getID());
            result.setReference_Type(this.reference_Type.getValue());
            result.setReference_Id(this.reference_Id.getValue());
            result.setObsolete(this.obsolete.getValue());
            result.setAsp_Admin(this.asp_Admin.getValue());
            result.setSecurity_Profile_Id(this.security_Profile_Id.getValue());
            result.setEmail_Address(this.email_Address.getValue());
            result.setLogin_Method(this.login_Method.getValue());

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": setValue(): failed because DFException : " + dfe.getMessage());
        }

        return result;
    }





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


    /**
     * Getter for property obsolete.
     * @return Value of property obsolete.
     */
    public DFBoolean getObsolete() {
        return obsolete;
    }


    /**
     * Setter for property obsolete.
     * @param obsolete New value of property obsolete.
     */
    public void setObsolete(Boolean obsolete) throws DFException {
        this.obsolete.setValue(obsolete);
    }


    /**
     * Getter for property asp_Admin.
     * @return Value of property asp_Admin.
     */
    public DFBoolean getAsp_Admin() {
        return asp_Admin;
    }


    /**
     * Setter for property asp_Admin.
     * @param asp_Admin New value of property asp_Admin.
     */
    public void setAsp_Admin(Boolean asp_Admin) throws DFException {
        this.asp_Admin.setValue(asp_Admin);
    }


    /**
     * Getter for property security_Profile_Id.
     * @return Value of property security_Profile_Id.
     */
    public DFString getSecurity_Profile_Id() {
        return security_Profile_Id;
    }


    /**
     * Setter for property security_Profile_Id.
     * @param security_Profile_Id New value of property security_Profile_Id.
     */
    public void setSecurity_Profile_Id(String security_Profile_Id) throws DFException {
        this.security_Profile_Id.setValue(security_Profile_Id);
    }


    /**
     * Getter for property email_Address.
     * @return Value of property email_Address.
     */
    public DFE_Mail getEmail_Address() {
        return email_Address;
    }


    /**
     * Setter for property email_Address.
     * @param email_Address New value of property email_Address.
     */
    public void setEmail_Address(String email_Address) throws DFException {
        this.email_Address.setValue(email_Address);
    }


    /**
     * Getter for property login_Method.
     * @return Value of property login_Method.
     */
    public DFString_Enumerated getLogin_Method() {
        return login_Method;
    }


    /**
     * Setter for property login_Method.
     * @param login_Method New value of property login_Method.
     */
    public void setLogin_Method(String login_Method) throws DFException {
        this.login_Method.setValue(login_Method);
    }


    //============== End jdbr Generated Section ===========================


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


    /** writes this User to session attribute  "i_Am_User"  */
    public void set_Session_Instance(HttpSession session){
        session.setAttribute("i_Am_User", this);
    }

    /**
     * Reads User from session attribute "i_Am_User".
     * @return A User instance of logged in user.
     * @throws if the session in invalid or
     * if no user has logged in.
     */
    public static User get_Session_Instance(HttpSession session)
    throws Database_Record_Exception {
        User temp_User;
        try {
            temp_User = (User) session.getAttribute("i_Am_User");
        }
        catch(IllegalStateException ise){
            session.getServletContext().log("Your Session has Timed Out.\n"+"Laucher FATAL Error :" + ise.getMessage(), ise);

            throw new Database_Record_Exception("Your Session has Timed Out.");
        }
        if(temp_User == null){
            session.getServletContext().log("No User is Logged in.\n"+"Laucher FATAL Error :");

            throw new Database_Record_Exception("No User is Logged in.");
        }
        return temp_User;
    }


    public String getFull_Name(){
        return first_Name.toString() +" "+ last_Name.toString();
    }


    /**
     *
     */
    public DFString getName_First_Then_Last() {
        DFString name;
        try {
            name = new DFString("name", "Name", 60);
            if(first_Name.getValue().length()!=0 || last_Name.getValue().length()!=0)
                name.setValue(first_Name.toString() + " " + last_Name.toString());
        }catch(DFException dfe){
            return null;
        }
        return name;
    }

    /**
     *
     */
    public DFString getName_Last_Then_First() {
        DFString name;
        try {
            name = new DFString("name", "Name", 60);
            if(first_Name.getValue().length()!=0 || last_Name.getValue().length()!=0)
                name.setValue(last_Name.toString() + ", " + first_Name.toString());
        }catch(DFException dfe){
            return null;
        }
        return name;
    }


    public static User get_Possible_User(Database_Front database_Front, String user_Id) {

        User user;
        try {
            user = new User();
        }catch(Database_Record_Constructor_Exception dbcre){
            return null;
        }

        if(user_Id.equals(""))
            return user;
        try {
            user.setLast_Name("No Such User");
        }
        catch(DFException dfe){
            ;
        }


        try {
            user = (User) database_Front.getRecord(new User(user_Id));
        }
        catch(Database_Front_Exception dbfe){
            ;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            ;
        }
        catch(Database_Record_Constructor_Exception dbcre){
            ;
        }
        return user;
    }


    public int validate_Record(Database_Front database_Front) throws Database_Record_Constructor_Exception {




        this.security_Profile_Id.setRequest_Error("");

        Security_Profile security_Profile;
        try {
            security_Profile = (Security_Profile) database_Front.getRecord(new Security_Profile(this.security_Profile_Id.getValue()));
        }catch(Database_Front_Exception dfe){
            this.security_Profile_Id.setRequest_Error("Database Error.");
            return 1;
        }catch(Database_Front_No_Results_Exception dfe){
            this.security_Profile_Id.setRequest_Error("Must specify a Security Profile.");
            return 1;
        }




        return 0;

    }


    public boolean is_A_System_User(){
        if(this.login_Method.getValue().equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_SYSTEM_USER]))
            return true;
        else
            return false;
    }

    public boolean is_A_Customer(){
        if(this.login_Method.getValue().equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_CUSTOMER]))
            return true;
        else
            return false;
    }

    public boolean is_A_Vendor(){
        if(this.login_Method.getValue().equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_VENDOR]))
            return true;
        else
            return false;
    }

    public boolean is_A_Guest(){
        if(this.login_Method.getValue().equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_GUEST]))
            return true;
        else
            return false;
    }

    public boolean is_A_Teacher(){
        if(this.login_Method.getValue().equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_TEACHER]))
            return true;
        else
            return false;
    }

    public boolean is_A_Student(){
        if(this.login_Method.getValue().equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_STUDENT]))
            return true;
        else
            return false;
    }



}
