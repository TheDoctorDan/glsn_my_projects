/*
 * User_AutoCompletion.java
 *
 * Created on November 21, 2006, 2:13 PM
 */

package com.amlibtech.site_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.login.data.*;
import com.amlibtech.util.*;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import com.amlibtech.web_Processes.*;

/**
 *
 * @author  dgleeson
 * @version
 */
public class User_AutoCompletion extends HttpServlet {

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
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "User AutoCompletion Servlet";
    }
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        Web_Process web_Process;
        try {
            web_Process = Web_Process.get_For_Servlet(this, request, response);
        }
        catch(WPException wpe){
            Launcher_Servlet.show_Error(this, request, response, wpe.getMessage(), wpe);
            return;
        }
        
        
        
        
        String action = request.getParameter("action");
        if(action == null){
            web_Process.log("no action passed on call.");
            do_Respond_Invalid(response);
            return;
        }
        
        
        
        if(action.equals("nameLookup")){
            
            String form_Field_Id_Of_Name = request.getParameter("form_Field_Id_Of_Name");
            if(form_Field_Id_Of_Name == null){
                web_Process.log("no form_Field_Id_Of_Name passed on call.");
                do_Respond_Invalid(response);
                return;
            }
            
            
            User temp_User;
            try {
                temp_User = new User();
            }catch(Database_Record_Constructor_Exception dbre){
                web_Process.log("Database Record Error.");
                do_Respond_Invalid(response);
                return;
            }
            
            
            
            
            
            String last_Name=null;
            String first_Name=null;
            String[] names;
            
            String user_Name = request.getParameter(form_Field_Id_Of_Name);
            if (user_Name != null) {
                if(user_Name.indexOf(",")<0){
                    last_Name = user_Name;
                }else{
                    names = user_Name.split(",");
                    last_Name=names[0].trim();
                    if(names.length>1)
                        first_Name=names[1].trim();
                }
            }
            
            
            try {
                if(last_Name != null)
                    temp_User.setLast_Name(last_Name);
                if(first_Name != null)
                    temp_User.setFirst_Name(first_Name);
            }catch(DFException dfe){
                web_Process.log("User_AutoCompletion  : " + dfe.getMessage());
                do_Respond_Invalid(response);
                return;
            }
            
            
            String where_Clause = "";
            if(!web_Process.getUser().getAsp_Admin().getValue().booleanValue()){
                where_Clause = where_Clause + " AND " + temp_User.getAsp_Admin().toWhere_Clause();
            }
            if(!temp_User.getLast_Name().getValue().trim().equals("")){
                where_Clause = where_Clause +  " AND " + temp_User.getLast_Name().toWhere_Range_Clause();
                if(!temp_User.getFirst_Name().getValue().trim().equals(""))
                    where_Clause = where_Clause + " AND " + temp_User.getFirst_Name().toWhere_Range_Clause();
            }
            
            if(where_Clause.startsWith(" AND "))
                where_Clause = "WHERE " + where_Clause.substring(4);
            
            
            String sort_Clause = " ORDER BY " +
            temp_User.getLast_Name().getField_Name() + " ASC , " +
            temp_User.getFirst_Name().getField_Name() + " ASC ";
            
            
            
            
            User[] users;
            try {
                users = (User[]) web_Process.getDatabase_Front().getMany_Records(temp_User,
                where_Clause +
                sort_Clause
                , 0, 15);
            }
            catch(Database_Front_Exception dbre){
                web_Process.log("Database Error.");
                do_Respond_Invalid(response);
                return;
            }
            if(users != null){
                StringBuffer sb = new StringBuffer();
                for(int i=0;i<users.length;i++){
                    sb.append("<user>");
                    sb.append("<id>" + StringPlus.XML_Encode(users[i].getUser_Id().toString()) + "</id>");
                    sb.append("<name>" + StringPlus.XML_Encode((users[i].getName_Last_Then_First().toString())) + "</name>");
                    if(users[i].getEmail_Address().getValue().equals(""))
                        sb.append("<email>" + StringPlus.XML_Encode(" ") + "</email>");
                    else
                        sb.append("<email>" + StringPlus.XML_Encode(users[i].getEmail_Address().toString()) + "</email>");
                    sb.append("</user>");
                    
                    
                }
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                response.getWriter().write("<users>" + sb.toString() + "</users>");
                
                response.setStatus(HttpServletResponse.SC_OK);
                
                
            }else{
                do_Respond_Invalid(response);
            }
            
        }else if(action.equals("find")){
            String form_Field_Id_Of_Id = request.getParameter("form_Field_Id_Of_Id");
            if(form_Field_Id_Of_Id == null){
                web_Process.log("no form_Field_Id_Of_Id passed on call.");
                do_Respond_Invalid(response);
                return;
            }
            
            String user_Id = request.getParameter(form_Field_Id_Of_Id);
            if (user_Id == null) {
                do_Respond_Invalid(response);
                return;
            }
            
            
            
            User user;
            try {
                user = (User) web_Process.getDatabase_Front().getRecord(new User(user_Id));
            }catch(Exception ex){
                do_Respond_Invalid(response);
                return;
            }
            
            
            
            
            StringBuffer sb = new StringBuffer();
            sb.append("<user>");
            sb.append("<id>" + StringPlus.XML_Encode(user.getUser_Id().toString()) + "</id>");
            sb.append("<name>" + StringPlus.XML_Encode((user.getName_Last_Then_First().toString())) + "</name>");
            if(user.getEmail_Address().getValue().equals(""))
                sb.append("<email>" + StringPlus.XML_Encode(" ") + "</email>");
            else
                sb.append("<email>" + StringPlus.XML_Encode(user.getEmail_Address().toString()) + "</email>");
            sb.append("</user>");
            
            
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<users>" + sb.toString() + "</users>");
            
            response.setStatus(HttpServletResponse.SC_OK);
            
            
            
            
            
        }else{
            web_Process.log("unknown action :"+action+":");
            do_Respond_Invalid(response);
        }
        
        
    }
    
    
    
    void do_Respond_Invalid(HttpServletResponse response)
    throws ServletException, IOException {
        //nothing to show
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
    
    
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    
    
    public static void getJavaScript(PrintWriter out, Web_Process web_Process, String form_Name,
    String form_Field_Id_Of_Id, String form_Field_Id_Of_Name, String form_Field_Id_Of_Email,
    HttpServlet httpServlet, String maint_Action_Method)
    throws WPFatal_Exception {
        
        web_Process.curStatus_Add_Action_Node(new WPAction_Node("Maint.", httpServlet, maint_Action_Method));
        getJavaScript(out, web_Process, form_Name,
        form_Field_Id_Of_Id, form_Field_Id_Of_Name, form_Field_Id_Of_Email);
    }
    
    public static void getJavaScript(PrintWriter out, Web_Process web_Process, String form_Name,
    String form_Field_Id_Of_Id, String form_Field_Id_Of_Name, String form_Field_Id_Of_Email,
    Class passed_Class_Obj, String maint_Action_Method)
    throws WPFatal_Exception {
        
        web_Process.curStatus_Add_Action_Node(new WPAction_Node("Maint.", passed_Class_Obj, maint_Action_Method));
        getJavaScript(out, web_Process, form_Name,
        form_Field_Id_Of_Id, form_Field_Id_Of_Name, form_Field_Id_Of_Email);
    }
    
    
    private static void getJavaScript(PrintWriter out, Web_Process web_Process, String form_Name,
    String form_Field_Id_Of_Id, String form_Field_Id_Of_Name, String form_Field_Id_Of_Email){
        // <body  onLoad=form_Field_Id_Of_Id_Init()
        // <input name-user_Name ... onKeyUp=form_Field_Id_Of_Id_DoCompletion()
        
        out.println("<script type='text/JavaScript'>");
        out.println("");
        
        
        out.println("var " + form_Field_Id_Of_Id + "_CompleteTable;");
        
        out.println("var " + form_Field_Id_Of_Name + "_CompleteField;");
        if(form_Field_Id_Of_Email != null)
            out.println("var " + form_Field_Id_Of_Email + "_CompleteField;");
        
        out.println("var " + form_Field_Id_Of_Id + "_AutoRow;");
        out.println("var " + form_Field_Id_Of_Id + "_FindField;");
        
        
        out.println("function " + form_Field_Id_Of_Id + "_Init() {");
        out.println("    //alert(\"" + form_Field_Id_Of_Id + "_Init\");");
        
        out.println("    " + form_Field_Id_Of_Name + "_CompleteField = document.getElementById(\""+form_Field_Id_Of_Name+"\");");
        if(form_Field_Id_Of_Email != null)
            out.println("    " + form_Field_Id_Of_Email + "_CompleteField = document.getElementById(\""+form_Field_Id_Of_Email+"\");");
        
        out.println("    " + form_Field_Id_Of_Id + "_FindField = document.getElementById(\""+form_Field_Id_Of_Id+"\");");
        
        out.println("    var menu = document.getElementById(\"" + form_Field_Id_Of_Id + "_auto-row\");");
        out.println("    " + form_Field_Id_Of_Id + "_AutoRow = document.getElementById(\"" + form_Field_Id_Of_Id + "_menu-popup\");");
        out.println("    " + form_Field_Id_Of_Id + "_AutoRow.style.top = getElementY(menu) + \"px\";");
        out.println("    " + form_Field_Id_Of_Id + "_CompleteTable = document.getElementById(\"" + form_Field_Id_Of_Id + "_CompleteTable\");");
        out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("}");
        out.println("");
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_DoCompletion() {");
        out.println("    if (" + form_Field_Id_Of_Name + "_CompleteField.value == \"\") {");
        out.println("        " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    } else {");
        out.println("        var url = \"User_AutoCompletion"+web_Process.get_HTML_Of_Href_Call_Parameters()+
        "&action=nameLookup"+
        "&form_Field_Id_Of_Name=\"+escape(\""+form_Field_Id_Of_Name+"\")+\""+
        "&"+form_Field_Id_Of_Name+"=\"+escape(" + form_Field_Id_Of_Name + "_CompleteField.value);");
        
        out.println("        var ajax = new " + form_Field_Id_Of_Id + "_AJAXInteractionForNameLookup(url);");
        out.println("        ajax.send();");
        
        out.println("    }");
        out.println("}");
        out.println("");
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_AJAXInteractionForNameLookup(url) {");
        out.println("    this.url = url;");
        out.println("    var req = req_Init();");
        out.println("    req.onreadystatechange = processRequest;");
        out.println("");
        out.println("");
        out.println("    function req_Init() {");
        out.println("      if (window.XMLHttpRequest) {");
        out.println("        return new XMLHttpRequest();");
        out.println("      } else if (window.ActiveXObject) {");
        out.println("        return new ActiveXObject(\"Microsoft.XMLHTTP\");");
        out.println("      }");
        out.println("    }");
        out.println("");
        out.println("    function processRequest () {");
        out.println("");
        out.println("      if (req.readyState == 4) {");
        out.println("        if (req.status == 200) {");
        out.println("		//alert(\"state is 4  status is 200  responseXML : \" + req.responseXML + \"  responseText :\" + req.responseText);");
        out.println("          	" + form_Field_Id_Of_Id + "_PostProcessNameLookup(req.responseXML);");
        out.println("        }else{");
        out.println("        	//alert(\"state: is 4  status:\" + req.status);");
        out.println("            " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("		;");
        out.println("        }");
        out.println("      }else{");
        out.println("	//alert(\"state:\" + req.readystate +\"  status :\" + req.status );");
        out.println("	;");
        out.println("      }");
        out.println("    }");
        out.println("");
        out.println("");
        out.println("    this.send = function() {");
        out.println("        //req.setRequestHeader(\"Content-Type\", \"application/x-www-form-urlencoded\");");
        out.println("        req.open(\"GET\", url, true);");
        out.println("        req.send(null);");
        out.println("    }");
        out.println("}");
        out.println("");
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_ClearTable() {");
        out.println("    //alert(\"" + form_Field_Id_Of_Id + "_ClearTable\");");
        out.println("    if (" + form_Field_Id_Of_Id + "_CompleteTable) {");
        out.println("      " + form_Field_Id_Of_Id + "_CompleteTable.setAttribute(\"bordercolor\", \"white\");");
        out.println("      " + form_Field_Id_Of_Id + "_CompleteTable.setAttribute(\"border\", \"0\");");
        out.println("      " + form_Field_Id_Of_Id + "_CompleteTable.style.visible = false;");
        out.println("      for (loop = " + form_Field_Id_Of_Id + "_CompleteTable.childNodes.length -1; loop >= 0 ; loop--) {");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.removeChild(" + form_Field_Id_Of_Id + "_CompleteTable.childNodes[loop]);");
        out.println("      }");
        out.println("    }");
        out.println("}");
        out.println("");
        
        
        out.println("function " + form_Field_Id_Of_Id + "_PostProcessNameLookup(responseXML) {");
        out.println("    //alert(\"" + form_Field_Id_Of_Id + "_PostProcessNameLookup : responseXML=\"+responseXML);");
        out.println("");
        out.println("	var users = responseXML.getElementsByTagName(\"users\")[0];");
        out.println("	//alert(\"users.childNodes.length=\"+ users.childNodes.length);");
        out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    if (users.childNodes.length > 0) {");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.setAttribute(\"bordercolor\", \"black\");");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.setAttribute(\"border\", \"1\");");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.style.visible = true; ");
        out.println("    } else {");
        out.println("        " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    }");
        out.println("");
        out.println("    //alert(\"users : \" + users);");
        out.println("");
        out.println("    for (loop = 0; loop < users.childNodes.length; loop++) {");
        out.println("	var user = users.childNodes[loop];");
        out.println("        //alert(\"loop : \" + loop + \"  user=\" + user.value);");
        out.println("        var id = user.getElementsByTagName(\"id\")[0];");
        out.println("        var name = user.getElementsByTagName(\"name\")[0];");
        out.println("        var email = user.getElementsByTagName(\"email\")[0];");
        out.println("        //alert(\"name=\"+name);");
        out.println("        " + form_Field_Id_Of_Id + "_Append(id.childNodes[0].nodeValue, name.childNodes[0].nodeValue, email.childNodes[0].nodeValue, loop);");
        out.println("    }");
        out.println("    " + form_Field_Id_Of_Id + "_Append(\"Maint.\", \"\", \"\", -1);");
        out.println("    " + form_Field_Id_Of_Id + "_Append(\"Close\", \"\", \"\", -2);");
        out.println("");
        out.println("}");
        out.println("");
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_Append(id, name, email, loop) {");
        out.println("    //alert(\"" + form_Field_Id_Of_Id + "_Append\");");
        out.println("    var row;");
        out.println("    var nameCell;");
        out.println("    if (autoComplete_bool_isIE) {");
        out.println("        row = " + form_Field_Id_Of_Id + "_CompleteTable.insertRow(" + form_Field_Id_Of_Id + "_CompleteTable.rows.length);");
        out.println("        nameCell = row.insertCell(0);");
        out.println("    } else {");
        out.println("        row = document.createElement(\"tr\");");
        out.println("        nameCell = document.createElement(\"td\");");
        out.println("        row.appendChild(nameCell);");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.appendChild(row);");
        out.println("    }");
        out.println("    row.className = \"popupRow\";");
        out.println("    if(loop%2==0)");
        out.println("    	nameCell.setAttribute(\"bgcolor\", \"#FFFAFA\");");
        out.println("    else");
        out.println("        nameCell.setAttribute(\"bgcolor\", \"#FFFAAA\");");
        out.println("");
        
        out.println("    if(loop==-1){");
        
        out.println("        var linkElement = document.createElement(\"input\");");
        out.println("        linkElement.className = \"popupItem\";");
        out.println("        linkElement.setAttribute(\"type\", \"hidden\");");
        out.println("        linkElement.setAttribute(\"name\", \"maint\");");
        out.println("        linkElement.setAttribute(\"value\", \"user\");");
        out.println("        nameCell.appendChild(linkElement);");
        
        
        out.println("        var linkElement = document.createElement(\"input\");");
        out.println("        linkElement.className = \"popupItem\";");
        out.println("        linkElement.setAttribute(\"type\", \"submit\");");
        out.println("        linkElement.setAttribute(\"name\", \"action\");");
        out.println("        linkElement.setAttribute(\"value\", \"Maint.\");");
        out.println("        nameCell.appendChild(linkElement);");
        
        out.println("    } else if(loop == -2){");
        
        out.println("        var linkElement = document.createElement(\"input\");");
        out.println("        linkElement.className = \"popupItem\";");
        out.println("        linkElement.setAttribute(\"type\", \"button\");");
        out.println("        linkElement.setAttribute(\"value\", \"Close\");");
        out.println("        linkElement.setAttribute(\"onclick\", \"" + form_Field_Id_Of_Id + "_ClearTable()\");");
        out.println("        nameCell.appendChild(linkElement);");
        
        out.println("    } else{");
        
        out.println("        var linkElement = document.createElement(\"a\");");
        out.println("        linkElement.className = \"popupItem\";");
        
        if(form_Field_Id_Of_Email != null)
            out.println("        linkElement.setAttribute(\"href\", \"javascript:"+form_Field_Id_Of_Name+"_And_Email_Load('\" + id + \"', '\" + My_Escape(name) + \"', '\" + My_Escape(email) + \"')\");");
        else
            out.println("        linkElement.setAttribute(\"href\", \"javascript:"+form_Field_Id_Of_Name+"_Load('\" + id + \"', '\" + My_Escape(name) + \"')\");");
        
        //out.println("        linkElement.appendChild(document.createTextNode(id +\", \" + name));");
        out.println("        linkElement.appendChild(document.createTextNode(name +\", \" + email +\", (\" + id + \")\"));");
        out.println("        nameCell.appendChild(linkElement);");
        
        out.println("    }");
        
        out.println("}");
        out.println("");
        
        
        
        if(form_Field_Id_Of_Email != null){
            
            out.println("function "+form_Field_Id_Of_Name+"_And_Email_Load(id, name, email) {");
            out.println("    document."+form_Name + "."+form_Field_Id_Of_Id+".value = id;");
            out.println("    document."+form_Name + "."+form_Field_Id_Of_Name+".value = name;");
            out.println("    document."+form_Name + "."+form_Field_Id_Of_Email+".value = email;");
            out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
            out.println("}");
            out.println("");
            
        }else {
            
            
            
            out.println("function "+form_Field_Id_Of_Name+"_Load(id, name) {");
            out.println("    document."+form_Name + "."+form_Field_Id_Of_Id+".value = id;");
            out.println("    document."+form_Name + "."+form_Field_Id_Of_Name+".value = name;");
            out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
            out.println("}");
            out.println("");
            
        }
        
        
        out.println("function " + form_Field_Id_Of_Id + "_DoFind() {");
        out.println("    if (" + form_Field_Id_Of_Id + "_FindField.value == \"\") {");
        out.println("        " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    } else {");
        out.println("        var url = \"User_AutoCompletion"+web_Process.get_HTML_Of_Href_Call_Parameters()+
        "&action=find"+
        "&form_Field_Id_Of_Id=\"+escape(\""+form_Field_Id_Of_Id+"\")+\""+
        "&"+form_Field_Id_Of_Id+"=\"+escape(" + form_Field_Id_Of_Id + "_FindField.value);");
        
        out.println("        var ajax = new " + form_Field_Id_Of_Id + "_AJAXInteractionForFind(url);");
        out.println("        ajax.send();");
        
        out.println("    }");
        out.println("}");
        out.println("");
        
        
        
        
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_AJAXInteractionForFind(url) {");
        out.println("    this.url = url;");
        out.println("    var req = req_Init();");
        out.println("    req.onreadystatechange = processRequest;");
        out.println("");
        out.println("");
        out.println("    function req_Init() {");
        out.println("      if (window.XMLHttpRequest) {");
        out.println("        return new XMLHttpRequest();");
        out.println("      } else if (window.ActiveXObject) {");
        out.println("        return new ActiveXObject(\"Microsoft.XMLHTTP\");");
        out.println("      }");
        out.println("    }");
        out.println("");
        out.println("    function processRequest () {");
        out.println("");
        out.println("      if (req.readyState == 4) {");
        out.println("        if (req.status == 200) {");
        out.println("		//alert(\"state is 4  status is 200  responseXML : \" + req.responseXML + \"  responseText :\" + req.responseText);");
        out.println("          	" + form_Field_Id_Of_Id + "_PostProcessFind(req.responseXML);");
        out.println("        }else{");
        out.println("        	//alert(\"state: is 4  status:\" + req.status);");
        out.println("    document."+form_Name + "."+form_Field_Id_Of_Name+".value = \"No Such User\";");
        
        out.println("		;");
        out.println("        }");
        out.println("      }else{");
        out.println("	//alert(\"state:\" + req.readystate +\"  status :\" + req.status );");
        out.println("    document."+form_Name + "."+form_Field_Id_Of_Name+".value = \"No Such User\";");
        
        out.println("	;");
        out.println("      }");
        out.println("    }");
        out.println("");
        out.println("");
        out.println("    this.send = function() {");
        out.println("        //req.setRequestHeader(\"Content-Type\", \"application/x-www-form-urlencoded\");");
        out.println("        req.open(\"GET\", url, true);");
        out.println("        req.send(null);");
        out.println("    }");
        out.println("}");
        out.println("");
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_PostProcessFind(responseXML) {");
        out.println("    //alert(\"" + form_Field_Id_Of_Id + "_PostProcessFind : responseXML=\"+responseXML);");
        out.println("");
        out.println("	var users = responseXML.getElementsByTagName(\"users\")[0];");
        out.println("	//alert(\"users.childNodes.length=\"+ users.childNodes.length);");
        out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
        
        out.println("");
        out.println("    //alert(\"users : \" + users);");
        out.println("");
        out.println("    for (loop = 0; loop < users.childNodes.length; loop++) {");
        out.println("	var user = users.childNodes[loop];");
        out.println("        //alert(\"loop : \" + loop + \"  user=\" + user.value);");
        out.println("        var id = user.getElementsByTagName(\"id\")[0];");
        out.println("        var name = user.getElementsByTagName(\"name\")[0];");
        out.println("        var email = user.getElementsByTagName(\"email\")[0];");
        out.println("        //alert(\"name=\"+name);");
        //out.println("        " + form_Field_Id_Of_Id + "_Append(id.childNodes[0].nodeValue, name.childNodes[0].nodeValue, email.childNodes[0].nodeValue, loop);");
        out.println("    document."+form_Name + "."+form_Field_Id_Of_Id+".value = id.childNodes[0].nodeValue;");
        out.println("    document."+form_Name + "."+form_Field_Id_Of_Name+".value = name.childNodes[0].nodeValue;");
        if(form_Field_Id_Of_Email != null)
            out.println("    document."+form_Name + "."+form_Field_Id_Of_Email+".value = email.childNodes[0].nodeValue;");
        
        
        out.println("    }");
        
        
        out.println("");
        out.println("}");
        out.println("");
        
        
        
        
        out.println("</script>");
        
    }
    
    
    
    
    public static void getDivTable_Static(PrintWriter out, String form_Field_Id_Of_Id){
        out.println("<div style='position: static; width: 800px; font-user: Tahoma, Helvetica, sans, Arial, sans-serif; font-size: 12px' id='" + form_Field_Id_Of_Id + "_menu-popup'>");
        out.println("<table id='" + form_Field_Id_Of_Id + "_CompleteTable' border='1' bordercolor='black' cellpadding='0' cellspacing='0' >");
        out.println("<tr><td id='" + form_Field_Id_Of_Id + "_auto-row' colspan='2'>&nbsp;</td></tr>");
        out.println("</table></div>");
        
        //out.println("<div id=\"hiddenDIV2\" style=\"position:relative; top: -22px; left: 85px;width: 200; visibility:visible; background-color:white; border: 1px solid black;\"> </div>");
    }
    
    
}
