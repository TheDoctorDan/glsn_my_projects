/*
 * File_Management.java
 *
 * Created on November 22, 2006, 12:02 PM
 */

package com.amlibtech.util;

import java.io.*;

/**
 *
 * @author  dgleeson
 */
public class File_Management {
    
    /** Creates a new instance of File_Management */
    public File_Management() {
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
    
    public static void copy_A_File(String in_Filename, String out_Filename)
    throws FileNotFoundException, IOException {
        
        // Create variables for in/out streams
        FileInputStream fis = null;
        FileOutputStream fos = null;
        
        
            // Create in stream
            fis = new FileInputStream(in_Filename);
            
            // Create out stream
            fos = new FileOutputStream(out_Filename);
            
            // Declare variable for each byte read
            int ch;
            
            // Read byte til end of file
            while ((ch = fis.read()) != -1) {
                
                
                // Put bytes read into out stream
                fos.write(ch);
            }
            
            // Catch FileNotFoundException
        
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
       
    }
    
    public static void append_A_File(String in_Filename, String out_Filename)
    throws FileNotFoundException, IOException {
        
        // Create variables for in/out streams
        FileInputStream fis = null;
        FileOutputStream fos = null;
        
        
            // Create in stream
            fis = new FileInputStream(in_Filename);
            
            // Create out stream
            fos = new FileOutputStream(out_Filename, true);
            
            // Declare variable for each byte read
            int ch;
            
            // Read byte til end of file
            while ((ch = fis.read()) != -1) {
                
                
                // Put bytes read into out stream
                fos.write(ch);
            }
            
            // Catch FileNotFoundException
        
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
       
    }
    
}
