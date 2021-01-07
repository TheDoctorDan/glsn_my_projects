/*
 * Database_Front.java
 *
 * Created on May 31 14:55:00 CST 2005
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

import com.amlibtech.data_fields.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;






/**
 * Database_Front is a front end to an SQL database. It should isolate most of the SQL from a programmer.
 * @author dgleeson
 */
public class Database_Front  {
    
    Client_App_Constants    client_App_Constants;
    // sql
    Connection  connection=null;
    Driver driver;
    
    //boolean in_Transaction=false;
    
    /**
     * Create a new Database_Front.
     * @param passed_Client_App_Constants Contains info about the database to be opened by the database_Front.
     */
    public Database_Front(Client_App_Constants passed_Client_App_Constants){
        this.client_App_Constants = passed_Client_App_Constants;
        this.connection=null;
        this.driver=null;
    }
    
    /**
     * Get the Database_Front saved in for this session by set_Session_Instance().
     * @param httpSession HttpSession
     * @throws Database_Front_Exception If no Database_Front was saved or the connection to the database cannot be revalidated.
     * @return The database_Front saved for this session.
     */
    public synchronized static Database_Front get_Session_Instance(HttpSession httpSession)
    throws Database_Front_Exception {
        Database_Front database_Front = (Database_Front) httpSession.getAttribute("s_Database_Front");
        if(database_Front ==null){
            throw new Database_Front_Exception("Database_Front.get_Session_Instance: No session variable s_Database_Front. Try Database_Front.open() first");
        }
        try {
            database_Front.reValidate_Connection();
        }
        catch (Database_Front_Exception dbfe){
            throw dbfe;
        }
        
        return database_Front;
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
     * Getter for property connection.
     * @return Value of property connection.
     */
    public synchronized Connection get_Connection(){
        return connection;
    }
    
    /**
     * Set the Database_Front saved in for this session by set_Session_Instance().
     * @param httpSession HttpSession
     */
    
    public synchronized void set_Session_Instance(HttpSession httpSession){
        httpSession.setAttribute("s_Database_Front", this);
    }
    
    /**
     * Load the driver and establish a connection to the database, initialize the database options.
     * @throws Database_Front_Exception if it can't load the driver or if it can't open a connection, or if it can't initialize the options.
     */
    public synchronized void open() throws Database_Front_Exception {
        
        // sql
        
        try {
            driver = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
            try{
                connection = DriverManager.getConnection(client_App_Constants.getDatabase_URL(), client_App_Constants.getDatabase_User(), client_App_Constants.getDatabase_Password());
                connection.setAutoCommit(false);
            }
            catch(SQLException sqle){
                throw new Database_Front_Exception("Database_Front.open: Error getting Connection to database, SQLException: " + sqle.getMessage());
            }
        }
        catch (ClassNotFoundException cnfe){
            throw new Database_Front_Exception("Database_Front.open: Error loading driver for database, ClassNotFoundException: " + cnfe.getMessage());
        }
        catch (InstantiationException ie){
            throw new Database_Front_Exception("Database_Front.open: Error loading driver for database, InstantiationException: " + ie.getMessage());
        }
        catch (IllegalAccessException iae){
            throw new Database_Front_Exception("Database_Front.open: Error loading driver for database, IllegalAccessException: " + iae.getMessage());
        }
        
        //sql
        
        try {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate("SET GLOBAL TRANSACTION ISOLATION LEVEL READ COMMITTED;");
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception(this.getClass().getName() + ".open: Error setting TRANSACTION ISOLATION LEVEL READ COMMITTED, SQLException: " + sqle.getMessage());
        }
        
        try {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate("SET GLOBAL query_cache_type = OFF;");
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception(this.getClass().getName() + ".open: Error setting query_cache_type=OFF, SQLException: " + sqle.getMessage());
        }
        
        
        
        try {
            Statement statement2 = connection.createStatement();
            int rows2 = statement2.executeUpdate("RESET QUERY CACHE;");
            
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception(this.getClass().getName() + ".open: Error Resetting query cache, SQLException: " + sqle.getMessage());
        }
        
        return;
    }
    
    /**
     * Close the connection to the database.
     * @throws Database_Front_Exception If there was an error closing the connection.
     */
    public synchronized void close() throws Database_Front_Exception {
        
        // sql
        try {
            this.connection.close();
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.close: Error closing Connection to database, SQLException: " + sqle.getMessage());
        }
    }
    
    
    /**
     * Test the connection to the database.  If it has closed the re-open it and initialize the database options.
     * @throws Database_Front_Exception If is can't re-open the connection or if it can't initialize the database options.
     */
    public synchronized void reValidate_Connection() throws Database_Front_Exception {
        boolean need_to_reopen=false;
        
        //sql
        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT 1");
        }
        catch(SQLException se){
            need_to_reopen=true;
        }
        
        if(need_to_reopen){
            try {
                connection = DriverManager.getConnection(client_App_Constants.getDatabase_URL(), client_App_Constants.getDatabase_User(), client_App_Constants.getDatabase_Password());
                connection.setAutoCommit(false);
            }
            catch(SQLException sqle){
                throw new Database_Front_Exception("Database_Front.reValidate_Connection(): Error getting Connection to database, SQLException: " + sqle.getMessage());
            }
        }
        //sql
        try {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate("SET GLOBAL query_cache_type = OFF;");
            
            Statement statement2 = connection.createStatement();
            int rows2 = statement2.executeUpdate("RESET QUERY CACHE;");
            
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.reValidate_Connection(): Error setting query_cache_type, SQLException: " + sqle.getMessage());
        }
        
        
        
    }
    
