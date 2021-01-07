/*
 * Display_List_Sort_Manager.java
 *
 * Created on October 8, 2004, 12:28 PM
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


package com.amlibtech.web.util;

import java.util.*;
import com.amlibtech.web.data.*;

/**
 *
 * @author  dgleeson
 */
public class Display_List_Sort_Manager {
    
    private ArrayList   display_List_Sort_Nodes;
    private String prefix;
    

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
    
    
    /** Creates a new instance of Display_List_Sort_Manager */
    public Display_List_Sort_Manager(String prefix) {
        this.display_List_Sort_Nodes = new ArrayList();
        this.prefix=prefix;
    }
    
    
    /** Creates a new instance of Display_List_Sort_Manager */
    public Display_List_Sort_Manager() {
        this("");
    }
    
    
    public String remove_Prefix_From_Field_Name(String passed_Field_Name){
        String field_Name;
        if(this.prefix.length() >0){
           if(passed_Field_Name.startsWith(prefix)){
               field_Name=passed_Field_Name.substring(prefix.length());
            } else {
               // this is an error and should throw exception
               field_Name = passed_Field_Name;
           }
        } else {
           field_Name = passed_Field_Name;
        }
        return field_Name;
    }

    /* CUD change, update, delete */
    public void sort_Field_CUD(String passed_Field_Name){
        String field_Name = remove_Prefix_From_Field_Name(passed_Field_Name);
        

        Display_List_Sort_Node display_List_Sort_Node=null;
        
        for(int i=0;i<display_List_Sort_Nodes.size();i++){
            display_List_Sort_Node =(Display_List_Sort_Node) display_List_Sort_Nodes.get(i);
            if(display_List_Sort_Node.getField_Name().equals(field_Name)){
                display_List_Sort_Node.rotateDirection();
                if(display_List_Sort_Node.getDirection() == Display_List_Sort_Node.DIRECTION_NONE){
                    display_List_Sort_Nodes.remove(i);
                }
                return;
            }
        }
        display_List_Sort_Nodes.add(new Display_List_Sort_Node(field_Name, Display_List_Sort_Node.DIRECTION_FORWARD));
    }
    
    public int getSort_Field_Direction(String passed_Field_Name){
        String field_Name = remove_Prefix_From_Field_Name(passed_Field_Name);
        
        Display_List_Sort_Node display_List_Sort_Node=null;
        
        for(int i=0;i<display_List_Sort_Nodes.size();i++){
            display_List_Sort_Node =(Display_List_Sort_Node) display_List_Sort_Nodes.get(i);
            if(display_List_Sort_Node.getField_Name().equals(field_Name)){
                return display_List_Sort_Node.getDirection();
            }
        }
        return Display_List_Sort_Node.DIRECTION_NONE;
    }
    
    public String getSort_SQL_Clause(){
        StringBuffer sort_SQL_Clause = new StringBuffer();
        Display_List_Sort_Node display_List_Sort_Node=null;
        int direction;
        
        for(int i=0;i<display_List_Sort_Nodes.size();i++){
            display_List_Sort_Node =(Display_List_Sort_Node) display_List_Sort_Nodes.get(i);

	    if(sort_SQL_Clause.length() ==0){
	        sort_SQL_Clause.append(" ORDER BY ");
	    }else{
	        sort_SQL_Clause.append(", ");
	    }

	    sort_SQL_Clause.append(display_List_Sort_Node.getField_Name());

            direction = display_List_Sort_Node.getDirection();
            if(direction == Display_List_Sort_Node.DIRECTION_FORWARD){
                sort_SQL_Clause.append(" ASC");
                
            }else if(direction == Display_List_Sort_Node.DIRECTION_BACKWARD){
                sort_SQL_Clause.append(" DESC");
                
            }
        }
        return sort_SQL_Clause.toString();
    }

