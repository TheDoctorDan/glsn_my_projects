/*
 * Database_Record_Base.java
 *
 * Created on April 16, 2006, 10:41 PM
 */

package com.amlibtech.database;

import com.amlibtech.data_fields.*;
import java.lang.reflect.*;
import java.sql.*;
import javax.servlet.http.*;


/**
 * Database_Front is a front end to an SQL database.
 * It should isolate most of the SQL from a programmer.
 * @author dgleeson
 */
abstract public class Database_Record_Base {
    
    public static final int ORDER_KEYS_FIRST = 0;
    public static final int ORDER_KEYS_LAST = 1;
    public static final int ORDER_KEYS_ONLY = 2;
    public static final int ORDER_KEYS_NEVER = 3;
    
    public static final int PRIMARY_INDEX=0;
    public static final int ALPHA_INDEX=1;
    public static final int DATE_INDEX=2;
    
    String Entity_Name;
    String Entities_Name;
    
    public DFBase[]  primary_Index_DFields;
    public DFBase[]  data_DFields;
    
    /** Creates a new instance of Database_Record_Base
     * @param Entity_Name is the singular version of what is stored in this record. i.e. Company.
     * @param Entities_Name is the plural version of what is stored in this record.  i.e. Companies.
     */
    public Database_Record_Base(String Entity_Name, String Entities_Name) {
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
     * Getter for property primary_Index_DFields.
     * @return Value of property primary_Index_DFields.
     */
    final public DFBase[] getPrimary_Index_DFields() {
        return this.primary_Index_DFields;
    }
    
    
    /**
     * Setter for property primary_Index_DFields.
     * @param primary_Index_Fields New value of property primary_Index_DFields.
     * @throws Database_Record_Exception Can not be an index field because it is not a field in this record.
     */
    final public void setPrimary_Index_DFields(DFBase[] primary_Index_DFields) throws Database_Record_Constructor_Exception{
        Field[] my_Fields = getData_Field_List();
        for(int j=0; j<primary_Index_DFields.length; j++){
            boolean found=false;
            for(int i=0;i<my_Fields.length; i++){
                if(my_Fields[i].getType().getName().startsWith("com.amlibtech.data_fields")){
                    if(primary_Index_DFields[j].getField_Name().equals(my_Fields[i].getName())){
                        found=true;
                        i=my_Fields.length;
                    }
                }
            }
            if(!found)
                throw new Database_Record_Constructor_Exception("Field :" + primary_Index_DFields[j].getField_Name() +": Can not be an index field because it is not a field in this record.");
        }
        this.primary_Index_DFields = primary_Index_DFields;
        this.init_Data_Field_List();
    }
    
    /**
     * Setter for property primary_Index_DFields.
     * @param primary_Index_DField New value of property primary_Index_DFields.
     * @throws Database_Record_Constructor_Exception Can not be an index field because it is not a field in this record.
     */
    final public void setPrimary_Index_DFields(DFBase primary_Index_DField) throws Database_Record_Constructor_Exception{
        Field[] my_Fields = getData_Field_List();
        boolean found=false;
        for(int i=0;i<my_Fields.length; i++){
            if(my_Fields[i].getType().getName().startsWith("com.amlibtech.data_fields")){
                if(primary_Index_DField.getField_Name().equals(my_Fields[i].getName())){
                    found=true;
                    i=my_Fields.length;
                }
            }
            
            if(!found)
                throw new Database_Record_Constructor_Exception("Field :" + primary_Index_DField.getField_Name() +": Can not be an index field because it is not a field in this record.");
        }
        this.primary_Index_DFields = new DFBase[1];
        this.primary_Index_DFields[0] = primary_Index_DField;
        this.init_Data_Field_List();
    }
    
    
    
//    public DFBase[] get_Primary_Key_Field_List(){
//        DFBase[] dfbases = this.get_Field_List();
//        DFBase[] result_DFBases = new DFBase[this.primary_Index_DFields.length];
//        for(int j=0; j<this.primary_Index_DFields.length;j++){
//            for(int i=0;i<dfbases.length;i++){
//                if(dfbases[i].getField_Name().equals(this.primary_Index_DFields[j].getField_Name()))
//                    result_DFBases[j] = dfbases[i];
//            }
//        }
//        return result_DFBases;
//    }
    
    
    /**
     * Getter for property primary_Index_DFields.
     * @return Value of property primary_Index_DFields.
     */
    final public DFBase[] getData_DFields() {
        return this.data_DFields;
    }
    
    
    final private void init_Data_Field_List(){
        DFBase[] dfbases = this.get_Field_List();
        DFBase[] temp_DFBases = new DFBase[dfbases.length];
        int count=0;
        for(int i=0;i<dfbases.length;i++){
            boolean found=false;
            for(int j=0; j<this.primary_Index_DFields.length;j++){
                if(dfbases[i].getField_Name().equals(this.primary_Index_DFields[j].getField_Name())){
                    found=true;
                    j=this.primary_Index_DFields.length;
                }
            }
            if(!found)
                temp_DFBases[count++] = dfbases[i];
        }
        if(count==0)
            this.data_DFields=null;
        else{
            DFBase[] result_DFBases = new DFBase[count];
            for(int k=0; k<count;k++)
                result_DFBases[k] = temp_DFBases[k];
            this.data_DFields = result_DFBases;
        }
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * Returns name of Database table for these records.
     * @see Database_Front()
     * @return Table Name.
     */
    abstract public String get_Table_Name();
    
    /**
     * Returns first field name. Used in Database_Front to build short SELECT clause.
     * @see com.amlibtech.database.Database_Front.is_Match()
     * @return the field_Name of first data field in record.
     */
    final public String get_First_Field_Name(){
        return this.get_Field_List()[0].getField_Name();
    }
    
    /**
     * Used by Database_Front to INSERT records.
     * @see get_Values_Clause()
     * @return parenthesised comma separated list of ALL field_Names in record.
     */
    final public String get_Into_Clause(){
        String answer="(";
        DFBase[] dfBases = this.get_Field_List();
        for(int i=0; i< dfBases.length; i++){
            if(i!=0)
                answer=answer+", ";
            answer = answer + dfBases[i].getField_Name();
        }
        answer = answer + ")";
        return answer;
    }
    
    
    /**
     * Used by Database_Front to INSERT records.
     * @see get_Into_Clause()
     * @return parenthesised comma separated list of <CODE>?</CODE>. One for each field_Name in record.
     * @Note as counter part.
     */
    final public String get_Values_Clause(){
        String answer="(";
        DFBase[] dfBases = this.get_Field_List();
        for(int i=0; i< dfBases.length; i++){
            if(i!=0)
                answer=answer+", ";
            answer = answer + "?";
        }
        answer = answer + ")";
        return answer;
    }
    
    
    /**
     * Used by Database_Front to INSERT, FIND, DELETE, UPDATE a record.
     * @return comma separated list of field_Name = ? of all field_Names in Primary Key.
     */
    final public String get_Key_Where_Clause(){
        String answer="";
        DFBase[] dfBases = this.getPrimary_Index_DFields();
        for(int i=0; i< dfBases.length; i++){
                if(i!=0)
                    answer=answer+" AND ";
                answer = answer + dfBases[i].getField_Name() + " = ?";
        }
        return answer;
    }
    
    /**
     * Used by Database_Front to UPDATE a record.
     * @return comma separated list of field_Name = ? of all field_Names NOT in Primary Key.
     */
    final public String get_Set_Clause(){
        String answer="";
        DFBase[] dfBases = this.getData_DFields();
        if(dfBases!=null){
            for(int i=0; i< dfBases.length; i++){
                    if(i!=0)
                        answer=answer+", ";
                    answer = answer + dfBases[i].getField_Name() + " = ?";
            }
        }
        return answer;  
    }
    
    
    
    
    /**
     * Used by {@link Database_Front} to encode fields into a SQL prepared statement.
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
     * Used by Database_Front to decode SQL resultSet into data fields of record.
     * @param resultSet is a ResultSet returned from PrepareStatement.executeUpdate().
     * @return Database_Record_Base instance record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    abstract public Database_Record_Base resultSet_Row_Decode(ResultSet resultSet) throws Database_Record_Constructor_Exception;
    
    /**
     * Used by Database_Front to return many records.
     * @param array_Size is the size of the array required.
     * @return Database_Record_Base instance[] the size of array_Size as an Database_Record_Base[].
     */
    abstract public Database_Record_Base[] get_Array_of_Records(int array_Size);
    
    /**
     * Used by Database_Front to sort records.
     * @param which_Index used to select which index to use.  PRIMARY_INDEX is always available.
     * @return SQL ORDER BY clause for selected index.
     * @throws Database_Front_Exception if selected index is invalid.
     */
    abstract public String getNext_Sort_Clause(int which_Index) throws Database_Front_Exception;
    
    
    /**
     * Used by Database_Front to split matching records into small sets of records.
     * skip_Levels will increment as each prior level fails.
     * @param which_Index used to select which index to use.  PRIMARY_INDEX is always available.
     * @param skip_Levels used to control how many fields of the index are to be used.
     *  As last field of multi-field index maxes out, We want to allow next to last field to increase.
     * And then again on the next level.  Etc.
     * @return WHERE clause for which_Index and skip_Levels. <code>null</code> if skip_Levels exceeds maximum number for selected index. <code>null</code> if skip_Levels less than 0.
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
    final public void validate_Key_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front database_Front) throws  Database_Record_Exception {
        validate_Key_Fields_Of_Request("", httpServletRequest, database_Front, "");
    }
    
    /** Validate index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @throws Database_Record_Exception if a value is invalid.
     */
    final public void validate_Key_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front) throws  Database_Record_Exception {
        validate_Key_Fields_Of_Request(prefix, httpServletRequest, database_Front, "");
    }
    
    /** Validate index fields of an html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     */
    final public void validate_Key_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix) throws  Database_Record_Exception {
        validate_Key_Fields_Of_Request("", httpServletRequest, database_Front, suffix);
    }
    
