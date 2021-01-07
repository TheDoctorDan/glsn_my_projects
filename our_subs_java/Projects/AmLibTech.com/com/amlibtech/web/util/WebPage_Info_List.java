/*
 * WebPage_Info_List.java
 *
 * Created on June 15, 2005, 3:17 PM
 */

package com.amlibtech.web.util;

import java.util.*;
import javax.servlet.http.*;
import com.amlibtech.web.navigation.*;
import com.amlibtech.web.servlet_exceptions.*;


/**
 *
 * @author  dgleeson
 */
public class WebPage_Info_List {
    private LinkedList  linkedList;

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


    /** Creates a new instance of WebPage_Info_List */
    private WebPage_Info_List() {
        this.linkedList = new LinkedList();
    }


    public static WebPage_Info_List get_Session_Instance(HttpSession session){
        WebPage_Info_List webPage_Info_List = (WebPage_Info_List) session.getAttribute("s_WebPage_Info_List");
        if(webPage_Info_List ==null)
            webPage_Info_List=new WebPage_Info_List();
        session.setAttribute("s_WebPage_Info_List", webPage_Info_List);
        return webPage_Info_List;
    }


    public void set_Session_Instance(HttpSession session){
        session.setAttribute("s_WebPage_Info_List", this);
    }




    public static WebPage_Info_List get_Session_History_Instance(HttpSession session){
        WebPage_Info_List webPage_Info_List = (WebPage_Info_List) session.getAttribute("s_WebPage_Info_History_List");
        if(webPage_Info_List ==null)
            webPage_Info_List=new WebPage_Info_List();
        session.setAttribute("s_WebPage_Info_History_List", webPage_Info_List);
        return webPage_Info_List;
    }

    public void set_Session_History_Instance(HttpSession session){
        session.setAttribute("s_WebPage_Info_History_List", this);
    }




    public void setLinkedList(LinkedList linkedList){
        this.linkedList = linkedList;
    }

    public LinkedList getLinkedList(){ return linkedList; }




    public void add(WebPage_Info_Node webPage_Info_Node){
        this.linkedList.add(webPage_Info_Node);
    }

    public boolean remove_Node(WebPage_Info_Node webPage_Info_Node){
        return this.linkedList.remove(webPage_Info_Node);
    }

    public WebPage_Info_Node remove_Last_Node(){
        return (WebPage_Info_Node) this.linkedList.removeLast();
    }

    public void restart(WebPage_Info_Node webPage_Info_Node){
        this.linkedList = new LinkedList();
        this.linkedList.add(webPage_Info_Node);
    }




    public void dispatch(HttpServletResponse response, HttpSession session) throws NoSuchElementException, java.io.IOException {
        WebPage_Info_Node webPage_Info_Node = (WebPage_Info_Node) this.linkedList.getLast();
        webPage_Info_Node.dispatch(response, session);
    }

    public void reload_Dispatch(HttpServletResponse response, HttpSession session) throws NoSuchElementException, java.io.IOException {
        WebPage_Info_Node webPage_Info_Node = (WebPage_Info_Node) this.linkedList.getLast();
        webPage_Info_Node.reload_Dispatch(response, session);
    }

    public WebPage_Info_Node getLast() throws NoSuchElementException {
        WebPage_Info_Node webPage_Info_Node = (WebPage_Info_Node) this.linkedList.getLast();
        return webPage_Info_Node;
    }





