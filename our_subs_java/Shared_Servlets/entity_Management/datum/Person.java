/*
 * Person.java
 *
 * Created on July 30, 2006, 2:43 PM
 */

package entity_Management.datum;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.html_Tags.*;
import com.amlibtech.login.data.*;
import com.amlibtech.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import customer_Management.data.*;
import enroll_Now_Admin.data.*;
import enroll_Now_Office.data.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Person extends Database_Record_Base {
    
    private DFString_Filled	comp_Id;
    private DFInteger person_Number;
    private DFString first_Name;
    private DFString last_Name;
    
    private DFInteger    member_Of_Family_Number;
    
    private DFString_Enumerated address_Type;
    private DFString street_Address;
    private DFString city;
    private DFState_Abbr state_Abbr;
    private DFZip_Code   zip_Code;
    
    private DFString_Enumerated phone_Type;
    private DFPhone phone;
    
    private DFString_Enumerated e_Mail_Type;
    private DFE_Mail  e_Mail;
    
    // age
    private DFString_Enumerated date_Of_Birth_Status; // unknown, over_18, known;
    private DFDate date_Of_Birth;
    
    private DFString_Enumerated gender; // Male, Female, Other
    
    private DFBoolean obsolete;
    
    
    private DFInteger last_Used_Additional_Address_Number;
    private DFInteger last_Used_Additional_E_Mail_Number;
    
    
    
    public static final String  USE_FAMILY_TEXT="Use Family";
    
    public static final String[] ADDRESS_TYPES = { "Home", "Summer Home", "Winter Home", USE_FAMILY_TEXT };
    public static final String[] PHONE_TYPES = { "Home", "Office", "Mobile", "Emergency", "Fax", USE_FAMILY_TEXT };
    public static final String[] E_MAIL_TYPES = { "Home", "Office", "School", USE_FAMILY_TEXT };
    
    public static final int DATE_OF_BIRTH_STATUS_VALUE_UNKNOWN =0;
    public static final int DATE_OF_BIRTH_STATUS_VALUE_OVER18 =1;
    public static final int DATE_OF_BIRTH_STATUS_VALUE_KNOWN =2;
    public static final String[] DATE_OF_BIRTH_STATUS_VALUES = { "Unknown", "Over 18", "Known" };
    
    public static final String[] GENDER_VALUES = { "Male", "Female", "Other" };
    
    
    public static final int PERSON_NUMBER_LEN = 6;
    
    public static DFInteger DFInit_Person_Number()
    throws DFException {
        return new DFInteger("person_Number", "Person #", PERSON_NUMBER_LEN, 0, 999999);
    }
    
    //get_Age_As_Of_A_Date(date)
    
    
    
    //============== Begin jdbr Constructor Section ===========================
    
    
    // Empty Constructor
    /** Creates a new instance of Person
     * @throws Database_Record_Constructor_Exception
     */
    public Person() throws Database_Record_Constructor_Exception {
        super("Person", "Persons");
        try {
            this.comp_Id =  Comp.DFInit_Comp_Id();
            this.person_Number =  DFInit_Person_Number();
            
            this.first_Name =  new DFString("first_Name", "First Name", 1, 30);
            this.last_Name =  new DFString("last_Name", "Last Name", 1, 30);
            
            this.member_Of_Family_Number =  new DFInteger("member_Of_Family_Number", "Member Of Family#", Family.FAMILY_NUMBER_LEN);
            
            this.address_Type =  new DFString_Enumerated("address_Type", "Address Type", ADDRESS_TYPES);
            this.street_Address =  new DFString("street_Address", "Street Address",40);
            this.city =  new DFString("city", "City", 30);
            this.state_Abbr =  new DFState_Abbr("state_Abbr", "State");
            this.zip_Code =  new DFZip_Code("zip_Code", "Zip Code");
            this.phone_Type =  new DFString_Enumerated("phone_Type", "Phone Type", PHONE_TYPES);
            this.phone =  new DFPhone("phone", "Phone");
            this.e_Mail_Type =  new DFString_Enumerated("e_Mail_Type", "E-Mail Type", E_MAIL_TYPES);
            this.e_Mail =  new DFE_Mail("e_Mail", "E-Mail");
            
            this.date_Of_Birth_Status =  new DFString_Enumerated("date_Of_Birth_Status", "Date Of Birth Status", DATE_OF_BIRTH_STATUS_VALUES);
            this.date_Of_Birth =  new DFDate("date_Of_Birth", "Date Of Birth");
            this.gender =  new DFString_Enumerated("gender", "Gender", GENDER_VALUES);
            
            
            this.obsolete =  new DFBoolean("obsolete", "Obsolete");
            this.last_Used_Additional_Address_Number =  new DFInteger("last_Used_Additional_Address_Number", "Last Used Additional Address Number", Person_Additional_Address.PERSON_ADDITIONAL_ADDRESS_NUMBER_LEN);
            this.last_Used_Additional_E_Mail_Number =  new DFInteger("last_Used_Additional_E_Mail_Number", "Last Used Additional E-Mail Number", Person_Additional_E_Mail.PERSON_ADDITIONAL_E_MAIL_NUMBER_LEN);
            
            
            DFBase[] index =   { this.comp_Id, this.person_Number };
            this.setPrimary_Index_DFields(index);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }
    
    
    
    
    // Keys Only Constructor
    /** Creates a new instance of Person with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Person(String comp_Id, Integer person_Number) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.comp_Id.setValue(comp_Id);
            this.person_Number.setValue(person_Number);
            
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
        return "person";
    }
    
    
    
    
    /** Used to check for reasons not to delete this record from table.
     * @throws Database_Record_Exception
     * @param database_Front used if routine needs to check database.
     * @param httpSession used if routine needs extra info from a session attribute.
     */
    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession httpSession)
    throws Database_Record_Exception, Database_Record_Constructor_Exception {
        
        int count;
        
        
        
        
        {
            count=0;
            Outside_User outside_User = new Outside_User();
            try {
                outside_User.setComp_Id(this.getComp_Id().getValue());
                outside_User.setType_Index(Outside_User.OUTSIDE_USER_TYPES[Outside_User.OUTSIDE_USER_TYPE_INDEX_PERSON]);
                outside_User.setPerson_Number(this.getPerson_Number().getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(outside_User, "WHERE " +
                outside_User.getComp_Id().toWhere_Clause() + " AND " +
                outside_User.getType_Index().toWhere_Clause() + " AND " +
                outside_User.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + outside_User.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
        
        
        {
            count=0;
            Person_Additional_Phone person_Additional_Phone = new Person_Additional_Phone();
            try {
                person_Additional_Phone.setComp_Id(this.comp_Id.getValue());
                person_Additional_Phone.setPerson_Number(this.person_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(person_Additional_Phone, "WHERE " +
                person_Additional_Phone.getComp_Id().toWhere_Clause() + " AND " +
                person_Additional_Phone.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + person_Additional_Phone.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
        
        {
            count=0;
            Person_Additional_E_Mail person_Additional_E_Mail = new Person_Additional_E_Mail();
            try {
                person_Additional_E_Mail.setComp_Id(this.comp_Id.getValue());
                person_Additional_E_Mail.setPerson_Number(this.person_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(person_Additional_E_Mail, "WHERE " +
                person_Additional_E_Mail.getComp_Id().toWhere_Clause() + " AND " +
                person_Additional_E_Mail.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + person_Additional_E_Mail.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
        
        
        {
            count=0;
            Person_Additional_Address person_Additional_Address = new Person_Additional_Address();
            try {
                person_Additional_Address.setComp_Id(this.comp_Id.getValue());
                person_Additional_Address.setPerson_Number(this.person_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(person_Additional_Address, "WHERE " +
                person_Additional_Address.getComp_Id().toWhere_Clause() + " AND " +
                person_Additional_Address.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + person_Additional_Address.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
        
        
        
        {
            count=0;
            Cust cust = new Cust();
            try {
                cust.setComp_Id(this.comp_Id.getValue());
                cust.setPerson_Number(this.person_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(cust, "WHERE " +
                cust.getComp_Id().toWhere_Clause() + " AND " +
                cust.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + cust.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
        
        
        
        
        {
            count=0;
            Student student = new Student();
            try {
                student.setComp_Id(this.comp_Id.getValue());
                student.setPerson_Number(this.person_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(student, "WHERE " +
                student.getComp_Id().toWhere_Clause() + " AND " +
                student.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + student.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
        
        {
            count=0;
            Family family = new Family();
            try {
                family.setComp_Id(this.comp_Id.getValue());
                family.setPerson_Number(this.person_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(family, "WHERE " +
                family.getComp_Id().toWhere_Clause() + " AND " +
                family.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + family.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
        {
            if(Subsystem.is_Subsystem_Installed(httpSession, "Enroll_Now_Admin")){
                
                
                count=0;
                Teacher teacher = new Teacher();
                try {
                    teacher.setComp_Id(this.comp_Id.getValue());
                    teacher.setPerson_Number(this.person_Number.getValue());
                }catch(DFException dfe){
                    throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
                }
                try{
                    count =database_Front.getCount_Of_Records(teacher, "WHERE " +
                    teacher.getComp_Id().toWhere_Clause() + " AND " +
                    teacher.getPerson_Number().toWhere_Clause());
                }catch(Database_Front_Exception dbfe){
                    throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
                }
                if(count>0)
                    throw new Database_Record_Exception("Cannot delete because there are " + teacher.getEntities_Name() + " for this " + this.getEntity_Name()+".");
            }
        }
        return;
    }
    
    
    
    
    /**
     * Used by Database_Front to return many records.
     * @param array_Size is the size of the array required.
     * @return Person[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Person[] person_Records;
        person_Records = new Person[array_Size];
        return person_Records;
    }
    
    
    
    
    
    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {
        
        StringBuffer out = new StringBuffer();
        
        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + comp_Id.getSQL_Declaration());
        out.append("\t" + person_Number.getSQL_Declaration());
        out.append("\t" + first_Name.getSQL_Declaration());
        out.append("\t" + last_Name.getSQL_Declaration());
        out.append("\t" + member_Of_Family_Number.getSQL_Declaration());
        out.append("\t" + address_Type.getSQL_Declaration());
        out.append("\t" + street_Address.getSQL_Declaration());
        out.append("\t" + city.getSQL_Declaration());
        out.append("\t" + state_Abbr.getSQL_Declaration());
        out.append("\t" + zip_Code.getSQL_Declaration());
        out.append("\t" + phone_Type.getSQL_Declaration());
        out.append("\t" + phone.getSQL_Declaration());
        out.append("\t" + e_Mail_Type.getSQL_Declaration());
        out.append("\t" + e_Mail.getSQL_Declaration());
        out.append("\t" + date_Of_Birth_Status.getSQL_Declaration());
        out.append("\t" + date_Of_Birth.getSQL_Declaration());
        out.append("\t" + gender.getSQL_Declaration());
        out.append("\t" + obsolete.getSQL_Declaration());
        out.append("\t" + last_Used_Additional_Address_Number.getSQL_Declaration());
        out.append("\t" + last_Used_Additional_E_Mail_Number.getSQL_Declaration());
        
        
        out.append("        PRIMARY KEY  (`" + comp_Id.getField_Name() + "`, `" + person_Number.getField_Name() + "`)\n");
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
                return "ORDER BY comp_Id ASC, person_Number ASC";
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
                        + " AND person_Number > '" + SQLUtil.encode(this.person_Number.toString()) + "' ";
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
                    preparedStatement = this.person_Number.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){
                    
                    preparedStatement = this.first_Name.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.last_Name.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.member_Of_Family_Number.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.address_Type.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.street_Address.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.city.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.state_Abbr.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.zip_Code.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.phone_Type.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.phone.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.e_Mail_Type.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.e_Mail.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.date_Of_Birth_Status.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.date_Of_Birth.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.gender.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.obsolete.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.last_Used_Additional_Address_Number.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.last_Used_Additional_E_Mail_Number.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Person record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Person result;
        try {
            result = new Person();
            
            result.comp_Id.setValue_ResultSet(resultSet);
            result.person_Number.setValue_ResultSet(resultSet);
            result.first_Name.setValue_ResultSet(resultSet);
            result.last_Name.setValue_ResultSet(resultSet);
            result.member_Of_Family_Number.setValue_ResultSet(resultSet);
            result.address_Type.setValue_ResultSet(resultSet);
            result.street_Address.setValue_ResultSet(resultSet);
            result.city.setValue_ResultSet(resultSet);
            result.state_Abbr.setValue_ResultSet(resultSet);
            result.zip_Code.setValue_ResultSet(resultSet);
            result.phone_Type.setValue_ResultSet(resultSet);
            result.phone.setValue_ResultSet(resultSet);
            result.e_Mail_Type.setValue_ResultSet(resultSet);
            result.e_Mail.setValue_ResultSet(resultSet);
            result.date_Of_Birth_Status.setValue_ResultSet(resultSet);
            result.date_Of_Birth.setValue_ResultSet(resultSet);
            result.gender.setValue_ResultSet(resultSet);
            result.obsolete.setValue_ResultSet(resultSet);
            result.last_Used_Additional_Address_Number.setValue_ResultSet(resultSet);
            result.last_Used_Additional_E_Mail_Number.setValue_ResultSet(resultSet);
            
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
        error_Count += this.person_Number.validate_Request(prefix, httpServletRequest, suffix);
        
        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + ".validate_Key_Fields_Of_Request(): Invalid Key Data Field(s). " + error_Count + " errors.");
        
    }
    
    
    
    
    
    /** Validate non-index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     * @throws Database_Record_Constructor_Exception if a value could not be validated against foriegn record.
     */
    public synchronized void validate_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix)
    throws Database_Record_Exception, Database_Record_Constructor_Exception {
        
        int error_Count =0;
        
        error_Count += this.first_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.last_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.member_Of_Family_Number.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.address_Type.validate_Request(prefix, httpServletRequest, suffix);
        if(this.address_Type.getValue().equals(USE_FAMILY_TEXT)){
            this.street_Address.validate_Request(prefix, httpServletRequest, suffix);
            this.city.validate_Request(prefix, httpServletRequest, suffix);
            this.state_Abbr.validate_Request(prefix, httpServletRequest, suffix);
            this.zip_Code.validate_Request(prefix, httpServletRequest, suffix);
        }else{
            error_Count += this.street_Address.validate_Request(prefix, httpServletRequest, suffix);
            error_Count += this.city.validate_Request(prefix, httpServletRequest, suffix);
            error_Count += this.state_Abbr.validate_Request(prefix, httpServletRequest, suffix);
            error_Count += this.zip_Code.validate_Request(prefix, httpServletRequest, suffix);
        }
        
        error_Count += this.phone_Type.validate_Request(prefix, httpServletRequest, suffix);
        if(this.phone_Type.getValue().equals(USE_FAMILY_TEXT)){
            this.phone.validate_Request(prefix, httpServletRequest, suffix);
        }else{
            error_Count += this.phone.validate_Request(prefix, httpServletRequest, suffix);
        }
        
        error_Count += this.e_Mail_Type.validate_Request(prefix, httpServletRequest, suffix);
        if(this.e_Mail_Type.getValue().equals(USE_FAMILY_TEXT)){
            this.e_Mail.validate_Request(prefix, httpServletRequest, suffix);
        }else{
            error_Count += this.e_Mail.validate_Request(prefix, httpServletRequest, suffix);
        }
        
        error_Count += this.date_Of_Birth_Status.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.date_Of_Birth.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.gender.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.obsolete.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.last_Used_Additional_Address_Number.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.last_Used_Additional_E_Mail_Number.validate_Request(prefix, httpServletRequest, suffix);
        
        
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
        programmers_Field_List.add( comp_Id );
        programmers_Field_List.add( person_Number );
        programmers_Field_List.add( first_Name );
        programmers_Field_List.add( last_Name );
        programmers_Field_List.add( member_Of_Family_Number );
        programmers_Field_List.add( address_Type );
        programmers_Field_List.add( street_Address );
        programmers_Field_List.add( city );
        programmers_Field_List.add( state_Abbr );
        programmers_Field_List.add( zip_Code );
        programmers_Field_List.add( phone_Type );
        programmers_Field_List.add( phone );
        programmers_Field_List.add( e_Mail_Type );
        programmers_Field_List.add( e_Mail );
        programmers_Field_List.add( date_Of_Birth_Status );
        programmers_Field_List.add( date_Of_Birth );
        programmers_Field_List.add( gender );
        programmers_Field_List.add( obsolete );
        programmers_Field_List.add( last_Used_Additional_Address_Number );
        programmers_Field_List.add( last_Used_Additional_E_Mail_Number );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
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
     * Getter for property member_Of_Family_Number.
     * @return Value of property member_Of_Family_Number.
     */
    public DFInteger getMember_of_Family_Number() {
        return member_Of_Family_Number;
    }
    
    
    /**
     * Setter for property member_Of_Family_Number.
     * @param member_Of_Family_Number New value of property member_Of_Family_Number.
     */
    public void setMember_of_Family_Number(Integer member_Of_Family_Number) throws DFException {
        this.member_Of_Family_Number.setValue(member_Of_Family_Number);
    }
    
    
    /**
     * Getter for property address_Type.
     * @return Value of property address_Type.
     */
    public DFString_Enumerated getAddress_Type() {
        return address_Type;
    }
    
    
    /**
     * Setter for property address_Type.
     * @param address_Type New value of property address_Type.
     */
    public void setAddress_Type(String address_Type) throws DFException {
        this.address_Type.setValue(address_Type);
    }
    
    
    /**
     * Getter for property street_Address.
     * @return Value of property street_Address.
     */
    public DFString getStreet_Address() {
        return street_Address;
    }
    
    
    /**
     * Setter for property street_Address.
     * @param street_Address New value of property street_Address.
     */
    public void setStreet_Address(String street_Address) throws DFException {
        this.street_Address.setValue(street_Address);
    }
    
    
    /**
     * Getter for property city.
     * @return Value of property city.
     */
    public DFString getCity() {
        return city;
    }
    
    
    /**
     * Setter for property city.
     * @param city New value of property city.
     */
    public void setCity(String city) throws DFException {
        this.city.setValue(city);
    }
    
    
    /**
     * Getter for property state_Abbr.
     * @return Value of property state_Abbr.
     */
    public DFState_Abbr getState_Abbr() {
        return state_Abbr;
    }
    
    
    /**
     * Setter for property state_Abbr.
     * @param state_Abbr New value of property state_Abbr.
     */
    public void setState_Abbr(String state_Abbr) throws DFException {
        this.state_Abbr.setValue(state_Abbr);
    }
    
    
    /**
     * Getter for property zip_Code.
     * @return Value of property zip_Code.
     */
    public DFZip_Code getZip_Code() {
        return zip_Code;
    }
    
    
    /**
     * Setter for property zip_Code.
     * @param zip_Code New value of property zip_Code.
     */
    public void setZip_Code(String zip_Code) throws DFException {
        this.zip_Code.setValue(zip_Code);
    }
    
    
    /**
     * Getter for property phone_Type.
     * @return Value of property phone_Type.
     */
    public DFString_Enumerated getPhone_Type() {
        return phone_Type;
    }
    
    
    /**
     * Setter for property phone_Type.
     * @param phone_Type New value of property phone_Type.
     */
    public void setPhone_Type(String phone_Type) throws DFException {
        this.phone_Type.setValue(phone_Type);
    }
    
    
    /**
     * Getter for property phone.
     * @return Value of property phone.
     */
    public DFPhone getPhone() {
        return phone;
    }
    
    
    /**
     * Setter for property phone.
     * @param phone New value of property phone.
     */
    public void setPhone(String phone) throws DFException {
        this.phone.setValue(phone);
    }
    
    
    /**
     * Getter for property e_Mail_Type.
     * @return Value of property e_Mail_Type.
     */
    public DFString_Enumerated getE_Mail_Type() {
        return e_Mail_Type;
    }
    
    
    /**
     * Setter for property e_Mail_Type.
     * @param e_Mail_Type New value of property e_Mail_Type.
     */
    public void setE_Mail_Type(String e_Mail_Type) throws DFException {
        this.e_Mail_Type.setValue(e_Mail_Type);
    }
    
    
    /**
     * Getter for property e_Mail.
     * @return Value of property e_Mail.
     */
    public DFE_Mail getE_Mail() {
        return e_Mail;
    }
    
    
    /**
     * Setter for property e_Mail.
     * @param e_Mail New value of property e_Mail.
     */
    public void setE_Mail(String e_Mail) throws DFException {
        this.e_Mail.setValue(e_Mail);
    }
    
    
    /**
     * Getter for property date_Of_Birth_Status.
     * @return Value of property date_Of_Birth_Status.
     */
    public DFString_Enumerated getDate_Of_Birth_Status() {
        return date_Of_Birth_Status;
    }
    
    
    /**
     * Setter for property date_Of_Birth_Status.
     * @param date_Of_Birth_Status New value of property date_Of_Birth_Status.
     */
    public void setDate_Of_Birth_Status(String date_Of_Birth_Status) throws DFException {
        this.date_Of_Birth_Status.setValue(date_Of_Birth_Status);
    }
    
    
    /**
     * Getter for property date_Of_Birth.
     * @return Value of property date_Of_Birth.
     */
    public DFDate getDate_Of_Birth() {
        return date_Of_Birth;
    }
    
    
    /**
     * Setter for property date_Of_Birth.
     * @param date_Of_Birth New value of property date_Of_Birth.
     */
    public void setDate_Of_Birth(Date date_Of_Birth) throws DFException {
        this.date_Of_Birth.setValue(date_Of_Birth);
    }
    
    
    /**
     * Getter for property gender.
     * @return Value of property gender.
     */
    public DFString_Enumerated getGender() {
        return gender;
    }
    
    
    /**
     * Setter for property gender.
     * @param gender New value of property gender.
     */
    public void setGender(String gender) throws DFException {
        this.gender.setValue(gender);
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
     * Getter for property last_Used_Additional_Address_Number.
     * @return Value of property last_Used_Additional_Address_Number.
     */
    public DFInteger getLast_Used_Additional_Address_Number() {
        return last_Used_Additional_Address_Number;
    }
    
    
    /**
     * Setter for property last_Used_Additional_Address_Number.
     * @param last_Used_Additional_Address_Number New value of property last_Used_Additional_Address_Number.
     */
    public void setLast_Used_Additional_Address_Number(Integer last_Used_Additional_Address_Number) throws DFException {
        this.last_Used_Additional_Address_Number.setValue(last_Used_Additional_Address_Number);
    }
    
    
    /**
     * Getter for property last_Used_Additional_E_Mail_Number.
     * @return Value of property last_Used_Additional_E_Mail_Number.
     */
    public DFInteger getLast_Used_Additional_E_Mail_Number() {
        return last_Used_Additional_E_Mail_Number;
    }
    
    
    /**
     * Setter for property last_Used_Additional_E_Mail_Number.
     * @param last_Used_Additional_E_Mail_Number New value of property last_Used_Additional_E_Mail_Number.
     */
    public void setLast_Used_Additional_E_Mail_Number(Integer last_Used_Additional_E_Mail_Number) throws DFException {
        this.last_Used_Additional_E_Mail_Number.setValue(last_Used_Additional_E_Mail_Number);
    }
    
    
    //============== End jdbr Generated Section ===========================
    
    
    
    public static Integer get_Next_Additional_Address_Number(Database_Front database_Front, Person passed_Person)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception, Database_Record_Exception, Database_Record_Constructor_Exception {
        Person person;
        person = (Person)database_Front.getLocked_Record(passed_Person);
        person.last_Used_Additional_Address_Number.addEqual(1);
        while(database_Front.isMatch(new Person_Additional_Address(person.getComp_Id().getValue(), person.getPerson_Number().getValue(), person.last_Used_Additional_Address_Number.getValue()))){
            person.last_Used_Additional_Address_Number.addEqual(1);
        }
        database_Front.updateRecord(person);
        return person.last_Used_Additional_Address_Number.getValue();
    }
    
    public static Integer get_Next_Additional_E_Mail_Number(Database_Front database_Front, Person passed_Person)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception, Database_Record_Exception, Database_Record_Constructor_Exception {
        Person person;
        person = (Person)database_Front.getLocked_Record(passed_Person);
        person.last_Used_Additional_E_Mail_Number.addEqual(1);
        while(database_Front.isMatch(new Person_Additional_E_Mail(person.getComp_Id().getValue(), person.getPerson_Number().getValue(), person.last_Used_Additional_E_Mail_Number.getValue()))){
            person.last_Used_Additional_E_Mail_Number.addEqual(1);
        }
        database_Front.updateRecord(person);
        return person.last_Used_Additional_E_Mail_Number.getValue();
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
    
    
    public static Person get_Possible_Person(Database_Front database_Front, DFString comp_Id, DFInteger person_Number) {
        return get_Possible_Person(database_Front, comp_Id.getValue(), person_Number.getValue());
    }
    
    public static Person get_Possible_Person(Database_Front database_Front, String comp_Id, Integer person_Number) {
        
        Person person;
        try {
            person = new Person();
        }catch(Database_Record_Constructor_Exception dbcre){
            return null;
        }
        
        if(person_Number.intValue() == 0)
            return person;
        try {
            person.setLast_Name("No Such Person");
        }
        catch(DFException dfe){
            ;
        }
        
        
        try {
            person = (Person) database_Front.getRecord(new Person(comp_Id, person_Number));
            person.use_Family_Update(database_Front);
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
        return person;
    }
    
    
    // if person record has contact types of USE_FAMILY_TEXT, get updated info
    public void use_Family_Update(Database_Front database_Front) {
        Family family = Family.get_Possible_Family(database_Front, this.getComp_Id().getValue(), this.getMember_of_Family_Number().getValue());
        Person  family_Person = Person.get_Possible_Person(database_Front, family.getComp_Id().getValue(), family.getPerson_Number().getValue());
        
        if(this.getAddress_Type().getValue().equals(Person.USE_FAMILY_TEXT)){
            try{
                this.setStreet_Address(family_Person.getStreet_Address().getValue());
                this.setCity(family_Person.getCity().getValue());
                this.setState_Abbr(family_Person.getState_Abbr().getValue());
                this.setZip_Code(family_Person.getZip_Code().getValue());
            }catch(DFException dfe){
                ;
            }
        }
        
        if(this.getPhone_Type().getValue().equals(Person.USE_FAMILY_TEXT)){
            try{
                this.setPhone(family_Person.getPhone().getValue());
            }catch(DFException dfe){
                ;
            }
        }
        
        if(this.getE_Mail_Type().getValue().equals(Person.USE_FAMILY_TEXT)){
            try{
                this.setE_Mail(family_Person.getE_Mail().getValue());
            }catch(DFException dfe){
                ;
            }
        }
        
        
    }
    
    public int getAge()
    throws DFException {
        DFDate today = new DFDate("today", "Today");
        return getAge(today);
    }
    
    public int getAge(DFDate As_Of_Date){
        TimeZone tz = TimeZone.getTimeZone(Calendar_Plus.BOD_TimeZone_Name);
        Calendar today_Calendar = Calendar.getInstance(tz);
        today_Calendar.setTime(As_Of_Date.getValue());
        
        int todays_Day = today_Calendar.get(Calendar.DAY_OF_MONTH);
        int todays_Month = today_Calendar.get(Calendar.MONTH);
        int todays_Year = today_Calendar.get(Calendar.YEAR);
        
        // when was their last birthday
        Date date_Of_Birth = this.date_Of_Birth.getValue();
        Calendar date_Of_Birth_Calendar = Calendar.getInstance(tz);
        date_Of_Birth_Calendar.setTime(date_Of_Birth);
        
        
        int birthdays_Day = date_Of_Birth_Calendar.get(Calendar.DAY_OF_MONTH);
        int birthdays_Month = date_Of_Birth_Calendar.get(Calendar.MONTH);
        int birthdays_Year = date_Of_Birth_Calendar.get(Calendar.YEAR);
        
        int age;
        
        if(todays_Month > birthdays_Month){
            // then they have had a birthday this year
            age = todays_Year - birthdays_Year;
        }else if(todays_Month == birthdays_Month && todays_Day>=birthdays_Day){
            // then they have had a birthday this year
            age = todays_Year - birthdays_Year;
        }else{
            // then their last birthday was last year;
            age = (todays_Year-1) - birthdays_Year;
        }
        return age;
    }
    
    
    public DFString getAge_Text()
    throws DFException {
        DFDate today = new DFDate("today", "Today");
        return getAge_Text(today);
    }
    
    public DFString getAge_Text(DFDate As_Of_Date)
    throws DFException {
        DFString age = new DFString("age", "Age",7);
        if(this.date_Of_Birth_Status.getValue().equals("Unknown")){
            age.setValue("Unknown");
        }else if(this.date_Of_Birth_Status.getValue().equals("Over 18")){
            age.setValue("Over 18");
        }else {
            age.setValue(""+ getAge(As_Of_Date));
        }
        return age;
    }
    
    public DFString getDate_Of_Birth_Text()
    throws DFException {
        DFString dob = new DFString("dob", "Date Of Birth",10);
        if(this.date_Of_Birth_Status.getValue().equals("Unknown")){
            dob.setValue("Unknown");
        }else if(this.date_Of_Birth_Status.getValue().equals("Over 18")){
            dob.setValue("Over 18");
        }else {
            dob.setValue(this.getDate_Of_Birth().toString());
        }
        return dob;
    }
    
    
    public Family get_Aka_Family(Database_Front database_Front)
    throws WPFatal_Exception{
        Family    family=null;
        try {
            family= new Family();
        }catch(Database_Record_Constructor_Exception dbrce){
            throw new WPFatal_Exception(dbrce.getMessage());
        }
        Family[] families = null;
        try{
            families = (Family[])database_Front.getMany_Records(family, "WHERE " +
            this.comp_Id.toWhere_Clause() + " AND " +
            this.person_Number.toWhere_Clause());
        }catch(Database_Front_Exception dbfe){
            throw new WPFatal_Exception(dbfe.getMessage());
        }
        if(families != null){
            family = families[0];
            return family;
        }else{
            return null;
        }
    }
    
    
    public Student get_Aka_Student(Database_Front database_Front)
    throws WPFatal_Exception{
        Student    student = null;
        try {
            student = new Student();
        }catch(Database_Record_Constructor_Exception dbrce){
            throw new WPFatal_Exception(dbrce.getMessage());
        }
        Student[] students = null;
        try{
            students = (Student[])database_Front.getMany_Records(student, "WHERE " +
            this.comp_Id.toWhere_Clause() + " AND " +
            this.person_Number.toWhere_Clause());
        }catch(Database_Front_Exception dbfe){
            throw new WPFatal_Exception(dbfe.getMessage());
        }
        if(students != null){
            student = students[0];
            return student;
        }else{
            return null;
        }
    }
    
    public Cust get_Aka_Cust(Database_Front database_Front)
    throws WPFatal_Exception{
        Cust    cust = null;
        try {
            cust = new Cust();
        }catch(Database_Record_Constructor_Exception dbrce){
            throw new WPFatal_Exception(dbrce.getMessage());
        }
        
        try {
            cust.setKind_Of_Customer(Cust.KINDS_OF_CUSTOMERS[Cust.KINDS_OF_CUSTOMERS_INDEX_PERSON]);
        }catch(DFException dfe){
            throw new WPFatal_Exception(dfe.getMessage());
        }
        
        Cust[] custs = null;
        try{
            custs = (Cust[])database_Front.getMany_Records(cust, "WHERE " +
            this.comp_Id.toWhere_Clause() + " AND " +
            cust.getKind_Of_Customer().toWhere_Clause() + " AND " +
            this.person_Number.toWhere_Clause());
        }catch(Database_Front_Exception dbfe){
            throw new WPFatal_Exception(dbfe.getMessage());
        }
        if(custs != null){
            cust = custs[0];
            return cust;
        }else{
            return null;
        }
    }
    
    
    
    
    public Teacher get_Aka_Teacher(Web_Process web_Process)
    throws WPFatal_Exception{
        if(Subsystem.is_Subsystem_Installed(web_Process.getMy_Request().getSession(), "Enroll_Now_Admin")){
            
            Teacher    teacher = null;
            try {
                teacher = new Teacher();
            }catch(Database_Record_Constructor_Exception dbrce){
                throw new WPFatal_Exception(dbrce.getMessage());
            }
            Teacher[] teachers = null;
            try{
                teachers = (Teacher[])web_Process.getDatabase_Front().getMany_Records(teacher, "WHERE " +
                this.comp_Id.toWhere_Clause() + " AND " +
                this.person_Number.toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new WPFatal_Exception(dbfe.getMessage());
            }
            if(teachers != null){
                teacher = teachers[0];
                return teacher;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    
    
    
    public Outside_User get_Aka_Outside_User(Web_Process web_Process)
    throws WPFatal_Exception{
        
        Outside_User    outside_User = null;
        try {
            outside_User = new Outside_User(this);
        }catch(Database_Record_Constructor_Exception dbrce){
            throw new WPFatal_Exception(dbrce.getMessage());
        }
        Outside_User[] outside_Users = null;
        try{
            outside_Users = (Outside_User[])web_Process.getDatabase_Front().getMany_Records(outside_User,
            "WHERE " + outside_User.getComp_Id().toWhere_Clause() +
            " AND " +  outside_User.getType_Index().toWhere_Clause() +
            " AND " +  outside_User.getPerson_Number().toWhere_Clause());
        }catch(Database_Front_Exception dbfe){
            throw new WPFatal_Exception(dbfe.getMessage());
        }
        if(outside_Users != null){
            outside_User = outside_Users[0];
            return outside_User;
        }else{
            return null;
        }
        
    }
    
    
    
    public void prt_HTML_Addresses_Table(Web_Process web_Process, String Id)
    throws Database_Record_Constructor_Exception, WPFatal_Exception {
        
        HTML_Table_Tag out_Table = new HTML_Table_Tag(Id, web_Process.getOut(), "Record_View_Table");
        out_Table.setUse_Even_Odd_Classes(true);
        out_Table.do_Display_HTML_Table_Begin_Std();
        out_Table.do_Display_HTML_TBody_Begin_Std();
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        out_Table.do_Display_HTML_TH_Element_Col_Span("Addresses", 4);
        out_Table.do_Display_HTML_Row_End();
        
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(out_Table, this.getAddress_Type());
        out_Table.do_Display_HTML_TH_Element_Col_Span("Address", 3);
        out_Table.do_Display_HTML_Row_End();
        
        
        
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, this.getAddress_Type());
        web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, this.getStreet_Address());
        out_Table.do_Display_HTML_Row_End();
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        out_Table.do_Display_HTML_TH_Element("&nbsp;");
        web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, this.getCity());
        web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, this.getState_Abbr());
        web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, this.getZip_Code());
        out_Table.do_Display_HTML_Row_End();
        
        
        String address_Where_SQL_Clause = " WHERE " +
        this.getComp_Id().toWhere_Clause() + " AND " +
        this.getPerson_Number().toWhere_Clause();
        
        
        Person_Additional_Address[] person_Additional_Addresses;
        
        try {
            person_Additional_Addresses = (Person_Additional_Address[]) web_Process.getDatabase_Front().getMany_Records(new Person_Additional_Address(), address_Where_SQL_Clause);
        }catch(Database_Front_Exception dbfe){
            throw new WPFatal_Exception(dbfe.getMessage());
        }
        if(person_Additional_Addresses != null){
            for(int i=0; i<person_Additional_Addresses.length; i++){
                
                out_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, person_Additional_Addresses[i].getAddress_Type());
                web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, person_Additional_Addresses[i].getStreet_Address());
                out_Table.do_Display_HTML_Row_End();
                
                
                out_Table.do_Display_HTML_Row_Begin_Std();
                out_Table.do_Display_HTML_TH_Element("&nbsp;");
                web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, person_Additional_Addresses[i].getCity());
                web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, person_Additional_Addresses[i].getState_Abbr());
                web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, person_Additional_Addresses[i].getZip_Code());
                out_Table.do_Display_HTML_Row_End();
                
            }
        }
        
        
        
        out_Table.do_Display_HTML_TBody_End();
        out_Table.do_Display_HTML_Table_End();
    }
    
    
    
    public void prt_HTML_Phones_Table(Web_Process web_Process, String Id)
    throws Database_Record_Constructor_Exception, WPFatal_Exception {
        
        HTML_Table_Tag out_Table = new HTML_Table_Tag(Id, web_Process.getOut(), "Record_View_Table");
        
        out_Table.setUse_Even_Odd_Classes(true);
        out_Table.do_Display_HTML_Table_Begin_Std();
        out_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        out_Table.do_Display_HTML_TH_Element_Col_Span("Phones", 2);
        out_Table.do_Display_HTML_Row_End();
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(out_Table, this.getPhone_Type());
        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(out_Table, this.getPhone());
        out_Table.do_Display_HTML_Row_End();
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, this.getPhone_Type());
        web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, this.getPhone());
        out_Table.do_Display_HTML_Row_End();
        
        
        String phone_Where_SQL_Clause = " WHERE " +
        this.getComp_Id().toWhere_Clause() + " AND " +
        this.getPerson_Number().toWhere_Clause();
        
        
        Person_Additional_Phone[] person_Additional_Phones;
        
        try {
            person_Additional_Phones = (Person_Additional_Phone[]) web_Process.getDatabase_Front().getMany_Records(new Person_Additional_Phone(), phone_Where_SQL_Clause);
        }catch(Database_Front_Exception dbfe){
            throw new WPFatal_Exception(dbfe.getMessage());
        }
        if(person_Additional_Phones != null){
            for(int i=0; i<person_Additional_Phones.length; i++){
                
                out_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, person_Additional_Phones[i].getPhone_Type());
                web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, person_Additional_Phones[i].getPhone());
                out_Table.do_Display_HTML_Row_End();
                
            }
        }
        
        out_Table.do_Display_HTML_TBody_End();
        out_Table.do_Display_HTML_Table_End();
        
    }
    
    
    
    public void prt_HTML_E_Mails_Table(Web_Process web_Process, String Id)
    throws Database_Record_Constructor_Exception, WPFatal_Exception {
        
        HTML_Table_Tag out_Table = new HTML_Table_Tag(Id, web_Process.getOut(), "Record_View_Table");
        
        out_Table.setUse_Even_Odd_Classes(true);
        out_Table.do_Display_HTML_Table_Begin_Std();
        out_Table.do_Display_HTML_TBody_Begin_Std();
        
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        out_Table.do_Display_HTML_TH_Element_Col_Span("E-Mails", 2);
        out_Table.do_Display_HTML_Row_End();
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(out_Table, this.getE_Mail_Type());
        web_Process.do_Display_HTML_DF_Display_In_Table_As_Th(out_Table, this.getE_Mail());
        out_Table.do_Display_HTML_Row_End();
        
        out_Table.do_Display_HTML_Row_Begin_Std();
        web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, this.getE_Mail_Type());
        web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, this.getE_Mail());
        out_Table.do_Display_HTML_Row_End();
        
        
        String e_Mail_Where_SQL_Clause = " WHERE " +
        this.getComp_Id().toWhere_Clause() + " AND " +
        this.getPerson_Number().toWhere_Clause();
        
        
        Person_Additional_E_Mail[] person_Additional_E_Mails;
        
        try {
            person_Additional_E_Mails = (Person_Additional_E_Mail[]) web_Process.getDatabase_Front().getMany_Records(new Person_Additional_E_Mail(), e_Mail_Where_SQL_Clause);
        }catch(Database_Front_Exception dbfe){
            throw new WPFatal_Exception(dbfe.getMessage());
        }
        if(person_Additional_E_Mails != null){
            for(int i=0; i<person_Additional_E_Mails.length; i++){
                
                out_Table.do_Display_HTML_Row_Begin_Std();
                web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, person_Additional_E_Mails[i].getE_Mail_Type());
                web_Process.do_Display_HTML_DF_Display_In_Table(out_Table, person_Additional_E_Mails[i].getE_Mail());
                out_Table.do_Display_HTML_Row_End();
                
            }
        }
        
        out_Table.do_Display_HTML_TBody_End();
        out_Table.do_Display_HTML_Table_End();
        
    }
    
    
    public Family get_My_Family_By_Membership(Database_Front database_Front) {
        if(this.member_Of_Family_Number.getValue().intValue()==0)
            return null;
        Family family=null;
        try {
            family = (Family)database_Front.getRecord(new Family(this.comp_Id.getValue(), this.getMember_of_Family_Number().getValue()));
        }
        catch(Database_Front_Exception dbfe){
            return null;
        }catch(Database_Front_No_Results_Exception dbfe){
            return null;
        }catch(Database_Record_Constructor_Exception dbrce){
            return null;
        }
        return family;
    }
    
    
    
    
    
    
    
    public static void personA_Become_A_Customer_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        
        Person person = (Person) web_Process.getAttribute("person");
        
        web_Process.launch_Task("Cs100", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
        "&loading_Action="+StringPlus.http_Encode("Create_From_Person") +
        "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()));
        
        return;
    }
    
    
    
    public static void personA_Become_A_Family_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        
        Person person = (Person) web_Process.getAttribute("person");
        
        web_Process.launch_Task("Enty200", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
        "&loading_Action="+StringPlus.http_Encode("Create_From_Person") +
        "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()));
        
        return;
    }
    
    
    
    public static void personA_Become_A_Student_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        
        Person person = (Person) web_Process.getAttribute("person");
        
        web_Process.launch_Task("Eno100", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
        "&loading_Action="+StringPlus.http_Encode("Create_From_Person") +
        "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()));
        
        return;
    }
    
    
    
    public static void personA_Become_A_Teacher_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        
        Person person = (Person) web_Process.getAttribute("person");
        
        web_Process.launch_Task("Ena600", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
        "&loading_Action="+StringPlus.http_Encode("Create_From_Person") +
        "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()));
        
        return;
    }
    
    
    
    
    
    public static void personA_Select_Member_Of_Family(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        Person person = (Person) web_Process.getAttribute("person");
        
        web_Process.launch_Task("Enty200", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
        "&loading_Family_Number="+StringPlus.http_Encode(person.getMember_of_Family_Number().toString()));
        return;
    }
    
    
    
    
    public static void personP_Select_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        Person temp_Person;
        temp_Person=new Person();
        
        try{
            temp_Person.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
        }
        catch(Database_Record_Exception dbre){
            web_Process.error("Bad "+ temp_Person.getPerson_Number().getField_Title()+".", dbre);
            return;
        }
        
        web_Process.launch_Task("Enty100", "&loading_Comp_Id="+StringPlus.http_Encode(temp_Person.getComp_Id().getValue()) +
        "&loading_Person_Number="+StringPlus.http_Encode(temp_Person.getPerson_Number().toString())
        );
        
        return;
    }
    
    
    public static void personA_Add_Address(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        Person person = (Person) web_Process.getAttribute("person");
        
        web_Process.launch_Task("Enty101", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
        "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()) +
        "&loading_Action="+StringPlus.http_Encode("Add Address")
        );
        return;
    }
    
    public static void personA_Add_E_Mail(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        Person person = (Person) web_Process.getAttribute("person");
        
        web_Process.launch_Task("Enty102", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
        "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString()) +
        "&loading_Action="+StringPlus.http_Encode("Add E-Mail")
        );
        return;
    }
    
    public static void personA_Add_Phone(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        Person person = (Person) web_Process.getAttribute("person");
        
        web_Process.launch_Task("Enty103", "&loading_Comp_Id="+StringPlus.http_Encode(person.getComp_Id().getValue()) +
        "&loading_Person_Number="+StringPlus.http_Encode(person.getPerson_Number().toString())+
        "&loading_Action="+StringPlus.http_Encode("Add Phone#")
        );
        return;
    }
    
    
    
    
    
    public static void personA_Add_Login(Web_Process web_Process) {
        Person person = (Person) web_Process.getAttribute("person");
        try {
            web_Process.launch_Task("Enty400", "&second_Action=add_Person");
        }catch(ServletException se){
            web_Process.error_End(se);
            return;
        }catch(IOException ioe){
            web_Process.error_End(ioe);
            return;
        }
        return;
    }
    
    public void personA_Edit_Login(Web_Process web_Process) {
        Person person = (Person) web_Process.getAttribute("person");
        try {
            web_Process.launch_Task("Enty400", "&second_Action=edit_Person");
        }catch(ServletException se){
            web_Process.error_End(se);
            return;
        }catch(IOException ioe){
            web_Process.error_End(ioe);
            return;
        }
        return;
    }
    
    
    
    
    
    
    
    
    
    
    
    public int validate_Record(Database_Front database_Front)
    throws Database_Record_Constructor_Exception, Database_Record_Exception  {
        int error_Count=0;
        
        
        this.e_Mail.setRequest_Error("");
        this.e_Mail_Type.setRequest_Error("");
        
        Outside_User    temp_Outside_User = null;
        temp_Outside_User = new Outside_User();
        
        try {
            temp_Outside_User.setComp_Id(this.getComp_Id().getValue());
            temp_Outside_User.setType_Index(Outside_User.OUTSIDE_USER_TYPES[Outside_User.OUTSIDE_USER_TYPE_INDEX_PERSON]);
            temp_Outside_User.setPerson_Number(this.getPerson_Number().getValue());
        }catch(DFException dfe){
            throw new Database_Record_Exception(dfe.getMessage());
        }
        
        Outside_User[] outside_Users = null;
        try{
            outside_Users = (Outside_User[])database_Front.getMany_Records(temp_Outside_User,
            "WHERE " + temp_Outside_User.getComp_Id().toWhere_Clause() +
            " AND " +  temp_Outside_User.getType_Index().toWhere_Clause() +
            " AND " +  temp_Outside_User.getPerson_Number().toWhere_Clause());
        }catch(Database_Front_Exception dbfe){
            throw new Database_Record_Exception(dbfe.getMessage());
        }
        if(outside_Users != null){
            Outside_User outside_User = outside_Users[0];
            if(!outside_User.getOutside_User_Id().getValue().equals(this.e_Mail.getValue())){
                // problem.  changed email address.
                if(this.getE_Mail_Type().getValue().equals(Person.USE_FAMILY_TEXT)){
                    this.e_Mail_Type.setRequest_Error("This old E-Mail address is used to login.  It can not be a Use Family type.");
                    error_Count++;
                }else{
                    // Can we use the new one?
                    temp_Outside_User = new Outside_User();
                    
                    try {
                        temp_Outside_User.setComp_Id(this.getComp_Id().getValue());
                        temp_Outside_User.setOutside_User_Id(this.getE_Mail().getValue());
                    }catch(DFException dfe){
                        this.e_Mail.setRequest_Error("This old E-Mail address is used to login. This new E-Mail address is invalid for this purpose.");
                        error_Count++;
                    }
                    if(error_Count==0){
                        try {
                            if(database_Front.isMatch(temp_Outside_User)){
                                this.e_Mail.setRequest_Error("Someone else is using this E-Mail address to login.");
                                error_Count++;
                            }
                        }catch(Database_Front_Exception dbfe){
                            throw new Database_Record_Exception(dbfe.getMessage());
                        }
                    }
                }
            }
        }
        
        
        
        
        this.member_Of_Family_Number.setRequest_Error("");
        
        if(this.member_Of_Family_Number.getValue().intValue()==0){
            if(this.address_Type.getValue().equals(USE_FAMILY_TEXT)){
                this.address_Type.setRequest_Error("Not a Member of a Family");
                error_Count++;
            }
            if(this.phone_Type.getValue().equals(USE_FAMILY_TEXT)){
                this.address_Type.setRequest_Error("Not a Member of a Family");
                error_Count++;
            }
            if(this.e_Mail_Type.getValue().equals(USE_FAMILY_TEXT)){
                this.address_Type.setRequest_Error("Not a Member of a Family");
                error_Count++;
            }
        }else {
            Family family;
            try {
                family = (Family) database_Front.getRecord(new Family(this.comp_Id.getValue(), this.member_Of_Family_Number.getValue()));
            }catch(Database_Front_Exception dfe){
                this.member_Of_Family_Number.setRequest_Error("Database Error.");
                return 1;
            }catch(Database_Front_No_Results_Exception dfe){
                this.member_Of_Family_Number.setRequest_Error("Must specify a valid Family.");
                return 1;
            }
        }
        
        
        
        int count=0;
        Cust cust = new Cust();
        try{
            count =database_Front.getCount_Of_Records(cust, "WHERE " +
            this.comp_Id.toWhere_Clause() + " AND " +
            this.person_Number.toWhere_Clause());
        }catch(Database_Front_Exception dbfe){
            throw new Database_Record_Exception("Cannot validate because :" + dbfe.getMessage());
        }
        if(count>0){
            if(this.address_Type.getValue().equals(USE_FAMILY_TEXT)){
                this.address_Type.setRequest_Error("Not allowed for a Customer");
                error_Count++;
            }
            if(this.phone_Type.getValue().equals(USE_FAMILY_TEXT)){
                this.phone_Type.setRequest_Error("Not allowed for a Customer");
                error_Count++;
            }
            if(this.e_Mail_Type.getValue().equals(USE_FAMILY_TEXT)){
                this.e_Mail_Type.setRequest_Error("Not allowed for a Customer");
                error_Count++;
            }
        }
        
        
        Family family = new Family();
        Family[] families=null;
        try {
            families = (Family[])database_Front.getMany_Records(family, "WHERE " +
            this.comp_Id.toWhere_Clause() + " AND " +
            this.person_Number.toWhere_Clause());
        }
        catch(Database_Front_Exception dbfe){
            throw new Database_Record_Exception("Cannot validate because :" + dbfe.getMessage());
        }
        if(families!=null){
            if(this.address_Type.getValue().equals(USE_FAMILY_TEXT)){
                this.address_Type.setRequest_Error("Not allowed for Family's Main Contact");
                error_Count++;
            }
            if(this.phone_Type.getValue().equals(USE_FAMILY_TEXT)){
                this.phone_Type.setRequest_Error("Not allowed for Family's Main Contact");
                error_Count++;
            }
            if(this.e_Mail_Type.getValue().equals(USE_FAMILY_TEXT)){
                this.e_Mail_Type.setRequest_Error("Not allowed for Family's Main Contact");
                error_Count++;
            }
            if(this.member_Of_Family_Number.getValue().intValue()!=0){
                for(int i=0; i<families.length;i++){
                    family = families[i];
                    if(family.getFamily_Number().getValue().intValue() == this.member_Of_Family_Number.getValue().intValue()){
                        this.member_Of_Family_Number.setRequest_Error("Can not be a member of the same family for which they are the Main contact.");
                        error_Count++;
                        break;
                    }
                }
            }
        }
        
        if(error_Count==0)
            use_Family_Update(database_Front);
        
        
        return error_Count;
    }
    
    
    public void update_Family_Member_Info(Database_Front database_Front)
    throws WPFatal_Exception, Database_Record_Constructor_Exception, DFException, Database_Front_Exception, Database_Front_Deadlock_Exception {
        
        
        Family family = get_Aka_Family(database_Front);
        if(family==null)
            return;
        
        
        Person temp_Person = new Person(this.comp_Id.getValue(), new Integer(0));
        temp_Person.setMember_of_Family_Number(family.getFamily_Number().getValue());
        
        
        String where_Clause = " WHERE " +
        temp_Person.getComp_Id().toWhere_Clause() + " AND " +
        temp_Person.getMember_of_Family_Number().toWhere_Clause();
        
        Person[] persons = (Person[]) database_Front.getMany_Records(temp_Person, where_Clause);
        if(persons==null)
            return;
        
        
        for(int i=0; i<persons.length;i++){
            if(persons[i].address_Type.getValue().equals(USE_FAMILY_TEXT) ||
            
            persons[i].phone_Type.getValue().equals(USE_FAMILY_TEXT) ||
            persons[i].e_Mail_Type.getValue().equals(USE_FAMILY_TEXT)){
                
                
                
                Person person;
                try {
                    person = (Person) database_Front.getLocked_Record(persons[i]);
                }catch(Database_Front_No_Results_Exception dbnre){
                    continue;
                }
                
                
                person.use_Family_Update(database_Front);
                database_Front.updateRecord(person);
            }
        }
    }
    
    
    
    
    
    public boolean update_Outside_User(Web_Process web_Process)
    throws Database_Record_Constructor_Exception {
    
            //---------- fix outside_User ------------
            
            
            Outside_User    temp_Outside_User = null;
            temp_Outside_User = new Outside_User();
            
            try {
                temp_Outside_User.setComp_Id(this.getComp_Id().getValue());
                temp_Outside_User.setType_Index(Outside_User.OUTSIDE_USER_TYPES[Outside_User.OUTSIDE_USER_TYPE_INDEX_PERSON]);
                temp_Outside_User.setPerson_Number(this.getPerson_Number().getValue());
            }catch(DFException dfe){
                web_Process.error_End(dfe);
                return false;
            }
            
            Outside_User[] outside_Users = null;
            try{
                outside_Users = (Outside_User[]) web_Process.getDatabase_Front().getMany_Records(temp_Outside_User,
                "WHERE " + temp_Outside_User.getComp_Id().toWhere_Clause() +
                " AND " +  temp_Outside_User.getType_Index().toWhere_Clause() +
                " AND " +  temp_Outside_User.getPerson_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                web_Process.error_End(dbfe);
                return false;
            }
            
            
            if(outside_Users != null){
                Outside_User outside_User = outside_Users[0];
                if(!outside_User.getOutside_User_Id().getValue().equals(this.getE_Mail().getValue())){
                    // problem.  changed email address.
                    if(this.getE_Mail_Type().getValue().equals(Person.USE_FAMILY_TEXT)){
                        web_Process.error("This old E-Mail address is used to login.  It can not be a Use Family type.", null);
                        return false;
                    }else{
                        // Can we use the new one?
                        temp_Outside_User = new Outside_User();
                        
                        try {
                            temp_Outside_User.setComp_Id(this.getComp_Id().getValue());
                            temp_Outside_User.setOutside_User_Id(this.getE_Mail().getValue());
                        }catch(DFException dfe){
                            web_Process.error("This old E-Mail address is used to login. This new E-Mail address is invalid for this purpose.", dfe);
                            return false;
                        }
                        
                        try {
                            if(web_Process.getDatabase_Front().isMatch(temp_Outside_User)){
                                web_Process.error("Someone else is using this E-Mail address to login.", null);
                                return false;
                            }
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return false;
                        }
                        
                        // so change the outside user
                        try {
                            web_Process.getDatabase_Front().deleteRecord(outside_User);
                            outside_User.setOutside_User_Id(this.getE_Mail().getValue());
                            web_Process.getDatabase_Front().addRecord(outside_User);
                        }catch(Database_Front_Exception dbfe){
                            web_Process.error_End(dbfe);
                            return false;
                        }catch(DFException dfe){
                            try {
                                web_Process.getDatabase_Front().addRecord(outside_User);
                            }catch(Database_Front_Exception dbfe){
                                web_Process.error_End(dbfe);
                                return false;
                            }
                            web_Process.error("This old E-Mail address is used to login. This new E-Mail address is invalid for this purpose.", dfe);
                            return false;
                        }
                    }
                }
            }
            
            
            return true;
    }
    
    
    public static boolean personA_Output_View_Record_Buttons(Web_Process web_Process, HTML_Table_Tag button_Table){
       
        Person person = (Person) web_Process.getAttribute("person");
        if(person== null){
            web_Process.error_End("Can't find person attribute.", null);
            return false;
        }
        
        Outside_User outside_User;
        boolean valid_Email=true;
        try {
            outside_User = person.get_Aka_Outside_User(web_Process);
        }catch(WPFatal_Exception wpfe){
            // no good user id
            valid_Email=false;
            outside_User=null;
        }
        
        try {
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Add Address", "", button_Table, person.getClass(), "personA_Add_Address");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Add Phone#", "", button_Table, person.getClass(), "personA_Add_Phone");
            button_Table.do_Display_HTML_Row_End();
            
            button_Table.do_Display_HTML_Row_Begin_Std();
            web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Add E-Mail", "", button_Table, person.getClass(), "personA_Add_E_Mail");
            button_Table.do_Display_HTML_Row_End();
         
            if(valid_Email){
                if(outside_User == null){
                    button_Table.do_Display_HTML_Row_Begin_Std();
                    web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Add Login", "", button_Table, person.getClass(), "personA_Add_Login");
                    button_Table.do_Display_HTML_Row_End();
                    
                }else{
                    
                    button_Table.do_Display_HTML_Row_Begin_Std();
                    web_Process.do_Display_HTML_In_Form_Submit_Button_In_Table("action", "Edit Login", "", button_Table, person.getClass(), "personA_Edit_Login");
                    button_Table.do_Display_HTML_Row_End();
                }
            }
            
        }catch(WPFatal_Exception wpfe){
            web_Process.error_End("Failed to make buttons.", wpfe);
            return false;
        }
        
         return true;
    }
    
    
}
