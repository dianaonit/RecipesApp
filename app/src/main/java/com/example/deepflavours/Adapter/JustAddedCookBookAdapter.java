package com.example.deepflavours.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

public class JustAddedCookBookAdapter extends RecyclerView.Adapter<JustAddedCookBookAdapter.ViewHolder>{

    public Context mContext;
    public List<Recipe> mSaveRecipe;

    private FirebaseUser firebaseUser;

    public JustAddedCookBookAdapter(Context mContext, List<Recipe> mSaveRecipe) {
        this.mContext = mContext;
        this.mSaveRecipe = mSaveRecipe;
    }

    @NonNull
    @Override
    public JustAddedCookBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cookbook_justadded,viewGroup,false);
        return new JustAddedCookBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JustAddedCookBookAdapter.ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe recipe =mSaveRecipe.get(i);

        viewHolder.title_item_cookbook.setText(recipe.getTitle());
        viewHolder.description_item_cookbook.setText(recipe.getDescription());
        Glide.with(mContext).load(recipe.getRecipeimage()).into(viewHolder.savepostImage_justadded);



        nrCooked(viewHolder.cooknr,recipe.getRecipeid());
        calculateRating(recipe.getRecipeid(),viewHolder.ratingbar_recipe);

        viewHolder.savepostImage_justadded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid",recipe.getRecipeid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipeDetailFragment("SaveFragment",recipe.getRecipeid())).commit();
            }
        });

    }



    @Override
    public int getItemCount() {
        return mSaveRecipe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView CardView;
        public CardView CardView1;
        public ImageView savepostImage_justadded;
        public TextView title_cardview1 ,title_item_cookbook,cooknr,cooktimes_text,description_item_cookbook;
        public RatingBar ratingbar_recipe;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CardView = itemView.findViewById(R.id.savepost_CardView);
            CardView1 = itemView.findViewById(R.id.cardview_justadded);
            savepostImage_justadded =itemView.findViewById(R.id.imageview_cookbook_justadded);
            title_cardview1=itemView.findViewById(R.id.title_cardview_justadded);
            title_item_cookbook=itemView.findViewById(R.id.title_cookbook_justadded);
            description_item_cookbook=itemView.findViewById(R.id.description_cookbook_justadded);
            cooknr=itemView.findViewById(R.id.cooktimes_nr_save);
            cooktimes_text=itemView.findViewById(R.id.cooktimes_text);
            ratingbar_recipe=itemView.findViewById(R.id.ratingbar_recipe_save);

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
                if((int) dataSnapshot.getChildrenCount()>0) {
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
