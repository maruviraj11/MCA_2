<%@ include file="db.jsp" %>

<%
String title=request.getParameter("title");
String author=request.getParameter("author");
String price=request.getParameter("price");
String quantity=request.getParameter("quantity");
String isbn=request.getParameter("isbn");
String publisher=request.getParameter("publisher");
String year=request.getParameter("year");
String catalogueId=request.getParameter("catalogueId");

PreparedStatement ps=con.prepareStatement(
"insert into book(title,author,price,quantity,isbn,publisher,edition_year,catalogueId) values(?,?,?,?,?,?,?,?)");

ps.setString(1,title);
ps.setString(2,author);
ps.setDouble(3,Double.parseDouble(price));
ps.setInt(4,Integer.parseInt(quantity));
ps.setString(5,isbn);
ps.setString(6,publisher);
ps.setInt(7,Integer.parseInt(year));
ps.setInt(8,Integer.parseInt(catalogueId));

int i=ps.executeUpdate();

if(i>0)
{
%>

<h2>Book Added Successfully</h2>

<a href="display.jsp">View Books</a>

<%
}
else
{
%>

<h2>Insert Failed</h2>

<%
}
%>