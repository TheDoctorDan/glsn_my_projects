/*
 * Field_Description_Node.java
 *
 * Created on October 8, 2004, 9:50 PM
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

import java.math.BigDecimal;

/**
 *
 * @author  dgleeson
 */
public class Field_Description_Node {
    public static final int FILL_TYPE_NONE = 0;
    public static final int FILL_TYPE_LEFT = 1;
    public static final int FILL_TYPE_RIGHT = 2;
    public static final int FILL_TYPE_CENTER = 3;
    public static final int FILL_TYPE_KFILL = 4;
    public static final int FILL_TYPE_TRIM = 5;
    
    
    private String  type;
    private String field_Name;
    private String field_Title;
    
    private Integer min_Length;
    private Integer max_Length;
    
    private Integer  integer_Min_Value;
    private Integer  integer_Max_Value;
    
    private Double  double_Min_Value;
    private Double  double_Max_Value;
    
    private BigDecimal  bigDecimal_Min_Value;
    private BigDecimal  bigDecimal_Max_Value;
    
    private int precision; //number of decimal digits.
    private int scale;     //number of digits to right of the decimal point.
    
    private int  fill_Type; // left, right, trim
    private char  fill_Char;
    
    private String  fill_Pattern;




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
    
    
    /** Creates a new instance of Field_Description_Node */
    
    public Field_Description_Node(
    String type,
    String field_Name,
    String field_Title
    ) {
        this.type = type;
        this.field_Name = field_Name;
        this.field_Title = field_Title;
        this.min_Length = null;
        this.max_Length =null;
        this.integer_Min_Value=null;
        this.integer_Max_Value=null;
        this.double_Min_Value=null;
        this.double_Max_Value=null;
        this.bigDecimal_Min_Value=null;
        this.bigDecimal_Max_Value=null;
	this.precision=0;
	this.scale=0;
        this.fill_Type=FILL_TYPE_NONE;
        this.fill_Char=' ';
        this.fill_Pattern=null;
    }
    
    public static Field_Description_Node create_String(
    String field_Name,
    String field_Title,
    int min_Length,
    int max_Length
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("String", field_Name, field_Title);
        field_Description_node.setMin_Length(new Integer(min_Length));
        field_Description_node.setMax_Length(new Integer(max_Length));
        
        return field_Description_node;
    }
    
    
    
    public static Field_Description_Node create_Key_String(
    String field_Name,
    String field_Title,
    int min_Length,
    int max_Length,
    String fill_Pattern,
    char fill_Char
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Key_String", field_Name, field_Title);
        field_Description_node.setMin_Length(new Integer(min_Length));
        field_Description_node.setMax_Length(new Integer(max_Length));
        field_Description_node.setFill_Pattern(fill_Pattern);
        field_Description_node.setFill_Char(fill_Char);
        return field_Description_node;
    }
    
    
    public static Field_Description_Node create_Filled_String(
    String field_Name,
    String field_Title,
    int min_Length,
    int max_Length,
    int fill_Type,
    char fill_Char
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Filled_String", field_Name, field_Title);
        field_Description_node.setMin_Length(new Integer(min_Length));
        field_Description_node.setMax_Length(new Integer(max_Length));
        field_Description_node.setFill_Type(fill_Type);
        field_Description_node.setFill_Char(fill_Char);
        return field_Description_node;
    }
    
    
    
    public static Field_Description_Node create_Integer(
    String field_Name,
    String field_Title
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Integer", field_Name, field_Title);
        return field_Description_node;
    }
    
    
    
    public static Field_Description_Node create_Integer(
    String field_Name,
    String field_Title,
    Integer min_Value,
    Integer max_Value
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Integer", field_Name, field_Title);
        field_Description_node.setInteger_Min_Value(min_Value);
        field_Description_node.setInteger_Max_Value(max_Value);
        return field_Description_node;
    }
    
    
    public static Field_Description_Node create_Double(
    String field_Name,
    String field_Title,
    int precision,
    int scale
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Double", field_Name, field_Title);
        field_Description_node.setPrecision(precision);
        field_Description_node.setScale(scale);
        return field_Description_node;
    }
    
    
    
    public static Field_Description_Node create_Double(
    String field_Name,
    String field_Title,
    int precision,
    int scale,
    Double min_Value,
    Double max_Value
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Double", field_Name, field_Title);
        field_Description_node.setPrecision(precision);
        field_Description_node.setScale(scale);
        field_Description_node.setDouble_Min_Value(min_Value);
        field_Description_node.setDouble_Max_Value(max_Value);
        return field_Description_node;
    }
    
    
    public static Field_Description_Node create_BigDecimal(
    String field_Name,
    String field_Title,
    int precision,
    int scale
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("BigDecimal", field_Name, field_Title);
        field_Description_node.setPrecision(precision);
        field_Description_node.setScale(scale);
        return field_Description_node;
    }
    
    
    
    public static Field_Description_Node create_BigDecimal(
    String field_Name,
    String field_Title,
    int precision,
    int scale,
    BigDecimal min_Value,
    BigDecimal max_Value
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("BigDecimal", field_Name, field_Title);
        field_Description_node.setPrecision(precision);
        field_Description_node.setScale(scale);
        field_Description_node.setBigDecimal_Min_Value(min_Value);
        field_Description_node.setBigDecimal_Max_Value(max_Value);
        return field_Description_node;
    }
    
    
    public static Field_Description_Node create_Boolean(
    String field_Name,
    String field_Title
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Boolean", field_Name, field_Title);
        return field_Description_node;
    }
    
    
    
    
    public static Field_Description_Node create_Date(
    String field_Name,
    String field_Title
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Date", field_Name, field_Title);
        return field_Description_node;
    }
    
