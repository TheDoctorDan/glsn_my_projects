/*
 * Bread_Crumb_List.java
 *
 * Created on December 18, 2004, 6:28 PM
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
public class Bread_Crumb_List {
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

    /** Creates a new instance of Bread_Crumb_List */
    private Bread_Crumb_List() {
        this.linkedList = new LinkedList();
    }

    public static Bread_Crumb_List get_Session_Instance(HttpSession session){
        Bread_Crumb_List bread_Crumb_List = (Bread_Crumb_List) session.getAttribute("s_Bread_Crumb_List");
        if(bread_Crumb_List ==null)
            bread_Crumb_List=new Bread_Crumb_List();
        session.setAttribute("s_Bread_Crumb_List", bread_Crumb_List);
        return bread_Crumb_List;
    }

    public void set_Session_Instance(HttpSession session){
        session.setAttribute("s_Bread_Crumb_List", this);
    }


    public static Bread_Crumb_List get_Session_History_Instance(HttpSession session){
        Bread_Crumb_List bread_Crumb_List = (Bread_Crumb_List) session.getAttribute("s_Bread_Crumb_History_List");
        if(bread_Crumb_List ==null){
            bread_Crumb_List=new Bread_Crumb_List();
            session.setAttribute("s_Bread_Crumb_History_List", bread_Crumb_List);
        }
	return bread_Crumb_List;
    }

    public void set_Session_History_Instance(HttpSession session){
        session.setAttribute("s_Bread_Crumb_History_List", this);
    }

    public void setLinkedList(LinkedList linkedList){
        this.linkedList = linkedList;
    }

    public LinkedList getLinkedList(){ return linkedList; }




    public void add(Bread_Crumb_Node bread_Crumb_Node){
        this.linkedList.add(bread_Crumb_Node);
    }

    public boolean remove_Node(Bread_Crumb_Node bread_Crumb_Node){
        return this.linkedList.remove(bread_Crumb_Node);
    }

    public Bread_Crumb_Node remove_Last_Node(){
        return (Bread_Crumb_Node) this.linkedList.removeLast();
    }

    public void restart(Bread_Crumb_Node bread_Crumb_Node){
        this.linkedList = new LinkedList();
        this.linkedList.add(bread_Crumb_Node);
    }

    public void dispatch(HttpServletResponse response, HttpSession session) throws NoSuchElementException, java.io.IOException {
        Bread_Crumb_Node bread_Crumb_Node = (Bread_Crumb_Node) this.linkedList.getLast();
        bread_Crumb_Node.dispatch(response, session);
    }

    public void reload_Dispatch(HttpServletResponse response, HttpSession session) throws NoSuchElementException, java.io.IOException {
        Bread_Crumb_Node bread_Crumb_Node = (Bread_Crumb_Node) this.linkedList.getLast();
        bread_Crumb_Node.reload_Dispatch(response, session);
    }

    public Bread_Crumb_Node getLast() throws NoSuchElementException {
        Bread_Crumb_Node bread_Crumb_Node = (Bread_Crumb_Node) this.linkedList.getLast();
        return bread_Crumb_Node;
    }

    public Bread_Crumb_Node get_My_Page_Bread_Crumb_Node(HttpServletRequest request) throws NoSuchElementException {
        String my_Page=request.getRequestURI();
        Bread_Crumb_Node bread_Crumb_Node = null;

        for(int i = this.linkedList.size()-1; i>=0; i--){
            bread_Crumb_Node = (Bread_Crumb_Node) this.linkedList.get(i);
            if(my_Page.endsWith(bread_Crumb_Node.getDestination_URL())){
                return bread_Crumb_Node;
            }
        }

        throw new NoSuchElementException("No Bread Crumb Node found for this page.");
    }

    public String to_Html(String my_Selected_Tbl_Text_Color, String my_Selected_Tbl_Bg_Color, HttpServletRequest request, HttpSession session){
        StringBuffer result = new StringBuffer();
        String my_Page=request.getRequestURI();

        Bread_Crumb_Node bread_Crumb_Node = null;

        result.append("<br>\n");
        result.append("<table border=\"1\" align=\"left\" cellspacing=\"0\" bordercolor=\"" + my_Selected_Tbl_Text_Color + "\" bgcolor=\"" + my_Selected_Tbl_Bg_Color + "\">\n");
        result.append("<tbody>\n");
        result.append("<tr>\n");

        boolean first = true;
        boolean done=false;

        for(int i = 0; i< this.linkedList.size() && !done; i++){
            bread_Crumb_Node = (Bread_Crumb_Node) this.linkedList.get(i);
            if(!first)
                result.append("<th> > </th>");

            if(my_Page.endsWith(bread_Crumb_Node.getDestination_URL())){

                result.append("<th>\n");
                result.append(bread_Crumb_Node.getLabel());
                result.append("</th>\n");
                done=true;
            }else{
                result.append("<th>\n");
                if(bread_Crumb_Node.getReload_URL()==null || bread_Crumb_Node.getReload_URL().equals("")){
                    result.append("<a href=" + Nav_Base.get_Session_Instance(session).getBase_URL() + bread_Crumb_Node.getDestination_URL() + ">");
                }else{
                    result.append("<a href=" + Nav_Base.get_Session_Instance(session).getBase_URL() + bread_Crumb_Node.getReload_URL() + ">");
                }
                result.append(bread_Crumb_Node.getLabel());
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


        public Bread_Crumb_Node getBread_Crumb_Node_By_Id(String id) throws Parameter_Validation_Exception{
            Bread_Crumb_Node bread_Crumb_Node=null;
            boolean found=false;
            int size = this.linkedList.size();
            for(int i=size-1; i>=0 && !found ; i--){
                bread_Crumb_Node = (Bread_Crumb_Node) this.linkedList.get(i);
                if(bread_Crumb_Node.getId().equals(id)){
                    found=true;
                }
            }
            if(!found) {
                String message =  "No bread_Crumb_Node found to match bread_Crumb_Node_Id:" +  id + ".";
                throw new Parameter_Validation_Exception(message);
            }
            return bread_Crumb_Node;
        }

        public Bread_Crumb_Node getBread_Crumb_Node_By_Id_And_Remove_Higher(String id) throws Parameter_Validation_Exception{
            Bread_Crumb_Node bread_Crumb_Node=null;

            boolean found=false;
            int size = this.linkedList.size();
            for(int i=size-1; i>=0 && !found ; i--){
                bread_Crumb_Node = (Bread_Crumb_Node) this.linkedList.get(i);
                if(bread_Crumb_Node.getId().equals(id)){
                    found=true;
                }else{
                    this.linkedList.remove(i);
                }
            }
            if(!found) {
                String message =  "No bread_Crumb_Node found to match bread_Crumb_Node_Id:" +  id + ".";
                throw new Parameter_Validation_Exception(message);
            }
            return bread_Crumb_Node;
        }

       public Bread_Crumb_Node getBread_Crumb_Node_By_Servlet_And_What_Am_I_Doing(String servlet_Path_Name, String what_Am_I_Doing) throws Parameter_Validation_Exception{
            Bread_Crumb_Node bread_Crumb_Node=null;
            boolean found=false;
            int size = this.linkedList.size();
            for(int i=size-1; i>=0 && !found ; i--){
                bread_Crumb_Node = (Bread_Crumb_Node) this.linkedList.get(i);
                if(bread_Crumb_Node.getWhat_Am_I_Doing().equals(what_Am_I_Doing) &&
                   bread_Crumb_Node.getServlet_Path_Name().equals(servlet_Path_Name)){
                    found=true;
                }
            }
            if(!found) {
                String message =  "No bread_Crumb_Node found to match Servlet :" + servlet_Path_Name + ":  and What_Am_I_Doing :" +  what_Am_I_Doing + ":.";
                throw new Parameter_Validation_Exception(message);
            }
            return bread_Crumb_Node;
        }


    public String toString(){
        StringBuffer result = new StringBuffer();

        Bread_Crumb_Node bread_Crumb_Node = null;


        boolean first = true;

        for(int i = 0; i< this.linkedList.size(); i++){
            bread_Crumb_Node = (Bread_Crumb_Node) this.linkedList.get(i);
            if(!first)
                result.append(", ");

            result.append(bread_Crumb_Node.getLabel());

            first=false;
        }

        return result.toString();

    }



}
