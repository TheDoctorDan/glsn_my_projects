/*
 * Security_DataGroup.java
 *
 * Created on December 20, 2006, 3:50 PM
 */

package com.amlibtech.security_Management.data.list;

import com.amlibtech.data_fields.*;

/**
 *
 * @author  dgleeson
 */
public class Security_DataGroup extends DFChoice_Node {
    
    /** Creates a new instance of Security_DataGroup */
    public Security_DataGroup(String description) {
        super(description);
    }
    
    
    public static int SECURITY_DATAGROUP_ID_LEN = 30;

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
    
    public static DFString DFInit_Security_DataGroup_Id()
    throws DFException {
        return new DFString("security_DataGroup_Id", "Security Data Group Id", 1, SECURITY_DATAGROUP_ID_LEN);
    }
    
    
    public static final int SECURITY_DATAGROUP_INDEX_SOCIAL_SECURITY_NUMBER     = 0;
    public static final int SECURITY_DATAGROUP_INDEX_DRIVERS_LICENSE_NUMBER     = 1;
    public static final int SECURITY_DATAGROUP_INDEX_JOB_APPLICATION_DELETE     = 2;
    
     public static final Security_DataGroup[]  SECURITY_DATAGROUPS = {
        new  Security_DataGroup( "Social Security Number"),
        new  Security_DataGroup( "Divers License Number"),
        new  Security_DataGroup( "Job Application Delete")
     };
     
     
}
