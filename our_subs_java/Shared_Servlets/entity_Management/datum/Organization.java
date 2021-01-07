/*
 * Organization.java
 *
 * Created on August 1, 2006, 6:46 AM
 */

package entity_Management.datum;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import customer_Management.data.*;
import enroll_Now_Office.data.*;
import enroll_Now_Performance.data.*;
import entity_Management.datum.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 *
 * @author  dgleeson
 */
public class Organization extends Database_Record_Base {

    private DFString_Filled	comp_Id;
    private DFInteger organization_Number;

    private DFString    name;
    private DFBoolean   alpha_Alias_Different_From_Name;
    private DFString    alpha_Alias;
    private DFString    organization_Type_Id;


    private DFString street_Address;
    private DFString city;
    private DFState_Abbr state_Abbr;
    private DFZip_Code   zip_Code;

    private DFString phone_Contact_Type;
    private DFPhone  phone;

    private DFString fax_Contact_Type;
    private DFPhone fax;

    private DFString  e_Mail_Contact_Type;
    private DFE_Mail  e_Mail;

    private DFURL web_Site;

    private DFBoolean obsolete;

    private DFString_Text_Box description;


    public static final int ORGANIZATION_NUMBER_LEN=7;

    public static DFInteger DFInit_Organization_Number()
    throws DFException {
        return new DFInteger("organization_Number", "Organization#", ORGANIZATION_NUMBER_LEN);
    }





    //============== Begin jdbr Constructor Section ===========================


