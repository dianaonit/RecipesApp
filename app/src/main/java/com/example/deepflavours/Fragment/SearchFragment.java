package com.example.deepflavours.Fragment;

import android.content.Intent;
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
import android.widget.TextView;

import com.example.deepflavours.Adapter.MostLikedRecipeAdapter;
import com.example.deepflavours.Adapter.PopularRecipesAdapter;
import com.example.deepflavours.Adapter.SearchRecipesAdapter;
import com.example.deepflavours.Adapter.UserAdapter;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.example.deepflavours.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;


    private RecyclerView recyclerView_searchRecipes;
    private SearchRecipesAdapter searchRecipesAdapter;
    private List<Recipe> mRecipes;

    EditText search_bar;

    private RecyclerView recyclerView_mostLiked_Recipe;
    private MostLikedRecipeAdapter mostLikedRecipeAdapter;
    private List<Recipe> mostLikedRecipeList;

    TextView mostLiked_Title,popular_Title;

    private RecyclerView recyclerView_PopularRecipes;
    private PopularRecipesAdapter popularRecipesAdapter;
    private List<Recipe> popularRecipesList;

    List<String> mostLikedRecipeIds= new ArrayList<>();
    List<String>popularRecipeIds= new ArrayList<>();





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search,container,false);

            search_bar = view.findViewById(R.id.search_bar);



        mostLiked_Title = view.findViewById(R.id.mostlikedrecipes_category);
        popular_Title = view.findViewById(R.id.popularRecipes_category);


        //recycler view show user
        recyclerView = view.findViewById(R.id.recycler_view_searchbar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        userAdapter = new UserAdapter(getContext(),mUsers);
        recyclerView.setAdapter(userAdapter);


        //recycler view show recipes
        recyclerView_searchRecipes = view.findViewById(R.id.recycler_view_searchbar_recipes);
        recyclerView_searchRecipes.setHasFixedSize(true);
        recyclerView_searchRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecipes = new ArrayList<>();

        searchRecipesAdapter= new SearchRecipesAdapter(getContext(),mRecipes);
        recyclerView_searchRecipes.setAdapter(searchRecipesAdapter);


        //recycler view show most liked recipes
        recyclerView_mostLiked_Recipe = view.findViewById(R.id.recycler_view_mostlikedrecipes);
        recyclerView_mostLiked_Recipe.setHasFixedSize(true);
        recyclerView_mostLiked_Recipe.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        mostLikedRecipeList = new ArrayList<>();

        mostLikedRecipeAdapter = new MostLikedRecipeAdapter(getContext(),mostLikedRecipeList);
        recyclerView_mostLiked_Recipe.setAdapter(mostLikedRecipeAdapter);

        //recycler view show popular recipes
        recyclerView_PopularRecipes = view.findViewById(R.id.recycler_view_popularRecipes);
        recyclerView_PopularRecipes.setHasFixedSize(true);
        recyclerView_PopularRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        popularRecipesList = new ArrayList<>();

        popularRecipesAdapter = new PopularRecipesAdapter(getContext(),popularRecipesList);
        recyclerView_PopularRecipes.setAdapter(popularRecipesAdapter);


        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
                searchRecipes(s.toString());

                if(s.length()<1){
                    recyclerView.setVisibility(View.GONE);
                    recyclerView_searchRecipes.setVisibility(View.GONE);
                    mostLiked_Title.setVisibility(View.VISIBLE);
                    recyclerView_mostLiked_Recipe.setVisibility(View.VISIBLE);
                    popular_Title.setVisibility(View.VISIBLE);
                    recyclerView_PopularRecipes.setVisibility(View.VISIBLE);

                }else{

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView_searchRecipes.setVisibility(View.VISIBLE);
                    mostLiked_Title.setVisibility(View.GONE);
                    recyclerView_mostLiked_Recipe.setVisibility(View.GONE);
                    popular_Title.setVisibility(View.GONE);
                    recyclerView_PopularRecipes.setVisibility(View.GONE);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {




            }
        });



        readUsers();
        readRecipes();

        readMostLikedRecipes();
        readPopularRecipes();

        return view;
    }

    private void searchUsers(String s){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
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
                        for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                            User user = userSnapshot.getValue(User.class);
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



    private void searchRecipes(String s){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRecipes.clear();
                for(DataSnapshot recipeSnapshot : dataSnapshot.getChildren()){
                    Recipe recipe =recipeSnapshot.getValue(Recipe.class);
                    String recipeTitle = recipe.getTitle().toLowerCase();
                    if(recipeTitle.contains(s.toLowerCase())) {
                        mRecipes.add(recipe);
                    }
                }
                searchRecipesAdapter.notifyDataSetChanged();

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
                if(search_bar.getText().toString().equals("")){
                    mRecipes.clear();
                    for(DataSnapshot recipeSnapshot: dataSnapshot.getChildren()){
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        mRecipes.add(recipe);
                    }
                   searchRecipesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void readMostLikedRecipes(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Likes");                                 //se merge in colectia Likes pentru a colecta toate id-urile retetei
        reference.addValueEventListener(new ValueEventListener() {                                                                //se adauga in lista id-urilor , doar acele id-uri care au numarul de
            @Override                                                                                                             //like-uri mai mare de o anumita valoare data
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mostLikedRecipeList.clear();
                for(DataSnapshot likeSnapshot : dataSnapshot.getChildren()){
                    String recipeId = likeSnapshot.getKey();
                    DatabaseReference referenceRecipe = FirebaseDatabase.getInstance().getReference("Likes").child(recipeId);
                    referenceRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(likeSnapshot.getChildrenCount()>1){
                                    mostLikedRecipeIds.add(recipeId);
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");      //se merge in colectia Recipes, verificam daca lista id-urilor creata mai sus contine id-urile
                reference.addListenerForSingleValueEvent(new ValueEventListener() {                              //retetelor existente in colectia Recipes,iar daca acestea exista ,luam reteta(cu id-ul ei)
                    @Override                                                                                    //si o adaugam in lista retelor cu cele mai multe like-uri
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                               //aceasta lista o afisam cu ajutorul adapter-ului
                        for(DataSnapshot recipeSnapshot : dataSnapshot.getChildren()){
                            Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                            if(mostLikedRecipeIds.contains(recipe.getRecipeid())){
                                mostLikedRecipeList.add(recipe);
                            }
                        }
                        mostLikedRecipeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readPopularRecipes(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CookedRecipes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                popularRecipesList.clear();
                for(DataSnapshot cookedSnapshot : dataSnapshot.getChildren()){
                    String recipeId = cookedSnapshot.getKey();
                    DatabaseReference referenceRecipe = FirebaseDatabase.getInstance().getReference("CookedRecipes").child(recipeId);
                    referenceRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(cookedSnapshot.getChildrenCount()>1){
                               popularRecipeIds.add(recipeId);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot recipeSnapshot : dataSnapshot.getChildren()){
                            Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                            if(popularRecipeIds.contains(recipe.getRecipeid())){
                                popularRecipesList.add(recipe);
                            }
                        }
                        popularRecipesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}