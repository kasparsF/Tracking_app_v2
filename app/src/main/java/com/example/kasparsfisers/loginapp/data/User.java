package com.example.kasparsfisers.loginapp.data;


public class User {

    private String name;
    private String email;
    private String ImgId;

    public User(String name, String email, String image) {
        this.name = name;
        this.email = email;
        this.ImgId = image;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageId() {
        return ImgId;
    }

    public void setImageId(String id) {
        ImgId = id;
    }

    public boolean hasImage() {
        return !ImgId.equals("");
    }
}