package com.example.deepflavours.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepflavours.Adapter.FavoriteRecipesAdapter;
import com.example.deepflavours.Adapter.RecipeAdapter;
import com.example.deepflavours.EditProfileActivity;
import com.example.deepflavours.Login;
import com.example.deepflavours.MainActivity;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.example.deepflavours.NotesActivity;
import com.example.deepflavours.R;
import com.example.deepflavours.StartCooking;
import com.example.deepflavours.UserNotesActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;

    private RecyclerView favRecipes_recyclerView;
    private FavoriteRecipesAdapter favoriteRecipesAdapter;
    private List<Recipe> favouriteRecipes;


    CircleImageView image_profile;
    ImageView options, back;
    TextView username, bio, posts, following, followers, show_all, logout;
    Button edit_profile;
    LinearLayout noFoodPost, noFavPost;


    FirebaseUser firebaseUser;
    String profileid;
    String sourceFragment;
    String previousUser;
    String languageCode;

    public ProfileFragment() {

    }

    public ProfileFragment(String profileid) {
        this.profileid = profileid;
    }

    public ProfileFragment(String profileid, String sourceFragment) {
        this.profileid = profileid;
        this.sourceFragment = sourceFragment;
    }

    public ProfileFragment(String profileid, String sourceFragment, String previousUser) {
        this.profileid = profileid;
        this.sourceFragment = sourceFragment;
        this.previousUser = previousUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        languageCode = sharedPreferences.getString("Selected_language","");
        if(!languageCode.isEmpty()){
            setLocale(languageCode);
        }


        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String profileSourceFragment = preferences.getString("ProfileSourceFragment", "none");
        if (profileSourceFragment.equals("none")) {
            SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
            editor.putString("ProfileSourceFragment", sourceFragment);
            editor.apply();
        }
        if (sourceFragment == null) {
            sourceFragment = profileSourceFragment;
        }

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
        back = view.findViewById(R.id.btn_back_profile);
        noFoodPost = view.findViewById(R.id.ll_noFoodPost);
        noFavPost = view.findViewById(R.id.ll_noFavoritePost);


        if (profileid == null) {
            profileid = previousUser;
        }

        if (profileid!=null) {

            if (profileid.equals(firebaseUser.getUid())) {
                options.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                edit_profile.setText(getResources().getString(R.string.edit_profile));
            } else {
                options.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                checkFollow();
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sourceFragment) {
                    case "LikesFragment":
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new LikesFragment(previousUser)).commit();
                        break;
                    case "FollowersFragment":
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FollowersFragment()).commit();
                        break;
                    case "FollowingsFragment":
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FollowingsFragment()).commit();
                        break;
                    case "SearchFragment":
                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SearchFragment()).commit();
                        break;
                }
            }
        });


        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(), R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(
                                R.layout.bottom_sheet_options,
                                (LinearLayout) view.findViewById(R.id.bottomSheetContainerOptionsProfile)
                        );

                bottomSheetView.findViewById(R.id.language_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChangeLanguageDialog();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.notes_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), UserNotesActivity.class);
                        getContext().startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.logout_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Login.class);
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
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", profileid);
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FollowersFragment()).commit();
            }
        });


        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", profileid);
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FollowingsFragment()).commit();
            }
        });


        show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", profileid);
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AllMyPostFragment()).commit();
            }
        });


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile.getText().toString();

                if (btn.equals("Edit Profile") || btn.equals("Profil bearbeiten") || btn.equals("Editer le profil") || btn.equals("Editare Profil")) {
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                } else if (btn.equals("follow") || btn.equals("folgen") || btn.equals("suivre") || btn.equals("urmărește")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                    addNotifications();

                    edit_profile.setText(getResources().getString(R.string.btn_following));

                } else if (btn.equals("following") || btn.equals("folgenden") || btn.equals("suivante") || btn.equals("urmărești") ) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                    edit_profile.setText(getResources().getString(R.string.btn_follow));

                }
            }
        });


        //recycler view my post
        recyclerView = view.findViewById(R.id.foodpost_profile_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //recycler view favorite post(liked post)
        favRecipes_recyclerView = view.findViewById(R.id.favoritepost_profile_RecyclerView);
        favRecipes_recyclerView.setHasFixedSize(true);
        favRecipes_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeList = new ArrayList<>();

        favouriteRecipes = new ArrayList<>();


        recipeAdapter = new RecipeAdapter(getContext(), recipeList, profileid);
        recyclerView.setAdapter(recipeAdapter);

        favoriteRecipesAdapter = new FavoriteRecipesAdapter(getContext(), favouriteRecipes, profileid, "ProfileFragment");
        favRecipes_recyclerView.setAdapter(favoriteRecipesAdapter);


        userInfo();
        getFollowers();
        getNrPosts();

        readRecipes();
        readFavRecipes();


        return view;
    }


    private void showChangeLanguageDialog() {
        Resources resources = getResources();
        final String[] listItems = {resources.getString(R.string.languageTypeRo),resources.getString(R.string.languageTypeEn),resources.getString(R.string.languageTypeDe),resources.getString(R.string.languageTypeFr)};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);


        mBuilder.setTitle(R.string.Language);
        int checkedItem;
        if(languageCode.equalsIgnoreCase("ro")){
            checkedItem = 0;
        } else if( languageCode.equalsIgnoreCase("en")) {
            checkedItem = 1;
        } else if(languageCode.equalsIgnoreCase("de")){
            checkedItem = 2;
        } else if(languageCode.equalsIgnoreCase("fr")){
            checkedItem = 3;
        } else{
            checkedItem = -1;
        }
        mBuilder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("ro");
                } else if (i == 1) {
                    setLocale("en");
                } else if (i == 2) {
                    setLocale("de");
                } else if (i == 3) {
                    setLocale("fr");
                }
                dialogInterface.dismiss();

                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        getActivity().getResources().updateConfiguration(configuration, getActivity().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getContext().getSharedPreferences("Language", getContext().MODE_PRIVATE).edit();
        editor.putString("Selected_language", localeCode);
        editor.apply();
    }






    private void readRecipes() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if (recipe.getUserid().equals(profileid)) {
                        recipeList.add(recipe);
                    }

                }
                Collections.reverse(recipeList);
                recipeAdapter.notifyDataSetChanged();

                if (recipeList.isEmpty()) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noFoodPost.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readFavRecipes() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favourites");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouriteRecipes.clear();
                boolean hasFavRecipes = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        String userId = dataSnapshot1.getKey();
                        if (userId.equals(profileid)) {
                            String recipeId = snapshot.getKey();
                            hasFavRecipes = true;
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
                if (!hasFavRecipes) {
                    favRecipes_recyclerView.setVisibility(View.GONE);
                    noFavPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addNotifications(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(profileid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text" , "started following you");
        hashMap.put("postid", "" );
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);

    }


    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if(getContext()!=null){
                    Glide.with(getContext()).load(user.getImageurl()).into(image_profile);
                }

                username.setText(user.getUsername());
                bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void checkFollow() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()  ) {
                    edit_profile.setText(getResources().getString(R.string.btn_following));
                } else {
                    edit_profile.setText(getResources().getString(R.string.btn_follow));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getFollowers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("followers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText("" + dataSnapshot.getChildrenCount());
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
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getNrPosts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nrPosts = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if (recipe.getUserid().equals(profileid)) {
                        nrPosts++;
                    }
                }

                posts.setText("" + nrPosts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}