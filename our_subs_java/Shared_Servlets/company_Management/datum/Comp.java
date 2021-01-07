/*
 * Comp.java
 *
 * Created on January 6, 2005 3:34:24 PM CST
 */

/**
 *|------------------------------------------------------------------------------|
 *|       Copyright (c) 1985 thru 2002, 2003, 2004, 2005, 2006, 2007, 2008       |
 *|       Employees of American Liberator Technologies                           |
 *|       All Rights Reserved                                                    |
 *|                                                                              |
 *|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
 *|       Employees of American Liberator Technologies                           |
 *|       The copyright notice above does not evidence any                       |
 *|       actual or intended publication of such source code.                    |
 *|------------------------------------------------------------------------------|
 */

package company_Management.datum;

import com.amlibtech.data_fields.*;
import java.util.*;
import javax.servlet.http.*;

import com.amlibtech.web.data.*;
import com.amlibtech.util.*;
import com.amlibtech.web.servlet_exceptions.*;
import com.amlibtech.web.util.*;
import com.amlibtech.web.common_list.*;
import com.amlibtech.database.*;
import com.amlibtech.login.data.*;
import java.io.BufferedWriter;
import java.sql.*;


public class Comp extends Database_Record_Base {


    // Declaration Section

    private	DFString_Filled	comp_Id;
    private	DFString	name;
    private	DFString	address_Line1;
    private	DFString	address_Line2;
    private	DFString	city;
    private	DFState_Abbr	state_Abbr;
    private     DFZip_Code      zip_Code;
    private	DFString	federal_Income_Tax_Id_Num;
    private	DFPhone 	phone_Number;
    private	DFPhone 	fax_Number;
    private	DFPhone 	eight_Hundred_Number;

    private     DFURL           web_Site;
    private     DFE_Mail        e_Mail;

    public static final int COMP_ID_LEN = 2;

    public static DFString_Filled DFInit_Comp_Id()
    throws DFException {
        return  new DFString_Filled("comp_Id", "Company #", COMP_ID_LEN, DFString_Filled.FILL_TYPE_LEFT, '0');
    }



    //============== Begin jdbr Constructor Section ===========================


