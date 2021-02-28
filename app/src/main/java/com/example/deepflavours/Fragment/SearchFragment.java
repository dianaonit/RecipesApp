package com.example.deepflavours.Fragment;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.deepflavours.Adapter.MostLikedRecipeAdapter;
import com.example.deepflavours.Adapter.RecipeAdapter;
import com.example.deepflavours.Adapter.UserAdapter;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.example.deepflavours.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    EditText search_bar;

    private RecyclerView recyclerView_mostLiked_Recipe;
    private MostLikedRecipeAdapter mostLikedRecipeAdapter;
    private List<Recipe> mostLikedRecipeList;

    TextView mostLiked_Title,popular_Title;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        recyclerView = view.findViewById(R.id.recycler_view_searchbar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search_bar = view.findViewById(R.id.search_bar);
        mostLiked_Title = view.findViewById(R.id.mostlikedrecipes_category);
        popular_Title = view.findViewById(R.id.popularRecipes_category);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(),mUsers);
        recyclerView.setAdapter(userAdapter);

        recyclerView_mostLiked_Recipe = view.findViewById(R.id.recycler_view_mostlikedrecipes);
        recyclerView_mostLiked_Recipe.setHasFixedSize(true);
        recyclerView_mostLiked_Recipe.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        mostLikedRecipeList = new ArrayList<>();

        mostLikedRecipeAdapter = new MostLikedRecipeAdapter(getContext(),mostLikedRecipeList);
        recyclerView_mostLiked_Recipe.setAdapter(mostLikedRecipeAdapter);


        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
                if(s.length()<1){
                    recyclerView.setVisibility(View.GONE);
                    mostLiked_Title.setVisibility(View.VISIBLE);
                    recyclerView_mostLiked_Recipe.setVisibility(View.VISIBLE);
                    popular_Title.setVisibility(View.VISIBLE);


                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    mostLiked_Title.setVisibility(View.GONE);
                    recyclerView_mostLiked_Recipe.setVisibility(View.GONE);
                    popular_Title.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        readUsers();

        readRecipes();

        return view;
    }

    private void searchUsers(String s){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);
                    String username = user.getUsername().toLowerCase();
                    if(username.contains(s.toLowerCase())) {
                        mUsers.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(search_bar.getText().toString().equals("")){
                        mUsers.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            User user = snapshot.getValue(User.class);
                            mUsers.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readRecipes(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mostLikedRecipeList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Recipe recipe = snapshot.getValue(Recipe.class);
                        mostLikedRecipeList.add(recipe);

                }
                mostLikedRecipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}