/*
 * Security_Profile_AutoCompletion.java
 *
 * Created on November 15, 2006, 9:19 PM
 */

package com.amlibtech.security_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.security_Management.data.*;
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
public class Security_Profile_AutoCompletion extends HttpServlet {

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
        return "Security Profile AutoCompletion Servlet";
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
        
        
        
        
        
        String form_Field_Id_Of_Id = request.getParameter("form_Field_Id_Of_Id");
        if(form_Field_Id_Of_Id == null){
            web_Process.log("no form_Field_Id_Of_Id passed on call.");
            do_Respond_Invalid(response);
        }
        
        Security_Profile temp_Security_Profile;
        try {
            temp_Security_Profile = new Security_Profile();
        }catch(Database_Record_Constructor_Exception dbre){
            web_Process.log("Database Record Error.");
            do_Respond_Invalid(response);
            return;
        }
        
        try {
            String security_Profile_Id = request.getParameter(form_Field_Id_Of_Id);
            if (security_Profile_Id != null)
                temp_Security_Profile.setSecurity_Profile_Id(security_Profile_Id);
        }catch(DFException dfe){
            web_Process.log("Security_Profile_AutoCompletion  : " + dfe.getMessage());
            do_Respond_Invalid(response);
            return;
        }
        
        String where_Clause = "";
        
        if(!temp_Security_Profile.getSecurity_Profile_Id().getValue().trim().equals(""))
            where_Clause = " WHERE " + temp_Security_Profile.getSecurity_Profile_Id().toWhere_Range_Clause();
        
        String sort_Clause = " ORDER BY " +
        temp_Security_Profile.getSecurity_Profile_Id().getField_Name().toString() + " ASC ";
        
        
        Security_Profile[] security_Profiles;
        try {
            security_Profiles = (Security_Profile[]) web_Process.getDatabase_Front().getMany_Records(temp_Security_Profile, where_Clause +
            sort_Clause, 0, 20);
        }
        catch(Database_Front_Exception dbre){
            web_Process.log("Database Error.");
            do_Respond_Invalid(response);
            return;
        }
        if(security_Profiles != null){
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<security_Profiles.length;i++){
                sb.append("<security_Profile>");
                sb.append("<id>" + StringPlus.XML_Encode(security_Profiles[i].getSecurity_Profile_Id().toString()) + "</id>");
                sb.append("</security_Profile>");
                
                
            }
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<security_Profiles>" + sb.toString() + "</security_Profiles>");
            
