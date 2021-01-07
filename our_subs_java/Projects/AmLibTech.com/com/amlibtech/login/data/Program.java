/*
 * Program.java
 *
 * Created on July 14, 2006, 2:04 PM
 * revised on February 16, 2007, 1:28 PM added maintenance_Of_Table_Name, get_Maint_Program_Of_Table()
 */

package com.amlibtech.login.data;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import com.amlibtech.web_Processes.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Program extends Database_Record_Base {
    
    private DFString program_Name;
    private DFString program_Title;
    private DFString subsystem_Id;
    private DFString maintenance_Of_Table_Name;
    
    public static int PROGRAM_NAME_LEN =60;
    public static int TABLE_NAME_LEN =60;
    
    public static DFString DFInit_Program_Name()
    throws DFException {
        return new DFString("program_Name", "Program Name", 1, PROGRAM_NAME_LEN);
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
    /** Creates a new instance of Program
     * @throws Database_Record_Constructor_Exception
     */
    public Program() throws Database_Record_Constructor_Exception {
        super("Program", "Programs");
        try {
            this.program_Name = DFInit_Program_Name();
            this.program_Title =  new DFString("program_Title", "Title", 1, 80);
            this.subsystem_Id =  Subsystem.DFInit_Subsystem_Id();
            this.maintenance_Of_Table_Name =  new DFString("maintenance_Of_Table_Name", "Maintenance Of Table Name", TABLE_NAME_LEN);
            
            this.setPrimary_Index_DFields(this.program_Name);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }
    
    
    
    
    // Keys Only Constructor
    /** Creates a new instance of Program with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Program(String program_Name) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.program_Name.setValue(program_Name);
            
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
        return "program";
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
     * @return Program[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Program[] program_Records;
        program_Records = new Program[array_Size];
        return program_Records;
    }
    
    
    
    
    
    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {
        
        StringBuffer out = new StringBuffer();
        
        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + program_Name.getSQL_Declaration());
        out.append("\t" + program_Title.getSQL_Declaration());
        out.append("\t" + subsystem_Id.getSQL_Declaration());
        out.append("\t" + maintenance_Of_Table_Name.getSQL_Declaration());
        
        
        out.append("        PRIMARY KEY  (`" + program_Name.getField_Name() + "`)\n");
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
                return "ORDER BY program_Name ASC";
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
                        return "WHERE program_Name > '" + SQLUtil.encode(this.program_Name.toString()) + "' ";
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
                    preparedStatement = this.program_Name.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){
                    
                    preparedStatement = this.program_Title.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.subsystem_Id.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.maintenance_Of_Table_Name.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Program record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Program result;
        try {
            result = new Program();
            
            result.program_Name.setValue_ResultSet(resultSet);
            result.program_Title.setValue_ResultSet(resultSet);
            result.subsystem_Id.setValue_ResultSet(resultSet);
            result.maintenance_Of_Table_Name.setValue_ResultSet(resultSet);
            
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
        
        error_Count += this.program_Name.validate_Request(prefix, httpServletRequest, suffix);
        
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
        
        error_Count += this.program_Title.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.subsystem_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.maintenance_Of_Table_Name.validate_Request(prefix, httpServletRequest, suffix);
        
        
        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + ".validate_Fields_Of_Request(): Invalid Data Field(s). " + error_Count + " errors.");
        
        
    }
    
    
    
    
    
    /** Returns a list of fields of this record. Set manually by programmer.
     * @return DFBase[]
     */
    public DFBase[] get_Field_List() {
        // programmer has entered ...
        LinkedList programmers_Field_List = new LinkedList();
        programmers_Field_List.add( program_Name );
        programmers_Field_List.add( program_Title );
        programmers_Field_List.add( subsystem_Id );
        programmers_Field_List.add( maintenance_Of_Table_Name );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }
    
    
    
    /**
     * @return Program.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue() fails.
     */
    public synchronized Program copy_Of() throws Database_Record_Constructor_Exception {
        Program result;
        try {
            result = new Program();
            
            result.setProgram_Name(this.program_Name.getValue());
            result.setProgram_Title(this.program_Title.getValue());
            result.setSubsystem_Id(this.subsystem_Id.getValue());
            result.setMaintenance_Of_Table_Name(this.maintenance_Of_Table_Name.getValue());
            
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": setValue(): failed because DFException : " + dfe.getMessage());
        }
        
        return result;
    }
    
    
    
    
    
    /**
     * Getter for property program_Name.
     * @return Value of property program_Name.
     */
    public DFString getProgram_Name() {
        return program_Name;
    }
    
    
    /**
     * Setter for property program_Name.
     * @param program_Name New value of property program_Name.
     */
    public void setProgram_Name(String program_Name) throws DFException {
        this.program_Name.setValue(program_Name);
    }
    
    
    /**
     * Getter for property program_Title.
     * @return Value of property program_Title.
     */
    public DFString getProgram_Title() {
        return program_Title;
    }
    
    
    /**
     * Setter for property program_Title.
     * @param program_Title New value of property program_Title.
     */
    public void setProgram_Title(String program_Title) throws DFException {
        this.program_Title.setValue(program_Title);
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
     * Getter for property maintenance_Of_Table_Name.
     * @return Value of property maintenance_Of_Table_Name.
     */
    public DFString getMaintenance_Of_Table_Name() {
        return maintenance_Of_Table_Name;
    }
    
    
    /**
     * Setter for property maintenance_Of_Table_Name.
     * @param maintenance_Of_Table_Name New value of property maintenance_Of_Table_Name.
     */
    public void setMaintenance_Of_Table_Name(String maintenance_Of_Table_Name) throws DFException {
        this.maintenance_Of_Table_Name.setValue(maintenance_Of_Table_Name);
    }
    
    
    //============== End jdbr Generated Section ===========================
    
    
    public static Program[] get_Subsystem_Programs(String subsystem_Id, Database_Front database_Front)
    throws Database_Front_Exception, Database_Record_Exception, Database_Record_Constructor_Exception, DFException {
        
        Program temp_Program = new Program();
        temp_Program.setSubsystem_Id(subsystem_Id);
        
        
        Program[] programs;
        
        String where_Clause = " WHERE " +
        temp_Program.getSubsystem_Id().toWhere_Clause();
        
        
        programs = (Program[]) database_Front.getMany_Records(temp_Program, where_Clause);
        
        
        
        return programs;
    }
    
    
    
    
    public static Program get_Program(String program_Name, Database_Front database_Front)
    throws Database_Front_Exception, Database_Record_Constructor_Exception, Database_Front_No_Results_Exception {
        Program program=null;
        boolean debug_Mode=false;
        String message ="";
        
        try {
            program = (Program) database_Front.getRecord(new Program(program_Name));
        }
        catch(Database_Record_Constructor_Exception dbrce){
            if(debug_Mode){
                message = "Failed to find Subscriber_Organization in database. Database_Front_Exception :" + dbrce.getMessage();
                throw new Database_Front_Exception(message);
            }else{
                throw dbrce;
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
        
        
        return program;
    }
    
    
    
    public static Program get_Maint_Program_Of_Table(String table_Name, Web_Process web_Process)
    throws Database_Front_Exception, Database_Record_Constructor_Exception, DFException {
        
        String message ="";
        
        
        Program temp_Program = new Program();
        temp_Program.setMaintenance_Of_Table_Name(table_Name);
        
        String where_Clause = " WHERE " +
        temp_Program.getMaintenance_Of_Table_Name().toWhere_Clause();
        
        Program[] programs;
        
        
        programs = (Program[]) web_Process.getDatabase_Front().getMany_Records(temp_Program, where_Clause);
        
        
        
        if(programs==null){
            return null;
        }
        
        
        
        
        // program's subsystem installed
        
        
        Program program=null;
        int count_Found_Installed=0;;
        
        
        for(int i=0; i<programs.length; i++){
            program = programs[i];
            
            Subscriber_Organization_Portal_Subsystem temp_Subscriber_Organization_Portal_Subsystem =
            new Subscriber_Organization_Portal_Subsystem(web_Process.getSubscriber_Organization().getSubscriber_Organization_Id().getValue(),
            web_Process.getSubscriber_Organization_Portal().getPortal_Name().getValue(),
            program.getSubsystem_Id().getValue());
            
            Subscriber_Organization_Portal_Subsystem subscriber_Organization_Portal_Subsystem;
            
            try {
                subscriber_Organization_Portal_Subsystem = (Subscriber_Organization_Portal_Subsystem) web_Process.getDatabase_Front().getRecord(temp_Subscriber_Organization_Portal_Subsystem);
                count_Found_Installed++;
            }catch(Database_Front_No_Results_Exception dbfnre){
                programs[i]=null;
            }
        }
        
        
        
        
        if(count_Found_Installed>1){
            // give preference to local packages
            for(int i=0; i<programs.length; i++){
                program = programs[i];
                if(program==null)
                    continue;
                Subsystem subsystem;
                try {
                    subsystem = Subsystem.get_Subsystem(program.getSubsystem_Id().getValue());
                }catch(Database_Front_No_Results_Exception dbfnre){
                    programs[i]=null;
                    count_Found_Installed--;
                    continue;
                }
                if(!subsystem.getPackage_Name().getValue().startsWith("com.amlibtech")){
                    return program;
                }
            }
        }
        
        // first come, first server
        for(int i=0; i<programs.length; i++){
            program = programs[i];
            if(program==null)
                continue;
            
            return program;
        }
        
        return null;
    }
    
    
    
    
}
