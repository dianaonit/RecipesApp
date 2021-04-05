package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.deepflavours.Adapter.AllMyPostsAdapter;
import com.example.deepflavours.Adapter.UserNotesAdapter;
import com.example.deepflavours.Model.Note;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserNotesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserNotesAdapter userNotesAdapter;
    private List<Note> noteList;
    private ImageView back;
    EditText searchBar;
    List<String> idList;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notes);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        searchBar = findViewById(R.id.searchbar_notes);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        back = findViewById(R.id.btn_backfrom_allnotes);
        recyclerView =findViewById(R.id.recyclerview_notes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        noteList = new ArrayList<>();


        userNotesAdapter = new UserNotesAdapter(this,noteList);
        recyclerView.setAdapter(userNotesAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        idList = new ArrayList<>();
        readNotes();

    }



    public void readNotes(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noteList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot notesSnapshot : snapshot.getChildren()){
                        Note note = notesSnapshot.getValue(Note.class);
                        if(note.getUserId().equals(firebaseUser.getUid())) {
                            noteList.add(note);
                        }
                    }
                }

                Collections.reverse(noteList);
                userNotesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void searchNote(String s) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noteList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                        Note note = noteSnapshot.getValue(Note.class);
                        String noteTitle = note.getTitle().toLowerCase();
                        if (noteTitle.contains(s.toLowerCase())) {
                           noteList.add(note);
                        }
                    }
                }
                userNotesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }





}