/*
 * Jfiles_Work.java
 *
 * Created on May 20, 2006, 8:18 AM
 */

package db_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;

/**
 *
 * @author  dgleeson
 */
public class Jfiles_Work {
    public static final int ADD_TO_SQL_TYPE=1;
    public static final int DROP_FROM_SQL_TYPE=2;
    public static final int ALTER_TO_SQL_TYPE=3;
    public static final int RENAME_IN_SQL_TYPE=4;
    
    public static final int CLAUSE_CORRECTION_INTO_TYPE=100;
    public static final int MISC_SQL_COMMAND_TYPE=200;
    
    
    int type;
    String  table_Name;
    DFBase   programmer_Field;
    DFBase   sql_Field;
    
    String  misc_Sql_Command_Text;
    
    String  correction_Description;
    String  current_Text;
    String  corrected_Text;
    
    /** Creates a new instance of Jfiles_Work */
    public Jfiles_Work(int type, String table_Name, DFBase programmer_Field, DFBase sql_Field)
    throws Database_Record_Constructor_Exception {
        this.type = type;
        this.table_Name = table_Name;
        this.programmer_Field = programmer_Field;
        this.sql_Field = sql_Field;
        switch(type){
            case ADD_TO_SQL_TYPE:
                if(programmer_Field==null || sql_Field != null)
                    throw new Database_Record_Constructor_Exception(
                    "Jfiles_Work: constructor() failed because : ADD_TO_SQL_TYPE requires Programmer_Field and not Sql_Field DFBases.");
                
                return;
            case DROP_FROM_SQL_TYPE:
                if(programmer_Field!=null || sql_Field == null)
                    throw new Database_Record_Constructor_Exception(
                    "Jfiles_Work: constructor() failed because : DROP_FROM_SQL_TYPE requires Sql_Field and not Programmer_Field DFBases.");
                
                return;
            case ALTER_TO_SQL_TYPE:
                if(programmer_Field==null || sql_Field == null)
                    throw new Database_Record_Constructor_Exception("Jfiles_Work: constructor() failed because : ALTER_TO_SQL_TYPE requires both DFBases.");
                return;
            case RENAME_IN_SQL_TYPE:
                if(programmer_Field==null || sql_Field == null)
                    throw new Database_Record_Constructor_Exception("Jfiles_Work: constructor() failed because : RENAME_IN_SQL_TYPE requires both DFBases.");
                return;
            default:
                throw new Database_Record_Constructor_Exception("Jfiles_Work: constructor() SQL_Type failed because : unknown type :" + type + ": ");
                
        }
    }
    
    
    public Jfiles_Work(int type, String table_Name, String correction_Description, String current_Text, String corrected_Text)
    throws Database_Record_Constructor_Exception {
        this.type = type;
        this.table_Name = table_Name;
        this.correction_Description = correction_Description;
        this.current_Text = current_Text;
        this.corrected_Text = corrected_Text;
        switch(type){
            case CLAUSE_CORRECTION_INTO_TYPE:
                return;
            default:
                throw new Database_Record_Constructor_Exception("Jfiles_Work: constructor() Clause Correctionfailed because : unknown type :" + type + ": ");
                
        }
    }
    
    
    public Jfiles_Work(int type, String table_Name, String correction_Description, String misc_Sql_Command_Text)
    throws Database_Record_Constructor_Exception {
        this.type = type;
        this.table_Name = table_Name;
        this.correction_Description = correction_Description;
        this.misc_Sql_Command_Text = misc_Sql_Command_Text;
        switch(type){
            case MISC_SQL_COMMAND_TYPE:
                return;
            default:
                throw new Database_Record_Constructor_Exception("Jfiles_Work: constructor() Misc_Sql_Command failed because : unknown type :" + type + ": ");
                
        }
    }
    
    
    
    
    public String get_Sql_Command(boolean with_Cause) throws Database_Record_Exception {
        String answer;
        
        
        String programer_Field_Def=null;
        String sql_Field_Def=null;
        String dmys= "";
        
        // remove comma at the end of SQL_Declaration.
        if(programmer_Field!=null){
            dmys= programmer_Field.getSQL_Declaration().trim();
            if(dmys.lastIndexOf(',')==dmys.length()-1)
                programer_Field_Def = dmys.substring(0, dmys.length()-1);
            else
                programer_Field_Def = dmys;
        }
        
        if(sql_Field!=null){
            dmys= sql_Field.getSQL_Declaration().trim();
            if(dmys.lastIndexOf(',')==dmys.length()-1)
                sql_Field_Def = dmys.substring(0, dmys.length()-1);
            else
                sql_Field_Def = dmys;
        }
        
        
        switch(this.type){
            case ADD_TO_SQL_TYPE:
                return "ALTER TABLE `" + this.table_Name + "` ADD COLUMN " + programer_Field_Def;
                
            case DROP_FROM_SQL_TYPE:
                return "ALTER TABLE `" + table_Name + "` DROP COLUMN `" + sql_Field.getField_Name() + "`";
                
            case ALTER_TO_SQL_TYPE:
                if(with_Cause)
                    return "ALTER TABLE `" + table_Name + "` CHANGE COLUMN `" + programmer_Field.getField_Name() + "` " + programer_Field_Def + " CAUSE : " + sql_Field_Def;
                else
                    return "ALTER TABLE `" + table_Name + "` CHANGE COLUMN `" + programmer_Field.getField_Name() + "` " + programer_Field_Def;
                
            case RENAME_IN_SQL_TYPE:
                answer= "ALTER TABLE `" + table_Name + "` CHANGE COLUMN `" + sql_Field.getField_Name() + "`  " + programer_Field_Def;
                //                if(!sql_Field.same_Field_Layout(programmer_Field)){
                //                    answer=answer+"; " + "ALTER TABLE `" + table_Name + "` CHANGE COLUMN `" + programmer_Field.getField_Name() + "` " + programer_Field_Def;
                //
                //                }
                return answer;
                
            default:
                throw new Database_Record_Exception("Jfiles_Work: get_Sql_Command(): unknown type :" + type + ": ");
                
        }
    }
    
    public String get_Code_Correction(){
        String answer;
        
        answer = correction_Description + "\n" +
        "<br>Current Code :" + current_Text + "\n" +
        "<br>Correct Code :" + corrected_Text + "\n";
        
        return answer;
    }
    
    
    /**
     * Getter for property type.
     * @return Value of property type.
     */
    public int getType() {
        return type;
    }
    
    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(int type) {
        this.type = type;
    }
    
    /**
     * Getter for property misc_Sql_Command_Text.
     * @return Value of property misc_Sql_Command_Text.
     */
    public java.lang.String getMisc_Sql_Command_Text() {
        return misc_Sql_Command_Text;
    }
    
    /**
     * Getter for property table_Name.
     * @return Value of property table_Name.
     */
    public java.lang.String getTable_Name() {
        return table_Name;
    }
    
    /**
     * Getter for property programmer_Field.
     * @return Value of property programmer_Field.
     */
    public com.amlibtech.data_fields.DFBase getProgrammer_Field() {
        return programmer_Field;
    }
    
    
    
    /**
     * Getter for property sql_Field.
     * @return Value of property sql_Field.
     */
    public com.amlibtech.data_fields.DFBase getSql_Field() {
        return sql_Field;
    }
    
    
    
}
