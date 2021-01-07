/*
 * Search_Value_Manager.java
 *
 * Created on Tue Feb 28 12:23:52 CST 2006
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


package com.amlibtech.web.util;

import com.amlibtech.web.data.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Search_Value_Manager {
    ArrayList   search_Value_Nodes;

    

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

    /** Creates a new instance of Search_Value_Manager */
    public Search_Value_Manager() {
        search_Value_Nodes = new ArrayList();
    }
    
    public void add_Search_Field(Field_Description_Node field_Description_Node, Object value){
        search_Value_Nodes.add(new Search_Value_Node(field_Description_Node, value));
    }
     
    public void servlet_Request_Proccesor(HttpServletRequest request){
        
        HttpSession session = request.getSession();
        Search_Value_Node search_Value_Node=null;
        String parameter_Name;
        String parameter_Value;
        Field_Description_Node field_Description_Node;
        String message="";
                
        
        for(int i=0;i<search_Value_Nodes.size();i++){
            search_Value_Node =(Search_Value_Node) search_Value_Nodes.get(i); 
            parameter_Name = search_Value_Node.getField_Name();
                 
            parameter_Value = request.getParameter(parameter_Name);
            if(parameter_Value!=null && parameter_Value.length()!=0){
                search_Value_Node.setValue(parameter_Value);
            }else {
                search_Value_Node.setValue("");

            }
        }    

    }
    
    public String getWhere_SQL_Clause(){
        StringBuffer where_SQL_Clause = new StringBuffer();
        Search_Value_Node search_Value_Node=null;
        
        for(int i=0;i<search_Value_Nodes.size();i++){
            search_Value_Node =(Search_Value_Node) search_Value_Nodes.get(i); 
            String clause = search_Value_Node.getWhere_SQL_Clause();
            if(clause.length() !=0){
                if(where_SQL_Clause.length() !=0){
                    where_SQL_Clause.append(" AND ");
                }
                where_SQL_Clause.append(clause);
            }
        }
        return where_SQL_Clause.toString();
    }
    
    public Search_Value_Node get_By_Field_Name(String field_Name){
        Search_Value_Node search_Value_Node=null;
        for(int i=0;i<search_Value_Nodes.size();i++){
            search_Value_Node =(Search_Value_Node) search_Value_Nodes.get(i); 
            if(field_Name.equals(search_Value_Node.getField_Name()))
	    	return search_Value_Node;
	}
	return new Search_Value_Node(new Field_Description_Node("String", "unknown", "Unknown"), "unknown");
    }

    /*
    public String getSort_Order_Description(Field_Description_List field_Description_List){
        StringBuffer    sort_Order_Description = new StringBuffer();
        Search_Value_Node search_Value_Node= null;
        
        
        for(int i=0;i<search_Value_Nodes.size();i++){
            if(i==0){
                sort_Order_Description.append("Sort Order :");
            }else{
                sort_Order_Description.append(", ");
            }
            search_Value_Node =(Search_Value_Node) search_Value_Nodes.get(i);
            sort_Order_Description.append(field_Description_List.getField_Title(search_Value_Node.getField_Name()));
        }
        return sort_Order_Description.toString();
    }
    
   
    public String getHref_WebPage(String website_Base_Name, String servlet_Name, String to_Do, String webPage_Info_Node_Id, String field_Name){
        StringBuffer href= new StringBuffer();
        
        href.append("<a href=\"" + website_Base_Name + "/" + servlet_Name + "?to_Do=" + to_Do + "&webPage_Info_Node_Id=" + webPage_Info_Node_Id + "&sort_Field_Name=" + field_Name + "\">\n");
        
        href.append("   <img src=\"" + website_Base_Name + this.getField_Direction_Image(field_Name) + "\" border='0' align=\"right\">\n");
        
        href.append("   </a>");
        
        return(href.toString());
        
    }
     **/
}
