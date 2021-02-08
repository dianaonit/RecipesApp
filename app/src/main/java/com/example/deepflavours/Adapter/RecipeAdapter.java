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

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public Context mContext;
    public List<Recipe> mRecipe;

    private FirebaseUser firebaseUser;

    public RecipeAdapter(Context mContext, List<Recipe> mRecipe) {
        this.mContext = mContext;
        this.mRecipe = mRecipe;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_item_foodpost_profile,viewGroup,false);
        return new RecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe recipe =mRecipe.get(i);

        viewHolder.recipePost_Title.setText(recipe.getTitle());
        Glide.with(mContext).load(recipe.getRecipeimage()).into(viewHolder.recipePost_Image);

    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView recipePost_CardView;
        public ImageView recipePost_Image;
        public TextView recipePost_Title;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipePost_CardView = itemView.findViewById(R.id.carview_foodpost_profile);
            recipePost_Image = itemView.findViewById(R.id.imageview_foodpost_profile);
            recipePost_Title = itemView.findViewById(R.id.textview_foodpost_profile);


        }
    }


}
