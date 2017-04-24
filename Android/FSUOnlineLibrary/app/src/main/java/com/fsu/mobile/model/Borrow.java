package com.fsu.mobile.model;

import java.io.Serializable;

/**
 *
 */
public class Borrow implements Serializable {
    private int borrowId;
    private String bookTitle;
    private String copyAN;
    private String date;
    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getCopyAN() {
        return copyAN;
    }

    public void setCopyAN(String copyAN) {
        this.copyAN = copyAN;
    }

    public int getBorrowId() {

        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
