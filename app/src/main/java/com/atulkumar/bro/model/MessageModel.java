package com.atulkumar.bro.model;

public class MessageModel {
    private String message;
    private String senderId;
    private String reciverId;
    private long timestamp;

    public MessageModel(String message, String senderId, long timestamp) {
        this.message = message;
        this.senderId = senderId;

        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public MessageModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

}
