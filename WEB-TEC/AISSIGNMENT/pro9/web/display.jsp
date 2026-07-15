<%@ include file="db.jsp" %>

<%
Statement st=con.createStatement();
ResultSet rs=st.executeQuery("select * from book");

while(rs.next()){
%>

Book Id: <%= rs.getInt(1) %><br>
Title: <%= rs.getString(2) %><br>
Author: <%= rs.getString(3) %><br>
Price: <%= rs.getDouble(4) %><br>
Quantity: <%= rs.getInt(5) %><br>
ISBN: <%= rs.getString(6) %><br>
Publisher: <%= rs.getString(7) %><br>
Year: <%= rs.getInt(8) %><br>
CatalogueId: <%= rs.getInt(9) %><br>

<hr>

<%
}
%>