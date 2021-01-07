/*
 * Client_App_Constants.java
 *
 * Created on May 31, 2005, 2:57 PM
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

package com.amlibtech.database;

/**
 *
 * @author  dgleeson
 */
public class Client_App_Constants {
    private static String database_URL;
    private static String database_User;
    private static String database_Password;
    
    /** Creates a new instance of Site_Constants */


	public Client_App_Constants(String database_URL, String database_User, String database_Password) {
		this.database_URL = database_URL;
		this.database_User = database_User;
		this.database_Password = database_Password;
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

	public synchronized void setDatabase_URL(String database_URL){
		this.database_URL = database_URL;
	}

	public synchronized String getDatabase_URL(){ return database_URL; }

	public synchronized void setDatabase_User(String database_User){
		this.database_User = database_User;
	}

	public synchronized String getDatabase_User(){ return database_User; }

	public synchronized void setDatabase_Password(String database_Password){
		this.database_Password = database_Password;
	}

	public synchronized String getDatabase_Password(){ return database_Password; }

        
        public synchronized String toString()
        {
            return("\nURL:" + database_URL + "\nUser:" + database_User + "\nPassword:" + database_Password + "\n");
        }
    
}
