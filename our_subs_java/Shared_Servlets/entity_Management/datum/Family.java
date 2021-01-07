/*
 * Family.java
 *
 * Created on July 31, 2006, 5:46 PM
 */

package entity_Management.datum;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import com.amlibtech.web_Processes.*;
import company_Management.datum.*;
import customer_Management.data.*;
import enroll_Now_Office.data.*;
import entity_Management.datum.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Family extends Database_Record_Base {
    
    private DFString_Filled    comp_Id;
    private DFInteger   family_Number;
    
    // main contact person
    private DFInteger	person_Number;
    
    private DFString	cust_Id;
    private DFString_Text_Box  notes;
    
    
    
    public static final int FAMILY_NUMBER_LEN=7;
    
    public static DFInteger DFInit_Family_Number()
    throws DFException {
        return new DFInteger("family_Number", "Family #", FAMILY_NUMBER_LEN);
    }
    
    
    //============== Begin jdbr Constructor Section ===========================
    
    
    // Empty Constructor
    /** Creates a new instance of Family
     * @throws Database_Record_Constructor_Exception
     */
    public Family() throws Database_Record_Constructor_Exception {
        super("Family", "Families");
        try {
            this.comp_Id =  Comp.DFInit_Comp_Id();
            this.family_Number =  DFInit_Family_Number();
            
            this.person_Number =  Person.DFInit_Person_Number();
            this.cust_Id =  new DFString("cust_Id", "Customer Id", Cust.CUST_ID_LEN);
            this.notes =  new DFString_Text_Box("notes", "Notes", 80);
            
            DFBase[] index =   { this.comp_Id, this.family_Number };
            this.setPrimary_Index_DFields(index);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }
    
    
    
    
    // Keys Only Constructor
    /** Creates a new instance of Family with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Family(String comp_Id, Integer family_Number) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.comp_Id.setValue(comp_Id);
            this.family_Number.setValue(family_Number);
            
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
        return "family";
    }
    
    
    
    
    /** Used to check for reasons not to delete this record from table.
     * @throws Database_Record_Exception
     * @throws Database_Record_Constructor_Exception
     * @param database_Front used if routine needs to check database.
     * @param httpSession used if routine needs extra info from a session attribute.
     */
    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession httpSession)
    throws Database_Record_Exception, Database_Record_Constructor_Exception {
        int count=0;
        
        {
            count=0;
            Person person = new Person();
            try {
                person.setComp_Id(this.comp_Id.getValue());
                person.setMember_of_Family_Number(this.family_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(person, "WHERE " +
                person.getComp_Id().toWhere_Clause() + " AND " +
                person.getMember_of_Family_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + person.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
         
        {
            count=0;
            Reg_Ord_OC_Ln_Course reg_Ord_OC_Ln_Course = new Reg_Ord_OC_Ln_Course();
            
            try {
                reg_Ord_OC_Ln_Course.setComp_Id(this.comp_Id.getValue());
                reg_Ord_OC_Ln_Course.setFamily_Number(this.family_Number.getValue());
            }catch(DFException dfe){
                throw new Database_Record_Exception("Cannot delete because :" + dfe.getMessage());
            }
            try{
                count =database_Front.getCount_Of_Records(reg_Ord_OC_Ln_Course, "WHERE " +
                reg_Ord_OC_Ln_Course.getComp_Id().toWhere_Clause() + " AND " +
                reg_Ord_OC_Ln_Course.getFamily_Number().toWhere_Clause());
            }catch(Database_Front_Exception dbfe){
                throw new Database_Record_Exception("Cannot delete because :" + dbfe.getMessage());
            }
            if(count>0)
                throw new Database_Record_Exception("Cannot delete because there are " + reg_Ord_OC_Ln_Course.getEntities_Name() + " for this " + this.getEntity_Name()+".");
        }
        
        return;
    }
    
    
    
    
    /**
     * Used by Database_Front to return many records.
     * @param array_Size is the size of the array required.
     * @return Family[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Family[] family_Records;
        family_Records = new Family[array_Size];
        return family_Records;
    }
    
    
    
    
    
    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {
        
        StringBuffer out = new StringBuffer();
        
        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + comp_Id.getSQL_Declaration());
        out.append("\t" + family_Number.getSQL_Declaration());
        out.append("\t" + person_Number.getSQL_Declaration());
        out.append("\t" + cust_Id.getSQL_Declaration());
        out.append("\t" + notes.getSQL_Declaration());
        
        
        out.append("        PRIMARY KEY  (`" + comp_Id.getField_Name() + "`, `" + family_Number.getField_Name() + "`)\n");
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
                return "ORDER BY comp_Id ASC, family_Number ASC";
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
                        + " AND family_Number > '" + SQLUtil.encode(this.family_Number.toString()) + "' ";
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
                    preparedStatement = this.family_Number.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){
                    
                    preparedStatement = this.person_Number.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.cust_Id.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.notes.get_PreparedStatement_Value(preparedStatement, count++);
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
     * @return Family record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Family result;
        try {
            result = new Family();
            
            result.comp_Id.setValue_ResultSet(resultSet);
            result.family_Number.setValue_ResultSet(resultSet);
            result.person_Number.setValue_ResultSet(resultSet);
            result.cust_Id.setValue_ResultSet(resultSet);
            result.notes.setValue_ResultSet(resultSet);
            
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
        error_Count += this.family_Number.validate_Request(prefix, httpServletRequest, suffix);
        
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
        
        error_Count += this.person_Number.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.cust_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.notes.validate_Request(prefix, httpServletRequest, suffix);
        
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
        programmers_Field_List.add( family_Number );
        programmers_Field_List.add( person_Number );
        programmers_Field_List.add( cust_Id );
        programmers_Field_List.add( notes );
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
     * Getter for property family_Number.
     * @return Value of property family_Number.
     */
    public DFInteger getFamily_Number() {
        return family_Number;
    }
    
    
    /**
     * Setter for property family_Number.
     * @param family_Number New value of property family_Number.
     */
    public void setFamily_Number(Integer family_Number) throws DFException {
        this.family_Number.setValue(family_Number);
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
     * Getter for property cust_Id.
     * @return Value of property cust_Id.
     */
    public DFString getCust_Id() {
        return cust_Id;
    }
    
    
    /**
     * Setter for property cust_Id.
     * @param cust_Id New value of property cust_Id.
     */
    public void setCust_Id(String cust_Id) throws DFException {
        this.cust_Id.setValue(cust_Id);
    }
    
    
    /**
     * Getter for property notes.
     * @return Value of property notes.
     */
    public DFString_Text_Box getNotes() {
        return notes;
    }
    
    
    /**
     * Setter for property notes.
     * @param notes New value of property notes.
     */
    public void setNotes(String notes) throws DFException {
        this.notes.setValue(notes);
    }
    
    
    //============== End jdbr Generated Section ===========================
    
    
    
    /**
     * Getter for property name.
     * @return Value of property name  First name + space + last name.
     */
    public DFString getName_First_Then_Last(Database_Front database_Front){
        Person person = Person.get_Possible_Person(database_Front, this.comp_Id.getValue(), this.person_Number.getValue());
        DFString name;
        try {
            name = new DFString("name", "Name", 60);
            if(person.getFirst_Name().getValue().length()!=0 || person.getLast_Name().getValue().length()!=0)
                name.setValue(person.getFirst_Name().toString() + " " + person.getLast_Name().toString());
        }catch(DFException dfe){
            return null;
        }
        return name;
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.  Last anem + comma + space + first name
     */
    public DFString getName_Last_Then_First(Database_Front database_Front){
        Person person = Person.get_Possible_Person(database_Front, this.comp_Id.getValue(), this.person_Number.getValue());
        DFString name;
        try {
            name = new DFString("name", "Name", 60);
            if(person.getFirst_Name().getValue().length()!=0 || person.getLast_Name().getValue().length()!=0)
                name.setValue(person.getLast_Name().toString() + ", " + person.getFirst_Name().toString());
        }catch(DFException dfe){
            return null;
        }
        return name;
    }
    
    
    
    
    public static Family get_Possible_Family(Database_Front database_Front, DFString comp_Id, DFInteger family_Number) {
        return get_Possible_Family(database_Front, comp_Id.getValue(), family_Number.getValue());
    }
    
    
    public static Family get_Possible_Family(Database_Front database_Front, String comp_Id, Integer family_Number) {
        
        Family family;
        try{
            family = new Family();
        }
        catch(Database_Record_Constructor_Exception dbre){
            return null;
        }
        if(family_Number.intValue() == 0)
            return family;
        
        family.person_Number.subtractEqual(1);
        
        
        
        try {
            family = (Family) database_Front.getRecord(new Family(comp_Id, family_Number));
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
        return family;
    }
    
    public Person get_Aka_Person(Database_Front database_Front) {
        Person person = Person.get_Possible_Person(database_Front, this.comp_Id.getValue(), this.person_Number.getValue());
        return person;
    }
    
    public Person[] get_Main_And_Members_Person_Records(Database_Front database_Front)
    throws WPFatal_Exception {
        Person main_Person = get_Aka_Person(database_Front);
        
        Person temp_Person=null;
        try {
            temp_Person = new Person();
        }catch(Database_Record_Constructor_Exception dbrce){
            throw new WPFatal_Exception(dbrce.getMessage());
        }
        
        try {
            temp_Person.setComp_Id(this.getComp_Id().getValue());
            temp_Person.setMember_of_Family_Number(this.getFamily_Number().getValue());
        }catch(DFException dfe){
            throw new WPFatal_Exception(dfe.getMessage());
        }
        
        String person_Where_SQL_Clause = " WHERE " +
        temp_Person.getComp_Id().toWhere_Clause() + " AND " +
        temp_Person.getMember_of_Family_Number().toWhere_Clause();
        
        
        Person[] person_List=null;
        try {
            person_List = (Person[]) database_Front.getMany_Records(temp_Person, person_Where_SQL_Clause);
        }
        
        catch(Database_Front_Exception dbfe){
            throw new WPFatal_Exception(dbfe.getMessage());
        }
        
        Person[] answer_Persons;
        if(person_List == null){
            answer_Persons = new Person[1];
            answer_Persons[0]=main_Person;
        }else{
            answer_Persons = new Person[1+person_List.length];
            answer_Persons[0]=main_Person;
            for(int i = 0; i<person_List.length;i++){
                answer_Persons[i+1]=person_List[i];
            }
        }
        return answer_Persons;
    }
    
    
    public Cust get_My_Cust(Database_Front database_Front) {
        if(this.cust_Id.getValue().trim().equals(""))
            return null;
        Cust cust=null;
        try {
            cust = (Cust)database_Front.getRecord(new Cust(this.comp_Id.getValue(), this.getCust_Id().getValue()));
        }
        catch(Database_Front_Exception dbfe){
            return null;
        }catch(Database_Front_No_Results_Exception dbfe){
            return null;
        }catch(Database_Record_Constructor_Exception dbrce){
            return null;
        }
        return cust;
    }
    
    
    public static void familyP_Select_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        Family temp_Family=new Family();
        
        try{
            temp_Family.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
        }
        catch(Database_Record_Exception dbre){
            web_Process.error("Bad "+ temp_Family.getFamily_Number().getField_Title()+".", dbre);
            return;
        }
        
        web_Process.launch_Task("Enty200", "&loading_Comp_Id="+StringPlus.http_Encode(temp_Family.getComp_Id().getValue()) +
        "&loading_Family_Number="+StringPlus.http_Encode(temp_Family.getFamily_Number().toString())
        );
        
        return;
    }
    
    
    public static void familyP_Create_A_Family_Member_Action(Web_Process web_Process)
    throws ServletException, IOException, Database_Record_Constructor_Exception {
        Family temp_Family=new Family();
        
        try{
            temp_Family.validate_Key_Fields_Of_Request(web_Process.getMy_Request(), web_Process.getDatabase_Front());
        }
        catch(Database_Record_Exception dbre){
            web_Process.error("Bad "+ temp_Family.getFamily_Number().getField_Title()+".", dbre);
            return;
        }
        
        web_Process.launch_Task("Enty100", "&loading_Comp_Id="+StringPlus.http_Encode(temp_Family.getComp_Id().getValue()) +
        "&loading_Action="+StringPlus.http_Encode("Create_For_A_Family") +
        "&loading_Family_Number="+StringPlus.http_Encode(temp_Family.getFamily_Number().toString()));
        return;
    }
    
    
    public int validate_Record(Database_Front database_Front){
        
        this.cust_Id.setRequest_Error("");
        if(this.cust_Id.getValue().length()!=0){
            
            Cust cust;
            try {
                cust = (Cust) database_Front.getRecord(new Cust(this.comp_Id.toString(), this.cust_Id.toString()));
            }catch(Database_Front_Exception dfe){
                this.cust_Id.setRequest_Error("Database Error.");
                return 1;
            }catch(Database_Record_Constructor_Exception dfe){
                this.cust_Id.setRequest_Error("Database Record Error.");
                return 1;
            }catch(Database_Front_No_Results_Exception dfe){
                this.cust_Id.setRequest_Error("Must specify a Customer.");
                return 1;
            }
            
            try {
                this.cust_Id.setValue(cust.getCust_Id().getValue());
            }catch(DFException dfe){
                this.cust_Id.setRequest_Error(dfe.getMessage());
                return 1;
            }
            
        }
        return 0;
        
    }
    
}
