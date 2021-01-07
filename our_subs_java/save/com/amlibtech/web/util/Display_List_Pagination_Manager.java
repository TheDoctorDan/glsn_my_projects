/*
 * Display_List_Pagination_Manager.java
 *
 * Created on October 14, 2004, 3:55 PM
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

import javax.servlet.http.*;


/**
 *
 * @author  dgleeson
 */
public class Display_List_Pagination_Manager {
    int current_Page;
    int items_Per_Page;
    int items_In_Result_Set;
    
    /** Creates a new instance of Display_List_Pagination_Manager */
    
    public	Display_List_Pagination_Manager(
    int items_Per_Page,
    int items_In_Result_Set
    ) {
        this.current_Page = 1;
        this.items_Per_Page = items_Per_Page;
        if(this.items_Per_Page <1)
            this.items_Per_Page =1;
        this.items_In_Result_Set = items_In_Result_Set;
        if(this.items_In_Result_Set <0)
            this.items_In_Result_Set =0;
    }
    
    public void setCurrent_Page(int current_Page){
        this.current_Page = current_Page;
        if(this.current_Page > this.getLast_Page_Number())
            this.current_Page = this.getLast_Page_Number();
        if(this.current_Page <1)
            this.current_Page = 1;
    }
    
    public int getCurrent_Page(){ return current_Page; }
    
    public void setItems_Per_Page(int items_Per_Page){
        this.items_Per_Page = items_Per_Page;
        if(this.items_Per_Page <1)
            this.items_Per_Page =1;
        int p = this.current_Page;
        this.setCurrent_Page(p);
    }
    
    public int getItems_Per_Page(){ return items_Per_Page; }
    
    public void setItems_In_Result_Set(int items_In_Result_Set){
        this.items_In_Result_Set = items_In_Result_Set;
        if(this.items_In_Result_Set <0)
            this.items_In_Result_Set =0;
    }
    
    public int getItems_In_Result_Set(){ return items_In_Result_Set; }
    
    
    public int getLast_Page_Number(){
    	if(this.items_In_Result_Set==0){
		return 1;
	}else{
		int     last_Page = this.items_In_Result_Set / this.items_Per_Page;
		if( this.items_In_Result_Set % this.items_Per_Page !=0)
			last_Page += 1;
		return last_Page ;
	}
    }
    
    public int getFirst_Item_Number_On_Page(int page){
        if(page<1) page=1;
        int item =(page-1)* this.items_Per_Page;
        if(item<0)
            item=0;
        return item;
    }
    
    
    public int getLast_Item_Number_On_Page(int page){
        if(page<1) page=1;
        int item = (page * this.items_Per_Page)-1;
        if(item>=items_In_Result_Set)
            item=items_In_Result_Set-1;
	if(item<0)
		item=0;
        return item;
    }
    
    public int getFirst_Item_Number_On_Current_Page(){
        int item =(this.current_Page-1)* this.items_Per_Page;
        if(item<0)
            item=0;
        return item;
    }
    
    
    public int getLast_Item_Number_On_Current_Page(){
        int item = (this.current_Page * this.items_Per_Page)-1;
        if(item>=items_In_Result_Set)
            item=items_In_Result_Set-1;
	if(item<0)
		item=0;
        return item;
        
    }
    
    public void setNext_Page(){
        this.current_Page += 1;
        if(this.current_Page > this.getLast_Page_Number())
            this.current_Page = this.getLast_Page_Number();
    }
    
    
    public void setPrev_Page(){
        this.current_Page -= 1;
        if(this.current_Page <1)
            this.current_Page = 1;
    }
    
    public void setFirst_Page(){
        this.current_Page = 1;
    }
    
    
    public void setLast_Page(){
        this.current_Page = this.getLast_Page_Number();
    }
    