    public String to_Html(String my_Selected_Tbl_Text_Color, String my_Selected_Tbl_Bg_Color, HttpServletRequest request, HttpSession session){
        StringBuffer result = new StringBuffer();
        String my_Page=request.getRequestURI();

        WebPage_Info_Node webPage_Info_Node = null;

        result.append("<br>\n");
        result.append("<table border=\"1\" align=\"left\" cellspacing=\"0\" bordercolor=\"" + my_Selected_Tbl_Text_Color + "\" bgcolor=\"" + my_Selected_Tbl_Bg_Color + "\">\n");
        result.append("<tbody>\n");
        result.append("<tr>\n");

        boolean first = true;
        boolean done=false;

        for(int i = 0; i< this.linkedList.size() && !done; i++){
            webPage_Info_Node = (WebPage_Info_Node) this.linkedList.get(i);
            if(!first)
                result.append("<th> > </th>");

            if(my_Page.endsWith(webPage_Info_Node.getDestination_URL())){

                result.append("<th>\n");
                result.append(webPage_Info_Node.getLabel());
                result.append("</th>\n");
                done=true;
            }else{
                result.append("<th>\n");
                if(webPage_Info_Node.getReload_URL()==null || webPage_Info_Node.getReload_URL().equals("")){
                    result.append("<a href=" + Nav_Base.get_Session_Instance(session).getBase_URL() + webPage_Info_Node.getDestination_URL() + ">");
                }else{
                    result.append("<a href=" + Nav_Base.get_Session_Instance(session).getBase_URL() + webPage_Info_Node.getReload_URL() + ">");
                }
                result.append(webPage_Info_Node.getLabel());
                result.append("</a>\n");
                result.append("</th>\n");
            }

            first=false;
        }


        result.append("</tr>\n");
        result.append("</tbody>\n");
        result.append("</table>\n");
        result.append("<br>\n");

        return result.toString();

    }





        public WebPage_Info_Node getWebPage_Info_Node_By_Id(String id) throws Parameter_Validation_Exception{
            WebPage_Info_Node webPage_Info_Node=null;
            boolean found=false;
            int size = this.linkedList.size();
            for(int i=size-1; i>=0 && !found ; i--){
                webPage_Info_Node = (WebPage_Info_Node) this.linkedList.get(i);
                if(webPage_Info_Node.getId().equals(id)){
                    found=true;
                }
            }
            if(!found) {
                String message =  "No webPage_Info_Node found to match webPage_Info_Node_Id:" +  id + ".";
                throw new Parameter_Validation_Exception(message);
            }
            return webPage_Info_Node;
        }




        public WebPage_Info_Node getWebPage_Info_Node_By_Id_And_Remove_Higher(String id) throws Parameter_Validation_Exception{
            WebPage_Info_Node webPage_Info_Node=null;

            boolean found=false;
            int size = this.linkedList.size();
            for(int i=size-1; i>=0 && !found ; i--){
                webPage_Info_Node = (WebPage_Info_Node) this.linkedList.get(i);
                if(webPage_Info_Node.getId().equals(id)){
                    found=true;
                }else{
                    this.linkedList.remove(i);
                }
            }
            if(!found) {
                String message =  "No webPage_Info_Node found to match webPage_Info_Node_Id:" +  id + ".";
                throw new Parameter_Validation_Exception(message);
            }
            return webPage_Info_Node;
        }



       public WebPage_Info_Node getWebPage_Info_Node_By_Servlet_And_What_Am_I_Doing(String servlet_Path_Name, String what_Am_I_Doing) throws Parameter_Validation_Exception{
            WebPage_Info_Node webPage_Info_Node=null;
            boolean found=false;
            int size = this.linkedList.size();
            for(int i=size-1; i>=0 && !found ; i--){
                webPage_Info_Node = (WebPage_Info_Node) this.linkedList.get(i);
                if(webPage_Info_Node.getWhat_Am_I_Doing().equals(what_Am_I_Doing) &&
                   webPage_Info_Node.getServlet_Path_Name().equals(servlet_Path_Name)){
                    found=true;
                }
            }
            if(!found) {
                String message =  "No webPage_Info_Node found to match Servlet :" + servlet_Path_Name + ":  and What_Am_I_Doing :" +  what_Am_I_Doing + ":.";
                throw new Parameter_Validation_Exception(message);
            }
            return webPage_Info_Node;
        }





    public String toString(){
        StringBuffer result = new StringBuffer();

        WebPage_Info_Node webPage_Info_Node = null;


        boolean first = true;

        for(int i = 0; i< this.linkedList.size(); i++){
            webPage_Info_Node = (WebPage_Info_Node) this.linkedList.get(i);
            if(!first)
                result.append(", ");

            result.append(webPage_Info_Node.getLabel());

            first=false;
        }

        return result.toString();

    }



}
