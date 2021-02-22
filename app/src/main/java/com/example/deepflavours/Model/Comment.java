package com.example.deepflavours.Model;

public class Comment {

    private String comment;
    private String user;
    private String commentid;

    public Comment(String comment, String user,String commentid) {
        this.comment = comment;
        this.user = user;
        this.commentid = commentid;
    }

    public Comment(){

    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
