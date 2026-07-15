/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author VIRAJ
 */
public class CIServlet extends HttpServlet {

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
            out.println("<title>Servlet CIServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CIServlet at " + request.getContextPath() + "</h1>");
            
            
            double P = Double.parseDouble(request.getParameter("principal"));
            double R = Double.parseDouble(request.getParameter("rate"));
            double n = Double.parseDouble(request.getParameter("compound"));
            double months = Double.parseDouble(request.getParameter("months"));

           
            if (P <= 0 || R <= 0 || n <= 0 || months <= 0) {
                out.println("<h3>All values must be greater than 0</h3>");
                return;
            }

           
            double t = months / 12.0;

           
            R = R / 100;

           
            double A = P * Math.pow((1 + (R / n)), (n * t));
            
            
            out.println("<h2>Compound Interest Result</h2>");
            out.println("Principal: " + P + "<br>");
            out.println("Total Amount (A): " + A + "<br>");
            out.println("Interest Earned: " + (A - P));

        
            
            out.println("</body>");
            out.println("</html>");
            
        }
        catch(Exception e)
        {
            System.out.println("invalied..."+e.getMessage());
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
