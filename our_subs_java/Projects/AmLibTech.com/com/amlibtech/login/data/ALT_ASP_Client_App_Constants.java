/*
 * ALT_ASP_Client_App_Constants.java
 *
 * Created on July 14, 2006, 1:18 PM
 */

package com.amlibtech.login.data;

import com.amlibtech.database.*;

/**
 *
 * @author  dgleeson
 */
public class ALT_ASP_Client_App_Constants {
    
    /** Creates a new instance of ALT_ASP_Client_App_Constants */
    private ALT_ASP_Client_App_Constants() {
    }
    
    public static Client_App_Constants getALT_ASP_Client_App_Constants(){
        Client_App_Constants alt_Asp_Client_App_Constants = new Client_App_Constants("jdbc:mysql://localhost/ALTASP", "ASP_admin", "master23er2indi");
        
        return alt_Asp_Client_App_Constants;
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

}
