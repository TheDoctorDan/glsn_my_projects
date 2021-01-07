/*
 * Single_Connect_to_Data_Base.java
 *
 * Created on Mar 18 10:57:37 CST 2005
 */

/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 1999, 2000, 2001, 2002, 2003, 2004, 2005       |
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

import java.io.*;
import java.net.*;
import java.sql.*;






/**
 *
 * @author  dgleeson
 */
public class Obsolete_Single_Connect_To_Data_Base  {
    
    private Obsolete_Single_Connect_To_Data_Base(){}

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
     *
     * @param site_Constants
     * @throws IOException
     * @return
     */    
    public static Connection open(Site_Constants site_Constants) throws IOException {
        String message;
        String database_URL = site_Constants.getDatabase_URL();
        String database_User = site_Constants.getDatabase_User();
        String database_Password = site_Constants.getDatabase_Password();
        Driver d;
        Connection connection;
        
        try {
            // d = (Driver)Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            d = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
            try{
                connection = DriverManager.getConnection(database_URL, database_User, database_Password);
                connection.setAutoCommit(false);
            }
            catch(SQLException sqle){
                message ="Single_Connect_To_Data_Base.open: Error getting Connection to database, SQLException: " + sqle.getMessage();
                throw new IOException(message);
            }
        }
        catch (ClassNotFoundException cnfe){
            message ="Single_Connect_To_Data_Base.open: Error loading driver for database, ClassNotFoundException: " + cnfe.getMessage();
            throw new IOException(message);
        }
        catch (InstantiationException ie){
            message ="Single_Connect_To_Data_Base.open: Error loading driver for database, InstantiationException: " + ie.getMessage();
            throw new IOException(message);
        }
        catch (IllegalAccessException iae){
            message ="Single_Connect_To_Data_Base.open: Error loading driver for database, IllegalAccessException: " + iae.getMessage();
            throw new IOException(message);
        }
        
        return connection;
    }
    
    /**
     *
     * @param connection
     * @throws IOException
     */    
    public static void close(Connection connection) throws IOException {
        String message;
        try {
            connection.close();
        }
        catch(SQLException sqle){
            message ="Single_Connect_To_Data_Base.close: Error closing Connection to database, SQLException: " + sqle.getMessage();
            throw new IOException(message);
        }
    }

}
