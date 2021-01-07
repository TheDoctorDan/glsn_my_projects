/*
 * Web_Process_Table.java
 *
 * Created on April 30, 2006
 */

package com.amlibtech.web_Processes;

import com.amlibtech.web.navigation.*;
import java.util.*;
import javax.servlet.http.*;
import com.amlibtech.web.servlet_exceptions.*;


/**
 *
 * @author  dgleeson
 */
public class Web_Process_Table {

    private static LinkedList linkedList = null;
    
    private static final String[] Copyright = {
        "|       Copyright (c) 1985 thru 2001, 2002, 2003, 2004, 2005, 2006, 2007       |",
        "|       American Liberator Technologies                                        |",
        "|       All Rights Reserved                                                    |",
        "|                                                                              |",
        "|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |",
        "|       American Liberator Technologies                                        |",
        "|       The copyright notice above does not evidence any                       |",
        "|       actual or intended publication of such source code.                    |"
    };
    
    /** Creates a new instance of WebPage_Info_List */
    private Web_Process_Table() {
        if(Web_Process_Table.linkedList == null){
            this.linkedList = new LinkedList();
        }
    }
    
    
    public static synchronized Web_Process_Table get_Instance() {
        Web_Process_Table web_Process_Table = new Web_Process_Table();
        return web_Process_Table;
    }
    
    
    
    public LinkedList getLinkedList() { return linkedList; }
    
    
    
    
    public static synchronized void add(Web_Process web_Process) {
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        web_Process_Table.linkedList.add(web_Process);
    }
    
    public static synchronized boolean remove_Node(Web_Process web_Process) {
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        return web_Process_Table.linkedList.remove(web_Process);
    }
    
    
    public Web_Process getLast() throws NoSuchElementException {
        Web_Process web_Process = (Web_Process) this.linkedList.getLast();
        return web_Process;
    }
    
    
    public static boolean id_In_Use(long id) {
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        Web_Process web_Process;
        
        for(int i=0;i<web_Process_Table.linkedList.size();i++) {
            web_Process = (Web_Process) web_Process_Table.linkedList.get(i);
            if(web_Process.getId() == id) {
                return true;
            }
        }
        return false;
    }
    
    public static void clean() {
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        Web_Process web_Process;
        
        for(int i=web_Process_Table.linkedList.size()-1;i>=0;i--) {
            web_Process = (Web_Process) web_Process_Table.linkedList.get(i);
            //web_Process.clean_If_Invalid();
        }
        System.gc();
    }
    
    public static Web_Process find_A_Child_Process(long parent_Id) {
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        Web_Process child_Web_Process;
        
        for(int i=web_Process_Table.linkedList.size()-1;i>=0;i--) {
            child_Web_Process = (Web_Process) web_Process_Table.linkedList.get(i);
            if(child_Web_Process.getParent_Web_Process_Id()== parent_Id)
                return child_Web_Process;
        }
        return null;
    }
    
    public static String to_Html(String my_Selected_Tbl_Text_Color, String my_Selected_Tbl_Bg_Color, HttpServletRequest request, HttpSession session) {
        StringBuffer result = new StringBuffer();
        String my_Page=request.getRequestURI();
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        
        Web_Process web_Process = null;
        
        result.append("<br>\n");
        result.append("<table border=\"1\" align=\"left\" cellspacing=\"0\" bordercolor=\"" + my_Selected_Tbl_Text_Color + "\" bgcolor=\"" + my_Selected_Tbl_Bg_Color + "\">\n");
        result.append("<thead>\n");
        result.append(Web_Process.get_Process_Table_HTML_Table_Heading());
        
        result.append("</thead>\n");
        
        
        result.append("<tbody>\n");
        
        TimeZone tz = TimeZone.getDefault();
        
        for(int i = 0; i< web_Process_Table.linkedList.size(); i++) {
            web_Process = (Web_Process) web_Process_Table.linkedList.get(i);
            result.append("<tr>\n");
            web_Process.get_Process_Table_HTML_Table_Row(tz);
            result.append("</tr>\n");
            
        }
        
        
        result.append("</tbody>\n");
        result.append("</table>\n");
        result.append("<br>\n");
        
        return result.toString();
        
    }
    
    
    
    
    
    public static Web_Process getWeb_Process_By_Id(long id) throws WPException {
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        Web_Process web_Process;
        for(int i=0;i<web_Process_Table.linkedList.size();i++) {
            web_Process = (Web_Process) web_Process_Table.linkedList.get(i);
            if(web_Process.getId() == id) {
                return web_Process;
            }
        }
        throw new WPException("No web_Process found to match web_Process_Id:" +  id + ".");
    }
    
    public static int get_Number_Of_Processes() {
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        return web_Process_Table.linkedList.size();
    }
    
}
