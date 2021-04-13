package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.deepflavours.Model.Note;
import com.example.deepflavours.Model.Recipe;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;


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
        final String nServings = servingsN.getText().toString().trim();
        final String nPrepTime = prepTimeN.getText().toString().trim();
        final String nCookTime = cookTimeN.getText().toString().trim();
        final String nIngredients = ingredientsN.getText().toString().trim();
        final String nDirections = directionsN.getText().toString().trim();

        boolean error = false;


        if (nTitle.length() > 25) {
            titleN.setError(getResources().getString(R.string.title_length));
            titleN.requestFocus();
            error = true;
        } else if (nTitle.isEmpty()) {
            titleN.setError(getResources().getString(R.string.empty_error));
            titleN.requestFocus();
            error = true;
        } else {
            titleN.setError(null);
            titleN.requestFocus();
        }


        if (nDescription.length() > 40) {
            descriptionN.setError(getResources().getString(R.string.description_length));
            descriptionN.requestFocus();
            error = true;
        } else if (nDescription.isEmpty()) {
            descriptionN.setError(getResources().getString(R.string.empty_error));
            descriptionN.requestFocus();
            error = true;
        } else {
            descriptionN.setError(null);
            descriptionN.requestFocus();
        }


        if (nServings.length() > 2) {
            servingsN.setError(getResources().getString(R.string.servings_length));
            servingsN.requestFocus();
            error = true;
        } else if (nServings.isEmpty()) {
            servingsN.setError(getResources().getString(R.string.empty_error));
            servingsN.requestFocus();
            error = true;
        } else {
            servingsN.setError(null);
            servingsN.requestFocus();
        }


        if (nPrepTime.length() > 3) {
            prepTimeN.setError(getResources().getString(R.string.prep_cook_length));
            prepTimeN.requestFocus();
            error = true;
        } else if (nPrepTime.isEmpty()) {
            prepTimeN.setError(getResources().getString(R.string.empty_error));
            prepTimeN.requestFocus();
            error = true;
        } else {
            prepTimeN.setError(null);
            prepTimeN.requestFocus();
        }


        if (nCookTime.length() > 3) {
            cookTimeN.setError(getResources().getString(R.string.prep_cook_length));
            cookTimeN.requestFocus();
            error = true;
        } else if (nCookTime.isEmpty()) {
            cookTimeN.setError(getResources().getString(R.string.empty_error));
            cookTimeN.requestFocus();
            error = true;
        } else {
            cookTimeN.setError(null);
            cookTimeN.requestFocus();
        }

        if (nIngredients.isEmpty()) {
            ingredientsN.setError(getResources().getString(R.string.empty_error));
            ingredientsN.requestFocus();
            error = true;
        } else {
            ingredientsN.setError(null);
            ingredientsN.requestFocus();
        }

        if (nDirections.isEmpty()) {
            directionsN.setError(getResources().getString(R.string.empty_error));
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
                            Toast.makeText(NotesActivity.this, getResources().getString(R.string.update_note), Toast.LENGTH_LONG).show();
                        }
                    }
                    if(!hasNote && updateNote.getNoteId()==null){
                        addNotes(postid);
                        Toast.makeText(NotesActivity.this, getResources().getString(R.string.add_note), Toast.LENGTH_LONG).show();
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