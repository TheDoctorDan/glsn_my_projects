/*
 * Table_List_Pagination_Manager.java
 *
 * Created on Febuary 19, 2006
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

import javax.servlet.http.*;


/**
 *
 * @author  dgleeson
 */
public class Table_List_Pagination_Manager {
    int current_Page;
    int items_Per_Page;
    boolean next_Page_Available;
    boolean prev_Page_Available;
    

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
    
    /** Creates a new instance of Table_List_Pagination_Manager */
    
    public Table_List_Pagination_Manager(int items_Per_Page) {
        this.current_Page = 1;
        this.items_Per_Page = items_Per_Page;
        if(this.items_Per_Page <1)
            this.items_Per_Page =1;
    }
    
    
    
    public int getCurrent_Page(){ return current_Page; }
    
    public int getSkip_Count(){
        if(current_Page==1)
            return(0);
        else{
            return((current_Page-1)*items_Per_Page);
        }
    }
    
    public void setItems_Per_Page(int items_Per_Page){
        int skip_Count = getSkip_Count();
        this.items_Per_Page = items_Per_Page;
        if(this.items_Per_Page <1)
            this.items_Per_Page =1;
        this.current_Page = skip_Count/this.items_Per_Page +1;
    }
    
    public int getItems_Per_Page(){ return items_Per_Page; }
    
    
    
    
    
    public void setNext_Page(){
        if(next_Page_Available)
            this.current_Page += 1;
    }
    
    
    public void setPrev_Page(){
        if(prev_Page_Available)
            this.current_Page -= 1;
    }
    
    public void setFirst_Page(){
        this.current_Page = 1;
    }
    
    
   
    
     
     public String to_Html_WebPage(String base_URL, String dir, String servlet, String to_Do, String webPage_Info_Node_Id, String text_Color){
        StringBuffer result = new StringBuffer();
        
        
        
        
        result.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ><tbody><tr>\n");

        if(prev_Page_Available){
            result.append("<td><a  href=\"" +  base_URL + "/");
            if(!dir.equals(""))
                    result.append(dir + "/");
            result.append(servlet + "?to_Do=" + to_Do + "&webPage_Info_Node_Id=" + webPage_Info_Node_Id + "&page=first\"> <img src=\"" + base_URL + "/buttons/page_first_pointer.png\" border='0'><a/></td>\n");


            result.append("<td><a  href=\"" + base_URL + "/");
            if(!dir.equals(""))
                    result.append(dir + "/");
            result.append(servlet + "?to_Do=" + to_Do + "&webPage_Info_Node_Id=" + webPage_Info_Node_Id + "&page=prev\"> <img src=\"" + base_URL + "/buttons/page_prev_pointer.png\" border='0'><a/></td>\n");

        }
        
        result.append("<td>" + this.getCurrent_Page() + "</td>\n");
        
        if(next_Page_Available){

            result.append("<td><a  href=\"" + base_URL + "/");
            if(!dir.equals(""))
                    result.append(dir + "/");
            result.append(servlet + "?to_Do=" + to_Do + "&webPage_Info_Node_Id=" + webPage_Info_Node_Id + "&page=next\"> <img src=\"" + base_URL + "/buttons/page_next_pointer.png\" border='0'><a/></td>\n");

/*
            result.append("<td><a  href=\"" + base_URL + "/");
            if(!dir.equals(""))
                    result.append(dir + "/");
            result.append(servlet + "?to_Do=" + to_Do + "&webPage_Info_Node_Id=" + webPage_Info_Node_Id + "&page=last\"> <img src=\"" + base_URL + "/buttons/page_last_pointer.png\" border='0'><a/></td>\n");
*/
            
        }
        
        
        
        
        result.append("<td><form method=\"post\"  action=\"" + base_URL + "/");
	if(!dir.equals(""))
		result.append(dir + "/");
	result.append(servlet + "\">\n");


        result.append("<input type=\"hidden\" name=\"to_Do\" value=\"" + to_Do + "\">\n");

        result.append("<input type=\"hidden\" name=\"webPage_Info_Node_Id\" value=\"" + webPage_Info_Node_Id + "\">\n");

        result.append("<font color=\"" + text_Color + "\"> @\n");


        
        result.append("<select name='per_page' id='per_page'>\n");
        for(int i=10; i<=40; i+=10){
            result.append("<option value='" + i +"'");
            if(i == this.getItems_Per_Page())
                result.append(" selected");
            result.append(">" + i + "</option>\n");
        }

        result.append("</select>\n");
     
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
                /*
                 }else if(page.equals("last")){
                    setLast_Page();
                 **/
                }else if(page.equals("this")){
                    ;
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
    
    /**
     * Getter for property next_Page_Available.
     * @return Value of property next_Page_Available.
     */
    public boolean isNext_Page_Available() {
        return next_Page_Available;
    }
    
    /**
     * Setter for property next_Page_Available.
     * @param next_Page_Available New value of property next_Page_Available.
     */
    public void setNext_Page_Available(boolean next_Page_Available) {
        this.next_Page_Available = next_Page_Available;
    }
    
    /**
     * Getter for property prev_Page_Available.
     * @return Value of property prev_Page_Available.
     */
    public boolean isPrev_Page_Available() {
        return prev_Page_Available;
    }
    
    /**
     * Setter for property prev_Page_Available.
     * @param prev_Page_Available New value of property prev_Page_Available.
     */
    public void setPrev_Page_Available(boolean prev_Page_Available) {
        this.prev_Page_Available = prev_Page_Available;
    }
    
}
