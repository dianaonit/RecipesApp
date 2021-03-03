package com.example.deepflavours.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class SearchRecipesAdapter  extends RecyclerView.Adapter<SearchRecipesAdapter.ViewHolder>{

    public Context mContext;
    public List<Recipe> mRecipe;

    private FirebaseUser firebaseUser;

    public SearchRecipesAdapter(Context mContext, List<Recipe> mRecipe) {
        this.mContext = mContext;
        this.mRecipe = mRecipe;
    }

    @NonNull
    @Override
    public SearchRecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe_search,viewGroup,false);
        return new SearchRecipesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecipesAdapter.ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe recipe =mRecipe.get(i);

        viewHolder.recipeSearch_Title.setText(recipe.getTitle());
        viewHolder.recipeSearch_Desc.setText(recipe.getDescription());
        Glide.with(mContext).load(recipe.getRecipeimage()).into(viewHolder.recipeSearch_Image);

        viewHolder.item_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid",recipe.getRecipeid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipeDetailFragment("SearchFragment",recipe.getRecipeid())).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView recipeSearch_Image;
        public TextView recipeSearch_Title,recipeSearch_Desc;
        public LinearLayout item_recipe;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            item_recipe=itemView.findViewById(R.id.item_recipeSearch);
            recipeSearch_Image= itemView.findViewById(R.id.recipesearch_image);
            recipeSearch_Title = itemView.findViewById(R.id.serach_recipe_title);
            recipeSearch_Desc = itemView.findViewById(R.id.search_recipe_description);


        }
    }
}
