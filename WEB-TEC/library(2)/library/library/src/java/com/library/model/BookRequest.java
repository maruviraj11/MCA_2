package com.library.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class BookRequest {
    private int id;
    private int userId;
    private int bookId;
    private Timestamp requestDate;
    private Timestamp issueDate;
    private Date dueDate;
    private Timestamp returnDate;
    private BigDecimal fineAmount;
    private boolean finePaid;
    private Timestamp finePaidDate;
    private BigDecimal paidAmount;
    private String status; // 'pending', 'issued', 'returned', 'rejected'
    
    // For joining display
    private String userName;
    private String bookTitle;

    public BookRequest() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public Timestamp getRequestDate() { return requestDate; }
    public void setRequestDate(Timestamp requestDate) { this.requestDate = requestDate; }

    public Timestamp getIssueDate() { return issueDate; }
    public void setIssueDate(Timestamp issueDate) { this.issueDate = issueDate; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Timestamp getReturnDate() { return returnDate; }
    public void setReturnDate(Timestamp returnDate) { this.returnDate = returnDate; }

    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }

    public boolean isFinePaid() { return finePaid; }
    public void setFinePaid(boolean finePaid) { this.finePaid = finePaid; }

    public Timestamp getFinePaidDate() { return finePaidDate; }
    public void setFinePaidDate(Timestamp finePaidDate) { this.finePaidDate = finePaidDate; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
}
