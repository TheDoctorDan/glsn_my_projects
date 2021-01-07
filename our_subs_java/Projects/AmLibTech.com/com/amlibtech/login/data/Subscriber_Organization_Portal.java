/*
 * Subscriber_Organization_Portal.java
 *
 * Created on May 24, 2006, 2:20 PM
 */
/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 2000, 2001, 2002, 2003, 2004, 2005, 2006       |
|       American Liberator Technologies                                        |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       American Liberator Technologies                                        |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
*/

package com.amlibtech.login.data;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import java.util.*;
import javax.servlet.http.*;

/**
 * List of Portals for a Subscriber Organization.
 * Controls landing/menu page, root paths for JavaScripts, css, images, application context.
 * Also allows for custom css themes, preselected constants.
 * Controls login method.
 * @author  dgleeson
 */
public class Subscriber_Organization_Portal extends Database_Record_Base {
    
    /** ID for the Subscriber Organization.
     * Length 1-30.
     * (table Key 1) 
     */
    private DFString	subscriber_Organization_Id;
    /** Name of the Portal.
     * Length 1-30.
     * (table Key 2)
     */
    private DFString    portal_Name;
    /** File path of Landing page of successful login or of program termination.
     * Usually a menu.
     * A prefix of web_Root + "/Portal_Pages/" is assumed.
     * Length 1-100
     */
    private DFString    destination_Url;
    /** Directory path where JavaScripts are kept.
     * A prefix of web_Root is assumed.
     */
    private DFString    menu_Js_Path;
    /** Choice of one of the four login methods */
    private DFString_Enumerated login_Method;
    /** Context root for the application in the application server.
     *  This is a directory path and must start with "/".
     */
    private DFString    web_Root;
    /** Directory path where portal images are kept.
     * A prefix of web_Root is assumed.
     * Standard library images are kept in web_root + "/images".
     */
    private DFString    image_Root;
    /** Directory path where cascading style sheets are kept.
     * A prefix of web_Root is assumed.
     * Standard library css are kept in web_root + css_Root .
     * Many portal css are also kept there.
     */
    private DFString    css_Root;
    /** Used to decide if portal with use the file "Std.css" in the css themes directory or not. */
    private DFBoolean   use_Std_Css_Theme;
    /** File path of the portal's css.
     * A prefix of web_Root + css_Root is assumed.
     */
    private DFString    custom_Css_Theme_Name;
    
    /** A Company ID# can be affixed to this portal.
     * Useful in a multi company enviroment.
     * Login for customer, vendors and employees can be limited to a company
     * based on portal selected.
     */
    private DFString_Filled    customer_Or_Vendor_Comp_Id;
    
    private DFBoolean   kiosk_Mode;
    

    /** Constant for User index in LOGIN_METHODS array */
    public static final int LOGIN_METHOD_INDEX_SYSTEM_USER = 0;
    /** Constant for Customer index in LOGIN_METHODS array */
    public static final int LOGIN_METHOD_INDEX_CUSTOMER = 1;
    /** Constant for Vendor index in LOGIN_METHODS array */
    public static final int LOGIN_METHOD_INDEX_VENDOR = 2;
    /** Constant for Guest index in LOGIN_METHODS array */
    public static final int LOGIN_METHOD_INDEX_GUEST = 3;
    /** Constant for Teacher index in LOGIN_METHODS array */
    public static final int LOGIN_METHOD_INDEX_TEACHER = 4;
    /** Constant for Student index in LOGIN_METHODS array */
    public static final int LOGIN_METHOD_INDEX_STUDENT = 5;
    /** Valid login methods.
     * "User", "Customer", "Vendor", "Guest", "Teacher", "Student"
     */
    public static final String[] LOGIN_METHODS = { "User", "Customer", "Vendor", "Guest", "Teacher", "Student" };
    
    
    public static final int SUBSCRIBER_ORGANIZATION_PORTAL_NAME_LEN = 30;

    
    public static DFString DFInit_Portal_Name()
    throws DFException {
        return new DFString("portal_Name", "Portal Name", 1, SUBSCRIBER_ORGANIZATION_PORTAL_NAME_LEN);
    }

    public static DFString_Enumerated DFInit_Login_Method()
    throws DFException {
        return new DFString_Enumerated("login_Method", "Login Method", LOGIN_METHODS);

    }

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
    

