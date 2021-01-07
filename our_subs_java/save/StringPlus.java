/* StringPlus.java */
/**
 *
 * Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004
 * American Liberator Technologies
 * All Rights Reserved
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
 * American Liberator Technologies
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 **/

package com.amlibtech.util;

public class StringPlus {

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
    
    public static String left_Fill(String in_String, int length, char c){
        String result = in_String;
        while(result.length() < length){
            result=new StringBuffer().append(c).append(result).toString();
        }
        return result;
    }
    
    public static String left_Fill(int num, int length, char c){
        String in_String = String.valueOf(num);
        return(left_Fill(in_String, length, c));
    }
    
    public static String left_Fill(long num, int length, char c){
        String in_String = String.valueOf(num);
        return(left_Fill(in_String, length, c));
    }
    
    
    
    public static String right_Fill(String in_String, int length, char c){
        String result = in_String;
        while(result.length() < length){
            result=new StringBuffer().append(result).append(c).toString();
        }
        return(result);
    }
    
    public static String right_Fill(int num, int length, char c){
        String in_String = String.valueOf(num);
        return(right_Fill(in_String, length, c));
    }
    
    public static String right_Fill(long num, int length, char c){
        String in_String = String.valueOf(num);
        return(right_Fill(in_String, length, c));
    }
    
    
    public static String center_Fill(String in_String, int length, char c){
        String result = in_String;
        while(result.length() < length){
            if(result.length() %2 ==0)
                result=new StringBuffer().append(result).append(c).toString();
            else
                result=new StringBuffer().append(c).append(result).toString();
        }
        return(result);
    }
    
    /**
     *
     * KEY IS A KEY TO BE FILLED
     * MASK IS A MASK TO FOLLOW
     * IER IS AN ERROR FLAG, 0=NO ERROR
     *
     * MASKING CHARACTERS ARE:
     *
     * 1=ALPHA. LEFT  FILL ' '
     * 2=ALPHA. RIGHT FILL ' '
     * 3=DIGIT  LEFT  FILL ' '
     * 4=DIGIT  RIGHT FILL ' '
     * 5=DIGIT  LEFT  FILL '0
     * 6=DIGIT  RIGHT FILL '0'
     * 7=ALPHA OR DIGIT LEFT  FILL ' '
     * 8=ALPHA OR DIGIT RIGHT FILL ' '
     * 9=ANY PRINTABLE CHAR BUT ','  LEFT  FILL ' '
     * 0=ANY PRINTABLE CHAR BUT ','  RIGHT FILL ' '
     *
     **/
    
