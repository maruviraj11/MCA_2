package com.library.dao;

import com.library.model.Book;
import com.library.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public boolean bookExists(String title, String author, String category) {
        String query = "SELECT 1 FROM books WHERE LOWER(title) = LOWER(?) AND LOWER(author) = LOWER(?) AND LOWER(category) = LOWER(?) LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, category);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean bookExistsExcludingId(int bookId, String title, String author, String category) {
        String query = "SELECT 1 FROM books WHERE id <> ? AND LOWER(title) = LOWER(?) AND LOWER(author) = LOWER(?) AND LOWER(category) = LOWER(?) LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookId);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setString(4, category);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addBook(Book book) {
        String query = "INSERT INTO books (title, author, category, quantity, available) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setInt(4, book.getQuantity());
            ps.setInt(5, book.getAvailable());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> getAllBooks() {
        return getBooksByQuery("SELECT * FROM books");
    }

    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE title LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             ps.setString(1, "%" + title + "%");
             try (ResultSet rs = ps.executeQuery()) {
                 while (rs.next()) {
                     books.add(extractBookFromResultSet(rs));
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> sortBooksBy(String criteria) {
        String query = "SELECT * FROM books ORDER BY title ASC";
        if ("author".equalsIgnoreCase(criteria)) {
            query = "SELECT * FROM books ORDER BY author ASC";
        } else if ("category".equalsIgnoreCase(criteria)) {
            query = "SELECT * FROM books ORDER BY category ASC";
        }
        return getBooksByQuery(query);
    }
    
    private List<Book> getBooksByQuery(String query) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setCategory(rs.getString("category"));
        book.setQuantity(rs.getInt("quantity"));
        book.setAvailable(rs.getInt("available"));
        return book;
    }

    public boolean updateBookDetails(int bookId, String title, String author, String category) {
        String query = "UPDATE books SET title = ?, author = ?, category = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, category);
            ps.setInt(4, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBookAvailability(int bookId, int availableChange) {
        String query = "UPDATE books SET available = available + ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, availableChange);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean removeBookCopies(int bookId, int count) {
        String query = "UPDATE books SET quantity = quantity - ?, available = GREATEST(0, available - ?) WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, count);
            ps.setInt(2, count);
            ps.setInt(3, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addBookCopies(int bookId, int count) {
        String query = "UPDATE books SET quantity = quantity + ?, available = available + ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, count);
            ps.setInt(2, count);
            ps.setInt(3, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getBookQuantity(int bookId) {
        String query = "SELECT quantity FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             ps.setInt(1, bookId);
             try (ResultSet rs = ps.executeQuery()) {
                 if (rs.next()) {
                     return rs.getInt("quantity");
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteBook(int bookId) {
        String requestQuery = "DELETE FROM requests WHERE book_id = ?";
        String bookQuery = "DELETE FROM books WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
             conn.setAutoCommit(false);
             try (PreparedStatement psReq = conn.prepareStatement(requestQuery);
                  PreparedStatement psBook = conn.prepareStatement(bookQuery)) {
                  
                  psReq.setInt(1, bookId);
                  psReq.executeUpdate();
                  
                  psBook.setInt(1, bookId);
                  int res = psBook.executeUpdate();
                  
                  conn.commit();
                  return res > 0;
             } catch (SQLException ex) {
                 conn.rollback();
                 ex.printStackTrace();
                 return false;
             }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
