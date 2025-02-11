package com.atulkumar.bro.model;

public class UserModel {
    public String name;
    public String email,userImage;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserModel(String name, String email, String userImage) {
        this.name = name;
        this.email = email;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
