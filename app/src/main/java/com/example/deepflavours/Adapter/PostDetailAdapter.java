package com.example.deepflavours.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepflavours.Fragment.RecipeDetailFragment;
import com.example.deepflavours.MainActivity;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.User;
import com.example.deepflavours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class PostDetailAdapter extends RecyclerView.Adapter<PostDetailAdapter.ViewHolder> {

    public Context mContext;
    public List<Recipe> mPost;

    private FirebaseUser firebaseUser;

    public PostDetailAdapter(Context mContext, List<Recipe> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_postdetail_fragment,viewGroup,false);
        return new PostDetailAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe post = mPost.get(i);


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



        userInfo(viewHolder.image_profile,viewHolder.user_name,post.getUserid());
        nrLikes(viewHolder.likes,post.getRecipeid());
        isFavourite(post.getRecipeid(),viewHolder.favourite);
        isCooked(post.getRecipeid(),viewHolder.cook);




      viewHolder.btn_ingredients.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              viewHolder.btn_ingredients.setTextColor(Color.parseColor("#000000"));
              viewHolder.btn_directions.setTextColor(Color.parseColor("#aaa480"));
              viewHolder.ingredients.setVisibility(View.VISIBLE);
              viewHolder.directions.setVisibility(View.GONE);
          }
      });

        viewHolder.btn_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.btn_ingredients.setTextColor(Color.parseColor("#aaa480"));
                viewHolder.btn_directions.setTextColor(Color.parseColor("#000000"));
                viewHolder.ingredients.setVisibility(View.GONE);
                viewHolder.directions.setVisibility(View.VISIBLE);
            }
        });



        viewHolder.image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid",post.getRecipeid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipeDetailFragment()).commit();
            }
        });


        viewHolder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.favourite.getTag().equals("+add to fav")){
                    FirebaseDatabase.getInstance().getReference().child("Favourites").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Favourites").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });


        viewHolder.cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.cook.getTag().equals("Start Cooking")){
                    FirebaseDatabase.getInstance().getReference().child("CookedRecipes").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("CookedRecipes").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView post_title, cooktimesNr, servings, prepTime, cookTime, user_name, likes, comments,post_desc, ingredients, directions;
        public ImageView image_post, image_profile,back,favourite,cook;
        public Button btn_ingredients,btn_directions;


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
            post_desc = itemView.findViewById(R.id.post_description);
            ingredients = itemView.findViewById(R.id.tv_ingredients);
            directions = itemView.findViewById(R.id.tv_directions);
            btn_ingredients = itemView.findViewById(R.id.btn_ingredients);
            btn_directions = itemView.findViewById(R.id.btn_directions);
            back = itemView.findViewById(R.id.btn_back_detailpost);
            favourite = itemView.findViewById(R.id.add_fav);
           cook = itemView.findViewById(R.id.add_cooking);




        }
    }


    private void userInfo(final ImageView image_profile,final TextView username, final String userid){
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

    private void nrLikes(TextView likes, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+ "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void isFavourite(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Favourites")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
                   imageView.setImageResource(R.drawable.fav_full);
                   imageView.setTag("favourite");
                }else{
                   imageView.setImageResource(R.drawable.fav);
                   imageView.setTag("+add to fav");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isCooked(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("CookedRecipes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.cooking_full);
                    imageView.setTag("Cooked Recipe");
                }else{
                    imageView.setImageResource(R.drawable.cooking);
                    imageView.setTag("Start Cooking");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
