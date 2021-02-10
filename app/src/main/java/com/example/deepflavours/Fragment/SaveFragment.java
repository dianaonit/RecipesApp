package com.example.deepflavours.Fragment;

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
import java.util.List;


public class SaveFragment extends Fragment {


    private RecyclerView saveRecipesJustAdded_recyclerView;
    private JustAddedCookBookAdapter justAddedCookBookAdapter;
    private List<Recipe> savedRecipes;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save,container,false);


        //recycler view saved post(justadded)
        saveRecipesJustAdded_recyclerView = view.findViewById(R.id.savefragment_justadded_RecyclerView);
        saveRecipesJustAdded_recyclerView.setHasFixedSize(true);
        saveRecipesJustAdded_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        savedRecipes = new ArrayList<>();

        justAddedCookBookAdapter=new JustAddedCookBookAdapter(getContext(),savedRecipes);
        saveRecipesJustAdded_recyclerView.setAdapter(justAddedCookBookAdapter);


        readSaveRecipes();


        return view;
    }


//    private void readSaveRecipes(){
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                savedRecipes.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Recipe recipe = snapshot.getValue(Recipe.class);
//                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(recipe.getUserid())) {
//                        savedRecipes.add(recipe);
//                    }
//
//                }
//                justAddedCookBookAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void readSaveRecipes(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                        String userId = dataSnapshot1.getKey();
                        if(userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            String recipeId = snapshot.getKey();
                            FirebaseDatabase.getInstance().getReference("Recipes").child(recipeId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Recipe recipe = snapshot.getValue(Recipe.class);
                                    savedRecipes.add(recipe);

                                    justAddedCookBookAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}