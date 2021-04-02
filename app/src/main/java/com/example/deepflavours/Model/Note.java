package com.example.deepflavours.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Note {


    private String noteId;
    private String userId;
    private String title;
    private String ingredients;
    private String directions;
    private String description;
    private String servings;
    private String cookTime;
    private String preparationTime;


    public Note(){

    }

    public Note(String noteId, String userId, String title, String ingredients, String directions, String description, String servings, String cookTime, String preparationTime) {
        this.noteId = noteId;
        this.userId = userId;
        this.title = title;
        this.ingredients = ingredients;
        this.directions = directions;
        this.description = description;
        this.servings = servings;
        this.cookTime = cookTime;
        this.preparationTime = preparationTime;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }
}