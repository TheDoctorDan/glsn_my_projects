/*
 * Database_Record.java
 *
 * Created on May 31, 2005, 5:28 PM
 */

package com.amlibtech.database;

import java.sql.*;

/**
 *
 * @author  dgleeson
 */
public interface Database_Record_V1 {

    public static final String[] Copyright= {
    "|       Copyright (c) 1985 thru 2001, 2002, 2003, 2004, 2005, 2006, 2007       |",
    "|       American Liberator Technologies                                        |",
    "|       All Rights Reserved                                                    |",
    "|                                                                              |",
    "|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |",
    "|       American Liberator Technologies                                        |",
    "|       The copyright notice above does not evidence any                       |",
    "|       actual or intended publication of such source code.                    |"
    };
    
    public static final int ORDER_KEYS_FIRST = 0;
    public static final int ORDER_KEYS_LAST = 1;
    public static final int ORDER_KEYS_ONLY = 2;
    public static final int ORDER_KEYS_NEVER = 3;
    
    public static final int PRIMARY_INDEX=0;
    public static final int ALPHA_INDEX=1;
    public static final int DATE_INDEX=2;

    
    public String get_Table_Name();

    public String get_Into_Clause();
    public String get_Values_Clause();
    
    public PreparedStatement preparedStatement_Encode(PreparedStatement ps, int order) throws Database_Record_Exception;
    
    public String get_First_Field_Name();

    public String get_Key_Where_Clause();

    public String get_Set_Clause();
   
    public Object resultSet_Row_Decode(ResultSet rs) throws Database_Record_Exception;

    public Object[] get_Array_of_Records(int size);
    
    public String getNext_Sort_Clause(int which_index) throws Database_Front_Exception;
    
    public String getNext_Where_Clause(int which_index, int skip_Levels) throws Database_Front_Exception;


}
