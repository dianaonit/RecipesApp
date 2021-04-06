package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deepflavours.Model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NoteDetailActivity extends AppCompatActivity {

    TextView noteServings, noteTitle, notePrepTime, noteCookTime, noteIngredients, noteDirections,noteDescription;
    ImageView back,editNote;
    Button btnIngredients, btnDirections;
    View lineIngredients, lineDirections;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        noteServings = findViewById(R.id.note_nrOfservings);
        noteTitle = findViewById(R.id.note_detail_title);
        notePrepTime = findViewById(R.id.note_timeOfprep);
        noteCookTime = findViewById(R.id.note_timeOfcook);
        noteIngredients = findViewById(R.id.note_ingredients);
        noteDirections = findViewById(R.id.note_directions);
        noteDescription = findViewById(R.id.note_description);
        btnIngredients = findViewById(R.id.btn_ingredients_note);
        btnDirections = findViewById(R.id.btn_directions_note);
        lineIngredients = findViewById(R.id.line_ingredients_note);
        lineDirections = findViewById(R.id.line_directions_note);


        editNote = findViewById(R.id.edit_note);


        btnIngredients.setTextColor(Color.parseColor("#000000"));
        btnDirections.setTextColor(Color.parseColor("#aaa480"));
        noteIngredients.setVisibility(View.VISIBLE);
        noteDirections.setVisibility(View.GONE);

        btnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnIngredients.setTextColor(Color.parseColor("#000000"));
                btnDirections.setTextColor(Color.parseColor("#aaa480"));
                lineIngredients.setVisibility(View.VISIBLE);
                noteIngredients.setVisibility(View.VISIBLE);
                noteDirections.setVisibility(View.GONE);
                lineDirections.setVisibility(View.GONE);
            }
        });

        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnIngredients.setTextColor(Color.parseColor("#aaa480"));
                btnDirections.setTextColor(Color.parseColor("#000000"));
                noteIngredients.setVisibility(View.GONE);
                lineIngredients.setVisibility(View.GONE);
                noteDirections.setVisibility(View.VISIBLE);
                lineDirections.setVisibility(View.VISIBLE);
            }
        });


        back = findViewById(R.id.btn_backfrom_detailnote);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();
        String noteId = intent.getStringExtra("noteId");
        String recipeId = intent.getStringExtra("recipeId");

        editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteDetailActivity.this, EditNoteActivity.class);
                intent.putExtra("noteId",noteId);
                intent.putExtra("recipeId", recipeId);
                NoteDetailActivity.this.startActivity(intent);
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(recipeId).child(noteId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Note note = snapshot.getValue(Note.class);
                noteServings.setText(note.getServings());
                noteTitle.setText(note.getTitle());
                noteDescription.setText(note.getDescription());
                noteCookTime.setText(note.getCookTime());
                noteIngredients.setText(note.getIngredients());
                notePrepTime.setText(note.getPreparationTime());
                noteDirections.setText(note.getDirections());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}