    public static String	key_Fill(String key, String mask) throws StringPlus_Exception {
        
        int	mask_ptr;
        int	key_ptr;
        StringBuffer	dmys1 = new StringBuffer();
        
        StringBuffer	result = new StringBuffer();
        String  mess;
        char	last_c;
        boolean	left_fill_ended_for_pattern= false;
        boolean right_fill_started_for_pattern=false;
        
        
        
        last_c=0;
        
        mask_ptr = 0;
        key_ptr = 0;
        while (mask_ptr< mask.length() || key_ptr <key.length()) {
            
            
            if(last_c != mask.charAt(mask_ptr)){
                result = result.append(dmys1);
                dmys1=new StringBuffer();
                left_fill_ended_for_pattern=false;
                right_fill_started_for_pattern=false;
            }
            if(mask_ptr >= mask.length()){
                /* end of mask */
                
                mess = "Unexpected End of mask";
                throw new StringPlus_Exception(mess);
            }
            
            switch (mask.charAt(mask_ptr)){
                case '1':
                    /* 1=ALPHA. LEFT  FILL ' ' */
                    if(key_ptr >= key.length()){
                        dmys1=new StringBuffer().append(" ").append(dmys1);
                    }else  if(key.substring(key_ptr, key_ptr+1).matches("[a-zA-Z]")){
                        left_fill_ended_for_pattern=true;
                        dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                        key_ptr++;
                    }else if(key.substring(key_ptr, key_ptr+1).matches("[0-9]")){
                        mess = "Digit where there should be alpha";
                        throw new StringPlus_Exception(mess);
                    }else if(key.charAt(key_ptr)==' '){
                        if(left_fill_ended_for_pattern){
                            dmys1=new StringBuffer().append(" ").append(dmys1);
                        }else{
                            dmys1=new StringBuffer().append(" ").append(dmys1);
                            key_ptr++;
                        }
                    }else{
                        dmys1=new StringBuffer().append(" ").append(dmys1);
                    }
                    break;
                    
                case '2':
                    /* 2=ALPHA. RIGHT FILL ' ' */
                    if(key_ptr >= key.length()){
                        dmys1 = dmys1.append(" ");
                        right_fill_started_for_pattern=true;
                        
                    } else if(key.substring(key_ptr, key_ptr+1).matches("[a-zA-Z]")){
                        if(right_fill_started_for_pattern){
                            dmys1 = dmys1.append(" ");
                            right_fill_started_for_pattern=true;
                        }else{
                            dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                            key_ptr++;
                        }
                        
                    }else if(key.substring(key_ptr, key_ptr+1).matches("[0-9]")){
                        mess="Digit where there should be alpha";
                        throw new StringPlus_Exception(mess);
                        
                    }else if(key.charAt(key_ptr) ==' '){
                        dmys1 = dmys1.append(" ");
                        key_ptr++;
                        right_fill_started_for_pattern=true;
                        
                    }else{
                        dmys1 = dmys1.append(" ");
                        right_fill_started_for_pattern=true;
                    }
                    break;
                    
                case '3':
                    /* 3=DIGIT  LEFT  FILL ' ' */
                    if(key_ptr >= key.length()){
                        dmys1=new StringBuffer().append(" ").append(dmys1);
                        
                    } else if(key.substring(key_ptr, key_ptr+1).matches("[0-9]")){
                        left_fill_ended_for_pattern=true;
                        dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                        key_ptr++;
                    }else if(key.substring(key_ptr, key_ptr+1).matches("[a-zA-Z]")){
                        mess = "Alpha where there should be digit";
                        throw new StringPlus_Exception(mess);
                    }else if(key.charAt(key_ptr) ==' '){
                        if(left_fill_ended_for_pattern){
                            dmys1=new StringBuffer().append(" ").append(dmys1);
                        }else{
                            dmys1=new StringBuffer().append(" ").append(dmys1);
                            key_ptr++;
                        }
                    }else{
                        dmys1=new StringBuffer().append(" ").append(dmys1);
                    }
                    break;
                    
                case '4':
                    /* 4=DIGIT  RIGHT FILL ' ' */
                    if(key_ptr >= key.length()){
                        dmys1 = dmys1.append(" ");
                        right_fill_started_for_pattern=true;
                    } else if(key.substring(key_ptr, key_ptr+1).matches("[0-9]")){
                        if(right_fill_started_for_pattern){
                            dmys1 = dmys1.append(" ");
                            right_fill_started_for_pattern=true;
                        }else{
                            dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                            key_ptr++;
                        }
                    }else if(key.substring(key_ptr, key_ptr+1).matches("[a-zA-Z]")){
                        mess = "Alpha where there should be digit";
                        throw new StringPlus_Exception(mess);
                    }else if(key.charAt(key_ptr) ==' '){
                        dmys1 = dmys1.append(" ");
                        key_ptr++;
                        right_fill_started_for_pattern=true;
                    }else{
                        dmys1 = dmys1.append(" ");
                        right_fill_started_for_pattern=true;
                    }
                    break;
                    
                case '5':
                    /* 5=DIGIT  LEFT  FILL '0  */
                    if(key_ptr >= key.length()){
                        dmys1=new StringBuffer().append("0").append(dmys1);
                        
                    }else if(key.substring(key_ptr, key_ptr+1).matches("[0-9]")){
                        left_fill_ended_for_pattern=true;
                        dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                        key_ptr++;
                        
                    }else if(key.substring(key_ptr, key_ptr+1).matches("[a-zA-Z]")){
                        mess = "Alpha where there should be digit";
                        throw new StringPlus_Exception(mess);
                        
                    }else{
                        dmys1=new StringBuffer().append("0").append(dmys1);
                        
                    }
                    break;
                    
                case '6':
                    /* 6=DIGIT  RIGHT FILL '0' */
                    if(key_ptr >= key.length()){
                        dmys1 = dmys1.append("0");
                    } else if(key.substring(key_ptr, key_ptr+1).matches("[0-9]")){
                        if(right_fill_started_for_pattern){
                            dmys1 = dmys1.append("0");
                        }else{
                            dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                            key_ptr++;
                        }
                    }else if(key.substring(key_ptr, key_ptr+1).matches("[a-zA-Z]")){
                        mess = "Alpha where there should be digit";
                        throw new StringPlus_Exception(mess);
                    }else{
                        dmys1 = dmys1.append("0");
                    }
                    break;
                    
                case '7':
                    /* 7=ALPHA OR DIGIT LEFT  FILL ' ' */
                    if(key_ptr >= key.length()){
                        dmys1=new StringBuffer().append(" ").append(dmys1);
                        
                    } else if(key.substring(key_ptr, key_ptr+1).matches("[0-9][a-z][A-Z]")){
                        left_fill_ended_for_pattern=true;
                        dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                        key_ptr++;
                    }else if(key.charAt(key_ptr) ==' '){
                        if(left_fill_ended_for_pattern){
                            dmys1=new StringBuffer().append(" ").append(dmys1);
                        }else{
                            dmys1=new StringBuffer().append(" ").append(dmys1);
                            key_ptr++;
                        }
                    }else{
                        dmys1=new StringBuffer().append(" ").append(dmys1);
                    }
                    break;
                    
                case '8':
                    /* 8=ALPHA OR DIGIT RIGHT FILL ' ' */
                    if(key_ptr >= key.length()){
                        dmys1 = dmys1.append(" ");
                        right_fill_started_for_pattern=true;
                        
                    } else if(key.substring(key_ptr, key_ptr+1).matches("[0-9][a-z][A-Z]")){
                        if(right_fill_started_for_pattern){
                            dmys1 = dmys1.append(" ");
                            right_fill_started_for_pattern=true;
                        }else{
                            dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                            key_ptr++;
                        }
                    }else if(key.charAt(key_ptr) ==' '){
                        dmys1 = dmys1.append(" ");
                        key_ptr++;
                        right_fill_started_for_pattern=true;
                    }else{
                        dmys1 = dmys1.append(" ");
                        right_fill_started_for_pattern=true;
                    }
                    break;
                    
                case '9':
                    /* 9=ANY PRINTABLE CHAR BUT ','  LEFT FILL ' ' */
                    if(key_ptr >= key.length()){
                        dmys1=new StringBuffer().append(" ").append(dmys1);
                    } else if(key.charAt(key_ptr) ==','){
                        dmys1=new StringBuffer().append(" ").append(dmys1);
                    } else if(key.substring(key_ptr,key_ptr+1).matches("[a-z][A-Z][0-9][!@#$%^&*()-_=+\\[{\\]};:'\"\\\\/\\?\\.\\,<>]") ){
                        left_fill_ended_for_pattern=true;
                        dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                        key_ptr++;
                    }else{
                        dmys1=new StringBuffer().append(" ").append(dmys1);
                    }
                    break;
                    
                case '0':
                    /* 9=ANY PRINTABLE CHAR BUT ','  RIGHT FILL ' ' */
                    if(key_ptr >= key.length()){
                        dmys1 = dmys1.append(" ");
                        right_fill_started_for_pattern=true;
                    } else if(key.substring(key_ptr,key_ptr+1).matches("[a-z][A-Z][0-9][!@#$%^&*()-_=+\\[{\\]};:'\"\\\\/\\?\\.\\,<>]") ){
                        if(right_fill_started_for_pattern){
                            dmys1 = dmys1.append(" ");
                            right_fill_started_for_pattern=true;
                        }else{
                            dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                            key_ptr++;
                        }
                    }else if(key.charAt(key_ptr) ==','){
                        dmys1 = dmys1.append(" ");
                        key_ptr++;
                        right_fill_started_for_pattern=true;
                    }else{
                        dmys1 = dmys1.append(" ");
                        right_fill_started_for_pattern=true;
                    }
                    break;
                    
                    
                default:
                    
                    if(key_ptr == key.length()){
                        dmys1 = dmys1.append(mask.substring(mask_ptr, mask_ptr+1));
                        
                    }else if(mask.charAt(mask_ptr) == key.charAt(key_ptr)){
                        dmys1 = dmys1.append(key.substring(key_ptr,key_ptr+1));
                        key_ptr++;
                        
                    }else if(key.substring(key_ptr, key_ptr+1).matches("[0-9][a-z][A-Z]")){
                        dmys1 = dmys1.append(mask.substring(mask_ptr,mask_ptr+1));
                        
                    }else {
                        mess = "field separator in key not same in mask";
                        throw new StringPlus_Exception(mess);
                    }
                    break;
            }
            last_c = mask.charAt(mask_ptr);
            mask_ptr++;
            
        }
        result = result.append(dmys1);
        
        
        return result.toString();
        
    }
    
    
    public static String XML_Encode(String in_String){
        if(in_String==null)
            return "";
        String result = in_String;
        result = result.replaceAll("&", "&amp;");
        result = result.replaceAll("<", "&#60;");
        result = result.replaceAll(">", "&#62;");
        result = result.replaceAll("'", "&#39;");
        result = result.replaceAll("\"", "&#34;");
        return result;
    }
    
