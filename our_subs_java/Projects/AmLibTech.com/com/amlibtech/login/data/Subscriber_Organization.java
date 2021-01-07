/*
 * Subscriber_Organization.java
 *
 * Created on March 17, 2005 3:02:11 PM CST
 */

/**
 * @author  dgleeson
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
import com.amlibtech.web.servlet_exceptions.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import com.amlibtech.web.util.*;
import java.lang.reflect.*;



/**
 * List of Subscribing Organization and info to access their database and directories
 * @author  dgleeson
 */
public class Subscriber_Organization extends Database_Record_Base {


    // Declaration Section

    /** (table Key), ID for Subscriber Organization, length 1-30*/
    private	DFString	subscriber_Organization_Id;
    /** name for Subscriber Organization, length 1-38 */
    private	DFString	name;
    /** Description of Subscriber Organization, length 0-100 */
    private	DFString	description;
    /** URL of Database Server, length 1-200 */
    private	DFString	database_Server_URL;
    /** Name of the database on the server, length 1-100 */
    private	DFString	database_Name;
    /** Login name on the server to access the database, length 1-30 */
    private	DFString	database_Access_Login_Name;
    /** Password on the server for login name */
    private	DFString	database_Access_Login_Password;
    /** Full path name on localhost for the website's root directory */
    private     DFString        home_Directory;

    /**
     * Maximum Length of Subscriber_Organization_Id.
     */
    public static final int SUBSCRIBER_ORGANIZATION_ID_LEN = 30;