    //============== Begin jdbr Constructor Section ===========================


    // Empty Constructor
    /** Creates a new instance of Subscriber_Organization_Portal
     * @throws Database_Record_Constructor_Exception
     */
    public Subscriber_Organization_Portal() throws Database_Record_Constructor_Exception {
        super("Subscriber Organization Portal", "Subscriber Organization Portals");
        try {
            this.subscriber_Organization_Id = Subscriber_Organization.DFInit_Subscriber_Organization_Id();
            this.portal_Name = DFInit_Portal_Name();

            this.destination_Url = new DFString("destination_Url", "Destination URL", 0, 100);
            this.menu_Js_Path = new DFString("menu_Js_Path", "Menu JavaScript Path", 1, 50);
            this.login_Method = DFInit_Login_Method();
            this.web_Root = new DFString("web_Root", "Web Root", 1, 50);

            this.image_Root =  new DFString("image_Root", "Image Root", 1, 50);

            this.css_Root = new DFString("css_Root", "CSS Root", 1, 200);
            this.use_Std_Css_Theme = new DFBoolean("use_Std_Css_Theme", "Use Std Css Theme");
            this.custom_Css_Theme_Name = new DFString("custom_Css_Theme_Name", "Custom Css Theme Name", 40);
            this.customer_Or_Vendor_Comp_Id = new DFString_Filled("customer_Or_Vendor_Comp_Id", "Company Id", 2, DFString_Filled.FILL_TYPE_LEFT, '0');

            this.kiosk_Mode =  new DFBoolean("kiosk_Mode", "Kiosk Mode");

            DFBase[] index =   { this.subscriber_Organization_Id, this.portal_Name };
            this.setPrimary_Index_DFields(index);
        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor() : failed because DFException : " + dfe.getMessage());
        }
    }