    /** Validate index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     */
    abstract public void validate_Key_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix) throws  Database_Record_Exception;
    
    
    
    
    
    
    /** Validate non-index fields of an html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @throws Database_Record_Exception if a value is invalid.
     * @throws Database_Record_Constructor_Exception if a value could not be validated againt foreign record.
     */
    final public void validate_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front database_Front)
    throws  Database_Record_Exception, Database_Record_Constructor_Exception {
        validate_Fields_Of_Request("", httpServletRequest, database_Front, "");
    }
    
    
    /** Validate non-index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @throws Database_Record_Exception if a value is invalid.
     * @throws Database_Record_Constructor_Exception if a value could not be validated againt foreign record.
     */
    final public void validate_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front)
    throws  Database_Record_Exception, Database_Record_Constructor_Exception {
        validate_Fields_Of_Request(prefix, httpServletRequest, database_Front, "");
    }
    
    /** Validate non-index fields of an html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     * @throws Database_Record_Constructor_Exception if a value could not be validated againt foreign record.
     */
    final public void validate_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix)
    throws  Database_Record_Exception, Database_Record_Constructor_Exception {
        validate_Fields_Of_Request("", httpServletRequest, database_Front, suffix);
    }
    
    
    /**
     * Validate non-index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     * @throws Database_Record_Constructor_Exception if a value could not be validated againt foreign record.
     */
    abstract public void validate_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix)
    throws  Database_Record_Exception, Database_Record_Constructor_Exception;
    
    
    
    /**
     * Used to check for reasons not to delete this record from table.
     * @param httpSession used if routine needs extra info from a session attribute.
     * @param database_Front used if routine needs to check database.
     * @throws Database_Record_Exception If you can not delete this record.
     * @throws Database_Record_Constructor_Exception If you can not delete this record.
     */
    abstract public void is_Delete_Allowed(Database_Front database_Front, HttpSession httpSession) throws Database_Record_Exception, Database_Record_Constructor_Exception;
    
    /**
     * Returns a list of fields of this record. Set manually by programmer.
     * @return DFBase[]
     */
    abstract public DFBase[] get_Field_List();

    
}
