/*
 * DFInteger_Auto_Increment.java
 *
 * Created on August 1, 2006, 12:27 PM
 */

package com.amlibtech.data_fields;


/**
 *
 * @author  dgleeson
 */
public class DFInteger_Auto_Increment extends DFInteger {

    public DFInteger_Auto_Increment(String field_Name, String field_Title, int max_Length) throws DFException {
        super(field_Name, field_Title, max_Length);
    }
    
    public DFInteger_Auto_Increment(String field_Name, String field_Title, int max_Length, Integer min_Value, Integer max_Value)
    throws DFException {
        super(field_Name, field_Title, max_Length,  min_Value,  max_Value);
    }
    
    public DFInteger_Auto_Increment(String field_Name, String field_Title, int max_Length, int min_Value, int max_Value) throws DFException {
        super(field_Name, field_Title, max_Length, min_Value, max_Value);
    }
    
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
    
    
    public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + this.getSql_Type() + "(" + this.getMax_Length() + ") NOT NULL auto_increment,\n");
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(Integer value) throws DFException {
        if(value == null)
            this.value=null;
        else
            super.setValue(value);
    }
        
     
    public java.sql.PreparedStatement get_PreparedStatement_Value(java.sql.PreparedStatement preparedStatement, int count) throws DFException {
         try {
             if(this.value==null){
                 preparedStatement.setString(count, "NULL");
             }else{
                preparedStatement.setInt(count, this.value.intValue());
             }
         }
         catch(java.sql.SQLException sqle){
             throw new DFException(sqle.getMessage());
        }
         return preparedStatement;
    }
   
}
