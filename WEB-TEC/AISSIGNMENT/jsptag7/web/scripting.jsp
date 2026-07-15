<%-- 
    Document   : scripting
    Created on : 13 Mar, 2026, 9:09:57 AM
    Author     : VIRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>JSP Scripting Elements Example</h2>

            <%
            String name = "Viraj";
            int a = 10;
            int b = 20;
            int sum = a + b;
            %>

            Name : <%= name %> <br>
            Sum : <%= sum %>

            <%!
            int count = 0;
            public int visitorCount()
            {
            count++;
            return count;
            }
            %>

            <br>Visitor Count : <%= visitorCount() %>
    </body>
</html>
