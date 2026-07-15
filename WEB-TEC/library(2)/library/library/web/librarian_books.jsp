<%@page import="java.util.List"%>
<%@page import="com.library.model.Book"%>
<%@page import="com.library.dao.BookDAO"%>
<%@page import="com.library.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Prevent caching so the back button doesn't work after logout
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    User user = (User) session.getAttribute("user");
    if (user == null || !"librarian".equals(user.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Manage Books Inventory</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body class="theme-sky librarian">
    <div class="container">
        <div class="header">
            <h2>Manage Books Inventory</h2>
            <div class="header-actions">
                <a href="librarian_dashboard.jsp" class="back-btn">&#8592; Back to Dashboard</a>
                <a href="LoginServlet?action=logout" class="logout-btn">Logout</a>
            </div>
        </div>
        
        <% 
            String msg = request.getParameter("msg");
            if (msg != null) out.println("<p class='msg'>" + msg + "</p>");
            String error = request.getParameter("error");
            if (error != null) out.println("<p class='error'>" + error + "</p>");
        %>

        <div class="panel panel--compact" style="margin-bottom: 22px;">
            <h3>Add New Book</h3>
            <form action="LibrarianServlet" method="POST" class="flex-form" autocomplete="off">
                <input type="hidden" name="action" value="addBook">
                <input type="hidden" name="source" value="books">
                <div class="form-group" style="flex:2;">
                    <label>Title</label>
                    <input type="text" name="title" required>
                </div>
                <div class="form-group" style="flex:2;">
                    <label>Author</label>
                    <input type="text" name="author" required>
                </div>
                <div class="form-group" style="flex:2;">
                    <label>Category</label>
                    <input type="text" name="category" required>
                </div>
                <div class="form-group" style="flex:1;">
                    <label>Quantity</label>
                    <input type="number" name="quantity" min="1" required>
                </div>
                <button type="submit" class="btn" style="margin-bottom: 15px;">Add</button>
            </form>
        </div>

        <h3>Books List</h3>
        <form method="GET" action="librarian_books.jsp" class="toolbar">
            <input type="text" name="searchQuery" placeholder="Search by book title..." class="toolbar-grow">
            <button type="submit" class="btn">Search</button>
            <a href="librarian_books.jsp" class="btn btn-secondary">Clear</a>
        </form>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Category</th>
                    <th>Total Qty</th>
                    <th>Available</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    BookDAO bookDao = new BookDAO();
                    String searchQuery = request.getParameter("searchQuery");
                    List<Book> books;
                    if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                        books = bookDao.searchBooksByTitle(searchQuery.trim());
                    } else {
                        books = bookDao.getAllBooks();
                    }
                    for(Book b : books) {
                %>
                <tr>
                    <td><%= b.getId() %></td>
                    <td><%= b.getTitle() %></td>
                    <td><%= b.getAuthor() %></td>
                    <td><%= b.getCategory() %></td>
                    <td><%= b.getQuantity() %></td>
                    <td><b><%= b.getAvailable() %></b></td>
                    <td>
                        <div class="table-actions">
                            <form action="LibrarianServlet" method="POST" class="action-form" autocomplete="off">
                                <input type="hidden" name="action" value="addBookCopy">
                                <input type="hidden" name="bookId" value="<%= b.getId() %>">
                                <input type="hidden" name="source" value="books">
                                <input type="number" name="addCount" min="1" value="1">
                                <button type="submit" class="btn btn-small btn-success">Add</button>
                            </form>
                            <form action="LibrarianServlet" method="POST" class="action-form" autocomplete="off">
                                <input type="hidden" name="action" value="removeBookCopy">
                                <input type="hidden" name="bookId" value="<%= b.getId() %>">
                                <input type="hidden" name="source" value="books">
                                <input type="number" name="removeCount" min="1" max="<%= b.getQuantity() %>" value="1">
                                <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Ensure you want to remove these copies?');">Remove</button>
                            </form>
                        </div>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <script src="app.js"></script>
</body>
</html>
