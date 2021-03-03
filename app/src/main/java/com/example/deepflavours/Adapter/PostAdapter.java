package com.example.deepflavours.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepflavours.Fragment.HomeFragment;
import com.example.deepflavours.Fragment.RecipeDetailFragment;
import com.example.deepflavours.Login;
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
    List<String> cookedRecipesIds= new ArrayList<>();

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



        isLiked(post.getRecipeid(),viewHolder.like);

        nrLikes(viewHolder.likes,post.getRecipeid());

        nrCooked(viewHolder.cooknr,post.getRecipeid());

        isSaved(post.getRecipeid(),viewHolder.save);



//        viewHolder.save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(viewHolder.save.getTag().equals("save")){
//                    FirebaseDatabase.getInstance().getReference().child("Saves").child(post.getRecipeid())
//                            .child(firebaseUser.getUid()).setValue(true);
//                }else{
//                    FirebaseDatabase.getInstance().getReference().child("Saves").child(post.getRecipeid())
//                            .child(firebaseUser.getUid()).removeValue();
//                }
//            }
//        });


        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CookedRecipes").child(post.getRecipeid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isCookedByCurrentUser = false;


                        for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                            String currentUserId =FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if(userSnapshot.getKey().equals(currentUserId)){
                                isCookedByCurrentUser = true;

                            }
                        }

                        if(!isCookedByCurrentUser  && viewHolder.save.getTag().equals("save")){
                            FirebaseDatabase.getInstance().getReference().child("Saves").child(post.getRecipeid())
                                    .child(firebaseUser.getUid()).setValue(true);

                        }else if(viewHolder.save.getTag().equals("saved")){
                            FirebaseDatabase.getInstance().getReference().child("Saves").child(post.getRecipeid())
                                    .child(firebaseUser.getUid()).removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        viewHolder.imagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid",post.getRecipeid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipeDetailFragment("HomeFragment",post.getRecipeid())).commit();
            }
        });


        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getRecipeid())
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

       public CardView postItem_CardView;
       public TextView titlePostItem ,cooknr, textView_CookThis,likes;
       public ImageView imagePost,like,save;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postItem_CardView = itemView.findViewById(R.id.post_CardView);
            titlePostItem = itemView.findViewById(R.id.post_titleCardView);
            cooknr = itemView.findViewById(R.id.cooktimes_nr);
            textView_CookThis = itemView.findViewById(R.id.tv_timesCook);
            imagePost = itemView.findViewById(R.id.image_Post_CardView);
            likes = itemView.findViewById(R.id.nr_like_post);
            like = itemView.findViewById(R.id.like_post);
            save = itemView.findViewById(R.id.save_post);
        }
    }

    private void isLiked(String postid, ImageView imageView ){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
                    imageView.setTag("liked");
                }else{
                    imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    imageView.setTag("like");
                }
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

    private void nrCooked(TextView cook, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("CookedRecipes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cook.setText(dataSnapshot.getChildrenCount()+ "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void isSaved(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Saves")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_baseline_turned_in_24_white);
                    imageView.setTag("saved");
                }else{
                    imageView.setImageResource(R.drawable.ic_baseline_turned_in_not_24);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
