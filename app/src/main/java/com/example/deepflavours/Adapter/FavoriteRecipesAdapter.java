package com.example.deepflavours.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepflavours.Fragment.RecipeDetailFragment;
import com.example.deepflavours.Model.Rating;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRecipesAdapter extends RecyclerView.Adapter<FavoriteRecipesAdapter.ViewHolder> {

    public Context mContext;
    public List<Recipe> mFavRecipe;

    private FirebaseUser firebaseUser;
    private String profileid;
    private String fragmentName;

    public FavoriteRecipesAdapter(Context mContext, List<Recipe> mFavRecipe, String profileid, String fragmentName) {
        this.mContext = mContext;
        this.mFavRecipe = mFavRecipe;
        this.profileid = profileid;
        this.fragmentName = fragmentName;
    }

    @NonNull
    @Override
    public FavoriteRecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_item_profile, viewGroup, false);
        return new FavoriteRecipesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Recipe recipe = mFavRecipe.get(i);
        if (recipe == null && mFavRecipe.size() == 1) {
            mFavRecipe = new ArrayList<>();
        }
        if (recipe != null) {
            viewHolder.favoritepost_title.setText(recipe.getTitle());
            viewHolder.favoritepost_description.setText(recipe.getDescription());
            Glide.with(mContext).load(recipe.getRecipeimage()).into(viewHolder.favoritepost_image);


            nrCooked(viewHolder.cooknr, recipe.getRecipeid());
            calculateRating(recipe.getRecipeid(), viewHolder.ratingbar_recipe);

            viewHolder.favoritepost_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", recipe.getRecipeid());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new RecipeDetailFragment(fragmentName, recipe.getRecipeid(), profileid)).commit();
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return mFavRecipe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView CardView0;
        public CardView CardView1;
        public ImageView favoritepost_image;
        public TextView favoritepost_title, favoritepost_description, cooknr, cooktimes_text;
        public RatingBar ratingbar_recipe;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CardView0 = itemView.findViewById(R.id.favoritepost_CardView0);
            CardView1 = itemView.findViewById(R.id.favoritepost_CardView1);
            favoritepost_image = itemView.findViewById(R.id.favoritepost_Image);
            favoritepost_title = itemView.findViewById(R.id.favoritepost_title);
            favoritepost_description = itemView.findViewById(R.id.favoritepost_description);
            cooknr = itemView.findViewById(R.id.cooktimes_nr_cooked);
            cooktimes_text = itemView.findViewById(R.id.cooktimes_text);
            ratingbar_recipe = itemView.findViewById(R.id.ratingbar_recipe_cooked);

        }
    }


    private void nrCooked(TextView cook, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("CookedRecipes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cook.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void calculateRating(String postid, RatingBar ratingBar) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ratings").child(postid);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sum = 0;
                int ratingValue = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rating rating = snapshot.getValue(Rating.class);
                    sum = sum + rating.getUserRatingValue();
                }
                if ((int) dataSnapshot.getChildrenCount() > 0) {
                    ratingValue = sum / (int) dataSnapshot.getChildrenCount();
                }
                ratingBar.setRating(ratingValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
