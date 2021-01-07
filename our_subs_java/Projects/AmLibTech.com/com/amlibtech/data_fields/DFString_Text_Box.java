/*
 * DFString_Text_Box.java
 *
 * Created on July 18, 2006, 4:01 PM
 */

package com.amlibtech.data_fields;

import com.amlibtech.util.*;

/**
 *
 * @author  dgleeson
 */
public class DFString_Text_Box extends DFString {
    private static String sql_Type = "text";
    
    private int box_Width;
    
    /** Creates a new instance of DFString_Text_Box */
    
    public DFString_Text_Box(String field_Name, String field_Title, int box_Width) throws DFException {
        super(field_Name, field_Title, 65000);
        this.box_Width = box_Width;
        
    }
    
    //    public DFString_Text_Box(String field_Name, String field_Title, int box_Width,  int min_Length, int max_Length) throws DFException {
    //        super(field_Name, field_Title, min_Length, 65000);
    //        this.box_Width = box_Width;
    //    }
    
    
    
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
    
    
    
    public String toHTML_Input(String prefix, String web_Root, String form_Name, int size, String option, String suffix) {
        
        String out_Option;
        if(option==null)
            out_Option="";
        else
            out_Option=option;
        
        if(size==0)
            size=this.box_Width;
        
        int rows=(max_Length/size);
        if(max_Length%size !=0)rows+=1;
        if(rows >10) rows=10;
        
        String answer;
        answer ="\t<textarea " +
        "name='" + prefix + this.field_Name + suffix + "' " +
        "id='" + prefix + this.field_Name + suffix + "' " +
        "cols='" + size +"' " +
        "rows='" + rows+ "' " +
        "maxlength='" + this.max_Length +"' " +
        out_Option + " >" +
        StringPlus.html_Encode(this.request_Value) + "</textarea>";
        if(this.min_Length !=0)
            answer += RED_ASTERISK_HTML;
        if(this.request_Error.length() !=0)
            answer += "<font color=\"#ff0000\">" + StringPlus.html_Encode(this.request_Error) + "</font>\n";
        return answer;
    }
    
    
    public String getSQL_Declaration(){
        return ("`" + this.field_Name + "` " + sql_Type + " NOT NULL default '',\n");
    }
    
    public boolean same_Field_Layout(DFBase dFBase){
        if (dFBase instanceof DFString_Text_Box){
            DFString_Text_Box temp = (DFString_Text_Box) dFBase;
            if(!same_Base_Field_Layout(temp)){
                return false;
            }
            if(!this.sql_Type.equals(temp.sql_Type)){
                return false;
            }
            
            return true;
        }else{
            // wrong type
            return false;
        }
    }
    
    public String toHTML2() {
        
        StringBuffer sb = new StringBuffer(this.toString());
        int len=sb.length();
        StringBuffer answer = new StringBuffer();
        for (int i = 0; i < len-1; i++){
            char ch1 = sb.charAt(i);
            char ch2 = sb.charAt(i+1);
            
            if (ch1 == 13 &&  // 13 is the ASCII code for a carriage return
            ch2 == 10){  // 10 is the ASCII code for a line feed
                answer.append("&lt;br>");
            }else{
                answer.append(ch1);
            }
        }
        int i=len-1;
        if(i>=0){
            char ch1 = sb.charAt(i);
            answer.append(ch1);
        }
        
        return ("<p>"+StringPlus.html_Encode(answer.toString())+"</p>");
        
    }
        
     public String toHTML3(int size, String option) {
         
        String out_Option;
        if(option==null)
            out_Option="";
        else
            out_Option=option;
        
        if(size==0)
            size=this.box_Width;
         
        
        int rows=(max_Length/size);
        if(max_Length%size !=0)rows+=1;
        if(rows >10) rows=10;
        
        String answer;
        answer ="<textarea  readonly " +
        "cols='" + size +"' " +
        "rows='" + rows+ "' " +
        "maxlength='" + this.max_Length +"' " +
        out_Option + " >" +
        StringPlus.html_Encode(this.request_Value) + "</textarea>";
        
        return answer;
    }
    
}
