/*
 * Company_Management_Subsystem_Servlet.java
 *
 * Created on July 1, 2006, 3:59 PM
 */

package company_Management;

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
public class Company_Management_Subsystem_Servlet extends Subsystem_Servlet_Base {
    
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
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Compmany Management Subsystem Servlet";
    }
    
    public String[] getDatabase_Record_List() {
        String[] database_Record_List ={ 
            "company_Management.datum.Comp"
        };
        return database_Record_List;
    }
    
    public WPHttpServlet_Base[] getServlet_List() {
        WPHttpServlet_Base[] servlet_List = {
            new Cmp100(),
            new Get_Comp()
        };
        return servlet_List;
    }    
    
}
