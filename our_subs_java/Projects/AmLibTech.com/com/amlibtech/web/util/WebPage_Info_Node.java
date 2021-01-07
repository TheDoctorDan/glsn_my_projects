/*
 * WebPage_Info_Node.java
 *
 * Created on Jun 15, 2005, 3:17 PM
 */

package com.amlibtech.web.util;

import java.util.*;
import javax.servlet.http.*;
import com.amlibtech.web.navigation.*;

/**
 *
 * @author  dgleeson
 */
public class WebPage_Info_Node {
    private static int id_Counter=1;
    
    
    private String id;

    private String  label;
    private String  destination_URL;
    
    private String  reload_URL;
    
    private String what_Am_I_Doing;
    private Date date;

    private String preset_Where_Clause;
    
    //private String called_from;
    private String servlet_Path_Name;

    private Display_List_Sort_Manager display_List_Sort_Manager;
    private Display_List_Pagination_Manager display_List_Pagination_Manager;
    private Table_List_Pagination_Manager   table_List_Pagination_Manager;
    private Search_Value_Manager    search_Value_Manager;
    
    private Object[] data_Object_Records;
    private Object[] data_VO_Object_Records;
    private Object data_Object;
    private Object data_VO_Object;
    
    private Stack   arg_Stack;

    private Object parent_Data_Object;
    private Object parent_Data_VO_Object;

    private Object third_File_Data_Object;
    
    private WebPage_Info_Node calling_WePage_Info_Node;


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
    
    
    /** Creates a new instance of WebPage_Info_Node */

	public WebPage_Info_Node(String what_Am_I_Doing, String servlet_Path_Name) {
                int id_num = WebPage_Info_Node.next_Id_Counter();
		this.id = String.valueOf(id_num);
		this.what_Am_I_Doing = what_Am_I_Doing;
		this.date = Calendar.getInstance().getTime();
                this.servlet_Path_Name = servlet_Path_Name;
                this.preset_Where_Clause="";
                this.calling_WePage_Info_Node=null;
	}


	public void setLabel(String label){
		this.label = label;
	}

	public String getLabel(){ return label; }


	public void setDestination_URL(String destination_URL){
		this.destination_URL = destination_URL;
	}

	public String getDestination_URL(){ return destination_URL; }
        
        

	public void setReload_URL(String reload_URL){
		this.reload_URL = reload_URL;
	}

	public String getReload_URL(){ return reload_URL; }



	public void setId(String id){
		this.id = id;
	}

	public String getId(){ return id; }



	public void setWhat_Am_I_Doing(String what_Am_I_Doing){
		this.what_Am_I_Doing = what_Am_I_Doing;
	}

	public String getWhat_Am_I_Doing(){ return what_Am_I_Doing; }



	public void setDate(Date date){
		this.date = date;
	}

	public Date getDate(){ return date; }



        

	public void setServlet_Path_Name(String servlet_Path_Name){
		this.servlet_Path_Name = servlet_Path_Name;
	}

	public String getServlet_Path_Name(){ return servlet_Path_Name; }

        

	public void setDisplay_List_Sort_Manager(Display_List_Sort_Manager display_List_Sort_Manager){
		this.display_List_Sort_Manager = display_List_Sort_Manager;
	}

	public Display_List_Sort_Manager getDisplay_List_Sort_Manager(){ return display_List_Sort_Manager; }



	public void setDisplay_List_Pagination_Manager(Display_List_Pagination_Manager display_List_Pagination_Manager){
		this.display_List_Pagination_Manager = display_List_Pagination_Manager;
	}

	public Display_List_Pagination_Manager getDisplay_List_Pagination_Manager(){ return display_List_Pagination_Manager; }



	public void setData_Object_Records(Object[] data_Object_Records){
		this.data_Object_Records = data_Object_Records;
	}

	public Object[] getData_Object_Records(){ return data_Object_Records; }


	public void setData_VO_Object_Records(Object[] data_VO_Object_Records){
		this.data_VO_Object_Records = data_VO_Object_Records;
	}

	public Object[] getData_VO_Object_Records(){ return data_VO_Object_Records; }



	public void setData_Object(Object data_Object){
		this.data_Object = data_Object;
	}

	public Object getData_Object(){ return data_Object; }



	public void setData_VO_Object(Object data_VO_Object){
		this.data_VO_Object = data_VO_Object;
	}

	public Object getData_VO_Object(){ return data_VO_Object; }



