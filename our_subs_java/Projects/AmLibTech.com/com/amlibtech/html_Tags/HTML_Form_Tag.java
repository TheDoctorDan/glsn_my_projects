/*
 * HTML_Form_Tag.java
 *
 * Created on July 6, 2006, 5:20 PM
 */

package com.amlibtech.html_Tags;

import com.amlibtech.util.*;
import java.io.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class HTML_Form_Tag extends Html_Tag_Base{
    
    private String form_Name;
    private String action;
    private int count_Of_Submit_Buttons;
    private int count_Of_Reset_Buttons;
    private PrintWriter out;
    private boolean multi_Part;
    
    /** Creates a new instance of HTML_Form_Tag */
    public HTML_Form_Tag(String form_Name, String action, PrintWriter out, boolean multi_Part) {
        super("<form>");
        this.form_Name = form_Name;
        this.action = action;
        this.out = out;
        
        this.count_Of_Submit_Buttons=0;
        this.count_Of_Reset_Buttons=0;
        
        this.multi_Part= multi_Part;
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
    
    public void do_Display_HTML_Form_Begin_Std(){
        out.println("<form id='"+form_Name+"' method='post' ");
        if(this.multi_Part)
            out.println(" enctype='multipart/form-data'" );
        out.println(" action='"+ action +"'>");
    }
    
    
    public void do_Display_HTML_Form_Begin_Href_Blank(){
        out.println("<form id='"+form_Name+"' method='post' ");
        if(this.multi_Part)
            out.println(" enctype='multipart/form-data'" );
        out.println("action='"+ action +"' target='_blank' >");
    }
    
    
    
    public void do_Display_HTML_Form_End(){
        out.println("</form>");
    }
    
    
    
    
    
    
    // launch button
    //    public void do_Display_HTML_Submit_Button(String param_Value, String text){
    //        out.println("<input type=submit  value='" + param_Value + "' >"+StringPlus.html_Encode(text)+"</input>");
    //        count_Of_Submit_Buttons++;
    //    }
    
    
    
    
    
    public void do_Display_HTML_Submit_Button(String param_Name, String param_Value, String text){
        out.println("<input type=submit  name='" + param_Name + "'  value='" + StringPlus.html_Encode(param_Value) + "' >"+StringPlus.html_Encode(text)+"</input>");
        count_Of_Submit_Buttons++;
    }
    
    
    
    public void do_Display_HTML_Submit_Button_In_Table(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag){
        table_Tag.do_Display_HTML_TD_Element("<input type=submit  name='" + param_Name + "'  value='" + StringPlus.html_Encode(param_Value) + "' >"+StringPlus.html_Encode(text)+"</input>");
        count_Of_Submit_Buttons++;
    }
    
    
    public void do_Display_HTML_Submit_Button_In_Table_With_Hidden_Parameter(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag, String param_Name2, String param_Value2){
        count_Of_Submit_Buttons++;
        String javascript_Function_Name = this.form_Name + "_HiddenP_" + count_Of_Submit_Buttons;
        table_Tag.do_Display_HTML_TD_Element("<input type=button    value='" + StringPlus.html_Encode(param_Value) + "'  onclick=\"" + javascript_Function_Name + "();\"></input>");
        
        
        out.println("<script type='text/javascript'>");
        out.println("function " + javascript_Function_Name + "(){");
        out.println("       " + table_Tag.getTable_Name() + " = document.getElementById(\"" + table_Tag.getTable_Name() + "\");");
        out.println("        var linkElement = document.createElement(\"input\");");
        out.println("        linkElement.setAttribute(\"type\", \"hidden\");");
        out.println("        linkElement.setAttribute(\"name\", \"" + param_Name + "\");");
        out.println("        linkElement.setAttribute(\"value\", \"" + param_Value + "\");");
        out.println("       " + table_Tag.getTable_Name() + ".appendChild(linkElement);");
        out.println("        var linkElement = document.createElement(\"input\");");
        out.println("        linkElement.setAttribute(\"type\", \"hidden\");");
        out.println("        linkElement.setAttribute(\"name\", \"" + param_Name2 + "\");");
        out.println("        linkElement.setAttribute(\"value\", \"" + param_Value2 + "\");");
        out.println("       " + table_Tag.getTable_Name() + ".appendChild(linkElement);");
        out.println("        document." + form_Name + ".submit();");
        out.println("}");
        out.println("</script>");
        
    }
    
    public void do_Display_HTML_Submit_Button_With_Label(String param_Name, String param_Value, String button_Label, String text){
        if(!param_Name.equals(""))
            out.println("<input type=hidden name='" + param_Name + "'  value='" + StringPlus.html_Encode(param_Value) + "'></input>");
        out.println("<input type=submit  value='" + button_Label + "'>"+StringPlus.html_Encode(text)+"</input>");
        count_Of_Submit_Buttons++;
    }
    
    
    
    public void do_Display_HTML_Submit_Image_In_Table(String param_Name, String param_Value, HTML_Table_Tag table_Tag, String image_FileName, int width, int height, String background_Color){
        
//        table_Tag.do_Display_HTML_TD_Element("<a href=\"javascript: " +
//        " document." + this.form_Name + "." + param_Name + ".value='" + param_Value + "';" +
//        " document." + this.form_Name + ".submit();\">" +
//        "<img src='"+ image_FileName + "' " + image_Options + " >" +
//        "</a>");
        
        //        table_Tag.do_Display_HTML_TD_Element("<input type='image'" +
        //        " name='" + param_Name + "'" +
        //        " value='" + param_Value + "'" +
        //        " src='" + image_FileName + "'" +
        //        " ></input>");
        
        String command = "<input type='image'" +
        " name='" + param_Name + "'" +
        " value='" + param_Value + "'";
        
        if(width!=0 || height!=0 || !background_Color.equals("")){
            command += " style='";
            if(width!=0)
                command += "width: " + width + "px;";
            if(height!=0)
                command += " height: " + height + "px;";
            if(!background_Color.equals(""))
                command += " background-color: " + background_Color + ";";
            command += "'";
        }
        
        command += " src='"+ image_FileName + "'";
        if(width!=0)
            command += " width='" + width + "'";
        if(height!=0)
            command += " height='" + height + "'";
        command += "  >";
        
        table_Tag.do_Display_HTML_TD_Element(command);
        count_Of_Submit_Buttons++;
    }
    
    
    
    
    public void do_Display_HTML_Submit_Image(String param_Name, String param_Value, String image_FileName, int width, int height, String background_Color){
        
//        table_Tag.do_Display_HTML_TD_Element("<a href=\"javascript: " +
//        " document." + this.form_Name + "." + param_Name + ".value='" + param_Value + "';" +
//        " document." + this.form_Name + ".submit();\">" +
//        "<img src='"+ image_FileName + "' " + image_Options + " >" +
//        "</a>");
        
        //        table_Tag.do_Display_HTML_TD_Element("<input type='image'" +
        //        " name='" + param_Name + "'" +
        //        " value='" + param_Value + "'" +
        //        " src='" + image_FileName + "'" +
        //        " ></input>");
        
        String command = "<input type='image'" +
        " name='" + param_Name + "'" +
        " value='" + param_Value + "'";
        
        if(width!=0 || height!=0 || !background_Color.equals("")){
            command += " style='";
            if(width!=0)
                command += "width: " + width + "px;";
            if(height!=0)
                command += " height: " + height + "px;";
            if(!background_Color.equals(""))
                command += " background-color: " + background_Color + ";";
            command += "'";
        }
        
        command += " src='"+ image_FileName + "'";
        if(width!=0)
            command += " width='" + width + "'";
        if(height!=0)
            command += " height='" + height + "'";
        command += "  >";

            
        out.println(command);
        count_Of_Submit_Buttons++;
    }
    
    
    
    
    public void do_Display_HTML_In_Form_Confirm_Submit_Button_In_Table(String param_Name, String param_Value, String text, HTML_Table_Tag table_Tag){
        count_Of_Submit_Buttons++;
        String javascript_Function_Name = this.form_Name + "_Confirm_" + count_Of_Submit_Buttons;
        table_Tag.do_Display_HTML_TD_Element("<input type=button    value='" + param_Value + "'  onclick=\"" + javascript_Function_Name + "();\"></input>");
        
        out.println("<script type='text/javascript'>");
        out.println("function " + javascript_Function_Name + "(){");
        
        out.println("       var answer = confirm(\"Ok to " + param_Value + "?\");");
        out.println("       if(answer) {");
        
        out.println("            var " + table_Tag.getTable_Name() + " = document.getElementById(\"" + table_Tag.getTable_Name() + "\");");
        out.println("            var linkElement = document.createElement(\"input\");");
        out.println("            linkElement.setAttribute(\"type\", \"hidden\");");
        out.println("            linkElement.setAttribute(\"name\", \"" + param_Name + "\");");
        out.println("            linkElement.setAttribute(\"value\", \"" + param_Value + "\");");
        out.println("           " + table_Tag.getTable_Name() + ".appendChild(linkElement);");
        out.println("            document." + form_Name + ".submit();");
        out.println("       }");
        
        out.println("}");
        out.println("</script>");
        
    }
    
    
    
    
    public void do_Display_HTML_Hidden_Input(String param_Name, String param_Value){
        out.println("<input type=hidden id='" + param_Name +"' name='" + param_Name + "'  value='" + param_Value + "'></input>");
    }
    
    /**
     * Getter for property form_Name.
     * @return Value of property form_Name.
     */
    public java.lang.String getForm_Name() {
        return form_Name;
    }
    
    /**
     * Setter for property form_Name.
     * @param form_Name New value of property form_Name.
     */
    public void setForm_Name(java.lang.String form_Name) {
        this.form_Name = form_Name;
    }
    
    /**
     * Getter for property action.
     * @return Value of property action.
     */
    public java.lang.String getAction() {
        return action;
    }
    
    /**
     * Setter for property action.
     * @param action New value of property action.
     */
    public void setAction(java.lang.String action) {
        this.action = action;
    }
    
    /**
     * Getter for property count_Of_Submit_Buttons.
     * @return Value of property count_Of_Submit_Buttons.
     */
    public int getCount_Of_Submit_Buttons() {
        return count_Of_Submit_Buttons;
    }
    
    /**
     * Setter for property count_Of_Submit_Buttons.
     * @param count_Of_Submit_Buttons New value of property count_Of_Submit_Buttons.
     */
    public void setCount_Of_Submit_Buttons(int count_Of_Submit_Buttons) {
        this.count_Of_Submit_Buttons = count_Of_Submit_Buttons;
    }
    
    /**
     * Getter for property count_Of_Reset_Buttons.
     * @return Value of property count_Of_Reset_Buttons.
     */
    public int getCount_Of_Reset_Buttons() {
        return count_Of_Reset_Buttons;
    }
    
    /**
     * Setter for property count_Of_Reset_Buttons.
     * @param count_Of_Reset_Buttons New value of property count_Of_Reset_Buttons.
     */
    public void setCount_Of_Reset_Buttons(int count_Of_Reset_Buttons) {
        this.count_Of_Reset_Buttons = count_Of_Reset_Buttons;
    }
    
    /**
     * Getter for property multi_Part.
     * @return Value of property multi_Part.
     */
    public boolean isMulti_Part() {
        return multi_Part;
    }
    
    /**
     * Setter for property multi_Part.
     * @param multi_Part New value of property multi_Part.
     */
    public void setMulti_Part(boolean multi_Part) {
        this.multi_Part = multi_Part;
    }
    
}
