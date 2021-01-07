/*
 * WPAction_Stack.java
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
public class WPAction_Stack {
    
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
    
    /** Creates a new instance of WPAction_Stack */
    protected WPAction_Stack() {
        this.linkedList = new LinkedList();
    }
    
    
    
    
    protected LinkedList getLinkedList(){ return linkedList; }
    
    protected void clear_Action_Stack(){
        this.linkedList = new LinkedList();
    }
    
    protected void add_Action_Node(WPAction_Node wpAction_Node)
    throws WPFatal_Exception {
        
        
        for(int i=0; i<this.linkedList.size();i++){
            WPAction_Node working_Node = (WPAction_Node)this.linkedList.get(i);
            
            if(working_Node.getAction_Text().equals(wpAction_Node.getAction_Text())){
                if(working_Node.equals(wpAction_Node)){
                    return;
                }
                throw new WPFatal_Exception("Action :" + wpAction_Node.getAction_Text() +":  is already in Action_Stack with different action.");
            }
        }
        
        
        this.linkedList.add(wpAction_Node);
    }
    
    protected void add_Action_Node_Allow_Repeat(WPAction_Node wpAction_Node){
        for(int i=0; i<this.linkedList.size();i++){
            WPAction_Node working_Node = (WPAction_Node)this.linkedList.get(i);
            if(working_Node.getAction_Text().equals(wpAction_Node.getAction_Text())){
                return;
            }
        }
        
        this.linkedList.add(wpAction_Node);
    }
    
    
    protected void find_And_Invoke_Action(Web_Process web_Process, String action_Text)
    throws WPException {
        System.err.println("in WPAction_Stack.find_And_Invoke_Action() action_text=:"+action_Text+":");
        
        for(int i=0; i<this.linkedList.size();i++){
            WPAction_Node working_Node = (WPAction_Node)this.linkedList.get(i);
                
            if(working_Node.getAction_Text().equals(action_Text)){
                working_Node.invoke(web_Process);
                return;
            }
        }
        throw new WPException("No Such Action :"+action_Text+":.");
    }
    
    public String print(){
        String result = "WPAction_Stack : {\n";
        
        if(linkedList==null){
            result += "linkedList: null\n";
        }else{
            result += "linkedList:  size="+ linkedList.size() + "\n";
            for(int i=0; i<linkedList.size(); i++){
                WPAction_Node wpAction_Node = (WPAction_Node) linkedList.get(i);
                result += "Item #: " +i +"  ";
                result += wpAction_Node.print();
            }
        }
        
        result += "}\n";
        return result;
    }
    
    public void print(PrintWriter out){
        out.print(print());
    }
    
    
}
