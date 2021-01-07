/*
 * Outside_User_LoginServlet.java
 *
 * Created on April 1, 2007, 7:23 PM
 */

package entity_Management;

import com.amlibtech.data_fields.*;
import com.amlibtech.database.*;
import com.amlibtech.login.data.*;
import com.amlibtech.web_Processes.*;
import customer_Management.datum.*;
import enroll_Now_Admin.data.*;
import entity_Management.datum.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 * @version
 */
public class Outside_User_LoginServlet extends HttpServlet {
    
    LinkedList user_List;
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    
    
    
    
    void show_Select_Outside_User_Page(HttpServletRequest request, HttpServletResponse response, Subscriber_Organization subscriber_Organization, Subscriber_Organization_Portal subscriber_Organization_Portal, String web_Root, String message)
    throws IOException {
        
        
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        
        
        out.println("<html>\n");
        
        
        
        String css_Root = subscriber_Organization_Portal.getCss_Root().toString();
        String img_Root = subscriber_Organization_Portal.getImage_FullPath();
        //String web_Root = subscriber_Organization_Portal.getWeb_Root().toString();
        
        out.println("<head>");
        out.println("<title>"+subscriber_Organization.getName().getValue()+" " +
        subscriber_Organization_Portal.getPortal_Name().getValue() +" Login Page</title>");
        
        if(subscriber_Organization_Portal.getUse_Std_Css_Theme().getValue().booleanValue())
            out.println("   <link rel='stylesheet' type='text/css' href='" + web_Root + css_Root + "/Themes/STD.css' >");
        else
            out.println("   <link rel='stylesheet' type='text/css' href='" + web_Root + css_Root + "/Themes/"+ subscriber_Organization_Portal.getCustom_Css_Theme_Name().toString()+".css' >");
        
        
        out.println("</head>");
        
        
        
        
        
        
        out.println("<div id=header>");
        
        out.println("<table width='100%' height='80'  border='0' align='left' cellpadding='0' cellspacing='0'>");
        out.println("   <tr>");
        out.println("     <th width='15%'class='pinkborder' scope='col'><img src='"+img_Root+"/Dancer%20large_clear.gif' width='75' height='97'></th>");
        out.println("     <th width='70%' class='flower' scope='col'><span class='style5'>Welcome to Dancenter North ");
        out.println("</span></th>");
        out.println("     <th width='15%' class='pinkborder' scope='col'><img src='"+img_Root+"/Dancer%20large_clearRT.gif' width='75' height='97'></th>");
        out.println("   </tr>");
        out.println(" </table>");
        
        
        
        out.println("</div>");
        
        
        out.println("\n");
        out.println("    <body onload=\"document.login.outside_User_Id.focus();\">\n");
        
        
        
        
        out.println("<div id=main>");
        
        out.println("		<center><h2>" + user_List.size() + " users currently logged in.</h2></center>\n");
        
        out.println("		<center><h2>Please Login</h2></center>\n");
        out.println("\n");
        out.println("		<form name=\"login\" action=\"" + web_Root + "/LoginServlet\" method=\"post\">\n");
        out.println("               <input type=\"hidden\" name=\"subscriber_Organization_Id\" value=\"" + subscriber_Organization.getSubscriber_Organization_Id().getValue() + "\">\n");
        out.println("               <input type=\"hidden\" name=\"subscriber_Organization_Portal_Name\" value=\"" + subscriber_Organization_Portal.getPortal_Name().getValue() + "\">\n");
        out.println("		    <center>\n");
        out.println("			<table cellspacing=\"5\" border=\"0\">\n");
        out.println("			<tbody align=center >\n");
        
        out.println("				<tr>\n");
        out.println("					<td align=\"right\">E-Mail Address</td>\n");
        String outside_User_Id = request.getParameter("outside_User_Id");
        if(outside_User_Id== null){
            out.println("					<td><input type=\"text\" name=\"outside_User_Id\"></td>\n");
        }else{
            out.println("					<td><input type=\"text\" name=\"outside_User_Id\" value=\"" + outside_User_Id + "\" ></td>\n");
        }
        out.println("				</tr>\n");
        
        out.println("				<tr>\n");
        out.println("					<td align=\"right\">Password</td>\n");
        out.println("					<td><input type=\"password\" name=\"password\"></td>\n");
        out.println("				</tr>\n");
        out.println("				<tr>\n");
        out.println("					<td><input type=\"submit\" name=\"to_Do\" value=\"Login\"></td>\n");
        out.println("				</tr>\n");
        out.println("			</tbody>\n");
        out.println("			</table>\n");
        out.println("		</center>\n");
        out.println("		</form>\n");
        
        if(message != null && message.equals("")==false){
            out.println("    <font color=\"#FF0000\"><font size=4>\n");
            out.println("        <center><i>Error Message: " + message + "</i></center>\n");
            out.println("    </font></font>\n");
        }
        
        out.println("</div>");
        
        out.println("<div id=sidebar>");
        out.println("</div>");
        
        
        out.println("	</body>\n");
        out.println("</html>\n");
        
        
        out.close();
        
        return;
    }
    
    
    
