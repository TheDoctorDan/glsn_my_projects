/*
 * Call_Tracker_List.java
 *
 * Created on December 2, 2004, 1:40 PM
 */

package com.amlibtech.web.util;

import java.util.*;
import com.amlibtech.web.servlet_exceptions.*;
/**
 *
 * @author  dgleeson
 */
public class Call_Tracker_List {
    private LinkedList  linkedList;
    
    /** Creates a new instance of Call_Tracker_List */
    public Call_Tracker_List() {
        this.linkedList = new LinkedList();
    }
    
	public void setLinkedList(LinkedList linkedList){
		this.linkedList = linkedList;
	}

	public LinkedList getLinkedList(){ return linkedList; }

         
        public Call_Tracker_Node getCall_Tracker_Node_By_Id(String id) throws Parameter_Validation_Exception{
            Call_Tracker_Node call_Tracker_Node=null;
            boolean found=false;
            int size = this.linkedList.size();
            for(int i=size-1; i>=0 && !found ; i--){
                call_Tracker_Node = (Call_Tracker_Node) this.linkedList.get(i);
                if(call_Tracker_Node.getId().equals(id)){
                    found=true;
                }
            }
            if(!found) {  
                String message =  "No call_Tracker_Node found to match call_Tracker_Node_Id:" +  id + ".";
                throw new Parameter_Validation_Exception(message);
            }
            return call_Tracker_Node;
        }
    
        public Call_Tracker_Node getCall_Tracker_Node_By_Id_And_Remove_Higher(String id) throws Parameter_Validation_Exception{
            Call_Tracker_Node call_Tracker_Node=null;
    
            boolean found=false;
            int size = this.linkedList.size();
            for(int i=size-1; i>=0 && !found ; i--){
                call_Tracker_Node = (Call_Tracker_Node) this.linkedList.get(i);
                if(call_Tracker_Node.getId().equals(id)){
                    found=true;
                }else{
                    this.linkedList.remove(i);
                }
            }
            if(!found) {
                String message =  "No call_Tracker_Node found to match call_Tracker_Node_Id:" +  id + ".";
                throw new Parameter_Validation_Exception(message);
            }
            return call_Tracker_Node;
        }
        
        public Call_Tracker_Node getCall_Tracker_Node_By_What_Am_I_Doing(String what_Am_I_Doing) throws Parameter_Validation_Exception{
            Call_Tracker_Node call_Tracker_Node=null;
            boolean found=false;
            int size = this.linkedList.size();
            for(int i=size-1; i>=0 && !found ; i--){
                call_Tracker_Node = (Call_Tracker_Node) this.linkedList.get(i);
                if(call_Tracker_Node.getWhat_Am_I_Doing().equals(what_Am_I_Doing)){
                    found=true;
                }
            }
            if(!found) {
                String message =  "No call_Tracker_Node found to match call_Tracker_Node_What_Am_I_Doing:" +  what_Am_I_Doing + ".";
                throw new Parameter_Validation_Exception(message);
            }
            return call_Tracker_Node;
        }
    
        public void add(Call_Tracker_Node call_Tracker_Node){
            this.linkedList.add(call_Tracker_Node);
        }
       
        public void remove_Node(Call_Tracker_Node call_Tracker_Node){
            this.linkedList.remove(call_Tracker_Node);
        }
}
