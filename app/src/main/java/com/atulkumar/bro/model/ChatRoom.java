package com.atulkumar.bro.model;

public class ChatRoom {

    private String chatRoomId;
    private String bookId;
    private String buyerId;
    private String sellerId,imageurl,lastmessage,recivername;


    public ChatRoom(String chatRoomId, String bookId, String buyerId, String sellerId, String imageurl, String recivername,String lastmessage ) {
        this.chatRoomId = chatRoomId;
        this.bookId = bookId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.imageurl = imageurl;
        this.recivername = recivername;
        this.lastmessage = lastmessage;

    }

    public ChatRoom() {
    }

    public String getRecivername() {
        return recivername;
    }

    public void setRecivername(String recivername) {
        this.recivername = recivername;
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

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
