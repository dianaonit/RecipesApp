package com.example.deepflavours.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepflavours.Adapter.FavoriteRecipesAdapter;
import com.example.deepflavours.Adapter.RecipeAdapter;
import com.example.deepflavours.Adapter.UserAdapter;
import com.example.deepflavours.EditProfileActivity;
import com.example.deepflavours.FollowersActivity;
import com.example.deepflavours.FollowingsActivity;
import com.example.deepflavours.Login;
import com.example.deepflavours.MainActivity;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.example.deepflavours.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;

    private RecyclerView favRecipes_recyclerView;
    private FavoriteRecipesAdapter favoriteRecipesAdapter;
    private List<Recipe> favouriteRecipes;




    CircleImageView image_profile;
    ImageView options;
    TextView username,bio,posts,following,followers,show_all,logout;
    Button edit_profile;

    FirebaseUser firebaseUser;
    String profileid;





    public ProfileFragment() {

    }

     public ProfileFragment(String profileid){
         this.profileid = profileid;
     }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        
        image_profile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.iv_menu);
        username = view.findViewById(R.id.username);
        bio = view.findViewById(R.id.tv_bio);
        posts = view.findViewById(R.id.posts_nr);
        following = view.findViewById(R.id.followings_nr);
        followers = view.findViewById(R.id.followers_nr);
        show_all = view.findViewById(R.id.seeAllmypost);
        edit_profile = view.findViewById(R.id.editprofile_button);
        logout = view.findViewById(R.id.logout_option);


        if(profileid.equals(firebaseUser.getUid())){
            options.setVisibility(View.VISIBLE);
            edit_profile.setText("Edit Profile");
        }else {
            options.setVisibility(View.INVISIBLE);
            checkFollow();
        }



        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(
                                R.layout.bottom_sheet_options,
                                (LinearLayout)view.findViewById(R.id.bottomSheetContainerOptions)
                        );
                bottomSheetView.findViewById(R.id.logout_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getContext().startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id",profileid);
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowingsActivity.class);
                intent.putExtra("id",profileid);
                startActivity(intent);
            }
        });



     show_all.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
             editor.putString("profileid",profileid);
             editor.apply();

             ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                     new AllMyPostFragment()).commit();
         }
     });




        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile.getText().toString();

                if(btn.equals("Edit Profile")){
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                }else if(btn.equals("follow")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                }else if(btn.equals("following")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });



        //recycler view my post
        recyclerView = view.findViewById(R.id.foodpost_profile_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //recycler view favorite post(liked post)
        favRecipes_recyclerView = view.findViewById(R.id.favoritepost_profile_RecyclerView);
        favRecipes_recyclerView.setHasFixedSize(true);
        favRecipes_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeList = new ArrayList<>();

        favouriteRecipes = new ArrayList<>();


        recipeAdapter = new RecipeAdapter(getContext(),recipeList);
        recyclerView.setAdapter(recipeAdapter);

        favoriteRecipesAdapter=new FavoriteRecipesAdapter(getContext(),favouriteRecipes);
        favRecipes_recyclerView.setAdapter(favoriteRecipesAdapter);




        userInfo();
        getFollowers();
        getNrPosts();

        readRecipes();
        readFavRecipes();


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
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readFavRecipes(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favourites");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouriteRecipes.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                   for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                       String userId = dataSnapshot1.getKey();
                       if(userId.equals(profileid)){
                           String recipeId = snapshot.getKey();
                           FirebaseDatabase.getInstance().getReference("Recipes").child(recipeId).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   Recipe recipe = snapshot.getValue(Recipe.class);
                                   favouriteRecipes.add(recipe);
                                   favoriteRecipesAdapter.notifyDataSetChanged();
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


    private void userInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());
                bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void checkFollow(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(profileid).exists()){
                    edit_profile.setText("following");
                }else{
                    edit_profile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getFollowers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("followers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText(""+ dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("following");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText(""+ dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    private void getNrPosts(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nrPosts = 0;

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if(recipe.getUserid().equals(profileid)){
                        nrPosts++;
                    }
                }

                posts.setText(""+nrPosts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}