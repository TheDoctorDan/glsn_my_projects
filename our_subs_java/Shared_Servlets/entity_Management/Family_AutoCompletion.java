/*
 * Family_AutoCompletion.java
 *
 * Created on August 30, 2006, 12:12 AM
 */

package entity_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.util.*;
import com.amlibtech.web_Processes.*;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import entity_Management.datum.*;

/**
 *
 * @author  dgleeson
 * @version
 */
public class Family_AutoCompletion extends HttpServlet {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Family AutoCompletion Servlet";
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
        
        String comp_Id = request.getParameter("comp_Id");
        if(comp_Id == null){
            web_Process.log("no comp_Id passed on call.");
            do_Respond_Invalid(response);
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
            
            
            Family temp_Family;
            try {
                temp_Family = new Family();
            }catch(Database_Record_Constructor_Exception dbre){
                web_Process.log("Database Record Error.");
                do_Respond_Invalid(response);
                return;
            }
            try {
                temp_Family.setComp_Id(comp_Id);
            }catch(DFException dfe){
                web_Process.log("Family_AutoCompletion could not set Comp_Id. Because : " + dfe.getMessage());
                do_Respond_Invalid(response);
                return;
            }
            
            Person temp_Person;
            try {
                temp_Person = new Person();
            }catch(Database_Record_Constructor_Exception dbre){
                web_Process.log("Database Record Error.");
                do_Respond_Invalid(response);
                return;
            }
            
            
            String last_Name=null;
            String first_Name=null;
            String[] names;
            
            String family_Name = request.getParameter(form_Field_Id_Of_Name);
            if (family_Name != null) {
                if(family_Name.indexOf(",")<0){
                    last_Name = family_Name;
                }else{
                    names = family_Name.split(",");
                    last_Name=names[0].trim();
                    if(names.length>1)
                        first_Name=names[1].trim();
                }
            }
            
            
            try {
                temp_Person.setComp_Id(comp_Id);
                if(last_Name != null)
                    temp_Person.setLast_Name(last_Name);
                if(first_Name != null)
                    temp_Person.setFirst_Name(first_Name);
            }catch(DFException dfe){
                web_Process.log("Family_AutoCompletion  : " + dfe.getMessage());
                do_Respond_Invalid(response);
                return;
            }
            
            
            String where_Clause = " WHERE " +
            temp_Family.get_Table_Name() + "." + temp_Family.getComp_Id().toWhere_Clause();
            if(!temp_Person.getLast_Name().getValue().trim().equals(""))
                where_Clause = where_Clause + " AND " +  temp_Person.getLast_Name().toWhere_Range_Clause(temp_Person.get_Table_Name() + ".");
            if(!temp_Person.getFirst_Name().getValue().trim().equals(""))
                where_Clause = where_Clause + " AND " +temp_Person.getFirst_Name().toWhere_Range_Clause(temp_Person.get_Table_Name() + ".");
            
            
            String sort_Clause = " ORDER BY " +
            temp_Person.get_Table_Name() + "." + temp_Person.getLast_Name().getField_Name() + " ASC , " +
            temp_Person.get_Table_Name() + "." + temp_Person.getFirst_Name().getField_Name() + " ASC ";
            
            
            
            
            Family[] familys;
            try {
                familys = (Family[]) web_Process.getDatabase_Front().getMany_Records(temp_Family,
                " JOIN " + temp_Person.get_Table_Name() + " USING(" +
                temp_Family.getComp_Id().getField_Name()+", "+ temp_Family.getPerson_Number().getField_Name()+") " +
                where_Clause +
                sort_Clause
                , 0, 15);
            }
            catch(Database_Front_Exception dbre){
                web_Process.log("Database Error.");
                do_Respond_Invalid(response);
                return;
            }
            if(familys != null){
                StringBuffer sb = new StringBuffer();
                for(int i=0;i<familys.length;i++){
                    sb.append("<family>");
                    sb.append("<id>" + StringPlus.XML_Encode(familys[i].getFamily_Number().toString()) + "</id>");
                    Person person = Person.get_Possible_Person(web_Process.getDatabase_Front(), familys[i].getComp_Id().getValue(), familys[i].getPerson_Number().getValue());
                    sb.append("<name>" + StringPlus.XML_Encode((person.getName_Last_Then_First().toString())) + "</name>");
                    if(person.getCity().getValue().equals(""))
                        sb.append("<city>" + StringPlus.XML_Encode(" ") + "</city>");
                    else
                        sb.append("<city>" + StringPlus.XML_Encode(person.getCity().toString()) + "</city>");
                    sb.append("</family>");
                    
                    
                }
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                response.getWriter().write("<familys>" + sb.toString() + "</familys>");
                
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
            
            String family_Number = request.getParameter(form_Field_Id_Of_Id);
            if (family_Number == null) {
                do_Respond_Invalid(response);
                return;
            }
            
            
            
            Family family;
            try {
                family = (Family) web_Process.getDatabase_Front().getRecord(new Family(comp_Id, new Integer(family_Number)));
            }catch(Exception ex){
                do_Respond_Invalid(response);
                return;
            }
            
            Person person = Person.get_Possible_Person(web_Process.getDatabase_Front(), family.getComp_Id().getValue(), family.getPerson_Number().getValue());

            
            
            StringBuffer sb = new StringBuffer();
                    sb.append("<family>");
            sb.append("<id>" + StringPlus.XML_Encode(family.getFamily_Number().toString()) + "</id>");
            sb.append("<name>" + StringPlus.XML_Encode((person.getName_Last_Then_First().toString())) + "</name>");
            if(person.getCity().getValue().equals(""))
                sb.append("<city>" + StringPlus.XML_Encode(" ") + "</city>");
            else
                sb.append("<city>" + StringPlus.XML_Encode(person.getCity().toString()) + "</city>");
                    sb.append("</family>");
            
            
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
                response.getWriter().write("<familys>" + sb.toString() + "</familys>");
            
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
    String form_Field_Id_Of_Id, String form_Field_Id_Of_Name, String form_Field_Id_Of_Comp_Id,
    HttpServlet httpServlet, String maint_Action_Method)
    throws WPFatal_Exception {
        
        web_Process.curStatus_Add_Action_Node(new WPAction_Node("Maint.", httpServlet, maint_Action_Method));
        getJavaScript(out, web_Process, form_Name,
        form_Field_Id_Of_Id, form_Field_Id_Of_Name, form_Field_Id_Of_Comp_Id);
    }
    
    public static void getJavaScript(PrintWriter out, Web_Process web_Process, String form_Name,
    String form_Field_Id_Of_Id, String form_Field_Id_Of_Name, String form_Field_Id_Of_Comp_Id,
    Class passed_Class_Obj, String maint_Action_Method)
    throws WPFatal_Exception {
        
        web_Process.curStatus_Add_Action_Node(new WPAction_Node("Maint.", passed_Class_Obj, maint_Action_Method));
        getJavaScript(out, web_Process, form_Name,
        form_Field_Id_Of_Id, form_Field_Id_Of_Name, form_Field_Id_Of_Comp_Id);
    }
    
    
    private static void getJavaScript(PrintWriter out, Web_Process web_Process, String form_Name,
    String form_Field_Id_Of_Id, String form_Field_Id_Of_Name, String form_Field_Id_Of_Comp_Id){
        // <body  onLoad=form_Field_Id_Of_Id_Init()
        // <input name-family_Name ... onKeyUp=form_Field_Id_Of_Id_DoCompletion()
        
        out.println("<script language=\"JavaScript\" type=\"text/JavaScript\">");
        out.println("");
        
        
        out.println("var " + form_Field_Id_Of_Id + "_CompleteTable;");
        out.println("var " + form_Field_Id_Of_Name + "_CompleteField;");
        out.println("var " + form_Field_Id_Of_Id + "_AutoRow;");
        out.println("var " + form_Field_Id_Of_Id + "_Comp_Id;");
        out.println("var " + form_Field_Id_Of_Id + "_FindField;");
        
        
        out.println("function " + form_Field_Id_Of_Id + "_Init() {");
        out.println("    //alert(\"" + form_Field_Id_Of_Id + "_Init\");");
        out.println("    " + form_Field_Id_Of_Name + "_CompleteField = document.getElementById(\""+form_Field_Id_Of_Name+"\");");
        out.println("    " + form_Field_Id_Of_Id + "_FindField = document.getElementById(\""+form_Field_Id_Of_Id+"\");");
        out.println("    " + form_Field_Id_Of_Id + "_Comp_Id = document.getElementById(\""+form_Field_Id_Of_Comp_Id+"\");");
        out.println("    //alert(\"" + form_Field_Id_Of_Id + "_Init:" + form_Field_Id_Of_Id + "_Comp_Id value is:\" + " + form_Field_Id_Of_Id + "_Comp_Id.value );");
        
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
        out.println("        var url = \"Family_AutoCompletion"+web_Process.get_HTML_Of_Href_Call_Parameters()+
        "&action=nameLookup"+
        "&comp_Id=\"+escape(" + form_Field_Id_Of_Id + "_Comp_Id.value)+\""+
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
        out.println("	var familys = responseXML.getElementsByTagName(\"familys\")[0];");
        out.println("	//alert(\"familys.childNodes.length=\"+ familys.childNodes.length);");
        out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    if (familys.childNodes.length > 0) {");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.setAttribute(\"bordercolor\", \"black\");");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.setAttribute(\"border\", \"1\");");
        out.println("        " + form_Field_Id_Of_Id + "_CompleteTable.style.visible = true; ");
        out.println("    } else {");
        out.println("        " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    }");
        out.println("");
        out.println("    //alert(\"familys : \" + familys);");
        out.println("");
        out.println("    for (loop = 0; loop < familys.childNodes.length; loop++) {");
        out.println("	var family = familys.childNodes[loop];");
        out.println("        //alert(\"loop : \" + loop + \"  family=\" + family.value);");
        out.println("        var id = family.getElementsByTagName(\"id\")[0];");
        out.println("        var name = family.getElementsByTagName(\"name\")[0];");
        out.println("        var city = family.getElementsByTagName(\"city\")[0];");
        out.println("        //alert(\"name=\"+name);");
        out.println("        " + form_Field_Id_Of_Id + "_Append(id.childNodes[0].nodeValue, name.childNodes[0].nodeValue, city.childNodes[0].nodeValue, loop);");
        out.println("    }");
        out.println("    " + form_Field_Id_Of_Id + "_Append(\"Maint.\", \"\", \"\", -1);");
        out.println("    " + form_Field_Id_Of_Id + "_Append(\"Close\", \"\", \"\", -2);");
        out.println("");
        out.println("}");
        out.println("");
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_Append(id, name, city, loop) {");
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
        out.println("        linkElement.setAttribute(\"value\", \"family\");");
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
        out.println("        linkElement.setAttribute(\"href\", \"javascript:"+form_Field_Id_Of_Name+"_Load('\" + id + \"', '\" + My_Escape(name) + \"')\");");
        //out.println("        linkElement.appendChild(document.createTextNode(id +\", \" + name));");
        out.println("        linkElement.appendChild(document.createTextNode(name +\", \" + city +\", (\" + id + \")\"));");
        out.println("        nameCell.appendChild(linkElement);");
        
        out.println("    }");
        
        out.println("}");
        out.println("");
        
        
        
        out.println("function "+form_Field_Id_Of_Name+"_Load(id, name) {");
        out.println("    document."+form_Name + "."+form_Field_Id_Of_Id+".value = id;");
        out.println("    document."+form_Name + "."+form_Field_Id_Of_Name+".value = name;");
        out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("}");
        out.println("");
        
        
        
        
        
        
        out.println("function " + form_Field_Id_Of_Id + "_DoFind() {");
        out.println("    if (" + form_Field_Id_Of_Id + "_FindField.value == \"\") {");
        out.println("        " + form_Field_Id_Of_Id + "_ClearTable();");
        out.println("    } else {");
        out.println("        var url = \"Family_AutoCompletion"+web_Process.get_HTML_Of_Href_Call_Parameters()+
        "&action=find"+
        "&comp_Id=\"+escape(" + form_Field_Id_Of_Id + "_Comp_Id.value)+\""+
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
       out.println("    document."+form_Name + "."+form_Field_Id_Of_Name+".value = \"No Such Family\";");

        out.println("		;");
        out.println("        }");
        out.println("      }else{");
        out.println("	//alert(\"state:\" + req.readystate +\"  status :\" + req.status );");
               out.println("    document."+form_Name + "."+form_Field_Id_Of_Name+".value = \"No Such Family\";");

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
        out.println("	var familys = responseXML.getElementsByTagName(\"familys\")[0];");
        out.println("	//alert(\"familys.childNodes.length=\"+ familys.childNodes.length);");
        out.println("    " + form_Field_Id_Of_Id + "_ClearTable();");
        
        out.println("");
        out.println("    //alert(\"familys : \" + familys);");
        out.println("");
        out.println("    for (loop = 0; loop < familys.childNodes.length; loop++) {");
        out.println("	var family = familys.childNodes[loop];");
        out.println("        //alert(\"loop : \" + loop + \"  family=\" + family.value);");
        out.println("        var id = family.getElementsByTagName(\"id\")[0];");
        out.println("        var name = family.getElementsByTagName(\"name\")[0];");
        out.println("        var city = family.getElementsByTagName(\"city\")[0];");
        out.println("        //alert(\"name=\"+name);");
        //out.println("        " + form_Field_Id_Of_Id + "_Append(id.childNodes[0].nodeValue, name.childNodes[0].nodeValue, city.childNodes[0].nodeValue, loop);");
        out.println("    document."+form_Name + "."+form_Field_Id_Of_Id+".value = id.childNodes[0].nodeValue;");
        out.println("    document."+form_Name + "."+form_Field_Id_Of_Name+".value = name.childNodes[0].nodeValue;");
        
        out.println("    }");
        
        
        out.println("");
        out.println("}");
        out.println("");
        
        
        
        
        out.println("</script>");
        
    }
    
    
    
    public static void getDivTable(PrintWriter out, int top, int left, String form_Field_Id_Of_Id){
        out.println("<div style='position: absolute; top:" + top + "px;left:" + left + "px; width: 800px; font-family: Tahoma, Helvetica, sans, Arial, sans-serif; font-size: 12px' id='" + form_Field_Id_Of_Id + "_menu-popup'>");
        out.println("<table id='" + form_Field_Id_Of_Id + "_CompleteTable' border='1' bordercolor='black' cellpadding='0' cellspacing='0' >");
        out.println("<tr><td id='" + form_Field_Id_Of_Id + "_auto-row' colspan='2'>&nbsp;</td></tr>");
        out.println("</table></div>");
        
        //out.println("<div id=\"hiddenDIV2\" style=\"position:relative; top: -22px; left: 85px;width: 200; visibility:visible; background-color:white; border: 1px solid black;\"> </div>");
    }
    
    public static void getDivTable_Relative(PrintWriter out, int top, int left, String form_Field_Id_Of_Id){
        out.println("<div style='position: relative; top:" + top + "px;left:" + left + "px; width: 800px; font-family: Tahoma, Helvetica, sans, Arial, sans-serif; font-size: 12px' id='" + form_Field_Id_Of_Id + "_menu-popup'>");
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
