package com.fsu.mobile.model;

import java.io.Serializable;

/**
 *
 */
public class Return implements Serializable{

    private int returnId;
    private String bookTitle;
    private String copyAN;
    private String date;

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