    // Keys Only Constructor
    /** Creates a new instance of Subscriber_Organization_Portal with key fields set to passed values.
     * @throws Database_Record_Constructor_Exception
     */
    public Subscriber_Organization_Portal(String subscriber_Organization_Id, String portal_Name) throws Database_Record_Constructor_Exception {
        this();
        try {
            this.subscriber_Organization_Id.setValue(subscriber_Organization_Id);
            this.portal_Name.setValue(portal_Name);

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": constructor(ID) : failed because DFException : " + dfe.getMessage());
        }
    }




    //============== End jdbr Constructor Section ===========================


    //============== Begin jdbr Generated Section ===========================



    /**
     * Returns name of Database table for these records.
     * @see Database_Front.
     * @return Table Name.
     */
    public String get_Table_Name() {
        return "subscriber_organization_portal";
    }




    /** Used to check for reasons not to delete this record from table.
     * @throws Database_Record_Exception
     * @throws Database_Record_Constructor_Exception
     * @param database_Front used if routine needs to check database.
     * @param httpSession used if routine needs extra info from a session attribute.
     */
    public synchronized void is_Delete_Allowed(Database_Front database_Front, HttpSession httpSession) throws Database_Record_Exception, Database_Record_Constructor_Exception {
        return;
    }




    /**
     * Used by Database_Front to return many records.
     * @param array_Size is the size of the array required.
     * @return Subscriber_Organization_Portal[] the size of array_Size as an Database_Record_Base[].
     */
    public Database_Record_Base[] get_Array_of_Records(int array_Size) {
        Subscriber_Organization_Portal[] subscriber_Organization_Portal_Records;
        subscriber_Organization_Portal_Records = new Subscriber_Organization_Portal[array_Size];
        return subscriber_Organization_Portal_Records;
    }





    /**
     * Creates a <code>String</code> containing an SQL command to generate table for this record.
     * @return SQL CREATE TABLE Command.
     */
    public String getSQL_Create_Table() {

        StringBuffer out = new StringBuffer();

        out.append("CREATE TABLE `" + get_Table_Name() + "` (\n");
        out.append("\t" + subscriber_Organization_Id.getSQL_Declaration());
        out.append("\t" + portal_Name.getSQL_Declaration());
        out.append("\t" + destination_Url.getSQL_Declaration());
        out.append("\t" + menu_Js_Path.getSQL_Declaration());
        out.append("\t" + login_Method.getSQL_Declaration());
        out.append("\t" + web_Root.getSQL_Declaration());
        out.append("\t" + image_Root.getSQL_Declaration());
        out.append("\t" + css_Root.getSQL_Declaration());
        out.append("\t" + use_Std_Css_Theme.getSQL_Declaration());
        out.append("\t" + custom_Css_Theme_Name.getSQL_Declaration());
        out.append("\t" + customer_Or_Vendor_Comp_Id.getSQL_Declaration());
        out.append("\t" + kiosk_Mode.getSQL_Declaration());


        out.append("        PRIMARY KEY  (`" + subscriber_Organization_Id.getField_Name() + "`, `" + portal_Name.getField_Name() + "`)\n");
        out.append(") TYPE=InnoDB;\n");

        return (out.toString());
    }




    /**
     * Used by Database_Front to sort records.
     * @param which_Index used to select which index to use.  PRIMARY_INDEX is always available.
     * @return SQL ORDER BY clause for selected index.
     * @throws Database_Front_Exception if selected index is invalid.
     */
    public String getNext_Sort_Clause(int which_Index) throws Database_Front_Exception {
        switch(which_Index){
            case PRIMARY_INDEX:
                return "ORDER BY subscriber_Organization_Id ASC, portal_Name ASC";
            default:
                throw new Database_Front_Exception(this.getClass().getName() + ".getNext_Sort_Clause(): unknown Index:"+which_Index);
        }
    }




    /**
     * Used by Database_Front to split matching records into small sets of records.
     * skip_Levels will increment as each prior level fails.
     * @param which_Index used to select which index to use.  PRIMARY_INDEX is always available.
     * @param skip_Levels used to control how many fields of the index are to be used.
     *  As last field of multi-field index maxes out, We want to allow next to last field to increase.
     * And then again on the next level.  Etc.
     * @return WHERE clause for which_Index and skip_Levels.
     * @return <code>null</code> if skip_Levels exceeds maximum number for selected index.
     * @return <code>null</code> if skip_Levels less than 0.
     * @throws Database_Front_Exception if selected index is invalid.
     */
    public String getNext_Where_Clause(int which_Index, int skip_Levels) throws Database_Front_Exception {
        switch(which_Index){
        case PRIMARY_INDEX:
            switch(skip_Levels){
            case 0:
                return "WHERE subscriber_Organization_Id = '" + SQLUtil.encode(this.subscriber_Organization_Id.toString()) + "' "
                    + " AND portal_Name > '" + SQLUtil.encode(this.portal_Name.toString()) + "' ";
            case 1:
                return "WHERE subscriber_Organization_Id > '" + SQLUtil.encode(this.subscriber_Organization_Id.toString()) + "' ";
            default:
                return null;
            }
        default:
            throw new Database_Front_Exception(this.getClass().getName() + ".getNext_Sort_Clause(): unknown Index:"+which_Index);
        }
    }





    /**
     * Used by Database_Front to encode fields into a SQL prepared statement.
     * The first section is for fields in Primary key.
     * See get_Key_Where_Clause() .
     * The second section is for the rest of the data fields.
     * @see get_Set_Clause().
     * @see also get_Into_Clause() and get_Values_Clause().
     * @param preparedStatement is the SQL statement with ? for values.
     * @param order indicates which groups of values in which order are in the preparedStatement.
     * order values can be:
     * <ul><li>ORDER_KEYS_FIRST used when index fields are at beginning of the preparedStatement. (addRecord(), INSERT INTO ...).
     * <li>ORDER_KEYS_LAST used when index fields are at end of the preparedStatement. (updateRecord(), UPDATE x SET ... WHERE ...).
     * <li>ORDER_KEYS_ONLY used when index fields are the only values in the preparedStatement. (is_Match(), getResultSet_Row(), Select * FROM x WHERE ..., deleteRecord(), DELETE FROM ...).
     * <li>ORDER_KEYS_NEVER used when index fields are not in the preparedStatement.
     * </ul>
     * @return preparedStatement with values inserted where ? were.
     * @throws Database_Record_Exception if any of the data_fields.DFBase Classes : get_PreparedStatement_Value() fails.
     */
    public synchronized java.sql.PreparedStatement preparedStatement_Encode(java.sql.PreparedStatement preparedStatement, int order)
    throws Database_Record_Exception {
        int count=1;
        try {
            for(int pass=1;pass<=2;pass++){
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_ONLY) ||
                (pass == 2 && order == ORDER_KEYS_LAST)
                ){
                    preparedStatement = this.subscriber_Organization_Id.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.portal_Name.get_PreparedStatement_Value(preparedStatement, count++);
                }
                if((pass == 1 && order == ORDER_KEYS_FIRST) ||
                (pass == 1 && order == ORDER_KEYS_LAST) ||
                (pass == 1 && order == ORDER_KEYS_NEVER)
                ){

                    preparedStatement = this.destination_Url.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.menu_Js_Path.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.login_Method.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.web_Root.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.image_Root.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.css_Root.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.use_Std_Css_Theme.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.custom_Css_Theme_Name.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.customer_Or_Vendor_Comp_Id.get_PreparedStatement_Value(preparedStatement, count++);
                    preparedStatement = this.kiosk_Mode.get_PreparedStatement_Value(preparedStatement, count++);
                }
            }
        }
        catch(DFException dfe){
            throw new Database_Record_Exception(this.getClass().getName() + ": preparedStatement_Encode(): failed because DFException : " + dfe.getMessage());
        }

        return preparedStatement;
    }





    /**
     * Used by Database_Front to decode SQL resultSet into data fields of record.
     * @param resultSet is a ResultSet returned from PrepareStatement.executeUpdate().
     * @return Subscriber_Organization_Portal record.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue_ResultSet() fails.
     */
    public synchronized Database_Record_Base resultSet_Row_Decode(java.sql.ResultSet resultSet) throws Database_Record_Constructor_Exception {
        Subscriber_Organization_Portal result;
        try {
            result = new Subscriber_Organization_Portal();

            result.subscriber_Organization_Id.setValue_ResultSet(resultSet);
            result.portal_Name.setValue_ResultSet(resultSet);
            result.destination_Url.setValue_ResultSet(resultSet);
            result.menu_Js_Path.setValue_ResultSet(resultSet);
            result.login_Method.setValue_ResultSet(resultSet);
            result.web_Root.setValue_ResultSet(resultSet);
            result.image_Root.setValue_ResultSet(resultSet);
            result.css_Root.setValue_ResultSet(resultSet);
            result.use_Std_Css_Theme.setValue_ResultSet(resultSet);
            result.custom_Css_Theme_Name.setValue_ResultSet(resultSet);
            result.customer_Or_Vendor_Comp_Id.setValue_ResultSet(resultSet);
            result.kiosk_Mode.setValue_ResultSet(resultSet);

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": resultSet_Row_Decode(): failed because DFException : " + dfe.getMessage());
        }

        return result;
    }





    /** Validate index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     */
    public synchronized void validate_Key_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix) throws Database_Record_Exception {
        int error_Count =0;

        error_Count += this.subscriber_Organization_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.portal_Name.validate_Request(prefix, httpServletRequest, suffix);

        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + ".validate_Key_Fields_Of_Request(): Invalid Key Data Field(s). " + error_Count + " errors.");

    }





    /** Validate non-index fields of an html form.
     * @param prefix contains string prepended to input field name of submitted html form.
     * @param httpServletRequest contains values of submitted html form.
     * @param database_Front used if values need to be validated against database.
     * @param suffix contains string appended to input field name of submitted html form.
     * @throws Database_Record_Exception if a value is invalid.
     * @throws Database_Record_Constructor_Exception if a value could not be validated against a foriegn record.
     */
    public synchronized void validate_Fields_Of_Request(String prefix, HttpServletRequest httpServletRequest, Database_Front database_Front, String suffix) throws Database_Record_Exception, Database_Record_Constructor_Exception {

        int error_Count =0;

        error_Count += this.destination_Url.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.menu_Js_Path.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.login_Method.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.web_Root.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.image_Root.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.css_Root.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.use_Std_Css_Theme.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.custom_Css_Theme_Name.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.customer_Or_Vendor_Comp_Id.validate_Request(prefix, httpServletRequest, suffix);
        error_Count += this.kiosk_Mode.validate_Request(prefix, httpServletRequest, suffix);


        if(error_Count !=0)
            throw new Database_Record_Exception(this.getClass().getName() + ".validate_Fields_Of_Request(): Invalid Data Field(s). " + error_Count + " errors.");


    }





    /** Returns a list of fields of this record. Set manually by programmer.
     * @return DFBase[]
     */
    public DFBase[] get_Field_List() {
        // programmer has entered ...
        LinkedList programmers_Field_List = new LinkedList();
        programmers_Field_List.add( subscriber_Organization_Id );
        programmers_Field_List.add( portal_Name );
        programmers_Field_List.add( destination_Url );
        programmers_Field_List.add( menu_Js_Path );
        programmers_Field_List.add( login_Method );
        programmers_Field_List.add( web_Root );
        programmers_Field_List.add( image_Root );
        programmers_Field_List.add( css_Root );
        programmers_Field_List.add( use_Std_Css_Theme );
        programmers_Field_List.add( custom_Css_Theme_Name );
        programmers_Field_List.add( customer_Or_Vendor_Comp_Id );
        programmers_Field_List.add( kiosk_Mode );
        DFBase[] programmers_Fields = new DFBase[programmers_Field_List.size()];
        for(int i=0; i<programmers_Fields.length;i++){
            programmers_Fields[i] = (DFBase) programmers_Field_List.get(i);
        }
        return programmers_Fields;
    }



    /**
     * @return Subscriber_Organization_Portal.
     * @throws Database_Record_Constructor_Exception if any of the data_fields.DFBase Classes : setValue() fails.
     */
    public synchronized Subscriber_Organization_Portal copy_Of() throws Database_Record_Constructor_Exception {
        Subscriber_Organization_Portal result;
        try {
            result = new Subscriber_Organization_Portal();

            result.setSubscriber_Organization_Id(this.subscriber_Organization_Id.getValue());
            result.setPortal_Name(this.portal_Name.getValue());
            result.setDestination_Url(this.destination_Url.getValue());
            result.setMenu_Js_Path(this.menu_Js_Path.getValue());
            result.setLogin_Method(this.login_Method.getValue());
            result.setWeb_Root(this.web_Root.getValue());
            result.setImage_Root(this.image_Root.getValue());
            result.setCss_Root(this.css_Root.getValue());
            result.setUse_Std_Css_Theme(this.use_Std_Css_Theme.getValue());
            result.setCustom_Css_Theme_Name(this.custom_Css_Theme_Name.getValue());
            result.setCustomer_Or_Vendor_Comp_Id(this.customer_Or_Vendor_Comp_Id.getValue());
            result.setKiosk_Mode(this.kiosk_Mode.getValue());

        }
        catch(DFException dfe){
            throw new Database_Record_Constructor_Exception(this.getClass().getName() + ": setValue(): failed because DFException : " + dfe.getMessage());
        }

        return result;
    }





    /**
     * Getter for property subscriber_Organization_Id.
     * @return Value of property subscriber_Organization_Id.
     */
    public DFString getSubscriber_Organization_Id() {
        return subscriber_Organization_Id;
    }


    /**
     * Setter for property subscriber_Organization_Id.
     * @param subscriber_Organization_Id New value of property subscriber_Organization_Id.
     */
    public void setSubscriber_Organization_Id(String subscriber_Organization_Id) throws DFException {
        this.subscriber_Organization_Id.setValue(subscriber_Organization_Id);
    }


    /**
     * Getter for property portal_Name.
     * @return Value of property portal_Name.
     */
    public DFString getPortal_Name() {
        return portal_Name;
    }


    /**
     * Setter for property portal_Name.
     * @param portal_Name New value of property portal_Name.
     */
    public void setPortal_Name(String portal_Name) throws DFException {
        this.portal_Name.setValue(portal_Name);
    }

    
    public String getPrefix_Dir(){
        return "/Subscriber_Organization/" + this.subscriber_Organization_Id.getValue() +
        "/Portal/" + this.portal_Name.getValue();
    }

    /**
     * Getter for property destination_Url.
     * @return Value of property destination_Url.
     */
    public DFString getDestination_Url() {
        if(this.destination_Url.getValue().equals("")){
            try {
                DFString temp_Destination_Url = new DFString(this.destination_Url.getField_Name(), this.destination_Url.getField_Title(), 1, 300);
                temp_Destination_Url.setValue(this.getPrefix_Dir() +"/menu.jsp");
                return temp_Destination_Url;
            }catch(DFException dfe){
                return destination_Url;
            }
        }else{
            return destination_Url;
        }
    }


    /**
     * Setter for property destination_Url.
     * @param destination_Url New value of property destination_Url.
     */
    public void setDestination_Url(String destination_Url) throws DFException {
        this.destination_Url.setValue(destination_Url);
    }


    /**
     * Getter for property menu_Js_Path.
     * @return Value of property menu_Js_Path.
     */
    public DFString getMenu_Js_Path() {
        if(this.menu_Js_Path.getValue().equals("")){
            try {
                DFString temp_Menu_Js_Path = new DFString(this.menu_Js_Path.getField_Name(), this.menu_Js_Path.getField_Title(), 1, 300);
                temp_Menu_Js_Path.setValue(this.getPrefix_Dir() +"/scripts/menu.js");
                return temp_Menu_Js_Path;
            }catch(DFException dfe){
                return menu_Js_Path;
            }
        }else{
            return menu_Js_Path;
        }
    }


    /**
     * Setter for property menu_Js_Path.
     * @param menu_Js_Path New value of property menu_Js_Path.
     */
    public void setMenu_Js_Path(String menu_Js_Path) throws DFException {
        this.menu_Js_Path.setValue(menu_Js_Path);
    }


    /**
     * Getter for property login_Method.
     * @return Value of property login_Method.
     */
    public DFString_Enumerated getLogin_Method() {
        return login_Method;
    }


    /**
     * Setter for property login_Method.
     * @param login_Method New value of property login_Method.
     */
    public void setLogin_Method(String login_Method) throws DFException {
        this.login_Method.setValue(login_Method);
    }


    /**
     * Getter for property web_Root.
     * @return Value of property web_Root.
     */
    public DFString getWeb_Root() {
        return web_Root;
    }


    /**
     * Setter for property web_Root.
     * @param web_Root New value of property web_Root.
     */
    public void setWeb_Root(String web_Root) throws DFException {
        this.web_Root.setValue(web_Root);
    }


    /**
     * Getter for property image_Root.
     * @return Value of property image_Root.
     */
    public DFString getImage_Root() {
        if(this.image_Root.getValue().equals("")){
            try {
                DFString temp_Image_Root = new DFString(this.image_Root.getField_Name(), this.image_Root.getField_Title(), 1, 300);
                temp_Image_Root.setValue(this.getPrefix_Dir() +"/images");
                return temp_Image_Root;
            }catch(DFException dfe){
                return image_Root;
            }
        }else{
            return image_Root;
        }
    }


    /**
     * Setter for property image_Root.
     * @param image_Root New value of property image_Root.
     */
    public void setImage_Root(String image_Root) throws DFException {
        this.image_Root.setValue(image_Root);
    }


    /**
     * Getter for property css_Root.
     * @return Value of property css_Root.
     */
    public DFString getCss_Root() {
        if(this.css_Root.getValue().equals("")){
            try {
                DFString temp_Css_Root = new DFString(this.css_Root.getField_Name(), this.css_Root.getField_Title(), 1, 300);
                temp_Css_Root.setValue(this.getPrefix_Dir() +"/css_Include");
                return temp_Css_Root;
            }catch(DFException dfe){
                return css_Root;
            }
        }else{
            return css_Root;
        }
    }


    /**
     * Setter for property css_Root.
     * @param css_Root New value of property css_Root.
     */
    public void setCss_Root(String css_Root) throws DFException {
        this.css_Root.setValue(css_Root);
    }


    /**
     * Getter for property use_Std_Css_Theme.
     * @return Value of property use_Std_Css_Theme.
     */
    public DFBoolean getUse_Std_Css_Theme() {
        return use_Std_Css_Theme;
    }


    /**
     * Setter for property use_Std_Css_Theme.
     * @param use_Std_Css_Theme New value of property use_Std_Css_Theme.
     */
    public void setUse_Std_Css_Theme(Boolean use_Std_Css_Theme) throws DFException {
        this.use_Std_Css_Theme.setValue(use_Std_Css_Theme);
    }


    /**
     * Getter for property custom_Css_Theme_Name.
     * @return Value of property custom_Css_Theme_Name.
     */
    public DFString getCustom_Css_Theme_Name() {
        return custom_Css_Theme_Name;
    }


    /**
     * Setter for property custom_Css_Theme_Name.
     * @param custom_Css_Theme_Name New value of property custom_Css_Theme_Name.
     */
    public void setCustom_Css_Theme_Name(String custom_Css_Theme_Name) throws DFException {
        this.custom_Css_Theme_Name.setValue(custom_Css_Theme_Name);
    }


    /**
     * Getter for property customer_Or_Vendor_Comp_Id.
     * @return Value of property customer_Or_Vendor_Comp_Id.
     */
    public DFString_Filled getCustomer_Or_Vendor_Comp_Id() {
        return customer_Or_Vendor_Comp_Id;
    }


    /**
     * Setter for property customer_Or_Vendor_Comp_Id.
     * @param customer_Or_Vendor_Comp_Id New value of property customer_Or_Vendor_Comp_Id.
     */
    public void setCustomer_Or_Vendor_Comp_Id(String customer_Or_Vendor_Comp_Id) throws DFException {
        this.customer_Or_Vendor_Comp_Id.setValue(customer_Or_Vendor_Comp_Id);
    }


    /**
     * Getter for property kiosk_Mode.
     * @return Value of property kiosk_Mode.
     */
    public DFBoolean getKiosk_Mode() {
        return kiosk_Mode;
    }


    /**
     * Setter for property kiosk_Mode.
     * @param kiosk_Mode New value of property kiosk_Mode.
     */
    public void setKiosk_Mode(Boolean kiosk_Mode) throws DFException {
        this.kiosk_Mode.setValue(kiosk_Mode);
    }


    //============== End jdbr Generated Section ===========================




  
    public static Subscriber_Organization_Portal get_Subscriber_Organization_Portal(Subscriber_Organization subscriber_Organization, String subscriber_Organization_Portal_Name, Database_Front database_Front)
    throws Database_Front_Exception, Database_Record_Constructor_Exception, Database_Front_No_Results_Exception {
        Subscriber_Organization_Portal subscriber_Organization_Portal=null;
        boolean debug_Mode=true;
        String message ="";
        
        try {
            subscriber_Organization_Portal = (Subscriber_Organization_Portal) database_Front.getRecord(new Subscriber_Organization_Portal(subscriber_Organization.getSubscriber_Organization_Id().toString(), subscriber_Organization_Portal_Name));
        }
        catch(Database_Front_Exception dbfe){
            if(debug_Mode){
                message = "Failed to find Subscriber_Organization_Portal in database. Database_Front_Exception :" + dbfe.getMessage();
                throw new Database_Front_Exception(message);
            }else{
                throw dbfe;
            }
        }
        catch(Database_Front_No_Results_Exception dbfe){
            if(debug_Mode){
                message = "Failed to find Subscriber_Organization_Portal in database. Database_Front_No_Results_Exception :" + dbfe.getMessage();
                throw new Database_Front_No_Results_Exception(message);
            }else{
                throw dbfe;
            }
        }
        
        
        return subscriber_Organization_Portal;
    }
    
     
    
    /** writes this Subscriber_Organization_Portal to session attribute  "i_Am_Subscriber_Organization_Portal"  */
    public void set_Session_Instance(HttpSession session){
        session.setAttribute("i_Am_Subscriber_Organization_Portal", this);
    }
    
    /**
     * Reads User from session attribute "i_Am_Subscriber_Organization_Portal".
     * @return A Subscriber_Organization_Portal instance of logged in user.
     * @throws if the session in invalid or
     * if no user has logged in.
     */
    public static Subscriber_Organization_Portal get_Session_Instance(HttpSession session)
    throws Database_Record_Exception {
        Subscriber_Organization_Portal temp_Subscriber_Organization_Portal;
        try {
            temp_Subscriber_Organization_Portal = (Subscriber_Organization_Portal) session.getAttribute("i_Am_Subscriber_Organization_Portal");
        }
        catch(IllegalStateException ise){
            throw new Database_Record_Exception("Your Session has Timed Out.");
        }
        if(temp_Subscriber_Organization_Portal == null){
            throw new Database_Record_Exception("No Portal has been selected for this session.");
        }
        return temp_Subscriber_Organization_Portal;
    }
    


    /**
     * Getter for property image_FullPath.
     * @return Value of property image_FullPath.
     */
    public String getImage_FullPath() {
        return this.web_Root.getValue() + this.image_Root.getValue();
    }
    
    
    
}
