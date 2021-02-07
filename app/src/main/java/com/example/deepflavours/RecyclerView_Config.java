package com.example.deepflavours;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.deepflavours.Model.Recipe;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private RecipesAdapter mRecipesAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Recipe> recipes, List<String> keys){
        mContext = context;
        mRecipesAdapter = new RecipesAdapter(recipes, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mRecipesAdapter);
    }



    class RecipeItemView extends RecyclerView.ViewHolder{
        private ImageView imageRecipePost;
       private TextView titleRecipePost;

        private String key;

        public RecipeItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.recipe_item_foodpost_profile,parent,false));

            imageRecipePost = (ImageView) itemView.findViewById(R.id.imageview_foodpost_profile);
            titleRecipePost = (TextView) itemView.findViewById(R.id.textview_foodpost_profile);

        }

        public void bind(Recipe recipe, String key){
            titleRecipePost.setText(recipe.getTitle());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(mContext).load(recipe.getRecipeimage()).apply(options).into(imageRecipePost);
            this.key=key;
        }

    }

    class RecipesAdapter extends RecyclerView.Adapter<RecipeItemView>{
        List<Recipe> mRecipeList;
        List<String> mKeys;

        public RecipesAdapter(List<Recipe> mRecipeList, List<String> mKeys) {
            this.mRecipeList = mRecipeList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public RecipeItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecipeItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeItemView holder, int position) {
            holder.bind(mRecipeList.get(position),mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mRecipeList.size();
        }
    }

}
