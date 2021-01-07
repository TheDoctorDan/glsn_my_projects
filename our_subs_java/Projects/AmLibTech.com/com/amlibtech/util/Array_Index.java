/*
 * Array_Index.java
 *
 * Created on May 16, 2006, 3:54 PM
 */

package com.amlibtech.util;

/**
 *
 * @author  dgleeson
 */
public class Array_Index {
    
    int value;
    int max_Value;
    
    /** Creates a new instance of Array_Index */
    public Array_Index(int init_Value, int max_Value) {
        this.value = init_Value;
        this.max_Value = max_Value;
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
    
    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(int value) {
        this.value = value;
    }
    
    /**
     * Getter for property max_Value.
     * @return Value of property max_Value.
     */
    public int getMax_Value() {
        return max_Value;
    }
    
    /**
     * Setter for property max_Value.
     * @param max_Value New value of property max_Value.
     */
    public void setMax_Value(int max_Value) {
        this.max_Value = max_Value;
    }
    
    public int incr_Value() throws Array_Index_Exception {
        value++;
        if(value > max_Value)
            throw new Array_Index_Exception("Index Value Exceeds Array's Max Index Value.");
        return value;
    }
    
    public int decr_Value() throws Array_Index_Exception {
        value--;
        if(value < 0)
            throw new Array_Index_Exception("Index Value is less than 0.");
        return value;
    }
}
