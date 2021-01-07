/*
 * Database_Front_V2.java
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
 * Database_Front_V2 is a front end to an SQL database. It should isolate most of the SQL from a programmer.
 * @author dgleeson
 */
public class Database_Front_V2  {
    
    Client_App_Constants    client_App_Constants;
    // sql
    Connection  connection=null;
    Driver driver;
    
    /**
     * Create a new Database_Front_V2.
     * @param passed_Client_App_Constants Contains info about the database to be opened by the database_Front_V2.
     */
    public Database_Front_V2(Client_App_Constants passed_Client_App_Constants){
        this.client_App_Constants = passed_Client_App_Constants;
        this.connection=null;
        this.driver=null;
    }
    
    /**
     * Get the Database_Front_V2 saved in for this session by set_Session_Instance().
     * @param httpSession HttpSession
     * @throws Database_Front_Exception If no Database_Front_V2 was saved or the connection to the database cannot be revalidated.
     * @return The database_Front_V2 saved for this session.
     */
    public synchronized static Database_Front_V2 get_Session_Instance(HttpSession httpSession) throws Database_Front_Exception {
        Database_Front_V2 database_Front = (Database_Front_V2) httpSession.getAttribute("s_Database_Front_V2");
        if(database_Front ==null){
            throw new Database_Front_Exception("Database_Front_V2.get_Session_Instance: No session variable s_Database_Front_V2. Try Database_Front_V2.open() first");
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
     * Set the Database_Front_V2 saved in for this session by set_Session_Instance().
     * @param httpSession HttpSession
     */
    
    public synchronized void set_Session_Instance(HttpSession httpSession){
        httpSession.setAttribute("s_Database_Front_V2", this);
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
                throw new Database_Front_Exception("Database_Front_V2.open: Error getting Connection to database, SQLException: " + sqle.getMessage());
            }
        }
        catch (ClassNotFoundException cnfe){
            throw new Database_Front_Exception("Database_Front_V2.open: Error loading driver for database, ClassNotFoundException: " + cnfe.getMessage());
        }
        catch (InstantiationException ie){
            throw new Database_Front_Exception("Database_Front_V2.open: Error loading driver for database, InstantiationException: " + ie.getMessage());
        }
        catch (IllegalAccessException iae){
            throw new Database_Front_Exception("Database_Front_V2.open: Error loading driver for database, IllegalAccessException: " + iae.getMessage());
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
            throw new Database_Front_Exception("Database_Front_V2.close: Error closing Connection to database, SQLException: " + sqle.getMessage());
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
                throw new Database_Front_Exception("Database_Front_V2.reValidate_Connection(): Error getting Connection to database, SQLException: " + sqle.getMessage());
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
            throw new Database_Front_Exception("Database_Front_V2.reValidate_Connection(): Error setting query_cache_type, SQLException: " + sqle.getMessage());
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
            throw new Database_Front_Exception("Database_Front_V2.cancel_Record_Lock(): SQLException: " + sqle.getMessage());
        }
    }
    
    /**
     * Test if Database_Record_Base_V2 is in the database.
     * SELECT " + database_Record_Base.get_First_Field_Name() + " FROM " + database_Record_Base.get_Table_Name() + " WHERE " + database_Record_Base.get_Key_Where_Clause()
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception if a database error occurrs.
     * @return True if found, False if not found.
     */
    public synchronized boolean isMatch(Database_Record_Base_V2 database_Record_Base)
    throws Database_Front_Exception {
        return isMatch(database_Record_Base.get_Table_Name(), database_Record_Base);
    }
    
    /**
     * Test if Database_Record_Base_V2_V2 is in the database.
     * SELECT " + database_Record_Base.get_First_Field_Name() + " FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause()
     * @return True if found, False if not found.
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception if a database error occurrs.
     */
    public synchronized boolean isMatch(String table_Name, Database_Record_Base_V2 database_Record_Base)
    throws Database_Front_Exception {
        
        // sql
        String sql_Statement = "/* disable qcache */ SELECT " + database_Record_Base.get_First_Field_Name() + " FROM " + table_Name + " WHERE " + database_Record_Base.get_Key_Where_Clause();
        
        PreparedStatement ps;
        ResultSet results;
        boolean row_Exists=false;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record_Base.preparedStatement_Encode(ps, Database_Record_Base_V2.ORDER_KEYS_ONLY);
            results = ps.executeQuery();
            row_Exists = results.next();
            results.close();
            ps.close();
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front_V2.isMatch Failed! Table :" + table_Name + " :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.isMatch Failed! Table :" + table_Name + " :" + dbre.getMessage());
            
        }
        
