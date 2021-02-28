package com.example.deepflavours.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepflavours.Fragment.RecipeDetailFragment;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MostLikedRecipeAdapter extends RecyclerView.Adapter<MostLikedRecipeAdapter.ViewHolder> {

    public Context mContext;
    public List<Recipe> mRecipe;

    private FirebaseUser firebaseUser;

    public MostLikedRecipeAdapter(Context mContext, List<Recipe> mRecipe) {
        this.mContext = mContext;
        this.mRecipe = mRecipe;
    }

    @NonNull
    @Override
    public MostLikedRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mostlikedrecipe_searchfragment,viewGroup,false);
        return new MostLikedRecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostLikedRecipeAdapter.ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe recipe =mRecipe.get(i);

        viewHolder.recipePost_Title.setText(recipe.getTitle());
        Glide.with(mContext).load(recipe.getRecipeimage()).into(viewHolder.recipePost_Image);

        viewHolder.recipePost_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid",recipe.getRecipeid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipeDetailFragment()).commit();
            }
        });


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

            recipePost_CardView = itemView.findViewById(R.id.post_CardView);
            recipePost_Image = itemView.findViewById(R.id.image_Post_CardView);
            recipePost_Title = itemView.findViewById(R.id.post_titleCardView);


        }
    }


}
