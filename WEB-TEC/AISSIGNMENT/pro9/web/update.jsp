<%@ include file="db.jsp" %>

<%
int id=Integer.parseInt(request.getParameter("id"));
double price=Double.parseDouble(request.getParameter("price"));

PreparedStatement ps=con.prepareStatement(
"update book set price=? where bookId=?");

ps.setDouble(1,price);
ps.setInt(2,id);

ps.executeUpdate();

out.println("Updated Successfully");
%>
<form action="update.jsp">
Book Id:
<input type="text" name="id"><br>

New Price:
<input type="text" name="price"><br>

<input type="submit" value="Update">
</form>