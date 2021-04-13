package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.deepflavours.Model.Recipe;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

    ImageView close, save;

    TextInputEditText title, description, servings, prepTime, cookTime, ingredients, directions;
    String postid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_post);

        close = findViewById(R.id.btn_close);
        save = findViewById(R.id.btn_save);
        title = findViewById(R.id.post_title);
        description = findViewById(R.id.post_description);
        servings = findViewById(R.id.servings);
        prepTime = findViewById(R.id.prep_time);
        cookTime = findViewById(R.id.cook_time);
        ingredients = findViewById(R.id.ingredients);
        directions = findViewById(R.id.directions);

        SharedPreferences preferences = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        postid = preferences.getString("postid", "none");


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                title.setText(recipe.getTitle());
                description.setText(recipe.getDescription());
                servings.setText(recipe.getServings());
                prepTime.setText(recipe.getPreparationtime());
                cookTime.setText(recipe.getCooktime());
                ingredients.setText(recipe.getIngredients());
                directions.setText(recipe.getDirections());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageValidationError();
            }

        });


    }


    private void messageValidationError() {

        final String rTitle = title.getText().toString().trim();
        final String rDescription = description.getText().toString().trim();
        final String rServings = servings.getText().toString().trim();
        final String rPrepTime = prepTime.getText().toString().trim();
        final String rCookTime = cookTime.getText().toString().trim();
        final String rIngredients = ingredients.getText().toString().trim();
        final String rDirections = directions.getText().toString().trim();

        boolean error = false;


        if (rTitle.length() > 25) {
            title.setError(getResources().getString(R.string.title_length));
            title.requestFocus();
            error = true;
        } else if (rTitle.isEmpty()) {
            title.setError(getResources().getString(R.string.empty_error));
            title.requestFocus();
            error = true;
        } else {
            title.setError(null);
            title.requestFocus();
        }


        if (rDescription.length() > 40) {
            description.setError(getResources().getString(R.string.description_length));
            description.requestFocus();
            error = true;
        } else if (rDescription.isEmpty()) {
            description.setError(getResources().getString(R.string.empty_error));
            description.requestFocus();
            error = true;
        } else {
            description.setError(null);
            description.requestFocus();
        }


        if (rServings.length() > 2) {
            servings.setError(getResources().getString(R.string.servings_length));
            servings.requestFocus();
            error = true;
        } else if (rServings.isEmpty()) {
            servings.setError(getResources().getString(R.string.empty_error));
            servings.requestFocus();
            error = true;
        } else {
            servings.setError(null);
            servings.requestFocus();
        }


        if (rPrepTime.length() > 3) {
            prepTime.setError(getResources().getString(R.string.prep_cook_length));
            prepTime.requestFocus();
            error = true;
        } else if (rPrepTime.isEmpty()) {
            prepTime.setError(getResources().getString(R.string.empty_error));
            prepTime.requestFocus();
            error = true;
        } else {
            prepTime.setError(null);
            prepTime.requestFocus();
        }


        if (rCookTime.length() > 3) {
            cookTime.setError(getResources().getString(R.string.prep_cook_length));
            cookTime.requestFocus();
            error = true;
        } else if (rCookTime.isEmpty()) {
            cookTime.setError(getResources().getString(R.string.empty_error));
            cookTime.requestFocus();
            error = true;
        } else {
            cookTime.setError(null);
            cookTime.requestFocus();
        }

        if (rIngredients.isEmpty()) {
            ingredients.setError(getResources().getString(R.string.empty_error));
            ingredients.requestFocus();
            error = true;
        } else {
            ingredients.setError(null);
            ingredients.requestFocus();
        }

        if (rDirections.isEmpty()) {
            directions.setError(getResources().getString(R.string.empty_error));
            directions.requestFocus();
            error = true;
        } else {
            directions.setError(null);
            directions.requestFocus();
        }

        if (!error) {

            editPost(title.getText().toString(), description.getText().toString(), servings.getText().toString(), prepTime.getText().toString(), cookTime.getText().toString(), ingredients.getText().toString(), directions.getText().toString());
            finish();

        }


    }


    private void editPost(String title, String description, String servings, String preparationTime, String cookTime, String ingredients, String directions) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes").child(postid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", title);
        hashMap.put("description", description);
        hashMap.put("servings", servings);
        hashMap.put("preparationtime", preparationTime);
        hashMap.put("cooktime", cookTime);
        hashMap.put("ingredients", ingredients);
        hashMap.put("directions", directions);

        reference.updateChildren(hashMap);

    }


}