            response.setStatus(HttpServletResponse.SC_OK);
            
            
        }else{
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
    String form_Field_Id_Of_Id,
    HttpServlet httpServlet, String maint_Action_Method)
    throws WPFatal_Exception {
        
        web_Process.curStatus_Add_Action_Node(new WPAction_Node("Maint.", httpServlet, maint_Action_Method));
        getJavaScript(out, web_Process, form_Name,
        form_Field_Id_Of_Id);
    }
    
    public static void getJavaScript(PrintWriter out, Web_Process web_Process, String form_Name,
    String form_Field_Id_Of_Id,
    Class passed_Class_Obj, String maint_Action_Method)
    throws WPFatal_Exception {
        
        web_Process.curStatus_Add_Action_Node(new WPAction_Node("Maint.", passed_Class_Obj, maint_Action_Method));
        getJavaScript(out, web_Process, form_Name,
        form_Field_Id_Of_Id);
    }
    
    
    
    
    
    private static void getJavaScript(PrintWriter out, Web_Process web_Process, String form_Name,
    String form_Field_Id_Of_Id){
        // <body  onLoad=form_Field_Id_Of_Id_Init()
        // <input name-security_Profile_Id ... onKeyUp=form_Field_Id_Of_Id_DoCompletion()
        
        out.println("<script type='text/JavaScript'>");
        out.println("");
        
        
        out.println("var " + form_Field_Id_Of_Id + "_CompleteTable;");
        out.println("var " + form_Field_Id_Of_Id + "_CompleteField;");
        out.println("var " + form_Field_Id_Of_Id + "_AutoRow;");
        
        
        out.println("function " + form_Field_Id_Of_Id + "_Init() {");
        out.println("    //alert(\"" + form_Field_Id_Of_Id + "_Init\");");
        out.println("    " + form_Field_Id_Of_Id + "_CompleteField = document.getElementById(\""+form_Field_Id_Of_Id+"\");");
        
        out.println("    var menu = document.getElementById(\"" + form_Field_Id_Of_Id + "_auto-row\");");
        out.println("    " + form_Field_Id_Of_Id + "_AutoRow = document.getElementById(\"" + form_Field_Id_Of_Id + "_menu-popup\");");
        out.println("    " + form_Field_Id_Of_Id + "_AutoRow.style.top = getElementY(menu) + \"px\";");
        out.println("    " + form_Field_Id_Of_Id + "_CompleteTable = document.getElementById(\"" + form_Field_Id_Of_Id + "_CompleteTable\");");
        out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("}");
        out.println("");
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_DoCompletion() {");
        out.println("    if (" + form_Field_Id_Of_Id + "_CompleteField.value == \"\") {");
        out.println("        " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    } else {");
        out.println("        var url = \"Security_Profile_AutoCompletion"+web_Process.get_HTML_Of_Href_Call_Parameters()+
        "&form_Field_Id_Of_Id=\"+escape(\""+form_Field_Id_Of_Id+"\")+\""+
        "&"+form_Field_Id_Of_Id+"=\"+escape(" + form_Field_Id_Of_Id + "_CompleteField.value);");
        
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
        out.println("	var security_Profiles = responseXML.getElementsByTagName(\"security_Profiles\")[0];");
        out.println("	//alert(\"security_Profiles.childNodes.length=\"+ security_Profiles.childNodes.length);");
        out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    if (security_Profiles.childNodes.length > 0) {");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.setAttribute(\"bordercolor\", \"black\");");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.setAttribute(\"border\", \"1\");");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.style.visible = true; ");
        out.println("    } else {");
        out.println("        " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    }");
        out.println("");
        out.println("    //alert(\"security_Profiles : \" + security_Profiles);");
        out.println("");
        out.println("    for (loop = 0; loop < security_Profiles.childNodes.length; loop++) {");
        out.println("	var security_Profile = security_Profiles.childNodes[loop];");
        out.println("        //alert(\"loop : \" + loop + \"  security_Profile=\" + security_Profile.value);");
        out.println("        var id = security_Profile.getElementsByTagName(\"id\")[0];");
        out.println("        //alert(\"name=\"+name);");
        out.println("        " + form_Field_Id_Of_Id + "_Append(id.childNodes[0].nodeValue, loop);");
        out.println("    }");
        out.println("    " + form_Field_Id_Of_Id + "_Append(\"Maint.\",  -1);");
        out.println("    " + form_Field_Id_Of_Id + "_Append(\"Close\",  -2);");
        out.println("");
        out.println("}");
        out.println("");
        
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_Append(id, loop) {");
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
        out.println("        linkElement.setAttribute(\"value\", \"security_Profile\");");
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
        //out.println("        linkElement.className = \"popupItem\";");
        out.println("        linkElement.setAttribute(\"href\", \"javascript:"+form_Field_Id_Of_Id+"_Load('\" + id + \"')\");");
        out.println("        linkElement.appendChild(document.createTextNode(id));");
        out.println("        nameCell.appendChild(linkElement);");
        
        out.println("    }");
        
        out.println("}");
        out.println("");
        
        
        
        out.println("function "+form_Field_Id_Of_Id+"_Load(id) {");
        out.println("    document."+form_Name + "."+form_Field_Id_Of_Id+".value = id;");
        out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("}");
        out.println("");
        
        
        
        
        out.println("</script>");
        
    }
    
    
    public static void getDivTable(PrintWriter out, int top, int left, String form_Field_Id_Of_Id){
        out.println("<div style='position: absolute; top:" + top + "px;left:" + left + "px; width: 300px; font-family: Tahoma, Helvetica, sans, Arial, sans-serif; font-size: 12px' id='" + form_Field_Id_Of_Id + "_menu-popup'>");
        out.println("<table id='" + form_Field_Id_Of_Id + "_CompleteTable' border='1' bordercolor='black' cellpadding='0' cellspacing='0' >");
        out.println("<tr><td id='" + form_Field_Id_Of_Id + "_auto-row' colspan='2'>&nbsp;</td></tr>");
        out.println("</table></div>");
        
        //out.println("<div id=\"hiddenDIV2\" style=\"position:relative; top: -22px; left: 85px;width: 200; visibility:visible; background-color:white; border: 1px solid black;\"> </div>");
    }
    
    public static void getDivTable_Static(PrintWriter out, String form_Field_Id_Of_Id){
        out.println("<div style='position: static; width: 800px; font-family: Tahoma, Helvetica, sans, Arial, sans-serif; font-size: 12px' id='" + form_Field_Id_Of_Id + "_menu-popup'>");
        out.println("<table id='" + form_Field_Id_Of_Id + "_CompleteTable' border='1' bordercolor='black' cellpadding='0' cellspacing='0' >");
        out.println("<tr><td id='" + form_Field_Id_Of_Id + "_auto-row' colspan='2'>&nbsp;</td></tr>");
        out.println("</table></div>");
        
        //out.println("<div id=\"hiddenDIV2\" style=\"position:relative; top: -22px; left: 85px;width: 200; visibility:visible; background-color:white; border: 1px solid black;\"> </div>");
    }
    
    
}
