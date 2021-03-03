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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PopularRecipesAdapter extends RecyclerView.Adapter<PopularRecipesAdapter.ViewHolder> {

    public Context mContext;
    public List<Recipe> mRecipe;

    private FirebaseUser firebaseUser;

    public PopularRecipesAdapter(Context mContext, List<Recipe> mRecipe) {
        this.mContext = mContext;
        this.mRecipe = mRecipe;
    }

    @NonNull
    @Override
    public PopularRecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popular_recipes,viewGroup,false);
        return new PopularRecipesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularRecipesAdapter.ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe recipe =mRecipe.get(i);

        viewHolder.recipePost_Title.setText(recipe.getTitle());
        viewHolder.recipePost_Desc.setText(recipe.getDescription());
        Glide.with(mContext).load(recipe.getRecipeimage()).into(viewHolder.recipePost_Image);

        nrCooked(viewHolder.cooknr,recipe.getRecipeid());

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
        public TextView recipePost_Title,recipePost_Desc,cooknr;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipePost_Image = itemView.findViewById(R.id.popular_recipe_image);
            recipePost_Title = itemView.findViewById(R.id.popular_recipe_title);
            recipePost_Desc = itemView.findViewById(R.id.popular_recipe_description);
            cooknr = itemView.findViewById(R.id.cooktimes_nr);


        }
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





}
