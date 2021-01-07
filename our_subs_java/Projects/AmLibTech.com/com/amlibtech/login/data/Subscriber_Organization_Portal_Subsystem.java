/*
 * Subscriber_Organization_Portal_Subsystem.java
 *
 * Created on June 27, 2006, 10:24 PM
 */
/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 2000, 2001, 2002, 2003, 2004, 2005, 2006       |
|       American Liberator Technologies                                        |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       American Liberator Technologies                                        |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
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
public class Subscriber_Organization_Portal_Subsystem extends Database_Record_Base {
    
    private DFString	subscriber_Organization_Id;
    private DFString    portal_Name;
    private DFString    subsystem_Id;
    

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
    /** Creates a new instance of Subscriber_Organization_Portal_Subsystem
     * @throws Database_Record_Constructor_Exception
     */
    public Subscriber_Organization_Portal_Subsystem() throws Database_Record_Constructor_Exception {
        super("Subscriber_Organization_Portal_Subsystem", "Subscriber_Organization_Portal_Subsystems");
        try {
            this.subscriber_Organization_Id = Subscriber_Organization.DFInit_Subscriber_Organization_Id();
            this.portal_Name = Subscriber_Organization_Portal.DFInit_Portal_Name();
            this.subsystem_Id =  Subsystem.DFInit_Subsystem_Id();

            DFBase[] index =   { this.subscriber_Organization_Id, this.portal_Name, this.subsystem_Id };
            this.setPrimary_Index_DFields(index);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }




    // Keys Only Constructor
    /** Creates a new instance of Subscriber_Organization_Portal_Subsystem with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Subscriber_Organization_Portal_Subsystem(String subscriber_Organization_Id, String portal_Name, String subsystem_Id)
    throws Database_Record_Constructor_Exception {
        this();
        try {
            this.subscriber_Organization_Id.setValue(subscriber_Organization_Id);
            this.portal_Name.setValue(portal_Name);
            if(subsystem_Id!=null)
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
        return "subscriber_organization_portal_subsystem";
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
     * @return Subscriber_Organization_Portal_Subsystem[] the size of array_Size as an Object[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Subscriber_Organization_Portal_Subsystem[] subscriber_Organization_Portal_Subsystem_Records;
        subscriber_Organization_Portal_Subsystem_Records = new Subscriber_Organization_Portal_Subsystem[array_Size];
        return subscriber_Organization_Portal_Subsystem_Records;
    }





    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {

        StringBuffer out = new StringBuffer();

        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + subscriber_Organization_Id.getSQL_Declaration());
        out.append("\t" + portal_Name.getSQL_Declaration());
        out.append("\t" + subsystem_Id.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + subscriber_Organization_Id.getField_Name() + "`, `" + portal_Name.getField_Name() + "`, `" + subsystem_Id.getField_Name() + "`)\n");
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
                return "ORDER BY subscriber_Organization_Id ASC, portal_Name ASC, subsystem_Id ASC";
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
                return "WHERE subscriber_Organization_Id = '" + SQLUtil.encode(this.subscriber_Organization_Id.toString()) + "' "
                    + " AND portal_Name = '" + SQLUtil.encode(this.portal_Name.toString()) + "' "
                    + " AND subsystem_Id > '" + SQLUtil.encode(this.subsystem_Id.toString()) + "' ";
            case 1:
                return "WHERE subscriber_Organization_Id = '" + SQLUtil.encode(this.subscriber_Organization_Id.toString()) + "' "
                    + " AND portal_Name > '" + SQLUtil.encode(this.portal_Name.toString()) + "' ";
            case 2:
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
                    preparedStatement = this.portal_Name.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.subsystem_Id.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){

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
     * @return Subscriber_Organization_Portal_Subsystem record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Subscriber_Organization_Portal_Subsystem result;
        try {
            result = new Subscriber_Organization_Portal_Subsystem();

            result.subscriber_Organization_Id.setValue_ResultSet(resultSet);
            result.portal_Name.setValue_ResultSet(resultSet);
            result.subsystem_Id.setValue_ResultSet(resultSet);

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

        error_Count += this.subscriber_Organization_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.portal_Name.validate_Request(prefix, httpServletRequest, suffix);
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



        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + "validate_Fields_Of_Request(): Invalid Data Field(s).");


    }



    /** Returns a list of fields of this record. Set manually by programmer.
     * @return DFBase[]
     */
    public DFBase[] get_Field_List() {
        // programmer has entered ...
        LinkedList programmers_Field_List = new LinkedList();
        programmers_Field_List.add( subscriber_Organization_Id );
        programmers_Field_List.add( portal_Name );
        programmers_Field_List.add( subsystem_Id );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
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
     * Getter for property portal_Name.
     * @return Value of property portal_Name.
     */
    public DFString getPortal_Name() {
        return portal_Name;
    }


    /**
     * Setter for property portal_Name.
     * @param portal_Name New value of property portal_Name.
     */
    public void setPortal_Name(String portal_Name) throws DFException {
        this.portal_Name.setValue(portal_Name);
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


    //============== End jdbr Generated Section ===========================

   

}
