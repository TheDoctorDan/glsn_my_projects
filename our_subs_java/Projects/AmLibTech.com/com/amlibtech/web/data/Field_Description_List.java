/*
 * Field_Description_List.java
 *
 * Created on October 8, 2004, 9:54 PM
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

package com.amlibtech.web.data;

import java.util.*;
import java.math.BigDecimal;

/**
 *
 * @author  dgleeson
 */
public class Field_Description_List {
    ArrayList   field_Description_Nodes;

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

    /** Creates a new instance of Field_Description_List */
    public Field_Description_List() {
        field_Description_Nodes = new ArrayList();
    }



    public void add_Node_String(String field_Name, String field_Title, int min_Length, int max_Length){
        field_Description_Nodes.add(
        Field_Description_Node.create_String(field_Name, field_Title, min_Length, max_Length));
    }


    public void add_Node_Key_String(String field_Name, String field_Title, int min_Length, int max_Length,  String fill_Pattern, char fill_Character){
        field_Description_Nodes.add(
        Field_Description_Node.create_Key_String(field_Name, field_Title, min_Length, max_Length,  fill_Pattern, fill_Character));
    }

    public void add_Node_Filled_String(String field_Name, String field_Title, int min_Length, int max_Length, int fill_Type, char fill_Character){
        field_Description_Nodes.add(
        Field_Description_Node.create_Filled_String(field_Name, field_Title, min_Length, max_Length, fill_Type, fill_Character));
    }


    public void add_Node_Integer(String field_Name, String field_Title){
        field_Description_Nodes.add(
        Field_Description_Node.create_Integer(field_Name, field_Title));
    }


    public void add_Node_Integer(String field_Name, String field_Title, Integer min_Value, Integer max_Value){
        field_Description_Nodes.add(
        Field_Description_Node.create_Integer(field_Name, field_Title, min_Value, max_Value));
    }


    public void add_Node_Double(String field_Name, String field_Title, int precision, int scale){
        field_Description_Nodes.add(
        Field_Description_Node.create_Double(field_Name, field_Title, precision, scale));
    }


    public void add_Node_Double(String field_Name, String field_Title, int precision, int scale, Double min_Value, Double max_Value){
        field_Description_Nodes.add(
        Field_Description_Node.create_Double(field_Name, field_Title, precision, scale, min_Value, max_Value));
    }


    public void add_Node_BigDecimal(String field_Name, String field_Title, int precision, int scale){
        field_Description_Nodes.add(
        Field_Description_Node.create_BigDecimal(field_Name, field_Title, precision, scale));
    }


    public void add_Node_BigDecimal(String field_Name, String field_Title, int precision, int scale, BigDecimal min_Value, BigDecimal max_Value){
        field_Description_Nodes.add(
        Field_Description_Node.create_BigDecimal(field_Name, field_Title, precision, scale, min_Value, max_Value));
    }


    public void add_Node_Boolean(String field_Name, String field_Title){
        field_Description_Nodes.add(
        Field_Description_Node.create_Boolean(field_Name, field_Title));
    }


    public void add_Node_Sql_Date(String field_Name, String field_Title){
        field_Description_Nodes.add(
        Field_Description_Node.create_Sql_Date(field_Name, field_Title));
    }

    public void add_Node_Date(String field_Name, String field_Title){
        field_Description_Nodes.add(
        Field_Description_Node.create_Date(field_Name, field_Title));
    }


    public void add_Node_Timestamp(String field_Name, String field_Title){
        field_Description_Nodes.add(
        Field_Description_Node.create_Timestamp(field_Name, field_Title));
    }


    public void add_Node_TimeZone_String(String field_Name, String field_Title, int min_Length, int max_Length){
        field_Description_Nodes.add(
        Field_Description_Node.create_TimeZone_String(field_Name, field_Title, min_Length, max_Length));
    }


    public void add_Node_Color(String field_Name, String field_Title){
        field_Description_Nodes.add(
        Field_Description_Node.create_Color(field_Name, field_Title));
    }








    public  String getField_Title(String field_Name) {
        Field_Description_Node field_Description_Node=null;

        for(int i=0;i< field_Description_Nodes.size(); i++){
            field_Description_Node =(Field_Description_Node) field_Description_Nodes.get(i);

            if(field_Description_Node.getField_Name().equals(field_Name))
                return field_Description_Node.getField_Title();
        }
        return (field_Name + " is an unknown field name");
    }

    
    public  boolean setField_Title(String field_Name, String title) {
        Field_Description_Node field_Description_Node=null;
        boolean found=false;
        for(int i=0;i< field_Description_Nodes.size(); i++){
            field_Description_Node =(Field_Description_Node) field_Description_Nodes.get(i);

            if(field_Description_Node.getField_Name().equals(field_Name)){
                 field_Description_Node.setField_Title(title);
                 found=true;
                 return found;
            }
        }
        return found;
    }
    
    
    public  boolean	is_Valid_Field(String field_Name) {
        Field_Description_Node field_Description_Node=null;

        for(int i=0;i< field_Description_Nodes.size(); i++){
            field_Description_Node =(Field_Description_Node) field_Description_Nodes.get(i);

            if(field_Description_Node.getField_Name().equals(field_Name))
                return true;
        }
        return false;
    }

    public  Field_Description_Node getField_Node(String field_Name) {
        Field_Description_Node field_Description_Node=null;

        for(int i=0;i< field_Description_Nodes.size(); i++){
            field_Description_Node =(Field_Description_Node) field_Description_Nodes.get(i);

            if(field_Description_Node.getField_Name().equals(field_Name))
                return field_Description_Node;
        }
        return null;
    }

    
    public  String getField_Name(int index) {
        if(index<0 || index >= this.field_Description_Nodes.size())
            return null;
        Field_Description_Node field_Description_Node=(Field_Description_Node) field_Description_Nodes.get(index);
        return field_Description_Node.getField_Name();      
    }
    
    
    public Field_Description_List copy(){
        Field_Description_List result = new Field_Description_List();
        Field_Description_Node field_Description_Node=null;

        for(int i=0;i< this.field_Description_Nodes.size(); i++){
            field_Description_Node =(Field_Description_Node) this.field_Description_Nodes.get(i);
            result.field_Description_Nodes.add(field_Description_Node.copy());
        }
        return result;
        
    }

    public int length(){
        return this.field_Description_Nodes.size();
    }
}
