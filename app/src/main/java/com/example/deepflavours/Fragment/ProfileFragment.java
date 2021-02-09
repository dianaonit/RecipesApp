package com.example.deepflavours.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepflavours.Adapter.FavoriteRecipesAdapter;
import com.example.deepflavours.Adapter.RecipeAdapter;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.example.deepflavours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;

    private RecyclerView favRecipes_recyclerView;
    private FavoriteRecipesAdapter favoriteRecipesAdapter;
    private List<String> likeList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        //recycler view my post
        recyclerView = view.findViewById(R.id.foodpost_profile_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //recycler view favorite post(liked post)
        favRecipes_recyclerView = view.findViewById(R.id.favoritepost_profile_RecyclerView);
        favRecipes_recyclerView.setHasFixedSize(true);
        favRecipes_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeList = new ArrayList<>();


        recipeAdapter = new RecipeAdapter(getContext(),recipeList);
        favoriteRecipesAdapter=new FavoriteRecipesAdapter(getContext(),recipeList);

        recyclerView.setAdapter(recipeAdapter);
        favRecipes_recyclerView.setAdapter(favoriteRecipesAdapter);

       checkLikes();

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
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(recipe.getUserid())) {
                        recipeList.add(recipe);
                    }

                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private  void checkLikes(){
        likeList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Likes")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               likeList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   likeList.add(snapshot.getKey());
                }

                readFavRecipes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void readFavRecipes(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Recipe recipe = snapshot.getValue(Recipe.class);
                  // for ()
//                    String currentRecipeId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    if(likeList.contains(currentRecipeId)){
//                        recipeList.add(recipe);
//                    }
                }
                favoriteRecipesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}