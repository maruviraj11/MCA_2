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
public class formservlet extends HttpServlet {

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
            out.println("<title>Servlet formservlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet formservlet at " + request.getContextPath() + "</h1>");
            out.println("<h2>THIS IS HOME PAGE</h2>");
            
            String fname=request.getParameter("fname");
            String lname=request.getParameter("lname");
            String password=request.getParameter("password");
            String cpassword=request.getParameter("cpassword");
            String email=request.getParameter("email");
            String address=request.getParameter("address");
            String role=request.getParameter("role");
            
            
            try
            {
               
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/college","root","")) {
                    String query="insert into student(fname,lname,password,cpassword,email,address,role)values(?,?,?,?,?,?,?)";
                    
                   //preparedStatment ps=con.preparedStatment(query);
                    PreparedStatement ps = con.prepareStatement(query);
                    
                    ps.setString(1,fname);
                    ps.setString(2, lname);
                    ps.setString(3,password);
                    ps.setString(4,cpassword);
                    ps.setString(5, email);
                    ps.setString(6,address);
                    ps.setString(7,role);
                    
                    int i=ps.executeUpdate();
                    
                    if(i>0)
                    {
                        out.println("<h2>REGISTRATION SUCCEESSFULLY</h2>");
                    }
                    else
                    {
                        out.println("<h2>REGISTRATION FALIED</h2>");
                    }
                }
            }
            catch(ClassNotFoundException | SQLException e)
            {
                out.println("this is a exception called"+e.getMessage());
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

    private void Forname(String conmysqlcjjdbcDriver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