    void show_Server_Busy_Page(HttpServletRequest request, HttpServletResponse response, Subscriber_Organization subscriber_Organization, Subscriber_Organization_Portal subscriber_Organization_Portal, String web_Root, String message)
    throws IOException {
        
        
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        
        
        out.println("<html>\n");
        
        
        
        String css_Root = subscriber_Organization_Portal.getCss_Root().toString();
        String img_Root = subscriber_Organization_Portal.getImage_FullPath();
        //String web_Root = subscriber_Organization_Portal.getWeb_Root().toString();
        
        out.println("<head>");
        out.println("<title>"+subscriber_Organization.getName().getValue()+" " +
        subscriber_Organization_Portal.getPortal_Name().getValue() +" Server Busy Page</title>");
        
        if(subscriber_Organization_Portal.getUse_Std_Css_Theme().getValue().booleanValue())
            out.println("   <link rel='stylesheet' type='text/css' href='" + web_Root + css_Root + "/Themes/STD.css' >");
        else
            out.println("   <link rel='stylesheet' type='text/css' href='" + web_Root + css_Root + "/Themes/"+ subscriber_Organization_Portal.getCustom_Css_Theme_Name().toString()+".css' >");
        
        
        out.println("</head>");
        
        
        
        
        
        
        out.println("<div id=header>");
        
        out.println("<table width='100%' height='80'  border='0' align='left' cellpadding='0' cellspacing='0'>");
        out.println("   <tr>");
        out.println("     <th width='15%'class='pinkborder' scope='col'><img src='"+img_Root+"/Dancer%20large_clear.gif' width='75' height='97'></th>");
        out.println("     <th width='70%' class='flower' scope='col'><span class='style5'>Welcome to Dancenter North ");
        out.println("</span></th>");
        out.println("     <th width='15%' class='pinkborder' scope='col'><img src='"+img_Root+"/Dancer%20large_clearRT.gif' width='75' height='97'></th>");
        out.println("   </tr>");
        out.println(" </table>");
        
        
        
        out.println("</div>");
        
        
        out.println("\n");
        out.println("    <body>\n");
        
        
        
        
        out.println("<div id=main>");
        
        
        out.println("		<center><h2>" + user_List.size() + " users currently logged in.</h2></center>\n");
        out.println("		<center><h2>Sorry, Server Busy Now.</h2></center>\n");
        out.println("		<center><h2>Please try back later. </h2></center>\n");
        out.println("\n");
        
        if(message != null && message.equals("")==false){
            out.println("    <font color=\"#FF0000\"><font size=4>\n");
            out.println("        <center><i>Error Message: " + message + "</i></center>\n");
            out.println("    </font></font>\n");
        }
        
        out.println("</div>");
        
        out.println("<div id=sidebar>");
        out.println("</div>");
        
        
        out.println("	</body>\n");
        out.println("</html>\n");
        
        
        out.close();
        
        return;
    }
    
    
    
    
    
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String message;
        message ="";
        session.setAttribute("s_message", message);
        
        
        
        
        
