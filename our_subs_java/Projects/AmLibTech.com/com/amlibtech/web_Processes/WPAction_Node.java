/*
 * WPAction_Node.java
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
public class WPAction_Node {
    private String  action_Text;
    private Class   class_Obj;
    private Method  method_Obj;
    
    
    
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
    
    
    /** Creates a new instance of WPAction_Node */
    public WPAction_Node(String action_Text, Class passed_Class_Obj, String method_Name)
    throws WPFatal_Exception {
        this.action_Text = action_Text;
        this.class_Obj = passed_Class_Obj;
        
        Class[] parameter_Types = new Class[1];
        
        try {
            parameter_Types[0] = Class.forName("com.amlibtech.web_Processes.Web_Process");
        }
        catch (ClassNotFoundException cnfe){
            String message ="Action :"+ action_Text +":  Error loading Class for : " + "com.amlibtech.web_Processes.Web_Process" + ", ClassNotFoundException: " + cnfe.getMessage();
            throw new WPFatal_Exception(message);
        }
        
        try {
            this.method_Obj = class_Obj.getDeclaredMethod(method_Name, parameter_Types);
        }catch(NoSuchMethodException ex){
            String message ="Action :"+ action_Text +":  Error getting Method for : " + method_Name + ", NoSuchMethodException: " + ex.getMessage();
            throw new WPFatal_Exception(message);
        }
    }
    
    /** Creates a new instance of WPAction_Node */
    //    public WPAction_Node(String action_Text, String class_Name, String method_Name)
    //    throws WPFatal_Exception {
    //        this.action_Text = action_Text;
    //
    //        Class[] parameter_Types = new Class[1];
    //
    //        try {
    //            this.class_Obj = Class.forName(class_Name);
    //        }
    //        catch (ClassNotFoundException cnfe){
    //            String message ="Action :"+ action_Text +":  Error loading Class for : " + class_Name + ", ClassNotFoundException: " + cnfe.getMessage();
    //            throw new WPFatal_Exception(message);
    //        }
    //
    //
    //        try {
    //            parameter_Types[0] = Class.forName("web_Processes.Web_Process");
    //        }
    //        catch (ClassNotFoundException cnfe){
    //            String message ="Action :"+ action_Text +":  Error loading Class for : " + "web_Processes.Web_Process" + ", ClassNotFoundException: " + cnfe.getMessage();
    //            throw new WPFatal_Exception(message);
    //        }
    //
    //        try {
    //            this.method_Obj = class_Obj.getDeclaredMethod(method_Name, parameter_Types);
    //        }catch(NoSuchMethodException ex){
    //            String message ="Action :"+ action_Text +":  Error getting Method for : " + method_Name + ", NoSuchMethodException: " + ex.getMessage();
    //            throw new WPFatal_Exception(message);
    //        }
    //    }
    
    /** Creates a new instance of WPAction_Node */
    public WPAction_Node(String action_Text, HttpServlet httpServlet, String method_Name)
    throws WPFatal_Exception {
        this(action_Text, httpServlet.getClass(), method_Name);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void invoke(Web_Process web_Process)
    throws WPException {
        System.err.println("in WPAction_Node.invoke() action_text="+action_Text);

        Object[] args = new Object[1];
        args[0] = web_Process;
        
	if(this.class_Obj ==null){
                String message ="Action :"+ action_Text +":  Error class_Obj is null.";
		throw new WPException(message);
	}
	
	if(this.method_Obj ==null){
		String message ="Action :"+ action_Text +":  Error method_Obj is null.";
		throw new WPException(message);
	}
	
        Object class_Instance = null;
        try {
            class_Instance = this.class_Obj.newInstance();
        }
        catch (InstantiationException ie){
            String message ="Action :"+ action_Text +":  Error instanciating Class for : " + this.class_Obj.getName() + ", InstantiationException: " + ie.getMessage();
            throw new WPException(message);
        }
        catch (IllegalAccessException iae){
            String message ="Action :"+ action_Text +":  Error instanciating Class for : " + this.class_Obj.getName() + ", IllegalAccessException: " + iae.getMessage();
            throw new WPException(message);
        }
        
        try {
            this.method_Obj.invoke(class_Instance, args);
        }catch (IllegalAccessException iae){
            String message ="Action :"+ action_Text +":  Error invoking method : " + this.method_Obj.getName() + ", IllegalAccessException: " + iae.getMessage();
            throw new WPException(message);
        }catch (InvocationTargetException ite){
            String message ="Action :"+ action_Text +":  Error invoking method : " + this.method_Obj.getName() + ", InvocationTargetException: " + ite.getMessage();
	    ite.printStackTrace();
            throw new WPException(message);
        }
        //        if(web_Process.getError_Exception() != null){
        //            web_Process.error_End(web_Process.getError_Exception());
        //        }
    }
    
    /**
     * Getter for property action_Text.
     * @return Value of property action_Text.
     */
    public java.lang.String getAction_Text() {
        return action_Text;
    }
    
    public boolean equals(WPAction_Node passed_WPAction_Node){
        if(!this.action_Text.equals(passed_WPAction_Node.action_Text)){
            return false;
        }
        if(!this.class_Obj.getName().equals(passed_WPAction_Node.class_Obj.getName())){
            return false;
        }
        if(!this.method_Obj.getName().equals(passed_WPAction_Node.method_Obj.getName())){
            return false;
        }
        
        return true;
    }
    
    public String print(){
        String result = "WPAction Node: {\n";
        result += "Action Text :" + action_Text + ":\n";
        
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
        
        result += "}\n";
        
        return result;
    }
    
    public void print(PrintWriter out){
        out.print(print());
    }
}
