/*
 * Entity_Management_Subsystem_Servlet.java
 *
 * Created on July 30, 2006, 2:42 PM
 */

package entity_Management;

import com.amlibtech.web_Processes.*;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 * @version
 */
public class Entity_Management_Subsystem_Servlet extends Subsystem_Servlet_Base  {
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Entity Management Subsystem Servlet";
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
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        /* TODO output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet</title>");
        out.println("</head>");
        out.println("<body>");
         
        out.println("</body>");
        out.println("</html>");
         */
        out.close();
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
    
    
    
    public String[] getDatabase_Record_List() {
        String[] database_Record_List ={ 
            "entity_Management.datum.Entity_Control",
            "entity_Management.datum.Organization",
            "entity_Management.datum.Organization_Contact_Type",
            "entity_Management.datum.Organization_Type",
            "entity_Management.datum.Outside_User",
            "entity_Management.datum.Person",
            "entity_Management.datum.Person_Additional_Address",
            "entity_Management.datum.Person_Additional_E_Mail",
            "entity_Management.datum.Person_Additional_Phone",
            "entity_Management.datum.Person_Contact_Type",
            "entity_Management.datum.Family"

        };
        return database_Record_List;
    }
    
}
