package com.example.deepflavours.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.deepflavours.Adapter.AllMyPostsAdapter;
import com.example.deepflavours.MainActivity;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllMyPostFragment extends Fragment {


    private RecyclerView recyclerView;
    private AllMyPostsAdapter allMyPostsAdapter;
    private List<Recipe> recipeList;

    ImageView back;
    LinearLayout noPost;

    String profileid ;

    public AllMyPostFragment(){

    }
    public AllMyPostFragment(String profileid){
        this.profileid = profileid;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_all_my_post,container,false);

        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = preferences.getString("profileid","none");



        recyclerView = view.findViewById(R.id.all_foodpost_fragment_RecyclerView);
        noPost = view.findViewById(R.id.ll_noPost);

        back = view.findViewById(R.id.btn_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment(profileid)).commit();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        recipeList = new ArrayList<>();


        allMyPostsAdapter = new AllMyPostsAdapter(getContext(),recipeList,profileid);
        recyclerView.setAdapter(allMyPostsAdapter);

        readRecipes();

        return view;
    }

    private void readRecipes(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if(recipe.getUserid().equals(profileid)) {
                        recipeList.add(recipe);
                    }

                }
                Collections.reverse(recipeList);
                allMyPostsAdapter.notifyDataSetChanged();

                if(recipeList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    noPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }






}