    // Empty Constructor
    /** Creates a new instance of Comp
     * @throws Database_Record_Constructor_Exception
     */
    public Comp() throws Database_Record_Constructor_Exception {
        super("Company", "Companies");
        try {
            this.comp_Id =  DFInit_Comp_Id();
            this.name =     new DFString("name", "Name", 1, 38);
            this.address_Line1 =    new DFString("address_Line1", "Address Line 1", 30);
            this.address_Line2 =    new DFString("address_Line2", "Address Line 2", 30);
            this.city =             new DFString("city", "City", 30);
            this.state_Abbr =       new DFState_Abbr("state_Abbr", "State");
            this.zip_Code =  	    new DFZip_Code("zip_Code", "Zip Code");
            this.federal_Income_Tax_Id_Num =    new DFString("federal_Income_Tax_Id_Num", "FEIN#", 12);
            this.phone_Number =     new DFPhone("phone_Number", "Phone#", 1);
            this.fax_Number =       new DFPhone("fax_Number", "FAX#");
            this.eight_Hundred_Number = new DFPhone("eight_Hundred_Number", "Toll Free#");
            this.web_Site =  new DFURL("web_Site", "Web Site");
            this.e_Mail =  new DFE_Mail("e_Mail", "E Mail");

            this.setPrimary_Index_DFields(this.comp_Id);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }




    // Keys Only Constructor
    /** Creates a new instance of Comp with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Comp(String comp_Id) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.comp_Id.setValue(comp_Id);

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
        return "comp";
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
     * @return Comp[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Comp[] comp_Records;
        comp_Records = new Comp[array_Size];
        return comp_Records;
    }





    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {

        StringBuffer out = new StringBuffer();

        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + comp_Id.getSQL_Declaration());
        out.append("\t" + name.getSQL_Declaration());
        out.append("\t" + address_Line1.getSQL_Declaration());
        out.append("\t" + address_Line2.getSQL_Declaration());
        out.append("\t" + city.getSQL_Declaration());
        out.append("\t" + state_Abbr.getSQL_Declaration());
        out.append("\t" + zip_Code.getSQL_Declaration());
        out.append("\t" + federal_Income_Tax_Id_Num.getSQL_Declaration());
        out.append("\t" + phone_Number.getSQL_Declaration());
        out.append("\t" + fax_Number.getSQL_Declaration());
        out.append("\t" + eight_Hundred_Number.getSQL_Declaration());
        out.append("\t" + web_Site.getSQL_Declaration());
        out.append("\t" + e_Mail.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + comp_Id.getField_Name() + "`)\n");
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
                return "ORDER BY comp_Id ASC";
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
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){

                    preparedStatement = this.name.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.address_Line1.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.address_Line2.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.city.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.state_Abbr.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.zip_Code.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.federal_Income_Tax_Id_Num.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.phone_Number.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.fax_Number.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.eight_Hundred_Number.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.web_Site.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.e_Mail.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Comp record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Comp result;
        try {
            result = new Comp();

            result.comp_Id.setValue_ResultSet(resultSet);
            result.name.setValue_ResultSet(resultSet);
            result.address_Line1.setValue_ResultSet(resultSet);
            result.address_Line2.setValue_ResultSet(resultSet);
            result.city.setValue_ResultSet(resultSet);
            result.state_Abbr.setValue_ResultSet(resultSet);
            result.zip_Code.setValue_ResultSet(resultSet);
            result.federal_Income_Tax_Id_Num.setValue_ResultSet(resultSet);
            result.phone_Number.setValue_ResultSet(resultSet);
            result.fax_Number.setValue_ResultSet(resultSet);
            result.eight_Hundred_Number.setValue_ResultSet(resultSet);
            result.web_Site.setValue_ResultSet(resultSet);
            result.e_Mail.setValue_ResultSet(resultSet);

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
        error_Count += this.address_Line1.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.address_Line2.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.city.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.state_Abbr.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.zip_Code.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.federal_Income_Tax_Id_Num.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.phone_Number.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.fax_Number.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.eight_Hundred_Number.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.web_Site.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.e_Mail.validate_Request(prefix, httpServletRequest, suffix);


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
        programmers_Field_List.add( name );
        programmers_Field_List.add( address_Line1 );
        programmers_Field_List.add( address_Line2 );
        programmers_Field_List.add( city );
        programmers_Field_List.add( state_Abbr );
        programmers_Field_List.add( zip_Code );
        programmers_Field_List.add( federal_Income_Tax_Id_Num );
        programmers_Field_List.add( phone_Number );
        programmers_Field_List.add( fax_Number );
        programmers_Field_List.add( eight_Hundred_Number );
        programmers_Field_List.add( web_Site );
        programmers_Field_List.add( e_Mail );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }



    /**
     * @return Comp.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue() fails.
     */
    public synchronized Comp copy_Of() throws Database_Record_Constructor_Exception {
        Comp result;
        try {
            result = new Comp();

            result.setComp_Id(this.comp_Id.getValue());
            result.setName(this.name.getValue());
            result.setAddress_Line1(this.address_Line1.getValue());
            result.setAddress_Line2(this.address_Line2.getValue());
            result.setCity(this.city.getValue());
            result.setState_Abbr(this.state_Abbr.getValue());
            result.setZip_Code(this.zip_Code.getValue());
            result.setFederal_Income_Tax_Id_Num(this.federal_Income_Tax_Id_Num.getValue());
            result.setPhone_Number(this.phone_Number.getValue());
            result.setFax_Number(this.fax_Number.getValue());
            result.setEight_Hundred_Number(this.eight_Hundred_Number.getValue());
            result.setWeb_Site(this.web_Site.getValue());
            result.setE_Mail(this.e_Mail.getValue());

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
     * Getter for property address_Line1.
     * @return Value of property address_Line1.
     */
    public DFString getAddress_Line1() {
        return address_Line1;
    }


    /**
     * Setter for property address_Line1.
     * @param address_Line1 New value of property address_Line1.
     */
    public void setAddress_Line1(String address_Line1) throws DFException {
        this.address_Line1.setValue(address_Line1);
    }


    /**
     * Getter for property address_Line2.
     * @return Value of property address_Line2.
     */
    public DFString getAddress_Line2() {
        return address_Line2;
    }


    /**
     * Setter for property address_Line2.
     * @param address_Line2 New value of property address_Line2.
     */
    public void setAddress_Line2(String address_Line2) throws DFException {
        this.address_Line2.setValue(address_Line2);
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
     * Getter for property federal_Income_Tax_Id_Num.
     * @return Value of property federal_Income_Tax_Id_Num.
     */
    public DFString getFederal_Income_Tax_Id_Num() {
        return federal_Income_Tax_Id_Num;
    }


    /**
     * Setter for property federal_Income_Tax_Id_Num.
     * @param federal_Income_Tax_Id_Num New value of property federal_Income_Tax_Id_Num.
     */
    public void setFederal_Income_Tax_Id_Num(String federal_Income_Tax_Id_Num) throws DFException {
        this.federal_Income_Tax_Id_Num.setValue(federal_Income_Tax_Id_Num);
    }


    /**
     * Getter for property phone_Number.
     * @return Value of property phone_Number.
     */
    public DFPhone getPhone_Number() {
        return phone_Number;
    }


    /**
     * Setter for property phone_Number.
     * @param phone_Number New value of property phone_Number.
     */
    public void setPhone_Number(String phone_Number) throws DFException {
        this.phone_Number.setValue(phone_Number);
    }


    /**
     * Getter for property fax_Number.
     * @return Value of property fax_Number.
     */
    public DFPhone getFax_Number() {
        return fax_Number;
    }


    /**
     * Setter for property fax_Number.
     * @param fax_Number New value of property fax_Number.
     */
    public void setFax_Number(String fax_Number) throws DFException {
        this.fax_Number.setValue(fax_Number);
    }


    /**
     * Getter for property eight_Hundred_Number.
     * @return Value of property eight_Hundred_Number.
     */
    public DFPhone getEight_Hundred_Number() {
        return eight_Hundred_Number;
    }


    /**
     * Setter for property eight_Hundred_Number.
     * @param eight_Hundred_Number New value of property eight_Hundred_Number.
     */
    public void setEight_Hundred_Number(String eight_Hundred_Number) throws DFException {
        this.eight_Hundred_Number.setValue(eight_Hundred_Number);
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


    //============== End jdbr Generated Section ===========================



}
