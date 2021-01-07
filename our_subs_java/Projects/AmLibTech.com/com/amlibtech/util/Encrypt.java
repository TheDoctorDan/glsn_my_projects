/*
 * Encrypt.java
 *
 * Created on July 20, 2006, 10:35 AM
 */

package com.amlibtech.util;

import com.amlibtech.database.*;
import com.amlibtech.login.data.*;

import java.security.*;
import java.io.*;
import javax.crypto.*;
import sun.misc.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  dgleeson
 */



public class Encrypt{

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
    
    
    public static Key getKey() throws Database_Front_Exception, SQLException, IOException, ClassNotFoundException, Database_Front_No_Results_Exception {
        Database_Front alt_Asp_Database_Front;
        
        //Client_App_Constants alt_Asp_Client_App_Constants = new Client_App_Constants("jdbc:mysql://localhost/ALTASP", "ASP_admin", "master23er2indi");
        Client_App_Constants alt_Asp_Client_App_Constants = ALT_ASP_Client_App_Constants.getALT_ASP_Client_App_Constants();
        
        alt_Asp_Database_Front = new Database_Front(alt_Asp_Client_App_Constants);
        
        try{
            alt_Asp_Database_Front.open();
        }
        catch(Database_Front_Exception e){
            throw e;
        }
        
        
        
        Security.addProvider( new com.sun.crypto.provider.SunJCE() );
        Key key = null;
        
        //ObjectInputStream in = new ObjectInputStream (new FileInputStream("key.txt"));
        //key = (Key)in.readObject();
        //in.close();
        
        //write from database to key object
        PreparedStatement ps;
        try {
            ps=alt_Asp_Database_Front.get_Connection().prepareStatement("SELECT KeyString FROM KeyTable");
        }
        catch(SQLException e){
            throw e;
        }
        ResultSet rs;
        try {
            rs = ps.executeQuery();
        }
        catch(SQLException e){
            throw e;
        }
        
        boolean found;
        try {
            found=rs.next();
        }
        catch(SQLException e){
            throw e;
        }
        if(found){
            byte[]keyBytes;
            try {
                keyBytes = rs.getBytes("KeyString");
            }
            catch(SQLException e){
                throw e;
            }
            ByteArrayInputStream keyArrayStream =  new ByteArrayInputStream(keyBytes);
            
            ObjectInputStream keyObjectStream;
            try {
                keyObjectStream =  new ObjectInputStream(keyArrayStream);
            }
            catch(IOException e){
                throw e;
            }
            try {
                key= (Key)keyObjectStream.readObject();
            }
            catch(IOException e){
                throw e;
            }
            catch(ClassNotFoundException e){
                throw e;
            }
            
        }else{
            throw new Database_Front_No_Results_Exception("No Key found in database.");
        }
        return key;
    }
    