    /**
     * Release the record lock.
     * @throws Database_Front_Exception If the cancel failed.
     */
    public synchronized void cancel_Record_Lock()
    throws Database_Front_Exception {
        
        try {
            connection.commit();
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.cancel_Record_Lock(): SQLException: " + sqle.getMessage());
        }
    }
    
    /**
     * Test if Database_Record_Base is in the database.
     * SELECT " + database_Record_Base.get_First_Field_Name() + " FROM " + database_Record_Base.get_Table_Name() + " WHERE " + database_Record_Base.get_Key_Where_Clause()
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception if a database error occurrs.
     * @return True if found, False if not found.
     */
    public synchronized boolean isMatch(Database_Record_Base database_Record_Base)
    throws Database_Front_Exception {
        return isMatch(database_Record_Base.get_Table_Name(), database_Record_Base);
    }
    
    
    
    
    
    /**
     * Test if Database_Record_Base is in the database.
     * SELECT " + database_Record_Base.get_First_Field_Name() + " FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause()
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param database_Record_Base database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception if a database error occurrs.
     * @return True if found, False if not found.
     */    
    public synchronized boolean isMatch(String table_Name, Database_Record_Base database_Record_Base)
    throws Database_Front_Exception {
        
        // sql
        String sql_Statement = "/* disable qcache */ SELECT " + database_Record_Base.get_First_Field_Name() + " FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause();
        
        PreparedStatement ps;
        ResultSet results;
        boolean row_Exists=false;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record_Base.preparedStatement_Encode(ps, Database_Record_Base.ORDER_KEYS_ONLY);
            results = ps.executeQuery();
            row_Exists = results.next();
            results.close();
            ps.close();
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.isMatch Failed! Table :" + table_Name + " :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front.isMatch Failed! Table :" + table_Name + " :" + dbre.getMessage());
            
        }
        
