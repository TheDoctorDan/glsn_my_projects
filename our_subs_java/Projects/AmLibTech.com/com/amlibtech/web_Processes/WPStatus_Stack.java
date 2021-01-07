/*
 * WPStatus_Stack.java
 *
 * Created on September 24, 2006, 6:19 PM
 */

package com.amlibtech.web_Processes;

import java.io.*;
import java.util.*;

/**
 *
 * @author  dgleeson
 */
public class WPStatus_Stack {
    
    private LinkedList  linkedList=null;
    
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
    
    /** Creates a new instance of WPStatus_Stack */
    public WPStatus_Stack() {
        this.linkedList = new LinkedList();
    }
    
    
    
    
    //public LinkedList getLinkedList(){ return linkedList; }
    
    
    
    public void resetStatus_Stack(WPStatus_Node wPStatus_Node){
        this.linkedList = new LinkedList();
        this.linkedList.add(wPStatus_Node);
    }
    
    public WPStatus_Node getStatus_Node() {
        return (WPStatus_Node) linkedList.getLast();
    }
    
    
    public void pushStatus_Node(WPStatus_Node wPStatus_Node) {
        WPStatus_Node current_Status = (WPStatus_Node)this.linkedList.getLast();
        if(!wPStatus_Node.getStatus_Text().equals(current_Status.getStatus_Text()))
            this.linkedList.add(wPStatus_Node);
        else {
            this.linkedList.removeLast();
            this.linkedList.add(wPStatus_Node);
        }
    }
    
    public WPStatus_Node popStatus_Node(){
        WPStatus_Node result= (WPStatus_Node)this.linkedList.getLast();
        this.linkedList.removeLast();
        return result;
    }
    
    protected void curStatus_Clear_Action_Stack(){
        this.getStatus_Node().clear_Action_Stack();
    }
    
    protected void curStatus_Add_Action_Node(WPAction_Node wpAction_Node)
    throws WPFatal_Exception {
        this.getStatus_Node().add_Action_Node(wpAction_Node);
    }
    
    protected void curStatus_Find_And_Invoke_Action(Web_Process web_Process, String action_Text)
    throws WPException {
        this.getStatus_Node().find_And_Invoke_Action(web_Process, action_Text);
    }
    
    
    
    public String curStatus_GetBreadCrumb_Text() {
        WPStatus_Node result= (WPStatus_Node)this.linkedList.getLast();
        
        return result.getBreadCrumb_Text();
    }
    
    
    public void curStatus_SetBreadCrumb_Text(String breadCrumb_Text) {
        WPStatus_Node result= (WPStatus_Node)this.linkedList.getLast();
        
        result.setBreadCrumb_Text(breadCrumb_Text);
    }
    
    public String[] getBreadCrumbs_Text(){
        String[] breadCrumbs_Text = new String[this.linkedList.size()];
        for(int i=0;i <this.linkedList.size(); i++){
            WPStatus_Node wpStatus_Node = (WPStatus_Node) this.linkedList.get(i);
            breadCrumbs_Text[i] = wpStatus_Node.getBreadCrumb_Text();
        }
        return breadCrumbs_Text;
    }
    
    public String[] getStatuses_Text(){
        String[] Statuses_Text = new String[this.linkedList.size()];
        for(int i=0;i <this.linkedList.size(); i++){
            WPStatus_Node wpStatus_Node = (WPStatus_Node) this.linkedList.get(i);
            Statuses_Text[i] = wpStatus_Node.getStatus_Text();
        }
        return Statuses_Text;
    }
    
    
    
    public String print(){
        String result = "WPStatus_Stack : {\n";
        
        if(linkedList==null){
            result += "linkedList: null\n";
        }else{
            result += "linkedList:  size="+ linkedList.size() + "\n";
            for(int i=0; i<linkedList.size(); i++){
                WPStatus_Node wpStatus_Node = (WPStatus_Node) linkedList.get(i);
                result += "Item #: " +i +"  ";
                result += wpStatus_Node.print();
            }
        }
        
        result += "}\n";
        
        return result;
    }
    
    public void print(PrintWriter out){
        out.print(print());
    }
    
    
}
