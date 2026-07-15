<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>

<%
if(session.getAttribute("email")==null){
response.sendRedirect("index.jsp");
}
%>

<!DOCTYPE html>
<html>
<head>
<title>Home</title>
</head>

<body>

<h1>HOME PAGE</h1>

<h2>Welcome <%=session.getAttribute("first_name")%></h2>

<h3>Your Role : <%=session.getAttribute("role")%></h3>

<a href="logout.jsp">Logout</a>

</body>
</html>