    public String getSort_SQL_Clause(String table_Name){
        StringBuffer sort_SQL_Clause = new StringBuffer();
        Display_List_Sort_Node display_List_Sort_Node=null;
        int direction;
        
        for(int i=0;i<display_List_Sort_Nodes.size();i++){
            display_List_Sort_Node =(Display_List_Sort_Node) display_List_Sort_Nodes.get(i);

	    if(sort_SQL_Clause.length() ==0){
	        sort_SQL_Clause.append(" ORDER BY ");
	    }else{
	        sort_SQL_Clause.append(", ");
	    }

	    sort_SQL_Clause.append(table_Name + ".");
	    sort_SQL_Clause.append(display_List_Sort_Node.getField_Name());

            direction = display_List_Sort_Node.getDirection();
            if(direction == Display_List_Sort_Node.DIRECTION_FORWARD){
                sort_SQL_Clause.append(" ASC");
                
            }else if(direction == Display_List_Sort_Node.DIRECTION_BACKWARD){
                sort_SQL_Clause.append(" DESC");
                
            }
        }
        return sort_SQL_Clause.toString();
    }
    
    public String getField_Direction_Image(String passed_Field_Name){
        String field_Name = remove_Prefix_From_Field_Name(passed_Field_Name);
        
        Display_List_Sort_Node display_List_Sort_Node= null;
        
        for(int i=0;i<display_List_Sort_Nodes.size();i++){
            display_List_Sort_Node =(Display_List_Sort_Node) display_List_Sort_Nodes.get(i);
            if(display_List_Sort_Node.getField_Name().equals(field_Name))
                return display_List_Sort_Node.getDirection_Image();
            
        }
        display_List_Sort_Node= new Display_List_Sort_Node("", Display_List_Sort_Node.DIRECTION_NONE);
        return display_List_Sort_Node.getDirection_Image();
    }
    
    public String getSort_Order_Description(Field_Description_List field_Description_List){
        StringBuffer    sort_Order_Description = new StringBuffer();
        Display_List_Sort_Node display_List_Sort_Node= null;
        
        
        for(int i=0;i<display_List_Sort_Nodes.size();i++){
            if(i==0){
                sort_Order_Description.append("Sort Order :");
            }else{
                sort_Order_Description.append(", ");
            }
            display_List_Sort_Node =(Display_List_Sort_Node) display_List_Sort_Nodes.get(i);
            sort_Order_Description.append(field_Description_List.getField_Title(display_List_Sort_Node.getField_Name()));
        }
        return sort_Order_Description.toString();
    }
    
    public String getHref(String website_Base_Name, String servlet_Name, String to_Do, String bread_Crumb_Node_Id, String field_Name){
        StringBuffer href= new StringBuffer();
        
        href.append("<a href=\"" + website_Base_Name + "/" + servlet_Name + "?to_Do=" + to_Do + "&bread_Crumb_Node_Id=" + bread_Crumb_Node_Id + "&sort_Field_Name=" + field_Name + "\">\n");
        
        href.append("   <img src=\"" + website_Base_Name + this.getField_Direction_Image(field_Name) + "\" border='0' align=\"right\">\n");
        
        href.append("   </a>");
        
        return(href.toString());
        
    } 
    
    public String getHref_WebPage(String website_Base_Name, String servlet_Name, String to_Do, String webPage_Info_Node_Id, String field_Name){
        StringBuffer href= new StringBuffer();
        
        href.append("<a href=\"" + website_Base_Name + "/" + servlet_Name + "?to_Do=" + to_Do + "&webPage_Info_Node_Id=" + webPage_Info_Node_Id + "&sort_Field_Name=" + field_Name + "\">\n");
        
        href.append("   <img src=\"" + website_Base_Name + this.getField_Direction_Image(field_Name) + "\" border='0' align=\"right\">\n");
        
        href.append("   </a>");
        
        return(href.toString());
        
    }
    
    /**
     * Getter for property prefix.
     * @return Value of property prefix.
     */
    public java.lang.String getPrefix() {
        return prefix;
    }
    
    
    
}