	public void setArg_Stack(Stack arg_Stack){
		this.arg_Stack = arg_Stack;
	}

	public Stack getArg_Stack(){ return arg_Stack; }


        
	public void setParent_Data_Object(Object parent_Data_Object){
		this.parent_Data_Object = parent_Data_Object;
	}

	public Object getParent_Data_Object(){ return parent_Data_Object; }



	public void setParent_Data_VO_Object(Object parent_Data_VO_Object){
		this.parent_Data_VO_Object = parent_Data_VO_Object;
	}

	public Object getParent_Data_VO_Object(){ return parent_Data_VO_Object; }


        

	public void setThird_File_Data_Object(Object third_File_Data_Object){
		this.third_File_Data_Object = third_File_Data_Object;
	}

	public Object getThird_File_Data_Object(){ return third_File_Data_Object; }

        
          

	public void setCalling_WebPage_Info_Node(WebPage_Info_Node calling_WebPage_Info_Node){
		this.calling_WePage_Info_Node = calling_WebPage_Info_Node;
	}

	public WebPage_Info_Node getCalling_WebPage_Info_Node(){ return this.calling_WePage_Info_Node; }

        
        
        
        
    private static synchronized int next_Id_Counter(){
	    id_Counter++;	
	    return id_Counter;
    }



    public void dispatch(HttpServletResponse response, HttpSession session) throws java.io.IOException {
	Nav_Base.forward(response, session, this.getDestination_URL());
    }


    public void reload_Dispatch(HttpServletResponse response, HttpSession session) throws java.io.IOException {
	if(this.getReload_URL()==null || this.getReload_URL().equals("")){
		Nav_Base.forward(response, session, this.getDestination_URL());
	}else{
		Nav_Base.forward(response, session, this.getReload_URL());
	}
    }


    public static WebPage_Info_Node get_Session_Instance(HttpSession session){
        WebPage_Info_Node webPage_Info_Node = (WebPage_Info_Node) session.getAttribute("s_WebPage_Info_Node");
        return webPage_Info_Node;
    }


    public void set_Session_Instance(HttpSession session){
        session.setAttribute("s_WebPage_Info_Node", this);
    }

    public void remove_Session_Instance(HttpSession session){
        session.removeAttribute("s_WebPage_Info_Node");
    }



    public void add_To_Session_List(HttpSession session){
	WebPage_Info_List webPage_Info_List = WebPage_Info_List.get_Session_Instance(session);
	webPage_Info_List.add(this);
	webPage_Info_List.set_Session_Instance(session);
    }

    public void remove_From_Session_List(HttpSession session){
	WebPage_Info_List webPage_Info_List = WebPage_Info_List.get_Session_Instance(session);
	webPage_Info_List.remove_Node(this);
	webPage_Info_List.set_Session_Instance(session);
    }
    
    /**
     * Getter for property table_List_Pagination_Manager.
     * @return Value of property table_List_Pagination_Manager.
     */
    public Table_List_Pagination_Manager getTable_List_Pagination_Manager() {
        return table_List_Pagination_Manager;
    }    

    /**
     * Setter for property table_List_Pagination_Manager.
     * @param table_List_Pagination_Manager New value of property table_List_Pagination_Manager.
     */
    public void setTable_List_Pagination_Manager(Table_List_Pagination_Manager table_List_Pagination_Manager) {
        this.table_List_Pagination_Manager = table_List_Pagination_Manager;
    }    

    /**
     * Getter for property preset_Where_Clause.
     * @return Value of property preset_Where_Clause.
     */
    public java.lang.String getPreset_Where_Clause() {
        return preset_Where_Clause;
    }
    
    /**
     * Setter for property preset_Where_Clause.
     * @param preset_Where_Clause New value of property preset_Where_Clause.
     */
    public void setPreset_Where_Clause(java.lang.String preset_Where_Clause) {
        this.preset_Where_Clause = preset_Where_Clause;
    }
    
    /**
     * Getter for property search_Value_Manager.
     * @return Value of property search_Value_Manager.
     */
    public com.amlibtech.web.util.Search_Value_Manager getSearch_Value_Manager() {
        return search_Value_Manager;
    }
    
    /**
     * Setter for property search_Value_Manager.
     * @param search_Value_Manager New value of property search_Value_Manager.
     */
    public void setSearch_Value_Manager(com.amlibtech.web.util.Search_Value_Manager search_Value_Manager) {
        this.search_Value_Manager = search_Value_Manager;
    }
    
}
