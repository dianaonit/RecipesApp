package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.deepflavours.Adapter.CommentAdapter;
import com.example.deepflavours.Model.Comment;
import com.example.deepflavours.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    EditText addcomment;
    ImageView image_profile,back;
    TextView post;

    private RecyclerView recyclerViewComment;
    private CommentAdapter commentAdapter;
    private List<Comment> commentsList;


    String postid;
    String userid;

    FirebaseUser firebaseUser;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        userid = intent.getStringExtra("userid");


        back = findViewById(R.id.btn_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerViewComment= findViewById(R.id.recycler_view_comments);
        recyclerViewComment.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewComment.setLayoutManager(linearLayoutManager);

        commentsList= new ArrayList<>();
        commentAdapter = new CommentAdapter(this,commentsList,postid);
        recyclerViewComment.setAdapter(commentAdapter);



        addcomment = findViewById(R.id.add_comment);
        image_profile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post_comment);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addcomment.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this,getResources().getString(R.string.empty_comment),Toast.LENGTH_SHORT).show();
                }else{
                    addComment();
                }
            }
        });
            getImage();
            readComments();
    }



    private void addComment(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        String commentid =reference.push().getKey();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("comment",addcomment.getText().toString());
        hashMap.put("user",firebaseUser.getUid());
        hashMap.put("commentid",commentid);

        reference.child(commentid).setValue(hashMap);

        addNotifications();
        addcomment.setText("");
    }

    private void addNotifications(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text" , "commented:"+ addcomment.getText().toString());
        hashMap.put("postid", postid );
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);

    }


    private void getImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readComments(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               commentsList.clear();

               for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                   Comment comment = snapshot.getValue(Comment.class);
                   commentsList.add(comment);
               }

               commentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}