    public static String html_Encode(String in_String){
        if(in_String==null)
            return "";
        String result = in_String;

        result = result.replaceAll("<", "&#60");
        result = result.replaceAll("&lt;", "<");
        //result = result.replaceAll("&", "&amp;");

        result = result.replaceAll("'", "&#39");
        result = result.replaceAll("`", "&#96");
        result = result.replaceAll("\"", "&#34");
        return result;
    }
    
    public static String http_Encode(String in_String){
        if(in_String==null)
            return "";
        String result = in_String;
        result = result.replaceAll(" ", "%20");
        result = result.replaceAll("<", "%3C");
        result = result.replaceAll("'", "%27");
        result = result.replaceAll("\"", "%22");
        result = result.replaceAll("\\&", "%26");
        result = result.replaceAll("\\?", "%3F");
        return result;
    }
    
    public static String javascript_Encode(String in_String){
        if(in_String==null)
            return "";
        String result = in_String;
        result = result.replaceAll("<", "&#60");
        result = result.replaceAll("'", "&#92&#39");
        result = result.replaceAll("\"", "&#34");
        result = result.replaceAll("[\015\012]", " ");
        return result;
    }
    
    
    public static String[] sort_Array(String[] list){
        // bubble sort
        String temp;
        for(int n=0; n<list.length-1;n++){
            for(int i=list.length-1;i>n;i--){
                int j=i-1;
                if(list[i].compareTo(list[j])<0){
                    temp=list[i];
                    list[i]=list[j];
                    list[j]=temp;
                }
            }
        }
        return list;
    }
    
    
    public static int getArray_Index_of_Value(String[] array_Of_Strings, String value){
        if(array_Of_Strings==null)
            return(-1);
        if(value==null)
            return(-1);
        for(int index=0;index<array_Of_Strings.length;index++){
            if(array_Of_Strings[index].equals(value))
                return index;
        }
        return(-1);
    }
    
    
    public static String bytes_To_Hex_String(byte[] passed_Bytes){
        byte high_Mask = (byte)0xF0;
        byte low_Mask = (byte)0x0F;
        
        
        String hex_String="";
        for(int i=0; i<passed_Bytes.length;i++){
            
            int high_Half = ((passed_Bytes[i] & high_Mask) >>4)&low_Mask;
            hex_String += Integer.toHexString(high_Half);
            
            int low_Half = passed_Bytes[i] & low_Mask;
            hex_String += Integer.toHexString(low_Half);
        }
        
        return hex_String;
    }
    
