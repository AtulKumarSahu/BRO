package com.atulkumar.bro.model;

import java.io.Serializable;

public class BookModel implements Serializable {
    private String bookName,publication,yearOrSemesterStr,imageUrl,decriptionStr,bookId,book_userid;

    public BookModel(String bookName, String publication, String yearOrSemesterStr, String imageUrl, String decriptionStr,String bookId,String book_userid) {
        this.bookName = bookName;
        this.publication = publication;
        this.yearOrSemesterStr = yearOrSemesterStr;
        this.imageUrl = imageUrl;
        this.decriptionStr = decriptionStr;
        this.bookId = bookId;
        this.book_userid = book_userid;

    }

    public BookModel() {
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getYearOrSemesterStr() {
        return yearOrSemesterStr;
    }

    public void setYearOrSemesterStr(String yearOrSemesterStr) {
        this.yearOrSemesterStr = yearOrSemesterStr;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDecriptionStr() {
        return decriptionStr;
    }

    public void setDecriptionStr(String decriptionStr) {
        this.decriptionStr = decriptionStr;
    }

    public String getBook_userid() {
        return book_userid;
    }

    public void setBook_userid(String book_userid) {
        this.book_userid = book_userid;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;

    }
}
