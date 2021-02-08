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
import com.example.deepflavours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    }

    @Override
    public int getItemCount() {
       return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

       public CardView postItem_CardView;
       public TextView titlePostItem ,manyTimes, textView_CookThis;
       public ImageView imagePost,like,save;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postItem_CardView = itemView.findViewById(R.id.post_CardView);
            titlePostItem = itemView.findViewById(R.id.post_titleCardView);
            manyTimes = itemView.findViewById(R.id.post_manyTimes);
            textView_CookThis = itemView.findViewById(R.id.tv_timesCook);
            imagePost = itemView.findViewById(R.id.image_Post_CardView);
            like = itemView.findViewById(R.id.like_post);
            save = itemView.findViewById(R.id.save_post);
        }
    }



}
