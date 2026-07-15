import dbconnection.dbconnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/loginServlet")

public class loginServlet extends HttpServlet {

protected void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {

    String email=request.getParameter("email");
    String password=request.getParameter("password");

    response.setContentType("text/html");
    PrintWriter out=response.getWriter();

    try{

        Connection con=dbconnection.getConnection();
        //Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/college","root","");
          

        PreparedStatement ps=con.prepareStatement(
    //"select u.name,r.role from users u inner join user_role r on u.email=r.email where u.email=? and u.password=?");
           "select * from users where email=? and password=?");
        ps.setString(1,email);
        ps.setString(2,password);

        ResultSet rs=ps.executeQuery();

        if(rs.next()){

            HttpSession session=request.getSession();

            session.setAttribute("email",email);
            session.setAttribute("first_name",rs.getString("first_name"));
            session.setAttribute("role",rs.getString("role"));

            response.sendRedirect("home.jsp");

        }else{

            out.println("<h3>Invalid Email or Password</h3>");

        }

        con.close();

    }
   
    catch(Exception e){

        e.printStackTrace();
        out.println(e);
    }
   
}
}