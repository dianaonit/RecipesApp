package com.example.deepflavours;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.deepflavours.Model.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceRecipes;
    private List<Recipe> recipes = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Recipe> recipes, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceRecipes = mDatabase.getReference("Recipes");

    }

    public void readRecipes(final DataStatus dataStatus){
        mReferenceRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipes.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                        keys.add(keyNode.getKey());
                        Recipe recipe = keyNode.getValue(Recipe.class);
                        recipes.add(recipe);
                }
                dataStatus.DataIsLoaded(recipes,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
