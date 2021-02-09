package com.example.deepflavours.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context mContext;
    public List<Recipe> mPost;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Recipe> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_home_fragment,viewGroup,false);
        return new PostAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
      firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
      Recipe post = mPost.get(i);

        viewHolder.titlePostItem.setText(post.getTitle());
        Glide.with(mContext).load(post.getRecipeimage()).into(viewHolder.imagePost);

        isLiked(post.getRecipeid(), viewHolder.like);
        nrLikes(viewHolder.nrLikePost, post.getNrlikes());

        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.like.getTag().equals("unliked")){
                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid())
                            .child("likedRecipe").child(post.getRecipeid()).setValue(true);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Recipe recipe = snapshot.getValue(Recipe.class);
                                if(post.getRecipeid().equals(recipe.getRecipeid())) {
                                    int nrLikes = recipe.getNrlikes() + 1;
                                    FirebaseDatabase.getInstance().getReference().child("Recipes").child(post.getRecipeid())
                                            .child("nrlikes").setValue(nrLikes);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid())
                            .child("likedRecipe").child(post.getRecipeid()).removeValue();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Recipe recipe = snapshot.getValue(Recipe.class);
                                if(post.getRecipeid().equals(recipe.getRecipeid())) {
                                    if(recipe.getNrlikes()>0) {
                                        int nrLikes = recipe.getNrlikes() - 1;
                                        FirebaseDatabase.getInstance().getReference().child("Recipes").child(post.getRecipeid())
                                                .child("nrlikes").setValue(nrLikes);
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
        });

    }

    @Override
    public int getItemCount() {
       return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

       public CardView postItem_CardView;
       public TextView titlePostItem ,manyTimes, textView_CookThis,nrLikePost;
       public ImageView imagePost,like,save;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postItem_CardView = itemView.findViewById(R.id.post_CardView);
            titlePostItem = itemView.findViewById(R.id.post_titleCardView);
            manyTimes = itemView.findViewById(R.id.post_manyTimes);
            textView_CookThis = itemView.findViewById(R.id.tv_timesCook);
            imagePost = itemView.findViewById(R.id.image_Post_CardView);
            nrLikePost = itemView.findViewById(R.id.nr_like_post);
            like = itemView.findViewById(R.id.like_post);
            save = itemView.findViewById(R.id.save_post);
        }
    }


    private void isLiked(String postid,ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(firebaseUser.getUid())
                .child("likedRecipe")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
                        imageView.setTag("liked");
                    }else{
                        imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        imageView.setTag("unliked");
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void nrLikes(TextView likes,int nrlikes){
        likes.setText(nrlikes+ "");
    }



}
