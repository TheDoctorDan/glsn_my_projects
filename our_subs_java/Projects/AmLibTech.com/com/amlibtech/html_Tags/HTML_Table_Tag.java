/*
 * HTML_Table_Tag.java
 *
 * Created on July 11, 2006, 4:28 PM
 */

package com.amlibtech.html_Tags;

import com.amlibtech.util.*;
import java.io.*;

/**
 *
 * @author  dgleeson
 */
public class HTML_Table_Tag extends Html_Tag_Base {
    
    private String  table_Name;
    private PrintWriter out;
    private String  class_Name;
    private String  caption;
    private String  width;
    private String  height;
    private String  border;
    private String  cellspacing;
    private String  cellpadding;
    
    private int row_Count;
    private boolean use_Even_Odd_Classes;
    
    /** Creates a new instance of HTML_Table_Tag */
    public HTML_Table_Tag(String table_Name, PrintWriter out) {
        super("<table>");
        this.table_Name = table_Name;
        this.out=out;
        this.class_Name="";
        this.caption="";
        this.width="";
        this.height="";
        this.border="";
        this.cellspacing="";
        this.cellpadding="";
        
        this.row_Count=0;
        this.use_Even_Odd_Classes=false;
    }
    
    public HTML_Table_Tag(String table_Name, PrintWriter out, String class_Name) {
        this(table_Name, out);
        this.class_Name=class_Name;
    }
    
    public HTML_Table_Tag(String table_Name, PrintWriter out, String class_Name, String  caption) {
        this(table_Name, out, class_Name);
        this.caption=caption;
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
    
    public void do_Display_HTML_Table_Begin_Std(){
        out.println("<table id='"+table_Name+"' ");
        if(!this.class_Name.equals(""))
            out.println("class='" +class_Name+ "' ");
        if(!this.width.equals(""))
            out.println("width='" + this.width + "' ");
        if(!this.height.equals(""))
            out.println("height='" + this.height + "' ");
        if(!this.border.equals(""))
            out.println("border='" + this.border + "' ");

        out.println(">");
        
        if(!this.caption.equals(""))
            out.println("<caption align='top'>" +StringPlus.html_Encode(this.caption) + "</caption>");
        
        this.row_Count=0;
    }
    
    public void do_Display_HTML_Table_End(){
        out.println("</table>");
    }
    
    public void do_Display_HTML_TBody_Begin_Std(){
        out.println("<tbody>");
    }
    
    
    public void do_Display_HTML_TBody_End(){
        out.println("</tbody>");
    }
    
    public void do_Display_HTML_Row_Begin_Std(){
        this.row_Count++;
        if(this.use_Even_Odd_Classes){
            if(this.row_Count%2 ==0){
                // even
                out.println("<tr class='" + this.class_Name + "_Even" + "' >");
            }else{
                // odd
                out.println("<tr class='" + this.class_Name + "_Odd" + "' >");
            }
        }else
            out.println("<tr>");
    }
    
    public void do_Display_HTML_Row_End(){
        out.println("</tr>");
    }
    
    
    public void do_Display_HTML_TH_Element(String element){
        out.println("<th nowrap>" + element + "</th>\n");
    }
    
    public void do_Display_HTML_TH_Element_Col_Span(String element, int col_Span){
        out.println("<th align='center' colspan='"+col_Span+"' >" + element + "</th>\n");
    }
    
    public void do_Display_HTML_TH_Element_Width(String element, int width){
        out.println("<th width='"+width+"' >" + element + "</th>\n");
    }
    
    public void do_Display_HTML_TH_Begin(){
        out.println("<th>\n");
    }
    
    public void do_Display_HTML_TH_End(){
        out.println("</th>\n");
    }
    
    public void do_Display_HTML_TD_Element(String element){
        out.println("<td>" + element + "</td>\n");
    }
    
    public void do_Display_HTML_TD_Element_Col_Span(String element, int col_Span){
        out.println("<td colspan='"+col_Span+"' >" + element + "</td>\n");
    }
    
    public void do_Display_HTML_TD_Begin(){
        out.println("<td>\n");
    }
    
    public void do_Display_HTML_TD_Begin_Col_Span(int col_Span){
        out.println("<td colspan='"+col_Span+"' >\n");
    }
    
    public void do_Display_HTML_TD_End(){
        out.println("</td>\n");
    }
    
    /**
     * Getter for property table_Name.
     * @return Value of property table_Name.
     */
    public java.lang.String getTable_Name() {
        return table_Name;
    }
    
    /**
     * Setter for property table_Name.
     * @param table_Name New value of property table_Name.
     */
    public void setTable_Name(java.lang.String table_Name) {
        this.table_Name = table_Name;
    }
    
    /**
     * Getter for property out.
     * @return Value of property out.
     */
    public java.io.PrintWriter getOut() {
        return out;
    }
    
    /**
     * Setter for property out.
     * @param out New value of property out.
     */
    public void setOut(java.io.PrintWriter out) {
        this.out = out;
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
     * Getter for property row_Count.
     * @return Value of property row_Count.
     */
    public int getRow_Count() {
        return row_Count;
    }
    
    /**
     * Setter for property row_Count.
     * @param row_Count New value of property row_Count.
     */
    public void setRow_Count(int row_Count) {
        this.row_Count = row_Count;
    }
    
    /**
     * Getter for property use_Even_Odd_Classes.
     * @return Value of property use_Even_Odd_Classes.
     */
    public boolean isUse_Even_Odd_Classes() {
        return use_Even_Odd_Classes;
    }
    
    /**
     * Setter for property use_Even_Odd_Classes.
     * @param use_Even_Odd_Classes New value of property use_Even_Odd_Classes.
     */
    public void setUse_Even_Odd_Classes(boolean use_Even_Odd_Classes) {
        this.use_Even_Odd_Classes = use_Even_Odd_Classes;
    }
    
    /**
     * Getter for property width.
     * @return Value of property width.
     */
    public java.lang.String getWidth() {
        return width;
    }
    
    /**
     * Setter for property width.
     * @param width New value of property width.
     */
    public void setWidth(java.lang.String width) {
        this.width = width;
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
     * Getter for property height.
     * @return Value of property height.
     */
    public java.lang.String getHeight() {
        return height;
    }
    
    /**
     * Setter for property height.
     * @param height New value of property height.
     */
    public void setHeight(java.lang.String height) {
        this.height = height;
    }
    
    /**
     * Getter for property cellspacing.
     * @return Value of property cellspacing.
     */
    public java.lang.String getCellspacing() {
        return cellspacing;
    }
    
    /**
     * Setter for property cellspacing.
     * @param cellspacing New value of property cellspacing.
     */
    public void setCellspacing(java.lang.String cellspacing) {
        this.cellspacing = cellspacing;
    }
    
    /**
     * Getter for property cellpadding.
     * @return Value of property cellpadding.
     */
    public java.lang.String getCellpadding() {
        return cellpadding;
    }
    
    /**
     * Setter for property cellpadding.
     * @param cellpadding New value of property cellpadding.
     */
    public void setCellpadding(java.lang.String cellpadding) {
        this.cellpadding = cellpadding;
    }
    
}
