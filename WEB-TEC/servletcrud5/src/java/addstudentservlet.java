/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author VIRAJ
 */
public class addstudentservlet extends HttpServlet {

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
            out.println("<title>Servlet addstudentservlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addstudentservlet at " + request.getContextPath() + "</h1>");
            out.println("<h2>THIS IS A ADD STUDENT SERVLET</h2>");
            
           String name=request.getParameter("name");
           String email=request.getParameter("email");
           
           try
           {
              Class.forName("com.mysql.cj.jdbc.Driver");
              
              Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/servletcrud","root","");
              
              String query = "insert into student(name,email)values(?,?)";
              
              PreparedStatement ps = con.prepareStatement(query);
              
              ps.setString(1, name);
              ps.setString(2, email);
              
              int i;
                i = ps.executeUpdate();
                
                if(i>0)
                {
                    out.println("<h2>STUDENT ADD SUCCESSFULLY</h2>");
                }
                else
                {
                    out.println("<h2>STUDENT ADD FAILED</h2>");
                }
             response.sendRedirect("viewstudentservlet");
              
           }
           catch(ClassNotFoundException | SQLException e)
           {
               out.println("this is a exception"+e.getMessage());
           }
           
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

    private Connection getconnection(String jdbcmysqllocalhost3306servletcrud, String root, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
