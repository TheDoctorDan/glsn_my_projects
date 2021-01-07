/*
 * Outside_User.java
 *
 * Created on April 1, 2007, 5:03 PM
 *
 * Enty400 maint
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
public class Outside_User extends Database_Record_Base {

    private DFString_Filled                 comp_Id;
    private DFString                        outside_User_Id;
    private DFString_Encrypted_Hex          password;

    private DFString_Enumerated type_Index; // person or organization;
    private DFInteger person_Number;
    private DFInteger organization_Number;

    private DFBoolean obsolete;

    private DFTimestamp	last_Logged_In;




    public static final int OUTSIDE_USER_ID_LEN = 100;

    public static final int OUTSIDE_USER_TYPE_INDEX_PERSON = 0;
    public static final int OUTSIDE_USER_TYPE_INDEX_ORGANIZATION = 1;

    public static final String[] OUTSIDE_USER_TYPES = {
        "Person",
        "Organization"
    };


    //============== Begin jdbr Constructor Section ===========================


    // Empty Constructor
    /** Creates a new instance of Outside_User
     * @throws Database_Record_Constructor_Exception
     */
    public Outside_User() throws Database_Record_Constructor_Exception {
        super("Outside User", "Outside Users");
        try {
            this.comp_Id =  Comp.DFInit_Comp_Id();
            this.outside_User_Id =  new DFString("outside_User_Id", "Outside User Id", 3, OUTSIDE_USER_ID_LEN);
            this.password =  new DFString_Encrypted_Hex("password", "Password", 6, 30);
            this.type_Index =  new DFString_Enumerated("type_Index", "Type Index", OUTSIDE_USER_TYPES);
            this.person_Number =  Person.DFInit_Person_Number();
            this.organization_Number =  Organization.DFInit_Organization_Number();
            this.obsolete =  new DFBoolean("obsolete", "Obsolete");
            this.last_Logged_In =  new DFTimestamp("last_Logged_In", "Last Logged In");

            DFBase[] index =   { this.comp_Id, this.outside_User_Id };
            this.setPrimary_Index_DFields(index);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }




    // Keys Only Constructor
    /** Creates a new instance of Outside_User with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Outside_User(String comp_Id, String outside_User_Id) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.comp_Id.setValue(comp_Id);
            this.outside_User_Id.setValue(outside_User_Id);

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor(ID) : failed because DFException : " + dfe.getMessage());
        }
    }



    // Keys Only Constructor
    /** Creates a new instance of Outside_User with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Outside_User(Person person) throws Database_Record_Constructor_Exception {
        this();
        try {
            if(person.getE_Mail_Type().getValue().equals(Person.USE_FAMILY_TEXT))
                throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor(ID) : failed because person's E-Mail address is from Family and not their own.");
            this.comp_Id.setValue(person.getComp_Id().getValue());
            this.type_Index.setValue(this.OUTSIDE_USER_TYPES[this.OUTSIDE_USER_TYPE_INDEX_PERSON]);
            this.person_Number.setValue(person.getPerson_Number().getValue());
            this.outside_User_Id.setValue(person.getE_Mail().getValue());
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor(ID) : failed because DFException : " + dfe.getMessage());
        }
    }



    // Keys Only Constructor
    /** Creates a new instance of Outside_User with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Outside_User(Organization organization) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.comp_Id.setValue(organization.getComp_Id().getValue());
            this.type_Index.setValue(this.OUTSIDE_USER_TYPES[this.OUTSIDE_USER_TYPE_INDEX_ORGANIZATION]);
            this.organization_Number.setValue(organization.getOrganization_Number().getValue());
            this.outside_User_Id.setValue(organization.getE_Mail().getValue());
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
        return "outside_user";
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
     * @return Outside_User[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Outside_User[] outside_User_Records;
        outside_User_Records = new Outside_User[array_Size];
        return outside_User_Records;
    }





    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {

        StringBuffer out = new StringBuffer();

        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + comp_Id.getSQL_Declaration());
        out.append("\t" + outside_User_Id.getSQL_Declaration());
        out.append("\t" + password.getSQL_Declaration());
        out.append("\t" + type_Index.getSQL_Declaration());
        out.append("\t" + person_Number.getSQL_Declaration());
        out.append("\t" + organization_Number.getSQL_Declaration());
        out.append("\t" + obsolete.getSQL_Declaration());
        out.append("\t" + last_Logged_In.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + comp_Id.getField_Name() + "`, `" + outside_User_Id.getField_Name() + "`)\n");

        out.append(",        UNIQUE KEY `Entity_Index` (`" + comp_Id.getField_Name() + "`, `" +
        type_Index.getField_Name() + "`, `" +
        person_Number.getField_Name() + "`, `" +
        organization_Number.getField_Name() + "`)\n");

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
                return "ORDER BY comp_Id ASC, outside_User_Id ASC";
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
                        + " AND outside_User_Id > '" + SQLUtil.encode(this.outside_User_Id.toString()) + "' ";
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
                    preparedStatement = this.outside_User_Id.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){

                    preparedStatement = this.password.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.type_Index.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.person_Number.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.organization_Number.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.obsolete.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.last_Logged_In.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Outside_User record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Outside_User result;
        try {
            result = new Outside_User();

            result.comp_Id.setValue_ResultSet(resultSet);
            result.outside_User_Id.setValue_ResultSet(resultSet);
            result.password.setValue_ResultSet(resultSet);
            result.type_Index.setValue_ResultSet(resultSet);
            result.person_Number.setValue_ResultSet(resultSet);
            result.organization_Number.setValue_ResultSet(resultSet);
            result.obsolete.setValue_ResultSet(resultSet);
            result.last_Logged_In.setValue_ResultSet(resultSet);

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
        error_Count += this.outside_User_Id.validate_Request(prefix, httpServletRequest, suffix);

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

        error_Count += this.password.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.type_Index.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.person_Number.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.organization_Number.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.obsolete.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.last_Logged_In.validate_Request(prefix, httpServletRequest, suffix);


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
        programmers_Field_List.add( outside_User_Id );
        programmers_Field_List.add( password );
        programmers_Field_List.add( type_Index );
        programmers_Field_List.add( person_Number );
        programmers_Field_List.add( organization_Number );
        programmers_Field_List.add( obsolete );
        programmers_Field_List.add( last_Logged_In );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }



    /**
     * @return Outside_User.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue() fails.
     */
    public synchronized Outside_User copy_Of() throws Database_Record_Constructor_Exception {
        Outside_User result;
        try {
            result = new Outside_User();

            result.setComp_Id(this.comp_Id.getValue());
            result.setOutside_User_Id(this.outside_User_Id.getValue());
            result.setPassword(this.password.getValue());
            result.setType_Index(this.type_Index.getValue());
            result.setPerson_Number(this.person_Number.getValue());
            result.setOrganization_Number(this.organization_Number.getValue());
            result.setObsolete(this.obsolete.getValue());
            result.setLast_Logged_In(this.last_Logged_In.getValue());

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
     * Getter for property outside_User_Id.
     * @return Value of property outside_User_Id.
     */
    public DFString getOutside_User_Id() {
        return outside_User_Id;
    }


    /**
     * Setter for property outside_User_Id.
     * @param outside_User_Id New value of property outside_User_Id.
     */
    public void setOutside_User_Id(String outside_User_Id) throws DFException {
        this.outside_User_Id.setValue(outside_User_Id);
    }


    /**
     * Getter for property password.
     * @return Value of property password.
     */
    public DFString_Encrypted_Hex getPassword() {
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
     * Getter for property type_Index.
     * @return Value of property type_Index.
     */
    public DFString_Enumerated getType_Index() {
        return type_Index;
    }


    /**
     * Setter for property type_Index.
     * @param type_Index New value of property type_Index.
     */
    public void setType_Index(String type_Index) throws DFException {
        this.type_Index.setValue(type_Index);
    }


    /**
     * Getter for property person_Number.
     * @return Value of property person_Number.
     */
    public DFInteger getPerson_Number() {
        return person_Number;
    }


    /**
     * Setter for property person_Number.
     * @param person_Number New value of property person_Number.
     */
    public void setPerson_Number(Integer person_Number) throws DFException {
        this.person_Number.setValue(person_Number);
    }


    /**
     * Getter for property organization_Number.
     * @return Value of property organization_Number.
     */
    public DFInteger getOrganization_Number() {
        return organization_Number;
    }


    /**
     * Setter for property organization_Number.
     * @param organization_Number New value of property organization_Number.
     */
    public void setOrganization_Number(Integer organization_Number) throws DFException {
        this.organization_Number.setValue(organization_Number);
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
     * Getter for property last_Logged_In.
     * @return Value of property last_Logged_In.
     */
    public DFTimestamp getLast_Logged_In() {
        return last_Logged_In;
    }


    /**
     * Setter for property last_Logged_In.
     * @param last_Logged_In New value of property last_Logged_In.
     */
    public void setLast_Logged_In(GregorianCalendar last_Logged_In) throws DFException {
        this.last_Logged_In.setValue(last_Logged_In);
    }


    //============== End jdbr Generated Section ===========================




}
