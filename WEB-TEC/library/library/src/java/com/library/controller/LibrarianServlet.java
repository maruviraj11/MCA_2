package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.RequestDAO;
import com.library.model.Book;
import com.library.util.Validation;
import com.library.util.WebUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LibrarianServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        String source = request.getParameter("source");
        String redirectTarget = "books".equals(source) ? "librarian_books.jsp" : 
                              "requests".equals(source) ? "librarian_requests.jsp" : 
                              "librarian_dashboard.jsp";

        if ("addBook".equals(action)) {
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String category = request.getParameter("category");
            Integer quantity = Validation.parsePositiveInt(request.getParameter("quantity"));

            if (Validation.isBlank(title) || Validation.isBlank(author) || Validation.isBlank(category) || quantity == null) {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Please fill all fields correctly."));
                return;
            }

            title = title.trim();
            author = author.trim();
            category = category.trim();

            if (!Validation.isAlphaSpace(author)) {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Author name must contain only alphabets and spaces."));
                return;
            }
            
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setCategory(category);
            book.setQuantity(quantity.intValue());
            book.setAvailable(quantity.intValue());
            
            BookDAO dao = new BookDAO();
            if (dao.bookExists(title, author, category)) {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Duplicate book entry. Use Add copies for existing book."));
                return;
            }
            if (dao.addBook(book)) {
                response.sendRedirect(redirectTarget + "?msg=" + WebUtil.enc("Book Added Successfully"));
            } else {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Failed to add Book"));
            }
        } else if ("updateRequest".equals(action)) {
            Integer requestId = Validation.parsePositiveInt(request.getParameter("requestId"));
            String status = request.getParameter("status");
            Integer bookId = Validation.parsePositiveInt(request.getParameter("bookId"));

            if (requestId == null || bookId == null || Validation.isBlank(status)) {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Invalid request."));
                return;
            }
            
            RequestDAO dao = new RequestDAO();
            if (dao.updateRequestStatus(requestId.intValue(), status, bookId.intValue())) {
                response.sendRedirect(redirectTarget + "?msg=" + WebUtil.enc("Request Updated Successfully"));
            } else {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Failed to update Request"));
            }
        } else if ("removeBookCopy".equals(action)) {
            Integer bookId = Validation.parsePositiveInt(request.getParameter("bookId"));
            Integer removeCount = Validation.parsePositiveInt(request.getParameter("removeCount"));
            if (bookId == null || removeCount == null) {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Invalid book quantity."));
                return;
            }
            
            BookDAO dao = new BookDAO();
            boolean success = dao.removeBookCopies(bookId.intValue(), removeCount.intValue());
            
            if (success) {
                int newQty = dao.getBookQuantity(bookId.intValue());
                if (newQty <= 0) {
                    dao.deleteBook(bookId.intValue());
                    response.sendRedirect(redirectTarget + "?msg=" + WebUtil.enc("Book entirely removed (0 copies left)"));
                } else {
                    response.sendRedirect(redirectTarget + "?msg=" + WebUtil.enc(removeCount + " copies removed successfully"));
                }
            } else {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Failed to remove copy"));
            }
        } else if ("addBookCopy".equals(action)) {
            Integer bookId = Validation.parsePositiveInt(request.getParameter("bookId"));
            Integer addCount = Validation.parsePositiveInt(request.getParameter("addCount"));
            if (bookId == null || addCount == null) {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Invalid book quantity."));
                return;
            }
            
            BookDAO dao = new BookDAO();
            boolean success = dao.addBookCopies(bookId.intValue(), addCount.intValue());
            
            if (success) {
                response.sendRedirect(redirectTarget + "?msg=" + WebUtil.enc(addCount + " copies added successfully"));
            } else {
                response.sendRedirect(redirectTarget + "?error=" + WebUtil.enc("Failed to add copies"));
            }
        }
    }
}
