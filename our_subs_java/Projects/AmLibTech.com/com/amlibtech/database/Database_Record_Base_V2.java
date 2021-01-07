/*
 * Database_Record_Base_V2.java
 *
 * Created on April 16, 2006, 10:41 PM
 */

package com.amlibtech.database;

import com.amlibtech.data_fields.*;
import java.lang.reflect.*;
import java.sql.*;
import javax.servlet.http.*;


/**
 * Database_Front_V2 is a front end to an SQL database.
 * It should isolate most of the SQL from a programmer.
 * @author dgleeson
 */
public abstract class Database_Record_Base_V2 {
    
    public static final int ORDER_KEYS_FIRST = 0;
    public static final int ORDER_KEYS_LAST = 1;
    public static final int ORDER_KEYS_ONLY = 2;
    public static final int ORDER_KEYS_NEVER = 3;
    
    public static final int PRIMARY_INDEX=0;
    public static final int ALPHA_INDEX=1;
    public static final int DATE_INDEX=2;
    
    String Entity_Name;
    String Entities_Name;
    
    
    /** Creates a new instance of Database_Record_Base_V2
     * @param Entity_Name is the singular version of what is stored in this record. i.e. Company.
     * @param Entities_Name is the plural version of what is stored in this record.  i.e. Companies.
     */
    public Database_Record_Base_V2(String Entity_Name, String Entities_Name) {
        this.Entity_Name = Entity_Name;
        this.Entities_Name = Entities_Name;
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


    /**
     * Getter for property Entity_Name.
     * @return Value of property Entity_Name.
     */
    final public java.lang.String getEntity_Name() {
        return Entity_Name;
    }
    
    /**
     * Setter for property Entity_Name.
     * @param Entity_Name New value of property Entity_Name.
     */
    final public void setEntity_Name(java.lang.String Entity_Name) {
        this.Entity_Name = Entity_Name;
    }
    
    /**
     * Getter for property Entities_Name.
     * @return Value of property Entities_Name.
     */
    final public java.lang.String getEntities_Name() {
        return Entities_Name;
    }
    
    /**
     * Setter for property Entities_Name.
     * @param Entities_Name New value of property Entities_Name.
     */
    final public void setEntities_Name(java.lang.String Entities_Name) {
        this.Entities_Name = Entities_Name;
    }
    
    /**
     * Provides a list of DFBase fields in this class.
     * @return an array of {@link Field} for each {@link DFBase} field in this class.
     */
    final public Field[] getData_Field_List(){
        Class my_Class = this.getClass();
        Field[] all_Fields = my_Class.getDeclaredFields();
        int count;
        count=0;
        
        for(int i=0;i<all_Fields.length; i++){
            if(all_Fields[i].getType().getName().startsWith("com.amlibtech.data_fields"))
                count++;
        }
        
        Field[] my_Fields = new Field[count];
        count=0;
        for(int i=0;i<all_Fields.length; i++){
            if(all_Fields[i].getType().getName().startsWith("com.amlibtech.data_fields"))
                my_Fields[count++] = all_Fields[i];
        }
        return my_Fields;
    }
    
        
    final public boolean is_Valid_Field_Name(String field_Name){
        DFBase[] dfbases = this.get_Field_List();
        for(int i=0;i<dfbases.length;i++){
            if(dfbases[i].getField_Name().equals(field_Name))
                return true;
        }
        return false;
    }
    
    
  
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Returns name of Database table for these records.
     * @see Database_Front_V2()
     * @return Table Name.
     */
    abstract public String get_Table_Name();
    
    /**
     * Returns first field name. Used in Database_Front_V2 to build short SELECT clause.
     * @see Database_Front_V2.is_Match().
     * @return the field_Name of first data field in record.
     */
    abstract public String get_First_Field_Name();

    
    /**
     * Used by Database_Front_V2 to INSERT records.
     * @see get_Values_Clause()
     * @return parenthesised comma separated list of ALL field_Names in record.
     */
    abstract public String get_Into_Clause();
        
    
    
    /**
     * Used by Database_Front_V2 to INSERT records.
     * @see get_Into_Clause()
     * @return parenthesised comma separated list of <CODE>?</CODE>. One for each field_Name in record.
     * @Note as counter part.
     */
    abstract public String get_Values_Clause();
       
    
    
    /**
     * Used by Database_Front_V2 to INSERT, FIND, DELETE, UPDATE a record.
     * @return comma separated list of field_Name = ? of all field_Names in Primary Key.
     */
    abstract public String get_Key_Where_Clause();
        
    
    /**
     * Used by Database_Front_V2 to UPDATE a record.
     * @return comma separated list of field_Name = ? of all field_Names NOT in Primary Key.
     */
    abstract public String get_Set_Clause();
        
    
    
    /**
     * Used by {@link Database_Front_V2} to encode fields into a SQL prepared statement.
     * The first section is for fields in Primary key.
     * See {@link get_Key_Where_Clause} .
     * The second section is for the rest of the data fields.
     * @return preparedStatement with values inserted where ? were.
     * @param preparedStatement is the SQL statement with ? for values.
     * @param order indicates which groups of values in which order are in the preparedStatement.
     * order values can be:
     * <ul><li>ORDER_KEYS_FIRST used when index fields are at beginning of the preparedStatement. (addRecord(), INSERT INTO ...).
     * <li>ORDER_KEYS_LAST used when index fields are at end of the preparedStatement. (updateRecord(), UPDATE x SET ... WHERE ...).
     * <li>ORDER_KEYS_ONLY used when index fields are the only values in the preparedStatement. (is_Match(), getResultSet_Row(), Select * FROM x WHERE ..., deleteRecord(), DELETE FROM ...).
     * <li>ORDER_KEYS_NEVER used when index fields are not in the preparedStatement.
     * </ul>
     * @throws Database_Record_Exception if any of the DFBase.get_PreparedStatement_Value(PreparedStatement preparedStatement, int count) fails.
     * @see get_Values_Clause()
     * @see get_Set_Clause()
     * @see get_Into_Clause()
     */
    abstract public PreparedStatement preparedStatement_Encode(PreparedStatement preparedStatement, int order) throws Database_Record_Exception;
    
    
    
    /**
     * Used by Database_Front_V2 to decode SQL resultSet into data fields of record.
     * @param resultSet is a ResultSet returned from PrepareStatement.executeUpdate().
     * @return Database_Record_Base_V2 instance record.
     * @throws Database_Record_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    abstract public Database_Record_Base_V2 resultSet_Row_Decode(ResultSet resultSet) throws Database_Record_Exception;
    
    /**
     * Used by Database_Front_V2 to return many records.
     * @param array_Size is the size of the array required.
     * @return Database_Record_Base_V2 instance[] the size of array_Size as an Database_Record_Base_V2[].
     */
    abstract public Database_Record_Base_V2[] get_Array_of_Records(int array_Size);
    
    /**
     * Used by Database_Front_V2 to sort records.
     * @param which_Index used to select which index to use.  PRIMARY_INDEX is always available.
     * @return SQL ORDER BY clause for selected index.
     * @throws Database_Front_Exception if selected index is invalid.
     */
    abstract public String getNext_Sort_Clause(int which_Index) throws Database_Front_Exception;
    
    
    /**
     * Used by Database_Front_V2 to split matching records into small sets of records.
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
    abstract public String getNext_Where_Clause(int which_Index, int skip_Levels) throws Database_Front_Exception;
    
    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    abstract public String getSQL_Create_Table();
    
    
    
    /** Validate index fields of an html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @throws Database_Record_Exception if a value is invalid.
     */
    final public void validate_Key_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front_V2 database_Front) throws  Database_Record_Exception {
        validate_Key_Fields_Of_Request("", httpServletRequest, database_Front, "");
    }
    
