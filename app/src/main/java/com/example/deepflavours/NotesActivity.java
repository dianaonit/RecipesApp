package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.deepflavours.Model.Note;
import com.example.deepflavours.Model.Rating;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    ImageView back, save;
    TextInputEditText titleN, descriptionN, servingsN, prepTimeN, cookTimeN, ingredientsN, directionsN;
    String postid;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        back = findViewById(R.id.btn_back_post);
        save = findViewById(R.id.btn_save_notes);
        titleN = findViewById(R.id.post_title_notes);
        descriptionN = findViewById(R.id.post_description_notes);
        servingsN = findViewById(R.id.servings_notes);
        prepTimeN = findViewById(R.id.prep_time_notes);
        cookTimeN = findViewById(R.id.cook_time_notes);
        ingredientsN = findViewById(R.id.ingredients_notes);
        directionsN = findViewById(R.id.directions_notes);



        SharedPreferences preferences = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        postid = preferences.getString("postid", "none");

        DatabaseReference referenceNote = FirebaseDatabase.getInstance().getReference("Notes").child(postid);
        referenceNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean hasNote = false;
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Note note = dataSnapshot.getValue(Note.class);
                        if (note.getUserId() != null) {
                            if (note.getUserId().equals(firebaseUser.getUid())) {
                                hasNote = true;
                                titleN.setText(note.getTitle());
                                descriptionN.setText(note.getDescription());
                                servingsN.setText(note.getServings());
                                prepTimeN.setText(note.getPreparationTime());
                                cookTimeN.setText(note.getCookTime());
                                ingredientsN.setText(note.getIngredients());
                                directionsN.setText(note.getDirections());
                            }
                        }
                    }
                }
                if (!hasNote) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes").child(postid);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Recipe recipe = dataSnapshot.getValue(Recipe.class);
                            titleN.setText(recipe.getTitle());
                            descriptionN.setText(recipe.getDescription());
                            servingsN.setText(recipe.getServings());
                            prepTimeN.setText(recipe.getPreparationtime());
                            cookTimeN.setText(recipe.getCooktime());
                            ingredientsN.setText(recipe.getIngredients());
                            directionsN.setText(recipe.getDirections());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
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

        final String nTitle = titleN.getText().toString().trim();
        final String nDescription = descriptionN.getText().toString().trim();
        final String nSevings = servingsN.getText().toString().trim();
        final String nPrepTime = prepTimeN.getText().toString().trim();
        final String nCookTime = cookTimeN.getText().toString().trim();
        final String nIngredients = ingredientsN.getText().toString().trim();
        final String nDirections = directionsN.getText().toString().trim();

        boolean error = false;


        if (nTitle.length() > 25) {
            titleN.setError("Max length: 25 char!");
            titleN.requestFocus();
            error = true;
        } else if (nTitle.isEmpty()) {
            titleN.setError("Can't be empty!");
            titleN.requestFocus();
            error = true;
        } else {
            titleN.setError(null);
            titleN.requestFocus();
        }


        if (nDescription.length() > 40) {
            descriptionN.setError("Max length: 40 char!");
            descriptionN.requestFocus();
            error = true;
        } else if (nDescription.isEmpty()) {
            descriptionN.setError("Can't be empty!");
            descriptionN.requestFocus();
            error = true;
        } else {
            descriptionN.setError(null);
            descriptionN.requestFocus();
        }


        if (nSevings.length() > 2) {
            servingsN.setError("Max length: 2 char!");
            servingsN.requestFocus();
            error = true;
        } else if (nSevings.isEmpty()) {
            servingsN.setError("Can't be empty!");
            servingsN.requestFocus();
            error = true;
        } else {
            servingsN.setError(null);
            servingsN.requestFocus();
        }


        if (nPrepTime.length() > 3) {
            prepTimeN.setError("Max length: 3 char!");
            prepTimeN.requestFocus();
            error = true;
        } else if (nPrepTime.isEmpty()) {
            prepTimeN.setError("Can't be empty!");
            prepTimeN.requestFocus();
            error = true;
        } else {
            prepTimeN.setError(null);
            prepTimeN.requestFocus();
        }


        if (nCookTime.length() > 3) {
            cookTimeN.setError("Max length: 3 char!");
            cookTimeN.requestFocus();
            error = true;
        } else if (nCookTime.isEmpty()) {
            cookTimeN.setError("Can't be empty!");
            cookTimeN.requestFocus();
            error = true;
        } else {
            cookTimeN.setError(null);
            cookTimeN.requestFocus();
        }

        if (nIngredients.isEmpty()) {
            ingredientsN.setError("Can't be empty!");
            ingredientsN.requestFocus();
            error = true;
        } else {
            ingredientsN.setError(null);
            ingredientsN.requestFocus();
        }

        if (nDirections.isEmpty()) {
            directionsN.setError("Can't be empty!");
            directionsN.requestFocus();
            error = true;
        } else {
            directionsN.setError(null);
            directionsN.requestFocus();
        }


        if (!error) {


            Note updateNote = new Note();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(postid);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean hasNote=false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Note note = dataSnapshot.getValue(Note.class);
                        if (note.getUserId().equals(firebaseUser.getUid()) && updateNote.getNoteId()==null) {
                            hasNote=true;
                            updateNote.setNoteId(note.getNoteId());
                            updateNote.setUserId(note.getUserId());
                            updateNote.setRecipeId(note.getRecipeId());
                            updateNote.setTitle(titleN.getText().toString());
                            updateNote.setDescription(descriptionN.getText().toString());
                            updateNote.setServings(servingsN.getText().toString());
                            updateNote.setPreparationTime(prepTimeN.getText().toString());
                            updateNote.setCookTime(cookTimeN.getText().toString());
                            updateNote.setIngredients(ingredientsN.getText().toString());
                            updateNote.setDirections(directionsN.getText().toString());

                            updateNotes(postid, updateNote);
                            Toast.makeText(NotesActivity.this, "Updated note!", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(!hasNote && updateNote.getNoteId()==null){
                        addNotes(postid);
                        Toast.makeText(NotesActivity.this, "Just added a note!", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            finish();

        }


    }

    private void addNotes(String postid) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(postid);
        String noteId = reference.push().getKey();

        HashMap<String, Object> notesHashMap = new HashMap<>();
        notesHashMap.put("userId", firebaseUser.getUid());
        notesHashMap.put("recipeId", postid);
        notesHashMap.put("noteId", noteId);
        notesHashMap.put("title", titleN.getText().toString());
        notesHashMap.put("description", descriptionN.getText().toString());
        notesHashMap.put("servings", servingsN.getText().toString());
        notesHashMap.put("preparationTime", prepTimeN.getText().toString());
        notesHashMap.put("cookTime", cookTimeN.getText().toString());
        notesHashMap.put("ingredients", ingredientsN.getText().toString());
        notesHashMap.put("directions", directionsN.getText().toString());

        reference.child(noteId).setValue(notesHashMap);
    }


    public void updateNotes(String postid, Note note) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(postid).child(note.getNoteId());

        HashMap<String, Object> updateNote = new HashMap<>();
        updateNote.put("userId", note.getUserId());
        updateNote.put("noteId", note.getNoteId());
        updateNote.put("title", note.getTitle());
        updateNote.put("description", note.getDescription());
        updateNote.put("servings", note.getServings());
        updateNote.put("preparationTime",note.getPreparationTime());
        updateNote.put("cookTime", note.getCookTime());
        updateNote.put("ingredients", note.getIngredients());
        updateNote.put("directions", note.getDirections());

        reference.updateChildren(updateNote);
    }




}