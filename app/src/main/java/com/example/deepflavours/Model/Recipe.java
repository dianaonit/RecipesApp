package com.example.deepflavours.Model;

import android.net.Uri;

public class Recipe {
//    private String key;
//    private Double ratingValue;
//    private Long ratingCount;
    private String recipeid;
    private String recipeimage;
    private String title;
    private String userid;
    private String ingredients;
    private String directions;
    private String description;
    private String servings;
    private String cooktime;
    private String preparationtime;


    public Recipe(String recipeid, String recipeimage, String title, String userid, String ingredients, String directions, String description, String servings,String cooktime, String preparationtime) {
        this.recipeid = recipeid;
        this.recipeimage = recipeimage;
        this.title = title;
        this.userid = userid;
        this.ingredients = ingredients;
        this.directions = directions;
        this.description = description;
        this.cooktime = cooktime;
        this.preparationtime = preparationtime;
        this.servings = servings;

    }

    public Recipe(){

    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getRecipeid() {
        return recipeid;
    }

    public void setRecipeid(String recipeid) {
        this.recipeid = recipeid;
    }

    public String getRecipeimage() {
        return recipeimage;
    }

    public void setRecipeimage(String recipeimage) {
        this.recipeimage = recipeimage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCooktime() {
        return cooktime;
    }

    public void setCooktime(String cooktime) {
        this.cooktime = cooktime;
    }

    public String getPreparationtime() {
        return preparationtime;
    }

    public void setPreparationtime(String preparationtime) {
        this.preparationtime = preparationtime;
    }

//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }
//
//    public Double getRatingValue() {
//        return ratingValue;
//    }
//
//    public void setRatingValue(Double ratingValue) {
//        this.ratingValue = ratingValue;
//    }
//
//    public Long getRatingCount() {
//        return ratingCount;
//    }
//
//    public void setRatingCount(Long ratingCount) {
//        this.ratingCount = ratingCount;
//    }
}