    /** Validate index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @throws Database_Record_Exception if a value is invalid.
     */
    final public void validate_Key_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front_V2 database_Front) throws  Database_Record_Exception {
        validate_Key_Fields_Of_Request(prefix, httpServletRequest, database_Front, "");
    }
    
    /** Validate index fields of an html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     */
    final public void validate_Key_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front_V2 database_Front, String suffix) throws  Database_Record_Exception {
        validate_Key_Fields_Of_Request("", httpServletRequest, database_Front, suffix);
    }
    
    /** Validate index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     */
    abstract public void validate_Key_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front_V2 database_Front, String suffix) throws  Database_Record_Exception;
    
    
    
    
    
    
    /** Validate non-index fields of an html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @throws Database_Record_Exception if a value is invalid.
     */
    final public void validate_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front_V2 database_Front) throws  Database_Record_Exception {
        validate_Fields_Of_Request("", httpServletRequest, database_Front, "");
    }
    
    
    /** Validate non-index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @throws Database_Record_Exception if a value is invalid.
     */
    final public void validate_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front_V2 database_Front) throws  Database_Record_Exception {
        validate_Fields_Of_Request(prefix, httpServletRequest, database_Front, "");
    }
    
    /** Validate non-index fields of an html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     */
    final public void validate_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front_V2 database_Front, String suffix) throws  Database_Record_Exception {
        validate_Fields_Of_Request("", httpServletRequest, database_Front, suffix);
    }
    
    
    /**
     * Validate non-index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     */
    abstract public void validate_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front_V2 database_Front, String suffix) throws  Database_Record_Exception;
    
    
    
    /**
     * Used to check for reasons not to delete this record from table.
     * @param httpSession used if routine needs extra info from a session attribute.
     * @param database_Front used if routine needs to check database.
     * @throws Database_Record_Exception If you can not delete this record.
     */
    abstract public void is_Delete_Allowed(Database_Front_V2 database_Front, HttpSession httpSession) throws Database_Record_Exception;
    
    /**
     * Returns a list of fields of this record. Set manually by programmer.
     * @return DFBase[]
     */
    abstract public DFBase[] get_Field_List();

    
}
