/*
 * Site_Constants.java
 *
 * Created on October 28, 2004, 8:35 AM
 */

/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004       |
|       American Liberator Technologies                                        |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       American Liberator Technologies                                        |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
*/

package com.amlibtech.web.database;

/**
 *
 * @author  dgleeson
 */
public class Site_Constants {
    private static String database_URL;
    private static String database_User;
    private static String database_Password;
    

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

    /** Creates a new instance of Site_Constants */


	public	Site_Constants(
		String database_URL,
		String database_User,
		String database_Password
	) {
		this.database_URL = database_URL;
		this.database_User = database_User;
		this.database_Password = database_Password;
	}

	public void setDatabase_URL(String database_URL){
		this.database_URL = database_URL;
	}

	public String getDatabase_URL(){ return database_URL; }

	public void setDatabase_User(String database_User){
		this.database_User = database_User;
	}

	public String getDatabase_User(){ return database_User; }

	public void setDatabase_Password(String database_Password){
		this.database_Password = database_Password;
	}

	public String getDatabase_Password(){ return database_Password; }

        
        public String toString()
        {
            return("\nURL:" + database_URL + "\nUser:" + database_User + "\nPassword:" + database_Password + "\n");
        }
    
}
