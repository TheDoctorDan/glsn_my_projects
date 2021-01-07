/*
 * Multiple_Connect_to_Data_Base.java
 *
 * Created on September 9, 2004, 5:03 PM
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

import java.io.*;
import java.net.*;
import java.sql.*;






/**
 *
 * @author  dgleeson
 */
public class Multiple_Connect_To_Data_Base  {
    private static int database_Max_Connections=10;
    private static int last_Assigned_Connection_Index= -1;
    static Connection[] connections = new Connection[database_Max_Connections];
    Connection connection;

    //private static Connect_To_Data_Base connect_To_Data_Base_Instance=null;
    private static Driver d;
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

    /** Creates a new instance of Connect_to_Data_Base */
    public Multiple_Connect_To_Data_Base(Site_Constants site_Constants) throws IOException {
        String message;
        database_URL = site_Constants.getDatabase_URL();
        database_User = site_Constants.getDatabase_User();
        database_Password = site_Constants.getDatabase_Password();
        try {
            // d = (Driver)Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            d = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
            try{
                for(int connection_Index=0; connection_Index <database_Max_Connections; connection_Index++){
                    connection = DriverManager.getConnection(database_URL, database_User, database_Password);
                    connection.setAutoCommit(false);
                    connections[connection_Index] = connection;
                }
            }
            catch(SQLException sqle){
                message ="Multiple_Connect_To_Data_Base: Error getting Connection to database, SQLException: " + sqle.getMessage();
                throw new IOException(message);
            }
        }
        catch (ClassNotFoundException cnfe){
            message ="Multiple_Connect_To_Data_Base: Error loading driver for database, ClassNotFoundException: " + cnfe.getMessage();
            throw new IOException(message);
        }
        catch (InstantiationException ie){
            message ="Multiple_Connect_To_Data_Base: Error loading driver for database, InstantiationException: " + ie.getMessage();
            throw new IOException(message);
        }
        catch (IllegalAccessException iae){
            message ="Multiple_Connect_To_Data_Base: Error loading driver for database, IllegalAccessException: " + iae.getMessage();
            throw new IOException(message);
        }
    }

    public  synchronized Connection getConnection(Site_Constants site_constants)  throws IOException {
      

        last_Assigned_Connection_Index++;
        if(last_Assigned_Connection_Index >= database_Max_Connections)
            last_Assigned_Connection_Index=0;

        connections[last_Assigned_Connection_Index] = reValidate_Connection(connections[last_Assigned_Connection_Index]);

        return connections[last_Assigned_Connection_Index];
    }

    public  synchronized Connection reValidate_Connection(Connection connection) throws IOException {
        DatabaseMetaData databaseMetaData=null;
        boolean need_to_reopen=false;
        String message="";

        try {
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery("SELECT 1");
        }
        catch(SQLException se){
            need_to_reopen=true;
        }
        if(need_to_reopen){
            try {
                connection = DriverManager.getConnection(database_URL, database_User, database_Password);
                connection.setAutoCommit(false);
            }
            catch(SQLException sqle){
                message ="Multiple_Connect_To_Data_Base.reValidate_Connection(): Error getting Connection to database, SQLException: " + sqle.getMessage();
                throw new IOException(message);
            }
        }

     

        return connection;
    }

}
