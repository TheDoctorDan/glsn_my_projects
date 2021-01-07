/*
 * MathPlus.java
 *
 * Created on July 30, 2006, 4:09 PM
 */

package com.amlibtech.util;

/**
 *
 * @author  dgleeson
 */
public class MathPlus {

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
    
    public static double log10(double value){
        return Math.log(value)/Math.log(10.);
    }
    
    public static int length_Of_Integer(Integer value){
        return  (int)Math.floor(MathPlus.log10(Math.abs((double)value.intValue())));
    }
    public static int length_Of_Integer(int value){
        return  (int)Math.floor(MathPlus.log10(Math.abs((double)value)));
    }
}
