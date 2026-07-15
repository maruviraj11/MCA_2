
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CalculateServlet extends HttpServlet {

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
   {
       response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            // Retrieve input values
            double principal = Double.parseDouble(request.getParameter("principal"));
            double rate = Double.parseDouble(request.getParameter("rate"));
            int years = Integer.parseInt(request.getParameter("years"));
            int months = Integer.parseInt(request.getParameter("months"));
            int interval = Integer.parseInt(request.getParameter("interval"));

             if(principal >= 1000000)
            {
               out.println("<p style='color:red;'>Error: Enter Amount less than 1000000</p>");
              
            }
            else{
            // Calculate time in years
            double time = years + (months / 12.0);

            // Calculate compound interest: A = P(1 + R/(n*100))^(n*t)
            double amount = principal * Math.pow((1 + rate / (interval * 100)), interval * time);
            out.println("  <div style='width: 500px; border: 3px solid black;padding: 30px; margin: 5px; margin-left:400px; margin-top:50px;'>");
            out.println("<h1  style='color:blue;'>Compound Interest Calculation</h1>");
            out.println("<p>Principal Amount (Rs.) : <b>" + principal + "</b> </p>");
            out.println("<p>Intresr Rate :<b> " + rate + " %</b></p>");
            out.println("<p>Calculation of : <b>" + years + "</b> years and <b>"+months+"</b> Months</p>");
            out.println("<p>Compounded <b>" + interval + "</b> times per year</p>");
            out.println("<p>Future Total Amount (Rs.) :<b> " + String.format("%.2f", amount) + "</b></p>");
            out.print("<center><button style='color:white;  height: 42px; border-radius: 24px; background-color: lightblue;' onclick=\"history.back()\">Go Back</button></center>");
            out.println("</div>");
            } 
        }
        catch (NumberFormatException e) {
            out.println("<p style='color:red;'>Error: Please Enter valid numbers Input.</p>");
           
        } finally {
            out.close();
        }
   }
}
