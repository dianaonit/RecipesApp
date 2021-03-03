package com.example.deepflavours.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
    private String previousActivity = null;

    String postid;
    String profileid;

    ImageView back;

    public RecipeDetailFragment() {

    }

    public RecipeDetailFragment(String postid) {
        this.postid = postid;
    }

    public RecipeDetailFragment(String previousActivity, String postid) {
        this.previousActivity = previousActivity;
        this.postid = postid;
    }

    public RecipeDetailFragment(String previousActivity, String postid, String profileid) {
        this.previousActivity = previousActivity;
        this.postid = postid;
        this.profileid = profileid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String sourceFragment = preferences.getString("fragment", "none");
        if (sourceFragment.equals("none")) {
            SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
            editor.putString("fragment", previousActivity);
            editor.apply();
        }
        if(previousActivity == null){
            previousActivity = sourceFragment;
        }
        postid = preferences.getString("postid", "none");

        back = view.findViewById(R.id.btn_back_detailpost);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (previousActivity) {
                    case "ProfileFragment":
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ProfileFragment(profileid)).commit();
                        break;
                    case "HomeFragment":
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();
                        break;
                    case "SaveFragment":
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SaveFragment(postid)).commit();
                        break;
                    case "SearchFragment":
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SearchFragment()).commit();
                        break;
                    case "AllMyPostFragment":
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AllMyPostFragment(profileid)).commit();
                        break;
                }
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view_postdetail);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postList = new ArrayList<>();
        postDetailAdapter = new PostDetailAdapter(getContext(), postList, profileid);

        recyclerView.setAdapter(postDetailAdapter);

        readPost();

        return view;
    }


    private void readPost() {

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