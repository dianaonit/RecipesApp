package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepflavours.Model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditNoteActivity extends AppCompatActivity {

    ImageView save, back;
    TextView nEditTitle, nEditDesc, nEditServings, nEditPrepTime, nEditCookTime, nEditIngredients, nEditDirections;

    FirebaseUser firebaseUser;

    String noteId;
    String recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        save = findViewById(R.id.btn_save_edit_notes);
        back = findViewById(R.id.btn_back_to_note);
        nEditTitle = findViewById(R.id.post_title_edit_notes);
        nEditDesc = findViewById(R.id.post_description_edit_notes);
        nEditServings = findViewById(R.id.servings_edit_notes);
        nEditPrepTime = findViewById(R.id.prep_time_edit_notes);
        nEditCookTime = findViewById(R.id.cook_time_edit_notes);
        nEditIngredients = findViewById(R.id.ingredients_edit_notes);
        nEditDirections = findViewById(R.id.directions_edit_notes);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        noteId = intent.getStringExtra("noteId");
        recipeId = intent.getStringExtra("recipeId");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(recipeId).child(noteId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Note note = snapshot.getValue(Note.class);
                nEditServings.setText(note.getServings());
                nEditTitle.setText(note.getTitle());
                nEditDesc.setText(note.getDescription());
                nEditCookTime.setText(note.getCookTime());
                nEditIngredients.setText(note.getIngredients());
                nEditPrepTime.setText(note.getPreparationTime());
                nEditDirections.setText(note.getDirections());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        final String nTitle = nEditTitle.getText().toString().trim();
        final String nDescription = nEditDesc.getText().toString().trim();
        final String nServings = nEditServings.getText().toString().trim();
        final String nPrepTime = nEditPrepTime.getText().toString().trim();
        final String nCookTime = nEditCookTime.getText().toString().trim();
        final String nIngredients = nEditIngredients.getText().toString().trim();
        final String nDirections = nEditDirections.getText().toString().trim();

        boolean error = false;


        if (nTitle.length() > 25) {
            nEditTitle.setError("Max length: 25 char!");
            nEditTitle.requestFocus();
            error = true;
        } else if (nTitle.isEmpty()) {
            nEditTitle.setError("Can't be empty!");
            nEditTitle.requestFocus();
            error = true;
        } else {
            nEditTitle.setError(null);
            nEditTitle.requestFocus();
        }


        if (nDescription.length() > 40) {
            nEditDesc.setError("Max length: 40 char!");
            nEditDesc.requestFocus();
            error = true;
        } else if (nDescription.isEmpty()) {
            nEditDesc.setError("Can't be empty!");
            nEditDesc.requestFocus();
            error = true;
        } else {
            nEditDesc.setError(null);
            nEditDesc.requestFocus();
        }


        if (nServings.length() > 2) {
            nEditServings.setError("Max length: 2 char!");
            nEditServings.requestFocus();
            error = true;
        } else if (nServings.isEmpty()) {
            nEditServings.setError("Can't be empty!");
            nEditServings.requestFocus();
            error = true;
        } else {
            nEditServings.setError(null);
            nEditServings.requestFocus();
        }


        if (nPrepTime.length() > 3) {
            nEditPrepTime.setError("Max length: 3 char!");
            nEditPrepTime.requestFocus();
            error = true;
        } else if (nPrepTime.isEmpty()) {
            nEditPrepTime.setError("Can't be empty!");
            nEditPrepTime.requestFocus();
            error = true;
        } else {
            nEditPrepTime.setError(null);
            nEditPrepTime.requestFocus();
        }


        if (nCookTime.length() > 3) {
            nEditCookTime.setError("Max length: 3 char!");
            nEditCookTime.requestFocus();
            error = true;
        } else if (nCookTime.isEmpty()) {
            nEditCookTime.setError("Can't be empty!");
            nEditCookTime.requestFocus();
            error = true;
        } else {
            nEditCookTime.setError(null);
            nEditCookTime.requestFocus();
        }

        if (nIngredients.isEmpty()) {
            nEditIngredients.setError("Can't be empty!");
            nEditIngredients.requestFocus();
            error = true;
        } else {
            nEditIngredients.setError(null);
            nEditIngredients.requestFocus();
        }

        if (nDirections.isEmpty()) {
            nEditDirections.setError("Can't be empty!");
            nEditDirections.requestFocus();
            error = true;
        } else {
            nEditDirections.setError(null);
            nEditDirections.requestFocus();
        }


        if (!error) {

            Note updateNote = new Note();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(recipeId).child(noteId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Note note = snapshot.getValue(Note.class);
                    if (note.getUserId().equals(firebaseUser.getUid()) && updateNote.getNoteId() == null) {
                        updateNote.setNoteId(note.getNoteId());
                        updateNote.setUserId(note.getUserId());
                        updateNote.setRecipeId(note.getRecipeId());
                        updateNote.setTitle(nEditTitle.getText().toString());
                        updateNote.setDescription(nEditDesc.getText().toString());
                        updateNote.setServings(nEditServings.getText().toString());
                        updateNote.setPreparationTime(nEditPrepTime.getText().toString());
                        updateNote.setCookTime(nEditCookTime.getText().toString());
                        updateNote.setIngredients(nEditIngredients.getText().toString());
                        updateNote.setDirections(nEditDirections.getText().toString());

                        updateNotes(note.getRecipeId(), updateNote);

                        Toast.makeText(EditNoteActivity.this, "Updated note!", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            finish();

        }


    }


    public void updateNotes(String postid, Note note) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(postid).child(note.getNoteId());

        HashMap<String, Object> updateNote = new HashMap<>();
        updateNote.put("userId", note.getUserId());
        updateNote.put("noteId", note.getNoteId());
        updateNote.put("title", note.getTitle());
        updateNote.put("description", note.getDescription());
        updateNote.put("servings", note.getServings());
        updateNote.put("preparationTime", note.getPreparationTime());
        updateNote.put("cookTime", note.getCookTime());
        updateNote.put("ingredients", note.getIngredients());
        updateNote.put("directions", note.getDirections());

        reference.updateChildren(updateNote);
    }


}