        return row_Exists;
    }
    
    
    
    
    /**
     * Add a Database_Record_Base_V2 to the database.
     * "INSERT INTO " + database_Record_Base.get_Table_Name() + " " + database_Record_Base.get_Into_Clause() + "  VALUES " + database_Record_Base.get_Values_Clause()
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If add fails.
     * @return Count of rows affected.
     */
    public synchronized int addRecord(Database_Record_Base_V2 database_Record_Base)
    throws Database_Front_Exception{
        return addRecord(database_Record_Base.get_Table_Name(), database_Record_Base);
    }
    
    /**
     * Add a Database_Record_Base_V2 to the database.
     * "INSERT INTO " + table_Name + " " + database_Record_Base.get_Into_Clause() + "  VALUES " + database_Record_Base.get_Values_Clause()
     * @return Count of rows affected.
     * @param table_Name Which table is to be accessed.  Useful for copying one table to another of identical layout.
     * @param database_Record_Base Any database_Record_Base.
     * @throws Database_Front_Exception If add fails.
     */
    public synchronized int addRecord(String table_Name, Database_Record_Base_V2 database_Record_Base)
    throws Database_Front_Exception{
        
        String sql_Statement = "INSERT INTO " + table_Name + " " + database_Record_Base.get_Into_Clause() + "  VALUES " + database_Record_Base.get_Values_Clause() ;
        PreparedStatement ps;
        int row_Count =0;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record_Base.preparedStatement_Encode(ps, Database_Record_Base_V2.ORDER_KEYS_FIRST);
            row_Count = ps.executeUpdate();
            connection.commit();
            ps.close();
            if(row_Count==0){
                throw new Database_Front_Exception("Database_Front_V2.addRecord Failed! Table :" + table_Name + "  : row Count=0.");
            }
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front_V2.addRecord Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.addRecord Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        
        return row_Count;
    }
    
    
    
    
    
    public  synchronized int updateRecord(Database_Record_Base_V2 database_Record)
    throws Database_Front_Exception{
        return updateRecord(database_Record.get_Table_Name(), database_Record);
    }
    
    
    public  synchronized int updateRecord(String table_Name, Database_Record_Base_V2 database_Record)
    throws Database_Front_Exception{
        // perhaps if get_Set_Clause is empty just return 1
        String sql_Statement = "UPDATE " + table_Name + " SET " +  database_Record.get_Set_Clause()  +
        "  WHERE " + database_Record.get_Key_Where_Clause() ;
        PreparedStatement ps;
        int row_Count=0;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record.preparedStatement_Encode(ps, Database_Record_Base_V2.ORDER_KEYS_LAST);
            row_Count = ps.executeUpdate();
            ps.close();
            connection.commit();
            if(row_Count==0){
                throw new Database_Front_Exception("Database_Front_V2.updateRecord Failed! Table :" + table_Name + "  : row Count=0.");
            }
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front_V2.updateRecord Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.updateRecord Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        
        
        return row_Count;
    }
    
    
    
    
    
    public  synchronized int deleteRecord(Database_Record_Base_V2 database_Record)
    throws Database_Front_Exception{
        return deleteRecord(database_Record.get_Table_Name(), database_Record);
    }
    
    public  synchronized int deleteRecord(String table_Name, Database_Record_Base_V2 database_Record)
    throws Database_Front_Exception{
        
        String sql_Statement = "DELETE FROM " + table_Name + " WHERE " + database_Record.get_Key_Where_Clause() ;
        PreparedStatement ps;
        int row_Count=0;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record.preparedStatement_Encode(ps, Database_Record_Base_V2.ORDER_KEYS_ONLY);
            row_Count = ps.executeUpdate();
            ps.close();
            connection.commit();
            if(row_Count==0){
                throw new Database_Front_Exception("Database_Front_V2.deleteRecord Failed! Table :" + table_Name + "  : row Count=0.");
            }
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front_V2.deleteRecord Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.deleteRecord Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        
        return row_Count;
    }
    
    
    
    
    public  synchronized ResultSet getResultSet_Row(Database_Record_Base_V2 database_Record)
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        return getResultSet_Row(database_Record.get_Table_Name(), database_Record);
    }
    
    public  synchronized ResultSet getResultSet_Row(String table_Name, Database_Record_Base_V2 database_Record)
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        
        String sql_Statement = "/* disable qcache */ SELECT * FROM " + table_Name + " WHERE " + database_Record.get_Key_Where_Clause() ;
        
        PreparedStatement ps;
        ResultSet results=null;
        
        try{
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record.preparedStatement_Encode(ps, Database_Record_Base_V2.ORDER_KEYS_ONLY);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front_V2.getResultSet_Row, Table :" + table_Name + " No Record Found.");
            }
            
            //results.close();
            
            //ps.close();
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front_V2.getResultSet_Row Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.getResultSet_Row Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        return results;
    }
    
    
    
    
    public synchronized Database_Record_Base_V2  getRecord(Database_Record_Base_V2 database_Record)
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        return getRecord(database_Record.get_Table_Name(), database_Record);
    }
    
    public synchronized Database_Record_Base_V2  getRecord(String table_Name, Database_Record_Base_V2 passed_Database_Record)
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        ResultSet results =getResultSet_Row(table_Name, passed_Database_Record);
        Database_Record_Base_V2 resulting_Record = null;
        try {
            resulting_Record = passed_Database_Record.resultSet_Row_Decode(results);
            results.close();
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front_V2.getRecord Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.getRecord Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        return resulting_Record;
    }
    
    
    
    
    
    public synchronized ResultSet getLocked_ResultSet_Row(Database_Record_Base_V2 database_Record)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception {
        return getLocked_ResultSet_Row(database_Record.get_Table_Name(), database_Record);
    }
    
    public synchronized ResultSet getLocked_ResultSet_Row(String table_Name, Database_Record_Base_V2 database_Record)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception {
        
        String sql_Statement = "/* disable qcache */ SELECT * FROM " + table_Name + " WHERE " + database_Record.get_Key_Where_Clause() + " FOR UPDATE";
        
        PreparedStatement ps;
        ResultSet results = null;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            ps = database_Record.preparedStatement_Encode(ps, Database_Record_Base_V2.ORDER_KEYS_ONLY);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front_V2.getLocked_ResultSet_Row, Table :" + table_Name + ", No Record Found.");
            }
            
            //results.close();
            
            //ps.close();
        }
        catch(SQLException sqle){
            if(sqle.getMessage().indexOf("Deadlock") >=0){
                throw new Database_Front_Deadlock_Exception("Database_Front_V2.getResultSet_Row Failed! Table :" + table_Name + "  :" + sqle.getMessage());
            }else{
                throw new Database_Front_Exception("Database_Front_V2.getResultSet_Row Failed! Table :" + table_Name + "  :" + sqle.getMessage());
            }
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.getResultSet_Row Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        return results;
    }
    
    
    
    
    
    public synchronized Database_Record_Base_V2  getLocked_Record(Database_Record_Base_V2 passed_Database_Record)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception {
        return getLocked_Record(passed_Database_Record.get_Table_Name(), passed_Database_Record);
    }
    
    public synchronized Database_Record_Base_V2  getLocked_Record(String table_Name, Database_Record_Base_V2 passed_Database_Record)
    throws Database_Front_Exception, Database_Front_No_Results_Exception, Database_Front_Deadlock_Exception {
        ResultSet results =getLocked_ResultSet_Row(table_Name, passed_Database_Record);
        Database_Record_Base_V2 resulting_Record = null;
        try {
            resulting_Record = passed_Database_Record.resultSet_Row_Decode(results);
            results.close();
        }
        catch(SQLException sqle){
            // thrown by close()
            throw new Database_Front_Exception("Database_Front_V2.getLocked_Record Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.getLocked_Record Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
        return resulting_Record;
    }
    
    
    
    public  synchronized ResultSet getResultSet_Many_Rows(Database_Record_Base_V2 database_Record, String where_And_Sort_Clause, int offset, int limit)
    throws Database_Front_Exception, SQLException, Database_Front_No_Results_Exception {
        return getResultSet_Many_Rows(database_Record.get_Table_Name(), where_And_Sort_Clause, offset, limit);
    }
    
    public  synchronized ResultSet getResultSet_Many_Rows(String table_Name, String where_And_Sort_Clause, int offset, int limit)
    throws SQLException, Database_Front_No_Results_Exception {
        
        String sql_Statement = "/* disable qcache */ SELECT * FROM " + table_Name + " " + where_And_Sort_Clause + " LIMIT " + offset + ", " + limit;
        
        
        PreparedStatement ps;
        ResultSet results = null;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front_V2.getResultSet_Many_Rows, Table :" + table_Name + ", No Records Found.");
            }
            results.beforeFirst();
            
            //results.close();
            
            //ps.close();
        }
        catch(SQLException sqle){
            throw new SQLException("Database_Front_V2.getResultSet_Many_Rows Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        return results;
    }
    
    
    /** Depricated */
    public  synchronized ResultSet getResultSet_Many_Rows(String table_Name, String where_And_Sort_Clause)
    throws SQLException, Database_Front_No_Results_Exception {
        
        String sql_Statement = "/* disable qcache */ SELECT * FROM " + table_Name + " " + where_And_Sort_Clause;
        
        
        
        
        PreparedStatement ps;
        ResultSet results = null;
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front_V2.getResultSet_Many_Rows, Table :" + table_Name + ", No Records Found.");
            }
            results.beforeFirst();
            
            //results.close();
            
            //ps.close();
        }
        catch(SQLException sqle){
            throw new SQLException("Database_Front_V2.getResultSet_Many_Rows Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        return results;
    }
    
    public synchronized Database_Record_Base_V2[] getNext_N_Records(String table_Name, Database_Record_Base_V2[] passed_Database_Record_Array, int which_Index)
    throws Database_Front_Exception {
        int skip_Levels;
        skip_Levels=0;
        int array_Size=passed_Database_Record_Array.length;
        
        Database_Record_Base_V2[] resulting_Records;
        
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
        
        Database_Record_Base_V2 highest_Database_Record_Base_V2_So_Far = passed_Database_Record_Array[highest_Used_Element];
        for(int i=0;i<passed_Database_Record_Array.length;i++){
            passed_Database_Record_Array[i]=null;
        }
        int passed_Record_Array_Index= 0;
        int remainder;
        
        
        boolean finished=false;
        while(!finished && passed_Record_Array_Index<array_Size){
            try {
                sort_Clause = highest_Database_Record_Base_V2_So_Far.getNext_Sort_Clause(which_Index);
                where_Clause = highest_Database_Record_Base_V2_So_Far.getNext_Where_Clause(which_Index, skip_Levels);
                if(where_Clause==null)
                    return null;
                
            }
            catch (Database_Front_Exception dbfe){
                //invalid index
                throw new Database_Front_Exception("Database_Front_V2.getNext_Record Failed! Table :" + table_Name + " :" + dbfe.getMessage());
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
                //throw new Database_Front_Exception("Database_Front_V2.getNext_Record Failed! Table :" + table_Name + " :" + dbfe.getMessage());
                skip_Levels++;
            }
            for(int i=0; i<resulting_Records.length && passed_Record_Array_Index<array_Size;i++){
                passed_Database_Record_Array[passed_Record_Array_Index++] = resulting_Records[i];
            }
        }
        return resulting_Records;
    }
    
    /** Depricated */
    public synchronized Database_Record_Base_V2[] getMany_Records(Database_Record_Base_V2 passed_Database_Record, String where_And_Sort_Clause)
    throws Database_Front_Exception {
        return getMany_Records(passed_Database_Record.get_Table_Name(), passed_Database_Record, where_And_Sort_Clause);
        
    }
    
    
    /** Depricated */
    public synchronized Database_Record_Base_V2[] getMany_Records(String table_Name, Database_Record_Base_V2 passed_Database_Record, String where_And_Sort_Clause)
    throws Database_Front_Exception {
        
        
        Database_Record_Base_V2[] resulting_Records;
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
                throw new Database_Front_Exception("Database_Front_V2.getMany_Records, Table :" + table_Name + ", No Records Found(2).");
            }
        }
        
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front_V2.getMany_Records Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.getMany_Records Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
    }
    
    public synchronized Database_Record_Base_V2[] getMany_Records(Database_Record_Base_V2 passed_Database_Record, String where_And_Sort_Clause, int offset, int limit)
    throws Database_Front_Exception{
        return getMany_Records(passed_Database_Record.get_Table_Name(), passed_Database_Record, where_And_Sort_Clause, offset, limit);
    }
    
    public synchronized Database_Record_Base_V2[] getMany_Records(String table_Name, Database_Record_Base_V2 passed_Database_Record, String where_And_Sort_Clause, int offset, int limit)
    throws Database_Front_Exception{
        
        
        Database_Record_Base_V2[] resulting_Records;
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
                throw new Database_Front_Exception("Database_Front_V2.getMany_Records, Table :" + table_Name + ", No Records Found(2).");
            }
        }
        
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front_V2.getMany_Records Failed! Table :" + table_Name + "  :" + sqle.getMessage());
        }
        catch(Database_Record_Exception dbre){
            throw new Database_Front_Exception("Database_Front_V2.getMany_Records Failed! Table :" + table_Name + "  :" + dbre.getMessage());
        }
    }
    
    public synchronized int getCount_Of_Records(Database_Record_Base_V2 passed_Database_Record, String where_And_Sort_Clause)
    throws Database_Front_Exception{
        return getCount_Of_Records(passed_Database_Record.get_Table_Name(), passed_Database_Record, where_And_Sort_Clause);
    }
    
    public synchronized int getCount_Of_Records(String table_Name, Database_Record_Base_V2 passed_Database_Record, String where_And_Sort_Clause)
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
            throw new Database_Front_Exception(this.getClass().getName() + ".getCount_Of_Records : Failed to count records for  " + passed_Database_Record.getEntities_Name() + " in database. " + sqle.getMessage());
        }
        return row_Count;
        
        
    }
    
    
    
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
                throw new Database_Front_Exception("Database_Front_V2.get_Last_Insert_Id: Error getting LAST_INSERT_ID.  Result set is empty.");
            }
            
            rs.close();
            
            
        }
        catch(SQLException sqle){
            throw new Database_Front_Exception("Database_Front_V2.get_Last_Insert_Id: Error getting LAST_INSERT_ID, SQLException: " + sqle.getMessage());
        }
        return answer;
    }
    
    
    
    public synchronized Database_Record_Base_V2  getNext_Record(Database_Record_Base_V2 database_Record, int which_Index)
    throws Database_Front_Exception {
        return getNext_Record(database_Record.get_Table_Name(), database_Record, which_Index);
    }
    
    public synchronized Database_Record_Base_V2  getNext_Record(String table_Name, Database_Record_Base_V2 passed_Database_Record, int which_Index)
    throws Database_Front_Exception {
        int skip_Levels;
        skip_Levels=0;
        int array_Size=1;
        
        Database_Record_Base_V2[] resulting_Records;
        
        String  where_Clause="";
        String  sort_Clause="";
        
        
        resulting_Records = passed_Database_Record.get_Array_of_Records(array_Size);
        
        boolean finished=false;
        while(!finished){
            try {
                sort_Clause = passed_Database_Record.getNext_Sort_Clause(which_Index);
                where_Clause = passed_Database_Record.getNext_Where_Clause(which_Index, skip_Levels);
                if(where_Clause==null)
                    return null;
            }
            catch (Database_Front_Exception dbfe){
                //invalid index
                throw new Database_Front_Exception("Database_Front_V2.getNext_Record Failed! Table :" + table_Name + " :" + dbfe.getMessage());
            }
            
            try {
                resulting_Records = getMany_Records(table_Name, passed_Database_Record, where_Clause + sort_Clause, 0, 1);
                if(resulting_Records==null)
                    skip_Levels++;
                else
                    finished=true;
            }
            catch(Database_Front_Exception dbfe){
                //throw new Database_Front_Exception("Database_Front_V2.getNext_Record Failed! Table :" + table_Name + " :" + dbfe.getMessage());
                skip_Levels++;
            }
        }
        return resulting_Records[0];
    }
    
    
    public synchronized String[] get_Table_List()
    throws Database_Front_Exception, Database_Front_No_Results_Exception {
        
        String sql_Statement = "show tables";
        
        PreparedStatement ps;
        ResultSet results = null;
        
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            
            results = ps.executeQuery();
            
            if (results.next() == false) {
                throw new Database_Front_No_Results_Exception("Database_Front_V2.get_Table_List : No Tables Found.");
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
            throw new Database_Front_Exception("Database_Front_V2.get_Table_List Failed, because :" + sqle.getMessage());
        }
        
    }
    
    
    public synchronized DFBase[] get_Table_Column_List(String tableName)
    throws Database_Front_Exception, DFException, SQLException {
        
        String sql_Statement = "select * from " + tableName;
        
        PreparedStatement ps= null;
        ResultSet results = null;
        
        
        try {
            ps = connection.prepareStatement(sql_Statement);
            
            results = ps.executeQuery();
            
            //if (results.next() == false) {
            //throw new Database_Front_Exception("Database_Front_V2.get_Table_Column_List : No Columns Found for Table :" + tableName + ":.");
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
                    throw new Database_Front_Exception("Database_Front_V2: get_Table_Column_List() for Table :" + tableName + ": Failed, because Column Name :" + columnName +": is of a type :" + columnTypeName + ": that is unknown to get_Table_Column_List().");
                }
            }
            
            ps.close();
            
            return answer_List;
            
        }
        catch(SQLException sqle){
            throw new SQLException("Database_Front_V2.get_Table_Column_List Failed, because :" + sqle.getMessage());
        }
        catch(DFException dfe){
            throw new DFException("Database_Front_V2.get_Table_Column_List Failed, because :" + dfe.getMessage());
        }
    }
    
    
}
