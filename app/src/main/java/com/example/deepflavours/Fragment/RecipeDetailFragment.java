package com.example.deepflavours.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.deepflavours.Adapter.PostAdapter;
import com.example.deepflavours.Adapter.PostDetailAdapter;
import com.example.deepflavours.EditPostActivity;
import com.example.deepflavours.EditProfileActivity;
import com.example.deepflavours.Login;
import com.example.deepflavours.MainActivity;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseUser firebaseUser;

    String postid;
    String profileid;

    ImageView back,options;

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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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

        options = view.findViewById(R.id.more_actions);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Recipe post = dataSnapshot.getValue(Recipe.class);
                if(post!=null) {
                    String recipeUserId = post.getUserid();
                    if (firebaseUser.getUid().equals(recipeUserId)) {
                        options.setVisibility(View.VISIBLE);
                    } else {
                        options.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(
                                R.layout.bottom_sheet_options_post,
                                (LinearLayout)view.findViewById(R.id.bottomSheetContainerOptions)
                        );
                bottomSheetView.findViewById(R.id.delete_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePost(postid);
                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.edit_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), EditPostActivity.class));
                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });


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

    private void deletePost(String postId){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
        alertDialog.setTitle(getResources().getString(R.string.delete_question));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.delete_post_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.delete_post_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        FirebaseDatabase.getInstance().getReference("Recipes").child(postId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful() ){
                                    FirebaseDatabase.getInstance().getReference("CookedRecipes").child(postId).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Saves").child(postId).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Favourites").child(postId).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Likes").child(postId).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Ratings").child(postId).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Notifications").child(postId).removeValue();
                                    Toast.makeText(getContext(),"Deleted!",Toast.LENGTH_SHORT).show();


                                }
                            }
                        });
                        dialogInterface.dismiss();
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getContext().startActivity(intent);
                    }
                });

        alertDialog.show();
    }


}