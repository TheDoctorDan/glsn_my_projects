/*
 * Organization_Type.java
 *
 * Created on August 17, 2006, 8:21 PM
 *
 * Enty010 maint
 *
 */

package entity_Management.datum;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import company_Management.datum.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Organization_Type extends Database_Record_Base {
    
    private DFString_Filled	comp_Id;
    private DFString organization_Type_Id;
    
    public static final int ORGANIZATION_TYPE_ID_LEN = 20;
    
    public static DFString DFInit_Organization_Type_Id() 
    throws DFException {
        return new DFString("organization_Type_Id", "Organization Type", 1, ORGANIZATION_TYPE_ID_LEN);
    }
    

    public static final String TYPE_SCHOOL = "School";
    
    //============== Begin jdbr Constructor Section ===========================


    // Empty Constructor
    /** Creates a new instance of Organization_Type
     * @throws Database_Record_Constructor_Exception
     */
    public Organization_Type() throws Database_Record_Constructor_Exception {
        super("Organization Type", "Organization Types");
        try {
            this.comp_Id =  Comp.DFInit_Comp_Id();
            this.organization_Type_Id =  DFInit_Organization_Type_Id();

            DFBase[] index =   { this.comp_Id, this.organization_Type_Id };
            this.setPrimary_Index_DFields(index);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }




    // Keys Only Constructor
    /** Creates a new instance of Organization_Type with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Organization_Type(String comp_Id, String organization_Type_Id) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.comp_Id.setValue(comp_Id);
            this.organization_Type_Id.setValue(organization_Type_Id);

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
        return "organization_type";
    }




    /** Used to check for reasons not to delete this record from table.
     * @throws Database_Record_Exception
     * @throws Database_Record_Constructor_Exception
     * @param database_Front used if routine needs to check database.
     * @param httpSession used if routine needs extra info from a session attribute.
     */
    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession httpSession) throws Database_Record_Exception, Database_Record_Constructor_Exception {
         
        int count;
        
        
        {
            count=0;
            Organization organization = new Organization();
            try {
                organization.setComp_Id(this.comp_Id.getValue());
                organization.setOrganization_Type_Id(this.organization_Type_Id.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(organization, "WHERE " +
                organization.getComp_Id().toWhere_Clause() + " AND " +
                organization.getOrganization_Type_Id().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + organization.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
        return;
    }




    /**
     * Used by Database_Front to return many records.
     * @param array_Size is the size of the array required.
     * @return Organization_Type[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Organization_Type[] organization_Type_Records;
        organization_Type_Records = new Organization_Type[array_Size];
        return organization_Type_Records;
    }





    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {

        StringBuffer out = new StringBuffer();

        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + comp_Id.getSQL_Declaration());
        out.append("\t" + organization_Type_Id.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + comp_Id.getField_Name() + "`, `" + organization_Type_Id.getField_Name() + "`)\n");
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
                return "ORDER BY comp_Id ASC, organization_Type_Id ASC";
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
                return "WHERE comp_Id = '" + SQLUtil.encode(this.comp_Id.toString()) + "' "
                    + " AND organization_Type_Id > '" + SQLUtil.encode(this.organization_Type_Id.toString()) + "' ";
            case 1:
                return "WHERE comp_Id > '" + SQLUtil.encode(this.comp_Id.toString()) + "' ";
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
                    preparedStatement = this.comp_Id.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.organization_Type_Id.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Organization_Type record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Organization_Type result;
        try {
            result = new Organization_Type();

            result.comp_Id.setValue_ResultSet(resultSet);
            result.organization_Type_Id.setValue_ResultSet(resultSet);

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

        error_Count += this.comp_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.organization_Type_Id.validate_Request(prefix, httpServletRequest, suffix);

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



        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + ".validate_Fields_Of_Request(): Invalid Data Field(s). " + error_Count + " errors.");


    }





    /** Returns a list of fields of this record. Set manually by programmer.
     * @return DFBase[]
     */
    public DFBase[] get_Field_List() {
        // programmer has entered ...
        LinkedList programmers_Field_List = new LinkedList();
        programmers_Field_List.add( comp_Id );
        programmers_Field_List.add( organization_Type_Id );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }



    /**
     * @return Organization_Type.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue() fails.
     */
    public synchronized Organization_Type copy_Of() throws Database_Record_Constructor_Exception {
        Organization_Type result;
        try {
            result = new Organization_Type();

            result.setComp_Id(this.comp_Id.getValue());
            result.setOrganization_Type_Id(this.organization_Type_Id.getValue());

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": setValue(): failed because DFException : " + dfe.getMessage());
        }

        return result;
    }





    /**
     * Getter for property comp_Id.
     * @return Value of property comp_Id.
     */
    public DFString_Filled getComp_Id() {
        return comp_Id;
    }


    /**
     * Setter for property comp_Id.
     * @param comp_Id New value of property comp_Id.
     */
    public void setComp_Id(String comp_Id) throws DFException {
        this.comp_Id.setValue(comp_Id);
    }


    /**
     * Getter for property organization_Type_Id.
     * @return Value of property organization_Type_Id.
     */
    public DFString getOrganization_Type_Id() {
        return organization_Type_Id;
    }


    /**
     * Setter for property organization_Type_Id.
     * @param organization_Type_Id New value of property organization_Type_Id.
     */
    public void setOrganization_Type_Id(String organization_Type_Id) throws DFException {
        this.organization_Type_Id.setValue(organization_Type_Id);
    }


    //============== End jdbr Generated Section ===========================




}
