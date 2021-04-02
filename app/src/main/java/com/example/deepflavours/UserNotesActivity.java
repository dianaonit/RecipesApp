package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.deepflavours.Adapter.AllMyPostsAdapter;
import com.example.deepflavours.Adapter.UserNotesAdapter;
import com.example.deepflavours.Model.Note;
import com.example.deepflavours.Model.Recipe;
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

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notes);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        back = findViewById(R.id.btn_backfrom_detailpost);
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

}