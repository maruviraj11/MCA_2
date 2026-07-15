import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, 
                          HttpServletResponse response)
                          throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get values from form
        String n1 = request.getParameter("num1");
        String n2 = request.getParameter("num2");

        int num1 = Integer.parseInt(n1);
        int num2 = Integer.parseInt(n2);

        int sum = num1 + num2;

        out.println("<h2>Result is: " + sum + "</h2>");
    }
}
