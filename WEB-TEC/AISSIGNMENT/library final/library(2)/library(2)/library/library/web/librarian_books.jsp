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
                <button type="submit" class="btn" style="margin-bottom: 15px;">Add</button>
            </form>
        </div>

        <div class="panel panel--compact" style="margin-bottom: 22px;">
            <h3>Edit Book</h3>
            <p class="muted" style="margin-top:-10px;">Click Edit from the list to auto-fill this form.</p>
            <form action="LibrarianServlet" method="POST" class="flex-form" autocomplete="off" id="editBookForm">
                <input type="hidden" name="action" value="editBook">
                <input type="hidden" name="source" value="books">
                <div class="form-group" style="flex:1;">
                    <label>Book ID</label>
                    <input type="number" id="editBookId" name="bookId" required min="1" readonly>
                </div>
                <div class="form-group" style="flex:2;">
                    <label>Title</label>
                    <input type="text" id="editBookTitle" name="title" required>
                </div>
                <div class="form-group" style="flex:2;">
                    <label>Author</label>
                    <input type="text" id="editBookAuthor" name="author" required>
                </div>
                <div class="form-group" style="flex:2;">
                    <label>Category</label>
                    <input type="text" id="editBookCategory" name="category" required>
                </div>
                <button type="submit" class="btn" style="margin-bottom: 15px;">Update</button>
                <button type="button" class="btn btn-secondary js-clear-edit-book" style="margin-bottom: 15px;">Clear</button>
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
                    <td>
                        <div class="table-actions">
                            <button
                                type="button"
                                class="btn btn-small btn-secondary js-edit-book"
                                data-id="<%= b.getId() %>">
                                Edit
                            </button>
                            <form action="LibrarianServlet" method="POST" class="action-form" autocomplete="off">
                                <input type="hidden" name="action" value="removeBookCopy">
                                <input type="hidden" name="bookId" value="<%= b.getId() %>">
                                <input type="hidden" name="source" value="books">
                                <input type="hidden" name="removeCount" value="1">
                                <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Ensure you want to remove 1 copy?');">Remove</button>
                            </form>
                        </div>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <script src="app.js"></script>
    <script>
        (function () {
            function $(id) { return document.getElementById(id); }

            window.addEventListener("DOMContentLoaded", function () {
                var editForm = $("editBookForm");
                var idInput = $("editBookId");
                var titleInput = $("editBookTitle");
                var authorInput = $("editBookAuthor");
                var categoryInput = $("editBookCategory");
                if (!editForm || !idInput || !titleInput || !authorInput || !categoryInput) return;

                var buttons = document.querySelectorAll(".js-edit-book");
                for (var i = 0; i < buttons.length; i++) {
                    buttons[i].addEventListener("click", function () {
                        var row = this;
                        while (row && row.tagName !== "TR") row = row.parentNode;
                        if (!row) return;

                        var cells = row.querySelectorAll("td");
                        if (!cells || cells.length < 4) return;

                        idInput.value = (cells[0].textContent || "").trim() || (this.getAttribute("data-id") || "");
                        titleInput.value = (cells[1].textContent || "").trim();
                        authorInput.value = (cells[2].textContent || "").trim();
                        categoryInput.value = (cells[3].textContent || "").trim();
                        titleInput.focus();
                        editForm.scrollIntoView({ behavior: "smooth", block: "start" });
                    });
                }

                var clearBtn = document.querySelector(".js-clear-edit-book");
                if (clearBtn) {
                    clearBtn.addEventListener("click", function () {
                        idInput.value = "";
                        titleInput.value = "";
                        authorInput.value = "";
                        categoryInput.value = "";
                        titleInput.focus();
                    });
                }
            });
        })();
    </script>
</body>
</html>
