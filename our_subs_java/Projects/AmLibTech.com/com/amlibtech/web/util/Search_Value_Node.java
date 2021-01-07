/*
 * Search_Value_Node.java
 *
 * Created on Tue Feb 28 12:24:08 CST 2006
 */

/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 2000, 2001, 2002, 2003, 2004, 2005, 2006       |
|       American Liberator Technologies                                        |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       American Liberator Technologies                                        |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
*/


package com.amlibtech.web.util;

import com.amlibtech.util.*;
import com.amlibtech.web.data.*;

/**
 *
 * @author  dgleeson
 */
public class Search_Value_Node {

    Field_Description_Node field_Description_Node;
    Object value;

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
    
    /** Creates a new instance of Search_Value_Node */
    public Search_Value_Node(Field_Description_Node field_Description_Node, Object value) {
        this.field_Description_Node = field_Description_Node;
        this.value = value;
    }
    
    
    public Object getValue(){
        return value;
    }
    
    public void setValue(Object value){
        this.value = value;
    }

   
    
    /**
     * Getter for property field_Description_Node.
     * @return Value of property field_Description_Node.
     */
    public Field_Description_Node getField_Description_Node() {
        return field_Description_Node;
    }    
    
    /**
     * Setter for property field_Description_Node.
     * @param field_Description_Node New value of property field_Description_Node.
     */
    public void setField_Description_Node(Field_Description_Node field_Description_Node) {
        this.field_Description_Node = field_Description_Node;
    }
    
    public String getField_Name(){
    	return field_Description_Node.getField_Name();
    }
    
    public String getWhere_SQL_Clause(){
        if(value.toString().equals(""))
            return ("");
        else
            return(field_Description_Node.getField_Name() + " = '" + SQLUtil.encode(value.toString()) + "'");
    }
}