    public static Field_Description_Node create_Sql_Date(
    String field_Name,
    String field_Title
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Sql_Date", field_Name, field_Title);
        return field_Description_node;
    }
    
    
    
    public static Field_Description_Node create_Timestamp(
    String field_Name,
    String field_Title
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Timestamp", field_Name, field_Title);
        return field_Description_node;
    }
    
    
    
    public static Field_Description_Node create_TimeZone_String(
    String field_Name,
    String field_Title,
    int min_Length,
    int max_Length
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("TimeZone_String", field_Name, field_Title);
        field_Description_node.setMin_Length(new Integer(min_Length));
        field_Description_node.setMax_Length(new Integer(max_Length));
        
        return field_Description_node;
    }
    
    
    public static Field_Description_Node create_Color(
    String field_Name,
    String field_Title
    ) {
        Field_Description_Node field_Description_node = new Field_Description_Node("Color", field_Name, field_Title);
        return field_Description_node;
    }
    
    
    
    
    
    public void setType(String type){
        this.type = type;
    }
    
    public String getType(){ return type; }
    
    public void setField_Name(String field_Name){
        this.field_Name = field_Name;
    }
    
    public String getField_Name(){ return field_Name; }
    
    public void setField_Title(String field_Title){
        this.field_Title = field_Title;
    }
    
    public String getField_Title(){ return field_Title; }
    
    public void setMin_Length(Integer min_Length){
        this.min_Length = min_Length;
    }
    
    public Integer getMin_Length(){ return min_Length; }
    
    public void setMax_Length(Integer max_Length){
        this.max_Length = max_Length;
    }
    
    public Integer getMax_Length(){ return max_Length; }
    
    public void setInteger_Min_Value(Integer integer_Min_Value){
        this.integer_Min_Value = integer_Min_Value;
    }
    
    public Integer getInteger_Min_Value(){ return integer_Min_Value; }
    
    public void setInteger_Max_Value(Integer integer_Max_Value){
        this.integer_Max_Value = integer_Max_Value;
    }
    
    public Integer getInteger_Max_Value(){ return integer_Max_Value; }
    
    public void setDouble_Min_Value(Double double_Min_Value){
        this.double_Min_Value = double_Min_Value;
    }
    
    public Double getDouble_Min_Value(){ return double_Min_Value; }
    
    public void setDouble_Max_Value(Double double_Max_Value){
        this.double_Max_Value = double_Max_Value;
    }
    
    public Double getDouble_Max_Value(){ return double_Max_Value; }
    
    public void setBigDecimal_Min_Value(BigDecimal bigDecimal_Min_Value){
        this.bigDecimal_Min_Value = bigDecimal_Min_Value;
    }
    
    public BigDecimal getBigDecimal_Min_Value(){ return bigDecimal_Min_Value; }
    
    public void setBigDecimal_Max_Value(BigDecimal bigDecimal_Max_Value){
        this.bigDecimal_Max_Value = bigDecimal_Max_Value;
    }
    
    public BigDecimal getBigDecimal_Max_Value(){ return bigDecimal_Max_Value; }
    
    
    public void setPrecision(int precision){
        this.precision = precision;
    }
    
    public int getPrecision(){ return precision; }
    
    public void setScale(int scale){
        this.scale = scale;
    }
    
    public int getScale(){ return scale; }
    
    
    public void setFill_Type(int fill_Type){
        this.fill_Type = fill_Type;
    }
    
    public int getFill_Type(){ return fill_Type; }
    
    public void setFill_Char(char fill_Char){
        this.fill_Char = fill_Char;
    }
    
    public char getFill_Char(){ return fill_Char; }
    
    
    public void setFill_Pattern(String fill_Pattern){
        this.fill_Pattern = fill_Pattern;
    }
    
    public String getFill_Pattern(){ return fill_Pattern; }
    
    
    public Field_Description_Node copy(){
        Field_Description_Node result = new Field_Description_Node(this.type, this.field_Name, this.field_Title);
        
        if(this.min_Length != null)
            result.min_Length = 	new Integer(this.min_Length.intValue());
        if(this.max_Length!= null)
            result.max_Length = 	new Integer(this.max_Length.intValue());
        if(this.integer_Min_Value!= null)
            result.integer_Min_Value = 	new Integer(this.integer_Min_Value.intValue());
        if(this.integer_Max_Value!= null)
            result.integer_Max_Value = 	new Integer(this.integer_Max_Value.intValue());
        if(this.double_Min_Value!= null)
            result.double_Min_Value = 	new Double(this.double_Min_Value.doubleValue());
        if(this.double_Max_Value!= null)
            result.double_Max_Value = 	new Double(this.double_Max_Value.doubleValue());
        if(this.bigDecimal_Min_Value!= null)
            result.bigDecimal_Min_Value = 	new BigDecimal(this.bigDecimal_Min_Value.doubleValue());
        
        if(this.bigDecimal_Max_Value!= null)
            result.bigDecimal_Max_Value = 	new BigDecimal(this.bigDecimal_Max_Value.doubleValue());
        
        result.precision =      this.precision;
        result.scale =          this.scale;
        
        result.fill_Type = 	this.fill_Type;
        result.fill_Char = 	this.fill_Char;
        result.fill_Pattern = 	this.fill_Pattern;
        
        return result;
        
    }
}
