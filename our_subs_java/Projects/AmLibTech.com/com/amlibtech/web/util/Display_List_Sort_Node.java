/*
 * Display_List_Sort_Node.java
 *
 * Created on October 8, 2004, 12:41 PM
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

/**
 *
 * @author  dgleeson
 */
public class Display_List_Sort_Node {
    public static final int DIRECTION_BACKWARD = -1;
    public static final int DIRECTION_NONE = 0;
    
    public static final int DIRECTION_FORWARD = 1;
    
    String field_Name;
    int direction;



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
    

    /** Creates a new instance of Display_List_Sort_Node */
    public Display_List_Sort_Node(String field_Name, int direction) {
        this.field_Name = field_Name;
        this.direction = direction;
    }
    
    public String getField_Name(){
        return field_Name;
    }
    
    public void setField_Name(String field_Name){
        this.field_Name = field_Name;
    }
    
    public int getDirection(){
        return direction;
    }
    
    public void setDirection(int direction){
        this.direction = direction;
    }
    
    
    public void rotateDirection(){
        if(direction == DIRECTION_NONE){
            direction=DIRECTION_FORWARD;
        }else if(direction == DIRECTION_FORWARD){
            direction=DIRECTION_BACKWARD;
        }else if(direction==DIRECTION_BACKWARD){
            direction=DIRECTION_NONE;
        }else{
            direction=DIRECTION_NONE;
        }
    }
    
    
    public String getDirection_Image(){
        if(direction == DIRECTION_NONE){
            return "/buttons/sort_none.png";
        }else if(direction == DIRECTION_FORWARD){
            return "/buttons/sort_down_pointer.png";
        }else if(direction==DIRECTION_BACKWARD){
            return "/buttons/sort_up_pointer.png";
        }else{
            return "/buttons/sort_none.png";
        }
    }
    
}
