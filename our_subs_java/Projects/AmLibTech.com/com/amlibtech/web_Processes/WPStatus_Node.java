/*
 * WPStatus_Node.java
 *
 * Created on September 24, 2006, 2:01 PM
 */

package com.amlibtech.web_Processes;

import java.io.*;
import java.lang.reflect.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class WPStatus_Node {
    private int     type;
    private String  status_Text;
    //private Class   class_Obj;
    //private Method  method_Obj;
    private WPAction_Stack wpAction_Stack;
    private String  breadCrumb_Text;
    
    public static final int TYPE_NOTE =0;
    public static final int TYPE_ACTIONABLE=1;
    
    public static String[] TYPES ={
        "Note",
        "Actionable"
    };
    
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
    
    /** Creates a new instance of WPStatus_Node */
    public WPStatus_Node(String status_Text) {
        this.type=TYPE_NOTE;
        this.status_Text = status_Text;
        //this.class_Obj = null;
        //this.method_Obj = null;
        wpAction_Stack = new WPAction_Stack();
        this.breadCrumb_Text="";
    }
    
    
    /** Creates a new instance of WPStatus_Node */
    //    public WPStatus_Node(String status_Text, String class_Name, String method_Name)
    //    throws WPFatal_Exception {
    //        this.type=TYPE_ACTIONABLE;
    //        this.status_Text = status_Text;
    //        wpAction_Stack = new WPAction_Stack();
    //
    //        Class[] parameter_Types = new Class[2];
    //
    //        try {
    //            this.class_Obj = Class.forName(class_Name);
    //        }catch (ClassNotFoundException cnfe){
    //            String message ="Status :"+ status_Text +":  Error loading Class for : " + class_Name + ", ClassNotFoundException: " + cnfe.getMessage();
    //            throw new WPFatal_Exception(message);
    //        }
    //
    //
    //        try {
    //            parameter_Types[0] = Class.forName("com.amlibtech.web_Processes.Web_Process");
    //            parameter_Types[1] = Class.forName("java.lang.String");
    //        }catch (ClassNotFoundException cnfe){
    //            String message ="Status :"+ status_Text +":  Error loading Class for : " + class_Name + ", ClassNotFoundException: " + cnfe.getMessage();
    //            throw new WPFatal_Exception(message);
    //        }
    //
    //        try {
    //            this.method_Obj = class_Obj.getDeclaredMethod(method_Name, parameter_Types);
    //        }catch(NoSuchMethodException ex){
    //            String message ="Status :"+ status_Text +":  Error getting Method for : " + method_Name + ", NoSuchMethodException: " + ex.getMessage();
    //            throw new WPFatal_Exception(message);
    //        }
    //
    //    }
    
    
    
    /** Creates a new instance of WPStatus_Node */
    /*
    public WPStatus_Node(String status_Text, Class passed_Class_Obj, String method_Name)
    throws WPFatal_Exception {
        this.type=TYPE_ACTIONABLE;
        this.status_Text = status_Text;
        wpAction_Stack = new WPAction_Stack();
        
        */
    
        /*
        Class[] parameter_Types = new Class[2];
        
        this.class_Obj = passed_Class_Obj;
        
        
        try {
            parameter_Types[0] = Class.forName("com.amlibtech.web_Processes.Web_Process");
            parameter_Types[1] = Class.forName("java.lang.String");
        }catch (ClassNotFoundException cnfe){
            String message ="Status :"+ status_Text +":  Error loading Class for : " + this.class_Obj.getName() + ", ClassNotFoundException: " + cnfe.getMessage();
            throw new WPFatal_Exception(message);
        }
        
        try {
            this.method_Obj = class_Obj.getDeclaredMethod(method_Name, parameter_Types);
        }catch(NoSuchMethodException ex){
            String message ="Status :"+ status_Text +":  Error getting Method for : " + method_Name + ", NoSuchMethodException: " + ex.getMessage();
            throw new WPFatal_Exception(message);
        }
        */
       /* 
    }
    */
    
    
    /** Creates a new instance of WPStatus_Node */
    /*
    public WPStatus_Node(String status_Text, HttpServlet httpServlet, String method_Name)
    throws WPFatal_Exception {
        this(status_Text, httpServlet.getClass(), method_Name);
    }
    */
    
    
    /*
    public void invoke(Web_Process web_Process, String action)
    throws WPException {
        if(this.type!= this.TYPE_ACTIONABLE){
            String message ="Error invoking status : " + this.status_Text + ", it is not actionable.";
            throw new WPException(message);
        }
        Object[] args = new Object[2];
        args[0] = web_Process;
        args[1] = action;
        
        Object class_Instance = null;
        try {
            class_Instance = this.class_Obj.newInstance();
        }catch (InstantiationException ie){
            String message ="Status :"+ status_Text +":  Error instanciating Class for : " + this.class_Obj.getName() + ", InstantiationException: " + ie.getMessage();
            throw new WPException(message);
        }catch (IllegalAccessException iae){
            String message ="Status :"+ status_Text +":  Error instanciating Class for : " + this.class_Obj.getName() + ", IllegalAccessException: " + iae.getMessage();
            throw new WPException(message);
        }
        
        try {
            this.method_Obj.invoke(class_Instance, args);
        }catch (IllegalAccessException iae){
            String message ="Status :"+ status_Text +":  Error invoking method for : " + this.method_Obj.getName() + ", IllegalAccessException: " + iae.getMessage();
            throw new WPException(message);
        }catch (InvocationTargetException ite){
            String message ="Status :"+ status_Text +":  Error invoking method for : " + this.method_Obj.getName() + ", InvocationTargetException: " + ite.getMessage();
            throw new WPException(message);
        }
    }
    */
    
    
    /**
     * Getter for property status_Text.
     * @return Value of property status_Text.
     */
    public String getStatus_Text() {
        return status_Text;
    }
    
    
    public boolean is_Actionable(){
        return (this.type == this.TYPE_ACTIONABLE);
    }
    
    /**
     * Getter for property wpAction_Stack.
     * @return Value of property wpAction_Stack.
     */
    //    public WPAction_Stack getWPAction_Stack() {
    //        return wpAction_Stack;
    //    }
    
    protected void clear_Action_Stack(){
        wpAction_Stack.clear_Action_Stack();
    }
    
    protected void add_Action_Node(WPAction_Node wpAction_Node)
    throws WPFatal_Exception {
        this.type = this.TYPE_ACTIONABLE;
        wpAction_Stack.add_Action_Node(wpAction_Node);
    }
    
    protected void find_And_Invoke_Action(Web_Process web_Process, String action_Text)
    throws WPException {
        System.err.println("in WPStatus_Node.find_And_Invoke_Action() action_text="+action_Text);
        wpAction_Stack.find_And_Invoke_Action(web_Process, action_Text);
    }
    
    /**
     * Getter for property breadCrumb_Text.
     * @return Value of property breadCrumb_Text.
     */
    public String getBreadCrumb_Text() {
        return breadCrumb_Text;
    }
    
    /**
     * Setter for property breadCrumb_Text.
     * @param breadCrumb_Text New value of property breadCrumb_Text.
     */
    public void setBreadCrumb_Text(String breadCrumb_Text) {
        this.breadCrumb_Text = breadCrumb_Text;
    }
    
    
    public String print(){
        String result = "WPStatus Node: {\n";
        result += "Type: "+ TYPES[type] + "\n";
        result += "Status Text: "+ status_Text + "\n";
        result += "breadCrumb Text: "+ breadCrumb_Text + "\n";
        
        /*
        if(class_Obj==null){
            result += "class_Obj: null\n";
        }else{
            result += "class_Obj: " + class_Obj.getName() + "\n";
        }
        
        if(method_Obj==null){
            result += "method_Obj: null\n";
        }else{
            result += "method_Obj: " + method_Obj.getName() + "\n";
        }
        
        */
        
        if(wpAction_Stack==null){
            result += "wpAction_Stack: null\n";
        }else{
            result += wpAction_Stack.print();
        }
        
        result += "}\n";
        
        return result;
    }
    
    public void print(PrintWriter out){
        out.println(print());
    }
    
}