        String web_Root;
        
        web_Root = (String) session.getAttribute("s_web_Root");
        if(web_Root == null){
            session.setAttribute("s_web_Root", request.getContextPath());
            web_Root = (String) session.getAttribute("s_web_Root");
        }
        
        
        
        
        
        
        //========== get subscriber_Organization_Id ==========================================
        Subscriber_Organization i_Am_Subscriber_Organization=null;
        
        try {
            i_Am_Subscriber_Organization= Subscriber_Organization.get_Session_Instance(session);
        }catch(Database_Record_Exception dbre){
            log("Outside_User_LoginServlet: No Subscriber_Organization Session Instance");
            getServletContext().getRequestDispatcher("/LoginServlet").forward(request, response);
        }
        
        
        //========== get subscriber_Organization_Portal_Name ==========================================
        Subscriber_Organization_Portal i_Am_Subscriber_Organization_Portal=null;
        
        try {
            i_Am_Subscriber_Organization_Portal = Subscriber_Organization_Portal.get_Session_Instance(session);
        }catch(Database_Record_Exception dbre){
            log("Outside_User_LoginServlet: No Subscriber_Organization_Portal Session Instance");
            getServletContext().getRequestDispatcher("/LoginServlet").forward(request, response);
        }
        
        Web_Process_Table web_Process_Table = Web_Process_Table.get_Instance();
        LinkedList wp_List = web_Process_Table.getLinkedList();
        user_List = new LinkedList();
        
        
        for(int i=0; i<wp_List.size(); i++){
            Web_Process wp = (Web_Process)wp_List.get(i);
            User wp_User = wp.getUser();
            boolean found_User=false;
            for(int j=0; j<user_List.size() && !found_User; j++){
                User temp_User = (User) user_List.get(j);
                
                if(wp_User.is_A_Customer()){
                    if(temp_User.is_A_Customer()){
                        if(wp_User.getReference_Id().getValue().equals(temp_User.getReference_Id().getValue())){
                            found_User=true;
                        }
                    }
                }else if(wp_User.is_A_Teacher()){
                    if(temp_User.is_A_Teacher()){
                        if(wp_User.getReference_Id().getValue().equals(temp_User.getReference_Id().getValue())){
                            found_User=true;
                        }
                    }
                }else  if(wp_User.is_A_Vendor()){
                    if(temp_User.is_A_Vendor()){
                        if(wp_User.getReference_Id().getValue().equals(temp_User.getReference_Id().getValue())){
                            found_User=true;
                        }
                    }
                }else{
                    if(wp_User.getUser_Id().getValue().equals(temp_User.getUser_Id().getValue())){
                        found_User=true;
                    }
                }
            }
            if(!found_User){
                user_List.add(wp_User);
            }
        }
        
