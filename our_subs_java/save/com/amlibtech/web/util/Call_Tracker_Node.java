/*
 * Call_Tracker.java
 *
 * Created on November 30, 2004, 1:32 PM
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
import com.amlibtech.util.*;

/**
 *
 * @author  dgleeson
 */
public class Call_Tracker_Node {
    private static int id_Counter=1;

    private String id;
    private String what_Am_I_Doing;
    private Date date;

    private String called_from;
    private String servlet;

    private Display_List_Sort_Manager display_List_Sort_Manager;
    private Display_List_Pagination_Manager display_List_Pagination_Manager;
    private Object[] data_Object_Records;
    private Object data_Object;
    private Object data_VO_Object;

    /** Creates a new instance of Call_Tracker */

	public Call_Tracker_Node(
		String what_Am_I_Doing,
		String called_from,
                String servlet,
		Display_List_Sort_Manager display_List_Sort_Manager,
		Display_List_Pagination_Manager display_List_Pagination_Manager,
		Object[] data_Object_Records,
		Object data_Object,
		Object data_VO_Object
	) {
            int id_num = Call_Tracker_Node.next_Id_Counter();

		this.id = String.valueOf(id_num);
		this.what_Am_I_Doing = what_Am_I_Doing;
		this.date = Calendar.getInstance().getTime();

		this.called_from = called_from;
                this.servlet = servlet;
                
		this.display_List_Sort_Manager = display_List_Sort_Manager;
		this.display_List_Pagination_Manager = display_List_Pagination_Manager;
		this.data_Object_Records = data_Object_Records;
		this.data_Object = data_Object;
		this.data_VO_Object = data_VO_Object;
	}

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

	public void setCalled_From(String called_from){
		this.called_from = called_from;
	}

	public String getCalled_From(){ return called_from; }
        
	public void setServlet(String servlet){
		this.servlet = servlet;
	}

	public String getServlet(){ return servlet; }

        
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

	public void setData_Object(Object data_Object){
		this.data_Object = data_Object;
	}

	public Object getData_Object(){ return data_Object; }

	public void setData_VO_Object(Object data_VO_Object){
		this.data_VO_Object = data_VO_Object;
	}

	public Object getData_VO_Object(){ return data_VO_Object; }


    private static synchronized int next_Id_Counter(){
	    id_Counter++;	
	    return id_Counter;
    }

    public String toString(){
    	String timeZone_Name = "CST6CDT";
    	return toString(timeZone_Name);
    }

    public String toString(String passed_TimeZone_Name) {
    	StringBuffer result = new StringBuffer();
	TimeZone tz = TimeZone.getTimeZone(passed_TimeZone_Name);
	result.append("id : " + id + ": \n");
	result.append("what_Am_I_Doing : " + what_Am_I_Doing + ": \n");
	result.append("date : " + Calendar_Plus.get_Civilian_English_Date_And_Time(date, tz) + ": \n");
	result.append("called_from : " + called_from + ": \n");
        result.append("servlet : " + servlet + ": \n");


	if(display_List_Sort_Manager == null)
		result.append("display_List_Sort_Manager : null: \n");
	else
		result.append("display_List_Sort_Manager : Not null: \n");


	if(display_List_Pagination_Manager == null)
		result.append("display_List_Pagination_Manager : null: \n");
	else
		result.append("display_List_Pagination_Manager : Not null: \n");

	if(data_Object_Records == null)
		result.append("data_Object_Records : null: \n");
	else
		result.append("data_Object_Records : Not null: \n");

	if(data_Object == null)
		result.append("data_Object : null: \n");
	else
		result.append("data_Object : Not null: \n");

	if(data_VO_Object == null)
		result.append("data_VO_Object : null: \n");
	else
		result.append("data_VO_Object : Not null: \n");

	return result.toString();

    }

}
