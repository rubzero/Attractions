package com.example.rubze.attraction;

public class User {
    private String id, name, email;
    private int photo;

    public User(String id, String name, String email){
        this.id=id;
        this.name=name;
        this.email=email;
        photo=0;
    }

    public User(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
