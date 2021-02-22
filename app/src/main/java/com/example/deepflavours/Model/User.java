package com.example.deepflavours.Model;

public class User {
    private String id;
    private String username;
    private String useremail;
    private String userpassword;
    private String bio;
    private String imageurl;
    private Boolean connected;

    public User(String id, String username, String useremail, String userpassword, String bio, String imageurl,Boolean connected) {
        this.id = id;
        this.username = username;
        this.useremail = useremail;
        this.userpassword = userpassword;
        this.bio = bio;
        this.imageurl = imageurl;
        this.connected = connected;
    }

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
