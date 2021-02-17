package com.example.deepflavours.Adapter;

import android.content.Context;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.deepflavours.Fragment.RecipeDetailFragment;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AllMyPostsAdapter extends RecyclerView.Adapter<AllMyPostsAdapter.ViewHolder>{



    public Context mContext;
    public List<Recipe> mRecipe;

    private FirebaseUser firebaseUser;

    public AllMyPostsAdapter(Context mContext, List<Recipe> mRecipe) {
        this.mContext = mContext;
        this.mRecipe = mRecipe;
    }

    @NonNull
    @Override
    public AllMyPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_showall,viewGroup,false);
        return new AllMyPostsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllMyPostsAdapter.ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe recipe =mRecipe.get(i);

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


        public ImageView recipePost_Image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipePost_Image = itemView.findViewById(R.id.imageview_allfoodpost_profile);

        }
    }


}
