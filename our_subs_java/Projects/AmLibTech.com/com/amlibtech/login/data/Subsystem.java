/*
 * Subsystem.java
 *
 * Created on July 14, 2006, 1:12 PM
 */

package com.amlibtech.login.data;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Subsystem extends Database_Record_Base {
    
    private DFString    subsystem_Id;
    private DFString    description;
    private DFString    package_Name;
    private DFString    servlet_Name;
    
    
    /**
     * Length of subsystem_Id.
     */
    public static final int SUBSYSTEM_ID_LEN = 60;
    
    public static DFString DFInit_Subsystem_Id()
    throws DFException {
        return new DFString("subsystem_Id", "Subsystem Id", SUBSYSTEM_ID_LEN);
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
    /** Creates a new instance of Subsystem
     * @throws Database_Record_Exception
     */
    public Subsystem() throws Database_Record_Constructor_Exception {
        super("Subsystem", "Subsystems");
        try {
            this.subsystem_Id =  DFInit_Subsystem_Id();
            
            this.description =  new DFString("description", "Description", 30);
            this.package_Name =  new DFString("package_Name", "Package Name", 90);
            this.servlet_Name =  new DFString("servlet_Name", "Servlet Name", 60);
            
            this.setPrimary_Index_DFields(this.subsystem_Id);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }
    
    
    
    
    // Keys Only Constructor
    /** Creates a new instance of Subsystem with key fields set to passed values.
     * @throws Database_Record_Exception
     */
    public Subsystem(String subsystem_Id) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.subsystem_Id.setValue(subsystem_Id);
            
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
        return "subsystem";
    }
    
    
    
    
    /** Used to check for reasons not to delete this record from table.
     * @throws Database_Record_Exception
     * @param database_Front used if routine needs to check database.
     * @param httpSession used if routine needs extra info from a session attribute.
     */
    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession httpSession) throws Database_Record_Exception {
        return;
    }
    
    
    
    
    /**
     * Used by Database_Front to return many records.
     * @param array_Size is the size of the array required.
     * @return Subsystem[] the size of array_Size as an Object[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Subsystem[] subsystem_Records;
        subsystem_Records = new Subsystem[array_Size];
        return subsystem_Records;
    }
    
    
    
    
    
    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {
        
        StringBuffer out = new StringBuffer();
        
        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + subsystem_Id.getSQL_Declaration());
        out.append("\t" + description.getSQL_Declaration());
        out.append("\t" + package_Name.getSQL_Declaration());
        out.append("\t" + servlet_Name.getSQL_Declaration());
        
        
        out.append("        PRIMARY KEY  (`" + subsystem_Id.getField_Name() + "`)\n");
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
                return "ORDER BY subsystem_Id ASC";
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
                        return "WHERE subsystem_Id > '" + SQLUtil.encode(this.subsystem_Id.toString()) + "' ";
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
                    preparedStatement = this.subsystem_Id.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){
                    
                    preparedStatement = this.description.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.package_Name.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.servlet_Name.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Subsystem record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Subsystem result;
        try {
            result = new Subsystem();
            
            result.subsystem_Id.setValue_ResultSet(resultSet);
            result.description.setValue_ResultSet(resultSet);
            result.package_Name.setValue_ResultSet(resultSet);
            result.servlet_Name.setValue_ResultSet(resultSet);
            
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
     * @except throws Database_Record_Exception if a value is invalid.
     */
    public synchronized void validate_Key_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix) throws Database_Record_Exception {
        int error_Count =0;
        
        error_Count += this.subsystem_Id.validate_Request(prefix, httpServletRequest, suffix);
        
        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + "validate_Key_Fields_Of_Request(): Invalid Key Data Field(s).");
        
    }
    
    
    
    
    
    /** Validate non-index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @except throws Database_Record_Exception if a value is invalid.
     */
    public synchronized void validate_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix) throws Database_Record_Exception {
        
        int error_Count =0;
        
        error_Count += this.description.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.package_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.servlet_Name.validate_Request(prefix, httpServletRequest, suffix);
        
        
        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + "validate_Fields_Of_Request(): Invalid Data Field(s).");
        
        
    }
    
    
    
    
    
    /** Returns a list of fields of this record. Set manually by programmer.
     * @return DFBase[]
     */
    public DFBase[] get_Field_List() {
        // programmer has entered ...
        LinkedList programmers_Field_List = new LinkedList();
        programmers_Field_List.add( subsystem_Id );
        programmers_Field_List.add( description );
        programmers_Field_List.add( package_Name );
        programmers_Field_List.add( servlet_Name );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }
    
    
    
    /**
     * Getter for property subsystem_Id.
     * @return Value of property subsystem_Id.
     */
    public DFString getSubsystem_Id() {
        return subsystem_Id;
    }
    
    
    /**
     * Setter for property subsystem_Id.
     * @param subsystem_Id New value of property subsystem_Id.
     */
    public void setSubsystem_Id(String subsystem_Id) throws DFException {
        this.subsystem_Id.setValue(subsystem_Id);
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
     * Getter for property package_Name.
     * @return Value of property package_Name.
     */
    public DFString getPackage_Name() {
        return package_Name;
    }
    
    
    /**
     * Setter for property package_Name.
     * @param package_Name New value of property package_Name.
     */
    public void setPackage_Name(String package_Name) throws DFException {
        this.package_Name.setValue(package_Name);
    }
    
    
    /**
     * Getter for property servlet_Name.
     * @return Value of property servlet_Name.
     */
    public DFString getServlet_Name() {
        return servlet_Name;
    }
    
    
    /**
     * Setter for property servlet_Name.
     * @param servlet_Name New value of property servlet_Name.
     */
    public void setServlet_Name(String servlet_Name) throws DFException {
        this.servlet_Name.setValue(servlet_Name);
    }
    
    
    //============== End jdbr Generated Section ===========================
    
    
    public static String[] get_Subscriber_Installed_Subsystems(HttpSession session)
    throws Database_Front_Exception, Database_Record_Exception, Database_Record_Constructor_Exception {
        Database_Front database_Front = Database_Front.get_Session_Instance(session);
        
        Subscriber_Organization subscriber_Organization= Subscriber_Organization.get_Session_Instance(session);
        Subscriber_Organization_Portal subscriber_Organization_Portal = Subscriber_Organization_Portal.get_Session_Instance(session);
        
        Subscriber_Organization_Portal_Subsystem[] subscriber_Organization_Portal_Subsystems;
        
        String where_Clause = " WHERE " +
        subscriber_Organization.getSubscriber_Organization_Id().toWhere_Clause() + " AND " +
        subscriber_Organization_Portal.getPortal_Name().toWhere_Clause();
        
        subscriber_Organization_Portal_Subsystems = (Subscriber_Organization_Portal_Subsystem[]) database_Front.getMany_Records(new Subscriber_Organization_Portal_Subsystem(), where_Clause);
        
        if(subscriber_Organization_Portal_Subsystems != null){
            String[] subsystem_Ids = new String[subscriber_Organization_Portal_Subsystems.length];
            for(int i=0; i<subscriber_Organization_Portal_Subsystems.length; i++){
                subsystem_Ids[i] = subscriber_Organization_Portal_Subsystems[i].getSubsystem_Id().getValue();
            }
            return subsystem_Ids;
        }else{
            return null;
        }
    }
    
    
    
    public static void set_Session_Instance_Of_Installed_Subsystems(HttpSession session)
    throws Database_Front_Exception, Database_Record_Exception, Database_Record_Constructor_Exception {
        Database_Front database_Front = Database_Front.get_Session_Instance(session);
        
        Subscriber_Organization subscriber_Organization= Subscriber_Organization.get_Session_Instance(session);
        Subscriber_Organization_Portal subscriber_Organization_Portal = Subscriber_Organization_Portal.get_Session_Instance(session);
        
        Subscriber_Organization_Portal_Subsystem[] subscriber_Organization_Portal_Subsystems;
        
        String where_Clause = " WHERE " +
        subscriber_Organization.getSubscriber_Organization_Id().toWhere_Clause() + " AND " +
        subscriber_Organization_Portal.getPortal_Name().toWhere_Clause();
        
        subscriber_Organization_Portal_Subsystems = (Subscriber_Organization_Portal_Subsystem[]) database_Front.getMany_Records(new Subscriber_Organization_Portal_Subsystem(), where_Clause);
        
        if(subscriber_Organization_Portal_Subsystems != null){
            String[] subsystem_Ids = new String[subscriber_Organization_Portal_Subsystems.length];
            for(int i=0; i<subscriber_Organization_Portal_Subsystems.length; i++){
                subsystem_Ids[i] = subscriber_Organization_Portal_Subsystems[i].getSubsystem_Id().getValue();
            }
            session.setAttribute("Installed_Subsystem_Ids", subsystem_Ids);
        }else{
            session.setAttribute("Installed_Subsystem_Ids", null);
        }
    }
    
    
    
    public static String[] get_Session_Instance_Of_Installed_Subsystems(HttpSession session)
    throws Database_Front_Exception, Database_Record_Exception, Database_Record_Constructor_Exception {
        
        String[] subsystem_Ids;
        subsystem_Ids = (String[])session.getAttribute("Installed_Subsystem_Ids");
        if(subsystem_Ids==null){
            set_Session_Instance_Of_Installed_Subsystems(session);
            subsystem_Ids = (String[])session.getAttribute("Installed_Subsystem_Ids");
        }
        return subsystem_Ids;
    }
    
    public static boolean is_Subsystem_Installed(HttpSession session, String subsystem_Id){
        String[] subsystem_Ids;
        try {
            subsystem_Ids = get_Session_Instance_Of_Installed_Subsystems(session);
        }catch(Database_Record_Exception dbre){
            return false;
        }
        catch(Database_Record_Constructor_Exception dbre){
            return false;
        }
        catch(Database_Front_Exception dbfe){
            return false;
        }
        
        if(subsystem_Ids != null){
            for(int i=0; i<subsystem_Ids.length;i++){
                if(subsystem_Id.equals(subsystem_Ids[i]))
                    return true;
            }
            return false;
        }else{
            return false;
        }
    }
    
    
    public static Subsystem get_Subsystem(String subsystem_Id)
    throws Database_Front_Exception, Database_Record_Constructor_Exception, Database_Front_No_Results_Exception {
        Subsystem subsystem=null;
        boolean debug_Mode=true;
        String message ="";
        
        Database_Front alt_Asp_Database_Front=null;
        
        //Client_App_Constants alt_Asp_Client_App_Constants = new Client_App_Constants("jdbc:mysql://localhost/ALTASP", "ASP_admin", "master23er2indi");
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
            subsystem = (Subsystem) alt_Asp_Database_Front.getRecord(new Subsystem(subsystem_Id));
        }
        catch(Database_Front_Exception dbfe){
            if(debug_Mode){
                message = "Failed to find Subsystem in database. Database_Front_Exception :" + dbfe.getMessage();
                throw new Database_Front_Exception(message);
            }else{
                throw dbfe;
            }
        }
        catch(Database_Front_No_Results_Exception dbfe){
            if(debug_Mode){
                message = "Failed to find Subsystem in database. Database_Front_No_Results_Exception :" + dbfe.getMessage();
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
        
        return subsystem;
    }
    
    
    
}
