<%@ include file="db.jsp" %>

<%
int id=Integer.parseInt(request.getParameter("id"));

PreparedStatement ps=con.prepareStatement(
"delete from book where bookId=?");

ps.setInt(1,id);

ps.executeUpdate();

out.println("Deleted Successfully");
%>
<form action="delete.jsp">
Enter Book Id:
<input type="text" name="id">
<input type="submit" value="Delete">
</form>