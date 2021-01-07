/*
 * Person_Additional_E_Mail.java
 *
 * Created on July 30, 2006, 2:43 PM
 */

package entity_Management.datum;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 *
 * @author  dgleeson
 */
public class Person_Additional_E_Mail extends Database_Record_Base {
    
    private DFString_Filled	comp_Id;
    private DFInteger person_Number;
    private DFInteger person_Additional_E_Mail_Number;


    private DFString_Enumerated e_Mail_Type;
    private DFE_Mail  e_Mail;

    public static final String[] E_MAIL_TYPES = { "Home", "Office", "School" };

    public static final int PERSON_ADDITIONAL_E_MAIL_NUMBER_LEN=3;

    public static DFInteger DFInit_Person_Additional_E_Mail_Number() 
    throws DFException {
        return new DFInteger("person_Additional_E_Mail_Number", "Additional E-Mail#", PERSON_ADDITIONAL_E_MAIL_NUMBER_LEN);
    }
    


    //============== Begin jdbr Constructor Section ===========================


    // Empty Constructor
    /** Creates a new instance of Person_Additional_E_Mail
     * @throws Database_Record_Constructor_Exception
     */
    public Person_Additional_E_Mail() throws Database_Record_Constructor_Exception {
        super("Person_Additional_E_Mail", "Person_Additional_E_Mails");
        try {
            this.comp_Id =  Comp.DFInit_Comp_Id();
            this.person_Number =  Person.DFInit_Person_Number();
            this.person_Additional_E_Mail_Number =  DFInit_Person_Additional_E_Mail_Number();
            this.e_Mail_Type =  new DFString_Enumerated("e_Mail_Type", "E-Mail Type", E_MAIL_TYPES);
            this.e_Mail =  new DFE_Mail("e_Mail", "E-Mail");

            DFBase[] index =   { this.comp_Id, this.person_Number, this.person_Additional_E_Mail_Number };
            this.setPrimary_Index_DFields(index);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }




    // Keys Only Constructor
    /** Creates a new instance of Person_Additional_E_Mail with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Person_Additional_E_Mail(String comp_Id, Integer person_Number, Integer person_Additional_E_Mail_Number) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.comp_Id.setValue(comp_Id);
            this.person_Number.setValue(person_Number);
            this.person_Additional_E_Mail_Number.setValue(person_Additional_E_Mail_Number);

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
        return "person_additional_e_mail";
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
     * @return Person_Additional_E_Mail[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Person_Additional_E_Mail[] person_Additional_E_Mail_Records;
        person_Additional_E_Mail_Records = new Person_Additional_E_Mail[array_Size];
        return person_Additional_E_Mail_Records;
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
        out.append("\t" + person_Additional_E_Mail_Number.getSQL_Declaration());
        out.append("\t" + e_Mail_Type.getSQL_Declaration());
        out.append("\t" + e_Mail.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + comp_Id.getField_Name() + "`, `" + person_Number.getField_Name() + "`, `" + person_Additional_E_Mail_Number.getField_Name() + "`)\n");
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
                return "ORDER BY comp_Id ASC, person_Number ASC, person_Additional_E_Mail_Number ASC";
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
                    + " AND person_Number = '" + SQLUtil.encode(this.person_Number.toString()) + "' "
                    + " AND person_Additional_E_Mail_Number > '" + SQLUtil.encode(this.person_Additional_E_Mail_Number.toString()) + "' ";
            case 1:
                return "WHERE comp_Id = '" + SQLUtil.encode(this.comp_Id.toString()) + "' "
                    + " AND person_Number > '" + SQLUtil.encode(this.person_Number.toString()) + "' ";
            case 2:
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
                    preparedStatement = this.person_Additional_E_Mail_Number.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){

                    preparedStatement = this.e_Mail_Type.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Person_Additional_E_Mail record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Person_Additional_E_Mail result;
        try {
            result = new Person_Additional_E_Mail();

            result.comp_Id.setValue_ResultSet(resultSet);
            result.person_Number.setValue_ResultSet(resultSet);
            result.person_Additional_E_Mail_Number.setValue_ResultSet(resultSet);
            result.e_Mail_Type.setValue_ResultSet(resultSet);
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
        error_Count += this.person_Number.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.person_Additional_E_Mail_Number.validate_Request(prefix, httpServletRequest, suffix);

        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + ".validate_Key_Fields_Of_Request(): Invalid Key Data Field(s). " + error_Count + " errors.");

    }





    /** Validate non-index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     */
    public synchronized void validate_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix) throws Database_Record_Exception {

        int error_Count =0;

        error_Count += this.e_Mail_Type.validate_Request(prefix, httpServletRequest, suffix);
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
        programmers_Field_List.add( person_Number );
        programmers_Field_List.add( person_Additional_E_Mail_Number );
        programmers_Field_List.add( e_Mail_Type );
        programmers_Field_List.add( e_Mail );
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
     * Getter for property person_Additional_E_Mail_Number.
     * @return Value of property person_Additional_E_Mail_Number.
     */
    public DFInteger getPerson_Additional_E_Mail_Number() {
        return person_Additional_E_Mail_Number;
    }


    /**
     * Setter for property person_Additional_E_Mail_Number.
     * @param person_Additional_E_Mail_Number New value of property person_Additional_E_Mail_Number.
     */
    public void setPerson_Additional_E_Mail_Number(Integer person_Additional_E_Mail_Number) throws DFException {
        this.person_Additional_E_Mail_Number.setValue(person_Additional_E_Mail_Number);
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


    //============== End jdbr Generated Section ===========================


    
    public static void person_Additional_E_MailP_Select_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
            
            Person_Additional_E_Mail temp_Person_E_Mail_Address;
            temp_Person_E_Mail_Address=new Person_Additional_E_Mail();
            
            try{
                temp_Person_E_Mail_Address.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
            }
            catch(Database_Record_Exception dbre){
                web_Process.error("Bad "+ temp_Person_E_Mail_Address.getPerson_Additional_E_Mail_Number().getField_Title()+".", dbre);
                return;
            }
            web_Process.launch_Task("Enty102", "&loading_Comp_Id="+StringPlus.http_Encode(temp_Person_E_Mail_Address.getComp_Id().getValue()) +
            "&loading_Person_Number="+StringPlus.http_Encode(temp_Person_E_Mail_Address.getPerson_Number().toString()) +
            "&loading_Person_Additional_E_Mail_Number="+StringPlus.http_Encode(temp_Person_E_Mail_Address.getPerson_Additional_E_Mail_Number().toString())
            );
            return;
    }


}
