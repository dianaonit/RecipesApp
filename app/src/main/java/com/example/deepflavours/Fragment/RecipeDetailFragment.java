package com.example.deepflavours.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.deepflavours.Adapter.PostAdapter;
import com.example.deepflavours.Adapter.PostDetailAdapter;
import com.example.deepflavours.MainActivity;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RecipeDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostDetailAdapter postDetailAdapter;
    private List<Recipe> postList;

    String postid;
    String profileid;

    ImageView back;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_detail,container,false);

        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postid = preferences.getString("postid", "none");

        back = view.findViewById(R.id.btn_back_detailpost);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view_postdetail);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postList= new ArrayList<>();
        postDetailAdapter = new PostDetailAdapter(getContext(),postList);

        recyclerView.setAdapter(postDetailAdapter);

        readPost();

        return view;
    }


    private void readPost(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                Recipe post = dataSnapshot.getValue(Recipe.class);
                postList.add(post);

                postDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





}