package com.example.deepflavours.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepflavours.CommentsActivity;
import com.example.deepflavours.EditPostActivity;
import com.example.deepflavours.Fragment.LikesFragment;
import com.example.deepflavours.Fragment.RecipeDetailFragment;
import com.example.deepflavours.Login;
import com.example.deepflavours.Model.Rating;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.example.deepflavours.R;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PostDetailAdapter extends RecyclerView.Adapter<PostDetailAdapter.ViewHolder> {

    public Context mContext;
    public List<Recipe> mPost;
    private String profileid;
    private FirebaseUser firebaseUser;
    public RatingBar ratingBar_user;


    public PostDetailAdapter(Context mContext, List<Recipe> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;

    }

    public PostDetailAdapter(Context mContext, List<Recipe> mPost, String profileid) {
        this.mContext = mContext;
        this.mPost = mPost;
        this.profileid = profileid;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.postdetail_item_fragment, viewGroup, false);

        return new PostDetailAdapter.ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe post = mPost.get(i);
        if (post != null) {
            viewHolder.post_title.setText(post.getTitle());
            viewHolder.post_desc.setText(post.getDescription());
            viewHolder.servings.setText(post.getServings());
            viewHolder.prepTime.setText(post.getPreparationtime());
            viewHolder.cookTime.setText(post.getCooktime());
            viewHolder.ingredients.setText(post.getIngredients());
            viewHolder.directions.setText(post.getDirections());

            Glide.with(mContext).load(post.getRecipeimage()).into(viewHolder.image_post);


            viewHolder.btn_ingredients.setTextColor(Color.parseColor("#000000"));
            viewHolder.btn_directions.setTextColor(Color.parseColor("#aaa480"));
            viewHolder.ingredients.setVisibility(View.VISIBLE);
            viewHolder.directions.setVisibility(View.GONE);


            userInfo(viewHolder.image_profile, viewHolder.user_name, post.getUserid());
            nrLikes(viewHolder.likes, post.getRecipeid());
            nrCooked(viewHolder.cooktimesNr, post.getRecipeid());
            nrComments(viewHolder.commnr, post.getRecipeid());
            nrRatings(viewHolder.ratingnr,post.getRecipeid());
            isFavourite(post.getRecipeid(), viewHolder.favourite);
            isCooked(post.getRecipeid(), viewHolder.cook);
        }


        calculateRating(post.getRecipeid(),viewHolder.rating);


        viewHolder.btn_ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.btn_ingredients.setTextColor(Color.parseColor("#000000"));
                viewHolder.btn_directions.setTextColor(Color.parseColor("#aaa480"));
                viewHolder.line_ingred.setVisibility(View.VISIBLE);
                viewHolder.ingredients.setVisibility(View.VISIBLE);
                viewHolder.directions.setVisibility(View.GONE);
                viewHolder.line_dir.setVisibility(View.GONE);
            }
        });

        viewHolder.btn_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.btn_ingredients.setTextColor(Color.parseColor("#aaa480"));
                viewHolder.btn_directions.setTextColor(Color.parseColor("#000000"));
                viewHolder.ingredients.setVisibility(View.GONE);
                viewHolder.line_ingred.setVisibility(View.GONE);
                viewHolder.directions.setVisibility(View.VISIBLE);
                viewHolder.line_dir.setVisibility(View.VISIBLE);
            }
        });


        viewHolder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.favourite.getTag().equals("+add to fav")) {
                    FirebaseDatabase.getInstance().getReference().child("Favourites").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Favourites").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });


        viewHolder.cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.cook.getTag().equals("Start Cooking")) {
                    FirebaseDatabase.getInstance().getReference().child("CookedRecipes").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).removeValue();

                    addNotifications(post.getUserid(), post.getRecipeid());

                } else {
                    FirebaseDatabase.getInstance().getReference().child("CookedRecipes").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).removeValue();


                }
            }
        });


        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid", post.getRecipeid());
                intent.putExtra("userid", post.getUserid());
                mContext.startActivity(intent);
            }
        });

        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getRecipeid());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LikesFragment(profileid)).commit();

            }
        });

        viewHolder.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent = new Intent(mContext, NotesActivity.class);
