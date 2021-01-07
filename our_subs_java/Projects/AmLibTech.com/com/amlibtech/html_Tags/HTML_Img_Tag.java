/*
 * HTML_Img_Tag.java
 *
 * Created on July 11, 2006, 4:28 PM
 */

package com.amlibtech.html_Tags;

import com.amlibtech.util.*;
import com.amlibtech.web_Processes.*;
import java.io.*;

/**
 *
 * @author  dgleeson
 */
public class HTML_Img_Tag extends Html_Tag_Base {
    
    private String  src;
    private String  alt;
    
    private String  class_Name;
    private int  width;
    private int  height;
    private String  border;
    private String  borderColor;
    
    
    
    /** Creates a new instance of HTML_Img_Tag */
    public HTML_Img_Tag(String src, String alt) {
        super("<table>");
        this.src = src;
        this.alt = alt;
        
        this.class_Name="";
        this.width=0;
        this.height=0;
        this.border="";
        this.borderColor="";
        
        
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
     * Getter for property src.
     * @return Value of property src.
     */
    public java.lang.String getSrc() {
        return src;
    }
    
    /**
     * Setter for property src.
     * @param src New value of property src.
     */
    public void setSrc(java.lang.String src) {
        this.src = src;
    }
    
    /**
     * Getter for property alt.
     * @return Value of property alt.
     */
    public java.lang.String getAlt() {
        return alt;
    }
    
    /**
     * Setter for property alt.
     * @param alt New value of property alt.
     */
    public void setAlt(java.lang.String alt) {
        this.alt = alt;
    }
    
    /**
     * Getter for property class_Name.
     * @return Value of property class_Name.
     */
    public java.lang.String getClass_Name() {
        return class_Name;
    }
    
    /**
     * Setter for property class_Name.
     * @param class_Name New value of property class_Name.
     */
    public void setClass_Name(java.lang.String class_Name) {
        this.class_Name = class_Name;
    }
    
    
    
    /**
     * Getter for property border.
     * @return Value of property border.
     */
    public java.lang.String getBorder() {
        return border;
    }
    
    /**
     * Setter for property border.
     * @param border New value of property border.
     */
    public void setBorder(java.lang.String border) {
        this.border = border;
    }
    
    /**
     * Getter for property borderColor.
     * @return Value of property borderColor.
     */
    public java.lang.String getBorderColor() {
        return borderColor;
    }
    
    /**
     * Setter for property borderColor.
     * @param borderColor New value of property borderColor.
     */
    public void setBorderColor(java.lang.String borderColor) {
        this.borderColor = borderColor;
    }
    
    
    /**
     * Getter for property width.
     * @return Value of property width.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Setter for property width.
     * @param width New value of property width.
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * Getter for property height.
     * @return Value of property height.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Setter for property height.
     * @param height New value of property height.
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    
    public String getSrc_Context_Path(Web_Process web_Process){
        String src_ContextPath;
        if(src.startsWith("/"))
            src_ContextPath = src;
        else
            src_ContextPath = web_Process.getSubscriber_Organization_Portal().getImage_FullPath() +"/" + src;
        return src_ContextPath;
    }
    
    public String getSrc_URL(Web_Process web_Process){
        String src_ContextPath = getSrc_Context_Path(web_Process);
        if(src_ContextPath.startsWith("/"))
            src_ContextPath = src_ContextPath.substring(1);
            
        String base_URL = web_Process.getBase_URL();
        if(base_URL.endsWith("/"))
            base_URL = base_URL.substring(0, base_URL.length()-2);
        
        return web_Process.getBase_URL() + "/" + src_ContextPath;
       
        
    }
    
    
    
    
    public void do_Display_HTML(Web_Process web_Process){
        PrintWriter out = web_Process.getOut();
        
        String command = "<img src='";
        
        if(src.startsWith("/"))
            command += StringPlus.html_Encode(src);
        else
            command += StringPlus.html_Encode(web_Process.getSubscriber_Organization_Portal().getImage_FullPath()) +"/" + StringPlus.html_Encode(src);
        command += "' ";
        
        command += "alt='" + StringPlus.html_Encode(alt) +"' ";
        
        if(width!=0)
            command += "width='" + width + "' ";
        if(height!=0)
            command += " height='" + height + "' ";
        
        if(!class_Name.equals(""))
            command += " class='" + StringPlus.html_Encode(class_Name) + "' ";
        
        if(!border.equals(""))
            command += " border='" + StringPlus.html_Encode(border) + "' ";
        
        if(!borderColor.equals(""))
            command += " bordercolor='" + StringPlus.html_Encode(borderColor) + "' ";
        
        command += " > ";
        
        out.println(command);
        
    }
    
}