    // Empty Constructor
    /** Creates a new instance of Organization
     * @throws Database_Record_Constructor_Exception
     */
    public Organization() throws Database_Record_Constructor_Exception {
        super("Organization", "Organizations");
        try {
            this.comp_Id =  Comp.DFInit_Comp_Id();
            this.organization_Number =  DFInit_Organization_Number();

            this.name =  new DFString("name", "Name", 1, 40);
            this.alpha_Alias_Different_From_Name =  new DFBoolean("alpha_Alias_Different_From_Name", "Alpha Alias Different From Name");
            this.alpha_Alias =  new DFString("alpha_Alias", "Alphabetical Alias", 40);

            this.organization_Type_Id =  Organization_Type.DFInit_Organization_Type_Id();

            this.street_Address =  new DFString("street_Address", "Street Address", 40);
            this.city =  new DFString("city", "City", 30);
            this.state_Abbr =  new DFState_Abbr("state_Abbr", "State");
            this.zip_Code =  new DFZip_Code("zip_Code", "Zip Code");


            this.phone_Contact_Type = Organization_Contact_Type.DFInit_Organization_Contact_Type_Id("phone_Contact_Type", "Phone Contact Type");
            this.phone =  new DFPhone("phone", "Phone");

            this.fax_Contact_Type = Organization_Contact_Type.DFInit_Organization_Contact_Type_Id("fax_Contact_Type", "Fax Contact Type");
            this.fax =  new DFPhone("fax", "Fax");

            this.e_Mail_Contact_Type = Organization_Contact_Type.DFInit_Organization_Contact_Type_Id("e_Mail_Contact_Type", "E-Mail Contact Type");
            this.e_Mail =  new DFE_Mail("e_Mail", "E-Mail");

            this.web_Site =  new DFURL("web_Site", "WebSite");
            this.obsolete =  new DFBoolean("obsolete", "Obsolete");
            this.description =  new DFString_Text_Box("description", "Description", 40);

            DFBase[] index =   { this.comp_Id, this.organization_Number };
            this.setPrimary_Index_DFields(index);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }




    // Keys Only Constructor
    /** Creates a new instance of Organization with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Organization(String comp_Id, Integer organization_Number) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.comp_Id.setValue(comp_Id);
            this.organization_Number.setValue(organization_Number);

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
        return "organization";
    }




    /** Used to check for reasons not to delete this record from table.
     * @throws Database_Record_Exception
     * @throws Database_Record_Constructor_Exception
     * @param database_Front used if routine needs to check database.
     * @param httpSession used if routine needs extra info from a session attribute.
     */
    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession httpSession) throws Database_Record_Exception, Database_Record_Constructor_Exception {
        int count=0;


        {
            count=0;
            Cust cust = new Cust();
            try {
                cust.setComp_Id(this.comp_Id.getValue());
                cust.setOrganization_Number(this.organization_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(cust, "WHERE " +
                cust.getComp_Id().toWhere_Clause() + " AND " +
                cust.getOrganization_Number().toWhere_Clause());
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
                student.setSchool_Organization_Number(this.organization_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(student, "WHERE " +
                student.getComp_Id().toWhere_Clause() + " AND " +
                student.getSchool_Organization_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + student.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }

        

        {
            count=0;
            Venue venue = new Venue();
            try {
                venue.setComp_Id(this.comp_Id.getValue());
                venue.setOrganization_Number(this.organization_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(venue, "WHERE " +
                venue.getComp_Id().toWhere_Clause() + " AND " +
                venue.getOrganization_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + venue.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }


        return;
    }




    /**
     * Used by Database_Front to return many records.
     * @param array_Size is the size of the array required.
     * @return Organization[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Organization[] organization_Records;
        organization_Records = new Organization[array_Size];
        return organization_Records;
    }





    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {

        StringBuffer out = new StringBuffer();

        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + comp_Id.getSQL_Declaration());
        out.append("\t" + organization_Number.getSQL_Declaration());
        out.append("\t" + name.getSQL_Declaration());
        out.append("\t" + alpha_Alias_Different_From_Name.getSQL_Declaration());
        out.append("\t" + alpha_Alias.getSQL_Declaration());
        out.append("\t" + organization_Type_Id.getSQL_Declaration());
        out.append("\t" + street_Address.getSQL_Declaration());
        out.append("\t" + city.getSQL_Declaration());
        out.append("\t" + state_Abbr.getSQL_Declaration());
        out.append("\t" + zip_Code.getSQL_Declaration());
        out.append("\t" + phone_Contact_Type.getSQL_Declaration());
        out.append("\t" + phone.getSQL_Declaration());
        out.append("\t" + fax_Contact_Type.getSQL_Declaration());
        out.append("\t" + fax.getSQL_Declaration());
        out.append("\t" + e_Mail_Contact_Type.getSQL_Declaration());
        out.append("\t" + e_Mail.getSQL_Declaration());
        out.append("\t" + web_Site.getSQL_Declaration());
        out.append("\t" + obsolete.getSQL_Declaration());
        out.append("\t" + description.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + comp_Id.getField_Name() + "`, `" + organization_Number.getField_Name() + "`)\n");
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
                return "ORDER BY comp_Id ASC, organization_Number ASC";
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
                + " AND organization_Number > '" + SQLUtil.encode(this.organization_Number.toString()) + "' ";
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
                    preparedStatement = this.organization_Number.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){

                    preparedStatement = this.name.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.alpha_Alias_Different_From_Name.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.alpha_Alias.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.organization_Type_Id.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.street_Address.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.city.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.state_Abbr.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.zip_Code.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.phone_Contact_Type.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.phone.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.fax_Contact_Type.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.fax.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.e_Mail_Contact_Type.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.e_Mail.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.web_Site.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.obsolete.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.description.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Organization record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Organization result;
        try {
            result = new Organization();

            result.comp_Id.setValue_ResultSet(resultSet);
            result.organization_Number.setValue_ResultSet(resultSet);
            result.name.setValue_ResultSet(resultSet);
            result.alpha_Alias_Different_From_Name.setValue_ResultSet(resultSet);
            result.alpha_Alias.setValue_ResultSet(resultSet);
            result.organization_Type_Id.setValue_ResultSet(resultSet);
            result.street_Address.setValue_ResultSet(resultSet);
            result.city.setValue_ResultSet(resultSet);
            result.state_Abbr.setValue_ResultSet(resultSet);
            result.zip_Code.setValue_ResultSet(resultSet);
            result.phone_Contact_Type.setValue_ResultSet(resultSet);
            result.phone.setValue_ResultSet(resultSet);
            result.fax_Contact_Type.setValue_ResultSet(resultSet);
            result.fax.setValue_ResultSet(resultSet);
            result.e_Mail_Contact_Type.setValue_ResultSet(resultSet);
            result.e_Mail.setValue_ResultSet(resultSet);
            result.web_Site.setValue_ResultSet(resultSet);
            result.obsolete.setValue_ResultSet(resultSet);
            result.description.setValue_ResultSet(resultSet);

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
        error_Count += this.organization_Number.validate_Request(prefix, httpServletRequest, suffix);

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
        error_Count += this.alpha_Alias_Different_From_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.alpha_Alias.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.organization_Type_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.street_Address.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.city.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.state_Abbr.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.zip_Code.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.phone_Contact_Type.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.phone.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.fax_Contact_Type.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.fax.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.e_Mail_Contact_Type.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.e_Mail.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.web_Site.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.obsolete.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.description.validate_Request(prefix, httpServletRequest, suffix);

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
        programmers_Field_List.add( organization_Number );
        programmers_Field_List.add( name );
        programmers_Field_List.add( alpha_Alias_Different_From_Name );
        programmers_Field_List.add( alpha_Alias );
        programmers_Field_List.add( organization_Type_Id );
        programmers_Field_List.add( street_Address );
        programmers_Field_List.add( city );
        programmers_Field_List.add( state_Abbr );
        programmers_Field_List.add( zip_Code );
        programmers_Field_List.add( phone_Contact_Type );
        programmers_Field_List.add( phone );
        programmers_Field_List.add( fax_Contact_Type );
        programmers_Field_List.add( fax );
        programmers_Field_List.add( e_Mail_Contact_Type );
        programmers_Field_List.add( e_Mail );
        programmers_Field_List.add( web_Site );
        programmers_Field_List.add( obsolete );
        programmers_Field_List.add( description );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }



    /**
     * @return Organization.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue() fails.
     */
    public synchronized Organization copy_Of() throws Database_Record_Constructor_Exception {
        Organization result;
        try {
            result = new Organization();

            result.setComp_Id(this.comp_Id.getValue());
            result.setOrganization_Number(this.organization_Number.getValue());
            result.setName(this.name.getValue());
            result.setAlpha_Alias_Different_From_Name(this.alpha_Alias_Different_From_Name.getValue());
            result.setAlpha_Alias(this.alpha_Alias.getValue());
            result.setOrganization_Type_Id(this.organization_Type_Id.getValue());
            result.setStreet_Address(this.street_Address.getValue());
            result.setCity(this.city.getValue());
            result.setState_Abbr(this.state_Abbr.getValue());
            result.setZip_Code(this.zip_Code.getValue());
            result.setPhone_Contact_Type(this.phone_Contact_Type.getValue());
            result.setPhone(this.phone.getValue());
            result.setFax_Contact_Type(this.fax_Contact_Type.getValue());
            result.setFax(this.fax.getValue());
            result.setE_Mail_Contact_Type(this.e_Mail_Contact_Type.getValue());
            result.setE_Mail(this.e_Mail.getValue());
            result.setWeb_Site(this.web_Site.getValue());
            result.setObsolete(this.obsolete.getValue());
            result.setDescription(this.description.getValue());

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
     * Getter for property alpha_Alias_Different_From_Name.
     * @return Value of property alpha_Alias_Different_From_Name.
     */
    public DFBoolean getAlpha_Alias_Different_From_Name() {
        return alpha_Alias_Different_From_Name;
    }


    /**
     * Setter for property alpha_Alias_Different_From_Name.
     * @param alpha_Alias_Different_From_Name New value of property alpha_Alias_Different_From_Name.
     */
    public void setAlpha_Alias_Different_From_Name(Boolean alpha_Alias_Different_From_Name) throws DFException {
        this.alpha_Alias_Different_From_Name.setValue(alpha_Alias_Different_From_Name);
    }


    /**
     * Getter for property alpha_Alias.
     * @return Value of property alpha_Alias.
     */
    public DFString getAlpha_Alias() {
        return alpha_Alias;
    }


    /**
     * Setter for property alpha_Alias.
     * @param alpha_Alias New value of property alpha_Alias.
     */
    public void setAlpha_Alias(String alpha_Alias) throws DFException {
        this.alpha_Alias.setValue(alpha_Alias);
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
     * Getter for property phone_Contact_Type.
     * @return Value of property phone_Contact_Type.
     */
    public DFString getPhone_Contact_Type() {
        return phone_Contact_Type;
    }


    /**
     * Setter for property phone_Contact_Type.
     * @param phone_Contact_Type New value of property phone_Contact_Type.
     */
    public void setPhone_Contact_Type(String phone_Contact_Type) throws DFException {
        this.phone_Contact_Type.setValue(phone_Contact_Type);
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
     * Getter for property fax_Contact_Type.
     * @return Value of property fax_Contact_Type.
     */
    public DFString getFax_Contact_Type() {
        return fax_Contact_Type;
    }


    /**
     * Setter for property fax_Contact_Type.
     * @param fax_Contact_Type New value of property fax_Contact_Type.
     */
    public void setFax_Contact_Type(String fax_Contact_Type) throws DFException {
        this.fax_Contact_Type.setValue(fax_Contact_Type);
    }


    /**
     * Getter for property fax.
     * @return Value of property fax.
     */
    public DFPhone getFax() {
        return fax;
    }


    /**
     * Setter for property fax.
     * @param fax New value of property fax.
     */
    public void setFax(String fax) throws DFException {
        this.fax.setValue(fax);
    }


    /**
     * Getter for property e_Mail_Contact_Type.
     * @return Value of property e_Mail_Contact_Type.
     */
    public DFString getE_Mail_Contact_Type() {
        return e_Mail_Contact_Type;
    }


    /**
     * Setter for property e_Mail_Contact_Type.
     * @param e_Mail_Contact_Type New value of property e_Mail_Contact_Type.
     */
    public void setE_Mail_Contact_Type(String e_Mail_Contact_Type) throws DFException {
        this.e_Mail_Contact_Type.setValue(e_Mail_Contact_Type);
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
     * Getter for property web_Site.
     * @return Value of property web_Site.
     */
    public DFURL getWeb_Site() {
        return web_Site;
    }


    /**
     * Setter for property web_Site.
     * @param web_Site New value of property web_Site.
     */
    public void setWeb_Site(String web_Site) throws DFException {
        this.web_Site.setValue(web_Site);
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
     * Getter for property description.
     * @return Value of property description.
     */
    public DFString_Text_Box getDescription() {
        return description;
    }


    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) throws DFException {
        this.description.setValue(description);
    }


    //============== End jdbr Generated Section ===========================


    
    public static Organization get_Possible_Organization(Database_Front database_Front, DFString comp_Id, DFInteger organization_Number) {
        return get_Possible_Organization(database_Front, comp_Id.getValue(), organization_Number.getValue());
    }
    
    public static Organization get_Possible_Organization(Database_Front database_Front, String comp_Id, Integer organization_Number) {

        Organization organization;
        try{
            organization = new Organization();
        }
        catch(Database_Record_Constructor_Exception dbre){
            return null;
        }
        if(organization_Number.intValue() == 0)
            return organization;
        try {
            organization.setName("No Such Organization");
        }
        catch(DFException dfe){
            ;
        }


        try {
            organization = (Organization) database_Front.getRecord(new Organization(comp_Id, organization_Number));
        }
        catch(Database_Front_Exception dbfe){
            ;
        }
        catch(Database_Record_Constructor_Exception dbfe){
            ;
        }
        catch(Database_Front_No_Results_Exception dbfe){
            ;
        }
        return organization;
    }



    public static void organizationP_Select_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {

            Organization temp_Organization;
            temp_Organization=new Organization();

            try{
                temp_Organization.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Organization.getOrganization_Number().getField_Title()+".", dbre);
                return;
            }


            web_Process.launch_Task("Enty300", "&loading_Comp_Id="+StringPlus.http_Encode(temp_Organization.getComp_Id().getValue()) +
            "&loading_Organization_Number="+StringPlus.http_Encode(temp_Organization.getOrganization_Number().toString()));

            return;
    }


    
    public int validate_Record(Database_Front database_Front)
    throws Database_Record_Constructor_Exception {
        
        int error_Count=0;
        
        this.organization_Type_Id.setRequest_Error("");
        Organization_Type temp_Organization_Type = new Organization_Type(this.comp_Id.getValue(), this.organization_Type_Id.getValue());
        try {
            if(!database_Front.isMatch(temp_Organization_Type)){
                this.organization_Type_Id.setRequest_Error("No Such "+ temp_Organization_Type.getEntities_Name());
                error_Count++;
            }
        }catch(Database_Front_Exception dbfe){
            this.organization_Type_Id.setRequest_Error("Database Error.");
            error_Count++;
        }

        
        this.phone_Contact_Type.setRequest_Error("");
        Organization_Contact_Type temp_Organization_Contact_Type = new Organization_Contact_Type(this.comp_Id.getValue(), this.organization_Type_Id.getValue(), this.phone_Contact_Type.getValue());
        try {
            if(!database_Front.isMatch(temp_Organization_Contact_Type)){
                this.phone_Contact_Type.setRequest_Error("No Such "+ temp_Organization_Contact_Type.getEntities_Name());
                error_Count++;
            }
        }catch(Database_Front_Exception dbfe){
            this.phone_Contact_Type.setRequest_Error("Database Error.");
            error_Count++;
        }

        
        this.e_Mail_Contact_Type.setRequest_Error("");
        temp_Organization_Contact_Type = new Organization_Contact_Type(this.comp_Id.getValue(), this.organization_Type_Id.getValue(), this.e_Mail_Contact_Type.getValue());
        try {
            if(!database_Front.isMatch(temp_Organization_Contact_Type)){
                this.e_Mail_Contact_Type.setRequest_Error("No Such "+ temp_Organization_Contact_Type.getEntities_Name());
                error_Count++;
            }
        }catch(Database_Front_Exception dbfe){
            this.e_Mail_Contact_Type.setRequest_Error("Database Error.");
            error_Count++;
        }

        
        this.fax_Contact_Type.setRequest_Error("");
        temp_Organization_Contact_Type = new Organization_Contact_Type(this.comp_Id.getValue(), this.organization_Type_Id.getValue(), this.fax_Contact_Type.getValue());
        try {
            if(!database_Front.isMatch(temp_Organization_Contact_Type)){
                this.fax_Contact_Type.setRequest_Error("No Such "+ temp_Organization_Contact_Type.getEntities_Name());
                error_Count++;
            }
        }catch(Database_Front_Exception dbfe){
            this.fax_Contact_Type.setRequest_Error("Database Error.");
            error_Count++;
        }

        
        if(!this.alpha_Alias_Different_From_Name.getValue().booleanValue()){
            try {
                this.alpha_Alias.setValue(this.name.getValue());
            }catch(DFException dfe){
                this.fax_Contact_Type.setRequest_Error(dfe.getMessage());
                error_Count++;
            }
        }
        

        return error_Count;
        
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
            cust.setKind_Of_Customer(Cust.KINDS_OF_CUSTOMERS[Cust.KINDS_OF_CUSTOMERS_INDEX_ORGANIZATION]);
            cust.setComp_Id(this.comp_Id.getValue());
            cust.setOrganization_Number(this.organization_Number.getValue());
        }catch(DFException dfe){
            throw new WPFatal_Exception(dfe.getMessage());
        }
        
        Cust[] custs = null;
        try{
            custs = (Cust[])database_Front.getMany_Records(cust, "WHERE " +
            cust.getComp_Id().toWhere_Clause() + " AND " +
            cust.getKind_Of_Customer().toWhere_Clause() + " AND " +
            cust.getOrganization_Number().toWhere_Clause());
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
    
    
    public Venue get_Aka_Venue(Database_Front database_Front)
    throws WPFatal_Exception{
        Venue    venue = null;
        try {
            venue = new Venue();
        }catch(Database_Record_Constructor_Exception dbrce){
            throw new WPFatal_Exception(dbrce.getMessage());
        }
        
        try {
            venue.setComp_Id(this.comp_Id.getValue());
            venue.setOrganization_Number(this.organization_Number.getValue());
        }catch(DFException dfe){
            throw new WPFatal_Exception(dfe.getMessage());
        }
        
        Venue[] venues = null;
        try{
            venues = (Venue[])database_Front.getMany_Records(venue, "WHERE " +
            venue.getComp_Id().toWhere_Clause() + " AND " +
            venue.getOrganization_Number().toWhere_Clause());
        }catch(Database_Front_Exception dbfe){
            throw new WPFatal_Exception(dbfe.getMessage());
        }
        if(venues != null){
            venue = venues[0];
            return venue;
        }else{
            return null;
        }
    }
    
    
    
    public static void organizationA_Become_A_Customer_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        
        Organization organization = (Organization) web_Process.getAttribute("organization");
        
        web_Process.launch_Task("Cs100", "&loading_Comp_Id="+StringPlus.http_Encode(organization.getComp_Id().getValue()) +
        "&loading_Action="+StringPlus.http_Encode("Create_From_Organization") +
        "&loading_Organization_Number="+StringPlus.http_Encode(organization.getOrganization_Number().toString()));
        
        return;
    }
    

    
    public static void organizationA_Become_A_Venue_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        
        Organization organization = (Organization) web_Process.getAttribute("organization");
        
        web_Process.launch_Task("Enp700", "&loading_Comp_Id="+StringPlus.http_Encode(organization.getComp_Id().getValue()) +
        "&loading_Action="+StringPlus.http_Encode("Create_From_Organization") +
        "&loading_Organization_Number="+StringPlus.http_Encode(organization.getOrganization_Number().toString()));
        
        return;
    }
    

}