        return row_Exists;
    }
    
    
    
    
    /**
     * Add a Database_Record_Base to the database.
     * "INSERT INTO " + database_Record_Base.get_Table_Name() + " " + database_Record_Base.get_Into_Clause() + "  VALUES " + database_Record_Base.get_Values_Clause()
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If add fails.
     * @return Count of rows affected.
     */
    public synchronized int addRecord(Database_Record_Base database_Record_Base)
    throws Database_Front_Exception{
        return addRecord(database_Record_Base.get_Table_Name(), database_Record_Base);
    }
    
    /**
     * Add a Database_Record_Base to the database.
     * "INSERT INTO " + table_Name + " " + database_Record_Base.get_Into_Clause() + "  VALUES " + database_Record_Base.get_Values_Clause()
     * @return Count of rows affected.
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If add fails.
     */
    public synchronized int addRecord(String table_Name, Database_Record_Base database_Record_Base)
    throws Database_Front_Exception{
        
        String sql_Statement = "INSERT INTO " + table_Name + " " + database_Record_Base.get_Into_Clause() + "  VALUES " + database_Record_Base.get_Values_Clause() ;
        PreparedStatement ps=null;
        int row_Count =0;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record_Base.preparedStatement_Encode(ps, Database_Record_Base.ORDER_KEYS_FIRST);
            row_Count = ps.executeUpdate();
            //if(!in_Transaction)
                connection.commit();
            ps.close();
            if(row_Count==0){
                throw new Database_Front_Exception("Database_Front.addRecord Failed! Table :" + table_Name + "  : row Count=0.");
            }
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.addRecord Failed! Table :" + table_Name + "  :" + sqle.getMessage() + "    Statement:  " + ps.toString());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front.addRecord Failed! Table :" + table_Name + "  :" + dbre.getMessage() + "    Statement:  " + ps.toString());
        }
        
        return row_Count;
    }
    
    
    
    
    
    /**
     * Update Database_Record_Base to Database
     * UPDATE " + table_Name + " SET " +  database_Record.get_Set_Clause()  +
     *   "  WHERE " + database_Record.get_Key_Where_Clause() ;
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If effected row count =0, or SQL exception, or prepared_Statement_Encode() failed.
     * @return Effected row count.
     */    
    public  synchronized int updateRecord(Database_Record_Base database_Record_Base)
    throws Database_Front_Exception{
        return updateRecord(database_Record_Base.get_Table_Name(), database_Record_Base);
    }
    
    
    /**
     * Update Database_Record_Base to Database
     * UPDATE " + table_Name + " SET " +  database_Record.get_Set_Clause()  +
     *   "  WHERE " + database_Record.get_Key_Where_Clause() ;
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If effected row count =0, or SQL exception, or prepared_Statement_Encode() failed.
     * @return Effected row count.
     */    
    public  synchronized int updateRecord(String table_Name, Database_Record_Base database_Record_Base)
    throws Database_Front_Exception{
        // perhaps if get_Set_Clause is empty just return 1
	if(database_Record_Base.get_Set_Clause().equals(""))
		return 1;
        String sql_Statement = "UPDATE " + table_Name + " SET " +  database_Record_Base.get_Set_Clause()  +
        "  WHERE " + database_Record_Base.get_Key_Where_Clause() ;
        PreparedStatement ps;
        int row_Count=0;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record_Base.preparedStatement_Encode(ps, Database_Record_Base.ORDER_KEYS_LAST);
            row_Count = ps.executeUpdate();
            ps.close();
            //if(!in_Transaction)
                connection.commit();
            if(row_Count==0){
                throw new Database_Front_Exception("Database_Front.updateRecord Failed! Table :" + table_Name + "  : row Count=0.");
            }
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.updateRecord Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front.updateRecord Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        
        
        return row_Count;
    }
    
     
    
    
    /**
     * Update Database_Record_Base to Database but don't unlock
     * UPDATE " + table_Name + " SET " +  database_Record.get_Set_Clause()  +
     *   "  WHERE " + database_Record.get_Key_Where_Clause() ;
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If effected row count =0, or SQL exception, or prepared_Statement_Encode() failed.
     * @return Effected row count.
     */    
    public  synchronized int updateRecord_Dont_Unlock(Database_Record_Base database_Record_Base)
    throws Database_Front_Exception{
        return updateRecord_Dont_Unlock(database_Record_Base.get_Table_Name(), database_Record_Base);
    }
    
    
    /**
     * Update Database_Record_Base to Database but don't unlock
     * UPDATE " + table_Name + " SET " +  database_Record.get_Set_Clause()  +
     *   "  WHERE " + database_Record.get_Key_Where_Clause() ;
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If effected row count =0, or SQL exception, or prepared_Statement_Encode() failed.
     * @return Effected row count.
     */    
    public  synchronized int updateRecord_Dont_Unlock(String table_Name, Database_Record_Base database_Record_Base)
    throws Database_Front_Exception{
        // perhaps if get_Set_Clause is empty just return 1
        String sql_Statement = "UPDATE " + table_Name + " SET " +  database_Record_Base.get_Set_Clause()  +
        "  WHERE " + database_Record_Base.get_Key_Where_Clause() ;
        PreparedStatement ps;
        int row_Count=0;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record_Base.preparedStatement_Encode(ps, Database_Record_Base.ORDER_KEYS_LAST);
            row_Count = ps.executeUpdate();
            ps.close();
            
            if(row_Count==0){
                throw new Database_Front_Exception("Database_Front.updateRecord Failed! Table :" + table_Name + "  : row Count=0.");
            }
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.updateRecord Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front.updateRecord Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        
        
        return row_Count;
    }
    
    
    
    
    
    /**
     * Delete the database_Record_Base from the database.
     * DELETE FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() ;
     * @return Effected row count.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If effected row count =0, or SQL exception, or prepared_Statement_Encode() failed.
     */    
    public  synchronized int deleteRecord(Database_Record_Base database_Record_Base)
    throws Database_Front_Exception{
        return deleteRecord(database_Record_Base.get_Table_Name(), database_Record_Base);
    }
    
    /**
     * Delete the database_Record_Base from the database.
     * DELETE FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() ;
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If effected row count =0, or SQL exception, or prepared_Statement_Encode() failed.
     * @return Effected row count.
     */    
    public  synchronized int deleteRecord(String table_Name, Database_Record_Base database_Record_Base)
    throws Database_Front_Exception{
        
        String sql_Statement = "DELETE FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() ;
        PreparedStatement ps;
        int row_Count=0;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record_Base.preparedStatement_Encode(ps, Database_Record_Base.ORDER_KEYS_ONLY);
            row_Count = ps.executeUpdate();
            ps.close();
            //if(!in_Transaction)
                connection.commit();
            if(row_Count==0){
                throw new Database_Front_Exception("Database_Front.deleteRecord Failed! Table :" + table_Name + "  : row Count=0.");
            }
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.deleteRecord Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front.deleteRecord Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        
        return row_Count;
    }
    
    
    
    
    /**
     * Get SQL query results for rows that match the database_Record_Base's get_Key_Where_Clause()
     * SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() ;
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed.
     * @throws Database_Front_No_Results_Exception If no rows returned from SQL query.
     * @return results from SQL query.
     */    
    public  synchronized ResultSet getResultSet_Row(Database_Record_Base database_Record_Base)
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        return getResultSet_Row(database_Record_Base.get_Table_Name(), database_Record_Base);
    }
    
    /**
     * Get SQL query results for rows that match the database_Record_Base's get_Key_Where_Clause()
     * SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() ;
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed.
     * @throws Database_Front_No_Results_Exception If no rows returned from SQL query.
     * @return results from SQL query.
     */    
    public  synchronized ResultSet getResultSet_Row(String table_Name, Database_Record_Base database_Record_Base)
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        
        String sql_Statement = "/* disable qcache */ SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() ;
        
        PreparedStatement ps;
        ResultSet results=null;
        
        try{
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record_Base.preparedStatement_Encode(ps, Database_Record_Base.ORDER_KEYS_ONLY);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front.getResultSet_Row, Table :" + table_Name + " No Record Found.");
            }
            
            //results.close();
            
            //ps.close();
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.getResultSet_Row Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front.getResultSet_Row Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        return results;
    }
    
    
    
    
    /**
     * Get Database Record that match the passed_Database_Record_Base's get_Key_Where_Clause()
     * SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() ;.
     * @param passed_Database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed
     * @throws Database_Front_No_Results_Exception If no record found.
     * @return found Database Record in new memory allocated in resultSet_Row_Decode().
     */          
    public synchronized Database_Record_Base  getRecord(Database_Record_Base passed_Database_Record_Base)
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        return getRecord(passed_Database_Record_Base.get_Table_Name(), passed_Database_Record_Base);
    }
    
    /**
     * Get Database Record that match the passed_Database_Record_Base's get_Key_Where_Clause()
     * SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() ;.
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param passed_Database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed
     * @throws Database_Front_No_Results_Exception If no record found.
     * @return found Database Record in new memory allocated in resultSet_Row_Decode().
     */    
    public synchronized Database_Record_Base  getRecord(String table_Name, Database_Record_Base passed_Database_Record_Base)
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        ResultSet results =getResultSet_Row(table_Name, passed_Database_Record_Base);
        Database_Record_Base resulting_Record = null;
        try {
            resulting_Record = passed_Database_Record_Base.resultSet_Row_Decode(results);
            results.close();
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.getRecord Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Constructor_Exception dbrce){
             throw new Database_Front_Exception("Database_Front.getRecord Failed! Table :" + table_Name + "  :" + dbrce.getMessage());

        }
        return resulting_Record;
    }
    
    
    
    
    
    /**
     * Get SQL query results for rows that match the database_Record_Base's get_Key_Where_Clause() And Lock it.
     * SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() + " FOR UPDATE";.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed.
     * @throws Database_Front_No_Results_Exception If no rows returned from SQL query.
     * @throws Database_Front_Deadlock_Exception If locking row would cause a Dead Lock Condition.
     * @return results from SQL query.
     */
    public synchronized ResultSet getLocked_ResultSet_Row(Database_Record_Base database_Record_Base)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception {
        return getLocked_ResultSet_Row(database_Record_Base.get_Table_Name(), database_Record_Base);
    }
    
      
    /**
     * Get SQL query results for rows that match the database_Record_Base's get_Key_Where_Clause() And Lock it.
     * SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() + " FOR UPDATE";.
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed.
     * @throws Database_Front_No_Results_Exception If no rows returned from SQL query.
     * @throws Database_Front_Deadlock_Exception If locking record would cause a Dead Lock Condition.
     * @return results from SQL query.
     */
    public synchronized ResultSet getLocked_ResultSet_Row(String table_Name, Database_Record_Base database_Record_Base)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception {
        
        String sql_Statement = "/* disable qcache */ SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() + " FOR UPDATE";
        
        PreparedStatement ps;
        ResultSet results = null;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record_Base.preparedStatement_Encode(ps, Database_Record_Base.ORDER_KEYS_ONLY);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front.getLocked_ResultSet_Row, Table :" + table_Name + ", No Record Found.");
            }
            
            //results.close();
            
            //ps.close();
        }
        catch(SQLException sqle){
            if(sqle.getMessage().indexOf("Deadlock") >=0){
                throw new Database_Front_Deadlock_Exception("Database_Front.getResultSet_Row Failed! Table :" + table_Name + "  :" + sqle.getMessage());
            }else{
                throw new Database_Front_Exception("Database_Front.getResultSet_Row Failed! Table :" + table_Name + "  :" + sqle.getMessage());
            }
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front.getResultSet_Row Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        return results;
    }
    
    
    
    
    
    /**
     * Get Database Record that match the passed_Database_Record_Base's get_Key_Where_Clause() and Lock it.
     * SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() + " FOR UPDATE";.
     * @param passed_Database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed
     * @throws Database_Front_No_Results_Exception If no record found.
     * @throws Database_Front_Deadlock_Exception If locking record would cause a Dead Lock Condition.
     * @return found Database Record in new memory allocated in resultSet_Row_Decode().
     */    
    public synchronized Database_Record_Base  getLocked_Record(Database_Record_Base passed_Database_Record_Base)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception {
        return getLocked_Record(passed_Database_Record_Base.get_Table_Name(), passed_Database_Record_Base);
    }
    
    /**
     * Get Database Record that match the passed_Database_Record_Base's get_Key_Where_Clause() and Lock it.
     * SELECT * FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause() + " FOR UPDATE";.
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param passed_Database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed
     * @throws Database_Front_No_Results_Exception If no record found.
     * @throws Database_Front_Deadlock_Exception If locking record would cause a Dead Lock Condition.
     * @return found Database Record in new memory allocated in resultSet_Row_Decode().
     */    
    public synchronized Database_Record_Base  getLocked_Record(String table_Name, Database_Record_Base passed_Database_Record_Base)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception {
        ResultSet results =getLocked_ResultSet_Row(table_Name, passed_Database_Record_Base);
        Database_Record_Base resulting_Record = null;
        try {
            resulting_Record = passed_Database_Record_Base.resultSet_Row_Decode(results);
            results.close();
        }
        catch(SQLException sqle){
            // thrown by close()
            throw new Database_Front_Exception("Database_Front.getLocked_Record Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Constructor_Exception dbrce){
            throw new Database_Front_Exception("Database_Front.getLocked_Record Failed! Table :" + table_Name + "  :" + dbrce.getMessage());
        }
        return resulting_Record;
    }
    
    
    
    /**
     * Get SQL query results for rows that match the "where and sort clause", skipping "offset" rows and returning only "limit" rows.
     * SELECT * FROM " + table_Name + " " + where_And_Sort_Clause + " LIMIT " + offset + ", " + limit;     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @return Rows that match the query;
     * @param database_Record_Base Any database_Record_Base.
     * @param where_And_Sort_Clause SQL where clause and sort clause for query.
     * @param offset Number of Rows to skip before returning results.
     * @param limit Maximum number of rows to return;
     * @throws SQLException If SQL error occurrs.
     * @throws Database_Front_No_Results_Exception If NO rows where found.
     */     
    public  synchronized ResultSet getResultSet_Many_Rows(Database_Record_Base database_Record_Base, String where_And_Sort_Clause, int offset, int limit)
    throws SQLException, Database_Front_No_Results_Exception {
        return getResultSet_Many_Rows(database_Record_Base.get_Table_Name(), where_And_Sort_Clause, offset, limit);
    }
    
    /**
     * Get SQL query results for rows that match the "where and sort clause", skipping "offset" rows and returning only "limit" rows.
     * SELECT * FROM " + table_Name + " " + where_And_Sort_Clause + " LIMIT " + offset + ", " + limit;
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param where_And_Sort_Clause SQL where clause and sort clause for query.
     * @param offset Number of Rows to skip before returning results.
     * @param limit Maximum number of rows to return;
     * @throws SQLException If SQL error occurrs.
     * @throws Database_Front_No_Results_Exception  If NO rows where found.
     * @return Rows that match the query;
     */    
    public  synchronized ResultSet getResultSet_Many_Rows(String table_Name, String where_And_Sort_Clause, int offset, int limit)
    throws SQLException, Database_Front_No_Results_Exception {
        
        String sql_Statement = "/* disable qcache */ SELECT * FROM " + table_Name + " " + where_And_Sort_Clause + " LIMIT " + offset + ", " + limit;
        
        
        PreparedStatement ps;
        ResultSet results = null;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front.getResultSet_Many_Rows, Table :" + table_Name + ", No Records Found.");
            }
            results.beforeFirst();
            
            //results.close();
            
            //ps.close();
        }
        catch(SQLException sqle){
            throw new SQLException("Database_Front.getResultSet_Many_Rows Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        return results;
    }
    
    
    /**
     * Depricated
     */
    public  synchronized ResultSet getResultSet_Many_Rows(String table_Name, String where_And_Sort_Clause)
    throws SQLException, Database_Front_No_Results_Exception {
        
        String sql_Statement = "/* disable qcache */ SELECT * FROM " + table_Name + " " + where_And_Sort_Clause;
        
        
        
        
        PreparedStatement ps;
        ResultSet results = null;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front.getResultSet_Many_Rows, Table :" + table_Name + ", No Records Found.");
            }
            results.beforeFirst();
            
            //results.close();
            
            //ps.close();
        }
        catch(SQLException sqle){
            throw new SQLException("Database_Front.getResultSet_Many_Rows Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        return results;
    }
    
    
    
    /**
     * Get an array of Database Records same size as passed_Database_Record_Array starting 1 up from
     * last record in passed_Database_Record_Array with where and sort order controlled by "which_Index".
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param passed_Database_Record_Array is array of Database Records 
     * @param which_Index one of the indexes for this Database_Record_Base instance.
     * @throws Database_Front_Exception if getNext_Sort_Clause() or getNext_Where_Clause() fails.
     * @return a new array of Database Records or NULL.
     */    
    public synchronized Database_Record_Base[] getNext_N_Records(String table_Name, Database_Record_Base[] passed_Database_Record_Array, int which_Index)
    throws Database_Front_Exception {
        int skip_Levels;
        skip_Levels=0;
        int array_Size=passed_Database_Record_Array.length;
        
        Database_Record_Base[] resulting_Records;
        
        String  where_Clause="";
        String  sort_Clause="";
        
        
        
        resulting_Records = passed_Database_Record_Array[0].get_Array_of_Records(array_Size);
        
        // get highest used array_element or 0 if none;
        int highest_Used_Element=passed_Database_Record_Array.length-1;
        for(int i=0;i<=highest_Used_Element; i++){
            if(passed_Database_Record_Array[i]==null)
                highest_Used_Element=i-1;
        }
        if(highest_Used_Element<0)
            highest_Used_Element=0;
        
        Database_Record_Base highest_Database_Record_Base_So_Far = passed_Database_Record_Array[highest_Used_Element];
        for(int i=0;i<passed_Database_Record_Array.length;i++){
            passed_Database_Record_Array[i]=null;
        }
        int passed_Record_Array_Index= 0;
        int remainder;
        
        
        boolean finished=false;
        while(!finished && passed_Record_Array_Index<array_Size){
            try {
                sort_Clause = highest_Database_Record_Base_So_Far.getNext_Sort_Clause(which_Index);
                where_Clause = highest_Database_Record_Base_So_Far.getNext_Where_Clause(which_Index, skip_Levels);
                if(where_Clause==null)
                    return null;
                
            }
            catch (Database_Front_Exception dbfe){
                //invalid index
                throw new Database_Front_Exception("Database_Front.getNext_Record Failed! Table :" + table_Name + " :" + dbfe.getMessage());
            }
            
            remainder=array_Size - passed_Record_Array_Index;
            
            try {
                resulting_Records = getMany_Records(table_Name, passed_Database_Record_Array[highest_Used_Element], where_Clause +" "+ sort_Clause, 0, remainder);
                if(resulting_Records==null)
                    skip_Levels++;
                else
                    finished=true;
            }
            catch(Database_Front_Exception dbfe){
                //throw new Database_Front_Exception("Database_Front.getNext_Record Failed! Table :" + table_Name + " :" + dbfe.getMessage());
                skip_Levels++;
            }
            for(int i=0; i<resulting_Records.length && passed_Record_Array_Index<array_Size;i++){
                passed_Database_Record_Array[passed_Record_Array_Index++] = resulting_Records[i];
            }
        }
        return resulting_Records;
    }
    
    /** Depricated */
    public synchronized Database_Record_Base[] getMany_Records(Database_Record_Base passed_Database_Record, String where_And_Sort_Clause)
    throws Database_Front_Exception {
        return getMany_Records(passed_Database_Record.get_Table_Name(), passed_Database_Record, where_And_Sort_Clause);
        
    }
    
    
    /** Depricated */
    public synchronized Database_Record_Base[] getMany_Records(String table_Name, Database_Record_Base passed_Database_Record, String where_And_Sort_Clause)
    throws Database_Front_Exception {
        
        
        Database_Record_Base[] resulting_Records;
        ResultSet results;
        
        try {
            results =getResultSet_Many_Rows(table_Name, where_And_Sort_Clause);
        }
        
        catch(Database_Front_No_Results_Exception dbfnre){
            // no records found
            return null;
        }
        
        catch(SQLException sqle){
            throw new Database_Front_Exception(sqle.getMessage());
        }
        
        
        try{
            if (results.next() == true) {
                results.last();
                
                int array_Size = results.getRow();
                resulting_Records = passed_Database_Record.get_Array_of_Records(array_Size);
                
                results.beforeFirst();
                
                while (results.next() == true) {
                    int row=results.getRow()-1;
                    resulting_Records[row] = passed_Database_Record.resultSet_Row_Decode(results);
                }
                results.close();
                return resulting_Records;
                
            }else{
                results.close();
                throw new Database_Front_Exception("Database_Front.getMany_Records, Table :" + table_Name + ", No Records Found(2).");
            }
        }
        
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.getMany_Records Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Constructor_Exception dbrce){
            throw new Database_Front_Exception("Database_Front.getMany_Records Failed! Table :" + table_Name + "  :" + dbrce.getMessage());
        }
    }
    
    
    /**
     * Get an array of Database Records that match the "where and sort clause", skipping "offset" records and returning only "limit" records.
     * SELECT * FROM " + table_Name + " " + where_And_Sort_Clause + " LIMIT " + offset + ", " + limit;
     * @param passed_Database_Record_Base Any database_Record_Base.
     * @param where_And_Sort_Clause SQL where clause and sort clause for query.
     * @param offset Number of Records to skip before returning results.
     * @param limit Maximum number of records to return;
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed
     * @return Database Records that match the query or NULL if no records found.
     */    
     
    public synchronized Database_Record_Base[] getMany_Records(Database_Record_Base passed_Database_Record_Base, String where_And_Sort_Clause, int offset, int limit)
    throws Database_Front_Exception{
        return getMany_Records(passed_Database_Record_Base.get_Table_Name(), passed_Database_Record_Base, where_And_Sort_Clause, offset, limit);
    }
    
    /**
     * Get an array of Database Records that match the "where and sort clause", skipping "offset" records and returning only "limit" records.
     * SELECT * FROM " + table_Name + " " + where_And_Sort_Clause + " LIMIT " + offset + ", " + limit;
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param passed_Database_Record_Base Any database_Record_Base.
     * @param where_And_Sort_Clause SQL where clause and sort clause for query.
     * @param offset Number of Records to skip before returning results.
     * @param limit Maximum number of records to return;
     * @throws Database_Front_Exception If SQL exception, or prepared_Statement_Encode() failed
     * @return Database Records that match the query or NULL if no records found.
     */    
     
    public synchronized Database_Record_Base[] getMany_Records(String table_Name, Database_Record_Base passed_Database_Record_Base, String where_And_Sort_Clause, int offset, int limit)
    throws Database_Front_Exception{
        
        
        Database_Record_Base[] resulting_Records;
        ResultSet results;
        
        try {
            results =getResultSet_Many_Rows(table_Name, where_And_Sort_Clause, offset, limit);
        }
        
        catch(Database_Front_No_Results_Exception dbfe){
            // no records found
            return null;
        }
        
        catch(SQLException sqle){
            throw new Database_Front_Exception(sqle.getMessage());
        }
        
        
        try{
            if (results.next() == true) {
                results.last();
                
                int array_Size = results.getRow();
                resulting_Records = passed_Database_Record_Base.get_Array_of_Records(array_Size);
                
                results.beforeFirst();
                
                while (results.next() == true) {
                    int row=results.getRow()-1;
                    resulting_Records[row] = passed_Database_Record_Base.resultSet_Row_Decode(results);
                }
                results.close();
                return resulting_Records;
                
            }else{
                results.close();
                throw new Database_Front_Exception("Database_Front.getMany_Records, Table :" + table_Name + ", No Records Found(2).");
            }
        }
        
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.getMany_Records Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Constructor_Exception dbrce){
            throw new Database_Front_Exception("Database_Front.getMany_Records Failed! Table :" + table_Name + "  :" + dbrce.getMessage());
        }
    }
    
    /**
     * Get a count of Database Records that match the "where and sort clause".
     * @param passed_Database_Record_Base Any database_Record_Base.
     * @param where_And_Sort_Clause SQL where clause and sort clause for query.
     * @throws Database_Front_Exception if SQL exception
     * @return record count
     */    
    public synchronized int getCount_Of_Records(Database_Record_Base passed_Database_Record_Base, String where_And_Sort_Clause)
    throws Database_Front_Exception{
        return getCount_Of_Records(passed_Database_Record_Base.get_Table_Name(), passed_Database_Record_Base, where_And_Sort_Clause);
    }
    
    
    
    /**
     * Get a count of Database Records that match the "where and sort clause".
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param passed_Database_Record_Base Any database_Record_Base.
     * @param where_And_Sort_Clause SQL where clause and sort clause for query.
     * @throws Database_Front_Exception if SQL exception
     * @return record count
     */    
    public synchronized int getCount_Of_Records(String table_Name, Database_Record_Base passed_Database_Record_Base, String where_And_Sort_Clause)
    throws Database_Front_Exception{
        Statement statement;
        ResultSet rs;
        
        int row_Count = 0;
        
        try{
            statement = connection.createStatement();
            
            rs = statement.executeQuery("SELECT COUNT(*) as rowcount from " + table_Name + " " + where_And_Sort_Clause);
            rs.next();
            // Get the rowcount column value.
            row_Count = rs.getInt("rowcount") ;
            rs.close();
        }
        catch(java.sql.SQLException sqle){
            throw new Database_Front_Exception(this.getClass().getName() + ".getCount_Of_Records : Failed to count records for  " + passed_Database_Record_Base.getEntities_Name() + " in database. " + sqle.getMessage());
        }
        return row_Count;
        
    }
    
    
    
    /**
     *
     * @throws Database_Front_Exception
     * @return int of last Id inserted by auto increment
     */    
    public synchronized int get_Last_Insert_Id() throws Database_Front_Exception {
        Statement statement;
        ResultSet rs;
        int answer= -1;
        
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
            
            if (rs.next()) {
                answer = rs.getInt(1);
            } else {
                // throw an exception from here
                throw new Database_Front_Exception("Database_Front.get_Last_Insert_Id: Error getting LAST_INSERT_ID.  Result set is empty.");
            }
            
            rs.close();
            
            
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.get_Last_Insert_Id: Error getting LAST_INSERT_ID, SQLException: " + sqle.getMessage());
        }
        return answer;
    }
    
    
    
    /**
     *
     * @param database_Record_Base Any database_Record_Base.
     * @param which_Index
     * @throws Database_Front_Exception
     * @return Database_Record_Base of next record in which_Index order
     */    
    public synchronized Database_Record_Base  getNext_Record(Database_Record_Base database_Record_Base, int which_Index)
    throws Database_Front_Exception {
        return getNext_Record(database_Record_Base.get_Table_Name(), database_Record_Base, which_Index);
    }
    
    /**
     *
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param passed_Database_Record_Base Any database_Record_Base.
     * @param which_Index
     * @throws Database_Front_Exception
     * @return Database_Record_Base of next record in which_Index order
     */    
    public synchronized Database_Record_Base  getNext_Record(String table_Name, Database_Record_Base passed_Database_Record_Base, int which_Index)
    throws Database_Front_Exception {
        int skip_Levels;
        skip_Levels=0;
        int array_Size=1;
        
        Database_Record_Base[] resulting_Records;
        
        String  where_Clause="";
        String  sort_Clause="";
        
        
        resulting_Records = passed_Database_Record_Base.get_Array_of_Records(array_Size);
        
        boolean finished=false;
        while(!finished){
            try {
                sort_Clause = passed_Database_Record_Base.getNext_Sort_Clause(which_Index);
                where_Clause = passed_Database_Record_Base.getNext_Where_Clause(which_Index, skip_Levels);
                if(where_Clause==null)
                    return null;
            }
            catch (Database_Front_Exception dbfe){
                //invalid index
                throw new Database_Front_Exception("Database_Front.getNext_Record Failed! Table :" + table_Name + " :" + dbfe.getMessage());
            }
            
            try {
                resulting_Records = getMany_Records(table_Name, passed_Database_Record_Base, where_Clause + sort_Clause, 0, 1);
                if(resulting_Records==null)
                    skip_Levels++;
                else
                    finished=true;
            }
            catch(Database_Front_Exception dbfe){
                //throw new Database_Front_Exception("Database_Front.getNext_Record Failed! Table :" + table_Name + " :" + dbfe.getMessage());
                skip_Levels++;
            }
        }
        return resulting_Records[0];
    }
    
    
    /**
     *
     * @throws Database_Front_Exception
     * @throws Database_Front_No_Results_Exception
     * @return String[] of the names of all the tables in database.
     */    
    public synchronized String[] get_Table_List()
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        
        String sql_Statement = "show tables";
        
        PreparedStatement ps;
        ResultSet results = null;
        
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front.get_Table_List : No Tables Found.");
            }
            
            results.afterLast();
            results.previous();
            int rowCount = results.getRow();
            String answer_List[] = new String[rowCount];
            
            results.beforeFirst();
            while (results.next()){
                answer_List[results.getRow()-1] = results.getString(1);
            }
            
            results.close();
            
            ps.close();
            
            return answer_List;
            
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front.get_Table_List Failed, because :" + sqle.getMessage());
        }
        
    }
    
    
    /**
     *
     * @param tableName
     * @throws Database_Front_Exception
     * @throws DFException
     * @throws SQLException
     * @return DFBase[] of all columns/fields in table/file.
     */    
    public synchronized DFBase[] get_Table_Column_List(String tableName)
    throws Database_Front_Exception, DFException, SQLException {
        
        String sql_Statement = "select * from " + tableName;
        
        PreparedStatement ps= null;
        ResultSet results = null;
        
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            
            results = ps.executeQuery();
            
            //if (results.next() == false) {
            //throw new Database_Front_Exception("Database_Front.get_Table_Column_List : No Columns Found for Table :" + tableName + ":.");
            //}
            
            ResultSetMetaData metaData = results.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            DFBase[] answer_List = new DFBase[columnCount];
            TimeZone tz = TimeZone.getDefault();
            
            for(int icol=1;icol<=columnCount;icol++){
                
                String columnName = metaData.getColumnName(icol);
                String columnTypeName = metaData.getColumnTypeName(icol);
                String columnLabel = metaData.getColumnLabel(icol);
                
                
                if(columnTypeName.equalsIgnoreCase("date")){
                    answer_List[icol-1] = new DFDate(columnName,columnLabel);
                    
                }else if(columnTypeName.equalsIgnoreCase("DATETIME")){
                    answer_List[icol-1] = new DFTimestamp(columnName,columnLabel);

                }else if(columnTypeName.equalsIgnoreCase("timestamp")){
                    //answer_List[icol-1] = new DFTimestamp(columnName,columnLabel, tz);
                    answer_List[icol-1] = new DFTimestamp(columnName,columnLabel);
                    
                    //}else if(columnTypeName.equalsIgnoreCase("enum")){
                    //answer_List[icol-1] = new DFString_Enumerated(columnName,columnLabel, ?);
                    
                }else if(columnTypeName.equalsIgnoreCase("bool")){
                    answer_List[icol-1] = new DFBoolean(columnName,columnLabel);
                    
                }else if(columnTypeName.equalsIgnoreCase("varchar")){
                    if(metaData.getColumnDisplaySize(icol)<=255)
                        answer_List[icol-1] = new DFString(columnName,columnLabel, metaData.getColumnDisplaySize(icol));
                    else
                        answer_List[icol-1] = new DFString_Text_Box(columnName,columnLabel, 40);
                    
                }else if(columnTypeName.equalsIgnoreCase("text")){
                    answer_List[icol-1] = new DFString_Text_Box(columnName,columnLabel, 40);
                    
                }else if(columnTypeName.equalsIgnoreCase("bytes")){
                    answer_List[icol-1] = new DFString_Scrambled(columnName,columnLabel, metaData.getColumnDisplaySize(icol));
                    
                }else if(columnTypeName.equalsIgnoreCase("int")){
                    answer_List[icol-1] = new DFInteger(columnName,columnLabel, metaData.getColumnDisplaySize(icol));
                    
                }else if(columnTypeName.equalsIgnoreCase("INTEGER")){
                    answer_List[icol-1] = new DFInteger(columnName,columnLabel, metaData.getColumnDisplaySize(icol));

                }else if(columnTypeName.equalsIgnoreCase("INTEGER UNSIGNED")){
                    answer_List[icol-1] = new DFColor(columnName,columnLabel);
                    
                }else if(columnTypeName.equalsIgnoreCase("char")){
                    answer_List[icol-1] = new DFString(columnName,columnLabel, metaData.getColumnDisplaySize(icol));
                    
                }else if(columnTypeName.equalsIgnoreCase("tinyint")){
                    int length = metaData.getColumnDisplaySize(icol);
                    if(length==1)
                        answer_List[icol-1] = new DFBoolean(columnName,columnLabel);
                    else{
                        answer_List[icol-1] = new DFInteger(columnName,columnLabel,  metaData.getColumnDisplaySize(icol));
                    }
                    
                }else if(columnTypeName.equalsIgnoreCase("double")){
                    int right_Of_Decimal = metaData.getScale(icol);
                    int left_Of_Decimal = metaData.getPrecision(icol) - right_Of_Decimal;
                    answer_List[icol-1] = new DFBigDecimal(columnName,columnLabel, left_Of_Decimal, right_Of_Decimal);
                }else{
                    ps.close();
                    throw new Database_Front_Exception("Database_Front: get_Table_Column_List() for Table :" + tableName + ": Failed, because Column Name :" + columnName +": is of a type :" + columnTypeName + ": that is unknown to get_Table_Column_List().");
                }
            }
            
            ps.close();
            
            return answer_List;
            
        }
        catch(SQLException sqle){
            throw new SQLException("Database_Front.get_Table_Column_List Failed, because :" + sqle.getMessage());
        }
        catch(DFException dfe){
            throw new DFException("Database_Front.get_Table_Column_List Failed, because :" + dfe.getMessage());
        }
    }
    
    
