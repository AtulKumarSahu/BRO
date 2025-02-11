package com.atulkumar.bro.model;

public class Partcipents {
    public String buyerId;
    public String sellerId,bookid,imageurl,lastmessage;



    public Partcipents(String buyerId, String sellerId, String bookid, String imageurl, String lastmessage) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.bookid = bookid;
        this.imageurl = imageurl;
        this.lastmessage = lastmessage;
    }

    public Partcipents() {
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String userName) {
        this.sellerId = sellerId;
    }
}