    public String to_Html(String base_URL, String dir, String servlet, String to_Do, String bread_Crumb_Node_Id, String text_Color){
        StringBuffer result = new StringBuffer();
        
        
        
        
        result.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ><tbody><tr>\n");
        result.append("<td><a  href=\"" +  base_URL + "/" + dir + "/" + servlet + "?to_Do=" + to_Do + "&bread_Crumb_Node_Id=" + bread_Crumb_Node_Id + "&page=first\"> <img src=\"" + base_URL + "/buttons/page_first_pointer.png\" border='0'><a/></td>\n");
        result.append("<td><a  href=\"" + base_URL + "/" + dir + "/" + servlet + "?to_Do=" + to_Do + "&bread_Crumb_Node_Id=" + bread_Crumb_Node_Id + "&page=prev\"> <img src=\"" + base_URL + "/buttons/page_prev_pointer.png\" border='0'><a/></td>\n");
        
        result.append("<td><form method=\"post\" action=\"" + base_URL + "/" + dir + "/" + servlet + "\">\n");
        result.append("<input type=\"hidden\" name=\"to_Do\" value=\"" + to_Do + "\">\n");
        result.append("<input type=\"hidden\" name=\"bread_Crumb_Node_Id\" value=\"" + bread_Crumb_Node_Id + "\">\n");
        result.append("<input type=\"text\" name=\"page\" size=2 maxlength=4 value=\"" + this.getCurrent_Page() + "\">\n");
        result.append("</form></td>\n");
        
        result.append("<td> <font color=\"" + text_Color + "\">of " + this.getLast_Page_Number() + "</font></td>\n");
        result.append("<td><a  href=\"" + base_URL + "/" + dir + "/" + servlet + "?to_Do=" + to_Do + "&bread_Crumb_Node_Id=" + bread_Crumb_Node_Id + "&page=next\"> <img src=\"" + base_URL + "/buttons/page_next_pointer.png\" border='0'><a/></td>\n");
        result.append("<td><a  href=\"" + base_URL + "/" + dir + "/" + servlet + "?to_Do=" + to_Do + "&bread_Crumb_Node_Id=" + bread_Crumb_Node_Id + "&page=last\"> <img src=\"" + base_URL + "/buttons/page_last_pointer.png\" border='0'><a/></td>\n");
        
        result.append("<td><form method=\"post\"  action=\"" + base_URL + "/" + dir + "/" + servlet + "\">\n");
        result.append("<input type=\"hidden\" name=\"to_Do\" value=\"" + to_Do + "\">\n");
        result.append("<input type=\"hidden\" name=\"bread_Crumb_Node_Id\" value=\"" + bread_Crumb_Node_Id + "\">\n");
        result.append("<font color=\"" + text_Color + "\"> @\n");
        result.append("<input type=\"text\" name=\"per_page\" size=2 maxlength=4 value=\"" + this.getItems_Per_Page() + "\"> per page.\n");
        result.append("</font></form></td>\n");
        
        result.append("</tr></tbody></table>\n");
        
        return result.toString();
        
    }
    
    public void servlet_Request_Proccesor(HttpServletRequest request){
        
        HttpSession session = request.getSession();
        
        
        String message="";
        
       
            
            String page = request.getParameter("page");
            if(page !=null){
                // validate page # or selection
                if(page.equals("first")){
                    setFirst_Page();
                }else if(page.equals("prev")){
                    setPrev_Page();
                }else if(page.equals("next")){
                    setNext_Page();
                }else if(page.equals("last")){
                    setLast_Page();
                }else{
                    try{
                        int p = Integer.parseInt(page);
                        setCurrent_Page(p);
                    }
                    catch(NumberFormatException nfe){
                        setFirst_Page();
                        message = nfe.getMessage();
                        
                    }
                }
            }
            
            String per_page = request.getParameter("per_page");
            if(per_page != null){
                // validate per_page
                try{
                    int p = Integer.parseInt(per_page);
                    setItems_Per_Page(p);
                }
                catch(NumberFormatException nfe){
                    setItems_Per_Page(3);
                    message = nfe.getMessage();
                }
            }
            
        
        
    }
}
