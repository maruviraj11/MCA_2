/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author VIRAJ
 */
public class hello extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet hello</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet hello at " + request.getContextPath() + "</h1>");
            out.print("<h2>THIS IS A SERVLET PAGE</h2>");
            
            String name= request.getParameter("name");
            
            HttpSession session = request.getSession(true);
            
            Date starttime = (Date)session.getAttribute("name");
            
            if(starttime == null)
            {
                starttime = new Date();
                session.setAttribute("starttime",starttime); 
            }
            
            Integer count = (Integer)session.getAttribute("count");
            
            if(count == null)
            {
                count =1;
            }
            else
            {
                count++;
            }
            session.setAttribute("count",count);
            
            out.println("<h2>SESSION INFORMATION</h2>");
            
            out.println("<h3>WELCOME </h3>"+name);
            
            out.println("session id:<br>"+session.getId()+"<br>");
            out.println("srssion creation time<br>"+session.getCreationTime()+"<br>");
            out.println("<br>"+"session last access time<br>"+session.getLastAccessedTime()+"<br>");
            
            
            out.println("<h2>visiter is:</h2>"+count+"<br>");
            
            out.println("<form action='logout' method='post'><br>");
            out.println("<input type='submit'  value='logout'<br>");
            out.println("</form>");
            
            
            
            
            
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
