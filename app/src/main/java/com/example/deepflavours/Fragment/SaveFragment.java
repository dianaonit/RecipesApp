package com.example.deepflavours.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.deepflavours.Adapter.FavoriteRecipesAdapter;
import com.example.deepflavours.Adapter.JustAddedCookBookAdapter;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.R;
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


public class SaveFragment extends Fragment {


    private RecyclerView saveRecipesJustAdded_recyclerView;
    private JustAddedCookBookAdapter justAddedCookBookAdapter;
    private List<Recipe> savedRecipes;


    private RecyclerView cookedRecipes_recyclerView;
    private FavoriteRecipesAdapter favoriteRecipesAdapter;
    private List<Recipe> cookedRecipes;

    String profileid;

    List<String> cookedRecipesIds = new ArrayList<>();


    public SaveFragment() {

    }

    public SaveFragment(String profileid){
        this.profileid = profileid;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save,container,false);


        //recycler view saved post(justadded)
        saveRecipesJustAdded_recyclerView = view.findViewById(R.id.savefragment_justadded_RecyclerView);
        saveRecipesJustAdded_recyclerView.setHasFixedSize(true);
        saveRecipesJustAdded_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


        cookedRecipes_recyclerView = view.findViewById(R.id.savefragment_cookedrecipes);
        cookedRecipes_recyclerView.setHasFixedSize(true);
        cookedRecipes_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));


        savedRecipes = new ArrayList<>();
        cookedRecipes = new ArrayList<>();

        justAddedCookBookAdapter=new JustAddedCookBookAdapter(getContext(),savedRecipes);
        favoriteRecipesAdapter=new FavoriteRecipesAdapter(getContext(),cookedRecipes,profileid,"SaveFragment");

        saveRecipesJustAdded_recyclerView.setAdapter(justAddedCookBookAdapter);
        cookedRecipes_recyclerView.setAdapter(favoriteRecipesAdapter);



        readCookedRecipes();
        readSaveRecipes();



        return view;
    }


    private void readCookedRecipes(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CookedRecipes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cookedRecipes.clear();
                for(DataSnapshot cookedRecipesSnapshot :dataSnapshot.getChildren()){
                    for(DataSnapshot userSnapshot: cookedRecipesSnapshot.getChildren()){
                        String userId = userSnapshot.getKey();
                        if(userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            String recipeIdCooked = cookedRecipesSnapshot.getKey();
                            FirebaseDatabase.getInstance().getReference("Recipes").child(recipeIdCooked).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Recipe recipe = snapshot.getValue(Recipe.class);
                                        cookedRecipes.add(recipe);
                                        cookedRecipesIds.add(recipeIdCooked);
                                        savedRecipes.remove(recipe);

                                        favoriteRecipesAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    Collections.reverse(cookedRecipes);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void readSaveRecipes(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savedRecipes.clear();
                for(DataSnapshot saveSnapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot userSnapshot : saveSnapshot.getChildren()){
                        String userId = userSnapshot.getKey();
                        if(userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                          String  recipeIdSaved = saveSnapshot.getKey();
                            FirebaseDatabase.getInstance().getReference("Recipes").child(recipeIdSaved).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Recipe recipe = snapshot.getValue(Recipe.class);
                                    if(!cookedRecipesIds.contains(recipe.getRecipeid())){
                                        savedRecipes.add(recipe);
                                    }else{
                                        savedRecipes.remove(recipe);
                                    }


                                    justAddedCookBookAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    Collections.reverse(savedRecipes);
                }
//                if(savedRecipes.isEmpty()){
//                    Recipe recipe = new Recipe();
//                    recipe.setTitle("generic title");
//                    savedRecipes.add(recipe);
//                    justAddedCookBookAdapter.notifyDataSetChanged();
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}