        if(user_List.size() >10){
            show_Server_Busy_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, message);
        }else{
            
            
            
            Database_Front subscriber_Organization_Database_Front=null;
            
            subscriber_Organization_Database_Front =new Database_Front(i_Am_Subscriber_Organization.get_Client_App_Constants_Constants());
            
            try{
                subscriber_Organization_Database_Front.open();
            }
            catch(Database_Front_Exception dbfe){
                message = "Failed to open "+ i_Am_Subscriber_Organization.getDatabase_Name() +
                " (which is this Subscriber Organization's database_Front), because :" + dbfe.getMessage() + ".";
                log("Outside_User_LoginServlet: " + message);
                getServletContext().getRequestDispatcher("/LoginServlet").forward(request, response);
                //show_Select_Organization_Page(request, response, web_Root, message);
                return;
            }
            
            
            
            
            
            String login_Method = i_Am_Subscriber_Organization_Portal.getLogin_Method().getValue();
            
            log("Outside_User_LoginServlet: login_Method =" + login_Method);
            
            
            //========== get outside user and password ==========================================
            
            
            
            
            
            String outside_User_Id = request.getParameter("outside_User_Id");
            if(outside_User_Id== null){
                // No outside_User_Id passed to servlet  so ask for one
                message="";
                show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, message);
                return;
            }
            
            String password = request.getParameter("password");
            if(password== null){
                // No password passed to servlet  so ask for one
                message="";
                show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, message);
                return;
            }
            
            
            Outside_User i_Am_Outside_User=null;
            i_Am_Outside_User = validate_Outside_User(i_Am_Subscriber_Organization_Portal, subscriber_Organization_Database_Front, outside_User_Id, password);
            
            if(i_Am_Outside_User==null){
                try {
                    subscriber_Organization_Database_Front.close();
                } catch(Database_Front_Exception dbfe){
                    log("Outside_User_LoginServlet: Database_Front_Exception on close of database :" + dbfe.getMessage());
                }
                show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login Failed.");
                return;
            }
            
            
            
            
            if(i_Am_Outside_User.getType_Index().getValue().equals(Outside_User.OUTSIDE_USER_TYPES[Outside_User.OUTSIDE_USER_TYPE_INDEX_PERSON])){
                
                Person person = null;
                
                person = validate_Person(subscriber_Organization_Database_Front, i_Am_Outside_User);
                
                if(person == null){
                    try {
                        subscriber_Organization_Database_Front.close();
                    }catch(Database_Front_Exception dbfe2){
                        log("Outside_User_LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                    }
                    show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login Failed.");
                    return;
                }
                
                
                
                
                if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_CUSTOMER])){
                    // are they a customer
                    Cust cust;
                    
                    try {
                        cust = person.get_Aka_Cust(subscriber_Organization_Database_Front);
                    }catch(WPFatal_Exception wpfe){
                        log("Outside_User_LoginServlet: User :" + outside_User_Id + ":  failed to get AKA cust.", wpfe);
                        try {
                            subscriber_Organization_Database_Front.close();
                        }catch(Database_Front_Exception dbfe2){
                            log("Outside_User_LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                        }
                        show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login Failed.");
                        return;
                    }
                    
                    if(cust==null){
                        log("Outside_User_LoginServlet: User :" + outside_User_Id + ":  not a customer.");
                        try {
                            subscriber_Organization_Database_Front.close();
                        }catch(Database_Front_Exception dbfe2){
                            log("Outside_User_LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                        }
                        show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login Failed.");
                        return;
                    }
                    
                    User i_Am_User=null;
                    
                    try {
                        i_Am_User=new User("CUSTOMER");
                        
                        i_Am_User.setDescription("Customer User");
                        i_Am_User.setLast_Name(person.getLast_Name().getValue());
                        i_Am_User.setFirst_Name(person.getFirst_Name().getValue());
                        
                        i_Am_User.setSecurity_Profile_Id("CUSTOMER");
                        i_Am_User.setTimeZone_Name("CST6CDT");
                        i_Am_User.setReference_Id(cust.getCust_Id().getValue());
                        i_Am_User.setLogin_Method(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_CUSTOMER]);
                        
                    }catch(Exception e){
                        log("Outside_User_LoginServlet: " + e);
                        getServletContext().getRequestDispatcher("LoginServlet").forward(request, response);
                        //show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "Outside_User_LoginServlet: " + e);
                        return;
                    }
                    
                    i_Am_User.set_Session_Instance(session);
                    
                    log(i_Am_User.getFirst_Name() + " " + i_Am_User.getLast_Name() + " Logged in.");
                    
                    subscriber_Organization_Database_Front.set_Session_Instance(session);
                    
                    getServletContext().getRequestDispatcher("/portal_Pages/"+i_Am_Subscriber_Organization_Portal.getDestination_Url().getValue()).forward(request, response);
                    
                    return;
                    
                    
                } else if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_VENDOR])){
                    getServletContext().getRequestDispatcher("/Vendor_LoginServlet").forward(request, response);
                    //log("LoginServlet: portal login method :" + login_Method + " not yet implimented.");
                    //show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "LoginServlet: portal login method :" + login_Method + " not yet implimented.");
                    return;
                } else if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_TEACHER])){
                    // are they a Teacher
                    
                    
                    
                    Teacher    teacher = null;
                    try {
                        teacher = new Teacher();
                    }catch(Database_Record_Constructor_Exception dbrce){
                        log("Outside_User_LoginServlet: User :" + outside_User_Id + ":  failed to get AKA Teacher.", dbrce);
                        try {
                            subscriber_Organization_Database_Front.close();
                        }catch(Database_Front_Exception dbfe2){
                            log("Outside_User_LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                        }
                        show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login Failed.");
                        return;
                    }
                    
                    Teacher[] teachers = null;
                    try{
                        teachers = (Teacher[]) subscriber_Organization_Database_Front.getMany_Records(teacher, "WHERE " +
                        person.getComp_Id().toWhere_Clause() + " AND " +
                        person.getPerson_Number().toWhere_Clause());
                    }catch(Database_Front_Exception dbfe){
                        log("Outside_User_LoginServlet: User :" + outside_User_Id + ":  failed to get AKA Teacher.", dbfe);
                        try {
                            subscriber_Organization_Database_Front.close();
                        }catch(Database_Front_Exception dbfe2){
                            log("Outside_User_LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                        }
                        show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login Failed.");
                        return;
                    }
                    
                    if(teachers == null){
                        log("Outside_User_LoginServlet: User :" + outside_User_Id + ":  failed to get AKA Teacher.");
                        try {
                            subscriber_Organization_Database_Front.close();
                        }catch(Database_Front_Exception dbfe2){
                            log("Outside_User_LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                        }
                        show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login Failed.");
                        return;
                    }
                    
                    teacher = teachers[0];
                    
                    
                    if(teacher==null){
                        log("Outside_User_LoginServlet: User :" + outside_User_Id + ":  not a teacher.");
                        try {
                            subscriber_Organization_Database_Front.close();
                        }catch(Database_Front_Exception dbfe2){
                            log("Outside_User_LoginServlet: Database_Front_Exception on close of database :" + dbfe2.getMessage());
                        }
                        show_Select_Outside_User_Page(request, response, i_Am_Subscriber_Organization, i_Am_Subscriber_Organization_Portal, web_Root, "Login Failed.");
                        return;
                    }
                    
                    
                    User i_Am_User=null;
                    
                    try {
                        i_Am_User=new User("TEACHER");
                        
                        i_Am_User.setDescription("Teacher User");
                        i_Am_User.setLast_Name(person.getLast_Name().getValue());
                        i_Am_User.setFirst_Name(person.getFirst_Name().getValue());
                        
                        i_Am_User.setSecurity_Profile_Id("TEACHER");
                        i_Am_User.setTimeZone_Name("CST6CDT");
                        
                        i_Am_User.setReference_Id(teacher.getTeacher_Id().getValue());
                        i_Am_User.setLogin_Method(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_TEACHER]);
                        
                    }catch(Exception e){
                        log("Outside_User_LoginServlet: " + e);
                        getServletContext().getRequestDispatcher("LoginServlet").forward(request, response);
                        //show_Select_Portal_Page(request, response, i_Am_Subscriber_Organization, web_Root, "Outside_User_LoginServlet: " + e);
                        return;
                    }
                    
                    
                    
                    i_Am_User.set_Session_Instance(session);
                    
                    log(i_Am_User.getFirst_Name() + " " + i_Am_User.getLast_Name() + " Logged in.");
                    
                    subscriber_Organization_Database_Front.set_Session_Instance(session);
                    
                    getServletContext().getRequestDispatcher("/portal_Pages/"+i_Am_Subscriber_Organization_Portal.getDestination_Url().getValue()).forward(request, response);
                    
                    return;
                    
                    
                } else if(login_Method.equals(Subscriber_Organization_Portal.LOGIN_METHODS[Subscriber_Organization_Portal.LOGIN_METHOD_INDEX_STUDENT])){
                    getServletContext().getRequestDispatcher("/Student_LoginServlet").forward(request, response);
                    return;
                }else{
                    log("Outside_User_LoginServlet: portal has unknown login method :" + login_Method + ".");
                    getServletContext().getRequestDispatcher("LoginServlet").forward(request, response);
                    return;
                }
                
            }else if(i_Am_Outside_User.getType_Index().getValue().equals(Outside_User.OUTSIDE_USER_TYPES[Outside_User.OUTSIDE_USER_TYPE_INDEX_ORGANIZATION])){
                ;
            }else{
                log("Outside_User_LoginServlet: outside_User is of unknown type :" + i_Am_Outside_User.getType_Index().getValue() + ".");
                getServletContext().getRequestDispatcher("/LoginServlet").forward(request, response);
                return;
            }
            
            
        }
        return;
        
        
    }
    
    
    
    Outside_User validate_Outside_User(Subscriber_Organization_Portal i_Am_Subscriber_Organization_Portal, Database_Front subscriber_Organization_Database_Front, String outside_User_Id, String password)
    throws IOException {
        
        Outside_User i_Am_Outside_User;
        
        try {
            i_Am_Outside_User = (Outside_User) subscriber_Organization_Database_Front.getRecord(new Outside_User(i_Am_Subscriber_Organization_Portal.getCustomer_Or_Vendor_Comp_Id().getValue(), outside_User_Id));
        }catch(Database_Record_Constructor_Exception dbre){
            log("Outside_User_LoginServlet: Database_Record_Constructor_Exception:" + dbre.getMessage() );
            return null;
        }catch(Database_Front_Exception dbfe){
            log("Outside_User_LoginServlet: Database_Front_Exception:" + dbfe.getMessage() );
            return null;
        }catch(Database_Front_No_Results_Exception dbfe){
            log("Outside_User_LoginServlet: No such User :" + outside_User_Id + ":  Database_Front_No_Results_Exception :" + dbfe.getMessage() );
            return null;
        }
        
        if(password.equals(i_Am_Outside_User.getPassword().getValue())  == false){
            //message = "Login failed. "+ "Wrong password";
            log("Outside_User_LoginServlet: User :" + outside_User_Id + ":  wrong password :" + password);
            try {
                subscriber_Organization_Database_Front.close();
            }catch(Database_Front_Exception dbfe){
                log("Outside_User_LoginServlet: Database_Front_Exception on close of database :" + dbfe.getMessage());
            }
            return null;
        }
        if(i_Am_Outside_User.getObsolete().getValue().booleanValue()){
            log("Outside_User_LoginServlet: User :" + outside_User_Id + ":  obsolete.");
            return null;
        }
        
        
        return i_Am_Outside_User;
    }
    
    
    
    Person validate_Person(Database_Front subscriber_Organization_Database_Front, Outside_User i_Am_Outside_User){
        Person temp_Person;
        Person person;
        
        try {
            temp_Person = new Person();
        }catch(Database_Record_Constructor_Exception dbre){
            log("Outside_User_LoginServlet: Database_Record_Constructor_Exception:" + dbre.getMessage() );
            return null;
        }
        
        try {
            temp_Person.setComp_Id(i_Am_Outside_User.getComp_Id().getValue());
            temp_Person.setPerson_Number(i_Am_Outside_User.getPerson_Number().getValue());
        }catch(DFException dfe){
            log("Outside_User_LoginServlet: DFException:" + dfe.getMessage() );
            return null;
        }
        
        try {
            person = (Person) subscriber_Organization_Database_Front.getRecord(temp_Person);
        }catch(Database_Front_Exception dbfe){
            log("Outside_User_LoginServlet: Database_Front_Exception:" + dbfe.getMessage() );
            return null;
        }catch(Database_Front_No_Results_Exception dbfe){
            log("Outside_User_LoginServlet: No such person :" + i_Am_Outside_User.getPerson_Number().getValue() + ":  Database_Front_No_Results_Exception :" + dbfe.getMessage() );
            return null;
        }
        
        if(person.getObsolete().getValue().booleanValue()){
            log("Outside_User_LoginServlet: person :" + i_Am_Outside_User.getPerson_Number().getValue() + ": is obsolete.");
            return null;
        }
        
        return person;
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
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}


//{
//log("Outside_User_LoginServlet: no subscriber_Organization_Id parameter passed.");
//getServletContext().getRequestDispatcher("LoginServlet").forward(request, response);
//}