    public static byte[] hex_String_to_Bytes(String hex_String)
    throws StringPlus_Exception {
        if(hex_String.length()%2 !=0){
            throw new StringPlus_Exception("hex_String_to_Bytes: requires passed Hex String length to be an even number.");
        }
        
        byte[] dmy_Bytes;
        
        dmy_Bytes = new byte[hex_String.length()/2];
        for(int i=0; i<dmy_Bytes.length;i++)
            dmy_Bytes[i]=0;
                
        for(int j=0; j<hex_String.length();j++){
            String hex_Digit=hex_String.substring(j,j+1);
            if((hex_Digit.compareTo("0") >=0 && hex_Digit.compareTo("9") <=0) ||
               (hex_Digit.compareTo("a") >=0 && hex_Digit.compareTo("f") <=0) ||
               (hex_Digit.compareTo("A") >=0 && hex_Digit.compareTo("A") <=0)
            ){
                int k=j/2;
                Byte b = Byte.decode("0x"+hex_Digit);
                if(j%2==0){
                    // high digit
                    dmy_Bytes[k] = (byte)(b.byteValue() <<4);
                }else{
                    // low digit
                    dmy_Bytes[k] |= b.byteValue();
                }
            }else{
                throw new StringPlus_Exception("hex_String_to_Bytes: passed Hex String must be hexidecimal digits 0-9, a-f or A-F.");
            }
        }

        return dmy_Bytes;
    }
    

}

