package com.example.deepflavours.Model;

import java.util.Map;

public class Rating {
    private int userRatingValue;
    private String ratingId;
    private String userId;

    public Rating(){

    }

    public Rating( String userId,String ratingId,int userRatingValue) {

        this.userId = userId;
        this.ratingId = ratingId;
        this.userRatingValue = userRatingValue;
    }

    public int getUserRatingValue() {
        return userRatingValue;
    }

    public void setUserRatingValue(int userRatingValue) {
        this.userRatingValue = userRatingValue;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



}