    public static DFString DFInit_Subscriber_Organization_Id()
    throws DFException {
        return new DFString("subscriber_Organization_Id", "Subscriber Organization ID#", 1, SUBSCRIBER_ORGANIZATION_ID_LEN);
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

    //============== Begin jdbr Constructor Section ===========================


    // Empty Constructor
    /** Creates a new instance of Subscriber_Organization
     * @throws Database_Record_Constructor_Exception
     */
    public Subscriber_Organization() throws Database_Record_Constructor_Exception {
        super("Subscriber Organization", "Subscriber Organizations");

        try {
            this.subscriber_Organization_Id = DFInit_Subscriber_Organization_Id();

            this.name = new DFString("name", "Name", 1, 38);
            this.description = new DFString("description", "Description", 100);
            this.database_Server_URL = new DFString("database_Server_URL", "Database Server URL", 1, 200);
            this.database_Name = new DFString("database_Name", "Database Name", 1, 100);
            this.database_Access_Login_Name = new DFString("database_Access_Login_Name", "Database Access Login Name", 1, 30);
            this.database_Access_Login_Password = new DFString("database_Access_Login_Password", "Database Access Login Password", 30);
            this.home_Directory =  new DFString("home_Directory", "Home Directory", 100);

            this.setPrimary_Index_DFields(this.subscriber_Organization_Id);
            
            this.database_Server_URL.setValue("jdbc:mysql://localhost");

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
        
    }




    // Keys Only Constructor
    /** Creates a new instance of Subscriber_Organization with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Subscriber_Organization(String subscriber_Organization_Id) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.subscriber_Organization_Id.setValue(subscriber_Organization_Id);

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
        return "subscriber_organization";
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
     * @return Subscriber_Organization[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Subscriber_Organization[] subscriber_Organization_Records;
        subscriber_Organization_Records = new Subscriber_Organization[array_Size];
        return subscriber_Organization_Records;
    }





    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
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
        out.append("\t" + home_Directory.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + subscriber_Organization_Id.getField_Name() + "`)\n");
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
                return "ORDER BY subscriber_Organization_Id ASC";
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
                return "WHERE subscriber_Organization_Id > '" + SQLUtil.encode(this.subscriber_Organization_Id.toString()) + "' ";
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
                    preparedStatement = this.home_Directory.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Subscriber_Organization record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Subscriber_Organization result;
        try {
            result = new Subscriber_Organization();

            result.subscriber_Organization_Id.setValue_ResultSet(resultSet);
            result.name.setValue_ResultSet(resultSet);
            result.description.setValue_ResultSet(resultSet);
            result.database_Server_URL.setValue_ResultSet(resultSet);
            result.database_Name.setValue_ResultSet(resultSet);
            result.database_Access_Login_Name.setValue_ResultSet(resultSet);
            result.database_Access_Login_Password.setValue_ResultSet(resultSet);
            result.home_Directory.setValue_ResultSet(resultSet);

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

        error_Count += this.subscriber_Organization_Id.validate_Request(prefix, httpServletRequest, suffix);

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

        error_Count += this.name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.description.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.database_Server_URL.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.database_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.database_Access_Login_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.database_Access_Login_Password.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.home_Directory.validate_Request(prefix, httpServletRequest, suffix);


        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + ".validate_Fields_Of_Request(): Invalid Data Field(s). " + error_Count + " errors.");


    }





    /** Returns a list of fields of this record. Set manually by programmer.
     * @return DFBase[]
     */
    public DFBase[] get_Field_List() {
        // programmer has entered ...
        LinkedList programmers_Field_List = new LinkedList();
        programmers_Field_List.add( subscriber_Organization_Id );
        programmers_Field_List.add( name );
        programmers_Field_List.add( description );
        programmers_Field_List.add( database_Server_URL );
        programmers_Field_List.add( database_Name );
        programmers_Field_List.add( database_Access_Login_Name );
        programmers_Field_List.add( database_Access_Login_Password );
        programmers_Field_List.add( home_Directory );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }



    /**
     * @return Subscriber_Organization.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue() fails.
     */
    public synchronized Subscriber_Organization copy_Of() throws Database_Record_Constructor_Exception {
        Subscriber_Organization result;
        try {
            result = new Subscriber_Organization();

            result.setSubscriber_Organization_Id(this.subscriber_Organization_Id.getValue());
            result.setName(this.name.getValue());
            result.setDescription(this.description.getValue());
            result.setDatabase_Server_URL(this.database_Server_URL.getValue());
            result.setDatabase_Name(this.database_Name.getValue());
            result.setDatabase_Access_Login_Name(this.database_Access_Login_Name.getValue());
            result.setDatabase_Access_Login_Password(this.database_Access_Login_Password.getValue());
            result.setHome_Directory(this.home_Directory.getValue());

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": setValue(): failed because DFException : " + dfe.getMessage());
        }

        return result;
    }





    /**
     * Getter for property subscriber_Organization_Id.
     * @return Value of property subscriber_Organization_Id.
     */
    public DFString getSubscriber_Organization_Id() {
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
     * Getter for property home_Directory.
     * @return Value of property home_Directory.
     */
    public DFString getHome_Directory() {
        return home_Directory;
    }


    /**
     * Setter for property home_Directory.
     * @param home_Directory New value of property home_Directory.
     */
    public void setHome_Directory(String home_Directory) throws DFException {
        this.home_Directory.setValue(home_Directory);
    }


    //============== End jdbr Generated Section ===========================


    public static Subscriber_Organization get_Subscriber_Organization(String subscriber_Organization_Id)
    throws Database_Front_Exception, Database_Record_Constructor_Exception, Database_Front_No_Results_Exception {
        Subscriber_Organization subscriber_Organization=null;
        boolean debug_Mode=true;
        String message ="";

        Database_Front alt_Asp_Database_Front=null;

        Client_App_Constants alt_Asp_Client_App_Constants = ALT_ASP_Client_App_Constants.getALT_ASP_Client_App_Constants();

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
            subscriber_Organization = (Subscriber_Organization) alt_Asp_Database_Front.getRecord(new Subscriber_Organization(subscriber_Organization_Id));
        }
        catch(Database_Front_Exception dbfe){
            if(debug_Mode){
                message = "Failed to find Subscriber_Organization in database. Database_Front_Exception :" + dbfe.getMessage();
                throw new Database_Front_Exception(message);
            }else{
                throw dbfe;
            }
        }
        catch(Database_Front_No_Results_Exception dbfe){
            if(debug_Mode){
                message = "Failed to find Subscriber_Organization in database. Database_Front_No_Results_Exception :" + dbfe.getMessage();
                throw new Database_Front_No_Results_Exception(message);
            }else{
                throw dbfe;
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




    /** writes this Subscriber_Organization to session attribute  "i_Am_Subscriber_Organization"  */
    public void set_Session_Instance(HttpSession session){
        session.setAttribute("i_Am_Subscriber_Organization", this);
    }

    /**
     * Reads User from session attribute "i_Am_Subscriber_Organization".
     * @return A Subscriber_Organization instance of logged in user.
     * @throws if the session in invalid or
     * if no user has logged in.
     */
    public static Subscriber_Organization get_Session_Instance(HttpSession session)
    throws Database_Record_Exception {
        Subscriber_Organization temp_Subscriber_Organization;
        try {
            temp_Subscriber_Organization = (Subscriber_Organization) session.getAttribute("i_Am_Subscriber_Organization");
        }
        catch(IllegalStateException ise){
            throw new Database_Record_Exception("Your Session has Timed Out.");
        }
        if(temp_Subscriber_Organization == null){
            throw new Database_Record_Exception("No Organization has been selected for this session.");
        }
        return temp_Subscriber_Organization;
    }




}