    public static Cipher getCipher(Key key) throws EncryptException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        }
        catch(NoSuchAlgorithmException e){
            throw new EncryptException("ClassNotFoundException: "+e.getMessage());
        }
        catch(NoSuchPaddingException e){
            throw new EncryptException("NoSuchPaddingException: "+e.getMessage());
        }
       return cipher;
    }
    
    public static String getEncryptedStringToString(String input) throws EncryptException {
        
        
        Key key;
        try {
            key = getKey();
        }
        catch(Exception e){
            throw new EncryptException("Encrypt.getKey() failed  because :" + e.getMessage());
        }
        
        Cipher cipher = getCipher(key);        
        
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
        catch(InvalidKeyException e){
            throw new EncryptException("cipher.init: InvalidKeyException: "+e.getMessage());
        }
        
        
        
        byte[]inputBytes;
        try {
            inputBytes = input.getBytes("UTF8");
        }
        catch(UnsupportedEncodingException e){
            throw new EncryptException("UnsupportedEncodingException: "+e.getMessage());
        }
        
        //Encode
        
        byte[] outputBytes;
        try {
            outputBytes = cipher.doFinal(inputBytes);
        }
        catch(IllegalBlockSizeException e){
            throw new EncryptException("IllegalBlockSizeException: "+e.getMessage());
        }
        catch(BadPaddingException e){
            throw new EncryptException("BadPaddingException: "+e.getMessage());
        }
        
        
        
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encode(outputBytes);
        
        return base64;
        
    }
    
    public static String getDecryptedStringToString(String input) throws EncryptException {
        
        Key key;
        try {
            key = getKey();
        }
        catch(Exception e){
            throw new EncryptException("Encrypt.getKey() failed  because :" + e.getMessage());
        }
        
                Cipher cipher = getCipher(key);        

        
        
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch(InvalidKeyException e){
            throw new EncryptException("InvalidKeyException: "+e.getMessage());
        }
        
        
        
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] inputBytes;
        try {
            inputBytes  = decoder.decodeBuffer(input);
        }
        catch(IOException e){
            throw new EncryptException("IOException: "+e.getMessage());
        }
        
        
        
        //decode
        
        byte[] outputBytes;
        try {
            outputBytes = cipher.doFinal(inputBytes);
        }
        catch(IllegalBlockSizeException e){
            throw new EncryptException("IllegalBlockSizeException: "+e.getMessage());
        }
        catch(BadPaddingException e){
            throw new EncryptException("IllegalBlockSizeException: "+e.getMessage());
        }
        
        String ouputString;
        try {
            ouputString = new String(outputBytes, "UTF8");
        }
        catch(UnsupportedEncodingException e){
            throw new EncryptException("UnsupportedEncodingException: "+e.getMessage());
        }
        
        return ouputString;
        
        
    }
    
    
    public static byte[] getEncryptedStringToBytes(String input) throws EncryptException {
        
        
        Key key;
        try {
            key = getKey();
        }
        catch(Exception e){
            throw new EncryptException("Encrypt.getKey() failed  because :" + e.getMessage());
        }
        
                Cipher cipher = getCipher(key);        

        
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
        catch(InvalidKeyException e){
            throw new EncryptException("cipher.init: InvalidKeyException: "+e.getMessage());
        }
        
        
        
        byte[]inputBytes;
        try {
            inputBytes = input.getBytes("UTF8");
        }
        catch(UnsupportedEncodingException e){
            throw new EncryptException("UnsupportedEncodingException: "+e.getMessage());
        }
        
        //Encode
        
        byte[] outputBytes;
        try {
            outputBytes = cipher.doFinal(inputBytes);
        }
        catch(IllegalBlockSizeException e){
            throw new EncryptException("IllegalBlockSizeException: "+e.getMessage());
        }
        catch(BadPaddingException e){
            throw new EncryptException("BadPaddingException: "+e.getMessage());
        }
        
        return outputBytes;
    }
    
    public static String getDecryptedBytesToString(byte[] inputBytes) throws EncryptException {
        
        Key key;
        try {
            key = getKey();
        }
        catch(Exception e){
            throw new EncryptException("Encrypt.getKey() failed  because :" + e.getMessage());
        }
        
                Cipher cipher = getCipher(key);        

        
        
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch(InvalidKeyException e){
            throw new EncryptException("InvalidKeyException: "+e.getMessage());
        }
        
        
        
        //decode
        
        byte[] outputBytes;
        try {
            outputBytes = cipher.doFinal(inputBytes);
        }
        catch(IllegalBlockSizeException e){
            throw new EncryptException("IllegalBlockSizeException: "+e.getMessage());
        }
        catch(BadPaddingException e){
            throw new EncryptException("IllegalBlockSizeException: "+e.getMessage());
        }
        
        String ouputString;
        try {
            ouputString = new String(outputBytes, "UTF8");
        }
        catch(UnsupportedEncodingException e){
            throw new EncryptException("UnsupportedEncodingException: "+e.getMessage());
        }
        
        return ouputString;
        
        
    }
    
    
    
}//end Encrypt class




