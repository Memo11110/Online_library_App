package com.fsu.mobile.model;

import java.io.Serializable;

/**
 *
 */
public class Book implements Serializable{
    private int bookId;
    private String title;
    private String autor;
    private String imageCoverURL;
    private int quantity;
    private String categoryName;
    private String isbn;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getImageCoverURL() {
        return imageCoverURL;
    }

    public void setImageCoverURL(String imageCoverURL) {
        this.imageCoverURL = imageCoverURL;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
