<%-- 
    Document   : index
    Created on : 13 Mar, 2026, 12:07:42 PM
    Author     : viraj maru
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <title>JSP Demo</title>
        
         <script>
            function validateForm()
{
    var name = document.getElementById("name").value.trim();

    if(name === "")
    {
        alert("Do not leave any field blank");
        return false;
    }

    return true;
}
                   function noSpace(event)
                      {
                          if(event.key === " ")
                          {
                              alert("Space is not allowed");
                              return false;
                          }
                      }


                      function onlyLetters(event)
                      {
                          var char = event.key;

                          if(!/[a-zA-Z]/.test(char))
                          {
                              alert("Only letters allowed in Name field");
                              return false;
                          }
          }

            </script>
    </head>

    <body>

        <h2>JSP Scripting Elements</h2>

        <%! int num = 100;%>
        

        <%
            int a = 10;
            int b = 20;
            int sum = a + b;
        %>

        Sum is : <%= sum%>

        <h2>JSP Implicit Object Example</h2>

        <form action="index.jsp"  method="post" onsubmit="return validateForm()">
            Enter Name:
            <input type="text" id="name" name="name" onkeypress="return onlyLetters(event)"  onkeypress="return noSpace(event)" maxlength="10">
            <input type="submit" onsubmit="return validateForm()">
        </form>

        <%
            String name = request.getParameter("name");
            if (name != null) {
                out.println("Welcome " + name);
            }
        %>

        <h2>JSP Action Tag Example</h2>

        <jsp:include page="footer.jsp" />
       
      

    </body>
</html>