//                    mContext.startActivity(intent);
            }
        });

        viewHolder.add_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_rating, null);

                ratingBar_user = itemView.findViewById(R.id.user_ratingbar);
                Rating rating1 = new Rating();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ratings").child(post.getRecipeid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Rating rating = dataSnapshot.getValue(Rating.class);
                            if(rating.getUserId().equals(firebaseUser.getUid())){
                                ratingBar_user.setRating(rating.getUserRatingValue());
                                rating1.setUserId(rating.getUserId());
                                rating1.setRatingId(rating.getRatingId());
                                rating1.setUserRatingValue(rating.getUserRatingValue());
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                alertDialog.setView(itemView);

                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        });

                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                if(rating1.getRatingId()!=null && !rating1.getRatingId().isEmpty()) {
                                    updateRating(post.getRecipeid(), rating1);
                                    Toast.makeText(mContext,"Updated rating!", Toast.LENGTH_LONG).show();

                                }else {
                                    addRating(post.getRecipeid());
                                    Toast.makeText(mContext,"Thanks for rating!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                alertDialog.show();
            }

        });


    }


    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView post_title, cooktimesNr, servings, prepTime, cookTime, user_name, likes, commnr, comments, post_desc, ingredients, directions,ratingnr;
        public ImageView image_post, image_profile, back, favourite, cook, more, like, notes, add_rating;
        public Button btn_ingredients, btn_directions;
        public View line_ingred, line_dir;
        public RatingBar rating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_post = itemView.findViewById(R.id.recipe_image);
            image_profile = itemView.findViewById(R.id.image_user);
            post_title = itemView.findViewById(R.id.post_title);
            servings = itemView.findViewById(R.id.tv_nrOfservings);
            prepTime = itemView.findViewById(R.id.tv_timeOfprep);
            cookTime = itemView.findViewById(R.id.tv_timeOfcook);
            user_name = itemView.findViewById(R.id.name_user);
            likes = itemView.findViewById(R.id.tv_likesnr);
            like = itemView.findViewById(R.id.iv_like);
            post_desc = itemView.findViewById(R.id.post_description);
            ingredients = itemView.findViewById(R.id.tv_ingredients);
            directions = itemView.findViewById(R.id.tv_directions);
            btn_ingredients = itemView.findViewById(R.id.btn_ingredients);
            btn_directions = itemView.findViewById(R.id.btn_directions);
            back = itemView.findViewById(R.id.btn_back_detailpost);
            favourite = itemView.findViewById(R.id.add_fav);
            cook = itemView.findViewById(R.id.add_cooking);
            more = itemView.findViewById(R.id.more_actions);
            cooktimesNr = itemView.findViewById(R.id.cooktimes_nr);
            comments = itemView.findViewById(R.id.tv_allcomm);
            commnr = itemView.findViewById(R.id.tv_comm);
            line_ingred = itemView.findViewById(R.id.line_ingredients);
            line_dir = itemView.findViewById(R.id.line_directions);
            notes = itemView.findViewById(R.id.addnotes);
            add_rating = itemView.findViewById(R.id.add_rating);
            rating = itemView.findViewById(R.id.ratingbar_recipe_postdetail);
            ratingnr = itemView.findViewById(R.id.nr_ratingbar);

        }
    }

    private void calculateRating(String postid, RatingBar ratingBar) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ratings").child(postid);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sum = 0;
                int ratingValue = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rating rating = snapshot.getValue(Rating.class);
                    sum = sum + rating.getUserRatingValue();
                }
                if((int) dataSnapshot.getChildrenCount()>0) {
                    ratingValue = sum / (int) dataSnapshot.getChildrenCount();
                }
                ratingBar.setRating(ratingValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void userInfo(final ImageView image_profile, final TextView username, final String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void nrLikes(TextView likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addNotifications(String userid, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "cooked your recipe");
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);

    }


    private void nrCooked(TextView cookednr, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("CookedRecipes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cookednr.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void isFavourite(String postid, ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Favourites")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.fav_bej_full);
                    imageView.setTag("favourite");
                } else {
                    imageView.setImageResource(R.drawable.fav_bej);
                    imageView.setTag("+add to fav");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isCooked(String postid, ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("CookedRecipes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.cooking_full_bej);
                    imageView.setTag("Cooked Recipe");
                } else {
                    imageView.setImageResource(R.drawable.cooking_bej);
                    imageView.setTag("Start Cooking");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void nrComments(TextView commentsnr, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentsnr.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void addRating(String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ratings").child(postid);
        String ratingId = reference.push().getKey();

        HashMap<String, Object> ratingHashMap = new HashMap<>();
        ratingHashMap.put("userRatingValue", (int) ratingBar_user.getRating());
        ratingHashMap.put("ratingId", ratingId);
        ratingHashMap.put("userId", firebaseUser.getUid());

        reference.child(ratingId).setValue(ratingHashMap);


    }

    public  void updateRating(String postid, Rating rating){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ratings").child(postid).child(rating.getRatingId());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ratingId",rating.getRatingId());
        hashMap.put("userId",rating.getUserId());
        hashMap.put("userRatingValue",(int)ratingBar_user.getRating());

        reference.updateChildren(hashMap);
    }

    private void nrRatings(TextView ratingNr, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Ratings").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ratingNr.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
