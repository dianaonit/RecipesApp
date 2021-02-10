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
    }



    @Override
    public int getItemCount() {
        return mSaveRecipe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView CardView;
        public CardView CardView1;
        public ImageView savepostImage_justadded;
        public TextView title_cardview1 ,title_item_cookbook,cooktimes_nr,cooktimes_text,description_item_cookbook;
        public RatingBar ratingbar_recipe;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CardView = itemView.findViewById(R.id.savepost_CardView);
            CardView1 = itemView.findViewById(R.id.cardview_justadded);
            savepostImage_justadded =itemView.findViewById(R.id.imageview_cookbook_justadded);
            title_cardview1=itemView.findViewById(R.id.title_cardview_justadded);
            title_item_cookbook=itemView.findViewById(R.id.title_cookbook_justadded);
            description_item_cookbook=itemView.findViewById(R.id.description_cookbook_justadded);
            cooktimes_nr=itemView.findViewById(R.id.cooktimes_nr);
            cooktimes_text=itemView.findViewById(R.id.cooktimes_text);
            ratingbar_recipe=itemView.findViewById(R.id.ratingbar_recipe);

        }
    }

}
