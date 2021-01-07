/*
 * Experiment.java
 *
 * Created on April 15, 2006, 9:00 PM
 */

package data;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Experiment extends Database_Record_Base implements Database_Record_Interface {
    // Declaration Section
    DFString_Filled var1;
    DFString        var2;

    /** Creates a new instance of Experiment
     * @exception throws Database_Record_Exception
     */
    public	Experiment() throws Database_Record_Exception {
        super("Experiment", "Experiments");
        try{
            this.var1 =  new DFString_Filled("var1", "Var One", 10, DFString_Filled.FILL_TYPE_LEFT, '0');
            this.var2 =     new DFString("var2", "Var Two", 30);

        }
        catch(DFException dfe){
            throw new Database_Record_Exception("Class Experiment constructor failed because : " + dfe.getMessage());
        }
    }


    /**
     * Used by Database_Front to choose table in database.
     * @return table_Name "experiment".
     */
    public String get_Table_Name() {
        return "experiment";
    }

    /** Unknown Use ?
     * @exception throws Database_Record_Exception
     * @param Database_Front HttpSession
     */
    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession httpSession) throws Database_Record_Exception {
        return;
    }

    /**
     * Used by Database_Front to return many records.
     * @param int array_size
     * @return Experiment[] the size of array_Size.
     */
    public Object[] get_Array_of_Records(int array_Size) {
        Experiment[] experiment_Records;
        experiment_Records = new Experiment[array_Size];
        return experiment_Records;
    }



    /**
     * Creates a String of SQL commands to generate table.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {

        StringBuffer out = new StringBuffer();

        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + var1.getSQL_Declaration());
        out.append("\t" + var2.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + var1.getField_Name() + "`)\n");
        out.append(") TYPE=InnoDB;\n");

        return (out.toString());
    }


    /**
     * Used by Database_Front to sort records.
     * @exception throws Database_Front_Exception
     * @param int which_Index 
     * @return ORDER BY clause for which_Index.
     */
    public String getNext_Sort_Clause(int which_Index) throws Database_Front_Exception {
        switch(which_Index){
            case PRIMARY_INDEX:
                return "ORDER BY var1 ASC";
            default:
                throw new Database_Front_Exception(this.getClass().getName() + ".getNext_Sort_Clause(): unknown Index:"+which_Index);
        }
    }

    /**
     * Used by Database_Front to sort records.
     * skip_Levels will increment as each prior level fails.
     * @exception throws Database_Front_Exception
     * @param int which_Index, int skip_Levels
     * @return WHERE clause for which_Index.
     */
    public String getNext_Where_Clause(int which_Index, int skip_Levels) throws Database_Front_Exception {
        switch(which_Index){
            case PRIMARY_INDEX:
                switch(skip_Levels){
                    case 0:
                        return "WHERE var1 > '"+ SQLUtil.encode(this.var1.getValue()) + "'";
                    default:
                        return null;
                }
            default:
                throw new Database_Front_Exception(this.getClass().getName() + ".getNext_Sort_Clause(): unknown Index:"+which_Index);
        }
    }


    /**
     * Used by Database_Front to test if record exist.
     * @return the field_Name of first data field in record.
     */
    public String get_First_Field_Name() {
        return "var1";
    }

    /**
     * Used by Database_Front to INSERT records.
     * See counter part get_Values_Clause()
     * @return parenthesised comma separated list of ALL field_Names in record.
     */
    public String get_Into_Clause() {
        return "(var1, var2)";
    }


    /**
     * Used by Database_Front to INSERT records.
     * See counter part get_Into_Clause()
     * @return parenthesised comma separated list of ?. One for each field_Name in record.
     */
    public String get_Values_Clause() {
        return "( ?, ? )";
    }


    /**
     * Used by Database_Front to INSERT, FIND, DELETE, UPDATE a record.
     * @return comma separated list of field_Name = ? of all field_Names in Primary Key.
     */
    public String get_Key_Where_Clause() {
        return "var1 = ? ";
    }


    /**
     * Used by Database_Front to UPDATE a record.
     * @return comma separated list of field_Name = ? of all field_Names NOT in Primary Key.
     */
    public String get_Set_Clause() {
        return "var2 = ?";
    }

    /**
     * Used by Database_Front to encode fields into a sql prepared statement.
     * The first section is for fields in Primary key.
     * See get_Key_Where_Clause() .
     * The second section is for the rest of the data fields.
     * See get_Set_Clause().
     * See also get_Into_Clause() and get_Values_Clause().
     * @exception throws Database_Record_Exception
     * @param java.sql.PreparedStatement, int order
     * @return preparedStatement with values inserted where ? were.
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
                    preparedStatement = this.var1.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){

                    preparedStatement = this.var2.get_PreparedStatement_Value(preparedStatement, count++);
                }
            }
        }
        catch(DFException dfe){
            throw new Database_Record_Exception(this.getClass().getName() + ": preparedStatement_Encode(): failed because DFException : "
            + dfe.getMessage());
        }

        return preparedStatement;
    }

    /**
     * Used by Database_Front to decode sql resultSet into data fields of record.
     * @exception throws Database_Record_Exception
     * @param java.sql.ResultSet
     * @return Experiment record.
     */
    public synchronized Object resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Exception {
        Experiment result;
        try {
            result = new Experiment();

            result.var1.setValue_ResultSet(resultSet);
            result.var2.setValue_ResultSet(resultSet);

        }
        catch(DFException dfe){
            throw new Database_Record_Exception(this.getClass().getName() + ": resultSet_Row_Decode(): failed because DFException : " + dfe.getMessage());
        }

        return result;
    }


    /** Unknown Use ?
     * @except throws Database_Record_Exception
     * @param HttpServletRequest httpServletRequest, Database_Front database_Front
     */
    public synchronized void validate_Key_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front database_Front) throws Database_Record_Exception {
        int error_Count =0;

        error_Count += this.var1.validate_Request(httpServletRequest);

        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + "validate_Key_Fields_Of_Request(): Invalid Key Data Field(s).");

    }

    /** Unknown Use ?
     * @exception throws Database_Record_Exception
     * @param HttpServletRequest httpServletRequest, Database_Front database_Front
     */    
    public synchronized void validate_Fields_Of_Request(HttpServletRequest httpServletRequest, Database_Front database_Front) throws Database_Record_Exception {
     int error_Count =0;

        error_Count += this.var2.validate_Request(httpServletRequest);


        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + "validate_Fields_Of_Request(): Invalid Data Field(s).");


    }

    /** Unknown Use ?
     * @exception throws Database_Record_Exception
     */
    public DFSql_Column_Node[] get_Column_List() throws Database_Record_Exception {

        // programmer has entered ...
        LinkedList programmers_Columns = new LinkedList();
        try {
            programmers_Columns.add( var1.getDFSql_Column_Node());
            programmers_Columns.add( var2.getDFSql_Column_Node());


        }
        catch(DFException dfe){
            throw new Database_Record_Exception(this.getClass().getName()+": compare_Sql_Table() failed because :" + dfe.getMessage());
        }

        DFSql_Column_Node[] programmers_Column_Nodes = new DFSql_Column_Node[programmers_Columns.size()];

        for(int i=0; i<programmers_Column_Nodes.length;i++){
            programmers_Column_Nodes[i] = (DFSql_Column_Node) programmers_Columns.get(i);
        }

        return programmers_Column_Nodes;
    }


    /**
     * Getter for property var1.
     * @return Value of property var1.
     */
    public DFString_Filled getVar1() {
        return var1;
    }

    /**
     * Setter for property var1.
     * @param var1 New value of property var1.
     */
    public void setVar1(DFString_Filled var1) {
        this.var1 = var1;
    }

    /**
     * Getter for property var2.
     * @return Value of property var2.
     */
    public DFString getVar2() {
        return var2;
    }

    /**
     * Setter for property var2.
     * @param var2 New value of property var2.
     */
    public void setVar2(DFString var2) {
        this.var2 = var2;
    }




}