//    public synchronized void transaction_Begin()
//    throws Database_Front_Transaction_Exception {
//        if(in_Transaction){
//            throw new Database_Front_Transaction_Exception("Can not Begin another Transaction until you finish the old one.");
//        }
//        try {
//            this.connection.setSavepoint();
//        }catch(SQLException sqle){
//            throw new Database_Front_Transaction_Exception("Database_Front.transaction_Begin Failed, because :" + sqle.getMessage());
//        }
//        in_Transaction=true;
//    }
//    
//    public synchronized void transaction_Successful_End()
//    throws Database_Front_Transaction_Exception {
//        if(!in_Transaction){
//            throw new Database_Front_Transaction_Exception("Can not End Transaction until you start one.");
//        }
//        try {
//            this.connection.commit();
//        }catch(SQLException sqle){
//            throw new Database_Front_Transaction_Exception("Database_Front.transaction_Successful_End Failed, because :" + sqle.getMessage());
//        }
//        in_Transaction=true;
//    }
//    
//    public synchronized void transaction_Failure_End()
//    throws Database_Front_Transaction_Exception {
//        if(!in_Transaction){
//            throw new Database_Front_Transaction_Exception("Can not End Transaction until you start one.");
//        }
//        try {
//            this.connection.rollback();
//        }catch(SQLException sqle){
//            throw new Database_Front_Transaction_Exception("Database_Front.transaction_Failure_End Failed, because :" + sqle.getMessage());
//        }
//        in_Transaction=true;
//    }
    
}
