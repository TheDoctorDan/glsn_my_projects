/*
 * Security_Profile_DataGroup.java
 *
 * Created on November 14, 2006, 9:27 AM
 */

package com.amlibtech.security_Management.data;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.security_Management.data.list.*;
import com.amlibtech.util.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Security_Profile_DataGroup extends Database_Record_Base {
    
    private DFString    security_Profile_Id;
    private DFString    security_DataGroup_Id;
    private DFString_Enumerated access_Type; //query, maint, None


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
    /** Creates a new instance of Security_Profile_DataGroup
     * @throws Database_Record_Constructor_Exception
     */
    public Security_Profile_DataGroup() throws Database_Record_Constructor_Exception {
        super("Security Profile DataGroup", "Security Profile DataGroups");
        try {
            this.security_Profile_Id =  Security_Profile.DFInit_Security_Profile_Id();
            this.security_DataGroup_Id =  Security_DataGroup.DFInit_Security_DataGroup_Id();
            this.access_Type =  new DFString_Enumerated("access_Type", "Access Type", Security_Profile.ACCESS_TYPES);

            DFBase[] index =   { this.security_Profile_Id, this.security_DataGroup_Id };
            this.setPrimary_Index_DFields(index);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }




    // Keys Only Constructor
    /** Creates a new instance of Security_Profile_DataGroup with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Security_Profile_DataGroup(String security_Profile_Id, String security_DataGroup_Id) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.security_Profile_Id.setValue(security_Profile_Id);
            this.security_DataGroup_Id.setValue(security_DataGroup_Id);

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
        return "security_profile_datagroup";
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
     * @return Security_Profile_DataGroup[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Security_Profile_DataGroup[] security_Profile_Datagroup_Records;
        security_Profile_Datagroup_Records = new Security_Profile_DataGroup[array_Size];
        return security_Profile_Datagroup_Records;
    }





    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {

        StringBuffer out = new StringBuffer();

        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + security_Profile_Id.getSQL_Declaration());
        out.append("\t" + security_DataGroup_Id.getSQL_Declaration());
        out.append("\t" + access_Type.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + security_Profile_Id.getField_Name() + "`, `" + security_DataGroup_Id.getField_Name() + "`)\n");
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
                return "ORDER BY security_Profile_Id ASC, security_DataGroup_Id ASC";
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
                return "WHERE security_Profile_Id = '" + SQLUtil.encode(this.security_Profile_Id.toString()) + "' "
                    + " AND security_DataGroup_Id > '" + SQLUtil.encode(this.security_DataGroup_Id.toString()) + "' ";
            case 1:
                return "WHERE security_Profile_Id > '" + SQLUtil.encode(this.security_Profile_Id.toString()) + "' ";
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
                    preparedStatement = this.security_Profile_Id.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.security_DataGroup_Id.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){

                    preparedStatement = this.access_Type.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Security_Profile_DataGroup record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Security_Profile_DataGroup result;
        try {
            result = new Security_Profile_DataGroup();

            result.security_Profile_Id.setValue_ResultSet(resultSet);
            result.security_DataGroup_Id.setValue_ResultSet(resultSet);
            result.access_Type.setValue_ResultSet(resultSet);

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

        error_Count += this.security_Profile_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.security_DataGroup_Id.validate_Request(prefix, httpServletRequest, suffix);

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

        error_Count += this.access_Type.validate_Request(prefix, httpServletRequest, suffix);

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
        programmers_Field_List.add( security_Profile_Id );
        programmers_Field_List.add( security_DataGroup_Id );
        programmers_Field_List.add( access_Type );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }



    /**
     * @return Security_Profile_DataGroup.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue() fails.
     */
    public synchronized Security_Profile_DataGroup copy_Of() throws Database_Record_Constructor_Exception {
        Security_Profile_DataGroup result;
        try {
            result = new Security_Profile_DataGroup();

            result.security_Profile_Id.setValue(this.security_Profile_Id.getValue());
            result.security_DataGroup_Id.setValue(this.security_DataGroup_Id.getValue());
            result.access_Type.setValue(this.access_Type.getValue());

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": setValue(): failed because DFException : " + dfe.getMessage());
        }

        return result;
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
     * Getter for property security_DataGroup_Id.
     * @return Value of property security_DataGroup_Id.
     */
    public DFString getSecurity_DataGroup_Id() {
        return security_DataGroup_Id;
    }


    /**
     * Setter for property security_DataGroup_Id.
     * @param security_DataGroup_Id New value of property security_DataGroup_Id.
     */
    public void setSecurity_DataGroup_Id(String security_DataGroup_Id) throws DFException {
        this.security_DataGroup_Id.setValue(security_DataGroup_Id);
    }


    /**
     * Getter for property access_Type.
     * @return Value of property access_Type.
     */
    public DFString_Enumerated getAccess_Type() {
        return access_Type;
    }


    /**
     * Setter for property access_Type.
     * @param access_Type New value of property access_Type.
     */
    public void setAccess_Type(String access_Type) throws DFException {
        this.access_Type.setValue(access_Type);
    }


    //============== End jdbr Generated Section ===========================

    public int validate_Record(Database_Front database_Front)
    {    
        int error_Count=0;
        
        this.security_DataGroup_Id.setRequest_Error("");

        int index = Security_DataGroup.indexOfDescription(Security_DataGroup.SECURITY_DATAGROUPS, this.security_DataGroup_Id.getValue());
        if(index == -1)
            error_Count++;
        
        return error_Count;   
    }

}
