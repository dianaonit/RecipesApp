package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

    ImageView close, save;
    MaterialEditText title, description, servings, prepTime, cookTime, ingredients, directions;

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
        final String rSevings = servings.getText().toString().trim();
        final String rPrepTime = prepTime.getText().toString().trim();
        final String rCookTime = cookTime.getText().toString().trim();
        final String rIngredients = ingredients.getText().toString().trim();
        final String rDirections = directions.getText().toString().trim();

        boolean error = false;


        if (rTitle.length() > 25) {
            title.setError("Max length: 25 char!");
            title.requestFocus();
            error = true;
        } else if (rTitle.isEmpty()) {
            title.setError("Can't be empty!");
            title.requestFocus();
            error = true;
        } else {
            title.setError(null);
            title.requestFocus();
        }


        if (rDescription.length() > 40) {
            description.setError("Max length: 40 char!");
            description.requestFocus();
            error = true;
        } else if (rDescription.isEmpty()) {
            description.setError("Can't be empty!");
            description.requestFocus();
            error = true;
        } else {
            description.setError(null);
            description.requestFocus();
        }


        if (rSevings.length() > 2) {
            servings.setError("Max length: 2 char!");
            servings.requestFocus();
            error = true;
        } else if (rSevings.isEmpty()) {
            servings.setError("Can't be empty!");
            servings.requestFocus();
            error = true;
        } else {
            servings.setError(null);
            servings.requestFocus();
        }


        if (rPrepTime.length() > 3) {
            prepTime.setError("Max length: 3 char!");
            prepTime.requestFocus();
            error = true;
        } else if (rPrepTime.isEmpty()) {
            prepTime.setError("Can't be empty!");
            prepTime.requestFocus();
            error = true;
        } else {
            prepTime.setError(null);
            prepTime.requestFocus();
        }


        if (rCookTime.length() > 3) {
            cookTime.setError("Max length: 3 char!");
            cookTime.requestFocus();
            error = true;
        } else if (rCookTime.isEmpty()) {
            cookTime.setError("Can't be empty!");
            cookTime.requestFocus();
            error = true;
        } else {
            cookTime.setError(null);
            cookTime.requestFocus();
        }

        if (rIngredients.isEmpty()) {
            ingredients.setError("Can't be empty!");
            ingredients.requestFocus();
            error = true;
        } else {
            ingredients.setError(null);
            ingredients.requestFocus();
        }

        if (rDirections.isEmpty()) {
            directions.setError("Can't be empty!");
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