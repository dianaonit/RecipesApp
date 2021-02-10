package com.example.deepflavours.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class FavoriteRecipesAdapter extends RecyclerView.Adapter<FavoriteRecipesAdapter.ViewHolder> {

    public Context mContext;
    public List<Recipe> mFavRecipe;

    private FirebaseUser firebaseUser;

    public FavoriteRecipesAdapter(Context mContext, List<Recipe> mFavRecipe) {
        this.mContext = mContext;
        this.mFavRecipe = mFavRecipe;
    }

    @NonNull
    @Override
    public FavoriteRecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_item_profile,viewGroup,false);
        return new FavoriteRecipesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe recipe =mFavRecipe.get(i);

        viewHolder.favoritepost_title.setText(recipe.getTitle());
        viewHolder.favoritepost_description.setText(recipe.getDescription());
        Glide.with(mContext).load(recipe.getRecipeimage()).into(viewHolder.favoritepost_image);
    }



    @Override
    public int getItemCount() {
        return mFavRecipe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView CardView0;
        public CardView CardView1;
        public ImageView favoritepost_image;
        public TextView favoritepost_title ,favoritepost_description,cooktimes_nr,cooktimes_text;
        public RatingBar ratingbar_recipe;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CardView0 = itemView.findViewById(R.id.favoritepost_CardView0);
            CardView1 = itemView.findViewById(R.id.favoritepost_CardView1);
            favoritepost_image =itemView.findViewById(R.id.favoritepost_Image);
            favoritepost_title=itemView.findViewById(R.id.favoritepost_title);
            favoritepost_description=itemView.findViewById(R.id.favoritepost_description);
            cooktimes_nr=itemView.findViewById(R.id.cooktimes_nr);
            cooktimes_text=itemView.findViewById(R.id.cooktimes_text);
            ratingbar_recipe=itemView.findViewById(R.id.ratingbar_recipe);

        }
    }


}