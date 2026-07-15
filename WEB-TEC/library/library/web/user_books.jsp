<%@page import="com.library.model.Book"%>
<%@page import="java.util.List"%>
<%@page import="com.library.dao.BookDAO"%>
<%@page import="com.library.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Prevent caching so the back button doesn't work after logout
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    User user = (User) session.getAttribute("user");
    if (user == null || !"user".equals(user.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Available Books</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky user">
    <div class="container">
        <div class="header">
            <h2>Available Books</h2>
            <div class="header-actions">
                <a href="user_dashboard.jsp" class="back-btn">&#8592; Back to Dashboard</a>
                <a href="LoginServlet?action=logout" class="logout-btn">Logout</a>
            </div>
        </div>
        
        <% 
            String msg = request.getParameter("msg");
            if (msg != null) out.println("<p class='msg'>" + msg + "</p>");
            String error = request.getParameter("error");
            if (error != null) out.println("<p class='error'>" + error + "</p>");
        %>

        <form action="user_books.jsp" method="GET" class="toolbar">
            <input type="text" name="searchQuery" placeholder="Search by title..." class="toolbar-grow2">
            <select name="sortBy" class="toolbar-select">
                <option value="">Sort By...</option>
                <option value="author">Author</option>
                <option value="category">Category</option>
            </select>
            <button type="submit" class="btn">Search / Sort</button>
            <a href="user_books.jsp" class="btn btn-secondary">Clear</a>
        </form>
        <table>
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Category</th>
                    <th>Available</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    BookDAO bookDao = new BookDAO();
                    String searchQuery = request.getParameter("searchQuery");
                    String sortBy = request.getParameter("sortBy");
                    
                    List<Book> books;
                    if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                        books = bookDao.searchBooksByTitle(searchQuery.trim());
                    } else if (sortBy != null && !sortBy.trim().isEmpty()) {
                        books = bookDao.sortBooksBy(sortBy.trim());
                    } else {
                        books = bookDao.getAllBooks();
                    }
                    
                    for(Book b : books) {
                %>
                <tr>
                    <td><%= b.getTitle() %></td>
                    <td><%= b.getAuthor() %></td>
                    <td><%= b.getCategory() %></td>
                    <td><%= b.getAvailable() %></td>
                    <td>
                        <% if (b.getAvailable() > 0) { %>
                            <form action="UserServlet" method="POST">
                                <input type="hidden" name="action" value="requestBook">
                                <input type="hidden" name="bookId" value="<%= b.getId() %>">
                                <button type="submit" class="btn btn-small">Request</button>
                            </form>
                        <% } else { %>
                            <span class="text-danger">Out of Stock</span>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <script src="app.js"></script>
</body>
</html>
