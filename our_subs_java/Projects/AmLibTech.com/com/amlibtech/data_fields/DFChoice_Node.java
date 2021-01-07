/*
 * DFChoice_Node.java
 *
 * Created on October 15, 2006, 11:12 AM
 */

package com.amlibtech.data_fields;

/**
 *
 * @author  dgleeson
 */
public class DFChoice_Node {
    
    private String  description;
    
    /** Creates a new instance of DFChoice_Node */
    public DFChoice_Node(String description) {
        this.description = description;
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
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
     
    public static int indexOfDescription(DFChoice_Node[] choice_Nodes, String description){
        DFChoice_Node choice_Node;
        
        for(int i=0; i < choice_Nodes.length; i++){
            choice_Node = choice_Nodes[i];
            if(choice_Node.getDescription().equals(description))
                return i;
        }
        return -1;
    }
    
    
    
}
