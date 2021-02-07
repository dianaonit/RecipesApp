package com.example.deepflavours.Model;

import android.net.Uri;

public class Recipe {

    private String recipeid;
    private String recipeimage;
    private String title;
    private String userid;
    private String ingredients;
    private String directions;
    private String descriprion;
    private String cooktime;
    private String preparationtime;


    public Recipe(String recipeid, String recipeimage, String title, String userid, String ingredients, String directions, String descriprion, String cooktime, String preparationtime) {
        this.recipeid = recipeid;
        this.recipeimage = recipeimage;
        this.title = title;
        this.userid = userid;
        this.ingredients = ingredients;
        this.directions = directions;
        this.descriprion = descriprion;
        this.cooktime = cooktime;
        this.preparationtime = preparationtime;
    }

    public Recipe(){

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

    public String getDescriprion() {
        return descriprion;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
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
}
